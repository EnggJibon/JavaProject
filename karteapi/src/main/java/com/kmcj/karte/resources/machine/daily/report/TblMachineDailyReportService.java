/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.daily.report;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.lot.TblComponentLot;
import com.kmcj.karte.resources.component.lot.TblComponentLotService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelation;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVo;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.component.material.MstComponentMaterial;
import com.kmcj.karte.resources.component.material.MstComponentMaterialService;
import com.kmcj.karte.resources.component.structure.MstComponentStructure;
import com.kmcj.karte.resources.component.structure.MstComponentStructureVoList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetail;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetailList;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetailPK;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetailVo;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForDay;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForMonth;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForWeek;
import com.kmcj.karte.resources.machine.production.TblMachineProductionPeriodService;
import com.kmcj.karte.resources.material.stock.TblMaterialStockService;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForDay;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForMonth;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForWeek;
import com.kmcj.karte.resources.mold.production.TblMoldProductionPeriodService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.TblProductionService;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.production.detail.TblProductionDetailService;
import com.kmcj.karte.resources.production.suspension.TblProductionSuspensionList;
import com.kmcj.karte.resources.production.suspension.TblProductionSuspensionVo;
import com.kmcj.karte.resources.production.suspension.TblProductionSuspensionlService;
import com.kmcj.karte.resources.stock.TblStockService;
import com.kmcj.karte.resources.work.TblWorkResource;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import com.kmcj.karte.util.TimezoneConverter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 *
 * @author
 */
@Dependent
public class TblMachineDailyReportService {

    private Logger logger = Logger.getLogger(TblMachineDailyReportService.class.getName());

    private static final String COUNT_STR = "count";
    
    private static final String CONST_SECOND = ":00";

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstComponentMaterialService mstComponentMaterialService;

    @Inject
    private TblProductionSuspensionlService tblProductionSuspensionlService;

    @Inject
    private TblProductionService tblProductionService;
    
    @Inject
    private TblProductionDetailService tblProductionDetailService;
    
    @Inject
    private CnfSystemService cnfSystemService;
    
    @Inject
    private TblMoldProductionPeriodService tblMoldProductionPeriodService;
    
    @Inject
    private TblMachineProductionPeriodService tblMachineProductionPeriodService;
    
    @Inject
    private MstMoldService mstMoldService;
    
    @Inject
    private MstMachineService mstMachineService;
    
    @Inject
    private TblStockService tblStockService;
    
    @Inject
    private MstProcedureService mstProcedureService;
    
    @Inject
    private TblMaterialStockService tblMaterialStockService;
    
    @Inject
    private TblComponentLotService tblComponentLotService;
    
    @Inject
    private TblComponentLotRelationService tblComponentLotRelationService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("productionDate", " ORDER BY mdr.tblMachineDailyReportPK.productionDate ");// 生産日
        orderKey.put("machineName", " ORDER BY machine.machineName ");// 設備名称
        orderKey.put("issueReportPersonName", " ORDER BY mu.userName ");// 報告者
        orderKey.put("startDatetime", " ORDER BY mdr.startDatetime ");// 開始時刻
        orderKey.put("endDatetime", " ORDER BY mdr.endDatetime ");// 終了時刻
        orderKey.put("netProducintTimeMinutes", " ORDER BY mdr.netProducintTimeMinutes ");// 実稼動時間
        orderKey.put("suspendedTimeMinutes", " ORDER BY mdr.suspendedTimeMinutes ");// 中断時間
        orderKey.put("shotCount", " ORDER BY mdr.shotCount ");// ショット数
        orderKey.put("componentCode", " ORDER BY mc.componentCode ");// 部品コード
        orderKey.put("componentName", " ORDER BY mc.componentName ");// 部品名称
        orderKey.put("procedureCode", " ORDER BY mp.procedureCode ");// 部品工程番号
        orderKey.put("procedureName", " ORDER BY mp.procedureName ");// 部品工程名称
        orderKey.put("completeCount", " ORDER BY dailyReportDetail.completeCount ");// 完成数
        orderKey.put("componentNetProducintTimeMinutes", " ORDER BY dailyReportDetail.componentNetProducintTimeMinutes ");// 部品別実稼動時間
        orderKey.put("workPhaseCode", " ORDER BY mwp.workPhaseCode ");// 工程コード
        orderKey.put("workPhaseName", " ORDER BY mwp.workPhaseName ");// 工程名称
        orderKey.put("machineCode", " ORDER BY machine.machineCd ");// 設備コード
        orderKey.put("strageLocationCd", " ORDER BY machine.strageLocationCd ");// 設備工程コード
        orderKey.put("operatorCd", " ORDER BY machine.operatorCd ");// 日報担当者コード
        orderKey.put("chargeCd", " ORDER BY machine.chargeCd ");// チャージコード

    }

    /**
     * 機械日報一覧内容取得
     *
     * @param componentCode
     * @param formatProductionDateFrom
     * @param formatProductionDateTo
     * @param reporterUser
     * @param machineId
     * @param department
     * @return
     */
    public TblMachineDailyReportDetailList getMachineDailyReportDetailsByCondition(String componentCode, Date formatProductionDateFrom, Date formatProductionDateTo, String reporterUser, String machineId, Integer department) {
        // 一覧データ取得
        List list = getMacDailyReportList(
                componentCode, formatProductionDateFrom, formatProductionDateTo,
                reporterUser, machineId, department, null);
        TblMachineDailyReportDetailList response = new TblMachineDailyReportDetailList();
        response.setTblMachineDailyReportDetails(list);
        return response;
    }

    /**
     * 機械日報一覧件数取得
     *
     * @param componentCode
     * @param formatProductionDateFrom
     * @param formatProductionDateTo
     * @param reporterUser
     * @param machineId
     * @param department
     * @return
     */
    public CountResponse getMachineDailyReportCountByCondition(String componentCode, Date formatProductionDateFrom, Date formatProductionDateTo, String reporterUser, String machineId, int department) {
        // 一覧件数取得
        List list = getMacDailyReportList(
                componentCode, formatProductionDateFrom, formatProductionDateTo,
                reporterUser, machineId, department, COUNT_STR);
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    /**
     * 機械日報CSV出力
     *
     * @param tblMachineDailyReportDetailList
     * @param loginUser
     * @return
     */
    public FileReponse getDailyReportCSV(TblMachineDailyReportDetailList tblMachineDailyReportDetailList, LoginUser loginUser) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*
         * Header用意
         */
        String langId = loginUser.getLangId();
        List<String> dictKeyList = Arrays.asList("production_date", "machine_name", "issue_report_person_name", "work_start_time", "work_end_time", "work_actual_time_minutes",
                "suspended_time_minutes", "shot_count", "component_code", "component_name", "procedure_code", "procedure_name", "complete_count", "component_net_producing_time_minutes",
                "work_phase_code", "work_phase_name", "machine_code", "strage_location_cd", "operator_cd", "charge_cd", "machine_daily_report_list");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        FileReponse fr = new FileReponse();

        /*
         * Header設定
         */
        ArrayList csvOutHeadList = new ArrayList();
        csvOutHeadList.add(headerMap.get("production_date"));
        csvOutHeadList.add(headerMap.get("machine_name"));
        csvOutHeadList.add(headerMap.get("issue_report_person_name"));
        csvOutHeadList.add(headerMap.get("work_start_time"));
        csvOutHeadList.add(headerMap.get("work_end_time"));
        csvOutHeadList.add(headerMap.get("work_actual_time_minutes"));
        csvOutHeadList.add(headerMap.get("suspended_time_minutes"));
        csvOutHeadList.add(headerMap.get("shot_count"));
        csvOutHeadList.add(headerMap.get("component_code"));
        csvOutHeadList.add(headerMap.get("component_name"));
        csvOutHeadList.add(headerMap.get("procedure_code"));
        csvOutHeadList.add(headerMap.get("procedure_name"));
        csvOutHeadList.add(headerMap.get("complete_count"));
        csvOutHeadList.add(headerMap.get("component_net_producing_time_minutes"));
        csvOutHeadList.add(headerMap.get("work_phase_code"));
        csvOutHeadList.add(headerMap.get("work_phase_name"));
        csvOutHeadList.add(headerMap.get("machine_code"));
        csvOutHeadList.add(headerMap.get("strage_location_cd"));
        csvOutHeadList.add(headerMap.get("operator_cd"));
        csvOutHeadList.add(headerMap.get("charge_cd"));

        // 出力データ準備
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(csvOutHeadList);

        // 行用
        ArrayList<String> line;

        // 日付変換用
        SimpleDateFormat sdfDate = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        List<TblMachineDailyReportDetail> tblMachineDailyReportDetails = tblMachineDailyReportDetailList.getTblMachineDailyReportDetails();
        if (tblMachineDailyReportDetails != null && !tblMachineDailyReportDetails.isEmpty()) {
            for (TblMachineDailyReportDetail tblMachineDailyReportDetail : tblMachineDailyReportDetails) {
                line = new ArrayList<>();
                // 生産日
                if (tblMachineDailyReportDetail.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate() != null) {
                    String outputProductionDate = sdfDate.format(tblMachineDailyReportDetail.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate());
                    line.add(outputProductionDate);
                } else {
                    line.add("");
                }
                // 設備名称
                if (tblMachineDailyReportDetail.getTblMachineDailyReport().getMstMachine() != null) {
                    line.add(tblMachineDailyReportDetail.getTblMachineDailyReport().getMstMachine().getMachineName());
                } else {
                    line.add("");
                }
                // 報告者
                if (tblMachineDailyReportDetail.getTblMachineDailyReport().getMstUser() != null) {
                    line.add(tblMachineDailyReportDetail.getTblMachineDailyReport().getMstUser().getUserName());
                } else {
                    line.add("");
                }
                // 開始時刻
                if (tblMachineDailyReportDetail.getTblMachineDailyReport().getStartDatetime() != null) {
                    String outputStartDatetime = FileUtil.getDateTimeFormatMMDDHHMMStr(tblMachineDailyReportDetail.getTblMachineDailyReport().getStartDatetime());
                    line.add(outputStartDatetime);
                } else {
                    line.add("");
                }
                // 終了時刻
                if (tblMachineDailyReportDetail.getTblMachineDailyReport().getEndDatetime() != null) {
                    String outputEndDatetime = FileUtil.getDateTimeFormatMMDDHHMMStr(tblMachineDailyReportDetail.getTblMachineDailyReport().getEndDatetime());
                    line.add(outputEndDatetime);
                } else {
                    line.add("");
                }
                // 実稼働時間
                if (tblMachineDailyReportDetail.getTblMachineDailyReport().getNetProducintTimeMinutes() != null) {
                    line.add(Integer.toString(tblMachineDailyReportDetail.getTblMachineDailyReport().getNetProducintTimeMinutes()));
                } else {
                    line.add("");
                }
                // 中断時間
                if (tblMachineDailyReportDetail.getTblMachineDailyReport().getSuspendedTimeMinutes() != null) {
                    line.add(Integer.toString(tblMachineDailyReportDetail.getTblMachineDailyReport().getSuspendedTimeMinutes()));
                } else {
                    line.add("");
                }
                // ショット数
                line.add(Integer.toString(tblMachineDailyReportDetail.getTblMachineDailyReport().getShotCount()));
                // 部品コード
                line.add(tblMachineDailyReportDetail.getMstComponent().getComponentCode());
                // 部品名称
                line.add(tblMachineDailyReportDetail.getMstComponent().getComponentName());
                if (tblMachineDailyReportDetail.getMstProcedure() != null) {
                    // 部品工程番号
                    line.add(tblMachineDailyReportDetail.getMstProcedure().getProcedureCode());
                    // 部品工程名称
                    line.add(tblMachineDailyReportDetail.getMstProcedure().getProcedureName());
                } else {
                    line.add("");
                    line.add("");
                }
                // 完成数
                line.add(Integer.toString(tblMachineDailyReportDetail.getCompleteCount()));
                // 部品別実稼働時間
                if (tblMachineDailyReportDetail.getComponentNetProducintTimeMinutes() != null) {
                    line.add(Integer.toString(tblMachineDailyReportDetail.getComponentNetProducintTimeMinutes()));
                } else {
                    line.add("");
                }
                // 工程コード(生産事績の作業工程ID)
                // 工程名称
                if (tblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMstWorkPhase() != null) {
                    line.add(tblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMstWorkPhase().getWorkPhaseCode());
                    line.add(tblMachineDailyReportDetail.getTblMachineDailyReport().getTblProduction().getMstWorkPhase().getWorkPhaseName());
                } else {
                    line.add("");
                    line.add("");
                }
                if (tblMachineDailyReportDetail.getTblMachineDailyReport().getMstMachine() != null) {
                    // 設備コード
                    line.add(tblMachineDailyReportDetail.getTblMachineDailyReport().getMstMachine().getMachineCd());
                    // 設備工程コード
                    line.add(tblMachineDailyReportDetail.getTblMachineDailyReport().getMstMachine().getStrageLocationCd());
                    // 日報担当者コード
                    line.add(tblMachineDailyReportDetail.getTblMachineDailyReport().getMstMachine().getOperatorCd());
                    // チャージコード
                    line.add(tblMachineDailyReportDetail.getTblMachineDailyReport().getMstMachine().getChargeCd());
                }
                // 行を明細リストに追加
                gLineList.add(line);
            }
        }

        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        FileUtil fu = new FileUtil();

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MACHINE_DAILY_REPORT);
        tblCsvExport.setExportDate(new java.util.Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE_DAILY_REPORT);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        tblCsvExport.setClientFileName(fu.getCsvFileName(headerMap.get("machine_daily_report_list")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * 機械日報テーブル削除データチェック
     *
     * @param response
     * @param id
     * @param loginUser
     */
    public void checkDeleteData(BasicResponse response, String id, LoginUser loginUser) {
        // 機械日報テーブル自体の存在チェック
        TblMachineDailyReport deleteDailyReport = getDailyReportSingleById(id);
        if (deleteDailyReport != null) {
            // 生産実績がすでに完了している機械日報を削除できない、制御
            if (null != deleteDailyReport.getTblProduction().getStatus() && deleteDailyReport.getTblProduction().getStatus() == 9) {
                setApplicationError(response, loginUser, "msg_error_not_delete_producint_completed", "TblMachineDailyReport");
            }
        } else {
            setApplicationError(response, loginUser, "msg_error_data_deleted", "TblMachineDailyReport");
        }

    }

    /**
     * 機械日報1件取得(TblMachineDailyReport型で返却)
     *
     * @param id
     * @return
     */
    public TblMachineDailyReport getDailyReportSingleById(String id) {
        Query query = entityManager.createNamedQuery("TblMachineDailyReport.findById");
        query.setParameter("id", id);
        try {
            return (TblMachineDailyReport) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 機械日報削除
     *
     * @param id
     * @param loginUser
     */
    @Transactional
    public void deleteMachineDailyReport(String id, LoginUser loginUser) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        // 機械日報テーブル削除
        Query query = entityManager.createNamedQuery("TblMachineDailyReport.delete");
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    /**
     * 機械日報完成数更新する時、日/週/月別完成数集計テーブルに完成数を更新する
     * 
     * @param tblMachineDailyReport
     * @param tblMachineDailyReportDetail
     * @param completedCount
     * @param loginUser 
     */
    public void calculateProductionPeriodForUpdate(TblMachineDailyReport tblMachineDailyReport, 
        TblMachineDailyReportDetail tblMachineDailyReportDetail, int completedCount, LoginUser loginUser) {
        // 機械日報明細情報取得
        if (tblMachineDailyReport != null) {
            String moldUuid = tblMachineDailyReport.getTblProduction().getMoldUuid();
            String machineUuid = tblMachineDailyReport.getMachineUuid();
            Date productionDate = tblMachineDailyReport.getTblMachineDailyReportPK().getProductionDate();
            // 機械日報明細情報がある場合
            if (tblMachineDailyReportDetail != null) {
                // 生産日により週開始日取得
                Date monday = DateFormat.strToDate(DateFormat.getWeekMonday(productionDate));
                // 生産日により週終了日取得
                Date sunday = DateFormat.strToDate(DateFormat.getWeekSunday(productionDate));
                // 生産日により月(yyyy/MM)取得
                String productionMonth = DateFormat.getFirstDay(productionDate) == null ? "" : DateFormat.getFirstDay(productionDate).substring(0, 7);
                // 該当機械日報に対する生産実績に金型を指定している場合、金型日/週/月別完成数集計テーブルに完成数をマイナスする
                if (moldUuid != null && !"".equals(moldUuid)) {
                    // 日別完成数集計テーブルに完成数をマイナスする
                    TblMoldProductionForDay tblMoldProductionForDay = tblMoldProductionPeriodService.getProductionForDaySingleByPK(moldUuid, tblMachineDailyReportDetail.getComponentId(), productionDate);
                    if (tblMoldProductionForDay != null) {
                        tblMoldProductionPeriodService.minusCompletedCount(tblMoldProductionForDay, completedCount, loginUser);
                    }
                    // 週別完成数集計テーブルに完成数をマイナスする
                    TblMoldProductionForWeek tblMoldProductionForWeek = tblMoldProductionPeriodService.getProductionForWeekSingleByPK(moldUuid, tblMachineDailyReportDetail.getComponentId(), monday, sunday);
                    if (tblMoldProductionForWeek != null) {
                        tblMoldProductionPeriodService.minusCompletedCount(tblMoldProductionForWeek, completedCount, loginUser);
                    }
                    // 月別完成数集計テーブルに完成数をマイナスする
                    TblMoldProductionForMonth tblMoldProductionForMonth = tblMoldProductionPeriodService.getProductionForMonthSingleByPK(moldUuid, tblMachineDailyReportDetail.getComponentId(), productionMonth);
                    if (tblMoldProductionForMonth != null) {
                        tblMoldProductionPeriodService.minusCompletedCount(tblMoldProductionForMonth, completedCount, loginUser);
                    }
                }
                // 設備日/週/月別完成数集計テーブルに完成数をマイナスする
                // 日別完成数集計テーブルに完成数をマイナスする
                TblMachineProductionForDay tblMachineProductionForDay = tblMachineProductionPeriodService.getProductionForDaySingleByPK(machineUuid, tblMachineDailyReportDetail.getComponentId(), moldUuid, productionDate);
                if (tblMachineProductionForDay != null) {
                    tblMachineProductionPeriodService.minusCompletedCount(tblMachineProductionForDay, completedCount, loginUser);
                }
                // 週別完成数集計テーブルに完成数をマイナスする
                TblMachineProductionForWeek tblMachineProductionForWeek = tblMachineProductionPeriodService.getProductionForWeekSingleByPK(machineUuid, tblMachineDailyReportDetail.getComponentId(), moldUuid, monday, sunday);
                if (tblMachineProductionForWeek != null) {
                    tblMachineProductionPeriodService.minusCompletedCount(tblMachineProductionForWeek, completedCount, loginUser);
                }
                // 月別完成数集計テーブルに完成数をマイナスする
                TblMachineProductionForMonth tblMachineProductionForMonth = tblMachineProductionPeriodService.getProductionForMonthSingleByPK(machineUuid, tblMachineDailyReportDetail.getComponentId(), moldUuid, productionMonth);
                if (tblMachineProductionForMonth != null) {
                    tblMachineProductionPeriodService.minusCompletedCount(tblMachineProductionForMonth, completedCount, loginUser);
                }
            }
        }
    }
    
    /**
     * 機械日報削除する時、日/週/月別完成数集計テーブルに完成数をマイナスする
     * 
     * @param id 機械日報ID
     * @param loginUser 
     */
    @Transactional
    public void calculateProductionPeriodForDelete(String id, LoginUser loginUser) {
        // 機械日報明細情報取得
        TblMachineDailyReportVo tblMachineDailyReportVo = getDailyReportDetails(id, null, loginUser);
        if (tblMachineDailyReportVo.getMachineDailyReportVos() != null && !tblMachineDailyReportVo.getMachineDailyReportVos().isEmpty()) {
            String moldUuid = tblMachineDailyReportVo.getMachineDailyReportVos().get(0).getTblProduction().getMoldUuid();
            String machineUuid = tblMachineDailyReportVo.getMachineDailyReportVos().get(0).getMachineUuid();
            Date productionDate = tblMachineDailyReportVo.getMachineDailyReportVos().get(0).getProductionDate();
            // 機械日報明細情報がある場合
            if (tblMachineDailyReportVo.getMachineDailyReportVos().get(0).getMachineDailyReportDetailVos() != null && 
                !tblMachineDailyReportVo.getMachineDailyReportVos().get(0).getMachineDailyReportDetailVos().isEmpty()) {
                // 生産日により週開始日取得
                Date monday = DateFormat.strToDate(DateFormat.getWeekMonday(productionDate));
                // 生産日により週終了日取得
                Date sunday = DateFormat.strToDate(DateFormat.getWeekSunday(productionDate));
                // 生産日により月(yyyy/MM)取得
                String productionMonth = DateFormat.getFirstDay(productionDate) == null ? "" : DateFormat.getFirstDay(productionDate).substring(0, 7);
                for (TblMachineDailyReportDetailVo detailVo : tblMachineDailyReportVo.getMachineDailyReportVos().get(0).getMachineDailyReportDetailVos()) {
                    // 機械日報詳細完成数
                    long completedCount = detailVo.getCompleteCount() == null ? 0L : Long.valueOf(detailVo.getCompleteCount());
                    // 該当機械日報に対する生産実績に金型を指定している場合、金型日/週/月別完成数集計テーブルに完成数をマイナスする
                    if (moldUuid != null && !"".equals(moldUuid)) {
                        // 日別完成数集計テーブルに完成数をマイナスする
                        TblMoldProductionForDay tblMoldProductionForDay = tblMoldProductionPeriodService.getProductionForDaySingleByPK(moldUuid, detailVo.getComponentId(), productionDate);
                        if (tblMoldProductionForDay != null) {
                            tblMoldProductionPeriodService.minusCompletedCount(tblMoldProductionForDay, completedCount, loginUser);
                        }
                        // 週別完成数集計テーブルに完成数をマイナスする
                        TblMoldProductionForWeek tblMoldProductionForWeek = tblMoldProductionPeriodService.getProductionForWeekSingleByPK(moldUuid, detailVo.getComponentId(), monday, sunday);
                        if (tblMoldProductionForWeek != null) {
                            tblMoldProductionPeriodService.minusCompletedCount(tblMoldProductionForWeek, completedCount, loginUser);
                        }
                        // 月別完成数集計テーブルに完成数をマイナスする
                        TblMoldProductionForMonth tblMoldProductionForMonth = tblMoldProductionPeriodService.getProductionForMonthSingleByPK(moldUuid, detailVo.getComponentId(), productionMonth);
                        if (tblMoldProductionForMonth != null) {
                            tblMoldProductionPeriodService.minusCompletedCount(tblMoldProductionForMonth, completedCount, loginUser);
                        }
                    }
                    // 設備日/週/月別完成数集計テーブルに完成数をマイナスする
                    // 日別完成数集計テーブルに完成数をマイナスする
                    TblMachineProductionForDay tblMachineProductionForDay = tblMachineProductionPeriodService.getProductionForDaySingleByPK(machineUuid, detailVo.getComponentId(), moldUuid, productionDate);
                    if (tblMachineProductionForDay != null) {
                        tblMachineProductionPeriodService.minusCompletedCount(tblMachineProductionForDay, completedCount, loginUser);
                    }
                    // 週別完成数集計テーブルに完成数をマイナスする
                    TblMachineProductionForWeek tblMachineProductionForWeek = tblMachineProductionPeriodService.getProductionForWeekSingleByPK(machineUuid, detailVo.getComponentId(), moldUuid, monday, sunday);
                    if (tblMachineProductionForWeek != null) {
                        tblMachineProductionPeriodService.minusCompletedCount(tblMachineProductionForWeek, completedCount, loginUser);
                    }
                    // 月別完成数集計テーブルに完成数をマイナスする
                    TblMachineProductionForMonth tblMachineProductionForMonth = tblMachineProductionPeriodService.getProductionForMonthSingleByPK(machineUuid, detailVo.getComponentId(), moldUuid, productionMonth);
                    if (tblMachineProductionForMonth != null) {
                        tblMachineProductionPeriodService.minusCompletedCount(tblMachineProductionForMonth, completedCount, loginUser);
                    }
                }
            }
        }
    }
    
    /**
     * 機械日報削除する時、生産実績情報を更新する
     * 
     * @param tblMachineDailyReportDetailList
     * @param productionId
     * @param response
     * @param loginUser 
     */
    public void calculateProductionForDelete(TblMachineDailyReportDetailList tblMachineDailyReportDetailList, String productionId, BasicResponse response, LoginUser loginUser) {
        // 生産実績取得
        List<TblProductionDetail> list = tblProductionDetailService.getProductionDetailsByProductionId(productionId);
        if (list == null || list.isEmpty()) {
            setApplicationError(response, loginUser, "msg_error_data_deleted", "TblProduction");
            return;
        }
        TblProduction tblProduction = list.get(0).getTblProduction();
        if (tblMachineDailyReportDetailList.getTblMachineDailyReportDetails() != null && !tblMachineDailyReportDetailList.getTblMachineDailyReportDetails().isEmpty()) {
            int shotCount = 0, disposedShotCount = 0;
//                    producingTimeMinutes = 0, suspendedTimeMinutes = 0;
            Date oldProductionDate = DateFormat.strToDate("1900/01/01");
            for (TblMachineDailyReportDetail detail : tblMachineDailyReportDetailList.getTblMachineDailyReportDetails()) {
                if (detail.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate().compareTo(oldProductionDate) != 0) {
                    shotCount += detail.getTblMachineDailyReport().getShotCount();
                    disposedShotCount += detail.getTblMachineDailyReport().getDisposedShotCount();
//                    producingTimeMinutes += (FileUtil.getIntegerValue(detail.getTblMachineDailyReport().getProducingTimeMinutes()));
//                    suspendedTimeMinutes += (FileUtil.getIntegerValue(detail.getTblMachineDailyReport().getSuspendedTimeMinutes()));
                    oldProductionDate = detail.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate();
                }
            }
//            int netProducintTimeMinutes = producingTimeMinutes - suspendedTimeMinutes;
            // ショット数
            tblProduction.setShotCount(shotCount);
            // 捨てショット数
            tblProduction.setDisposedShotCount(disposedShotCount);
//            // 生産時間(分)
//            tblProduction.setProducingTimeMinutes(producingTimeMinutes);
//            // 中断時間(分)
//            tblProduction.setSuspendedTimeMinutes(suspendedTimeMinutes);
//            // 実稼働時間(分)
//            tblProduction.setNetProducintTimeMinutes(netProducintTimeMinutes);
        } else {
            // ショット数
            tblProduction.setShotCount(0);
            // 捨てショット数
            tblProduction.setDisposedShotCount(0);
//            // 生産時間(分)
//            tblProduction.setProducingTimeMinutes(0);
//            // 中断時間(分)
//            tblProduction.setSuspendedTimeMinutes(0);
//            // 実稼働時間(分)
//            tblProduction.setNetProducintTimeMinutes(0);
        }
        tblProductionService.updateTblProduction(tblProduction, loginUser);
        
        // 生産実績明細
        int countPerShot = 0, defectCount = 0, completeCount = 0;
        for (TblProductionDetail productionDetail : list) {
            if (tblMachineDailyReportDetailList.getTblMachineDailyReportDetails() != null && !tblMachineDailyReportDetailList.getTblMachineDailyReportDetails().isEmpty()) {
                for (TblMachineDailyReportDetail detail : tblMachineDailyReportDetailList.getTblMachineDailyReportDetails()) {
                    if (productionDetail.getComponentId().equals(detail.getComponentId())) {
                        countPerShot += detail.getCountPerShot();
                        defectCount += detail.getDefectCount();
                        completeCount += detail.getCompleteCount();
                    }
                }
                // 取り数
                productionDetail.setCountPerShot(countPerShot);
                // 不良数
                productionDetail.setDefectCount(defectCount);
                // 完成数
                productionDetail.setCompleteCount(completeCount);
            } else {
                // 取り数
                productionDetail.setCountPerShot(0);
                // 不良数
                productionDetail.setDefectCount(0);
                // 完成数
                productionDetail.setCompleteCount(0);
            }
            tblProductionDetailService.updateTblProductionDetail(productionDetail, loginUser);
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

    /**
     * @param productionId
     * @param loginUser
     * @return
     */
    public TblMachineDailyReportVo getDailyReports(String productionId, LoginUser loginUser) {
        TblMachineDailyReportVo dailyReportVo = new TblMachineDailyReportVo();
        FileUtil fu = new FileUtil();
        if (null != productionId && !productionId.isEmpty()) {
            TblProduction tblProduction = entityManager.find(TblProduction.class, productionId);
            if (null == tblProduction) {
                dailyReportVo.setError(true);
                dailyReportVo.setErrorCode(ErrorMessages.E201_APPLICATION);
                dailyReportVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
                return dailyReportVo;
            } 
        }

        List<TblMachineDailyReportVo> response = new ArrayList<>();

        StringBuilder dailyReportSql = new StringBuilder("SELECT Tdr FROM TblMachineDailyReport Tdr JOIN FETCH Tdr.mstUser u WHERE 1=1 ");
        if (productionId != null && !productionId.isEmpty()) {
            dailyReportSql.append(" and Tdr.tblMachineDailyReportPK.productionId = :productionId ");
        }
        Query dailyReportQuery = entityManager.createQuery(dailyReportSql.toString());
        if (productionId != null && !productionId.isEmpty()) {
            dailyReportQuery.setParameter("productionId", productionId);
        }

        List<TblMachineDailyReport> reports = dailyReportQuery.getResultList();
        for (TblMachineDailyReport report : reports) {
            TblMachineDailyReportVo aVo = new TblMachineDailyReportVo();
            aVo.setId(report.getId());
            aVo.setPersonName(report.getMstUser().getUserName());
            aVo.setProductionDateStr(fu.getDateFormatForStr(report.getTblMachineDailyReportPK().getProductionDate()));
            aVo.setReporterUuid(report.getReporterUuid());
            if (null != report.getMstUser()) {
                aVo.setReporterName(report.getMstUser().getUserName());
            } else {
                aVo.setReporterName("");
            }
            aVo.setStartDatetimeStr(fu.getDateTimeFormatForStr(report.getStartDatetime()));
            aVo.setStartDatetimeStzStr(fu.getDateTimeFormatForStr(report.getStartDatetimeStz()));
            if (null != report.getEndDatetime()) {
                aVo.setEndDatetime(report.getEndDatetime());
                aVo.setEndDatetimeStr(fu.getDateTimeFormatForStr(report.getEndDatetime()));
                aVo.setEndDatetimeStzStr(fu.getDateTimeFormatForStr(report.getEndDatetimeStz()));
            } else {
                aVo.setEndDatetime(null);
                aVo.setEndDatetimeStr("");
                aVo.setEndDatetimeStzStr("");
            }
            if (null != report.getNetProducintTimeMinutes()) {
                aVo.setNetProducintTimeMinutes(String.valueOf(report.getNetProducintTimeMinutes()));
            }else{
                aVo.setNetProducintTimeMinutes("0");
            }

            if (null != report.getSuspendedTimeMinutes()) {
                aVo.setSuspendedTimeMinutes(String.valueOf(report.getSuspendedTimeMinutes()));
            } else {
                aVo.setSuspendedTimeMinutes("0");
            }
            
            aVo.setNoRegistrationFlag(String.valueOf(report.getNoRegistrationFlag()));
            
            if (0 == report.getNoRegistrationFlag()) {

                aVo.setNoRegistrationFlagText(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "holiday"));
            } else {
                aVo.setNoRegistrationFlagText("");
            }

            aVo.setShotCount(String.valueOf(report.getShotCount()));
            aVo.setDisposedShotCount(String.valueOf(report.getDisposedShotCount()));
            response.add(aVo);
        }
        dailyReportVo.setMachineDailyReportVos(response);

        return dailyReportVo;
    }

    /**
     * @param machineDailyReportId
     * @param productionId
     * @param loginUser
     * @return
     */
    public TblMachineDailyReportVo getDailyReportDetails(String machineDailyReportId, String productionId, LoginUser loginUser) {
        TblMachineDailyReportVo dailyReportVo = new TblMachineDailyReportVo();

        FileUtil fu = new FileUtil();
        if (null != productionId && !productionId.isEmpty()) {
            return getDailyReportDetailsByProductionId(productionId, loginUser);
        }

        StringBuilder sqlMachineDailyReport = new StringBuilder("Select r from TblMachineDailyReport r JOIN FETCH r.tblProduction where 1=1 ");
        if (null != machineDailyReportId && !machineDailyReportId.isEmpty()) {
            sqlMachineDailyReport.append(" and r.id = :machineDailyReportId ");
        }
        Query machineDailyReportQuery = entityManager.createQuery(sqlMachineDailyReport.toString());
        if (null != machineDailyReportId && !machineDailyReportId.isEmpty()) {
            machineDailyReportQuery.setParameter("machineDailyReportId", machineDailyReportId);
        }
        List<TblMachineDailyReport> machineDailyReports = machineDailyReportQuery.getResultList();

        if (null == machineDailyReports || machineDailyReports.isEmpty()) {
            dailyReportVo.setError(true);
            dailyReportVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            dailyReportVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return dailyReportVo;
        }

        TblMachineDailyReport tblMachineDailyReport = machineDailyReports.get(0);
        List<TblMachineDailyReportVo> response = new ArrayList<>();

        TblMachineDailyReportVo aVo = new TblMachineDailyReportVo();
        aVo.setTblProduction(tblMachineDailyReport.getTblProduction());
        // 生産終了チェックボックスフラグ
        if (tblMachineDailyReport.getTblProduction() != null) {
            Map<String, String> dates = getLastProductionDate(tblMachineDailyReport.getTblProduction());
            String productionDate = dates.get("productionDate");
            if (tblMachineDailyReport.getTblProduction().getStatus() != null && tblMachineDailyReport.getTblProduction().getStatus() == 9) {
                aVo.setProductionEndFlag(true);
            } else if (!productionDate.equals(DateFormat.dateToStr(DateFormat.getAfterDay(tblMachineDailyReport.getTblMachineDailyReportPK().getProductionDate()), DateFormat.DATE_FORMAT))) {
                aVo.setProductionEndFlag(true);
            } else {
                aVo.setProductionEndFlag(false);
            }
        }
        //生産日
        aVo.setProductionDate(tblMachineDailyReport.getTblMachineDailyReportPK().getProductionDate());
        aVo.setProductionDateStr(fu.getDateFormatForStr(tblMachineDailyReport.getTblMachineDailyReportPK().getProductionDate()));
        //生産開始時刻
        aVo.setStartDatetimeStr(fu.getDateTimeFormatForStr(tblMachineDailyReport.getStartDatetime()));
        aVo.setStartDatetimeStzStr(fu.getDateTimeFormatForStr(tblMachineDailyReport.getStartDatetimeStz()));
        //生産終了時刻
        aVo.setEndDatetimeStr(fu.getDateTimeFormatForStr(tblMachineDailyReport.getEndDatetime()));
        aVo.setEndDatetimeStzStr(fu.getDateTimeFormatForStr(tblMachineDailyReport.getEndDatetimeStz()));
        aVo.setProducingTimeMinutes("" + tblMachineDailyReport.getProducingTimeMinutes());
        //中断時間
        if (null != tblMachineDailyReport.getSuspendedTimeMinutes()) {
            aVo.setSuspendedTimeMinutes(String.valueOf(tblMachineDailyReport.getSuspendedTimeMinutes()));
        } else {
            aVo.setSuspendedTimeMinutes("0");
        }

        //実稼働時間
        if (null != tblMachineDailyReport.getNetProducintTimeMinutes()) {
            aVo.setNetProducintTimeMinutes(String.valueOf(tblMachineDailyReport.getNetProducintTimeMinutes()));
        } else {
            aVo.setNetProducintTimeMinutes("0");
        }
        
        aVo.setNoRegistrationFlag(String.valueOf(tblMachineDailyReport.getNoRegistrationFlag()));
        
        MstMachine machine = null;
        if (null != (machine = tblMachineDailyReport.getMstMachine())) {
            aVo.setMachineId(machine.getMachineId());
            aVo.setMachineUuid(machine.getUuid());
            aVo.setMachineName(machine.getMachineName());
        } else {
            aVo.setMachineId("");
            aVo.setMachineUuid("");
            aVo.setMachineName("");
        }

        aVo.setShotCount(String.valueOf(tblMachineDailyReport.getShotCount()));
        aVo.setDisposedShotCount(String.valueOf(tblMachineDailyReport.getDisposedShotCount()));

        if (null != tblMachineDailyReport.getMessageForNextDay() && !tblMachineDailyReport.getMessageForNextDay().isEmpty()) {
            aVo.setMessageForNextDay(tblMachineDailyReport.getMessageForNextDay());
        } else {
            aVo.setMessageForNextDay("");
        }
        aVo.setProductionId(tblMachineDailyReport.getTblMachineDailyReportPK().getProductionId());

        TblMachineDailyReport dailyInfo = getMessageForBeforetDay(aVo.getProductionId(), aVo.getProductionDateStr());
        aVo.setMessageForPreviousDay(dailyInfo == null ? "" : dailyInfo.getMessageForNextDay());
        
        aVo.setLotNumber(tblMachineDailyReport.getTblProduction().getLotNumber());
        aVo.setPrevLotNumber(tblMachineDailyReport.getTblProduction().getPrevLotNumber());
        if (null != tblMachineDailyReport.getTblProduction().getTblDirection()) {
            aVo.setDirectionCode(tblMachineDailyReport.getTblProduction().getTblDirection().getDirectionCode());
        }

        StringBuilder sqlMachineDailyDetailReport = new StringBuilder("Select d from TblMachineDailyReportDetail d where 1=1 ");
        if (null != machineDailyReportId && !machineDailyReportId.isEmpty()) {
            sqlMachineDailyDetailReport.append(" and d.tblMachineDailyReportDetailPK.macReportId = :machineDailyReportId ");
        }
        sqlMachineDailyDetailReport.append("ORDER BY d.mstComponent.componentCode DESC");
        Query machineDailyReportDetailQuery = entityManager.createQuery(sqlMachineDailyDetailReport.toString());
        if (null != machineDailyReportId && !machineDailyReportId.isEmpty()) {
            machineDailyReportDetailQuery.setParameter("machineDailyReportId", machineDailyReportId);
        }
        List<TblMachineDailyReportDetail> machineDailyReportDetails = machineDailyReportDetailQuery.getResultList();
        List<TblMachineDailyReportDetailVo> detailVos = new ArrayList<>();
        for (TblMachineDailyReportDetail aDetail : machineDailyReportDetails) {
            TblMachineDailyReportDetailVo aDetailVo = new TblMachineDailyReportDetailVo();
            if (!aVo.getProductionEndFlag()) {
                // 生産実績明細情報設定
                TblProductionDetail tblProductionDetail = tblProductionDetailService.getProductionDetailSingleById(aDetail.getTblMachineDailyReportDetailPK().getProductionDetailId());
                aDetailVo.setTblProductionDetail(tblProductionDetail);
            }
            aDetailVo.setId(aDetail.getId());
            if (null != aDetail.getMstComponent()) {
                aDetailVo.setComponentId(aDetail.getComponentId());
                aDetailVo.setComponentCode(aDetail.getMstComponent().getComponentCode());
                aDetailVo.setComponentName(aDetail.getMstComponent().getComponentName());
            } else {
                aDetailVo.setComponentCode("");
                aDetailVo.setComponentName("");
            }
            
            aDetailVo.setCompleteCount(String.valueOf(aDetail.getCompleteCount()));

            if (null != aDetail.getComponentNetProducintTimeMinutes() ) {
                
                aDetailVo.setComponentNetProducintTimeMinutes(String.valueOf(aDetail.getComponentNetProducintTimeMinutes()));
            } else {
                aDetailVo.setComponentNetProducintTimeMinutes("0");
            }
            
            aDetailVo.setCountPerShot(String.valueOf(aDetail.getCountPerShot()));
            aDetailVo.setDefectCount(String.valueOf(aDetail.getDefectCount()));
            
            if (null != aDetail.getMstProcedure()) {
                aDetailVo.setProcedureId(aDetail.getProcedureId());
                aDetailVo.setProcedureCode(aDetail.getMstProcedure().getProcedureCode());
            } else {
                aDetailVo.setProcedureId("");
                aDetailVo.setProcedureCode("");
            }

            aDetailVo.setMaterial01Id(aDetail.getMaterial01Id());

            if (null != aDetail.getMstMaterial01()) {
                if (null != aDetail.getMstMaterial01().getMaterialName() && !aDetail.getMstMaterial01().getMaterialName().isEmpty()) {
                    aDetailVo.setMaterial01Name(aDetail.getMstMaterial01().getMaterialName());
                } else {
                    aDetailVo.setMaterial01Name("");
                }

                if (null != aDetail.getMstMaterial01().getMaterialType() && !aDetail.getMstMaterial01().getMaterialType().isEmpty()) {
                    aDetailVo.setMaterial01Type(aDetail.getMstMaterial01().getMaterialType());
                } else {
                    aDetailVo.setMaterial01Type("");
                }

                if (null != aDetail.getMstMaterial01().getMaterialGrade() && !aDetail.getMstMaterial01().getMaterialGrade().isEmpty()) {
                    aDetailVo.setMaterial01Grade(aDetail.getMstMaterial01().getMaterialGrade());
                } else {
                    aDetailVo.setMaterial01Grade("");
                }
                
                if (null != aDetail.getMstMaterial01().getMaterialCode() && !aDetail.getMstMaterial01().getMaterialCode().isEmpty()) {
                    aDetailVo.setMaterial01Code(aDetail.getMstMaterial01().getMaterialCode());
                } else {
                    aDetailVo.setMaterial01Code("");
                }

                if (null != aDetail.getMaterial01LotNo() && !aDetail.getMaterial01LotNo().isEmpty()) {
                    aDetailVo.setMaterial01LotNo(aDetail.getMaterial01LotNo());
                }

                if (null != aDetail.getMaterial01PurgedAmount()) {
                    aDetailVo.setMaterial01PurgedAmount("" + aDetail.getMaterial01PurgedAmount());
                }
                aDetailVo.setMaterial01Amount("" + aDetail.getMaterial01Amount());
            }

            aDetailVo.setMaterial02Id(aDetail.getMaterial02Id());

            if (null != aDetail.getMstMaterial02()) {
                if (null != aDetail.getMstMaterial02().getMaterialName() && !aDetail.getMstMaterial02().getMaterialName().isEmpty()) {
                    aDetailVo.setMaterial02Name(aDetail.getMstMaterial02().getMaterialName());
                } else {
                    aDetailVo.setMaterial02Name("");
                }

                if (null != aDetail.getMstMaterial02().getMaterialType() && !aDetail.getMstMaterial02().getMaterialType().isEmpty()) {
                    aDetailVo.setMaterial02Type(aDetail.getMstMaterial02().getMaterialType());
                } else {
                    aDetailVo.setMaterial02Type("");
                }

                if (null != aDetail.getMstMaterial02().getMaterialGrade() && !aDetail.getMstMaterial02().getMaterialGrade().isEmpty()) {
                    aDetailVo.setMaterial02Grade(aDetail.getMstMaterial02().getMaterialGrade());
                } else {
                    aDetailVo.setMaterial02Grade("");
                }
                
                if (null != aDetail.getMstMaterial02().getMaterialCode() && !aDetail.getMstMaterial02().getMaterialCode().isEmpty()) {
                    aDetailVo.setMaterial02Code(aDetail.getMstMaterial02().getMaterialCode());
                } else {
                    aDetailVo.setMaterial02Code("");
                }

                if (null != aDetail.getMaterial02LotNo() && !aDetail.getMaterial02LotNo().isEmpty()) {
                    aDetailVo.setMaterial02LotNo(aDetail.getMaterial02LotNo());
                }

                if (null != aDetail.getMaterial02PurgedAmount()) {
                    aDetailVo.setMaterial02PurgedAmount("" + aDetail.getMaterial02PurgedAmount());
                }

                aDetailVo.setMaterial02Amount("" + aDetail.getMaterial02Amount());
            }

            aDetailVo.setMaterial03Id(aDetail.getMaterial03Id());

            if (null != aDetail.getMstMaterial03()) {
                if (null != aDetail.getMstMaterial03().getMaterialName() && !aDetail.getMstMaterial03().getMaterialName().isEmpty()) {
                    aDetailVo.setMaterial03Name(aDetail.getMstMaterial03().getMaterialName());
                } else {
                    aDetailVo.setMaterial03Name("");
                }

                if (null != aDetail.getMstMaterial03().getMaterialType() && !aDetail.getMstMaterial03().getMaterialType().isEmpty()) {
                    aDetailVo.setMaterial03Type(aDetail.getMstMaterial03().getMaterialType());
                } else {
                    aDetailVo.setMaterial03Type("");
                }

                if (null != aDetail.getMstMaterial03().getMaterialGrade() && !aDetail.getMstMaterial03().getMaterialGrade().isEmpty()) {
                    aDetailVo.setMaterial03Grade(aDetail.getMstMaterial03().getMaterialGrade());
                } else {
                    aDetailVo.setMaterial03Grade("");
                }
                
                if (null != aDetail.getMstMaterial03().getMaterialCode() && !aDetail.getMstMaterial03().getMaterialCode().isEmpty()) {
                    aDetailVo.setMaterial03Code(aDetail.getMstMaterial03().getMaterialCode());
                } else {
                    aDetailVo.setMaterial03Code("");
                }

                if (null != aDetail.getMaterial03LotNo() && !aDetail.getMaterial03LotNo().isEmpty()) {
                    aDetailVo.setMaterial03LotNo(aDetail.getMaterial03LotNo());
                }

                if (null != aDetail.getMaterial03PurgedAmount()) {
                    aDetailVo.setMaterial03PurgedAmount("" + aDetail.getMaterial03PurgedAmount());
                }

                aDetailVo.setMaterial03Amount("" + aDetail.getMaterial03Amount());
            }

            // 材料関連の部品材料情報を取得
            getComponentMaterialInfo(aDetailVo);

            aDetailVo.setProductionDetailId(aDetail.getTblMachineDailyReportDetailPK().getProductionDetailId());

//            aDetailVo.setDefectCount("" + aDetail.getDefectCount());
//            aDetailVo.setCompleteCount("" + aDetail.getCompleteCount());
//            aDetailVo.setCountPerShot("" + aDetail.getCountPerShot());

            detailVos.add(aDetailVo);
        }
        
        // 部品コード昇順
        Collections.sort(detailVos, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TblMachineDailyReportDetailVo obj1 = (TblMachineDailyReportDetailVo) o1;
                TblMachineDailyReportDetailVo obj2 = (TblMachineDailyReportDetailVo) o2;
                return obj1.getComponentCode().compareTo(obj2.getComponentCode());
            }
        });
        
        aVo.setMachineDailyReportDetailVos(detailVos);

        TblProductionSuspensionList tblProductionSuspensionList = tblProductionSuspensionlService.getProductionSuspensionListByProductionId(aVo.getProductionId(), loginUser);

        aVo.setProductionSuspensionVos(tblProductionSuspensionList.getTblProductionSuspensionVo());
        response.add(aVo);

        dailyReportVo.setMachineDailyReportVos(response);

        return dailyReportVo;
    }
    
    /**
     * 生産実績IDによりすべて機械日報情報取得
     * 
     * @param productionId
     * @return 
     */
    public TblMachineDailyReportDetailList getAllDailyReportByProductionId(String productionId) {
        StringBuilder sql = new StringBuilder("SELECT dailyReportDetail FROM TblMachineDailyReportDetail dailyReportDetail JOIN FETCH dailyReportDetail.tblMachineDailyReport WHERE ");
        sql.append("dailyReportDetail.tblMachineDailyReport.tblMachineDailyReportPK.productionId = :productionId ");
        sql.append("ORDER BY dailyReportDetail.tblMachineDailyReport.tblMachineDailyReportPK.productionDate DESC");
        Query machineDailyReportDetailQuery = entityManager.createQuery(sql.toString());
        machineDailyReportDetailQuery.setParameter("productionId", productionId);
        List<TblMachineDailyReportDetail> machineDailyReportDetails = machineDailyReportDetailQuery.getResultList();
        TblMachineDailyReportDetailList response = new TblMachineDailyReportDetailList();
        response.setTblMachineDailyReportDetails(machineDailyReportDetails);
        return response;
    }

    public TblMachineDailyReportVo getDailyReportDetailsByProductionId(String productionId, LoginUser loginUser) {
        TblMachineDailyReportVo dailyReportVo = new TblMachineDailyReportVo();
        
        if (null == productionId || productionId.isEmpty()) {
            return dailyReportVo;
        }
        TblProduction oldProduction = entityManager.find(TblProduction.class, productionId);
        if (null == oldProduction) {
            dailyReportVo.setError(true);
            dailyReportVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            dailyReportVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return dailyReportVo;
        }

        List<TblMachineDailyReportVo> response = new ArrayList<>();

        TblMachineDailyReportVo aVo = new TblMachineDailyReportVo();
        Map<String, String> dates = getLastProductionDate(oldProduction);
        aVo.setProductionDateStr(dates.get("productionDate"));
        //生産開始時刻
        aVo.setStartDatetimeStr(dates.get("startDatetime"));
        //生産終了時刻
        aVo.setEndDatetimeStr(dates.get("endDatetime"));
        
        String chkProductionDateFlg = checkProductionDate(aVo, productionId, loginUser);
        
        // 最新の機械日報編集モードに変更
        if ("1".equals(chkProductionDateFlg)) {

            TblMachineDailyReportVo res = getDailyReportDetails(aVo.getId(), null, loginUser);
            
            return res;

        }

        // 生産終了チェックボックスフラグ
        aVo.setProductionEndFlag(false);
        aVo.setTblProduction(oldProduction);
        // 生産開始入力した捨てショット数は初日の機械日報(機械日報の生産日と生産実績の生産日と同じ)につける
        boolean firstDay = false;
        if (dates.get("productionDate").equals(DateFormat.dateToStr(oldProduction.getProductionDate(), DateFormat.DATE_FORMAT))) {
            firstDay = true;
            aVo.setDisposedShotCount(String.valueOf(oldProduction.getDisposedShotCount()));
        } else {
            aVo.setDisposedShotCount("0");
        }
        MstMachine machine = null;
        if (null != (machine = oldProduction.getMstMachine())) {
            aVo.setMachineId(machine.getMachineId());
            aVo.setMachineUuid(machine.getUuid());
            aVo.setMachineName(machine.getMachineName());
        } else {
            aVo.setMachineId("");
            aVo.setMachineUuid("");
            aVo.setMachineName("");
        }

        aVo.setProducingTimeMinutes("0");
        aVo.setReporterUuid("");
        aVo.setProductionId(productionId);
        TblMachineDailyReport dailyInfo = getMessageForBeforetDay(aVo.getProductionId(), aVo.getProductionDateStr());
        aVo.setMessageForPreviousDay(dailyInfo == null ? "" : dailyInfo.getMessageForNextDay());
        aVo.setShotCount("0");
        if (null != oldProduction.getTblDirection()) {
            aVo.setDirectionCode(oldProduction.getTblDirection().getDirectionCode());
        }
        aVo.setLotNumber(oldProduction.getLotNumber());
        aVo.setPrevLotNumber(oldProduction.getPrevLotNumber());

        TblProductionSuspensionList tblProductionSuspensionList = tblProductionSuspensionlService.getProductionSuspensionListByProductionId(productionId, loginUser);

        aVo.setProductionSuspensionVos(tblProductionSuspensionList.getTblProductionSuspensionVo());

//        //中断時間 生産開始時刻、生産終了時刻の間の中断履歴テーブルの中断時間合計。
//        aVo.setSuspendedTimeMinutes("" + tblProductionSuspensionList.getSumSuspendedTimeMinute());
        List<TblProductionDetail> productionDetails = entityManager.createQuery("Select d from TblProductionDetail d where d.tblProduction.id = :productionId ")
                .setParameter("productionId", productionId)
                .getResultList();
        // 前日機械日報明細の材料ロット番号取得
        List<TblMachineDailyReportDetail> lotNumList = getLotNumForBeforetDay(dailyInfo == null ? "" : dailyInfo.getId());
        List<TblMachineDailyReportDetailVo> detailVos = new ArrayList<>();
        for (TblProductionDetail aDetail : productionDetails) {
            TblMachineDailyReportDetailVo aDetailVo = new TblMachineDailyReportDetailVo();
            aDetailVo.setTblProductionDetail(aDetail);
            aDetailVo.setId(aDetail.getId());
            aDetailVo.setProductionDetailId(aDetail.getId());
            if (null != aDetail.getMstComponent()) {
                aDetailVo.setComponentId(aDetail.getComponentId());
                aDetailVo.setComponentCode(aDetail.getMstComponent().getComponentCode());
                aDetailVo.setComponentName(aDetail.getMstComponent().getComponentName());
            } else {
                aDetailVo.setComponentCode("");
                aDetailVo.setComponentName("");
            }
            
            aDetailVo.setCompleteCount(String.valueOf(aDetail.getCompleteCount()));
            aDetailVo.setComponentNetProducintTimeMinutes("0");
            aDetailVo.setCountPerShot(String.valueOf(aDetail.getCountPerShot()));
            aDetailVo.setDefectCount(String.valueOf(aDetail.getDefectCount()));

            if (null != aDetail.getMstProcedure()) {
                aDetailVo.setProcedureId(aDetail.getProcedureId());
                aDetailVo.setProcedureCode(aDetail.getMstProcedure().getProcedureCode());
            } else {
                aDetailVo.setProcedureId("");
                aDetailVo.setProcedureCode("");
            }

            aDetailVo.setMaterial01Id(aDetail.getMaterial01Id());

            if (null != aDetail.getMstMaterial01()) {
                if (null != aDetail.getMstMaterial01().getMaterialName() && !aDetail.getMstMaterial01().getMaterialName().isEmpty()) {
                    aDetailVo.setMaterial01Name(aDetail.getMstMaterial01().getMaterialName());
                } else {
                    aDetailVo.setMaterial01Name("");
                }

                if (null != aDetail.getMstMaterial01().getMaterialType() && !aDetail.getMstMaterial01().getMaterialType().isEmpty()) {
                    aDetailVo.setMaterial01Type(aDetail.getMstMaterial01().getMaterialType());
                } else {
                    aDetailVo.setMaterial01Type("");
                }

                if (null != aDetail.getMstMaterial01().getMaterialGrade() && !aDetail.getMstMaterial01().getMaterialGrade().isEmpty()) {
                    aDetailVo.setMaterial01Grade(aDetail.getMstMaterial01().getMaterialGrade());
                } else {
                    aDetailVo.setMaterial01Grade("");
                }
                
                if (null != aDetail.getMstMaterial01().getMaterialCode() && !aDetail.getMstMaterial01().getMaterialCode().isEmpty()) {
                    aDetailVo.setMaterial01Code(aDetail.getMstMaterial01().getMaterialCode());
                } else {
                    aDetailVo.setMaterial01Code("");
                }

                if (null != aDetail.getMaterial01LotNo() && !aDetail.getMaterial01LotNo().isEmpty()) {
                    aDetailVo.setMaterial01LotNo(aDetail.getMaterial01LotNo());
                }

                if (null != aDetail.getMaterial01PurgedAmount()) {
                    if (firstDay) {
                        aDetailVo.setMaterial01PurgedAmount("" + aDetail.getMaterial01PurgedAmount());
                    }
                }
                aDetailVo.setMaterial01Amount("" + aDetail.getMaterial01Amount());
            }

            aDetailVo.setMaterial02Id(aDetail.getMaterial02Id());

            if (null != aDetail.getMstMaterial02()) {
                if (null != aDetail.getMstMaterial02().getMaterialName() && !aDetail.getMstMaterial02().getMaterialName().isEmpty()) {
                    aDetailVo.setMaterial02Name(aDetail.getMstMaterial02().getMaterialName());
                } else {
                    aDetailVo.setMaterial02Name("");
                }

                if (null != aDetail.getMstMaterial02().getMaterialType() && !aDetail.getMstMaterial02().getMaterialType().isEmpty()) {
                    aDetailVo.setMaterial02Type(aDetail.getMstMaterial02().getMaterialType());
                } else {
                    aDetailVo.setMaterial02Type("");
                }

                if (null != aDetail.getMstMaterial02().getMaterialGrade() && !aDetail.getMstMaterial02().getMaterialGrade().isEmpty()) {
                    aDetailVo.setMaterial02Grade(aDetail.getMstMaterial02().getMaterialGrade());
                } else {
                    aDetailVo.setMaterial02Grade("");
                }
                
                if (null != aDetail.getMstMaterial02().getMaterialCode() && !aDetail.getMstMaterial02().getMaterialCode().isEmpty()) {
                    aDetailVo.setMaterial02Code(aDetail.getMstMaterial02().getMaterialCode());
                } else {
                    aDetailVo.setMaterial02Code("");
                }

                if (null != aDetail.getMaterial02LotNo() && !aDetail.getMaterial02LotNo().isEmpty()) {
                    aDetailVo.setMaterial02LotNo(aDetail.getMaterial02LotNo());
                }

                if (null != aDetail.getMaterial02PurgedAmount()) {
                    if (firstDay) {
                        aDetailVo.setMaterial02PurgedAmount("" + aDetail.getMaterial02PurgedAmount());
                    }
                }

                aDetailVo.setMaterial02Amount("" + aDetail.getMaterial02Amount());
            }

            aDetailVo.setMaterial03Id(aDetail.getMaterial03Id());

            if (null != aDetail.getMstMaterial03()) {
                if (null != aDetail.getMstMaterial03().getMaterialName() && !aDetail.getMstMaterial03().getMaterialName().isEmpty()) {
                    aDetailVo.setMaterial03Name(aDetail.getMstMaterial03().getMaterialName());
                } else {
                    aDetailVo.setMaterial03Name("");
                }

                if (null != aDetail.getMstMaterial03().getMaterialType() && !aDetail.getMstMaterial03().getMaterialType().isEmpty()) {
                    aDetailVo.setMaterial03Type(aDetail.getMstMaterial03().getMaterialType());
                } else {
                    aDetailVo.setMaterial03Type("");
                }

                if (null != aDetail.getMstMaterial03().getMaterialGrade() && !aDetail.getMstMaterial03().getMaterialGrade().isEmpty()) {
                    aDetailVo.setMaterial03Grade(aDetail.getMstMaterial03().getMaterialGrade());
                } else {
                    aDetailVo.setMaterial03Grade("");
                }
                
                if (null != aDetail.getMstMaterial03().getMaterialCode() && !aDetail.getMstMaterial03().getMaterialCode().isEmpty()) {
                    aDetailVo.setMaterial03Code(aDetail.getMstMaterial03().getMaterialCode());
                } else {
                    aDetailVo.setMaterial03Code("");
                }

                if (null != aDetail.getMaterial03LotNo() && !aDetail.getMaterial03LotNo().isEmpty()) {
                    aDetailVo.setMaterial03LotNo(aDetail.getMaterial03LotNo());
                }

                if (null != aDetail.getMaterial03PurgedAmount()) {
                    if (firstDay) {
                        aDetailVo.setMaterial03PurgedAmount("" + aDetail.getMaterial03PurgedAmount());
                    }
                }

                aDetailVo.setMaterial03Amount("" + aDetail.getMaterial03Amount());
            }

            // 材料関連の部品材料情報を取得
            getComponentMaterialInfo(aDetailVo);
            
            // 材料ロット番号を前日の機械日報から引き継ぐ
            setMaterialLotNumFromBeforeDailyReport(aDetailVo, lotNumList);

//            aDetailVo.setCompleteCount("0");
//            aDetailVo.setCountPerShot("0");
//            aDetailVo.setDefectCount("0");
            aDetailVo.setMacReportId("");
            aDetailVo.setProductionDetailId("");

            detailVos.add(aDetailVo);
        }
        
        // 部品コード昇順
        Collections.sort(detailVos, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TblMachineDailyReportDetailVo obj1 = (TblMachineDailyReportDetailVo) o1;
                TblMachineDailyReportDetailVo obj2 = (TblMachineDailyReportDetailVo) o2;
                return obj1.getComponentCode().compareTo(obj2.getComponentCode());
            }
        });

        aVo.setMachineDailyReportDetailVos(detailVos);

        // 中断時間を計算する
        calculateSuspendedMin(aVo, loginUser);

        // 実稼動時間を計算
        calculateMin(aVo);

        response.add(aVo);

        dailyReportVo.setMachineDailyReportVos(response);

        return dailyReportVo;
    }

    /**
     *
     * @param macReportId
     * @return
     */
    public List<TblMachineDailyReportDetail> getDailyReportDetailsByMacReportId(String macReportId) {
        Query query = entityManager.createNamedQuery("TblMachineDailyReportDetail.findByMacReportId");
        query.setParameter("macReportId", macReportId);
        return query.getResultList();
    }
    /*
    * 取得productionDate
     */
    Map<String, String> getLastProductionDate(TblProduction production) {
        Map<String, String> dates = new HashMap();
        FileUtil fu = new FileUtil();
        Calendar calendar = Calendar.getInstance();

        List<TblMachineDailyReport> machineDailyReports = entityManager.createQuery("select r from TblMachineDailyReport r where r.tblMachineDailyReportPK.productionId = :productionId order by r.startDatetime desc ")
                .setParameter("productionId", production.getId())
                .setMaxResults(1)
                .getResultList();
        Date resDate = null;
        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
        String businessStartTime = cnfSystem.getConfigValue().length() == 4 ? "0" + cnfSystem.getConfigValue() : cnfSystem.getConfigValue();
        if (null == machineDailyReports || machineDailyReports.isEmpty()) {
//            resDate = production.getProductionDate();

            dates.put("startDatetime", fu.getDateTimeFormatForStr(production.getStartDatetime()));
            resDate = fu.getDateParseForDate(dates.get("startDatetime").substring(0,dates.get("startDatetime").indexOf(" ")));
        } else {
            resDate = machineDailyReports.get(0).getTblMachineDailyReportPK().getProductionDate();

            calendar.setTime(resDate);
            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, day + 1);
            resDate = calendar.getTime();

            dates.put("startDatetime", fu.getDateFormatForStr(resDate) + " " + businessStartTime + DateFormat.TIME_SECONDS);
        }

        dates.put("productionDate", fu.getDateFormatForStr(resDate));

        Date nowDate = new Date();
        String nowDateStr = DateFormat.dateToStr(nowDate, DateFormat.DATE_FORMAT);
        if (dates.get("productionDate").compareTo(nowDateStr) == 0) {
            dates.put("endDatetime", DateFormat.dateToStr(nowDate, DateFormat.DATETIME_NOSECONDS_FORMAT) + DateFormat.TIME_SECONDS);
        } else {
            calendar.setTime(resDate);
            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, day + 1);
            Date endDatetime = calendar.getTime();
            dates.put("endDatetime", fu.getDateFormatForStr(endDatetime) + " " + businessStartTime + DateFormat.TIME_SECONDS);
        }
        return dates;
    }

    /**
     * 機械日報一覧データ取得
     *
     * @param componentCode
     * @param formatProductionDateFrom
     * @param formatProductionDateTo
     * @param reporterUser
     * @param machineId
     * @param department
     * @param action
     * @return
     */
    private List getMacDailyReportList(
            String componentCode,
            Date formatProductionDateFrom,
            Date formatProductionDateTo,
            String reporterUser,
            String machineId,
            int department,
            String action) {
        StringBuilder sql = new StringBuilder();

        if (COUNT_STR.equals(action)) {
            sql.append(" SELECT count(1) ");
        } else {
            sql.append(" SELECT dailyReportDetail ");
        }

        sql.append(" FROM TblMachineDailyReportDetail dailyReportDetail ");
        sql.append(" JOIN FETCH dailyReportDetail.tblMachineDailyReport ");
        if (!COUNT_STR.equals(action)) {
            sql.append(" LEFT JOIN FETCH dailyReportDetail.tblMachineDailyReport.mstUser ");
            sql.append(" LEFT JOIN FETCH dailyReportDetail.tblMachineDailyReport.mstMachine ");
        }
        sql.append(" WHERE dailyReportDetail.tblMachineDailyReport.noRegistrationFlag != 0 ");

        // 部品コード 部分一致
        if (!StringUtils.isEmpty(componentCode)) {
            sql.append(" AND dailyReportDetail.mstComponent.componentCode like :componentCode ");
        }
        // 開始時刻 同日以上
        if (formatProductionDateFrom != null) {
            sql.append(" AND dailyReportDetail.tblMachineDailyReport.tblMachineDailyReportPK.productionDate >= :formatProductionDateFrom ");
        }
        // 終了時刻 同日以下
        if (formatProductionDateTo != null) {
            sql.append(" AND dailyReportDetail.tblMachineDailyReport.tblMachineDailyReportPK.productionDate <= :formatProductionDateTo ");
        }
        // 報告者 部分一致
        if (!StringUtils.isEmpty(reporterUser)) {
            sql.append(" AND dailyReportDetail.tblMachineDailyReport.mstUser.userName like :reporterUser ");
        }
        // 設備ID 完全一致
        if (!StringUtils.isEmpty(machineId)) {
            sql.append(" AND dailyReportDetail.tblMachineDailyReport.mstMachine.machineId = :machineId ");
        }
        // 所属 完全一致
        if (department != 0) {
            sql.append(" AND dailyReportDetail.tblMachineDailyReport.mstMachine.department = :department ");
        }
        if (!COUNT_STR.equals(action)) {
            // 生産日　降順, 開始時刻 降順
            sql.append(" ORDER BY dailyReportDetail.tblMachineDailyReport.tblMachineDailyReportPK.productionDate DESC, dailyReportDetail.tblMachineDailyReport.startDatetime DESC ");
        }
        Query query = entityManager.createQuery(sql.toString());

        // パラーメタ設定
        if (!StringUtils.isEmpty(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }
        if (formatProductionDateFrom != null) {
            query.setParameter("formatProductionDateFrom", formatProductionDateFrom);
        }
        if (formatProductionDateTo != null) {
            query.setParameter("formatProductionDateTo", formatProductionDateTo);
        }
        if (!StringUtils.isEmpty(reporterUser)) {
            query.setParameter("reporterUser", "%" + reporterUser + "%");
        }
        if (!StringUtils.isEmpty(machineId)) {
            query.setParameter("machineId", machineId);
        }
        if (department != 0) {
            query.setParameter("department", department);
        }

        List list = query.getResultList();
        return list;
    }

    /**
     * 機械日報post
     *
     * @param id
     * @param loginUser
     */
    @Transactional
    TblMachineDailyReportVo postDailyReportDetail(TblMachineDailyReportVo machineDailyReportVo, LoginUser loginUser) {
        TblMachineDailyReportVo response = new TblMachineDailyReportVo();

        // 画面から機械日報明細取得
        List<TblMachineDailyReportDetailVo> detailVos = machineDailyReportVo.getMachineDailyReportDetailVos();
        // 構成部品取得フラグ
        if (machineDailyReportVo.getStructureFlg() == 0) {
            if(checkDateTime(machineDailyReportVo, loginUser)) {
            
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(machineDailyReportVo.getErrorMessage());

                return response;   
            }
            
            List<MstComponentStructureVoList> structureList = new ArrayList();
            if (null != detailVos && !detailVos.isEmpty()) {
                // 休日以外
                if (!"0".equals(machineDailyReportVo.getNoRegistrationFlag())) {
                    for (TblMachineDailyReportDetailVo aDetailVo : detailVos) {
                        // 完成数が0より大きい
                        if (Integer.parseInt(aDetailVo.getCompleteCount()) > 0) {
                            MstProcedure maxMstProcedureCode = mstProcedureService.getMaxProcedureCode(aDetailVo.getComponentId());
                            // 工程番号（工番）が部品コードの中で最大の時だけ実施する
                            if (aDetailVo.getProcedureCode().equals(maxMstProcedureCode == null ? "" : maxMstProcedureCode.getProcedureCode())) {
                                // 日報指定した部品の下位階層部品リスト取得
                                List<MstComponentStructure> list = tblStockService.getStructureListByParentComponentId(aDetailVo.getComponentId());
                                if (list != null && !list.isEmpty()) {
                                    for (MstComponentStructure cs : list) {
                                        // 下位階層部品のロット番号取得
                                        List<TblComponentLot> componentLots = entityManager.createNamedQuery("TblComponentLot.findByComponentIdForStructure").setParameter("componentId", cs.getComponentId()).getResultList();
                                        cs.setTblComponentLotList(componentLots);
                                    }
                                    MstComponentStructureVoList structureVoList = new MstComponentStructureVoList();
                                    structureVoList.setMstComponentStructures(list);
                                    structureList.add(structureVoList);
                                }
                            }
                        }
                    }
                }
            }
            // 下位階層部品存在している場合、構成部品ロット選択ダイアログ表示
            if (!structureList.isEmpty()) {
                response.setMstComponentStructureVoList(structureList);
                return response;
            }
        }
        
        FileUtil fu = new FileUtil();
        TblMachineDailyReport aDailyReport = null;
        List<TblMachineDailyReport> reports = entityManager.createNamedQuery("TblMachineDailyReport.findByProductionIdAndDate")
                .setParameter("productionId", machineDailyReportVo.getProductionId())
                .setParameter("productionDate", fu.getDateParseForDate(machineDailyReportVo.getProductionDateStr()))
                .setMaxResults(1)
                .getResultList();
        if (null == reports || reports.isEmpty()) {
            aDailyReport = new TblMachineDailyReport();
            TblMachineDailyReportPK rPK = new TblMachineDailyReportPK();
            rPK.setProductionId(machineDailyReportVo.getProductionId());
            rPK.setProductionDate(fu.getDateParseForDate(machineDailyReportVo.getProductionDateStr()));
            aDailyReport.setTblMachineDailyReportPK(rPK);
            aDailyReport.setId(IDGenerator.generate());
            aDailyReport.setCreateDate(new Date());
            aDailyReport.setCreateUserUuid(loginUser.getUserUuid());
        } else {
            aDailyReport = reports.get(0);
            machineDailyReportVo.setDisposedShotCountBeforeUpd(aDailyReport.getDisposedShotCount());
            machineDailyReportVo.setShotCountBeforeUpd(aDailyReport.getShotCount());
            machineDailyReportVo.setNetProducintTimeMinutesBeforeUpd(aDailyReport.getNetProducintTimeMinutes());
        }
        if (null != machineDailyReportVo.getDisposedShotCount() && !machineDailyReportVo.getDisposedShotCount().isEmpty()) {
            aDailyReport.setDisposedShotCount(Integer.parseInt(machineDailyReportVo.getDisposedShotCount()));
        } else {
            aDailyReport.setDisposedShotCount(0);
        }
        if (null != machineDailyReportVo.getMachineUuid() && !machineDailyReportVo.getMachineUuid().isEmpty()) {
            aDailyReport.setMachineUuid(machineDailyReportVo.getMachineUuid());
        } else {
            aDailyReport.setMachineUuid(null);
        }
        if (null != machineDailyReportVo.getReporterUuid() && !machineDailyReportVo.getReporterUuid().isEmpty()) {
            aDailyReport.setReporterUuid(machineDailyReportVo.getReporterUuid());
        } else {
            aDailyReport.setReporterUuid(null);
        }
        if (null != machineDailyReportVo.getStartDatetimeStr() && !machineDailyReportVo.getStartDatetimeStr().isEmpty()) {
            aDailyReport.setStartDatetime(fu.getDateTimeParseForDate(machineDailyReportVo.getStartDatetimeStr()));
            aDailyReport.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), fu.getDateTimeParseForDate(machineDailyReportVo.getStartDatetimeStr())));

        } else {
            aDailyReport.setStartDatetime(null);
            aDailyReport.setStartDatetimeStz(null);
        }
        if (null != machineDailyReportVo.getEndDatetimeStr() && !machineDailyReportVo.getEndDatetimeStr().isEmpty()) {
            machineDailyReportVo.setProductionDate(fu.getDateParseForDate(machineDailyReportVo.getProductionDateStr()));
            aDailyReport.setEndDatetime(fu.getDateTimeParseForDate(machineDailyReportVo.getEndDatetimeStr()));
            aDailyReport.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), fu.getDateTimeParseForDate(machineDailyReportVo.getEndDatetimeStr())));
        } else {
            machineDailyReportVo.setProductionDate(null);
            aDailyReport.setEndDatetime(null);
            aDailyReport.setEndDatetimeStz(null);
        }
        if (null != machineDailyReportVo.getMessageForNextDay() && !machineDailyReportVo.getMessageForNextDay().isEmpty()) {
            aDailyReport.setMessageForNextDay(machineDailyReportVo.getMessageForNextDay());
        } else {
            aDailyReport.setMessageForNextDay(null);
        }

        if (null != machineDailyReportVo.getNetProducintTimeMinutes()) {
            aDailyReport.setNetProducintTimeMinutes(Integer.parseInt(machineDailyReportVo.getNetProducintTimeMinutes()));
        } else {
            aDailyReport.setNetProducintTimeMinutes(0);
        }
        if (null != machineDailyReportVo.getProducingTimeMinutes() && !machineDailyReportVo.getProducingTimeMinutes().isEmpty()) {
            aDailyReport.setProducingTimeMinutes(Integer.parseInt(machineDailyReportVo.getProducingTimeMinutes()));
        } else {
            int tMinutes = 0;
            tMinutes = FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(machineDailyReportVo.getEndDatetimeStr()), fu.getDateTimeParseForDate(machineDailyReportVo.getStartDatetimeStr()));
            aDailyReport.setProducingTimeMinutes(tMinutes);
        }
        if (null != machineDailyReportVo.getShotCount() && !machineDailyReportVo.getShotCount().isEmpty()) {
            aDailyReport.setShotCount(Integer.parseInt(machineDailyReportVo.getShotCount()));
        } else {
            aDailyReport.setShotCount(0);
        }
        if (null != machineDailyReportVo.getSuspendedTimeMinutes() && !machineDailyReportVo.getSuspendedTimeMinutes().isEmpty()) {
            aDailyReport.setSuspendedTimeMinutes(Integer.parseInt(machineDailyReportVo.getSuspendedTimeMinutes()));
        } else {
            aDailyReport.setSuspendedTimeMinutes(0);
        }
        aDailyReport.setNoRegistrationFlag(Integer.parseInt(machineDailyReportVo.getNoRegistrationFlag()));
        aDailyReport.setUpdateDate(new Date());
        aDailyReport.setUpdateUserUuid(loginUser.getUserUuid());
        aDailyReport.setReporterUuid(loginUser.getUserUuid());
        // 日報作成時
        if (null == reports || reports.isEmpty()) {
            entityManager.persist(aDailyReport);
            TblProduction tblProduction = tblProductionService.getProductionSingleById(machineDailyReportVo.getProductionId());
            if (tblProduction != null && tblProduction.getMoldUuid() != null && !"".equals(tblProduction.getMoldUuid())) {
                // 金型マスタ最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数更新
                mstMoldService.updateMstMoldForDailyReport(tblProduction, machineDailyReportVo, loginUser);
            }
            // 設備マスタ最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数更新
            mstMachineService.updateMstMachineForDailyReport(tblProduction, machineDailyReportVo, loginUser);
        // 日報更新時
        } else {
            entityManager.merge(aDailyReport);
            if (!reports.isEmpty() && reports.get(0).getTblProduction() != null && reports.get(0).getTblProduction().getMoldUuid() != null && !"".equals(reports.get(0).getTblProduction().getMoldUuid())) {
                // 金型マスタ最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数更新
                mstMoldService.updateMstMoldForDailyReport(reports.get(0).getTblProduction(), machineDailyReportVo, loginUser);
            }
            // 設備マスタ最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数更新
            mstMachineService.updateMstMachineForDailyReport(reports.get(0).getTblProduction(), machineDailyReportVo, loginUser);
        }

        response.setId(aDailyReport.getId());

        if (null != detailVos && !detailVos.isEmpty()) {
            for (TblMachineDailyReportDetailVo aDetailVo : detailVos) {
                TblMachineDailyReportDetail aDetail = null;
                List<TblMachineDailyReportDetail> oldDetails = entityManager.createNamedQuery("TblMachineDailyReportDetail.findByProductionDetailIdAndMacReportId")
                        .setParameter("macReportId", aDailyReport.getId())
                        .setParameter("productionDetailId", null == aDetailVo.getProductionDetailId() || aDetailVo.getProductionDetailId().isEmpty() ? aDetailVo.getId() : aDetailVo.getProductionDetailId())
                        .setMaxResults(1)
                        .getResultList();
                if (null == oldDetails || oldDetails.isEmpty()) {
                    aDetail = new TblMachineDailyReportDetail();
                    TblMachineDailyReportDetailPK detailPK = new TblMachineDailyReportDetailPK();
                    detailPK.setMacReportId(aDailyReport.getId());
                    detailPK.setProductionDetailId(aDetailVo.getId());
                    aDetail.setTblMachineDailyReportDetailPK(detailPK);
                    aDetail.setId(IDGenerator.generate());
                    aDetail.setCreateDate(new Date());
                    aDetail.setCreateUserUuid(loginUser.getUserUuid());
                } else {
                    aDetail = oldDetails.get(0);
                    aDetailVo.setCompleteCountBeforeUpd(aDetail.getCompleteCount());
                    aDetailVo.setMaterial01AmountBeforeUpd(aDetail.getMaterial01Amount());
                    aDetailVo.setMaterial01PurgedAmountBeforeUpd(aDetail.getMaterial01PurgedAmount());
                    aDetailVo.setMaterial02AmountBeforeUpd(aDetail.getMaterial02Amount());
                    aDetailVo.setMaterial02PurgedAmountBeforeUpd(aDetail.getMaterial02PurgedAmount());
                    aDetailVo.setMaterial03AmountBeforeUpd(aDetail.getMaterial03Amount());
                    aDetailVo.setMaterial03PurgedAmountBeforeUpd(aDetail.getMaterial03PurgedAmount());
                }

                if (null != aDetailVo.getProcedureId() && !aDetailVo.getProcedureId().isEmpty()) {
                    aDetail.setProcedureId(aDetailVo.getProcedureId());
                } else {
                    aDetail.setProcedureId(null);
                }
                if (null != aDetailVo.getComponentId() && !aDetailVo.getComponentId().isEmpty()) {
                    aDetail.setComponentId(aDetailVo.getComponentId());
                } else {
                    aDetail.setComponentId(null);
                }
                // 機械日報部品毎完成数更新された場合、集計テーブル更新する
                // 機械日報詳細更新の場合
                if (null != oldDetails && !oldDetails.isEmpty()) {
                    int updCompleteCount = aDetail.getCompleteCount() - (aDetailVo.getCompleteCount() == null ? 0 : Integer.parseInt(aDetailVo.getCompleteCount()));
                    if (updCompleteCount != 0) {
                        calculateProductionPeriodForUpdate(aDailyReport, aDetail, updCompleteCount, loginUser);
                    }
                }
                if (null != aDetailVo.getCompleteCount() && !aDetailVo.getCompleteCount().isEmpty()) {
                    aDetail.setCompleteCount(Integer.parseInt(aDetailVo.getCompleteCount()));
                } else {
                    aDetail.setCompleteCount(0);
                }
                if (null != aDetailVo.getComponentNetProducintTimeMinutes() && !aDetailVo.getComponentNetProducintTimeMinutes().isEmpty()) {
                    aDetail.setComponentNetProducintTimeMinutes(Integer.parseInt(aDetailVo.getComponentNetProducintTimeMinutes()));
                } else {
                    aDetail.setComponentNetProducintTimeMinutes(0);
                }
                if (null != aDetailVo.getCountPerShot() && !aDetailVo.getCountPerShot().isEmpty()) {
                    aDetail.setCountPerShot(Integer.parseInt(aDetailVo.getCountPerShot()));
                } else {
                    aDetail.setCountPerShot(0);
                }
                if (null != aDetailVo.getDefectCount() && !aDetailVo.getDefectCount().isEmpty()) {
                    aDetail.setDefectCount(Integer.parseInt(aDetailVo.getDefectCount()));
                } else {
                    aDetail.setDefectCount(0);
                }
                if (null != aDetailVo.getMaterial01Id() && !aDetailVo.getMaterial01Id().isEmpty()) {
                    aDetail.setMaterial01Id(aDetailVo.getMaterial01Id());
                } else {
                    aDetail.setMaterial01Id(null);
                }
                if (null != aDetailVo.getMaterial01Amount() && !aDetailVo.getMaterial01Amount().isEmpty()) {
                    aDetail.setMaterial01Amount(new BigDecimal(aDetailVo.getMaterial01Amount()));
                } else if (null != aDetailVo.getMaterial01Id() && !aDetailVo.getMaterial01Id().isEmpty()) {
                    aDetail.setMaterial01Amount(new BigDecimal(0));
                } else {
                    aDetail.setMaterial01Amount(null);
                }
                aDetail.setMaterial01LotNo(aDetailVo.getMaterial01LotNo());
                if (null != aDetailVo.getMaterial01PurgedAmount() && !aDetailVo.getMaterial01PurgedAmount().isEmpty()) {
                    aDetail.setMaterial01PurgedAmount(new BigDecimal(aDetailVo.getMaterial01PurgedAmount()));
                } else if (null != aDetailVo.getMaterial01Id() && !aDetailVo.getMaterial01Id().isEmpty()) {
                    aDetail.setMaterial01PurgedAmount(new BigDecimal(0));
                } else {
                    aDetail.setMaterial01PurgedAmount(null);
                }
                if (null != aDetailVo.getMaterial02Id() && !aDetailVo.getMaterial02Id().isEmpty()) {
                    aDetail.setMaterial02Id(aDetailVo.getMaterial02Id());
                } else {
                    aDetail.setMaterial02Id(null);
                }
                if (null != aDetailVo.getMaterial02Amount() && !aDetailVo.getMaterial02Amount().isEmpty()) {
                    aDetail.setMaterial02Amount(new BigDecimal(aDetailVo.getMaterial02Amount()));
                } else if (null != aDetailVo.getMaterial02Id() && !aDetailVo.getMaterial02Id().isEmpty()){
                    aDetail.setMaterial02Amount(new BigDecimal(0));
                } else {
                    aDetail.setMaterial02Amount(null);
                }
                aDetail.setMaterial02LotNo(aDetailVo.getMaterial02LotNo());
                if (null != aDetailVo.getMaterial02PurgedAmount() && !aDetailVo.getMaterial02PurgedAmount().isEmpty()) {
                    aDetail.setMaterial02PurgedAmount(new BigDecimal(aDetailVo.getMaterial02PurgedAmount()));
                } else if (null != aDetailVo.getMaterial02Id() && !aDetailVo.getMaterial02Id().isEmpty()){
                    aDetail.setMaterial02PurgedAmount(new BigDecimal(0));
                } else {
                    aDetail.setMaterial02PurgedAmount(null);
                }
                if (null != aDetailVo.getMaterial03Id() && !aDetailVo.getMaterial03Id().isEmpty()) {
                    aDetail.setMaterial03Id(aDetailVo.getMaterial03Id());
                } else {
                    aDetail.setMaterial03Id(null);
                }
                if (null != aDetailVo.getMaterial03Amount() && !aDetailVo.getMaterial03Amount().isEmpty()) {
                    aDetail.setMaterial03Amount(new BigDecimal(aDetailVo.getMaterial03Amount()));
                } else if (null != aDetailVo.getMaterial03Id() && !aDetailVo.getMaterial03Id().isEmpty()) {
                    aDetail.setMaterial03Amount(new BigDecimal(0));
                } else {
                    aDetail.setMaterial03Amount(null);
                }
                aDetail.setMaterial03LotNo(aDetailVo.getMaterial03LotNo());
                if (null != aDetailVo.getMaterial03PurgedAmount() && !aDetailVo.getMaterial03PurgedAmount().isEmpty()) {
                    aDetail.setMaterial03PurgedAmount(new BigDecimal(aDetailVo.getMaterial03PurgedAmount()));
                } else if (null != aDetailVo.getMaterial03Id() && !aDetailVo.getMaterial03Id().isEmpty()) {
                    aDetail.setMaterial03PurgedAmount(new BigDecimal(0));
                } else {
                    aDetail.setMaterial03PurgedAmount(null);
                }

                aDetail.setUpdateDate(new Date());
                aDetail.setUpdateUserUuid(loginUser.getUserUuid());

                long quantity = 0L;
                if (null == oldDetails || oldDetails.isEmpty()) {
                    entityManager.persist(aDetail);
                    quantity = Long.parseLong(aDetailVo.getCompleteCount());
                } else {
                    entityManager.merge(aDetail);
                    quantity = Long.parseLong(String.valueOf(aDetail.getCompleteCount() - aDetailVo.getCompleteCountBeforeUpd()));
                }
                
                // 在庫管理更新 2017/11 Add S
                if (StringUtils.isEmpty(aDetail.getProcedureId())) continue;
                MstProcedure currentMstProcedure = mstProcedureService.getMstProcedureById(aDetail.getProcedureId());
                MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(aDetail.getComponentId(), currentMstProcedure.getProcedureCode());
                // 休日以外且つ工程番号（工番）が部品コードの中で最大の時だけ実施する
                if (aDailyReport.getNoRegistrationFlag() != 0) {
                    // 日報修正の場合登録した部品ロット関連情報から差分完成数を構成部品に計算する
                    if (null != oldDetails && !oldDetails.isEmpty()) {
                        TblComponentLotRelationVoList tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationList(null, aDailyReport.getId());
                        aDetailVo.setTblComponentLotRelationVoList(tblComponentLotRelationVoList);
                    }
                    BasicResponse basicResponse = tblStockService.doTblStock(
                        aDetailVo.getComponentCode(),
                        currentMstProcedure,
                        prevMstProcedure,
                        CommonConstants.STORE,
                        quantity,
                        DateFormat.getCurrentDateTime(),
                        machineDailyReportVo.getLotNumber(),
                        0,
                        null,
                        CommonConstants.SHIPMENT_NO,
                        aDetailVo.getTblComponentLotRelationVoList(),
                        loginUser.getUserUuid(),
                        loginUser.getLangId());

                    if (basicResponse.isError()) {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(basicResponse.getErrorMessage());

                        return response;
                    }
                }
                // 在庫管理更新 2017/11 Add E
                
                // 部品ロット関連テーブルを登録する
                if ((null == oldDetails || oldDetails.isEmpty()) && aDailyReport.getNoRegistrationFlag() != 0 
                        && aDetailVo.getTblComponentLotRelationVoList() != null && aDetailVo.getTblComponentLotRelationVoList().getTblComponentLotRelationVos() != null 
                        && !aDetailVo.getTblComponentLotRelationVoList().getTblComponentLotRelationVos().isEmpty()) {
                    TblComponentLot tblComponentLot = tblComponentLotService.getSingleResultTblComponentLot(aDetailVo.getComponentCode(), aDetailVo.getProcedureCode(), machineDailyReportVo.getLotNumber());
                    if (tblComponentLot != null) {
                        updateComponentLotRelation(aDetailVo, tblComponentLot, aDailyReport.getId(), loginUser.getUserUuid());
                    }
                }
                
                // 材料在庫更新 2017/12 Add S
                if (aDailyReport.getNoRegistrationFlag() != 0 && StringUtils.isNotEmpty(aDetailVo.getMaterial01Id())) {
                    BigDecimal material01Quantity;
                    if (null == oldDetails || oldDetails.isEmpty()) {
                        material01Quantity = aDetail.getMaterial01Amount().add(aDetail.getMaterial01PurgedAmount());
                    } else {
                        material01Quantity = aDetail.getMaterial01Amount().add(aDetail.getMaterial01PurgedAmount()).subtract(aDetailVo.getMaterial01AmountBeforeUpd()).subtract(aDetailVo.getMaterial01PurgedAmountBeforeUpd());
                    }
                    BasicResponse basicResponse = tblMaterialStockService.doMaterialStock(aDetailVo.getMaterial01Code(),
                        aDetailVo.getMaterial01LotNo(),
                        CommonConstants.DELIVERY,
                        material01Quantity,
                        DateFormat.getCurrentDateTime(),
                        aDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                        0,
                        null,
                        loginUser.getUserUuid(),
                        loginUser.getLangId(),
                        false);
                    
                    if (basicResponse.isError()) {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(basicResponse.getErrorMessage());

                        return response;
                    }
                }
                if (aDailyReport.getNoRegistrationFlag() != 0 && StringUtils.isNotEmpty(aDetailVo.getMaterial02Id())) {
                    BigDecimal material02Quantity;
                    if (null == oldDetails || oldDetails.isEmpty()) {
                        material02Quantity = aDetail.getMaterial02Amount().add(aDetail.getMaterial02PurgedAmount());
                    } else {
                        material02Quantity = aDetail.getMaterial02Amount().add(aDetail.getMaterial02PurgedAmount()).subtract(aDetailVo.getMaterial02AmountBeforeUpd()).subtract(aDetailVo.getMaterial02PurgedAmountBeforeUpd());
                    }
                    BasicResponse basicResponse = tblMaterialStockService.doMaterialStock(aDetailVo.getMaterial02Code(),
                        aDetailVo.getMaterial02LotNo(),
                        CommonConstants.DELIVERY,
                        material02Quantity,
                        DateFormat.getCurrentDateTime(),
                        aDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                        0,
                        null,
                        loginUser.getUserUuid(),
                        loginUser.getLangId(),
                        false);
                    
                    if (basicResponse.isError()) {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(basicResponse.getErrorMessage());

                        return response;
                    }
                }
                if (aDailyReport.getNoRegistrationFlag() != 0 && StringUtils.isNotEmpty(aDetailVo.getMaterial03Id())) {
                    BigDecimal material03Quantity;
                    if (null == oldDetails || oldDetails.isEmpty()) {
                        material03Quantity = aDetail.getMaterial03Amount().add(aDetail.getMaterial03PurgedAmount());
                    } else {
                        material03Quantity = aDetail.getMaterial03Amount().add(aDetail.getMaterial03PurgedAmount()).subtract(aDetailVo.getMaterial03AmountBeforeUpd()).subtract(aDetailVo.getMaterial03PurgedAmountBeforeUpd());
                    }
                    BasicResponse basicResponse = tblMaterialStockService.doMaterialStock(aDetailVo.getMaterial03Code(),
                        aDetailVo.getMaterial03LotNo(),
                        CommonConstants.DELIVERY,
                        material03Quantity,
                        DateFormat.getCurrentDateTime(),
                        aDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                        0,
                        null,
                        loginUser.getUserUuid(),
                        loginUser.getLangId(),
                        false);
                    
                    if (basicResponse.isError()) {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(basicResponse.getErrorMessage());

                        return response;
                    }
                }
                // 材料在庫更新 2017/12 Add E
            }
        }
        return response;
    }

    /**
     * 実稼動時間の計算
     *
     * @param tblMachineDailyReportVo
     */
    public void calculateMin(TblMachineDailyReportVo tblMachineDailyReportVo) {

        FileUtil fu = new FileUtil();

        long tMinutes = 0;
        tMinutes = FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(tblMachineDailyReportVo.getEndDatetimeStr()), fu.getDateTimeParseForDate(tblMachineDailyReportVo.getStartDatetimeStr()));

        // 生産時間を設定する(生産終了時刻　－　生産開始時刻)
        tblMachineDailyReportVo.setProducingTimeMinutes("" + tMinutes);

        tMinutes -= Long.valueOf(tblMachineDailyReportVo.getSuspendedTimeMinutes());

        // 実稼働時間を設定(生産終了時刻　－　生産開始時刻　－　中断時間)
        tblMachineDailyReportVo.setNetProducintTimeMinutes("" + tMinutes);

        int listSize = tblMachineDailyReportVo.getMachineDailyReportDetailVos().size();

        if (listSize > 0) {

            long avgMin = tMinutes / listSize;

            // 部品別の実稼動時間を設定する
            for (int i = 0; i < listSize; i++) {

                if (i == listSize - 1) {// 最終のレコードの場合

                    long tempMinutes = avgMin + tMinutes % listSize;

                    tblMachineDailyReportVo.getMachineDailyReportDetailVos().get(i).setComponentNetProducintTimeMinutes("" + tempMinutes);

                } else {

                    tblMachineDailyReportVo.getMachineDailyReportDetailVos().get(i).setComponentNetProducintTimeMinutes("" + avgMin);
                }
            }
        }
    }

    /**
     * 材料使用量の計算
     *
     * @param tblMachineDailyReportVo
     */
    public void calculateUsage(TblMachineDailyReportVo tblMachineDailyReportVo) {

        int listSize = tblMachineDailyReportVo.getMachineDailyReportDetailVos().size();

        if (listSize > 0) {

            for (int i = 0; i < listSize; i++) {

                TblMachineDailyReportDetailVo tblMachineDailyReportDetailVo = tblMachineDailyReportVo.getMachineDailyReportDetailVos().get(i);

                BigDecimal shotCount = new BigDecimal(FileUtil.getStrToNum(tblMachineDailyReportVo.getShotCount()));

                BigDecimal countPerShot = new BigDecimal(FileUtil.getStrToNum(tblMachineDailyReportDetailVo.getCountPerShot()));

                // 材料01の使用量を設定
                if (StringUtils.isNotEmpty(tblMachineDailyReportDetailVo.getMaterial01Id())) {

                    tblMachineDailyReportVo.getMachineDailyReportDetailVos().get(i).setMaterial01Amount(getMaterialAmount(shotCount, countPerShot, FileUtil.getNum(tblMachineDailyReportDetailVo.getNumerator01()), FileUtil.getNum(tblMachineDailyReportDetailVo.getDenominator01())));

                }

                // 材料02の使用量を設定
                if (StringUtils.isNotEmpty(tblMachineDailyReportDetailVo.getMaterial02Id())) {

                    tblMachineDailyReportVo.getMachineDailyReportDetailVos().get(i).setMaterial02Amount(getMaterialAmount(shotCount, countPerShot, FileUtil.getNum(tblMachineDailyReportDetailVo.getNumerator02()), FileUtil.getNum(tblMachineDailyReportDetailVo.getDenominator02())));

                }

                // 材料03の使用量を設定
                if (StringUtils.isNotEmpty(tblMachineDailyReportDetailVo.getMaterial03Id())) {

                    tblMachineDailyReportVo.getMachineDailyReportDetailVos().get(i).setMaterial03Amount(getMaterialAmount(shotCount, countPerShot, FileUtil.getNum(tblMachineDailyReportDetailVo.getNumerator03()), FileUtil.getNum(tblMachineDailyReportDetailVo.getDenominator03())));

                }

            }
        }
    }

    /**
     * 前日日報前日引継事項取得
     *
     * @param productionId
     * @param productionDate
     * @return
     */
    public TblMachineDailyReport getMessageForBeforetDay(String productionId, String productionDate) {

        String sql = "SELECT t FROM TblMachineDailyReport t WHERE t.tblMachineDailyReportPK.productionId = :productionId AND t.tblMachineDailyReportPK.productionDate < :productionDate AND t.noRegistrationFlag = 1 ORDER BY t.tblMachineDailyReportPK.productionDate DESC";

        Query query = entityManager.createQuery(sql);

        query.setParameter("productionId", productionId);
        query.setParameter("productionDate", DateFormat.strToDate(productionDate));

        List<TblMachineDailyReport> tblMachineDailyReportList = query.getResultList();

        if (tblMachineDailyReportList.size() > 0) {

            return tblMachineDailyReportList.get(0);
        } else {
            return null;
        }

    }

    /**
     * 前日日報材料ロット番号取得
     *
     * @param macReportId
     * @return
     */
    public List<TblMachineDailyReportDetail> getLotNumForBeforetDay(String macReportId) {

        if (!macReportId.isEmpty()) {
            String sql = "SELECT t FROM TblMachineDailyReportDetail t JOIN FETCH t.tblMachineDailyReport WHERE t.tblMachineDailyReport.id = :macReportId AND t.tblMachineDailyReport.noRegistrationFlag = 1";

            Query query = entityManager.createQuery(sql);

            query.setParameter("macReportId", macReportId);

            List<TblMachineDailyReportDetail> tblMachineDailyReportDetailList = query.getResultList();

            if (tblMachineDailyReportDetailList.size() > 0) {

                return tblMachineDailyReportDetailList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * 使用量を取得する
     *
     * @param shotCount
     * @param countPerShot
     * @param numerator
     * @param denominator
     * @return String
     */
    private String getMaterialAmount(BigDecimal shotCount, BigDecimal countPerShot, BigDecimal numerator, BigDecimal denominator) {

        if (0 == denominator.intValue()) {

            return "0";
        } else {

            BigDecimal materialAmount = shotCount.multiply(countPerShot).multiply(numerator).divide(denominator, 5, RoundingMode.HALF_DOWN);

            return materialAmount.toString();

        }

    }

    /**
     * 部品材料マスタ情報を取得
     *
     * @param tblMachineDailyReportDetailVo
     */
    public void getComponentMaterialInfo(TblMachineDailyReportDetailVo tblMachineDailyReportDetailVo) {

        String componentId = tblMachineDailyReportDetailVo.getComponentId();

        // 部品ID存在の場合
        if (StringUtils.isNotEmpty(componentId)) {

            String material01Id = tblMachineDailyReportDetailVo.getMaterial01Id();

            // 材料01ID存在の場合
            if (StringUtils.isNotEmpty(material01Id)) {

                List<MstComponentMaterial> MstComponentMaterialList = mstComponentMaterialService.getComponentmaterialInfoByCondition(componentId, material01Id, "");

                if (MstComponentMaterialList.size() > 0) {

                    MstComponentMaterial mstComponentMaterial = MstComponentMaterialList.get(0);

                    tblMachineDailyReportDetailVo.setNumerator01(mstComponentMaterial.getNumerator());

                    tblMachineDailyReportDetailVo.setDenominator01(mstComponentMaterial.getDenominator());

                    tblMachineDailyReportDetailVo.setMaterial01Code(mstComponentMaterial.getMstMaterial().getMaterialCode());
                }

            }

            String material02Id = tblMachineDailyReportDetailVo.getMaterial02Id();

            // 材料02ID存在の場合
            if (StringUtils.isNotEmpty(material02Id)) {

                List<MstComponentMaterial> MstComponentMaterialList = mstComponentMaterialService.getComponentmaterialInfoByCondition(componentId, material02Id, "");

                if (MstComponentMaterialList.size() > 0) {

                    MstComponentMaterial mstComponentMaterial = MstComponentMaterialList.get(0);

                    tblMachineDailyReportDetailVo.setNumerator02(mstComponentMaterial.getNumerator());

                    tblMachineDailyReportDetailVo.setDenominator02(mstComponentMaterial.getDenominator());

                    tblMachineDailyReportDetailVo.setMaterial02Code(mstComponentMaterial.getMstMaterial().getMaterialCode());
                }

            }

            String material03Id = tblMachineDailyReportDetailVo.getMaterial03Id();

            // 材料01ID存在の場合
            if (StringUtils.isNotEmpty(material03Id)) {

                List<MstComponentMaterial> MstComponentMaterialList = mstComponentMaterialService.getComponentmaterialInfoByCondition(componentId, material03Id, "");

                if (MstComponentMaterialList.size() > 0) {

                    MstComponentMaterial mstComponentMaterial = MstComponentMaterialList.get(0);

                    tblMachineDailyReportDetailVo.setNumerator03(mstComponentMaterial.getNumerator());

                    tblMachineDailyReportDetailVo.setDenominator03(mstComponentMaterial.getDenominator());

                    tblMachineDailyReportDetailVo.setMaterial03Code(mstComponentMaterial.getMstMaterial().getMaterialCode());
                }

            }

        }

    }
    
    /**
     * 材料ロット番号を前日の機械日報から引き継ぐ
     */
    private void setMaterialLotNumFromBeforeDailyReport(TblMachineDailyReportDetailVo aDetailVo, List<TblMachineDailyReportDetail> lotNumList) {
        if (lotNumList != null && !lotNumList.isEmpty()) {
            for (TblMachineDailyReportDetail beforeDetail : lotNumList) {
                if (aDetailVo.getComponentId().equals(beforeDetail.getComponentId())) {
                    if (aDetailVo.getMaterial01Id() != null && !"".equals(aDetailVo.getMaterial01Id())) {
                        if (aDetailVo.getMaterial01Id().equals(beforeDetail.getMaterial01Id())) {
                            aDetailVo.setMaterial01LotNo(beforeDetail.getMaterial01LotNo());
                        } else if (aDetailVo.getMaterial01Id().equals(beforeDetail.getMaterial02Id())) {
                            aDetailVo.setMaterial01LotNo(beforeDetail.getMaterial02LotNo());
                        } else if (aDetailVo.getMaterial01Id().equals(beforeDetail.getMaterial03Id())) {
                            aDetailVo.setMaterial01LotNo(beforeDetail.getMaterial03LotNo());
                        }
                    }

                    if (aDetailVo.getMaterial02Id() != null && !"".equals(aDetailVo.getMaterial02Id())) {
                        if (aDetailVo.getMaterial02Id().equals(beforeDetail.getMaterial01Id())) {
                            aDetailVo.setMaterial02LotNo(beforeDetail.getMaterial01LotNo());
                        } else if (aDetailVo.getMaterial02Id().equals(beforeDetail.getMaterial02Id())) {
                            aDetailVo.setMaterial02LotNo(beforeDetail.getMaterial02LotNo());
                        } else if (aDetailVo.getMaterial02Id().equals(beforeDetail.getMaterial03Id())) {
                            aDetailVo.setMaterial02LotNo(beforeDetail.getMaterial03LotNo());
                        }
                    }

                    if (aDetailVo.getMaterial03Id() != null && !"".equals(aDetailVo.getMaterial03Id())) {
                        if (aDetailVo.getMaterial03Id().equals(beforeDetail.getMaterial01Id())) {
                            aDetailVo.setMaterial03LotNo(beforeDetail.getMaterial01LotNo());
                        } else if (aDetailVo.getMaterial03Id().equals(beforeDetail.getMaterial02Id())) {
                            aDetailVo.setMaterial03LotNo(beforeDetail.getMaterial02LotNo());
                        } else if (aDetailVo.getMaterial03Id().equals(beforeDetail.getMaterial03Id())) {
                            aDetailVo.setMaterial03LotNo(beforeDetail.getMaterial03LotNo());
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 生産終了時 機械日報データを登録 差分の機械日報を自動作成
     *
     * @param tblProduction
     * @param tblProductionDetails
     * @param loginUser
     * @return TblMachineDailyReport tblMachineDailyReport
     */
    @Transactional
    public TblMachineDailyReport finishProduct(TblProduction tblProduction, List<TblProductionDetail> tblProductionDetails, LoginUser loginUser) {

        FileUtil fu = new FileUtil();
        int shotCount = tblProduction.getShotCount();
        int disposedShotCount = tblProduction.getDisposedShotCount();
        Map<String, TblProductionDetail> completeCounts = new HashMap<>();
        for (TblProductionDetail tblProductionDetail : tblProductionDetails) {
            completeCounts.put(tblProductionDetail.getComponentId(), tblProductionDetail);
        }

        TblMachineDailyReport tblMachineDailyReport = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        String strProductionEndDate = sdf.format(tblProduction.getEndDatetime());
        try {
            List<TblMachineDailyReport> oldMachineDailyReports = entityManager.createNamedQuery("TblMachineDailyReport.findByProductionIdAndDate")
                    .setParameter("productionId", tblProduction.getId())
                    .setParameter("productionDate", fu.getDateParseForDate(strProductionEndDate))
                    .getResultList();

            if (null != oldMachineDailyReports && !oldMachineDailyReports.isEmpty()) {
                tblMachineDailyReport = oldMachineDailyReports.get(0);
            } else {
                tblMachineDailyReport = new TblMachineDailyReport();
                tblMachineDailyReport.setCreateDate(new Date());
                tblMachineDailyReport.setCreateUserUuid(loginUser.getUserUuid());
                tblMachineDailyReport.setId(IDGenerator.generate());
                TblMachineDailyReportPK pk = new TblMachineDailyReportPK();
                pk.setProductionId(tblProduction.getId());
                pk.setProductionDate(sdf.parse(strProductionEndDate));
                tblMachineDailyReport.setTblMachineDailyReportPK(pk);

                tblMachineDailyReport.setMachineUuid(tblProduction.getMachineUuid());
                TblMachineDailyReport dailyInfo = getMessageForBeforetDay(pk.getProductionId(), strProductionEndDate);
                tblMachineDailyReport.setMessageForNextDay(dailyInfo == null ? "" : dailyInfo.getMessageForNextDay());
                tblMachineDailyReport.setNetProducintTimeMinutes(tblProduction.getNetProducintTimeMinutes());
                tblMachineDailyReport.setNoRegistrationFlag(1);//登録要
                tblMachineDailyReport.setProducingTimeMinutes(tblProduction.getProducingTimeMinutes());
                tblMachineDailyReport.setReporterUuid(loginUser.getUserUuid());
                CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
                String businessStartTime = cnfSystem.getConfigValue().length() == 4 ? "0" + cnfSystem.getConfigValue() : cnfSystem.getConfigValue();
                if (strProductionEndDate.equals(sdf.format(tblProduction.getStartDatetime()))){//当日终了,只有一天
                    tblMachineDailyReport.setStartDatetime(tblProduction.getStartDatetime());
                }else{
                    tblMachineDailyReport.setStartDatetime(fu.getDateTimeParseForDate(strProductionEndDate + " " + businessStartTime + DateFormat.TIME_SECONDS));
                }                
                tblMachineDailyReport.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), fu.getDateTimeParseForDate(strProductionEndDate + " " + businessStartTime + DateFormat.TIME_SECONDS)));
                tblMachineDailyReport.setEndDatetime(tblProduction.getEndDatetime());
                tblMachineDailyReport.setEndDatetimeStz(tblProduction.getEndDatetimeStz());
                tblMachineDailyReport.setSuspendedTimeMinutes(tblProduction.getSuspendedTimeMinutes());
            }

            List<TblMachineDailyReport> machineDailyReports = entityManager.createQuery("SELECT t FROM TblMachineDailyReport t WHERE t.tblMachineDailyReportPK.productionId = :productionId And t.tblMachineDailyReportPK.productionDate != :productionDate")
                    .setParameter("productionId", tblProduction.getId())
                    .setParameter("productionDate", fu.getDateParseForDate(strProductionEndDate))
                    .getResultList();
            if (null != machineDailyReports && !machineDailyReports.isEmpty()) {
                for (TblMachineDailyReport machineDailyReport : machineDailyReports) {
                    shotCount -= machineDailyReport.getShotCount();
                    disposedShotCount -= machineDailyReport.getDisposedShotCount();

                    List<TblMachineDailyReportDetail> machineDailyReportDetails = entityManager.createNamedQuery("TblMachineDailyReportDetail.findByMacReportId")
                            .setParameter("macReportId", machineDailyReport.getId())
                            .getResultList();
                    for (TblMachineDailyReportDetail machineDailyReportDetail : machineDailyReportDetails) {
                        TblProductionDetail tmpProductionDetail = completeCounts.get(machineDailyReportDetail.getComponentId());
                        tmpProductionDetail.setCompleteCount(tmpProductionDetail.getCompleteCount() - machineDailyReportDetail.getCompleteCount());
                        tmpProductionDetail.setCountPerShot(tmpProductionDetail.getCountPerShot() - machineDailyReportDetail.getCountPerShot());
                        tmpProductionDetail.setDefectCount(tmpProductionDetail.getDefectCount() - machineDailyReportDetail.getDefectCount());
                        completeCounts.put(machineDailyReportDetail.getComponentId(), tmpProductionDetail);
                    }
                }                
            }
            TblMachineDailyReportVo machineDailyReportVo = new TblMachineDailyReportVo();
            machineDailyReportVo.setShotCount(String.valueOf(shotCount));
            machineDailyReportVo.setShotCountBeforeUpd(tblMachineDailyReport.getShotCount());
            machineDailyReportVo.setDisposedShotCount(String.valueOf(disposedShotCount));
            machineDailyReportVo.setDisposedShotCountBeforeUpd(tblMachineDailyReport.getDisposedShotCount());
            tblMachineDailyReport.setShotCount(shotCount);
            tblMachineDailyReport.setDisposedShotCount(disposedShotCount);

            tblMachineDailyReport.setUpdateDate(new Date());
            tblMachineDailyReport.setUpdateUserUuid(loginUser.getUserUuid());

            machineDailyReportVo.setMachineDailyReportDetailVos(new ArrayList<>());
            BeanCopyUtil.copyFields(tblMachineDailyReport, machineDailyReportVo);
            machineDailyReportVo.setProductionId(tblMachineDailyReport.getTblMachineDailyReportPK().getProductionId());
            machineDailyReportVo.setProductionDate(tblMachineDailyReport.getTblMachineDailyReportPK().getProductionDate());
            for (TblProductionDetail tblProductionDetail : tblProductionDetails) {
                TblMachineDailyReportDetailVo machineDailyReportDetailVo = new TblMachineDailyReportDetailVo();
                BeanCopyUtil.copyFields(tblProductionDetail, machineDailyReportDetailVo);
                machineDailyReportVo.getMachineDailyReportDetailVos().add(machineDailyReportDetailVo);
            }
            machineDailyReportVo.setEndDatetimeStr(fu.getDateTimeFormatForStr(tblMachineDailyReport.getEndDatetime()));
            machineDailyReportVo.setStartDatetimeStr(fu.getDateTimeFormatForStr(tblMachineDailyReport.getStartDatetime()));
            // 中断時間を計算する
            calculateSuspendedMin(machineDailyReportVo, loginUser);
            // 実稼動時間を計算
            calculateMin(machineDailyReportVo);
            
            machineDailyReportVo.setNetProducintTimeMinutesBeforeUpd(tblMachineDailyReport.getNetProducintTimeMinutes() == null ? 0 : tblMachineDailyReport.getNetProducintTimeMinutes());
            tblMachineDailyReport.setProducingTimeMinutes(Integer.parseInt(machineDailyReportVo.getProducingTimeMinutes()));
            tblMachineDailyReport.setNetProducintTimeMinutes(Integer.parseInt(machineDailyReportVo.getNetProducintTimeMinutes()));
            tblMachineDailyReport.setSuspendedTimeMinutes(Integer.parseInt(machineDailyReportVo.getSuspendedTimeMinutes()));

            if (null != oldMachineDailyReports && !oldMachineDailyReports.isEmpty()) {
                entityManager.merge(tblMachineDailyReport);
            } else {
                entityManager.persist(tblMachineDailyReport);
            }
            
            // 金型/設備マスタ更新(最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数)
            if (tblProduction.getMoldUuid() != null && !"".equals(tblProduction.getMoldUuid())) {
                mstMoldService.updateMstMoldForDailyReport(tblProduction, machineDailyReportVo, loginUser);
            }
            mstMachineService.updateMstMachineForDailyReport(tblProduction, machineDailyReportVo, loginUser);

            if (null != oldMachineDailyReports && !oldMachineDailyReports.isEmpty()) {
                List<TblMachineDailyReportDetail> machineDailyReportDetails = entityManager.createNamedQuery("TblMachineDailyReportDetail.findByMacReportId")
                        .setParameter("macReportId", oldMachineDailyReports.get(0).getId())
                        .getResultList();
                for (TblMachineDailyReportDetail machineDailyReportDetail : machineDailyReportDetails) {
                    // 機械日報部品毎完成数更新された場合、集計テーブル更新する
                    // 機械日報差分日報更新される場合
                    int updCompleteCount = machineDailyReportDetail.getCompleteCount() - completeCounts.get(machineDailyReportDetail.getComponentId()).getCompleteCount();
                    if (updCompleteCount != 0) {
                        calculateProductionPeriodForUpdate(tblMachineDailyReport, machineDailyReportDetail, updCompleteCount, loginUser);
                    }
                    machineDailyReportDetail.setCompleteCount(completeCounts.get(machineDailyReportDetail.getComponentId()).getCompleteCount());
                    machineDailyReportDetail.setCountPerShot(completeCounts.get(machineDailyReportDetail.getComponentId()).getCountPerShot());
                    machineDailyReportDetail.setDefectCount(completeCounts.get(machineDailyReportDetail.getComponentId()).getDefectCount());
                    machineDailyReportDetail.setUpdateDate(new Date());
                    machineDailyReportDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.merge(machineDailyReportDetail);
                    
                    // 在庫管理更新 2017/11 Add S
                    // 休日以外且つ
                    if (machineDailyReportDetail.getMstProcedure() != null && tblMachineDailyReport.getNoRegistrationFlag() != 0) {
                        MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(machineDailyReportDetail.getComponentId(), machineDailyReportDetail.getMstProcedure().getProcedureCode());
                        // 部品ロット関連テーブルから構成部品リスト取得
                        TblComponentLotRelationVoList tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationList(null, oldMachineDailyReports.get(0).getId());
                        completeCounts.get(machineDailyReportDetail.getComponentId()).setTblComponentLotRelationVoList(tblComponentLotRelationVoList);
                        
                        tblStockService.doTblStock(
                            machineDailyReportDetail.getMstComponent().getComponentCode(),
                            machineDailyReportDetail.getMstProcedure(),
                            prevMstProcedure,
                            CommonConstants.STORE,
                            updCompleteCount * -1,
                            DateFormat.getCurrentDateTime(),
                            tblProduction.getLotNumber(),
                            0,
                            null,
                            CommonConstants.SHIPMENT_NO,
                            completeCounts.get(machineDailyReportDetail.getComponentId()).getTblComponentLotRelationVoList(),
                            loginUser.getUserUuid(),
                            loginUser.getLangId());
                    }
                    // 在庫管理更新 2017/11 Add E
                }
            } else {
                for (int i = 0, j = tblProductionDetails.size(); i < j; i++) {
                    TblProductionDetail tblProductionDetail = tblProductionDetails.get(i);
                    TblMachineDailyReportDetail machineDailyReportDetail = new TblMachineDailyReportDetail();
                    machineDailyReportDetail.setCompleteCount(completeCounts.get(tblProductionDetail.getComponentId()).getCompleteCount());
                    machineDailyReportDetail.setCountPerShot(completeCounts.get(tblProductionDetail.getComponentId()).getCountPerShot());
                    machineDailyReportDetail.setDefectCount(completeCounts.get(tblProductionDetail.getComponentId()).getDefectCount());

                    machineDailyReportDetail.setId(IDGenerator.generate());
                    TblMachineDailyReportDetailPK pk = new TblMachineDailyReportDetailPK();
                    pk.setMacReportId(tblMachineDailyReport.getId());
                    pk.setProductionDetailId(tblProductionDetail.getId());
                    machineDailyReportDetail.setTblMachineDailyReportDetailPK(pk);
                    machineDailyReportDetail.setComponentId(tblProductionDetail.getComponentId());
                    machineDailyReportDetail.setProcedureId(tblProductionDetail.getProcedureId());
                    machineDailyReportDetail.setComponentNetProducintTimeMinutes(Integer.parseInt(machineDailyReportVo.getMachineDailyReportDetailVos().get(0).getComponentNetProducintTimeMinutes()));
                    if (i == j - 1 && null != machineDailyReportVo.getNetProducintTimeMinutes()
                            && Integer.parseInt(machineDailyReportVo.getNetProducintTimeMinutes()) % tblProductionDetails.size() != 0) {
                        machineDailyReportDetail.setComponentNetProducintTimeMinutes(machineDailyReportDetail.getComponentNetProducintTimeMinutes() + Integer.parseInt(machineDailyReportVo.getNetProducintTimeMinutes()) % tblProductionDetails.size());
                    }
                    machineDailyReportDetail.setCountPerShot(tblProductionDetail.getCountPerShot());
                    machineDailyReportDetail.setDefectCount(tblProductionDetail.getDefectCount());

                    if (null != tblProductionDetail.getMaterial01Id() && !tblProductionDetail.getMaterial01Id().isEmpty()) {
                        machineDailyReportDetail.setMaterial01Id(tblProductionDetail.getMaterial01Id());
                        machineDailyReportDetail.setMaterial01Amount(tblProductionDetail.getMaterial01Amount());
                        machineDailyReportDetail.setMaterial01LotNo(tblProductionDetail.getMaterial01LotNo());
                        machineDailyReportDetail.setMaterial01PurgedAmount(tblProductionDetail.getMaterial01PurgedAmount());
                    } else {
                        machineDailyReportDetail.setMaterial01Id(null);
                        machineDailyReportDetail.setMaterial01Amount(null);
                        machineDailyReportDetail.setMaterial01LotNo(null);
                        machineDailyReportDetail.setMaterial01PurgedAmount(null);
                    }
                    if (null != tblProductionDetail.getMaterial02Id() && !tblProductionDetail.getMaterial02Id().isEmpty()) {
                        machineDailyReportDetail.setMaterial02Id(tblProductionDetail.getMaterial02Id());
                        machineDailyReportDetail.setMaterial02Amount(tblProductionDetail.getMaterial02Amount());
                        machineDailyReportDetail.setMaterial02LotNo(tblProductionDetail.getMaterial02LotNo());
                        machineDailyReportDetail.setMaterial02PurgedAmount(tblProductionDetail.getMaterial02PurgedAmount());
                    } else {
                        machineDailyReportDetail.setMaterial02Id(null);
                        machineDailyReportDetail.setMaterial02Amount(null);
                        machineDailyReportDetail.setMaterial02LotNo(null);
                        machineDailyReportDetail.setMaterial02PurgedAmount(null);
                    }
                    if (null != tblProductionDetail.getMaterial03Id() && !tblProductionDetail.getMaterial03Id().isEmpty()) {
                        machineDailyReportDetail.setMaterial03Id(tblProductionDetail.getMaterial03Id());
                        machineDailyReportDetail.setMaterial03Amount(tblProductionDetail.getMaterial03Amount());
                        machineDailyReportDetail.setMaterial03LotNo(tblProductionDetail.getMaterial03LotNo());
                        machineDailyReportDetail.setMaterial03PurgedAmount(tblProductionDetail.getMaterial03PurgedAmount());
                    } else {
                        machineDailyReportDetail.setMaterial03Id(null);
                        machineDailyReportDetail.setMaterial03Amount(null);
                        machineDailyReportDetail.setMaterial03LotNo(null);
                        machineDailyReportDetail.setMaterial03PurgedAmount(null);
                    }

                    // 前日機械日報明細の材料ロット番号取得
                    TblMachineDailyReport dailyInfo = getMessageForBeforetDay(tblProduction.getId(), DateFormat.dateToStr(tblProduction.getEndDatetime(), DateFormat.DATETIME_FORMAT));
                    List<TblMachineDailyReportDetail> lotNumList = getLotNumForBeforetDay(dailyInfo == null ? "" : dailyInfo.getId());
                    
                    // 材料ロット番号を前日の機械日報から引き継ぐ
                    TblMachineDailyReportDetailVo aDetailVo = new TblMachineDailyReportDetailVo();
                    aDetailVo.setComponentId(machineDailyReportDetail.getComponentId());
                    aDetailVo.setMaterial01Id(machineDailyReportDetail.getMaterial01Id());
                    aDetailVo.setMaterial02Id(machineDailyReportDetail.getMaterial02Id());
                    aDetailVo.setMaterial03Id(machineDailyReportDetail.getMaterial03Id());
                    setMaterialLotNumFromBeforeDailyReport(aDetailVo, lotNumList);
                    machineDailyReportDetail.setMaterial01LotNo(aDetailVo.getMaterial01LotNo());
                    machineDailyReportDetail.setMaterial02LotNo(aDetailVo.getMaterial02LotNo());
                    machineDailyReportDetail.setMaterial03LotNo(aDetailVo.getMaterial03LotNo());
                    
                    
                    machineDailyReportDetail.setCreateDate(new Date());
                    machineDailyReportDetail.setCreateUserUuid(loginUser.getUserUuid());
                    machineDailyReportDetail.setUpdateDate(new Date());
                    machineDailyReportDetail.setUpdateUserUuid(loginUser.getUserUuid());

                    entityManager.persist(machineDailyReportDetail);
                    
                    // 在庫管理更新 2017/11 Add S
                    // 休日以外且つ工程番号（工番）が部品コードの中で最大の時だけ実施する
                    if (tblProductionDetail.getMstProcedure() != null && tblMachineDailyReport.getNoRegistrationFlag() != 0) {
                        MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(tblProductionDetail.getComponentId(), tblProductionDetail.getMstProcedure().getProcedureCode());
                        tblStockService.doTblStock(
                            tblProductionDetail.getComponentCode(),
                            tblProductionDetail.getMstProcedure(),
                            prevMstProcedure,
                            CommonConstants.STORE,
                            machineDailyReportDetail.getCompleteCount(),
                            DateFormat.getCurrentDateTime(),
                            tblProduction.getLotNumber(),
                            0,
                            null,
                            CommonConstants.SHIPMENT_NO,
                            tblProductionDetail.getTblComponentLotRelationVoList(),
                            loginUser.getUserUuid(),
                            loginUser.getLangId()
                        );
                    }
                    // 在庫管理更新 2017/11 Add E
                    
                    // 部品ロット関連テーブルを登録する
                    if (tblProductionDetail.getMstProcedure() != null && tblProductionDetail.getTblComponentLotRelationVoList() != null && tblProductionDetail.getTblComponentLotRelationVoList().getTblComponentLotRelationVos() != null && !tblProductionDetail.getTblComponentLotRelationVoList().getTblComponentLotRelationVos().isEmpty()) {
                        TblComponentLot tblComponentLot = tblComponentLotService.getSingleResultTblComponentLot(tblProductionDetail.getMstComponent().getComponentCode(), tblProductionDetail.getMstProcedure().getProcedureCode(), tblProduction.getLotNumber());
                        if (tblComponentLot != null) {
                            tblProductionService.updateComponentLotRelation(tblProductionDetail, tblComponentLot, tblMachineDailyReport.getId(), loginUser.getUserUuid());
                        }
                    }
                    
                    // 材料在庫更新 2017/12 Add S
                    if (tblMachineDailyReport.getNoRegistrationFlag() != 0 && StringUtils.isNotEmpty(machineDailyReportDetail.getMaterial01Id())) {
                        tblMaterialStockService.doMaterialStock(tblProductionDetail.getMaterial01Code(),
                            machineDailyReportDetail.getMaterial01LotNo(),
                            CommonConstants.DELIVERY,
                            machineDailyReportDetail.getMaterial01Amount().add(machineDailyReportDetail.getMaterial01PurgedAmount()),
                            DateFormat.getCurrentDateTime(),
                            machineDailyReportDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);
                    }
                    if (tblMachineDailyReport.getNoRegistrationFlag() != 0 && StringUtils.isNotEmpty(machineDailyReportDetail.getMaterial02Id())) {
                        tblMaterialStockService.doMaterialStock(tblProductionDetail.getMaterial02Code(),
                            machineDailyReportDetail.getMaterial02LotNo(),
                            CommonConstants.DELIVERY,
                            machineDailyReportDetail.getMaterial02Amount().add(machineDailyReportDetail.getMaterial02PurgedAmount()),
                            DateFormat.getCurrentDateTime(),
                            machineDailyReportDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);
                    }
                    if (tblMachineDailyReport.getNoRegistrationFlag() != 0 && StringUtils.isNotEmpty(machineDailyReportDetail.getMaterial03Id())) {
                        tblMaterialStockService.doMaterialStock(tblProductionDetail.getMaterial03Code(),
                            machineDailyReportDetail.getMaterial03LotNo(),
                            CommonConstants.DELIVERY,
                            machineDailyReportDetail.getMaterial03Amount().add(machineDailyReportDetail.getMaterial03PurgedAmount()),
                            DateFormat.getCurrentDateTime(),
                            machineDailyReportDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);
                    }
                    // 材料在庫更新 2017/12 Add E
                }
            }

        } catch (ParseException ex) {
            Logger.getLogger(TblMachineDailyReportService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return tblMachineDailyReport;
    }

    /**
     * getMachineDailyReportsCountByProductionId
     *
     * @param productionId
     * @return
     */
    public Long getMachineDailyReportsCountByProductionId(String productionId) {
        List list = entityManager.createQuery("SELECT count(t) FROM TblMachineDailyReport t WHERE t.tblMachineDailyReportPK.productionId = :productionId")
                .setParameter("productionId", productionId)
                .getResultList();
        return (Long) list.get(0);
    }

    /**
     * 中断時間を計算する
     *
     * @param tblMachineDailyReportVo
     * @param loginUser
     *
     */
    public void calculateSuspendedMin(TblMachineDailyReportVo tblMachineDailyReportVo, LoginUser loginUser) {

        FileUtil fu = new FileUtil();

        int sumSuspendedTimeMinute = 0;
        
        if(StringUtils.isEmpty(tblMachineDailyReportVo.getProductionId())){
            
            tblMachineDailyReportVo.setSuspendedTimeMinutes("" + sumSuspendedTimeMinute);
            return;
        }
      
        TblProductionSuspensionList tblProductionSuspensionList = tblProductionSuspensionlService.getProductionSuspensionListByProductionId(tblMachineDailyReportVo.getProductionId(), loginUser);

        List<TblProductionSuspensionVo> tblProductionSuspensionVoList = tblProductionSuspensionList.getTblProductionSuspensionVo();

        if (null != tblProductionSuspensionVoList && tblProductionSuspensionVoList.size() > 0) {

            for (TblProductionSuspensionVo tblProductionSuspensionVo : tblProductionSuspensionVoList) {

                String suspensionStartTime = tblProductionSuspensionVo.getStartDatetimeStr();

                String suspensionEndTime = tblProductionSuspensionVo.getEndDatetimeStr();

                // 中断終了時刻がない場合
                if (StringUtils.isEmpty(suspensionEndTime)) {

                    // 日報開始時刻＞中断開始時刻の場合
                    if (tblMachineDailyReportVo.getStartDatetimeStr().compareTo(suspensionStartTime) > 0) {

                        sumSuspendedTimeMinute += FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(tblMachineDailyReportVo.getEndDatetimeStr()), fu.getDateTimeParseForDate(tblMachineDailyReportVo.getStartDatetimeStr()));

                    } else if (tblMachineDailyReportVo.getEndDatetimeStr().compareTo(suspensionStartTime) > 0) {// 日報開始時刻＜＝中断開始時刻 且つ　日報終了時刻＞中断開始時刻の場合

                        sumSuspendedTimeMinute += FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(tblMachineDailyReportVo.getEndDatetimeStr()), fu.getDateTimeParseForDate(suspensionStartTime));
                    }

                } else// 中断終了時刻が存在の場合
                // 日報開始時刻＞中断開始時刻 且つ 日報終了時刻＜中断終了時刻
                if (tblMachineDailyReportVo.getStartDatetimeStr().compareTo(suspensionStartTime) > 0
                        && tblMachineDailyReportVo.getEndDatetimeStr().compareTo(suspensionEndTime) < 0) {

                    sumSuspendedTimeMinute += FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(tblMachineDailyReportVo.getEndDatetimeStr()), fu.getDateTimeParseForDate(tblMachineDailyReportVo.getStartDatetimeStr()));

                } else if (tblMachineDailyReportVo.getStartDatetimeStr().compareTo(suspensionStartTime) < 0
                        && tblMachineDailyReportVo.getEndDatetimeStr().compareTo(suspensionEndTime) > 0) {// 日報開始時刻＜中断開始時刻 且つ 日報終了時刻＞中断終了時刻

                    sumSuspendedTimeMinute += FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(suspensionEndTime), fu.getDateTimeParseForDate(suspensionStartTime));

                } else if (tblMachineDailyReportVo.getEndDatetimeStr().compareTo(suspensionStartTime) < 0) {// 日報終了時刻＜中断開始時刻

//                    sumSuspendedTimeMinute += FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(tblMachineDailyReportVo.getEndDatetimeStr()), fu.getDateTimeParseForDate(tblMachineDailyReportVo.getStartDatetimeStr()));
                    sumSuspendedTimeMinute += 0;

                } else if (tblMachineDailyReportVo.getEndDatetimeStr().compareTo(suspensionStartTime) > 0 && tblMachineDailyReportVo.getStartDatetimeStr().compareTo(suspensionStartTime) < 0
                        && tblMachineDailyReportVo.getEndDatetimeStr().compareTo(suspensionEndTime) < 0) {// 日報終了時刻＞中断開始時刻　且つ　日報開始時刻＜中断開始時刻 且つ 日報終了時刻＜中断終了時刻

                    sumSuspendedTimeMinute += FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(tblMachineDailyReportVo.getEndDatetimeStr()), fu.getDateTimeParseForDate(suspensionStartTime));

                } else if (tblMachineDailyReportVo.getStartDatetimeStr().compareTo(suspensionEndTime) > 0) {// 日報1開始時刻＞中断終了時刻

//                    sumSuspendedTimeMinute += FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(tblMachineDailyReportVo.getEndDatetimeStr()), fu.getDateTimeParseForDate(tblMachineDailyReportVo.getStartDatetimeStr()));
                    
                    sumSuspendedTimeMinute += 0;

                } else if (tblMachineDailyReportVo.getStartDatetimeStr().compareTo(suspensionEndTime) < 0 && tblMachineDailyReportVo.getStartDatetimeStr().compareTo(suspensionStartTime) > 0
                        && tblMachineDailyReportVo.getEndDatetimeStr().compareTo(suspensionEndTime) > 0) {// 日報1開始時刻＜中断終了時刻 且つ　日報開始時刻＞中断開始時刻 且つ 日報終了時刻＞中断終了時刻

                    sumSuspendedTimeMinute += FileUtil.getDatePoorMinute(fu.getDateTimeParseForDate(suspensionEndTime), fu.getDateTimeParseForDate(tblMachineDailyReportVo.getStartDatetimeStr()));
                }
            }
        }

        tblMachineDailyReportVo.setSuspendedTimeMinutes("" + sumSuspendedTimeMinute);

    }

    /**
     * checkDateTime 日報登録の場合、開始時刻と終了時刻のチェックする
     *
     * @param tblMachineDailyReportVo
     * @param loginUser
     * @return
     */
    public boolean checkDateTime(TblMachineDailyReportVo tblMachineDailyReportVo, LoginUser loginUser) {

        boolean res = false;

        // 非営業日の場合
        if ("0".equals(tblMachineDailyReportVo.getNoRegistrationFlag())) {
            return res;
        }

        Query query = entityManager.createNamedQuery("TblMachineDailyReport.findByProductionId");

        query.setParameter("productionId", tblMachineDailyReportVo.getProductionId());

        List<TblMachineDailyReport> list = query.getResultList();
        boolean isFirstDay = false;
        boolean isPEndLastDay = false;
        boolean isPNotEndLastDay = false;
        if (!list.isEmpty()) {
            List<Date> tempSDateList = new ArrayList();
            List<Date> tempEDateList = new ArrayList();
            for (TblMachineDailyReport tblMachineDailyReport : list) {
                tempSDateList.add(tblMachineDailyReport.getStartDatetime());
                tempEDateList.add(tblMachineDailyReport.getEndDatetime());
            }
            Date firstReportDate = Collections.min(tempSDateList);
            Date lastReportDate = Collections.max(tempEDateList);
            if (tblMachineDailyReportVo.getProductionDateStr().compareTo(DateFormat.dateToStr(firstReportDate, DateFormat.DATE_FORMAT)) == 0) {
                isFirstDay = true;
            }
            if (tblMachineDailyReportVo.getProductionDateStr().compareTo(DateFormat.dateToStr(lastReportDate, DateFormat.DATE_FORMAT)) == 0) {
                isPEndLastDay = true;
            } else if (tblMachineDailyReportVo.getProductionDateStr().compareTo(DateFormat.dateToStr(DateFormat.getBeforeDay(lastReportDate), DateFormat.DATE_FORMAT)) == 0) {
                isPNotEndLastDay = true;
            }
        }
        
        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
        // 最初の機械日報の場合
        if (list.isEmpty() || isFirstDay) {

            // 生産実績の開始時刻を取得する
            TblProduction tblProduction = tblProductionService.getProductionSingleById(tblMachineDailyReportVo.getProductionId());

            if (null != tblProduction) {

                String productionStartTime = DateFormat.dateToStr(tblProduction.getStartDatetime(), DateFormat.DATETIME_FORMAT);

                // 日報開始時刻＜生産開始時刻の場合
                if (tblMachineDailyReportVo.getStartDatetimeStr().compareTo(productionStartTime) < 0) {

                    tblMachineDailyReportVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_actual_work_datetime_order"));

                    return true;
                }
                
                if (tblProduction.getEndDatetime() != null && isPEndLastDay) {
                    String productionEndTime = DateFormat.dateToStr(tblProduction.getEndDatetime(), DateFormat.DATETIME_FORMAT);

                    // 日報終了時刻＞生産終了時刻の場合
                    if (tblMachineDailyReportVo.getEndDatetimeStr().compareTo(productionEndTime) > 0) {

                        tblMachineDailyReportVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_next_day_producint_work_datetime_order"));

                        return true;
                    }
                } else {
                    if (chkEndDateTimeAfterSysEnd(cnfSystem, tblMachineDailyReportVo, loginUser)) {
                        return true;
                    }
                }

            }

        } else {// 生産実績の２番目以降の機械日報の場合    

            StringBuilder startDateTimeBuild = new StringBuilder();
            
            if (cnfSystem.getConfigValue().length() == 4) {

                startDateTimeBuild.append(tblMachineDailyReportVo.getProductionDateStr()).append(" 0").append(cnfSystem.getConfigValue()).append(CONST_SECOND);
            } else {

                startDateTimeBuild.append(tblMachineDailyReportVo.getProductionDateStr()).append(" ").append(cnfSystem.getConfigValue()).append(CONST_SECOND);
            }

            // 業務開始時刻
            String startDateTimeConst = startDateTimeBuild.toString();

            if (tblMachineDailyReportVo.getStartDatetimeStr().compareTo(startDateTimeConst) < 0) {

                tblMachineDailyReportVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_producint_work_datetime_order"));

                return true;
            }

            if (isPEndLastDay || isPNotEndLastDay) {
                // 生産実績の開始時刻を取得する
                TblProduction tblProduction = tblProductionService.getProductionSingleById(tblMachineDailyReportVo.getProductionId());
                if (null != tblProduction) {
                    if (tblProduction.getEndDatetime() != null && isPEndLastDay) {
                        String productionEndTime = DateFormat.dateToStr(tblProduction.getEndDatetime(), DateFormat.DATETIME_FORMAT);

                        // 日報終了時刻＞生産終了時刻の場合
                        if (tblMachineDailyReportVo.getEndDatetimeStr().compareTo(productionEndTime) > 0) {

                            tblMachineDailyReportVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_next_day_producint_work_datetime_order"));

                            return true;
                        }
                    } else {
                        if (chkEndDateTimeAfterSysEnd(cnfSystem, tblMachineDailyReportVo, loginUser)) {
                            return true;
                        }
                    }
                }
            } else {
                if (chkEndDateTimeAfterSysEnd(cnfSystem, tblMachineDailyReportVo, loginUser)) {
                    return true;
                }
            }

        }

        return res;
    }
    
    /**
     * checkProductionDate 日報登録の場合、日報の生産日をチェックする
     *
     * @param tblMachineDailyReportVo
     * @param productionId
     * @param loginUser
     * @return
     */
    public String checkProductionDate(TblMachineDailyReportVo tblMachineDailyReportVo, String productionId, LoginUser loginUser) {

        String res = "0";

        Date nowDate = new Date();

        String nowDateStr = DateFormat.dateToStr(nowDate, DateFormat.DATE_FORMAT);

        if (tblMachineDailyReportVo.getProductionDateStr().compareTo(nowDateStr) > 0) {

            // 生産実績の開始時刻を取得する
            Query query = entityManager.createNamedQuery("TblMachineDailyReport.findByProductionIdAndDate");

            query.setParameter("productionId", productionId);
            query.setParameter("productionDate", DateFormat.getBeforeDay(DateFormat.strToDate(tblMachineDailyReportVo.getProductionDateStr())));

            List<TblMachineDailyReport> tblMachineDailyReportList = query.getResultList();

            if (!tblMachineDailyReportList.isEmpty()) {
                tblMachineDailyReportVo.setId(tblMachineDailyReportList.get(0).getId());

                return "1";
            }

        }

        return res;
    }
    
    /**
     * chkEndDateTimeAfterSysEnd 入力終了時刻は業務終了時刻より後の場合、エラーになる
     *
     * @param tblMachineDailyReportVo
     * @param productionId
     * @param loginUser
     * @return
     */
    private boolean chkEndDateTimeAfterSysEnd (CnfSystem cnfSystem,
        TblMachineDailyReportVo tblMachineDailyReportVo, LoginUser loginUser) {
        StringBuilder endDateTimeBulid = new StringBuilder();

            if (cnfSystem.getConfigValue().length() == 4) {

                endDateTimeBulid.append(DateFormat.dateToStr(DateFormat.getAfterDay(DateFormat.strToDate(tblMachineDailyReportVo.getProductionDateStr())), DateFormat.DATE_FORMAT)).append(" 0").append(cnfSystem.getConfigValue()).append(CONST_SECOND);
            } else {

                endDateTimeBulid.append(DateFormat.dateToStr(DateFormat.getAfterDay(DateFormat.strToDate(tblMachineDailyReportVo.getProductionDateStr())), DateFormat.DATE_FORMAT)).append(" ").append(cnfSystem.getConfigValue()).append(CONST_SECOND);
            }

            // 翌日の業務開始時刻
            String endDateTimeConst = endDateTimeBulid.toString();

            if (tblMachineDailyReportVo.getEndDatetimeStr().compareTo(endDateTimeConst) > 0) {

                tblMachineDailyReportVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_next_day_actual_work_datetime_order"));

                return true;
            }
            
            return false;
    }
    
    /**
     * 機械日報一覧内容取得
     *
     * @param componentCode
     * @param formatProductionDateFrom
     * @param formatProductionDateTo
     * @param reporterUser
     * @param machineId
     * @param department
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return
     */
    public TblMachineDailyReportDetailList getMachineDailyReportDetailsByConditionPage(String componentCode,
            Date formatProductionDateFrom, Date formatProductionDateTo, String reporterUser, String machineId,
            Integer department, String sidx, String sord, int pageNumber, int pageSize, boolean isPage) {

        TblMachineDailyReportDetailList response = new TblMachineDailyReportDetailList();

        if (isPage) {

            List count = getMacDailyReportListByPage(componentCode, formatProductionDateFrom, formatProductionDateTo,
                    reporterUser, machineId, department, sidx, sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            response.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            response.setCount(counts);
            response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }

        // 一覧データ取得
        List list = getMacDailyReportListByPage(componentCode, formatProductionDateFrom, formatProductionDateTo,
                reporterUser, machineId, department, sidx, sord, pageNumber, pageSize, false);

        response.setTblMachineDailyReportDetails(list);
        return response;
    }
    
    /**
     * 機械日報一覧データ取得
     *
     * @param componentCode
     * @param formatProductionDateFrom
     * @param formatProductionDateTo
     * @param reporterUser
     * @param machineId
     * @param department
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    private List getMacDailyReportListByPage(String componentCode, Date formatProductionDateFrom,
            Date formatProductionDateTo, String reporterUser, String machineId, int department, String sidx,
            String sord, int pageNumber, int pageSize, boolean isCount) {
        StringBuilder sql = new StringBuilder();

        if (isCount) {
            sql.append(" SELECT count(1) ");
        } else {
            sql.append(" SELECT dailyReportDetail ");
        }

        sql.append(" FROM TblMachineDailyReportDetail dailyReportDetail ");
        sql.append(" JOIN FETCH dailyReportDetail.tblMachineDailyReport mdr ");
        if (!isCount) {
            sql.append(" LEFT JOIN FETCH dailyReportDetail.tblMachineDailyReport.mstUser mu ");
            sql.append(" LEFT JOIN FETCH dailyReportDetail.mstComponent mc ");
            sql.append(" LEFT JOIN FETCH dailyReportDetail.tblMachineDailyReport.mstMachine machine ");
            sql.append(" LEFT JOIN FETCH dailyReportDetail.mstProcedure mp ");
            sql.append(" LEFT JOIN FETCH dailyReportDetail.tblMachineDailyReport.tblProduction tp ");
            sql.append(" LEFT JOIN FETCH dailyReportDetail.tblMachineDailyReport.tblProduction.mstWorkPhase mwp ");
        }
        sql.append(" WHERE dailyReportDetail.tblMachineDailyReport.noRegistrationFlag != 0 ");

        // 部品コード 部分一致
        if (!StringUtils.isEmpty(componentCode)) {
            sql.append(" AND dailyReportDetail.mstComponent.componentCode like :componentCode ");
        }
        // 開始時刻 同日以上
        if (formatProductionDateFrom != null) {
            sql.append(
                    " AND dailyReportDetail.tblMachineDailyReport.tblMachineDailyReportPK.productionDate >= :formatProductionDateFrom ");
        }
        // 終了時刻 同日以下
        if (formatProductionDateTo != null) {
            sql.append(
                    " AND dailyReportDetail.tblMachineDailyReport.tblMachineDailyReportPK.productionDate <= :formatProductionDateTo ");
        }
        // 報告者 部分一致
        if (!StringUtils.isEmpty(reporterUser)) {
            sql.append(" AND dailyReportDetail.tblMachineDailyReport.mstUser.userName like :reporterUser ");
        }
        // 設備ID 完全一致
        if (!StringUtils.isEmpty(machineId)) {
            sql.append(" AND dailyReportDetail.tblMachineDailyReport.mstMachine.machineId = :machineId ");
        }
        // 所属 完全一致
        if (department != 0) {
            sql.append(" AND dailyReportDetail.tblMachineDailyReport.mstMachine.department = :department ");
        }
        
        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);

            } else {

                // 生産日 降順, 開始時刻 降順
                sql.append(
                        " ORDER BY dailyReportDetail.tblMachineDailyReport.tblMachineDailyReportPK.productionDate DESC, dailyReportDetail.tblMachineDailyReport.startDatetime DESC ");

            }

        }
        
        Query query = entityManager.createQuery(sql.toString());

        // パラーメタ設定
        if (!StringUtils.isEmpty(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }
        if (formatProductionDateFrom != null) {
            query.setParameter("formatProductionDateFrom", formatProductionDateFrom);
        }
        if (formatProductionDateTo != null) {
            query.setParameter("formatProductionDateTo", formatProductionDateTo);
        }
        if (!StringUtils.isEmpty(reporterUser)) {
            query.setParameter("reporterUser", "%" + reporterUser + "%");
        }
        if (!StringUtils.isEmpty(machineId)) {
            query.setParameter("machineId", machineId);
        }
        if (department != 0) {
            query.setParameter("department", department);
        }
        
        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();
        return list;
    }

    @Transactional
    private void updateComponentLotRelation(TblMachineDailyReportDetailVo aDetailVo, TblComponentLot tblComponentLot, String macReportId, String userUuid) {
        if (aDetailVo.getTblComponentLotRelationVoList() != null && aDetailVo.getTblComponentLotRelationVoList().getTblComponentLotRelationVos() != null && !aDetailVo.getTblComponentLotRelationVoList().getTblComponentLotRelationVos().isEmpty()) {
            for (TblComponentLotRelationVo relationVo : aDetailVo.getTblComponentLotRelationVoList().getTblComponentLotRelationVos()) {
                String subComponentLotId = null;
                if (StringUtils.isNotEmpty(relationVo.getSubComponentLotNo())) {
                    TblComponentLot subComponentLot = tblComponentLotService.getSingleResultTblComponentLot(relationVo.getSubComponentCode(), aDetailVo.getProcedureCode(), relationVo.getSubComponentLotNo());
                    if (subComponentLot == null) {
                        continue;
                    } else {
                        subComponentLotId = subComponentLot.getUuid();
                    }
                }
                
//                if (tblComponentLotRelationVoList != null && tblComponentLotRelationVoList.getTblComponentLotRelationVos() != null && !tblComponentLotRelationVoList.getTblComponentLotRelationVos().isEmpty()) {
//                    boolean isExist = false;
//                    for (TblComponentLotRelationVo tblComponentLotRelationVo : tblComponentLotRelationVoList.getTblComponentLotRelationVos()) {
//                        if (tblComponentLotRelationVo.getMacReportId().equals(relationVo.getMacReportId()) && tblComponentLotRelationVo.getSubComponentLotNo().equals(relationVo.getSubComponentLotNo())) {
//                            isExist = true;
//                            break;
//                        }
//                    }
//                    if (isExist) {
//                        continue;
//                    }
//                }
                MstProcedure maxMstProcedureCode = mstProcedureService.getMaxProcedureCode(relationVo.getSubComponentId());
                
                TblComponentLotRelation tblComponentLotRelation = new TblComponentLotRelation();
                tblComponentLotRelation.setUuid(IDGenerator.generate());
                tblComponentLotRelation.setComponentId(aDetailVo.getComponentId());
                tblComponentLotRelation.setProcedureCode(aDetailVo.getProcedureCode());
                tblComponentLotRelation.setComponentLotId(tblComponentLot.getUuid());
                tblComponentLotRelation.setSubComponentId(relationVo.getSubComponentId());
                tblComponentLotRelation.setSubProcedureCode(maxMstProcedureCode == null ? "" : maxMstProcedureCode.getProcedureCode());
                tblComponentLotRelation.setSubComponentLotId(subComponentLotId);
                tblComponentLotRelation.setProductionDetailId(StringUtils.isEmpty(aDetailVo.getProductionDetailId()) ? aDetailVo.getId() : aDetailVo.getProductionDetailId());
                tblComponentLotRelation.setMacReportId(macReportId);
                tblComponentLotRelation.setCreateDate(new Date());
                tblComponentLotRelation.setCreateUserUuid(userUuid);
                tblComponentLotRelation.setUpdateDate(tblComponentLotRelation.getCreateDate());
                tblComponentLotRelation.setUpdateUserUuid(userUuid);
                entityManager.persist(tblComponentLotRelation);
            }
        }
    }
}
