/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.spec;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.mold.proccond.MstMoldProcCondService;
import com.kmcj.karte.util.FileUtil;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author jiangxs
 */
@Dependent
public class MstMoldProcCondSpecService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    private MstMoldProcCondService mstMoldProcCondService;

    public boolean getMstMoldProcCondSpecExistCheck(String moldProcCondId, String attrId) {
        Query query = entityManager.createNamedQuery("MstMoldProcCondSpec.findByMoldProcCondIdAndAttrId");
        query.setParameter("moldProcCondId", moldProcCondId);
        query.setParameter("attrId", attrId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     *
     * @param mstMoldProcCondSpec
     * @return
     */
    @Transactional
    public int updateMstMoldSpec(MstMoldProcCondSpec mstMoldProcCondSpec) {
        Query query = entityManager.createNamedQuery("MstMoldProcCondSpec.updateMoldProcCondSpec");
        query.setParameter("attrValue", mstMoldProcCondSpec.getAttrValue());
        query.setParameter("updateUserUuid", mstMoldProcCondSpec.getUpdateUserUuid());
        query.setParameter("updateDate", mstMoldProcCondSpec.getUpdateDate());
        query.setParameter("moldProcCondId", mstMoldProcCondSpec.getMstMoldProcCondSpecPK().getMoldProcCondId());
        query.setParameter("attrId", mstMoldProcCondSpec.getMstMoldProcCondSpecPK().getAttrId());
        return query.executeUpdate();
    }

    /**
     *
     * @param mstMoldProcCondSpec
     */
    @Transactional
    public void createMstMoldSpec(MstMoldProcCondSpec mstMoldProcCondSpec) {
        java.util.Date utilDate = new Date();
        java.sql.Timestamp stp = new java.sql.Timestamp(utilDate.getTime());
        mstMoldProcCondSpec.setCreateDate(stp);
        mstMoldProcCondSpec.setUpdateDate(stp);
        entityManager.persist(mstMoldProcCondSpec);
    }
    
    /**
     * 
     * バッチで金型加工条件仕様マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstMoldProcCondSpecList getExtMoldProcCondSpecsByBatch(String latestExecutedDate, String moldUuid) {
        MstMoldProcCondSpecList resList = new MstMoldProcCondSpecList();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMoldProcCondSpec t join MstApiUser u on u.companyId = t.mstMoldProcCond.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.mstMoldProcCond.mstMold.uuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMoldProcCondSpec> tmpList = query.getResultList();
        for (MstMoldProcCondSpec mstMoldProcCondSpec : tmpList) {
            mstMoldProcCondSpec.setMstMoldProcCond(null);
            mstMoldProcCondSpec.setMstMoldProcCondAttribute(null);
        }
        resList.setMstMoldProcCondSpecList(tmpList);
        return resList;
    }

    /**
     * バッチで金型加工条件仕様マスタデータを更新
     *
     * @param procCondSpecs
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldProcCondSpecsByBatch(List<MstMoldProcCondSpec> procCondSpecs) {
        BasicResponse response = new BasicResponse();

        if (procCondSpecs != null && !procCondSpecs.isEmpty()) {
            List<String> moldProcCondIds = mstMoldProcCondService.getAllIdList();
            for (MstMoldProcCondSpec aMoldProcCondSpec : procCondSpecs) {
                if (null != moldProcCondIds && !moldProcCondIds.isEmpty() && moldProcCondIds.contains(aMoldProcCondSpec.getMstMoldProcCondSpecPK().getMoldProcCondId())) {
                    List<MstMoldProcCondSpec> oldProcCondSpecs = entityManager.createQuery("SELECT t FROM MstMoldProcCondSpec t WHERE t.mstMoldProcCondSpecPK.moldProcCondId = :moldProcCondId and t.mstMoldProcCondSpecPK.attrId = :attrId ")
                            .setParameter("moldProcCondId", aMoldProcCondSpec.getMstMoldProcCondSpecPK().getMoldProcCondId())
                            .setParameter("attrId", aMoldProcCondSpec.getMstMoldProcCondSpecPK().getAttrId())
                            .setMaxResults(1)
                            .getResultList();

                    MstMoldProcCondSpec newMoldProcCondSpec;

                    if (null != oldProcCondSpecs && !oldProcCondSpecs.isEmpty()) {
                        newMoldProcCondSpec = oldProcCondSpecs.get(0);
                    } else {
                        newMoldProcCondSpec = new MstMoldProcCondSpec();
                        newMoldProcCondSpec.setId(aMoldProcCondSpec.getId());
                        MstMoldProcCondSpecPK pk = new MstMoldProcCondSpecPK();
                        pk.setAttrId(aMoldProcCondSpec.getMstMoldProcCondSpecPK().getAttrId());
                        pk.setMoldProcCondId(aMoldProcCondSpec.getMstMoldProcCondSpecPK().getMoldProcCondId());
                        newMoldProcCondSpec.setMstMoldProcCondSpecPK(pk);
                    }
                    newMoldProcCondSpec.setAttrValue(aMoldProcCondSpec.getAttrValue());

                    newMoldProcCondSpec.setCreateDate(aMoldProcCondSpec.getCreateDate());
                    newMoldProcCondSpec.setCreateUserUuid(aMoldProcCondSpec.getCreateUserUuid());
                    newMoldProcCondSpec.setUpdateDate(new Date());
                    newMoldProcCondSpec.setUpdateUserUuid(aMoldProcCondSpec.getUpdateUserUuid());

                    if (null != oldProcCondSpecs && !oldProcCondSpecs.isEmpty()) {
                        entityManager.merge(newMoldProcCondSpec);
                    } else {
                        newMoldProcCondSpec.setId(aMoldProcCondSpec.getId());
                        entityManager.persist(newMoldProcCondSpec);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}