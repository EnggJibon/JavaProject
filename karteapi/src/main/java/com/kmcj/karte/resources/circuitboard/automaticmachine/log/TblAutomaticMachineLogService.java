/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.log;

import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.inspection.CircuitBoardInspectionResultSum;
import com.kmcj.karte.resources.circuitboard.inspection.TblCircuitBoardInspectionResultService;
import com.kmcj.karte.resources.circuitboard.inspection.TblCircuitBoardInspectionResultSum;
import com.kmcj.karte.resources.circuitboard.inspection.TblCircuitBoardWorkAssignment;
import com.kmcj.karte.resources.circuitboard.point.CircuitBoardPointData;
import com.kmcj.karte.resources.circuitboard.point.MstCircuitBoardPoint;
import com.kmcj.karte.resources.circuitboard.point.MstCircuitBoardPointService;
import com.kmcj.karte.util.FileUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.persistence.Query;

/**
 *
 * @author h.ishihara
 */
@Dependent
public class TblAutomaticMachineLogService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Transactional
    public ImportResultResponse insertAutomaticMachineLogs(List<AutomaticMachineLog> machineLogList, LoginUser user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ImportResultResponse response = new ImportResultResponse();
        int addedCount = 0;
        int failedCount = 0;
        for (AutomaticMachineLog item : machineLogList) {
            try {
                TblAutomaticMachineLogPK pk = new TblAutomaticMachineLogPK();
                TblAutomaticMachineLog automaticMachineLog = new TblAutomaticMachineLog();
                pk.setMachineUuid(item.getMachineUuid());
                pk.setEventNo(item.getEventNo());
                pk.setCreateDate(item.getCreateDate());

                automaticMachineLog.setTblAutomaticMachineLogPK(pk);
                automaticMachineLog.setLineNumber(item.getLineNumber());
                automaticMachineLog.setMachineType(item.getMachineType());
                automaticMachineLog.setCircuitBoardCode(item.getCircuitBoardCode());
                automaticMachineLog.setSerialNumber(item.getSerialNumber());
                automaticMachineLog.setLogType(item.getLogType());
                
                automaticMachineLog.setTxtcol1(item.getTxtcol1());
                automaticMachineLog.setTxtcol2(item.getTxtcol2());
                automaticMachineLog.setTxtcol3(item.getTxtcol3());
                automaticMachineLog.setTxtcol4(item.getTxtcol4());
                automaticMachineLog.setTxtcol5(item.getTxtcol5());
                automaticMachineLog.setTxtcol6(item.getTxtcol6());
                automaticMachineLog.setTxtcol7(item.getTxtcol7());
                automaticMachineLog.setTxtcol8(item.getTxtcol8());
                automaticMachineLog.setTxtcol9(item.getTxtcol9());
                automaticMachineLog.setTxtcol10(item.getTxtcol10());
                automaticMachineLog.setTxtcol11(item.getTxtcol11());
                automaticMachineLog.setTxtcol12(item.getTxtcol12());
                automaticMachineLog.setTxtcol13(item.getTxtcol13());
                automaticMachineLog.setTxtcol14(item.getTxtcol14());
                automaticMachineLog.setTxtcol15(item.getTxtcol15());

                automaticMachineLog.setNumcol1(item.getNumcol1());
                automaticMachineLog.setNumcol2(item.getNumcol2());
                automaticMachineLog.setNumcol3(item.getNumcol3());
                automaticMachineLog.setNumcol4(item.getNumcol4());
                automaticMachineLog.setNumcol5(item.getNumcol5());
                automaticMachineLog.setNumcol6(item.getNumcol6());
                automaticMachineLog.setNumcol7(item.getNumcol7());
                automaticMachineLog.setNumcol8(item.getNumcol8());
                automaticMachineLog.setNumcol9(item.getNumcol9());
                automaticMachineLog.setNumcol10(item.getNumcol10());
                automaticMachineLog.setNumcol11(item.getNumcol11());
                automaticMachineLog.setNumcol12(item.getNumcol12());
                automaticMachineLog.setNumcol13(item.getNumcol13());
                automaticMachineLog.setNumcol14(item.getNumcol14());
                automaticMachineLog.setNumcol15(item.getNumcol15());

                automaticMachineLog.setDtcol1(item.getDtcol1());
                automaticMachineLog.setDtcol2(item.getDtcol2());
                automaticMachineLog.setDtcol3(item.getDtcol3());
                automaticMachineLog.setDtcol4(item.getDtcol4());
                automaticMachineLog.setDtcol5(item.getDtcol5());
                automaticMachineLog.setDtcol6(item.getDtcol6());
                automaticMachineLog.setDtcol7(item.getDtcol7());
                automaticMachineLog.setDtcol8(item.getDtcol8());
                automaticMachineLog.setDtcol9(item.getDtcol9());
                automaticMachineLog.setDtcol10(item.getDtcol10());

                entityManager.merge(automaticMachineLog);
                addedCount++;
            } catch (Exception ex) {
                failedCount++;
                StringBuilder sb = new StringBuilder();
                sb.append(item.getCircuitBoardCode());
                sb.append(",");
                sb.append(item.getMachineType());
                sb.append(",");
                sb.append(item.getEventNo());
                sb.append(System.getProperty("line.separator"));
                response.setLog(response.getLog() + sb.toString());
                response.setErrorMessage(ex.getMessage());
            }

        }
        response.setSucceededCount(addedCount);
        response.setFailedCount(failedCount);
        return response;
    }

     /**
     * @param productCode
     * @param componentCode
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List searchInspectionResultSumsByCriteria2(
            String productCode,
            String componentCode,
            Date inspectionDateStart,
            Date inspectionDateEnd,
            int pageNumber,
            int pageSize
            ) 
    {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DATE_FORMAT(t.DTCOL1,'%Y/%m/%d'), t.CIRCUIT_BOARD_CODE, ");
        sql.append(" (SELECT p1.ID FROM mst_component p1 WHERE t.CIRCUIT_BOARD_CODE = p1.COMPONENT_CODE) COMP_ID, ");
        sql.append(" COUNT(DISTINCT t.SERIAL_NUMBER) TOTAL_CNT, ");
        sql.append(" (SELECT COUNT(DISTINCT tmp.SERIAL_NUMBER) FROM tbl_automatic_machine_log tmp WHERE tmp.TXTCOL5 = 'NG' AND DATE_FORMAT(tmp.DTCOL1,'%Y/%m/%d') = DATE_FORMAT(t.DTCOL1,'%Y/%m/%d') AND tmp.CIRCUIT_BOARD_CODE = t.CIRCUIT_BOARD_CODE) NG_CNT, ");
        sql.append(" (SELECT pt.PRODUCT_CODE FROM mst_component p, mst_product pt, mst_product_component pc WHERE t.CIRCUIT_BOARD_CODE = p.COMPONENT_CODE and p.ID = pc.COMPONENT_ID AND pc.PRODUCT_ID = pt.PRODUCT_ID) PDT_CODE ");
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
    }

     /**
     * @param productCode
     * @param componentCode
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List searchInspectionMonthlyResultSumsByCriteria2(
            String productCode,
            String componentCode,
            Date inspectionDateStart,
            Date inspectionDateEnd,
            int pageNumber,
            int pageSize
            ) 
    {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DATE_FORMAT(t.DTCOL1,'%Y/%m'), t.CIRCUIT_BOARD_CODE, ");
        sql.append(" (SELECT p1.id FROM mst_component p1 WHERE t.CIRCUIT_BOARD_CODE = p1.COMPONENT_CODE) COMP_ID, ");
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
    }
}


