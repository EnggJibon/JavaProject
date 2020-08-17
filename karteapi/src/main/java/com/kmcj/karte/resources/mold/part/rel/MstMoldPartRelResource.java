/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.rel;

import com.kmcj.karte.UrlDecodeInterceptor;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author BnK Win10 2010
 */
@RequestScoped
@Path("mold/part/rel")
public class MstMoldPartRelResource {
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MstMoldPartResource
     */
    public MstMoldPartRelResource() {
    }

    @Inject
    private MstMoldPartRelService mstMoldPartRelService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    
    /**
     * 会社マスタ取得(getMoldPartRel)
     *
     * @param maintenanceId
     * @param moldUuid
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //@Interceptors({UrlDecodeInterceptor.class})
    public List<MstMoldPartDetailMaintenance> getMstMoldPartRelDetailByMaintId(
        @QueryParam("maintenanceId") String maintenanceId,@QueryParam("moldUuid") String moldUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if(moldUuid != null && !moldUuid.equals("")){
            return mstMoldPartRelService.getMstMoldPartRelsByMoldUuid(moldUuid, loginUser);
        }else{
            return mstMoldPartRelService.getMstMoldPartRelDetailByMaintId(maintenanceId, loginUser);
        }
    }
}
