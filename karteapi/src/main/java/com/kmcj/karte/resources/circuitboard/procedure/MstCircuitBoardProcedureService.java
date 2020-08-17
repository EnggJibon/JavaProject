package com.kmcj.karte.resources.circuitboard.procedure;

import com.kmcj.karte.constants.CommonConstants;
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
public class MstCircuitBoardProcedureService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    public MstCircuitBoardProcedureList getMstCircuitBoardProcedureSubstrate() {
        MstCircuitBoardProcedureList dataList = new MstCircuitBoardProcedureList();
        TypedQuery<MstCircuitBoardProcedure> query = entityManager.createNamedQuery("MstCircuitBoardProcedure.findAll", MstCircuitBoardProcedure.class);
        List<MstCircuitBoardProcedure> list = query.getResultList();
        dataList.setMstCircuitBoardProcedureList(list);
        return dataList;
    }

    public List<MstCircuitBoardProcedure> getMstCircuitBoardProcedure() {
        TypedQuery<MstCircuitBoardProcedure> query = entityManager.createNamedQuery("MstCircuitBoardProcedure.findAll", MstCircuitBoardProcedure.class);
        return query.getResultList();
    }

    public ProcedureData getProceduresForDefectListDisplay() {
        ProcedureData data = new ProcedureData();
        TypedQuery<MstCircuitBoardProcedure> query = entityManager.createNamedQuery("MstCircuitBoardProcedure.findForDefectListDisplay", MstCircuitBoardProcedure.class);
        List<MstCircuitBoardProcedure> list = query.getResultList();
        data.setProcedureList(list);
        return data;
    }

    public ProcedureData getProceduresForForManhourDisplay() {
        ProcedureData data = new ProcedureData();
        TypedQuery<MstCircuitBoardProcedure> query = entityManager.createNamedQuery("MstCircuitBoardProcedure.findForManhourDisplayFlg", MstCircuitBoardProcedure.class);
        List<MstCircuitBoardProcedure> list = query.getResultList();
        data.setProcedureList(list);
        return data;
    }

    public MstCircuitBoardProcedure checkProcedureNameExist(String stringName) {
        TypedQuery<MstCircuitBoardProcedure> query = entityManager.createNamedQuery("MstCircuitBoardProcedure.findByProcedureName", MstCircuitBoardProcedure.class);
        query.setParameter("procedureName", stringName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public MstCircuitBoardProcedure getProcedureByNameAndId(String id, String procedureName) {
        TypedQuery<MstCircuitBoardProcedure> query = entityManager.createNamedQuery("MstCircuitBoardProcedure.findByIdAndProcedureName",
                MstCircuitBoardProcedure.class);
        query.setParameter("id", id);
        query.setParameter("procedureName", procedureName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public MstCircuitBoardProcedure getEnabledProcedureData(String code, int isDefect, int isManhour) {
        TypedQuery<MstCircuitBoardProcedure> query = entityManager.createNamedQuery("MstCircuitBoardProcedure.findByEnabledProcedureData", MstCircuitBoardProcedure.class);
        query.setParameter("procedureName", code);
        query.setParameter("isDefect", isDefect);
        query.setParameter("isManHour", isManhour);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Transactional
    public Map<String, String> insertManyProcedure(List<MstCircuitBoardProcedure> listResponse, LoginUser loginUser) {
        Map<String, String> listUID = new HashMap<>();
        for (MstCircuitBoardProcedure procedure : listResponse) {
            String uId = IDGenerator.generate();
            listUID.put(procedure.getId(), uId);
            MstCircuitBoardProcedure data = new MstCircuitBoardProcedure();
            data.setId(uId);
            data.setProcedureName(procedure.getProcedureName());
            data.setDisplayFlg(procedure.getDisplayFlg());
            data.setDefectListDisplayFlg(procedure.getDefectListDisplayFlg());
            data.setManhourDisplayFlg(procedure.getManhourDisplayFlg());
            data.setCreateDate(new java.util.Date());
            data.setUpdateDate(new java.util.Date());
            data.setCreateUserUuid(loginUser.getUserUuid());
            data.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(data);
        }

        return listUID;
    }

    @Transactional
    public void updateManyProcedure(List<MstCircuitBoardProcedure> listResponse, LoginUser loginUser) {
        for (MstCircuitBoardProcedure procedure : listResponse) {
            Query query = entityManager.createNamedQuery("MstCircuitBoardProcedure.updateProcedureById");
            query.setParameter("id", procedure.getId());
            query.setParameter("procedureName", procedure.getProcedureName());
            query.setParameter("displayFlg", procedure.getDisplayFlg());
            query.setParameter("defectListDisplayFlg", procedure.getDefectListDisplayFlg());
            query.setParameter("manhourDisplayFlg", procedure.getManhourDisplayFlg());
            query.setParameter("updateDate", new java.util.Date());
            query.setParameter("updateUserUuid", loginUser.getUserUuid());
            query.executeUpdate();
        }
    }

    @Transactional
    public void deleteProcedure(String id) {
        Query query = entityManager.createNamedQuery("MstCircuitBoardProcedure.delete");
        query.setParameter("id", id);
        query.executeUpdate();
    }

}
