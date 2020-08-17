/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.smt.nozzle;

import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.smt.SMTGraphList;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author zf
 */
@Path("smtnozzle")
@RequestScoped
public class SMTnozzleResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private SMTnozzleService smtnozzleservice;

    public SMTnozzleResource() {

    }

    /**
     * SMT実装機のノズル別分析情報検索
     * 
     * @param lineNo
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SMTGraphList getSearchResult(@QueryParam("lineNo") int lineNo,
            @QueryParam("sidx") String sidx,//order Key
            @QueryParam("sord") String sord,//order 順
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize) {
        return smtnozzleservice.getSearchResult(lineNo, sidx, sord, pageNumber, pageSize);
    }

    /**
     * ノズル別表示対象リスト取得
     * 
     * @param machineUuid
     * @return 
     */
    @GET
    @Path("attr")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MachineAttributeSelectList getGraphTargetMachine(@QueryParam("machineUuid") String machineUuid) {
        MachineAttributeSelectList selectList = new MachineAttributeSelectList();
        selectList.setMstMachineAttributeList(smtnozzleservice.getGraphTargetMachine(machineUuid));
        return selectList;
    }

    /**
     * SMT実装機のノズル別分析情報グラフ作成
     * 
     * @param machineUuid
     * @param attrCode
     * @param graphTarget
     * @param graphType
     * @return 
     */
    @GET
    @Path("graph")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SMTGraphList getSmtNozzleGraph(@QueryParam("machineUuid") String machineUuid,
            @QueryParam("attrCode") String attrCode,
            @QueryParam("graphTarget") String graphTarget,
            @QueryParam("graphType") String graphType//棒グラフ表示対象
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return smtnozzleservice.getSMTNozzleGraph(machineUuid, graphTarget, graphType, attrCode, loginUser.getLangId());
    }
}
