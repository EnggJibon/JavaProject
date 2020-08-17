/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user;

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
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.SafeHashGenerator;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.language.MstLanguage;
import com.kmcj.karte.resources.language.MstLanguageService;
import com.kmcj.karte.resources.timezone.MstTimezone;
import com.kmcj.karte.resources.timezone.MstTimezoneService;
import com.kmcj.karte.resources.authorization.MstAuth;
import com.kmcj.karte.resources.authorization.MstAuthService;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.license.limit.MstLicenseLimit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.RandomStringUtils;
import com.kmcj.karte.resources.license.limit.MstLicenseLimitService;

/**
 * REST Web Service
 *
 * @author Fumisato
 */
@RequestScoped
@Path("user")
public class MstUserResource {

    @Context
    private UriInfo context;
    
    @Inject
    private MstUserService mstUserService;
    
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MstLanguageService mstLanguageService;

    @Inject
    private MstTimezoneService mstTimezoneService;

    @Inject
    private MstAuthService mstAuthService;

    @Inject
    private MstCompanyService mstCompanyService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private MstLicenseLimitService mstLicenseLimitService;
    /**
     * Creates a new instance of MstUserResource
     */
    public MstUserResource() {
    }

    /**
     * すべてのユーザーマスターを取得する
     * @return an instance of MstUserList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstUserList getUsers() {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        String timeZone = loginUser.getJavaZoneId();
        return mstUserService.getMstUsers(timeZone);
    }

    /**
     * IDを指定してユーザーマスターを1件取得する
     * @param userId
     * @return an instance of MstUserList
     */
    @GET
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstUserList getUser(@PathParam("userId") String userId) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        String timeZone = loginUser.getJavaZoneId();
        return mstUserService.getSingleMstUser(userId, timeZone);
    }

    /**
     * 既存のユーザーマスターを更新する
     * @param mstUser
     * @return an instance of BasicResponse
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putUser(MstUser mstUser) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        mstUser.setUpdateUserUuid(loginUser.getUserUuid());
        mstUser.setUpdateDate(new java.util.Date());
        int cnt = mstUserService.updateMstUserByQuery(mstUser);
        BasicResponse response = new BasicResponse();
        if (cnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return response;
    }

    /**
     * 新規のユーザーマスターを追加する
     * @param mstUser
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserAddedResponse postUser(MstUser mstUser) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        UserAddedResponse response = new UserAddedResponse();
        if (mstUserService.getMstUser(mstUser.getUserId()) != null || mstUser.getUserId() == null || "".equals(mstUser.getUserId())
                || mstUser.getUserName() == null || "".equals(mstUser.getUserName())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            if (mstUser.getUserId() == null || "".equals(mstUser.getUserId())
                || mstUser.getUserName() == null || "".equals(mstUser.getUserName())) {
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            } else {
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            } 
        }
        //ライセンス リミット
        else if (mstLicenseLimitService.CheckUserLimit(loginUser.getLangId(),response)) {
                 
            //初期パスワードはユーザーIDと同じ→リセット時と同じランダムパスワードに変更
            //String initialPassword = SafeHashGenerator.getStretchedPassword(mstUser.getUserId(), mstUser.getUserId());
            //初期パスワード生成
            String initialPassword = RandomStringUtils.randomAlphanumeric(10);
            String hashedInitialPassword = SafeHashGenerator.getStretchedPassword(initialPassword, mstUser.getUserId());
            mstUser.setHashedPassword(hashedInitialPassword);
            mstUser.setPasswordChangedAt(null);
            mstUser.setPasswordExpiresAt(null);
            mstUser.setCreateUserUuid(loginUser.getUserUuid());
            mstUser.setCreateDate(new java.util.Date());
            mstUser.setUpdateUserUuid(loginUser.getUserUuid());
            mstUser.setUpdateDate(mstUser.getCreateDate());
            if (mstUser.getIndefiniteFlg() == null) {
                mstUser.setIndefiniteFlg(0);
            }
            mstUser.setInitialPasswordFlg(1); //初期パスワードフラグを立てる
            mstUser.setLoginFailCount(0); //ログイン失敗回数はゼロ
            mstUserService.createMstUser(mstUser);
            response.setInitialPassword(initialPassword);     
        }
        return response;
    }

    /**
     * IDを指定してユーザーマスターを1件削除する
     * @param userId
     * @return an instance of MstUserList
     */
    @DELETE
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteUser(@PathParam("userId") String userId) {
        BasicResponse response = new BasicResponse();
        int cnt = mstUserService.deleteMstUser(userId);
        if (cnt < 1) {
            LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return response;
    }

     /**
     * パスワードをリセットする
     * @param mstUser
     * @return an instance of BasicResponse
     */
    @POST
    @Path("pwdreset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstUserList resetPassword(MstUser mstUser) {
        int cnt = mstUserService.resetPassword(mstUser);
        MstUserList response = new MstUserList();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (cnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
        }
        else {
            mstUser.setDisplayTimeZone(loginUser.getJavaZoneId());
            mstUser.setHashedPassword(null);
            response.getMstUsers().add(mstUser);
        }
        return response;
    }

    @POST
    @Path("pwdselfreset/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse selfResetPassword(@PathParam("userId") String userId, @QueryParam("lang") String lang) {
        return mstUserService.selfResetPassword(userId, lang);
    }

    /**
     *ユーザーマスタCSV出力（getUserCsv)
     * @param userId
     * @param userName
     * @param validFlg
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getUserCsv(@QueryParam("userId") String userId,
            @QueryParam("userName") String userName, @QueryParam("validFlg") String validFlg) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstUserService.getMstUserOutputCsv(userId, userName, validFlg, loginUser);
    }

    /**
     *ユーザーマスタCSV取込（postUserCsv)
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postUserCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long deletedCount = 0;
        long failedCount = 0;
        String logFileUuid = IDGenerator.generate();
        String langId = loginUser.getLangId();

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
                String userId = mstDictionaryService.getDictionaryValue(userLangId, "user_id");
                String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
                String result = mstDictionaryService.getDictionaryValue(userLangId, "db_process");
                String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");

                MstUser readCsvInfo;
                FileUtil fu = new FileUtil();
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList usrList = (ArrayList) readList.get(i);

                    if (usrList.size() > 11) {
                        //エラー情報をログファイルに記入
                        String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, userId, "", error, 1, errorContents, layout));
                        continue;
                    }

                    String[] csvArray = new String[usrList.size()];
                    String strUserId = String.valueOf(usrList.get(0));
                    csvArray[0] = strUserId;
                    String strUserName = String.valueOf(usrList.get(1));
                    csvArray[1] = strUserName;
                    String strMailAddress = String.valueOf(usrList.get(2));
                    csvArray[2] = strMailAddress;
                    String strLanguage = String.valueOf(usrList.get(3));
                    csvArray[3] = strLanguage;
                    String strTimeZone = String.valueOf(usrList.get(4));
                    csvArray[4] = strTimeZone;
                    String strAuth = String.valueOf(usrList.get(5));
                    csvArray[5] = strAuth;
                    String strCompanyName = String.valueOf(usrList.get(6));
                    csvArray[6] = strCompanyName;
                    String strDepartment = String.valueOf(usrList.get(7));
                    csvArray[7] = strDepartment;
                    String strProcCd = String.valueOf(usrList.get(8));
                    csvArray[8] = strProcCd;
                    String strValidFlg = String.valueOf(usrList.get(9));
                    csvArray[9] = strValidFlg;
                    String strDelFlag = String.valueOf(usrList.get(10));

                    //delFlag 指定されている場合
                    if (strDelFlag != null && strDelFlag.trim().equals("1")) {
                        if (!mstUserService.getMstUserExistCheck(strUserId)) {
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            String notFound = mstDictionaryService.getDictionaryValue(userLangId, "mst_error_record_not_found");
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, userId, strUserId, error, 1, errorContents, notFound));
                        } else {
                            try {
                                mstUserService.deleteMstUser(strUserId);
                                deletedCount = deletedCount + 1;
                                //エラー情報をログファイルに記入
                                String deletedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_deleted");
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, userId, strUserId, error, 0, result, deletedMsg));
                            } catch (Exception e) {
                                //エラー情報をログファイルに記入
                                String canNotdeleteMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_cannot_delete_used_record");
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, userId, strUserId, error, 1, errorContents, canNotdeleteMsg));
                                failedCount = failedCount + 1;
                            }
                        }
                    } else //check

                        if (mstUserService.checkCsvFileData(csvArray, userLangId, logFile, i)) {
                            readCsvInfo = new MstUser();
                            readCsvInfo.setUserId(fu.blankEscape(csvArray[0].trim()));
                            readCsvInfo.setUserName(fu.blankEscape(csvArray[1].trim()));
                            readCsvInfo.setMailAddress(fu.blankEscape(csvArray[2].trim()));

                            MstLanguage langResponse = mstLanguageService.getByLangLanguage(csvArray[3].trim());
                            readCsvInfo.setLangId(fu.blankEscape(langResponse.getId()));

                            MstTimezone timeResponse = mstTimezoneService.getByTimezoneName(csvArray[4].trim());
                            readCsvInfo.setTimezone(fu.blankEscape(timeResponse.getId()));

                            MstAuth authResponse = mstAuthService.getByGroupNameAuths(csvArray[5].trim());
                            readCsvInfo.setAuthId(fu.blankEscape(authResponse.getId()));

                            MstCompany companyResponse = mstCompanyService.getByCompanyNameId(csvArray[6].trim());
                            if (companyResponse != null) {
                                readCsvInfo.setCompanyId(fu.blankEscape(companyResponse.getId()));
                            } else {
                                readCsvInfo.setCompanyId(null);
                            }

                            MstChoice departmentResponse = mstChoiceService.getByChoiceSeq(csvArray[7].trim(), langId, "mst_user.department");
                            if (departmentResponse != null) {
                                readCsvInfo.setDepartment(fu.blankEscape(departmentResponse.getMstChoicePK().getSeq()));
                            } else {
                                readCsvInfo.setCompanyId(null);
                            }

                            readCsvInfo.setProcCd(fu.blankEscape(csvArray[8].trim()));
                            readCsvInfo.setValidFlg(fu.blankEscape(csvArray[9].trim()));

                            MstUser user = mstUserService.getMstUser(readCsvInfo.getUserId());
                            if (user != null) {
                                readCsvInfo.setIndefiniteFlg(user.getIndefiniteFlg());
                            //if (mstUserService.getMstUserExistCheck(readCsvInfo.getUserId())) {
                                //存在チェックの場合　//更新
                                // データを更新
                                Date sysDate = new Date();
                                readCsvInfo.setUpdateDate(sysDate);
                                readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                int count = mstUserService.updateMstUserByQuery(readCsvInfo);
                                updatedCount = updatedCount + count;
                                //エラー情報をログファイルに記入
                                String updatedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_data_modified");
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, userId, readCsvInfo.getUserId(), error, 0, result, updatedMsg));
                            } else {
                                BasicResponse response = new BasicResponse();
                                if (mstLicenseLimitService.CheckUserLimit(loginUser.getLangId(),response)) {
                                    //追加
                                    
                                    
                                    Date sysDate = new Date();
                                    readCsvInfo.setUuid(IDGenerator.generate());
                                    readCsvInfo.setCreateDate(sysDate);
                                    readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                                    //初期パスワードセット
                                    String initialPassword = SafeHashGenerator.getStretchedPassword(RandomStringUtils.randomAlphanumeric(10), readCsvInfo.getUserId());
                                    readCsvInfo.setHashedPassword(initialPassword);
                                    readCsvInfo.setPasswordChangedAt(null);
                                    readCsvInfo.setPasswordExpiresAt(null);
                                    readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                    readCsvInfo.setUpdateDate(readCsvInfo.getCreateDate());
                                    readCsvInfo.setIndefiniteFlg(0); //CSV取込ではPW無期限設定はできない
                                    readCsvInfo.setInitialPasswordFlg(1); //初期PWフラグを立てる
                                    readCsvInfo.setLoginFailCount(0); //ログイン失敗回数はゼロ
                                    mstUserService.createMstUser(readCsvInfo);
                                    addedCount = addedCount + 1;
                                    //エラー情報をログファイルに記入
                                    String addedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_added");
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, userId, readCsvInfo.getUserId(), error, 0, result, addedMsg));
                                }
                                else {  
                                    failedCount = failedCount + 1;
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, userId, readCsvInfo.getUserId(), error, 1, result, response.getErrorMessage()));
                                }       
                            }
                        }   
                      else {
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
            tblCsvImport.setImportTable(CommonConstants.TBL_MST_USER);
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(fileUuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_MST_USER);
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount(readList.size() - 1);
            tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
            tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
            tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
            tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
            tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
            tblCsvImport.setLogFileUuid(logFileUuid);

            String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_USER);
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
     ユーザーマスタ件数を取得する
     * @param userId
     * @param userName
     * @param validFlg
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getUserCount(@QueryParam("userId") String userId,
            @QueryParam("userName") String userName, @QueryParam("validFlg") String validFlg) {
        CountResponse count = mstUserService.getMstUserCount(userId, userName,validFlg);

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
     検索ユーザーマスタデータを取得する
     * @param userId
     * @param userName
     * @param validFlg
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstUserList getSearchUserData(@QueryParam("userId") String userId,
            @QueryParam("userName") String userName, @QueryParam("validFlg") String validFlg) {
        List list = mstUserService.searchUserListSql(userId, userName, validFlg);
        MstUserList response = new MstUserList();
        response.setMstUsers(list);
        return response;
    } 
}
