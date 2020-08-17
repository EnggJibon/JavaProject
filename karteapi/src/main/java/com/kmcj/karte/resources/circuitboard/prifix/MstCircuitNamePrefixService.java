/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.prifix;

import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.persistence.*;
import javax.transaction.Transactional;

/**
 *
 * @author bacpd
 */
@Dependent
public class MstCircuitNamePrefixService {
    @PersistenceContext(unitName = "pu_karte") 
    private EntityManager entityManager;
    
    public PrefixData getAllCircuitNamePrefix() {
        PrefixData data = new PrefixData();
        Query query = entityManager.createNamedQuery("MstCircuitNamePrefix.findAll");
        List<MstCircuitNamePrefix> list = query.getResultList();
        data.setPrefixList(list);
        return data;
    }

    public PrefixData getCircuitNamePrefixForDisplay(){
        PrefixData data = new PrefixData();
        Query query = entityManager.createNamedQuery("MstCircuitNamePrefix.findForDisplay");
        List<MstCircuitNamePrefix> list = query.getResultList();
        data.setPrefixList(list);
        return data;
    }

    public MstCircuitNamePrefix checkPrefixExists(String stringName) {
        TypedQuery<MstCircuitNamePrefix> query = entityManager.createNamedQuery("MstCircuitNamePrefix.findByPrefix", MstCircuitNamePrefix.class);
        query.setParameter("prefix", stringName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public MstCircuitNamePrefix getPrefixByNameAndId(String id, String prefixName) {
        TypedQuery<MstCircuitNamePrefix> query = entityManager.createNamedQuery("MstCircuitNamePrefix.findByIdAndPrefixName", MstCircuitNamePrefix.class);
        query.setParameter("id", id);
        query.setParameter("prefix", prefixName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public MstCircuitNamePrefix checkPrefixIdExists(String stringId) {
        TypedQuery<MstCircuitNamePrefix> query = entityManager.createNamedQuery("MstCircuitNamePrefix.findById", MstCircuitNamePrefix.class);
        query.setParameter("id", stringId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Map<String, String> insertManyPrefix(List<MstCircuitNamePrefix> listResponse, LoginUser loginUser) {
        Map<String, String> listUID = new HashMap<>();
        for (MstCircuitNamePrefix itemPrefix : listResponse) {
            listUID.put(itemPrefix.getId(), itemPrefix.getPrefixId());
            MstCircuitNamePrefix data = new MstCircuitNamePrefix();
            data.setId(itemPrefix.getPrefixId());
            data.setPrefix(itemPrefix.getPrefix());
            data.setDisplayFlg(itemPrefix.getDisplayFlg());
            data.setCreateDate(new java.util.Date());
            data.setUpdateDate(new java.util.Date());
            data.setCreateUserUuid(loginUser.getUserUuid());
            data.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(data);
        }

        return listUID;
    }

    @Transactional
    public void updateManyPrefix(List<MstCircuitNamePrefixUpdate> listResponse, LoginUser loginUser) {
        for (MstCircuitNamePrefixUpdate itemPrefix : listResponse) {
            Query query = entityManager.createNamedQuery("MstCircuitNamePrefix.updatePrefixById");
            query.setParameter("id", itemPrefix.getPrefixId()); //this is new id after update
            query.setParameter("oldId", itemPrefix.getId()); //this is old id before update
            query.setParameter("prefix", itemPrefix.getPrefix());
            query.setParameter("displayFlg", itemPrefix.getDisplayFlg());
            query.setParameter("updateDate", new java.util.Date());
            query.setParameter("updateUserUuid", loginUser.getUserUuid());
            query.executeUpdate();
        }
    }

    @Transactional
    public void deletePrefixById(String id) {
        Query query = entityManager.createNamedQuery("MstCircuitNamePrefix.delete");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
