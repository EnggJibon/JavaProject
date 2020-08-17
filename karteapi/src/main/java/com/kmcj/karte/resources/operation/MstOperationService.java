/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation;

import com.kmcj.karte.constants.CommonConstants;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kmc
 */
@Dependent
public class MstOperationService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    public MstOperationList getMstOperationAll() {
        Query query = entityManager.createNamedQuery("MstOperation.findAll");
        List list = query.getResultList();
        MstOperationList mstOperationList = new MstOperationList();
        mstOperationList.setMstOperationList(list);
        return mstOperationList;
    }

}
