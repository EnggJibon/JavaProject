package com.kmcj.karte.resources.machine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.MailSender;
import com.kmcj.karte.batch.externalmold.choice.ExtMstChoiceService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.external.MstExternalDataGetSetting;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.installationsite.MstInstallationSiteService;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.location.MstLocationService;
import com.kmcj.karte.resources.machine.assetno.MstMachineAssetNo;
import com.kmcj.karte.resources.machine.assetno.MstMachineAssetNoPK;
import com.kmcj.karte.resources.machine.assetno.MstMachineAssetNoService;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttribute;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeList;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeService;
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportVo;
import com.kmcj.karte.resources.machine.inventory.Machine;
import com.kmcj.karte.resources.machine.inventory.MachineStocktake;
import com.kmcj.karte.resources.machine.inventory.MstMachineStoctake;
import com.kmcj.karte.resources.machine.inventory.OutputCondition;
import com.kmcj.karte.resources.machine.inventory.OutputErrorInfo;
import com.kmcj.karte.resources.machine.inventory.SearchCondition;
import com.kmcj.karte.resources.machine.inventory.SearchConditionList;
import com.kmcj.karte.resources.machine.inventory.TblMachineInventory;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistory;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistoryVo;
import com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomendService;
import com.kmcj.karte.resources.machine.proccond.MstMachineProcCond;
import com.kmcj.karte.resources.machine.proccond.MstMachineProcCondService;
import com.kmcj.karte.resources.machine.proccond.MstMachineProcCondVo;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpec;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecPK;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecService;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecVo;
import com.kmcj.karte.resources.machine.reception.TblMachineReceptionList;
import com.kmcj.karte.resources.machine.reception.TblMachineReceptionVo;
import com.kmcj.karte.resources.machine.spec.MstMachineSpec;
import com.kmcj.karte.resources.machine.spec.MstMachineSpecPK;
import com.kmcj.karte.resources.machine.spec.MstMachineSpecService;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistory;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtnService;
import com.kmcj.karte.resources.mold.MstMoldAutoComplete;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.sigma.MstSigma;
import com.kmcj.karte.resources.sigma.MstSigmaService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.NumberUtil;
import com.kmcj.karte.util.Pager;
import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

/**
 *
 * @author admin
 */
@Dependent
public class MstMachineService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstMachineSpecService mstMachineSpecService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstMachineProcCondSpecService mstMachineProcCondSpecService;

    @Inject
    private MstMachineProcCondService mstMachineProcCondService;

    @Inject
    private MstCompanyService mstCompanyService;

    private Map<String, String> inMachineTypeMapTemp;

    private Map<String, String> outMachineTypeMapTemp;

    private Map<String, String> inStatusMapTemp;

    private Map<String, String> inDepartmentsMapTemp;

    private Map<String, String> outStatusMapTemp;

    @Inject
    private MstLocationService mstLocationService;

    @Inject
    private MstSigmaService mstSigmaService;

    @Inject
    private MstInstallationSiteService mstInstallationSiteService;
    @Inject
    private ExtMstChoiceService extMstChoiceService;

    @Inject
    private MstMachineAssetNoService mstMachineAsseNoService;

    @Inject
    private TblUploadFileService tblUploadFileService;

    @Inject
    private MstMachineAttributeService mstMachineAttributeService;

    @Inject
    private TblMaintenanceCyclePtnService tblMaintenanceCyclePtnService;

    @Inject
    private TblMachineMaintenanceRecomendService tblMachineMaintenanceRecomendService;

    @Inject
    private MstUserMailReceptionService mstUserMailReceptionService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MailSender mailSender; //20170626 Apeng add

    public static final String EXT_LOGIN_URL = "ws/karte/api/authentication/extlogin?lang=";
    public static final String EXT_MACHINE_RECEPTION_UPDATE = "ws/karte/api/machine/reception/update";

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("machineId", " ORDER BY m.machineId ");// 設備ＩＤ
        orderKey.put("machineName", " ORDER BY m.machineName ");// 設備名称
        orderKey.put("machineType", " ORDER BY m.machineType ");// 設備種類
        orderKey.put("mainAssetNo", " ORDER BY m.mainAssetNo ");// 代表資産番号
        orderKey.put("machineCreatedDateStr", " ORDER BY m.machineCreatedDate ");// 設備作成日
        orderKey.put("inspectedDateStr", " ORDER BY m.inspectedDate ");// 検収日
        orderKey.put("ownerCompanyName", " ORDER BY oc.companyName ");// 所有会社名称
        orderKey.put("installedDateStr", " ORDER BY m.installedDate ");// 設置日
        orderKey.put("companyName", " ORDER BY c.companyName ");// 会社名称
        orderKey.put("locationName", " ORDER BY location.locationName ");// 所在地名称
        orderKey.put("instllationSiteName", " ORDER BY isite.installationSiteName ");// 設置場所名称
        orderKey.put("status", " ORDER BY m.status ");// ステータス
        orderKey.put("statusChangedDateStr", " ORDER BY m.statusChangedDate ");// ステータ変更日
        orderKey.put("departmentName", " ORDER BY m.department ");// 所属
        orderKey.put("sigmaCode", " ORDER BY s.sigmaCode ");// Σ軍師コード
        orderKey.put("macKey", " ORDER BY m.macKey ");// 連携コード
        orderKey.put("baseCycleTime", " ORDER BY m.baseCycleTime ");// 基準サイクルタイム(s)
        orderKey.put("machineCd", " ORDER BY m.machineCd ");// 設備コード
        orderKey.put("strageLocationCd", " ORDER BY m.strageLocationCd ");// 設備工程コード
        orderKey.put("chargeCd", " ORDER BY m.chargeCd ");// チャージコード
        orderKey.put("operatorCd", " ORDER BY m.operatorCd ");// 日報担当者コード
        orderKey.put("lastProductionDateStr", " ORDER BY m.lastProductionDate ");// 最終生産日
        orderKey.put("totalProducingTimeHour", " ORDER BY m.totalProducingTimeHour ");// 累計生産時間
        orderKey.put("totalShotCount", " ORDER BY m.totalShotCount ");// 累計ショット数
        orderKey.put("lastMainteDateStr", " ORDER BY m.lastMainteDate ");// 最終メンテナンス日
        orderKey.put("afterMainteTotalProducingTimeHour", " ORDER BY m.afterMainteTotalProducingTimeHour ");// メンテナンス後生産時間
        orderKey.put("afterMainteTotalShotCount", " ORDER BY m.afterMainteTotalShotCount ");// メンテナンス後ショット数
        orderKey.put("mainteCycleCode01", " ORDER BY cyclePtn01.tblMaintenanceCyclePtnPK.cycleCode ");// メンテサイクルID01
        orderKey.put("mainteCycleCode02", " ORDER BY cyclePtn02.tblMaintenanceCyclePtnPK.cycleCode ");// メンテサイクルID02
        orderKey.put("mainteCycleCode03", " ORDER BY cyclePtn03.tblMaintenanceCyclePtnPK.cycleCode ");// メンテサイクルID03       

    }
    
    /**
     *
     * @param machineUuid
     * @return
     */
    public MstMachineList getMstMachineDetail(String machineUuid) {
        Query query = entityManager.createNamedQuery("MstMachine.findByUuid");
        query.setParameter("uuid", machineUuid);
        List list = query.getResultList();
        MstMachineList response = new MstMachineList();
        response.setMstMachines(list);
        return response;
    }

    /**
     * 設備一覧使用
     *
     * @param machineId
     * @return
     */
    public MstMachineList getMstMachineDetailByMachineId(String machineId) {
        Query query = entityManager.createNamedQuery("MstMachine.findByMachineId");
        query.setParameter("machineId", machineId);
        List list = query.getResultList();
        MstMachineList response = new MstMachineList();
        response.setMstMachines(list);
        return response;
    }

    /**
     * 設備マスタ詳細取得
     *
     * @param machineId
     * @param loginUser
     * @return
     */
    public MstMachineVo getMachineByMachineId(String machineId, LoginUser loginUser) {
        MstMachineVo response = new MstMachineVo();
        Query query = entityManager.createNamedQuery("MstMachine.findByMachineId")
                .setParameter("machineId", machineId);
        MstMachine mstMachine;
        try {
            mstMachine = (MstMachine) query.getSingleResult();
        } catch (NoResultException e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        response.setMstMachine(mstMachine);
        return response;
    }

    public MstMachineList getMachineByMachineIdList(List<String> machineIdList, LoginUser loginUser) {
        MstMachineList response = new MstMachineList();
        Query query = entityManager.createNamedQuery("MstMachine.findByMachineIdList")
                .setParameter("machineIdList", machineIdList);
        List<MstMachine> mstMachines;
        try {
            mstMachines =  query.getResultList();
        } catch (NoResultException e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        response.setMstMachines(mstMachines);
        return response;
    }
        
    /**
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
     * @param lastMainteDateFrom//最終メンテナンス日From
     * @param lastMainteDateTo//最終メンテナンス日To
     * @param afterMainteTotalShotCountFrom//メンテナンス後生産時間From
     * @param afterMainteTotalShotCountTo//メンテナンス後ショット数To
     * @param afterMainteTotalProducingTimeHourFrom//メンテナンス後生産時間From
     * @param afterMainteTotalProducingTimeHourTo//メンテナンス後ショット数To
     * @param machineCreateDateFrom
     * @param machineCreateDateTo
     * @param status
     * @return
     */
    public CountResponse getMstMachineCount(
            String machineId,
            String machineName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer machineType,
            Integer department,
            Date lastProductionDateFrom,
            Date lastProductionDateTo,
            Integer totalProducingTimeHourFrom,
            Integer totalProducingTimeHourTo,
            Integer totalShotCountFrom,
            Integer totalShotCountTo,
            Date lastMainteDateFrom,
            Date lastMainteDateTo,
            Integer afterMainteTotalProducingTimeHourFrom,
            Integer afterMainteTotalProducingTimeHourTo,
            Integer afterMainteTotalShotCountFrom,
            Integer afterMainteTotalShotCountTo,
            Date machineCreateDateFrom,
            Date machineCreateDateTo,
            Integer status) {
        List list = getSql(
                machineId,
                machineName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                machineType,
                department,
                lastProductionDateFrom,
                lastProductionDateTo,
                totalProducingTimeHourFrom,
                totalProducingTimeHourTo,
                totalShotCountFrom,
                totalShotCountTo,
                lastMainteDateFrom,
                lastMainteDateTo,
                afterMainteTotalProducingTimeHourFrom,
                afterMainteTotalProducingTimeHourTo,
                afterMainteTotalShotCountFrom,
                afterMainteTotalShotCountTo,
                machineCreateDateFrom,
                machineCreateDateTo,
                status,
                null,
                "count");
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    public MstMachineList getMstMachines(
            String machineId,
            String machineName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer machineType,
            Integer department,
            Date lastProductionDateFrom,
            Date lastProductionDateTo,
            Integer totalProducingTimeHourFrom,
            Integer totalProducingTimeHourTo,
            Integer totalShotCountFrom,
            Integer totalShotCountTo,
            Date lastMainteDateFrom,
            Date lastMainteDateTo,
            Integer afterMainteTotalProducingTimeHourFrom,
            Integer afterMainteTotalProducingTimeHourTo,
            Integer afterMainteTotalShotCountFrom,
            Integer afterMainteTotalShotCountTo,
            Date machineCreatedDateFrom,
            Date machineCreatedDateTo,
            Integer status,
            String orderByMachineName,
            LoginUser loginUser) {
        MstChoiceList mstDepartmentChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
        MstMachineList mstMachineDetailList = new MstMachineList();
        List<MstMachineVo> mstMachineVos = new ArrayList<>();
        List<MstMachine> list = getSql(
                machineId,
                machineName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                machineType,
                department,
                lastProductionDateFrom,
                lastProductionDateTo,
                totalProducingTimeHourFrom,
                totalProducingTimeHourTo,
                totalShotCountFrom,
                totalShotCountTo,
                lastMainteDateFrom,
                lastMainteDateTo,
                afterMainteTotalProducingTimeHourFrom,
                afterMainteTotalProducingTimeHourTo,
                afterMainteTotalShotCountFrom,
                afterMainteTotalShotCountTo,
                machineCreatedDateFrom,
                machineCreatedDateTo,
                status,
                orderByMachineName,
                "");
        FileUtil fu = new FileUtil();
        for (MstMachine mstMachine : list) {
            MstMachineVo mstMachineVo = new MstMachineVo();
            mstMachineVo.setMachineUuid(mstMachine.getUuid());
            mstMachineVo.setMachineId(mstMachine.getMachineId());
            mstMachineVo.setMachineName(mstMachine.getMachineName());

            if (mstMachine.getCompanyId() != null && !"".equals(mstMachine.getCompanyId())) {
                if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", mstMachine.getCompanyId(), loginUser).isError() == true) {
                    mstMachineVo.setMachineTypeText(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine.machine_type", String.valueOf(mstMachine.getMachineType()), loginUser.getLangId()));
                    mstMachineVo.setExternalFlg(1);
                } else if (mstMachine.getMachineType() != null) {
                    mstMachineVo.setExternalFlg(0);
                    mstMachineVo.setMachineType(mstMachine.getMachineType());
                }
            } else {
                mstMachineVo.setExternalFlg(0);
                if (mstMachine.getMachineType() != null) {
                    mstMachineVo.setMachineType(mstMachine.getMachineType());
                    //mstMachineVo.setMachineTypeText(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine.machine_type", String.valueOf(mstMachine.getMachineType()), loginUser.getLangId()));
                }
            }

            if (mstMachine.getMainAssetNo() != null && !"".equals(mstMachine.getMainAssetNo().trim())) {
                mstMachineVo.setMainAssetNo(mstMachine.getMainAssetNo());
            } else {
                mstMachineVo.setMainAssetNo("");
            }
            if (null != mstMachine.getMachineCreatedDate()) {
                mstMachineVo.setMachineCreatedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getMachineCreatedDate()));
            }
            if (mstMachine.getInspectedDate() != null) {
                mstMachineVo.setInspectedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getInspectedDate()));
            }
            if (mstMachine.getOwnerMstCompany() != null) {
                mstMachineVo.setOwnerCompanyId(mstMachine.getOwnerMstCompany().getId());
                mstMachineVo.setOwnerCompanyName(mstMachine.getOwnerMstCompany().getCompanyName());
            } else {
                mstMachineVo.setOwnerCompanyId("");
                mstMachineVo.setOwnerCompanyName("");
            }
            if (mstMachine.getInstalledDate() != null) {
                mstMachineVo.setInstalledDateStr(new FileUtil().getDateFormatForStr(mstMachine.getInstalledDate()));
            }

            if (null != mstMachine.getMstCompany()) {
                mstMachineVo.setCompanyId(mstMachine.getMstCompany().getId());
                mstMachineVo.setCompanyName(mstMachine.getMstCompany().getCompanyName());
            } else {
                mstMachineVo.setCompanyId("");
                mstMachineVo.setCompanyName("");
            }

            if (null != mstMachine.getMstLocation()) {
                mstMachineVo.setLocationId(mstMachine.getMstLocation().getId());
                mstMachineVo.setLocationName(mstMachine.getMstLocation().getLocationName());
            } else {
                mstMachineVo.setLocationId("");
                mstMachineVo.setLocationName("");
            }

            if (null != mstMachine.getMstInstallationSite()) {
                mstMachineVo.setInstllationSiteId(mstMachine.getMstInstallationSite().getId());
                mstMachineVo.setInstllationSiteName(mstMachine.getMstInstallationSite().getInstallationSiteName());
            } else {
                mstMachineVo.setInstllationSiteId("");
                mstMachineVo.setInstllationSiteName("");
            }

            if (mstMachine.getStatus() != null) {
                mstMachineVo.setStatus(mstMachine.getStatus());
            }
            if (mstMachine.getStatusChangedDate() != null) {
                mstMachineVo.setStatusChangedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getStatusChangedDate()));
            }
            if (mstMachine.getDepartment() != null) {
                mstMachineVo.setDepartmentId("" + mstMachine.getDepartment());
                for (MstChoice mstChoice : mstDepartmentChoiceList.getMstChoice()) {
                    if (mstChoice.getMstChoicePK().getSeq().equals(mstMachineVo.getDepartmentId())) {
                        mstMachineVo.setDepartmentName(mstChoice.getChoice());
                        break;
                    }
                }
            }
            // 4.2 Zhangying S
            // 最終生産日
            if (mstMachine.getLastProductionDate() != null) {
                mstMachineVo.setLastProductionDate(mstMachine.getLastProductionDate());
                mstMachineVo.setLastProductionDateStr(fu.getDateFormatForStr(mstMachine.getLastProductionDate()));
            } else {
                mstMachineVo.setLastProductionDate(null);
                mstMachineVo.setLastProductionDateStr("");
            }
            // 累計生産時間
            if (mstMachine.getTotalProducingTimeHour() != null) {
                mstMachineVo.setTotalProducingTimeHour(mstMachine.getTotalProducingTimeHour());
            } else {
                mstMachineVo.setTotalProducingTimeHour(BigDecimal.ZERO);
            }
            // 累計ショット数
            if (mstMachine.getTotalShotCount() != null) {
                mstMachineVo.setTotalShotCount("" + mstMachine.getTotalShotCount());
            } else {
                mstMachineVo.setTotalShotCount("0");
            }
            // 最終メンテナンス日
            if (mstMachine.getLastMainteDate() != null) {
                mstMachineVo.setLastMainteDate(mstMachine.getLastMainteDate());
                mstMachineVo.setLastMainteDateStr(fu.getDateFormatForStr(mstMachine.getLastMainteDate()));
            } else {
                mstMachineVo.setLastMainteDate(null);
                mstMachineVo.setLastMainteDateStr("");
            }
            // メンテナンス後生産時間
            if (mstMachine.getAfterMainteTotalProducingTimeHour() != null) {
                mstMachineVo.setAfterMainteTotalProducingTimeHour(mstMachine.getAfterMainteTotalProducingTimeHour());
            } else {
                mstMachineVo.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
            }
            // メンテナンス後ショット数
            if (mstMachine.getAfterMainteTotalShotCount() != null) {
                mstMachineVo.setAfterMainteTotalShotCount(mstMachine.getAfterMainteTotalShotCount());
            } else {
                mstMachineVo.setAfterMainteTotalShotCount(0);
            }

            if (null != mstMachine.getBlMaintenanceCyclePtn01()) {
                // メンテサイクルコード01
                mstMachineVo.setMainteCycleCode01(mstMachine.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                mstMachineVo.setMainteCycleCode01("");
            }
            if (null != mstMachine.getBlMaintenanceCyclePtn02()) {
                // メンテサイクルコード02
                mstMachineVo.setMainteCycleCode02(mstMachine.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                mstMachineVo.setMainteCycleCode02("");
            }
            if (null != mstMachine.getBlMaintenanceCyclePtn03()) {
                // メンテサイクルコード03
                mstMachineVo.setMainteCycleCode03(mstMachine.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                mstMachineVo.setMainteCycleCode03("");
            }
            // 4.2 Zhangying E
            MstSigma mstSigma = null;
            if (null != (mstSigma = mstMachine.getMstSigma())) {
                mstMachineVo.setSigmaId(mstSigma.getId());
                mstMachineVo.setSigmaCode(mstSigma.getSigmaCode());
            }

            if (mstMachine.getMacKey() != null && !"".equals(mstMachine.getMacKey().trim())) {
                mstMachineVo.setMacKey(mstMachine.getMacKey());
            }
            if (null != mstMachine.getBaseCycleTime()) {
                mstMachineVo.setBaseCycleTime("" + mstMachine.getBaseCycleTime());
            }
            if (mstMachine.getMachineCd() != null && !"".equals(mstMachine.getMachineCd().trim())) {
                mstMachineVo.setMachineCd(mstMachine.getMachineCd());
            }
            if (mstMachine.getStrageLocationCd() != null && !"".equals(mstMachine.getStrageLocationCd().trim())) {
                mstMachineVo.setStrageLocationCd(mstMachine.getStrageLocationCd());
            }
            if (mstMachine.getChargeCd() != null && !"".equals(mstMachine.getChargeCd().trim())) {
                mstMachineVo.setChargeCd(mstMachine.getChargeCd());
            }
            if (mstMachine.getOperatorCd() != null && !"".equals(mstMachine.getOperatorCd().trim())) {
                mstMachineVo.setOperatorCd(mstMachine.getOperatorCd());
            }

            mstMachineVos.add(mstMachineVo);
        }
        mstMachineDetailList.setMstMachineVos(mstMachineVos);
        return mstMachineDetailList;

    }
    public MstMachineList getMstMachinesWithoutDispose(
            String machineId,
            String machineName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer machineType,
            Integer department,
            Date lastProductionDateFrom,
            Date lastProductionDateTo,
            Integer totalProducingTimeHourFrom,
            Integer totalProducingTimeHourTo,
            Integer totalShotCountFrom,
            Integer totalShotCountTo,
            Date lastMainteDateFrom,
            Date lastMainteDateTo,
            Integer afterMainteTotalProducingTimeHourFrom,
            Integer afterMainteTotalProducingTimeHourTo,
            Integer afterMainteTotalShotCountFrom,
            Integer afterMainteTotalShotCountTo,
            Date machineCreatedDateFrom,
            Date machineCreatedDateTo,
            Integer status,
            String orderByMachineName,
            LoginUser loginUser) {
        MstChoiceList mstDepartmentChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
        MstMachineList mstMachineDetailList = new MstMachineList();
        List<MstMachineVo> mstMachineVos = new ArrayList<>();
        List<MstMachine> list = getSqlWithoutDispose(
                machineId,
                machineName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                machineType,
                department,
                lastProductionDateFrom,
                lastProductionDateTo,
                totalProducingTimeHourFrom,
                totalProducingTimeHourTo,
                totalShotCountFrom,
                totalShotCountTo,
                lastMainteDateFrom,
                lastMainteDateTo,
                afterMainteTotalProducingTimeHourFrom,
                afterMainteTotalProducingTimeHourTo,
                afterMainteTotalShotCountFrom,
                afterMainteTotalShotCountTo,
                machineCreatedDateFrom,
                machineCreatedDateTo,
                status,
                orderByMachineName,
                "");
        FileUtil fu = new FileUtil();
        for (MstMachine mstMachine : list) {
            MstMachineVo mstMachineVo = new MstMachineVo();
            mstMachineVo.setMachineUuid(mstMachine.getUuid());
            mstMachineVo.setMachineId(mstMachine.getMachineId());
            mstMachineVo.setMachineName(mstMachine.getMachineName());

            if (mstMachine.getCompanyId() != null && !"".equals(mstMachine.getCompanyId())) {
                if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", mstMachine.getCompanyId(), loginUser).isError() == true) {
                    mstMachineVo.setMachineTypeText(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine.machine_type", String.valueOf(mstMachine.getMachineType()), loginUser.getLangId()));
                    mstMachineVo.setExternalFlg(1);
                } else if (mstMachine.getMachineType() != null) {
                    mstMachineVo.setExternalFlg(0);
                    mstMachineVo.setMachineType(mstMachine.getMachineType());
                }
            } else {
                mstMachineVo.setExternalFlg(0);
                if (mstMachine.getMachineType() != null) {
                    mstMachineVo.setMachineType(mstMachine.getMachineType());
                    //mstMachineVo.setMachineTypeText(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine.machine_type", String.valueOf(mstMachine.getMachineType()), loginUser.getLangId()));
                }
            }

            if (mstMachine.getMainAssetNo() != null && !"".equals(mstMachine.getMainAssetNo().trim())) {
                mstMachineVo.setMainAssetNo(mstMachine.getMainAssetNo());
            } else {
                mstMachineVo.setMainAssetNo("");
            }
            if (null != mstMachine.getMachineCreatedDate()) {
                mstMachineVo.setMachineCreatedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getMachineCreatedDate()));
            }
            if (mstMachine.getInspectedDate() != null) {
                mstMachineVo.setInspectedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getInspectedDate()));
            }
            if (mstMachine.getOwnerMstCompany() != null) {
                mstMachineVo.setOwnerCompanyId(mstMachine.getOwnerMstCompany().getId());
                mstMachineVo.setOwnerCompanyName(mstMachine.getOwnerMstCompany().getCompanyName());
            } else {
                mstMachineVo.setOwnerCompanyId("");
                mstMachineVo.setOwnerCompanyName("");
            }
            if (mstMachine.getInstalledDate() != null) {
                mstMachineVo.setInstalledDateStr(new FileUtil().getDateFormatForStr(mstMachine.getInstalledDate()));
            }

            if (null != mstMachine.getMstCompany()) {
                mstMachineVo.setCompanyId(mstMachine.getMstCompany().getId());
                mstMachineVo.setCompanyName(mstMachine.getMstCompany().getCompanyName());
            } else {
                mstMachineVo.setCompanyId("");
                mstMachineVo.setCompanyName("");
            }

            if (null != mstMachine.getMstLocation()) {
                mstMachineVo.setLocationId(mstMachine.getMstLocation().getId());
                mstMachineVo.setLocationName(mstMachine.getMstLocation().getLocationName());
            } else {
                mstMachineVo.setLocationId("");
                mstMachineVo.setLocationName("");
            }

            if (null != mstMachine.getMstInstallationSite()) {
                mstMachineVo.setInstllationSiteId(mstMachine.getMstInstallationSite().getId());
                mstMachineVo.setInstllationSiteName(mstMachine.getMstInstallationSite().getInstallationSiteName());
            } else {
                mstMachineVo.setInstllationSiteId("");
                mstMachineVo.setInstllationSiteName("");
            }

            if (mstMachine.getStatus() != null) {
                mstMachineVo.setStatus(mstMachine.getStatus());
            }
            if (mstMachine.getStatusChangedDate() != null) {
                mstMachineVo.setStatusChangedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getStatusChangedDate()));
            }
            if (mstMachine.getDepartment() != null) {
                mstMachineVo.setDepartmentId("" + mstMachine.getDepartment());
                for (MstChoice mstChoice : mstDepartmentChoiceList.getMstChoice()) {
                    if (mstChoice.getMstChoicePK().getSeq().equals(mstMachineVo.getDepartmentId())) {
                        mstMachineVo.setDepartmentName(mstChoice.getChoice());
                        break;
                    }
                }
            }
            // 4.2 Zhangying S
            // 最終生産日
            if (mstMachine.getLastProductionDate() != null) {
                mstMachineVo.setLastProductionDate(mstMachine.getLastProductionDate());
                mstMachineVo.setLastProductionDateStr(fu.getDateFormatForStr(mstMachine.getLastProductionDate()));
            } else {
                mstMachineVo.setLastProductionDate(null);
                mstMachineVo.setLastProductionDateStr("");
            }
            // 累計生産時間
            if (mstMachine.getTotalProducingTimeHour() != null) {
                mstMachineVo.setTotalProducingTimeHour(mstMachine.getTotalProducingTimeHour());
            } else {
                mstMachineVo.setTotalProducingTimeHour(BigDecimal.ZERO);
            }
            // 累計ショット数
            if (mstMachine.getTotalShotCount() != null) {
                mstMachineVo.setTotalShotCount("" + mstMachine.getTotalShotCount());
            } else {
                mstMachineVo.setTotalShotCount("0");
            }
            // 最終メンテナンス日
            if (mstMachine.getLastMainteDate() != null) {
                mstMachineVo.setLastMainteDate(mstMachine.getLastMainteDate());
                mstMachineVo.setLastMainteDateStr(fu.getDateFormatForStr(mstMachine.getLastMainteDate()));
            } else {
                mstMachineVo.setLastMainteDate(null);
                mstMachineVo.setLastMainteDateStr("");
            }
            // メンテナンス後生産時間
            if (mstMachine.getAfterMainteTotalProducingTimeHour() != null) {
                mstMachineVo.setAfterMainteTotalProducingTimeHour(mstMachine.getAfterMainteTotalProducingTimeHour());
            } else {
                mstMachineVo.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
            }
            // メンテナンス後ショット数
            if (mstMachine.getAfterMainteTotalShotCount() != null) {
                mstMachineVo.setAfterMainteTotalShotCount(mstMachine.getAfterMainteTotalShotCount());
            } else {
                mstMachineVo.setAfterMainteTotalShotCount(0);
            }

            if (null != mstMachine.getBlMaintenanceCyclePtn01()) {
                // メンテサイクルコード01
                mstMachineVo.setMainteCycleCode01(mstMachine.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                mstMachineVo.setMainteCycleCode01("");
            }
            if (null != mstMachine.getBlMaintenanceCyclePtn02()) {
                // メンテサイクルコード02
                mstMachineVo.setMainteCycleCode02(mstMachine.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                mstMachineVo.setMainteCycleCode02("");
            }
            if (null != mstMachine.getBlMaintenanceCyclePtn03()) {
                // メンテサイクルコード03
                mstMachineVo.setMainteCycleCode03(mstMachine.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                mstMachineVo.setMainteCycleCode03("");
            }
            // 4.2 Zhangying E
            MstSigma mstSigma = null;
            if (null != (mstSigma = mstMachine.getMstSigma())) {
                mstMachineVo.setSigmaId(mstSigma.getId());
                mstMachineVo.setSigmaCode(mstSigma.getSigmaCode());
            }

            if (mstMachine.getMacKey() != null && !"".equals(mstMachine.getMacKey().trim())) {
                mstMachineVo.setMacKey(mstMachine.getMacKey());
            }
            if (null != mstMachine.getBaseCycleTime()) {
                mstMachineVo.setBaseCycleTime("" + mstMachine.getBaseCycleTime());
            }
            if (mstMachine.getMachineCd() != null && !"".equals(mstMachine.getMachineCd().trim())) {
                mstMachineVo.setMachineCd(mstMachine.getMachineCd());
            }
            if (mstMachine.getStrageLocationCd() != null && !"".equals(mstMachine.getStrageLocationCd().trim())) {
                mstMachineVo.setStrageLocationCd(mstMachine.getStrageLocationCd());
            }
            if (mstMachine.getChargeCd() != null && !"".equals(mstMachine.getChargeCd().trim())) {
                mstMachineVo.setChargeCd(mstMachine.getChargeCd());
            }
            if (mstMachine.getOperatorCd() != null && !"".equals(mstMachine.getOperatorCd().trim())) {
                mstMachineVo.setOperatorCd(mstMachine.getOperatorCd());
            }

            mstMachineVos.add(mstMachineVo);
        }
        mstMachineDetailList.setMstMachineVos(mstMachineVos);
        return mstMachineDetailList;

    }
    /**
     * 設備マスタ詳細取得
     *
     * @param machineId
     * @param loginUser
     * @return
     */
    public MstMachineVo getMstMachineDetails(String machineId, LoginUser loginUser) {

        StringBuilder sql = new StringBuilder("SELECT m FROM MstMachine m "
                + " LEFT JOIN FETCH m.mstCreateUser mstCreateUser "
                + " LEFT JOIN FETCH m.mstUpdateUser mstUpdateUser "
                + " LEFT JOIN FETCH m.mstSigma WHERE 1=1 ");
        if (null != machineId && !machineId.isEmpty()) {
            sql.append(" and m.machineId = :machineId ");
        }
        MstMachineVo mstMachineVo = new MstMachineVo();

        Query query = entityManager.createQuery(sql.toString());
        if (null != machineId && !machineId.isEmpty()) {
            query.setParameter("machineId", machineId);
        }

        MstMachine machine;
        try {
            machine = (MstMachine) query.getSingleResult();
        } catch (NoResultException e) {
            mstMachineVo.setError(true);
            mstMachineVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstMachineVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return mstMachineVo;
        }

        FileUtil fu = new FileUtil();
        mstMachineVo.setMachineUuid(machine.getUuid());
        mstMachineVo.setMachineId(machine.getMachineId());
        mstMachineVo.setMachineName(machine.getMachineName());
        mstMachineVo.setMachineType(machine.getMachineType());
        mstMachineVo.setMainAssetNo(machine.getMainAssetNo());
        if (null != machine.getMachineCreatedDate()) {
            mstMachineVo.setMachineCreatedDate(machine.getMachineCreatedDate());
            mstMachineVo.setMachineCreatedDateStr(fu.getDateFormatForStr(machine.getMachineCreatedDate()));
        }
        if (null != machine.getInspectedDate()) {
            mstMachineVo.setInspectedDate(machine.getInspectedDate());
            mstMachineVo.setInspectedDateStr(fu.getDateFormatForStr(mstMachineVo.getInspectedDate()));
        }
        if (null != machine.getCreateDate()) {
            mstMachineVo.setCreatedDate(machine.getCreateDate());
            mstMachineVo.setCreatedDateStr(fu.getDateFormatForStr(machine.getCreateDate()));
        }

        if (null == machine.getOwnerMstCompany()) {
            mstMachineVo.setOwnerCompanyId("");
            mstMachineVo.setOwnerCompanyName("");
        } else {
            mstMachineVo.setOwnerCompanyId(machine.getOwnerMstCompany().getId());
            mstMachineVo.setOwnerCompanyName(machine.getOwnerMstCompany().getCompanyName());
        }

        if (null != machine.getInstalledDate()) {
            mstMachineVo.setInstalledDate(machine.getInstalledDate());
            mstMachineVo.setInstalledDateStr(fu.getDateFormatForStr(mstMachineVo.getInstalledDate()));
        }

        if (null != machine.getMstCompany()) {
            mstMachineVo.setCompanyId(machine.getMstCompany().getId());
            mstMachineVo.setCompanyName(machine.getMstCompany().getCompanyName());
        } else {
            mstMachineVo.setCompanyId("");
            mstMachineVo.setCompanyName("");
        }

        if (null != machine.getMstLocation()) {
            mstMachineVo.setLocationId(machine.getMstLocation().getId());
            mstMachineVo.setLocationName(machine.getMstLocation().getLocationName());
        } else {
            mstMachineVo.setLocationId("");
            mstMachineVo.setLocationName("");
        }

        if (null != machine.getMstInstallationSite()) {
            mstMachineVo.setInstllationSiteId(machine.getMstInstallationSite().getId());
            mstMachineVo.setInstllationSiteName(machine.getMstInstallationSite().getInstallationSiteName());
        } else {
            mstMachineVo.setInstllationSiteId("");
            mstMachineVo.setInstllationSiteName("");
        }

        mstMachineVo.setStatus(machine.getStatus());
        if (null != machine.getStatusChangedDate()) {
            mstMachineVo.setStatusChangedDate(machine.getStatusChangedDate());
            mstMachineVo.setStatusChangedDateStr(fu.getDateFormatForStr(machine.getStatusChangedDate()));
        }
        mstMachineVo.setMainteStatus(machine.getMainteStatus());
        if (machine.getMainteStatus() != null) {
            int mainteStatus = machine.getMainteStatus();
            MstChoiceList mainteStatusChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_machine.mainte_status");
            for (int momi = 0; momi < mainteStatusChoiceList.getMstChoice().size(); momi++) {
                MstChoice aMstChoice = mainteStatusChoiceList.getMstChoice().get(momi);
                if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mainteStatus))) {
                    mstMachineVo.setMainteStatusText(aMstChoice.getChoice());
                    break;
                } else {
                    mstMachineVo.setMainteStatusText("");
                }
            }
        } else {
            mstMachineVo.setMainteStatusText("");
        }
        mstMachineVo.setImgFilePath01(machine.getImgFilePath01());
        mstMachineVo.setImgFilePath02(machine.getImgFilePath02());
        mstMachineVo.setImgFilePath03(machine.getImgFilePath03());
        mstMachineVo.setImgFilePath04(machine.getImgFilePath04());
        mstMachineVo.setImgFilePath05(machine.getImgFilePath05());
        mstMachineVo.setImgFilePath06(machine.getImgFilePath06());
        mstMachineVo.setImgFilePath07(machine.getImgFilePath07());
        mstMachineVo.setImgFilePath08(machine.getImgFilePath08());
        mstMachineVo.setImgFilePath09(machine.getImgFilePath09());
        mstMachineVo.setImgFilePath10(machine.getImgFilePath10());

        if (null != machine.getReportFilePath01() && !machine.getReportFilePath01().trim().equals("")) {
            mstMachineVo.setReportFilePath01(machine.getReportFilePath01());

            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath01());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName01(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName01("");
            }
        } else {
            mstMachineVo.setReportFilePath01("");
            mstMachineVo.setReportFilePathName01("");
        }
        if (null != machine.getReportFilePath02() && !machine.getReportFilePath02().trim().equals("")) {
            mstMachineVo.setReportFilePath02(machine.getReportFilePath02());
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath02());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName02(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName02("");
            }
        } else {
            mstMachineVo.setReportFilePath02("");
            mstMachineVo.setReportFilePathName02("");
        }
        if (null != machine.getReportFilePath03() && !machine.getReportFilePath03().trim().equals("")) {
            mstMachineVo.setReportFilePath03(machine.getReportFilePath03());
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath03());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName03(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName03("");
            }
        } else {
            mstMachineVo.setReportFilePath03("");
            mstMachineVo.setReportFilePathName03("");
        }
        if (null != machine.getReportFilePath04() && !machine.getReportFilePath04().trim().equals("")) {
            mstMachineVo.setReportFilePath04(machine.getReportFilePath04());
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath04());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName04(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName04("");
            }
        } else {
            mstMachineVo.setReportFilePath04("");
            mstMachineVo.setReportFilePathName04("");
        }
        if (null != machine.getReportFilePath05() && !machine.getReportFilePath05().trim().equals("")) {
            mstMachineVo.setReportFilePath05(machine.getReportFilePath05());
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath05());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName05(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName05("");
            }
        } else {
            mstMachineVo.setReportFilePath05("");
            mstMachineVo.setReportFilePathName05("");
        }
        if (null != machine.getReportFilePath06() && !machine.getReportFilePath06().trim().equals("")) {
            mstMachineVo.setReportFilePath06(machine.getReportFilePath06());
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath06());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName06(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName06("");
            }
        } else {
            mstMachineVo.setReportFilePath06("");
            mstMachineVo.setReportFilePathName06("");
        }
        if (null != machine.getReportFilePath07() && !machine.getReportFilePath07().trim().equals("")) {
            mstMachineVo.setReportFilePath07(machine.getReportFilePath07());
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath07());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName07(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName07("");
            }
        } else {
            mstMachineVo.setReportFilePath07("");
            mstMachineVo.setReportFilePathName07("");
        }
        if (null != machine.getReportFilePath08() && !machine.getReportFilePath08().trim().equals("")) {
            mstMachineVo.setReportFilePath08(machine.getReportFilePath08());
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath08());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName08(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName08("");
            }
        } else {
            mstMachineVo.setReportFilePath08("");
            mstMachineVo.setReportFilePathName08("");
        }
        if (null != machine.getReportFilePath09() && !machine.getReportFilePath09().trim().equals("")) {
            mstMachineVo.setReportFilePath09(machine.getReportFilePath09());
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath09());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName09(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName09("");
            }
        } else {
            mstMachineVo.setReportFilePath09("");
            mstMachineVo.setReportFilePathName09("");
        }
        if (null != machine.getReportFilePath10() && !machine.getReportFilePath10().trim().equals("")) {
            mstMachineVo.setReportFilePath10(machine.getReportFilePath10());
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(machine.getReportFilePath10());
            if (null != tblUploadFile) {
                mstMachineVo.setReportFilePathName10(tblUploadFile.getUploadFileName());
            } else {
                mstMachineVo.setReportFilePathName10("");
            }
        } else {
            mstMachineVo.setReportFilePath10("");
            mstMachineVo.setReportFilePathName10("");
        }

        if (null != machine.getDepartment()) {
            MstChoiceList mstDepartmentChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
            mstMachineVo.setDepartmentId("" + machine.getDepartment());
            mstMachineVo.setDepartment(machine.getDepartment());

            for (MstChoice mstChoice : mstDepartmentChoiceList.getMstChoice()) {
                if (mstChoice.getMstChoicePK().getSeq().equals(mstMachineVo.getDepartmentId())) {
                    mstMachineVo.setDepartmentName(mstChoice.getChoice());
                    break;
                }
            }
        }
        MstSigma mstSigma = null;
        if (null != (mstSigma = machine.getMstSigma())) {
            mstMachineVo.setSigmaId(mstSigma.getId());
            mstMachineVo.setSigmaCode(mstSigma.getSigmaCode());
        }
        if (null != machine.getMacKey() && !"".equals(machine.getMacKey().trim())) {
            mstMachineVo.setMacKey(machine.getMacKey());
        }
        if (null != machine.getBaseCycleTime()) {
            mstMachineVo.setBaseCycleTime("" + machine.getBaseCycleTime());
        }
        mstMachineVo.setMachineCd(null == machine.getMachineCd() ? "" : machine.getMachineCd());
        mstMachineVo.setStrageLocationCd(null == machine.getStrageLocationCd() ? "" : machine.getStrageLocationCd());
        mstMachineVo.setChargeCd(null == machine.getChargeCd() ? "" : machine.getChargeCd());
        mstMachineVo.setOperatorCd(null == machine.getOperatorCd() ? "" : machine.getOperatorCd());

        // 4.2 対応追加 LYD S
        //最終生産日
        mstMachineVo.setLastProductionDate(machine.getLastProductionDate());
        mstMachineVo.setLastProductionDateStr(fu.getDateFormatForStr(machine.getLastProductionDate()));
        //累計生産時間
        mstMachineVo.setTotalProducingTimeHour(machine.getTotalProducingTimeHour());
        //累計ショット数
        if (machine.getTotalShotCount() != null) {
            mstMachineVo.setTotalShotCount(String.valueOf(machine.getTotalShotCount()));
        } else {
            mstMachineVo.setTotalShotCount("0");
        }

        //最終メンテナンス日
        mstMachineVo.setLastMainteDate(machine.getLastMainteDate());
        mstMachineVo.setLastMainteDateStr(fu.getDateFormatForStr(machine.getLastMainteDate()));
        //メンテナンス後生産時間
        mstMachineVo.setAfterMainteTotalProducingTimeHour(machine.getAfterMainteTotalProducingTimeHour());
        //メンテナンス後ショット数
        mstMachineVo.setAfterMainteTotalShotCount(machine.getAfterMainteTotalShotCount());
        //メンテサイクルコード01
        if (machine.getBlMaintenanceCyclePtn01() != null) {
            mstMachineVo.setMainteCycleId01(machine.getMainteCycleId01());
            mstMachineVo.setMainteCycleCode01(machine.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
        } else {
            mstMachineVo.setMainteCycleId01("");
            mstMachineVo.setMainteCycleCode01("");
        }
        //メンテサイクルコード02
        if (machine.getBlMaintenanceCyclePtn02() != null) {
            mstMachineVo.setMainteCycleId02(machine.getMainteCycleId02());
            mstMachineVo.setMainteCycleCode02(machine.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
        } else {
            mstMachineVo.setMainteCycleId02("");
            mstMachineVo.setMainteCycleCode02("");
        }
        //メンテサイクルコード03
        if (machine.getBlMaintenanceCyclePtn03() != null) {
            mstMachineVo.setMainteCycleId03(machine.getMainteCycleId03());
            mstMachineVo.setMainteCycleCode03(machine.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
        } else {
            mstMachineVo.setMainteCycleId03("");
            mstMachineVo.setMainteCycleCode03("");
        }
        // 4.2 対応追加 LYD E s

        if (machine.getCompanyId() != null && !"".equals(machine.getCompanyId())) {
            if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", machine.getCompanyId(), loginUser).isError()) {
                mstMachineVo.setExternalFlg(1);
                mstMachineVo.setMachineTypeText(extMstChoiceService.getExtMstChoiceText(machine.getCompanyId(), "mst_machine.machine_type", String.valueOf(machine.getMachineType()), loginUser.getLangId()));
            } else {
                mstMachineVo.setExternalFlg(0);
            }
        } else {
            mstMachineVo.setExternalFlg(0);
        }

        //  KM-147 登録日時、登録ユーザー名称、更新日時、更新ユーザーを表示 ADD START
        mstMachineVo.setCreatedDateStr(fu.getDateTimeFormatForStr(machine.getCreateDate()));
        mstMachineVo.setUpdateDate(machine.getUpdateDate());
        mstMachineVo.setUpdateDateStr(fu.getDateTimeFormatForStr(machine.getUpdateDate()));
        if (machine.getMstCreateUser() != null) {
            mstMachineVo.setCreateUserName(machine.getMstCreateUser().getUserName());
        }

        if (machine.getMstUpdateUser() != null) {
            mstMachineVo.setUpdateUserName(machine.getMstUpdateUser().getUserName());
        }
        //  KM-147 登録日時、登録ユーザー名称、更新日時、更新ユーザーを表示 ADD END

        return mstMachineVo;

    }

    private List getSql(
            String machineId,
            String machineName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer machineType,
            Integer department,
            Date lastProductionDateFrom,
            Date lastProductionDateTo,
            Integer totalProducingTimeHourFrom,
            Integer totalProducingTimeHourTo,
            Integer totalShotCountFrom,
            Integer totalShotCountTo,
            Date lastMainteDateFrom,
            Date lastMainteDateTo,
            Integer afterMainteTotalProducingTimeHourFrom,
            Integer afterMainteTotalProducingTimeHourTo,
            Integer afterMainteTotalShotCountFrom,
            Integer afterMainteTotalShotCountTo,
            Date machineCreatedDateFrom,
            Date machineCreatedDateTo,
            Integer status,
            String orderByMachineName,
            String action) {

        StringBuilder sql;

        if ("count".equals(action)) {
            sql = new StringBuilder("SELECT count(m.machineId) ");
        } else {
            sql = new StringBuilder("SELECT m ");
        }

        sql = sql.append(" FROM MstMachine m "
                + "LEFT JOIN FETCH m.ownerMstCompany oc "
                + "LEFT JOIN FETCH m.mstCompany c "
                + "LEFT JOIN FETCH m.mstInstallationSite isite "
                + "LEFT JOIN FETCH m.mstLocation location "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn01 cyclePtn01 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn02 cyclePtn02 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn03 cyclePtn03 "
                + "LEFT JOIN FETCH m.mstSigma s "
                + " WHERE 1=1 ");

        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" AND m.machineId like :machineId ");
        }
        if (machineName != null && !"".equals(machineName)) {
            sql = sql.append(" AND m.machineName like :machineName ");
        }

        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            sql = sql.append(" AND m.mainAssetNo LIKE :mainAssetNo ");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" and oc.companyName like :ownerCompanyName ");
        }

        if (companyName != null && !"".equals(companyName)) {
            sql = sql.append(" and c.companyName like :companyName ");
        }

        if (locationName != null && !"".equals(locationName)) {
            sql = sql.append(" and location.locationName like :locationName ");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            sql = sql.append(" and isite.installationSiteName like :instllationSiteName ");
        }

        if (machineType != null && 0 != machineType) {
            sql = sql.append(" and m.machineType = :machineType ");
        }

        if (department != null && 0 != department) {
            sql = sql.append(" and m.department = :department ");
        }

        if (lastProductionDateFrom != null) {
            sql = sql.append(" and m.lastProductionDate >= :lastProductionDateFrom ");
        }

        if (lastProductionDateTo != null) {
            sql = sql.append(" and m.lastProductionDate <= :lastProductionDateTo ");
        }

        if (totalProducingTimeHourFrom != null) {
            sql = sql.append(" and m.totalProducingTimeHour >= :totalProducingTimeHourFrom ");
        }

        if (totalProducingTimeHourTo != null) {
            sql = sql.append(" and m.totalProducingTimeHour <= :totalProducingTimeHourTo ");
        }

        if (totalShotCountFrom != null) {
            sql = sql.append(" and m.totalShotCount >= :totalShotCountFrom ");
        }

        if (totalShotCountTo != null) {
            sql = sql.append(" and m.totalShotCount <= :totalShotCountTo ");
        }

        if (lastMainteDateFrom != null) {
            sql = sql.append(" and m.lastMainteDate >= :lastMainteDateFrom ");
        }

        if (lastMainteDateTo != null) {
            sql = sql.append(" and m.lastMainteDate <= :lastMainteDateTo ");
        }

        if (afterMainteTotalProducingTimeHourFrom != null) {
            sql = sql.append(" and m.afterMainteTotalProducingTimeHour >= :afterMainteTotalProducingTimeHourFrom ");
        }

        if (afterMainteTotalProducingTimeHourTo != null) {
            sql = sql.append(" and m.afterMainteTotalProducingTimeHour <= :afterMainteTotalProducingTimeHourTo ");
        }

        if (afterMainteTotalShotCountFrom != null) {
            sql = sql.append(" and m.afterMainteTotalShotCount >= :afterMainteTotalShotCountFrom ");
        }

        if (afterMainteTotalShotCountTo != null) {
            sql = sql.append(" and m.afterMainteTotalShotCount <= :afterMainteTotalShotCountTo ");
        }

        if (machineCreatedDateFrom != null) {
            sql = sql.append(" and m.machineCreatedDate >= :machineCreatedDateFrom ");
        }

        if (machineCreatedDateTo != null) {
            sql = sql.append(" and m.machineCreatedDate <= :machineCreatedDateTo ");
        }

        if (status != null) {
            sql = sql.append(" and m.status = :status ");
        }

        if (!"count".equals(action)) {
            if (orderByMachineName != null && !"".equals(orderByMachineName)) {
                sql.append(" order by m.machineName ");
            } else {
                sql.append(" order by m.machineId ");//表示順は設備IDの昇順。
            }
        }

        Query query = entityManager.createQuery(sql.toString());

        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }

        if (machineName != null && !"".equals(machineName)) {
            query.setParameter("machineName", "%" + machineName + "%");
        }

        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            query.setParameter("mainAssetNo", "%" + mainAssetNo + "%");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            query.setParameter("ownerCompanyName", "%" + ownerCompanyName + "%");
        }

        if (companyName != null && !"".equals(companyName)) {
            query.setParameter("companyName", "%" + companyName + "%");
        }

        if (locationName != null && !"".equals(locationName)) {
            query.setParameter("locationName", "%" + locationName + "%");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            query.setParameter("instllationSiteName", "%" + instllationSiteName + "%");
        }

        if (machineType != null && 0 != machineType) {
            query.setParameter("machineType", machineType);
        }

        if (department != null && 0 != department) {
            query.setParameter("department", department);
        }

        if (lastProductionDateFrom != null) {
            query.setParameter("lastProductionDateFrom", lastProductionDateFrom);
        }
        if (lastProductionDateTo != null) {
            query.setParameter("lastProductionDateTo", lastProductionDateTo);
        }
        if (totalProducingTimeHourFrom != null) {
            query.setParameter("totalProducingTimeHourFrom", totalProducingTimeHourFrom);
        }
        if (totalProducingTimeHourTo != null) {
            query.setParameter("totalProducingTimeHourTo", totalProducingTimeHourTo);
        }
        if (totalShotCountFrom != null) {
            query.setParameter("totalShotCountFrom", totalShotCountFrom);
        }
        if (totalShotCountTo != null) {
            query.setParameter("totalShotCountTo", totalShotCountTo);
        }
        if (lastMainteDateFrom != null) {
            query.setParameter("lastMainteDateFrom", lastMainteDateFrom);
        }
        if (lastMainteDateTo != null) {
            query.setParameter("lastMainteDateTo", lastMainteDateTo);
        }
        if (afterMainteTotalProducingTimeHourFrom != null) {
            query.setParameter("afterMainteTotalProducingTimeHourFrom", afterMainteTotalProducingTimeHourFrom);
        }
        if (afterMainteTotalProducingTimeHourTo != null) {
            query.setParameter("afterMainteTotalProducingTimeHourTo", afterMainteTotalProducingTimeHourTo);
        }
        if (afterMainteTotalShotCountFrom != null) {
            query.setParameter("afterMainteTotalShotCountFrom", afterMainteTotalShotCountFrom);
        }
        if (afterMainteTotalShotCountTo != null) {
            query.setParameter("afterMainteTotalShotCountTo", afterMainteTotalShotCountTo);
        }

        if (machineCreatedDateFrom != null) {
            query.setParameter("machineCreatedDateFrom", machineCreatedDateFrom);
        }

        if (machineCreatedDateTo != null) {
            query.setParameter("machineCreatedDateTo", machineCreatedDateTo);
        }

        if (status != null) {
            query.setParameter("status", status);
        }

        List list = query.getResultList();

        return list;

    }

    private List getSqlWithoutDispose(
            String machineId,
            String machineName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer machineType,
            Integer department,
            Date lastProductionDateFrom,
            Date lastProductionDateTo,
            Integer totalProducingTimeHourFrom,
            Integer totalProducingTimeHourTo,
            Integer totalShotCountFrom,
            Integer totalShotCountTo,
            Date lastMainteDateFrom,
            Date lastMainteDateTo,
            Integer afterMainteTotalProducingTimeHourFrom,
            Integer afterMainteTotalProducingTimeHourTo,
            Integer afterMainteTotalShotCountFrom,
            Integer afterMainteTotalShotCountTo,
            Date machineCreatedDateFrom,
            Date machineCreatedDateTo,
            Integer status,
            String orderByMachineName,
            String action) {

        StringBuilder sql;

        if ("count".equals(action)) {
            sql = new StringBuilder("SELECT count(m.machineId) ");
        } else {
            sql = new StringBuilder("SELECT m ");
        }

        sql = sql.append(" FROM MstMachine m "
                + "LEFT JOIN FETCH m.ownerMstCompany oc "
                + "LEFT JOIN FETCH m.mstCompany c "
                + "LEFT JOIN FETCH m.mstInstallationSite isite "
                + "LEFT JOIN FETCH m.mstLocation location "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn01 cyclePtn01 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn02 cyclePtn02 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn03 cyclePtn03 "
                + "LEFT JOIN FETCH m.mstSigma s "
                + " WHERE 1=1 and m.status <> 9 ");

        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" AND m.machineId like :machineId ");
        }
        if (machineName != null && !"".equals(machineName)) {
            sql = sql.append(" AND m.machineName like :machineName ");
        }

        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            sql = sql.append(" AND m.mainAssetNo LIKE :mainAssetNo ");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" and oc.companyName like :ownerCompanyName ");
        }

        if (companyName != null && !"".equals(companyName)) {
            sql = sql.append(" and c.companyName like :companyName ");
        }

        if (locationName != null && !"".equals(locationName)) {
            sql = sql.append(" and location.locationName like :locationName ");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            sql = sql.append(" and isite.installationSiteName like :instllationSiteName ");
        }

        if (machineType != null && 0 != machineType) {
            sql = sql.append(" and m.machineType = :machineType ");
        }

        if (department != null && 0 != department) {
            sql = sql.append(" and m.department = :department ");
        }

        if (lastProductionDateFrom != null) {
            sql = sql.append(" and m.lastProductionDate >= :lastProductionDateFrom ");
        }

        if (lastProductionDateTo != null) {
            sql = sql.append(" and m.lastProductionDate <= :lastProductionDateTo ");
        }

        if (totalProducingTimeHourFrom != null) {
            sql = sql.append(" and m.totalProducingTimeHour >= :totalProducingTimeHourFrom ");
        }

        if (totalProducingTimeHourTo != null) {
            sql = sql.append(" and m.totalProducingTimeHour <= :totalProducingTimeHourTo ");
        }

        if (totalShotCountFrom != null) {
            sql = sql.append(" and m.totalShotCount >= :totalShotCountFrom ");
        }

        if (totalShotCountTo != null) {
            sql = sql.append(" and m.totalShotCount <= :totalShotCountTo ");
        }

        if (lastMainteDateFrom != null) {
            sql = sql.append(" and m.lastMainteDate >= :lastMainteDateFrom ");
        }

        if (lastMainteDateTo != null) {
            sql = sql.append(" and m.lastMainteDate <= :lastMainteDateTo ");
        }

        if (afterMainteTotalProducingTimeHourFrom != null) {
            sql = sql.append(" and m.afterMainteTotalProducingTimeHour >= :afterMainteTotalProducingTimeHourFrom ");
        }

        if (afterMainteTotalProducingTimeHourTo != null) {
            sql = sql.append(" and m.afterMainteTotalProducingTimeHour <= :afterMainteTotalProducingTimeHourTo ");
        }

        if (afterMainteTotalShotCountFrom != null) {
            sql = sql.append(" and m.afterMainteTotalShotCount >= :afterMainteTotalShotCountFrom ");
        }

        if (afterMainteTotalShotCountTo != null) {
            sql = sql.append(" and m.afterMainteTotalShotCount <= :afterMainteTotalShotCountTo ");
        }

        if (machineCreatedDateFrom != null) {
            sql = sql.append(" and m.machineCreatedDate >= :machineCreatedDateFrom ");
        }

        if (machineCreatedDateTo != null) {
            sql = sql.append(" and m.machineCreatedDate <= :machineCreatedDateTo ");
        }

        if (status != null) {
            sql = sql.append(" and m.status = :status ");
        }

        if (!"count".equals(action)) {
            if (orderByMachineName != null && !"".equals(orderByMachineName)) {
                sql.append(" order by m.machineName ");
            } else {
                sql.append(" order by m.machineId ");//表示順は設備IDの昇順。
            }
        }

        Query query = entityManager.createQuery(sql.toString());

        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }

        if (machineName != null && !"".equals(machineName)) {
            query.setParameter("machineName", "%" + machineName + "%");
        }

        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            query.setParameter("mainAssetNo", "%" + mainAssetNo + "%");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            query.setParameter("ownerCompanyName", "%" + ownerCompanyName + "%");
        }

        if (companyName != null && !"".equals(companyName)) {
            query.setParameter("companyName", "%" + companyName + "%");
        }

        if (locationName != null && !"".equals(locationName)) {
            query.setParameter("locationName", "%" + locationName + "%");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            query.setParameter("instllationSiteName", "%" + instllationSiteName + "%");
        }

        if (machineType != null && 0 != machineType) {
            query.setParameter("machineType", machineType);
        }

        if (department != null && 0 != department) {
            query.setParameter("department", department);
        }

        if (lastProductionDateFrom != null) {
            query.setParameter("lastProductionDateFrom", lastProductionDateFrom);
        }
        if (lastProductionDateTo != null) {
            query.setParameter("lastProductionDateTo", lastProductionDateTo);
        }
        if (totalProducingTimeHourFrom != null) {
            query.setParameter("totalProducingTimeHourFrom", totalProducingTimeHourFrom);
        }
        if (totalProducingTimeHourTo != null) {
            query.setParameter("totalProducingTimeHourTo", totalProducingTimeHourTo);
        }
        if (totalShotCountFrom != null) {
            query.setParameter("totalShotCountFrom", totalShotCountFrom);
        }
        if (totalShotCountTo != null) {
            query.setParameter("totalShotCountTo", totalShotCountTo);
        }
        if (lastMainteDateFrom != null) {
            query.setParameter("lastMainteDateFrom", lastMainteDateFrom);
        }
        if (lastMainteDateTo != null) {
            query.setParameter("lastMainteDateTo", lastMainteDateTo);
        }
        if (afterMainteTotalProducingTimeHourFrom != null) {
            query.setParameter("afterMainteTotalProducingTimeHourFrom", afterMainteTotalProducingTimeHourFrom);
        }
        if (afterMainteTotalProducingTimeHourTo != null) {
            query.setParameter("afterMainteTotalProducingTimeHourTo", afterMainteTotalProducingTimeHourTo);
        }
        if (afterMainteTotalShotCountFrom != null) {
            query.setParameter("afterMainteTotalShotCountFrom", afterMainteTotalShotCountFrom);
        }
        if (afterMainteTotalShotCountTo != null) {
            query.setParameter("afterMainteTotalShotCountTo", afterMainteTotalShotCountTo);
        }

        if (machineCreatedDateFrom != null) {
            query.setParameter("machineCreatedDateFrom", machineCreatedDateFrom);
        }

        if (machineCreatedDateTo != null) {
            query.setParameter("machineCreatedDateTo", machineCreatedDateTo);
        }

        if (status != null) {
            query.setParameter("status", status);
        }

        List list = query.getResultList();

        return list;

    }
    
    /**
     *
     * @param langId
     */
    public void outMachineTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outMachineTypeMapTemp == null) {
            outMachineTypeMapTemp = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_machine.machine_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                outMachineTypeMapTemp.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
        }

    }

    /**
     *
     * @param langId
     * @return
     */
    public Map inMachineTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inMachineTypeMapTemp == null) {
            inMachineTypeMapTemp = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_machine.machine_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMachineTypeMapTemp.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMachineTypeMapTemp;
    }

    /**
     *
     * @param langId
     */
    public void outStatusOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outStatusMapTemp == null) {
            outStatusMapTemp = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_machine.status");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                outStatusMapTemp.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
        }

    }

    /**
     *
     * @param langId
     * @return
     */
    public Map inStatusOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inStatusMapTemp == null) {
            inStatusMapTemp = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold.status");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inStatusMapTemp.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inStatusMapTemp;
    }

    /**
     *
     * @param langId
     * @return
     */
    public Map inDepartmentsOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inDepartmentsMapTemp == null) {
            inDepartmentsMapTemp = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_user.department");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inDepartmentsMapTemp.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inDepartmentsMapTemp;
    }

    /**
     * 設備削除用
     *
     * @param machineId
     * @return
     */
    @Transactional
    public int deleteMstMachine(String machineId) {
        StringBuilder sql;
        sql = new StringBuilder("DELETE FROM MstMachine m WHERE m.machineId = :machineId");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("machineId", machineId);
        return query.executeUpdate();
    }

    /**
     * 設備存在チェック用
     *
     * @param machineId
     * @return
     */
    public boolean getMstMachineExistCheck(String machineId) {
        if (new FileUtil().isNullCheck(machineId) == false) {
            Query query = entityManager.createNamedQuery("MstMachine.findByMachineId");
            query.setParameter("machineId", machineId);
            try {
                query.getSingleResult();
            } catch (NoResultException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * macKey唯一性チェック
     *
     * @param macKey
     * @return
     */
    public boolean getMstMachineMacKeyExistCheck(String macKey) {

        boolean isExist = false;
        if (StringUtils.isNotEmpty(macKey)) {
            Query query = entityManager.createQuery("SELECT m FROM MstMachine m WHERE m.macKey = :macKey ");
            query.setParameter("macKey", macKey);
            List list = query.getResultList();
            if (null != list && list.size() > 0) {
                isExist = true;
            }
        }
        return isExist;
    }

    /**
     * 設備M0013 csv取り込み使用
     *
     * @param mstMachine
     * @return
     */
    @Transactional
    public int updateMstMachineByQuery(MstMachine mstMachine) {
        StringBuilder sql = new StringBuilder(" UPDATE MstMachine m SET ");
        sql.append("m.machineName = :machineName,");
        sql.append("m.machineType = :machineType,");
        sql.append("m.mainAssetNo = :mainAssetNo,");
        sql.append("m.machineCreatedDate = :machineCreatedDate,");
        sql.append("m.inspectedDate = :inspectedDate,");
        sql.append("m.ownerCompanyId = :ownerCompanyId,");
        sql.append("m.installedDate = :installedDate,");
        sql.append("m.companyId = :companyId,");
        sql.append("m.locationId = :locationId,");
        sql.append("m.instllationSiteId = :instllationSiteId,");
        sql.append("m.companyName = :companyName,");
        sql.append("m.locationName = :locationName,");
        sql.append("m.instllationSiteName = :instllationSiteName,");
        sql.append("m.status = :status,");
        sql.append("m.statusChangedDate = :statusChangedDate,");
        sql.append("m.imgFilePath01 = :imgFilePath01,");
        sql.append("m.imgFilePath02 = :imgFilePath02,");
        sql.append("m.imgFilePath03 = :imgFilePath03,");
        sql.append("m.imgFilePath04 = :imgFilePath04,");
        sql.append("m.imgFilePath05 = :imgFilePath05,");
        sql.append("m.imgFilePath06 = :imgFilePath06,");
        sql.append("m.imgFilePath07 = :imgFilePath07,");
        sql.append("m.imgFilePath08 = :imgFilePath08,");
        sql.append("m.imgFilePath09 = :imgFilePath09,");
        sql.append("m.imgFilePath10 = :imgFilePath10, ");
        sql.append("m.department = :department, ");

        sql.append("m.lastProductionDate = :lastProductionDate, ");
        sql.append("m.totalProducingTimeHour = :totalProducingTimeHour, ");
        sql.append("m.totalShotCount = :totalShotCount, ");
        sql.append("m.lastMainteDate = :lastMainteDate, ");
        sql.append("m.afterMainteTotalShotCount = :afterMainteTotalShotCount, ");
        sql.append("m.afterMainteTotalProducingTimeHour = :afterMainteTotalProducingTimeHour, ");
        sql.append("m.mainteCycleId01 = :mainteCycleId01, ");
        sql.append("m.mainteCycleId02 = :mainteCycleId02, ");
        sql.append("m.mainteCycleId03 = :mainteCycleId03, ");

        sql.append("m.macKey = :macKey, ");
        sql.append("m.baseCycleTime = :baseCycleTime, ");
        sql.append("m.sigmaId = :sigmaId, ");
        sql.append("m.machineCd = :machineCd, ");
        sql.append("m.strageLocationCd = :strageLocationCd, ");
        sql.append("m.chargeCd = :chargeCd, ");
        sql.append("m.operatorCd = :operatorCd, ");
        sql.append("m.updateDate = :updateDate,");
        sql.append("m.updateUserUuid = :updateUserUuid ");
        sql.append("WHERE m.machineId = :machineId");

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("machineName", mstMachine.getMachineName());
        query.setParameter("machineType", mstMachine.getMachineType());
        query.setParameter("mainAssetNo", mstMachine.getMainAssetNo());
        query.setParameter("machineCreatedDate", mstMachine.getMachineCreatedDate());
        query.setParameter("inspectedDate", mstMachine.getInspectedDate());
        query.setParameter("ownerCompanyId", mstMachine.getOwnerCompanyId());
        query.setParameter("installedDate", mstMachine.getInstalledDate());
        query.setParameter("companyId", mstMachine.getCompanyId());
        query.setParameter("locationId", mstMachine.getLocationId());
        query.setParameter("instllationSiteId", mstMachine.getInstllationSiteId());
        query.setParameter("companyName", mstMachine.getCompanyName());
        query.setParameter("locationName", mstMachine.getLocationName());
        query.setParameter("instllationSiteName", mstMachine.getInstllationSiteName());
        query.setParameter("status", mstMachine.getStatus());
        query.setParameter("statusChangedDate", mstMachine.getStatusChangedDate());
        query.setParameter("imgFilePath01", mstMachine.getImgFilePath01());
        query.setParameter("imgFilePath02", mstMachine.getImgFilePath02());
        query.setParameter("imgFilePath03", mstMachine.getImgFilePath03());
        query.setParameter("imgFilePath04", mstMachine.getImgFilePath04());
        query.setParameter("imgFilePath05", mstMachine.getImgFilePath05());
        query.setParameter("imgFilePath06", mstMachine.getImgFilePath06());
        query.setParameter("imgFilePath07", mstMachine.getImgFilePath07());
        query.setParameter("imgFilePath08", mstMachine.getImgFilePath08());
        query.setParameter("imgFilePath09", mstMachine.getImgFilePath09());
        query.setParameter("imgFilePath10", mstMachine.getImgFilePath10());

        query.setParameter("department", mstMachine.getDepartment());

        query.setParameter("lastProductionDate", mstMachine.getLastProductionDate());
        query.setParameter("totalProducingTimeHour", mstMachine.getTotalProducingTimeHour());
        query.setParameter("totalShotCount", mstMachine.getTotalShotCount());
        query.setParameter("lastMainteDate", mstMachine.getLastMainteDate());
        query.setParameter("afterMainteTotalShotCount", mstMachine.getAfterMainteTotalShotCount());
        query.setParameter("afterMainteTotalProducingTimeHour", mstMachine.getAfterMainteTotalProducingTimeHour());
        query.setParameter("mainteCycleId01", mstMachine.getMainteCycleId01());
        query.setParameter("mainteCycleId02", mstMachine.getMainteCycleId02());
        query.setParameter("mainteCycleId03", mstMachine.getMainteCycleId03());

        query.setParameter("macKey", mstMachine.getMacKey());
        query.setParameter("baseCycleTime", mstMachine.getBaseCycleTime());
        query.setParameter("sigmaId", mstMachine.getSigmaId());
        query.setParameter("machineCd", mstMachine.getMachineCd());
        query.setParameter("strageLocationCd", mstMachine.getStrageLocationCd());
        query.setParameter("chargeCd", mstMachine.getChargeCd());
        query.setParameter("operatorCd", mstMachine.getOperatorCd());

        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", mstMachine.getUpdateUserUuid());
        query.setParameter("machineId", mstMachine.getMachineId());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * 設備M0013 csv取り込み使用 （外部）
     *
     * @param mstMachine
     * @return
     */
    @Transactional
    public int updateExtMachineByQuery(MstMachine mstMachine) {
        StringBuilder sql = new StringBuilder(" UPDATE MstMachine m SET ");
        sql.append("m.mainAssetNo = :mainAssetNo,");
        sql.append("m.macKey = :macKey, ");
        sql.append("m.sigmaId = :sigmaId, ");
        sql.append("m.machineCd = :machineCd, ");
        sql.append("m.strageLocationCd = :strageLocationCd, ");
        sql.append("m.chargeCd = :chargeCd, ");
        sql.append("m.operatorCd = :operatorCd, ");
        sql.append("m.updateDate = :updateDate,");
        sql.append("m.mainteCycleId01 = :mainteCycleId01, ");
        sql.append("m.mainteCycleId02 = :mainteCycleId02, ");
        sql.append("m.mainteCycleId03 = :mainteCycleId03, ");
        sql.append("m.updateUserUuid = :updateUserUuid ");
        sql.append("WHERE m.machineId = :machineId");

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("mainAssetNo", mstMachine.getMainAssetNo());
        query.setParameter("macKey", mstMachine.getMacKey());
        query.setParameter("sigmaId", mstMachine.getSigmaId());
        query.setParameter("machineCd", mstMachine.getMachineCd());
        query.setParameter("strageLocationCd", mstMachine.getStrageLocationCd());
        query.setParameter("chargeCd", mstMachine.getChargeCd());
        query.setParameter("operatorCd", mstMachine.getOperatorCd());

        query.setParameter("updateDate", new Date());
        query.setParameter("mainteCycleId01", mstMachine.getMainteCycleId01());
        query.setParameter("mainteCycleId02", mstMachine.getMainteCycleId02());
        query.setParameter("mainteCycleId03", mstMachine.getMainteCycleId03());
        query.setParameter("updateUserUuid", mstMachine.getUpdateUserUuid());
        query.setParameter("machineId", mstMachine.getMachineId());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * 設備移動　T0003　使用
     *
     * @param mstMachine
     * @return
     */
    @Transactional
    public int updateByMachineIdForLocationHistory(MstMachine mstMachine) {
        Query query = entityManager.createNamedQuery("MstMachine.updateByMachineIdForLocationHistory");
        query.setParameter("installedDate", mstMachine.getInstalledDate());
        query.setParameter("companyId", mstMachine.getCompanyId());
        query.setParameter("locationId", mstMachine.getLocationId());
        query.setParameter("instllationSiteId", mstMachine.getInstllationSiteId());
        query.setParameter("companyName", mstMachine.getCompanyName());
        query.setParameter("locationName", mstMachine.getLocationName());
        query.setParameter("instllationSiteName", mstMachine.getInstllationSiteName());
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", mstMachine.getUpdateUserUuid());
        query.setParameter("machineId", mstMachine.getMachineId());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * 設備マスタ追加
     *
     * @param mstMachineVo
     * @param loginUser
     */
    @Transactional
    public void createMstMachineByMstMachineDetail(MstMachineVo mstMachineVo, LoginUser loginUser) {
        //設備マスタ追加
        String uuid = IDGenerator.generate();
        MstMachine mstMachine = new MstMachine();
        MstMachine inputMstMachine = mstMachineVo.getMstMachine();
        mstMachine.setUuid(uuid);
        mstMachine.setMachineId(inputMstMachine.getMachineId());
        mstMachine.setMachineName(inputMstMachine.getMachineName());
        mstMachine.setMachineType(inputMstMachine.getMachineType());
        mstMachine.setMachineCreatedDate(inputMstMachine.getMachineCreatedDate());
        mstMachine.setInspectedDate(inputMstMachine.getInspectedDate());
        mstMachine.setMainteStatus(0);

        if (inputMstMachine.getOwnerCompanyId() != null && !"".equals(inputMstMachine.getOwnerCompanyId())) {
            mstMachine.setOwnerCompanyId(inputMstMachine.getOwnerCompanyId());
        }

        if (inputMstMachine.getCompanyId() != null && !"".equals(inputMstMachine.getCompanyId())) {
            mstMachine.setCompanyId(inputMstMachine.getCompanyId());
            mstMachine.setCompanyName(inputMstMachine.getCompanyName());
        }
        if (inputMstMachine.getLocationId() != null && !"".equals(inputMstMachine.getLocationId())) {
            mstMachine.setLocationId(inputMstMachine.getLocationId());
            mstMachine.setLocationName(inputMstMachine.getLocationName());
        }
        if (inputMstMachine.getInstllationSiteId() != null && !"".equals(inputMstMachine.getInstllationSiteId())) {
            mstMachine.setInstllationSiteId(inputMstMachine.getInstllationSiteId());
            mstMachine.setInstllationSiteName(inputMstMachine.getInstllationSiteName());
        }
        if (null != inputMstMachine.getCompanyId() || null != inputMstMachine.getLocationId() || null != inputMstMachine.getInstllationSiteId()) {
            mstMachine.setInstalledDate(new Date());
        }
        mstMachine.setStatus(inputMstMachine.getStatus());
        mstMachine.setStatusChangedDate(new Date());

        String imgFilePath01 = inputMstMachine.getImgFilePath01();
        if (imgFilePath01 != null && !"".equals(imgFilePath01.trim())) {
            mstMachine.setImgFilePath01(imgFilePath01.trim());
        }

        String imgFilePath02 = inputMstMachine.getImgFilePath02();
        if (imgFilePath02 != null && !"".equals(imgFilePath02.trim())) {
            mstMachine.setImgFilePath02(imgFilePath02.trim());
        }

        String imgFilePath03 = inputMstMachine.getImgFilePath03();
        if (imgFilePath03 != null && !"".equals(imgFilePath03.trim())) {
            mstMachine.setImgFilePath03(imgFilePath03.trim());
        }

        String imgFilePath04 = inputMstMachine.getImgFilePath04();
        if (imgFilePath04 != null && !"".equals(imgFilePath04.trim())) {
            mstMachine.setImgFilePath04(imgFilePath04.trim());
        }

        String imgFilePath05 = inputMstMachine.getImgFilePath05();
        if (imgFilePath05 != null && !"".equals(imgFilePath05.trim())) {
            mstMachine.setImgFilePath05(imgFilePath05.trim());
        }

        String imgFilePath06 = inputMstMachine.getImgFilePath06();
        if (imgFilePath06 != null && !"".equals(imgFilePath06.trim())) {
            mstMachine.setImgFilePath06(imgFilePath06.trim());
        }

        String imgFilePath07 = inputMstMachine.getImgFilePath07();
        if (imgFilePath07 != null && !"".equals(imgFilePath07.trim())) {
            mstMachine.setImgFilePath07(imgFilePath07.trim());
        }

        String imgFilePath08 = inputMstMachine.getImgFilePath08();
        if (imgFilePath08 != null && !"".equals(imgFilePath08.trim())) {
            mstMachine.setImgFilePath08(imgFilePath08.trim());
        }

        String imgFilePath09 = inputMstMachine.getImgFilePath09();
        if (imgFilePath09 != null && !"".equals(imgFilePath09.trim())) {
            mstMachine.setImgFilePath09(imgFilePath09.trim());
        }

        String imgFilePath10 = inputMstMachine.getImgFilePath10();
        if (imgFilePath10 != null && !"".equals(imgFilePath10.trim())) {
            mstMachine.setImgFilePath10(imgFilePath10.trim());
        }

        if (inputMstMachine.getReportFilePath01() != null && !"".equals(inputMstMachine.getReportFilePath01().trim())) {
            mstMachine.setReportFilePath01(inputMstMachine.getReportFilePath01());
        }
        if (inputMstMachine.getReportFilePath02() != null && !"".equals(inputMstMachine.getReportFilePath02().trim())) {
            mstMachine.setReportFilePath02(inputMstMachine.getReportFilePath02());
        }
        if (inputMstMachine.getReportFilePath03() != null && !"".equals(inputMstMachine.getReportFilePath03().trim())) {
            mstMachine.setReportFilePath03(inputMstMachine.getReportFilePath03());
        }
        if (inputMstMachine.getReportFilePath04() != null && !"".equals(inputMstMachine.getReportFilePath04().trim())) {
            mstMachine.setReportFilePath04(inputMstMachine.getReportFilePath04());
        }
        if (inputMstMachine.getReportFilePath05() != null && !"".equals(inputMstMachine.getReportFilePath05().trim())) {
            mstMachine.setReportFilePath05(inputMstMachine.getReportFilePath05());
        }
        if (inputMstMachine.getReportFilePath06() != null && !"".equals(inputMstMachine.getReportFilePath06().trim())) {
            mstMachine.setReportFilePath06(inputMstMachine.getReportFilePath06());
        }
        if (inputMstMachine.getReportFilePath07() != null && !"".equals(inputMstMachine.getReportFilePath07().trim())) {
            mstMachine.setReportFilePath07(inputMstMachine.getReportFilePath07());
        }
        if (inputMstMachine.getReportFilePath08() != null && !"".equals(inputMstMachine.getReportFilePath08().trim())) {
            mstMachine.setReportFilePath08(inputMstMachine.getReportFilePath08());
        }
        if (inputMstMachine.getReportFilePath09() != null && !"".equals(inputMstMachine.getReportFilePath09().trim())) {
            mstMachine.setReportFilePath09(inputMstMachine.getReportFilePath09());
        }
        if (inputMstMachine.getReportFilePath10() != null && !"".equals(inputMstMachine.getReportFilePath10().trim())) {
            mstMachine.setReportFilePath10(inputMstMachine.getReportFilePath10());
        }

        if (null != inputMstMachine.getDepartment()) {
            mstMachine.setDepartment(inputMstMachine.getDepartment());
        } else {
            mstMachine.setDepartment(0);
        }
        if (null != inputMstMachine.getSigmaId() && !"".equals(inputMstMachine.getSigmaId().trim())) {
            mstMachine.setSigmaId(inputMstMachine.getSigmaId());
        } else {
            mstMachine.setSigmaId(null);
        }
        if (null != inputMstMachine.getMacKey() && !"".equals(inputMstMachine.getMacKey().trim())) {
            mstMachine.setMacKey(inputMstMachine.getMacKey());
        }
        if (null != inputMstMachine.getBaseCycleTime()) {
            mstMachine.setBaseCycleTime(inputMstMachine.getBaseCycleTime());
        } else {
            mstMachine.setBaseCycleTime(BigDecimal.ZERO);
        }
        if (null != inputMstMachine.getMachineCd() && !"".equals(inputMstMachine.getMachineCd().trim())) {
            mstMachine.setMachineCd(inputMstMachine.getMachineCd());
        }
        if (null != inputMstMachine.getStrageLocationCd() && !"".equals(inputMstMachine.getStrageLocationCd().trim())) {
            mstMachine.setStrageLocationCd(inputMstMachine.getStrageLocationCd());
        }
        if (null != inputMstMachine.getChargeCd() && !"".equals(inputMstMachine.getChargeCd().trim())) {
            mstMachine.setChargeCd(inputMstMachine.getChargeCd());
        }
        if (null != inputMstMachine.getOperatorCd() && !"".equals(inputMstMachine.getOperatorCd().trim())) {
            mstMachine.setOperatorCd(inputMstMachine.getOperatorCd());
        }

        //4.2 対応 S
        mstMachine.setLastProductionDate(inputMstMachine.getLastProductionDate());
        if (null != inputMstMachine.getTotalProducingTimeHour()) {
            mstMachine.setTotalProducingTimeHour(inputMstMachine.getTotalProducingTimeHour());
        } else {
            mstMachine.setTotalProducingTimeHour(BigDecimal.ZERO);
        }
        if (null != inputMstMachine.getTotalShotCount()) {
            mstMachine.setTotalShotCount(inputMstMachine.getTotalShotCount());
        } else {
            mstMachine.setTotalShotCount(0);
        }
        mstMachine.setLastMainteDate(inputMstMachine.getLastMainteDate());
        if (null != inputMstMachine.getAfterMainteTotalProducingTimeHour()) {
            mstMachine.setAfterMainteTotalProducingTimeHour(inputMstMachine.getAfterMainteTotalProducingTimeHour());
        } else {
            mstMachine.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
        }
        if (null != inputMstMachine.getAfterMainteTotalShotCount()) {
            mstMachine.setAfterMainteTotalShotCount(inputMstMachine.getAfterMainteTotalShotCount());
        } else {
            mstMachine.setAfterMainteTotalShotCount(0);
        }
        if (!StringUtils.isEmpty(inputMstMachine.getMainteCycleId01())) {
            mstMachine.setMainteCycleId01(inputMstMachine.getMainteCycleId01());
        } else {
            mstMachine.setMainteCycleId01(null);
        }
        if (!StringUtils.isEmpty(inputMstMachine.getMainteCycleId02())) {
            mstMachine.setMainteCycleId02(inputMstMachine.getMainteCycleId02());
        } else {
            mstMachine.setMainteCycleId02(null);
        }
        if (!StringUtils.isEmpty(inputMstMachine.getMainteCycleId03())) {
            mstMachine.setMainteCycleId03(inputMstMachine.getMainteCycleId03());
        } else {
            mstMachine.setMainteCycleId03(null);
        }

        //4.2 対応 E
        mstMachine.setCreateDate(new java.util.Date());
        mstMachine.setCreateUserUuid(loginUser.getUserUuid());
        mstMachine.setUpdateDate(new java.util.Date());
        mstMachine.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(mstMachine);

        //代表資産番号を登録
        if (inputMstMachine.getMainAssetNo() != null && !inputMstMachine.getMainAssetNo().trim().equals("")) {
            mstMachine.setMainAssetNo(inputMstMachine.getMainAssetNo());
            MstMachineAssetNo mstMachineAssetNo = new MstMachineAssetNo();
            MstMachineAssetNoPK mstMachineAssetNoPK = new MstMachineAssetNoPK();
            mstMachineAssetNoPK.setMachineUuid(uuid);
            mstMachineAssetNoPK.setAssetNo(inputMstMachine.getMainAssetNo());
            mstMachineAssetNo.setId(IDGenerator.generate());
            mstMachineAssetNo.setMainFlg(1);
            mstMachineAssetNo.setAssetAmount(BigDecimal.ZERO);
            mstMachineAssetNo.setNumberedDate(new Date());
            mstMachineAssetNo.setCreateDate(new Date());
            mstMachineAssetNo.setCreateUserUuid(loginUser.getUserUuid());
            mstMachineAssetNo.setMstMachineAssetNoPK(mstMachineAssetNoPK);
            entityManager.persist(mstMachineAssetNo);
        }

        MstMachineSpecHistory mstMachineSpecHistory = null;
        //設備仕様マスタ追加
        if (mstMachineVo.getMstMachineSpec() != null && mstMachineVo.getMstMachineSpec().size() > 0) {
            mstMachineSpecHistory = new MstMachineSpecHistory();
            mstMachineSpecHistory.setId(IDGenerator.generate());
            mstMachineSpecHistory.setCreateDate(new Date());
            mstMachineSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
            mstMachineSpecHistory.setStartDate(new Date());
            mstMachineSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付
            mstMachineSpecHistory.setMachineUuid(mstMachine.getUuid());
            //設備仕様履歴名称を「最初のバージョン」(文言キー:machine_spec_first_version）
            mstMachineSpecHistory.setMachineSpecName(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_spec_first_version"));
            entityManager.persist(mstMachineSpecHistory);
        }

        //設備加工条件マスタ追加
        Map machineProcCondsMap = new HashMap();
        if (mstMachineVo.getMstMachineProcCondVos() != null && mstMachineVo.getMstMachineProcCondVos().size() > 0) {
            for (int mpFlag = 0; mpFlag < mstMachineVo.getMstMachineProcCondVos().size(); mpFlag++) {
                MstMachineProcCondVo aMstMachineProcCondVo = mstMachineVo.getMstMachineProcCondVos().get(mpFlag);
                if ("1".equals(aMstMachineProcCondVo.getOperationFlag())) {
                    //削除の記し
                    String id = aMstMachineProcCondVo.getId();
                    if (null != id) {
                        mstMachineProcCondService.deleteProcCondNameById(id);
                    }
                    continue;
                }
                MstMachineProcCond machineProcCond = new MstMachineProcCond();
                machineProcCond.setId(IDGenerator.generate());
                if (null != aMstMachineProcCondVo.getComponentId() && !aMstMachineProcCondVo.getComponentId().equals("")) {
                    machineProcCond.setComponentId(aMstMachineProcCondVo.getComponentId());
                } else {
                    machineProcCond.setComponentId(null);
                }
                machineProcCond.setMachineUuid(mstMachine.getUuid());
                machineProcCond.setCreateDate(new Date());
                machineProcCond.setCreateUserUuid(loginUser.getUserUuid());
                machineProcCond.setSeq(mpFlag);
                machineProcCond.setUpdateDate(new Date());
                machineProcCond.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(machineProcCond);

                machineProcCondsMap.put(aMstMachineProcCondVo.getComponentId(), machineProcCond);
            }
        }

        if (!machineProcCondsMap.isEmpty() && mstMachineVo.getMstMachineProcCondSpecVos() != null && mstMachineVo.getMstMachineProcCondSpecVos().size() > 0) {
            MstMachineProcCond mstMachineProcCond;
            for (int i = 0; i < mstMachineVo.getMstMachineProcCondSpecVos().size(); i++) {
                MstMachineProcCondSpecPK pk = new MstMachineProcCondSpecPK();
                MstMachineProcCondSpecVo machineProcCondSpecVo = mstMachineVo.getMstMachineProcCondSpecVos().get(i);
                MstMachineProcCondSpec machineProcCondSpec = new MstMachineProcCondSpec();
                mstMachineProcCond = (MstMachineProcCond) machineProcCondsMap.get(null == machineProcCondSpecVo.getComponentId() ? "" : machineProcCondSpecVo.getComponentId());
                pk.setMachineProcCondId(mstMachineProcCond.getId());
                pk.setAttrId(machineProcCondSpecVo.getMstMachineProcCondSpecPK().getAttrId());

                machineProcCondSpec.setId(IDGenerator.generate());
                machineProcCondSpec.setCreateDate(new Date());
                machineProcCondSpec.setCreateUserUuid(loginUser.getUserUuid());
                machineProcCondSpec.setUpdateDate(new Date());
                machineProcCondSpec.setUpdateUserUuid(loginUser.getUserUuid());
                machineProcCondSpec.setMstMachineProcCondSpecPK(pk);
                machineProcCondSpec.setAttrValue(machineProcCondSpecVo.getAttrValue());
                entityManager.persist(machineProcCondSpec);
            }
        }

        //設備所在履歴マスタ編集
        if (null != mstMachine.getInstllationSiteId()
                || null != mstMachine.getLocationId()
                || null != mstMachine.getCompanyId()) {
            TblMachineLocationHistory tblMachineLocationHistory = new TblMachineLocationHistory();
            tblMachineLocationHistory.setId(IDGenerator.generate());
            tblMachineLocationHistory.setMachineUuid(mstMachine.getUuid());
            tblMachineLocationHistory.setChangeReason(0); //変更理由：なし（ゼロ）
            tblMachineLocationHistory.setCompanyId(null == mstMachine.getCompanyId() ? null : mstMachine.getCompanyId());
            tblMachineLocationHistory.setCompanyName(null == mstMachine.getCompanyName() ? null : mstMachine.getCompanyName());

            tblMachineLocationHistory.setLocationId(null == mstMachine.getLocationId() ? mstMachine.getLocationId() : mstMachine.getLocationId());
            tblMachineLocationHistory.setLocationName(null == mstMachine.getLocationName() ? mstMachine.getLocationName() : mstMachine.getLocationName());

            tblMachineLocationHistory.setInstllationSiteId(null == mstMachine.getInstllationSiteId() ? null : mstMachine.getInstllationSiteId());
            tblMachineLocationHistory.setInstllationSiteName(null == mstMachine.getInstllationSiteName() ? null : mstMachine.getInstllationSiteName());
            tblMachineLocationHistory.setUpdateDate(new Date());
            tblMachineLocationHistory.setUpdateUserUuid(loginUser.getUserUuid());
            tblMachineLocationHistory.setCreateDate(new Date());
            tblMachineLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
            tblMachineLocationHistory.setStartDate(null == mstMachine.getMachineCreatedDate() ? new Date() : mstMachine.getMachineCreatedDate());
            tblMachineLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付
            entityManager.persist(tblMachineLocationHistory);
        } else {
            mstMachine.setInstalledDate(null);
        }

        if (mstMachineVo.getMstMachineSpec() != null && mstMachineVo.getMstMachineSpec().size() > 0) {
            for (int i = 0; i < mstMachineVo.getMstMachineSpec().size(); i++) {
                MstMachineSpecPK mstMachineSpecPK = new MstMachineSpecPK();
                MstMachineSpec mstMachineSpec = new MstMachineSpec();
                MstMachineSpec formMachineSpec = mstMachineVo.getMstMachineSpec().get(i);
                mstMachineSpec.setAttrValue(formMachineSpec.getAttrValue());
                mstMachineSpec.setId(IDGenerator.generate());
                mstMachineSpecPK.setAttrId(formMachineSpec.getMstMachineSpecPK().getAttrId());
                if (mstMachineSpecHistory != null) {
                    mstMachineSpecPK.setMachineSpecHstId(mstMachineSpecHistory.getId());
                }
                mstMachineSpec.setMstMachineSpecPK(mstMachineSpecPK);
                mstMachineSpec.setCreateDate(new Date());
                mstMachineSpec.setCreateUserUuid(loginUser.getUserUuid());
                mstMachineSpec.setUpdateDate(new Date());
                mstMachineSpec.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(mstMachineSpec);
            }
        }
    }

    /**
     * 設備マスタ追加bycsv
     *
     * @param mstMachine
     */
    @Transactional
    public void createMstMachineByCsv(MstMachine mstMachine) {
        entityManager.persist(mstMachine);
    }

    /**
     * 設備マスタ更新
     *
     * @param mstMachineVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateMstMachineByMachineDetail(MstMachineVo mstMachineVo, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();

        MstMachine oldMstMachine = entityManager.find(MstMachine.class, mstMachineVo.getMstMachine().getMachineId());
        if (oldMstMachine == null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }

        //設備マスタ更新
        MstMachine formMachine = mstMachineVo.getMstMachine();

        deleteMachineMaintenanceRecomend(oldMstMachine, formMachine);

        MstMachine updateMstMachine = entityManager.find(MstMachine.class, formMachine.getMachineId());
        updateMstMachine.setMachineName(formMachine.getMachineName());
        updateMstMachine.setMachineType(formMachine.getMachineType());
        updateMstMachine.setMainAssetNo(formMachine.getMainAssetNo());
        updateMstMachine.setMachineCreatedDate(formMachine.getMachineCreatedDate());
        updateMstMachine.setInspectedDate(formMachine.getInspectedDate());
        if (null != formMachine.getOwnerCompanyId() && !formMachine.getOwnerCompanyId().trim().equals("")) {
            updateMstMachine.setOwnerCompanyId(formMachine.getOwnerCompanyId());
        } else {
            updateMstMachine.setOwnerCompanyId(null);
        }
        if (null != formMachine.getCompanyId() && !formMachine.getCompanyId().trim().equals("")) {
            updateMstMachine.setCompanyId(formMachine.getCompanyId());
            updateMstMachine.setCompanyName(formMachine.getCompanyName());
        } else {
            updateMstMachine.setCompanyId(null);
            updateMstMachine.setCompanyName(null);
        }
        if (null != formMachine.getLocationId() && !formMachine.getLocationId().trim().equals("")) {
            updateMstMachine.setLocationId(formMachine.getLocationId());
            updateMstMachine.setLocationName(formMachine.getLocationName());
        } else {
            updateMstMachine.setLocationId(null);
            updateMstMachine.setLocationName(null);
        }
        if (null != formMachine.getInstllationSiteId() && !formMachine.getInstllationSiteId().trim().equals("")) {
            updateMstMachine.setInstllationSiteId(formMachine.getInstllationSiteId());
            updateMstMachine.setInstllationSiteName(formMachine.getInstllationSiteName());
        } else {
            updateMstMachine.setInstllationSiteId(null);
            updateMstMachine.setInstllationSiteName(null);
        }
        if (null == oldMstMachine.getInstalledDate() && (null != updateMstMachine.getCompanyId() || null != updateMstMachine.getLocationId() || null != updateMstMachine.getInstllationSiteId())) {
            updateMstMachine.setInstalledDate(new Date());
        }
        if (null != updateMstMachine.getStatus() && formMachine.getStatus().compareTo(updateMstMachine.getStatus()) != 0) {
            updateMstMachine.setStatusChangedDate(new Date());
        }
        updateMstMachine.setStatus(formMachine.getStatus());
        updateMstMachine.setImgFilePath01(formMachine.getImgFilePath01());
        updateMstMachine.setImgFilePath02(formMachine.getImgFilePath02());
        updateMstMachine.setImgFilePath03(formMachine.getImgFilePath03());
        updateMstMachine.setImgFilePath04(formMachine.getImgFilePath04());
        updateMstMachine.setImgFilePath05(formMachine.getImgFilePath05());
        updateMstMachine.setImgFilePath06(formMachine.getImgFilePath06());
        updateMstMachine.setImgFilePath07(formMachine.getImgFilePath07());
        updateMstMachine.setImgFilePath08(formMachine.getImgFilePath08());
        updateMstMachine.setImgFilePath09(formMachine.getImgFilePath09());
        updateMstMachine.setImgFilePath10(formMachine.getImgFilePath10());

        updateMstMachine.setReportFilePath01(formMachine.getReportFilePath01());
        updateMstMachine.setReportFilePath02(formMachine.getReportFilePath02());
        updateMstMachine.setReportFilePath03(formMachine.getReportFilePath03());
        updateMstMachine.setReportFilePath04(formMachine.getReportFilePath04());
        updateMstMachine.setReportFilePath05(formMachine.getReportFilePath05());
        updateMstMachine.setReportFilePath06(formMachine.getReportFilePath06());
        updateMstMachine.setReportFilePath07(formMachine.getReportFilePath07());
        updateMstMachine.setReportFilePath08(formMachine.getReportFilePath08());
        updateMstMachine.setReportFilePath09(formMachine.getReportFilePath09());
        updateMstMachine.setReportFilePath10(formMachine.getReportFilePath10());

        if (null != formMachine.getDepartment()) {
            updateMstMachine.setDepartment(formMachine.getDepartment());
        } else {
            updateMstMachine.setDepartment(0);
        }
        if (null != formMachine.getSigmaId() && !"".equals(formMachine.getSigmaId().trim())) {
            updateMstMachine.setSigmaId(formMachine.getSigmaId());
        } else {
            updateMstMachine.setSigmaId(null);
        }
        if (null != formMachine.getMacKey() && !"".equals(formMachine.getMacKey().trim())) {
            if (null == updateMstMachine.getMacKey() || !formMachine.getMacKey().equals(updateMstMachine.getMacKey())) {
                if (getMstMachineMacKeyExistCheck(formMachine.getMacKey())) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                    return response;
                }
            }

            updateMstMachine.setMacKey(formMachine.getMacKey());
        } else {
            updateMstMachine.setMacKey(null);
        }
        if (null != formMachine.getBaseCycleTime()) {
            updateMstMachine.setBaseCycleTime(formMachine.getBaseCycleTime());
        } else {
            updateMstMachine.setBaseCycleTime(BigDecimal.ZERO);
        }
        if (null != formMachine.getMachineCd() && !"".equals(formMachine.getMachineCd().trim())) {
            updateMstMachine.setMachineCd(formMachine.getMachineCd());
        } else {
            updateMstMachine.setMachineCd(null);
        }
        if (null != formMachine.getStrageLocationCd() && !"".equals(formMachine.getStrageLocationCd().trim())) {
            updateMstMachine.setStrageLocationCd(formMachine.getStrageLocationCd());
        } else {
            updateMstMachine.setStrageLocationCd(null);
        }
        if (null != formMachine.getChargeCd() && !"".equals(formMachine.getChargeCd().trim())) {
            updateMstMachine.setChargeCd(formMachine.getChargeCd());
        } else {
            updateMstMachine.setChargeCd(null);
        }
        if (null != formMachine.getOperatorCd() && !"".equals(formMachine.getOperatorCd().trim())) {
            updateMstMachine.setOperatorCd(formMachine.getOperatorCd());
        } else {
            updateMstMachine.setOperatorCd(null);
        }

        updateMstMachine.setLastProductionDate(formMachine.getLastProductionDate());
        updateMstMachine.setTotalProducingTimeHour(formMachine.getTotalProducingTimeHour() == null ? BigDecimal.ZERO : formMachine.getTotalProducingTimeHour());
        updateMstMachine.setTotalShotCount(formMachine.getTotalShotCount() == null ? 0 : formMachine.getTotalShotCount());
        updateMstMachine.setLastMainteDate(formMachine.getLastMainteDate());
        updateMstMachine.setAfterMainteTotalProducingTimeHour(formMachine.getAfterMainteTotalProducingTimeHour() == null ? BigDecimal.ZERO : formMachine.getAfterMainteTotalProducingTimeHour());
        updateMstMachine.setAfterMainteTotalShotCount(formMachine.getAfterMainteTotalShotCount() == null ? 0 : formMachine.getAfterMainteTotalShotCount());

        updateMstMachine.setMainteCycleId01(StringUtils.isEmpty(formMachine.getMainteCycleId01()) ? null : formMachine.getMainteCycleId01());
        updateMstMachine.setMainteCycleId02(StringUtils.isEmpty(formMachine.getMainteCycleId02()) ? null : formMachine.getMainteCycleId02());
        updateMstMachine.setMainteCycleId03(StringUtils.isEmpty(formMachine.getMainteCycleId03()) ? null : formMachine.getMainteCycleId03());

        updateMstMachine.setUpdateDate(new Date());
        updateMstMachine.setUpdateUserUuid(loginUser.getUserUuid());

        if (mstMachineVo.getTblMachineLocationHistoryVos() != null && mstMachineVo.getTblMachineLocationHistoryVos().size() > 0) {
            int delCount = 0; //削除count
            TblMachineLocationHistoryVo tblMachineLocationHistorys;
            for (int i = 0; i < mstMachineVo.getTblMachineLocationHistoryVos().size(); i++) {
                tblMachineLocationHistorys = mstMachineVo.getTblMachineLocationHistoryVos().get(i);
                TblMachineLocationHistory tblMachineLocationHistory = entityManager.find(TblMachineLocationHistory.class, tblMachineLocationHistorys.getId());
                if (null != tblMachineLocationHistory) {
                    if (!"1".equals(tblMachineLocationHistorys.getOperationFlag())) {
                        //更新する
                        tblMachineLocationHistory.setMachineUuid(updateMstMachine.getUuid());
                        tblMachineLocationHistory.setChangeReason(tblMachineLocationHistorys.getChangeReason());
                        tblMachineLocationHistory.setChangeReasonText(tblMachineLocationHistorys.getChangeReasonText());
                        if (null != tblMachineLocationHistorys.getCompanyId() && !tblMachineLocationHistorys.getCompanyId().trim().equals("")) {
                            tblMachineLocationHistory.setCompanyId(tblMachineLocationHistorys.getCompanyId());
                            tblMachineLocationHistory.setCompanyName(tblMachineLocationHistorys.getCompanyName());
                        }
                        if (null != tblMachineLocationHistorys.getLocationId() && !tblMachineLocationHistorys.getLocationId().trim().equals("")) {
                            tblMachineLocationHistory.setLocationId(tblMachineLocationHistorys.getLocationId());
                            tblMachineLocationHistory.setLocationName(tblMachineLocationHistorys.getLocationName());
                        } else {
                            tblMachineLocationHistory.setLocationId(null);
                            tblMachineLocationHistory.setLocationName(null);
                        }
                        if (null != tblMachineLocationHistorys.getInstllationSiteId() && !tblMachineLocationHistorys.getInstllationSiteId().trim().equals("")) {
                            tblMachineLocationHistory.setInstllationSiteId(tblMachineLocationHistorys.getInstllationSiteId());
                            tblMachineLocationHistory.setInstllationSiteName(tblMachineLocationHistorys.getInstllationSiteName());
                        } else {
                            tblMachineLocationHistory.setInstllationSiteId(null);
                            tblMachineLocationHistory.setInstllationSiteName(null);
                            updateMstMachine.setInstllationSiteId(null);
                        }
                        tblMachineLocationHistory.setUpdateDate(new Date());
                        tblMachineLocationHistory.setUpdateUserUuid(loginUser.getUserUuid());
                        if ("3".equals(tblMachineLocationHistorys.getOperationFlag())) {
                            entityManager.merge(tblMachineLocationHistory);
                        } else if ("4".equals(tblMachineLocationHistorys.getOperationFlag())) {
                            entityManager.persist(tblMachineLocationHistory);
                        }
                    } else {
                        //削除する
                        entityManager.remove(tblMachineLocationHistory);
                        delCount++;
                    }
                }
            }

            if (delCount == mstMachineVo.getTblMachineLocationHistoryVos().size()) {//if delete all
                updateMstMachine.setCompanyId(null);
                updateMstMachine.setCompanyName(null);
                updateMstMachine.setLocationId(null);
                updateMstMachine.setLocationName(null);
                updateMstMachine.setInstalledDate(null);
                updateMstMachine.setInstllationSiteId(null);
                updateMstMachine.setInstllationSiteName(null);
            } else {
                Query lastestMachineLocationHistoryQuery = entityManager.createQuery("select h from TblMachineLocationHistory h join h.mstMachine m where m.machineId = :machineId order by h.endDate desc,h.startDate desc");
                lastestMachineLocationHistoryQuery.setMaxResults(1);
                lastestMachineLocationHistoryQuery.setParameter("machineId", formMachine.getMachineId());
                List<TblMachineLocationHistory> resTblMachineLocationHistory = lastestMachineLocationHistoryQuery.getResultList();
                if (null != resTblMachineLocationHistory && !resTblMachineLocationHistory.isEmpty()) {
                    TblMachineLocationHistory lastestTblMachineLocationHistory = resTblMachineLocationHistory.get(0);
                    lastestTblMachineLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付
                    entityManager.merge(lastestTblMachineLocationHistory);
                    if (null != lastestTblMachineLocationHistory.getCompanyId() && !lastestTblMachineLocationHistory.getCompanyId().trim().equals("")) {
                        updateMstMachine.setCompanyId(lastestTblMachineLocationHistory.getCompanyId());
                        updateMstMachine.setCompanyName(lastestTblMachineLocationHistory.getCompanyName());
                    }
                    if (null != lastestTblMachineLocationHistory.getLocationId() && !lastestTblMachineLocationHistory.getLocationId().trim().equals("")) {
                        updateMstMachine.setLocationId(lastestTblMachineLocationHistory.getLocationId());
                        updateMstMachine.setLocationName(lastestTblMachineLocationHistory.getLocationName());
                    } else {
                        updateMstMachine.setLocationId(null);
                        updateMstMachine.setLocationName(null);
                    }
                    if (null != lastestTblMachineLocationHistory.getInstllationSiteId() && !lastestTblMachineLocationHistory.getInstllationSiteId().trim().equals("")) {
                        updateMstMachine.setInstllationSiteId(lastestTblMachineLocationHistory.getInstllationSiteId());
                        updateMstMachine.setInstllationSiteName(lastestTblMachineLocationHistory.getInstllationSiteName());
                    } else {
                        updateMstMachine.setInstllationSiteId(null);
                        updateMstMachine.setInstllationSiteName(null);
                    }

                    updateMstMachine.setInstalledDate(lastestTblMachineLocationHistory.getStartDate());
                }
            }
        } else if ((null != updateMstMachine.getInstllationSiteId()
                || null != updateMstMachine.getLocationId()
                || null != updateMstMachine.getCompanyId())) {

            Query qCountLocationHistory = entityManager.createNamedQuery("TblMachineLocationHistory.countByMachineUuid")
                    .setParameter("machineUuid", oldMstMachine.getUuid());
            int count = Integer.parseInt(qCountLocationHistory.getResultList().get(0).toString());
            if (count == 0) {
                TblMachineLocationHistory tblMachineLocationHistory = new TblMachineLocationHistory();
                tblMachineLocationHistory.setId(IDGenerator.generate());
                tblMachineLocationHistory.setMachineUuid(updateMstMachine.getUuid());
                tblMachineLocationHistory.setChangeReason(0); //変更理由：なし（ゼロ）
                if (null != updateMstMachine.getCompanyId() && !updateMstMachine.getCompanyId().trim().equals("")) {
                    tblMachineLocationHistory.setCompanyId(updateMstMachine.getCompanyId());
                    tblMachineLocationHistory.setCompanyName(null == updateMstMachine.getCompanyName() ? null : updateMstMachine.getCompanyName());
                }
                if (null != updateMstMachine.getLocationId() && !updateMstMachine.getLocationId().trim().equals("")) {
                    tblMachineLocationHistory.setLocationId(updateMstMachine.getLocationId());
                    tblMachineLocationHistory.setLocationName(null == updateMstMachine.getLocationName() ? updateMstMachine.getLocationName() : updateMstMachine.getLocationName());
                }
                if (null != updateMstMachine.getInstllationSiteId() && !updateMstMachine.getInstllationSiteId().trim().equals("")) {
                    tblMachineLocationHistory.setInstllationSiteId(updateMstMachine.getInstllationSiteId());
                    tblMachineLocationHistory.setInstllationSiteName(null == updateMstMachine.getInstllationSiteName() ? null : updateMstMachine.getInstllationSiteName());
                }
                tblMachineLocationHistory.setCreateDate(new Date());
                tblMachineLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
                tblMachineLocationHistory.setUpdateDate(new Date());
                tblMachineLocationHistory.setUpdateUserUuid(loginUser.getUserUuid());
                tblMachineLocationHistory.setStartDate(null == updateMstMachine.getInstalledDate() ? new Date() : updateMstMachine.getInstalledDate());
                tblMachineLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付
                entityManager.persist(tblMachineLocationHistory);
            }
        }

        entityManager.merge(updateMstMachine);
        formMachine = updateMstMachine;

        // 設備対応代表番号のＦｌａｇを0にする
        entityManager.createQuery("UPDATE MstMachineAssetNo n set n.mainFlg = 0 WHERE n.mstMachineAssetNoPK.machineUuid = :machineUuid ")
                .setParameter("machineUuid", updateMstMachine.getUuid())
                .executeUpdate();

        if (null != formMachine.getMainAssetNo() && !formMachine.getMainAssetNo().trim().equals("")) {
            MstMachineAssetNo mstMachineAssetNo;
            MstMachineAssetNoPK mstMachineAssetNoPK = new MstMachineAssetNoPK();
            mstMachineAssetNoPK.setMachineUuid(updateMstMachine.getUuid());
            mstMachineAssetNoPK.setAssetNo(formMachine.getMainAssetNo());
            mstMachineAssetNo = entityManager.find(MstMachineAssetNo.class, mstMachineAssetNoPK);
            if (null != mstMachineAssetNo) {
                mstMachineAssetNo.setMainFlg(1);
                mstMachineAssetNo.setUpdateDate(new Date());
                mstMachineAssetNo.setUpdateUserUuid(loginUser.getUserUuid());
                mstMachineAsseNoService.updateMstMachineAssetNoByQuery(mstMachineAssetNo);
            } else {
                mstMachineAssetNo = new MstMachineAssetNo();

                mstMachineAssetNo.setMstMachineAssetNoPK(mstMachineAssetNoPK);
                mstMachineAssetNo.setMainFlg(1);
                mstMachineAssetNo.setAssetAmount(BigDecimal.ZERO);
                mstMachineAssetNo.setNumberedDate(new Date());
                mstMachineAssetNo.setCreateDate(new Date());
                mstMachineAssetNo.setCreateUserUuid(loginUser.getUserUuid());
                mstMachineAssetNo.setId(IDGenerator.generate());
                entityManager.persist(mstMachineAssetNo);
            }
        }

        //boolean needNewSpecHistory = null == mstMachineVo.getMstMachineSpec() || mstMachineVo.getMstMachineSpec().isEmpty() ? false : true;
        boolean needNewSpecHistory = (null != mstMachineVo.getMstMachineSpec());

        String modeType = oldMstMachine.getMachineType().toString();
        //設備種類変更時 すべての設備仕様履歴を削除し
        MstMachineSpecHistory mstMachineSpecHistory = null;
        if (modeType.equals(mstMachineVo.getMstMachine().getMachineType().toString()) == false) {
            List<MstMachineSpecHistory> oldMachineSpecHistorys = entityManager.createNamedQuery("MstMachineSpecHistory.findByMachineUuid")
                    .setParameter("machineUuid", oldMstMachine.getUuid())
                    .getResultList();
            for (int i = 0; i < oldMachineSpecHistorys.size(); i++) {
                entityManager.remove(oldMachineSpecHistorys.get(i));
            }

            //設備仕様マスタ追加
            if (mstMachineVo.getMstMachineSpec() != null && mstMachineVo.getMstMachineSpec().size() > 0) {
                mstMachineSpecHistory = new MstMachineSpecHistory();
                mstMachineSpecHistory.setId(IDGenerator.generate());
                mstMachineSpecHistory.setCreateDate(new Date());
                mstMachineSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
                mstMachineSpecHistory.setStartDate(new Date());
                mstMachineSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付
                mstMachineSpecHistory.setMachineUuid(mstMachineVo.getMstMachine().getUuid());
                mstMachineSpecHistory.setMstMachine(mstMachineVo.getMstMachine());
                //設備仕様履歴名称を「最初のバージョン」(文言キー:machine_spec_first_version）
                mstMachineSpecHistory.setMachineSpecName(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_spec_first_version"));

                mstMachineSpecHistory.setUpdateDate(new Date());
                mstMachineSpecHistory.setUpdateUserUuid(loginUser.getUserUuid());

                entityManager.persist(mstMachineSpecHistory);
            }
        } else {
            if (needNewSpecHistory == true && mstMachineVo.getMstMachineSpec() != null && mstMachineVo.getMstMachineSpec().size() > 0) {
                for (MstMachineSpec aSpec : mstMachineVo.getMstMachineSpec()) {
                    if (null != aSpec.getMstMachineSpecPK().getMachineSpecHstId() && !aSpec.getMstMachineSpecPK().getMachineSpecHstId().trim().equals("")) {
                        needNewSpecHistory = false;
                    }
                }
            }

            if (needNewSpecHistory == true) {
                //設備仕様マスタ追加
                mstMachineSpecHistory = new MstMachineSpecHistory();
                mstMachineSpecHistory.setId(IDGenerator.generate());
                mstMachineSpecHistory.setCreateDate(new Date());
                mstMachineSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
                mstMachineSpecHistory.setStartDate(new Date());
                mstMachineSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付/todo
                mstMachineSpecHistory.setMachineUuid(mstMachineVo.getMstMachine().getUuid());
                mstMachineSpecHistory.setMstMachine(mstMachineVo.getMstMachine());
                //設備仕様履歴名称を「最初のバージョン」(文言キー:machine_spec_first_version）
                mstMachineSpecHistory.setMachineSpecName(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_spec_first_version"));

                mstMachineSpecHistory.setUpdateDate(new Date());
                mstMachineSpecHistory.setUpdateUserUuid(loginUser.getUserUuid());

                Set<MstMachineSpecHistory> mshc = new HashSet<>();
                mshc.add(mstMachineSpecHistory);
                entityManager.persist(mstMachineSpecHistory);
            }
        }

        MstMachineSpec mstMachineSpec;
        //設備仕様マスタ更新
        if (mstMachineVo.getMstMachineSpec() != null && mstMachineVo.getMstMachineSpec().size() > 0) {
            for (int i = 0; i < mstMachineVo.getMstMachineSpec().size(); i++) {
                mstMachineSpec = mstMachineVo.getMstMachineSpec().get(i);
                MstMachineSpecPK mstMachineSpecPK = new MstMachineSpecPK();
                mstMachineSpecPK.setAttrId(mstMachineSpec.getMstMachineSpecPK().getAttrId());
                //設備種類変更時
                if (modeType.equals(mstMachineVo.getMstMachine().getMachineType().toString()) == false) {
                    if (mstMachineSpecHistory != null) {
                        mstMachineSpecPK.setMachineSpecHstId(mstMachineSpecHistory.getId());
                    }
                } else if (needNewSpecHistory == true) {
                    if (mstMachineSpecHistory != null) {
                        mstMachineSpecPK.setMachineSpecHstId(mstMachineSpecHistory.getId());
                    }
                } else {
                    mstMachineSpecPK.setMachineSpecHstId(mstMachineSpec.getMstMachineSpecPK().getMachineSpecHstId());
                }

                mstMachineSpec.setMstMachineSpecPK(mstMachineSpecPK);
                mstMachineSpec.setUpdateDate(new Date());
                mstMachineSpec.setUpdateUserUuid(loginUser.getUserUuid());

                if (mstMachineSpecService.getMstMachineSpecsFK(mstMachineSpecPK.getMachineSpecHstId(), mstMachineSpecPK.getAttrId())) {
                    mstMachineSpecService.updateMstMachineSpecByQuery(mstMachineSpec);
                } else {
                    mstMachineSpec.setId(IDGenerator.generate());
                    mstMachineSpecService.createMstMachineSpecByCsv(mstMachineSpec);
                }
            }
        }

        //設備加工条件マスタ更新
        Map machineProcCondsMap = new HashMap();
        if (mstMachineVo.getMstMachineProcCondVos() != null && mstMachineVo.getMstMachineProcCondVos().size() > 0) {
            for (int mpFlag = 0; mpFlag < mstMachineVo.getMstMachineProcCondVos().size(); mpFlag++) {
                MstMachineProcCondVo aMstMachineProcCondVo = mstMachineVo.getMstMachineProcCondVos().get(mpFlag);
                String machineProcCondId;
                if ("1".equals(aMstMachineProcCondVo.getOperationFlag())) {
                    //削除の記し
                    String id = aMstMachineProcCondVo.getId();
                    if (null != id) {
                        mstMachineProcCondService.deleteProcCondNameById(id);
                    }
                    continue;
                } else if ("4".equals(aMstMachineProcCondVo.getOperationFlag())) {
                    //new 
                    MstMachineProcCond machineProcCond = new MstMachineProcCond();
                    machineProcCond.setId(IDGenerator.generate());
                    if (null != aMstMachineProcCondVo.getComponentId() && !aMstMachineProcCondVo.getComponentId().equals("")) {
                        machineProcCond.setComponentId(aMstMachineProcCondVo.getComponentId());
                    } else {
                        machineProcCond.setComponentId(null);
                    }

                    machineProcCond.setMachineUuid(formMachine.getUuid());
                    machineProcCond.setCreateDate(new Date());
                    machineProcCond.setCreateUserUuid(loginUser.getUserUuid());
                    machineProcCond.setUpdateDate(new Date());
                    machineProcCond.setUpdateUserUuid(loginUser.getUserUuid());
                    machineProcCond.setSeq(mpFlag);//20170608 Apeng add
                    entityManager.persist(machineProcCond);
                    machineProcCondId = machineProcCond.getId();
                } else {
                    machineProcCondId = aMstMachineProcCondVo.getId();
                }
                machineProcCondsMap.put(aMstMachineProcCondVo.getComponentId(), machineProcCondId);
            }
        }

        if (!machineProcCondsMap.isEmpty() && mstMachineVo.getMstMachineProcCondSpecVos() != null && mstMachineVo.getMstMachineProcCondSpecVos().size() > 0) {
            for (int i = 0; i < mstMachineVo.getMstMachineProcCondSpecVos().size(); i++) {
                MstMachineProcCondSpecPK pk = new MstMachineProcCondSpecPK();
                MstMachineProcCondSpecVo machineProcCondSpecVo = mstMachineVo.getMstMachineProcCondSpecVos().get(i);
                MstMachineProcCondSpec machineProcCondSpec;
                String machineProcCondId = machineProcCondsMap.get(null == machineProcCondSpecVo.getComponentId() ? "" : machineProcCondSpecVo.getComponentId()).toString();
                pk.setMachineProcCondId(machineProcCondId);
                pk.setAttrId(machineProcCondSpecVo.getMstMachineProcCondSpecPK().getAttrId());

                List<MstMachineProcCondSpec> mstMachineProcCondSpecs = entityManager.createNamedQuery("MstMachineProcCondSpec.findByMachineProcCondIdAndAttrId")
                        .setParameter("machineProcCondId", machineProcCondId)
                        .setParameter("attrId", machineProcCondSpecVo.getMstMachineProcCondSpecPK().getAttrId())
                        .getResultList();

                if (null != mstMachineProcCondSpecs && !mstMachineProcCondSpecs.isEmpty()) {
                    machineProcCondSpec = mstMachineProcCondSpecs.get(0);
                    machineProcCondSpec.setUpdateDate(new Date());
                    machineProcCondSpec.setUpdateUserUuid(loginUser.getUserUuid());
                    machineProcCondSpec.setMstMachineProcCondSpecPK(pk);
                    machineProcCondSpec.setAttrValue(mstMachineVo.getMstMachineProcCondSpecVos().get(i).getAttrValue());
                    entityManager.merge(machineProcCondSpec);

                } else {
                    machineProcCondSpec = new MstMachineProcCondSpec();
                    machineProcCondSpec.setId(IDGenerator.generate());
                    machineProcCondSpec.setCreateDate(new Date());
                    machineProcCondSpec.setCreateUserUuid(loginUser.getUserUuid());
                    machineProcCondSpec.setUpdateDate(new Date());
                    machineProcCondSpec.setUpdateUserUuid(loginUser.getUserUuid());
                    machineProcCondSpec.setMstMachineProcCondSpecPK(pk);
                    machineProcCondSpec.setAttrValue(mstMachineVo.getMstMachineProcCondSpecVos().get(i).getAttrValue());
                    mstMachineProcCondSpecService.createMstMachineSpec(machineProcCondSpec);
                }
            }
        }

        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /**
     * 設備一覧　CSV出力ボタン処理
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
     * @param totalShotCountFrom
     * @param totalShotCountTo
     * @param lastMainteDateFrom
     * @param lastMainteDateTo
     * @param afterMainteTotalProducingTimeHourFrom
     * @param afterMainteTotalProducingTimeHourTo
     * @param afterMainteTotalShotCountFrom
     * @param afterMainteTotalShotCountTo
     * @param machineCreatedDateFrom
     * @param machineCreatedDateTo
     * @param status
     * @param loginUser
     * @return
     */
    public FileReponse getMstMachineOutputCsv(
            String machineId,
            String machineName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer machineType,
            Integer department,
            Date lastProductionDateFrom,
            Date lastProductionDateTo,
            Integer totalProducingTimeHourFrom,
            Integer totalProducingTimeHourTo,
            Integer totalShotCountFrom,
            Integer totalShotCountTo,
            Date lastMainteDateFrom,
            Date lastMainteDateTo,
            Integer afterMainteTotalProducingTimeHourFrom,
            Integer afterMainteTotalProducingTimeHourTo,
            Integer afterMainteTotalShotCountFrom,
            Integer afterMainteTotalShotCountTo,
            Date machineCreatedDateFrom,
            Date machineCreatedDateTo,
            Integer status,
            LoginUser loginUser) {

        StringBuffer sql = new StringBuffer(" SELECT m FROM MstMachine m "
                + "LEFT JOIN FETCH m.ownerMstCompany "
                + "LEFT JOIN FETCH m.mstCompany "
                + "LEFT JOIN FETCH m.mstInstallationSite "
                + "LEFT JOIN FETCH m.mstLocation "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn01 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn02 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn03 "
                + "WHERE 1=1 ");

        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" AND m.machineId like :machineId ");
        }
        if (machineName != null && !"".equals(machineName)) {
            sql = sql.append(" AND m.machineName like :machineName ");
        }
        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            sql = sql.append(" AND m.mainAssetNo LIKE :mainAssetNo ");
        }
        if (companyName != null && !"".equals(companyName)) {
            sql = sql.append(" and m.companyName like :companyName ");
        }
        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" and m.ownerMstCompany.companyName like :ownerCompanyName ");
        }
        if (locationName != null && !"".equals(locationName)) {
            sql = sql.append(" and m.locationName like :locationName ");
        }
        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            sql = sql.append(" and m.instllationSiteName like :instllationSiteName ");
        }
        if (machineType != null && 0 != machineType) {
            sql = sql.append(" and m.machineType = :machineType ");
        }
        if (department != null && 0 != department) {
            sql = sql.append(" and m.department = :department ");
        }

        if (null != lastProductionDateFrom) {
            sql = sql.append(" and m.lastProductionDate >= :lastProductionDateFrom ");
        }
        if (null != lastProductionDateTo) {
            sql = sql.append(" and m.lastProductionDate <= :lastProductionDateTo ");
        }
        if (null != totalProducingTimeHourFrom) {
            sql = sql.append(" and m.totalProducingTimeHour >= :totalProducingTimeHourFrom ");
        }
        if (null != totalProducingTimeHourTo) {
            sql = sql.append(" and m.totalProducingTimeHour <= :totalProducingTimeHourTo ");
        }
        if (null != totalShotCountFrom) {
            sql = sql.append(" and m.totalShotCount >= :totalShotCountFrom ");
        }
        if (null != totalShotCountTo) {
            sql = sql.append(" and m.totalShotCount <= :totalShotCountTo ");
        }
        if (null != lastMainteDateFrom) {
            sql = sql.append(" and m.lastMainteDate >= :lastMainteDateFrom ");
        }
        if (null != lastMainteDateTo) {
            sql = sql.append(" and m.lastMainteDate <= :lastMainteDateTo ");
        }
        if (null != afterMainteTotalProducingTimeHourFrom) {
            sql = sql.append(" and m.afterMainteTotalProducingTimeHour >= :afterMainteTotalProducingTimeHourFrom ");
        }
        if (null != afterMainteTotalProducingTimeHourTo) {
            sql = sql.append(" and m.afterMainteTotalProducingTimeHour <= :afterMainteTotalProducingTimeHourTo ");
        }
        if (null != afterMainteTotalShotCountFrom) {
            sql = sql.append(" and m.afterMainteTotalShotCount >= :afterMainteTotalShotCountFrom ");
        }
        if (null != afterMainteTotalShotCountTo) {
            sql = sql.append(" and m.afterMainteTotalShotCount <= :afterMainteTotalShotCountTo ");
        }

        if (null != status) {
            sql = sql.append(" and m.status = :status ");
        }

        if (null != machineCreatedDateFrom) {
            sql = sql.append(" and m.machineCreatedDate >= :machineCreatedDateFrom ");
        }

        if (null != machineCreatedDateTo) {
            sql = sql.append(" and m.machineCreatedDate <= :machineCreatedDateTo ");
        }

        sql = sql.append(" Order by m.machineId ");

        Query getCsvDetail = entityManager.createQuery(sql.toString());

        if (machineId != null && !"".equals(machineId)) {
            getCsvDetail.setParameter("machineId", "%" + machineId + "%");
        }

        if (machineName != null && !"".equals(machineName)) {
            getCsvDetail.setParameter("machineName", "%" + machineName + "%");
        }

        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            getCsvDetail.setParameter("mainAssetNo", "%" + mainAssetNo + "%");
        }

        if (companyName != null && !"".equals(companyName)) {
            getCsvDetail.setParameter("companyName", "%" + companyName + "%");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            getCsvDetail.setParameter("ownerCompanyName", "%" + ownerCompanyName + "%");
        }

        if (locationName != null && !"".equals(locationName)) {
            getCsvDetail.setParameter("locationName", "%" + locationName + "%");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            getCsvDetail.setParameter("instllationSiteName", "%" + instllationSiteName + "%");
        }

        if (machineType != null && 0 != machineType) {
            getCsvDetail.setParameter("machineType", machineType);
        }

        if (department != null && 0 != department) {
            getCsvDetail.setParameter("department", department);
        }

        if (null != lastProductionDateFrom) {
            getCsvDetail.setParameter("lastProductionDateFrom", lastProductionDateFrom);
        }
        if (null != lastProductionDateTo) {
            getCsvDetail.setParameter("lastProductionDateTo", lastProductionDateTo);
        }
        if (null != totalProducingTimeHourFrom) {
            getCsvDetail.setParameter("totalProducingTimeHourFrom", totalProducingTimeHourFrom);
        }
        if (null != totalProducingTimeHourTo) {
            getCsvDetail.setParameter("totalProducingTimeHourTo", totalProducingTimeHourTo);
        }
        if (null != totalShotCountFrom) {
            getCsvDetail.setParameter("totalShotCountFrom", totalShotCountFrom);
        }
        if (null != totalShotCountTo) {
            getCsvDetail.setParameter("totalShotCountTo", totalShotCountTo);
        }
        if (null != lastMainteDateFrom) {
            getCsvDetail.setParameter("lastMainteDateFrom", lastMainteDateFrom);
        }
        if (null != lastMainteDateTo) {
            getCsvDetail.setParameter("lastMainteDateTo", lastMainteDateTo);
        }
        if (null != afterMainteTotalProducingTimeHourFrom) {
            getCsvDetail.setParameter("afterMainteTotalProducingTimeHourFrom", afterMainteTotalProducingTimeHourFrom);
        }
        if (null != afterMainteTotalProducingTimeHourTo) {
            getCsvDetail.setParameter("afterMainteTotalProducingTimeHourTo", afterMainteTotalProducingTimeHourTo);
        }
        if (null != afterMainteTotalShotCountFrom) {
            getCsvDetail.setParameter("afterMainteTotalShotCountFrom", afterMainteTotalShotCountFrom);
        }
        if (null != afterMainteTotalShotCountTo) {
            getCsvDetail.setParameter("afterMainteTotalShotCountTo", afterMainteTotalShotCountTo);
        }

        if (null != status) {
            getCsvDetail.setParameter("status", status);
        }

        if (null != machineCreatedDateFrom) {
            getCsvDetail.setParameter("machineCreatedDateFrom", machineCreatedDateFrom);
        }

        if (null != machineCreatedDateTo) {
            getCsvDetail.setParameter("machineCreatedDateTo", machineCreatedDateTo);
        }

        List list = getCsvDetail.getResultList();
        int specHeaderCount = 0;//仕様個数
        MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
        MstChoiceList machineTypeChoice = mstChoiceService.getChoice(loginUser.getLangId(), "mst_machine.machine_type");
        MstChoiceList machineStatuChoice = mstChoiceService.getChoice(loginUser.getLangId(), "mst_mold.status");

        ArrayList<ArrayList> machineOutList = new ArrayList<>();

        //一時格納用
        ArrayList<String> tempOutList;
        for (int i = 0; i < list.size(); i++) {
            MstMachine csvMstMachine = (MstMachine) list.get(i);

            tempOutList = new ArrayList<>();
            tempOutList.add(csvMstMachine.getMachineId());
            tempOutList.add(csvMstMachine.getMachineName());

            if (machineTypeChoice != null && machineTypeChoice.getMstChoice() != null && machineTypeChoice.getMstChoice().size() > 0) {
                boolean found = false;
                for (int momi = 0; momi < machineTypeChoice.getMstChoice().size(); momi++) {
                    MstChoice aMstChoice = machineTypeChoice.getMstChoice().get(momi);
                    if (aMstChoice.getMstChoicePK().getSeq() != null && csvMstMachine.getMachineType() != null) {
                        if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(csvMstMachine.getMachineType()))) {
                            tempOutList.add(aMstChoice.getChoice());
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    tempOutList.add("");
                }

            } else {
                tempOutList.add("");
            }

            tempOutList.add(csvMstMachine.getMainAssetNo());
            if (csvMstMachine.getMachineCreatedDate() != null) {
                tempOutList.add(FileUtil.dateFormat(csvMstMachine.getMachineCreatedDate()));
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getInspectedDate() != null) {
                tempOutList.add(FileUtil.dateFormat(csvMstMachine.getInspectedDate()));
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getOwnerMstCompany() != null) {
                tempOutList.add(csvMstMachine.getOwnerMstCompany().getCompanyCode());
                tempOutList.add(csvMstMachine.getOwnerMstCompany().getCompanyName());
            } else {
                tempOutList.add("");
                tempOutList.add("");
            }

            if (csvMstMachine.getInstalledDate() != null) {
                tempOutList.add(FileUtil.dateFormat(csvMstMachine.getInstalledDate()));
            } else {
                tempOutList.add("");
            }

            if (csvMstMachine.getMstCompany() != null) {
                tempOutList.add(csvMstMachine.getMstCompany().getCompanyCode());
                tempOutList.add(csvMstMachine.getMstCompany().getCompanyName());
            } else {
                tempOutList.add("");
                tempOutList.add("");
            }

            if (csvMstMachine.getMstLocation() != null) {
                tempOutList.add(csvMstMachine.getMstLocation().getLocationCode());
                tempOutList.add(csvMstMachine.getMstLocation().getLocationName());
            } else {
                tempOutList.add("");
                tempOutList.add("");
            }

            if (csvMstMachine.getMstInstallationSite() != null) {
                tempOutList.add(csvMstMachine.getMstInstallationSite().getInstallationSiteCode());
                tempOutList.add(csvMstMachine.getMstInstallationSite().getInstallationSiteName());
            } else {
                tempOutList.add("");
                tempOutList.add("");
            }

            if (machineStatuChoice != null && machineStatuChoice.getMstChoice() != null && machineStatuChoice.getMstChoice().size() > 0) {
                for (int momi = 0; momi < machineStatuChoice.getMstChoice().size(); momi++) {
                    MstChoice aMstChoice = machineStatuChoice.getMstChoice().get(momi);
                    if (aMstChoice.getMstChoicePK().getSeq() != null && csvMstMachine.getStatus() != null) {
                        if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(csvMstMachine.getStatus()))) {
                            tempOutList.add(aMstChoice.getChoice());
                            break;
                        }
                    }
                }
            } else {
                tempOutList.add("");
            }

            if (csvMstMachine.getStatusChangedDate() != null) {
                tempOutList.add(FileUtil.dateFormat(csvMstMachine.getStatusChangedDate()));
            } else {
                tempOutList.add("");
            }

            if (csvMstMachine.getDepartment() != null) {
                int departmentVal = csvMstMachine.getDepartment();
                String departmentName = "";
                for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                    if (mstChoice.getMstChoicePK().getSeq().equals("" + departmentVal)) {
                        departmentName = mstChoice.getChoice();
                        break;
                    }
                }
                if (!departmentName.equals("")) {
                    tempOutList.add(departmentName);
                } else {
                    tempOutList.add("");
                }
            } else {
                tempOutList.add("");
            }

            if (csvMstMachine.getMstSigma() != null) {
                tempOutList.add(csvMstMachine.getMstSigma().getSigmaCode());
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getMacKey() != null) {
                tempOutList.add(csvMstMachine.getMacKey());
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getBaseCycleTime() != null) {
                tempOutList.add("" + csvMstMachine.getBaseCycleTime());
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getMachineCd() != null) {
                tempOutList.add(csvMstMachine.getMachineCd());
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getStrageLocationCd() != null) {
                tempOutList.add(csvMstMachine.getStrageLocationCd());
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getChargeCd() != null) {
                tempOutList.add(csvMstMachine.getChargeCd());
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getOperatorCd() != null) {
                tempOutList.add(csvMstMachine.getOperatorCd());
            } else {
                tempOutList.add("");
            }

            if (csvMstMachine.getLastProductionDate() != null) {
                tempOutList.add(FileUtil.dateFormat(csvMstMachine.getLastProductionDate()));
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getTotalProducingTimeHour() != null) {
                tempOutList.add("" + csvMstMachine.getTotalProducingTimeHour());
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getTotalShotCount() != null) {
                tempOutList.add("" + csvMstMachine.getTotalShotCount());
            } else {
                tempOutList.add("0");
            }
            if (csvMstMachine.getLastMainteDate() != null) {
                tempOutList.add(FileUtil.dateFormat(csvMstMachine.getLastMainteDate()));
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getAfterMainteTotalProducingTimeHour() != null) {
                tempOutList.add("" + csvMstMachine.getAfterMainteTotalProducingTimeHour());
            } else {
                tempOutList.add("");
            }
            if (csvMstMachine.getAfterMainteTotalShotCount() != null) {
                tempOutList.add("" + csvMstMachine.getAfterMainteTotalShotCount());
            } else {
                tempOutList.add("");
                csvMstMachine.getBlMaintenanceCyclePtn01();
            }
            if (null != csvMstMachine.getBlMaintenanceCyclePtn01()) {
                tempOutList.add(csvMstMachine.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                tempOutList.add("");
            }
            if (null != csvMstMachine.getBlMaintenanceCyclePtn02()) {
                tempOutList.add(csvMstMachine.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                tempOutList.add("");
            }
            if (null != csvMstMachine.getBlMaintenanceCyclePtn03()) {
                tempOutList.add(csvMstMachine.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                tempOutList.add("");
            }

            // 仕様取得
            int itemSpec = 0;
            List<MstMachineSpecHistory> csvMstMachineSpecHistory = entityManager.createQuery("select t from MstMachineSpecHistory t where t.machineUuid = :machineUuid order by t.endDate desc ")
                    .setParameter("machineUuid", csvMstMachine.getUuid())
                    .setMaxResults(1)
                    .getResultList();
            if (null != csvMstMachineSpecHistory && !csvMstMachineSpecHistory.isEmpty()) {
                Query getCsvApec = entityManager.createNamedQuery("MstMachineSpec.findByMachineListSpec");
                getCsvApec.setParameter("machineSpecHstId", csvMstMachineSpecHistory.get(0).getId());
                if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, csvMstMachine.getMachineId(), null, loginUser).isError() == true) {
                    getCsvApec.setParameter("externalFlg", CommonConstants.EXTERNALFLG);
                } else {
                    getCsvApec.setParameter("externalFlg", CommonConstants.MINEFLAG);
                }
                getCsvApec.setParameter("machineType", csvMstMachine.getMachineType());
                List<MstMachineSpec> csvMstMachineSpec = getCsvApec.getResultList();
                if (null != csvMstMachineSpec && !csvMstMachineSpec.isEmpty()) {
                    for (MstMachineSpec mstMachineSpec : csvMstMachineSpec) {
                        tempOutList.add(mstMachineSpec.getAttrValue());
                        itemSpec = itemSpec + 1;
                    }
                }
            }

            // 仕様最大個数を取得
            if (specHeaderCount < itemSpec) {
                specHeaderCount = itemSpec;
            }
            machineOutList.add(tempOutList);
        }
        /**
         * Header 用意
         */
        String langId = loginUser.getLangId();

        List<String> dictKeyList = Arrays.asList(
                "machine_id",
                "machine_name",
                "machine_type",
                "main_asset_no",
                "machine_created_date",
                "inspected_date",
                "owner_company_code",//所有会社名称
                "owner_company_name",//所有会社名称
                "installed_date",
                "company_code",
                "company_name",
                "location_code",
                "location_name",
                "installation_site_code",
                "installation_site_name",
                "user_department",
                "machine_last_production_date",
                "machine_total_production_time_hour",
                "total_shot_count",
                "machine_last_mainte_date",
                "machine_after_mainte_total_production_time_hour",
                "machine_after_mainte_total_shot_count",
                "machine_mainte_cycle_code_01",
                "machine_mainte_cycle_code_02",
                "machine_mainte_cycle_code_03",
                "sigma_code",
                "mac_key",
                "base_cycle_time",
                "machine_code",
                "strage_location_cd",
                "charge_cd",
                "operator_cd",
                "mold_status",
                "status_changed_date",
                "machine_spec",//設備仕様
                "component_code",//部品コード
                "delete_record"
        );
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        String outMachineId = headerMap.get("machine_id");
        String outMachineName = headerMap.get("machine_name");
        String outMachineType = headerMap.get("machine_type");
        String outMainAssetNo = headerMap.get("main_asset_no");
        String outMachineCreatedDate = headerMap.get("machine_created_date");
        String outInspectedDate = headerMap.get("inspected_date");
        String outOwnerCompanyCode = headerMap.get("owner_company_code");//所有会社名称
        String outOwnerCompanyName = headerMap.get("owner_company_name");//所有会社名称
        String outInstalledDate = headerMap.get("installed_date");
        String outCompanyCode = headerMap.get("company_code");
        String outCompanyName = headerMap.get("company_name");
        String outLocationCode = headerMap.get("location_code");
        String outLocationName = headerMap.get("location_name");
        String outInstllationSiteCode = headerMap.get("installation_site_code");
        String outInstllationSiteName = headerMap.get("installation_site_name");

        String outDepartmentName = headerMap.get("user_department");

        String outLastProductionDate = headerMap.get("machine_last_production_date");
        String outTotalProductionTimeHour = headerMap.get("machine_total_production_time_hour");
        String outTotalShotCount = headerMap.get("total_shot_count");
        String outLastMainteDate = headerMap.get("machine_last_mainte_date");
        String outAfterMainteTotalProducingTimeHour = headerMap.get("machine_after_mainte_total_production_time_hour");
        String outAfterMainteTotalShotCount = headerMap.get("machine_after_mainte_total_shot_count");
        String outMainteCycleCode01 = headerMap.get("machine_mainte_cycle_code_01");
        String outMainteCycleCode02 = headerMap.get("machine_mainte_cycle_code_02");
        String outMainteCycleCode03 = headerMap.get("machine_mainte_cycle_code_03");

        String outSigmaCode = headerMap.get("sigma_code");
        String outMacKey = headerMap.get("mac_key");
        String outBaseCycletime = headerMap.get("base_cycle_time");
        String outMachineCd = headerMap.get("machine_code");
        String outStrageLocationCd = headerMap.get("strage_location_cd");
        String outChargeCd = headerMap.get("charge_cd");
        String outOperatorCd = headerMap.get("operator_cd");

        String outStatus = headerMap.get("mold_status");
        String outStatusChangedDate = headerMap.get("status_changed_date");

        String outMachineSpec = headerMap.get("machine_spec");//設備仕様
        String delete = headerMap.get("delete_record");

        FileReponse fr = new FileReponse();

        /*Head*/
        ArrayList csvOutHeadList = new ArrayList();
        csvOutHeadList.add(outMachineId);
        csvOutHeadList.add(outMachineName);
        csvOutHeadList.add(outMachineType);
        csvOutHeadList.add(outMainAssetNo);
        csvOutHeadList.add(outMachineCreatedDate);
        csvOutHeadList.add(outInspectedDate);
        csvOutHeadList.add(outOwnerCompanyCode);
        csvOutHeadList.add(outOwnerCompanyName);
        csvOutHeadList.add(outInstalledDate);
        csvOutHeadList.add(outCompanyCode);
        csvOutHeadList.add(outCompanyName);
        csvOutHeadList.add(outLocationCode);
        csvOutHeadList.add(outLocationName);
        csvOutHeadList.add(outInstllationSiteCode);
        csvOutHeadList.add(outInstllationSiteName);
        csvOutHeadList.add(outStatus);
        csvOutHeadList.add(outStatusChangedDate);
        csvOutHeadList.add(outDepartmentName);
        csvOutHeadList.add(outSigmaCode);
        csvOutHeadList.add(outMacKey);
        csvOutHeadList.add(outBaseCycletime);
        csvOutHeadList.add(outMachineCd);
        csvOutHeadList.add(outStrageLocationCd);
        csvOutHeadList.add(outChargeCd);
        csvOutHeadList.add(outOperatorCd);
        csvOutHeadList.add(outLastProductionDate);
        csvOutHeadList.add(outTotalProductionTimeHour);
        csvOutHeadList.add(outTotalShotCount);
        csvOutHeadList.add(outLastMainteDate);
        csvOutHeadList.add(outAfterMainteTotalProducingTimeHour);
        csvOutHeadList.add(outAfterMainteTotalShotCount);
        csvOutHeadList.add(outMainteCycleCode01);
        csvOutHeadList.add(outMainteCycleCode02);
        csvOutHeadList.add(outMainteCycleCode03);
        int index = 34;//固定列数
        for (int i = 0; i < machineOutList.size(); i++) {
            if (machineOutList.get(i).size() < index + specHeaderCount) {
                for (int j = machineOutList.get(i).size(); j < index + specHeaderCount; j++) {
                    machineOutList.get(i).add("");
                }
            }
            machineOutList.get(i).add("");//delete
        }

        //仕様Header
        for (int i = 0; i < specHeaderCount; i++) {
            csvOutHeadList.add(outMachineSpec + (i + 1));
        }
        // Header準備完了
        csvOutHeadList.add(delete);
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(csvOutHeadList);

        // 出力データ準備
        for (int i = 0; i < machineOutList.size(); i++) {
            gLineList.add(machineOutList.get(i));
        }

        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MACHINE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.TBL_MST_MACHINE);
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;

    }

    /**
     * 設備一覧　CSV取り込みボタン CSVの中身に対してチェックを行う
     *
     * @param logParm
     * @param lineCsv
     * @param userLangId
     * @param logFile
     * @param index
     * @return
     */
    public boolean checkCsvFileData(Map<String, String> logParm, String lineCsv[], String userLangId, String logFile, int index) {
        //ログ出力内容を用意する
        /**
         * Head
         */
        String lineNo = logParm.get("lineNo");
        String head_machineId = logParm.get("head_machineId");//設備ID
        String head_machineName = logParm.get("head_machineName");//設備名称
        String head_machineType = logParm.get("head_machineType");//設備種類
        String head_machineAssetNo = logParm.get("head_machineAssetNo");//代表資産番号
        String head_machineCreatedDate = logParm.get("head_machineCreatedDate");//設備作成日
        String head_inspectedDdate = logParm.get("head_inspectedDdate");//検収日
        String head_machineOwnerCompanyCode = logParm.get("head_machineOwnerCompanyCode");//所有会社名称
        String head_installedDate = logParm.get("head_installedDate");//設置日
        String head_machineCompanyCode = logParm.get("head_machineCompanyCode");//会社名称
        String head_machineLocationCode = logParm.get("head_machineLocationCode");//所在地名称
        String head_machineInstallationSiteCode = logParm.get("head_machineInstallationSiteCode");//設置場所名称
        String head_status = logParm.get("head_status");//状態
        String head_statusChangedDate = logParm.get("head_statusChangedDate");//状態変更日 
        String head_DepartmentName = logParm.get("head_DepartmentName");//所属 
        String head_SigmaCode = logParm.get("head_SigmaCode");//Sigma 
        String head_BaseCycletime = logParm.get("head_BaseCycletime");//BaseCycletime 
        String head_MacKey = logParm.get("head_MacKey");//MacKey
        String head_MachineCd = logParm.get("head_MachineCd");//MachineCd
        String head_StrageLocationCd = logParm.get("head_StrageLocationCd");//StrageLocationCd
        String head_ChargeCd = logParm.get("head_ChargeCd");//ChargeCd
        String head_OperatorCd = logParm.get("head_OperatorCd");//OperatorCd
        String head_machinespec = logParm.get("head_machinespec");//設備仕様

        String head_lastProductionDate = logParm.get("head_lastProductionDate");
        String head_totalProductionTimeHour = logParm.get("head_totalProductionTimeHour");
        String head_totalShotCount = logParm.get("head_totalShotCount");
        String head_lastMainteDate = logParm.get("head_lastMainteDate");
        String head_afterMainteTotalProductionTimeHour = logParm.get("head_afterMainteTotalProductionTimeHour");
        String head_afterMainteTotalShotCount = logParm.get("head_afterMainteTotalShotCount");
        String head_mainteCycleCode01 = logParm.get("head_mainteCycleCode01");
        String head_mainteCycleCode02 = logParm.get("head_mainteCycleCode02");
        String head_mainteCycleCode03 = logParm.get("head_mainteCycleCode03");

        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String maxLangth = logParm.get("maxLangth");
        String dateCheck = logParm.get("dateCheck");
        String errorValue = logParm.get("errorValue");
        String numberCheck = logParm.get("numberCheck");
        String layout = logParm.get("layout");
        String notFound = logParm.get("notFound");
        String existsMsg = logParm.get("existsMsg");
        FileUtil fu = new FileUtil();

        String strMachineId = lineCsv[0].trim();//設備ID
        if (fu.isNullCheck(strMachineId)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineId, strMachineId, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strMachineId, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineId, strMachineId, error, 1, errorContents, maxLangth));
            return false;
        }

        String strMachineName = lineCsv[1].trim();//設備名称
        if (fu.isNullCheck(strMachineName)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineName, strMachineName, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strMachineName, 100)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineName, strMachineName, error, 1, errorContents, maxLangth));
            return false;
        }

        String strMachineType = lineCsv[2].trim();//設備種類
        Object machineTypeValue = "0";
        Map inMachineTypeMapTempS = inMachineTypeOfChoice(userLangId);
        if (!"".equals(strMachineType.trim())) {
            if (null == (machineTypeValue = inMachineTypeMapTempS.get(strMachineType))) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineType, strMachineType, error, 1, errorContents, notFound));
                return false;
            }
            if (fu.isNullCheck(machineTypeValue.toString())) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineType, strMachineType, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strMachineAssetNo = lineCsv[3].trim();//代表資産番号
        if (fu.maxLangthCheck(strMachineAssetNo, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineAssetNo, strMachineAssetNo, error, 1, errorContents, maxLangth));
            return false;
        }

        String strMachineCreatedDate = lineCsv[4].trim();//設備作成日
        if (!fu.isNullCheck(strMachineCreatedDate)) {
            if (!fu.dateCheck(strMachineCreatedDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineCreatedDate, strMachineCreatedDate, error, 1, errorContents, dateCheck));
                return false;
            }
        }
        String strInspectedDate = lineCsv[5].trim();//検収日
        if (!fu.isNullCheck(strInspectedDate)) {
            if (!fu.dateCheck(strInspectedDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_inspectedDdate, strInspectedDate, error, 1, errorContents, dateCheck));
                return false;
            }
        }
        String strOwnerCompanyCode = lineCsv[6].trim();//所有会社コード
        if (fu.maxLangthCheck(strOwnerCompanyCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineOwnerCompanyCode, strOwnerCompanyCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strOwnerCompanyCode)) {
            if (!mstCompanyService.getMstCompanyExistCheck(strOwnerCompanyCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineOwnerCompanyCode, strOwnerCompanyCode, error, 1, errorContents, notFound));
                return false;
            }
        }
        String strInstalledDate = lineCsv[8].trim();//設置日
        if (!fu.isNullCheck(strInstalledDate)) {
            if (!fu.dateCheck(strInstalledDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_installedDate, strInstalledDate, error, 1, errorContents, dateCheck));
                return false;
            }
        }
        String strMachineCompanyCode = lineCsv[9].trim();//会社コード
        if (fu.maxLangthCheck(strMachineCompanyCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineCompanyCode, strMachineCompanyCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strMachineCompanyCode)) {
            if (!mstCompanyService.getMstCompanyExistCheck(strMachineCompanyCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineCompanyCode, strMachineCompanyCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strMachineLocationCode = lineCsv[11].trim();//所在地コード
        if (fu.maxLangthCheck(strMachineLocationCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineLocationCode, strMachineLocationCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strMachineLocationCode)) {
            if (fu.isNullCheck(strMachineCompanyCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineCompanyCode, strMachineCompanyCode, error, 1, errorContents, notFound));
                return false;
            }
            if (!mstLocationService.getMstLocationExistCheck(strMachineLocationCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineLocationCode, strMachineLocationCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strMachineInstallationSiteCode = lineCsv[13].trim();//設置場所コード
        if (fu.maxLangthCheck(strMachineInstallationSiteCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineInstallationSiteCode, strMachineInstallationSiteCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strMachineInstallationSiteCode)) {
            if (fu.isNullCheck(strMachineCompanyCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineCompanyCode, strMachineCompanyCode, error, 1, errorContents, notFound));
                return false;
            }
            if (fu.isNullCheck(strMachineLocationCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineLocationCode, strMachineLocationCode, error, 1, errorContents, notFound));
                return false;
            }
            if (!mstInstallationSiteService.getMstInstallationSiteExistCheck(strMachineInstallationSiteCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineInstallationSiteCode, strMachineInstallationSiteCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strStatus = lineCsv[15].trim();//ステータス
        Map inStatusMapTempS = inStatusOfChoice(userLangId);
        if (inStatusMapTempS.get(strStatus) == null) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_status, strStatus, error, 1, errorContents, notFound));
            return false;
        } else if (fu.isNullCheck(inStatusMapTempS.get(strStatus).toString())) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_status, strStatus, error, 1, errorContents, notFound));
            return false;
        }

        String strStatusChangedDate = lineCsv[16].trim();//ステータス変更日
        if (strStatusChangedDate != null && !"".equals(strStatusChangedDate)) {
            if (!fu.dateCheck(strStatusChangedDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_statusChangedDate, strStatusChangedDate, error, 1, errorContents, dateCheck));
                return false;
            }
        }

        String strDepartment = lineCsv[17].trim();//所属
        Map inDepartmentsOfChoice = inDepartmentsOfChoice(userLangId);
        if (!fu.isNullCheck(strDepartment)) {
            if (fu.maxLangthCheck(strMachineInstallationSiteCode, 100)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_DepartmentName, strDepartment, error, 1, errorContents, maxLangth));
                return false;
            } else if (inDepartmentsOfChoice.get(strDepartment) == null) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_DepartmentName, strDepartment, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strSigmaCode = lineCsv[18].trim();//Sigma
        if (fu.maxLangthCheck(strSigmaCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_SigmaCode, strSigmaCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strSigmaCode)) {
            if (!mstSigmaService.checkSigmaCode(strSigmaCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_SigmaCode, strSigmaCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strMacKey = lineCsv[19].trim();//MacKey
        if (!fu.isNullCheck(strMacKey)) {
            if (fu.maxLangthCheck(strMacKey, 50)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_MacKey, strMacKey, error, 1, errorContents, maxLangth));
                return false;
            } else if (!checkMacKeyUnique(strMacKey, strMachineId)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_MacKey, strMacKey, error, 1, errorContents, existsMsg));
                return false;
            }
        }

        String strBaseCycleTime = lineCsv[20].trim();//BaseCycleTime
        if (!fu.isNullCheck(strBaseCycleTime)) {
            if (!fu.isDouble(strBaseCycleTime)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_BaseCycletime, strBaseCycleTime, error, 1, errorContents, numberCheck));
                return false;
            }
        }

        String strMachineCd = lineCsv[21].trim();//MachineCd
        if (fu.maxLangthCheck(strMachineCd, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_MachineCd, strMachineCd, error, 1, errorContents, maxLangth));
            return false;
        }

        String strStrageLocationCd = lineCsv[22].trim();//StrageLocationCd
        if (fu.maxLangthCheck(strStrageLocationCd, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_StrageLocationCd, strStrageLocationCd, error, 1, errorContents, maxLangth));
            return false;
        }

        String strChargeCd = lineCsv[23].trim();//ChargeCd
        if (fu.maxLangthCheck(strChargeCd, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_ChargeCd, strChargeCd, error, 1, errorContents, maxLangth));
            return false;
        }

        String strOperatorCd = lineCsv[24].trim();//OperatorCd
        if (fu.maxLangthCheck(strOperatorCd, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_OperatorCd, strOperatorCd, error, 1, errorContents, maxLangth));
            return false;
        }

        String strLastProductionDate = lineCsv[25].trim();//LastProductionDate
        if (!fu.isNullCheck(strLastProductionDate)) {
            strLastProductionDate = DateFormat.formatDateYear(strLastProductionDate, DateFormat.DATE_FORMAT);
            if (!fu.dateCheck(strLastProductionDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_lastProductionDate, strLastProductionDate, error, 1, errorContents, dateCheck));
                return false;
            }
        }
        String strTotalProductionTimeHour = lineCsv[26].trim();//TotalProductionTimeHour
        if (!fu.isNullCheck(strTotalProductionTimeHour)) {
            if (fu.maxLangthCheck(strTotalProductionTimeHour, 11)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_totalProductionTimeHour, strTotalProductionTimeHour, error, 1, errorContents, maxLangth));
                return false;
            //} else if (!FileUtil.isInteger(strTotalProductionTimeHour)) {
            } else if (!NumberUtil.validateDecimal(strTotalProductionTimeHour, 10, 1)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_totalProductionTimeHour, strTotalProductionTimeHour, error, 1, errorContents, errorValue));
                return false;
            }
        }
        String strTotalShotCount = lineCsv[27].trim();//TotalShotCount
        if (!fu.isNullCheck(strTotalShotCount)) {
            if (fu.maxLangthCheck(strTotalShotCount, 11)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_totalShotCount, strTotalShotCount, error, 1, errorContents, maxLangth));
                return false;
            } else if (!FileUtil.isInteger(strTotalShotCount)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_totalShotCount, strTotalShotCount, error, 1, errorContents, errorValue));
                return false;
            }
        }
        String strLastMainteDate = lineCsv[28].trim();//LastMainteDate
        if (!fu.isNullCheck(strLastMainteDate)) {
            strLastMainteDate = DateFormat.formatDateYear(strLastMainteDate, DateFormat.DATE_FORMAT);
            if (!fu.dateCheck(strLastMainteDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_lastMainteDate, strLastMainteDate, error, 1, errorContents, dateCheck));
                return false;
            }
        }
        String strAfterMainteTotalProductionTimeHour = lineCsv[29].trim();//AfterMainteTotalProductionTimeHour
        if (!fu.isNullCheck(strAfterMainteTotalProductionTimeHour)) {
            if (fu.maxLangthCheck(strAfterMainteTotalProductionTimeHour, 11)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_afterMainteTotalProductionTimeHour, strAfterMainteTotalProductionTimeHour, error, 1, errorContents, maxLangth));
                return false;
//            } else if (!FileUtil.isInteger(strAfterMainteTotalProductionTimeHour)) {
            } else if (!NumberUtil.validateDecimal(strAfterMainteTotalProductionTimeHour, 10, 1)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_afterMainteTotalProductionTimeHour, strAfterMainteTotalProductionTimeHour, error, 1, errorContents, errorValue));
                return false;
            }
        }
        String strAfterMainteTotalShotCount = lineCsv[30].trim();//AfterMainteTotalShotCount
        if (!fu.isNullCheck(strAfterMainteTotalShotCount)) {
            if (fu.maxLangthCheck(strAfterMainteTotalShotCount, 11)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_afterMainteTotalShotCount, strAfterMainteTotalShotCount, error, 1, errorContents, maxLangth));
                return false;
            } else if (!FileUtil.isInteger(strAfterMainteTotalShotCount)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_afterMainteTotalShotCount, strAfterMainteTotalShotCount, error, 1, errorContents, errorValue));
                return false;
            }
        }
        String strMainteCycleCode01 = lineCsv[31].trim();//MainteCycleCode01
        if (!fu.isNullCheck(strMainteCycleCode01)) {
            if (fu.maxLangthCheck(strMainteCycleCode01, 45)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode01, strMainteCycleCode01, error, 1, errorContents, maxLangth));
                return false;
            } else if (!fu.isNullCheck(strMainteCycleCode01)) {
                List<TblMaintenanceCyclePtn> maintenanceCyclePtns = tblMaintenanceCyclePtnService.getTblMaintenanceCyclePtnsByTypeAndCodes(2, strMainteCycleCode01);
                if (null == maintenanceCyclePtns || maintenanceCyclePtns.isEmpty()) {
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode01, strMainteCycleCode01, error, 1, errorContents, notFound));
                    return false;
                }
            }
        }
        String strMainteCycleCode02 = lineCsv[32].trim();//MainteCycleCode02
        if (!fu.isNullCheck(strMainteCycleCode02)) {
            if (fu.maxLangthCheck(strMainteCycleCode02, 45)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode02, strMainteCycleCode02, error, 1, errorContents, maxLangth));
                return false;
            } else if (!fu.isNullCheck(strMainteCycleCode02)) {
                List<TblMaintenanceCyclePtn> maintenanceCyclePtns = tblMaintenanceCyclePtnService.getTblMaintenanceCyclePtnsByTypeAndCodes(2, strMainteCycleCode02);
                if (null == maintenanceCyclePtns || maintenanceCyclePtns.isEmpty()) {
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode02, strMainteCycleCode02, error, 1, errorContents, notFound));
                    return false;
                }
            }
        }
        String strMainteCycleCode03 = lineCsv[33].trim();//MainteCycleCode03
        if (!fu.isNullCheck(strMainteCycleCode03)) {
            if (fu.maxLangthCheck(strMainteCycleCode03, 45)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode03, strMainteCycleCode03, error, 1, errorContents, maxLangth));
                return false;
            } else if (!fu.isNullCheck(strMainteCycleCode03)) {
                List<TblMaintenanceCyclePtn> maintenanceCyclePtns = tblMaintenanceCyclePtnService.getTblMaintenanceCyclePtnsByTypeAndCodes(2, strMainteCycleCode03);
                if (null == maintenanceCyclePtns || maintenanceCyclePtns.isEmpty()) {
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode03, strMainteCycleCode03, error, 1, errorContents, notFound));
                    return false;
                }
            }
        }

        if (lineCsv.length >= 34) {//仕様があります時
            MstMachineAttributeList mstMachineAttribute = mstMachineAttributeService.getMstMachineAttributes(machineTypeValue.toString());
            int attrCode = mstMachineAttribute.getMstMachineAttributes().size();
            int iFor;
            if (lineCsv.length - 35 > attrCode) {
                iFor = attrCode;
            } else {
                iFor = lineCsv.length - 35;
            }
            for (int j = 0; j < iFor; j++) {
                if (StringUtils.isEmpty(lineCsv[j + 34])) {
                    continue;
                }
                String strAttrValue = String.valueOf(lineCsv[j + 34]).trim();
                MstMachineAttribute aMstMachineAttribute = mstMachineAttribute.getMstMachineAttributes().get(j);
                int attrType = aMstMachineAttribute.getAttrType();
                // KM-427
                boolean numCheck = false;
                try {
                    Float.parseFloat(strAttrValue);
                } catch (Exception e) {
                    numCheck = true;
                }
                if (numCheck && CommonConstants.ATTRIBUTE_TYPE_NUMBER == attrType) { //KM-427
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machinespec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, numberCheck));
                    return false;
                }
                if (fu.checkSpecAttrType(attrType, strAttrValue) == false) {
                    //エラー情報をログファイルに記入
                    switch (attrType) {
                        case CommonConstants.ATTRIBUTE_TYPE_DATE:
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machinespec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, dateCheck));
                            break;
                        case CommonConstants.ATTRIBUTE_TYPE_NUMBER:
                        case CommonConstants.ATTRIBUTE_TYPE_TEXT:
                        case CommonConstants.ATTRIBUTE_TYPE_STATICLINK:
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machinespec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, maxLangth));
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 設備一覧　CSV取り込みボタン CSVの中身に対してチェックを行う (外部设备)
     *
     * @param logParm
     * @param lineCsv
     * @param userLangId
     * @param logFile
     * @param index
     * @return
     */
    public boolean checkExtCsvFileData(Map<String, String> logParm, String lineCsv[], String userLangId, String logFile, int index) {
        //ログ出力内容を用意する
        /**
         * Head
         */
        String lineNo = logParm.get("lineNo");
        String head_machineId = logParm.get("head_machineId");//設備ID        
        String head_machineAssetNo = logParm.get("head_machineAssetNo");//代表資産番号        
        String head_SigmaCode = logParm.get("head_SigmaCode");//Sigma 
        String head_MacKey = logParm.get("head_MacKey");//MacKey
        String head_MachineCd = logParm.get("head_MachineCd");//MachineCd
        String head_StrageLocationCd = logParm.get("head_StrageLocationCd");//StrageLocationCd
        String head_ChargeCd = logParm.get("head_ChargeCd");//ChargeCd
        String head_OperatorCd = logParm.get("head_OperatorCd");//OperatorCd
        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String maxLangth = logParm.get("maxLangth");
        String dataCheck = logParm.get("dataCheck");
        String layout = logParm.get("layout");
        String notFound = logParm.get("notFound");
        String existsMsg = logParm.get("existsMsg");
        FileUtil fu = new FileUtil();

        String strMachineId = lineCsv[0].trim();//設備ID
        if (fu.isNullCheck(strMachineId)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineId, strMachineId, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strMachineId, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineId, strMachineId, error, 1, errorContents, maxLangth));
            return false;
        }

        String strMachineAssetNo = lineCsv[3].trim();//代表資産番号
        if (fu.maxLangthCheck(strMachineAssetNo, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_machineAssetNo, strMachineAssetNo, error, 1, errorContents, maxLangth));
            return false;
        }

        String strSigmaCode = lineCsv[18].trim();//Sigma
        if (fu.maxLangthCheck(strSigmaCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_SigmaCode, strSigmaCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strSigmaCode)) {
            if (!mstSigmaService.checkSigmaCode(strSigmaCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_SigmaCode, strSigmaCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strMacKey = lineCsv[19].trim();//MacKey
        if (!fu.isNullCheck(strMacKey)) {
            if (fu.maxLangthCheck(strMacKey, 50)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_MacKey, strMacKey, error, 1, errorContents, maxLangth));
                return false;
            } else if (!checkMacKeyUnique(strMacKey, strMachineId)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_MacKey, strMacKey, error, 1, errorContents, existsMsg));
                return false;
            }
        }

        String strMachineCd = lineCsv[21].trim();//MachineCd
        if (fu.maxLangthCheck(strMachineCd, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_MachineCd, strMachineCd, error, 1, errorContents, maxLangth));
            return false;
        }

        String strStrageLocationCd = lineCsv[22].trim();//StrageLocationCd
        if (fu.maxLangthCheck(strStrageLocationCd, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_StrageLocationCd, strStrageLocationCd, error, 1, errorContents, maxLangth));
            return false;
        }

        String strChargeCd = lineCsv[23].trim();//ChargeCd
        if (fu.maxLangthCheck(strChargeCd, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_ChargeCd, strChargeCd, error, 1, errorContents, maxLangth));
            return false;
        }

        String strOperatorCd = lineCsv[24].trim();//OperatorCd
        if (fu.maxLangthCheck(strOperatorCd, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_OperatorCd, strOperatorCd, error, 1, errorContents, maxLangth));
            return false;
        }

        return true;
    }

    /**
     *
     * @param machineId
     * @param machineName
     * @param ext
     * @param loginUser
     * @return
     */
    public MstMachineList getMstMachineAutoComplete(String machineId, String machineName, String ext, LoginUser loginUser) {

        StringBuilder sql;
        String sqlMachineId = "";
        String sqlMachineName = "";
        sql = new StringBuilder(" SELECT m FROM MstMachine m WHERE 1=1 ");

        if ("like".equals(ext)) {
            if (machineId != null && !"".equals(machineId)) {
                sqlMachineId = machineId.trim();
                sql = sql.append(" and m.machineId like :machineId ");
            }

            if (machineName != null && !"".equals(machineName)) {
                sqlMachineName = machineName.trim();
                sql = sql.append(" and m.machineName like :machineName ");
            }
        } else {
            if (machineId != null && !"".equals(machineId)) {
                sqlMachineId = machineId.trim();
                sql = sql.append(" and m.machineId =:machineId ");
            }

            if (machineName != null && !"".equals(machineName)) {
                sqlMachineName = machineName.trim();
                sql = sql.append(" and m.machineName =:machineName ");
            }
        }
        sql = sql.append(" Order By m.machineId ");//設備IDの昇順

        Query query = entityManager.createQuery(sql.toString());

        if ("like".equals(ext)) {
            if (machineId != null && !"".equals(machineId)) {
                query.setParameter("machineId", "%" + sqlMachineId + "%");
            }

            if (machineName != null && !"".equals(machineName)) {
                query.setParameter("machineName", "%" + sqlMachineName + "%");
            }
            query.setMaxResults(100);
        } else {
            if (machineId != null && !"".equals(machineId)) {
                query.setParameter("machineId", sqlMachineId);
            }

            if (machineName != null && !"".equals(machineName)) {
                query.setParameter("machineName", sqlMachineName);
            }
        }

        MstMachineList mstMachineList = new MstMachineList();
        MstMoldAutoComplete mstMachineAutoComplete;
        List<MstMoldAutoComplete> mstMachineAutoCompletes = new ArrayList<>();
        List list = query.getResultList();
        for (int i = 0; i < list.size(); i++) {
            mstMachineAutoComplete = new MstMoldAutoComplete();
            MstMachine mstMachine = (MstMachine) list.get(i);

            BasicResponse response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, mstMachine.getMachineId(), "", loginUser);
            if (response.isError()) {
                mstMachineAutoComplete.setExternalFlag("1");
            } else {
                mstMachineAutoComplete.setExternalFlag("0");
            }
            mstMachineAutoComplete.setUuid(mstMachine.getUuid());
            mstMachineAutoComplete.setMachineUuid(mstMachine.getUuid());
            mstMachineAutoComplete.setMachineId(mstMachine.getMachineId());
            mstMachineAutoComplete.setMachineName(mstMachine.getMachineName());
            mstMachineAutoComplete.setInstalledDate(mstMachine.getInstalledDate());
            mstMachineAutoComplete.setMachineType(String.valueOf(FileUtil.getIntegerValue(mstMachine.getMachineType())));
            if (null != mstMachine.getMstCompany()) {
                mstMachineAutoComplete.setCompanyId(mstMachine.getMstCompany().getId());
                mstMachineAutoComplete.setCompanyName(mstMachine.getMstCompany().getCompanyName());
            } else {
                mstMachineAutoComplete.setCompanyId("");
                mstMachineAutoComplete.setCompanyName("");
            }

            if (null != mstMachine.getMstLocation()) {
                mstMachineAutoComplete.setLocationId(mstMachine.getMstLocation().getId());
                mstMachineAutoComplete.setLocationName(mstMachine.getMstLocation().getLocationName());
            } else {
                mstMachineAutoComplete.setLocationId("");
                mstMachineAutoComplete.setLocationName("");
            }

            if (null != mstMachine.getMstInstallationSite()) {
                mstMachineAutoComplete.setInstllationSiteId(mstMachine.getMstInstallationSite().getId());
                mstMachineAutoComplete.setInstllationSiteName(mstMachine.getMstInstallationSite().getInstallationSiteName());
            } else {
                mstMachineAutoComplete.setInstllationSiteId("");
                mstMachineAutoComplete.setInstllationSiteName("");
            }
            mstMachineAutoCompletes.add(mstMachineAutoComplete);

        }
        mstMachineList.setMstMachineAutoComplete(mstMachineAutoCompletes);
        return mstMachineList;
    }

    /**
     *
     * @param machineId
     * @param machineName
     * @param ext
     * @param loginUser
     * @return
     */
    public MstMachineList getMstMachineAutoCompleteWithoutDispose(String machineId, String machineName, String ext, LoginUser loginUser) {

        StringBuilder sql;
        String sqlMachineId = "";
        String sqlMachineName = "";
        sql = new StringBuilder(" SELECT m FROM MstMachine m WHERE 1=1 and m.status <> 9 ");

        if ("like".equals(ext)) {
            if (machineId != null && !"".equals(machineId)) {
                sqlMachineId = machineId.trim();
                sql = sql.append(" and m.machineId like :machineId ");
            }

            if (machineName != null && !"".equals(machineName)) {
                sqlMachineName = machineName.trim();
                sql = sql.append(" and m.machineName like :machineName ");
            }
        } else {
            if (machineId != null && !"".equals(machineId)) {
                sqlMachineId = machineId.trim();
                sql = sql.append(" and m.machineId =:machineId ");
            }

            if (machineName != null && !"".equals(machineName)) {
                sqlMachineName = machineName.trim();
                sql = sql.append(" and m.machineName =:machineName ");
            }
        }
        sql = sql.append(" Order By m.machineId ");//設備IDの昇順

        Query query = entityManager.createQuery(sql.toString());

        if ("like".equals(ext)) {
            if (machineId != null && !"".equals(machineId)) {
                query.setParameter("machineId", "%" + sqlMachineId + "%");
            }

            if (machineName != null && !"".equals(machineName)) {
                query.setParameter("machineName", "%" + sqlMachineName + "%");
            }
            query.setMaxResults(100);
        } else {
            if (machineId != null && !"".equals(machineId)) {
                query.setParameter("machineId", sqlMachineId);
            }

            if (machineName != null && !"".equals(machineName)) {
                query.setParameter("machineName", sqlMachineName);
            }
        }

        MstMachineList mstMachineList = new MstMachineList();
        MstMoldAutoComplete mstMachineAutoComplete;
        List<MstMoldAutoComplete> mstMachineAutoCompletes = new ArrayList<>();
        List list = query.getResultList();
        for (int i = 0; i < list.size(); i++) {
            mstMachineAutoComplete = new MstMoldAutoComplete();
            MstMachine mstMachine = (MstMachine) list.get(i);

            BasicResponse response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, mstMachine.getMachineId(), "", loginUser);
            if (response.isError()) {
                mstMachineAutoComplete.setExternalFlag("1");
            } else {
                mstMachineAutoComplete.setExternalFlag("0");
            }
            mstMachineAutoComplete.setUuid(mstMachine.getUuid());
            mstMachineAutoComplete.setMachineUuid(mstMachine.getUuid());
            mstMachineAutoComplete.setMachineId(mstMachine.getMachineId());
            mstMachineAutoComplete.setMachineName(mstMachine.getMachineName());
            mstMachineAutoComplete.setInstalledDate(mstMachine.getInstalledDate());
            mstMachineAutoComplete.setMachineType(String.valueOf(FileUtil.getIntegerValue(mstMachine.getMachineType())));
            if (null != mstMachine.getMstCompany()) {
                mstMachineAutoComplete.setCompanyId(mstMachine.getMstCompany().getId());
                mstMachineAutoComplete.setCompanyName(mstMachine.getMstCompany().getCompanyName());
            } else {
                mstMachineAutoComplete.setCompanyId("");
                mstMachineAutoComplete.setCompanyName("");
            }

            if (null != mstMachine.getMstLocation()) {
                mstMachineAutoComplete.setLocationId(mstMachine.getMstLocation().getId());
                mstMachineAutoComplete.setLocationName(mstMachine.getMstLocation().getLocationName());
            } else {
                mstMachineAutoComplete.setLocationId("");
                mstMachineAutoComplete.setLocationName("");
            }

            if (null != mstMachine.getMstInstallationSite()) {
                mstMachineAutoComplete.setInstllationSiteId(mstMachine.getMstInstallationSite().getId());
                mstMachineAutoComplete.setInstllationSiteName(mstMachine.getMstInstallationSite().getInstallationSiteName());
            } else {
                mstMachineAutoComplete.setInstllationSiteId("");
                mstMachineAutoComplete.setInstllationSiteName("");
            }
            mstMachineAutoCompletes.add(mstMachineAutoComplete);

        }
        mstMachineList.setMstMachineAutoComplete(mstMachineAutoCompletes);
        return mstMachineList;
    }    
    
    /**
     * QRコードを読み取り、その設備情報を表示する。 設備マスタ詳細取得
     *
     * @param machineId
     * @param langId
     * @return
     */
    public MstMachineVo getMachineByMachineId(String machineId, String langId) {
        MstMachineVo response = new MstMachineVo();
        Query query1 = entityManager.createNamedQuery("MstMachine.findByMachineId")
                .setParameter("machineId", machineId);
        try {
            MstMachine mstMachine = (MstMachine) query1.getSingleResult();
            response.setMstMachine(mstMachine);
        } catch (NoResultException e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found"));
            return response;
        }

        return response;
    }

    /**
     * このメッソードタブレット専用　修正しないでください 設備棚卸（タブレット） 設備棚卸登録/検索 設備棚卸情報を取得
     *
     * @param machineId
     * @param companyId
     * @param ownerCompanyName
     * @param locationId
     * @param installationSiteId
     * @param department
     * @param login
     * @return
     */
    public MstMachineList getMachineInventories(String machineId, String companyId, String ownerCompanyName, String locationId, String installationSiteId, int department, LoginUser login) {

        MstMachineList tblMachineList = new MstMachineList();
        StringBuffer sql = new StringBuffer(" SELECT t FROM MstMachine t "
                + " LEFT JOIN FETCH t.latestTblMachineInventory "
                + " LEFT JOIN FETCH t.ownerMstCompany "
                + " LEFT JOIN FETCH t.mstCompany "
                + " LEFT JOIN FETCH t.mstLocation "
                + " LEFT JOIN FETCH t.mstInstallationSite "
                + " WHERE 1=1 ");

        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" And t.machineId = :machineId ");
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("machineId", machineId);

            List list = query.getResultList();
            tblMachineList.setMstMachines(list);
            return tblMachineList;
        }

        if (companyId != null && !"".equals(companyId)) {
            sql = sql.append(" And t.mstCompany.id = :companyId ");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" And t.ownerMstCompany.companyName = :ownerCompanyName ");
        }

        if (locationId != null && !"".equals(locationId)) {
            sql = sql.append(" And t.mstLocation.id = :locationId ");
        }

        if (installationSiteId != null && !"".equals(installationSiteId)) {
            sql = sql.append(" And t.mstInstallationSite.id = :installationSiteId ");
        }
        if (department != 0) {
            sql = sql.append(" And t.department = :department ");
        }
        Query query = entityManager.createQuery(sql.toString());

        if (companyId != null && !"".equals(companyId)) {
            query.setParameter("companyId", companyId);
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            query.setParameter("ownerCompanyName", ownerCompanyName);
        }

        if (locationId != null && !"".equals(locationId)) {
            query.setParameter("locationId", locationId);
        }

        if (installationSiteId != null && !"".equals(installationSiteId)) {
            query.setParameter("installationSiteId", installationSiteId);
        }
        if (department != 0) {
            query.setParameter("department", department);
        }
        List list = query.getResultList();
        tblMachineList.setMstMachines(list);

        return tblMachineList;
    }

    /**
     * バッチで設備マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @param machineId
     * @param machineUuid
     * @param apiUserId
     * @return
     */
    public MstMachineList getExtMachinesByBatch(String latestExecutedDate, String machineId, String machineUuid, String apiUserId) {
        MstMachineList resList = new MstMachineList();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMachine t join MstApiUser u on u.companyId = t.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineId && !machineId.trim().equals("")) {
            sql.append(" and t.machineId = :machineId ");
        }
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.uuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        // 外部バッチ取得認証できたログインAPiユーザID
        sql.append(" and u.userId = :apiUserId ");

        Query query = entityManager.createQuery(sql.toString());
        if (null != machineId && !machineId.trim().equals("")) {
            query.setParameter("machineId", machineId);
        }
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        query.setParameter("apiUserId", apiUserId);
        List<MstMachineVo> mstMachineDetails = new ArrayList<>();
        List<MstMachine> tmpList = query.getResultList();
        for (MstMachine mstMachine : tmpList) {
            MstMachineVo aMachineVo = new MstMachineVo();
            aMachineVo.setMachineUuid(mstMachine.getUuid());
            if (null != mstMachine.getCreateDate()) {
                aMachineVo.setCreatedDate(mstMachine.getCreateDate());
                aMachineVo.setCreatedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getCreateDate()));
            }
            aMachineVo.setCreateUserUuid(mstMachine.getCreateUserUuid());
            aMachineVo.setImgFilePath01(mstMachine.getImgFilePath01());
            aMachineVo.setImgFilePath02(mstMachine.getImgFilePath02());
            aMachineVo.setImgFilePath03(mstMachine.getImgFilePath03());
            aMachineVo.setImgFilePath04(mstMachine.getImgFilePath04());
            aMachineVo.setImgFilePath05(mstMachine.getImgFilePath05());
            aMachineVo.setImgFilePath06(mstMachine.getImgFilePath06());
            aMachineVo.setImgFilePath07(mstMachine.getImgFilePath07());
            aMachineVo.setImgFilePath08(mstMachine.getImgFilePath08());
            aMachineVo.setImgFilePath09(mstMachine.getImgFilePath09());
            aMachineVo.setImgFilePath10(mstMachine.getImgFilePath10());

            aMachineVo.setReportFilePath01(mstMachine.getReportFilePath01());
            aMachineVo.setReportFilePath02(mstMachine.getReportFilePath02());
            aMachineVo.setReportFilePath03(mstMachine.getReportFilePath03());
            aMachineVo.setReportFilePath04(mstMachine.getReportFilePath04());
            aMachineVo.setReportFilePath05(mstMachine.getReportFilePath05());
            aMachineVo.setReportFilePath06(mstMachine.getReportFilePath06());
            aMachineVo.setReportFilePath07(mstMachine.getReportFilePath07());
            aMachineVo.setReportFilePath08(mstMachine.getReportFilePath08());
            aMachineVo.setReportFilePath09(mstMachine.getReportFilePath09());
            aMachineVo.setReportFilePath10(mstMachine.getReportFilePath10());

            if (null != mstMachine.getInspectedDate()) {
                aMachineVo.setInspectedDate(mstMachine.getInspectedDate());
                aMachineVo.setInspectedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getInspectedDate()));
            }
            if (null != mstMachine.getInstalledDate()) {
                aMachineVo.setInstalledDate(mstMachine.getInstalledDate());
                aMachineVo.setInstalledDateStr(new FileUtil().getDateFormatForStr(mstMachine.getInstalledDate()));
            }
            if (null != mstMachine.getInstllationSiteId()) {
                aMachineVo.setInstllationSiteId(mstMachine.getInstllationSiteId());
                aMachineVo.setInstllationSiteName(mstMachine.getInstllationSiteName());
            }
            if (null != mstMachine.getLatestInventoryId()) {
                aMachineVo.setLatestInventoryId(mstMachine.getLatestInventoryId());
            }
            if (null != mstMachine.getLocationId()) {
                aMachineVo.setLocationId(mstMachine.getLocationId());
                aMachineVo.setLocationName(mstMachine.getLocationName());
            }
            aMachineVo.setMainteStatus(mstMachine.getMainteStatus());
            if (null != mstMachine.getMachineCreatedDate()) {
                aMachineVo.setMachineCreatedDate(mstMachine.getMachineCreatedDate());
                aMachineVo.setMachineCreatedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getMachineCreatedDate()));
            }
            aMachineVo.setMachineId(mstMachine.getMachineId());
            aMachineVo.setMachineName(mstMachine.getMachineName());
            aMachineVo.setMachineType(mstMachine.getMachineType());
            aMachineVo.setBaseCycleTime(null == mstMachine.getBaseCycleTime() ? null : "" + mstMachine.getBaseCycleTime());

            aMachineVo.setStatus(mstMachine.getStatus());
            if (null != mstMachine.getStatusChangedDate()) {
                aMachineVo.setStatusChangedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getStatusChangedDate()));
            }
            if (null != mstMachine.getUpdateDate()) {
                aMachineVo.setUpdateDateStr(new FileUtil().getDateFormatForStr(mstMachine.getUpdateDate()));
            }
            if (null != mstMachine.getUpdateUserUuid()) {
                aMachineVo.setUpdateUserUuid(mstMachine.getUpdateUserUuid());
            }
            /// KM-260 LYD 追加　S
            //最終生産日
            aMachineVo.setLastProductionDate(mstMachine.getLastProductionDate());
            //累計生産時間
            aMachineVo.setTotalProducingTimeHour(mstMachine.getTotalProducingTimeHour());
            //累計ショット数
            aMachineVo.setTotalShotCount(String.valueOf(FileUtil.getIntegerValue(mstMachine.getTotalShotCount())));
            //最終メンテナンス日
            aMachineVo.setLastMainteDate(mstMachine.getLastMainteDate());
            //メンテナンス後生産時間
            aMachineVo.setAfterMainteTotalProducingTimeHour(mstMachine.getAfterMainteTotalProducingTimeHour());
            //メンテナンス後ショット数
            aMachineVo.setAfterMainteTotalShotCount(mstMachine.getAfterMainteTotalShotCount());
            /// KM-260 LYD 追加　E
            mstMachineDetails.add(aMachineVo);
        }
        resList.setMstMachineVos(mstMachineDetails);
        return resList;
    }

    /**
     * バッチで設備マスタデータを更新
     *
     * @param machineDetails
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachinesByBatch(List<MstMachineVo> machineDetails) {
        BasicResponse response = new BasicResponse();

        if (machineDetails != null && !machineDetails.isEmpty()) {
            for (MstMachineVo aMachineVo : machineDetails) {

                //設備
                MstMachine updateMachine = entityManager.find(MstMachine.class, aMachineVo.getMachineId());
                if (null == updateMachine) {
                    continue;
                }
                updateMachine.setMachineName(aMachineVo.getMachineName());
                updateMachine.setMachineType(aMachineVo.getMachineType());
                if (null != aMachineVo.getMachineCreatedDateStr() && !aMachineVo.getMachineCreatedDateStr().trim().equals("")) {
                    updateMachine.setMachineCreatedDate(new FileUtil().getDateParseForDate(aMachineVo.getMachineCreatedDateStr()));
                }
                if (null != aMachineVo.getInspectedDateStr() && !aMachineVo.getInspectedDateStr().trim().equals("")) {
                    updateMachine.setInspectedDate(new FileUtil().getDateParseForDate(aMachineVo.getInspectedDateStr()));
                }
                if (null != aMachineVo.getInstalledDateStr() && !aMachineVo.getInstalledDateStr().trim().equals("")) {
                    updateMachine.setInstalledDate(new FileUtil().getDateParseForDate(aMachineVo.getInstalledDateStr()));
                }
                if (null != aMachineVo.getLocationId() && !aMachineVo.getLocationId().trim().equals("")) {
                    MstLocation location = entityManager.find(MstLocation.class, aMachineVo.getLocationId());
                    if (null != location) {
                        updateMachine.setLocationId(aMachineVo.getLocationId());
                        updateMachine.setLocationName(aMachineVo.getLocationName());
                    } else {
                        updateMachine.setLocationId(null);
                        updateMachine.setLocationName(null);
                    }
                } else {
                    updateMachine.setLocationId(null);
                    updateMachine.setLocationName(null);
                }

                if (null != aMachineVo.getInstllationSiteId() && !aMachineVo.getInstllationSiteId().trim().equals("")) {
                    MstInstallationSite installationSite = entityManager.find(MstInstallationSite.class, aMachineVo.getInstllationSiteId());
                    if (null != installationSite) {
                        updateMachine.setInstllationSiteId(aMachineVo.getInstllationSiteId());
                        updateMachine.setInstllationSiteName(aMachineVo.getInstllationSiteName());
                    } else {
                        updateMachine.setInstllationSiteId(null);
                        updateMachine.setInstllationSiteName(null);
                    }
                } else {
                    updateMachine.setInstllationSiteId(null);
                    updateMachine.setInstllationSiteName(null);
                }

                if (null != aMachineVo.getLatestInventoryId() && !aMachineVo.getLatestInventoryId().trim().equals("")) {
                    TblMachineInventory machineInventory = entityManager.find(TblMachineInventory.class, aMachineVo.getLatestInventoryId());
                    if (null != machineInventory) {
                        updateMachine.setLatestInventoryId(aMachineVo.getLatestInventoryId());
                    }
                }

                updateMachine.setStatus(aMachineVo.getStatus());
                if (null != aMachineVo.getStatusChangedDateStr() && !aMachineVo.getStatusChangedDateStr().trim().equals("")) {
                    updateMachine.setStatusChangedDate(new FileUtil().getDateParseForDate(aMachineVo.getStatusChangedDateStr()));
                }

                /// KM-260 LYD 追加　S
                //最終生産日
                updateMachine.setLastProductionDate(aMachineVo.getLastProductionDate());
                //累計生産時間
                updateMachine.setTotalProducingTimeHour(aMachineVo.getTotalProducingTimeHour());
                //累計ショット数
                updateMachine.setTotalShotCount(Integer.parseInt(aMachineVo.getTotalShotCount()));
                //最終メンテナンス日
                updateMachine.setLastMainteDate(aMachineVo.getLastMainteDate());
                //メンテナンス後生産時間
                updateMachine.setAfterMainteTotalProducingTimeHour(aMachineVo.getAfterMainteTotalProducingTimeHour());
                //メンテナンス後ショット数
                updateMachine.setAfterMainteTotalShotCount(aMachineVo.getAfterMainteTotalShotCount());
                /// KM-260 LYD 追加　E

                updateMachine.setImgFilePath01(aMachineVo.getImgFilePath01());
                updateMachine.setImgFilePath02(aMachineVo.getImgFilePath02());
                updateMachine.setImgFilePath03(aMachineVo.getImgFilePath03());
                updateMachine.setImgFilePath04(aMachineVo.getImgFilePath04());
                updateMachine.setImgFilePath05(aMachineVo.getImgFilePath05());
                updateMachine.setImgFilePath06(aMachineVo.getImgFilePath06());
                updateMachine.setImgFilePath07(aMachineVo.getImgFilePath07());
                updateMachine.setImgFilePath08(aMachineVo.getImgFilePath08());
                updateMachine.setImgFilePath09(aMachineVo.getImgFilePath09());
                updateMachine.setImgFilePath10(aMachineVo.getImgFilePath10());

                updateMachine.setReportFilePath01(aMachineVo.getReportFilePath01());
                updateMachine.setReportFilePath02(aMachineVo.getReportFilePath02());
                updateMachine.setReportFilePath03(aMachineVo.getReportFilePath03());
                updateMachine.setReportFilePath04(aMachineVo.getReportFilePath04());
                updateMachine.setReportFilePath05(aMachineVo.getReportFilePath05());
                updateMachine.setReportFilePath06(aMachineVo.getReportFilePath06());
                updateMachine.setReportFilePath07(aMachineVo.getReportFilePath07());
                updateMachine.setReportFilePath08(aMachineVo.getReportFilePath08());
                updateMachine.setReportFilePath09(aMachineVo.getReportFilePath09());
                updateMachine.setReportFilePath10(aMachineVo.getReportFilePath10());

                if (null != aMachineVo.getBaseCycleTime() && !"".equals(aMachineVo.getBaseCycleTime().trim())) {
                    updateMachine.setBaseCycleTime(new BigDecimal(aMachineVo.getBaseCycleTime()));
                } else {
                    updateMachine.setBaseCycleTime(null);
                }

                updateMachine.setMainteStatus(aMachineVo.getMainteStatus());
                if (null != aMachineVo.getCreatedDateStr() && !aMachineVo.getCreatedDateStr().trim().equals("")) {
                    updateMachine.setCreateDate(new FileUtil().getDateParseForDate(aMachineVo.getCreatedDateStr()));
                    updateMachine.setCreateUserUuid(aMachineVo.getCreateUserUuid());
                }
                updateMachine.setUpdateDate(new Date());
                updateMachine.setUpdateUserUuid(aMachineVo.getUpdateUserUuid());
                entityManager.merge(updateMachine);//更新

            }
        }
        return response;
    }

    /**
     * 設備IDによる1件取得
     *
     * @param machineId
     * @return
     */
    public MstMachine getMstMachineByMachineId(String machineId) {
        Query query = entityManager.createNamedQuery("MstMachine.findByMachineId");
        query.setParameter("machineId", machineId);
        try {
            return (MstMachine) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * UUIDによる1件取得
     *
     * @param uuid
     * @return
     */
    public MstMachine getMstMachineByUuid(String uuid) {
        Query query = entityManager.createNamedQuery("MstMachine.findByUuid");
        query.setParameter("uuid", uuid);
        try {
            return (MstMachine) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 設備マスターのFK依存関係チェック
     *
     * @param machineId
     * @return
     */
    public boolean getMstMachineFKCheck(String machineId) {

        //tbl_production    FK_TBL_PRODUCTION_MACHINE_UUID  NO ACTION
        Query query = entityManager.createNamedQuery("MstMachine.findByMachineId");
        query.setParameter("machineId", machineId);

        boolean flg = false;
        try {
            MstMachine mstMachine = (MstMachine) query.getSingleResult();
            String machineUuid = mstMachine.getUuid();

            // 設備の関連テーブルの作業実績テーブルＦＫチェックを実装
            if (!flg) {
                //mst_installation_site	NO ACTION ok
                Query queryTblProduction = entityManager.createNamedQuery("TblProduction.findFkByMachineUuid");
                queryTblProduction.setParameter("machineUuid", machineUuid);
                flg = queryTblProduction.getResultList().size() > 0;
            }

            // 設備の関連テーブルの機械日報テーブルＦＫチェックを実装
            if (!flg) {
                //tbl_machine_daily_report	NO ACTION ok
                Query queryTblMachineDailyReport = entityManager.createNamedQuery("TblMachineDailyReport.findFkByMachineUuid");
                queryTblMachineDailyReport.setParameter("machineUuid", machineUuid);
                flg = queryTblMachineDailyReport.getResultList().size() > 0;
            }
        } catch (NoResultException e) {
            // nothing
        }
        return flg;
    }

    public MstMachine getMstMachineFromMacKey(String macKey) {
        Query query = entityManager.createNamedQuery("MstMachine.findByMacKey");
        query.setParameter("macKey", macKey);
        List<MstMachine> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return (MstMachine)list.get(0);
        }
        else {
            return null;
        }
    }
    
    /**
     * getMachineUuid 連携コード(Σ軍師連携用)をキーとして、UUIDを取得する
     *
     * @param macKey
     * @return
     */
    public MstMachineVo getMachineUuid(String macKey) {

        Query query = entityManager.createNamedQuery("MstMachine.findByMacKey");
        query.setParameter("macKey", macKey);
        try {
            MstMachine mstMachine = (MstMachine) query.getSingleResult();
            MstMachineVo mstMachineVo = new MstMachineVo();
            mstMachineVo.setUuid(mstMachine.getUuid());
            return mstMachineVo;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * macKey唯一性チェック
     *
     * @param strMacKey
     * @param strMachineId
     * @return
     */
    private boolean checkMacKeyUnique(String strMacKey, String strMachineId) {
        List<MstMachine> machines = entityManager.createNamedQuery("MstMachine.findByMacKey")
                .setParameter("macKey", strMacKey)
                .setMaxResults(1)
                .getResultList();
        if (null == machines || machines.isEmpty()) {
            return true;
        } else {
            return machines.get(0).getMachineId().equals(strMachineId);
        }
    }

    /**
     * 機械日報登録/更新/削除時設備詳細更新(最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数)
     *
     * @param tblProduction
     * @param machineDailyReportVo
     * @param loginUser
     */
    @Transactional
    public void updateMstMachineForDailyReport(TblProduction tblProduction, TblMachineDailyReportVo machineDailyReportVo, LoginUser loginUser) {
        MstMachine machine = tblProduction.getMstMachine();
        if (machine != null) {
            if (machine.getLastProductionDate() == null || (machineDailyReportVo.getProductionDate() != null && machine.getLastProductionDate().compareTo(machineDailyReportVo.getProductionDate()) < 0)) {
                machine.setLastProductionDate(machineDailyReportVo.getProductionDate());
            }
            int shotCount = 0, disposedShotCount = 0, netProducintTimeMinutes = 0;
            if (null != machineDailyReportVo.getDisposedShotCount() && !machineDailyReportVo.getDisposedShotCount().isEmpty()) {
                disposedShotCount = Integer.parseInt(machineDailyReportVo.getDisposedShotCount());
            }
            if (null != machineDailyReportVo.getShotCount() && !machineDailyReportVo.getShotCount().isEmpty()) {
                shotCount = Integer.parseInt(machineDailyReportVo.getShotCount());
            }
            if (null != machineDailyReportVo.getNetProducintTimeMinutes() && !machineDailyReportVo.getNetProducintTimeMinutes().isEmpty()) {
                netProducintTimeMinutes = Integer.parseInt(machineDailyReportVo.getNetProducintTimeMinutes());
            }
            machine.setTotalShotCount(FileUtil.getIntegerValue(machine.getTotalShotCount())
                    + (shotCount - machineDailyReportVo.getShotCountBeforeUpd()) + (disposedShotCount - machineDailyReportVo.getDisposedShotCountBeforeUpd()));
            machine.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(machine.getAfterMainteTotalShotCount())
                    + (shotCount - machineDailyReportVo.getShotCountBeforeUpd()) + (disposedShotCount - machineDailyReportVo.getDisposedShotCountBeforeUpd()));


            BigDecimal bdMinutes = new BigDecimal(String.valueOf(netProducintTimeMinutes - machineDailyReportVo.getNetProducintTimeMinutesBeforeUpd()));
            BigDecimal bd60Minutes = new BigDecimal("60");
            BigDecimal bdHours = bdMinutes.divide(bd60Minutes, 1, BigDecimal.ROUND_HALF_UP);
            machine.setTotalProducingTimeHour(machine.getTotalProducingTimeHour() == null ? bdHours : machine.getTotalProducingTimeHour().add(bdHours));
            machine.setAfterMainteTotalProducingTimeHour(machine.getAfterMainteTotalProducingTimeHour() == null ? bdHours : machine.getAfterMainteTotalProducingTimeHour().add(bdHours));
            machine.setUpdateDate(new Date());
            machine.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(machine);
        }
    }

    /**
     * 設備詳細画面で下記項目について、いずれか更新されたら、設備候補テーブルに該当設備が未メンテのデータを削除を行う
     *
     * @param oldMstMachine
     * @param formMachine
     */
    public void deleteMachineMaintenanceRecomend(MstMachine oldMstMachine, MstMachine formMachine) {
        //下記項目について、いずれか更新されたら、設備候補テーブルに該当設備が未メンテのデータを削除を行う
        //最終メンテナンス日
        boolean recomendDelFlg = false;
        if (oldMstMachine.getLastMainteDate() == null && formMachine.getLastMainteDate() == null) {
            recomendDelFlg = false;
        } else if (oldMstMachine.getLastMainteDate() == null && formMachine.getLastMainteDate() != null) {
            recomendDelFlg = true;
        } else if (oldMstMachine.getLastMainteDate() != null && formMachine.getLastMainteDate() == null) {
            recomendDelFlg = true;
        } else if (oldMstMachine.getLastMainteDate() != null && formMachine.getLastMainteDate() != null) {
            if (oldMstMachine.getLastMainteDate().compareTo(formMachine.getLastMainteDate()) != 0) {
                recomendDelFlg = true;
            }
        }

        if (!recomendDelFlg) {
            //メンテナンス後生産時間
            if (oldMstMachine.getAfterMainteTotalProducingTimeHour() == null && formMachine.getAfterMainteTotalProducingTimeHour() == null) {
                recomendDelFlg = false;
            } else if (oldMstMachine.getAfterMainteTotalProducingTimeHour() == null && formMachine.getAfterMainteTotalProducingTimeHour() != null) {
                recomendDelFlg = true;
            } else if (oldMstMachine.getAfterMainteTotalProducingTimeHour() != null && formMachine.getAfterMainteTotalProducingTimeHour() == null) {
                recomendDelFlg = true;
            } else if (oldMstMachine.getAfterMainteTotalProducingTimeHour() != null && formMachine.getAfterMainteTotalProducingTimeHour() != null) {
                if (oldMstMachine.getAfterMainteTotalProducingTimeHour().compareTo(formMachine.getAfterMainteTotalProducingTimeHour()) != 0) {
                    recomendDelFlg = true;
                }
            }
        }
        if (!recomendDelFlg) {
            //メンテナンス後ショット数
            if (oldMstMachine.getAfterMainteTotalShotCount() == null && formMachine.getAfterMainteTotalShotCount() == null) {
                recomendDelFlg = false;
            } else if (oldMstMachine.getAfterMainteTotalShotCount() == null && formMachine.getAfterMainteTotalShotCount() != null) {
                recomendDelFlg = true;
            } else if (oldMstMachine.getAfterMainteTotalShotCount() != null && formMachine.getAfterMainteTotalShotCount() == null) {
                recomendDelFlg = true;
            } else if (oldMstMachine.getAfterMainteTotalShotCount() != null && formMachine.getAfterMainteTotalShotCount() != null) {
                if (oldMstMachine.getAfterMainteTotalShotCount().compareTo(formMachine.getAfterMainteTotalShotCount()) != 0) {
                    recomendDelFlg = true;
                }
            }
        }
        if (!recomendDelFlg) {
            //メンテサイクルコード01
            String oldMainteCycleId01 = FileUtil.getStringValue(oldMstMachine.getMainteCycleId01());
            String newMainteCycleId01 = FileUtil.getStringValue(formMachine.getMainteCycleId01());
            if (StringUtils.isEmpty(oldMainteCycleId01) && StringUtils.isEmpty(newMainteCycleId01)) {
                recomendDelFlg = false;
            } else if (StringUtils.isEmpty(oldMainteCycleId01) && StringUtils.isNotEmpty(newMainteCycleId01)) {
                recomendDelFlg = true;
            } else if (StringUtils.isNotEmpty(oldMainteCycleId01) && StringUtils.isEmpty(newMainteCycleId01)) {
                recomendDelFlg = true;
            } else if (StringUtils.isNotEmpty(oldMainteCycleId01) && StringUtils.isNotEmpty(newMainteCycleId01)) {
                if (oldMainteCycleId01.compareTo(newMainteCycleId01) != 0) {
                    recomendDelFlg = true;
                }
            }
        }
        if (!recomendDelFlg) {
            //メンテサイクルコード02
            String oldMainteCycleId02 = FileUtil.getStringValue(oldMstMachine.getMainteCycleId02());
            String newMainteCycleId02 = FileUtil.getStringValue(formMachine.getMainteCycleId02());
            if (StringUtils.isEmpty(oldMainteCycleId02) && StringUtils.isEmpty(newMainteCycleId02)) {
                recomendDelFlg = false;
            } else if (StringUtils.isEmpty(oldMainteCycleId02) && StringUtils.isNotEmpty(newMainteCycleId02)) {
                recomendDelFlg = true;
            } else if (StringUtils.isNotEmpty(oldMainteCycleId02) && StringUtils.isEmpty(newMainteCycleId02)) {
                recomendDelFlg = true;
            } else if (StringUtils.isNotEmpty(oldMainteCycleId02) && StringUtils.isNotEmpty(newMainteCycleId02)) {
                if (oldMainteCycleId02.compareTo(newMainteCycleId02) != 0) {
                    recomendDelFlg = true;
                }
            }
        }

        if (!recomendDelFlg) {
            //メンテサイクルコード03
            String oldMainteCycleId03 = FileUtil.getStringValue(oldMstMachine.getMainteCycleId03());
            String newMainteCycleId03 = FileUtil.getStringValue(formMachine.getMainteCycleId03());
            if (StringUtils.isEmpty(oldMainteCycleId03) && StringUtils.isEmpty(newMainteCycleId03)) {
                recomendDelFlg = false;
            } else if (StringUtils.isEmpty(oldMainteCycleId03) && StringUtils.isNotEmpty(newMainteCycleId03)) {
                recomendDelFlg = true;
            } else if (StringUtils.isNotEmpty(oldMainteCycleId03) && StringUtils.isEmpty(newMainteCycleId03)) {
                recomendDelFlg = true;
            } else if (StringUtils.isNotEmpty(oldMainteCycleId03) && StringUtils.isNotEmpty(newMainteCycleId03)) {
                if (oldMainteCycleId03.compareTo(newMainteCycleId03) != 0) {
                    recomendDelFlg = true;
                }
            }
        }
        if (recomendDelFlg) {
            tblMachineMaintenanceRecomendService.deleteTblMachineMaintenanceRecomendByMachineUuid(oldMstMachine.getUuid());
        }
    }

    /**
     * 設備情報を設備貸出先に送るため、カルテ間データ連携により設備マスタの送信を指示する。
     *
     * @param mstMachineSendList
     * @param loginUser
     * @return
     */
    public BasicResponse sendMachineToUsedCompany(MstMachineSendList mstMachineSendList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        MstExternalDataGetSetting externalCompany;//外部データ
        StringBuilder baseUrl = new StringBuilder();
        StringBuilder sendUrl = new StringBuilder();
        URL urlStr;
        Credential result = new Credential();
        if (mstMachineSendList != null) {
            List<MstMachine> mstMachines = mstMachineSendList.getMstMachines();//設備
            if (mstMachines == null || mstMachines.size() <= 0) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                return response;
            }
            // 受信者リストを設定する
            List<String> receiverList = new ArrayList();
            for (String mailAddress : mstMachineSendList.getMailAddress().split(",")) {
                if (FileUtil.isValidEmail(mailAddress)) {
                    receiverList.add(mailAddress);
                } else {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_not_valid_mail_address"));
                    return response;
                }
            }
            //会社ID照会外部データ取得設定
            Query queryMstcompany = entityManager.createNamedQuery("MstExternalDataGetSetting.findByCompanyId");
            queryMstcompany.setParameter("companyId", mstMachineSendList.getCompanyId());
            List<MstExternalDataGetSetting> listCompany = queryMstcompany.getResultList();
            if (listCompany != null && listCompany.size() > 0) {
                externalCompany = listCompany.get(listCompany.size() - 1);
                try {
                    sendUrl = sendUrl.append(externalCompany.getApiBaseUrl());
                    if (sendUrl.length() > 0) {
                        if (!sendUrl.toString().endsWith("/")) {
                            sendUrl = sendUrl.append("/");
                        }
                    }
                    //　相手のURLが有効かどうか検証
                    FileUtil.SSL();
                    urlStr = new URL(sendUrl.toString());
                    HttpURLConnection connection = (HttpURLConnection) urlStr.openConnection();

                    int state = connection.getResponseCode();
                    // ステータスコード404(Not Found)の場合
                    if (state != 200) {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_url_invalid"));
                        return response;
                    } else {
                        Credential credential = new Credential();
                        credential.setUserid(externalCompany.getUserId());
                        credential.setPassword(FileUtil.decrypt(externalCompany.getEncryptedPassword()));
                        baseUrl = baseUrl.append(sendUrl).append(EXT_LOGIN_URL).append(loginUser.getLangId());
                        result = FileUtil.sendPost(baseUrl.toString(), credential);
                        if (result.isError()) {
                            response.setError(result.isError());
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            if (StringUtils.isEmpty(result.getErrorMessage())) {
                                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_url_invalid"));
                            } else {
                                response.setErrorMessage(result.getErrorMessage());
                            }
                            return response;
                        }
                    }
                } catch (Exception e) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_url_invalid"));
                    return response;
                }
            } else {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                return response;
            }
            String subject;//メール件名設定
            String letterBody;//メール本文設定
            subject = mstMachineSendList.getSubject();
            letterBody = mstMachineSendList.getLetterBody();
            BasicResponse basicResponse = sendMachineInfoToUsedCompany(mstMachines, sendUrl.toString(), result.getToken(), loginUser);

            if (!basicResponse.isError()) {
                try {
                    //Remove duplicate data
                    Set<String> set = new HashSet<>();
                    set.addAll(receiverList);
                    receiverList = new ArrayList();
                    receiverList.addAll(set);
                    mailSender.setMakePlainTextBody(true);
                    mailSender.sendMail(receiverList, null, subject, letterBody);
                } catch (Exception e) {
                    Logger.getLogger(MstMachineService.class.getName()).log(Level.SEVERE, null, e);
                }
            } else {
                response.setError(basicResponse.isError());
                response.setErrorCode(basicResponse.getErrorCode());
                response.setErrorMessage(basicResponse.getErrorMessage());
                return response;
            }
        }
        return response;
    }

    /**
     *
     * @param mstMachines
     * @param sendUrl
     * @param resultToken
     * @param loginUser
     * @return
     */
    public BasicResponse sendMachineInfoToUsedCompany(
            List<MstMachine> mstMachines,
            String sendUrl,
            String resultToken,
            LoginUser loginUser
    ) {
        BasicResponse response = new BasicResponse();
        StringBuilder baseUrl = new StringBuilder();
        TblMachineReceptionList tblMachineReceptionList = new TblMachineReceptionList();
        List<TblMachineReceptionVo> vos = new ArrayList();

        // 実施者
        Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
        userQuery.setParameter("uuid", loginUser.getUserUuid());
        MstUser users = (MstUser) userQuery.getSingleResult();
        TblMachineReceptionVo tblMachineReceptionVo;
        for (MstMachine mstMachine : mstMachines) {
            MstMachine mstMachineFind = entityManager.find(MstMachine.class, mstMachine.getMachineId());
            if (mstMachineFind != null) {
                tblMachineReceptionVo = new TblMachineReceptionVo();//設備受信テーブル
                tblMachineReceptionVo.setMachineId(mstMachineFind.getMachineId());//設備ID
                tblMachineReceptionVo.setMachineName(mstMachineFind.getMachineName());//設備名称
                tblMachineReceptionVo.setOwnerContactName(users.getUserName() == null ? "" : users.getUserName()); //所有会社担当者
                vos.add(tblMachineReceptionVo);
            }
            tblMachineReceptionList.setTblMachineReceptionVos(vos);
        }

        try {
            baseUrl = baseUrl.append(sendUrl).append(EXT_MACHINE_RECEPTION_UPDATE);
            String str = FileUtil.sendPost(baseUrl.toString(), resultToken, tblMachineReceptionList);
            Gson gson = new Gson();
            response = gson.fromJson(str, new TypeToken<BasicResponse>() {
            }.getType());
        } catch (Exception e) {
            Logger.getLogger(MstMachineService.class.getName()).log(Level.SEVERE, null, e);
        }

        return response;
    }

    /**
     * 設備送信入力
     *
     * @param fileUuid
     * @param loginUser
     * @return
     */
    public MstMachineSendList sendImportCsv(String fileUuid, LoginUser loginUser) {
        MstMachineSendList mstMachineSendList = new MstMachineSendList();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            mstMachineSendList.setError(true);
            mstMachineSendList.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            mstMachineSendList.setErrorMessage(msg);
            return mstMachineSendList;
        }

        ArrayList readList = CSVFileUtil.readCsv(csvFile);

        if (readList.size() <= 1) {
            return mstMachineSendList;
        } else {
            List<MstMachine> mstMachines = new ArrayList();
            List<String> machineIds = new ArrayList();
            StringBuilder sql;
            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);
                machineIds.add(String.valueOf(comList.get(0)).trim());
            }
            sql = new StringBuilder("SELECT m FROM MstMachine m WHERE m.machineId IN :machineIds");
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("machineIds", machineIds);
            
            List<MstMachine> list = query.getResultList();
            if(list != null && list.size() > 0) {
                for(MstMachine mstMachine : list) {
                    mstMachine.setMachineId(mstMachine.getMachineId());//設備ID;
                    mstMachine.setMachineName(mstMachine.getMachineName());//設備名称;
                    mstMachines.add(mstMachine);
                }
            }
            mstMachineSendList.setMstMachines(mstMachines);
        }
        return mstMachineSendList;
    }
    
    /**
     * Σ軍師対応設備一覧
     *
     * @param machineId
     * @param machineName
     * @param machineType
     * @param department
     * @return
     */
    public MstMachineList getMstMachineSigmaNotNull(
            String machineId,
            String machineName,
            Integer machineType,
            Integer department
    ) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT mstMachine FROM MstMachine mstMachine "
                + "WHERE mstMachine.sigmaId IS NOT NULL ");
        
        // パラメータに応じてwhere条件追加
        // 設備ID
        if (!StringUtils.isEmpty(machineId)) {
            sql = sql.append(" AND mstMachine.machineId like :machineId ");
        }
        // 設備名称
        if (!StringUtils.isEmpty(machineName)) {
            sql = sql.append(" AND mstMachine.machineName like :machineName ");
        }
        // 設備種類
        if (machineType != null && machineType > 0) {
            sql = sql.append(" and mstMachine.machineType = :machineType ");
        }
        // 所属
        if (department != null && department > 0) {
            sql = sql.append(" and mstMachine.department = :department ");
        }
        
        sql = sql.append("order by mstMachine.machineId");
        Query query = entityManager.createQuery(sql.toString());
        
        // パラメータに応じて追加したwhere条件にパラメータの値セット
        // 設備ID
        if (!StringUtils.isEmpty(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }
        // 設備名称
        if (!StringUtils.isEmpty(machineName)) {
            query.setParameter("machineName", "%" + machineName + "%");
        }
        // 設備種類
        if (machineType != null && machineType > 0) {
            query.setParameter("machineType", machineType);
        }
        // 所属
        if (department != null && department > 0) {
            query.setParameter("department", department);
        }
        
        List list = query.getResultList();
        MstMachineList response = new MstMachineList();
        response.setMstMachines(list);
        return response;
    }
    
    public MstMachineList getMstMachinesByPage(String machineId, String machineName, String mainAssetNo,
            String ownerCompanyName, String companyName, String locationName, String instllationSiteName,
            Integer machineType, Integer department, Date lastProductionDateFrom, Date lastProductionDateTo,
            Integer totalProducingTimeHourFrom, Integer totalProducingTimeHourTo, Integer totalShotCountFrom,
            Integer totalShotCountTo, Date lastMainteDateFrom, Date lastMainteDateTo,
            Integer afterMainteTotalProducingTimeHourFrom, Integer afterMainteTotalProducingTimeHourTo,
            Integer afterMainteTotalShotCountFrom, Integer afterMainteTotalShotCountTo, Date machineCreatedDateFrom,
            Date machineCreatedDateTo, Integer status, String orderByMachineName, LoginUser loginUser, String sidx,
            String sord, int pageNumber, int pageSize, boolean isPage) {
        
        MstChoiceList mstDepartmentChoiceList = mstChoiceService.getChoice(loginUser.getLangId(),
                "mst_user.department");
        
        MstMachineList mstMachineDetailList = new MstMachineList();
        List<MstMachineVo> mstMachineVos = new ArrayList<>();
        
        if (isPage) {

            List count = getSqlByPage(machineId, machineName, mainAssetNo, ownerCompanyName, companyName, locationName,
                    instllationSiteName, machineType, department, lastProductionDateFrom, lastProductionDateTo,
                    totalProducingTimeHourFrom, totalProducingTimeHourTo, totalShotCountFrom, totalShotCountTo,
                    lastMainteDateFrom, lastMainteDateTo, afterMainteTotalProducingTimeHourFrom,
                    afterMainteTotalProducingTimeHourTo, afterMainteTotalShotCountFrom, afterMainteTotalShotCountTo,
                    machineCreatedDateFrom, machineCreatedDateTo, status, orderByMachineName, sidx, sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            mstMachineDetailList.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            mstMachineDetailList.setCount(counts);
            mstMachineDetailList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }
        
        List<MstMachine> list = getSqlByPage(machineId, machineName, mainAssetNo, ownerCompanyName, companyName, locationName,
                instllationSiteName, machineType, department, lastProductionDateFrom, lastProductionDateTo,
                totalProducingTimeHourFrom, totalProducingTimeHourTo, totalShotCountFrom, totalShotCountTo,
                lastMainteDateFrom, lastMainteDateTo, afterMainteTotalProducingTimeHourFrom,
                afterMainteTotalProducingTimeHourTo, afterMainteTotalShotCountFrom, afterMainteTotalShotCountTo,
                machineCreatedDateFrom, machineCreatedDateTo, status, orderByMachineName, sidx, sord, pageNumber, pageSize, false);
        
        FileUtil fu = new FileUtil();
        for (MstMachine mstMachine : list) {
            MstMachineVo mstMachineVo = new MstMachineVo();
            mstMachineVo.setMachineUuid(mstMachine.getUuid());
            mstMachineVo.setMachineId(mstMachine.getMachineId());
            mstMachineVo.setMachineName(mstMachine.getMachineName());

            if (mstMachine.getCompanyId() != null && !"".equals(mstMachine.getCompanyId())) {
                if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", mstMachine.getCompanyId(),
                        loginUser).isError() == true) {
                    mstMachineVo.setMachineTypeText(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(),
                            "mst_machine.machine_type", String.valueOf(mstMachine.getMachineType()),
                            loginUser.getLangId()));
                    mstMachineVo.setExternalFlg(1);
                } else if (mstMachine.getMachineType() != null) {
                    mstMachineVo.setExternalFlg(0);
                    mstMachineVo.setMachineType(mstMachine.getMachineType());
                }
            } else {
                mstMachineVo.setExternalFlg(0);
                if (mstMachine.getMachineType() != null) {
                    mstMachineVo.setMachineType(mstMachine.getMachineType());
                    // mstMachineVo.setMachineTypeText(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(),
                    // "mst_machine.machine_type",
                    // String.valueOf(mstMachine.getMachineType()),
                    // loginUser.getLangId()));
                }
            }

            if (mstMachine.getMainAssetNo() != null && !"".equals(mstMachine.getMainAssetNo().trim())) {
                mstMachineVo.setMainAssetNo(mstMachine.getMainAssetNo());
            } else {
                mstMachineVo.setMainAssetNo("");
            }
            if (null != mstMachine.getMachineCreatedDate()) {
                mstMachineVo.setMachineCreatedDateStr(
                        new FileUtil().getDateFormatForStr(mstMachine.getMachineCreatedDate()));
            }
            if (mstMachine.getInspectedDate() != null) {
                mstMachineVo.setInspectedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getInspectedDate()));
            }
            if (mstMachine.getOwnerMstCompany() != null) {
                mstMachineVo.setOwnerCompanyId(mstMachine.getOwnerMstCompany().getId());
                mstMachineVo.setOwnerCompanyName(mstMachine.getOwnerMstCompany().getCompanyName());
            } else {
                mstMachineVo.setOwnerCompanyId("");
                mstMachineVo.setOwnerCompanyName("");
            }
            if (mstMachine.getInstalledDate() != null) {
                mstMachineVo.setInstalledDateStr(new FileUtil().getDateFormatForStr(mstMachine.getInstalledDate()));
            }

            if (null != mstMachine.getMstCompany()) {
                mstMachineVo.setCompanyId(mstMachine.getMstCompany().getId());
                mstMachineVo.setCompanyName(mstMachine.getMstCompany().getCompanyName());
            } else {
                mstMachineVo.setCompanyId("");
                mstMachineVo.setCompanyName("");
            }

            if (null != mstMachine.getMstLocation()) {
                mstMachineVo.setLocationId(mstMachine.getMstLocation().getId());
                mstMachineVo.setLocationName(mstMachine.getMstLocation().getLocationName());
            } else {
                mstMachineVo.setLocationId("");
                mstMachineVo.setLocationName("");
            }

            if (null != mstMachine.getMstInstallationSite()) {
                mstMachineVo.setInstllationSiteId(mstMachine.getMstInstallationSite().getId());
                mstMachineVo.setInstllationSiteName(mstMachine.getMstInstallationSite().getInstallationSiteName());
            } else {
                mstMachineVo.setInstllationSiteId("");
                mstMachineVo.setInstllationSiteName("");
            }

            if (mstMachine.getStatus() != null) {
                mstMachineVo.setStatus(mstMachine.getStatus());
            }
            if (mstMachine.getStatusChangedDate() != null) {
                mstMachineVo
                        .setStatusChangedDateStr(new FileUtil().getDateFormatForStr(mstMachine.getStatusChangedDate()));
            }
            if (mstMachine.getDepartment() != null) {
                mstMachineVo.setDepartmentId("" + mstMachine.getDepartment());
                for (MstChoice mstChoice : mstDepartmentChoiceList.getMstChoice()) {
                    if (mstChoice.getMstChoicePK().getSeq().equals(mstMachineVo.getDepartmentId())) {
                        mstMachineVo.setDepartmentName(mstChoice.getChoice());
                        break;
                    }
                }
            }
            // 4.2 Zhangying S
            // 最終生産日
            if (mstMachine.getLastProductionDate() != null) {
                mstMachineVo.setLastProductionDate(mstMachine.getLastProductionDate());
                mstMachineVo.setLastProductionDateStr(fu.getDateFormatForStr(mstMachine.getLastProductionDate()));
            } else {
                mstMachineVo.setLastProductionDate(null);
                mstMachineVo.setLastProductionDateStr("");
            }
            // 累計生産時間
            if (mstMachine.getTotalProducingTimeHour() != null) {
                mstMachineVo.setTotalProducingTimeHour(mstMachine.getTotalProducingTimeHour());
            } else {
                mstMachineVo.setTotalProducingTimeHour(BigDecimal.ZERO);
            }
            // 累計ショット数
            if (mstMachine.getTotalShotCount() != null) {
                mstMachineVo.setTotalShotCount("" + mstMachine.getTotalShotCount());
            } else {
                mstMachineVo.setTotalShotCount("0");
            }
            // 最終メンテナンス日
            if (mstMachine.getLastMainteDate() != null) {
                mstMachineVo.setLastMainteDate(mstMachine.getLastMainteDate());
                mstMachineVo.setLastMainteDateStr(fu.getDateFormatForStr(mstMachine.getLastMainteDate()));
            } else {
                mstMachineVo.setLastMainteDate(null);
                mstMachineVo.setLastMainteDateStr("");
            }
            // メンテナンス後生産時間
            if (mstMachine.getAfterMainteTotalProducingTimeHour() != null) {
                mstMachineVo.setAfterMainteTotalProducingTimeHour(mstMachine.getAfterMainteTotalProducingTimeHour());
            } else {
                mstMachineVo.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
            }
            // メンテナンス後ショット数
            if (mstMachine.getAfterMainteTotalShotCount() != null) {
                mstMachineVo.setAfterMainteTotalShotCount(mstMachine.getAfterMainteTotalShotCount());
            } else {
                mstMachineVo.setAfterMainteTotalShotCount(0);
            }

            if (null != mstMachine.getBlMaintenanceCyclePtn01()) {
                // メンテサイクルコード01
                mstMachineVo.setMainteCycleCode01(
                        mstMachine.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                mstMachineVo.setMainteCycleCode01("");
            }
            if (null != mstMachine.getBlMaintenanceCyclePtn02()) {
                // メンテサイクルコード02
                mstMachineVo.setMainteCycleCode02(
                        mstMachine.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                mstMachineVo.setMainteCycleCode02("");
            }
            if (null != mstMachine.getBlMaintenanceCyclePtn03()) {
                // メンテサイクルコード03
                mstMachineVo.setMainteCycleCode03(
                        mstMachine.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
            } else {
                mstMachineVo.setMainteCycleCode03("");
            }
            // 4.2 Zhangying E
            MstSigma mstSigma = null;
            if (null != (mstSigma = mstMachine.getMstSigma())) {
                mstMachineVo.setSigmaId(mstSigma.getId());
                mstMachineVo.setSigmaCode(mstSigma.getSigmaCode());
            }

            if (mstMachine.getMacKey() != null && !"".equals(mstMachine.getMacKey().trim())) {
                mstMachineVo.setMacKey(mstMachine.getMacKey());
            }
            if (null != mstMachine.getBaseCycleTime()) {
                mstMachineVo.setBaseCycleTime("" + mstMachine.getBaseCycleTime());
            }
            if (mstMachine.getMachineCd() != null && !"".equals(mstMachine.getMachineCd().trim())) {
                mstMachineVo.setMachineCd(mstMachine.getMachineCd());
            }
            if (mstMachine.getStrageLocationCd() != null && !"".equals(mstMachine.getStrageLocationCd().trim())) {
                mstMachineVo.setStrageLocationCd(mstMachine.getStrageLocationCd());
            }
            if (mstMachine.getChargeCd() != null && !"".equals(mstMachine.getChargeCd().trim())) {
                mstMachineVo.setChargeCd(mstMachine.getChargeCd());
            }
            if (mstMachine.getOperatorCd() != null && !"".equals(mstMachine.getOperatorCd().trim())) {
                mstMachineVo.setOperatorCd(mstMachine.getOperatorCd());
            }

            mstMachineVos.add(mstMachineVo);
        }
        mstMachineDetailList.setMstMachineVos(mstMachineVos);
        return mstMachineDetailList;

    }
    
    private List getSqlByPage(String machineId, String machineName, String mainAssetNo, String ownerCompanyName,
            String companyName, String locationName, String instllationSiteName, Integer machineType,
            Integer department, Date lastProductionDateFrom, Date lastProductionDateTo,
            Integer totalProducingTimeHourFrom, Integer totalProducingTimeHourTo, Integer totalShotCountFrom,
            Integer totalShotCountTo, Date lastMainteDateFrom, Date lastMainteDateTo,
            Integer afterMainteTotalProducingTimeHourFrom, Integer afterMainteTotalProducingTimeHourTo,
            Integer afterMainteTotalShotCountFrom, Integer afterMainteTotalShotCountTo, Date machineCreatedDateFrom,
            Date machineCreatedDateTo, Integer status, String orderByMachineName, String sidx, String sord,
            int pageNumber, int pageSize, boolean isCount) {

        StringBuilder sql;

        if (isCount) {
            sql = new StringBuilder("SELECT count(m.machineId) ");
        } else {
            sql = new StringBuilder("SELECT m ");
        }

        sql = sql.append(" FROM MstMachine m " + "LEFT JOIN FETCH m.ownerMstCompany oc "
                + "LEFT JOIN FETCH m.mstCompany c " + "LEFT JOIN FETCH m.mstInstallationSite isite "
                + "LEFT JOIN FETCH m.mstLocation location " + "LEFT JOIN FETCH m.blMaintenanceCyclePtn01 cyclePtn01 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn02 cyclePtn02 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn03 cyclePtn03 " + "LEFT JOIN FETCH m.mstSigma s "
                + " WHERE 1=1 ");

        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" AND m.machineId like :machineId ");
        }
        if (machineName != null && !"".equals(machineName)) {
            sql = sql.append(" AND m.machineName like :machineName ");
        }

        // 資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            sql = sql.append(" AND m.mainAssetNo LIKE :mainAssetNo ");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" and oc.companyName like :ownerCompanyName ");
        }

        if (companyName != null && !"".equals(companyName)) {
            sql = sql.append(" and c.companyName like :companyName ");
        }

        if (locationName != null && !"".equals(locationName)) {
            sql = sql.append(" and location.locationName like :locationName ");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            sql = sql.append(" and isite.installationSiteName like :instllationSiteName ");
        }

        if (machineType != null && 0 != machineType) {
            sql = sql.append(" and m.machineType = :machineType ");
        }

        if (department != null && 0 != department) {
            sql = sql.append(" and m.department = :department ");
        }

        if (lastProductionDateFrom != null) {
            sql = sql.append(" and m.lastProductionDate >= :lastProductionDateFrom ");
        }

        if (lastProductionDateTo != null) {
            sql = sql.append(" and m.lastProductionDate <= :lastProductionDateTo ");
        }

        if (totalProducingTimeHourFrom != null) {
            sql = sql.append(" and m.totalProducingTimeHour >= :totalProducingTimeHourFrom ");
        }

        if (totalProducingTimeHourTo != null) {
            sql = sql.append(" and m.totalProducingTimeHour <= :totalProducingTimeHourTo ");
        }

        if (totalShotCountFrom != null) {
            sql = sql.append(" and m.totalShotCount >= :totalShotCountFrom ");
        }

        if (totalShotCountTo != null) {
            sql = sql.append(" and m.totalShotCount <= :totalShotCountTo ");
        }

        if (lastMainteDateFrom != null) {
            sql = sql.append(" and m.lastMainteDate >= :lastMainteDateFrom ");
        }

        if (lastMainteDateTo != null) {
            sql = sql.append(" and m.lastMainteDate <= :lastMainteDateTo ");
        }

        if (afterMainteTotalProducingTimeHourFrom != null) {
            sql = sql.append(" and m.afterMainteTotalProducingTimeHour >= :afterMainteTotalProducingTimeHourFrom ");
        }

        if (afterMainteTotalProducingTimeHourTo != null) {
            sql = sql.append(" and m.afterMainteTotalProducingTimeHour <= :afterMainteTotalProducingTimeHourTo ");
        }

        if (afterMainteTotalShotCountFrom != null) {
            sql = sql.append(" and m.afterMainteTotalShotCount >= :afterMainteTotalShotCountFrom ");
        }

        if (afterMainteTotalShotCountTo != null) {
            sql = sql.append(" and m.afterMainteTotalShotCount <= :afterMainteTotalShotCountTo ");
        }

        if (machineCreatedDateFrom != null) {
            sql = sql.append(" and m.machineCreatedDate >= :machineCreatedDateFrom ");
        }

        if (machineCreatedDateTo != null) {
            sql = sql.append(" and m.machineCreatedDate <= :machineCreatedDateTo ");
        }

        if (status != null) {
            sql = sql.append(" and m.status = :status ");
        }

        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                // 表示順は設備IDの昇順。
                sql.append(sortStr);

            } else {

                // 表示順は設備IDの昇順
                sql.append(" order by m.machineId ");

            }

        }

        Query query = entityManager.createQuery(sql.toString());

        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }

        if (machineName != null && !"".equals(machineName)) {
            query.setParameter("machineName", "%" + machineName + "%");
        }

        // 資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            query.setParameter("mainAssetNo", "%" + mainAssetNo + "%");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            query.setParameter("ownerCompanyName", "%" + ownerCompanyName + "%");
        }

        if (companyName != null && !"".equals(companyName)) {
            query.setParameter("companyName", "%" + companyName + "%");
        }

        if (locationName != null && !"".equals(locationName)) {
            query.setParameter("locationName", "%" + locationName + "%");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            query.setParameter("instllationSiteName", "%" + instllationSiteName + "%");
        }

        if (machineType != null && 0 != machineType) {
            query.setParameter("machineType", machineType);
        }

        if (department != null && 0 != department) {
            query.setParameter("department", department);
        }

        if (lastProductionDateFrom != null) {
            query.setParameter("lastProductionDateFrom", lastProductionDateFrom);
        }
        if (lastProductionDateTo != null) {
            query.setParameter("lastProductionDateTo", lastProductionDateTo);
        }
        if (totalProducingTimeHourFrom != null) {
            query.setParameter("totalProducingTimeHourFrom", totalProducingTimeHourFrom);
        }
        if (totalProducingTimeHourTo != null) {
            query.setParameter("totalProducingTimeHourTo", totalProducingTimeHourTo);
        }
        if (totalShotCountFrom != null) {
            query.setParameter("totalShotCountFrom", totalShotCountFrom);
        }
        if (totalShotCountTo != null) {
            query.setParameter("totalShotCountTo", totalShotCountTo);
        }
        if (lastMainteDateFrom != null) {
            query.setParameter("lastMainteDateFrom", lastMainteDateFrom);
        }
        if (lastMainteDateTo != null) {
            query.setParameter("lastMainteDateTo", lastMainteDateTo);
        }
        if (afterMainteTotalProducingTimeHourFrom != null) {
            query.setParameter("afterMainteTotalProducingTimeHourFrom", afterMainteTotalProducingTimeHourFrom);
        }
        if (afterMainteTotalProducingTimeHourTo != null) {
            query.setParameter("afterMainteTotalProducingTimeHourTo", afterMainteTotalProducingTimeHourTo);
        }
        if (afterMainteTotalShotCountFrom != null) {
            query.setParameter("afterMainteTotalShotCountFrom", afterMainteTotalShotCountFrom);
        }
        if (afterMainteTotalShotCountTo != null) {
            query.setParameter("afterMainteTotalShotCountTo", afterMainteTotalShotCountTo);
        }

        if (machineCreatedDateFrom != null) {
            query.setParameter("machineCreatedDateFrom", machineCreatedDateFrom);
        }

        if (machineCreatedDateTo != null) {
            query.setParameter("machineCreatedDateTo", machineCreatedDateTo);
        }

        if (status != null) {
            query.setParameter("status", status);
        }
        
        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();

        return list;

    }

    /**
     * 設備棚卸画像アップロード
     *
     * @param uploadFile
     * @param uploadFileDetail
     * @param outputFileUuid
     * @param imageFileKey
     * @param langId
     * @param userUuid
     * @return
     */
    @Transactional
    public BasicResponse machineStocktakePicture(
            InputStream uploadFile,
            FormDataContentDisposition uploadFileDetail,
            String outputFileUuid,
            String imageFileKey,
            String langId,
            String userUuid
    ) {
        BasicResponse basicResponse = new BasicResponse();
        try {

            if (StringUtils.isEmpty(outputFileUuid)) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_not_null_with_item");
                msg = String.format(msg, mstDictionaryService.getDictionaryValue(langId, "output_file_uuid"));
                basicResponse.setErrorMessage(msg);
                return basicResponse;
            }
            if (StringUtils.isEmpty(imageFileKey)) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_not_null_with_item");
                msg = String.format(msg, mstDictionaryService.getDictionaryValue(langId, "image_file_key"));
                basicResponse.setErrorMessage(msg);
                return basicResponse;
            }

            if (uploadFile == null) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_not_null_with_item");
                msg = String.format(msg, mstDictionaryService.getDictionaryValue(langId, "upload_file"));
                basicResponse.setErrorMessage(msg);
                return basicResponse;
            }

            // 1 設備棚卸画像ファイルアップロードAPIは受け取った画像ファイルをサーバーに格納し
            StringBuffer path = new StringBuffer(kartePropertyService.getDocumentDirectory());
            path = path.append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE);

            String uuid = IDGenerator.generate();
            // 2 fileUuid を設備棚卸結果テーブルに保持する。
            // その際、ネイティブアプリから送信された出力ファイルUUIDと画像ファイルユニークキーをレコード特定のキーとする。
            Query query = entityManager.createQuery("SELECT machineInventory FROM TblMachineInventory machineInventory "
                    + " WHERE machineInventory.outputFileUuid = :outputFileUuid AND machineInventory.imageFileKey = :imageFileKey");

            query.setParameter("outputFileUuid", outputFileUuid);
            query.setParameter("imageFileKey", imageFileKey);
            try {
                TblMachineInventory tblMachineInventory = (TblMachineInventory) query.getSingleResult();
                tblMachineInventory.setImgFilePath(uuid);
                tblMachineInventory.setUpdateDate(new Date());
                tblMachineInventory.setUpdateUserUuid(userUuid);
                entityManager.merge(tblMachineInventory);
            } catch (NoResultException e) {
                //outputFileUuid, imageFileKeyで棚卸レコードが見つからない場合でもResponseのerrorをtrueにしない。errorCode, errorMessageもセットしない。
                //basicResponse.setError(true);
                //basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                //String msg = mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found_with_item");
                //basicResponse.setErrorMessage(msg);
                return basicResponse;
            }
            String getFileType = uploadFileDetail.getFileName();
            getFileType = getFileType.substring(getFileType.lastIndexOf(".") + 1);
            FileUtil fu = new FileUtil();

            fu.createPath(path.toString());
            path = path.append(FileUtil.SEPARATOR).append(uuid).append(".").append(getFileType);

            fu.createFile(path.toString());
            fu.writeToFile(uploadFile, path.toString());

        } catch (Exception e) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(e.getMessage());
        }
        return basicResponse;
    }

    /**
     *
     * @param searchConditionList
     * @param langId
     * @return
     */
    public MstMachineStoctake getMachineStocktakeResult(SearchConditionList searchConditionList, LoginUser loginUser) {

        //検索画面で指定された会社名称、所在地名称、設置場所名称、所属を表示する。
        StringBuilder sql = new StringBuilder("SELECT m FROM MstMachine m "
                + " LEFT JOIN FETCH m.mstCompany mstCompany "
                + " LEFT JOIN FETCH m.mstInstallationSite mstInstallationSite "
                + " LEFT JOIN FETCH m.ownerMstCompany mstCompanyByOwnerCompany "
                + " LEFT JOIN FETCH m.mstLocation mstLocation "
                + " LEFT JOIN FETCH m.latestTblMachineInventory tblMachineInventory "
                + " WHERE 1=1 ");

        //設備マスタより指定された会社、所在地、設置場所、所属にあてはまるレコードを検索し、
        if (searchConditionList != null) {
            if (searchConditionList.getSearchConditions() != null && searchConditionList.getSearchConditions().size() > 0) {
                int searchConditionCount = searchConditionList.getSearchConditions().size();
                sql.append(" AND ( 1=1 ");
                for (int i = 0; i < searchConditionCount; i++) {
                    int j = i + 1;

                    if (i > 0 && j <= searchConditionCount) {
                        sql.append(" OR ( 1=1 ");
                    }
                    SearchCondition searchCondition = searchConditionList.getSearchConditions().get(i);
                    // 会社、所在地のみ必須とし、設置場所名称、所属、所有会社は任意とする。
                    if (StringUtils.isNotEmpty(searchCondition.getCompanyId())) {
                        sql.append(" AND mstCompany.id = :companyId").append(i);
                    }
                    if (StringUtils.isNotEmpty(searchCondition.getLocationId())) {
                        sql.append(" AND mstLocation.id = :locationId").append(i);
                    }
                    if (StringUtils.isNotEmpty(searchCondition.getInstallationSiteId())) {
                        sql.append(" AND mstInstallationSite.id = :installationSiteId").append(i);
                    }
                    if (StringUtils.isNotEmpty(searchCondition.getOwnerCompanyId())) {
                        sql.append(" AND mstCompanyByOwnerCompany.id = :ownerCompanyId").append(i);
                    }
                    if (0 != searchCondition.getDepartment()) {
                        sql.append(" AND m.department =  :department").append(i);
                    }
                    if (1 == searchCondition.getInventoryNotDoneFlg()) {
                        sql.append(" AND m.inventoryStatus =  :inventoryStatus").append(i);//設備の棚卸ステータスが未実施であるもの
                    }
                    if (i > 0 && j <= searchConditionCount) {
                        sql.append(" ) ");
                    }
                }
                sql.append(" ) ");
            }
        }
        // 設備IDの昇順で一覧表示する。
        sql.append(" ORDER BY m.machineId ");
        System.out.println(sql.toString());
        Query query = entityManager.createQuery(sql.toString());

        List<OutputCondition> outputConditions = new ArrayList<>();
        if (searchConditionList != null) {
            if (searchConditionList.getSearchConditions() != null && searchConditionList.getSearchConditions().size() > 0) {

                OutputCondition outputCondition;
                int searchConditionCount = searchConditionList.getSearchConditions().size();

                for (int i = 0; i < searchConditionCount; i++) {

                    SearchCondition searchCondition = searchConditionList.getSearchConditions().get(i);
                    outputCondition = new OutputCondition();
                    outputCondition.setCompanyName(FileUtil.getStr(searchCondition.getCompanyName()));
                    outputCondition.setLocationName(FileUtil.getStr(searchCondition.getLocationName()));
                    outputCondition.setInstallationSiteName(FileUtil.getStr(searchCondition.getInstallationSiteName()));
                    outputCondition.setDepartmentName(FileUtil.getStr(searchCondition.getDepartmentName()));
                    outputConditions.add(outputCondition);

                    // 会社、所在地のみ必須とし、設置場所名称、所属、所有会社は任意とする。
                    if (StringUtils.isNotEmpty(searchCondition.getCompanyId())) {
                        query.setParameter("companyId" + i, searchCondition.getCompanyId());
                    }
                    if (StringUtils.isNotEmpty(searchCondition.getLocationId())) {
                        query.setParameter("locationId" + i, searchCondition.getLocationId());
                    }

                    if (StringUtils.isNotEmpty(searchCondition.getInstallationSiteId())) {
                        query.setParameter("installationSiteId" + i, searchCondition.getInstallationSiteId());
                    }
                    if (StringUtils.isNotEmpty(searchCondition.getOwnerCompanyId())) {
                        query.setParameter("ownerCompanyId" + i, searchCondition.getOwnerCompanyId());
                    }

                    if (0 < searchCondition.getDepartment()) {
                        query.setParameter("department" + i, searchCondition.getDepartment());
                    }
                    if (1 == searchCondition.getInventoryNotDoneFlg()) {
                        query.setParameter("inventoryStatus" + i, 0);
                    }
                }
            }
        }
        List list = query.getResultList();

        List<Machine> machines = new ArrayList<>();
        if (!list.isEmpty()) {
            String choiceKey = "mst_user.department";
            Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{choiceKey});
            for (int i = 0; i < list.size(); i++) {
                //mstMachineStoctake 
                MstMachine mstMachine = (MstMachine) list.get(i);
                Machine machine = new Machine();
                machine.setMachineId(mstMachine.getMachineId());//設備ID
                machine.setMachineName(mstMachine.getMachineName());//設備名称

                MstCompany mstCompany = mstMachine.getMstCompany();
                if (mstCompany != null) {
                    machine.setCompanyCode(mstCompany.getCompanyCode());//会社コード
                    machine.setCompanyName(mstCompany.getCompanyName());//会社名称
                } else {
                    machine.setCompanyCode("");//会社コード
                    machine.setCompanyName("");//会社名称
                }

                MstLocation mstLocation = mstMachine.getMstLocation();
                if (mstLocation != null) {
                    machine.setLocationCode(mstLocation.getLocationCode());//所在地コード
                    machine.setLocationName(mstLocation.getLocationName());//所在地名称
                } else {
                    machine.setLocationCode("");//所在地コード
                    machine.setLocationName("");//所在地名称
                }

                MstInstallationSite mstInstallationSite = mstMachine.getMstInstallationSite();
                if (mstInstallationSite != null) {
                    machine.setInstallationSiteCode(mstInstallationSite.getInstallationSiteCode());//設置場所コード
                    machine.setInstallationSiteName(mstInstallationSite.getInstallationSiteName());//設置場所名称
                } else {
                    machine.setInstallationSiteCode("");//設置場所コード
                    machine.setInstallationSiteName("");//設置場所名称
                }

                machine.setDepartment(mstMachine.getDepartment());//所属番号
                machine.setDepartmentName(FileUtil.getStr(map.get(choiceKey + String.valueOf(mstMachine.getDepartment()))));//所属名称
                machine.setStocktakeStatus(mstMachine.getInventoryStatus());
                machine.setNotes("");//備考
                machine.setImageFileKey("");//画像ファイルユニークキー *2
                machine.setQrPlateInfo("");
                
                if (machine.getStocktakeStatus() == 1) {//棚卸ステータスが1(実施済み)の場合
                    TblMachineInventory tblMachineInventory = mstMachine.getLatestTblMachineInventory();
                    if (tblMachineInventory != null) {
                        machine.setStocktakeResult(tblMachineInventory.getInventoryResult());//棚卸結果
                        machine.setStocktakeDatetime(tblMachineInventory.getInventoryDate());//棚卸日時
                        machine.setStocktakeMethod(tblMachineInventory.getMachineConfirmMethod());//確認方法
                        machine.setNotes(tblMachineInventory.getRemarks());//備考
                        machine.setChangeDepartment(tblMachineInventory.getDepartmentChange());//部署変更
                        machine.setBarcodeReprint(tblMachineInventory.getBarcodeReprint());//資産シール再発行要否
                        machine.setAssetDamaged(tblMachineInventory.getAssetDamaged());//故障
                        machine.setNotInUse(tblMachineInventory.getNotInUse());//遊休
                    }
                }
                
                machines.add(machine);
            }
        }
        MachineStocktake machineStoctake = new MachineStocktake();
        machineStoctake.setMachines(machines);
        machineStoctake.setOutputConditions(outputConditions);//出力条件ルート
        machineStoctake.setApiBaseUrl(kartePropertyService.getBaseUrl() + "/ws/karte/api/");
        machineStoctake.setOutputFileUuid(IDGenerator.generate());//出力ファイルUUID。このファイルのユニークキー
        MstUser mstUser = entityManager.find(MstUser.class, loginUser.getUserid());
        if (mstUser != null) {
            machineStoctake.setOutputPersonName(mstUser.getUserName());        //ファイルを出力した人の名称
        } else {
            machineStoctake.setOutputPersonName("");        //ファイルを出力した人の名称
        }
        machineStoctake.setOutputPersonUuid(loginUser.getUserUuid());//ファイルを出力した人のユニークキー
        machineStoctake.setOutputPersonUserId(loginUser.getUserid());   //ファイルを出力した人のユーザーID
        try {
            SimpleDateFormat format = new SimpleDateFormat(DateFormat.DATETIME_EXPANSION_FORMAT);
            String dateString = format.format(new Date());
            Date date = format.parse(dateString);
            machineStoctake.setOutputDate(date);//ファイル出力日時
        } catch (ParseException ex) {
            Logger.getLogger(MstMachineService.class.getName()).log(Level.SEVERE, null, ex);
        }
        MstMachineStoctake mstMachineStoctake = new MstMachineStoctake();
        mstMachineStoctake.setMachineStocktake(machineStoctake);

        try {
            FileUtil fileUtil = new FileUtil();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat(DateFormat.DATETIME_EXPANSION_FORMAT);
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();
            String fileName = "karte-machine-stocktake-" + machineStoctake.getOutputFileUuid();
            String outJsonPath = FileUtil.outJsonFile(kartePropertyService, fileName);
            String str = gson.toJson(mstMachineStoctake);
            fileUtil.writeInfoToFile(outJsonPath, str);

            TblCsvExport tblCsvExport = new TblCsvExport();
            tblCsvExport.setFileUuid(machineStoctake.getOutputFileUuid());
            tblCsvExport.setExportTable("mst_machine");
            tblCsvExport.setExportDate(new Date());
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId("25000");
            tblCsvExport.setFunctionId(mstFunction);
            tblCsvExport.setExportUserUuid(loginUser.getUserUuid());

            tblCsvExport.setClientFileName(fileName + CommonConstants.EXT_JSON);
            tblCsvExportService.createTblCsvExport(tblCsvExport);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mstMachineStoctake;
    }

    /**
     *
     * @param langId
     * @return
     */
    public Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList(
                "row_number",
                "machine_id",
                "machine",
                "error",
                "error_detail",
                "msg_error_not_null",
                "msg_record_added",
                "msg_report_name_multiple_records_found",
                "msg_error_machine_machine_asset_relation_not_null",
                "mst_error_record_not_found",
                "msg_error_value_invalid",
                "msg_error_over_length",
                "db_process",
                "company_code",
                "location_code",
                "installation_site_code",
                "stocktake_result_mail_title",
                "stocktake_result_mail_body",
                "machine_inventory_result",
                "machine_inventory_date",
                "machine_confirm_method",
                "reply_user_name",
                "inventory_object",
                "inventory_execution",
                "location_confirm",
                "location_unknown",
                "stocktake_result_error_mail_body",
                "stocktake_result_location_confirm_mail_body",
                "stocktake_result_location_unknown_mail_body",
                "output_file_id",
                "image_file_key",
                "machine_name",
                "user_department",
                "company_name",
                "location_name",
                "installation_site_name",
                "evidence_upload_time",
                "total_count",
                "remarks",
                "department_change",
                "asset_damaged",
                "not_in_use",
                "bar_code_reprint",
                "issue_taken_date",
                "error_msg",
                "msg_error_record_not_found_item",
                "msg_stocktake_result_imported",
                "msg_error_over_length_with_item",
                "msg_error_date_format_invalid_with_item",
                "msg_error_not_null_with_item",
                "msg_error_importes_result",
                "inventory_not_done",
                "stocktake_upload_result",
                "additional_flag",
                "qr_plate_info",
                "stocktake_result_location_exclude_mail_body",
                "stocktake_status",
                "production_date",
                "production_time_minutes",
                "shot_count",
                "correction_flg",
                "mold_or_machine_production_result_add",
                "msg_data_modified",
                "msg_record_skipped",
                "msg_error_date_format_invalid",
                "msg_error_wrong_csv_layout",
                "msg_error_production_date"
        );

        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }

    @Transactional
    public int batchInsert(List<TblMachineInventory> list, int type) {

        int insertCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            if (0 == type) {
                entityManager.persist(list.get(i - 1));
            } else {
                entityManager.merge(list.get(i - 1));
            }

            // 1000件毎にDBへ登録する
            if (i % 1000 == 0) {
                entityManager.flush();
                entityManager.clear();

                insertCount += 1000;
            }

            count = i;

        }

        insertCount += count % 1000;

        return insertCount;
    }

    @Transactional
    public int batchInsertMachine(List<MstMachine> list) {

        int insertCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            entityManager.merge(list.get(i - 1));

            // 1000件毎にDBへ登録する
            if (i % 1000 == 0) {
                entityManager.flush();
                entityManager.clear();

                insertCount += 1000;
            }

            count = i;

        }

        insertCount += count % 1000;

        return insertCount;
    }

    /**
     *
     * @param mstMachineStoctake
     * @param unknownErrorList
     * @param departmentErrorList
     * @param addtionalList
     * @param locationConfirmCount
     * @param locationUnknownCount
     * @param errorCount
     * @param logFile
     * @param departments
     * @param uploadUser
     */
    public void machineStocktakeResultPrepareMail(MstMachineStoctake mstMachineStoctake,
            List<OutputErrorInfo> unknownErrorList,
            List<OutputErrorInfo> departmentErrorList,
            List<OutputErrorInfo> addtionalList,
            long locationConfirmCount,
            long locationUnknownCount,
            long errorCount,
            String logFile,
            Set<Integer> departments,
            MstUser uploadUser // 棚卸実施者
    ) {

        List<MstUser> userList = new ArrayList<>();
        String userName = uploadUser.getUserName();
        userList.add(uploadUser);//棚卸実施者には必ず送信

        for (Integer department : departments) {
            List<MstUser> list = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail017", department);
            if (list != null && list.size() > 0) {
                userList.addAll(list);
            }
        }

        Map<String, List<String>> receiverListByLangId = new HashMap<>();

        for (int i = 0; i < userList.size(); i++) {
            MstUser userInfo = userList.get(i);
            List<String> mailAddressList;
            if (receiverListByLangId.get(userInfo.getLangId()) == null) {
                if (StringUtils.isNotEmpty(userInfo.getMailAddress())) {
                    mailAddressList = new ArrayList<>();
                    mailAddressList.add(userInfo.getMailAddress());
                    receiverListByLangId.put(userInfo.getLangId(), mailAddressList);
                }
            } else {
                mailAddressList = receiverListByLangId.get(userInfo.getLangId());
                if (StringUtils.isNotEmpty(userInfo.getMailAddress())) {
                    if (!mailAddressList.contains(userInfo.getMailAddress())) {
                        mailAddressList.add(userInfo.getMailAddress());
                    }
                }
            }
        }

        for (Map.Entry<String, List<String>> entry : receiverListByLangId.entrySet()) {
            machineStocktakeResultSendMailMassage(mstMachineStoctake,
                    unknownErrorList,
                    departmentErrorList,
                    addtionalList,
                    locationConfirmCount,
                    locationUnknownCount,
                    errorCount,
                    entry.getValue(),
                    entry.getKey(),
                    logFile,
                    userName);
        }

    }

    /**
     *
     * @param mstMachineStoctake
     * @param langId
     * @param unknownErrorList
     * @param departmentErrorList
     * @param addtionalList
     * @param locationConfirmCount
     * @param locationUnknownCount
     * @param errorCount
     * @param userMailList
     * @param logFile
     * @param userName
     */
    public void machineStocktakeResultSendMailMassage(MstMachineStoctake mstMachineStoctake,
            List<OutputErrorInfo> unknownErrorList,
            List<OutputErrorInfo> departmentErrorList,
            List<OutputErrorInfo> addtionalList,
            long locationConfirmCount,
            long locationUnknownCount,
            long errorCount,
            List<String> userMailList,
            String langId,
            String logFile,
            String userName) {

        StringBuilder mailBody = new StringBuilder();
        Map<String, String> dictMap = getDictionaryList(langId);
        String tital = String.format(dictMap.get("stocktake_result_mail_title"), dictMap.get("machine"));
        int total = mstMachineStoctake.getMachineStocktake().getMachines().size();
        List<OutputCondition> outputCondition = mstMachineStoctake.getMachineStocktake().getOutputConditions();
        mailBody.append((String.format(dictMap.get("stocktake_result_mail_body"), dictMap.get("machine"))));
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        mailBody.append(" " + MailSender.MAIL_RETURN_CODE);
        mailBody.append((dictMap.get("reply_user_name") + "："));
        mailBody.append(userName);
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        mailBody.append((dictMap.get("evidence_upload_time") + "："));
        FileUtil fileUtil = new FileUtil();
        mailBody.append(fileUtil.getDateTimeFormatForStr(new Date()));
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        mailBody.append((dictMap.get("inventory_object") + "："));
        for (int i = 0; i < outputCondition.size(); i++) {
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            mailBody.append(("    " + outputCondition.get(i).getCompanyName()
                    + ", " + outputCondition.get(i).getLocationName()
                    + ", " + outputCondition.get(i).getInstallationSiteName()
                    + ", " + outputCondition.get(i).getDepartmentName()));
        }
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        mailBody.append((dictMap.get("db_process") + "："));
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        mailBody.append(("    " + dictMap.get("total_count") + "          " + total));
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        mailBody.append(("    " + dictMap.get("inventory_execution") + "        " + (locationConfirmCount + locationUnknownCount + errorCount)));
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        mailBody.append(("    " + dictMap.get("location_confirm") + "        " + locationConfirmCount));
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        mailBody.append(("    " + dictMap.get("location_unknown") + "        " + locationUnknownCount));
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        mailBody.append(("    " + dictMap.get("error") + "          " + errorCount));
        mailBody.append(MailSender.MAIL_RETURN_CODE);
        if (errorCount > 0) {
            mailBody.append(dictMap.get("stocktake_result_error_mail_body"));
            mailBody.append(MailSender.MAIL_RETURN_CODE);
        }
        mailBody.append(" " + MailSender.MAIL_RETURN_CODE);
        if (!unknownErrorList.isEmpty()) {
            String str = String.format(dictMap.get("stocktake_result_location_confirm_mail_body"), dictMap.get("machine"));
            mailBody.append(str);
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            mailBody.append(("    " + dictMap.get("machine_id") + "," + dictMap.get("machine_name")
                    + "," + dictMap.get("user_department") + "," + dictMap.get("company_name")
                    + "," + dictMap.get("location_name") + "," + dictMap.get("installation_site_name")));
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            for (int i = 0; i < unknownErrorList.size(); i++) {
                mailBody.append(("    " + unknownErrorList.get(i).getMachineId() + ","));
                mailBody.append((unknownErrorList.get(i).getMachineName() + ","));
                mailBody.append((unknownErrorList.get(i).getDepartmentName() + ","));
                mailBody.append((unknownErrorList.get(i).getCompanyName() + ","));
                mailBody.append((unknownErrorList.get(i).getLocationName() + ","));
                mailBody.append(unknownErrorList.get(i).getInstallationSiteName());
                mailBody.append(MailSender.MAIL_RETURN_CODE);
            }
            mailBody.append(" " + MailSender.MAIL_RETURN_CODE);
        }
        if (!departmentErrorList.isEmpty()) {

            String str = String.format(dictMap.get("stocktake_result_location_unknown_mail_body"), dictMap.get("machine"));
            mailBody.append(str);
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            mailBody.append(("    " + dictMap.get("machine_id") + "," + dictMap.get("machine_name")
                    + "," + dictMap.get("user_department") + "," + dictMap.get("company_name")
                    + "," + dictMap.get("location_name") + "," + dictMap.get("installation_site_name")));
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            for (int i = 0; i < departmentErrorList.size(); i++) {
                mailBody.append(("    " + departmentErrorList.get(i).getMachineId() + ","));
                mailBody.append((departmentErrorList.get(i).getMachineName() + ","));
                mailBody.append((departmentErrorList.get(i).getDepartmentName() + ","));
                mailBody.append((departmentErrorList.get(i).getCompanyName() + ","));
                mailBody.append((departmentErrorList.get(i).getLocationName() + ","));
                mailBody.append(departmentErrorList.get(i).getInstallationSiteName());
                mailBody.append(MailSender.MAIL_RETURN_CODE);

            }
            mailBody.append(" " + MailSender.MAIL_RETURN_CODE);
        }
        if (!addtionalList.isEmpty()) {
            String str = String.format(dictMap.get("stocktake_result_location_exclude_mail_body"), dictMap.get("machine"));
            mailBody.append(str);
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            mailBody.append(("    " + dictMap.get("machine_id") + "," + dictMap.get("qr_plate_info")));
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            for (int i = 0; i < addtionalList.size(); i++) {
                mailBody.append(("    " + addtionalList.get(i).getMachineId() + ","));
                mailBody.append(addtionalList.get(i).getQrPlateInfo());
                mailBody.append(MailSender.MAIL_RETURN_CODE);
            }
            mailBody.append(" " + MailSender.MAIL_RETURN_CODE);
        }
        mailBody.append((dictMap.get("output_file_id") + "："));
        mailBody.append(FileUtil.getStr(mstMachineStoctake.getMachineStocktake().getOutputFileUuid()));
        try {
            mailSender.setMakePlainTextBody(true);
            mailSender.sendMailWithAttachment(userMailList, null, tital, mailBody.toString(), logFile);
        } catch (IOException ex) {
            Logger.getLogger(MstMachineService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(MstMachineService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param readFilePath
     * @param uuid
     * @param userUuid
     * @param langId
     * @return
     */
    @Transactional
    public ImportResultResponse productionResultAdd(String readFilePath, String uuid, String userUuid, String langId) {
        CSVFileUtil csvFileUtil = null;
        boolean readEnd = false;
        int rowIndex = 0;
        long updatedCount = 0;
        long failedCount = 0;

        ImportResultResponse file = new ImportResultResponse();
        try {
            csvFileUtil = new CSVFileUtil(readFilePath);
            String logFileUuid = IDGenerator.generate();
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            Map<String, String> dictMap = getDictionaryList(langId);
            boolean isStart = true;
            FileUtil fileUtil = new FileUtil();
            do {                
                String readLine = csvFileUtil.readLine();
                if (isStart) {//ヘッダー行スキップ
                    readLine = csvFileUtil.readLine();
                    isStart = false;
                }

                List<String> readList;//rowinfo                
                if (StringUtils.isEmpty(readLine)) {
                    readEnd = true;
                } else {
                    rowIndex = rowIndex + 1;
                    readList = CSVFileUtil.fromCSVLinetoArray(readLine);
                    // データチェック
                    if (readList.size() != 5) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("machine_id"), "", dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_wrong_csv_layout")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    int index = 0;
                    //金型ID
                    String machineId = readList.get(index).trim();
                    //生産日
                    index++;
                    String productionDate = readList.get(index).trim();
                    //生産時間
                    index++;
                    String productionTimeMinutes = readList.get(index).trim();
                    //ショット数
                    index++;
                    String shotCount = readList.get(index).trim();
                    //訂正フラグ
                    index++;
                    String correctionFlg = readList.get(index).trim();

                    if (StringUtils.isEmpty(machineId)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("machine_id"), machineId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    //型チェック
                    if (StringUtils.isEmpty(productionDate)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("production_date"), productionDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    Date dateProductionDate;
                    try {
                        SimpleDateFormat dateFormat;
                        dateFormat = new SimpleDateFormat(DateFormat.DATE_FORMAT);
                        dateProductionDate = dateFormat.parse(DateFormat.formatDateYear(productionDate, DateFormat.DATE_FORMAT));
                    } catch (ParseException pe) {
                        // msg_error
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("production_date"), productionDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_date_format_invalid")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    BigDecimal intProductionTimeMinutes = BigDecimal.ZERO;
                    if (StringUtils.isNotEmpty(productionTimeMinutes)) {
                        if (!NumberUtil.validateDecimal(productionTimeMinutes, 10, 1)) {
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("production_time_minutes"), productionTimeMinutes, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                            failedCount = failedCount + 1;
                            continue;
                        }

                        intProductionTimeMinutes = new BigDecimal(productionTimeMinutes);
                    }

                    int intShotCount = 0;
                    if (StringUtils.isNotEmpty(shotCount)) {
                        try {
                            intShotCount = Integer.parseInt(shotCount);
                        } catch (NumberFormatException e) {
                            // msg_error
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("shot_count"), shotCount, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }

                    int intCorrectionFlg = 0;
                    if (StringUtils.isNotEmpty(correctionFlg)) {
                        try {
                            intCorrectionFlg = Integer.parseInt(correctionFlg);
                        } catch (NumberFormatException e) {
                            // msg_error
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("correction_flg"), correctionFlg, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }

                    //設備レコードを一意にするキー
                    MstMachine msrMachine = entityManager.find(MstMachine.class, machineId);
                    if (msrMachine == null) {
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("machine_id"), machineId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    int afterMainteTotalShotCount = FileUtil.getIntegerValue(msrMachine.getAfterMainteTotalShotCount());// ショット数
                    int totalShotCount = FileUtil.getIntegerValue(msrMachine.getTotalShotCount());// 累計ショット数

                    BigDecimal afterMainteTotalProducingTimeHour = BigDecimal.ZERO; //メンテナンス後生産時間
                    if (msrMachine.getAfterMainteTotalProducingTimeHour() != null) {
                        afterMainteTotalProducingTimeHour = msrMachine.getAfterMainteTotalProducingTimeHour();
                    }

                    BigDecimal totalProducingTimeHour = BigDecimal.ZERO; //累計生産時間
                    if (msrMachine.getTotalProducingTimeHour() != null) {
                        totalProducingTimeHour = msrMachine.getTotalProducingTimeHour();
                    }

                    if (intCorrectionFlg == 1) { // 立っている場合は、生産日のチェックを行ずに必ず足しこみをする。最終生産日の更新はしない

                        msrMachine.setAfterMainteTotalProducingTimeHour(afterMainteTotalProducingTimeHour.add(intProductionTimeMinutes));
                        msrMachine.setTotalProducingTimeHour(totalProducingTimeHour.add(intProductionTimeMinutes));

                    } else {
                        if (msrMachine.getLastProductionDate() != null) {
                            if (msrMachine.getLastProductionDate().compareTo(dateProductionDate) == -1) {
                                // 設備マスタの累計生産時間、メンテナンス後生産時間にこの値を加算
                                msrMachine.setAfterMainteTotalProducingTimeHour(afterMainteTotalProducingTimeHour.add(intProductionTimeMinutes));
                                msrMachine.setTotalProducingTimeHour(totalProducingTimeHour.add(intProductionTimeMinutes));
                                msrMachine.setLastProductionDate(dateProductionDate);
                            } else {
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("machine_id"), machineId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_production_date")));
                                failedCount = failedCount + 1;
                                continue;
                            }
                        } else {
                            msrMachine.setAfterMainteTotalProducingTimeHour(afterMainteTotalProducingTimeHour.add(intProductionTimeMinutes));
                            msrMachine.setTotalProducingTimeHour(totalProducingTimeHour.add(intProductionTimeMinutes));
                            msrMachine.setLastProductionDate(dateProductionDate);
                        }
                    }
                    msrMachine.setTotalShotCount(totalShotCount + intShotCount);
                    msrMachine.setAfterMainteTotalShotCount(afterMainteTotalShotCount + intShotCount);
                    msrMachine.setUpdateDate(new Date());
                    msrMachine.setUpdateUserUuid(userUuid);
                    entityManager.merge(msrMachine);
                    updatedCount = updatedCount + 1;
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("machine_id"), machineId, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_data_modified")));
                }

            } while (!readEnd);

            // リターン情報
            file.setTotalCount(Long.valueOf(String.valueOf(rowIndex)));
            file.setSucceededCount(updatedCount);
            file.setAddedCount(0);
            file.setUpdatedCount(updatedCount);
            file.setDeletedCount(0);
            file.setFailedCount(failedCount);
            file.setLog(logFileUuid);

            // アップロードログをテーブルに書き出し
            TblCsvImport tblCsvImport = new TblCsvImport();
            tblCsvImport.setImportUuid(IDGenerator.generate());
            tblCsvImport.setImportUserUuid(userUuid);
            tblCsvImport.setImportDate(new Date());
            tblCsvImport.setImportTable(CommonConstants.TBL_MST_MACHINE);
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(uuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_MACHINE);
            tblCsvImport.setFunctionId(mstFunction);

            tblCsvImport.setRecordCount(Integer.valueOf(String.valueOf(updatedCount + failedCount)));
            tblCsvImport.setSuceededCount(Integer.valueOf(String.valueOf(updatedCount)));
            tblCsvImport.setAddedCount(0);
            tblCsvImport.setUpdatedCount(Integer.valueOf(String.valueOf(updatedCount)));
            tblCsvImport.setDeletedCount(0);
            tblCsvImport.setFailedCount(Integer.valueOf(String.valueOf(failedCount)));
            tblCsvImport.setLogFileUuid(logFileUuid);
            tblCsvImport.setLogFileName(FileUtil.getLogFileName(String.format(dictMap.get("mold_or_machine_production_result_add"), dictMap.get("machine"))));
            tblCsvImportService.createCsvImpor(tblCsvImport);

        } catch (Exception e) {
            Logger.getLogger(MstMachineService.class.getName()).log(Level.SEVERE, null, e);
            file.setError(true);
            file.setErrorCode(ErrorMessages.E201_APPLICATION);
            file.setErrorMessage(e.getMessage());
            return file;
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }

        return file;
    }

    /**
     *
     * @param loginUser
     * @return
     */
    public FileReponse productionResultExport(LoginUser loginUser) {

        FileReponse file = new FileReponse();
        try {

            ArrayList<ArrayList> gLineList = new ArrayList<>();
            /**
             * Header
             */

            ArrayList headList = new ArrayList();
            /*Head*/
            Map<String, String> csvHeader = getDictionaryList(loginUser.getLangId());
            headList.add(csvHeader.get("machine_id"));
            headList.add(csvHeader.get("production_date"));
            headList.add(csvHeader.get("production_time_minutes"));
            headList.add(csvHeader.get("shot_count"));
            headList.add(csvHeader.get("correction_flg"));
            gLineList.add(headList);

            //CSVファイル出力
            String uuid = IDGenerator.generate();
            String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

            CSVFileUtil.writeCsv(outCsvPath, gLineList);

            TblCsvExport tblCsvExport = new TblCsvExport();
            tblCsvExport.setFileUuid(uuid);
            tblCsvExport.setExportTable(CommonConstants.TBL_MST_MACHINE);
            tblCsvExport.setExportDate(new Date());
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_MACHINE);
            tblCsvExport.setFunctionId(mstFunction);
            tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
            tblCsvExport.setClientFileName(String.format(csvHeader.get("mold_or_machine_production_result_add"), csvHeader.get("machine")) + CommonConstants.EXT_CSV);
            tblCsvExportService.createTblCsvExport(tblCsvExport);

            //csvファイルのUUIDをリターンする
            file.setFileUuid(uuid);
        } catch (Exception e) {
            Logger.getLogger(MstMachineService.class.getName()).log(Level.SEVERE, null, e);
            file.setError(true);
            file.setErrorCode(ErrorMessages.E201_APPLICATION);
            file.setErrorMessage(e.getMessage());
            return file;
        }
        return file;

    }

}
