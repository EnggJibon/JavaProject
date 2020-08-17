package com.kmcj.karte.resources.machine.inspection.choice;

import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
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
 * @author jiangxs
 */
@RequestScoped
@Path("machine/inspection/choice")
public class MstMachineInspectionChoiceResource {

    public MstMachineInspectionChoiceResource() {

    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstMachineInspectionChoiceService mstMachineInspectionChoiceService;

    /**
     * 点検項目設定
     * 選択された点検項目の結果種別が、任意選択式のとき、点検項目選択肢マスタより該当の結果値を取得し、右側の点検項目選択肢一覧に連番順に表示する
     *
     * @param inspectionItemId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineInspectionChoiceVo getInspectionChoicesByItem(@QueryParam("inspectionItemId") String inspectionItemId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMachineInspectionChoiceService.getInspectionChoicesByItemId(inspectionItemId, loginUser);
    }
    
    /**
     * バッチで設備点検項目選択肢マスタデータを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extmachineinspectionchoice")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineInspectionChoiceVo getExtMachineInspectionChoicesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return mstMachineInspectionChoiceService.getExtMachineInspectionChoicesByBatch(latestExecutedDate);
    }
}
