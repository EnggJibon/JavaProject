/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.MailSender;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.language.MstLanguage;
import com.kmcj.karte.resources.language.MstLanguageService;
import com.kmcj.karte.resources.timezone.MstTimezone;
import com.kmcj.karte.resources.timezone.MstTimezoneService;
import com.kmcj.karte.resources.authorization.MstAuth;
import com.kmcj.karte.resources.authorization.MstAuthService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.password.policy.CnfPasswordPolicyService;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.SafeHashGenerator;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author f.kitaoji
 */
@Dependent
//@Transactional
public class MstUserService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private MstLanguageService mstLanguageService;

    @Inject
    private MstTimezoneService mstTimezoneService;

    @Inject
    private MstAuthService mstAuthService;

    @Inject
    private MstCompanyService mstCompanyService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private CnfPasswordPolicyService cnfPasswordPolicyService;
    
    @Inject
    private MailSender mailSender;
    
    public MstUser getMstUser(String userId) {
        Query query = entityManager.createNamedQuery("MstUser.findByUserId");
        query.setParameter("userId", userId);
        try {
            MstUser user = (MstUser)query.getSingleResult();
            return user;
        }
        catch (NoResultException e) {
            return null;
        }
    }
    
    public MstUserList getSingleMstUser(String userId, String displayTimeZone) {
        Query query = entityManager.createNamedQuery("MstUser.findByUserId");
        query.setParameter("userId", userId);
        List list = query.getResultList();
        for (Object mstUser: list) {
            ((MstUser)mstUser).setDisplayTimeZone(displayTimeZone);
            ((MstUser)mstUser).setHashedPassword("*****");
        }
        MstUserList response = new MstUserList();
        response.setMstUsers(list);
        return response;
    }
    
    public MstUserList getMstUsers(String displayTimeZone) {
        Query query = entityManager.createNamedQuery("MstUser.findAll");
        List list = query.getResultList();
        for (Object mstUser: list) {
            ((MstUser)mstUser).setDisplayTimeZone(displayTimeZone);
            ((MstUser)mstUser).setHashedPassword("*****");
        }
        MstUserList response = new MstUserList();
        response.setMstUsers(list);
        return response;
    }

    @Transactional
    public void createMstUser(MstUser mstUser) {
        if (mstUser.getUuid() == null) {
            mstUser.setUuid(IDGenerator.generate());
        }
        if (mstUser.getCompanyId() != null) {
            if (mstUser.getCompanyId().trim().equals("")) {
                mstUser.setCompanyId(null);
            }
        }
        if (!"1".equals(mstUser.getValidFlg())) {
            mstUser.setValidFlg("0");
        }
        entityManager.persist(mstUser);
    }

    @Transactional
    public void updateMstUser(MstUser mstUser) {
        entityManager.merge(mstUser);
    }
    
    @Transactional
    public int updateMstUserByQuery(MstUser mstUser) {
        Query query = entityManager.createNamedQuery("MstUser.updateByUserId");
        query.setParameter("userId", mstUser.getUserId());
        query.setParameter("userName", mstUser.getUserName());
        query.setParameter("mailAddress", mstUser.getMailAddress());
        query.setParameter("timezone", mstUser.getTimezone());
        query.setParameter("langId", mstUser.getLangId());
        query.setParameter("authId", mstUser.getAuthId());
        query.setParameter("updateDate", mstUser.getUpdateDate());
        query.setParameter("updateUserUuid", mstUser.getUpdateUserUuid());
        query.setParameter("indefiniteFlg", mstUser.getIndefiniteFlg());
        query.setParameter("indefiniteFlg2", mstUser.getIndefiniteFlg()); //無期限フラグがONならパスワード有効期限をNULLクリアする(CASE文に渡す)
        if (mstUser.getCompanyId() != null) {
            query.setParameter("companyId", mstUser.getCompanyId().trim().equals("") ? null : mstUser.getCompanyId());
        }
        else {
            query.setParameter("companyId", mstUser.getCompanyId());
        }
        
        query.setParameter("department", mstUser.getDepartment());
        query.setParameter("procCd", mstUser.getProcCd());
        if ("1".equals(mstUser.getValidFlg())) {
            query.setParameter("validFlg", mstUser.getValidFlg());
        } else {
            query.setParameter("validFlg", "0");
        }
        int cnt = query.executeUpdate();
        return cnt;
    }
    
    @Transactional
    public int resetPassword(MstUser mstUser) {
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        mstUser.setResetPassword(newPassword);
        mstUser.setHashedPassword(SafeHashGenerator.getStretchedPassword(newPassword, mstUser.getUserId()));
        mstUser.setPasswordChangedAt(new java.util.Date());
        //パスワード有効期限
        if (mstUser.getIndefiniteFlg() != null && mstUser.getIndefiniteFlg() == 1) {
            //無期限の指定のあるとき
            mstUser.setPasswordExpiresAt(null); //有効期限はなし
        }
        else {
            mstUser.setPasswordExpiresAt(cnfPasswordPolicyService.getPasswordExpiringDate());
        }
        //初期パスワードフラグを立てる
        mstUser.setInitialPasswordFlg(1);
        //ログイン失敗回数リセット
        mstUser.setLoginFailCount(0);
        //UPDATE文実行
        Query query = entityManager.createNamedQuery("MstUser.updatePassword");
        query.setParameter("userId", mstUser.getUserId());
        query.setParameter("hashedPassword", mstUser.getHashedPassword());
        query.setParameter("passwordChangedAt", mstUser.getPasswordChangedAt());
        query.setParameter("passwordExpiresAt", mstUser.getPasswordExpiresAt());
        query.setParameter("initialPasswordFlg", mstUser.getInitialPasswordFlg());
        query.setParameter("loginFailCount", mstUser.getLoginFailCount());
        query.setParameter("accountLocked", 0); //アカウントロック解除
        int cnt = query.executeUpdate();
        return cnt;
    }
    
    /**
     * ユーザーが自分自身でパスワードリセット、メール送信
     * @param userId
     * @return 
     */
    @Transactional
    public BasicResponse selfResetPassword(String userId, String langId) {
        BasicResponse response = new BasicResponse();
        MstUser mstUser = getMstUser(userId);
        if (mstUser == null || mstUser.getMailAddress() == null || mstUser.getMailAddress().equals("")) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_mail_address_not_found"));
        }
        else {
            String newPassword = RandomStringUtils.randomAlphanumeric(10);
            mstUser.setResetPassword(newPassword);
            mstUser.setHashedPassword(SafeHashGenerator.getStretchedPassword(newPassword, mstUser.getUserId()));
            mstUser.setPasswordChangedAt(new java.util.Date());
            //パスワード有効期限
            if (mstUser.getIndefiniteFlg() != null && mstUser.getIndefiniteFlg() == 1) {
                //無期限の指定のあるとき
                mstUser.setPasswordExpiresAt(null); //有効期限はなし
            }
            else {
                mstUser.setPasswordExpiresAt(cnfPasswordPolicyService.getPasswordExpiringDate());
            }
            //初期パスワードフラグを立てる
            mstUser.setInitialPasswordFlg(1);
            //ログイン失敗回数リセット
            mstUser.setLoginFailCount(0);
            //UPDATE文実行
            Query query = entityManager.createNamedQuery("MstUser.updatePassword");
            query.setParameter("userId", mstUser.getUserId());
            query.setParameter("hashedPassword", mstUser.getHashedPassword());
            query.setParameter("passwordChangedAt", mstUser.getPasswordChangedAt());
            query.setParameter("passwordExpiresAt", mstUser.getPasswordExpiresAt());
            query.setParameter("initialPasswordFlg", mstUser.getInitialPasswordFlg());
            query.setParameter("loginFailCount", mstUser.getLoginFailCount());
            query.setParameter("accountLocked", 0); //アカウントロック解除
            query.executeUpdate();
            //メール送信
            //件名
            String mailSubject = mstDictionaryService.getDictionaryValue(mstUser.getLangId(), "mail_subject_reset_password");
            //本文
            StringBuilder mailBody = new StringBuilder();
            mailBody.append(mstDictionaryService.getDictionaryValue(mstUser.getLangId(), "mail_body_reset_password"));
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            mailBody.append(newPassword);
            List<String> toList = new ArrayList<>();
            toList.add(mstUser.getMailAddress());
            //メール送信
            try {
                mailSender.setMakePlainTextBody(true);
                mailSender.sendMail(toList, null, mailSubject, mailBody.toString());
            }
            catch (Exception e) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("Failed to send email.");
            }
        }
        return response;
    }

    @Transactional
    public int deleteMstUser(String userId) {
        Query query = entityManager.createNamedQuery("MstUser.delete");
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }

    /**
     *
     * @param UserId
     * @return
     */
    public boolean getMstUserExistCheck(String UserId) {
        Query query = entityManager.createNamedQuery("MstUser.findByUserId");
        query.setParameter("userId", UserId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean checkCsvFileData(String lineCsv[], String userLangId, String logFile, int index) {

        //ログ出力内容を用意する
        String lineNo = mstDictionaryService.getDictionaryValue(userLangId, "row_number");
        String userId = mstDictionaryService.getDictionaryValue(userLangId, "user_id");
        String userName = mstDictionaryService.getDictionaryValue(userLangId, "user_name");
        String mailAddress = mstDictionaryService.getDictionaryValue(userLangId, "mail_address");
        String lang = mstDictionaryService.getDictionaryValue(userLangId, "lang");
        String timezoneName = mstDictionaryService.getDictionaryValue(userLangId, "timezone_name");
        String authGroup = mstDictionaryService.getDictionaryValue(userLangId, "auth_group");
        String companyName = mstDictionaryService.getDictionaryValue(userLangId, "company_name");
        String department = mstDictionaryService.getDictionaryValue(userLangId, "user_department");
        String validFlg = mstDictionaryService.getDictionaryValue(userLangId, "user_valid_flg");
    
        String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
        String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");

        int arrayLength = lineCsv.length;
        FileUtil fu = new FileUtil();
        if (arrayLength != 11) {
            //エラー情報をログファイルに記入
            String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, userId, "", error, 1, errorContents, layout));
            return false;
        }

        //分割した文字をObjectに格納する
        String strUserId = lineCsv[0].trim();
        if (fu.isNullCheck(strUserId)) {
            //エラー情報をログファイルに記入
            String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, userId, strUserId, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strUserId, 45)) {
            //エラー情報をログファイルに記入
            String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, userId, strUserId, error, 1, errorContents, maxLangth));
            return false;
        }

        String strUserName = lineCsv[1].trim();
        if (fu.isNullCheck(strUserName)) {
            //エラー情報をログファイルに記入
            String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, userName, strUserName, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strUserName, 45)) {
            //エラー情報をログファイルに記入
            String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, userName, strUserName, error, 1, errorContents, maxLangth));
            return false;
        }

        String strMailAddress = lineCsv[2].trim();
        if (fu.maxLangthCheck(strMailAddress, 100)) {
            //エラー情報をログファイルに記入
            String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, mailAddress, strMailAddress, error, 1, errorContents, maxLangth));
            return false;
        }

        String strLang = lineCsv[3].trim();
        if (fu.isNullCheck(strLang)) {
            //エラー情報をログファイルに記入
            String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, lang, strLang, error, 1, errorContents, nullMsg));
            return false;
        } else {
           MstLanguage langResponse = mstLanguageService.getByLangLanguage(strLang);
            if (langResponse == null) {
                String invalidData = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_value_invalid");
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, lang, strLang, error, 1, errorContents, invalidData));
                return false;
            }
        }

        String strTimezoneName = lineCsv[4].trim();
        if (fu.isNullCheck(strTimezoneName)) {
            //エラー情報をログファイルに記入
            String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, timezoneName, strTimezoneName, error, 1, errorContents, nullMsg));
            return false;
        } else {
            MstTimezone timezoneResponse = mstTimezoneService.getByTimezoneName(strTimezoneName);
            if (timezoneResponse == null) {
                String invalidData = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_value_invalid");
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, timezoneName, strTimezoneName, error, 1, errorContents, invalidData));
                return false;
            }
        }

        String strAuthGroup = lineCsv[5].trim();
        if (fu.isNullCheck(strAuthGroup)) {
            //エラー情報をログファイルに記入
            String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
           fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, authGroup, strAuthGroup, error, 1, errorContents, nullMsg));
            return false;
        } else {
            MstAuth authResponse = mstAuthService.getByGroupNameAuths(strAuthGroup);
            if (authResponse == null) {
                String invalidData = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_value_invalid");
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, authGroup, strAuthGroup, error, 1, errorContents, invalidData));
                return false;
            }
        }

        String strCompanyName = lineCsv[6].trim();
        if (!(fu.isNullCheck(strCompanyName)) && !"".equals(strCompanyName)) {
            MstCompany companyResponse = mstCompanyService.getByCompanyNameId(strCompanyName);
            if (companyResponse == null) {
                String invalidData = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_value_invalid");
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, companyName, strCompanyName, error, 1, errorContents, invalidData));
                return false;
            }
        }
        
        String strDepartment = lineCsv[7].trim();
        if (!(fu.isNullCheck(strDepartment)) && !"".equals(strDepartment)) {
            MstChoice departmentResponse = mstChoiceService.getByChoiceSeq(strDepartment, userLangId, "mst_user.department");
            if (departmentResponse == null) {
                String invalidData = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_value_invalid");
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, department, strDepartment, error, 1, errorContents, invalidData));
                return false;
            }
        }

        String strValidFlg = lineCsv[9].trim();
        if (fu.isNullCheck(strValidFlg)) {
            //エラー情報をログファイルに記入
            String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, validFlg, strValidFlg, error, 1, errorContents, nullMsg));
            return false;
        } else {
            if (!("0".equals(strValidFlg)) && !("1".equals(strValidFlg))) {
                String invalidData = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_value_invalid");
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, validFlg, strValidFlg, error, 1, errorContents, invalidData));
                return false;
            }
        }
        return true;
    }

    /**
     * @param userId
     * @param userName
     * @param validFlg
     * @param loginUser
     * @return
     */
    public FileReponse getMstUserOutputCsv(String userId, String userName, String validFlg, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        String outUserId = mstDictionaryService.getDictionaryValue(langId, "user_id");
        String outUserName = mstDictionaryService.getDictionaryValue(langId, "user_name");
        String outMailAddress = mstDictionaryService.getDictionaryValue(langId, "mail_address");
        String outLang = mstDictionaryService.getDictionaryValue(langId, "lang");
        String outTimezoneName = mstDictionaryService.getDictionaryValue(langId, "timezone_name");
        String outAuthGroup = mstDictionaryService.getDictionaryValue(langId, "auth_group");
        String outCompanyName = mstDictionaryService.getDictionaryValue(langId, "company_name");
        String outDepartment = mstDictionaryService.getDictionaryValue(langId, "user_department");
        String outProcCd = mstDictionaryService.getDictionaryValue(langId, "proc_cd");
        String outValidFlg = mstDictionaryService.getDictionaryValue(langId, "user_valid_flg");
        String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");

        /*Head*/
        HeadList.add(outUserId);
        HeadList.add(outUserName);
        HeadList.add(outMailAddress);
        HeadList.add(outLang);
        HeadList.add(outTimezoneName);
        HeadList.add(outAuthGroup);
        HeadList.add(outCompanyName);
        HeadList.add(outDepartment);
        HeadList.add(outProcCd);
        HeadList.add(outValidFlg);
        HeadList.add(delete);
        gLineList.add(HeadList);
        //明細データを取得
        List list = searchUserListSql(userId, userName, validFlg);

        MstUserList response = new MstUserList();
        response.setMstUsers(list);
        /*Detail*/
        for (int i = 0; i < response.getMstUsers().size(); i++) {
            lineList = new ArrayList();
            MstUser mstUser = response.getMstUsers().get(i);
            lineList.add(mstUser.getUserId());
            lineList.add(mstUser.getUserName());
            lineList.add(mstUser.getMailAddress());
            MstLanguage langResponse = mstLanguageService.getByIDLanguage(mstUser.getLangId());
            lineList.add(langResponse.getLang());
            MstTimezone timezoneResponse = mstTimezoneService.getByIdTimezone(mstUser.getTimezone());
            lineList.add(timezoneResponse.getTimezoneName());
            MstAuth authResponse = mstAuthService.getByIDAuths(mstUser.getAuthId());
            lineList.add(authResponse.getGroupName());
            MstCompany companyResponse = mstCompanyService.getByCompanyIdName(mstUser.getCompanyId());
            if(companyResponse != null){
                lineList.add(companyResponse.getCompanyName());
            } else {
                lineList.add(null);
            }
            MstChoice departmentResponse = mstChoiceService.getBySeqChoice(mstUser.getDepartment(), langId, "mst_user.department");
            if(departmentResponse != null){
                lineList.add(departmentResponse.getChoice());
            } else {
                lineList.add(null);
            }
            lineList.add(mstUser.getProcCd());
            lineList.add(mstUser.getValidFlg());
            lineList.add("");
            gLineList.add(lineList);
        }

        CSVFileUtil csvFileUtil = null;
        try {
            csvFileUtil = new CSVFileUtil(outCsvPath, "csvOutput");
            Iterator<ArrayList> iter = gLineList.iterator();
            while (iter.hasNext()) {
                csvFileUtil.toCSVLine(iter.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_USER);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_USER);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_user");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    public List searchUserListSql(String userId, String userName, String validFlg) {
        String sqlUserId = "";
        String sqlUserName = "";
        String sqlValidFlg = null;
        if (!"0".equals(validFlg)) {
            sqlValidFlg = validFlg.trim();
        }
        else {
            sqlValidFlg = "0";
        }
        Query query;
        StringBuilder sql;
        sql = new StringBuilder("SELECT user FROM MstUser user"
              + " LEFT JOIN FETCH user.mstCompany"
              + " JOIN FETCH user.mstAuth "
              + " JOIN FETCH user.mstTimezone"
              + " JOIN FETCH user.mstLanguage"
              + " WHERE 1=1");


        if (userId != null && !"".equals(userId) && userName != null && !"".equals(userName)) {
            sql.append(" AND user.userId like :userId");
            sql.append(" AND user.userName like :userName"); 
            sql.append(" AND user.validFlg = :validFlg");
            sqlUserId = userId.trim();
            sqlUserName = userName.trim();
            query = entityManager.createQuery(sql.toString());
            query.setParameter("userId", "%" + sqlUserId + "%");
            query.setParameter("userName", "%" + sqlUserName + "%");
            query.setParameter("validFlg", sqlValidFlg);
        } else if (userId != null && !"".equals(userId)) {
            sql.append(" AND user.userId like :userId");
            sql.append(" AND user.validFlg = :validFlg");
            sqlUserId = userId.trim();
            query = entityManager.createQuery(sql.toString());
            query.setParameter("userId", "%" + sqlUserId + "%");
            query.setParameter("validFlg", sqlValidFlg);
        } else if (userName != null && !"".equals(userName)) {
            sql.append(" AND user.userName like :userName"); 
            sql.append(" AND user.validFlg = :validFlg");
            sqlUserName = userName.trim();
            query = entityManager.createQuery(sql.toString());
            query.setParameter("userName", "%" + sqlUserName + "%");
            query.setParameter("validFlg", sqlValidFlg);
        } else {
            sql.append(" AND user.validFlg = :validFlg");
            query = entityManager.createQuery(sql.toString());
            query.setParameter("validFlg", sqlValidFlg);
        }
        
        List list = query.getResultList();
        return list;
    }

    /**
     * @param userId
     * @param userName
     * @param validFlg
     * @return
     */
    public CountResponse getMstUserCount(String userId, String userName, String validFlg) {
        List list = countUserListSql(userId, userName, validFlg);
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    public List countUserListSql(String userId, String userName, String validFlg) {
        String sqlUserId = "";
        String sqlUserName = "";
        String sqlValidFlg = validFlg.trim();
        Query query;

        if (userId != null && !"".equals(userId) && userName != null && !"".equals(userName)) {
            sqlUserId = userId.trim();
            sqlUserName = userName.trim();
            query = entityManager.createNamedQuery("MstUser.countByUserIdNameValid");
            query.setParameter("userId", sqlUserId);
            query.setParameter("userName", "%" + sqlUserName + "%");
            query.setParameter("validFlg", sqlValidFlg);
        } else if (userId != null && !"".equals(userId)) {
            sqlUserId = userId.trim();
            query = entityManager.createNamedQuery("MstUser.countByUserIdValid");
            query.setParameter("userId", sqlUserId);
            query.setParameter("validFlg", sqlValidFlg);
        } else if (userName != null && !"".equals(userName)) {
            sqlUserName = userName.trim();
            query = entityManager.createNamedQuery("MstUser.countByUserNameValid");
            query.setParameter("userName", "%" + sqlUserName + "%");
            query.setParameter("validFlg", sqlValidFlg);
        } else {
            query = entityManager.createNamedQuery("MstUser.countByValid");
            query.setParameter("validFlg", sqlValidFlg);
        }
        
        return query.getResultList();
    }
    
    /**
     * 所属IDから、ユーザー情報を取得
     * 
     * @param department
     *
     * @return
     */
    public List<MstUser> getMstUserByDepartment(String department) {
        Query query = entityManager.createNamedQuery("MstUser.findByDepartment");
        query.setParameter("department", department);

        return (List<MstUser>) query.getResultList();
    }

    /**
     * @return the cnfPasswordPolicyService
     */
    public CnfPasswordPolicyService getCnfPasswordPolicyService() {
        return cnfPasswordPolicyService;
    }

    @Transactional
    public void createPasswordHistory(TblPasswordHistory history) {
        if (history == null) return;
        entityManager.persist(history);
    }
    
    @Transactional
    public void updateLoginFailCountAndAccountLocked(MstUser user, int failCount, boolean locked) {
        if (user == null) return;
        Query query = entityManager.createNamedQuery("MstUser.updateLoginFailCountAndAccountLocked");
        query.setParameter("loginFailCount", failCount);
        query.setParameter("accountLocked", locked ? 1 : 0);
        query.setParameter("userId", user.getUserId());
        query.executeUpdate();
    }
    
    public MstUser getMstUserByUuid(String uuid) {
        Query query = entityManager.createNamedQuery("MstUser.findByUserUuid");
        query.setParameter("uuid", uuid);
        try {
            MstUser user = (MstUser)query.getSingleResult();
            return user;
        }
        catch (NoResultException e) {
            return null;
        }
    }
    
}
