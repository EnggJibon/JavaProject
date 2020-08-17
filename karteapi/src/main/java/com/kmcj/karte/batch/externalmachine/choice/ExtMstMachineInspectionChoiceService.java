/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmachine.choice;

import com.kmcj.karte.constants.CommonConstants;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author
 */
@Dependent
public class ExtMstMachineInspectionChoiceService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    public List<ExtMstMachineInspectionChoice> getExtMstMachineInspectionChoiceByItemId(String inspectionItemId) {
        Query query = entityManager.createNamedQuery("ExtMstMachineInspectionChoice.findByInspectionItemId");
        query.setParameter("inspectionItemId", inspectionItemId);
        return query.getResultList();
    }
}
