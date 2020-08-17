/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.direction;

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
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;

/**
 * 手配テーブルリソース
 *
 * @author t.ariki
 */
@RequestScoped
@Path("direction")
public class TblDirectionResource {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblDirectionService tblDirectionService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    public TblDirectionResource() {
    }

    /**
     * 手配テーブル取得
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TblDirectionList getDirection() {
        return tblDirectionService.getDirections();
    }

    /**
     * 手配テーブルの入力候補表示(部分一致)
     *
     * @param directionCode
     * @return
     */
    @GET
    @Path("like")
    @Produces(MediaType.APPLICATION_JSON)
    public TblDirectionList getDirectionLikeCode(@QueryParam("directionCode") String directionCode) {
        return tblDirectionService.getDirectionLikeCode(directionCode);
    }

    /**
     * 手配テーブルの入力候補表示(完全一致)
     *
     * @param directionCode
     * @return
     */
    @GET
    @Path("equal")
    @Produces(MediaType.APPLICATION_JSON)
    public TblDirectionList getDirectionByCode(@QueryParam("directionCode") String directionCode) {
        return tblDirectionService.getDirectionByCode(directionCode);
    }

    /**
     * T0012 手配詳細_手配テーブル更新 画面の各項目の値で手配テーブルへ追加・更新する
     *
     * @param tblDirection
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putTblDirection(TblDirectionVo tblDirection) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        BasicResponse response = new BasicResponse();
        if (!tblDirectionService.getTblDirectionExistCheck(tblDirection.getId())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        // input check
        response = tblDirectionService.checkTblDirection(tblDirection, loginUser);
        if (response.isError()) {
            return response;
        }
        response = tblDirectionService.updateTblDirection(tblDirection, loginUser);

        return response;
    }

    /**
     * 手配テーブル一件取得(getTblDirection) 選択されている手配・工事情報の詳細画面に編集モードで遷移する。
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public TblDirectionVo getTblDirection(@PathParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblDirectionService.getTblDirectionByDirectionId(id, loginUser);
    }

    /**
     * T0012 手配詳細_手配テーブル追加 画面の各項目の値で手配テーブルへ追加・更新する
     *
     * @param tblDirection
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblDirectionVo postTblDirection(TblDirectionVo tblDirection) {
        TblDirectionVo response = new TblDirectionVo();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATE_FORMAT);
        if (tblDirectionService.checkDirectionCodeExistCheck(tblDirection.getDirectionCode(), tblDirection.getComponentId())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }
//          // input check
//        response =  tblDirectionService.checkTblDirection(tblDirection,loginUser);
//        if (response.isError()) {
//            return response;
//        }
        TblDirection newTblDirection = new TblDirection();
        newTblDirection.setDirectionCode(tblDirection.getDirectionCode());
        if (tblDirection.getDirectionCategory() != null && !"".equals(tblDirection.getDirectionCategory())) {
            newTblDirection.setDirectionCategory(Integer.parseInt(tblDirection.getDirectionCategory()));
        }
        if (tblDirection.getComponentId() != null && !"".equals(tblDirection.getComponentId())) {
            newTblDirection.setComponentId(tblDirection.getComponentId());
        }

        if (tblDirection.getQuantity() != null && !"".equals(tblDirection.getQuantity())) {
            newTblDirection.setQuantity(new BigDecimal(tblDirection.getQuantity()));
        } else {
            newTblDirection.setQuantity(new BigDecimal("0.0000"));
        }

        if (tblDirection.getDueDate() != null && !"".equals(tblDirection.getDueDate())) {
            try {
                newTblDirection.setDueDate(sdf.parse(tblDirection.getDueDate()));
            } catch (ParseException ex) {
                Logger.getLogger(TblDirectionResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        newTblDirection.setPoNumber(tblDirection.getPoNumber());
        if (tblDirection.getDepartment() != null && !"".equals(tblDirection.getDepartment())) {
            newTblDirection.setDepartment(Integer.parseInt(tblDirection.getDepartment()));
        }
        if (tblDirection.getMoldUuid() != null && !"".equals(tblDirection.getMoldUuid())) {
            newTblDirection.setMoldUuid(tblDirection.getMoldUuid());
        }

        // 追加
        tblDirectionService.createTblDirection(newTblDirection, loginUser);
        response.setId(newTblDirection.getId());
        return response;
    }

    /**
     * 手配テーブル取得 T0011 手配一覧_検索 件数取得 システム設定の一覧表示最大件数を超える場合は警告。
     *
     * @param directionCode
     * @param dueDateStart
     * @param dueDateEnd
     * @param department
     * @param poNumber
     * @return an instance of TblDirectionList
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("directionCode") String directionCode,
            @QueryParam("dueDateStart") String dueDateStart, @QueryParam("dueDateEnd") String dueDateEnd,
            @QueryParam("department") String department, @QueryParam("poNumber") String poNumber) {
        CountResponse count = tblDirectionService.getDirectionCount(directionCode, dueDateStart, dueDateEnd, department, poNumber);

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
     * 手配テーブル取得 T0011 手配一覧_検索 手配テーブルから条件にあてはまるデータを取得し、手配・工事番号の昇順で表示する。
     *
     * @param directionCode
     * @param poNumber
     * @param dueDateStart
     * @param department
     * @param dueDateEnd
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblDirectionList getTblDirections(@QueryParam("directionCode") String directionCode,
            @QueryParam("dueDateStart") String dueDateStart, @QueryParam("dueDateEnd") String dueDateEnd,
            @QueryParam("department") String department,
            @QueryParam("poNumber") String poNumber) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblDirectionService.getTblDirections(directionCode, dueDateStart, dueDateEnd, department, poNumber, loginUser);

    }

    /**
     * 手配テーブル取得 T0011 手配一覧_削除
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse delDirection(@PathParam("id") String id) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        if (!tblDirectionService.getTblDirectionExistCheck(id)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }

        tblDirectionService.deleteTblDirection(id);

        return response;
    }

    /**
     * 手配テーブルから条件にあてはまるデータを取得し、手配・工事番号の昇順でCSVファイルに出力する。
     *
     * @param directionCode
     * @param dueDateStart
     * @param dueDateEnd
     * @param department
     * @param poNumber
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblDirectionCsv(@QueryParam("directionCode") String directionCode,
            @QueryParam("dueDateStart") String dueDateStart,
            @QueryParam("dueDateEnd") String dueDateEnd,
            @QueryParam("department") String department,
            @QueryParam("poNumber") String poNumber) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblDirectionService.getTblDirectionOutputCsv(directionCode, dueDateStart, dueDateEnd, department, poNumber, loginUser);
    }

    /**
     * CSVファイルから手配テーブルへデータの追加・更新・削除を行う。
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postTblDirectionCsv(@PathParam("fileUuid") String fileUuid) {
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

                String directionCode = mstDictionaryService.getDictionaryValue(userLangId, "direction_code");
                String directionValue = mstDictionaryService.getDictionaryValue(userLangId, "direction_value");
                String componentCode = mstDictionaryService.getDictionaryValue(userLangId, "component_code");
                String componentName = mstDictionaryService.getDictionaryValue(userLangId, "component_name");
                String quantity = mstDictionaryService.getDictionaryValue(userLangId, "quantity");
                String dueDate = mstDictionaryService.getDictionaryValue(userLangId, "due_date");
                String moldId = mstDictionaryService.getDictionaryValue(userLangId, "mold_id");
                String moldName = mstDictionaryService.getDictionaryValue(userLangId, "mold_name");
                String department = mstDictionaryService.getDictionaryValue(userLangId, "work_user_department");
                String orderedNumber = mstDictionaryService.getDictionaryValue(userLangId, "po_number");

                String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
                String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");
                String isExist = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_record_exists");
                String lineNo = mstDictionaryService.getDictionaryValue(userLangId, "row_number");
                String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
                String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
                String result = mstDictionaryService.getDictionaryValue(userLangId, "db_process");
                String addedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_added");
                String notFound = mstDictionaryService.getDictionaryValue(userLangId, "mst_error_record_not_found");
                String isNumber = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_isnumber");
                String isDate = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_date_format_invalid");
                String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");
                Map<String, String> logMap = new HashMap<>();
                logMap.put("lineNo", lineNo);
                logMap.put("directionCode", directionCode);
                logMap.put("directionValue", directionValue);
                logMap.put("componentCode", componentCode);
                logMap.put("componentName", componentName);
                logMap.put("quantity", quantity);
                logMap.put("dueDate", dueDate);
                logMap.put("moldId", moldId);
                logMap.put("moldName", moldName);
                logMap.put("department", department);
                logMap.put("orderedNumber", orderedNumber);
                logMap.put("error", error);
                logMap.put("errorContents", errorContents);
                logMap.put("maxLangth", maxLangth);
                logMap.put("nullMsg", nullMsg);
                logMap.put("layout", layout);
                logMap.put("isNumber", isNumber);
                logMap.put("isDate", isDate);

                TblDirection readCsvInfo;
                FileUtil fu = new FileUtil();
                Map<String, String> departmentMap = new HashMap<>();
                MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
                for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                    departmentMap.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
                }
                Map<String, String> directionCategoryMap = new HashMap<>();
                MstChoiceList mstDirectionChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_direction.direction_caterogy");
                for (MstChoice mstChoice : mstDirectionChoiceList.getMstChoice()) {
                    directionCategoryMap.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
                }

                for (int i = 1; i < readList.size(); i++) {
                    ArrayList List = (ArrayList) readList.get(i);

                    if (List.size() != 10) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, directionCode, "", error, 1, errorContents, layout));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    String[] csvArray = new String[List.size()];
                    String strDirectionCode = String.valueOf(List.get(0));
                    csvArray[0] = strDirectionCode;
                    String strDirectionValue = String.valueOf(List.get(1));
                    String strDirectionNum = "";
                    if (strDirectionValue != null && !"".equals(strDirectionValue)) {
                        if (directionCategoryMap != null && directionCategoryMap.size() > 0) {
                            strDirectionNum = directionCategoryMap.get(strDirectionValue);
                        } else {
                            strDirectionNum = "0";
                        }
                    } else {
                        strDirectionNum = "0";
                    }
                    csvArray[1] = strDirectionValue;
                    String strComponentCode = String.valueOf(List.get(2));
                    csvArray[2] = strComponentCode;
                    String strComponentName = String.valueOf(List.get(3));
                    csvArray[3] = strComponentName;
                    String strQuantity = String.valueOf(List.get(4));
                    csvArray[4] = strQuantity;
                    String strDueDate = String.valueOf(List.get(5));
                    if (null != strDueDate && !strDueDate.isEmpty()) {
                        strDueDate = DateFormat.formatDateYear(strDueDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[5] = strDueDate.equals("-1") ? String.valueOf(List.get(5)) : strDueDate;
                    String strMoldId = String.valueOf(List.get(6));
                    csvArray[6] = strMoldId;
                    String strMoldName = String.valueOf(List.get(7));
                    csvArray[7] = strMoldName;
                    String strDepartment = String.valueOf(List.get(8));
                    String strDepartmentNum = "";
                    if (strDepartment != null && !"".equals(strDepartment)) {
                        if (departmentMap != null && departmentMap.size() > 0) {
                            strDepartmentNum = departmentMap.get(strDepartment);
                        } else {
                            strDepartment = "0";
                        }
                    } else {
                        strDepartment = "0";
                    }
                    csvArray[8] = strDepartment;
                    String strOrderedNumber = String.valueOf(List.get(9));
                    csvArray[9] = strOrderedNumber;

                    if (tblDirectionService.checkCsvFileData(logMap, csvArray, userLangId, logFile, i)) {
                        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
                        readCsvInfo = new TblDirection();
                        readCsvInfo.setDirectionCode(strDirectionCode.trim());
                        if (strDirectionNum != null && !"".equals(strDirectionNum)) {
                            readCsvInfo.setDirectionCategory(Integer.parseInt(strDirectionNum.trim()));
                        } else {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, directionValue, strDirectionValue, error, 1, errorContents, notFound));
                            continue;
                        }

                        if (strComponentCode != null && !"".equals(strComponentCode)) {
                            MstComponent mstComponent = entityManager.find(MstComponent.class, strComponentCode);
                            if (mstComponent != null) {
                                readCsvInfo.setComponentId(mstComponent.getId());
                            } else {
                                //エラー情報をログファイルに記入
                                failedCount = failedCount + 1;
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, strComponentCode, error, 1, errorContents, notFound));
                                continue;
                            }
                        } else {
                            readCsvInfo.setComponentId(null);
                        }

                        if (strQuantity != null && !"".equals(strQuantity)) {
                            readCsvInfo.setQuantity(new BigDecimal(strQuantity.trim()));
                        } else {
                            readCsvInfo.setQuantity(new BigDecimal("0.0000"));
                        }

                        if (strDueDate != null && !"".equals(strDueDate)) {
                            try {
                                readCsvInfo.setDueDate(sdf.parse(strDueDate.trim()));
                            } catch (ParseException ex) {
                                Logger.getLogger(TblDirectionResource.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            readCsvInfo.setDueDate(null);
                        }

                        //strMoldId
                        if (strMoldId != null && !"".equals(strMoldId)) {
                            MstMold mstMold = entityManager.find(MstMold.class, strMoldId.trim());
                            if (mstMold != null) {
                                readCsvInfo.setMoldUuid(mstMold.getUuid());
                            } else {
                                //エラー情報をログファイルに記入
                                failedCount = failedCount + 1;
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, moldId, strMoldId, error, 1, errorContents, notFound));
                                continue;
                            }
                        } else {
                            readCsvInfo.setMoldUuid(null);
                        }

                        if (strDepartmentNum != null && !"".equals(strDepartmentNum)) {
                            readCsvInfo.setDepartment(Integer.parseInt(strDepartmentNum.trim()));
                        } else {
//                        //エラー情報をログファイルに記入
//                        failedCount = failedCount + 1;
//                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, department, strDepartment, error, 1, errorContents, notFound));
//                        continue;
                            readCsvInfo.setDepartment(0);
                        }
                        if (strOrderedNumber != null && !"".equals(strOrderedNumber)) {
                            readCsvInfo.setPoNumber(strOrderedNumber.trim());
                        } else {
                            readCsvInfo.setPoNumber("");
                        }

                        if (tblDirectionService.checkDirectionCodeExistCheck(strDirectionCode, readCsvInfo.getComponentId())) {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, directionCode, strDirectionCode, error, 1, errorContents, isExist));
                            continue;
                        }
                        //追加
                        tblDirectionService.createTblDirection(readCsvInfo, loginUser);
                        addedCount = addedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, directionCode, strDirectionCode, error, 0, result, addedMsg));
                    } else {
                        failedCount = failedCount + 1;
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

        //アップロードログをテーブルに書き出し TODO
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.TBL_DIRECTION);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_MST_DIRECTION_MAINTENANCE);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_DIRECTION);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;

    }

    /**
     * T0015 手配別生産実績照会 手配・工事テーブル、生産実績テーブル、生産実績明細テーブルより条件にあてはまるレコードを検索し、
     * 手配番号の降順で表示する。 システム設定の一覧表示最大件数を超えるときは警告。
     *
     * @param directionCode
     * @param department
     * @param startDueDate
     * @param endDueDate
     * @param poNumber
     * @return
     */
    @GET
    @Path("refdirectioncount")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getProductionRefCompareWithDirectionCount(@QueryParam("directionCode") String directionCode,
            @QueryParam("department") String department,
            @QueryParam("startDueDate") String startDueDate,
            @QueryParam("endDueDate") String endDueDate,
            @QueryParam("poNumber") String poNumber) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        CountResponse count = tblDirectionService.getProductionRefCompareWithDirectionCount(directionCode, department, startDueDate, endDueDate, poNumber, loginUser);
        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());
        if (count.getCount() > sysCount) {
            count.setError(true);
            count.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            count.setErrorMessage(msg);
        }

        return count;
    }

    /**
     * T0015 手配別生産実績照会 手配・工事テーブル、生産実績テーブル、生産実績明細テーブルより条件にあてはまるレコードを検索し、
     * 手配番号の降順で表示する。
     *
     * @param directionCode
     * @param department
     * @param startDueDate
     * @param endDueDate
     * @param poNumber
     * @return
     */
    @GET
    @Path("refdirection")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblDirectionList getProductionRefCompareWithDirection(@QueryParam("directionCode") String directionCode,
            @QueryParam("department") String department,
            @QueryParam("startDueDate") String startDueDate,
            @QueryParam("endDueDate") String endDueDate,
            @QueryParam("poNumber") String poNumber) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblDirectionService.getProductionRefCompareWithDirection(directionCode, department, startDueDate, endDueDate, poNumber, loginUser);
    }

    /**
     * T0015 手配別生産実績照会 詳細-生産計画対比照会または生産実績照会に遷移する。
     *
     * @param id
     * @return
     */
    @GET
    @Path("directionflag")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblDirectionVo getProductionRefCompareWithDirectionFlag(@QueryParam("id") String id) {
        TblDirectionVo response = new TblDirectionVo();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (id != null && !"".equals(id)) {
            response = tblDirectionService.getProductionRefCompareWithDirectionFlag(id, loginUser);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_record_selected"));
        }
        return response;
    }

    /**
     * T0015 手配別生産実績照会 CSV出力
     *
     * @param directionCode
     * @param department
     * @param startDueDate
     * @param endDueDate
     * @param poNumber
     * @return
     */
    @GET
    @Path("refdirectionexportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getProductionRefCompareWithDirectionCSV(@QueryParam("directionCode") String directionCode,
            @QueryParam("department") String department,
            @QueryParam("startDueDate") String startDueDate,
            @QueryParam("endDueDate") String endDueDate,
            @QueryParam("poNumber") String poNumber) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblDirectionService.getProductionRefCompareWithDirectionCSV(directionCode, department, startDueDate, endDueDate, poNumber, loginUser);
    }
    
    /**
     * 手配テーブル取得 T0011 手配一覧_検索 手配テーブルから条件にあてはまるデータを取得し、手配・工事番号の昇順で表示する。
     *
     * @param directionCode
     * @param poNumber
     * @param dueDateStart
     * @param department
     * @param dueDateEnd
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Path("searchbypage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblDirectionList getTblDirectionsByPage(@QueryParam("directionCode") String directionCode,
            @QueryParam("dueDateStart") String dueDateStart, @QueryParam("dueDateEnd") String dueDateEnd,
            @QueryParam("department") String department, @QueryParam("poNumber") String poNumber,
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblDirectionService.getTblDirectionsByPage(directionCode, dueDateStart, dueDateEnd, department, poNumber,
                loginUser, sidx, sord, pageNumber, pageSize, true);

    }
}
