/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect;

import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;

import javax.enterprise.context.Dependent;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bacpd
 */
@Dependent
public class MstCircuitBoardDefectService {
    @PersistenceContext(unitName = "pu_karte")
    private EntityManager entityManager;

    public DefectData getDefects() {
        DefectData data = new DefectData();
        Query query = entityManager.createNamedQuery("MstCircuitBoardDefect.findAll");
        List<MstCircuitBoardDefect> list = query.getResultList();
        data.setDefectList(list);
        return data;
    }

    public MstCircuitBoardDefect checkDefectNameExist(String stringName) {
        TypedQuery<MstCircuitBoardDefect> query = entityManager.createNamedQuery("MstCircuitBoardDefect.findByDefectName", MstCircuitBoardDefect.class);
        query.setParameter("defectName", stringName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public MstCircuitBoardDefect getDefectByNameAndId(String id, String defectName) {
        TypedQuery<MstCircuitBoardDefect> query = entityManager.createNamedQuery("MstCircuitBoardDefect.findByIdAndDefectName", MstCircuitBoardDefect.class);
        query.setParameter("id", id);
        query.setParameter("defectName", defectName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Map<String, String> insertManyDefect(List<MstCircuitBoardDefect> listResponse, LoginUser loginUser) {
        Map<String, String> listUID = new HashMap<>();
        for (MstCircuitBoardDefect defectSubstrate : listResponse) {
            String uId = IDGenerator.generate();
            listUID.put(defectSubstrate.getId(), uId);
            MstCircuitBoardDefect data = new MstCircuitBoardDefect();
            data.setId(uId);
            data.setDefectName(defectSubstrate.getDefectName());
            data.setCreateDate(new java.util.Date());
            data.setUpdateDate(new java.util.Date());
            data.setCreateUserUuid(loginUser.getUserUuid());
            data.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(data);
        }

        return listUID;
    }

    @Transactional
    public void updateManyDefect(List<MstCircuitBoardDefect> listResponse, LoginUser loginUser) {
        for (MstCircuitBoardDefect defectSubstrate : listResponse) {
            Query query = entityManager.createNamedQuery("MstCircuitBoardDefect.updateDefectById");
            query.setParameter("id", defectSubstrate.getId());
            query.setParameter("defectName", defectSubstrate.getDefectName());
            query.setParameter("updateDate", new java.util.Date());
            query.setParameter("updateUserUuid", loginUser.getUserUuid());
            query.executeUpdate();
        }
    }

    @Transactional
    public void deleteDefect(String id) {
        Query query = entityManager.createNamedQuery("MstCircuitBoardDefect.delete");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
