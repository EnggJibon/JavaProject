/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.remodeling.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.batch.externalmold.choice.ExtMstChoiceService;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributes;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationPK;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationVo;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodelingService;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodelingVo;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResult;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultPK;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultVo;
import com.kmcj.karte.resources.mold.spec.MstMoldSpec;
import com.kmcj.karte.resources.mold.spec.MstMoldSpecPK;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistory;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.TimezoneConverter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

/**
 *
 * @author jiangxs
 */
@Dependent
public class TblMoldRemodelingDetailService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private TblMoldMaintenanceRemodelingService tblMoldMaintenanceRemodelingService;

    @Inject
    private MstChoiceService mstChoiceService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private ExtMstChoiceService extMstChoiceService;
    
    @Inject
    private CnfSystemService cnfSystemService;

    /**
     * 金型改造メンテナンステーブルより、新仕様が登録されていない改造データを取得し一覧に表示する。
     *
     * @param user
     * @return
     */
    public TblMoldMaintenanceRemodelingVo getMoldMaintenanceRemodelings(LoginUser user) {

        TblMoldMaintenanceRemodelingVo resTblMoldRemodelings = new TblMoldMaintenanceRemodelingVo();
        List<TblMoldMaintenanceRemodelingVo> tblMoldRemodelingList = new ArrayList<>();
        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findAllByRemodel");
        query.setParameter("mainteOrRemodel", CommonConstants.MAINTEORREMODEL_REMODEL);
        query.setParameter("endDatetime", CommonConstants.SYS_MAX_DATE);

        List<TblMoldMaintenanceRemodeling> list = query.getResultList();
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(user.getLangId(), "tbl_mold_maintenance_remodeling.remodeling_type");
        for (TblMoldMaintenanceRemodeling tblMoldRemodeling : list) {
            TblMoldMaintenanceRemodelingVo aresTblMoldRemodelings = new TblMoldMaintenanceRemodelingVo();
            MstMold mstMold = tblMoldRemodeling.getMstMold();
            if (mstMold != null) {
                //ID
                aresTblMoldRemodelings.setId(tblMoldRemodeling.getId());
                //金型UUID
                aresTblMoldRemodelings.setMoldUuid(tblMoldRemodeling.getMoldUuid());
                // 金型ID
                aresTblMoldRemodelings.setMoldId(mstMold.getMoldId() == null ? "" : mstMold.getMoldId());
                // 金型名称
                aresTblMoldRemodelings.setMoldName(mstMold.getMoldName() == null ? "" : mstMold.getMoldName());
                
                if(mstMold.getMoldId() != null && !"".equals(mstMold.getMoldId())){
                    if(FileUtil.checkExternal(entityManager, mstDictionaryService, mstMold.getMoldId(), user).isError()){
                        aresTblMoldRemodelings.setExternalFlg("1");
                    }else{
                        aresTblMoldRemodelings.setExternalFlg("0");
                    }
                }
                
                // 実施者
                Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
                userQuery.setParameter("uuid", tblMoldRemodeling.getCreateUserUuid());
                try {
                    MstUser users = (MstUser) userQuery.getSingleResult();
                    aresTblMoldRemodelings.setReportPersonName(users.getUserName() == null ? "" : users.getUserName());
                } catch (NoResultException e) {
                    aresTblMoldRemodelings.setReportPersonName("");
                }

                // 開始日時
                aresTblMoldRemodelings.setStartDatetime(tblMoldRemodeling.getStartDatetime() == null ? "" : new FileUtil().getDateTimeFormatForStr(tblMoldRemodeling.getStartDatetime()));
                //  終了日時
                if(tblMoldRemodeling.getEndDatetime() != null){
                    aresTblMoldRemodelings.setEndDatetime(tblMoldRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(tblMoldRemodeling.getEndDatetime()));
                }else{
                    aresTblMoldRemodelings.setEndDatetime("-");
                }
                
                //改造区分 
                if (tblMoldRemodeling.getRemodelingType() != null) {
                    if (FileUtil.checkExternal(entityManager, mstDictionaryService, mstMold.getMoldId(), user).isError() == true) {
                        aresTblMoldRemodelings.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(mstMold.getCompanyId(), "tbl_mold_maintenance_remodeling.remodeling_type", "" + tblMoldRemodeling.getRemodelingType(), user.getLangId()));
                    } else {
                        aresTblMoldRemodelings.setRemodelingType(tblMoldRemodeling.getRemodelingType());
                        for (int momi = 0; momi < mstChoiceList.getMstChoice().size(); momi++) {
                            MstChoice aMstChoice = mstChoiceList.getMstChoice().get(momi);
                            if (aMstChoice.getMstChoicePK().getSeq().equals(tblMoldRemodeling.getRemodelingType().toString())) {
                                aresTblMoldRemodelings.setRemodelingTypeText(aMstChoice.getChoice());
                                break;
                            }
                        }
                    }
                } else {
                    aresTblMoldRemodelings.setRemodelingTypeText("");
                }

                //報告事項	
                aresTblMoldRemodelings.setReport(tblMoldRemodeling.getReport() == null ? "" : tblMoldRemodeling.getReport());
                //手配・工事内容
                aresTblMoldRemodelings.setDirectionId(tblMoldRemodeling.getDirectionId() == null ? "" : tblMoldRemodeling.getDirectionId());
                if(tblMoldRemodeling.getTblDirection() != null){
                    aresTblMoldRemodelings.setDirectionCode(tblMoldRemodeling.getTblDirection().getDirectionCode() == null ? "" : tblMoldRemodeling.getTblDirection().getDirectionCode());
                }else{
                    aresTblMoldRemodelings.setDirectionCode("");
                }
                tblMoldRemodelingList.add(aresTblMoldRemodelings);
            }
        }
        resTblMoldRemodelings.setMoldMaintenanceRemodelingVo(tblMoldRemodelingList);

        return resTblMoldRemodelings;
    }

    /**
     * 一覧で選択されているレコードを削除し、金型改造をなかったころにする。確認メッセージを表示し、キャンセルボタンが押されれば処理を取り消す。
     *
     * @param user
     * @param id
     * @return
     */
    @Transactional
    public int deleteRemodeling(LoginUser user, String id) {
        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.deleteById");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean getRemodelingExistCheck(String id) {
        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findById");
        query.setParameter("id", id);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * TT0009 金型改造終了入力 再開 終了している金型改造のステータスを改造中に戻す。 終了していない金型改造の場合は、処理を行えない。
     *
     * @param moldId
     * @param mainteOrRemodel
     * @param user
     * @return 
     */
    @Transactional
    public BasicResponse putRemodelingResumption(String moldId, int mainteOrRemodel, LoginUser user) {
        BasicResponse basicResponse = new BasicResponse();
        Query queryMold = entityManager.createNamedQuery("MstMold.findByMoldId");
        queryMold.setParameter("moldId", moldId);

        MstMold mold = (MstMold) queryMold.getSingleResult();
        mold.setMainteStatus(CommonConstants.MAINTE_STATUS_REMODELING);
        mold.setUpdateDate(new Date());
        mold.setUpdateUserUuid(user.getUserUuid());
        entityManager.merge(mold);

        TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo = (TblMoldMaintenanceRemodelingVo) tblMoldMaintenanceRemodelingService.getMoldmainteOrRemodelDetail(moldId, mainteOrRemodel, user);
        String id = tblMoldMaintenanceRemodelingVo.getId();

        StringBuilder sql = new StringBuilder(" UPDATE TblMoldMaintenanceRemodeling t SET ");
        sql.append(" t.startDatetime = :startDatetime, ");
        sql.append(" t.startDatetimeStz = :startDatetimeStz, ");
        sql.append(" t.endDatetime = :endDatetime, ");
        sql.append(" t.endDatetimeStz = :endDatetimeStz, ");
        sql.append(" t.updateDate = :updateDate, ");
        sql.append(" t.updateUserUuid = :updateUserUuid ");
        sql.append(" WHERE t.id = :id ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("startDatetime", TimezoneConverter.getLocalTime(user.getJavaZoneId()));
        query.setParameter("startDatetimeStz", new Date());
        query.setParameter("endDatetime", null);
        query.setParameter("endDatetimeStz", null);
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", user.getUserUuid());
        query.setParameter("id", id);

        query.executeUpdate();
        
        return basicResponse;
    }

    /**
     * TT0009 金型改造終了入力 開始取消 選択された金型改造の開始をなかったことにするため、データベースからレコードを削除する。
     *
     * @param moldId
     * @param user
     * @return 
     */
    @Transactional
    public BasicResponse putRemodelingStartCancel(String moldId, LoginUser user) {
        BasicResponse basicResponse = new BasicResponse();
        Query queryMold = entityManager.createNamedQuery("MstMold.findByMoldId");
        queryMold.setParameter("moldId", moldId);

        MstMold mold = (MstMold) queryMold.getSingleResult();
        mold.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL);
        mold.setUpdateDate(new Date());
        mold.setUpdateUserUuid(user.getUserUuid());
        entityManager.merge(mold);

        TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo = (TblMoldMaintenanceRemodelingVo) tblMoldMaintenanceRemodelingService.getMoldmainteOrRemodelDetail(moldId, CommonConstants.MAINTE_STATUS_REMODELING, user);
        String id = tblMoldMaintenanceRemodelingVo.getId();

        StringBuffer sql = new StringBuffer(" DELETE FROM TblMoldMaintenanceRemodeling t ");
        sql.append(" WHERE t.id = :id ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("id", id);

        query.executeUpdate();
        
        return basicResponse;
    }

    /**
     * 金型改造詳細複数を取得可能
     *
     * @param loginUser
     * @param moldId
     * @param mainteId
     * @return
     */
    public TblMoldMaintenanceRemodelingVo getTblMoldRemodelingDetailByMoldId(LoginUser loginUser, String moldId, String mainteId) {
        TblMoldMaintenanceRemodelingVo trdvL = new TblMoldMaintenanceRemodelingVo();
        TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling;
        Query query;
        if (null != mainteId && !mainteId.trim().equals("")) {
            query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findById");
            query.setParameter("id", mainteId);
        } else if (null != moldId && !moldId.trim().equals("")) {
            query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findByMoldId");
            query.setParameter("moldId", moldId);
            query.setParameter("mainteOrRemodel", CommonConstants.MAINTEORREMODEL_REMODEL);
            query.setMaxResults(1);
        } else {
            return trdvL;
        }

        List<TblMoldRemodelingDetailVo> tblMoldRemodelingDetailVoList = new ArrayList<>();
        try {
            tblMoldMaintenanceRemodeling = (TblMoldMaintenanceRemodeling) query.getSingleResult();

            //maintenanceid
            trdvL.setId(tblMoldMaintenanceRemodeling.getId());
            MstMold mold = tblMoldMaintenanceRemodeling.getMstMold();
            if (FileUtil.checkExternal(entityManager, mstDictionaryService, mold.getMoldId(), loginUser).isError()) {
                trdvL.setExternalFlg("1");
                //メンテ分類
                if (null != tblMoldMaintenanceRemodeling.getMainteType()) {
                    trdvL.setMainteType(tblMoldMaintenanceRemodeling.getMainteType());
                    trdvL.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_remodeling.mainte_type", String.valueOf(tblMoldMaintenanceRemodeling.getMainteType()), loginUser.getLangId()));
                } else {
                    trdvL.setMainteTypeText("");
                }

                //改造分類
                if (null != tblMoldMaintenanceRemodeling.getRemodelingType()) {
                    trdvL.setRemodelingType(tblMoldMaintenanceRemodeling.getRemodelingType());
                    trdvL.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_remodeling.remodeling_type", String.valueOf(tblMoldMaintenanceRemodeling.getRemodelingType()), loginUser.getLangId()));
                } else {
                    trdvL.setRemodelingTypeText("");
                }

            } else {
                trdvL.setExternalFlg("0");
                trdvL.setMainteType(tblMoldMaintenanceRemodeling.getMainteType());
                trdvL.setRemodelingType(tblMoldMaintenanceRemodeling.getRemodelingType());
            }
            //報告事項
            trdvL.setReport(tblMoldMaintenanceRemodeling.getReport() == null ? "" : tblMoldMaintenanceRemodeling.getReport());

            trdvL.setDirectionId(tblMoldMaintenanceRemodeling.getDirectionId() == null ? "" : tblMoldMaintenanceRemodeling.getDirectionId());
            if (tblMoldMaintenanceRemodeling.getTblDirection() != null) {
                trdvL.setDirectionCode(tblMoldMaintenanceRemodeling.getTblDirection().getDirectionCode() == null ? "" : tblMoldMaintenanceRemodeling.getTblDirection().getDirectionCode());
            } else {
                if (tblMoldMaintenanceRemodeling.getDirectionCode() != null) {
                    trdvL.setDirectionCode(tblMoldMaintenanceRemodeling.getDirectionCode());
                }
                else {
                    trdvL.setDirectionCode("");
                }
            }

            trdvL.setMoldName(tblMoldMaintenanceRemodeling.getMstMold().getMoldName());

            if (tblMoldMaintenanceRemodeling.getTblMoldRemodelingDetailCollection() != null && tblMoldMaintenanceRemodeling.getTblMoldRemodelingDetailCollection().size() > 0) {
                List<TblMoldRemodelingDetail> tblMoldRemodelingDetailList = new ArrayList<>(tblMoldMaintenanceRemodeling.getTblMoldRemodelingDetailCollection());
                for (TblMoldRemodelingDetail tblMoldRemodelingDetail : tblMoldRemodelingDetailList) {
                    TblMoldRemodelingDetailVo tblMoldRemodelingDetailVo = new TblMoldRemodelingDetailVo();

                    tblMoldRemodelingDetailVo.setId(tblMoldRemodelingDetail.getId());
                    tblMoldRemodelingDetailVo.setMaintenanceId(tblMoldRemodelingDetail.getTblMoldRemodelingDetailPK().getMaintenanceId());
                    tblMoldRemodelingDetailVo.setSeq(tblMoldRemodelingDetail.getTblMoldRemodelingDetailPK().getSeq());

                    //改造理由大分類
                    tblMoldRemodelingDetailVo.setRemodelReasonCategory1(String.valueOf(tblMoldRemodelingDetail.getRemodelReasonCategory1() == null ? "" : tblMoldRemodelingDetail.getRemodelReasonCategory1()));
                    tblMoldRemodelingDetailVo.setRemodelReasonCategory1Text(tblMoldRemodelingDetail.getRemodelReasonCategory1Text() == null ? "" : tblMoldRemodelingDetail.getRemodelReasonCategory1Text());
                    //改造理由中分類
                    tblMoldRemodelingDetailVo.setRemodelReasonCategory2(String.valueOf(tblMoldRemodelingDetail.getRemodelReasonCategory2() == null ? "" : tblMoldRemodelingDetail.getRemodelReasonCategory2()));
                    tblMoldRemodelingDetailVo.setRemodelReasonCategory2Text(tblMoldRemodelingDetail.getRemodelReasonCategory2Text() == null ? "" : tblMoldRemodelingDetail.getRemodelReasonCategory2Text());
                    //改造理由小分類
                    tblMoldRemodelingDetailVo.setRemodelReasonCategory3(String.valueOf(tblMoldRemodelingDetail.getRemodelReasonCategory3() == null ? "" : tblMoldRemodelingDetail.getRemodelReasonCategory3()));
                    tblMoldRemodelingDetailVo.setRemodelReasonCategory3Text(tblMoldRemodelingDetail.getRemodelReasonCategory3Text() == null ? "" : tblMoldRemodelingDetail.getRemodelReasonCategory3Text());
                    //改造理由
                    tblMoldRemodelingDetailVo.setRemodelReason(tblMoldRemodelingDetail.getRemodelReason() == null ? "" : tblMoldRemodelingDetail.getRemodelReason());

                    //改造指示大分類
                    tblMoldRemodelingDetailVo.setRemodelDirectionCategory1(String.valueOf(tblMoldRemodelingDetail.getRemodelDirectionCategory1() == null ? "" : tblMoldRemodelingDetail.getRemodelDirectionCategory1()));
                    tblMoldRemodelingDetailVo.setRemodelDirectionCategory1Text(tblMoldRemodelingDetail.getRemodelDirectionCategory1Text() == null ? "" : tblMoldRemodelingDetail.getRemodelDirectionCategory1Text());
                    //改造指示中分類
                    tblMoldRemodelingDetailVo.setRemodelDirectionCategory2(String.valueOf(tblMoldRemodelingDetail.getRemodelDirectionCategory2() == null ? "" : tblMoldRemodelingDetail.getRemodelDirectionCategory2()));
                    tblMoldRemodelingDetailVo.setRemodelDirectionCategory2Text(tblMoldRemodelingDetail.getRemodelDirectionCategory2Text() == null ? "" : tblMoldRemodelingDetail.getRemodelDirectionCategory2Text());
                    //改造指示小分類
                    tblMoldRemodelingDetailVo.setRemodelDirectionCategory3(String.valueOf(tblMoldRemodelingDetail.getRemodelDirectionCategory3() == null ? "" : tblMoldRemodelingDetail.getRemodelDirectionCategory3()));
                    tblMoldRemodelingDetailVo.setRemodelDirectionCategory3Text(tblMoldRemodelingDetail.getRemodelDirectionCategory3Text() == null ? "" : tblMoldRemodelingDetail.getRemodelDirectionCategory3Text());
                    //改造指示
                    tblMoldRemodelingDetailVo.setRemodelDirection(tblMoldRemodelingDetail.getRemodelDirection() == null ? "" : tblMoldRemodelingDetail.getRemodelDirection());

                    //作業大分類
                    tblMoldRemodelingDetailVo.setTaskCategory1(String.valueOf(tblMoldRemodelingDetail.getTaskCategory1() == null ? "" : tblMoldRemodelingDetail.getTaskCategory1()));
                    tblMoldRemodelingDetailVo.setTaskCategory1Text(tblMoldRemodelingDetail.getTaskCategory1Text() == null ? "" : tblMoldRemodelingDetail.getTaskCategory1Text());
                    //作業中分類
                    tblMoldRemodelingDetailVo.setTaskCategory2(String.valueOf(tblMoldRemodelingDetail.getTaskCategory2() == null ? "" : tblMoldRemodelingDetail.getTaskCategory2()));
                    tblMoldRemodelingDetailVo.setTaskCategory2Text(tblMoldRemodelingDetail.getTaskCategory2Text() == null ? "" : tblMoldRemodelingDetail.getTaskCategory2Text());
                    //作業小分類
                    tblMoldRemodelingDetailVo.setTaskCategory3(String.valueOf(tblMoldRemodelingDetail.getTaskCategory3() == null ? "" : tblMoldRemodelingDetail.getTaskCategory3()));
                    tblMoldRemodelingDetailVo.setTaskCategory3Text(tblMoldRemodelingDetail.getTaskCategory3Text() == null ? "" : tblMoldRemodelingDetail.getTaskCategory3Text());
                    //作業
                    tblMoldRemodelingDetailVo.setTask(tblMoldRemodelingDetail.getTask() == null ? "" : tblMoldRemodelingDetail.getTask());
                    
                    List<TblMoldRemodelingDetailImageFileVo> fileResultVos = new ArrayList<>();
                    //TblMoldMaintenanceDetailImageFileVo　検索
                    List<TblMoldRemodelingDetailImageFile> tmpFileResults = entityManager.createQuery("SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.tblMoldRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId ")
                            .setParameter("remodelingDetailId", tblMoldRemodelingDetail.getId())
                            .getResultList();

                    TblMoldRemodelingDetailImageFileVo moldRemodelingDetailImageFileVo = null;
                    for (int i = 0; i < tmpFileResults.size(); i++) {
                        TblMoldRemodelingDetailImageFile aFile = tmpFileResults.get(i);
                        moldRemodelingDetailImageFileVo = new TblMoldRemodelingDetailImageFileVo();
                        moldRemodelingDetailImageFileVo.setRemodelingDetailId(tblMoldRemodelingDetail.getId());
                        moldRemodelingDetailImageFileVo.setSeq("" + aFile.getTblMoldRemodelingDetailImageFilePK().getSeq());
                        moldRemodelingDetailImageFileVo.setFileType("" + aFile.getFileType());
                        moldRemodelingDetailImageFileVo.setFileExtension(aFile.getFileExtension());
                        moldRemodelingDetailImageFileVo.setFileUuid(aFile.getFileUuid());
                        moldRemodelingDetailImageFileVo.setRemarks(aFile.getRemarks());
                        moldRemodelingDetailImageFileVo.setThumbnailFileUuid(aFile.getThumbnailFileUuid());
                        if (null != aFile.getTakenDate()) {
                            moldRemodelingDetailImageFileVo.setTakenDateStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDate()));
                        }
                        if (null != aFile.getTakenDateStz()) {
                            moldRemodelingDetailImageFileVo.setTakenDateStzStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDateStz()));
                        }

                        fileResultVos.add(moldRemodelingDetailImageFileVo);
                    }
                    tblMoldRemodelingDetailVo.setMoldRemodelingDetailImageFileVos(fileResultVos);

                    tblMoldRemodelingDetailVoList.add(tblMoldRemodelingDetailVo);
                }
            }
            trdvL.setMoldRemodelingDetailVo(tblMoldRemodelingDetailVoList);
        } catch (NoResultException e) {
            return trdvL;
        }
        return trdvL;
    }

    /**
     * TT0009 金型改造終了入力 登録 金型改造内容を登録し、金型のメンテ状態を戻すために、データベースを更新する。
     *
     * @param tblMoldMaintenanceRemodelingVo
     * @param user
     * @return 
     */
    @Transactional
    public BasicResponse postMoldRemodelingDetailes(TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo, LoginUser user) {
        BasicResponse response = new BasicResponse();
        String maintenanceId = tblMoldMaintenanceRemodelingVo.getId();
        
        if(!getRemodelingExistCheck(maintenanceId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        // 外部データチェック
        String moldId = tblMoldMaintenanceRemodelingVo.getMoldId();
        response = FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, user);
        if(response.isError()){
            return response;
        }

        // メンテナンスの一次保存機能 2018/09/13 -S
        if (0 == tblMoldMaintenanceRemodelingVo.getTemporarilySaved()) {
            //金型マスタのメンテ状態を改造中から通常に戻す。
            Query queryMold = entityManager.createNamedQuery("MstMold.findByMoldId");
            queryMold.setParameter("moldId", moldId);

            MstMold mold = (MstMold) queryMold.getSingleResult();
            mold.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL);
            // 4.2 対応　BY LiuYoudong S
            CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
            mold.setLastMainteDate(DateFormat.strToDate(DateFormat.getBusinessDate(DateFormat.dateToStr(new Date(), DateFormat.DATETIME_FORMAT), cnfSystem))); // 最終メンテナンス日
            if (tblMoldMaintenanceRemodelingVo.isResetAfterMainteTotalProducingTimeHourFlag()) {
                mold.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO); // メンテナンス後生産時間
            }
            if (tblMoldMaintenanceRemodelingVo.isResetAfterMainteTotalShotCountFlag()) {
                mold.setAfterMainteTotalShotCount(0); // メンテナンス後ショット数
            }

            // 4.2 対応　BY LiuYoudong E
            mold.setUpdateDate(new Date());
            mold.setUpdateUserUuid(user.getUserUuid());
            entityManager.merge(mold);
        }
        // メンテナンスの一次保存機能 2018/09/13 -E

        //金型改造メンテナンス 終了日時、改造分類、報告事項を設定
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
        
        StringBuilder sql = new StringBuilder(" UPDATE TblMoldMaintenanceRemodeling t SET ");
        sql.append(" t.remodelingType = :remodelingType, ");
        if (tblMoldMaintenanceRemodelingVo.getReport() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getReport())) {
            sql.append(" t.report = :report, ");
        }
        if(tblMoldMaintenanceRemodelingVo.getDirectionId() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getDirectionId())){
            sql.append(" t.directionId = :directionId, ");
        }
        if(tblMoldMaintenanceRemodelingVo.getDirectionCode() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getDirectionCode())){
            sql.append(" t.directionCode = :directionCode, ");
        }

        // メンテナンスの一次保存機能 2018/09/13 -S
        if (0 == tblMoldMaintenanceRemodelingVo.getTemporarilySaved()) {
            sql.append(" t.endDatetime = :endDatetime, ");
            sql.append(" t.endDatetimeStz = :endDatetimeStz, ");
        }
        // メンテナンスの一次保存機能 2018/09/13 -E
        sql.append(" t.updateDate = :updateDate, ");
        sql.append(" t.updateUserUuid = :updateUserUuid ");
        sql.append(" WHERE t.id = :id ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("remodelingType", tblMoldMaintenanceRemodelingVo.getRemodelingType());
        if (tblMoldMaintenanceRemodelingVo.getReport() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getReport())) {
            query.setParameter("report", tblMoldMaintenanceRemodelingVo.getReport());
        }
        if (tblMoldMaintenanceRemodelingVo.getDirectionId() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getDirectionId())) {
            query.setParameter("directionId", tblMoldMaintenanceRemodelingVo.getDirectionId());
        }
        if (tblMoldMaintenanceRemodelingVo.getDirectionCode() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getDirectionCode())) {
            query.setParameter("directionCode", tblMoldMaintenanceRemodelingVo.getDirectionCode());
        }

        // メンテナンスの一次保存機能 2018/09/13 -S
        if (0 == tblMoldMaintenanceRemodelingVo.getTemporarilySaved()) {
            query.setParameter("endDatetimeStz", new Date());
            try {
                query.setParameter("endDatetime", sdf.parse(DateFormat.dateTimeFormat(new Date(), user.getJavaZoneId())));
            } catch (ParseException ex) {
                Logger.getLogger(TblMoldRemodelingDetailService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // メンテナンスの一次保存機能 2018/09/13 -E
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", user.getUserUuid());
        query.setParameter("id", maintenanceId);

        query.executeUpdate();

        //金型改造詳細 追加・更新 再開の場合は更新 
        if (tblMoldMaintenanceRemodelingVo.getMoldRemodelingDetailVo() != null && tblMoldMaintenanceRemodelingVo.getMoldRemodelingDetailVo().size() > 0) {
            for (TblMoldRemodelingDetailVo moldRemodelingDetailVo : tblMoldMaintenanceRemodelingVo.getMoldRemodelingDetailVo()) {

                int Seq = moldRemodelingDetailVo.getSeq();
                String remodelReasonCategory1 = "";
                String remodelReasonCategory2 = "";
                String remodelReasonCategory3 = "";
                String remodelReason = "";
                String remodelDirectionCategory1 = "";
                String remodelDirectionCategory2 = "";
                String remodelDirectionCategory3 = "";
                String remodelDirection = "";
                String taskCategory1 = "";
                String taskCategory2 = "";
                String taskCategory3 = "";
                String task = "";

                String id = "";
                if (checkTblMoldRemodelingDetail(maintenanceId, Seq)) {
                    //更新
                    StringBuilder detailSql = new StringBuilder("UPDATE TblMoldRemodelingDetail t SET ");
                    if (moldRemodelingDetailVo.getRemodelReasonCategory1() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory1())) {
                        remodelReasonCategory1 = moldRemodelingDetailVo.getRemodelReasonCategory1();
                        detailSql.append(" t.remodelReasonCategory1 = :remodelReasonCategory1, ");
                        detailSql.append(" t.remodelReasonCategory1Text = :remodelReasonCategory1Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelReasonCategory2() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory2())) {
                        remodelReasonCategory2 = moldRemodelingDetailVo.getRemodelReasonCategory2();
                        detailSql.append(" t.remodelReasonCategory2 = :remodelReasonCategory2, ");
                        detailSql.append(" t.remodelReasonCategory2Text = :remodelReasonCategory2Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelReasonCategory3() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory3())) {
                        remodelReasonCategory3 = moldRemodelingDetailVo.getRemodelReasonCategory3();
                        detailSql.append(" t.remodelReasonCategory3 = :remodelReasonCategory3, ");
                        detailSql.append(" t.remodelReasonCategory3Text = :remodelReasonCategory3Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelReason() != null && !"".equals(moldRemodelingDetailVo.getRemodelReason())) {
                        remodelReason = moldRemodelingDetailVo.getRemodelReason();
                        detailSql.append(" t.remodelReason = :remodelReason, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory1() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory1())) {
                        remodelDirectionCategory1 = moldRemodelingDetailVo.getRemodelDirectionCategory1();
                        detailSql.append(" t.remodelDirectionCategory1 = :remodelDirectionCategory1, ");
                        detailSql.append(" t.remodelDirectionCategory1Text = :remodelDirectionCategory1Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory2() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory2())) {
                        remodelDirectionCategory2 = moldRemodelingDetailVo.getRemodelDirectionCategory2();
                        detailSql.append(" t.remodelDirectionCategory2 = :remodelDirectionCategory2, ");
                        detailSql.append(" t.remodelDirectionCategory2Text = :remodelDirectionCategory2Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory3() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory3())) {
                        remodelDirectionCategory3 = moldRemodelingDetailVo.getRemodelDirectionCategory3();
                        detailSql.append(" t.remodelDirectionCategory3 = :remodelDirectionCategory3, ");
                        detailSql.append(" t.remodelDirectionCategory3Text = :remodelDirectionCategory3Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelDirection() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirection())) {
                        remodelDirection = moldRemodelingDetailVo.getRemodelDirection();
                        detailSql.append(" t.remodelDirection = :remodelDirection, ");
                    }
                    if (moldRemodelingDetailVo.getTaskCategory1() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory1())) {
                        taskCategory1 = moldRemodelingDetailVo.getTaskCategory1();
                        detailSql.append(" t.taskCategory1 = :taskCategory1, ");
                        detailSql.append(" t.taskCategory1Text = :taskCategory1Text, ");
                    }
                    if (moldRemodelingDetailVo.getTaskCategory2() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory2())) {
                        taskCategory2 = moldRemodelingDetailVo.getTaskCategory2();
                        detailSql.append(" t.taskCategory2 = :taskCategory2, ");
                        detailSql.append(" t.taskCategory2Text = :taskCategory2Text, ");
                    }
                    if (moldRemodelingDetailVo.getTaskCategory3() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory3())) {
                        taskCategory3 = moldRemodelingDetailVo.getTaskCategory3();
                        detailSql.append(" t.taskCategory3 = :taskCategory3, ");
                        detailSql.append(" t.taskCategory3Text = :taskCategory3Text, ");
                    }
                    if (moldRemodelingDetailVo.getTask() != null && !"".equals(moldRemodelingDetailVo.getTask())) {
                        task = moldRemodelingDetailVo.getTask();
                        detailSql.append(" t.task = :task, ");
                    }
                    detailSql.append(" t.updateDate = :updateDate,t.updateUserUuid = :updateUserUuid ");
                    detailSql.append(" WHERE t.tblMoldRemodelingDetailPK.maintenanceId = :maintenanceId ");
                    detailSql.append(" And t.tblMoldRemodelingDetailPK.seq = :seq ");

                    Query detailQuery = entityManager.createQuery(detailSql.toString());

                    if (remodelReasonCategory1 != null && !"".equals(remodelReasonCategory1)) {
                        detailQuery.setParameter("remodelReasonCategory1", Integer.parseInt(remodelReasonCategory1));
                        detailQuery.setParameter("remodelReasonCategory1Text", moldRemodelingDetailVo.getRemodelReasonCategory1Text() == null ? "" : moldRemodelingDetailVo.getRemodelReasonCategory1Text());
                    }
                    if (remodelReasonCategory2 != null && !"".equals(remodelReasonCategory2)) {
                        detailQuery.setParameter("remodelReasonCategory2", Integer.parseInt(remodelReasonCategory2));
                        detailQuery.setParameter("remodelReasonCategory2Text", moldRemodelingDetailVo.getRemodelReasonCategory2Text() == null ? "" : moldRemodelingDetailVo.getRemodelReasonCategory2Text());
                    }
                    if (remodelReasonCategory3 != null && !"".equals(remodelReasonCategory3)) {
                        detailQuery.setParameter("remodelReasonCategory3", Integer.parseInt(remodelReasonCategory3));
                        detailQuery.setParameter("remodelReasonCategory3Text", moldRemodelingDetailVo.getRemodelReasonCategory3Text() == null ? "" : moldRemodelingDetailVo.getRemodelReasonCategory3Text());
                    }
                    if (remodelReason != null && !"".equals(remodelReason)) {
                        detailQuery.setParameter("remodelReason", remodelReason);
                    }
                    if (remodelDirectionCategory1 != null && !"".equals(remodelDirectionCategory1)) {
                        detailQuery.setParameter("remodelDirectionCategory1", Integer.parseInt(remodelDirectionCategory1));
                        detailQuery.setParameter("remodelDirectionCategory1Text", moldRemodelingDetailVo.getRemodelDirectionCategory1Text() == null ? "" : moldRemodelingDetailVo.getRemodelDirectionCategory1Text());
                    }
                    if (remodelDirectionCategory2 != null && !"".equals(remodelDirectionCategory2)) {
                        detailQuery.setParameter("remodelDirectionCategory2", Integer.parseInt(remodelDirectionCategory2));
                        detailQuery.setParameter("remodelDirectionCategory2Text", moldRemodelingDetailVo.getRemodelDirectionCategory2Text() == null ? "" : moldRemodelingDetailVo.getRemodelDirectionCategory2Text());
                    }
                    if (remodelDirectionCategory3 != null && !"".equals(remodelDirectionCategory3)) {
                        detailQuery.setParameter("remodelDirectionCategory3", Integer.parseInt(remodelDirectionCategory3));
                        detailQuery.setParameter("remodelDirectionCategory3Text", moldRemodelingDetailVo.getRemodelDirectionCategory3Text() == null ? "" : moldRemodelingDetailVo.getRemodelDirectionCategory3Text());
                    }
                    if (remodelDirection != null && !"".equals(remodelDirection)) {
                        detailQuery.setParameter("remodelDirection", remodelDirection);
                    }
                    if (taskCategory1 != null && !"".equals(taskCategory1)) {
                        detailQuery.setParameter("taskCategory1", Integer.parseInt(taskCategory1));
                        detailQuery.setParameter("taskCategory1Text", moldRemodelingDetailVo.getTaskCategory1Text() == null ? "" : moldRemodelingDetailVo.getTaskCategory1Text());
                    }
                    if (taskCategory2 != null && !"".equals(taskCategory2)) {
                        detailQuery.setParameter("taskCategory2", Integer.parseInt(taskCategory2));
                        detailQuery.setParameter("taskCategory2Text", moldRemodelingDetailVo.getTaskCategory2Text() == null ? "" : moldRemodelingDetailVo.getTaskCategory2Text());
                    }
                    if (taskCategory3 != null && !"".equals(taskCategory3)) {
                        detailQuery.setParameter("taskCategory3", Integer.parseInt(taskCategory3));
                        detailQuery.setParameter("taskCategory3Text", moldRemodelingDetailVo.getTaskCategory3Text() == null ? "" : moldRemodelingDetailVo.getTaskCategory3Text());
                    }
                    if (task != null && !"".equals(task)) {
                        detailQuery.setParameter("task", task);
                    }
                    detailQuery.setParameter("updateDate", new Date());
                    detailQuery.setParameter("updateUserUuid", user.getUserUuid());
                    detailQuery.setParameter("maintenanceId", maintenanceId);
                    detailQuery.setParameter("seq", moldRemodelingDetailVo.getSeq());

                    detailQuery.executeUpdate();
                    id = moldRemodelingDetailVo.getId();
                } else {
                    //追加 
                    TblMoldRemodelingDetail tblMoldRemodelingDetail = new TblMoldRemodelingDetail();
                    TblMoldRemodelingDetailPK tblMoldRemodelingDetailPK = new TblMoldRemodelingDetailPK();
                    tblMoldRemodelingDetail.setId(IDGenerator.generate());
                    tblMoldRemodelingDetailPK.setMaintenanceId(moldRemodelingDetailVo.getMaintenanceId());
                    tblMoldRemodelingDetailPK.setSeq(moldRemodelingDetailVo.getSeq());
                    tblMoldRemodelingDetail.setTblMoldRemodelingDetailPK(tblMoldRemodelingDetailPK);
                    if (moldRemodelingDetailVo.getRemodelReasonCategory1() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory1())) {
                        tblMoldRemodelingDetail.setRemodelReasonCategory1(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory1()));
                        tblMoldRemodelingDetail.setRemodelReasonCategory1Text(moldRemodelingDetailVo.getRemodelReasonCategory1Text());
                    }
                    if (moldRemodelingDetailVo.getRemodelReasonCategory2() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory2())) {
                        tblMoldRemodelingDetail.setRemodelReasonCategory2(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory2()));
                        tblMoldRemodelingDetail.setRemodelReasonCategory2Text(moldRemodelingDetailVo.getRemodelReasonCategory2Text());
                    }
                    if (moldRemodelingDetailVo.getRemodelReasonCategory3() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory3())) {
                        tblMoldRemodelingDetail.setRemodelReasonCategory3(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory3()));
                        tblMoldRemodelingDetail.setRemodelReasonCategory3Text(moldRemodelingDetailVo.getRemodelReasonCategory3Text());
                    }
                    tblMoldRemodelingDetail.setRemodelReason(moldRemodelingDetailVo.getRemodelReason());
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory1() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory1())) {
                        tblMoldRemodelingDetail.setRemodelDirectionCategory1(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory1()));
                        tblMoldRemodelingDetail.setRemodelDirectionCategory1Text(moldRemodelingDetailVo.getRemodelDirectionCategory1Text());
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory2() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory2())) {
                        tblMoldRemodelingDetail.setRemodelDirectionCategory2(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory2()));
                        tblMoldRemodelingDetail.setRemodelDirectionCategory2Text(moldRemodelingDetailVo.getRemodelDirectionCategory2Text());
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory3() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory3())) {
                        tblMoldRemodelingDetail.setRemodelDirectionCategory3(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory3()));
                        tblMoldRemodelingDetail.setRemodelDirectionCategory3Text(moldRemodelingDetailVo.getRemodelDirectionCategory3Text());
                    }

                    tblMoldRemodelingDetail.setRemodelDirection(moldRemodelingDetailVo.getRemodelDirection());
                    if (moldRemodelingDetailVo.getTaskCategory1() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory1())) {
                        tblMoldRemodelingDetail.setTaskCategory1(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory1()));
                        tblMoldRemodelingDetail.setTaskCategory1Text(moldRemodelingDetailVo.getTaskCategory1Text());
                    }
                    if (moldRemodelingDetailVo.getTaskCategory2() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory2())) {
                        tblMoldRemodelingDetail.setTaskCategory2(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory2()));
                        tblMoldRemodelingDetail.setTaskCategory2Text(moldRemodelingDetailVo.getTaskCategory2Text());
                    }
                    if (moldRemodelingDetailVo.getTaskCategory3() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory3())) {
                        tblMoldRemodelingDetail.setTaskCategory3(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory3()));
                        tblMoldRemodelingDetail.setTaskCategory3Text(moldRemodelingDetailVo.getTaskCategory3Text());
                    }

                    tblMoldRemodelingDetail.setTask(moldRemodelingDetailVo.getTask());
                    tblMoldRemodelingDetail.setCreateDate(new Date());
                    tblMoldRemodelingDetail.setCreateUserUuid(user.getUserUuid());
                    tblMoldRemodelingDetail.setUpdateDate(new Date());
                    tblMoldRemodelingDetail.setUpdateUserUuid(user.getUserUuid());
                    entityManager.persist(tblMoldRemodelingDetail);
                    id = tblMoldRemodelingDetail.getId();
                }
                
                //設備点検結果 追加
                List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVo = moldRemodelingDetailVo.getMoldRemodelingInspectionResultVos();
                if (moldRemodelingInspectionResultVo != null && !moldRemodelingInspectionResultVo.isEmpty()) {
                    if (null != moldRemodelingDetailVo.getId()) {
                        Query delInspectionResultQuery = entityManager.createQuery("DELETE FROM TblMoldRemodelingInspectionResult t WHERE t.tblMoldRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId");
                        delInspectionResultQuery.setParameter("remodelingDetailId", moldRemodelingDetailVo.getId());
                        delInspectionResultQuery.executeUpdate();
                    }

                    for (int j = 0; j < moldRemodelingInspectionResultVo.size(); j++) {
                        TblMoldRemodelingInspectionResultVo aMoldRemodelingInspectionResultVo = moldRemodelingInspectionResultVo.get(j);
                        TblMoldRemodelingInspectionResult aRemodelingResult = new TblMoldRemodelingInspectionResult();
                        aRemodelingResult.setUpdateDate(new Date());
                        aRemodelingResult.setUpdateUserUuid(user.getUserUuid());
                        aRemodelingResult.setInspectionResult(aMoldRemodelingInspectionResultVo.getInspectionResult());
                        aRemodelingResult.setInspectionResultText(aMoldRemodelingInspectionResultVo.getInspectionResultText());

                        aRemodelingResult.setId(IDGenerator.generate());
                        TblMoldRemodelingInspectionResultPK resultPK = new TblMoldRemodelingInspectionResultPK();
                        resultPK.setInspectionItemId(aMoldRemodelingInspectionResultVo.getInspectionItemId());
                        if (aMoldRemodelingInspectionResultVo.getRemodelingDetailId() != null && !"".equals(aMoldRemodelingInspectionResultVo.getRemodelingDetailId())) {
                            id = aMoldRemodelingInspectionResultVo.getRemodelingDetailId();
                        }
                        resultPK.setRemodelingDetailId(id);
                        resultPK.setSeq(aMoldRemodelingInspectionResultVo.getSeq());
                        aRemodelingResult.setTblMoldRemodelingInspectionResultPK(resultPK);

                        aRemodelingResult.setCreateDate(new Date());
                        aRemodelingResult.setCreateUserUuid(user.getUserUuid());
                        entityManager.persist(aRemodelingResult);
                    }
                }

                //設備ImageFile 追加
                List<TblMoldRemodelingDetailImageFileVo> moldRemodelingDetailImageFileVos = moldRemodelingDetailVo.getMoldRemodelingDetailImageFileVos();
                if (null != moldRemodelingDetailImageFileVos && !moldRemodelingDetailImageFileVos.isEmpty()) {
                    Query delImageFileQuery = entityManager.createNamedQuery("TblMoldRemodelingDetailImageFile.deleteByRemodelingDetailId");
                    delImageFileQuery.setParameter("remodelingDetailId", id);
                    delImageFileQuery.executeUpdate();

                    for (TblMoldRemodelingDetailImageFileVo aImageFileVo : moldRemodelingDetailImageFileVos) {
                        TblMoldRemodelingDetailImageFile aImageFile = new TblMoldRemodelingDetailImageFile();
                        aImageFile.setUpdateDate(new Date());
                        aImageFile.setUpdateUserUuid(user.getUserUuid());
                        aImageFile.setCreateDate(new Date());
                        aImageFile.setCreateDateUuid(user.getUserUuid());

                        TblMoldRemodelingDetailImageFilePK aPK = new TblMoldRemodelingDetailImageFilePK();
                        aPK.setRemodelingDetailId(id);
                        aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                        aImageFile.setTblMoldRemodelingDetailImageFilePK(aPK);

                        if (null != aImageFileVo.getFileExtension() && !aImageFileVo.getFileExtension().isEmpty()) {
                            aImageFile.setFileExtension(aImageFileVo.getFileExtension());
                        } else {
                            aImageFile.setFileExtension(null);
                        }
                        if (null != aImageFileVo.getFileType() && !aImageFileVo.getFileType().isEmpty()) {
                            aImageFile.setFileType(Integer.parseInt(aImageFileVo.getFileType()));
                        } else {
                            aImageFile.setFileType(null);
                        }
                        if (null != aImageFileVo.getFileUuid() && !aImageFileVo.getFileUuid().isEmpty()) {
                            aImageFile.setFileUuid(aImageFileVo.getFileUuid());
                        }
                        if (null != aImageFileVo.getRemarks() && !aImageFileVo.getRemarks().isEmpty()) {
                            aImageFile.setRemarks(aImageFileVo.getRemarks());
                        } else {
                            aImageFile.setRemarks(null);
                        }
                        if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                            aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                        } else {
                            aImageFile.setTakenDate(null);
                        }
                        if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                            aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                        } else {
                            aImageFile.setTakenDateStz(null);
                        }
                        if (null != aImageFileVo.getThumbnailFileUuid() && !aImageFileVo.getThumbnailFileUuid().isEmpty()) {
                            aImageFile.setThumbnailFileUuid(aImageFileVo.getThumbnailFileUuid());
                        } else {
                            aImageFile.setThumbnailFileUuid(null);
                        }

                        entityManager.persist(aImageFile);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_record_added"));
        return response;
    }

    /**
     * TT0009 金型改造開始入力 登録 金型改造内容を登録し、データベースを更新する。
     *
     * @param tblMoldMaintenanceRemodelingVo
     * @param user
     * @return
     */
    @Transactional
    public BasicResponse postMoldRemodelingDetailes2(TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo, LoginUser user) {
        BasicResponse response = new BasicResponse();
        String maintenanceId = tblMoldMaintenanceRemodelingVo.getId();

//        if (!getRemodelingExistCheck(maintenanceId)) {
//            response.setError(true);
//            response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_data_deleted"));
//            return response;
//        }
//        // 外部データチェック
//        String moldId = tblMoldMaintenanceRemodelingVo.getMoldId();
//        response = FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, user);
//        if (response.isError()) {
//            return response;
//        }


        //金型改造メンテナンス 終了日時、改造分類、報告事項を設定
        StringBuilder sql = new StringBuilder(" UPDATE TblMoldMaintenanceRemodeling t SET ");
        sql.append(" t.remodelingType = :remodelingType, ");
        if (tblMoldMaintenanceRemodelingVo.getReport() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getReport())) {
            sql.append(" t.report = :report, ");
        }
        if (tblMoldMaintenanceRemodelingVo.getDirectionId() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getDirectionId())) {
            sql.append(" t.directionId = :directionId, ");
        }
        if (tblMoldMaintenanceRemodelingVo.getDirectionCode() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getDirectionCode())) {
            sql.append(" t.directionCode = :directionCode, ");
        }
//        sql.append(" t.endDatetime = :endDatetime, ");
//        sql.append(" t.endDatetimeStz = :endDatetimeStz, ");
        sql.append(" t.updateDate = :updateDate, ");
        sql.append(" t.updateUserUuid = :updateUserUuid ");
        sql.append(" WHERE t.id = :id ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("remodelingType", tblMoldMaintenanceRemodelingVo.getRemodelingType());
        if (tblMoldMaintenanceRemodelingVo.getReport() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getReport())) {
            query.setParameter("report", tblMoldMaintenanceRemodelingVo.getReport());
        }
        if (tblMoldMaintenanceRemodelingVo.getDirectionId() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getDirectionId())) {
            query.setParameter("directionId", tblMoldMaintenanceRemodelingVo.getDirectionId());
        }
        if (tblMoldMaintenanceRemodelingVo.getDirectionCode() != null && !"".equals(tblMoldMaintenanceRemodelingVo.getDirectionCode())) {
            query.setParameter("directionCode", tblMoldMaintenanceRemodelingVo.getDirectionCode());
        }

//        query.setParameter("endDatetimeStz", new Date());
//        try {
//            query.setParameter("endDatetime", sdf.parse(DateFormat.dateTimeFormat(new Date(), user.getJavaZoneId())));
//        } catch (ParseException ex) {
//            Logger.getLogger(TblMoldRemodelingDetailService.class.getName()).log(Level.SEVERE, null, ex);
//        }
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", user.getUserUuid());
        query.setParameter("id", maintenanceId);

        query.executeUpdate();

        //金型改造詳細 追加・更新 再開の場合は更新 
        if (tblMoldMaintenanceRemodelingVo.getMoldRemodelingDetailVo() != null && tblMoldMaintenanceRemodelingVo.getMoldRemodelingDetailVo().size() > 0) {
            for (TblMoldRemodelingDetailVo moldRemodelingDetailVo : tblMoldMaintenanceRemodelingVo.getMoldRemodelingDetailVo()) {

                int Seq = moldRemodelingDetailVo.getSeq();
                String remodelReasonCategory1 = "";
                String remodelReasonCategory2 = "";
                String remodelReasonCategory3 = "";
                String remodelReason = "";
                String remodelDirectionCategory1 = "";
                String remodelDirectionCategory2 = "";
                String remodelDirectionCategory3 = "";
                String remodelDirection = "";
                String taskCategory1 = "";
                String taskCategory2 = "";
                String taskCategory3 = "";
                String task = "";

                String id = "";
                if (checkTblMoldRemodelingDetail(maintenanceId, Seq)) {
                    //更新
                    StringBuilder detailSql = new StringBuilder("UPDATE TblMoldRemodelingDetail t SET ");
                    if (moldRemodelingDetailVo.getRemodelReasonCategory1() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory1())) {
                        remodelReasonCategory1 = moldRemodelingDetailVo.getRemodelReasonCategory1();
                        detailSql.append(" t.remodelReasonCategory1 = :remodelReasonCategory1, ");
                        detailSql.append(" t.remodelReasonCategory1Text = :remodelReasonCategory1Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelReasonCategory2() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory2())) {
                        remodelReasonCategory2 = moldRemodelingDetailVo.getRemodelReasonCategory2();
                        detailSql.append(" t.remodelReasonCategory2 = :remodelReasonCategory2, ");
                        detailSql.append(" t.remodelReasonCategory2Text = :remodelReasonCategory2Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelReasonCategory3() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory3())) {
                        remodelReasonCategory3 = moldRemodelingDetailVo.getRemodelReasonCategory3();
                        detailSql.append(" t.remodelReasonCategory3 = :remodelReasonCategory3, ");
                        detailSql.append(" t.remodelReasonCategory3Text = :remodelReasonCategory3Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelReason() != null && !"".equals(moldRemodelingDetailVo.getRemodelReason())) {
                        remodelReason = moldRemodelingDetailVo.getRemodelReason();
                        detailSql.append(" t.remodelReason = :remodelReason, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory1() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory1())) {
                        remodelDirectionCategory1 = moldRemodelingDetailVo.getRemodelDirectionCategory1();
                        detailSql.append(" t.remodelDirectionCategory1 = :remodelDirectionCategory1, ");
                        detailSql.append(" t.remodelDirectionCategory1Text = :remodelDirectionCategory1Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory2() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory2())) {
                        remodelDirectionCategory2 = moldRemodelingDetailVo.getRemodelDirectionCategory2();
                        detailSql.append(" t.remodelDirectionCategory2 = :remodelDirectionCategory2, ");
                        detailSql.append(" t.remodelDirectionCategory2Text = :remodelDirectionCategory2Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory3() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory3())) {
                        remodelDirectionCategory3 = moldRemodelingDetailVo.getRemodelDirectionCategory3();
                        detailSql.append(" t.remodelDirectionCategory3 = :remodelDirectionCategory3, ");
                        detailSql.append(" t.remodelDirectionCategory3Text = :remodelDirectionCategory3Text, ");
                    }
                    if (moldRemodelingDetailVo.getRemodelDirection() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirection())) {
                        remodelDirection = moldRemodelingDetailVo.getRemodelDirection();
                        detailSql.append(" t.remodelDirection = :remodelDirection, ");
                    }
                    if (moldRemodelingDetailVo.getTaskCategory1() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory1())) {
                        taskCategory1 = moldRemodelingDetailVo.getTaskCategory1();
                        detailSql.append(" t.taskCategory1 = :taskCategory1, ");
                        detailSql.append(" t.taskCategory1Text = :taskCategory1Text, ");
                    }
                    if (moldRemodelingDetailVo.getTaskCategory2() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory2())) {
                        taskCategory2 = moldRemodelingDetailVo.getTaskCategory2();
                        detailSql.append(" t.taskCategory2 = :taskCategory2, ");
                        detailSql.append(" t.taskCategory2Text = :taskCategory2Text, ");
                    }
                    if (moldRemodelingDetailVo.getTaskCategory3() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory3())) {
                        taskCategory3 = moldRemodelingDetailVo.getTaskCategory3();
                        detailSql.append(" t.taskCategory3 = :taskCategory3, ");
                        detailSql.append(" t.taskCategory3Text = :taskCategory3Text, ");
                    }
                    if (moldRemodelingDetailVo.getTask() != null && !"".equals(moldRemodelingDetailVo.getTask())) {
                        task = moldRemodelingDetailVo.getTask();
                        detailSql.append(" t.task = :task, ");
                    }
                    detailSql.append(" t.updateDate = :updateDate,t.updateUserUuid = :updateUserUuid ");
                    detailSql.append(" WHERE t.tblMoldRemodelingDetailPK.maintenanceId = :maintenanceId ");
                    detailSql.append(" And t.tblMoldRemodelingDetailPK.seq = :seq ");

                    Query detailQuery = entityManager.createQuery(detailSql.toString());

                    if (remodelReasonCategory1 != null && !"".equals(remodelReasonCategory1)) {
                        detailQuery.setParameter("remodelReasonCategory1", Integer.parseInt(remodelReasonCategory1));
                        detailQuery.setParameter("remodelReasonCategory1Text", moldRemodelingDetailVo.getRemodelReasonCategory1Text() == null ? "" : moldRemodelingDetailVo.getRemodelReasonCategory1Text());
                    }
                    if (remodelReasonCategory2 != null && !"".equals(remodelReasonCategory2)) {
                        detailQuery.setParameter("remodelReasonCategory2", Integer.parseInt(remodelReasonCategory2));
                        detailQuery.setParameter("remodelReasonCategory2Text", moldRemodelingDetailVo.getRemodelReasonCategory2Text() == null ? "" : moldRemodelingDetailVo.getRemodelReasonCategory2Text());
                    }
                    if (remodelReasonCategory3 != null && !"".equals(remodelReasonCategory3)) {
                        detailQuery.setParameter("remodelReasonCategory3", Integer.parseInt(remodelReasonCategory3));
                        detailQuery.setParameter("remodelReasonCategory3Text", moldRemodelingDetailVo.getRemodelReasonCategory3Text() == null ? "" : moldRemodelingDetailVo.getRemodelReasonCategory3Text());
                    }
                    if (remodelReason != null && !"".equals(remodelReason)) {
                        detailQuery.setParameter("remodelReason", remodelReason);
                    }
                    if (remodelDirectionCategory1 != null && !"".equals(remodelDirectionCategory1)) {
                        detailQuery.setParameter("remodelDirectionCategory1", Integer.parseInt(remodelDirectionCategory1));
                        detailQuery.setParameter("remodelDirectionCategory1Text", moldRemodelingDetailVo.getRemodelDirectionCategory1Text() == null ? "" : moldRemodelingDetailVo.getRemodelDirectionCategory1Text());
                    }
                    if (remodelDirectionCategory2 != null && !"".equals(remodelDirectionCategory2)) {
                        detailQuery.setParameter("remodelDirectionCategory2", Integer.parseInt(remodelDirectionCategory2));
                        detailQuery.setParameter("remodelDirectionCategory2Text", moldRemodelingDetailVo.getRemodelDirectionCategory2Text() == null ? "" : moldRemodelingDetailVo.getRemodelDirectionCategory2Text());
                    }
                    if (remodelDirectionCategory3 != null && !"".equals(remodelDirectionCategory3)) {
                        detailQuery.setParameter("remodelDirectionCategory3", Integer.parseInt(remodelDirectionCategory3));
                        detailQuery.setParameter("remodelDirectionCategory3Text", moldRemodelingDetailVo.getRemodelDirectionCategory3Text() == null ? "" : moldRemodelingDetailVo.getRemodelDirectionCategory3Text());
                    }
                    if (remodelDirection != null && !"".equals(remodelDirection)) {
                        detailQuery.setParameter("remodelDirection", remodelDirection);
                    }
                    if (taskCategory1 != null && !"".equals(taskCategory1)) {
                        detailQuery.setParameter("taskCategory1", Integer.parseInt(taskCategory1));
                        detailQuery.setParameter("taskCategory1Text", moldRemodelingDetailVo.getTaskCategory1Text() == null ? "" : moldRemodelingDetailVo.getTaskCategory1Text());
                    }
                    if (taskCategory2 != null && !"".equals(taskCategory2)) {
                        detailQuery.setParameter("taskCategory2", Integer.parseInt(taskCategory2));
                        detailQuery.setParameter("taskCategory2Text", moldRemodelingDetailVo.getTaskCategory2Text() == null ? "" : moldRemodelingDetailVo.getTaskCategory2Text());
                    }
                    if (taskCategory3 != null && !"".equals(taskCategory3)) {
                        detailQuery.setParameter("taskCategory3", Integer.parseInt(taskCategory3));
                        detailQuery.setParameter("taskCategory3Text", moldRemodelingDetailVo.getTaskCategory3Text() == null ? "" : moldRemodelingDetailVo.getTaskCategory3Text());
                    }
                    if (task != null && !"".equals(task)) {
                        detailQuery.setParameter("task", task);
                    }
                    detailQuery.setParameter("updateDate", new Date());
                    detailQuery.setParameter("updateUserUuid", user.getUserUuid());
                    detailQuery.setParameter("maintenanceId", maintenanceId);
                    detailQuery.setParameter("seq", moldRemodelingDetailVo.getSeq());

                    detailQuery.executeUpdate();
                    id = moldRemodelingDetailVo.getId();
                } else {
                    //追加 
                    TblMoldRemodelingDetail tblMoldRemodelingDetail = new TblMoldRemodelingDetail();
                    TblMoldRemodelingDetailPK tblMoldRemodelingDetailPK = new TblMoldRemodelingDetailPK();
                    tblMoldRemodelingDetail.setId(IDGenerator.generate());
                    tblMoldRemodelingDetailPK.setMaintenanceId(moldRemodelingDetailVo.getMaintenanceId());
                    tblMoldRemodelingDetailPK.setSeq(moldRemodelingDetailVo.getSeq());
                    tblMoldRemodelingDetail.setTblMoldRemodelingDetailPK(tblMoldRemodelingDetailPK);
                    if (moldRemodelingDetailVo.getRemodelReasonCategory1() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory1())) {
                        tblMoldRemodelingDetail.setRemodelReasonCategory1(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory1()));
                        tblMoldRemodelingDetail.setRemodelReasonCategory1Text(moldRemodelingDetailVo.getRemodelReasonCategory1Text());
                    }
                    if (moldRemodelingDetailVo.getRemodelReasonCategory2() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory2())) {
                        tblMoldRemodelingDetail.setRemodelReasonCategory2(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory2()));
                        tblMoldRemodelingDetail.setRemodelReasonCategory2Text(moldRemodelingDetailVo.getRemodelReasonCategory2Text());
                    }
                    if (moldRemodelingDetailVo.getRemodelReasonCategory3() != null && !"".equals(moldRemodelingDetailVo.getRemodelReasonCategory3())) {
                        tblMoldRemodelingDetail.setRemodelReasonCategory3(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory3()));
                        tblMoldRemodelingDetail.setRemodelReasonCategory3Text(moldRemodelingDetailVo.getRemodelReasonCategory3Text());
                    }
                    tblMoldRemodelingDetail.setRemodelReason(moldRemodelingDetailVo.getRemodelReason());
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory1() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory1())) {
                        tblMoldRemodelingDetail.setRemodelDirectionCategory1(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory1()));
                        tblMoldRemodelingDetail.setRemodelDirectionCategory1Text(moldRemodelingDetailVo.getRemodelDirectionCategory1Text());
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory2() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory2())) {
                        tblMoldRemodelingDetail.setRemodelDirectionCategory2(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory2()));
                        tblMoldRemodelingDetail.setRemodelDirectionCategory2Text(moldRemodelingDetailVo.getRemodelDirectionCategory2Text());
                    }
                    if (moldRemodelingDetailVo.getRemodelDirectionCategory3() != null && !"".equals(moldRemodelingDetailVo.getRemodelDirectionCategory3())) {
                        tblMoldRemodelingDetail.setRemodelDirectionCategory3(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory3()));
                        tblMoldRemodelingDetail.setRemodelDirectionCategory3Text(moldRemodelingDetailVo.getRemodelDirectionCategory3Text());
                    }

                    tblMoldRemodelingDetail.setRemodelDirection(moldRemodelingDetailVo.getRemodelDirection());
                    if (moldRemodelingDetailVo.getTaskCategory1() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory1())) {
                        tblMoldRemodelingDetail.setTaskCategory1(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory1()));
                        tblMoldRemodelingDetail.setTaskCategory1Text(moldRemodelingDetailVo.getTaskCategory1Text());
                    }
                    if (moldRemodelingDetailVo.getTaskCategory2() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory2())) {
                        tblMoldRemodelingDetail.setTaskCategory2(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory2()));
                        tblMoldRemodelingDetail.setTaskCategory2Text(moldRemodelingDetailVo.getTaskCategory2Text());
                    }
                    if (moldRemodelingDetailVo.getTaskCategory3() != null && !"".equals(moldRemodelingDetailVo.getTaskCategory3())) {
                        tblMoldRemodelingDetail.setTaskCategory3(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory3()));
                        tblMoldRemodelingDetail.setTaskCategory3Text(moldRemodelingDetailVo.getTaskCategory3Text());
                    }

                    tblMoldRemodelingDetail.setTask(moldRemodelingDetailVo.getTask());
                    tblMoldRemodelingDetail.setCreateDate(new Date());
                    tblMoldRemodelingDetail.setCreateUserUuid(user.getUserUuid());
                    tblMoldRemodelingDetail.setUpdateDate(new Date());
                    tblMoldRemodelingDetail.setUpdateUserUuid(user.getUserUuid());
                    entityManager.persist(tblMoldRemodelingDetail);
                    id = tblMoldRemodelingDetail.getId();
                }

                //設備点検結果 追加
                List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVo = moldRemodelingDetailVo.getMoldRemodelingInspectionResultVos();
                if (moldRemodelingInspectionResultVo != null && !moldRemodelingInspectionResultVo.isEmpty()) {
                    if (null != moldRemodelingDetailVo.getId()) {
                        Query delInspectionResultQuery = entityManager.createQuery("DELETE FROM TblMoldRemodelingInspectionResult t WHERE t.tblMoldRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId");
                        delInspectionResultQuery.setParameter("remodelingDetailId", moldRemodelingDetailVo.getId());
                        delInspectionResultQuery.executeUpdate();
                    }

                    for (int j = 0; j < moldRemodelingInspectionResultVo.size(); j++) {
                        TblMoldRemodelingInspectionResultVo aMoldRemodelingInspectionResultVo = moldRemodelingInspectionResultVo.get(j);
                        TblMoldRemodelingInspectionResult aRemodelingResult = new TblMoldRemodelingInspectionResult();
                        aRemodelingResult.setUpdateDate(new Date());
                        aRemodelingResult.setUpdateUserUuid(user.getUserUuid());
                        aRemodelingResult.setInspectionResult(aMoldRemodelingInspectionResultVo.getInspectionResult());
                        aRemodelingResult.setInspectionResultText(aMoldRemodelingInspectionResultVo.getInspectionResultText());

                        aRemodelingResult.setId(IDGenerator.generate());
                        TblMoldRemodelingInspectionResultPK resultPK = new TblMoldRemodelingInspectionResultPK();
                        resultPK.setInspectionItemId(aMoldRemodelingInspectionResultVo.getInspectionItemId());
                        if (aMoldRemodelingInspectionResultVo.getRemodelingDetailId() != null && !"".equals(aMoldRemodelingInspectionResultVo.getRemodelingDetailId())) {
                            id = aMoldRemodelingInspectionResultVo.getRemodelingDetailId();
                        }
                        resultPK.setRemodelingDetailId(id);
                        resultPK.setSeq(aMoldRemodelingInspectionResultVo.getSeq());
                        aRemodelingResult.setTblMoldRemodelingInspectionResultPK(resultPK);

                        aRemodelingResult.setCreateDate(new Date());
                        aRemodelingResult.setCreateUserUuid(user.getUserUuid());
                        entityManager.persist(aRemodelingResult);
                    }
                }

                //設備ImageFile 追加
                List<TblMoldRemodelingDetailImageFileVo> moldRemodelingDetailImageFileVos = moldRemodelingDetailVo.getMoldRemodelingDetailImageFileVos();
                if (null != moldRemodelingDetailImageFileVos && !moldRemodelingDetailImageFileVos.isEmpty()) {
                    Query delImageFileQuery = entityManager.createNamedQuery("TblMoldRemodelingDetailImageFile.deleteByRemodelingDetailId");
                    delImageFileQuery.setParameter("remodelingDetailId", id);
                    delImageFileQuery.executeUpdate();

                    for (TblMoldRemodelingDetailImageFileVo aImageFileVo : moldRemodelingDetailImageFileVos) {
                        TblMoldRemodelingDetailImageFile aImageFile = new TblMoldRemodelingDetailImageFile();
                        aImageFile.setUpdateDate(new Date());
                        aImageFile.setUpdateUserUuid(user.getUserUuid());
                        aImageFile.setCreateDate(new Date());
                        aImageFile.setCreateDateUuid(user.getUserUuid());

                        TblMoldRemodelingDetailImageFilePK aPK = new TblMoldRemodelingDetailImageFilePK();
                        aPK.setRemodelingDetailId(id);
                        aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                        aImageFile.setTblMoldRemodelingDetailImageFilePK(aPK);

                        if (null != aImageFileVo.getFileExtension() && !aImageFileVo.getFileExtension().isEmpty()) {
                            aImageFile.setFileExtension(aImageFileVo.getFileExtension());
                        } else {
                            aImageFile.setFileExtension(null);
                        }
                        if (null != aImageFileVo.getFileType() && !aImageFileVo.getFileType().isEmpty()) {
                            aImageFile.setFileType(Integer.parseInt(aImageFileVo.getFileType()));
                        } else {
                            aImageFile.setFileType(null);
                        }
                        if (null != aImageFileVo.getFileUuid() && !aImageFileVo.getFileUuid().isEmpty()) {
                            aImageFile.setFileUuid(aImageFileVo.getFileUuid());
                        }
                        if (null != aImageFileVo.getRemarks() && !aImageFileVo.getRemarks().isEmpty()) {
                            aImageFile.setRemarks(aImageFileVo.getRemarks());
                        } else {
                            aImageFile.setRemarks(null);
                        }
                        if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                            aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                        } else {
                            aImageFile.setTakenDate(null);
                        }
                        if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                            aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                        } else {
                            aImageFile.setTakenDateStz(null);
                        }
                        if (null != aImageFileVo.getThumbnailFileUuid() && !aImageFileVo.getThumbnailFileUuid().isEmpty()) {
                            aImageFile.setThumbnailFileUuid(aImageFileVo.getThumbnailFileUuid());
                        } else {
                            aImageFile.setThumbnailFileUuid(null);
                        }

                        entityManager.persist(aImageFile);
                    }
                }
            }
        }
//        response.setError(false);
//        response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_record_added"));
        return response;
    }

    
    
    /**
     * 金型改造詳細データがチェックして
     *
     * @param maintenanceId
     * @param seq
     * @return
     */
    public boolean checkTblMoldRemodelingDetail(String maintenanceId, int seq) {
        Query query = entityManager.createNamedQuery("TblMoldRemodelingDetail.findByMaintenanceIdAndSeq");
        query.setParameter("maintenanceId", maintenanceId);
        query.setParameter("seq", seq);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    /**
     * 金型改造詳細_追加 金型仕様履歴_追加・更新_改造日を開始日とする新たな履歴の作成、それまで最新だった仕様の終了日を改造日の前日で更新。
     * 金型仕様_追加 金型改造メンテナンス_更新_金型仕様履歴IDの更新 金型部品関係_追加
     *
     * @param tblMoldRemodelingDetailVo
     * @param user
     */
    @Transactional
    public BasicResponse postMoldRemodelingResultRegistration(TblMoldRemodelingDetailVo tblMoldRemodelingDetailVo, LoginUser user) {
        BasicResponse response = new BasicResponse();
        
        String moldUuid = tblMoldRemodelingDetailVo.getMoldUuid();
        String specName = tblMoldRemodelingDetailVo.getSpecName();
        String maintenanceId = tblMoldRemodelingDetailVo.getMaintenanceId();

        Calendar calendar = Calendar.getInstance();

        if (specName != null && !"".equals(specName.trim())) {
            //金型仕様履歴_更新

            String specHisId = tblMoldRemodelingDetailVo.getSpecHisId();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date endDate = calendar.getTime();

            Query queryMoldSpecHis = entityManager.createNamedQuery("MstMoldSpecHistory.findById");
            queryMoldSpecHis.setParameter("id", specHisId);
            MstMoldSpecHistory mstMoldSpecHistory = (MstMoldSpecHistory) queryMoldSpecHis.getSingleResult();
            mstMoldSpecHistory.setEndDate(endDate);
            mstMoldSpecHistory.setUpdateDate(new Date());
            mstMoldSpecHistory.setUpdateUserUuid(user.getUserUuid());
            entityManager.merge(mstMoldSpecHistory);

            //金型仕様履歴_追加
//            calendar.add(Calendar.DAY_OF_MONTH, +1);
//            Date newEndDate = calendar.getTime();
            MstMoldSpecHistory newhistory = new MstMoldSpecHistory();
            String newSpecHisId = IDGenerator.generate();
            newhistory.setId(newSpecHisId);
            newhistory.setMoldUuid(moldUuid);
            newhistory.setStartDate(new Date());
            newhistory.setEndDate(CommonConstants.SYS_MAX_DATE);
            newhistory.setMoldSpecName(specName);
            newhistory.setCreateDate(new Date());
            newhistory.setCreateUserUuid(user.getUserUuid());
            newhistory.setUpdateDate(new Date());
            newhistory.setUpdateUserUuid(user.getUserUuid());

            entityManager.persist(newhistory);

            //金型属性データ
            List<MstMoldAttributes> attributeses = tblMoldRemodelingDetailVo.getAttributes();

            if (attributeses != null && attributeses.size() > 0) {
                for (MstMoldAttributes attribute : attributeses) {
                    //金型仕様_追加
                    MstMoldSpecPK mstMoldSpecPK = new MstMoldSpecPK();
                    MstMoldSpec spec = new MstMoldSpec();
                    spec.setId(IDGenerator.generate());
                    mstMoldSpecPK.setAttrId(attribute.getId());
                    mstMoldSpecPK.setMoldSpecHstId(newSpecHisId);
                    spec.setMstMoldSpecPK(mstMoldSpecPK);
                    spec.setAttrValue(attribute.getAttrValue() == null ? "" : attribute.getAttrValue());
                    spec.setCreateDate(new Date());
                    spec.setCreateUserUuid(user.getUserUuid());
                    spec.setUpdateDate(new Date());
                    spec.setUpdateUserUuid(user.getUserUuid());
                    entityManager.persist(spec);
                }
            }

            //金型改造メンテナンス_更新				
            Query queryMaintenanceRemodeling = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findById");
            queryMaintenanceRemodeling.setParameter("id", maintenanceId);
            TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling = (TblMoldMaintenanceRemodeling) queryMaintenanceRemodeling.getSingleResult();
            tblMoldMaintenanceRemodeling.setMoldSpecHstIdStr(newSpecHisId);
            tblMoldMaintenanceRemodeling.setUpdateDate(new Date());
            tblMoldMaintenanceRemodeling.setUpdateUserUuid(user.getUserUuid());
            entityManager.merge(tblMoldMaintenanceRemodeling);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_not_null"));
            return response;
        }

        Query queryDeleteRelation = entityManager.createNamedQuery("MstMoldComponentRelation.deleteBymoldUuid");
                queryDeleteRelation.setParameter("moldUuid", moldUuid);
                queryDeleteRelation.executeUpdate();
        
        //金型部品関係_追加
        List<MstMoldComponentRelationVo> mstMoldComponentRelationVos = tblMoldRemodelingDetailVo.getMstMoldComponentRelationVos();
        if (mstMoldComponentRelationVos != null && mstMoldComponentRelationVos.size() > 0) {
            // 重複を除く
            for (int i = 0; i < mstMoldComponentRelationVos.size(); i++) {
                for (int j = mstMoldComponentRelationVos.size() - 1; j > i; j--) {
                    MstMoldComponentRelationVo vo1 = mstMoldComponentRelationVos.get(j);
                    MstMoldComponentRelationVo vo2 = mstMoldComponentRelationVos.get(i);
                    if (vo1.getComponentId().equals(vo2.getComponentId())) {
                        mstMoldComponentRelationVos.remove(j);
                    }
                }
            }
            for (MstMoldComponentRelationVo relationVo : mstMoldComponentRelationVos) {
                String componentId = relationVo.getComponentId();
                if (componentId != null && !"".equals(componentId) && moldUuid != null && !"".equals(moldUuid) ) {
                    MstMoldComponentRelation relation = new MstMoldComponentRelation();
                    MstMoldComponentRelationPK mstMoldComponentRelationPK = new MstMoldComponentRelationPK();
                    mstMoldComponentRelationPK.setComponentId(componentId);
                    mstMoldComponentRelationPK.setMoldUuid(moldUuid);
                    relation.setMstMoldComponentRelationPK(mstMoldComponentRelationPK);
                    try {
                        if (relationVo.getCountPerShot() != null && !"".equals(relationVo.getCountPerShot())) {
                            relation.setCountPerShot(Integer.parseInt(relationVo.getCountPerShot()));
                        } else {
                            relation.setCountPerShot(0);
                        }
                    } catch (NumberFormatException e) {
                        relation.setCountPerShot(0);
                    }
                    relation.setCreateDate(new Date());
                    relation.setCreateUserUuid(user.getUserUuid());
                    relation.setUpdateDate(new Date());
                    relation.setUpdateUserUuid(user.getUserUuid());
                    entityManager.persist(relation);
                }
            }
        }
        return response;
    }
   
    
    /**
     * バッチで金型改造詳細データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMoldRemodelingDetailVo getExtMoldRemodelingDetailsByBatch(String latestExecutedDate, String moldUuid) {
        TblMoldRemodelingDetailVo resList = new TblMoldRemodelingDetailVo();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblMoldRemodelingDetail t join MstApiUser u on u.companyId = t.tblMoldMaintenanceRemodeling.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.tblMoldMaintenanceRemodeling.mstMold.uuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMoldRemodelingDetail> tmpList = query.getResultList();
        for (TblMoldRemodelingDetail tblMoldRemodelingDetail : tmpList) {
            tblMoldRemodelingDetail.setTblMoldMaintenanceRemodeling(null);
        }
        resList.setTblMoldRemodelingDetails(tmpList);
        return resList;
    }

    /**
     * バッチで金型改造詳細データを更新
     *
     * @param moldRemodelingDetails
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldRemodelingDetailsByBatch(List<TblMoldRemodelingDetail> moldRemodelingDetails) {
        BasicResponse response = new BasicResponse();

        if (moldRemodelingDetails != null && !moldRemodelingDetails.isEmpty()) {
            for (TblMoldRemodelingDetail aMoldRemodelingDetail : moldRemodelingDetails) {
                TblMoldMaintenanceRemodeling moldMaintenanceRemodeling = entityManager.find(TblMoldMaintenanceRemodeling.class, aMoldRemodelingDetail.getTblMoldRemodelingDetailPK().getMaintenanceId());
                if (null != moldMaintenanceRemodeling) {
                    
                    List<TblMoldRemodelingDetail> oldMoldRemodelingDetails = entityManager.createQuery("SELECT t FROM TblMoldRemodelingDetail t WHERE t.tblMoldRemodelingDetailPK.maintenanceId = :maintenanceId and t.tblMoldRemodelingDetailPK.seq = :seq ")
                            .setParameter("maintenanceId", aMoldRemodelingDetail.getTblMoldRemodelingDetailPK().getMaintenanceId())
                            .setParameter("seq", aMoldRemodelingDetail.getTblMoldRemodelingDetailPK().getSeq())
                            .setMaxResults(1)
                            .getResultList();

                    TblMoldRemodelingDetail newDetail;
                    if (null != oldMoldRemodelingDetails && !oldMoldRemodelingDetails.isEmpty()) {
                        newDetail = oldMoldRemodelingDetails.get(0);
                    } else {
                        newDetail = new TblMoldRemodelingDetail();
                        TblMoldRemodelingDetailPK pk = new TblMoldRemodelingDetailPK();
                        pk.setMaintenanceId(aMoldRemodelingDetail.getTblMoldRemodelingDetailPK().getMaintenanceId());
                        pk.setSeq(aMoldRemodelingDetail.getTblMoldRemodelingDetailPK().getSeq());
                        newDetail.setTblMoldRemodelingDetailPK(pk);
                        
                    }
                    newDetail.setId(aMoldRemodelingDetail.getId());
                    newDetail.setRemodelReasonCategory1(aMoldRemodelingDetail.getRemodelReasonCategory1());
                    newDetail.setRemodelReasonCategory1Text(aMoldRemodelingDetail.getRemodelReasonCategory1Text());
                    newDetail.setRemodelReasonCategory2(aMoldRemodelingDetail.getRemodelReasonCategory2());
                    newDetail.setRemodelReasonCategory2Text(aMoldRemodelingDetail.getRemodelReasonCategory2Text());
                    newDetail.setRemodelReasonCategory3(aMoldRemodelingDetail.getRemodelReasonCategory3());
                    newDetail.setRemodelReasonCategory3Text(aMoldRemodelingDetail.getRemodelReasonCategory3Text());
                    newDetail.setRemodelReason(aMoldRemodelingDetail.getRemodelReason());
                    newDetail.setRemodelDirectionCategory1(aMoldRemodelingDetail.getRemodelDirectionCategory1());
                    newDetail.setRemodelDirectionCategory1Text(aMoldRemodelingDetail.getRemodelDirectionCategory1Text());
                    newDetail.setRemodelDirectionCategory2(aMoldRemodelingDetail.getRemodelDirectionCategory2());
                    newDetail.setRemodelDirectionCategory2Text(aMoldRemodelingDetail.getRemodelDirectionCategory2Text());
                    newDetail.setRemodelDirectionCategory3(aMoldRemodelingDetail.getRemodelDirectionCategory3());
                    newDetail.setRemodelDirectionCategory3Text(aMoldRemodelingDetail.getRemodelDirectionCategory3Text());
                    newDetail.setRemodelDirection(aMoldRemodelingDetail.getRemodelDirection());
                    newDetail.setTaskCategory1(aMoldRemodelingDetail.getTaskCategory1());
                    newDetail.setTaskCategory1Text(aMoldRemodelingDetail.getTaskCategory1Text());
                    newDetail.setTaskCategory2(aMoldRemodelingDetail.getTaskCategory2());
                    newDetail.setTaskCategory2Text(aMoldRemodelingDetail.getTaskCategory2Text());
                    newDetail.setTaskCategory3(aMoldRemodelingDetail.getTaskCategory3());
                    newDetail.setTaskCategory3Text(aMoldRemodelingDetail.getTaskCategory3Text());
                    newDetail.setTask(aMoldRemodelingDetail.getTask());

                    newDetail.setCreateDate(aMoldRemodelingDetail.getCreateDate());
                    newDetail.setCreateUserUuid(aMoldRemodelingDetail.getCreateUserUuid());
                    newDetail.setUpdateDate(new Date());
                    newDetail.setUpdateUserUuid(aMoldRemodelingDetail.getUpdateUserUuid());

                    if (null != oldMoldRemodelingDetails && !oldMoldRemodelingDetails.isEmpty()) {
                        entityManager.merge(newDetail);
                    } else {
                        entityManager.persist(newDetail);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /**
     * バッチで金型改造詳細ImageFileデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMoldRemodelingDetailVo getExtMoldRemodelingDetailImageFilesByBatch(String latestExecutedDate, String moldUuid) {
        TblMoldRemodelingDetailVo resList = new TblMoldRemodelingDetailVo();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblMoldRemodelingDetailImageFile t join MstApiUser u on u.companyId = t.tblMoldRemodelingDetail.tblMoldMaintenanceRemodeling.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.tblMoldRemodelingDetail.tblMoldMaintenanceRemodeling.mstMold.uuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMoldRemodelingDetailImageFile> tmpList = query.getResultList();
        List<TblMoldRemodelingDetailImageFileVo> resVos = new ArrayList<>();
        FileUtil fu = new FileUtil();
        for (TblMoldRemodelingDetailImageFile aDetailImageFile : tmpList) {
            TblMoldRemodelingDetailImageFileVo aVo = new TblMoldRemodelingDetailImageFileVo();
            if (null != aDetailImageFile.getCreateDate()) {
                aVo.setCreateDate(aDetailImageFile.getCreateDate());
                aVo.setCreateDateStr(fu.getDateTimeFormatForStr(aDetailImageFile.getCreateDate()));
            }
            aVo.setCreateDateUuid(aDetailImageFile.getCreateDateUuid());
            if (null != aDetailImageFile.getFileExtension()) {
                aVo.setFileExtension(aDetailImageFile.getFileExtension());
            }
            if (null != aDetailImageFile.getFileType()) {
                aVo.setFileType("" + aDetailImageFile.getFileType());
            }
            aVo.setFileUuid(aDetailImageFile.getFileUuid());
            aVo.setRemodelingDetailId(aDetailImageFile.getTblMoldRemodelingDetailImageFilePK().getRemodelingDetailId());
            aVo.setSeq("" + aDetailImageFile.getTblMoldRemodelingDetailImageFilePK().getSeq());
            if (null != aDetailImageFile.getRemarks()) {
                aVo.setRemarks(aDetailImageFile.getRemarks());
            }
            if (null != aDetailImageFile.getTakenDate()) {
                aVo.setTakenDate(aDetailImageFile.getTakenDate());
                aVo.setTakenDateStr(fu.getDateTimeFormatForStr(aDetailImageFile.getTakenDate()));
            }
            if (null != aDetailImageFile.getTakenDateStz()) {
                aVo.setTakenDateStz(aDetailImageFile.getTakenDateStz());
                aVo.setTakenDateStzStr(fu.getDateTimeFormatForStr(aDetailImageFile.getTakenDateStz()));
            }

            if (null != aDetailImageFile.getThumbnailFileUuid()) {
                aVo.setThumbnailFileUuid(aDetailImageFile.getThumbnailFileUuid());
            }
            resVos.add(aVo);
        }
        resList.setMoldRemodelingDetailImageFileVos(resVos);
        return resList;
    }

    /**
     * バッチで金型改造詳細ImageFileデータを更新
     *
     * @param imageFileVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldRemodelingDetailImageFilesByBatch(List<TblMoldRemodelingDetailImageFileVo> imageFileVos) {
        BasicResponse response = new BasicResponse();

        if (imageFileVos != null && !imageFileVos.isEmpty()) {
            FileUtil fu = new FileUtil();
            for (TblMoldRemodelingDetailImageFileVo aVo : imageFileVos) {
                List<TblMoldRemodelingDetail> moldRemodelingDetails = entityManager.createQuery("from TblMoldRemodelingDetail t where t.id = :remodelingDetailId ")
                        .setParameter("remodelingDetailId", aVo.getRemodelingDetailId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != moldRemodelingDetails && !moldRemodelingDetails.isEmpty()) {
                    
                    List<TblMoldRemodelingDetailImageFile> oldImageFiles = entityManager.createQuery("SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.tblMoldRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId and t.tblMoldRemodelingDetailImageFilePK.seq = :seq ")
                            .setParameter("remodelingDetailId", aVo.getRemodelingDetailId())
                            .setParameter("seq", Integer.parseInt(aVo.getSeq()))
                            .setMaxResults(1)
                            .getResultList();

                    TblMoldRemodelingDetailImageFile newImageFile;
                    if (null != oldImageFiles && !oldImageFiles.isEmpty()) {
                        newImageFile = oldImageFiles.get(0);
                    } else {
                        newImageFile = new TblMoldRemodelingDetailImageFile();
                        TblMoldRemodelingDetailImageFilePK pk = new TblMoldRemodelingDetailImageFilePK();
                        pk.setRemodelingDetailId(aVo.getRemodelingDetailId());
                        pk.setSeq(Integer.parseInt(aVo.getSeq()));
                        newImageFile.setTblMoldRemodelingDetailImageFilePK(pk);

                    }
                    newImageFile.setFileUuid(aVo.getFileUuid());
                    if (null != aVo.getFileExtension() && !aVo.getFileExtension().isEmpty()) {
                        newImageFile.setFileExtension(aVo.getFileExtension());
                    } else {
                        newImageFile.setFileExtension(null);
                    }
                    if (null != aVo.getFileType()) {
                        newImageFile.setFileType(Integer.parseInt(aVo.getFileType()));
                    } else {
                        newImageFile.setFileType(null);
                    }
                    newImageFile.setRemarks(aVo.getRemarks());
                    if (null != aVo.getTakenDateStr()) {
                        newImageFile.setTakenDate(fu.getDateTimeParseForDate(aVo.getTakenDateStr()));
                    } else {
                        newImageFile.setTakenDate(null);
                    }
                    if (null != aVo.getTakenDateStzStr()) {
                        newImageFile.setTakenDateStz(fu.getDateTimeParseForDate(aVo.getTakenDateStzStr()));
                    } else {
                        newImageFile.setTakenDateStz(null);
                    }
                    if (null != aVo.getThumbnailFileUuid() && !aVo.getThumbnailFileUuid().isEmpty()) {
                        newImageFile.setThumbnailFileUuid(aVo.getThumbnailFileUuid());
                    } else {
                        newImageFile.setThumbnailFileUuid(null);
                    }

                    newImageFile.setCreateDate(fu.getDateTimeParseForDate(aVo.getCreateDateStr()));
                    newImageFile.setCreateDateUuid(aVo.getCreateDateUuid());
                    newImageFile.setUpdateDate(new Date());
                    newImageFile.setUpdateUserUuid(aVo.getUpdateUserUuid());

                    if (null != oldImageFiles && !oldImageFiles.isEmpty()) {
                        entityManager.merge(newImageFile);
                    } else {
                        entityManager.persist(newImageFile);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
