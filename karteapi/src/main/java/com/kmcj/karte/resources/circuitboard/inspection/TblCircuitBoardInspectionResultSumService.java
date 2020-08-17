/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.graphical.GraphicalAxis;
import com.kmcj.karte.graphical.GraphicalData;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.point.CircuitBoardPointData;
import com.kmcj.karte.resources.circuitboard.point.MstCircuitBoardPoint;
import com.kmcj.karte.resources.circuitboard.point.MstCircuitBoardPointService;
import com.kmcj.karte.resources.circuitboard.targetppm.CircuitBoardTargetPpmData;
import com.kmcj.karte.resources.circuitboard.targetppm.MstCircuitBoardTargetPpm;
import com.kmcj.karte.resources.circuitboard.targetppm.MstCircuitBoardTargetPpmService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author h.ishihara
 */
@Dependent
public class TblCircuitBoardInspectionResultSumService {
    @PersistenceContext(unitName = "pu_karte") 
    private EntityManager entityManager;

    @Inject
    private TblCircuitBoardInspectionResultService tblCircuitBoardInspectionResultService;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;    
        
    @Inject
    private MstCircuitBoardPointService mstCircuitBoardPointService;
    
    @Inject
    private MstCircuitBoardTargetPpmService mstCircuitBoardTargetPpmService;
    
    public InspectionResult searchInspectionResultSumsByCriteria(String inspectorId, String procedureId, Date inspectionDate){
//        StringBuilder sql = new StringBuilder(" SELECT t FROM TblCircuitBoardInspectionResultSum t "
//                + " WHERE t.inspectorId = :inspectorId and t.inspectionDate=:inspectionDate ");
//        if (null != procedureId && !procedureId.trim().equals("")) {
//            sql.append(" AND t.procedureId = :procedureId ");
//        }
//        Query query =  entityManager.createQuery(sql.toString());
//        query.setParameter("inspectorId", inspectorId);
//        query.setParameter("inspectionDate", inspectionDate);
//        if (null != procedureId && !procedureId.trim().equals("")) {
//            query.setParameter("procedureId", procedureId);
//        }          

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DATE_FORMAT(t.DTCOL1,'%Y/%m'), t.CIRCUIT_BOARD_CODE, ");
        sql.append(" (SELECT p1.ID FROM mst_component p1 WHERE t.CIRCUIT_BOARD_CODE = p1.COMPONENT_CODE) COMP_ID, ");
        sql.append(" COUNT(DISTINCT t.SERIAL_NUMBER) TOTAL_CNT, ");
        sql.append(" (SELECT COUNT(DISTINCT tmp.SERIAL_NUMBER) FROM tbl_automatic_machine_log tmp WHERE tmp.TXTCOL5 = 'NG' AND DATE_FORMAT(tmp.DTCOL1,'%Y/%m') = DATE_FORMAT(t.DTCOL1,'%Y/%m') AND tmp.CIRCUIT_BOARD_CODE = t.CIRCUIT_BOARD_CODE) NG_CNT, ");
        sql.append(" (SELECT pt.PRODUCT_CODE FROM mst_component p, mst_product pt, mst_product_component pc WHERE t.CIRCUIT_BOARD_CODE = p.COMPONENT_CODE and p.ID = pc.COMPONENT_ID and pc.PRODUCT_ID = pt.PRODUCT_ID) PDT_CODE ");
        sql.append(" FROM tbl_automatic_machine_log t ");
        sql.append(" WHERE t.MACHINE_TYPE IN ('");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI_NG);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_ICT);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT_NG);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FCT);
        sql.append("')");
                   
        if (inspectionDate != null) {
            sql.append(" AND t.DTCOL1 >= ? ");
        }
        if (inspectionDate != null) {
            sql.append(" AND t.DTCOL1 <= ? ");
        }
        
        sql.append(" GROUP BY DATE_FORMAT(t.DTCOL1,'%Y/%m'), t.CIRCUIT_BOARD_CODE ");
        sql.append(" ORDER BY DATE_FORMAT(t.DTCOL1,'%Y/%m'), t.CIRCUIT_BOARD_CODE ");
        
        Query query = entityManager.createNativeQuery(sql.toString());
        
//        if (!StringUtils.isEmpty(productCode)) {
//            query.setParameter("productCode", productCode.trim());
//        }

        int idx = 1;        
        if (inspectionDate != null) {
            query.setParameter(idx++, inspectionDate);
        }

        if (inspectionDate != null) {
            query.setParameter(idx++, inspectionDate);
        }
        
        HashMap<String,HashMap<String, Integer>> partNumList = null;
        
        List list1 = query.getResultList();
        
        CircuitBoardPointData pointData = null;
        if(list1.size() >0){
//            partNumList = TblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode,0,null, inspectionDateStart, inspectionDateEnd);
//            pointData = MstCircuitBoardPointService.getCircuitBoardPoints();
        }
        
        BigDecimal million = new BigDecimal(1000000);
        ArrayList sumList = new ArrayList();
        for(Object obj : list1){
            Object[] logData = (Object[]) obj;
            CircuitBoardInspectionResultSum inspectionResultSum = new CircuitBoardInspectionResultSum();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
            try
            {
                inspectionResultSum.setInspectionDateStr(logData[0].toString());
            }
            catch(Exception e)
            {
            }

            if (logData[1] == null)
            {
            inspectionResultSum.setComponentName(null);
            }
            else
            {
            inspectionResultSum.setComponentName(logData[1].toString());
            }
                
            if (logData[2] == null)
            {
            inspectionResultSum.setComponentId(null);
            }
            else
            {
            inspectionResultSum.setComponentId(logData[2].toString());
            }
            
            inspectionResultSum.setInspectorName(null);
            inspectionResultSum.setInspectorId(null);
            if (logData[3] != null)
            {
                inspectionResultSum.setInspectedItemNum(Integer.parseInt(logData[3].toString()));
            }

            if (logData[4] != null)
            {
                inspectionResultSum.setDefectiveItemNum(Integer.parseInt(logData[4].toString()));
            }
            
            try
            {
                inspectionResultSum.setInspectionDate(sdf.parse(logData[0].toString()));
            }
            catch(Exception e)
            {
            }
                        
            if (inspectionResultSum.getInspectedItemNum() > 0 && pointData !=null) {
                Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(inspectionResultSum.getComponentId())).findFirst();//.get();
                MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
                if (circuitBoardPoint != null && circuitBoardPoint.getPoint() >0) {
                    //double itemppm = (((double) sum.getDefectiveItemNum()) / ((double) (sum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
                    //double d = sum.getDefectiveItemNum() / (sum.getInspectedItemNum() * circuitBoardPoint.getPoint());
                    BigDecimal itemppm = new BigDecimal(inspectionResultSum.getDefectiveItemNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
                    //BigDecimal itemppm = BigDecimal.valueOf(d).divide().multiply(1000000);//(sum.getDefectiveItemNum() / (sum.getInspectedItemNum() * circuitBoardPoint.getPoint())))* 1000000;
                    inspectionResultSum.setPpmDefectiveItemNum(itemppm);
                }
            }else{
                if (pointData == null)
                {
                    BigDecimal itemppm = new BigDecimal(inspectionResultSum.getDefectiveItemNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * 1)),10, BigDecimal.ROUND_DOWN).multiply(million);
                    inspectionResultSum.setPpmDefectiveItemNum(itemppm);                    
                }
                else
                {
                inspectionResultSum.setPpmDefectiveItemNum(BigDecimal.ZERO);                    
                }                
            }

            inspectionResultSum.setOperators("");

//            if(sum.getTblCircuitBoardWorkAssignmentCollection()!=null){
//                StringBuilder sb = new StringBuilder();
//                for(TblCircuitBoardWorkAssignment workAssignment : inspectionResultSum.getTblCircuitBoardWorkAssignmentCollection()){
//                    sb.append(workAssignment.getMstUser().getUserName());
//                    sb.append("<br>");
//                }
//                inspectionResultSum.setOperators(sb.toString());
//            }else{
//                 inspectionResultSum.setOperators("");
//            }
            
            if (partNumList != null) {
                String key = inspectionResultSum.getInspectorId() + ","  + inspectionResultSum.getInspectionDateStr();
                if (partNumList.containsKey(key)) {
                    HashMap<String, Integer> defectList = partNumList.get(key);
                    StringBuilder detail = new StringBuilder();
                    for (Map.Entry<String, Integer> entry : defectList.entrySet()) {
                        if(detail.length()!=0){
                            detail.append("<br>");
                        }
                        detail.append(entry.getKey()).append(": ").append(entry.getValue());
                    }
                    inspectionResultSum.setDefectiveItemDetail(detail.toString());
                    int total = defectList.values().stream().mapToInt(i -> i).sum();
                    inspectionResultSum.setDefectivePartNum(total);
                    if (pointData !=null && inspectionResultSum.getDefectivePartNum() != null && inspectionResultSum.getInspectedItemNum() != null && inspectionResultSum.getInspectedItemNum() > 0) {
                        Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(inspectionResultSum.getComponentId())).findFirst();//.get();
                        MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
                        if (circuitBoardPoint != null && circuitBoardPoint.getPoint() > 0) {
                            //double partppm = (((double) inspectionResultSum.getDefectivePartNum()) / ((double) (inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
                            BigDecimal partppm = new BigDecimal(inspectionResultSum.getDefectivePartNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
                            inspectionResultSum.setPpmDefectivePartNum(partppm);
                        }
                    }else{
                        inspectionResultSum.setPpmDefectivePartNum(BigDecimal.ZERO);
                    }
                }else{
                    inspectionResultSum.setDefectivePartNum(0);
                    inspectionResultSum.setPpmDefectivePartNum(BigDecimal.ZERO);
                }
            }
            sumList.add(inspectionResultSum);
        }
        
        InspectionResult list = new InspectionResult();
        list.setInspectionResultSumList(sumList);
       
       Query namedQuery =  entityManager.createNamedQuery("TblCircuitBoardWorkAssignment.findByCircuitBoardInspectionResultSumId");

       for(TblCircuitBoardInspectionResultSum sum :  list.getInspectionResultSumList()){
           namedQuery.setParameter("resultSumId", sum.getCircuitBoardInspectionResultSumId());
           List assignmentList = namedQuery.getResultList();
           sum.setTblCircuitBoardWorkAssignmentCollection(assignmentList);
       }
               
       return list;
    }
    
     /**
     * @param productCode
     * @param componentId
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List searchInspectionResultSumsByCriteria(
            String productCode,
            String componentCode,
            Date inspectionDateStart,
            Date inspectionDateEnd,
            int pageNumber,
            int pageSize
    ) {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DATE_FORMAT(t.DTCOL1,'%Y/%m/%d'), t.CIRCUIT_BOARD_CODE, ");
        sql.append(" (SELECT p1.ID FROM mst_component p1 WHERE t.CIRCUIT_BOARD_CODE = p1.COMPONENT_CODE) COMP_ID, ");
        sql.append(" COUNT(DISTINCT t.SERIAL_NUMBER) TOTAL_CNT, ");
        sql.append(" (SELECT COUNT(DISTINCT tmp.SERIAL_NUMBER) FROM tbl_automatic_machine_log tmp WHERE tmp.TXTCOL5 = 'NG' AND DATE_FORMAT(tmp.DTCOL1,'%Y/%m/%d') = DATE_FORMAT(t.DTCOL1,'%Y/%m/%d') AND tmp.CIRCUIT_BOARD_CODE = t.CIRCUIT_BOARD_CODE) NG_CNT, ");
        sql.append(" (SELECT pt.PRODUCT_CODE FROM mst_component p, mst_product pt, mst_product_component pc WHERE t.CIRCUIT_BOARD_CODE = p.COMPONENT_CODE and p.ID = pc.COMPONENT_ID and pc.PRODUCT_ID = pt.PRODUCT_ID) PDT_CODE ");
        sql.append(" FROM tbl_automatic_machine_log t ");
        sql.append(" WHERE t.MACHINE_TYPE IN ('");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI_NG);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_ICT);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT_NG);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FCT);
        sql.append("')");
                   
        if (!StringUtils.isEmpty(componentCode)) {
            sql.append(" AND t.CIRCUIT_BOARD_CODE = ? ");
        }       

        if (inspectionDateStart != null) {
            sql.append(" AND t.DTCOL1 >= ? ");
        }
        if (inspectionDateEnd != null) {
            sql.append(" AND t.DTCOL1 <= ? ");
        }
        
        sql.append(" GROUP BY DATE_FORMAT(t.DTCOL1,'%Y/%m/%d'), t.CIRCUIT_BOARD_CODE ");
        sql.append(" ORDER BY DATE_FORMAT(t.DTCOL1,'%Y/%m/%d'), t.CIRCUIT_BOARD_CODE ");
        
        Query query = entityManager.createNativeQuery(sql.toString());
        
//        if (!StringUtils.isEmpty(productCode)) {
//            query.setParameter("productCode", productCode.trim());
//        }

        int idx = 1;
        if (!StringUtils.isEmpty(componentCode)) {
            query.setParameter(idx++, componentCode.trim());
        }

        if (inspectionDateStart != null) {
            query.setParameter(idx++, inspectionDateStart);
        }

        if (inspectionDateEnd != null) {
            query.setParameter(idx++, inspectionDateEnd);
        }

        if (pageNumber != 0 && pageSize != 0) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        
        HashMap<String,HashMap<String, Integer>> partNumList = null;
        
        List list = query.getResultList();
        
        CircuitBoardPointData pointData = null;
        if(list.size() >0){
//            partNumList = TblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode,0,null, inspectionDateStart, inspectionDateEnd);
//            pointData = MstCircuitBoardPointService.getCircuitBoardPoints();
        }
        
        BigDecimal million = new BigDecimal(1000000);
        ArrayList sumList = new ArrayList();
        for(Object obj : list){
            Object[] logData = (Object[]) obj;
            CircuitBoardInspectionResultSum inspectionResultSum = new CircuitBoardInspectionResultSum();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try
            {
                inspectionResultSum.setInspectionDateStr(logData[0].toString());
            }
            catch(Exception e)
            {
            }

            if (logData[1] == null)
            {
            inspectionResultSum.setComponentName(null);
            }
            else
            {
            inspectionResultSum.setComponentName(logData[1].toString());
            }
                
            if (logData[2] == null)
            {
            inspectionResultSum.setComponentId(null);
            }
            else
            {
            inspectionResultSum.setComponentId(logData[2].toString());
            }
            
            inspectionResultSum.setInspectorName(null);
            inspectionResultSum.setInspectorId(null);
            if (logData[3] != null)
            {
                inspectionResultSum.setInspectedItemNum(Integer.parseInt(logData[3].toString()));
            }

            if (logData[4] != null)
            {
                inspectionResultSum.setDefectiveItemNum(Integer.parseInt(logData[4].toString()));
            }
            
            try
            {
                inspectionResultSum.setInspectionDate(sdf.parse(logData[0].toString()));
            }
            catch(Exception e)
            {
            }
                        
            if (inspectionResultSum.getInspectedItemNum() > 0 && pointData !=null) {
                Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(inspectionResultSum.getComponentId())).findFirst();//.get();
                MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
                if (circuitBoardPoint != null && circuitBoardPoint.getPoint() >0) {
                    //double itemppm = (((double) sum.getDefectiveItemNum()) / ((double) (sum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
                    //double d = sum.getDefectiveItemNum() / (sum.getInspectedItemNum() * circuitBoardPoint.getPoint());
                    BigDecimal itemppm = new BigDecimal(inspectionResultSum.getDefectiveItemNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
                    //BigDecimal itemppm = BigDecimal.valueOf(d).divide().multiply(1000000);//(sum.getDefectiveItemNum() / (sum.getInspectedItemNum() * circuitBoardPoint.getPoint())))* 1000000;
                    inspectionResultSum.setPpmDefectiveItemNum(itemppm);
                }
            }else{
                if (pointData == null)
                {
                    BigDecimal itemppm = new BigDecimal(inspectionResultSum.getDefectiveItemNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * 1)),10, BigDecimal.ROUND_DOWN).multiply(million);
                    inspectionResultSum.setPpmDefectiveItemNum(itemppm);                    
                }
                else
                {
                inspectionResultSum.setPpmDefectiveItemNum(BigDecimal.ZERO);                    
                }                
            }

            inspectionResultSum.setOperators("");

//            if(sum.getTblCircuitBoardWorkAssignmentCollection()!=null){
//                StringBuilder sb = new StringBuilder();
//                for(TblCircuitBoardWorkAssignment workAssignment : inspectionResultSum.getTblCircuitBoardWorkAssignmentCollection()){
//                    sb.append(workAssignment.getMstUser().getUserName());
//                    sb.append("<br>");
//                }
//                inspectionResultSum.setOperators(sb.toString());
//            }else{
//                 inspectionResultSum.setOperators("");
//            }
            
            if (partNumList != null) {
                String key = inspectionResultSum.getInspectorId() + ","  + inspectionResultSum.getInspectionDateStr();
                if (partNumList.containsKey(key)) {
                    HashMap<String, Integer> defectList = partNumList.get(key);
                    StringBuilder detail = new StringBuilder();
                    for (Map.Entry<String, Integer> entry : defectList.entrySet()) {
                        if(detail.length()!=0){
                            detail.append("<br>");
                        }
                        detail.append(entry.getKey()).append(": ").append(entry.getValue());
                    }
                    inspectionResultSum.setDefectiveItemDetail(detail.toString());
                    int total = defectList.values().stream().mapToInt(i -> i).sum();
                    inspectionResultSum.setDefectivePartNum(total);
                    if (pointData !=null && inspectionResultSum.getDefectivePartNum() != null && inspectionResultSum.getInspectedItemNum() != null && inspectionResultSum.getInspectedItemNum() > 0) {
                        Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(inspectionResultSum.getComponentId())).findFirst();//.get();
                        MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
                        if (circuitBoardPoint != null && circuitBoardPoint.getPoint() > 0) {
                            //double partppm = (((double) inspectionResultSum.getDefectivePartNum()) / ((double) (inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
                            BigDecimal partppm = new BigDecimal(inspectionResultSum.getDefectivePartNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
                            inspectionResultSum.setPpmDefectivePartNum(partppm);
                        }
                    }else{
                        inspectionResultSum.setPpmDefectivePartNum(BigDecimal.ZERO);
                    }
                }else{
                    inspectionResultSum.setDefectivePartNum(0);
                    inspectionResultSum.setPpmDefectivePartNum(BigDecimal.ZERO);
                }
            }
            sumList.add(inspectionResultSum);
        }
        return sumList;
        
//        StringBuilder sql = new StringBuilder(" SELECT t FROM TblCircuitBoardInspectionResultSum t");
//        sql.append(" LEFT JOIN MstProductComponent mstProductComponent ON mstProductComponent.componentId = t.componentId");
//        sql.append(" LEFT JOIN MstProduct mstProduct ON mstProduct.productId = mstProductComponent.productId");
//        sql.append(" WHERE 1=1");
//        if (!StringUtils.isEmpty(productCode)) {
//            sql = sql.append(" AND mstProduct.productCode = :productCode ");
//        }
//
//        if (!StringUtils.isEmpty(componentId)) {
//            sql = sql.append(" AND t.componentId = :componentId ");
//        }       
//
//        if (inspectionDateStart != null) {
//            sql = sql.append(" and t.inspectionDate >= :inspectionDateStart ");
//        }
//
//        if (inspectionDateEnd != null) {
//            sql = sql.append(" and t.inspectionDate <= :inspectionDateEnd ");
//        }
//
//        Query query = entityManager.createQuery(sql.toString());
//
//        if (!StringUtils.isEmpty(productCode)) {
//            query.setParameter("productCode", productCode.trim());
//        }
//        if (!StringUtils.isEmpty(componentId)) {
//            query.setParameter("componentId", componentId.trim());
//        }
//
//        if (inspectionDateStart != null) {
//            query.setParameter("inspectionDateStart", inspectionDateStart);
//        }
//
//        if (inspectionDateEnd != null) {
//            query.setParameter("inspectionDateEnd", inspectionDateEnd);
//        }
//        
//        if (pageNumber != 0 && pageSize != 0) {
//            Pager pager = new Pager();
//            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
//            query.setMaxResults(pageSize);
//        }
//        HashMap<String,HashMap<String, Integer>> partNumList = null;
//        List list = query.getResultList();
//        CircuitBoardPointData pointData = null;
//        if(list.size() >0){
//            partNumList = tblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode,0,null, inspectionDateStart, inspectionDateEnd);
//            pointData = mstCircuitBoardPointService.getCircuitBoardPoints();
//        }
//        
//        BigDecimal million = new BigDecimal(1000000);
//        ArrayList sumList = new ArrayList();
//        for(Object obj : list){
//            TblCircuitBoardInspectionResultSum sum = (TblCircuitBoardInspectionResultSum)obj;
//            CircuitBoardInspectionResultSum inspectionResultSum = new CircuitBoardInspectionResultSum();
//            inspectionResultSum.setInspectionDateStr(FileUtil.dateFormat(sum.getInspectionDate()));
//            inspectionResultSum.setComponentId(sum.getComponentId());
//            inspectionResultSum.setComponentName(sum.getMstComponent().getComponentName());
//            inspectionResultSum.setInspectorName(sum.getMstUser().getUserName());
//            inspectionResultSum.setInspectorId(sum.getInspectorId());
//            inspectionResultSum.setInspectedItemNum(sum.getInspectedItemNum());
//            inspectionResultSum.setDefectiveItemNum(sum.getDefectiveItemNum());
//            inspectionResultSum.setInspectionDate(sum.getInspectionDate());
//            if (sum.getInspectedItemNum() > 0 && pointData !=null) {
//                Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(sum.getComponentId())).findFirst();//.get();
//                MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
//                if (circuitBoardPoint != null && circuitBoardPoint.getPoint() >0) {
//                    //double itemppm = (((double) sum.getDefectiveItemNum()) / ((double) (sum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
//                    //double d = sum.getDefectiveItemNum() / (sum.getInspectedItemNum() * circuitBoardPoint.getPoint());
//                    BigDecimal itemppm = new BigDecimal(sum.getDefectiveItemNum()).divide((new BigDecimal(sum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
//                    //BigDecimal itemppm = BigDecimal.valueOf(d).divide().multiply(1000000);//(sum.getDefectiveItemNum() / (sum.getInspectedItemNum() * circuitBoardPoint.getPoint())))* 1000000;
//                    inspectionResultSum.setPpmDefectiveItemNum(itemppm);
//                }
//            }else{
//                inspectionResultSum.setPpmDefectiveItemNum(BigDecimal.ZERO);
//            }
//            if(sum.getTblCircuitBoardWorkAssignmentCollection()!=null){
//                StringBuilder sb = new StringBuilder();
//                for(TblCircuitBoardWorkAssignment workAssignment : sum.getTblCircuitBoardWorkAssignmentCollection()){
//                    sb.append(workAssignment.getMstUser().getUserName());
//                    sb.append("<br>");
//                }
//                inspectionResultSum.setOperators(sb.toString());
//            }else{
//                 inspectionResultSum.setOperators("");
//            }
//            
//            if (partNumList != null) {
//                String key = inspectionResultSum.getInspectorId() + ","  + inspectionResultSum.getInspectionDateStr();
//                if (partNumList.containsKey(key)) {
//                    HashMap<String, Integer> defectList = partNumList.get(key);
//                    StringBuilder detail = new StringBuilder();
//                    for (Entry<String, Integer> entry : defectList.entrySet()) {
//                        if(detail.length()!=0){
//                            detail.append("<br>");
//                        }
//                        detail.append(entry.getKey()).append(": ").append(entry.getValue());
//                    }
//                    inspectionResultSum.setDefectiveItemDetail(detail.toString());
//                    int total = defectList.values().stream().mapToInt(i -> i).sum();
//                    inspectionResultSum.setDefectivePartNum(total);
//                    if (pointData !=null && inspectionResultSum.getDefectivePartNum() != null && inspectionResultSum.getInspectedItemNum() != null && inspectionResultSum.getInspectedItemNum() > 0) {
//                        Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(inspectionResultSum.getComponentId())).findFirst();//.get();
//                        MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
//                        if (circuitBoardPoint != null && circuitBoardPoint.getPoint() > 0) {
//                            //double partppm = (((double) inspectionResultSum.getDefectivePartNum()) / ((double) (inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
//                            BigDecimal partppm = new BigDecimal(inspectionResultSum.getDefectivePartNum()).divide((new BigDecimal(sum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
//                            inspectionResultSum.setPpmDefectivePartNum(partppm);
//                        }
//                    }else{
//                        inspectionResultSum.setPpmDefectivePartNum(BigDecimal.ZERO);
//                    }
//                }else{
//                    inspectionResultSum.setDefectivePartNum(0);
//                    inspectionResultSum.setPpmDefectivePartNum(BigDecimal.ZERO);
//                }
//            }
//            sumList.add(inspectionResultSum);
//        }
//        return sumList;
    }
    
         /**
     * @param productCode
     * @param componentId
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List searchInspectionMonthlyResultSumsByCriteria(
            String productCode,
            String componentCode,
            Date inspectionDateStart,
            Date inspectionDateEnd,
            int pageNumber,
            int pageSize
    ) {
              
       StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DATE_FORMAT(t.DTCOL1,'%Y/%m'), t.CIRCUIT_BOARD_CODE, ");
        sql.append(" (SELECT p1.ID FROM mst_component p1 WHERE t.CIRCUIT_BOARD_CODE = p1.COMPONENT_CODE) COMP_ID, ");
        sql.append(" COUNT(DISTINCT t.SERIAL_NUMBER) TOTAL_CNT, ");
        sql.append(" (SELECT COUNT(DISTINCT tmp.SERIAL_NUMBER) FROM tbl_automatic_machine_log tmp WHERE tmp.TXTCOL5 = 'NG' AND DATE_FORMAT(tmp.DTCOL1,'%Y/%m') = DATE_FORMAT(t.DTCOL1,'%Y/%m') AND tmp.CIRCUIT_BOARD_CODE = t.CIRCUIT_BOARD_CODE) NG_CNT, ");
        sql.append(" (SELECT pt.PRODUCT_CODE FROM mst_component p, mst_product pt, mst_product_component pc WHERE t.CIRCUIT_BOARD_CODE = p.COMPONENT_CODE and p.ID = pc.COMPONENT_ID and pc.PRODUCT_ID = pt.PRODUCT_ID) PDT_CODE ");
        sql.append(" FROM tbl_automatic_machine_log t ");
        sql.append(" WHERE t.MACHINE_TYPE IN ('");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI_NG);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_ICT);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT_NG);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FCT);
        sql.append("')");
                   
        if (!StringUtils.isEmpty(componentCode)) {
            sql.append(" AND t.CIRCUIT_BOARD_CODE = ? ");
        }       

        if (inspectionDateStart != null) {
            sql.append(" AND t.DTCOL1 >= ? ");
        }
        if (inspectionDateEnd != null) {
            sql.append(" AND t.DTCOL1 <= ? ");
        }
        
        sql.append(" GROUP BY DATE_FORMAT(t.DTCOL1,'%Y/%m'), t.CIRCUIT_BOARD_CODE ");
        sql.append(" ORDER BY DATE_FORMAT(t.DTCOL1,'%Y/%m'), t.CIRCUIT_BOARD_CODE ");
        
        Query query = entityManager.createNativeQuery(sql.toString());
        
//        if (!StringUtils.isEmpty(productCode)) {
//            query.setParameter("productCode", productCode.trim());
//        }

        int idx = 1;
        if (!StringUtils.isEmpty(componentCode)) {
            query.setParameter(idx++, componentCode.trim());
        }

        if (inspectionDateStart != null) {
            query.setParameter(idx++, inspectionDateStart);
        }

        if (inspectionDateEnd != null) {
            query.setParameter(idx++, inspectionDateEnd);
        }

        if (pageNumber != 0 && pageSize != 0) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        
        HashMap<String,HashMap<String, Integer>> partNumList = null;
        
        List list = query.getResultList();
        
        CircuitBoardPointData pointData = null;
        if(list.size() >0){
//            partNumList = TblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode,0,null, inspectionDateStart, inspectionDateEnd);
//            pointData = MstCircuitBoardPointService.getCircuitBoardPoints();
        }
        
        BigDecimal million = new BigDecimal(1000000);
        ArrayList sumList = new ArrayList();
        for(Object obj : list){
            Object[] logData = (Object[]) obj;
            CircuitBoardInspectionResultSum inspectionResultSum = new CircuitBoardInspectionResultSum();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
            try
            {
                inspectionResultSum.setInspectionDateStr(logData[0].toString());
            }
            catch(Exception e)
            {
            }

            if (logData[1] == null)
            {
            inspectionResultSum.setComponentName(null);
            }
            else
            {
            inspectionResultSum.setComponentName(logData[1].toString());
            }
                
            if (logData[2] == null)
            {
            inspectionResultSum.setComponentId(null);
            }
            else
            {
            inspectionResultSum.setComponentId(logData[2].toString());
            }
            
            inspectionResultSum.setInspectorName(null);
            inspectionResultSum.setInspectorId(null);
            if (logData[3] != null)
            {
                inspectionResultSum.setInspectedItemNum(Integer.parseInt(logData[3].toString()));
            }

            if (logData[4] != null)
            {
                inspectionResultSum.setDefectiveItemNum(Integer.parseInt(logData[4].toString()));
            }
            
            try
            {
                inspectionResultSum.setInspectionDate(sdf.parse(logData[0].toString()));
            }
            catch(Exception e)
            {
            }
                        
            if (inspectionResultSum.getInspectedItemNum() > 0 && pointData !=null) {
                Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(inspectionResultSum.getComponentId())).findFirst();//.get();
                MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
                if (circuitBoardPoint != null && circuitBoardPoint.getPoint() >0) {
                    //double itemppm = (((double) sum.getDefectiveItemNum()) / ((double) (sum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
                    //double d = sum.getDefectiveItemNum() / (sum.getInspectedItemNum() * circuitBoardPoint.getPoint());
                    BigDecimal itemppm = new BigDecimal(inspectionResultSum.getDefectiveItemNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
                    //BigDecimal itemppm = BigDecimal.valueOf(d).divide().multiply(1000000);//(sum.getDefectiveItemNum() / (sum.getInspectedItemNum() * circuitBoardPoint.getPoint())))* 1000000;
                    inspectionResultSum.setPpmDefectiveItemNum(itemppm);
                }
            }else{
                if (pointData == null)
                {
                    BigDecimal itemppm = new BigDecimal(inspectionResultSum.getDefectiveItemNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * 1)),10, BigDecimal.ROUND_DOWN).multiply(million);
                    inspectionResultSum.setPpmDefectiveItemNum(itemppm);                    
                }
                else
                {
                inspectionResultSum.setPpmDefectiveItemNum(BigDecimal.ZERO);                    
                }                
            }

            inspectionResultSum.setOperators("");

//            if(sum.getTblCircuitBoardWorkAssignmentCollection()!=null){
//                StringBuilder sb = new StringBuilder();
//                for(TblCircuitBoardWorkAssignment workAssignment : inspectionResultSum.getTblCircuitBoardWorkAssignmentCollection()){
//                    sb.append(workAssignment.getMstUser().getUserName());
//                    sb.append("<br>");
//                }
//                inspectionResultSum.setOperators(sb.toString());
//            }else{
//                 inspectionResultSum.setOperators("");
//            }
            
            if (partNumList != null) {
                String key = inspectionResultSum.getInspectorId() + ","  + inspectionResultSum.getInspectionDateStr();
                if (partNumList.containsKey(key)) {
                    HashMap<String, Integer> defectList = partNumList.get(key);
                    StringBuilder detail = new StringBuilder();
                    for (Map.Entry<String, Integer> entry : defectList.entrySet()) {
                        if(detail.length()!=0){
                            detail.append("<br>");
                        }
                        detail.append(entry.getKey()).append(": ").append(entry.getValue());
                    }
                    inspectionResultSum.setDefectiveItemDetail(detail.toString());
                    int total = defectList.values().stream().mapToInt(i -> i).sum();
                    inspectionResultSum.setDefectivePartNum(total);
                    if (pointData !=null && inspectionResultSum.getDefectivePartNum() != null && inspectionResultSum.getInspectedItemNum() != null && inspectionResultSum.getInspectedItemNum() > 0) {
                        Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(inspectionResultSum.getComponentId())).findFirst();//.get();
                        MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
                        if (circuitBoardPoint != null && circuitBoardPoint.getPoint() > 0) {
                            //double partppm = (((double) inspectionResultSum.getDefectivePartNum()) / ((double) (inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
                            BigDecimal partppm = new BigDecimal(inspectionResultSum.getDefectivePartNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
                            inspectionResultSum.setPpmDefectivePartNum(partppm);
                        }
                    }else{
                        inspectionResultSum.setPpmDefectivePartNum(BigDecimal.ZERO);
                    }
                }else{
                    inspectionResultSum.setDefectivePartNum(0);
                    inspectionResultSum.setPpmDefectivePartNum(BigDecimal.ZERO);
                }
            }
            sumList.add(inspectionResultSum);
        }
        return sumList;
        
//        StringBuilder sql = new StringBuilder(" SELECT DATE_FORMAT(t.inspection_date, '%Y/%m'),t.component_id,component_name,SUM(INSPECTED_ITEM_NUM), SUM(DEFECTIVE_ITEM_NUM) FROM tbl_circuit_board_inspection_result_sum t");
//        sql.append(" LEFT JOIN mst_product_component mstProductComponent ON mstProductComponent.component_Id = t.component_Id");
//        sql.append(" LEFT JOIN mst_product mstProduct ON mstProduct.product_Id = mstProductComponent.product_Id");
//        sql.append(" LEFT JOIN mst_component mstComponent ON t.component_id = mstComponent.id");
//        sql.append(" WHERE 1=1");
//        
//        int paramSet = 0;
//        
//        if (!StringUtils.isEmpty(productCode)) {
//            sql = sql.append(" AND mstProduct.product_Code = ? ");
//            paramSet = 1;
//        } 
//        
//        if (!StringUtils.isEmpty(componentId)) {
//            sql = sql.append(" AND t.component_id = ? ");
//            paramSet += 2;
//        } 
//
//        if (inspectionDateStart != null) {
//            sql = sql.append(" and t.inspection_Date >= ? ");
//            paramSet += 4;
//        }
//
//        if (inspectionDateEnd != null) {
//            sql = sql.append(" and t.inspection_Date <= ? ");
//            paramSet += 8;
//        }
//        sql = sql.append(" GROUP BY DATE_FORMAT(t.inspection_date, '%Y/%m'), t.component_id");
//        Query query = entityManager.createNativeQuery(sql.toString());
//
//        switch (paramSet) {
//            case 1:
//                query.setParameter(1, productCode.trim());
//                break;
//            case 2:
//                query.setParameter(1, componentId.trim());
//                break;
//            case 3:
//                query.setParameter(1, productCode.trim());
//                query.setParameter(2, componentId.trim());
//                break;
//            case 4:
//                query.setParameter(1, inspectionDateStart);
//                break;
//            case 5:
//                query.setParameter(1, productCode.trim());
//                query.setParameter(2, inspectionDateStart);
//             case 6:
//                query.setParameter(1, componentId.trim());
//                query.setParameter(2, inspectionDateStart);               
//                break;
//             case 7:
//                query.setParameter(1, productCode.trim());
//                query.setParameter(2, componentId.trim());
//                query.setParameter(3, inspectionDateStart);
//                break;
//            case 8:
//                query.setParameter(1, inspectionDateEnd);
//                break;
//            case 9:
//                query.setParameter(1, productCode.trim());
//                query.setParameter(2, inspectionDateEnd);
//                break;
//            case 10:
//                query.setParameter(1, componentId.trim());
//                query.setParameter(2, inspectionDateEnd);
//                break;
//            case 11:
//                query.setParameter(1, productCode.trim());
//                query.setParameter(2, componentId.trim());
//                query.setParameter(3, inspectionDateEnd);
//                break;
//            case 13:
//                query.setParameter(1, productCode.trim());
//                query.setParameter(2, inspectionDateStart);
//                query.setParameter(3, inspectionDateEnd);
//                break;
//            case 14:
//                query.setParameter(1, componentId.trim());
//                query.setParameter(2, inspectionDateStart);
//                query.setParameter(3, inspectionDateEnd);
//                break;
//            case 15:
//                query.setParameter(1, productCode.trim());
//                query.setParameter(2, componentId.trim());
//                query.setParameter(3, inspectionDateStart);
//                query.setParameter(4, inspectionDateEnd);
//                break;
//        }
//        
//        if (pageNumber != 0 && pageSize != 0) {
//            Pager pager = new Pager();
//            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
//            query.setMaxResults(pageSize);
//        }
//        
//        List<Object[]> list = query.getResultList();
//        HashMap<String,HashMap<String, Integer>> partNumList = null;
//        CircuitBoardPointData pointData = null;
//        if (list.size() > 0) {
//            partNumList = tblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode, 0, null, inspectionDateStart, inspectionDateEnd, true);
//            pointData = mstCircuitBoardPointService.getCircuitBoardPoints();
//        }
//        BigDecimal million = new BigDecimal(1000000);
//        ArrayList sumList = new ArrayList();
//        for(int i = 0;i < list.size();i++){
//            
//            CircuitBoardInspectionResultSum inspectionResultSum = new CircuitBoardInspectionResultSum();
//            if (list.get(i)[0] != null) {
//                inspectionResultSum.setInspectionDateStr(String.valueOf(list.get(i)[0]));
//            }
//            if (list.get(i)[1] != null) {
//                inspectionResultSum.setComponentId(String.valueOf(list.get(i)[1]));
//            }
//            if (list.get(i)[2] != null) {
//                inspectionResultSum.setComponentName(String.valueOf(list.get(i)[2]));
//            }
//            if (list.get(i)[3] != null) {
//                inspectionResultSum.setInspectedItemNum(Integer.parseInt(String.valueOf(list.get(i)[3])));
//            }
//            if (list.get(i)[4] != null) {
//                inspectionResultSum.setDefectiveItemNum(Integer.parseInt(String.valueOf(list.get(i)[4])));
//            }
//
//            if (inspectionResultSum.getInspectedItemNum() > 0  && pointData !=null) {
//                Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(inspectionResultSum.getComponentId())).findFirst();//.get();
//                MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
//                if (circuitBoardPoint != null && circuitBoardPoint.getPoint() > 0) {
//                    //double itemppm = (((double) inspectionResultSum.getDefectiveItemNum()) / ((double)(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
//                    BigDecimal itemppm = new BigDecimal(inspectionResultSum.getDefectiveItemNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
//                    inspectionResultSum.setPpmDefectiveItemNum(itemppm);
//                }
//            }else{
//                inspectionResultSum.setPpmDefectiveItemNum(BigDecimal.ZERO);
//            }
//
//            if (partNumList != null) {
//                String key = inspectionResultSum.getInspectionDateStr();
//                if (partNumList.containsKey(key)) {
//                    HashMap<String, Integer> defectList = partNumList.get(key);
//                                        StringBuilder detail = new StringBuilder();
//                    for (Entry<String, Integer> entry : defectList.entrySet()) {
//                        if(detail.length()!=0){
//                            detail.append("<br>");
//                        }
//                        detail.append(entry.getKey()).append(": ").append(entry.getValue());
//                    }
//                    inspectionResultSum.setDefectiveItemDetail(detail.toString());
//                    int total = defectList.values().stream().mapToInt(j -> j).sum();
//                    inspectionResultSum.setDefectivePartNum(total);
//                    if (pointData !=null && inspectionResultSum.getDefectivePartNum() != null && inspectionResultSum.getInspectedItemNum() != null && inspectionResultSum.getInspectedItemNum() > 0) {
//                        Optional<MstCircuitBoardPoint> o = pointData.getPointList().stream().filter(id -> id.getComponentId().equalsIgnoreCase(inspectionResultSum.getComponentId())).findFirst();//.get();
//                        MstCircuitBoardPoint circuitBoardPoint = o.orElse(null);
//                        if (circuitBoardPoint != null && circuitBoardPoint.getPoint() > 0) {
//                            //double partppm = (((double) inspectionResultSum.getDefectivePartNum()) / ((double)(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint()))) * 1000000;
//                            BigDecimal partppm = new BigDecimal(inspectionResultSum.getDefectivePartNum()).divide((new BigDecimal(inspectionResultSum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
//                            inspectionResultSum.setPpmDefectivePartNum(partppm);
//                        }
//                    }else{
//                        inspectionResultSum.setPpmDefectivePartNum(BigDecimal.ZERO);
//                    }
//                }
//            }
//            sumList.add(inspectionResultSum);
//        }
//        return sumList;
    }
    
    public FileReponse getInspectionResultSumsCSV(
            String productCode,
            String componentCode,
            Date inspectionDateStart,
            Date inspectionDateEnd,
            LoginUser loginUser
    ) {
        FileReponse response = new FileReponse();

        //CSVファイル出力
        String fileUuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, fileUuid);
        Map<String, String> headerMap = getDictionaryList(langId);

        ArrayList<ArrayList> dataList = new ArrayList<>();
        ArrayList headerList = new ArrayList();
        headerList.add(headerMap.get("inspection_date"));
        headerList.add(headerMap.get("circuit_board_name"));
        headerList.add(headerMap.get("inspected_item_num"));
        headerList.add(headerMap.get("defective_item_num"));
        headerList.add(headerMap.get("ppm_defective_item_num"));
        dataList.add(headerList);
        ArrayList lineList;
        List list = this.searchInspectionResultSumsByCriteria(productCode,componentCode, inspectionDateStart, inspectionDateEnd, 0, 0);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                CircuitBoardInspectionResultSum sum = (CircuitBoardInspectionResultSum) list.get((i));
                lineList = new ArrayList();
                lineList.add(sum.getInspectionDateStr());
                lineList.add(sum.getComponentName());
                lineList.add(String.valueOf(sum.getInspectedItemNum()));
                lineList.add(String.valueOf(sum.getDefectiveItemNum()));
                lineList.add(String.valueOf(sum.getPpmDefectiveItemNum()));
                dataList.add(lineList);
            }
        }

        CSVFileUtil.writeCsv(outCsvPath, dataList);
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(fileUuid);
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        ArrayList<String> keyList = new ArrayList<String>();
        keyList.add("circuit_board_inspection_result_list");
        keyList.add("daily_total");
        HashMap<String, String> nameMap = mstDictionaryService.getDictionaryHashMap(langId, keyList);
        StringBuilder sbFileName = new StringBuilder();
        sbFileName.append((nameMap.get(keyList.get(0)) != null) ? nameMap.get(keyList.get(0)) : keyList.get(0));
        sbFileName.append("_");
        sbFileName.append((nameMap.get(keyList.get(1)) != null) ? nameMap.get(keyList.get(1)) : keyList.get(1));
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(sbFileName.toString()));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        response.setFileUuid(fileUuid);
        return response;
    }
    
        public FileReponse getInspectionMonthlyResultSumsCSV(
            String productCode,
            String componentId,
            Date inspectionDateStart,
            Date inspectionDateEnd,
            LoginUser loginUser
    ) {
        FileReponse response = new FileReponse();

        //CSVファイル出力
        String fileUuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, fileUuid);
        Map<String, String> headerMap = getDictionaryList(langId);
        
        ArrayList<ArrayList> dataList = new ArrayList<>();
        ArrayList headerList = new ArrayList();
        headerList.add(headerMap.get("inspection_month"));
        headerList.add(headerMap.get("circuit_board_name"));
        headerList.add(headerMap.get("inspected_item_num"));
        headerList.add(headerMap.get("defective_item_num"));
        headerList.add(headerMap.get("ppm_defective_item_num"));
        dataList.add(headerList);
        ArrayList lineList;
        List list = this.searchInspectionMonthlyResultSumsByCriteria(productCode,componentId, inspectionDateStart, inspectionDateEnd, 0, 0);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                CircuitBoardInspectionResultSum sum = (CircuitBoardInspectionResultSum) list.get((i));
                lineList = new ArrayList();
                lineList.add(sum.getInspectionDateStr());
                lineList.add(sum.getComponentName());
                lineList.add(String.valueOf(sum.getInspectedItemNum()));
                lineList.add(String.valueOf(sum.getDefectiveItemNum()));
                lineList.add(String.valueOf(sum.getPpmDefectiveItemNum()));
                dataList.add(lineList);
            }
        }

        CSVFileUtil.writeCsv(outCsvPath, dataList);
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(fileUuid);
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add("circuit_board_inspection_result_list");
        keyList.add("monthly_total");
        HashMap<String, String> nameMap = mstDictionaryService.getDictionaryHashMap(langId, keyList);
        StringBuilder sbFileName = new StringBuilder();
        sbFileName.append((nameMap.get(keyList.get(0)) != null) ? nameMap.get(keyList.get(0)) : keyList.get(0));
        sbFileName.append("_");
        sbFileName.append((nameMap.get(keyList.get(1)) != null) ? nameMap.get(keyList.get(1)) : keyList.get(1));
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(sbFileName.toString()));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        response.setFileUuid(fileUuid);
        return response;
    }
    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("inspection_date", "inspector_name","circuit_board_name", "operators", "inspected_item_num", "defective_item_num", "ppm_defective_item_num","defective_part_num","ppm_defective_part_num","defect_detail");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }
    
    public InspectionResult searchWorkAssignments(String resultSumId){
        Query query = entityManager.createNamedQuery("TblCircuitBoardWorkAssignment.findByCircuitBoardInspectionResultSumId");
        query.setParameter("resultSumId", resultSumId);
        
        InspectionResult result = new InspectionResult();
        result.setWorkAssignmentList(query.getResultList());
        return result;
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public InspectionResultGraphData getMonthlyGraphData(String componentCode, Date inspectionDateStart, Date inspectionDateEnd, String langId) {
        InspectionResultGraphData graphData = new InspectionResultGraphData();
        GraphicalItemInfo graphicalItemInfo = new GraphicalItemInfo();
        List<GraphicalAxis> xAxisList = new ArrayList<>();
        List<GraphicalAxis> yAxisList = new ArrayList<>();
        List<GraphicalData> dataList = new ArrayList<>();

        GraphicalAxis xAxis = new GraphicalAxis();
        GraphicalAxis yAxisLeft = new GraphicalAxis();
        //GraphicalAxis yAxisRight = new GraphicalAxis();

        List<String> dataListX = new ArrayList<>();

        //GraphicalData dataLeft = new GraphicalData();
        GraphicalData targetPpm = new GraphicalData();
        GraphicalData ppmPerItem = new GraphicalData();
        GraphicalData ppmPerPart = new GraphicalData();

        List<String> targetPpmList = new ArrayList<>(); // 目標ppm
        List<String> ppmPerItemList = new ArrayList<>(); // 不良率(ppm)/発生個数
        List<String> ppmPerPartList = new ArrayList<>(); // 不良率(ppm)/発生箇所数

        BigDecimal maxDataLeftY =new BigDecimal(0);
        BigDecimal minDataLeftY = new BigDecimal(0);
        //double tickDataLeft;

        //文言
        List<String> dictKeyList = Arrays.asList("circuit_board_inspection_result_list","target","inspection_month", "ppm_defective_item_num", "ppm_defective_part_num");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        List list = searchInspectionMonthlyResultSumsByCriteria(null,componentCode, inspectionDateStart,inspectionDateEnd,0,0);
        CircuitBoardTargetPpmData targetPpmData = this.mstCircuitBoardTargetPpmService.getTargetPpms();
        String componentName = "";
        List<String> rateList = new ArrayList<>();

        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                CircuitBoardInspectionResultSum inspectionResultSum = (CircuitBoardInspectionResultSum) obj;
                componentName = inspectionResultSum.getComponentName();
                dataListX.add(inspectionResultSum.getInspectionDateStr());
//                BigDecimal numcol1 = new BigDecimal(String.valueOf(inspectionResultSum[1]));
                Optional<MstCircuitBoardTargetPpm> o = targetPpmData.getTargetPpmList().stream().filter(t -> FileUtil.dateFormatToMonth(t.getBaseDate()).equals(inspectionResultSum.getInspectionDateStr())).findFirst();
                MstCircuitBoardTargetPpm circuitBoardTargetPpm = o.orElse(null);
             
                targetPpmList.add(String.valueOf(circuitBoardTargetPpm != null?circuitBoardTargetPpm.getTargetPpm():0));// 目標ppm
                ppmPerItemList.add(String.valueOf(inspectionResultSum.getPpmDefectiveItemNum() != null ? inspectionResultSum.getPpmDefectiveItemNum() : 0));// 吸着エラー率
                ppmPerPartList.add(String.valueOf(inspectionResultSum.getPpmDefectivePartNum() != null ? inspectionResultSum.getPpmDefectivePartNum() : 0));// 認識エラー1率

            }
        }
        
        //rateList.addAll(targetPpmList);
        //BigDecimal itemppm = new BigDecimal(sum.getDefectiveItemNum()).divide((new BigDecimal(sum.getInspectedItemNum() * circuitBoardPoint.getPoint())),10, BigDecimal.ROUND_DOWN).multiply(million);
        rateList.addAll(ppmPerItemList);
        rateList.addAll(ppmPerPartList);
        rateList.sort(Comparator.comparing(Double::new));
        if (rateList.size() > 0) {
            //minDataLeftY = 0;//最小値
            maxDataLeftY = new BigDecimal(rateList.get(rateList.size() - 1)).setScale(5);//最大値
        }

        // x軸表示データ
        xAxis.setTicks(dataListX.toArray(new String[0]));
        xAxis.setTitle(headerMap.get("inspection_month"));
        xAxisList.add(xAxis);

        yAxisLeft.setTitle("ppm");
//        yAxisLeft.setMaxTicks(String.valueOf(maxDataLeftY));
//        yAxisLeft.setMinTicks(String.valueOf(minDataLeftY));//y

        BigDecimal maxY = new BigDecimal(1000000);
        BigDecimal minY = new BigDecimal(0);

        if (maxDataLeftY.compareTo(maxY) < 1)
        {
            yAxisLeft.setMaxTicks(String.valueOf(maxY));
        }
        else
        {
            yAxisLeft.setMaxTicks(String.valueOf(maxDataLeftY));            
        }

        if (minDataLeftY.compareTo(minY) < 1)
        {
            yAxisLeft.setMinTicks(String.valueOf(minDataLeftY));//y
        }
        else
        {
            yAxisLeft.setMinTicks(String.valueOf(minY));//y
        }
        
        yAxisList.add(yAxisLeft);
        //yAxisList.add(yAxisRight);

        // グラフデータの設定
        targetPpm.setDataValue(targetPpmList.toArray(new String[0]));
        ppmPerItem.setDataValue(ppmPerItemList.toArray(new String[0]));
        ppmPerPart.setDataValue(ppmPerPartList.toArray(new String[0]));

        // グラフ表示タイプ
        targetPpm.setGraphType("line");
        ppmPerItem.setGraphType("line");
        ppmPerPart.setGraphType("line");

        // グラフデータ名称
        targetPpm.setDataName(headerMap.get("target")  + "ppm");//y
        ppmPerItem.setDataName(headerMap.get("ppm_defective_item_num"));//y
        ppmPerPart.setDataName(headerMap.get("ppm_defective_part_num"));//y

        // y軸表示側
        targetPpm.setYaxisFlg(1);
        ppmPerItem.setYaxisFlg(1);
        ppmPerPart.setYaxisFlg(1);

        dataList.add(targetPpm);
        dataList.add(ppmPerItem);
        dataList.add(ppmPerPart);
        
        graphicalItemInfo.setOptionTitle(componentName + " " + headerMap.get("circuit_board_inspection_result_list"));
        graphicalItemInfo.setxAxisList(xAxisList);
        graphicalItemInfo.setyAxisList(yAxisList);
        graphicalItemInfo.setDataList(dataList);

        graphData.setGraphicalItemInfo(graphicalItemInfo);

        return graphData;
    }
}
