package com.kmcj.karte.resources.machine.assetno;

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
 */
@RequestScoped
@Path("machine/assetno")
public class MstMachineAssetNoResource {

    public MstMachineAssetNoResource() {
    }

    @Inject
    private MstMachineAssetNoService mstMachineAsseNoService;

    @Context
    private ContainerRequestContext requestContext;
    
//    @Inject
//    private MstDictionaryService mstDictionaryService;

    /**
     *
     * 設備資産番号 設備資産番号マスタ取得
     *
     * @param machineId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineAssetNoList getMachineAssetNos(@QueryParam("machineId") String machineId) {
//        MstMachineAssetNoList response = new MstMachineAssetNoList();
//        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
//        FileUtil fu = new FileUtil();
//        if (null != machineId && !"".equals(machineId.trim()) && !fu.checkString(machineId)) {
//            response.setError(true);
//            response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_value_invalid"));
//            return response;
//        }
        MstMachineAssetNoList response = mstMachineAsseNoService.getMachineAssetNos(machineId);
        return response;
    }

    /**
     *
     * 設備資産番号 設備資産番号更新.追加・削除する
     *
     * @param mstMachineAssetNoList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineAssetNos(List<MstMachineAssetNoVo> mstMachineAssetNoList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMachineAsseNoService.postMachineAssetNos(mstMachineAssetNoList, loginUser);
    }

}
