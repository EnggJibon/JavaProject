/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.password.policy;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.TblPasswordHistory;
import com.kmcj.karte.util.SafeHashGenerator;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author admin
 */
@Dependent
public class CnfPasswordPolicyService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    private CnfPasswordPolicy cnfPasswordPolicy = null;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    /**
     *
     * @return
     */
    public CnfPasswordPolicyList getCnfPasswordPolicy() {

        Query query = entityManager.createNamedQuery("CnfPasswordPolicy.findAll");
        query.setMaxResults(1);
        CnfPasswordPolicyList cnfPasswordPolicyList = new CnfPasswordPolicyList();
        List list = query.getResultList();

        cnfPasswordPolicyList.setCnfPasswordPolicies(list);
        return cnfPasswordPolicyList;
    }
    
    public CnfPasswordPolicy getCnfPasswordPolicySetting() {
        if (cnfPasswordPolicy != null) {
            return cnfPasswordPolicy;
        }
        else {
            cnfPasswordPolicy = getCnfPasswordPolicy().getCnfPasswordPolicies().get(0);
            return cnfPasswordPolicy;
        }
    }

    public BasicResponse checkPassword(MstUser user, String password, String langId) {
        BasicResponse response = new BasicResponse();
        if (user == null || password == null) {
            return response;
        }
        CnfPasswordPolicy policy = getCnfPasswordPolicySetting();
        //最小桁数チェック
        if (policy.getMinimumLength() > 0) {
            if (password.length() < policy.getMinimumLength()) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                String errMsg = mstDictionaryService.getDictionaryValue(langId, "msg_error_pw_length");
                if (errMsg != null) {
                    errMsg = errMsg.replace("%s", Integer.toString(policy.getMinimumLength()));
                }
                response.setErrorMessage(errMsg);
                return response;
            }
        }
        //パスワード複雑性
        if (policy.getPwComplexity() > 0) {
            String regex = "";
            String dictKey = null;
            if (policy.getPwComplexity() == 1) {
                //英字、数字を両方含める
                regex = "^(?=.*[a-zA-Z])(?=.*[0-9]).*";
                dictKey = "msg_error_pw_complexity01";
            }
            else if (policy.getPwComplexity() == 2) {
                //英字、数字、記号をすべて含める
                regex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[\\p{Punct}]).*";
                dictKey = "msg_error_pw_complexity02";
            }
            else if (policy.getPwComplexity() == 3) {
                //数字、大文字、小文字をすべて含める
                regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).*";
                dictKey = "msg_error_pw_complexity03";
            }
            else if (policy.getPwComplexity() == 4) {
                //数字、大文字、小文字、記号をすべて含める
                regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\\p{Punct}]).*";
                dictKey = "msg_error_pw_complexity04";
            }
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(password);
            boolean meetComplexity = m.find();
            if (!meetComplexity && dictKey != null) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                String errMsg = mstDictionaryService.getDictionaryValue(langId, dictKey);
                response.setErrorMessage(errMsg);
                return response;
            }
        }
        //過去の履歴
        if (policy.getNoReuseHistory() > 0) {
            boolean foundSamePassword = false;
            //1番目の履歴は今のユーザーマスタにあるのでそれと比較
            if (SafeHashGenerator.getStretchedPassword(password, user.getUserId()).equals(user.getHashedPassword())) {
                foundSamePassword = true;
            }
            if (!foundSamePassword) {
                Query query = entityManager.createNamedQuery("TblPasswordHistory.findByUserUuid");
                query.setParameter("userUuid", user.getUuid());
                List<TblPasswordHistory> listHistory = query.getResultList();
                int cnt = 2; //2世代以前を探索
                for (TblPasswordHistory history : listHistory) {
                    //ハッシュ値同士を比較
                    if (SafeHashGenerator.getStretchedPassword(password, user.getUserId()).equals(history.getHashedPassword())) {
                        foundSamePassword = true;
                        break;
                    }
                    cnt++;
                    if (cnt > policy.getNoReuseHistory()) {
                        break;
                    }
                }
            }
            if (foundSamePassword) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                String errMsg = mstDictionaryService.getDictionaryValue(langId, "msg_error_pw_reuse_history");
                if (errMsg != null) {
                    errMsg = errMsg.replace("%s", Integer.toString(policy.getNoReuseHistory()));
                }
                response.setErrorMessage(errMsg);
                return response;
            }
        }
        return response;
    }
    
    
    
    /**
     * パスワードポリシーからパスワード有効期限を算出。現在時刻からの有効期間
     * @return 
     */
    public java.util.Date getPasswordExpiringDate() {
        java.util.Date expiringDate = null;
        java.util.Date currentDate = new java.util.Date();
        java.util.Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        CnfPasswordPolicy policy = getCnfPasswordPolicySetting();
        if (policy.getPwValidPeriod() == 1) {
            //30日
            calendar.add(Calendar.DATE, 30);
            expiringDate = calendar.getTime();
        }
        else if (policy.getPwValidPeriod() == 2) {
            //60日
            calendar.add(Calendar.DATE, 60);
            expiringDate = calendar.getTime();
        }
        else if (policy.getPwValidPeriod() == 3) {
            //90日
            calendar.add(Calendar.DATE, 90);
            expiringDate = calendar.getTime();
        }
        else if (policy.getPwValidPeriod() == 4) {
            //180日
            calendar.add(Calendar.DATE, 180);
            expiringDate = calendar.getTime();
        }
        else if (policy.getPwValidPeriod() == 5) {
            //1年
            calendar.add(Calendar.YEAR, 1);
            expiringDate = calendar.getTime();
        }
        return expiringDate;
    }

    /**
     *
     * @param userUuid
     * @param cnfPasswordPolicy
     * @return
     */
    @Transactional
    public BasicResponse updateCnfPasswordPolicy(String userUuid, CnfPasswordPolicy cnfPasswordPolicy) {
        BasicResponse basicResponse = new BasicResponse();
        CnfPasswordPolicy oldCnfPasswordPolicy = entityManager.find(CnfPasswordPolicy.class, cnfPasswordPolicy.getPolicyId());
        if (oldCnfPasswordPolicy != null) {
            oldCnfPasswordPolicy.setMinimumLength(cnfPasswordPolicy.getMinimumLength());
            oldCnfPasswordPolicy.setNoReuseHistory(cnfPasswordPolicy.getNoReuseHistory());
            oldCnfPasswordPolicy.setPwComplexity(cnfPasswordPolicy.getPwComplexity());
            oldCnfPasswordPolicy.setPwValidPeriod(cnfPasswordPolicy.getPwValidPeriod());
            oldCnfPasswordPolicy.setFailCountToLock(cnfPasswordPolicy.getFailCountToLock());
            oldCnfPasswordPolicy.setUpdateDate(new Date());
            oldCnfPasswordPolicy.setUpdateUserUuid(userUuid);
            entityManager.merge(oldCnfPasswordPolicy);
        }
//        else {
//
//            cnfPasswordPolicy.setPolicyId(IDGenerator.generate());
//            cnfPasswordPolicy.setCreateDate(new Date());
//            cnfPasswordPolicy.setCreateUserUuid(userUuid);
//            cnfPasswordPolicy.setUpdateDate(new Date());
//            cnfPasswordPolicy.setUpdateUserUuid(userUuid);
//            entityManager.persist(cnfPasswordPolicy);
//        }

        return basicResponse;

    }

}
