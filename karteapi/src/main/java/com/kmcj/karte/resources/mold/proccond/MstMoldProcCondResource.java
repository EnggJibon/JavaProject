/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpec;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecList;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 金型加工条件マスタ
 *
 * @author admin
 */
@RequestScoped
@Path("mold/proccond")
public class MstMoldProcCondResource {

    public MstMoldProcCondResource() {

    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstMoldProcCondService mstMoldProcCondService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMoldProcCondsCsv(@QueryParam("moldId") String moldId,
            @QueryParam("moldType") String moldType, @QueryParam("moldProcCondName") String moldProcCondName,
            @QueryParam("externalFlg")String externalFlg) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldProcCondService.getMstMoldProcCondOutputCsv(moldType, moldId, moldProcCondName, loginUser,externalFlg);
    }

    /**
     * 金型加工条件照会 画面描画 金型加工条件マスタ複数取得
     *
     * @param moldId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldProcCondList getMoldProcCondByMoldId(@QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMoldProcCondList list = mstMoldProcCondService.getMoldProcCondByMoldId(moldId, loginUser);

        MstMoldProcCondList mstMoldProcCondList = new MstMoldProcCondList();
        MstMoldProcConds mstMoldProcConds;
        List<MstMoldProcConds> inputlist = new ArrayList<>();

        for (int i = 0; i < list.getMstMoldProcCondList().size(); i++) {

            mstMoldProcConds = new MstMoldProcConds();
            MstMoldProcCond in = list.getMstMoldProcCondList().get(i);
            mstMoldProcConds.setMoldProcCondName(in.getMoldProcCondName() == null ? "" : in.getMoldProcCondName());
            mstMoldProcConds.setId(in.getId() == null ? "" : in.getId());
            mstMoldProcConds.setMoldName(in.getMstMold().getMoldName() == null ? "" : in.getMstMold().getMoldName());
            inputlist.add(mstMoldProcConds);

        }
        if (inputlist != null && inputlist.size() > 0) {
            mstMoldProcCondList.setMstMoldProcConds(inputlist);
        } else {
            mstMoldProcCondList.setError(true);
            mstMoldProcCondList.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstMoldProcCondList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
        }
        return mstMoldProcCondList;
    }

    /**
     * 金型加工条件照会 金型加工条件照会名称を変更の場合 金型加工条件マスタ複数取得
     *
     * @param moldId
     * @param moldProcCondName
     * @return
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldProcCondList getMoldProcCondByMoldName(@QueryParam("moldId") String moldId, @QueryParam("moldProcCondName") String moldProcCondName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMoldProcCondSpecList mstMoldProcCondSpecList = mstMoldProcCondService.getMoldProcCondByMoldName(moldId, moldProcCondName, loginUser);
        MstMoldProcCondList response = new MstMoldProcCondList();
        List<MstMoldProcConds> list = new ArrayList<>();

        MstMoldProcConds mstMoldProcConds;
        MstMoldProcCondSpec mstMoldProcCondSpec;

        for (int i = 0; i < mstMoldProcCondSpecList.getMstMoldProcCondSpecList().size(); i++) {
            mstMoldProcConds = new MstMoldProcConds();
            mstMoldProcCondSpec = new MstMoldProcCondSpec();
            mstMoldProcCondSpec = mstMoldProcCondSpecList.getMstMoldProcCondSpecList().get(i);

            mstMoldProcConds.setAttrId(mstMoldProcCondSpec.getMstMoldProcCondAttribute().getId());
            mstMoldProcConds.setAttrType(mstMoldProcCondSpec.getMstMoldProcCondAttribute().getAttrType());
            mstMoldProcConds.setAttrValue(mstMoldProcCondSpec.getAttrValue() == null ? "" : mstMoldProcCondSpec.getAttrValue());
            mstMoldProcConds.setMoldProcCondAttributeCode(mstMoldProcCondSpec.getMstMoldProcCondAttribute().getAttrCode());
            mstMoldProcConds.setMoldProcCondAttributeName(mstMoldProcCondSpec.getMstMoldProcCondAttribute().getAttrName());
            mstMoldProcConds.setMoldProcCondId(mstMoldProcCondSpec.getMstMoldProcCond().getId());
            mstMoldProcConds.setMoldProcCondName(mstMoldProcCondSpec.getMstMoldProcCond().getMoldProcCondName());

            list.add(mstMoldProcConds);
        }

        response.setMstMoldProcConds(list);
        return response;
    }

    /**
     *
     * @param moldUuid
     * @return
     */
    @GET
    @Path("names/{moldUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldProcConds getMstMoldSpecHistoryNames(@PathParam("moldUuid") String moldUuid) {
        return mstMoldProcCondService.getMoldProcCondNamesByMoldUuid(moldUuid);
    }
    
    /**
     * バッチで金型加工条件マスタデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldproccond")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldProcCondList getExtMoldProcCondsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return mstMoldProcCondService.getExtMoldProcCondsByBatch(latestExecutedDate, moldUuid);
    }
}
