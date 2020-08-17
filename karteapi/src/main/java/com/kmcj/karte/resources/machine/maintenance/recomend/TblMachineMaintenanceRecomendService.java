/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.maintenance.recomend;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
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
public class TblMachineMaintenanceRecomendService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    /**
     * 設備メンテ開始候補リスト取得
     *
     * @return
     */
    public TblMachineMaintenanceRecomendList getMachineMaintenanceRecommendList() {
        TblMachineMaintenanceRecomendList tblMachineMaintenanceRecomendList = new TblMachineMaintenanceRecomendList();

        // アラート・メンテ区分が2(メンテ)且つメンテ済みフラグが0(未メンテ)且つ設備メンテステータスが0(通常)のデータ抽出        
        Query machineRecomendQuery = entityManager.createNamedQuery("TblMachineMaintenanceRecomend.findRecomendList");
        List<TblMachineMaintenanceRecomend> list = machineRecomendQuery.getResultList();

        if (!list.isEmpty()) {
            FileUtil fu = new FileUtil();
            for (TblMachineMaintenanceRecomend tblMachineMaintenanceRecomend : list) {
                if (tblMachineMaintenanceRecomend.getMstMachine()!= null) {
                    tblMachineMaintenanceRecomend.getMstMachine().setLastMainteDateStr(fu.getDateFormatForStr(tblMachineMaintenanceRecomend.getMstMachine().getLastMainteDate()));
                }
            }
        }
        tblMachineMaintenanceRecomendList.setTblMachineMaintenanceRecomendList(list);
        return tblMachineMaintenanceRecomendList;
    }
    
    /**
     * 設備UUIDより設備メンテ候補データ取得
     *
     * @param machineUuid
     * @return
     */
    public TblMachineMaintenanceRecomendList getMachineMaintenanceRecommendByUuid(String machineUuid) {
        TblMachineMaintenanceRecomendList tblMachineMaintenanceRecomendList = new TblMachineMaintenanceRecomendList();

        // 設備UUIDによりメンテ済みフラグが0(未メンテ)のデータ抽出
        Query machineRecomendQuery = entityManager.createNamedQuery("TblMachineMaintenanceRecomend.findByUuid");
        machineRecomendQuery.setParameter("machineUuid", machineUuid);
        List<TblMachineMaintenanceRecomend> list = machineRecomendQuery.getResultList();

        tblMachineMaintenanceRecomendList.setTblMachineMaintenanceRecomendList(list);
        return tblMachineMaintenanceRecomendList;
    }

    /**
     * IDによる存在チェック
     *
     * @param id
     * @return
     */
    public boolean isExsistById(String id) {
        Query query = entityManager.createNamedQuery("TblMachineMaintenanceRecomend.findById");
        query.setParameter("id", id);
        try {
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (NoResultException noResultException) {
            return false;
        }
        return false;
    }

    /**
     * 設備メンテナンス候補テーブル登録
     *
     * @param tblMachineMaintenanceRecomend
     * @param loginUser
     */
    @Transactional
    public void createTblMachineMaintenanceRecomend(TblMachineMaintenanceRecomend tblMachineMaintenanceRecomend, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        tblMachineMaintenanceRecomend.setId(IDGenerator.generate());
        tblMachineMaintenanceRecomend.setCreateDate(new java.util.Date());
        tblMachineMaintenanceRecomend.setCreateUserUuid(loginUser.getUserUuid());
        tblMachineMaintenanceRecomend.setUpdateDate(new java.util.Date());
        tblMachineMaintenanceRecomend.setUpdateUserUuid(loginUser.getUserUuid());

        entityManager.persist(tblMachineMaintenanceRecomend);
    }

    /**
     * 設備メンテナンス候補テーブル削除
     *
     * @param tblMachineMaintenanceRecomend
     */
    @Transactional
    public void deleteTblMachineMaintenanceRecomend(TblMachineMaintenanceRecomend tblMachineMaintenanceRecomend) {

        Query query = entityManager.createNamedQuery("TblMachineMaintenanceRecomend.delete");
        query.setParameter("id", tblMachineMaintenanceRecomend.getId());
        query.executeUpdate();
    }

    /**
     * 設備メンテナンス候補テーブル更新
     *
     * @param tblMachineMaintenanceRecomend
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMachineMaintenanceRecomend updateTblMachineMaintenanceRecomend(TblMachineMaintenanceRecomend tblMachineMaintenanceRecomend, LoginUser loginUser) {
        tblMachineMaintenanceRecomend.setUpdateDate(new java.util.Date());
        tblMachineMaintenanceRecomend.setUpdateUserUuid(loginUser.getUserUuid());
        // メンテ済を更新する
        tblMachineMaintenanceRecomend.setMaintainedFlag(1);
        entityManager.merge(tblMachineMaintenanceRecomend);
        return tblMachineMaintenanceRecomend;
    }
    
        /**
     * 金型メンテ開始候補リスト取得
     *
     * @param machineUuid
     * @param alertMainteType
     * @param maintainedFlag
     *
     * @return
     */
    public int chkExists(String machineUuid, Integer alertMainteType, Integer maintainedFlag) {

        Query query = entityManager.createNamedQuery("TblMachineMaintenanceRecomend.chkExists");
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("alertMainteType", alertMainteType);
        query.setParameter("maintainedFlag", maintainedFlag);

        return query.getResultList().size();
    }

    /**
     * 設備メンテナンス候補テーブルに登録
     * 
     * @param list
     * @return 
     */
    @Transactional
    public int batchInsert(List<TblMachineMaintenanceRecomend> list) {
        
        int insertCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            entityManager.persist(list.get(i - 1));

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();

                insertCount += 50;
            }

            count = i;

        }

        insertCount += count % 50;

        return insertCount;
    }

    /**
     * 設備メンテナンス候補テーブル削除 BY サイクルID 
     * サイクルコード修正するとき、且つメンテ候補テーブルのデータがまだ処理されていないデータを削除
     *
     * @param mainteCycleId
     */
    @Transactional
    public void deleteTblMachineMaintenanceRecomendBymainteCycleId(String mainteCycleId) {
        Query query = entityManager.createNamedQuery("TblMachineMaintenanceRecomend.deleteByMainteCycleId");
        query.setParameter("mainteCycleId", mainteCycleId);
        query.executeUpdate();
    }
    
    /**
     * // 最終メンテナンス日 //メンテナンス後生産時間 //メンテナンス後ショット数 //メンテサイクルコード01 //メンテサイクルコード02
     * //メンテサイクルコード03 //　上記項目について、いずれか更新されたら、設備候補テーブルに該当設備が未メンテのデータを削除を行う
     *
     * @param machineUuid
     */
    @Transactional
    public void deleteTblMachineMaintenanceRecomendByMachineUuid(String machineUuid) {
        Query query = entityManager.createNamedQuery("TblMachineMaintenanceRecomend.deleteByMainteMachineUuid");
        query.setParameter("machineUuid", machineUuid);
        query.executeUpdate();
    }


}
