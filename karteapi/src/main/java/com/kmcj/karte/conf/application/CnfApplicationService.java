
package com.kmcj.karte.conf.application;

import com.kmcj.karte.constants.CommonConstants;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author m.jibon
 */
@Dependent
public class CnfApplicationService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    public CnfApplicationService() {
    }
    public CnfApplication findByKey(String configKey) {
        Query query = entityManager.createNamedQuery("CnfApplication.findByConfigKey");
        query.setParameter("configKey", configKey);
        CnfApplication cnfApp = null;
        try {
            cnfApp = (CnfApplication)query.getSingleResult();
        }
        catch (NoResultException e) {}
        return cnfApp;
    }
    
}
