/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authorization;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MstAuthService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    public MstAuthList getMstAuths() {
        Query query = entityManager.createNamedQuery("MstAuth.findAll");
        List list = query.getResultList();
        MstAuthList response = new MstAuthList();
        response.setMstAuths(list);
        return response;
    }
    
    public boolean checkCanDelete(MstAuthList mstAuthList) {
        for (MstAuth mstAuth: mstAuthList.getMstAuths()) {
            if (mstAuth.isDeleted()) {
                //MST_USER使用チェック
                Query query = entityManager.createNamedQuery("MstUser.countByAuthId");
                query.setParameter("authId", mstAuth.getId());
                Long cnt = (Long)query.getSingleResult();
                if (cnt > 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Transactional
//    public BasicResponse updateMstAuth(MstAuthList mstAuthList, LoginUser loginUser) {
    public MstAuthList updateMstAuth(MstAuthList mstAuthList, LoginUser loginUser)  {
        //BasicResponse response = new BasicResponse();
        for (MstAuth mstAuth: mstAuthList.getMstAuths()) {
            if (mstAuth.isAdded()) {
                mstAuth.setId(IDGenerator.generate());
                mstAuth.setCreateDate(new java.util.Date());
                mstAuth.setCreateUserUuid(loginUser.getUserUuid());
                mstAuth.setUpdateDate(mstAuth.getCreateDate());
                mstAuth.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(mstAuth);
            }
            else if (mstAuth.isDeleted()) {
                if (mstAuth.getId() != null && !mstAuth.getId().equals("")) {
                    Query query = entityManager.createNamedQuery("MstAuth.delete");
                    query.setParameter("id", mstAuth.getId());
                    query.executeUpdate();
                }
            }
            else if (mstAuth.isModified()) {
                if (mstAuth.getId() != null && !mstAuth.getId().equals("")) {
                    Query query = entityManager.createNamedQuery("MstAuth.update");
                    query.setParameter("id", mstAuth.getId());
                    query.setParameter("groupName", mstAuth.getGroupName());
                    query.setParameter("seq", mstAuth.getSeq());
                    query.setParameter("systemDefault", mstAuth.getSystemDefault());
                    query.setParameter("updateDate", new java.util.Date());
                    query.setParameter("updateUserUuid", loginUser.getUserUuid());
                    query.executeUpdate();
                }
            }
        }
        //return response;
        return mstAuthList;
    }
    
    public MstAuth getByGroupNameAuths(String authGroup) {
        Query query = entityManager.createNamedQuery("MstAuth.findByGroupName");
        query.setParameter("groupName", authGroup);
        try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1){
                MstAuth response = (MstAuth)list.get(0);
                return response;
            } else {
                return null;
            } 
        }
        catch (NoResultException e) {
            return null;
        }
    }
    
    public MstAuth getByIDAuths(String id) {
        Query query = entityManager.createNamedQuery("MstAuth.findById");
        query.setParameter("id", id);
        try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1){
                MstAuth response = (MstAuth)list.get(0);
                return response;
            } else {
                return null;
            } 
        }
        catch (NoResultException e) {
            return null;
        }
    }

    
}
