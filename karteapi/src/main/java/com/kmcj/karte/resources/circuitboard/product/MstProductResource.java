package com.kmcj.karte.resources.circuitboard.product;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.IndexsResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponentList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by xiaozhou.wang on 2017/08/10.
 * Updated by MinhDTB on 2018/03/01
 */
@Path("product")
@RequestScoped
public class MstProductResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstProductService mstProductService;

    public MstProductResource() {
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstProductList getMstProductList(@QueryParam("productName") String productName,
                                            @QueryParam("productCode") String productCode) {
        return mstProductService.getMstProductList(productName, productCode);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateMstProducts(List<MstProduct> mstProducts) {
//    public IndexsResponse<MstProduct> updateMstProducts(List<MstProduct> mstProducts) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProductService.updateMstProducts(mstProducts, loginUser);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse addMstProducts(List<MstProduct> mstProducts) {
//    public IndexsResponse<MstProduct> addMstProducts(List<MstProduct> mstProducts) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProductService.addMstProducts(mstProducts, loginUser);
    }

    @DELETE
    @Path("{productId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMstProduct(@PathParam("productId") String productId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProductService.deleteMstProductByProductId(productId, loginUser);
    }

    @GET
    @Path("component")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentList getMstComponentList(@QueryParam("componentCode") String componentCode) {
        return mstProductService.getMstComponentList(componentCode);
    }

    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse exportMstProducts(@QueryParam("productName") String productName,
                                         @QueryParam("productCode") String productCode) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProductService.exportCsv(productName, productCode, loginUser);
    }

    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse importMstProducts(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProductService.importCsv(fileUuid, loginUser);
    }

}
