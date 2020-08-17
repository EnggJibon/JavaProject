/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.language;

import com.kmcj.karte.constants.CommonConstants;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MstLanguageService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    public MstLanguageList getMstLanguages() {
        Query query = entityManager.createNamedQuery("MstLanguage.findAll");
        List list = query.getResultList();
        MstLanguageList response = new MstLanguageList();
        response.setMstLanguages(list);
        return response;
    }
    
    public MstLanguage getDefaultLanguage() {
        Query query = entityManager.createNamedQuery("MstLanguage.findDefault");
        try {
            MstLanguage mstLanguage = (MstLanguage)query.getSingleResult();
            return mstLanguage;
        }
        catch (NoResultException e) {
            return null;
        }
    }
    
    public MstLanguage getByLangLanguage(String lang) {
        Query query = entityManager.createNamedQuery("MstLanguage.findByLang");
        query.setParameter("lang", lang);
        try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1){
                MstLanguage response = (MstLanguage)list.get(0);
                return response;
            } else {
                return null;
            } 
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public MstLanguage getByIDLanguage(String id) {
        Query query = entityManager.createNamedQuery("MstLanguage.findById");
        query.setParameter("id", id);
        try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1){
                MstLanguage response = (MstLanguage)list.get(0);
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
