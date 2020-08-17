/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.item;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jiangxs
 */
@RequestScoped
@Path("mold/inspection/item")
public class MstMoldInspectionItemResource {

    public MstMoldInspectionItemResource() {

    }

    @Inject
    private MstMoldInspectionItemService mstMoldInspectionItemService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * M0016 点検項目設定 画面描画時 点検項目マスタより作業大分類、作業中分類、作業小分類を集約して取得し、左側の作業分類一覧に表示 *
     *
     * @param taskCategory1
     * @param taskCategory2
     * @param taskCategory3
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldInspectionItemVo getInspectionItems(@QueryParam("taskCategory1") String taskCategory1,
            @QueryParam("taskCategory2") String taskCategory2,
            @QueryParam("taskCategory3") String taskCategory3) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldInspectionItemService.getInspectionItems(taskCategory1, taskCategory2, taskCategory3, loginUser);
    }

    /**
     * M0016
     *
     * @param moldInspectionItemVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postInspectionItems(MstMoldInspectionItemVo moldInspectionItemVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldInspectionItemService.postInspectionItems(moldInspectionItemVo, loginUser);
    }
    
    /**
     * バッチで金型点検項目マスタデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extmoldinspectionitem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldInspectionItemList getExtMoldInspectionItemsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstMoldInspectionItemService.getExtMoldInspectionItemsByBatch(latestExecutedDate);
    }
}
