/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user.mail.reception;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author admin
 */
@RequestScoped
@Path("user/mail/reception")
public class MstUserMailReceptionResource {

    @Inject
    private MstUserMailReceptionService mstUserMailReceptionService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * Creates a new instance of MstUserMailReceptionResource
     */
    public MstUserMailReceptionResource() {
    }

    /**
     * 通知イベントを取得
     *
     * @return an instance of MstUserMailReceptionEventList
     */
    @GET
    @Path("event")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstUserMailReceptionEventList getMstUserMailReceptionEventList() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);;
        return mstUserMailReceptionService.getUserMailReceptionEvent(loginUser.getUserUuid());
    }

    /**
     * ログインユーザー以外の通知イベントを取得
     *
     * @return an instance of MstUserMailReceptionEventList
     */
    @GET
    @Path("event/other")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstUserMailReceptionEventList getOtherMstUserMailReceptionEventList(@QueryParam("userUuid") String userUuid) {
        return mstUserMailReceptionService.getUserMailReceptionEvent(userUuid);
    }

    /**
     * 通知イベントを追加する
     *
     * @param mstUserMailReceptionEventList
     * @return an instance of BasicResponse
     */
    @POST
    @Path("event")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postUser(MstUserMailReceptionEventList mstUserMailReceptionEventList, @QueryParam("userUuid") String userUuid) {
        String paramUserUuid;
        if (userUuid == null || userUuid.equals("")) {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            paramUserUuid = loginUser.getUserUuid();
        }
        else {
            paramUserUuid = userUuid;
        }
        return mstUserMailReceptionService.postUserMailReceptionEvent(mstUserMailReceptionEventList, paramUserUuid);
    }

    /**
     * 所属別設定を取得
     *
     * @return an instance of MstUserMailReceptionEventList
     */
    @GET
    @Path("department")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstUserMailReceptionDepartmentList getMstUserMailReceptionDepartmentList(@QueryParam("userUuid") String userUuid) {
        String paramUserUuid;
        if (userUuid == null || userUuid.equals("")) {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            paramUserUuid = loginUser.getUserUuid();
        }
        else {
            paramUserUuid = userUuid;
        }
        return mstUserMailReceptionService.getMstUserMailReceptionDepartmentList(paramUserUuid);
    }

}
