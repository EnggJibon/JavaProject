/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.assetno;

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
public class MstMoldAssetNoService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     *
     * @param moldId
     * @return MstMoldAssetNoList
     */
    public MstMoldAssetNoList getMoldAssetNos(String moldId) {
        List list = getSqlList(moldId);
        MstMoldAssetNoList response = new MstMoldAssetNoList();
        List<MstMoldAssetNos> mstMoldAssetNoList = new ArrayList<>();
        MstMoldAssetNos mstMoldAssetNos;
        for (int i = 0; i < list.size(); i++) {
            mstMoldAssetNos = new MstMoldAssetNos();
            Object[] objs = (Object[]) list.get(i);
            mstMoldAssetNos.setId(null == objs[0] ? "" : (String) objs[0]);
            mstMoldAssetNos.setUuid(null == objs[1] ? "" : (String) objs[1]);
            mstMoldAssetNos.setMoldUuid(null == objs[1] ? "" : (String) objs[1]);
            mstMoldAssetNos.setMoldId(null == objs[2] ? "" : (String) objs[2]);
            mstMoldAssetNos.setAssetNo(null == objs[3] ? "" : (String) objs[3]);
            if (null != objs[4]) {
                mstMoldAssetNos.setNumberedDate((Date) objs[4]);
            }
            if (null != objs[5]) {
                BigDecimal bd = new BigDecimal(objs[5].toString());
                bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
                mstMoldAssetNos.setStrAssetAmount(bd.toString());
//                mstMoldAssetNos.setAssetAmount(new BigDecimal(objs[5].toString()));
            }
            if (null != objs[6]) {
                mstMoldAssetNos.setMainFlg(Integer.parseInt(objs[6].toString()));
            }
            mstMoldAssetNoList.add(mstMoldAssetNos);
        }
        response.setMstMoldAssetNos(mstMoldAssetNoList);
        return response;

    }

    public List getSqlList(String moldId) {
        StringBuilder sql;

        sql = new StringBuilder("SELECT t2.id"
                + ",t2.mstMoldAssetNoPK.moldUuid"
                + ",t1.moldId"
                + ",t2.mstMoldAssetNoPK.assetNo"
                + ",t2.numberedDate"
                + ",t2.assetAmount "
                + ",t2.mainFlg "
                + "FROM MstMold t1 "
                + "LEFT JOIN MstMoldAssetNo t2 "
                + "WHERE t1.uuid = t2.mstMoldAssetNoPK.moldUuid  ");
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" and t1.moldId = :moldId ");
        }

        sql = sql.append(" order by t2.numberedDate");

        Query query = entityManager.createQuery(sql.toString());

        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", moldId);
        }

        return query.getResultList();
    }

    /**
     *
     * @param mstMoldAssetNoList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMoldAssetNos(List<MstMoldAssetNos> mstMoldAssetNoList, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        String userUuid = loginUser.getUserUuid();
        MstMoldAssetNo mstMoldAssetNo;
        MstMoldAssetNoPK mstMoldAssetNoPK;

        for (int i = 0; i < mstMoldAssetNoList.size(); i++) {

            mstMoldAssetNo = new MstMoldAssetNo();
            mstMoldAssetNoPK = new MstMoldAssetNoPK();
            MstMoldAssetNos mstMoldAssetNos = mstMoldAssetNoList.get(i);

            if (null != mstMoldAssetNos.getDeleteFlag()) {
                switch (mstMoldAssetNos.getDeleteFlag()) {
                    case "1":
                        if (getMstMoldAssetNoCheck(mstMoldAssetNos.getId())) {
                            //削除

                            deleteMstMoldAssetNoByID(mstMoldAssetNos.getId());
                            if (1 == mstMoldAssetNos.getMainFlg()) {
                                Query namedQuery = entityManager.createNamedQuery("MstMold.updateAssetNoByMoldId");
                                namedQuery.setParameter("moldId", mstMoldAssetNos.getMoldId());
                                namedQuery.setParameter("mainAssetNo", null);
                                namedQuery.executeUpdate();
                            }

                        }
                        break;
                    case "3": {
                        Date sysDate = new Date();
                        mstMoldAssetNo.setId(IDGenerator.generate());
                        mstMoldAssetNoPK.setAssetNo(mstMoldAssetNos.getAssetNo());
                        mstMoldAssetNoPK.setMoldUuid(mstMoldAssetNos.getMoldUuid());
                        mstMoldAssetNo.setMstMoldAssetNoPK(mstMoldAssetNoPK);
                        mstMoldAssetNo.setNumberedDate(mstMoldAssetNos.getNumberedDate());
                        mstMoldAssetNo.setMainFlg(mstMoldAssetNos.getMainFlg());
                        if (null != mstMoldAssetNos.getAssetAmount()) {
                            mstMoldAssetNo.setAssetAmount(mstMoldAssetNos.getAssetAmount());
                        } else {
                            mstMoldAssetNo.setAssetAmount(BigDecimal.ZERO);
                        }
                        mstMoldAssetNo.setUpdateDate(sysDate);
                        mstMoldAssetNo.setUpdateUserUuid(userUuid);
                        if (1 == mstMoldAssetNos.getMainFlg()) {
                            Query namedQuery = entityManager.createNamedQuery("MstMold.updateAssetNoByMoldId");
                            namedQuery.setParameter("moldId", mstMoldAssetNos.getMoldId());
                            namedQuery.setParameter("mainAssetNo", mstMoldAssetNos.getAssetNo());
                            namedQuery.executeUpdate();
                        }
                        if (checkMoldAssetNoByPK(mstMoldAssetNos.getAssetNo(), mstMoldAssetNos.getMoldUuid(), mstMoldAssetNos.getId())) {
                            updateMstMoldAssetNoByQuery(mstMoldAssetNo);
                        }
                        if (checkmainFlagByPK(mstMoldAssetNos.getMoldUuid())) {
                            Query namedQuery = entityManager.createNamedQuery("MstMold.updateAssetNoByMoldId");
                            namedQuery.setParameter("moldId", mstMoldAssetNos.getMoldId());
                            namedQuery.setParameter("mainAssetNo", null);
                            namedQuery.executeUpdate();
                        }
                        break;
                    }
                    case "4": {
                        Date sysDate = new Date();
                        mstMoldAssetNo.setId(IDGenerator.generate());
                        mstMoldAssetNoPK.setAssetNo(mstMoldAssetNos.getAssetNo());
                        mstMoldAssetNoPK.setMoldUuid(mstMoldAssetNos.getMoldUuid());
                        mstMoldAssetNo.setMstMoldAssetNoPK(mstMoldAssetNoPK);
                        mstMoldAssetNo.setNumberedDate(mstMoldAssetNos.getNumberedDate());
                        mstMoldAssetNo.setMainFlg(mstMoldAssetNos.getMainFlg());
                        if (null != mstMoldAssetNos.getAssetAmount()) {
                            mstMoldAssetNo.setAssetAmount(mstMoldAssetNos.getAssetAmount());
                        } else {
                            mstMoldAssetNo.setAssetAmount(BigDecimal.ZERO);
                        }
                        mstMoldAssetNo.setCreateDate(sysDate);
                        mstMoldAssetNo.setCreateUserUuid(userUuid);
                        mstMoldAssetNo.setUpdateDate(sysDate);
                        mstMoldAssetNo.setUpdateUserUuid(userUuid);
                        //新規
                        if (!checkMoldAssetNoByPK(mstMoldAssetNos.getAssetNo(), mstMoldAssetNos.getMoldUuid(), mstMoldAssetNos.getId())) {
                            createMstMoldAssetNo(mstMoldAssetNo);
                        }
                        if (1 == mstMoldAssetNos.getMainFlg()) {
                            Query namedQuery = entityManager.createNamedQuery("MstMold.updateAssetNoByMoldId");
                            namedQuery.setParameter("moldId", mstMoldAssetNos.getMoldId());
                            namedQuery.setParameter("mainAssetNo", mstMoldAssetNos.getAssetNo());
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
     * @param mstMoldAssetNo
     */
    @Transactional
    public void createMstMoldAssetNo(MstMoldAssetNo mstMoldAssetNo) {
        entityManager.persist(mstMoldAssetNo);
    }

    /**
     *
     * @param mstMoldAssetNo
     * @return
     */
    @Transactional
    public int updateMstMoldAssetNoByQuery(MstMoldAssetNo mstMoldAssetNo) {
        Query query = entityManager.createNamedQuery("MstMoldAssetNo.updateByPK");
        query.setParameter("moldUuid", mstMoldAssetNo.mstMoldAssetNoPK.getMoldUuid());
        query.setParameter("assetNo", mstMoldAssetNo.mstMoldAssetNoPK.getAssetNo());
        query.setParameter("numberedDate", mstMoldAssetNo.getNumberedDate());
        query.setParameter("assetAmount", mstMoldAssetNo.getAssetAmount());
        query.setParameter("mainFlg", mstMoldAssetNo.getMainFlg());
        query.setParameter("updateDate", mstMoldAssetNo.getUpdateDate());
        query.setParameter("updateUserUuid", mstMoldAssetNo.getUpdateUserUuid());
        int cnt = query.executeUpdate();
        return cnt;
    }
    
    /**
     * 金型登録画面CSV入力用金型資産番号マスタに代表フラグだけ更新処理
     * 
     * @param mstMoldAssetNo
     * @return 
     */
    @Transactional
    public int updateMstMoldAssetNoMainFlg(MstMoldAssetNo mstMoldAssetNo) {
        Query query = entityManager.createQuery("UPDATE MstMoldAssetNo m SET m.mainFlg = :mainFlg, m.updateDate = :updateDate, m.updateUserUuid = :updateUserUuid WHERE m.mstMoldAssetNoPK.assetNo = :assetNo and m.mstMoldAssetNoPK.moldUuid = :moldUuid");
        query.setParameter("moldUuid", mstMoldAssetNo.mstMoldAssetNoPK.getMoldUuid());
        query.setParameter("assetNo", mstMoldAssetNo.mstMoldAssetNoPK.getAssetNo());
        query.setParameter("mainFlg", mstMoldAssetNo.getMainFlg());
        query.setParameter("updateDate", mstMoldAssetNo.getUpdateDate());
        query.setParameter("updateUserUuid", mstMoldAssetNo.getUpdateUserUuid());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean getMstMoldAssetNoCheck(String id) {
        Query query = entityManager.createNamedQuery("MstMoldAssetNo.findById");
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
     * @param moldUuid
     * @return
     */
    @Transactional
    public int deleteMstMoldAssetNo(String assetNo, String moldUuid) {
        Query query = entityManager.createNamedQuery("MstMoldAssetNo.deleteByAssetNo");
        query.setParameter("assetNo", assetNo);
        query.setParameter("moldUuid", moldUuid);
        return query.executeUpdate();
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteMstMoldAssetNoByID(String id) {
        Query query = entityManager.createNamedQuery("MstMoldAssetNo.deleteByID");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     *
     * @param assetNo
     * @return
     */
    public MstMoldAssetNoList getMstMoldAssetNoByMoldUuIdList(String assetNo) {
        //金型マスタ 検索　使用
        List list = getSql(assetNo, "", "1");
        MstMoldAssetNoList response = new MstMoldAssetNoList();
        response.setMstMoldAssetNo(list);

        return response;
    }

    /**
     *
     * @param assetNo
     * @return
     */
    public MstMoldAssetNoList getMstMoldAssetNoList(String assetNo) {

        List list = getSql(assetNo, "", "");
        MstMoldAssetNoList response = new MstMoldAssetNoList();
        response.setMstMoldAssetNo(list);

        return response;
    }

    /**
     *
     * @param assetNo
     * @return
     */
    public CountResponse getMstMoldAssetNoCount(String assetNo) {

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

        sql = sql.append(" FROM MstMoldAssetNo m where 1=1 ");

        if (assetNo != null && !"".equals(assetNo)) {
            sqlAssetNo = assetNo.trim();
            sql.append(" and m.mstMoldAssetNoPK.assetNo = :assetNo");
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
     * @param moldUuid
     * @param id
     * @return
     */
    public boolean checkMoldAssetNoByPK(String assetNo, String moldUuid, String id) {
        Query namedQuery = entityManager.createNamedQuery("MstMoldAssetNo.findByPK");
        namedQuery.setParameter("assetNo", assetNo);
        namedQuery.setParameter("moldUuid", moldUuid);
        try {
            namedQuery.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param moldUuid
     * @return
     */
    public boolean checkmainFlagByPK(String moldUuid) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT count(m.mainFlg) FROM MstMoldAssetNo m "
                + " WHERE m.mstMoldAssetNoPK.moldUuid = :moldUuid "
                + " And m.mainFlg = 1 ");
        Query namedQuery = entityManager.createQuery(sql.toString());
        namedQuery.setParameter("moldUuid", moldUuid);
        Long count = (Long) namedQuery.getSingleResult();
        return count == 0;
    }

    @Transactional
    public void updateMstMoldAssetNoByMoldUuId(String strMoldUuId, String userUuid) {
        entityManager.createQuery("UPDATE MstMoldAssetNo m set m.mainFlg = 0,m.updateDate = :updateDate,m.updateUserUuid = :updateUserUuid where m.mstMoldAssetNoPK.moldUuid = :moldUuid and m.mainFlg = 1")
                .setParameter("updateDate", new Date())
                .setParameter("updateUserUuid", userUuid)
                .setParameter("moldUuid", strMoldUuId)
                .executeUpdate();
    }

    @Transactional
    public void updateMstMoldAssetNo(MstMoldAssetNo mstMoldAssetNo) {
        entityManager.merge(mstMoldAssetNo);
    }
}
