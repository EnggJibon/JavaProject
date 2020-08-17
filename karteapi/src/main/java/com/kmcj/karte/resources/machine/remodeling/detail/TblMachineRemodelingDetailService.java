package com.kmcj.karte.resources.machine.remodeling.detail;

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
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeVo;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodeling;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingService;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingVo;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResult;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultPK;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultVo;
import com.kmcj.karte.resources.machine.spec.MstMachineSpec;
import com.kmcj.karte.resources.machine.spec.MstMachineSpecPK;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistory;
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
public class TblMachineRemodelingDetailService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private TblMachineMaintenanceRemodelingService tblMachineMaintenanceRemodelingService;

    @Inject
    private MstChoiceService mstChoiceService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private ExtMstChoiceService extMstChoiceService;
    
    @Inject
    private CnfSystemService cnfSystemService;
    
    /**
     * 設備改造終了入力 再開 終了している
     * 設備改造のステータスを改造中に戻す。 終了していない設備改造の場合は、処理を行えない。
     * @param machineId
     * @param mainteOrRemodel
     * @param user
     * @return 
     */
    @Transactional
    public BasicResponse putRemodelingResumption(String machineId, int mainteOrRemodel, LoginUser user) {
        BasicResponse basicResponse = new BasicResponse();
        Query queryMachine = entityManager.createNamedQuery("MstMachine.findByMachineId");
        queryMachine.setParameter("machineId", machineId);
        try {
            MstMachine machine = (MstMachine) queryMachine.getSingleResult();
            machine.setMainteStatus(CommonConstants.MAINTE_STATUS_REMODELING);
            machine.setUpdateDate(new Date());
            machine.setUpdateUserUuid(user.getUserUuid());
            entityManager.merge(machine);

        } catch (NoResultException ex) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_data_deleted"));
            return basicResponse;
        }
        
        TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo = (TblMachineMaintenanceRemodelingVo) tblMachineMaintenanceRemodelingService.getMachinemainteOrRemodelDetail(machineId, mainteOrRemodel, user);
        String id = tblMachineMaintenanceRemodelingVo.getId();

        StringBuilder sql = new StringBuilder(" UPDATE TblMachineMaintenanceRemodeling t SET ");
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
        
        basicResponse.setError(false);
        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
        basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_record_updated"));
        return basicResponse;
    }
    
    
    /**
     * 設備改造終了入力 開始取消 選択された設備改造の開始をなかったことにするため、データベースからレコードを削除する。
     *
     * @param machineId
     * @param user
     * @return 
     */
    @Transactional
    public BasicResponse putRemodelingStartCancel(String machineId, LoginUser user) {
        
        BasicResponse basicResponse = new BasicResponse();
        try {
            Query queryMachine = entityManager.createNamedQuery("MstMachine.findByMachineId");
            queryMachine.setParameter("machineId", machineId);

            MstMachine machine = (MstMachine) queryMachine.getSingleResult();
            machine.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL);
            machine.setUpdateDate(new Date());
            machine.setUpdateUserUuid(user.getUserUuid());
            entityManager.merge(machine);

        } catch (NoResultException ex) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_data_deleted"));
            return basicResponse;
        }

        TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo = (TblMachineMaintenanceRemodelingVo) tblMachineMaintenanceRemodelingService.getMachinemainteOrRemodelDetail(machineId, CommonConstants.MAINTE_STATUS_REMODELING, user);
        String id = tblMachineMaintenanceRemodelingVo.getId();

        StringBuilder sql = new StringBuilder(" DELETE FROM TblMachineMaintenanceRemodeling t ");
        sql.append(" WHERE t.id = :id ");

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("id", id);
        query.executeUpdate();

        basicResponse.setError(false);
        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
        basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_record_updated"));
        return basicResponse;
    }
    
    
    /**
     * 設備改造終了入力 登録 設備改造内容を登録し、設備のメンテ状態を戻すために、データベースを更新する。
     *
     * @param tblMachineMaintenanceRemodelingVo
     * @param user
     * @return 
     */
    @Transactional
    public BasicResponse postMachineRemodelingDetailes(TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo, LoginUser user) {

        BasicResponse response = new BasicResponse();
        String maintenanceId = tblMachineMaintenanceRemodelingVo.getId();
        if(!getRemodelingExistCheck(maintenanceId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        
        String machineId = tblMachineMaintenanceRemodelingVo.getMachineId();
        if (machineId == null || "".equals(machineId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_not_null"));
            return response;
        }

        // 外部データチェック
        response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", user);
        if (response.isError()) {
            return response;
        }
        
        response = new BasicResponse();
        if (0 == tblMachineMaintenanceRemodelingVo.getTemporarilySaved()) { // メンテナンスの一次保存機能 2018/09/13 -S
            try {
                //設備マスタのメンテ状態を改造中から通常に戻す。
                Query queryMachine = entityManager.createNamedQuery("MstMachine.findByMachineId");
                queryMachine.setParameter("machineId", machineId);

                MstMachine machine = (MstMachine) queryMachine.getSingleResult();
                machine.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL);

                // 4.2 対応　BY LiuYoudong S
                CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
                machine.setLastMainteDate(DateFormat.strToDate(DateFormat.getBusinessDate(DateFormat.dateToStr(new Date(), DateFormat.DATETIME_FORMAT), cnfSystem))); // 最終メンテナンス日
                if (tblMachineMaintenanceRemodelingVo.isResetAfterMainteTotalProducingTimeHourFlag()) {
                    machine.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO); // メンテナンス後生産時間
                }
                if (tblMachineMaintenanceRemodelingVo.isResetAfterMainteTotalShotCountFlag()) {
                    machine.setAfterMainteTotalShotCount(0); // メンテナンス後ショット数
                }
                // 4.2 対応　BY LiuYoudong E

                machine.setUpdateDate(new Date());
                machine.setUpdateUserUuid(user.getUserUuid());
                entityManager.merge(machine);

            } catch (NoResultException ex) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_data_deleted"));
                return response;
            }
        } // メンテナンスの一次保存機能 2018/09/13 -E

        //設備改造メンテナンス 終了日時、改造分類、報告事項を設定
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);

        StringBuilder sql = new StringBuilder(" UPDATE TblMachineMaintenanceRemodeling t SET ");
        sql.append(" t.remodelingType = :remodelingType, ");
        if (tblMachineMaintenanceRemodelingVo.getReport() != null && !"".equals(tblMachineMaintenanceRemodelingVo.getReport())) {
            sql.append(" t.report = :report, ");
        }
        if(tblMachineMaintenanceRemodelingVo.getTblDirectionId()!= null && !"".equals(tblMachineMaintenanceRemodelingVo.getTblDirectionId())){
            sql.append(" t.tblDirectionId = :directionId, ");
        }
        if(tblMachineMaintenanceRemodelingVo.getTblDirectionCode()!= null && !"".equals(tblMachineMaintenanceRemodelingVo.getTblDirectionCode())){
            sql.append(" t.tblDirectionCode = :directionCode, ");
        }
        // メンテナンスの一次保存機能 2018/09/13 -S
        if (0 == tblMachineMaintenanceRemodelingVo.getTemporarilySaved()) {
            sql.append(" t.endDatetime = :endDatetime, ");
            sql.append(" t.endDatetimeStz = :endDatetimeStz, ");
        }
        // メンテナンスの一次保存機能 2018/09/13 -E
        sql.append(" t.updateDate = :updateDate, ");
        sql.append(" t.updateUserUuid = :updateUserUuid ");
        sql.append(" WHERE t.id = :id ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("remodelingType", tblMachineMaintenanceRemodelingVo.getRemodelingType());
        if (tblMachineMaintenanceRemodelingVo.getReport() != null && !"".equals(tblMachineMaintenanceRemodelingVo.getReport())) {
            query.setParameter("report", tblMachineMaintenanceRemodelingVo.getReport());
        }
        if(tblMachineMaintenanceRemodelingVo.getTblDirectionId()!= null && !"".equals(tblMachineMaintenanceRemodelingVo.getTblDirectionId())){
            query.setParameter("directionId", tblMachineMaintenanceRemodelingVo.getTblDirectionId());
        }
        if(tblMachineMaintenanceRemodelingVo.getTblDirectionCode()!= null && !"".equals(tblMachineMaintenanceRemodelingVo.getTblDirectionCode())){
            query.setParameter("directionCode", tblMachineMaintenanceRemodelingVo.getTblDirectionCode());
        }

        // メンテナンスの一次保存機能 2018/09/13 -S
        if (0 == tblMachineMaintenanceRemodelingVo.getTemporarilySaved()) {
            query.setParameter("endDatetimeStz", new Date());
            try {
                query.setParameter("endDatetime", sdf.parse(DateFormat.dateTimeFormat(new Date(), user.getJavaZoneId())));
            } catch (ParseException ex) {
                Logger.getLogger(TblMachineRemodelingDetailService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // メンテナンスの一次保存機能 2018/09/13 -E
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", user.getUserUuid());
        query.setParameter("id", maintenanceId);

        query.executeUpdate();

        //設備改造詳細 追加・更新 再開の場合は更新 
        if (tblMachineMaintenanceRemodelingVo.getMachineRemodelingDetailVo()!= null && tblMachineMaintenanceRemodelingVo.getMachineRemodelingDetailVo().size() > 0) {
            String id = "";
            for (TblMachineRemodelingDetailVo machineRemodelingDetailVo : tblMachineMaintenanceRemodelingVo.getMachineRemodelingDetailVo()) {

                int Seq = machineRemodelingDetailVo.getSeq();
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

                if (checkTblMachineRemodelingDetail(maintenanceId, Seq)) {
                    //更新
                    StringBuilder detailSql = new StringBuilder("UPDATE TblMachineRemodelingDetail t SET ");
                    if (machineRemodelingDetailVo.getRemodelReasonCategory1() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory1())) {
                        remodelReasonCategory1 = machineRemodelingDetailVo.getRemodelReasonCategory1();
                        detailSql.append(" t.remodelReasonCategory1 = :remodelReasonCategory1, ");
                        detailSql.append(" t.remodelReasonCategory1Text = :remodelReasonCategory1Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelReasonCategory2() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory2())) {
                        remodelReasonCategory2 = machineRemodelingDetailVo.getRemodelReasonCategory2();
                        detailSql.append(" t.remodelReasonCategory2 = :remodelReasonCategory2, ");
                        detailSql.append(" t.remodelReasonCategory2Text = :remodelReasonCategory2Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelReasonCategory3() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory3())) {
                        remodelReasonCategory3 = machineRemodelingDetailVo.getRemodelReasonCategory3();
                        detailSql.append(" t.remodelReasonCategory3 = :remodelReasonCategory3, ");
                        detailSql.append(" t.remodelReasonCategory3Text = :remodelReasonCategory3Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelReason() != null && !"".equals(machineRemodelingDetailVo.getRemodelReason())) {
                        remodelReason = machineRemodelingDetailVo.getRemodelReason();
                        detailSql.append(" t.remodelReason = :remodelReason, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory1() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory1())) {
                        remodelDirectionCategory1 = machineRemodelingDetailVo.getRemodelDirectionCategory1();
                        detailSql.append(" t.remodelDirectionCategory1 = :remodelDirectionCategory1, ");
                        detailSql.append(" t.remodelDirectionCategory1Text = :remodelDirectionCategory1Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory2() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory2())) {
                        remodelDirectionCategory2 = machineRemodelingDetailVo.getRemodelDirectionCategory2();
                        detailSql.append(" t.remodelDirectionCategory2 = :remodelDirectionCategory2, ");
                        detailSql.append(" t.remodelDirectionCategory2Text = :remodelDirectionCategory2Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory3() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory3())) {
                        remodelDirectionCategory3 = machineRemodelingDetailVo.getRemodelDirectionCategory3();
                        detailSql.append(" t.remodelDirectionCategory3 = :remodelDirectionCategory3, ");
                        detailSql.append(" t.remodelDirectionCategory3Text = :remodelDirectionCategory3Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelDirection() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirection())) {
                        remodelDirection = machineRemodelingDetailVo.getRemodelDirection();
                        detailSql.append(" t.remodelDirection = :remodelDirection, ");
                    }
                    if (machineRemodelingDetailVo.getTaskCategory1() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory1())) {
                        taskCategory1 = machineRemodelingDetailVo.getTaskCategory1();
                        detailSql.append(" t.taskCategory1 = :taskCategory1, ");
                        detailSql.append(" t.taskCategory1Text = :taskCategory1Text, ");
                    }
                    if (machineRemodelingDetailVo.getTaskCategory2() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory2())) {
                        taskCategory2 = machineRemodelingDetailVo.getTaskCategory2();
                        detailSql.append(" t.taskCategory2 = :taskCategory2, ");
                        detailSql.append(" t.taskCategory2Text = :taskCategory2Text, ");
                    }
                    if (machineRemodelingDetailVo.getTaskCategory3() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory3())) {
                        taskCategory3 = machineRemodelingDetailVo.getTaskCategory3();
                        detailSql.append(" t.taskCategory3 = :taskCategory3, ");
                        detailSql.append(" t.taskCategory3Text = :taskCategory3Text, ");
                    }
                    if (machineRemodelingDetailVo.getTask() != null && !"".equals(machineRemodelingDetailVo.getTask())) {
                        task = machineRemodelingDetailVo.getTask();
                        detailSql.append(" t.task = :task, ");
                    }
                    detailSql.append(" t.updateDate = :updateDate,t.updateUserUuid = :updateUserUuid ");
                    detailSql.append(" WHERE t.tblMachineRemodelingDetailPK.maintenanceId = :maintenanceId ");
                    detailSql.append(" And t.tblMachineRemodelingDetailPK.seq = :seq ");

                    Query detailQuery = entityManager.createQuery(detailSql.toString());

                    if (remodelReasonCategory1 != null && !"".equals(remodelReasonCategory1)) {
                        detailQuery.setParameter("remodelReasonCategory1", Integer.parseInt(remodelReasonCategory1));
                        detailQuery.setParameter("remodelReasonCategory1Text", machineRemodelingDetailVo.getRemodelReasonCategory1Text() == null ? "" : machineRemodelingDetailVo.getRemodelReasonCategory1Text());
                    }
                    if (remodelReasonCategory2 != null && !"".equals(remodelReasonCategory2)) {
                        detailQuery.setParameter("remodelReasonCategory2", Integer.parseInt(remodelReasonCategory2));
                        detailQuery.setParameter("remodelReasonCategory2Text", machineRemodelingDetailVo.getRemodelReasonCategory2Text() == null ? "" : machineRemodelingDetailVo.getRemodelReasonCategory2Text());
                    }
                    if (remodelReasonCategory3 != null && !"".equals(remodelReasonCategory3)) {
                        detailQuery.setParameter("remodelReasonCategory3", Integer.parseInt(remodelReasonCategory3));
                        detailQuery.setParameter("remodelReasonCategory3Text", machineRemodelingDetailVo.getRemodelReasonCategory3Text() == null ? "" : machineRemodelingDetailVo.getRemodelReasonCategory3Text());
                    }
                    if (remodelReason != null && !"".equals(remodelReason)) {
                        detailQuery.setParameter("remodelReason", remodelReason);
                    }
                    if (remodelDirectionCategory1 != null && !"".equals(remodelDirectionCategory1)) {
                        detailQuery.setParameter("remodelDirectionCategory1", Integer.parseInt(remodelDirectionCategory1));
                        detailQuery.setParameter("remodelDirectionCategory1Text", machineRemodelingDetailVo.getRemodelDirectionCategory1Text() == null ? "" : machineRemodelingDetailVo.getRemodelDirectionCategory1Text());
                    }
                    if (remodelDirectionCategory2 != null && !"".equals(remodelDirectionCategory2)) {
                        detailQuery.setParameter("remodelDirectionCategory2", Integer.parseInt(remodelDirectionCategory2));
                        detailQuery.setParameter("remodelDirectionCategory2Text", machineRemodelingDetailVo.getRemodelDirectionCategory2Text() == null ? "" : machineRemodelingDetailVo.getRemodelDirectionCategory2Text());
                    }
                    if (remodelDirectionCategory3 != null && !"".equals(remodelDirectionCategory3)) {
                        detailQuery.setParameter("remodelDirectionCategory3", Integer.parseInt(remodelDirectionCategory3));
                        detailQuery.setParameter("remodelDirectionCategory3Text", machineRemodelingDetailVo.getRemodelDirectionCategory3Text() == null ? "" : machineRemodelingDetailVo.getRemodelDirectionCategory3Text());
                    }
                    if (remodelDirection != null && !"".equals(remodelDirection)) {
                        detailQuery.setParameter("remodelDirection", remodelDirection);
                    }
                    if (taskCategory1 != null && !"".equals(taskCategory1)) {
                        detailQuery.setParameter("taskCategory1", Integer.parseInt(taskCategory1));
                        detailQuery.setParameter("taskCategory1Text", machineRemodelingDetailVo.getTaskCategory1Text() == null ? "" : machineRemodelingDetailVo.getTaskCategory1Text());
                    }
                    if (taskCategory2 != null && !"".equals(taskCategory2)) {
                        detailQuery.setParameter("taskCategory2", Integer.parseInt(taskCategory2));
                        detailQuery.setParameter("taskCategory2Text", machineRemodelingDetailVo.getTaskCategory2Text() == null ? "" : machineRemodelingDetailVo.getTaskCategory2Text());
                    }
                    if (taskCategory3 != null && !"".equals(taskCategory3)) {
                        detailQuery.setParameter("taskCategory3", Integer.parseInt(taskCategory3));
                        detailQuery.setParameter("taskCategory3Text", machineRemodelingDetailVo.getTaskCategory3Text() == null ? "" : machineRemodelingDetailVo.getTaskCategory3Text());
                    }
                    if (task != null && !"".equals(task)) {
                        detailQuery.setParameter("task", task);
                    }
                    detailQuery.setParameter("updateDate", new Date());
                    detailQuery.setParameter("updateUserUuid", user.getUserUuid());
                    detailQuery.setParameter("maintenanceId", maintenanceId);
                    detailQuery.setParameter("seq", machineRemodelingDetailVo.getSeq());

                    detailQuery.executeUpdate();
                    id = machineRemodelingDetailVo.getId();
                } else {
                    //追加 
                    TblMachineRemodelingDetail tblMachineRemodelingDetail = new TblMachineRemodelingDetail();
                    TblMachineRemodelingDetailPK tblMachineRemodelingDetailPK = new TblMachineRemodelingDetailPK();
                    tblMachineRemodelingDetail.setId(IDGenerator.generate());
                    tblMachineRemodelingDetailPK.setMaintenanceId(maintenanceId);
                    tblMachineRemodelingDetailPK.setSeq(machineRemodelingDetailVo.getSeq());
                    tblMachineRemodelingDetail.setTblMachineRemodelingDetailPK(tblMachineRemodelingDetailPK);
                    if (machineRemodelingDetailVo.getRemodelReasonCategory1() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory1())) {
                        tblMachineRemodelingDetail.setRemodelReasonCategory1(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory1()));
                        tblMachineRemodelingDetail.setRemodelReasonCategory1Text(machineRemodelingDetailVo.getRemodelReasonCategory1Text());
                    }
                    if (machineRemodelingDetailVo.getRemodelReasonCategory2() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory2())) {
                        tblMachineRemodelingDetail.setRemodelReasonCategory2(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory2()));
                        tblMachineRemodelingDetail.setRemodelReasonCategory2Text(machineRemodelingDetailVo.getRemodelReasonCategory2Text());
                    }
                    if (machineRemodelingDetailVo.getRemodelReasonCategory3() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory3())) {
                        tblMachineRemodelingDetail.setRemodelReasonCategory3(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory3()));
                        tblMachineRemodelingDetail.setRemodelReasonCategory3Text(machineRemodelingDetailVo.getRemodelReasonCategory3Text());
                    }
                    tblMachineRemodelingDetail.setRemodelReason(machineRemodelingDetailVo.getRemodelReason());
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory1() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory1())) {
                        tblMachineRemodelingDetail.setRemodelDirectionCategory1(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory1()));
                        tblMachineRemodelingDetail.setRemodelDirectionCategory1Text(machineRemodelingDetailVo.getRemodelDirectionCategory1Text());
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory2() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory2())) {
                        tblMachineRemodelingDetail.setRemodelDirectionCategory2(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory2()));
                        tblMachineRemodelingDetail.setRemodelDirectionCategory2Text(machineRemodelingDetailVo.getRemodelDirectionCategory2Text());
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory3() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory3())) {
                        tblMachineRemodelingDetail.setRemodelDirectionCategory3(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory3()));
                        tblMachineRemodelingDetail.setRemodelDirectionCategory3Text(machineRemodelingDetailVo.getRemodelDirectionCategory3Text());
                    }

                    tblMachineRemodelingDetail.setRemodelDirection(machineRemodelingDetailVo.getRemodelDirection());
                    if (machineRemodelingDetailVo.getTaskCategory1() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory1())) {
                        tblMachineRemodelingDetail.setTaskCategory1(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory1()));
                        tblMachineRemodelingDetail.setTaskCategory1Text(machineRemodelingDetailVo.getTaskCategory1Text());
                    }
                    if (machineRemodelingDetailVo.getTaskCategory2() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory2())) {
                        tblMachineRemodelingDetail.setTaskCategory2(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory2()));
                        tblMachineRemodelingDetail.setTaskCategory2Text(machineRemodelingDetailVo.getTaskCategory2Text());
                    }
                    if (machineRemodelingDetailVo.getTaskCategory3() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory3())) {
                        tblMachineRemodelingDetail.setTaskCategory3(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory3()));
                        tblMachineRemodelingDetail.setTaskCategory3Text(machineRemodelingDetailVo.getTaskCategory3Text());
                    }

                    tblMachineRemodelingDetail.setTask(machineRemodelingDetailVo.getTask());
                    tblMachineRemodelingDetail.setCreateDate(new Date());
                    tblMachineRemodelingDetail.setCreateUserUuid(user.getUserUuid());
                    tblMachineRemodelingDetail.setUpdateDate(new Date());
                    tblMachineRemodelingDetail.setUpdateUserUuid(user.getUserUuid());
                    entityManager.persist(tblMachineRemodelingDetail);
                    id = tblMachineRemodelingDetail.getId();
                }
                //設備点検結果 追加
                List<TblMachineRemodelingInspectionResultVo> machineRemodelingInspectionResultVo = machineRemodelingDetailVo.getMachineRemodelingInspectionResultVo();
                if (machineRemodelingInspectionResultVo != null && !machineRemodelingInspectionResultVo.isEmpty()) {
                    if (null != machineRemodelingDetailVo.getId()) {
                        Query delInspectionResultQuery = entityManager.createQuery("DELETE FROM TblMachineRemodelingInspectionResult t WHERE t.tblMachineRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId");
                        delInspectionResultQuery.setParameter("remodelingDetailId", machineRemodelingDetailVo.getId());
                        delInspectionResultQuery.executeUpdate();
                    }
                    for (TblMachineRemodelingInspectionResultVo aMachineRemodelingInspectionResultVo : machineRemodelingInspectionResultVo) {
                        TblMachineRemodelingInspectionResult aRemodelingResult = new TblMachineRemodelingInspectionResult();
                        aRemodelingResult.setUpdateDate(new Date());
                        aRemodelingResult.setUpdateUserUuid(user.getUserUuid());
                        aRemodelingResult.setInspectionResult(aMachineRemodelingInspectionResultVo.getInspectionResult());
                        aRemodelingResult.setInspectionResultText(aMachineRemodelingInspectionResultVo.getInspectionResultText());

                        aRemodelingResult.setId(IDGenerator.generate());
                        TblMachineRemodelingInspectionResultPK resultPK = new TblMachineRemodelingInspectionResultPK();
                        resultPK.setInspectionItemId(aMachineRemodelingInspectionResultVo.getMstMachineInspectionItemId());
                        if (aMachineRemodelingInspectionResultVo.getRemodelingDetailId() != null && !"".equals(aMachineRemodelingInspectionResultVo.getRemodelingDetailId())) {
                            id = aMachineRemodelingInspectionResultVo.getRemodelingDetailId();
                        }
                        resultPK.setRemodelingDetailId(id);
                        resultPK.setSeq(aMachineRemodelingInspectionResultVo.getSeq());
                        aRemodelingResult.setTblMachineRemodelingInspectionResultPK(resultPK);

                        aRemodelingResult.setCreateDate(new Date());
                        aRemodelingResult.setCreateUserUuid(user.getUserUuid());
                        entityManager.persist(aRemodelingResult);
                    }
                }
                
                //設備ImageFile 追加
                List<TblMachineRemodelingDetailImageFileVo> machineRemodelingDetailImageFileVos = machineRemodelingDetailVo.getMachineRemodelingDetailImageFileVos();
                
                if (null != machineRemodelingDetailImageFileVos && !machineRemodelingDetailImageFileVos.isEmpty()) {

                    Query delImageFileQuery = entityManager.createNamedQuery("TblMachineRemodelingDetailImageFile.deleteByRemodelingDetailId");
                    delImageFileQuery.setParameter("remodelingDetailId", id);
                    delImageFileQuery.executeUpdate();

                    for (TblMachineRemodelingDetailImageFileVo aImageFileVo : machineRemodelingDetailImageFileVos) {
                        TblMachineRemodelingDetailImageFile aImageFile = new TblMachineRemodelingDetailImageFile();
                        aImageFile.setUpdateDate(new Date());
                        aImageFile.setUpdateUserUuid(user.getUserUuid());
                        aImageFile.setCreateDate(new Date());
                        aImageFile.setCreateDateUuid(user.getUserUuid());

                        TblMachineRemodelingDetailImageFilePK aPK = new TblMachineRemodelingDetailImageFilePK();
                        aPK.setRemodelingDetailId(id);
                        aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                        aImageFile.setTblMachineRemodelingDetailImageFilePK(aPK);

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
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_record_added"));
        return response;
    }
    
    
    /**
     * 設備改造開始入力 登録 設備改造内容を登録し、データベースを更新する。
     *
     * @param tblMachineMaintenanceRemodelingVo
     * @param user
     * @return
     */
    @Transactional
    public BasicResponse postMachineRemodelingDetailes2(TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo, LoginUser user) {

        BasicResponse response = new BasicResponse();
        String maintenanceId = tblMachineMaintenanceRemodelingVo.getId();
//        if (!getRemodelingExistCheck(maintenanceId)) {
//            response.setError(true);
//            response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_data_deleted"));
//            return response;
//        }
//
//        String machineId = tblMachineMaintenanceRemodelingVo.getMachineId();
//        if (machineId == null || "".equals(machineId)) {
//            response.setError(true);
//            response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_not_null"));
//            return response;
//        }
//
//        // 外部データチェック
//        response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", user);
//        if (response.isError()) {
//            return response;
//        }
//
//        response = new BasicResponse();

        //設備改造メンテナンス 終了日時、改造分類、報告事項を設定
//        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);

        StringBuilder sql = new StringBuilder(" UPDATE TblMachineMaintenanceRemodeling t SET ");
        sql.append(" t.remodelingType = :remodelingType, ");
        if (tblMachineMaintenanceRemodelingVo.getReport() != null && !"".equals(tblMachineMaintenanceRemodelingVo.getReport())) {
            sql.append(" t.report = :report, ");
        }
        if (tblMachineMaintenanceRemodelingVo.getTblDirectionId() != null && !"".equals(tblMachineMaintenanceRemodelingVo.getTblDirectionId())) {
            sql.append(" t.tblDirectionId = :directionId, ");
        }
        if (tblMachineMaintenanceRemodelingVo.getTblDirectionCode() != null && !"".equals(tblMachineMaintenanceRemodelingVo.getTblDirectionCode())) {
            sql.append(" t.tblDirectionCode = :directionCode, ");
        }

//        sql.append(" t.endDatetime = :endDatetime, ");
//        sql.append(" t.endDatetimeStz = :endDatetimeStz, ");
        sql.append(" t.updateDate = :updateDate, ");
        sql.append(" t.updateUserUuid = :updateUserUuid ");
        sql.append(" WHERE t.id = :id ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("remodelingType", tblMachineMaintenanceRemodelingVo.getRemodelingType());
        if (tblMachineMaintenanceRemodelingVo.getReport() != null && !"".equals(tblMachineMaintenanceRemodelingVo.getReport())) {
            query.setParameter("report", tblMachineMaintenanceRemodelingVo.getReport());
        }
        if (tblMachineMaintenanceRemodelingVo.getTblDirectionId() != null && !"".equals(tblMachineMaintenanceRemodelingVo.getTblDirectionId())) {
            query.setParameter("directionId", tblMachineMaintenanceRemodelingVo.getTblDirectionId());
        }
        if (tblMachineMaintenanceRemodelingVo.getTblDirectionCode() != null && !"".equals(tblMachineMaintenanceRemodelingVo.getTblDirectionCode())) {
            query.setParameter("directionCode", tblMachineMaintenanceRemodelingVo.getTblDirectionCode());
        }

//        query.setParameter("endDatetimeStz", new Date());
//        try {
//            query.setParameter("endDatetime", sdf.parse(DateFormat.dateTimeFormat(new Date(), user.getJavaZoneId())));            
//        } catch (ParseException ex) {
//            Logger.getLogger(TblMachineRemodelingDetailService.class.getName()).log(Level.SEVERE, null, ex);
//        }
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", user.getUserUuid());
        query.setParameter("id", maintenanceId);

        query.executeUpdate();

        //設備改造詳細 追加・更新 再開の場合は更新 
        if (tblMachineMaintenanceRemodelingVo.getMachineRemodelingDetailVo() != null && tblMachineMaintenanceRemodelingVo.getMachineRemodelingDetailVo().size() > 0) {
            String id = "";
            for (TblMachineRemodelingDetailVo machineRemodelingDetailVo : tblMachineMaintenanceRemodelingVo.getMachineRemodelingDetailVo()) {

                int Seq = machineRemodelingDetailVo.getSeq();
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

                if (checkTblMachineRemodelingDetail(maintenanceId, Seq)) {
                    //更新
                    StringBuilder detailSql = new StringBuilder("UPDATE TblMachineRemodelingDetail t SET ");
                    if (machineRemodelingDetailVo.getRemodelReasonCategory1() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory1())) {
                        remodelReasonCategory1 = machineRemodelingDetailVo.getRemodelReasonCategory1();
                        detailSql.append(" t.remodelReasonCategory1 = :remodelReasonCategory1, ");
                        detailSql.append(" t.remodelReasonCategory1Text = :remodelReasonCategory1Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelReasonCategory2() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory2())) {
                        remodelReasonCategory2 = machineRemodelingDetailVo.getRemodelReasonCategory2();
                        detailSql.append(" t.remodelReasonCategory2 = :remodelReasonCategory2, ");
                        detailSql.append(" t.remodelReasonCategory2Text = :remodelReasonCategory2Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelReasonCategory3() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory3())) {
                        remodelReasonCategory3 = machineRemodelingDetailVo.getRemodelReasonCategory3();
                        detailSql.append(" t.remodelReasonCategory3 = :remodelReasonCategory3, ");
                        detailSql.append(" t.remodelReasonCategory3Text = :remodelReasonCategory3Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelReason() != null && !"".equals(machineRemodelingDetailVo.getRemodelReason())) {
                        remodelReason = machineRemodelingDetailVo.getRemodelReason();
                        detailSql.append(" t.remodelReason = :remodelReason, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory1() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory1())) {
                        remodelDirectionCategory1 = machineRemodelingDetailVo.getRemodelDirectionCategory1();
                        detailSql.append(" t.remodelDirectionCategory1 = :remodelDirectionCategory1, ");
                        detailSql.append(" t.remodelDirectionCategory1Text = :remodelDirectionCategory1Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory2() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory2())) {
                        remodelDirectionCategory2 = machineRemodelingDetailVo.getRemodelDirectionCategory2();
                        detailSql.append(" t.remodelDirectionCategory2 = :remodelDirectionCategory2, ");
                        detailSql.append(" t.remodelDirectionCategory2Text = :remodelDirectionCategory2Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory3() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory3())) {
                        remodelDirectionCategory3 = machineRemodelingDetailVo.getRemodelDirectionCategory3();
                        detailSql.append(" t.remodelDirectionCategory3 = :remodelDirectionCategory3, ");
                        detailSql.append(" t.remodelDirectionCategory3Text = :remodelDirectionCategory3Text, ");
                    }
                    if (machineRemodelingDetailVo.getRemodelDirection() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirection())) {
                        remodelDirection = machineRemodelingDetailVo.getRemodelDirection();
                        detailSql.append(" t.remodelDirection = :remodelDirection, ");
                    }
                    if (machineRemodelingDetailVo.getTaskCategory1() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory1())) {
                        taskCategory1 = machineRemodelingDetailVo.getTaskCategory1();
                        detailSql.append(" t.taskCategory1 = :taskCategory1, ");
                        detailSql.append(" t.taskCategory1Text = :taskCategory1Text, ");
                    }
                    if (machineRemodelingDetailVo.getTaskCategory2() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory2())) {
                        taskCategory2 = machineRemodelingDetailVo.getTaskCategory2();
                        detailSql.append(" t.taskCategory2 = :taskCategory2, ");
                        detailSql.append(" t.taskCategory2Text = :taskCategory2Text, ");
                    }
                    if (machineRemodelingDetailVo.getTaskCategory3() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory3())) {
                        taskCategory3 = machineRemodelingDetailVo.getTaskCategory3();
                        detailSql.append(" t.taskCategory3 = :taskCategory3, ");
                        detailSql.append(" t.taskCategory3Text = :taskCategory3Text, ");
                    }
                    if (machineRemodelingDetailVo.getTask() != null && !"".equals(machineRemodelingDetailVo.getTask())) {
                        task = machineRemodelingDetailVo.getTask();
                        detailSql.append(" t.task = :task, ");
                    }
                    detailSql.append(" t.updateDate = :updateDate,t.updateUserUuid = :updateUserUuid ");
                    detailSql.append(" WHERE t.tblMachineRemodelingDetailPK.maintenanceId = :maintenanceId ");
                    detailSql.append(" And t.tblMachineRemodelingDetailPK.seq = :seq ");

                    Query detailQuery = entityManager.createQuery(detailSql.toString());

                    if (remodelReasonCategory1 != null && !"".equals(remodelReasonCategory1)) {
                        detailQuery.setParameter("remodelReasonCategory1", Integer.parseInt(remodelReasonCategory1));
                        detailQuery.setParameter("remodelReasonCategory1Text", machineRemodelingDetailVo.getRemodelReasonCategory1Text() == null ? "" : machineRemodelingDetailVo.getRemodelReasonCategory1Text());
                    }
                    if (remodelReasonCategory2 != null && !"".equals(remodelReasonCategory2)) {
                        detailQuery.setParameter("remodelReasonCategory2", Integer.parseInt(remodelReasonCategory2));
                        detailQuery.setParameter("remodelReasonCategory2Text", machineRemodelingDetailVo.getRemodelReasonCategory2Text() == null ? "" : machineRemodelingDetailVo.getRemodelReasonCategory2Text());
                    }
                    if (remodelReasonCategory3 != null && !"".equals(remodelReasonCategory3)) {
                        detailQuery.setParameter("remodelReasonCategory3", Integer.parseInt(remodelReasonCategory3));
                        detailQuery.setParameter("remodelReasonCategory3Text", machineRemodelingDetailVo.getRemodelReasonCategory3Text() == null ? "" : machineRemodelingDetailVo.getRemodelReasonCategory3Text());
                    }
                    if (remodelReason != null && !"".equals(remodelReason)) {
                        detailQuery.setParameter("remodelReason", remodelReason);
                    }
                    if (remodelDirectionCategory1 != null && !"".equals(remodelDirectionCategory1)) {
                        detailQuery.setParameter("remodelDirectionCategory1", Integer.parseInt(remodelDirectionCategory1));
                        detailQuery.setParameter("remodelDirectionCategory1Text", machineRemodelingDetailVo.getRemodelDirectionCategory1Text() == null ? "" : machineRemodelingDetailVo.getRemodelDirectionCategory1Text());
                    }
                    if (remodelDirectionCategory2 != null && !"".equals(remodelDirectionCategory2)) {
                        detailQuery.setParameter("remodelDirectionCategory2", Integer.parseInt(remodelDirectionCategory2));
                        detailQuery.setParameter("remodelDirectionCategory2Text", machineRemodelingDetailVo.getRemodelDirectionCategory2Text() == null ? "" : machineRemodelingDetailVo.getRemodelDirectionCategory2Text());
                    }
                    if (remodelDirectionCategory3 != null && !"".equals(remodelDirectionCategory3)) {
                        detailQuery.setParameter("remodelDirectionCategory3", Integer.parseInt(remodelDirectionCategory3));
                        detailQuery.setParameter("remodelDirectionCategory3Text", machineRemodelingDetailVo.getRemodelDirectionCategory3Text() == null ? "" : machineRemodelingDetailVo.getRemodelDirectionCategory3Text());
                    }
                    if (remodelDirection != null && !"".equals(remodelDirection)) {
                        detailQuery.setParameter("remodelDirection", remodelDirection);
                    }
                    if (taskCategory1 != null && !"".equals(taskCategory1)) {
                        detailQuery.setParameter("taskCategory1", Integer.parseInt(taskCategory1));
                        detailQuery.setParameter("taskCategory1Text", machineRemodelingDetailVo.getTaskCategory1Text() == null ? "" : machineRemodelingDetailVo.getTaskCategory1Text());
                    }
                    if (taskCategory2 != null && !"".equals(taskCategory2)) {
                        detailQuery.setParameter("taskCategory2", Integer.parseInt(taskCategory2));
                        detailQuery.setParameter("taskCategory2Text", machineRemodelingDetailVo.getTaskCategory2Text() == null ? "" : machineRemodelingDetailVo.getTaskCategory2Text());
                    }
                    if (taskCategory3 != null && !"".equals(taskCategory3)) {
                        detailQuery.setParameter("taskCategory3", Integer.parseInt(taskCategory3));
                        detailQuery.setParameter("taskCategory3Text", machineRemodelingDetailVo.getTaskCategory3Text() == null ? "" : machineRemodelingDetailVo.getTaskCategory3Text());
                    }
                    if (task != null && !"".equals(task)) {
                        detailQuery.setParameter("task", task);
                    }
                    detailQuery.setParameter("updateDate", new Date());
                    detailQuery.setParameter("updateUserUuid", user.getUserUuid());
                    detailQuery.setParameter("maintenanceId", maintenanceId);
                    detailQuery.setParameter("seq", machineRemodelingDetailVo.getSeq());

                    detailQuery.executeUpdate();
                    id = machineRemodelingDetailVo.getId();
                } else {
                    //追加 
                    TblMachineRemodelingDetail tblMachineRemodelingDetail = new TblMachineRemodelingDetail();
                    TblMachineRemodelingDetailPK tblMachineRemodelingDetailPK = new TblMachineRemodelingDetailPK();
                    tblMachineRemodelingDetail.setId(IDGenerator.generate());
                    tblMachineRemodelingDetailPK.setMaintenanceId(maintenanceId);
                    tblMachineRemodelingDetailPK.setSeq(machineRemodelingDetailVo.getSeq());
                    tblMachineRemodelingDetail.setTblMachineRemodelingDetailPK(tblMachineRemodelingDetailPK);
                    if (machineRemodelingDetailVo.getRemodelReasonCategory1() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory1())) {
                        tblMachineRemodelingDetail.setRemodelReasonCategory1(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory1()));
                        tblMachineRemodelingDetail.setRemodelReasonCategory1Text(machineRemodelingDetailVo.getRemodelReasonCategory1Text());
                    }
                    if (machineRemodelingDetailVo.getRemodelReasonCategory2() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory2())) {
                        tblMachineRemodelingDetail.setRemodelReasonCategory2(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory2()));
                        tblMachineRemodelingDetail.setRemodelReasonCategory2Text(machineRemodelingDetailVo.getRemodelReasonCategory2Text());
                    }
                    if (machineRemodelingDetailVo.getRemodelReasonCategory3() != null && !"".equals(machineRemodelingDetailVo.getRemodelReasonCategory3())) {
                        tblMachineRemodelingDetail.setRemodelReasonCategory3(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory3()));
                        tblMachineRemodelingDetail.setRemodelReasonCategory3Text(machineRemodelingDetailVo.getRemodelReasonCategory3Text());
                    }
                    tblMachineRemodelingDetail.setRemodelReason(machineRemodelingDetailVo.getRemodelReason());
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory1() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory1())) {
                        tblMachineRemodelingDetail.setRemodelDirectionCategory1(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory1()));
                        tblMachineRemodelingDetail.setRemodelDirectionCategory1Text(machineRemodelingDetailVo.getRemodelDirectionCategory1Text());
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory2() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory2())) {
                        tblMachineRemodelingDetail.setRemodelDirectionCategory2(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory2()));
                        tblMachineRemodelingDetail.setRemodelDirectionCategory2Text(machineRemodelingDetailVo.getRemodelDirectionCategory2Text());
                    }
                    if (machineRemodelingDetailVo.getRemodelDirectionCategory3() != null && !"".equals(machineRemodelingDetailVo.getRemodelDirectionCategory3())) {
                        tblMachineRemodelingDetail.setRemodelDirectionCategory3(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory3()));
                        tblMachineRemodelingDetail.setRemodelDirectionCategory3Text(machineRemodelingDetailVo.getRemodelDirectionCategory3Text());
                    }

                    tblMachineRemodelingDetail.setRemodelDirection(machineRemodelingDetailVo.getRemodelDirection());
                    if (machineRemodelingDetailVo.getTaskCategory1() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory1())) {
                        tblMachineRemodelingDetail.setTaskCategory1(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory1()));
                        tblMachineRemodelingDetail.setTaskCategory1Text(machineRemodelingDetailVo.getTaskCategory1Text());
                    }
                    if (machineRemodelingDetailVo.getTaskCategory2() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory2())) {
                        tblMachineRemodelingDetail.setTaskCategory2(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory2()));
                        tblMachineRemodelingDetail.setTaskCategory2Text(machineRemodelingDetailVo.getTaskCategory2Text());
                    }
                    if (machineRemodelingDetailVo.getTaskCategory3() != null && !"".equals(machineRemodelingDetailVo.getTaskCategory3())) {
                        tblMachineRemodelingDetail.setTaskCategory3(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory3()));
                        tblMachineRemodelingDetail.setTaskCategory3Text(machineRemodelingDetailVo.getTaskCategory3Text());
                    }

                    tblMachineRemodelingDetail.setTask(machineRemodelingDetailVo.getTask());
                    tblMachineRemodelingDetail.setCreateDate(new Date());
                    tblMachineRemodelingDetail.setCreateUserUuid(user.getUserUuid());
                    tblMachineRemodelingDetail.setUpdateDate(new Date());
                    tblMachineRemodelingDetail.setUpdateUserUuid(user.getUserUuid());
                    entityManager.persist(tblMachineRemodelingDetail);
                    id = tblMachineRemodelingDetail.getId();
                }
                //設備点検結果 追加
                List<TblMachineRemodelingInspectionResultVo> machineRemodelingInspectionResultVo = machineRemodelingDetailVo.getMachineRemodelingInspectionResultVo();
                if (machineRemodelingInspectionResultVo != null && !machineRemodelingInspectionResultVo.isEmpty()) {
                    if (null != machineRemodelingDetailVo.getId()) {
                        Query delInspectionResultQuery = entityManager.createQuery("DELETE FROM TblMachineRemodelingInspectionResult t WHERE t.tblMachineRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId");
                        delInspectionResultQuery.setParameter("remodelingDetailId", machineRemodelingDetailVo.getId());
                        delInspectionResultQuery.executeUpdate();
                    }
                    for (TblMachineRemodelingInspectionResultVo aMachineRemodelingInspectionResultVo : machineRemodelingInspectionResultVo) {
                        TblMachineRemodelingInspectionResult aRemodelingResult = new TblMachineRemodelingInspectionResult();
                        aRemodelingResult.setUpdateDate(new Date());
                        aRemodelingResult.setUpdateUserUuid(user.getUserUuid());
                        aRemodelingResult.setInspectionResult(aMachineRemodelingInspectionResultVo.getInspectionResult());
                        aRemodelingResult.setInspectionResultText(aMachineRemodelingInspectionResultVo.getInspectionResultText());

                        aRemodelingResult.setId(IDGenerator.generate());
                        TblMachineRemodelingInspectionResultPK resultPK = new TblMachineRemodelingInspectionResultPK();
                        resultPK.setInspectionItemId(aMachineRemodelingInspectionResultVo.getMstMachineInspectionItemId());
                        if (aMachineRemodelingInspectionResultVo.getRemodelingDetailId() != null && !"".equals(aMachineRemodelingInspectionResultVo.getRemodelingDetailId())) {
                            id = aMachineRemodelingInspectionResultVo.getRemodelingDetailId();
                        }
                        resultPK.setRemodelingDetailId(id);
                        resultPK.setSeq(aMachineRemodelingInspectionResultVo.getSeq());
                        aRemodelingResult.setTblMachineRemodelingInspectionResultPK(resultPK);

                        aRemodelingResult.setCreateDate(new Date());
                        aRemodelingResult.setCreateUserUuid(user.getUserUuid());
                        entityManager.persist(aRemodelingResult);
                    }
                }

                //設備ImageFile 追加
                List<TblMachineRemodelingDetailImageFileVo> machineRemodelingDetailImageFileVos = machineRemodelingDetailVo.getMachineRemodelingDetailImageFileVos();

                if (null != machineRemodelingDetailImageFileVos && !machineRemodelingDetailImageFileVos.isEmpty()) {

                    Query delImageFileQuery = entityManager.createNamedQuery("TblMachineRemodelingDetailImageFile.deleteByRemodelingDetailId");
                    delImageFileQuery.setParameter("remodelingDetailId", id);
                    delImageFileQuery.executeUpdate();

                    for (TblMachineRemodelingDetailImageFileVo aImageFileVo : machineRemodelingDetailImageFileVos) {
                        TblMachineRemodelingDetailImageFile aImageFile = new TblMachineRemodelingDetailImageFile();
                        aImageFile.setUpdateDate(new Date());
                        aImageFile.setUpdateUserUuid(user.getUserUuid());
                        aImageFile.setCreateDate(new Date());
                        aImageFile.setCreateDateUuid(user.getUserUuid());

                        TblMachineRemodelingDetailImageFilePK aPK = new TblMachineRemodelingDetailImageFilePK();
                        aPK.setRemodelingDetailId(id);
                        aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                        aImageFile.setTblMachineRemodelingDetailImageFilePK(aPK);

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
//        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//        response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_record_added"));
        return response;
    }

    
    /**
     * 設備改造詳細データがチェックして
     *
     * @param maintenanceId
     * @param seq
     * @return
     */
    public boolean checkTblMachineRemodelingDetail(String maintenanceId, int seq) {
      
        StringBuilder sql = new StringBuilder(" SELECT t FROM TblMachineRemodelingDetail t WHERE ");
        sql.append(" t.tblMachineRemodelingDetailPK.maintenanceId = :maintenanceId And t.tblMachineRemodelingDetailPK.seq = :seq ");
        Query query = entityManager.createQuery(sql.toString());
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
     * 設備改造メンテナンステーブルより、新仕様が登録されていない改造データを取得し一覧に表示する。
     *
     * @param user
     * @return
     */
    public TblMachineMaintenanceRemodelingVo getMachineMaintenanceRemodelings(LoginUser user) {

        TblMachineMaintenanceRemodelingVo resTblMachineRemodelings = new TblMachineMaintenanceRemodelingVo();
        List<TblMachineMaintenanceRemodelingVo> tblMachineRemodelingList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblMachineMaintenanceRemodeling t ");
        sql.append("WHERE t.mainteOrRemodel = :mainteOrRemodel  AND t.endDatetime != :endDatetime AND t.machineSpecHstId IS NULL  ORDER BY t.startDatetime DESC");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("mainteOrRemodel", CommonConstants.MAINTEORREMODEL_REMODEL);
        query.setParameter("endDatetime", CommonConstants.SYS_MAX_DATE);

        List<TblMachineMaintenanceRemodeling> list = query.getResultList();
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(user.getLangId(), "tbl_machine_maintenance_remodeling.remodeling_type");
        for (TblMachineMaintenanceRemodeling tblMachineRemodeling : list) {
            TblMachineMaintenanceRemodelingVo aresTblMachineRemodelings = new TblMachineMaintenanceRemodelingVo();
            MstMachine mstMachine = tblMachineRemodeling.getMstMachine();
            if (mstMachine != null) {
                //ID
                aresTblMachineRemodelings.setId(tblMachineRemodeling.getId());
                //設備UUID
                aresTblMachineRemodelings.setMachineUuid(tblMachineRemodeling.getMachineUuid());
                // 設備ID
                aresTblMachineRemodelings.setMachineId(mstMachine.getMachineId() == null ? "" : mstMachine.getMachineId());
                // 設備名称
                aresTblMachineRemodelings.setMachineName(mstMachine.getMachineName() == null ? "" : mstMachine.getMachineName());
                
                // 実施者
                Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
                userQuery.setParameter("uuid", tblMachineRemodeling.getCreateUserUuid());
                try {
                    MstUser users = (MstUser) userQuery.getSingleResult();
                    aresTblMachineRemodelings.setReportPersonName(users.getUserName() == null ? "" : users.getUserName());
                } catch (NoResultException e) {
                    aresTblMachineRemodelings.setReportPersonName("");
                }

                // 開始日時
                aresTblMachineRemodelings.setStartDatetimeStr(tblMachineRemodeling.getStartDatetime() == null ? "" : new FileUtil().getDateTimeFormatForStr(tblMachineRemodeling.getStartDatetime()));
                //  終了日時
                if(tblMachineRemodeling.getEndDatetime() != null){
                    aresTblMachineRemodelings.setEndDatetimeStr(tblMachineRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(tblMachineRemodeling.getEndDatetime()));
                }else{
                    aresTblMachineRemodelings.setEndDatetimeStr("-");
                }
                
                //改造区分 
                if (tblMachineRemodeling.getRemodelingType() != null) {
                    if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "",mstMachine.getCompanyId(), user).isError() == true) {
                        aresTblMachineRemodelings.setExternalFlg("1");
                        aresTblMachineRemodelings.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_maintenance_remodeling.remodeling_type", "" + tblMachineRemodeling.getRemodelingType(), user.getLangId()));
                    } else {
                        aresTblMachineRemodelings.setRemodelingType(tblMachineRemodeling.getRemodelingType());
                        aresTblMachineRemodelings.setExternalFlg("0");
                        for (int momi = 0; momi < mstChoiceList.getMstChoice().size(); momi++) {
                            MstChoice aMstChoice = mstChoiceList.getMstChoice().get(momi);
                            if (aMstChoice.getMstChoicePK().getSeq().equals(tblMachineRemodeling.getRemodelingType().toString())) {
                                aresTblMachineRemodelings.setRemodelingTypeText(aMstChoice.getChoice());
                                break;
                            }
                        }
                    }
                } else {
                    aresTblMachineRemodelings.setRemodelingTypeText("");
                }

                //報告事項	
                aresTblMachineRemodelings.setReport(tblMachineRemodeling.getReport() == null ? "" : tblMachineRemodeling.getReport());
                //手配・工事内容
                aresTblMachineRemodelings.setTblDirectionId(tblMachineRemodeling.getTblDirectionId() == null ? "" : tblMachineRemodeling.getTblDirectionId());
                if(tblMachineRemodeling.getTblDirection() != null){
                    aresTblMachineRemodelings.setTblDirectionCode(tblMachineRemodeling.getTblDirection().getDirectionCode() == null ? "" : tblMachineRemodeling.getTblDirection().getDirectionCode());
                }else{
                    aresTblMachineRemodelings.setTblDirectionCode("");
                }
                tblMachineRemodelingList.add(aresTblMachineRemodelings);
            }
        }
        resTblMachineRemodelings.setMachineMaintenanceRemodelingVo(tblMachineRemodelingList);

        return resTblMachineRemodelings;
    }
    
    
    /**
     * 設備改造データがチェックして
     * @param id
     * @return
     */
    public boolean getRemodelingExistCheck(String id) {
        Query query = entityManager.createNamedQuery("TblMachineMaintenanceRemodeling.findById");
        query.setParameter("id", id);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
    
    
     /**
     * 一覧で選択されているレコードを削除し、設備改造をなかったころにする。確認メッセージを表示し、キャンセルボタンが押されれば処理を取り消す。
     *
     * @param user
     * @param id
     * @return
     */
    @Transactional
    public int deleteRemodeling(LoginUser user, String id) {
        StringBuilder sql = new StringBuilder("DELETE FROM TblMachineMaintenanceRemodeling t WHERE t.id=:id");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("id", id);
        return query.executeUpdate();
    }
    
    
    /**
     * 設備改造詳細複数を取得可能
     *
     * @param loginUser
     * @param mainteId
     * @return
     */
    public TblMachineMaintenanceRemodelingVo getTblMachineRemodelingDetailByMachineId(LoginUser loginUser, String mainteId) {
        TblMachineMaintenanceRemodelingVo trdvL = new TblMachineMaintenanceRemodelingVo();
        TblMachineRemodelingDetailVo tblMachineRemodelingDetailVo;
        Query query;
      if (null != mainteId && !mainteId.trim().equals("")) {
            ///設備改造終了画面で終了ボタンを押下
            //　最新の改造IDにより、明細レコードを取得する TblMachineMaintenanceRemodeling
            StringBuilder sql = new StringBuilder("SELECT m FROM TblMachineRemodelingDetail m ");
            sql.append(" left join fetch TblMachineMaintenanceRemodeling m1 on m.tblMachineRemodelingDetailPK.maintenanceId = m1.id");
            sql.append(" where m1.id = :mainteId And m1.mainteOrRemodel = :mainteOrRemodel ");
            query = entityManager.createQuery(sql.toString());
            query.setParameter("mainteId", mainteId);//
            query.setParameter("mainteOrRemodel", CommonConstants.MAINTEORREMODEL_REMODEL);
        } else {
            trdvL.setError(true);
            trdvL.setErrorCode(ErrorMessages.E201_APPLICATION);
            trdvL.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_record_selected"));
            return trdvL;
        }

        List<TblMachineRemodelingDetailVo> tblMachineRemodelingDetailVoList = new ArrayList<>();

        List list = query.getResultList();

        for (int i = 0; i < list.size(); i++) {

            TblMachineRemodelingDetail tblMachineRemodelingDetail = (TblMachineRemodelingDetail) list.get(i);
            //maintenanceid
            trdvL.setId(tblMachineRemodelingDetail.getTblMachineRemodelingDetailPK().getMaintenanceId());
            MstMachine machine = tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getMstMachine();

            if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", machine.getCompanyId(), loginUser).isError()) {
                trdvL.setExternalFlg("1");
                //メンテ分類
                if (null != tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getMainteType()) {
                    trdvL.setMainteType(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getMainteType());
                    trdvL.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(machine.getCompanyId(), "tbl_machine_maintenance_remodeling.mainte_type", String.valueOf(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling()), loginUser.getLangId()));
                } else {
                    trdvL.setMainteTypeText("");
                }

                //改造分類
                if (null != tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getRemodelingType()) {
                    trdvL.setRemodelingType(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getRemodelingType());
                    trdvL.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(machine.getCompanyId(), "tbl_machine_maintenance_remodeling.remodeling_type", String.valueOf(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getRemodelingType()), loginUser.getLangId()));
                } else {
                    trdvL.setRemodelingTypeText("");
                }

            } else {
                trdvL.setExternalFlg("0");
                trdvL.setMainteType(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getMainteType());
                trdvL.setRemodelingType(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getRemodelingType());
            }

            //報告事項
            trdvL.setReport(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getReport() == null ? "" : tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getReport());

            trdvL.setTblDirectionId(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getTblDirectionId() == null ? "" : tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getTblDirectionId());
            if (tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getTblDirection() != null) {
                trdvL.setTblDirectionCode(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getTblDirection().getDirectionCode() == null ? "" : tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getTblDirection().getDirectionCode());
            } else {
                //システム設定により手配・工事テーブル参照しない場合はコードが直接保存されているのでセット
                if (tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getTblDirectionCode() != null) {
                    trdvL.setTblDirectionCode(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getTblDirectionCode());
                }
                else {
                    trdvL.setTblDirectionCode("");
                }
            }

            trdvL.setMachineName(tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getMstMachine().getMachineName() == null ? "" : tblMachineRemodelingDetail.getTblMachineMaintenanceRemodeling().getMstMachine().getMachineName());

            tblMachineRemodelingDetailVo = new TblMachineRemodelingDetailVo();
//            TblMachineRemodelingDetail tblMachineRemodelingDetail = tblMachineMaintenanceRemodeling.getTblMachineRemodelingDetail();
            if (tblMachineRemodelingDetail != null) {
                tblMachineRemodelingDetailVo.setId(tblMachineRemodelingDetail.getId());

                tblMachineRemodelingDetailVo.setMaintenanceId(tblMachineRemodelingDetail.getTblMachineRemodelingDetailPK().getMaintenanceId());
                tblMachineRemodelingDetailVo.setSeq(tblMachineRemodelingDetail.getTblMachineRemodelingDetailPK().getSeq());

                //改造理由大分類
                tblMachineRemodelingDetailVo.setRemodelReasonCategory1(String.valueOf(tblMachineRemodelingDetail.getRemodelReasonCategory1() == null ? "" : tblMachineRemodelingDetail.getRemodelReasonCategory1()));
                tblMachineRemodelingDetailVo.setRemodelReasonCategory1Text(tblMachineRemodelingDetail.getRemodelReasonCategory1Text() == null ? "" : tblMachineRemodelingDetail.getRemodelReasonCategory1Text());
                //改造理由中分類
                tblMachineRemodelingDetailVo.setRemodelReasonCategory2(String.valueOf(tblMachineRemodelingDetail.getRemodelReasonCategory2() == null ? "" : tblMachineRemodelingDetail.getRemodelReasonCategory2()));
                tblMachineRemodelingDetailVo.setRemodelReasonCategory2Text(tblMachineRemodelingDetail.getRemodelReasonCategory2Text() == null ? "" : tblMachineRemodelingDetail.getRemodelReasonCategory2Text());
                //改造理由小分類
                tblMachineRemodelingDetailVo.setRemodelReasonCategory3(String.valueOf(tblMachineRemodelingDetail.getRemodelReasonCategory3() == null ? "" : tblMachineRemodelingDetail.getRemodelReasonCategory3()));
                tblMachineRemodelingDetailVo.setRemodelReasonCategory3Text(tblMachineRemodelingDetail.getRemodelReasonCategory3Text() == null ? "" : tblMachineRemodelingDetail.getRemodelReasonCategory3Text());
                //改造理由
                tblMachineRemodelingDetailVo.setRemodelReason(tblMachineRemodelingDetail.getRemodelReason() == null ? "" : tblMachineRemodelingDetail.getRemodelReason());

                //改造指示大分類
                tblMachineRemodelingDetailVo.setRemodelDirectionCategory1(String.valueOf(tblMachineRemodelingDetail.getRemodelDirectionCategory1() == null ? "" : tblMachineRemodelingDetail.getRemodelDirectionCategory1()));
                tblMachineRemodelingDetailVo.setRemodelDirectionCategory1Text(tblMachineRemodelingDetail.getRemodelDirectionCategory1Text() == null ? "" : tblMachineRemodelingDetail.getRemodelDirectionCategory1Text());
                //改造指示中分類
                tblMachineRemodelingDetailVo.setRemodelDirectionCategory2(String.valueOf(tblMachineRemodelingDetail.getRemodelDirectionCategory2() == null ? "" : tblMachineRemodelingDetail.getRemodelDirectionCategory2()));
                tblMachineRemodelingDetailVo.setRemodelDirectionCategory2Text(tblMachineRemodelingDetail.getRemodelDirectionCategory2Text() == null ? "" : tblMachineRemodelingDetail.getRemodelDirectionCategory2Text());
                //改造指示小分類
                tblMachineRemodelingDetailVo.setRemodelDirectionCategory3(String.valueOf(tblMachineRemodelingDetail.getRemodelDirectionCategory3() == null ? "" : tblMachineRemodelingDetail.getRemodelDirectionCategory3()));
                tblMachineRemodelingDetailVo.setRemodelDirectionCategory3Text(tblMachineRemodelingDetail.getRemodelDirectionCategory3Text() == null ? "" : tblMachineRemodelingDetail.getRemodelDirectionCategory3Text());
                //改造指示
                tblMachineRemodelingDetailVo.setRemodelDirection(tblMachineRemodelingDetail.getRemodelDirection() == null ? "" : tblMachineRemodelingDetail.getRemodelDirection());

                //作業大分類
                tblMachineRemodelingDetailVo.setTaskCategory1(String.valueOf(tblMachineRemodelingDetail.getTaskCategory1() == null ? "" : tblMachineRemodelingDetail.getTaskCategory1()));
                tblMachineRemodelingDetailVo.setTaskCategory1Text(tblMachineRemodelingDetail.getTaskCategory1Text() == null ? "" : tblMachineRemodelingDetail.getTaskCategory1Text());
                //作業中分類
                tblMachineRemodelingDetailVo.setTaskCategory2(String.valueOf(tblMachineRemodelingDetail.getTaskCategory2() == null ? "" : tblMachineRemodelingDetail.getTaskCategory2()));
                tblMachineRemodelingDetailVo.setTaskCategory2Text(tblMachineRemodelingDetail.getTaskCategory2Text() == null ? "" : tblMachineRemodelingDetail.getTaskCategory2Text());
                //作業小分類
                tblMachineRemodelingDetailVo.setTaskCategory3(String.valueOf(tblMachineRemodelingDetail.getTaskCategory3() == null ? "" : tblMachineRemodelingDetail.getTaskCategory3()));
                tblMachineRemodelingDetailVo.setTaskCategory3Text(tblMachineRemodelingDetail.getTaskCategory3Text() == null ? "" : tblMachineRemodelingDetail.getTaskCategory3Text());
                //作業
                tblMachineRemodelingDetailVo.setTask(tblMachineRemodelingDetail.getTask() == null ? "" : tblMachineRemodelingDetail.getTask());

                List<TblMachineRemodelingDetailImageFileVo> fileResultVos = new ArrayList<>();
                //TblMoldMaintenanceDetailImageFileVo　検索
                List<TblMachineRemodelingDetailImageFile> tmpFileResults = entityManager.createQuery("SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.tblMachineRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId ")
                        .setParameter("remodelingDetailId", tblMachineRemodelingDetail.getId())
                        .getResultList();

                TblMachineRemodelingDetailImageFileVo machineRemodelingDetailImageFileVo = null;
                for (TblMachineRemodelingDetailImageFile aFile : tmpFileResults) {
                    machineRemodelingDetailImageFileVo = new TblMachineRemodelingDetailImageFileVo();
                    machineRemodelingDetailImageFileVo.setRemodelingDetailId(tblMachineRemodelingDetail.getId());
                    machineRemodelingDetailImageFileVo.setSeq("" + aFile.getTblMachineRemodelingDetailImageFilePK().getSeq());
                    machineRemodelingDetailImageFileVo.setFileType("" + aFile.getFileType());
                    machineRemodelingDetailImageFileVo.setFileExtension(aFile.getFileExtension());
                    machineRemodelingDetailImageFileVo.setFileUuid(aFile.getFileUuid());
                    machineRemodelingDetailImageFileVo.setRemarks(aFile.getRemarks());
                    machineRemodelingDetailImageFileVo.setThumbnailFileUuid(aFile.getThumbnailFileUuid());
                    if (null != aFile.getTakenDate()) {
                        machineRemodelingDetailImageFileVo.setTakenDateStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDate()));
                    }
                    if (null != aFile.getTakenDateStz()) {
                        machineRemodelingDetailImageFileVo.setTakenDateStzStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDateStz()));
                    }

                    fileResultVos.add(machineRemodelingDetailImageFileVo);
                }

                tblMachineRemodelingDetailVo.setMachineRemodelingDetailImageFileVos(fileResultVos);

                tblMachineRemodelingDetailVoList.add(tblMachineRemodelingDetailVo);
            }
        }

        trdvL.setMachineRemodelingDetailVo(tblMachineRemodelingDetailVoList);
        return trdvL;

    }
    
    
    /**
     * 設備改造詳細_追加 設備仕様履歴_追加・更新_改造日を開始日とする新たな履歴の作成、それまで最新だった仕様の終了日を改造日の前日で更新。
     * 設備仕様_追加 設備改造メンテナンス_更新_設備仕様履歴IDの更新 設備部品関係_追加
     *
     * @param tblMachineRemodelingDetailVo
     * @param user
     * @return 
     */
    @Transactional
    public BasicResponse postMachineRemodelingResultRegistration(TblMachineRemodelingDetailVo tblMachineRemodelingDetailVo, LoginUser user) {
        BasicResponse response = new BasicResponse();
        
        String machineUuid = tblMachineRemodelingDetailVo.getMachineUuid();
        String specName = tblMachineRemodelingDetailVo.getSpecName();
        String maintenanceId = tblMachineRemodelingDetailVo.getMaintenanceId();

        Calendar calendar = Calendar.getInstance();
        if (specName != null && !"".equals(specName.trim())) {
            //設備仕様履歴_更新
            String specHisId = tblMachineRemodelingDetailVo.getSpecHisId();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date endDate = calendar.getTime();
            try {
                Query queryMoldSpecHis = entityManager.createNamedQuery("MstMachineSpecHistory.findById");
                queryMoldSpecHis.setParameter("id", specHisId);
                MstMachineSpecHistory mstMachineSpecHistory = (MstMachineSpecHistory) queryMoldSpecHis.getSingleResult();
                mstMachineSpecHistory.setEndDate(endDate);
                mstMachineSpecHistory.setUpdateDate(new Date());
                mstMachineSpecHistory.setUpdateUserUuid(user.getUserUuid());
                entityManager.merge(mstMachineSpecHistory);
            } catch (NoResultException ex) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_data_deleted"));
                return response;
            }

            //設備仕様履歴_追加
//            calendar.add(Calendar.DAY_OF_MONTH, +1);
//            Date newEndDate = calendar.getTime();
            MstMachineSpecHistory newhistory = new MstMachineSpecHistory();
            String newSpecHisId = IDGenerator.generate();
            newhistory.setId(newSpecHisId);
            newhistory.setMachineUuid(machineUuid);
            newhistory.setStartDate(new Date());
            newhistory.setEndDate(CommonConstants.SYS_MAX_DATE);
            newhistory.setMachineSpecName(specName);
            newhistory.setCreateDate(new Date());
            newhistory.setCreateUserUuid(user.getUserUuid());
            newhistory.setUpdateDate(new Date());
            newhistory.setUpdateUserUuid(user.getUserUuid());

            entityManager.persist(newhistory);

            //設備属性データ
            List<MstMachineAttributeVo> attributeses = tblMachineRemodelingDetailVo.getAttributes();

            if (attributeses != null && attributeses.size() > 0) {
                for (MstMachineAttributeVo attribute : attributeses) {
                    //設備仕様_追加
                    MstMachineSpecPK mstMachineSpecPK = new MstMachineSpecPK();
                    MstMachineSpec spec = new MstMachineSpec();
                    spec.setId(IDGenerator.generate());
                    mstMachineSpecPK.setAttrId(attribute.getId());
                    mstMachineSpecPK.setMachineSpecHstId(newSpecHisId);
                    spec.setMstMachineSpecPK(mstMachineSpecPK);
                    spec.setAttrValue(attribute.getAttrValue() == null ? "" : attribute.getAttrValue());
                    spec.setCreateDate(new Date());
                    spec.setCreateUserUuid(user.getUserUuid());
                    spec.setUpdateDate(new Date());
                    spec.setUpdateUserUuid(user.getUserUuid());
                    entityManager.persist(spec);
                }
            }

            //設備改造メンテナンス_更新				
            Query queryMaintenanceRemodeling = entityManager.createNamedQuery("TblMachineMaintenanceRemodeling.findById");
            queryMaintenanceRemodeling.setParameter("id", maintenanceId);
            try {
                TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling = (TblMachineMaintenanceRemodeling) queryMaintenanceRemodeling.getSingleResult();
                tblMachineMaintenanceRemodeling.setMachineSpecHstId(newSpecHisId);
                tblMachineMaintenanceRemodeling.setUpdateDate(new Date());
                tblMachineMaintenanceRemodeling.setUpdateUserUuid(user.getUserUuid());
                entityManager.merge(tblMachineMaintenanceRemodeling);
            } catch (NoResultException ex) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_data_deleted"));
                return response;
            }
           
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_not_null"));
            return response;
        }

        return response;
    }
    
    
    /**
     * バッチで設備改造詳細データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMachineRemodelingDetailVo getExtMachineRemodelingDetailsByBatch(String latestExecutedDate, String machineUuid) {
        TblMachineRemodelingDetailVo resList = new TblMachineRemodelingDetailVo();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblMachineRemodelingDetail t join MstApiUser u on u.companyId = t.tblMachineMaintenanceRemodeling.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.tblMachineMaintenanceRemodeling.mstMachine.uuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMachineRemodelingDetail> tmpList = query.getResultList();
        for (TblMachineRemodelingDetail tblMachineRemodelingDetail : tmpList) {
            tblMachineRemodelingDetail.setTblMachineMaintenanceRemodeling(null);
        }
        resList.setTblMachineRemodelingDetails(tmpList);
        return resList;
    }
    
    
    /**
     * バッチで設備改造詳細データを更新
     *
     * @param machineRemodelingDetails
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineRemodelingDetailsByBatch(List<TblMachineRemodelingDetail> machineRemodelingDetails) {
        BasicResponse response = new BasicResponse();
        if (machineRemodelingDetails != null && !machineRemodelingDetails.isEmpty()) {
            for (TblMachineRemodelingDetail aMachineRemodelingDetail : machineRemodelingDetails) {
                TblMachineMaintenanceRemodeling macheineMaintenanceRemodeling = entityManager.find(TblMachineMaintenanceRemodeling.class, aMachineRemodelingDetail.getTblMachineRemodelingDetailPK().getMaintenanceId());
                if (null != macheineMaintenanceRemodeling) {
                    
                    List<TblMachineRemodelingDetail> oldMachineRemodelingDetails = entityManager.createQuery("SELECT t FROM TblMachineRemodelingDetail t WHERE t.tblMachineRemodelingDetailPK.maintenanceId = :maintenanceId and t.tblMachineRemodelingDetailPK.seq = :seq ")
                            .setParameter("maintenanceId", aMachineRemodelingDetail.getTblMachineRemodelingDetailPK().getMaintenanceId())
                            .setParameter("seq", aMachineRemodelingDetail.getTblMachineRemodelingDetailPK().getSeq())
                            .setMaxResults(1)
                            .getResultList();

                    TblMachineRemodelingDetail newDetail;
                    if (null != oldMachineRemodelingDetails && !oldMachineRemodelingDetails.isEmpty()) {
                        newDetail = oldMachineRemodelingDetails.get(0);
                    } else {
                        newDetail = new TblMachineRemodelingDetail();
                        TblMachineRemodelingDetailPK pk = new TblMachineRemodelingDetailPK();
                        pk.setMaintenanceId(aMachineRemodelingDetail.getTblMachineRemodelingDetailPK().getMaintenanceId());
                        pk.setSeq(aMachineRemodelingDetail.getTblMachineRemodelingDetailPK().getSeq());
                        newDetail.setTblMachineRemodelingDetailPK(pk);
                        
                    }
                    newDetail.setId(aMachineRemodelingDetail.getId());
                    newDetail.setRemodelReasonCategory1(aMachineRemodelingDetail.getRemodelReasonCategory1());
                    newDetail.setRemodelReasonCategory1Text(aMachineRemodelingDetail.getRemodelReasonCategory1Text());
                    newDetail.setRemodelReasonCategory2(aMachineRemodelingDetail.getRemodelReasonCategory2());
                    newDetail.setRemodelReasonCategory2Text(aMachineRemodelingDetail.getRemodelReasonCategory2Text());
                    newDetail.setRemodelReasonCategory3(aMachineRemodelingDetail.getRemodelReasonCategory3());
                    newDetail.setRemodelReasonCategory3Text(aMachineRemodelingDetail.getRemodelReasonCategory3Text());
                    newDetail.setRemodelReason(aMachineRemodelingDetail.getRemodelReason());
                    newDetail.setRemodelDirectionCategory1(aMachineRemodelingDetail.getRemodelDirectionCategory1());
                    newDetail.setRemodelDirectionCategory1Text(aMachineRemodelingDetail.getRemodelDirectionCategory1Text());
                    newDetail.setRemodelDirectionCategory2(aMachineRemodelingDetail.getRemodelDirectionCategory2());
                    newDetail.setRemodelDirectionCategory2Text(aMachineRemodelingDetail.getRemodelDirectionCategory2Text());
                    newDetail.setRemodelDirectionCategory3(aMachineRemodelingDetail.getRemodelDirectionCategory3());
                    newDetail.setRemodelDirectionCategory3Text(aMachineRemodelingDetail.getRemodelDirectionCategory3Text());
                    newDetail.setRemodelDirection(aMachineRemodelingDetail.getRemodelDirection());
                    newDetail.setTaskCategory1(aMachineRemodelingDetail.getTaskCategory1());
                    newDetail.setTaskCategory1Text(aMachineRemodelingDetail.getTaskCategory1Text());
                    newDetail.setTaskCategory2(aMachineRemodelingDetail.getTaskCategory2());
                    newDetail.setTaskCategory2Text(aMachineRemodelingDetail.getTaskCategory2Text());
                    newDetail.setTaskCategory3(aMachineRemodelingDetail.getTaskCategory3());
                    newDetail.setTaskCategory3Text(aMachineRemodelingDetail.getTaskCategory3Text());
                    newDetail.setTask(aMachineRemodelingDetail.getTask());

                    newDetail.setCreateDate(aMachineRemodelingDetail.getCreateDate());
                    newDetail.setCreateUserUuid(aMachineRemodelingDetail.getCreateUserUuid());
                    newDetail.setUpdateDate(new Date());
                    newDetail.setUpdateUserUuid(aMachineRemodelingDetail.getUpdateUserUuid());

                    if (null != oldMachineRemodelingDetails && !oldMachineRemodelingDetails.isEmpty()) {
                        entityManager.merge(newDetail);
                    } else {
                        entityManager.persist(newDetail);
                    }
                }
            }
        }
        response.setError(false);
        return response;
    }

    /**
     * バッチで設備改造詳細ImageFileデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMachineRemodelingDetailVo getExtMachineRemodelingDetailImageFilesByBatch(String latestExecutedDate, String machineUuid) {
        TblMachineRemodelingDetailVo resList = new TblMachineRemodelingDetailVo();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblMachineRemodelingDetailImageFile t join MstApiUser u on u.companyId = t.tblMachineRemodelingDetail.tblMachineMaintenanceRemodeling.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.tblMachineRemodelingDetail.tblMachineMaintenanceRemodeling.mstMachine.uuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMachineRemodelingDetailImageFile> tmpList = query.getResultList();
        List<TblMachineRemodelingDetailImageFileVo> resVos = new ArrayList<>();
        FileUtil fu = new FileUtil();
        for (TblMachineRemodelingDetailImageFile aDetailImageFile : tmpList) {
            TblMachineRemodelingDetailImageFileVo aVo = new TblMachineRemodelingDetailImageFileVo();
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
            aVo.setRemodelingDetailId(aDetailImageFile.getTblMachineRemodelingDetailImageFilePK().getRemodelingDetailId());
            aVo.setSeq("" + aDetailImageFile.getTblMachineRemodelingDetailImageFilePK().getSeq());
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
        resList.setMachineRemodelingDetailImageFileVos(resVos);
        return resList;
    }

    /**
     * バッチで設備改造詳細ImageFileデータを更新
     *
     * @param imageFileVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldRemodelingDetailImageFilesByBatch(List<TblMachineRemodelingDetailImageFileVo> imageFileVos) {
        BasicResponse response = new BasicResponse();

        if (imageFileVos != null && !imageFileVos.isEmpty()) {
            FileUtil fu = new FileUtil();
            for (TblMachineRemodelingDetailImageFileVo aVo : imageFileVos) {
                List<TblMachineRemodelingDetail> machineRemodelingDetails = entityManager.createQuery("from TblMachineRemodelingDetail t where t.id = :remodelingDetailId ")
                        .setParameter("remodelingDetailId", aVo.getRemodelingDetailId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != machineRemodelingDetails && !machineRemodelingDetails.isEmpty()) {                    
                    List<TblMachineRemodelingDetailImageFile> oldImageFiles = entityManager.createQuery("SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.tblMachineRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId and t.tblMachineRemodelingDetailImageFilePK.seq = :seq ")
                            .setParameter("remodelingDetailId", aVo.getRemodelingDetailId())
                            .setParameter("seq", Integer.parseInt(aVo.getSeq()))
                            .setMaxResults(1)
                            .getResultList();

                    TblMachineRemodelingDetailImageFile newImageFile;
                    if (null != oldImageFiles && !oldImageFiles.isEmpty()) {
                        newImageFile = oldImageFiles.get(0);
                    } else {
                        newImageFile = new TblMachineRemodelingDetailImageFile();
                        TblMachineRemodelingDetailImageFilePK pk = new TblMachineRemodelingDetailImageFilePK();
                        pk.setRemodelingDetailId(aVo.getRemodelingDetailId());
                        pk.setSeq(Integer.parseInt(aVo.getSeq()));
                        newImageFile.setTblMachineRemodelingDetailImageFilePK(pk);

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
        return response;
    }
}
