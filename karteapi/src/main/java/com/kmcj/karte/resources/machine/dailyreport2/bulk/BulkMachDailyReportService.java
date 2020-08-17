/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2.bulk;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.dailyreport2.MachineDailyReport2Service;
import com.kmcj.karte.resources.machine.dailyreport2.TblMachineDailyReport2;
import com.kmcj.karte.resources.machine.dailyreport2.TblMachineDailyReport2Detail;
import com.kmcj.karte.resources.machine.dailyreport2.TblMachineDailyReport2ProdDetail;
//import com.kmcj.karte.resources.machine.dailyreport2.TblMachineDailyReport2Detail;
import com.kmcj.karte.resources.machine.dailyreport2.bulk.models.BulkMDReport;
import com.kmcj.karte.resources.machine.dailyreport2.bulk.models.BulkMDReportDetail;
import com.kmcj.karte.resources.machine.dailyreport2.bulk.models.BulkMDReportProdDetail;
import com.kmcj.karte.resources.machine.downtime.MstMachineDowntime;
import com.kmcj.karte.resources.machine.downtime.MstMachineDowntimeService;
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.material.MstMaterialService;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.TblProductionService;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.production.lot.balance.TblProductionLotBalanceService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.resources.work.TblWork;
import com.kmcj.karte.resources.work.TblWorkService;
import com.kmcj.karte.resources.work.phase.MstWorkPhase;
import com.kmcj.karte.resources.work.phase.MstWorkPhaseService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.TimezoneConverter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class BulkMachDailyReportService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    MstDictionaryService mstDictionaryService;
    @Inject
    MstMachineService mstMachineService;
    @Inject
    MstMoldService mstMoldService;
    @Inject
    MstComponentService mstComponentService;
    @Inject
    MstUserService mstUserService;
    @Inject
    MstWorkPhaseService mstWorkPhaseService;
    @Inject
    MstMoldComponentRelationService mstMoldComponentRelationService;
    @Inject
    MstProcedureService mstProcedureService;
    @Inject
    TblProductionLotBalanceService tblProductionLotBalanceService;
    @Inject
    MstMaterialService mstMaterialService;
    @Inject
    MachineDailyReport2Service machineDailyReport2Service;
    @Inject
    TblWorkService tblWorkService;
    @Inject
    TblProductionService tblProductionService;
    @Inject
    MstMachineDowntimeService mstMachineDowntimeService;
    @Inject
    CnfSystemService cnfSystemService;
    
    private LoginUser loginUser;
    private final List<TblWork> tblWorkList = new ArrayList<>();
    private final List<TblProduction> tblProductionList = new ArrayList<>();
    private final List<TblProductionDetail> tblProductionDetailList = new ArrayList<>();
    
    @Transactional(rollbackOn = Exception.class)
    public BasicResponse createMachineDailyReport(BulkMDReport report, LoginUser loginUser) throws Exception {
        this.loginUser = loginUser;
        BasicResponse response = new BasicResponse();
        //機械日報ヘダー
        if (!checkHeaderFormat(report, response)) return response;
        if (!validateTime(report, response)) return response;
        TblMachineDailyReport2 tblReport = new TblMachineDailyReport2();
        tblReport.setTblMachineDailyReport2DetailList(new ArrayList<>());
        if (!createHeader(report, tblReport, response)) return response;
        //機械日報明細
        for (BulkMDReportDetail detail : report.getDetails()) {
            if (!checkDetailFormat(detail, response)) return response;
            if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_WORK) {
                //作業
                if (!createWork(report, detail, tblReport, response)) return response;
            }
            else if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD) {
                //生産実績
                if (!createProduction(report, detail, tblReport, response)) return response;
            }
            else if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_DOWNTIME) {
                //設備停止
                if (!createDowntime(detail, tblReport, response)) return response;
            }
        }
        //作業実績、生産実績テーブルにレコード作成
        insertWorkProduction();
        //機械日報明細のロット番号を更新(在庫Appの改修により機械日報生産明細のロット番号で生産実績のロット番号が上書きされてしまうため)
        updateMdrLotNumber(tblReport);
        machineDailyReport2Service.updateMachienDailyReport2(tblReport, loginUser);
        return response;
    }
    
    private void updateMdrLotNumber(TblMachineDailyReport2 tblReport) {
        for (TblMachineDailyReport2Detail detail: tblReport.getTblMachineDailyReport2DetailList()) {
            if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD) {
                if (detail.getProductionId() != null) {
                    TblProduction production = getProductionFromList(detail.getProductionId());
                    if (production != null && detail.getTblMachineDailyReport2ProdDetailList().size() > 0) {
                        TblMachineDailyReport2ProdDetail prodDetail = detail.getTblMachineDailyReport2ProdDetailList().get(0);
                        prodDetail.setLotNumber(production.getLotNumber());
                    }
                }
            }
        }
    }
    
    private TblProduction getProductionFromList(String productionId) {
        for (TblProduction production: tblProductionList) {
            if (production.getId().equals(productionId)) {
                return production;
            }
        }
        return null;
    }
    
    /**
     * 開始時刻、終了時刻が営業時間内か、合計時間が1440分を超えていないかチェック
     * @param report
     * @param response
     * @return 
     */
    private boolean validateTime(BulkMDReport report,  BasicResponse response) {
        //システム設定から業務開始時刻を取得
        CnfSystem cnfSystem =  cnfSystemService.findByKey("system", "business_start_time");
        String businessStartTime = cnfSystem.getConfigValue();
        if (businessStartTime.length() == 4) {
            businessStartTime = "0" + businessStartTime;
        }
        //日報日付の業務開始日時、終了日時を算出
        String reportDate = DateFormat.dateToStr(report.getReportDateValue(), DateFormat.DATE_FORMAT);
        java.util.Date startTime = DateFormat.strToDatetime(reportDate + " " + businessStartTime + ":00");
        java.util.Date endTime = DateFormat.getAfterDay(startTime);
        //明細をループして開始時刻、終了時刻の範囲をチェックし、合計時間を足し込み
        int totalDuration = 0;
        String msg = null;
        for (BulkMDReportDetail detail : report.getDetails()) {
            if (detail.getStartDatetimeValue() == null || detail.getEndDatetimeValue() == null) continue; //Nullチェックは別メソッドで
            if (detail.getStartDatetimeValue().before(startTime) || detail.getStartDatetimeValue().after(endTime)) {
                msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_out_of_business_date").replace("%s", reportDate) + 
                    "(startDatetime: " + detail.getStartDatetime() + ")";
            }
            else if (detail.getEndDatetimeValue().before(startTime) || detail.getEndDatetimeValue().after(endTime)) {
                msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_out_of_business_date").replace("%s", reportDate) + 
                    "(endDatetime: " + detail.getEndDatetime() + ")";
            }
            if (msg != null) {
                setErrorInfo(response, msg);
                return false;
            }
            totalDuration = totalDuration + detail.getDurationMinutes();
        }
        if (totalDuration > 1440) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_report_error_total_time");
            setErrorInfo(response, msg);
            return false;
        }
        return true;
    }
    
    /**
     * 受け取った機械日報Jsonのヘダー項目のフォーマットをチェック
     * @param report
     * @param response
     * @return 
     */
    private boolean checkHeaderFormat(BulkMDReport report,  BasicResponse response) {
        String msg = null;
        if (report.getReportDate() == null || report.getReportDate().equals("")) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "reportDate");
        }
        else if (report.getReportDateValue() == null) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid") + "(reportDate : " + report.getReportDate() + ")";
        }
        else if (report.getMachineId() == null || report.getMachineId().equals("")) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "machineId");
        }
        else if (report.getReportPersonId() == null || report.getReportPersonId().equals("")) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "reportPersonId");
        }
        else if (report.getDetails() == null || report.getDetails().size() == 0) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "details");
        }
        if (msg != null) {
            setErrorInfo(response, msg);
            return false;
        }
        return true;
    }
    
    /**
     * 機械日報ヘダー作成
     * @param report
     * @param response
     * @return 
     */
    private boolean createHeader(BulkMDReport report, TblMachineDailyReport2 tblReport, BasicResponse response) {
        //マスタ参照
        MstMachine machine = getMstMachine(report.getMachineId(), response);
        if (machine == null) return false;
        //同一日付、同一設備ですでに機械日報が登録されていないか
        if (machineDailyReport2Service.getMachineDailyReport2ByUniqueKey(report.getReportDateValue(), machine.getUuid()) != null) {
            String msg = mstDictionaryService.getDictionaryValue(
                loginUser.getLangId(), "msg_error_mdr2_duplicate").replace("%s", DateFormat.dateToStr(report.getReportDateValue(), DateFormat.DATE_FORMAT));
            setErrorInfo(response, msg);
            return false;
        }
        MstUser reportUser = getMstUser(report.getReportPersonId(), response, "reportPersonId");
        if (reportUser == null) return false;
        //ヘダーテーブル作成
        tblReport.setReportDate(report.getReportDateValue());
        tblReport.setMachineUuid(machine.getUuid());
        tblReport.setReportPersonUuid(reportUser.getUuid());
        tblReport.setAdded(true);
        return true;
    }

    /**
     * IDから設備マスタ取得
     * @param machineId
     * @param response
     * @return 
     */
    private MstMachine getMstMachine(String machineId, BasicResponse response) {
        MstMachine machine = mstMachineService.getMstMachineByMachineId(machineId);
        if (machine == null) {
            setCodeMissingError(response, "machineId", machineId);
        }
        return machine;
    }
    
    /**
     * IDから金型マスタ取得
     * @param moldId
     * @param response
     * @return 
     */
    private MstMold getMstMold(String moldId, BasicResponse response) {
        MstMold mold = mstMoldService.getMstMoldByMoldId(moldId);
        if (mold == null) {
            setCodeMissingError(response, "moldId", moldId);
        }
        return mold;
    }
    
    /**
     * 部品コードから部品マスタ取得
     * @param partCode
     * @param response
     * @return 
     */
    private MstComponent getMstComponent(String partCode, BasicResponse response) {
        MstComponent component = mstComponentService.getMstComponentByCode(partCode);
        if (component == null) {
            setCodeMissingError(response, "partCode", partCode);
        }
        return component;
    }
    
    /**
     * IDからユーザーマスタ取得
     * @param userId
     * @param response
     * @param elementName
     * @return 
     */
    private MstUser getMstUser(String userId, BasicResponse response, String elementName) {
        MstUser mstUser = mstUserService.getMstUser(userId);
        if (mstUser == null) {
            setCodeMissingError(response, elementName, userId);
        }
        return mstUser;
    }
    
    /**
     * コードから工程マスタ取得
     * @param workPhaseCode
     * @param response
     * @return 
     */
    private MstWorkPhase getMstWorkPhase(String workPhaseCode, BasicResponse response) {
        MstWorkPhase mstWorkPhase = mstWorkPhaseService.getMstWorkPhaseByCode(workPhaseCode);
        if (mstWorkPhase == null) {
            setCodeMissingError(response, "workPhaseCode", workPhaseCode);
        }
        return mstWorkPhase;
    }

    /**
     * 材料コードから材料マスタ取得
     * @param mateiralCode
     * @param response
     * @return 
     */
    private MstMaterial getMstMaterial(String materialCode, BasicResponse response, String elementName) {
        MstMaterial mstMaterial = mstMaterialService.getMstMaterialByCode(materialCode);
        if (mstMaterial == null) {
            setCodeMissingError(response, elementName, materialCode);
        }
        return mstMaterial;
    }
    
    /**
     * 設備停止時間コードから設備停止時間マスタ取得
     * @param downtimeCode
     * @param response
     * @return 
     */
    private MstMachineDowntime getMstMachineDowntime(String downtimeCode, BasicResponse response) {
        MstMachineDowntime machineDowntime = mstMachineDowntimeService.getMstMachineDowntimeByCode(downtimeCode);
        if (machineDowntime == null) {
            setCodeMissingError(response, "machineDowntimeCode", downtimeCode);
        }
        return machineDowntime;
    }
    
    /**
     * 機械日報Jsonの明細レコードフォーマットチェック
     * @param detail
     * @param response
     * @return 
     */
    private boolean checkDetailFormat(BulkMDReportDetail detail, BasicResponse response) {
        String msg = null;
        if (detail.getDetailType() < 1 || detail.getDetailType() > 3) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid") + "(detailType : " + detail.getDetailType() + ")";
        }
        else if (detail.getStartDatetime() == null || detail.getStartDatetime().equals("")) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "startDatetime");
        }
        else if (detail.getStartDatetimeValue() == null) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid") + "(startDatetime : " + detail.getStartDatetime() + ")";
        }
        else if (detail.getEndDatetime() == null || detail.getEndDatetime().equals("")) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "endDatetime");
        }
        else if (detail.getEndDatetimeValue() == null) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid") + "(endDatetime : " + detail.getEndDatetime() + ")";
        }
        else if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_WORK || detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD) {
            //作業、生産のとき
            if (detail.getWorkerId() == null || detail.getWorkerId().equals("")) {
                msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "workerId");
            }
            else if (detail.getWorkPhaseCode() == null || detail.getWorkPhaseCode().equals("")) {
                msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "workPhaseCode");
            }
            else if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD) {
                //生産のとき生産明細がなければエラー
                if (detail.getProdDetails() == null || detail.getProdDetails().size() == 0) {
                    msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "prodDetails");
                }
            }
        }
        else if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_DOWNTIME) {
            //設備停止のとき
            if (detail.getMachineDowntimeCode() == null || detail.getMachineDowntimeCode().equals("")) {
                msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "machineDowntimeCode");
            }
            else if (detail.getDowntimeComment() != null && detail.getDowntimeComment().length() > 200) {
                msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length") + "(downtimeComment : " + detail.getDowntimeComment() + ")";
            }
        }
        if (msg != null) {
            setErrorInfo(response, msg);
            return false;
        }
        return true;
    }
    
    private boolean checkProdDetailFormat(BulkMDReportProdDetail prodDetail, BasicResponse response) {
        String msg = null;
        if (prodDetail.getMaterial01LotNo() != null && prodDetail.getMaterial01LotNo().length() > 45) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length") + "(material01LotNo : " + prodDetail.getMaterial01LotNo() + ")";
        }
        else if (prodDetail.getMaterial02LotNo() != null && prodDetail.getMaterial02LotNo().length() > 45) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length") + "(material02LotNo : " + prodDetail.getMaterial02LotNo() + ")";
        }
        else if (prodDetail.getMaterial03LotNo() != null && prodDetail.getMaterial03LotNo().length() > 45) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length") + "(material03LotNo : " + prodDetail.getMaterial03LotNo() + ")";
        }
        if (msg != null) {
            setErrorInfo(response, msg);
            return false;
        }
        return true;
    }
    
    /**
     * 金型UUIDと部品IDから関係マスタを取得
     * @param moldUuid
     * @param componentId
     * @return 
     */
    private MstMoldComponentRelation getMstMoldComponentRelation(String moldUuid, String componentId) {
        if (moldUuid == null || componentId == null) return null;
        return mstMoldComponentRelationService.getMstMoldComponentRelation(moldUuid, componentId);
    }
    
    /**
     * 作業レコード作成
     * @param detail
     * @param response
     * @return 
     */
    //@Transactional
    private boolean createWork(BulkMDReport report, BulkMDReportDetail detail, TblMachineDailyReport2 tblReport, BasicResponse response) {
        //作業テーブルオブジェクト作成
        TblWork tblWork = new TblWork();
        tblWork.setId(IDGenerator.generate());
        tblWork.setWorkingDate(report.getReportDateValue());
        tblWork.setStartDatetime(detail.getStartDatetimeValue());
        tblWork.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblWork.getStartDatetime()));
        tblWork.setEndDatetime(detail.getEndDatetimeValue());
        tblWork.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblWork.getEndDatetime()));
        tblWork.setWorkingTimeMinutes(detail.getDurationMinutes());
        tblWork.setActualTimeMinutes(detail.getDurationMinutes());
        tblWork.setBreakTimeMinutes(0);
        tblWork.setMachineUuid(tblReport.getMachineUuid());
        tblWork.setCreateDate(new java.util.Date());
        tblWork.setUpdateDate(tblWork.getCreateDate());
        tblWork.setCreateUserUuid(loginUser.getUserUuid());
        tblWork.setUpdateUserUuid(loginUser.getUserUuid());
        //機械日報明細オブジェクト作成
        TblMachineDailyReport2Detail tblReportDetail = new TblMachineDailyReport2Detail();
        tblReportDetail.setAdded(true);
        tblReportDetail.setStartDatetime(detail.getStartDatetimeValue());
        tblReportDetail.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblWork.getStartDatetime()));
        tblReportDetail.setEndDatetime(detail.getEndDatetimeValue());
        tblReportDetail.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblWork.getEndDatetime()));
        tblReportDetail.setDurationMinitues(detail.getDurationMinutes());
        tblReportDetail.setDetailType(TblMachineDailyReport2.DETAIL_TYPE_WORK);
        tblReportDetail.setOperatingFlg(0);
        //マスタ参照
        if (detail.getPartCode() != null && !detail.getPartCode().equals("")) {
            MstComponent mstComponent = getMstComponent(detail.getPartCode(), response);
            if (mstComponent != null) {
                tblWork.setComponentId(mstComponent.getId());
                tblReportDetail.setFirstComponentId(mstComponent.getId());
            }
        }
        if (detail.getMoldId() != null && !detail.getMoldId().equals("")) {
            MstMold mold = getMstMold(detail.getMoldId(), response);
            if (mold != null) {
                tblWork.setMoldUuid(mold.getUuid());
                tblReportDetail.setMoldUuid(mold.getUuid());
            }
            else {
                return false;
            }
        }
        if (detail.getWorkerId() != null && !detail.getWorkerId().equals("")) {
            MstUser worker = getMstUser(detail.getWorkerId(), response, "workerId");
            if (worker != null) {
                tblWork.setPersonUuid(worker.getUuid());
                tblReportDetail.setWorkerUuid(worker.getUuid());
            }
            else {
                return false;
            }
        }
        if (detail.getWorkPhaseCode() != null && !detail.getWorkPhaseCode().equals("")) {
            MstWorkPhase mstWorkPhase = getMstWorkPhase(detail.getWorkPhaseCode(), response);
            if (mstWorkPhase != null) {
                tblWork.setWorkPhaseId(mstWorkPhase.getId());
                tblReportDetail.setWorkPhaseId(mstWorkPhase.getId());
            }
            else {
                return false;
            }
        }
        tblWorkList.add(tblWork);
        //作業実績IDを機械日報明細に保持
        tblReportDetail.setWorkId(tblWork.getId());
        tblReport.getTblMachineDailyReport2DetailList().add(tblReportDetail);
        return true;
    }
    
   
    /**
     * 生産実績レコード作成
     * @param report
     * @param detail
     * @param tblReport
     * @param response
     * @return 
     */
    private boolean createProduction(BulkMDReport report, BulkMDReportDetail detail, TblMachineDailyReport2 tblReport, BasicResponse response) {
        //生産実績オブジェクト作成
        TblProduction tblProduction = new TblProduction();
        tblProduction.setId(IDGenerator.generate());
        tblProduction.setProductionDate(report.getReportDateValue());
        tblProduction.setStartDatetime(detail.getStartDatetimeValue());
        tblProduction.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblProduction.getStartDatetime()));
        tblProduction.setEndDatetime(detail.getEndDatetimeValue());
        tblProduction.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblProduction.getEndDatetime()));
//        try {
//            tblProduction.setProdDepartment(Integer.parseInt(loginUser.getDepartment()));
//        }
//        catch (NumberFormatException ne) {
//            tblProduction.setProdDepartment(0);
//        }
        tblProduction.setProducingTimeMinutes(detail.getDurationMinutes());
        tblProduction.setSuspendedTimeMinutes(0);
        tblProduction.setNetProducintTimeMinutes(detail.getDurationMinutes());
        tblProduction.setStatus(9); //常に生産終了
        tblProduction.setMachineUuid(tblReport.getMachineUuid());
        tblProduction.setShotCount(detail.getShotCount());
        tblProduction.setDisposedShotCount(detail.getDisposedShotCount());
        tblProduction.setCreateDate(new java.util.Date());
        tblProduction.setUpdateDate(tblProduction.getCreateDate());
        tblProduction.setCreateUserUuid(loginUser.getUserUuid());
        tblProduction.setUpdateUserUuid(loginUser.getUserUuid());
        if (detail.getLotNumber() != null && !detail.getLotNumber().equals("")) {
            tblProduction.setLotNumber(detail.getLotNumber());
        }
        //tblProduction.setTblProductionDetailCollection(new ArrayList<>());

        //機械日報明細オブジェクト作成
        TblMachineDailyReport2Detail tblReportDetail = new TblMachineDailyReport2Detail();
        tblReportDetail.setAdded(true);
        tblReportDetail.setStartDatetime(detail.getStartDatetimeValue());
        tblReportDetail.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblProduction.getStartDatetime()));
        tblReportDetail.setEndDatetime(detail.getEndDatetimeValue());
        tblReportDetail.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblProduction.getEndDatetime()));
        tblReportDetail.setDurationMinitues(detail.getDurationMinutes());
        tblReportDetail.setDetailType(TblMachineDailyReport2.DETAIL_TYPE_PROD);
        tblReportDetail.setOperatingFlg(1);
        tblReportDetail.setShotCount(detail.getShotCount());
        tblReportDetail.setDisposedShotCount(detail.getDisposedShotCount());
        tblReportDetail.setTblMachineDailyReport2ProdDetailList(new ArrayList<>());
        tblReportDetail.setProductionId(tblProduction.getId()); //機械日報明細に生産実績IDを保持

        if (detail.getWorkerId() != null && !detail.getWorkerId().equals("")) {
            MstUser worker = getMstUser(detail.getWorkerId(), response, "workerId");
            if (worker != null) {
                tblProduction.setPersonUuid(worker.getUuid());
                tblReportDetail.setWorkerUuid(worker.getUuid());
                try {
                    tblProduction.setProdDepartment(Integer.parseInt(worker.getDepartment()));
                }
                catch (NumberFormatException ne) {
                    tblProduction.setProdDepartment(0);
                }
            }
            else {
                return false;
            }
        }
        if (detail.getMoldId() != null && !detail.getMoldId().equals("")) {
            MstMold mold = getMstMold(detail.getMoldId(), response);
            if (mold != null) {
                tblProduction.setMoldUuid(mold.getUuid());
                tblReportDetail.setMoldUuid(mold.getUuid());
            }
            else {
                return false;
            }
        }
        if (detail.getWorkPhaseCode() != null && !detail.getWorkPhaseCode().equals("")) {
            MstWorkPhase mstWorkPhase = getMstWorkPhase(detail.getWorkPhaseCode(), response);
            if (mstWorkPhase != null) {
                tblProduction.setWorkPhaseId(mstWorkPhase.getId());
                tblReportDetail.setWorkPhaseId(mstWorkPhase.getId());
            }
            else {
                return false;
            }
        }
        
        int idx = 0;
        //生産実績明細作成
        for (BulkMDReportProdDetail prodDetail: detail.getProdDetails()) {
            if (!checkProdDetailFormat(prodDetail, response)) return false;
            TblProductionDetail tblProductionDetail = new TblProductionDetail();
            tblProductionDetail.setProductionId(tblProduction);
            tblProductionDetail.setId(IDGenerator.generate());
            tblProductionDetail.setCreateDate(tblProduction.getCreateDate());
            tblProductionDetail.setUpdateDate(tblProduction.getUpdateDate());
            tblProductionDetail.setCreateUserUuid(tblProduction.getCreateUserUuid());
            tblProductionDetail.setUpdateUserUuid(tblProduction.getUpdateUserUuid());
            //tblProduction.getTblProductionDetailCollection().add(tblProductionDetail);
            TblMachineDailyReport2ProdDetail tblReportProdDetail = new TblMachineDailyReport2ProdDetail();
            tblReportProdDetail.setAdded(true);
            tblReportProdDetail.setProductionDetailId(tblProductionDetail.getId()); //機械日報生産明細に生産実績明細IDを保持
            tblReportDetail.getTblMachineDailyReport2ProdDetailList().add(tblReportProdDetail);
            if (prodDetail.getPartCode() == null || prodDetail.getPartCode().equals("")) {
                //部品コードは必須
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "partCode");
                setErrorInfo(response, msg);
                return false;
            }
            else {
                MstComponent mstComponent = getMstComponent(prodDetail.getPartCode(), response);
                if (mstComponent == null) {
                    return false;
                }
                else {
                    tblProductionDetail.setComponentId(mstComponent.getId());
                    tblReportProdDetail.setComponentId(mstComponent.getId());
                }
                if (idx == 0) {
                    //最初の部品コードを機械日報明細に保持
                    tblReportDetail.setFirstComponentId(tblProductionDetail.getComponentId());
                }
            }
            if (prodDetail.getProcedureCode() == null || prodDetail.getProcedureCode().equals("")) {
                //部品工程番号が指定されていなければDBから取得
                //金型部品関係マスタから決まればそれを使用
                MstMoldComponentRelation rel = getMstMoldComponentRelation(tblReportDetail.getMoldUuid(), tblReportProdDetail.getComponentId());
                if (rel != null && rel.getMstProcedure() != null) {
                    tblProductionDetail.setProcedureId(rel.getMstProcedure().getId());
                    tblReportProdDetail.setProcedureId(rel.getMstProcedure().getId());
                }
                else {
                    //金型部品関係マスタから決まらなければ部品工程マスタから最初の部品工程を取得
                    MstProcedure mstProcedure = mstProcedureService.getFirstMstProcedure(tblReportProdDetail.getComponentId());
                    if (mstProcedure != null) {
                        tblProductionDetail.setProcedureId(mstProcedure.getId());
                        tblReportProdDetail.setProcedureId(mstProcedure.getId());
                    }
                    
                }
                if (tblProductionDetail.getProcedureId() == null) {
                    //部品工程が取得できなければエラー
                    setCodeMissingError(response, "procedureCode", "Not defined in the Procedure Master");
                    return false;
                }
            }
            else {
                //部品工程番号が指定されていればそれを使用
                MstProcedure mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(tblReportProdDetail.getComponentId(), prodDetail.getProcedureCode());
                if (mstProcedure != null) {
                    tblProductionDetail.setProcedureId(mstProcedure.getId());
                    tblReportProdDetail.setProcedureId(mstProcedure.getId());
                }
                else {
                    setCodeMissingError(response, "procedureCode", prodDetail.getProcedureCode());
                    return false;
                }
            }
            //取り数
            tblProductionDetail.setCountPerShot(prodDetail.getCountPerShot());
            tblReportProdDetail.setCountPerShot(prodDetail.getCountPerShot());
            //完成数。Jsonから送られて来たらそれを利用。送られてこなかったら計算
            int completeCount = prodDetail.getCompleteCount();
            if (completeCount == 0) { //Jsonから来ないとき計算
                completeCount = detail.getShotCount() * prodDetail.getCountPerShot() - prodDetail.getDefectCount(); //ショット数*取り数-不良数
            }
            tblProductionDetail.setCompleteCount(completeCount);
            tblProductionDetail.setPlanNotAppropriatedCount(completeCount);
            tblProductionDetail.setPlanAppropriatedCount(0);
            tblReportProdDetail.setCompleteCount(completeCount);
            //不良数
            tblProductionDetail.setDefectCount(prodDetail.getDefectCount());
            tblReportProdDetail.setDefectCount(prodDetail.getDefectCount());
            tblProductionDetailList.add(tblProductionDetail);
            //材料
            if (prodDetail.getMaterial01Code() != null && !prodDetail.getMaterial01Code().equals("")) {
                MstMaterial mstMaterial = getMstMaterial(prodDetail.getMaterial01Code(), response, "material01Code");
                if (mstMaterial == null) {
                    return false;
                }
                tblProductionDetail.setMaterial01Id(mstMaterial.getId());
                tblProductionDetail.setMaterial01Amount(prodDetail.getMaterial01Amount());
                tblProductionDetail.setMaterial01LotNo(prodDetail.getMaterial01LotNo());
                tblProductionDetail.setMaterial01PurgedAmount(prodDetail.getMaterial01PurgedAmount());
                tblReportProdDetail.setMaterial01Id(mstMaterial.getId());
                tblReportProdDetail.setMaterial01Amount(prodDetail.getMaterial01Amount());
                tblReportProdDetail.setMaterial01LotNo(prodDetail.getMaterial01LotNo());
                tblReportProdDetail.setMaterial01PurgedAmount(prodDetail.getMaterial01PurgedAmount());
            }
            if (prodDetail.getMaterial02Code() != null && !prodDetail.getMaterial02Code().equals("")) {
                MstMaterial mstMaterial = getMstMaterial(prodDetail.getMaterial02Code(), response, "material02Code");
                if (mstMaterial == null) {
                    return false;
                }
                tblProductionDetail.setMaterial02Id(mstMaterial.getId());
                tblProductionDetail.setMaterial02Amount(prodDetail.getMaterial02Amount());
                tblProductionDetail.setMaterial02LotNo(prodDetail.getMaterial02LotNo());
                tblProductionDetail.setMaterial02PurgedAmount(prodDetail.getMaterial02PurgedAmount());
                tblReportProdDetail.setMaterial02Id(mstMaterial.getId());
                tblReportProdDetail.setMaterial02Amount(prodDetail.getMaterial02Amount());
                tblReportProdDetail.setMaterial02LotNo(prodDetail.getMaterial02LotNo());
                tblReportProdDetail.setMaterial02PurgedAmount(prodDetail.getMaterial02PurgedAmount());
            }
            if (prodDetail.getMaterial03Code() != null && !prodDetail.getMaterial03Code().equals("")) {
                MstMaterial mstMaterial = getMstMaterial(prodDetail.getMaterial03Code(), response, "material03Code");
                if (mstMaterial == null) {
                    return false;
                }
                tblProductionDetail.setMaterial03Id(mstMaterial.getId());
                tblProductionDetail.setMaterial03Amount(prodDetail.getMaterial03Amount());
                tblProductionDetail.setMaterial03LotNo(prodDetail.getMaterial03LotNo());
                tblProductionDetail.setMaterial03PurgedAmount(prodDetail.getMaterial03PurgedAmount());
                tblReportProdDetail.setMaterial03Id(mstMaterial.getId());
                tblReportProdDetail.setMaterial03Amount(prodDetail.getMaterial03Amount());
                tblReportProdDetail.setMaterial03LotNo(prodDetail.getMaterial03LotNo());
                tblReportProdDetail.setMaterial03PurgedAmount(prodDetail.getMaterial03PurgedAmount());
            }
            idx++;
        }
        tblReport.getTblMachineDailyReport2DetailList().add(tblReportDetail);
        tblProductionList.add(tblProduction);
        return true;
    }
    
    /**
     * 設備停止時間レコード作成
     * @param report
     * @param detail
     * @param tblReport
     * @param response
     * @return 
     */
    private boolean createDowntime(BulkMDReportDetail detail, TblMachineDailyReport2 tblReport, BasicResponse response) {
        MstMachineDowntime mstMachineDowntime = getMstMachineDowntime(detail.getMachineDowntimeCode(), response);
        if (mstMachineDowntime == null) return false;
        //機械日報明細オブジェクト作成
        TblMachineDailyReport2Detail tblReportDetail = new TblMachineDailyReport2Detail();
        tblReport.getTblMachineDailyReport2DetailList().add(tblReportDetail);
        tblReportDetail.setAdded(true);
        tblReportDetail.setDetailType(TblMachineDailyReport2.DETAIL_TYPE_DOWNTIME);
        tblReportDetail.setMachineDowntimeId(mstMachineDowntime.getId());
        tblReportDetail.setDowntimeComment(detail.getDowntimeComment());
        tblReportDetail.setStartDatetime(detail.getStartDatetimeValue());
        tblReportDetail.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), detail.getStartDatetimeValue()));
        tblReportDetail.setEndDatetime(detail.getEndDatetimeValue());
        tblReportDetail.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), detail.getEndDatetimeValue()));
        tblReportDetail.setDurationMinitues(detail.getDurationMinutes());
        tblReportDetail.setOperatingFlg(0);
        return true;
    }
    
    /**
     * データベースへ追加
     */
    //@Transactional
    private void insertWorkProduction() {
        for (TblWork tblWork: tblWorkList) {
            entityManager.persist(tblWork);
        }
        for (TblProduction tblProduction: tblProductionList) {
            if (tblProduction.getLotNumber() == null) { //クライアントから渡されているときは採番しないでそれを使う
                //ロット番号採番(行ロックかかる)
                String lotNo = tblProductionLotBalanceService.makeNewLotNumber(null);
                tblProduction.setLotNumber(lotNo);
            }
            entityManager.persist(tblProduction);
        }
        for (TblProductionDetail tblProductionDetail: tblProductionDetailList) {
            entityManager.persist(tblProductionDetail);
        }
    }
    
    /**
     * コードが見つからないエラーをレスポンスオブジェクトにセット
     * @param response
     * @param elementName
     * @param codeValue 
     */
    private void setCodeMissingError(BasicResponse response, String elementName, String codeValue) {
        StringBuilder msg = new StringBuilder();
        msg.append(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
        msg.append("(");
        msg.append(elementName);
        msg.append(" : ");
        msg.append(codeValue);
        msg.append(")");
        setErrorInfo(response, msg.toString());
    }
    
    /**
     * レスポンスオブジェクトにエラー情報をセット
     * @param response
     * @param errorMessage 
     */
    private void setErrorInfo(BasicResponse response, String errorMessage) {
        response.setError(true);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(errorMessage);
    }

    /**
     * 機械日報削除
     * @param reportDate
     * @param machineId
     * @param loginUser
     * @return 
     */
    @Transactional(rollbackOn = Exception.class)
    public BasicResponse deleteMachineDailyReport(String reportDate, String machineId, LoginUser loginUser) throws Exception {
        BasicResponse response = new BasicResponse();
        this.loginUser = loginUser;
        Date pReportDate = DateFormat.hyphenStrToDate(reportDate);
        String msg = null;
        if (reportDate == null || reportDate.equals("")) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "reportDate");
        }
        else if (pReportDate == null) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid") + ":" + reportDate;
        }
        else if (machineId == null || machineId.equals("")) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item").replace("%s", "machineId");
        }
        if (msg != null) {
            setErrorInfo(response, msg);
            return response;
        }
        MstMachine mstMachine = getMstMachine(machineId, response);
        if (mstMachine == null) {
            return response;
        }
        TblMachineDailyReport2 report = machineDailyReport2Service.getMachineDailyReport2ByUniqueKey(pReportDate, mstMachine.getUuid());
        if (report == null) {
            msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted");
            setErrorInfo(response, msg);
            return response;
        }
        report.setDeleted(true);
        //関連する作業、生産を削除するためIDをリストに保持しておく
        List<String> workIdList = new ArrayList<>();
        List<String> productionIdList = new ArrayList<>();
        for (TblMachineDailyReport2Detail detail: report.getTblMachineDailyReport2DetailList()) {
            if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_WORK) {
                workIdList.add(detail.getWorkId());
            }
            else if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD) {
                productionIdList.add(detail.getProductionId());
            }
        }
        //サービスクラスに渡して機械日報レコードを削除
        machineDailyReport2Service.updateMachienDailyReport2(report, loginUser);
        //作業テーブル削除
        for (String id: workIdList) {
            tblWorkService.deleteOnlyTblWork(id);
        }
        //生産テーブル削除
        for (String id: productionIdList) {
            tblProductionService.deleteTblProduction(id, loginUser);
        }
        return response;
    }
    
}
