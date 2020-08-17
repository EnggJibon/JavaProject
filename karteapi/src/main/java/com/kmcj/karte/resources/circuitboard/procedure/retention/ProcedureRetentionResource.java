/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.procedure.retention;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.apache.commons.lang.StringUtils;

/**
 * 基板工程滞留情報
 *
 * @author zds
 */
@RequestScoped
@Path("procedure/retention")
public class ProcedureRetentionResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private ProcedureRetentionService procedureRetentionService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    public ProcedureRetentionResource() {
    }
    
    /**
     * 基板工程滞留状況一覧
     *
     * @param productCode
     * @param procedureId
     * @param componentCode
     * @param startDate
     * @param endDate
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ProcedureRetentionList getProcedureRetention(
            @QueryParam("productCode") String productCode, //機種名(製品コード)
            @QueryParam("procedureId") String procedureId, //工程ID
            @QueryParam("componentCode") String componentCode, //基板名(部品コード)
            @QueryParam("startDate") String startDate, //期間開始日
            @QueryParam("endDate") String endDate, //期間終了日
            @QueryParam("sidx") String sidx,//order Key
            @QueryParam("sord") String sord,//order 順
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        FileUtil fu = new FileUtil();
        //検査日From - To
        Date formatStartDate = null;
        Date formatEndDate = null;
        // 検査開始日
        if (StringUtils.isNotEmpty(startDate)) {
            formatStartDate = fu.getDateTimeParseForDate(startDate + CommonConstants.SYS_MIN_TIME);
            if (formatStartDate == null) {
                ProcedureRetentionList response = new ProcedureRetentionList();
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }

        // 検査终了日
        if (StringUtils.isNotEmpty(endDate)) {
            formatEndDate = fu.getDateTimeParseForDate(endDate + CommonConstants.SYS_MAX_TIME);
            if (formatEndDate == null) {
                ProcedureRetentionList response = new ProcedureRetentionList();
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }
        return procedureRetentionService.getProcedureRetentionList(
                productCode,
                procedureId,
                componentCode,
                formatStartDate,
                formatEndDate,
                sidx,
                sord,
                pageNumber,
                pageSize,
                loginUser);
    }
    
    /**
     * 基板工程別不良情報CSV出力
     * @param productCode
     * @param procedureId
     * @param componentCode
     * @param startDate
     * @param endDate
     * @return 
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getProcedureRetentionCsvOutPut( 
            @QueryParam("productCode") String productCode, //機種名(製品)
            @QueryParam("productionLineNo") String procedureId, //工程ID
            @QueryParam("componentCode") String componentCode, //基板名(部品コード)
            @QueryParam("startDate") String startDate, //期間開始日
            @QueryParam("endDate") String endDate //期間終了日
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        FileUtil fu = new FileUtil();
        //検査日From - To
        Date formatStartDate = null;
        Date formatEndDate = null;
        // 検査開始日
        if (StringUtils.isNotEmpty(startDate)) {
            formatStartDate = fu.getDateTimeParseForDate(startDate + CommonConstants.SYS_MIN_TIME);
            if (formatStartDate == null) {
                FileReponse response = new FileReponse();
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }

        // 検査终了日
        if (StringUtils.isNotEmpty(endDate)) {
            formatEndDate = fu.getDateTimeParseForDate(endDate + CommonConstants.SYS_MAX_TIME);
            if (formatEndDate == null) {
                FileReponse response = new FileReponse();
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }
        return procedureRetentionService.getProcedureRetentionCsvOutPut(
                productCode,
                procedureId,
                componentCode,
                formatStartDate,
                formatEndDate,
                loginUser);
    }
}
