/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.suspension;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.TblProductionService;
import com.kmcj.karte.resources.work.TblWorkResource;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.TimezoneConverter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * 生産中断履歴テーブルサービス
 *
 * @author t.ariki
 */
@Dependent
public class TblProductionSuspensionlService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblProductionService tblProductionService;
    
    @Inject
    private MstChoiceService mstChoiceService;

    private Logger logger = Logger.getLogger(TblProductionSuspensionlService.class.getName());

    /**
     * 中断履歴一覧取得By 生産実績Id
     *
     * @param productionId
     * @param loginUser
     * @return
     */
    public TblProductionSuspensionList getProductionSuspensionListByProductionId(String productionId, LoginUser loginUser) {

        /* 
         * JPQLで条件定義
         *   結合条件はエンティティクラスに定義済み
         */
        StringBuilder sql = new StringBuilder();

        sql = sql.append("SELECT tblProductionSuspension FROM TblProductionSuspension tblProductionSuspension"
                + " LEFT JOIN FETCH tblProductionSuspension.tblProduction"
                + " LEFT JOIN FETCH tblProductionSuspension.tblProduction.mstWorkPhase"
                + " LEFT JOIN FETCH tblProductionSuspension.tblWork"
                + " WHERE 1=1 ");
        // 生産実績
        if (productionId != null && !"".equals(productionId)) {
            sql.append(" and tblProductionSuspension.productionId = :productionId ");
        }
        // 開始日時 降順
        sql = sql.append(" ORDER BY tblProductionSuspension.startDatetime DESC ");

        Query query = entityManager.createQuery(sql.toString());

        /*
         * パラメータバインド
         */
        if (productionId != null && !"".equals(productionId)) {
            query.setParameter("productionId", productionId);
        }

        List list = query.getResultList();
        TblProductionSuspensionList response = new TblProductionSuspensionList();
        List<TblProductionSuspensionVo> productionSuspensionVos = new ArrayList<TblProductionSuspensionVo>();
        TblProductionSuspensionVo productionSuspensionVo;
        FileUtil fileUtil = new FileUtil();

        if (!list.isEmpty()) {
            /**
             * 中断理由分類リストを選択肢マスタより取得しておく
             */
            MstChoiceList suspendReason = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_production_suspension.suspend_reason");
            for (int i = 0; i < list.size(); i++) {
                TblProductionSuspension productionSuspension = (TblProductionSuspension) list.get(i);
                productionSuspensionVo = new TblProductionSuspensionVo();
                productionSuspensionVo.setId(productionSuspension.getId());
                productionSuspensionVo.setProductionId(productionSuspension.getProductionId());

                productionSuspensionVo.setStartDatetimeStr(DateFormat.dateToStr(productionSuspension.getStartDatetime(), DateFormat.DATETIME_FORMAT));
                productionSuspensionVo.setStartDatetimeStzStr(DateFormat.dateToStr(productionSuspension.getStartDatetimeStz(), DateFormat.DATETIME_FORMAT));

                productionSuspensionVo.setEndDatetimeStr(DateFormat.dateToStr(productionSuspension.getEndDatetime(), DateFormat.DATETIME_FORMAT));
                productionSuspensionVo.setEndDatetimeStzStr(DateFormat.dateToStr(productionSuspension.getEndDatetimeStz(), DateFormat.DATETIME_FORMAT));

                // 選択肢マスタ.中断理由分類の名称取得
                if (productionSuspension.getSuspendReason() != null) {
                    productionSuspensionVo.setSuspendReason(String.valueOf(productionSuspension.getSuspendReason()));
                    productionSuspensionVo.setSuspendReasonType("");
                    for (MstChoice suspendReasonCategory : suspendReason.getMstChoice()) {
                        // SEQが一致する場合は名称設定
                        if (productionSuspension.getSuspendReason() == Integer.parseInt(suspendReasonCategory.getMstChoicePK().getSeq())) {
                            productionSuspensionVo.setSuspendReasonType(suspendReasonCategory.getChoice());
                            break;
                        }
                    }
                } else {
                    productionSuspensionVo.setSuspendReasonType("");
                    productionSuspensionVo.setSuspendReason("");
                }

                productionSuspensionVo.setSuspendReasonText(productionSuspension.getSuspendReasonText());

                if (productionSuspension.getSuspendedTimeMinutes() != null) {
                    productionSuspensionVo.setSuspendedTimeMinutes(String.valueOf(productionSuspension.getSuspendedTimeMinutes()));
                } else {
                    productionSuspensionVo.setSuspendedTimeMinutes("0");
                }

                productionSuspensionVo.setWorkingDate("");
                productionSuspensionVo.setWorkName("");
                productionSuspensionVo.setUserId("");
                productionSuspensionVo.setUserName("");
                if (productionSuspension.getWorkId() != null) {
                    productionSuspensionVo.setWorkId(productionSuspension.getWorkId());

                    // 作業日
                    if (productionSuspension.getTblWork() != null) {
                        productionSuspensionVo.setWorkingDate(fileUtil.getDateFormatForStr(productionSuspension.getTblWork().getWorkingDate()));

                        if (productionSuspension.getTblWork().getMstUser() != null) {
                            productionSuspensionVo.setUserId(productionSuspension.getTblWork().getMstUser().getUserId());
                            productionSuspensionVo.setUserName(productionSuspension.getTblWork().getMstUser().getUserName());
                        }
                    }

                    if (productionSuspension.getTblProduction() != null && productionSuspension.getTblProduction().getMstWorkPhase() != null) {
                        productionSuspensionVo.setWorkName(productionSuspension.getTblProduction().getMstWorkPhase().getWorkPhaseName());
                    }
                }

                productionSuspensionVo.setCreateDateStr(fileUtil.getDateTimeFormatForStr(productionSuspension.getCreateDate()));
                productionSuspensionVo.setCreateUserUuid(productionSuspension.getCreateUserUuid());
                productionSuspensionVo.setUpdateDateStr(fileUtil.getDateTimeFormatForStr(productionSuspension.getUpdateDate()));
                productionSuspensionVo.setUpdateUserUuid(productionSuspension.getUpdateUserUuid());
                productionSuspensionVos.add(productionSuspensionVo);

            }

            response.setTblProductionSuspensionVo(productionSuspensionVos);
        }

        return response;
    }

    /**
     * 生産実績状態更新
     *
     * @param tblProductionSuspension
     * @param tblProduction
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse changeStatus(TblProductionSuspension tblProductionSuspension, TblProduction tblProduction, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        if (tblProductionSuspension == null) {
            setApplicationError(response, loginUser, "msg_error_no_processing_record", "TblProductionSuspension");
            return response;
        }

        if (tblProduction == null) {

            if (StringUtils.isNotEmpty(tblProductionSuspension.getProductionId())) {

                tblProduction = tblProductionService.getProductionSingleById(tblProductionSuspension.getProductionId());
                if (tblProduction == null) {
                    setApplicationError(response, loginUser, "msg_error_data_deleted", "TblProduction");
                    return response;
                }
            }
        }

        if (tblProductionSuspension.getInterruptionFlag() == 1) { // 中断

            // 中断の場合
            if (tblProduction.getStatus() != null && (tblProduction.getStatus() == 1 || tblProduction.getStatus() == 2)) {
                // すでに中断している場合は何もしない。
                setApplicationError(response, loginUser, "msg_error_suspend", "TblProduction");
                return response;
            }
            // 終了の場合
            if (tblProduction.getStatus() != null && tblProduction.getStatus() == 9) {
                // すでに終了している場合は何もしない。
                setApplicationError(response, loginUser, "msg_production_end", "TblProduction");
                return response;
            }
            // 生産実績状態
            tblProduction.setStatus(1); //中断
            // 選択されている生産実績を中断状態にし、
            tblProductionService.updateTblProduction(tblProduction, loginUser);
            // 生産中断履歴テーブルに中断時刻と中断理由を記録する。
            tblProductionSuspension.setSuspendedTimeMinutes(0);
            tblProductionSuspension.setStartDatetime(new java.util.Date());
            tblProductionSuspension.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), new java.util.Date()));
            createProductionSuspension(tblProductionSuspension, loginUser);
        } else if (tblProductionSuspension.getInterruptionFlag() == 2) { // 一時中断

            if (tblProductionSuspension.getWorkEnd() == 0) {// 0:作業開始

                // 生産実績状態
                tblProduction.setStatus(2); //一時中断
                // 作業開始画面に遷移し、作業登録がされたら、選択されている生産実績を一時中断状態にし、
                tblProductionService.updateTblProduction(tblProduction, loginUser);
                // 生産中断履歴テーブルに中断時刻と作業IDを記録する。
                tblProductionSuspension.setSuspendedTimeMinutes(0);
                tblProductionSuspension.setStartDatetime(new java.util.Date());
                tblProductionSuspension.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), new java.util.Date()));
                createProductionSuspension(tblProductionSuspension, loginUser);
            } else {  //1:作業終了
                // 作業終了により一時中断を解除する。
                tblProduction.setStatus(0); //生産中
                tblProductionService.updateTblProduction(tblProduction, loginUser);
                // 生産中断履歴テーブルに中断時刻と作業IDを記録する。
                TblProductionSuspension updateProductionSuspension = getProductionIdAndEndDatetimeIsNull(tblProduction.getId());
                if (updateProductionSuspension != null) {
                    Date endDatetime = new java.util.Date();
                    //中断時刻取得
                    updateProductionSuspension.setSuspendedTimeMinutes(FileUtil.getDatePoorMinute(endDatetime, updateProductionSuspension.getStartDatetime()));
                    updateProductionSuspension.setEndDatetime(endDatetime);
                    updateProductionSuspension.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), endDatetime));
                    updateProductionSuspension(updateProductionSuspension, loginUser);
                }
            }

        } else if (tblProductionSuspension.getInterruptionFlag() == 0) {
            //再開
            //その他場合、０
            // 選択されている生産実績が中断または一時中断でないときは何もしない。
            if (tblProduction.getStatus() != null && (tblProduction.getStatus() != 1 && tblProduction.getStatus() != 2)) {
                setApplicationError(response, loginUser, "msg_error_not_suspend", "TblProduction");
                return response;
            }

            // 選択されている生産実績の状態を生産中に戻す。
            tblProduction.setStatus(0);
            tblProductionService.updateTblProduction(tblProduction, loginUser);

            // 生産中断履歴テーブルに中断時刻と作業IDを記録する。
            TblProductionSuspension updateProductionSuspension = getProductionIdAndEndDatetimeIsNull(tblProduction.getId());
            if (updateProductionSuspension != null) {
                Date endDatetime = new java.util.Date();
                //中断時刻取得
                updateProductionSuspension.setSuspendedTimeMinutes(FileUtil.getDatePoorMinute(endDatetime, updateProductionSuspension.getStartDatetime()));
                updateProductionSuspension.setEndDatetime(endDatetime);
                updateProductionSuspension.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), endDatetime));
                updateProductionSuspension(updateProductionSuspension, loginUser);
            }
        }

        setApplicationError(response, loginUser, "msg_record_updated", "TblProductionSuspension");
        return response;
    }
    
    

    /**
     * 生産中断履歴テーブル登録
     *
     * @param tblProductionSuspension
     * @param loginUser
     * @return
     */
    @Transactional
    public TblProductionSuspension createProductionSuspension(TblProductionSuspension tblProductionSuspension, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        tblProductionSuspension.setId(IDGenerator.generate());
        tblProductionSuspension.setCreateDate(new java.util.Date());
        tblProductionSuspension.setCreateUserUuid(loginUser.getUserUuid());
        tblProductionSuspension.setUpdateDate(new java.util.Date());
        tblProductionSuspension.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(tblProductionSuspension);
        return tblProductionSuspension;
    }

    /**
     * 生産中断履歴テーブル更新
     *
     * @param tblProductionSuspension
     * @param loginUser
     * @return
     */
    @Transactional
    public TblProductionSuspension updateProductionSuspension(TblProductionSuspension tblProductionSuspension, LoginUser loginUser) {
        tblProductionSuspension.setUpdateDate(new java.util.Date());
        tblProductionSuspension.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(tblProductionSuspension);
        return tblProductionSuspension;
    }

    /**
     * 終了日時 IS NULL 生産中断履歴1件取得(TblProductionSuspension型で返却)
     *
     * @param productionId
     * @return
     */
    public TblProductionSuspension getProductionIdAndEndDatetimeIsNull(String productionId) {
        Query query = entityManager.createNamedQuery("TblProductionSuspension.findByProductionIdAndEndDatetimeIsNull");
        query.setParameter("productionId", productionId);
        query.setMaxResults(1);
        try {
            return (TblProductionSuspension) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    
    
    /**
     * 終了日時 IS NULL 生産中断履歴1件取得(TblProductionSuspension型で返却)
     *
     * @param workId
     * @return
     */
    public TblProductionSuspension getWorkIdAndEndDatetimeIsNull(String workId) {
        Query query = entityManager.createNamedQuery("TblProductionSuspension.findByWorkIdAndEndDatetimeIsNull");
        query.setParameter("workId", workId);
        query.setMaxResults(1);
        try {
            return (TblProductionSuspension) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    

    /**
     * 生産中断履歴1件取得(TblProductionSuspension型で返却)
     *
     * @param id
     * @return
     */
    public TblProductionSuspension getProductionSuspensionSingleById(String id) {
        Query query = entityManager.createNamedQuery("TblProductionSuspension.findById");
        query.setParameter("id", id);
        try {
            return (TblProductionSuspension) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private BasicResponse setApplicationError(BasicResponse response, LoginUser loginUser, String dictKey, String errorContentsName) {
        setBasicResponseError(
                response, true, ErrorMessages.E201_APPLICATION, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
        );
        String logMessage = response.getErrorMessage() + ":" + errorContentsName;
        Logger.getLogger(TblWorkResource.class.getName()).log(Level.FINE, logMessage);
        return response;
    }

    private BasicResponse setBasicResponseError(BasicResponse response, boolean error, String errorCode, String errorMessage) {
        response.setError(error);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }
}
