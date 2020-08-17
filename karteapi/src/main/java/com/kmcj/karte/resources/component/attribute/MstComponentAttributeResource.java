/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.attribute;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtnList;
import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtnService;
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
 *
 * @author admin
 */
@RequestScoped
@Path("component/attribute")
public class MstComponentAttributeResource {

    public MstComponentAttributeResource() {
    }
    @Inject
    private MstComponentAttributeService mstComponentAttributeService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MstFileLinkPtnService mstFileLinkPtnService;

    /**
     * 部品属性マスタ複数取得(getComponentAttributes)
     *
     * @param componentType
     * @return an instance of MstCompanyList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentAttributeList getComponentAttributes(@QueryParam("componentType") String componentType) {
        return mstComponentAttributeService.getMstComponentAttributes(componentType);
    }
    
    /**
     * 部品属性マスタ複数取得(getComponentAttributes)
     * 部品詳細用
     * @param componentCode
     * @param componentType
     * @return an instance of MstCompanyList
     */
    @GET
    @Path("getattr")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentAttributeList getAttributes(@QueryParam("componentCode") String componentCode,@QueryParam("componentType") String componentType) {
        return mstComponentAttributeService.getAttributes(componentCode,componentType);
    }
    
    

    /**
     * 部品属性マスタ追加・更新・削除（postComponentAttributes)
     *
     * @param mstComponentAttributeList
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postComponentAttributes(List<MstComponentAttributes> mstComponentAttributeList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentAttributeService.getNewMstComponentAttributes(mstComponentAttributeList, loginUser);
    }

    /**
     * 部品属性マスタCSV出力
     *
     * @param componentType
     * @return an instance of MstComponentAttributeList
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getComponentAttributesCsv(@QueryParam("componentType") String componentType) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstComponentAttributeService.outAttrTypeOfChoice(loginUser.getLangId());
        mstComponentAttributeService.outComponentTypeOfChoice(loginUser.getLangId());
        return mstComponentAttributeService.getMstComponentAttributesOutputCsv(componentType,
                loginUser);
    }

    /**
     * 部品属性マスタCSV取込
     *
     * @param fileUuid
     * @return an instance of MstComponentAttributeList
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postComponentAttributesCsv(@PathParam("fileUuid") String fileUuid) {
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
                String componentType = mstDictionaryService.getDictionaryValue(userLangId, "component_type");
                String attrCode = mstDictionaryService.getDictionaryValue(userLangId, "component_attribute_code");
                String attrType = mstDictionaryService.getDictionaryValue(userLangId, "component_attribute_type");
                String fileLinkPtnName = mstDictionaryService.getDictionaryValue(userLangId, "file_link_ptn_name");

                String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
                String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
                String result = mstDictionaryService.getDictionaryValue(userLangId, "db_process");
                String addedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_added");
                String updatedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_data_modified");
                String deletedMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_record_deleted");
                String notFound = mstDictionaryService.getDictionaryValue(userLangId, "mst_error_record_not_found");
                String layout = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_wrong_csv_layout");
                //String canNotdeleteMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_cannot_delete_used_record");


                String seq = mstDictionaryService.getDictionaryValue(userLangId, "component_attribute_seq");
                String attrName = mstDictionaryService.getDictionaryValue(userLangId, "component_attribute_name");
                String nullMsg = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_null");
                String maxLangth = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_over_length");
                String notIsnumber = mstDictionaryService.getDictionaryValue(userLangId, "msg_error_not_isnumber");

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("seq", seq);
                logParm.put("componentType", componentType);
                logParm.put("attrCode", attrCode);
                logParm.put("attrName", attrName);
                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("notFound", notFound);
                logParm.put("maxLangth", maxLangth);
                logParm.put("notIsnumber", notIsnumber);

                Map inComponentTypeMap = mstComponentAttributeService.inComponentTypeOfChoice(loginUser.getLangId());
                Map insAttrTypeMap = mstComponentAttributeService.inAttrTypeOfChoice(loginUser.getLangId());

                MstComponentAttribute readCsvInfo;
                FileUtil fu = new FileUtil();
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);
                    if (comList.size() != 7) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, "", error, 1, errorContents, layout));
                        deletedCount = deletedCount + 1;
                        continue;
                    }

                    String[] csvArray = new String[comList.size()];
                    String strSeq = String.valueOf(comList.get(0));
                    csvArray[0] = strSeq;

                    //componentTypeのkeyを取得
                    String strComponentTypeVlue = String.valueOf(comList.get(1));
                    String strComponentTypeKey;
                    if ("".equals(strComponentTypeVlue.trim())) {
                        strComponentTypeKey = "0";
                    } else {
                        strComponentTypeKey = (String) inComponentTypeMap.get(strComponentTypeVlue);
                        if (null == strComponentTypeKey || "".equals(strComponentTypeKey)) {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, componentType, strComponentTypeVlue, error, 1, errorContents, notFound));
                            continue;
                        }
                    }

                    csvArray[1] = strComponentTypeKey;

                    String strAttrCode = String.valueOf(comList.get(2));
                    csvArray[2] = strAttrCode;
                    String strAttrName = String.valueOf(comList.get(3));
                    csvArray[3] = strAttrName;

                    //AttrTypeのkeyを取得
                    String strAttrType = String.valueOf(comList.get(4));
                    String strAttrTypeKey = (String) insAttrTypeMap.get(strAttrType);

                    if (fu.isNullCheck(strAttrTypeKey)) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrType, strAttrType, error, 1, errorContents, notFound));
                        continue;
                    }

                    csvArray[4] = strAttrTypeKey;

                    //MstFileLinkPtn　の　fileLinkPtnName判断
                    if ("5".equals(strAttrTypeKey) && !fu.isNullCheck(String.valueOf(comList.get(5)))) {
                        MstFileLinkPtnList mstFileLinkPtnList = mstFileLinkPtnService.getMstFileLinkPtnesByName(String.valueOf(comList.get(5)));
                        String strId;
                        if (mstFileLinkPtnList.getMstFileLinkPtnes().size() > 0) {
                            if (mstFileLinkPtnList.getMstFileLinkPtnes().get(0) != null) {
                                strId = mstFileLinkPtnList.getMstFileLinkPtnes().get(0).getId();
                                csvArray[5] = String.valueOf(strId);
                            } else {
                                //エラー情報をログファイルに記入
                                failedCount = failedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, fileLinkPtnName, String.valueOf(comList.get(5)), error, 1, errorContents, notFound));
                                continue;
                            }
                        }
                    } else {
                        csvArray[5] = "";
                    }

                    String strDelFlag = String.valueOf(comList.get(6));

                    if (strDelFlag != null && strDelFlag.trim().equals("1")) {
                        if (!mstComponentAttributeService.getMstComponentAttributeCheck(strAttrCode, Integer.parseInt(strComponentTypeKey))) {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, strAttrCode, error, 1, errorContents, notFound));
                        } else {
                            // FK依存関係チェック　True依存関係なし削除できる　False　削除できない
//                    if (!mstComponentAttributeService.getMstComponentAttributeFKCheck(strAttrCode,strComponentTypeKey)) {
                            mstComponentAttributeService.deleteMstComponentAttribute(strAttrCode, Integer.parseInt(strComponentTypeKey));
                            deletedCount = deletedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, strAttrCode, error, 1, result, deletedMsg));
//                    } else {
//                        //エラー情報をログファイルに記入
//                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, strAttrCode, error, 1, errorContents, canNotdeleteMsg));
//                        failedCount = failedCount + 1;
                        }
                    } else //check
                        if (mstComponentAttributeService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i)) {

                            String strfileLinkPtnName = String.valueOf(comList.get(5));
                            String strFileLinkPtnId = "";
                            if (strfileLinkPtnName != null && !"".equals(strfileLinkPtnName)) {
                                strFileLinkPtnId = mstFileLinkPtnService.getFileLinkPtnIdByName(strfileLinkPtnName);
                                if ("".equals(strFileLinkPtnId)) {
                                    failedCount = failedCount + 1;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, fileLinkPtnName, strfileLinkPtnName, error, 1, errorContents, notFound));
                                    continue;
                                }
                            }

                            readCsvInfo = new MstComponentAttribute();
                            readCsvInfo.setSeq(Integer.parseInt(strSeq));
                            MstComponentAttributePK mstComponentAttributePK = new MstComponentAttributePK();
                            mstComponentAttributePK.setAttrCode(strAttrCode);
                            mstComponentAttributePK.setComponentType(Integer.parseInt(strComponentTypeKey));
                            readCsvInfo.setMstComponentAttributePK(mstComponentAttributePK);
                            readCsvInfo.setAttrName(strAttrName);
                            readCsvInfo.setAttrType(Integer.parseInt(strAttrTypeKey));
                            if (!"".equals(strFileLinkPtnId)) {
                                readCsvInfo.setFileLinkPtnId(strFileLinkPtnId);
                            }

                            String getAttrCode = readCsvInfo.getMstComponentAttributePK().getAttrCode();
                            int getComponentType = readCsvInfo.getMstComponentAttributePK().getComponentType();

                            if (mstComponentAttributeService.getMstComponentAttributeCheck(getAttrCode, getComponentType)) {
                                //存在チェックの場合　//更新
                                // データを更新
                                Date sysDate = new Date();
                                readCsvInfo.setUpdateDate(sysDate);
                                readCsvInfo.setUpdateUserUuid(loginUser.getUserUuid());
                                int count = mstComponentAttributeService.updateMstComponentAttributeByQuery(readCsvInfo);
                                updatedCount = updatedCount + count;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, readCsvInfo.getMstComponentAttributePK().getAttrCode(), error, 0, result, updatedMsg));
                            } else {
                                //追加
                                Date sysDate = new Date();
                                readCsvInfo.setId(IDGenerator.generate());
                                readCsvInfo.setCreateDate(sysDate);
                                readCsvInfo.setCreateUserUuid(loginUser.getUserUuid());
                                mstComponentAttributeService.createMstComponentAttribute(readCsvInfo);
                                addedCount = addedCount + 1;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, attrCode, readCsvInfo.getMstComponentAttributePK().getAttrCode(), error, 0, result, addedMsg));
                            }
                        } else {
                            failedCount = failedCount + 1;
                        }
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
            tblCsvImport.setImportTable(CommonConstants.TBL_MST_COMPONENT_ATTRIBUTE);
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(fileUuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_COMPONENT_CATTRIBUTE);
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount(readList.size() - 1);
            tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
            tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
            tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
            tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
            tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
            tblCsvImport.setLogFileUuid(logFileUuid);

            String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_COMPONENT_ATTRIBUTE);
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
}
