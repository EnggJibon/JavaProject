/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.machine.proc.cond;

//import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.util.IDGenerator;
import javax.enterprise.context.Dependent;
//import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class TblProductionMachineProcCondService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    //@Inject
    //private MstDictionaryService mstDictionaryService;
    
    @Transactional
    public void createProductionMachineProcCond(TblProductionMachineProcCond productionMachineProcCond) {
        if (productionMachineProcCond.getId() == null) {
            productionMachineProcCond.setId(IDGenerator.generate());
        }
        entityManager.persist(productionMachineProcCond);
    }
    
    @Transactional
    public void updateProductionMachineProcCond(TblProductionMachineProcCond productionMachineProcCond) {
        entityManager.merge(productionMachineProcCond);
    }
    
    @Transactional
    public void deleteProductionMachineProcCondByDetailId(String productionDetailId) {
        Query query = entityManager.createNamedQuery("TblProductionMachineProcCond.deleteByDetailId");
        query.setParameter("productionDetailId", productionDetailId);
        query.executeUpdate();
    }
    
}
