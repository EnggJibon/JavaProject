/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.extercomponentinspection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.ObjectResponse;
import com.kmcj.karte.batch.externalmold.delete.TblDeletedKeyService;
import com.kmcj.karte.batch.externalmold.delete.TblDeletedKeyVo;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectClass;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFile;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFileService;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectLang;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectType;
import com.kmcj.karte.resources.component.inspection.file.model.MstFileSettingsForBat;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTableService;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemsTableForBatch;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemsTableRespVo;
import com.kmcj.karte.resources.component.inspection.referencefile.TblComponentInspectionReferenceFile;
import com.kmcj.karte.resources.component.inspection.referencefile.TblComponentInspectionReferenceFileNewest;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultService;
import com.kmcj.karte.resources.component.inspection.result.model.*;
import com.kmcj.karte.resources.component.inspection.visualimage.TblComponentInspectionVisualImageFile;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.util.FileUtil;
import org.apache.commons.lang.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResult.AbortFlg;
import java.io.IOException;

/**
 * @author duanlin
 */
@Dependent
public class ExternalComponentInspectionService {

    private static final String EXT_COMPONENT_INSPECTION_ITEM_TABLE_PUSH_API_URL = "ws/karte/api/component/inspection/item/extdata/push";
    private static final String EXT_COMPONENT_INSPECTION_OUTGOING_RESULT_GET_API_URL = "ws/karte/api/component/inspection/result/outgoing/extdata/get";
    private static final String EXT_COMPONENT_INSPECTION_OUTGOING_RESULT_NOTIFY_API_URL = "ws/karte/api/component/inspection/result/outgoing/extdata/notify";
    private static final String EXT_COMPONENT_INSPECTION_INCOMING_RESULT_PUSH_API_URL = "ws/karte/api/component/inspection/result/incoming/extdata/push";
    private static final String EXT_OUTGOING_PO_INFO_GET_API_URL = "ws/karte/api/component/inspection/result/outgoing/poinfo/get";
    private static final String EXT_COMPONENT_INSPECTION_OUTGOING_PO_DONE_API_URL = "ws/karte/api/component/inspection/result/outgoing/extdata/podone";
    private static final String EXT_COMPONENT_INSPECTION_OUTGOING_SHIPMENT_DONE_API_URL = "ws/karte/api/component/inspection/result/outgoing/extdata/shipmentdone";
    private static final String EXTLOGIN_DELETEDKEY_INSPECTION_API_URL = "ws/karte/api/deletedkey/inspectextdeletedkey";
    private static final String EXT_FILE_SETTINGS_PUSH_URL = "ws/karte/api/component/inspection/file/extdata/push";
    private static final String EXT_GET_ABORTING_RESULT_URL = "ws/karte/api/component/inspection/result/extaborting";
    private static final String EXT_ABORTED_PUSH_URL = "ws/karte/api/component/inspection/result/ackaborted";

    //FileDL
    private static final String EXT_FILENAME_API_URL = "ws/karte/api/files/extfilename";
    private static final String EXT_FILESDOWNLOAD_API_URL = "ws/karte/api/files/extfiledownload";
    private static final String EXT_FILESUPLOAD_API_URL = "ws/karte/api/files/extfileupload";
    private static final String GET_EXTFILEUPLOAD_API_URL = "ws/karte/api/files/getextuploadfile";

    private static final Logger LOGGER = Logger.getLogger(ExternalComponentInspectionService.class.getName());

    
    @Inject
    private KartePropertyService kartePropertyService;
    @Inject
    private MstComponentInspectionItemsTableService mstComponentInspectionItemsTableService;
    @Inject
    private TblComponentInspectionResultService tblComponentInspectionResultService;
    @Inject
    private MstComponentInspectFileService mstCompInspectFileService;
    @Inject
    private TblUploadFileService tblUploadFileService;
    @Inject
    private TblDeletedKeyService tblDeletedKeyService;

    /**
     * push external inspection items table
     * 
     * @param apiBaseUrl
     * @param token
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param methodName 
     */
    public void pushExternalInspectionItemsTable(String apiBaseUrl, String token,
            String outgoingCompanyId, String incomingCompanyId, String methodName) {

        List<ComponentInspectionItemsTableForBatch> dataList =
                this.mstComponentInspectionItemsTableService.getExternalInspectionItemsTable(outgoingCompanyId, incomingCompanyId);
        
        if (dataList == null || dataList.isEmpty()) {
            LOGGER.log(Level.INFO, "部品検査項目表のデータが見つかりません。", methodName);
        } else {
            // call API
            String resultJson = FileUtil.sendPost(apiBaseUrl + EXT_COMPONENT_INSPECTION_ITEM_TABLE_PUSH_API_URL, token, dataList);
            
            if (StringUtils.isNotEmpty(resultJson)) {
                Gson gson = new Gson();
                ComponentInspectionItemsTableRespVo response = gson.fromJson(resultJson, ComponentInspectionItemsTableRespVo.class);
                
                // if error then output log
                if (response.isError()) {
                    LOGGER.log(Level.WARNING, response.getErrorMessage(), methodName);
                }
                
                // KM-464 部品番号読み替え対応 2017/12/4 by penggd Start
                if (null != response.getIdList() && response.getIdList().size() > 0) {

                    Map<String, String> itemsTableMap = new HashMap<>();
                    
                    for (int i = 0; i < response.getIdList().size(); i++) {
                        itemsTableMap.put(response.getIdList().get(i), response.getIdList().get(i));
                    }

                    List<ComponentInspectionItemsTableForBatch> sucDataList = new ArrayList<ComponentInspectionItemsTableForBatch>();

                    for (ComponentInspectionItemsTableForBatch componentInspectionItemsTableForBatch : dataList) {

                        if (StringUtils.isEmpty(itemsTableMap
                                .get(componentInspectionItemsTableForBatch.getInspectionItemsTable().getId()))) {

                            sucDataList.add(componentInspectionItemsTableForBatch);
                        }

                    }

                    this.mstComponentInspectionItemsTableService.updateInspBatchUpdateStatus(sucDataList);
                } else {

                    this.mstComponentInspectionItemsTableService.updateInspBatchUpdateStatus(dataList);
                }
                // KM-464 部品番号読み替え対応 2017/12/4 by penggd End
                
                LOGGER.log(Level.INFO, "部品検査項目表のデータをプシュしました。", methodName);
            }
        }
    }
    
    public void pushExtInspectFileSettings(String apiBaseUrl, String token, String outgoingCompanyId) {
        List<MstComponentInspectClass> classes = mstCompInspectFileService.findNotPushedClass(outgoingCompanyId);
        List<MstComponentInspectType> types = mstCompInspectFileService.findNotPushedType(outgoingCompanyId);
        types.forEach(t -> {t.setMstComponentInspectFileCollection(new ArrayList<>());});
        List<String> classids = classes.stream().map(c -> c.getPk().getId()).collect(Collectors.toList());
        if(classids.isEmpty()) {
            classids.add("");//jpqlのINに空リストを渡すとエラーになるのを回避。
        }
        List<String> typeids = types.stream().map(t -> t.getId()).collect(Collectors.toList());
        if(typeids.isEmpty()) {
            typeids.add("");
        }
        List<MstComponentInspectFile> files = mstCompInspectFileService.findByClassOrType(classids, typeids, "SELF");
        files.forEach(f -> {
            f.setMstComponentInspectClass(null);
            f.setMstComponentInspectType(null);
        });
        List<MstFileSettingsForBat.FileName> fileNames = mstCompInspectFileService.findNotPushedFileName(outgoingCompanyId).stream()
            .map(f -> new MstFileSettingsForBat.FileName(f.getPk().getId(), f.getFileName())).collect(Collectors.toList());
        List<String> dictKeys = toDictKeys(classes, types);
        List<MstComponentInspectLang> langs = mstCompInspectFileService.findLangsByDictKeys(dictKeys);
        
        MstFileSettingsForBat postData = new MstFileSettingsForBat();
        postData.setInspClasses(classes);
        postData.setInspTypes(types);
        postData.setInspFiles(files);
        postData.setFileNames(fileNames);
        postData.setInspLangs(langs);
        
        String resultJson = FileUtil.sendPost(apiBaseUrl + EXT_FILE_SETTINGS_PUSH_URL, token, postData);
        Gson gson = new Gson();
        BasicResponse res = gson.fromJson(resultJson, BasicResponse.class);
        if (res.isError()) {
            LOGGER.log(Level.WARNING, res.getErrorMessage(), apiBaseUrl + EXT_FILE_SETTINGS_PUSH_URL);
            return;
        }
        mstCompInspectFileService.updateBatUpdStatusPushed(postData, outgoingCompanyId);
    }
    
    private List<String> toDictKeys(List<MstComponentInspectClass> classes, List<MstComponentInspectType> types) {
        List<String> classDictKeys = classes.stream().map(c -> c.getDictKey()).collect(Collectors.toList());
        List<String> typeDictKeys = types.stream().map(t -> t.getDictKey()).collect(Collectors.toList());
        return Stream.concat(classDictKeys.stream(), typeDictKeys.stream()).collect(Collectors.toList());
    }
    
    /**
     * push external incoming inspection result to supplier
     * 
     * @param apiBaseUrl
     * @param token
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param methodName 
     */
    public void pushExtIncomingInspectionResult(String apiBaseUrl, String token, String outgoingCompanyId,
            String incomingCompanyId, String methodName) {
        List<ComponentInspectionResultInfoForBatch> dataList = this.tblComponentInspectionResultService
                .getExtIncomingInspectionResult(outgoingCompanyId, incomingCompanyId);

        if (dataList == null || dataList.isEmpty()) {
            LOGGER.log(Level.INFO, "部品入荷結果のデータが見つかりません。", methodName);
        } else {
            String path = kartePropertyService.getDocumentDirectory();

            for (ComponentInspectionResultInfoForBatch input : dataList) {
                input.getInspectionResult().setMstTable(null);
                for (TblComponentInspectionVisualImageFile file : input.getInspectionVisualImageFiles()) {
                    String fileUuid = file.getFileUuid();

                    String filePath = path + FileUtil.SEPARATOR + CommonConstants.IMAGE;

                    File uploadFile = FileUtil.downloadFile(fileUuid, filePath);
                    if (null != uploadFile) {
                        String fileName = uploadFile.getName();

                        TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(fileUuid);

                        if (null != tblUploadFile) {
                            String uploadFileName = tblUploadFile.getUploadFileName();

                            Map<String, String> textParams = new HashMap<>();
                            Map<String, File> fileparams = new HashMap<>();

                            textParams.put("fileName", fileName);
                            textParams.put("functionId", "30100");
                            textParams.put("fileUuid", fileUuid);
                            textParams.put("uploadFileName", uploadFileName);
                            textParams.put("fileType", CommonConstants.IMAGE);
                            fileparams.put("uploadFile", uploadFile);

                            if (StringUtils.isNotBlank(fileName)) {
                                FileUtil.sendPostUpload(apiBaseUrl + EXT_FILESUPLOAD_API_URL, token, textParams,
                                        fileparams);
                            }
                        }
                    }
                }
            }
            LOGGER.log(Level.INFO, "Fetching Data from DB completed.");
            // call API
            String resultJson = FileUtil.sendPost(apiBaseUrl + EXT_COMPONENT_INSPECTION_INCOMING_RESULT_PUSH_API_URL,
                    token, dataList);
            LOGGER.log(Level.INFO, "Posting Data completed.");
            if (StringUtils.isNotEmpty(resultJson)) {
                Gson gson = new Gson();
                TblComponentInspectionResultVo response = gson.fromJson(resultJson, TblComponentInspectionResultVo.class);
                // if error then output log
                if (response.isError()) {
                    LOGGER.log(Level.WARNING, response.getErrorMessage(), methodName);
                    return;
                }

                // 検査文書を連携 2017/10/30 penggd Start
                Map<String, String> imagePdfUUidMap = new LinkedHashMap<>();

                if (dataList.size() > 0) {

                    for (ComponentInspectionResultInfoForBatch inputData : dataList) {

                        TblComponentInspectionReferenceFile inspectionRefFile = inputData.getInspectionRefFile();
                        if (inspectionRefFile != null) {
                            if (StringUtils.isNotBlank(inspectionRefFile.getDrawingFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getDrawingFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getProofFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getProofFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getRohsProofFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getRohsProofFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getPackageSpecFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getPackageSpecFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getQcPhaseFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getQcPhaseFileUuid(), CommonConstants.DOC);
                            }

                            if (StringUtils.isNotBlank(inspectionRefFile.getFile06Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile06Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile07Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile07Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile08Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile08Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile09Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile09Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile10Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile10Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile11Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile11Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile12Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile12Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile13Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile13Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile14Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile14Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile15Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile15Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile16Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile16Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile17Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile17Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile18Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile18Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile19Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile19Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile20Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile20Uuid(), CommonConstants.DOC);
                            }
                        }

                        TblComponentInspectionReferenceFileNewest inspectionRefFileNewest = inputData
                                .getInspectionRefFileNewest();
                        if (inspectionRefFileNewest != null) {
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getDrawingFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getDrawingFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getProofFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getProofFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getRohsProofFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getRohsProofFileUuid(),
                                        CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getPackageSpecFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getPackageSpecFileUuid(),
                                        CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getQcPhaseFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getQcPhaseFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile06Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile06Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile07Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile07Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile08Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile08Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile09Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile09Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile10Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile10Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile11Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile11Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile12Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile12Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile13Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile13Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile14Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile14Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile15Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile15Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile16Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile16Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile17Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile17Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile18Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile18Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile19Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile19Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile20Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFileNewest.getFile20Uuid(), CommonConstants.DOC);
                            }
                        }
                    }
                }

                // 拠点から、サプライヤ側へファイルをアップロードする     
                for (Map.Entry<String, String> entry : imagePdfUUidMap.entrySet()) {

                    String fileUuid = entry.getKey();
                    String fileType = entry.getValue();

                    String filePath = path + FileUtil.SEPARATOR + fileType;

                    // ファイルを取得
                    File uploadFile = FileUtil.downloadFile(fileUuid, filePath);

                    if (null != uploadFile) {

                        String fileName = uploadFile.getName();

                        // 実際のファイル名称を取得
                        TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(fileUuid);

                        if (null != tblUploadFile) {

                            String uploadFileName = tblUploadFile.getUploadFileName();

                            // ファイルアップロード用のパラメータを設定する
                            Map<String, String> textParams = new HashMap<>();
                            Map<String, File> fileparams = new HashMap<>();

                            textParams.put("fileName", fileName);
                            // 入荷検査登録の権限を設定する
                            textParams.put("functionId", "30100");
                            textParams.put("fileUuid", fileUuid);
                            textParams.put("uploadFileName", uploadFileName);
                            textParams.put("fileType", CommonConstants.DOC);
                            fileparams.put("uploadFile", uploadFile);

                            if (StringUtils.isNotBlank(fileName)) {

                                // サプライヤ側へファイルをアップロードする
                                FileUtil.sendPostUpload(apiBaseUrl + EXT_FILESUPLOAD_API_URL, token, textParams,
                                        fileparams);
                            }
                        }

                    }
                }
                // 検査文書を連携 2017/10/30 penggd End
                
                // KM-464 部品番号読み替え対応 2017/12/4 by penggd Start
                List<String> resultIdList = dataList.stream().map(mapper -> mapper.getInspectionResult().getId())
                        .collect(Collectors.toCollection(ArrayList::new));

                if (null != response.getIdList() && response.getIdList().size() > 0) {

                    Map<String, String> resultIdMap = new HashMap<>();

                    for (int i = 0; i < response.getIdList().size(); i++) {
                        resultIdMap.put(response.getIdList().get(i), response.getIdList().get(i));
                    }

                    List<String> sucDataList = new ArrayList<String>();

                    for (String resultId : resultIdList) {

                        if (StringUtils.isEmpty(resultIdMap.get(resultId))) {

                            sucDataList.add(resultId);
                        }

                    }

                    this.tblComponentInspectionResultService.updateInspBatchUpdateStatus(sucDataList,
                            CommonConstants.INSP_BATCH_UPDATE_STATUS_I_RESULT_PUSHED);
                } else {

                    this.tblComponentInspectionResultService.updateInspBatchUpdateStatus(resultIdList,
                            CommonConstants.INSP_BATCH_UPDATE_STATUS_I_RESULT_PUSHED);
                }
                // KM-464 部品番号読み替え対応 2017/12/4 by penggd End

                LOGGER.log(Level.INFO, "部品入荷検査結果のデータをプシュしました。", methodName);
            }
        }
    }
    
    /**
     * Get external outgoing inspection result
     * 
     * @param apiBaseUrl
     * @param token
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param methodName 
     */
    public void getExtOutgoingInspectionResult(String apiBaseUrl, String token,
            String outgoingCompanyId, String incomingCompanyId, String methodName) {
        ComponentInspectionResultResp outgoingResultResp = FileUtil.callGetAPI(apiBaseUrl + EXT_COMPONENT_INSPECTION_OUTGOING_RESULT_GET_API_URL, token, ComponentInspectionResultResp.class);
        if (outgoingResultResp == null) {
            return;
        }
        List<ComponentInspectionResultInfoForBatch> outgoingResultList = outgoingResultResp.getDataForBatch();
        if (outgoingResultList == null || outgoingResultList.isEmpty()) {
            LOGGER.log(Level.INFO, "部品出荷検査結果のデータが見つかりません。", methodName);
            return;
        }
        
        Map<String, String> imagePdfUUidMap =  this.tblComponentInspectionResultService.importOutgoingDataByBatch(
                outgoingResultList, outgoingCompanyId, incomingCompanyId);
        LOGGER.log(Level.INFO, "部品出荷検査結果のデータを取得・更新しました。", methodName);
        
        String path = kartePropertyService.getDocumentDirectory();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        
        for (Map.Entry<String, String> entry : imagePdfUUidMap.entrySet()) {
            String fileUuid = entry.getKey();
            String fileType = entry.getValue();

            String filePath = path + FileUtil.SEPARATOR + fileType;
            String urlParam = "?uuid=" + fileUuid + "&fileType=" + fileType;

            String fileName = FileUtil.getDataGet(apiBaseUrl + EXT_FILENAME_API_URL + urlParam, token);
            if (StringUtils.isNotBlank(fileName)) {

                FileUtil.getFileGet(apiBaseUrl + EXT_FILESDOWNLOAD_API_URL + urlParam, fileName, token, filePath);

                // tbl_upload_fileの更新
                String strUploadFiles = FileUtil
                        .getDataGet(apiBaseUrl + GET_EXTFILEUPLOAD_API_URL + "?fileUuid=" + fileUuid, token);
                if (null != strUploadFiles && !strUploadFiles.trim().equals("")) {
                    TblUploadFile uploadFile = gson.fromJson(strUploadFiles, new TypeToken<TblUploadFile>() {
                    }.getType());
                    if (null == tblUploadFileService) {
                        tblUploadFileService = new TblUploadFileService();
                    }
                    if (null != uploadFile) {
                        List<TblUploadFile> uploadFileList = new ArrayList<TblUploadFile>();
                        uploadFileList.add(uploadFile);

                        tblUploadFileService.updateExtIssuesByBatch(uploadFileList);
                        LOGGER.log(Level.INFO, "UploadFileテーブルのデータを取得・更新しました。", methodName);
                    } else {
                        LOGGER.log(Level.WARNING, "UploadFileテーブルのデータが見つかりません。", methodName);
                    }
                }

            }
        }

        List<String> keyList = this.tblComponentInspectionResultService.getNotYetNotifyResult(outgoingCompanyId, incomingCompanyId);
        String result = FileUtil.sendPost(apiBaseUrl + EXT_COMPONENT_INSPECTION_OUTGOING_RESULT_NOTIFY_API_URL, token, keyList);
        BasicResponse response = gson.fromJson(result, BasicResponse.class);
        if (response != null && !response.isError()) {
            this.tblComponentInspectionResultService.updateInspBatchUpdateStatus(keyList, CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_GET_NOTIFIED);
        } else {
            LOGGER.log(Level.WARNING, "サプライヤーへ通信失敗しました。", methodName);
        }
    }
    
    /**
     * Get external outgoing poInfo
     * 
     * @param apiBaseUrl
     * @param token
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param methodName
     */
    public void getExtOutgoingPoInfo(String apiBaseUrl, String token, String outgoingCompanyId,
            String incomingCompanyId, String methodName) {

        String resultJson = FileUtil.getDataGet(apiBaseUrl + EXT_OUTGOING_PO_INFO_GET_API_URL, token);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ComponentInspectionResultResp outgoingResultResp = gson.fromJson(resultJson,
                ComponentInspectionResultResp.class);
        if (outgoingResultResp == null) {
            return;
        }
        ComponentPoInfoForBatch outgoingPoInfo = outgoingResultResp.getDataForPoInfoBatch();
        if (null == outgoingPoInfo || outgoingPoInfo.getTblPoOutboundVoList().isEmpty()
                || outgoingPoInfo.getTblShipmentOutboundVoList().isEmpty()) {
            LOGGER.log(Level.INFO, "部品関連POのデータが見つかりません。", methodName);
            return;
        }

        Map<String, String> uuidMap = this.tblComponentInspectionResultService
                .importOutgoingPoInfoByBatch(outgoingPoInfo, outgoingCompanyId, incomingCompanyId);
        LOGGER.log(Level.INFO, "部品関連POのデータを取得・更新しました。", methodName);

        List<String> poUuidList = new ArrayList<String>();
        List<String> shipmentUuidList = new ArrayList<String>();

        if (!outgoingPoInfo.getTblPoOutboundVoList().isEmpty()) {

            for (TblPoOutboundVo tblPoOutboundVo : outgoingPoInfo.getTblPoOutboundVoList()) {

                if (StringUtils.isEmpty(uuidMap.get(tblPoOutboundVo.getPoOutbound().getUuid()))) {

                    poUuidList.add(tblPoOutboundVo.getPoOutbound().getUuid());
                }
            }

        }

        if (!outgoingPoInfo.getTblShipmentOutboundVoList().isEmpty()) {

            for (TblShipmentOutboundVo tblShipmentOutboundVo : outgoingPoInfo.getTblShipmentOutboundVoList()) {

                if (StringUtils.isEmpty(uuidMap.get(tblShipmentOutboundVo.getShipmentOutbound().getUuid()))) {

                    shipmentUuidList.add(tblShipmentOutboundVo.getShipmentOutbound().getUuid());
                }
            }

        }

        // POテーブルの連携ステータスを更新する
        String resultPo = FileUtil.sendPost(apiBaseUrl + EXT_COMPONENT_INSPECTION_OUTGOING_PO_DONE_API_URL, token,
                poUuidList);
        BasicResponse response = gson.fromJson(resultPo, BasicResponse.class);

        if (response == null || response.isError()) {

            LOGGER.log(Level.WARNING, "サプライヤーへ通信失敗しました。", methodName);
        }

        // 出荷テーブルの連携ステータスを更新する
        String resultShipment = FileUtil.sendPost(apiBaseUrl + EXT_COMPONENT_INSPECTION_OUTGOING_SHIPMENT_DONE_API_URL,
                token, shipmentUuidList);

        BasicResponse responseShipment = gson.fromJson(resultShipment, BasicResponse.class);

        if (responseShipment == null || responseShipment.isError()) {
            LOGGER.log(Level.WARNING, "サプライヤーへ通信失敗しました。", methodName);
        }
    }
    
    /**
     * tblPoに対し、tblPoOutbound情報を削除する
     * 
     * @param apiBaseUrl
     * @param token
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param methodName
     */
    public void delＴblPoOutboundInfo(String apiBaseUrl, String token, String outgoingCompanyId,
            String incomingCompanyId, String methodName) {

        try {
            String iDeletedKeys = FileUtil.getDataGet(apiBaseUrl + EXTLOGIN_DELETEDKEY_INSPECTION_API_URL, token);

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            TblDeletedKeyVo delKeys = gson.fromJson(iDeletedKeys, new TypeToken<TblDeletedKeyVo>() {
            }.getType());
            // 削除済みデータのテーブル名、IDをもとに、テーブルからデータを削除
            // 金型マスタは削除しない
            if (null != delKeys.getTblDeletedKeys() && delKeys.getTblDeletedKeys().size() > 0) {

                tblDeletedKeyService.updateExtDeletedKeysByBatch(delKeys.getTblDeletedKeys());

                LOGGER.log(Level.INFO, "削除キーテーブルのデータを取得・更新しました。", methodName);
            } else {
                LOGGER.log(Level.INFO, "削除キーテーブルのデータが見つかりません。", methodName);
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "削除キーテーブルのデータを取得・更新失敗しました。", methodName);
        }
    }
    
    public void getExtAborting(String apiBaseUrl, String token, String batName) throws IOException {
        TypeToken<ObjectResponse<List<String>>> typeToken = new TypeToken<ObjectResponse<List<String>>>() {};
        ObjectResponse<List<String>> resp = FileUtil.callGetAPI(apiBaseUrl + EXT_GET_ABORTING_RESULT_URL, token, typeToken);
        if(resp == null) {
            return;
        }
        List<String> abortingResultIds = resp.getObj();
        if(abortingResultIds.isEmpty()) {
            LOGGER.log(Level.INFO, "No inspection data are aborted.", batName);
            return;
        }
        List<String> abortedIds = this.tblComponentInspectionResultService.abort(abortingResultIds, AbortFlg.ABORTED, null);
        BasicResponse postResp = FileUtil.sendPost(apiBaseUrl + EXT_ABORTED_PUSH_URL, token, abortedIds, BasicResponse.class);
        if(postResp.isError()) {
            LOGGER.log(Level.WARNING, postResp.getErrorMessage(), batName);
        } else {
            LOGGER.log(Level.INFO, "Aborted inspection data are synced.", batName);
        }
    }
}
