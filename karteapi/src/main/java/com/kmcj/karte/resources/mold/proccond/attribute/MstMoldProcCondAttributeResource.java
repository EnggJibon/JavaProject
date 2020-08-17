/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.attribute;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
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
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

/**
 * 金型加工条件属性マスタ
 *
 * @author admin
 */
@RequestScoped
@Path("mold/proccond/attribute")
public class MstMoldProcCondAttributeResource {

    public MstMoldProcCondAttributeResource() {

    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstMoldProcCondAttributeService mstMoldProcCondAttributeService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    /**
     * 金型加工条件属性マスタを取得する
     *
     * @param moldType
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldProcCondAttributes getMoldProcCondAttributes(@QueryParam("moldType") String moldType) {
        return mstMoldProcCondAttributeService.getMstMoldProcCondAttributes(moldType);
    }

    /**
     * 金型加工条件属性マスタを取得する
     *
     * @param moldType
     * @param moldId
     * @param moldProcCondId
     * @param moldProcCondName
     * @param externalFlg
     * @return
     */
    @GET
    @Path("type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldProcCondAttributes getMoldProcCondAttributesByType(@QueryParam("moldType") String moldType, @QueryParam("moldId") String moldId,
            @QueryParam("moldProcCondId") String moldProcCondId, @QueryParam("moldProcCondName") String moldProcCondName, @QueryParam("externalFlg") String externalFlg) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldProcCondAttributeService.getMstMoldProcCondAttributesVoByType(moldType, moldId, moldProcCondId, externalFlg, loginUser);
    }

    /**
     * 金型加工条件属性マスタ追加・更新・削除
     *
     * @param mstMoldProcCondAttributesList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldAttributes(List<MstMoldProcCondAttributes> mstMoldProcCondAttributesList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldProcCondAttributeService.getNewMstMoldProcCnondAttributes(mstMoldProcCondAttributesList, loginUser);
    }

    /**
     * 金型加工条件属性マスタCSV出力
     *
     * @param moldType
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMoldProcCondAttributesCSV(@QueryParam("moldType") String moldType) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstMoldProcCondAttributeService.outMoldTypeOfChoice(loginUser.getLangId());
        mstMoldProcCondAttributeService.outAttrTypeOfChoice(loginUser.getLangId());
        return mstMoldProcCondAttributeService.getMstMoldProcCondAttributesOutputCsv(moldType, loginUser);
    }

    /**
     * 金型加工条件属性マスタCSV取込
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMoldProcCondAttributesCSV(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
//        mstMoldProcCondAttributeService.getMessageByAttrType(loginUser.getLangId());

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
                String seq = mstDictionaryService.getDictionaryValue(userLangId, "mold_attribute_seq");
                String moldType = mstDictionaryService.getDictionaryValue(userLangId, "mold_type");
                String attrCode = mstDictionaryService.getDictionaryValue(userLangId, "mold_attribute_code");
                String attrName = mstDictionaryService.getDictionaryValue(userLangId, "mold_attribute_name");
                String attrType = mstDictionaryService.getDictionaryValue(userLangId, "mold_proc_cond_attribute_type");

                String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
                String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
                String result = mstDictionaryService.getDictionaryValue(userLangId, "db_process");

                String addedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_added");
                String updatedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_data_modified");
                String deletedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_deleted");
                //String canNotdeleteMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_cannot_delete_used_record");
                String notFound = mstDictionaryService.getDictionaryValue(userLangId, "mst_error_record_not_found");
                String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");

                String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
                String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");
                String notIsnumber = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_isnumber");
                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("seq", seq);
                logParm.put("moldType", moldType);
                logParm.put("attrCode", attrCode);
                logParm.put("attrName", attrName);
                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("notFound", notFound);
                logParm.put("maxLangth", maxLangth);
                logParm.put("notIsnumber", notIsnumber);

                Map inMoldTypeMap = mstMoldProcCondAttributeService.inMoldTypeOfChoice(loginUser.getLangId());
                Map insAttrTypeMap = mstMoldProcCondAttributeService.inAttrTypeOfChoice(loginUser.getLangId());

                MstMoldProcCondAttribute readCsvInfo;
                FileUtil fu = new FileUtil();
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);
                    if (comList.size() != 6) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, "", error, 1, errorContents, layout));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    String[] csvArray = new String[comList.size()];
                    String strSeq = String.valueOf(comList.get(0));
                    csvArray[0] = strSeq;

                    //moldTypeのkeyを
                    String strMoldType = String.valueOf(comList.get(1));
                    String strMoldTypeKey = (String) inMoldTypeMap.get(strMoldType);
                    if ("".equals(strMoldType.trim())) {
                        strMoldTypeKey = "0";
                    } else if (null == strMoldTypeKey || "".equals(strMoldTypeKey)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, moldType, strMoldType, error, 1, errorContents, notFound));
                        continue;
                    }
                    csvArray[1] = strMoldTypeKey;

                    String strAttrCode = String.valueOf(comList.get(2));
                    csvArray[2] = strAttrCode;
                    String strAttrName = String.valueOf(comList.get(3));
                    csvArray[3] = strAttrName;

                    //AttrTypeのkeyを
                    String strAttrType = String.valueOf(comList.get(4));
                    String strAttrTypeKey = (String) insAttrTypeMap.get(strAttrType);

                    if (null == strAttrTypeKey || "".equals(strAttrTypeKey.trim())) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrType, strAttrType, error, 1, errorContents, notFound));
                        continue;
                    }

                    csvArray[4] = strAttrTypeKey;

                    String strDelFlag = String.valueOf(comList.get(5));

                    Query query = entityManager.createNamedQuery("MstMoldProcCondAttribute.findByAttrCode");
                    query.setParameter("attrCode", strAttrCode);
                    query.setParameter("moldType", Integer.parseInt(strMoldTypeKey));
                    query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                    String id;
                    try {
                        MstMoldProcCondAttribute mstMoldProcCondAttribute = (MstMoldProcCondAttribute) query.getSingleResult();
                        id = mstMoldProcCondAttribute.getId();
                    } catch (NoResultException e) {
                        id = "";
                    }

                    //2016-12-5 jiangxiaosong update start
                    //delFlag 指定されている場合
                    if (strDelFlag != null && strDelFlag.trim().equals("1")) {
                        if (!"".equals(id)) {
                            // FK　関連チェック
//                        if (!mstMoldProcCondAttributeService.getMstMoldProcCondAttributeFKCheck(id)) {
                            mstMoldProcCondAttributeService.deleteMstMoldProcCondAttribute(id);
                            deletedCount = deletedCount + 1;
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, strAttrCode, error, 0, result, deletedMsg));
//                        } else {
//                            //エラー情報をログファイルに記入
//                            failedCount = failedCount + 1;
//                            //エラー情報をログファイルに記入
//                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, strAttrCode, error, 1, errorContents, canNotdeleteMsg));
//                        }

                        } else {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, strAttrCode, error, 1, errorContents, notFound));
                        }

                    } else //check
                    {
                        if (mstMoldProcCondAttributeService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i)) {

                            readCsvInfo = new MstMoldProcCondAttribute();
                            readCsvInfo.setSeq(Integer.parseInt(strSeq));
                            readCsvInfo.setAttrCode(strAttrCode);
                            readCsvInfo.setAttrName(strAttrName);
                            readCsvInfo.setAttrType(Integer.parseInt(strAttrTypeKey));
                            readCsvInfo.setMoldType(Integer.parseInt(strMoldTypeKey));

                            if (!"".equals(id)) {
                                //存在チェックの場合　//更新
                                // データを更新
                                Date sysDate = new Date();
                                readCsvInfo.setId(id);
                                readCsvInfo.setUpdateDate(sysDate);
                                readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                mstMoldProcCondAttributeService.updateMstMoldProcCondAttributeByQuery(readCsvInfo);
                                updatedCount = updatedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, readCsvInfo.getAttrCode(), error, 0, result, updatedMsg));
                            } else {
                                //追加
                                Date sysDate = new Date();
                                readCsvInfo.setId(IDGenerator.generate());
                                readCsvInfo.setCreateDate(sysDate);
                                readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                                readCsvInfo.setExternalFlg(CommonConstants.MINEFLAG);
                                mstMoldProcCondAttributeService.createMstMoldProcCondAttribute(readCsvInfo);
                                addedCount = addedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, readCsvInfo.getAttrCode(), error, 0, result, addedMsg));
                            }

                        } else {
                            failedCount = failedCount + 1;
                        }
                    }
                    //2016-12-5 jiangxiaosong update start
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
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_MOLD_PROCCOND_ATTRIBUTE);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD_PROCCOND_ATTRIBUTE);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MOLD_PROCCOND_ATTRIBUTE);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;

    }

    /**
     * バッチで金型加工条件属性マスタデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extmoldproccondattribute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldProcCondAttributeList getExtMstMoldProcCondAttributesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstMoldProcCondAttributeService.getExtMstMoldProcCondAttributesByBatch(latestExecutedDate);
    }
}
