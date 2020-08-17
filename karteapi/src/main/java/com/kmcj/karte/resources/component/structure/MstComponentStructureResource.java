/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.structure;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("component/structure")
public class MstComponentStructureResource {

    /**
     * Creates a new instance of MstComponentStructureResource
     */
    public MstComponentStructureResource() {
    }

    @Inject
    private MstComponentStructureService mstComponentStructureService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * 部品構成マスタ複数取得
     *
     * @param componentCode
     * @param rootComponentCode
     * @param parentComponentCode
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentStructureVoList getMstComponentStructureVoList(
        @QueryParam("componentCode") String componentCode,
        @QueryParam("rootComponentCode") String rootComponentCode,
        @QueryParam("parentComponentCode") String parentComponentCode,
        @QueryParam("sidx") String sidx, // ソートキー
        @QueryParam("sord") String sord, // ソート順
        @QueryParam("pageNumber") int pageNumber, // ページNo
        @QueryParam("pageSize") int pageSize // ページSize
    ) {

        return mstComponentStructureService.getMstComponentStructureVoList(componentCode, rootComponentCode, parentComponentCode, sidx, sord, pageNumber, pageSize, true, 0);
    }

    /**
     *
     * 変更内容（追加、変更、削除）で部品構成テーブルを更新する。
     *
     * @param mstComponentStructureVos
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMstComponentStructureVoList(MstComponentStructureVoList mstComponentStructureVos) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentStructureService.postMstComponentStructure(mstComponentStructureVos, loginUser);
    }

    /**
     * 部品構成CSV出力
     *
     * @param componentCode
     * @param rootComponentCode
     * @param parentComponentCode
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMstComponentStructureCSV(
            @QueryParam("componentCode") String componentCode,
            @QueryParam("rootComponentCode") String rootComponentCode,
            @QueryParam("parentComponentCode") String parentComponentCode
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentStructureService.getMstComponentStructureCSV(componentCode, rootComponentCode, parentComponentCode, loginUser);
    }

    /**
     * 部品構成マスタ情報をCSV取込
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMstComponentStructureCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentStructureService.postMstComponentStructureCSV(fileUuid, loginUser);
    }

}
