/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.plan;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
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
import javax.ws.rs.core.UriInfo;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.direction.TblDirectionService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * 生産計画テーブルリソース
 *
 * @author t.ariki
 */
@RequestScoped
@Path("production/plan")
public class TblProductionPlanResource {

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
    TblDirectionService tblDirectionService;

    @Inject
    private KartePropertyService kartePropertyService;

    private Logger logger = Logger.getLogger(TblProductionPlanResource.class.getName());

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    TblProductionPlanService tblProductionPlanService;
    @Inject
    private TblCsvImportService tblCsvImportService;

    public TblProductionPlanResource() {
    }

    /**
     * 生産計画テーブルから条件にあてはまるデータを取得し、工程納期の降順、部品コード、工番の昇順で表示する。
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param finalProcedureOnly
     * @param directionNumber
     * @param directionId
     * @param completeFlg
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionPlanList getTblProductionPlans(
            @QueryParam("componentCode") String componentCode,
            @QueryParam("procedureDueDateFrom") String procedureDueDateFrom,
            @QueryParam("procedureDueDateTo") String procedureDueDateTo,
            @QueryParam("finalProcedureOnly") int finalProcedureOnly,
            @QueryParam("directionNumber") String directionNumber,
            @QueryParam("directionId") String directionId,
            @QueryParam("completeFlg") String completeFlg) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblProductionPlanList tblProductionPlanList = tblProductionPlanService.getTblProductionPlans(
                componentCode,
                procedureDueDateFrom, procedureDueDateTo,
                finalProcedureOnly,
                directionNumber,
                directionId,
                completeFlg,
                loginUser);
        return tblProductionPlanList;
    }

    /**
     * システム設定の一覧表示最大件数を超える場合は警告。
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param directionNumber
     * @param completeFlg
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getgetTblProductionPlanCount(
            @QueryParam("componentCode") String componentCode,
            @QueryParam("procedureDueDateFrom") String procedureDueDateFrom,
            @QueryParam("procedureDueDateTo") String procedureDueDateTo,
            @QueryParam("directionNumber") String directionNumber,
            @QueryParam("completeFlg") String completeFlg) {
        CountResponse count = tblProductionPlanService.getRecordCount(
                componentCode,
                procedureDueDateFrom,
                procedureDueDateTo,
                directionNumber,
                completeFlg);
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
     * 選択されている生産計画情報の詳細画面に読み取り専用で遷移する。
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionPlanVo getProductionPlanDetail(@PathParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String timeZone = loginUser.getJavaZoneId();
        return tblProductionPlanService.getProductionPlanDetails(id, timeZone);
    }

    /**
     * 確認メッセージを出力の上、選択されている生産計画情報を削除する。
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteTblProductionPlan(@PathParam("id") String id) {
        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (!tblProductionPlanService.getTblProductionPlanExistCheck(id)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        } else {
            tblProductionPlanService.deleteTblProductionPlan(id);
        }

        return response;
    }

    /**
     * 生産計画テーブル更新
     *
     * @param tblProductionPlanVo
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putTblProductionPlan(TblProductionPlanVo tblProductionPlanVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        if (!tblProductionPlanService.getTblProductionPlanExistCheck(tblProductionPlanVo.getId())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }

        TblProductionPlan tblProductionPlan = new TblProductionPlan();
        tblProductionPlan.setComponentId(tblProductionPlanVo.getComponentId());
        tblProductionPlan.setProcedureId(tblProductionPlanVo.getProcedureId());
        tblProductionPlan.setDirectionId(tblProductionPlanVo.getDirectionId());
        tblProductionPlan.setProcedureDueDate(new Date(tblProductionPlanVo.getProcedureDueDate()));
        // 数量check
        try {
            Integer.valueOf(tblProductionPlanVo.getQuantity());
        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid"));
            return response;
        }

        // input check
        response = tblProductionPlanService.checkTblProductionPlan(tblProductionPlan, loginUser);
        if (response.isError()) {
            return response;
        }

        tblProductionPlanService.updateTblProductionPlanByVoQuery(tblProductionPlanVo, loginUser);
        return response;
    }

    /**
     * 画面で入力された値を用いて生産計画テーブルへ追加.更新を行う。
     *
     * @param tblProductionPlan
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionPlanVo postTblProductionPlan(TblProductionPlanVo tblProductionPlan) {
        TblProductionPlanVo response = new TblProductionPlanVo();
        BasicResponse basic = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        if (tblProductionPlanService.ProductionPlanExistChecks(tblProductionPlan.getComponentId(), tblProductionPlan.getProcedureId(), tblProductionPlan.getDirectionId())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }

        TblProductionPlan newTblProductionPlan = new TblProductionPlan();
        newTblProductionPlan.setComponentId(tblProductionPlan.getComponentId() == null ? "" : tblProductionPlan.getComponentId());
        newTblProductionPlan.setProcedureId(tblProductionPlan.getProcedureId() == null ? "" : tblProductionPlan.getProcedureId());
        if (tblProductionPlan.getQuantity() != null && !"".equals(tblProductionPlan.getQuantity())) {
            newTblProductionPlan.setQuantity(Integer.parseInt(tblProductionPlan.getQuantity()));
        } else {
            newTblProductionPlan.setQuantity(0);
        }
        try {
            if (tblProductionPlan.getProcedureDueDate() != null && !"".equals(tblProductionPlan.getProcedureDueDate())) {
                newTblProductionPlan.setProcedureDueDate(sdf.parse(tblProductionPlan.getProcedureDueDate()));
            } else {
                newTblProductionPlan.setProcedureDueDate(null);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionPlanResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (tblProductionPlan.getDirectionId() != null && !"".equals(tblProductionPlan.getDirectionId())) {
            newTblProductionPlan.setDirectionId(tblProductionPlan.getDirectionId());
        }

        // input check
        basic = tblProductionPlanService.checkTblProductionPlan(newTblProductionPlan, loginUser);
        if (response.isError()) {
            return response;
        }
        // 追加
        basic = tblProductionPlanService.createTblProductionPlan(newTblProductionPlan, loginUser);
        response.setId(newTblProductionPlan.getId());
        response.setError(basic.isError());
        response.setErrorCode(basic.getErrorCode());
        response.setErrorMessage(basic.getErrorMessage());
        return response;
    }

    /**
     * 生産計画テーブルから条件にあてはまるデータを取得し、工程納期の降順、部品コード、工順の昇順でCSVファイルに出力する。
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param finalProcedureOnly
     * @param directionNumber
     * @param completeFlg
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblProductionPlansCSV(@QueryParam("componentCode") String componentCode,
            @QueryParam("procedureDueDateFrom") String procedureDueDateFrom,
            @QueryParam("procedureDueDateTo") String procedureDueDateTo,
            @QueryParam("finalProcedureOnly") int finalProcedureOnly,
            @QueryParam("directionNumber") String directionNumber,
            @QueryParam("completeFlg") String completeFlg) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblProductionPlanService.getTblProductionPlansOutputCsv(componentCode, procedureDueDateFrom, procedureDueDateTo, finalProcedureOnly, directionNumber, completeFlg, loginUser);
    }

    /**
     * CSVファイルから生産計画テーブルへデータの追加・更新・削除を行う。
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postTblProductionPlansCSV(@PathParam("fileUuid") String fileUuid) {
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
                String langId = loginUser.getLangId();
                String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
                String lineNo = mstDictionaryService.getDictionaryValue(langId, "row_number");
                //String id = mstDictionaryService.getDictionaryValue(langId, "id");
                String error = mstDictionaryService.getDictionaryValue(langId, "error");
                String errorContents = mstDictionaryService.getDictionaryValue(langId, "error_detail");
                String result = mstDictionaryService.getDictionaryValue(langId, "db_process");
                String addedMsg = mstDictionaryService.getDictionaryValue(langId, "msg_record_added");
                String notFound = mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found");
                String invalidDate = mstDictionaryService.getDictionaryValue(langId, "msg_error_date_format_invalid");
                String layout = mstDictionaryService.getDictionaryValue(langId, "msg_error_wrong_csv_layout");
                String notNumber = mstDictionaryService.getDictionaryValue(langId, "msg_error_not_isnumber");

                //String componentId = mstDictionaryService.getDictionaryValue(langId, "component_id");
                String componentCode = mstDictionaryService.getDictionaryValue(langId, "component_code");
                String componentName = mstDictionaryService.getDictionaryValue(langId, "component_name");
                //String procedureId = mstDictionaryService.getDictionaryValue(langId, "procedureCodet_id");
                String procedureCode = mstDictionaryService.getDictionaryValue(langId, "procedure_code");
                String procedureName = mstDictionaryService.getDictionaryValue(langId, "procedure_name");
                String quantity = mstDictionaryService.getDictionaryValue(langId, "quantity");
                String procedureDueDate = mstDictionaryService.getDictionaryValue(langId, "procedure_due_date");
                //String directionId = mstDictionaryService.getDictionaryValue(langId, "directionNumber_id");
                String directionCode = mstDictionaryService.getDictionaryValue(langId, "direction_number");
                String nullMsg = mstDictionaryService.getDictionaryValue(langId, "msg_error_not_null");
                String maxLangth = mstDictionaryService.getDictionaryValue(langId, "msg_error_over_length");
                String isExist = mstDictionaryService.getDictionaryValue(langId, "msg_error_record_exists");

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("componentCode", componentCode);
                logParm.put("componentName", componentName);
                logParm.put("procedureCode", procedureCode);
                logParm.put("procedureName", procedureName);
                logParm.put("quantity", quantity);
                logParm.put("procedureDueDate", procedureDueDate);
                logParm.put("directionCode", directionCode);
                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("notFound", notFound);
                logParm.put("invalidDate", invalidDate);
                logParm.put("maxLangth", maxLangth);
                logParm.put("notNumber", notNumber);
                logParm.put("layout", layout);

                TblProductionPlan readCsvInfo;
                FileUtil fu = new FileUtil();
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList planList = (ArrayList) readList.get(i);

                    if (planList.size() != 7) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, "", error, 1, errorContents, layout));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    String[] csvArray = new String[planList.size()];
                    String strComponentCode = String.valueOf(planList.get(0));
                    csvArray[0] = strComponentCode;
                    String strComponentName = String.valueOf(planList.get(1));
                    csvArray[1] = strComponentName;
                    String strProcedureCode = String.valueOf(planList.get(2));
                    csvArray[2] = strProcedureCode;
                    String strProcedureName = String.valueOf(planList.get(3));
                    csvArray[3] = strProcedureName;
                    String strQuantity = String.valueOf(planList.get(4));
                    csvArray[4] = strQuantity;
                    String strProcedureDueDate = String.valueOf(planList.get(5));
                    if (null != strProcedureDueDate && !strProcedureDueDate.isEmpty()) {
                        strProcedureDueDate = DateFormat.formatDateYear(strProcedureDueDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[5] = strProcedureDueDate.equals("-1") ? String.valueOf(planList.get(5)) : strProcedureDueDate;
                    String strDirectionCode = String.valueOf(planList.get(6)).trim();
                    csvArray[6] = strDirectionCode;
                    //String strDelFlag = String.valueOf(planList.get(7));

                    MstComponent mstComponent = mstComponentService.getMstComponent(strComponentCode);
                    MstProcedure mstProcedure = null;
                    if (mstComponent != null) {
                        mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(mstComponent.getId(), strProcedureCode);
                    }
                    TblDirection tblDirection = tblDirectionService.getTblDirectionByDirectionCode(strDirectionCode);
                    if (tblProductionPlanService.checkCsvFileData(logParm, csvArray, langId, logFile, i)) {
                        readCsvInfo = new TblProductionPlan();
                        readCsvInfo.setComponentId(mstComponent.getId());
                        readCsvInfo.setProcedureId(mstProcedure.getId());
                        if ("".equals(strDirectionCode)) {
                            readCsvInfo.setDirectionId(null);
                        } else {
                            readCsvInfo.setDirectionId(tblDirection.getId());
                        }
                        readCsvInfo.setQuantity(Integer.valueOf(strQuantity.trim()));
                        readCsvInfo.setProcedureDueDate(fu.getDateParseForDate(strProcedureDueDate));

//                    if (!tblProductionPlanService.ProductionPlanExistChecks(mstComponent.getId(), mstProcedure.getId(), tblDirection.getId())) {
                        //追加
                        Date sysDate = new Date();
                        readCsvInfo.setId(IDGenerator.generate());
                        readCsvInfo.setCreateDate(sysDate);
                        readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                        tblProductionPlanService.createTblProductionPlan(readCsvInfo, loginUser);
                        addedCount = addedCount + 1;

                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 0, result, addedMsg));
//                    } else {
//                        failedCount = failedCount + 1;
//                        //エラー情報をログファイルに記入
//                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 0, result, isExist));
//                    }
                    } else {
                        failedCount = failedCount + 1;
                    } //end loop
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
                tblCsvImport.setImportTable(CommonConstants.TBL_PRODUCTION_PLAN);
                TblUploadFile tblUploadFile = new TblUploadFile();
                tblUploadFile.setFileUuid(fileUuid);
                tblCsvImport.setUploadFileUuid(tblUploadFile);
                MstFunction mstFunction = new MstFunction();
                mstFunction.setId(CommonConstants.FUN_PRODUCTION_PLAN);
                tblCsvImport.setFunctionId(mstFunction);
                tblCsvImport.setRecordCount(readList.size() - 1);
                tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
                tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
                tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
                tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
                tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
                tblCsvImport.setLogFileUuid(logFileUuid);

                String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "tbl_production_plan_list");
                tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

                tblCsvImportService.createCsvImpor(tblCsvImport);

                return importResultResponse;
            } catch (Exception e) {
                importResultResponse.setError(true);
                importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid");
                importResultResponse.setErrorMessage(msg);
                return importResultResponse;
            }
        }
    }

    /**
     * 生産計画テーブルから条件にあてはまるデータを取得し、工程納期の降順、部品コード、工番の昇順で表示する。
     *
     * @param componentCode
     * @param procedureDueDateFrom
     * @param procedureDueDateTo
     * @param directionNumber
     * @param directionId
     * @param finalProcedureOnly
     * @param completeFlg
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionPlanList getTblProductionPlansByPage(@QueryParam("componentCode") String componentCode,
            @QueryParam("procedureDueDateFrom") String procedureDueDateFrom,
            @QueryParam("procedureDueDateTo") String procedureDueDateTo,
            @QueryParam("finalProcedureOnly") int finalProcedureOnly,
            @QueryParam("directionNumber") String directionNumber, @QueryParam("directionId") String directionId,
            @QueryParam("completeFlg") String completeFlg, @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblProductionPlanList tblProductionPlanList = tblProductionPlanService.getTblProductionPlansByPage(
                componentCode, procedureDueDateFrom, procedureDueDateTo, finalProcedureOnly, directionNumber, directionId, completeFlg,
                loginUser, sidx, sord, pageNumber, pageSize, true);
        return tblProductionPlanList;
    }
}
