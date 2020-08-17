/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

import com.google.gson.Gson;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.ObjectResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * REST Web Service
 *
 * @author admin
 */
@Path("customreport")
@RequestScoped
public class TblCustomReportQueryResource {

    @Context
    private UriInfo context;

    @Inject
    private TblCustomReportQueryService tblCustomReportQueryService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblUploadFileService tblUploadFileService;

    /**
     * Creates a new instance of MstDictionaryResource
     */
    public TblCustomReportQueryResource() {
    }
    
        /**
     * カスタムレポートクエリ一覧を取得用
     *
     * @return
     */
    @GET
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblCustomReportQueryVoList getCustomReportQueryList() {
        return tblCustomReportQueryService.getCustomReportQueryList();
    }
    
    /**
     * カスタムレポートクエリ一覧を取得用
     * @param categoryId
     * @return
     */
    @GET
    @Path("category/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblCustomReportQueryVoList getCustomReportQueryList(@QueryParam("categoryId") String categoryId) {
        return tblCustomReportQueryService.getCustomReportQueryCategoryList(categoryId);
    }

    /**
     * カスタムレポートクエリ一覧から実行ボタンを押下
     *
     * @param reportId
     * @return
     */
    @GET
    @Path("{reportId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblCustomReportQueryVo getCustomReportQuery(@PathParam("reportId") Long reportId) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblCustomReportQueryService.getCustomReportQuery(reportId, loginUser.getLangId());
    }
    
    @GET
    @Path("query/{reportId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<TblCustomReportQuery> getQuery(@PathParam("reportId") Long reportId) {
        return new ObjectResponse<>(tblCustomReportQueryService.getQuery(reportId));
    }

    /**
     * カスタムレポートクエリ一覧から追加ボタンを押下
     *
     * @param tblCustomReportQueryVo
     * @return
     */
    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblCustomReportQueryVo addCustomReportQuery(TblCustomReportQueryVo tblCustomReportQueryVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblCustomReportQueryService.addCustomReportQuery(tblCustomReportQueryVo, loginUser.getUserUuid());
    }

    /**
     * カスタムレポートクエリ一覧から削除ボタンを押下
     *
     * @param reportId
     * @return
     */
    @DELETE
    @Path("{reportId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteCustomReportQuery(@PathParam("reportId") String reportId) {
        BasicResponse response = new BasicResponse();
        int cnt = tblCustomReportQueryService.deleteCustomReportQuery(Long.valueOf(reportId));

        if (cnt < 1) {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return response;
    }

    /**
     * カスタムレポートクエリ詳細画面から保存ボタンを押下
     *
     * @param tblCustomReportQueryVo
     * @return
     */
    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateCustomReportQuery(TblCustomReportQueryVo tblCustomReportQueryVo) {
        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        int cnt = tblCustomReportQueryService.updateCustomReportQuery(tblCustomReportQueryVo, loginUser.getUserUuid());

        if (cnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return response;
    }

    /**
     * カスタムレポートクエリ詳細画面から実行ボタンを押下
     *
     * @param tblCustomReportQueryVo
     * @return
     */
    @POST
    @Path("json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblCustomReportQueryResultList getCustomReportQueryJson(TblCustomReportQueryVo tblCustomReportQueryVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblCustomReportQueryService.getCustomReportQueryJson(tblCustomReportQueryVo, loginUser);
    }

    /**
     * カスタムレポートクエリ詳細画面から実行ボタンを押下
     *
     * @param tblCustomReportQueryVo
     * @return
     */
    @POST
    @Path("csv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblCustomReportQueryResultList getCustomReportQueryCsv(TblCustomReportQueryVo tblCustomReportQueryVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblCustomReportQueryService.getCustomReportQueryCsv(tblCustomReportQueryVo, loginUser);
    }

    /**
     * URL直接アクセス
     *
     * @return
     */
    @GET
    @Path("json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getCustomReportQueryJson2() {
        MultivaluedMap<String, String> map = context.getQueryParameters();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (map.get("reportId") == null) {
            if (map.get("reportName") != null && map.get("reportName").get(0) != null) {
                String reportName = FileUtil.getDecode(map.get("reportName").get(0));
                int flag = tblCustomReportQueryService.checkReportName(reportName);
                if (2 == flag) {
                    Gson gson = new Gson();
                    Map<String, Object> returnMap = new HashMap();
                    returnMap.put("error", true);
                    returnMap.put("errorCode", ErrorMessages.E201_APPLICATION);
                    returnMap.put("errorMessage", mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_report_name_multiple_records_found"));
                    return gson.toJson(returnMap);
                }
            }
        }
        return tblCustomReportQueryService.getCustomReportQueryJsonForUrl(tblCustomReportQueryService.getUrlCustomReportQuery(map, loginUser.getLangId()), loginUser);

    }

    /**
     * URL直接アクセスし
     * <P>
     * 一回のAPIコールでCSVを返す
     *
     * @return
     */
    @GET
    @Path("csv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getCustomReportQueryCsv2() {
        MultivaluedMap<String, String> map = context.getQueryParameters();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        if (map.get("reportId") == null) {
            if (map.get("reportName") != null && map.get("reportName").get(0) != null) {
                String reportName = FileUtil.getDecode(map.get("reportName").get(0));
                int flag = tblCustomReportQueryService.checkReportName(reportName);
                if (2 == flag) {
//                Gson gson = new Gson();
//                Map<String, Object> returnMap = new HashMap();
//                returnMap.put("error", true);
//                returnMap.put("errorCode", ErrorMessages.E201_APPLICATION);
//                returnMap.put("errorMessage", mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_report_name_multiple_records_found"));
                    Response.ResponseBuilder response = Response.status(403);
                    return response.build();
                }
            }
        }

        return tblCustomReportQueryService.getCustomReportQueryCsv2(tblCustomReportQueryService.getUrlCustomReportQuery(map, loginUser.getLangId()), loginUser);
    }

    /**
     * カスタムレポートクエリの定義情報出力
     *
     * @param all
     * @param tblCustomReportQueryVoList
     * @return
     */
    @POST
    @Path("exportjson")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getCustomReportExportJson(@QueryParam("all") int all, TblCustomReportQueryVoList tblCustomReportQueryVoList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblCustomReportQueryService.getCustomReportSetValueJson(loginUser, all, tblCustomReportQueryVoList);
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
    @Path("importjson")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse importJson(
            @FormDataParam("upfile") InputStream uploadFile,
            @FormDataParam("upfile") FormDataContentDisposition uploadFileDetail,
            @FormDataParam("fileType") String fileType,
            @FormDataParam("functionId") String functionId
    ) {
        StringBuffer path = new StringBuffer(kartePropertyService.getDocumentDirectory());
        ImportResultResponse response = new ImportResultResponse();
        path = path.append(FileUtil.SEPARATOR).append(CommonConstants.CSV);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String uuid = IDGenerator.generate();
        String getFileType = uploadFileDetail.getFileName();
        getFileType = getFileType.substring(getFileType.lastIndexOf(".") + 1);

        if (!getFileType.endsWith("json")) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            response.setErrorMessage(msg);
            return response;
        }

        FileUtil fu = new FileUtil();

        fu.createPath(path.toString());
        path = path.append(FileUtil.SEPARATOR).append(uuid).append(".").append(getFileType);

        fu.createFile(path.toString());
        fu.writeToFile(uploadFile, path.toString());

        Date uploadDate = new Date();
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

        return tblCustomReportQueryService.importCustomReportJson(path.toString(), uuid, loginUser.getUserUuid(), loginUser.getLangId());

    }


}
