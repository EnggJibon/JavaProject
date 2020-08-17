/*
 * To chnge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.company;

import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("component/company")
public class MstComponentCompanyResource {

    /**
     * Creates a new instance of MstComponentCompanyResource
     */
    public MstComponentCompanyResource() {
    }

    @Inject
    private MstComponentCompanyService mstComponentCompanyService;
    
    @Context
    private ContainerRequestContext requestContext;

    /**
     * <P>
     * 金型詳細画面では 部品コード(先方部品コードから読み替えができたとき)がデフォルトセットされる
     *
     * @param componentId
     * @param mstComponentCompanyVoList
     * @return an instance of MstComponentCompanyVoList
     */
    @POST
    @Path("replacement/{componentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentCompanyVoList getMstComponentCompanies(
            @PathParam("componentId") String componentId,
            MstComponentCompanyVoList mstComponentCompanyVoList) {
        return mstComponentCompanyService.replacementOtherComponentCode(componentId, mstComponentCompanyVoList);
    }
    
    
     /**
     * <P>
     * 外部連携バッチ用
     *
     * @return an instance of MstComponentCompanyVoList
     */
    @GET
    @Path("extdata/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentCompanyVoList getMstComponentCompanies() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentCompanyService.getBatchOtherComponentCode(loginUser);
    }

}
