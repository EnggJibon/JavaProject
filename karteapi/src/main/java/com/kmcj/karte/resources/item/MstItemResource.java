/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.item;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("item")
public class MstItemResource {

    /**
     * Creates a new instance of TblInventoryResource
     */
    public MstItemResource() {
    }

    @Inject
    private MstItemService mstItemService;

    /**
     * 品目コードで補助用
     *
     * @param itemCode
     * @param itemName
     * @return an instance of MstItemList
     */
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstItemList getLikeMstItemList(@QueryParam("itemCode") String itemCode, @QueryParam("itemName") String itemName) {
        return mstItemService.getMstItemList(itemCode, itemName, true);
    }

    /**
     * 品目コードを決めたとき
     *
     * @param itemCode
     * @param itemName
     * @return an instance of MstItemList
     */
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstItemList getEqualMstItemList(@QueryParam("itemCode") String itemCode, @QueryParam("itemName") String itemName) {
        return mstItemService.getMstItemList(itemCode, itemName, false);
    }

}
