/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.installationsite;

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
import com.kmcj.karte.resources.company.MstCLIAutoComplete;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.location.MstLocationService;
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
@Path("installationsite")
public class MstInstallationSiteResource {

    /**
     * Creates a new instance of MstInstallationSiteResource
     */
    public MstInstallationSiteResource() {
    }

    @Inject
    private MstInstallationSiteService mstInstallationSiteService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstLocationService mstLocationService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    /**
     * 設置場所マスタ複数取得(getInstallationSites)
     *
     * @param installationSiteCode
     * @param installationSiteName
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @param isEquals
     * @return an instance of MstInstallationSiteList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstInstallationSiteList getInstallationSites(@QueryParam("installationSiteCode") String installationSiteCode,
            @QueryParam("installationSiteName") String installationSiteName,
            @QueryParam("locationCode") String locationCode,
            @QueryParam("locationName") String locationName,
            @QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName,
            @QueryParam("isEquals") boolean isEquals
    ) {
        return mstInstallationSiteService.getMstInstallationSites(installationSiteCode,
                installationSiteName,
                locationCode,
                locationName,
                companyCode,
                companyName,
                isEquals
        );
    }

    /**
     *
     * @param installationSiteName
     * @return
     */
    @GET
    @Path("getname")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstInstallationSiteList getInstallationSitesByName(@QueryParam("installationSiteName") String installationSiteName) {
        return mstInstallationSiteService.getMstInstallationSitesByName(installationSiteName);
    }
    
    /**
     *
     * @param installationSiteId
     * @return
     */
    @GET
    @Path("getbyid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstInstallationSiteResp getInstallationSiteById(@QueryParam("installationSiteId") String installationSiteId) {
         MstInstallationSiteResp mstInstallationSiteResp = new MstInstallationSiteResp();
         mstInstallationSiteResp = mstInstallationSiteService.getInstallationSiteById(installationSiteId);
         return mstInstallationSiteResp;
    }

    /**
     * 設置場所マスタ取得(getInstallationSite)
     *
     * @param installationSiteCode
     * @return
     */
    @GET
    @Path("{installationSiteCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstInstallationSiteList getInstallationSite(@PathParam("installationSiteCode") String installationSiteCode) {
        return mstInstallationSiteService.getMstInstallationSiteDetail(FileUtil.getDecode(installationSiteCode));
    }

    /**
     * 設置場所マスタ件数を取得する
     *
     * @param installationSiteCode
     * @param installationSiteName
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
    public CountResponse getRecordCount(@QueryParam("installationSiteCode") String installationSiteCode,
            @QueryParam("installationSiteName") String installationSiteName,
            @QueryParam("locationCode") String locationCode,
            @QueryParam("locationName") String locationName,
            @QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        CountResponse count = mstInstallationSiteService.getMstInstallationSiteCount(installationSiteCode, installationSiteName, locationCode, locationName, companyCode, companyName);
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
     * 設置場所マスタ更新（putInstallationSite)
     *
     * @param mstInstallationSite
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putInstallationSite(MstInstallationSite mstInstallationSite) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        mstInstallationSite.setUpdateDate(new java.util.Date());
        mstInstallationSite.setUpdateUserUuid(loginUser.getUserUuid());

        String getInstallationSiteCode = mstInstallationSite.getInstallationSiteCode();
        
        if (null == getInstallationSiteCode || "".equals(getInstallationSiteCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        String getLocationId = mstInstallationSite.getLocationId();
        if (null == getLocationId || "".equals(getLocationId.trim())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        
        
        if (!mstInstallationSiteService.getMstInstallationSiteExistCheck(getInstallationSiteCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }

        int cnt = mstInstallationSiteService.updateMstInstallationSiteByQuery(mstInstallationSite);

        if (cnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        }
        return response;
    }

    /**
     * 設置場所マスタ追加（postInstallationSite)
     *
     * @param mstInstallationSite
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postInstallationSite(MstInstallationSite mstInstallationSite) {
        BasicResponse response = new BasicResponse();
        String getInstallationSiteCode = mstInstallationSite.getInstallationSiteCode();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        if (null == getInstallationSiteCode || "".equals(getInstallationSiteCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        String getLocationId = mstInstallationSite.getLocationId();
        if (null == getLocationId || "".equals(getLocationId.trim())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        
        if (mstInstallationSiteService.getMstInstallationSiteExistCheck(getInstallationSiteCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }

        mstInstallationSite.setId(IDGenerator.generate());
        mstInstallationSite.setCreateDate(new java.util.Date());
        mstInstallationSite.setCreateUserUuid(loginUser.getUserUuid());
        mstInstallationSite.setUpdateDate(new java.util.Date());
        mstInstallationSite.setUpdateUserUuid(loginUser.getUserUuid());
        //2016-11-30 jiangxiaosong add start
        mstInstallationSite.setExternalFlg(CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        mstInstallationSiteService.createMstInstallationSite(mstInstallationSite);
        return response;
    }

    /**
     * 設置場所マスタ削除（deleteInstallationSite）
     *
     * @param installationSiteCode
     * @return
     */
    @DELETE
    @Path("{installationSiteCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteInstallationSite(@PathParam("installationSiteCode") String installationSiteCode) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        installationSiteCode = FileUtil.getDecode(installationSiteCode);
        if (!mstInstallationSiteService.getMstInstallationSiteExistCheck(installationSiteCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }

        if (!mstInstallationSiteService.getMstInstallationSiteFKCheck(installationSiteCode)) {
            mstInstallationSiteService.deleteMstInstallationSite(installationSiteCode);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
        }

        return response;
    }

    /**
     * 設置場所マスタCSV出力（getInstallationSitesCsv)
     *
     * @param installationSiteCode
     * @param installationSiteName
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
    public FileReponse getInstallationSitesCsv(
            @QueryParam("installationSiteCode") String installationSiteCode,
            @QueryParam("installationSiteName") String installationSiteName,
            @QueryParam("locationCode") String locationCode,
            @QueryParam("locationName") String locationName,
            @QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstInstallationSiteService.getMstInstallationSitesOutputCsv(installationSiteCode,
                installationSiteName,
                locationCode,
                locationName,
                companyCode,
                companyName,
                loginUser);
    }

    /**
     * 設置場所マスタCSV取込（postInstallationSitesCsv)
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postInstallationSitesCsv(@PathParam("fileUuid") String fileUuid) {
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
                String installationSiteCode = mstDictionaryService.getDictionaryValue(userLangId, "installation_site_code");
                String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
                String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
                String result = mstDictionaryService.getDictionaryValue(userLangId, "db_process");
                String addedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_added");
                String updatedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_data_modified");
                String deletedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_deleted");
                String canNotdeleteMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_cannot_delete_used_record");
                String notFound = mstDictionaryService.getDictionaryValue(userLangId, "mst_error_record_not_found");
                String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");

                String installationSiteName = mstDictionaryService.getDictionaryValue(userLangId, "installation_site_name");
                String locationId = mstDictionaryService.getDictionaryValue(userLangId, "location_id");

                String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
                String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("installationSiteCode", installationSiteCode);
                logParm.put("installationSiteName", installationSiteName);
                logParm.put("locationId", locationId);
                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("maxLangth", maxLangth);

                MstInstallationSite readCsvInfo;

                FileUtil fu = new FileUtil();
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);

                    if (comList.size() != 7) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, installationSiteCode, "", error, 1, errorContents, layout));
                        continue;
                    }

                    String[] csvArray = new String[comList.size()];
                    String strInstallationsiteCode = String.valueOf(comList.get(0));
                    csvArray[0] = strInstallationsiteCode;
                    String strInstallationsiteName = String.valueOf(comList.get(1));
                    csvArray[1] = strInstallationsiteName;
                    String strLocationCode = String.valueOf(comList.get(2));
                    csvArray[2] = strLocationCode;
                    String strDelFlag = String.valueOf(comList.get(6));

                    //delFlag 指定されている場合
                    if (strDelFlag != null && strDelFlag.trim().equals("1")) {
                        if (!mstInstallationSiteService.getMstInstallationSiteExistCheck(strInstallationsiteCode)) {
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, installationSiteCode, strInstallationsiteCode, error, 1, errorContents, notFound));
                        } else /// FK 関連チェック
                            if (!mstInstallationSiteService.getMstInstallationSiteFKCheck(strInstallationsiteCode)) {
                                mstInstallationSiteService.deleteMstInstallationSite(strInstallationsiteCode);
                                deletedCount = deletedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, installationSiteCode, strInstallationsiteCode, error, 0, result, deletedMsg));
                            } else {
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, installationSiteCode, strInstallationsiteCode, error, 0, result, canNotdeleteMsg));
                            }
                    } else //check
                    {
                        if (mstInstallationSiteService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i)) {
                            MstLocation mstLocation = mstLocationService.getMstLocationByCode(strLocationCode);
                            if (mstLocation == null) {
                                failedCount = failedCount + 1;
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, installationSiteCode, strLocationCode, error, 1, result, notFound));
                                continue;
                            }
                            readCsvInfo = new MstInstallationSite();
                            readCsvInfo.setInstallationSiteCode(strInstallationsiteCode);
                            readCsvInfo.setInstallationSiteName(strInstallationsiteName);
                            readCsvInfo.setLocationId(mstLocation.getId());
                            if (mstInstallationSiteService.getMstInstallationSiteExistCheck(readCsvInfo.getInstallationSiteCode())) {
                                //存在チェックの場合　//更新
                                // データを更新
                                Date sysDate = new Date();
                                readCsvInfo.setUpdateDate(sysDate);
                                readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                int count = mstInstallationSiteService.updateMstInstallationSiteByQuery(readCsvInfo);
                                updatedCount = updatedCount + count;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, installationSiteCode, readCsvInfo.getInstallationSiteCode(), error, 0, result, updatedMsg));
                            } else {
                                //追加
                                Date sysDate = new Date();
                                readCsvInfo.setId(IDGenerator.generate());
                                readCsvInfo.setCreateDate(sysDate);
                                readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                                //2016-11-30 jiangxiaosong add start
                                readCsvInfo.setExternalFlg(CommonConstants.MINEFLAG);
                                //2016-11-30 jiangxiaosong add end
                                mstInstallationSiteService.createMstInstallationSite(readCsvInfo);
                                addedCount = addedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, installationSiteCode, readCsvInfo.getInstallationSiteCode(), error, 0, result, addedMsg));
                            }
                        } else {
                            failedCount = failedCount + 1;
                        }
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
            tblCsvImport.setImportTable(CommonConstants.TBL_MST_INSTALLATION_SITE);
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(fileUuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_INSTALLATION_SITE);
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount(readList.size() - 1);
            tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
            tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
            tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
            tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
            tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
            tblCsvImport.setLogFileUuid(logFileUuid);

            String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_INSTALLATION_SITE);
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
     * バッチで設置場所マスタデータを取得、追加、更新する
     *
     * @param latestExecutedDate
     * @return an instance of MstLocationList
     */
    @GET
    @Path("extinstallationsite")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstInstallationSiteList getExtInstallationSitesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstInstallationSiteService.getExtInstallationSitesByBatch(latestExecutedDate);
    }
    
    
    /**
     * 所在３点セット制御 順番
     * @param companyId
     * @param locationId
     * @return 
     */
    @GET
    @Path("cliautocomplete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCLIAutoComplete getMstCLIAutoComplete(@QueryParam("companyId") String companyId,@QueryParam("locationId") String locationId){
        return mstInstallationSiteService.getMstCLIAutoComplete(companyId, locationId);
    }
    
    
    /**
     * 所在３点セット制御 逆
     * @param locationId
     * @param installationSiteId
     * @return 
     */
    @GET
    @Path("cliautocompletedown")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCLIAutoComplete getMstCLIAutoCompleteDown(@QueryParam("locationId") String locationId,@QueryParam("installationSiteId")String installationSiteId){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstInstallationSiteService.getMstCLIAutoCompleteDown(locationId, installationSiteId, loginUser);
    }
    
    /**
     * カスタマイズ,保管場所コンボボックスに設定用。
     *
     * @return
     */
    @GET
    @Path("load/combobox")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstInstallationSiteList loadComboBoxInstallationSiteList() {
        return mstInstallationSiteService.loadComboBoxInstallationSiteList();
    }
}
