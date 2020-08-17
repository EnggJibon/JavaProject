package com.kmcj.karte.resources.common.gridcolumn;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.component.inspection.result.model.TblGridColumnMemory;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author t.takasaki
 */
@Dependent
public class GridcolumnMemoryService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    
    public List<TblGridColumnMemory> getGridColumnMemory(String userId, String screenId, String gridId) {
        return entityManager.createNamedQuery("TblGridColumnMemory.findByGridScreenId", TblGridColumnMemory.class)
            .setParameter("userId", userId)
            .setParameter("screenId", screenId)
            .setParameter("gridId", gridId).getResultList();
    }
    
    @Transactional
    public void mergeGridColumnMemory(String userId, List<TblGridColumnMemory> gridCols) {
        for (TblGridColumnMemory gridCol : gridCols) {
            gridCol.setUserId(userId);
            entityManager.merge(gridCol);
        }
    }
}
