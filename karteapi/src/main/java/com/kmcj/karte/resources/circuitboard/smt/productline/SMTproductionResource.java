/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.smt.productline;

import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.smt.SMTGraphList;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author zf
 */
@Path("smtproduction")
@RequestScoped
public class SMTproductionResource {

    @Context
    private ContainerRequestContext requestcontext;

    @Inject
    private SMTproductionService smtproductionservice;

    public void SMTproductionResource() {
    }

    /**
     * SMT実装機の生産ライン別分析情報稼働状況検索
     * 
     * @param lineNo
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SMTGraphList getSearchResult(@QueryParam("lineNo") int lineNo) {
        return smtproductionservice.getSearchResult(lineNo);
    }

    /**
     * SMT実装機の生産ライン別分析情報機種別稼働分析グラフ作成
     * 
     * @param lineNo
     * @return 
     */
    @GET
    @Path("graphofmachine")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SMTGraphList getGraphOfMachines(@QueryParam("lineNo") int lineNo) {
        LoginUser loginUser = (LoginUser)requestcontext.getProperty(RequestParameters.LOGINUSER);
        return smtproductionservice.getGraphOfMachines(getSearchResult(lineNo), loginUser);
    }

    /**
     * SMT実装機の生産ライン別分析情報生産ライン稼働分析グラフ作成
     * 
     * @param lineNo
     * @return 
     */
    @GET
    @Path("graphofline")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SMTGraphList getGraphOfLine(@QueryParam("lineNo") int lineNo) {
        LoginUser loginUser = (LoginUser)requestcontext.getProperty(RequestParameters.LOGINUSER);
        return smtproductionservice.getGraphOfLine(getSearchResult(lineNo), loginUser);
    }
}
