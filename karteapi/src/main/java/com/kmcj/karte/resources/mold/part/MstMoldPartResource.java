package com.kmcj.karte.resources.mold.part;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
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

@RequestScoped
@Path("mold/part")
public class MstMoldPartResource {

    /**
     * Creates a new instance of MstMoldPartResource
     */
    public MstMoldPartResource() {
    }

    @Inject
    private MstMoldPartService mstMoldPartService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;


    /**
     * get moldPart list based on moldPartCode, moldPartName, manufacturer, memo(search)
     *
     * @param moldPartCode
     * @param moldPartName
     * @param manufacturer
     * @param memo
     * @param pageNumber
     * @param pageSize
     * @return an instance of MstMoldPart
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldPartList getMoldPartsBy(@QueryParam("moldPartCode") String moldPartCode,
            @QueryParam("moldPartName") String moldPartName, 
            @QueryParam("manufacturer") String manufacturer, 
            @QueryParam("memo") String memo,
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize) {
        return mstMoldPartService.getMstMoldParts(moldPartCode, moldPartName, manufacturer, memo, pageNumber, pageSize);
    }
    /**
     * get MoldPart detail based on moldPartCode
     *
     * @param moldPartId
     * @return
     */
    @GET
    @Path("{moldPartId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldPartList getMoldPart(@PathParam("moldPartId") String moldPartId) {
        return mstMoldPartService.getMstMoldPartDetail(moldPartId);
    }

    /**
     *get MoldPart detail based on moldPartName
     * @param moldPartName
     * @return
     */
    @GET
    @Path("getname")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldPartList getMoldPartByName(@QueryParam("moldPartName") String moldPartName) {
        return mstMoldPartService.getMoldPartByName(moldPartName);
    }

    /**
     * Get the number of MoldPart
     *
     * @param moldPartCode
     * @param moldPartName
     * @param manufacturer
     * @param memo
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("moldPartCode") String moldPartCode,
            @QueryParam("moldPartName") String moldPartName, @QueryParam("manufacturer") String manufacturer, @QueryParam("memo") String memo) {
        CountResponse count = mstMoldPartService.getMstMoldPartCount(moldPartCode, moldPartName, manufacturer, memo);

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
     * Create MoldPart detail（postMoldPart)
     *
     * @param mstMoldPart
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldPart(MstMoldPart mstMoldPart) {
        BasicResponse response = new BasicResponse();
        String getMoldPartCode = mstMoldPart.getMoldPartCode();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        if (null == getMoldPartCode && "".equals(getMoldPartCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        
        if (mstMoldPartService.getMstMoldPartExistCheck(getMoldPartCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }

        mstMoldPart.setId(IDGenerator.generate());
        mstMoldPart.setCreateDate(new java.util.Date());
        mstMoldPart.setCreateUserUuid(loginUser.getUserUuid());
        mstMoldPart.setUpdateDate(new java.util.Date());
        mstMoldPart.setUpdateUserUuid(loginUser.getUserUuid());
        mstMoldPartService.createMstMoldPart(mstMoldPart);
        return response;
    }

    /**
     * Delete MoldPart detail（deleteMoldPart）
     *
     * @param moldPartId
     * @return
     */
    @DELETE
    @Path("{moldPartId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMoldPart(@PathParam("moldPartId") String moldPartId) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (!mstMoldPartService.isDeletableMoldPart(moldPartId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        } else {
            mstMoldPartService.deleteMstMoldPartById(moldPartId);
        }

        return response;
    }

    /**
     * Update MoldPart detail（putMoldPart)
     *
     * @param mstMoldPart
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMoldPart(MstMoldPart mstMoldPart) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstMoldPart.setUpdateDate(new java.util.Date());
        mstMoldPart.setUpdateUserUuid(loginUser.getUserUuid());
        BasicResponse response = new BasicResponse();
        String getMoldPartCode = mstMoldPart.getMoldPartCode();
        if (null == getMoldPartCode && "".equals(getMoldPartCode)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        
        if (!mstMoldPartService.getMstMoldPartExistCheck(mstMoldPart.getMoldPartCode())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        
        int cnt = mstMoldPartService.updateMstMoldPartByQuery(mstMoldPart);
        if (cnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        }
        return response;
    }

    /**
     * MoldPart CSV output（getMoldPartsCsv)
     *
     * @param moldPartCode
     * @param moldPartName
     * @param manufacturer
     * @param memo
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMoldPartsCsv(@QueryParam("moldPartCode") String moldPartCode,
            @QueryParam("moldPartName") String moldPartName, @QueryParam("manufacturer") String manufacturer, @QueryParam("memo") String memo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldPartService.getOutputCsv(moldPartCode,
                moldPartName, manufacturer, memo,
                loginUser);
    }

    /**
     * MoldPart CSV import（postMoldPartsCsv)
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMoldPartsCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            return mstMoldPartService.importCsv(fileUuid, loginUser);
        } catch (Exception ex) {
            ImportResultResponse importResultResponse = new ImportResultResponse();
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            importResultResponse.setErrorMessage(
                    mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid"));
            return importResultResponse;
        }
    }
    
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldPartDetailList searchMoldParts(@QueryParam("moldPartCode") String moldPartCode, @QueryParam("moldPartName") String moldPartName) {
        return mstMoldPartService.searchMoldParts(moldPartCode, moldPartName, true);
    }
    
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldPartDetailList findMoldPart(@QueryParam("moldPartCode") String moldPartCode, @QueryParam("moldPartName") String moldPartName) {
        return mstMoldPartService.searchMoldParts(moldPartCode, moldPartName, false);
    }
}