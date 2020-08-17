/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authctrl;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MstAuthCtrlService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    public boolean isAuthorizedAPI(String authId, String apiPath) {
        return false;
    }
    
    public boolean isAuthorizedFunction(String authId, String functionId) {
        Query query = entityManager.createNamedQuery("MstAuthCtrl.findByPK");
        query.setParameter("authId", authId);
        query.setParameter("functionId", functionId);
        try {
            MstAuthCtrl authCtrl = (MstAuthCtrl)query.getSingleResult();
            return authCtrl.getAvailable() == 1;
        }
        catch (NoResultException e) {
            return false;
        }
    }
    
    public MstAuthCtrlList getAuthorizedFunctions(String authId) {
        Query query = entityManager.createNamedQuery("MstAuthCtrl.findByAuthIdAvailable");
        query.setParameter("authId", authId);
        List list = query.getResultList();
        MstAuthCtrlList mstAuthCtrlList = new MstAuthCtrlList();
        mstAuthCtrlList.setMstAuthCtrlList(list);
        return mstAuthCtrlList;
    }
    
    public MstAuthCtrlList getMstAuthCtrls() {
        Query query = entityManager.createNamedQuery("MstAuthCtrl.findAll");
        List list = query.getResultList();
        MstAuthCtrlList mstAuthCtrlList = new MstAuthCtrlList();
        mstAuthCtrlList.setMstAuthCtrlList(list);
        return mstAuthCtrlList;
    }

    @Transactional
    public BasicResponse replaceMstAuthCtrls(MstAuthCtrlList mstAuthCtrlList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        Query query = entityManager.createNamedQuery("MstAuthCtrl.deleteAll");
        query.executeUpdate();
        for (MstAuthCtrl mstAuthCtrl: mstAuthCtrlList.getMstAuthCtrlList()) {
            mstAuthCtrl.setCreateDate(new java.util.Date());
            mstAuthCtrl.setUpdateDate(mstAuthCtrl.getCreateDate());
            mstAuthCtrl.setCreateUserUuid(loginUser.getUserUuid());
            mstAuthCtrl.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(mstAuthCtrl);
        }
        return response;
    }
}
