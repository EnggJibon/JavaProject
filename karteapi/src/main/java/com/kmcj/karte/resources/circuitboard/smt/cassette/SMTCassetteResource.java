package com.kmcj.karte.resources.circuitboard.smt.cassette;

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
 * Created by xiaozhou.wang on 2017/08/08.
 */
@Path("smtcassette")
@RequestScoped
public class SMTCassetteResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private SMTCassetteService smtCassetteService;

    public SMTCassetteResource() {

    }

    /**
     * SMT実装機のカセット別の吸着情報の検索
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
    public SMTGraphList getSearchResult(
            @QueryParam("lineNo") int lineNo,
            @QueryParam("sidx") String sidx,//order Key
            @QueryParam("sord") String sord,//order 順
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize
    ) {
        return smtCassetteService.getSearchResult(lineNo,sidx,sord,pageNumber,pageSize);
    }

    /**
     * SMT実装機のカセット別の吸着情報取得グラフ作成
     * @param lineNo
     * @param machineUuid
     * @param barGraphColName
     * @return 
     */
    @GET
    @Path("graph")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SMTGraphList getSMTCassetteGraphList(
            @QueryParam("lineNo") int lineNo,//生産ラインマスタ
            @QueryParam("machineUuid") String machineUuid,//設備UUID
            @QueryParam("barGraphColName") String barGraphColName//棒グラフ表示対象
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return smtCassetteService.getSMTCassetteGraphList(lineNo,machineUuid,barGraphColName,loginUser.getLangId());
    }
}
