package com.kmcj.karte.resources.machine.proccond.spec;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.machine.proccond.MstMachineProcCondService;
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
public class MstMachineProcCondSpecService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    private MstMachineProcCondService mstMachineProcCondService;

    public boolean getMstMachineProcCondSpecExistCheck(String machineProcCondId, String attrId) {
        Query query = entityManager.createNamedQuery("MstMachineProcCondSpec.findByMachineProcCondIdAndAttrId");
        query.setParameter("machineProcCondId", machineProcCondId);
        query.setParameter("attrId", attrId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

//    /**
//     *
//     * @param machineProcCondSpec
//     * @return
//     */
//    @Transactional
//    public int updateMstMachineSpec(MstMachineProcCondSpec machineProcCondSpec) {
//        Query query = entityManager.createNamedQuery("MstMachineProcCondSpec.updateMachineProcCondSpec");
//        query.setParameter("attrValue", machineProcCondSpec.getAttrValue());
//        query.setParameter("updateUserUuid", machineProcCondSpec.getUpdateUserUuid());
//        query.setParameter("updateDate", machineProcCondSpec.getUpdateDate());
//        query.setParameter("machineProcCondId", machineProcCondSpec.getMstMachineProcCondSpecPK().getMachineProcCondId());
//        query.setParameter("attrId", machineProcCondSpec.getMstMachineProcCondSpecPK().getAttrId());
//        return query.executeUpdate();
//    }

    /**
     *
     * @param machineProcCondSpec
     */
    @Transactional
    public void createMstMachineSpec(MstMachineProcCondSpec machineProcCondSpec) {
        java.util.Date utilDate = new Date();
        java.sql.Timestamp stp = new java.sql.Timestamp(utilDate.getTime());
        machineProcCondSpec.setCreateDate(stp);
        machineProcCondSpec.setUpdateDate(stp);
        entityManager.persist(machineProcCondSpec);
    }

    /**
     *
     * バッチで設備加工条件仕様マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstMachineProcCondSpecList getExtMachineProcCondSpecsByBatch(String latestExecutedDate, String machineUuid) {
        MstMachineProcCondSpecList resList = new MstMachineProcCondSpecList();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMachineProcCondSpec t join MstApiUser u on u.companyId = t.mstMachineProcCond.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.mstMachineProcCond.mstMachine.uuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMachineProcCondSpec> tmpList = query.getResultList();
        for (MstMachineProcCondSpec mstMachineProcCondSpec : tmpList) {
            mstMachineProcCondSpec.setMstMachineProcCond(null);
            mstMachineProcCondSpec.setMstMachineProcCondAttribute(null);
        }
        resList.setMstMachineProcCondSpecs(tmpList);
        return resList;
    }

    /**
     * バッチで設備加工条件仕様マスタデータを更新
     *
     * @param procCondSpec
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineProcCondSpecsByBatch(List<MstMachineProcCondSpec> procCondSpec) {
        BasicResponse response = new BasicResponse();

        if (procCondSpec != null && !procCondSpec.isEmpty()) {
            
            for (MstMachineProcCondSpec aMachineProcCondSpec : procCondSpec) {

                List<MstMachineProcCondSpec> oldProcCondSpecs = entityManager.createQuery("SELECT t FROM MstMachineProcCondSpec t WHERE t.mstMachineProcCondSpecPK.machineProcCondId = :machineProcCondId and t.mstMachineProcCondSpecPK.attrId = :attrId ")
                        .setParameter("machineProcCondId", aMachineProcCondSpec.getMstMachineProcCondSpecPK().getMachineProcCondId())
                        .setParameter("attrId", aMachineProcCondSpec.getMstMachineProcCondSpecPK().getAttrId())
                        .setMaxResults(1)
                        .getResultList();

                MstMachineProcCondSpec newMachineProcCondSpec;

                if (null != oldProcCondSpecs && !oldProcCondSpecs.isEmpty()) {
                    newMachineProcCondSpec = oldProcCondSpecs.get(0);
                } else {
                    newMachineProcCondSpec = new MstMachineProcCondSpec();
                    newMachineProcCondSpec.setId(aMachineProcCondSpec.getId());
                    MstMachineProcCondSpecPK mstMachineProcCondSpecPK = new MstMachineProcCondSpecPK();
                    mstMachineProcCondSpecPK.setAttrId(aMachineProcCondSpec.getMstMachineProcCondSpecPK().getAttrId());
                    mstMachineProcCondSpecPK.setMachineProcCondId(aMachineProcCondSpec.getMstMachineProcCondSpecPK().getMachineProcCondId());
                    newMachineProcCondSpec.setMstMachineProcCondSpecPK(mstMachineProcCondSpecPK);
                }
                newMachineProcCondSpec.setAttrValue(aMachineProcCondSpec.getAttrValue());
                newMachineProcCondSpec.setCreateDate(aMachineProcCondSpec.getCreateDate());
                newMachineProcCondSpec.setCreateUserUuid(aMachineProcCondSpec.getCreateUserUuid());
                newMachineProcCondSpec.setUpdateDate(new Date());
                newMachineProcCondSpec.setUpdateUserUuid(aMachineProcCondSpec.getUpdateUserUuid());

                if (null != oldProcCondSpecs && !oldProcCondSpecs.isEmpty()) {
                    entityManager.merge(newMachineProcCondSpec);
                } else {
                    entityManager.persist(newMachineProcCondSpec);
                }
            }
        }
        response.setError(false);
        return response;
    }
}
