package com.kmcj.karte.resources.component.inspection.defect;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.DateFormat;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author penggd
 */
@RequestScoped
@Path("component/inspection/defect")
public class TblComponentInspectionDefectResource {
    
    @Inject
    private TblComponentInspectionDefectService tblComponentInspectionDefectService;
    
    @Context
    private ContainerRequestContext requestContext;
    
    /**
     * 部品検査不具合情報を取得
     *
     * @param componentInspectionResultId
     * @return TblComponentInspectionDefectList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentInspectionDefectList getTblComponentInspectionDefectList(@QueryParam("componentInspectionResultId") String componentInspectionResultId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentInspectionDefectService.getTblComponentInspectionDefectList(componentInspectionResultId, loginUser.getLangId());
    }
    
    /**
     * 部品検査不具合情報の保存
     *
     * @param tblComponentInspectionDefectList
     * @return TblComponentInspectionDefectList
     */
    @PUT
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentInspectionDefectList saveTblComponentInspectionDefect(TblComponentInspectionDefectList tblComponentInspectionDefectList) {
        return tblComponentInspectionDefectService.saveTblComponentInspectionDefect(tblComponentInspectionDefectList);
    }
    
    /**
     * 部品検査不具合情報の削除
     *
     * @param id
     * @return TblComponentInspectionDefectList
     */
    @DELETE
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse delTblComponentInspectionDefect(@QueryParam("id") String id) {
        return tblComponentInspectionDefectService.delTblComponentInspectionDefect(id);
    }
    
     /**
     * 部品検査不具合のパレート表示用の情報を取得
     *
     * @param incomingCompanyId
     * @param componentId
     * @param inspectionDateFrom
     * @param inspectionDateTo
     * @param cavityPrefix
     * @param cavityNum
     * @return TblComponentInspectionDefectList
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentInspectionDefectList getTblComponentInspectionDefectPraetoList(@QueryParam("incomingCompanyId") String incomingCompanyId, 
            @QueryParam("componentId") String componentId, 
            @QueryParam("inspectionDateFrom") String inspectionDateFrom,
            @QueryParam("inspectionDateTo") String inspectionDateTo,
            @QueryParam("cavityPrefix") String cavityPrefix,
            @QueryParam("cavityNum") int cavityNum) {
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        Date formatComponentInspectionFrom = null;
        Date formatComponentInspectionTo = null;
        // 検査実施日
        try {
            if (StringUtils.isNotEmpty(inspectionDateFrom)) {
                formatComponentInspectionFrom = sdf.parse(inspectionDateFrom + CommonConstants.SYS_MIN_TIME);
            }
            if (StringUtils.isNotEmpty(inspectionDateTo)) {
                formatComponentInspectionTo = sdf.parse(inspectionDateTo + CommonConstants.SYS_MAX_TIME);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblComponentInspectionDefectResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tblComponentInspectionDefectService.getTblComponentInspectionDefectPraetoList(incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum, loginUser.getLangId());
    }
    
    /**
     * 部品検査不具合のパレート表示用の情報を取得
     *
     * @param incomingCompanyId
     * @param componentId
     * @param inspectionDateFrom
     * @param inspectionDateTo
     * @param cavityPrefix
     * @param cavityNum
     * @return TblComponentInspectionDefectList
     * @throws IOException
     */
    @GET
    @Path("export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/octet-stream;charset=UTF-8")
    public Response exportTblComponentInspectionDefectPraeto(@QueryParam("incomingCompanyId") String incomingCompanyId, 
            @QueryParam("componentId") String componentId, 
            @QueryParam("inspectionDateFrom") String inspectionDateFrom,
            @QueryParam("inspectionDateTo") String inspectionDateTo,
            @QueryParam("cavityPrefix") String cavityPrefix,
            @QueryParam("cavityNum") int cavityNum) throws IOException {
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        Date formatComponentInspectionFrom = null;
        Date formatComponentInspectionTo = null;
        // 検査実施日
        try {
            if (StringUtils.isNotEmpty(inspectionDateFrom)) {
                formatComponentInspectionFrom = sdf.parse(inspectionDateFrom);
            }
            if (StringUtils.isNotEmpty(inspectionDateTo)) {
                formatComponentInspectionTo = sdf.parse(inspectionDateTo);
            }
        } catch (ParseException e) {
            Logger.getLogger(TblComponentInspectionDefectResource.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        }
        
        return tblComponentInspectionDefectService.exportTblComponentInspectionDefectPraeto(incomingCompanyId, componentId, formatComponentInspectionFrom, formatComponentInspectionTo, cavityPrefix, cavityNum, loginUser.getLangId());
    }

}
