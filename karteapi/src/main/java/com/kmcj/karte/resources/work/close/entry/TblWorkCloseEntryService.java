/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.close.entry;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.persistence.NoResultException;

/**
 * 作業入力締めテーブルサービス
 * @author t.ariki
 */
@Dependent
public class TblWorkCloseEntryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    /**
     * レコード全取得
     * @return 
     */
    public TblWorkCloseEntryList getAll() {
        Query query = entityManager.createNamedQuery("TblWorkCloseEntry.findAll");
        List list = query.getResultList();
        TblWorkCloseEntryList response = new TblWorkCloseEntryList();
        response.setTblWorkCloseEntries(list);
        return response;
    }
    
    /**
     * 最新レコード取得
     * @return 
     */
    public TblWorkCloseEntry getLatest() {
        Query query = entityManager.createNamedQuery("TblWorkCloseEntry.findByLatestFlg");
        query.setParameter("latestFlg", 1);
        try {
            TblWorkCloseEntry tblWorkCloseEntry = (TblWorkCloseEntry) query.getSingleResult();
            return tblWorkCloseEntry;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 作業入力締めテーブル登録
     * @param tblWorkCloseEntry
     * @param loginUser 
     */
    @Transactional
    public void createTblWorkCloseEntry(TblWorkCloseEntry tblWorkCloseEntry, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        tblWorkCloseEntry.setId(IDGenerator.generate());
        tblWorkCloseEntry.setCreateDate(new java.util.Date());
        tblWorkCloseEntry.setCreateUserUuid(loginUser.getUserUuid());
        tblWorkCloseEntry.setUpdateDate(tblWorkCloseEntry.getCreateDate());
        tblWorkCloseEntry.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(tblWorkCloseEntry);
    }
    
    /**
     * 作業入力締めテーブル更新
     * @param tblWorkCloseEntry
     * @param loginUser 
     */
    @Transactional
    public void updateTblWorkCloseEntry(TblWorkCloseEntry tblWorkCloseEntry, LoginUser loginUser) {
        tblWorkCloseEntry.setUpdateDate(new java.util.Date());
        tblWorkCloseEntry.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(tblWorkCloseEntry);
    }
}
