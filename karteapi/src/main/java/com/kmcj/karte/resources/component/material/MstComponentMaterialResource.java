/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.material;

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
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.material.MstMaterialService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("component/material")
public class MstComponentMaterialResource {

    /**
     * Creates a new instance of MstLocationResource
     */
    public MstComponentMaterialResource() {
    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstComponentMaterialService mstComponentMaterialService;

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
    private MstComponentService mstComponentService;
    @Inject
    private MstMaterialService mstMaterialService;
    @Inject
    private MstProcedureService mstProcedureService;

    /**
     * 部品別材料件数を取得する
     *
     * @param componentCode
     * @param componentName
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getComponentMaterialCount(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName) {
        CountResponse count = mstComponentMaterialService.getComponentMaterialCount(componentCode, componentName);
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
     * 部品別材料複数取得
     *
     * @param componentCode
     * @param componentName
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentMaterialList getComponentMaterials(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName) {
        return mstComponentMaterialService.getComponentMaterials(componentCode, componentName);
    }

    /**
     * 部品別材料取得(getComponentMaterial)
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentMaterialVo getComponentMaterial(@PathParam("id") String id) {
        MstComponentMaterialVo mstComponentMaterialVo = null;
        if (mstComponentMaterialService.getComponentMaterial(id).getMstComponentMaterialsList().size() > 0) {
            mstComponentMaterialVo = mstComponentMaterialService.getComponentMaterial(id).getMstComponentMaterialsList().get(0);
        } else {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            mstComponentMaterialVo = new MstComponentMaterialVo();
            mstComponentMaterialVo.setError(true);
            mstComponentMaterialVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstComponentMaterialVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return mstComponentMaterialVo;
    }

    /**
     * 新規の部品別材を追加する
     *
     * @param mstComponentMaterialV0
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentMaterialVo postComponentMaterial(MstComponentMaterialVo mstComponentMaterialV0) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BigDecimal denominator = new BigDecimal(mstComponentMaterialV0.getRequiredQuantityDenominator());
        int flag = denominator.compareTo(new BigDecimal(0));
        if (!mstComponentMaterialService.checkParam(mstComponentMaterialV0)) {
            mstComponentMaterialV0.setError(true);
            mstComponentMaterialV0.setErrorCode(ErrorMessages.E201_APPLICATION);
            if (flag != 0) {
                mstComponentMaterialV0.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            } else {
                mstComponentMaterialV0.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_numerator_not_zero"));
            }
        } else if (mstComponentMaterialService.getComponentMaterialExistCheck(
                mstComponentMaterialV0.getComponentId(), mstComponentMaterialV0.getProcedureCode(), mstComponentMaterialV0.getMaterialId(), mstComponentMaterialV0.getApplicationStartDate()) != null) {
            mstComponentMaterialV0.setError(true);
            mstComponentMaterialV0.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstComponentMaterialV0.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
        } else {
            mstComponentMaterialV0.setId(IDGenerator.generate());
            mstComponentMaterialService.createMstComponentMaterial(mstComponentMaterialV0, loginUser);
        }
        return mstComponentMaterialV0;
    }

    /**
     * 部品別材削除（deleteMstComponentMaterial）
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMstComponentMaterial(@PathParam("id") String id) {
        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (!mstComponentMaterialService.getMstComponentMaterialExistCheck(id)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        } else {
            mstComponentMaterialService.deleteMstComponentMaterial(id);
        }

        return response;
    }

    /**
     * 部品別材更新（putMstComponentMaterial)
     *
     * @param mstComponentMaterialV0
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMstComponentMaterial(MstComponentMaterialVo mstComponentMaterialV0) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BigDecimal denominator = new BigDecimal(mstComponentMaterialV0.getRequiredQuantityDenominator());
        int flag = denominator.compareTo(new BigDecimal(0));
        BasicResponse response = new BasicResponse();
        if (!mstComponentMaterialService.getMstComponentMaterialExistCheck(mstComponentMaterialV0.getId())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        } else if (!mstComponentMaterialService.checkParam(mstComponentMaterialV0)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            if (flag != 0) {
                mstComponentMaterialV0.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            } else {
                mstComponentMaterialV0.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_numerator_not_zero"));
            }
        }
        mstComponentMaterialService.updateMstComponentMaterialByQuery(mstComponentMaterialV0, loginUser);
        return response;
    }

    /**
     * 部品別材CSV出力（getMstComponentMaterialsCsv)
     *
     * @param componentCode
     * @param componentName
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMstComponentMaterialsCsv(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentMaterialService.getMstComponentMaterialsOutputCsv(componentCode, componentName, loginUser);
    }

    /**
     * 部品別材CSV取込（postLocationsCsv)
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postComponentMaterialsCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long deletedCount = 0;
        long failedCount = 0;
        String logFileUuid = IDGenerator.generate();
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }
        CSVFileUtil csvFileUtil = null;
        ArrayList readList = new ArrayList();
        try {
            csvFileUtil = new CSVFileUtil(csvFile);
            boolean readEnd = false;
            do {
                String readLine = csvFileUtil.readLine();
                if (readLine == null) {
                    readEnd = true;
                } else {
                    readList.add(CSVFileUtil.fromCSVLinetoArray(readLine));
                }
            } while (!readEnd);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }

        if (readList.size() <= 1) {
            return importResultResponse;
        } else {
            try {
                String userLangId = loginUser.getLangId();
                String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
                String lineNo = mstDictionaryService.getDictionaryValue(userLangId, "row_number");
                String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
                String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
                String result = mstDictionaryService.getDictionaryValue(userLangId, "db_process");
                String addedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_added");
                String updatedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_data_modified");
                String deletedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_deleted");
                String notFound = mstDictionaryService.getDictionaryValue(userLangId, "mst_error_record_not_found");
                String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");
                String invalidDate = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_date_format_invalid");
                String notZero = mstDictionaryService.getDictionaryValue(userLangId, "msg_numerator_not_zero");

                String componentCode = mstDictionaryService.getDictionaryValue(userLangId, "component_code");
                String componentName = mstDictionaryService.getDictionaryValue(userLangId, "component_name");
                String procedureCode = mstDictionaryService.getDictionaryValue(userLangId, "procedure_code");
                String procedureName = mstDictionaryService.getDictionaryValue(userLangId, "procedure_name");
                String materialCode = mstDictionaryService.getDictionaryValue(userLangId, "material_code");
                String materialName = mstDictionaryService.getDictionaryValue(userLangId, "material_name");
                String numerator = mstDictionaryService.getDictionaryValue(userLangId, "numerator");
                String denominator = mstDictionaryService.getDictionaryValue(userLangId, "denominator");
                String applyingStartDate = mstDictionaryService.getDictionaryValue(userLangId, "applying_start_date");
                String applyingEndDate = mstDictionaryService.getDictionaryValue(userLangId, "applying_end_date");

                String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
                String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");
                String notNumber = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_isnumber");

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("componentCode", componentCode);
                logParm.put("componentName", componentName);
                logParm.put("procedureCode", procedureCode);
                logParm.put("procedureName", procedureName);
                logParm.put("materialCode", materialCode);
                logParm.put("materialName", materialName);
                logParm.put("numerator", numerator);
                logParm.put("denominator", denominator);
                logParm.put("applyingStartDate", applyingStartDate);
                logParm.put("applyingEndDate", applyingEndDate);

                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("notFound", notFound);
                logParm.put("maxLangth", maxLangth);
                logParm.put("layout", layout);
                logParm.put("notNumber", notNumber);
                logParm.put("invalidDate", invalidDate);
                logParm.put("notZero", notZero);

                MstComponentMaterialVo readCsvInfoVo;

                FileUtil fu = new FileUtil();
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);

                    if (comList.size() != 11) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, "", error, 1, errorContents, layout));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    String[] csvArray = new String[comList.size()];
                    String strComponentCode = String.valueOf(comList.get(0));
                    csvArray[0] = strComponentCode;
                    String strComponentName = String.valueOf(comList.get(1));
                    csvArray[1] = strComponentName;
                    String strProcedureCode = String.valueOf(comList.get(2));
                    csvArray[2] = strProcedureCode;
                    String strProcedureName = String.valueOf(comList.get(3));
                    csvArray[3] = strProcedureName;
                    String strMaterialCode = String.valueOf(comList.get(4));
                    csvArray[4] = strMaterialCode;
                    String strMaterialName = String.valueOf(comList.get(5));
                    csvArray[5] = strMaterialName;
                    String strNumerator = String.valueOf(comList.get(6)).trim();
                    csvArray[6] = strNumerator;
                    String strDenominator = String.valueOf(comList.get(7)).trim();
                    csvArray[7] = strDenominator;
                    String strApplyingStartDate = String.valueOf(comList.get(8));
                    if (null != strApplyingStartDate && !strApplyingStartDate.isEmpty()) {
                        strApplyingStartDate = DateFormat.formatDateYear(strApplyingStartDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[8] = strApplyingStartDate.equals("-1") ? String.valueOf(comList.get(8)) : strApplyingStartDate;
                    String strApplyingEndDate = String.valueOf(comList.get(9));
                    if (null != strApplyingEndDate && !strApplyingEndDate.isEmpty()) {
                        strApplyingEndDate = DateFormat.formatDateYear(strApplyingEndDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[9] = strApplyingEndDate.equals("-1") ? String.valueOf(comList.get(9)) : strApplyingEndDate;
                    String strDelFlag = String.valueOf(comList.get(10));

                    MstComponent mstComponent = mstComponentService.getMstComponent(strComponentCode);
                    if (mstComponent == null) {
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 1, errorContents, notFound));
                        continue;
                    }
                    MstMaterial mstMaterial = mstMaterialService.getMstMaterialByCode(strMaterialCode);
                    if (mstMaterial == null) {
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialCode, strMaterialCode, error, 1, errorContents, notFound));
                        continue;
                    }
                    MstProcedure MstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(mstComponent.getId(), strProcedureCode);
                    MstComponentMaterial mstComponentMaterial = null;
                    if (mstComponent != null && mstMaterial != null) {
                        mstComponentMaterial = mstComponentMaterialService.getComponentMaterialExistCheck(
                                mstComponent.getId(), strProcedureCode, mstMaterial.getId(), strApplyingStartDate);
                    }

                    //delFlag 指定されている場合
                    if (strDelFlag != null && strDelFlag.equals("1")) {
                        if (mstComponentMaterial == null) {
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 1, errorContents, notFound));
                        } else {
                            mstComponentMaterialService.deleteMstComponentMaterial(mstComponentMaterial.getId());
                            deletedCount = deletedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 0, result, deletedMsg));
                        }
                    } else //check
                    {
                        if (mstComponentMaterialService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i)) {

                            readCsvInfoVo = new MstComponentMaterialVo();
                            if (mstComponent != null && mstMaterial != null) {
                                readCsvInfoVo.setMaterialId(mstMaterial.getId());
                                readCsvInfoVo.setComponentId(mstComponent.getId());
                            }
                            readCsvInfoVo.setProcedureCode(strProcedureCode);
                            readCsvInfoVo.setRequiredQuantityDenominator(strDenominator);
                            readCsvInfoVo.setRequiredQuantityMolecule(strNumerator);
                            readCsvInfoVo.setApplicationStartDate(strApplyingStartDate);
                            readCsvInfoVo.setApplicationEndDate(strApplyingEndDate);

                            if (mstComponentMaterial != null) {
                                //存在チェックの場合　//更新
                                // データを更新
                                readCsvInfoVo.setId(mstComponentMaterial.getId());
                                int count = mstComponentMaterialService.updateMstComponentMaterialByQuery(readCsvInfoVo, loginUser);
                                updatedCount = updatedCount + count;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 0, result, updatedMsg));
                            } else if (MstProcedure != null) {
                                //追加
                                readCsvInfoVo.setId(IDGenerator.generate());
                                mstComponentMaterialService.createMstComponentMaterial(readCsvInfoVo, loginUser);
                                addedCount = addedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 0, result, addedMsg));

                            } else {
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 1, errorContents, notFound));
                            }
                        } else {
                            failedCount = failedCount + 1;
                        }
                    }
                } //end loop
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
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_COMPONENT_MATERIAL);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_MST_COMPONENT_MATERIAL);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_component_material_maintentance");
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;

    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentMaterialList getListByComponentIdAndProceduerCode(@QueryParam("componentId") String componentId, @QueryParam("proceduerCode") String proceduerCode) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        MstComponentMaterialList response = mstComponentMaterialService.getListByComponentIdAndProceduerCode(componentId, proceduerCode);
        return response;
    }

    /**
     * 材料子画面検索
     *
     * @param componentId
     * @param materialName
     * @return
     */
    @GET
    @Path("materialinfo")
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentMaterialList getMaterialInfoByComponentIdAndName(@QueryParam("componentId") String componentId, @QueryParam("materialName") String materialName) {

        MstComponentMaterialList response = mstComponentMaterialService.getMaterialInfoByComponentIdAndName(componentId, materialName);
        return response;
    }
    
    /**
     * 部品別材料複数取得
     *
     * @param componentCode
     * @param componentName
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
    public MstComponentMaterialList getComponentMaterialsByPage(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName, @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        return mstComponentMaterialService.getComponentMaterialsByPage(componentCode, componentName, sidx, sord,
                pageNumber, pageSize, true);
    }
    
    @GET
    @Path("history")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentMaterial getSingleComponentMaterialFromHistory(
            @QueryParam("componentId") String componentId,
            @QueryParam("procedureCode") String procedureCode,
            @QueryParam("materialId") String materialId,
            @QueryParam("date") String date
    ) {
        return mstComponentMaterialService.getSingleComponentMaterialFromHistory(componentId, procedureCode, materialId, date);
    }

}
