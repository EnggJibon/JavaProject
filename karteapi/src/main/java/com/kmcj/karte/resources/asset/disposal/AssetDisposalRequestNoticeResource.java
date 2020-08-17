/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal;

import com.google.gson.Gson;
import com.kmcj.karte.BasicResponse;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@Path("asset/disposal/notice")
@RequestScoped
public class AssetDisposalRequestNoticeResource {

    @Context
    private UriInfo context;
    
    @Inject
    private AssetDisposalRequestNoticeService assetDisposalRequestNoticeService;

    /**
     * Creates a new instance of AssetDisposalRequestNoticeResource
     */
    public AssetDisposalRequestNoticeResource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.asset.disposal.AssetDisposalRequestNoticeResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public BasicResponse sendNotice(JsonObject jsonObj) {
        BasicResponse response = new BasicResponse();
        //JsonObject jsonObj = new Gson().fromJson(jsonParam, JsonObject.class);
        int processType = jsonObj.getInt("processType");
        JsonArray requestUuidJsonArray = jsonObj.getJsonArray("requestUuids");
        String[] requestUuids = new String[requestUuidJsonArray.size()];
        for (int i = 0; i < requestUuidJsonArray.size(); i++) {
            requestUuids[i]  = requestUuidJsonArray.getString(i);
        }
        assetDisposalRequestNoticeService.sendNotice(processType, requestUuids);
        return response;
    }
    

    /**
     * PUT method for updating or creating an instance of AssetDisposalRequestNoticeResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
