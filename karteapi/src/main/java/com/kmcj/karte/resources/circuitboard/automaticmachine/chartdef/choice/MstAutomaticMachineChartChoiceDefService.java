package com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.choice;


import com.kmcj.karte.constants.CommonConstants;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import javax.enterprise.context.Dependent;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Apeng
 */
@Dependent
public class MstAutomaticMachineChartChoiceDefService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

   /**
    * 棒グラフ表示対象取得
    * @param automaticMachineType
    * @param logType
    * @param seq
    * @return 
    */
    public List getMachineChartChoiceDefByPK(String automaticMachineType, String logType,String seq) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT t FROM MstAutomaticMachineChartChoiceDef t WHERE 1=1 ");

        if (StringUtils.isNotEmpty(automaticMachineType)) {
            sql.append(" AND t.mstAutomaticMachineChartChoiceDefPK.automaticMachineType = :automaticMachineType ");
        }
        
        if (StringUtils.isNotEmpty(logType)) {
            sql.append(" AND t.mstAutomaticMachineChartChoiceDefPK.logType = :logType ");
        }
        
        if (StringUtils.isNotEmpty(seq)) {
            sql.append(" AND t.mstAutomaticMachineChartChoiceDefPK.seq = :seq ");
        }
        
        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(automaticMachineType)) {
            query.setParameter("automaticMachineType", automaticMachineType);
        }

        if (StringUtils.isNotEmpty(logType)) {
            query.setParameter("logType", logType);
        }

        if (StringUtils.isNotEmpty(seq)) {
            query.setParameter("seq", Integer.parseInt(seq));
        }
        return query.getResultList();
    }
}
