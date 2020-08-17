/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.apiuser;

import com.kmcj.karte.CountResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.SafeHashGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author f.zds
 *
 */
@Dependent
public class MstApiUserService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    public MstApiUser getApiMstUser(String userId) {
        Query query = entityManager.createNamedQuery("MstApiUser.findByUserId");
        query.setParameter("userId", userId);
        try {
            MstApiUser apiUser = (MstApiUser) query.getSingleResult();
            return apiUser;
        } catch (NoResultException e) {
            return null;
        }
    }

    public MstApiUserVo getSingleMstApiUser(String userId, String displayTimeZone) {
        MstApiUserVo resVo = new MstApiUserVo();
        Query query = entityManager.createNamedQuery("MstApiUser.findByUserId");
        query.setParameter("userId", userId);
        List<MstApiUser> list = query.getResultList();
        if (null != list && !list.isEmpty()) {
            MstApiUser aMstApiUser = list.get(0);
            resVo.setCompanyId(aMstApiUser.getCompanyId());
            resVo.setCompanyName(aMstApiUser.getMstCompany().getCompanyName());
            resVo.setUserId(aMstApiUser.getUserId());
            resVo.setUserName(aMstApiUser.getUserName());
            resVo.setMailAddress(aMstApiUser.getMailAddress());
            resVo.setLangId(aMstApiUser.getLangId());
            resVo.setTimezone(aMstApiUser.getTimezone());
            resVo.setValidFlg(aMstApiUser.getValidFlg().toString());
            resVo.setPasswordChangedAt(null == aMstApiUser.getPasswordChangedAt() ? "" : new FileUtil().getDateTimeFormatForStr(aMstApiUser.getPasswordChangedAt()));
        }

        return resVo;
    }

    /**
     *
     * @param userId
     * @param userName
     * @param companyCode
     * @param companyName
     * @return MstApiUserList
     */
    public CountResponse getApiMstUserCount(String userId, String userName, String companyCode, String companyName) {
        List list = getSql(userId, userName, companyCode, companyName, "count");
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    /**
     *
     * @param userId
     * @param userName
     * @param companyCode
     * @param companyName
     * @return MstApiUserList
     */
    public MstApiUserList getMstApiUsers(String userId, String userName, String companyCode, String companyName) {
        List list = getSql(userId, userName, companyCode, companyName, "");
        MstApiUserList response = new MstApiUserList();
        List<MstApiUserVo> mstApiUserVoList = new ArrayList<>();
        MstApiUserVo mstApiUserVo;
        for (int i = 0; i < list.size(); i++) {
            mstApiUserVo = new MstApiUserVo();
            Object[] objs = (Object[]) list.get(i);
            mstApiUserVo.setCompanyCode(null == objs[0] ? "" : (String) objs[0]);
            mstApiUserVo.setCompanyName(null == objs[1] ? "" : (String) objs[1]);
            mstApiUserVo.setUserId(null == objs[2] ? "" : (String) objs[2]);//ユーザーID
            mstApiUserVo.setUserName(null == objs[3] ? "" : (String) objs[3]);
            mstApiUserVo.setMailAddress(null == objs[4] ? "" : (String) objs[4]);
            mstApiUserVo.setLangName(null == objs[5] ? "" : (String) objs[5]);
            mstApiUserVo.setTimeZoneName(null == objs[6] ? "" : (String) objs[6]);
            if (null != objs[7] && !"".equals(objs[7])) {
                mstApiUserVo.setValidFlg(objs[7].toString());
            }
            if (null != objs[8] && !"".equals(objs[8])) {
                mstApiUserVo.setPasswordChangedAt(new FileUtil().getDateTimeFormatForStr(objs[8]));
            }

            mstApiUserVoList.add(mstApiUserVo);
        }
        response.setMstApiUserVoList(mstApiUserVoList);
        return response;

    }

    public List getSql(String userId, String userName, String companyCode, String companyName, String action) {

        StringBuilder sql;
        String strUserId = "";
        String strUserName = "";
        String strCompanyCode = "";
        String strCompanyName = "";

        if ("count".equals(action)) {
            sql = new StringBuilder("SELECT count(t0.userId) "
                    + " FROM MstApiUser t0 LEFT JOIN MstCompany t1 "
                    + " ON t0.companyId = t1.id"
                    + " Where 1=1 ");
        } else {
            sql = new StringBuilder("SELECT t1.companyCode,t1.companyName,t0.userId,t0.userName,"
                    + " t0.mailAddress,t2.lang,t3.timezoneName,t0.validFlg,t0.passwordChangedAt "
                    + " FROM MstApiUser t0 LEFT JOIN MstCompany t1 "
                    + " ON t0.companyId = t1.id "
                    + " LEFT JOIN MstLanguage t2 "
                    + " ON t0.langId = t2.id "
                    + " LEFT JOIN MstTimezone t3 "
                    + " ON t0.timezone = t3.id "
                    + " Where 1=1 ");
        }

        if (userId != null && !"".equals(userId)) {
            strUserId = userId.trim();
            sql = sql.append(" and t0.userId = :userId ");
        }
        if (userName != null && !"".equals(userName)) {
            strUserName = userName.trim();
            sql = sql.append(" and t0.userName like :userName ");
        }

        if (companyCode != null && !"".equals(companyCode)) {
            strCompanyCode = companyCode.trim();
            sql = sql.append(" and t1.companyCode = :companyCode ");
        }
        if (companyName != null && !"".equals(companyName)) {
            strCompanyName = companyName.trim();
            sql = sql.append(" and t1.companyName like :companyName ");
        }
        sql = sql.append(" Order by t0.mstCompany.companyCode,t0.userId ");    //companyCode,userIdの昇順

        Query query = entityManager.createQuery(sql.toString());
        if (userId != null && !"".equals(userId)) {
            query.setParameter("userId", strUserId);
        }
        if (userName != null && !"".equals(userName)) {
            query.setParameter("userName", "%" + strUserName + "%");
        }
        if (companyCode != null && !"".equals(companyCode)) {
            query.setParameter("companyCode", strCompanyCode);
        }
        if (companyName != null && !"".equals(companyName)) {
            query.setParameter("companyName", "%" + strCompanyName + "%");
        }
        return query.getResultList();

    }

    @Transactional
    public void createMstApiUser(MstApiUser mstApiUser) {
        if (mstApiUser.getUuid() == null) {
            mstApiUser.setUuid(IDGenerator.generate());
        }
        entityManager.persist(mstApiUser);
    }

    @Transactional
    public int updateMstApiUserByQuery(MstApiUser mstApiUser, LoginUser loginUser) {
        MstApiUser oldMstApiUser = entityManager.find(MstApiUser.class, mstApiUser.getUserId());
        if (null == oldMstApiUser) {
            return 0;
        }

        oldMstApiUser.setCompanyId(mstApiUser.getCompanyId());
        oldMstApiUser.setUserId(mstApiUser.getUserId());
        oldMstApiUser.setUserName(mstApiUser.getUserName());
        oldMstApiUser.setMailAddress(mstApiUser.getMailAddress());
        oldMstApiUser.setLangId(mstApiUser.getLangId());
        oldMstApiUser.setTimezone(mstApiUser.getTimezone());
        oldMstApiUser.setValidFlg(mstApiUser.getValidFlg());
        oldMstApiUser.setUpdateDate(new Date());
        oldMstApiUser.setUpdateUserUuid(loginUser.getUserUuid());
        try {
            entityManager.merge(oldMstApiUser);
        } catch (Exception e) {
            return 0;
        }

        return 1;
    }

    @Transactional
    public int resetPassword(MstApiUser mstApiUser) {
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        mstApiUser.setResetPassword(newPassword);
        mstApiUser.setHashedPassword(SafeHashGenerator.getStretchedPassword(newPassword, mstApiUser.getUserId()));
        mstApiUser.setPasswordChangedAt(new java.util.Date());
        Query query = entityManager.createNamedQuery("MstApiUser.updatePassword");
        query.setParameter("userId", mstApiUser.getUserId());
        query.setParameter("hashedPassword", mstApiUser.getHashedPassword());
        query.setParameter("passwordChangedAt", new Date());
        int cnt = query.executeUpdate();
        return cnt;
    }

    @Transactional
    public int deleteMstApiUser(String userId) {
        Query query = entityManager.createNamedQuery("MstApiUser.delete");
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }

}
