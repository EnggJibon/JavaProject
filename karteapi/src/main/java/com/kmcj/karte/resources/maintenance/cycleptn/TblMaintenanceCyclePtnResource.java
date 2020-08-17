package com.kmcj.karte.resources.maintenance.cycleptn;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachineVo;
import com.kmcj.karte.resources.mold.MstMoldDetail;
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
 *
 * @author admin
 */
@RequestScoped
@Path("maintenance/cycle/ptn")
public class TblMaintenanceCyclePtnResource {

    @Inject
    private TblMaintenanceCyclePtnService tblMaintenanceCyclePtnService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * メンテナンスサイクルパターン登録　検索
     *
     * @param type
     * @param cycleCode
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMaintenanceCyclePtnList getTblMaintenanceCyclePtnList(@QueryParam("type") String type, @QueryParam("cycleCode") String cycleCode) {

        return tblMaintenanceCyclePtnService.getTblMaintenanceCyclePtnList(type, cycleCode);
    }

    /**
     * メンテナンスサイクルパターン登録　保存
     *
     * @param tblMaintenanceCyclePtnList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMaintenanceCyclePtnList saveTblMaintenanceCyclePtnList(TblMaintenanceCyclePtnList tblMaintenanceCyclePtnList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblMaintenanceCyclePtnService.saveTblMaintenanceCyclePtnList(tblMaintenanceCyclePtnList, loginUser);
    }

    /**
     * 金型メンテナンス/アラートチェック
     * このメソッドが使っていないようです　保留
     * @param mstMoldDetail
     * @return
     */
    @POST
    @Path("checkmoldmainte")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse chkMoldMainte(MstMoldDetail mstMoldDetail) {

        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        List<MstMoldDetail> mstMoldDetailList = new ArrayList();
        mstMoldDetailList.add(mstMoldDetail);

        tblMaintenanceCyclePtnService.chkMoldMainte(mstMoldDetailList);

        int msgType = mstMoldDetailList.get(0).getMsgFlg();

        if (1 == msgType) {

            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_maintenance_time");
            msg = String.format(msg, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold"));
            response.setErrorMessage(msg);

        } else if (2 == msgType) {

            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_maintenance_time_expired");
            msg = String.format(msg, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold"));
            response.setErrorMessage(msg);

        }

        return response;
    }
    
    /**
     * 設備メンテナンス/アラートチェック
     * このメソッドが使っていないようです,保留
     * @param mstMachineVo
     * @return
     */
    @POST
    @Path("checkmachinemainte")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse chkMachineMainte(MstMachineVo mstMachineVo) {

        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        List<MstMachineVo> mstMachineVoList = new ArrayList();
        mstMachineVoList.add(mstMachineVo);

        tblMaintenanceCyclePtnService.chkMachineMainte(mstMachineVoList);

        int msgType = mstMachineVoList.get(0).getMsgFlg();

        if (1 == msgType) {

            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_maintenance_time");
            msg = String.format(msg, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine"));
            response.setErrorMessage(msg);

        } else if (2 == msgType) {

            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_maintenance_time_expired");
            msg = String.format(msg, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine"));
            response.setErrorMessage(msg);

        }

        return response;
    }

}
