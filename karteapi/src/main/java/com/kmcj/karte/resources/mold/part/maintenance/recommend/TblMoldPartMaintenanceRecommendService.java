/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.maintenance.recommend;

import com.kmcj.karte.constants.CommonConstants;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Dependent
public class TblMoldPartMaintenanceRecommendService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     *
     * @param moldPartRelId
     * @return
     */
    public TblMoldPartMaintenanceRecommend getTblMoldPartMaintenanceRecommendByPartRelId(String moldPartRelId) {
        Query query = entityManager.createNamedQuery("TblMoldPartMaintenanceRecommend.findByMoldPartRelId");
        query.setParameter("moldPartRelId", moldPartRelId);
        TblMoldPartMaintenanceRecommend tblMoldPartMaintenanceRecommend = null;
        try {
            tblMoldPartMaintenanceRecommend = (TblMoldPartMaintenanceRecommend) query.getSingleResult();
            return tblMoldPartMaintenanceRecommend;
        } catch (NoResultException e) {
            return tblMoldPartMaintenanceRecommend;
        }
    }

    /**
     *
     * @param tblMoldPartMaintenanceRecommend
     * @return
     */
    @Transactional
    public int updateTblMoldPartMaintenanceRecommendByQuery(TblMoldPartMaintenanceRecommend tblMoldPartMaintenanceRecommend) {
        Query query = entityManager.createNamedQuery("TblMoldPartMaintenanceRecommend.updateByMoldPartRelId");
        query.setParameter("moldPartRelId", tblMoldPartMaintenanceRecommend.getMoldPartRelId());
        query.setParameter("maintainedFlag", tblMoldPartMaintenanceRecommend.getMaintainedFlag());
        query.setParameter("updateDate", tblMoldPartMaintenanceRecommend.getUpdateDate());
        query.setParameter("updateUserUuid", tblMoldPartMaintenanceRecommend.getUpdateUserUuid());
        int cnt = query.executeUpdate();
        return cnt;
    }
    
    /**
     * Registered in mold part maintenance candidate table
     *
     * @param list
     * @return
     */
    @Transactional
    public int batchInsert(List<TblMoldPartMaintenanceRecomendDetail> list) {

        int insertCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            entityManager.persist(list.get(i - 1));

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();

                insertCount += 50;
            }

            count = i;

        }

        insertCount += count % 50;

        return insertCount;
    }
}
