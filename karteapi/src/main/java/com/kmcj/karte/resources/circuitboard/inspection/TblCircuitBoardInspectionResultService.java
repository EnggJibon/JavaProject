/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.inspection.detail.TblCircuitBoardInspectionResultDetail;
import com.kmcj.karte.resources.circuitboard.inspection.detail.TblCircuitBoardInspectionResultDetailPK;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.util.Pager;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author h.ishihara
 */
@Dependent
public class TblCircuitBoardInspectionResultService {
    @PersistenceContext(unitName = "pu_karte") 
    private EntityManager entityManager;
    
    @Transactional
    public void updateInspecttionResult(CircuitBoardInspectionResult result, LoginUser loginUser){
       
       CircuitBoardInspectionResultSum resultSum = result.getResultSum();
       
       TblCircuitBoardInspectionResult tblCircuitBoardInspectionResult = new TblCircuitBoardInspectionResult();
                   Date today =new Date();
       if(null == result.getCircuitBoardInspectionResultId() || "".equals(result.getCircuitBoardInspectionResultId())){
            String resultId = IDGenerator.generate();
            tblCircuitBoardInspectionResult.setCircuitBoardInspectionResultId(resultId);
            result.setCircuitBoardInspectionResultId(resultId);

            tblCircuitBoardInspectionResult.setCreateDate(today);
            tblCircuitBoardInspectionResult.setCreateUserUuid(result.getCreateUserUuid());
            tblCircuitBoardInspectionResult.setUpdateDate(today);
            tblCircuitBoardInspectionResult.setUpdateUserUuid(result.getCreateUserUuid());
       }else{
            tblCircuitBoardInspectionResult.setCircuitBoardInspectionResultId(result.getCircuitBoardInspectionResultId());
            tblCircuitBoardInspectionResult.setUpdateDate(today);
            tblCircuitBoardInspectionResult.setUpdateUserUuid(result.getCreateUserUuid());
       }
       
       tblCircuitBoardInspectionResult.setInspectorId(result.getInspectorId());
       tblCircuitBoardInspectionResult.setInspectionDate(result.getInspectionDate());
       tblCircuitBoardInspectionResult.setInspectionResult(result.getInspectionResult());
       tblCircuitBoardInspectionResult.setProcedureId(result.getProcedureId());
//       MstCircuitBoardSerialNumber serialNumber = new MstCircuitBoardSerialNumber();
//       serialNumber.setSerialNumber(result.getSerialNumber());
//       serialNumber.setCircuitBoardSnId("1");
        
       tblCircuitBoardInspectionResult.setSerialNumber(result.getSerialNumber());//.setSerialNumber(result.getSerialNumber());
//       MstComponent mstComponent = new MstComponent();
//       mstComponent.setId(result.getComponentId());
//       mstComponent.setComponentCode("A7PUH021GT0");
       tblCircuitBoardInspectionResult.setComponentId(result.getComponentId());
       
       entityManager.merge(tblCircuitBoardInspectionResult);
       
       int resultId = result.getInspectionResult();
       int inspectedItemNum = 0;
       int recheckedItemNum = 0;
       int repairedItemNum = 0;
       int defecetiveItemNum = 0;
       int discardedItemNum =0;
       
       Query inspectionResultQuery = entityManager.createNamedQuery("TblCircuitBoardInspectionResult.findByCriteria");
       inspectionResultQuery.setParameter("inspectorId", result.getInspectorId());
       inspectionResultQuery.setParameter("procedureId", result.getProcedureId());
       inspectionResultQuery.setParameter("componentId", result.getComponentId());
       inspectionResultQuery.setParameter("inspectionDate", result.getInspectionDate());
       
       List<TblCircuitBoardInspectionResult> inspectionResultList = inspectionResultQuery.getResultList();
       
       for(TblCircuitBoardInspectionResult inspectionResult : inspectionResultList){
           //確認数加算
           inspectedItemNum += 1;
           
           //不良判定
           int ans1 = inspectionResult.getInspectionResult() & 1;
           if(ans1 > 0){
               defecetiveItemNum = defecetiveItemNum + 1;
           }
           //再確認判定
           int ans2 = inspectionResult.getInspectionResult() & 2;
           if(ans2 > 0){
               recheckedItemNum = recheckedItemNum + 1;
           }
           //修理判定
           int ans4 = inspectionResult.getInspectionResult() & 4;
           if(ans4 > 0){
               repairedItemNum = repairedItemNum + 1;
           }
           //廃棄判定
           int ans8 = inspectionResult.getInspectionResult() & 8;
           if(ans8 > 0){
               discardedItemNum = discardedItemNum + 1;
           }
       }
               
       Query query = entityManager.createNamedQuery("TblCircuitBoardInspectionResultSum.findByCriteria");
       query.setParameter("inspectorId", result.getInspectorId());
       query.setParameter("procedureId", result.getProcedureId());
       query.setParameter("componentId", result.getComponentId());
       query.setParameter("inspectionDate", result.getInspectionDate());
          
       TblCircuitBoardInspectionResultSum tblResultSum = new TblCircuitBoardInspectionResultSum();
       String resultSumId = "";
       List o = query.getResultList();
       if(o != null && o.size() > 0){
           TblCircuitBoardInspectionResultSum sum = (TblCircuitBoardInspectionResultSum) o.get(0);
           resultSumId = sum.getCircuitBoardInspectionResultSumId();
//           inspectedItemNum = sum.getInspectedItemNum() + inspectedItemNum;
//           recheckedItemNum = sum.getRecheckedItemNum() + recheckedItemNum;
//           repairedItemNum = sum.getRepairedItemNum() + repairedItemNum;
//           defecetiveItemNum = sum.getDefectiveItemNum() + defecetiveItemNum;
//           discardedItemNum = sum.getDiscardedItemNum() + discardedItemNum;

           sum.setDiscardedItemNum(discardedItemNum);// resultSum.getDiscardedItemNum());
           sum.setDefectiveItemNum(defecetiveItemNum);// resultSum.getDefectiveItemNum());
           sum.setInspectedItemNum(inspectedItemNum);// resultSum.getInspectedItemNum());
           sum.setRepairedItemNum(repairedItemNum);// resultSum.getRepairedItemNum());
           sum.setRecheckedItemNum(recheckedItemNum);// resultSum.getRecheckedItemNum());
           
           if (null == sum.getCreateUserUuid() || "".equals(sum.getCreateUserUuid()))
           {
               sum.setCreateUserUuid(result.getCreateUserUuid());
               sum.setCreateDate(today);    
           }
           
           sum.setUpdateDate(today);
           sum.setUpdateUserUuid(result.getCreateUserUuid());
                  
           entityManager.merge(sum);
           Query deleteQuery = entityManager.createNamedQuery("TblCircuitBoardWorkAssignment.DeleteByCircuitBoardInspectionResultSumId");
           deleteQuery.setParameter("resultSumId", sum.getCircuitBoardInspectionResultSumId());
           int count = deleteQuery.executeUpdate();

           if (null != result.getWorkAssignmentList()) {
               result.getWorkAssignmentList().forEach((detail) -> {
                   TblCircuitBoardWorkAssignmentPK pk = new TblCircuitBoardWorkAssignmentPK();
                   pk.setCircuitBoardInspectionResultSumId(sum.getCircuitBoardInspectionResultSumId());
                   pk.setUserUuid(detail.getUserUuid());
                   pk.setAssignmentCode(detail.getAssignmentCode());

                   TblCircuitBoardWorkAssignment workAssignment = new TblCircuitBoardWorkAssignment();
                   workAssignment.setTblCircuitBoardWorkAssignmentPK(pk);
                   entityManager.merge(workAssignment);
               });
           }
       }else{      
         //tblResultSum = new TblCircuitBoardInspectionResultSum();
            resultSumId = IDGenerator.generate();
            tblResultSum.setCircuitBoardInspectionResultSumId(resultSumId);
//       if(null == resultSum.getCircuitBoardInspectionResultSumId() || "".equals(resultSum.getCircuitBoardInspectionResultSumId())){
//           String resultSumId = IDGenerator.generate();
//           tblResultSum.setCircuitBoardInspectionResultSumId(resultSumId);
//       }else{
//           tblResultSum.setCircuitBoardInspectionResultSumId(resultSum.getCircuitBoardInspectionResultSumId());
//       }
       
            tblResultSum.setInspectorId(resultSum.getInspectorId());
            tblResultSum.setInspectionDate(resultSum.getInspectionDate());
            //tblResultSum.setInspectionResult(result.getInspectionResult());
            tblResultSum.setProcedureId(resultSum.getProcedureId());
            tblResultSum.setComponentId(resultSum.getComponentId());

            tblResultSum.setDiscardedItemNum(discardedItemNum);// resultSum.getDiscardedItemNum());
            tblResultSum.setDefectiveItemNum(defecetiveItemNum);// resultSum.getDefectiveItemNum());
            tblResultSum.setInspectedItemNum(inspectedItemNum);// resultSum.getInspectedItemNum());
            tblResultSum.setRepairedItemNum(repairedItemNum);// resultSum.getRepairedItemNum());
            tblResultSum.setRecheckedItemNum(recheckedItemNum);// resultSum.getRecheckedItemNum());

           if (null == tblResultSum.getCreateUserUuid() || "".equals(tblResultSum.getCreateUserUuid()))
           {
               tblResultSum.setCreateUserUuid(result.getCreateUserUuid());
               tblResultSum.setCreateDate(today);    
           }
           
           tblResultSum.setUpdateDate(today);
           tblResultSum.setUpdateUserUuid(result.getCreateUserUuid());
        
            entityManager.merge(tblResultSum);
            result.getWorkAssignmentList().forEach((detail) -> {
                            TblCircuitBoardWorkAssignmentPK pk = new TblCircuitBoardWorkAssignmentPK();
                            pk.setCircuitBoardInspectionResultSumId(tblResultSum.getCircuitBoardInspectionResultSumId());
                            pk.setUserUuid(detail.getUserUuid());
                            pk.setAssignmentCode(detail.getAssignmentCode());
                            
                            TblCircuitBoardWorkAssignment workAssignment = new TblCircuitBoardWorkAssignment();
                            workAssignment.setTblCircuitBoardWorkAssignmentPK(pk);                            
                            entityManager.merge(workAssignment);
            });
       }
       result.getResultDetailList().forEach((detail) -> {
                            TblCircuitBoardInspectionResultDetailPK pk = new TblCircuitBoardInspectionResultDetailPK();//detail
                            pk.setCircuitBoardInspectionResultId(tblCircuitBoardInspectionResult.getCircuitBoardInspectionResultId());
                            pk.setCircuitBoardComponentId(detail.getCircuitBoardComponentId());
                            pk.setDefectiveItemId(detail.getDefectiveItemId());
                            
                            TblCircuitBoardInspectionResultDetail resultDetail = new TblCircuitBoardInspectionResultDetail();
                            resultDetail.setTblCircuitBoardInspectionResultDetailPK(pk);
                            resultDetail.setDefectNum(detail.getDefectNum());
                            
                            if (null == resultDetail.getCreateUserUuid() || "".equals(resultDetail.getCreateUserUuid()))
                            {
                                resultDetail.setCreateUserUuid(result.getCreateUserUuid());
                                resultDetail.setCreateDate(today);    
                            }
           
                            resultDetail.setUpdateDate(today);
                            resultDetail.setUpdateUserUuid(result.getCreateUserUuid());
                            
                            entityManager.merge(resultDetail);
       });
       

    }
    
    public InspectionResult getInspectionResultsByCriteria(String inspectorId, String componentId, String procedureId, Date inspectionDate){
       Query query =  entityManager.createNamedQuery("TblCircuitBoardInspectionResult.findByCriteria");
       query.setParameter("inspectorId", inspectorId);
       query.setParameter("componentId", componentId);
       query.setParameter("procedureId", procedureId);
       query.setParameter("inspectionDate", inspectionDate);
       InspectionResult list = new InspectionResult();
       list.setInspectionResultList(query.getResultList());
       
       return list;
    }
    
    
    public InspectionResult searchInspectionResultsByCriteria(String inspectorId, String procedureId, String componentId, Date inspectionDate){
        StringBuilder sql = new StringBuilder(" SELECT t FROM TblCircuitBoardInspectionResult t "
                + " WHERE t.inspectorId = :inspectorId and t.inspectionDate = :inspectionDate ");
        if (null != procedureId && !procedureId.trim().equals("")) {
            sql.append(" AND t.procedureId = :procedureId ");
        }
        
        if (null != componentId && !componentId.trim().equals("")) {
            sql.append(" AND t.componentId = :componentId ");
        }
        Query query =  entityManager.createQuery(sql.toString());
        query.setParameter("inspectorId", inspectorId);
        query.setParameter("inspectionDate", inspectionDate);
        if (null != procedureId && !procedureId.trim().equals("")) {
            query.setParameter("procedureId", procedureId);
        }          
        if (null != componentId && !componentId.trim().equals("")) {
             query.setParameter("componentId", componentId);
        }
                
        InspectionResult list = new InspectionResult();
        list.setInspectionResultList(query.getResultList());
       
       return list;
    }

    public InspectionResult searchInspectionResultsBySerialNumber(String inspectorId, String procedureId, String componentId, Date inspectionDate, String serialNumber){
        StringBuilder sql = new StringBuilder(" SELECT t FROM TblCircuitBoardInspectionResult t "
                + " WHERE t.inspectorId = :inspectorId and t.inspectionDate = :inspectionDate ");
        if (null != procedureId && !procedureId.trim().equals("")) {
            sql.append(" AND t.procedureId = :procedureId ");
        }
        
        if (null != componentId && !componentId.trim().equals("")) {
            sql.append(" AND t.componentId = :componentId ");
        }

        if (null != serialNumber && !serialNumber.trim().equals("")) {
            sql.append(" AND t.serialNumber = :serialNumber ");
        }

        Query query =  entityManager.createQuery(sql.toString());
        query.setParameter("inspectorId", inspectorId);
        query.setParameter("inspectionDate", inspectionDate);
        if (null != procedureId && !procedureId.trim().equals("")) {
            query.setParameter("procedureId", procedureId);
        }          
        if (null != componentId && !componentId.trim().equals("")) {
             query.setParameter("componentId", componentId);
        }
        if (null != serialNumber && !serialNumber.trim().equals("")) {
             query.setParameter("serialNumber", serialNumber);
        }
                
        InspectionResult list = new InspectionResult();
        list.setInspectionResultList(query.getResultList());
       
       return list;
    }
    
    public HashMap<String,HashMap<String, Integer>> searchInspectionResultsByCriteria(String productCode, int productionLineNo, String componentId, Date inspectionDateFrom, Date inspectionDateTo, Boolean... perMonth){
        StringBuilder sql = new StringBuilder(" SELECT t FROM TblCircuitBoardInspectionResult t ");
        sql.append(" LEFT JOIN MstProductComponent mstProductComponent ON mstProductComponent.componentId = t.componentId");
        sql.append(" LEFT JOIN MstProduct mstProduct ON mstProduct.productId = mstProductComponent.productId"); 
        //LEFT JOIN MstProduct mstProduct ON mstProduct.productId = mstProductComponent.productId");
        sql.append(" WHERE 1=1 ");
        if (!StringUtils.isEmpty(productCode)) {
            sql = sql.append(" AND mstProduct.productCode = :productCode ");
        }

        if (productionLineNo != 0) {
            sql = sql.append(" AND t.lineNumber = :productionLineNo ");
        }

        if (!StringUtils.isEmpty(componentId)) {
            sql = sql.append(" AND t.componentId = :componentId ");
        }       

        if (inspectionDateFrom != null) {
            sql = sql.append(" and t.inspectionDate >= :inspectionDateFrom ");
        }

        if (inspectionDateTo != null) {
            sql = sql.append(" and t.inspectionDate <= :inspectionDateTo ");
        }
        Query query =  entityManager.createQuery(sql.toString());
        if (!StringUtils.isEmpty(productCode)) {
            query.setParameter("productCode", productCode.trim());
        }

        if (productionLineNo != 0) {
            query.setParameter("productionLineNo", productionLineNo);
        }

        if (!StringUtils.isEmpty(componentId)) {
            query.setParameter("componentId", componentId.trim());
        }

        if (inspectionDateFrom != null) {
            query.setParameter("inspectionDateFrom", inspectionDateFrom);
        }

        if (inspectionDateTo != null) {
            query.setParameter("inspectionDateTo", inspectionDateTo);
        } 

        List list =query.getResultList();
        //HashMap<String, Integer> detailList = new HashMap<>();
        HashMap<String,HashMap<String, Integer>> detailList = new HashMap<>();
        list.stream().forEach((inspectionResult) -> {
             Collection coll =null;
             HashMap<String, Integer> defectList = null;
             TblCircuitBoardInspectionResult result = (TblCircuitBoardInspectionResult)inspectionResult;
            try{
                coll = result.getTblCircuitBoardInspectionResultDetailCollection();
            }catch(Exception ex){
                String em = ex.getMessage();
            }
            String key = null;
            String defectName = null;
            for (Object o : coll) {
                TblCircuitBoardInspectionResultDetail resultDetail = (TblCircuitBoardInspectionResultDetail) o;
                //TblCircuitBoardInspectionResultDetailPK pk = resultDetail.getTblCircuitBoardInspectionResultDetailPK();
                if (perMonth.length ==1 && perMonth[0] == true) {
                    key = FileUtil.dateFormatToMonth(result.getInspectionDate());
                } else {
                    key = result.getInspectorId() +"," + FileUtil.dateFormat(result.getInspectionDate());
                }
                
                if (!detailList.containsKey(key)) {
                    defectList = new HashMap<>();
                    detailList.put(key, defectList);
                } else {
                    defectList = detailList.get(key);
                }
                if(resultDetail.getMstCircuitBoardDefect()!=null){
                    defectName = resultDetail.getTblCircuitBoardInspectionResultDetailPK().getCircuitBoardComponentId() + " " + resultDetail.getMstCircuitBoardDefect().getDefectName();
                }else{
                    defectName = resultDetail.getTblCircuitBoardInspectionResultDetailPK().getCircuitBoardComponentId() + " " + resultDetail.getTblCircuitBoardInspectionResultDetailPK().getDefectiveItemId();
                }
                if (!defectList.containsKey(key)) {
                    defectList.put(defectName, resultDetail.getDefectNum());
                } else {
                    int defectNum = defectList.get(defectName);
                    defectNum = defectNum + resultDetail.getDefectNum();
                    defectList.put(defectName, defectNum);
                }            
            }

        });
        //List list = query.getResultList();
        //resultlist.setDetailItemList(detailList);
       return detailList;
    }
    
}
