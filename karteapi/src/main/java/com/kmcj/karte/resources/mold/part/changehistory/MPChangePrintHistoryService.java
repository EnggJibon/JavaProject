/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.changehistory;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;
import java.util.Date;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author t.takasaki
 */
@Dependent
public class MPChangePrintHistoryService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    public Optional<TblMoldPartChangePrintHistory> getLastHistory(int department) {
        return entityManager.createNamedQuery("TblMoldPartChangePrintHistory.findLastByDepartment", TblMoldPartChangePrintHistory.class)
            .setParameter("department", department).setMaxResults(1).getResultList().stream().findFirst();
    }
    
    @Transactional
    public void addHistory(int department, Date startDate, Date endDate, LoginUser loginUser) {
        TblMoldPartChangePrintHistory history = new TblMoldPartChangePrintHistory(IDGenerator.generate(), department, startDate, endDate);
        Date curr = new Date();
        history.setCreateDate(curr);
        history.setCreateUserUuid(loginUser.getUserUuid());
        history.setUpdateDate(curr);
        history.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(history);
    }
}
