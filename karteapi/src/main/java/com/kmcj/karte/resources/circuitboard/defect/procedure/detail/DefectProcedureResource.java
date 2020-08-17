/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect.procedure.detail;

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
 * 基板工程別不良情報
 *
 * @author Apeng
 */
@RequestScoped
@Path("defect/procedure")
public class DefectProcedureResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private DefectProcedureService appearanceDefectProcedureService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    public DefectProcedureResource() {
    }

    /**
     * 基板工程別工程一覧
     *
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DefectProcedureList getProcedureList() {
        return appearanceDefectProcedureService.getProcedureList();
    }
    
    /**
     * 基板工程別不良情報一覧
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param serialNumber
     * @param checkDateStart
     * @param checkDateEnd
     * @param engineereGrids
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
    public DefectProcedureList getDefectProcedure(
            @QueryParam("productCode") String productCode, //機種名(製品)
            @QueryParam("productionLineNo") int productionLineNo, //生産ライン
            @QueryParam("componentCode") String componentCode, //基板名(部品コード)
            @QueryParam("checkDateStart") String checkDateStart, //検索開始検査日
            @QueryParam("checkDateEnd") String checkDateEnd, //検索终了検査日
            @QueryParam("serialNumber") String serialNumber, //シリアルナンバー
            @QueryParam("engineereGrids") String engineereGrids, //工程
            @QueryParam("sidx") String sidx,//order Key
            @QueryParam("sord") String sord,//order 順
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        FileUtil fu = new FileUtil();
        //検査日From - To
        Date formatCheckDateStart = null;
        Date formatCheckDateEnd = null;
        // 検査開始日
        if (StringUtils.isNotEmpty(checkDateStart)) {
            formatCheckDateStart = fu.getDateTimeParseForDate(checkDateStart + CommonConstants.SYS_MIN_TIME);
            if (formatCheckDateStart == null) {
                DefectProcedureList response = new DefectProcedureList();
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }

        // 検査终了日
        if (StringUtils.isNotEmpty(checkDateEnd)) {
            formatCheckDateEnd = fu.getDateTimeParseForDate(checkDateEnd + CommonConstants.SYS_MAX_TIME);
            if (formatCheckDateEnd == null) {
                DefectProcedureList response = new DefectProcedureList();
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }
        return appearanceDefectProcedureService.getDefectProcedureList(
                productCode,
                productionLineNo,
                componentCode,
                formatCheckDateStart,
                formatCheckDateEnd,
                serialNumber,
                engineereGrids,
                sidx,
                sord,
                pageNumber,
                pageSize,
                loginUser);
    }
    
    /**
     * 基板工程別不良情報詳細取得
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param serialNumber
     * @param checkDate
     * @param resultType
     * @return 
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DefectProcedureList getDefectProcedureById(
            @QueryParam("productCode") String productCode, //機種名(製品)
            @QueryParam("productionLineNo") int productionLineNo, //生産ライン
            @QueryParam("componentCode") String componentCode, //基板名(部品コード)
            @QueryParam("serialNumber") String serialNumber, //シリアルナンバー
            @QueryParam("checkDate") String checkDate, //検査日
            @QueryParam("resultType") String resultType //类型
            ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        Date formatCheckDate = null;
        try {
            // 検査開始日
            if (StringUtils.isNotEmpty(checkDate)) {
                formatCheckDate = sdf.parse(checkDate);
            }
        } catch (ParseException e) {
            // nothing
        }
        return appearanceDefectProcedureService.getDefectProcedureDetail(
                productCode,
                productionLineNo,
                componentCode,
                serialNumber,
                formatCheckDate,
                resultType,
                loginUser);
    }
    
    /**
     * 基板工程別不良情報CSV出力
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param checkDateStart
     * @param checkDateEnd
     * @param serialNumber
     * @param engineereGrids
     * @return 
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getDefectProcedureCsvOutPut( 
            @QueryParam("productCode") String productCode, //機種名(製品)
            @QueryParam("productionLineNo") int productionLineNo, //生産ライン
            @QueryParam("componentCode") String componentCode, //基板名(部品コード)
            @QueryParam("checkDateStart") String checkDateStart, //検索開始検査日
            @QueryParam("checkDateEnd") String checkDateEnd, //検索终了検査日
            @QueryParam("serialNumber") String serialNumber, //シリアルナンバー
            @QueryParam("engineereGrids") String engineereGrids //工程
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        FileUtil fu = new FileUtil();
        //検査日From - To
        Date formatCheckDateStart = null;
        Date formatCheckDateEnd = null;
        // 検査開始日
        if (StringUtils.isNotEmpty(checkDateStart)) {
            formatCheckDateStart = fu.getDateTimeParseForDate(checkDateStart + CommonConstants.SYS_MIN_TIME);
            if (formatCheckDateStart == null) {
                FileReponse response = new FileReponse();
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }

        // 検査终了日
        if (StringUtils.isNotEmpty(checkDateEnd)) {
            formatCheckDateEnd = fu.getDateTimeParseForDate(checkDateEnd + CommonConstants.SYS_MAX_TIME);
            if (formatCheckDateEnd == null) {
                FileReponse response = new FileReponse();
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }
        return appearanceDefectProcedureService.getDefectProcedureCsvOutPut(
                productCode,
                productionLineNo,
                componentCode,
                formatCheckDateStart,
                formatCheckDateEnd,
                serialNumber,
                engineereGrids,
                loginUser);
    }
}
