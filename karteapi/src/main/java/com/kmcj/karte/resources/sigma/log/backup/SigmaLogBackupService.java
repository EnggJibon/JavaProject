/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log.backup;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author c.darvin
 */

@Dependent
public class SigmaLogBackupService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    /**
     * @param sigmaLogBackupList  
     */
    @Transactional
    public void backupAvailable(TblSigmaLogBackupList sigmaLogBackupList, LoginUser loginUser){
        Date currentDate = new Date();
        List<String> gunshiIdRetainer = new ArrayList();
        for (TblSigmaLogBackup sigmaLogBackup : sigmaLogBackupList.getTblSigmaLogBackup()) {
            if(sigmaLogBackup.getGunshiId() == null || sigmaLogBackup.getFolderName() == null) continue;
            
            if(!gunshiIdRetainer.contains(sigmaLogBackup.getGunshiId().toString())){
                gunshiIdRetainer.add(sigmaLogBackup.getGunshiId().toString());
                
                availableToZero(sigmaLogBackup.getGunshiId().toString());
            }
            StringBuilder sqlSelect = new StringBuilder();

            sqlSelect.append("SELECT t FROM TblSigmaLogBackup t");
            sqlSelect.append(" WHERE t.tblSigmaLogBackupPK.gunshiId = :gunshiId");
            sqlSelect.append(" AND t.tblSigmaLogBackupPK.folderName = :folderName");
            Query querySelect = entityManager.createQuery(sqlSelect.toString());
            querySelect.setParameter("gunshiId", sigmaLogBackup.getGunshiId());
            querySelect.setParameter("folderName", sigmaLogBackup.getFolderName());
            List<TblSigmaLogBackup> selectList = querySelect.getResultList();

            //sigmaLogBackup.setProcStatus(sigmaLogBackup.getProcStatus());
            sigmaLogBackup.setAvailableFlg(1);
            sigmaLogBackup.setReImportedDate(null);
            sigmaLogBackup.setUpdateDate(currentDate);
            sigmaLogBackup.setUpdateUserUuid(loginUser.getUserUuid());
            
            if(selectList.isEmpty() == false){
                updateAvailable(sigmaLogBackup);
            } else {
                insertAvailable(sigmaLogBackup);
            }
        }
    }

    private void availableToZero(String gunshiId){
        StringBuilder sqlFlgToZero = new StringBuilder();
        sqlFlgToZero.append("UPDATE TblSigmaLogBackup t ");
        sqlFlgToZero.append(" SET t.availableFlg = 0");
        sqlFlgToZero.append(" WHERE");
        sqlFlgToZero.append(" t.tblSigmaLogBackupPK.gunshiId = :gunshiId");
        Query queryFlgToZero = entityManager.createQuery(sqlFlgToZero.toString());
        //queryFlgToZero.setParameter("availableFlg", 0);
        queryFlgToZero.setParameter("gunshiId", gunshiId);
        queryFlgToZero.executeUpdate();
    }
    
    private void updateAvailable(TblSigmaLogBackup sigmaLogBackup){
        Date currentDate = new Date();
        StringBuilder sqlFlgToZero = new StringBuilder();
        sqlFlgToZero.append("UPDATE TblSigmaLogBackup t ");
        sqlFlgToZero.append(" SET t.availableFlg = :availableFlg");
        sqlFlgToZero.append(", t.updateDate = :currentDate");
        sqlFlgToZero.append(", t.updateUserUuid = :loginUser");
        sqlFlgToZero.append(" WHERE");
        sqlFlgToZero.append(" t.tblSigmaLogBackupPK.gunshiId = :gunshiId");
        sqlFlgToZero.append(" AND");
        sqlFlgToZero.append(" t.tblSigmaLogBackupPK.folderName = :folderName");
        Query queryFlgToZero = entityManager.createQuery(sqlFlgToZero.toString());
        queryFlgToZero.setParameter("availableFlg", sigmaLogBackup.getAvailableFlg());
        queryFlgToZero.setParameter("currentDate", currentDate);
        queryFlgToZero.setParameter("loginUser", sigmaLogBackup.getUpdateUserUuid());
        queryFlgToZero.setParameter("gunshiId", sigmaLogBackup.getGunshiId());
        queryFlgToZero.setParameter("folderName", sigmaLogBackup.getFolderName());
        queryFlgToZero.executeUpdate();
    }
    
    private void insertAvailable(TblSigmaLogBackup sigmaLogBackup){
        StringBuilder sqlList = new StringBuilder();
        sqlList.append("SELECT m FROM MstSigma m");
        if(sigmaLogBackup.getGunshiId() != null){
            sqlList.append(" WHERE m.id = :gunshiId");
        }
        Query query = entityManager.createQuery(sqlList.toString());
        query.setParameter("gunshiId", sigmaLogBackup.getGunshiId());
        List<TblSigmaLogBackup> list = query.getResultList();
        if(list == null || list.size() <= 0) return;

        TblSigmaLogBackup tblSigmaLogBackup = new TblSigmaLogBackup();
        TblSigmaLogBackupPK sigmaLogBackupPK = new TblSigmaLogBackupPK();
        tblSigmaLogBackup.setTblSigmaLogBackupPK(sigmaLogBackupPK);
        
        tblSigmaLogBackup.setGunshiId(sigmaLogBackup.getGunshiId());
        tblSigmaLogBackup.setFolderName(sigmaLogBackup.getFolderName());
        sigmaLogBackupPK.setGunshiId(tblSigmaLogBackup.getGunshiId());
        sigmaLogBackupPK.setFolderName(tblSigmaLogBackup.getFolderName());
        tblSigmaLogBackup.tblSigmaLogBackupPK.setGunshiId(sigmaLogBackupPK.getGunshiId());
        tblSigmaLogBackup.tblSigmaLogBackupPK.setFolderName(sigmaLogBackupPK.getFolderName());
        tblSigmaLogBackup.setReImportedDate(sigmaLogBackup.getReImportedDate());
        tblSigmaLogBackup.setAvailableFlg(sigmaLogBackup.getAvailableFlg());
        tblSigmaLogBackup.setProcStatus(0);
        tblSigmaLogBackup.setUpdateDate(sigmaLogBackup.getUpdateDate());
        tblSigmaLogBackup.setCreateDate(sigmaLogBackup.getUpdateDate()); 
        tblSigmaLogBackup.setUpdateUserUuid(sigmaLogBackup.getUpdateUserUuid());
        tblSigmaLogBackup.setCreateUserUuid(sigmaLogBackup.getUpdateUserUuid());
        entityManager.persist(tblSigmaLogBackup);
    }
    
    @Transactional
    public void backupList(TblSigmaLogBackupList sigmaLogBackupList, LoginUser loginUser){
        for (TblSigmaLogBackup sigmaLogBackup : sigmaLogBackupList.getTblSigmaLogBackup()) {
            if(sigmaLogBackup.getGunshiId() == null || sigmaLogBackup.getFolderName() == null) continue;
            
            sigmaLogBackup.setUpdateUserUuid(loginUser.getUserUuid());
            
            updateBackupList(sigmaLogBackup);
        }
    }
    
    private void updateBackupList(TblSigmaLogBackup sigmaLogBackup){
        Date currentDate = new Date();
        StringBuilder sqlFlgToZero = new StringBuilder();
        sqlFlgToZero.append("UPDATE TblSigmaLogBackup t ");
        sqlFlgToZero.append(" SET t.updateDate = :currentDate");
        sqlFlgToZero.append(", t.updateUserUuid = :loginUser");
        sqlFlgToZero.append(", t.procStatus = :procStatus");
        if(sigmaLogBackup.getProcStatus() == 3){
            sqlFlgToZero.append(", t.reImportedDate = :currentDate");
        }
        else {
            sqlFlgToZero.append(", t.reImportedDate = null ");
        }
        sqlFlgToZero.append(" WHERE");
        sqlFlgToZero.append(" t.tblSigmaLogBackupPK.gunshiId = :gunshiId");
        sqlFlgToZero.append(" AND");
        sqlFlgToZero.append(" t.tblSigmaLogBackupPK.folderName = :folderName");
        Query queryFlgToZero = entityManager.createQuery(sqlFlgToZero.toString());
        queryFlgToZero.setParameter("gunshiId", sigmaLogBackup.getGunshiId());
        queryFlgToZero.setParameter("folderName", sigmaLogBackup.getFolderName());
        queryFlgToZero.setParameter("currentDate", currentDate);        
        queryFlgToZero.setParameter("loginUser", sigmaLogBackup.getUpdateUserUuid());
        queryFlgToZero.setParameter("procStatus", sigmaLogBackup.getProcStatus());
        if(sigmaLogBackup.getProcStatus() == 3){
            queryFlgToZero.setParameter("currentDate", currentDate);
        }
        queryFlgToZero.executeUpdate();
    }
    
    
    public TblSigmaLogBackupList getList (String gunshiId){
        TblSigmaLogBackupList response = new TblSigmaLogBackupList();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t FROM TblSigmaLogBackup t WHERE t.availableFlg = 1");
        
        if(gunshiId != null){
            sql.append(" AND t.tblSigmaLogBackupPK.gunshiId = :gunshiId");
        }
        sql.append(" ORDER BY t.tblSigmaLogBackupPK.gunshiId, t.tblSigmaLogBackupPK.folderName ASC ");
        Query query = entityManager.createQuery(sql.toString());
        if(gunshiId != null){
            query.setParameter("gunshiId", gunshiId);
        }
        List<TblSigmaLogBackup> list = query.getResultList();
        
        response.setTblSigmaLogBackup(list);
        response.formatJson();
        return response; 
    }
    
    public TblSigmaLogBackupList getRequests (String gunshiId){
        TblSigmaLogBackupList response = new TblSigmaLogBackupList();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t FROM TblSigmaLogBackup t WHERE t.procStatus = 1 AND t.availableFlg = 1");
        
        if(gunshiId != null){
            sql.append(" AND t.tblSigmaLogBackupPK.gunshiId = :gunshiId");
        }
        sql.append(" ORDER BY t.tblSigmaLogBackupPK.gunshiId, t.tblSigmaLogBackupPK.folderName ASC");
        Query query = entityManager.createQuery(sql.toString());
        if(gunshiId != null){
            query.setParameter("gunshiId", gunshiId);
        }
        List<TblSigmaLogBackup> list = query.getResultList();
        
        response.setTblSigmaLogBackup(list);
        response.formatJson();
        return response; 
    }
    
    @Transactional
    public void sigmaLogBackup(TblSigmaLogBackup tblSigmaLogBackup, LoginUser loginUser){
        Date currentDate = new Date();    
        StringBuilder sql = new StringBuilder();
            
        sql.append("UPDATE TblSigmaLogBackup t ");
        sql.append(" SET t.procStatus = :procStatus");
        if(tblSigmaLogBackup.getProcStatus() == 3){
            sql.append(", t.reImportedDate = :currentDate");
        }
        else {
            sql.append(", t.reImportedDate = null ");
        }
        sql.append(", t.updateDate = :currentDate");
        sql.append(", t.updateUserUuid = :loginUser");
        sql.append(" WHERE");
        sql.append(" t.tblSigmaLogBackupPK.gunshiId = :gunshiId");
        sql.append(" AND");
        sql.append(" t.tblSigmaLogBackupPK.folderName = :folderName");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("procStatus", tblSigmaLogBackup.getProcStatus());
        if(tblSigmaLogBackup.getProcStatus() == 3){
            query.setParameter("currentDate", currentDate);
        }
        query.setParameter("currentDate", currentDate);
        query.setParameter("loginUser", loginUser.getUserUuid());
        query.setParameter("gunshiId", tblSigmaLogBackup.getGunshiId().toString());
        query.setParameter("folderName", tblSigmaLogBackup.getFolderName().toString());
        query.executeUpdate();
    }
    
    public boolean isRequested(String gunshiId){
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT t FROM TblSigmaLogBackup t WHERE t.procStatus = 1");
        
        if(gunshiId != null){
            jpql.append(" AND t.gunshiId = :gunshiId");
        }
        Query query = entityManager.createQuery(jpql.toString());
        if(gunshiId != null){
            query.setParameter("gunshiId", gunshiId);
        }
        List<TblSigmaLogBackup> list = query.getResultList();
        
        if (list.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }
    
}
