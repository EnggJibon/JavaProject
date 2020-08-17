/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.reception;

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
@Path("mold/reception")
public class TblMoldReceptionResource {

    @Inject
    private TblMoldReceptionService tblMoldReceptionService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * Creates a new instance of TblMoldReceptionResource
     */
    public TblMoldReceptionResource() {
    }

    /**
     *
     * M0029_金型受信_画面初期化
     *
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldReceptionList getTblMoldReceptionList() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldReceptionService.getTblMoldReceptionList(loginUser);
    }

    /**
     * M0028_金型送信_送信ボタン
     * <p>
     * 金型受信テーブル更新
     * <p>
     * 所有会社により、送信してくるとき使うAPIです。
     *
     * @param tblMoldReceptionList
     * @return
     */
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblMoldReception(TblMoldReceptionList tblMoldReceptionList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldReceptionService.postTblMoldReception(tblMoldReceptionList, loginUser);
    }

    /**
     * M0028_金型送信_登録せず削除ボタン
     * <p>
     * 金型受信テーブルから削除
     * <p>
     *
     * @param receptionId
     * @return
     */
    @DELETE
    @Path("delete/{receptionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteTblMoldReception(@PathParam("receptionId") String receptionId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldReceptionService.deleteTblMoldReception(receptionId, loginUser);
    }

}
