/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.close.entry;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;

/**
 * 作業入力締めテーブルリソース
 * @author t.ariki
 */
@RequestScoped
@Path("work/close/entry")
public class TblWorkCloseEntryResource {
    
    @Context
    private UriInfo context;
    
    @Context
    private ContainerRequestContext requestContext;
   
    @Inject
    private TblWorkCloseEntryService tblWorkCloseEntryService;

    public TblWorkCloseEntryResource() {}
    
    /**
     * 作業入力締めテーブル全件取得
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TblWorkCloseEntryList getWorkCloseEntries() {
        return tblWorkCloseEntryService.getAll();
    }

    /**
     * 作業入力締めテーブル最新取得
     * @return 
     */    
    @GET
    @Path("latest")
    @Produces(MediaType.APPLICATION_JSON)
    public TblWorkCloseEntryList getLatest() {
        TblWorkCloseEntryList response = new TblWorkCloseEntryList();
        TblWorkCloseEntry latest = tblWorkCloseEntryService.getLatest();
        ArrayList<TblWorkCloseEntry> latests = new ArrayList<>();
        latests.add(latest);
        if (latest != null) {
            response.setTblWorkCloseEntries(latests);
        }
        return response;
    }
}
