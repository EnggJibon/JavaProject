/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect.inspection.detail;

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
 * 基板外観検査不良情報
 *
 * @author admin
 */
@RequestScoped
@Path("defect/inspection")
public class DefectInspectionResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private DefectInspectionService defectInspectionService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    public DefectInspectionResource() {
    }

    /**
     * 基板外観検査不良情報一覧
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param checkDateStart
     * @param checkDateEnd
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
    public DefectInspectionList getDefectInspection(
            @QueryParam("productCode") String productCode, //機種名(製品)
            @QueryParam("productionLineNo") int productionLineNo, //生産ライン
            @QueryParam("componentCode") String componentCode, //基板名(部品コード)
            @QueryParam("checkDateStart") String checkDateStart, //検索開始検査日
            @QueryParam("checkDateEnd") String checkDateEnd, //検索终了検査日
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
                DefectInspectionList response = new DefectInspectionList();
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
                DefectInspectionList response = new DefectInspectionList();
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                return response;
            }
        }
        
        return defectInspectionService.getDefectInspectionList(
                productCode,
                productionLineNo,
                componentCode,
                formatCheckDateStart,
                formatCheckDateEnd,
                sidx,
                sord,
                pageNumber,
                pageSize);
    }

    /**
     * 基板外観検査不良情報詳細取得
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param inspectionDateStr
     * @param defectMendFlg
     * @return
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DefectInspectionList getDefectInspectionDetail(
            @QueryParam("productCode") String productCode, // 機種ID(製品ID)
            @QueryParam("productionLineNo") int productionLineNo, // 生産ラインNo
            @QueryParam("componentCode") String componentCode, //基板コード(部品コード)
            @QueryParam("inspectionDateStr") String inspectionDateStr, // 検査日
            @QueryParam("defectMendFlg") int defectMendFlg // 不良修理フラグ
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        //検査日From - To
        Date inspectionDate = null;
        try {
            // 検索日
            if (StringUtils.isNotEmpty(inspectionDateStr)) {
                inspectionDate = sdf.parse(inspectionDateStr);
            }
        } catch (ParseException e) {
            DefectInspectionList response = new DefectInspectionList();
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
            return response;
        }
        return defectInspectionService.getDefectInspectionDetail(productCode, productionLineNo, componentCode, inspectionDate, defectMendFlg, loginUser.getLangId());
    }
    
     /**
     * 基板工程別不良情報をCSV出力
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param checkDateStart
     * @param checkDateEnd
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getDefectInspectionCSV(
            @QueryParam("productCode") String productCode, //機種名(製品)
            @QueryParam("productionLineNo") int productionLineNo, //生産ライン
            @QueryParam("componentCode") String componentCode, //基板名(部品コード)
            @QueryParam("checkDateStart") String checkDateStart, //検索開始検査日
            @QueryParam("checkDateEnd") String checkDateEnd //検索终了検査日
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
        return defectInspectionService.getAppearanceInspectionDefectCSV(productCode, productionLineNo, componentCode, formatCheckDateStart, formatCheckDateEnd, loginUser);
        
    }
}
