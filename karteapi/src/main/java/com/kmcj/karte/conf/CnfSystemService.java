/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.conf;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionary;
import com.kmcj.karte.resources.dictionary.MstDictionaryList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
//import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
//@Transactional
public class CnfSystemService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    public CnfSystemService() {
    }
    
    public CnfSystem findByKey(String configGroup, String configKey) {
        Query query = entityManager.createNamedQuery("CnfSystem.findByConfigKey");
        query.setParameter("configGroup", configGroup);
        query.setParameter("configKey", configKey);
        CnfSystem cnf = null;
        try {
            cnf = (CnfSystem)query.getSingleResult();
        }
        catch (NoResultException e) {}
        return cnf;
    }
    
    public CnfSystemList getAllCnfSystem(LoginUser loginUser) {
        String langId = loginUser.getLangId();
        TypedQuery<CnfSystem> query = entityManager.createQuery("SELECT c FROM CnfSystem c "
                + "ORDER BY c.seq", CnfSystem.class);
        List<CnfSystem> cnfSystems = query.getResultList();
        
        Set<String> dictKeySet = new HashSet<>();
        for(CnfSystem cs : cnfSystems){
            dictKeySet.add(cs.getDictKey());
            dictKeySet.add(cs.getGroupDictKey());
        }
        HashMap<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, new ArrayList<>(dictKeySet));
        for(CnfSystem cs : cnfSystems){
            cs.setDictVal(dictMap.get(cs.getDictKey()));
            cs.setGroupDictVal(dictMap.get(cs.getGroupDictKey()));
        }
        CnfSystemList ret = new CnfSystemList();
        ret.setCnfSystems(cnfSystems);
        return ret;
        
    }
    
    @Transactional
    public void updateCnfSystemValues(CnfSystemList cnfSystemList, LoginUser loginUser) {
        java.util.Date updateDate = new java.util.Date();
        Query query = entityManager.createNamedQuery("CnfSystem.update");
        for (CnfSystem cnfSystem: cnfSystemList.getCnfSystems()) {
            if (cnfSystem.isModified()) {
                query.setParameter("configValue", cnfSystem.getConfigValue());
                query.setParameter("configGroup", cnfSystem.cnfSystemPK.getConfigGroup());
                query.setParameter("configKey", cnfSystem.cnfSystemPK.getConfigKey());
                query.setParameter("updateDate", updateDate);
                query.setParameter("updateUserUuid", loginUser.getUserUuid());
                query.executeUpdate();
            }
        }
        
    }

}
