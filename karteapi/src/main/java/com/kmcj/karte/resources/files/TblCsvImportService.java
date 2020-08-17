/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.files;

import com.kmcj.karte.constants.CommonConstants;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author admin
 */
@Dependent
//@Transactional
public class TblCsvImportService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Transactional
    public void createCsvImpor(TblCsvImport tblCsvImport) {
        entityManager.persist(tblCsvImport);
    } 
    
    
    /**
     * ログファイル名を取得する
     * @param logUuid
     * @return 
     */
    public String getLogFileNameByUuid(String logUuid) {
        
        Query query = entityManager.createNamedQuery("TblCsvImport.findByLogFileUuid");
        query.setParameter("logFileUuid", logUuid);
        try {
            TblCsvImport tblCsvImport = (TblCsvImport)query.getSingleResult();
            return tblCsvImport.getLogFileName();
        }
        catch (NoResultException e) {
           return "";
        }
    }  
    
}
