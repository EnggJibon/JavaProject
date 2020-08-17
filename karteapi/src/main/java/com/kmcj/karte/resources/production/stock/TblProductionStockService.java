/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.stock;

import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
@Dependent
public class TblProductionStockService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private CnfSystemService cnfSystemService;

    /**
     *
     * @return
     */
    public TblProductionStockVoList getPproductionStockVoList() {
        return new TblProductionStockVoList();
    }

}
