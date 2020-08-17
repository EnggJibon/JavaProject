package com.kmcj.karte.resources.circuitboard.productionline.machine;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultService;
import com.kmcj.karte.util.IDGenerator;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xiaozhou.wang on 2017/08/09.
 * Updated by MinhDTB
 */
@Dependent
public class MstProductionLineMachineService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    MstProductionLineMachineList getMstProductionLineMachineList(String productionLineId) {
        MstProductionLineMachineList response = new MstProductionLineMachineList();
        response.setMstProductionLineMachines(getMstProductionLineMachines(productionLineId));
        return response;
    }

    public List<MstProductionLineMachine> getMstProductionLineMachines(String productionLineId) {
        TypedQuery<MstProductionLineMachine> query =
                entityManager.createNamedQuery("MstProductionLineMachine.findByProductionLineId", MstProductionLineMachine.class);
        query.setParameter("productionLineId", productionLineId);
        return query.getResultList();
    }


    @Transactional
    private void addProductionLineMachines(String productionLineId, MstProductionLineMachine mstProductionLineMachine, LoginUser loginUser) {
        try {
            mstProductionLineMachine.setProductionLineMachineId(IDGenerator.generate());
            mstProductionLineMachine.setProductionLineId(productionLineId);
            mstProductionLineMachine.setCreateDate(new Date());
            mstProductionLineMachine.setCreateUserUuid(loginUser.getUserUuid());
            mstProductionLineMachine.setUpdateDate(new Date());
            mstProductionLineMachine.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(mstProductionLineMachine);
        } catch (Exception e) {
            Logger.getLogger(MstProductionLineMachineService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Transactional
    public void deleteAllByProductionLineId(String productionLineId) {
        try {
            Query query = entityManager.createNamedQuery("MstProductionLineMachine.deleteByProductionLineId");
            query.setParameter("productionLineId", productionLineId);
            query.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger(MstProductionLineMachineService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Transactional
    public void updateMstProductionLineMachines(String productionLineId,
                                                List<MstProductionLineMachine> mstProductionLineMachines, LoginUser loginUser) {
        try {            
            deleteAllByProductionLineId(productionLineId);
            
            for (MstProductionLineMachine mstProductionLineMachine : mstProductionLineMachines) {
                addProductionLineMachines(productionLineId, mstProductionLineMachine, loginUser);
            }
        } catch(Exception e) {
            Logger.getLogger(MstProductionLineMachineService.class.getName()).log(Level.SEVERE, null, e);
        }                       
    }
}
