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

@Dependent
//@Transactional
public class TblCsvExportService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Transactional
    public void createTblCsvExport(TblCsvExport tblCsvExport) {
        entityManager.persist(tblCsvExport);
    }    
    
    /**
     * 
     * @param uuid
     * @return 
     */
    public String getTblCsvExportFileNameByUuid(String uuid) {
        
        Query query = entityManager.createNamedQuery("TblCsvExport.findByFileUuid");
        query.setParameter("fileUuid", uuid);
        try {
            TblCsvExport tblUploadFile = (TblCsvExport)query.getSingleResult();
            return tblUploadFile.getClientFileName();
        }
        catch (NoResultException e) {
           return "";
        }
    }  
    
}
