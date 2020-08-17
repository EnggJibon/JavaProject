/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.spec;

import com.kmcj.karte.CountResponse;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jiangxs
 */
@RequestScoped
@Path("mold/proccond/spec")
public class MstMoldProcCondSpecResource {

    public MstMoldProcCondSpecResource() {
    }

    @Inject
    private MstMoldProcCondSpecService mstMoldProcCondSpecService;

    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("moldId") String moldId) {
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(12);//TODO
        return countResponse;
    }
    
    
   
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getMstMoldProcCondSpecs(@QueryParam("moldId") String moldId) {
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(12);//TODO
        return countResponse;
    }
    
    /**
     * バッチで金型加工条件仕様マスタデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldproccondspec")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldProcCondSpecList getExtMoldProcCondSpecsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return mstMoldProcCondSpecService.getExtMoldProcCondSpecsByBatch(latestExecutedDate, moldUuid);
    }
}
