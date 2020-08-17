/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.suspension;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.logging.Level;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 生産中断履歴テーブルリソース
 *
 * @author zds
 */
@RequestScoped
@Path("production/suspension")
public class TblProductionSuspensionResource {

    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblProductionSuspensionlService tblProductionSuspensionlService;



    private Logger logger = Logger.getLogger(TblProductionSuspensionResource.class.getName());

    public TblProductionSuspensionResource() {
    }

    /**
     * 中断履歴一覧を取得, By生産実績ID
     *
     * @param productionId
     * @return
     */
    @GET
    @Path("{productionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionSuspensionList getExtProductionDetailsByBatch(@PathParam("productionId") String productionId) {

        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        logger.log(Level.FINE, "PathParam'{'productionId:{0}'}'", productionId);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblProductionSuspensionList tblProductionSuspensions = tblProductionSuspensionlService.getProductionSuspensionListByProductionId(productionId,loginUser);

        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);

        return tblProductionSuspensions;
    }

    /**
     * 中断/一時中断/再開 では選択されている生産実績を OO状態 にし、生産中断履歴テーブルに中断時刻と中断理由を記録する。
     *
     * @param tblProductionSuspension 生産中断履歴
     * @return
     */
    @POST
    @Path("changestatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse changeStatus(TblProductionSuspension tblProductionSuspension) {

        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = tblProductionSuspensionlService.changeStatus(tblProductionSuspension, null, loginUser);

        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);

        return response;
    }

}
