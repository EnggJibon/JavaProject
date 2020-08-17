/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation.log;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
 * @author kmc
 */
@RequestScoped
@Path("operation/log")
public class TblOperationLogResource {
    
    public TblOperationLogResource() {
    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblOperationLogService tblOperationLogService;
    
    /**
     * 操作ログ照会データ取得
     *
     * @param operationDateFrom
     * @param operationDateTo
     * @param userId
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblOperationLogList getOperationLogList(
            @QueryParam("operationDateFrom") String operationDateFrom,
            @QueryParam("operationDateTo") String operationDateTo,
            @QueryParam("userId") String userId,
            @QueryParam("sidx") String sidx,//order Key
            @QueryParam("sord") String sord,//order 順
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblOperationLogService.getTblOperationLogList(operationDateFrom, operationDateTo, userId, 
                pageNumber, pageSize, loginUser.getLangId(), false, true, sidx, sord);
    }

    /**
     * 操作ログ照会CSV出力
     *
     * @param operationDateFrom
     * @param operationDateTo
     * @param userId
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getOperationLogCsvOutPut(
            @QueryParam("operationDateFrom") String operationDateFrom,
            @QueryParam("operationDateTo") String operationDateTo,
            @QueryParam("userId") String userId
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblOperationLogService.getTblOperationLogCsvOutPut(operationDateFrom, operationDateTo, userId, loginUser);
    }
    
}
