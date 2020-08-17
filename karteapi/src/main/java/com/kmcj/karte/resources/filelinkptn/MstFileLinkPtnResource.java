/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.filelinkptn;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.ArrayList;
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
 * ファイルリンクパターンマスタの処理
 *
 * @author admin
 */
@RequestScoped
@Path("filelinkptn")
public class MstFileLinkPtnResource {

    public MstFileLinkPtnResource() {
    }
    @Inject
    private MstFileLinkPtnService mstFileLinkPtnService;

    @Context
    private ContainerRequestContext requestContext;


    /**
     * ファイルリンクパターンマスタ複数取得
     * @param purpose
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstFileLinkPtnList getFileLinkPtnes(@QueryParam("purpose") String purpose) {
        
       MstFileLinkPtnList mstFileLinkPtnList = mstFileLinkPtnService.getMstFileLinkPtnes(purpose);
       List<MstFileLinkPtnes> mstFileLinkPtnesList = new ArrayList<MstFileLinkPtnes>();
       MstFileLinkPtnList response = new MstFileLinkPtnList();
       MstFileLinkPtnes mstFileLinkPtnes;
       
       for(int i= 0; i<mstFileLinkPtnList.getMstFileLinkPtnes().size();i++){
           MstFileLinkPtn mstFileLinkPtn = mstFileLinkPtnList.getMstFileLinkPtnes().get(i);
           mstFileLinkPtnes = new MstFileLinkPtnes();
           mstFileLinkPtnes.setId(mstFileLinkPtn.getId());
           mstFileLinkPtnes.setFileLinkPtnName(mstFileLinkPtn.getFileLinkPtnName());
           mstFileLinkPtnes.setLinkString(mstFileLinkPtn.getLinkString());
           mstFileLinkPtnes.setPurpose(mstFileLinkPtn.getPurpose());
           mstFileLinkPtnesList.add(mstFileLinkPtnes);
       }
       response.setMstFileLinkPtnesList(mstFileLinkPtnesList);
       return response;

    }

    /**
     * ファイルリンクパターンマスタ追加・更新・削除
     *
     * @param mstFileLinkPtnList
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postFileLinkPtnes(List<MstFileLinkPtnes> mstFileLinkPtnList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstFileLinkPtnService.getNewMstFileLinkPtnes(mstFileLinkPtnList, loginUser);
    }

    /**
     * ファイルリンクパターン複数取得
     *
     * @param id
     * @return an instance of MstFileLinkPtnList
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstFileLinkPtnList getFileLinkPtnesById(@QueryParam("id") String id) {
        return mstFileLinkPtnService.getMstFileLinkPtnesById(id);
    }
}
