/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.UrlDecodeInterceptor;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyList;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentList;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.installationsite.MstInstallationSiteList;
import com.kmcj.karte.resources.installationsite.MstInstallationSiteService;
import com.kmcj.karte.resources.location.MstLocationList;
import com.kmcj.karte.resources.location.MstLocationService;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtnService;
import com.kmcj.karte.resources.mold.assetno.MstMoldAssetNo;
import com.kmcj.karte.resources.mold.assetno.MstMoldAssetNoService;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeList;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeService;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationPK;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationService;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationVo;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistory;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistoryService;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistorys;
import com.kmcj.karte.resources.mold.spec.MstMoldSpec;
import com.kmcj.karte.resources.mold.spec.MstMoldSpecPK;
import com.kmcj.karte.resources.mold.spec.MstMoldSpecService;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistory;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistoryService;
import com.kmcj.karte.resources.procedure.MstProcedureList;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;
import com.kmcj.karte.resources.license.limit.MstLicenseLimitService;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.mold.inventory.Mold;
import com.kmcj.karte.resources.mold.inventory.MstMoldStoctake;
import com.kmcj.karte.resources.mold.inventory.OutputErrorInfo;
import com.kmcj.karte.resources.mold.inventory.SearchConditionList;
import com.kmcj.karte.resources.mold.inventory.TblMoldInventory;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelList;
import com.kmcj.karte.resources.user.MstUser;
import static com.kmcj.karte.util.FileUtil.SEPARATOR;
import com.kmcj.karte.util.TimezoneConverter;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 * 金型
 *
 * @author admin
 */
@RequestScoped
@Path("mold")
public class MstMoldResource {

    public MstMoldResource() {
    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstMoldService mstMoldService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private MstLicenseLimitService mstLicenseLimitService;
    
    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MstMoldSpecService mstMoldSpecService;

    @Inject
    private MstMoldSpecHistoryService mstMoldSpecHistoryService;

    @Inject
    private MstComponentService mstComponentService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstMoldComponentRelationService mstMoldComponentRelationService;

    @Inject
    private MstMoldAttributeService mstMoldAttributeService;

    @Inject
    private MstCompanyService mstCompanyService;

    @Inject
    private MstLocationService mstLocationService;

    @Inject
    private MstInstallationSiteService mstInstallationSiteService;

    @Inject
    private TblMoldLocationHistoryService tblMoldLocationHistoryService;

    @Inject
    private TblUploadFileService tblUploadFileService;

    @Inject
    private MstMoldAssetNoService mstMoldAssetNoService;

    @Inject
    private TblMaintenanceCyclePtnService tblMaintenanceCyclePtnService;
    
    @Inject
    private MstProcedureService mstProcedureService;

    /**
     * 金型マスタ件数取得
     *
     * @param moldId //金型ID
     * @param moldName //金型名称
     * @param mainAssetNo //資産番号
     * @param ownerCompanyName //所有会社名称
     * @param companyName //会社名称
     * @param locationName //所在地名称
     * @param instllationSiteName //設置場所名称
     * @param moldType //金型種類
     * @param department //所属
     * @param moldCreatedDateFrom//金型作成日From
     * @param moldCreatedDateTo//金型作成日To
     * @param lastProductionDateFrom//最終生産日From
     * @param lastProductionDateTo//最終生産日To
     * @param totalProducingTimeHourFrom//累計生産時間From
     * @param totalProducingTimeHourTo//累計生産時間To
     * @param totalShotCountFrom//累計ショット数From
     * @param totalShotCountTo//累計ショット数To
     * @param lastMainteDateFrom//最終メンテナンス日From
     * @param lastMainteDateTo//最終メンテナンス日To
     * @param afterMainteTotalShotCountFrom//メンテナンス後生産時間From
     * @param afterMainteTotalShotCountTo//メンテナンス後ショット数To
     * @param afterMainteTotalProducingTimeHourFrom//メンテナンス後生産時間From
     * @param afterMainteTotalProducingTimeHourTo//メンテナンス後ショット数To
     * @param status //ステータス
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("moldType") String moldType,
            @QueryParam("department") String department,
            @QueryParam("status") String status,// 4.2 対応　ZhangYing S
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("moldCreatedDateFrom") String moldCreatedDateFrom, // 金型作成日yyyy/MM/dd
            @QueryParam("moldCreatedDateTo") String moldCreatedDateTo, // 金型作成日yyyy/MM/dd

            @QueryParam("totalProducingTimeHourFrom") String totalProducingTimeHourFrom, // 累計生産時間 数字
            @QueryParam("totalProducingTimeHourTo") String totalProducingTimeHourTo, // 累計生産時間 数字
            @QueryParam("totalShotCountFrom") String totalShotCountFrom, // 累計ショット数 数字
            @QueryParam("totalShotCountTo") String totalShotCountTo, //　累計ショット数数字
            @QueryParam("afterMainteTotalProducingTimeHourFrom") String afterMainteTotalProducingTimeHourFrom, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalProducingTimeHourTo") String afterMainteTotalProducingTimeHourTo, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalShotCountFrom") String afterMainteTotalShotCountFrom, // メンテナンス後ショット数 数字
            @QueryParam("afterMainteTotalShotCountTo") String afterMainteTotalShotCountTo // メンテナンス後ショット数 数字
    // 4.2 対応　ZhangYing E
    ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        // 最後生産日From - To
        Date formatLastProductionDateFrom = null;
        Date formatLastProductionDateTo = null;
        // 最後メンテ日From - To
        Date formatLastMainteDateFrom = null;
        Date formatLastMainteDateTo = null;
        // 金型作成日From - To
        Date formatMoldCreatedDateFrom = null;
        Date formatMoldCreatedDateTo = null;
        // 累計生産時間 From - To
        Integer formatTotalProducingTimeHourFrom = null;
        Integer formatTotalProducingTimeHourTo = null;

        // 累計ショット数 From - To
        Integer formatTotalShotCountFrom = null;
        Integer formatTotalShotCountTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalProducingTimeHourFrom = null;
        Integer formatAfterMainteTotalProducingTimeHourTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalShotCountFrom = null;
        Integer formatAfterMainteTotalShotCountTo = null;

        Integer formatMoldType = null;
        Integer formatDepartment = null;
        Integer formatStatus = null;
        try {
            // 最後生産日From - To
            if (lastProductionDateFrom != null && !"".equals(lastProductionDateFrom)) {
                formatLastProductionDateFrom = sdf.parse(lastProductionDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (lastProductionDateTo != null && !"".equals(lastProductionDateTo)) {
                formatLastProductionDateTo = sdf.parse(lastProductionDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 最後メンテ日From - To
            if (lastMainteDateFrom != null && !"".equals(lastMainteDateFrom)) {
                formatLastMainteDateFrom = sdf.parse(lastMainteDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {

            if (lastMainteDateTo != null && !"".equals(lastMainteDateTo)) {
                formatLastMainteDateTo = sdf.parse(lastMainteDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {
            // 金型作成日From - To
            if (moldCreatedDateFrom != null && !"".equals(moldCreatedDateFrom)) {
                formatMoldCreatedDateFrom = sdf.parse(moldCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (moldCreatedDateTo != null && !"".equals(moldCreatedDateTo)) {
                formatMoldCreatedDateTo = sdf.parse(moldCreatedDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 累計生産時間 From - To
            if (totalProducingTimeHourFrom != null && !"".equals(totalProducingTimeHourFrom)) {
                formatTotalProducingTimeHourFrom = Integer.parseInt(totalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalProducingTimeHourTo != null && !"".equals(totalProducingTimeHourTo)) {
                formatTotalProducingTimeHourTo = Integer.parseInt(totalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountFrom != null && !"".equals(totalShotCountFrom)) {
                formatTotalShotCountFrom = Integer.parseInt(totalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountTo != null && !"".equals(totalShotCountTo)) {
                formatTotalShotCountTo = Integer.parseInt(totalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourFrom != null && !"".equals(afterMainteTotalProducingTimeHourFrom)) {
                formatAfterMainteTotalProducingTimeHourFrom = Integer.parseInt(afterMainteTotalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourTo != null && !"".equals(afterMainteTotalProducingTimeHourTo)) {
                formatAfterMainteTotalProducingTimeHourTo = Integer.parseInt(afterMainteTotalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountFrom != null && !"".equals(afterMainteTotalShotCountFrom)) {
                formatAfterMainteTotalShotCountFrom = Integer.parseInt(afterMainteTotalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountTo != null && !"".equals(afterMainteTotalShotCountTo)) {
                formatAfterMainteTotalShotCountTo = Integer.parseInt(afterMainteTotalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (moldType != null && !"".equals(moldType)) {
                formatMoldType = Integer.parseInt(moldType);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (department != null && !"".equals(department)) {
                formatDepartment = Integer.parseInt(department);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (status != null && !"".equals(status)) {
                formatStatus = Integer.parseInt(status);
            }
        } catch (NumberFormatException e) {
            // nothing
        }

        CountResponse count = mstMoldService.getMstMoldCount(moldId,
                moldName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                formatMoldType,
                formatDepartment,
                formatLastProductionDateFrom,
                formatLastProductionDateTo,
                formatTotalProducingTimeHourFrom,
                formatTotalProducingTimeHourTo,
                formatTotalShotCountFrom,
                formatTotalShotCountTo,
                formatLastMainteDateFrom,
                formatLastMainteDateTo,
                formatAfterMainteTotalProducingTimeHourFrom,
                formatAfterMainteTotalProducingTimeHourTo,
                formatAfterMainteTotalShotCountFrom,
                formatAfterMainteTotalShotCountTo,
                formatMoldCreatedDateFrom,
                formatMoldCreatedDateTo,
                formatStatus);

        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());
        if (count.getCount() > sysCount) {
            count.setError(true);
            count.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            count.setErrorMessage(msg);
        }

        return count;
    }

    /**
     * 金型マスタ複数取得
     *
     * @param moldId
     * @param moldName
     * @param mainAssetNo
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param moldType
     * @param department
     * @param moldCreatedDateFrom//金型作成日From
     * @param moldCreatedDateTo//金型作成日To
     * @param lastProductionDateFrom//最終生産日From
     * @param lastProductionDateTo//最終生産日To
     * @param totalProducingTimeHourFrom//累計生産時間From
     * @param totalProducingTimeHourTo//累計生産時間To
     * @param totalShotCountFrom//累計ショット数From
     * @param totalShotCountTo//累計ショット数To
     * @param lastMainteDateFrom//最終メンテナンス日From
     * @param lastMainteDateTo//最終メンテナンス日To
     * @param afterMainteTotalShotCountFrom//メンテナンス後生産時間From
     * @param afterMainteTotalShotCountTo//メンテナンス後ショット数To
     * @param afterMainteTotalProducingTimeHourFrom//メンテナンス後生産時間From
     * @param afterMainteTotalProducingTimeHourTo//メンテナンス後ショット数To
     * @param status
     * @return an instance of MstMoldList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldDetailList getMolds(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("moldType") String moldType,
            @QueryParam("department") String department,
            // 4.2 対応　ZhangYing S
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("moldCreatedDateFrom") String moldCreatedDateFrom, // 金型作成日yyyy/MM/dd
            @QueryParam("moldCreatedDateTo") String moldCreatedDateTo, // 金型作成日yyyy/MM/dd
            @QueryParam("totalProducingTimeHourFrom") String totalProducingTimeHourFrom, // 累計生産時間 数字
            @QueryParam("totalProducingTimeHourTo") String totalProducingTimeHourTo, // 累計生産時間 数字
            @QueryParam("totalShotCountFrom") String totalShotCountFrom, // 累計ショット数 数字
            @QueryParam("totalShotCountTo") String totalShotCountTo, //　累計ショット数数字
            @QueryParam("afterMainteTotalProducingTimeHourFrom") String afterMainteTotalProducingTimeHourFrom, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalProducingTimeHourTo") String afterMainteTotalProducingTimeHourTo, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalShotCountFrom") String afterMainteTotalShotCountFrom, // メンテナンス後ショット数 数字
            @QueryParam("afterMainteTotalShotCountTo") String afterMainteTotalShotCountTo, // メンテナンス後ショット数 数字
            // 4.2 対応　ZhangYing E
            @QueryParam("status") String status) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        // 最後生産日From - To
        Date formatLastProductionDateFrom = null;
        Date formatLastProductionDateTo = null;
        // 最後メンテ日From - To
        Date formatLastMainteDateFrom = null;
        Date formatLastMainteDateTo = null;
        // 金型作成日From - To
        Date formatMoldCreatedDateFrom = null;
        Date formatMoldCreatedDateTo = null;
        // 累計生産時間 From - To
        Integer formatTotalProducingTimeHourFrom = null;
        Integer formatTotalProducingTimeHourTo = null;

        // 累計ショット数 From - To
        Integer formatTotalShotCountFrom = null;
        Integer formatTotalShotCountTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalProducingTimeHourFrom = null;
        Integer formatAfterMainteTotalProducingTimeHourTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalShotCountFrom = null;
        Integer formatAfterMainteTotalShotCountTo = null;

        Integer formatMoldType = null;
        Integer formatDepartment = null;
        Integer formatStatus = null;

        try {
            // 最後生産日From - To
            if (lastProductionDateFrom != null && !"".equals(lastProductionDateFrom)) {
                formatLastProductionDateFrom = sdf.parse(lastProductionDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (lastProductionDateTo != null && !"".equals(lastProductionDateTo)) {
                formatLastProductionDateTo = sdf.parse(lastProductionDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 最後メンテ日From - To
            if (lastMainteDateFrom != null && !"".equals(lastMainteDateFrom)) {
                formatLastMainteDateFrom = sdf.parse(lastMainteDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {

            if (lastMainteDateTo != null && !"".equals(lastMainteDateTo)) {
                formatLastMainteDateTo = sdf.parse(lastMainteDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {
            // 金型作成日From - To
            if (moldCreatedDateFrom != null && !"".equals(moldCreatedDateFrom)) {
                formatMoldCreatedDateFrom = sdf.parse(moldCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (moldCreatedDateTo != null && !"".equals(moldCreatedDateTo)) {
                formatMoldCreatedDateTo = sdf.parse(moldCreatedDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 累計生産時間 From - To
            if (totalProducingTimeHourFrom != null && !"".equals(totalProducingTimeHourFrom)) {
                formatTotalProducingTimeHourFrom = Integer.parseInt(totalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalProducingTimeHourTo != null && !"".equals(totalProducingTimeHourTo)) {
                formatTotalProducingTimeHourTo = Integer.parseInt(totalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountFrom != null && !"".equals(totalShotCountFrom)) {
                formatTotalShotCountFrom = Integer.parseInt(totalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountTo != null && !"".equals(totalShotCountTo)) {
                formatTotalShotCountTo = Integer.parseInt(totalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourFrom != null && !"".equals(afterMainteTotalProducingTimeHourFrom)) {
                formatAfterMainteTotalProducingTimeHourFrom = Integer.parseInt(afterMainteTotalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourTo != null && !"".equals(afterMainteTotalProducingTimeHourTo)) {
                formatAfterMainteTotalProducingTimeHourTo = Integer.parseInt(afterMainteTotalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountFrom != null && !"".equals(afterMainteTotalShotCountFrom)) {
                formatAfterMainteTotalShotCountFrom = Integer.parseInt(afterMainteTotalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountTo != null && !"".equals(afterMainteTotalShotCountTo)) {
                formatAfterMainteTotalShotCountTo = Integer.parseInt(afterMainteTotalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (moldType != null && !"".equals(moldType)) {
                formatMoldType = Integer.parseInt(moldType);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (department != null && !"".equals(department)) {
                formatDepartment = Integer.parseInt(department);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (status != null && !"".equals(status)) {
                formatStatus = Integer.parseInt(status);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMoldDetailList mstMoldList = mstMoldService.getMstMolds(
                moldId,
                moldName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                formatMoldType,
                formatDepartment,
                formatLastProductionDateFrom,
                formatLastProductionDateTo,
                formatTotalProducingTimeHourFrom,
                formatTotalProducingTimeHourTo,
                formatTotalShotCountFrom,
                formatTotalShotCountTo,
                formatLastMainteDateFrom,
                formatLastMainteDateTo,
                formatAfterMainteTotalProducingTimeHourFrom,
                formatAfterMainteTotalProducingTimeHourTo,
                formatAfterMainteTotalShotCountFrom,
                formatAfterMainteTotalShotCountTo,
                formatMoldCreatedDateFrom,
                formatMoldCreatedDateTo,
                formatStatus,
                loginUser);
        return mstMoldList;
    }
    /**
     * 金型マスタ複数取得
     *
     * @param moldId
     * @param moldName
     * @param mainAssetNo
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param moldType
     * @param department
     * @param moldCreatedDateFrom//金型作成日From
     * @param moldCreatedDateTo//金型作成日To
     * @param lastProductionDateFrom//最終生産日From
     * @param lastProductionDateTo//最終生産日To
     * @param totalProducingTimeHourFrom//累計生産時間From
     * @param totalProducingTimeHourTo//累計生産時間To
     * @param totalShotCountFrom//累計ショット数From
     * @param totalShotCountTo//累計ショット数To
     * @param lastMainteDateFrom//最終メンテナンス日From
     * @param lastMainteDateTo//最終メンテナンス日To
     * @param afterMainteTotalShotCountFrom//メンテナンス後生産時間From
     * @param afterMainteTotalShotCountTo//メンテナンス後ショット数To
     * @param afterMainteTotalProducingTimeHourFrom//メンテナンス後生産時間From
     * @param afterMainteTotalProducingTimeHourTo//メンテナンス後ショット数To
     * @param status
     * @return an instance of MstMoldList
     */
    @GET
    @Path("moldListWithoutDispose")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldDetailList getMoldsWithoutDispose(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("moldType") String moldType,
            @QueryParam("department") String department,
            // 4.2 対応　ZhangYing S
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("moldCreatedDateFrom") String moldCreatedDateFrom, // 金型作成日yyyy/MM/dd
            @QueryParam("moldCreatedDateTo") String moldCreatedDateTo, // 金型作成日yyyy/MM/dd
            @QueryParam("totalProducingTimeHourFrom") String totalProducingTimeHourFrom, // 累計生産時間 数字
            @QueryParam("totalProducingTimeHourTo") String totalProducingTimeHourTo, // 累計生産時間 数字
            @QueryParam("totalShotCountFrom") String totalShotCountFrom, // 累計ショット数 数字
            @QueryParam("totalShotCountTo") String totalShotCountTo, //　累計ショット数数字
            @QueryParam("afterMainteTotalProducingTimeHourFrom") String afterMainteTotalProducingTimeHourFrom, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalProducingTimeHourTo") String afterMainteTotalProducingTimeHourTo, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalShotCountFrom") String afterMainteTotalShotCountFrom, // メンテナンス後ショット数 数字
            @QueryParam("afterMainteTotalShotCountTo") String afterMainteTotalShotCountTo, // メンテナンス後ショット数 数字
            // 4.2 対応　ZhangYing E
            @QueryParam("status") String status) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        // 最後生産日From - To
        Date formatLastProductionDateFrom = null;
        Date formatLastProductionDateTo = null;
        // 最後メンテ日From - To
        Date formatLastMainteDateFrom = null;
        Date formatLastMainteDateTo = null;
        // 金型作成日From - To
        Date formatMoldCreatedDateFrom = null;
        Date formatMoldCreatedDateTo = null;
        // 累計生産時間 From - To
        Integer formatTotalProducingTimeHourFrom = null;
        Integer formatTotalProducingTimeHourTo = null;

        // 累計ショット数 From - To
        Integer formatTotalShotCountFrom = null;
        Integer formatTotalShotCountTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalProducingTimeHourFrom = null;
        Integer formatAfterMainteTotalProducingTimeHourTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalShotCountFrom = null;
        Integer formatAfterMainteTotalShotCountTo = null;

        Integer formatMoldType = null;
        Integer formatDepartment = null;
        Integer formatStatus = null;

        try {
            // 最後生産日From - To
            if (lastProductionDateFrom != null && !"".equals(lastProductionDateFrom)) {
                formatLastProductionDateFrom = sdf.parse(lastProductionDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (lastProductionDateTo != null && !"".equals(lastProductionDateTo)) {
                formatLastProductionDateTo = sdf.parse(lastProductionDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 最後メンテ日From - To
            if (lastMainteDateFrom != null && !"".equals(lastMainteDateFrom)) {
                formatLastMainteDateFrom = sdf.parse(lastMainteDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {

            if (lastMainteDateTo != null && !"".equals(lastMainteDateTo)) {
                formatLastMainteDateTo = sdf.parse(lastMainteDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {
            // 金型作成日From - To
            if (moldCreatedDateFrom != null && !"".equals(moldCreatedDateFrom)) {
                formatMoldCreatedDateFrom = sdf.parse(moldCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (moldCreatedDateTo != null && !"".equals(moldCreatedDateTo)) {
                formatMoldCreatedDateTo = sdf.parse(moldCreatedDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 累計生産時間 From - To
            if (totalProducingTimeHourFrom != null && !"".equals(totalProducingTimeHourFrom)) {
                formatTotalProducingTimeHourFrom = Integer.parseInt(totalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalProducingTimeHourTo != null && !"".equals(totalProducingTimeHourTo)) {
                formatTotalProducingTimeHourTo = Integer.parseInt(totalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountFrom != null && !"".equals(totalShotCountFrom)) {
                formatTotalShotCountFrom = Integer.parseInt(totalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountTo != null && !"".equals(totalShotCountTo)) {
                formatTotalShotCountTo = Integer.parseInt(totalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourFrom != null && !"".equals(afterMainteTotalProducingTimeHourFrom)) {
                formatAfterMainteTotalProducingTimeHourFrom = Integer.parseInt(afterMainteTotalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourTo != null && !"".equals(afterMainteTotalProducingTimeHourTo)) {
                formatAfterMainteTotalProducingTimeHourTo = Integer.parseInt(afterMainteTotalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountFrom != null && !"".equals(afterMainteTotalShotCountFrom)) {
                formatAfterMainteTotalShotCountFrom = Integer.parseInt(afterMainteTotalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountTo != null && !"".equals(afterMainteTotalShotCountTo)) {
                formatAfterMainteTotalShotCountTo = Integer.parseInt(afterMainteTotalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (moldType != null && !"".equals(moldType)) {
                formatMoldType = Integer.parseInt(moldType);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (department != null && !"".equals(department)) {
                formatDepartment = Integer.parseInt(department);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (status != null && !"".equals(status)) {
                formatStatus = Integer.parseInt(status);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMoldDetailList mstMoldList = mstMoldService.getMstMoldsWithoutDispose(
                moldId,
                moldName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                formatMoldType,
                formatDepartment,
                formatLastProductionDateFrom,
                formatLastProductionDateTo,
                formatTotalProducingTimeHourFrom,
                formatTotalProducingTimeHourTo,
                formatTotalShotCountFrom,
                formatTotalShotCountTo,
                formatLastMainteDateFrom,
                formatLastMainteDateTo,
                formatAfterMainteTotalProducingTimeHourFrom,
                formatAfterMainteTotalProducingTimeHourTo,
                formatAfterMainteTotalShotCountFrom,
                formatAfterMainteTotalShotCountTo,
                formatMoldCreatedDateFrom,
                formatMoldCreatedDateTo,
                formatStatus,
                loginUser);
        return mstMoldList;
    }
    /**
     * 金型マスタ削除
     *
     * @param moldId
     * @return
     */
    @DELETE
    @Path("{moldId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public BasicResponse deleteMold(@PathParam("moldId") String moldId) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (!mstMoldService.getMstMoldExistCheck(moldId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
//        } else if (mstMoldService.checkMoldUsedByProduction(moldId)) {
//            response.setError(true);
//            response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
        } else {
            mstMoldService.deleteMstMold(moldId);
        }
        return response;
    }

    /**
     * 金型マスタCSV出力
     *
     * @param moldId
     * @param moldName
     * @param mainAssetNo
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param moldType
     * @param department
     * @param lastProductionDateFrom
     * @param lastProductionDateTo
     * @param totalProducingTimeHourFrom
     * @param totalProducingTimeHourTo
     * @param moldCreatedDateFrom
     * @param moldCreatedDateTo
     * @param totalShotCountFrom
     * @param totalShotCountTo
     * @param lastMainteDateFrom
     * @param lastMainteDateTo
     * @param afterMainteTotalProducingTimeHourFrom
     * @param afterMainteTotalProducingTimeHourTo
     * @param afterMainteTotalShotCountFrom
     * @param afterMainteTotalShotCountTo
     * @param status
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMoldsCSV(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("moldType") String moldType,
            @QueryParam("department") String department,
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("moldCreatedDateFrom") String moldCreatedDateFrom, // 金型作成日yyyy/MM/dd
            @QueryParam("moldCreatedDateTo") String moldCreatedDateTo, // 金型作成日yyyy/MM/dd
            @QueryParam("totalProducingTimeHourFrom") String totalProducingTimeHourFrom, // 累計生産時間 数字
            @QueryParam("totalProducingTimeHourTo") String totalProducingTimeHourTo, // 累計生産時間 数字
            @QueryParam("totalShotCountFrom") String totalShotCountFrom, // 累計ショット数 数字
            @QueryParam("totalShotCountTo") String totalShotCountTo, //　累計ショット数数字
            @QueryParam("afterMainteTotalProducingTimeHourFrom") String afterMainteTotalProducingTimeHourFrom, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalProducingTimeHourTo") String afterMainteTotalProducingTimeHourTo, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalShotCountFrom") String afterMainteTotalShotCountFrom, // メンテナンス後ショット数 数字
            @QueryParam("afterMainteTotalShotCountTo") String afterMainteTotalShotCountTo, // メンテナンス後ショット数 数字
            @QueryParam("status") String status) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstMoldService.outMoldTypeOfChoice(loginUser.getLangId());
        mstMoldService.outStatusOfChoice(loginUser.getLangId());

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        // 最後生産日From - To
        Date formatLastProductionDateFrom = null;
        Date formatLastProductionDateTo = null;
        // 最後メンテ日From - To
        Date formatLastMainteDateFrom = null;
        Date formatLastMainteDateTo = null;
        // 設備作成日From - To
        Date formatMoldCreatedDateFrom = null;
        Date formatMoldCreatedDateTo = null;
        // 累計生産時間 From - To
        Integer formatTotalProducingTimeHourFrom = null;
        Integer formatTotalProducingTimeHourTo = null;

        // 累計ショット数 From - To
        Integer formatTotalShotCountFrom = null;
        Integer formatTotalShotCountTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalProducingTimeHourFrom = null;
        Integer formatAfterMainteTotalProducingTimeHourTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalShotCountFrom = null;
        Integer formatAfterMainteTotalShotCountTo = null;

        Integer formatMoldType = null;
        Integer formatDepartment = null;
        Integer formatStatus = null;
        try {
            // 最後生産日From - To
            if (lastProductionDateFrom != null && !"".equals(lastProductionDateFrom)) {
                formatLastProductionDateFrom = sdf.parse(lastProductionDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (lastProductionDateTo != null && !"".equals(lastProductionDateTo)) {
                formatLastProductionDateTo = sdf.parse(lastProductionDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 最後メンテ日From - To
            if (lastMainteDateFrom != null && !"".equals(lastMainteDateFrom)) {
                formatLastMainteDateFrom = sdf.parse(lastMainteDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {

            if (lastMainteDateTo != null && !"".equals(lastMainteDateTo)) {
                formatLastMainteDateTo = sdf.parse(lastMainteDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {
            // 金型作成日From - To
            if (moldCreatedDateFrom != null && !"".equals(moldCreatedDateFrom)) {
                formatMoldCreatedDateFrom = sdf.parse(moldCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (moldCreatedDateTo != null && !"".equals(moldCreatedDateTo)) {
                formatMoldCreatedDateTo = sdf.parse(moldCreatedDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 累計生産時間 From - To
            if (totalProducingTimeHourFrom != null && !"".equals(totalProducingTimeHourFrom)) {
                formatTotalProducingTimeHourFrom = Integer.parseInt(totalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalProducingTimeHourTo != null && !"".equals(totalProducingTimeHourTo)) {
                formatTotalProducingTimeHourTo = Integer.parseInt(totalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountFrom != null && !"".equals(totalShotCountFrom)) {
                formatTotalShotCountFrom = Integer.parseInt(totalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountTo != null && !"".equals(totalShotCountTo)) {
                formatTotalShotCountTo = Integer.parseInt(totalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourFrom != null && !"".equals(afterMainteTotalProducingTimeHourFrom)) {
                formatAfterMainteTotalProducingTimeHourFrom = Integer.parseInt(afterMainteTotalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourTo != null && !"".equals(afterMainteTotalProducingTimeHourTo)) {
                formatAfterMainteTotalProducingTimeHourTo = Integer.parseInt(afterMainteTotalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountFrom != null && !"".equals(afterMainteTotalShotCountFrom)) {
                formatAfterMainteTotalShotCountFrom = Integer.parseInt(afterMainteTotalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountTo != null && !"".equals(afterMainteTotalShotCountTo)) {
                formatAfterMainteTotalShotCountTo = Integer.parseInt(afterMainteTotalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (moldType != null && !"".equals(moldType)) {
                formatMoldType = Integer.parseInt(moldType);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (department != null && !"".equals(department)) {
                formatDepartment = Integer.parseInt(department);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (status != null && !"".equals(status)) {
                formatStatus = Integer.parseInt(status);
            }
        } catch (NumberFormatException e) {
            // nothing
        }

        return mstMoldService.getMstMoldOutputCsv(
                moldId,
                moldName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                formatMoldType,
                formatDepartment,
                formatLastProductionDateFrom,
                formatLastProductionDateTo,
                formatTotalProducingTimeHourFrom,
                formatTotalProducingTimeHourTo,
                formatTotalShotCountFrom,
                formatTotalShotCountTo,
                formatLastMainteDateFrom,
                formatLastMainteDateTo,
                formatAfterMainteTotalProducingTimeHourFrom,
                formatAfterMainteTotalProducingTimeHourTo,
                formatAfterMainteTotalShotCountFrom,
                formatAfterMainteTotalShotCountTo,
                formatStatus,
                formatMoldCreatedDateFrom,
                formatMoldCreatedDateTo,
                loginUser);

    }

    /**
     * 金型一覧CSV取込ボタン 金型マスタCSV取込
     *
     * @param fileUuid
     * @return
     * @throws java.text.ParseException
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMoldsCSV(@PathParam("fileUuid") String fileUuid) throws ParseException {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long deletedCount = 0;
        long failedCount = 0;
        String logFileUuid = IDGenerator.generate();
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        ArrayList readList = CSVFileUtil.readCsv(csvFile);

        if (readList.size() <= 1) {
            return importResultResponse;
        } else {
            try {
                String strHead = readList.get(0).toString();
                String userLangId = loginUser.getLangId();
                String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

                List<String> dictKeyList = Arrays.asList(
                        "row_number",
                        "mold_id",//金型ID
                        "mold_spec",//金型仕様
                        "component_code",//部品コード
                        "error",
                        "error_detail",
                        "db_process",
                        "msg_record_added",
                        "msg_data_modified",
                        "msg_error_data_deleted",
                        "mst_error_record_not_found",
                        "msg_error_over_length",
                        "msg_error_wrong_csv_layout",
                        "msg_error_value_invalid",
                        "msg_error_ext_edit",
                        "msg_error_not_isnumber",
                        "mold_name",//金型名称
                        "mold_type",//金型種類
                        "main_asset_no",//代表資産番号
                        "mold_created_date",//金型作成日
                        "inspected_date",//検収日
                        "user_department",//所属
                        "owner_company_code",//所有会社名称
                        "installed_date",//設置日
                        "company_code",//会社名称
                        "location_code",//所在地名称
                        "installation_site_code",//設置場所名称
                        "mold_status",//状態
                        "status_changed_date",//状態変更日 
                        "msg_record_deleted",
                        "mold_last_production_date",
                        "mold_total_production_time_hour",
                        "total_shot_count",
                        "mold_last_mainte_date",
                        "mold_after_mainte_total_production_time_hour",
                        "mold_after_mainte_total_shot_count",
                        "mold_mainte_cycle_code_01",
                        "mold_mainte_cycle_code_02",
                        "mold_mainte_cycle_code_03",
                        "mold_spec",//金型仕様
                        "msg_error_not_null",
                        "msg_error_date_format_invalid"
                );
                Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, userLangId, dictKeyList);

                String lineNo = headerMap.get("row_number");
                String head_moldId = headerMap.get("mold_id");//金型ID
                String head_moldSpec = headerMap.get("mold_spec");//金型仕様
                String head_componentCode = headerMap.get("component_code");//部品コード
                String error = headerMap.get("error");
                String errorContents = headerMap.get("error_detail");
                String result = headerMap.get("db_process");
                String addedMsg = headerMap.get("msg_record_added");
                String updatedMsg = headerMap.get("msg_data_modified");
                String deletedMsg = headerMap.get("msg_record_deleted");
                String notFound = headerMap.get("mst_error_record_not_found");
                String maxLangth = headerMap.get("msg_error_over_length");
                String layout = headerMap.get("msg_error_wrong_csv_layout");
                String invalidValue = headerMap.get("msg_error_value_invalid");
                String extEdit = headerMap.get("msg_error_ext_edit");
                String notnumber = headerMap.get("msg_error_not_isnumber");

                String head_moldName = headerMap.get("mold_name");//金型名称
                String head_moldType = headerMap.get("mold_type");//金型種類
                String head_moldAssetNo = headerMap.get("main_asset_no");//代表資産番号
                String head_moldCreatedDate = headerMap.get("mold_created_date");//金型作成日
                String head_inspectedDdate = headerMap.get("inspected_date");//検収日
                String head_department = headerMap.get("user_department");//所属
                String head_moldOwnerCompanyCode = headerMap.get("owner_company_code");//所有会社名称
                String head_installedDate = headerMap.get("installed_date");//設置日
                String head_moldCompanyCode = headerMap.get("company_code");//会社名称
                String head_moldLocationCode = headerMap.get("location_code");//所在地名称
                String head_moldInstallationSiteCode = headerMap.get("installation_site_code");//設置場所名称
                String head_status = headerMap.get("mold_status");//状態
                String head_statusChangedDate = headerMap.get("status_changed_date");//状態変更日 

                String head_lastProductionDate = headerMap.get("mold_last_production_date");
                String head_totalProductionTimeHour = headerMap.get("mold_total_production_time_hour");
                String head_totalShotCount = headerMap.get("total_shot_count");
                String head_lastMainteDate = headerMap.get("mold_last_mainte_date");
                String head_afterMainteTotalProductionTimeHour = headerMap.get("mold_after_mainte_total_production_time_hour");
                String head_afterMainteTotalShotCount = headerMap.get("mold_after_mainte_total_shot_count");
                String head_mainteCycleCode01 = headerMap.get("mold_mainte_cycle_code_01");
                String head_mainteCycleCode02 = headerMap.get("mold_mainte_cycle_code_02");
                String head_mainteCycleCode03 = headerMap.get("mold_mainte_cycle_code_03");

                String head_moldspec = headerMap.get("mold_spec");//金型仕様
                String nullMsg = headerMap.get("msg_error_not_null");
                String dataCheck = headerMap.get("msg_error_date_format_invalid");

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("head_moldId", head_moldId);
                logParm.put("head_moldName", head_moldName);
                logParm.put("head_moldType", head_moldType);
                logParm.put("head_moldAssetNo", head_moldAssetNo);
                logParm.put("head_moldCreatedDate", head_moldCreatedDate);
                logParm.put("head_inspectedDdate", head_inspectedDdate);
                logParm.put("head_department", head_department);
                logParm.put("head_moldOwnerCompanyCode", head_moldOwnerCompanyCode);
                logParm.put("head_installedDate", head_installedDate);
                logParm.put("head_moldCompanyCode", head_moldCompanyCode);
                logParm.put("head_moldLocationCode", head_moldLocationCode);
                logParm.put("head_moldInstallationSiteCode", head_moldInstallationSiteCode);
                logParm.put("head_status", head_status);
                logParm.put("head_statusChangedDate", head_statusChangedDate);

                logParm.put("head_lastProductionDate", head_lastProductionDate);
                logParm.put("head_totalProductionTimeHour", head_totalProductionTimeHour);
                logParm.put("head_totalShotCount", head_totalShotCount);
                logParm.put("head_lastMainteDate", head_lastMainteDate);
                logParm.put("head_afterMainteTotalProductionTimeHour", head_afterMainteTotalProductionTimeHour);
                logParm.put("head_afterMainteTotalShotCount", head_afterMainteTotalShotCount);
                logParm.put("head_mainteCycleCode01", head_mainteCycleCode01);
                logParm.put("head_mainteCycleCode02", head_mainteCycleCode02);
                logParm.put("head_mainteCycleCode03", head_mainteCycleCode03);

                logParm.put("head_moldspec", head_moldspec);
                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("maxLangth", maxLangth);
                logParm.put("invalidValue", invalidValue);
                logParm.put("notnumber", notnumber);
                logParm.put("dataCheck", dataCheck);
                logParm.put("layout", layout);
                logParm.put("notFound", notFound);

                FileUtil fu = new FileUtil();

                /* 金型仕様と部品コードのcount Start*/
                int imoldSpec = 0;
                int iComponentCode = 0;
                int additionalColCnt = -1;
                String csvArrayLine0[] = FileUtil.splitCsvLine(strHead);
                int delFlagPostion = csvArrayLine0.length - 1;
                for (int j = 0; j < delFlagPostion; j++) {
                    if (csvArrayLine0[j].trim().contains(head_moldSpec)) {
                        imoldSpec = imoldSpec + 1;
                    }
                    if (csvArrayLine0[j].trim().contains(head_componentCode)) {
                        iComponentCode = iComponentCode + 1;
                    }
                    if (csvArrayLine0[j].trim().contains(head_mainteCycleCode01.substring(0, head_mainteCycleCode01.length()-3))) {
                        additionalColCnt = additionalColCnt + 1;
                    }
                }
                /* 金型仕様と部品コードのcount End*/

                Map inMoldTypeMapTemp = null;
                Map inDepartmentMapTemp = null;
                Map inStatusMapTemp = null;

                
                if(additionalColCnt <=0 ){
                    ((ArrayList) readList.get(0)).add(25, head_mainteCycleCode02);
                    ((ArrayList) readList.get(0)).add(26, head_mainteCycleCode03);
                }
                
                for (int i = 1; i < readList.size(); i++) {
                    boolean addBlankMaintCycleCols = false;
                    int colCntAdjust = 0;
                    ArrayList comList = (ArrayList) readList.get(i);
                    
                    if(additionalColCnt <=0 ){
                        comList.add(25, "");
                        comList.add(26, "");
                        colCntAdjust = 2;
                        addBlankMaintCycleCols = true;
                    }
                    // The template included strMainteCycleCode02 & strMainteCycleCode03
                    //Boolean isNewTemplate = comList.size() == 42;
                    // If the importing file is the old template => keep the existed logic
                    // Else diff the array list to 2 items => ignore strMainteCycleCode02 & strMainteCycleCode03
                    //Integer diffCycleCodeSize = isNewTemplate ? 2 : 0;
                    
                    //ヘーダー列数と値の列数異なる場合、エラーとなる
                    if (((ArrayList) readList.get(0)).size() != comList.size()) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldId, "", error, 1, errorContents, layout));
                        failedCount += 1;
                        continue;
                    }
                    int comListSizeAdjust = addBlankMaintCycleCols ? colCntAdjust : 0;
                    String[] csvArray = new String[comList.size() + comListSizeAdjust];//diffCycleCodeSize];
                    int startSpecMoldIdx = (25+colCntAdjust);
                    
                    String strMoldId = String.valueOf(comList.get(0));//金型ID
                    
                    csvArray[0] = strMoldId;
                    String strMoldName = String.valueOf(comList.get(1));//金型名称
                    csvArray[1] = strMoldName;
                    String strMoldTypeValue = String.valueOf(comList.get(2));//金型種類
                    csvArray[2] = strMoldTypeValue;
                    String strMainAssetNo = String.valueOf(comList.get(3));//代表資産番号
                    csvArray[3] = strMainAssetNo;
                    String strMoldCreatedDate = String.valueOf(comList.get(4));//金型作成日
                    if (!StringUtils.isEmpty(strMoldCreatedDate)) {
                        strMoldCreatedDate = DateFormat.formatDateYear(strMoldCreatedDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[4] = strMoldCreatedDate.equals("-1") ? String.valueOf(comList.get(4)) : strMoldCreatedDate;;
                    String strInspectedDate = String.valueOf(comList.get(5));//検収日
                    if (!StringUtils.isEmpty(strInspectedDate)) {
                        strInspectedDate = DateFormat.formatDateYear(strInspectedDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[5] = strInspectedDate.equals("-1") ? String.valueOf(comList.get(5)) : strInspectedDate;

                    String strDepartment = String.valueOf(comList.get(6));//所属
                    csvArray[6] = strDepartment;

                    String strOwnerCompanyCode = String.valueOf(comList.get(7));//所有会社コード
                    csvArray[7] = strOwnerCompanyCode;

                    String strCompanyCode = String.valueOf(comList.get(10));//会社コード
                    csvArray[10] = strCompanyCode;
                    String strLocationCode = String.valueOf(comList.get(12));//所在地コード
                    csvArray[12] = strLocationCode;
                    String strInstllationSiteCode = String.valueOf(comList.get(14));//設置場所コード
                    csvArray[14] = strInstllationSiteCode;
                    String strStatus = String.valueOf(comList.get(16));//Status
                    csvArray[16] = strStatus;
                    String strStatusChangedDate = String.valueOf(comList.get(17));//Status变更日
                    if (!StringUtils.isEmpty(strStatusChangedDate)) {
                        strStatusChangedDate = DateFormat.formatDateYear(strStatusChangedDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[17] = strStatusChangedDate.equals("-1") ? String.valueOf(comList.get(17)) : strStatusChangedDate;

                    String strLastProductionDate = String.valueOf(comList.get(18));//LastMainteDate
                    if (!StringUtils.isEmpty(strLastProductionDate)) {
                        strLastProductionDate = DateFormat.formatDateYear(strLastProductionDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[18] = strLastProductionDate.equals("-1") ? String.valueOf(comList.get(18)) : strLastProductionDate;

                    String strTotalProductionTimeHour = String.valueOf(comList.get(19));//TotalProductionTimeHour
                    csvArray[19] = strTotalProductionTimeHour;
                    String strTotalShotCount = String.valueOf(comList.get(20));//TotalShotCount
                    csvArray[20] = strTotalShotCount;

                    String strLastMainteDate = String.valueOf(comList.get(21));//LastMainteDate
                    if (!StringUtils.isEmpty(strLastMainteDate)) {
                        strLastMainteDate = DateFormat.formatDateYear(strLastMainteDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[21] = strLastMainteDate.equals("-1") ? String.valueOf(comList.get(21)) : strLastMainteDate;

                    String strAfterMainteTotalProductionTimeHour = String.valueOf(comList.get(22));//AfterMainteTotalProductionTimeHour
                    csvArray[22] = strAfterMainteTotalProductionTimeHour;
                    String strAfterMainteTotalShotCount = String.valueOf(comList.get(23));//AfterMainteTotalShotCount
                    csvArray[23] = strAfterMainteTotalShotCount;
                    String strMainteCycleCode01 = String.valueOf(comList.get(24));//MainteCycleCode01
                    csvArray[24] = strMainteCycleCode01;
                    String strMainteCycleCode02 = "";
                    String strMainteCycleCode03 = "";
                    if(colCntAdjust > 0){
                        if(!addBlankMaintCycleCols){
                            strMainteCycleCode02 = String.valueOf(comList.get(25)); //: "";//MainteCycleCode02
                            strMainteCycleCode03 = String.valueOf(comList.get(26)); //: "";//MainteCycleCode03
                        }
                        csvArray[25] = strMainteCycleCode02;
                        csvArray[26] = strMainteCycleCode03;
                    }
                    
                    String strDelFlag = delFlagPostion <= comList.size() ? String.valueOf(comList.get(comList.size() - 1)) : null;//delFlag 指定されている場合
                    if (imoldSpec != 0) {//仕様があります時
                        for (int index = startSpecMoldIdx; index < comList.size(); index++) {
                            csvArray[index] = String.valueOf(comList.get(index));
                        }
                    }

                    boolean isExtData = FileUtil.checkExternal(entityManager, mstDictionaryService, strMoldId, loginUser).isError();

                    /**
                     * 削除の場合
                     */
                    if (strDelFlag != null && strDelFlag.trim().equals("1")) {
                        if (!mstMoldService.getMstMoldExistCheck(strMoldId)) {//MoldIdはmst_moldに存在しない

                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldId, strMoldId, error, 1, errorContents, notFound));
                        } else if (!isExtData) {
                            mstMoldService.deleteMstMold(strMoldId);
                            deletedCount = deletedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldId, strMoldId, error, 0, result, deletedMsg));
                        } else {
                            failedCount = failedCount + 1;
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldId, strMoldId, error, 1, errorContents, extEdit));
                        }
                        // 更新/登録の場合
                    } else {
                        String strMoldType = strMoldTypeValue.trim();
                        MstMold readCsvInfoMstMold = new MstMold();
                        List<MstMold> oldMstMolds = mstMoldService.getMstMoldDetailByMoldId(strMoldId).getMstMold();
                        MstMold oldMstMold = null;
                        if (null != oldMstMolds && !oldMstMolds.isEmpty()) {
                            oldMstMold = oldMstMolds.get(0);
                        }
                        MstMoldSpec readCsvInfoMstMoldSpec = null;
                        MstMoldSpecHistory readCsvInfoMstMoldSpecHistory = null;
                        MstMoldComponentRelation readCsvInfomstMoldComponentRelation = null;
                        Date dateInspectedDate = null;

                        boolean isChecked = isExtData ? mstMoldService.checkExtCsvFileData(logParm, csvArray, userLangId, logFile, i) : mstMoldService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i);

                        // CSVファイルの各入力値を存在、長さ、数字型などをチェックする
                        if (isChecked) {
                            readCsvInfoMstMold.setMoldId(strMoldId);//金型ID
                            if (!isExtData) {
                                readCsvInfoMstMold.setMoldName(strMoldName);//金型名称
                                String sMoldKey;
                                // 定義している金型種類を取得する
                                if (null == inMoldTypeMapTemp) {
                                    inMoldTypeMapTemp = mstMoldService.inMoldTypeOfChoice(loginUser.getLangId());
                                }
                                if ("".equals(strMoldType)) {
                                    sMoldKey = "0";
                                } else {
                                    sMoldKey = inMoldTypeMapTemp.get(strMoldType).toString();
                                }

                                readCsvInfoMstMold.setMoldType(Integer.parseInt(sMoldKey));//金型種類

                                if (!fu.isNullCheck(strMoldCreatedDate)) {
                                    Date dateMoldCreatedDate = fu.getDateParseForDate(strMoldCreatedDate);
                                    readCsvInfoMstMold.setMoldCreatedDate(dateMoldCreatedDate);//金型作成日
                                }

                                if (!fu.isNullCheck(strInspectedDate)) {
                                    dateInspectedDate = fu.getDateParseForDate(strInspectedDate);
                                    readCsvInfoMstMold.setInspectedDate(dateInspectedDate);//検収日
                                }

                                if (!fu.isNullCheck(strDepartment)) {
                                    strDepartment = strDepartment.trim();
                                    String sDepartmentKey;
                                    if (null == inDepartmentMapTemp) {
                                        inDepartmentMapTemp = mstMoldService.inDepartmentOfChoice(loginUser.getLangId());
                                    }
                                    if ("".equals(strDepartment)) {
                                        sDepartmentKey = "0";
                                    } else {
                                        sDepartmentKey = inDepartmentMapTemp.get(strDepartment).toString();
                                    }
                                    readCsvInfoMstMold.setDepartment(Integer.parseInt(sDepartmentKey));//所属
                                } else {
                                    readCsvInfoMstMold.setDepartment(0);//所属
                                }

                                String strOwnerCompanyId;
                                if (!fu.isNullCheck(strOwnerCompanyCode)) {
                                    MstCompanyList mstCompanyList = mstCompanyService.getMstCompanyDetail(strOwnerCompanyCode);
                                    if (mstCompanyList.getMstCompanies().size() > 0) {
                                        strOwnerCompanyId = mstCompanyList.getMstCompanies().get(0).getId();
                                        readCsvInfoMstMold.setOwnerCompanyId(strOwnerCompanyId);//所有会社
                                    }
                                }

                                String strCompanyId;
                                String strCompanyName;
                                if (!fu.isNullCheck(strCompanyCode)) {
                                    MstCompanyList mstCompanyList = mstCompanyService.getMstCompanyDetail(strCompanyCode);
                                    if (mstCompanyList.getMstCompanies().size() > 0) {
                                        strCompanyId = mstCompanyList.getMstCompanies().get(0).getId();
                                        strCompanyName = mstCompanyList.getMstCompanies().get(0).getCompanyName();
                                        readCsvInfoMstMold.setCompanyId(strCompanyId);//会社id
                                        readCsvInfoMstMold.setCompanyName(strCompanyName);//会社名称
                                    }
                                }

                                String strLocationId;
                                String strLocationName;
                                if (!fu.isNullCheck(strLocationCode)) {
                                    MstLocationList mstLocationList = mstLocationService.getMstLocationDetail(strLocationCode);
                                    if (mstLocationList.getMstLocations().size() > 0) {
                                        strLocationId = mstLocationList.getMstLocations().get(0).getId();
                                        strLocationName = mstLocationList.getMstLocations().get(0).getLocationName();
                                        readCsvInfoMstMold.setLocationId(strLocationId);//所在地id
                                        readCsvInfoMstMold.setLocationName(strLocationName);//所在地名称
                                    }
                                }

                                String strInstllationSiteId;
                                String strInstllationSiteName;
                                if (!fu.isNullCheck(strInstllationSiteCode)) {
                                    MstInstallationSiteList mstInstallationSiteList = mstInstallationSiteService.getMstInstallationSiteDetail(strInstllationSiteCode);
                                    if (mstInstallationSiteList.getMstInstallationSites().size() > 0) {
                                        strInstllationSiteId = mstInstallationSiteList.getMstInstallationSites().get(0).getId();
                                        strInstllationSiteName = mstInstallationSiteList.getMstInstallationSites().get(0).getInstallationSiteName();
                                        readCsvInfoMstMold.setInstllationSiteId(strInstllationSiteId);//設置場所名称id
                                        readCsvInfoMstMold.setInstllationSiteName(strInstllationSiteName);//設置場所名称
                                    }
                                }

                                if (strCompanyCode.equals("") && strLocationCode.equals("") && strInstllationSiteCode.equals("")) {
                                    readCsvInfoMstMold.setCompanyId(null);
                                    readCsvInfoMstMold.setCompanyName(null);
                                    readCsvInfoMstMold.setLocationId(null);
                                    readCsvInfoMstMold.setLocationName(null);
                                    readCsvInfoMstMold.setInstalledDate(null);
                                    readCsvInfoMstMold.setInstllationSiteId(null);
                                    readCsvInfoMstMold.setInstllationSiteName(null);
                                } else {
                                    readCsvInfoMstMold.setInstalledDate(new Date());
                                }

                                if (null != oldMstMold) {
                                    String newCompanyCode = StringUtils.isEmpty(strCompanyCode) ? "" : strCompanyCode;
                                    String newLocationCode = StringUtils.isEmpty(strLocationCode) ? "" : strLocationCode;
                                    String newInstallationSiteCode = StringUtils.isEmpty(strInstllationSiteCode) ? "" : strInstllationSiteCode;
                                    String oldCompanyCode = null == oldMstMold.getMstCompanyByCompanyId() ? "" : oldMstMold.getMstCompanyByCompanyId().getCompanyCode();
                                    String oldLocationCode = null == oldMstMold.getMstLocation() ? "" : oldMstMold.getMstLocation().getLocationCode();
                                    String oldInstallationSiteCode = null == oldMstMold.getMstInstallationSite() ? "" : oldMstMold.getMstInstallationSite().getInstallationSiteCode();

                                    if (oldCompanyCode.equals("") && oldLocationCode.equals("") && oldInstallationSiteCode.equals("")
                                            && newCompanyCode.equals("") && newLocationCode.equals("") && newInstallationSiteCode.equals("")) {
                                        readCsvInfoMstMold.setInstalledDate(null);
                                    } else if (!oldCompanyCode.equals("") || !oldLocationCode.equals("") || !oldInstallationSiteCode.equals("")) {
                                        if (newCompanyCode.equals("") && newLocationCode.equals("") && newInstallationSiteCode.equals("")) {
                                            if (null != oldMstMold.getInstalledDate()) {
                                                readCsvInfoMstMold.setInstalledDate(oldMstMold.getInstalledDate());
                                            }
                                            readCsvInfoMstMold.setCompanyId(null);//会社id
                                            readCsvInfoMstMold.setCompanyName(null);//会社名称
                                            readCsvInfoMstMold.setLocationId(null);//所在地id
                                            readCsvInfoMstMold.setLocationName(null);//所在地名称
                                            readCsvInfoMstMold.setInstllationSiteId(null);//設置場所名称id
                                            readCsvInfoMstMold.setInstllationSiteName(null);//設置場所名称  

                                            TblMoldLocationHistorys hVo = tblMoldLocationHistoryService.getMoldLocationHistories(strMoldId, loginUser);
                                            if (null != hVo && null != hVo.getTblMoldLocationHistoryVos() && hVo.getTblMoldLocationHistoryVos().size() > 1) {
                                                TblMoldLocationHistorys oldVo = hVo.getTblMoldLocationHistoryVos().get(1);
                                                if (!StringUtils.isEmpty(oldVo.getCompanyId())) {
                                                    readCsvInfoMstMold.setCompanyId(oldVo.getCompanyId());//会社id
                                                    readCsvInfoMstMold.setCompanyName(oldVo.getCompanyName());//会社名称
                                                } else {
                                                    readCsvInfoMstMold.setCompanyId(null);//会社id
                                                    readCsvInfoMstMold.setCompanyName(null);//会社名称
                                                }
                                                if (!StringUtils.isEmpty(oldVo.getLocationId())) {
                                                    readCsvInfoMstMold.setLocationId(oldVo.getLocationId());//所在地id
                                                    readCsvInfoMstMold.setLocationName(oldVo.getLocationName());//所在地名称
                                                } else {
                                                    readCsvInfoMstMold.setLocationId(null);
                                                    readCsvInfoMstMold.setLocationName(null);
                                                }
                                                if (!StringUtils.isEmpty(oldVo.getInstllationSiteId())) {
                                                    readCsvInfoMstMold.setInstllationSiteId(oldVo.getInstllationSiteId());//設置場所名称id
                                                    readCsvInfoMstMold.setInstllationSiteName(oldVo.getInstllationSiteName());//設置場所名称 
                                                } else {
                                                    readCsvInfoMstMold.setInstllationSiteId(null);
                                                    readCsvInfoMstMold.setInstllationSiteName(null);
                                                }
                                                readCsvInfoMstMold.setInstalledDate(fu.getDateParseForDate(oldVo.getStartDate()));
                                            } else {
                                                readCsvInfoMstMold.setInstalledDate(null);
                                            }
                                        }
                                        if ((newCompanyCode.equals(oldCompanyCode) && newLocationCode.equals(oldLocationCode) && newInstallationSiteCode.contains(oldInstallationSiteCode))) {
                                            if (null != oldMstMold.getInstalledDate()) {
                                                readCsvInfoMstMold.setInstalledDate(oldMstMold.getInstalledDate());//設置日  
                                            }
                                        }
                                    }
                                }

                                inStatusMapTemp = null == inStatusMapTemp ? mstMoldService.inStatusOfChoice(loginUser.getLangId()) : inStatusMapTemp;
                                String sStatusKey = inStatusMapTemp.get(strStatus).toString();
                                readCsvInfoMstMold.setStatus(Integer.parseInt(sStatusKey));//ステータス
                            }

                            if (fu.isNullCheck(strMainAssetNo)) {
                                readCsvInfoMstMold.setMainAssetNo(null);//代表資産番号
                            } else {
                                readCsvInfoMstMold.setMainAssetNo(strMainAssetNo);//代表資産番号
                            }
                            if (!fu.isNullCheck(strLastProductionDate)) {
                                readCsvInfoMstMold.setLastProductionDate(fu.getDateParseForDate(strLastProductionDate));
                            } else {
                                readCsvInfoMstMold.setLastProductionDate(null);
                            }
                            if (!fu.isNullCheck(strTotalProductionTimeHour)) {
                                readCsvInfoMstMold.setTotalProducingTimeHour(new BigDecimal(strTotalProductionTimeHour)); //Integer.parseInt(strTotalProductionTimeHour));
                            } else {
                                readCsvInfoMstMold.setTotalProducingTimeHour(BigDecimal.ZERO);
                            }
                            if (!fu.isNullCheck(strTotalShotCount)) {
                                readCsvInfoMstMold.setTotalShotCount(Integer.parseInt(strTotalShotCount));
                            } else {
                                readCsvInfoMstMold.setTotalShotCount(0);
                            }
                            if (!fu.isNullCheck(strLastMainteDate)) {
                                readCsvInfoMstMold.setLastMainteDate(fu.getDateParseForDate(strLastMainteDate));
                            } else {
                                readCsvInfoMstMold.setLastMainteDate(null);
                            }
                            if (!fu.isNullCheck(strAfterMainteTotalProductionTimeHour)) {
                                readCsvInfoMstMold.setAfterMainteTotalProducingTimeHour(new BigDecimal(strAfterMainteTotalProductionTimeHour));
                            } else {
                                readCsvInfoMstMold.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
                            }
                            if (!fu.isNullCheck(strAfterMainteTotalShotCount)) {
                                readCsvInfoMstMold.setAfterMainteTotalShotCount(Integer.parseInt(strAfterMainteTotalShotCount));
                            } else {
                                readCsvInfoMstMold.setAfterMainteTotalShotCount(0);
                            }
                            readCsvInfoMstMold.setBlMaintenanceCyclePtn01(null);
                            if(colCntAdjust > 0){
                                readCsvInfoMstMold.setBlMaintenanceCyclePtn02(null);
                                readCsvInfoMstMold.setBlMaintenanceCyclePtn03(null);
                            } else {
                                readCsvInfoMstMold.setBlMaintenanceCyclePtn02(oldMstMold.getBlMaintenanceCyclePtn02());
                                readCsvInfoMstMold.setBlMaintenanceCyclePtn03(oldMstMold.getBlMaintenanceCyclePtn03());
                            }
                            if (!(StringUtils.isEmpty(strMainteCycleCode01))) {
                                List<TblMaintenanceCyclePtn> maintenanceCyclePtns = tblMaintenanceCyclePtnService.getTblMaintenanceCyclePtnsByTypeAndCodes(1, strMainteCycleCode01);
                                for (TblMaintenanceCyclePtn maintenanceCyclePtn : maintenanceCyclePtns) {
                                    if (maintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getCycleCode().equals(strMainteCycleCode01)) {
                                        readCsvInfoMstMold.setMainteCycleId01(maintenanceCyclePtn.getId());
                                    }
                                    if(colCntAdjust > 0){
                                        if (maintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getCycleCode().equals(strMainteCycleCode02)) {
                                            readCsvInfoMstMold.setMainteCycleId02(maintenanceCyclePtn.getId());
                                        }
                                        if (maintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getCycleCode().equals(strMainteCycleCode03)) {
                                            readCsvInfoMstMold.setMainteCycleId03(maintenanceCyclePtn.getId());
                                        }
                                    } else {
                                        readCsvInfoMstMold.setMainteCycleId02(oldMstMold.getMainteCycleId02());
                                        readCsvInfoMstMold.setMainteCycleId03(oldMstMold.getMainteCycleId03());
                                    }
                                }
                            }

                            String strMoldUuId;
                            // 更新の場合
                            if (null != oldMstMolds && !oldMstMolds.isEmpty()) {
                                strMoldUuId = oldMstMold.getUuid();

                                int count = 0;
                                // 外部の場合、代表資産番号しか更新できない
                                if (isExtData) {
                                    count = mstMoldService.updateExternalMstMold(readCsvInfoMstMold, strMoldId);
                                    // エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldId, readCsvInfoMstMold.getMoldId(), error, 0, result, updatedMsg));
                                    updatedCount = updatedCount + count;
                                } else {

                                    Date sysDate = new Date();
                                    readCsvInfoMstMold.setUpdateDate(sysDate);
                                    readCsvInfoMstMold.setUpdateUserUuid(loginUser.getUserUuid());
                                    //ステータス日
                                    //如果status没有改变把StatusChangedDate改回旧的
                                    if (isExtData || (oldMstMold.getStatus() != null && readCsvInfoMstMold.getStatus().compareTo(oldMstMold.getStatus()) == 0)) {
                                        readCsvInfoMstMold.setStatusChangedDate(oldMstMold.getStatusChangedDate());
                                    } else {
                                        readCsvInfoMstMold.setStatusChangedDate(new Date());
                                    }

                                    mstMoldService.deleteMoldMaintenanceRecomend(oldMstMold, readCsvInfoMstMold);
                                    // データを更新
                                    count = mstMoldService.updateMstMoldByQuery(readCsvInfoMstMold);

                                    if (!isExtData && comList.size() >= startSpecMoldIdx+1) {
                                        //headは"金型仕様"がありますとき
                                        MstMoldSpecHistory oldMoldSpecHistory;
                                        if (strHead.contains(head_moldSpec)) {
                                            oldMoldSpecHistory = mstMoldSpecHistoryService.getMstMoldSpecHistoryLatestByMoldUuidFromMoldCsv(strMoldUuId);//最新のバージョン

                                            //金型種類変更時 すべての金型仕様履歴を削除し
                                            if (oldMoldSpecHistory == null || FileUtil.getIntegerValue(oldMstMold.getMoldType()).compareTo(FileUtil.getIntegerValue(readCsvInfoMstMold.getMoldType())) != 0) {
                                                mstMoldSpecHistoryService.deleteMstMoldSpecHistory(strMoldUuId);

                                                String strHistoryId = IDGenerator.generate();

                                                readCsvInfoMstMoldSpecHistory = new MstMoldSpecHistory();
                                                readCsvInfoMstMoldSpecHistory.setMoldUuid(strMoldUuId);
                                                readCsvInfoMstMoldSpecHistory.setStartDate(null == readCsvInfoMstMold.getCreateDate() ? new Date() : readCsvInfoMstMold.getCreateDate());//金型作成日
                                                readCsvInfoMstMoldSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//システム最大日付
                                                readCsvInfoMstMoldSpecHistory.setId(strHistoryId);
                                                String firstVersion = mstDictionaryService.getDictionaryValue(userLangId, "mold_spec_first_version");
                                                readCsvInfoMstMoldSpecHistory.setMoldSpecName(firstVersion);//"最初のバージョン" (文言キー:mold_spec_first_version)
                                                readCsvInfoMstMoldSpecHistory.setCreateDate(sysDate);
                                                readCsvInfoMstMoldSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
                                                readCsvInfoMstMoldSpecHistory.setUpdateDate(sysDate);
                                                readCsvInfoMstMoldSpecHistory.setUpdateUserUuid(loginUser.getUserUuid());
                                                mstMoldSpecHistoryService.createMstMoldSpecHistoryByCsv(readCsvInfoMstMoldSpecHistory);

                                                MstMoldAttributeList mstMoldAttribute = mstMoldAttributeService.getMstMoldAttributes(String.valueOf(readCsvInfoMstMold.getMoldType()));
                                                int attrCode = mstMoldAttribute.getMstMoldAttribute().size();

                                                int iFor;
                                                if (comList.size() - startSpecMoldIdx+1 > attrCode) {
                                                    iFor = attrCode;
                                                } else {
                                                    iFor = comList.size() - startSpecMoldIdx+1;
                                                }

                                                for (int j = 0; j < iFor; j++) {
                                                    String strAttrValue = String.valueOf(comList.get(j + startSpecMoldIdx)).trim();
                                                    String strAttrId = mstMoldAttribute.getMstMoldAttribute().get(j).getId();

                                                    if (fu.maxLangthCheck(strAttrValue, 256)) {
                                                        //エラー情報をログファイルに記入
                                                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldSpec.concat(String.valueOf(j + 25)), strAttrValue, error, 1, errorContents, maxLangth));
                                                        failedCount = failedCount + 1;
                                                        continue;
                                                    } else {
                                                        MstMoldSpecPK newPk = new MstMoldSpecPK();
                                                        newPk.setAttrId(strAttrId);
                                                        newPk.setMoldSpecHstId(strHistoryId);
                                                        readCsvInfoMstMoldSpec = new MstMoldSpec();
                                                        readCsvInfoMstMoldSpec.setId(IDGenerator.generate());
                                                        readCsvInfoMstMoldSpec.setAttrValue(strAttrValue);
                                                        readCsvInfoMstMoldSpec.setMstMoldSpecPK(newPk);
                                                        readCsvInfoMstMoldSpec.setCreateDate(sysDate);
                                                        readCsvInfoMstMoldSpec.setCreateUserUuid(loginUser.getUserUuid());
                                                        readCsvInfoMstMoldSpec.setUpdateDate(sysDate);
                                                        readCsvInfoMstMoldSpec.setUpdateUserUuid(loginUser.getUserUuid());
                                                    }
                                                    if (!mstMoldSpecService.getMstMoldSpecsFK(strHistoryId, strAttrId)) {
                                                        mstMoldSpecService.createMstMoldSpecByCsv(readCsvInfoMstMoldSpec);
                                                    }
                                                }

                                            } else {
                                                readCsvInfoMstMoldSpec = new MstMoldSpec();
                                                String strNewMstMoldSpecHistoryById = oldMoldSpecHistory.getId();
                                                MstMoldAttributeList mstMoldAttribute = mstMoldAttributeService.getMstMoldAttributes(String.valueOf(readCsvInfoMstMold.getMoldType()));
                                                int attrCode = mstMoldAttribute.getMstMoldAttribute().size();
                                                int iFor;
                                                if (comList.size() - startSpecMoldIdx+1 > attrCode) {
                                                    iFor = attrCode;
                                                } else {
                                                    iFor = comList.size() - startSpecMoldIdx+1;
                                                }
                                                for (int j = 0; j < iFor; j++) {
                                                    String strAttrValue = String.valueOf(comList.get(j + startSpecMoldIdx)).trim();
                                                    String strAttrId = mstMoldAttribute.getMstMoldAttribute().get(j).getId();

                                                    if (fu.maxLangthCheck(strAttrValue, 256)) {
                                                        //エラー情報をログファイルに記入
                                                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldSpec.concat(String.valueOf(j + 30)), strAttrValue, error, 1, errorContents, maxLangth));
                                                        failedCount = failedCount + 1;
                                                        continue;
                                                    } else {
                                                        MstMoldSpecPK newPk = new MstMoldSpecPK();
                                                        newPk.setAttrId(strAttrId);
                                                        newPk.setMoldSpecHstId(strNewMstMoldSpecHistoryById);
                                                        readCsvInfoMstMoldSpec.setAttrValue(strAttrValue);
                                                        readCsvInfoMstMoldSpec.setMstMoldSpecPK(newPk);
                                                        readCsvInfoMstMoldSpec.setUpdateDate(sysDate);
                                                        readCsvInfoMstMoldSpec.setUpdateUserUuid(loginUser.getUserUuid());
                                                    }
                                                    if (mstMoldSpecService.getMstMoldSpecsFK(strNewMstMoldSpecHistoryById, strAttrId)) {
                                                        mstMoldSpecService.updateMstMoldSpecByQuery(readCsvInfoMstMoldSpec);
                                                    }
                                                }
                                            }
                                        }

                                        if (strHead.contains(head_componentCode)) {
                                            readCsvInfomstMoldComponentRelation = new MstMoldComponentRelation();
                                            mstMoldComponentRelationService.deleteMstMold(oldMstMold.getUuid());
                                            for (int j = 0, index = startSpecMoldIdx + imoldSpec; j < iComponentCode; j++, index += 2) {
                                                if (index + j >= comList.size()) {
                                                    break;
                                                }
                                                String strComponentCode = String.valueOf(comList.get(index)).trim();
                                                MstComponentList mstComponentList = mstComponentService.getMstComponentDetail(strComponentCode);
                                                if (mstComponentList.getMstComponents().size() > 0) {
                                                    String strComponentId = mstComponentList.getMstComponents().get(0).getId();
                                                    if (!fu.isNullCheck(strComponentId)) {
                                                        if (!mstMoldComponentRelationService.mstMoldComponentRelationExistCheckByMoldUuid(strMoldUuId, strComponentId)) {
                                                            //金型部品関係マスタ
                                                            MstMoldComponentRelationPK newPk = new MstMoldComponentRelationPK();
                                                            newPk.setMoldUuid(strMoldUuId);
                                                            newPk.setComponentId(strComponentId);
                                                            readCsvInfomstMoldComponentRelation.setMstMoldComponentRelationPK(newPk);
                                                            readCsvInfomstMoldComponentRelation.setCreateDate(sysDate);
                                                            readCsvInfomstMoldComponentRelation.setCreateUserUuid(loginUser.getUserUuid());
                                                            readCsvInfomstMoldComponentRelation.setUpdateDate(sysDate);
                                                            readCsvInfomstMoldComponentRelation.setUpdateUserUuid(loginUser.getUserUuid());
                                                            String strCount = String.valueOf(comList.get(index + 1)).trim();
                                                            if (!StringUtils.isEmpty(strCount)) {
                                                                readCsvInfomstMoldComponentRelation.setCountPerShot(Integer.parseInt(strCount));
                                                            } else {
                                                                readCsvInfomstMoldComponentRelation.setCountPerShot(0);
                                                            }
                                                            mstMoldComponentRelationService.createMstMoldComponentRelationByCsv(readCsvInfomstMoldComponentRelation);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        String oldCompanyId = null == oldMstMold.getCompanyId() ? "" : oldMstMold.getCompanyId();
                                        String oldLocationId = null == oldMstMold.getLocationId() ? "" : oldMstMold.getLocationId();
                                        String oldInstllationSiteId = null == oldMstMold.getInstllationSiteId() ? "" : oldMstMold.getInstllationSiteId();
                                        String newCompanyId = null == readCsvInfoMstMold.getCompanyId() ? "" : readCsvInfoMstMold.getCompanyId();
                                        String newLocationId = null == readCsvInfoMstMold.getLocationId() ? "" : readCsvInfoMstMold.getLocationId();
                                        String newInstllationSiteId = null == readCsvInfoMstMold.getInstllationSiteId() ? "" : readCsvInfoMstMold.getInstllationSiteId();
                                        if (!oldCompanyId.equals(newCompanyId) || !oldLocationId.equals(newLocationId) || !oldInstllationSiteId.equals(newInstllationSiteId)) {
                                            tblMoldLocationHistoryService.updateLatestTblMoldLocationHistoryDateByMoldUuid(strMoldUuId);

                                            if (!strCompanyCode.equals("") || !strLocationCode.equals("") || !strInstllationSiteCode.equals("")) {
                                                TblMoldLocationHistory readCsvInfoTblMoldLocationHistory = new TblMoldLocationHistory();
                                                readCsvInfoTblMoldLocationHistory.setId(IDGenerator.generate());
                                                readCsvInfoTblMoldLocationHistory.setMoldUuid(strMoldUuId);
                                                readCsvInfoTblMoldLocationHistory.setChangeReason(0); //変更理由：なし（ゼロ）
                                                if (null != readCsvInfoMstMold.getCompanyId()) {
                                                    readCsvInfoTblMoldLocationHistory.setCompanyId(readCsvInfoMstMold.getCompanyId());
                                                    readCsvInfoTblMoldLocationHistory.setCompanyName(null == readCsvInfoMstMold.getCompanyName() ? null : readCsvInfoMstMold.getCompanyName());
                                                }
                                                if (null != readCsvInfoMstMold.getLocationId()) {
                                                    readCsvInfoTblMoldLocationHistory.setLocationId(readCsvInfoMstMold.getLocationId());
                                                    readCsvInfoTblMoldLocationHistory.setLocationName(null == readCsvInfoMstMold.getLocationName() ? readCsvInfoMstMold.getLocationName() : readCsvInfoMstMold.getLocationName());
                                                }
                                                if (null != readCsvInfoMstMold.getInstllationSiteId()) {
                                                    readCsvInfoTblMoldLocationHistory.setInstllationSiteId(readCsvInfoMstMold.getInstllationSiteId());
                                                    readCsvInfoTblMoldLocationHistory.setInstllationSiteName(null == readCsvInfoMstMold.getInstllationSiteName() ? null : readCsvInfoMstMold.getInstllationSiteName());
                                                }
                                                readCsvInfoTblMoldLocationHistory.setCreateDate(new Date());
                                                readCsvInfoTblMoldLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
                                                readCsvInfoTblMoldLocationHistory.setStartDate(null == readCsvInfoMstMold.getInstalledDate() ? new Date() : readCsvInfoMstMold.getInstalledDate());
                                                readCsvInfoTblMoldLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);
                                                tblMoldLocationHistoryService.creatMoldLocationHistories(readCsvInfoTblMoldLocationHistory);
                                            }
                                        }
                                        if ((!oldCompanyId.isEmpty() || !oldLocationId.isEmpty() || !oldInstllationSiteId.isEmpty())
                                                && (strCompanyCode.equals("") && strLocationCode.equals("") && strInstllationSiteCode.equals(""))) {
                                            tblMoldLocationHistoryService.deleteMoldLocationHistorieLatest(strMoldUuId);
                                        }
                                    }

                                    updatedCount = updatedCount + count;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldId, readCsvInfoMstMold.getMoldId(), error, 0, result, updatedMsg));
                                }
                            } else {
                                //ライセンス リミット
                                BasicResponse response = new BasicResponse();
                                if(mstLicenseLimitService.CheckMoldLimit(loginUser.getLangId(),response)){
                                //追加
                                Date sysDate = new Date();
                                strMoldUuId = IDGenerator.generate();
                                Date moldCreatDate = readCsvInfoMstMold.getMoldCreatedDate();
                                readCsvInfoMstMold.setUuid(strMoldUuId);
                                readCsvInfoMstMold.setInspectedDate(dateInspectedDate);
                                readCsvInfoMstMold.setMainteStatus(0);
                                readCsvInfoMstMold.setCreateDate(sysDate);
                                readCsvInfoMstMold.setCreateUserUuid(loginUser.getUserUuid());
                                readCsvInfoMstMold.setUpdateDate(sysDate);
                                readCsvInfoMstMold.setUpdateUserUuid(loginUser.getUserUuid());

                                readCsvInfoMstMold.setStatusChangedDate(new Date());//ステータス日


                                
                                    if (comList.size() >= startSpecMoldIdx+1) {
                                        //headは"金型仕様"がありますとき
                                        if (strHead.contains(head_moldSpec)) {
                                            List<MstMoldSpecHistory> MstMoldSpecHistorys = new ArrayList<>();
                                            String strHistoryId = IDGenerator.generate();

                                            readCsvInfoMstMoldSpecHistory = new MstMoldSpecHistory();
                                            readCsvInfoMstMoldSpecHistory.setMoldUuid(strMoldUuId);
                                            readCsvInfoMstMoldSpecHistory.setStartDate(null == moldCreatDate ? new Date() : moldCreatDate);//金型作成日
                                            readCsvInfoMstMoldSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//システム最大日付
                                            readCsvInfoMstMoldSpecHistory.setId(strHistoryId);
                                            String firstVersion = mstDictionaryService.getDictionaryValue(userLangId, "mold_spec_first_version");
                                            readCsvInfoMstMoldSpecHistory.setMoldSpecName(firstVersion);//"最初のバージョン" (文言キー:mold_spec_first_version)
                                            readCsvInfoMstMoldSpecHistory.setCreateDate(sysDate);
                                            readCsvInfoMstMoldSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
                                            readCsvInfoMstMoldSpecHistory.setUpdateDate(sysDate);
                                            readCsvInfoMstMoldSpecHistory.setUpdateUserUuid(loginUser.getUserUuid());

                                            MstMoldSpecHistorys.add(readCsvInfoMstMoldSpecHistory);
                                            readCsvInfoMstMold.setMstMoldSpecHistoryCollection(MstMoldSpecHistorys);

                                            MstMoldAttributeList mstMoldAttribute = mstMoldAttributeService.getMstMoldAttributes(String.valueOf(readCsvInfoMstMold.getMoldType()));
                                            int attrCode = mstMoldAttribute.getMstMoldAttribute().size();

                                            int iFor;
                                            if (comList.size() - startSpecMoldIdx+1 > attrCode) {

                                                iFor = attrCode;
                                            } else {

                                                iFor = comList.size() - startSpecMoldIdx+1;
                                            }
                                            List<MstMoldSpec> mstMoldSpecs = new ArrayList<>();
                                            for (int j = 0; j < iFor; j++) {
                                                String strAttrValue = String.valueOf(comList.get(j + startSpecMoldIdx)).trim();
                                                String strAttrId = mstMoldAttribute.getMstMoldAttribute().get(j).getId();

                                                if (fu.maxLangthCheck(strAttrValue, 256)) {
                                                    //エラー情報をログファイルに記入
                                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldSpec.concat(String.valueOf(j + 25)), strAttrValue, error, 1, errorContents, maxLangth));
                                                    failedCount = failedCount + 1;
                                                    continue;
                                                } else {
                                                    MstMoldSpecPK newPk = new MstMoldSpecPK();
                                                    newPk.setAttrId(strAttrId);
                                                    newPk.setMoldSpecHstId(strHistoryId);
                                                    readCsvInfoMstMoldSpec = new MstMoldSpec();
                                                    readCsvInfoMstMoldSpec.setId(IDGenerator.generate());
                                                    readCsvInfoMstMoldSpec.setAttrValue(strAttrValue);
                                                    readCsvInfoMstMoldSpec.setMstMoldSpecPK(newPk);
                                                    readCsvInfoMstMoldSpec.setCreateDate(sysDate);
                                                    readCsvInfoMstMoldSpec.setCreateUserUuid(loginUser.getUserUuid());
                                                    readCsvInfoMstMoldSpec.setUpdateDate(sysDate);
                                                    readCsvInfoMstMoldSpec.setUpdateUserUuid(loginUser.getUserUuid());
                                                }
                                                if (!mstMoldSpecService.getMstMoldSpecsFK(strHistoryId, strAttrId)) {

                                                    mstMoldSpecs.add(readCsvInfoMstMoldSpec);
                                                }
                                            }

                                            readCsvInfoMstMoldSpecHistory.setMstMoldSpecCollection(mstMoldSpecs);
                                        }
                                        if (strHead.contains(head_componentCode)) {
                                            List<MstMoldComponentRelation> mstMoldComponentRelations = new ArrayList<>();
                                            for (int j = 0, index = startSpecMoldIdx + imoldSpec; j < iComponentCode; j++, index += 2) {
                                                if (index + j >= comList.size()) {
                                                    break;
                                                }
                                                String strComponentCode = String.valueOf(comList.get(index)).trim();
                                                MstComponentList mstComponentList = mstComponentService.getMstComponentDetail(strComponentCode);
                                                if (mstComponentList.getMstComponents().size() > 0) {
                                                    String strComponentId = mstComponentList.getMstComponents().get(0).getId();
                                                    if (!fu.isNullCheck(strComponentId)) {
                                                        if (!mstMoldComponentRelationService.mstMoldComponentRelationExistCheckByMoldUuid(strMoldUuId, strComponentId)) {
                                                            //金型部品関係マスタ
                                                            MstMoldComponentRelationPK newPk = new MstMoldComponentRelationPK();
                                                            newPk.setMoldUuid(strMoldUuId);

                                                            newPk.setComponentId(strComponentId);
                                                            readCsvInfomstMoldComponentRelation = new MstMoldComponentRelation();
                                                            readCsvInfomstMoldComponentRelation.setMstMoldComponentRelationPK(newPk);
                                                            readCsvInfomstMoldComponentRelation.setCreateDate(sysDate);
                                                            readCsvInfomstMoldComponentRelation.setCreateUserUuid(loginUser.getUserUuid());
                                                            readCsvInfomstMoldComponentRelation.setUpdateDate(sysDate);
                                                            readCsvInfomstMoldComponentRelation.setUpdateUserUuid(loginUser.getUserUuid());
                                                            String strCount = String.valueOf(comList.get(index + 1)).trim();
                                                            if (!StringUtils.isEmpty(strCount)) {
                                                                readCsvInfomstMoldComponentRelation.setCountPerShot(Integer.parseInt(strCount));
                                                            } else {
                                                                readCsvInfomstMoldComponentRelation.setCountPerShot(0);
                                                            }
                                                            mstMoldComponentRelations.add(readCsvInfomstMoldComponentRelation);
                                                        }
                                                    }

                                                }
                                            }

                                            readCsvInfoMstMold.setMstMoldComponentRelationCollection(mstMoldComponentRelations);
                                        }
                                        if (null != readCsvInfoMstMold.getCompanyId() || null != readCsvInfoMstMold.getLocationId() || null != readCsvInfoMstMold.getInstllationSiteId()) {
                                            List<TblMoldLocationHistory> hs = new ArrayList<>();
                                            TblMoldLocationHistory readCsvInfoTblMoldLocationHistory = new TblMoldLocationHistory();
                                            readCsvInfoTblMoldLocationHistory.setId(IDGenerator.generate());
                                            readCsvInfoTblMoldLocationHistory.setMoldUuid(readCsvInfoMstMold.getUuid());
                                            readCsvInfoTblMoldLocationHistory.setChangeReason(0); //変更理由：なし（ゼロ）
                                            if (null != readCsvInfoMstMold.getCompanyId()) {
                                                readCsvInfoTblMoldLocationHistory.setCompanyId(readCsvInfoMstMold.getCompanyId());
                                                readCsvInfoTblMoldLocationHistory.setCompanyName(null == readCsvInfoMstMold.getCompanyName() ? null : readCsvInfoMstMold.getCompanyName());
                                            }
                                            if (null != readCsvInfoMstMold.getLocationId()) {
                                                readCsvInfoTblMoldLocationHistory.setLocationId(readCsvInfoMstMold.getLocationId());
                                                readCsvInfoTblMoldLocationHistory.setLocationName(null == readCsvInfoMstMold.getLocationName() ? readCsvInfoMstMold.getLocationName() : readCsvInfoMstMold.getLocationName());
                                            }
                                            if (null != readCsvInfoMstMold.getInstllationSiteId()) {
                                                readCsvInfoTblMoldLocationHistory.setInstllationSiteId(readCsvInfoMstMold.getInstllationSiteId());
                                                readCsvInfoTblMoldLocationHistory.setInstllationSiteName(null == readCsvInfoMstMold.getInstllationSiteName() ? null : readCsvInfoMstMold.getInstllationSiteName());
                                            }
                                            readCsvInfoTblMoldLocationHistory.setCreateDate(new Date());
                                            readCsvInfoTblMoldLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
                                            readCsvInfoTblMoldLocationHistory.setStartDate(null == readCsvInfoMstMold.getInstalledDate() ? new Date() : readCsvInfoMstMold.getInstalledDate());
                                            readCsvInfoTblMoldLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);
                                            hs.add(readCsvInfoTblMoldLocationHistory);
                                            readCsvInfoMstMold.setTblMoldLocationHistoryCollection(hs);
                                        }

                                        mstMoldService.createMstMoldByCsv(readCsvInfoMstMold);
                                    }
                                    
                                            // 金型資産番号マスタ更新/追加
                                            Date updDate = new Date();
                                            List<Object[]> moldAssetNoList = mstMoldAssetNoService.getSqlList(strMoldId);
                                            // 該当金型IDで資産番号マスタにデータがある場合
                                            if (moldAssetNoList != null && moldAssetNoList.size() > 0) {
                                                // CSVに入力した値を資産番号マスタに存在するかどうかフラグ
                                                boolean existFlg = false;
                                                // 全て存在している代表資産番号の代表フラグを0に更新され、CSVの入力した代表資産番号の代表フラグは1に更新する
                                                for (Object[] objs : moldAssetNoList) {
                                                    MstMoldAssetNo mstMoldAssetNo = new MstMoldAssetNo(strMoldUuId, String.valueOf(objs[3]));
                                                    mstMoldAssetNo.setMainFlg(0);
                                                    if (objs[3].equals(strMainAssetNo)) {
                                                        existFlg = true;
                                                        mstMoldAssetNo.setMainFlg(1);
                                                    }
                                                    mstMoldAssetNo.setUpdateDate(updDate);
                                                    mstMoldAssetNo.setUpdateUserUuid(loginUser.getUserUuid());
                                                    mstMoldAssetNoService.updateMstMoldAssetNoMainFlg(mstMoldAssetNo);
                                                }
                                                // 存在フラグが不存在且つ入力値がある場合、該当資産番号を追加する
                                                if (!existFlg && strMainAssetNo != null && !"".equals(strMainAssetNo)) {
                                                    // 追加
                                                    MstMoldAssetNo mstMoldAssetNo = new MstMoldAssetNo(strMoldUuId, strMainAssetNo);
                                                    mstMoldAssetNo.setId(IDGenerator.generate());
                                                    mstMoldAssetNo.setMainFlg(1);
                                                    mstMoldAssetNo.setNumberedDate(updDate);
                                                    mstMoldAssetNo.setAssetAmount(BigDecimal.ZERO);
                                                    mstMoldAssetNo.setCreateDate(updDate);
                                                    mstMoldAssetNo.setCreateUserUuid(loginUser.getUserUuid());
                                                    mstMoldAssetNo.setUpdateDate(updDate);
                                                    mstMoldAssetNo.setUpdateUserUuid(loginUser.getUserUuid());
                                                    mstMoldAssetNoService.createMstMoldAssetNo(mstMoldAssetNo);
                                                }
                                            } else // 入力値がある場合
                                            {
                                                if (strMainAssetNo != null && !"".equals(strMainAssetNo)) {
                                                    // 追加
                                                    MstMoldAssetNo mstMoldAssetNo = new MstMoldAssetNo(strMoldUuId, strMainAssetNo);
                                                    mstMoldAssetNo.setId(IDGenerator.generate());
                                                    mstMoldAssetNo.setMainFlg(1);
                                                    mstMoldAssetNo.setNumberedDate(updDate);
                                                    mstMoldAssetNo.setAssetAmount(BigDecimal.ZERO);
                                                    mstMoldAssetNo.setCreateDate(updDate);
                                                    mstMoldAssetNo.setCreateUserUuid(loginUser.getUserUuid());
                                                    mstMoldAssetNo.setUpdateDate(updDate);
                                                    mstMoldAssetNo.setUpdateUserUuid(loginUser.getUserUuid());
                                                    mstMoldAssetNoService.createMstMoldAssetNo(mstMoldAssetNo);
                                                }
                                            }          
                                    addedCount = addedCount + 1;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldId, readCsvInfoMstMold.getMoldId(), error, 0, result, addedMsg));
                                //}

                            }
                            else
                            {
                               failedCount = failedCount + 1;
                               fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_moldId, readCsvInfoMstMold.getMoldId(), error, 1, result, response.getErrorMessage()));
                            } 
                        }
                        } else {
                            failedCount = failedCount + 1;
                        }
                    }
                }
            } catch (Exception e) {
                importResultResponse.setError(true);
                importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid");
                importResultResponse.setErrorMessage(msg);
                return importResultResponse;
            }
        }
        // リターン情報
        succeededCount = addedCount + updatedCount + deletedCount;
        importResultResponse.setTotalCount(readList.size() - 1);
        importResultResponse.setSucceededCount(succeededCount);
        importResultResponse.setAddedCount(addedCount);
        importResultResponse.setUpdatedCount(updatedCount);
        importResultResponse.setDeletedCount(deletedCount);
        importResultResponse.setFailedCount(failedCount);
        importResultResponse.setLog(logFileUuid);

        //アップロードログをテーブルに書き出し
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_MOLD);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MOLD);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;

    }
    /**
     * 金型マスタCSV出力
     *
     * @param moldId
     * @param moldName
     * @param mainAssetNo
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param moldType
     * @param department
     * @param lastProductionDateFrom
     * @param lastProductionDateTo
     * @param totalProducingTimeHourFrom
     * @param totalProducingTimeHourTo
     * @param moldCreatedDateFrom
     * @param moldCreatedDateTo
     * @param totalShotCountFrom
     * @param totalShotCountTo
     * @param lastMainteDateFrom
     * @param lastMainteDateTo
     * @param afterMainteTotalProducingTimeHourFrom
     * @param afterMainteTotalProducingTimeHourTo
     * @param afterMainteTotalShotCountFrom
     * @param afterMainteTotalShotCountTo
     * @param status
     * @return
     */
    @GET
    @Path("exportmoldpartcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMoldPartsCSV(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("moldType") String moldType,
            @QueryParam("department") String department,
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("moldCreatedDateFrom") String moldCreatedDateFrom, // 金型作成日yyyy/MM/dd
            @QueryParam("moldCreatedDateTo") String moldCreatedDateTo, // 金型作成日yyyy/MM/dd
            @QueryParam("totalProducingTimeHourFrom") String totalProducingTimeHourFrom, // 累計生産時間 数字
            @QueryParam("totalProducingTimeHourTo") String totalProducingTimeHourTo, // 累計生産時間 数字
            @QueryParam("totalShotCountFrom") String totalShotCountFrom, // 累計ショット数 数字
            @QueryParam("totalShotCountTo") String totalShotCountTo, //　累計ショット数数字
            @QueryParam("afterMainteTotalProducingTimeHourFrom") String afterMainteTotalProducingTimeHourFrom, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalProducingTimeHourTo") String afterMainteTotalProducingTimeHourTo, // メンテナンス後生産時間 数字
            @QueryParam("afterMainteTotalShotCountFrom") String afterMainteTotalShotCountFrom, // メンテナンス後ショット数 数字
            @QueryParam("afterMainteTotalShotCountTo") String afterMainteTotalShotCountTo, // メンテナンス後ショット数 数字
            @QueryParam("status") String status) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstMoldService.outMoldTypeOfChoice(loginUser.getLangId());
        mstMoldService.outStatusOfChoice(loginUser.getLangId());

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        // 最後生産日From - To
        Date formatLastProductionDateFrom = null;
        Date formatLastProductionDateTo = null;
        // 最後メンテ日From - To
        Date formatLastMainteDateFrom = null;
        Date formatLastMainteDateTo = null;
        // 設備作成日From - To
        Date formatMoldCreatedDateFrom = null;
        Date formatMoldCreatedDateTo = null;
        // 累計生産時間 From - To
        Integer formatTotalProducingTimeHourFrom = null;
        Integer formatTotalProducingTimeHourTo = null;

        // 累計ショット数 From - To
        Integer formatTotalShotCountFrom = null;
        Integer formatTotalShotCountTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalProducingTimeHourFrom = null;
        Integer formatAfterMainteTotalProducingTimeHourTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalShotCountFrom = null;
        Integer formatAfterMainteTotalShotCountTo = null;

        Integer formatMoldType = null;
        Integer formatDepartment = null;
        Integer formatStatus = null;
        try {
            // 最後生産日From - To
            if (lastProductionDateFrom != null && !"".equals(lastProductionDateFrom)) {
                formatLastProductionDateFrom = sdf.parse(lastProductionDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (lastProductionDateTo != null && !"".equals(lastProductionDateTo)) {
                formatLastProductionDateTo = sdf.parse(lastProductionDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 最後メンテ日From - To
            if (lastMainteDateFrom != null && !"".equals(lastMainteDateFrom)) {
                formatLastMainteDateFrom = sdf.parse(lastMainteDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {

            if (lastMainteDateTo != null && !"".equals(lastMainteDateTo)) {
                formatLastMainteDateTo = sdf.parse(lastMainteDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {
            // 金型作成日From - To
            if (moldCreatedDateFrom != null && !"".equals(moldCreatedDateFrom)) {
                formatMoldCreatedDateFrom = sdf.parse(moldCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (moldCreatedDateTo != null && !"".equals(moldCreatedDateTo)) {
                formatMoldCreatedDateTo = sdf.parse(moldCreatedDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 累計生産時間 From - To
            if (totalProducingTimeHourFrom != null && !"".equals(totalProducingTimeHourFrom)) {
                formatTotalProducingTimeHourFrom = Integer.parseInt(totalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalProducingTimeHourTo != null && !"".equals(totalProducingTimeHourTo)) {
                formatTotalProducingTimeHourTo = Integer.parseInt(totalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountFrom != null && !"".equals(totalShotCountFrom)) {
                formatTotalShotCountFrom = Integer.parseInt(totalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountTo != null && !"".equals(totalShotCountTo)) {
                formatTotalShotCountTo = Integer.parseInt(totalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourFrom != null && !"".equals(afterMainteTotalProducingTimeHourFrom)) {
                formatAfterMainteTotalProducingTimeHourFrom = Integer.parseInt(afterMainteTotalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourTo != null && !"".equals(afterMainteTotalProducingTimeHourTo)) {
                formatAfterMainteTotalProducingTimeHourTo = Integer.parseInt(afterMainteTotalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountFrom != null && !"".equals(afterMainteTotalShotCountFrom)) {
                formatAfterMainteTotalShotCountFrom = Integer.parseInt(afterMainteTotalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountTo != null && !"".equals(afterMainteTotalShotCountTo)) {
                formatAfterMainteTotalShotCountTo = Integer.parseInt(afterMainteTotalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (moldType != null && !"".equals(moldType)) {
                formatMoldType = Integer.parseInt(moldType);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (department != null && !"".equals(department)) {
                formatDepartment = Integer.parseInt(department);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (status != null && !"".equals(status)) {
                formatStatus = Integer.parseInt(status);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        return mstMoldService.getMstMoldPartOutputCsv(
                moldId,
                moldName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                formatMoldType,
                formatDepartment,
                formatLastProductionDateFrom,
                formatLastProductionDateTo,
                formatTotalProducingTimeHourFrom,
                formatTotalProducingTimeHourTo,
                formatTotalShotCountFrom,
                formatTotalShotCountTo,
                formatLastMainteDateFrom,
                formatLastMainteDateTo,
                formatAfterMainteTotalProducingTimeHourFrom,
                formatAfterMainteTotalProducingTimeHourTo,
                formatAfterMainteTotalShotCountFrom,
                formatAfterMainteTotalShotCountTo,
                formatStatus,
                formatMoldCreatedDateFrom,
                formatMoldCreatedDateTo,
                loginUser);

    }
    /**
     * （postMoldPartRelsCsv)
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importmoldpartrelcsv/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMoldPartRelsCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            return mstMoldService.importMoldPartRelCsv(fileUuid, loginUser);
        } catch (Exception ex) {
            ImportResultResponse importResultResponse = new ImportResultResponse();
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            importResultResponse.setErrorMessage(
                    mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid"));
            return importResultResponse;
        }
    }
    /**
     *
     *
     * @param uuid
     * @return
     */
    @GET
    @Path("{uuid}/part")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public MstMoldPartRelList getMoldPartDetail(@PathParam("uuid") String uuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldService.getMstMoldPartDetailsByMoldUuid(uuid, loginUser);
    }
    
    /**
     * 金型マスタ詳細取得
     *
     * @param moldId
     * @return
     */
    @GET
    @Path("detail/{moldId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public MstMoldDetail getMoldByMoldById(@PathParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldService.getMstMoldDetails(moldId, loginUser);
    }

    @GET
    @Path("uuid/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldDetail getMoldByMoldUuid(@PathParam("uuid") String uuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldService.getMstMoldDetails(uuid, null, loginUser);
    }
    
    
    /**
     * 金型マスタ追加
     *
     * @param mstMoldDetail
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMold(MstMoldDetail mstMoldDetail) {
        BasicResponse response = new BasicResponse();
        String moldId = mstMoldDetail.getMstMold().getMoldId();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (mstMoldService.getMstMoldExistCheck(moldId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }
        //ライセンス リミット
        if (mstLicenseLimitService.CheckMoldLimit(loginUser.getLangId(),response)) {
        mstMoldService.createMstMoldByMstMoldDetail(mstMoldDetail, loginUser);
        }
        return response;
    }

    @POST
    @Path("changeLocation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateMoldChangeLocationHistory(MstMold moldData ){
            
        
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            BasicResponse response = new BasicResponse();
            try {
                mstMoldService.updateMoldChangeLocationHistory(moldData, loginUser);
            }
            catch (Exception e) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(e.getMessage());
            }
            return response;      
    }
    
    /**
     * 金型マスタ更新
     *
     * @param mstMoldDetail
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMold(MstMoldDetail mstMoldDetail) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldService.updateMstMoldByMoldDetail(mstMoldDetail, loginUser);
    }

	@PUT
    @Path("updateImage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateMoldImage(MstMold moldData ){
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        try {
            mstMoldService.updateMstMoldImageByMoldId(moldData, loginUser);
        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
	
	
    /**
     * 金型コード・金型名称により金型マスタ取得※部分一致
     *
     * @param moldId
     * @param moldName
     * @return
     */
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldList getMoldLikeCodeOrName(@QueryParam("moldId") String moldId, @QueryParam("moldName") String moldName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldService.getMstMoldAutoComplete(moldId, moldName, "like", loginUser);
    }

        /**
     * 金型コード・金型名称により金型マスタ取得※部分一致
     *
     * @param moldId
     * @param moldName
     * @return
     */
    @GET
    @Path("likeWithoutDispose")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldList getMoldLikeCodeOrNameWithoutDispose(@QueryParam("moldId") String moldId, @QueryParam("moldName") String moldName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldService.getMstMoldAutoCompleteWithoutDispose(moldId, moldName, "like", loginUser);
    }

    /**
     * 金型コード・金型名称により金型マスタ取得※完全一致
     *
     * @param moldId
     * @param moldName
     * @return
     */
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldList getMoldEqualCodeOrName(@QueryParam("moldId") String moldId, @QueryParam("moldName") String moldName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldService.getMstMoldAutoComplete(moldId, moldName, "equal", loginUser);
    }

        /**
     * 金型コード・金型名称により金型マスタ取得※完全一致
     *
     * @param moldId
     * @param moldName
     * @return
     */
    @GET
    @Path("equalWithoutDispose")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldList getMoldEqualCodeOrNameWithoutDispose(@QueryParam("moldId") String moldId, @QueryParam("moldName") String moldName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldService.getMstMoldAutoCompleteWithoutDispose(moldId, moldName, "equal", loginUser);
    }
    /**
     * QRコードを読み取り、その金型情報を表示する。
     *
     * @param moldId
     * @return
     */
    @GET
    @Path("diteal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldDetail getMoldByMoldId(@QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        MstMoldDetail mstMoldDetail = mstMoldService.getMoldByMoldId(moldId, loginUser.getLangId());

        MstMoldDetail response = new MstMoldDetail();
        if (mstMoldDetail != null && mstMoldDetail.getMstMold() != null) {
            List<MstMoldComponentRelationVo> mstMoldComponentRelations = new ArrayList<>();
            if (mstMoldDetail.getMstMold().getMstMoldComponentRelationCollection() != null && mstMoldDetail.getMstMold().getMstMoldComponentRelationCollection().size() > 0) {
                MstMoldComponentRelationVo moldComponentRelationVo;
                Iterator<MstMoldComponentRelation> moldComponentes = mstMoldDetail.getMstMold().getMstMoldComponentRelationCollection().iterator();
                while (moldComponentes.hasNext()) {
                    MstMoldComponentRelation moldComponentRelation = moldComponentes.next();
                    moldComponentRelationVo = new MstMoldComponentRelationVo();
                    moldComponentRelationVo.setMoldUuid(moldComponentRelation.getMstMoldComponentRelationPK().getMoldUuid());
                    moldComponentRelationVo.setComponentId(moldComponentRelation.getMstMoldComponentRelationPK().getComponentId());
                    moldComponentRelationVo.setComponentCode(moldComponentRelation.getMstComponent().getComponentCode());
                    moldComponentRelationVo.setComponentName(moldComponentRelation.getMstComponent().getComponentName());
                    if (moldComponentRelation.getCountPerShot() != null) {
                        moldComponentRelationVo.setCountPerShot(String.valueOf(moldComponentRelation.getCountPerShot()));
                    } else {
                        moldComponentRelationVo.setCountPerShot("0");
                    }

                    mstMoldComponentRelations.add(moldComponentRelationVo);
                }
            }

            if (mstMoldComponentRelations.size() > 1) {
                // 部品コード昇順
                Collections.sort(mstMoldComponentRelations, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        MstMoldComponentRelationVo stu1 = (MstMoldComponentRelationVo) o1;
                        MstMoldComponentRelationVo stu2 = (MstMoldComponentRelationVo) o2;
                        return stu1.getComponentCode().compareTo(stu2.getComponentCode());
                    }
                });
            }

            response.setMstMoldComponentRelationVo(mstMoldComponentRelations);
            MstMold mstMold = mstMoldDetail.getMstMold();
            response.setMainAssetNo(mstMold.getMainAssetNo());
            response.setMoldName(mstMold.getMoldName());
            response.setMoldId(mstMold.getMoldId());
            response.setMoldUuid(mstMold.getUuid());
            if (FileUtil.checkExternal(entityManager, mstDictionaryService, mstMold.getMoldId(), loginUser).isError()) {
                response.setExternalFlg(1);
            } else {
                response.setExternalFlg(0);
            }
            response.setMoldCreatedDate(mstMold.getMoldCreatedDate());
            response.setInspectedDate(mstMold.getInspectedDate());
            response.setInstalledDate(mstMold.getInstalledDate());

            if (null != mstMold.getMstCompanyByCompanyId()) {
                response.setCompanyId(mstMold.getMstCompanyByCompanyId().getId());
                response.setCompanyName(mstMold.getMstCompanyByCompanyId().getCompanyName());
            } else {
                response.setCompanyId("");
                response.setCompanyName("");
            }

            if (null != mstMold.getMstLocation()) {
                response.setLocationId(mstMold.getMstLocation().getId());
                response.setLocationName(mstMold.getMstLocation().getLocationName());
            } else {
                response.setLocationId("");
                response.setLocationName("");
            }

            if (null != mstMold.getMstInstallationSite()) {
                response.setInstllationSiteId(mstMold.getMstInstallationSite().getId());
                response.setInstllationSiteName(mstMold.getMstInstallationSite().getInstallationSiteName());
            } else {
                response.setInstllationSiteId("");
                response.setInstllationSiteName("");
            }

            if (mstMold.getDepartment() != null) {
                boolean found = false;
                MstChoiceList mstDepartmentChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
                response.setDepartment(String.valueOf(mstMold.getDepartment()));
                for (MstChoice mstChoice : mstDepartmentChoiceList.getMstChoice()) {
                    if (mstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mstMold.getDepartment()))) {
                        response.setDepartmentName(mstChoice.getChoice());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    response.setDepartmentName("");
                }
            } else {
                response.setDepartmentName("");
            }

            if (mstMold.getMainteStatus() != null && !"".equals(String.valueOf(mstMold.getMainteStatus()))) {
                int mainteStatus = mstMold.getMainteStatus();
                MstChoiceList mainteStatusChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_mold.mainte_status");
                for (int momi = 0; momi < mainteStatusChoiceList.getMstChoice().size(); momi++) {
                    MstChoice aMstChoice = mainteStatusChoiceList.getMstChoice().get(momi);
                    if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mainteStatus))) {
                        response.setMainteStatusText(aMstChoice.getChoice());
                        break;
                    } else {
                        response.setMainteStatusText("");
                    }
                }
            } else {
                response.setMainteStatusText("");
            }

            response.setStatus(mstMold.getStatus());
            response.setStatusChangedDate(mstMold.getStatusChangedDate());
            response.setImgFilePath01(mstMold.getImgFilePath01() == null ? "" : mstMold.getImgFilePath01());
            response.setImgFilePath02(mstMold.getImgFilePath02() == null ? "" : mstMold.getImgFilePath02());
            response.setImgFilePath03(mstMold.getImgFilePath03() == null ? "" : mstMold.getImgFilePath03());
            response.setImgFilePath04(mstMold.getImgFilePath04() == null ? "" : mstMold.getImgFilePath04());
            response.setImgFilePath05(mstMold.getImgFilePath05() == null ? "" : mstMold.getImgFilePath05());
            response.setImgFilePath06(mstMold.getImgFilePath06() == null ? "" : mstMold.getImgFilePath06());
            response.setImgFilePath07(mstMold.getImgFilePath07() == null ? "" : mstMold.getImgFilePath07());
            response.setImgFilePath08(mstMold.getImgFilePath08() == null ? "" : mstMold.getImgFilePath08());
            response.setImgFilePath09(mstMold.getImgFilePath09() == null ? "" : mstMold.getImgFilePath09());
            response.setImgFilePath10(mstMold.getImgFilePath10() == null ? "" : mstMold.getImgFilePath10());

            // 添付ファイル
            String strReportFilePath01 = mstMold.getReportFilePath01();
            if (null != strReportFilePath01 && !"".equals(strReportFilePath01)) {
                response.setReportFilePath01(strReportFilePath01);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath01);
                if (null != tblUploadFile) {
                    response.setReportFilePathName01(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName01("");
                }
            } else {
                response.setReportFilePath01("");
                response.setReportFilePathName01("");
            }

            String strReportFilePath02 = mstMold.getReportFilePath02();
            if (null != strReportFilePath02 && !"".equals(strReportFilePath02)) {
                response.setReportFilePath02(strReportFilePath02);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath02);
                if (null != tblUploadFile) {
                    response.setReportFilePathName02(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName02("");
                }
            } else {
                response.setReportFilePath02("");
                response.setReportFilePathName02("");
            }

            String strReportFilePath03 = mstMold.getReportFilePath03();
            if (null != strReportFilePath03 && !"".equals(strReportFilePath03)) {
                response.setReportFilePath03(strReportFilePath03);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath03);
                if (null != tblUploadFile) {
                    response.setReportFilePathName03(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName03("");
                }
            } else {
                response.setReportFilePath03("");
                response.setReportFilePathName03("");
            }

            String strReportFilePath04 = mstMold.getReportFilePath04();
            if (null != strReportFilePath04 && !"".equals(strReportFilePath04)) {
                response.setReportFilePath04(strReportFilePath04);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath04);
                if (null != tblUploadFile) {
                    response.setReportFilePathName04(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName04("");
                }
            } else {
                response.setReportFilePath04("");
                response.setReportFilePathName04("");
            }

            String strReportFilePath05 = mstMold.getReportFilePath05();
            if (null != strReportFilePath05 && !"".equals(strReportFilePath05)) {
                response.setReportFilePath05(strReportFilePath05);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath05);
                if (null != tblUploadFile) {
                    response.setReportFilePathName05(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName05("");
                }
            } else {
                response.setReportFilePath05("");
                response.setReportFilePathName05("");
            }

            String strReportFilePath06 = mstMold.getReportFilePath06();
            if (null != strReportFilePath06 && !"".equals(strReportFilePath06)) {
                response.setReportFilePath06(strReportFilePath06);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath06);
                if (null != tblUploadFile) {
                    response.setReportFilePathName06(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName06("");
                }
            } else {
                response.setReportFilePath06("");
                response.setReportFilePathName06("");
            }

            String strReportFilePath07 = mstMold.getReportFilePath07();
            if (null != strReportFilePath07 && !"".equals(strReportFilePath07)) {
                response.setReportFilePath07(strReportFilePath07);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath07);
                if (null != tblUploadFile) {
                    response.setReportFilePathName07(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName07("");
                }
            } else {
                response.setReportFilePath07("");
                response.setReportFilePathName07("");
            }

            String strReportFilePath08 = mstMold.getReportFilePath08();
            if (null != strReportFilePath08 && !"".equals(strReportFilePath08)) {
                response.setReportFilePath08(strReportFilePath08);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath08);
                if (null != tblUploadFile) {
                    response.setReportFilePathName08(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName08("");
                }
            } else {
                response.setReportFilePath08("");
                response.setReportFilePathName08("");
            }

            String strReportFilePath09 = mstMold.getReportFilePath09();
            if (null != strReportFilePath09 && !"".equals(strReportFilePath09)) {
                response.setReportFilePath09(strReportFilePath09);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath09);
                if (null != tblUploadFile) {
                    response.setReportFilePathName09(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName09("");
                }
            } else {
                response.setReportFilePath09("");
                response.setReportFilePathName09("");
            }

            String strReportFilePath10 = mstMold.getReportFilePath10();
            if (null != strReportFilePath10 && !"".equals(strReportFilePath10)) {
                response.setReportFilePath10(strReportFilePath10);
                TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath10);
                if (null != tblUploadFile) {
                    response.setReportFilePathName10(tblUploadFile.getUploadFileName());
                } else {
                    response.setReportFilePathName10("");
                }
            } else {
                response.setReportFilePath10("");
                response.setReportFilePathName10("");
            }

            if (mstMold.getMstCompanyByOwnerCompanyId() != null) {
                response.setOwnerCompanyId(mstMold.getMstCompanyByOwnerCompanyId().getId());
                response.setOwnerCompanyName(mstMold.getMstCompanyByOwnerCompanyId().getCompanyName());
            }

            if (mstMold.getMainteStatus() != null) {
                response.setMainteStatus(mstMold.getMainteStatus());
            } else {
                response.setMainteStatus(null);
            }

            // 4.2 対応追加 LYD S
            FileUtil fu = new FileUtil();
            //最終生産日
            response.setLastProductionDate(mstMold.getLastProductionDate());
            response.setLastProductionDateStr(fu.getDateFormatForStr(mstMold.getLastProductionDate()));
            //累計生産時間
            response.setTotalProducingTimeHour(mstMold.getTotalProducingTimeHour());
            //累計ショット数
            if (mstMold.getTotalShotCount() != null) {
                response.setTotalShotCount(String.valueOf(mstMold.getTotalShotCount()));
            } else {
                response.setTotalShotCount("0");
            }
            //最終メンテナンス日
            response.setLastMainteDate(mstMold.getLastMainteDate());
            response.setLastMainteDateStr(fu.getDateFormatForStr(mstMold.getLastMainteDate()));
            //メンテナンス後生産時間
            response.setAfterMainteTotalProducingTimeHour(mstMold.getAfterMainteTotalProducingTimeHour());
            //メンテナンス後ショット数
            response.setAfterMainteTotalShotCount(mstMold.getAfterMainteTotalShotCount());
            //メンテサイクルコード01
            if (mstMold.getBlMaintenanceCyclePtn01() != null) {
                response.setMainteCycleCode01(mstMold.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                response.setMainteCycleCode01("");
            }
            //メンテサイクルコード02
            if (mstMold.getBlMaintenanceCyclePtn02() != null) {
                response.setMainteCycleCode02(mstMold.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                response.setMainteCycleCode02("");
            }
            //メンテサイクルコード03
            if (mstMold.getBlMaintenanceCyclePtn03() != null) {
                response.setMainteCycleCode03(mstMold.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                response.setMainteCycleCode03("");
            }
            // 4.2 対応追加 LYD E
        }

        return response;
    }
     
    /**
     * スマホ版生産登録、金型レコード確定時に、対応する部品詳細情報を取得
     * @param moldId
     * @return
     */
    @GET
    @Path("diteal/component")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldDetail getMoldComponentProceduresByMoldId(@QueryParam("moldId") String moldId) {
    	
    	MstMoldDetail response = getMoldByMoldId(moldId);
    	if(null != response.getMstMoldComponentRelationVo() && response.getMstMoldComponentRelationVo().size() > 0) {
    		for(MstMoldComponentRelationVo mstMoldComponentRelationVo : response.getMstMoldComponentRelationVo()) {
    	    	MstProcedureList mstProcedureList = mstProcedureService.getComponentProcedures(mstMoldComponentRelationVo.getComponentId());
    	    	mstMoldComponentRelationVo.setProcedureList(mstProcedureList.getMstProcedures());
        	}
    	}
        return response;
    }
    
    
    /**
     * TT0003 通常打上 部品コード変更時 金型部品関係テーブルより部品を取得し、金型IDが一意に定まれば金型ID、金型名称を表示する。
     * （新規登録時はメモリに保持するのみ）
     *
     * @param moldId
     * @return
     */
    @GET
    @Path("autocomplete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldAutoComplete getMoldAutoComplete(@QueryParam("moldId") String moldId) {
        MstMoldList list = mstMoldService.getMstMoldDetailByMoldId(moldId);
        MstMoldAutoComplete response = new MstMoldAutoComplete();
        if (list.getMstMold() != null && list.getMstMold().size() > 0) {
            MstMold mstMold = list.getMstMold().get(0);
            response.setUuid(mstMold.getUuid());
            response.setMoldId(mstMold.getMoldId());
            response.setMoldName(mstMold.getMoldName());
            int size = mstMold.getMstMoldComponentRelationCollection().size();
            if (size == 1) {
                Iterator<MstMoldComponentRelation> mstMoldComponentRelation = mstMold.getMstMoldComponentRelationCollection().iterator();
                MstComponent mstComponent = new MstComponent();
                while (mstMoldComponentRelation.hasNext()) {
                    mstComponent = mstMoldComponentRelation.next().getMstComponent();
                }
                response.setComponentId(mstComponent.getId());
                response.setComponentCode(mstComponent.getComponentCode());
                response.setComponentName(mstComponent.getComponentName());
            }
        } else {
            response.setError(true);
        }
        return response;
    }

    /**
     * バッチで金型マスタデータを取得
     *
     * @param latestExecutedDate
     * @param moldId
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmold")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldDetailList getExtMoldsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldUuid") String moldUuid
    ) {
        //セッションから認証済みされたユーザのＩＤを取得し、キーとしてデータを絞ること
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return mstMoldService.getExtMoldsByBatch(latestExecutedDate, moldId, moldUuid, loginUser.getUserid());
    }

    /**
     * この金型が外部管理しているかを確認、 外部であれば、所有のほう編集・メンテ・改造を行えない
     *
     * @param moldId
     * @return
     */
    @GET
    @Path("extcheck")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse checkExt(@QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser);
    }

    /**
     * M0028_金型送信_送信
     *
     * @param mstMoldSendlist
     * @return
     */
    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse sendMoldToUsedCompany(MstMoldSendList mstMoldSendlist) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldService.sendMoldToUsedCompany(mstMoldSendlist, loginUser);
    }

    /**
     * 金型送信CSV取込ボタン 金型マスタCSV取込
     *
     * @param fileUuid
     * @return
     * @throws java.text.ParseException
     */
    @POST
    @Path("send/importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldSendList sendImportCsv(@PathParam("fileUuid") String fileUuid) throws ParseException {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return mstMoldService.sendImportCsv(fileUuid, loginUser);
    }
    
    /**
     * 金型マスタ複数取得
     *
     * @param moldId
     * @param moldName
     * @param mainAssetNo
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param moldType
     * @param department
     * @param moldCreatedDateFrom//金型作成日From
     * @param moldCreatedDateTo//金型作成日To
     * @param lastProductionDateFrom//最終生産日From
     * @param lastProductionDateTo//最終生産日To
     * @param totalProducingTimeHourFrom//累計生産時間From
     * @param totalProducingTimeHourTo//累計生産時間To
     * @param totalShotCountFrom//累計ショット数From
     * @param totalShotCountTo//累計ショット数To
     * @param lastMainteDateFrom//最終メンテナンス日From
     * @param lastMainteDateTo//最終メンテナンス日To
     * @param afterMainteTotalShotCountFrom//メンテナンス後生産時間From
     * @param afterMainteTotalShotCountTo//メンテナンス後ショット数To
     * @param afterMainteTotalProducingTimeHourFrom//メンテナンス後生産時間From
     * @param afterMainteTotalProducingTimeHourTo//メンテナンス後ショット数To
     * @param status
     * @return an instance of MstMoldList
     */
    @GET
    @Path("getmolds")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldDetailList getMoldsByPage(@QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName, @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName, @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName, @QueryParam("moldType") String moldType,
            @QueryParam("department") String department,
            // 4.2 対応 ZhangYing S
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日
                                                                                 // yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日
                                                                             // yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日
                                                                         // yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日
                                                                     // yyyy/MM/dd
            @QueryParam("moldCreatedDateFrom") String moldCreatedDateFrom, // 金型作成日yyyy/MM/dd
            @QueryParam("moldCreatedDateTo") String moldCreatedDateTo, // 金型作成日yyyy/MM/dd
            @QueryParam("totalProducingTimeHourFrom") String totalProducingTimeHourFrom, // 累計生産時間
                                                                                         // 数字
            @QueryParam("totalProducingTimeHourTo") String totalProducingTimeHourTo, // 累計生産時間
                                                                                     // 数字
            @QueryParam("totalShotCountFrom") String totalShotCountFrom, // 累計ショット数
                                                                         // 数字
            @QueryParam("totalShotCountTo") String totalShotCountTo, // 累計ショット数数字
            @QueryParam("afterMainteTotalProducingTimeHourFrom") String afterMainteTotalProducingTimeHourFrom, // メンテナンス後生産時間
                                                                                                               // 数字
            @QueryParam("afterMainteTotalProducingTimeHourTo") String afterMainteTotalProducingTimeHourTo, // メンテナンス後生産時間
                                                                                                           // 数字
            @QueryParam("afterMainteTotalShotCountFrom") String afterMainteTotalShotCountFrom, // メンテナンス後ショット数
                                                                                               // 数字
            @QueryParam("afterMainteTotalShotCountTo") String afterMainteTotalShotCountTo, // メンテナンス後ショット数
                                                                                           // 数字
            // 4.2 対応 ZhangYing E
            @QueryParam("status") String status, @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        // 最後生産日From - To
        Date formatLastProductionDateFrom = null;
        Date formatLastProductionDateTo = null;
        // 最後メンテ日From - To
        Date formatLastMainteDateFrom = null;
        Date formatLastMainteDateTo = null;
        // 金型作成日From - To
        Date formatMoldCreatedDateFrom = null;
        Date formatMoldCreatedDateTo = null;
        // 累計生産時間 From - To
        Integer formatTotalProducingTimeHourFrom = null;
        Integer formatTotalProducingTimeHourTo = null;

        // 累計ショット数 From - To
        Integer formatTotalShotCountFrom = null;
        Integer formatTotalShotCountTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalProducingTimeHourFrom = null;
        Integer formatAfterMainteTotalProducingTimeHourTo = null;

        // 累計生産時間 From - To
        Integer formatAfterMainteTotalShotCountFrom = null;
        Integer formatAfterMainteTotalShotCountTo = null;

        Integer formatMoldType = null;
        Integer formatDepartment = null;
        Integer formatStatus = null;

        try {
            // 最後生産日From - To
            if (lastProductionDateFrom != null && !"".equals(lastProductionDateFrom)) {
                formatLastProductionDateFrom = sdf.parse(lastProductionDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (lastProductionDateTo != null && !"".equals(lastProductionDateTo)) {
                formatLastProductionDateTo = sdf.parse(lastProductionDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 最後メンテ日From - To
            if (lastMainteDateFrom != null && !"".equals(lastMainteDateFrom)) {
                formatLastMainteDateFrom = sdf.parse(lastMainteDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {

            if (lastMainteDateTo != null && !"".equals(lastMainteDateTo)) {
                formatLastMainteDateTo = sdf.parse(lastMainteDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }

        try {
            // 金型作成日From - To
            if (moldCreatedDateFrom != null && !"".equals(moldCreatedDateFrom)) {
                formatMoldCreatedDateFrom = sdf.parse(moldCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (moldCreatedDateTo != null && !"".equals(moldCreatedDateTo)) {
                formatMoldCreatedDateTo = sdf.parse(moldCreatedDateTo);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 累計生産時間 From - To
            if (totalProducingTimeHourFrom != null && !"".equals(totalProducingTimeHourFrom)) {
                formatTotalProducingTimeHourFrom = Integer.parseInt(totalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalProducingTimeHourTo != null && !"".equals(totalProducingTimeHourTo)) {
                formatTotalProducingTimeHourTo = Integer.parseInt(totalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountFrom != null && !"".equals(totalShotCountFrom)) {
                formatTotalShotCountFrom = Integer.parseInt(totalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (totalShotCountTo != null && !"".equals(totalShotCountTo)) {
                formatTotalShotCountTo = Integer.parseInt(totalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourFrom != null && !"".equals(afterMainteTotalProducingTimeHourFrom)) {
                formatAfterMainteTotalProducingTimeHourFrom = Integer.parseInt(afterMainteTotalProducingTimeHourFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalProducingTimeHourTo != null && !"".equals(afterMainteTotalProducingTimeHourTo)) {
                formatAfterMainteTotalProducingTimeHourTo = Integer.parseInt(afterMainteTotalProducingTimeHourTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountFrom != null && !"".equals(afterMainteTotalShotCountFrom)) {
                formatAfterMainteTotalShotCountFrom = Integer.parseInt(afterMainteTotalShotCountFrom);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (afterMainteTotalShotCountTo != null && !"".equals(afterMainteTotalShotCountTo)) {
                formatAfterMainteTotalShotCountTo = Integer.parseInt(afterMainteTotalShotCountTo);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (moldType != null && !"".equals(moldType)) {
                formatMoldType = Integer.parseInt(moldType);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (department != null && !"".equals(department)) {
                formatDepartment = Integer.parseInt(department);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (status != null && !"".equals(status)) {
                formatStatus = Integer.parseInt(status);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        MstMoldDetailList mstMoldList = mstMoldService.getMstMoldsByPage(moldId, moldName, mainAssetNo,
                ownerCompanyName, companyName, locationName, instllationSiteName, formatMoldType, formatDepartment,
                formatLastProductionDateFrom, formatLastProductionDateTo, formatTotalProducingTimeHourFrom,
                formatTotalProducingTimeHourTo, formatTotalShotCountFrom, formatTotalShotCountTo,
                formatLastMainteDateFrom, formatLastMainteDateTo, formatAfterMainteTotalProducingTimeHourFrom,
                formatAfterMainteTotalProducingTimeHourTo, formatAfterMainteTotalShotCountFrom,
                formatAfterMainteTotalShotCountTo, formatMoldCreatedDateFrom, formatMoldCreatedDateTo, formatStatus,
                loginUser, sidx, sord, pageNumber, pageSize, true);
        
        return mstMoldList;
    }

    /**
     * 金型棚卸結果アップロード
     *
     * @param mstMoldStoctake
     * @return
     */
    @POST
    @Path("stocktake/result")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse moldStocktakeResult(
            MstMoldStoctake mstMoldStoctake//金型棚卸リストJsonフォーマット
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return postMoldStocktakeResult(mstMoldStoctake, loginUser);

    }

    /**
     * 金型棚卸画像アップロード
     *
     * @param uploadFile
     * @param uploadFileDetail
     * @param outputFileUuid
     * @param imageFileKey
     * @return
     */
    @POST
    @Path("stocktake/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse moldStocktakePicture(
            @FormDataParam("upfile") InputStream uploadFile,//アップロード対象ファイル
            @FormDataParam("upfile") FormDataContentDisposition uploadFileDetail,
            @FormDataParam("outputFileUuid") String outputFileUuid,//金型棚卸リストの出力ファイルID
            @FormDataParam("imageFileKey") String imageFileKey//画像ファイルユニークキー
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse basicResponse = mstMoldService.moldStocktakePicture(
                uploadFile,//アップロード対象ファイル
                uploadFileDetail,
                outputFileUuid,//金型棚卸リストの出力ファイルID
                imageFileKey,//画像ファイルユニークキー
                loginUser.getLangId(),
                loginUser.getUserUuid()
        );
        return basicResponse;

    }

    /**
     * 金型棚卸実施 - 検索
     *
     * @param searchConditionList
     * @return
     */
    @POST
    @Path("stocktake/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldStoctake getMoldStocktakeResult(
            SearchConditionList searchConditionList//続けて検索により検索条件が追加されたときは、追加された条件をORでつなげて検索する。
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMoldStoctake mstMoldStocktake = mstMoldService.getMoldStocktakeResult(searchConditionList, loginUser);

        return mstMoldStocktake;
    }
    
    /**
     *
     * @param mstMoldStoctake
     * @param loginUser
     * @return
     */
    private BasicResponse postMoldStocktakeResult(MstMoldStoctake mstMoldStoctake, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        try {
            int locationConfirmCount = 0;//1:所在確認
            int locationUnknownCount = 0;//2:所在不明
            int errorCount = 0;
            int moldIdNotExistCount = 0;//アップロードされた金型IDがマスタにないエラーカウント

            if (mstMoldStoctake != null && mstMoldStoctake.getMoldStocktake() != null) {
                List<Mold> molds = mstMoldStoctake.getMoldStocktake().getMolds();
                if (molds != null && !molds.isEmpty()) {
                    TblMoldInventory tblMoldInventory;
                    OutputErrorInfo outputErrorInfo;
                    FileUtil fu = new FileUtil();
                    String logFileUuid = IDGenerator.generate();//ログファイルUUID用意
                    String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
                    Map<String, String> dictMap = mstMoldService.getDictionaryList(loginUser.getLangId());
                    List<OutputErrorInfo> unknownErrorList = new ArrayList();
                    List<OutputErrorInfo> departmentErrorList = new ArrayList();
                    List<OutputErrorInfo> addtionalList = new ArrayList();

                    SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
                    fu.writeInfoToFile(logFile, fu.outLogValue(dictMap.get("row_number"), dictMap.get("mold_id"), dictMap.get("db_process"), dictMap.get("error_msg")));

                    if (StringUtils.isNotEmpty(mstMoldStoctake.getMoldStocktake().getOutputFileUuid()) && fu.maxLangthCheck(mstMoldStoctake.getMoldStocktake().getOutputFileUuid(), 45)) {
                        basicResponse.setError(true);
                        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                        String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length");
                        basicResponse.setErrorMessage(msg);
                        return basicResponse;
                    }

                    List<TblMoldInventory> insTblMoldInventory = new ArrayList<>();
                    List<TblMoldInventory> updTblMoldInventory = new ArrayList<>();
                    List<MstMold> updMstMold = new ArrayList<>();
                    Set<Integer> department = new HashSet<>();
                    MstUser uploadUser = entityManager.find(MstUser.class, loginUser.getUserid());
                    if (StringUtils.isNotEmpty(uploadUser.getDepartment())) {
                        try {
                            int uploadUserDepartment = Integer.parseInt(uploadUser.getDepartment());
                            if (0 != uploadUserDepartment) {
                                department.add(uploadUserDepartment);
                            }
                        } catch (NumberFormatException e) {
                            Logger.getLogger(MstMoldResource.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }

                    Map<String, MstCompany> mapCompany = new HashMap<>();
                    Map<String, MstLocation> mapLocation = new HashMap<>();
                    Map<String, MstInstallationSite> mapInstallationSite = new HashMap<>();
                    Set<Integer> setChoice = new HashSet<>();
                    for (int i = 0, j = 1; i < molds.size(); i++, j++) {
                        Mold mold = molds.get(i);
                        outputErrorInfo = null;

                        switch (mold.getStocktakeResult()) {
                            case 0:
                                //0:未実施
                                //処理しないのでスキップ
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("inventory_not_done"), ""));
                                continue;
                            case 1:
                                break;
                            case 2:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("mold_inventory_result"), mold.getStocktakeResult())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        if (StringUtils.isEmpty(mold.getMoldId())) {
                            //ログファイルを記入
                            fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_not_null_with_item"), dictMap.get("mold_id"))));
                            errorCount = errorCount + 1;
                            continue;
                        }

                        if (StringUtils.isNotEmpty(mold.getImageFileKey()) && fu.maxLangthCheck(mold.getImageFileKey(), 100)) {
                            //ログファイルを記入
                            fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_over_length_with_item"), dictMap.get("image_file_key"), mold.getImageFileKey().length())));
                            errorCount = errorCount + 1;
                            continue;
                        }

                        if (StringUtils.isNotEmpty(mold.getNotes()) && fu.maxLangthCheck(mold.getNotes(), 200)) {
                            //ログファイルを記入
                            fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_over_length_with_item"), dictMap.get("remarks"), mold.getNotes().length())));
                            errorCount = errorCount + 1;
                            continue;
                        }

                        Date inputInventoryDate;
                        Date inputInventoryDateSzt;
                        if (null != mold.getStocktakeDatetime()) {
                            String stocktakeDatetime = null;
                            try {
                                stocktakeDatetime = DateFormat.dateTimeFormat(mold.getStocktakeDatetime(), loginUser.getJavaZoneId());
                                inputInventoryDate = sdf.parse(stocktakeDatetime);
                                inputInventoryDateSzt = mold.getStocktakeDatetime();
                            } catch (ParseException ex) {
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("mold_inventory_date"), mold.getStocktakeDatetime())));
                                errorCount = errorCount + 1;
                                Logger.getLogger(MstMoldService.class.getName()).log(Level.SEVERE, null, ex);
                                continue;
                            }
                        } else {
                            inputInventoryDate = TimezoneConverter.getLocalTime(loginUser.getJavaZoneId());
                            inputInventoryDateSzt = new Date();
                        }

                        Date inputImageTakenDatetime;
                        Date inputImageTakenDatetimeSzt;
                        if (null != mold.getImageTakenDatetime()) {
                            String imageTakenDatetime = null;
                            try {
                                imageTakenDatetime = DateFormat.dateTimeFormat(mold.getImageTakenDatetime(), loginUser.getJavaZoneId());
                                inputImageTakenDatetime = sdf.parse(imageTakenDatetime);
                                inputImageTakenDatetimeSzt = mold.getImageTakenDatetime();
                            } catch (ParseException ex) {
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("issue_taken_date"), mold.getImageTakenDatetime())));
                                errorCount = errorCount + 1;
                                Logger.getLogger(MstMoldService.class.getName()).log(Level.SEVERE, null, ex);
                                continue;
                            }
                        } else {
                            inputImageTakenDatetime = TimezoneConverter.getLocalTime(loginUser.getJavaZoneId());
                            inputImageTakenDatetimeSzt = new Date();
                        }

                        switch (mold.getStocktakeMethod()) {
                            case 0:
                            case 1:
                            case 2:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("mold_confirm_method"), mold.getStocktakeMethod())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (mold.getChangeDepartment()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("department_change"), mold.getChangeDepartment())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (mold.getBarcodeReprint()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("bar_code_reprint"), mold.getBarcodeReprint())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (mold.getAssetDamaged()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("asset_damaged"), mold.getAssetDamaged())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (mold.getNotInUse()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("not_in_use"), mold.getNotInUse())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (mold.getAdditionalFlag()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("additional_flag"), mold.getAdditionalFlag())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        if (mold.getDepartment() != 0) {
                            if (setChoice.add(mold.getDepartment())) {
                                MstChoice mstChoice = mstChoiceService.findByPk("mst_user.department", loginUser.getLangId(), "" + mold.getDepartment());

                                if (mstChoice == null) {
                                    //ログファイルを記入
                                    fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("user_department"))));
                                    errorCount = errorCount + 1;
                                    setChoice.remove(mold.getDepartment());
                                    continue;
                                }
                            }
                            department.add(mold.getDepartment());
                        }

                        MstMold mstMold = entityManager.find(MstMold.class, mold.getMoldId());
                        if (mstMold == null) {
                            //アップロードされた金型IDがマスタにないエラーカウント
                            moldIdNotExistCount = moldIdNotExistCount + 1;
                            //ログファイルを記入
                            fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("mold_id"))));
                            errorCount = errorCount + 1;
                            continue;
                        }

                        boolean isOld = false;
                        if (StringUtils.isNotEmpty(mstMoldStoctake.getMoldStocktake().getOutputFileUuid())
                                && StringUtils.isNotEmpty(mold.getImageFileKey())) {
                            Query query1 = entityManager.createQuery("SELECT moldInventory FROM TblMoldInventory moldInventory WHERE moldInventory.outputFileUuid = :outputFileUuid AND moldInventory.imageFileKey = :imageFileKey");
                            query1.setParameter("outputFileUuid", mstMoldStoctake.getMoldStocktake().getOutputFileUuid());
                            query1.setParameter("imageFileKey", mold.getImageFileKey());
                            try {
                                tblMoldInventory = (TblMoldInventory) query1.getSingleResult();
                                isOld = true;
                            } catch (NoResultException e) {
                                tblMoldInventory = new TblMoldInventory();
                                tblMoldInventory.setId(IDGenerator.generate());
                            }
                        } else {
                            tblMoldInventory = new TblMoldInventory();
                            tblMoldInventory.setId(IDGenerator.generate());
                        }

                        if (StringUtils.isNotEmpty(mstMoldStoctake.getMoldStocktake().getOutputFileUuid())) {
                            tblMoldInventory.setOutputFileUuid(mstMoldStoctake.getMoldStocktake().getOutputFileUuid());
                        }
                        if (StringUtils.isNotEmpty(mold.getImageFileKey())) {
                            tblMoldInventory.setImageFileKey(mold.getImageFileKey());
                        }

                        tblMoldInventory.setMoldUuid(mstMold.getUuid());

                        tblMoldInventory.setInventoryDate(inputInventoryDate);
                        tblMoldInventory.setInventoryDateSzt(inputInventoryDateSzt);

                        tblMoldInventory.setTakenDate(inputImageTakenDatetime);
                        tblMoldInventory.setTakenDateStz(inputImageTakenDatetimeSzt);

                        tblMoldInventory.setPersonName(uploadUser.getUserName());
                        tblMoldInventory.setPersonUuid(loginUser.getUserUuid());

                        tblMoldInventory.setInventoryResult(mold.getStocktakeResult());

                        tblMoldInventory.setSiteConfirmMethod(mold.getStocktakeMethod());
                        tblMoldInventory.setMoldConfirmMethod(mold.getStocktakeMethod());

                        Query query;
                        if (StringUtils.isNotEmpty(mold.getCompanyCode())) {
                            MstCompany mstCompany = mapCompany.get(mold.getCompanyCode());
                            if (mstCompany != null) {
                                tblMoldInventory.setCompanyId(mstCompany.getId());
                                tblMoldInventory.setCompanyName(mstCompany.getCompanyName());
                            } else {
                                query = entityManager.createNamedQuery("MstCompany.findOnlyByCompanyCode");
                                query.setParameter("companyCode", mold.getCompanyCode());
                                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                                try {
                                    mstCompany = (MstCompany) query.getSingleResult();
                                    tblMoldInventory.setCompanyId(mstCompany.getId());
                                    tblMoldInventory.setCompanyName(mstCompany.getCompanyName());
                                    mapCompany.put(mold.getCompanyCode(), mstCompany);
                                } catch (NoResultException e) {
                                    //ログファイルを記入
                                    fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("company_code"))));
                                    errorCount = errorCount + 1;
                                    continue;
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(mold.getLocationCode())) {
                            MstLocation mstLocation = mapLocation.get(mold.getLocationCode());
                            if (mstLocation != null) {
                                tblMoldInventory.setLocationId(mstLocation.getId());
                                tblMoldInventory.setLocationName(mstLocation.getLocationName());
                            } else {
                                query = entityManager.createNamedQuery("MstLocation.findOnlyByLocationCode");
                                query.setParameter("locationCode", mold.getLocationCode());
                                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                                try {
                                    mstLocation = (MstLocation) query.getSingleResult();
                                    tblMoldInventory.setLocationId(mstLocation.getId());
                                    tblMoldInventory.setLocationName(mstLocation.getLocationName());
                                    mapLocation.put(mold.getLocationCode(), mstLocation);
                                } catch (NoResultException e) {
                                    //ログファイルを記入
                                    fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("location_code"))));
                                    errorCount = errorCount + 1;
                                    continue;
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(mold.getInstallationSiteCode())) {
                            MstInstallationSite mstInstallationSite = mapInstallationSite.get(mold.getInstallationSiteCode());
                            if (mstInstallationSite != null) {
                                tblMoldInventory.setInstllationSiteId(mstInstallationSite.getId());
                                tblMoldInventory.setInstllationSiteName(mstInstallationSite.getInstallationSiteName());
                            } else {
                                query = entityManager.createNamedQuery("MstInstallationSite.findByInstallationSiteCode");
                                query.setParameter("installationSiteCode", mold.getInstallationSiteCode());
                                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                                try {
                                    mstInstallationSite = (MstInstallationSite) query.getSingleResult();
                                    tblMoldInventory.setInstllationSiteId(mstInstallationSite.getId());
                                    tblMoldInventory.setInstllationSiteName(mstInstallationSite.getInstallationSiteName());
                                    mapInstallationSite.put(mold.getInstallationSiteCode(), mstInstallationSite);
                                } catch (NoResultException e) {
                                    //ログファイルを記入
                                    fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("installation_site_code"))));
                                    errorCount = errorCount + 1;
                                    continue;
                                }
                            }
                        }
                        tblMoldInventory.setInputType(1);
                        tblMoldInventory.setDepartment(mold.getDepartment());
                        tblMoldInventory.setRemarks(mold.getNotes());
                        tblMoldInventory.setDepartmentChange(mold.getChangeDepartment());
                        tblMoldInventory.setBarcodeReprint(mold.getBarcodeReprint());
                        tblMoldInventory.setAssetDamaged(mold.getAssetDamaged());
                        tblMoldInventory.setNotInUse(mold.getNotInUse());

                        // 棚卸結果が１、２になったものを棚卸実施
                        switch (mold.getStocktakeResult()) {
                            case 0:
                                //0:未実施
                                //処理しないのでスキップ
                                continue;
                            case 1:
                                //1:所在確認
                                locationConfirmCount = locationConfirmCount + 1;
                                break;
                            case 2:
                                //2:所在不明
                                outputErrorInfo = new OutputErrorInfo();
                                locationUnknownCount = locationUnknownCount + 1;
                                outputErrorInfo.setLocationConfirm(2);
                                mstMold.setStatus(8);
                                break;
                            default:
                                continue;
                        }
                        if (mold.getChangeDepartment() == 1) {
                            if (outputErrorInfo == null) { // 以下の金型は部署変更と回答されましたが、変更後の所属が不明です。
                                outputErrorInfo = new OutputErrorInfo();
                                outputErrorInfo.setLocationConfirm(1);
                            } else { // 下の金型は所在不明で部署変更と回答されましたが、変更後の所属が不明です。
                                outputErrorInfo.setLocationConfirm(3);
                            }
                        }

                        if (outputErrorInfo != null) {
                            outputErrorInfo.setCompanyName(tblMoldInventory.getCompanyName());
                            outputErrorInfo.setLocationName(tblMoldInventory.getLocationName());
                            outputErrorInfo.setInstallationSiteName(tblMoldInventory.getInstllationSiteName());
                            outputErrorInfo.setDepartmentName(mold.getDepartmentName());
                            outputErrorInfo.setMoldId(mstMold.getMoldId());
                            outputErrorInfo.setMoldName(mstMold.getMoldName());

                            switch (outputErrorInfo.getLocationConfirm()) {
                                case 1:
                                    departmentErrorList.add(outputErrorInfo);
                                    break;
                                case 2:
                                    unknownErrorList.add(outputErrorInfo);
                                    break;
                                case 3:
                                    departmentErrorList.add(outputErrorInfo);
                                    unknownErrorList.add(outputErrorInfo);
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (mold.getAdditionalFlag() == 1) {
                            if (outputErrorInfo == null) {
                                outputErrorInfo = new OutputErrorInfo();
                                outputErrorInfo.setMoldId(mstMold.getMoldId());
                            }
                            outputErrorInfo.setQrPlateInfo(mold.getQrPlateInfo());
                            addtionalList.add(outputErrorInfo);
                        }

                        if (isOld) {
                            tblMoldInventory.setUpdateDate(new Date());
                            tblMoldInventory.setUpdateUserUuid(loginUser.getUserUuid());
                            //entityManager.merge(tblMoldInventory);
                            updTblMoldInventory.add(tblMoldInventory);
                        } else {
                            tblMoldInventory.setCreateDate(new Date());
                            tblMoldInventory.setCreateUserUuid(loginUser.getUserUuid());
                            tblMoldInventory.setUpdateDate(new Date());
                            tblMoldInventory.setUpdateUserUuid(loginUser.getUserUuid());
                            //entityManager.persist(tblMoldInventory);
                            insTblMoldInventory.add(tblMoldInventory);
                        }

                        mstMold.setLatestInventoryId(tblMoldInventory.getId());
                        mstMold.setInventoryStatus(1);//処理済
                        mstMold.setUpdateDate(new Date());
                        mstMold.setUpdateUserUuid(loginUser.getUserUuid());
                        // entityManager.merge(mstMold);
                        updMstMold.add(mstMold);
                        //ログファイルを記入
                        fu.writeInfoToFile(logFile, fu.outLogValue("" + j, mold.getMoldId(), dictMap.get("msg_stocktake_result_imported"), ""));
                    }

                    mstMoldService.batchInsert(insTblMoldInventory, 0);
                    mstMoldService.batchInsert(updTblMoldInventory, 1);
                    mstMoldService.batchInsertMold(updMstMold);

                    //sendMail
                    // ファイルをReNameする  
                    String fileName = dictMap.get("stocktake_upload_result");
                    sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    fileName = fileName + "_" + sdf.format(new Date());
                    StringBuffer logPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
                    logPath = logPath.append(SEPARATOR).append(CommonConstants.LOG).append(SEPARATOR);
                    FileUtil.renameFile(logPath.toString(), logFileUuid + CommonConstants.EXT_LOG, fileName + CommonConstants.EXT_LOG);
                    logFile = logPath.toString() + fileName + CommonConstants.EXT_LOG;

                    mstMoldService.moldStocktakeResultPrepareMail(mstMoldStoctake,
                            unknownErrorList,
                            departmentErrorList,
                            addtionalList,
                            locationConfirmCount,
                            locationUnknownCount,
                            errorCount,
                            logFile,
                            department,
                            uploadUser);
                    
                    //発生したエラーが金型IDがマスタにないエラーのみの場合は、Responseのerrorをtrueにしない。errorCode, errorMessageもセットしない。
                    //if (errorCount > 0) {
                    if (errorCount > moldIdNotExistCount) {
                        basicResponse.setError(true);
                        basicResponse.setErrorMessage(dictMap.get("msg_error_importes_result"));
                        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                        return basicResponse;
                    }
                }
            }
        } catch (Exception e) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(e.getMessage());
            e.printStackTrace();
        }

        return basicResponse;
    }

    /**
     * 生産実績追加
     *
     * @param uploadFile
     * @param uploadFileDetail
     * @param fileType
     * @param functionId
     * @return
     */
    @POST
    @Path("production/result/add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse productionResultAdd(
            @FormDataParam("upfile") InputStream uploadFile,
            @FormDataParam("upfile") FormDataContentDisposition uploadFileDetail,
            @FormDataParam("fileType") String fileType,
            @FormDataParam("functionId") String functionId
    ) {
        StringBuffer path = new StringBuffer(kartePropertyService.getDocumentDirectory());
        ImportResultResponse response = new ImportResultResponse();
        path = path.append(FileUtil.SEPARATOR).append(CommonConstants.CSV);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String uuid = IDGenerator.generate();
        String getFileType = uploadFileDetail.getFileName();
        getFileType = getFileType.substring(getFileType.lastIndexOf(".") + 1);

        if (!getFileType.endsWith(CommonConstants.CSV)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            response.setErrorMessage(msg);
            return response;
        }

        FileUtil fu = new FileUtil();

        fu.createPath(path.toString());
        path = path.append(FileUtil.SEPARATOR).append(uuid).append(".").append(getFileType);

        fu.createFile(path.toString());
        fu.writeToFile(uploadFile, path.toString());

        Date uploadDate = new Date();
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileType(fileType);
        tblUploadFile.setFileUuid(uuid);
        tblUploadFile.setUploadFileName(FileUtil.getValueByUTF8(uploadFileDetail.getFileName()));
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(functionId);
        tblUploadFile.setFunctionId(mstFunction);
        tblUploadFile.setUploadDate(uploadDate);
        tblUploadFile.setUploadUserUuid(loginUser.getUserUuid());
        tblUploadFileService.createTblUploadFile(tblUploadFile);

        return mstMoldService.productionResultAdd(path.toString(), uuid, loginUser.getUserUuid(), loginUser.getLangId());

    }

    /**
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("production/importcsv/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse productionResultImport(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }
        return mstMoldService.productionResultAdd(csvFile, fileUuid, loginUser.getUserUuid(), loginUser.getLangId());
    }

    /**
     * ファイルテンプレート
     *
     * @return
     */
    @GET
    @Path("production/result/export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse productionResultExport() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return mstMoldService.productionResultExport(loginUser);
    }

}
