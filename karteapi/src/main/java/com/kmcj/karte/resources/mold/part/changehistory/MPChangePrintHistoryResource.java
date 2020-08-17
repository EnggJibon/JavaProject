/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.changehistory;

import com.kmcj.karte.ObjectResponse;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * 金型部品変更記録表の出力ログ用API
 *
 * @author t.takasaki
 */
@RequestScoped
@Path("mpcprinthistory")
public class MPChangePrintHistoryResource {

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MPChangePrintHistoryService mpcPrintService;

    /**
     * 部署の前回出力ログを取得する。
     * @param department
     * @return an instance of java.lang.String
     */
    @GET
    @Path("lasthistory")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<TblMoldPartChangePrintHistory> getLastHistory(@QueryParam("department") int department) {
        return new ObjectResponse<>(mpcPrintService.getLastHistory(department).orElse(null));
    }
}
