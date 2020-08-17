/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.standard.worktime;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

/**
 * 工標準業務時間マスタ サービス
 *
 * @author admin
 */
@Dependent
public class MstStandardWorkTimeService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Context
    ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 工標準業務時間マスタを取得
     *
     * @param loginUser
     * @return
     */
    public MstStandardWorkTimeList getMstStandardWorkTimes(LoginUser loginUser) {
        Query query = entityManager.createNamedQuery("MstStandardWorkTime.findAll");
        List list = query.getResultList();
        if (list.size() > 0) {
            MstStandardWorkTime systemDefault = (MstStandardWorkTime) list.get(0);
            if (systemDefault.getId().equals("system_default")) {
                systemDefault.setSystemDefault(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "system_default"));
            }
        }
        MstStandardWorkTimeList response = new MstStandardWorkTimeList();
        response.setMstStandardWorkTime(list);
        return response;
    }

    /**
     * 標準業務時間マスタ登録データ設定
     *
     * @param response
     * @param mstStandardWorkTime
     * @param loginUser
     * @return
     */
    public BasicResponse setCreateData(BasicResponse response, MstStandardWorkTime mstStandardWorkTime, LoginUser loginUser) {
        if (mstStandardWorkTime.getCompanyId() == null || "".equals(mstStandardWorkTime.getCompanyId())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }

        FileUtil fu = new FileUtil();
        String workTimeMinutes = String.valueOf(mstStandardWorkTime.getWorkTimeMinutes());
        if (!fu.isNumber(workTimeMinutes)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_isnumber"));
            return response;
        }
        if (mstStandardWorkTime.getWorkTimeMinutes() == null || "".equals(String.valueOf(mstStandardWorkTime.getWorkTimeMinutes()))) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        return response;
    }

    /**
     * 標準業務時間マスタロック更新用データ設定
     *
     * @param response
     * @param mstStandardWorkTime
     * @param loginUser
     * @return
     */
    public BasicResponse setUpdateData(BasicResponse response, MstStandardWorkTime mstStandardWorkTime, LoginUser loginUser) {
        if (mstStandardWorkTime.getId() == null || "".equals(mstStandardWorkTime.getId())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        if (!"system_default".equals(mstStandardWorkTime.getId())) {
            if (mstStandardWorkTime.getCompanyId() == null || "".equals(mstStandardWorkTime.getCompanyId())) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
                return response;
            }
        } else {
            mstStandardWorkTime.setCompanyId(null);
        }

        if (mstStandardWorkTime.getWorkTimeMinutes() == null || "".equals(String.valueOf(mstStandardWorkTime.getWorkTimeMinutes()))) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        MstStandardWorkTime mstStandardWorkTimes = entityManager.find(MstStandardWorkTime.class, mstStandardWorkTime.getId());
        if (mstStandardWorkTimes == null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_processing_record"));
            return response;
        }
        mstStandardWorkTime.setCreateDate(mstStandardWorkTimes.getCreateDate());
        mstStandardWorkTime.setCreateUserUuid(mstStandardWorkTimes.getCreateUserUuid());

        return response;
    }

    /**
     * 標準業務時間マスタ削除データ設定
     *
     * @param response
     * @param id
     * @param loginUser
     * @return
     */
    public BasicResponse setDeleteData(BasicResponse response, String id, LoginUser loginUser) {
        MstStandardWorkTimeList updateMstStandardWorkTimes = getStandardWorkTimeById(id);
        // 存在チェックを行い、存在しない場合、errorCode,errorMessageをセット
        if (updateMstStandardWorkTimes.getMstStandardWorkTime().isEmpty()) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return response;
        }
        if ("system_default".equals(id)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_default"));
            return response;
        }
        return response;
    }

    /**
     * 工標準業務時間マスタ登録
     *
     * @param mstStandardWorkTime
     * @param loginUser
     */
    @Transactional
    public void createMstStandardWorkTime(MstStandardWorkTime mstStandardWorkTime, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        mstStandardWorkTime.setId(IDGenerator.generate());
        Date intoDate = new Date();
        mstStandardWorkTime.setCreateDate(intoDate);
        mstStandardWorkTime.setCreateUserUuid(loginUser.getUserUuid());
        mstStandardWorkTime.setUpdateDate(intoDate);
        mstStandardWorkTime.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(mstStandardWorkTime);
    }

    /**
     * 工標準業務時間マスタ更新
     *
     * @param mstStandardWorkTime
     * @param loginUser
     */
    @Transactional
    public void updateMstStandardWorkTime(MstStandardWorkTime mstStandardWorkTime, LoginUser loginUser) {
        Date intoDate = new Date();
        mstStandardWorkTime.setUpdateDate(intoDate);
        mstStandardWorkTime.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(mstStandardWorkTime);
    }

    /**
     * 工標準業務時間マスタ削除
     *
     * @param id
     */
    @Transactional
    public void deleteMstStandardWorkTime(String id) {
        Query query = entityManager.createNamedQuery("MstStandardWorkTime.delete");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    /**
     * 標準業務時間マスタ1件取得(ID指定)
     *
     * @param id
     * @return
     */
    public MstStandardWorkTimeList getStandardWorkTimeById(String id) {
        // 標準業務時間データ取得
        Query query = entityManager.createNamedQuery("MstStandardWorkTime.findById");
        query.setParameter("id", id);
        List list = query.getResultList();
        MstStandardWorkTimeList response = new MstStandardWorkTimeList();
        response.setMstStandardWorkTime(list);
        return response;
    }

    /*
     * 標準業務時間マスタチェック
     *
     * @param department
     * @param workTimeMinutes
     * @return
     */
    public boolean standardWorkTimeCheck(int department, String companyId) {

        Query query = entityManager.createNamedQuery("MstStandardWorkTime.findBycompanyIdDepartment");

        query.setParameter("department", department);
        query.setParameter("companyId", companyId);

        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * 標準業務時間マスタ取得(ログインユーザ)
     *
     * @param loginUser
     * @return
     */
    public MstStandardWorkTimeList getStandardWorkTimesByLoginUser(LoginUser loginUser) {

        // ユーザ情報取得
        Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
        userQuery.setParameter("uuid", loginUser.getUserUuid());
        MstUser user = (MstUser) userQuery.getSingleResult();
        try {
            return getStandardWorkTimesByUser(user);
        } catch (NoResultException e) {
            return getStandardWorkTimesByUser(null);
        }
    }

    /**
     * 標準業務時間マスタ取得(ユーザID指定)
     *
     * @param userId
     * @return
     */
    public MstStandardWorkTimeList getStandardWorkTimesByUserId(String userId) {

        // ユーザ情報取得
        Query userQuery = entityManager.createNamedQuery("MstUser.findByUserId");
        userQuery.setParameter("userId", userId);
        try {
            MstUser user = (MstUser) userQuery.getSingleResult();
            return getStandardWorkTimesByUser(user);
        } catch (NoResultException e) {
            return getStandardWorkTimesByUser(null);
        }

    }

    /**
     * 標準業務時間マスタ取得(ユーザマスタ指定)
     *
     * @param user
     * @return
     */
    public MstStandardWorkTimeList getStandardWorkTimesByUser(MstUser user) {
        boolean useDefault = false;
        List list = null;
        // 標準業務時間データ取得
        if (user.getDepartment() != null) {
            Query query = entityManager.createNamedQuery("MstStandardWorkTime.findBycompanyIdDepartment");
            query.setParameter("companyId", user.getCompanyId());
            query.setParameter("department", Integer.parseInt(user.getDepartment()));

            list = query.getResultList();
            if (list.isEmpty()) {
                useDefault = true;
            }
        } else {
            useDefault = true;
        }
        // 該当無しならシステムデフォルト
        if (useDefault) {
            // 標準業務時間データ取得
            Query defaultQuery = entityManager.createNamedQuery("MstStandardWorkTime.findById");
            defaultQuery.setParameter("id", "system_default");

            list = defaultQuery.getResultList();
        }
        MstStandardWorkTimeList response = new MstStandardWorkTimeList();
        response.setMstStandardWorkTime(list);
        return response;
    }
}
