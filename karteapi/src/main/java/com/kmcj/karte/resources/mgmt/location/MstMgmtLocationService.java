/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mgmt.location;

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
public class MstMgmtLocationService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    /**
     *
     * @param mgmtLocationCode
     * @param mgmtLocationName
     * @param isLike
     * @return
     */
    public MstMgmtLocationList getMstMgmtLocationList(String mgmtLocationCode, String mgmtLocationName, boolean isLike) {
        MstMgmtLocationList mstMgmtLocationList = new MstMgmtLocationList();
        StringBuffer sql;
        sql = new StringBuffer(" SELECT m FROM MstMgmtLocation m WHERE 1=1 ");
        if (isLike) {
            if (StringUtils.isNotEmpty(mgmtLocationCode)) {
                sql.append(" AND m.mgmtLocationCode LIKE :mgmtLocationCode ");
            }

            if (StringUtils.isNotEmpty(mgmtLocationName)) {
                sql.append(" AND m.mgmtLocationName LIKE :mgmtLocationName ");
            }
        } else if (StringUtils.isNotEmpty(mgmtLocationCode)) {
            sql.append(" AND m.mgmtLocationCode = :mgmtLocationCode ");
        } else if (StringUtils.isNotEmpty(mgmtLocationName)) {
            sql.append(" AND m.mgmtLocationName = :mgmtLocationName ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (isLike) {
            if (StringUtils.isNotEmpty(mgmtLocationCode)) {
                query.setParameter("mgmtLocationCode", "%" + mgmtLocationCode + "%");
            }

            if (StringUtils.isNotEmpty(mgmtLocationName)) {
                query.setParameter("mgmtLocationName", "%" + mgmtLocationName + "%");
            }
        } else if (StringUtils.isNotEmpty(mgmtLocationCode)) {
            query.setParameter("mgmtLocationCode", mgmtLocationCode);
        } else if (StringUtils.isNotEmpty(mgmtLocationName)) {
            query.setParameter("mgmtLocationName", mgmtLocationName);
        }
        mstMgmtLocationList.setMstMgmtLocations(query.getResultList());
        return mstMgmtLocationList;
    }

    /**
     * 設置先コードを存在チェック
     *
     * @param mgmtLocationCode
     * @return
     */
    public boolean getSingleMstMgmtLocation(String mgmtLocationCode) {
        Query query = entityManager.createNamedQuery("MstMgmtLocation.findByMgmtLocationCode");
        query.setParameter("mgmtLocationCode", mgmtLocationCode);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
    
    /**
     * 設置先コード追加
     * @param mstMgmtLocation
     * @param loginUser 
     */
    @Transactional
    public void createMstMgmtLocation(MstMgmtLocation mstMgmtLocation,LoginUser loginUser) {
        mstMgmtLocation.setCreateUserUuid(IDGenerator.generate());
        mstMgmtLocation.setMgmtLocationCode(mstMgmtLocation.getMgmtLocationCode());
        mstMgmtLocation.setMgmtLocationName(mstMgmtLocation.getMgmtLocationName());
        mstMgmtLocation.setCreateDate(new Date());
        mstMgmtLocation.setCreateUserUuid(loginUser.getUserUuid());
        mstMgmtLocation.setUpdateDate(new Date());
        mstMgmtLocation.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(mstMgmtLocation);
    }
    
    /**
     * 設置先コード更新
     * @param mstMgmtLocation
     * @param loginUser 
     */
    @Transactional
    public void updateMstMgmtLocation(MstMgmtLocation mstMgmtLocation,LoginUser loginUser) {
        mstMgmtLocation.setMgmtLocationCode(mstMgmtLocation.getMgmtLocationCode());
        mstMgmtLocation.setMgmtLocationName(mstMgmtLocation.getMgmtLocationName());
        mstMgmtLocation.setCreateDate(new Date());
        mstMgmtLocation.setCreateUserUuid(loginUser.getUserUuid());
        mstMgmtLocation.setUpdateDate(new Date());
        mstMgmtLocation.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(mstMgmtLocation);
    }

}
