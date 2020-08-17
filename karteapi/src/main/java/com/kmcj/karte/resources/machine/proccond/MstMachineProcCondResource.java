package com.kmcj.karte.resources.machine.proccond;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpec;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecList;
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
 * 設備加工条件マスタ
 *
 * @author admin
 */
@RequestScoped
@Path("machine/proccond")
public class MstMachineProcCondResource {

    public MstMachineProcCondResource() {

    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstMachineProcCondService mstMachineProcCondService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMachineProcCondsCsv(@QueryParam("machineId") String machineId,
            @QueryParam("machineType") String machineType, @QueryParam("componentId") String componentId,@QueryParam("procId") String procId,
            @QueryParam("externalFlg") String externalFlg) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMachineProcCondService.getMstMachineProcCondOutputCsv(machineType, machineId, componentId,procId, loginUser, externalFlg);
    }

    /**
     * 設備加工条件照会 画面描画 設備加工条件マスタ複数取得
     *
     * @param machineId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineProcCondList getMachineProcCondByMachineId(@QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMachineProcCondList list = mstMachineProcCondService.getMachineProcCondByMachineId(machineId, loginUser);

        MstMachineProcCondList mstMachineProcCondList = new MstMachineProcCondList();
        MstMachineProcCondVo machineProcCondVo;
        List<MstMachineProcCondVo> inputlist = new ArrayList<>();

        for (int i = 0; i < list.getMstMachineProcConds().size(); i++) {

            machineProcCondVo = new MstMachineProcCondVo();
            MstMachineProcCond in = list.getMstMachineProcConds().get(i);
            machineProcCondVo.setId(in.getId() == null ? "" : in.getId());
            machineProcCondVo.setMachineName(in.getMstMachine().getMachineName() == null ? "" : in.getMstMachine().getMachineName());
            inputlist.add(machineProcCondVo);

        }
        if (inputlist != null && inputlist.size() > 0) {
            mstMachineProcCondList.setMstMachineProcCondVos(inputlist);
        } else {
            mstMachineProcCondList.setError(true);
            mstMachineProcCondList.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstMachineProcCondList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
        }
        return mstMachineProcCondList;
    }

    /**
     * 設備加工条件照会 設備加工条件照会名称を変更の場合 設備加工条件マスタ複数取得
     *
     * @param machineId
     * @param componentId
     * @return
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineProcCondList getMachineProcCondByComponentId(@QueryParam("machineId") String machineId, @QueryParam("componentId") String componentId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMachineProcCondSpecList mstMachineProcCondSpecList = mstMachineProcCondService.getMachineProcCondByComponentId(machineId, componentId, loginUser);
        MstMachineProcCondList response = new MstMachineProcCondList();
        List<MstMachineProcCondVo> list = new ArrayList<>();

        MstMachineProcCondVo mstMachineProcConds;
        MstMachineProcCondSpec machineProcCondSpec;

        for (int i = 0; i < mstMachineProcCondSpecList.getMstMachineProcCondSpecs().size(); i++) {
            mstMachineProcConds = new MstMachineProcCondVo();
            machineProcCondSpec = new MstMachineProcCondSpec();
            machineProcCondSpec = mstMachineProcCondSpecList.getMstMachineProcCondSpecs().get(i);

            mstMachineProcConds.setAttrId(machineProcCondSpec.getMstMachineProcCondAttribute().getId());
            mstMachineProcConds.setAttrType(machineProcCondSpec.getMstMachineProcCondAttribute().getAttrType());
            mstMachineProcConds.setAttrValue(machineProcCondSpec.getAttrValue() == null ? "" : machineProcCondSpec.getAttrValue());
            mstMachineProcConds.setMachineProcCondAttributeCode(machineProcCondSpec.getMstMachineProcCondAttribute().getAttrCode());
            mstMachineProcConds.setMachineProcCondAttributeName(machineProcCondSpec.getMstMachineProcCondAttribute().getAttrName());
            mstMachineProcConds.setMachineProcCondId(machineProcCondSpec.getMstMachineProcCond().getId());

            list.add(mstMachineProcConds);
        }

        response.setMstMachineProcCondVos(list);
        return response;
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
    public MstMachineProcCondVo getMstMachineSpecHistoryNames(@PathParam("machineUuid") String machineUuid) {
        return mstMachineProcCondService.getMachineProcCondNamesByMachineUuid(machineUuid);
    }

    /**
     * バッチで設備加工条件マスタデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachineproccond")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineProcCondList getExtMachineProcCondsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return mstMachineProcCondService.getExtMachineProcCondsByBatch(latestExecutedDate, machineUuid);
    }
}
