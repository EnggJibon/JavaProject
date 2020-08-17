/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.thtransport;


import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.material.MstMaterialService;
import com.kmcj.karte.resources.production.detail.TblProductionDetailService;
import com.kmcj.karte.resources.user.MstUserService;


import java.util.*;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;


/**
 *
 * @author m.jibon
 */
@Dependent
public class TblProductionThsetService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
      
    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstMachineService mstMachineService;

    @Inject
    private TblProductionDetailService tblProductionDetailService;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private MstComponentService mstComponentService;
       
    @Inject
    private MstUserService mstUserService;
    
    @Inject
    private MstMaterialService mstMaterialService;
      
    
    public TblThProductionList getStartProduction(LoginUser loginUser){
        
        TblThProductionList response = new TblThProductionList();
        StringBuilder retrieveQuery = new StringBuilder();
        
        retrieveQuery.append(" SELECT prod FROM TblThProduction prod ");
        retrieveQuery.append(" JOIN FETCH prod.mstMachine m ");
        retrieveQuery.append(" WHERE prod.endDatetime IS NULL ");
        retrieveQuery.append(" AND prod.machineUuid IS NOT NULL ");
        retrieveQuery.append(" AND m.sigmaId IS NOT NULL ");
        retrieveQuery.append(" AND m.macKey IS NOT NULL ");
        retrieveQuery.append(" AND NOT EXISTS (SELECT tpt FROM TblProductionThset tpt WHERE prod.id = tpt.productionId) ");
        retrieveQuery.append(" AND EXISTS (SELECT mmfd FROM MstMachineFileDef mmfd WHERE prod.machineUuid = mmfd.machineUuid AND mmfd.hasThreshold = 1) ");
        retrieveQuery.append(" ORDER BY prod.id ");
        Query query = entityManager.createQuery(retrieveQuery.toString());
        List thresholdList = query.getResultList();    
        response.setTblThProductions(thresholdList);
        response.orderedProductionDetailByComponentCode();
        response.formatJsonData();
        return response;
    }
    
    
    public TblThProductionList getEndAndCancelProduction(LoginUser loginUser){     
        TblThProductionList response = new TblThProductionList();
        String sql = "SELECT tblProductionThset FROM TblProductionThset tblProductionThset " +
                " LEFT JOIN FETCH TblThProduction tblProduction ON tblProductionThset.productionId  = tblProduction.id " +
                " WHERE (tblProduction.endDatetime IS NOT NULL AND tblProduction.machineUuid IS NOT NULL AND tblProductionThset.thsetStatus = '1') OR" + // Condition to retrieve ended production record
                " (tblProduction.id IS NULL AND tblProductionThset.thsetStatus = '1')  " + // Condition to retrieve canceled production record               
                " ORDER BY tblProduction.id";  
        Query query = entityManager.createQuery(sql);
        List thresholdList = query.getResultList();
        response.setTblThsetProduction(thresholdList);
        response.orderedProductionThresholdByComponentCode();
        response.formatJsonData(); 
        return response;
   
    }
    
    
    @Transactional
    public void creareThresholdTransport (List<TblProductionThset> tblProductionThsetList, LoginUser loginUser){       
        if (tblProductionThsetList != null){
            for(TblProductionThset productionThsetData : tblProductionThsetList){
                
                TblProductionThset objTblProductionThset = new TblProductionThset();
                                
                objTblProductionThset.setProductionId(productionThsetData.getProductionId());
                objTblProductionThset.setMachineUuid(productionThsetData.getMachineUuid());
                objTblProductionThset.setThsetStatus(productionThsetData.getThsetStatus());
                objTblProductionThset.setCreateUserUuid(loginUser.getUserUuid());
                objTblProductionThset.setCreateDate(new Date());
                
                //Get Production Threshold and Delete Existing Data in tbl_production_thset
                deleteExistingProductionThset(objTblProductionThset.getProductionId());
                
                entityManager.persist(objTblProductionThset);
            }
        }
    }
    
     
    @Transactional
    public void updateTrasportedThreshold(List<TblProductionThset> tblProductionThsetList, LoginUser loginUser){
            
        if (tblProductionThsetList != null){
            for(TblProductionThset productionThsetData : tblProductionThsetList){
             
                TblProductionThset objTblProductionThset = new TblProductionThset();
                StringBuilder updateQuery = new StringBuilder();
                
                //Set Production Data from List.
                objTblProductionThset.setProductionId(productionThsetData.getProductionId());
                objTblProductionThset.setThsetStatus(productionThsetData.getThsetStatus());
                objTblProductionThset.setUpdateUserUuid(loginUser.getUserUuid());
                objTblProductionThset.setUpdateDate(new Date());
                            
                //Create Update Query with Parameter.
                updateQuery.append(" UPDATE TblProductionThset m SET ");
                updateQuery.append(" m.thsetStatus = :thsetStatus, ");
                updateQuery.append(" m.updateDate = :updateDate, ");
                updateQuery.append(" m.updateUserUuid = :updateUserUuid ");
                updateQuery.append(" WHERE m.productionId = :productionId");

                Query finalUpdateQuery = entityManager.createQuery(updateQuery.toString());

                //Pass parameter values.
                finalUpdateQuery.setParameter("thsetStatus", objTblProductionThset.getThsetStatus());
                finalUpdateQuery.setParameter("updateDate", objTblProductionThset.getUpdateDate());
                finalUpdateQuery.setParameter("updateUserUuid", objTblProductionThset.getUpdateUserUuid());
                finalUpdateQuery.setParameter("productionId", objTblProductionThset.getProductionId());

                finalUpdateQuery.executeUpdate(); 
            }
        }
    }
    
    
    @Transactional
    public void deleteExistingProductionThset(String productionId){
    
        StringBuilder deleteQuery = new StringBuilder();
        
        deleteQuery.append( "DELETE FROM " );
        deleteQuery.append( "TblProductionThset tpt " );
        deleteQuery.append( "WHERE tpt.productionId = :productionId" );
        
        Query query = entityManager.createQuery(deleteQuery.toString());
        
        query.setParameter("productionId", productionId);
        
        query.executeUpdate();
        
    }
    
}
