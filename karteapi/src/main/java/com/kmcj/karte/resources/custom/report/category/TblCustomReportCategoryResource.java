/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.category;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
 * レポートクエリユーザーマスタ リソース
 *
 * @author admin
 */
@RequestScoped
@Path("custom/report/category")
public class TblCustomReportCategoryResource {

    public TblCustomReportCategoryResource() {
    }
    
    @Inject
    private TblCustomReportCategoryService tblCustomReportCategoryService;
    
    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @GET
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblCustomReportCategoryList getCategories() {
        return tblCustomReportCategoryService.getCategories();
    }
    
    /**
     * @param tblCustomReportCategoryList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblCustomReportCategoryList addCategory(TblCustomReportCategoryList tblCustomReportCategoryList){
        tblCustomReportCategoryService.updateCategories(tblCustomReportCategoryList);
        return tblCustomReportCategoryList; 
    }
    
    /**
     * @param id
     * @return an instance of BasicResponse
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteCategory(@PathParam("id") String id) {
        BasicResponse response = new BasicResponse();
        int cnt = tblCustomReportCategoryService.deleteCategory(id);

        if (cnt < 1) {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return response;
    }

}
