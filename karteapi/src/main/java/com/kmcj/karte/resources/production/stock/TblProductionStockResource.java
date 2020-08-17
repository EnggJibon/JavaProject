/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.stock;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("production/stock")
public class TblProductionStockResource {

    /**
     * Creates a new instance of TblProductionStockResource
     */
    public TblProductionStockResource() {
    }

    @Inject
    private TblProductionStockService tblProductionStockService;

    /**
     *
     *
     * @return an instance of TblProductionStockVoList
     */
    @GET
    @Path("init")
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionStockVoList getTblProductionStockVoList() {
        return tblProductionStockService.getPproductionStockVoList();
    }

}
