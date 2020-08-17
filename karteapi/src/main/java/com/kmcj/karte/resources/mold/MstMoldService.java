/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold;

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
import com.kmcj.karte.resources.component.MstComponent;
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
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportVo;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtnService;
import com.kmcj.karte.resources.mold.assetno.MstMoldAssetNo;
import com.kmcj.karte.resources.mold.assetno.MstMoldAssetNoPK;
import com.kmcj.karte.resources.mold.assetno.MstMoldAssetNoService;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttribute;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeList;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeService;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationList;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationPK;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationService;
import com.kmcj.karte.resources.mold.inventory.Mold;
import com.kmcj.karte.resources.mold.inventory.MoldStocktake;
import com.kmcj.karte.resources.mold.inventory.MstMoldStoctake;
import com.kmcj.karte.resources.mold.inventory.OutputCondition;
import com.kmcj.karte.resources.mold.inventory.OutputErrorInfo;
import com.kmcj.karte.resources.mold.inventory.SearchCondition;
import com.kmcj.karte.resources.mold.inventory.SearchConditionList;
import com.kmcj.karte.resources.mold.inventory.TblMoldInventory;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistory;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistorys;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistoryService;
import com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomendService;
import com.kmcj.karte.resources.mold.part.MstMoldPart;
import com.kmcj.karte.resources.mold.part.MstMoldPartService;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRel;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelDetail;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelService;
import com.kmcj.karte.resources.mold.proccond.MstMoldProcCond;
import com.kmcj.karte.resources.mold.proccond.MstMoldProcCondService;
import com.kmcj.karte.resources.mold.proccond.MstMoldProcConds;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpec;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecPK;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecService;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecs;
import com.kmcj.karte.resources.mold.reception.TblMoldReceptionList;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelList;
import com.kmcj.karte.resources.mold.reception.TblMoldReceptionVo;
import com.kmcj.karte.resources.mold.spec.MstMoldSpec;
import com.kmcj.karte.resources.mold.spec.MstMoldSpecPK;
import com.kmcj.karte.resources.mold.spec.MstMoldSpecService;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistory;
import com.kmcj.karte.resources.production.TblProduction;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
public class MstMoldService {

    private Logger logger = Logger.getLogger(MstMoldService.class.getName());
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstMoldSpecService mstMoldSpecService;

    @Inject
    private MstMoldComponentRelationService mstMoldComponentRelationService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstMoldProcCondSpecService mstMoldProcCondSpecService;

    @Inject
    private MstMoldProcCondService mstMoldProcCondService;

    @Inject
    private MstCompanyService mstCompanyService;

    private Map<String, String> inMoldTypeMapTemp;

    private Map<String, String> inDepartmentMapTemp;

    private Map<String, String> outMoldTypeMapTemp;

    private Map<String, String> inStatusMapTemp;

    private Map<String, String> outStatusMapTemp;

    @Inject
    private MstLocationService mstLocationService;

    @Inject
    private MstInstallationSiteService mstInstallationSiteService;
    
    @Inject
    private TblMoldLocationHistoryService tblMoldLocationHistoryService;

    @Inject
    private ExtMstChoiceService extMstChoiceService;

    @Inject
    private MstMoldAssetNoService mstMoldAsseNoService;

    @Inject
    private TblUploadFileService tblUploadFileService;

    @Inject
    private MstMoldAttributeService mstMoldAttributeService;

    @Inject
    private TblMaintenanceCyclePtnService tblMaintenanceCyclePtnService;

    @Inject
    private TblMoldMaintenanceRecomendService tblMoldMaintenanceRecomendService;

    @Inject
    private MstUserMailReceptionService mstUserMailReceptionService;

    @Inject
    private MstMoldPartRelService mstMoldPartRelService;
    
    @Inject 
    private MstMoldPartService mstMoldPartService;
    
    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MailSender mailSender; // 20170609 Apeng add

    public static final String EXT_LOGIN_URL = "ws/karte/api/authentication/extlogin?lang=";
    public static final String EXT_MOLD_RECEPTION_UPDATE = "ws/karte/api/mold/reception/update";

    private final static Map<String, String> orderKey;

    // リターン情報を初期化
    long succeededCount = 0;//成功件数
    long addedCount = 0;//追加件数
    long updatedCount = 0;//更新件数
    long failedCount = 0; //失敗件数
    long deletedCount = 0; //削除件数

    static {
        orderKey = new HashMap<>();
        orderKey.put("moldId", " ORDER BY t0.moldId ");// 金型ＩＤ
        orderKey.put("moldName", " ORDER BY t0.moldName ");// 金型名称
        orderKey.put("moldType", " ORDER BY t0.moldType ");// 金型種類
        orderKey.put("mainAssetNo", " ORDER BY t0.mainAssetNo ");// 代表資産番号
        orderKey.put("moldCreatedDate", " ORDER BY t0.moldCreatedDate ");// 金型作成日
        orderKey.put("inspectedDate", " ORDER BY t0.inspectedDate ");// 検収日
        orderKey.put("departmentName", " ORDER BY t0.department ");// 所属
        orderKey.put("ownerCompanyName", " ORDER BY mco.companyName ");// 所有会社名称
        orderKey.put("installedDate", " ORDER BY t0.installedDate ");// 設置日
        orderKey.put("companyName", " ORDER BY mc.companyName ");// 会社名称
        orderKey.put("locationName", " ORDER BY ml.locationName ");// 所在地名称
        orderKey.put("instllationSiteName", " ORDER BY mi.installationSiteName ");// 設置場所名称
        orderKey.put("status", " ORDER BY t0.status ");// ステータス
        orderKey.put("statusChangedDate", " ORDER BY t0.statusChangedDate ");// ステータ変更日
        orderKey.put("lastProductionDateStr", " ORDER BY t0.lastProductionDate ");// 最終生産日
        orderKey.put("totalProducingTimeHour", " ORDER BY t0.totalProducingTimeHour ");// 累計生産時間
        orderKey.put("totalShotCount", " ORDER BY t0.totalShotCount ");// 累計ショット数
        orderKey.put("lastMainteDateStr", " ORDER BY t0.lastMainteDate ");// 最終メンテナンス日
        orderKey.put("afterMainteTotalProducingTimeHour", " ORDER BY t0.afterMainteTotalProducingTimeHour ");// メンテナンス後生産時間
        orderKey.put("afterMainteTotalShotCount", " ORDER BY t0.afterMainteTotalShotCount ");// メンテナンス後ショット数
        orderKey.put("mainteCycleCode01", " ORDER BY cycle01.tblMaintenanceCyclePtnPK.cycleCode ");// メンテサイクルID01
        orderKey.put("mainteCycleCode02", " ORDER BY cycle02.tblMaintenanceCyclePtnPK.cycleCode ");// メンテサイクルID02
        orderKey.put("mainteCycleCode03", " ORDER BY cycle03.tblMaintenanceCyclePtnPK.cycleCode ");// メンテサイクルID03       

    }

    /**
     *
     * @param moldUuid
     * @return
     */
    public MstMoldList getMstMoldDetail(String moldUuid) {
        Query query = entityManager.createNamedQuery("MstMold.findByUuid");
        query.setParameter("uuid", moldUuid);
        List list = query.getResultList();
        MstMoldList response = new MstMoldList();
        response.setMstMold(list);
        return response;
    }

    /**
     * 金型一覧使用
     *
     * @param moldId
     * @return
     */
    public MstMoldList getMstMoldDetailByMoldId(String moldId) {
        Query query = entityManager.createNamedQuery("MstMold.findByMoldId");
        query.setParameter("moldId", moldId);
        List list = query.getResultList();
        MstMoldList response = new MstMoldList();
        response.setMstMold(list);
        return response;
    }

    /**
     * 金型マスタ詳細取得
     *
     * @param moldId
     * @param loginUser
     * @return
     */
    public MstMoldDetail getMoldByMoldId(String moldId, LoginUser loginUser) {
        MstMoldDetail response = new MstMoldDetail();
        Query query = entityManager.createNamedQuery("MstMold.findByMoldId");
        query.setParameter("moldId", moldId);
        MstMold mstMold;
        try {
            mstMold = (MstMold) query.getSingleResult();
        } catch (NoResultException e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        List<MstComponent> mstComponentList = new ArrayList<>();
        MstMoldComponentRelationList list = mstMoldComponentRelationService.getMstMoldComponentRelationByMoldId(moldId);
        for (int i = 0; i < list.getMstMoldComponentRelation().size(); i++) {
            MstMoldComponentRelation mstMoldComponentRelation = list.getMstMoldComponentRelation().get(i);
            String componentId = mstMoldComponentRelation.getMstMoldComponentRelationPK().getComponentId();
            MstComponent component = getMstComponentById(componentId);
            mstComponentList.add(component);
        }
        response.setMstMold(mstMold);
        response.setMstComponentList(mstComponentList);
        return response;
    }

    /**
     *
     * @param ComponentId
     * @return
     */
    public MstComponent getMstComponentById(String ComponentId) {
        Query query = entityManager.createNamedQuery("MstComponent.findById");
        query.setParameter("id", ComponentId);
        MstComponent mstComponent;
        try {
            mstComponent = (MstComponent) query.getResultList();
            return mstComponent;
        } catch (NoResultException e) {
            throw e;
        }
    }

    /**
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
     * @return
     */
    public CountResponse getMstMoldCount(
            String moldId,
            String moldName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer moldType,
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
            Date moldCreatedDateFrom,
            Date moldCreatedDateTo,
            Integer status) {
        List list = getSql(
                moldId,
                moldName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                moldType,
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
                moldCreatedDateFrom,
                moldCreatedDateTo,
                status,
                "count");
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    public MstMoldDetailList getMstMolds(
            String moldId,
            String moldName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer moldType,
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
            Date moldCreatedDateFrom,
            Date moldCreatedDateTo,
            Integer status,
            LoginUser loginUser) {
        MstMoldDetailList mstMoldDetailList = new MstMoldDetailList();
        List<MstMoldDetail> mstMoldDetails = new ArrayList<>();
        FileUtil fu = new FileUtil();
        List list = getSql(
                moldId,
                moldName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                moldType,
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
                moldCreatedDateFrom,
                moldCreatedDateTo,
                status,
                "");
        if (list != null && list.size() > 0) {
            MstChoiceList mstDepartmentChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
            for (int i = 0; i < list.size(); i++) {
                MstMold mstMold = (MstMold) list.get(i);
                MstMoldDetail mstMoldDetail = new MstMoldDetail();
                mstMoldDetail.setMoldUuid(mstMold.getUuid());
                mstMoldDetail.setMoldId(mstMold.getMoldId());
                mstMoldDetail.setMoldName(mstMold.getMoldName());
                Integer itemMoldType = mstMold.getMoldType();
                if (FileUtil.checkExternal(entityManager, mstDictionaryService, mstMold.getMoldId(), loginUser).isError() == true) {
                    mstMoldDetail.setExternalFlg(1);
                    if (itemMoldType != null) {
                        mstMoldDetail.setMoldTypeText(extMstChoiceService.getExtMstChoiceText(mstMold.getCompanyId(), "mst_mold.mold_type", String.valueOf(itemMoldType), loginUser.getLangId()));
                    }
                } else {
                    mstMoldDetail.setExternalFlg(0);
                    if (itemMoldType != null) {
                        mstMoldDetail.setMoldType(itemMoldType);
                    }
                }

                if (mstMold.getMainAssetNo() != null && !"".equals(mstMold.getMainAssetNo())) {
                    mstMoldDetail.setMainAssetNo(mstMold.getMainAssetNo());
                } else {
                    mstMoldDetail.setMainAssetNo("");
                }
                if (mstMold.getMoldCreatedDate() != null) {
                    mstMoldDetail.setMoldCreatedDate(mstMold.getMoldCreatedDate());
                }
                if (mstMold.getInspectedDate() != null) {
                    mstMoldDetail.setInspectedDate(mstMold.getInspectedDate());
                }
                if (mstMold.getMstCompanyByOwnerCompanyId() != null && mstMold.getMstCompanyByOwnerCompanyId().getCompanyName() != null) {
                    mstMoldDetail.setOwnerCompanyName(mstMold.getMstCompanyByOwnerCompanyId().getCompanyName());
                } else {
                    mstMoldDetail.setOwnerCompanyName("");
                }
                if (mstMold.getInstalledDate() != null) {
                    mstMoldDetail.setInstalledDate(mstMold.getInstalledDate());
                }
                if (mstMold.getMstCompanyByCompanyId() != null) {
                    mstMoldDetail.setCompanyId(mstMold.getMstCompanyByCompanyId().getId());
                    mstMoldDetail.setCompanyName(mstMold.getMstCompanyByCompanyId().getCompanyName());
                } else {
                    mstMoldDetail.setCompanyId("");
                    mstMoldDetail.setCompanyName("");
                }

                if (mstMold.getMstLocation() != null) {
                    mstMoldDetail.setLocationId(mstMold.getMstLocation().getId());
                    mstMoldDetail.setLocationName(mstMold.getMstLocation().getLocationName());
                } else {
                    mstMoldDetail.setLocationId("");
                    mstMoldDetail.setLocationName("");
                }

                if (mstMold.getMstInstallationSite() != null) {
                    mstMoldDetail.setInstllationSiteId(mstMold.getMstInstallationSite().getId());
                    mstMoldDetail.setInstllationSiteName(mstMold.getMstInstallationSite().getInstallationSiteName());
                } else {
                    mstMoldDetail.setInstllationSiteId("");
                    mstMoldDetail.setInstllationSiteName("");
                }

                if (mstMold.getStatus() != null) {
                    mstMoldDetail.setStatus(mstMold.getStatus());
                }
                if (mstMold.getStatusChangedDate() != null) {
                    mstMoldDetail.setStatusChangedDate(mstMold.getStatusChangedDate());
                }

                if (mstMold.getDepartment() != null) {
                    mstMoldDetail.setDepartment("" + mstMold.getDepartment());
                    for (MstChoice mstChoice : mstDepartmentChoiceList.getMstChoice()) {
                        if (mstChoice.getMstChoicePK().getSeq().equals(mstMoldDetail.getDepartment())) {
                            mstMoldDetail.setDepartmentName(mstChoice.getChoice());
                            break;
                        }
                    }
                }

                // 4.2 Zhangying S
                // 最終生産日
                if (mstMold.getLastProductionDate() != null) {
                    mstMoldDetail.setLastProductionDate(mstMold.getLastProductionDate());
                    mstMoldDetail.setLastProductionDateStr(fu.getDateFormatForStr(mstMold.getLastProductionDate()));
                } else {
                    mstMoldDetail.setLastProductionDate(null);
                    mstMoldDetail.setLastProductionDateStr("");
                }
                // 累計生産時間
                if (mstMold.getTotalProducingTimeHour() != null) {
                    mstMoldDetail.setTotalProducingTimeHour(mstMold.getTotalProducingTimeHour());
                } else {
                    mstMoldDetail.setTotalProducingTimeHour(BigDecimal.ZERO);
                }
                // 累計ショット数
                if (mstMold.getTotalShotCount() != null) {
                    mstMoldDetail.setTotalShotCount("" + mstMold.getTotalShotCount());
                } else {
                    mstMoldDetail.setTotalShotCount("0");
                }
                // 最終メンテナンス日
                if (mstMold.getLastMainteDate() != null) {
                    mstMoldDetail.setLastMainteDate(mstMold.getLastMainteDate());
                    mstMoldDetail.setLastMainteDateStr(fu.getDateFormatForStr(mstMold.getLastMainteDate()));
                } else {
                    mstMoldDetail.setLastMainteDate(null);
                    mstMoldDetail.setLastMainteDateStr("");
                }
                // メンテナンス後生産時間
                if (mstMold.getAfterMainteTotalProducingTimeHour() != null) {
                    mstMoldDetail.setAfterMainteTotalProducingTimeHour(mstMold.getAfterMainteTotalProducingTimeHour());
                } else {
                    mstMoldDetail.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
                }
                // メンテナンス後ショット数
                if (mstMold.getAfterMainteTotalShotCount() != null) {
                    mstMoldDetail.setAfterMainteTotalShotCount(mstMold.getAfterMainteTotalShotCount());
                } else {
                    mstMoldDetail.setAfterMainteTotalShotCount(0);
                }

                if (null != mstMold.getBlMaintenanceCyclePtn01()) {
                    // メンテサイクルコード01
                    mstMoldDetail.setMainteCycleCode01(mstMold.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    mstMoldDetail.setMainteCycleCode01("");
                }
                if (null != mstMold.getBlMaintenanceCyclePtn02()) {
                    // メンテサイクルコード02
                    mstMoldDetail.setMainteCycleCode02(mstMold.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    mstMoldDetail.setMainteCycleCode02("");
                }
                if (null != mstMold.getBlMaintenanceCyclePtn03()) {
                    // メンテサイクルコード03
                    mstMoldDetail.setMainteCycleCode03(mstMold.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    mstMoldDetail.setMainteCycleCode03("");
                }
                // 4.2 Zhangying E      

                mstMoldDetails.add(mstMoldDetail);
            }
        }
        mstMoldDetailList.setMstMoldDetail(mstMoldDetails);
        return mstMoldDetailList;

    }
    
    public MstMoldDetailList getMstMoldsWithoutDispose(
            String moldId,
            String moldName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer moldType,
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
            Date moldCreatedDateFrom,
            Date moldCreatedDateTo,
            Integer status,
            LoginUser loginUser) {
        MstMoldDetailList mstMoldDetailList = new MstMoldDetailList();
        List<MstMoldDetail> mstMoldDetails = new ArrayList<>();
        FileUtil fu = new FileUtil();
        List list = getSqlWithoutDispose(
                moldId,
                moldName,
                mainAssetNo,
                ownerCompanyName,
                companyName,
                locationName,
                instllationSiteName,
                moldType,
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
                moldCreatedDateFrom,
                moldCreatedDateTo,
                status,
                "");
        if (list != null && list.size() > 0) {
            MstChoiceList mstDepartmentChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
            for (int i = 0; i < list.size(); i++) {
                MstMold mstMold = (MstMold) list.get(i);
                MstMoldDetail mstMoldDetail = new MstMoldDetail();
                mstMoldDetail.setMoldUuid(mstMold.getUuid());
                mstMoldDetail.setMoldId(mstMold.getMoldId());
                mstMoldDetail.setMoldName(mstMold.getMoldName());
                Integer itemMoldType = mstMold.getMoldType();
                if (FileUtil.checkExternal(entityManager, mstDictionaryService, mstMold.getMoldId(), loginUser).isError() == true) {
                    mstMoldDetail.setExternalFlg(1);
                    if (itemMoldType != null) {
                        mstMoldDetail.setMoldTypeText(extMstChoiceService.getExtMstChoiceText(mstMold.getCompanyId(), "mst_mold.mold_type", String.valueOf(itemMoldType), loginUser.getLangId()));
                    }
                } else {
                    mstMoldDetail.setExternalFlg(0);
                    if (itemMoldType != null) {
                        mstMoldDetail.setMoldType(itemMoldType);
                    }
                }

                if (mstMold.getMainAssetNo() != null && !"".equals(mstMold.getMainAssetNo())) {
                    mstMoldDetail.setMainAssetNo(mstMold.getMainAssetNo());
                } else {
                    mstMoldDetail.setMainAssetNo("");
                }
                if (mstMold.getMoldCreatedDate() != null) {
                    mstMoldDetail.setMoldCreatedDate(mstMold.getMoldCreatedDate());
                }
                if (mstMold.getInspectedDate() != null) {
                    mstMoldDetail.setInspectedDate(mstMold.getInspectedDate());
                }
                if (mstMold.getMstCompanyByOwnerCompanyId() != null && mstMold.getMstCompanyByOwnerCompanyId().getCompanyName() != null) {
                    mstMoldDetail.setOwnerCompanyName(mstMold.getMstCompanyByOwnerCompanyId().getCompanyName());
                } else {
                    mstMoldDetail.setOwnerCompanyName("");
                }
                if (mstMold.getInstalledDate() != null) {
                    mstMoldDetail.setInstalledDate(mstMold.getInstalledDate());
                }
                if (mstMold.getMstCompanyByCompanyId() != null) {
                    mstMoldDetail.setCompanyId(mstMold.getMstCompanyByCompanyId().getId());
                    mstMoldDetail.setCompanyName(mstMold.getMstCompanyByCompanyId().getCompanyName());
                } else {
                    mstMoldDetail.setCompanyId("");
                    mstMoldDetail.setCompanyName("");
                }

                if (mstMold.getMstLocation() != null) {
                    mstMoldDetail.setLocationId(mstMold.getMstLocation().getId());
                    mstMoldDetail.setLocationName(mstMold.getMstLocation().getLocationName());
                } else {
                    mstMoldDetail.setLocationId("");
                    mstMoldDetail.setLocationName("");
                }

                if (mstMold.getMstInstallationSite() != null) {
                    mstMoldDetail.setInstllationSiteId(mstMold.getMstInstallationSite().getId());
                    mstMoldDetail.setInstllationSiteName(mstMold.getMstInstallationSite().getInstallationSiteName());
                } else {
                    mstMoldDetail.setInstllationSiteId("");
                    mstMoldDetail.setInstllationSiteName("");
                }

                if (mstMold.getStatus() != null) {
                    mstMoldDetail.setStatus(mstMold.getStatus());
                }
                if (mstMold.getStatusChangedDate() != null) {
                    mstMoldDetail.setStatusChangedDate(mstMold.getStatusChangedDate());
                }

                if (mstMold.getDepartment() != null) {
                    mstMoldDetail.setDepartment("" + mstMold.getDepartment());
                    for (MstChoice mstChoice : mstDepartmentChoiceList.getMstChoice()) {
                        if (mstChoice.getMstChoicePK().getSeq().equals(mstMoldDetail.getDepartment())) {
                            mstMoldDetail.setDepartmentName(mstChoice.getChoice());
                            break;
                        }
                    }
                }

                // 4.2 Zhangying S
                // 最終生産日
                if (mstMold.getLastProductionDate() != null) {
                    mstMoldDetail.setLastProductionDate(mstMold.getLastProductionDate());
                    mstMoldDetail.setLastProductionDateStr(fu.getDateFormatForStr(mstMold.getLastProductionDate()));
                } else {
                    mstMoldDetail.setLastProductionDate(null);
                    mstMoldDetail.setLastProductionDateStr("");
                }
                // 累計生産時間
                if (mstMold.getTotalProducingTimeHour() != null) {
                    mstMoldDetail.setTotalProducingTimeHour(mstMold.getTotalProducingTimeHour());
                } else {
                    mstMoldDetail.setTotalProducingTimeHour(BigDecimal.ZERO);
                }
                // 累計ショット数
                if (mstMold.getTotalShotCount() != null) {
                    mstMoldDetail.setTotalShotCount("" + mstMold.getTotalShotCount());
                } else {
                    mstMoldDetail.setTotalShotCount("0");
                }
                // 最終メンテナンス日
                if (mstMold.getLastMainteDate() != null) {
                    mstMoldDetail.setLastMainteDate(mstMold.getLastMainteDate());
                    mstMoldDetail.setLastMainteDateStr(fu.getDateFormatForStr(mstMold.getLastMainteDate()));
                } else {
                    mstMoldDetail.setLastMainteDate(null);
                    mstMoldDetail.setLastMainteDateStr("");
                }
                // メンテナンス後生産時間
                if (mstMold.getAfterMainteTotalProducingTimeHour() != null) {
                    mstMoldDetail.setAfterMainteTotalProducingTimeHour(mstMold.getAfterMainteTotalProducingTimeHour());
                } else {
                    mstMoldDetail.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
                }
                // メンテナンス後ショット数
                if (mstMold.getAfterMainteTotalShotCount() != null) {
                    mstMoldDetail.setAfterMainteTotalShotCount(mstMold.getAfterMainteTotalShotCount());
                } else {
                    mstMoldDetail.setAfterMainteTotalShotCount(0);
                }

                if (null != mstMold.getBlMaintenanceCyclePtn01()) {
                    // メンテサイクルコード01
                    mstMoldDetail.setMainteCycleCode01(mstMold.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    mstMoldDetail.setMainteCycleCode01("");
                }
                if (null != mstMold.getBlMaintenanceCyclePtn02()) {
                    // メンテサイクルコード02
                    mstMoldDetail.setMainteCycleCode02(mstMold.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    mstMoldDetail.setMainteCycleCode02("");
                }
                if (null != mstMold.getBlMaintenanceCyclePtn03()) {
                    // メンテサイクルコード03
                    mstMoldDetail.setMainteCycleCode03(mstMold.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    mstMoldDetail.setMainteCycleCode03("");
                }
                // 4.2 Zhangying E      

                mstMoldDetails.add(mstMoldDetail);
            }
        }
        mstMoldDetailList.setMstMoldDetail(mstMoldDetails);
        return mstMoldDetailList;

    }
    
    public MstMoldDetail getMstMoldDetails(String moldUuid, String moldId, LoginUser loginUser) {
        StringBuilder sql = new StringBuilder("SELECT m FROM MstMold m "
                + " LEFT JOIN FETCH m.mstCreateUser mstCreateUser "
                + " LEFT JOIN FETCH m.mstUpdateUser mstUpdateUser "
                + "WHERE 1=1 ");
        if (null != moldId && !moldId.isEmpty()) {
            sql.append(" and m.moldId = :moldId ");
        }
        if (moldUuid != null && !moldUuid.isEmpty()) {
            sql.append(" and m.uuid = :uuid ");
        }
        MstMoldDetail mstMoldDetail = new MstMoldDetail();

        Query query = entityManager.createQuery(sql.toString());
        if (null != moldId && !moldId.isEmpty()) {
            query.setParameter("moldId", moldId);
        }
        if (moldUuid != null && !moldUuid.isEmpty()) {
            query.setParameter("uuid", moldUuid);
        }

        MstMold mold;
        try {
            mold = (MstMold) query.getSingleResult();
        } catch (NoResultException e) {
            mstMoldDetail.setError(true);
            mstMoldDetail.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstMoldDetail.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return mstMoldDetail;
        }

        mstMoldDetail.setMoldUuid(mold.getUuid());
        mstMoldDetail.setMoldId(mold.getMoldId());
        mstMoldDetail.setMoldName(mold.getMoldName());
        mstMoldDetail.setMoldType(mold.getMoldType());
        mstMoldDetail.setMainAssetNo(mold.getMainAssetNo());
        mstMoldDetail.setDepartment(String.valueOf(mold.getDepartment()));
        mstMoldDetail.setMoldCreatedDate(mold.getMoldCreatedDate());
        mstMoldDetail.setInspectedDate(mold.getInspectedDate());
        mstMoldDetail.setCreatedDate(mold.getCreateDate());
        if (null == mold.getMstCompanyByOwnerCompanyId()) {
            mstMoldDetail.setOwnerCompanyId("");
            mstMoldDetail.setOwnerCompanyName("");
        } else {
            mstMoldDetail.setOwnerCompanyId(mold.getMstCompanyByOwnerCompanyId().getId());
            mstMoldDetail.setOwnerCompanyName(mold.getMstCompanyByOwnerCompanyId().getCompanyName());
        }

        mstMoldDetail.setInstalledDate(mold.getInstalledDate());

        if (null != mold.getMstCompanyByCompanyId()) {
            mstMoldDetail.setCompanyId(mold.getMstCompanyByCompanyId().getId());
            mstMoldDetail.setCompanyName(mold.getMstCompanyByCompanyId().getCompanyName());
        } else {
            mstMoldDetail.setCompanyId("");
            mstMoldDetail.setCompanyName("");
        }

        if (null != mold.getMstLocation()) {
            mstMoldDetail.setLocationId(mold.getMstLocation().getId());
            mstMoldDetail.setLocationName(mold.getMstLocation().getLocationName());
        } else {
            mstMoldDetail.setLocationId("");
            mstMoldDetail.setLocationName("");
        }

        if (null != mold.getMstInstallationSite()) {
            mstMoldDetail.setInstllationSiteId(mold.getMstInstallationSite().getId());
            mstMoldDetail.setInstllationSiteName(mold.getMstInstallationSite().getInstallationSiteName());
        } else {
            mstMoldDetail.setInstllationSiteId("");
            mstMoldDetail.setInstllationSiteName("");
        }

        mstMoldDetail.setStatus(mold.getStatus());
        mstMoldDetail.setStatusChangedDate(mold.getStatusChangedDate());
        mstMoldDetail.setMainteStatus(mold.getMainteStatus());
        if (mold.getMainteStatus() != null && !"".equals(String.valueOf(mold.getMainteStatus()))) {
            int mainteStatus = mold.getMainteStatus();
            MstChoiceList mainteStatusChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_mold.mainte_status");
            for (int momi = 0; momi < mainteStatusChoiceList.getMstChoice().size(); momi++) {
                MstChoice aMstChoice = mainteStatusChoiceList.getMstChoice().get(momi);
                if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mainteStatus))) {
                    mstMoldDetail.setMainteStatusText(aMstChoice.getChoice());
                    break;
                } else {
                    mstMoldDetail.setMainteStatusText("");
                }
            }
        } else {
            mstMoldDetail.setMainteStatusText("");
        }
        mstMoldDetail.setImgFilePath01(mold.getImgFilePath01());
        mstMoldDetail.setImgFilePath02(mold.getImgFilePath02());
        mstMoldDetail.setImgFilePath03(mold.getImgFilePath03());
        mstMoldDetail.setImgFilePath04(mold.getImgFilePath04());
        mstMoldDetail.setImgFilePath05(mold.getImgFilePath05());
        mstMoldDetail.setImgFilePath06(mold.getImgFilePath06());
        mstMoldDetail.setImgFilePath07(mold.getImgFilePath07());
        mstMoldDetail.setImgFilePath08(mold.getImgFilePath08());
        mstMoldDetail.setImgFilePath09(mold.getImgFilePath09());
        mstMoldDetail.setImgFilePath10(mold.getImgFilePath10());
        // 添付ファイル
        String strReportFilePath01 = mold.getReportFilePath01();
        if (null != strReportFilePath01 && !"".equals(strReportFilePath01)) {
            mstMoldDetail.setReportFilePath01(strReportFilePath01);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath01);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName01(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName01("");
            }
        } else {
            mstMoldDetail.setReportFilePath01("");
            mstMoldDetail.setReportFilePathName01("");
        }

        String strReportFilePath02 = mold.getReportFilePath02();
        if (null != strReportFilePath02 && !"".equals(strReportFilePath02)) {
            mstMoldDetail.setReportFilePath02(strReportFilePath02);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath02);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName02(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName02("");
            }
        } else {
            mstMoldDetail.setReportFilePath02("");
            mstMoldDetail.setReportFilePathName02("");
        }

        String strReportFilePath03 = mold.getReportFilePath03();
        if (null != strReportFilePath03 && !"".equals(strReportFilePath03)) {
            mstMoldDetail.setReportFilePath03(strReportFilePath03);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath03);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName03(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName03("");
            }
        } else {
            mstMoldDetail.setReportFilePath03("");
            mstMoldDetail.setReportFilePathName03("");
        }

        String strReportFilePath04 = mold.getReportFilePath04();
        if (null != strReportFilePath04 && !"".equals(strReportFilePath04)) {
            mstMoldDetail.setReportFilePath04(strReportFilePath04);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath04);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName04(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName04("");
            }
        } else {
            mstMoldDetail.setReportFilePath04("");
            mstMoldDetail.setReportFilePathName04("");
        }

        String strReportFilePath05 = mold.getReportFilePath05();
        if (null != strReportFilePath05 && !"".equals(strReportFilePath05)) {
            mstMoldDetail.setReportFilePath05(strReportFilePath05);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath05);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName05(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName05("");
            }
        } else {
            mstMoldDetail.setReportFilePath05("");
            mstMoldDetail.setReportFilePathName05("");
        }

        String strReportFilePath06 = mold.getReportFilePath06();
        if (null != strReportFilePath06 && !"".equals(strReportFilePath06)) {
            mstMoldDetail.setReportFilePath06(strReportFilePath06);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath06);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName06(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName06("");
            }
        } else {
            mstMoldDetail.setReportFilePath06("");
            mstMoldDetail.setReportFilePathName06("");
        }

        String strReportFilePath07 = mold.getReportFilePath07();
        if (null != strReportFilePath07 && !"".equals(strReportFilePath07)) {
            mstMoldDetail.setReportFilePath07(strReportFilePath07);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath07);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName07(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName07("");
            }
        } else {
            mstMoldDetail.setReportFilePath07("");
            mstMoldDetail.setReportFilePathName07("");
        }

        String strReportFilePath08 = mold.getReportFilePath08();
        if (null != strReportFilePath08 && !"".equals(strReportFilePath08)) {
            mstMoldDetail.setReportFilePath08(strReportFilePath08);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath08);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName08(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName08("");
            }
        } else {
            mstMoldDetail.setReportFilePath08("");
            mstMoldDetail.setReportFilePathName08("");
        }

        String strReportFilePath09 = mold.getReportFilePath09();
        if (null != strReportFilePath09 && !"".equals(strReportFilePath09)) {
            mstMoldDetail.setReportFilePath09(strReportFilePath09);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath09);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName09(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName09("");
            }
        } else {
            mstMoldDetail.setReportFilePath09("");
            mstMoldDetail.setReportFilePathName09("");
        }

        String strReportFilePath10 = mold.getReportFilePath10();
        if (null != strReportFilePath10 && !"".equals(strReportFilePath10)) {
            mstMoldDetail.setReportFilePath10(strReportFilePath10);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath10);
            if (null != tblUploadFile) {
                mstMoldDetail.setReportFilePathName10(tblUploadFile.getUploadFileName());
            } else {
                mstMoldDetail.setReportFilePathName10("");
            }
        } else {
            mstMoldDetail.setReportFilePath10("");
            mstMoldDetail.setReportFilePathName10("");
        }

        //  KM-147 登録日時、登録ユーザー名称、更新日時、更新ユーザーを表示 ADD START
        FileUtil fu = new FileUtil();
        mstMoldDetail.setCreatedDateStr(fu.getDateTimeFormatForStr(mold.getCreateDate()));
        mstMoldDetail.setUpdateDate(mold.getUpdateDate());
        mstMoldDetail.setUpdateDateStr(fu.getDateTimeFormatForStr(mold.getUpdateDate()));
        if (mold.getMstCreateUser() != null) {
            mstMoldDetail.setCreateUserName(mold.getMstCreateUser().getUserName());
        }

        if (mold.getMstUpdateUser() != null) {
            mstMoldDetail.setUpdateUserName(mold.getMstUpdateUser().getUserName());
        }
        //  KM-147 登録日時、登録ユーザー名称、更新日時、更新ユーザーを表示 ADD END

        if (mold.getMoldId() != null && !"".equals(mold.getMoldId())) {
            if (FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser).isError()) {
                mstMoldDetail.setExternalFlg(1);
                mstMoldDetail.setMoldTypeText(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "mst_mold.mold_type", String.valueOf(mold.getMoldType()), loginUser.getLangId()));
            } else {
                mstMoldDetail.setExternalFlg(0);
            }
        }

        List<MstComponent> components = new ArrayList<>();
        Iterator<MstMoldComponentRelation> mstMoldComponentRelation = mold.getMstMoldComponentRelationCollection().iterator();
        while (mstMoldComponentRelation.hasNext()) {
            MstMoldComponentRelation mstMoldComponentRelation1 = mstMoldComponentRelation.next();
            MstComponent mstComponent = mstMoldComponentRelation1.getMstComponent();
            if (mstMoldComponentRelation1.getCountPerShot() != null) {
                mstComponent.setCountPerShot(mstMoldComponentRelation1.getCountPerShot());
            } else {
                mstComponent.setCountPerShot(0);
            }
            if (StringUtils.isNotEmpty(mstMoldComponentRelation1.getProcedureId())) {
                mstComponent.setProcedureId(mstMoldComponentRelation1.getProcedureId());
                mstComponent.setProcedureCode(mstMoldComponentRelation1.getMstProcedure() == null ? null : mstMoldComponentRelation1.getMstProcedure().getProcedureCode());
                mstComponent.setProcedureName(mstMoldComponentRelation1.getMstProcedure() == null ? null : mstMoldComponentRelation1.getMstProcedure().getProcedureName());
            }
            components.add(mstComponent);
        }

        if (components.size() > 1) {
            // 部品コード昇順
            Collections.sort(components, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    MstComponent stu1 = (MstComponent) o1;
                    MstComponent stu2 = (MstComponent) o2;
                    return stu1.getComponentCode().compareTo(stu2.getComponentCode());
                }
            });
        }

        mstMoldDetail.setMstComponentList(components);

        if (null != mold.getTotalShotCount()) {
            mstMoldDetail.setTotalShotCount("" + mold.getTotalShotCount());
        } else {
            mstMoldDetail.setTotalShotCount("0");
        }

        if (mold.getLastProductionDate() != null) {
            mstMoldDetail.setLastProductionDate(mold.getLastProductionDate());
            mstMoldDetail.setLastProductionDateStr(fu.getDateFormatForStr(mold.getLastProductionDate()));
        }
        if (mold.getTotalProducingTimeHour() != null) {
            mstMoldDetail.setTotalProducingTimeHour(mold.getTotalProducingTimeHour());
        }
        if (mold.getLastMainteDate() != null) {
            mstMoldDetail.setLastMainteDate(mold.getLastMainteDate());
            mstMoldDetail.setLastMainteDateStr(fu.getDateFormatForStr(mold.getLastMainteDate()));
        }
        if (mold.getAfterMainteTotalProducingTimeHour() != null) {
            mstMoldDetail.setAfterMainteTotalProducingTimeHour(mold.getAfterMainteTotalProducingTimeHour());
        }

        if (mold.getAfterMainteTotalProducingTimeHour() != null) {
            mstMoldDetail.setAfterMainteTotalShotCount(mold.getAfterMainteTotalShotCount());
        }

        mstMoldDetail.setMainteCycleId01(mold.getMainteCycleId01());
        mstMoldDetail.setMainteCycleId02(mold.getMainteCycleId02());
        mstMoldDetail.setMainteCycleId03(mold.getMainteCycleId03());

        return mstMoldDetail;
    }

        
        /**
     * 金型マスタ詳細取得
     *
     * @param moldId
     * @param loginUser
     * @return
     */
    public MstMoldDetail getMstMoldDetails(String moldId, LoginUser loginUser) {
        return this.getMstMoldDetails(null, moldId, loginUser);
    }

    private List getSql(
            String moldId,
            String moldName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer moldType,
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
            Date moldCreatedDateFrom,
            Date moldCreatedDateTo,
            Integer status,
            String action) {

        StringBuilder sql;

        if ("count".equals(action)) {
            sql = new StringBuilder("SELECT count(t0.moldId) ");
        } else {
            sql = new StringBuilder("SELECT t0 ");
        }
        sql = sql.append(" FROM MstMold t0 "
                + "LEFT JOIN FETCH t0.mstCompanyByOwnerCompanyId "
                + "LEFT JOIN FETCH t0.mstCompanyByCompanyId "
                + "LEFT JOIN FETCH t0.mstInstallationSite "
                + "LEFT JOIN FETCH t0.mstLocation "
                + "LEFT JOIN FETCH t0.blMaintenanceCyclePtn01 "
                + "LEFT JOIN FETCH t0.blMaintenanceCyclePtn02 "
                + "LEFT JOIN FETCH t0.blMaintenanceCyclePtn03 "
                + " WHERE 1=1 ");

        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" AND t0.moldId like :moldId ");
        }
        if (moldName != null && !"".equals(moldName)) {
            sql = sql.append(" AND t0.moldName like :moldName ");
        }

        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            sql = sql.append(" AND t0.mainAssetNo LIKE :mainAssetNo ");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" and t0.mstCompanyByOwnerCompanyId.companyName like :ownerCompanyName ");
        }

        if (companyName != null && !"".equals(companyName)) {
            sql = sql.append(" and t0.mstCompanyByCompanyId.companyName like :companyName ");
        }

        if (locationName != null && !"".equals(locationName)) {
            sql = sql.append(" and t0.mstLocation.locationName like :locationName ");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            sql = sql.append(" and t0.mstInstallationSite.installationSiteName like :instllationSiteName ");
        }

        if (moldType != null && 0 != moldType) {
            sql = sql.append(" and t0.moldType = :moldType ");
        }

        if (department != null && 0 != department) {
            sql = sql.append(" and t0.department = :department ");
        }

        if (moldCreatedDateFrom != null) {
            sql = sql.append(" and t0.moldCreatedDate >= :moldCreatedDateFrom ");
        }

        if (moldCreatedDateTo != null) {
            sql = sql.append(" and t0.moldCreatedDate <= :moldCreatedDateTo ");
        }

        if (lastProductionDateFrom != null) {
            sql = sql.append(" and t0.lastProductionDate >= :lastProductionDateFrom ");
        }

        if (lastProductionDateTo != null) {
            sql = sql.append(" and t0.lastProductionDate <= :lastProductionDateTo ");
        }

        if (totalProducingTimeHourFrom != null) {
            sql = sql.append(" and t0.totalProducingTimeHour >= :totalProducingTimeHourFrom ");
        }

        if (totalProducingTimeHourTo != null) {
            sql = sql.append(" and t0.totalProducingTimeHour <= :totalProducingTimeHourTo ");
        }

        if (totalShotCountFrom != null) {
            sql = sql.append(" and t0.totalShotCount >= :totalShotCountFrom ");
        }

        if (totalShotCountTo != null) {
            sql = sql.append(" and t0.totalShotCount <= :totalShotCountTo ");
        }

        if (lastMainteDateFrom != null) {
            sql = sql.append(" and t0.lastMainteDate >= :lastMainteDateFrom ");
        }

        if (lastMainteDateTo != null) {
            sql = sql.append(" and t0.lastMainteDate <= :lastMainteDateTo ");
        }

        if (afterMainteTotalProducingTimeHourFrom != null) {
            sql = sql.append(" and t0.afterMainteTotalProducingTimeHour >= :afterMainteTotalProducingTimeHourFrom ");
        }

        if (afterMainteTotalProducingTimeHourTo != null) {
            sql = sql.append(" and t0.afterMainteTotalProducingTimeHour <= :afterMainteTotalProducingTimeHourTo ");
        }

        if (afterMainteTotalShotCountFrom != null) {
            sql = sql.append(" and t0.afterMainteTotalShotCount >= :afterMainteTotalShotCountFrom ");
        }

        if (afterMainteTotalShotCountTo != null) {
            sql = sql.append(" and t0.afterMainteTotalShotCount <= :afterMainteTotalShotCountTo ");
        }

        if (status != null) {
            sql = sql.append(" and t0.status = :status ");
        }

        sql.append(" order by t0.moldId ");//表示順は金型IDの昇順。

        Query query = entityManager.createQuery(sql.toString());

        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", "%" + moldId + "%");
        }

        if (moldName != null && !"".equals(moldName)) {
            query.setParameter("moldName", "%" + moldName + "%");
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

        if (moldType != null && 0 != moldType) {
            query.setParameter("moldType", moldType);
        }

        if (department != null && 0 != department) {
            query.setParameter("department", department);
        }

        if (moldCreatedDateFrom != null) {
            query.setParameter("moldCreatedDateFrom", moldCreatedDateFrom);
        }
        if (moldCreatedDateTo != null) {
            query.setParameter("moldCreatedDateTo", moldCreatedDateTo);
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

        if (status != null) {
            query.setParameter("status", status);
        }

        List list = query.getResultList();

        return list;

    }
        private List getSqlWithoutDispose(
            String moldId,
            String moldName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer moldType,
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
            Date moldCreatedDateFrom,
            Date moldCreatedDateTo,
            Integer status,
            String action) {

        StringBuilder sql;

        if ("count".equals(action)) {
            sql = new StringBuilder("SELECT count(t0.moldId) ");
        } else {
            sql = new StringBuilder("SELECT t0 ");
        }
        sql = sql.append(" FROM MstMold t0 "
                + "LEFT JOIN FETCH t0.mstCompanyByOwnerCompanyId "
                + "LEFT JOIN FETCH t0.mstCompanyByCompanyId "
                + "LEFT JOIN FETCH t0.mstInstallationSite "
                + "LEFT JOIN FETCH t0.mstLocation "
                + "LEFT JOIN FETCH t0.blMaintenanceCyclePtn01 "
                + "LEFT JOIN FETCH t0.blMaintenanceCyclePtn02 "
                + "LEFT JOIN FETCH t0.blMaintenanceCyclePtn03 "
                + " WHERE 1=1 and t0.status <> 9 ");
        
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" AND t0.moldId like :moldId ");
        }
        if (moldName != null && !"".equals(moldName)) {
            sql = sql.append(" AND t0.moldName like :moldName ");
        }

        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            sql = sql.append(" AND t0.mainAssetNo LIKE :mainAssetNo ");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" and t0.mstCompanyByOwnerCompanyId.companyName like :ownerCompanyName ");
        }

        if (companyName != null && !"".equals(companyName)) {
            sql = sql.append(" and t0.mstCompanyByCompanyId.companyName like :companyName ");
        }

        if (locationName != null && !"".equals(locationName)) {
            sql = sql.append(" and t0.mstLocation.locationName like :locationName ");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            sql = sql.append(" and t0.mstInstallationSite.installationSiteName like :instllationSiteName ");
        }

        if (moldType != null && 0 != moldType) {
            sql = sql.append(" and t0.moldType = :moldType ");
        }

        if (department != null && 0 != department) {
            sql = sql.append(" and t0.department = :department ");
        }

        if (moldCreatedDateFrom != null) {
            sql = sql.append(" and t0.moldCreatedDate >= :moldCreatedDateFrom ");
        }

        if (moldCreatedDateTo != null) {
            sql = sql.append(" and t0.moldCreatedDate <= :moldCreatedDateTo ");
        }

        if (lastProductionDateFrom != null) {
            sql = sql.append(" and t0.lastProductionDate >= :lastProductionDateFrom ");
        }

        if (lastProductionDateTo != null) {
            sql = sql.append(" and t0.lastProductionDate <= :lastProductionDateTo ");
        }

        if (totalProducingTimeHourFrom != null) {
            sql = sql.append(" and t0.totalProducingTimeHour >= :totalProducingTimeHourFrom ");
        }

        if (totalProducingTimeHourTo != null) {
            sql = sql.append(" and t0.totalProducingTimeHour <= :totalProducingTimeHourTo ");
        }

        if (totalShotCountFrom != null) {
            sql = sql.append(" and t0.totalShotCount >= :totalShotCountFrom ");
        }

        if (totalShotCountTo != null) {
            sql = sql.append(" and t0.totalShotCount <= :totalShotCountTo ");
        }

        if (lastMainteDateFrom != null) {
            sql = sql.append(" and t0.lastMainteDate >= :lastMainteDateFrom ");
        }

        if (lastMainteDateTo != null) {
            sql = sql.append(" and t0.lastMainteDate <= :lastMainteDateTo ");
        }

        if (afterMainteTotalProducingTimeHourFrom != null) {
            sql = sql.append(" and t0.afterMainteTotalProducingTimeHour >= :afterMainteTotalProducingTimeHourFrom ");
        }

        if (afterMainteTotalProducingTimeHourTo != null) {
            sql = sql.append(" and t0.afterMainteTotalProducingTimeHour <= :afterMainteTotalProducingTimeHourTo ");
        }

        if (afterMainteTotalShotCountFrom != null) {
            sql = sql.append(" and t0.afterMainteTotalShotCount >= :afterMainteTotalShotCountFrom ");
        }

        if (afterMainteTotalShotCountTo != null) {
            sql = sql.append(" and t0.afterMainteTotalShotCount <= :afterMainteTotalShotCountTo ");
        }

        if (status != null) {
            sql = sql.append(" and t0.status = :status ");
        }

        sql.append(" order by t0.moldId ");//表示順は金型IDの昇順。

        Query query = entityManager.createQuery(sql.toString());

        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", "%" + moldId + "%");
        }

        if (moldName != null && !"".equals(moldName)) {
            query.setParameter("moldName", "%" + moldName + "%");
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

        if (moldType != null && 0 != moldType) {
            query.setParameter("moldType", moldType);
        }

        if (department != null && 0 != department) {
            query.setParameter("department", department);
        }

        if (moldCreatedDateFrom != null) {
            query.setParameter("moldCreatedDateFrom", moldCreatedDateFrom);
        }
        if (moldCreatedDateTo != null) {
            query.setParameter("moldCreatedDateTo", moldCreatedDateTo);
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

        if (status != null) {
            query.setParameter("status", status);
        }

        List list = query.getResultList();

        return list;

    }
    public MstMoldPartRelList getMstMoldPartDetails(String moldUuid, LoginUser loginUser) {
        StringBuilder sql = new StringBuilder("SELECT r FROM MstMoldPartRel r "
                + "WHERE 1=1 ");       
           
        if (moldUuid != null && !moldUuid.isEmpty()) {
            sql.append(" and r.moldUuid = :moldUuid ");
        }
        sql.append(" ORDER BY r.location");
      
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.isEmpty()) {
            query.setParameter("moldUuid", moldUuid);
        }

        MstMoldPartRelList mstMoldPartDetailList = new MstMoldPartRelList();
        List list = query.getResultList();
        mstMoldPartDetailList.setMstMoldPartRels(list);
        return mstMoldPartDetailList;
    }
   /**
     * 金型マスタ詳細取得
     *
     * @param moldUuid
     * @param loginUser
     * @return
     */
    public MstMoldPartRelList getMstMoldPartDetailsByMoldUuid(String moldUuid, LoginUser loginUser) {
        return this.getMstMoldPartDetails(moldUuid, loginUser);
    }
    /**
     *
     * @param langId
     */
    public void outMoldTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outMoldTypeMapTemp == null) {
            outMoldTypeMapTemp = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold.mold_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                outMoldTypeMapTemp.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
        }

    }

    /**
     *
     * @param langId
     * @return
     */
    public Map inMoldTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inMoldTypeMapTemp == null) {
            inMoldTypeMapTemp = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold.mold_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMoldTypeMapTemp.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMoldTypeMapTemp;
    }

    /**
     *
     * @param langId
     * @return
     */
    public Map inDepartmentOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inDepartmentMapTemp == null) {
            inDepartmentMapTemp = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_user.department");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inDepartmentMapTemp.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inDepartmentMapTemp;
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
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold.status");
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

    @Transactional
    public int deleteMstMold(String moldId) {
        Query query = entityManager.createNamedQuery("MstMold.delete");
        query.setParameter("moldId", moldId);
        return query.executeUpdate();
    }

    /**
     *
     * @param moldId
     * @return true:hadResult false:noResult
     */
    public boolean checkMoldUsedByProduction(String moldId) {
        List res = entityManager.createNamedQuery("TblProduction.countByMoldId")
                .setParameter("moldId", moldId)
                .getResultList();
        return null != res && !res.isEmpty() && ((Long) res.get(0)).compareTo(new Long("0")) != 0 ? true : false;
    }

    /**
     *
     * @param moldId
     * @return
     */
    public boolean getMstMoldExistCheck(String moldId) {
        Query query = entityManager.createNamedQuery("MstMold.findByMoldId");
        query.setParameter("moldId", moldId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * 金型M0013 csv取り込み使用
     *
     * @param mstMold
     * @return
     */
    @Transactional
    public int updateMstMoldByQuery(MstMold mstMold) {
        Query query = entityManager.createNamedQuery("MstMold.updateByMoldId");
        query.setParameter("moldName", mstMold.getMoldName());
        query.setParameter("moldType", mstMold.getMoldType());
        query.setParameter("mainAssetNo", mstMold.getMainAssetNo());
        query.setParameter("moldCreatedDate", mstMold.getMoldCreatedDate());
        query.setParameter("inspectedDate", mstMold.getInspectedDate());
        query.setParameter("department", mstMold.getDepartment());
        query.setParameter("ownerCompanyId", mstMold.getOwnerCompanyId());
        query.setParameter("installedDate", mstMold.getInstalledDate());
        query.setParameter("companyId", mstMold.getCompanyId());
        query.setParameter("locationId", mstMold.getLocationId());
        query.setParameter("instllationSiteId", mstMold.getInstllationSiteId());
        query.setParameter("companyName", mstMold.getCompanyName());
        query.setParameter("locationName", mstMold.getLocationName());
        query.setParameter("instllationSiteName", mstMold.getInstllationSiteName());
        query.setParameter("status", mstMold.getStatus());
        query.setParameter("statusChangedDate", mstMold.getStatusChangedDate());
        query.setParameter("imgFilePath01", mstMold.getImgFilePath01());
        query.setParameter("imgFilePath02", mstMold.getImgFilePath02());
        query.setParameter("imgFilePath03", mstMold.getImgFilePath03());
        query.setParameter("imgFilePath04", mstMold.getImgFilePath04());
        query.setParameter("imgFilePath05", mstMold.getImgFilePath05());
        query.setParameter("imgFilePath06", mstMold.getImgFilePath06());
        query.setParameter("imgFilePath07", mstMold.getImgFilePath07());
        query.setParameter("imgFilePath08", mstMold.getImgFilePath08());
        query.setParameter("imgFilePath09", mstMold.getImgFilePath09());
        query.setParameter("imgFilePath10", mstMold.getImgFilePath10());

        query.setParameter("lastProductionDate", mstMold.getLastProductionDate());
        query.setParameter("totalProducingTimeHour", mstMold.getTotalProducingTimeHour());
        query.setParameter("totalShotCount", mstMold.getTotalShotCount());
        query.setParameter("lastMainteDate", mstMold.getLastMainteDate());
        query.setParameter("afterMainteTotalShotCount", mstMold.getAfterMainteTotalShotCount());
        query.setParameter("afterMainteTotalProducingTimeHour", mstMold.getAfterMainteTotalProducingTimeHour());
        query.setParameter("mainteCycleId01", mstMold.getMainteCycleId01());
        query.setParameter("mainteCycleId02", mstMold.getMainteCycleId02());
        query.setParameter("mainteCycleId03", mstMold.getMainteCycleId03());

        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", mstMold.getUpdateUserUuid());
        query.setParameter("moldId", mstMold.getMoldId());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * 金型移動　T0003　使用
     *
     * @param mstMold
     * @return
     */
    @Transactional
    public int updateByMoldIdForLocationHistory(MstMold mstMold) {
        Query query = entityManager.createNamedQuery("MstMold.updateByMoldIdForLocationHistory");
        query.setParameter("installedDate", mstMold.getInstalledDate());
        query.setParameter("companyId", mstMold.getCompanyId());
        query.setParameter("locationId", mstMold.getLocationId());
        query.setParameter("instllationSiteId", mstMold.getInstllationSiteId());
        query.setParameter("companyName", mstMold.getCompanyName());
        query.setParameter("locationName", mstMold.getLocationName());
        query.setParameter("instllationSiteName", mstMold.getInstllationSiteName());
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", mstMold.getUpdateUserUuid());
        query.setParameter("moldId", mstMold.getMoldId());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * 金型マスタ追加
     *
     * @param mstMoldDetail
     * @param loginUser
     */
    @Transactional
    public void createMstMoldByMstMoldDetail(MstMoldDetail mstMoldDetail, LoginUser loginUser) {

        //金型マスタ追加
        String uuid = IDGenerator.generate();
        MstMold mstMold = new MstMold();
        MstMold inputMstMold = mstMoldDetail.getMstMold();
        mstMold.setUuid(uuid);
        mstMold.setMoldId(inputMstMold.getMoldId());
        mstMold.setMoldName(inputMstMold.getMoldName());
        mstMold.setMoldType(inputMstMold.getMoldType());
        mstMold.setMoldCreatedDate(inputMstMold.getMoldCreatedDate());
        mstMold.setInspectedDate(inputMstMold.getInspectedDate());
        mstMold.setStatusChangedDate(new Date());
        if (inputMstMold.getOwnerCompanyId() != null && !"".equals(inputMstMold.getOwnerCompanyId())) {
            mstMold.setOwnerCompanyId(inputMstMold.getOwnerCompanyId());
        }

        if (inputMstMold.getCompanyId() != null && !"".equals(inputMstMold.getCompanyId())) {
            mstMold.setCompanyId(inputMstMold.getCompanyId());
            mstMold.setCompanyName(inputMstMold.getCompanyName());
        }
        if (inputMstMold.getLocationId() != null && !"".equals(inputMstMold.getLocationId())) {
            mstMold.setLocationId(inputMstMold.getLocationId());
            mstMold.setLocationName(inputMstMold.getLocationName());
        }
        if (inputMstMold.getInstllationSiteId() != null && !"".equals(inputMstMold.getInstllationSiteId())) {
            mstMold.setInstllationSiteId(inputMstMold.getInstllationSiteId());
            mstMold.setInstllationSiteName(inputMstMold.getInstllationSiteName());
        }
        if (null != inputMstMold.getCompanyId() || null != inputMstMold.getLocationId() || null != inputMstMold.getInstllationSiteId()) {
            mstMold.setInstalledDate(new Date());
        }
        mstMold.setMainteStatus(0);
        mstMold.setStatus(inputMstMold.getStatus());
        mstMold.setCreateDate(new java.util.Date());
        mstMold.setCreateUserUuid(loginUser.getUserUuid());
        mstMold.setUpdateDate(new java.util.Date());
        mstMold.setUpdateUserUuid(loginUser.getUserUuid());

        String imgFilePath01 = inputMstMold.getImgFilePath01();
        if (imgFilePath01 != null && !"".equals(imgFilePath01.trim())) {
            mstMold.setImgFilePath01(imgFilePath01.trim());
        }

        String imgFilePath02 = inputMstMold.getImgFilePath02();
        if (imgFilePath02 != null && !"".equals(imgFilePath02.trim())) {
            mstMold.setImgFilePath02(imgFilePath02.trim());
        }

        String imgFilePath03 = inputMstMold.getImgFilePath03();
        if (imgFilePath03 != null && !"".equals(imgFilePath03.trim())) {
            mstMold.setImgFilePath03(imgFilePath03.trim());
        }

        String imgFilePath04 = inputMstMold.getImgFilePath04();
        if (imgFilePath04 != null && !"".equals(imgFilePath04.trim())) {
            mstMold.setImgFilePath04(imgFilePath04.trim());
        }

        String imgFilePath05 = inputMstMold.getImgFilePath05();
        if (imgFilePath05 != null && !"".equals(imgFilePath05.trim())) {
            mstMold.setImgFilePath05(imgFilePath05.trim());
        }

        String imgFilePath06 = inputMstMold.getImgFilePath06();
        if (imgFilePath06 != null && !"".equals(imgFilePath06.trim())) {
            mstMold.setImgFilePath06(imgFilePath06.trim());
        }

        String imgFilePath07 = inputMstMold.getImgFilePath07();
        if (imgFilePath07 != null && !"".equals(imgFilePath07.trim())) {
            mstMold.setImgFilePath07(imgFilePath07.trim());
        }

        String imgFilePath08 = inputMstMold.getImgFilePath08();
        if (imgFilePath08 != null && !"".equals(imgFilePath08.trim())) {
            mstMold.setImgFilePath08(imgFilePath08.trim());
        }

        String imgFilePath09 = inputMstMold.getImgFilePath09();
        if (imgFilePath09 != null && !"".equals(imgFilePath09.trim())) {
            mstMold.setImgFilePath09(imgFilePath09.trim());
        }

        String imgFilePath10 = inputMstMold.getImgFilePath10();
        if (imgFilePath10 != null && !"".equals(imgFilePath10.trim())) {
            mstMold.setImgFilePath10(imgFilePath10.trim());
        }

        // 添付ファイル
        String reportFilePath01 = inputMstMold.getReportFilePath01();
        if (reportFilePath01 != null && !"".equals(reportFilePath01.trim())) {
            mstMold.setReportFilePath01(reportFilePath01.trim());
        }

        String reportFilePath02 = inputMstMold.getReportFilePath02();
        if (reportFilePath02 != null && !"".equals(reportFilePath02.trim())) {
            mstMold.setReportFilePath02(reportFilePath02.trim());
        }

        String reportFilePath03 = inputMstMold.getReportFilePath03();
        if (reportFilePath03 != null && !"".equals(reportFilePath03.trim())) {
            mstMold.setReportFilePath01(reportFilePath03.trim());
        }

        String reportFilePath04 = inputMstMold.getReportFilePath04();
        if (reportFilePath04 != null && !"".equals(reportFilePath04.trim())) {
            mstMold.setReportFilePath01(reportFilePath04.trim());
        }

        String reportFilePath05 = inputMstMold.getReportFilePath05();
        if (reportFilePath05 != null && !"".equals(reportFilePath05.trim())) {
            mstMold.setReportFilePath05(reportFilePath05.trim());
        }
        String reportFilePath06 = inputMstMold.getReportFilePath06();
        if (reportFilePath06 != null && !"".equals(reportFilePath06.trim())) {
            mstMold.setReportFilePath06(reportFilePath06.trim());
        }
        String reportFilePath07 = inputMstMold.getReportFilePath07();
        if (reportFilePath07 != null && !"".equals(reportFilePath07.trim())) {
            mstMold.setReportFilePath07(reportFilePath07.trim());
        }
        String reportFilePath08 = inputMstMold.getReportFilePath07();
        if (reportFilePath08 != null && !"".equals(reportFilePath08.trim())) {
            mstMold.setReportFilePath08(reportFilePath08.trim());
        }
        String reportFilePath09 = inputMstMold.getReportFilePath09();
        if (reportFilePath09 != null && !"".equals(reportFilePath09.trim())) {
            mstMold.setReportFilePath09(reportFilePath09.trim());
        }

        String reportFilePath10 = inputMstMold.getReportFilePath10();
        if (reportFilePath10 != null && !"".equals(reportFilePath10.trim())) {
            mstMold.setReportFilePath10(reportFilePath10.trim());
        }

        if (null != inputMstMold.getDepartment()) {
            mstMold.setDepartment(inputMstMold.getDepartment());
        } else {
            mstMold.setDepartment(0);
        }

        mstMold.setLastProductionDate(inputMstMold.getLastProductionDate());
        if (null != inputMstMold.getTotalProducingTimeHour()) {
            mstMold.setTotalProducingTimeHour(inputMstMold.getTotalProducingTimeHour());
        } else {
            mstMold.setTotalProducingTimeHour(BigDecimal.ZERO);
        }
        if (null != inputMstMold.getTotalShotCount()) {
            mstMold.setTotalShotCount(inputMstMold.getTotalShotCount());
        } else {
            mstMold.setTotalShotCount(0);
        }
        mstMold.setLastMainteDate(inputMstMold.getLastMainteDate());
        if (null != inputMstMold.getAfterMainteTotalProducingTimeHour()) {
            mstMold.setAfterMainteTotalProducingTimeHour(inputMstMold.getAfterMainteTotalProducingTimeHour());
        } else {
            mstMold.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
        }
        if (null != inputMstMold.getAfterMainteTotalShotCount()) {
            mstMold.setAfterMainteTotalShotCount(inputMstMold.getAfterMainteTotalShotCount());
        } else {
            mstMold.setAfterMainteTotalShotCount(0);
        }
        if (!StringUtils.isEmpty(inputMstMold.getMainteCycleId01())) {
            mstMold.setMainteCycleId01(inputMstMold.getMainteCycleId01());
        } else {
            mstMold.setMainteCycleId01(null);
        }
        if (!StringUtils.isEmpty(inputMstMold.getMainteCycleId02())) {
            mstMold.setMainteCycleId02(inputMstMold.getMainteCycleId02());
        } else {
            mstMold.setMainteCycleId02(null);
        }
        if (!StringUtils.isEmpty(inputMstMold.getMainteCycleId03())) {
            mstMold.setMainteCycleId03(inputMstMold.getMainteCycleId03());
        } else {
            mstMold.setMainteCycleId03(null);
        }

        //代表資産番号を登録
        if (inputMstMold.getMainAssetNo() != null && !inputMstMold.getMainAssetNo().trim().equals("")) {
            mstMold.setMainAssetNo(inputMstMold.getMainAssetNo());
            Set<MstMoldAssetNo> setMstMoldAssetNo = new HashSet<>();
            MstMoldAssetNo mstMoldAssetNo = new MstMoldAssetNo();
            MstMoldAssetNoPK mstMoldAssetNoPK = new MstMoldAssetNoPK();
            mstMoldAssetNoPK.setMoldUuid(uuid);
            mstMoldAssetNoPK.setAssetNo(inputMstMold.getMainAssetNo());
            mstMoldAssetNo.setId(IDGenerator.generate());
            mstMoldAssetNo.setMainFlg(1);
            mstMoldAssetNo.setAssetAmount(BigDecimal.ZERO);
            mstMoldAssetNo.setNumberedDate(new Date());
            mstMoldAssetNo.setCreateDate(new Date());
            mstMoldAssetNo.setCreateUserUuid(loginUser.getUserUuid());
            mstMoldAssetNo.setMstMoldAssetNoPK(mstMoldAssetNoPK);
            setMstMoldAssetNo.add(mstMoldAssetNo);
            mstMold.setMstMoldAssetNoCollection(setMstMoldAssetNo);
        } else {
            mstMold.setMstMoldAssetNoCollection(null);
        }

        //金型部品関係マスタ登録
        if (mstMoldDetail.getMstComponentList() != null && mstMoldDetail.getMstComponentList().size() > 0) {
            Set<MstMoldComponentRelation> mstMoldComponentRelations = new HashSet<>();
            for (int i = 0; i < mstMoldDetail.getMstComponentList().size(); i++) {
                MstComponent mstComponent = mstMoldDetail.getMstComponentList().get(i);
                if (null == mstComponent.getId() || mstComponent.getId().trim().equals("")) {
                    continue;
                }

                MstMoldComponentRelation inMstMoldComponentRelation = new MstMoldComponentRelation();
                MstMoldComponentRelationPK mstMoldComponentRelationPK = new MstMoldComponentRelationPK();
                mstMoldComponentRelationPK.setComponentId(mstComponent.getId());
                mstMoldComponentRelationPK.setMoldUuid(uuid);
                inMstMoldComponentRelation.setMstMoldComponentRelationPK(mstMoldComponentRelationPK);
                if (StringUtils.isNotEmpty(mstComponent.getProcedureId())) {
                    inMstMoldComponentRelation.setProcedureId(mstComponent.getProcedureId());
                }
                try {
                    if (mstComponent.getCountPerShot() != null) {
                        inMstMoldComponentRelation.setCountPerShot(mstComponent.getCountPerShot());
                    } else {
                        inMstMoldComponentRelation.setCountPerShot(0);
                    }
                } catch (NumberFormatException e) {
                    inMstMoldComponentRelation.setCountPerShot(0);
                }

                inMstMoldComponentRelation.setCreateDate(new java.util.Date());
                inMstMoldComponentRelation.setCreateUserUuid(loginUser.getUserUuid());
                inMstMoldComponentRelation.setUpdateDate(new java.util.Date());
                inMstMoldComponentRelation.setUpdateUserUuid(loginUser.getUserUuid());
                mstMoldComponentRelations.add(inMstMoldComponentRelation);
            }
            mstMold.setMstMoldComponentRelationCollection(new ArrayList(mstMoldComponentRelations));
        }

        MstMoldSpecHistory mstMoldSpecHistory = null;
        //金型仕様マスタ追加
        if (mstMoldDetail.getMstMoldSpec() != null && mstMoldDetail.getMstMoldSpec().size() > 0) {
            Set<MstMoldSpecHistory> mshc = new HashSet<>();
            mstMoldSpecHistory = new MstMoldSpecHistory();
            mstMoldSpecHistory.setId(IDGenerator.generate());
            mstMoldSpecHistory.setCreateDate(new Date());
            mstMoldSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
            mstMoldSpecHistory.setStartDate(new Date());
            mstMoldSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付
            mstMoldSpecHistory.setMoldUuid(mstMold.getUuid());
            //金型仕様履歴名称を「最初のバージョン」(文言キー:mold_spec_first_version）
            mstMoldSpecHistory.setMoldSpecName(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_spec_first_version"));
            mshc.add(mstMoldSpecHistory);
            mstMold.setMstMoldSpecHistoryCollection(new ArrayList(mshc));
        }

        //金型加工条件マスタ追加
        Set<MstMoldProcCondSpec> mstMoldProcCondSpecs;
        Map moldProcCondsMap = new HashMap();
        if (mstMoldDetail.getMstMoldProcConds() != null && mstMoldDetail.getMstMoldProcConds().size() > 0) {
            for (int mpFlag = 0; mpFlag < mstMoldDetail.getMstMoldProcConds().size(); mpFlag++) {
                MstMoldProcConds aMstMoldProcConds = mstMoldDetail.getMstMoldProcConds().get(mpFlag);
                if ("1".equals(aMstMoldProcConds.getDeleteFlag())) {
                    //削除の記し
                    String id = aMstMoldProcConds.getId();
                    if (null != id) {
                        mstMoldProcCondService.deleteProcCondNameById(id);
                    }
                    continue;
                }
                MstMoldProcCond moldProcCond = new MstMoldProcCond();
                moldProcCond.setId(IDGenerator.generate());
                moldProcCond.setMoldProcCondName(aMstMoldProcConds.getMoldProcCondName());
                moldProcCond.setMoldUuid(mstMold.getUuid());
                moldProcCond.setCreateDate(new Date());
                moldProcCond.setCreateUserUuid(loginUser.getUserUuid());
                moldProcCond.setSeq(mpFlag);
                moldProcCond.setUpdateDate(new Date());
                moldProcCond.setUpdateUserUuid(loginUser.getUserUuid());
                moldProcCondsMap.put(aMstMoldProcConds.getMoldProcCondName(), moldProcCond);
            }
        }

        if (!moldProcCondsMap.isEmpty() && mstMoldDetail.getMstMoldProcCondSpecVos() != null && mstMoldDetail.getMstMoldProcCondSpecVos().size() > 0) {
            Set<MstMoldProcCond> mstMoldProcConds = new HashSet<>();
            MstMoldProcCond mstMoldProcCond = null;
            mstMoldProcCondSpecs = new HashSet<>();
            for (int i = 0; i < mstMoldDetail.getMstMoldProcCondSpecVos().size(); i++) {
                MstMoldProcCondSpecPK pk = new MstMoldProcCondSpecPK();
                MstMoldProcCondSpecs moldProcCondSpecVo = mstMoldDetail.getMstMoldProcCondSpecVos().get(i);
                MstMoldProcCondSpec moldProcCondSpec = new MstMoldProcCondSpec();
                mstMoldProcCond = (MstMoldProcCond) moldProcCondsMap.get(moldProcCondSpecVo.getMoldProcCondName());
                pk.setMoldProcCondId(mstMoldProcCond.getId());
                pk.setAttrId(moldProcCondSpecVo.getMstMoldProcCondSpecPK().getAttrId());

                moldProcCondSpec.setId(IDGenerator.generate());
                moldProcCondSpec.setCreateDate(new Date());
                moldProcCondSpec.setCreateUserUuid(loginUser.getUserUuid());
                moldProcCondSpec.setUpdateDate(new Date());
                moldProcCondSpec.setUpdateUserUuid(loginUser.getUserUuid());
                moldProcCondSpec.setMstMoldProcCondSpecPK(pk);
                moldProcCondSpec.setAttrValue(moldProcCondSpecVo.getAttrValue());
                mstMoldProcCondSpecs.add(moldProcCondSpec);
            }

            if (null != mstMoldProcCond) {
                mstMoldProcCond.setMstMoldProcCondSpecCollection(mstMoldProcCondSpecs);
                mstMoldProcConds.add(mstMoldProcCond);
            }
            mstMold.setMstMoldProcCondCollection(mstMoldProcConds);
        }

        //金型所在履歴マスタ編集
        if (null != mstMold.getInstllationSiteId()
                || null != mstMold.getLocationId()
                || null != mstMold.getCompanyId()) {
            Set<TblMoldLocationHistory> tblMoldLocationHistorys = new HashSet<>();
            TblMoldLocationHistory tblMoldLocationHistory = new TblMoldLocationHistory();
            tblMoldLocationHistory.setId(IDGenerator.generate());
            tblMoldLocationHistory.setMoldUuid(mstMold.getUuid());
            tblMoldLocationHistory.setChangeReason(0); //変更理由：なし（ゼロ）
            tblMoldLocationHistory.setCompanyId(null == mstMold.getCompanyId() ? null : mstMold.getCompanyId());
            tblMoldLocationHistory.setCompanyName(null == mstMold.getCompanyName() ? null : mstMold.getCompanyName());

            tblMoldLocationHistory.setLocationId(null == mstMold.getLocationId() ? mstMold.getLocationId() : mstMold.getLocationId());
            tblMoldLocationHistory.setLocationName(null == mstMold.getLocationName() ? mstMold.getLocationName() : mstMold.getLocationName());

            tblMoldLocationHistory.setInstllationSiteId(null == mstMold.getInstllationSiteId() ? null : mstMold.getInstllationSiteId());
            tblMoldLocationHistory.setInstllationSiteName(null == mstMold.getInstllationSiteName() ? null : mstMold.getInstllationSiteName());
            tblMoldLocationHistory.setUpdateDate(new Date());
            tblMoldLocationHistory.setUpdateUserUuid(loginUser.getUserUuid());
            tblMoldLocationHistory.setCreateDate(new Date());
            tblMoldLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
            tblMoldLocationHistory.setStartDate(null == mstMold.getInstalledDate() ? new Date() : mstMold.getInstalledDate());
            tblMoldLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付/todo
            tblMoldLocationHistorys.add(tblMoldLocationHistory);
            mstMold.setTblMoldLocationHistoryCollection(tblMoldLocationHistorys);
        } else {
            mstMold.setInstalledDate(null);
        }

        entityManager.persist(mstMold);

        if (mstMoldDetail.getMstMoldSpec() != null && mstMoldDetail.getMstMoldSpec().size() > 0) {
            for (int i = 0; i < mstMoldDetail.getMstMoldSpec().size(); i++) {
                MstMoldSpecPK mstMoldSpecPK = new MstMoldSpecPK();
                MstMoldSpec mstMoldSpec = new MstMoldSpec();
                MstMoldSpec formMoldSpec = mstMoldDetail.getMstMoldSpec().get(i);
                mstMoldSpec.setAttrValue(formMoldSpec.getAttrValue());
                mstMoldSpec.setId(IDGenerator.generate());
                mstMoldSpecPK.setAttrId(formMoldSpec.getMstMoldSpecPK().getAttrId());
                mstMoldSpecPK.setMoldSpecHstId(mstMoldSpecHistory.getId());
                mstMoldSpec.setMstMoldSpecPK(mstMoldSpecPK);
                mstMoldSpec.setCreateDate(new Date());
                mstMoldSpec.setCreateUserUuid(loginUser.getUserUuid());
                mstMoldSpec.setUpdateDate(new Date());
                mstMoldSpec.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(mstMoldSpec);
            }
        }
        
        if (null != mstMoldDetail.getMstMoldPartRels() && !mstMoldDetail.getMstMoldPartRels().isEmpty()) {
            for (int i = 0; i < mstMoldDetail.getMstMoldPartRels().size(); i++) {
                MstMoldPartRelDetail moldPartRel = (MstMoldPartRelDetail) mstMoldDetail.getMstMoldPartRels().get(i);
                if (1 != moldPartRel.getDeleteFlag()) {
                    MstMoldPartRel newMoldPartRel = new MstMoldPartRel();

                    newMoldPartRel.setId(IDGenerator.generate());
                    newMoldPartRel.setMoldUuid(mstMold.getUuid());
                    newMoldPartRel.setLocation(moldPartRel.getLocation());
                    newMoldPartRel.setMoldPartId(moldPartRel.getMoldPartId());
                    newMoldPartRel.setQuantity(moldPartRel.getQuantity());
                    newMoldPartRel.setAlias(moldPartRel.getAlias());
                    newMoldPartRel.setRplClShotCnt(moldPartRel.getRplClShotCnt());
                    newMoldPartRel.setRplClProdTimeHour(moldPartRel.getRplClProdTimeHour());
                    newMoldPartRel.setRplClLappsedDay(moldPartRel.getRplClLappsedDay());
                    newMoldPartRel.setRprClShotCnt(moldPartRel.getRprClShotCnt());
                    newMoldPartRel.setRprClProdTimeHour(moldPartRel.getRprClProdTimeHour());
                    newMoldPartRel.setRprClLappsedDay(moldPartRel.getRprClLappsedDay());
                    newMoldPartRel.setAftRplShotCnt(moldPartRel.getAftRplShotCnt());
                    newMoldPartRel.setAftRplProdTimeHour(moldPartRel.getAftRplProdTimeHour());
                    newMoldPartRel.setLastRplDatetime(moldPartRel.getLastRplDatetime());
                    newMoldPartRel.setAftRprShotCnt(moldPartRel.getAftRprShotCnt());
                    newMoldPartRel.setAftRprProdTimeHour(moldPartRel.getAftRprProdTimeHour());
                    newMoldPartRel.setLastRprDatetime(moldPartRel.getLastRprDatetime());

                    newMoldPartRel.setUpdateDate(new Date());
                    newMoldPartRel.setUpdateUserUuid(loginUser.getUserUuid());  
                    newMoldPartRel.setCreateDate(new Date());
                    newMoldPartRel.setCreateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(newMoldPartRel);
                }
            }
        }
    }

    @Transactional
    private void updateMstMoldForLocationChange(MstMold moldNewJsonData, MstMold moldOldData, LoginUser loginUser){
       
        StringBuilder jpql = new StringBuilder();
        jpql.append("UPDATE MstMold m SET ");
        jpql.append(" m.companyId = :companyId, ");
        jpql.append(" m.companyName = :companyName, ");
        jpql.append(" m.locationId = :locationId, ");
        jpql.append(" m.locationName = :locationName, ");
        jpql.append(" m.instllationSiteId = :instllationSiteId, ");
        jpql.append(" m.instllationSiteName = :instllationSiteName, ");
        jpql.append(" m.installedDate  = :installedDate, ");
        jpql.append(" m.updateDate = :updateDate, ");
        jpql.append(" m.updateUserUuid = :updateUserUuid ");
        jpql.append(" WHERE m.moldId = :moldId");

        //Installed date is updated only when it is null. When it is null, set the current date. Do NOT change if it has value.
        Query query = entityManager.createQuery(jpql.toString());
       
        query.setParameter("companyId", (StringUtils.isBlank(moldNewJsonData.getCompanyId()))? null : moldNewJsonData.getCompanyId());
        query.setParameter("companyName", (StringUtils.isBlank(moldNewJsonData.getCompanyName())) ? null: moldNewJsonData.getCompanyName());
        query.setParameter("locationId", (StringUtils.isBlank(moldNewJsonData.getLocationId())) ? null: moldNewJsonData.getLocationId());
        query.setParameter("locationName", (StringUtils.isBlank(moldNewJsonData.getLocationName())) ? null: moldNewJsonData.getLocationName());
        query.setParameter("instllationSiteId", (StringUtils.isBlank(moldNewJsonData.getInstllationSiteId()))? null: moldNewJsonData.getInstllationSiteId());
        query.setParameter("instllationSiteName", (StringUtils.isBlank(moldNewJsonData.getInstllationSiteName()))? null: moldNewJsonData.getInstllationSiteName());     
        query.setParameter("installedDate", null == moldOldData.getInstalledDate() ? new Date() : moldOldData.getInstalledDate());
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("moldId", moldNewJsonData.getMoldId());
       
        query.executeUpdate();       
    }
    
    @Transactional
    public void updateMoldChangeLocationHistory(MstMold moldNewJsonData, LoginUser loginUser) throws Exception{
        
        //Get data by moldId to check existance of installedDate
        Query updateQuery = entityManager.createNamedQuery("MstMold.findByMoldId");
        updateQuery.setParameter("moldId", moldNewJsonData.getMoldId());
        Object moldQueryData = updateQuery.getSingleResult();
        MstMold moldOldData = (MstMold)moldQueryData;  
        
        //Get Latest MstMold Data to use moldUuid
        updateMstMoldForLocationChange(moldNewJsonData, moldOldData, loginUser);   
           
        // Get sorted data from TblMoldLocationHistory
        TblMoldLocationHistorys moldLocationHistorysData = tblMoldLocationHistoryService.getMoldLocationHistories(moldNewJsonData.getMoldId(), loginUser);
           
        if (moldLocationHistorysData.getTblMoldLocationHistoryVos().size() > 0){
            //Update Mold Location History Date  
            //Get top data from mold location history list

            TblMoldLocationHistorys moldLocationHistorysTopData = (TblMoldLocationHistorys)moldLocationHistorysData.getTblMoldLocationHistoryVos().get(0); 
            TblMoldLocationHistory tblMoldLocationHistory =entityManager.find(TblMoldLocationHistory.class, moldLocationHistorysTopData.getId());
            
            tblMoldLocationHistory.setMoldUuid(moldOldData.getUuid());
            tblMoldLocationHistory.setCompanyId(StringUtils.isBlank(moldNewJsonData.getCompanyId())? null: moldNewJsonData.getCompanyId());
            tblMoldLocationHistory.setCompanyName(StringUtils.isBlank(moldNewJsonData.getCompanyName())? null: moldNewJsonData.getCompanyName());
            tblMoldLocationHistory.setLocationId(StringUtils.isBlank(moldNewJsonData.getLocationId())? null: moldNewJsonData.getLocationId());
            tblMoldLocationHistory.setLocationName(StringUtils.isBlank(moldNewJsonData.getLocationName())? null: moldNewJsonData.getLocationName());                 
            tblMoldLocationHistory.setInstllationSiteId(StringUtils.isBlank(moldNewJsonData.getInstllationSiteId())? null: moldNewJsonData.getInstllationSiteId());
            tblMoldLocationHistory.setInstllationSiteName(StringUtils.isBlank(moldNewJsonData.getInstllationSiteName())? null: moldNewJsonData.getInstllationSiteName());
            tblMoldLocationHistory.setUpdateDate(new Date());
            tblMoldLocationHistory.setUpdateUserUuid(loginUser.getUserUuid());
            
            entityManager.merge(tblMoldLocationHistory);
        }else{       
            //Insert Data into Mold Location History             
            TblMoldLocationHistory objMoldLocHistory = new TblMoldLocationHistory();
            objMoldLocHistory.setId(IDGenerator.generate());
            objMoldLocHistory.setMoldUuid(moldOldData.getUuid());                        
            objMoldLocHistory.setStartDate(new Date());
            objMoldLocHistory.setEndDate(CommonConstants.SYS_MAX_DATE);
            objMoldLocHistory.setCompanyId(StringUtils.isBlank(moldNewJsonData.getCompanyId())? null : moldNewJsonData.getCompanyId());
            objMoldLocHistory.setCompanyName(StringUtils.isBlank(moldNewJsonData.getCompanyName())? null: moldNewJsonData.getCompanyName());
            objMoldLocHistory.setLocationId(StringUtils.isBlank(moldNewJsonData.getLocationId())? null: moldNewJsonData.getLocationId());
            objMoldLocHistory.setLocationName(StringUtils.isBlank(moldNewJsonData.getLocationName())? null: moldNewJsonData.getLocationName());
            objMoldLocHistory.setInstllationSiteId(StringUtils.isBlank(moldNewJsonData.getInstllationSiteId())? null: moldNewJsonData.getInstllationSiteId());
            objMoldLocHistory.setInstllationSiteName(StringUtils.isBlank(moldNewJsonData.getInstllationSiteName())? null: moldNewJsonData.getInstllationSiteName());
            objMoldLocHistory.setUpdateDate(new Date());
            objMoldLocHistory.setUpdateUserUuid(loginUser.getUserUuid());
            objMoldLocHistory.setCreateDate(new Date());
            objMoldLocHistory.setCreateUserUuid(loginUser.getUserUuid());
            
            entityManager.persist(objMoldLocHistory);
        }               
    } 
    /**
     *
     * @param specId
     * @param attrId
     * @return true:hadResult false:noResult
     */
    public boolean CheckSpecIdAndAttrId(String specId, String attrId) {
        Query query = entityManager.createNamedQuery("MstMoldSpec.findByByMoldSpecPk");
        query.setParameter("attrId", attrId);
        query.setParameter("moldSpecHstId", specId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     *
     * @param proCondId
     * @param attrId
     * @return true:hadResult false:noResult
     */
    public boolean CheckproCondIdAndAttrId(String proCondId, String attrId) {
        Query query = entityManager.createNamedQuery("MstMoldProcCondSpec.findByMoldProcCondIdAndAttrId");
        query.setParameter("moldProcCondId", proCondId);
        query.setParameter("attrId", attrId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * 金型マスタ追加bycsv
     *
     * @param mstMold
     */
    @Transactional
    public void createMstMoldByCsv(MstMold mstMold) {
        entityManager.persist(mstMold);
    }

    /**
     * 金型マスタ更新
     *
     * @param mstMoldDetail
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateMstMoldByMoldDetail(MstMoldDetail mstMoldDetail, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        boolean needNewSpecHistory = null == mstMoldDetail.getMstMoldSpec() || mstMoldDetail.getMstMoldSpec().isEmpty() ? false : true;
        MstMold oldMstMold = entityManager.find(MstMold.class, mstMoldDetail.getMstMold().getMoldId());
        String modeType = oldMstMold.getMoldType().toString();
        //金型種類変更時 すべての金型仕様履歴を削除し
        MstMoldSpecHistory mstMoldSpecHistory = null;
        if (modeType.equals(mstMoldDetail.getMstMold().getMoldType().toString()) == false) {
            Query queryMoldSpecHistory = entityManager.createNamedQuery("MstMoldSpecHistory.findByMoldUuid");
            queryMoldSpecHistory.setParameter("moldUuid", oldMstMold.getUuid());
            List<MstMoldSpecHistory> oldMoldSpecHistorys = queryMoldSpecHistory.getResultList();
            for (int i = 0; i < oldMoldSpecHistorys.size(); i++) {
                entityManager.remove(oldMoldSpecHistorys.get(i));
            }

            //金型仕様マスタ追加
            if (mstMoldDetail.getMstMoldSpec() != null && mstMoldDetail.getMstMoldSpec().size() > 0) {
                mstMoldSpecHistory = new MstMoldSpecHistory();
                mstMoldSpecHistory.setId(IDGenerator.generate());
                mstMoldSpecHistory.setCreateDate(new Date());
                mstMoldSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
                mstMoldSpecHistory.setStartDate(new Date());
                mstMoldSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付
                mstMoldSpecHistory.setMoldUuid(mstMoldDetail.getMstMold().getUuid());
                mstMoldSpecHistory.setMstMold(mstMoldDetail.getMstMold());
                //金型仕様履歴名称を「最初のバージョン」(文言キー:mold_spec_first_version）
                mstMoldSpecHistory.setMoldSpecName(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_spec_first_version"));

                mstMoldSpecHistory.setUpdateDate(new Date());
                mstMoldSpecHistory.setUpdateUserUuid(loginUser.getUserUuid());

                Set<MstMoldSpecHistory> mshc = new HashSet<>();
                mshc.add(mstMoldSpecHistory);
                mstMoldDetail.getMstMold().setMstMoldSpecHistoryCollection(mshc);
                entityManager.persist(mstMoldSpecHistory);
            }
        } else {
            if (needNewSpecHistory == true && mstMoldDetail.getMstMoldSpec() != null && mstMoldDetail.getMstMoldSpec().size() > 0) {
                for (MstMoldSpec aSpec : mstMoldDetail.getMstMoldSpec()) {
                    if (null != aSpec.getMstMoldSpecPK().getMoldSpecHstId() && !aSpec.getMstMoldSpecPK().getMoldSpecHstId().trim().equals("")) {
                        needNewSpecHistory = false;
                    }
                }
            }

            if (needNewSpecHistory == true) {
                //金型仕様マスタ追加
                mstMoldSpecHistory = new MstMoldSpecHistory();
                mstMoldSpecHistory.setId(IDGenerator.generate());
                mstMoldSpecHistory.setCreateDate(new Date());
                mstMoldSpecHistory.setCreateUserUuid(loginUser.getUserUuid());
                mstMoldSpecHistory.setStartDate(new Date());
                mstMoldSpecHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付/todo
                mstMoldSpecHistory.setMoldUuid(mstMoldDetail.getMstMold().getUuid());
                mstMoldSpecHistory.setMstMold(mstMoldDetail.getMstMold());
                //金型仕様履歴名称を「最初のバージョン」(文言キー:mold_spec_first_version）
                mstMoldSpecHistory.setMoldSpecName(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_spec_first_version"));

                mstMoldSpecHistory.setUpdateDate(new Date());
                mstMoldSpecHistory.setUpdateUserUuid(loginUser.getUserUuid());

                Set<MstMoldSpecHistory> mshc = new HashSet<>();
                mshc.add(mstMoldSpecHistory);
                mstMoldDetail.getMstMold().setMstMoldSpecHistoryCollection(mshc);
                entityManager.persist(mstMoldSpecHistory);
            }
        }

        //金型マスタ更新
        MstMold formMold = mstMoldDetail.getMstMold();
        MstMold updateMstMold = entityManager.find(MstMold.class, formMold.getMoldId());
        updateMstMold.setMoldName(formMold.getMoldName());
        updateMstMold.setMoldType(formMold.getMoldType());

        if (null != formMold.getDepartment()) {
            updateMstMold.setDepartment(formMold.getDepartment());
        } else {
            updateMstMold.setDepartment(0);
        }

        // 金型対応代表番号のＦｌａｇを0にする
        Query queryMstMoldAssetNo = entityManager.createNamedQuery("MstMoldAssetNo.findByMoldUuid");
        queryMstMoldAssetNo.setParameter("moldUuid", updateMstMold.getUuid());
        List<MstMoldAssetNo> oldMstMoldAssetNo = queryMstMoldAssetNo.getResultList();
        for (int i = 0; i < oldMstMoldAssetNo.size(); i++) {
            MstMoldAssetNo mMstMoldAssetNo = oldMstMoldAssetNo.get(i);
            mMstMoldAssetNo.setMainFlg(0);
            entityManager.merge(mMstMoldAssetNo);
        }

        if (null != formMold.getMainAssetNo() && !formMold.getMainAssetNo().trim().equals("")) {
            MstMoldAssetNo mstMoldAssetNo = new MstMoldAssetNo();
            MstMoldAssetNoPK mstMoldAssetNoPK = new MstMoldAssetNoPK();
            mstMoldAssetNoPK.setMoldUuid(updateMstMold.getUuid());
            mstMoldAssetNoPK.setAssetNo(formMold.getMainAssetNo());
            mstMoldAssetNo.setMstMoldAssetNoPK(mstMoldAssetNoPK);
            MstMoldAssetNo updateMstMoldAssetNo = entityManager.find(MstMoldAssetNo.class, mstMoldAssetNoPK);
            if (null != updateMstMoldAssetNo) {
                mstMoldAssetNo.setMainFlg(1);
                mstMoldAssetNo.setAssetAmount(updateMstMoldAssetNo.getAssetAmount());
                mstMoldAssetNo.setNumberedDate(updateMstMoldAssetNo.getNumberedDate());
                mstMoldAssetNo.setUpdateDate(new Date());
                mstMoldAssetNo.setUpdateUserUuid(loginUser.getUserUuid());
                mstMoldAsseNoService.updateMstMoldAssetNoByQuery(mstMoldAssetNo);
            } else {
                Set<MstMoldAssetNo> setMstMoldAssetNo = new HashSet<>();
                mstMoldAssetNo.setMainFlg(1);
                mstMoldAssetNo.setAssetAmount(BigDecimal.ZERO);
                mstMoldAssetNo.setNumberedDate(new Date());
                mstMoldAssetNo.setCreateDate(new Date());
                mstMoldAssetNo.setCreateUserUuid(loginUser.getUserUuid());
                mstMoldAssetNo.setId(IDGenerator.generate());
                setMstMoldAssetNo.add(mstMoldAssetNo);
                updateMstMold.setMstMoldAssetNoCollection(setMstMoldAssetNo);
            }

        } else {
            updateMstMold.setMstMoldAssetNoCollection(null);
        }
        updateMstMold.setMainAssetNo(formMold.getMainAssetNo());
        updateMstMold.setMoldCreatedDate(formMold.getMoldCreatedDate());
        updateMstMold.setInspectedDate(formMold.getInspectedDate());

        if (null != formMold.getOwnerCompanyId() && !formMold.getOwnerCompanyId().trim().equals("")) {
            updateMstMold.setOwnerCompanyId(formMold.getOwnerCompanyId());
        } else {
            updateMstMold.setOwnerCompanyId(null);
        }
        if (null != formMold.getCompanyId() && !formMold.getCompanyId().trim().equals("")) {
            updateMstMold.setCompanyId(formMold.getCompanyId());
            updateMstMold.setCompanyName(formMold.getCompanyName());
        } else {
            updateMstMold.setCompanyId(null);
            updateMstMold.setCompanyName(null);
        }
        if (null != formMold.getLocationId() && !formMold.getLocationId().trim().equals("")) {
            updateMstMold.setLocationId(formMold.getLocationId());
            updateMstMold.setLocationName(formMold.getLocationName());
        } else {
            updateMstMold.setLocationId(null);
            updateMstMold.setLocationName(null);
        }
        if (null != formMold.getInstllationSiteId() && !formMold.getInstllationSiteId().trim().equals("")) {
            updateMstMold.setInstllationSiteId(formMold.getInstllationSiteId());
            updateMstMold.setInstllationSiteName(formMold.getInstllationSiteName());
        } else {
            updateMstMold.setInstllationSiteId(null);
            updateMstMold.setInstllationSiteName(null);
        }

        if (null == oldMstMold.getInstalledDate() && (null != updateMstMold.getCompanyId() || null != updateMstMold.getLocationId() || null != updateMstMold.getInstllationSiteId())) {
            updateMstMold.setInstalledDate(new Date());
        }

        //ステータス日
        if (oldMstMold.getStatus().compareTo(formMold.getStatus()) != 0) {
            updateMstMold.setStatus(formMold.getStatus());
            updateMstMold.setStatusChangedDate(new Date());
        } else {
            updateMstMold.setStatus(oldMstMold.getStatus());
            updateMstMold.setStatusChangedDate(oldMstMold.getStatusChangedDate());
        }

        updateMstMold.setImgFilePath01(formMold.getImgFilePath01());
        updateMstMold.setImgFilePath02(formMold.getImgFilePath02());
        updateMstMold.setImgFilePath03(formMold.getImgFilePath03());
        updateMstMold.setImgFilePath04(formMold.getImgFilePath04());
        updateMstMold.setImgFilePath05(formMold.getImgFilePath05());
        updateMstMold.setImgFilePath06(formMold.getImgFilePath06());
        updateMstMold.setImgFilePath07(formMold.getImgFilePath07());
        updateMstMold.setImgFilePath08(formMold.getImgFilePath08());
        updateMstMold.setImgFilePath09(formMold.getImgFilePath09());
        updateMstMold.setImgFilePath10(formMold.getImgFilePath10());

        updateMstMold.setReportFilePath01(formMold.getReportFilePath01());
        updateMstMold.setReportFilePath02(formMold.getReportFilePath02());
        updateMstMold.setReportFilePath03(formMold.getReportFilePath03());
        updateMstMold.setReportFilePath04(formMold.getReportFilePath04());
        updateMstMold.setReportFilePath05(formMold.getReportFilePath05());
        updateMstMold.setReportFilePath06(formMold.getReportFilePath06());
        updateMstMold.setReportFilePath07(formMold.getReportFilePath07());
        updateMstMold.setReportFilePath08(formMold.getReportFilePath08());
        updateMstMold.setReportFilePath09(formMold.getReportFilePath09());
        updateMstMold.setReportFilePath10(formMold.getReportFilePath10());

        deleteMoldMaintenanceRecomend(oldMstMold, formMold);

        updateMstMold.setLastProductionDate(formMold.getLastProductionDate());
        if (null != formMold.getTotalProducingTimeHour()) {
            updateMstMold.setTotalProducingTimeHour(formMold.getTotalProducingTimeHour());
        } else {
            updateMstMold.setTotalProducingTimeHour(BigDecimal.ZERO);
        }
        if (null != formMold.getTotalShotCount()) {
            updateMstMold.setTotalShotCount(formMold.getTotalShotCount());
        } else {
            updateMstMold.setTotalShotCount(0);
        }
        updateMstMold.setLastMainteDate(formMold.getLastMainteDate());
        if (null != formMold.getAfterMainteTotalProducingTimeHour()) {
            updateMstMold.setAfterMainteTotalProducingTimeHour(formMold.getAfterMainteTotalProducingTimeHour());
        } else {
            updateMstMold.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
        }
        if (null != formMold.getAfterMainteTotalShotCount()) {
            updateMstMold.setAfterMainteTotalShotCount(formMold.getAfterMainteTotalShotCount());
        } else {
            updateMstMold.setAfterMainteTotalShotCount(0);
        }
        if (!StringUtils.isEmpty(formMold.getMainteCycleId01())) {
            updateMstMold.setMainteCycleId01(formMold.getMainteCycleId01());
        } else {
            updateMstMold.setMainteCycleId01(null);
        }
        if (!StringUtils.isEmpty(formMold.getMainteCycleId02())) {
            updateMstMold.setMainteCycleId02(formMold.getMainteCycleId02());
        } else {
            updateMstMold.setMainteCycleId02(null);
        }
        if (!StringUtils.isEmpty(formMold.getMainteCycleId03())) {
            updateMstMold.setMainteCycleId03(formMold.getMainteCycleId03());
        } else {
            updateMstMold.setMainteCycleId03(null);
        }

        updateMstMold.setUpdateDate(new Date());
        updateMstMold.setUpdateUserUuid(loginUser.getUserUuid());

        if (mstMoldDetail.getTblMoldLocationHistorys() != null && mstMoldDetail.getTblMoldLocationHistorys().size() > 0) {
            int delCount = 0; //削除count
            Set<TblMoldLocationHistory> tblMoldLocationHistoryList = new HashSet<>();
            TblMoldLocationHistorys tblMoldLocationHistorys = null;
            for (int i = 0; i < mstMoldDetail.getTblMoldLocationHistorys().size(); i++) {
                tblMoldLocationHistorys = mstMoldDetail.getTblMoldLocationHistorys().get(i);
                TblMoldLocationHistory tblMoldLocationHistory = entityManager.find(TblMoldLocationHistory.class, tblMoldLocationHistorys.getId());
                if (null != tblMoldLocationHistory) {
                    if (!"1".equals(tblMoldLocationHistorys.getDeleteFlag())) {
                        //更新する
//                        tblMoldLocationHistory.setEndDate(null == mstMoldDetail.getTblMoldLocationHistorys().get(i+1) ? CommonConstants.SYS_MAX_DATE : mstMoldDetail.getTblMoldLocationHistorys().get(i+1).getStartDate());//終了日にシステム最大日付
                        tblMoldLocationHistory.setMoldUuid(updateMstMold.getUuid());
                        tblMoldLocationHistory.setChangeReason(tblMoldLocationHistorys.getChangeReason());
                        tblMoldLocationHistory.setChangeReasonText(tblMoldLocationHistorys.getChangeReasonText());
                        if (null != tblMoldLocationHistorys.getCompanyId() && !tblMoldLocationHistorys.getCompanyId().trim().equals("")) {
                            tblMoldLocationHistory.setCompanyId(tblMoldLocationHistorys.getCompanyId());
                            tblMoldLocationHistory.setCompanyName(tblMoldLocationHistorys.getCompanyName());
                        }
                        if (null != tblMoldLocationHistorys.getLocationId() && !tblMoldLocationHistorys.getLocationId().trim().equals("")) {
                            tblMoldLocationHistory.setLocationId(tblMoldLocationHistorys.getLocationId());
                            tblMoldLocationHistory.setLocationName(tblMoldLocationHistorys.getLocationName());
                        } else {
                            tblMoldLocationHistory.setLocationId(null);
                            tblMoldLocationHistory.setLocationName(null);
                        }
                        if (null != tblMoldLocationHistorys.getInstllationSiteId() && !tblMoldLocationHistorys.getInstllationSiteId().trim().equals("")) {
                            tblMoldLocationHistory.setInstllationSiteId(tblMoldLocationHistorys.getInstllationSiteId());
                            tblMoldLocationHistory.setInstllationSiteName(tblMoldLocationHistorys.getInstllationSiteName());
                        } else {
                            tblMoldLocationHistory.setInstllationSiteId(null);
                            tblMoldLocationHistory.setInstllationSiteName(null);
                            updateMstMold.setInstllationSiteId(null);
                        }
                        tblMoldLocationHistory.setUpdateDate(new Date());
                        tblMoldLocationHistory.setUpdateUserUuid(loginUser.getUserUuid());
                        tblMoldLocationHistoryList.add(tblMoldLocationHistory);
                    } else {
                        entityManager.remove(tblMoldLocationHistory);
                        delCount++;
                    }
                }
            }
            updateMstMold.setTblMoldLocationHistoryCollection(tblMoldLocationHistoryList);

            if (delCount == mstMoldDetail.getTblMoldLocationHistorys().size()) {//if delete all
                updateMstMold.setCompanyId(null);
                updateMstMold.setCompanyName(null);
                updateMstMold.setLocationId(null);
                updateMstMold.setLocationName(null);
                updateMstMold.setInstalledDate(null);
                updateMstMold.setInstllationSiteId(null);
                updateMstMold.setInstllationSiteName(null);
            } else {
                Query lastestMoldLocationHistoryQuery = entityManager.createQuery("select h from TblMoldLocationHistory h join h.mstMold m where m.moldId = :moldId order by h.endDate desc,h.startDate desc");
                lastestMoldLocationHistoryQuery.setMaxResults(1);
                lastestMoldLocationHistoryQuery.setParameter("moldId", formMold.getMoldId());
                List<TblMoldLocationHistory> resTblMoldLocationHistory = lastestMoldLocationHistoryQuery.getResultList();
                if (null != resTblMoldLocationHistory && !resTblMoldLocationHistory.isEmpty()) {
                    TblMoldLocationHistory lastestTblMoldLocationHistory = resTblMoldLocationHistory.get(0);
                    lastestTblMoldLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付
                    entityManager.merge(lastestTblMoldLocationHistory);
                    if (null != lastestTblMoldLocationHistory.getCompanyId() && !lastestTblMoldLocationHistory.getCompanyId().trim().equals("")) {
                        updateMstMold.setCompanyId(lastestTblMoldLocationHistory.getCompanyId());
                        updateMstMold.setCompanyName(lastestTblMoldLocationHistory.getCompanyName());
                    }
                    if (null != lastestTblMoldLocationHistory.getLocationId() && !lastestTblMoldLocationHistory.getLocationId().trim().equals("")) {
                        updateMstMold.setLocationId(lastestTblMoldLocationHistory.getLocationId());
                        updateMstMold.setLocationName(lastestTblMoldLocationHistory.getLocationName());
                    } else {
                        updateMstMold.setLocationId(null);
                        updateMstMold.setLocationName(null);
                    }
                    if (null != lastestTblMoldLocationHistory.getInstllationSiteId() && !lastestTblMoldLocationHistory.getInstllationSiteId().trim().equals("")) {
                        updateMstMold.setInstllationSiteId(lastestTblMoldLocationHistory.getInstllationSiteId());
                        updateMstMold.setInstllationSiteName(lastestTblMoldLocationHistory.getInstllationSiteName());
                    } else {
                        updateMstMold.setInstllationSiteId(null);
                        updateMstMold.setInstllationSiteName(null);
                    }

                    updateMstMold.setInstalledDate(lastestTblMoldLocationHistory.getStartDate());
                }
            }
        } else if ((null != updateMstMold.getInstllationSiteId()
                || null != updateMstMold.getLocationId()
                || null != updateMstMold.getCompanyId())) {
            if (null == updateMstMold.getTblMoldLocationHistoryCollection() || updateMstMold.getTblMoldLocationHistoryCollection().isEmpty()) {
                Set<TblMoldLocationHistory> tblMoldLocationHistorys = new HashSet<>();
                TblMoldLocationHistory tblMoldLocationHistory = new TblMoldLocationHistory();
                tblMoldLocationHistory.setId(IDGenerator.generate());
                tblMoldLocationHistory.setMoldUuid(updateMstMold.getUuid());
                tblMoldLocationHistory.setChangeReason(0); //変更理由：なし（ゼロ）
                if (null != updateMstMold.getCompanyId() && !updateMstMold.getCompanyId().trim().equals("")) {
                    tblMoldLocationHistory.setCompanyId(updateMstMold.getCompanyId());
                    tblMoldLocationHistory.setCompanyName(null == updateMstMold.getCompanyName() ? null : updateMstMold.getCompanyName());
                }
                if (null != updateMstMold.getLocationId() && !updateMstMold.getLocationId().trim().equals("")) {
                    tblMoldLocationHistory.setLocationId(updateMstMold.getLocationId());
                    tblMoldLocationHistory.setLocationName(null == updateMstMold.getLocationName() ? updateMstMold.getLocationName() : updateMstMold.getLocationName());
                }
                if (null != updateMstMold.getInstllationSiteId() && !updateMstMold.getInstllationSiteId().trim().equals("")) {
                    tblMoldLocationHistory.setInstllationSiteId(updateMstMold.getInstllationSiteId());
                    tblMoldLocationHistory.setInstllationSiteName(null == updateMstMold.getInstllationSiteName() ? null : updateMstMold.getInstllationSiteName());
                }
                tblMoldLocationHistory.setCreateDate(new Date());
                tblMoldLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
                tblMoldLocationHistory.setUpdateDate(new Date());
                tblMoldLocationHistory.setUpdateUserUuid(loginUser.getUserUuid());
                tblMoldLocationHistory.setStartDate(null == updateMstMold.getInstalledDate() ? new Date() : updateMstMold.getInstalledDate());
                tblMoldLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付
                tblMoldLocationHistorys.add(tblMoldLocationHistory);
                updateMstMold.setTblMoldLocationHistoryCollection(tblMoldLocationHistorys);
            }
        }

        entityManager.merge(updateMstMold);
        formMold = updateMstMold;
        mstMoldComponentRelationService.deleteMstMold(formMold.getUuid());
        if (mstMoldDetail.getMstComponentList() != null && mstMoldDetail.getMstComponentList().size() > 0) {

            for (int i = 0; i < mstMoldDetail.getMstComponentList().size(); i++) {
                for (int j = mstMoldDetail.getMstComponentList().size() - 1; j > i; j--) {
                    MstComponent vo1 = mstMoldDetail.getMstComponentList().get(j);
                    MstComponent vo2 = mstMoldDetail.getMstComponentList().get(i);
                    if (vo1.getId().equals(vo2.getId())) {
                        mstMoldDetail.getMstComponentList().remove(j);
                    }
                }
            }

            for (int i = 0; i < mstMoldDetail.getMstComponentList().size(); i++) {
                MstComponent mstComponent = mstMoldDetail.getMstComponentList().get(i);
                if (null == mstComponent.getId() || mstComponent.getId().trim().equals("")) {
                    continue;
                }
                MstMoldComponentRelation inMstMoldComponentRelation = new MstMoldComponentRelation();
                MstMoldComponentRelationPK mstMoldComponentRelationPK = new MstMoldComponentRelationPK();
                mstMoldComponentRelationPK.setMoldUuid(formMold.getUuid());
                mstMoldComponentRelationPK.setComponentId(mstComponent.getId());
                inMstMoldComponentRelation.setMstMoldComponentRelationPK(mstMoldComponentRelationPK);
                if (StringUtils.isNotEmpty(mstComponent.getProcedureId())) {
                    inMstMoldComponentRelation.setProcedureId(mstComponent.getProcedureId());
                }
                try {
                    if (mstComponent.getCountPerShot() != null) {
                        inMstMoldComponentRelation.setCountPerShot(mstComponent.getCountPerShot());
                    } else {
                        inMstMoldComponentRelation.setCountPerShot(0);
                    }
                } catch (NumberFormatException e) {
                    inMstMoldComponentRelation.setCountPerShot(0);
                }
                mstMoldComponentRelationService.createMstMoldComponentRelation(inMstMoldComponentRelation, loginUser);
            }
        }

        MstMoldSpec mstMoldSpec;
        if (mstMoldDetail.getMstMoldSpec() != null && mstMoldDetail.getMstMoldSpec().size() > 0) {
            for (int i = 0; i < mstMoldDetail.getMstMoldSpec().size(); i++) {
                mstMoldSpec = mstMoldDetail.getMstMoldSpec().get(i);
                MstMoldSpecPK mstMoldSpecPK = new MstMoldSpecPK();
                mstMoldSpecPK.setAttrId(mstMoldSpec.getMstMoldSpecPK().getAttrId());
                if (modeType.equals(mstMoldDetail.getMstMold().getMoldType().toString()) == false) {
                    mstMoldSpecPK.setMoldSpecHstId(mstMoldSpecHistory.getId());
                } else if (needNewSpecHistory == true) {
                    mstMoldSpecPK.setMoldSpecHstId(mstMoldSpecHistory.getId());
                } else {
                    mstMoldSpecPK.setMoldSpecHstId(mstMoldSpec.getMstMoldSpecPK().getMoldSpecHstId());
                }

                mstMoldSpec.setMstMoldSpecPK(mstMoldSpecPK);
                mstMoldSpec.setUpdateDate(new Date());
                mstMoldSpec.setUpdateUserUuid(loginUser.getUserUuid());

                if (mstMoldSpecService.getMstMoldSpecsFK(mstMoldSpecPK.getMoldSpecHstId(), mstMoldSpecPK.getAttrId())) {
                    mstMoldSpecService.updateMstMoldSpecByQuery(mstMoldSpec);
                } else {
                    mstMoldSpec.setId(IDGenerator.generate());
                    mstMoldSpecService.createMstMoldSpecByCsv(mstMoldSpec);
                }
            }
        }
        Map moldProcCondsMap = new HashMap();
        if (mstMoldDetail.getMstMoldProcConds() != null && mstMoldDetail.getMstMoldProcConds().size() > 0) {
            for (int mpFlag = 0; mpFlag < mstMoldDetail.getMstMoldProcConds().size(); mpFlag++) {
                MstMoldProcConds aMstMoldProcConds = mstMoldDetail.getMstMoldProcConds().get(mpFlag);
                String moldProcCondId;
                if ("1".equals(aMstMoldProcConds.getDeleteFlag())) {
                    String id = aMstMoldProcConds.getId();
                    if (null != id) {
                        mstMoldProcCondService.deleteProcCondNameById(id);
                    }
                    continue;
                } else if ("4".equals(aMstMoldProcConds.getDeleteFlag())) {
                    //new 
                    MstMoldProcCond moldProcCond = new MstMoldProcCond();
                    moldProcCond.setId(IDGenerator.generate());
                    moldProcCond.setMoldProcCondName(aMstMoldProcConds.getMoldProcCondName());
                    moldProcCond.setMoldUuid(formMold.getUuid());
                    moldProcCond.setCreateDate(new Date());
                    moldProcCond.setCreateUserUuid(loginUser.getUserUuid());
                    moldProcCond.setUpdateDate(new Date());
                    moldProcCond.setUpdateUserUuid(loginUser.getUserUuid());
                    moldProcCond.setSeq(mpFlag);//20170608 Apeng add
                    entityManager.persist(moldProcCond);
                    moldProcCondId = moldProcCond.getId();
                } else {
                    moldProcCondId = aMstMoldProcConds.getId();
                }
                moldProcCondsMap.put(aMstMoldProcConds.getMoldProcCondName(), moldProcCondId);
            }
        }

        if (!moldProcCondsMap.isEmpty() && mstMoldDetail.getMstMoldProcCondSpecVos() != null && mstMoldDetail.getMstMoldProcCondSpecVos().size() > 0) {
            for (int i = 0; i < mstMoldDetail.getMstMoldProcCondSpecVos().size(); i++) {
                MstMoldProcCondSpecPK pk = new MstMoldProcCondSpecPK();
                MstMoldProcCondSpecs moldProcCondSpecVo = mstMoldDetail.getMstMoldProcCondSpecVos().get(i);
                MstMoldProcCondSpec moldProcCondSpec;
                String moldProcCondId = moldProcCondsMap.get(moldProcCondSpecVo.getMoldProcCondName()).toString();
                pk.setMoldProcCondId(moldProcCondId);
                pk.setAttrId(moldProcCondSpecVo.getMstMoldProcCondSpecPK().getAttrId());

                Query moldProcCondSpecQuery = entityManager.createNamedQuery("MstMoldProcCondSpec.findByMoldProcCondIdAndAttrId");
                moldProcCondSpecQuery.setParameter("moldProcCondId", moldProcCondId);
                moldProcCondSpecQuery.setParameter("attrId", moldProcCondSpecVo.getMstMoldProcCondSpecPK().getAttrId());
                List<MstMoldProcCondSpec> mstMoldProcCondSpecs = moldProcCondSpecQuery.getResultList();

                if (null != mstMoldProcCondSpecs && !mstMoldProcCondSpecs.isEmpty()) {
                    moldProcCondSpec = mstMoldProcCondSpecs.get(0);
                    moldProcCondSpec.setUpdateDate(new Date());
                    moldProcCondSpec.setUpdateUserUuid(loginUser.getUserUuid());
                    moldProcCondSpec.setMstMoldProcCondSpecPK(pk);
                    if (CheckproCondIdAndAttrId(moldProcCondId, pk.getAttrId())) {
                        moldProcCondSpec.setAttrValue(mstMoldDetail.getMstMoldProcCondSpecVos().get(i).getAttrValue());
                    }
                    mstMoldProcCondSpecService.updateMstMoldSpec(moldProcCondSpec);
                } else {
                    moldProcCondSpec = new MstMoldProcCondSpec();
                    moldProcCondSpec.setId(IDGenerator.generate());
                    moldProcCondSpec.setCreateDate(new Date());
                    moldProcCondSpec.setCreateUserUuid(loginUser.getUserUuid());
                    moldProcCondSpec.setUpdateDate(new Date());
                    moldProcCondSpec.setUpdateUserUuid(loginUser.getUserUuid());
                    moldProcCondSpec.setMstMoldProcCondSpecPK(pk);
                    moldProcCondSpec.setAttrValue(mstMoldDetail.getMstMoldProcCondSpecVos().get(i).getAttrValue());
                    mstMoldProcCondSpecService.createMstMoldSpec(moldProcCondSpec);
                }
            }
        }

        if (null != mstMoldDetail.getMstMoldPartRels() && !mstMoldDetail.getMstMoldPartRels().isEmpty()) {
            for (int i = 0; i < mstMoldDetail.getMstMoldPartRels().size(); i++) {
                MstMoldPartRelDetail moldPartRel = (MstMoldPartRelDetail) mstMoldDetail.getMstMoldPartRels().get(i);
                if (1 == moldPartRel.getDeleteFlag() && null != moldPartRel.getId() && !"".equals(moldPartRel.getId())) {
                    // Delete mold part rel
                    Query deleteMoldPartRelCmd = entityManager.createNamedQuery("MstMoldPartRel.deleleById");
                    deleteMoldPartRelCmd.setParameter("id", moldPartRel.getId());
                    deleteMoldPartRelCmd.executeUpdate();
                } else if (3 == moldPartRel.getDeleteFlag() || 4 == moldPartRel.getDeleteFlag()) {
                     // Edit mold part rel
                    MstMoldPartRel updatedMoldPartRel = new MstMoldPartRel();
                    String moldPartRelId = "";
                    Boolean isUpdate = false;
                    // check existed mold part rel
                    MstMoldPartRel existedMoldPartRel = mstMoldPartRelService
                                .getMstMoldPartRelByKeys(moldPartRel.getMoldUuid(), moldPartRel.getLocation(), moldPartRel.getMoldPartId());
                    if (3 == moldPartRel.getDeleteFlag() 
                            && null != moldPartRel.getId() && !"".equals(moldPartRel.getId())) { // update
                        if (null != existedMoldPartRel && !moldPartRel.getId().equals(existedMoldPartRel.getId())) {
                            response.setError(true);
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                            return response;
                        }
                        isUpdate = true;
                        moldPartRelId = moldPartRel.getId();
                        updatedMoldPartRel.setCreateDate(moldPartRel.getCreateDate());
                        updatedMoldPartRel.setCreateUserUuid(moldPartRel.getCreateUserUuid());
                    } else if (4 == moldPartRel.getDeleteFlag()) { // add
                        if (null != existedMoldPartRel) {
                            isUpdate = true;
                            moldPartRelId = existedMoldPartRel.getId();
                            updatedMoldPartRel.setCreateDate(existedMoldPartRel.getCreateDate());
                            updatedMoldPartRel.setCreateUserUuid(existedMoldPartRel.getCreateUserUuid());
                        } else {
                            moldPartRelId = IDGenerator.generate();

                            updatedMoldPartRel.setCreateDate(new Date());
                            updatedMoldPartRel.setCreateUserUuid(loginUser.getUserUuid());
                        }
                    }
                    if (!"".equals(moldPartRelId)) {
                        updatedMoldPartRel.setId(moldPartRelId);
                        updatedMoldPartRel.setMoldUuid(moldPartRel.getMoldUuid());
                        updatedMoldPartRel.setLocation(moldPartRel.getLocation());
                        updatedMoldPartRel.setMoldPartId(moldPartRel.getMoldPartId());
                        updatedMoldPartRel.setQuantity(moldPartRel.getQuantity());
                        updatedMoldPartRel.setAlias(moldPartRel.getAlias());
                        updatedMoldPartRel.setRplClShotCnt(moldPartRel.getRplClShotCnt());
                        updatedMoldPartRel.setRplClProdTimeHour(moldPartRel.getRplClProdTimeHour());
                        updatedMoldPartRel.setRplClLappsedDay(moldPartRel.getRplClLappsedDay());
                        updatedMoldPartRel.setRprClShotCnt(moldPartRel.getRprClShotCnt());
                        updatedMoldPartRel.setRprClProdTimeHour(moldPartRel.getRprClProdTimeHour());
                        updatedMoldPartRel.setRprClLappsedDay(moldPartRel.getRprClLappsedDay());
                        updatedMoldPartRel.setAftRplShotCnt(moldPartRel.getAftRplShotCnt());
                        updatedMoldPartRel.setAftRplProdTimeHour(moldPartRel.getAftRplProdTimeHour());
                        updatedMoldPartRel.setLastRplDatetime(moldPartRel.getLastRplDatetime());
                        updatedMoldPartRel.setAftRprShotCnt(moldPartRel.getAftRprShotCnt());
                        updatedMoldPartRel.setAftRprProdTimeHour(moldPartRel.getAftRprProdTimeHour());
                        updatedMoldPartRel.setLastRprDatetime(moldPartRel.getLastRprDatetime());

                        updatedMoldPartRel.setUpdateDate(new Date());
                        updatedMoldPartRel.setUpdateUserUuid(loginUser.getUserUuid());  

                        if (isUpdate) {
                            entityManager.merge(updatedMoldPartRel);
                        } else {
                            entityManager.persist(updatedMoldPartRel);
                        }   
                    }
                }
            }           
        }

        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

	@Transactional
    public void updateMstMoldImageByMoldId( MstMold moldImageData, LoginUser loginUser){
        
        StringBuilder updateQuery = new StringBuilder();
             
        updateQuery.append(" UPDATE MstMold m SET ");
        updateQuery.append(" m.imgFilePath01 = :imgFilePath01, ");
        updateQuery.append(" m.imgFilePath02 = :imgFilePath02, ");
        updateQuery.append(" m.imgFilePath03 = :imgFilePath03, ");
        updateQuery.append(" m.imgFilePath04 = :imgFilePath04, ");
        updateQuery.append(" m.imgFilePath05 = :imgFilePath05, ");
        updateQuery.append(" m.imgFilePath06 = :imgFilePath06, ");
        updateQuery.append(" m.imgFilePath07 = :imgFilePath07, ");
        updateQuery.append(" m.imgFilePath08 = :imgFilePath08, ");
        updateQuery.append(" m.imgFilePath09 = :imgFilePath09, ");
        updateQuery.append(" m.imgFilePath10 = :imgFilePath10, ");
        updateQuery.append(" m.updateDate = :updateDate, ");
        updateQuery.append(" m.updateUserUuid = :updateUserUuid ");
        updateQuery.append(" WHERE m.moldId = :moldId");
        
        Query finalUpdateQuery = entityManager.createQuery(updateQuery.toString());
        
        finalUpdateQuery.setParameter("imgFilePath01", (StringUtils.isBlank(moldImageData.getImgFilePath01()))? null:moldImageData.getImgFilePath01());
        finalUpdateQuery.setParameter("imgFilePath02", (StringUtils.isBlank(moldImageData.getImgFilePath02()))? null:moldImageData.getImgFilePath02());
        finalUpdateQuery.setParameter("imgFilePath03", (StringUtils.isBlank(moldImageData.getImgFilePath03()))? null:moldImageData.getImgFilePath03());
        finalUpdateQuery.setParameter("imgFilePath04", (StringUtils.isBlank(moldImageData.getImgFilePath04()))? null:moldImageData.getImgFilePath04());
        finalUpdateQuery.setParameter("imgFilePath05", (StringUtils.isBlank(moldImageData.getImgFilePath05()))? null:moldImageData.getImgFilePath05());
        finalUpdateQuery.setParameter("imgFilePath06", (StringUtils.isBlank(moldImageData.getImgFilePath06()))? null:moldImageData.getImgFilePath06());
        finalUpdateQuery.setParameter("imgFilePath07", (StringUtils.isBlank(moldImageData.getImgFilePath07()))? null:moldImageData.getImgFilePath07());
        finalUpdateQuery.setParameter("imgFilePath08", (StringUtils.isBlank(moldImageData.getImgFilePath08()))? null:moldImageData.getImgFilePath08());
        finalUpdateQuery.setParameter("imgFilePath09", (StringUtils.isBlank(moldImageData.getImgFilePath09()))? null:moldImageData.getImgFilePath09());
        finalUpdateQuery.setParameter("imgFilePath10", (StringUtils.isBlank(moldImageData.getImgFilePath10()))? null:moldImageData.getImgFilePath10());
        finalUpdateQuery.setParameter("updateDate", new Date());
        finalUpdateQuery.setParameter("updateUserUuid", loginUser.getUserUuid());
        finalUpdateQuery.setParameter("moldId", moldImageData.getMoldId());
           
        finalUpdateQuery.executeUpdate(); 
    }
	
	
    /**
     * CSV
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
     * @param totalShotCountFrom
     * @param totalShotCountTo
     * @param lastMainteDateFrom
     * @param lastMainteDateTo
     * @param afterMainteTotalProducingTimeHourFrom
     * @param afterMainteTotalProducingTimeHourTo
     * @param afterMainteTotalShotCountFrom
     * @param afterMainteTotalShotCountTo
     * @param status
     * @param moldCreatedDateFrom
     * @param loginUser
     * @param moldCreatedDateTo
     * @return
     */
    public FileReponse getMstMoldOutputCsv(
            String moldId,
            String moldName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer moldType,
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
            Integer status,
            Date moldCreatedDateFrom,
            Date moldCreatedDateTo,
            LoginUser loginUser) {

        StringBuffer sql = new StringBuffer(" SELECT m FROM MstMold m "
                + "LEFT JOIN FETCH m.mstCompanyByOwnerCompanyId "
                + "LEFT JOIN FETCH m.mstCompanyByCompanyId "
                + "LEFT JOIN FETCH m.mstInstallationSite "
                + "LEFT JOIN FETCH m.mstLocation "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn01 "
                + " WHERE 1=1 ");
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" AND m.moldId like :moldId ");
        }
        if (moldName != null && !"".equals(moldName)) {
            sql = sql.append(" AND m.moldName like :moldName ");
        }
        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            sql = sql.append(" AND m.mainAssetNo LIKE :mainAssetNo ");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" and m.mstCompanyByOwnerCompanyId.companyName like :ownerCompanyName ");
        }

        if (companyName != null && !"".equals(companyName)) {
            sql = sql.append(" and m.companyName like :companyName ");
        }
        if (locationName != null && !"".equals(locationName)) {
            sql = sql.append(" and m.locationName like :locationName ");
        }
        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            sql = sql.append(" and m.instllationSiteName like :instllationSiteName ");
        }
        if (moldType != null && 0 != moldType) {
            sql = sql.append(" and m.moldType = :moldType ");
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
        if (null != moldCreatedDateFrom) {
            sql = sql.append(" and m.moldCreatedDate >= :moldCreatedDateFrom ");
        }
        if (null != moldCreatedDateTo) {
            sql = sql.append(" and m.moldCreatedDate <= :moldCreatedDateTo ");
        }
        if (status != null) {
            sql = sql.append(" and m.status = :status ");
        }

        sql = sql.append(" Order BY m.moldId ");
        Query getCsvDetail = entityManager.createQuery(sql.toString());

        if (moldId != null && !"".equals(moldId)) {
            getCsvDetail.setParameter("moldId", "%" + moldId + "%");
        }

        if (moldName != null && !"".equals(moldName)) {
            getCsvDetail.setParameter("moldName", "%" + moldName + "%");
        }

        //資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            getCsvDetail.setParameter("mainAssetNo", "%" + mainAssetNo + "%");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            getCsvDetail.setParameter("ownerCompanyName", "%" + ownerCompanyName + "%");
        }

        if (companyName != null && !"".equals(companyName)) {
            getCsvDetail.setParameter("companyName", "%" + companyName + "%");
        }

        if (locationName != null && !"".equals(locationName)) {
            getCsvDetail.setParameter("locationName", "%" + locationName + "%");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            getCsvDetail.setParameter("instllationSiteName", "%" + instllationSiteName + "%");
        }

        if (moldType != null && 0 != moldType) {
            getCsvDetail.setParameter("moldType", moldType);
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

        if (null != moldCreatedDateFrom) {
            getCsvDetail.setParameter("moldCreatedDateFrom", moldCreatedDateFrom);
        }
        if (null != moldCreatedDateTo) {
            getCsvDetail.setParameter("moldCreatedDateTo", moldCreatedDateTo);
        }

        if (status != null) {
            getCsvDetail.setParameter("status", status);
        }
        List list = getCsvDetail.getResultList();
        int specHeaderCount = 0;
        int contHeaderCount = 0;
        ArrayList<ArrayList> moldOutList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            MstChoiceList moldTypeChoice = mstChoiceService.getChoice(loginUser.getLangId(), "mst_mold.mold_type");
            MstChoiceList moldStatuChoice = mstChoiceService.getChoice(loginUser.getLangId(), "mst_mold.status");
            MstChoiceList mstDepartmentChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");
            //一時格納用
            ArrayList<String> tempOutList;
            for (int i = 0; i < list.size(); i++) {
                MstMold csvMstMold = (MstMold) list.get(i);

                tempOutList = new ArrayList<>();
                tempOutList.add(csvMstMold.getMoldId());
                tempOutList.add(csvMstMold.getMoldName());

                if (moldTypeChoice != null && moldTypeChoice.getMstChoice() != null && moldTypeChoice.getMstChoice().size() > 0) {
                    boolean found = false;
                    for (int momi = 0; momi < moldTypeChoice.getMstChoice().size(); momi++) {
                        MstChoice aMstChoice = moldTypeChoice.getMstChoice().get(momi);
                        if (aMstChoice.getMstChoicePK().getSeq() != null && csvMstMold.getMoldType() != null) {
                            if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(csvMstMold.getMoldType()))) {
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

                tempOutList.add(csvMstMold.getMainAssetNo());
                if (csvMstMold.getMoldCreatedDate() != null) {
                    tempOutList.add(FileUtil.dateFormat(csvMstMold.getMoldCreatedDate()));
                } else {
                    tempOutList.add("");
                }
                if (csvMstMold.getInspectedDate() != null) {
                    tempOutList.add(FileUtil.dateFormat(csvMstMold.getInspectedDate()));
                } else {
                    tempOutList.add("");
                }

                if (csvMstMold.getDepartment() != null) {
                    boolean found = false;
                    for (MstChoice mstChoice : mstDepartmentChoiceList.getMstChoice()) {
                        if (mstChoice.getMstChoicePK().getSeq().equals(String.valueOf(csvMstMold.getDepartment()))) {
                            tempOutList.add(mstChoice.getChoice());
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        tempOutList.add("");
                    }
                } else {
                    tempOutList.add("");
                }

                if (csvMstMold.getMstCompanyByOwnerCompanyId() != null) {
                    tempOutList.add(csvMstMold.getMstCompanyByOwnerCompanyId().getCompanyCode());
                    tempOutList.add(csvMstMold.getMstCompanyByOwnerCompanyId().getCompanyName());
                } else {
                    tempOutList.add("");
                    tempOutList.add("");
                }

                if (csvMstMold.getInstalledDate() != null) {
                    tempOutList.add(FileUtil.dateFormat(csvMstMold.getInstalledDate()));
                } else {
                    tempOutList.add("");
                }

                if (csvMstMold.getMstCompanyByCompanyId() != null) {
                    tempOutList.add(csvMstMold.getMstCompanyByCompanyId().getCompanyCode());
                    tempOutList.add(csvMstMold.getMstCompanyByCompanyId().getCompanyName());
                } else {
                    tempOutList.add("");
                    tempOutList.add("");
                }

                if (csvMstMold.getMstLocation() != null) {
                    tempOutList.add(csvMstMold.getMstLocation().getLocationCode());
                    tempOutList.add(csvMstMold.getMstLocation().getLocationName());
                } else {
                    tempOutList.add("");
                    tempOutList.add("");
                }

                if (csvMstMold.getMstInstallationSite() != null) {
                    tempOutList.add(csvMstMold.getMstInstallationSite().getInstallationSiteCode());
                    tempOutList.add(csvMstMold.getMstInstallationSite().getInstallationSiteName());
                } else {
                    tempOutList.add("");
                    tempOutList.add("");
                }

                if (moldStatuChoice != null && moldStatuChoice.getMstChoice() != null && moldStatuChoice.getMstChoice().size() > 0) {
                    for (int momi = 0; momi < moldStatuChoice.getMstChoice().size(); momi++) {
                        MstChoice aMstChoice = moldStatuChoice.getMstChoice().get(momi);
                        if (aMstChoice.getMstChoicePK().getSeq() != null && csvMstMold.getStatus() != null) {
                            if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(csvMstMold.getStatus()))) {
                                tempOutList.add(aMstChoice.getChoice());
                                break;
                            }
                        }
                    }
                } else {
                    tempOutList.add("");
                }

                if (csvMstMold.getStatusChangedDate() != null) {
                    tempOutList.add(FileUtil.dateFormat(csvMstMold.getStatusChangedDate()));
                } else {
                    tempOutList.add("");
                }

                if (csvMstMold.getLastProductionDate() != null) {
                    tempOutList.add(FileUtil.dateFormat(csvMstMold.getLastProductionDate()));
                } else {
                    tempOutList.add("");
                }
                if (csvMstMold.getTotalProducingTimeHour() != null) {
                    tempOutList.add("" + csvMstMold.getTotalProducingTimeHour());
                } else {
                    tempOutList.add("0");
                }
                if (csvMstMold.getTotalShotCount() != null) {
                    tempOutList.add("" + csvMstMold.getTotalShotCount());
                } else {
                    tempOutList.add("0");
                }
                if (csvMstMold.getLastMainteDate() != null) {
                    tempOutList.add(FileUtil.dateFormat(csvMstMold.getLastMainteDate()));
                } else {
                    tempOutList.add("");
                }
                if (csvMstMold.getAfterMainteTotalProducingTimeHour() != null) {
                    tempOutList.add("" + csvMstMold.getAfterMainteTotalProducingTimeHour());
                } else {
                    tempOutList.add("0");
                }
                if (csvMstMold.getAfterMainteTotalShotCount() != null) {
                    tempOutList.add("" + csvMstMold.getAfterMainteTotalShotCount());
                } else {
                    tempOutList.add("0");
                }
                if (!StringUtils.isEmpty(csvMstMold.getMainteCycleId01())) {
                    tempOutList.add(csvMstMold.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    tempOutList.add("");
                }
//                if (!StringUtils.isEmpty(csvMstMold.getMainteCycleId02())) {
//                    tempOutList.add(csvMstMold.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
//                } else {
//                    tempOutList.add("");
//                }
//                if (!StringUtils.isEmpty(csvMstMold.getMainteCycleId03())) {
//                    tempOutList.add(csvMstMold.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
//                } else {
//                    tempOutList.add("");
//                }

                // 
                int itemSpec = 0;
                if (csvMstMold.getMstMoldSpecHistoryCollection() != null) {
                    Iterator<MstMoldSpecHistory> csvMstMoldSpecHistory = csvMstMold.getMstMoldSpecHistoryCollection().iterator();
                    // *************************************
                    MstMoldSpecHistory mstMoldSpecHistory = new MstMoldSpecHistory();
                    while (csvMstMoldSpecHistory.hasNext()) {
                        mstMoldSpecHistory = csvMstMoldSpecHistory.next();

                    }

                    Query getCsvApec = entityManager.createNamedQuery("MstMoldSpec.findByMoldListSpec");
                    getCsvApec.setParameter("moldSpecHstId", mstMoldSpecHistory.getId());
                    if (FileUtil.checkExternal(entityManager, mstDictionaryService, csvMstMold.getMoldId(), loginUser).isError() == true) {
                        getCsvApec.setParameter("externalFlg", CommonConstants.EXTERNALFLG);
                    } else {
                        getCsvApec.setParameter("externalFlg", CommonConstants.MINEFLAG);
                    }
                    getCsvApec.setParameter("moldType", csvMstMold.getMoldType());
                    List<MstMoldSpec> specList = getCsvApec.getResultList();

                    if (specList != null && specList.size() > 0) {
                        for (int j = 0; j < specList.size(); j++) {
                            MstMoldSpec csvMstMoldSpec = specList.get(j);
                            tempOutList.add(csvMstMoldSpec.getAttrValue());
                            itemSpec = itemSpec + 1;
                        }
                    }
                }
                if (specHeaderCount < itemSpec) {
                    specHeaderCount = itemSpec;
                }
                moldOutList.add(tempOutList);
            }
        }
        /**
         * Header
         */
        String langId = loginUser.getLangId();

        List<String> dictKeyList = Arrays.asList(
                "mold_id",
                "mold_name",
                "mold_type",
                "main_asset_no",
                "mold_created_date",
                "inspected_date",
                "mold_created_date",
                "user_department",
                "inspected_date",
                "owner_company_code",
                "owner_company_name",
                "installed_date",
                "company_code",
                "company_name",
                "location_code",
                "location_name",
                "installation_site_code",
                "installation_site_name",
                "mold_status",
                "status_changed_date",
                "mold_last_production_date",
                "mold_total_production_time_hour",
                "total_shot_count",
                "mold_last_mainte_date",
                "mold_after_mainte_total_production_time_hour",
                "mold_after_mainte_total_shot_count",
                "mold_mainte_cycle_code_01",
                "mold_spec",
                "component_code",
                "count_per_shot",
                "delete_record"
        );
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        String outMoldId = headerMap.get("mold_id");
        String outMoldName = headerMap.get("mold_name");
        String outMoldType = headerMap.get("mold_type");
        String outMainAssetNo = headerMap.get("main_asset_no");
        String outMoldCreatedDate = headerMap.get("mold_created_date");
        String outInspectedDate = headerMap.get("inspected_date");
        String userDepartment = headerMap.get("user_department");
        String outOwnerCompanyCode = headerMap.get("owner_company_code");//所有会社名称
        String outOwnerCompanyName = headerMap.get("owner_company_name");//所有会社名称
        String outInstalledDate = headerMap.get("installed_date");
        String outCompanyCode = headerMap.get("company_code");
        String outCompanyName = headerMap.get("company_name");
        String outLocationCode = headerMap.get("location_code");
        String outLocationName = headerMap.get("location_name");
        String outInstllationSiteCode = headerMap.get("installation_site_code");
        String outInstllationSiteName = headerMap.get("installation_site_name");
        String outStatus = headerMap.get("mold_status");
        String outStatusChangedDate = headerMap.get("status_changed_date");

        String outLastProductionDate = headerMap.get("mold_last_production_date");
        String outTotalProductionTimeHour = headerMap.get("mold_total_production_time_hour");
        String outTotalShotCount = headerMap.get("total_shot_count");
        String outLastMainteDate = headerMap.get("mold_last_mainte_date");
        String outAfterMainteTotalProducingTimeHour = headerMap.get("mold_after_mainte_total_production_time_hour");
        String outAfterMainteTotalShotCount = headerMap.get("mold_after_mainte_total_shot_count");
        String outMainteCycleCode01 = headerMap.get("mold_mainte_cycle_code_01");

        String outMoldSpec = headerMap.get("mold_spec");//金型仕様
        String outComponentCode = headerMap.get("component_code");//部品コード
        String outComponentCountPerShot = headerMap.get("count_per_shot");//取り数
        String delete = headerMap.get("delete_record");
        FileReponse fr = new FileReponse();

        /*Head*/
        ArrayList csvOutHeadList = new ArrayList();
        csvOutHeadList.add(outMoldId);
        csvOutHeadList.add(outMoldName);
        csvOutHeadList.add(outMoldType);
        csvOutHeadList.add(outMainAssetNo);
        csvOutHeadList.add(outMoldCreatedDate);
        csvOutHeadList.add(outInspectedDate);
        csvOutHeadList.add(userDepartment);
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
        csvOutHeadList.add(outLastProductionDate);
        csvOutHeadList.add(outTotalProductionTimeHour);
        csvOutHeadList.add(outTotalShotCount);
        csvOutHeadList.add(outLastMainteDate);
        csvOutHeadList.add(outAfterMainteTotalProducingTimeHour);
        csvOutHeadList.add(outAfterMainteTotalShotCount);
        csvOutHeadList.add(outMainteCycleCode01);

        // 2019-07-31: Remove outMainteCycleCode02 and outMainteCycleCode03
        // Origin: index = 27 => Updated: index = 25
        int index = 25;//固定列数
        int listCount = 0;
        for (int i = 0; i < moldOutList.size(); i++) {
            if (moldOutList.get(i).size() < index + specHeaderCount) {
                for (int j = moldOutList.get(i).size(); j < index + specHeaderCount; j++) {
                    moldOutList.get(i).add("");
                }
            }
            int count = getComponentList((String) moldOutList.get(i).get(0), moldOutList.get(i));
            if (count > contHeaderCount) {
                contHeaderCount = count;
            }

            if (moldOutList.get(i).size() > listCount) {
                listCount = moldOutList.get(i).size();
            }
        }

        for (int i = 0; i < moldOutList.size(); i++) {
            if (moldOutList.get(i).size() < listCount) {
                for (int j = moldOutList.get(i).size(); j < listCount; j++) {
                    moldOutList.get(i).add("");
                }
            }
            moldOutList.get(i).add("");
        }

        //Header
        for (int i = 0; i < specHeaderCount; i++) {
            csvOutHeadList.add(outMoldSpec + (i + 1));
        }
        //Header
        for (int i = 0; i < contHeaderCount; i++) {
            csvOutHeadList.add(outComponentCode + (i + 1));
            csvOutHeadList.add(outComponentCountPerShot + (i + 1));
        }
        // Header
        csvOutHeadList.add(delete);

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(csvOutHeadList);
        for (int i = 0; i < moldOutList.size(); i++) {
            gLineList.add(moldOutList.get(i));
        }
        //CSVフ
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.TBL_MST_MOLD);
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        fr.setFileUuid(uuid);
        return fr;

    }

    /**
     * 
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
     * @param totalShotCountFrom
     * @param totalShotCountTo
     * @param lastMainteDateFrom
     * @param lastMainteDateTo
     * @param afterMainteTotalProducingTimeHourFrom
     * @param afterMainteTotalProducingTimeHourTo
     * @param afterMainteTotalShotCountFrom
     * @param afterMainteTotalShotCountTo
     * @param status
     * @param moldCreatedDateFrom
     * @param loginUser
     * @param moldCreatedDateTo
     * @return
     */
    public FileReponse getMstMoldPartOutputCsv(
            String moldId,
            String moldName,
            String mainAssetNo,
            String ownerCompanyName,
            String companyName,
            String locationName,
            String instllationSiteName,
            Integer moldType,
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
            Integer status,
            Date moldCreatedDateFrom,
            Date moldCreatedDateTo,
            LoginUser loginUser) {
        StringBuffer sql = new StringBuffer(" SELECT m FROM MstMold m "
                + "LEFT JOIN FETCH m.mstCompanyByOwnerCompanyId "
                + "LEFT JOIN FETCH m.mstCompanyByCompanyId "
                + "LEFT JOIN FETCH m.mstInstallationSite "
                + "LEFT JOIN FETCH m.mstLocation "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn01 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn02 "
                + "LEFT JOIN FETCH m.blMaintenanceCyclePtn03 "
                + " WHERE 1=1 ");
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" AND m.moldId like :moldId ");
        }
        if (moldName != null && !"".equals(moldName)) {
            sql = sql.append(" AND m.moldName like :moldName ");
        }
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            sql = sql.append(" AND m.mainAssetNo LIKE :mainAssetNo ");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" and m.mstCompanyByOwnerCompanyId.companyName like :ownerCompanyName ");
        }

        if (companyName != null && !"".equals(companyName)) {
            sql = sql.append(" and m.companyName like :companyName ");
        }
        if (locationName != null && !"".equals(locationName)) {
            sql = sql.append(" and m.locationName like :locationName ");
        }
        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            sql = sql.append(" and m.instllationSiteName like :instllationSiteName ");
        }
        if (moldType != null && 0 != moldType) {
            sql = sql.append(" and m.moldType = :moldType ");
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
        if (null != moldCreatedDateFrom) {
            sql = sql.append(" and m.moldCreatedDate >= :moldCreatedDateFrom ");
        }
        if (null != moldCreatedDateTo) {
            sql = sql.append(" and m.moldCreatedDate <= :moldCreatedDateTo ");
        }
        if (status != null) {
            sql = sql.append(" and m.status = :status ");
        }

        sql = sql.append(" Order BY m.moldId ");
        Query getCsvDetail = entityManager.createQuery(sql.toString());

        if (moldId != null && !"".equals(moldId)) {
            getCsvDetail.setParameter("moldId", "%" + moldId + "%");
        }

        if (moldName != null && !"".equals(moldName)) {
            getCsvDetail.setParameter("moldName", "%" + moldName + "%");
        }

        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            getCsvDetail.setParameter("mainAssetNo", "%" + mainAssetNo + "%");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            getCsvDetail.setParameter("ownerCompanyName", "%" + ownerCompanyName + "%");
        }

        if (companyName != null && !"".equals(companyName)) {
            getCsvDetail.setParameter("companyName", "%" + companyName + "%");
        }

        if (locationName != null && !"".equals(locationName)) {
            getCsvDetail.setParameter("locationName", "%" + locationName + "%");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            getCsvDetail.setParameter("instllationSiteName", "%" + instllationSiteName + "%");
        }

        if (moldType != null && 0 != moldType) {
            getCsvDetail.setParameter("moldType", moldType);
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

        if (null != moldCreatedDateFrom) {
            getCsvDetail.setParameter("moldCreatedDateFrom", moldCreatedDateFrom);
        }
        if (null != moldCreatedDateTo) {
            getCsvDetail.setParameter("moldCreatedDateTo", moldCreatedDateTo);
        }

        if (status != null) {
            getCsvDetail.setParameter("status", status);
        }
        List list = getCsvDetail.getResultList();
        ArrayList<ArrayList> gLineList = getHeaderes(loginUser.getLangId());
        if (list != null && list.size() > 0) {
            MstMoldPartRelList mstMoldPartRelList = new MstMoldPartRelList();
            mstMoldPartRelList = mstMoldPartRelService.getMstMoldPartRels();
            for (int i = 0; i < list.size(); i++) {
                MstMold csvMstMold = (MstMold) list.get(i);
                ArrayList lineList;
                for (MstMoldPartRel mstMoldPartRel : mstMoldPartRelList.getMstMoldPartRels()) {
                    if(csvMstMold.getUuid().equals(mstMoldPartRel.getMoldUuid())){
                        lineList = new ArrayList();
                        lineList.add(mstMoldPartRel.getMstMold().getMoldId());
                        lineList.add(mstMoldPartRel.getLocation());
                        lineList.add(mstMoldPartRel.getMstMoldPart().getMoldPartCode());
                        lineList.add(mstMoldPartRel.getMstMoldPart().getMoldPartName());
                        lineList.add(String.valueOf(mstMoldPartRel.getRplClShotCnt()));
                        lineList.add(String.valueOf(mstMoldPartRel.getRplClProdTimeHour()));
                        lineList.add(String.valueOf(mstMoldPartRel.getRplClLappsedDay()));
                        lineList.add(String.valueOf(mstMoldPartRel.getRprClShotCnt()));
                        lineList.add(String.valueOf(mstMoldPartRel.getRprClProdTimeHour()));
                        lineList.add(String.valueOf(mstMoldPartRel.getRprClLappsedDay()));
                        lineList.add(String.valueOf(mstMoldPartRel.getAftRplShotCnt()));
                        lineList.add(String.valueOf(mstMoldPartRel.getAftRplProdTimeHour()));
                        lineList.add(DateFormat.dateToStr(mstMoldPartRel.getLastRplDatetime(), DateFormat.DATETIME_FORMAT));
                        lineList.add(String.valueOf(mstMoldPartRel.getAftRprShotCnt()));
                        lineList.add(String.valueOf(mstMoldPartRel.getAftRprProdTimeHour()));
                        lineList.add(DateFormat.dateToStr(mstMoldPartRel.getLastRprDatetime(), DateFormat.DATETIME_FORMAT));
                        lineList.add(mstMoldPartRel.getQuantity() == null? "": mstMoldPartRel.getQuantity().toString());
                        //lineList.add(mstMoldPartRel.getAlias());
                        
                        lineList.add("");
                        gLineList.add(lineList);
                    }
                }
            }
        }
        //CSV
        String guuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, guuid);
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(guuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD_PART_REL);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MOLD_PART_REL);

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        FileReponse fileReponse = new FileReponse();
        fileReponse.setFileUuid(guuid);
        return fileReponse;
    }
    
    private static int MOLD_PART_REL_COLUMN_SIZE = 18;
    
    /**
     * CSV
     * @param fileUuid
     * @param loginUser
     * @return 
     */
    @Transactional
    public ImportResultResponse importMoldPartRelCsv(String fileUuid, LoginUser loginUser) {
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        // SCV
        ArrayList readList = CSVFileUtil.readCsv(csvFile);
        int csvInfoSize = readList.size();
        if (csvInfoSize <= 1) {
            return importResultResponse;
        } else {
            ArrayList headerList = (ArrayList) readList.get(0);
            if (headerList.size() != MOLD_PART_REL_COLUMN_SIZE) {
                importResultResponse.setError(true);
                importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                importResultResponse.setErrorMessage(
                        mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid"));
                return importResultResponse;
            }
            
            String logFileUuid = IDGenerator.generate();
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

            csvProcesStart(loginUser, readList, logFile);
            succeededCount = addedCount + updatedCount + deletedCount;
            csvProcesExit(loginUser, fileUuid, csvInfoSize, logFileUuid);
            importResultResponse.setTotalCount(csvInfoSize - 1);
            importResultResponse.setSucceededCount(succeededCount);
            importResultResponse.setAddedCount(addedCount);
            importResultResponse.setUpdatedCount(updatedCount);
            importResultResponse.setDeletedCount(deletedCount);
            importResultResponse.setFailedCount(failedCount);
            importResultResponse.setLog(logFileUuid);
            return importResultResponse;
        }
    }
    
    /**
     * CSV
     * @param loginUser
     * @param csvInfoList
     * @param logFile
     */
    private void csvProcesStart(LoginUser loginUser, ArrayList csvInfoList, String logFile) {
        FileUtil fileUtil = new FileUtil();
        Map<String, String> csvHeader = getDictValues(loginUser.getLangId());
        Map<String, String> csvCheckMsg = getCsvInfoCheckMsg(loginUser.getLangId());
        int deleteFlgCol = MOLD_PART_REL_COLUMN_SIZE - 1;
        
        for (int i = 1; i < csvInfoList.size(); i++) {
            
            ArrayList comList = (ArrayList) csvInfoList.get(i);
            MstMoldPartRel mstMoldPartRel = checkCsvInfo(comList, logFile, i, fileUtil, csvHeader, csvCheckMsg);
            if (!mstMoldPartRel.isError()) {
                MstMoldPartRel existedMoldPartRel = mstMoldPartRelService.getMstMoldPartRelByKeys(mstMoldPartRel.getMoldUuid(), mstMoldPartRel.getLocation(), mstMoldPartRel.getMoldPartId());
                if (null != existedMoldPartRel) {
                    // If delete flag is enable
                    if (null != comList.get(deleteFlgCol) && comList.get(deleteFlgCol).equals("1")) {
                        int count = mstMoldPartRelService.deleteMstMoldPartRel(
                                mstMoldPartRel.getMoldUuid(), mstMoldPartRel.getLocation(), mstMoldPartRel.getMoldPartId());
                        deletedCount = deletedCount + count;
                        fileUtil.writeInfoToFile(
                                logFile, 
                                fileUtil.outValue(
                                    csvCheckMsg.get("rowNumber"), 
                                    i, 
                                    csvHeader.get("moldId"), 
                                    existedMoldPartRel.getMstMold().getMoldId(), 
                                    csvCheckMsg.get("error"), 
                                    0, 
                                    csvCheckMsg.get("dbProcess"),
                                    csvCheckMsg.get("msgRecordDeleted")
                                ));
                    } else {
                        mstMoldPartRel.setId(existedMoldPartRel.getId());
                        mstMoldPartRel.setUpdateDate(new Date());
                        mstMoldPartRel.setCreateDate(existedMoldPartRel.getCreateDate());
                        mstMoldPartRel.setCreateUserUuid(existedMoldPartRel.getCreateUserUuid());
                        mstMoldPartRel.setUpdateUserUuid(loginUser.getUserUuid());
                        entityManager.merge(mstMoldPartRel);
                        updatedCount = updatedCount + 1;
                        fileUtil.writeInfoToFile(
                                logFile, 
                                fileUtil.outValue(
                                        csvCheckMsg.get("rowNumber"), 
                                        i, 
                                        csvHeader.get("moldId"), 
                                        mstMoldPartRel.getMstMold().getMoldId(),
                                        csvCheckMsg.get("error"), 
                                        0, 
                                        csvCheckMsg.get("dbProcess"), 
                                        csvCheckMsg.get("msgDataModified")
                                ));
                    }
                    
                } else if (null != comList.get(deleteFlgCol) && comList.get(deleteFlgCol).equals("1")) {
                    fileUtil.writeInfoToFile(
                            logFile, 
                            fileUtil.outValue(
                                    csvCheckMsg.get("rowNumber"), 
                                    i, 
                                    csvHeader.get("moldId"), 
                                    mstMoldPartRel.getMstMold().getMoldId(), 
                                    csvCheckMsg.get("error"), 
                                    1, 
                                    csvCheckMsg.get("dbProcess"), 
                                    csvCheckMsg.get("mstErrorRecordNotFound")
                            ));
                    failedCount = failedCount + 1;
                } else {
                    mstMoldPartRel.setId(IDGenerator.generate());
                    mstMoldPartRel.setCreateDate(new Date());
                    mstMoldPartRel.setCreateUserUuid(loginUser.getUserUuid());
                    mstMoldPartRel.setUpdateDate(new Date());
                    mstMoldPartRel.setUpdateUserUuid(loginUser.getUserUuid());                  
                    mstMoldPartRelService.createMstMoldPartRel(mstMoldPartRel);
                    addedCount = addedCount + 1;
                    fileUtil.writeInfoToFile(
                            logFile, 
                            fileUtil.outValue(
                                    csvCheckMsg.get("rowNumber"), 
                                    i, 
                                    csvHeader.get("moldId"), 
                                    mstMoldPartRel.getMstMold().getMoldId(), 
                                    csvCheckMsg.get("error"), 
                                    0, 
                                    csvCheckMsg.get("dbProcess"), 
                                    csvCheckMsg.get("msgRecordAdded")
                            ));
                }
            }
        }
    }
    
    /**
     * CSV
     * @param loginUser
     * @param fileUuid
     * @param csvInfoSize
     * @param logFileUuid
     */
    private void csvProcesExit(LoginUser loginUser, String fileUuid, int csvInfoSize, String logFileUuid) {

        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_MOLD_PART_REL);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(csvInfoSize - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MOLD_PART_REL);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);
    }
    
    /**
     * CSV
     *
     * @return
     */
    private ArrayList<ArrayList> getHeaderes(String langId) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        /*Head*/
        Map<String, String> csvHeader = getDictValues(langId);
        headList.add(csvHeader.get("moldId"));
        headList.add(csvHeader.get("location"));
        headList.add(csvHeader.get("moldPartCode"));
        headList.add(csvHeader.get("moldPartName"));
        headList.add(csvHeader.get("rplClShotCnt"));
        headList.add(csvHeader.get("rplClProdTimeHour"));
        headList.add(csvHeader.get("rplClLappsedDay"));
        headList.add(csvHeader.get("rprClShotCnt"));
        headList.add(csvHeader.get("rprClProdTimeHour"));
        headList.add(csvHeader.get("rprClLappsedDay"));
        headList.add(csvHeader.get("aftRplShotCnt"));
        headList.add(csvHeader.get("aftRplProdTimeHour"));
        headList.add(csvHeader.get("lastRplDatetime"));
        headList.add(csvHeader.get("aftRprShotCnt"));
        headList.add(csvHeader.get("aftRprProdTimeHour"));
        headList.add(csvHeader.get("lastRprDatetime"));
        headList.add(csvHeader.get("quantity"));
        //headList.add(csvHeader.get("alias"));
        headList.add(csvHeader.get("deleteRecord"));
        gLineList.add(headList);
        return gLineList;
    }
    /**
     * CSV
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictValues(String langId) {

        Map<String, String> dictMap = new HashMap<>();
        dictMap.put("moldId", mstDictionaryService.getDictionaryValue(langId, "mold_id"));
        dictMap.put("location", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_location"));
        dictMap.put("quantity", mstDictionaryService.getDictionaryValue(langId, "quantity"));
        dictMap.put("alias", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_alias"));
        dictMap.put("moldPartCode", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_mold_part_code"));
        dictMap.put("moldPartName", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_mold_part_name"));
        dictMap.put("rplClShotCnt", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_rpl_cl_shot_cnt"));
        dictMap.put("rplClProdTimeHour", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_rpl_cl_prod_time_hour"));
        dictMap.put("rplClLappsedDay", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_rpl_cl_lappsed_day"));
        dictMap.put("rprClShotCnt", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_rpr_cl_shot_cnt"));
        dictMap.put("rprClProdTimeHour", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_rpr_cl_prod_time_hour"));
        dictMap.put("rprClLappsedDay", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_rpr_lappsed_day"));
        dictMap.put("aftRplShotCnt", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_aft_rpl_shot_cnt"));
        dictMap.put("aftRplProdTimeHour", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_aft_rpl_prod_time_hour"));
        dictMap.put("lastRplDatetime", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_last_rpl_datetime"));
        dictMap.put("aftRprShotCnt", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_aft_rpr_shot_cnt"));
        dictMap.put("aftRprProdTimeHour", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_aft_rpr_prod_time_hour"));
        dictMap.put("lastRprDatetime", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_rel_last_rpr_datetime"));      
        dictMap.put("deleteRecord", mstDictionaryService.getDictionaryValue(langId, "delete_record"));
        return dictMap;
    }


    /**
     * CSV Info Check MSG
     *
     * @param langId
     * @return
     */
    private Map<String, String> getCsvInfoCheckMsg(String langId) {
        Map<String, String> msgMap = new HashMap<>();
        // info
        msgMap.put("rowNumber", mstDictionaryService.getDictionaryValue(langId, "row_number"));
        // error msg
        msgMap.put("msgErrorNotNull", mstDictionaryService.getDictionaryValue(langId, "msg_error_not_null"));
        msgMap.put("msgErrorOverLength", mstDictionaryService.getDictionaryValue(langId, "msg_error_over_length"));
        msgMap.put("msgErrorNotIsnumber", mstDictionaryService.getDictionaryValue(langId, "msg_error_not_isnumber"));
        msgMap.put("msgErrorInvalidValue", mstDictionaryService.getDictionaryValue(langId, "msg_error_value_invalid"));
        msgMap.put("msgErrorInvalidDateFormat", mstDictionaryService.getDictionaryValue(langId, "msg_error_date_format_invalid"));
        
        msgMap.put("error", mstDictionaryService.getDictionaryValue(langId, "error"));
        msgMap.put("errorDetail", mstDictionaryService.getDictionaryValue(langId, "error_detail"));
        msgMap.put("mstErrorRecordNotFound", mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found"));
        msgMap.put("msgErrorWrongCsvLayout", mstDictionaryService.getDictionaryValue(langId, "msg_error_wrong_csv_layout"));
        msgMap.put("msgCannotDeleteUsedRecord", mstDictionaryService.getDictionaryValue(langId, "msg_cannot_delete_used_record"));
        // db info
        msgMap.put("dbProcess", mstDictionaryService.getDictionaryValue(langId, "db_process"));
        msgMap.put("msgRecordAdded", mstDictionaryService.getDictionaryValue(langId, "msg_record_added"));
        msgMap.put("msgDataModified", mstDictionaryService.getDictionaryValue(langId, "msg_data_modified"));
        msgMap.put("msgErrorDataDeleted",mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
        msgMap.put("msgRecordDeleted",mstDictionaryService.getDictionaryValue(langId, "msg_record_deleted"));
        msgMap.put("msg_error_my_company_duplicate",mstDictionaryService.getDictionaryValue(langId, "msg_error_my_company_duplicate"));
        
        return msgMap;
    }
  /**
  * CSV
  * @param logMap
  * @param lineCsv
  * @param userLangId
  * @param logFile
  * @param index
  * @return 
  */
 private MstMoldPartRel checkCsvInfo(ArrayList lineCsv, String logFile, int index, FileUtil fileUtil, Map<String, String> csvHeader, Map<String, String> csvInfoMsg) {

     MstMoldPartRel mstMoldPartRel = new MstMoldPartRel();
     int arrayLength = lineCsv.size();
     if (arrayLength != MOLD_PART_REL_COLUMN_SIZE) {
        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldId"), "", csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorWrongCsvLayout")));
        failedCount = failedCount + 1;
        mstMoldPartRel.setIsError(true);
        return mstMoldPartRel;
     }
     String strMoldId = (null == lineCsv.get(0) ? "" : String.valueOf(lineCsv.get(0)));
     String strLocation = (null == lineCsv.get(1) ? "" : String.valueOf(lineCsv.get(1)));
     String strMoldPartCode = (null == lineCsv.get(2) ? "" : String.valueOf(lineCsv.get(2)));
     if (fileUtil.isNullCheck(strMoldId) || fileUtil.isNullCheck(strLocation) || fileUtil.isNullCheck(strMoldPartCode) ) {
         fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldId"), strMoldId, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotNull")));
         failedCount = failedCount + 1;
         mstMoldPartRel.setIsError(true);
         return mstMoldPartRel;
     } else if (fileUtil.maxLangthCheck(strMoldId, 45) || fileUtil.maxLangthCheck(strMoldId, 45) || fileUtil.maxLangthCheck(strLocation, 45)) {
         fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldId"), strMoldId, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
         failedCount = failedCount + 1;
         mstMoldPartRel.setIsError(true);
         return mstMoldPartRel;
     }
     
     String strRplClShotCnt = (null == lineCsv.get(4) ? "" : String.valueOf(lineCsv.get(4)));
     if (!fileUtil.isNullCheck(strRplClShotCnt)) {
         if (fileUtil.maxLangthCheck(strRplClShotCnt, 10)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rplClShotCnt"), strRplClShotCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!FileUtil.isInteger(strRplClShotCnt)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rplClShotCnt"), strRplClShotCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }

     String strRplClProdTimeHour = (null == lineCsv.get(5) ? "" : String.valueOf(lineCsv.get(5)));
     if (!fileUtil.isNullCheck(strRplClProdTimeHour)) {
         if (fileUtil.maxLangthCheck(strRplClProdTimeHour, 10)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rplClProdTimeHour"), strRplClProdTimeHour, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!FileUtil.isInteger(strRplClProdTimeHour)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rplClProdTimeHour"), strRplClProdTimeHour, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }

     String strRplClLappsedDay = (null == lineCsv.get(6) ? "" : String.valueOf(lineCsv.get(6)));
     if (!fileUtil.isNullCheck(strRplClLappsedDay)) {
         if (fileUtil.maxLangthCheck(strRplClLappsedDay, 10)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rplClLappsedDay"), strRplClLappsedDay, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!FileUtil.isInteger(strRplClLappsedDay)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rplClLappsedDay"), strRplClLappsedDay, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }
     
     String strRprClShotCnt = (null == lineCsv.get(7) ? "" : String.valueOf(lineCsv.get(7)));
     if (!fileUtil.isNullCheck(strRprClShotCnt)) {
         if (fileUtil.maxLangthCheck(strRprClShotCnt, 10)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rprClShotCnt"), strRprClShotCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!FileUtil.isInteger(strRprClShotCnt)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rprClShotCnt"), strRprClShotCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }

     String strRprClProdTimeHour = (null == lineCsv.get(8) ? "" : String.valueOf(lineCsv.get(8)));
     if (!fileUtil.isNullCheck(strRprClProdTimeHour)) {
         if (fileUtil.maxLangthCheck(strRprClProdTimeHour, 10)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rprClProdTimeHour"), strRprClProdTimeHour, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!FileUtil.isInteger(strRprClProdTimeHour)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rprClProdTimeHour"), strRprClProdTimeHour, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }
     

     String strRprClLappsedDay = (null == lineCsv.get(9) ? "" : String.valueOf(lineCsv.get(9)));
     if (!fileUtil.isNullCheck(strRprClLappsedDay)) {
         if (fileUtil.maxLangthCheck(strRprClLappsedDay, 10)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rprClLappsedDay"), strRprClLappsedDay, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!FileUtil.isInteger(strRprClLappsedDay)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("rprClLappsedDay"), strRprClLappsedDay, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }
     

     String strAftRplShotCnt = (null == lineCsv.get(10) ? "" : String.valueOf(lineCsv.get(10)));
     if (!fileUtil.isNullCheck(strAftRplShotCnt)) {
         if (fileUtil.maxLangthCheck(strAftRplShotCnt, 10)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("aftRplShotCnt"), strAftRplShotCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!FileUtil.isInteger(strAftRplShotCnt)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("aftRplShotCnt"), strAftRplShotCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }
     
     String strAftRplProdTimeHour = (null == lineCsv.get(11) ? "" : String.valueOf(lineCsv.get(11)));
     if (!fileUtil.isNullCheck(strAftRplProdTimeHour)) {
         if (fileUtil.maxLangthCheck(strAftRplProdTimeHour, 11)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("aftRplProdTimeHour"), strAftRplProdTimeHour, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!NumberUtil.validateDecimal(strAftRplProdTimeHour, 10, 1)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("aftRplProdTimeHour"), strAftRplProdTimeHour, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }
     
     String strLastRplDatetime = (null == lineCsv.get(12) ? "" : String.valueOf(lineCsv.get(12)));
     Date lastRplDatetime = fileUtil.getDateTimeParseForDate(strLastRplDatetime);
     if (!fileUtil.isNullCheck(strLastRplDatetime)) {
        if (null == lastRplDatetime) {
           fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("lastRplDatetime"), strLastRplDatetime, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidDateFormat")));
           failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
        }
     }

     String strAftRprShotCnt = (null == lineCsv.get(13) ? "" : String.valueOf(lineCsv.get(13)));
     if (!fileUtil.isNullCheck(strAftRprShotCnt)) {
         if (fileUtil.maxLangthCheck(strAftRprShotCnt, 10)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("aftRprShotCnt"), strAftRprShotCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!FileUtil.isInteger(strAftRprShotCnt)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("aftRprShotCnt"), strAftRprShotCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }
     
     String strAftRprProdTimeHour = (null == lineCsv.get(14) ? "" : String.valueOf(lineCsv.get(14)));
     if (!fileUtil.isNullCheck(strAftRprProdTimeHour)) {
         if (fileUtil.maxLangthCheck(strAftRprProdTimeHour, 11)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("aftRprProdTimeHour"), strAftRprProdTimeHour, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         } else if (!NumberUtil.validateDecimal(strAftRprProdTimeHour, 10, 1)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("aftRprProdTimeHour"), strAftRprProdTimeHour, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }

     String strLastRprDatetime = (null == lineCsv.get(15) ? "" : String.valueOf(lineCsv.get(15)));
     Date lastRprDatetime = fileUtil.getDateTimeParseForDate(strLastRprDatetime);
     if (!fileUtil.isNullCheck(strLastRprDatetime)) {
        if (null == lastRprDatetime) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("lastRprDatetime"), strLastRprDatetime, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidDateFormat")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
        }
     }
     String strQuantity = String.valueOf(lineCsv.get(16));
     if (strQuantity == null || strQuantity.equals("")) {
         strQuantity = "1";
     }
     else {
         if (!FileUtil.isInteger(strQuantity)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("quantity"), strQuantity, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidValue")));
            failedCount = failedCount + 1;
            mstMoldPartRel.setIsError(true);
            return mstMoldPartRel;
         }
     }
//     String strAlias = String.valueOf(lineCsv.get(17));
//     if (strAlias != null) {
//         if (fileUtil.maxLangthCheck(strAlias, 100)) {
//            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("alias"), strAlias, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
//            failedCount = failedCount + 1;
//            mstMoldPartRel.setIsError(true);
//            return mstMoldPartRel;
//         }
//     }

     String moldSql = "SELECT m FROM MstMold m WHERE m.moldId = :moldId";
     Query moldQuery = entityManager.createQuery(moldSql);
     moldQuery.setParameter("moldId", strMoldId);

     String moldPartSql = "SELECT p.id FROM MstMoldPart p WHERE p.moldPartCode = :moldPartCode";
     Query moldPartQuery = entityManager.createQuery(moldPartSql);
     moldPartQuery.setParameter("moldPartCode", strMoldPartCode);
     MstMold mold = null;
     try {
        mold = (MstMold) moldQuery.getSingleResult();
     } catch (NoResultException e) {
        mstMoldPartRel.setIsError(true);
        failedCount = failedCount +1;
        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldId"), strMoldId, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
        return mstMoldPartRel;
     }
     
     String moldPartId = "";
     try {
        moldPartId = moldPartQuery.getSingleResult().toString();
     } catch (NoResultException e) {
        mstMoldPartRel.setIsError(true);
        failedCount = failedCount +1;
        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldPartCode"), strMoldPartCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
        return mstMoldPartRel;
     }
     
     try {
        if (null != mold) {
            mstMoldPartRel.setMoldUuid(mold.getUuid());
        }
        mstMoldPartRel.setMoldPartId(moldPartId);
        mstMoldPartRel.setLocation(strLocation);
        mstMoldPartRel.setQuantity(Integer.parseInt(strQuantity));
        //mstMoldPartRel.setAlias(strAlias);
        mstMoldPartRel.setRplClShotCnt(Integer.parseInt(strRplClShotCnt));
        mstMoldPartRel.setRplClProdTimeHour(Integer.parseInt(strRplClProdTimeHour));
        mstMoldPartRel.setRplClLappsedDay(Integer.parseInt(strRplClLappsedDay));
        mstMoldPartRel.setRprClShotCnt(Integer.parseInt(strRprClShotCnt));
        mstMoldPartRel.setRprClProdTimeHour(Integer.parseInt(strRprClProdTimeHour));
        mstMoldPartRel.setRprClLappsedDay(Integer.parseInt(strRprClLappsedDay));
        mstMoldPartRel.setAftRplShotCnt(Integer.parseInt(strAftRplShotCnt));
        mstMoldPartRel.setAftRplProdTimeHour(new BigDecimal(strAftRplProdTimeHour));
        if (null != lastRplDatetime) {
            mstMoldPartRel.setLastRplDatetime(lastRplDatetime);
        }
        mstMoldPartRel.setAftRprShotCnt(Integer.parseInt(strAftRprShotCnt));
        mstMoldPartRel.setAftRprProdTimeHour(new BigDecimal(strAftRprProdTimeHour));
        if (null != lastRprDatetime) {
            mstMoldPartRel.setLastRprDatetime(lastRprDatetime);
        }
        mstMoldPartRel.setMstMold(mold);
        return mstMoldPartRel;
     } catch (Exception e) {
        mstMoldPartRel.setIsError(true);
        failedCount = failedCount +1;
        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("aftRprProdTimeHour"), strAftRprProdTimeHour, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotNull")));
        return mstMoldPartRel;
     }
 }
    /**
     * 金型一覧　CSV取り込みボタン CSVの中身に対してチェックを行う
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
        String head_moldId = logParm.get("head_moldId");//金型ID
        String head_moldName = logParm.get("head_moldName");//金型名称
        String head_moldType = logParm.get("head_moldType");//金型種類
        String head_moldAssetNo = logParm.get("head_moldAssetNo");//代表資産番号
        String head_moldCreatedDate = logParm.get("head_moldCreatedDate");//金型作成日
        String head_inspectedDdate = logParm.get("head_inspectedDdate");//検収日
        String head_department = logParm.get("head_department");//所属
        String head_moldOwnerCompanyCode = logParm.get("head_moldOwnerCompanyCode");//所有会社名称
        String head_installedDate = logParm.get("head_installedDate");//設置日
        String head_moldCompanyCode = logParm.get("head_moldCompanyCode");//会社名称
        String head_moldLocationCode = logParm.get("head_moldLocationCode");//所在地名称
        String head_moldInstallationSiteCode = logParm.get("head_moldInstallationSiteCode");//設置場所名称
        String head_status = logParm.get("head_status");//状態

        String head_lastProductionDate = logParm.get("head_lastProductionDate");
        String head_totalProductionTimeHour = logParm.get("head_totalProductionTimeHour");
        String head_totalShotCount = logParm.get("head_totalShotCount");
        String head_lastMainteDate = logParm.get("head_lastMainteDate");
        String head_afterMainteTotalProductionTimeHour = logParm.get("head_afterMainteTotalProductionTimeHour");
        String head_afterMainteTotalShotCount = logParm.get("head_afterMainteTotalShotCount");
        String head_mainteCycleCode01 = logParm.get("head_mainteCycleCode01");
        String head_mainteCycleCode02 = logParm.get("head_mainteCycleCode02");
        String head_mainteCycleCode03 = logParm.get("head_mainteCycleCode03");

        String head_moldspec = logParm.get("head_moldspec");//金型仕様
        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String maxLangth = logParm.get("maxLangth");
        String invalidValue = logParm.get("invalidValue");
        String dataCheck = logParm.get("dataCheck");
        String notnumber = logParm.get("notnumber");
        String notFound = logParm.get("notFound");
        FileUtil fu = new FileUtil();

        String strMoldId = lineCsv[0].trim();//金型ID
        if (fu.isNullCheck(strMoldId)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldId, strMoldId, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strMoldId, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldId, strMoldId, error, 1, errorContents, maxLangth));
            return false;
        }

        String strMoldName = lineCsv[1].trim();//金型名称
        if (fu.isNullCheck(strMoldName)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldName, strMoldName, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strMoldName, 100)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldName, strMoldName, error, 1, errorContents, maxLangth));
            return false;
        }

        String strMoldType = lineCsv[2].trim();//金型種類
        Object moldTypeValue = "0";
        Map inMoldTypeMapTempS = inMoldTypeOfChoice(userLangId);
        if (!"".equals(strMoldType.trim())) {
            if (null == (moldTypeValue = inMoldTypeMapTempS.get(strMoldType))) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldType, strMoldType, error, 1, errorContents, notFound));
                return false;
            } else if (fu.isNullCheck(moldTypeValue.toString())) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldType, strMoldType, error, 1, errorContents, notFound));
                return false;
            }
        }
        String strMoldAssetNo = lineCsv[3].trim();//代表資産番号
        if (fu.maxLangthCheck(strMoldAssetNo, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldAssetNo, strMoldAssetNo, error, 1, errorContents, maxLangth));
            return false;
        }

        String strMoldCreatedDate = lineCsv[4].trim();//金型作成日
        if (!fu.isNullCheck(strMoldCreatedDate)) {
            if (!fu.dateCheck(strMoldCreatedDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldCreatedDate, strMoldCreatedDate, error, 1, errorContents, dataCheck));
                return false;
            }
        }
        String strInspectedDate = lineCsv[5].trim();//検収日
        if (!fu.isNullCheck(strInspectedDate)) {
            if (!fu.dateCheck(strInspectedDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_inspectedDdate, strInspectedDate, error, 1, errorContents, dataCheck));
                return false;
            }
        }
        String strDepartment = lineCsv[6].trim();//所属
        if (fu.maxLangthCheck(strDepartment, 100)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_department, strDepartment, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strDepartment)) {
            Object moldDepartmentValue = "0";
            Map inDepartmentTempS = inDepartmentOfChoice(userLangId);
            if (null == (moldDepartmentValue = inDepartmentTempS.get(strDepartment))) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_department, strDepartment, error, 1, errorContents, notFound));
                return false;
            } else if (fu.isNullCheck(moldDepartmentValue.toString())) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_department, strDepartment, error, 1, errorContents, notFound));
                return false;
            }
        }
        String strOwnerCompanyCode = lineCsv[7].trim();//所有会社コード
        if (fu.maxLangthCheck(strOwnerCompanyCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldOwnerCompanyCode, strOwnerCompanyCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strOwnerCompanyCode)) {
            if (!mstCompanyService.getMstCompanyExistCheck(strOwnerCompanyCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldOwnerCompanyCode, strOwnerCompanyCode, error, 1, errorContents, notFound));
                return false;
            }
        }
//        String strInstalledDate = lineCsv[9].trim();//設置日
//        if (!fu.isNullCheck(strInstalledDate)) {
//            if (!fu.dateCheck(strInstalledDate)) {
//                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_installedDate, strInstalledDate, error, 1, errorContents, dataCheck));
//                return false;
//            }
//        }
        String strMoldCompanyCode = lineCsv[10].trim();//会社コード
        if (fu.maxLangthCheck(strMoldCompanyCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldCompanyCode, strMoldCompanyCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strMoldCompanyCode)) {
            if (!mstCompanyService.getMstCompanyExistCheck(strMoldCompanyCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldCompanyCode, strMoldCompanyCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strMoldLocationCode = lineCsv[12].trim();//所在地コード
        if (fu.maxLangthCheck(strMoldLocationCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldLocationCode, strMoldLocationCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strMoldLocationCode)) {
            if (fu.isNullCheck(strMoldCompanyCode) || !mstLocationService.getMstLocationExistCheck(strMoldLocationCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldLocationCode, strMoldLocationCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strMoldInstallationSiteCode = lineCsv[14].trim();//設置場所コード
        if (fu.maxLangthCheck(strMoldInstallationSiteCode, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldInstallationSiteCode, strMoldInstallationSiteCode, error, 1, errorContents, maxLangth));
            return false;
        } else if (!fu.isNullCheck(strMoldInstallationSiteCode)) {
            if (fu.isNullCheck(strMoldCompanyCode) || fu.isNullCheck(strMoldLocationCode) || !mstInstallationSiteService.getMstInstallationSiteExistCheck(strMoldInstallationSiteCode)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldInstallationSiteCode, strMoldInstallationSiteCode, error, 1, errorContents, notFound));
                return false;
            }
        }

        String strStatus = lineCsv[16].trim();//ステータス
        Map inStatusMapTempS = inStatusOfChoice(userLangId);
        if (inStatusMapTempS.get(strStatus) == null) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_status, strStatus, error, 1, errorContents, notFound));
            return false;
        } else if (fu.isNullCheck(inStatusMapTempS.get(strStatus).toString())) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_status, strStatus, error, 1, errorContents, notFound));
            return false;
        }

        String strLastProductionDate = lineCsv[18].trim();//LastProductionDate
        if (!fu.isNullCheck(strLastProductionDate)) {
            strLastProductionDate = DateFormat.formatDateYear(strLastProductionDate, DateFormat.DATE_FORMAT);
            if (!fu.dateCheck(strLastProductionDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_lastProductionDate, lineCsv[18], error, 1, errorContents, dataCheck));
                return false;
            }
        }
        String strTotalProductionTimeHour = lineCsv[19].trim();//TotalProductionTimeHour
        if (!fu.isNullCheck(strTotalProductionTimeHour)) {
            if (fu.maxLangthCheck(strTotalProductionTimeHour, 11)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_totalProductionTimeHour, strTotalProductionTimeHour, error, 1, errorContents, maxLangth));
                return false;
            //} else if (!FileUtil.isInteger(strTotalProductionTimeHour)) {
            } else if (!NumberUtil.validateDecimal(strTotalProductionTimeHour, 10, 1)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_totalProductionTimeHour, strTotalProductionTimeHour, error, 1, errorContents, invalidValue));
                return false;
            }
        }
        String strTotalShotCount = lineCsv[20].trim();//TotalShotCount
        if (!fu.isNullCheck(strTotalShotCount)) {
            if (fu.maxLangthCheck(strTotalShotCount, 11)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_totalShotCount, strTotalShotCount, error, 1, errorContents, maxLangth));
                return false;
            } else if (!FileUtil.isInteger(strTotalShotCount)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_totalShotCount, strTotalShotCount, error, 1, errorContents, invalidValue));
                return false;
            }
        }
        String strLastMainteDate = lineCsv[21].trim();//LastMainteDate
        if (!fu.isNullCheck(strLastMainteDate)) {
            strLastMainteDate = DateFormat.formatDateYear(strLastMainteDate, DateFormat.DATE_FORMAT);
            if (!fu.dateCheck(strLastMainteDate)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_lastMainteDate, lineCsv[21], error, 1, errorContents, dataCheck));
                return false;
            }
        }
        String strAfterMainteTotalProductionTimeHour = lineCsv[22].trim();//AfterMainteTotalProductionTimeHour
        if (!fu.isNullCheck(strAfterMainteTotalProductionTimeHour)) {
            if (fu.maxLangthCheck(strAfterMainteTotalProductionTimeHour, 11)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_afterMainteTotalProductionTimeHour, strAfterMainteTotalProductionTimeHour, error, 1, errorContents, maxLangth));
                return false;
//            } else if (!FileUtil.isInteger(strAfterMainteTotalProductionTimeHour)) {
            } else if (!NumberUtil.validateDecimal(strAfterMainteTotalProductionTimeHour, 10, 1)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_afterMainteTotalProductionTimeHour, strAfterMainteTotalProductionTimeHour, error, 1, errorContents, invalidValue));
                return false;
            }
        }
        String strAfterMainteTotalShotCount = lineCsv[23].trim();//AfterMainteTotalShotCount
        if (!fu.isNullCheck(strAfterMainteTotalShotCount)) {
            if (fu.maxLangthCheck(strAfterMainteTotalShotCount, 11)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_afterMainteTotalShotCount, strAfterMainteTotalShotCount, error, 1, errorContents, maxLangth));
                return false;
            } else if (!FileUtil.isInteger(strAfterMainteTotalShotCount)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_afterMainteTotalShotCount, strAfterMainteTotalShotCount, error, 1, errorContents, invalidValue));
                return false;
            }
        }
        String strMainteCycleCode01 = lineCsv[24].trim();//MainteCycleId01
        if (!fu.isNullCheck(strMainteCycleCode01)) {
            if (fu.maxLangthCheck(strMainteCycleCode01, 45)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode01, strMainteCycleCode01, error, 1, errorContents, maxLangth));
                return false;
            } else if (!fu.isNullCheck(strMainteCycleCode01)) {
                List<TblMaintenanceCyclePtn> maintenanceCyclePtns = tblMaintenanceCyclePtnService.getTblMaintenanceCyclePtnsByTypeAndCodes(1, strMainteCycleCode01);
                if (null == maintenanceCyclePtns || maintenanceCyclePtns.isEmpty()) {
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode01, strMainteCycleCode01, error, 1, errorContents, notFound));
                    return false;
                }
            }
        }
        String strMainteCycleCode02 = lineCsv[25].trim();//MainteCycleId02
        if (!fu.isNullCheck(strMainteCycleCode02)) {
            if (fu.maxLangthCheck(strMainteCycleCode02, 45)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode02, strMainteCycleCode02, error, 1, errorContents, maxLangth));
                return false;
            } else if (!fu.isNullCheck(strMainteCycleCode02)) {
                List<TblMaintenanceCyclePtn> maintenanceCyclePtns = tblMaintenanceCyclePtnService.getTblMaintenanceCyclePtnsByTypeAndCodes(1, strMainteCycleCode02);
                if (null == maintenanceCyclePtns || maintenanceCyclePtns.isEmpty()) {
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode02, strMainteCycleCode02, error, 1, errorContents, notFound));
                    return false;
                }
            }
        }
        String strMainteCycleCode03 = lineCsv[26].trim();//MainteCycleId03
        if (!fu.isNullCheck(strMainteCycleCode03)) {
            if (fu.maxLangthCheck(strMainteCycleCode03, 45)) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode03, strMainteCycleCode03, error, 1, errorContents, maxLangth));
                return false;
            } else if (!fu.isNullCheck(strMainteCycleCode03)) {
                List<TblMaintenanceCyclePtn> maintenanceCyclePtns = tblMaintenanceCyclePtnService.getTblMaintenanceCyclePtnsByTypeAndCodes(1, strMainteCycleCode03);
                if (null == maintenanceCyclePtns || maintenanceCyclePtns.isEmpty()) {
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_mainteCycleCode03, strMainteCycleCode03, error, 1, errorContents, notFound));
                    return false;
                }
            }
        }

        if (lineCsv.length >= 27) {//仕様があります時
            //用金型种类查金型attr
            MstMoldAttributeList mstMoldAttribute = mstMoldAttributeService.getMstMoldAttributes(moldTypeValue.toString());
            int attrCode = mstMoldAttribute.getMstMoldAttribute().size();
            int iFor;
            if (lineCsv.length - 28 > attrCode) {
                iFor = attrCode;
            } else {
                iFor = lineCsv.length - 28;
            }
            for (int j = 0; j < iFor; j++) {
                if (StringUtils.isEmpty(lineCsv[j + 27])) {
                    continue;
                }
                String strAttrValue = String.valueOf(lineCsv[j + 27]).trim();
                MstMoldAttribute aMstMoldAttribute = mstMoldAttribute.getMstMoldAttribute().get(j);
                int attrType = aMstMoldAttribute.getAttrType();
                // KM-427
                boolean numCheck = false;
                try {
                    Float.parseFloat(strAttrValue);
                } catch (Exception e) {
                    numCheck = true;
                }
                
                if (numCheck && CommonConstants.ATTRIBUTE_TYPE_NUMBER == attrType) { // KM-427
                //if (!fu.isNumber(strAttrValue) && CommonConstants.ATTRIBUTE_TYPE_NUMBER == attrType) {
                    fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldspec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, notnumber));
                    return false;
                }
                if (fu.checkSpecAttrType(attrType, strAttrValue) == false) {
                    //エラー情報をログファイルに記入
                    switch (attrType) {
                        case CommonConstants.ATTRIBUTE_TYPE_DATE:
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldspec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, dataCheck));
                            break;
                        case CommonConstants.ATTRIBUTE_TYPE_NUMBER:
                        case CommonConstants.ATTRIBUTE_TYPE_TEXT:
                        case CommonConstants.ATTRIBUTE_TYPE_STATICLINK:
                            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldspec.concat(String.valueOf(j + 1)), strAttrValue, error, 1, errorContents, maxLangth));
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
     *
     * @param moldId
     * @param moldName
     * @param ext
     * @param loginUser
     * @return
     */
    public MstMoldList getMstMoldAutoComplete(String moldId, String moldName, String ext, LoginUser loginUser) {

        StringBuilder sql;
        String sqlMoldId = "";
        String sqlMoldName = "";

        sql = new StringBuilder(" SELECT m FROM MstMold m WHERE 1=1 ");

        if ("like".equals(ext)) {
            if (moldId != null && !"".equals(moldId)) {
                sqlMoldId = moldId.trim();
                sql = sql.append(" and m.moldId like :moldId ");
            }

            if (moldName != null && !"".equals(moldName)) {
                sqlMoldName = moldName.trim();
                sql = sql.append(" and m.moldName like :moldName ");
            }
        } else {
            if (moldId != null && !"".equals(moldId)) {
                sqlMoldId = moldId.trim();
                sql = sql.append(" and m.moldId =:moldId ");
            }

            if (moldName != null && !"".equals(moldName)) {
                sqlMoldName = moldName.trim();
                sql = sql.append(" and m.moldName =:moldName ");
            }
        }
        sql = sql.append(" Order By m.moldId ");//金型IDの昇順

        Query query = entityManager.createQuery(sql.toString());

        if ("like".equals(ext)) {
            if (moldId != null && !"".equals(moldId)) {
                query.setParameter("moldId", "%" + sqlMoldId + "%");
            }

            if (moldName != null && !"".equals(moldName)) {
                query.setParameter("moldName", "%" + sqlMoldName + "%");
            }
            query.setMaxResults(100);
        } else {
            if (moldId != null && !"".equals(moldId)) {
                query.setParameter("moldId", sqlMoldId);
            }

            if (moldName != null && !"".equals(moldName)) {
                query.setParameter("moldName", sqlMoldName);
            }
        }

        MstMoldList mstMoldList = new MstMoldList();
        List<MstMoldAutoComplete> mstMoldAutoCompletes = new ArrayList<>();
        List list = query.getResultList();
        for (int i = 0; i < list.size(); i++) {
            MstMoldAutoComplete mstMoldAutoComplete = new MstMoldAutoComplete();
            MstMold mstMold = (MstMold) list.get(i);

            BasicResponse response = FileUtil.checkExternal(entityManager, mstDictionaryService, mstMold.getMoldId(), loginUser);
            if (response.isError()) {
                mstMoldAutoComplete.setExternalFlag("1");
            } else {
                mstMoldAutoComplete.setExternalFlag("0");
            }
            mstMoldAutoComplete.setUuid(mstMold.getUuid());
            mstMoldAutoComplete.setMoldId(mstMold.getMoldId());
            mstMoldAutoComplete.setMoldName(mstMold.getMoldName());
            mstMoldAutoComplete.setInstalledDate(mstMold.getInstalledDate());
            mstMoldAutoComplete.setMoldType(String.valueOf(FileUtil.getIntegerValue(mstMold.getMoldType())));
            if (null != mstMold.getMstCompanyByCompanyId()) {
                mstMoldAutoComplete.setCompanyId(mstMold.getMstCompanyByCompanyId().getId());
                mstMoldAutoComplete.setCompanyName(mstMold.getMstCompanyByCompanyId().getCompanyName());
            } else {
                mstMoldAutoComplete.setCompanyId("");
                mstMoldAutoComplete.setCompanyName("");
            }

            if (null != mstMold.getMstLocation()) {
                mstMoldAutoComplete.setLocationId(mstMold.getMstLocation().getId());
                mstMoldAutoComplete.setLocationName(mstMold.getMstLocation().getLocationName());
            } else {
                mstMoldAutoComplete.setLocationId("");
                mstMoldAutoComplete.setLocationName("");
            }

            if (null != mstMold.getMstInstallationSite()) {
                mstMoldAutoComplete.setInstllationSiteId(mstMold.getMstInstallationSite().getId());
                mstMoldAutoComplete.setInstllationSiteName(mstMold.getMstInstallationSite().getInstallationSiteName());
            } else {
                mstMoldAutoComplete.setInstllationSiteId("");
                mstMoldAutoComplete.setInstllationSiteName("");
            }
            mstMoldAutoComplete.setAfterMainteTotalShotCount(mstMold.getAfterMainteTotalShotCount() == null ? 0 : mstMold.getAfterMainteTotalShotCount());
            mstMoldAutoCompletes.add(mstMoldAutoComplete);
        }
        mstMoldList.setMstMoldAutoComplete(mstMoldAutoCompletes);
        return mstMoldList;
    }
   
    /**
     *
     * @param moldId
     * @param moldName
     * @param ext
     * @param loginUser
     * @return
     */
    public MstMoldList getMstMoldAutoCompleteWithoutDispose(String moldId, String moldName, String ext, LoginUser loginUser) {

        StringBuilder sql;
        String sqlMoldId = "";
        String sqlMoldName = "";

        sql = new StringBuilder(" SELECT m FROM MstMold m WHERE 1=1 and m.status <> 9");

        if ("like".equals(ext)) {
            if (moldId != null && !"".equals(moldId)) {
                sqlMoldId = moldId.trim();
                sql = sql.append(" and m.moldId like :moldId ");
            }

            if (moldName != null && !"".equals(moldName)) {
                sqlMoldName = moldName.trim();
                sql = sql.append(" and m.moldName like :moldName ");
            }
        } else {
            if (moldId != null && !"".equals(moldId)) {
                sqlMoldId = moldId.trim();
                sql = sql.append(" and m.moldId =:moldId ");
            }

            if (moldName != null && !"".equals(moldName)) {
                sqlMoldName = moldName.trim();
                sql = sql.append(" and m.moldName =:moldName ");
            }
        }
        sql = sql.append(" Order By m.moldId ");//金型IDの昇順

        Query query = entityManager.createQuery(sql.toString());

        if ("like".equals(ext)) {
            if (moldId != null && !"".equals(moldId)) {
                query.setParameter("moldId", "%" + sqlMoldId + "%");
            }

            if (moldName != null && !"".equals(moldName)) {
                query.setParameter("moldName", "%" + sqlMoldName + "%");
            }
            query.setMaxResults(100);
        } else {
            if (moldId != null && !"".equals(moldId)) {
                query.setParameter("moldId", sqlMoldId);
            }

            if (moldName != null && !"".equals(moldName)) {
                query.setParameter("moldName", sqlMoldName);
            }
        }

        MstMoldList mstMoldList = new MstMoldList();
        List<MstMoldAutoComplete> mstMoldAutoCompletes = new ArrayList<>();
        List list = query.getResultList();
        for (int i = 0; i < list.size(); i++) {
            MstMoldAutoComplete mstMoldAutoComplete = new MstMoldAutoComplete();
            MstMold mstMold = (MstMold) list.get(i);

            BasicResponse response = FileUtil.checkExternal(entityManager, mstDictionaryService, mstMold.getMoldId(), loginUser);
            if (response.isError()) {
                mstMoldAutoComplete.setExternalFlag("1");
            } else {
                mstMoldAutoComplete.setExternalFlag("0");
            }
            mstMoldAutoComplete.setUuid(mstMold.getUuid());
            mstMoldAutoComplete.setMoldId(mstMold.getMoldId());
            mstMoldAutoComplete.setMoldName(mstMold.getMoldName());
            mstMoldAutoComplete.setInstalledDate(mstMold.getInstalledDate());
            mstMoldAutoComplete.setMoldType(String.valueOf(FileUtil.getIntegerValue(mstMold.getMoldType())));
            if (null != mstMold.getMstCompanyByCompanyId()) {
                mstMoldAutoComplete.setCompanyId(mstMold.getMstCompanyByCompanyId().getId());
                mstMoldAutoComplete.setCompanyName(mstMold.getMstCompanyByCompanyId().getCompanyName());
            } else {
                mstMoldAutoComplete.setCompanyId("");
                mstMoldAutoComplete.setCompanyName("");
            }

            if (null != mstMold.getMstLocation()) {
                mstMoldAutoComplete.setLocationId(mstMold.getMstLocation().getId());
                mstMoldAutoComplete.setLocationName(mstMold.getMstLocation().getLocationName());
            } else {
                mstMoldAutoComplete.setLocationId("");
                mstMoldAutoComplete.setLocationName("");
            }

            if (null != mstMold.getMstInstallationSite()) {
                mstMoldAutoComplete.setInstllationSiteId(mstMold.getMstInstallationSite().getId());
                mstMoldAutoComplete.setInstllationSiteName(mstMold.getMstInstallationSite().getInstallationSiteName());
            } else {
                mstMoldAutoComplete.setInstllationSiteId("");
                mstMoldAutoComplete.setInstllationSiteName("");
            }
            mstMoldAutoComplete.setAfterMainteTotalShotCount(mstMold.getAfterMainteTotalShotCount() == null ? 0 : mstMold.getAfterMainteTotalShotCount());
            mstMoldAutoCompletes.add(mstMoldAutoComplete);
        }
        mstMoldList.setMstMoldAutoComplete(mstMoldAutoCompletes);
        return mstMoldList;
    }
    
    /**
     * QRコードを読み取り、その金型情報を表示する。 金型マスタ詳細取得
     *
     * @param moldId
     * @param langId
     * @return
     */
    public MstMoldDetail getMoldByMoldId(String moldId, String langId) {
        MstMoldDetail response = new MstMoldDetail();
        Query query1 = entityManager.createNamedQuery("MstMold.findByMoldId");
        query1.setParameter("moldId", moldId);
        try {
            MstMold mstMold = (MstMold) query1.getSingleResult();
            response.setMstMold(mstMold);
        } catch (NoResultException e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found"));
            return response;
        }

        return response;
    }

    /**
     * バッチで金型マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @param moldId
     * @param moldUuid
     * @param apiUserId
     * @return
     */
    public MstMoldDetailList getExtMoldsByBatch(String latestExecutedDate, String moldId, String moldUuid, String apiUserId) {
        MstMoldDetailList resList = new MstMoldDetailList();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMold t join MstApiUser u on u.companyId = t.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldId && !moldId.trim().equals("")) {
            sql.append(" and t.moldId = :moldId ");
        }
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.uuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        // 外部バッチ取得認証できたログインAPiユーザID
        sql.append(" and u.userId = :apiUserId ");
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldId && !moldId.trim().equals("")) {
            query.setParameter("moldId", moldId);
        }
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        query.setParameter("apiUserId", apiUserId);
        List<MstMoldDetail> mstMoldDetails = new ArrayList<>();
        List<MstMold> tmpList = query.getResultList();
        for (MstMold mstMold : tmpList) {
            MstMoldDetail aMoldDetail = new MstMoldDetail();
            aMoldDetail.setMoldUuid(mstMold.getUuid());
            if (null != mstMold.getCreateDate()) {
                aMoldDetail.setCreatedDate(mstMold.getCreateDate());
                aMoldDetail.setCreatedDateStr(new FileUtil().getDateFormatForStr(mstMold.getCreateDate()));
            }
            aMoldDetail.setCreateUserUuid(mstMold.getCreateUserUuid());
            aMoldDetail.setImgFilePath01(mstMold.getImgFilePath01());
            aMoldDetail.setImgFilePath02(mstMold.getImgFilePath02());
            aMoldDetail.setImgFilePath03(mstMold.getImgFilePath03());
            aMoldDetail.setImgFilePath04(mstMold.getImgFilePath04());
            aMoldDetail.setImgFilePath05(mstMold.getImgFilePath05());
            aMoldDetail.setImgFilePath06(mstMold.getImgFilePath06());
            aMoldDetail.setImgFilePath07(mstMold.getImgFilePath07());
            aMoldDetail.setImgFilePath08(mstMold.getImgFilePath08());
            aMoldDetail.setImgFilePath09(mstMold.getImgFilePath09());
            aMoldDetail.setImgFilePath10(mstMold.getImgFilePath10());

            aMoldDetail.setReportFilePath01(mstMold.getReportFilePath01());
            aMoldDetail.setReportFilePath02(mstMold.getReportFilePath02());
            aMoldDetail.setReportFilePath03(mstMold.getReportFilePath03());
            aMoldDetail.setReportFilePath04(mstMold.getReportFilePath04());
            aMoldDetail.setReportFilePath05(mstMold.getReportFilePath05());
            aMoldDetail.setReportFilePath06(mstMold.getReportFilePath06());
            aMoldDetail.setReportFilePath07(mstMold.getReportFilePath07());
            aMoldDetail.setReportFilePath08(mstMold.getReportFilePath08());
            aMoldDetail.setReportFilePath09(mstMold.getReportFilePath09());
            aMoldDetail.setReportFilePath10(mstMold.getReportFilePath10());

            if (null != mstMold.getInspectedDate()) {
                aMoldDetail.setInspectedDate(mstMold.getInspectedDate());
                aMoldDetail.setInspectedDateStr(new FileUtil().getDateFormatForStr(mstMold.getInspectedDate()));
            }
            if (null != mstMold.getInstalledDate()) {
                aMoldDetail.setInstalledDate(mstMold.getInstalledDate());
                aMoldDetail.setInstalledDateStr(new FileUtil().getDateFormatForStr(mstMold.getInstalledDate()));
            }
            if (null != mstMold.getInstllationSiteId()) {
                aMoldDetail.setInstllationSiteId(mstMold.getInstllationSiteId());
                aMoldDetail.setInstllationSiteName(mstMold.getInstllationSiteName());
            }
            if (null != mstMold.getLatestInventoryId()) {
                aMoldDetail.setLatestInventoryId(mstMold.getLatestInventoryId());
            }
            if (null != mstMold.getLocationId()) {
                aMoldDetail.setLocationId(mstMold.getLocationId());
                aMoldDetail.setLocationName(mstMold.getLocationName());
            }
            aMoldDetail.setMainteStatus(mstMold.getMainteStatus());
            if (null != mstMold.getMoldCreatedDate()) {
                aMoldDetail.setMoldCreatedDate(mstMold.getMoldCreatedDate());
                aMoldDetail.setMoldCreatedDateStr(new FileUtil().getDateFormatForStr(mstMold.getMoldCreatedDate()));
            }
            aMoldDetail.setMoldId(mstMold.getMoldId());
            aMoldDetail.setMoldName(mstMold.getMoldName());
            aMoldDetail.setMoldType(mstMold.getMoldType());
            aMoldDetail.setStatus(mstMold.getStatus());
            if (null != mstMold.getStatusChangedDate()) {
                aMoldDetail.setStatusChangedDateStr(new FileUtil().getDateFormatForStr(mstMold.getStatusChangedDate()));
            }
            if (null != mstMold.getUpdateDate()) {
                aMoldDetail.setUpdateDate(mstMold.getUpdateDate());
            }
            if (null != mstMold.getUpdateUserUuid()) {
                aMoldDetail.setUpdateUserUuid(mstMold.getUpdateUserUuid());
            }
            /// KM-260 LYD 追加　S
            //最終生産日
            aMoldDetail.setLastProductionDate(mstMold.getLastProductionDate());
            //累計生産時間
            aMoldDetail.setTotalProducingTimeHour(mstMold.getTotalProducingTimeHour());
            //累計ショット数
            aMoldDetail.setTotalShotCount(String.valueOf(FileUtil.getIntegerValue(mstMold.getTotalShotCount())));
            //最終メンテナンス日
            aMoldDetail.setLastMainteDate(mstMold.getLastMainteDate());
            //メンテナンス後生産時間
            aMoldDetail.setAfterMainteTotalProducingTimeHour(mstMold.getAfterMainteTotalProducingTimeHour());
            //メンテナンス後ショット数
            aMoldDetail.setAfterMainteTotalShotCount(mstMold.getAfterMainteTotalShotCount());
            /// KM-260 LYD 追加　E
            mstMoldDetails.add(aMoldDetail);
        }
        resList.setMstMoldDetail(mstMoldDetails);
        return resList;
    }

    /**
     * バッチで金型マスタデータを更新
     *
     * @param moldDetails
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldsByBatch(List<MstMoldDetail> moldDetails) {
        BasicResponse response = new BasicResponse();

        if (moldDetails != null && !moldDetails.isEmpty()) {
            for (MstMoldDetail aMoldDetail : moldDetails) {

                //金型
                MstMold updateMold = entityManager.find(MstMold.class, aMoldDetail.getMoldId());
                if (null == updateMold) {
                    continue;
                }
                // 連携しない updateMold.setUuid(aMold.getUuid());
                updateMold.setMoldName(aMoldDetail.getMoldName());
                updateMold.setMoldType(aMoldDetail.getMoldType());
                if (null != aMoldDetail.getMoldCreatedDateStr() && !aMoldDetail.getMoldCreatedDateStr().trim().equals("")) {
                    updateMold.setMoldCreatedDate(new FileUtil().getDateParseForDate(aMoldDetail.getMoldCreatedDateStr()));
                }
                if (null != aMoldDetail.getInspectedDateStr() && !aMoldDetail.getInspectedDateStr().trim().equals("")) {
                    updateMold.setInspectedDate(new FileUtil().getDateParseForDate(aMoldDetail.getInspectedDateStr()));
                }
                if (null != aMoldDetail.getInstalledDateStr() && !aMoldDetail.getInstalledDateStr().trim().equals("")) {
                    updateMold.setInstalledDate(new FileUtil().getDateParseForDate(aMoldDetail.getInstalledDateStr()));
                }

                if (null != aMoldDetail.getLocationId() && !aMoldDetail.getLocationId().trim().equals("")) {
                    MstLocation location = entityManager.find(MstLocation.class, aMoldDetail.getLocationId());
                    if (null != location) {
                        updateMold.setLocationId(aMoldDetail.getLocationId());
                        updateMold.setLocationName(aMoldDetail.getLocationName());
                    } else {
                        updateMold.setLocationId(null);
                        updateMold.setLocationName(null);
                    }
                } else {
                    updateMold.setLocationId(null);
                    updateMold.setLocationName(null);
                }

                if (null != aMoldDetail.getInstllationSiteId() && !aMoldDetail.getInstllationSiteId().trim().equals("")) {
                    MstInstallationSite installationSite = entityManager.find(MstInstallationSite.class, aMoldDetail.getInstllationSiteId());
                    if (null != installationSite) {
                        updateMold.setInstllationSiteId(aMoldDetail.getInstllationSiteId());
                        updateMold.setInstllationSiteName(aMoldDetail.getInstllationSiteName());
                    } else {
                        updateMold.setInstllationSiteId(null);
                        updateMold.setInstllationSiteName(null);
                    }
                } else {
                    updateMold.setInstllationSiteId(null);
                    updateMold.setInstllationSiteName(null);
                }

                if (null != aMoldDetail.getLatestInventoryId() && !aMoldDetail.getLatestInventoryId().trim().equals("")) {
                    TblMoldInventory moldInventory = entityManager.find(TblMoldInventory.class, aMoldDetail.getLatestInventoryId());
                    if (null != moldInventory) {
                        updateMold.setLatestInventoryId(aMoldDetail.getLatestInventoryId());
                    }
                }

                updateMold.setStatus(aMoldDetail.getStatus());
                if (null != aMoldDetail.getStatusChangedDateStr() && !aMoldDetail.getStatusChangedDateStr().trim().equals("")) {
                    updateMold.setStatusChangedDate(new FileUtil().getDateParseForDate(aMoldDetail.getStatusChangedDateStr()));
                }
                updateMold.setImgFilePath01(aMoldDetail.getImgFilePath01());
                updateMold.setImgFilePath02(aMoldDetail.getImgFilePath02());
                updateMold.setImgFilePath03(aMoldDetail.getImgFilePath03());
                updateMold.setImgFilePath04(aMoldDetail.getImgFilePath04());
                updateMold.setImgFilePath05(aMoldDetail.getImgFilePath05());
                updateMold.setImgFilePath06(aMoldDetail.getImgFilePath06());
                updateMold.setImgFilePath07(aMoldDetail.getImgFilePath07());
                updateMold.setImgFilePath08(aMoldDetail.getImgFilePath08());
                updateMold.setImgFilePath09(aMoldDetail.getImgFilePath09());
                updateMold.setImgFilePath10(aMoldDetail.getImgFilePath10());
                updateMold.setReportFilePath01(aMoldDetail.getReportFilePath01());
                updateMold.setReportFilePath02(aMoldDetail.getReportFilePath02());
                updateMold.setReportFilePath03(aMoldDetail.getReportFilePath03());
                updateMold.setReportFilePath04(aMoldDetail.getReportFilePath04());
                updateMold.setReportFilePath05(aMoldDetail.getReportFilePath05());
                updateMold.setReportFilePath06(aMoldDetail.getReportFilePath06());
                updateMold.setReportFilePath07(aMoldDetail.getReportFilePath07());
                updateMold.setReportFilePath08(aMoldDetail.getReportFilePath08());
                updateMold.setReportFilePath09(aMoldDetail.getReportFilePath09());
                updateMold.setReportFilePath10(aMoldDetail.getReportFilePath10());
                /// KM-260 LYD 追加　S
                //最終生産日
                updateMold.setLastProductionDate(aMoldDetail.getLastProductionDate());
                //累計生産時間
                updateMold.setTotalProducingTimeHour(aMoldDetail.getTotalProducingTimeHour());
                //累計ショット数
                updateMold.setTotalShotCount(Integer.parseInt(aMoldDetail.getTotalShotCount()));
                //最終メンテナンス日
                updateMold.setLastMainteDate(aMoldDetail.getLastMainteDate());
                //メンテナンス後生産時間
                updateMold.setAfterMainteTotalProducingTimeHour(aMoldDetail.getAfterMainteTotalProducingTimeHour());
                //メンテナンス後ショット数
                updateMold.setAfterMainteTotalShotCount(aMoldDetail.getAfterMainteTotalShotCount());
                /// KM-260 LYD 追加　E
                updateMold.setMainteStatus(aMoldDetail.getMainteStatus());
                updateMold.setMstMoldSpecHistoryCollection(null);
                if (null != aMoldDetail.getCreatedDateStr() && !aMoldDetail.getCreatedDateStr().trim().equals("")) {
                    updateMold.setCreateDate(new FileUtil().getDateParseForDate(aMoldDetail.getCreatedDateStr()));
                    updateMold.setCreateUserUuid(aMoldDetail.getCreateUserUuid());
                }
                updateMold.setUpdateDate(new Date());
                updateMold.setUpdateUserUuid(aMoldDetail.getUpdateUserUuid());
                entityManager.merge(updateMold);//更新

            }
        }
        return response;
    }

    /**
     * ユニークキー(MOLD_ID)による存在チェック
     *
     * @param uuid
     * @return
     */
    public boolean isExistsByPK(String uuid) {
        // 作業実績テーブルチェック
        Query query = entityManager.createNamedQuery("MstMold.findByUuid");
        query.setParameter("uuid", uuid);
        return query.getResultList().size() > 0;
    }

    /**
     * 金型IDによる1件取得
     *
     * @param moldId
     * @return
     */
    public MstMold getMstMoldByMoldId(String moldId) {
        Query query = entityManager.createNamedQuery("MstMold.findByMoldId");
        query.setParameter("moldId", moldId);
        try {
            return (MstMold) query.getSingleResult();
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
    public MstMold getMstMoldByUuid(String uuid) {
        Query query = entityManager.createNamedQuery("MstMold.findByUuid");
        query.setParameter("uuid", uuid);
        try {
            return (MstMold) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * ユニークキー(MOLD_ID)による存在チェック
     *
     * @param moldId
     * @return
     */
    public boolean isExistsByUniqueKey(String moldId) {
        // 作業実績テーブルチェック
        Query query = entityManager.createNamedQuery("MstMold.findByMoldId");
        query.setParameter("moldId", moldId);
        return query.getResultList().size() > 0;
    }

    /**
     * 金型登録画面用CSV入力外部金型レコードに対する代表資産番号のみ更新する処理
     *
     * @param readMstMold
     * @param strMoldId
     * @return
     */
    @Transactional
    public int updateExternalMstMold(MstMold readMstMold, String strMoldId) {
        String sql = "UPDATE MstMold m SET"
                + " m.mainAssetNo = :mainAssetNo,"
                + " m.mainteCycleId01 = :mainteCycleId01, "
//                + " m.mainteCycleId02 = :mainteCycleId02, "
//                + " m.mainteCycleId03 = :mainteCycleId03 "
                + " WHERE m.moldId = :moldId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("mainAssetNo", readMstMold.getMainAssetNo());
        query.setParameter("mainteCycleId01", readMstMold.getMainteCycleId01());
//        query.setParameter("mainteCycleId02", readMstMold.getMainteCycleId02());
//        query.setParameter("mainteCycleId03", readMstMold.getMainteCycleId03());
        query.setParameter("moldId", strMoldId);
        return query.executeUpdate();
    }

    boolean checkExtCsvFileData(Map<String, String> logParm, String lineCsv[], String userLangId, String logFile, int index) {
        //ログ出力内容を用意する
        /**
         * Head
         */
        String lineNo = logParm.get("lineNo");
        String head_moldId = logParm.get("head_moldId");//金型ID        
        String head_moldAssetNo = logParm.get("head_moldAssetNo");//代表資産番号  
        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String maxLangth = logParm.get("maxLangth");
        FileUtil fu = new FileUtil();

        String strMachineId = lineCsv[0].trim();//設備ID
        if (fu.isNullCheck(strMachineId)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldId, strMachineId, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strMachineId, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldId, strMachineId, error, 1, errorContents, maxLangth));
            return false;
        }

        String strMachineAssetNo = lineCsv[3].trim();//代表資産番号
        if (fu.maxLangthCheck(strMachineAssetNo, 45)) {
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, head_moldAssetNo, strMachineAssetNo, error, 1, errorContents, maxLangth));
            return false;
        }

        return true;
    }

    /**
     * 生産終了時金型詳細更新(最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数)
     * 金型のみ指定して生産開始終了用
     *
     * @param tblProduction
     * @param tblProductionVo
     * @param cnfSystem
     * @param loginUser
     */
//    @Transactional
//    public void updateMstMoldForProductionEnd(TblProduction tblProduction, TblProductionVo tblProductionVo, CnfSystem cnfSystem, LoginUser loginUser) {
//        MstMold mold = tblProduction.getMstMold();
//        if (mold != null) {
//            Date businessDate = DateFormat.strToDate(DateFormat.getBusinessDate(DateFormat.dateToStr(tblProduction.getEndDatetime(), DateFormat.DATETIME_FORMAT), cnfSystem));
//            if (mold.getLastProductionDate() == null || (businessDate != null && mold.getLastProductionDate().compareTo(businessDate) < 0)) {
//                mold.setLastProductionDate(businessDate);
//            }
//            mold.setTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getTotalProducingTimeHour())
//                    + (FileUtil.getIntegerValue(tblProductionVo.getNetProducintTimeMinutes()) - tblProductionVo.getNetProducintTimeMinutesBeforeUpd()) / 60);
//            mold.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getAfterMainteTotalProducingTimeHour())
//                    + (FileUtil.getIntegerValue(tblProductionVo.getNetProducintTimeMinutes()) - tblProductionVo.getNetProducintTimeMinutesBeforeUpd()) / 60);
//            mold.setTotalShotCount(FileUtil.getIntegerValue(mold.getTotalShotCount())
//                    + (tblProductionVo.getShotCount() - tblProductionVo.getShotCountBeforeUpd()) + (tblProductionVo.getDisposedShotCount() - tblProductionVo.getDisposedShotCountBeforeUpd()));
//            mold.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(mold.getAfterMainteTotalShotCount())
//                    + (tblProductionVo.getShotCount() - tblProductionVo.getShotCountBeforeUpd()) + (tblProductionVo.getDisposedShotCount() - tblProductionVo.getDisposedShotCountBeforeUpd()));
//            mold.setUpdateDate(new Date());
//            mold.setUpdateUserUuid(loginUser.getUserUuid());
//            entityManager.merge(mold);
//        }
//    }

    /**
     * 機械日報登録/更新/削除時金型詳細更新(最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数)
     *
     * @param tblProduction
     * @param machineDailyReportVo
     * @param loginUser
     */
    @Transactional
    public void updateMstMoldForDailyReport(TblProduction tblProduction, TblMachineDailyReportVo machineDailyReportVo, LoginUser loginUser) {
        MstMold mold = tblProduction.getMstMold();
        if (mold != null) {
            if (mold.getLastProductionDate() == null || (machineDailyReportVo.getProductionDate() != null && mold.getLastProductionDate().compareTo(machineDailyReportVo.getProductionDate()) < 0)) {
                mold.setLastProductionDate(machineDailyReportVo.getProductionDate());
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
            mold.setTotalShotCount(FileUtil.getIntegerValue(mold.getTotalShotCount())
                    + (shotCount - machineDailyReportVo.getShotCountBeforeUpd()) + (disposedShotCount - machineDailyReportVo.getDisposedShotCountBeforeUpd()));
            mold.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(mold.getAfterMainteTotalShotCount())
                    + (shotCount - machineDailyReportVo.getShotCountBeforeUpd()) + (disposedShotCount - machineDailyReportVo.getDisposedShotCountBeforeUpd()));
            BigDecimal bdMinutes = new BigDecimal(String.valueOf(netProducintTimeMinutes - machineDailyReportVo.getNetProducintTimeMinutesBeforeUpd()));
            BigDecimal bd60Minutes = new BigDecimal("60");
            BigDecimal bdHours = bdMinutes.divide(bd60Minutes, 1, BigDecimal.ROUND_HALF_UP);
            mold.setTotalProducingTimeHour(mold.getTotalProducingTimeHour() == null ? bdHours : mold.getTotalProducingTimeHour().add(bdHours));
            mold.setAfterMainteTotalProducingTimeHour(mold.getAfterMainteTotalProducingTimeHour() == null ? bdHours : mold.getAfterMainteTotalProducingTimeHour().add(bdHours));
            mold.setUpdateDate(new Date());
            mold.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(mold);
        }
    }

    private int getComponentList(String moldId, ArrayList tempOutList) {
        // 部品取得
        List<MstMoldComponentRelation> moldComponentRelations = entityManager.createQuery(" SELECT m FROM MstMoldComponentRelation m "
                + " JOIN FETCH m.mstComponent "
                + " JOIN FETCH m.mstMold "
                + " WHERE m.mstMold.moldId =:moldId order by m.mstComponent.componentCode asc ")
                .setParameter("moldId", moldId)
                .getResultList();
        int itemCont = 0;
        for (MstMoldComponentRelation mstMoldComponentRelation : moldComponentRelations) {
            MstComponent csvMstComponent = mstMoldComponentRelation.getMstComponent();

            tempOutList.add(csvMstComponent.getComponentCode());
            tempOutList.add("" + FileUtil.getIntegerValue(mstMoldComponentRelation.getCountPerShot()));

            itemCont = itemCont + 1;
        }
        return itemCont;
    }

    /**
     * 金型詳細画面で下記項目について、いずれか更新されたら、金型候補テーブルに該当金型が未メンテのデータを削除を行う
     *
     * @param oldMstMold
     * @param formMold
     */
    public void deleteMoldMaintenanceRecomend(MstMold oldMstMold, MstMold formMold) {
        //下記項目について、いずれか更新されたら、金型候補テーブルに該当金型が未メンテのデータを削除を行う
        //最終メンテナンス日
        boolean recomendDelFlg = false;
        if (oldMstMold.getLastMainteDate() == null && formMold.getLastMainteDate() == null) {
            recomendDelFlg = false;
        } else if (oldMstMold.getLastMainteDate() == null && formMold.getLastMainteDate() != null) {
            recomendDelFlg = true;
        } else if (oldMstMold.getLastMainteDate() != null && formMold.getLastMainteDate() == null) {
            recomendDelFlg = true;
        } else if (oldMstMold.getLastMainteDate() != null && formMold.getLastMainteDate() != null) {
            if (oldMstMold.getLastMainteDate().compareTo(formMold.getLastMainteDate()) != 0) {
                recomendDelFlg = true;
            }
        }

        if (!recomendDelFlg) {
            //メンテナンス後生産時間
            if (oldMstMold.getAfterMainteTotalProducingTimeHour() == null && formMold.getAfterMainteTotalProducingTimeHour() == null) {
                recomendDelFlg = false;
            } else if (oldMstMold.getAfterMainteTotalProducingTimeHour() == null && formMold.getAfterMainteTotalProducingTimeHour() != null) {
                recomendDelFlg = true;
            } else if (oldMstMold.getAfterMainteTotalProducingTimeHour() != null && formMold.getAfterMainteTotalProducingTimeHour() == null) {
                recomendDelFlg = true;
            } else if (oldMstMold.getAfterMainteTotalProducingTimeHour() != null && formMold.getAfterMainteTotalProducingTimeHour() != null) {
                if (oldMstMold.getAfterMainteTotalProducingTimeHour().compareTo(formMold.getAfterMainteTotalProducingTimeHour()) != 0) {
                    recomendDelFlg = true;
                }
            }
        }
        if (!recomendDelFlg) {
            //メンテナンス後ショット数
            if (oldMstMold.getAfterMainteTotalShotCount() == null && formMold.getAfterMainteTotalShotCount() == null) {
                recomendDelFlg = false;
            } else if (oldMstMold.getAfterMainteTotalShotCount() == null && formMold.getAfterMainteTotalShotCount() != null) {
                recomendDelFlg = true;
            } else if (oldMstMold.getAfterMainteTotalShotCount() != null && formMold.getAfterMainteTotalShotCount() == null) {
                recomendDelFlg = true;
            } else if (oldMstMold.getAfterMainteTotalShotCount() != null && formMold.getAfterMainteTotalShotCount() != null) {
                if (oldMstMold.getAfterMainteTotalShotCount().compareTo(formMold.getAfterMainteTotalShotCount()) != 0) {
                    recomendDelFlg = true;
                }
            }
        }
        if (!recomendDelFlg) {
            //メンテサイクルコード01
            String oldMainteCycleId01 = FileUtil.getStringValue(oldMstMold.getMainteCycleId01());
            String newMainteCycleId01 = FileUtil.getStringValue(formMold.getMainteCycleId01());
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
            String oldMainteCycleId02 = FileUtil.getStringValue(oldMstMold.getMainteCycleId02());
            String newMainteCycleId02 = FileUtil.getStringValue(formMold.getMainteCycleId02());
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
            String oldMainteCycleId03 = FileUtil.getStringValue(oldMstMold.getMainteCycleId03());
            String newMainteCycleId03 = FileUtil.getStringValue(formMold.getMainteCycleId03());
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
            tblMoldMaintenanceRecomendService.deleteTblMoldMaintenanceRecomendByMoldUuid(oldMstMold.getUuid());
        }
    }

    /**
     * 金型送信論理(20170609 Apeng add)
     * <P>
     * 金型情報を金型貸出先に送るため、カルテ間データ連携により金型マスタの送信を指示する。
     *
     * @param mstMoldSendList
     * @param loginUser
     * @return
     */
    public BasicResponse sendMoldToUsedCompany(MstMoldSendList mstMoldSendList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        MstExternalDataGetSetting externalCompany;//外部データ
        StringBuilder baseUrl = new StringBuilder();
        StringBuilder sendUrl = new StringBuilder();
        URL urlStr;
        Credential result = new Credential();
        if (mstMoldSendList != null) {
            List<MstMold> mstMolds = mstMoldSendList.getMstMolds();//金型
            if (mstMolds == null || mstMolds.size() <= 0) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                return response;
            }
            // 受信者リストを設定する
            List<String> receiverList = new ArrayList();
            for (String mailAddress : mstMoldSendList.getMailAddress().split(",")) {
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
            queryMstcompany.setParameter("companyId", mstMoldSendList.getCompanyId());
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
            subject = mstMoldSendList.getSubject();
            letterBody = mstMoldSendList.getLetterBody();
            BasicResponse basicResponse = sendMoldInfoToUsedCompany(mstMolds, sendUrl.toString(), result.getToken(), loginUser);

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
                    Logger.getLogger(MstMoldService.class.getName()).log(Level.SEVERE, null, e);
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
     * 金型送信入力
     *
     * @param fileUuid
     * @param loginUser
     * @return
     */
    public MstMoldSendList sendImportCsv(String fileUuid, LoginUser loginUser) {
        MstMoldSendList mstMoldSendList = new MstMoldSendList();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            mstMoldSendList.setError(true);
            mstMoldSendList.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            mstMoldSendList.setErrorMessage(msg);
            return mstMoldSendList;
        }

        ArrayList readList = CSVFileUtil.readCsv(csvFile);

        if (readList.size() <= 1) {
            return mstMoldSendList;
        } else {
            List<MstMold> mstMolds = new ArrayList();
            List<String> mstMoldIds = new ArrayList();
            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);
                mstMoldIds.add(String.valueOf(comList.get(0)).trim());
            }
            
            StringBuilder sql;
            sql = new StringBuilder("SELECT m FROM MstMold m WHERE m.moldId IN :mstMoldIds");
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("mstMoldIds", mstMoldIds);
            List<MstMold> list = query.getResultList();
            if (list != null && list.size() > 0) {
                for (MstMold mstMold : list) {
                    mstMold.setMoldId(mstMold.getMoldId());
                    mstMold.setMoldName(mstMold.getMoldName());
                    mstMolds.add(mstMold);
                }
            }
            mstMoldSendList.setMstMolds(mstMolds);
        }
        return mstMoldSendList;
    }

    /**
     *
     * @param mstMolds
     * @param sendUrl
     * @param resultToken
     * @param loginUser
     * @return
     */
    public BasicResponse sendMoldInfoToUsedCompany(
            List<MstMold> mstMolds,
            String sendUrl,
            String resultToken,
            LoginUser loginUser
    ) {
        BasicResponse response = new BasicResponse();
        StringBuilder baseUrl = new StringBuilder();
        TblMoldReceptionList tblMoldReceptionList = new TblMoldReceptionList();
        List<TblMoldReceptionVo> vos = new ArrayList();

        // 実施者
        Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
        userQuery.setParameter("uuid", loginUser.getUserUuid());
        MstUser users = (MstUser) userQuery.getSingleResult();
        TblMoldReceptionVo tblMoldReceptionVo;
        for (MstMold mstMold : mstMolds) {
            MstMold mstMoldFind = entityManager.find(MstMold.class, mstMold.getMoldId());

            if (mstMoldFind != null) {
                Iterator<MstMoldComponentRelation> mstMoldComponentRelation = mstMoldFind.getMstMoldComponentRelationCollection().iterator();
                if (mstMoldComponentRelation != null && mstMoldComponentRelation.hasNext()) {
                    while (mstMoldComponentRelation.hasNext()) {
                        MstMoldComponentRelation componentRelation = mstMoldComponentRelation.next();
                        if (componentRelation != null) {
                            tblMoldReceptionVo = new TblMoldReceptionVo();//金型受信テーブル
                            tblMoldReceptionVo.setMoldId(mstMoldFind.getMoldId());//金型ID
                            tblMoldReceptionVo.setMoldName(mstMoldFind.getMoldName());//金型名称
                            tblMoldReceptionVo.setOtherComponentCode(componentRelation.getMstComponent().getComponentCode());//先方部品コード
                            tblMoldReceptionVo.setOtherComponentName(componentRelation.getMstComponent().getComponentName());//先方部品名称
                            tblMoldReceptionVo.setOwnerContactName(users.getUserName() == null ? "" : users.getUserName()); //所有会社担当者
                            vos.add(tblMoldReceptionVo);
                        }
                    }
                } else {
                    tblMoldReceptionVo = new TblMoldReceptionVo();//金型受信テーブル
                    tblMoldReceptionVo.setMoldId(mstMoldFind.getMoldId());//金型ID
                    tblMoldReceptionVo.setMoldName(mstMoldFind.getMoldName());//金型名称
                    tblMoldReceptionVo.setOwnerContactName(users.getUserName() == null ? "" : users.getUserName()); //所有会社担当者
                    vos.add(tblMoldReceptionVo);
                }
                tblMoldReceptionList.setTblMoldReceptionVos(vos);
            }
        }

        try {
            baseUrl = baseUrl.append(sendUrl).append(EXT_MOLD_RECEPTION_UPDATE);
            String str = FileUtil.sendPost(baseUrl.toString(), resultToken, tblMoldReceptionList);
            Gson gson = new Gson();
            response = gson.fromJson(str, new TypeToken<BasicResponse>() {
            }.getType());
        } catch (Exception e) {
            Logger.getLogger(MstMoldService.class.getName()).log(Level.SEVERE, null, e);
        }

        return response;
    }
    
    public MstMoldDetailList getMstMoldsByPage(String moldId, String moldName, String mainAssetNo,
            String ownerCompanyName, String companyName, String locationName, String instllationSiteName,
            Integer moldType, Integer department, Date lastProductionDateFrom, Date lastProductionDateTo,
            Integer totalProducingTimeHourFrom, Integer totalProducingTimeHourTo, Integer totalShotCountFrom,
            Integer totalShotCountTo, Date lastMainteDateFrom, Date lastMainteDateTo,
            Integer afterMainteTotalProducingTimeHourFrom, Integer afterMainteTotalProducingTimeHourTo,
            Integer afterMainteTotalShotCountFrom, Integer afterMainteTotalShotCountTo, Date moldCreatedDateFrom,
            Date moldCreatedDateTo, Integer status, LoginUser loginUser, String sidx, String sord, int pageNumber,
            int pageSize, boolean isPage) {
        
        MstMoldDetailList mstMoldDetailList = new MstMoldDetailList();
        
        List<MstMoldDetail> mstMoldDetails = new ArrayList<>();
        
        FileUtil fu = new FileUtil();
        
        if (isPage) {

            List count = getSqlByPage(moldId, moldName, mainAssetNo, ownerCompanyName, companyName, locationName,
                    instllationSiteName, moldType, department, lastProductionDateFrom, lastProductionDateTo,
                    totalProducingTimeHourFrom, totalProducingTimeHourTo, totalShotCountFrom, totalShotCountTo,
                    lastMainteDateFrom, lastMainteDateTo, afterMainteTotalProducingTimeHourFrom,
                    afterMainteTotalProducingTimeHourTo, afterMainteTotalShotCountFrom, afterMainteTotalShotCountTo,
                    moldCreatedDateFrom, moldCreatedDateTo, status, sidx, sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            mstMoldDetailList.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            mstMoldDetailList.setCount(counts);
            mstMoldDetailList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }
        
        List list = getSqlByPage(moldId, moldName, mainAssetNo, ownerCompanyName, companyName, locationName,
                instllationSiteName, moldType, department, lastProductionDateFrom, lastProductionDateTo,
                totalProducingTimeHourFrom, totalProducingTimeHourTo, totalShotCountFrom, totalShotCountTo,
                lastMainteDateFrom, lastMainteDateTo, afterMainteTotalProducingTimeHourFrom,
                afterMainteTotalProducingTimeHourTo, afterMainteTotalShotCountFrom, afterMainteTotalShotCountTo,
                moldCreatedDateFrom, moldCreatedDateTo, status, sidx, sord, pageNumber, pageSize, false);
        
        if (list != null && list.size() > 0) {
            MstChoiceList mstDepartmentChoiceList = mstChoiceService.getChoice(loginUser.getLangId(),
                    "mst_user.department");
            for (int i = 0; i < list.size(); i++) {
                MstMold mstMold = (MstMold) list.get(i);
                MstMoldDetail mstMoldDetail = new MstMoldDetail();
                mstMoldDetail.setMoldUuid(mstMold.getUuid());
                mstMoldDetail.setMoldId(mstMold.getMoldId());
                mstMoldDetail.setMoldName(mstMold.getMoldName());
                Integer itemMoldType = mstMold.getMoldType();
                if (FileUtil.checkExternal(entityManager, mstDictionaryService, mstMold.getMoldId(), loginUser)
                        .isError() == true) {
                    mstMoldDetail.setExternalFlg(1);
                    if (itemMoldType != null) {
                        mstMoldDetail.setMoldTypeText(extMstChoiceService.getExtMstChoiceText(mstMold.getCompanyId(),
                                "mst_mold.mold_type", String.valueOf(itemMoldType), loginUser.getLangId()));
                    }
                } else {
                    mstMoldDetail.setExternalFlg(0);
                    if (itemMoldType != null) {
                        mstMoldDetail.setMoldType(itemMoldType);
                    }
                }

                if (mstMold.getMainAssetNo() != null && !"".equals(mstMold.getMainAssetNo())) {
                    mstMoldDetail.setMainAssetNo(mstMold.getMainAssetNo());
                } else {
                    mstMoldDetail.setMainAssetNo("");
                }
                if (mstMold.getMoldCreatedDate() != null) {
                    mstMoldDetail.setMoldCreatedDate(mstMold.getMoldCreatedDate());
                }
                if (mstMold.getInspectedDate() != null) {
                    mstMoldDetail.setInspectedDate(mstMold.getInspectedDate());
                }
                if (mstMold.getMstCompanyByOwnerCompanyId() != null
                        && mstMold.getMstCompanyByOwnerCompanyId().getCompanyName() != null) {
                    mstMoldDetail.setOwnerCompanyName(mstMold.getMstCompanyByOwnerCompanyId().getCompanyName());
                } else {
                    mstMoldDetail.setOwnerCompanyName("");
                }
                if (mstMold.getInstalledDate() != null) {
                    mstMoldDetail.setInstalledDate(mstMold.getInstalledDate());
                }
                if (mstMold.getMstCompanyByCompanyId() != null) {
                    mstMoldDetail.setCompanyId(mstMold.getMstCompanyByCompanyId().getId());
                    mstMoldDetail.setCompanyName(mstMold.getMstCompanyByCompanyId().getCompanyName());
                } else {
                    mstMoldDetail.setCompanyId("");
                    mstMoldDetail.setCompanyName("");
                }

                if (mstMold.getMstLocation() != null) {
                    mstMoldDetail.setLocationId(mstMold.getMstLocation().getId());
                    mstMoldDetail.setLocationName(mstMold.getMstLocation().getLocationName());
                } else {
                    mstMoldDetail.setLocationId("");
                    mstMoldDetail.setLocationName("");
                }

                if (mstMold.getMstInstallationSite() != null) {
                    mstMoldDetail.setInstllationSiteId(mstMold.getMstInstallationSite().getId());
                    mstMoldDetail.setInstllationSiteName(mstMold.getMstInstallationSite().getInstallationSiteName());
                } else {
                    mstMoldDetail.setInstllationSiteId("");
                    mstMoldDetail.setInstllationSiteName("");
                }

                if (mstMold.getStatus() != null) {
                    mstMoldDetail.setStatus(mstMold.getStatus());
                }
                if (mstMold.getStatusChangedDate() != null) {
                    mstMoldDetail.setStatusChangedDate(mstMold.getStatusChangedDate());
                }

                if (mstMold.getDepartment() != null) {
                    mstMoldDetail.setDepartment("" + mstMold.getDepartment());
                    for (MstChoice mstChoice : mstDepartmentChoiceList.getMstChoice()) {
                        if (mstChoice.getMstChoicePK().getSeq().equals(mstMoldDetail.getDepartment())) {
                            mstMoldDetail.setDepartmentName(mstChoice.getChoice());
                            break;
                        }
                    }
                }

                // 4.2 Zhangying S
                // 最終生産日
                if (mstMold.getLastProductionDate() != null) {
                    mstMoldDetail.setLastProductionDate(mstMold.getLastProductionDate());
                    mstMoldDetail.setLastProductionDateStr(fu.getDateFormatForStr(mstMold.getLastProductionDate()));
                } else {
                    mstMoldDetail.setLastProductionDate(null);
                    mstMoldDetail.setLastProductionDateStr("");
                }
                // 累計生産時間
                if (mstMold.getTotalProducingTimeHour() != null) {
                    mstMoldDetail.setTotalProducingTimeHour(mstMold.getTotalProducingTimeHour());
                } else {
                    mstMoldDetail.setTotalProducingTimeHour(BigDecimal.ZERO);
                }
                // 累計ショット数
                if (mstMold.getTotalShotCount() != null) {
                    mstMoldDetail.setTotalShotCount("" + mstMold.getTotalShotCount());
                } else {
                    mstMoldDetail.setTotalShotCount("0");
                }
                // 最終メンテナンス日
                if (mstMold.getLastMainteDate() != null) {
                    mstMoldDetail.setLastMainteDate(mstMold.getLastMainteDate());
                    mstMoldDetail.setLastMainteDateStr(fu.getDateFormatForStr(mstMold.getLastMainteDate()));
                } else {
                    mstMoldDetail.setLastMainteDate(null);
                    mstMoldDetail.setLastMainteDateStr("");
                }
                // メンテナンス後生産時間
                if (mstMold.getAfterMainteTotalProducingTimeHour() != null) {
                    mstMoldDetail.setAfterMainteTotalProducingTimeHour(mstMold.getAfterMainteTotalProducingTimeHour());
                } else {
                    mstMoldDetail.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
                }
                // メンテナンス後ショット数
                if (mstMold.getAfterMainteTotalShotCount() != null) {
                    mstMoldDetail.setAfterMainteTotalShotCount(mstMold.getAfterMainteTotalShotCount());
                } else {
                    mstMoldDetail.setAfterMainteTotalShotCount(0);
                }

                if (null != mstMold.getBlMaintenanceCyclePtn01()) {
                    // メンテサイクルコード01
                    mstMoldDetail.setMainteCycleCode01(
                            mstMold.getBlMaintenanceCyclePtn01().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    mstMoldDetail.setMainteCycleCode01("");
                }
                if (null != mstMold.getBlMaintenanceCyclePtn02()) {
                    // メンテサイクルコード02
                    mstMoldDetail.setMainteCycleCode02(
                            mstMold.getBlMaintenanceCyclePtn02().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    mstMoldDetail.setMainteCycleCode02("");
                }
                if (null != mstMold.getBlMaintenanceCyclePtn03()) {
                    // メンテサイクルコード03
                    mstMoldDetail.setMainteCycleCode03(
                            mstMold.getBlMaintenanceCyclePtn03().getTblMaintenanceCyclePtnPK().getCycleCode());
                } else {
                    mstMoldDetail.setMainteCycleCode03("");
                }
                // 4.2 Zhangying E

                mstMoldDetails.add(mstMoldDetail);
            }
        }
        mstMoldDetailList.setMstMoldDetail(mstMoldDetails);
        return mstMoldDetailList;

    }
    
    private List getSqlByPage(String moldId, String moldName, String mainAssetNo, String ownerCompanyName,
            String companyName, String locationName, String instllationSiteName, Integer moldType, Integer department,
            Date lastProductionDateFrom, Date lastProductionDateTo, Integer totalProducingTimeHourFrom,
            Integer totalProducingTimeHourTo, Integer totalShotCountFrom, Integer totalShotCountTo,
            Date lastMainteDateFrom, Date lastMainteDateTo, Integer afterMainteTotalProducingTimeHourFrom,
            Integer afterMainteTotalProducingTimeHourTo, Integer afterMainteTotalShotCountFrom,
            Integer afterMainteTotalShotCountTo, Date moldCreatedDateFrom, Date moldCreatedDateTo, Integer status,
            String sidx, String sord, int pageNumber, int pageSize, boolean isCount) {

        StringBuilder sql;

        if (isCount) {
            sql = new StringBuilder("SELECT count(t0.moldId) ");
        } else {
            sql = new StringBuilder("SELECT t0 ");
        }
        sql = sql.append(" FROM MstMold t0 " + "LEFT JOIN FETCH t0.mstCompanyByOwnerCompanyId mco "
                + "LEFT JOIN FETCH t0.mstCompanyByCompanyId mc  " + "LEFT JOIN FETCH t0.mstInstallationSite mi "
                + "LEFT JOIN FETCH t0.mstLocation ml " + "LEFT JOIN FETCH t0.blMaintenanceCyclePtn01 cycle01 "
                + "LEFT JOIN FETCH t0.blMaintenanceCyclePtn02 cycle02 " + "LEFT JOIN FETCH t0.blMaintenanceCyclePtn03 cycle03 "
                + " WHERE 1=1 ");

        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" AND t0.moldId like :moldId ");
        }
        if (moldName != null && !"".equals(moldName)) {
            sql = sql.append(" AND t0.moldName like :moldName ");
        }

        // 資産番号
        if (mainAssetNo != null && !"".equals(mainAssetNo)) {
            sql = sql.append(" AND t0.mainAssetNo LIKE :mainAssetNo ");
        }

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" and t0.mstCompanyByOwnerCompanyId.companyName like :ownerCompanyName ");
        }

        if (companyName != null && !"".equals(companyName)) {
            sql = sql.append(" and t0.mstCompanyByCompanyId.companyName like :companyName ");
        }

        if (locationName != null && !"".equals(locationName)) {
            sql = sql.append(" and t0.mstLocation.locationName like :locationName ");
        }

        if (instllationSiteName != null && !"".equals(instllationSiteName)) {
            sql = sql.append(" and t0.mstInstallationSite.installationSiteName like :instllationSiteName ");
        }

        if (moldType != null && 0 != moldType) {
            sql = sql.append(" and t0.moldType = :moldType ");
        }

        if (department != null && 0 != department) {
            sql = sql.append(" and t0.department = :department ");
        }

        if (moldCreatedDateFrom != null) {
            sql = sql.append(" and t0.moldCreatedDate >= :moldCreatedDateFrom ");
        }

        if (moldCreatedDateTo != null) {
            sql = sql.append(" and t0.moldCreatedDate <= :moldCreatedDateTo ");
        }

        if (lastProductionDateFrom != null) {
            sql = sql.append(" and t0.lastProductionDate >= :lastProductionDateFrom ");
        }

        if (lastProductionDateTo != null) {
            sql = sql.append(" and t0.lastProductionDate <= :lastProductionDateTo ");
        }

        if (totalProducingTimeHourFrom != null) {
            sql = sql.append(" and t0.totalProducingTimeHour >= :totalProducingTimeHourFrom ");
        }

        if (totalProducingTimeHourTo != null) {
            sql = sql.append(" and t0.totalProducingTimeHour <= :totalProducingTimeHourTo ");
        }

        if (totalShotCountFrom != null) {
            sql = sql.append(" and t0.totalShotCount >= :totalShotCountFrom ");
        }

        if (totalShotCountTo != null) {
            sql = sql.append(" and t0.totalShotCount <= :totalShotCountTo ");
        }

        if (lastMainteDateFrom != null) {
            sql = sql.append(" and t0.lastMainteDate >= :lastMainteDateFrom ");
        }

        if (lastMainteDateTo != null) {
            sql = sql.append(" and t0.lastMainteDate <= :lastMainteDateTo ");
        }

        if (afterMainteTotalProducingTimeHourFrom != null) {
            sql = sql.append(" and t0.afterMainteTotalProducingTimeHour >= :afterMainteTotalProducingTimeHourFrom ");
        }

        if (afterMainteTotalProducingTimeHourTo != null) {
            sql = sql.append(" and t0.afterMainteTotalProducingTimeHour <= :afterMainteTotalProducingTimeHourTo ");
        }

        if (afterMainteTotalShotCountFrom != null) {
            sql = sql.append(" and t0.afterMainteTotalShotCount >= :afterMainteTotalShotCountFrom ");
        }

        if (afterMainteTotalShotCountTo != null) {
            sql = sql.append(" and t0.afterMainteTotalShotCount <= :afterMainteTotalShotCountTo ");
        }

        if (status != null) {
            sql = sql.append(" and t0.status = :status ");
        }
        
        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                // 表示順は設備IDの昇順。
                sql.append(sortStr);

            } else {

                // // 表示順は金型IDの昇順
                sql.append(" order by t0.moldId ");

            }

        }

        Query query = entityManager.createQuery(sql.toString());

        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", "%" + moldId + "%");
        }

        if (moldName != null && !"".equals(moldName)) {
            query.setParameter("moldName", "%" + moldName + "%");
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

        if (moldType != null && 0 != moldType) {
            query.setParameter("moldType", moldType);
        }

        if (department != null && 0 != department) {
            query.setParameter("department", department);
        }

        if (moldCreatedDateFrom != null) {
            query.setParameter("moldCreatedDateFrom", moldCreatedDateFrom);
        }
        if (moldCreatedDateTo != null) {
            query.setParameter("moldCreatedDateTo", moldCreatedDateTo);
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
     *
     * @param langId
     * @return
     */
    public Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList(
                "row_number",
                "mold_id",
                "mold",
                "error",
                "error_detail",
                "msg_error_not_null",
                "msg_record_added",
                "msg_report_name_multiple_records_found",
                "msg_error_mold_machine_asset_relation_not_null",
                "mst_error_record_not_found",
                "msg_error_value_invalid",
                "msg_error_over_length",
                "db_process",
                "company_code",
                "location_code",
                "installation_site_code",
                "stocktake_result_mail_title",
                "stocktake_result_mail_body",
                "mold_inventory_result",
                "mold_inventory_date",
                "mold_confirm_method",
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
                "mold_name",
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
    public int batchInsert(List<TblMoldInventory> list, int type) {

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
    public int batchInsertMold(List<MstMold> list) {

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
     * @param mstMoldStoctake
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
    public void moldStocktakeResultPrepareMail(MstMoldStoctake mstMoldStoctake,
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
            moldStocktakeResultSendMailMassage(mstMoldStoctake,
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
     * @param mstMoldStoctake
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
    public void moldStocktakeResultSendMailMassage(MstMoldStoctake mstMoldStoctake,
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
        String tital = String.format(dictMap.get("stocktake_result_mail_title"), dictMap.get("mold"));
        int total = mstMoldStoctake.getMoldStocktake().getMolds().size();
        List<OutputCondition> outputCondition = mstMoldStoctake.getMoldStocktake().getOutputConditions();
        mailBody.append((String.format(dictMap.get("stocktake_result_mail_body"), dictMap.get("mold"))));
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
            String str = String.format(dictMap.get("stocktake_result_location_confirm_mail_body"), dictMap.get("mold"));
            mailBody.append(str);
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            mailBody.append(("    " + dictMap.get("mold_id") + "," + dictMap.get("mold_name")
                    + "," + dictMap.get("user_department") + "," + dictMap.get("company_name")
                    + "," + dictMap.get("location_name") + "," + dictMap.get("installation_site_name")));
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            for (int i = 0; i < unknownErrorList.size(); i++) {
                mailBody.append(("    " + unknownErrorList.get(i).getMoldId() + ","));
                mailBody.append((unknownErrorList.get(i).getMoldName() + ","));
                mailBody.append((unknownErrorList.get(i).getDepartmentName() + ","));
                mailBody.append((unknownErrorList.get(i).getCompanyName() + ","));
                mailBody.append((unknownErrorList.get(i).getLocationName() + ","));
                mailBody.append(unknownErrorList.get(i).getInstallationSiteName());
                mailBody.append(MailSender.MAIL_RETURN_CODE);
            }
            mailBody.append(" " + MailSender.MAIL_RETURN_CODE);
        }
        if (!departmentErrorList.isEmpty()) {

            String str = String.format(dictMap.get("stocktake_result_location_unknown_mail_body"), dictMap.get("mold"));
            mailBody.append(str);
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            mailBody.append(("    " + dictMap.get("mold_id") + "," + dictMap.get("mold_name")
                    + "," + dictMap.get("user_department") + "," + dictMap.get("company_name")
                    + "," + dictMap.get("location_name") + "," + dictMap.get("installation_site_name")));
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            for (int i = 0; i < departmentErrorList.size(); i++) {
                mailBody.append(("    " + departmentErrorList.get(i).getMoldId() + ","));
                mailBody.append((departmentErrorList.get(i).getMoldName() + ","));
                mailBody.append((departmentErrorList.get(i).getDepartmentName() + ","));
                mailBody.append((departmentErrorList.get(i).getCompanyName() + ","));
                mailBody.append((departmentErrorList.get(i).getLocationName() + ","));
                mailBody.append(departmentErrorList.get(i).getInstallationSiteName());
                mailBody.append(MailSender.MAIL_RETURN_CODE);

            }
            mailBody.append(" " + MailSender.MAIL_RETURN_CODE);
        }
        if (!addtionalList.isEmpty()) {
            String str = String.format(dictMap.get("stocktake_result_location_exclude_mail_body"), dictMap.get("mold"));
            mailBody.append(str);
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            mailBody.append(("    " + dictMap.get("mold_id") + "," + dictMap.get("qr_plate_info")));
            mailBody.append(MailSender.MAIL_RETURN_CODE);
            for (int i = 0; i < addtionalList.size(); i++) {
                mailBody.append(("    " + addtionalList.get(i).getMoldId() + ","));
                mailBody.append(addtionalList.get(i).getQrPlateInfo());
                mailBody.append(MailSender.MAIL_RETURN_CODE);
            }
            mailBody.append(" " + MailSender.MAIL_RETURN_CODE);
        }
        mailBody.append((dictMap.get("output_file_id") + "："));
        mailBody.append(FileUtil.getStr(mstMoldStoctake.getMoldStocktake().getOutputFileUuid()));
        try {
            mailSender.setMakePlainTextBody(true);
            mailSender.sendMailWithAttachment(userMailList, null, tital, mailBody.toString(), logFile);
        } catch (IOException ex) {
            Logger.getLogger(MstMoldService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(MstMoldService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
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
    public BasicResponse moldStocktakePicture(
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
            // 1 金型棚卸画像ファイルアップロードAPIは受け取った画像ファイルをサーバーに格納し
            StringBuffer path = new StringBuffer(kartePropertyService.getDocumentDirectory());
            path = path.append(FileUtil.SEPARATOR).append(CommonConstants.IMAGE);

            String uuid = IDGenerator.generate();
            // 2 fileUuid を金型棚卸結果テーブルに保持する。
            // その際、ネイティブアプリから送信された出力ファイルUUIDと画像ファイルユニークキーをレコード特定のキーとする。
            Query query = entityManager.createQuery("SELECT moldInventory FROM TblMoldInventory moldInventory WHERE moldInventory.outputFileUuid = :outputFileUuid AND moldInventory.imageFileKey = :imageFileKey");

            query.setParameter("outputFileUuid", outputFileUuid);
            query.setParameter("imageFileKey", imageFileKey);
            try {
                TblMoldInventory tblMoldInventory = (TblMoldInventory) query.getSingleResult();
                tblMoldInventory.setImgFilePath(uuid);
                tblMoldInventory.setUpdateDate(new Date());
                tblMoldInventory.setUpdateUserUuid(userUuid);
                entityManager.merge(tblMoldInventory);
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
     * @param loginUser
     * @return
     */
    public MstMoldStoctake getMoldStocktakeResult(SearchConditionList searchConditionList, LoginUser loginUser) {

        //検索画面で指定された会社名称、所在地名称、設置場所名称、所属を表示する。
        StringBuilder sql = new StringBuilder("SELECT m FROM MstMold m "
                + " LEFT JOIN FETCH m.mstCompanyByCompanyId mstCompany "
                + " LEFT JOIN FETCH m.mstInstallationSite mstInstallationSite "
                + " LEFT JOIN FETCH m.mstCompanyByOwnerCompanyId mstCompanyByOwnerCompany "
                + " LEFT JOIN FETCH m.mstLocation mstLocation "
                + " LEFT JOIN FETCH m.tblMoldInventory tblMoldInventory "
                + " WHERE 1=1 ");

        //金型マスタより指定された会社、所在地、設置場所、所属にあてはまるレコードを検索し、
        if (searchConditionList != null) {
            if (searchConditionList.getSearchConditions() != null && searchConditionList.getSearchConditions().size() > 0) {
                int searchConditionCount = searchConditionList.getSearchConditions().size();
                sql.append(" AND ( 1=1 ");
                for (int i = 0; i < searchConditionCount; i++) {
                    int j = i + 1;

                    if (i > 0 && j <= searchConditionCount) {
                        sql.append(" OR ( 1=1  ");
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
                        sql.append(" AND m.inventoryStatus =  :inventoryStatus").append(i);//金型の棚卸ステータスが未実施であるもの
                    }
                    if (i > 0 && j <= searchConditionCount) {
                        sql.append(" ) ");
                    }
                }
                sql.append(" ) ");
            }
        }

        // 金型IDの昇順で一覧表示する。
        sql.append(" ORDER BY m.moldId ");
        Query query = entityManager.createQuery(sql.toString());

        List<OutputCondition> outputConditions = new ArrayList<>();

        if (searchConditionList != null) {
            if (searchConditionList.getSearchConditions() != null && searchConditionList.getSearchConditions().size() > 0) {

                int searchConditionCount = searchConditionList.getSearchConditions().size();
                OutputCondition outputCondition;
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
                        query.setParameter("inventoryStatus" + i, 0);//金型の棚卸ステータスが未実施であるもの
                    }
                }
            }
        }
        List list = query.getResultList();

        List<Mold> molds = new ArrayList<>();
        if (!list.isEmpty()) {
            String choiceKey = "mst_user.department";
            Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{choiceKey});
            for (int i = 0; i < list.size(); i++) {
                //mstMoldStoctake 
                MstMold mstMold = (MstMold) list.get(i);
                Mold mold = new Mold();
                mold.setMoldId(mstMold.getMoldId());//金型ID
                mold.setMoldName(mstMold.getMoldName());//金型名称

                MstCompany mstCompany = mstMold.getMstCompanyByCompanyId();
                if (mstCompany != null) {
                    mold.setCompanyCode(mstCompany.getCompanyCode());//会社コード
                    mold.setCompanyName(mstCompany.getCompanyName());//会社名称
                } else {
                    mold.setCompanyCode("");//会社コード
                    mold.setCompanyName("");//会社名称
                }

                MstLocation mstLocation = mstMold.getMstLocation();
                if (mstLocation != null) {
                    mold.setLocationCode(mstLocation.getLocationCode());//所在地コード
                    mold.setLocationName(mstLocation.getLocationName());//所在地名称
                } else {
                    mold.setLocationCode("");//所在地コード
                    mold.setLocationName("");//所在地名称
                }

                MstInstallationSite mstInstallationSite = mstMold.getMstInstallationSite();
                if (mstInstallationSite != null) {
                    mold.setInstallationSiteCode(mstInstallationSite.getInstallationSiteCode());//設置場所コード
                    mold.setInstallationSiteName(mstInstallationSite.getInstallationSiteName());//設置場所名称
                } else {
                    mold.setInstallationSiteCode("");//設置場所コード
                    mold.setInstallationSiteName("");//設置場所名称
                }

                mold.setDepartment(mstMold.getDepartment());//所属番号
                mold.setDepartmentName(FileUtil.getStr(map.get(choiceKey + String.valueOf(mstMold.getDepartment()))));//所属名称
                mold.setStocktakeStatus(mstMold.getInventoryStatus());
                mold.setNotes("");//備考
                mold.setImageFileKey("");//画像ファイルユニークキー *2
                mold.setQrPlateInfo("");
                
                if (mold.getStocktakeStatus() == 1) {//棚卸ステータスが1(実施済み)の場合
                    TblMoldInventory tblMoldInventory = mstMold.getTblMoldInventory();
                    if (tblMoldInventory != null) {
                        mold.setStocktakeResult(tblMoldInventory.getInventoryResult());//棚卸結果
                        mold.setStocktakeDatetime(tblMoldInventory.getInventoryDate());//棚卸日時
                        mold.setStocktakeMethod(tblMoldInventory.getMoldConfirmMethod());//確認方法
                        mold.setNotes(tblMoldInventory.getRemarks());//備考
                        mold.setChangeDepartment(tblMoldInventory.getDepartmentChange());//部署変更
                        mold.setBarcodeReprint(tblMoldInventory.getBarcodeReprint());//資産シール再発行要否
                        mold.setAssetDamaged(tblMoldInventory.getAssetDamaged());//故障
                        mold.setNotInUse(tblMoldInventory.getNotInUse());//遊休
                    }
                }
                
                molds.add(mold);
            }
        }
        MoldStocktake moldStoctake = new MoldStocktake();
        moldStoctake.setMolds(molds);
        moldStoctake.setOutputConditions(outputConditions);//出力条件ルート
        moldStoctake.setApiBaseUrl(kartePropertyService.getBaseUrl() + "/ws/karte/api/");
        moldStoctake.setOutputFileUuid(IDGenerator.generate());//出力ファイルUUID。このファイルのユニークキー
        MstUser mstUser = entityManager.find(MstUser.class, loginUser.getUserid());
        if (mstUser != null) {
            moldStoctake.setOutputPersonName(mstUser.getUserName());        //ファイルを出力した人の名称
        } else {
            moldStoctake.setOutputPersonName("");//ファイルを出力した人の名称
        }
        moldStoctake.setOutputPersonUuid(loginUser.getUserUuid());//ファイルを出力した人のユニークキー
        moldStoctake.setOutputPersonUserId(loginUser.getUserid());   //ファイルを出力した人のユーザーID

        try {
            SimpleDateFormat format = new SimpleDateFormat(DateFormat.DATETIME_EXPANSION_FORMAT);
            String dateString = format.format(new Date());
            Date date = format.parse(dateString);
            moldStoctake.setOutputDate(date);//ファイル出力日時
        } catch (ParseException ex) {
            Logger.getLogger(MstMoldService.class.getName()).log(Level.SEVERE, null, ex);
        }
        MstMoldStoctake mstMoldStoctake = new MstMoldStoctake();
        mstMoldStoctake.setMoldStocktake(moldStoctake);
        try {
            FileUtil fileUtil = new FileUtil();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat(DateFormat.DATETIME_EXPANSION_FORMAT);
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();
            String fileName = "karte-mold-stocktake-" + moldStoctake.getOutputFileUuid();
            String outJsonPath = FileUtil.outJsonFile(kartePropertyService, fileName);
            String str = gson.toJson(mstMoldStoctake);
            fileUtil.writeInfoToFile(outJsonPath, str);

            TblCsvExport tblCsvExport = new TblCsvExport();
            tblCsvExport.setFileUuid(moldStoctake.getOutputFileUuid());
            tblCsvExport.setExportTable("mst_mold");
            tblCsvExport.setExportDate(new Date());
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId("15000");
            tblCsvExport.setFunctionId(mstFunction);
            tblCsvExport.setExportUserUuid(loginUser.getUserUuid());

            tblCsvExport.setClientFileName(fileName + CommonConstants.EXT_JSON);
            tblCsvExportService.createTblCsvExport(tblCsvExport);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mstMoldStoctake;
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
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("mold_id"), "", dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_wrong_csv_layout")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    int index = 0;
                    //金型ID
                    String moldId = readList.get(index).trim();
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

                    if (StringUtils.isEmpty(moldId)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("mold_id"), moldId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
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

                    //金型レコードを一意にするキー
                    MstMold mstMold = entityManager.find(MstMold.class, moldId);
                    if (mstMold == null) {
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("mold_id"), moldId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    int afterMainteTotalShotCount = FileUtil.getIntegerValue(mstMold.getAfterMainteTotalShotCount());// ショット数                    
                    int totalShotCount = FileUtil.getIntegerValue(mstMold.getTotalShotCount());// 累計ショット数

                    BigDecimal afterMainteTotalProducingTimeHour = BigDecimal.ZERO; //メンテナンス後生産時間
                    if (mstMold.getAfterMainteTotalProducingTimeHour() != null) {
                        afterMainteTotalProducingTimeHour = mstMold.getAfterMainteTotalProducingTimeHour();
                    }

                    BigDecimal totalProducingTimeHour = BigDecimal.ZERO; //累計生産時間
                    if (mstMold.getTotalProducingTimeHour() != null) {
                        totalProducingTimeHour = mstMold.getTotalProducingTimeHour();
                    }

                    if (intCorrectionFlg == 1) { // 立っている場合は、生産日のチェックを行ずに必ず足しこみをする。最終生産日の更新はしない

                        mstMold.setAfterMainteTotalProducingTimeHour(afterMainteTotalProducingTimeHour.add(intProductionTimeMinutes));
                        mstMold.setTotalProducingTimeHour(totalProducingTimeHour.add(intProductionTimeMinutes));

                    } else {
                        if (mstMold.getLastProductionDate() != null) {
                            if (mstMold.getLastProductionDate().compareTo(dateProductionDate) == -1) {
                                // 金型マスタの累計生産時間、メンテナンス後生産時間にこの値を加算
                                mstMold.setAfterMainteTotalProducingTimeHour(afterMainteTotalProducingTimeHour.add(intProductionTimeMinutes));
                                mstMold.setTotalProducingTimeHour(totalProducingTimeHour.add(intProductionTimeMinutes));
                                mstMold.setLastProductionDate(dateProductionDate);
                            } else {
                                // 金型マスタの最終生産日がこの値と同じかこの値よりすでに大きいときは処理をスキップし、エラーとしてログに出力する。
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("mold_id"), moldId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_production_date")));
                                failedCount = failedCount + 1;
                                continue;
                            }
                        } else {
                            mstMold.setAfterMainteTotalProducingTimeHour(afterMainteTotalProducingTimeHour.add(intProductionTimeMinutes));
                            mstMold.setTotalProducingTimeHour(totalProducingTimeHour.add(intProductionTimeMinutes));
                            mstMold.setLastProductionDate(dateProductionDate);
                        }
                    }
                    mstMold.setAfterMainteTotalShotCount(afterMainteTotalShotCount + intShotCount);
                    mstMold.setTotalShotCount(totalShotCount + intShotCount);
                    mstMold.setUpdateDate(new Date());
                    mstMold.setUpdateUserUuid(userUuid);
                    entityManager.merge(mstMold);
                    updatedCount = updatedCount + 1;
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), rowIndex, dictMap.get("mold_id"), moldId, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_data_modified")));
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
            tblCsvImport.setImportTable(CommonConstants.TBL_MST_MOLD);
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(uuid);
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_MOLD);
            tblCsvImport.setFunctionId(mstFunction);

            tblCsvImport.setRecordCount(Integer.valueOf(String.valueOf(updatedCount + failedCount)));
            tblCsvImport.setSuceededCount(Integer.valueOf(String.valueOf(updatedCount)));
            tblCsvImport.setAddedCount(0);
            tblCsvImport.setUpdatedCount(Integer.valueOf(String.valueOf(updatedCount)));
            tblCsvImport.setDeletedCount(0);
            tblCsvImport.setFailedCount(Integer.valueOf(String.valueOf(failedCount)));
            tblCsvImport.setLogFileUuid(logFileUuid);
            tblCsvImport.setLogFileName(FileUtil.getLogFileName(String.format(dictMap.get("mold_or_machine_production_result_add"), dictMap.get("mold"))));

            tblCsvImportService.createCsvImpor(tblCsvImport);

        } catch (Exception e) {
            Logger.getLogger(MstMoldService.class.getName()).log(Level.SEVERE, null, e);
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
            headList.add(csvHeader.get("mold_id"));
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
            tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD);
            tblCsvExport.setExportDate(new Date());
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_MOLD);
            tblCsvExport.setFunctionId(mstFunction);
            tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
            tblCsvExport.setClientFileName(String.format(csvHeader.get("mold_or_machine_production_result_add"), csvHeader.get("mold")) + CommonConstants.EXT_CSV);
            tblCsvExportService.createTblCsvExport(tblCsvExport);

            //csvファイルのUUIDをリターンする
            file.setFileUuid(uuid);
        } catch (Exception e) {
            Logger.getLogger(MstMoldService.class.getName()).log(Level.SEVERE, null, e);
            file.setError(true);
            file.setErrorCode(ErrorMessages.E201_APPLICATION);
            file.setErrorMessage(e.getMessage());
            return file;
        }
        return file;

    }
}
