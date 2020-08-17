/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.targetppm;

import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author h.ishihara
 */
@Dependent
public class MstCircuitBoardTargetPpmService {
    @PersistenceContext(unitName = "pu_karte") 
    private EntityManager entityManager;
    
    public CircuitBoardTargetPpmData getTargetPpms(){
        Query query = entityManager.createNamedQuery("MstCircuitBoardTargetPpm.findAll");
        
        CircuitBoardTargetPpmData result = new CircuitBoardTargetPpmData();
        result.setTargetPpmList(query.getResultList());
        return result;
    }
    
    public CircuitBoardTargetPpmData getTargetPpmsByBaseDate(Date baseDate){
        Query query = entityManager.createNamedQuery("MstCircuitBoardTargetPpm.findByBaseDate");
        query.setParameter("baseDate", baseDate);
        CircuitBoardTargetPpmData result = new CircuitBoardTargetPpmData();
        result.setTargetPpmList(query.getResultList());
        return result;
    }
}
