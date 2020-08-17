/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.attribute;

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
 * 金型属性マスタの処理
 *
 * @author admin
 */
@RequestScoped
@Path("mold/attribute")
public class MstMoldAttributeResource {

    public MstMoldAttributeResource() {
    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstMoldAttributeService mstMoldAttributeService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    /**
     * すべての金型属性マスターを取得する
     *
     * @param moldType
     * @return an instance of MstMoldtAttributeList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldAttributeList getMoldAttributes(@QueryParam("moldType") String moldType) {
        MstMoldAttributeList mstMoldAttributeList = mstMoldAttributeService.getMstMoldAttributes(moldType);
        MstMoldAttributeList newMstMoldAttributeList = new MstMoldAttributeList();
        List<MstMoldAttributes> MstMoldAttributes = new ArrayList<>();
        MstMoldAttributes response;

        for (int i = 0; i < mstMoldAttributeList.getMstMoldAttribute().size(); i++) {
            MstMoldAttribute mstMoldAttribute = mstMoldAttributeList.getMstMoldAttribute().get(i);
            response = new MstMoldAttributes();
            response.setId(mstMoldAttribute.getId());
            response.setSeq(mstMoldAttribute.getSeq());
            response.setAttrCode(mstMoldAttribute.getAttrCode());
            response.setAttrName(mstMoldAttribute.getAttrName());
            response.setAttrType(mstMoldAttribute.getAttrType());
            response.setFileLinkPtnId(mstMoldAttribute.getFileLinkPtnId() == null ? "" : mstMoldAttribute.getFileLinkPtnId());
            if (mstMoldAttribute.getFileLinkPtnId() != null) {
                response.setFileLinkPtnName(mstMoldAttribute.getMstFileLinkPtn_MoldAttr().getFileLinkPtnName());
                response.setLinkString(mstMoldAttribute.getMstFileLinkPtn_MoldAttr().getLinkString());
            } else {
                response.setFileLinkPtnName("");
                response.setLinkString("");
            }

            MstMoldAttributes.add(response);
        }
        newMstMoldAttributeList.setMstMoldAttributes(MstMoldAttributes);
        return newMstMoldAttributeList;
    }

    /**
     * 金型詳細画面で金型仕様を取得　金型Type
     *
     * @param moldType
     * @param moldId
     * @param moldSpecId
     * @param moldSpecName
     * @param externalFlg
     * @return an instance of MstMoldtAttributeList
     */
    @GET
    @Path("type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldAttributes getMoldAttributesByType(@QueryParam("moldType") String moldType, @QueryParam("moldId") String moldId,
            @QueryParam("moldSpecId") String moldSpecId, @QueryParam("moldSpecName") String moldSpecName,
            @QueryParam("externalFlg") String externalFlg) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        //return mstMoldAttributeService.getMstMoldAttributesByType(moldType,FileUtil.getDecode(moldId),moldSpecName,loginUser,externalFlg);
        return mstMoldAttributeService.getMstMoldAttributesByTypeAndSpecHistoryId(moldType, FileUtil.getDecode(moldId), moldSpecId, moldSpecName, loginUser, externalFlg);
    }

    /**
     *
     * 金型属性マスタ追加・更新・削除
     *
     * @param mstMoldAttributesList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMoldAttributes(List<MstMoldAttributes> mstMoldAttributesList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldAttributeService.getNewMstMoldAttributes(mstMoldAttributesList, loginUser);

    }

    /**
     * 金型属性マスタCSV出力
     *
     * @param moldType
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMoldAttributesCSV(@QueryParam("moldType") String moldType) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstMoldAttributeService.outMoldTypeOfChoice(loginUser.getLangId());
        mstMoldAttributeService.outAttrTypeOfChoice(loginUser.getLangId());
        return mstMoldAttributeService.getMstMoldAttributesOutputCsv(moldType, loginUser);
    }

    /**
     * 金型属性マスタCSV取込
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMoldAttributesCSV(@PathParam("fileUuid") String fileUuid) {
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
                String fileLinkPtnName = mstDictionaryService.getDictionaryValue(userLangId, "file_link_ptn_name");
                String moldAttributeType = mstDictionaryService.getDictionaryValue(userLangId, "mold_attribute_type");

                String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
                String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
                String result = mstDictionaryService.getDictionaryValue(userLangId, "db_process");

                String addedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_added");
                String updatedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_data_modified");
                String deletedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_deleted");
                //String canNotdeleteMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_cannot_delete_used_record");
                String notFound = mstDictionaryService.getDictionaryValue(userLangId, "mst_error_record_not_found");
                String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");

                String attrName = mstDictionaryService.getDictionaryValue(userLangId, "mold_attribute_name");
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

                Map inMoldTypeMap = mstMoldAttributeService.inMoldTypeOfChoice(loginUser.getLangId());
                Map insAttrTypeMap = mstMoldAttributeService.inAttrTypeOfChoice(loginUser.getLangId());

                MstMoldAttribute readCsvInfo;
                FileUtil fu = new FileUtil();
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);
                    if (comList.size() != 7) {
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
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, moldAttributeType, strAttrType, error, 1, errorContents, notFound));
                        continue;
                    }

                    csvArray[4] = strAttrTypeKey;

                    csvArray[5] = String.valueOf(comList.get(5));

                    String strDelFlag = String.valueOf(comList.get(6));

                    Query query = entityManager.createNamedQuery("MstMoldAttribute.findByAttrCode");
                    query.setParameter("attrCode", strAttrCode);
                    query.setParameter("moldType", Integer.parseInt(strMoldTypeKey));
                    query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                    String id;
                    try {
                        MstMoldAttribute mstMoldAttribute = (MstMoldAttribute) query.getSingleResult();
                        id = mstMoldAttribute.getId();
                    } catch (NoResultException e) {
                        id = "";
                    }
                    //delFlag 指定されている場合 
                    if (strDelFlag != null && strDelFlag.trim().equals("1")) {
                        if (!"".equals(id)) {
                            //  if (mstMoldAttributeService.getMstMoldAttributeCheck(strAttrCode,Integer.parseInt(strMoldTypeKey))) {
                            // FK　関連チェック 
//                        if (!mstMoldAttributeService.getMstMoldAttributeFKCheck(id)) {
                            mstMoldAttributeService.deleteMstMoldAttribute(id);
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
                        if (mstMoldAttributeService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i)) {

                            readCsvInfo = new MstMoldAttribute();
                            String strfileLinkPtnName = String.valueOf(comList.get(5));
                            String strFileLinkPtnId;
                            if ("5".equals(strAttrTypeKey)) {
                                if (strfileLinkPtnName != null && !"".equals(strfileLinkPtnName)) {
                                    strFileLinkPtnId = mstMoldAttributeService.getFileLinkPtnIdByName(strfileLinkPtnName);

                                    if ("".equals(strFileLinkPtnId)) {
                                        failedCount = failedCount + 1;
                                        //エラー情報をログファイルに記入
                                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, fileLinkPtnName, strfileLinkPtnName, error, 1, errorContents, notFound));
                                        continue;
                                    }
                                    if (!"".equals(strFileLinkPtnId)) {
                                        readCsvInfo.setFileLinkPtnId(strFileLinkPtnId);
                                    } else {
                                        //エラー情報をログファイルに記入
                                        failedCount = failedCount + 1;
                                        //エラー情報をログファイルに記入
                                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, strAttrCode, error, 1, errorContents, notFound));
                                        continue;
                                    }
                                }
                            }

                            readCsvInfo.setSeq(Integer.parseInt(strSeq));
                            readCsvInfo.setAttrCode(strAttrCode);
                            readCsvInfo.setMoldType(Integer.parseInt(strMoldTypeKey));
                            readCsvInfo.setAttrName(strAttrName);
                            readCsvInfo.setAttrType(Integer.parseInt(strAttrTypeKey));
                            if (!"".equals(id)) {
                                //if (mstMoldAttributeService.getMstMoldAttributeCheck(strAttrCode,Integer.parseInt(strMoldTypeKey))) {
                                //存在チェックの場合　//更新
                                // データを更新
                                Date sysDate = new Date();
                                readCsvInfo.setId(id);
                                readCsvInfo.setUpdateDate(sysDate);
                                readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                int count = mstMoldAttributeService.updateMstMoldAttributeByQuery(readCsvInfo);
                                updatedCount = updatedCount + count;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, readCsvInfo.getAttrCode(), error, 0, result, updatedMsg));
                            } else {
                                //追加
                                Date sysDate = new Date();
                                readCsvInfo.setId(IDGenerator.generate());
                                readCsvInfo.setCreateDate(sysDate);
                                readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                                //2016-12-2 jiangxiaosong update start
                                readCsvInfo.setExternalFlg(CommonConstants.MINEFLAG);
                                //2016-12-2 jiangxiaosong update end
                                mstMoldAttributeService.createMstMoldAttribute(readCsvInfo);
                                addedCount = addedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, readCsvInfo.getAttrCode(), error, 0, result, addedMsg));
                            }
                        } else {
                            failedCount = failedCount + 1;
                        }
                    }
                }
            } catch (Exception e) {
                importResultResponse.setError(true);
                importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid");
                importResultResponse.setErrorMessage(msg);
                return importResultResponse;
            }
        } //end loop

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
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_MOLD_ATTRIBUTE);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD_ATTRIBUTE);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MOLD_ATTRIBUTE);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;

    }

    /**
     * 金型属性マスタ件数取得
     *
     * @param attrType
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("attrType") String attrType) {
        CountResponse count = mstMoldAttributeService.getMstMoldAttributeCount(attrType);
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
     * バッチで金型属性マスタデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extmoldattribute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldAttributeList getExtMoldAttributesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstMoldAttributeService.getExtMoldAttributesByBatch(latestExecutedDate);
    }
}
