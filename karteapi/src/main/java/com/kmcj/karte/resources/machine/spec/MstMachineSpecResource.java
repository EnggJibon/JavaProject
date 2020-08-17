package com.kmcj.karte.resources.machine.spec;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeService;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeVo;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistory;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryList;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryVo;
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
@Path("machine/spec")
public class MstMachineSpecResource {

    @Inject
    private MstMachineSpecService mstMstMachineSpecService;

    @Inject
    private MstMachineAttributeService mstMachineAttributeService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * 設備仕様マスタCSV出力
     *
     * @param machineId
     * @param machineType
     * @param machineSpecHstId
     * @param machineSpecHstName
     * @param externalFlg
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMachineSpecesCSV(@QueryParam("machineId") String machineId,
            @QueryParam("machineType") String machineType,
            @QueryParam("machineSpecHstId") String machineSpecHstId,
            @QueryParam("machineSpecHstName") String machineSpecHstName,
            @QueryParam("externalFlg") String externalFlg
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMstMachineSpecService.getMstMachineSpecOutputCsv(machineType, machineId, machineSpecHstId, machineSpecHstName, loginUser, externalFlg);
    }

    /**
     * Tablet QRコードを読み取る。 設備履歴名称を取得
     *
     * @param machineId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineSpecHistoryList getMachineSpecHistoryNameByMachineId(@QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMachineSpecHistoryList mstMachineSpecHistoryList = mstMstMachineSpecService.getMachineSpecHistoryNameByMachineId(machineId, loginUser);
        MstMachineSpecHistoryVo mstMachineSpecHistorys;
        MstMachineSpecHistoryList response = new MstMachineSpecHistoryList();
        List<MstMachineSpecHistoryVo> mstMachineSpecHistorysList = new ArrayList<>();
        if (null == mstMachineSpecHistoryList || null == mstMachineSpecHistoryList.getMstMachineSpecHistorys() || mstMachineSpecHistoryList.getMstMachineSpecHistorys().isEmpty()) {
            // nothing
        } else {
            for (int i = 0; i < mstMachineSpecHistoryList.getMstMachineSpecHistorys().size(); i++) {
                MstMachineSpecHistory mstMachineSpecHistory = mstMachineSpecHistoryList.getMstMachineSpecHistorys().get(i);
                mstMachineSpecHistorys = new MstMachineSpecHistoryVo();
                mstMachineSpecHistorys.setId(mstMachineSpecHistory.getId());
                mstMachineSpecHistorys.setMachineSpecName(mstMachineSpecHistory.getMachineSpecName());

                if (mstMachineSpecHistory.getMstMachine() != null) {
                    mstMachineSpecHistorys.setMachineName(mstMachineSpecHistory.getMstMachine().getMachineName());
                    mstMachineSpecHistorys.setInstllationSiteName(mstMachineSpecHistory.getMstMachine().getInstllationSiteName());
                } else {
                    mstMachineSpecHistorys.setMachineName("");
                    mstMachineSpecHistorys.setInstllationSiteName("");
                }
                mstMachineSpecHistorysList.add(mstMachineSpecHistorys);
            }
        }

        response.setMstMachineSpecHistoryVos(mstMachineSpecHistorysList);
        return response;
    }

    /**
     * Tablet QRコードを読み取る。 設備名称を変更場合 設備仕様マスタ複数取得
     *
     * @param machineId
     * @param machineSpecName
     * @return
     */
    @GET
    @Path("details")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineSpecList getMachineSpeceDetails(@QueryParam("machineId") String machineId, @QueryParam("machineSpecName") String machineSpecName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMachineSpecList response = new MstMachineSpecList();
        List<MstMachineSpecVo> mstMachineSpecDeatilList = new ArrayList<>();
        MstMachineSpecVo mstMachineSpecDeatil;
        //2016-12-19 16:02:26 jiangxiaosong update
        List<MstMachineAttributeVo> resMachineAttributes = mstMachineAttributeService.getMstMachineAttributesByType("", machineId, machineSpecName,"", loginUser, "").getMstMachineAttributeVos();
        for (int i = 0; i < resMachineAttributes.size(); i++) {
            MstMachineAttributeVo aMstMachineAttributes = resMachineAttributes.get(i);
            mstMachineSpecDeatil = new MstMachineSpecVo();
            mstMachineSpecDeatil.setMachineAttrbuteCode(aMstMachineAttributes.getAttrCode());
            mstMachineSpecDeatil.setMachineAttrbuteName(aMstMachineAttributes.getAttrName());
            mstMachineSpecDeatil.setMachineAttrbuteType(aMstMachineAttributes.getAttrType());
            mstMachineSpecDeatil.setAttrValue(aMstMachineAttributes.getAttrValue());
            mstMachineSpecDeatil.setAttrId(aMstMachineAttributes.getAttrId());
            mstMachineSpecDeatilList.add(mstMachineSpecDeatil);
        }

        response.setMstMachineSpecVos(mstMachineSpecDeatilList);
        return response;
    }

    /**
     * バッチで設備仕様マスタデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachinespec")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineSpecList getExtMachineSpecsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return mstMstMachineSpecService.getExtMachineSpecsByBatch(latestExecutedDate, machineUuid);
    }
}
