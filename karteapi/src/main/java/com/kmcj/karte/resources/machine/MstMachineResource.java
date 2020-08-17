package com.kmcj.karte.resources.machine;

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
import com.kmcj.karte.resources.machine.assetno.MstMachineAssetNo;
import com.kmcj.karte.resources.machine.assetno.MstMachineAssetNoPK;
import com.kmcj.karte.resources.machine.assetno.MstMachineAssetNoService;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeList;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeService;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistory;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistoryService;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistoryVo;
import com.kmcj.karte.resources.machine.spec.MstMachineSpec;
import com.kmcj.karte.resources.machine.spec.MstMachineSpecPK;
import com.kmcj.karte.resources.machine.spec.MstMachineSpecService;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistory;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryService;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtnService;
import com.kmcj.karte.resources.mold.MstMoldAutoComplete;
import com.kmcj.karte.resources.sigma.MstSigma;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import com.kmcj.karte.resources.machine.inventory.Machine;
import com.kmcj.karte.resources.machine.inventory.MstMachineStoctake;
import com.kmcj.karte.resources.machine.inventory.SearchConditionList;
import com.kmcj.karte.resources.machine.inventory.TblMachineInventory;
import com.kmcj.karte.resources.machine.inventory.OutputErrorInfo;
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
 * 設備
 *
 * @author admin
 */
@RequestScoped
@Path("machine")
public class MstMachineResource {

    public MstMachineResource() {
    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstMachineService mstMachineService;
    
    @Inject
    private MstLicenseLimitService mstLicenseLimitService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MstMachineSpecService mstMachineSpecService;

    @Inject
    private MstMachineSpecHistoryService mstMachineSpecHistoryService;

    @Inject
    private MstMachineAttributeService mstMachineAttributeService;

    @Inject
    private MstCompanyService mstCompanyService;

    @Inject
    private MstLocationService mstLocationService;

    @Inject
    private MstInstallationSiteService mstInstallationSiteService;

    @Inject
    private TblMachineLocationHistoryService tblMachineLocationHistoryService;
    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstMachineAssetNoService mstMachineAssetNoService;

    @Inject
    private TblMaintenanceCyclePtnService tblMaintenanceCyclePtnService;

    @Inject
    private TblUploadFileService tblUploadFileService;

    /**
     * 設備マスタ件数取得
     *
     * @param machineId //設備ID
     * @param machineName //設備名称
     * @param mainAssetNo //資産番号
     * @param ownerCompanyName //所有会社名称
     * @param companyName //会社名称
     * @param locationName //所在地名称
     * @param instllationSiteName //設置場所名称
     * @param machineType //設備種類
     * @param department //所属
     * @param lastProductionDateFrom//最終生産日From
     * @param lastProductionDateTo//最終生産日To
     * @param totalProducingTimeHourFrom//累計生産時間From
     * @param totalProducingTimeHourTo//累計生産時間To
     * @param totalShotCountFrom//累計ショット数From
     * @param machineCreatedDateFrom
     * @param machineCreatedDateTo
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
    public CountResponse getRecordCount(@QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("machineType") String machineType,
            @QueryParam("department") String department,
            @QueryParam("status") String status,
            // 4.2 対応　ZhangYing S
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("machineCreatedDateFrom") String machineCreatedDateFrom, // 設備作成日yyyy/MM/dd
            @QueryParam("machineCreatedDateTo") String machineCreatedDateTo, // 設備作成日yyyy/MM/dd

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
        // 設備作成日From - To
        Date formatMachineCreatedDateFrom = null;
        Date formatMachineCreatedDateTo = null;
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

        Integer formatMachineType = null;
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
            // 設備作成日From - To
            if (machineCreatedDateFrom != null && !"".equals(machineCreatedDateFrom)) {
                formatMachineCreatedDateFrom = sdf.parse(machineCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (machineCreatedDateTo != null && !"".equals(machineCreatedDateTo)) {
                formatMachineCreatedDateTo = sdf.parse(machineCreatedDateTo);
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
            if (machineType != null && !"".equals(machineType)) {
                formatMachineType = Integer.parseInt(machineType);
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

        CountResponse count = mstMachineService.getMstMachineCount(machineId,
                machineName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                formatMachineType,
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
                formatMachineCreatedDateFrom,
                formatMachineCreatedDateTo,
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
     * Get Machine by Machine Uuid
     * @param machineUuid
     * @return MstMachine
     */
    @GET
    @Path("getMachine")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachine getMachine(@QueryParam("machineUuid") String machineUuid){
        return mstMachineService.getMstMachineByUuid(machineUuid);
    }

    /**
     * 設備マスタ複数取得
     *
     * @param machineId
     * @param machineName
     * @param mainAssetNo
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param machineType
     * @param department
     * @param lastProductionDateFrom//最終生産日From
     * @param lastProductionDateTo//最終生産日To
     * @param totalProducingTimeHourFrom//累計生産時間From
     * @param totalProducingTimeHourTo//累計生産時間To
     * @param totalShotCountFrom//累計ショット数From
     * @param totalShotCountTo//累計ショット数To
     * @param machineCreatedDateFrom
     * @param machineCreatedDateTo
     * @param lastMainteDateFrom//最終メンテナンス日From
     * @param lastMainteDateTo//最終メンテナンス日To
     * @param afterMainteTotalShotCountFrom//メンテナンス後生産時間From
     * @param afterMainteTotalShotCountTo//メンテナンス後ショット数To
     * @param afterMainteTotalProducingTimeHourFrom//メンテナンス後生産時間From
     * @param afterMainteTotalProducingTimeHourTo//メンテナンス後ショット数To
     * @param status
     * @param orderByMachineName
     * @return an instance of MstMachineList
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getMachines(@QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("machineType") String machineType,
            @QueryParam("department") String department,
            @QueryParam("status") String status,
            @QueryParam("orderByMachineName") String orderByMachineName,
            // 4.2 対応　ZhangYing S
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("machineCreatedDateFrom") String machineCreatedDateFrom, // 設備作成日yyyy/MM/dd
            @QueryParam("machineCreatedDateTo") String machineCreatedDateTo, // 設備作成日yyyy/MM/dd
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

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        // 最後生産日From - To
        Date formatLastProductionDateFrom = null;
        Date formatLastProductionDateTo = null;
        // 最後メンテ日From - To
        Date formatLastMainteDateFrom = null;
        Date formatLastMainteDateTo = null;
        // 設備作成日From - To
        Date formatMachineCreatedDateFrom = null;
        Date formatMachineCreatedDateTo = null;
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

        Integer formatMachineType = null;
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
            // 設備作成日From - To
            if (machineCreatedDateFrom != null && !"".equals(machineCreatedDateFrom)) {
                formatMachineCreatedDateFrom = sdf.parse(machineCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (machineCreatedDateTo != null && !"".equals(machineCreatedDateTo)) {
                formatMachineCreatedDateTo = sdf.parse(machineCreatedDateTo);
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
            if (machineType != null && !"".equals(machineType)) {
                formatMachineType = Integer.parseInt(machineType);
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
        MstMachineList mstMachineList = mstMachineService.getMstMachines(
                machineId,
                machineName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                formatMachineType,
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
                formatMachineCreatedDateFrom,
                formatMachineCreatedDateTo,
                formatStatus,
                orderByMachineName,
                loginUser);
        return mstMachineList;
    }

        /**
     * 設備マスタ複数取得
     *
     * @param machineId
     * @param machineName
     * @param mainAssetNo
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param machineType
     * @param department
     * @param lastProductionDateFrom//最終生産日From
     * @param lastProductionDateTo//最終生産日To
     * @param totalProducingTimeHourFrom//累計生産時間From
     * @param totalProducingTimeHourTo//累計生産時間To
     * @param totalShotCountFrom//累計ショット数From
     * @param totalShotCountTo//累計ショット数To
     * @param machineCreatedDateFrom
     * @param machineCreatedDateTo
     * @param lastMainteDateFrom//最終メンテナンス日From
     * @param lastMainteDateTo//最終メンテナンス日To
     * @param afterMainteTotalShotCountFrom//メンテナンス後生産時間From
     * @param afterMainteTotalShotCountTo//メンテナンス後ショット数To
     * @param afterMainteTotalProducingTimeHourFrom//メンテナンス後生産時間From
     * @param afterMainteTotalProducingTimeHourTo//メンテナンス後ショット数To
     * @param status
     * @param orderByMachineName
     * @return an instance of MstMachineList
     */
    @GET
    @Path("machineListWithoutDispose")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getMachinesWithoutDispose(@QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("machineType") String machineType,
            @QueryParam("department") String department,
            @QueryParam("status") String status,
            @QueryParam("orderByMachineName") String orderByMachineName,
            // 4.2 対応　ZhangYing S
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("machineCreatedDateFrom") String machineCreatedDateFrom, // 設備作成日yyyy/MM/dd
            @QueryParam("machineCreatedDateTo") String machineCreatedDateTo, // 設備作成日yyyy/MM/dd
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

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        // 最後生産日From - To
        Date formatLastProductionDateFrom = null;
        Date formatLastProductionDateTo = null;
        // 最後メンテ日From - To
        Date formatLastMainteDateFrom = null;
        Date formatLastMainteDateTo = null;
        // 設備作成日From - To
        Date formatMachineCreatedDateFrom = null;
        Date formatMachineCreatedDateTo = null;
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

        Integer formatMachineType = null;
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
            // 設備作成日From - To
            if (machineCreatedDateFrom != null && !"".equals(machineCreatedDateFrom)) {
                formatMachineCreatedDateFrom = sdf.parse(machineCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (machineCreatedDateTo != null && !"".equals(machineCreatedDateTo)) {
                formatMachineCreatedDateTo = sdf.parse(machineCreatedDateTo);
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
            if (machineType != null && !"".equals(machineType)) {
                formatMachineType = Integer.parseInt(machineType);
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
        MstMachineList mstMachineList = mstMachineService.getMstMachinesWithoutDispose(
                machineId,
                machineName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                formatMachineType,
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
                formatMachineCreatedDateFrom,
                formatMachineCreatedDateTo,
                formatStatus,
                orderByMachineName,
                loginUser);
        return mstMachineList;
    }

    /**
     * 設備マスタ削除
     *
     * @param machineId
     * @return
     */
    @DELETE
    @Path("{machineId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public BasicResponse deleteMachine(@PathParam("machineId") String machineId) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (!mstMachineService.getMstMachineExistCheck(machineId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        } else if (mstMachineService.getMstMachineFKCheck(machineId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
        } else {
            mstMachineService.deleteMstMachine(machineId);
        }
        return response;
    }

    /**
     * 設備マスタCSV出力
     *
     * @param machineId
     * @param machineName
     * @param mainAssetNo
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param machineType
     * @param department
     * @param lastProductionDateFrom
     * @param lastProductionDateTo
     * @param totalProducingTimeHourFrom
     * @param totalProducingTimeHourTo
     * @param machineCreatedDateFrom
     * @param machineCreatedDateTo
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
    public FileReponse getMachinesCSV(@QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName,
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("machineType") String machineType,
            @QueryParam("department") String department,
            // 4.2 対応　ZhangYing S
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日 yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日 yyyy/MM/dd
            @QueryParam("machineCreatedDateFrom") String machineCreatedDateFrom, // 設備作成日yyyy/MM/dd
            @QueryParam("machineCreatedDateTo") String machineCreatedDateTo, // 設備作成日yyyy/MM/dd
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
        // 設備作成日From - To
        Date formatMachineCreatedDateFrom = null;
        Date formatMachineCreatedDateTo = null;
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

        Integer formatMachineType = null;
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
            // 設備作成日From - To
            if (machineCreatedDateFrom != null && !"".equals(machineCreatedDateFrom)) {
                formatMachineCreatedDateFrom = sdf.parse(machineCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (machineCreatedDateTo != null && !"".equals(machineCreatedDateTo)) {
                formatMachineCreatedDateTo = sdf.parse(machineCreatedDateTo);
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
            if (machineType != null && !"".equals(machineType)) {
                formatMachineType = Integer.parseInt(machineType);
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

        mstMachineService.outMachineTypeOfChoice(loginUser.getLangId());
        mstMachineService.outStatusOfChoice(loginUser.getLangId());
        FileReponse response = mstMachineService.getMstMachineOutputCsv(
                machineId,
                machineName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                formatMachineType,
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
                formatMachineCreatedDateFrom,
                formatMachineCreatedDateTo,
                formatStatus,
                loginUser);
        return response;
    }

    /**
     * 設備一覧CSV取込ボタン 設備マスタCSV取込
     *
     * @param fileUuid
     * @return
     * @throws java.text.ParseException
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMachinesCSV(@PathParam("fileUuid") String fileUuid) throws ParseException {
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
                        "machine_id",//設備ID
                        "machine_spec",//設備仕様
                        "error",
                        "error_detail",
                        "db_process",
                        "msg_record_added",
                        "msg_error_record_exists",
                        "msg_data_modified",
                        "msg_record_deleted",
                        "msg_cannot_delete_used_record",
                        "mst_error_record_not_found",
                        "msg_error_over_length",
                        "msg_error_wrong_csv_layout",
                        "msg_error_ext_edit",
                        "machine_name",//設備名称
                        "machine_type",//設備種類
                        "main_asset_no",//代表資産番号
                        "machine_created_date",//設備作成日
                        "inspected_date",//検収日
                        "owner_company_code",//所有会社名称
                        "installed_date",//設置日
                        "company_code",//会社名称
                        "location_code",//所在地名称
                        "installation_site_code",//設置場所名称
                        "user_department",
                        "sigma_code",
                        "mac_key",
                        "base_cycle_time",
                        "machine_code",
                        "strage_location_cd",
                        "charge_cd",
                        "operator_cd",
                        "machine_last_production_date",
                        "machine_total_production_time_hour",
                        "total_shot_count",
                        "machine_last_mainte_date",
                        "machine_after_mainte_total_production_time_hour",
                        "machine_after_mainte_total_shot_count",
                        "machine_mainte_cycle_code_01",
                        "machine_mainte_cycle_code_02",
                        "machine_mainte_cycle_code_03",
                        "mold_status",//状態
                        "status_changed_date",//状態変更日 
                        "msg_error_not_null",
                        "msg_error_date_format_invalid",
                        "msg_error_value_invalid",
                        "msg_error_not_isnumber",
                        "delete_record"
                );
                Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, userLangId, dictKeyList);

                String lineNo = headerMap.get("row_number");
                String head_machineId = headerMap.get("machine_id");//設備ID
                String head_machineSpec = headerMap.get("machine_spec");//設備仕様
                String error = headerMap.get("error");
                String errorContents = headerMap.get("error_detail");
                String result = headerMap.get("db_process");
                String addedMsg = headerMap.get("msg_record_added");
                String existsMsg = headerMap.get("msg_error_record_exists");
                String updatedMsg = headerMap.get("msg_data_modified");
                String deletedMsg = headerMap.get("msg_record_deleted");
                String canNotdeleteMsg = headerMap.get("msg_cannot_delete_used_record");
                String notFound = headerMap.get("mst_error_record_not_found");
                String maxLangth = headerMap.get("msg_error_over_length");
                String layout = headerMap.get("msg_error_wrong_csv_layout");
                String extEdit = headerMap.get("msg_error_ext_edit");
                String head_machineName = headerMap.get("machine_name");//設備名称
                String head_machineType = headerMap.get("machine_type");//設備種類
                String head_machineAssetNo = headerMap.get("main_asset_no");//代表資産番号
                String head_machineCreatedDate = headerMap.get("machine_created_date");//設備作成日
                String head_inspectedDdate = headerMap.get("inspected_date");//検収日
                String head_machineOwnerCompanyCode = headerMap.get("owner_company_code");//所有会社名称
                String head_installedDate = headerMap.get("installed_date");//設置日
                String head_machineCompanyCode = headerMap.get("company_code");//会社名称
                String head_machineLocationCode = headerMap.get("location_code");//所在地名称
                String head_machineInstallationSiteCode = headerMap.get("installation_site_code");//設置場所名称
                String head_DepartmentName = headerMap.get("user_department");
                String head_SigmaCode = headerMap.get("sigma_code");
                String head_MacKey = headerMap.get("mac_key");
                String head_BaseCycletime = headerMap.get("base_cycle_time");
                String head_MachineCd = headerMap.get("machine_code");
                String head_StrageLocationCd = headerMap.get("strage_location_cd");
                String head_ChargeCd = headerMap.get("charge_cd");
                String head_OperatorCd = headerMap.get("operator_cd");
                String head_lastProductionDate = headerMap.get("machine_last_production_date");
                String head_totalProductionTimeHour = headerMap.get("machine_total_production_time_hour");
                String head_totalShotCount = headerMap.get("total_shot_count");
                String head_lastMainteDate = headerMap.get("machine_last_mainte_date");
                String head_afterMainteTotalProductionTimeHour = headerMap.get("machine_after_mainte_total_production_time_hour");
                String head_afterMainteTotalShotCount = headerMap.get("machine_after_mainte_total_shot_count");
                String head_mainteCycleCode01 = headerMap.get("machine_mainte_cycle_code_01");
                String head_mainteCycleCode02 = headerMap.get("machine_mainte_cycle_code_02");
                String head_mainteCycleCode03 = headerMap.get("machine_mainte_cycle_code_03");
                String head_status = headerMap.get("mold_status");//状態
                String head_statusChangedDate = headerMap.get("status_changed_date");//状態変更日 
                String nullMsg = headerMap.get("msg_error_not_null");
                String dateCheck = headerMap.get("msg_error_date_format_invalid");
                String errorValue = headerMap.get("msg_error_value_invalid");
                String numberCheck = headerMap.get("msg_error_not_isnumber");

                Map<String, String> logParm = new HashMap<>();
                logParm.put("lineNo", lineNo);
                logParm.put("head_machineId", head_machineId);
                logParm.put("head_machineName", head_machineName);
                logParm.put("head_machineType", head_machineType);
                logParm.put("head_machineAssetNo", head_machineAssetNo);
                logParm.put("head_machineCreatedDate", head_machineCreatedDate);
                logParm.put("head_inspectedDdate", head_inspectedDdate);
                logParm.put("head_machineOwnerCompanyCode", head_machineOwnerCompanyCode);
                logParm.put("head_installedDate", head_installedDate);
                logParm.put("head_machineCompanyCode", head_machineCompanyCode);
                logParm.put("head_machineLocationCode", head_machineLocationCode);
                logParm.put("head_machineInstallationSiteCode", head_machineInstallationSiteCode);

                logParm.put("head_DepartmentName", head_DepartmentName);
                logParm.put("head_SigmaCode", head_SigmaCode);
                logParm.put("head_MacKey", head_MacKey);
                logParm.put("head_BaseCycletime", head_BaseCycletime);
                logParm.put("head_MachineCd", head_MachineCd);
                logParm.put("head_StrageLocationCd", head_StrageLocationCd);
                logParm.put("head_ChargeCd", head_ChargeCd);
                logParm.put("head_OperatorCd", head_OperatorCd);

                logParm.put("head_status", head_status);
                logParm.put("head_statusChangedDate", head_statusChangedDate);
                logParm.put("error", error);
                logParm.put("errorContents", errorContents);
                logParm.put("nullMsg", nullMsg);
                logParm.put("maxLangth", maxLangth);
                logParm.put("dateCheck", dateCheck);
                logParm.put("errorValue", errorValue);
                logParm.put("numberCheck", numberCheck);
                logParm.put("layout", layout);
                logParm.put("notFound", notFound);
                logParm.put("existsMsg", existsMsg);
                logParm.put("head_machinespec", head_machineSpec);

                logParm.put("head_lastProductionDate", head_lastProductionDate);
                logParm.put("head_totalProductionTimeHour", head_totalProductionTimeHour);
                logParm.put("head_totalShotCount", head_totalShotCount);
                logParm.put("head_lastMainteDate", head_lastMainteDate);
                logParm.put("head_afterMainteTotalProductionTimeHour", head_afterMainteTotalProductionTimeHour);
                logParm.put("head_afterMainteTotalShotCount", head_afterMainteTotalShotCount);
                logParm.put("head_mainteCycleCode01", head_mainteCycleCode01);
                logParm.put("head_mainteCycleCode02", head_mainteCycleCode02);
                logParm.put("head_mainteCycleCode03", head_mainteCycleCode03);

                FileUtil fu = new FileUtil();

                /* 設備仕様と部品コードのcount Start*/
                int imachineSpec = 0;
                String csvArrayLine0[] = FileUtil.splitCsvLine(strHead);
                int delFlagPostion = csvArrayLine0.length - 1;
                for (int j = 0; j < delFlagPostion; j++) {
                    if (csvArrayLine0[j].trim().contains(head_machineSpec)) {
                        imachineSpec = imachineSpec + 1;
                    }
                }
                /* 設備仕様と部品コードのcount End*/

                Map inMachineTypeMapTemp = null;
                Map inStatusMapTemp = null;

                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);
                    //ヘーダー列数と値の列数異なる場合、エラーとなる
                    if (((ArrayList) readList.get(0)).size() != comList.size()) {
                        //エラー情報をログファイルに記入
                        fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineId, "", error, 1, errorContents, layout));
                        failedCount += 1;
                        continue;
                    }

                    String[] csvArray = new String[comList.size()];

                    String strMachineId = String.valueOf(comList.get(0));//設備ID

                    csvArray[0] = strMachineId;
                    String strMachineName = String.valueOf(comList.get(1));//設備名称
                    csvArray[1] = strMachineName;
                    String strMachineTypeValue = String.valueOf(comList.get(2));//設備種類
                    csvArray[2] = strMachineTypeValue;
                    String strMainAssetNo = String.valueOf(comList.get(3));//代表資産番号
                    csvArray[3] = strMainAssetNo;
                    String strMachineCreatedDate = String.valueOf(comList.get(4));//設備作成日
                    if (!StringUtils.isEmpty(strMachineCreatedDate)) {
                        strMachineCreatedDate = DateFormat.formatDateYear(strMachineCreatedDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[4] = strMachineCreatedDate.equals("-1") ? String.valueOf(comList.get(4)) : strMachineCreatedDate;
                    String strInspectedDate = String.valueOf(comList.get(5));//検収日
                    if (!StringUtils.isEmpty(strInspectedDate)) {
                        strInspectedDate = DateFormat.formatDateYear(strInspectedDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[5] = strInspectedDate.equals("-1") ? String.valueOf(comList.get(5)) : strInspectedDate;
                    String strOwnerCompanyCode = String.valueOf(comList.get(6));//所有会社コード
                    csvArray[6] = strOwnerCompanyCode;
                    String strInstalledDate = fu.getDateFormatForStr(new Date());
                    csvArray[8] = strInstalledDate;
                    String strCompanyCode = String.valueOf(comList.get(9));//会社コード
                    csvArray[9] = strCompanyCode;
                    String strLocationCode = String.valueOf(comList.get(11));//所在地コード
                    csvArray[11] = strLocationCode;
                    String strInstllationSiteCode = String.valueOf(comList.get(13));//設置場所コード
                    csvArray[13] = strInstllationSiteCode;

                    String strStatus = String.valueOf(comList.get(15));
                    csvArray[15] = strStatus;
                    String strStatusChangedDate = String.valueOf(comList.get(16));
                    if (!StringUtils.isEmpty(strStatusChangedDate)) {
                        strStatusChangedDate = DateFormat.formatDateYear(strStatusChangedDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[16] = strStatusChangedDate.equals("-1") ? String.valueOf(comList.get(16)) : strStatusChangedDate;

                    String strDepartment = String.valueOf(comList.get(17));
                    csvArray[17] = strDepartment;
                    String strSigmaCode = String.valueOf(comList.get(18));
                    csvArray[18] = strSigmaCode;
                    String strMacKey = String.valueOf(comList.get(19));
                    csvArray[19] = strMacKey;
                    String strBaseCycleTime = String.valueOf(comList.get(20));
                    csvArray[20] = strBaseCycleTime;
                    String strMachineCd = String.valueOf(comList.get(21));
                    csvArray[21] = strMachineCd;
                    String strStrageLocationCd = String.valueOf(comList.get(22));
                    csvArray[22] = strStrageLocationCd;
                    String strChargeCd = String.valueOf(comList.get(23));
                    csvArray[23] = strChargeCd;
                    String strOperatorCd = String.valueOf(comList.get(24));
                    csvArray[24] = strOperatorCd;

                    String strLastProductionDate = String.valueOf(comList.get(25));//LastProductionDate
                    if (!StringUtils.isEmpty(strLastProductionDate)) {
                        strLastProductionDate = DateFormat.formatDateYear(strLastProductionDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[25] = strLastProductionDate.equals("-1") ? String.valueOf(comList.get(25)) : strLastProductionDate;

                    String strTotalProductionTimeHour = String.valueOf(comList.get(26));//TotalProductionTimeHour
                    csvArray[26] = strTotalProductionTimeHour;
                    String strTotalShotCount = String.valueOf(comList.get(27));//TotalShotCount
                    csvArray[27] = strTotalShotCount;

                    String strLastMainteDate = String.valueOf(comList.get(28));//LastMainteDate
                    if (!StringUtils.isEmpty(strLastMainteDate)) {
                        strLastMainteDate = DateFormat.formatDateYear(strLastMainteDate, DateFormat.DATE_FORMAT);
                    }
                    csvArray[28] = strLastMainteDate.equals("-1") ? String.valueOf(comList.get(28)) : strLastMainteDate;

                    String strAfterMainteTotalProductionTimeHour = String.valueOf(comList.get(29));//AfterMainteTotalProductionTimeHour
                    csvArray[29] = strAfterMainteTotalProductionTimeHour;
                    String strAfterMainteTotalShotCount = String.valueOf(comList.get(30));//AfterMainteTotalShotCount
                    csvArray[30] = strAfterMainteTotalShotCount;
                    String strMainteCycleCode01 = String.valueOf(comList.get(31));//MainteCycleCode01
                    csvArray[31] = strMainteCycleCode01;
                    String strMainteCycleCode02 = String.valueOf(comList.get(32));//MainteCycleCode02
                    csvArray[32] = strMainteCycleCode02;
                    String strMainteCycleCode03 = String.valueOf(comList.get(33));//MainteCycleCode03
                    csvArray[33] = strMainteCycleCode03;

                    String strDelFlag = delFlagPostion <= comList.size() ? String.valueOf(comList.get(comList.size() - 1)) : null;//delFlag 指定されている場合

                    if (imachineSpec != 0) {//仕様があります時
                        int count = 34 + imachineSpec;
                        for (int index = 34; index < count; index++) {
                            csvArray[index] = String.valueOf(comList.get(index));
                        }
                    }

                    MstMachine oldMstMachine = entityManager.find(MstMachine.class, strMachineId);

                    boolean isExtData = FileUtil.checkMachineExternalByCompanyCode(entityManager, mstDictionaryService, strCompanyCode, loginUser).isError();

                    /**
                     * 削除
                     */
                    if (strDelFlag != null && strDelFlag.trim().equals("1")) {
                        if (null == oldMstMachine) {//MachineIdはmst_machineに存在か
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineId, strMachineId, error, 1, errorContents, notFound));
                        } else if (mstMachineService.getMstMachineFKCheck(strMachineId)) {
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineId, strMachineId, error, 1, errorContents, canNotdeleteMsg));
                        } else if (!isExtData) {
                            mstMachineService.deleteMstMachine(strMachineId);
                            deletedCount = deletedCount + 1;
                            //エラー情報をログファイルに記入
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineId, strMachineId, error, 0, result, deletedMsg));
                        } else {
                            failedCount = failedCount + 1;
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineId, strMachineId, error, 1, errorContents, extEdit));
                        }
                    } else {
                        MstMachine readCsvInfoMstMachine = new MstMachine();
                        MstMachineSpec readCsvInfoMstMachineSpec = null;
                        MstMachineSpecHistory readCsvInfoMstMachineSpecHistory = null;

                        boolean isChecked = isExtData ? mstMachineService.checkExtCsvFileData(logParm, csvArray, userLangId, logFile, i) : mstMachineService.checkCsvFileData(logParm, csvArray, userLangId, logFile, i);

                        if (isChecked) {
                            String strMachineType = strMachineTypeValue.trim();
                            String sMachineKey;
                            String strOwnerCompanyId = "";
                            String strCompanyId;
                            String strCompanyName;
                            String strLocationId;
                            String strLocationName;
                            Date dateInspectedDate = null;

                            readCsvInfoMstMachine.setMachineId(strMachineId);//設備ID
                            if (!isExtData) {
                                readCsvInfoMstMachine.setMachineName(strMachineName);//設備名称

                                if (null == inMachineTypeMapTemp) {
                                    inMachineTypeMapTemp = mstMachineService.inMachineTypeOfChoice(loginUser.getLangId());
                                }

                                if ("".equals(strMachineType)) {
                                    sMachineKey = "0";
                                } else {
                                    sMachineKey = inMachineTypeMapTemp.get(strMachineType).toString();
                                }

                                readCsvInfoMstMachine.setMachineType(Integer.parseInt(sMachineKey));//設備種類                            
                                if (!fu.isNullCheck(strMachineCreatedDate)) {
                                    Date dateMachineCreatedDate = fu.getDateParseForDate(strMachineCreatedDate);
                                    readCsvInfoMstMachine.setMachineCreatedDate(dateMachineCreatedDate);//設備作成日
                                }

                                if (!fu.isNullCheck(strInspectedDate)) {
                                    dateInspectedDate = fu.getDateParseForDate(strInspectedDate);
                                    readCsvInfoMstMachine.setInspectedDate(dateInspectedDate);//検収日
                                }

                                if (!fu.isNullCheck(strOwnerCompanyCode)) {
                                    MstCompanyList mstCompanyList = mstCompanyService.getMstCompanyDetail(strOwnerCompanyCode);
                                    if (mstCompanyList.getMstCompanies().size() > 0) {
                                        strOwnerCompanyId = mstCompanyList.getMstCompanies().get(0).getId();
                                        readCsvInfoMstMachine.setOwnerCompanyId(strOwnerCompanyId);//所有会社
                                    }
                                }

                                if (!fu.isNullCheck(strCompanyCode)) {
                                    MstCompanyList mstCompanyList = mstCompanyService.getMstCompanyDetail(strCompanyCode);
                                    if (mstCompanyList.getMstCompanies().size() > 0) {
                                        strCompanyId = mstCompanyList.getMstCompanies().get(0).getId();
                                        strCompanyName = mstCompanyList.getMstCompanies().get(0).getCompanyName();
                                        readCsvInfoMstMachine.setCompanyId(strCompanyId);//会社id
                                        readCsvInfoMstMachine.setCompanyName(strCompanyName);//会社名称
                                    }
                                }

                                if (!fu.isNullCheck(strLocationCode)) {
                                    MstLocationList mstLocationList = mstLocationService.getMstLocationDetail(strLocationCode);
                                    if (mstLocationList.getMstLocations().size() > 0) {
                                        strLocationId = mstLocationList.getMstLocations().get(0).getId();
                                        strLocationName = mstLocationList.getMstLocations().get(0).getLocationName();
                                        readCsvInfoMstMachine.setLocationId(strLocationId);//所在地id
                                        readCsvInfoMstMachine.setLocationName(strLocationName);//所在地名称
                                    }
                                }

                                String strInstllationSiteId;
                                String strInstllationSiteName;
                                if (!fu.isNullCheck(strInstllationSiteCode)) {
                                    MstInstallationSiteList mstInstallationSiteList = mstInstallationSiteService.getMstInstallationSiteDetail(strInstllationSiteCode);
                                    if (mstInstallationSiteList.getMstInstallationSites().size() > 0) {
                                        strInstllationSiteId = mstInstallationSiteList.getMstInstallationSites().get(0).getId();
                                        strInstllationSiteName = mstInstallationSiteList.getMstInstallationSites().get(0).getInstallationSiteName();
                                        readCsvInfoMstMachine.setInstllationSiteId(strInstllationSiteId);//設置場所名称id
                                        readCsvInfoMstMachine.setInstllationSiteName(strInstllationSiteName);//設置場所名称
                                    }
                                }

                                if (null != oldMstMachine) {
                                    String newCompanyCode = StringUtils.isEmpty(strCompanyCode) ? "" : strCompanyCode;
                                    String newLocationCode = StringUtils.isEmpty(strLocationCode) ? "" : strLocationCode;
                                    String newInstallationSiteCode = StringUtils.isEmpty(strInstllationSiteCode) ? "" : strInstllationSiteCode;
                                    String oldCompanyCode = null == oldMstMachine.getMstCompany() ? "" : oldMstMachine.getMstCompany().getCompanyCode();
                                    String oldLocationCode = null == oldMstMachine.getMstLocation() ? "" : oldMstMachine.getMstLocation().getLocationCode();
                                    String oldInstallationSiteCode = null == oldMstMachine.getMstInstallationSite() ? "" : oldMstMachine.getMstInstallationSite().getInstallationSiteCode();

                                    if (oldCompanyCode.equals("") && oldLocationCode.equals("") && oldInstallationSiteCode.equals("")
                                            && newCompanyCode.equals("") && newLocationCode.equals("") && newInstallationSiteCode.equals("")) {
                                        strInstalledDate = "";
                                    } else if (!oldCompanyCode.equals("") || !oldLocationCode.equals("") || !oldInstallationSiteCode.equals("")) {
                                        if (newCompanyCode.equals("") && newLocationCode.equals("") && newInstallationSiteCode.equals("")) {
                                            if (null != oldMstMachine.getInstalledDate()) {
                                                strInstalledDate = fu.getDateFormatForStr(oldMstMachine.getInstalledDate());
                                            }
                                            readCsvInfoMstMachine.setCompanyId(null);//会社id
                                            readCsvInfoMstMachine.setCompanyName(null);//会社名称
                                            readCsvInfoMstMachine.setLocationId(null);//所在地id
                                            readCsvInfoMstMachine.setLocationName(null);//所在地名称
                                            readCsvInfoMstMachine.setInstllationSiteId(null);//設置場所名称id
                                            readCsvInfoMstMachine.setInstllationSiteName(null);//設置場所名称  

                                            TblMachineLocationHistoryVo hVo = tblMachineLocationHistoryService.getMachineLocationHistories(strMachineId, loginUser);
                                            if (null != hVo && null != hVo.getTblMachineLocationHistoryVos() && hVo.getTblMachineLocationHistoryVos().size() > 1) {
                                                TblMachineLocationHistoryVo oldVo = hVo.getTblMachineLocationHistoryVos().get(1);
                                                if (!StringUtils.isEmpty(oldVo.getCompanyId())) {
                                                    readCsvInfoMstMachine.setCompanyId(oldVo.getCompanyId());//会社id
                                                    readCsvInfoMstMachine.setCompanyName(oldVo.getCompanyName());//会社名称
                                                } else {
                                                    readCsvInfoMstMachine.setCompanyId(null);//会社id
                                                    readCsvInfoMstMachine.setCompanyName(null);//会社名称
                                                }
                                                if (!StringUtils.isEmpty(oldVo.getLocationId())) {
                                                    readCsvInfoMstMachine.setLocationId(oldVo.getLocationId());//所在地id
                                                    readCsvInfoMstMachine.setLocationName(oldVo.getLocationName());//所在地名称
                                                } else {
                                                    readCsvInfoMstMachine.setLocationId(null);
                                                    readCsvInfoMstMachine.setLocationName(null);
                                                }
                                                if (!StringUtils.isEmpty(oldVo.getInstllationSiteId())) {
                                                    readCsvInfoMstMachine.setInstllationSiteId(oldVo.getInstllationSiteId());//設置場所名称id
                                                    readCsvInfoMstMachine.setInstllationSiteName(oldVo.getInstllationSiteName());//設置場所名称 
                                                } else {
                                                    readCsvInfoMstMachine.setInstllationSiteId(null);
                                                    readCsvInfoMstMachine.setInstllationSiteName(null);
                                                }
                                                readCsvInfoMstMachine.setInstalledDate(oldVo.getStartDate());
                                            } else {
                                                strInstalledDate = "";
                                                readCsvInfoMstMachine.setInstalledDate(null);
                                            }
                                        }
                                        if ((newCompanyCode.equals(oldCompanyCode) && newLocationCode.equals(oldLocationCode) && newInstallationSiteCode.contains(oldInstallationSiteCode))) {
                                            if (null != oldMstMachine.getInstalledDate()) {
                                                strInstalledDate = fu.getDateFormatForStr(oldMstMachine.getInstalledDate());
                                            }
                                        }
                                    }
                                }

                                if (!fu.isNullCheck(strInstalledDate)) {
                                    Date dateInstalledDate = fu.getDateParseForDate(strInstalledDate);
                                    readCsvInfoMstMachine.setInstalledDate(dateInstalledDate);//設置日  
                                } else {
                                    readCsvInfoMstMachine.setInstalledDate(null);//設置日  
                                }

                                inStatusMapTemp = null == inStatusMapTemp ? mstMachineService.inStatusOfChoice(loginUser.getLangId()) : inStatusMapTemp;
                                String sStatusKey = inStatusMapTemp.get(strStatus).toString();
                                readCsvInfoMstMachine.setStatus(Integer.parseInt(sStatusKey));//ステータス
                                if (!fu.isNullCheck(strStatusChangedDate)) {
                                    Date dateStatusChangedDate = fu.getDateParseForDate(strStatusChangedDate);
                                    readCsvInfoMstMachine.setStatusChangedDate(dateStatusChangedDate);//ステータス日
                                } else {
                                    readCsvInfoMstMachine.setStatusChangedDate(new Date());//ステータス日
                                }

                                if (!fu.isNullCheck(strBaseCycleTime)) {
                                    readCsvInfoMstMachine.setBaseCycleTime(new BigDecimal(strBaseCycleTime));
                                } else {
                                    readCsvInfoMstMachine.setBaseCycleTime(BigDecimal.ZERO);
                                }

                                readCsvInfoMstMachine.setDepartment(0);
                                if (!fu.isNullCheck(strDepartment) && !"0".equals(strDepartment)) {
                                    MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
                                    for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                                        if (mstChoice.getChoice().equals(strDepartment.trim())) {
                                            readCsvInfoMstMachine.setDepartment(Integer.parseInt(mstChoice.getMstChoicePK().getSeq()));
                                            break;
                                        }
                                    }
                                }
                            }

                            if (!fu.isNullCheck(strLastProductionDate)) {
                                readCsvInfoMstMachine.setLastProductionDate(fu.getDateParseForDate(strLastProductionDate));
                            } else {
                                readCsvInfoMstMachine.setLastProductionDate(null);
                            }

                            if (!fu.isNullCheck(strTotalProductionTimeHour)) {
                                readCsvInfoMstMachine.setTotalProducingTimeHour(new BigDecimal(strTotalProductionTimeHour));
                            } else {
                                readCsvInfoMstMachine.setTotalProducingTimeHour(BigDecimal.ZERO);
                            }
                            if (!fu.isNullCheck(strTotalShotCount)) {
                                readCsvInfoMstMachine.setTotalShotCount(Integer.parseInt(strTotalShotCount));
                            } else {
                                readCsvInfoMstMachine.setTotalShotCount(0);
                            }
                            if (!fu.isNullCheck(strLastMainteDate)) {
                                readCsvInfoMstMachine.setLastMainteDate(fu.getDateParseForDate(strLastMainteDate));
                            } else {
                                readCsvInfoMstMachine.setLastMainteDate(null);
                            }
                            if (!fu.isNullCheck(strAfterMainteTotalProductionTimeHour)) {
                                readCsvInfoMstMachine.setAfterMainteTotalProducingTimeHour(new BigDecimal(strAfterMainteTotalProductionTimeHour));
                            } else {
                                readCsvInfoMstMachine.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
                            }
                            if (!fu.isNullCheck(strAfterMainteTotalShotCount)) {
                                readCsvInfoMstMachine.setAfterMainteTotalShotCount(Integer.parseInt(strAfterMainteTotalShotCount));
                            } else {
                                readCsvInfoMstMachine.setAfterMainteTotalShotCount(0);
                            }
                            readCsvInfoMstMachine.setBlMaintenanceCyclePtn01(null);
                            readCsvInfoMstMachine.setBlMaintenanceCyclePtn02(null);
                            readCsvInfoMstMachine.setBlMaintenanceCyclePtn03(null);
                            if (!(StringUtils.isEmpty(strMainteCycleCode01) && StringUtils.isEmpty(strMainteCycleCode02) && StringUtils.isEmpty(strMainteCycleCode03))) {
                                List<TblMaintenanceCyclePtn> maintenanceCyclePtns = tblMaintenanceCyclePtnService.getTblMaintenanceCyclePtnsByTypeAndCodes(2, strMainteCycleCode01, strMainteCycleCode02, strMainteCycleCode03);
                                for (TblMaintenanceCyclePtn maintenanceCyclePtn : maintenanceCyclePtns) {
                                    if (maintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getCycleCode().equals(strMainteCycleCode01)) {
                                        readCsvInfoMstMachine.setMainteCycleId01(maintenanceCyclePtn.getId());
                                    }
                                    if (maintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getCycleCode().equals(strMainteCycleCode02)) {
                                        readCsvInfoMstMachine.setMainteCycleId02(maintenanceCyclePtn.getId());
                                    }
                                    if (maintenanceCyclePtn.getTblMaintenanceCyclePtnPK().getCycleCode().equals(strMainteCycleCode03)) {
                                        readCsvInfoMstMachine.setMainteCycleId03(maintenanceCyclePtn.getId());
                                    }
                                }
                            }

                            if (null != oldMstMachine) {
                                mstMachineService.deleteMachineMaintenanceRecomend(oldMstMachine, readCsvInfoMstMachine);
                            }

                            if (!fu.isNullCheck(strSigmaCode)) {
                                List<MstSigma> sigmas = entityManager.createNamedQuery("MstSigma.findBySigmaCode")
                                        .setParameter("sigmaCode", strSigmaCode)
                                        .setMaxResults(1)
                                        .getResultList();
                                if (null != sigmas && !sigmas.isEmpty()) {
                                    readCsvInfoMstMachine.setSigmaId(sigmas.get(0).getId());
                                }
                            }
                            if (!fu.isNullCheck(strMacKey)) {
                                readCsvInfoMstMachine.setMacKey(strMacKey);
                            }
//                        if (!fu.isNullCheck(strMachineCd)) {
                            readCsvInfoMstMachine.setMachineCd(strMachineCd);
//                        }
//                        if (!fu.isNullCheck(strStrageLocationCd)) {
                            readCsvInfoMstMachine.setStrageLocationCd(strStrageLocationCd);
//                        }
//                        if (!fu.isNullCheck(strChargeCd)) {
                            readCsvInfoMstMachine.setChargeCd(strChargeCd);
//                        }
//                        if (!fu.isNullCheck(strOperatorCd)) {
                            readCsvInfoMstMachine.setOperatorCd(strOperatorCd);
//                        }

                            if (null != oldMstMachine) {
                                // データを更新
                                mstMachineAssetNoService.updateMstMachineAssetNoByMachineUuId(oldMstMachine.getUuid(), loginUser.getUserUuid());
                                if (!fu.isNullCheck(strMainAssetNo)) {
                                    readCsvInfoMstMachine.setMainAssetNo(strMainAssetNo);//代表資産番号

                                    MstMachineAssetNoPK mstMachineAssetNoPK = new MstMachineAssetNoPK();
                                    mstMachineAssetNoPK.setMachineUuid(oldMstMachine.getUuid());
                                    mstMachineAssetNoPK.setAssetNo(strMainAssetNo);
                                    MstMachineAssetNo mstMachineAssetNo = entityManager.find(MstMachineAssetNo.class, mstMachineAssetNoPK);

                                    if (null == mstMachineAssetNo) {
                                        mstMachineAssetNo = new MstMachineAssetNo();

                                        mstMachineAssetNo.setMstMachineAssetNoPK(mstMachineAssetNoPK);
                                        mstMachineAssetNo.setMainFlg(1);
                                        mstMachineAssetNo.setAssetAmount(BigDecimal.ZERO);
                                        mstMachineAssetNo.setNumberedDate(new Date());
                                        mstMachineAssetNo.setCreateDate(new Date());
                                        mstMachineAssetNo.setCreateUserUuid(loginUser.getUserUuid());
                                        mstMachineAssetNo.setUpdateDate(new Date());
                                        mstMachineAssetNo.setUpdateUserUuid(loginUser.getUserUuid());
                                        mstMachineAssetNo.setId(IDGenerator.generate());
                                        mstMachineAssetNoService.createMstMachineAssetNo(mstMachineAssetNo);
                                    } else {
                                        mstMachineAssetNo.setMainFlg(1);
                                        mstMachineAssetNo.setUpdateDate(new Date());
                                        mstMachineAssetNo.setUpdateUserUuid(loginUser.getUserUuid());
                                        mstMachineAssetNoService.updateMstMachineAssetNo(mstMachineAssetNo);
                                    }
                                } else {
                                    readCsvInfoMstMachine.setMainAssetNo(null);//代表資産番号    
                                }

                                Date sysDate = new Date();
                                readCsvInfoMstMachine.setUpdateDate(sysDate);
                                readCsvInfoMstMachine.setUpdateUserUuid(loginUser.getUserUuid());
                                //如果status没有改变把StatusChangedDate改回旧的
                                if (isExtData || readCsvInfoMstMachine.getStatus().compareTo(oldMstMachine.getStatus()) == 0) {
                                    readCsvInfoMstMachine.setStatusChangedDate(oldMstMachine.getStatusChangedDate());
                                }
                                int count = 0;
                                if (isExtData) {
                                    count = mstMachineService.updateExtMachineByQuery(readCsvInfoMstMachine);
                                } else {
                                    count = mstMachineService.updateMstMachineByQuery(readCsvInfoMstMachine);
                                }

                                if (comList.size() >= 35 && !isExtData) {
                                    String strMachineUuId = oldMstMachine.getUuid();
                                    //headは"設備仕様"がありますとき
                                    MstMachineSpecHistory oldMachineSpecHistory;
                                    if (strHead.contains(head_machineSpec)) {
                                        oldMachineSpecHistory = mstMachineSpecHistoryService.getMstMachineSpecHistoryLatestByMachineUuidFromMachineCsv(strMachineUuId);//最新のバージョン

                                        //設備種類変更時 すべての設備仕様履歴を削除し
                                        if (oldMachineSpecHistory == null || FileUtil.getIntegerValue(oldMstMachine.getMachineType()).compareTo(FileUtil.getIntegerValue(readCsvInfoMstMachine.getMachineType())) != 0) {
                                            mstMachineSpecHistoryService.deleteMstMachineSpecHistory(strMachineUuId);

                                            String strHistoryId = IDGenerator.generate();
                                            readCsvInfoMstMachineSpecHistory = new MstMachineSpecHistory();
                                            readCsvInfoMstMachineSpecHistory.setMachineUuid(strMachineUuId);
                                            readCsvInfoMstMachineSpecHistory.setStartDate(null == readCsvInfoMstMachine.getCreateDate() ? new Date() : readCsvInfoMstMachine.getCreateDate());//設備作成日
                                            readCsvInfoMstMachineSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//システム最大日付
                                            readCsvInfoMstMachineSpecHistory.setId(strHistoryId);
                                            String firstVersion = mstDictionaryService.getDictionaryValue(userLangId, "machine_spec_first_version");
                                            readCsvInfoMstMachineSpecHistory.setMachineSpecName(firstVersion);//"最初のバージョン" (文言キー:machine_spec_first_version)
                                            readCsvInfoMstMachineSpecHistory.setCreateDate(sysDate);
                                            readCsvInfoMstMachineSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
                                            readCsvInfoMstMachineSpecHistory.setUpdateDate(sysDate);
                                            readCsvInfoMstMachineSpecHistory.setUpdateUserUuid(loginUser.getUserUuid());
                                            mstMachineSpecHistoryService.createMstMachineSpecHistoryByCsv(readCsvInfoMstMachineSpecHistory);

                                            MstMachineAttributeList mstMachineAttribute = mstMachineAttributeService.getMstMachineAttributes(String.valueOf(readCsvInfoMstMachine.getMachineType()));
                                            int attrCode = mstMachineAttribute.getMstMachineAttributes().size();

                                            int iFor;
                                            if (comList.size() - 35 > attrCode) {
                                                iFor = attrCode;
                                            } else {
                                                iFor = comList.size() - 35;
                                            }

                                            for (int j = 0; j < iFor; j++) {

                                                String strAttrValue = String.valueOf(comList.get(j + 34)).trim();
                                                String strAttrId = mstMachineAttribute.getMstMachineAttributes().get(j).getId();

                                                if (fu.maxLangthCheck(strAttrValue, 256)) {
                                                    //エラー情報をログファイルに記入
                                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineSpec.concat(String.valueOf(j + 32)), strAttrValue, error, 1, errorContents, maxLangth));
                                                    failedCount = failedCount + 1;
                                                    continue;
                                                } else {
                                                    MstMachineSpecPK newPk = new MstMachineSpecPK();
                                                    newPk.setAttrId(strAttrId);
                                                    newPk.setMachineSpecHstId(strHistoryId);
                                                    readCsvInfoMstMachineSpec = new MstMachineSpec();
                                                    readCsvInfoMstMachineSpec.setId(IDGenerator.generate());
                                                    readCsvInfoMstMachineSpec.setAttrValue(strAttrValue);
                                                    readCsvInfoMstMachineSpec.setMstMachineSpecPK(newPk);
                                                    readCsvInfoMstMachineSpec.setCreateDate(sysDate);
                                                    readCsvInfoMstMachineSpec.setCreateUserUuid(loginUser.getUserUuid());
                                                    readCsvInfoMstMachineSpec.setUpdateDate(sysDate);
                                                    readCsvInfoMstMachineSpec.setUpdateUserUuid(loginUser.getUserUuid());
                                                }
                                                if (!mstMachineSpecService.getMstMachineSpecsFK(strHistoryId, strAttrId)) {
                                                    mstMachineSpecService.createMstMachineSpecByCsv(readCsvInfoMstMachineSpec);
                                                }
                                            }

                                        } else {
                                            readCsvInfoMstMachineSpec = new MstMachineSpec();
                                            String strNewMstMachineSpecHistoryById = oldMachineSpecHistory.getId();
                                            MstMachineAttributeList mstMachineAttribute = mstMachineAttributeService.getMstMachineAttributes(String.valueOf(readCsvInfoMstMachine.getMachineType()));
                                            int attrCode = mstMachineAttribute.getMstMachineAttributes().size();
                                            int iFor;
                                            if (comList.size() - 35 > attrCode) {
                                                iFor = attrCode;
                                            } else {
                                                iFor = comList.size() - 35;
                                            }
                                            for (int j = 0; j < iFor; j++) {
                                                String strAttrValue = String.valueOf(comList.get(j + 34)).trim();
                                                String strAttrId = mstMachineAttribute.getMstMachineAttributes().get(j).getId();
                                                if (fu.maxLangthCheck(strAttrValue, 256)) {
                                                    //エラー情報をログファイルに記入
                                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineSpec.concat(String.valueOf(j + 37)), strAttrValue, error, 1, errorContents, maxLangth));
                                                    failedCount = failedCount + 1;
                                                    continue;
                                                } else {
                                                    MstMachineSpecPK newPk = new MstMachineSpecPK();
                                                    newPk.setAttrId(strAttrId);
                                                    newPk.setMachineSpecHstId(strNewMstMachineSpecHistoryById);
                                                    readCsvInfoMstMachineSpec.setAttrValue(strAttrValue);
                                                    readCsvInfoMstMachineSpec.setMstMachineSpecPK(newPk);
                                                    readCsvInfoMstMachineSpec.setUpdateDate(sysDate);
                                                    readCsvInfoMstMachineSpec.setUpdateUserUuid(loginUser.getUserUuid());
                                                }
                                                if (mstMachineSpecService.getMstMachineSpecsFK(strNewMstMachineSpecHistoryById, strAttrId)) {
                                                    mstMachineSpecService.updateMstMachineSpecByQuery(readCsvInfoMstMachineSpec);
                                                }
                                            }
                                        }
                                    }

                                    String oldCompanyId = null == oldMstMachine.getCompanyId() ? "" : oldMstMachine.getCompanyId();
                                    String oldLocationId = null == oldMstMachine.getLocationId() ? "" : oldMstMachine.getLocationId();
                                    String oldInstllationSiteId = null == oldMstMachine.getInstllationSiteId() ? "" : oldMstMachine.getInstllationSiteId();
                                    String newCompanyId = null == readCsvInfoMstMachine.getCompanyId() ? "" : readCsvInfoMstMachine.getCompanyId();
                                    String newLocationId = null == readCsvInfoMstMachine.getLocationId() ? "" : readCsvInfoMstMachine.getLocationId();
                                    String newInstllationSiteId = null == readCsvInfoMstMachine.getInstllationSiteId() ? "" : readCsvInfoMstMachine.getInstllationSiteId();
                                    if (!oldCompanyId.equals(newCompanyId) || !oldLocationId.equals(newLocationId) || !oldInstllationSiteId.equals(newInstllationSiteId)) {
                                        tblMachineLocationHistoryService.updateLatestTblMachineLocationHistoryDateByMachineUuid(strMachineUuId);

                                        if (!strCompanyCode.equals("") || !strLocationCode.equals("") || !strInstllationSiteCode.equals("")) {
                                            TblMachineLocationHistory readCsvInfoTblMachineLocationHistory = new TblMachineLocationHistory();
                                            readCsvInfoTblMachineLocationHistory.setId(IDGenerator.generate());
                                            readCsvInfoTblMachineLocationHistory.setMachineUuid(strMachineUuId);
                                            readCsvInfoTblMachineLocationHistory.setChangeReason(0); //変更理由：なし（ゼロ）
                                            if (null != readCsvInfoMstMachine.getCompanyId()) {
                                                readCsvInfoTblMachineLocationHistory.setCompanyId(readCsvInfoMstMachine.getCompanyId());
                                                readCsvInfoTblMachineLocationHistory.setCompanyName(null == readCsvInfoMstMachine.getCompanyName() ? null : readCsvInfoMstMachine.getCompanyName());
                                            }
                                            if (null != readCsvInfoMstMachine.getLocationId()) {
                                                readCsvInfoTblMachineLocationHistory.setLocationId(readCsvInfoMstMachine.getLocationId());
                                                readCsvInfoTblMachineLocationHistory.setLocationName(null == readCsvInfoMstMachine.getLocationName() ? readCsvInfoMstMachine.getLocationName() : readCsvInfoMstMachine.getLocationName());
                                            }
                                            if (null != readCsvInfoMstMachine.getInstllationSiteId()) {
                                                readCsvInfoTblMachineLocationHistory.setInstllationSiteId(readCsvInfoMstMachine.getInstllationSiteId());
                                                readCsvInfoTblMachineLocationHistory.setInstllationSiteName(null == readCsvInfoMstMachine.getInstllationSiteName() ? null : readCsvInfoMstMachine.getInstllationSiteName());
                                            }
                                            readCsvInfoTblMachineLocationHistory.setCreateDate(new Date());
                                            readCsvInfoTblMachineLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
                                            readCsvInfoTblMachineLocationHistory.setStartDate(null == readCsvInfoMstMachine.getInstalledDate() ? new Date() : readCsvInfoMstMachine.getInstalledDate());
                                            readCsvInfoTblMachineLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);
                                            tblMachineLocationHistoryService.creatMachineLocationHistories(readCsvInfoTblMachineLocationHistory);
                                        }
                                    }
                                    if ((!oldCompanyId.isEmpty() || !oldLocationId.isEmpty() || !oldInstllationSiteId.isEmpty())
                                            && (strCompanyCode.equals("") && strLocationCode.equals("") && strInstllationSiteCode.equals(""))) {
                                        tblMachineLocationHistoryService.deleteMachineLocationHistorieLatest(strMachineUuId);
                                    }
                                }

                                updatedCount = updatedCount + count;
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineId, readCsvInfoMstMachine.getMachineId(), error, 0, result, updatedMsg));
                            } else {
                                //追加
                                //ライセンス リミット
                                BasicResponse response = new BasicResponse();
                                if (mstLicenseLimitService.CheckMachineLimit(loginUser.getLangId(),response)) {
                                
                                    Date sysDate = new Date();
                                    String strUuId = IDGenerator.generate();
                                    Date machineCreatDate = readCsvInfoMstMachine.getMachineCreatedDate();
                                    readCsvInfoMstMachine.setUuid(strUuId);
                                    readCsvInfoMstMachine.setInspectedDate(dateInspectedDate);
                                    readCsvInfoMstMachine.setMainteStatus(0);

                                    readCsvInfoMstMachine.setCreateDate(sysDate);
                                    readCsvInfoMstMachine.setCreateUserUuid(loginUser.getUserUuid());
                                    readCsvInfoMstMachine.setUpdateDate(sysDate);
                                    readCsvInfoMstMachine.setUpdateUserUuid(loginUser.getUserUuid());
                                    readCsvInfoMstMachine.setMainAssetNo(strMainAssetNo);//代表資産番号
                                    mstMachineService.createMstMachineByCsv(readCsvInfoMstMachine);

                                    if (comList.size() >= 35) {
                                        //headは"設備仕様"がありますとき
                                        if (strHead.contains(head_machineSpec)) {
                                            String strHistoryId = IDGenerator.generate();
                                            readCsvInfoMstMachineSpecHistory = new MstMachineSpecHistory();
                                            readCsvInfoMstMachineSpecHistory.setMachineUuid(strUuId);
                                            readCsvInfoMstMachineSpecHistory.setStartDate(null == machineCreatDate ? new Date() : machineCreatDate);//設備作成日
                                            readCsvInfoMstMachineSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//システム最大日付
                                            readCsvInfoMstMachineSpecHistory.setId(strHistoryId);
                                            String firstVersion = mstDictionaryService.getDictionaryValue(userLangId, "machine_spec_first_version");
                                            readCsvInfoMstMachineSpecHistory.setMachineSpecName(firstVersion);//"最初のバージョン" (文言キー:machine_spec_first_version)
                                            readCsvInfoMstMachineSpecHistory.setCreateDate(sysDate);
                                            readCsvInfoMstMachineSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
                                            readCsvInfoMstMachineSpecHistory.setUpdateDate(sysDate);
                                            readCsvInfoMstMachineSpecHistory.setUpdateUserUuid(loginUser.getUserUuid());
                                            mstMachineSpecHistoryService.createMstMachineSpecHistoryByCsv(readCsvInfoMstMachineSpecHistory);

                                            MstMachineAttributeList mstMachineAttribute = mstMachineAttributeService.getMstMachineAttributes(String.valueOf(readCsvInfoMstMachine.getMachineType()));
                                            int attrCode = mstMachineAttribute.getMstMachineAttributes().size();
                                            int iFor;
                                            if (comList.size() - 35 > attrCode) {
                                                iFor = attrCode;
                                            } else {
                                                iFor = comList.size() - 35;
                                            }
                                            for (int j = 0; j < iFor; j++) {
                                                String strAttrValue = String.valueOf(comList.get(j + 34)).trim();
                                                String strAttrId = mstMachineAttribute.getMstMachineAttributes().get(j).getId();
                                                int attrType = mstMachineAttribute.getMstMachineAttributes().get(j).getAttrType();
                                                if (fu.maxLangthCheck(strAttrValue, 256)) {
                                                    //エラー情報をログファイルに記入
                                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineSpec.concat(String.valueOf(j + 32)), strAttrValue, error, 1, errorContents, maxLangth));
                                                    failedCount = failedCount + 1;
                                                    continue;
                                                } else if (fu.checkSpecAttrType(attrType, strAttrValue) == false) {
                                                    //エラー情報をログファイルに記入
                                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineSpec.concat(String.valueOf(j + 24)), strAttrValue, error, 1, errorContents, errorValue));
                                                    failedCount = failedCount + 1;
                                                    continue;
                                                } else {
                                                    MstMachineSpecPK newPk = new MstMachineSpecPK();
                                                    newPk.setAttrId(strAttrId);
                                                    newPk.setMachineSpecHstId(strHistoryId);
                                                    readCsvInfoMstMachineSpec = new MstMachineSpec();
                                                    readCsvInfoMstMachineSpec.setId(IDGenerator.generate());
                                                    readCsvInfoMstMachineSpec.setAttrValue(strAttrValue);
                                                    readCsvInfoMstMachineSpec.setMstMachineSpecPK(newPk);
                                                    readCsvInfoMstMachineSpec.setCreateDate(sysDate);
                                                    readCsvInfoMstMachineSpec.setCreateUserUuid(loginUser.getUserUuid());
                                                    readCsvInfoMstMachineSpec.setUpdateDate(sysDate);
                                                    readCsvInfoMstMachineSpec.setUpdateUserUuid(loginUser.getUserUuid());
                                                }
                                                if (!mstMachineSpecService.getMstMachineSpecsFK(strHistoryId, strAttrId)) {
                                                    mstMachineSpecService.createMstMachineSpecByCsv(readCsvInfoMstMachineSpec);
                                                }
                                            }

                                        }
                                        if (null != readCsvInfoMstMachine.getCompanyId() || null != readCsvInfoMstMachine.getLocationId() || null != readCsvInfoMstMachine.getInstllationSiteId()) {

                                            TblMachineLocationHistory readCsvInfoTblMachineLocationHistory = new TblMachineLocationHistory();
                                            readCsvInfoTblMachineLocationHistory.setId(IDGenerator.generate());
                                            readCsvInfoTblMachineLocationHistory.setMachineUuid(readCsvInfoMstMachine.getUuid());
                                            readCsvInfoTblMachineLocationHistory.setChangeReason(0); //変更理由：なし（ゼロ）
                                            if (null != readCsvInfoMstMachine.getCompanyId()) {
                                                readCsvInfoTblMachineLocationHistory.setCompanyId(readCsvInfoMstMachine.getCompanyId());
                                                readCsvInfoTblMachineLocationHistory.setCompanyName(null == readCsvInfoMstMachine.getCompanyName() ? null : readCsvInfoMstMachine.getCompanyName());
                                            }
                                            if (null != readCsvInfoMstMachine.getLocationId()) {
                                                readCsvInfoTblMachineLocationHistory.setLocationId(readCsvInfoMstMachine.getLocationId());
                                                readCsvInfoTblMachineLocationHistory.setLocationName(null == readCsvInfoMstMachine.getLocationName() ? readCsvInfoMstMachine.getLocationName() : readCsvInfoMstMachine.getLocationName());
                                            }
                                            if (null != readCsvInfoMstMachine.getInstllationSiteId()) {
                                                readCsvInfoTblMachineLocationHistory.setInstllationSiteId(readCsvInfoMstMachine.getInstllationSiteId());
                                                readCsvInfoTblMachineLocationHistory.setInstllationSiteName(null == readCsvInfoMstMachine.getInstllationSiteName() ? null : readCsvInfoMstMachine.getInstllationSiteName());
                                            }
                                            readCsvInfoTblMachineLocationHistory.setCreateDate(new Date());
                                            readCsvInfoTblMachineLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
                                            readCsvInfoTblMachineLocationHistory.setStartDate(null == readCsvInfoMstMachine.getInstalledDate() ? new Date() : readCsvInfoMstMachine.getInstalledDate());
                                            readCsvInfoTblMachineLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);
                                            tblMachineLocationHistoryService.creatMachineLocationHistories(readCsvInfoTblMachineLocationHistory);
                                        }

                                        if (!fu.isNullCheck(strMainAssetNo)) {
                                            readCsvInfoMstMachine.setMainAssetNo(strMainAssetNo);//代表資産番号

                                            MstMachineAssetNoPK mstMachineAssetNoPK = new MstMachineAssetNoPK();
                                            mstMachineAssetNoPK.setMachineUuid(readCsvInfoMstMachine.getUuid());
                                            mstMachineAssetNoPK.setAssetNo(strMainAssetNo);
                                            MstMachineAssetNo mstMachineAssetNo = entityManager.find(MstMachineAssetNo.class, mstMachineAssetNoPK);

                                            if (null == mstMachineAssetNo) {
                                                mstMachineAssetNoService.updateMstMachineAssetNoByMachineUuId(readCsvInfoMstMachine.getUuid(), loginUser.getUserUuid());

                                                mstMachineAssetNo = new MstMachineAssetNo();

                                                mstMachineAssetNo.setMstMachineAssetNoPK(mstMachineAssetNoPK);
                                                mstMachineAssetNo.setMainFlg(1);
                                                mstMachineAssetNo.setAssetAmount(BigDecimal.ZERO);
                                                mstMachineAssetNo.setNumberedDate(new Date());
                                                mstMachineAssetNo.setCreateDate(new Date());
                                                mstMachineAssetNo.setCreateUserUuid(loginUser.getUserUuid());
                                                mstMachineAssetNo.setId(IDGenerator.generate());
                                                mstMachineAssetNoService.createMstMachineAssetNo(mstMachineAssetNo);
                                            }
                                        } else {
                                            readCsvInfoMstMachine.setMainAssetNo(null);//代表資産番号    
                                            mstMachineAssetNoService.updateMstMachineAssetNoByMachineUuId(readCsvInfoMstMachine.getUuid(), loginUser.getUserUuid());
                                        }
                                    }
                                    addedCount = addedCount + 1;
                                    //エラー情報をログファイルに記入
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineId, readCsvInfoMstMachine.getMachineId(), error, 0, result, addedMsg));

                                }
                                else
                                {
                                    failedCount = failedCount + 1;
                                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, head_machineId, readCsvInfoMstMachine.getMachineId(), error, 1, result, response.getErrorMessage()));
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
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_MACHINE);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(readList.size() - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MACHINE);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);

        return importResultResponse;

    }

    /**
     * 設備マスタ詳細取得
     *
     * @param machineId
     * @return
     */
    @GET
    @Path("detail/{machineId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineVo getMachineByMachineById(@PathParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMachineService.getMstMachineDetails(FileUtil.getDecode(machineId), loginUser);
    }

    /**
     * 設備マスタ追加
     *
     * @param mstMachineVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachine(MstMachineVo mstMachineVo) {
        BasicResponse response = new BasicResponse();
        String machineId = mstMachineVo.getMstMachine().getMachineId();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        if (mstMachineService.getMstMachineExistCheck(machineId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }

        if (mstMachineService.getMstMachineMacKeyExistCheck(mstMachineVo.getMstMachine().getMacKey())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }
        //ライセンス リミット
        if (mstLicenseLimitService.CheckMachineLimit(loginUser.getLangId(),response)) {
        mstMachineService.createMstMachineByMstMachineDetail(mstMachineVo, loginUser);
        }
        return response;
    }

    /**
     * 設備マスタ更新
     *
     * @param mstMachineVo
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMachine(MstMachineVo mstMachineVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = mstMachineService.updateMstMachineByMachineDetail(mstMachineVo, loginUser);
        return response;
    }

    /**
     * 設備コード・設備名称により設備マスタ取得※部分一致
     *
     * @param machineId
     * @param machineName
     * @return
     */
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getMachineLikeCodeOrName(@QueryParam("machineId") String machineId, @QueryParam("machineName") String machineName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        MstMachineList response = mstMachineService.getMstMachineAutoComplete(machineId, machineName, "like", loginUser);
        return response;
    }

    /**
     * 設備コード・設備名称により設備マスタ取得※部分一致
     *
     * @param machineId
     * @param machineName
     * @return
     */
    @GET
    @Path("likeWithoutDispose")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getMachineLikeCodeOrNameWithoutDispose(@QueryParam("machineId") String machineId, @QueryParam("machineName") String machineName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        MstMachineList response = mstMachineService.getMstMachineAutoCompleteWithoutDispose(machineId, machineName, "like", loginUser);
        return response;
    }    
    
    /**
     * 設備コード・設備名称により設備マスタ取得※完全一致
     *
     * @param machineId
     * @param machineName
     * @return
     */
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getMachineEqualCodeOrName(@QueryParam("machineId") String machineId, @QueryParam("machineName") String machineName) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return mstMachineService.getMstMachineAutoComplete(machineId, machineName, "equal", loginUser);
    }

    /**
     * 設備コード・設備名称により設備マスタ取得※完全一致
     *
     * @param machineId
     * @param machineName
     * @return
     */
    @GET
    @Path("equalWithoutDispose")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getMachineEqualCodeOrNameWithoutDispose(@QueryParam("machineId") String machineId, @QueryParam("machineName") String machineName) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return mstMachineService.getMstMachineAutoCompleteWithoutDispose(machineId, machineName, "equal", loginUser);
    }    
    
    /**
     * TT0003 通常打上 部品コード変更時 設備部品関係テーブルより部品を取得し、設備IDが一意に定まれば設備ID、設備名称を表示する。
     * （新規登録時はメモリに保持するのみ）
     *
     * @param machineId
     * @return
     */
    @GET
    @Path("autocomplete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldAutoComplete getMachineAutoComplete(@QueryParam("machineId") String machineId) {
        MstMachineList list = mstMachineService.getMstMachineDetailByMachineId(machineId);
        MstMoldAutoComplete response = new MstMoldAutoComplete();
        if (list.getMstMachines() != null && list.getMstMachines().size() > 0) {
            MstMachine mstMachine = list.getMstMachines().get(0);
            response.setUuid(mstMachine.getUuid());
            response.setMachineId(mstMachine.getMachineId());
            response.setMachineName(mstMachine.getMachineName());

        } else {
            response.setError(true);
        }
        return response;
    }

    /**
     * バッチで設備マスタデータを取得
     *
     * @param latestExecutedDate
     * @param machineId
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachine")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getExtMachinesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate,
            @QueryParam("machineId") String machineId,
            @QueryParam("machineUuid") String machineUuid) {
        //セッションから認証済みされたユーザのＩＤを取得し、キーとしてデータを絞ること
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMachineService.getExtMachinesByBatch(latestExecutedDate, machineId, machineUuid, loginUser.getUserid());
    }

    /**
     * この設備が外部管理しているかを確認、 外部であれば、所有のほう編集・メンテ・改造を行えない
     *
     * @param machineId
     * @return
     */
    @GET
    @Path("extcheck")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse checkExt(@QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", loginUser);
    }

    /**
     * getMachineUuid 連携コード(Σ軍師連携用)をキーとして、UUIDを取得する
     *
     * @param macKey
     * @return
     */
    @GET
    @Path("getmachineuuid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineVo getMachineUuid(@QueryParam("macKey") String macKey) {

        try {

            return mstMachineService.getMachineUuid(macKey);

        } catch (Exception e) {

            MstMachineVo mstMachineVo = new MstMachineVo();

            mstMachineVo.setError(true);

            mstMachineVo.setErrorMessage(e.toString());

            return mstMachineVo;

        }
    }

    @POST
    @Path("getUuidlist")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MstMachineList getMachineByMachineIdList(List<String> machineIdList) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMachineList machineList= mstMachineService.getMachineByMachineIdList(machineIdList, loginUser);
//        Map<String, String> result = new HashMap<>();
//        for(MstMachine machine : machineList.getMstMachines()){
//            result.put(machine.getMachineId(), machine.getUuid());
//        }
        return machineList;
    }
    
    /**
     * M1104_設備送信_送信
     *
     * @param mstMachineSendlist
     * @return
     */
    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse sendMachineToUsedCompany(MstMachineSendList mstMachineSendlist) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMachineService.sendMachineToUsedCompany(mstMachineSendlist, loginUser);
    }

    /**
     * 設備送信CSV取込ボタン 設備マスタCSV取込
     *
     * @param fileUuid
     * @return
     * @throws java.text.ParseException
     */
    @POST
    @Path("send/importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineSendList sendImportCsv(@PathParam("fileUuid") String fileUuid) throws ParseException {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return mstMachineService.sendImportCsv(fileUuid, loginUser);
    }

    /**
     * getConnectingSigma Σ軍師と連携している設備の一覧を取得する
     *
     * @param machineId
     * @param machineName
     * @param machineType
     * @param department
     * @return
     */
    @GET
    @Path("connectingsigma")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getConnectingSigma(
            @QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("machineType") Integer machineType,
            @QueryParam("department") Integer department
    ) {
        MstMachineList response = mstMachineService.getMstMachineSigmaNotNull(machineId, machineName, machineType, department);
        return response;
    }
    
    /**
     * 設備マスタ複数取得(ページ表示)
     *
     * @param machineId
     * @param machineName
     * @param mainAssetNo
     * @param ownerCompanyName
     * @param companyName
     * @param locationName
     * @param instllationSiteName
     * @param machineType
     * @param department
     * @param lastProductionDateFrom//最終生産日From
     * @param lastProductionDateTo//最終生産日To
     * @param totalProducingTimeHourFrom//累計生産時間From
     * @param totalProducingTimeHourTo//累計生産時間To
     * @param totalShotCountFrom//累計ショット数From
     * @param totalShotCountTo//累計ショット数To
     * @param machineCreatedDateFrom
     * @param machineCreatedDateTo
     * @param lastMainteDateFrom//最終メンテナンス日From
     * @param lastMainteDateTo//最終メンテナンス日To
     * @param afterMainteTotalShotCountFrom//メンテナンス後生産時間From
     * @param afterMainteTotalShotCountTo//メンテナンス後ショット数To
     * @param afterMainteTotalProducingTimeHourFrom//メンテナンス後生産時間From
     * @param afterMainteTotalProducingTimeHourTo//メンテナンス後ショット数To
     * @param status
     * @param orderByMachineName
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return an instance of MstMachineList
     */
    @GET
    @Path("getmachines")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getMachinesByPage(@QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName, @QueryParam("mainAssetNo") String mainAssetNo,
            @QueryParam("ownerCompanyName") String ownerCompanyName, @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("instllationSiteName") String instllationSiteName,
            @QueryParam("machineType") String machineType, @QueryParam("department") String department,
            @QueryParam("status") String status, @QueryParam("orderByMachineName") String orderByMachineName,
            // 4.2 対応 ZhangYing S
            @QueryParam("lastProductionDateFrom") String lastProductionDateFrom, // 最後生産日
                                                                                 // yyyy/MM/dd
            @QueryParam("lastProductionDateTo") String lastProductionDateTo, // 最後生産日
                                                                             // yyyy/MM/dd
            @QueryParam("lastMainteDateFrom") String lastMainteDateFrom, // 最終メンテナンス日
                                                                         // yyyy/MM/dd
            @QueryParam("lastMainteDateTo") String lastMainteDateTo, // 最終メンテナンス日
                                                                     // yyyy/MM/dd
            @QueryParam("machineCreatedDateFrom") String machineCreatedDateFrom, // 設備作成日yyyy/MM/dd
            @QueryParam("machineCreatedDateTo") String machineCreatedDateTo, // 設備作成日yyyy/MM/dd
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
            @QueryParam("sidx") String sidx, // ソートキー
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
        // 設備作成日From - To
        Date formatMachineCreatedDateFrom = null;
        Date formatMachineCreatedDateTo = null;
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

        Integer formatMachineType = null;
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
            // 設備作成日From - To
            if (machineCreatedDateFrom != null && !"".equals(machineCreatedDateFrom)) {
                formatMachineCreatedDateFrom = sdf.parse(machineCreatedDateFrom);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            if (machineCreatedDateTo != null && !"".equals(machineCreatedDateTo)) {
                formatMachineCreatedDateTo = sdf.parse(machineCreatedDateTo);
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
            if (machineType != null && !"".equals(machineType)) {
                formatMachineType = Integer.parseInt(machineType);
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
        MstMachineList mstMachineList = mstMachineService.getMstMachinesByPage(machineId, machineName, mainAssetNo,
                ownerCompanyName, companyName, locationName, instllationSiteName, formatMachineType, formatDepartment,
                formatLastProductionDateFrom, formatLastProductionDateTo, formatTotalProducingTimeHourFrom,
                formatTotalProducingTimeHourTo, formatTotalShotCountFrom, formatTotalShotCountTo,
                formatLastMainteDateFrom, formatLastMainteDateTo, formatAfterMainteTotalProducingTimeHourFrom,
                formatAfterMainteTotalProducingTimeHourTo, formatAfterMainteTotalShotCountFrom,
                formatAfterMainteTotalShotCountTo, formatMachineCreatedDateFrom, formatMachineCreatedDateTo,
                formatStatus, orderByMachineName, loginUser, sidx, sord, pageNumber, pageSize, true);
        return mstMachineList;
    }

    /**
     * 設備棚卸結果アップロード
     *
     * @param mstMachineStoctake
     * @return
     */
    @POST
    @Path("stocktake/result")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse machineStocktakeResult(
            MstMachineStoctake mstMachineStoctake//設備棚卸リストJsonフォーマット
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return postMachineStocktakeResult(mstMachineStoctake, loginUser);

    }

    /**
     * 設備棚卸画像アップロード
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
    public BasicResponse machineStocktakePicture(
            @FormDataParam("upfile") InputStream uploadFile,//アップロード対象ファイル
            @FormDataParam("upfile") FormDataContentDisposition uploadFileDetail,
            @FormDataParam("outputFileUuid") String outputFileUuid,//設備棚卸リストの出力ファイルID
            @FormDataParam("imageFileKey") String imageFileKey//画像ファイルユニークキー
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse basicResponse = mstMachineService.machineStocktakePicture(
                uploadFile,//アップロード対象ファイル
                uploadFileDetail,
                outputFileUuid,//設備棚卸リストの出力ファイルID
                imageFileKey,//画像ファイルユニークキー
                loginUser.getLangId(),
                loginUser.getUserUuid()
        );
        return basicResponse;
    }

    /**
     * 設備棚卸実施 - 検索
     *
     * @param searchConditionList
     * @return
     */
    @POST
    @Path("stocktake/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineStoctake getMachineStocktakeResult(
            SearchConditionList searchConditionList//続けて検索により検索条件が追加されたときは、追加された条件をORでつなげて検索する。
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMachineStoctake mstMachineStoctake = mstMachineService.getMachineStocktakeResult(searchConditionList, loginUser);

        return mstMachineStoctake;

    }

    /**
     *
     * @param mstMachineStoctake
     * @param loginUser
     * @return
     */
    private BasicResponse postMachineStocktakeResult(MstMachineStoctake mstMachineStoctake, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        try {
            int locationConfirmCount = 0;//1:所在確認
            int locationUnknownCount = 0;//2:所在不明
            int errorCount = 0;
            int machineIdNotExistCount = 0;//アップロードされた設備IDがマスタにないエラーカウント

            if (mstMachineStoctake != null && mstMachineStoctake.getMachineStocktake() != null) {
                List<Machine> machines = mstMachineStoctake.getMachineStocktake().getMachines();
                if (machines != null && !machines.isEmpty()) {
                    TblMachineInventory tblMachineInventory;
                    OutputErrorInfo outputErrorInfo;
                    FileUtil fu = new FileUtil();
                    String logFileUuid = IDGenerator.generate();//ログファイルUUID用意
                    String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
                    Map<String, String> dictMap = mstMachineService.getDictionaryList(loginUser.getLangId());
                    List<OutputErrorInfo> unknownErrorList = new ArrayList();
                    List<OutputErrorInfo> departmentErrorList = new ArrayList();
                    List<OutputErrorInfo> addtionalList = new ArrayList();

                    SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
                    fu.writeInfoToFile(logFile, fu.outLogValue(dictMap.get("row_number"), dictMap.get("machine_id"), dictMap.get("db_process"), dictMap.get("error_msg")));

                    List<TblMachineInventory> insTblMachineInventory = new ArrayList<>();
                    List<TblMachineInventory> updTblMachineInventory = new ArrayList<>();
                    List<MstMachine> updMstMachine = new ArrayList<>();

                    if (StringUtils.isNotEmpty(mstMachineStoctake.getMachineStocktake().getOutputFileUuid()) && fu.maxLangthCheck(mstMachineStoctake.getMachineStocktake().getOutputFileUuid(), 45)) {
                        basicResponse.setError(true);
                        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                        String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length");
                        basicResponse.setErrorMessage(msg);
                        return basicResponse;
                    }
                    Set<Integer> department = new HashSet<>();
                    MstUser uploadUser = entityManager.find(MstUser.class, loginUser.getUserid());
                    if (StringUtils.isNotEmpty(uploadUser.getDepartment())) {
                        try {
                            int uploadUserDepartment = Integer.parseInt(uploadUser.getDepartment());
                            if (0 != uploadUserDepartment) {
                                department.add(uploadUserDepartment);
                            }
                        } catch (NumberFormatException e) {
                            Logger.getLogger(MstMachineResource.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }

                    Map<String, MstCompany> mapCompany = new HashMap<>();
                    Map<String, MstLocation> mapLocation = new HashMap<>();
                    Map<String, MstInstallationSite> mapInstallationSite = new HashMap<>();
                    Set<Integer> setChoice = new HashSet<>();
                    for (int i = 0, j = 1; i < machines.size(); i++, j++) {
                        Machine machine = machines.get(i);
                        outputErrorInfo = null;

                        switch (machine.getStocktakeResult()) {
                            case 0:
                                //0:未実施
                                //処理しないのでスキップ
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("inventory_not_done"), ""));
                                continue;
                            case 1:
                                break;
                            case 2:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("machine_inventory_result"), machine.getStocktakeResult())));
                                errorCount = errorCount + 1;
                                continue;
                        }
                        if (StringUtils.isEmpty(machine.getMachineId())) {
                            //ログファイルを記入
                            fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_not_null_with_item"), dictMap.get("machine_id"))));
                            errorCount = errorCount + 1;
                            continue;
                        }

                        if (StringUtils.isNotEmpty(machine.getImageFileKey()) && fu.maxLangthCheck(machine.getImageFileKey(), 100)) {
                            //ログファイルを記入
                            fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_over_length_with_item"), dictMap.get("image_file_key"), machine.getImageFileKey().length())));
                            errorCount = errorCount + 1;
                            continue;
                        }

                        if (StringUtils.isNotEmpty(machine.getNotes()) && fu.maxLangthCheck(machine.getNotes(), 200)) {
                            //ログファイルを記入
                            fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_over_length_with_item"), dictMap.get("remarks"), machine.getNotes().length())));
                            errorCount = errorCount + 1;
                            continue;
                        }

                        Date inputInventoryDate;
                        Date inputInventoryDateSzt;
                        if (null != machine.getStocktakeDatetime()) {
                            String stocktakeDatetime = null;
                            try {
                                stocktakeDatetime = DateFormat.dateTimeFormat(machine.getStocktakeDatetime(), loginUser.getJavaZoneId());
                                inputInventoryDate = sdf.parse(stocktakeDatetime);
                                inputInventoryDateSzt = machine.getStocktakeDatetime();
                            } catch (ParseException ex) {
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("machine_inventory_date"), machine.getStocktakeDatetime())));
                                errorCount = errorCount + 1;
                                Logger.getLogger(MstMachineService.class.getName()).log(Level.SEVERE, null, ex);
                                continue;
                            }
                        } else {
                            inputInventoryDate = TimezoneConverter.getLocalTime(loginUser.getJavaZoneId());
                            inputInventoryDateSzt = new Date();
                        }

                        Date inputImageTakenDatetime;
                        Date inputImageTakenDatetimeSzt;
                        if (null != machine.getImageTakenDatetime()) {
                            String imageTakenDatetime = null;
                            try {
                                imageTakenDatetime = DateFormat.dateTimeFormat(machine.getImageTakenDatetime(), loginUser.getJavaZoneId());
                                inputImageTakenDatetime = sdf.parse(imageTakenDatetime);
                                inputImageTakenDatetimeSzt = machine.getImageTakenDatetime();
                            } catch (ParseException ex) {
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("issue_taken_date"), machine.getImageTakenDatetime())));
                                errorCount = errorCount + 1;
                                Logger.getLogger(MstMachineService.class.getName()).log(Level.SEVERE, null, ex);
                                continue;
                            }
                        } else {
                            inputImageTakenDatetime = TimezoneConverter.getLocalTime(loginUser.getJavaZoneId());
                            inputImageTakenDatetimeSzt = new Date();
                        }

                        switch (machine.getStocktakeMethod()) {
                            case 0:
                            case 1:
                            case 2:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("machine_confirm_method"), machine.getStocktakeMethod())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (machine.getChangeDepartment()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("department_change"), machine.getChangeDepartment())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (machine.getBarcodeReprint()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("bar_code_reprint"), machine.getBarcodeReprint())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (machine.getAssetDamaged()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("asset_damaged"), machine.getAssetDamaged())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (machine.getNotInUse()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("not_in_use"), machine.getNotInUse())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        switch (machine.getAdditionalFlag()) {
                            case 0:
                            case 1:
                                break;
                            default:
                                //ログファイルを記入
                                fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_date_format_invalid_with_item"), dictMap.get("additional_flag"), machine.getAdditionalFlag())));
                                errorCount = errorCount + 1;
                                continue;
                        }

                        if (machine.getDepartment() != 0) {
                            if (setChoice.add(machine.getDepartment())) {
                                MstChoice mstChoice = mstChoiceService.findByPk("mst_user.department", loginUser.getLangId(), "" + machine.getDepartment());
                                if (mstChoice == null) {
                                    //ログファイルを記入
                                    fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("user_department"))));
                                    errorCount = errorCount + 1;
                                    setChoice.remove(machine.getDepartment());
                                    continue;
                                }
                            }
                            department.add(machine.getDepartment());
                        }

                        MstMachine mstMachine = entityManager.find(MstMachine.class, machine.getMachineId());
                        if (mstMachine == null) {
                            //アップロードされた設備IDがマスタにないエラーカウント
                            machineIdNotExistCount = machineIdNotExistCount + 1;
                            //ログファイルを記入
                            fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("machine_id"))));
                            errorCount = errorCount + 1;
                            continue;
                        }

                        boolean isOld = false;
                        if (StringUtils.isNotEmpty(mstMachineStoctake.getMachineStocktake().getOutputFileUuid())
                                && StringUtils.isNotEmpty(machine.getImageFileKey())) {
                            Query query1 = entityManager.createQuery("SELECT machineInventory FROM TblMachineInventory machineInventory WHERE machineInventory.outputFileUuid = :outputFileUuid AND machineInventory.imageFileKey = :imageFileKey");
                            query1.setParameter("outputFileUuid", mstMachineStoctake.getMachineStocktake().getOutputFileUuid());
                            query1.setParameter("imageFileKey", machine.getImageFileKey());
                            try {
                                tblMachineInventory = (TblMachineInventory) query1.getSingleResult();
                                isOld = true;
                            } catch (NoResultException e) {
                                tblMachineInventory = new TblMachineInventory();
                                tblMachineInventory.setId(IDGenerator.generate());
                            }
                        } else {
                            tblMachineInventory = new TblMachineInventory();
                            tblMachineInventory.setId(IDGenerator.generate());
                        }

                        if (StringUtils.isNotEmpty(mstMachineStoctake.getMachineStocktake().getOutputFileUuid())) {
                            tblMachineInventory.setOutputFileUuid(mstMachineStoctake.getMachineStocktake().getOutputFileUuid());
                        }
                        if (StringUtils.isNotEmpty(machine.getImageFileKey())) {

                            tblMachineInventory.setImageFileKey(machine.getImageFileKey());
                        }

                        tblMachineInventory.setMachineUuid(mstMachine.getUuid());

                        tblMachineInventory.setInventoryDate(inputInventoryDate);
                        tblMachineInventory.setInventoryDateSzt(inputInventoryDateSzt);

                        tblMachineInventory.setTakenDate(inputImageTakenDatetime);
                        tblMachineInventory.setTakenDateStz(inputImageTakenDatetimeSzt);

                        tblMachineInventory.setPersonName(uploadUser.getUserName());
                        tblMachineInventory.setPersonUuid(loginUser.getUserUuid());

                        tblMachineInventory.setInventoryResult(machine.getStocktakeResult());

                        tblMachineInventory.setSiteConfirmMethod(machine.getStocktakeMethod());
                        tblMachineInventory.setMachineConfirmMethod(machine.getStocktakeMethod());

                        Query query;
                        if (StringUtils.isNotEmpty(machine.getCompanyCode())) {
                            MstCompany mstCompany = mapCompany.get(machine.getCompanyCode());
                            if (mstCompany != null) {
                                tblMachineInventory.setCompanyId(mstCompany.getId());
                                tblMachineInventory.setCompanyName(mstCompany.getCompanyName());
                            } else {

                                query = entityManager.createNamedQuery("MstCompany.findOnlyByCompanyCode");
                                query.setParameter("companyCode", machine.getCompanyCode());
                                query.setParameter("externalFlg", CommonConstants.MINEFLAG);

                                try {
                                    mstCompany = (MstCompany) query.getSingleResult();
                                    tblMachineInventory.setCompanyId(mstCompany.getId());
                                    tblMachineInventory.setCompanyName(mstCompany.getCompanyName());

                                    mapCompany.put(machine.getCompanyCode(), mstCompany);
                                } catch (NoResultException e) {
                                    //ログファイルを記入
                                    fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("company_code"))));
                                    errorCount = errorCount + 1;
                                    continue;
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(machine.getLocationCode())) {
                            MstLocation mstLocation = mapLocation.get(machine.getLocationCode());
                            if (mstLocation != null) {
                                tblMachineInventory.setLocationId(mstLocation.getId());
                                tblMachineInventory.setLocationName(mstLocation.getLocationName());
                            } else {
                                query = entityManager.createNamedQuery("MstLocation.findOnlyByLocationCode");
                                query.setParameter("locationCode", machine.getLocationCode());
                                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                                try {
                                    mstLocation = (MstLocation) query.getSingleResult();
                                    tblMachineInventory.setLocationId(mstLocation.getId());
                                    tblMachineInventory.setLocationName(mstLocation.getLocationName());
                                    mapLocation.put(machine.getLocationCode(), mstLocation);
                                } catch (NoResultException e) {
                                    //ログファイルを記入
                                    fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("location_code"))));
                                    errorCount = errorCount + 1;
                                    continue;
                                }
                            }
                        }
                        if (StringUtils.isNotEmpty(machine.getInstallationSiteCode())) {
                            MstInstallationSite mstInstallationSite = mapInstallationSite.get(machine.getInstallationSiteCode());
                            if (mstInstallationSite != null) {
                                tblMachineInventory.setInstllationSiteId(mstInstallationSite.getId());
                                tblMachineInventory.setInstllationSiteName(mstInstallationSite.getInstallationSiteName());
                            } else {
                                query = entityManager.createNamedQuery("MstInstallationSite.findByInstallationSiteCode");
                                query.setParameter("installationSiteCode", machine.getInstallationSiteCode());
                                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                                try {
                                    mstInstallationSite = (MstInstallationSite) query.getSingleResult();
                                    tblMachineInventory.setInstllationSiteId(mstInstallationSite.getId());
                                    tblMachineInventory.setInstllationSiteName(mstInstallationSite.getInstallationSiteName());
                                    mapInstallationSite.put(machine.getInstallationSiteCode(), mstInstallationSite);
                                } catch (NoResultException e) {
                                    //ログファイルを記入
                                    fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("error"), String.format(dictMap.get("msg_error_record_not_found_item"), dictMap.get("installation_site_code"))));
                                    errorCount = errorCount + 1;
                                    continue;
                                }
                            }
                        }
                        tblMachineInventory.setInputType(1);
                        tblMachineInventory.setDepartment(machine.getDepartment());
                        tblMachineInventory.setRemarks(machine.getNotes());
                        tblMachineInventory.setDepartmentChange(machine.getChangeDepartment());
                        tblMachineInventory.setBarcodeReprint(machine.getBarcodeReprint());
                        tblMachineInventory.setAssetDamaged(machine.getAssetDamaged());
                        tblMachineInventory.setNotInUse(machine.getNotInUse());
                        // 棚卸結果が１、２になったものを棚卸実施
                        switch (machine.getStocktakeResult()) {
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
                                mstMachine.setStatus(8);
                                break;
                            default:
                                continue;
                        }
                        if (machine.getChangeDepartment() == 1) {
                            if (outputErrorInfo == null) { // 以下の金型は部署変更と回答されましたが、変更後の所属が不明です。
                                outputErrorInfo = new OutputErrorInfo();
                                outputErrorInfo.setLocationConfirm(1);
                            } else { // 下の金型は所在不明で部署変更と回答されましたが、変更後の所属が不明です。
                                outputErrorInfo.setLocationConfirm(3);
                            }
                        }

                        if (outputErrorInfo != null) {
                            outputErrorInfo.setCompanyName(tblMachineInventory.getCompanyName());
                            outputErrorInfo.setLocationName(tblMachineInventory.getLocationName());
                            outputErrorInfo.setInstallationSiteName(tblMachineInventory.getInstllationSiteName());
                            outputErrorInfo.setDepartmentName(machine.getDepartmentName());
                            outputErrorInfo.setMachineId(mstMachine.getMachineId());
                            outputErrorInfo.setMachineName(mstMachine.getMachineName());

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
                        if (machine.getAdditionalFlag() == 1) {
                            if (outputErrorInfo == null) {
                                outputErrorInfo = new OutputErrorInfo();
                                outputErrorInfo.setMachineId(mstMachine.getMachineId());
                            }
                            outputErrorInfo.setQrPlateInfo(machine.getQrPlateInfo());
                            addtionalList.add(outputErrorInfo);
                        }

                        if (isOld) {
                            tblMachineInventory.setUpdateDate(new Date());
                            tblMachineInventory.setUpdateUserUuid(loginUser.getUserUuid());
                            updTblMachineInventory.add(tblMachineInventory);
                        } else {
                            tblMachineInventory.setCreateDate(new Date());
                            tblMachineInventory.setCreateUserUuid(loginUser.getUserUuid());
                            tblMachineInventory.setUpdateDate(new Date());
                            tblMachineInventory.setUpdateUserUuid(loginUser.getUserUuid());
                            insTblMachineInventory.add(tblMachineInventory);
                        }

                        mstMachine.setLatestInventoryId(tblMachineInventory.getId());
                        mstMachine.setInventoryStatus(1);//処理済
                        mstMachine.setUpdateDate(new Date());
                        mstMachine.setUpdateUserUuid(loginUser.getUserUuid());
                        //entityManager.merge(mstMachine);
                        updMstMachine.add(mstMachine);
                        //ログファイルを記入
                        fu.writeInfoToFile(logFile, fu.outLogValue("" + j, machine.getMachineId(), dictMap.get("msg_stocktake_result_imported"), ""));
                    }

                    mstMachineService.batchInsert(insTblMachineInventory, 0);
                    mstMachineService.batchInsert(updTblMachineInventory, 1);
                    mstMachineService.batchInsertMachine(updMstMachine);

                    //sendMail
                    // ファイルをReNameする  
                    String fileName = dictMap.get("stocktake_upload_result");
                    sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    fileName = fileName + "_" + sdf.format(new Date());
                    StringBuffer logPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
                    logPath = logPath.append(SEPARATOR).append(CommonConstants.LOG).append(SEPARATOR);
                    FileUtil.renameFile(logPath.toString(), logFileUuid + CommonConstants.EXT_LOG, fileName + CommonConstants.EXT_LOG);
                    logFile = logPath.toString() + fileName + CommonConstants.EXT_LOG;

                    mstMachineService.machineStocktakeResultPrepareMail(mstMachineStoctake,
                            unknownErrorList,
                            departmentErrorList,
                            addtionalList,
                            locationConfirmCount,
                            locationUnknownCount,
                            errorCount,
                            logFile,
                            department,
                            uploadUser);
                    
                    //発生したエラーが設備IDがマスタにないエラーのみの場合は、Responseのerrorをtrueにしない。errorCode, errorMessageもセットしない。
                    //if (errorCount > 0) {
                    if (errorCount > machineIdNotExistCount) {
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

        return mstMachineService.productionResultAdd(path.toString(), uuid, loginUser.getUserUuid(), loginUser.getLangId());

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
        return mstMachineService.productionResultAdd(csvFile, fileUuid, loginUser.getUserUuid(), loginUser.getLangId());
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

        return mstMachineService.productionResultExport(loginUser);
    }
}
