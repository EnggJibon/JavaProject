/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.location;

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
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
@Path("location")
public class MstLocationResource {

    /**
     * Creates a new instance of MstLocationResource
     */
    public MstLocationResource() {
    }

    @Inject
    private MstLocationService mstLocationService;

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
    private MstCompanyService mstCompanyService;

    /**
     * 所在地マスタ複数取得(getLocations)
     *
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @param isEquals
     * @return an instance of MstLocationList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstLocationList getLocations(@QueryParam("locationCode") String locationCode,
            @QueryParam("locationName") String locationName,
            @QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName,
            @QueryParam("isEquals") boolean isEquals
            ) {
        return mstLocationService.getLocations(locationCode, locationName, companyCode, companyName, isEquals);
    }

    /**
     * 所在地マスタ取得(getLocation)
     *
     * @param locationCode
     * @return
     */
    @GET
    @Path("{locationCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstLocationList getLocation(@PathParam("locationCode") String locationCode) {
        return mstLocationService.getMstLocationDetail(FileUtil.getDecode(locationCode));
    }

    /**
     *
     * @param locationName
     * @return
     */
    @GET
    @Path("getname")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstLocationList getLocationByName(@QueryParam("locationName") String locationName) {
        return mstLocationService.getLocationByName(locationName);
    }

    /**
     *
     * @param locationId
     * @return
     */
    @GET
    @Path("getbyid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstLocationResp getLocationById(@QueryParam("locationId") String locationId) {
         MstLocationResp mstLocationResp = new MstLocationResp();
         mstLocationResp = mstLocationService.getLocationById(locationId);
         return mstLocationResp;
    }
    
    /**
     * 所在地マスタ件数を取得する
     *
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("locationCode") String locationCode,
            @QueryParam("locationName") String locationName,
            @QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        CountResponse count = mstLocationService.getMstLocationCount(locationCode, locationName, companyCode, companyName);
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
     * 所在地マスタ更新（putLocation)
     *
     * @param mstLocation
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putLocation(MstLocation mstLocation) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        BasicResponse response = new BasicResponse();
        if (mstLocation != null) {
            String getLocationCode = mstLocation.getLocationCode();
            if (null == getLocationCode || "".equals(getLocationCode)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
                return response;
            }
            String getCompanyId = mstLocation.getCompanyId();
            if (null == getCompanyId || "".equals(getCompanyId.trim())) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
                return response;
            }

            if (!mstLocationService.getMstLocationExistCheck(mstLocation.getLocationCode())) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
                return response;
            }

            mstLocation.setUpdateDate(new java.util.Date());
            mstLocation.setUpdateUserUuid(loginUser.getUserUuid());
            int cnt = mstLocationService.updateMstLocationByQuery(mstLocation);

            if (cnt < 1) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
            }
        }
        return response;
    }

    /**
     * 新規の所在地マスターを追加する
     *
     * @param mstLocation
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postLocation(MstLocation mstLocation) {
        BasicResponse response = new BasicResponse();

        if (mstLocation != null) {
            String getLocationCode = mstLocation.getLocationCode();
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

            if (null == getLocationCode || "".equals(getLocationCode)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
                return response;
            }
            String getCompanyId = mstLocation.getCompanyId();
            if (null == getCompanyId || "".equals(getCompanyId.trim())) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
                return response;
            }

            if (mstLocationService.getMstLocationExistCheck(getLocationCode)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                return response;
            }
            mstLocation.setId(IDGenerator.generate());
            mstLocation.setCreateDate(new java.util.Date());
            mstLocation.setCreateUserUuid(loginUser.getUserUuid());
            mstLocation.setUpdateDate(new java.util.Date());
            mstLocation.setUpdateUserUuid(loginUser.getUserUuid());
            //2016-11-30 jiangxiaosong add start
            mstLocation.setExternalFlg(CommonConstants.MINEFLAG);
            //2016-11-30 jiangxiaosong add end
            mstLocationService.createMstLocation(mstLocation);
        }

        return response;
    }

    /**
     * 所在地マスタ削除（deleteLocation）
     *
     * @param locationCode
     * @return
     */
    @DELETE
    @Path("{locationCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteLocation(@PathParam("locationCode") String locationCode) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        locationCode = FileUtil.getDecode(locationCode);
        if (!mstLocationService.getMstLocationExistCheck(locationCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        if (!mstLocationService.getMstLocationFKCheck(locationCode)) {
            mstLocationService.deleteMstLocation(locationCode);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
        }

        return response;
    }

    /**
     * 所在地マスタCSV出力（getLocationsCsv)
     *
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getLocationsCsv(@QueryParam("locationCode") String locationCode,
            @QueryParam("locationName") String locationName,
            @QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstLocationService.getMstLocationsOutputCsv(locationCode,
                locationName,
                companyCode,
                companyName,
                loginUser);
    }

    /**
     * 所在地マスタCSV取込（postLocationsCsv)
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postLocationsCsv(@PathParam("fileUuid") String fileUuid) {
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
                String userLangId = loginUser.getLangId();
                String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
                String lineNo = mstDictionaryService.getDictionaryValue(userLangId, "row_number");
                String locationCode = mstDictionaryService.getDictionaryValue(userLangId, "location_code");
                String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
                String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
                String result = mstDictionaryService.getDictionaryValue(userLangId, "db_process");
                String addedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_added");
                String updatedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_data_modified");
                String deletedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_deleted");
                String canNotdeleteMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_cannot_delete_used_record");
                String notFound = mstDictionaryService.getDictionaryValue(userLangId, "mst_error_record_not_found");
                String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");


                String locationName = mstDictionaryService.getDictionaryValue(userLangId, "location_name");
                String zipCode = mstDictionaryService.getDictionaryValue(userLangId, "zip_code");
                String address = mstDictionaryService.getDictionaryValue(userLangId, "address");
                String telNo = mstDictionaryService.getDictionaryValue(userLangId, "tel_no");
                String companyCode = mstDictionaryService.getDictionaryValue(userLangId, "company_code");
                String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
                String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");
                String mgmtCompanyCode = mstDictionaryService.getDictionaryValue(userLangId, "mgmt_company_code");//20170607 Apeng add

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("locationCode", locationCode);
                logParm.put("locationName", locationName);
                logParm.put("zipCode", zipCode);
                logParm.put("address", address);
                logParm.put("telNo", telNo);
                logParm.put("companyCode", companyCode);
                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("notFound", notFound);
                logParm.put("maxLangth", maxLangth);
                logParm.put("layout", layout);
                logParm.put("mgmtCompanyCode", mgmtCompanyCode);//20170607 Apeng add

                MstLocation readCsvInfo;

                FileUtil fu = new FileUtil();
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);

                    if (comList.size() != 10) {//20170607 Apeng update
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, "", error, 1, errorContents, layout));
                        continue;
                    }

                    String[] csvArray = new String[comList.size()];
                    String strLocationCode = String.valueOf(comList.get(0));
                    csvArray[0] = strLocationCode;
                    String strLocationName = String.valueOf(comList.get(1));
                    csvArray[1] = strLocationName;
                    String strZipCode = String.valueOf(comList.get(2));
                    csvArray[2] = strZipCode;
                    String strAddress = String.valueOf(comList.get(3));
                    csvArray[3] = strAddress;
                    String strTelNo = String.valueOf(comList.get(4));
                    csvArray[4] = strTelNo;
                    String strcompanyCode = String.valueOf(comList.get(5));
                    csvArray[5] = strcompanyCode;
                    String strMgmtCompanyCode = String.valueOf(comList.get(7));//20170710 Apeng add
                    csvArray[7] = strMgmtCompanyCode;
                    String strDelFlag = String.valueOf(comList.get(9));//20170710 Apeng update

                    //delFlag 指定されている場合
                    if (strDelFlag != null && strDelFlag.equals("1")) {
                        if (!mstLocationService.getMstLocationExistCheck(strLocationCode)) {
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, strLocationCode, error, 1, errorContents, notFound));
                        } else if (!mstLocationService.getMstLocationFKCheck(strLocationCode)) {
                            mstLocationService.deleteMstLocation(strLocationCode);
                            deletedCount = deletedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, strLocationCode, error, 0, result, deletedMsg));
                        } else {
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, strLocationCode, error, 1, errorContents, canNotdeleteMsg));
                            failedCount = failedCount + 1;
                        }
                    } else //check
                        if (mstLocationService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i)) {
                            MstCompany mstCompany = mstCompanyService.getMstCompanyByCode(strcompanyCode);
                            readCsvInfo = new MstLocation();
                            readCsvInfo.setLocationCode(strLocationCode);
                            readCsvInfo.setLocationName(strLocationName);
                            readCsvInfo.setZipCode(strZipCode);
                            readCsvInfo.setAddress(strAddress);
                            readCsvInfo.setTelNo(strTelNo);
                            readCsvInfo.setCompanyId(mstCompany.getId());
                            readCsvInfo.setMgmtCompanyCode(strMgmtCompanyCode);//20170607 Apeng add

                            if (mstLocationService.getMstLocationExistCheck(readCsvInfo.getLocationCode())) {
                                //存在チェックの場合　//更新
                                // データを更新
                                Date sysDate = new Date();
                                readCsvInfo.setUpdateDate(sysDate);
                                readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                int count = mstLocationService.updateMstLocationByQuery(readCsvInfo);
                                updatedCount = updatedCount + count;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, readCsvInfo.getLocationCode(), error, 0, result, updatedMsg));
                            } else {
                                //追加
                                Date sysDate = new Date();
                                readCsvInfo.setId(IDGenerator.generate());
                                readCsvInfo.setCreateDate(sysDate);
                                readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                                //2016-11-30 jiangxiaosong add start
                                readCsvInfo.setExternalFlg(CommonConstants.MINEFLAG);
                                //2016-11-30 jiangxiaosong add end
                                mstLocationService.createMstLocation(readCsvInfo);
                                addedCount = addedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, locationCode, readCsvInfo.getLocationCode(), error, 0, result, addedMsg));
                            }
                        } else {
                            failedCount = failedCount + 1;
                        }
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
            tblCsvImport.setImportTable(CommonConstants.TBL_MST_LOCATION);
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(fileUuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_MST_LOCATION);
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount(readList.size() - 1);
            tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
            tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
            tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
            tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
            tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
            tblCsvImport.setLogFileUuid(logFileUuid);

            String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_LOCATION);
            tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

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
     * バッチで外部所在地マスタデータを取得
     *
     * @param latestExecutedDate
     * @return an instance of MstLocationList
     */
    @GET
    @Path("extlocation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstLocationVo getExtLocationsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstLocationService.getExtLocationsByBatch(latestExecutedDate);
    }
}
