/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice.category;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
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
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@Path("choice/category")
@RequestScoped
public class MstChoiceCategoryResource {

    @Context
    private UriInfo context;

    @Inject
    private MstChoiceCategoryService mstChoiceCategoryService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblUploadFileService tblUploadFileService;

    @Inject
    private KartePropertyService kartePropertyService;

    /**
     * Creates a new instance of MstChoiceResource
     */
    public MstChoiceCategoryResource() {
    }

    /**
     *
     * S0011 分類項目設定 画面描画時 言語マスタよりすべての言語を取得し
     *
     * @param langId
     * @return an instance of java.lang.String
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstChoiceCategoryVo getChoiceCategories(@QueryParam("langId") String langId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if(StringUtils.isEmpty(langId)) {
            langId = loginUser.getLangId();
        }
        return mstChoiceCategoryService.getChoiceCategories(langId);
    }

    /**
     * S0011 分類項目設定 編集された内容に従って、選択肢マスタを更新する
     *
     * @param mstChoiceCategoryVo
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postChoiceCategories(MstChoiceCategoryVo mstChoiceCategoryVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        BasicResponse response = mstChoiceCategoryService.postChoiceCategories(mstChoiceCategoryVo, loginUser);
        return response;
    }

    /**
     * 分類項目CSV出力（getChoiceCategoryCsv)
     *
     * @param langId
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getChoiceCategoryCsv(@QueryParam("langId") String langId) {
        // CATEGORY,言語,種別,分類項目,上位分類項目,連番,区分値
        // CATEGORYは文言ではなく英語のままでかまいません。mst_user.departmentなどの値をそのまま出します。
        // 言語は文言(lang)を用いて、値は画面で選択されている言語名称を出力します。
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstChoiceCategoryService.getOutputCsv(langId, loginUser);
    }

    /**
     * 分類項目CSV取込（postChoiceCategoryCsv)
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postChoiceCategoryCsv(@PathParam("fileUuid") String fileUuid) {
        // 取込プログラムではCATEGORY、言語、連番でPK判定をし、INSERTかUPDATEを行ってください。
        // DELETEはなくてよいです。
        // 上位区分値は出力、取込ともに対象外でかまいません。
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            return mstChoiceCategoryService.importCsv(fileUuid, loginUser);
        } catch (Exception ex) {
            ImportResultResponse importResultResponse = new ImportResultResponse();
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            importResultResponse.setErrorMessage(
                    mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid"));
            return importResultResponse;
        }
    }

    /**
     * 分類項目設定の全言語出力
     *
     * @return
     */
    @GET
    @Path("export/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse exportAll() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return mstChoiceCategoryService.exportAll(loginUser);

    }

    /**
     * 分類項目設定の全言語取込
     *
     * @param uploadFile
     * @param uploadFileDetail
     * @param fileType
     * @param functionId
     * @return
     */
    @POST
    @Path("import/all")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse importAll(
            @FormDataParam("upfile") InputStream uploadFile,
            @FormDataParam("upfile") FormDataContentDisposition uploadFileDetail,
            @FormDataParam("fileType") String fileType,
            @FormDataParam("functionId") String functionId
    ) {
        StringBuffer path = new StringBuffer(kartePropertyService.getDocumentDirectory());
        ImportResultResponse response = new ImportResultResponse();
        path = path.append(FileUtil.SEPARATOR).append(CommonConstants.EXCEL_FILE);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String uuid = IDGenerator.generate();
        String getFileType = uploadFileDetail.getFileName();
        getFileType = getFileType.substring(getFileType.lastIndexOf(".") + 1);

        if (!getFileType.endsWith("xlsx") && !getFileType.endsWith("xls")) {
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
        return mstChoiceCategoryService.importAll(path.toString(), uuid, loginUser);
    }
}
