package com.kmcj.karte.resources.common.gridcolumn;

import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.inspection.result.model.ComponentInspectionDetailColumnResponse;
import com.kmcj.karte.resources.component.inspection.result.model.TblGridColumnMemory;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * グリッドの列幅・列順を取得、保存する。
 *
 * @author t.takasaki
 */
@Path("gridcolumn")
@RequestScoped
public class GridcolumnMemoryResource {

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private GridcolumnMemoryService service;

    /**
     * ログインユーザが保持している指定のグリッドの列情報を取得する。
     * @param screenId
     * @param gridId
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionDetailColumnResponse getGridColumnMemory(@QueryParam("screenId") String screenId, @QueryParam("gridId") String gridId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        ComponentInspectionDetailColumnResponse resp = new ComponentInspectionDetailColumnResponse();
        resp.setListInspectionDetailColumn(service.getGridColumnMemory(loginUser.getUserUuid(), screenId, gridId));
        return resp;
    }

    /**
     * ログインユーザでグリッドの列情報を保存する。
     * @param gridCols 
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void mergeGridColumnMemory(List<TblGridColumnMemory> gridCols) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        service.mergeGridColumnMemory(loginUser.getUserUuid(), gridCols);
    }
}
