/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmold.choice;

import com.kmcj.karte.constants.CommonConstants;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author
 */
@Dependent
public class ExtMstChoiceService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    
    public String getExtMstChoiceText(String companyId, String category, String seq ,String langId) {
        Query query = entityManager.createNamedQuery("ExtMstChoice.findByComAndCategoryAndSeqAndLang");
        query.setParameter("companyId", companyId);
        query.setParameter("category", category);
        query.setParameter("seq", seq);
        query.setParameter("langId", langId);
        try {
            ExtMstChoice aExtMstChoice = (ExtMstChoice) query.getSingleResult();
            return aExtMstChoice.getChoice();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     *
     * @param companyId
     * @param langId
     * @param categoryKeys
     * @return
     */
    public Map<String, String> getChoiceMap(String companyId, String langId, String[] categoryKeys) {

        Query query = entityManager.createNamedQuery("ExtMstChoice.findByCategoryList");

        query.setParameter("langId", langId);
        query.setParameter("companyId", companyId);
        List<String> categoryList = Arrays.asList(categoryKeys);
        query.setParameter("categoryList", categoryList);

        List<ExtMstChoice> list = (List<ExtMstChoice>) query.getResultList();

        Map<String, String> result = new HashMap<String, String>();

        for (ExtMstChoice mstChoice : list) {

            result.put(mstChoice.getCategory() + mstChoice.getSeq(), mstChoice.getChoice());
        }

        return result;
    }
}
