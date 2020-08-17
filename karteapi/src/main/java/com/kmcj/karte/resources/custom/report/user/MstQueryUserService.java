/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.user;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.custom.report.TblCustomReportQueryService;
import com.kmcj.karte.resources.custom.report.TblCustomReportQueryVo;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.SafeHashGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

/**
 * レポートクエリユーザーマスタ サービス
 *
 * @author admin
 */
@Dependent
public class MstQueryUserService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    public CountResponse getCountResponse(String userId, String userName, String companyCode, String companyName, String langId) {
        CountResponse countResponse = new CountResponse();

        List list = getSql(userId, userName, companyCode, companyName, "count");
        countResponse.setCount(((Long) list.get(0)));

        long sysCount = 0;
        try {
            CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
            sysCount = Long.parseLong(cnf.getConfigValue());
        } catch (NumberFormatException e) {
        }
        if (countResponse.getCount() > sysCount) {
            countResponse.setError(true);
            countResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            countResponse.setErrorMessage(msg);
        }
        return countResponse;
    }

    public MstQueryUserVoList getMstQueryUserVoList(String userId, String userName, String companyCode, String companyName) {
        List list = getSql(userId, userName, companyCode, companyName, "");
        MstQueryUserVoList response = new MstQueryUserVoList();
        List<MstQueryUserVo> mstQueryUserVoList = new ArrayList<>();
        MstQueryUserVo mstQueryUserVo;
        for (int i = 0; i < list.size(); i++) {
            mstQueryUserVo = new MstQueryUserVo();
            MstQueryUser mstQueryUser = (MstQueryUser) list.get(i);
            if (mstQueryUser.getMstCompany() != null) {
                mstQueryUserVo.setCompanyCode(mstQueryUser.getMstCompany().getCompanyCode());
                mstQueryUserVo.setCompanyName(mstQueryUser.getMstCompany().getCompanyName());
            }
            mstQueryUserVo.setUserId(mstQueryUser.getUserId());//ユーザーID
            mstQueryUserVo.setUserName(mstQueryUser.getUserName());
            if (mstQueryUser.getMstLanguage() != null) {
                mstQueryUserVo.setLangName(mstQueryUser.getMstLanguage().getLang());
            }
            if (mstQueryUser.getMstTimezone() != null) {
                mstQueryUserVo.setTimeZoneName(mstQueryUser.getMstTimezone().getTimezoneName());
            }
            mstQueryUserVo.setMailAddress(mstQueryUser.getMailAddress());
            mstQueryUserVo.setValidFlg(mstQueryUser.getValidFlg());
            mstQueryUserVo.setPasswordChangedAt(new FileUtil().getDateTimeFormatForStr(mstQueryUser.getPasswordChangedAt()));
            mstQueryUserVo.setUuid(null == mstQueryUser.getUuid() ? "" : mstQueryUser.getUuid());

            mstQueryUserVoList.add(mstQueryUserVo);
        }
        response.setMstQueryUserVoList(mstQueryUserVoList);
        return response;

    }

    public MstQueryUserVo getSingleMstQueryUserVo(String userId) {
        MstQueryUserVo resVo = new MstQueryUserVo();
        Query query = entityManager.createNamedQuery("MstQueryUser.findByUserId");
        query.setParameter("userId", userId);
        List<MstQueryUser> list = query.getResultList();
        if (null != list && !list.isEmpty()) {
            MstQueryUser aMstQueryUser = list.get(0);
            resVo.setCompanyId(aMstQueryUser.getCompanyId());
            resVo.setCompanyName(aMstQueryUser.getMstCompany().getCompanyName());
            resVo.setUserId(aMstQueryUser.getUserId());
            resVo.setUserName(aMstQueryUser.getUserName());
            resVo.setMailAddress(aMstQueryUser.getMailAddress());
            resVo.setLangId(aMstQueryUser.getLangId());
            resVo.setTimezone(aMstQueryUser.getTimezone());
            resVo.setValidFlg(aMstQueryUser.getValidFlg());
            resVo.setPasswordChangedAt(null == aMstQueryUser.getPasswordChangedAt() ? "" : new FileUtil().getDateTimeFormatForStr(aMstQueryUser.getPasswordChangedAt()));
        }

        return resVo;
    }

    @Transactional
    public BasicResponse updateMstQueryUser(MstQueryUser mstQueryUser, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        int cnt = updateMstQueryUserByQuery(mstQueryUser, loginUser);
        if (cnt < 1) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return basicResponse;
    }

    @Transactional
    public BasicResponse createMstQueryUser(MstQueryUser mstQueryUser, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        if (getQueryMstUser(mstQueryUser.getUserId()) != null) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
        } else {
            String initialPassword = SafeHashGenerator.getStretchedPassword(mstQueryUser.getUserId(), mstQueryUser.getUserId());
            MstQueryUser mstQueryUsers = new MstQueryUser();
            mstQueryUsers.setUserId(mstQueryUser.getUserId());
            mstQueryUsers.setUserName(mstQueryUser.getUserName());
            mstQueryUsers.setMailAddress(mstQueryUser.getMailAddress());
            mstQueryUsers.setTimezone(mstQueryUser.getTimezone());
            mstQueryUsers.setLangId(mstQueryUser.getLangId());
            mstQueryUsers.setCompanyId(mstQueryUser.getCompanyId());
            mstQueryUsers.setHashedPassword(initialPassword);
            mstQueryUsers.setPasswordChangedAt(null);
            mstQueryUsers.setPasswordExpiresAt(null);
            mstQueryUsers.setValidFlg(mstQueryUser.getValidFlg());
            mstQueryUsers.setCreateUserUuid(loginUser.getUserUuid());
            mstQueryUsers.setCreateDate(new java.util.Date());
            mstQueryUsers.setUpdateUserUuid(loginUser.getUserUuid());
            mstQueryUsers.setUpdateDate(new java.util.Date());

            createMstQueryUser(mstQueryUsers);
        }
        return basicResponse;
    }

    @Transactional
    public BasicResponse deleteMstQueryUser(String uuid, String langId) {
        BasicResponse basicResponse = new BasicResponse();
        Query query = entityManager.createNamedQuery("MstQueryUser.delete");
        query.setParameter("uuid", uuid);
        int cnt = query.executeUpdate();
        if (cnt < 1) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
        }
        return basicResponse;
    }

    @Transactional
    public MstQueryUserVo resetPassword(MstQueryUser mstQueryUser, LoginUser loginUser) {
        MstQueryUserVo mstQueryUserVo = new MstQueryUserVo();

        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        mstQueryUser.setResetPassword(newPassword);
        mstQueryUser.setHashedPassword(SafeHashGenerator.getStretchedPassword(newPassword, mstQueryUser.getUserId()));
        mstQueryUser.setPasswordChangedAt(new java.util.Date());
        Query query = entityManager.createNamedQuery("MstQueryUser.updatePassword");
        query.setParameter("userId", mstQueryUser.getUserId());
        query.setParameter("hashedPassword", mstQueryUser.getHashedPassword());
        query.setParameter("passwordChangedAt", mstQueryUser.getPasswordChangedAt());
        int cnt = query.executeUpdate();

        if (cnt < 1) {
            mstQueryUserVo.setError(true);
            mstQueryUserVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstQueryUserVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
        } else {
            List<MstQueryUser> list = new ArrayList();
            mstQueryUser.setDisplayTimeZone(loginUser.getJavaZoneId());
            mstQueryUser.setHashedPassword(null);
            list.add(mstQueryUser);
            mstQueryUserVo.setMstQueryUser(list);
        }
        return mstQueryUserVo;
    }

    private List getSql(String userId, String userName, String companyCode, String companyName, String action) {
        StringBuilder sql;

        if ("count".equals(action)) {
            sql = new StringBuilder("SELECT COUNT(1) ");
        } else {
            sql = new StringBuilder("SELECT mstQueryUser ");
        }
        sql.append(
                " FROM MstQueryUser mstQueryUser "
                + " LEFT JOIN FETCH mstQueryUser.mstCompany mstCompany "
                + " LEFT JOIN FETCH mstQueryUser.mstTimezone mstTimezone "
                + " LEFT JOIN FETCH mstQueryUser.mstLanguage mstLanguage "
                + " WHERE 1=1  ");

        if (StringUtils.isNotEmpty(userId)) {
            sql = sql.append(" and mstQueryUser.userId like :userId ");
        }
        if (StringUtils.isNotEmpty(userName)) {
            sql = sql.append(" and mstQueryUser.userName like :userName ");
        }
        if (StringUtils.isNotEmpty(companyCode)) {
            sql = sql.append(" and mstCompany.companyCode like :companyCode ");
        }
        if (StringUtils.isNotEmpty(companyName)) {
            sql = sql.append(" and mstCompany.companyName like :companyName ");
        }
        sql = sql.append(" Order by mstCompany.companyCode,mstQueryUser.userId ");    //companyCode,userIdの昇順

        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotEmpty(userId)) {
            query.setParameter("userId", "%" + userId + "%");
        }
        if (StringUtils.isNotEmpty(userName)) {
            query.setParameter("userName", "%" + userName + "%");
        }
        if (StringUtils.isNotEmpty(companyCode)) {
            query.setParameter("companyCode", "%" + companyCode + "%");
        }
        if (StringUtils.isNotEmpty(companyName)) {
            query.setParameter("companyName", "%" + companyName + "%");
        }
        return query.getResultList();
    }

    @Transactional
    private int updateMstQueryUserByQuery(MstQueryUser mstQueryUser, LoginUser loginUser) {
        MstQueryUser oldMstQueryUser = entityManager.find(MstQueryUser.class, mstQueryUser.getUserId());
        if (null == oldMstQueryUser) {
            return 0;
        }
        oldMstQueryUser.setCompanyId(mstQueryUser.getCompanyId());
        oldMstQueryUser.setUserId(mstQueryUser.getUserId());
        oldMstQueryUser.setUserName(mstQueryUser.getUserName());
        oldMstQueryUser.setMailAddress(mstQueryUser.getMailAddress());
        oldMstQueryUser.setLangId(mstQueryUser.getLangId());
        oldMstQueryUser.setTimezone(mstQueryUser.getTimezone());
        oldMstQueryUser.setValidFlg(mstQueryUser.getValidFlg());
        oldMstQueryUser.setUpdateDate(new Date());
        oldMstQueryUser.setUpdateUserUuid(loginUser.getUserUuid());
        try {
            entityManager.merge(oldMstQueryUser);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    private Object getQueryMstUser(String userId) {
        Query query = entityManager.createNamedQuery("MstQueryUser.findByUserId");
        query.setParameter("userId", userId);
        try {
            MstQueryUser queryUser = (MstQueryUser) query.getSingleResult();
            return queryUser;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    private void createMstQueryUser(MstQueryUser mstQueryUser) {
        if (mstQueryUser.getUuid() == null) {
            mstQueryUser.setUuid(IDGenerator.generate());
        }
        entityManager.persist(mstQueryUser);
    }

    /**
     *
     * @param reportId
     * @param tblReportQueryUserVos
     * @param userUuid
     */
    @Transactional
    public void addTblReportQueryUser(long reportId, List<TblReportQueryUserVo> tblReportQueryUserVos, String userUuid) {
        if (tblReportQueryUserVos != null && tblReportQueryUserVos.size() > 0) {
            TblReportQueryUser tblReportQueryUser;
            for (int i = 0; i < tblReportQueryUserVos.size(); i++) {
                TblReportQueryUserVo tblReportQueryUserVo = tblReportQueryUserVos.get(i);

                tblReportQueryUser = new TblReportQueryUser();
                tblReportQueryUser.setReportId(reportId);
                tblReportQueryUser.setQueryUserId(tblReportQueryUserVo.getQueryUserId());
                tblReportQueryUser.setUuid(IDGenerator.generate());

                tblReportQueryUser.setCreateUserUuid(userUuid);
                tblReportQueryUser.setCreateDate(new java.util.Date());
                tblReportQueryUser.setUpdateUserUuid(userUuid);
                tblReportQueryUser.setUpdateDate(new java.util.Date());

                entityManager.persist(tblReportQueryUser);
            }
        }
    }

    /**
     *
     * @param reportId
     * @param tblReportQueryUserVos
     * @param userUuid
     * @param selectedFlag
     */
    @Transactional
    public void updateTblReportQueryUser(long reportId, List<TblReportQueryUserVo> tblReportQueryUserVos, String userUuid, int selectedFlag) {
        if (selectedFlag == 1) {
            Query query = entityManager.createNamedQuery("TblReportQueryUser.deleteByReportId");
            query.setParameter("reportId", reportId);
            query.executeUpdate();
            addTblReportQueryUser(reportId, tblReportQueryUserVos, userUuid);
        }

    }

    /**
     * 公開ユーザ一覧を取得
     *
     * @param reportId
     * @param loginUser
     * @return
     */
    public TblReportQueryUserVoList getOpenUserList(Long reportId, LoginUser loginUser) {
        StringBuilder sql;

        sql = new StringBuilder("SELECT mstQueryUser FROM MstQueryUser mstQueryUser "
                + " LEFT JOIN FETCH mstQueryUser.mstCompany mstCompany ");
        sql.append(" ORDER BY mstCompany.companyName ");
        Query query = entityManager.createQuery(sql.toString());

        List list = query.getResultList();
        List list2 = null;
        Query query2;
        if (reportId != null) {
            sql = new StringBuilder("SELECT tblReportQueryUser FROM TblReportQueryUser tblReportQueryUser "
                    + " WHERE tblReportQueryUser.reportId = :reportId ");
            query2 = entityManager.createQuery(sql.toString());
            query2.setParameter("reportId", reportId);
            list2 = query2.getResultList();
        }

        TblReportQueryUserVoList tblReportQueryUserVoList = new TblReportQueryUserVoList();
        TblReportQueryUserVo tblReportQueryUserVo;
        List<TblReportQueryUserVo> tblReportQueryUserVos = new ArrayList();

        for (int i = 0; i < list.size(); i++) {
            MstQueryUser mstQueryUser = (MstQueryUser) list.get(i);
            tblReportQueryUserVo = new TblReportQueryUserVo();

            tblReportQueryUserVo.setQueryUserId(mstQueryUser.getUuid());
            tblReportQueryUserVo.setUserName(mstQueryUser.getUserName());
            if (mstQueryUser.getMstCompany() != null) {
                tblReportQueryUserVo.setCompanyName(mstQueryUser.getMstCompany().getCompanyName());
            } else {
                tblReportQueryUserVo.setCompanyName("");
            }

            if (list2 != null) {
                for (int j = 0; j < list2.size(); j++) {
                    TblReportQueryUser tblReportQueryUser = (TblReportQueryUser) list2.get(j);
                    if (mstQueryUser.getUuid().equals(tblReportQueryUser.getQueryUserId())) {
                        tblReportQueryUserVo.setCheckFlag(1);
                    }
                }
            }

            tblReportQueryUserVos.add(tblReportQueryUserVo);

        }
        tblReportQueryUserVoList.setTblReportQueryUserVoList(tblReportQueryUserVos);
        return tblReportQueryUserVoList;
    }

    public MstQueryUser getMstQueryUser(String userId) {
        Query query = entityManager.createNamedQuery("MstQueryUser.findByUserId");
        query.setParameter("userId", userId);
        try {
            MstQueryUser mstQueryUser = (MstQueryUser) query.getSingleResult();
            return mstQueryUser;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param tblReportQueryUserVo
     * @param tblCustomReportQueryService
     * @return
     */
    public FileReponse validateQueryUser(TblReportQueryUserVo tblReportQueryUserVo, TblCustomReportQueryService tblCustomReportQueryService) {
        FileReponse response = new FileReponse();
        Long reportId = tblReportQueryUserVo.getReportId();
        String userId = tblReportQueryUserVo.getUserId();
        String password = tblReportQueryUserVo.getPassword();
        String langId = tblReportQueryUserVo.getLangId();

        if (reportId == null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_not_null_with_item");
            String param = mstDictionaryService.getDictionaryValue(langId, "param");
            msg = String.format(msg, param + ":reportId");
            response.setErrorMessage(msg);
            return response;
        }

        Query query = entityManager.createNamedQuery("TblReportQueryUser.findByReportId");
        query.setParameter("reportId", reportId);
        query.setParameter("userId", userId);
        try {
            TblReportQueryUser tblReportQueryUser = (TblReportQueryUser) query.getSingleResult();
            if (!"1".equals(String.valueOf(tblReportQueryUser.getMstQueryUser().getValidFlg()))) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_invalid_userid_pwd"));
                return response;
            }
            if (!SafeHashGenerator.getStretchedPassword(
                    password, userId).equals(tblReportQueryUser.getMstQueryUser().getHashedPassword())) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_invalid_userid_pwd"));
                return response;
            }

            if (tblReportQueryUser.getMstQueryUser().getPasswordExpiresAt() != null) {
                if (tblReportQueryUser.getMstQueryUser().getPasswordExpiresAt().getTime() < System.currentTimeMillis()) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_pwd_expired"));
                    return response;
                }
            }
        } catch (NoResultException e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_invalid_userid_pwd"));
            return response;
        }

        TblCustomReportQueryVo tblCustomReportQueryVo = tblCustomReportQueryService.getCustomReportQuery(reportId, langId);
        if (tblCustomReportQueryVo.isError()) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(tblCustomReportQueryVo.getErrorMessage());
        }
        return tblCustomReportQueryService.getCustomReportQueryCsv3(tblCustomReportQueryVo, langId, userId);
    }

}
