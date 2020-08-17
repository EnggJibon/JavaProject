/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.currency;

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
@Path("currency")
public class MstCurrencyResource {

    /**
     * Creates a new instance of MstCurrencyResource
     */
    public MstCurrencyResource() {
    }

    @Inject
    private MstCurrencyService mstCurrencyService;

    /**
     * 通貨コードを取得
     *
     * @return an instance of MstCurrencyList
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MstCurrencyList getCurrencyCode() {
        return mstCurrencyService.getMstCurrency();
    }
    
    /**
     * 通貨コードにより、小数桁数を決める
     *
     * @return an instance of MstCurrencyList
     */
    @GET
    @Path("init")
    @Produces(MediaType.APPLICATION_JSON)
    public MstCurrency getDefaultCurrencyCode() {
        return mstCurrencyService.getDefaultCurrencyMstCurrency(null);
    }
    
    /**
     * 通貨コードで補助用
     *
     * @param currencyCode
     * @param currencyName
     * @return an instance of MstCurrencyList
     */
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCurrencyList getLikeMstCurrencyList(@QueryParam("currencyCode") String currencyCode,
            @QueryParam("currencyName") String currencyName) {
        return mstCurrencyService.getMstCurrencyList(currencyCode, currencyName, true);
    }

    /**
     * 通貨コードを決めたとき
     *
     * @param currencyCode
     * @param currencyName
     * @return an instance of MstCurrencyList
     */
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCurrencyList getEqualMstCurrencyList(@QueryParam("currencyCode") String currencyCode,
            @QueryParam("currencyName") String currencyName) {
        return mstCurrencyService.getMstCurrencyList(currencyCode, currencyName, false);
    }

}
