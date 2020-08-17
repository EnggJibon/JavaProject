/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.files;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
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
//@Transactional
public class TblUploadFileService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Transactional
    public void createTblUploadFile(TblUploadFile tblUploadFile) {
        entityManager.persist(tblUploadFile);
    }
    
    public TblUploadFile getTblUploadFile(String fileuuid) {
        Query query = entityManager.createNamedQuery("TblUploadFile.findByFileUuid");
        query.setParameter("fileUuid", fileuuid);
        try {
            TblUploadFile tblUploadFile = (TblUploadFile)query.getSingleResult();
            return tblUploadFile;
        }
        catch (NoResultException e) {
            return null;
        }
    }    
    
    /**
     * バッチでUploadFileデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @param companyId
     * @return
     */
    TblUploadFileList getExtUploadFilesByBatch(String docIds) {
        TblUploadFileList resList = new TblUploadFileList();
        if (null != docIds && !docIds.trim().equals("")) {
            String[] fileIds = docIds.split(",");

            StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblUploadFile t WHERE 1 = 1 ");
            if (null != fileIds && fileIds.length > 0) {
                sql.append(" and t.fileUuid in (");
                for (int i = 0; i < fileIds.length; i++) {
                    sql.append(":fileUuid").append(i);
                    if (i != fileIds.length - 1) {
                        sql.append(",");
                    }
                }
                sql.append(") ");
            }
            Query query = entityManager.createQuery(sql.toString());
            if (null != fileIds && fileIds.length > 0) {
                for (int i = 0; i < fileIds.length; i++) {
                    query.setParameter("fileUuid" + i, fileIds[i]);
                }
            }
            List<TblUploadFile> tmpList = query.getResultList();
            resList.setTblUploadFiles(tmpList);
        }
        return resList;
    }
    
    
    
    /**
     * バッチでUploadFileテーブルデータを更新
     *
     * @param files
     * @return
     */
    @Transactional
    public BasicResponse updateExtIssuesByBatch(List<TblUploadFile> files) {
        BasicResponse response = new BasicResponse();

        if (files != null && !files.isEmpty()) {
            for (TblUploadFile aFile : files) {
                List<TblUploadFile> oldUploadFiles = entityManager.createQuery("SELECT t FROM TblUploadFile t WHERE 1=1 and t.fileUuid=:fileUuid ")
                        .setParameter("fileUuid", aFile.getFileUuid())
                        .setMaxResults(1)
                        .getResultList();
                TblUploadFile newUploadFile;
                if (null != oldUploadFiles && !oldUploadFiles.isEmpty()) {
                    newUploadFile = oldUploadFiles.get(0);
                } else {
                    newUploadFile = new TblUploadFile();
                    newUploadFile.setFileUuid(aFile.getFileUuid());
                }
                newUploadFile.setFileType(aFile.getFileType());
                newUploadFile.setFunctionId(aFile.getFunctionId());                
                newUploadFile.setUploadDate(new Date());
                newUploadFile.setUploadFileName(aFile.getUploadFileName());
                if (null != oldUploadFiles && !oldUploadFiles.isEmpty()) {
                    entityManager.merge(newUploadFile);
                } else {
                    entityManager.persist(newUploadFile);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
