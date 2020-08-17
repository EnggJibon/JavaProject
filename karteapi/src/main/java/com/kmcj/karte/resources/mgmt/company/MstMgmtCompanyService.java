/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mgmt.company;

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
public class MstMgmtCompanyService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    /**
     *
     * @param mgmtCompanyCode
     * @param mgmtCompanyName
     * @param isLike
     * @return
     */
    public MstMgmtCompanyList getMstMgmtCompanyList(String mgmtCompanyCode, String mgmtCompanyName, boolean isLike) {
        MstMgmtCompanyList mstMgmtCompanyList = new MstMgmtCompanyList();
        StringBuffer sql;
        sql = new StringBuffer(" SELECT m FROM MstMgmtCompany m WHERE 1=1 ");
        if (isLike) {
            if (StringUtils.isNotEmpty(mgmtCompanyCode)) {
                sql.append(" AND m.mgmtCompanyCode LIKE :mgmtCompanyCode ");
            }

            if (StringUtils.isNotEmpty(mgmtCompanyName)) {
                sql.append(" AND m.mgmtCompanyName LIKE :mgmtCompanyName ");
            }
        } else if (StringUtils.isNotEmpty(mgmtCompanyCode)) {
            sql.append(" AND m.mgmtCompanyCode = :mgmtCompanyCode ");
        } else if (StringUtils.isNotEmpty(mgmtCompanyName)) {
            sql.append(" AND m.mgmtCompanyName = :mgmtCompanyName ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (isLike) {
            if (StringUtils.isNotEmpty(mgmtCompanyCode)) {
                query.setParameter("mgmtCompanyCode", "%" + mgmtCompanyCode + "%");
            }

            if (StringUtils.isNotEmpty(mgmtCompanyName)) {
                query.setParameter("mgmtCompanyName", "%" + mgmtCompanyName + "%");
            }
        } else if (StringUtils.isNotEmpty(mgmtCompanyCode)) {
            query.setParameter("mgmtCompanyCode", mgmtCompanyCode);
        } else if (StringUtils.isNotEmpty(mgmtCompanyName)) {
            query.setParameter("mgmtCompanyName", mgmtCompanyName);
        }
        mstMgmtCompanyList.setMstMgmtCompanies(query.getResultList());
        return mstMgmtCompanyList;
    }

    /**
     * 管理先コードを存在チェック
     *
     * @param mgmtCompanyCode
     * @return
     */
    public boolean getSingleMstMgmtCompany(String mgmtCompanyCode) {
        Query query = entityManager.createNamedQuery("MstMgmtCompany.findByMgmtCompanyCode");
        query.setParameter("mgmtCompanyCode", mgmtCompanyCode);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
    
    /**
     * 管理先コード追加
     * @param mstMgmtCompany
     * @param loginUser 
     */
    @Transactional
    public void createMstMgmtCompany(MstMgmtCompany mstMgmtCompany,LoginUser loginUser) {
        mstMgmtCompany.setCreateUserUuid(IDGenerator.generate());
        mstMgmtCompany.setMgmtCompanyCode(mstMgmtCompany.getMgmtCompanyCode());
        mstMgmtCompany.setMgmtCompanyName(mstMgmtCompany.getMgmtCompanyName());
        mstMgmtCompany.setCreateDate(new Date());
        mstMgmtCompany.setCreateUserUuid(loginUser.getUserUuid());
        mstMgmtCompany.setUpdateDate(new Date());
        mstMgmtCompany.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(mstMgmtCompany);
    }
    
    /**
     * 管理先コード更新
     * @param mstMgmtCompany
     * @param loginUser 
     */
    @Transactional
    public void updateMstMgmtCompany(MstMgmtCompany mstMgmtCompany,LoginUser loginUser) {
        mstMgmtCompany.setMgmtCompanyCode(mstMgmtCompany.getMgmtCompanyCode());
        mstMgmtCompany.setMgmtCompanyName(mstMgmtCompany.getMgmtCompanyName());
        mstMgmtCompany.setCreateDate(new Date());
        mstMgmtCompany.setCreateUserUuid(loginUser.getUserUuid());
        mstMgmtCompany.setUpdateDate(new Date());
        mstMgmtCompany.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(mstMgmtCompany);
    }
}
