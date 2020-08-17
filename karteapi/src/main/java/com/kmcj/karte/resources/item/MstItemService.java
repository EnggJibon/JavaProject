/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.item;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;
import java.util.Date;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
public class MstItemService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    /**
     *
     * @param itemCode
     * @param itemName
     * @param isLike
     * @return
     */
    public MstItemList getMstItemList(String itemCode, String itemName, boolean isLike) {
        MstItemList mstItemList = new MstItemList();
        StringBuffer sql;
        sql = new StringBuffer(" SELECT m FROM MstItem m WHERE 1=1 ");
        if (isLike) {
            if (StringUtils.isNotEmpty(itemCode)) {
                sql.append(" AND m.itemCode LIKE :itemCode ");
            }

            if (StringUtils.isNotEmpty(itemName)) {
                sql.append(" AND m.itemName LIKE :itemName ");
            }
        } else if (StringUtils.isNotEmpty(itemCode)) {
            sql.append(" AND m.itemCode = :itemCode ");
        } else if (StringUtils.isNotEmpty(itemName)) {
            sql.append(" AND m.itemName = :itemName ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (isLike) {
            if (StringUtils.isNotEmpty(itemCode)) {
                query.setParameter("itemCode", "%" + itemCode + "%");
            }

            if (StringUtils.isNotEmpty(itemName)) {
                query.setParameter("itemName", "%" + itemName + "%");
            }
        } else if (StringUtils.isNotEmpty(itemCode)) {
            query.setParameter("itemCode", itemCode);
        } else if (StringUtils.isNotEmpty(itemName)) {
            query.setParameter("itemName", itemName);
        }
        mstItemList.setMstItems(query.getResultList());
        return mstItemList;
    }

    /**
     * 品目コードを存在チェック
     *
     * @param itemCode
     * @return
     */
    public boolean getSingleMstItem(String itemCode) {
        Query query = entityManager.createNamedQuery("MstItem.findByItemCode");
        query.setParameter("itemCode", itemCode);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
    
    /**
     * 品目情報を取得
     *
     * @param itemCode
     * @return
     */
    public MstItem getSingleItem(String itemCode) {
        Query query = entityManager.createNamedQuery("MstItem.findByItemCode");
        query.setParameter("itemCode", itemCode);
        try {
            
            return (MstItem)query.getSingleResult();
            
        } catch (NoResultException e) {
            
            return null;
        }
    }

    /**
     * 品目コード追加
     * @param mstItem
     * @param loginUser 
     */
    @Transactional
    public void createMstItem(MstItem mstItem,LoginUser loginUser) {
        mstItem.setCreateUserUuid(IDGenerator.generate());
        mstItem.setItemCode(mstItem.getItemCode());
        mstItem.setItemName(mstItem.getItemName());
        mstItem.setCreateDate(new Date());
        mstItem.setCreateUserUuid(loginUser.getUserUuid());
        mstItem.setUpdateDate(new Date());
        mstItem.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(mstItem);
    }
    
    /**
     * 品目コード更新
     * @param mstItem
     * @param loginUser 
     */
    @Transactional
    public void updateMstItem(MstItem mstItem,LoginUser loginUser) {
        mstItem.setItemCode(mstItem.getItemCode());
        mstItem.setItemName(mstItem.getItemName());
        mstItem.setCreateDate(new Date());
        mstItem.setCreateUserUuid(loginUser.getUserUuid());
        mstItem.setUpdateDate(new Date());
        mstItem.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(mstItem);
    }
}
