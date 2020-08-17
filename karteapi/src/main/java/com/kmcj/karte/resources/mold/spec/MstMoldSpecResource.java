/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeService;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributes;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistory;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistoryList;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistorys;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
 * @author admin
 */
@RequestScoped
@Path("mold/spec")
public class MstMoldSpecResource {

    @Inject
    private MstMoldSpecService mstMstMoldSpecService;
    
    @Inject
    private MstMoldAttributeService mstMoldAttributeService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * 金型仕様マスタCSV出力
     *
     * @param moldId
     * @param moldType
     * @param moldSpecHstId
     * @param moldSpecHstName
     * @param externalFlg
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMoldSpecesCSV(@QueryParam("moldId") String moldId,
            @QueryParam("moldType") String moldType,
            @QueryParam("moldSpecHstId") String moldSpecHstId,
            @QueryParam("moldSpecHstName") String moldSpecHstName,
            @QueryParam("externalFlg")String externalFlg
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        moldId = FileUtil.getDecode(moldId);
        return mstMstMoldSpecService.getMstMoldSpecOutputCsv(moldType, moldId, moldSpecHstId, moldSpecHstName, loginUser,externalFlg);
    }

    /**
     * Tablet
     * QRコードを読み取る。 金型履歴名称を取得
     *
     * @param moldId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldSpecHistoryList getMoldSpecHistoryNameByMoldId(@QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMoldSpecHistoryList mstMoldSpecHistoryList = mstMstMoldSpecService.getMoldSpecHistoryNameByMoldId(moldId, loginUser);
        MstMoldSpecHistorys mstMoldSpecHistorys;
        MstMoldSpecHistoryList response = new MstMoldSpecHistoryList();
        List<MstMoldSpecHistorys> mstMoldSpecHistorysList = new ArrayList<>();
        if (null == mstMoldSpecHistoryList || null == mstMoldSpecHistoryList.getMstMoldSpecHistory() || mstMoldSpecHistoryList.getMstMoldSpecHistory().isEmpty()) {
//            mstMoldSpecHistorys = new MstMoldSpecHistorys();
//            mstMoldSpecHistorys.setMoldSpecName("test");
//            mstMoldSpecHistorysList.add(mstMoldSpecHistorys);
        } else {
            for (int i = 0; i < mstMoldSpecHistoryList.getMstMoldSpecHistory().size(); i++) {
                MstMoldSpecHistory mstMoldSpecHistory = mstMoldSpecHistoryList.getMstMoldSpecHistory().get(i);
                mstMoldSpecHistorys = new MstMoldSpecHistorys();
                mstMoldSpecHistorys.setId(mstMoldSpecHistory.getId());
                mstMoldSpecHistorys.setMoldSpecName(mstMoldSpecHistory.getMoldSpecName());
                mstMoldSpecHistorys.setMoldName(mstMoldSpecHistory.getMstMold().getMoldName());
                mstMoldSpecHistorysList.add(mstMoldSpecHistorys);
            }
        }
        
        response.setMstMoldSpecHistorys(mstMoldSpecHistorysList);
        return response;
    }

    /**
     * Tablet QRコードを読み取る。
     * 金型名称を変更場合 金型仕様マスタ複数取得
     *
     * @param moldId
     * @param moldSpecName
     * @return
     */
    @GET
    @Path("details")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldSpecList getMoldSpeceDetails(@QueryParam("moldId") String moldId, @QueryParam("moldSpecName") String moldSpecName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMoldSpecList response = new MstMoldSpecList();
        List<MstMoldSpecDeatil> mstMoldSpecDeatilList = new ArrayList<>();
        MstMoldSpecDeatil mstMoldSpecDeatil;
        //2016-12-19 16:02:26 jiangxiaosong update
        List<MstMoldAttributes> resMoldAttributes = mstMoldAttributeService.getMstMoldAttributesByType("", moldId, moldSpecName,"", loginUser,"").getMstMoldAttributes();
        for (int i = 0; i < resMoldAttributes.size(); i++) {
            MstMoldAttributes aMstMoldAttributes = resMoldAttributes.get(i);
            mstMoldSpecDeatil = new MstMoldSpecDeatil();
            mstMoldSpecDeatil.setMoldAttrbuteCode(aMstMoldAttributes.getAttrCode());
            mstMoldSpecDeatil.setMoldAttrbuteName(aMstMoldAttributes.getAttrName());
            mstMoldSpecDeatil.setMoldAttrbuteType(aMstMoldAttributes.getAttrType());
            mstMoldSpecDeatil.setAttrValue(aMstMoldAttributes.getAttrValue());
            mstMoldSpecDeatil.setAttrId(aMstMoldAttributes.getAttrId());
            mstMoldSpecDeatilList.add(mstMoldSpecDeatil);
        }
        
        response.setMstMoldSpecDeatil(mstMoldSpecDeatilList);        
        return response;
    }
    
    /**
     * バッチで金型仕様マスタデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldspec")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldSpecList getExtMoldSpecsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return mstMstMoldSpecService.getExtMoldSpecsByBatch(latestExecutedDate, moldUuid);
    }
}
