/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.fileimportjob;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.util.FileUtil;
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
    public void createTblUploadFile(TblUploadFile2 tblUploadFile) {
        entityManager.persist(tblUploadFile);
    }
    
    public TblUploadFile2 getTblUploadFile(String fileuuid) {
        Query query = entityManager.createNamedQuery("TblUploadFile.findByFileUuid");
        query.setParameter("fileUuid", fileuuid);
        try {
            TblUploadFile2 tblUploadFile = (TblUploadFile2)query.getSingleResult();
            return tblUploadFile;
        }
        catch (NoResultException e) {
            return null;
        }
    }    

    public TblUploadFileList getByFileTypeTblUploadFile(String filetype, String langId) {
        Query query = entityManager.createNamedQuery("importTblUploadFile.findByFileTypeOrderByUploadDate");
        query.setParameter("fileType", filetype);
        List list = query.getResultList();
        TblUploadFileList response = new TblUploadFileList();
        response.setTblUploadFile(list);
        return response;
    }


    public TblUploadFileList getByFileTypeUploadDateTblUploadFile(String langId, String uploaddate, String filetype) {
        FileUtil fu = new FileUtil();
        Date uploaddatetime = fu.getDateTimeParseForDate(uploaddate);
        Query query = entityManager.createNamedQuery("importTblUploadFile.findByFileTypeUploadDateOrderByUploadDate");
        query.setParameter("fileType", filetype);
        query.setParameter("uploadDate", uploaddatetime);
        List list = query.getResultList();
        TblUploadFileList response = new TblUploadFileList();
        response.setTblUploadFile(list);
        return response;
    }
}
