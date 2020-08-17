/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec.history;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtn;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttribute;
import com.kmcj.karte.resources.mold.spec.MstMoldSpec;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
 *
 * @author admin
 */
@RequestScoped
@Path("mold/spec/history")
public class MstMoldSpecHistoryResource {
    
    @Inject
    private MstMoldSpecHistoryService mstMoldSpecHistoryService;   
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    public MstMoldSpecHistoryResource() {
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
    public MstMoldSpecHistorys getMstMoldSpecHistoryNames(@PathParam("moldUuid") String moldUuid) {
        return mstMoldSpecHistoryService.getMoldSpecHistoryNamesByMoldUuid(moldUuid);
    }
    

    /**
     * T0004_新仕様登録画面_画面描画時
     * @param moldId
     * @param moldName
     * @return 
     */
    @GET
    @Path("getlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldSpecHistorys getMoldSpecHistoryList(@QueryParam("moldId") String moldId, @QueryParam("moldName") String moldName) {
        MstMoldSpecHistorys response = new MstMoldSpecHistorys();
        List<MstMoldSpecHistorys> mstMoldSpecHistorysList = new ArrayList<>();
        MstMoldSpecHistory mstMoldSpecHistory = mstMoldSpecHistoryService.getMoldSpecHistoryList(moldId);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (mstMoldSpecHistory != null) {
            Query getCsvApec = entityManager.createNamedQuery("MstMoldSpec.findByMoldListSpec");
            getCsvApec.setParameter("moldSpecHstId", mstMoldSpecHistory.getId());
            if (FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser).isError() == true) {
                getCsvApec.setParameter("externalFlg", CommonConstants.EXTERNALFLG);
            } else {
                getCsvApec.setParameter("externalFlg", CommonConstants.MINEFLAG);
            }
            getCsvApec.setParameter("moldType", mstMoldSpecHistory.getMstMold().getMoldType());
            
            List specList = getCsvApec.getResultList();
            MstMoldSpecHistorys out;
            response.setMoldSpecHstId(mstMoldSpecHistory.getId());
            if (specList != null && specList.size() > 0) {
                for (int j = 0; j < specList.size(); j++) {
                    out = new MstMoldSpecHistorys();
                    MstMoldSpec csvMstMoldSpec = (MstMoldSpec) specList.get(j);
                    if(csvMstMoldSpec != null) {
                        MstMoldAttribute mstMoldAttribute = csvMstMoldSpec.getMstMoldAttribute();
                        if(mstMoldAttribute != null) {
                            out.setAttrCode(mstMoldAttribute.getAttrCode());
                            out.setAttrId(mstMoldAttribute.getId());
                            out.setAttrName(mstMoldAttribute.getAttrName());
                            out.setAttrType(mstMoldAttribute.getAttrType());
                            if(mstMoldAttribute.getAttrType() == 5) {
                                MstFileLinkPtn mstFileLinkPtn = mstMoldAttribute.getMstFileLinkPtn_MoldAttr();
                                MstMold mstMold = mstMoldSpecHistory.getMstMold();
                                String tmpLnk = "";
                                if (mstMold != null) {
                                    if(mstFileLinkPtn != null){
                                        tmpLnk = mstFileLinkPtn.getLinkString();
                                    }
                                    String strMoldId = mstMold.getMoldId();
                                    String strMoldName = mstMold.getMoldName();
                                    String strMoldType;
                                    String strMoldTypeName = "";
                                    if (mstMoldAttribute.getAttrType() != null) {
                                        strMoldType = String.valueOf(mstMoldAttribute.getAttrType());
                                    } else {
                                        strMoldType = "";
                                    }
                                    String strMainAssetNo = mstMold.getMainAssetNo();
                                    out.setAttrValue(FileUtil.getFileLinkString(mstChoiceService,loginUser.getLangId(),tmpLnk,strMoldId,strMoldName,strMoldType,strMainAssetNo));
                                }
                            } else {
                                out.setAttrValue(csvMstMoldSpec.getAttrValue());
                            }
                            
                        }
                    }
                    mstMoldSpecHistorysList.add(out);
                }
            }
        }
        response.setMoldSpecHistoryVos(mstMoldSpecHistorysList);
        return response;
    }
    
    /**
     * バッチで金型マスタデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldspechistory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldSpecHistoryList getExtMoldSpecHistorysByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return mstMoldSpecHistoryService.getExtMoldSpecHistorysByBatch(latestExecutedDate, moldUuid);
    }
}
