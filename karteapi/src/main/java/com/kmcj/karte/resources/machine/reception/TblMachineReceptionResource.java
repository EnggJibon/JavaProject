/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.reception;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;

/**
 *
 * @author Apeng
 */
@RequestScoped
@Path("machine/reception")
public class TblMachineReceptionResource {

    @Inject
    private TblMachineReceptionService tblMachineReceptionService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * Creates a new instance of TblMachineReceptionResource
     */
    public TblMachineReceptionResource() {
    }

    /**
     *
     * M1105_設備受信_画面初期化
     *
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineReceptionList getTblMachineReceptionList() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineReceptionService.getTblMachineReceptionList(loginUser);
    }

    /**
     * M1105_設備受信テーブル更新
     * <p>
     * 所有会社により、送信してくるとき使うAPIです。
     *
     * @param tblMachineReceptionList
     * @return
     */
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblMachineReception(TblMachineReceptionList tblMachineReceptionList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineReceptionService.postTblMachineReception(tblMachineReceptionList, loginUser);
    }

    /**
     * M1105_設備受信テーブルから削除
     * <p>
     *
     * @param receptionId
     * @return
     */
    @DELETE
    @Path("delete/{receptionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteTblMachineReception(@PathParam("receptionId") String receptionId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineReceptionService.deleteTblMachineReception(receptionId, loginUser);
    }

}
