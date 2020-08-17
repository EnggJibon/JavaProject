/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.part;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


@Dependent
//@Transactional
public class TblMoldMaintenancePartService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     *
     * @param inTblMoldMaintenancePart
     * @param loginUser
     */
    @Transactional
    public void createTblMoldMaintenancePart(TblMoldMaintenancePart inTblMoldMaintenancePart, LoginUser loginUser) {

        TblMoldMaintenancePart tblMoldMaintenancePart = new TblMoldMaintenancePart();
        TblMoldMaintenancePartPK tblMoldMaintenancePartPK = new TblMoldMaintenancePartPK();
        tblMoldMaintenancePartPK.setMaintenanceId(inTblMoldMaintenancePart.getTblMoldMaintenancePartPK().getMaintenanceId());
        tblMoldMaintenancePartPK.setMoldPartRelId(inTblMoldMaintenancePart.getTblMoldMaintenancePartPK().getMoldPartRelId());
        tblMoldMaintenancePart.setMoldMaintenancePartPK(tblMoldMaintenancePartPK);
        tblMoldMaintenancePart.setReplaceOrRepair(inTblMoldMaintenancePart.getReplaceOrRepair());
        tblMoldMaintenancePart.setShotCntAtManit(inTblMoldMaintenancePart.getShotCntAtManit());

        tblMoldMaintenancePart.setCreateDate(new java.util.Date());
        tblMoldMaintenancePart.setCreateUserUuid(loginUser.getUserUuid());
        tblMoldMaintenancePart.setUpdateDate(new java.util.Date());
        tblMoldMaintenancePart.setUpdateUserUuid(loginUser.getUserUuid());

        entityManager.persist(tblMoldMaintenancePart);
    }
    
    public List<TblMoldMaintenancePart> getMaintPartsInMoldMaint(String moldMaintId) {
        return entityManager.createNamedQuery("TblMoldMaintenancePart.findByMainte", TblMoldMaintenancePart.class)
            .setParameter("maintenanceId", moldMaintId).getResultList();
    }
}
