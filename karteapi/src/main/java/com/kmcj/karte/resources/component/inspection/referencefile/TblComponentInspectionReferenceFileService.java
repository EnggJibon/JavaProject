/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.referencefile;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFile;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFileName;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFileService;
import com.kmcj.karte.resources.component.inspection.referencefile.model.ComponentInspectionReferenceFile;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.IDGenerator;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * Component inspection reference file service
 * 
 * @author duanlin
 */
@Dependent
public class TblComponentInspectionReferenceFileService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;    
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private MstComponentInspectFileService mstComponentInspectFileService;
 
    /**
     * Get newest reference files
     * 
     * @param componentId
     * @return 
     */
    public ComponentInspectionReferenceFile getNewestReferenceFile(String componentId) {
        ComponentInspectionReferenceFile referenceFile = new ComponentInspectionReferenceFile();
        referenceFile.setComponentId(componentId);
        
        TblComponentInspectionReferenceFileNewest tblReferenceFileNewest = this.entityManager
                .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId", TblComponentInspectionReferenceFileNewest.class)
                .setParameter("componentId", componentId)
                .getResultList().stream().findFirst().orElse(null);
        
        if (tblReferenceFileNewest != null) {
            referenceFile.setDrawingFileUuid(tblReferenceFileNewest.getDrawingFileUuid());
            referenceFile.setProofFileUuid(tblReferenceFileNewest.getProofFileUuid());
            referenceFile.setRohsProofFileUuid(tblReferenceFileNewest.getRohsProofFileUuid());
            referenceFile.setPackageSpecFileUuid(tblReferenceFileNewest.getPackageSpecFileUuid());
            referenceFile.setQcPhaseFileUuid(tblReferenceFileNewest.getQcPhaseFileUuid());
            referenceFile.setExistFlag(Boolean.TRUE);
            referenceFile.setFile06FileUuid(tblReferenceFileNewest.getFile06Uuid());
            referenceFile.setFile07FileUuid(tblReferenceFileNewest.getFile07Uuid());
            referenceFile.setFile08FileUuid(tblReferenceFileNewest.getFile08Uuid());
            referenceFile.setFile09FileUuid(tblReferenceFileNewest.getFile09Uuid());
            referenceFile.setFile10FileUuid(tblReferenceFileNewest.getFile10Uuid());
            referenceFile.setFile11FileUuid(tblReferenceFileNewest.getFile11Uuid());
            referenceFile.setFile12FileUuid(tblReferenceFileNewest.getFile12Uuid());
            referenceFile.setFile13FileUuid(tblReferenceFileNewest.getFile13Uuid());
            referenceFile.setFile14FileUuid(tblReferenceFileNewest.getFile14Uuid());
            referenceFile.setFile15FileUuid(tblReferenceFileNewest.getFile15Uuid());
            referenceFile.setFile16FileUuid(tblReferenceFileNewest.getFile16Uuid());
            referenceFile.setFile17FileUuid(tblReferenceFileNewest.getFile17Uuid());
            referenceFile.setFile18FileUuid(tblReferenceFileNewest.getFile18Uuid());
            referenceFile.setFile19FileUuid(tblReferenceFileNewest.getFile19Uuid());
            referenceFile.setFile20FileUuid(tblReferenceFileNewest.getFile20Uuid());
        }
        return referenceFile;
    }
    
    /**
     * Get reference files
     * 
     * @param componentInspectionResultId
     * @param componentId
     * 
     * @return 
     */
    public ComponentInspectionReferenceFile getReferenceFile(String componentInspectionResultId, String componentId) {
        ComponentInspectionReferenceFile referenceFile = new ComponentInspectionReferenceFile();
        referenceFile.setComponentInspectionResultId(componentInspectionResultId);
        
        TblComponentInspectionReferenceFile tblReferenceFile = this.entityManager
                .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
                .setParameter("componentInspectionResultId", componentInspectionResultId)
                .getResultList().stream().findFirst().orElse(null);
        
        if (tblReferenceFile != null) {
            referenceFile.setDrawingFileUuid(tblReferenceFile.getDrawingFileUuid());
            referenceFile.setProofFileUuid(tblReferenceFile.getProofFileUuid());
            referenceFile.setRohsProofFileUuid(tblReferenceFile.getRohsProofFileUuid());
            referenceFile.setPackageSpecFileUuid(tblReferenceFile.getPackageSpecFileUuid());
            referenceFile.setQcPhaseFileUuid(tblReferenceFile.getQcPhaseFileUuid());
            referenceFile.setFile06FileUuid(tblReferenceFile.getFile06Uuid());
            referenceFile.setFile07FileUuid(tblReferenceFile.getFile07Uuid());
            referenceFile.setFile08FileUuid(tblReferenceFile.getFile08Uuid());
            referenceFile.setFile09FileUuid(tblReferenceFile.getFile09Uuid());
            referenceFile.setFile10FileUuid(tblReferenceFile.getFile10Uuid());
            referenceFile.setFile11FileUuid(tblReferenceFile.getFile11Uuid());
            referenceFile.setFile12FileUuid(tblReferenceFile.getFile12Uuid());
            referenceFile.setFile13FileUuid(tblReferenceFile.getFile13Uuid());
            referenceFile.setFile14FileUuid(tblReferenceFile.getFile14Uuid());
            referenceFile.setFile15FileUuid(tblReferenceFile.getFile15Uuid());
            referenceFile.setFile16FileUuid(tblReferenceFile.getFile16Uuid());
            referenceFile.setFile17FileUuid(tblReferenceFile.getFile17Uuid());
            referenceFile.setFile18FileUuid(tblReferenceFile.getFile18Uuid());
            referenceFile.setFile19FileUuid(tblReferenceFile.getFile19Uuid());
            referenceFile.setFile20FileUuid(tblReferenceFile.getFile20Uuid());

            referenceFile.setDrawingFileDate(tblReferenceFile.getDrawingFileDate());
            referenceFile.setProofFileDate(tblReferenceFile.getProofFileDate());
            referenceFile.setRohsProofFileDate(tblReferenceFile.getRohsProofFileDate());
            referenceFile.setPackageSpecFileDate(tblReferenceFile.getPackageSpecFileDate());
            referenceFile.setQcPhaseFileDate(tblReferenceFile.getQcPhaseFileDate());
            referenceFile.setFile06FileDate(tblReferenceFile.getFile06Date());
            referenceFile.setFile07FileDate(tblReferenceFile.getFile07Date());
            referenceFile.setFile08FileDate(tblReferenceFile.getFile08Date());
            referenceFile.setFile09FileDate(tblReferenceFile.getFile09Date());
            referenceFile.setFile10FileDate(tblReferenceFile.getFile10Date());
            referenceFile.setFile11FileDate(tblReferenceFile.getFile11Date());
            referenceFile.setFile12FileDate(tblReferenceFile.getFile12Date());
            referenceFile.setFile13FileDate(tblReferenceFile.getFile13Date());
            referenceFile.setFile14FileDate(tblReferenceFile.getFile14Date());
            referenceFile.setFile15FileDate(tblReferenceFile.getFile15Date());
            referenceFile.setFile16FileDate(tblReferenceFile.getFile16Date());
            referenceFile.setFile17FileDate(tblReferenceFile.getFile17Date());
            referenceFile.setFile18FileDate(tblReferenceFile.getFile18Date());
            referenceFile.setFile19FileDate(tblReferenceFile.getFile19Date());
            referenceFile.setFile20FileDate(tblReferenceFile.getFile20Date());

            referenceFile.setDrawingFileStatus(tblReferenceFile.getDrawingFileStatus());
            referenceFile.setProofFileStatus(tblReferenceFile.getProofFileStatus());
            referenceFile.setRohsProofFileStatus(tblReferenceFile.getRohsProofFileStatus());
            referenceFile.setPackageSpecFileStatus(tblReferenceFile.getPackageSpecFileStatus());
            referenceFile.setQcPhaseFileStatus(tblReferenceFile.getQcPhaseFileStatus());
            referenceFile.setFile06FileStatus(tblReferenceFile.getFile06Status());
            referenceFile.setFile07FileStatus(tblReferenceFile.getFile07Status());
            referenceFile.setFile08FileStatus(tblReferenceFile.getFile08Status());
            referenceFile.setFile09FileStatus(tblReferenceFile.getFile09Status());
            referenceFile.setFile10FileStatus(tblReferenceFile.getFile10Status());
            referenceFile.setFile11FileStatus(tblReferenceFile.getFile11Status());
            referenceFile.setFile12FileStatus(tblReferenceFile.getFile12Status());
            referenceFile.setFile13FileStatus(tblReferenceFile.getFile13Status());
            referenceFile.setFile14FileStatus(tblReferenceFile.getFile14Status());
            referenceFile.setFile15FileStatus(tblReferenceFile.getFile15Status());
            referenceFile.setFile16FileStatus(tblReferenceFile.getFile16Status());
            referenceFile.setFile17FileStatus(tblReferenceFile.getFile17Status());
            referenceFile.setFile18FileStatus(tblReferenceFile.getFile18Status());
            referenceFile.setFile19FileStatus(tblReferenceFile.getFile19Status());
            referenceFile.setFile20FileStatus(tblReferenceFile.getFile20Status());
            
            // km-976 帳票確認者の検索を追加 20181122 start
            referenceFile.setDrawingFileConfirmerId(tblReferenceFile.getDrawingFileConfirmerId());
            referenceFile.setDrawingFileConfirmerName(tblReferenceFile.getDrawingFileConfirmerName());
            referenceFile.setProofFileConfirmerId(tblReferenceFile.getProofFileConfirmerId());
            referenceFile.setProofFileConfirmerName(tblReferenceFile.getProofFileConfirmerName());
            referenceFile.setRohsProofFileConfirmerId(tblReferenceFile.getRohsProofFileConfirmerId());
            referenceFile.setRohsProofFileConfirmerName(tblReferenceFile.getRohsProofFileConfirmerName());
            referenceFile.setPackageSpecFileConfirmerId(tblReferenceFile.getPackageSpecFileConfirmerId());
            referenceFile.setPackageSpecFileConfirmerName(tblReferenceFile.getPackageSpecFileConfirmerName());
            referenceFile.setQcPhaseFileConfirmerId(tblReferenceFile.getQcPhaseFileConfirmerId());
            referenceFile.setQcPhaseFileConfirmerName(tblReferenceFile.getQcPhaseFileConfirmerName());
            referenceFile.setFile06FileConfirmerId(tblReferenceFile.getFile06ConfirmerId());
            referenceFile.setFile06FileConfirmerName(tblReferenceFile.getFile06ConfirmerName());
            referenceFile.setFile07FileConfirmerId(tblReferenceFile.getFile07ConfirmerId());
            referenceFile.setFile07FileConfirmerName(tblReferenceFile.getFile07ConfirmerName());
            referenceFile.setFile08FileConfirmerId(tblReferenceFile.getFile08ConfirmerId());
            referenceFile.setFile08FileConfirmerName(tblReferenceFile.getFile08ConfirmerName());
            referenceFile.setFile09FileConfirmerId(tblReferenceFile.getFile09ConfirmerId());
            referenceFile.setFile09FileConfirmerName(tblReferenceFile.getFile09ConfirmerName());
            referenceFile.setFile10FileConfirmerId(tblReferenceFile.getFile10ConfirmerId());
            referenceFile.setFile10FileConfirmerName(tblReferenceFile.getFile10ConfirmerName());
            referenceFile.setFile11FileConfirmerId(tblReferenceFile.getFile11ConfirmerId());
            referenceFile.setFile11FileConfirmerName(tblReferenceFile.getFile11ConfirmerName());
            referenceFile.setFile12FileConfirmerId(tblReferenceFile.getFile12ConfirmerId());
            referenceFile.setFile12FileConfirmerName(tblReferenceFile.getFile12ConfirmerName());
            referenceFile.setFile13FileConfirmerId(tblReferenceFile.getFile13ConfirmerId());
            referenceFile.setFile13FileConfirmerName(tblReferenceFile.getFile13ConfirmerName());
            referenceFile.setFile14FileConfirmerId(tblReferenceFile.getFile14ConfirmerId());
            referenceFile.setFile14FileConfirmerName(tblReferenceFile.getFile14ConfirmerName());
            referenceFile.setFile15FileConfirmerId(tblReferenceFile.getFile15ConfirmerId());
            referenceFile.setFile15FileConfirmerName(tblReferenceFile.getFile15ConfirmerName());
            referenceFile.setFile16FileConfirmerId(tblReferenceFile.getFile16ConfirmerId());
            referenceFile.setFile16FileConfirmerName(tblReferenceFile.getFile16ConfirmerName());
            referenceFile.setFile17FileConfirmerId(tblReferenceFile.getFile17ConfirmerId());
            referenceFile.setFile17FileConfirmerName(tblReferenceFile.getFile17ConfirmerName());
            referenceFile.setFile18FileConfirmerId(tblReferenceFile.getFile18ConfirmerId());
            referenceFile.setFile18FileConfirmerName(tblReferenceFile.getFile18ConfirmerName());
            referenceFile.setFile19FileConfirmerId(tblReferenceFile.getFile19ConfirmerId());
            referenceFile.setFile19FileConfirmerName(tblReferenceFile.getFile19ConfirmerName());
            referenceFile.setFile20FileConfirmerId(tblReferenceFile.getFile20ConfirmerId());
            referenceFile.setFile20FileConfirmerName(tblReferenceFile.getFile20ConfirmerName());

            return referenceFile;
        }
        
        TblComponentInspectionReferenceFileNewest tblReferenceFileNewest = this.entityManager
                .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId", TblComponentInspectionReferenceFileNewest.class)
                .setParameter("componentId", componentId)
                .getResultList().stream().findFirst().orElse(null);
        if (tblReferenceFileNewest != null) {
            referenceFile.setDrawingFileUuid(tblReferenceFileNewest.getDrawingFileUuid());
            referenceFile.setProofFileUuid(tblReferenceFileNewest.getProofFileUuid());
            referenceFile.setRohsProofFileUuid(tblReferenceFileNewest.getRohsProofFileUuid());
            referenceFile.setPackageSpecFileUuid(tblReferenceFileNewest.getPackageSpecFileUuid());
            referenceFile.setQcPhaseFileUuid(tblReferenceFileNewest.getQcPhaseFileUuid());
            referenceFile.setFile06FileUuid(tblReferenceFileNewest.getFile06Uuid());
            referenceFile.setFile07FileUuid(tblReferenceFileNewest.getFile07Uuid());
            referenceFile.setFile08FileUuid(tblReferenceFileNewest.getFile08Uuid());
            referenceFile.setFile09FileUuid(tblReferenceFileNewest.getFile09Uuid());
            referenceFile.setFile10FileUuid(tblReferenceFileNewest.getFile10Uuid());
            referenceFile.setFile11FileUuid(tblReferenceFileNewest.getFile11Uuid());
            referenceFile.setFile12FileUuid(tblReferenceFileNewest.getFile12Uuid());
            referenceFile.setFile13FileUuid(tblReferenceFileNewest.getFile13Uuid());
            referenceFile.setFile14FileUuid(tblReferenceFileNewest.getFile14Uuid());
            referenceFile.setFile15FileUuid(tblReferenceFileNewest.getFile15Uuid());
            referenceFile.setFile16FileUuid(tblReferenceFileNewest.getFile16Uuid());
            referenceFile.setFile17FileUuid(tblReferenceFileNewest.getFile17Uuid());
            referenceFile.setFile18FileUuid(tblReferenceFileNewest.getFile18Uuid());
            referenceFile.setFile19FileUuid(tblReferenceFileNewest.getFile19Uuid());
            referenceFile.setFile20FileUuid(tblReferenceFileNewest.getFile20Uuid());
        }
        return referenceFile;
    }
    
    /**
     * 最新部品検査参照ファイル更新
     * 
     * @param input
     * @param userUuid 
     */
    @Transactional
    public void updateFileNewest(ComponentInspectionReferenceFile input, String userUuid) {
        TblComponentInspectionReferenceFileNewest tblReferenceFileNewest = this.entityManager
                .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId", TblComponentInspectionReferenceFileNewest.class)
                .setParameter("componentId", input.getComponentId())
                .getResultList().stream().findFirst().orElse(null);
        
        Date sysDate = new Date();
        if (tblReferenceFileNewest == null) {
            tblReferenceFileNewest = new TblComponentInspectionReferenceFileNewest();
            
            tblReferenceFileNewest.setId(IDGenerator.generate());
            tblReferenceFileNewest.setComponentId(input.getComponentId());
            tblReferenceFileNewest.setDrawingFileUuid(input.getDrawingFileUuid());
            tblReferenceFileNewest.setProofFileUuid(input.getProofFileUuid());
            tblReferenceFileNewest.setRohsProofFileUuid(input.getRohsProofFileUuid());
            tblReferenceFileNewest.setPackageSpecFileUuid(input.getPackageSpecFileUuid());
            tblReferenceFileNewest.setQcPhaseFileUuid(input.getQcPhaseFileUuid());
            tblReferenceFileNewest.setCreateDate(sysDate);
            tblReferenceFileNewest.setCreateUserUuid(userUuid);
            tblReferenceFileNewest.setUpdateDate(sysDate);
            tblReferenceFileNewest.setUpdateUserUuid(userUuid);
            tblReferenceFileNewest.setFile06Uuid(input.getFile06FileUuid());
            tblReferenceFileNewest.setFile07Uuid(input.getFile07FileUuid());
            tblReferenceFileNewest.setFile08Uuid(input.getFile08FileUuid());
            tblReferenceFileNewest.setFile09Uuid(input.getFile09FileUuid());
            tblReferenceFileNewest.setFile10Uuid(input.getFile10FileUuid());
            tblReferenceFileNewest.setFile11Uuid(input.getFile11FileUuid());
            tblReferenceFileNewest.setFile12Uuid(input.getFile12FileUuid());
            tblReferenceFileNewest.setFile13Uuid(input.getFile13FileUuid());
            tblReferenceFileNewest.setFile14Uuid(input.getFile14FileUuid());
            tblReferenceFileNewest.setFile15Uuid(input.getFile15FileUuid());
            tblReferenceFileNewest.setFile16Uuid(input.getFile16FileUuid());
            tblReferenceFileNewest.setFile17Uuid(input.getFile17FileUuid());
            tblReferenceFileNewest.setFile18Uuid(input.getFile18FileUuid());
            tblReferenceFileNewest.setFile19Uuid(input.getFile19FileUuid());
            tblReferenceFileNewest.setFile20Uuid(input.getFile20FileUuid());
            this.entityManager.persist(tblReferenceFileNewest);
        } else {
            tblReferenceFileNewest.setDrawingFileUuid(input.getDrawingFileUuid());
            tblReferenceFileNewest.setProofFileUuid(input.getProofFileUuid());
            tblReferenceFileNewest.setRohsProofFileUuid(input.getRohsProofFileUuid());
            tblReferenceFileNewest.setPackageSpecFileUuid(input.getPackageSpecFileUuid());
            tblReferenceFileNewest.setQcPhaseFileUuid(input.getQcPhaseFileUuid());
            tblReferenceFileNewest.setUpdateDate(sysDate);
            tblReferenceFileNewest.setUpdateUserUuid(userUuid);
            tblReferenceFileNewest.setFile06Uuid(input.getFile06FileUuid());
            tblReferenceFileNewest.setFile07Uuid(input.getFile07FileUuid());
            tblReferenceFileNewest.setFile08Uuid(input.getFile08FileUuid());
            tblReferenceFileNewest.setFile09Uuid(input.getFile09FileUuid());
            tblReferenceFileNewest.setFile10Uuid(input.getFile10FileUuid());
            tblReferenceFileNewest.setFile11Uuid(input.getFile11FileUuid());
            tblReferenceFileNewest.setFile12Uuid(input.getFile12FileUuid());
            tblReferenceFileNewest.setFile13Uuid(input.getFile13FileUuid());
            tblReferenceFileNewest.setFile14Uuid(input.getFile14FileUuid());
            tblReferenceFileNewest.setFile15Uuid(input.getFile15FileUuid());
            tblReferenceFileNewest.setFile16Uuid(input.getFile16FileUuid());
            tblReferenceFileNewest.setFile17Uuid(input.getFile17FileUuid());
            tblReferenceFileNewest.setFile18Uuid(input.getFile18FileUuid());
            tblReferenceFileNewest.setFile19Uuid(input.getFile19FileUuid());
            tblReferenceFileNewest.setFile20Uuid(input.getFile20FileUuid());
            this.entityManager.merge(tblReferenceFileNewest);
        }
    }
    
    /**
     * 部品検査別参照ファイル追加
     * 
     * @param inputInfo
     * @param userUuid 
     */
    @Transactional
    public void createFile(ComponentInspectionReferenceFile inputInfo, String userUuid) {
        
        Date sysDate = new Date();
        TblComponentInspectionReferenceFile tblComponentInspectionReferenceFile = new TblComponentInspectionReferenceFile();
        tblComponentInspectionReferenceFile.setId(IDGenerator.generate());
        tblComponentInspectionReferenceFile.setComponentInspectionResultId(inputInfo.getComponentInspectionResultId());
        tblComponentInspectionReferenceFile.setDrawingFileUuid(inputInfo.getDrawingFileUuid());
        tblComponentInspectionReferenceFile.setDrawingFileDate(sysDate);
        tblComponentInspectionReferenceFile.setProofFileUuid(inputInfo.getProofFileUuid());
        tblComponentInspectionReferenceFile.setProofFileDate(sysDate);
        tblComponentInspectionReferenceFile.setRohsProofFileUuid(inputInfo.getRohsProofFileUuid());
        tblComponentInspectionReferenceFile.setRohsProofFileDate(sysDate);
        tblComponentInspectionReferenceFile.setPackageSpecFileUuid(inputInfo.getPackageSpecFileUuid());
        tblComponentInspectionReferenceFile.setPackageSpecFileDate(sysDate);
        tblComponentInspectionReferenceFile.setQcPhaseFileUuid(inputInfo.getQcPhaseFileUuid());
        tblComponentInspectionReferenceFile.setQcPhaseFileDate(sysDate);
        tblComponentInspectionReferenceFile.setCreateDate(sysDate);
        tblComponentInspectionReferenceFile.setCreateUserUuid(userUuid);

        entityManager.persist(tblComponentInspectionReferenceFile);
    }
    
    /**
     * 部品検査別参照ファイル追加更新
     * 
     * @param input
     * @param userUuid 
     */
    @Transactional
    public void updateFile(ComponentInspectionReferenceFile input, String userUuid) {
        TblComponentInspectionReferenceFile refFile = this.entityManager
                .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
                .setParameter("componentInspectionResultId", input.getComponentInspectionResultId())
                .getResultList().stream().findFirst().orElse(null);
       
        Date sysDate = new Date();
        if (refFile == null) {
           createFile(input,userUuid);
        } else {
            // km-976 帳票確認者の検索を追加 20181121 start
            String userName = getUserName(userUuid);
            // km-976 帳票確認者の検索を追加 20181121 end
            if (refFile.getDrawingFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getDrawingFileUuid())) {
                    if (input.getDrawingFileUuid().trim().equals(refFile.getDrawingFileUuid())) {
                        refFile.setDrawingFileStatus(input.getDrawingFileStatus());
                    } else {
                        refFile.setDrawingFileUuid(input.getDrawingFileUuid().trim()); //図面ファイルUUID
                        refFile.setDrawingFileDate(sysDate);
                        refFile.setDrawingFileStatus(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setDrawingFileConfirmerId(userUuid);
                        refFile.setDrawingFileConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getDrawingFileStatus())) {
                    refFile.setDrawingFileStatus(input.getDrawingFileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setDrawingFileConfirmerId(userUuid);
                        refFile.setDrawingFileConfirmerName(userName);
                    }
                }
            }
            if (refFile.getProofFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getProofFileUuid())) {
                    if (input.getProofFileUuid().trim().equals(refFile.getProofFileUuid())) {
                        refFile.setProofFileStatus(input.getProofFileStatus());
                    } else {
                        refFile.setProofFileUuid(input.getProofFileUuid().trim()); //材料証明ファイルUUID
                        refFile.setProofFileDate(sysDate);
                        refFile.setProofFileStatus(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setProofFileConfirmerId(userUuid);
                        refFile.setProofFileConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getProofFileStatus())) {
                    refFile.setProofFileStatus(input.getProofFileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setProofFileConfirmerId(userUuid);
                        refFile.setProofFileConfirmerName(userName);
                    }
                }
            }
            if (refFile.getRohsProofFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getRohsProofFileUuid())) {
                    if (input.getRohsProofFileUuid().trim().equals(refFile.getRohsProofFileUuid())) {
                        refFile.setRohsProofFileStatus(input.getRohsProofFileStatus());
                    } else {
                        refFile.setRohsProofFileUuid(input.getRohsProofFileUuid().trim()); //RoHS適用証明ファイルUUID
                        refFile.setRohsProofFileDate(sysDate);
                        refFile.setRohsProofFileStatus(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setRohsProofFileConfirmerId(userUuid);
                        refFile.setRohsProofFileConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getRohsProofFileStatus())) {
                    refFile.setRohsProofFileStatus(input.getRohsProofFileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setRohsProofFileConfirmerId(userUuid);
                        refFile.setRohsProofFileConfirmerName(userName);
                    }
                }
            }
            if (refFile.getPackageSpecFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getPackageSpecFileUuid())) {
                    if (input.getPackageSpecFileUuid().trim().equals(refFile.getPackageSpecFileUuid())) {
                        refFile.setPackageSpecFileStatus(input.getPackageSpecFileStatus());
                    } else {
                        refFile.setPackageSpecFileUuid(input.getPackageSpecFileUuid().trim()); //包装仕様書ファイルUUID
                        refFile.setPackageSpecFileDate(sysDate);
                        refFile.setPackageSpecFileStatus(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setPackageSpecFileConfirmerId(userUuid);
                        refFile.setPackageSpecFileConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getPackageSpecFileStatus())) {
                    refFile.setPackageSpecFileStatus(input.getPackageSpecFileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setPackageSpecFileConfirmerId(userUuid);
                        refFile.setPackageSpecFileConfirmerName(userName);
                    }
                }
            }
            if (refFile.getQcPhaseFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getQcPhaseFileUuid())) {
                    if (input.getQcPhaseFileUuid().trim().equals(refFile.getQcPhaseFileUuid())) {
                        refFile.setQcPhaseFileStatus(input.getQcPhaseFileStatus());
                    } else {
                        refFile.setQcPhaseFileUuid(input.getQcPhaseFileUuid().trim()); //QC工程表ファイルUUID
                        refFile.setQcPhaseFileDate(sysDate);
                        refFile.setQcPhaseFileStatus(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setQcPhaseFileConfirmerId(userUuid);
                        refFile.setQcPhaseFileConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getQcPhaseFileStatus())) {
                    refFile.setQcPhaseFileStatus(input.getQcPhaseFileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setQcPhaseFileConfirmerId(userUuid);
                        refFile.setQcPhaseFileConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile06Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile06FileUuid())) {
                    if (input.getFile06FileUuid().trim().equals(refFile.getFile06Uuid())) {
                        refFile.setFile06Status(input.getFile06FileStatus());
                    } else {
                        refFile.setFile06Uuid(input.getFile06FileUuid().trim()); //QC工程表ファイルUUID
                        refFile.setFile06Date(sysDate);
                        refFile.setFile06Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile06ConfirmerId(userUuid);
                        refFile.setFile06ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile06FileStatus())) {
                    refFile.setFile06Status(input.getFile06FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile06ConfirmerId(userUuid);
                        refFile.setFile06ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile07Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile07FileUuid())) {
                    if (input.getFile07FileUuid().trim().equals(refFile.getFile07Uuid())) {
                        refFile.setFile07Status(input.getFile07FileStatus());
                    } else {
                        refFile.setFile07Uuid(input.getFile07FileUuid().trim()); //QC工程表ファイルUUID
                        refFile.setFile07Date(sysDate);
                        refFile.setFile07Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile07ConfirmerId(userUuid);
                        refFile.setFile07ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile07FileStatus())) {
                    refFile.setFile07Status(input.getFile07FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile07ConfirmerId(userUuid);
                        refFile.setFile07ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile08Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile08FileUuid())) {
                    if (input.getFile08FileUuid().trim().equals(refFile.getFile08Uuid())) {
                        refFile.setFile08Status(input.getFile08FileStatus());
                    } else {
                        refFile.setFile08Uuid(input.getFile08FileUuid().trim()); //QC工程表ファイルUUID
                        refFile.setFile08Date(sysDate);
                        refFile.setFile08Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile08ConfirmerId(userUuid);
                        refFile.setFile08ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile08FileStatus())) {
                    refFile.setFile08Status(input.getFile08FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile08ConfirmerId(userUuid);
                        refFile.setFile08ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile09Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile09FileUuid())) {
                    if (input.getFile09FileUuid().trim().equals(refFile.getFile09Uuid())) {
                        refFile.setFile09Status(input.getFile09FileStatus());
                    } else {
                        refFile.setFile09Uuid(input.getFile09FileUuid().trim()); //QC工程表ファイルUUID
                        refFile.setFile09Date(sysDate);
                        refFile.setFile09Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile09ConfirmerId(userUuid);
                        refFile.setFile09ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile09FileStatus())) {
                    refFile.setFile09Status(input.getFile09FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile09ConfirmerId(userUuid);
                        refFile.setFile09ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile10Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile10FileUuid())) {
                    if (input.getFile10FileUuid().trim().equals(refFile.getFile10Uuid())) {
                        refFile.setFile10Status(input.getFile10FileStatus());
                    } else {
                        refFile.setFile10Uuid(input.getFile10FileUuid().trim()); //QC工程表ファイルUUID
                        refFile.setFile10Date(sysDate);
                        refFile.setFile10Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile10ConfirmerId(userUuid);
                        refFile.setFile10ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile10FileStatus())) {
                    refFile.setFile10Status(input.getFile10FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile10ConfirmerId(userUuid);
                        refFile.setFile10ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile11Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile11FileUuid())) {
                    if (input.getFile11FileUuid().trim().equals(refFile.getFile11Uuid())) {
                        refFile.setFile11Status(input.getFile11FileStatus());
                    } else {
                        refFile.setFile11Uuid(input.getFile11FileUuid().trim()); //QC工程表ファイルUUID
                        refFile.setFile11Date(sysDate);
                        refFile.setFile11Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile11ConfirmerId(userUuid);
                        refFile.setFile11ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile11FileStatus())) {
                    refFile.setFile11Status(input.getFile11FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile11ConfirmerId(userUuid);
                        refFile.setFile11ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile12Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile12FileUuid())) {
                    if (input.getFile12FileUuid().trim().equals(refFile.getFile12Uuid())) {
                        refFile.setFile12Status(input.getFile12FileStatus());
                    } else {
                        refFile.setFile12Uuid(input.getFile12FileUuid().trim()); //QC工程表ファイルUUID
                        refFile.setFile12Date(sysDate);
                        refFile.setFile12Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile12ConfirmerId(userUuid);
                        refFile.setFile12ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile12FileStatus())) {
                    refFile.setFile12Status(input.getFile12FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile12ConfirmerId(userUuid);
                        refFile.setFile12ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile13Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile13FileUuid())) {
                    if (input.getFile13FileUuid().trim().equals(refFile.getFile13Uuid())) {
                        refFile.setFile13Status(input.getFile13FileStatus());
                    } else {
                        refFile.setFile13Uuid(input.getFile13FileUuid().trim()); //QC工程表ファイルUUID
                        refFile.setFile13Date(sysDate);
                        refFile.setFile13Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile13ConfirmerId(userUuid);
                        refFile.setFile13ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile13FileStatus())) {
                    refFile.setFile13Status(input.getFile13FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile13ConfirmerId(userUuid);
                        refFile.setFile13ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile14Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile14FileUuid())) {
                    if (input.getFile14FileUuid().trim().equals(refFile.getFile14Uuid())) {
                        refFile.setFile14Status(input.getFile14FileStatus());
                    } else {
                        refFile.setFile14Uuid(input.getFile14FileUuid().trim());
                        refFile.setFile14Date(sysDate);
                        refFile.setFile14Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile14ConfirmerId(userUuid);
                        refFile.setFile14ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile14FileStatus())) {
                    refFile.setFile14Status(input.getFile14FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile14ConfirmerId(userUuid);
                        refFile.setFile14ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile15Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile15FileUuid())) {
                    if (input.getFile15FileUuid().trim().equals(refFile.getFile15Uuid())) {
                        refFile.setFile15Status(input.getFile15FileStatus());
                    } else {
                        refFile.setFile15Uuid(input.getFile15FileUuid().trim());
                        refFile.setFile15Date(sysDate);
                        refFile.setFile15Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile15ConfirmerId(userUuid);
                        refFile.setFile15ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile15FileStatus())) {
                    refFile.setFile15Status(input.getFile15FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile15ConfirmerId(userUuid);
                        refFile.setFile15ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile16Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile16FileUuid())) {
                    if (input.getFile16FileUuid().trim().equals(refFile.getFile16Uuid())) {
                        refFile.setFile16Status(input.getFile16FileStatus());
                    } else {
                        refFile.setFile16Uuid(input.getFile16FileUuid().trim());
                        refFile.setFile16Date(sysDate);
                        refFile.setFile16Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile16ConfirmerId(userUuid);
                        refFile.setFile16ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile16FileStatus())) {
                    refFile.setFile16Status(input.getFile16FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile16ConfirmerId(userUuid);
                        refFile.setFile16ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile17Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile17FileUuid())) {
                    if (input.getFile17FileUuid().trim().equals(refFile.getFile17Uuid())) {
                        refFile.setFile17Status(input.getFile17FileStatus());
                    } else {
                        refFile.setFile17Uuid(input.getFile17FileUuid().trim());
                        refFile.setFile17Date(sysDate);
                        refFile.setFile17Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile17ConfirmerId(userUuid);
                        refFile.setFile17ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile17FileStatus())) {
                    refFile.setFile17Status(input.getFile17FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile17ConfirmerId(userUuid);
                        refFile.setFile17ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile18Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile18FileUuid())) {
                    if (input.getFile18FileUuid().trim().equals(refFile.getFile18Uuid())) {
                        refFile.setFile18Status(input.getFile18FileStatus());
                    } else {
                        refFile.setFile18Uuid(input.getFile18FileUuid().trim());
                        refFile.setFile18Date(sysDate);
                        refFile.setFile18Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile18ConfirmerId(userUuid);
                        refFile.setFile18ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile18FileStatus())) {
                    refFile.setFile18Status(input.getFile18FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile18ConfirmerId(userUuid);
                        refFile.setFile18ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile19Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile19FileUuid())) {
                    if (input.getFile19FileUuid().trim().equals(refFile.getFile19Uuid())) {
                        refFile.setFile19Status(input.getFile19FileStatus());
                    } else {
                        refFile.setFile19Uuid(input.getFile19FileUuid().trim());
                        refFile.setFile19Date(sysDate);
                        refFile.setFile19Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile19ConfirmerId(userUuid);
                        refFile.setFile19ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile19FileStatus())) {
                    refFile.setFile19Status(input.getFile19FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile19ConfirmerId(userUuid);
                        refFile.setFile19ConfirmerName(userName);
                    }
                }
            }
            if (refFile.getFile20Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                if (StringUtils.isNotEmpty(input.getFile20FileUuid())) {
                    if (input.getFile20FileUuid().trim().equals(refFile.getFile20Uuid())) {
                        refFile.setFile20Status(input.getFile20FileStatus());
                    } else {
                        refFile.setFile20Uuid(input.getFile20FileUuid().trim());
                        refFile.setFile20Date(sysDate);
                        refFile.setFile20Status(TblComponentInspectionReferenceFile.FILE_STATUS_DEFAULT);
                    }
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile20ConfirmerId(userUuid);
                        refFile.setFile20ConfirmerName(userName);
                    }
                } else if (StringUtils.isNotEmpty(input.getFile20FileStatus())) {
                    refFile.setFile20Status(input.getFile20FileStatus());
                    if (CommonConstants.INSPECTION_TYPE_OUTGOING != input.getInspectionType()) {
                        refFile.setFile20ConfirmerId(userUuid);
                        refFile.setFile20ConfirmerName(userName);
                    }
                }
            }
            refFile.setUpdateDate(sysDate);
            refFile.setUpdateUserUuid(userUuid);
            this.entityManager.merge(refFile);
        }
    }
    
    /**
     * 删除部品検査別参照ファイル
     * 
     * @param componentInspectionResultId
     * @param langId
     * @return 
     */
    @Transactional
    public BasicResponse deleteFile(String componentInspectionResultId, String langId) {
        BasicResponse basicResponse = new BasicResponse();
        Query query = entityManager.createNamedQuery("TblComponentInspectionReferenceFile.deleteByComponentInspectionResultId");
        query.setParameter("componentInspectionResultId", componentInspectionResultId);
        int count = query.executeUpdate();
        if (count > 0) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_record_deleted"));
        }
        return basicResponse;
    }
    
    /**
     * Copy form TblComponentInspectionReferenceFileNewest to TblComponentInspectionReferenceFile
     * 
     * @param componentId
     * @param componentInspectionResultId
     * @param userUuid 
     */
    @Transactional
    public void copy(String componentId, String componentInspectionResultId, String userUuid) {
        TblComponentInspectionReferenceFileNewest tblReferenceFileNewest = this.entityManager
                .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId", TblComponentInspectionReferenceFileNewest.class)
                .setParameter("componentId", componentId)
                .getResultList().stream().findFirst().orElse(null);

        if (tblReferenceFileNewest == null) {
            return;
        }
        
        TblComponentInspectionReferenceFile tblReferenceFile = this.entityManager
                .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
                .setParameter("componentInspectionResultId", componentInspectionResultId)
                .getResultList().stream().findFirst().orElse(null);
        Date sysDate = new Date();
        if (tblReferenceFile == null) {
            tblReferenceFile= new TblComponentInspectionReferenceFile();
            tblReferenceFile.setId(IDGenerator.generate());
            tblReferenceFile.setComponentInspectionResultId(componentInspectionResultId);
            tblReferenceFile.setDrawingFileUuid(tblReferenceFileNewest.getDrawingFileUuid());
            tblReferenceFile.setProofFileUuid(tblReferenceFileNewest.getProofFileUuid());
            tblReferenceFile.setRohsProofFileUuid(tblReferenceFileNewest.getRohsProofFileUuid());
            tblReferenceFile.setPackageSpecFileUuid(tblReferenceFileNewest.getPackageSpecFileUuid());
            tblReferenceFile.setQcPhaseFileUuid(tblReferenceFileNewest.getQcPhaseFileUuid());
            tblReferenceFile.setCreateDate(sysDate);
            tblReferenceFile.setCreateUserUuid(userUuid);
            tblReferenceFile.setUpdateDate(sysDate);
            tblReferenceFile.setUpdateUserUuid(userUuid);
            tblReferenceFile.setFile06Uuid(tblReferenceFileNewest.getFile06Uuid());
            tblReferenceFile.setFile07Uuid(tblReferenceFileNewest.getFile07Uuid());
            tblReferenceFile.setFile08Uuid(tblReferenceFileNewest.getFile08Uuid());
            tblReferenceFile.setFile09Uuid(tblReferenceFileNewest.getFile09Uuid());
            tblReferenceFile.setFile10Uuid(tblReferenceFileNewest.getFile10Uuid());
            tblReferenceFile.setFile11Uuid(tblReferenceFileNewest.getFile11Uuid());
            tblReferenceFile.setFile12Uuid(tblReferenceFileNewest.getFile12Uuid());
            tblReferenceFile.setFile13Uuid(tblReferenceFileNewest.getFile13Uuid());
            tblReferenceFile.setFile14Uuid(tblReferenceFileNewest.getFile14Uuid());
            tblReferenceFile.setFile15Uuid(tblReferenceFileNewest.getFile15Uuid());
            tblReferenceFile.setFile16Uuid(tblReferenceFileNewest.getFile16Uuid());
            tblReferenceFile.setFile17Uuid(tblReferenceFileNewest.getFile17Uuid());
            tblReferenceFile.setFile18Uuid(tblReferenceFileNewest.getFile18Uuid());
            tblReferenceFile.setFile19Uuid(tblReferenceFileNewest.getFile19Uuid());
            tblReferenceFile.setFile20Uuid(tblReferenceFileNewest.getFile20Uuid());
            this.entityManager.persist(tblReferenceFile);
        } else {
            tblReferenceFile.setDrawingFileUuid(tblReferenceFileNewest.getDrawingFileUuid());
            tblReferenceFile.setProofFileUuid(tblReferenceFileNewest.getProofFileUuid());
            tblReferenceFile.setRohsProofFileUuid(tblReferenceFileNewest.getRohsProofFileUuid());
            tblReferenceFile.setPackageSpecFileUuid(tblReferenceFileNewest.getPackageSpecFileUuid());
            tblReferenceFile.setQcPhaseFileUuid(tblReferenceFileNewest.getQcPhaseFileUuid());
            tblReferenceFile.setUpdateDate(sysDate);
            tblReferenceFile.setUpdateUserUuid(userUuid);
            tblReferenceFile.setFile06Uuid(tblReferenceFileNewest.getFile06Uuid());
            tblReferenceFile.setFile07Uuid(tblReferenceFileNewest.getFile07Uuid());
            tblReferenceFile.setFile08Uuid(tblReferenceFileNewest.getFile08Uuid());
            tblReferenceFile.setFile09Uuid(tblReferenceFileNewest.getFile09Uuid());
            tblReferenceFile.setFile10Uuid(tblReferenceFileNewest.getFile10Uuid());
            tblReferenceFile.setFile11Uuid(tblReferenceFileNewest.getFile11Uuid());
            tblReferenceFile.setFile12Uuid(tblReferenceFileNewest.getFile12Uuid());
            tblReferenceFile.setFile13Uuid(tblReferenceFileNewest.getFile13Uuid());
            tblReferenceFile.setFile14Uuid(tblReferenceFileNewest.getFile14Uuid());
            tblReferenceFile.setFile15Uuid(tblReferenceFileNewest.getFile15Uuid());
            tblReferenceFile.setFile16Uuid(tblReferenceFileNewest.getFile16Uuid());
            tblReferenceFile.setFile17Uuid(tblReferenceFileNewest.getFile17Uuid());
            tblReferenceFile.setFile18Uuid(tblReferenceFileNewest.getFile18Uuid());
            tblReferenceFile.setFile19Uuid(tblReferenceFileNewest.getFile19Uuid());
            tblReferenceFile.setFile20Uuid(tblReferenceFileNewest.getFile20Uuid());
            this.entityManager.merge(tblReferenceFile);
        }
    }
    
    @Transactional
    public void copyFile(String componentId, String componentInspectionResultId, MstComponentInspectFile inspFile, String userUuid, String incomingCompanyId) {
        TblComponentInspectionReferenceFileNewest tblReferenceFileNewest = this.entityManager
                .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId", TblComponentInspectionReferenceFileNewest.class)
                .setParameter("componentId", componentId)
                .getResultList().stream().findFirst().orElse(null);
        Date sysDate = new Date();
        
        if (tblReferenceFileNewest == null) {
            tblReferenceFileNewest = new TblComponentInspectionReferenceFileNewest();
            
            tblReferenceFileNewest.setId(IDGenerator.generate());
            tblReferenceFileNewest.setComponentId(componentId);
            tblReferenceFileNewest.setCreateDate(sysDate);
            tblReferenceFileNewest.setCreateUserUuid(userUuid);
            tblReferenceFileNewest.setUpdateDate(sysDate);
            tblReferenceFileNewest.setUpdateUserUuid(userUuid);
            this.entityManager.persist(tblReferenceFileNewest);
        }
        
        TblComponentInspectionReferenceFile tblReferenceFile = this.entityManager
                .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
                    .setParameter("componentInspectionResultId",componentInspectionResultId)
                .getResultList().stream().findFirst().orElse(null);
        
        if (tblReferenceFile == null) {
            tblReferenceFile= new TblComponentInspectionReferenceFile();
            tblReferenceFile.setId(IDGenerator.generate());
            tblReferenceFile.setComponentInspectionResultId(componentInspectionResultId);
            tblReferenceFile.setDrawingFileFlg(inspFile.getDrawingFlg());
            tblReferenceFile.setProofFileFlg(inspFile.getProofFlg());
            tblReferenceFile.setRohsProofFileFlg(inspFile.getRohsProofFlg());
            tblReferenceFile.setPackageSpecFileFlg(inspFile.getPackageSpecFlg());
            tblReferenceFile.setQcPhaseFileFlg(inspFile.getQcPhaseFlg());
            tblReferenceFile.setFile06Flg(inspFile.getFile06Flg());
            tblReferenceFile.setFile07Flg(inspFile.getFile07Flg());
            tblReferenceFile.setFile08Flg(inspFile.getFile08Flg());
            tblReferenceFile.setFile09Flg(inspFile.getFile09Flg());
            tblReferenceFile.setFile10Flg(inspFile.getFile10Flg());
            tblReferenceFile.setFile11Flg(inspFile.getFile11Flg());
            tblReferenceFile.setFile12Flg(inspFile.getFile12Flg());
            tblReferenceFile.setFile13Flg(inspFile.getFile13Flg());
            tblReferenceFile.setFile14Flg(inspFile.getFile14Flg());
            tblReferenceFile.setFile15Flg(inspFile.getFile15Flg());
            tblReferenceFile.setFile16Flg(inspFile.getFile16Flg());
            tblReferenceFile.setFile17Flg(inspFile.getFile17Flg());
            tblReferenceFile.setFile18Flg(inspFile.getFile18Flg());
            tblReferenceFile.setFile19Flg(inspFile.getFile19Flg());
            tblReferenceFile.setFile20Flg(inspFile.getFile20Flg());
            List<MstComponentInspectFileName> names = mstComponentInspectFileService.findFileNameByOwnerCompanyId(incomingCompanyId);
            if(names.isEmpty()) {
                names = mstComponentInspectFileService.findFileNameByOwnerCompanyId("SELF");
            }
            setFileNameToRefFile(names, tblReferenceFile);
            if(!inspFile.getDrawingFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setDrawingFileUuid(tblReferenceFileNewest.getDrawingFileUuid()); //図面ファイルUUID
            }
            if(!inspFile.getProofFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setProofFileUuid(tblReferenceFileNewest.getProofFileUuid()); //材料証明ファイルUUID
            }
            if(!inspFile.getRohsProofFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setRohsProofFileUuid(tblReferenceFileNewest.getRohsProofFileUuid()); //RoHS適用証明ファイルUUID
            }
            if(!inspFile.getPackageSpecFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setPackageSpecFileUuid(tblReferenceFileNewest.getPackageSpecFileUuid()); //包装仕様書ファイルUUID
            }
            if(!inspFile.getQcPhaseFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setQcPhaseFileUuid(tblReferenceFileNewest.getQcPhaseFileUuid()); //QC工程表ファイルUUID
            }
            if(!inspFile.getFile06Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile06Uuid(tblReferenceFileNewest.getFile06Uuid());
            }
            if(!inspFile.getFile07Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile07Uuid(tblReferenceFileNewest.getFile07Uuid());
            }
            if(!inspFile.getFile08Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile08Uuid(tblReferenceFileNewest.getFile08Uuid());
            }
            if(!inspFile.getFile09Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile09Uuid(tblReferenceFileNewest.getFile09Uuid());
            }
            if(!inspFile.getFile10Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile10Uuid(tblReferenceFileNewest.getFile10Uuid());
            }
            if(!inspFile.getFile11Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile11Uuid(tblReferenceFileNewest.getFile11Uuid());
            }
            if(!inspFile.getFile12Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile12Uuid(tblReferenceFileNewest.getFile12Uuid());
            }
            if(!inspFile.getFile13Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile13Uuid(tblReferenceFileNewest.getFile13Uuid());
            }
            if(!inspFile.getFile14Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile14Uuid(tblReferenceFileNewest.getFile14Uuid());
            }
            if(!inspFile.getFile15Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile15Uuid(tblReferenceFileNewest.getFile15Uuid());
            }
            if(!inspFile.getFile16Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile16Uuid(tblReferenceFileNewest.getFile16Uuid());
            }
            if(!inspFile.getFile17Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile17Uuid(tblReferenceFileNewest.getFile17Uuid());
            }
            if(!inspFile.getFile18Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile18Uuid(tblReferenceFileNewest.getFile18Uuid());
            }
            if(!inspFile.getFile19Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile19Uuid(tblReferenceFileNewest.getFile19Uuid());
            }
            if(!inspFile.getFile20Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile20Uuid(tblReferenceFileNewest.getFile20Uuid());
            }
            tblReferenceFile.setCreateDate(sysDate);
            tblReferenceFile.setCreateUserUuid(userUuid);
            tblReferenceFile.setUpdateDate(sysDate);
            tblReferenceFile.setUpdateUserUuid(userUuid);
            this.entityManager.persist(tblReferenceFile);
        } else {
            if(!inspFile.getDrawingFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setDrawingFileUuid(tblReferenceFileNewest.getDrawingFileUuid()); //図面ファイルUUID
            }
            if(!inspFile.getProofFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setProofFileUuid(tblReferenceFileNewest.getProofFileUuid()); //材料証明ファイルUUID
            }
            if(!inspFile.getRohsProofFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setRohsProofFileUuid(tblReferenceFileNewest.getRohsProofFileUuid()); //RoHS適用証明ファイルUUID
            }
            if(!inspFile.getPackageSpecFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setPackageSpecFileUuid(tblReferenceFileNewest.getPackageSpecFileUuid()); //包装仕様書ファイルUUID
            }
            if(!inspFile.getQcPhaseFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setQcPhaseFileUuid(tblReferenceFileNewest.getQcPhaseFileUuid()); //QC工程表ファイルUUID
            }
            if(!inspFile.getFile06Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile06Uuid(tblReferenceFileNewest.getFile06Uuid());
            }
            if(!inspFile.getFile07Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile07Uuid(tblReferenceFileNewest.getFile07Uuid());
            }
            if(!inspFile.getFile08Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile08Uuid(tblReferenceFileNewest.getFile08Uuid());
            }
            if(!inspFile.getFile09Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile09Uuid(tblReferenceFileNewest.getFile09Uuid());
            }
            if(!inspFile.getFile10Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile10Uuid(tblReferenceFileNewest.getFile10Uuid());
            }
            if(!inspFile.getFile11Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile11Uuid(tblReferenceFileNewest.getFile11Uuid());
            }
            if(!inspFile.getFile12Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile12Uuid(tblReferenceFileNewest.getFile12Uuid());
            }
            if(!inspFile.getFile13Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile13Uuid(tblReferenceFileNewest.getFile13Uuid());
            }
            if(!inspFile.getFile14Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile14Uuid(tblReferenceFileNewest.getFile14Uuid());
            }
            if(!inspFile.getFile15Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile15Uuid(tblReferenceFileNewest.getFile15Uuid());
            }
            if(!inspFile.getFile16Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile16Uuid(tblReferenceFileNewest.getFile16Uuid());
            }
            if(!inspFile.getFile17Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile17Uuid(tblReferenceFileNewest.getFile17Uuid());
            }
            if(!inspFile.getFile18Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile18Uuid(tblReferenceFileNewest.getFile18Uuid());
            }
            if(!inspFile.getFile19Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile19Uuid(tblReferenceFileNewest.getFile19Uuid());
            }
            if(!inspFile.getFile20Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG)) {
                tblReferenceFile.setFile20Uuid(tblReferenceFileNewest.getFile20Uuid());
            }
            tblReferenceFile.setUpdateDate(sysDate);
            tblReferenceFile.setUpdateUserUuid(userUuid);
            this.entityManager.merge(tblReferenceFile);
        }
    }
    
    private void setFileNameToRefFile(List<MstComponentInspectFileName> names, TblComponentInspectionReferenceFile tblReferenceFile) {
        names.forEach(name -> {
            switch(name.getPk().getId()) {
                case "drawingFlg":
                    tblReferenceFile.setDrawingFileName(name.getFileName());
                    break;
                case "proofFlg":
                    tblReferenceFile.setProofFileName(name.getFileName());
                    break;
                case "rohsProofFlg":
                    tblReferenceFile.setRohsProofFileName(name.getFileName());
                    break;
                case "packageSpecFlg":
                    tblReferenceFile.setPackageSpecFileName(name.getFileName());
                    break;
                case "qcPhaseFlg":
                    tblReferenceFile.setQcPhaseFileName(name.getFileName());
                    break;
                case "file06Flg":
                    tblReferenceFile.setFile06Name(name.getFileName());
                    break;
                case "file07Flg":
                    tblReferenceFile.setFile07Name(name.getFileName());
                    break;
                case "file08Flg":
                    tblReferenceFile.setFile08Name(name.getFileName());
                    break;
                case "file09Flg":
                    tblReferenceFile.setFile09Name(name.getFileName());
                    break;
                case "file10Flg":
                    tblReferenceFile.setFile10Name(name.getFileName());
                    break;
                case "file11Flg":
                    tblReferenceFile.setFile11Name(name.getFileName());
                    break;
                case "file12Flg":
                    tblReferenceFile.setFile12Name(name.getFileName());
                    break;
                case "file13Flg":
                    tblReferenceFile.setFile13Name(name.getFileName());
                    break;
                case "file14Flg":
                    tblReferenceFile.setFile14Name(name.getFileName());
                    break;
                case "file15Flg":
                    tblReferenceFile.setFile15Name(name.getFileName());
                    break;
                case "file16Flg":
                    tblReferenceFile.setFile16Name(name.getFileName());
                    break;
                case "file17Flg":
                    tblReferenceFile.setFile17Name(name.getFileName());
                    break;
                case "file18Flg":
                    tblReferenceFile.setFile18Name(name.getFileName());
                    break;
                case "file19Flg":
                    tblReferenceFile.setFile19Name(name.getFileName());
                    break;
                case "file20Flg":
                    tblReferenceFile.setFile20Name(name.getFileName());
                    break;
            }
        });
    }
    
    @Transactional
    public void copyFileNewest(String componentId, String componentInspectionResultId, String userUuid) {
        TblComponentInspectionReferenceFileNewest tblReferenceFileNewest = this.entityManager
                    .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId", TblComponentInspectionReferenceFileNewest.class)
                    .setParameter("componentId", componentId)
                    .getResultList().stream().findFirst().orElse(null);

        
        TblComponentInspectionReferenceFile tblReferenceFile = this.entityManager
                    .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
                    .setParameter("componentInspectionResultId", componentInspectionResultId)
                    .getResultList().stream().findFirst().orElse(null);
        if (tblReferenceFile == null) {
            return;
        }
        Date sysDate = new Date();
        if (tblReferenceFileNewest == null) {
            tblReferenceFileNewest= new TblComponentInspectionReferenceFileNewest();
            tblReferenceFileNewest.setId(IDGenerator.generate());
            tblReferenceFileNewest.setComponentId(componentId);
            tblReferenceFileNewest.setDrawingFileUuid(tblReferenceFile.getDrawingFileUuid());
            tblReferenceFileNewest.setProofFileUuid(tblReferenceFile.getProofFileUuid());
            tblReferenceFileNewest.setRohsProofFileUuid(tblReferenceFile.getRohsProofFileUuid());
            tblReferenceFileNewest.setPackageSpecFileUuid(tblReferenceFile.getPackageSpecFileUuid());
            tblReferenceFileNewest.setQcPhaseFileUuid(tblReferenceFile.getQcPhaseFileUuid());
            tblReferenceFileNewest.setCreateDate(sysDate);
            tblReferenceFileNewest.setCreateUserUuid(userUuid);
            tblReferenceFileNewest.setUpdateDate(sysDate);
            tblReferenceFileNewest.setUpdateUserUuid(userUuid);
            tblReferenceFileNewest.setFile06Uuid(tblReferenceFile.getFile06Uuid());
            tblReferenceFileNewest.setFile07Uuid(tblReferenceFile.getFile07Uuid());
            tblReferenceFileNewest.setFile08Uuid(tblReferenceFile.getFile08Uuid());
            tblReferenceFileNewest.setFile09Uuid(tblReferenceFile.getFile09Uuid());
            tblReferenceFileNewest.setFile10Uuid(tblReferenceFile.getFile10Uuid());
            tblReferenceFileNewest.setFile11Uuid(tblReferenceFile.getFile11Uuid());
            tblReferenceFileNewest.setFile12Uuid(tblReferenceFile.getFile12Uuid());
            tblReferenceFileNewest.setFile13Uuid(tblReferenceFile.getFile13Uuid());
            tblReferenceFileNewest.setFile14Uuid(tblReferenceFile.getFile14Uuid());
            tblReferenceFileNewest.setFile15Uuid(tblReferenceFile.getFile15Uuid());
            tblReferenceFileNewest.setFile16Uuid(tblReferenceFile.getFile16Uuid());
            tblReferenceFileNewest.setFile17Uuid(tblReferenceFile.getFile17Uuid());
            tblReferenceFileNewest.setFile18Uuid(tblReferenceFile.getFile18Uuid());
            tblReferenceFileNewest.setFile19Uuid(tblReferenceFile.getFile19Uuid());
            tblReferenceFileNewest.setFile20Uuid(tblReferenceFile.getFile20Uuid());
            this.entityManager.persist(tblReferenceFileNewest);
        } else {
            tblReferenceFileNewest.setDrawingFileUuid(tblReferenceFile.getDrawingFileUuid());
            tblReferenceFileNewest.setProofFileUuid(tblReferenceFile.getProofFileUuid());
            tblReferenceFileNewest.setRohsProofFileUuid(tblReferenceFile.getRohsProofFileUuid());
            tblReferenceFileNewest.setPackageSpecFileUuid(tblReferenceFile.getPackageSpecFileUuid());
            tblReferenceFileNewest.setQcPhaseFileUuid(tblReferenceFile.getQcPhaseFileUuid());
            tblReferenceFileNewest.setUpdateDate(sysDate);
            tblReferenceFileNewest.setUpdateUserUuid(userUuid);
            tblReferenceFileNewest.setFile06Uuid(tblReferenceFile.getFile06Uuid());
            tblReferenceFileNewest.setFile07Uuid(tblReferenceFile.getFile07Uuid());
            tblReferenceFileNewest.setFile08Uuid(tblReferenceFile.getFile08Uuid());
            tblReferenceFileNewest.setFile09Uuid(tblReferenceFile.getFile09Uuid());
            tblReferenceFileNewest.setFile10Uuid(tblReferenceFile.getFile10Uuid());
            tblReferenceFileNewest.setFile11Uuid(tblReferenceFile.getFile11Uuid());
            tblReferenceFileNewest.setFile12Uuid(tblReferenceFile.getFile12Uuid());
            tblReferenceFileNewest.setFile13Uuid(tblReferenceFile.getFile13Uuid());
            tblReferenceFileNewest.setFile14Uuid(tblReferenceFile.getFile14Uuid());
            tblReferenceFileNewest.setFile15Uuid(tblReferenceFile.getFile15Uuid());
            tblReferenceFileNewest.setFile16Uuid(tblReferenceFile.getFile16Uuid());
            tblReferenceFileNewest.setFile17Uuid(tblReferenceFile.getFile17Uuid());
            tblReferenceFileNewest.setFile18Uuid(tblReferenceFile.getFile18Uuid());
            tblReferenceFileNewest.setFile19Uuid(tblReferenceFile.getFile19Uuid());
            tblReferenceFileNewest.setFile20Uuid(tblReferenceFile.getFile20Uuid());
            this.entityManager.merge(tblReferenceFileNewest);
        }
    }
    
    public boolean isAnyFileReqired(String resultId) {
        
        TblComponentInspectionReferenceFile refFile = this.entityManager
            .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
            .setParameter("componentInspectionResultId", resultId)
            .getResultList().stream().findFirst().orElse(null);
        if(refFile == null) {
            return false;
        }
        return refFile.getDrawingFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getProofFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getRohsProofFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getPackageSpecFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getQcPhaseFileFlg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile06Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile07Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile08Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile09Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile10Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile11Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile12Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile13Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile14Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile15Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile16Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile17Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile18Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile19Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG) || 
                refFile.getFile20Flg().equals(CommonConstants.INSPECTION_CHAR_MASS_FLAG);
    }
    
    /**
     * ユーザー名称を取得する
     *
     * @param userUuid
     * @return
     */
    private String getUserName(String userUuid) {

        MstUser mstUser = this.entityManager
                .createNamedQuery("MstUser.findByUserUuid", MstUser.class)
                .setParameter("uuid", userUuid).getResultList().stream().findFirst()
                .orElse(null);

        if (null != mstUser) {

            return mstUser.getUserName();
        }

        return "";

    }
}
