/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.spec.history;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtn;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttribute;
import com.kmcj.karte.resources.machine.spec.MstMachineSpec;
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
@Path("machine/spec/history")
public class MstMachineSpecHistoryResource {

    @Inject
    private MstMachineSpecHistoryService mstMachineSpecHistoryService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstChoiceService mstChoiceService;

    public MstMachineSpecHistoryResource() {
    }

    /**
     *
     * @param machineUuid
     * @return
     */
    @GET
    @Path("names/{machineUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineSpecHistoryVo getMstMachineSpecHistoryNames(@PathParam("machineUuid") String machineUuid) {
        return mstMachineSpecHistoryService.getMachineSpecHistoryNamesByMachineUuid(machineUuid);
    }

    /**
     * T0004_新仕様登録画面_画面描画時
     *
     * @param machineId
     * @param machineName
     * @return
     */
    @GET
    @Path("getlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineSpecHistoryVo getMachineSpecHistoryList(@QueryParam("machineId") String machineId, @QueryParam("machineName") String machineName) {
        MstMachineSpecHistoryVo response = new MstMachineSpecHistoryVo();
        List<MstMachineSpecHistoryVo> mstMachineSpecHistorysList = new ArrayList<>();
        MstMachineSpecHistory mstMachineSpecHistory = mstMachineSpecHistoryService.getMachineSpecHistoryList(machineId);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (mstMachineSpecHistory != null) {
            StringBuilder sql = new StringBuilder("SELECT m1 FROM MstMachineSpec m1 LEFT JOIN FETCH MstMachineAttribute m ON m.id = m1.mstMachineSpecPK.attrId WHERE ");
            sql.append( "m1.mstMachineSpecPK.machineSpecHstId = :machineSpecHstId and m.machineType = :machineType and m.externalFlg = :externalFlg order by m.seq ");
            Query getCsvApec = entityManager.createQuery(sql.toString());
            getCsvApec.setParameter("machineSpecHstId", mstMachineSpecHistory.getId());
            if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", loginUser).isError() == true) {
                getCsvApec.setParameter("externalFlg", CommonConstants.EXTERNALFLG);
            } else {
                getCsvApec.setParameter("externalFlg", CommonConstants.MINEFLAG);
            }
            getCsvApec.setParameter("machineType", mstMachineSpecHistory.getMstMachine().getMachineType());
            
            List specList = getCsvApec.getResultList();
            MstMachineSpecHistoryVo out;
            response.setMachineSpecHstId(mstMachineSpecHistory.getId());
            if (specList != null && specList.size() > 0) {
                for (int j = 0; j < specList.size(); j++) {
                    out = new MstMachineSpecHistoryVo();
                    MstMachineSpec csvMstMachineSpec = (MstMachineSpec) specList.get(j);
                    if (csvMstMachineSpec != null) {
                        MstMachineAttribute mstMachineAttribute = csvMstMachineSpec.getMstMachineAttribute();
                        if (mstMachineAttribute != null) {
                            out.setAttrCode(mstMachineAttribute.getAttrCode());
                            out.setAttrId(mstMachineAttribute.getId());
                            out.setAttrName(mstMachineAttribute.getAttrName());
                            out.setAttrType(mstMachineAttribute.getAttrType());
                            if (mstMachineAttribute.getAttrType() == 5) {
                                MstFileLinkPtn mstFileLinkPtn = mstMachineAttribute.getMstFileLinkPtn();
                                MstMachine mstMachine = mstMachineSpecHistory.getMstMachine();
                                String tmpLnk = "";
                                if (mstMachine != null) {
                                    if (mstFileLinkPtn != null) {
                                        tmpLnk = mstFileLinkPtn.getLinkString();
                                    }
                                    String strMachineId = mstMachine.getMachineId();
                                    String strMachineName = mstMachine.getMachineName();
                                    String strMachineType;
                                    String strMachineTypeName = "";
                                    if (mstMachineAttribute.getAttrType() != null) {
                                        strMachineType = String.valueOf(mstMachineAttribute.getAttrType());
                                    } else {
                                        strMachineType = "";
                                    }
                                    String strMainAssetNo = mstMachine.getMainAssetNo();
                                    out.setAttrValue(FileUtil.getFileLinkString(mstChoiceService, loginUser.getLangId(), tmpLnk, strMachineId, strMachineName, strMachineType, strMainAssetNo));
                                }
                            } else {
                                out.setAttrValue(csvMstMachineSpec.getAttrValue());
                            }

                        }
                    }
                    mstMachineSpecHistorysList.add(out);
                }
            }
        }
        response.setMstMachineSpecHistoryVos(mstMachineSpecHistorysList);
        return response;
    }

    /**
     * バッチで設備マスタデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachinespechistory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineSpecHistoryList getExtMachineSpecHistorysByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return mstMachineSpecHistoryService.getExtMachineSpecHistorysByBatch(latestExecutedDate, machineUuid);
    }
}
