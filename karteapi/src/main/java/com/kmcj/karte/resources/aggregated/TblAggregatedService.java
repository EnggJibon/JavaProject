/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.aggregated;

import com.kmcj.karte.constants.CommonConstants;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author penggd
 */
@Dependent
public class TblAggregatedService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
       
    /**
     * 集計済みテーブルに登録
     *
     * @param list
     * @return
     */
    @Transactional
    public int batchInsert(List<TblAggregated> list) {

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
