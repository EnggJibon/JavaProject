package com.kmcj.karte.resources.machine.attribute;

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
 * 設備属性マスタの処理
 *
 * @author admin
 */
@RequestScoped
@Path("machine/attribute")
public class MstMachineAttributeResource {

    public MstMachineAttributeResource() {
    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstMachineAttributeService mstMachineAttributeService;

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
     * すべての設備属性マスターを取得する
     *
     * @param machineType
     * @return an instance of MstMachinetAttributeList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineAttributeList getMachineAttributes(@QueryParam("machineType") String machineType) {
        MstMachineAttributeList mstMachineAttributeList = mstMachineAttributeService.getMstMachineAttributes(machineType);
        MstMachineAttributeList resMachineAttributeList = new MstMachineAttributeList();
        List<MstMachineAttributeVo> mstMachineAttributeVos = new ArrayList<>();
        MstMachineAttributeVo aResVo;

        for (int i = 0; i < mstMachineAttributeList.getMstMachineAttributes().size(); i++) {
            MstMachineAttribute mstMachineAttribute = mstMachineAttributeList.getMstMachineAttributes().get(i);
            aResVo = new MstMachineAttributeVo();
            aResVo.setId(mstMachineAttribute.getId());
            aResVo.setSeq(mstMachineAttribute.getSeq());
            aResVo.setAttrCode(mstMachineAttribute.getAttrCode());
            aResVo.setAttrName(mstMachineAttribute.getAttrName());
            aResVo.setAttrType(mstMachineAttribute.getAttrType());
            aResVo.setFileLinkPtnId(mstMachineAttribute.getFileLinkPtnId() == null ? "" : mstMachineAttribute.getFileLinkPtnId());
            if (mstMachineAttribute.getFileLinkPtnId() != null) {
                aResVo.setFileLinkPtnName(mstMachineAttribute.getMstFileLinkPtn().getFileLinkPtnName());
                aResVo.setLinkString(mstMachineAttribute.getMstFileLinkPtn().getLinkString());
            } else {
                aResVo.setFileLinkPtnName("");
                aResVo.setLinkString("");
            }

            mstMachineAttributeVos.add(aResVo);
        }
        resMachineAttributeList.setMstMachineAttributeVos(mstMachineAttributeVos);
        return resMachineAttributeList;
    }

    /**
     * 設備詳細画面で設備仕様を取得　設備Type
     *
     * @param machineType
     * @param machineId
     * @param machineSpecId
     * @param machineSpecName
     * @param externalFlg
     * @return an instance of MstMachinetAttributeList
     */
    @GET
    @Path("type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineAttributeVo getMachineAttributesByType(@QueryParam("machineType") String machineType, @QueryParam("machineId") String machineId,
            @QueryParam("machineSpecId") String machineSpecId, @QueryParam("machineSpecName") String machineSpecName,
            @QueryParam("externalFlg") String externalFlg) {
        MstMachineAttributeVo response = new MstMachineAttributeVo();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
//        if (machineId != null && !"".equals(machineId)) {
//            FileUtil fu = new FileUtil();
//            if (!fu.checkString(machineId)) {
//                response.setError(true);
//                response.setErrorCode(ErrorMessages.E201_APPLICATION);
//                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid"));
//                return response;
//            }
//        }
        //return mstMachineAttributeService.getMstMachineAttributesByType(machineType,FileUtil.getDecode(machineId),machineSpecName,loginUser,externalFlg);
        response = mstMachineAttributeService.getMstMachineAttributesByTypeAndSpecHistoryId(machineType, FileUtil.getDecode(machineId), machineSpecId, machineSpecName, loginUser, externalFlg);
        return response;
    }

    /**
     *
     * 設備属性マスタ追加・更新・削除
     *
     * @param mstMachineAttributesList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMachineAttributes(List<MstMachineAttributeVo> mstMachineAttributesList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMachineAttributeService.getNewMstMachineAttributes(mstMachineAttributesList, loginUser);

    }

    /**
     * 設備属性マスタCSV出力
     *
     * @param machineType
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMachineAttributesCSV(@QueryParam("machineType") String machineType) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstMachineAttributeService.outMachineTypeOfChoice(loginUser.getLangId());
        mstMachineAttributeService.outAttrTypeOfChoice(loginUser.getLangId());
        return mstMachineAttributeService.getMstMachineAttributesOutputCsv(machineType, loginUser);
    }

    /**
     * 設備属性マスタCSV取込
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMachineAttributesCSV(@PathParam("fileUuid") String fileUuid) {
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
                String seq = mstDictionaryService.getDictionaryValue(userLangId, "machine_attribute_seq");
                String machineType = mstDictionaryService.getDictionaryValue(userLangId, "machine_type");
                String attrCode = mstDictionaryService.getDictionaryValue(userLangId, "machine_attribute_code");
                String fileLinkPtnName = mstDictionaryService.getDictionaryValue(userLangId, "file_link_ptn_name");
                String machineAttributeType = mstDictionaryService.getDictionaryValue(userLangId, "machine_attribute_type");

                String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
                String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
                String result = mstDictionaryService.getDictionaryValue(userLangId, "db_process");

                String addedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_added");
                String updatedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_data_modified");
                String deletedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_deleted");
                //String canNotdeleteMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_cannot_delete_used_record");
                String notFound = mstDictionaryService.getDictionaryValue(userLangId, "mst_error_record_not_found");
                String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");

                String attrName = mstDictionaryService.getDictionaryValue(userLangId, "machine_attribute_name");
                String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
                String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");
                String notIsnumber = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_isnumber");
                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("seq", seq);
                logParm.put("machineType", machineType);
                logParm.put("attrCode", attrCode);
                logParm.put("attrName", attrName);
                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("notFound", notFound);
                logParm.put("maxLangth", maxLangth);
                logParm.put("notIsnumber", notIsnumber);

                Map inMachineTypeMap = mstMachineAttributeService.inMachineTypeOfChoice(loginUser.getLangId());
                Map insAttrTypeMap = mstMachineAttributeService.inAttrTypeOfChoice(loginUser.getLangId());

                MstMachineAttribute readCsvInfo;
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

                    //machineTypeのkeyを
                    String strMachineType = String.valueOf(comList.get(1));
                    String strMachineTypeKey = (String) inMachineTypeMap.get(strMachineType);
                    if ("".equals(strMachineType.trim())) {
                        strMachineTypeKey = "0";
                    } else if (null == strMachineTypeKey || "".equals(strMachineTypeKey)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, machineType, strMachineType, error, 1, errorContents, notFound));
                        continue;
                    }

                    csvArray[1] = strMachineTypeKey;

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
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, machineAttributeType, strAttrType, error, 1, errorContents, notFound));
                        continue;
                    }

                    csvArray[4] = strAttrTypeKey;

                    csvArray[5] = String.valueOf(comList.get(5));

                    String strDelFlag = String.valueOf(comList.get(6));

                    Query query = entityManager.createNamedQuery("MstMachineAttribute.findByAttrCode");
                    query.setParameter("attrCode", strAttrCode);
                    query.setParameter("machineType", Integer.parseInt(strMachineTypeKey));
                    query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                    String id = "";
                    try {
                        MstMachineAttribute mstMachineAttribute = (MstMachineAttribute) query.getSingleResult();
                        id = mstMachineAttribute.getId();
                    } catch (NoResultException e) {
                        id = "";
                    }
                    //delFlag 指定されている場合 
                    if (strDelFlag != null && strDelFlag.trim().equals("1")) {
                        if (!"".equals(id)) {
                            //  if (mstMachineAttributeService.getMstMachineAttributeCheck(strAttrCode,Integer.parseInt(strMachineTypeKey))) {
                            // FK　関連チェック 
//                        if (!mstMachineAttributeService.getMstMachineAttributeFKCheck(id)) {
                            mstMachineAttributeService.deleteMstMachineAttribute(id);
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
                     if (mstMachineAttributeService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i)) {

                            readCsvInfo = new MstMachineAttribute();
                            String strfileLinkPtnName = String.valueOf(comList.get(5));
                            String strFileLinkPtnId = "";
                            if ("5".equals(strAttrTypeKey)) {
                                if (strfileLinkPtnName != null && !"".equals(strfileLinkPtnName)) {
                                    strFileLinkPtnId = mstMachineAttributeService.getFileLinkPtnIdByName(strfileLinkPtnName);

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
                            readCsvInfo.setMachineType(Integer.parseInt(strMachineTypeKey));
                            readCsvInfo.setAttrName(strAttrName);
                            readCsvInfo.setAttrType(Integer.parseInt(strAttrTypeKey));
                            if (!"".equals(id)) {
                                //if (mstMachineAttributeService.getMstMachineAttributeCheck(strAttrCode,Integer.parseInt(strMachineTypeKey))) {
                                //存在チェックの場合　//更新
                                // データを更新
                                Date sysDate = new Date();
                                readCsvInfo.setId(id);
                                readCsvInfo.setUpdateDate(sysDate);
                                readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                int count = mstMachineAttributeService.updateMstMachineAttributeByQuery(readCsvInfo);
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
                                mstMachineAttributeService.createMstMachineAttribute(readCsvInfo);
                                addedCount = addedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, readCsvInfo.getAttrCode(), error, 0, result, addedMsg));
                            }
                        } else {
                            failedCount = failedCount + 1;
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
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_MACHINE_ATTRIBUTE);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE_ATTRIBUTE);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MACHINE_ATTRIBUTE);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;

    }

    /**
     * 設備属性マスタ件数取得
     *
     * @param attrType
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("attrType") String attrType) {
        CountResponse count = mstMachineAttributeService.getMstMachineAttributeCount(attrType);
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
     * バッチで設備属性マスタデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extmachineattribute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineAttributeList getExtMachineAttributesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstMachineAttributeService.getExtMachineAttributesByBatch(latestExecutedDate);
    }
}
