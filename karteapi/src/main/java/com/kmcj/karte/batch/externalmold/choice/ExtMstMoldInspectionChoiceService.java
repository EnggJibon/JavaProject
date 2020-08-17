/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmold.choice;

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
public class ExtMstMoldInspectionChoiceService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    public List<ExtMstMoldInspectionChoice> getExtMstMoldInspectionChoiceByItemId(String inspectionItemId) {
        Query query = entityManager.createNamedQuery("ExtMstMoldInspectionChoice.findByInspectionItemId");
        query.setParameter("inspectionItemId", inspectionItemId);
        return query.getResultList();
    }
}
