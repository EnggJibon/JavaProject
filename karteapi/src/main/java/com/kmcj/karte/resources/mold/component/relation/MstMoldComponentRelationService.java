/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.component.relation;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVo;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVoList;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author hangju
 */
@Dependent
//@Transactional
public class MstMoldComponentRelationService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     *
     * @param moldUuid
     * @param componentId
     * @return
     */
    public boolean mstMoldComponentRelationExistCheckByMoldUuid(String moldUuid, String componentId) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.findByMoldUuid");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("componentId", componentId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * 金型部品関係マスタをPKで取得
     * @param moldUuid
     * @param componentId
     * @return 
     */
    public MstMoldComponentRelation getMstMoldComponentRelation(String moldUuid, String componentId) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.findByMoldUuid");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("componentId", componentId);
        try {
            return (MstMoldComponentRelation)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     *
     * @param componentId
     * @return
     */
    public List getMstComponent(String componentId) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.findByComponentId");
        query.setParameter("componentId", componentId);
        return query.getResultList();
    }

    @Transactional
    public int deleteMstMold(String moldUuid) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.deleteBymoldUuid");
        query.setParameter("moldUuid", moldUuid);
        return query.executeUpdate();
    }

    /**
     *
     * @param componentId
     * @return
     */
    @Transactional
    public int deleteMstMoldComponentRelationByComponentId(String componentId) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.deleteByComponentId");
        query.setParameter("componentId", componentId);
        return query.executeUpdate();
    }

    @Transactional
    public int deleteMstMoldComponentRelation(String componentId, String moldUuId) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.deleteByComponentIdAndMoldUuid");
        query.setParameter("moldUuid", moldUuId);
        query.setParameter("componentId", componentId);
        return query.executeUpdate();
    }

    /**
     *
     * @param inMstMoldComponentRelation
     * @param loginUser
     */
    @Transactional
    public void createMstMoldComponentRelation(MstMoldComponentRelation inMstMoldComponentRelation, LoginUser loginUser) {

        MstMoldComponentRelation mstMoldComponentRelation = new MstMoldComponentRelation();
        MstMoldComponentRelationPK mstMoldComponentRelationPK = new MstMoldComponentRelationPK();
        mstMoldComponentRelationPK.setMoldUuid(inMstMoldComponentRelation.getMstMoldComponentRelationPK().getMoldUuid());
        mstMoldComponentRelationPK.setComponentId(inMstMoldComponentRelation.getMstMoldComponentRelationPK().getComponentId());
        mstMoldComponentRelation.setMstMoldComponentRelationPK(mstMoldComponentRelationPK);
        mstMoldComponentRelation.setProcedureId(inMstMoldComponentRelation.getProcedureId());
        mstMoldComponentRelation.setCountPerShot(inMstMoldComponentRelation.getCountPerShot());

        mstMoldComponentRelation.setCreateDate(new java.util.Date());
        mstMoldComponentRelation.setCreateUserUuid(loginUser.getUserUuid());
        mstMoldComponentRelation.setUpdateDate(new java.util.Date());
        mstMoldComponentRelation.setUpdateUserUuid(loginUser.getUserUuid());

        entityManager.persist(mstMoldComponentRelation);
    }

    /**
     *
     * @param mstMoldComponentRelation
     */
    @Transactional
    public void createMstMoldComponentRelationByCsv(MstMoldComponentRelation mstMoldComponentRelation) {
        entityManager.persist(mstMoldComponentRelation);
    }

    /**
     *
     * @param inMstMoldComponentRelation
     * @param loginUser
     */
    @Transactional
    public int updateMstMoldComponentRelation(MstMoldComponentRelation inMstMoldComponentRelation, LoginUser loginUser) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.updateByComponentCode");
        query.setParameter("moldUuid", inMstMoldComponentRelation.getMstMoldComponentRelationPK().getMoldUuid());
        query.setParameter("componentId", inMstMoldComponentRelation.getMstMoldComponentRelationPK().getComponentId());
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());

        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     *
     * @param moldId
     * @return
     */
    public MstMoldComponentRelationList getMstMoldComponentRelationByMoldId(String moldId) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.findSql");
        query.setParameter("uuid", moldId);
        List list = query.getResultList();
        MstMoldComponentRelationList response = new MstMoldComponentRelationList();
        response.setMstMoldComponentRelation(list);
        return response;
    }
    
    /**
     * 部品IDから関連している金型情報取得
     * 
     * @param componentId
     * @return
     */
    public MstMoldComponentRelationList getMstMoldComponentRelationByComponentId(String componentId) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.findByComponentId");
        query.setParameter("componentId", componentId);
        List list = query.getResultList();
        MstMoldComponentRelationList response = new MstMoldComponentRelationList();
        response.setMstMoldComponentRelation(list);
        return response;
    }
    /**
     * 部品IDから関連している金型情報取得
     * 
     * @param componentId
     * @return
     */
    public MstMoldComponentRelationList getMstMoldComponentRelationByComponentIdWithoutDispose(String componentId) {
        Query query = entityManager.createNamedQuery("MstMoldComponentRelation.findByComponentIdWithoutDispose");
        query.setParameter("componentId", componentId);
        List list = query.getResultList();
        MstMoldComponentRelationList response = new MstMoldComponentRelationList();
        response.setMstMoldComponentRelation(list);
        return response;
    }
    /**
     * バッチで金型部品関係データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstMoldComponentRelationList getExtMoldComponentRelationsByBatch(String latestExecutedDate, String moldUuid) {
        MstMoldComponentRelationList resList = new MstMoldComponentRelationList();
        List<MstMoldComponentRelationVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMoldComponentRelation t join MstApiUser u on u.companyId = t.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.mstMold.uuid = :moldUuid ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        List<MstMoldComponentRelation> tmpList = query.getResultList();
        for (MstMoldComponentRelation mstMoldComponentRelation : tmpList) {
            MstMoldComponentRelationVo aVo = new MstMoldComponentRelationVo();
            MstComponent mstComponent = null;
            if (null != (mstComponent = mstMoldComponentRelation.getMstComponent())) {
                aVo.setComponentCode(mstComponent.getComponentCode());
            }
            mstMoldComponentRelation.setMstComponent(null);
            MstMold mstMold = null;
            if (null != (mstMold = mstMoldComponentRelation.getMstMold())) {
                aVo.setMoldId(mstMold.getMoldId());
            }
            mstMoldComponentRelation.setMstMold(null);

            aVo.setMstMoldComponentRelation(mstMoldComponentRelation);
            resVo.add(aVo);
        }
        resList.setMstMoldComponentRelationVos(resVo);
        return resList;
    }

    /**
     * バッチで金型部品関係データを更新
     *
     * @param relationVos
     * @param mstComponentCompanyVoList
     * @return
     */
    @Transactional
    public BasicResponse updateMoldComponentRelationsByBatch(List<MstMoldComponentRelationVo> relationVos,
            MstComponentCompanyVoList mstComponentCompanyVoList) {
        BasicResponse response = new BasicResponse();
        if (relationVos != null && !relationVos.isEmpty()) {

            // --スタート---金型部品関係マスタを更新する前に削除を実行
            StringBuilder deleteMCR = new StringBuilder("DELETE FROM MstMoldComponentRelation moldComponentRelation WHERE moldComponentRelation.mstMold.moldId IN ( ");
            int oldCount = relationVos.size();

            for (int i = 0; i < oldCount; i++) {
                deleteMCR.append(" :moldId").append(i);
                if (i != oldCount - 1) {
                    deleteMCR.append(",");
                }
            }
            deleteMCR.append(") ");

            Query delOldRelations = entityManager.createQuery(deleteMCR.toString());
            for (int i = 0; i < oldCount; i++) {
                delOldRelations.setParameter("moldId" + i, relationVos.get(i).getMoldId());
            }

            delOldRelations.executeUpdate();
            // --END---金型部品関係マスタを更新する前に削除を実行

            for (MstMoldComponentRelationVo aMoldComponentRelationVo : relationVos) {

                MstMoldComponentRelation newMoldComponentRelation = new MstMoldComponentRelation();

                MstMoldComponentRelationPK moldComponentRelationPK = new MstMoldComponentRelationPK();

                //自社のComponentIDに変換
                Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
                query.setParameter("componentCode", aMoldComponentRelationVo.getComponentCode());
                List<MstComponent> mstComponentList = query.getResultList();

                if (null != mstComponentList && !mstComponentList.isEmpty()) {
                    moldComponentRelationPK.setComponentId(mstComponentList.get(0).getId());
                } else if (mstComponentCompanyVoList.getMstComponentCompanyVos().size() > 0) {
                    for (MstComponentCompanyVo mstComponentCompanyVo : mstComponentCompanyVoList.getMstComponentCompanyVos()) {
                        if (StringUtils.isNotEmpty(aMoldComponentRelationVo.getComponentCode()) && StringUtils.isNotEmpty(mstComponentCompanyVo.getOtherComponentCode())) {
                            if (aMoldComponentRelationVo.getComponentCode().equals(mstComponentCompanyVo.getComponentCode())) {

                                // 先方部品により自社の部品IDを置換
                                query.setParameter("componentCode", mstComponentCompanyVo.getOtherComponentCode());
                                List<MstComponent> otherMstComponentList = query.getResultList();
                                if (null != otherMstComponentList && !otherMstComponentList.isEmpty()) {
                                    moldComponentRelationPK.setComponentId(otherMstComponentList.get(0).getId());
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    continue;
                }

                if (StringUtils.isNotEmpty(moldComponentRelationPK.getComponentId())) {
                    //自社の金型UUIDに変換
                    MstMold mstMold = entityManager.find(MstMold.class, aMoldComponentRelationVo.getMoldId());
                    if (null != mstMold) {
                        moldComponentRelationPK.setMoldUuid(mstMold.getUuid());
                        newMoldComponentRelation.setMstMoldComponentRelationPK(moldComponentRelationPK);

                        newMoldComponentRelation.setCountPerShot(FileUtil.getIntegerValue(aMoldComponentRelationVo.getMstMoldComponentRelation().getCountPerShot()));

                        newMoldComponentRelation.setCreateDate(aMoldComponentRelationVo.getMstMoldComponentRelation().getCreateDate());
                        newMoldComponentRelation.setCreateUserUuid(aMoldComponentRelationVo.getMstMoldComponentRelation().getCreateUserUuid());
                        newMoldComponentRelation.setUpdateDate(new Date());
                        newMoldComponentRelation.setUpdateUserUuid(aMoldComponentRelationVo.getMstMoldComponentRelation().getUpdateUserUuid());
                        entityManager.persist(newMoldComponentRelation);
                    }
                }
            }
        }
        response.setError(false);
        return response;
    }

}
