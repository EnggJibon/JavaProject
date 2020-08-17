/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.currency;

import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
public class MstCurrencyService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private CnfSystemService cnfSystemService;

    /**
     * 通貨コードを存在チェック
     *
     * @param currencyCode
     * @return
     */
    public boolean getSingleMstCurrency(String currencyCode) {
        Query query = entityManager.createNamedQuery("MstCurrency.findByCurrencyCode");
        query.setParameter("currencyCode", currencyCode);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     *
     * @param currencyCode
     * @param currencyName
     * @param isLike
     * @return
     */
    public MstCurrencyList getMstCurrencyList(String currencyCode, String currencyName, boolean isLike) {
        MstCurrencyList mstCurrencyList = new MstCurrencyList();
        StringBuffer sql;
        sql = new StringBuffer(" SELECT m FROM MstCurrency m WHERE 1=1 ");
        if (isLike) {
            if (StringUtils.isNotEmpty(currencyCode)) {
                sql.append(" AND m.currencyCode LIKE :currencyCode ");
            }

            if (StringUtils.isNotEmpty(currencyName)) {
                sql.append(" AND m.currencyName LIKE :currencyName ");
            }
        } else if (StringUtils.isNotEmpty(currencyCode)) {
            sql.append(" AND m.currencyCode = :currencyCode ");
        } else if (StringUtils.isNotEmpty(currencyName)) {
            sql.append(" AND m.currencyName = :currencyName ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (isLike) {
            if (StringUtils.isNotEmpty(currencyCode)) {
                query.setParameter("currencyCode", "%" + currencyCode + "%");
            }

            if (StringUtils.isNotEmpty(currencyName)) {
                query.setParameter("currencyName", "%" + currencyName + "%");
            }
        } else if (StringUtils.isNotEmpty(currencyCode)) {
            query.setParameter("currencyCode", currencyCode);
        } else if (StringUtils.isNotEmpty(currencyName)) {
            query.setParameter("currencyName", currencyName);
        }
        mstCurrencyList.setMstCurrencies(query.getResultList());
        return mstCurrencyList;
    }

    /**
     * currencyCode
     *
     * @param currencyCode
     * @return
     */
    public MstCurrency getDefaultCurrencyMstCurrency(String currencyCode) {

        Query query = entityManager.createNamedQuery("MstCurrency.findByCurrencyCode");

        if (StringUtils.isEmpty(currencyCode) || (StringUtils.isNotEmpty(currencyCode) && !getSingleMstCurrency(currencyCode))) {
            CnfSystem cnfSystem = cnfSystemService.findByKey("system", "default_currency_code");
            currencyCode = cnfSystem.getConfigValue();
        }
        query.setParameter("currencyCode", currencyCode);

        try {
            MstCurrency mstCurrency = (MstCurrency) query.getSingleResult();
            return mstCurrency;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 小数桁数を取得
     *
     * @param currencyCode
     * @return
     */
    public int getDecimalPlacesFromByCurrencyCode(String currencyCode){
        
        int decimalPlaces = 0;
        
        MstCurrency mstCurrency = getDefaultCurrencyMstCurrency(currencyCode);
        
        if(null != mstCurrency){
            
            decimalPlaces = mstCurrency.getDecimalPlaces();
            
        }
        
        return decimalPlaces;
    }

    /**
     *
     * @return
     */
    public MstCurrencyList getMstCurrency() {
        Query query = entityManager.createNamedQuery("MstCurrency.findAll");

        MstCurrencyList mstCurrencyList = null;
        try {
            List list = query.getResultList();

            mstCurrencyList = new MstCurrencyList();
            mstCurrencyList.setMstCurrencies(list);
        } catch (NoResultException e) {

        }
        return mstCurrencyList;
    }

}
