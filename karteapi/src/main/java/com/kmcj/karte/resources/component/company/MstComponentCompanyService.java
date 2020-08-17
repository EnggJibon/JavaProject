/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.company;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
public class MstComponentCompanyService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    /**
     *
     * @param mstComponentCompanyVos
     * @param userUuid
     * @param componentId
     */
    @Transactional
    public void createMstComponentCompany(List<MstComponentCompanyVo> mstComponentCompanyVos, String userUuid, String componentId) {
        // 重複があれば、除外する
        for (int i = 0; i < mstComponentCompanyVos.size(); i++) {
            for (int j = mstComponentCompanyVos.size() - 1; j > i; j--) {
                MstComponentCompanyVo vo1 = mstComponentCompanyVos.get(j);
                MstComponentCompanyVo vo2 = mstComponentCompanyVos.get(i);
                if (vo1.getCompanyId().equals(vo2.getCompanyId())) {
                    mstComponentCompanyVos.remove(j);
                }
            }
        }
        //
        for (MstComponentCompanyVo mstComponentCompanyVo : mstComponentCompanyVos) {

            if (StringUtils.isNotEmpty(mstComponentCompanyVo.getCompanyId())) {
                MstComponentCompany mstComponentCompany = new MstComponentCompany();
                MstComponentCompanyPK mstComponentCompanyPK = new MstComponentCompanyPK();
                mstComponentCompanyPK.setCompanyId(mstComponentCompanyVo.getCompanyId());
                mstComponentCompanyPK.setComponentId(componentId);
                mstComponentCompany.setOtherComponentCode(mstComponentCompanyVo.getOtherComponentCode());
                mstComponentCompany.setMstComponentCompanyPK(mstComponentCompanyPK);
                mstComponentCompany.setCreateDate(new java.util.Date());
                mstComponentCompany.setCreateUserUuid(userUuid);
                mstComponentCompany.setUpdateDate(new java.util.Date());
                mstComponentCompany.setUpdateUserUuid(userUuid);
                entityManager.persist(mstComponentCompany);
            }

        }

    }

    /**
     *
     * @param componentId
     */
    @Transactional
    public void deleteMstComponentCompanyByComponentId(String componentId) {
        Query query = entityManager.createNamedQuery("MstComponentCompany.deleteByComponentId");
        query.setParameter("componentId", componentId);
        query.executeUpdate();
    }

    /**
     *
     * @param componentId
     * @return
     */
    public List<MstComponentCompanyVo> getComponentCompaniesByComponentId(String componentId) {

        Query query = entityManager.createNamedQuery("MstComponentCompany.findByComponentId");
        query.setParameter("componentId", componentId);
        List list = query.getResultList();
        List<MstComponentCompanyVo> mstComponentCompanyVos = new ArrayList<>();
        MstComponentCompanyVo mstComponentCompanyVo;
        for (int i = 0; i < list.size(); i++) {
            MstComponentCompany mstComponentCompany = (MstComponentCompany) list.get(i);

            mstComponentCompanyVo = new MstComponentCompanyVo();
            mstComponentCompanyVo.setComponentId(mstComponentCompany.getMstComponent().getId());
            mstComponentCompanyVo.setCompanyId(mstComponentCompany.getMstComponentCompanyPK().getCompanyId());
            mstComponentCompanyVo.setOtherComponentCode(mstComponentCompany.getOtherComponentCode());

            mstComponentCompanyVos.add(mstComponentCompanyVo);
        }

        return mstComponentCompanyVos;
    }

    /**
     * 先方部品コードから読み替えができたとき
     *
     * @param companyId
     * @param inMstComponentCompanyVos
     * @return
     */
    public MstComponentCompanyVoList replacementOtherComponentCode(String companyId, MstComponentCompanyVoList inMstComponentCompanyVos) {
        MstComponentCompanyVoList mstComponentCompanyVoList = new MstComponentCompanyVoList();

        StringBuilder sql = new StringBuilder("SELECT m FROM MstComponentCompany m "
                + " JOIN FETCH m.mstComponent WHERE 1=1 ");

        if (StringUtils.isNotEmpty(companyId)) {
            sql.append(" AND m.mstComponentCompanyPK.companyId = :companyId ");
        }

        if (null != inMstComponentCompanyVos
                && null != inMstComponentCompanyVos.getMstComponentCompanyVos()
                && inMstComponentCompanyVos.getMstComponentCompanyVos().size() > 0) {
            sql.append(" AND m.otherComponentCode IN :otherComponentCodes ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(companyId)) {
            query.setParameter("companyId", companyId);
        }

        if (null != inMstComponentCompanyVos
                && null != inMstComponentCompanyVos.getMstComponentCompanyVos()
                && inMstComponentCompanyVos.getMstComponentCompanyVos().size() > 0) {
            List otherComponentCodes = new ArrayList();
            for (int i = 0; i < inMstComponentCompanyVos.getMstComponentCompanyVos().size(); i++) {
                MstComponentCompanyVo mstComponentCompanyVo = inMstComponentCompanyVos.getMstComponentCompanyVos().get(i);
                otherComponentCodes.add(mstComponentCompanyVo.getOtherComponentCode());
            }

            query.setParameter("otherComponentCodes", otherComponentCodes);
        }

        List list = query.getResultList();
        MstComponentCompanyVo mstComponentCompanyVo;
        List<MstComponentCompanyVo> mstComponentCompanyVos = new ArrayList();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                mstComponentCompanyVo = new MstComponentCompanyVo();
                MstComponentCompany mstComponentCompany = (MstComponentCompany) list.get(i);
                mstComponentCompanyVo.setOtherComponentCode(mstComponentCompany.getOtherComponentCode());
                MstComponent mstComponent = mstComponentCompany.getMstComponent();
                if (mstComponent != null) {
                    mstComponentCompanyVo.setComponentId(mstComponent.getId());
                    mstComponentCompanyVo.setComponentName(mstComponent.getComponentName());
                    mstComponentCompanyVo.setComponentCode(mstComponent.getComponentCode());
                }
                mstComponentCompanyVos.add(mstComponentCompanyVo);
            }
        }

        mstComponentCompanyVoList.setMstComponentCompanyVos(mstComponentCompanyVos);
        return mstComponentCompanyVoList;
    }

    /**
     * 外部連携バッチ用
     * @param loginUser
     * @return 
     */
    public MstComponentCompanyVoList getBatchOtherComponentCode(LoginUser loginUser) {
        MstComponentCompanyVoList mstComponentCompanyVoList = new MstComponentCompanyVoList();

        Query query = entityManager.createNamedQuery("MstComponentCompany.findMstComponentCompanyByBatch");

        query.setParameter("companyId", loginUser.getCompanyId());
        query.setParameter("apiUserId", loginUser.getUserid());

        List list = query.getResultList();
        List<MstComponentCompanyVo> mstComponentCompanyVos = new ArrayList<>();
        MstComponentCompanyVo mstComponentCompanyVo;
        for (int i = 0; i < list.size(); i++) {
            MstComponentCompany mstComponentCompany = (MstComponentCompany) list.get(i);

            mstComponentCompanyVo = new MstComponentCompanyVo();
            mstComponentCompanyVo.setComponentId(mstComponentCompany.getMstComponent().getId());
            mstComponentCompanyVo.setCompanyId(mstComponentCompany.getMstComponentCompanyPK().getCompanyId());
            mstComponentCompanyVo.setOtherComponentCode(mstComponentCompany.getOtherComponentCode());
            mstComponentCompanyVo.setComponentCode(mstComponentCompany.getMstComponent().getComponentCode());
            mstComponentCompanyVo.setComponentName(mstComponentCompany.getMstComponent().getComponentName());
            mstComponentCompanyVos.add(mstComponentCompanyVo);
        }
        mstComponentCompanyVoList.setMstComponentCompanyVos(mstComponentCompanyVos);
        return mstComponentCompanyVoList;
    }

}
