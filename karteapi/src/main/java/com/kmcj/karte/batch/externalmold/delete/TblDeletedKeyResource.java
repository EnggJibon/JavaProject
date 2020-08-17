/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmold.delete;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@Path("deletedkey")
@RequestScoped
public class TblDeletedKeyResource {
    @Inject
    private TblDeletedKeyService tblDeletedKeyService;

    /**
     * Creates a new instance of MstChoiceResource
     */
    public TblDeletedKeyResource() {
    }
    
    /**
     * バッチでTblDeletedKeyデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extdeletedkey")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblDeletedKeyVo getExtChoicesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return tblDeletedKeyService.getExtDeletedKeysByBatch(latestExecutedDate);
    }
    
    /**
     * バッチでTblDeletedKeyデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("inspectextdeletedkey")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblDeletedKeyVo getExtDeleteInfoByBatch() {
        return tblDeletedKeyService.getInspectExtDeletedKeysByBatch();
    }
}
