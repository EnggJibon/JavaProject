package com.kmcj.karte.resources.machine.proccond.spec;

import com.kmcj.karte.CountResponse;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jiangxs
 */
@RequestScoped
@Path("machine/proccond/spec")
public class MstMachineProcCondSpecResource {

    public MstMachineProcCondSpecResource() {
    }

    @Inject
    private MstMachineProcCondSpecService mstMachineProcCondSpecService;

    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("machineId") String machineId) {
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(12);//TODO
        return countResponse;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getMstMachineProcCondSpecs(@QueryParam("machineId") String machineId) {
        CountResponse countResponse = new CountResponse();
        countResponse.setCount(12);//TODO
        return countResponse;
    }

    /**
     * バッチで設備加工条件仕様マスタデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachineproccondspec")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineProcCondSpecList getExtMachineProcCondSpecsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return mstMachineProcCondSpecService.getExtMachineProcCondSpecsByBatch(latestExecutedDate, machineUuid);
    }
}
