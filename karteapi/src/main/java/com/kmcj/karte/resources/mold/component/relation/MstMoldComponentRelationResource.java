/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.component.relation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * 金型
 *
 * @author admin
 */
@RequestScoped
@Path("mold/component/relation")
public class MstMoldComponentRelationResource {

    public MstMoldComponentRelationResource() {
    }

    @Inject
    private MstMoldComponentRelationService mstMoldComponentRelationService;


    /**
     * 
     * @param moldId
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldComponentRelationList getMoldByMoldId(@PathParam("moldId") String moldId) {

        return mstMoldComponentRelationService.getMstMoldComponentRelationByMoldId(moldId);
    }
    
    /**
     * 部品IDから関連している金型情報取得
     * 
     * @param componentId
     * @return 
     */
    @GET
    @Path("{componentid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldComponentRelationList getMoldByComponentId(@PathParam("componentid") String componentId) {

        return mstMoldComponentRelationService.getMstMoldComponentRelationByComponentId(componentId);
    }
    
        /**
     * 部品IDから関連している金型情報取得
     * 
     * @param componentId
     * @return 
     */
    @GET
    @Path("withoutdispose/{componentid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldComponentRelationList getMoldByComponentIdWithoutDispose(@PathParam("componentid") String componentId) {

        return mstMoldComponentRelationService.getMstMoldComponentRelationByComponentIdWithoutDispose(componentId);
    }

    /**
     * バッチで金型部品関係データを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldcomponentrelation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldComponentRelationList getExtMoldComponentRelationsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return mstMoldComponentRelationService.getExtMoldComponentRelationsByBatch(latestExecutedDate, moldUuid);
    }
}
