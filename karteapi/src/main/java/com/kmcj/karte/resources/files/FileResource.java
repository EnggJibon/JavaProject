/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.files;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.properties.StockAppPropertyService;
import com.kmcj.karte.resources.authctrl.MstAuthCtrlService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.function.MstFunction;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * REST Web Service
 *
 * @author admin
 */
@RequestScoped
@Path("files")
public class FileResource {

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblUploadFileService tblUploadFileService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Context
    private ContainerRequestContext requestContext;
    
    @Context
    private ServletContext context;

    @Inject
    private MstAuthCtrlService mstAuthCtrlService;

    @Inject
    private TblCsvImportService tblCsvImportService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private StockAppPropertyService stockAppPropertyService;
    
    private static String CSV_TYPE = "EFBBBF22";
    private static String EXCEL_TYPE1 = "504B0304";
    private static String EXCEL_TYPE2 = "D0CF11E0";

    /**
     * Creates a new instance of FileResource
     */
    public FileResource() {
    }

    /**
     *
     * @param uploadFile
     * @param uploadFileDetail
     * @param fileType
     * @param functionId
     * @return
     */
    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse uploadFile(@FormDataParam("upfile") InputStream uploadFile,
            @FormDataParam("upfile") FormDataContentDisposition uploadFileDetail,
            @FormDataParam("fileType") String fileType,
            @FormDataParam("functionId") String functionId) {
        StringBuffer path = new StringBuffer(kartePropertyService.getDocumentDirectory());
        FileReponse response = new FileReponse();
        
        if (CommonConstants.CSV.equalsIgnoreCase(fileType)) {
            path = path.append(FileUtil.SEPARATOR).append(CommonConstants.CSV);
        } else if (CommonConstants.IMAGE.equalsIgnoreCase(fileType)) {
            path = path.append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE);
        } else if (CommonConstants.VIDEO.equalsIgnoreCase(fileType)) {
            path = path.append(FileUtil.SEPARATOR).append(CommonConstants.VIDEO);
        } else if (CommonConstants.DOC.equalsIgnoreCase(fileType)) {
            path = path.append(FileUtil.SEPARATOR).append(CommonConstants.DOC);          
        } else if (CommonConstants.EXCEL_FILE.equalsIgnoreCase(fileType)) {
            path = path.append(FileUtil.SEPARATOR).append(CommonConstants.EXCEL_FILE);   
        } else if (CommonConstants.WORK.equalsIgnoreCase(fileType)) {
            path = path.append(FileUtil.SEPARATOR).append(CommonConstants.WORK);   
        }else {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid"));
            return response;
        }
       
        String uuid = IDGenerator.generate();
        String getFileType = uploadFileDetail.getFileName();
        getFileType = getFileType.substring(getFileType.lastIndexOf(".") + 1);
        FileUtil fu = new FileUtil();

        fu.createPath(path.toString());
        path = path.append(FileUtil.SEPARATOR).append(uuid).append(".").append(getFileType);
                   
        fu.createFile(path.toString());
        fu.writeToFile(uploadFile, path.toString());
        
//        try {
//            InputStream chkInputStream1 = new FileInputStream(path.toString());
//            InputStream chkInputStream2 = new FileInputStream(path.toString());
//            
//            if (CommonConstants.CSV.equalsIgnoreCase(fileType)) {
//                
//                chkFileType = FileUtil.judgeFileType(chkInputStream1, CSV_TYPE);
//            }else if(CommonConstants.EXCEL_FILE.equalsIgnoreCase(fileType)){
//                
//                if(FileUtil.judgeFileType(chkInputStream1, EXCEL_TYPE1) || FileUtil.judgeFileType(chkInputStream2, EXCEL_TYPE2)){
//                    
//                    chkFileType = true;
//                }else {
//                    chkFileType = false;
//                }
//            }
//            if(!chkFileType){
//                LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
//                response.setError(true);
//                response.setErrorCode(ErrorMessages.E201_APPLICATION);
//                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid"));
//                return response;
//            }
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        Date uploadDate = new Date();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        loginUser.getAuthId();
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileType(fileType);
        tblUploadFile.setFileUuid(uuid);
        tblUploadFile.setUploadFileName(FileUtil.getValueByUTF8(uploadFileDetail.getFileName()));
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(functionId);
        tblUploadFile.setFunctionId(mstFunction);
        tblUploadFile.setUploadDate(uploadDate);
        tblUploadFile.setUploadUserUuid(loginUser.getUserUuid());
        tblUploadFileService.createTblUploadFile(tblUploadFile);

        response.setFileUuid(uuid);
        response.setUploadDate(uploadDate);
        return response;
    }
    
    /**
     * 複数
     * @param uploadFiles
     * @param fileType
     * @param functionId
     * @return 
     */
    @POST
    @Path("uploads")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse complexUploadFile(@FormDataParam("upfile") List<FormDataBodyPart> uploadFiles,
            @FormDataParam("fileType") String fileType,
            @FormDataParam("functionId") String functionId) {

        String fileUuid = "";
        InputStream uploadFile;
        FileReponse response = new FileReponse();
        FormDataContentDisposition uploadFileDetail;
        if (uploadFiles != null) {
            for (FormDataBodyPart part : uploadFiles) {
                StringBuffer path = new StringBuffer(kartePropertyService.getDocumentDirectory());
                if (CommonConstants.CSV.equalsIgnoreCase(fileType)) {
                    path = path.append(FileUtil.SEPARATOR).append(CommonConstants.CSV);
                } else if (CommonConstants.IMAGE.equalsIgnoreCase(fileType)) {
                    path = path.append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE);
                } else if (CommonConstants.VIDEO.equalsIgnoreCase(fileType)) {
                    path = path.append(FileUtil.SEPARATOR).append(CommonConstants.VIDEO);
                } else if (CommonConstants.DOC.equalsIgnoreCase(fileType)) {
                    path = path.append(FileUtil.SEPARATOR).append(CommonConstants.DOC);
                } else if (CommonConstants.EXCEL_FILE.equalsIgnoreCase(fileType)) {
                    path = path.append(FileUtil.SEPARATOR).append(CommonConstants.EXCEL_FILE);
                } else {
                    LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid"));
                    return response;
                }

                uploadFile = part.getValueAs(InputStream.class);
                uploadFileDetail = part.getFormDataContentDisposition();
                String uuid = IDGenerator.generate();
                String getFileType = uploadFileDetail.getFileName();
                getFileType = getFileType.substring(getFileType.lastIndexOf(".") + 1);
                FileUtil fu = new FileUtil();

                fu.createPath(path.toString());
                path = path.append(FileUtil.SEPARATOR).append(uuid).append(".").append(getFileType);

                fu.createFile(path.toString());
                fu.writeToFile(uploadFile, path.toString());
                LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
                loginUser.getAuthId();
                TblUploadFile tblUploadFile = new TblUploadFile();
                tblUploadFile.setFileType(fileType);
                tblUploadFile.setFileUuid(uuid);
                tblUploadFile.setUploadFileName(FileUtil.getValueByUTF8(uploadFileDetail.getFileName()));
                MstFunction mstFunction = new MstFunction();
                mstFunction.setId(functionId);
                tblUploadFile.setFunctionId(mstFunction);
                tblUploadFile.setUploadDate(new Date());
                tblUploadFile.setUploadUserUuid(loginUser.getUserUuid());
                tblUploadFileService.createTblUploadFile(tblUploadFile);
                fileUuid += uuid + ",";
            }
        }
        if (StringUtils.isNotEmpty(fileUuid)) {
            fileUuid = fileUuid.substring(0, fileUuid.length() - 1);
        }
        response.setFileUuid(fileUuid);
        return response;
    }

    @GET
    @Path("checkmoduleversion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse checkModuleVersion(@QueryParam("version") String version) {
        BasicResponse response = new BasicResponse();
        StringBuilder filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        filePath.append(FileUtil.SEPARATOR).append(CommonConstants.MODULE);
        filePath.append(FileUtil.SEPARATOR).append("moduleversion");
        FileReader fr = null;
        BufferedReader br = null;
        try {
            File file = new File(filePath.toString());
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String ver = br.readLine();
            if (ver != null && version != null && !ver.equals(version)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(null, "msg_warning_module_version"));
            }
        }
        catch (FileNotFoundException nf) {
            
        }
        catch (IOException ie) {
            
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException ie) {}
            }
            if (fr != null) {
                try {
                    fr.close();
                }
                catch (IOException io) {}
            }
        }
        return response;
    }
    
    
    @GET
    @Path("downloadguide/{guideFileName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadGuide(@PathParam("guideFileName") String guideFileName) {
        StringBuilder filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        filePath.append(FileUtil.SEPARATOR).append(CommonConstants.GUIDE);
        filePath.append(FileUtil.SEPARATOR).append(guideFileName);
        File file = new File(filePath.toString());
        Response.ResponseBuilder response = Response.ok(file);
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + FileUtil.getEncod(file.getName()) + "\"");
        return response.build();
    }
    
    
    @GET
    @Path("downloadmodule")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadModule() {
        StringBuilder filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        filePath.append(FileUtil.SEPARATOR).append(CommonConstants.MODULE);
        filePath.append(FileUtil.SEPARATOR).append("Module.zip");
        File file = new File(filePath.toString());
        Response.ResponseBuilder response = Response.ok(file);
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + FileUtil.getEncod(file.getName()) + "\"");
        return response.build();
    }

    /**
     * 認証ダイアログしてから、レポートCSVファイルをダウンロードする
     * <P>
     *
     *
     * @param fileUuid
     * @return
     */
    @GET
    @Path("download/custom/report/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getReportQueryDownload(
            @PathParam("fileUuid") String fileUuid) {
        StringBuilder fullPath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.CSV);
        String fielName = tblCsvExportService.getTblCsvExportFileNameByUuid(fileUuid);

        File file = new File(fullPath.toString());
        FileUtil fu = new FileUtil();
        String filePath = fu.loadFileByFileName(file, fileUuid);
        file = new File(filePath);
        Response.ResponseBuilder response = Response.ok(file);
        String encodeStr = FileUtil.getEncod(fielName);
        if (encodeStr != null) {
            encodeStr = encodeStr.replace("+", "%20");
        }
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeStr + "\"");
        return response.build();
    }


    @GET
    @Path("downloadmodule2")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadModule2() {
        StringBuilder filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        filePath.append(FileUtil.SEPARATOR).append(CommonConstants.MODULE);
        filePath.append(FileUtil.SEPARATOR).append("Module2.zip");
        File file = new File(filePath.toString());
        Response.ResponseBuilder response = Response.ok(file);
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + FileUtil.getEncod(file.getName()) + "\"");
        return response.build();
    }
    

    /**
     *
     * @param moduleFileName
     * @return
     */
    @GET
    @Path("downloadprogram/{moduleFileName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadProgram(@PathParam("moduleFileName") String moduleFileName) {
        StringBuilder filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        filePath.append(FileUtil.SEPARATOR).append(CommonConstants.MODULE);
        filePath.append(FileUtil.SEPARATOR).append(moduleFileName);
        File file = new File(filePath.toString());
        Response.ResponseBuilder response = Response.ok(file);
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + FileUtil.getEncod(file.getName()) + "\"");
        return response.build();
    }
    
    
    /**
     *
     * @param uuid
     * @param fileType
     * @return
     */
    @GET
    @Path("download/{fileType}/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("fileUuid") String uuid,
            @PathParam("fileType") String fileType) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(uuid);
        boolean flag = true;
        if (tblUploadFile != null) {
            flag = mstAuthCtrlService.isAuthorizedFunction(loginUser.getAuthId(), tblUploadFile.getFunctionId().getId());
        }
        // 権限あるかどうか判断
        if (flag) {
            String fielName = "";
            StringBuffer fullPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
            if (CommonConstants.CSV.equalsIgnoreCase(fileType)) {
                fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.CSV);
                fielName = tblCsvExportService.getTblCsvExportFileNameByUuid(uuid);

            } else if (CommonConstants.DOC.equalsIgnoreCase(fileType)) {
                fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.DOC);
                if (tblUploadFile != null) {
                    fielName = tblUploadFile.getUploadFileName();
                }else {
                    
                    fielName = tblCsvExportService.getTblCsvExportFileNameByUuid(uuid);
                }

            } else if(CommonConstants.EXCEL_FILE.equalsIgnoreCase(fileType)){
                
                fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.EXCEL_FILE);
                if (tblUploadFile != null) {
                    fielName = tblUploadFile.getUploadFileName();
                }else {
                    
                    fielName = tblCsvExportService.getTblCsvExportFileNameByUuid(uuid);
                }
                
            }else if (CommonConstants.LOG.equalsIgnoreCase(fileType)) {
            
                fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.LOG);
                fielName = tblCsvImportService.getLogFileNameByUuid(uuid);
            }

            File file = new File(fullPath.toString());
            FileUtil fu = new FileUtil();
            String filePath = fu.loadFileByFileName(file, uuid);
            file = new File(filePath);
            Response.ResponseBuilder response = Response.ok(file);
            String encodeStr = FileUtil.getEncod(fielName);
            if (encodeStr != null) {
                encodeStr = encodeStr.replace("+", "%20");
            }
            response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeStr + "\"");
            return response.build();
        } else {
            // 権限なし　エラーコード403をリターンする
            Response.ResponseBuilder response = Response.status(403);
            return response.build();
        }
    }
    
    /**
     *
     * @param inventoryId
     * @param fileName
     * @return
     */
    @GET
    @Path("download/excel/{id}/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadInventoryFile(@PathParam("id") String inventoryId,
            @PathParam("fileUuid") String fileName) {
        StringBuilder fullPath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.WORK)
            .append(FileUtil.SEPARATOR).append(CommonConstants.INVENTORY_REQUEST)
            .append(FileUtil.SEPARATOR).append(inventoryId).append(FileUtil.SEPARATOR);
        //flag = mstAuthCtrlService.isAuthorizedFunction(loginUser.getAuthId(), tblUploadFile.getFunctionId().getId());
        File file = new File(fullPath.toString());
        FileUtil fu = new FileUtil();
        String filePath = fu.loadFileByFileName(file, FileUtil.getDecode(fileName));
        file = new File(filePath);
        Response.ResponseBuilder response = Response.ok(file);
        String encodeStr = FileUtil.getEncod(FileUtil.getDecode(fileName));
        if (encodeStr != null) {
            encodeStr = encodeStr.replace("+", "%20");
        }
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeStr + "\"");
        return response.build();
    }
    
    /**
     * 
     * @param fileUuid
     * @param fileName
     * @return 
     */
    @GET
    @Path("download/zip/{fileType}/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadZipFile(@PathParam("fileUuid") String fileUuid,
            @PathParam("fileType") String fileName) {
        StringBuilder fullPath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.WORK)
            .append(FileUtil.SEPARATOR).append(CommonConstants.INSPECTION_REPORT)
            .append(FileUtil.SEPARATOR).append(fileUuid).append(FileUtil.SEPARATOR);
        File file = new File(fullPath.toString());
        FileUtil fu = new FileUtil();
        String filePath = fu.loadFileByFileName(file, FileUtil.getDecode(fileUuid));
        file = new File(filePath);
        Response.ResponseBuilder response = Response.ok(file);
        String encodeStr = FileUtil.getEncod(FileUtil.getDecode(fileName));
        if (encodeStr != null) {
            encodeStr = encodeStr.replace("+", "%20");
        }
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeStr + ".zip\"");
        return response.build();
    }

    /**
     *
     * @param uuid
     * @param fileType
     * @return
     */
    @GET
    @Path("downloadImageVideo/{fileType}/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadImageVideo(@PathParam("fileUuid") String uuid,
            @PathParam("fileType") String fileType) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(uuid);
        boolean flag = true;
        if (tblUploadFile != null) {
            flag = mstAuthCtrlService.isAuthorizedFunction(loginUser.getAuthId(), tblUploadFile.getFunctionId().getId());
        }
        // 権限あるかどうか判断
        if (flag) {
            StringBuffer fullPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
            if (CommonConstants.IMAGE.equalsIgnoreCase(fileType)) {
                fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE);

            } else if (CommonConstants.VIDEO.equalsIgnoreCase(fileType)) {
                fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.VIDEO);
            }

            File file = new File(fullPath.toString());
            FileUtil fu = new FileUtil();

            String filePath = fu.loadFileByFileName(file, uuid);
            file = new File(filePath);
            
            String mimeType = context.getMimeType(filePath);
            Response.ResponseBuilder response = Response.ok(file);
            if(mimeType == null){
                mimeType = "application/octet-stream";
            }      
            response.header(CommonConstants.CONTENT_TYPE, mimeType);
            return response.build();
        } else {
            // 権限なし　エラーコード403をリターンする
            Response.ResponseBuilder response = Response.status(403);
            return response.build();
        }
    }
    
    /**
     * バッチでFileデータを取得
     *
     * @param uuid
     * @param fileType
     * @return
     */
    @GET
    @Path("extfiledownload")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public File extDownloadFile(@QueryParam("uuid") String uuid,@QueryParam("fileType") String fileType) {
        StringBuffer fullPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
        if (CommonConstants.IMAGE.equalsIgnoreCase(fileType)) {
            fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE);

        } else if (CommonConstants.DOC.equalsIgnoreCase(fileType)) {
            fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.DOC);

        } else if (CommonConstants.VIDEO.equalsIgnoreCase(fileType)) {
            fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.VIDEO);
        }
        File file = new File(fullPath.toString());
        FileUtil fu = new FileUtil();
        String filePath = fu.loadFileByFileName(file, uuid);
        file = new File(filePath);
        return file;
    }
    
    /**
     * バッチでFileデータを取得
     *
     * @param uuid
     * @param fileType
     * @return
     */
    @GET
    @Path("extfilename")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String extGetFileName(@QueryParam("uuid") String uuid,@QueryParam("fileType") String fileType) {
        StringBuffer fullPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
        if (CommonConstants.IMAGE.equalsIgnoreCase(fileType)) {
            fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE);

        } else if (CommonConstants.DOC.equalsIgnoreCase(fileType)) {
            fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.DOC);

        } else if (CommonConstants.VIDEO.equalsIgnoreCase(fileType)) {
            fullPath = fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.VIDEO);
        }
        File file = new File(fullPath.toString());
        FileUtil fu = new FileUtil();
        String filePath = fu.loadFileByFileName(file, uuid);
        file = new File(filePath);
        return file.getName();
    }
    
    
    /**
     * バッチでUploadFileデータを取得
     * @param docIds
     * @return
     */
    @GET
    @Path("extfileuploads")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblUploadFileList extGetUploadFiles(@QueryParam("docIds") String docIds) {
        return tblUploadFileService.getExtUploadFilesByBatch(docIds);
    }
    
    /**
     *
     * @param tblUploadFileList
     * @return
     * @throws FileNotFoundException
     */
//    @POST
//    @Path("extfileupload")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public FileReponse extUploadFile(List<TblUploadFile> tblUploadFileList) {
//
//        FileReponse response = new FileReponse();
//
//        if (tblUploadFileList.size() > 0) {
//            
//            TblUploadFile paraTblUpLoadFile = tblUploadFileList.get(0);
//
//            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
//            loginUser.getAuthId();
//            TblUploadFile tblUploadFile = new TblUploadFile();
//            tblUploadFile.setFileType(paraTblUpLoadFile.getFileType());
//            tblUploadFile.setFileUuid(paraTblUpLoadFile.getFileUuid());
//            tblUploadFile.setUploadFileName(FileUtil.getValueByUTF8(paraTblUpLoadFile.getUploadFileName()));
//            MstFunction mstFunction = new MstFunction();
//            // 部品入荷登録画面権限を設定
//            mstFunction.setId("30501");
//            tblUploadFile.setFunctionId(mstFunction);
//            tblUploadFile.setUploadDate(new Date());
//            tblUploadFile.setUploadUserUuid(loginUser.getUserUuid());
//            tblUploadFileService.createTblUploadFile(tblUploadFile);
//
//            response.setFileUuid(paraTblUpLoadFile.getFileUuid());
//        }
//        return response;
//    }
    
    /**
     * バッチでUploadFileデータを取得
     * 
     * @param fileUuid
     * @return
     */
    @GET
    @Path("getextuploadfile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblUploadFile getextUploadFile(@QueryParam("fileUuid") String fileUuid) {
        return tblUploadFileService.getTblUploadFile(fileUuid);
    }

    /**
     *
     * @param uploadFile
     * @param fileUuid
     * @param uploadFileName
     * @param fileType
     * @param functionId
     * @return
     */
    @POST
    @Path("extfileupload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse extUploadFile(@FormDataParam("uploadFile") InputStream uploadFile,
            @FormDataParam("fileUuid") String fileUuid, @FormDataParam("uploadFileName") String uploadFileName,
            @FormDataParam("fileType") String fileType, @FormDataParam("functionId") String functionId) {

        StringBuffer path = new StringBuffer(kartePropertyService.getDocumentDirectory());
        FileReponse response = new FileReponse();

        TblUploadFile tblUploadFileOld = tblUploadFileService.getTblUploadFile(fileUuid);

        if (null == tblUploadFileOld) {

            if (CommonConstants.CSV.equalsIgnoreCase(fileType)) {
                path = path.append(FileUtil.SEPARATOR).append(CommonConstants.CSV);
            } else if (CommonConstants.IMAGE.equalsIgnoreCase(fileType)) {
                path = path.append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE);
            } else if (CommonConstants.VIDEO.equalsIgnoreCase(fileType)) {
                path = path.append(FileUtil.SEPARATOR).append(CommonConstants.VIDEO);
            } else if (CommonConstants.DOC.equalsIgnoreCase(fileType)) {
                path = path.append(FileUtil.SEPARATOR).append(CommonConstants.DOC);
            } else if (CommonConstants.EXCEL_FILE.equalsIgnoreCase(fileType)) {
                path = path.append(FileUtil.SEPARATOR).append(CommonConstants.EXCEL_FILE);
            } else {
                LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(
                        mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid"));
                return response;
            }

            String getFileType = uploadFileName;
            getFileType = getFileType.substring(getFileType.lastIndexOf(".") + 1);
            FileUtil fu = new FileUtil();

            fu.createPath(path.toString());
            path = path.append(FileUtil.SEPARATOR).append(fileUuid).append(".").append(getFileType);

            fu.createFile(path.toString());
            fu.writeToFile(uploadFile, path.toString());

            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            loginUser.getAuthId();

            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileType(fileType);
            tblUploadFile.setFileUuid(fileUuid);
            tblUploadFile.setUploadFileName(FileUtil.getDecode(uploadFileName));
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(functionId);
            tblUploadFile.setFunctionId(mstFunction);
            tblUploadFile.setUploadDate(new Date());
            tblUploadFile.setUploadUserUuid(loginUser.getUserUuid());
            tblUploadFileService.createTblUploadFile(tblUploadFile);
        }

        response.setFileUuid(fileUuid);
        return response;
   }

    /**
     * ネイティブアプリバージョン取得
     *
     * @param ostype
     * @return
     */
    @GET
    @Path("stocktake/appversion/{ostype}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getNativeApplicationVersion(
            @PathParam("ostype") String ostype
    ) {

        FileReponse response = new FileReponse();
        String ver;
        try {

            ver = stockAppPropertyService.getVersion(ostype);

            if (StringUtils.isEmpty(ver)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(null, "msg_error_value_invalid"));
                return response;
            }

        } catch (Exception e) {

            response.setError(true);
            response.setErrorCode(ErrorMessages.E901_OTHER);
            response.setErrorMessage(e.getMessage());
            return response;
        }
        response.setVersion(ver);

        return response;
    }

    /**
     * 検査管理項目のzipファイルを出力
     * 
     * @param fileUuid
     * @return
     */
    @GET
    @Path("download/item/zip/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadItemZipFile(@PathParam("fileUuid") String fileUuid) {
        StringBuilder fullPath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        fullPath.append(FileUtil.SEPARATOR).append(CommonConstants.CSV)
                .append(FileUtil.SEPARATOR).append(fileUuid)
                .append(FileUtil.SEPARATOR);
        File file = new File(fullPath.toString());
        FileUtil fu = new FileUtil();
        String filePath = fu.loadFileByFileName(file, FileUtil.getDecode(fileUuid));
        file = new File(filePath);
        Response.ResponseBuilder response = Response.ok(file);
        String encodeStr = FileUtil.getEncod(FileUtil.getDecode("InspectItems"));
        if (encodeStr != null) {
            encodeStr = encodeStr.replace("+", "%20");
        }
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeStr + ".zip\"");
        return response.build();
    }
}
