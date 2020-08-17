package com.kmcj.karte.resources.machine.inventory;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetailService;
import com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetail;
import com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetailService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistory;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistoryService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.TimezoneConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import com.kmcj.karte.util.Pager;
import java.util.HashMap;

/**
 *
 * @author admin
 */
@Dependent
@Transactional
public class TblMachineInventoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private TblMachineLocationHistoryService tblMachineLocationHistoryService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private TblInventoryDetailService tblInventoryDetailService;

    @Inject
    private TblInventoryRequestDetailService tblInventoryRequestDetailService;

    private final static Map<String, String> orderKey;
    static {
        orderKey = new HashMap<>();
        orderKey.put("machineId", " ORDER BY m.machineId ");// 金型ＩＤ
        orderKey.put("machineName", " ORDER BY m.machineName ");// 金型名称
        orderKey.put("ownerCompanyName", " ORDER BY oc.companyName ");// 所有会社名称
        orderKey.put("companyName", " ORDER BY c.companyName ");// 会社名称
        orderKey.put("locationName", " ORDER BY mstloc.locationName ");// 所在地名称
        orderKey.put("instllationSiteName", " ORDER BY mstins.installationSiteName ");// 設置場所名称   
        orderKey.put("inventoryDateStr", " ORDER BY mi.inventoryDate ");// 棚卸日
        orderKey.put("status", " ORDER BY m.status ");// ステータス
        orderKey.put("department", " ORDER BY mi.department ");// 所属
        orderKey.put("departmentChange", " ORDER BY mi.departmentChange ");// 所属
        orderKey.put("barcodeReprint", " ORDER BY mi.barcodeReprint ");
        orderKey.put("assetDamaged", " ORDER BY mi.assetDamaged ");
        orderKey.put("notInUse", " ORDER BY mi.notInUse ");
        orderKey.put("personName", " ORDER BY mi.personName ");
        orderKey.put("machineInstalledDateStr", " ORDER BY m.installedDate ");// 設置日
        
    }
    
    public TblMachineInventoryList getMachinesByPage(String machineId, String machineName, String ownerCompanyName, String companyName, String locationName, String instllationSiteName, 
            String latestInventoryDateStart, String latestInventoryDateEnd, List<Integer> status, String depart, LoginUser loginUser, String sidx, String sord, int pageNumber, int pageSize,
            int stocktakeIncomplete, boolean isPage) {
        //List<TblMoldInventorys> tblMoldInventorysList = new ArrayList();
        TblMachineInventoryVo paraTblMachineInventorys = new TblMachineInventoryVo(machineId, machineName, ownerCompanyName, companyName, locationName, instllationSiteName, 
                latestInventoryDateStart, latestInventoryDateEnd, null);
        paraTblMachineInventorys.setDepartment(depart);
        List<TblMachineInventoryVo> tblMachineInventorysList = new ArrayList();
        TblMachineInventoryList tblMachineInvList = new TblMachineInventoryList();
        if(isPage){
            Query query = getTblMachineInvQuery(paraTblMachineInventorys, "query", loginUser, sidx, sord, pageNumber, pageSize, stocktakeIncomplete, true, status);
            
            Pager pager = new Pager();
            tblMachineInvList.setPageNumber(pageNumber);
            long counts = (long) query.getResultList().size();
            tblMachineInvList.setCount(counts);
            tblMachineInvList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        }
        Query query = getTblMachineInvQuery(paraTblMachineInventorys, "query", loginUser, sidx, sord, pageNumber, pageSize, stocktakeIncomplete, false, status);
        Pager pager = new Pager();
        query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
        Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_user.department"});
        List list = query.getResultList();
        FileUtil fu = new FileUtil();
//        Query query = getTblMoldInventoryTypedQuery(paraMoldInventorys, "count", loginUser);
        for (int i = 0; i < list.size(); i++) {
            TblMachineInventoryVo aTblMachineInventoryVo = new TblMachineInventoryVo();
            Object[] objs = (Object[]) list.get(i);
            String strMachineUuid = String.valueOf(objs[0]);
            String strMachineId = String.valueOf(objs[1]);
            String strMachineName = String.valueOf(objs[2]);
            String strOwnerCompanyId = null == objs[3] ? "" : String.valueOf(objs[3]);
            String strInstalledDate = "";
            if (objs[4] != null && !"".equals(objs[4])) {
                strInstalledDate = fu.getDateFormatForStr(objs[4]);
            }
            String strCompanyId = null == objs[5] ? "" : String.valueOf(objs[5]);
            String strCompanyName = null == objs[6] ? "" : String.valueOf(objs[6]);
            String strLocationId = null == objs[7] ? "" : String.valueOf(objs[7]);
            String strLocationName = null == objs[8] ? "" : String.valueOf(objs[8]);
            String strInstllationSiteId = null == objs[9] ? "" : String.valueOf(objs[9]);
            String strInstllationSiteName = null == objs[10] ? "" : String.valueOf(objs[10]);
            String strInventoryDate = "";
            if (objs[12] != null && !"".equals(objs[12])) {
                strInventoryDate = fu.getDateTimeFormatForStr(objs[12]);
            }
            String strOwnerCompanyName = null == objs[13] ? "" : String.valueOf(objs[13]);
            String strStatusName = String.valueOf(objs[14]);

            String strDepartmentChange = null == objs[15] ? "" : String.valueOf(objs[15]);//null→""
            String strDepartment = null == objs[16] ? "" : String.valueOf(objs[16]);
            String strBarcodeReprint = null == objs[17] ? "" : String.valueOf(objs[17]);
            String strAssetDamaged = null == objs[18] ? "" : String.valueOf(objs[18]);
            String strNotInUse = null == objs[19] ? "" : String.valueOf(objs[19]);
            String personName = null == objs[20] ? "" : String.valueOf(objs[20]);

            aTblMachineInventoryVo.setMachineUuid(strMachineUuid);
            aTblMachineInventoryVo.setMachineId(strMachineId);
            aTblMachineInventoryVo.setMachineName(strMachineName);
            aTblMachineInventoryVo.setMachineInstalledDateStr(strInstalledDate);
//           aTblMachineInventorys.setStatus(strStatus);
            aTblMachineInventoryVo.setStatus(strStatusName);
            aTblMachineInventoryVo.setOwnerCompanyId(strOwnerCompanyId);
            aTblMachineInventoryVo.setOwnerCompanyName(strOwnerCompanyName);
            aTblMachineInventoryVo.setInventoryDateStr(strInventoryDate);
            aTblMachineInventoryVo.setCompanyId(strCompanyId);
            aTblMachineInventoryVo.setCompanyName(strCompanyName);
            aTblMachineInventoryVo.setLocationId(strLocationId);
            aTblMachineInventoryVo.setLocationName(strLocationName);
            aTblMachineInventoryVo.setInstllationSiteId(strInstllationSiteId);
            aTblMachineInventoryVo.setInstllationSiteName(strInstllationSiteName);

            aTblMachineInventoryVo.setDepartmentChange(strDepartmentChange);

            aTblMachineInventoryVo.setDepartment(map.get("mst_user.department" + strDepartment));

            aTblMachineInventoryVo.setBarcodeReprint(strBarcodeReprint);

            aTblMachineInventoryVo.setAssetDamaged(strAssetDamaged);

            aTblMachineInventoryVo.setNotInUse(strNotInUse);
            aTblMachineInventoryVo.setPersonName(personName);

            MstMachine mstMachine = entityManager.find(MstMachine.class, strMachineId);
            if (mstMachine.getInventoryStatus() == 0) {
                aTblMachineInventoryVo.setDepartmentChange("");
                aTblMachineInventoryVo.setDepartment("");
                aTblMachineInventoryVo.setBarcodeReprint("");
                aTblMachineInventoryVo.setAssetDamaged("");
                aTblMachineInventoryVo.setNotInUse("");
                aTblMachineInventoryVo.setStatus(""); 
                aTblMachineInventoryVo.setInventoryDateStr("");
                aTblMachineInventoryVo.setPersonName("");
                aTblMachineInventoryVo.setPersonUuid("");
            }
            tblMachineInventorysList.add(aTblMachineInventoryVo);
        }
        tblMachineInvList.setTblMachineInventoryVos(tblMachineInventorysList);
        query.setMaxResults(pageSize);
        return tblMachineInvList;
    }
    
    public Query getTblMachineInvQuery(TblMachineInventoryVo paraMachineInventoryVo, String type, LoginUser loginUser, String sidx, String sord, int pageNumber, int pageSize, 
            int stocktakeIncomplete, boolean isPage, List<Integer> status) {
        StringBuilder sql = new StringBuilder();
        if ("query".equals(type) || "detail".equals(type)) {
            sql.append(" SELECT m.uuid,m.machineId,m.machineName,m.ownerCompanyId,m.installedDate,m.companyId,");
            sql.append(" c.companyName,m.locationId,mstloc.locationName,m.instllationSiteId,mstins.installationSiteName,");
            sql.append(" m.status,mi.inventoryDate,oc.companyName AS ownerCompanyName,cc.choice AS statusName");

            //部署変更(0:無,1:有)
            //部署
            //資産シール(0:無,1:有)
            //故障(0:無,1:有)
            //遊休(0:F,1:T)
            sql.append(" ,mi.departmentChange, mi.department, mi.barcodeReprint, mi.assetDamaged, mi.notInUse,mi.personName");

            sql.append(" FROM MstMachine m  ");//設備マスタ
            sql.append(" INNER JOIN TblMachineInventory mi ");//設備棚卸テーブル
            sql.append(" ON m.uuid = mi.machineUuid ");
            sql.append(" AND m.latestInventoryId = mi.id ");//設備マスタ.最新棚卸ID
            sql.append(" LEFT OUTER JOIN MstLocation mstloc ");
            sql.append(" ON m.locationId = mstloc.id ");
            sql.append(" LEFT OUTER JOIN MstInstallationSite mstins ");
            sql.append(" ON m.instllationSiteId = mstins.id ");
            sql.append(" LEFT OUTER JOIN MstCompany c ");//会社マスタ
            sql.append(" ON m.companyId = c.id ");
            sql.append(" LEFT OUTER JOIN MstCompany oc ");//会社マスタ
            sql.append(" ON m.ownerCompanyId = oc.id ");
            sql.append(" LEFT OUTER JOIN MstChoice cc ");
            sql.append(" ON m.status = cc.mstChoicePK.seq ");
            sql.append(" AND cc.mstChoicePK.category = 'mst_machine.status' ");
            sql.append(" AND cc.mstChoicePK.langId = '".concat(loginUser.getLangId()).concat("'"));
            sql.append(" WHERE 1 = 1 ");
        } else if ("count".equals(type)) {
            sql.append(" SELECT count(m.uuid) ");
            sql.append(" FROM MstMachine m  ");//設備マスタ
            sql.append(" INNER JOIN TblMachineInventory mi ");//設備棚卸テーブル
            sql.append(" ON m.uuid = mi.machineUuid ");
            sql.append(" AND m.latestInventoryId = mi.id ");//設備マスタ.最新棚卸ID
            sql.append(" LEFT OUTER JOIN MstCompany c ");//会社マスタ
            sql.append(" ON m.companyId = c.id ");
            sql.append(" LEFT OUTER JOIN MstCompany oc ");//会社マスタ
            sql.append(" ON m.ownerCompanyId = oc.id ");
            sql.append(" LEFT OUTER JOIN MstChoice cc ");
            sql.append(" ON m.status = cc.mstChoicePK.seq ");
            sql.append(" AND cc.mstChoicePK.category = 'mst_machine.status' ");
            sql.append(" AND cc.mstChoicePK.langId = '".concat(loginUser.getLangId()).concat("'"));
            sql.append(" WHERE 1 = 1 ");
        }

        if (null != paraMachineInventoryVo.getMachineId() && !paraMachineInventoryVo.getMachineId().trim().equals("")) {
            sql.append(" and m.machineId like :machineId ");
        }
        if (null != paraMachineInventoryVo.getMachineName() && !paraMachineInventoryVo.getMachineName().trim().equals("")) {
            sql.append(" and m.machineName like :machineName ");
        }
        if (null != paraMachineInventoryVo.getOwnerCompanyName() && !paraMachineInventoryVo.getOwnerCompanyName().trim().equals("")) {
            sql.append(" and oc.companyName like :ownerCompanyName ");
        }
        if (null != paraMachineInventoryVo.getCompanyName() && !paraMachineInventoryVo.getCompanyName().trim().equals("")) {
            sql.append(" and c.companyName like :companyName ");
        }
        if (null != paraMachineInventoryVo.getLocationName() && !paraMachineInventoryVo.getLocationName().trim().equals("")) {
            sql.append(" and mstloc.locationName like :locationName ");
        }
        if (null != paraMachineInventoryVo.getInstllationSiteName() && !paraMachineInventoryVo.getInstllationSiteName().trim().equals("")) {
            sql.append(" and mstins.installationSiteName like :instllationSiteName ");
        }
        if (null != paraMachineInventoryVo.getLatestInventoryDateStartStr() && !paraMachineInventoryVo.getLatestInventoryDateStartStr().trim().equals("")) {
            sql.append(" and mi.inventoryDate >= :inventoryDateStart ");

        }

        if (null != paraMachineInventoryVo.getLatestInventoryDateEndStr() && !paraMachineInventoryVo.getLatestInventoryDateEndStr().trim().equals("")) {
            sql.append(" and mi.inventoryDate <= :inventoryDateEnd  ");

        }
        //最新棚卸日時の検索条件が入力されたとき、検索対象外
        if (StringUtils.isNotEmpty(paraMachineInventoryVo.getLatestInventoryDateStartStr())
                || StringUtils.isNotEmpty(paraMachineInventoryVo.getLatestInventoryDateEndStr())) {
            sql.append(" and m.inventoryStatus <> 0 ");
        }
        
        if(stocktakeIncomplete == 1){
            sql.append(" and m.inventoryStatus = 0 ");
        }

        if (null != status && !status.isEmpty()) {
            sql.append(" and m.status in :status ");
        }
        
        if (null != paraMachineInventoryVo.getPersonName()&& !paraMachineInventoryVo.getPersonName().trim().equals("")) {
            sql.append(" and mi.personName like :personName ");
        }
        
        if (null != paraMachineInventoryVo.getDepartment() && !paraMachineInventoryVo.getDepartment().isEmpty()) {
            sql.append(" and m.department = :department");
        }
        
        if (!isPage) {
            if (StringUtils.isNotEmpty(sidx)) {
                String sortStr = orderKey.get(sidx) + " " + sord;
                // 表示順は設備IDの昇順。
                sql.append(sortStr);
            } else {
                sql.append("  order by m.machineId ");
            }
        }
        Query query = entityManager.createQuery(sql.toString());

        if (null != paraMachineInventoryVo.getMachineId() && !paraMachineInventoryVo.getMachineId().trim().equals("")) {
            query.setParameter("machineId", "%" + paraMachineInventoryVo.getMachineId().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getMachineName() && !paraMachineInventoryVo.getMachineName().trim().equals("")) {
            query.setParameter("machineName", "%" + paraMachineInventoryVo.getMachineName().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getOwnerCompanyName() && !paraMachineInventoryVo.getOwnerCompanyName().trim().equals("")) {
            query.setParameter("ownerCompanyName", "%" + paraMachineInventoryVo.getOwnerCompanyName().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getCompanyName() && !paraMachineInventoryVo.getCompanyName().trim().equals("")) {
            query.setParameter("companyName", "%" + paraMachineInventoryVo.getCompanyName().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getLocationName() && !paraMachineInventoryVo.getLocationName().trim().equals("")) {
            query.setParameter("locationName", "%" + paraMachineInventoryVo.getLocationName().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getInstllationSiteName() && !paraMachineInventoryVo.getInstllationSiteName().trim().equals("")) {
            query.setParameter("instllationSiteName", "%" + paraMachineInventoryVo.getInstllationSiteName().trim() + "%");
        }
        FileUtil fu = new FileUtil();
        if (null != paraMachineInventoryVo.getLatestInventoryDateStartStr() && !paraMachineInventoryVo.getLatestInventoryDateStartStr().trim().equals("")) {
            query.setParameter("inventoryDateStart", fu.getDateTimeParseForDate(paraMachineInventoryVo.getLatestInventoryDateStartStr() + CommonConstants.SYS_MIN_TIME));
        }

        if (null != paraMachineInventoryVo.getLatestInventoryDateEndStr() && !paraMachineInventoryVo.getLatestInventoryDateEndStr().trim().equals("")) {
            query.setParameter("inventoryDateEnd", fu.getDateTimeParseForDate(paraMachineInventoryVo.getLatestInventoryDateEndStr() + CommonConstants.SYS_MAX_TIME));
        }
        
        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }
        
        if (null != paraMachineInventoryVo.getPersonName() && !paraMachineInventoryVo.getPersonName().trim().equals("")) {
            query.setParameter("personName", "%" + paraMachineInventoryVo.getPersonName().trim() + "%");
        }
        
        if (null != paraMachineInventoryVo.getDepartment() && !paraMachineInventoryVo.getDepartment().isEmpty()) {
            query.setParameter("department", Integer.parseInt(paraMachineInventoryVo.getDepartment()));
        }
        
        if (!isPage) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        return query;
    }
    
    /**
     * 設備棚卸マスタ count
     *
     * @param paraMachineInventorys
     * @param loginUser
     * @param stocktakeIncomplete
     * @return
     */
    public CountResponse getTblMachineInventoryCount(TblMachineInventoryVo paraMachineInventorys, LoginUser loginUser, int stocktakeIncomplete) {
        Query query = getTblMachineInventoryTypedQuery(paraMachineInventorys, "count", loginUser, stocktakeIncomplete);

        Long count = (Long) query.getSingleResult();

        CountResponse res = new CountResponse();
        res.setCount(count);
        return res;
    }

    /**
     * 設備棚卸マスタ 複数取得
     *
     * @param paraTblMachineInventorys
     * @param loginUser
     * @param stocktakeIncomplete
     * @return
     */
    public List<TblMachineInventoryVo> getMachineInventories(TblMachineInventoryVo paraTblMachineInventorys, LoginUser loginUser, int stocktakeIncomplete) {
        List<TblMachineInventoryVo> tblMachineInventorysList = new ArrayList();
        Query query = getTblMachineInventoryTypedQuery(paraTblMachineInventorys, "query", loginUser, stocktakeIncomplete);
        Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_user.department"});
        List list = query.getResultList();
        FileUtil fu = new FileUtil();
        /*Detail*/
        for (int i = 0; i < list.size(); i++) {
            TblMachineInventoryVo aTblMachineInventoryVo = new TblMachineInventoryVo();
            Object[] objs = (Object[]) list.get(i);
            String strMachineUuid = String.valueOf(objs[0]);
            String strMachineId = String.valueOf(objs[1]);
            String strMachineName = String.valueOf(objs[2]);
            String strOwnerCompanyId = null == objs[3] ? "" : String.valueOf(objs[3]);
            String strInstalledDate = "";
            if (objs[4] != null && !"".equals(objs[4])) {
                strInstalledDate = fu.getDateFormatForStr(objs[4]);
            }
            String strCompanyId = null == objs[5] ? "" : String.valueOf(objs[5]);
            String strCompanyName = null == objs[6] ? "" : String.valueOf(objs[6]);
            String strLocationId = null == objs[7] ? "" : String.valueOf(objs[7]);
            String strLocationName = null == objs[8] ? "" : String.valueOf(objs[8]);
            String strInstllationSiteId = null == objs[9] ? "" : String.valueOf(objs[9]);
            String strInstllationSiteName = null == objs[10] ? "" : String.valueOf(objs[10]);
            String strInventoryDate = "";
            if (objs[12] != null && !"".equals(objs[12])) {
                strInventoryDate = fu.getDateTimeFormatForStr(objs[12]);
            }
            String strOwnerCompanyName = null == objs[13] ? "" : String.valueOf(objs[13]);
            String strStatusName = String.valueOf(objs[14]);

            String strDepartmentChange = null == objs[15] ? "" : String.valueOf(objs[15]);//null→""
            String strDepartment = null == objs[16] ? "" : String.valueOf(objs[16]);
            String strBarcodeReprint = null == objs[17] ? "" : String.valueOf(objs[17]);
            String strAssetDamaged = null == objs[18] ? "" : String.valueOf(objs[18]);
            String strNotInUse = null == objs[19] ? "" : String.valueOf(objs[19]);
            String personName = null == objs[20] ? "" : String.valueOf(objs[20]);

            aTblMachineInventoryVo.setMachineUuid(strMachineUuid);
            aTblMachineInventoryVo.setMachineId(strMachineId);
            aTblMachineInventoryVo.setMachineName(strMachineName);
            aTblMachineInventoryVo.setMachineInstalledDateStr(strInstalledDate);
//           aTblMachineInventorys.setStatus(strStatus);
            aTblMachineInventoryVo.setStatus(strStatusName);
            aTblMachineInventoryVo.setOwnerCompanyId(strOwnerCompanyId);
            aTblMachineInventoryVo.setOwnerCompanyName(strOwnerCompanyName);
            aTblMachineInventoryVo.setInventoryDateStr(strInventoryDate);
            aTblMachineInventoryVo.setCompanyId(strCompanyId);
            aTblMachineInventoryVo.setCompanyName(strCompanyName);
            aTblMachineInventoryVo.setLocationId(strLocationId);
            aTblMachineInventoryVo.setLocationName(strLocationName);
            aTblMachineInventoryVo.setInstllationSiteId(strInstllationSiteId);
            aTblMachineInventoryVo.setInstllationSiteName(strInstllationSiteName);

            aTblMachineInventoryVo.setDepartmentChange(strDepartmentChange);

            aTblMachineInventoryVo.setDepartment(map.get("mst_user.department" + strDepartment));

            aTblMachineInventoryVo.setBarcodeReprint(strBarcodeReprint);

            aTblMachineInventoryVo.setAssetDamaged(strAssetDamaged);

            aTblMachineInventoryVo.setNotInUse(strNotInUse);
            
            aTblMachineInventoryVo.setPersonName(personName);

            MstMachine mstMachine = entityManager.find(MstMachine.class, strMachineId);
            if (mstMachine.getInventoryStatus() == 0) {
                aTblMachineInventoryVo.setDepartmentChange("");
                aTblMachineInventoryVo.setDepartment("");
                aTblMachineInventoryVo.setBarcodeReprint("");
                aTblMachineInventoryVo.setAssetDamaged("");
                aTblMachineInventoryVo.setNotInUse("");
                aTblMachineInventoryVo.setStatus(""); 
                aTblMachineInventoryVo.setInventoryDateStr("");
                aTblMachineInventoryVo.setPersonName("");
            }
            
            tblMachineInventorysList.add(aTblMachineInventoryVo);           
        }
        return tblMachineInventorysList;
    }

    /**
     * 設備棚卸履歴 画面レイアウト 設備マスタ、設備棚卸テーブルよりデータを取得し
     *
     * @param machineId
     * @param loginUser
     * @return
     */
    public TblMachineInventoryVo getMachineInventoriesHistories(String machineId, LoginUser loginUser) {
        FileUtil fu = new FileUtil();
        TblMachineInventoryVo resMachineInventorys = new TblMachineInventoryVo();
        List<TblMachineInventoryVo> machineInventorys = new ArrayList<>();
        Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_user.department"});
        List<MstMachine> list = entityManager.createNamedQuery("MstMachine.findTblMachineInventoryByid")
                .setParameter("machineId", machineId)
                .getResultList();
        if (null != list && !list.isEmpty()) {
            MstMachine machine = list.get(0);

            List<TblMachineInventory> listMi = entityManager.createQuery("SELECT i from TblMachineInventory i where i.machineUuid = :machineUuid ORDER BY i.inventoryDate ASC ")
                    .setParameter("machineUuid", machine.getUuid())
                    .getResultList();
            if (null != listMi && !listMi.isEmpty()) {
                List<MstChoice> inventory_resultChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_inventory.inventory_result").getMstChoice();
                List<MstChoice> machine_confirm_methodChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_inventory.machine_confirm_method").getMstChoice();
                List<MstChoice> site_confirm_methodChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_inventory.site_confirm_method").getMstChoice();
                List<MstChoice> input_typeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_inventory.input_type").getMstChoice();

                for (int i = 0, j = listMi.size(); i < j; i++) {
                    TblMachineInventory mis = listMi.get(i);
                    TblMachineInventoryVo resMis = new TblMachineInventoryVo();
                    resMis.setId(mis.getId());
                    resMis.setMachineId(machine.getMachineId());
                    resMis.setMachineName(machine.getMachineName());
                    resMis.setMachineUuid(machine.getUuid());
                    resMis.setMachineInspectedDateStr(null == machine.getInspectedDate() ? "" : fu.getDateFormatForStr(machine.getInspectedDate()));
                    resMis.setMachineMainAssetNo(machine.getMainAssetNo());
                    resMis.setMachineCreateDateStr(null == machine.getMachineCreatedDate() ? "" : fu.getDateFormatForStr(machine.getMachineCreatedDate()));
                    resMis.setInventoryDateStr(null == mis.getInventoryDate() ? "" : fu.getDateTimeFormatForStr(mis.getInventoryDate()));

                    for (int inventory_resulti = 0; inventory_resulti < inventory_resultChoiceList.size(); inventory_resulti++) {
                        MstChoice aMstChoice = inventory_resultChoiceList.get(inventory_resulti);
                        if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mis.getInventoryResult()))) {
                            resMis.setInventoryResult(aMstChoice.getChoice());
                            break;
                        }
                    }

                    resMis.setCompanyName(mis.getCompanyName());
                    resMis.setLocationName(mis.getLocationName());
                    resMis.setInstllationSiteName(mis.getInstllationSiteName());

                    for (int machine_confirm_methodi = 0; machine_confirm_methodi < machine_confirm_methodChoiceList.size(); machine_confirm_methodi++) {
                        MstChoice aMstChoice = machine_confirm_methodChoiceList.get(machine_confirm_methodi);
                        if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mis.getMachineConfirmMethod()))) {
                            resMis.setMachineConfirmMethod(aMstChoice.getChoice());
                            break;
                        }
                    }

                    for (int site_confirm_methodi = 0; site_confirm_methodi < site_confirm_methodChoiceList.size(); site_confirm_methodi++) {
                        MstChoice aMstChoice = site_confirm_methodChoiceList.get(site_confirm_methodi);
                        if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mis.getSiteConfirmMethod()))) {
                            resMis.setSiteConfirmMethod(aMstChoice.getChoice());
                            break;
                        }
                    }

                    for (int input_typei = 0; input_typei < input_typeChoiceList.size(); input_typei++) {
                        MstChoice aMstChoice = input_typeChoiceList.get(input_typei);
                        if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mis.getInputType()))) {
                            resMis.setInputType(aMstChoice.getChoice());
                            break;
                        }
                    }

                    resMis.setImgFilePath(mis.getImgFilePath() == null ? "" : mis.getImgFilePath());

                    if (mis.getFileType() != null) {
                        resMis.setFileType(String.valueOf(mis.getFileType()));
                    } else {
                        resMis.setFileType("0");
                    }

                    if (mis.getTakenDate() != null) {
                        resMis.setTakenDate(fu.getDateTimeFormatForStr(mis.getTakenDate()));
                    } else {
                        resMis.setTakenDate("");
                    }

                    if (mis.getTakenDateStz() != null) {
                        resMis.setTakenDateStz(fu.getDateTimeFormatForStr(mis.getTakenDateStz()));
                    } else {
                        resMis.setTakenDateStz("");
                    }

                    resMis.setPersonUuid(mis.getPersonName());
                    resMis.setRemarks(mis.getRemarks());

                    
                    String departmentChange = String.valueOf(mis.getDepartmentChange()==null?"":mis.getDepartmentChange());
                    resMis.setDepartmentChange(departmentChange);

                    String department = String.valueOf(mis.getDepartment()==null?"":mis.getDepartment());
                    resMis.setDepartment(FileUtil.getStringValue(map.get("mst_user.department"+department)));
                    
                    String barcodeReprint =String.valueOf(mis.getBarcodeReprint()==null?"":mis.getBarcodeReprint());
                    resMis.setBarcodeReprint(barcodeReprint);

                    String assetDamaged =String.valueOf(mis.getAssetDamaged()==null?"":mis.getAssetDamaged());
                    resMis.setAssetDamaged(assetDamaged);

                    String notInUse = String.valueOf(mis.getNotInUse()==null?"":mis.getNotInUse());
                    resMis.setNotInUse(notInUse);

                    if (mis.getMstMachine().getInventoryStatus() == 0) {
                        resMis.setDepartmentChange("");
                        resMis.setDepartment("");
                        resMis.setBarcodeReprint("");
                        resMis.setAssetDamaged("");
                        resMis.setNotInUse("");
                    }
                    machineInventorys.add(resMis);
                }
            }

            resMachineInventorys.setTblMachineInventoryVos(machineInventorys);

            resMachineInventorys.setMachineId(machine.getMachineId());
            resMachineInventorys.setMachineName(machine.getMachineName());
            if (null != machine.getStatus()) {
                resMachineInventorys.setStatus("" + machine.getStatus());
            }
            resMachineInventorys.setOwnerCompanyName(null == machine.getOwnerMstCompany() ? "" : machine.getOwnerMstCompany().getCompanyName());

            resMachineInventorys.setCompanyId(machine.getCompanyId());
            resMachineInventorys.setCompanyName(null == machine.getMstCompany() ? "" : machine.getMstCompany().getCompanyName());
            resMachineInventorys.setLocationId(machine.getLocationId());
            resMachineInventorys.setLocationName(null == machine.getMstLocation() ? "" : machine.getMstLocation().getLocationName());
            resMachineInventorys.setInstllationSiteId(machine.getInstllationSiteId());
            resMachineInventorys.setInstllationSiteName(null == machine.getMstInstallationSite() ? "" : machine.getMstInstallationSite().getInstallationSiteName());
            //externalデータがチェック 
            if (machine.getCompanyId() != null && !"".equals(machine.getCompanyId())) {
                if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", machine.getCompanyId(), loginUser).isError()) {
                    resMachineInventorys.setExternalFlg(1);
                } else {
                    resMachineInventorys.setExternalFlg(0);
                }
            } else {
                resMachineInventorys.setExternalFlg(0);
            }

            //externalデータがチェック 
            resMachineInventorys.setMachineUuid(machine.getUuid());
            resMachineInventorys.setInstalledDateStr(fu.getDateFormatForStr(machine.getInstalledDate()));
            resMachineInventorys.setMachineMainAssetNo(machine.getMainAssetNo());
            resMachineInventorys.setMachineCreateDateStr(null == machine.getMachineCreatedDate() ? "" : fu.getDateFormatForStr(machine.getMachineCreatedDate()));
            resMachineInventorys.setMachineInspectedDateStr(null == machine.getInspectedDate() ? "" : fu.getDateFormatForStr(machine.getInspectedDate()));
        }

        return resMachineInventorys;
    }

    /**
     * 設備棚卸マスタCSV出力
     *
     *
     * @param paraMachineInventorys
     * @param loginUser
     * @param stocktakeIncomplete
     * @return
     */
    public FileReponse getTblMachineInventoryOutputCsv(TblMachineInventoryVo paraMachineInventorys, LoginUser loginUser, int stocktakeIncomplete) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        String outMachineId = mstDictionaryService.getDictionaryValue(langId, "machine_id");
        String outMachineName = mstDictionaryService.getDictionaryValue(langId, "machine_name");
        String outLatestInventoryDate = mstDictionaryService.getDictionaryValue(langId, "latest_inventory_date");
        String outStatus = mstDictionaryService.getDictionaryValue(langId, "machine_status");
        String outPersonName = mstDictionaryService.getDictionaryValue(langId, "machine_inventory_person");
        String outDepartmentChange = mstDictionaryService.getDictionaryValue(langId, "department_change");//部署変更
        String outDepartment = mstDictionaryService.getDictionaryValue(langId, "contact_department");//部署
        String outBarcodeReprint = mstDictionaryService.getDictionaryValue(langId, "bar_code_reprint");//資産シール
        String outAssetDamaged = mstDictionaryService.getDictionaryValue(langId, "asset_damaged");//故障
        String outNotInUse = mstDictionaryService.getDictionaryValue(langId, "not_in_use");//遊休
        String outOwnerCompanyName = mstDictionaryService.getDictionaryValue(langId, "owner_company_name");
        String outInstalledDate = mstDictionaryService.getDictionaryValue(langId, "installed_date");
        String outCompName = mstDictionaryService.getDictionaryValue(langId, "company_name");
        String outLocationName = mstDictionaryService.getDictionaryValue(langId, "location_name");
        String outInstallationSiteName = mstDictionaryService.getDictionaryValue(langId, "installation_site_name");

        /*Head*/
        headList.add(outMachineId);
        headList.add(outMachineName);
        headList.add(outLatestInventoryDate);
        headList.add(outStatus);
        headList.add(outPersonName);
        headList.add(outDepartmentChange);//部署変更
        headList.add(outDepartment);//部署
        headList.add(outBarcodeReprint);//資産シール
        headList.add(outAssetDamaged);//故障
        headList.add(outNotInUse);//遊休
        headList.add(outOwnerCompanyName);
        headList.add(outInstalledDate);
        headList.add(outCompName);
        headList.add(outLocationName);
        headList.add(outInstallationSiteName);

        gLineList.add(headList);

        //明細データを取得
        List<TblMachineInventoryVo> resList = getMachineInventories(paraMachineInventorys, loginUser, stocktakeIncomplete);
        /*Detail*/
        for (int i = 0; i < resList.size(); i++) {
            lineList = new ArrayList();
            TblMachineInventoryVo aTblMachineInventoryVo = resList.get(i);
            lineList.add(aTblMachineInventoryVo.getMachineId());
            lineList.add(aTblMachineInventoryVo.getMachineName());
            lineList.add(aTblMachineInventoryVo.getInventoryDateStr());
            lineList.add(aTblMachineInventoryVo.getStatus());
            lineList.add(aTblMachineInventoryVo.getPersonName());
            lineList.add(aTblMachineInventoryVo.getDepartmentChange());//部署変更
            lineList.add(aTblMachineInventoryVo.getDepartment());//部署Text
            lineList.add(aTblMachineInventoryVo.getBarcodeReprint());//資産シール
            lineList.add(aTblMachineInventoryVo.getAssetDamaged());//故障
            lineList.add(aTblMachineInventoryVo.getNotInUse());//遊休
            lineList.add(aTblMachineInventoryVo.getOwnerCompanyName());
            lineList.add(aTblMachineInventoryVo.getMachineInstalledDateStr());
            lineList.add(aTblMachineInventoryVo.getCompanyName());
            lineList.add(aTblMachineInventoryVo.getLocationName());
            lineList.add(aTblMachineInventoryVo.getInstllationSiteName());
            gLineList.add(lineList);
        }

        CSVFileUtil csvFileUtil = null;
        try {
            csvFileUtil = new CSVFileUtil(outCsvPath, "csvOutput");
            Iterator<ArrayList> iter = gLineList.iterator();
            while (iter.hasNext()) {
                csvFileUtil.toCSVLine(iter.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_TBL_MACHINE_INVENTORY);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE_INVENTORY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "machine_inventory");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * 設備棚卸 取得方法
     *
     * @param paraMachineInventoryVo
     * @param type： query--複数取得、count--件数取得
     * @param loginUser
     * @param stocktakeIncomplete
     * @return
     */
    public Query getTblMachineInventoryTypedQuery(TblMachineInventoryVo paraMachineInventoryVo, String type, LoginUser loginUser, int stocktakeIncomplete) {
        StringBuilder sql = new StringBuilder();
        if ("query".equals(type) || "detail".equals(type)) {
            sql.append(" SELECT m.uuid,m.machineId,m.machineName,m.ownerCompanyId,m.installedDate,m.companyId,");
            sql.append(" c.companyName,m.locationId,mstloc.locationName,m.instllationSiteId,mstins.installationSiteName,");
            sql.append(" m.status,mi.inventoryDate,oc.companyName AS ownerCompanyName,cc.choice AS statusName");

            //部署変更(0:無,1:有)
            //部署
            //資産シール(0:無,1:有)
            //故障(0:無,1:有)
            //遊休(0:F,1:T)
            sql.append(" ,mi.departmentChange, mi.department, mi.barcodeReprint, mi.assetDamaged, mi.notInUse, mi.personName");

            sql.append(" FROM MstMachine m  ");//設備マスタ
            sql.append(" INNER JOIN TblMachineInventory mi ");//設備棚卸テーブル
            sql.append(" ON m.uuid = mi.machineUuid ");
            sql.append(" AND m.latestInventoryId = mi.id ");//設備マスタ.最新棚卸ID
            sql.append(" LEFT OUTER JOIN MstLocation mstloc ");//所在地マスタ
            sql.append(" ON m.locationId = mstloc.id ");
            sql.append(" LEFT OUTER JOIN MstInstallationSite mstins ");//設置場所地マスタ
            sql.append(" ON m.instllationSiteId = mstins.id ");
            sql.append(" LEFT OUTER JOIN MstCompany c ");//会社マスタ
            sql.append(" ON m.companyId = c.id ");
            sql.append(" LEFT OUTER JOIN MstCompany oc ");//会社マスタ
            sql.append(" ON m.ownerCompanyId = oc.id ");
            sql.append(" LEFT OUTER JOIN MstChoice cc ");
            sql.append(" ON m.status = cc.mstChoicePK.seq ");
            sql.append(" AND cc.mstChoicePK.category = 'mst_machine.status' ");
            sql.append(" AND cc.mstChoicePK.langId = '".concat(loginUser.getLangId()).concat("'"));
            sql.append(" WHERE 1 = 1 ");
        } else if ("count".equals(type)) {
            sql.append(" SELECT count(m.uuid) ");
            sql.append(" FROM MstMachine m  ");//設備マスタ
            sql.append(" INNER JOIN TblMachineInventory mi ");//設備棚卸テーブル
            sql.append(" ON m.uuid = mi.machineUuid ");
            sql.append(" AND m.latestInventoryId = mi.id ");//設備マスタ.最新棚卸ID
            sql.append(" LEFT OUTER JOIN MstLocation mstloc ");//所在地マスタ
            sql.append(" ON m.locationId = mstloc.id ");
            sql.append(" LEFT OUTER JOIN MstInstallationSite mstins ");//設置場所地マスタ
            sql.append(" ON m.instllationSiteId = mstins.id ");
            sql.append(" LEFT OUTER JOIN MstCompany c ");//会社マスタ
            sql.append(" ON m.companyId = c.id ");
            sql.append(" LEFT OUTER JOIN MstCompany oc ");//会社マスタ
            sql.append(" ON m.ownerCompanyId = oc.id ");
            sql.append(" LEFT OUTER JOIN MstChoice cc ");
            sql.append(" ON m.status = cc.mstChoicePK.seq ");
            sql.append(" AND cc.mstChoicePK.category = 'mst_machine.status' ");
            sql.append(" AND cc.mstChoicePK.langId = '".concat(loginUser.getLangId()).concat("'"));
            sql.append(" WHERE 1 = 1 ");
        }

        if (null != paraMachineInventoryVo.getMachineId() && !paraMachineInventoryVo.getMachineId().trim().equals("")) {
            sql.append(" and m.machineId like :machineId ");
        }
        if (null != paraMachineInventoryVo.getMachineName() && !paraMachineInventoryVo.getMachineName().trim().equals("")) {
            sql.append(" and m.machineName like :machineName ");
        }
        if (null != paraMachineInventoryVo.getOwnerCompanyName() && !paraMachineInventoryVo.getOwnerCompanyName().trim().equals("")) {
            sql.append(" and oc.companyName like :ownerCompanyName ");
        }
        if (null != paraMachineInventoryVo.getCompanyName() && !paraMachineInventoryVo.getCompanyName().trim().equals("")) {
            sql.append(" and c.companyName like :companyName ");
        }
        if (null != paraMachineInventoryVo.getLocationName() && !paraMachineInventoryVo.getLocationName().trim().equals("")) {
            sql.append(" and mstloc.locationName like :locationName ");
        }
        if (null != paraMachineInventoryVo.getInstllationSiteName() && !paraMachineInventoryVo.getInstllationSiteName().trim().equals("")) {
            sql.append(" and mstins.installationSiteName like :instllationSiteName ");
        }
        if (null != paraMachineInventoryVo.getLatestInventoryDateStartStr() && !paraMachineInventoryVo.getLatestInventoryDateStartStr().trim().equals("")) {
            sql.append(" and mi.inventoryDate >= :inventoryDateStart ");

        }

        if (null != paraMachineInventoryVo.getLatestInventoryDateEndStr() && !paraMachineInventoryVo.getLatestInventoryDateEndStr().trim().equals("")) {
            sql.append(" and mi.inventoryDate <= :inventoryDateEnd  ");

        }
        //最新棚卸日時の検索条件が入力されたとき、検索対象外
        if (StringUtils.isNotEmpty(paraMachineInventoryVo.getLatestInventoryDateStartStr())
                || StringUtils.isNotEmpty(paraMachineInventoryVo.getLatestInventoryDateEndStr())) {
            sql.append(" and m.inventoryStatus <> 0 ");
        }
        
        if(stocktakeIncomplete == 1){
            sql.append(" and m.inventoryStatus = 0 ");
        }

        if (null != paraMachineInventoryVo.getStatus() && !paraMachineInventoryVo.getStatus().trim().equals("")) {
            sql.append(" and m.status = :status ");
        }
        
        if (null != paraMachineInventoryVo.getPersonName()&& !paraMachineInventoryVo.getPersonName().trim().equals("")) {
            sql.append(" and mi.personName like :personName ");
        }

        sql.append("  order by m.machineId ");//表示順は設備IDの昇順

        Query query = entityManager.createQuery(sql.toString());

        if (null != paraMachineInventoryVo.getMachineId() && !paraMachineInventoryVo.getMachineId().trim().equals("")) {
            query.setParameter("machineId", "%" + paraMachineInventoryVo.getMachineId().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getMachineName() && !paraMachineInventoryVo.getMachineName().trim().equals("")) {
            query.setParameter("machineName", "%" + paraMachineInventoryVo.getMachineName().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getOwnerCompanyName() && !paraMachineInventoryVo.getOwnerCompanyName().trim().equals("")) {
            query.setParameter("ownerCompanyName", "%" + paraMachineInventoryVo.getOwnerCompanyName().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getCompanyName() && !paraMachineInventoryVo.getCompanyName().trim().equals("")) {
            query.setParameter("companyName", "%" + paraMachineInventoryVo.getCompanyName().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getLocationName() && !paraMachineInventoryVo.getLocationName().trim().equals("")) {
            query.setParameter("locationName", "%" + paraMachineInventoryVo.getLocationName().trim() + "%");
        }
        if (null != paraMachineInventoryVo.getInstllationSiteName() && !paraMachineInventoryVo.getInstllationSiteName().trim().equals("")) {
            query.setParameter("instllationSiteName", "%" + paraMachineInventoryVo.getInstllationSiteName().trim() + "%");
        }
        FileUtil fu = new FileUtil();
        if (null != paraMachineInventoryVo.getLatestInventoryDateStartStr() && !paraMachineInventoryVo.getLatestInventoryDateStartStr().trim().equals("")) {
            query.setParameter("inventoryDateStart", fu.getDateTimeParseForDate(paraMachineInventoryVo.getLatestInventoryDateStartStr() + CommonConstants.SYS_MIN_TIME));
        }

        if (null != paraMachineInventoryVo.getLatestInventoryDateEndStr() && !paraMachineInventoryVo.getLatestInventoryDateEndStr().trim().equals("")) {
            query.setParameter("inventoryDateEnd", fu.getDateTimeParseForDate(paraMachineInventoryVo.getLatestInventoryDateEndStr() + CommonConstants.SYS_MAX_TIME));
        }

        if (null != paraMachineInventoryVo.getStatus() && !paraMachineInventoryVo.getStatus().trim().equals("")) {
            //if (!"0".equals(paraMachineInventoryVo.getStatus().trim())) {
            query.setParameter("status", Integer.parseInt(paraMachineInventoryVo.getStatus()));
            //}
        }
        
        if (null != paraMachineInventoryVo.getPersonName() && !paraMachineInventoryVo.getPersonName().trim().equals("")) {
            query.setParameter("personName", "%" + paraMachineInventoryVo.getPersonName().trim() + "%");
        }

        return query;
    }

    /**
     * 設備マスタの最新棚卸IDも更新。所在が変わっているとき設備マスタの所在も更新、所在履歴作成。
     *
     * @param tblMachineInventorys
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMachineInventoryHistorie(TblMachineInventoryVo tblMachineInventorys, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        MstMachine machine = entityManager.find(MstMachine.class, tblMachineInventorys.getMachineId());
        boolean isHistory = true;
        String oldCompanyId = null == machine.getCompanyId() ? "" : machine.getCompanyId();
        String oldLocationId = null == machine.getLocationId() ? "" : machine.getLocationId();
        String oldInstllationSiteId = null == machine.getInstllationSiteId() ? "" : machine.getInstllationSiteId();
        if (!oldCompanyId.equals(tblMachineInventorys.getCompanyId()) || !oldLocationId.equals(tblMachineInventorys.getLocationId()) || !oldInstllationSiteId.equals(tblMachineInventorys.getInstllationSiteId())) {
            isHistory = false;
        }

        TblMachineInventory aTblMachineInventory = new TblMachineInventory();
        aTblMachineInventory.setCreateDate(new Date());
        aTblMachineInventory.setCreateUserUuid(loginUser.getUserUuid());
        aTblMachineInventory.setUpdateDate(new Date());
        aTblMachineInventory.setUpdateUserUuid(loginUser.getUserUuid());

        String str = IDGenerator.generate();
        aTblMachineInventory.setId(str);
        aTblMachineInventory.setMachineUuid(tblMachineInventorys.getMachineUuid());
        aTblMachineInventory.setRemarks(tblMachineInventorys.getRemarks());
        aTblMachineInventory.setInventoryResult(Integer.parseInt(tblMachineInventorys.getInventoryResult()));
        FileUtil fu = new FileUtil();
        if (null != tblMachineInventorys.getInventoryDateStr() && !"".contains(tblMachineInventorys.getInventoryDateStr())) {
            SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
            try {
                aTblMachineInventory.setInventoryDate(sdf.parse(DateFormat.dateTimeFormat(sdf.parse(tblMachineInventorys.getInventoryDateStr()), loginUser.getJavaZoneId())));
                aTblMachineInventory.setInventoryDateSzt(sdf.parse(tblMachineInventorys.getInventoryDateStr()));
            } catch (ParseException ex) {
                Logger.getLogger(TblMachineInventoryService.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            aTblMachineInventory.setInventoryDate(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
            aTblMachineInventory.setInventoryDateSzt(null == tblMachineInventorys.getInventoryDateStr() ? new Date() : fu.getDateTimeParseForDate(tblMachineInventorys.getInventoryDateSztStr()));
        }
        Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
        userQuery.setParameter("uuid", loginUser.getUserUuid());
        try {
            MstUser user = (MstUser) userQuery.getSingleResult();
            aTblMachineInventory.setPersonName(user.getUserName());
            aTblMachineInventory.setPersonUuid(loginUser.getUserUuid());
        } catch (NoResultException e) {
            aTblMachineInventory.setPersonName(null);
            aTblMachineInventory.setPersonUuid(loginUser.getUserUuid());
        }

        aTblMachineInventory.setMachineConfirmMethod(1);
        aTblMachineInventory.setSiteConfirmMethod(1);
        aTblMachineInventory.setInputType(2);
        if (tblMachineInventorys.getCompanyId() != null && !tblMachineInventorys.getCompanyId().trim().equals("")) {
            aTblMachineInventory.setCompanyId(tblMachineInventorys.getCompanyId());
            aTblMachineInventory.setCompanyName(tblMachineInventorys.getCompanyName());
        }

        if (tblMachineInventorys.getInstllationSiteId() != null && !tblMachineInventorys.getInstllationSiteId().trim().equals("")) {
            aTblMachineInventory.setInstllationSiteId(tblMachineInventorys.getInstllationSiteId());
            aTblMachineInventory.setInstllationSiteName(tblMachineInventorys.getInstllationSiteName());
        }

        if (tblMachineInventorys.getLocationId() != null && !tblMachineInventorys.getLocationId().trim().equals("")) {
            aTblMachineInventory.setLocationId(tblMachineInventorys.getLocationId());
            aTblMachineInventory.setLocationName(tblMachineInventorys.getLocationName());
        }
        //2018.5.03 
        String departmentChange = FileUtil.getStringValue(tblMachineInventorys.getDepartmentChange());
        aTblMachineInventory.setDepartmentChange(Integer.parseInt("".equals(departmentChange) ? "0" : departmentChange));

        if (1 == aTblMachineInventory.getDepartmentChange()) {
            String department = FileUtil.getStringValue(tblMachineInventorys.getDepartment());
            aTblMachineInventory.setDepartment(Integer.parseInt("".equals(department) ? "0" : department));
        } else {
            aTblMachineInventory.setDepartment(machine.getDepartment());
        }

        String barcodeReprint = FileUtil.getStringValue(tblMachineInventorys.getBarcodeReprint());
        aTblMachineInventory.setBarcodeReprint(Integer.parseInt("".equals(barcodeReprint) ? "0" : barcodeReprint));

        String assetDamaged = FileUtil.getStringValue(tblMachineInventorys.getAssetDamaged());
        aTblMachineInventory.setAssetDamaged(Integer.parseInt("".equals(assetDamaged) ? "0" : assetDamaged));

        String notInUse = FileUtil.getStringValue(tblMachineInventorys.getNotInUse());
        aTblMachineInventory.setNotInUse(Integer.parseInt("".equals(notInUse) ? "0" : notInUse));

        entityManager.persist(aTblMachineInventory);
        //棚卸結果が所在不明、処分済みのとき、設備マスタのステータスを合わせて更新する
        String choiceText = "";
        String statusValue = "";
        if (aTblMachineInventory.getInventoryResult() == 1) {
            //1 == 所在確認
            statusValue = "0";
        } else {
            MstChoiceList inventoryresultChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_inventory.inventory_result");
            if (null != inventoryresultChoiceList && !inventoryresultChoiceList.getMstChoice().isEmpty()) {
                for (int i = 0; i < inventoryresultChoiceList.getMstChoice().size(); i++) {
                    if (inventoryresultChoiceList.getMstChoice().get(i).getMstChoicePK().getSeq().equals("" + aTblMachineInventory.getInventoryResult())) {
                        choiceText = inventoryresultChoiceList.getMstChoice().get(i).getChoice();
                        break;
                    }
                }
            }

            MstChoiceList machinestatusChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_machine.status");
            if (null != machinestatusChoiceList && !machinestatusChoiceList.getMstChoice().isEmpty()) {
                for (int i = 0; i < machinestatusChoiceList.getMstChoice().size(); i++) {
                    if (choiceText.equals(machinestatusChoiceList.getMstChoice().get(i).getChoice())) {
                        statusValue = machinestatusChoiceList.getMstChoice().get(i).getMstChoicePK().getSeq();
                        break;
                    }
                }
            }
        }
        StringBuilder sql;
        sql = new StringBuilder("UPDATE MstMachine t SET "
                + " t.latestInventoryId = :latestInventoryId,"
                + " t.companyId = :companyId, "
                + " t.companyName = :companyName,"
                + " t.locationId = :locationId, "
                + " t.locationName = :locationName, "
                + " t.instllationSiteId = :instllationSiteId, "
                + " t.instllationSiteName = :instllationSiteName, "
                + " t.updateDate = :updateDate, "
                + " t.updateUserUuid = :updateUserUuid ");

        if (statusValue.equals("") == false) {
            sql.append(" ,t.status = :status, t.statusChangedDate = :statusChangedDate ");
        }
        if (null == machine.getInstalledDate() && (null != aTblMachineInventory.getCompanyId() || null != aTblMachineInventory.getLocationId() || null != aTblMachineInventory.getInstllationSiteId())) {
            sql.append(" ,t.installedDate = :installedDate  ");
        }
        sql.append(" ,t.inventoryStatus = :inventoryStatus ");
        if (1 == aTblMachineInventory.getDepartmentChange()) {
            sql.append(" ,t.department = :department ");
        }

        sql.append(" WHERE t.uuid = :machineUuid");
        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("machineUuid", tblMachineInventorys.getMachineUuid());

        query.setParameter("latestInventoryId", str);
        query.setParameter("companyId", null == tblMachineInventorys.getCompanyId() || tblMachineInventorys.getCompanyId().trim().equals("") ? null : tblMachineInventorys.getCompanyId());
        query.setParameter("companyName", null == tblMachineInventorys.getCompanyName() || tblMachineInventorys.getCompanyName().trim().equals("") ? null : tblMachineInventorys.getCompanyName());
        query.setParameter("locationId", null == tblMachineInventorys.getLocationId() || tblMachineInventorys.getLocationId().trim().equals("") ? null : tblMachineInventorys.getLocationId());
        query.setParameter("locationName", null == tblMachineInventorys.getLocationName() || tblMachineInventorys.getLocationName().trim().equals("") ? null : tblMachineInventorys.getLocationName());
        query.setParameter("instllationSiteId", null == tblMachineInventorys.getInstllationSiteId() || tblMachineInventorys.getInstllationSiteId().trim().equals("") ? null : tblMachineInventorys.getInstllationSiteId());
        query.setParameter("instllationSiteName", null == tblMachineInventorys.getInstllationSiteName() || tblMachineInventorys.getInstllationSiteName().trim().equals("") ? null : tblMachineInventorys.getInstllationSiteName());
        if (statusValue.equals("") == false) {
            if ("1".equals(notInUse)) {
                query.setParameter("status", 7);
            } else {
                query.setParameter("status", Integer.parseInt(statusValue));
            }
            query.setParameter("statusChangedDate", aTblMachineInventory.getInventoryDate());

        }
        if (null == machine.getInstalledDate() && (null != aTblMachineInventory.getCompanyId() || null != aTblMachineInventory.getLocationId() || null != aTblMachineInventory.getInstllationSiteId())) {
            query.setParameter("installedDate", new Date());
        }
        if (1 == aTblMachineInventory.getDepartmentChange()) {
            query.setParameter("department", aTblMachineInventory.getDepartment());
        }
        query.setParameter("inventoryStatus", 1);
        query.executeUpdate();

        if (isHistory == false) {
            TblMachineLocationHistory inData;
            List list = tblMachineLocationHistoryService.getMachineLocationHistoriesByMachineId(tblMachineInventorys.getMachineId());
            if (list.size() > 0) {
                if (list.get(0) != null) {
                    inData = new TblMachineLocationHistory();
                    String strId = list.get(0).toString();
                    fu = new FileUtil();
                    Date befInstalledDate = fu.getSpecifiedDayBefore(new Date());
                    inData.setEndDate(befInstalledDate);//現在の履歴は終了日に新しい設置日の前日をセットして更新する。
                    inData.setUpdateDate(new Date());
                    inData.setUpdateUserUuid(loginUser.getUserUuid());
                    inData.setId(strId);
                    //update 現在の履歴 MachineLocationHistory
                    tblMachineLocationHistoryService.updateMachineLocationHistoriesByEndDate(inData);
                }
            }
            inData = new TblMachineLocationHistory();
            inData.setId(IDGenerator.generate());
            inData.setMachineUuid(tblMachineInventorys.getMachineUuid());
            inData.setStartDate(new Date());//開始日に設置日
            inData.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付/todo

            // 棚卸という文言を選択マスタから取得する
            MstChoiceList choiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_machine.change_reason");
            //移動理由を「棚卸」に設定
            if (null != choiceList.getMstChoice() && choiceList.getMstChoice().size() > 0) {
                MstChoice mstChoice = choiceList.getMstChoice().get(0);
                inData.setChangeReason(Integer.parseInt(mstChoice.getMstChoicePK().getSeq())); //1
                inData.setChangeReasonText(mstChoice.getChoice()); //棚卸
            }

            if (tblMachineInventorys.getCompanyId() != null && !"".equals(tblMachineInventorys.getCompanyId())) {
                inData.setCompanyId(tblMachineInventorys.getCompanyId());
                inData.setCompanyName(tblMachineInventorys.getCompanyName());
            }

            if (tblMachineInventorys.getLocationId() != null && !"".equals(tblMachineInventorys.getLocationId())) {

                inData.setLocationId(tblMachineInventorys.getLocationId());
                inData.setLocationName(tblMachineInventorys.getLocationName());

            }

            if (tblMachineInventorys.getInstllationSiteId() != null && !"".equals(tblMachineInventorys.getInstllationSiteId())) {
                inData.setInstllationSiteId(tblMachineInventorys.getInstllationSiteId());
                inData.setInstllationSiteName(tblMachineInventorys.getInstllationSiteName());
            }

            inData.setCreateDate(new Date());
            inData.setCreateUserUuid(loginUser.getUserUuid());
            inData.setUpdateDate(new Date());
            inData.setUpdateUserUuid(loginUser.getUserUuid());
            //creat MachineLocationHistory(new) 新たな設備移動履歴
            tblMachineLocationHistoryService.creatMachineLocationHistories(inData);
        }

        // 設備IDで棚卸依頼IDテーブルを見る
        List<TblInventoryRequestDetail> list = tblInventoryRequestDetailService.getTblInventoryRequestMoldMachineIdExist(null, machine.getMachineId());
        if (list != null && list.size() > 0) {
            //棚卸依頼明細テーブル\棚卸依頼明細IDテーブル現品有無更新
            tblInventoryRequestDetailService.updateTblInventoryRequestDetailExistence(list, aTblMachineInventory.getInventoryResult(), loginUser.getUserUuid(), isHistory, aTblMachineInventory.getLocationId());
        } else {
            //棚卸明細テーブル現品有無更新
            tblInventoryDetailService.updateTblInventoryDetailExistence(null, machine.getUuid(), aTblMachineInventory.getInventoryResult(), isHistory, aTblMachineInventory.getLocationId());
        }
        return basicResponse;
    }

    /**
     * 削除は最新の履歴のみ可能。設備マスタ、所在移動履歴も更新
     *
     * @param inventoryId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteMachineInventoryHistorie(String inventoryId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        TblMachineInventory tblMachineInventory = entityManager.find(TblMachineInventory.class, inventoryId);
        if (null == tblMachineInventory) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return basicResponse;
        }

        MstMachine machine = tblMachineInventory.getMstMachine();

        StringBuilder sql = new StringBuilder("SELECT t FROM TblMachineInventory t WHERE t.mstMachine.uuid = :machineUuid ORDER BY t.inventoryDate desc ");
        Query query = entityManager.createQuery(sql.toString())
                .setParameter("machineUuid", machine.getUuid())
                .setMaxResults(2);

        List<TblMachineInventory> tmpMachineInventorys = query.getResultList(); //lastest 2 TblMachineInventory
        if (!tmpMachineInventorys.isEmpty()) {
            if (tmpMachineInventorys.get(0).getId().equals(inventoryId) == false) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_latest_history_can_be_edited"));
                return basicResponse;
            }
            sql = new StringBuilder("UPDATE MstMachine t SET "
                    + " t.latestInventoryId = :latestInventoryId,"
                    + " t.companyId = :companyId, "
                    + " t.companyName = :companyName,"
                    + " t.locationId = :locationId, "
                    + " t.locationName = :locationName, "
                    + " t.instllationSiteId = :instllationSiteId, "
                    + " t.instllationSiteName = :instllationSiteName, "
                    + " t.updateDate = :updateDate, "
                    + " t.updateUserUuid = :updateUserUuid "
                    + " WHERE t.uuid = :machineUuid");

            query = entityManager.createQuery(sql.toString());

            query.setParameter("updateDate", new Date());
            query.setParameter("updateUserUuid", loginUser.getUserUuid());
            query.setParameter("machineUuid", machine.getUuid());
            if (tmpMachineInventorys.size() == 1) {
                query.setParameter("latestInventoryId", null);
                query.setParameter("companyId", null);
                query.setParameter("companyName", null);
                query.setParameter("locationId", null);
                query.setParameter("locationName", null);
                query.setParameter("instllationSiteId", null);
                query.setParameter("instllationSiteName", null);
            } else {
                query.setParameter("latestInventoryId", tmpMachineInventorys.get(1).getId());
                query.setParameter("companyId", tmpMachineInventorys.get(1).getCompanyId());
                query.setParameter("companyName", tmpMachineInventorys.get(1).getCompanyName());
                query.setParameter("locationId", tmpMachineInventorys.get(1).getLocationId());
                query.setParameter("locationName", tmpMachineInventorys.get(1).getLocationName());
                query.setParameter("instllationSiteId", tmpMachineInventorys.get(1).getInstllationSiteId());
                query.setParameter("instllationSiteName", tmpMachineInventorys.get(1).getInstllationSiteName());
            }
            query.executeUpdate();
            query = entityManager.createNamedQuery("TblMachineInventory.removeById");
            query.setParameter("id", tmpMachineInventorys.get(0).getId());
            query.executeUpdate();
        } else {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return basicResponse;
        }
        basicResponse.setError(false);
        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
        basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        return basicResponse;
    }

    /**
     * 設備棚卸登録/登録 棚卸結果を登録
     *
     * @param tblMachineInventoryList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateMachineInventories(TblMachineInventoryList tblMachineInventoryList, LoginUser loginUser) {
        TblMachineInventoryVo inputData;
        TblMachineInventory tblMachineInventory = new TblMachineInventory();
        SimpleDateFormat format = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        BasicResponse response = new BasicResponse();
        if (tblMachineInventoryList.getTblMachineInventoryVos() != null && tblMachineInventoryList.getTblMachineInventoryVos().size() > 0) {
            // 棚卸という文言を選択マスタから取得する
            MstChoiceList choiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_machine.change_reason");
            MstChoiceList inventoryresultChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_inventory.inventory_result");
            MstChoiceList machinestatusChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_machine.status");

            for (int i = 0; i < tblMachineInventoryList.getTblMachineInventoryVos().size(); i++) {
                inputData = tblMachineInventoryList.getTblMachineInventoryVos().get(i);

                String oldCompanyId = "";
                String oldLocationId = "";
                String oldInstllationSiteId = "";
                String oldStatus = "";

                String strCompanyId = "";
                String strCompanyName = "";
                String strLocationId = "";
                String strLocationName = "";
                String strInstllationSiteId = "";
                String strInstllationSiteName = "";
//                String strStatus = "";

                if (inputData != null) {
                    MstMachine machine = entityManager.find(MstMachine.class, inputData.getMachineId());
                    oldCompanyId = null == (oldCompanyId = machine.getCompanyId()) ? "" : oldCompanyId;
                    oldLocationId = null == (oldLocationId = machine.getLocationId()) ? "" : oldLocationId;
                    oldInstllationSiteId = null == (oldInstllationSiteId = machine.getInstllationSiteId()) ? "" : oldInstllationSiteId;
//                oldStatus = machine.getStatus().toString();

                    //外部データが取得 add 2017-1-16 10:28:43 jiangxiaosong
                    if (machine.getCompanyId() != null && !"".equals(machine.getCompanyId())) {
                        response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", machine.getCompanyId(), loginUser);
                        if (response.isError()) {
                            return response;
                        }
                    }

                    TblMachineLocationHistory inData = null;
                    if ("1".equals(inputData.getHisFlag())) {
                        List list = tblMachineLocationHistoryService.getMachineLocationHistoriesByMachineId(inputData.getMachineId());
                        if (list.size() > 0) {
                            String objs = (String) list.get(0);
                            if (objs != null) {
                                inData = new TblMachineLocationHistory();
                                String strId = objs;
                                FileUtil fu = new FileUtil();
                                Date befInstalledDate = fu.getSpecifiedDayBefore(new Date());
                                inData.setEndDate(befInstalledDate);//現在の履歴は終了日に新しい設置日の前日をセットして更新する。
                                inData.setUpdateDate(new Date());
                                inData.setUpdateUserUuid(loginUser.getUserUuid());
                                inData.setId(strId);
                                //update 現在の履歴 MachineLocationHistory
                                tblMachineLocationHistoryService.updateMachineLocationHistoriesByEndDate(inData);
                            }
                        }
                        inData = new TblMachineLocationHistory();
                        inData.setId(IDGenerator.generate());
                        inData.setMachineUuid(inputData.getMachineUuid());
                        inData.setStartDate(new Date());//開始日に設置日
                        inData.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付

                        //移動理由を「棚卸」に設定
                        if (null != choiceList.getMstChoice() && choiceList.getMstChoice().size() > 0) {
                            MstChoice mstChoice = choiceList.getMstChoice().get(0);
                            inData.setChangeReason(Integer.parseInt(mstChoice.getMstChoicePK().getSeq())); //1
                            inData.setChangeReasonText(mstChoice.getChoice()); //棚卸
                        }

                        if (inputData.getCompanyId() != null && !"".equals(inputData.getCompanyId())) {
                            inData.setCompanyId(inputData.getCompanyId());
                            inData.setCompanyName(inputData.getCompanyName());
                        }

                        if (inputData.getLocationId() != null && !"".equals(inputData.getLocationId())) {

                            inData.setLocationId(inputData.getLocationId());
                            inData.setLocationName(inputData.getLocationName());

                        }

                        if (inputData.getInstllationSiteId() != null && !"".equals(inputData.getInstllationSiteId())) {
                            inData.setInstllationSiteId(inputData.getInstllationSiteId());
                            inData.setInstllationSiteName(inputData.getInstllationSiteName());
                        }

                        inData.setCreateDate(new Date());
                        inData.setCreateUserUuid(loginUser.getUserUuid());
                        inData.setUpdateDate(new Date());
                        inData.setUpdateUserUuid(loginUser.getUserUuid());
                        //creat MachineLocationHistory(new) 新たな設備移動履歴
                        tblMachineLocationHistoryService.creatMachineLocationHistories(inData);
                    }

                    //設備棚卸登録 if 最新棚卸IDが存在とき　update 設備棚卸テーブル
                    boolean needNewInventory = false;
                    if (inputData.getInventoryId() != null && !"".equals(inputData.getInventoryId())) {
                        if (oldStatus.equals(inputData.getStatus())) {
                            updateTblMachineInventor(inputData, loginUser);
                        } else {
                            needNewInventory = true;
                        }
                    } else {
                        needNewInventory = true;
                    }
                    if (needNewInventory == true) {
                        tblMachineInventory = new TblMachineInventory();
                        //creat 設備棚卸テーブル  update設備マスタ.最新棚卸ID
                        String strId = IDGenerator.generate();
                        tblMachineInventory.setId(strId);//ID
                        tblMachineInventory.setMachineUuid(inputData.getMachineUuid());//設備UUID
                        tblMachineInventory.setInventoryDate(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));//棚卸日時
                        tblMachineInventory.setInventoryDateSzt(new Date());//棚卸日時(SZT)

                        Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
                        userQuery.setParameter("uuid", loginUser.getUserUuid());
                        try {
                            MstUser user = (MstUser) userQuery.getSingleResult();
                            tblMachineInventory.setPersonName(user.getUserName());//担当者名称
                            tblMachineInventory.setPersonUuid(loginUser.getUserUuid());//担当者UUID
                        } catch (NoResultException e) {
                            tblMachineInventory.setPersonName(null);
                            tblMachineInventory.setPersonUuid(loginUser.getUserUuid());
                        }
                        if (null != inputData.getInventoryResult() && !"".equals(inputData.getInventoryResult().trim())) {
                            tblMachineInventory.setInventoryResult(Integer.parseInt(inputData.getInventoryResult()));//棚卸結果
                        } else {
                            tblMachineInventory.setInventoryResult(0);//棚卸結果
                        }
                        if (null != inputData.getSiteConfirmMethod() && !"".equals(inputData.getSiteConfirmMethod().trim())) {
                            tblMachineInventory.setSiteConfirmMethod(Integer.parseInt(inputData.getSiteConfirmMethod()));//設置場所確認方法(手動/QR)
                        }
                        if (null != inputData.getMachineConfirmMethod() && !"".equals(inputData.getMachineConfirmMethod().trim())) {
                            tblMachineInventory.setMachineConfirmMethod(Integer.parseInt(inputData.getMachineConfirmMethod()));//設備確認方法(手動/QR)
                        }

                        if (null != inputData.getCompanyId() && !"".equals(inputData.getCompanyId())) {
                            tblMachineInventory.setCompanyId(inputData.getCompanyId());//会社ID
                            tblMachineInventory.setCompanyName(inputData.getCompanyName());//会社名称
                        }
                        if (null != inputData.getLocationId() && !"".equals(inputData.getLocationId())) {
                            tblMachineInventory.setLocationId(inputData.getLocationId());//所在地ID
                            tblMachineInventory.setLocationName(inputData.getLocationName());//所在地名称
                        }

                        if (null != inputData.getInstllationSiteId() && !"".equals(inputData.getInstllationSiteId())) {
                            tblMachineInventory.setInstllationSiteId(inputData.getInstllationSiteId());//設置場所ID
                            tblMachineInventory.setInstllationSiteName(inputData.getInstllationSiteName());//設置場所名称
                        }
                        tblMachineInventory.setRemarks(inputData.getRemarks());//備考
                        tblMachineInventory.setInputType(Integer.parseInt(inputData.getInputType()));//入力区分(Tablet/Web)

                        //2018.4.26
                        tblMachineInventory.setAssetDamaged(Integer.parseInt(inputData.getAssetDamaged()));
                        tblMachineInventory.setBarcodeReprint(Integer.parseInt(inputData.getBarcodeReprint()));
                        tblMachineInventory.setDepartment(Integer.parseInt(inputData.getDepartment())); 
                        //棚卸時に変更後部署不明にすると部署変更フラグが立たない KM-853
                        tblMachineInventory.setDepartmentChange(Integer.parseInt(inputData.getDepartmentChange()));//2018.9.10

//                        if (null != inputData.getDepartment() && !"0".equals(inputData.getDepartment())) {//部门
//                           
//                        } else {
//                            tblMachineInventory.setDepartmentChange(0);
//                        }
                        //end
                        tblMachineInventory.setNotInUse(Integer.parseInt(inputData.getNotInUse()));

                        if (null != inputData.getImgFilePath() && !inputData.getImgFilePath().isEmpty()) {
                            tblMachineInventory.setImgFilePath(inputData.getImgFilePath());
                        }

                        if (null != inputData.getFileType() && !inputData.getFileType().isEmpty()) {
                            tblMachineInventory.setFileType(Integer.parseInt(inputData.getFileType()));
                        }

                        try {
                            if (null != inputData.getTakenDate() && !inputData.getTakenDate().isEmpty()) {
                                tblMachineInventory.setTakenDate(format.parse(DateFormat.dateTimeFormat(format.parse(inputData.getTakenDate()), loginUser.getJavaZoneId())));
                                tblMachineInventory.setTakenDateStz(format.parse(inputData.getTakenDate()));
                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(TblMachineInventoryService.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        tblMachineInventory.setCreateDate(new java.util.Date());//作成日時
                        tblMachineInventory.setCreateUserUuid(loginUser.getUserUuid());//更新日時
                        tblMachineInventory.setUpdateDate(new java.util.Date());//作成ユーザーUUID
                        tblMachineInventory.setUpdateUserUuid(loginUser.getUserUuid());//更新ユーザーUUID
                        entityManager.persist(tblMachineInventory);
                        inputData.setInventoryId(tblMachineInventory.getId());
                        inputData.setInventoryDateStr(DateFormat.dateFormat(tblMachineInventory.getInventoryDate(), loginUser.getLangId()));
                    }

                    if (!inputData.getInventoryResult().equals("0")) {
                        //棚卸結果が所在不明、処分済みのとき、所在確認、設備マスタのステータスを合わせて更新する
                        String choiceText = "";
                        String statusValue = "";
                        if (inputData.getInventoryResult().equals("1")) {
                            //1 == 所在確認
                            statusValue = "0";
                        } else {
                            //棚卸結果が所在不明、処分済みのとき
                            if (null != inventoryresultChoiceList && !inventoryresultChoiceList.getMstChoice().isEmpty()) {
                                for (int iri = 0, iricount = inventoryresultChoiceList.getMstChoice().size(); iri < iricount; iri++) {
                                    if (inventoryresultChoiceList.getMstChoice().get(iri).getMstChoicePK().getSeq().equals("" + inputData.getInventoryResult())) {
                                        choiceText = inventoryresultChoiceList.getMstChoice().get(iri).getChoice();
                                        break;
                                    }
                                }
                            }

                            if (null != machinestatusChoiceList && !machinestatusChoiceList.getMstChoice().isEmpty()) {
                                for (int si = 0, scount = machinestatusChoiceList.getMstChoice().size(); si < scount; si++) {
                                    if (choiceText.equals(machinestatusChoiceList.getMstChoice().get(si).getChoice())) {
                                        statusValue = machinestatusChoiceList.getMstChoice().get(si).getMstChoicePK().getSeq();
                                        break;
                                    }
                                }
                            }
                        }

                        //update設備マスタ.最新棚卸ID    
                        machine.setLatestInventoryId(inputData.getInventoryId());
                        machine.setCompanyId(null == (strCompanyId = inputData.getCompanyId()) || strCompanyId.equals("") ? null : strCompanyId);
                        machine.setCompanyName(null == (strCompanyName = inputData.getCompanyName()) || strCompanyName.equals("") ? null : strCompanyName);
                        machine.setLocationId(null == (strLocationId = inputData.getLocationId()) || strLocationId.equals("") ? null : strLocationId);
                        machine.setLocationName(null == (strLocationName = inputData.getLocationName()) || strLocationName.equals("") ? null : strLocationName);
                        machine.setInstllationSiteId(null == (strInstllationSiteId = inputData.getInstllationSiteId()) || strInstllationSiteId.equals("") ? null : strInstllationSiteId);
                        machine.setInstllationSiteName(null == (strInstllationSiteName = inputData.getInstllationSiteName()) || strInstllationSiteName.equals("") ? null : strInstllationSiteName);
                        if (statusValue.equals("") == false) {
                            machine.setStatus(Integer.parseInt(statusValue));
                            machine.setStatusChangedDate(new Date());
                        }
                        if (null == machine.getInstalledDate() && ((null != strCompanyId && !"".equals(strCompanyId.trim())) || (null != strLocationId && !"".equals(strLocationId.trim())) || (null != strInstllationSiteId && !"".equals(strInstllationSiteId.trim())))) {
                            FileUtil fu = new FileUtil();
                            machine.setInstalledDate(fu.getSpecifiedDayBefore(new Date()));
                        }
                        machine.setUpdateDate(new Date());
                        machine.setUpdateUserUuid(loginUser.getUserUuid());
                        //変更後部署を小ダイアログで登録。設備マスタの部署を更新
                        machine.setDepartment(Integer.parseInt(inputData.getDepartment()));
                        machine.setInventoryStatus(1);
                        //チェックした時、設備マスタのステータスを遊休にする。
                        if(Integer.parseInt(inputData.getNotInUse())==1){
                         machine.setStatus(7);
                        }
                        entityManager.merge(machine);
//                        strStatus = machine.getStatus().toString();
                    }
                    boolean isFlag = true;
                    if ((null == inData || null == inData.getId())
                            && (!oldCompanyId.equals(null == strCompanyId ? "" : strCompanyId) || !oldLocationId.equals(null == strLocationId ? "" : strLocationId) || !oldInstllationSiteId.equals(null == strInstllationSiteId ? "" : strInstllationSiteId))) {
                        isFlag = false;
                        FileUtil fu = new FileUtil();
                        Query query = entityManager.createQuery("UPDATE TblMachineLocationHistory t SET "
                                + "t.endDate = :endDate,"
                                + "t.updateDate = :updateDate,"
                                + "t.updateUserUuid = :updateUserUuid "
                                + "WHERE t.endDate = :oldEndDate and t.machineUuid = :machineUuid ");
                        query.setParameter("endDate", fu.getSpecifiedDayBefore(new Date()));
                        query.setParameter("updateDate", new Date());
                        query.setParameter("updateUserUuid", loginUser.getUserUuid());
                        query.setParameter("oldEndDate", CommonConstants.SYS_MAX_DATE);
                        query.setParameter("machineUuid", machine.getUuid());
                        query.executeUpdate();
//                        inData.setEndDate(fu.getSpecifiedDayBefore(new Date()));
//                        inData.setUpdateDate(new Date());
//                        inData.setUpdateUserUuid(loginUser.getUserUuid());
//                        entityManager.merge(inData);

                        TblMachineLocationHistory tblMachineLocationHistory = new TblMachineLocationHistory();
                        tblMachineLocationHistory.setId(IDGenerator.generate());
                        tblMachineLocationHistory.setMachineUuid(machine.getUuid());
                        //移動理由を「棚卸」に設定
                        if (null != choiceList.getMstChoice() && choiceList.getMstChoice().size() > 1) {
                            MstChoice mstChoice = choiceList.getMstChoice().get(1);
                            tblMachineLocationHistory.setChangeReason(Integer.parseInt(mstChoice.getMstChoicePK().getSeq())); //1
                            tblMachineLocationHistory.setChangeReasonText(mstChoice.getChoice()); //棚卸
                        }
                        tblMachineLocationHistory.setCompanyName(strCompanyName);
                        tblMachineLocationHistory.setLocationName(strLocationName);
                        tblMachineLocationHistory.setInstllationSiteName(strInstllationSiteName);
                        tblMachineLocationHistory.setCreateDate(new Date());
                        tblMachineLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
                        //TODO StartDate not allowed null but mstMachine's inspectedDate、installedDate and createDate allowed null
                        tblMachineLocationHistory.setStartDate(null == machine.getInstalledDate() ? new Date() : machine.getInstalledDate());
                        tblMachineLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付/todo
                        entityManager.persist(tblMachineLocationHistory);
                    }

                    // 設備IDで棚卸依頼IDテーブルを見る
                    List<TblInventoryRequestDetail> list = tblInventoryRequestDetailService.getTblInventoryRequestMoldMachineIdExist(null, machine.getMachineId());
                    if (list != null && list.size() > 0) {
                        //棚卸依頼明細テーブル\棚卸依頼明細IDテーブル現品有無更新
                        tblInventoryRequestDetailService.updateTblInventoryRequestDetailExistence(list, tblMachineInventory.getInventoryResult(), loginUser.getUserUuid(), isFlag, tblMachineInventory.getLocationId());
                    } else {
                        //棚卸明細テーブル現品有無更新
                        tblInventoryDetailService.updateTblInventoryDetailExistence(null, machine.getUuid(), tblMachineInventory.getInventoryResult(), isFlag, tblMachineInventory.getLocationId());
                    }
                }
            }
        }

        return response;
    }

    /**
     * 設備棚卸登録/登録-棚卸結果を登録 TblMachineInventor更新のため
     *
     * @param getData
     * @param loginUser
     * @return
     */
    @Transactional
    public int updateTblMachineInventor(TblMachineInventoryVo getData, LoginUser loginUser) {
        String id = getData.getInventoryId();
        Query query = entityManager.createNamedQuery("TblMachineInventory.updateById");

        query.setParameter("inventoryDate", new Date());//棚卸日時
        query.setParameter("inventoryResult", Integer.parseInt(getData.getInventoryResult()));//棚卸結果
        query.setParameter("siteConfirmMethod", Integer.parseInt(getData.getSiteConfirmMethod()));//設置場所確認方法(手動/QR)
        query.setParameter("machineConfirmMethod", Integer.parseInt(getData.getMachineConfirmMethod()));//設備確認方法(手動/QR)
        query.setParameter("inputType", Integer.parseInt(getData.getInputType()));//入力区分(Tablet/Web)
        query.setParameter("remarks", getData.getRemarks());//備考
        if (null != getData.getCompanyId() && !"".equals(getData.getCompanyId())) {
            query.setParameter("companyId", getData.getCompanyId());
            query.setParameter("companyName", getData.getCompanyName());
        } else {
            query.setParameter("companyId", null);
            query.setParameter("companyName", null);
        }
        if (null != getData.getLocationId() && !"".equals(getData.getLocationId())) {
            query.setParameter("locationId", getData.getLocationId());
            query.setParameter("locationName", getData.getLocationName());
        } else {
            query.setParameter("locationId", null);
            query.setParameter("locationName", null);
        }
        if (null != getData.getInstllationSiteId() && !"".equals(getData.getInstllationSiteId())) {
            query.setParameter("instllationSiteId", getData.getInstllationSiteId());
            query.setParameter("instllationSiteName", getData.getInstllationSiteName());
        } else {
            query.setParameter("instllationSiteId", null);
            query.setParameter("instllationSiteName", null);
        }
        query.setParameter("createDate", new Date());
        query.setParameter("updateDate", new Date());
        query.setParameter("createUserUuid", loginUser.getUserUuid());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("id", id);
     
        return query.executeUpdate();
    }

    /**
     *
     * @param tblMachineInventory
     */
    @Transactional
    public void createTblMachineInventory(TblMachineInventory tblMachineInventory) {
        entityManager.persist(tblMachineInventory);
    }

    /**
     * バッチで設備棚卸テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMachineInventoryList getExtMachineInventorysByBatch(String latestExecutedDate, String machineUuid) {
        TblMachineInventoryList resList = new TblMachineInventoryList();
        List<TblMachineInventoryVo> res = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMachineInventory t join MstApiUser u on u.companyId = t.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.machineUuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMachineInventory> tmpList = query.getResultList();
        for (TblMachineInventory tblMachineInventory : tmpList) {
            TblMachineInventoryVo aRes = new TblMachineInventoryVo();
            if (null != tblMachineInventory.getMstCompany()) {
                aRes.setCompanyId(tblMachineInventory.getMstCompany().getId());
                aRes.setCompanyCode(tblMachineInventory.getMstCompany().getCompanyCode());
                aRes.setCompanyName(tblMachineInventory.getCompanyName());
            }
            aRes.setId(tblMachineInventory.getId());
            aRes.setInputType("" + tblMachineInventory.getInputType());
            if (null != tblMachineInventory.getMstInstallationSite()) {
                aRes.setInstllationSiteCode(tblMachineInventory.getMstInstallationSite().getInstallationSiteCode());
                aRes.setInstllationSiteName(tblMachineInventory.getInstllationSiteName());
            }
            if (null != tblMachineInventory.getInventoryDate()) {
                aRes.setInventoryDateStr(new FileUtil().getDateTimeFormatForStr(tblMachineInventory.getInventoryDate()));
            }
            if (null != tblMachineInventory.getInventoryDateSzt()) {
                aRes.setInventoryDateSztStr(new FileUtil().getDateTimeFormatForStr(tblMachineInventory.getInventoryDateSzt()));
            }
            aRes.setPersonName(tblMachineInventory.getPersonName());
            aRes.setInventoryResult("" + tblMachineInventory.getInventoryResult());
            if (null != tblMachineInventory.getMstLocation()) {
                aRes.setLocationCode(tblMachineInventory.getMstLocation().getLocationCode());
                aRes.setLocationName(tblMachineInventory.getLocationName());
            }
            aRes.setMachineConfirmMethod("" + tblMachineInventory.getMachineConfirmMethod());
            aRes.setMachineCreateDateStr(new FileUtil().getDateTimeFormatForStr(tblMachineInventory.getCreateDate()));
            aRes.setMachineId(tblMachineInventory.getMstMachine().getMachineId());
            aRes.setRemarks(tblMachineInventory.getRemarks());
            aRes.setSiteConfirmMethod("" + tblMachineInventory.getSiteConfirmMethod());

            if (null != tblMachineInventory.getFileType()) {
                aRes.setFileType("" + tblMachineInventory.getFileType());
            } else {
                aRes.setFileType(null);
            }
            if (null != tblMachineInventory.getImgFilePath() && !tblMachineInventory.getImgFilePath().isEmpty()) {
                aRes.setImgFilePath(tblMachineInventory.getImgFilePath());
            } else {
                aRes.setImgFilePath(null);
            }
            if (null != tblMachineInventory.getTakenDate()) {
                aRes.setTakenDate(new FileUtil().getDateTimeFormatForStr(tblMachineInventory.getTakenDate()));
            } else {
                aRes.setTakenDate(null);
            }
            if (null != tblMachineInventory.getTakenDateStz()) {
                aRes.setTakenDateStz(new FileUtil().getDateTimeFormatForStr(tblMachineInventory.getTakenDateStz()));
            } else {
                aRes.setTakenDateStz(null);
            }
            res.add(aRes);
        }
        resList.setTblMachineInventoryVos(res);
        return resList;
    }

    /**
     * バッチで設備棚卸テーブルデータを更新
     *
     * @param machineInventorys
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineInventorysByBatch(List<TblMachineInventoryVo> machineInventorys) {
        BasicResponse response = new BasicResponse();
        if (machineInventorys != null && !machineInventorys.isEmpty()) {
            for (TblMachineInventoryVo aMachineInventoryVo : machineInventorys) {
                TblMachineInventory newMachineInventory;
                List<TblMachineInventory> oldInventorys = entityManager.createQuery("SELECT t FROM TblMachineInventory t WHERE t.id=:id ")
                        .setParameter("id", aMachineInventoryVo.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null == oldInventorys || oldInventorys.isEmpty()) {
                    newMachineInventory = new TblMachineInventory();
                    newMachineInventory.setId(aMachineInventoryVo.getId());
                } else {
                    newMachineInventory = oldInventorys.get(0);
                }
                //自社の設備UUIDに変換                        
                MstMachine ownerMachine = entityManager.find(MstMachine.class, aMachineInventoryVo.getMachineId());
                if (null != ownerMachine) {
                    newMachineInventory.setMachineUuid(ownerMachine.getUuid());

                    if (null != aMachineInventoryVo.getInventoryDateStr() && !"".equals(aMachineInventoryVo.getInventoryDateStr().trim())) {
                        newMachineInventory.setInventoryDate(new FileUtil().getDateTimeParseForDate(aMachineInventoryVo.getInventoryDateStr()));
                    }
                    if (null != aMachineInventoryVo.getInventoryDateSztStr() && !"".equals(aMachineInventoryVo.getInventoryDateSztStr().trim())) {
                        newMachineInventory.setInventoryDateSzt(new FileUtil().getDateTimeParseForDate(aMachineInventoryVo.getInventoryDateSztStr()));
                    }
                    //連携しない newMachineInventory.setPersonUuid(aMachineInventory.getPersonUuid());
                    newMachineInventory.setPersonName(aMachineInventoryVo.getPersonName());
                    if (null != aMachineInventoryVo.getInventoryResult() && !aMachineInventoryVo.getInventoryResult().trim().equals("")) {
                        newMachineInventory.setInventoryResult(Integer.parseInt(aMachineInventoryVo.getInventoryResult()));
                    }
                    if (null != aMachineInventoryVo.getSiteConfirmMethod()) {
                        newMachineInventory.setSiteConfirmMethod(Integer.parseInt(aMachineInventoryVo.getSiteConfirmMethod()));
                    }
                    if (null != aMachineInventoryVo.getMachineConfirmMethod()) {
                        newMachineInventory.setMachineConfirmMethod(Integer.parseInt(aMachineInventoryVo.getMachineConfirmMethod()));
                    }
                    if (null != aMachineInventoryVo.getCompanyCode() && !aMachineInventoryVo.getCompanyCode().trim().equals("")) {
                        MstCompany com = entityManager.find(MstCompany.class, aMachineInventoryVo.getCompanyId());
                        if (null != com) {
                            newMachineInventory.setCompanyId(com.getId());
                            newMachineInventory.setCompanyName(com.getCompanyName());
                        }
                    } else {
                        newMachineInventory.setCompanyId(null);
                        newMachineInventory.setCompanyName(null);
                    }
                    if (null != aMachineInventoryVo.getLocationCode() && !aMachineInventoryVo.getLocationCode().trim().equals("")) {
                        List<MstLocation> locations = entityManager.createNamedQuery("MstLocation.findByLocationCode")
                                .setParameter("locationCode", aMachineInventoryVo.getLocationCode())
                                .setParameter("externalFlg", CommonConstants.EXTERNALFLG)
                                .getResultList();
                        if (null != locations && !locations.isEmpty()) {
                            newMachineInventory.setLocationId(locations.get(0).getId());
                            newMachineInventory.setLocationName(locations.get(0).getLocationName());
                        }
                    } else {
                        newMachineInventory.setLocationId(null);
                        newMachineInventory.setLocationName(null);
                    }
                    if (null != aMachineInventoryVo.getInstllationSiteCode() && !aMachineInventoryVo.getInstllationSiteCode().trim().equals("")) {
                        List<MstInstallationSite> installationSites = entityManager.createNamedQuery("MstInstallationSite.findByInstallationSiteCode")
                                .setParameter("installationSiteCode", aMachineInventoryVo.getInstllationSiteCode())
                                .setParameter("externalFlg", CommonConstants.EXTERNALFLG)
                                .getResultList();
                        if (null != installationSites && !installationSites.isEmpty()) {
                            newMachineInventory.setInstllationSiteId(installationSites.get(0).getId());
                            newMachineInventory.setInstllationSiteName(installationSites.get(0).getInstallationSiteName());
                        }
                    } else {
                        newMachineInventory.setInstllationSiteId(null);
                        newMachineInventory.setInstllationSiteName(null);
                    }
                    newMachineInventory.setRemarks(aMachineInventoryVo.getRemarks());
                    if (null != aMachineInventoryVo.getInputType()) {
                        newMachineInventory.setInputType(Integer.parseInt(aMachineInventoryVo.getInputType()));
                    }

                    if (null != aMachineInventoryVo.getFileType() && !aMachineInventoryVo.getFileType().isEmpty()) {
                        newMachineInventory.setFileType(Integer.parseInt(aMachineInventoryVo.getFileType()));
                    } else {
                        newMachineInventory.setFileType(null);
                    }
                    newMachineInventory.setImgFilePath(aMachineInventoryVo.getImgFilePath());
                    if (null != aMachineInventoryVo.getTakenDate()) {
                        newMachineInventory.setTakenDate(new FileUtil().getDateTimeParseForDate(aMachineInventoryVo.getTakenDate()));
                    } else {
                        newMachineInventory.setTakenDate(null);
                    }
                    if (null != aMachineInventoryVo.getTakenDateStz()) {
                        newMachineInventory.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aMachineInventoryVo.getTakenDateStz()));
                    } else {
                        newMachineInventory.setTakenDateStz(null);
                    }

                    newMachineInventory.setCreateDate(aMachineInventoryVo.getCreateDate());
                    newMachineInventory.setCreateUserUuid(aMachineInventoryVo.getCreateUserUuid());
                    newMachineInventory.setUpdateDate(new Date());
                    newMachineInventory.setUpdateUserUuid(aMachineInventoryVo.getUpdateUserUuid());
                    if (null == oldInventorys || oldInventorys.isEmpty()) {
                        entityManager.persist(newMachineInventory);
                    } else {
                        entityManager.merge(newMachineInventory);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /**
     * 棚卸基準日入力ダイアログを表示し、最新棚卸日時がそれよりも前の設備の棚卸ステータスを0にする。
     *
     * @param tblMachineInventoryList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateMachineInventory(TblMachineInventoryList tblMachineInventoryList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        Query query = entityManager.createQuery("SELECT t FROM TblMachineInventory t WHERE t.inventoryDate <= :inventoryDate");
        FileUtil fileUtil = new FileUtil();
        query.setParameter("inventoryDate", fileUtil.strDateTimeFormatToDate(tblMachineInventoryList.getInventoryStandardDate() + CommonConstants.SYS_MAX_TIME));
        List<TblMachineInventory> list = query.getResultList();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TblMachineInventory tblMachineInventory = list.get(i);
                if (tblMachineInventory.getMstMachine() != null) {
                    MstMachine mstMachine = entityManager.find(MstMachine.class, tblMachineInventory.getMstMachine().getMachineId());
                    if (mstMachine != null) {
                        mstMachine.setInventoryStatus(0);
                        mstMachine.setUpdateDate(new Date());
                        mstMachine.setUpdateUserUuid(loginUser.getUserUuid());
                        entityManager.merge(mstMachine);
                    }
                }
            }
        }
        return response;
    }
}
