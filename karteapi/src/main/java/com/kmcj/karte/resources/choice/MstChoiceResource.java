/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice;

import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@Path("choice")
@RequestScoped
public class MstChoiceResource {

    @Context
    private UriInfo context;
    @Inject
    private MstChoiceService mstChoiceService;
    @Context
    private ContainerRequestContext requestContext;

    /**
     * Creates a new instance of MstChoiceResource
     */
    public MstChoiceResource() {
    }

    /**
     * @param category
     * @param langId
     * @return an instance of java.lang.String
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstChoiceList getChoices(@QueryParam("category") String category, @QueryParam("langId") String langId, @QueryParam("seq") String seq) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (langId == null) {
            langId = loginUser.getLangId();
        }
        MstChoice choice = mstChoiceService.getBySeqChoice(seq, langId, category);
        MstChoiceList response = new MstChoiceList();
        ArrayList<MstChoice> list = new ArrayList<>();
        if (choice != null) {
            list.add(choice);
        }
        response.setMstChoice(list);
        return response;
    }

    /**
     * @param category
     * @param langId
     * @return an instance of java.lang.String
     */
    @GET
    @Path("getlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstChoiceList getChoices(@QueryParam("category") String category, @QueryParam("langId") String langId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (langId == null) {
            langId = loginUser.getLangId();
        }

        return mstChoiceService.getChoice(langId, category);
    }

    /**
     * PUT method for updating or creating an instance of MstChoiceResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    /**
     * categoryを取得
     *
     * @param langId
     * @param category
     * @param parentSeq
     * @return
     */
    @GET
    @Path("getcategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstChoiceList getCategories(@QueryParam("langId") String langId, @QueryParam("category") String category, @QueryParam("parentSeq") String parentSeq) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstChoiceList list = mstChoiceService.getCategories(langId, category, parentSeq, loginUser);
        return list;
    }

    /**
     * S0011 分類項目設定 選択された分類項目に上位分類項目(parent_category)があるときは、上位分類項目の区分値を取得し
     *
     * @param langId
     * @param category
     * @return
     */
    @GET
    @Path("parentseq")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstChoiceVo getParentSeq4combo(@QueryParam("langId") String langId, @QueryParam("category") String category) {
        MstChoiceVo resVo = new MstChoiceVo();
        if (null != category && !category.trim().equals("")) {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            MstChoiceList list = mstChoiceService.getChoice(null == langId || langId.equals("") ? loginUser.getLangId() : langId, category);
            if (null != list.getMstChoice() && !list.getMstChoice().isEmpty()) {
                List<MstChoice> mstChoices = list.getMstChoice();
                List<MstChoiceVo> vos = new ArrayList<>();
                for (int i = 0; i < mstChoices.size(); i++) {
                    MstChoice choice = mstChoices.get(i);
                    MstChoiceVo vo = new MstChoiceVo();
                    vo.setSeq(choice.getMstChoicePK().getSeq());
                    vo.setChoice(choice.getChoice());
                    vos.add(vo);
                }
                resVo.setChoiceVo(vos);
            }
        }

        return resVo;
    }
    
    /**
     * バッチで選択肢マスタデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extchoice")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstChoiceVo getExtChoicesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstChoiceService.getExtChoicesByBatch(latestExecutedDate);
    }
}
