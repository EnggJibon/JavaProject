/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.UrlDecodeInterceptor;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.attribute.MstComponentAttribute;
import com.kmcj.karte.resources.component.attribute.MstComponentAttributeList;
import com.kmcj.karte.resources.component.spec.MstComponentSpec;
import com.kmcj.karte.resources.component.spec.MstComponentSpecPK;
import com.kmcj.karte.resources.component.spec.MstComponentSpecService;
import com.kmcj.karte.resources.currency.MstCurrencyService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.mold.MstMoldAutoComplete;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
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
import org.apache.commons.lang.StringUtils;

/**
 * 部品マスタの処理
 *
 * @author admin
 */
@RequestScoped
@Path("component")
public class MstComponentResource {

    /**
     * Creates a new instance of MstComponentResource
     */
    public MstComponentResource() {
    }

    @Inject
    private MstComponentService mstComponentService;

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
    private MstComponentSpecService mstComponentSpecService;

    @Inject
    private MstCurrencyService mstCurrencyService;

    /**
     * 部品マスターから件数
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentType") Integer componentType) {
        CountResponse count = mstComponentService.getMstComponentCount(componentCode, componentName, componentType);

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
     * すべての部品マスターを取得する
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @return an instance of MstComponentList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getComponents(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentType") Integer componentType) 
    {
        return mstComponentService.getMstComponents(componentCode, componentName, componentType);
    }

    @GET
    @Path("purchasedAndcircuitboard")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getComponentsPurchasedAndCircuitboard(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentType") Integer componentType,
            @QueryParam("isPurchasedPart") Integer isPurchasedPart,
            @QueryParam("isCircuitboard") Integer isCircuitboard
            ) 
    {        
        return mstComponentService.getMstComponentsPurchasedAndCircuitboard(componentCode, componentName, componentType, isPurchasedPart, isCircuitboard);
    }
    
    /**
     * 基板の部品を取得する
     *
     * @return an instance of MstComponentList
     */
    @GET
    @Path("circuit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getCircuitComponents() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentService.getMstComponentsOfCircuit(loginUser);
    }

    /**
     * 基板の部品を取得する（オートコンプリート）
     *
     * @param componentCode
    * @return an instance of MstComponentList
     */
    @GET
    @Path("circuitauto")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getCircuitComponentsAutoComplete(@QueryParam("componentCode") String componentCode) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentService.getMstComponentsOfCircuitAutoComplete(componentCode, loginUser);
    }

    /**
     * シリアルナンバーから基板の部品を取得する
     *
     * @param serialNumber
     * @return an instance of MstComponentList
     */
    @GET
    @Path("circuitSerialNumber")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getCircuitComponentBySerialNumber(@QueryParam("serialNumber") String serialNumber) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentService.getMstComponentsOfCircuitSerialNumber(serialNumber, loginUser);
    }

    @GET
    @Path("circuitCode")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getCircuitComponentByCircuitCode(@QueryParam("circuitCode") String circuitCode) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentService.getMstComponentsOfCircuitCode(circuitCode, loginUser);
    }
    
    /**
     * 部品一覧から削除ボタンを押下時
     *
     * @param componentCode
     * @return
     */
    @DELETE
    @Path("{componentCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public BasicResponse deleteMstComponent(@PathParam("componentCode") String componentCode) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        if (!mstComponentService.getMstComponentExistCheck(componentCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }

        // FK依存関係チェック　True依存関係なし削除できる　False　削除できない
        if (!mstComponentService.getMstComponentFKCheck(componentCode)) {
            mstComponentService.deleteMstComponent(componentCode);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
        }

        return response;

    }

    /**
     * 部品一覧から編集ボタンを押下時
     *
     * @param componentCode
     * @return
     */
    @GET
    @Path("/detail/{componentCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public MstComponentDetail getComponentDetail(@PathParam("componentCode") String componentCode) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentService.getMstComponentDetails(null, componentCode, loginUser);
    }

    /**
     * 部品一覧からCSVを出力
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getComponentsCsv(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentType") Integer componentType) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstComponentService.outComponentTypeOfChoice(loginUser.getLangId());
        return mstComponentService.getMstComponentsOutputCsv(componentCode, componentName, componentType, loginUser);
    }

    /**
     * 部品一覧からCSVを取込
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postComponentsCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long deletedCount = 0;
        long failedCount = 0;
        String logFileUuid = IDGenerator.generate();
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);

        // csv取込する際に必要な文言を取る
        List<String> dictKeyList = Arrays.asList("msg_error_value_invalid", "is_purchased_part", "msg_error_wrong_csv_layout", "row_number", "component_code", "component_type", "circuit_board", "sn_length", "sn_fixed_value", "sn_fixed_length", "component_spec", "error",
                "error_detail", "db_process", "msg_record_added", "msg_data_modified", "msg_cannot_delete_used_record", "mst_error_record_not_found",
                "msg_error_over_length", "msg_error_data_deleted", "component_name", "msg_error_not_null", "mst_component",
                "msg_error_date_format_invalid", "msg_error_not_isnumber", "msg_record_deleted", "unit_price", "currency_code", "stock_unit");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, loginUser.getLangId(), dictKeyList);

        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = headerMap.get("msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        // CSV中身を読み取り
        ArrayList<List<String>> readList = CSVFileUtil.readCsv(csvFile);

        try {
            if (readList.size() <= 1) {
                return importResultResponse;
            } else {
                //部品種類のkeyを取得する
                Map inComponentType = mstComponentService.inComponentTypeOfChoice(loginUser.getLangId());

                String userLangId = loginUser.getLangId();
                String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
                String lineNo = headerMap.get("row_number");
                String componentCode = headerMap.get("component_code");
                String componentType = headerMap.get("component_type");
                String isPurchasedPart = headerMap.get("is_purchased_part");
                String isCircuitBoard = headerMap.get("circuit_board");
                String snLength = headerMap.get("sn_length");
                String snFixedValue = headerMap.get("sn_fixed_value");
                String snFixedLength = headerMap.get("sn_fixed_length");
                String componentSpec = headerMap.get("component_spec");
                String error = headerMap.get("error");
                String errorContents = headerMap.get("error_detail");
                String result = headerMap.get("db_process");
                String addedMsg = headerMap.get("msg_record_added");
                String updatedMsg = headerMap.get("msg_data_modified");
                String canNotdeleteMsg = headerMap.get("msg_cannot_delete_used_record");
                String notFound = headerMap.get("mst_error_record_not_found");
                String maxLangth = headerMap.get("msg_error_over_length");
                String layout = headerMap.get("msg_error_wrong_csv_layout");
                String deletedMsg = headerMap.get("msg_record_deleted");
                String msgErrorDateDormatInvalid = headerMap.get("msg_error_date_format_invalid");
                String msgErrorNotIsnumber = headerMap.get("msg_error_not_isnumber");
                String msgErrorValueInvalid = headerMap.get("msg_error_value_invalid");
                String unitPrice = headerMap.get("unit_price");
                String currencyCode = headerMap.get("currency_code");
                String stockUnit = headerMap.get("stock_unit");
                String componentName = headerMap.get("component_name");
                String nullMsg = headerMap.get("msg_error_not_null");

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("componentCode", componentCode);
                logParm.put("componentName", componentName);
                logParm.put("isCircuitBoard", isCircuitBoard);
                logParm.put("snLength", snLength);
                logParm.put("snFixedValue", snFixedValue);
                logParm.put("snFixedLength", snFixedLength);
                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("maxLangth", maxLangth);
                logParm.put("layout", layout);

                MstComponent readCsvInfo;
                MstComponentSpec readCsvInfoSpec;
                FileUtil fu = new FileUtil();

                Map<String, Integer> colIndexMap = new HashMap<>();
                /* 仕様のcount Start*/
                int iCompSpec = 0;
                int compoSpecStartIndex = 0;
                int delFlagPostion = readList.get(0).size() - 1;
                for (int j = 0; j < delFlagPostion; j++) {
                    if (readList.get(0).get(j).trim().contains(componentSpec)) {
                        iCompSpec = iCompSpec + 1;
                        if (compoSpecStartIndex == 0) {
                            compoSpecStartIndex = j;
                        }
                    } else {
                        if (readList.get(0).get(j).trim().equals(componentCode)) {
                            colIndexMap.put("componentCode", j);
                        } else if (readList.get(0).get(j).trim().equals(componentName)) {
                            colIndexMap.put("componentName", j);
                        } else if (readList.get(0).get(j).trim().equals(componentType)) {
                            colIndexMap.put("componentType", j);
                        } else if (readList.get(0).get(j).trim().equals(isPurchasedPart)) {
                            colIndexMap.put("isPurchasedPart", j);
                        } else if (readList.get(0).get(j).trim().equals(isCircuitBoard)) {
                            colIndexMap.put("isCircuitBoard", j);
                        } else if (readList.get(0).get(j).trim().equals(snLength)) {
                            colIndexMap.put("snLength", j);
                        } else if (readList.get(0).get(j).trim().equals(snFixedValue)) {
                            colIndexMap.put("snFixedValue", j);
                        } else if (readList.get(0).get(j).trim().equals(snFixedLength)) {
                            colIndexMap.put("snFixedLength", j);
                        } else if (readList.get(0).get(j).trim().equals(unitPrice)) {
                            colIndexMap.put("unitPrice", j);
                        } else if (readList.get(0).get(j).trim().equals(currencyCode)) {
                            colIndexMap.put("currencyCode", j);
                        } else if (readList.get(0).get(j).trim().equals(stockUnit)) {
                            colIndexMap.put("stockUnit", j);
                        }
                    }
                }
                /* 仕様のcount End*/
                Collection<MstComponentSpec> mstComponentSpecCollections;

                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);
                    //ヘーダー列数と値の列数異なる場合、エラーとなる
                    if (((ArrayList) readList.get(0)).size() != comList.size()) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, "", error, 1, errorContents, layout));
                        failedCount += 1;
                        continue;
                    }
                    
                    readCsvInfo = new MstComponent();
                    // 部品コード
                    if (colIndexMap.get("componentCode") != null) {
                        readCsvInfo.setComponentCode(String.valueOf(comList.get(colIndexMap.get("componentCode"))));
                    }

                    String strDelFlag = String.valueOf(comList.get(comList.size() - 1));

                    //delFlag 指定されている場合
                    if (strDelFlag != null && strDelFlag.trim().equals("1")) {
                        //存在チェック
                        if (mstComponentService.getMstComponentExistCheck(readCsvInfo.getComponentCode())) {
                            // FK依存関係チェック　True依存関係なし削除できる　False　削除できない
                            if (!mstComponentService.getMstComponentFKCheck(readCsvInfo.getComponentCode())) {
                                mstComponentService.deleteMstComponent(readCsvInfo.getComponentCode());
                                deletedCount = deletedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, readCsvInfo.getComponentCode(), error, 0, result, deletedMsg));
                            } else {
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, readCsvInfo.getComponentCode(), error, 1, errorContents, canNotdeleteMsg));
                                failedCount = failedCount + 1;
                            }
                        } else {
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, readCsvInfo.getComponentCode(), error, 1, errorContents, notFound));
                        }

                    } else {
                        boolean errorFlag = false;
                        // 部品名
                        if (colIndexMap.get("componentName") != null) {
                            readCsvInfo.setComponentName(String.valueOf(comList.get(colIndexMap.get("componentName"))));
                        }
                        // 部品種類
                        if (colIndexMap.get("componentType") == null) {
                            readCsvInfo.setComponentType(0);
                        } else {
                            String strComponentTypeValue = String.valueOf(comList.get(colIndexMap.get("componentType")));
                            if (StringUtils.isEmpty(strComponentTypeValue)) {
                                readCsvInfo.setComponentType(0);
                            } else {
                                String strComponentTypeKey = (String) inComponentType.get(strComponentTypeValue.trim());
                                if (StringUtils.isEmpty(strComponentTypeKey)) {
                                    //エラー情報をログファイルに記入
                                    failedCount = failedCount + 1;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentType, strComponentTypeValue, error, 1, errorContents, notFound));
                                    continue;
                                } else {
                                    readCsvInfo.setComponentType(Integer.parseInt(strComponentTypeKey));
                                }
                            }
                        }

                        // 購入部品フラグ
                        if (colIndexMap.get("isPurchasedPart") == null) {
                            readCsvInfo.setIsPurchasedPart(0);
                        } else {
                            String strIsPurchasedPart = "";
                            try {
                                strIsPurchasedPart = String.valueOf(comList.get(colIndexMap.get("isPurchasedPart")));
                                int isPurchasedPartItem = Integer.parseInt(strIsPurchasedPart);
                                if (0 != isPurchasedPartItem && 1 != isPurchasedPartItem) {
                                    //エラー情報をログファイルに記入
                                    failedCount = failedCount + 1;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, isPurchasedPart, strIsPurchasedPart, error, 1, errorContents, msgErrorValueInvalid));
                                    continue;
                                }
                                readCsvInfo.setIsPurchasedPart(isPurchasedPartItem);
                            } catch (NumberFormatException e) {
                                //エラー情報をログファイルに記入
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, isPurchasedPart, strIsPurchasedPart, error, 1, errorContents, msgErrorNotIsnumber));
                                continue;
                            }
                        }

                        //通貨コード
                        if (colIndexMap.get("currencyCode") != null) {
                            String inCurrencyCode = String.valueOf(comList.get(colIndexMap.get("currencyCode")));
                            if (StringUtils.isNotEmpty(inCurrencyCode)) {
                                if (mstCurrencyService.getSingleMstCurrency(inCurrencyCode)) {
                                    readCsvInfo.setCurrencyCode(inCurrencyCode);
                                } else {
                                    failedCount = failedCount + 1;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, currencyCode, inCurrencyCode, error, 1, errorContents, notFound));
                                    errorFlag = true;
                                }
                            } else {
                                readCsvInfo.setCurrencyCode("");
                            }
                        }
                        //単価
                        if (colIndexMap.get("unitPrice") != null) {
                            try {
                                readCsvInfo.setUnitPrice(new BigDecimal(String.valueOf(comList.get(colIndexMap.get("unitPrice")))));
                            } catch (Exception e) {
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, unitPrice, String.valueOf(comList.get(colIndexMap.get("unitPrice"))).trim(), error, 1, errorContents, msgErrorNotIsnumber));
                                errorFlag = true;
                            }
                        }
                        //在庫単位数
                        if (colIndexMap.get("stockUnit") != null) {
                            try {
                                readCsvInfo.setStockUnit(Integer.parseInt(String.valueOf(comList.get(colIndexMap.get("stockUnit")))));
                            } catch (Exception e) {
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, stockUnit, String.valueOf(comList.get(colIndexMap.get("stockUnit"))).trim(), error, 1, errorContents, msgErrorNotIsnumber));
                                errorFlag = true;
                            }
                        }

                        // 基板区分
                        if (colIndexMap.get("isCircuitBoard") != null) {
                            try {
                                readCsvInfo.setIsCircuitBoard(Integer.parseInt(String.valueOf(comList.get(colIndexMap.get("isCircuitBoard")))));
                            } catch (Exception e) {
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, isCircuitBoard, String.valueOf(comList.get(colIndexMap.get("isCircuitBoard"))).trim(), error, 1, errorContents, msgErrorNotIsnumber));
                                errorFlag = true;
                            }
                        }
                        // SN桁数
                        if (colIndexMap.get("snLength") != null) {
                            try {
                                readCsvInfo.setSnLength(Integer.parseInt(String.valueOf(comList.get(colIndexMap.get("snLength")))));
                            } catch (Exception e) {
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, snLength, String.valueOf(comList.get(colIndexMap.get("snLength"))).trim(), error, 1, errorContents, msgErrorNotIsnumber));
                                errorFlag = true;
                            }
                        }
                        // SN固定ヘッダー
                        if (colIndexMap.get("snFixedValue") != null) {
                            readCsvInfo.setSnFixedValue(String.valueOf(comList.get(colIndexMap.get("snFixedValue"))));
                        }
                        // SN固定ヘッダー桁数
                        if (colIndexMap.get("snFixedLength") != null) {
                            try {
                                readCsvInfo.setSnFixedLength(Integer.parseInt(String.valueOf(comList.get(colIndexMap.get("snFixedLength")))));
                            } catch (Exception e) {
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, snFixedLength, String.valueOf(comList.get(colIndexMap.get("snFixedLength"))).trim(), error, 1, errorContents, msgErrorNotIsnumber));
                                errorFlag = true;
                            }
                        }
                        //チェック
                        if (mstComponentService.checkCsvFileData(logParm, readCsvInfo, userLangId, logFile, i)) {
                            //存在チェックの場合　
                            if (mstComponentService.getMstComponentExistCheck(readCsvInfo.getComponentCode())) {
                                // データを更新
                                Date sysDate = new Date();
                                readCsvInfo.setUpdateDate(sysDate);
                                readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                if (iCompSpec > 0) {
                                    MstComponentList mstComponent = mstComponentService.getMstComponentDetail(readCsvInfo.getComponentCode());
                                    String strComponentId = mstComponent.getMstComponents().get(0).getId();
                                    MstComponentAttributeList mstComponentAttribute = mstComponentService.getMstComponentAttributeDetail(readCsvInfo.getComponentType());
                                    int attrCode = mstComponentAttribute.getMstComponentAttributes().size();
                                    int iFor;
                                    if (iCompSpec > attrCode) {
                                        //attrCode data for list
                                        iFor = attrCode;
                                    } else {
                                        // arrayLength  data for list
                                        iFor = iCompSpec;
                                    }

                                    for (int j = 0; j < iFor; j++) {
                                        String strAttrValue = String.valueOf(comList.get(j + compoSpecStartIndex)).trim();
                                        if (StringUtils.isEmpty(strAttrValue)) {
                                            continue;
                                        }
                                        MstComponentAttribute attributeTypeCheck = mstComponentAttribute.getMstComponentAttributes().get(j);
                                        String strId = mstComponentAttribute.getMstComponentAttributes().get(j).getId();
                                        int attrType = FileUtil.getIntegerValue(attributeTypeCheck.getAttrType());
                                        
                                        // KM-427
                                        boolean numCheck = false;
                                        try {
                                            strAttrValue = String.valueOf(Float.parseFloat(strAttrValue));
                                        } catch (NumberFormatException e) {
                                            numCheck = true;
                                        }
                                        if (numCheck && CommonConstants.ATTRIBUTE_TYPE_NUMBER == attrType) {//km-427
                                            //if (!fu.isNumber(strAttrValue) && CommonConstants.ATTRIBUTE_TYPE_NUMBER == attrType) {
                                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentSpec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, msgErrorNotIsnumber));
                                            failedCount = failedCount + 1;
                                            errorFlag = true;
                                            break;
                                        }
                                        if (fu.checkSpecAttrType(attributeTypeCheck.getAttrType(), strAttrValue) == false) {
                                            //エラー情報をログファイルに記入
                                            failedCount = failedCount + 1;
                                            errorFlag = true;
                                            switch (attrType) {
                                                case CommonConstants.ATTRIBUTE_TYPE_DATE:
                                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentSpec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, msgErrorDateDormatInvalid));
                                                    break;
                                                case CommonConstants.ATTRIBUTE_TYPE_NUMBER:
                                                case CommonConstants.ATTRIBUTE_TYPE_TEXT:
                                                case CommonConstants.ATTRIBUTE_TYPE_STATICLINK:
                                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentSpec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, maxLangth));
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        } else {
                                            readCsvInfoSpec = new MstComponentSpec();
                                            MstComponentSpecPK newPk = new MstComponentSpecPK();
                                            newPk.setAttrId(strId);
                                            newPk.setComponentId(strComponentId);
                                            readCsvInfoSpec.setAttrValue(strAttrValue);
                                            readCsvInfoSpec.setMstComponentSpecPK(newPk);
                                            if (mstComponentSpecService.getMstComponentSpecExistCheck(strComponentId, strId)) {
                                                readCsvInfoSpec.setUpdateDate(sysDate);
                                                readCsvInfoSpec.setUpdateUserUuid(loginUser.getUserUuid());
                                                mstComponentSpecService.updateMstComponentSpec(readCsvInfoSpec);
                                            } else {
                                                readCsvInfoSpec.setId(IDGenerator.generate());
                                                readCsvInfoSpec.setCreateDate(sysDate);
                                                readCsvInfoSpec.setCreateUserUuid(loginUser.getUserUuid());
                                                mstComponentSpecService.createMstComponentSpec(readCsvInfoSpec);
                                            }
                                        }
                                    }
                                }

                                if (!errorFlag) {
                                    int count = mstComponentService.updateMstComponentByQuery(readCsvInfo, colIndexMap);
                                    updatedCount = updatedCount + count;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, readCsvInfo.getComponentCode(), error, 0, result, updatedMsg));
                                }

                            } else {
                                // データを追加
                                Date sysDate = new Date();
                                String strComponentId = IDGenerator.generate();
                                readCsvInfo.setId(strComponentId);
                                readCsvInfo.setCreateDate(sysDate);
                                readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                                readCsvInfo.setUpdateDate(sysDate);
                                readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                readCsvInfo.setUnitPrice(new BigDecimal(BigInteger.ZERO));
                                if (iCompSpec > 0) {
                                    MstComponentAttributeList mstComponentAttribute = mstComponentService.getMstComponentAttributeDetail(readCsvInfo.getComponentType());
                                    int attrCode = mstComponentAttribute.getMstComponentAttributes().size();
                                    int iFor;
                                    if (iCompSpec > attrCode) {
                                        //attrCode data for list
                                        iFor = attrCode;
                                    } else {
                                        // arrayLength  data for list
                                        iFor = iCompSpec;
                                    }

                                    mstComponentSpecCollections = new HashSet<>();

                                    for (int j = 0; j < iFor; j++) {
                                        String strAttrValue = String.valueOf(comList.get(j + compoSpecStartIndex)).trim();
                                        if (StringUtils.isEmpty(strAttrValue)) {
                                            continue;
                                        }
                                        MstComponentAttribute attributeTypeCheck = mstComponentAttribute.getMstComponentAttributes().get(j);
                                        String strId = attributeTypeCheck.getId();
                                        int attrType = FileUtil.getIntegerValue(attributeTypeCheck.getAttrType());
                                        // KM-427
                                        boolean numCheck = false;
                                        try {
                                            strAttrValue = String.valueOf(Float.parseFloat(strAttrValue));
                                        } catch (NumberFormatException e) {
                                            numCheck = true;
                                        }
                                        if (numCheck && CommonConstants.ATTRIBUTE_TYPE_NUMBER == attrType) { //KM-427
                                            //if (!fu.isNumber(strAttrValue) && CommonConstants.ATTRIBUTE_TYPE_NUMBER == attrType) {
                                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentSpec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, msgErrorNotIsnumber));
                                            failedCount = failedCount + 1;
                                            errorFlag = true;
                                            break;
                                        }
                                        if (fu.checkSpecAttrType(attrType, strAttrValue) == false) {
                                            failedCount = failedCount + 1;
                                            errorFlag = true;
                                            switch (FileUtil.getIntegerValue(attributeTypeCheck.getAttrType())) {
                                                case CommonConstants.ATTRIBUTE_TYPE_DATE:
                                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentSpec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, msgErrorDateDormatInvalid));
                                                    break;
                                                case CommonConstants.ATTRIBUTE_TYPE_NUMBER:
                                                case CommonConstants.ATTRIBUTE_TYPE_TEXT:
                                                case CommonConstants.ATTRIBUTE_TYPE_STATICLINK:
                                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentSpec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, maxLangth));
                                                    break;
                                                default:
                                                    break;
                                            }
                                            break;
                                        } else {
                                            readCsvInfoSpec = new MstComponentSpec();
                                            MstComponentSpecPK newPk = new MstComponentSpecPK();
                                            newPk.setAttrId(strId);
                                            newPk.setComponentId(strComponentId);
                                            readCsvInfoSpec.setId(IDGenerator.generate());
                                            readCsvInfoSpec.setMstComponentSpecPK(newPk);
                                            readCsvInfoSpec.setAttrValue(strAttrValue);
                                            readCsvInfoSpec.setCreateDate(sysDate);
                                            readCsvInfoSpec.setCreateUserUuid(loginUser.getUserUuid());
                                            readCsvInfoSpec.setUpdateDate(sysDate);
                                            readCsvInfoSpec.setUpdateUserUuid(loginUser.getUserUuid());
                                            mstComponentSpecCollections.add(readCsvInfoSpec);
                                        }
                                    }
                                    if (!errorFlag && mstComponentSpecCollections.size() > 0) {
                                        readCsvInfo.setMstComponentSpecCollection(mstComponentSpecCollections);
                                    }
                                }

                                if (!errorFlag) {
                                    mstComponentService.createMstComponent(readCsvInfo);
                                    addedCount = addedCount + 1;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentCode, readCsvInfo.getComponentCode(), error, 0, result, addedMsg));
                                }
                            }
                        } else {
                            failedCount = failedCount + 1;
                        } //end loop
                    }
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
            tblCsvImport.setImportTable(CommonConstants.TBL_MST_COMPONENT);
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(fileUuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_COMPONENT);
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount(readList.size() - 1);
            tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
            tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
            tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
            tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
            tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
            tblCsvImport.setLogFileUuid(logFileUuid);
            tblCsvImport.setLogFileName(FileUtil.getLogFileName(headerMap.get("mst_component")));

            tblCsvImportService.createCsvImpor(tblCsvImport);

            return importResultResponse;

        } catch (Exception ex) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            importResultResponse.setErrorMessage(
                    mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid"));
            return importResultResponse;
        }
    }

    /**
     * 部品詳細画面　更新
     *
     * @param mstComponent
     * @return
     */
    @PUT
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putComponent(MstComponentDetail mstComponent) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstComponent.getMstComponent().setUpdateDate(new java.util.Date());
        mstComponent.getMstComponent().setUpdateUserUuid(loginUser.getUserUuid());
        BasicResponse response = new BasicResponse();

        if (!mstComponentService.getMstComponentExistCheck(mstComponent.getMstComponent().getComponentCode())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }

        int cnt = mstComponentService.updateMstComponentDet(mstComponent, loginUser);
        if (cnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        }
        return response;
    }

    /**
     * 部品詳細画面　追加
     *
     * @param mstComponent
     * @return
     */
    @POST
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postComponent(MstComponentDetail mstComponent) {
        BasicResponse response = new BasicResponse();
        String componentCode = mstComponent.getMstComponent().getComponentCode();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (mstComponentService.getMstComponentExistCheck(componentCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }

        mstComponentService.createMstComponent(mstComponent, loginUser);
        return response;
    }

    /**
     * 部品情報の入力候補表示 like
     *
     * @param componentCode
     * @param componentName
     * @return
     */
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getComponentLikeCodeOrName(@QueryParam("componentCode") String componentCode, @QueryParam("componentName") String componentName) {
        return mstComponentService.getComponentLikeCodeOrName(componentCode, componentName);
    }
    
    /**
     * 部品コード、部品名称の部分一致で部品を取得する。<br>
     * ただし、使用可能な検査管理項目があるものだけを返す。
     * @param componentCode
     * @param componentName
     * @return 
     */
    @GET
    @Path("/inspect/like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getInspectComponentLike(@QueryParam("componentCode") String componentCode, @QueryParam("componentName") String componentName) {
        return mstComponentService.getInspectComponentLikeCodeOrName(componentCode, componentName);
    }

    /**
     * 部品情報の入力候補表示 equal
     *
     * @param componentCode
     * @param componentName
     * @return
     */
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getComponentByCodeOrName(@QueryParam("componentCode") String componentCode, @QueryParam("componentName") String componentName) {
        return mstComponentService.getComponentByCodeOrName(componentCode, componentName);
    }

    /**
     * TT0003 通常打上 部品コード変更時 金型部品関係テーブルより部品を取得し、金型IDが一意に定まれば金型ID、金型名称を表示する。
     * （新規登録時はメモリに保持するのみ）
     *
     * @param componentCode
     * @return
     */
    @GET
    @Path("autocomplete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldAutoComplete getComponentAutoComplete(@QueryParam("componentCode") String componentCode) {
        MstComponent mstComponent = mstComponentService.getMstComponent(componentCode);
        MstMoldAutoComplete response = new MstMoldAutoComplete();
        if (mstComponent != null) {
            response.setComponentId(mstComponent.getId());
            response.setComponentCode(mstComponent.getComponentCode());
            response.setComponentName(mstComponent.getComponentName());
            int size = mstComponent.getMstMoldComponentRelationCollection().size();
            if (size == 1) {
                Iterator<MstMoldComponentRelation> mstMoldComponentRelation = mstComponent.getMstMoldComponentRelationCollection().iterator();
                MstMold mstMold = new MstMold();
                while (mstMoldComponentRelation.hasNext()) {
                    mstMold = mstMoldComponentRelation.next().getMstMold();
                }
                response.setUuid(mstMold.getUuid());
                response.setMoldId(mstMold.getMoldId());
                response.setMoldName(mstMold.getMoldName());
            }
        } else {
            response.setError(true);
        }
        return response;

    }

    /**
     * バッチで部品マスタデータを取得
     *
     * @param latestExecutedDate
     * @return an instance of MstLocationList
     */
    @GET
    @Path("extcomponent")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getExtComponentsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstComponentService.getExtComponentsByBatch(latestExecutedDate);
    }
    
    /**
     * ページ表示に部品マスターを取得する
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @return an instance of MstComponentList
     */
    @GET
    @Path("getcomponents")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getComponentsByPage(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName, @QueryParam("componentType") Integer componentType,
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize, // ページSize
            @QueryParam("isPurchasedPart") int isPurchasedPart,
            @QueryParam("isCircuitboard") int isCircuitboard
    ) {
        return mstComponentService.getMstComponentsByPage(componentCode, componentName, componentType, sidx, sord,
                pageNumber, pageSize, true, isPurchasedPart, isCircuitboard);
    }
    
     /**
     * 検査用の部品情報の入力候補表示 like
     *
     * @param component
     * @return
     */
    @GET
    @Path("inspectlike")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getInspectComponentLikeCodeOrName(@QueryParam("component") String component) {
        return mstComponentService.getInspectComponentLikeCodeOrName(component);
    }
}
