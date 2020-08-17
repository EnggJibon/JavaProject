/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.point;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author h.ishihara
 */
@Dependent
public class MstCircuitBoardPointService {
    @PersistenceContext(unitName = "pu_karte") 
    private EntityManager entityManager;
    
    public CircuitBoardPointData getCircuitBoardPoints(){
        Query query = entityManager.createNamedQuery("MstCircuitBoardPoint.findAll");
        
        CircuitBoardPointData result = new CircuitBoardPointData();
        result.setPointList(query.getResultList());
        return result;
    }
}
