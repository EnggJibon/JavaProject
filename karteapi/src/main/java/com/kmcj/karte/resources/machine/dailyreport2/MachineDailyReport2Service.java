/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForDay;
import com.kmcj.karte.resources.mold.production.TblMoldProductionPeriodService;
import com.kmcj.karte.resources.production2.Production;
import com.kmcj.karte.resources.production2.ProductionDetail;
import com.kmcj.karte.resources.work.TblWork;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.choice.MstChoiceVo;
import com.kmcj.karte.resources.component.structure.MstComponentStructureVoList;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.dailyreport2.stockupdate.StockUpdater;
import com.kmcj.karte.resources.machine.downtime.MstMachineDowntimeService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.TblProductionList;
import com.kmcj.karte.resources.production.TblProductionService;
import com.kmcj.karte.resources.production.defect.TblProductionDefectDaily;
import com.kmcj.karte.resources.production.defect.TblProductionDefectList;
import com.kmcj.karte.resources.production.defect.TblProductionDefectService;
import com.kmcj.karte.resources.production.lot.balance.LotBalanceUpdater;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.kmcj.karte.util.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MachineDailyReport2Service {
	//String headerList;
    Object [][] data;
    
	@PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TotalUpdater totalUpdater;
    
    @Inject
    private PeriodProductionUpdater periodProductionUpdater;
    
    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private MstMachineDowntimeService mstMachineDowntimeService;
    
    @Inject
    private StockUpdater stockUpdater;
    
    @Inject
    private LotBalanceUpdater lotBalanceUpdater;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    @Inject
    private TblProductionDefectService tblProductionDefectService;
    
    @Inject
    private MstProcedureService mstProcedureService;
    
    @Inject
    private TblProductionService tblProductionService;

    private CnfSystem cnfSystemBusinessStarttime = null;

    //                                                       0  1  2  3    4   5   6  7  8  9 10 11 12  13     14   15     16   17
    private static List<String> EXCEL_FORMAT = Arrays.asList("","","","","num","","","","","","","","","num","num","num","num","num",
        "","num","num","","num","num","","num","num");
    
    SimpleDateFormat sdfDate = new SimpleDateFormat(DateFormat.DATE_FORMAT);
    SimpleDateFormat sdfDate2 = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
    private Map<String, String> headerMap;
    
    private String[] makeLine(TblMachineDailyReport2 report, TblMachineDailyReport2Detail detail, MstChoiceList defectList) {
        String[] line = null;
        if (defectList != null && defectList.getMstChoiceVo() != null && !defectList.getMstChoiceVo().isEmpty()) {
            line = new String[38 - 11 + defectList.getMstChoiceVo().size()];// 不良種別01-10とその他表示しないように分類項目マスタから生産不具合区分を表示する
        } else {
            line = new String[27];
        }
        for (int i = 0; i < line.length; i++) {
            line[i] = "";
        }
        if (report.getReportDate() != null) {
            line[0] = sdfDate.format(report.getReportDate());
        }
        if (report.getMachineName() != null) {
            line[1] = report.getMachineName();
        }
        if (detail.getStartDatetime() != null) {
            line[2] = sdfDate2.format(detail.getStartDatetime());
        }
        if (detail.getEndDatetime() != null) {
            line[3] = sdfDate2.format(detail.getEndDatetime());
        }

        line[4] = String.valueOf(detail.getDurationMinitues());

        line[5] = detail.getOperatingFlg() == 0 ? headerMap.get("operating_flg_off") : headerMap.get("operating_flg_on");

        if (detail.getWork() != null) {
            line[6] = detail.getWork();
        }
        if(detail.getMstMachineDowntime() != null && detail.getMstMachineDowntime().getDowntimeCode() != null){
            String outputDowntimeCode = detail.getMstMachineDowntime().getDowntimeCode();
            line[7] = outputDowntimeCode;
        }
        if(detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_DOWNTIME && detail.getMstMachineDowntime() != null){
            int outputPlannedFlg = detail.getMstMachineDowntime().getPlannedFlg();
            String flag = null;
            if(outputPlannedFlg == 0){
                flag = headerMap.get("unplanned_downtime");

            } else if(outputPlannedFlg == 1){
                flag = headerMap.get("planned_downtime");
            }
            if (flag != null){
                line[8] = flag;
            }
        }
        else if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_WORK) {
            line[8] = headerMap.get("planned_downtime");
        }
        if(detail.getDowntimeComment() != null){
            line[9] = detail.getDowntimeComment();
        }
        if(detail.getWorkerName() != null){
            line[10] = detail.getWorkerName();
        }
        if(detail.getMoldId() != null){
            line[11] = detail.getMoldId();
        }

        //line[12] = String.valueOf(detail.getShotCount());
        if (detail.getFirstComponentCode() != null) {
            line[12] = detail.getFirstComponentCode();
        }

        return line;
    }
    
    private String[] makeLineProd(TblMachineDailyReport2 report, TblMachineDailyReport2Detail detail, TblMachineDailyReport2ProdDetail prod, MstChoiceList defectList, List<String> excelFormat) {
        //line.add(report.getMachineName());
        String[] line = makeLine(report, detail, defectList);

        line[4] = String.valueOf(prod.getComponentOperatingMinutes());
        line[13] = String.valueOf(detail.getShotCount());
        if(prod.getComponentCode() != null){
            
            line[12] = prod.getComponentCode();
        }
        
        line[14] = String.valueOf(detail.getDisposedShotCount());
        
        line[15] = String.valueOf(prod.getCountPerShot());
        
        line[16] = String.valueOf(prod.getCompleteCount());
        
        line[17] = String.valueOf(prod.getDefectCount());
        
        int index = 17;
        if (defectList != null && defectList.getMstChoiceVo() != null && !defectList.getMstChoiceVo().isEmpty()) {
            for (MstChoiceVo choice : defectList.getMstChoiceVo()) {
                if (prod.getTblProductionDefectDailyList() != null && !prod.getTblProductionDefectDailyList().isEmpty()) {
                    boolean isExist = false;
                    for (TblProductionDefectDaily defectDaily : prod.getTblProductionDefectDailyList()) {
                        if (Integer.parseInt(choice.getSeq()) == defectDaily.getDefectSeq()) {
                            line[++index] = String.valueOf(defectDaily.getQuantity());
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        line[++index] = "0";
                    }
                } else {
                    line[++index] = "0";
                }
                excelFormat.add(index, "num");
            }
        }
        
        if(prod.getMaterial01Name() != null){
            line[++index] = prod.getMaterial01Name();
        }
        if(prod.getMaterial01Amount() != null){
            BigDecimal outputMaterial01Amount = prod.getMaterial01Amount();
            line[++index] = outputMaterial01Amount.toString();
        }
        if(prod.getMaterial01PurgedAmount() != null){
            BigDecimal outputMaterial01PurgedAmount = prod.getMaterial01PurgedAmount();
            line[++index] = outputMaterial01PurgedAmount.toString();
        }
        if(prod.getMaterial02Name() != null){
            line[++index] = prod.getMaterial02Name();
        }
        if(prod.getMaterial02Amount()!= null){
            BigDecimal outputMaterial02Amount = prod.getMaterial02Amount();
            line[++index] = outputMaterial02Amount.toString();
        }
        if(prod.getMaterial02PurgedAmount() != null){
            BigDecimal outputMaterial02PurgedAmount = prod.getMaterial02PurgedAmount();
            line[++index] = outputMaterial02PurgedAmount.toString();
        }
        if(prod.getMaterial03Name() != null){
            line[++index] = prod.getMaterial03Name();
        }
        if(prod.getMaterial03Amount() != null){
            BigDecimal outputMaterial03Amount = prod.getMaterial03Amount();
            line[++index] = outputMaterial03Amount.toString();
        }
        if(prod.getMaterial03PurgedAmount() != null){
            BigDecimal outputMaterial03PurgedAmount = prod.getMaterial03PurgedAmount();
            line[++index] = outputMaterial03PurgedAmount.toString();
        }
        return line;
    }
    
	public FileReponse getDailyReportOutputExcel(String reportDate, String department, LoginUser loginUser) {
        String langId = loginUser.getLangId();
        List<String> dictKeyList = Arrays.asList("machine_daily_report2_reference", "machine_report_date", "machine_name", "work_start_time", 
                "work_end_time", "duration_minutes", "operating_flg", "machine_report_work", "downtime_code", "planned_flg", "downtime_comment",
                "work_user_name", "mold_id", "component_code", "shot_count", "disposed_shot_count", "count_per_shot",  "complete_count", "defect_count", "material_name_with_parameter", "material_amount_with_parameter",  
                "material_purged_amount_with_parameter", "planned_downtime", "unplanned_downtime", "operating_flg_on", "operating_flg_off");
        headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        
        FileReponse response = new FileReponse();
        String sheetName = headerMap.get("machine_daily_report2_reference");
        
        MstChoiceList defectList = null;
        
        List<String> excelOutHeadList = new ArrayList<>();
        excelOutHeadList.add(headerMap.get("machine_report_date"));
        excelOutHeadList.add(headerMap.get("machine_name"));
        excelOutHeadList.add(headerMap.get("work_start_time"));
        excelOutHeadList.add(headerMap.get("work_end_time"));
        excelOutHeadList.add(headerMap.get("duration_minutes"));
        excelOutHeadList.add(headerMap.get("operating_flg"));
        excelOutHeadList.add(headerMap.get("machine_report_work"));
        excelOutHeadList.add(headerMap.get("downtime_code"));
        excelOutHeadList.add(headerMap.get("planned_flg"));
        excelOutHeadList.add(headerMap.get("downtime_comment"));
        excelOutHeadList.add(headerMap.get("work_user_name"));
        excelOutHeadList.add(headerMap.get("mold_id"));
        excelOutHeadList.add(headerMap.get("component_code"));
        excelOutHeadList.add(headerMap.get("shot_count"));
        excelOutHeadList.add(headerMap.get("disposed_shot_count"));
        excelOutHeadList.add(headerMap.get("count_per_shot"));
        excelOutHeadList.add(headerMap.get("complete_count"));
        excelOutHeadList.add(headerMap.get("defect_count"));
        if (StringUtils.isNotEmpty(department)) {
            defectList = mstChoiceService.getCategories(langId, "tbl_production_defect.defect_type", department, null);
            if (defectList != null && defectList.getMstChoiceVo() != null && !defectList.getMstChoiceVo().isEmpty()) {
                for (MstChoiceVo choice : defectList.getMstChoiceVo()) {
                    excelOutHeadList.add(choice.getChoice());
                }
            }
        }
        excelOutHeadList.add(headerMap.get("material_name_with_parameter").toString().replace("%s", "1"));
        excelOutHeadList.add(headerMap.get("material_amount_with_parameter").toString().replace("%s", "1"));
        excelOutHeadList.add(headerMap.get("material_purged_amount_with_parameter").toString().replace("%s", "1"));
        excelOutHeadList.add(headerMap.get("material_name_with_parameter").toString().replace("%s", "2"));
        excelOutHeadList.add(headerMap.get("material_amount_with_parameter").toString().replace("%s", "2"));
        excelOutHeadList.add(headerMap.get("material_purged_amount_with_parameter").toString().replace("%s", "2"));
        excelOutHeadList.add(headerMap.get("material_name_with_parameter").toString().replace("%s", "3"));
        excelOutHeadList.add(headerMap.get("material_amount_with_parameter").toString().replace("%s", "3"));
        excelOutHeadList.add(headerMap.get("material_purged_amount_with_parameter").toString().replace("%s", "3"));
        
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        DataFormat dataFormat = workbook.createDataFormat();
        Row row = sheet.createRow(0);
        for(int i = 0; i < excelOutHeadList.size(); i++){
            Cell cell = row.createCell(i);
            String val = excelOutHeadList.get(i);
            cell.setCellValue(val);
        }
        ArrayList<List> excelData = new ArrayList<>();
        List<String> excelFormat = new ArrayList<>(EXCEL_FORMAT);
        
        MachineDailyReport2List reportList = getMachineDailyReport2(reportDate, department, null);

       // TblMachineDailyReport2Detail reportList2 = getMachineDailyReport2(reportDate, department);
//        TblMachineDailyReport2ProdDetail reportProdDetail = getTblMachineDailyReport2ProdDetailList();
        if (reportList != null) {
            //Excel Body Making Logic
            List<TblMachineDailyReport2> reports = reportList.getTblMachineDailyReports();
            if (reports != null) {
                for (TblMachineDailyReport2 report : reports) {
                    List<String> line = new ArrayList<>();
                    //format = new ArrayList<>();

                    if(report.getTblMachineDailyReport2DetailList() != null){
                        for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                            if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD) {
                                if(detail.getTblMachineDailyReport2ProdDetailList() != null){
                                    for (TblMachineDailyReport2ProdDetail prodDetail : detail.getTblMachineDailyReport2ProdDetailList()) {
                                        String[] lineStrings = makeLineProd(report, detail, prodDetail, defectList, excelFormat);
                                        line = Arrays.asList(lineStrings);
                                        excelData.add(line);
                                    }
                                }
                                
                            }
                            else {
                                String[] lineStrings =  makeLine(report, detail, null);
                                //Convert array to list
                                line = Arrays.asList(lineStrings);
                                excelData.add(line);
                            }
                        }
                    }
                    
                }
            }
        }
//        String [] alignArray;
//        alignArray = new String [36];
        for(int d = 0; d < excelData.size(); d++){
            Row dataRow = sheet.createRow(d+1);
            for(int i = 0; i < excelData.get(d).size(); i++){
                Cell cell = dataRow.createCell(i);
                
                if("num".equals(excelFormat.get(i))){
                    CellStyle style = workbook.createCellStyle();
                    style.setAlignment(HorizontalAlignment.RIGHT);
                    style.setDataFormat(dataFormat.getFormat("0.0"));
                    cell.setCellStyle(style);
                } 
                String val = excelData.get(d).get(i).toString();
                cell.setCellValue(val);
            }
        }
        
        String uuid = IDGenerator.generate();
        try{String outExcelPath = FileUtil.outExcelFile(kartePropertyService, uuid);
            FileOutputStream fout=new FileOutputStream(outExcelPath);
            workbook.write(fout);    
            workbook.close();
            System.out.println("success...");    
        }catch(Exception e){System.out.println(e);}
        
        FileUtil fu = new FileUtil();
        
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.MACHINE_DAILY_REPORT2_REFERENCE);
        tblCsvExport.setExportDate(new java.util.Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE_DAILY_REPORT2_REFERENCE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        tblCsvExport.setClientFileName(fu.getExcelFileName(headerMap.get("machine_daily_report2_reference")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        response.setFileUuid(uuid);
        return response;
            
    }
	
	
	
    public TblMachineDailyReport2 getMachineDailyReport2ById(String id) {
        TblMachineDailyReport2 report = null;
        Query query = entityManager.createNamedQuery("TblMachineDailyReport2.findById");
        query.setParameter("id", id);
        try {
            report = (TblMachineDailyReport2)query.getSingleResult();
        }
        catch (NoResultException ne) {
            
        }
        return report;
    }

    public TblMachineDailyReport2 getMachineDailyReport2ByUniqueKey(java.util.Date reportDate, String machineUuid) {
        TblMachineDailyReport2 report = null;
        Query query = entityManager.createNamedQuery("TblMachineDailyReport2.findByUniqueKey");
        query.setParameter("reportDate", reportDate);
        query.setParameter("machineUuid", machineUuid);
        try {
            report = (TblMachineDailyReport2)query.getSingleResult();
        }
        catch (NoResultException ne) {
            
        }
        return report;
    }
    
    /**
     * 同じ生産実績IDを持つ機械日報明細をすべて更新
     * @param productionId 
     */
    private void updateProductionEndFlgs(String productionId, int flg) {
        Query query = entityManager.createQuery("UPDATE TblMachineDailyReport2Detail t SET t.productionEndFlg = :flg WHERE t.productionId = :productionId");
        query.setParameter("flg", flg);
        query.setParameter("productionId", productionId);
        query.executeUpdate();
    }
    
    
    /**
     * 生産実績IDの等しい機械日報明細を取得
     * @param productionId
     * @return 
     */
    private List <TblMachineDailyReport2Detail> getReportDetailsByProductionId(String productionId) {
        Query query = entityManager.createQuery("SELECT t FROM TblMachineDailyReport2Detail t WHERE t.productionId = :productionId ORDER BY t.endDatetime DESC ");
        query.setParameter("productionId", productionId);
        return query.getResultList();
    }
    
    /**
     * 生産実績の生産開始日時を調整する。機械日報2で変更したときに合わせるため。
     * @param reportDetail
     * @param loginUser 
     */
    @Transactional
    private void adjustProductionStartDatetime(TblMachineDailyReport2Detail reportDetail, LoginUser loginUser) {
        if (reportDetail.getDetailType() != TblMachineDailyReport2.DETAIL_TYPE_PROD || reportDetail.getProductionId() == null) return;
        Production production = getProductionById(reportDetail.getProductionId());
        if (production == null) return;
        //システム設定の業務開始時刻を取得
        if (cnfSystemBusinessStarttime == null) {
            cnfSystemBusinessStarttime =  cnfSystemService.findByKey("system", "business_start_time");
        }
        //生産実績の生産開始日時と機械日報の生産開始日時が同じ営業日に属し、かつ値が違うとき、機械日報の生産開始日時にあわせる
        String businessDateProductionStartDatetime = DateFormat.getBusinessDate2(DateFormat.dateToStr(production.getStartDatetime(), DateFormat.DATETIME_FORMAT), cnfSystemBusinessStarttime);
        String businessDateMDR2ProductionStartDatetime = DateFormat.getBusinessDate2(DateFormat.dateToStr(reportDetail.getStartDatetime(), DateFormat.DATETIME_FORMAT), cnfSystemBusinessStarttime);
        if (businessDateProductionStartDatetime.equals(businessDateMDR2ProductionStartDatetime)) {
            production.setStartDatetime(reportDetail.getStartDatetime());
            production.setStartDatetimeStz(reportDetail.getEndDatetimeStz());
            production.setUpdateDate(new java.util.Date());
            production.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(production);
        }
    }
    
    /**
     * 生産終了の取り消し
     * @param reportDetail
     * @param loginUser 
     */
    @Transactional
    private void cancelEndProduction(TblMachineDailyReport2Detail reportDetail, LoginUser loginUser) {
        //生産実績データを取得
        Production production = getProductionById(reportDetail.getProductionId());
        if (production == null) return;
        //生産終了時にセットする値をすべて初期化
        production.setEndDatetime(null);
        production.setEndDatetimeStz(null);
        production.setProducingTimeMinutes(null);
        production.setSuspendedTimeMinutes(null);
        production.setNetProducintTimeMinutes(null);
        production.setShotCount(0);
        production.setStatus(0);
        production.setUpdateDate(new java.util.Date());
        production.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(production);
        for (ProductionDetail productionDetail : production.getProductionDetails()) {
            productionDetail.setCompleteCount(0); //完成数
            productionDetail.setDefectCount(0); //不良数
            productionDetail.setMaterial01Amount(new BigDecimal(0)); //材料01使用量
            productionDetail.setMaterial01PurgedAmount(new BigDecimal(0)); //材料01パージ量
            productionDetail.setMaterial02Amount(new BigDecimal(0)); //材料02使用量
            productionDetail.setMaterial02PurgedAmount(new BigDecimal(0)); //材料02パージ量
            productionDetail.setMaterial03Amount(new BigDecimal(0)); //材料03使用量
            productionDetail.setMaterial03PurgedAmount(new BigDecimal(0)); //材料03パージ量
            productionDetail.setUpdateDate(new java.util.Date());
            productionDetail.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(productionDetail);
        }
        //同じ生産実績IDをもつ機械日報明細テーブルの生産終了フラグを落とす
        updateProductionEndFlgs(reportDetail.getProductionId(), 0);
}

    /**
     * 生産終了
     * @param reportDetail
     * @param loginUser 
     */
    @Transactional
    private void endProduction(TblMachineDailyReport2Detail reportDetail, LoginUser loginUser) {
        //生産実績データを取得
        Production production = getProductionById(reportDetail.getProductionId());
        //同じ生産実績IDを持つ機械日報明細をすべて取得(終了日時の降順)
        List<TblMachineDailyReport2Detail> details = getReportDetailsByProductionId(reportDetail.getProductionId());
        if (details == null || details.isEmpty()) return;
        TblMachineDailyReport2Detail lastReportDetail = details.get(0);
        if (lastReportDetail.getEndDatetime().compareTo(reportDetail.getEndDatetime()) < 0 || lastReportDetail.getId().equals(reportDetail.getId())) {
            lastReportDetail = reportDetail;
        }
        
        //生産実績を更新
        production.setEndDatetime(lastReportDetail.getEndDatetime()); //終了日時
        production.setEndDatetimeStz(lastReportDetail.getEndDatetimeStz()); //終了日時(STZ)
        //生産時間を計算 -> 生産開始時刻と生産終了時刻の単純差異 
        long diffMills = lastReportDetail.getEndDatetime().getTime() - production.getStartDatetime().getTime();
        long diffMinutes = diffMills / (1000 * 60);
        production.setProducingTimeMinutes((int)diffMinutes); //生産時間(分)
        //実稼働時間(分),ショット数,捨てショット数,生産数の足し込み
        //現在の値を初期化
        production.setNetProducintTimeMinutes(0);
        production.setShotCount(0);
        production.setDisposedShotCount(0);
        for (ProductionDetail productionDetail : production.getProductionDetails()) {
            productionDetail.setCompleteCount(0); //完成数
            productionDetail.setDefectCount(0); //不良数
            productionDetail.setMaterial01Amount(new BigDecimal(0)); //材料01使用量
            productionDetail.setMaterial01PurgedAmount(new BigDecimal(0)); //材料01パージ量
            productionDetail.setMaterial02Amount(new BigDecimal(0)); //材料02使用量
            productionDetail.setMaterial02PurgedAmount(new BigDecimal(0)); //材料02パージ量
            productionDetail.setMaterial03Amount(new BigDecimal(0)); //材料03使用量
            productionDetail.setMaterial03PurgedAmount(new BigDecimal(0)); //材料03パージ量
        }
        for (TblMachineDailyReport2Detail tmp: details) {
            //更新のときは更新後のオブジェクトを用いる
            TblMachineDailyReport2Detail detail;
            if (tmp.getId().equals(reportDetail.getId()) && reportDetail.isModified()) {
                detail = reportDetail;
            }
            else {
                detail = tmp;
            }
            
            //実稼働時間、ショット数、捨てショット数を足し込み
            production.setNetProducintTimeMinutes(production.getNetProducintTimeMinutes() + detail.getDurationMinitues());
            production.setShotCount(production.getShotCount() + detail.getShotCount());
            production.setDisposedShotCount(production.getDisposedShotCount() + detail.getDisposedShotCount());
            //生産数、材料使用量の足し込み
            for (ProductionDetail productionDetail : production.getProductionDetails()) {
                for (TblMachineDailyReport2ProdDetail reportProdDetail: detail.getTblMachineDailyReport2ProdDetailList()) {
                    //生産実績明細IDの等しいものを足し込む
                    if (productionDetail.getId().equals(reportProdDetail.getProductionDetailId())) {
                        productionDetail.setCompleteCount(productionDetail.getCompleteCount() + reportProdDetail.getCompleteCount());
                        productionDetail.setDefectCount(productionDetail.getDefectCount() + reportProdDetail.getDefectCount());
                        if (reportProdDetail.getMaterial01Amount() != null) {
                            productionDetail.setMaterial01Amount(productionDetail.getMaterial01Amount().add(reportProdDetail.getMaterial01Amount()));
                        }
                        if (reportProdDetail.getMaterial01PurgedAmount() != null) {
                            productionDetail.setMaterial01PurgedAmount(productionDetail.getMaterial01PurgedAmount().add(reportProdDetail.getMaterial01PurgedAmount()));
                        }
                        if (reportProdDetail.getMaterial02Amount() != null) {
                            productionDetail.setMaterial02Amount(productionDetail.getMaterial02Amount().add(reportProdDetail.getMaterial02Amount()));
                        }
                        if (reportProdDetail.getMaterial02PurgedAmount() != null) {
                            productionDetail.setMaterial02PurgedAmount(productionDetail.getMaterial02PurgedAmount().add(reportProdDetail.getMaterial02PurgedAmount()));
                        }
                        if (reportProdDetail.getMaterial03Amount() != null) {
                            productionDetail.setMaterial03Amount(productionDetail.getMaterial03Amount().add(reportProdDetail.getMaterial03Amount()));
                        }
                        if (reportProdDetail.getMaterial03PurgedAmount() != null) {
                            productionDetail.setMaterial03PurgedAmount(productionDetail.getMaterial03PurgedAmount().add(reportProdDetail.getMaterial03PurgedAmount()));
                        }
                    }
                }
            }
            
        }
        //中断時間 -> 生産時間と実稼働時間の差分
        production.setSuspendedTimeMinutes(production.getProducingTimeMinutes() - production.getNetProducintTimeMinutes());
        //ステータスを9(完了)に
        production.setStatus(9);
        production.setUpdateDate(new java.util.Date());
        production.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(production);
        for (ProductionDetail productionDetail : production.getProductionDetails()) {
            productionDetail.setUpdateDate(new java.util.Date());
            productionDetail.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(productionDetail);
        }
        
        //同じ生産実績IDをもつ機械日報明細テーブルの生産終了フラグを立てる
        updateProductionEndFlgs(reportDetail.getProductionId(), 1);
    }
    
    @Transactional
    private Production getProductionById(String productionId) {
        Query query = entityManager.createQuery("SELECT t FROM Production t WHERE t.id = :id");
        query.setParameter("id", productionId);
        List<Production> list = query.getResultList();
        if (list != null && !list.isEmpty()) {
            return (Production)list.get(0);
        }
        else {
            return null;
        }
    }
    
    @Transactional
    public void updateMachienDailyReport2(TblMachineDailyReport2 report, LoginUser loginUser) throws Exception {
        //日付、報告者がなければエラーで返す
        if (report.getReportDate() == null) {
            throw new Exception("Report date (reportDate) is missing.");
        }
        if (report.getReportPersonUuid() == null || report.getReportPersonUuid().equals("")) {
            throw new Exception("Report person (reportPersonUuid) is missing.");
        }
        report.getMstMachineDowntimes(mstMachineDowntimeService);
        report.calcTotal();
        periodProductionUpdater.setLoginUser(loginUser);
        periodProductionUpdater.prepare();
        stockUpdater.setLoginUser(loginUser);
        Query query;
        if (report.isDeleted()) {
            TblMachineDailyReport2 currentReport = getMachineDailyReport2ById(report.getId());
            if (currentReport == null) {
                throw new Exception(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            }
            //金型、設備のショット数、生産時間の減少をカウント
            if (currentReport.getTblMachineDailyReport2DetailList() != null) {
                for (TblMachineDailyReport2Detail detail : currentReport.getTblMachineDailyReport2DetailList()) {
                    if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD ) {
                        //削除なのですべてマイナスする
                        if (detail.getMoldUuid() != null) {
                            //金型累計ショット数、累計生産時間更新(マイナス)
                            totalUpdater.addMoldCounts(detail.getMoldUuid(), (detail.getShotCount() + detail.getDisposedShotCount()) * (-1), detail.getDurationMinitues() * (-1));
                            //金型期間別生産数テーブル更新(マイナス)
                            if (detail.getTblMachineDailyReport2ProdDetailList() != null) {
                                for (TblMachineDailyReport2ProdDetail prodDetail : detail.getTblMachineDailyReport2ProdDetailList()) {
                                    periodProductionUpdater.addMoldProductionPeriod(detail.getMoldUuid(), prodDetail.getComponentId(), currentReport.getReportDate(), prodDetail.getCompleteCount() * (-1));
                                    periodProductionUpdater.addMachineProductionPeriod(
                                        currentReport.getMachineUuid(), detail.getMoldUuid(), prodDetail.getComponentId(), currentReport.getReportDate(), prodDetail.getCompleteCount() * (-1));
                                    //部品在庫更新(マイナス)
                                    stockUpdater.addComponentStock(
                                        detail.getProductionId(), prodDetail.getProductionDetailId(), prodDetail.getComponentId(), prodDetail.getProcedureId(),
                                        prodDetail.getCompleteCount() * (-1), prodDetail.getTblComponentLotRelationVoList());
                                    //材料在庫更新(マイナス)
                                    setMaterialInfo(prodDetail);
                                    if (prodDetail.getMaterial01Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial01Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                            prodDetail.getMaterial01LotNo(), (prodDetail.getMaterial01Amount().add(prodDetail.getMaterial01PurgedAmount())).multiply(new BigDecimal(-1)));
                                    }
                                    if (prodDetail.getMaterial02Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial02Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                            prodDetail.getMaterial02LotNo(), (prodDetail.getMaterial02Amount().add(prodDetail.getMaterial02PurgedAmount())).multiply(new BigDecimal(-1)));
                                    }
                                    if (prodDetail.getMaterial03Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial03Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                            prodDetail.getMaterial03LotNo(), (prodDetail.getMaterial03Amount().add(prodDetail.getMaterial03PurgedAmount())).multiply(new BigDecimal(-1)));
                                    }
                                }
                            }
                        }
                        //設備累計ショット数、累計生産時間更新(マイナス)
                        totalUpdater.addMachineCounts(currentReport.getMachineUuid(), (detail.getShotCount() + detail.getDisposedShotCount()) * (-1), detail.getDurationMinitues() * (-1));
                    }
                }
            }
            //削除の場合は明細も生産明細もすべて削除(CASCADE)
            query = entityManager.createQuery("DELETE FROM TblMachineDailyReport2 t WHERE t.id = :id");
            query.setParameter("id", report.getId());
            query.executeUpdate();
        }
        else if (report.isAdded()) {
            //追加の場合は明細も生産明細もすべて追加。ただし削除フラグのついている明細、生産明細は除く
            //ヘダーをインサート
            report.setId(IDGenerator.generate());
            report.setCreateDate(new java.util.Date());
            report.setUpdateDate(report.getCreateDate());
            report.setCreateUserUuid(loginUser.getUserUuid());
            report.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(report);
            //明細をループ
            if (report.getTblMachineDailyReport2DetailList() != null) {
                for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                    if (detail.isDeleted()) continue; //削除フラグ除外
                    detail.setId(IDGenerator.generate());
                    detail.setReportId(report.getId());
                    detail.setMoldUuid(StringUtils.isEmpty(detail.getMoldUuid()) ? null : detail.getMoldUuid());
                    detail.setStzTimes(loginUser.getJavaZoneId());
                    detail.setCreateDate(new java.util.Date());
                    detail.setUpdateDate(detail.getCreateDate());
                    detail.setCreateUserUuid(loginUser.getUserUuid());
                    detail.setUpdateUserUuid(loginUser.getUserUuid());
                    if (detail.getFirstComponentId() != null && detail.getFirstComponentId().equals("")) {
                        detail.setFirstComponentId(null);
                    }
                    entityManager.persist(detail);
                    if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD ) {
                        //生産明細を追加
                        if (detail.getTblMachineDailyReport2ProdDetailList() != null) {
                            for (TblMachineDailyReport2ProdDetail prodDetail : detail.getTblMachineDailyReport2ProdDetailList()) {
                                if (prodDetail.isDeleted()) continue;
                                prodDetail.setReportDetailId(detail.getId());
                                prodDetail.setId(IDGenerator.generate());
                                prodDetail.setCreateDate(new java.util.Date());
                                prodDetail.setUpdateDate(prodDetail.getCreateDate());
                                prodDetail.setCreateUserUuid(loginUser.getUserUuid());
                                prodDetail.setUpdateUserUuid(loginUser.getUserUuid());
                                prodDetail.setMaterial01Id(StringUtils.isEmpty(prodDetail.getMaterial01Id()) ? null : prodDetail.getMaterial01Id());
                                prodDetail.setMaterial02Id(StringUtils.isEmpty(prodDetail.getMaterial02Id()) ? null : prodDetail.getMaterial02Id());
                                prodDetail.setMaterial03Id(StringUtils.isEmpty(prodDetail.getMaterial03Id()) ? null : prodDetail.getMaterial03Id());
                                entityManager.persist(prodDetail);
                                //期間別生産数テーブル更新
                                if (detail.getMoldUuid() != null) {
                                    periodProductionUpdater.addMoldProductionPeriod(
                                        detail.getMoldUuid(), prodDetail.getComponentId(), report.getReportDate(), prodDetail.getCompleteCount());
                                    periodProductionUpdater.addMachineProductionPeriod(
                                        report.getMachineUuid(), detail.getMoldUuid(), prodDetail.getComponentId(), report.getReportDate(), prodDetail.getCompleteCount());
                                }
                                //部品在庫更新
                                stockUpdater.addComponentStock(
                                    detail.getProductionId(), prodDetail.getProductionDetailId(), prodDetail.getComponentId(), prodDetail.getProcedureId(),
                                    prodDetail.getCompleteCount(), prodDetail.getTblComponentLotRelationVoList());
                                //材料在庫更新
                                setMaterialInfo(prodDetail);
                                if (prodDetail.getMaterial01Id() != null) {
                                    stockUpdater.addMaterialStock(prodDetail.getMaterial01Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                        prodDetail.getMaterial01LotNo(), prodDetail.getMaterial01Amount().add(prodDetail.getMaterial01PurgedAmount()));
                                }
                                if (prodDetail.getMaterial02Id() != null) {
                                    stockUpdater.addMaterialStock(prodDetail.getMaterial02Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                        prodDetail.getMaterial02LotNo(), prodDetail.getMaterial02Amount().add(prodDetail.getMaterial02PurgedAmount()));
                                }
                                if (prodDetail.getMaterial03Id() != null) {
                                    stockUpdater.addMaterialStock(prodDetail.getMaterial03Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                        prodDetail.getMaterial03LotNo(), prodDetail.getMaterial03Amount().add(prodDetail.getMaterial03PurgedAmount()));
                                }
                                //ロット残高更新
                                lotBalanceUpdater.addBalance(prodDetail.getProductionDetailId(), prodDetail.getCompleteCount());
                            }
                        }
                        //累計ショット数、累計生産時間の更新
                        if (detail.getMoldUuid() != null) {
                            totalUpdater.addMoldCounts(detail.getMoldUuid(), detail.getShotCount() + detail.getDisposedShotCount(), detail.getDurationMinitues());
                        }
                        totalUpdater.addMachineCounts(report.getMachineUuid(), detail.getShotCount() + detail.getDisposedShotCount(), detail.getDurationMinitues());
                    }
                    else {
                        detail.setTblMachineDailyReport2ProdDetailList(null);
                    }
                }
                for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                    if (detail.isDeleted()) continue;
                    if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD  ) {
                        if (detail.getTblMachineDailyReport2ProdDetailList() != null) {
                            for (TblMachineDailyReport2ProdDetail prodDetail : detail.getTblMachineDailyReport2ProdDetailList()) {
                                // 日別生産不具合を追加
                                if (prodDetail.getTblProductionDefectDailyList() != null && !prodDetail.getTblProductionDefectDailyList().isEmpty()) {
                                    for (TblProductionDefectDaily defectDaily : prodDetail.getTblProductionDefectDailyList()) {
                                        defectDaily.setMdr2ProdDetailId(prodDetail.getId());
                                    }
                                    tblProductionDefectService.postProductionDefectsDailyFromMdr(prodDetail.getTblProductionDefectDailyList(), report.getReportDate(), prodDetail.getProductionDetailId(), loginUser.getUserUuid());
                                }
                            }
                            // 生産実績製造ロット番号更新
                            Production production = getProductionById(detail.getProductionId());
                            if (production != null && production.getLotNumber() != null && !production.getLotNumber().equals(detail.getTblMachineDailyReport2ProdDetailList().get(0).getLotNumber())) {
                                production.setLotNumber(detail.getTblMachineDailyReport2ProdDetailList().get(0).getLotNumber());
                                production.setUpdateDate(new Date());
                                production.setUpdateUserUuid(loginUser.getUserUuid());
                                entityManager.merge(production);
                            }
                        }
                    }
                }
                //明細追加のループ２回目。機会日報2テーブル,機会日報2明細テーブル,機会日報2生産明細テーブルの３テーブルをPERSISTしてから次の更新処理を行わないとJPAエラーになるため
                for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                    if (detail.isDeleted()) continue;
                    if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD  ) {
                        //生産実績の開始日時を調整
                        adjustProductionStartDatetime(detail, loginUser);
                        //生産終了するとき生産実績データを作成
                        if (detail.getProductionEndFlg() == 1) {
                            endProduction(detail, loginUser);
                        }
                    }
                }
            }
        }
        else if (report.isModified()) {
            TblMachineDailyReport2 currentReport = getMachineDailyReport2ById(report.getId());
            if (currentReport == null) {
                throw new Exception(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            }
            report.setUpdateDate(new java.util.Date());
            report.setUpdateUserUuid(loginUser.getUserUuid());
            updateDailyReport2(report);
            //更新の場合、明細、生産明細について追加、更新、削除があり得る
            if (report.getTblMachineDailyReport2DetailList() != null) {
                //明細の追加、更新、削除
                for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                    if (detail.isDeleted()) {
                        //ショット数、生産時間のマイナス
                        TblMachineDailyReport2Detail currentDetail = currentReport.getReportDetailById(detail.getId());
                        if (currentDetail != null && currentDetail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD) {
                            if (currentDetail.getMoldUuid() != null) {
                                totalUpdater.addMoldCounts(
                                    currentDetail.getMoldUuid(), (currentDetail.getShotCount() + currentDetail.getDisposedShotCount()) * (-1), currentDetail.getDurationMinitues() * (-1));
                            }
                            totalUpdater.addMachineCounts(report.getMachineUuid(), (currentDetail.getShotCount() + currentDetail.getDisposedShotCount()) * (-1), currentDetail.getDurationMinitues() * (-1));
                            //期間別生産数テーブル更新(マイナス)
                            if (currentDetail.getMoldUuid() != null && currentDetail.getTblMachineDailyReport2ProdDetailList() != null) {
                                for (TblMachineDailyReport2ProdDetail prodDetail : currentDetail.getTblMachineDailyReport2ProdDetailList()) {
                                    periodProductionUpdater.addMoldProductionPeriod(
                                        currentDetail.getMoldUuid(), prodDetail.getComponentId(), report.getReportDate(), prodDetail.getCompleteCount() * (-1));
                                    periodProductionUpdater.addMachineProductionPeriod(
                                        currentReport.getMachineUuid(), currentDetail.getMoldUuid(), prodDetail.getComponentId(), report.getReportDate(), prodDetail.getCompleteCount() * (-1));
                                    //部品在庫更新
                                    stockUpdater.addComponentStock(
                                        detail.getProductionId(), prodDetail.getProductionDetailId(), prodDetail.getComponentId(), prodDetail.getProcedureId(),
                                        prodDetail.getCompleteCount() * (-1), prodDetail.getTblComponentLotRelationVoList());
                                    //材料在庫更新(マイナス)
                                    setMaterialInfo(prodDetail);
                                    if (prodDetail.getMaterial01Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial01Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                            prodDetail.getMaterial01LotNo(), (prodDetail.getMaterial01Amount().add(prodDetail.getMaterial01PurgedAmount())).multiply(new BigDecimal(-1)));
                                    }
                                    if (prodDetail.getMaterial02Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial02Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                            prodDetail.getMaterial02LotNo(), (prodDetail.getMaterial02Amount().add(prodDetail.getMaterial02PurgedAmount())).multiply(new BigDecimal(-1)));
                                    }
                                    if (prodDetail.getMaterial03Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial03Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                            prodDetail.getMaterial03LotNo(), (prodDetail.getMaterial03Amount().add(prodDetail.getMaterial03PurgedAmount())).multiply(new BigDecimal(-1)));
                                    }
                                    //ロット残高更新(マイナス)
                                    lotBalanceUpdater.addBalance(prodDetail.getProductionDetailId(), prodDetail.getCompleteCount() * (-1));
                                }
                            }
                        }
                        deleteDetail(detail.getId());
                    }
                    else if (detail.isAdded()) {
                        detail.setId(IDGenerator.generate());
                        detail.setReportId(report.getId());
                        detail.setStzTimes(loginUser.getJavaZoneId());
                        detail.setCreateDate(new java.util.Date());
                        detail.setUpdateDate(detail.getCreateDate());
                        detail.setCreateUserUuid(loginUser.getUserUuid());
                        detail.setUpdateUserUuid(loginUser.getUserUuid());
                        if (detail.getFirstComponentId() != null && detail.getFirstComponentId().equals("")) {
                            detail.setFirstComponentId(null);
                        }
                        detail.setMoldUuid(StringUtils.isEmpty(detail.getMoldUuid()) ? null : detail.getMoldUuid());
                        entityManager.persist(detail);
                        if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD  ) {
                            //明細が追加なら生産明細も追加。ただし削除は除く
                            //生産明細の作成
                            if (detail.getTblMachineDailyReport2ProdDetailList() != null) {
                                for (TblMachineDailyReport2ProdDetail prodDetail : detail.getTblMachineDailyReport2ProdDetailList()) {
                                    if (prodDetail.isDeleted()) continue; //削除除外
                                    prodDetail.setReportDetailId(detail.getId());
                                    prodDetail.setId(IDGenerator.generate());
                                    prodDetail.setCreateDate(new java.util.Date());
                                    prodDetail.setUpdateDate(prodDetail.getCreateDate());
                                    prodDetail.setCreateUserUuid(loginUser.getUserUuid());
                                    prodDetail.setUpdateUserUuid(loginUser.getUserUuid());
                                    prodDetail.setMaterial01Id(StringUtils.isEmpty(prodDetail.getMaterial01Id()) ? null : prodDetail.getMaterial01Id());
                                    prodDetail.setMaterial02Id(StringUtils.isEmpty(prodDetail.getMaterial02Id()) ? null : prodDetail.getMaterial02Id());
                                    prodDetail.setMaterial03Id(StringUtils.isEmpty(prodDetail.getMaterial03Id()) ? null : prodDetail.getMaterial03Id());
                                    entityManager.persist(prodDetail);
                                    //期間別生産数テーブル更新
                                    if (detail.getMoldUuid() != null) {
                                        periodProductionUpdater.addMoldProductionPeriod(
                                            detail.getMoldUuid(), prodDetail.getComponentId(), report.getReportDate(), prodDetail.getCompleteCount());
                                        periodProductionUpdater.addMachineProductionPeriod(
                                            report.getMachineUuid(), detail.getMoldUuid(), prodDetail.getComponentId(), report.getReportDate(), prodDetail.getCompleteCount());
                                    }
                                    //部品在庫更新
                                    stockUpdater.addComponentStock(
                                        detail.getProductionId(), prodDetail.getProductionDetailId(), prodDetail.getComponentId(), prodDetail.getProcedureId(),
                                        prodDetail.getCompleteCount(), prodDetail.getTblComponentLotRelationVoList());
                                    //材料在庫更新
                                    setMaterialInfo(prodDetail);
                                    if (prodDetail.getMaterial01Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial01Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                            prodDetail.getMaterial01LotNo(), prodDetail.getMaterial01Amount().add(prodDetail.getMaterial01PurgedAmount()));
                                    }
                                    if (prodDetail.getMaterial02Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial02Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                            prodDetail.getMaterial02LotNo(), prodDetail.getMaterial02Amount().add(prodDetail.getMaterial02PurgedAmount()));
                                    }
                                    if (prodDetail.getMaterial03Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial03Id(), detail.getProductionId(), prodDetail.getProductionDetailId(),
                                            prodDetail.getMaterial03LotNo(), prodDetail.getMaterial03Amount().add(prodDetail.getMaterial03PurgedAmount()));
                                    }
                                    //ロット残高更新
                                    lotBalanceUpdater.addBalance(prodDetail.getProductionDetailId(), prodDetail.getCompleteCount());
                                }
                            }
                            //累計ショット数、累計生産時間の追加
                            if (detail.getMoldUuid() != null) {
                                totalUpdater.addMoldCounts(detail.getMoldUuid(), detail.getShotCount() + detail.getDisposedShotCount(), detail.getDurationMinitues());
                            }
                            totalUpdater.addMachineCounts(report.getMachineUuid(), detail.getShotCount() + detail.getDisposedShotCount(), detail.getDurationMinitues());
                        }
                        else {
                            detail.setTblMachineDailyReport2ProdDetailList(null);
                        }
                    }
                    else if (detail.isModified()) {
                        //現在のショット数、生産時間を保存しておく
                        TblMachineDailyReport2Detail currentDetail = currentReport.getReportDetailById(detail.getId());
                        int currentShotCount = currentDetail.getShotCount() + currentDetail.getDisposedShotCount();
                        int currentMinutes = currentDetail.getDurationMinitues();
                        if (detail.getId() != null && !detail.getId().equals("")) {
                            detail.setUpdateDate(new java.util.Date());
                            detail.setUpdateUserUuid(loginUser.getUserUuid());
                            detail.setStzTimes(loginUser.getJavaZoneId());
                            if (detail.getFirstComponentId() != null && detail.getFirstComponentId().equals("")) {
                                detail.setFirstComponentId(null);
                            }
                            detail.setMoldUuid(StringUtils.isEmpty(detail.getMoldUuid()) ? null : detail.getMoldUuid());
                            updateDailyReport2Detail(detail);
                        }
                        if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD  ) {
                            //生産明細の更新
                            if (detail.getTblMachineDailyReport2ProdDetailList() != null) {
                                for (TblMachineDailyReport2ProdDetail prodDetail : detail.getTblMachineDailyReport2ProdDetailList()) {
                                    //変更前の生産明細を取得
                                    TblMachineDailyReport2ProdDetail currentProdDetail = currentReport.getReportProdDetailById(prodDetail.getId());
                                    currentProdDetail.setMaterial01Amount(currentProdDetail.getMaterial01Amount() == null ? BigDecimal.ZERO : currentProdDetail.getMaterial01Amount());
                                    currentProdDetail.setMaterial01PurgedAmount(currentProdDetail.getMaterial01PurgedAmount() == null ? BigDecimal.ZERO : currentProdDetail.getMaterial01PurgedAmount());
                                    currentProdDetail.setMaterial02Amount(currentProdDetail.getMaterial02Amount() == null ? BigDecimal.ZERO : currentProdDetail.getMaterial02Amount());
                                    currentProdDetail.setMaterial02PurgedAmount(currentProdDetail.getMaterial02PurgedAmount() == null ? BigDecimal.ZERO : currentProdDetail.getMaterial02PurgedAmount());
                                    currentProdDetail.setMaterial03Amount(currentProdDetail.getMaterial03Amount() == null ? BigDecimal.ZERO : currentProdDetail.getMaterial03Amount());
                                    currentProdDetail.setMaterial03PurgedAmount(currentProdDetail.getMaterial03PurgedAmount() == null ? BigDecimal.ZERO : currentProdDetail.getMaterial03PurgedAmount());
                                    //生産明細は更新のみ想定
                                    //if (prodDetail.isModified()) { 明細の時間が更新された時、部品別稼働時間も更新する必要があるので、Mofifiedでなくても更新する。
                                    prodDetail.setUpdateDate(new java.util.Date());
                                    prodDetail.setUpdateUserUuid(loginUser.getUserUuid());
                                    prodDetail.setMaterial01Id(StringUtils.isEmpty(prodDetail.getMaterial01Id()) ? null : prodDetail.getMaterial01Id());
                                    prodDetail.setMaterial02Id(StringUtils.isEmpty(prodDetail.getMaterial02Id()) ? null : prodDetail.getMaterial02Id());
                                    prodDetail.setMaterial03Id(StringUtils.isEmpty(prodDetail.getMaterial03Id()) ? null : prodDetail.getMaterial03Id());
                                    updateDailyReport2ProdDetail(prodDetail);
                                    //}
                                    //期間別生産数テーブル更新
                                    //変更前の生産明細があればマイナスする
                                    if (detail.getMoldUuid() != null && currentProdDetail != null) {
                                        periodProductionUpdater.addMoldProductionPeriod(
                                            detail.getMoldUuid(), currentProdDetail.getComponentId(), report.getReportDate(), currentProdDetail.getCompleteCount() * (-1));
                                        periodProductionUpdater.addMachineProductionPeriod(
                                            report.getMachineUuid(), detail.getMoldUuid(), currentProdDetail.getComponentId(), report.getReportDate(), currentProdDetail.getCompleteCount() * (-1));
                                    }
                                    //今回の生産数を追加する
                                    if (detail.getMoldUuid() != null) {
                                        periodProductionUpdater.addMoldProductionPeriod(
                                            detail.getMoldUuid(), prodDetail.getComponentId(), report.getReportDate(), prodDetail.getCompleteCount());
                                        periodProductionUpdater.addMachineProductionPeriod(
                                            report.getMachineUuid(), detail.getMoldUuid(), prodDetail.getComponentId(), report.getReportDate(), prodDetail.getCompleteCount());
                                    }
                                    //部品在庫更新
                                    stockUpdater.addComponentStock(
                                        detail.getProductionId(), prodDetail.getProductionDetailId(), prodDetail.getComponentId(), prodDetail.getProcedureId(),
                                        prodDetail.getCompleteCount() - currentProdDetail.getCompleteCount(), prodDetail.getTblComponentLotRelationVoList());
                                    //材料在庫更新
                                    setMaterialInfo(prodDetail);
                                    if (prodDetail.getMaterial01Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial01Id(), detail.getProductionId(), prodDetail.getProductionDetailId(), prodDetail.getMaterial01LotNo(), 
                                            (prodDetail.getMaterial01Amount().add(prodDetail.getMaterial01PurgedAmount())).subtract(currentProdDetail.getMaterial01Amount().add(currentProdDetail.getMaterial01PurgedAmount()))
                                        );
                                    }
                                    if (prodDetail.getMaterial02Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial02Id(), detail.getProductionId(), prodDetail.getProductionDetailId(), prodDetail.getMaterial02LotNo(), 
                                            (prodDetail.getMaterial02Amount().add(prodDetail.getMaterial02PurgedAmount())).subtract(currentProdDetail.getMaterial02Amount().add(currentProdDetail.getMaterial02PurgedAmount()))
                                        );
                                    }
                                    if (prodDetail.getMaterial03Id() != null) {
                                        stockUpdater.addMaterialStock(prodDetail.getMaterial03Id(), detail.getProductionId(), prodDetail.getProductionDetailId(), prodDetail.getMaterial03LotNo(),
                                            (prodDetail.getMaterial03Amount().add(prodDetail.getMaterial03PurgedAmount())).subtract(currentProdDetail.getMaterial03Amount().add(currentProdDetail.getMaterial03PurgedAmount()))
                                        );
                                    }
                                    //ロット残高更新
                                    lotBalanceUpdater.addBalance(prodDetail.getProductionDetailId(), prodDetail.getCompleteCount() - currentProdDetail.getCompleteCount());
                                }

                            }
                            //ショット数、生産時間の更新(差分を加算する)
                            int shotCountDiff = 0;
                            int minutesDiff = 0;
                            if (currentDetail != null && currentDetail.getMoldUuid() != null) {
                                shotCountDiff = detail.getShotCount() + detail.getDisposedShotCount() - currentShotCount;
                                minutesDiff = detail.getDurationMinitues() - currentMinutes;
                                if (shotCountDiff != 0 || minutesDiff != 0) {
                                    totalUpdater.addMoldCounts(currentDetail.getMoldUuid(), shotCountDiff, minutesDiff);
                                }
                            }
                            if (shotCountDiff != 0 || minutesDiff != 0) {
                                totalUpdater.addMachineCounts(report.getMachineUuid(),  shotCountDiff, minutesDiff);
                            }
                        }
                    }
                }
                for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                    if (detail.isAdded() || detail.isModified()) {
                        if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD  ) {
                            if (detail.getTblMachineDailyReport2ProdDetailList() != null) {
                                for (TblMachineDailyReport2ProdDetail prodDetail : detail.getTblMachineDailyReport2ProdDetailList()) {
                                    // 日別生産不具合を追加
                                    if (prodDetail.getTblProductionDefectDailyList() != null && !prodDetail.getTblProductionDefectDailyList().isEmpty()) {
                                        for (TblProductionDefectDaily defectDaily : prodDetail.getTblProductionDefectDailyList()) {
                                            defectDaily.setMdr2ProdDetailId(prodDetail.getId());
                                        }
                                        tblProductionDefectService.postProductionDefectsDailyFromMdr(prodDetail.getTblProductionDefectDailyList(), report.getReportDate(), prodDetail.getProductionDetailId(), loginUser.getUserUuid());
                                    }
                                    // 生産実績製造ロット番号更新
                                    Production production = getProductionById(detail.getProductionId());
                                    if (production != null && production.getLotNumber() != null && !production.getLotNumber().equals(detail.getTblMachineDailyReport2ProdDetailList().get(0).getLotNumber())) {
                                        production.setLotNumber(detail.getTblMachineDailyReport2ProdDetailList().get(0).getLotNumber());
                                        production.setUpdateDate(new Date());
                                        production.setUpdateUserUuid(loginUser.getUserUuid());
                                        entityManager.merge(production);
                                    }
                                }
                            }
                        }
                    }
                }
                //明細追加のループ２回目。機会日報2テーブル,機会日報2明細テーブル,機会日報2生産明細テーブルの３テーブルをPERSISTしてから次の更新処理を行わないとJPAエラーになるため
                for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                    if (detail.isAdded() || detail.isModified()) {
                        if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD  ) {
                            if (detail.getProductionEndFlg() == 1) {
                                //生産実績の開始日時を調整
                                adjustProductionStartDatetime(detail, loginUser);
                                //生産終了するとき生産実績データを作成
                                endProduction(detail, loginUser);
                            }
                            else {
                                if (detail.isModified()) {
                                    cancelEndProduction(detail, loginUser);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        //金型マスタ、設備マスタのショット数、生産時間を更新
        totalUpdater.update(report.getReportDate());
        //金型期間別生産数テーブル新規追加
        periodProductionUpdater.updateDB();
        //在庫更新
        stockUpdater.updateStock();
        //ロット残高更新
        lotBalanceUpdater.updateLotBalance(loginUser);
    }

    public MachineDailyReport2List getMachineDailyReport2ForV2(String reportDate, String department, String langId) {
        MachineDailyReport2List response = new MachineDailyReport2List();
        java.util.Date paramReportDate = null;
        int paramDepartment = -1;
        if (!StringUtils.isEmpty(reportDate)) {
            paramReportDate = DateFormat.strToDate(reportDate);
            if (paramReportDate == null) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("Invalid date format: reportDate.");
                return response;
            }
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage("Invalid empty value: reportDate.");
            return response;
        }
        
        if (!StringUtils.isEmpty(department)) {
            try {
                paramDepartment = Integer.parseInt(department);
            }
            catch (NumberFormatException ne) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("Invalid integer value: department.");
                return response;
            }
        }
        
        StringBuilder sbSql = new StringBuilder();
        // 該当所属の設備リスト取得
        sbSql.append("SELECT machine FROM MstMachine machine ");
        sbSql.append("WHERE 1 = 1 ");
        if (!StringUtils.isEmpty(department)) {
            sbSql.append(" AND machine.department = :department ");
        }
        sbSql.append("ORDER BY machine.machineId ");
        Query query = entityManager.createQuery(sbSql.toString());
        if (!StringUtils.isEmpty(department)) {
            query.setParameter("department", paramDepartment);
        }
        List<MstMachine> departmentMachineList = query.getResultList();
        
        // 当日機械日報情報取得
        sbSql.delete(0, sbSql.length());
        sbSql.append("SELECT report FROM TblMachineDailyReport2 report ");
        sbSql.append("WHERE report.reportDate = :reportDate ");
        if (!StringUtils.isEmpty(department)) {
            sbSql.append(" AND report.mstMachine.department = :department ");
        }
        sbSql.append("ORDER BY report.mstMachine.machineId ");
        query = entityManager.createQuery(sbSql.toString());
        query.setParameter("reportDate", paramReportDate);
        if (!StringUtils.isEmpty(department)) {
            query.setParameter("department", paramDepartment);
        }
        List<TblMachineDailyReport2> departmentReportList = query.getResultList();
        
        // 所属の選択が不良数の内訳表示に必要
        Map<String, List<TblProductionDefectDaily>> responseDefectMap = new HashMap<>();
        if (!StringUtils.isEmpty(department)) {
            // 該当所属を不良情報の上位カテゴリとして不良分類項目取得
            MstChoiceList defectList = mstChoiceService.getCategories(langId, "tbl_production_defect.defect_type", String.valueOf(paramDepartment), null);
            
            
            // 日別生産不具合数集計する
            sbSql.delete(0, sbSql.length());
            sbSql.append("SELECT report.machineUuid, defect.defectSeq, SUM(defect.quantity) FROM TblProductionDefectDaily defect ");
            sbSql.append("LEFT JOIN TblMachineDailyReport2ProdDetail prodDetail ON prodDetail.id = defect.mdr2ProdDetailId ");
            sbSql.append("LEFT JOIN TblMachineDailyReport2Detail detail ON detail.id = prodDetail.reportDetailId ");
            sbSql.append("LEFT JOIN TblMachineDailyReport2 report ON report.id = detail.reportId ");
            sbSql.append("WHERE report.reportDate = :reportDate ");
            sbSql.append("AND defect.defectDate = :reportDate ");
            sbSql.append("AND report.mstMachine.department = :department ");
            sbSql.append("GROUP BY report.machineUuid, defect.defectSeq ");
            query = entityManager.createQuery(sbSql.toString());
            query.setParameter("reportDate", paramReportDate);
            query.setParameter("department", paramDepartment);
            List list = query.getResultList();
            
            // 結果リスト変換
            Map<String, Map<Integer, Integer>> machineDefectMap = new HashMap<>();
            if (list != null && !list.isEmpty()) {
                Map<Integer, Integer> defectMap = new HashMap<>();
                String oldMachineUuid = "";
                for (int j = 0; j < list.size(); j++) {
                    Object[] objs = (Object[]) list.get(j);
                    if (!oldMachineUuid.equals(objs[0].toString())) {
                        if (StringUtils.isNotEmpty(oldMachineUuid)) {
                            machineDefectMap.put(oldMachineUuid, defectMap);
                        }
                        defectMap = new HashMap<>();
                    }
                    defectMap.put(Integer.parseInt(objs[1].toString()), Integer.parseInt(objs[2].toString()));
                    
                    oldMachineUuid = objs[0].toString();
                    
                    if (j == list.size() - 1) {
                        machineDefectMap.put(oldMachineUuid, defectMap);
                    }
                }
            }
            
            // 設備ごと不良情報設定
            if (departmentMachineList != null && !departmentMachineList.isEmpty()) {
                for (MstMachine machine : departmentMachineList) {
                    if (defectList != null && defectList.getMstChoiceVo() != null && !defectList.getMstChoiceVo().isEmpty()) {
                        List<TblProductionDefectDaily> responseDefectList = new ArrayList<>();
                        if (defectList.getMstChoiceVo().size() > 10) {
                            for (int i = 0; i < 10; i++) {
                                TblProductionDefectDaily defect = new TblProductionDefectDaily();
                                Integer seq = Integer.valueOf(defectList.getMstChoiceVo().get(i).getSeq());
                                defect.setDefectSeq(seq);
                                if (machineDefectMap.containsKey(machine.getUuid()) && machineDefectMap.get(machine.getUuid()).containsKey(seq)) {
                                    defect.setQuantity(machineDefectMap.get(machine.getUuid()).get(seq));
                                } else {
                                    defect.setQuantity(0);
                                }
                                responseDefectList.add(defect);
                            }
                            TblProductionDefectDaily defect = new TblProductionDefectDaily();
                            int otherQuantity = 0;
                            for (int j = 10; j < defectList.getMstChoiceVo().size(); j++) {
                                Integer seq = Integer.valueOf(defectList.getMstChoiceVo().get(j).getSeq());
                                if (machineDefectMap.containsKey(machine.getUuid()) && machineDefectMap.get(machine.getUuid()).containsKey(seq)) {
                                    otherQuantity += machineDefectMap.get(machine.getUuid()).get(seq);
                                }
                            }
                            defect.setDefectSeq(99999);
                            defect.setQuantity(otherQuantity);
                            responseDefectList.add(defect);
                        } else {
                            for (int i = 0; i < defectList.getMstChoiceVo().size(); i++) {
                                TblProductionDefectDaily defect = new TblProductionDefectDaily();
                                Integer seq = Integer.valueOf(defectList.getMstChoiceVo().get(i).getSeq());
                                defect.setDefectSeq(seq);
                                if (machineDefectMap.containsKey(machine.getUuid()) && machineDefectMap.get(machine.getUuid()).containsKey(seq)) {
                                    defect.setQuantity(machineDefectMap.get(machine.getUuid()).get(seq));
                                } else {
                                    defect.setQuantity(0);
                                }
                                responseDefectList.add(defect);
                            }
                        }
                        responseDefectMap.put(machine.getUuid(), responseDefectList);
                    }
                }
            }
        }
        
        List<TblMachineDailyReport2> responseReportList = new ArrayList<>();
        if (departmentMachineList != null && !departmentMachineList.isEmpty()) {
            for (MstMachine machine : departmentMachineList) {
                TblMachineDailyReport2 report = new TblMachineDailyReport2();
                if (departmentReportList != null && !departmentReportList.isEmpty()) {
                    boolean isExist = false;
                    for (TblMachineDailyReport2 dr : departmentReportList) {
                        if (machine.getUuid().equals(dr.getMachineUuid())) {
                            isExist = true;
                            dr.setMachineName(dr.getMstMachine().getMachineName());
                            report = dr;
                            break;
                        }
                    }
                    if (!isExist) {
                        report.setReportDate(paramReportDate);
                        report.setMachineUuid(machine.getUuid());
                        report.setMachineId(machine.getMachineId());
                        report.setMachineName(machine.getMachineName());
                    }
                } else {
                    report.setReportDate(paramReportDate);
                    report.setMachineUuid(machine.getUuid());
                    report.setMachineId(machine.getMachineId());
                    report.setMachineName(machine.getMachineName());
                }
                report.setDefectInfoList(responseDefectMap.get(machine.getUuid()));
                
                responseReportList.add(report);
            }
        }
        
        response.setTblMachineDailyReports(responseReportList);
        response.calcOperaingRates();
        return response;
    }
    
    public MachineDailyReport2List getMachineDailyReport2(String reportDate, String department, String machineUuid) {
        MachineDailyReport2List response = new MachineDailyReport2List();
        StringBuilder sbSql = new StringBuilder();
        java.util.Date paramReportDate = null;
        int paramDepartment = -1;
        sbSql.append("SELECT t FROM TblMachineDailyReport2 t ");
        sbSql.append("WHERE 1 = 1 ");
        if (reportDate != null && !reportDate.equals("")) {
            paramReportDate = DateFormat.strToDate(reportDate);
            if (paramReportDate == null) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("Invalid date format: reportDate.");
                return response;
            }
            else {
                sbSql.append(" AND t.reportDate = :reportDate ");
            }
        }
        if (department != null && !department.equals("")) {
            try {
                paramDepartment = Integer.parseInt(department);
            }
            catch (NumberFormatException ne) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("Invalid integer value: department.");
                return response;
            }
            sbSql.append(" AND t.mstMachine.department = :department ");
        }
        if (machineUuid != null && !machineUuid.equals("")) {
            sbSql.append(" AND t.machineUuid = :machineUuid ");
        }
        sbSql.append(" ORDER BY t.mstMachine.machineName ");
        Query query = entityManager.createQuery(sbSql.toString());
        if (paramReportDate != null) {
            query.setParameter("reportDate", paramReportDate);
        }
        if (paramDepartment != -1) {
            query.setParameter("department", paramDepartment);
        }
        if (machineUuid != null && !machineUuid.equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        List list = query.getResultList();
        response.setTblMachineDailyReports(list);
        java.util.Date inReportDate = DateFormat.strToDate(reportDate);
        response.formatJson(inReportDate);
        response.calcOperaingRates();
        
        List<String> productionIds = new ArrayList<>();
        // 日別生産不具合取得
        sbSql = new StringBuilder();
        sbSql.append(" SELECT daily FROM TblProductionDefectDaily daily ");
        sbSql.append(" WHERE 1=1 ");
        if (paramReportDate != null) {
            sbSql.append(" AND daily.defectDate = :defectDate ");
        }
        query = entityManager.createQuery(sbSql.toString());
        if (paramReportDate != null) {
            query.setParameter("defectDate", paramReportDate);
        }
        List<TblProductionDefectDaily> dailyList = query.getResultList();
        if (list != null && !list.isEmpty()) {
            for (TblMachineDailyReport2 report : response.getTblMachineDailyReports()) {
                if (report.getTblMachineDailyReport2DetailList() != null) {
                    for (TblMachineDailyReport2Detail reportDetail : report.getTblMachineDailyReport2DetailList()) {
                        if (reportDetail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD ) {
                            productionIds.add(reportDetail.getProductionId());
                            // ロット番号設定
                            Production production = getProductionById(reportDetail.getProductionId());
                            reportDetail.setLotNumber(production == null ? null : production.getLotNumber());
                            if (reportDetail.getTblMachineDailyReport2ProdDetailList() != null) {
                                for (TblMachineDailyReport2ProdDetail reportProdDetail : reportDetail.getTblMachineDailyReport2ProdDetailList()) {
                                    List<TblProductionDefectDaily> prodDailyList = new ArrayList<>();
                                    for (TblProductionDefectDaily daily : dailyList) {
                                        if (reportProdDetail.getId().equals(daily.getMdr2ProdDetailId())) {
                                            prodDailyList.add(daily);
                                        }
                                    }
                                    reportProdDetail.setTblProductionDefectDailyList(prodDailyList);
                                    
                                    // 前ロット番号取得
                                    MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(reportProdDetail.getComponentId(), reportProdDetail.getProcedureCode());
                                    if (prevMstProcedure != null) {
                                        // 部品ロット番号リスト
                                        TblProductionList lotNumberList = tblProductionService.getProcuctionLotListByPrevProcedureId(reportProdDetail.getComponentId(), prevMstProcedure.getId(), report.getReportDate());
                                        if (lotNumberList != null) {
                                            reportProdDetail.setComponentLotNumberList(lotNumberList.getProductions());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // 生産場所取得設定する
        if (productionIds.size() > 0) {
            sbSql = new StringBuilder();
            sbSql.append(" SELECT p FROM Production p ");
            sbSql.append(" WHERE p.id IN :ids ");
            query = entityManager.createQuery(sbSql.toString());
            query.setParameter("ids", productionIds);
            List<Production> produnctionList = query.getResultList();
            if (produnctionList != null && !produnctionList.isEmpty()) {
                if (list != null && !list.isEmpty()) {
                    for (TblMachineDailyReport2 report : response.getTblMachineDailyReports()) {
                        if (report.getTblMachineDailyReport2DetailList() != null) {
                            for (TblMachineDailyReport2Detail reportDetail : report.getTblMachineDailyReport2DetailList()) {
                                if (reportDetail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD ) {
                                    for (Production production : produnctionList) {
                                        if (reportDetail.getProductionId().equals(production.getId())) {
                                            reportDetail.setProductionProdDepartment(production.getProdDepartment());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return response;
    }
    
    public MachineDailyReport2List collectDepartmentMachineDailyReport2(String reportDate, String department) {
        MachineDailyReport2List response = new MachineDailyReport2List();
        //設備マスタから該当部署の設備一覧を取得
        StringBuilder sbSql = new StringBuilder();
        java.util.Date paramReportDate = null;
        int paramDepartment = -1;
        sbSql.append("SELECT t FROM MstMachine t ");
        sbSql.append("WHERE 1 = 1 ");
        if (reportDate != null && !reportDate.equals("")) {
            paramReportDate = DateFormat.strToDate(reportDate);
            if (paramReportDate == null) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("Invalid date format: reportDate.");
                return response;
            }
        }
        if (department != null && !department.equals("")) {
            try {
                paramDepartment = Integer.parseInt(department);
            }
            catch (NumberFormatException ne) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage("Invalid integer value: department.");
                return response;
            }
            sbSql.append(" AND t.department = :department ");
        }
        sbSql.append(" ORDER BY t.machineName ");
        Query query = entityManager.createQuery(sbSql.toString());
        if (paramDepartment != -1) {
            query.setParameter("department", paramDepartment);
        }
        List<MstMachine> machineList = (List<MstMachine>)query.getResultList();
        List<TblMachineDailyReport2> reportList = new ArrayList<>();
        //取得した設備すべてに機械日報2エンティティを作成する。
        for (MstMachine machine : machineList) {
            TblMachineDailyReport2 report = new TblMachineDailyReport2();
            report.setMachineUuid(machine.getUuid());
            report.setMachineId(machine.getMachineId());
            report.setMachineName(machine.getMachineName());
            reportList.add(report);
        }
        response.setTblMachineDailyReports(reportList);
        try {
            List<TblMachineDailyReport2> expectedReportList = getExpectedTotalTime(response, reportDate);
            response.setTblMachineDailyReports(expectedReportList);
        }
        catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    
    /**
     * 機械日報登録されていない作業、生産時間まで含めた時間を取得する
     * @param reports
     * @param reportDate 
     */
    private List<TblMachineDailyReport2> getExpectedTotalTime(MachineDailyReport2List reports, String reportDate) throws Exception {
        List<TblMachineDailyReport2> list = new ArrayList<>();
        //業務開始時刻、終了時刻の取得
        CnfSystem cnfSystem =  cnfSystemService.findByKey("system", "business_start_time");
        if (cnfSystem == null) {
            //reports.setError(true);
            //reports.setErrorCode(ErrorMessages.E201_APPLICATION);
            //reports.setErrorMessage("Business start time is not defined in system setting.");
            throw new Exception("Business start time is not defined in system setting.");
        }
        String businessStartTime = cnfSystem.getConfigValue();
        if (businessStartTime.length() == 4) {
            businessStartTime = "0" + businessStartTime;
        }
        java.util.Date startTime = DateFormat.strToDatetime(reportDate + " " + businessStartTime + ":00");
        java.util.Date endTime = DateFormat.getAfterDay(startTime);
        
        List<String> machineUuidList = new ArrayList<>();
        for (int i = 0; i < reports.getTblMachineDailyReports().size(); i++) {
            machineUuidList.add(reports.getTblMachineDailyReports().get(i).getMachineUuid());
            //TblMachineDailyReport2 expectedReport = this.collectMachineDailyReport2Core(report.getMachineUuid(), startTime, endTime);
            //list.add(expectedReport);
        }
        if (machineUuidList.size() > 0) {
            list = this.collectMachineDailyReport2Multi(machineUuidList, startTime, endTime);
        }
        return list;
    }
    
    private List<TblMachineDailyReport2> collectMachineDailyReport2Multi(List<String> machineUuid, java.util.Date startTime, java.util.Date endTime) {
//        MachineDailyReport2Res report = new MachineDailyReport2Res();
        List<TblMachineDailyReport2> reports = new ArrayList<>();
        for (int i = 0; i < machineUuid.size(); i++) {
            TblMachineDailyReport2 report = new TblMachineDailyReport2();
            report.setMachineUuid(machineUuid.get(i));
            reports.add(report);
            report.setTblMachineDailyReport2DetailList(new ArrayList<TblMachineDailyReport2Detail>());
            //明細リストの中から設備UUIDが等しいものを取得
        }
        //List<TblMachineDailyReport2Detail> detailList = new ArrayList<>();
        //作業抽出
        // 抽出条件(1 or 2)
        // 1.開始時刻または終了時刻が業務時間内のもの
        // 2.開始時刻が業務終了時刻より前で終了時刻のないもの
        StringBuilder sqlWork = new StringBuilder();
        sqlWork.append("SELECT tblWork FROM TblWork tblWork ");
        sqlWork.append(" LEFT JOIN FETCH tblWork.mstWorkPhase ");
        sqlWork.append( " LEFT JOIN FETCH tblWork.mstComponent ");
        sqlWork.append( " LEFT JOIN FETCH tblWork.mstMold ");
        sqlWork.append( " LEFT JOIN FETCH tblWork.mstMachine ");
        sqlWork.append( " LEFT JOIN FETCH tblWork.mstUser ");
        sqlWork.append( " WHERE tblWork.mstMachine.uuid IN :machineUuid ");
        sqlWork.append( " AND ((tblWork.startDatetime >= :startTime1 AND tblWork.startDatetime < :endTime1 "); //条件1
        sqlWork.append( "   OR tblWork.endDatetime > :startTime2 AND tblWork.endDatetime <= :endTime2) "); //条件1
        sqlWork.append( " OR (tblWork.startDatetime < :startTime3 AND tblWork.endDatetime IS NULL)) "); //条件2
        sqlWork.append( " AND NOT EXISTS (SELECT t FROM TblMachineDailyReport2Detail t WHERE t.workId = tblWork.id ");//日報に登録済みのものは対象外
        sqlWork.append(     " AND t.startDatetime >= :startTime4 AND t.endDatetime <= :endTime3 ) ");
        Query query = entityManager.createQuery(sqlWork.toString());
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("startTime1", startTime);
        query.setParameter("startTime2", startTime);
        query.setParameter("startTime3", startTime);
        query.setParameter("startTime4", startTime);
        query.setParameter("endTime1", endTime);
        query.setParameter("endTime2", endTime);
        query.setParameter("endTime3", endTime);
        List<TblWork> workList = query.getResultList();
        for (TblWork tblWork : workList) {
            TblMachineDailyReport2Detail detail = new TblMachineDailyReport2Detail();
            detail.setStartDatetime(tblWork.getStartDatetime().compareTo(startTime) < 0 ? startTime : tblWork.getStartDatetime()); //業務開始時刻より前なら業務開始時刻をセット
            detail.setEndDatetime((tblWork.getEndDatetime() == null || tblWork.getEndDatetime().compareTo(endTime) > 0) ? endTime : tblWork.getEndDatetime()); //業務終了時刻より後なら業務終了時刻をセット
            detail.calcDurationMinutes();
            detail.setWorkPhaseId(tblWork.getWorkPhaseId());
            detail.setMstWorkPhase(tblWork.getMstWorkPhase());
            detail.setOperatingFlg(0);
            detail.setDetailType(TblMachineDailyReport2.DETAIL_TYPE_WORK);
            detail.setWorkId(tblWork.getId());
            detail.setFirstComponentId(tblWork.getComponentId());
            detail.setMstComponent(tblWork.getMstComponent());
            detail.setMoldUuid(tblWork.getMoldUuid());
            detail.setMstMold(tblWork.getMstMold());
            detail.setWorkerUuid(tblWork.getPersonUuid());
            detail.setMstUser(tblWork.getMstUser());
            detail.setWorkEndFlg(tblWork.getEndDatetime() == null ? 0 : 1);
            //設備UUIDが等しいreportの明細に追加
            for (TblMachineDailyReport2 report : reports) {
                if (report.getMachineUuid().equals(tblWork.getMachineUuid())) {
                    report.getTblMachineDailyReport2DetailList().add(detail);
                }
            }
            //detailList.add(detail);
        }
        //生産実績を取得
        sqlWork = new StringBuilder();
        sqlWork.append(" SELECT t FROM Production t ");
        sqlWork.append( " WHERE t.machineUuid IN :machineUuid ");
        sqlWork.append( " AND ((t.startDatetime >= :startTime1 AND t.startDatetime < :endTime1 "); //条件1
        sqlWork.append( "   OR t.endDatetime > :startTime2 AND t.endDatetime <= :endTime2) "); //条件1
        sqlWork.append( " OR (t.startDatetime < :startTime3 AND t.endDatetime IS NULL)) "); //条件2
        sqlWork.append( " AND NOT EXISTS (SELECT rt FROM TblMachineDailyReport2Detail rt WHERE rt.productionId = t.id "); //その日の日報に登録済みのものは対象外
        sqlWork.append(     " AND rt.startDatetime >= :startTime4 AND rt.endDatetime <= :endTime3 ) ");
        query = entityManager.createQuery(sqlWork.toString());
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("startTime1", startTime);
        query.setParameter("startTime2", startTime);
        query.setParameter("startTime3", startTime);
        query.setParameter("startTime4", startTime);
        query.setParameter("endTime1", endTime);
        query.setParameter("endTime2", endTime);
        query.setParameter("endTime3", endTime);
        //query.setParameter("reportDate", endTime);
        List <Production> productions = query.getResultList();
        for (Production production: productions) {
            production.sortDetailByComponentCode(); //部品コードで明細をソートしておく
            TblMachineDailyReport2Detail detail = new TblMachineDailyReport2Detail();
            detail.setStartDatetime(production.getStartDatetime().compareTo(startTime) < 0 ? startTime : production.getStartDatetime()); //業務開始時刻より前なら業務開始時刻をセット
            detail.setEndDatetime((production.getEndDatetime() == null || production.getEndDatetime().compareTo(endTime) > 0) ? endTime : production.getEndDatetime()); //業務終了時刻より後なら業務終了時刻をセット
            detail.calcDurationMinutes();
            detail.setWorkPhaseId(production.getWorkPhaseId());
            detail.setMstWorkPhase(production.getMstWorkPhase());
            detail.setOperatingFlg(1);
            detail.setDetailType(TblMachineDailyReport2.DETAIL_TYPE_PROD);
            detail.setProductionId(production.getId());
            detail.setMoldUuid(production.getMoldUuid());
            detail.setMstMold(production.getMstMold());
            detail.setWorkerUuid(production.getPersonUuid());
            detail.setMstUser(production.getMstUser());
            detail.setShotCount(production.getShotCount());
            //捨てショットは初日の機会日報のときだけセット
            boolean firstDay = production.getStartDatetime().compareTo(startTime) >= 0 && production.getStartDatetime().compareTo(endTime) <= 0;
            if (firstDay) {
                detail.setDisposedShotCount(production.getDisposedShotCount());
            }
            detail.setTblMachineDailyReport2ProdDetailList(new ArrayList<TblMachineDailyReport2ProdDetail>());
            for (int i = 0; i < production.getProductionDetails().size(); i++) {
                ProductionDetail prodDetail = production.getProductionDetails().get(i);
                //部品コードでソートした１行目を第一部品にセット
                if (i == 0) {
                    detail.setFirstComponentId(prodDetail.getComponentId());
                    detail.setMstComponent(prodDetail.getMstComponent());
                }
                //機械日報2生産明細オブジェクトへコピー
                TblMachineDailyReport2ProdDetail report2ProdDetail = new TblMachineDailyReport2ProdDetail();
                report2ProdDetail.setProductionDetailId(prodDetail.getId());
                report2ProdDetail.setComponentId(prodDetail.getComponentId());
                report2ProdDetail.setMstComponent(prodDetail.getMstComponent());
                report2ProdDetail.setProcedureId(prodDetail.getProcedureId());
                report2ProdDetail.setMstProcedure(prodDetail.getMstProcedure());
                report2ProdDetail.setCountPerShot(prodDetail.getCountPerShot());
                report2ProdDetail.setMaterial01Id(prodDetail.getMaterial01Id());
                report2ProdDetail.setMstMaterial01(prodDetail.getMstMaterial01());
                if (firstDay) { //パージ量は初日の機械日報のときだけセット
                    report2ProdDetail.setMaterial01PurgedAmount(prodDetail.getMaterial01PurgedAmount());
                }
                report2ProdDetail.setMaterial02Id(prodDetail.getMaterial02Id());
                report2ProdDetail.setMstMaterial02(prodDetail.getMstMaterial02());
                if (firstDay) { //パージ量は初日の機械日報のときだけセット
                    report2ProdDetail.setMaterial02PurgedAmount(prodDetail.getMaterial02PurgedAmount());
                }
                report2ProdDetail.setMaterial03Id(prodDetail.getMaterial03Id());
                report2ProdDetail.setMstMaterial03(prodDetail.getMstMaterial03());
                if (firstDay) { //パージ量は初日の機械日報のときだけセット
                    report2ProdDetail.setMaterial03PurgedAmount(prodDetail.getMaterial03PurgedAmount());
                }
                detail.getTblMachineDailyReport2ProdDetailList().add(report2ProdDetail);
            }
            //設備UUIDが等しいreportの明細に追加
            for (TblMachineDailyReport2 report : reports) {
                if (report.getMachineUuid().equals(production.getMachineUuid())) {
                    report.getTblMachineDailyReport2DetailList().add(detail);
                }
            }
//            detailList.add(detail);
        }
//        report.setMachineUuid(machineUuid);
//        report.setTblMachineDailyReport2DetailList(detailList);
        for (TblMachineDailyReport2 report : reports) {
            //合計時間を計算
            report.calcTotal();
            //開始時刻でソート
            //report.sortDatailByStartTime();
            //Json用に整形
            //report.formatJson();
        }
        return reports;
    }

    @Transactional
    private void updateDailyReport2(TblMachineDailyReport2 report) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TblMachineDailyReport2 t SET ");
        sql.append(" t.totalTimeMinutes = :totalTimeMinutes, ");
        sql.append(" t.totalOperatingMinutes = :totalOperatingMinutes, ");
        sql.append(" t.totalDowntimeMinutes = :totalDowntimeMinutes, ");
        sql.append(" t.totalPlannedDowntimeMinutes = :totalPlannedDowntimeMinutes, ");
        sql.append(" t.totalUnplannedDowntimeMinutes = :totalUnplannedDowntimeMinutes, ");
        sql.append(" t.totalShotCount = :totalShotCount, ");
        sql.append(" t.totalCompleteCount = :totalCompleteCount, ");
        sql.append(" t.totalDefectCount = :totalDefectCount, ");
        sql.append(" t.totalDefectCountType01 = :totalDefectCountType01, ");
        sql.append(" t.totalDefectCountType02 = :totalDefectCountType02, ");
        sql.append(" t.totalDefectCountType03 = :totalDefectCountType03, ");
        sql.append(" t.totalDefectCountType04 = :totalDefectCountType04, ");
        sql.append(" t.totalDefectCountType05 = :totalDefectCountType05, ");
        sql.append(" t.totalDefectCountType06 = :totalDefectCountType06, ");
        sql.append(" t.totalDefectCountType07 = :totalDefectCountType07, ");
        sql.append(" t.totalDefectCountType08 = :totalDefectCountType08, ");
        sql.append(" t.totalDefectCountType09 = :totalDefectCountType09, ");
        sql.append(" t.totalDefectCountType10 = :totalDefectCountType10, ");
        sql.append(" t.totalDefectCountOther = :totalDefectCountOther, ");
        sql.append(" t.reportPersonUuid = :reportPersonUuid, ");
        sql.append(" t.updateDate = :updateDate, ");
        sql.append(" t.updateUserUuid = :updateUserUuid ");
        sql.append(" WHERE t.id = :id");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("totalTimeMinutes", report.getTotalTimeMinutes());
        query.setParameter("totalOperatingMinutes", report.getTotalOperatingMinutes());
        query.setParameter("totalDowntimeMinutes", report.getTotalDowntimeMinutes());
        query.setParameter("totalPlannedDowntimeMinutes", report.getTotalPlannedDowntimeMinutes());
        query.setParameter("totalUnplannedDowntimeMinutes", report.getTotalUnplannedDowntimeMinutes());
        query.setParameter("totalShotCount", report.getTotalShotCount());
        query.setParameter("totalCompleteCount", report.getTotalCompleteCount());
        query.setParameter("totalDefectCount", report.getTotalDefectCount());
        query.setParameter("totalDefectCountType01", report.getTotalDefectCountType01());
        query.setParameter("totalDefectCountType02", report.getTotalDefectCountType02());
        query.setParameter("totalDefectCountType03", report.getTotalDefectCountType03());
        query.setParameter("totalDefectCountType04", report.getTotalDefectCountType04());
        query.setParameter("totalDefectCountType05", report.getTotalDefectCountType05());
        query.setParameter("totalDefectCountType06", report.getTotalDefectCountType06());
        query.setParameter("totalDefectCountType07", report.getTotalDefectCountType07());
        query.setParameter("totalDefectCountType08", report.getTotalDefectCountType08());
        query.setParameter("totalDefectCountType09", report.getTotalDefectCountType09());
        query.setParameter("totalDefectCountType10", report.getTotalDefectCountType10());
        query.setParameter("totalDefectCountOther", report.getTotalDefectCountOther());
        query.setParameter("reportPersonUuid", report.getReportPersonUuid());
        query.setParameter("updateDate", report.getUpdateDate());
        query.setParameter("updateUserUuid", report.getUpdateUserUuid());
        query.setParameter("id", report.getId());
        query.executeUpdate();
    }

    @Transactional
    private void updateDailyReport2Detail(TblMachineDailyReport2Detail detail) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TblMachineDailyReport2Detail t SET ");
        sql.append(" t.startDatetime = :startDatetime, ");
        sql.append(" t.startDatetimeStz = :startDatetimeStz, ");
        sql.append(" t.endDatetime = :endDatetime, ");
        sql.append(" t.endDatetimeStz = :endDatetimeStz, ");
        sql.append(" t.durationMinitues = :durationMinitues, ");
        sql.append(" t.operatingFlg = :operatingFlg, ");
        sql.append(" t.detailType = :detailType, ");
        sql.append(" t.workId = :workId, ");
        sql.append(" t.productionId = :productionId, ");
        sql.append(" t.productionEndFlg = :productionEndFlg, ");
        sql.append(" t.workerUuid = :workerUuid, ");
        sql.append(" t.firstComponentId = :firstComponentId, ");
        sql.append(" t.moldUuid = :moldUuid, ");
        sql.append(" t.shotCount = :shotCount, ");
        sql.append(" t.disposedShotCount = :disposedShotCount, ");
        sql.append(" t.workPhaseId = :workPhaseId, ");
        sql.append(" t.machineDowntimeId = :machineDowntimeId, ");
        sql.append(" t.downtimeComment = :downtimeComment, ");
        sql.append(" t.updateDate = :updateDate, ");
        sql.append(" t.updateUserUuid = :updateUserUuid ");
        sql.append(" WHERE t.id = :id");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("startDatetime", detail.getStartDatetime());
        query.setParameter("startDatetimeStz", detail.getStartDatetimeStz());
        query.setParameter("endDatetime", detail.getEndDatetime());
        query.setParameter("endDatetimeStz", detail.getEndDatetimeStz());
        query.setParameter("durationMinitues", detail.getDurationMinitues());
        query.setParameter("operatingFlg", detail.getOperatingFlg());
        query.setParameter("detailType", detail.getDetailType());
        query.setParameter("workId", detail.getWorkId());
        query.setParameter("productionId", detail.getProductionId());
        query.setParameter("productionEndFlg", detail.getProductionEndFlg());
        query.setParameter("workerUuid", detail.getWorkerUuid());
        query.setParameter("firstComponentId", detail.getFirstComponentId());
        query.setParameter("moldUuid", detail.getMoldUuid());
        query.setParameter("shotCount", detail.getShotCount());
        query.setParameter("disposedShotCount", detail.getDisposedShotCount());
        query.setParameter("workPhaseId", detail.getWorkPhaseId());
        query.setParameter("machineDowntimeId", detail.getMachineDowntimeId());
        query.setParameter("downtimeComment", detail.getDowntimeComment());
        query.setParameter("updateDate", detail.getUpdateDate());
        query.setParameter("updateUserUuid", detail.getUpdateUserUuid());
        query.setParameter("id", detail.getId());
        query.executeUpdate();
    }

    @Transactional
    private void updateDailyReport2ProdDetail(TblMachineDailyReport2ProdDetail prodDetail) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE TblMachineDailyReport2ProdDetail t SET ");
        sql.append(" t.componentId = :componentId, ");
        sql.append(" t.procedureId = :procedureId, ");
        sql.append(" t.countPerShot = :countPerShot, ");
        sql.append(" t.defectCount = :defectCount, ");
        sql.append(" t.completeCount = :completeCount, ");
        sql.append(" t.material01Id = :material01Id, ");
        sql.append(" t.material01LotNo = :material01LotNo, ");
        sql.append(" t.material01Amount = :material01Amount, ");
        sql.append(" t.material01PurgedAmount = :material01PurgedAmount, ");
        sql.append(" t.material02Id = :material02Id, ");
        sql.append(" t.material02LotNo = :material02LotNo, ");
        sql.append(" t.material02Amount = :material02Amount, ");
        sql.append(" t.material02PurgedAmount = :material02PurgedAmount, ");
        sql.append(" t.material03Id = :material03Id, ");
        sql.append(" t.material03LotNo = :material03LotNo, ");
        sql.append(" t.material03Amount = :material03Amount, ");
        sql.append(" t.material03PurgedAmount = :material03PurgedAmount, ");
        sql.append(" t.componentOperatingMinutes = :componentOperatingMinutes, ");
        sql.append(" t.defectCountType01 = :defectCountType01, ");
        sql.append(" t.defectCountType02 = :defectCountType02, ");
        sql.append(" t.defectCountType03 = :defectCountType03, ");
        sql.append(" t.defectCountType04 = :defectCountType04, ");
        sql.append(" t.defectCountType05 = :defectCountType05, ");
        sql.append(" t.defectCountType06 = :defectCountType06, ");
        sql.append(" t.defectCountType07 = :defectCountType07, ");
        sql.append(" t.defectCountType08 = :defectCountType08, ");
        sql.append(" t.defectCountType09 = :defectCountType09, ");
        sql.append(" t.defectCountType10 = :defectCountType10, ");
        sql.append(" t.defectCountOther = :defectCountOther, ");
        sql.append(" t.updateDate = :updateDate, ");
        sql.append(" t.updateUserUuid = :updateUserUuid ");
        sql.append(" WHERE t.id = :id");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", prodDetail.getComponentId());
        query.setParameter("procedureId", prodDetail.getProcedureId());
        query.setParameter("countPerShot", prodDetail.getCountPerShot());
        query.setParameter("defectCount", prodDetail.getDefectCount());
        query.setParameter("completeCount", prodDetail.getCompleteCount());
        query.setParameter("material01Id", prodDetail.getMaterial01Id());
        query.setParameter("material01LotNo", prodDetail.getMaterial01LotNo());
        query.setParameter("material01Amount", prodDetail.getMaterial01Amount());
        query.setParameter("material01PurgedAmount", prodDetail.getMaterial01PurgedAmount());
        query.setParameter("material02Id", prodDetail.getMaterial02Id());
        query.setParameter("material02LotNo", prodDetail.getMaterial02LotNo());
        query.setParameter("material02Amount", prodDetail.getMaterial02Amount());
        query.setParameter("material02PurgedAmount", prodDetail.getMaterial02PurgedAmount());
        query.setParameter("material03Id", prodDetail.getMaterial03Id());
        query.setParameter("material03LotNo", prodDetail.getMaterial03LotNo());
        query.setParameter("material03Amount", prodDetail.getMaterial03Amount());
        query.setParameter("material03PurgedAmount", prodDetail.getMaterial03PurgedAmount());
        query.setParameter("componentOperatingMinutes", prodDetail.getComponentOperatingMinutes());
        query.setParameter("defectCountType01", prodDetail.getDefectCountType01());
        query.setParameter("defectCountType02", prodDetail.getDefectCountType02());
        query.setParameter("defectCountType03", prodDetail.getDefectCountType03());
        query.setParameter("defectCountType04", prodDetail.getDefectCountType04());
        query.setParameter("defectCountType05", prodDetail.getDefectCountType05());
        query.setParameter("defectCountType06", prodDetail.getDefectCountType06());
        query.setParameter("defectCountType07", prodDetail.getDefectCountType07());
        query.setParameter("defectCountType08", prodDetail.getDefectCountType08());
        query.setParameter("defectCountType09", prodDetail.getDefectCountType09());
        query.setParameter("defectCountType10", prodDetail.getDefectCountType10());
        query.setParameter("defectCountOther", prodDetail.getDefectCountOther());
        query.setParameter("updateDate", prodDetail.getUpdateDate());
        query.setParameter("updateUserUuid", prodDetail.getUpdateUserUuid());
        query.setParameter("id", prodDetail.getId());
        query.executeUpdate();
    }

    private MachineDailyReport2Res collectMachineDailyReport2Core(String machineUuid, java.util.Date startTime, java.util.Date endTime, java.util.Date inReportDate) {
        MachineDailyReport2Res report = new MachineDailyReport2Res();
        List<TblMachineDailyReport2Detail> detailList = new ArrayList<>();
        //作業抽出
        // 抽出条件(1 or 2)
        // 1.開始時刻または終了時刻が業務時間内のもの
        // 2.開始時刻が業務終了時刻より前で終了時刻のないもの
        StringBuilder sqlWork = new StringBuilder();
        sqlWork.append("SELECT tblWork FROM TblWork tblWork ");
        sqlWork.append(" LEFT JOIN FETCH tblWork.mstWorkPhase ");
        sqlWork.append( " LEFT JOIN FETCH tblWork.mstComponent ");
        sqlWork.append( " LEFT JOIN FETCH tblWork.mstMold ");
        sqlWork.append( " LEFT JOIN FETCH tblWork.mstMachine ");
        sqlWork.append( " LEFT JOIN FETCH tblWork.mstUser ");
        sqlWork.append( " WHERE tblWork.mstMachine.uuid = :machineUuid ");
        sqlWork.append( " AND ((tblWork.startDatetime >= :startTime1 AND tblWork.startDatetime < :endTime1 "); //条件1
        sqlWork.append( "   OR tblWork.endDatetime > :startTime2 AND tblWork.endDatetime <= :endTime2) "); //条件1
        sqlWork.append( " OR (tblWork.startDatetime < :startTime3 AND tblWork.endDatetime IS NULL)) "); //条件2
        sqlWork.append( " AND NOT EXISTS (SELECT t FROM TblMachineDailyReport2Detail t WHERE t.workId = tblWork.id ");//日報に登録済みのものは対象外
        sqlWork.append(     " AND t.startDatetime >= :startTime4 AND t.endDatetime <= :endTime3 ) ");
        Query query = entityManager.createQuery(sqlWork.toString());
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("startTime1", startTime);
        query.setParameter("startTime2", startTime);
        query.setParameter("startTime3", startTime);
        query.setParameter("startTime4", startTime);
        query.setParameter("endTime1", endTime);
        query.setParameter("endTime2", endTime);
        query.setParameter("endTime3", endTime);
        List<TblWork> workList = query.getResultList();
        for (TblWork tblWork : workList) {
            TblMachineDailyReport2Detail detail = new TblMachineDailyReport2Detail();
            detail.setStartDatetime(tblWork.getStartDatetime().compareTo(startTime) < 0 ? startTime : tblWork.getStartDatetime()); //業務開始時刻より前なら業務開始時刻をセット
            detail.setEndDatetime((tblWork.getEndDatetime() == null || tblWork.getEndDatetime().compareTo(endTime) > 0) ? endTime : tblWork.getEndDatetime()); //業務終了時刻より後なら業務終了時刻をセット
            detail.calcDurationMinutes();
            detail.setWorkPhaseId(tblWork.getWorkPhaseId());
            detail.setMstWorkPhase(tblWork.getMstWorkPhase());
            detail.setOperatingFlg(0);
            detail.setDetailType(TblMachineDailyReport2.DETAIL_TYPE_WORK);
            detail.setWorkId(tblWork.getId());
            detail.setFirstComponentId(tblWork.getComponentId());
            detail.setMstComponent(tblWork.getMstComponent());
            detail.setMoldUuid(tblWork.getMoldUuid());
            detail.setMstMold(tblWork.getMstMold());
            detail.setWorkerUuid(tblWork.getPersonUuid());
            detail.setMstUser(tblWork.getMstUser());
            detail.setWorkEndFlg(tblWork.getEndDatetime() == null ? 0 : 1);
            detailList.add(detail);
        }
        //生産実績を取得
        sqlWork = new StringBuilder();
        sqlWork.append(" SELECT t FROM Production t ");
        sqlWork.append( " WHERE t.machineUuid = :machineUuid ");
        sqlWork.append( " AND ((t.startDatetime >= :startTime1 AND t.startDatetime < :endTime1 "); //条件1
        sqlWork.append( "   OR t.endDatetime > :startTime2 AND t.endDatetime <= :endTime2) "); //条件1
        sqlWork.append( " OR (t.startDatetime < :startTime3 AND t.endDatetime IS NULL)) "); //条件2
        sqlWork.append( " AND NOT EXISTS (SELECT rt FROM TblMachineDailyReport2Detail rt WHERE rt.productionId = t.id "); //その日の日報に登録済みのものは対象外
        sqlWork.append(     " AND rt.startDatetime >= :startTime4 AND rt.endDatetime <= :endTime3 ) ");
        query = entityManager.createQuery(sqlWork.toString());
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("startTime1", startTime);
        query.setParameter("startTime2", startTime);
        query.setParameter("startTime3", startTime);
        query.setParameter("startTime4", startTime);
        query.setParameter("endTime1", endTime);
        query.setParameter("endTime2", endTime);
        query.setParameter("endTime3", endTime);
        //query.setParameter("reportDate", endTime);
        List <Production> productions = query.getResultList();
        for (Production production: productions) {
            production.sortDetailByComponentCode(); //部品コードで明細をソートしておく
            TblMachineDailyReport2Detail detail = new TblMachineDailyReport2Detail();
            detail.setStartDatetime(production.getStartDatetime().compareTo(startTime) < 0 ? startTime : production.getStartDatetime()); //業務開始時刻より前なら業務開始時刻をセット
            detail.setEndDatetime((production.getEndDatetime() == null || production.getEndDatetime().compareTo(endTime) > 0) ? endTime : production.getEndDatetime()); //業務終了時刻より後なら業務終了時刻をセット
            detail.calcDurationMinutes();
            detail.setWorkPhaseId(production.getWorkPhaseId());
            detail.setMstWorkPhase(production.getMstWorkPhase());
            detail.setOperatingFlg(1);
            detail.setDetailType(TblMachineDailyReport2.DETAIL_TYPE_PROD);
            detail.setProductionId(production.getId());
            detail.setMoldUuid(production.getMoldUuid());
            detail.setMstMold(production.getMstMold());
            detail.setWorkerUuid(production.getPersonUuid());
            detail.setMstUser(production.getMstUser());
            detail.setShotCount(production.getShotCount());
            detail.setProductionProdDepartment(production.getProdDepartment());
            detail.setDirectionCode(production.getTblDirection() == null ? null : production.getTblDirection().getDirectionCode());
            //捨てショットは初日の機会日報のときだけセット
            boolean firstDay = production.getStartDatetime().compareTo(startTime) >= 0 && production.getStartDatetime().compareTo(endTime) <= 0;
            if (firstDay) {
                detail.setDisposedShotCount(production.getDisposedShotCount());
            }
            //ロット番号
            detail.setLotNumber(production.getLotNumber());
            detail.setTblMachineDailyReport2ProdDetailList(new ArrayList<TblMachineDailyReport2ProdDetail>());
            for (int i = 0; i < production.getProductionDetails().size(); i++) {
                ProductionDetail prodDetail = production.getProductionDetails().get(i);
                //部品コードでソートした１行目を第一部品にセット
                if (i == 0) {
                    detail.setFirstComponentId(prodDetail.getComponentId());
                    detail.setMstComponent(prodDetail.getMstComponent());
                }
                //機械日報2生産明細オブジェクトへコピー
                TblMachineDailyReport2ProdDetail report2ProdDetail = new TblMachineDailyReport2ProdDetail();
                report2ProdDetail.setProductionDetailId(prodDetail.getId());
                report2ProdDetail.setComponentId(prodDetail.getComponentId());
                report2ProdDetail.setMstComponent(prodDetail.getMstComponent());
                report2ProdDetail.setProcedureId(prodDetail.getProcedureId());
                report2ProdDetail.setMstProcedure(prodDetail.getMstProcedure());
                report2ProdDetail.setCountPerShot(prodDetail.getCountPerShot());
                report2ProdDetail.setMaterial01Id(prodDetail.getMaterial01Id());
                report2ProdDetail.setMstMaterial01(prodDetail.getMstMaterial01());
                report2ProdDetail.setMaterial01LotNo(prodDetail.getMaterial01LotNo());
                if (firstDay) { //パージ量は初日の機械日報のときだけセット
                    report2ProdDetail.setMaterial01PurgedAmount(prodDetail.getMaterial01PurgedAmount());
                }
                report2ProdDetail.setMaterial02Id(prodDetail.getMaterial02Id());
                report2ProdDetail.setMstMaterial02(prodDetail.getMstMaterial02());
                report2ProdDetail.setMaterial02LotNo(prodDetail.getMaterial02LotNo());
                if (firstDay) { //パージ量は初日の機械日報のときだけセット
                    report2ProdDetail.setMaterial02PurgedAmount(prodDetail.getMaterial02PurgedAmount());
                }
                report2ProdDetail.setMaterial03Id(prodDetail.getMaterial03Id());
                report2ProdDetail.setMstMaterial03(prodDetail.getMstMaterial03());
                report2ProdDetail.setMaterial03LotNo(prodDetail.getMaterial03LotNo());
                if (firstDay) { //パージ量は初日の機械日報のときだけセット
                    report2ProdDetail.setMaterial03PurgedAmount(prodDetail.getMaterial03PurgedAmount());
                }
                
                // 日別生産不具合データ取得
                TblProductionDefectList defectsDaily = tblProductionDefectService.getMdrProdDetailDefectList(report2ProdDetail.getProductionDetailId(), DateFormat.dateToStr(startTime, DateFormat.DATE_FORMAT));
                if (defectsDaily.isError()) {
                    report.setError(true);
                    report.setErrorCode(defectsDaily.getErrorCode());
                    report.setErrorMessage(defectsDaily.getErrorMessage());
                    return report;
                }
                report2ProdDetail.setTblProductionDefectDailyList(defectsDaily.getProductionDefectsDaily());
                if (defectsDaily.getProductionDefectsDaily() != null && !defectsDaily.getProductionDefectsDaily().isEmpty()) {
                    int totalDefectCount = 0;
                    for (TblProductionDefectDaily daily : defectsDaily.getProductionDefectsDaily()) {
                        totalDefectCount += daily.getQuantity();
                    }
                    report2ProdDetail.setDefectCount(totalDefectCount);
                }
                
                // 前ロット番号取得
                MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(prodDetail.getComponentId(), prodDetail.getMstProcedure().getProcedureCode());
                if (prevMstProcedure != null) {
                    // 部品ロット番号リスト
                    TblProductionList lotNumberList = tblProductionService.getProcuctionLotListByPrevProcedureId(prodDetail.getComponentId(), prevMstProcedure.getId(), production.getProductionDate());
                    if (lotNumberList != null) {
                        report2ProdDetail.setComponentLotNumberList(lotNumberList.getProductions());
                    }
                }
                                    
                detail.getTblMachineDailyReport2ProdDetailList().add(report2ProdDetail);
            }
            
            detailList.add(detail);
        }
        report.setMachineUuid(machineUuid);
        report.setTblMachineDailyReport2DetailList(detailList);
        //合計時間を計算
        report.calcTotal();
        //開始時刻でソート
        report.sortDatailByStartTime();
        //Json用に整形
        report.formatJson(inReportDate);
        return report;
    }
    
    public MachineDailyReport2Res collectMachineDailyReport2(String reportDate, String machineUuid) {
        MachineDailyReport2Res report = new MachineDailyReport2Res();
        if (reportDate == null || reportDate.equals("")) {
            report.setError(true);
            report.setErrorCode(ErrorMessages.E201_APPLICATION);
            report.setErrorMessage("reportDate parmeter is required.");
            return report;
        }
        if (machineUuid == null || machineUuid.equals("")) {
            report.setError(true);
            report.setErrorCode(ErrorMessages.E201_APPLICATION);
            report.setErrorMessage("machineUuid parmeter required.");
            return report;
        }
        //業務開始時刻、終了時刻の取得
        CnfSystem cnfSystem =  cnfSystemService.findByKey("system", "business_start_time");
        if (cnfSystem == null) {
            report.setError(true);
            report.setErrorCode(ErrorMessages.E201_APPLICATION);
            report.setErrorMessage("Business start time is not defined in system setting.");
            return report;
        }
        String businessStartTime = cnfSystem.getConfigValue();
        if (businessStartTime.length() == 4) {
            businessStartTime = "0" + businessStartTime;
        }
        java.util.Date startTime = DateFormat.strToDatetime(reportDate + " " + businessStartTime + ":00");
        java.util.Date endTime = DateFormat.getAfterDay(startTime);
        java.util.Date inReportDate = DateFormat.strToDate(reportDate);
        return this.collectMachineDailyReport2Core(machineUuid, startTime, endTime, inReportDate);
    }
    
    /**
     * 作業IDから機械日報2のリストを取得
     * @param workId
     * @return 
     */
    private List<TblMachineDailyReport2> getMachineDailyReport2ListByWorkId(String workId) {
        String sql = " SELECT t FROM TblMachineDailyReport2 t JOIN FETCH t.tblMachineDailyReport2DetailList dtl WHERE dtl.workId = :workId ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("workId", workId);
        List<TblMachineDailyReport2> list = query.getResultList();
        return list;
    }

    /**
     * 生産IDから機械日報2のリストを取得
     * @param productionId
     * @return 
     */
    private List<TblMachineDailyReport2> getMachineDailyReport2ListByProductionId(String productionId) {
        String sql = " SELECT t FROM TblMachineDailyReport2 t JOIN FETCH t.tblMachineDailyReport2DetailList dtl WHERE dtl.productionId = :productionId ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("productionId", productionId);
        List<TblMachineDailyReport2> list = query.getResultList();
        return list;
    }
    
    private void deleteDetail(String id) {
        Query query = entityManager.createQuery("DELETE FROM TblMachineDailyReport2Detail t WHERE t.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    /**
     * 指定された作業IDに関連する機械日報の削除。作業テーブル削除時に呼ばれる
     * @param workId 
     */
    public void deleteWork(String workId) {
        List<TblMachineDailyReport2> list = getMachineDailyReport2ListByWorkId(workId);
        if (list == null || list.isEmpty()) return;
        //ヘダーテーブルから合計時間を減らす
        for (TblMachineDailyReport2 report : list) {
            if (report.getTblMachineDailyReport2DetailList() == null) continue;
            for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                report.setTotalTimeMinutes(report.getTotalTimeMinutes() - detail.getDurationMinitues());
                report.setTotalDowntimeMinutes(report.getTotalDowntimeMinutes() - detail.getDurationMinitues());
                report.setTotalPlannedDowntimeMinutes(report.getTotalPlannedDowntimeMinutes() - detail.getDurationMinitues());
                //明細の削除
                deleteDetail(detail.getId());
            }
            //ヘダーの更新
            updateDailyReport2(report);
        }
    }
    
    /**
     * 指定された生産IDに関連する機械日報の削除。生産テーブル削除時に呼ばれる
     * @param productionId 
     */
    public boolean deleteProduction(String productionId, LoginUser loginUser) {
        List<TblMachineDailyReport2> list = getMachineDailyReport2ListByProductionId(productionId);
        if (list == null || list.isEmpty()) return false;
        periodProductionUpdater.setLoginUser(loginUser);
        periodProductionUpdater.prepare();
        stockUpdater.setLoginUser(loginUser);
        //ヘダーテーブルから合計時間,合計ショット数,合計生産数,合計不良数を減らす
        for (TblMachineDailyReport2 report : list) {
            if (report.getTblMachineDailyReport2DetailList() == null) continue;
            for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                //合計時間、合計ショット数は明細から減らす
                report.setTotalTimeMinutes(report.getTotalTimeMinutes() - detail.getDurationMinitues()); //合計時間
                report.setTotalOperatingMinutes(report.getTotalOperatingMinutes() - detail.getDurationMinitues()); //合計稼働時間
                report.setTotalShotCount(report.getTotalShotCount() - detail.getDisposedShotCount() - detail.getShotCount());
                if (detail.getMoldUuid() != null) {
                    //金型マスタの累計も減らす
                    totalUpdater.addMoldCounts(detail.getMoldUuid(), (detail.getShotCount() + detail.getDisposedShotCount()) * (-1), detail.getDurationMinitues() * (-1));
                }
                //設備マスタの累計も減らす
                if (report.getMachineUuid() != null) {
                    totalUpdater.addMachineCounts(report.getMachineUuid(), (detail.getShotCount() + detail.getDisposedShotCount()) * (-1), detail.getDurationMinitues() * (-1));
                }
                //生産数、不良数は生産明細から減らす
                if (detail.getTblMachineDailyReport2ProdDetailList() != null) {
                    for (TblMachineDailyReport2ProdDetail prod : detail.getTblMachineDailyReport2ProdDetailList()) {
                        report.setTotalCompleteCount(report.getTotalCompleteCount() - prod.getCompleteCount());
                        report.setTotalDefectCount(report.getTotalDefectCount() - prod.getDefectCount());
                        report.setTotalDefectCountType01(report.getTotalDefectCountType01() - prod.getDefectCountType01());
                        report.setTotalDefectCountType02(report.getTotalDefectCountType02() - prod.getDefectCountType02());
                        report.setTotalDefectCountType03(report.getTotalDefectCountType03() - prod.getDefectCountType03());
                        report.setTotalDefectCountType04(report.getTotalDefectCountType04() - prod.getDefectCountType04());
                        report.setTotalDefectCountType05(report.getTotalDefectCountType05() - prod.getDefectCountType05());
                        report.setTotalDefectCountType06(report.getTotalDefectCountType06() - prod.getDefectCountType06());
                        report.setTotalDefectCountType07(report.getTotalDefectCountType07() - prod.getDefectCountType07());
                        report.setTotalDefectCountType08(report.getTotalDefectCountType08() - prod.getDefectCountType08());
                        report.setTotalDefectCountType09(report.getTotalDefectCountType09() - prod.getDefectCountType09());
                        report.setTotalDefectCountType10(report.getTotalDefectCountType10() - prod.getDefectCountType10());
                        report.setTotalDefectCountOther(report.getTotalDefectCountOther() - prod.getDefectCountOther());
                        //金型期間別生産数を減らす
                        if (detail.getMoldUuid() != null) {
                            periodProductionUpdater.addMoldProductionPeriod(detail.getMoldUuid(), prod.getComponentId(), report.getReportDate(), prod.getCompleteCount() * (-1));
                            periodProductionUpdater.addMachineProductionPeriod(report.getMachineUuid(), detail.getMoldUuid(), prod.getComponentId(), report.getReportDate(), prod.getCompleteCount() * (-1));
                        }
                        //生産終了していないときは機械日報2で計上した在庫を減らす(生産終了済みのときは生産側(TblProductionResource)で減らされる)
                        if (detail.getProductionEndFlg() == 0) {
                            //部品在庫
                            stockUpdater.addComponentStock(
                                detail.getProductionId(), prod.getProductionDetailId(), prod.getComponentId(), prod.getProcedureId(), prod.getCompleteCount(), null);
                            //材料在庫更新
                            if (prod.getMaterial01Id() != null) {
                                stockUpdater.addMaterialStock(prod.getMaterial01Id(), detail.getProductionId(), prod.getProductionDetailId(),
                                    prod.getMaterial01LotNo(), prod.getMaterial01Amount().add(prod.getMaterial01PurgedAmount()));
                            }
                            if (prod.getMaterial02Id() != null) {
                                stockUpdater.addMaterialStock(prod.getMaterial02Id(), detail.getProductionId(), prod.getProductionDetailId(),
                                    prod.getMaterial02LotNo(), prod.getMaterial02Amount().add(prod.getMaterial02PurgedAmount()));
                                }
                            if (prod.getMaterial03Id() != null) {
                                stockUpdater.addMaterialStock(prod.getMaterial03Id(), detail.getProductionId(), prod.getProductionDetailId(),
                                    prod.getMaterial03LotNo(), prod.getMaterial03Amount().add(prod.getMaterial03PurgedAmount()));
                            }
                        }
                    }
                }
                //明細の削除
                deleteDetail(detail.getId());
            }
            //ヘダーの更新
            report.setUpdateUserUuid(loginUser.getUserUuid());
            report.setUpdateDate(new java.util.Date());
            updateDailyReport2(report);
        }
        //設備マスタ、金型マスタの更新
        totalUpdater.update(null);
        //期間別生産数の更新
        periodProductionUpdater.updateDB();
        return true;
    }
    
//    public Production getProduction(String id) {
//        String sql = " SELECT t FROM Production t WHERE t.id = :id";
//        Query query = entityManager.createQuery(sql.toString());
//        query.setParameter("id", id);
//        Production prod = (Production) query.getSingleResult();
//        prod.sortDetailByComponentCode();
//        return prod;
//    }
    @Inject
    private TblMoldProductionPeriodService moldProductionPeriodService;
    
    @Transactional
    public TblMoldProductionForDay getTblMoldProductionForDay(String moldUuid, String componentId, java.util.Date date) {
        return moldProductionPeriodService.getProductionForDaySingleByPK(moldUuid, componentId, date);
    }
    
    public MstComponentStructureVoList getComponentStructures(String componentId, String procedureId) {
        return stockUpdater.getComponentStructures(componentId, procedureId);
    }
    
    /**
     * 指定された設備の指定された日付より前に生産情報の未登録がないか警告する
     * @param reportDate
     * @param machineUuid
     * @return 
     */
    public String checkPreviousDays(String reportDate, String machineUuid) {
        if (machineUuid == null || machineUuid.equals("")) {
            return "machineUuid is required.";
        }
        java.util.Date paramReportDate = null;
        if (reportDate != null && !reportDate.equals("")) {
            paramReportDate = DateFormat.strToDate(reportDate);
        }
        else {
            return "reportDate is required.";
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Map<String, Object> response = new HashMap<>();
        boolean warning = false;
        java.util.Date date = null;
        //collectメソッドで指定日に登録される予定の生産実績を探す
        MachineDailyReport2Res plannedReport = collectMachineDailyReport2(reportDate, machineUuid);
        for (TblMachineDailyReport2Detail detail : plannedReport.getTblMachineDailyReport2DetailList()) {
            if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD && detail.getProductionId() != null) {
                date = findDateProductionUnregistered(detail.getProductionId(), paramReportDate);
            }
            if (date != null) {
                warning = true;
                break;
            }
        }
        if (!warning) {
            //すでに指定日に日報登録済みの生産実績を探す
            StringBuilder sbSql = new StringBuilder();
            sbSql.append("SELECT t FROM TblMachineDailyReport2 t ");
            sbSql.append("WHERE t.machineUuid = :machineUuid ");
            sbSql.append(" AND t.reportDate = :reportDate ");
            Query query = entityManager.createQuery(sbSql.toString());
            query.setParameter("reportDate", paramReportDate);
            query.setParameter("machineUuid", machineUuid);
            List list = query.getResultList();
            TblMachineDailyReport2 report = null;
            if (list.size() > 0) {
                report = (TblMachineDailyReport2)list.get(0);
                for (TblMachineDailyReport2Detail detail : report.getTblMachineDailyReport2DetailList()) {
                    if (detail.getDetailType() == TblMachineDailyReport2.DETAIL_TYPE_PROD && detail.getProductionId() != null) {
                        date = findDateProductionUnregistered(detail.getProductionId(), paramReportDate);
                    }
                    if (date != null) {
                        warning = true;
                        break;
                    }
                }
            }
        }
        response.put("warning", warning);
        response.put("date", date);
        return gson.toJson(response);
    }
    
    /**
     * 基準日より前で生産実績が日報として登録されていない日付を探す
     * @param productionId
     * @param baseDate
     * @return 
     */
    private java.util.Date findDateProductionUnregistered(String productionId, java.util.Date baseDate) {
        Production production = getProductionById(productionId);
        //生産開始日時の属す営業日を取得
        String startDatetime = DateFormat.dateToStr(production.getStartDatetime(), DateFormat.DATETIME_FORMAT);
        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
        String startDate = DateFormat.getBusinessDate(startDatetime, cnfSystem);
        java.util.Date paramStartDate = DateFormat.strToDate(startDate);
        //生産開始日が基準日なら日報が登録されていなくてもかまわないので判定終了
        if (paramStartDate.compareTo(baseDate) == 0) {
            return null;
        }
        //生産開始日以降と基準日より前で機械日報が登録されている日付の最小値を取得
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT t FROM TblMachineDailyReport2 t ");
        jpql.append(" WHERE EXISTS (SELECT dtl FROM TblMachineDailyReport2Detail dtl WHERE dtl.reportId = t.id AND dtl.productionId = :productionId) "); //該当生産実績IDの機会日報が未作成
        jpql.append(" AND t.reportDate >= :startDate "); //生産開始日時の属す営業日以降
        jpql.append(" AND t.reportDate < :baseDate "); //基準日より前
        jpql.append(" ORDER BY t.reportDate ASC "); //昇順に並べて最小の日付をとる
        Query query = entityManager.createQuery(jpql.toString());
        query.setParameter("productionId", productionId);
        query.setParameter("startDate", paramStartDate);
        query.setParameter("baseDate", baseDate);
        List<TblMachineDailyReport2> list = query.getResultList();
        if (list.size() > 0) {
            //日報登録がされていれば登録のもれている日付がないかチェックする
            java.util.Date tmpDate = paramStartDate;
            while (tmpDate.compareTo(baseDate) < 0) { //生産開始日の属す営業日から順番に基準日より小さい日付をすべてチェックする
                boolean found = false;
                for (TblMachineDailyReport2 report : list) {
                    if (tmpDate.compareTo(report.getReportDate()) == 0) {
                        found = true;
                        break;
                    }
                }
                //日報が見つからなければその日を返す
                if (!found) {
                    return tmpDate;
                }
                //日報が見つかれば翌日をチェック
                tmpDate = DateFormat.getAfterDay(tmpDate);
            }
        }
        else {
            //日報が登録が１件もされていなければ、生産開始日時の属す営業日。
            return paramStartDate;
        }
        return null;
    }
    
    private void setMaterialInfo(TblMachineDailyReport2ProdDetail prodDetail) {

        if (prodDetail.getMaterial01Id() != null) {
            prodDetail.setMaterial01Amount(prodDetail.getMaterial01Amount() == null ? BigDecimal.ZERO : prodDetail.getMaterial01Amount());
            prodDetail.setMaterial01PurgedAmount(prodDetail.getMaterial01PurgedAmount() == null ? BigDecimal.ZERO : prodDetail.getMaterial01PurgedAmount());
        }
        if (prodDetail.getMaterial02Id() != null) {
            prodDetail.setMaterial02Amount(prodDetail.getMaterial02Amount() == null ? BigDecimal.ZERO : prodDetail.getMaterial02Amount());
            prodDetail.setMaterial02PurgedAmount(prodDetail.getMaterial02PurgedAmount() == null ? BigDecimal.ZERO : prodDetail.getMaterial02PurgedAmount());
        }
        if (prodDetail.getMaterial03Id() != null) {
            prodDetail.setMaterial03Amount(prodDetail.getMaterial03Amount() == null ? BigDecimal.ZERO : prodDetail.getMaterial03Amount());
            prodDetail.setMaterial03PurgedAmount(prodDetail.getMaterial03PurgedAmount() == null ? BigDecimal.ZERO : prodDetail.getMaterial03PurgedAmount());
        }

    }
    
    public void deleteDetailDownTime(String id) {
        deleteDetail(id);
    }
    
}
