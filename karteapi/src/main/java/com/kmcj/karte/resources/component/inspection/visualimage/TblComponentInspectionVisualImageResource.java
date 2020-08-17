/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.visualimage;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.component.inspection.visualimage.model.ComponentInspectionItemDetail;
import com.kmcj.karte.resources.component.inspection.visualimage.model.ComponentInspectionVisualImagesResp;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import java.util.List;
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
 * Master Component visual inspection image resource
 *
 * @author duanlin
 */
@RequestScoped
@Path("component/inspection/visualimage")
public class TblComponentInspectionVisualImageResource {

    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private TblComponentInspectionVisualImageService tblComponentInspectionVisualImageService;
    @Inject
    private MstCompanyService mstCompanyService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * Get All
     * 
     * @param resultID
     * @param inspectionType
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionVisualImagesResp getAllVisualImagesForTablet(
            @QueryParam("inspectResultID") String resultID,
            @QueryParam("inspectionType") Integer inspectionType) {

        ComponentInspectionVisualImagesResp response = this.tblComponentInspectionVisualImageService.getInspectionVisualImagesForTablet(resultID, inspectionType);
        
        if (response == null) {
            response = new ComponentInspectionVisualImagesResp();
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;
        }

        return response;
    }
    /**
     * update component inspection visual files. 
     * 
     * @param items
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postVisualImages(List<ComponentInspectionItemDetail> items) {
        BasicResponse response = new BasicResponse();
        if (items == null || items.size() < 1) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;            
        }
        // get login user info.
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        // update component inspection visual files. 
        this.tblComponentInspectionVisualImageService.updateVisualImages(items, loginUser);
        return response;
    }
    
    /**
     * Get one inspection item's visual images
     * 
     * @param inspectionResultDetailId
     * @return
     */
    @GET
    @Path("/{inspectionResultDetailId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionVisualImagesResp getOneInpsectionItemVisualImages(@PathParam("inspectionResultDetailId") String inspectionResultDetailId) {

        ComponentInspectionVisualImagesResp response = new ComponentInspectionVisualImagesResp();
        response.setVisualImages(this.tblComponentInspectionVisualImageService.getVisualImagesById(inspectionResultDetailId));
   
        return response;
    }
    
    private void setErrorInfo(BasicResponse response, String errorCode, String msgDictKey, Object... args) {
        response.setError(true);
        response.setErrorCode(errorCode);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), msgDictKey);
        if (args != null) {
            msg = String.format(msg, args);
        }
        response.setErrorMessage(msg);
    }
}
