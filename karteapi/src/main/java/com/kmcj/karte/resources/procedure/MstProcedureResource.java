/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.procedure;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentList;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.MstComponents;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;

/**
 * 生産実績ロット残高テーブルリソース
 *
 * @author t.ariki
 */
@RequestScoped
@Path("procedure")
public class MstProcedureResource {

    @Context
    private UriInfo context;

    @Context
    ContainerRequestContext requestContext;

    @Inject
    MstDictionaryService mstDictionaryService;

    @Inject
    MstProcedureService mstProcedureService;

    @Inject
    MstComponentService mstComponentService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    private Logger logger = Logger.getLogger(MstProcedureResource.class.getName());

    private static final String FINAL_FLAG_CHECKED = "1";
    private static final String FINAL_FLAG_NOT_CHECKED = "0";
    
    public MstProcedureResource() {
    }

    /**
     * 最初の工程取得
     *
     * @param componentId
     * @return
     */
    @GET
    @Path("first/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public MstProcedureList getFirstProcedure(@PathParam("componentId") String componentId) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        logger.log(Level.FINE, "PathParam'{'componentId:{0}'}'", componentId);
        MstProcedureList response = mstProcedureService.getFirstProcedure(componentId);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 最終工程工程
     *
     * @param componentId
     * @return
     */
    @GET
    @Path("final/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public MstProcedureList getFinalProcedure(@PathParam("componentId") String componentId) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        logger.log(Level.FINE, "PathParam'{'componentId:{0}'}'", componentId);
        MstProcedureList response = mstProcedureService.getFinalProcedure(componentId);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 最初の工程取得
     *
     * @param componentId
     * @return
     */
    @GET
    @Path("list/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public MstProcedureList getComponentProcedures(@PathParam("componentId") String componentId) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        logger.log(Level.FINE, "PathParam'{'componentId:{0}'}'", componentId);
        MstProcedureList response = mstProcedureService.getComponentProcedures(componentId);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 最初の工程取得
     *
     * @return
     */
    @GET
    @Path("list/all")
    @Produces(MediaType.APPLICATION_JSON)
    public MstProcedureList getAllProcedures() {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        MstProcedureList response = mstProcedureService.getAllProcedures();
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * M0018 工程登録	検索 部品マスタ、工程マスタより条件にあてはまるデータを取得し、一覧に表示する。
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @param processesNotRegistered
     * @return
     */
    @GET
    @Path("procedure")
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getComponentsByQuery(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentType") String componentType,
            @QueryParam("processesNotRegistered") String processesNotRegistered) {
        MstComponents queryVo = new MstComponents(componentCode, componentName, componentType, processesNotRegistered);
        return mstComponentService.getMstComponentsByQueryVo(queryVo);
    }

    /**
     * M0018 工程登録	画面に入力された値で工程マスタへ追加・更新・削除を行う。
     *
     * @param procedureVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postProcedures(MstProcedureVo procedureVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProcedureService.postProcedures(procedureVo, loginUser);
    }

    /**
     * M0018 CSV出力
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @param processesNotRegistered
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getCompaniesCsv(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentType") String componentType,
            @QueryParam("processesNotRegistered") String processesNotRegistered) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstComponents queryVo = new MstComponents(componentCode, componentName, componentType, processesNotRegistered);
        return mstProcedureService.getMstProceduresOutputCsv(queryVo, loginUser);
    }

    private BasicResponse setApplicationError(BasicResponse response, LoginUser loginUser, String dictKey, String errorContentsName) {
        setBasicResponseError(
                response, true, ErrorMessages.E201_APPLICATION, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
        );
        String logMessage = response.getErrorMessage() + ":" + errorContentsName;
        logger.log(Level.FINE, logMessage);
        return response;
    }

    private BasicResponse setBasicResponseError(BasicResponse response, boolean error, String errorCode, String errorMessage) {
        response.setError(error);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }

    /**
     * CSVファイルを取り込んで工程マスタへの追加・更新・削除を行う。
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMstProcedureCSV(@PathParam("fileUuid") String fileUuid) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long deletedCount = 0;
        long failedCount = 0;
        String logFileUuid = IDGenerator.generate();
        ImportResultResponse importResultResponse = new ImportResultResponse();
        importResultResponse.setLog(logFileUuid);
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        ArrayList readList = CSVFileUtil.readCsv(csvFile);

        if (readList.size() <= 1) {
            return importResultResponse;
        } else {
            try {
                String userLangId = loginUser.getLangId();
                String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

                ArrayList dictKeyList = new ArrayList();

                dictKeyList.add("row_number");
                dictKeyList.add("component_code");
                dictKeyList.add("component_name");
                dictKeyList.add("final_procedure");
                dictKeyList.add("category_seq");
                dictKeyList.add("procedure_code");
                dictKeyList.add("procedure_name");
                dictKeyList.add("instration_site");
                dictKeyList.add("error");
                dictKeyList.add("error_detail");
                dictKeyList.add("db_process");
                dictKeyList.add("msg_record_added");
                dictKeyList.add("msg_data_modified");
                dictKeyList.add("msg_record_deleted");
                dictKeyList.add("mst_error_record_not_found");
                dictKeyList.add("msg_error_over_length");
                dictKeyList.add("msg_error_wrong_csv_layout");
                dictKeyList.add("msg_error_data_deleted");
                dictKeyList.add("msg_error_not_null");
                dictKeyList.add("msg_error_not_isnumber");
                dictKeyList.add("msg_error_record_exists");

                Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(userLangId, dictKeyList);

                String lineNo = dictMap.get("row_number");
                String componentCode = dictMap.get("component_code");
                String componentName = dictMap.get("component_name");
                String finalProcedure = dictMap.get("final_procedure");
                String seq = dictMap.get("category_seq");
                String procedureCode = dictMap.get("procedure_code");
                String procedureName = dictMap.get("procedure_name");
                String instrationSite = dictMap.get("instration_site");
                String error = dictMap.get("error");
                String errorContents = dictMap.get("error_detail");
                String result = dictMap.get("db_process");
                String addedMsg = dictMap.get("msg_record_added");
                String updatedMsg = dictMap.get("msg_data_modified");
                String notFound = dictMap.get("mst_error_record_not_found");
                String maxLangth = dictMap.get("msg_error_over_length");
                String layout = dictMap.get("msg_error_wrong_csv_layout");
                String deletedErrMsg = dictMap.get("msg_error_data_deleted");
                String deletedMsg = dictMap.get("msg_record_deleted");
                String nullMsg = dictMap.get("msg_error_not_null");
                String notIsnumber = dictMap.get("msg_error_not_isnumber");
                String recordExists = dictMap.get("msg_error_record_exists");

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("componentName", componentName);
                logParm.put("procedureName", procedureName);

                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("notFound", notFound);
                logParm.put("maxLangth", maxLangth);
                logParm.put("notIsnumber", notIsnumber);

                MstProcedure readCsvInfo;
                List<String> whereKey = new ArrayList<>();
                Set<String> notUpdateKey = new HashSet<>();

                FileUtil fu = new FileUtil();

                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);
                    if (comList.size() < 9) {
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
                    String strFinalProcedure = String.valueOf(comList.get(2));
                    csvArray[2] = strFinalProcedure;
                    String strseq = String.valueOf(comList.get(3));
                    csvArray[3] = strseq;
                    String strProcedureCode = String.valueOf(comList.get(4)).trim();
                    csvArray[4] = strProcedureCode;
                    String strProcedureName = String.valueOf(comList.get(5)).trim();
                    csvArray[5] = strProcedureName;

                    String strInstrationSiteCode = String.valueOf(comList.get(6)).trim();

                    String operationFlag = String.valueOf(comList.get(8));

                    if (strComponentCode == null || "".equals(strComponentCode)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 1, errorContents, nullMsg));
                        continue;
                    }

                    String componentId = "";
                    MstComponent mstComponent = entityManager.find(MstComponent.class, strComponentCode);
                    if (mstComponent != null) {
                        componentId = mstComponent.getId();
                    } else {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 1, errorContents, notFound));
                        continue;
                    }
                    if (!whereKey.contains(componentId)) {
                        whereKey.add(componentId);
                    }

                    if (strFinalProcedure == null || "".equals(strFinalProcedure)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, finalProcedure, strFinalProcedure, error, 1, errorContents, nullMsg));
                        continue;
                    } else if (!fu.isNumber(strFinalProcedure)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, finalProcedure, strFinalProcedure, error, 1, errorContents, notIsnumber));
                        continue;
                    } else if (!FINAL_FLAG_CHECKED.equals(strFinalProcedure) && !FINAL_FLAG_NOT_CHECKED.equals(strFinalProcedure)) {
                        strFinalProcedure = FINAL_FLAG_NOT_CHECKED;
                    } else if (FINAL_FLAG_CHECKED.equals(strFinalProcedure)) {
                        if (notUpdateKey.contains(componentId)) {
                            strFinalProcedure = FINAL_FLAG_NOT_CHECKED;
                        }
                        notUpdateKey.add(componentId);
                    }
                    
                    if (strseq == null || "".equals(strseq)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, seq, strseq, error, 1, errorContents, nullMsg));
                        continue;
                    } else if (!fu.isNumber(strseq)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, seq, strseq, error, 1, errorContents, notIsnumber));
                        continue;
                    }

                    if (strProcedureCode == null || "".equals(strProcedureCode)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, procedureCode, strProcedureCode, error, 1, errorContents, nullMsg));
                        continue;
                    } else if (fu.maxLangthCheck(strProcedureCode, 45)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, procedureCode, strProcedureCode, error, 1, errorContents, maxLangth));
                        continue;
                    }

                    String strInstrationSiteId = null;

                    if (StringUtils.isNotEmpty(strInstrationSiteCode)) {
                        Query query = entityManager.createNamedQuery("MstInstallationSite.findByInstallationSiteCode");
                        query.setParameter("installationSiteCode", strInstrationSiteCode);
                        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                        try {
                            MstInstallationSite mstInstallationSite = (MstInstallationSite) query.getSingleResult();
                            strInstrationSiteId = mstInstallationSite.getId();
                        } catch (NoResultException e) {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, instrationSite, strInstrationSiteCode, error, 1, errorContents, notFound));
                            continue;
                        }
                    }

                    String id = mstProcedureService.getMstProcedureId(componentId, strProcedureCode, strseq);

                    //operationFlag 指定されている場合 
                    if (operationFlag != null && "1".equals(operationFlag.trim())) {
                        if (mstProcedureService.checkMstProcedure(componentId, strProcedureCode, strseq)) {
                            mstProcedureService.transactionalMstProcedure(id, null, "1");
                            deletedCount = deletedCount + 1;
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, procedureCode, strProcedureCode, error, 0, result, deletedMsg));
                        } else {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, procedureCode, strProcedureCode, error, 1, errorContents, deletedErrMsg));
                        }
                    } else if (mstProcedureService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i)) {
                        readCsvInfo = new MstProcedure();
                        readCsvInfo.setComponentId(componentId);
                        readCsvInfo.setFinalFlg(Integer.parseInt(strFinalProcedure));
                        readCsvInfo.setSeq(Integer.parseInt(strseq));
                        readCsvInfo.setProcedureCode(strProcedureCode);
                        readCsvInfo.setProcedureName(strProcedureName);
                        readCsvInfo.setInstallationSiteId(strInstrationSiteId);
                        if (mstProcedureService.checkMstProcedure(componentId, strProcedureCode, strseq)) {
                            //更新
                            readCsvInfo.setId(id);
                            readCsvInfo.setUpdateDate(new Date());
                            readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                            readCsvInfo.setCreateDate(new Date());
                            readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                            readCsvInfo.setExternalFlg(0);
                            mstProcedureService.transactionalMstProcedure("", readCsvInfo, "3");
                            updatedCount = updatedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, procedureCode, strProcedureCode, error, 0, result, updatedMsg));
                        } else {
                            //追加
                            readCsvInfo.setId(IDGenerator.generate());
                            readCsvInfo.setUpdateDate(new Date());
                            readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                            readCsvInfo.setCreateDate(new Date());
                            readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                            if (!mstProcedureService.checkProcedure(componentId, strProcedureCode)) {
                                mstProcedureService.transactionalMstProcedure("", readCsvInfo, "4");
                                addedCount = addedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, procedureCode, strProcedureCode, error, 0, result, addedMsg));
                            } else {
                                //エラー情報をログファイルに記入
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, procedureCode, strProcedureCode, error, 1, errorContents, recordExists));
                            }
                        }
                    } else {
                        failedCount = failedCount + 1;
                    }

                }

                if (whereKey != null && whereKey.size() > 0) {
                    mstProcedureService.updateSeq(whereKey);
                }

                for (String componentId : whereKey) {
                    if (!notUpdateKey.contains(componentId)) {
                        mstProcedureService.updateFinalFlag(componentId);
                    }
                }
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
        tblCsvImport.setImportTable(CommonConstants.MST_PROCEDURE);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_PROCEDURE);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.MST_PROCEDURE);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;
    }

    //CSV入力 end
    /**
     * バッチで工程テーブルデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extprocedure")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstProcedureList getExtProceduresByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {

        return mstProcedureService.getExtProceduresByBatch(latestExecutedDate);
    }

    @GET
    @Path("getprocedurebycomponentId")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<MstProcedureVo> getProcedureByComponentId(@QueryParam("componentId") String componentId) {
        return mstProcedureService.getProcedureByComponentId(componentId);
    }

    /**
     *
     * @param componentId
     * @return
     */
    @GET
    @Path("maxprocedurecode/{componentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstProcedureVo getMaxProcedureCodeByComponentId(@PathParam("componentId") String componentId) {
        MstProcedureVo mstProcedureVo = new MstProcedureVo();
        MstProcedure mstProcedure = mstProcedureService.getMaxProcedureCode(componentId);
        if (mstProcedure != null) {
            mstProcedureVo.setId(mstProcedure.getId());
            mstProcedureVo.setProcedureCode(mstProcedure.getProcedureCode());
            mstProcedureVo.setProcedureName(mstProcedure.getProcedureName());
        } else {
            mstProcedureVo.setId("");
            mstProcedureVo.setProcedureCode("");
            mstProcedureVo.setProcedureName("");
        }

        return mstProcedureVo;
    }
}
