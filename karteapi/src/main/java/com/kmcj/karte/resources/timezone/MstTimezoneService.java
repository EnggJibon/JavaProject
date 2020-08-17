/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.timezone;

import com.kmcj.karte.constants.CommonConstants;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MstTimezoneService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    public MstTimezoneList getAvailableMstLanguages() {
        Query query = entityManager.createNamedQuery("MstTimezone.findUseOnly");
        List list = query.getResultList();
        MstTimezoneList response = new MstTimezoneList();
        response.setMstTimezones(list);
        return response;
    }

    public MstTimezoneList getAllMstLanguages() {
        Query query = entityManager.createNamedQuery("MstTimezone.findAll");
        List list = query.getResultList();
        MstTimezoneList response = new MstTimezoneList();
        response.setMstTimezones(list);
        return response;
    }
    
    public MstTimezone getByTimezoneName(String timezoneName) {
        Query query = entityManager.createNamedQuery("MstTimezone.findByTimezoneName");
        query.setParameter("timezoneName", timezoneName);
       try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1){
                MstTimezone response = (MstTimezone)list.get(0);
                return response;
            } else {
                return null;
            } 
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public MstTimezone getByIdTimezone(String id) {
        Query query = entityManager.createNamedQuery("MstTimezone.findById");
        query.setParameter("id", id);
       try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1){
                MstTimezone response = (MstTimezone)list.get(0);
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
