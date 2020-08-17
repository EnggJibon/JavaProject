/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.password.policy;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author admin
 */
@Path("passwordpolicy")
@RequestScoped
public class CnfPasswordPolicyResource {

    @Inject
    private CnfPasswordPolicyService cnfPasswordPolicyService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * Creates a new instance of CnfPasswordPolicyResource
     */
    public CnfPasswordPolicyResource() {
    }

    /**
     * パスワードポリシー設定情報を取得用
     *
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CnfPasswordPolicyList initCnfPasswordPolicy() {

        return cnfPasswordPolicyService.getCnfPasswordPolicy();
    }

    /**
     * パスワードポリシー設定情報を設定用
     *
     * @param cnfPasswordPolicy
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateCnfPasswordPolicy(CnfPasswordPolicy cnfPasswordPolicy) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return cnfPasswordPolicyService.updateCnfPasswordPolicy(loginUser.getUserUuid(), cnfPasswordPolicy);
    }

}
