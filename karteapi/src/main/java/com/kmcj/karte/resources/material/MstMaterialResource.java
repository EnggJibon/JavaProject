/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

/**
 * 材料マスタ リソース
 *
 * @author zds
 */
@RequestScoped
@Path("material")
public class MstMaterialResource {

    public MstMaterialResource() {
    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private CnfSystemService cnfSystemService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstMaterialService mstMaterialService;

    /**
     * M0019 材料マスタ 材料マスタ件数取得
     *
     * @param materialCode
     * @param materialName
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getMstMaterialCount(@QueryParam("materialCode") String materialCode, @QueryParam("materialName") String materialName) {

        CountResponse count = mstMaterialService.getMstMaterialCount(materialCode, materialName);

        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());
        if (count.getCount() > sysCount) {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            count.setError(true);
            count.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            count.setErrorMessage(msg);
        }

        return count;
    }

    /**
     * M0019 材料マスタ 材料マスタ複数取得
     *
     * @param materialCode
     * @param materialName
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMaterialList getMstMaterial(@QueryParam("materialCode") String materialCode, @QueryParam("materialName") String materialName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMaterialList mstMaterialList = mstMaterialService.getMstMaterial(materialCode, materialName, loginUser);
        return mstMaterialList;

    }

    /**
     * M0019 材料マスタ 編集された内容で材料マスタに追加・更新・削除を実行する。
     *
     * @param mstMaterialList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMstMaterial(MstMaterialList mstMaterialList) {

        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        List<MstMaterial> replacement = new ArrayList<>();
        if (mstMaterialList != null && mstMaterialList.getMstMaterialList().size() > 0) {

            for (int i = 0; i < mstMaterialList.getMstMaterialList().size(); i++) {
                for (int j = mstMaterialList.getMstMaterialList().size() - 1; j > i; j--) {
                    MstMaterial vo1 = mstMaterialList.getMstMaterialList().get(j);
                    MstMaterial vo2 = mstMaterialList.getMstMaterialList().get(i);
                    if (vo1.getMaterialCode().equals(vo2.getMaterialCode())) {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                        return response;
                    }
                }
            }
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_processing_record"));
            return response;
        }

        for (int i = 0; i < mstMaterialList.getMstMaterialList().size(); i++) {
            MstMaterial mstMaterials = mstMaterialList.getMstMaterialList().get(i);

            // 登録可能チェック
            if ("4".equals(mstMaterials.getOperationFlag())) {
                response = mstMaterialService.setCreateData(response, mstMaterials, loginUser);
                if (response.isError()) {
                    return response;
                }
                replacement.add(mstMaterials);
            }

            // 更新可能チェック
            if ("3".equals(mstMaterials.getOperationFlag())) {
                response = mstMaterialService.setUpdateData(response, mstMaterials, loginUser);
                if (response.isError()) {
                    return response;
                }
                replacement.add(mstMaterials);
            }

            // 削除可能チェック
            if ("1".equals(mstMaterials.getOperationFlag())) {
                mstMaterialService.setDeleteData(response, mstMaterials.getMaterialCode(), loginUser);
                if (response.isError()) {
                    return response;
                }
                replacement.add(mstMaterials);
            }
        }

        /*
         * 一括反映
         */
        for (MstMaterial mstMaterial : replacement) {
            if ("4".equals(mstMaterial.getOperationFlag())) {
                mstMaterialService.createMaterial(mstMaterial, loginUser);
            }
            if ("3".equals(mstMaterial.getOperationFlag())) {
                mstMaterialService.updateMaterial(mstMaterial, loginUser);
            }
            if ("1".equals(mstMaterial.getOperationFlag())) {
                mstMaterialService.deleteMaterial(mstMaterial.getMaterialCode());
            }
        }
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /**
     * M0019 材料マスタ 材料マスタより検索条件にあてはまるレコードを取得し、 材料コードの昇順でCSVファイルに出力する。
     *
     * @param materialCode
     * @param materialName
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMstMaterialCSV(@QueryParam("materialCode") String materialCode, @QueryParam("materialName") String materialName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMaterialService.getMstMaterialCSV(materialCode, materialName, loginUser);
    }

    /**
     * M0019 材料マスタ CSVファイルよりレコードを読み取り、 材料マスタへの追加・更新・削除を行う。
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMstMaterialCSV(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long deletedCount = 0;
        long failedCount = 0;

        String logFileUuid = IDGenerator.generate();
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
        importResultResponse.setLog(logFileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        ArrayList<List<String>> readList = CSVFileUtil.readCsv(csvFile);

        if (readList.size() <= 1) {
            return importResultResponse;
        } else {

            try {

                String userLangId = loginUser.getLangId();

                List dictKeys = new ArrayList();
                dictKeys.add("row_number");
                dictKeys.add("material_code");
                dictKeys.add("material_name");
                dictKeys.add("material_type");
                dictKeys.add("material_grade");
                dictKeys.add("asset_ctg");

                dictKeys.add("error");
                dictKeys.add("error_detail");
                dictKeys.add("db_process");
                dictKeys.add("msg_record_added");
                dictKeys.add("msg_data_modified");
                dictKeys.add("msg_record_deleted");
                dictKeys.add("mst_error_record_not_found");
                dictKeys.add("msg_error_wrong_csv_layout");

                dictKeys.add("msg_error_not_null");
                dictKeys.add("msg_error_over_length");
                dictKeys.add("msg_error_not_isnumber");

                Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(userLangId, dictKeys);

                String lineNo = dictMap.get("row_number");
                String materialCode = dictMap.get("material_code");
                String materialName = dictMap.get("material_name");
                String materialType = dictMap.get("material_type");
                String materialGrade = dictMap.get("material_grade");
                String assetCtg = dictMap.get("asset_ctg");

                String error = dictMap.get("error");
                String errorContents = dictMap.get("error_detail");
                String result = dictMap.get("db_process");

                String addedMsg = dictMap.get("msg_record_added");
                String updatedMsg = dictMap.get("msg_data_modified");
                String deletedMsg = dictMap.get("msg_record_deleted");

                String notFound = dictMap.get("mst_error_record_not_found");
                String layout = dictMap.get("msg_error_wrong_csv_layout");

                String nullMsg = dictMap.get("msg_error_not_null");
                String maxLangth = dictMap.get("msg_error_over_length");
                String notIsnumber = dictMap.get("msg_error_not_isnumber");

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("materialCode", materialCode);
                logParm.put("materialName", materialName);
                logParm.put("materialType", materialType);
                logParm.put("materialGrade", materialGrade);
                logParm.put("assetCtg", assetCtg);

                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("notFound", notFound);
                logParm.put("maxLangth", maxLangth);
                logParm.put("notIsnumber", notIsnumber);

                Map<String, Integer> inMap = new HashMap<>();
                MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_material.asset_ctg");
                for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                    inMap.put(mstChoice.getChoice(), Integer.parseInt(mstChoice.getMstChoicePK().getSeq()));
                }
                Map<String, Integer> inMap2 = new HashMap<>();
                mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_material.stock_unit");
                for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                    inMap2.put(mstChoice.getChoice(), Integer.parseInt(mstChoice.getMstChoicePK().getSeq()));
                }

                MstMaterial readCsvInfo;
                FileUtil fu = new FileUtil();
                
                Map<String, Integer> colIndexMap = new HashMap<>();
                
                for (int j = 0; j < readList.get(0).size() - 1; j++) {
                    if (readList.get(0).get(j).trim().equals(dictMap.get("material_code"))) {
                        colIndexMap.put("materialCode", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_name"))) {
                        colIndexMap.put("materialName", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_type"))) {
                        colIndexMap.put("materialType", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_grade"))) {
                        colIndexMap.put("materialGrade", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("asset_ctg"))) {
                        colIndexMap.put("assetCtg", j);
                    }
                }

                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);
                    //ヘーダー列数と値の列数異なる場合、エラーとなる
                    if (((ArrayList) readList.get(0)).size() != comList.size()) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialCode, "", error, 1, errorContents, layout));
                        failedCount += 1;
                        continue;
                    }

                    readCsvInfo = new MstMaterial();
                    //材料コード
                    if (colIndexMap.get("materialCode") != null) {
                        readCsvInfo.setMaterialCode(String.valueOf(comList.get(colIndexMap.get("materialCode"))).trim());
                    }

                    String operationFlag = String.valueOf(comList.get(comList.size() - 1));

                    //operationFlag 指定されている場合 
                    if (operationFlag != null && "1".equals(operationFlag.trim())) {
                        if (mstMaterialService.checkMaterial(readCsvInfo.getMaterialCode())) {
                            //削除
                            mstMaterialService.deleteMaterial(readCsvInfo.getMaterialCode());
                            deletedCount = deletedCount + 1;
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialCode, readCsvInfo.getMaterialCode(), error, 0, result, deletedMsg));
                        } else {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialCode, readCsvInfo.getMaterialCode(), error, 1, errorContents, notFound));
                        }
                    } else {
                        // 材料名称
                        if (colIndexMap.get("materialName") != null) {
                            readCsvInfo.setMaterialName(String.valueOf(comList.get(colIndexMap.get("materialName"))).trim());
                        }
                        // 材質
                        if (colIndexMap.get("materialType") != null) {
                            readCsvInfo.setMaterialType(String.valueOf(comList.get(colIndexMap.get("materialType"))).trim());
                        }
                        // グレード
                        if (colIndexMap.get("materialGrade") != null) {
                            readCsvInfo.setMaterialGrade(String.valueOf(comList.get(colIndexMap.get("materialGrade"))).trim());
                        }
                        //資産区分
                        if (colIndexMap.get("assetCtg") == null) {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetCtg, "", error, 1, errorContents, nullMsg));
                            continue;
                        } else {
                            String strAssetCtgValue = String.valueOf(comList.get(colIndexMap.get("assetCtg")));
                            if (StringUtils.isEmpty(strAssetCtgValue)) {
                                //エラー情報をログファイルに記入
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetCtg, strAssetCtgValue, error, 1, errorContents, nullMsg));
                                continue;
                            } else {
                                Integer numAssetCtg = inMap.get(strAssetCtgValue.trim());
                                if (numAssetCtg == null) {
                                    //エラー情報をログファイルに記入
                                    failedCount = failedCount + 1;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetCtg, strAssetCtgValue, error, 1, errorContents, notFound));
                                    continue;
                                } else {
                                    readCsvInfo.setAssetCtg(numAssetCtg);
                                }
                            }
                        }
                        
                        if (mstMaterialService.checkCsvFileData(logParm, readCsvInfo, userLangId, logFile, i)) {
                            if (mstMaterialService.checkMaterial(readCsvInfo.getMaterialCode())) {
                                //更新
                                MstMaterial mstMaterial = entityManager.find(MstMaterial.class, readCsvInfo.getMaterialCode());
                                readCsvInfo.setId(mstMaterial.getId());
                                mstMaterialService.updateMaterial(readCsvInfo, loginUser);
                                updatedCount = updatedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialCode, readCsvInfo.getMaterialCode(), error, 0, result, updatedMsg));
                            } else {
                                //追加
                                mstMaterialService.createMaterial(readCsvInfo, loginUser);
                                addedCount = addedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialCode, readCsvInfo.getMaterialCode(), error, 0, result, addedMsg));
                            }
                        } else {
                            failedCount = failedCount + 1;
                        }
                    }
                }//end loop
            } catch (Exception e) {
                importResultResponse.setError(true);
                importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid");
                importResultResponse.setErrorMessage(msg);
                return importResultResponse;
            }
        }
        // リターン情報
        succeededCount = addedCount + updatedCount + deletedCount;
        importResultResponse.setTotalCount(readList.size() - 1);
        importResultResponse.setSucceededCount(succeededCount);
        importResultResponse.setAddedCount(addedCount);
        importResultResponse.setUpdatedCount(updatedCount);
        importResultResponse.setDeletedCount(deletedCount);
        importResultResponse.setFailedCount(failedCount);
        importResultResponse.setLog(logFileUuid);
        //アップロードログをテーブルに書き出し
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_MATERIAL);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MATERIAL);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MATERIAL);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;
    }

    /**
     * 材料マスタの入力候補表示 equal
     *
     * @param materialCode
     * @param materialName
     * @return
     */
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMaterialList getMaterialByCodeOrName(@QueryParam("materialCode") String materialCode, @QueryParam("materialName") String materialName) {
        return mstMaterialService.getMaterialByCodeOrName(materialCode, materialName);
    }

    /**
     * 材料マスタの入力候補表示 like
     *
     * @param materialCode
     * @param materialName
     * @return
     */
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMaterialList getMaterialLikeCodeOrName(@QueryParam("materialCode") String materialCode, @QueryParam("materialName") String materialName) {
        return mstMaterialService.getMaterialLikeCodeOrName(materialCode, materialName);
    }

    /**
     * M0019 材料マスタ 材料マスタ複数取得
     *
     * @param materialCode
     * @param materialName
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMaterialList getMstMaterialByPage(@QueryParam("materialCode") String materialCode,
            @QueryParam("materialName") String materialName, @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMaterialList mstMaterialList = mstMaterialService.getMstMaterialByPage(materialCode, materialName, loginUser,
                sidx, sord, pageNumber, pageSize, true);
        return mstMaterialList;

    }
}
