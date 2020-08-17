/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.visualimage;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTableDetail;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResult;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultDetail;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultService;
import com.kmcj.karte.resources.component.inspection.result.model.TblComponentInspectionResultPoList;
import com.kmcj.karte.resources.component.inspection.result.model.TblComponentInspectionResultPoVo;
import com.kmcj.karte.resources.component.inspection.visualimage.model.ComponentInspectionItemDetail;
import com.kmcj.karte.resources.component.inspection.visualimage.model.ComponentInspectionItemVisualImages;
import com.kmcj.karte.resources.component.inspection.visualimage.model.ComponentInspectionItemVisualImages.InspectionVisualImage;
import com.kmcj.karte.resources.component.inspection.visualimage.model.ComponentInspectionVisualImagesResp;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * Master Component visual inspection image service
 *
 * @author duanlin
 */
@Dependent
public class TblComponentInspectionVisualImageService {
    
    private static final String PRODUCTION_LOT_NUMBER = "NO_PRODUCTION_LOT_NUM";

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    @Inject
    private TblComponentInspectionResultService tblComponentInspectionResultService;

    /**
     * Get component inspection visual images for tablet
     *
     * @param resultID
     * @param inspectType
     * @return
     */
    public ComponentInspectionVisualImagesResp getInspectionVisualImagesForTablet(String resultID, Integer inspectType) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblComponentInspectionResult t");
        queryBuilder.append(" JOIN FETCH t.mstComponent");
        queryBuilder.append(" WHERE t.id = :id");
        queryBuilder.append(" AND t.inspectionStatus IN ('");
        queryBuilder.append(CommonConstants.INSPECTION_STATUS_O_INSPECTING);
        queryBuilder.append("', '");
        queryBuilder.append(CommonConstants.INSPECTION_STATUS_O_REJECTED);
        queryBuilder.append("', '");
        queryBuilder.append(CommonConstants.INSPECTION_STATUS_I_INSPECTING);
        queryBuilder.append("', '");
        queryBuilder.append(CommonConstants.INSPECTION_STATUS_I_AGAIN_REJECTED);
        queryBuilder.append("')");
        queryBuilder.append(" ORDER BY t.createDate DESC");

        TypedQuery<TblComponentInspectionResult> query = this.entityManager.createQuery(
                queryBuilder.toString(), TblComponentInspectionResult.class);

        query.setParameter("id", resultID);

        List<TblComponentInspectionResult> resultList = query.getResultList();
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }

        TblComponentInspectionResult tblResult = resultList.get(0);
        String componentInspectionResultId = tblResult.getId();
        ComponentInspectionVisualImagesResp visalImageInfo = new ComponentInspectionVisualImagesResp();
        visalImageInfo.setComponentInspectionResultId(componentInspectionResultId);
        visalImageInfo.setComponentCode(tblResult.getMstComponent().getComponentCode());
        visalImageInfo.setComponentName(tblResult.getMstComponent().getComponentName());
        visalImageInfo.setComponentType(tblResult.getMstComponent().getComponentType());
        String poNumber = "";
        if (StringUtils.isNotEmpty(tblResult.getProductionLotNum()) && !PRODUCTION_LOT_NUMBER.equals(tblResult.getProductionLotNum())) {
            
            TblComponentInspectionResultPoList poList = tblComponentInspectionResultService.getTblPoOutbound(
                        tblResult.getComponentId(), tblResult.getProductionLotNum(),tblResult.getIncomingCompanyId(),tblResult.getOutgoingCompanyId());
            
            if (poList != null && poList.getTblComponentInspectionResultPoVos() != null && poList.getTblComponentInspectionResultPoVos().size() > 0) {
                for (TblComponentInspectionResultPoVo poVo : poList.getTblComponentInspectionResultPoVos()) {
                    poNumber += poVo.getOrderNumber().concat("-").concat(poVo.getItemNumber()) + "\n";
                }
            }
            visalImageInfo.setProductionLotNum(tblResult.getProductionLotNum());
        } else {
            visalImageInfo.setProductionLotNum("");
        }
        visalImageInfo.setPoNumber(poNumber);
        List<ComponentInspectionItemDetail> visualImagesList
                = this.getVisualImages(componentInspectionResultId, inspectType);
        
        //連番昇順
        for(ComponentInspectionItemDetail item : visualImagesList) {
            if (item.getDetails() != null && item.getDetails().size() > 1) {
                item.getDetails().sort((vo1, vo2) -> vo1.getSeq().compareTo(vo2.getSeq()));
            }
        }
        visalImageInfo.setVisualImagesList(visualImagesList);
        
        return visalImageInfo;
    }

    /**
     * Get by id
     *
     * @param inspectionResultDetailId
     * @return
     */
    public ComponentInspectionItemVisualImages getVisualImagesById(String inspectionResultDetailId) {
        List<TblComponentInspectionVisualImageFile> tblImageFileList = this.entityManager
                .createNamedQuery("TblComponentInspectionVisualImageFile.findByComponentInspectionResultDetailId", TblComponentInspectionVisualImageFile.class)
                .setParameter("componentInspectionResultDetailId", inspectionResultDetailId)
                .getResultList();

        // made date format
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
        ComponentInspectionItemVisualImages visualImages = new ComponentInspectionItemVisualImages();
        visualImages.setInspectionResultDetailId(inspectionResultDetailId);
        List<InspectionVisualImage> visualImageList = new ArrayList<>();
        tblImageFileList.forEach(action -> {
            InspectionVisualImage visualImage = new InspectionVisualImage();
            visualImage.setSeq(action.getTblComponentInspectionVisualImageFilePK().getSeq());
            visualImage.setFileUuid(action.getFileUuid());
            visualImage.setFileType(action.getFileType());
            visualImage.setFileExtension(action.getFileExtension());
            visualImage.setTakenDate(sdf.format(action.getTakenDate()));
            visualImage.setTakenDateStz(sdf.format(action.getTakenDateStz()));
            visualImage.setRemarks(action.getRemarks());
            visualImage.setThumbnailFileUuid(action.getThumbnailFileUuid());
            visualImage.setDetailSeq(action.getResultDetail().getSeq());
            visualImageList.add(visualImage);
        });

        visualImages.setInspectionVisualImage(visualImageList);
        return visualImages;
    }

    /**
     * update component inspection visual files.
     *
     * @param items
     * @param loginUser
     */
    @Transactional
    public void updateVisualImages(List<ComponentInspectionItemDetail> items, LoginUser loginUser) {
        updateItemResult(items, loginUser);
        List<ComponentInspectionItemVisualImages> images = items.stream().flatMap(detail -> detail.getDetails().stream()).collect(Collectors.toList());
        updateSeqVisualResult(images, loginUser);
        // delete visual inspection files with item.
        this.deleteFiles(images);
        // insert visual inspection files
        this.insertFiles(images, loginUser);

    }

    /*  private methods
    |========================================================================*/
    private List<ComponentInspectionItemDetail> getVisualImages(String componentInspectionResultId, Integer inspectionType) {
        List<TblComponentInspectionResultDetail> tblResultDetails = this.entityManager
                .createQuery("SELECT t FROM TblComponentInspectionResultDetail t"
                        + " INNER JOIN TblComponentInspectionResult rs on rs.id = t.componentInspectionResultId "
                        + " INNER JOIN MstComponentInspectionItemsTableDetail m on rs.componentInspectionItemsTableId = m.componentInspectionItemsTableId and m.inspectionItemSno= t.inspectionItemSno "
                        + " and m.measurementType= :measurementType "
                        + " WHERE t.componentInspectionResultId = :componentInspectionResultId AND t.inspectionType = :inspectionType"
                        + " order by m.localSeq,m.inspectionItemSno ", TblComponentInspectionResultDetail.class)
                .setParameter("componentInspectionResultId", componentInspectionResultId)
                .setParameter("inspectionType", inspectionType)
                .setParameter("measurementType", CommonConstants.MEASUREMENT_TYPE_VISUAL)
                .getResultList();
        
        // select MstComponentInspectionItemsTableDetail
        Map<String, MstComponentInspectionItemsTableDetail> mstDetailMap
                = this.tblComponentInspectionResultService.getMstInspectionItemDetailMap(componentInspectionResultId);

        List<ComponentInspectionItemDetail> visualImagesList = new ArrayList<>();
        Map<String, ComponentInspectionItemDetail> detailPerSno = new LinkedHashMap<>();
        List<String> idList = new ArrayList<>();
        tblResultDetails.stream().forEach(resultDetial -> {
            idList.add(resultDetial.getId());
            ComponentInspectionItemVisualImages visualImages = new ComponentInspectionItemVisualImages();
            visualImages.setInspectionResultDetailId(resultDetial.getId());
            visualImages.setVisualResult(resultDetial.getSeqVisualResult());
            visualImages.setSeq(resultDetial.getSeq());
            visualImages.setItemResultAuto(resultDetial.getItemResultAuto());//自动判断
            visualImages.setItemResultManual(resultDetial.getItemResultManual());//手動判定
            visualImages.setManJudgeComment(resultDetial.getManJudgeComment());//手動判定コメント
            
            MstComponentInspectionItemsTableDetail mDetail = mstDetailMap.get(resultDetial.getInspectionItemSno());
            if (mDetail != null) {
                visualImages.setInspectionItemName(mDetail.getInspectionItemName());
            }
            if(!detailPerSno.containsKey(resultDetial.getInspectionItemSno())) {
                ComponentInspectionItemDetail detail = new ComponentInspectionItemDetail(resultDetial.getInspectionItemSno());
                detailPerSno.put(resultDetial.getInspectionItemSno(), detail);
            }
            detailPerSno.get(resultDetial.getInspectionItemSno()).getDetails().add(visualImages);
        });
        detailPerSno.values().forEach(val -> {visualImagesList.add(val);});
        // no visual inpseciton items
        if (idList.isEmpty()) {
            return visualImagesList;
        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblComponentInspectionVisualImageFile t");
        queryBuilder.append(" WHERE t.tblComponentInspectionVisualImageFilePK.componentInspectionResultDetailId IN :componentInspectionResultDetailId");
        List<TblComponentInspectionVisualImageFile> tblImageFileList = this.entityManager
                .createQuery(queryBuilder.toString(), TblComponentInspectionVisualImageFile.class)
                .setParameter("componentInspectionResultDetailId", idList)
                .getResultList();

        if (tblImageFileList.isEmpty()) {
            return visualImagesList;
        }

        // made date format
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
        Map<String, List<InspectionVisualImage>> visualImageMap = new HashMap<>();
        List<InspectionVisualImage> visualImageList;
        for (TblComponentInspectionVisualImageFile tblImageFile : tblImageFileList) {
            String mapKey = tblImageFile.getTblComponentInspectionVisualImageFilePK().getComponentInspectionResultDetailId();
            if (visualImageMap.containsKey(mapKey)) {
                visualImageList = visualImageMap.get(mapKey);
            } else {
                visualImageList = new ArrayList<>();
                visualImageMap.put(mapKey, visualImageList);
            }
            InspectionVisualImage visualImage = new InspectionVisualImage();
            visualImage.setSeq(tblImageFile.getTblComponentInspectionVisualImageFilePK().getSeq());
            visualImage.setFileUuid(tblImageFile.getFileUuid());
            visualImage.setFileType(tblImageFile.getFileType());
            visualImage.setFileExtension(tblImageFile.getFileExtension());
            visualImage.setTakenDate(tblImageFile.getTakenDate() == null ? null : sdf.format(tblImageFile.getTakenDate()));
            visualImage.setTakenDateStz(tblImageFile.getTakenDateStz() == null ? null : sdf.format(tblImageFile.getTakenDateStz()));
            visualImage.setRemarks(tblImageFile.getRemarks());
            visualImage.setThumbnailFileUuid(tblImageFile.getThumbnailFileUuid());
            visualImageList.add(visualImage);
        }

        visualImagesList.stream().forEach(item -> {
            item.getDetails().forEach(detail -> {
                detail.setInspectionVisualImage(visualImageMap.get(detail.getInspectionResultDetailId()));
            });
        });
        return visualImagesList;
    }

    /**
     * delete visual inspection files with item.
     *
     * @param items
     */
    private void deleteFiles(List<ComponentInspectionItemVisualImages> items) {
        // keys
        List<String> detailIds = new ArrayList<>();
        // make detail id list 
        items.forEach(item -> detailIds.add(item.getInspectionResultDetailId()));
        // delete visual inspection files with item.
        entityManager.createNamedQuery("TblComponentInspectionVisualImageFile.deleteByItems")
                .setParameter("componentInspectionResultDetailId", detailIds).executeUpdate();
    }

    /**
     * insert visual inspection files.
     *
     * @param items
     * @param loginUser
     */
    private void insertFiles(List<ComponentInspectionItemVisualImages> items, LoginUser loginUser) {
        // made date format
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
        // loop files
        items.forEach((item) -> {
            int i = 1;
            for (InspectionVisualImage inputFile : item.getInspectionVisualImage()) {
                // primary key
                TblComponentInspectionVisualImageFilePK pk = new TblComponentInspectionVisualImageFilePK();
                // entity
                TblComponentInspectionVisualImageFile file = new TblComponentInspectionVisualImageFile();
                // make primary key
                pk.setComponentInspectionResultDetailId(item.getInspectionResultDetailId());
                pk.setSeq(i++);
                file.setTblComponentInspectionVisualImageFilePK(pk);
                // set value
                file.setFileUuid(inputFile.getFileUuid());
                file.setFileType(inputFile.getFileType());
                file.setFileExtension(inputFile.getFileExtension());
                try {
                    if (StringUtils.isNotEmpty(inputFile.getTakenDate())) {
                        file.setTakenDate(sdf.parse(DateFormat.dateTimeFormat(sdf.parse(inputFile.getTakenDate()), loginUser.getJavaZoneId())));
                        file.setTakenDateStz(sdf.parse(inputFile.getTakenDate()));
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(TblComponentInspectionVisualImageService.class.getName()).log(Level.SEVERE, null, ex);
                }
                file.setRemarks(inputFile.getRemarks());
                file.setThumbnailFileUuid(inputFile.getThumbnailFileUuid());
                file.setCreateDate(new Date());
                file.setCreateDateUuid(loginUser.getUserUuid());
                file.setUpdateDate(new Date());
                file.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(file);
            }
        });
    }
    
    private void updateSeqVisualResult(List<ComponentInspectionItemVisualImages> images, LoginUser loginUser) {
        List<ComponentInspectionItemVisualImages> updTgt = images.stream().filter(detail -> detail.getSeq() != 0).collect(Collectors.toList());
        updTgt.forEach(detail -> {
            entityManager.createNamedQuery("TblComponentInspectionResultDetail.updateSeqVisualResult")
                .setParameter("seqVisualResult", detail.getVisualResult())
                .setParameter("updateUserUuid", loginUser.getUserUuid())
                .setParameter("updateDate", new Date())
                .setParameter("id", detail.getInspectionResultDetailId())
                .executeUpdate();
        });
    }
    
    private void updateItemResult(List<ComponentInspectionItemDetail> details, LoginUser loginUser) {
        details.forEach(detail -> {
            ComponentInspectionItemVisualImages vi = detail.getDetails().get(0);
            Set<Integer> vResults = detail.getDetails().stream().filter(result -> result.getSeq() != 0).map(result -> result.getVisualResult()).collect(Collectors.toSet());
            Integer iResult;
            Integer iResultAuto;
            if(vResults.contains(0)) {
                iResult = 0;
                iResultAuto = 0;
            } else if(vResults.contains(2)) {
                iResult = 2;
                iResultAuto = 2;
            } else {
                iResult = 1;
                iResultAuto = 1;
            }
            if(vi.getItemResultManual() != null && vi.getItemResultManual() != 0) {
                iResult = vi.getItemResultManual();
            }
            entityManager.createNamedQuery("TblComponentInspectionResultDetail.updateItemResult")
                .setParameter("itemResult", iResult)
                .setParameter("updateUserUuid", loginUser.getUserUuid())
                .setParameter("itemResultAuto", iResultAuto)
                .setParameter("updateDate", new Date())
                .setParameter("id", vi.getInspectionResultDetailId())
                .executeUpdate();
        });
    }
}
