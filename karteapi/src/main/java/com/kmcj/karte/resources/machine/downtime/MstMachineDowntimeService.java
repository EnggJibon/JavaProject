/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.downtime;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MstMachineDowntimeService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;


    public MstMachineDowntimeList getMstMachineDowntimeList() {
        Query query =  entityManager.createNamedQuery("MstMachineDowntime.findAll");
        List list = query.getResultList();
        MstMachineDowntimeList response = new MstMachineDowntimeList();
        response.setMstMachineDowntimes(list);
        return response;
    }
    
    public MstMachineDowntime getMstMachineDowntimeById(String id) {
        Query query = entityManager.createNamedQuery("MstMachineDowntime.findById");
        query.setParameter("id", id);
        List list = query.getResultList();
        if (list.size() > 0) {
            return (MstMachineDowntime)list.get(0);
        }
        else {
            return null;
        }
    }
    
    public MstMachineDowntime getMstMachineDowntimeByCode(String downtimeCode) {
        Query query = entityManager.createNamedQuery("MstMachineDowntime.findByDowntimeCode");
        query.setParameter("downtimeCode", downtimeCode);
        List list = query.getResultList();
        if (list.size() > 0) {
            return (MstMachineDowntime)list.get(0);
        }
        else {
            return null;
        }
    }

    @Transactional
    public void updateMstMachineDowntime(MstMachineDowntimeList mstMachineDowntimeList, LoginUser loginUser) {
        for (MstMachineDowntime downtime : mstMachineDowntimeList.getMstMachineDowntimes()) {
            if (downtime.isDeleted()) {
                Query query = entityManager.createNamedQuery("MstMachineDowntime.deleteByKey");
                query.setParameter("id", downtime.getId());
                query.executeUpdate();
            }
            else if (downtime.isAdded()) {
                downtime.setId(IDGenerator.generate());
                downtime.setCreateUserUuid(loginUser.getUserUuid());
                downtime.setCreateDate(new java.util.Date());
                downtime.setUpdateUserUuid(loginUser.getUserUuid());
                downtime.setUpdateDate(downtime.getCreateDate());
                entityManager.persist(downtime);
            }
            else if (downtime.isModified()) {
                downtime.setUpdateUserUuid(loginUser.getUserUuid());
                downtime.setUpdateDate(new java.util.Date());
                entityManager.merge(downtime);
            }
        }
    }
}
