/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.maintenance.cycleptn;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachineVo;
import com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomendService;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomendService;
import com.kmcj.karte.resources.mold.part.MstMoldPartVo;
import com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecomendDetail;
import com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecommend;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
public class TblMaintenanceCyclePtnService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblMoldMaintenanceRecomendService tblMoldMaintenanceRecomendService;

    @Inject
    private TblMachineMaintenanceRecomendService tblMachineMaintenanceRecomendService;
    
    Date nowDate = new Date();
    /**
     * メンテナンスサイクルパターン1件取得(TblMaintenanceCyclePtn型で返却)
     *
     * @param type
     * @param cycleCode
     * @return
     */
    public TblMaintenanceCyclePtn geTblMaintenanceCyclePtnSingleByPK(String type, String cycleCode) {
        Query query = entityManager.createNamedQuery("TblMaintenanceCyclePtn.findByPK");
        query.setParameter("type", Integer.parseInt(type));
        query.setParameter("cycleCode", cycleCode);
        try {
            return (TblMaintenanceCyclePtn) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * PKによる存在チェック
     *
     * @param type
     * @param cycleCode
     * @return
     */
    public boolean isExsistByPK(Integer type, String cycleCode) {
        Query query = entityManager.createNamedQuery("TblMaintenanceCyclePtn.findByPK");
        query.setParameter("type", type);
        query.setParameter("cycleCode", cycleCode);
        try {
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (NoResultException noResultException) {
            return false;
        }
        return false;
    }

    /**
     * IDによる存在チェック
     *
     * @param id
     * @return
     */
    public boolean isExsistById(String id) {
        Query query = entityManager.createNamedQuery("TblMaintenanceCyclePtn.findById");
        query.setParameter("id", id);
        try {
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (NoResultException noResultException) {
            return false;
        }
        return false;
    }

    /**
     * メンテナンスサイクルパターンテーブル登録
     *
     * @param tblMaintenanceCyclePtn
     * @param loginUser
     */
    @Transactional
    public void createTblMaintenanceCyclePtn(TblMaintenanceCyclePtn tblMaintenanceCyclePtn, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        tblMaintenanceCyclePtn.setId(IDGenerator.generate());
        tblMaintenanceCyclePtn.setCreateDate(new java.util.Date());
        tblMaintenanceCyclePtn.setCreateUserUuid(loginUser.getUserUuid());
        tblMaintenanceCyclePtn.setUpdateDate(new java.util.Date());
        tblMaintenanceCyclePtn.setUpdateUserUuid(loginUser.getUserUuid());

        entityManager.persist(tblMaintenanceCyclePtn);
    }

    /**
     * メンテナンスサイクルパターンテーブル削除
     *
     * @param tblMaintenanceCyclePtn
     */
    @Transactional
    public void deleteTblMaintenanceCyclePtn(TblMaintenanceCyclePtn tblMaintenanceCyclePtn) {

        Query query = entityManager.createNamedQuery("TblMaintenanceCyclePtn.delete");
        query.setParameter("id", tblMaintenanceCyclePtn.getId());
        query.executeUpdate();
    }

    /**
     * メンテナンスサイクルパターンテーブル更新
     *
     * @param tblMaintenanceCyclePtn
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMaintenanceCyclePtn updateTblMaintenanceCyclePtn(TblMaintenanceCyclePtn tblMaintenanceCyclePtn, LoginUser loginUser) {
        tblMaintenanceCyclePtn.setUpdateDate(new java.util.Date());
        tblMaintenanceCyclePtn.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(tblMaintenanceCyclePtn);
        return tblMaintenanceCyclePtn;
    }

    /**
     * メンテナンスサイクルパターンテーブルを取得
     *
     * @param type
     * @param cycleCode
     * @return
     */
    public TblMaintenanceCyclePtnList getTblMaintenanceCyclePtnList(String type, String cycleCode) {

        Integer formatType = null;
        try {
            if (type != null && !"".equals(type)) {
                formatType = Integer.parseInt(type);
            }
        } catch (NumberFormatException e) {
            // nothing
        }

        StringBuilder sql;
        sql = new StringBuilder("SELECT m FROM TblMaintenanceCyclePtn m WHERE 1=1 ");

        if (type != null && !"".equals(type)) {
            sql = sql.append(" AND m.tblMaintenanceCyclePtnPK.type = :type ");
        }

        if (cycleCode != null && !"".equals(cycleCode)) {
            sql = sql.append(" AND m.tblMaintenanceCyclePtnPK.cycleCode = :cycleCode ");
        }
        sql = sql.append(" ORDER BY m.tblMaintenanceCyclePtnPK.cycleCode ");

        Query query = entityManager.createQuery(sql.toString());
        if (formatType != null) {
            query.setParameter("type", formatType);
        }

        if (cycleCode != null && !"".equals(cycleCode)) {
            query.setParameter("cycleCode", cycleCode);
        }

        List list = query.getResultList();
        TblMaintenanceCyclePtnList response = new TblMaintenanceCyclePtnList();
        response.setTblMaintenanceCyclePtnList(list);
        return response;
    }

    /**
     * メンテナンスサイクルパターンテーブルを更新
     *
     * @param tblMaintenanceCyclePtnList
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMaintenanceCyclePtnList saveTblMaintenanceCyclePtnList(TblMaintenanceCyclePtnList tblMaintenanceCyclePtnList, LoginUser loginUser) {

        for (TblMaintenanceCyclePtn tblMaintenanceCyclePtn : tblMaintenanceCyclePtnList.getTblMaintenanceCyclePtnList()) {
            /*
             * 必須チェック
             */

            if (tblMaintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getCycleCode().isEmpty()) {
                setApplicationError(tblMaintenanceCyclePtnList, loginUser, "msg_error_not_null");
                return tblMaintenanceCyclePtnList;
            }

            /*
             * 入力チェック
             */
            if (FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getMainteConditionsCol01()) <= 0
                    || FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getMainteConditionsCol02()) <= 0
                    || FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getMainteConditionsCol03()) <= 0
                    || FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getAlertConditionsCol01()) <= 0
                    || FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getAlertConditionsCol02()) <= 0
                    || FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getAlertConditionsCol03()) <= 0) {

                setApplicationError(tblMaintenanceCyclePtnList, loginUser, "msg_numerator_not_zero");
                return tblMaintenanceCyclePtnList;
            }

            /*
             * 登録制御
             */
            if ("4".equals(tblMaintenanceCyclePtn.getOperationFlag())) {
                // 登録制御処理実行
                if (createControl(tblMaintenanceCyclePtnList, tblMaintenanceCyclePtn, loginUser).isError()) {
                    return tblMaintenanceCyclePtnList;
                }
            } /*
             * 削除制御
             */ else if ("1".equals(tblMaintenanceCyclePtn.getOperationFlag())) {
                // 削除制御処理実行
                if (deleteControl(tblMaintenanceCyclePtnList, tblMaintenanceCyclePtn, loginUser).isError()) {
                    return tblMaintenanceCyclePtnList;
                }
            } /*
             * 更新制御
             */ else if ("3".equals(tblMaintenanceCyclePtn.getOperationFlag())) {
                if (updateControl(tblMaintenanceCyclePtnList, tblMaintenanceCyclePtn, loginUser).isError()) {
                    return tblMaintenanceCyclePtnList;
                }
            }
        }
        return tblMaintenanceCyclePtnList;
    }

    /**
     * メンテナンスサイクルパターンテーブル削除制御処理
     *
     * @param tblMaintenanceCyclePtnList
     * @param tblMaintenanceCyclePtn
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMaintenanceCyclePtnList deleteControl(TblMaintenanceCyclePtnList tblMaintenanceCyclePtnList, TblMaintenanceCyclePtn tblMaintenanceCyclePtn, LoginUser loginUser) {

        // メンテナンスサイクルパターンIDによる存在チェック
        if (!isExsistById(tblMaintenanceCyclePtn.getId())) {
            setApplicationError(tblMaintenanceCyclePtnList, loginUser, "msg_error_data_deleted");
            return tblMaintenanceCyclePtnList;
        }

        // メンテナンスサイクルパターン削除
        deleteTblMaintenanceCyclePtn(tblMaintenanceCyclePtn);
        return tblMaintenanceCyclePtnList;
    }

    /**
     * メンテナンスサイクルパターンテーブル更新制御処理
     *
     * @param tblMaintenanceCyclePtnList
     * @param tblMaintenanceCyclePtn
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMaintenanceCyclePtnList updateControl(TblMaintenanceCyclePtnList tblMaintenanceCyclePtnList, TblMaintenanceCyclePtn tblMaintenanceCyclePtn, LoginUser loginUser) {
        /*
         * 作業工程マスタ更新制御
         */
        // メンテナンスサイクルパターンPKによる存在チェック
        if (!isExsistByPK(tblMaintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getType(), tblMaintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getCycleCode())) {
            setApplicationError(tblMaintenanceCyclePtnList, loginUser, "msg_error_data_deleted");
            return tblMaintenanceCyclePtnList;
        }
        // 作業工程マスタ更新
        updateTblMaintenanceCyclePtn(tblMaintenanceCyclePtn, loginUser);

        // サイクル条件を変えると候補テーブル未メンテデータを削除を行う
        if (tblMaintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getType() == 1) { // 金型
            tblMoldMaintenanceRecomendService.deleteTblMoldMaintenanceRecomendBymainteCycleId(tblMaintenanceCyclePtn.getId());
        } else if (tblMaintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getType() == 2) {// 設備
            tblMachineMaintenanceRecomendService.deleteTblMachineMaintenanceRecomendBymainteCycleId(tblMaintenanceCyclePtn.getId());
        }

        return tblMaintenanceCyclePtnList;
    }

    /**
     * メンテナンスサイクルパターンテーブル登録制御処理
     *
     * @param tblMaintenanceCyclePtnList
     * @param tblMaintenanceCyclePtn
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMaintenanceCyclePtnList createControl(TblMaintenanceCyclePtnList tblMaintenanceCyclePtnList, TblMaintenanceCyclePtn tblMaintenanceCyclePtn, LoginUser loginUser) {
        /*
         * メンテナンスサイクルパターン登録制御
         */
        // メンテナンスサイクルパターン PKによる存在チェック
        if (isExsistByPK(tblMaintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getType(), tblMaintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getCycleCode())) {
            setApplicationError(tblMaintenanceCyclePtnList, loginUser, "msg_error_record_exists");
            return tblMaintenanceCyclePtnList;
        }

        createTblMaintenanceCyclePtn(tblMaintenanceCyclePtn, loginUser);

        return tblMaintenanceCyclePtnList;
    }

    private void setApplicationError(TblMaintenanceCyclePtnList tblMaintenanceCyclePtnList, LoginUser loginUser, String dictKey) {
        setBasicResponseError(
                tblMaintenanceCyclePtnList, true, ErrorMessages.E201_APPLICATION, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
        );
    }

    private void setBasicResponseError(TblMaintenanceCyclePtnList tblMaintenanceCyclePtnList, boolean error, String errorCode, String errorMessage) {
        tblMaintenanceCyclePtnList.setError(error);
        tblMaintenanceCyclePtnList.setErrorCode(errorCode);
        tblMaintenanceCyclePtnList.setErrorMessage(errorMessage);
    }

    /**
     * 金型メンテナンス/アラートチェック
     *
     * @param moldVoList
     *
     */
    public void chkMoldMainte(List<MstMoldDetail> moldVoList) {

        if (!moldVoList.isEmpty()) {

            TblMaintenanceCyclePtn maintenanceCyclePtn;
            Query query = entityManager.createQuery("SELECT t FROM TblMaintenanceCyclePtn t WHERE t.tblMaintenanceCyclePtnPK.type = 1 AND t.def = 1");

            try {
                maintenanceCyclePtn = (TblMaintenanceCyclePtn) query.getSingleResult();

            } catch (NoResultException e) {
                maintenanceCyclePtn = null;
            }

            for (int i = 0; i < moldVoList.size(); i++) {

                moldVoList.get(i).setSendFlg(0);
                moldVoList.get(i).setMsgFlg(0);

                int newSendFlg = 0;

                MstMoldDetail mstMoldDetail = moldVoList.get(i);

                String mainteCycleId01 = mstMoldDetail.getMainteCycleId01();
                String mainteCycleId02 = mstMoldDetail.getMainteCycleId02();
                String mainteCycleId03 = mstMoldDetail.getMainteCycleId03();

                // 該当金型のメンテサイクルID未設定の場合
                if (StringUtils.isEmpty(mainteCycleId01) && StringUtils.isEmpty(mainteCycleId02) && StringUtils.isEmpty(mainteCycleId03)) {

                    if (maintenanceCyclePtn == null) {
                        continue;
                    } else {
                        newSendFlg = compareMoldCondition(mstMoldDetail, maintenanceCyclePtn);
                    }

                } else {

                    // サイクルパターン01存在の場合
                    if (StringUtils.isNotEmpty(mainteCycleId01)) {

                        TblMaintenanceCyclePtn tblMaintenanceCyclePtn = getMainTenanceCyclePtnByID(mainteCycleId01);

                        newSendFlg = compareMoldCondition(mstMoldDetail, tblMaintenanceCyclePtn);
                    }

                    // 未送信、且つサイクルパターン02存在の場合
                    if (newSendFlg == 0 && StringUtils.isNotEmpty(mainteCycleId02)) {

                        TblMaintenanceCyclePtn tblMaintenanceCyclePtn = getMainTenanceCyclePtnByID(mainteCycleId02);

                        newSendFlg = compareMoldCondition(mstMoldDetail, tblMaintenanceCyclePtn);
                    }

                    // 未送信、且つサイクルパターン03存在の場合
                    if (newSendFlg == 0 && StringUtils.isNotEmpty(mainteCycleId03)) {

                        TblMaintenanceCyclePtn tblMaintenanceCyclePtn = getMainTenanceCyclePtnByID(mainteCycleId03);

                        newSendFlg = compareMoldCondition(mstMoldDetail, tblMaintenanceCyclePtn);
                    }

                }

                moldVoList.get(i).setMsgFlg(mstMoldDetail.getMsgFlg());
                moldVoList.get(i).setMainteReasonText(mstMoldDetail.getMainteReasonText());
                moldVoList.get(i).setSendFlg(newSendFlg);
                // KM-360 対応 start
                moldVoList.get(i).setHitCondition(mstMoldDetail.getHitCondition());
                // KM-360 対応 end
            }

        }

    }

    public void chkMoldPartMaintRecommend(List<MstMoldDetail> moldVoList) {
        if (!moldVoList.isEmpty()) {
            for (int i = 0; i < moldVoList.size(); i++) {
                String moldPartMaintRecommendSql = 
                        "SELECT mpMaintRecommend FROM TblMoldPartMaintenanceRecommend mpMaintRecommend"
                        + " WHERE mpMaintRecommend.moldUuid = :moldUuid AND mpMaintRecommend.maintainedFlag = 0";
                Query query = entityManager.createQuery(moldPartMaintRecommendSql);
                query.setParameter("moldUuid", moldVoList.get(i).getMoldUuid());
                List<TblMoldPartMaintenanceRecommend> moldPartMaintRecommends = query.getResultList();
                
                if (!moldPartMaintRecommends.isEmpty()) {
                    moldVoList.get(i).setMsgFlg(3);
                    moldVoList.get(i).setMainteReasonText(moldVoList.get(i).getMainteReasonText());
                    moldVoList.get(i).setSendFlg(0);
                }
            }
        }
    }
    
    /**
     * IDから、メンテナンスサイクルパターン情報を取得
     *
     * @param id
     * @return
     */
    private TblMaintenanceCyclePtn getMainTenanceCyclePtnByID(String id) {

        Query query = entityManager.createNamedQuery("TblMaintenanceCyclePtn.findById");

        query.setParameter("id", id);

        try {

            TblMaintenanceCyclePtn tblMaintenanceCyclePtn = (TblMaintenanceCyclePtn) query.getSingleResult();

            return tblMaintenanceCyclePtn;
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * 金型メンテナンス/アラートに判定
     *
     * @param mstMoldDetail
     * @param tblMaintenanceCyclePtn
     * @return
     */
    private int compareMoldCondition(MstMoldDetail mstMoldDetail, TblMaintenanceCyclePtn tblMaintenanceCyclePtn) {

        int rs = 0;

        int afterMainteTotalProducingTimeHour = mstMoldDetail.getAfterMainteTotalProducingTimeHour() == null ? 0 : mstMoldDetail.getAfterMainteTotalProducingTimeHour().intValue(); //FileUtil.getIntegerValue(mstMoldDetail.getAfterMainteTotalProducingTimeHour());

        int afterMainteTotalDay = 0;
        if (null != mstMoldDetail.getLastMainteDate()) {
            afterMainteTotalDay = DateFormat.daysBetween(mstMoldDetail.getLastMainteDate(), new Date());
        }

        int afterMainteTotalShotCount = FileUtil.getIntegerValue(mstMoldDetail.getAfterMainteTotalShotCount());

        int mainteCondition01 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getMainteConditionsCol01());

        int mainteCondition02 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getMainteConditionsCol02());

        int mainteCondition03 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getMainteConditionsCol03());

        int alertCondition01 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getAlertConditionsCol01());

        int alertCondition02 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getAlertConditionsCol02());

        int alertCondition03 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getAlertConditionsCol03());

        if ((afterMainteTotalDay >= mainteCondition01) || (afterMainteTotalProducingTimeHour >= mainteCondition02)
                || (afterMainteTotalShotCount >= mainteCondition03)) {

            mstMoldDetail.setMsgFlg(2);

            // 金型メンテナンス候補テーブルに、区分は「メンテ」且つ未メンテナンスのレコードが存在しない場合
            if (tblMoldMaintenanceRecomendService.chkExists(mstMoldDetail.getMoldUuid(), 2, 0) == 0) {

                mstMoldDetail.setActualMainteCycleId(tblMaintenanceCyclePtn.getId());
                mstMoldDetail.setMainteReasonText(tblMaintenanceCyclePtn.getMainteReasonText());

                // KM-360 該当条件を設定する start
                if (afterMainteTotalDay >= mainteCondition01) {
                    mstMoldDetail.setHitCondition(1);
                } else if (afterMainteTotalProducingTimeHour >= mainteCondition02) {
                    mstMoldDetail.setHitCondition(2);
                } else if (afterMainteTotalShotCount >= mainteCondition03) {
                    mstMoldDetail.setHitCondition(3);
                }
                // KM-360 該当条件を設定する end
                return 2;
            }

        } else if ((afterMainteTotalDay >= alertCondition01) || (afterMainteTotalProducingTimeHour >= alertCondition02)
                || (afterMainteTotalShotCount >= alertCondition03)) {

            mstMoldDetail.setMsgFlg(1);

            // 金型メンテナンス候補テーブルに、区分は「アラート」且つ未メンテナンスのレコードが存在しない場合
            if (tblMoldMaintenanceRecomendService.chkExists(mstMoldDetail.getMoldUuid(), 1, 0) == 0) {

                mstMoldDetail.setActualMainteCycleId(tblMaintenanceCyclePtn.getId());
                mstMoldDetail.setMainteReasonText(tblMaintenanceCyclePtn.getMainteReasonText());

                // KM-360 該当条件を設定する start
                if (afterMainteTotalDay >= alertCondition01) {
                    mstMoldDetail.setHitCondition(1);
                } else if (afterMainteTotalProducingTimeHour >= alertCondition02) {
                    mstMoldDetail.setHitCondition(2);
                } else if (afterMainteTotalShotCount >= alertCondition03) {
                    mstMoldDetail.setHitCondition(3);
                }
                // KM-360 該当条件を設定する end
                return 1;
            }

        }

        return rs;

    }

    /**
     * 設備メンテナンス/アラートチェック
     *
     * @param machineVoList
     *
     */
    public void chkMachineMainte(List<MstMachineVo> machineVoList) {

        if (!machineVoList.isEmpty()) {

            TblMaintenanceCyclePtn maintenanceCyclePtn;
            Query query = entityManager.createQuery("SELECT t FROM TblMaintenanceCyclePtn t WHERE t.tblMaintenanceCyclePtnPK.type = 2 AND t.def = 1");
            try {
                maintenanceCyclePtn = (TblMaintenanceCyclePtn) query.getSingleResult();

            } catch (NoResultException e) {
                maintenanceCyclePtn = null;
            }

            for (int i = 0; i < machineVoList.size(); i++) {

                machineVoList.get(i).setSendFlg(0);
                machineVoList.get(i).setMsgFlg(0);

                int newSendFlg = 0;

                MstMachineVo mstMachineVo = machineVoList.get(i);

                String mainteCycleId01 = mstMachineVo.getMainteCycleId01();
                String mainteCycleId02 = mstMachineVo.getMainteCycleId02();
                String mainteCycleId03 = mstMachineVo.getMainteCycleId03();

                // 該当金型のメンテサイクルID未設定の場合
                if (StringUtils.isEmpty(mainteCycleId01) && StringUtils.isEmpty(mainteCycleId02) && StringUtils.isEmpty(mainteCycleId03)) {

                    if (maintenanceCyclePtn == null) {
                        continue;
                    } else {
                        newSendFlg = compareMachineCondition(mstMachineVo, maintenanceCyclePtn);
                    }

                } else {

                    // サイクルパターン01存在の場合
                    if (StringUtils.isNotEmpty(mainteCycleId01)) {

                        TblMaintenanceCyclePtn tblMaintenanceCyclePtn = getMainTenanceCyclePtnByID(mainteCycleId01);

                        newSendFlg = compareMachineCondition(mstMachineVo, tblMaintenanceCyclePtn);
                    }

                    // 未送信、且つサイクルパターン02存在の場合
                    if (newSendFlg == 0 && StringUtils.isNotEmpty(mainteCycleId02)) {

                        TblMaintenanceCyclePtn tblMaintenanceCyclePtn = getMainTenanceCyclePtnByID(mainteCycleId02);

                        newSendFlg = compareMachineCondition(mstMachineVo, tblMaintenanceCyclePtn);
                    }

                    // 未送信、且つサイクルパターン03存在の場合
                    if (newSendFlg == 0 && StringUtils.isNotEmpty(mainteCycleId03)) {

                        TblMaintenanceCyclePtn tblMaintenanceCyclePtn = getMainTenanceCyclePtnByID(mainteCycleId03);

                        newSendFlg = compareMachineCondition(mstMachineVo, tblMaintenanceCyclePtn);
                    }

                }

                machineVoList.get(i).setMsgFlg(mstMachineVo.getMsgFlg());
                machineVoList.get(i).setMainteReasonText(mstMachineVo.getMainteReasonText());
                machineVoList.get(i).setSendFlg(newSendFlg);
                // KM-360 対応 start
                machineVoList.get(i).setHitCondition(mstMachineVo.getHitCondition());
                // KM-360 対応 end
            }

        }

    }

    /**
     * 設備メンテナンス/アラートに判定
     *
     * @param mstMachineVo
     * @param tblMaintenanceCyclePtn
     * @return
     */
    private int compareMachineCondition(MstMachineVo mstMachineVo, TblMaintenanceCyclePtn tblMaintenanceCyclePtn) {

        int rs = 0;

        int afterMainteTotalProducingTimeHour = mstMachineVo.getAfterMainteTotalProducingTimeHour() == null ? 0 : mstMachineVo.getAfterMainteTotalProducingTimeHour().intValue();

        int afterMainteTotalDay = 0;
        if (null != mstMachineVo.getLastMainteDate()) {
            afterMainteTotalDay = DateFormat.daysBetween(mstMachineVo.getLastMainteDate(), new Date());
        }

        int afterMainteTotalShotCount = FileUtil.getIntegerValue(mstMachineVo.getAfterMainteTotalShotCount());

        int mainteCondition01 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getMainteConditionsCol01());

        int mainteCondition02 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getMainteConditionsCol02());

        int mainteCondition03 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getMainteConditionsCol03());

        int alertCondition01 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getAlertConditionsCol01());

        int alertCondition02 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getAlertConditionsCol02());

        int alertCondition03 = FileUtil.getIntegerValue(tblMaintenanceCyclePtn.getAlertConditionsCol03());

        if ((afterMainteTotalDay >= mainteCondition01) || (afterMainteTotalProducingTimeHour >= mainteCondition02) || (afterMainteTotalShotCount >= mainteCondition03)) {

            mstMachineVo.setMsgFlg(2);

            // 設備メンテナンス候補テーブルに、区分は「メンテ」且つ未メンテナンスのレコードが存在しない場合
            if (tblMachineMaintenanceRecomendService.chkExists(mstMachineVo.getMachineUuid(), 2, 0) == 0) {

                mstMachineVo.setActualMainteCycleId(tblMaintenanceCyclePtn.getId());
                mstMachineVo.setMainteReasonText(tblMaintenanceCyclePtn.getMainteReasonText());
                // KM-360 該当条件を設定する start
                if (afterMainteTotalDay >= mainteCondition01) {
                    mstMachineVo.setHitCondition(1);
                } else if (afterMainteTotalProducingTimeHour >= mainteCondition02) {
                    mstMachineVo.setHitCondition(2);
                } else if (afterMainteTotalShotCount >= mainteCondition03) {
                    mstMachineVo.setHitCondition(3);
                }
                // KM-360 該当条件を設定する end
                return 2;
            }

        } else if ((afterMainteTotalDay >= alertCondition01) || (afterMainteTotalProducingTimeHour >= alertCondition02) || (afterMainteTotalShotCount >= alertCondition03)) {

            mstMachineVo.setMsgFlg(1);

            // 設備メンテナンス候補テーブルに、区分は「アラート」且つ未メンテナンスのレコードが存在しない場合
            if (tblMachineMaintenanceRecomendService.chkExists(mstMachineVo.getMachineUuid(), 1, 0) == 0) {

                mstMachineVo.setActualMainteCycleId(tblMaintenanceCyclePtn.getId());
                mstMachineVo.setMainteReasonText(tblMaintenanceCyclePtn.getMainteReasonText());
                // KM-360 該当条件を設定する start
                if (afterMainteTotalDay >= alertCondition01) {
                    mstMachineVo.setHitCondition(1);
                } else if (afterMainteTotalProducingTimeHour >= alertCondition02) {
                    mstMachineVo.setHitCondition(2);
                } else if (afterMainteTotalShotCount >= alertCondition03) {
                    mstMachineVo.setHitCondition(3);
                }
                // KM-360 該当条件を設定する end
                return 1;
            }

        }

        return rs;

    }

    /*
    * 根据type和不定数量的code取得TblMaintenanceCyclePtn
     */
    public List<TblMaintenanceCyclePtn> getTblMaintenanceCyclePtnsByTypeAndCodes(int type, String... codes) {
        StringBuilder sqlMaintenanceCyclePtns = new StringBuilder("SELECT m from TblMaintenanceCyclePtn m where m.tblMaintenanceCyclePtnPK.type = :type and (");
        boolean isAllEmpty = true;
        for (int i = 0; i < codes.length; i++) {
            if (null != codes[i] && !codes[i].isEmpty()) {
                sqlMaintenanceCyclePtns.append(" m.tblMaintenanceCyclePtnPK.cycleCode = :code").append(i);
                if (i != codes.length - 1) {
                    sqlMaintenanceCyclePtns.append(" or ");
                }
                if (isAllEmpty == true) {
                    isAllEmpty = false;
                }
            }
        }
        if (isAllEmpty == true) {
            return new ArrayList();
        }
        if (sqlMaintenanceCyclePtns.toString().endsWith(" or ")) {
            sqlMaintenanceCyclePtns.delete(sqlMaintenanceCyclePtns.lastIndexOf("or"), sqlMaintenanceCyclePtns.length());
        }
        sqlMaintenanceCyclePtns.append(")");
        Query q = entityManager.createQuery(sqlMaintenanceCyclePtns.toString());
        q.setParameter("type", type);
        for (int i = 0; i < codes.length; i++) {
            if (null != codes[i] && !codes[i].isEmpty()) {
                q.setParameter("code" + i, codes[i]);
            }
        }
        return q.getResultList();
    }
    
    /**
     * 設備メンテナンス/アラートチェック
     *
     * @param moldPartVoList
     *
     */
    public void chkMoldPartMainte(List<MstMoldPartVo> moldPartVoList) {
        List<MstMoldPartVo> processList = new ArrayList();
        if (!moldPartVoList.isEmpty()) {
            
            for (int i = 0; i < moldPartVoList.size(); i++) {
                MstMoldPartVo moldPartVo = moldPartVoList.get(i);
                TblMoldPartMaintenanceRecomendDetail tblMoldPartMaintenanceRecomendDetail;
                Query query = entityManager.createNamedQuery("TblMoldPartMaintenanceRecomendDetail.findById");
                query.setParameter("moldPartRelId", moldPartVo.getId());
                try {
                    tblMoldPartMaintenanceRecomendDetail = (TblMoldPartMaintenanceRecomendDetail) query.getSingleResult();
                } catch (NoResultException e) {
                    tblMoldPartMaintenanceRecomendDetail = null;
                }
                
                //Case replace on ShotCnt
                if(0!=moldPartVo.getRplClShotCnt() && moldPartVo.getAftRplShotCnt() >= moldPartVo.getRplClShotCnt()){
                    //Delete from mold part maintenance recommend if exist and add new one
                    removeMoldPartMainRe(tblMoldPartMaintenanceRecomendDetail);
                    moldPartVo.setReplaceOrRepair(1);
                    moldPartVo.setHitCondition(3);
                    moldPartVo.setMaintainedFlag(0);
                    
                //Case replace on ProdTimeHour
                }else if(0!=moldPartVo.getRplClProdTimeHour() && moldPartVo.getAftRplProdTimeHour() >= moldPartVo.getRplClProdTimeHour()){
                    //Delete from mold part maintenance recommend if exist and add new one
                    removeMoldPartMainRe(tblMoldPartMaintenanceRecomendDetail);
                    moldPartVo.setReplaceOrRepair(1);
                    moldPartVo.setHitCondition(2);
                    moldPartVo.setMaintainedFlag(0);
                    
                //Case replace on LappsedDay
                }else if(null != moldPartVo.getLastRplDateTime() && 0!=moldPartVo.getRplClLappsedDay() && DateFormat.daysBetween(moldPartVo.getLastRplDateTime(),nowDate) >= moldPartVo.getRplClLappsedDay()){
                    //Delete from mold part maintenance recommend if exist and add new one
                    removeMoldPartMainRe(tblMoldPartMaintenanceRecomendDetail);
                    moldPartVo.setReplaceOrRepair(1);
                    moldPartVo.setHitCondition(1);
                    moldPartVo.setMaintainedFlag(0);
                    
                //Case repair on ShotCnt
                }else if(0!=moldPartVo.getRprClShotCnt() && moldPartVo.getAftRprShotCnt() >= moldPartVo.getRprClShotCnt()){
                    if(tblMoldPartMaintenanceRecomendDetail != null){
                        //Remove from mold part list
                        processList.add(moldPartVo);
                    }else{
                        moldPartVo.setReplaceOrRepair(2);
                        moldPartVo.setHitCondition(3);
                        moldPartVo.setMaintainedFlag(0);
                    }
                    
                //Case repair on ProdTimeHour
                }else if(0!=moldPartVo.getRprClProdTimeHour() && moldPartVo.getAftRprProdTimeHour() >= moldPartVo.getRprClProdTimeHour()){
                    if(tblMoldPartMaintenanceRecomendDetail != null){
                        //Remove from mold part list
                        processList.add(moldPartVo);
                    }else{
                        moldPartVo.setReplaceOrRepair(2);
                        moldPartVo.setHitCondition(2);
                        moldPartVo.setMaintainedFlag(0);
                    }
                    
                //Case repair on LappsedDay
                }else if(null != moldPartVo.getLastRprDateTime() && 0!=moldPartVo.getRprClLappsedDay() && DateFormat.daysBetween(moldPartVo.getLastRprDateTime(),nowDate) >= moldPartVo.getRprClLappsedDay()){
                    if(tblMoldPartMaintenanceRecomendDetail != null){
                        //Remove from mold part list
                        processList.add(moldPartVo);
                    }else{
                        moldPartVo.setReplaceOrRepair(2);
                        moldPartVo.setHitCondition(1);
                        moldPartVo.setMaintainedFlag(0);
                    }
                    
                //Case exception
                }else {
                    //Remove from mold part list
                    processList.add(moldPartVo);
                }
            }
        }
        if(processList.size()>0){
            for (MstMoldPartVo processData : processList) {
                moldPartVoList.remove(processData);
            }
        }
    }
    
    /**
     * Check and remove mold part if exist in mold_part_maintenance_recommend table
     *
     * @param moldPartMainteRe
     *
     */
    public void removeMoldPartMainRe(TblMoldPartMaintenanceRecomendDetail moldPartMainteRe){
        if(moldPartMainteRe != null){
            Query query = entityManager.createQuery("DELETE FROM TblMoldPartMaintenanceRecomendDetail t WHERE t.moldPartRelId = :moldPartRelId AND t.maintainedFlag = 0");
            query.setParameter("moldPartRelId", moldPartMainteRe.getMoldPartRelId());
            query.executeUpdate();
        }
    }
    
}
