package com.kmcj.karte.resources.machine.assetno;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author admin
 */
@Dependent
public class MstMachineAssetNoService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     *
     * @param machineId
     * @return MstMachineAssetNoList
     */
    public MstMachineAssetNoList getMachineAssetNos(String machineId) {
        List list = getSqlList(machineId);
        MstMachineAssetNoList response = new MstMachineAssetNoList();
        List<MstMachineAssetNoVo> mstMachineAssetNoList = new ArrayList<>();
        MstMachineAssetNoVo mstMachineAssetNos;
        for (int i = 0; i < list.size(); i++) {
            mstMachineAssetNos = new MstMachineAssetNoVo();
            Object[] objs = (Object[]) list.get(i);
            mstMachineAssetNos.setId(null == objs[0] ? "" : (String) objs[0]);
            mstMachineAssetNos.setUuid(null == objs[1] ? "" : (String) objs[1]);
            mstMachineAssetNos.setMachineUuid(null == objs[1] ? "" : (String) objs[1]);
            mstMachineAssetNos.setMachineId(null == objs[2] ? "" : (String) objs[2]);
            mstMachineAssetNos.setAssetNo(null == objs[3] ? "" : (String) objs[3]);
            if (null != objs[4]) {
                mstMachineAssetNos.setNumberedDate((Date) objs[4]);
            }
            if (null != objs[5]) {
                BigDecimal bd = new BigDecimal(objs[5].toString());
                bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
                mstMachineAssetNos.setAssetAmountStr(bd.toString());
            }
            if (null != objs[6]) {
                mstMachineAssetNos.setMainFlg(Integer.parseInt(objs[6].toString()));
            }
            mstMachineAssetNoList.add(mstMachineAssetNos);
        }
        response.setMstMachineAssetNoVos(mstMachineAssetNoList);
        return response;

    }

    public List getSqlList(String machineId) {
        StringBuilder sql;

        sql = new StringBuilder("SELECT t2.id"
                + ",t2.mstMachineAssetNoPK.machineUuid"
                + ",t1.machineId"
                + ",t2.mstMachineAssetNoPK.assetNo"
                + ",t2.numberedDate"
                + ",t2.assetAmount "
                + ",t2.mainFlg "
                + "FROM MstMachine t1 "
                + "LEFT JOIN MstMachineAssetNo t2 "
                + "WHERE t1.uuid = t2.mstMachineAssetNoPK.machineUuid  ");
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" and t1.machineId = :machineId ");
        }

        sql = sql.append(" order by t2.numberedDate");

        Query query = entityManager.createQuery(sql.toString());

        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", machineId);
        }

        return query.getResultList();
    }

    /**
     *
     * @param mstMachineAssetNoList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMachineAssetNos(List<MstMachineAssetNoVo> mstMachineAssetNoList, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        String userUuid = loginUser.getUserUuid();
        MstMachineAssetNo mstMachineAssetNo;
        MstMachineAssetNoPK mstMachineAssetNoPK;

        for (int i = 0; i < mstMachineAssetNoList.size(); i++) {

            mstMachineAssetNo = new MstMachineAssetNo();
            mstMachineAssetNoPK = new MstMachineAssetNoPK();
            MstMachineAssetNoVo mstMachineAssetNos = mstMachineAssetNoList.get(i);

            if (null != mstMachineAssetNos.getOperationFlag()) {
                switch (mstMachineAssetNos.getOperationFlag()) {
                    case "1":
                        if (getMstMachineAssetNoCheck(mstMachineAssetNos.getId())) {
                            //削除

                            deleteMstMachineAssetNoByID(mstMachineAssetNos.getId());
                            if (1 == mstMachineAssetNos.getMainFlg()) {
                                Query namedQuery = entityManager.createNamedQuery("MstMachine.updateAssetNoByMachineId");
                                namedQuery.setParameter("machineId", mstMachineAssetNos.getMachineId());
                                namedQuery.setParameter("mainAssetNo", null);
                                namedQuery.executeUpdate();
                            }

                        }
                        break;
                    case "3": {
                        Date sysDate = new Date();
                        mstMachineAssetNo.setId(IDGenerator.generate());
                        mstMachineAssetNoPK.setAssetNo(mstMachineAssetNos.getAssetNo());
                        mstMachineAssetNoPK.setMachineUuid(mstMachineAssetNos.getMachineUuid());
                        mstMachineAssetNo.setMstMachineAssetNoPK(mstMachineAssetNoPK);
                        mstMachineAssetNo.setNumberedDate(mstMachineAssetNos.getNumberedDate());
                        mstMachineAssetNo.setMainFlg(mstMachineAssetNos.getMainFlg());
                        if (null != mstMachineAssetNos.getAssetAmount()) {
                            mstMachineAssetNo.setAssetAmount(mstMachineAssetNos.getAssetAmount());
                        } else {
                            mstMachineAssetNo.setAssetAmount(BigDecimal.ZERO);
                        }
                        mstMachineAssetNo.setUpdateDate(sysDate);
                        mstMachineAssetNo.setUpdateUserUuid(userUuid);
                        if (1 == mstMachineAssetNos.getMainFlg()) {
                            Query namedQuery = entityManager.createNamedQuery("MstMachine.updateAssetNoByMachineId");
                            namedQuery.setParameter("machineId", mstMachineAssetNos.getMachineId());
                            namedQuery.setParameter("mainAssetNo", mstMachineAssetNos.getAssetNo());
                            namedQuery.executeUpdate();
                        }
                        if (checkMachineAssetNoByPK(mstMachineAssetNos.getAssetNo(), mstMachineAssetNos.getMachineUuid(), mstMachineAssetNos.getId())) {
                            updateMstMachineAssetNoByQuery(mstMachineAssetNo);
                        }
                        if (checkmainFlagByPK(mstMachineAssetNos.getMachineUuid())) {
                            Query namedQuery = entityManager.createNamedQuery("MstMachine.updateAssetNoByMachineId");
                            namedQuery.setParameter("machineId", mstMachineAssetNos.getMachineId());
                            namedQuery.setParameter("mainAssetNo", null);
                            namedQuery.executeUpdate();
                        }
                        break;
                    }
                    case "4": {
                        Date sysDate = new Date();
                        mstMachineAssetNo.setId(IDGenerator.generate());
                        mstMachineAssetNoPK.setAssetNo(mstMachineAssetNos.getAssetNo());
                        mstMachineAssetNoPK.setMachineUuid(mstMachineAssetNos.getMachineUuid());
                        mstMachineAssetNo.setMstMachineAssetNoPK(mstMachineAssetNoPK);
                        mstMachineAssetNo.setNumberedDate(mstMachineAssetNos.getNumberedDate());
                        mstMachineAssetNo.setMainFlg(mstMachineAssetNos.getMainFlg());
                        if (null != mstMachineAssetNos.getAssetAmount()) {
                            mstMachineAssetNo.setAssetAmount(mstMachineAssetNos.getAssetAmount());
                        } else {
                            mstMachineAssetNo.setAssetAmount(BigDecimal.ZERO);
                        }
                        mstMachineAssetNo.setCreateDate(sysDate);
                        mstMachineAssetNo.setCreateUserUuid(userUuid);
                        mstMachineAssetNo.setUpdateDate(sysDate);
                        mstMachineAssetNo.setUpdateUserUuid(userUuid);
                        //新規
                        if (!checkMachineAssetNoByPK(mstMachineAssetNos.getAssetNo(), mstMachineAssetNos.getMachineUuid(), mstMachineAssetNos.getId())) {
                            createMstMachineAssetNo(mstMachineAssetNo);
                        }
                        if (1 == mstMachineAssetNos.getMainFlg()) {
                            Query namedQuery = entityManager.createNamedQuery("MstMachine.updateAssetNoByMachineId");
                            namedQuery.setParameter("machineId", mstMachineAssetNos.getMachineId());
                            namedQuery.setParameter("mainAssetNo", mstMachineAssetNos.getAssetNo());
                            namedQuery.executeUpdate();
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }

        return basicResponse;
    }

    /**
     *
     * @param mstMachineAssetNo
     */
    @Transactional
    public void createMstMachineAssetNo(MstMachineAssetNo mstMachineAssetNo) {
        entityManager.persist(mstMachineAssetNo);
    }

    /**
     *
     * @param mstMachineAssetNo
     * @return
     */
    @Transactional
    public int updateMstMachineAssetNoByQuery(MstMachineAssetNo mstMachineAssetNo) {
        Query query = entityManager.createNamedQuery("MstMachineAssetNo.updateByPK");
        query.setParameter("machineUuid", mstMachineAssetNo.mstMachineAssetNoPK.getMachineUuid());
        query.setParameter("assetNo", mstMachineAssetNo.mstMachineAssetNoPK.getAssetNo());
        query.setParameter("numberedDate", mstMachineAssetNo.getNumberedDate());
        query.setParameter("assetAmount", mstMachineAssetNo.getAssetAmount());
        query.setParameter("mainFlg", mstMachineAssetNo.getMainFlg());
        query.setParameter("updateDate", mstMachineAssetNo.getUpdateDate());
        query.setParameter("updateUserUuid", mstMachineAssetNo.getUpdateUserUuid());
        int cnt = query.executeUpdate();
        return cnt;
    }
    
    /**
     *
     * @param mstMachineAssetNo
     */
    @Transactional
    public void updateMstMachineAssetNo(MstMachineAssetNo mstMachineAssetNo) {
        entityManager.merge(mstMachineAssetNo);
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean getMstMachineAssetNoCheck(String id) {
        Query query = entityManager.createNamedQuery("MstMachineAssetNo.findById");
        query.setParameter("id", id);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     *
     * @param assetNo
     * @param machineUuid
     * @return
     */
    @Transactional
    public int deleteMstMachineAssetNo(String assetNo, String machineUuid) {
        Query query = entityManager.createNamedQuery("MstMachineAssetNo.deleteByAssetNo");
        query.setParameter("assetNo", assetNo);
        query.setParameter("machineUuid", machineUuid);
        return query.executeUpdate();
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteMstMachineAssetNoByID(String id) {
        Query query = entityManager.createNamedQuery("MstMachineAssetNo.deleteById");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     *
     * @param assetNo
     * @return
     */
    public MstMachineAssetNoList getMstMachineAssetNoByMachineUuIdList(String assetNo) {
        //設備マスタ 検索　使用
        List list = getSql(assetNo, "", "1");
        MstMachineAssetNoList response = new MstMachineAssetNoList();
        response.setMstMachineAssetNos(list);

        return response;
    }

    /**
     *
     * @param assetNo
     * @return
     */
    public MstMachineAssetNoList getMstMachineAssetNoList(String assetNo) {

        List list = getSql(assetNo, "", "");
        MstMachineAssetNoList response = new MstMachineAssetNoList();
        response.setMstMachineAssetNos(list);

        return response;
    }

    /**
     *
     * @param assetNo
     * @return
     */
    public CountResponse getMstMachineAssetNoCount(String assetNo) {

        List list = getSql(assetNo, "count", "");
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));

        return count;
    }

    /**
     *
     * @param assetNo
     * @param action
     * @param sMainFlg
     * @return
     */
    public List getSql(String assetNo, String action, String sMainFlg) {

        StringBuilder sql;
        String sqlAssetNo = "";

        if ("count".equals(action)) {
            sql = new StringBuilder("SELECT count(m)");

        } else {
            sql = new StringBuilder("SELECT m");

        }

        sql = sql.append(" FROM MstMachineAssetNo m where 1=1 ");

        if (assetNo != null && !"".equals(assetNo)) {
            sqlAssetNo = assetNo.trim();
            sql.append(" and m.mstMachineAssetNoPK.assetNo = :assetNo");
        }

        if ("1".equals(sMainFlg)) {
            sql.append(" and m.mainFlg = '1' ");
        }
        Query query = entityManager.createQuery(sql.toString());

        if (assetNo != null && !"".equals(assetNo)) {
            query.setParameter("assetNo", sqlAssetNo);
        }

        List list = query.getResultList();
        return list;
    }

    /**
     *
     * @param assetNo
     * @param machineUuid
     * @param id
     * @return
     */
    public boolean checkMachineAssetNoByPK(String assetNo, String machineUuid, String id) {
        Query namedQuery = entityManager.createNamedQuery("MstMachineAssetNo.findByPK");
        namedQuery.setParameter("assetNo", assetNo);
        namedQuery.setParameter("machineUuid", machineUuid);
        try {
            namedQuery.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param machineUuid
     * @return
     */
    public boolean checkmainFlagByPK(String machineUuid) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT count(m.mainFlg) FROM MstMachineAssetNo m "
                + " WHERE m.mstMachineAssetNoPK.machineUuid = :machineUuid "
                + " And m.mainFlg = 1 ");
        Query namedQuery = entityManager.createQuery(sql.toString());
        namedQuery.setParameter("machineUuid", machineUuid);
        Long count = (Long) namedQuery.getSingleResult();
        return count == 0;
    }
    
    /**
     *
     * @param machineUuId
     * @param updateUserUuid
     */
    @Transactional
    public void updateMstMachineAssetNoByMachineUuId(String machineUuId, String updateUserUuid) {
        entityManager.createQuery("UPDATE MstMachineAssetNo m set m.mainFlg = 0,m.updateDate = :updateDate,m.updateUserUuid = :updateUserUuid where m.mstMachineAssetNoPK.machineUuid = :machineUuid and m.mainFlg = 1")
                .setParameter("updateDate", new Date())
                .setParameter("updateUserUuid", updateUserUuid)
                .setParameter("machineUuid", machineUuId)
                .executeUpdate();
    }
}
