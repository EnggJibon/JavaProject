/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.issue;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVo;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVoList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import com.kmcj.karte.util.TimezoneConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
 * @author jiangxs
 */
@Dependent
public class TblIssueService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstChoiceService mstChoiceService;
    
    // 打上場所
    public static final String USER_DEPARTMENT = "mst_user.department";
    // 打上工程
    public static final String ISSUE_REPORT_PHASE = "tbl_issue.report_phase";
    // 打上大分類
    public static final String ISSUE_REPORT_CATEGORY1 = "tbl_issue.report_category1";
    // 打上中分類
    public static final String ISSUE_REPORT_CATEGORY2 = "tbl_issue.report_category2";
    // 打上小分類
    public static final String ISSUE_REPORT_CATEGORY3 = "tbl_issue.report_category3";

    // 打上用選択肢
    public static final String[] ISSUE_ARRAY = { USER_DEPARTMENT, ISSUE_REPORT_PHASE, ISSUE_REPORT_CATEGORY1,
            ISSUE_REPORT_CATEGORY2, ISSUE_REPORT_CATEGORY3 };

    private Map<String, String> outMeasureStatus;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("reportDate", " ORDER BY issue.reportDate ");// 打上日
        orderKey.put("measureDueDate", " ORDER BY issue.measureDueDate ");// 対策期限
        orderKey.put("measureStatus", " ORDER BY issue.measureStatus ");// 対策ステータス
        orderKey.put("machineId", " ORDER BY machine.machineId ");// 設備ＩＤ
        orderKey.put("machineName", " ORDER BY machine.machineName ");// 設備名称
        orderKey.put("moldId", " ORDER BY mold.moldId ");// 金型ＩＤ
        orderKey.put("moldName", " ORDER BY mold.moldName ");// 金型名称
        orderKey.put("componentCode", " ORDER BY mc.componentCode ");// 部品コード
        orderKey.put("componentName", " ORDER BY mc.componentName ");// 部品名称
        orderKey.put("quantity", " ORDER BY issue.quantity ");// 数量
        orderKey.put("shotCountAtIssue", " ORDER BY issue.shotCountAtIssue ");// 打上時ショット数
        orderKey.put("issueReportPersonName", " ORDER BY mu.userName ");// 報告者
        orderKey.put("reportDepartmentName", " ORDER BY issue.reportDepartment ");// 打上場所
        orderKey.put("reportPhaseText", " ORDER BY issue.reportPhase ");// 打上工程
        orderKey.put("reportCategory1Text", " ORDER BY issue.reportCategory1 ");// 打上大分類
        orderKey.put("reportCategory2Text", " ORDER BY issue.reportCategory2 ");// 打上中分類
        orderKey.put("reportCategory3Text", " ORDER BY issue.reportCategory3 ");// 打上小分類
        orderKey.put("memo01", " ORDER BY issue.memo01 ");
        orderKey.put("memo02", " ORDER BY issue.memo02 ");
        orderKey.put("memo03", " ORDER BY issue.memo03 ");
        orderKey.put("issue", " ORDER BY issue.issue ");// 打上事象
        orderKey.put("measureSummary", " ORDER BY issue.measureSummary ");// 対策内容
        orderKey.put("measuerCompletedDate", " ORDER BY issue.measuerCompletedDate ");// 対策完了日
        orderKey.put("mainteDate", " ORDER BY moldmt.mainteDate ");// 金型メンテナンス日
        orderKey.put("machineMainteDate", " ORDER BY machinemt.mainteDate ");// 設備メンテナンス日

    }

    /**
     *
     * @param langId
     */
    public void outMoldTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outMeasureStatus == null) {
            outMeasureStatus = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "tbl_issue.measure_status");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                outMeasureStatus.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
        }
    }

    /**
     * 異常一覧 異常情報件数取得
     *
     * @param measureStatus
     * @param department
     * @param reportDate
     * @param reportDateForm
     * @param reportDateTo
     * @param measureDueDate
     * @param moldId
     * @param measureDueDateForm
     * @param measureDueDateTo
     * @param moldName
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param mainTenanceId
     * @param measureStatusOperand
     * @param reportPersonName
     * @return
     */
    public CountResponse getIssueCount(
            int noMoldMainte,
            int noMachineMainte,
            int measureStatus,int measureStatusOperand, String department, String reportDate,
            String reportDateForm, String reportDateTo,
            String measureDueDate,
            String measureDueDateForm, String measureDueDateTo, String moldId, String moldName,
            String componentCode, String componentName, String machineId, String machineName,
            String mainTenanceId,
            String reportPersonName // KM-359 打上一覧に報告者追加
            ,boolean orderByDueDate
    ) {
        List list = getIssueCountSql(noMoldMainte,noMachineMainte,measureStatus,measureStatusOperand, department, reportDate, reportDateForm, reportDateTo, measureDueDate, measureDueDateForm, measureDueDateTo,
                moldId, moldName, componentCode, componentName, machineId, machineName, mainTenanceId, reportPersonName, "count", orderByDueDate);
        CountResponse count = new CountResponse();
        if (list.size() > 0) {
            count.setCount((long) (list.get(0)));
        } else {
            count.setCount(0);
        }
        return count;
    }

    /**
     * 異常一覧,通常打上 異常情報複数取得
     *
     * @param noMoldMainte
     * @param   noMachineMainte
     * @param measureStatus
     * @param measureStatusOperand     // Added parameter "measureStatusOperand"  for Issue: KM-743
     * @param department
     * @param reportDate
     * @param reportDateFrom
     * @param reportDateTo
     * @param measureDueDate
     * @param moldId
     * @param moldName
     * @param measureDueDateFrom
     * @param measureDueDateFromTo
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param mainTenanceId
     * @param reportPersonName
     * @param orderByDueDate
     * @param reportPhase
     * @param reportCategory1
     * @param reportCategory2
     * @param reportCategory3
     * @param memo01
     * @param memo02
     * @param memo03
     * @param happenedAt
     * @return
     */
    public List<TblIssue> getIssues(int noMoldMainte, int noMachineMainte, int measureStatus,int measureStatusOperand , String department, String reportDate, String reportDateFrom, String reportDateTo, String measureDueDate,
            String measureDueDateFrom, String measureDueDateFromTo, String moldId, String moldName, String componentCode,
            String componentName, String machineId, String machineName, String mainTenanceId,
            String reportPersonName// KM-359 打上一覧に報告者追加
            ,boolean orderByDueDate, int reportPhase, int reportCategory1, int reportCategory2, int reportCategory3, String memo01, String memo02, String memo03, String happenedAt
    ) {
        List<TblIssue> list = getIssueSql(
                noMoldMainte, noMachineMainte, measureStatus, measureStatusOperand, department, reportDate, reportDateFrom, reportDateTo, measureDueDate, measureDueDateFrom,
                measureDueDateFromTo, moldId, moldName, componentCode, componentName, machineId, machineName, mainTenanceId, reportPersonName, "", orderByDueDate, reportPhase,
                reportCategory1, reportCategory2, reportCategory3, memo01, memo02, memo03, happenedAt);
        return list;
    }

    /**
     * sql文の用
     *
     * @param measureStatus
     * @param measureStatusOperand     // Added parameter "measureStatusOperand"  for Issue: KM-598
     * @param department
     * @param reportDate
     * @param reportDateFrom
     * @param reportDateTo
     * @param measureDueDate
     * @param measureDueDateForm
     * @param measureDueDateTo
     * @param moldId
     * @param moldName
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param mainTenanceId
     * @param reportPersonName
     * @param action
     * @param happenedAt
     * @return
     */
        // Added parameter "measureStatusOperand"  for Issue: KM-743
    private List getIssueSql(int noMoldMainte , int noMachineMainte, int measureStatus,int measureStatusOperand, String department, String reportDate, String reportDateFrom, String reportDateTo, String measureDueDate,String measureDueDateForm, String measureDueDateTo,
            String moldId, String moldName, String componentCode, String componentName, String machineId, String machineName, String mainTenanceId,
            String reportPersonName, // KM-359 打上一覧に報告者追加
            String action, boolean orderByDueDate, int reportPhase, int reportCategory1, int reportCategory2, int reportCategory3, String memo01, String memo02, String memo03, String happenedAt) {
        StringBuilder sql = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        if ("count".equals(action)) {
            sql.append("SELECT count(issue.id) FROM TblIssue issue WHERE 1=1 ");
        } else {
            sql.append("SELECT issue FROM TblIssue issue "
                    + " LEFT JOIN FETCH issue.mstMold"
                    + " LEFT JOIN FETCH issue.mstComponent "
                    + " LEFT JOIN FETCH issue.tblMoldMaintenanceRemodeling"
                    + " LEFT JOIN FETCH issue.mstMachine"
                    + " LEFT JOIN FETCH issue.tblMachineMaintenanceRemodeling"
                    + " LEFT JOIN FETCH issue.mstUser"
                    + " WHERE 1=1 ");
        }
        if (measureStatus != -1) {
            if (measureStatusOperand !=1)
            {
                sql.append(" And issue.measureStatus = :measureStatus ");  
            }
            else{
                sql.append(" And issue.measureStatus != :measureStatus ");
            }
        }

        if (noMoldMainte == 1) { //メンテナンス未実施の不具合
            sql.append(" And issue.mainTenanceId IS NULL ");
        }
        if (noMachineMainte == 1) { //設備メンテナンス未実施の不具合
            sql.append(" AND issue.machineMainTenanceId IS NULL ");
        }

        if (department != null && !"0".equals(department)) {
            sql.append(" And issue.reportDepartment = :reportDepartment ");
        }

        if (reportDate != null && !"".equals(reportDate)) {
            sql.append(" And issue.reportDate = :reportDate ");
        }
        if (reportDateFrom != null && !"".equals(reportDateFrom)) {
            sql.append(" And issue.reportDate >= :reportDateFrom  ");
        }

        if (reportDateTo != null && !"".equals(reportDateTo)) {
            sql.append(" And issue.reportDate <= :reportDateTo ");
        }

        if (measureDueDate != null && !"".equals(measureDueDate)) {
            sql.append(" And issue.measureDueDate = :measureDueDate ");
        }

        if (measureDueDateForm != null && !"".equals(measureDueDateForm)) {
            sql.append(" And issue.measureDueDate >= :measureDueDateForm ");
        }

        if (measureDueDateTo != null && !"".equals(measureDueDateTo)) {
            sql.append(" And issue.measureDueDate <= :measureDueDateTo ");
        }

        if (moldId != null && !"".equals(moldId)) {
            sql.append(" And issue.mstMold.moldId LIKE :moldId ");
        }
        if (moldName != null && !"".equals(moldName)) {
            sql.append(" And issue.mstMold.moldName like :moldName ");
        }
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" And issue.mstComponent.componentCode LIKE :componentCode ");
        }
        if (componentName != null && !"".equals(componentName)) {
            sql.append(" And issue.mstComponent.componentName like :componentName ");
        }
        if (machineId != null && !"".equals(machineId)) {
            sql.append(" And issue.mstMachine.machineId LIKE :machineId ");
        }
        if (machineName != null && !"".equals(machineName)) {
            sql.append(" And issue.mstMachine.machineName like :machineName ");
        }
        if (mainTenanceId != null && !"".equals(mainTenanceId)) {
            //-1 means mainTenanceId is null
            sql.append(mainTenanceId.equals("-1") ? "And issue.mainTenanceId is null " : " And issue.mainTenanceId = :mainTenanceId ");
        }

        // KM-359 打上一覧に報告者追加
        if (StringUtils.isNotEmpty(reportPersonName)) {
            sql.append(" AND issue.mstUser.userName LIKE :reportPersonName ");
        }
        
        if (reportPhase != 0) {
	sql.append(" And issue.reportPhase = :reportPhase ");
        }
        if (reportCategory1 != 0) {
                sql.append(" And issue.reportCategory1 = :reportCategory1 ");
        }
        if (reportCategory2 != 0) {
                sql.append(" And issue.reportCategory2 = :reportCategory2 ");
        }
        if (reportCategory3 != 0) {
                sql.append(" And issue.reportCategory3 = :reportCategory3 ");
        }
        
        if (memo01 != null && !"".equals(memo01)) {
            sql.append(" And issue.memo01 like :memo01 ");
        }
        
        if (memo02 != null && !"".equals(memo02)) {
            sql.append(" And issue.memo02 like :memo02 ");
        }
        
        if (memo03 != null && !"".equals(memo03)) {
            sql.append(" And issue.memo03 like :memo03 ");
        }
        
        if (orderByDueDate) {     
            sql.append(" Order By issue.measureDueDate "); 
        }
        else {
            sql.append(" Order By issue.reportDate desc, issue.happenedAt desc ");
        }
        
        Query query = entityManager.createQuery(sql.toString());

        
        if (measureStatus != -1) {
            query.setParameter("measureStatus", measureStatus);  
        }

        if (department != null && !"0".equals(department)) {
            try {
                query.setParameter("reportDepartment", Integer.parseInt(department));
            } catch (NumberFormatException e) {
                query.setParameter("reportDepartment", 0);
            }
        }

        try {
            if (reportDate != null && !"".equals(reportDate)) {
                query.setParameter("reportDate", sdf.parse(reportDate));
            }
            if (measureDueDate != null && !"".equals(measureDueDate)) {
                query.setParameter("measureDueDate", sdf.parse(measureDueDate));
            }
            if (moldId != null && !"".equals(moldId)) {
                query.setParameter("moldId", "%" + moldId + "%");
            }
            if (moldName != null && !"".equals(moldName)) {
                query.setParameter("moldName", "%" + moldName + "%");
            }
            if (componentCode != null && !"".equals(componentCode)) {
                query.setParameter("componentCode", "%" + componentCode + "%");
            }
            if (componentName != null && !"".equals(componentName)) {
                query.setParameter("componentName", "%" + componentName + "%");
            }
            if (machineId != null && !"".equals(machineId)) {
                query.setParameter("machineId", "%" + machineId + "%");
            }
            if (machineName != null && !"".equals(machineName)) {
                query.setParameter("machineName", "%" + machineName + "%");
            }
            if (mainTenanceId != null && !"".equals(mainTenanceId) && !"-1".equals(mainTenanceId)) {
                query.setParameter("mainTenanceId", mainTenanceId);
            }

            if (reportDateFrom != null && !"".equals(reportDateFrom)) {
                query.setParameter("reportDateFrom", sdf.parse(reportDateFrom));
            }

            if (reportDateTo != null && !"".equals(reportDateTo)) {
                query.setParameter("reportDateTo", sdf.parse(reportDateTo));
            }

            if (measureDueDateForm != null && !"".equals(measureDueDateForm)) {
                query.setParameter("measureDueDateForm", sdf.parse(measureDueDateForm));
            }

            if (measureDueDateTo != null && !"".equals(measureDueDateTo)) {
                query.setParameter("measureDueDateTo", sdf.parse(measureDueDateTo));
            }

            // KM-359 打上一覧に報告者追加
            if (StringUtils.isNotEmpty(reportPersonName)) {
                query.setParameter("reportPersonName", "%" + reportPersonName + "%");
            }
            
            if (reportPhase != 0) {
                query.setParameter("reportPhase", reportPhase);
            }
            if (reportCategory1 != 0) {
                query.setParameter("reportCategory1", reportCategory1);
            }
            if (reportCategory2 != 0) {
                query.setParameter("reportCategory2", reportCategory2);
            }
            if (reportCategory3 != 0) {
                query.setParameter("reportCategory3", reportCategory3);
            }
            if (memo01 != null && !"".equals(memo01)) {
                query.setParameter("memo01", "%" + memo01 + "%");
            }
            if (memo02 != null && !"".equals(memo02)) {
                query.setParameter("memo02", "%" + memo02 + "%");
            }
            if (memo03 != null && !"".equals(memo03)) {
                query.setParameter("memo03", "%" + memo03 + "%");
            }

        } catch (ParseException ex) {
            Logger.getLogger(TblIssueService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query.getResultList();
    }

    private List getIssueCountSql(int noMoldMainte , int noMachineMainte, int measureStatus,int measureStatusOperand, String department, String reportDate, String reportDateFrom, String reportDateTo, String measureDueDate,String measureDueDateForm, String measureDueDateTo,
            String moldId, String moldName, String componentCode, String componentName, String machineId, String machineName, String mainTenanceId,
            String reportPersonName, // KM-359 打上一覧に報告者追加
            String action, boolean orderByDueDate) {
        StringBuilder sql = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        if ("count".equals(action)) {
            sql.append("SELECT count(issue.id) FROM TblIssue issue WHERE 1=1 ");
        } else {
            sql.append("SELECT issue FROM TblIssue issue "
                    + " LEFT JOIN FETCH issue.mstMold"
                    + " LEFT JOIN FETCH issue.mstComponent "
                    + " LEFT JOIN FETCH issue.tblMoldMaintenanceRemodeling"
                    + " LEFT JOIN FETCH issue.mstMachine"
                    + " LEFT JOIN FETCH issue.tblMachineMaintenanceRemodeling"
                    + " LEFT JOIN FETCH issue.mstUser"
                    + " WHERE 1=1 ");
        }
        if (measureStatus != -1) {
            if (measureStatusOperand !=1)
            {
                sql.append(" And issue.measureStatus = :measureStatus ");  
            }
            else{
                sql.append(" And issue.measureStatus != :measureStatus ");
            }
        }

        if (noMoldMainte == 1) { //メンテナンス未実施の不具合
            sql.append(" And issue.mainTenanceId IS NULL ");
        }
        if (noMachineMainte == 1) { //設備メンテナンス未実施の不具合
            sql.append(" AND issue.machineMainTenanceId IS NULL ");
        }

        if (department != null && !"0".equals(department)) {
            sql.append(" And issue.reportDepartment = :reportDepartment ");
        }

        if (reportDate != null && !"".equals(reportDate)) {
            sql.append(" And issue.reportDate = :reportDate ");
        }
        if (reportDateFrom != null && !"".equals(reportDateFrom)) {
            sql.append(" And issue.reportDate >= :reportDateFrom  ");
        }

        if (reportDateTo != null && !"".equals(reportDateTo)) {
            sql.append(" And issue.reportDate <= :reportDateTo ");
        }

        if (measureDueDate != null && !"".equals(measureDueDate)) {
            sql.append(" And issue.measureDueDate = :measureDueDate ");
        }

        if (measureDueDateForm != null && !"".equals(measureDueDateForm)) {
            sql.append(" And issue.measureDueDate >= :measureDueDateForm ");
        }

        if (measureDueDateTo != null && !"".equals(measureDueDateTo)) {
            sql.append(" And issue.measureDueDate <= :measureDueDateTo ");
        }

        if (moldId != null && !"".equals(moldId)) {
            sql.append(" And issue.mstMold.moldId LIKE :moldId ");
        }
        if (moldName != null && !"".equals(moldName)) {
            sql.append(" And issue.mstMold.moldName like :moldName ");
        }
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" And issue.mstComponent.componentCode LIKE :componentCode ");
        }
        if (componentName != null && !"".equals(componentName)) {
            sql.append(" And issue.mstComponent.componentName like :componentName ");
        }
        if (machineId != null && !"".equals(machineId)) {
            sql.append(" And issue.mstMachine.machineId LIKE :machineId ");
        }
        if (machineName != null && !"".equals(machineName)) {
            sql.append(" And issue.mstMachine.machineName like :machineName ");
        }
        if (mainTenanceId != null && !"".equals(mainTenanceId)) {
            //-1 means mainTenanceId is null
            sql.append(mainTenanceId.equals("-1") ? "And issue.mainTenanceId is null " : " And issue.mainTenanceId = :mainTenanceId ");
        }

        // KM-359 打上一覧に報告者追加
        if (StringUtils.isNotEmpty(reportPersonName)) {
            sql.append(" AND issue.mstUser.userName LIKE :reportPersonName ");
        }
        
        if (orderByDueDate) {     
            sql.append(" Order By issue.measureDueDate "); 
        }
        else {
            sql.append(" Order By issue.reportDate desc, issue.happenedAt desc ");
        }
        
        Query query = entityManager.createQuery(sql.toString());

        
        if (measureStatus != -1) {
            query.setParameter("measureStatus", measureStatus);  
        }

        if (department != null && !"0".equals(department)) {
            try {
                query.setParameter("reportDepartment", Integer.parseInt(department));
            } catch (NumberFormatException e) {
                query.setParameter("reportDepartment", 0);
            }
        }

        try {
            if (reportDate != null && !"".equals(reportDate)) {
                query.setParameter("reportDate", sdf.parse(reportDate));
            }
            if (measureDueDate != null && !"".equals(measureDueDate)) {
                query.setParameter("measureDueDate", sdf.parse(measureDueDate));
            }
            if (moldId != null && !"".equals(moldId)) {
                query.setParameter("moldId", "%" + moldId + "%");
            }
            if (moldName != null && !"".equals(moldName)) {
                query.setParameter("moldName", "%" + moldName + "%");
            }
            if (componentCode != null && !"".equals(componentCode)) {
                query.setParameter("componentCode", "%" + componentCode + "%");
            }
            if (componentName != null && !"".equals(componentName)) {
                query.setParameter("componentName", "%" + componentName + "%");
            }
            if (machineId != null && !"".equals(machineId)) {
                query.setParameter("machineId", "%" + machineId + "%");
            }
            if (machineName != null && !"".equals(machineName)) {
                query.setParameter("machineName", "%" + machineName + "%");
            }
            if (mainTenanceId != null && !"".equals(mainTenanceId) && !"-1".equals(mainTenanceId)) {
                query.setParameter("mainTenanceId", mainTenanceId);
            }

            if (reportDateFrom != null && !"".equals(reportDateFrom)) {
                query.setParameter("reportDateFrom", sdf.parse(reportDateFrom));
            }

            if (reportDateTo != null && !"".equals(reportDateTo)) {
                query.setParameter("reportDateTo", sdf.parse(reportDateTo));
            }

            if (measureDueDateForm != null && !"".equals(measureDueDateForm)) {
                query.setParameter("measureDueDateForm", sdf.parse(measureDueDateForm));
            }

            if (measureDueDateTo != null && !"".equals(measureDueDateTo)) {
                query.setParameter("measureDueDateTo", sdf.parse(measureDueDateTo));
            }

            // KM-359 打上一覧に報告者追加
            if (StringUtils.isNotEmpty(reportPersonName)) {
                query.setParameter("reportPersonName", "%" + reportPersonName + "%");
            }

        } catch (ParseException ex) {
            Logger.getLogger(TblIssueService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return query.getResultList();
    }
    
    /**
     * 通常打上 異常IDを保持して、通常打上画面に編集モードで詳細情報を画面に表示する
     *
     * @param id
     * @return
     */
    public List<TblIssue> getIssueById(String id) {
        Query query = entityManager.createNamedQuery("TblIssue.findById");
        query.setParameter("id", id);
        List<TblIssue> tblIssueList = query.getResultList();
        return tblIssueList;
    }

    /**
     * 通常打上 異常情報を登録
     *
     * @param tblIssueVo
     * @param loginUser
     * @return
     */
    @Transactional
    public TblIssueVo createIssue(TblIssueVo tblIssueVo, LoginUser loginUser) {

        TblIssueImageFile tblIssueImageFile;
        TblIssueImageFilePK tblIssueImageFilePK;
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
        // 金型外部であれば、編集不可
        if (tblIssueVo.getMoldId() != null && !"".equals(tblIssueVo.getMoldId())) {
            BasicResponse response = FileUtil.checkExternal(entityManager, mstDictionaryService, tblIssueVo.getMoldId(), loginUser);
            if (response.isError()) {
                tblIssueVo.setError(response.isError());
                tblIssueVo.setErrorCode(response.getErrorCode());
                tblIssueVo.setErrorMessage(response.getErrorMessage());
                return tblIssueVo;
            }
        }
        // 設備外部であれば、編集不可
        if (tblIssueVo.getMachineId() != null && !"".equals(tblIssueVo.getMachineId())) {
            BasicResponse response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, tblIssueVo.getMachineId(), "", loginUser);
            if (response.isError()) {
                tblIssueVo.setError(response.isError());
                tblIssueVo.setErrorCode(response.getErrorCode());
                tblIssueVo.setErrorMessage(response.getErrorMessage());
                return tblIssueVo;
            }
        }

        if (tblIssueVo.getId() != null && !"".equals(tblIssueVo.getId())) {
            //更新
            StringBuilder sql = new StringBuilder(" UPDATE TblIssue t SET  t.measureStatus = :measureStatus, ");

            if (tblIssueVo.getReportDate() != null) {
                sql.append(" t.reportDate = :reportDate, ");
            }
            
            if (tblIssueVo.getHappenedAt()!= null) {
                sql.append(" t.happenedAt = :happenedAt, ");
            }
            
            if (tblIssueVo.getMeasureSummary() != null) {
                sql.append(" t.measureSummary = :measureSummary, ");
            }

            if (tblIssueVo.getMeasureDueDate() != null) {
                sql.append(" t.measureDueDate = :measureDueDate, ");
            }

            if (tblIssueVo.getMoldUuid() != null) {
                if ("".equals(tblIssueVo.getMoldUuid())) {
                    sql.append(" t.moldUuid = null, ");
                } else {
                    sql.append(" t.moldUuid = :moldUuid, ");
                }
            }
            if (tblIssueVo.getComponentId() != null) {
                if ("".equals(tblIssueVo.getComponentId())) {
                    sql.append(" t.componentId = null, ");
                } else {
                    sql.append(" t.componentId = :componentId, ");
                }
            }
            if (tblIssueVo.getLotNumber() != null && !"".equals(tblIssueVo.getLotNumber())) {
                sql.append(" t.lotNumber = :lotNumber, ");
            }
            if (tblIssueVo.getProcedureId() != null && !"".equals(tblIssueVo.getProcedureId())) {
                sql.append(" t.procedureId = :procedureId, ");
            }
            if (tblIssueVo.getMachineUuid() != null) {
                if ("".equals(tblIssueVo.getMachineUuid())) {
                    sql.append(" t.machineUuid = null, ");
                } else {
                    sql.append(" t.machineUuid = :machineUuid, ");
                }
            }
            if (tblIssueVo.getReportDepartment() != null) {
                sql.append(" t.reportDepartment = :reportDepartment,t.reportDepartmentName = :reportDepartmentName, ");
            }

            if (tblIssueVo.getReportPhase() != null) {
                sql.append(" t.reportPhase = :reportPhase,t.reportPhaseText = :reportPhaseText, ");
            }

            if (tblIssueVo.getReportCategory1() != null) {
                sql.append(" t.reportCategory1 = :reportCategory1,t.reportCategory1Text = :reportCategory1Text, ");
            }

            if (tblIssueVo.getReportCategory2() != null) {
                sql.append(" t.reportCategory2 = :reportCategory2,t.reportCategory2Text = :reportCategory2Text, ");
            }

            if (tblIssueVo.getReportCategory3() != null) {
                sql.append(" t.reportCategory3 = :reportCategory3,t.reportCategory3Text = :reportCategory3Text, ");
            }
            
            if (tblIssueVo.getMemo01() != null) {
                sql.append(" t.memo01 = :memo01, ");
            }
            if (tblIssueVo.getMemo02() != null) {
                sql.append(" t.memo02 = :memo02, ");
            }
            if (tblIssueVo.getMemo03() != null) {
                sql.append(" t.memo03 = :memo03, ");
            }

            if (tblIssueVo.getReportFilePath01() != null) {
                sql.append(" t.reportFilePath01 = :reportFilePath01, ");
            }
            if (tblIssueVo.getReportFilePath02() != null) {
                sql.append(" t.reportFilePath02 = :reportFilePath02, ");
            }
            if (tblIssueVo.getReportFilePath03() != null) {
                sql.append(" t.reportFilePath03 = :reportFilePath03, ");
            }
            if (tblIssueVo.getReportFilePath04() != null) {
                sql.append(" t.reportFilePath04 = :reportFilePath04, ");
            }
            if (tblIssueVo.getReportFilePath05() != null) {
                sql.append(" t.reportFilePath05 = :reportFilePath05, ");
            }
            if (tblIssueVo.getReportFilePath06() != null) {
                sql.append(" t.reportFilePath06 = :reportFilePath06, ");
            }
            if (tblIssueVo.getReportFilePath07() != null) {
                sql.append(" t.reportFilePath07 = :reportFilePath07, ");
            }
            if (tblIssueVo.getReportFilePath08() != null) {
                sql.append(" t.reportFilePath08 = :reportFilePath08, ");
            }
            if (tblIssueVo.getReportFilePath09() != null) {
                sql.append(" t.reportFilePath09 = :reportFilePath09, ");
            }
            if (tblIssueVo.getReportFilePath10() != null) {
                sql.append(" t.reportFilePath10 = :reportFilePath10, ");
            }
            
            if (tblIssueVo.getIssue() != null) {
                sql.append(" t.issue = :issue, ");
            }

            if (tblIssueVo.getMeasureStatus() == CommonConstants.ISSUE_MEASURE_STATUS_NOTYET || tblIssueVo.getMeasureStatus() == CommonConstants.ISSUE_MEASURE_STATUS_RESOLVING || tblIssueVo.getMeasureStatus() == CommonConstants.ISSUE_MEASURE_STATUS_NONEED) {
                sql.append(" t.measuerCompletedDate = NULL, ");
            } else {
                sql.append(" t.measuerCompletedDate = :measuerCompletedDate, ");
            }
            sql.append(" t.quantity = :quantity, ");
            sql.append(" t.shotCountAtIssue = :shotCountAtIssue, ");
            sql.append(" t.mainteType = :mainteType, ");
            sql.append(" t.updateDate = :updateDate,t.updateUserUuid = :updateUserUuid ");
            sql.append(" WHERE t.id = :id ");

            Query query = entityManager.createQuery(sql.toString());

            query.setParameter("measureStatus", tblIssueVo.getMeasureStatus());

            if (tblIssueVo.getMeasureStatus() == CommonConstants.ISSUE_MEASURE_STATUS_TEMPORARILY_RESOLVED || tblIssueVo.getMeasureStatus() == CommonConstants.ISSUE_MEASURE_STATUS_PERMANENTLY_RESOLVED || tblIssueVo.getMeasureStatus() == CommonConstants.ISSUE_MEASURE_STATUS_COMPLETED) {
                query.setParameter("measuerCompletedDate", new Date());
            }

            if (tblIssueVo.getMeasureSummary() != null) {
                query.setParameter("measureSummary", tblIssueVo.getMeasureSummary());
            }

            try {
                if (tblIssueVo.getHappenedAt()!= null) {
                    query.setParameter("happenedAt", sdf.parse(tblIssueVo.getHappenedAt()));
                }
            } catch (ParseException ex) {
                Logger.getLogger(TblIssueService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                if (tblIssueVo.getReportDate() != null) {
                    query.setParameter("reportDate", sdf.parse(tblIssueVo.getReportDate()));

                }
                if (tblIssueVo.getMeasureDueDate() != null) {
                    query.setParameter("measureDueDate", sdf.parse(tblIssueVo.getMeasureDueDate()));
                }

            } catch (ParseException ex) {
                Logger.getLogger(TblIssueService.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (tblIssueVo.getMoldUuid() != null && !"".equals(tblIssueVo.getMoldUuid())) {
                query.setParameter("moldUuid", tblIssueVo.getMoldUuid());
            }
            if (tblIssueVo.getComponentId() != null && !"".equals(tblIssueVo.getComponentId())) {
                query.setParameter("componentId", tblIssueVo.getComponentId());
            }
            if (tblIssueVo.getLotNumber()!= null && !"".equals(tblIssueVo.getLotNumber())) {
                query.setParameter("lotNumber", tblIssueVo.getLotNumber());
            }
            if (tblIssueVo.getProcedureId()!= null && !"".equals(tblIssueVo.getProcedureId())) {
                query.setParameter("procedureId", tblIssueVo.getProcedureId());
            }
            if (tblIssueVo.getMachineUuid() != null && !"".equals(tblIssueVo.getMachineUuid())) {
                query.setParameter("machineUuid", tblIssueVo.getMachineUuid());
            }
            if (tblIssueVo.getReportDepartment() != null) {
                try {
                    query.setParameter("reportDepartment", Integer.parseInt(tblIssueVo.getReportDepartment()));
                    query.setParameter("reportDepartmentName", tblIssueVo.getReportDepartmentName());
                } catch (NumberFormatException e) {
                    query.setParameter("reportDepartment", Integer.parseInt("0"));
                    query.setParameter("reportDepartmentName", "");
                }
            }
            if (tblIssueVo.getReportPhase() != null) {
                try {
                    query.setParameter("reportPhase", Integer.parseInt(tblIssueVo.getReportPhase()));
                    query.setParameter("reportPhaseText", tblIssueVo.getReportPhaseText());
                } catch (NumberFormatException e) {
                    query.setParameter("reportPhase", Integer.parseInt("0"));
                    query.setParameter("reportPhaseText", "");
                }
            }

            if (tblIssueVo.getReportCategory1() != null) {
                try {
                    query.setParameter("reportCategory1", Integer.parseInt(tblIssueVo.getReportCategory1()));
                    query.setParameter("reportCategory1Text", tblIssueVo.getReportCategory1Text());
                } catch (NumberFormatException e) {
                    query.setParameter("reportCategory1", Integer.parseInt("0"));
                    query.setParameter("reportCategory1Text", "");
                }
            }

            if (tblIssueVo.getReportCategory2() != null) {
                try {
                    query.setParameter("reportCategory2", Integer.parseInt(tblIssueVo.getReportCategory2()));
                    query.setParameter("reportCategory2Text", tblIssueVo.getReportCategory2Text());
                } catch (NumberFormatException e) {
                    query.setParameter("reportCategory2", Integer.parseInt("0"));
                    query.setParameter("reportCategory2Text", "");
                }
            }

            if (tblIssueVo.getReportCategory3() != null) {
                try {
                    query.setParameter("reportCategory3", Integer.parseInt(tblIssueVo.getReportCategory3()));
                    query.setParameter("reportCategory3Text", tblIssueVo.getReportCategory3Text());
                } catch (NumberFormatException e) {
                    query.setParameter("reportCategory3", Integer.parseInt("0"));
                    query.setParameter("reportCategory3Text", "");
                }
            }
            
            if (tblIssueVo.getMemo01() != null) {
                query.setParameter("memo01", tblIssueVo.getMemo01());
            }
            if (tblIssueVo.getMemo02() != null) {
                query.setParameter("memo02", tblIssueVo.getMemo02());
            }
            if (tblIssueVo.getMemo03() != null) {
                query.setParameter("memo03", tblIssueVo.getMemo03());
            }
            
            if (tblIssueVo.getReportFilePath01() != null) {
                query.setParameter("reportFilePath01", tblIssueVo.getReportFilePath01());
            }
            if (tblIssueVo.getReportFilePath02() != null) {
                query.setParameter("reportFilePath02", tblIssueVo.getReportFilePath02());
            }
            if (tblIssueVo.getReportFilePath03() != null) {
                query.setParameter("reportFilePath03", tblIssueVo.getReportFilePath03());
            }
            if (tblIssueVo.getReportFilePath04() != null) {
                query.setParameter("reportFilePath04", tblIssueVo.getReportFilePath04());
            }
            if (tblIssueVo.getReportFilePath05() != null) {
                query.setParameter("reportFilePath05", tblIssueVo.getReportFilePath05());
            }
            if (tblIssueVo.getReportFilePath06() != null) {
                query.setParameter("reportFilePath06", tblIssueVo.getReportFilePath06());
            }
            if (tblIssueVo.getReportFilePath07() != null) {
                query.setParameter("reportFilePath07", tblIssueVo.getReportFilePath07());
            }
            if (tblIssueVo.getReportFilePath08() != null) {
                query.setParameter("reportFilePath08", tblIssueVo.getReportFilePath08());
            }
            if (tblIssueVo.getReportFilePath09() != null) {
                query.setParameter("reportFilePath09", tblIssueVo.getReportFilePath09());
            }
            if (tblIssueVo.getReportFilePath10() != null) {
                query.setParameter("reportFilePath10", tblIssueVo.getReportFilePath10());
            }
            if (tblIssueVo.getIssue() != null) {
                query.setParameter("issue", tblIssueVo.getIssue());
            }
            query.setParameter("quantity",tblIssueVo.getQuantity());
            query.setParameter("shotCountAtIssue",tblIssueVo.getShotCountAtIssue());
            query.setParameter("mainteType",tblIssueVo.getMainteType());
            query.setParameter("updateDate", new Date());
            query.setParameter("updateUserUuid", loginUser.getUserUuid());
            query.setParameter("id", tblIssueVo.getId());
            query.executeUpdate();
        } else {
            TblIssue tblIssue = new TblIssue();
            tblIssue.setId(IDGenerator.generate());
            tblIssueVo.setId(tblIssue.getId());
            tblIssue.setMeasureStatus(tblIssueVo.getMeasureStatus());
            if (tblIssueVo.getMeasureStatus() == CommonConstants.ISSUE_MEASURE_STATUS_TEMPORARILY_RESOLVED || tblIssueVo.getMeasureStatus() == CommonConstants.ISSUE_MEASURE_STATUS_PERMANENTLY_RESOLVED || tblIssueVo.getMeasureStatus() == CommonConstants.ISSUE_MEASURE_STATUS_COMPLETED) {
                tblIssue.setMeasuerCompletedDate(new Date());
            }
            try {
                if (tblIssueVo.getReportDate() != null && !"".equals(tblIssueVo.getReportDate())) {
                    tblIssue.setReportDate(sdf.parse(tblIssueVo.getReportDate()));
                }
                
                if (tblIssueVo.getHappenedAt()!= null && !"".equals(tblIssueVo.getHappenedAt())) {
                    tblIssue.setHappenedAt(sdf.parse(tblIssueVo.getHappenedAt()));
                    tblIssue.setHappenedAtStz(sdf.parse(tblIssueVo.getHappenedAt()));
                } else {
                    tblIssue.setHappenedAt(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
                    tblIssue.setHappenedAtStz(new Date());
                }
                
                if (tblIssueVo.getMoldUuid() != null && !"".equals(tblIssueVo.getMoldUuid())) {
                    tblIssue.setMoldUuid(tblIssueVo.getMoldUuid());
                }
                if (tblIssueVo.getComponentId() != null && !"".equals(tblIssueVo.getComponentId())) {
                    tblIssue.setComponentId(tblIssueVo.getComponentId());
                }
                if (tblIssueVo.getLotNumber() != null && !"".equals(tblIssueVo.getLotNumber())) {
                    tblIssue.setLotNumber(tblIssueVo.getLotNumber());
                }
                if (tblIssueVo.getProcedureId() != null && !"".equals(tblIssueVo.getProcedureId())) {
                    tblIssue.setProcedureId(tblIssueVo.getProcedureId());
                }
                if (tblIssueVo.getMachineUuid() != null && !"".equals(tblIssueVo.getMachineUuid())) {
                    tblIssue.setMachineUuid(tblIssueVo.getMachineUuid());
                }

                tblIssue.setReportPersonUuid(loginUser.getUserUuid());
                Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
                userQuery.setParameter("uuid", loginUser.getUserUuid());
                try {
                    MstUser user = (MstUser) userQuery.getSingleResult();
                    tblIssue.setReportPersonName(user.getUserName());
                } catch (NoResultException e) {
                    tblIssue.setReportPersonName(null);
                }

                if (tblIssueVo.getReportDepartment() != null && !"".equals(tblIssueVo.getReportDepartment())) {
                    tblIssue.setReportDepartment(Integer.parseInt(tblIssueVo.getReportDepartment()));
                }

                tblIssue.setReportDepartmentName(tblIssueVo.getReportDepartmentName());
                if (tblIssueVo.getReportPhase() != null && !"".equals(tblIssueVo.getReportPhase())) {
                    tblIssue.setReportPhase(Integer.parseInt(tblIssueVo.getReportPhase()));
                }

                tblIssue.setReportPhaseText(tblIssueVo.getReportPhaseText());
                if (tblIssueVo.getReportCategory1() != null && !"".equals(tblIssueVo.getReportCategory1())) {
                    tblIssue.setReportCategory1(Integer.parseInt(tblIssueVo.getReportCategory1()));
                }

                tblIssue.setReportCategory1Text(tblIssueVo.getReportCategory1Text());
                if (tblIssueVo.getReportCategory2() != null && !"".equals(tblIssueVo.getReportCategory2())) {
                    tblIssue.setReportCategory2(Integer.parseInt(tblIssueVo.getReportCategory2()));
                }

                tblIssue.setReportCategory2Text(tblIssueVo.getReportCategory2Text());
                if (tblIssueVo.getReportCategory3() != null && !"".equals(tblIssueVo.getReportCategory3())) {
                    tblIssue.setReportCategory3(Integer.parseInt(tblIssueVo.getReportCategory3()));
                }

                tblIssue.setReportCategory3Text(tblIssueVo.getReportCategory3Text());
                
                if (tblIssueVo.getMemo01() != null && !"".equals(tblIssueVo.getMemo01())) {
                    tblIssue.setMemo01(tblIssueVo.getMemo01());
                }
                if (tblIssueVo.getMemo02() != null && !"".equals(tblIssueVo.getMemo02())) {
                    tblIssue.setMemo02(tblIssueVo.getMemo02());
                }
                if (tblIssueVo.getMemo03() != null && !"".equals(tblIssueVo.getMemo03())) {
                    tblIssue.setMemo03(tblIssueVo.getMemo03());
                }
                
                if (tblIssueVo.getReportFilePath01() != null && !"".equals(tblIssueVo.getReportFilePath01())) {
                    tblIssue.setReportFilePath01(tblIssueVo.getReportFilePath01());
                }
                if (tblIssueVo.getReportFilePath02() != null && !"".equals(tblIssueVo.getReportFilePath02())) {
                    tblIssue.setReportFilePath02(tblIssueVo.getReportFilePath02());
                }
                if (tblIssueVo.getReportFilePath03() != null && !"".equals(tblIssueVo.getReportFilePath03())) {
                    tblIssue.setReportFilePath03(tblIssueVo.getReportFilePath03());
                }
                if (tblIssueVo.getReportFilePath04() != null && !"".equals(tblIssueVo.getReportFilePath04())) {
                    tblIssue.setReportFilePath04(tblIssueVo.getReportFilePath04());
                }
                if (tblIssueVo.getReportFilePath05() != null && !"".equals(tblIssueVo.getReportFilePath05())) {
                    tblIssue.setReportFilePath05(tblIssueVo.getReportFilePath05());
                }
                if (tblIssueVo.getReportFilePath06() != null && !"".equals(tblIssueVo.getReportFilePath06())) {
                    tblIssue.setReportFilePath06(tblIssueVo.getReportFilePath06());
                }
                if (tblIssueVo.getReportFilePath07() != null && !"".equals(tblIssueVo.getReportFilePath07())) {
                    tblIssue.setReportFilePath07(tblIssueVo.getReportFilePath07());
                }
                if (tblIssueVo.getReportFilePath08() != null && !"".equals(tblIssueVo.getReportFilePath08())) {
                    tblIssue.setReportFilePath08(tblIssueVo.getReportFilePath08());
                }
                if (tblIssueVo.getReportFilePath09() != null && !"".equals(tblIssueVo.getReportFilePath09())) {
                    tblIssue.setReportFilePath09(tblIssueVo.getReportFilePath09());
                }
                if (tblIssueVo.getReportFilePath10() != null && !"".equals(tblIssueVo.getReportFilePath10())) {
                    tblIssue.setReportFilePath10(tblIssueVo.getReportFilePath10());
                }

                tblIssue.setMeasureSummary(tblIssueVo.getMeasureSummary());
                tblIssue.setIssue(tblIssueVo.getIssue());
                if (tblIssueVo.getMeasureDueDate() != null && !"".equals(tblIssueVo.getMeasureDueDate())) {
                    tblIssue.setMeasureDueDate(sdf.parse(tblIssueVo.getMeasureDueDate()));
                }
                tblIssue.setQuantity(tblIssueVo.getQuantity());
                tblIssue.setShotCountAtIssue(tblIssueVo.getShotCountAtIssue());
                tblIssue.setMainteType(tblIssueVo.getMainteType());
                tblIssue.setUpdateDate(new Date());
                tblIssue.setUpdateUserUuid(loginUser.getUserUuid());
                tblIssue.setCreateDate(new Date());
                tblIssue.setCreateUserUuid(loginUser.getUserUuid());
            } catch (ParseException ex) {
                Logger.getLogger(TblIssueService.class.getName()).log(Level.SEVERE, null, ex);
            }
            //追加
            entityManager.persist(tblIssue);
        }

        //issueImageFile更新と追加 
        //削除、更新、追加
        Query query = entityManager.createNamedQuery("TblIssueImageFile.deleteByIssueId");
        query.setParameter("issueId", tblIssueVo.getId());
        query.executeUpdate();

        if (tblIssueVo.getTblIssueImageFileVoList() != null && tblIssueVo.getTblIssueImageFileVoList().size() > 0) {
            for (int i = 0; i < tblIssueVo.getTblIssueImageFileVoList().size(); i++) {
                TblIssueImageFileVo input = tblIssueVo.getTblIssueImageFileVoList().get(i);
                tblIssueImageFile = new TblIssueImageFile();
                tblIssueImageFilePK = new TblIssueImageFilePK();

                tblIssueImageFile.setFileUuid(input.getFileUuid());
                tblIssueImageFilePK.setIssueId(tblIssueVo.getId());
                tblIssueImageFilePK.setSeq((i + 1));
                tblIssueImageFile.setTblIssueImageFilePK(tblIssueImageFilePK);
                tblIssueImageFile.setFileExtension(input.getFileExtension());
                tblIssueImageFile.setFileType(input.getFileType());
                tblIssueImageFile.setRemarks(input.getRemarks());
                try {
                    if (input.getTakenDate() != null && !"".equals(input.getTakenDate())) {
                        tblIssueImageFile.setTakenDate(sdf.parse(DateFormat.dateTimeFormat(sdf.parse(input.getTakenDate()), loginUser.getJavaZoneId())));
                        tblIssueImageFile.setTakenDateStz(sdf.parse(input.getTakenDate()));
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(TblIssueService.class.getName()).log(Level.SEVERE, null, ex);
                }
                tblIssueImageFile.setThumbnailFileUuid(input.getThumbnailFileUuid());
                tblIssueImageFile.setCreateDate(new Date());
                tblIssueImageFile.setCreateDateUuid(loginUser.getUserUuid());
                tblIssueImageFile.setUpdateDate(new Date());
                tblIssueImageFile.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(tblIssueImageFile);

            }
        }

        return tblIssueVo;
    }

    /**
     * 通常打上 データチェック
     *
     * @param id
     * @return
     */
    public boolean checkIssue(String id) {
        Query query = entityManager.createNamedQuery("TblIssue.findById");
        query.setParameter("id", id);
        try {
            TblIssue tblIssue = (TblIssue) query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }

        return true;
    }

    /**
     * 通常打上,異常一覧 削除してよいか確認ダイアログを表示して、選択されている異常データを削除する。
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteIssue(String id) {
        Query query = entityManager.createNamedQuery("TblIssue.deleteTblIssue");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     * MoldMaintenanceRemodeling データチェック
     *
     * @param issueId
     * @return
     */
    public boolean checkMoldMaintenanceRemodeling(String issueId) {
        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findByIssueId");
        query.setParameter("issueId", issueId);
        if (query.getResultList().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * IssueImageFileをチェックして
     *
     * @param issueId
     * @return
     */
    public boolean checkIssueImageFile(String issueId) {
        Query query = entityManager.createNamedQuery("TblIssueImageFile.findByIssueId");
        query.setParameter("issueId", issueId);
        return query.getResultList().size() > 0;//trueで更新する、falseで追加する
    }

    /**
     * 異常一覧 CSV出力 異常情報テーブルから検索条件にあてはまるデータを検索しCSVファイルに出力する。
     * 出力順は打上の降順。検索条件が指定されていない場合は全件を出力する。
     *
     * @param measureStatus
     * @param measureStatusOperand     // Added parameter "measureStatusOperand"  for Issue: KM-743
     * @param department
     * @param reportDate
     * @param reportDateFrom
     * @param reportDateTo
     * @param measureDueDate
     * @param measureDueDateFrom
     * @param measureDueDateTo
     * @param moldId
     * @param moldName
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param reportPersonName
     * @param reportPhase
     * @param reportCategory1
     * @param reportCategory2
     * @param reportCategory3
     * @param memo01
     * @param memo02
     * @param memo03
     * @param happenedAt
     * @param loginUser
     * @return
     */
    public FileReponse getIssuesCsv(int measureStatus,int measureStatusOperand, String department, String reportDate,
            String reportDateFrom, String reportDateTo, String measureDueDate,
            String measureDueDateFrom, String measureDueDateTo, String moldId, String moldName,
            String componentCode, String componentName,
            String machineId, String machineName,
            String reportPersonName, int reportPhase, int reportCategory1, int reportCategory2, int reportCategory3, String memo01, String memo02, String memo03, String happenedAt,
            LoginUser loginUser) {

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        
        // 選択肢Map
        Map<String, String> choiceMap = mstChoiceService.getChoiceMap(loginUser.getLangId(), ISSUE_ARRAY);

        //CSVファイル出力
        String id = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, id);
        
        List keyList = new ArrayList();
        keyList.add("issue_report_date");
        keyList.add("issue_measure_due_date");
        keyList.add("m-karte_csv_upload_error_datetime");
        keyList.add("issue_measure_status");
        keyList.add("mold_id");
        keyList.add("mold_name");
        keyList.add("component_code");
        keyList.add("component_name");
        keyList.add("machine_id");
        keyList.add("machine_name");
        keyList.add("issue_reported_department");
        keyList.add("issue_report_phase");
        keyList.add("issue_report_category1");
        keyList.add("issue_report_category2");
        keyList.add("issue_report_category3");
        keyList.add("issue");
        keyList.add("issue_memo01");
        keyList.add("issue_memo02");
        keyList.add("issue_memo03");
        keyList.add("issue_measure_summary");
        keyList.add("issue_measuer_completed_date");
        keyList.add("mold_mainte_date");
        keyList.add("machine_mainte_date");
        keyList.add("issue_report_person_name");
        keyList.add("quantity");
        keyList.add("shot_count_at_issue");
        Map<String, String> dictKey = mstDictionaryService.getDictionaryHashMap(langId, keyList);
        
        String outIssueReportDate = dictKey.get("issue_report_date");
        String outIssueMeasureDueDate = dictKey.get("issue_measure_due_date");
        String outHappenedAt = dictKey.get("m-karte_csv_upload_error_datetime");
        String outIssueMeasureStatus = dictKey.get("issue_measure_status");
        String outMoldId = dictKey.get("mold_id");
        String outMoldName = dictKey.get("mold_name");
        String outShotCountAtIssue = dictKey.get("shot_count_at_issue");
        String outComponentCode = dictKey.get("component_code");
        String outComponentName = dictKey.get("component_name");
        String outMachineId = dictKey.get("machine_id");
        String outMachineName = dictKey.get("machine_name");
        String outIssueReportedDepartment = dictKey.get("issue_reported_department");
        String outIssueReportPhase = dictKey.get("issue_report_phase");
        String outIssueReportCategory1 = dictKey.get("issue_report_category1");
        String outIssueReportCategory2 = dictKey.get("issue_report_category2");
        String outIssueReportCategory3 = dictKey.get("issue_report_category3");
        String outIssue = dictKey.get("issue");
        String outMemo01 = dictKey.get("issue_memo01");
        String outMemo02 = dictKey.get("issue_memo02");
        String outMemo03 = dictKey.get("issue_memo03");
        String outIssueMeasureSummary = dictKey.get("issue_measure_summary");
        String outIssueMeasuerCompleteDate = dictKey.get("issue_measuer_completed_date");
        String outMoldMaintenanceDate = dictKey.get("mold_mainte_date");
        String outMachineMaintenanceDate = dictKey.get("machine_mainte_date");
        String outReportPersonName = dictKey.get("issue_report_person_name");
        String outQuantity = dictKey.get("quantity");


        /*Head*/
        HeadList.add(outIssueReportDate);
        HeadList.add(outIssueMeasureDueDate);
        HeadList.add(outHappenedAt);
        HeadList.add(outIssueMeasureStatus);
        HeadList.add(outMachineId);
        HeadList.add(outMachineName);
        HeadList.add(outMoldId);
        HeadList.add(outMoldName);
        HeadList.add(outShotCountAtIssue);
        HeadList.add(outComponentCode);
        HeadList.add(outComponentName);
        HeadList.add(outQuantity);
        HeadList.add(outReportPersonName);
        HeadList.add(outIssueReportedDepartment);
        HeadList.add(outIssueReportPhase);
        HeadList.add(outIssueReportCategory1);
        HeadList.add(outIssueReportCategory2);
        HeadList.add(outIssueReportCategory3);
        HeadList.add(outIssue);
        HeadList.add(outMemo01);
        HeadList.add(outMemo02);
        HeadList.add(outMemo03);
        HeadList.add(outIssueMeasureSummary);
        HeadList.add(outIssueMeasuerCompleteDate);
        HeadList.add(outMoldMaintenanceDate);
        HeadList.add(outMachineMaintenanceDate);

        gLineList.add(HeadList);

        List<TblIssue> list = getIssues(0, 0, measureStatus, measureStatusOperand, department, reportDate, reportDateFrom, reportDateTo, measureDueDate,
                measureDueDateFrom, measureDueDateTo, moldId, moldName, componentCode, componentName, machineId, machineName, "", reportPersonName, 
                false, reportPhase, reportCategory1, reportCategory2, reportCategory3, memo01, memo02, memo03, happenedAt);
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATE_FORMAT);
        SimpleDateFormat sdtf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT_YYYYMMDDHHMM);

        for (int i = 0; i < list.size(); i++) {
            lineList = new ArrayList();
            TblIssue tblIssue = list.get(i);

            String reportDates = "";
            if (tblIssue.getReportDate() != null) {
                reportDates = sdf.format(tblIssue.getReportDate());
            }
            lineList.add(reportDates);

            String measureDueDates = "";
            if (tblIssue.getMeasureDueDate() != null) {
                measureDueDates = sdf.format(tblIssue.getMeasureDueDate());
            }
            lineList.add(measureDueDates);
            
            String happenedAtDate = "";
            if (tblIssue.getHappenedAt() != null) {
                happenedAtDate = sdtf.format(tblIssue.getHappenedAt());
            }
            lineList.add(happenedAtDate);

            if (!fu.isNullCheck(String.valueOf(tblIssue.getMeasureStatus()))) {
                String strMeasureStatus = outMeasureStatus.get(String.valueOf(tblIssue.getMeasureStatus()));
                lineList.add(strMeasureStatus);
            } else {
                lineList.add("");
            }

            if (tblIssue.getMstMachine() != null) {
                lineList.add(tblIssue.getMstMachine().getMachineId());
                lineList.add(tblIssue.getMstMachine().getMachineName());
            } else {
                lineList.add("");
                lineList.add("");
            }

            if (tblIssue.getMstMold() != null) {
                lineList.add(tblIssue.getMstMold().getMoldId());
                lineList.add(tblIssue.getMstMold().getMoldName());
            } else {
                lineList.add("");
                lineList.add("");
            }
            lineList.add(String.valueOf(tblIssue.getShotCountAtIssue()));
            if (tblIssue.getMstComponent() != null) {
                lineList.add(tblIssue.getMstComponent().getComponentCode());
                lineList.add(tblIssue.getMstComponent().getComponentName());
            } else {
                lineList.add("");
                lineList.add("");
            }
            
            lineList.add(String.valueOf(tblIssue.getQuantity()));
            
            //報告者 // KM-359 打上一覧に報告者追加
            if (tblIssue.getMstUser() != null) {
                lineList.add(tblIssue.getMstUser().getUserName());
            } else {
                lineList.add("");
            }

            lineList.add(FileUtil.getStr(choiceMap.get(USER_DEPARTMENT + String.valueOf(tblIssue.getReportDepartment()))));
            lineList.add(FileUtil.getStr(choiceMap.get(ISSUE_REPORT_PHASE + String.valueOf(tblIssue.getReportPhase()))));
            lineList.add(FileUtil.getStr(choiceMap.get(ISSUE_REPORT_CATEGORY1 + String.valueOf(tblIssue.getReportCategory1()))));
            lineList.add(FileUtil.getStr(choiceMap.get(ISSUE_REPORT_CATEGORY2 + String.valueOf(tblIssue.getReportCategory2()))));
            lineList.add(FileUtil.getStr(choiceMap.get(ISSUE_REPORT_CATEGORY3 + String.valueOf(tblIssue.getReportCategory3()))));
            
            lineList.add(tblIssue.getIssue() == null ? "" : tblIssue.getIssue());
            lineList.add(tblIssue.getMemo01() == null ? "" : tblIssue.getMemo01());
            lineList.add(tblIssue.getMemo02() == null ? "" : tblIssue.getMemo02());
            lineList.add(tblIssue.getMemo03() == null ? "" : tblIssue.getMemo03());
            
            lineList.add(tblIssue.getMeasureSummary() == null ? "" : tblIssue.getMeasureSummary());

            String measuerCompletedDate = "";
            if (tblIssue.getMeasuerCompletedDate() != null) {
                measuerCompletedDate = sdf.format(tblIssue.getMeasuerCompletedDate());
            }
            lineList.add(measuerCompletedDate);

            if (tblIssue.getTblMoldMaintenanceRemodeling() != null) {
                String mainteDate = "";
                if (tblIssue.getTblMoldMaintenanceRemodeling().getMainteDate() != null) {
                    mainteDate = sdf.format(tblIssue.getTblMoldMaintenanceRemodeling().getMainteDate());
                }
                lineList.add(mainteDate);
            } else {
                lineList.add("");
            }

            if (tblIssue.getTblMachineMaintenanceRemodeling() != null) {
                String mainteDate = "";
                if (tblIssue.getTblMachineMaintenanceRemodeling().getMainteDate() != null) {
                    mainteDate = sdf.format(tblIssue.getTblMachineMaintenanceRemodeling().getMainteDate());
                }
                lineList.add(mainteDate);
            } else {
                lineList.add("");
            }

            gLineList.add(lineList);
        }
        
        CSVFileUtil.writeCsv(outCsvPath, gLineList);
        
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(id);
        tblCsvExport.setExportTable(CommonConstants.TBL_ISSUE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD_ISSUE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.TBL_ISSUE);
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(id);

        return fr;
    }

    /**
     * バッチで異常テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblIssueList getExtIssuesByBatch(String latestExecutedDate, String moldUuid) {
        TblIssueList resList = new TblIssueList();
        List<TblIssueVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblIssue t join MstApiUser u on u.companyId = t.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.mstMold.uuid = :moldUuid ");
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
        List<TblIssue> tmpList = query.getResultList();
        for (TblIssue tblIssue : tmpList) {
            TblIssueVo aRes = new TblIssueVo();

            List<TblIssueImageFile> imageFiles = new ArrayList(tblIssue.getTblIssueImageFile());
            if (imageFiles.isEmpty() == false) {
                List<TblIssueImageFileVo> imageFileVos = new ArrayList<>();
                for (TblIssueImageFile imageFile : imageFiles) {
                    TblIssueImageFileVo aVo = new TblIssueImageFileVo();
                    aVo.setFileUuid(imageFile.getFileUuid());
                    aVo.setIssueId(imageFile.getTblIssueImageFilePK().getIssueId());
                    aVo.setSeq(imageFile.getTblIssueImageFilePK().getSeq());
                    aVo.setFileType(imageFile.getFileType());
                    aVo.setFileExtension(imageFile.getFileExtension());
                    if (null != imageFile.getTakenDate()) {
                        aVo.setTakenDate(new FileUtil().getDateTimeFormatForStr(imageFile.getTakenDate()));
                    }
                    if (null != imageFile.getTakenDateStz()) {
                        aVo.setTakenDateStz(new FileUtil().getDateTimeFormatForStr(imageFile.getTakenDateStz()));
                    }
                    aVo.setRemarks(imageFile.getRemarks());
                    aVo.setThumbnailFileUuid(imageFile.getThumbnailFileUuid());

                    aVo.setCreateDate(imageFile.getCreateDate());
                    aVo.setCreateDateUuid(imageFile.getCreateDateUuid());
                    aVo.setUpdateDate(new Date());
                    aVo.setUpdateUserUuid(imageFile.getUpdateUserUuid());

                    imageFileVos.add(aVo);
                }
                aRes.setTblIssueImageFileVoList(imageFileVos);
            }
            MstMold mstMold = null;
            if (null != (mstMold = tblIssue.getMstMold())) {
                aRes.setMoldId(mstMold.getMoldId());
            }
            tblIssue.setMstMold(null);
            MstComponent mstComponent = null;
            if (null != (mstComponent = tblIssue.getMstComponent())) {
                aRes.setComponentCode(mstComponent.getComponentCode());
            }
            tblIssue.setMstComponent(null);

            tblIssue.setTblIssueImageFile(null);
            tblIssue.setTblMoldMaintenanceRemodeling(null);
            tblIssue.setTblMachineMaintenanceRemodeling(null);

            aRes.setTblIssue(tblIssue);
            resVo.add(aRes);
        }
        resList.setTblIssueVoList(resVo);
        return resList;
    }

    /**
     * バッチで異常テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public TblIssueList getExtMachineIssuesByBatch(String latestExecutedDate, String machineUuid) {
        TblIssueList resList = new TblIssueList();
        List<TblIssueVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblIssue t join MstApiUser u on u.companyId = t.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.mstMachine.uuid = :machineUuid ");
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
        List<TblIssue> tmpList = query.getResultList();
        for (TblIssue tblIssue : tmpList) {
            TblIssueVo aRes = new TblIssueVo();

            List<TblIssueImageFile> imageFiles = new ArrayList(tblIssue.getTblIssueImageFile());
            if (imageFiles.isEmpty() == false) {
                List<TblIssueImageFileVo> imageFileVos = new ArrayList<>();
                for (TblIssueImageFile imageFile : imageFiles) {
                    TblIssueImageFileVo aVo = new TblIssueImageFileVo();
                    aVo.setFileUuid(imageFile.getFileUuid());
                    aVo.setIssueId(imageFile.getTblIssueImageFilePK().getIssueId());
                    aVo.setSeq(imageFile.getTblIssueImageFilePK().getSeq());
                    aVo.setFileType(imageFile.getFileType());
                    aVo.setFileExtension(imageFile.getFileExtension());
                    if (null != imageFile.getTakenDate()) {
                        aVo.setTakenDate(new FileUtil().getDateTimeFormatForStr(imageFile.getTakenDate()));
                    }
                    if (null != imageFile.getTakenDateStz()) {
                        aVo.setTakenDateStz(new FileUtil().getDateTimeFormatForStr(imageFile.getTakenDateStz()));
                    }
                    aVo.setRemarks(imageFile.getRemarks());
                    aVo.setThumbnailFileUuid(imageFile.getThumbnailFileUuid());

                    aVo.setCreateDate(imageFile.getCreateDate());
                    aVo.setCreateDateUuid(imageFile.getCreateDateUuid());
                    aVo.setUpdateDate(new Date());
                    aVo.setUpdateUserUuid(imageFile.getUpdateUserUuid());

                    imageFileVos.add(aVo);
                }
                aRes.setTblIssueImageFileVoList(imageFileVos);
            }
            MstMold mstMold = null;
            if (null != (mstMold = tblIssue.getMstMold())) {
                aRes.setMoldId(mstMold.getMoldId());
            }
            tblIssue.setMstMold(null);
            MstMachine mstMachine = null;
            if (null != (mstMachine = tblIssue.getMstMachine())) {
                aRes.setMachineId(mstMachine.getMachineId());
            }
            tblIssue.setMstMachine(null);
            MstComponent mstComponent = null;
            if (null != (mstComponent = tblIssue.getMstComponent())) {
                aRes.setComponentCode(mstComponent.getComponentCode());
            }
            tblIssue.setMstComponent(null);

            tblIssue.setTblIssueImageFile(null);
            tblIssue.setTblMoldMaintenanceRemodeling(null);
            tblIssue.setTblMachineMaintenanceRemodeling(null);

            aRes.setTblIssue(tblIssue);
            resVo.add(aRes);
        }
        resList.setTblIssueVoList(resVo);
        return resList;
    }

    /**
     * バッチで異常テーブルデータを更新
     *
     * @param issueVos
     * @param withMainTe
     * @param mstComponentCompanyVoList
     * @return
     */
    @Transactional
    public BasicResponse updateExtIssuesByBatch(List<TblIssueVo> issueVos, boolean withMainTe, MstComponentCompanyVoList mstComponentCompanyVoList) {

        BasicResponse response = new BasicResponse();

        if (issueVos != null && !issueVos.isEmpty()) {

            for (TblIssueVo aIssueVo : issueVos) {

                List<TblIssue> oldMoldMaintenanceDetails = entityManager.createQuery("SELECT t FROM TblIssue t WHERE 1=1 and t.id=:id ")
                        .setParameter("id", aIssueVo.getTblIssue().getId())
                        .setMaxResults(1)
                        .getResultList();
                TblIssue newIssue;
                if (null != oldMoldMaintenanceDetails && !oldMoldMaintenanceDetails.isEmpty()) {
                    newIssue = oldMoldMaintenanceDetails.get(0);
                } else {
                    newIssue = new TblIssue();
                    newIssue.setId(aIssueVo.getTblIssue().getId());
                }

                MstMold ownerMold = null;
                if (StringUtils.isNotEmpty(aIssueVo.getMoldId())) {
                    ownerMold = entityManager.find(MstMold.class, aIssueVo.getMoldId());
                    //自社の金型UUIDに変換                    
                    if (null != ownerMold) {
                        newIssue.setMoldUuid(ownerMold.getUuid());
                    }
                }

                MstMachine ownerMachine = null;
                if (StringUtils.isNotEmpty(aIssueVo.getMachineId())) {
                    ownerMachine = entityManager.find(MstMachine.class, aIssueVo.getMachineId());
                    if (null != ownerMachine) {
                        newIssue.setMachineUuid(ownerMachine.getUuid());
                    }
                }

                if (null == ownerMold && null == ownerMachine) {
                    return response;
                }

                newIssue.setReportDate(aIssueVo.getTblIssue().getReportDate());
                newIssue.setHappenedAt(aIssueVo.getTblIssue().getHappenedAt());
                newIssue.setHappenedAtStz(aIssueVo.getTblIssue().getHappenedAtStz());
                newIssue.setReportPersonUuid(aIssueVo.getTblIssue().getReportPersonUuid());
                newIssue.setReportPersonName(aIssueVo.getTblIssue().getReportPersonName());
                newIssue.setQuantity(aIssueVo.getTblIssue().getQuantity());
                //自社のComponentIDに変換
                if (StringUtils.isNotEmpty(aIssueVo.getTblIssue().getComponentId())) {
                    Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
                    query.setParameter("componentCode", aIssueVo.getComponentCode());
                    List<MstComponent> mstComponentList = query.getResultList();
                    if (null != mstComponentList && !mstComponentList.isEmpty()) {
                        newIssue.setComponentId(mstComponentList.get(0).getId());
                    } else if (mstComponentCompanyVoList.getMstComponentCompanyVos().size() > 0) {
                        for (MstComponentCompanyVo mstComponentCompanyVo : mstComponentCompanyVoList.getMstComponentCompanyVos()) {
                            if (StringUtils.isNotEmpty(aIssueVo.getComponentCode()) && StringUtils.isNotEmpty(mstComponentCompanyVo.getOtherComponentCode())) {
                                if (aIssueVo.getComponentCode().equals(mstComponentCompanyVo.getComponentCode())) {
                                    // 先方部品により自社の部品IDを置換
                                    query.setParameter("componentCode", mstComponentCompanyVo.getOtherComponentCode());
                                    List<MstComponent> otherMstComponentList = query.getResultList();
                                    if (null != otherMstComponentList && !otherMstComponentList.isEmpty()) {
                                        newIssue.setComponentId(otherMstComponentList.get(0).getId());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    newIssue.setComponentId(null);
                }

                newIssue.setReportDepartment(FileUtil.getIntegerValue(aIssueVo.getTblIssue().getReportDepartment()));
                newIssue.setReportDepartmentName(aIssueVo.getTblIssue().getReportDepartmentName());
                newIssue.setReportPhase(FileUtil.getIntegerValue(aIssueVo.getTblIssue().getReportPhase()));
                newIssue.setReportPhaseText(aIssueVo.getTblIssue().getReportPhaseText());
                newIssue.setReportCategory1(FileUtil.getIntegerValue(aIssueVo.getTblIssue().getReportCategory1()));
                newIssue.setReportCategory1Text(aIssueVo.getTblIssue().getReportCategory1Text());
                newIssue.setReportCategory2(FileUtil.getIntegerValue(aIssueVo.getTblIssue().getReportCategory2()));
                newIssue.setReportCategory2Text(aIssueVo.getTblIssue().getReportCategory2Text());
                newIssue.setReportCategory3(FileUtil.getIntegerValue(aIssueVo.getTblIssue().getReportCategory3()));
                newIssue.setReportCategory3Text(aIssueVo.getTblIssue().getReportCategory3Text());
                newIssue.setMemo01(aIssueVo.getTblIssue().getMemo01());
                newIssue.setMemo02(aIssueVo.getTblIssue().getMemo02());
                newIssue.setMemo03(aIssueVo.getTblIssue().getMemo03());
                newIssue.setIssue(aIssueVo.getTblIssue().getIssue());
                newIssue.setMeasureDueDate(aIssueVo.getTblIssue().getMeasureDueDate());
                newIssue.setMeasureStatus(FileUtil.getIntegerValue(aIssueVo.getTblIssue().getMeasureStatus()));
                newIssue.setMeasuerCompletedDate(aIssueVo.getTblIssue().getMeasuerCompletedDate());
                newIssue.setMeasureSummary(aIssueVo.getTblIssue().getMeasureSummary());
                // 金型メンテＩＤ
                if (StringUtils.isNotEmpty(aIssueVo.getTblIssue().getMainTenanceId())) {
                    TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling = entityManager.find(TblMoldMaintenanceRemodeling.class, aIssueVo.getTblIssue().getMainTenanceId());
                    if (tblMoldMaintenanceRemodeling != null) {
                        newIssue.setMainTenanceId(tblMoldMaintenanceRemodeling.getId());
                    }
                }
                //設備メンテＩＤ
                if (StringUtils.isNotEmpty(aIssueVo.getTblIssue().getMachineMainTenanceId())) {
                    TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling = entityManager.find(TblMachineMaintenanceRemodeling.class, aIssueVo.getTblIssue().getMachineMainTenanceId());
                    if (tblMachineMaintenanceRemodeling != null) {
                        newIssue.setMachineMainTenanceId(tblMachineMaintenanceRemodeling.getId());
                    }
                }
                newIssue.setReportFilePath01(aIssueVo.getTblIssue().getReportFilePath01());
                newIssue.setReportFilePath02(aIssueVo.getTblIssue().getReportFilePath02());
                newIssue.setReportFilePath03(aIssueVo.getTblIssue().getReportFilePath03());
                newIssue.setReportFilePath04(aIssueVo.getTblIssue().getReportFilePath04());
                newIssue.setReportFilePath05(aIssueVo.getTblIssue().getReportFilePath05());
                newIssue.setReportFilePath06(aIssueVo.getTblIssue().getReportFilePath06());
                newIssue.setReportFilePath07(aIssueVo.getTblIssue().getReportFilePath07());
                newIssue.setReportFilePath08(aIssueVo.getTblIssue().getReportFilePath08());
                newIssue.setReportFilePath09(aIssueVo.getTblIssue().getReportFilePath09());
                newIssue.setReportFilePath10(aIssueVo.getTblIssue().getReportFilePath10());

                newIssue.setCreateDate(aIssueVo.getTblIssue().getCreateDate());
                newIssue.setCreateUserUuid(aIssueVo.getTblIssue().getCreateUserUuid());
                newIssue.setUpdateDate(new Date());
                newIssue.setUpdateUserUuid(aIssueVo.getTblIssue().getUpdateUserUuid());
                if (null != oldMoldMaintenanceDetails && !oldMoldMaintenanceDetails.isEmpty()) {
                    entityManager.merge(newIssue);
                } else {
                    entityManager.persist(newIssue);
                }

                if (withMainTe && null != aIssueVo.getTblIssueImageFileVoList()) {
                    for (TblIssueImageFileVo aVo : aIssueVo.getTblIssueImageFileVoList()) {
                        TblIssueImageFilePK pk = new TblIssueImageFilePK();
                        pk.setIssueId(aVo.getIssueId());
                        pk.setSeq(aVo.getSeq());
                        TblIssueImageFile issueImageFile;
                        List<TblIssueImageFile> oldIssueImageFiles = entityManager.createQuery("SELECT t FROM TblIssueImageFile t WHERE t.tblIssueImageFilePK.issueId = :issueId and t.tblIssueImageFilePK.seq = :seq ")
                                .setParameter("issueId", pk.getIssueId())
                                .setParameter("seq", pk.getSeq())
                                .setMaxResults(1)
                                .getResultList();
                        if (null == oldIssueImageFiles || oldIssueImageFiles.isEmpty()) {
                            issueImageFile = new TblIssueImageFile();
                            issueImageFile.setTblIssueImageFilePK(pk);
                        } else {
                            issueImageFile = oldIssueImageFiles.get(0);
                        }

                        issueImageFile.setFileUuid(aVo.getFileUuid());
                        issueImageFile.setFileType(FileUtil.getIntegerValue(aVo.getFileType()));
                        issueImageFile.setFileExtension(aVo.getFileExtension());
                        if (StringUtils.isNotEmpty(aVo.getTakenDate())) {
                            issueImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(aVo.getTakenDate()));
                        } else {
                            issueImageFile.setTakenDateStz(null);
                        }

                        if (StringUtils.isNotEmpty(aVo.getTakenDateStz())) {
                            issueImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aVo.getTakenDateStz()));
                        } else {
                            issueImageFile.setTakenDateStz(null);
                        }
                        issueImageFile.setRemarks(aVo.getRemarks());
                        issueImageFile.setThumbnailFileUuid(aVo.getThumbnailFileUuid());

                        issueImageFile.setCreateDate(aVo.getCreateDate());
                        issueImageFile.setCreateDateUuid(aVo.getCreateDateUuid());
                        issueImageFile.setUpdateDate(new Date());
                        issueImageFile.setUpdateUserUuid(aVo.getUpdateUserUuid());

                        if (null == oldIssueImageFiles || oldIssueImageFiles.isEmpty()) {
                            entityManager.persist(issueImageFile);
                        } else {
                            entityManager.merge(issueImageFile);
                        }
                    }
                }
            }
        }
        response.setError(false);
        return response;
    }
    
    /**
     * 異常一覧,通常打上 異常情報複数取得
     *
     * @param measureStatus
     * @param measureStatusOperand     // Added parameter "measureStatusOperand"  for Issue: KM-743
     * @param department
     * @param reportDate
     * @param reportDateFrom
     * @param reportDateTo
     * @param measureDueDate
     * @param moldId
     * @param moldName
     * @param measureDueDateFrom
     * @param measureDueDateFromTo
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param mainTenanceId
     * @param reportPersonName
     * @param tblIssueList
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @param reportPhase
     * @param reportCategory1
     * @param reportCategory2
     * @param reportCategory3
     * @param memo01
     * @param memo02
     * @param memo03
     * @param happenedAt
     * @param moldUuid
     * @param machineUuid
     * @return
     */
        // Added parameter "measureStatusOperand"  for Issue: KM-743
    public List<TblIssue> getIssuesByPage(int measureStatus,int measureStatusOperand, String department, String reportDate,
            String reportDateFrom, String reportDateTo, String measureDueDate, String measureDueDateFrom,
            String measureDueDateFromTo, String moldId, String moldName, String componentCode, String componentName,
            String machineId, String machineName, String mainTenanceId, String reportPersonName, // KM-359
                                                                                                 // 打上一覧に報告者追加
            TblIssueList tblIssueList, String sidx, String sord, int pageNumber, int pageSize, boolean isPage,
            int reportPhase, int reportCategory1, int reportCategory2, int reportCategory3, String memo01, String memo02, String memo03, 
            String happenedAt, String moldUuid, String machineUuid) {

        if (isPage) {

            List count = getIssueSqlByPage(measureStatus,measureStatusOperand, department, reportDate, reportDateFrom, reportDateTo,
                    measureDueDate, measureDueDateFrom, measureDueDateFromTo, moldId, moldName, componentCode,
                    componentName, machineId, machineName, mainTenanceId, reportPersonName, sidx, sord, pageNumber,
                    pageSize, true, reportPhase, reportCategory1, reportCategory2, reportCategory3, memo01, memo02, memo03, moldUuid, machineUuid);

            // ページをめぐる
            Pager pager = new Pager();
            tblIssueList.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            tblIssueList.setCount(counts);
            tblIssueList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }

        List<TblIssue> list = getIssueSqlByPage(measureStatus,measureStatusOperand, department, reportDate, reportDateFrom, reportDateTo,
                measureDueDate, measureDueDateFrom, measureDueDateFromTo, moldId, moldName, componentCode,
                componentName, machineId, machineName, mainTenanceId, reportPersonName, sidx, sord, pageNumber,
                pageSize, false, reportPhase, reportCategory1, reportCategory2, reportCategory3, memo01, memo02, memo03, moldUuid, machineUuid);
        return list;
    }
    
    /**
     * sql文の用
     *
     * @param measureStatus
     * @param measureStatusOperand
     * @param department
     * @param reportDate
     * @param reportDateFrom
     * @param reportDateTo
     * @param measureDueDate
     * @param measureDueDateForm
     * @param measureDueDateTo
     * @param moldId
     * @param moldName
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param mainTenanceId
     * @param reportPersonName
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @param reportPhase
     * @param reportCategory1
     * @param reportCategory2
     * @param reportCategory3
     * @param memo01
     * @param memo02
     * @param memo03
     * @param moldUuid
     * @param machineUuid
     * @return
     */
    // Added parameter "measureStatusOperand"  for Issue: KM-743
    public List getIssueSqlByPage(int measureStatus,int measureStatusOperand, String department, String reportDate, String reportDateFrom, String reportDateTo, String measureDueDate,
            String measureDueDateForm, String measureDueDateTo,
            String moldId, String moldName, String componentCode, String componentName, String machineId, String machineName, String mainTenanceId,
            String reportPersonName, // KM-359 打上一覧に報告者追加
            String sidx,
            String sord, int pageNumber, int pageSize, boolean isCount, int reportPhase, int reportCategory1, int reportCategory2, int reportCategory3, String memo01,
            String memo02, String memo03, String moldUuid, String machineUuid) {
        StringBuilder sql = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        if (isCount) {
            sql.append("SELECT count(issue.id) FROM TblIssue issue WHERE 1=1 ");
        } else {
            sql.append("SELECT issue FROM TblIssue issue " + " LEFT JOIN FETCH issue.mstMold mold "
                    + " LEFT JOIN FETCH issue.mstComponent mc " + " LEFT JOIN FETCH issue.tblMoldMaintenanceRemodeling moldmt"
                    + " LEFT JOIN FETCH issue.mstProcedure mp "
                    + " LEFT JOIN FETCH issue.mstMachine machine" + " LEFT JOIN FETCH issue.tblMachineMaintenanceRemodeling machinemt"
                    + " LEFT JOIN FETCH issue.mstUser mu" + " WHERE 1=1 ");
        }
        if (measureStatus != -1) {
            if (measureStatusOperand !=1)
            {
                sql.append(" And issue.measureStatus = :measureStatus ");  
            }
            else{
                sql.append(" And issue.measureStatus != :measureStatus ");
            }
        }

        if (department != null && !"0".equals(department)) {
            sql.append(" And issue.reportDepartment = :reportDepartment ");
        }

        if (reportDate != null && !"".equals(reportDate)) {
            sql.append(" And issue.reportDate = :reportDate ");
        }
        if (reportDateFrom != null && !"".equals(reportDateFrom)) {
            sql.append(" And issue.reportDate >= :reportDateFrom  ");
        }

        if (reportDateTo != null && !"".equals(reportDateTo)) {
            sql.append(" And issue.reportDate <= :reportDateTo ");
        }

        if (measureDueDate != null && !"".equals(measureDueDate)) {
            sql.append(" And issue.measureDueDate = :measureDueDate ");
        }

        if (measureDueDateForm != null && !"".equals(measureDueDateForm)) {
            sql.append(" And issue.measureDueDate >= :measureDueDateForm ");
        }

        if (measureDueDateTo != null && !"".equals(measureDueDateTo)) {
            sql.append(" And issue.measureDueDate <= :measureDueDateTo ");
        }

        if (moldId != null && !"".equals(moldId)) {
            sql.append(" And issue.mstMold.moldId LIKE :moldId ");
        }
        if (moldName != null && !"".equals(moldName)) {
            sql.append(" And issue.mstMold.moldName like :moldName ");
        }
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" And issue.mstComponent.componentCode LIKE :componentCode ");
        }
        if (componentName != null && !"".equals(componentName)) {
            sql.append(" And issue.mstComponent.componentName like :componentName ");
        }
        if (machineId != null && !"".equals(machineId)) {
            sql.append(" And issue.mstMachine.machineId LIKE :machineId ");
        }
        if (machineName != null && !"".equals(machineName)) {
            sql.append(" And issue.mstMachine.machineName like :machineName ");
        }
        if (mainTenanceId != null && !"".equals(mainTenanceId)) {
            // -1 means mainTenanceId is null
            sql.append(mainTenanceId.equals("-1") ? "And issue.mainTenanceId is null "
                    : " And issue.mainTenanceId = :mainTenanceId ");
        }

        // KM-359 打上一覧に報告者追加
        if (StringUtils.isNotEmpty(reportPersonName)) {
            sql.append(" AND issue.mstUser.userName LIKE :reportPersonName ");
        }
        if (reportPhase != 0) {
            sql.append(" And issue.reportPhase = :reportPhase ");
        }
        if (reportCategory1 != 0) {
            sql.append(" And issue.reportCategory1 = :reportCategory1 ");
        }
        if (reportCategory2 != 0) {
            sql.append(" And issue.reportCategory2 = :reportCategory2 ");
        }
        if (reportCategory3 != 0) {
            sql.append(" And issue.reportCategory3 = :reportCategory3 ");
        }
        
        if (memo01 != null && !"".equals(memo01)) {
            sql.append(" And issue.memo01 like :memo01 ");
        }
        if (memo02 != null && !"".equals(memo02)) {
            sql.append(" And issue.memo02 like :memo02 ");
        }
        if (memo03 != null && !"".equals(memo03)) {
            sql.append(" And issue.memo03 like :memo03 ");
        }
        
        if (moldUuid != null && !"".equals(moldUuid)) {
            sql.append(" And issue.moldUuid = :moldUuid ");
        }
        
        if (machineUuid != null && !"".equals(machineUuid)) {
            sql.append(" And issue.machineUuid = :machineUuid ");
        }
        
        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);

            } else {

                sql.append(" Order By issue.reportDate desc, issue.happenedAt desc ");

            }

        }
        
        Query query = entityManager.createQuery(sql.toString());

        if (measureStatus != -1) {
            query.setParameter("measureStatus", measureStatus);
        }

        if (department != null && !"0".equals(department)) {
            try {
                query.setParameter("reportDepartment", Integer.parseInt(department));
            } catch (NumberFormatException e) {
                query.setParameter("reportDepartment", 0);
            }
        }

        try {
            if (reportDate != null && !"".equals(reportDate)) {
                query.setParameter("reportDate", sdf.parse(reportDate));
            }
            if (measureDueDate != null && !"".equals(measureDueDate)) {
                query.setParameter("measureDueDate", sdf.parse(measureDueDate));
            }
            if (moldId != null && !"".equals(moldId)) {
                query.setParameter("moldId", "%" + moldId + "%");
            }
            if (moldName != null && !"".equals(moldName)) {
                query.setParameter("moldName", "%" + moldName + "%");
            }
            if (componentCode != null && !"".equals(componentCode)) {
                query.setParameter("componentCode", "%" + componentCode + "%");
            }
            if (componentName != null && !"".equals(componentName)) {
                query.setParameter("componentName", "%" + componentName + "%");
            }
            if (machineId != null && !"".equals(machineId)) {
                query.setParameter("machineId", "%" + machineId + "%");
            }
            if (machineName != null && !"".equals(machineName)) {
                query.setParameter("machineName", "%" + machineName + "%");
            }
            if (mainTenanceId != null && !"".equals(mainTenanceId) && !"-1".equals(mainTenanceId)) {
                query.setParameter("mainTenanceId", mainTenanceId);
            }

            if (reportDateFrom != null && !"".equals(reportDateFrom)) {
                query.setParameter("reportDateFrom", sdf.parse(reportDateFrom));
            }

            if (reportDateTo != null && !"".equals(reportDateTo)) {
                query.setParameter("reportDateTo", sdf.parse(reportDateTo));
            }

            if (measureDueDateForm != null && !"".equals(measureDueDateForm)) {
                query.setParameter("measureDueDateForm", sdf.parse(measureDueDateForm));
            }

            if (measureDueDateTo != null && !"".equals(measureDueDateTo)) {
                query.setParameter("measureDueDateTo", sdf.parse(measureDueDateTo));
            }

            // KM-359 打上一覧に報告者追加
            if (StringUtils.isNotEmpty(reportPersonName)) {
                query.setParameter("reportPersonName", "%" + reportPersonName + "%");
            }
            if (reportPhase != 0) {
                query.setParameter("reportPhase", reportPhase);
            }
            if (reportCategory1 != 0) {
                query.setParameter("reportCategory1", reportCategory1);
            }
            if (reportCategory2 != 0) {
                query.setParameter("reportCategory2", reportCategory2);
            }
            if (reportCategory3 != 0) {
                query.setParameter("reportCategory3", reportCategory3);
            }
            
            if (memo01 != null && !"".equals(memo01)) {
                query.setParameter("memo01", "%" + memo01 + "%");
            }
            if (memo02 != null && !"".equals(memo02)) {
                query.setParameter("memo02", "%" + memo02 + "%");
            }
            if (memo03 != null && !"".equals(memo03)) {
                query.setParameter("memo03", "%" + memo03 + "%");
            }
            if (moldUuid != null && !"".equals(moldUuid)) {
                query.setParameter("moldUuid", moldUuid);
            }
            if (machineUuid != null && !"".equals(machineUuid)) {
                query.setParameter("machineUuid", machineUuid);
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(TblIssueService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        
        return query.getResultList();
    }
}
