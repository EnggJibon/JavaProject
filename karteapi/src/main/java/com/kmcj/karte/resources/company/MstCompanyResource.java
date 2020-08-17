/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.company;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
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
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("company")
public class MstCompanyResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MstCompanyResource
     */
    public MstCompanyResource() {
    }

    @Inject
    private MstCompanyService mstCompanyService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 会社マスタ複数取得(getCompanies)
     *
     * @param companyCode
     * @param companyName
     * @return an instance of MstCompanyList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyList getCompanies(@QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        return mstCompanyService.getMstCompanies(companyCode, companyName);
    }

    /**
     * 会社マスタ取得(getCompany)
     *
     * @param companyCode
     * @return
     */
    @GET
    @Path("{companyCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyList getCompany(@PathParam("companyCode") String companyCode) {
        return mstCompanyService.getMstCompanyDetail(FileUtil.getDecode(companyCode));
    }

    /**
     *
     * @param companyName
     * @return
     */
    @GET
    @Path("getname")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyList getCompanyByName(@QueryParam("companyName") String companyName) {
        return mstCompanyService.getCompanyByName(companyName);
    }

    /**
     *
     * @param companyId
     * @return
     */
    @GET
    @Path("getbyid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyResp getCompanyById(@QueryParam("companyId") String companyId) {
        MstCompanyResp mstCompanyResp = new MstCompanyResp();
        mstCompanyResp = mstCompanyService.getCompanyById(companyId);
        return mstCompanyResp;
    }
    
    /**
     * 会社マスタ件数を取得する
     *
     * @param companyCode
     * @param companyName
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        CountResponse count = mstCompanyService.getMstCompanyCount(companyCode, companyName);

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
     * 会社マスタ追加（postCompany)
     *
     * @param mstCompany
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postCompany(MstCompany mstCompany) {
        BasicResponse response = new BasicResponse();
        String getCompanyCode = mstCompany.getCompanyCode();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        if (null == getCompanyCode && "".equals(getCompanyCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        
        if (mstCompanyService.getMstCompanyExistCheck(getCompanyCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }
        
        if (mstCompany.getMyCompany() == 1) {
            if (mstCompanyService.checkMyCompanyCount(getCompanyCode)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_my_company_duplicate"));
                return response;
            }
        }

        mstCompany.setId(IDGenerator.generate());
        mstCompany.setCreateDate(new java.util.Date());
        mstCompany.setCreateUserUuid(loginUser.getUserUuid());
        mstCompany.setUpdateDate(new java.util.Date());
        mstCompany.setUpdateUserUuid(loginUser.getUserUuid());
        //2016-11-30 jiangxiaosong add start
        mstCompany.setExternalFlg(CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        mstCompanyService.createMstCompany(mstCompany);
        return response;
    }

    /**
     * 会社マスタ削除（deleteCompany）
     *
     * @param companyCode
     * @return
     */
    @DELETE
    @Path("{companyCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteCompany(@PathParam("companyCode") String companyCode) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        companyCode = FileUtil.getDecode(companyCode);
        if (!mstCompanyService.getMstCompanyExistCheck(companyCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }

        // FK依存関係チェック　True依存関係なし削除できる　False　削除できない
        if (!mstCompanyService.getMstCompanyFKCheck(companyCode)) {
            mstCompanyService.deleteMstCompany(companyCode);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
        }

        return response;
    }

    /**
     * 会社マスタ更新（putCompany)
     *
     * @param mstCompany
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putCompany(MstCompany mstCompany) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstCompany.setUpdateDate(new java.util.Date());
        mstCompany.setUpdateUserUuid(loginUser.getUserUuid());
        BasicResponse response = new BasicResponse();
        String getCompanyCode = mstCompany.getCompanyCode();
        if (null == getCompanyCode && "".equals(getCompanyCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        
        if (!mstCompanyService.getMstCompanyExistCheck(mstCompany.getCompanyCode())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        
        if (mstCompany.getMyCompany() == 1) {
            if (mstCompanyService.checkMyCompanyCount(getCompanyCode)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_my_company_duplicate"));
                return response;
            }
        }
        
        int cnt = mstCompanyService.updateMstCompanyByQuery(mstCompany);
        if (cnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        }
        return response;
    }

    /**
     * 会社マスタCSV出力（getCompaniesCsv)
     *
     * @param companyCode
     * @param companyName
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getCompaniesCsv(@QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstCompanyService.getOutputCsv(companyCode,
                companyName,
                loginUser);
    }

    /**
     * 会社マスタCSV取込（postCompaniesCsv)
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postCompaniesCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            return mstCompanyService.importCsv(fileUuid, loginUser);
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
     * 自社フラグ＝1のものをデフォルトセット。複数見つかった場合は最初に見つかったもの
     *
     * @return
     */
    @GET
    @Path("mycompany")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyList getMyCompanies() {
        return mstCompanyService.getMyCompanies();
    }
    
    /**
     * 自社フラグ＝0のものをデフォルトセット。
     *
     * @return
     */
    @GET
    @Path("not/mycompany")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyList getNotMyCompanies() {
        return mstCompanyService.getNotMyCompanies();
    }
    

    /**
     * バッチで外部会社マスタデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extcompany")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyList getExtMstCompaniesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstCompanyService.getExtMstCompaniesByBatch(latestExecutedDate);
    }
    
     /**
     * 検査用の納品先会社マスタ複数取得(getInspectIncomingCompanies)
     *
     * @param company
     * @return an instance of MstCompanyList
     */
    @GET
    @Path("inspectincomingcompany")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyList getInspectIncomingCompanies(@QueryParam("company") String company) {
        return mstCompanyService.getInspectIncomingMstCompanies(company);
    }
    
    /**
     * 検査用の出荷元会社マスタ複数取得(getInspectCompanies)
     *
     * @param company
     * @return an instance of MstCompanyList
     */
    @GET
    @Path("inspectcompany")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyMinList getInspectCompanies(@QueryParam("company") String company) {
        return mstCompanyService.getInspectCompanies(company);
    }
    
    /**
     * 自社情報を取得する
     *
     * @return
     */
    @GET
    @Path("getmycompany")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompany getMyCompany() {
        return mstCompanyService.getSelfCompany();
    }
    
     /**
     *
     * @param companyName
     * @return
     */
    @GET
    @Path("getincomingname")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCompanyList getIncomingCompanyByName(@QueryParam("companyName") String companyName) {
        return mstCompanyService.getIncomingCompanyByName(companyName);
    }

}
