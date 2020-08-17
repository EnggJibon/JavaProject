/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inventory;

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
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistory;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistoryService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.TimezoneConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class TblMoldInventoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private TblMoldLocationHistoryService tblMoldLocationHistoryService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private TblInventoryDetailService tblInventoryDetailService;

    @Inject
    private TblInventoryRequestDetailService tblInventoryRequestDetailService;

    private final static Map<String, String> orderKey;
    static {
        orderKey = new HashMap<>();
        orderKey.put("moldId", " ORDER BY t0.moldId ");// 金型ＩＤ
        orderKey.put("moldName", " ORDER BY t0.moldName ");// 金型名称
        orderKey.put("ownerCompanyName", " ORDER BY t2.companyName ");// 所有会社名称
        orderKey.put("companyName", " ORDER BY tc2.companyName ");// 会社名称
        orderKey.put("locationName", " ORDER BY mstloc.locationName ");// 所在地名称
        orderKey.put("instllationSiteName", " ORDER BY mstins.installationSiteName ");// 設置場所名称  
        orderKey.put("inventoryDate", " ORDER BY t1.inventoryDate ");// 棚卸日
        orderKey.put("status", " ORDER BY t0.status ");// ステータス
        orderKey.put("department", " ORDER BY t1.department ");// 所属
        orderKey.put("departmentChange", " ORDER BY t1.departmentChange ");// 所属
        orderKey.put("barcodeReprint", " ORDER BY t1.barcodeReprint ");
        orderKey.put("assetDamaged", " ORDER BY t1.assetDamaged ");
        orderKey.put("notInUse", " ORDER BY t1.notInUse ");
        orderKey.put("personName", " ORDER BY t1.personName ");
        orderKey.put("moldInstalledDate", " ORDER BY t0.installedDate ");// 設置日
    }
    
    public TblMoldInventoryList getMoldsByPage(String moldId, String moldName, String ownerCompanyName, String companyName, String locationName, String instllationSiteName, 
            String latestInventoryDateStart, String latestInventoryDateEnd, List<Integer> status, String depart, LoginUser loginUser, String sidx, String sord, int pageNumber, int pageSize, 
            int stocktakeIncomplete, boolean isPage) {
        //List<TblMoldInventorys> tblMoldInventorysList = new ArrayList();
        TblMoldInventorys paraTblMoldInventorys = new TblMoldInventorys(moldId, moldName, ownerCompanyName, companyName, locationName, instllationSiteName, 
                latestInventoryDateStart, latestInventoryDateEnd, null);
        paraTblMoldInventorys.setDepartment(depart);
        List<TblMoldInventorys> tblMoldInventorysList = new ArrayList();
        TblMoldInventoryList tblMoldInvList = new TblMoldInventoryList();
        if(isPage){
            Query query = getTblMoldInvQuery(paraTblMoldInventorys, "query", loginUser, sidx, sord, pageNumber, pageSize, stocktakeIncomplete, true, status);
            
            Pager pager = new Pager();
            tblMoldInvList.setPageNumber(pageNumber);
            long counts = (long) query.getResultList().size();
            tblMoldInvList.setCount(counts);
            tblMoldInvList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        }
        Query query = getTblMoldInvQuery(paraTblMoldInventorys, "query", loginUser, sidx, sord, pageNumber, pageSize, stocktakeIncomplete, false, status);
        Pager pager = new Pager();
        query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
        Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_user.department"});
        List list = query.getResultList();
        FileUtil fu = new FileUtil();
//        Query query = getTblMoldInventoryTypedQuery(paraMoldInventorys, "count", loginUser);
        for (int i = 0; i < list.size(); i++) {
            TblMoldInventorys aTblMoldInventorys = new TblMoldInventorys();
            Object[] objs = (Object[]) list.get(i);
            String strMoldUuid = String.valueOf(objs[0]);
            String strMoldId = String.valueOf(objs[1]);
            String strMoldName = String.valueOf(objs[2]);
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
            String strPersonName = null == objs[20] ? "" : String.valueOf(objs[20]);
            
            aTblMoldInventorys.setMoldUuid(strMoldUuid);
            aTblMoldInventorys.setMoldId(strMoldId);
            aTblMoldInventorys.setMoldName(strMoldName);
            aTblMoldInventorys.setMoldInstalledDate(strInstalledDate);
//            aTblMoldInventorys.setStatus(strStatus);
            aTblMoldInventorys.setStatus(strStatusName);
            aTblMoldInventorys.setOwnerCompanyId(strOwnerCompanyId);
            aTblMoldInventorys.setOwnerCompanyName(strOwnerCompanyName);
            aTblMoldInventorys.setInventoryDate(strInventoryDate);
            aTblMoldInventorys.setCompanyId(strCompanyId);
            aTblMoldInventorys.setCompanyName(strCompanyName);
            aTblMoldInventorys.setLocationId(strLocationId);
            aTblMoldInventorys.setLocationName(strLocationName);
            aTblMoldInventorys.setInstllationSiteId(strInstllationSiteId);
            aTblMoldInventorys.setInstllationSiteName(strInstllationSiteName);

            aTblMoldInventorys.setDepartmentChange(strDepartmentChange);

            aTblMoldInventorys.setDepartment(map.get("mst_user.department" + strDepartment));

            aTblMoldInventorys.setBarcodeReprint(strBarcodeReprint);

            aTblMoldInventorys.setAssetDamaged(strAssetDamaged);

            aTblMoldInventorys.setNotInUse(strNotInUse);
            
            aTblMoldInventorys.setPersonName(strPersonName);
            
            MstMold mstMold = entityManager.find(MstMold.class, strMoldId);
            if (mstMold.getInventoryStatus() == 0) {
                aTblMoldInventorys.setDepartmentChange("");
                aTblMoldInventorys.setDepartment("");
                aTblMoldInventorys.setBarcodeReprint("");
                aTblMoldInventorys.setAssetDamaged("");
                aTblMoldInventorys.setNotInUse("");
                aTblMoldInventorys.setStatus("");    
                aTblMoldInventorys.setInventoryDate("");
                aTblMoldInventorys.setPersonName("");
                aTblMoldInventorys.setPersonUuid("");
            }
            tblMoldInventorysList.add(aTblMoldInventorys);
        }
        tblMoldInvList.setTblMoldInventorys(tblMoldInventorysList);
        query.setMaxResults(pageSize);
        return tblMoldInvList;
    }
    
    public Query getTblMoldInvQuery(TblMoldInventorys paraMoldInventorys, String type, LoginUser loginUser, String sidx, String sord, int pageNumber, int pageSize,
            int stocktakeIncomplete, boolean isPage, List<Integer> status) {
        StringBuilder sql = new StringBuilder();
        if ("query".equals(type) || "detail".equals(type)) {
            sql.append(" SELECT t0.uuid,t0.moldId,t0.moldName,t0.ownerCompanyId,t0.installedDate,t0.companyId,");
            sql.append(" tc2.companyName,t0.locationId,mstloc.locationName,t0.instllationSiteId,mstins.installationSiteName,");
            sql.append(" t0.status,t1.inventoryDate,t2.companyName AS ownerCompanyName,t3.choice AS statusName ");
            //部署変更(0:無,1:有)
            //部署
            //資産シール(0:無,1:有)
            //故障(0:無,1:有)
            //遊休(0:F,1:T)
            sql.append(" ,t1.departmentChange, t1.department, t1.barcodeReprint, t1.assetDamaged, t1.notInUse, t1.personName");

            sql.append(" FROM MstMold t0  ");//金型マスタ
            sql.append(" INNER JOIN TblMoldInventory t1 ");//金型棚卸テーブル
            sql.append(" ON t0.uuid = t1.moldUuid ");
            sql.append(" AND t0.latestInventoryId = t1.id ");//金型マスタ.最新棚卸ID
            sql.append(" LEFT OUTER JOIN MstLocation mstloc ");
            sql.append(" ON t0.locationId = mstloc.id ");
            sql.append(" LEFT OUTER JOIN MstInstallationSite mstins ");
            sql.append(" ON t0.instllationSiteId = mstins.id ");
            sql.append(" LEFT OUTER JOIN MstCompany tc2 ");//会社マスタ
            sql.append(" ON t0.companyId = tc2.id ");
            sql.append(" LEFT OUTER JOIN MstCompany t2 ");//会社マスタ
            sql.append(" ON t0.ownerCompanyId = t2.id ");
            sql.append(" LEFT OUTER JOIN MstChoice t3 ");
            sql.append(" ON t0.status = t3.mstChoicePK.seq ");
            sql.append(" AND t3.mstChoicePK.category = 'mst_mold.status' ");
            sql.append(" AND t3.mstChoicePK.langId = '".concat(loginUser.getLangId()).concat("'"));
            sql.append(" WHERE 1 = 1 ");
        } else if ("count".equals(type)) {
            sql.append(" SELECT count(t0.uuid) ");
            sql.append(" FROM MstMold t0  ");//金型マスタ
            sql.append(" INNER JOIN TblMoldInventory t1 ");//金型棚卸テーブル
            sql.append(" ON t0.uuid = t1.moldUuid ");
            sql.append(" AND t0.latestInventoryId = t1.id ");//金型マスタ.最新棚卸ID
            sql.append(" LEFT OUTER JOIN MstCompany t2 ");//会社マスタ
            sql.append(" ON t0.ownerCompanyId = t2.id ");
            sql.append(" LEFT OUTER JOIN MstChoice t3 ");
            sql.append(" ON t0.status = t3.mstChoicePK.seq ");
            sql.append(" AND t3.mstChoicePK.category = 'mst_mold.status' ");
            sql.append(" AND t3.mstChoicePK.langId = '".concat(loginUser.getLangId()).concat("'"));
            sql.append(" WHERE 1 = 1 ");
        }

        if (null != paraMoldInventorys.getMoldId() && !paraMoldInventorys.getMoldId().trim().equals("")) {
            sql.append(" and t0.moldId like :moldId ");
        }
        if (null != paraMoldInventorys.getMoldName() && !paraMoldInventorys.getMoldName().trim().equals("")) {
            sql.append(" and t0.moldName like :moldName ");
        }
        if (null != paraMoldInventorys.getOwnerCompanyName() && !paraMoldInventorys.getOwnerCompanyName().trim().equals("")) {
            sql.append(" and t2.companyName like :ownerCompanyName ");
        }
        if (null != paraMoldInventorys.getCompanyName() && !paraMoldInventorys.getCompanyName().trim().equals("")) {
            sql.append(" and tc2.companyName like :companyName ");
        }
        if (null != paraMoldInventorys.getLocationName() && !paraMoldInventorys.getLocationName().trim().equals("")) {
            sql.append(" and mstloc.locationName like :locationName ");
        }
        if (null != paraMoldInventorys.getInstllationSiteName() && !paraMoldInventorys.getInstllationSiteName().trim().equals("")) {
            sql.append(" and mstins.installationSiteName like :instllationSiteName ");
        }
        if (null != paraMoldInventorys.getLatestInventoryDateStart() && !paraMoldInventorys.getLatestInventoryDateStart().trim().equals("")) {
            sql.append(" and t1.inventoryDate >= :inventoryDateStart  ");
        }

        if (null != paraMoldInventorys.getLatestInventoryDateEnd() && !paraMoldInventorys.getLatestInventoryDateEnd().trim().equals("")) {
            sql.append(" and t1.inventoryDate <= :inventoryDateEnd  ");
        }

        //最新棚卸日時の検索条件が入力されたとき、検索対象外
        if (StringUtils.isNotEmpty(paraMoldInventorys.getLatestInventoryDateStart())
                || StringUtils.isNotEmpty(paraMoldInventorys.getLatestInventoryDateEnd())) {
                sql.append(" and t0.inventoryStatus <> 0 ");
        }
        
        if(stocktakeIncomplete == 1){
            sql.append(" and t0.inventoryStatus = 0 ");
        }
        
        if (null != status && !status.isEmpty()) {
            sql.append(" and t0.status in :status ");
        }
        
        if (null != paraMoldInventorys.getPersonName()&& !paraMoldInventorys.getPersonName().trim().equals("")) {
            sql.append(" and t1.personName like :personName ");
        }
        
        if (null != paraMoldInventorys.getDepartment() && !paraMoldInventorys.getDepartment().isEmpty()) {
            sql.append(" and t0.department = :department");
        }
        if (!isPage) {
            if (StringUtils.isNotEmpty(sidx)) {
                String sortStr = orderKey.get(sidx) + " " + sord;
                // 表示順は設備IDの昇順。
                sql.append(sortStr);
            } else {
                sql.append("  order by t0.moldId ");
            }
        }
        Query query = entityManager.createQuery(sql.toString());

        if (null != paraMoldInventorys.getMoldId() && !paraMoldInventorys.getMoldId().trim().equals("")) {
            query.setParameter("moldId", "%" + paraMoldInventorys.getMoldId().trim() + "%");
        }
        if (null != paraMoldInventorys.getMoldName() && !paraMoldInventorys.getMoldName().trim().equals("")) {
            query.setParameter("moldName", "%" + paraMoldInventorys.getMoldName().trim() + "%");
        }
        if (null != paraMoldInventorys.getOwnerCompanyName() && !paraMoldInventorys.getOwnerCompanyName().trim().equals("")) {
            query.setParameter("ownerCompanyName", "%" + paraMoldInventorys.getOwnerCompanyName().trim() + "%");
        }
        if (null != paraMoldInventorys.getCompanyName() && !paraMoldInventorys.getCompanyName().trim().equals("")) {
            query.setParameter("companyName", "%" + paraMoldInventorys.getCompanyName().trim() + "%");
        }
        if (null != paraMoldInventorys.getLocationName() && !paraMoldInventorys.getLocationName().trim().equals("")) {
            query.setParameter("locationName", "%" + paraMoldInventorys.getLocationName().trim() + "%");
        }
        if (null != paraMoldInventorys.getInstllationSiteName() && !paraMoldInventorys.getInstllationSiteName().trim().equals("")) {
            query.setParameter("instllationSiteName", "%" + paraMoldInventorys.getInstllationSiteName().trim() + "%");
        }
        FileUtil fu = new FileUtil();
        if (null != paraMoldInventorys.getLatestInventoryDateStart() && !paraMoldInventorys.getLatestInventoryDateStart().trim().equals("")) {
            query.setParameter("inventoryDateStart", fu.getDateTimeParseForDate(paraMoldInventorys.getLatestInventoryDateStart() + CommonConstants.SYS_MIN_TIME));
        }

        if (null != paraMoldInventorys.getLatestInventoryDateEnd() && !paraMoldInventorys.getLatestInventoryDateEnd().trim().equals("")) {
            query.setParameter("inventoryDateEnd", fu.getDateTimeParseForDate(paraMoldInventorys.getLatestInventoryDateEnd() + CommonConstants.SYS_MAX_TIME));
        }

        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }
        if (null != paraMoldInventorys.getPersonName() && !paraMoldInventorys.getPersonName().trim().equals("")) {
            query.setParameter("personName", "%" + paraMoldInventorys.getPersonName().trim() + "%");
        }
        if (null != paraMoldInventorys.getDepartment() && !paraMoldInventorys.getDepartment().isEmpty()) {
            query.setParameter("department", Integer.parseInt(paraMoldInventorys.getDepartment()));
        }
        if (!isPage) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        return query;
    }
    
    /**
     * 金型棚卸マスタ count
     *
     * @param paraMoldInventorys
     * @param loginUser
     * @param stocktakeIncomplete
     * @return
     */
    public CountResponse getTblMoldInventoryCount(TblMoldInventorys paraMoldInventorys, LoginUser loginUser, int stocktakeIncomplete) {
        Query query = getTblMoldInventoryTypedQuery(paraMoldInventorys, "count", loginUser, stocktakeIncomplete);

        Long count = (Long) query.getSingleResult();

        CountResponse res = new CountResponse();
        res.setCount(count);
        return res;
    }

    /**
     * 金型棚卸マスタ 複数取得
     *
     * @param paraTblMoldInventorys
     * @param loginUser
     * @param stocktakeIncomplete
     * @return
     */
    public List<TblMoldInventorys> getMoldInventories(TblMoldInventorys paraTblMoldInventorys, LoginUser loginUser, int stocktakeIncomplete) {
        List<TblMoldInventorys> tblMoldInventorysList = new ArrayList();
        Query query = getTblMoldInventoryTypedQuery(paraTblMoldInventorys, "query", loginUser, stocktakeIncomplete);
        Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_user.department"});
        List list = query.getResultList();
        FileUtil fu = new FileUtil();
        /*Detail*/
        for (int i = 0; i < list.size(); i++) {
            TblMoldInventorys aTblMoldInventorys = new TblMoldInventorys();
            Object[] objs = (Object[]) list.get(i);
            String strMoldUuid = String.valueOf(objs[0]);
            String strMoldId = String.valueOf(objs[1]);
            String strMoldName = String.valueOf(objs[2]);
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
            String strPersonName = null == objs[20] ? "" : String.valueOf(objs[20]);

            aTblMoldInventorys.setMoldUuid(strMoldUuid);
            aTblMoldInventorys.setMoldId(strMoldId);
            aTblMoldInventorys.setMoldName(strMoldName);
            aTblMoldInventorys.setMoldInstalledDate(strInstalledDate);
//            aTblMoldInventorys.setStatus(strStatus);
            aTblMoldInventorys.setStatus(strStatusName);
            aTblMoldInventorys.setOwnerCompanyId(strOwnerCompanyId);
            aTblMoldInventorys.setOwnerCompanyName(strOwnerCompanyName);
            aTblMoldInventorys.setInventoryDate(strInventoryDate);
            aTblMoldInventorys.setCompanyId(strCompanyId);
            aTblMoldInventorys.setCompanyName(strCompanyName);
            aTblMoldInventorys.setLocationId(strLocationId);
            aTblMoldInventorys.setLocationName(strLocationName);
            aTblMoldInventorys.setInstllationSiteId(strInstllationSiteId);
            aTblMoldInventorys.setInstllationSiteName(strInstllationSiteName);

            aTblMoldInventorys.setDepartmentChange(strDepartmentChange);

            aTblMoldInventorys.setDepartment(map.get("mst_user.department" + strDepartment));

            aTblMoldInventorys.setBarcodeReprint(strBarcodeReprint);

            aTblMoldInventorys.setAssetDamaged(strAssetDamaged);

            aTblMoldInventorys.setNotInUse(strNotInUse);
            
            aTblMoldInventorys.setPersonName(strPersonName);

            MstMold mstMold = entityManager.find(MstMold.class, strMoldId);
            if (mstMold.getInventoryStatus() == 0) {
                aTblMoldInventorys.setDepartmentChange("");
                aTblMoldInventorys.setDepartment("");
                aTblMoldInventorys.setBarcodeReprint("");
                aTblMoldInventorys.setAssetDamaged("");
                aTblMoldInventorys.setNotInUse("");
                aTblMoldInventorys.setStatus("");    
                aTblMoldInventorys.setInventoryDate("");
                aTblMoldInventorys.setPersonName("");
            }
            
            tblMoldInventorysList.add(aTblMoldInventorys);
        }
        return tblMoldInventorysList;
    }

    /**
     * 金型棚卸履歴 画面レイアウト 金型マスタ、金型棚卸テーブルよりデータを取得し
     *
     * @param moldId
     * @param loginUser
     * @return
     */
    public TblMoldInventorys getMoldInventoriesHistories(String moldId, LoginUser loginUser) {
        FileUtil fu = new FileUtil();
        TblMoldInventorys resMoldInventorys = new TblMoldInventorys();
        List<MstComponent> components = new ArrayList<>();
        List<TblMoldInventorys> moldInventorys = new ArrayList<>();
        Query query = entityManager.createNamedQuery("MstMold.findTblMoldInventoryByid");
        query.setParameter("moldId", moldId);
        Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_user.department"});
        List list = query.getResultList();
        if (!list.isEmpty()) {
            MstMold mold = (MstMold) list.get(0);

            Iterator<MstMoldComponentRelation> iterator = mold.getMstMoldComponentRelationCollection().iterator();
            while (iterator.hasNext()) {
                MstMoldComponentRelation mr = iterator.next();
                components.add(mr.getMstComponent());
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

            List<TblMoldInventory> listMi = null;
            if (!mold.getTblMoldInventoryCollection().isEmpty()) {
                listMi = new ArrayList(mold.getTblMoldInventoryCollection());

                List<MstChoice> inventory_resultChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_mold_inventory.inventory_result").getMstChoice();
                List<MstChoice> mold_confirm_methodChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_mold_inventory.mold_confirm_method").getMstChoice();
                List<MstChoice> site_confirm_methodChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_mold_inventory.site_confirm_method").getMstChoice();
                List<MstChoice> input_typeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_mold_inventory.input_type").getMstChoice();

                for (int i = 0, j = listMi.size(); i < j; i++) {
                    TblMoldInventory mis = listMi.get(i);
                    TblMoldInventorys resMis = new TblMoldInventorys();
                    resMis.setId(mis.getId());
                    resMis.setMoldId(mold.getMoldId());
                    resMis.setMoldName(mold.getMoldName());
                    resMis.setMoldUuid(mold.getUuid());
                    resMis.setMoldInspectedDate(null == mold.getInspectedDate() ? "" : fu.getDateFormatForStr(mold.getInspectedDate()));
                    resMis.setMoldMainAssetNo(mold.getMainAssetNo());
                    resMis.setMoldCreateDate(null == mold.getMoldCreatedDate() ? "" : fu.getDateFormatForStr(mold.getMoldCreatedDate()));
                    resMis.setInventoryDate(null == mis.getInventoryDate() ? "" : fu.getDateTimeFormatForStr(mis.getInventoryDate()));

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

                    for (int mold_confirm_methodi = 0; mold_confirm_methodi < mold_confirm_methodChoiceList.size(); mold_confirm_methodi++) {
                        MstChoice aMstChoice = mold_confirm_methodChoiceList.get(mold_confirm_methodi);
                        if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(mis.getMoldConfirmMethod()))) {
                            resMis.setMoldConfirmMethod(aMstChoice.getChoice());
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

                    String barcodeReprint = String.valueOf(mis.getBarcodeReprint()==null?"":mis.getBarcodeReprint());
                    resMis.setBarcodeReprint(barcodeReprint);

                    String assetDamaged = String.valueOf(mis.getAssetDamaged()==null?"":mis.getAssetDamaged());
                    resMis.setAssetDamaged(assetDamaged);

                    String notInUse = String.valueOf(mis.getNotInUse()==null?"":mis.getNotInUse());
                    resMis.setNotInUse(notInUse);

                    
                    if (mis.getMstMold().getInventoryStatus() == 0) {
                        resMis.setDepartmentChange("");
                        resMis.setDepartment("");
                        resMis.setBarcodeReprint("");
                        resMis.setAssetDamaged("");
                        resMis.setNotInUse("");
                    }
                    moldInventorys.add(resMis);
                }
            }

            resMoldInventorys.setComponents(components);
            resMoldInventorys.setMoldInventorys(moldInventorys);

            resMoldInventorys.setMoldId(mold.getMoldId());
            resMoldInventorys.setMoldName(mold.getMoldName());

            resMoldInventorys.setStatus(mold.getStatus().toString());
            resMoldInventorys.setOwnerCompanyName(null == mold.getMstCompanyByOwnerCompanyId() ? "" : mold.getMstCompanyByOwnerCompanyId().getCompanyName());

            resMoldInventorys.setCompanyId(mold.getCompanyId());
            resMoldInventorys.setCompanyName(null == mold.getMstCompanyByCompanyId() ? "" : mold.getMstCompanyByCompanyId().getCompanyName());
            resMoldInventorys.setLocationId(mold.getLocationId());
            resMoldInventorys.setLocationName(null == mold.getMstLocation() ? "" : mold.getMstLocation().getLocationName());
            resMoldInventorys.setInstllationSiteId(mold.getInstllationSiteId());
            resMoldInventorys.setInstllationSiteName(null == mold.getMstInstallationSite() ? "" : mold.getMstInstallationSite().getInstallationSiteName());

            //externalデータがチェック 
            if (FileUtil.checkExternal(entityManager, mstDictionaryService, mold.getMoldId(), loginUser).isError()) {
                resMoldInventorys.setExternalFlg(1);
            } else {
                resMoldInventorys.setExternalFlg(0);
            }

            //externalデータがチェック 
            resMoldInventorys.setMoldUuid(mold.getUuid());
            resMoldInventorys.setInstalledDate(fu.getDateFormatForStr(mold.getInstalledDate()));
            resMoldInventorys.setMoldMainAssetNo(mold.getMainAssetNo());
            resMoldInventorys.setMoldCreateDate(null == mold.getMoldCreatedDate() ? "" : fu.getDateFormatForStr(mold.getMoldCreatedDate()));
            resMoldInventorys.setMoldInspectedDate(null == mold.getInspectedDate() ? "" : fu.getDateFormatForStr(mold.getInspectedDate()));
        }

        return resMoldInventorys;
    }

    /**
     * 金型棚卸マスタCSV出力
     *
     *
     * @param paraMoldInventorys
     * @param loginUser
     * @param stocktakeIncomplete
     * @return
     */
    public FileReponse getTblMoldInventoryOutputCsv(TblMoldInventorys paraMoldInventorys, LoginUser loginUser, int stocktakeIncomplete) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        String outMoldId = mstDictionaryService.getDictionaryValue(langId, "mold_id");
        String outMoldName = mstDictionaryService.getDictionaryValue(langId, "mold_name");
        String outLatestInventoryDate = mstDictionaryService.getDictionaryValue(langId, "latest_inventory_date");
        String outStatus = mstDictionaryService.getDictionaryValue(langId, "mold_status");
        String outPersonName = mstDictionaryService.getDictionaryValue(langId, "mold_inventory_person");
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
        headList.add(outMoldId);
        headList.add(outMoldName);
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
        List<TblMoldInventorys> resList = getMoldInventories(paraMoldInventorys, loginUser, stocktakeIncomplete);
        /*Detail*/
        for (int i = 0; i < resList.size(); i++) {
            lineList = new ArrayList();
            TblMoldInventorys aTblMoldInventorys = resList.get(i);
            lineList.add(aTblMoldInventorys.getMoldId());
            lineList.add(aTblMoldInventorys.getMoldName());
            lineList.add(aTblMoldInventorys.getInventoryDate());
            lineList.add(aTblMoldInventorys.getStatus());
            lineList.add(aTblMoldInventorys.getPersonName());
            lineList.add(aTblMoldInventorys.getDepartmentChange());//部署変更
            lineList.add(aTblMoldInventorys.getDepartment());//部署Text
            lineList.add(aTblMoldInventorys.getBarcodeReprint());//資産シール
            lineList.add(aTblMoldInventorys.getAssetDamaged());//故障
            lineList.add(aTblMoldInventorys.getNotInUse());//遊休
            lineList.add(aTblMoldInventorys.getOwnerCompanyName());
            lineList.add(aTblMoldInventorys.getMoldInstalledDate());
            lineList.add(aTblMoldInventorys.getCompanyName());
            lineList.add(aTblMoldInventorys.getLocationName());
            lineList.add(aTblMoldInventorys.getInstllationSiteName());
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
        tblCsvExport.setExportTable(CommonConstants.TBL_TBL_MOLD_INVENTORY);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_INVENTORY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mold_inventory");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * 金型棚卸 取得方法
     *
     * @param paraMoldInventorys
     * @param type： query--複数取得、count--件数取得
     * @param loginUser
     * @param stocktakeIncomplete
     * @return
     */
    public Query getTblMoldInventoryTypedQuery(TblMoldInventorys paraMoldInventorys, String type, LoginUser loginUser, int stocktakeIncomplete) {
        StringBuilder sql = new StringBuilder();
        if ("query".equals(type) || "detail".equals(type)) {
            sql.append(" SELECT t0.uuid,t0.moldId,t0.moldName,t0.ownerCompanyId,t0.installedDate,t0.companyId,");
            sql.append(" t4.companyName,t0.locationId,t5.locationName,t0.instllationSiteId,t6.installationSiteName,");
            sql.append(" t0.status,t1.inventoryDate,t2.companyName AS ownerCompanyName,t3.choice AS statusName ");
            //部署変更(0:無,1:有)
            //部署
            //資産シール(0:無,1:有)
            //故障(0:無,1:有)
            //遊休(0:F,1:T)
            sql.append(" ,t1.departmentChange, t1.department, t1.barcodeReprint, t1.assetDamaged, t1.notInUse, t1.personName");

            sql.append(" FROM MstMold t0  ");//金型マスタ
            sql.append(" INNER JOIN TblMoldInventory t1 ");//金型棚卸テーブル
            sql.append(" ON t0.uuid = t1.moldUuid ");
            sql.append(" AND t0.latestInventoryId = t1.id ");//金型マスタ.最新棚卸ID
            sql.append(" LEFT OUTER JOIN MstCompany t2 ");//会社マスタ
            sql.append(" ON t0.ownerCompanyId = t2.id ");
            sql.append(" LEFT OUTER JOIN MstChoice t3 ");
            sql.append(" ON t0.status = t3.mstChoicePK.seq ");
            sql.append(" AND t3.mstChoicePK.category = 'mst_mold.status' ");
            sql.append(" AND t3.mstChoicePK.langId = '".concat(loginUser.getLangId()).concat("'"));
            sql.append(" LEFT OUTER JOIN MstCompany t4 ");//会社マスタ
            sql.append(" ON t0.companyId = t4.id ");
            sql.append(" LEFT OUTER JOIN MstLocation t5 ");//所在地マスタ
            sql.append(" ON t0.locationId = t5.id ");
            sql.append(" LEFT OUTER JOIN MstInstallationSite t6 ");//設置場所マスタ
            sql.append(" ON t0.instllationSiteId = t6.id ");
            sql.append(" WHERE 1 = 1 ");
        } else if ("count".equals(type)) {
            sql.append(" SELECT count(t0.uuid) ");
            sql.append(" FROM MstMold t0  ");//金型マスタ
            sql.append(" INNER JOIN TblMoldInventory t1 ");//金型棚卸テーブル
            sql.append(" ON t0.uuid = t1.moldUuid ");
            sql.append(" AND t0.latestInventoryId = t1.id ");//金型マスタ.最新棚卸ID
            sql.append(" LEFT OUTER JOIN MstCompany t2 ");//会社マスタ
            sql.append(" ON t0.ownerCompanyId = t2.id ");
            sql.append(" LEFT OUTER JOIN MstChoice t3 ");
            sql.append(" ON t0.status = t3.mstChoicePK.seq ");
            sql.append(" AND t3.mstChoicePK.category = 'mst_mold.status' ");
            sql.append(" AND t3.mstChoicePK.langId = '".concat(loginUser.getLangId()).concat("'"));
            sql.append(" LEFT OUTER JOIN MstCompany t4 ");//会社マスタ
            sql.append(" ON t0.companyId = t4.id ");
            sql.append(" LEFT OUTER JOIN MstLocation t5 ");//所在地マスタ
            sql.append(" ON t0.locationId = t5.id ");
            sql.append(" LEFT OUTER JOIN MstInstallationSite t6 ");//設置場所マスタ
            sql.append(" ON t0.instllationSiteId = t6.id ");
            sql.append(" WHERE 1 = 1 ");
        }

        if (null != paraMoldInventorys.getMoldId() && !paraMoldInventorys.getMoldId().trim().equals("")) {
            sql.append(" and t0.moldId like :moldId ");
        }
        if (null != paraMoldInventorys.getMoldName() && !paraMoldInventorys.getMoldName().trim().equals("")) {
            sql.append(" and t0.moldName like :moldName ");
        }
        if (null != paraMoldInventorys.getOwnerCompanyName() && !paraMoldInventorys.getOwnerCompanyName().trim().equals("")) {
            sql.append(" and t2.companyName like :ownerCompanyName ");
        }
        if (null != paraMoldInventorys.getCompanyName() && !paraMoldInventorys.getCompanyName().trim().equals("")) {
            sql.append(" and t4.companyName like :companyName ");
        }
        if (null != paraMoldInventorys.getLocationName() && !paraMoldInventorys.getLocationName().trim().equals("")) {
            sql.append(" and t5.locationName like :locationName ");
        }
        if (null != paraMoldInventorys.getInstllationSiteName() && !paraMoldInventorys.getInstllationSiteName().trim().equals("")) {
            sql.append(" and t6.installationSiteName like :instllationSiteName ");
        }
        if (null != paraMoldInventorys.getLatestInventoryDateStart() && !paraMoldInventorys.getLatestInventoryDateStart().trim().equals("")) {
            sql.append(" and t1.inventoryDate >= :inventoryDateStart  ");
        }

        if (null != paraMoldInventorys.getLatestInventoryDateEnd() && !paraMoldInventorys.getLatestInventoryDateEnd().trim().equals("")) {
            sql.append(" and t1.inventoryDate <= :inventoryDateEnd  ");
        }

        //最新棚卸日時の検索条件が入力されたとき、検索対象外
        if (StringUtils.isNotEmpty(paraMoldInventorys.getLatestInventoryDateStart())
                || StringUtils.isNotEmpty(paraMoldInventorys.getLatestInventoryDateEnd())) {
            sql.append(" and t0.inventoryStatus <> 0 ");
        }
        
        if(stocktakeIncomplete == 1){
            sql.append(" and t0.inventoryStatus = 0 ");
        }

        if (null != paraMoldInventorys.getStatus() && !paraMoldInventorys.getStatus().trim().equals("")) {
            sql.append(" and t0.status = :status ");
        }
        
        if (null != paraMoldInventorys.getPersonName()&& !paraMoldInventorys.getPersonName().trim().equals("")) {
            sql.append(" and t1.personName like :personName ");
        }
        
        sql.append("  order by t0.moldId ");//表示順は金型IDの昇順

        Query query = entityManager.createQuery(sql.toString());

        if (null != paraMoldInventorys.getMoldId() && !paraMoldInventorys.getMoldId().trim().equals("")) {
            query.setParameter("moldId", "%" + paraMoldInventorys.getMoldId().trim() + "%");
        }
        if (null != paraMoldInventorys.getMoldName() && !paraMoldInventorys.getMoldName().trim().equals("")) {
            query.setParameter("moldName", "%" + paraMoldInventorys.getMoldName().trim() + "%");
        }
        if (null != paraMoldInventorys.getOwnerCompanyName() && !paraMoldInventorys.getOwnerCompanyName().trim().equals("")) {
            query.setParameter("ownerCompanyName", "%" + paraMoldInventorys.getOwnerCompanyName().trim() + "%");
        }
        if (null != paraMoldInventorys.getCompanyName() && !paraMoldInventorys.getCompanyName().trim().equals("")) {
            query.setParameter("companyName", "%" + paraMoldInventorys.getCompanyName().trim() + "%");
        }
        if (null != paraMoldInventorys.getLocationName() && !paraMoldInventorys.getLocationName().trim().equals("")) {
            query.setParameter("locationName", "%" + paraMoldInventorys.getLocationName().trim() + "%");
        }
        if (null != paraMoldInventorys.getInstllationSiteName() && !paraMoldInventorys.getInstllationSiteName().trim().equals("")) {
            query.setParameter("instllationSiteName", "%" + paraMoldInventorys.getInstllationSiteName().trim() + "%");
        }
        FileUtil fu = new FileUtil();
        if (null != paraMoldInventorys.getLatestInventoryDateStart() && !paraMoldInventorys.getLatestInventoryDateStart().trim().equals("")) {
            query.setParameter("inventoryDateStart", fu.getDateTimeParseForDate(paraMoldInventorys.getLatestInventoryDateStart() + CommonConstants.SYS_MIN_TIME));
        }

        if (null != paraMoldInventorys.getLatestInventoryDateEnd() && !paraMoldInventorys.getLatestInventoryDateEnd().trim().equals("")) {
            query.setParameter("inventoryDateEnd", fu.getDateTimeParseForDate(paraMoldInventorys.getLatestInventoryDateEnd() + CommonConstants.SYS_MAX_TIME));
        }

        if (null != paraMoldInventorys.getStatus() && !paraMoldInventorys.getStatus().trim().equals("")) {
//            if (!"0".equals(paraMoldInventorys.getStatus().trim())) {
            query.setParameter("status", Integer.parseInt(paraMoldInventorys.getStatus()));
//            }
        }
        
        if (null != paraMoldInventorys.getPersonName() && !paraMoldInventorys.getPersonName().trim().equals("")) {
            query.setParameter("personName", "%" + paraMoldInventorys.getPersonName().trim() + "%");
        }

        return query;
    }

    /**
     * 金型マスタの最新棚卸IDも更新。所在が変わっているとき金型マスタの所在も更新、所在履歴作成。
     *
     * @param tblMoldInventorys
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMoldInventoryHistorie(TblMoldInventorys tblMoldInventorys, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        MstMold mold = entityManager.find(MstMold.class, tblMoldInventorys.getMoldId());
        boolean isHistory = true;
        String oldCompanyId = null == mold.getCompanyId() ? "" : mold.getCompanyId();
        String oldLocationId = null == mold.getLocationId() ? "" : mold.getLocationId();
        String oldInstllationSiteId = null == mold.getInstllationSiteId() ? "" : mold.getInstllationSiteId();
        if (!oldCompanyId.equals(tblMoldInventorys.getCompanyId()) || !oldLocationId.equals(tblMoldInventorys.getLocationId()) || !oldInstllationSiteId.equals(tblMoldInventorys.getInstllationSiteId())) {
            isHistory = false;
        }

        TblMoldInventory aTblMoldInventory = new TblMoldInventory();
        aTblMoldInventory.setCreateDate(new Date());
        aTblMoldInventory.setCreateUserUuid(loginUser.getUserUuid());
        aTblMoldInventory.setUpdateDate(new Date());
        aTblMoldInventory.setUpdateUserUuid(loginUser.getUserUuid());

        String str = IDGenerator.generate();
        aTblMoldInventory.setId(str);
        aTblMoldInventory.setMoldUuid(tblMoldInventorys.getMoldUuid());
        aTblMoldInventory.setRemarks(tblMoldInventorys.getRemarks());
        aTblMoldInventory.setInventoryResult(Integer.parseInt(tblMoldInventorys.getInventoryResult()));
        FileUtil fu = new FileUtil();
        if (null != tblMoldInventorys.getInventoryDate() && !"".contains(tblMoldInventorys.getInventoryDate())) {
            SimpleDateFormat sdf = new SimpleDateFormat(FileUtil.DATETIME_FORMAT);
            try {
                aTblMoldInventory.setInventoryDate(sdf.parse(DateFormat.dateTimeFormat(sdf.parse(tblMoldInventorys.getInventoryDate()), loginUser.getJavaZoneId())));
                aTblMoldInventory.setInventoryDateSzt(sdf.parse(tblMoldInventorys.getInventoryDate()));
            } catch (ParseException ex) {
                Logger.getLogger(TblMoldInventoryService.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            aTblMoldInventory.setInventoryDate(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
            aTblMoldInventory.setInventoryDateSzt(null == tblMoldInventorys.getInventoryDate() ? new Date() : fu.getDateTimeParseForDate(tblMoldInventorys.getInventoryDate()));
        }
        Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
        userQuery.setParameter("uuid", loginUser.getUserUuid());
        try {
            MstUser user = (MstUser) userQuery.getSingleResult();
            aTblMoldInventory.setPersonName(user.getUserName());
            aTblMoldInventory.setPersonUuid(loginUser.getUserUuid());
        } catch (NoResultException e) {
            aTblMoldInventory.setPersonName(null);
            aTblMoldInventory.setPersonUuid(loginUser.getUserUuid());
        }

        aTblMoldInventory.setMoldConfirmMethod(1);
        aTblMoldInventory.setSiteConfirmMethod(1);
        aTblMoldInventory.setInputType(2);
        if (tblMoldInventorys.getCompanyId() != null && !tblMoldInventorys.getCompanyId().trim().equals("")) {
            aTblMoldInventory.setCompanyId(tblMoldInventorys.getCompanyId());
            aTblMoldInventory.setCompanyName(tblMoldInventorys.getCompanyName());
        }

        if (tblMoldInventorys.getInstllationSiteId() != null && !tblMoldInventorys.getInstllationSiteId().trim().equals("")) {
            aTblMoldInventory.setInstllationSiteId(tblMoldInventorys.getInstllationSiteId());
            aTblMoldInventory.setInstllationSiteName(tblMoldInventorys.getInstllationSiteName());
        }

        if (tblMoldInventorys.getLocationId() != null && !tblMoldInventorys.getLocationId().trim().equals("")) {
            aTblMoldInventory.setLocationId(tblMoldInventorys.getLocationId());
            aTblMoldInventory.setLocationName(tblMoldInventorys.getLocationName());
        }

        String departmentChange = FileUtil.getStringValue(tblMoldInventorys.getDepartmentChange());
        aTblMoldInventory.setDepartmentChange(Integer.parseInt("".equals(departmentChange) ? "0" : departmentChange));

        if (1 == aTblMoldInventory.getDepartmentChange()) {
            String department = FileUtil.getStringValue(tblMoldInventorys.getDepartment());
            aTblMoldInventory.setDepartment(Integer.parseInt("".equals(department) ? "0" : department));
        } else {
            aTblMoldInventory.setDepartment(mold.getDepartment());
        }

        String barcodeReprint = FileUtil.getStringValue(tblMoldInventorys.getBarcodeReprint());
        aTblMoldInventory.setBarcodeReprint(Integer.parseInt("".equals(barcodeReprint) ? "0" : barcodeReprint));

        String assetDamaged = FileUtil.getStringValue(tblMoldInventorys.getAssetDamaged());
        aTblMoldInventory.setAssetDamaged(Integer.parseInt("".equals(assetDamaged) ? "0" : assetDamaged));

        String notInUse = FileUtil.getStringValue(tblMoldInventorys.getNotInUse());
        aTblMoldInventory.setNotInUse(Integer.parseInt("".equals(notInUse) ? "0" : notInUse));

        entityManager.persist(aTblMoldInventory);
        //棚卸結果が所在不明、処分済みのとき、金型マスタのステータスを合わせて更新する
        String choiceText = "";
        String statusValue = "";
        if (aTblMoldInventory.getInventoryResult() == 1) {
            //1 == 所在確認
            statusValue = "0";
        } else {
            MstChoiceList inventoryresultChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_mold_inventory.inventory_result");
            if (null != inventoryresultChoiceList && !inventoryresultChoiceList.getMstChoice().isEmpty()) {
                for (int i = 0; i < inventoryresultChoiceList.getMstChoice().size(); i++) {
                    if (inventoryresultChoiceList.getMstChoice().get(i).getMstChoicePK().getSeq().equals("" + aTblMoldInventory.getInventoryResult())) {
                        choiceText = inventoryresultChoiceList.getMstChoice().get(i).getChoice();
                        break;
                    }
                }
            }

            MstChoiceList moldstatusChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_mold.status");
            if (null != moldstatusChoiceList && !moldstatusChoiceList.getMstChoice().isEmpty()) {
                for (int i = 0; i < moldstatusChoiceList.getMstChoice().size(); i++) {
                    if (choiceText.equals(moldstatusChoiceList.getMstChoice().get(i).getChoice())) {
                        statusValue = moldstatusChoiceList.getMstChoice().get(i).getMstChoicePK().getSeq();
                        break;
                    }
                }
            }
        }
        StringBuilder sql;
        sql = new StringBuilder("UPDATE MstMold t "
                + " SET t.latestInventoryId = :latestInventoryId,"
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
        if (null == mold.getInstalledDate() && (null != aTblMoldInventory.getCompanyId() || null != aTblMoldInventory.getLocationId() || null != aTblMoldInventory.getInstllationSiteId())) {
            sql.append(" ,t.installedDate = :installedDate  ");
        }
        if (1 == aTblMoldInventory.getDepartmentChange()) {
            sql.append(" ,t.department = :department ");
        }
        sql.append(" ,t.inventoryStatus = :inventoryStatus ");
        sql.append(" WHERE t.uuid = :moldUuid");
        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("moldUuid", tblMoldInventorys.getMoldUuid());

        query.setParameter("latestInventoryId", str);
        query.setParameter("companyId", null == tblMoldInventorys.getCompanyId() || tblMoldInventorys.getCompanyId().trim().equals("") ? null : tblMoldInventorys.getCompanyId());
        query.setParameter("companyName", null == tblMoldInventorys.getCompanyName() || tblMoldInventorys.getCompanyName().trim().equals("") ? null : tblMoldInventorys.getCompanyName());
        query.setParameter("locationId", null == tblMoldInventorys.getLocationId() || tblMoldInventorys.getLocationId().trim().equals("") ? null : tblMoldInventorys.getLocationId());
        query.setParameter("locationName", null == tblMoldInventorys.getLocationName() || tblMoldInventorys.getLocationName().trim().equals("") ? null : tblMoldInventorys.getLocationName());
        query.setParameter("instllationSiteId", null == tblMoldInventorys.getInstllationSiteId() || tblMoldInventorys.getInstllationSiteId().trim().equals("") ? null : tblMoldInventorys.getInstllationSiteId());
        query.setParameter("instllationSiteName", null == tblMoldInventorys.getInstllationSiteName() || tblMoldInventorys.getInstllationSiteName().trim().equals("") ? null : tblMoldInventorys.getInstllationSiteName());
        if (statusValue.equals("") == false) {
            if ("1".equals(notInUse)) {
                query.setParameter("status", 7);
            } else {
                query.setParameter("status", Integer.parseInt(statusValue));
            }

            query.setParameter("statusChangedDate", aTblMoldInventory.getInventoryDate());

        }
        if (null == mold.getInstalledDate() && (null != aTblMoldInventory.getCompanyId() || null != aTblMoldInventory.getLocationId() || null != aTblMoldInventory.getInstllationSiteId())) {
            query.setParameter("installedDate", new Date());
        }
        if (1 == aTblMoldInventory.getDepartmentChange()) {
            query.setParameter("department", aTblMoldInventory.getDepartment());
        }
        query.setParameter("inventoryStatus", 1);
        query.executeUpdate();

        if (isHistory == false) {
            TblMoldLocationHistory inData;
            List list = tblMoldLocationHistoryService.getMoldLocationHistoriesByMoldId(tblMoldInventorys.getMoldId());
            if (list.size() > 0) {
                if (list.get(0) != null) {
                    inData = new TblMoldLocationHistory();
                    String strId = list.get(0).toString();
                    fu = new FileUtil();
                    Date befInstalledDate = fu.getSpecifiedDayBefore(new Date());
                    inData.setEndDate(befInstalledDate);//現在の履歴は終了日に新しい設置日の前日をセットして更新する。
                    inData.setUpdateDate(new Date());
                    inData.setUpdateUserUuid(loginUser.getUserUuid());
                    inData.setId(strId);
                    //update 現在の履歴 MoldLocationHistory
                    tblMoldLocationHistoryService.updateMoldLocationHistoriesByEndDate(inData);
                }
            }
            inData = new TblMoldLocationHistory();
            inData.setId(IDGenerator.generate());
            inData.setMoldUuid(tblMoldInventorys.getMoldUuid());
            inData.setStartDate(new Date());//開始日に設置日
            inData.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付/todo

            // 棚卸という文言を選択マスタから取得する
            MstChoiceList choiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_mold.change_reason");
            //移動理由を「棚卸」に設定
            if (null != choiceList.getMstChoice() && choiceList.getMstChoice().size() > 0) {
                MstChoice mstChoice = choiceList.getMstChoice().get(0);
                inData.setChangeReason(Integer.parseInt(mstChoice.getMstChoicePK().getSeq())); //1
                inData.setChangeReasonText(mstChoice.getChoice()); //棚卸
            }

            if (tblMoldInventorys.getCompanyId() != null && !"".equals(tblMoldInventorys.getCompanyId())) {
                inData.setCompanyId(tblMoldInventorys.getCompanyId());
                inData.setCompanyName(tblMoldInventorys.getCompanyName());
            }

            if (tblMoldInventorys.getLocationId() != null && !"".equals(tblMoldInventorys.getLocationId())) {

                inData.setLocationId(tblMoldInventorys.getLocationId());
                inData.setLocationName(tblMoldInventorys.getLocationName());

            }

            if (tblMoldInventorys.getInstllationSiteId() != null && !"".equals(tblMoldInventorys.getInstllationSiteId())) {
                inData.setInstllationSiteId(tblMoldInventorys.getInstllationSiteId());
                inData.setInstllationSiteName(tblMoldInventorys.getInstllationSiteName());
            }

            inData.setCreateDate(new Date());
            inData.setCreateUserUuid(loginUser.getUserUuid());
            inData.setUpdateDate(new Date());
            inData.setUpdateUserUuid(loginUser.getUserUuid());
            //creat MoldLocationHistory(new) 新たな金型移動履歴
            tblMoldLocationHistoryService.creatMoldLocationHistories(inData);
        }
        // 金型IDで棚卸依頼IDテーブルを見る
        List<TblInventoryRequestDetail> list = tblInventoryRequestDetailService.getTblInventoryRequestMoldMachineIdExist(mold.getMoldId(), null);
        if (list != null && list.size() > 0) {
            //棚卸依頼明細テーブル\棚卸依頼明細IDテーブル現品有無更新
            tblInventoryRequestDetailService.updateTblInventoryRequestDetailExistence(list, aTblMoldInventory.getInventoryResult(), loginUser.getUserUuid(), isHistory, aTblMoldInventory.getLocationId());
        } else {
            //棚卸明細テーブル現品有無更新
            tblInventoryDetailService.updateTblInventoryDetailExistence(mold.getUuid(), null, aTblMoldInventory.getInventoryResult(), isHistory, aTblMoldInventory.getLocationId());
        }
        return basicResponse;
    }

    /**
     * 削除は最新の履歴のみ可能。金型マスタ、所在移動履歴も更新
     *
     * @param inventoryId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteMoldInventoryHistorie(String inventoryId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        TblMoldInventory tblMoldInventory = entityManager.find(TblMoldInventory.class, inventoryId);
        if (null == tblMoldInventory) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return basicResponse;
        }
        MstMold mold = tblMoldInventory.getMstMold();

        StringBuilder sql;
        sql = new StringBuilder("SELECT t FROM TblMoldInventory t WHERE t.mstMold.uuid = :moldUuid ORDER BY t.inventoryDate desc ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("moldUuid", mold.getUuid());
        query.setMaxResults(2);

        List<TblMoldInventory> tmpMoldInventorys = query.getResultList(); //lastest 2 TblMoldInventory
        if (!tmpMoldInventorys.isEmpty()) {
            if (tmpMoldInventorys.get(0).getId().equals(inventoryId) == false) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_latest_history_can_be_edited"));
                return basicResponse;
            }
            sql = new StringBuilder("UPDATE MstMold t SET "
                    + " t.latestInventoryId = :latestInventoryId,"
                    + " t.companyId = :companyId, "
                    + " t.companyName = :companyName,"
                    + " t.locationId = :locationId, "
                    + " t.locationName = :locationName, "
                    + " t.instllationSiteId = :instllationSiteId, "
                    + " t.instllationSiteName = :instllationSiteName, "
                    + " t.updateDate = :updateDate, "
                    + " t.updateUserUuid = :updateUserUuid "
                    + " WHERE t.uuid = :moldUuid");

            query = entityManager.createQuery(sql.toString());

            query.setParameter("updateDate", new Date());
            query.setParameter("updateUserUuid", loginUser.getUserUuid());
            query.setParameter("moldUuid", tblMoldInventory.getMstMold().getUuid());
            if (tmpMoldInventorys.size() == 1) {
                query.setParameter("latestInventoryId", null);
                query.setParameter("companyId", null);
                query.setParameter("companyName", null);
                query.setParameter("locationId", null);
                query.setParameter("locationName", null);
                query.setParameter("instllationSiteId", null);
                query.setParameter("instllationSiteName", null);
            } else {
                query.setParameter("latestInventoryId", tmpMoldInventorys.get(1).getId());
                query.setParameter("companyId", tmpMoldInventorys.get(1).getCompanyId());
                query.setParameter("companyName", tmpMoldInventorys.get(1).getCompanyName());
                query.setParameter("locationId", tmpMoldInventorys.get(1).getLocationId());
                query.setParameter("locationName", tmpMoldInventorys.get(1).getLocationName());
                query.setParameter("instllationSiteId", tmpMoldInventorys.get(1).getInstllationSiteId());
                query.setParameter("instllationSiteName", tmpMoldInventorys.get(1).getInstllationSiteName());
            }
            query.executeUpdate();
            query = entityManager.createNamedQuery("TblMoldInventory.removeById");
            query.setParameter("id", tmpMoldInventorys.get(0).getId());
            query.executeUpdate();
//            if (query.executeUpdate() == 0) {
//                entityManager.getTransaction().rollback();
//                basicResponse.setError(true);
//                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
//                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
//                return basicResponse;
//            }

            basicResponse.setError(false);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return basicResponse;
        } else {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return basicResponse;
        }
    }

    /**
     * このメッソードタブレット専用　修正しないでください 金型棚卸（タブレット） 金型棚卸登録/検索 金型棚卸情報を取得
     * @param moldId
     * @param companyId
     * @param ownerCompanyName
     * @param locationId
     * @param installationSiteId
     * @param department
     * @param login
     * @return
     */
    public TblMoldInventoryList getMoldInventories(String moldId, String companyId, String ownerCompanyName, String locationId, String installationSiteId,int department, LoginUser login) {

        TblMoldInventoryList tblMoldInventoryList = new TblMoldInventoryList();
        StringBuffer sql = new StringBuffer(" SELECT t FROM MstMold t "
                + "LEFT JOIN FETCH t.tblMoldInventory "
                + "LEFT JOIN FETCH t.mstCompanyByOwnerCompanyId "
                + "LEFT JOIN FETCH t.mstCompanyByCompanyId "
                + "LEFT JOIN FETCH t.mstLocation "
                + "LEFT JOIN FETCH t.mstInstallationSite "
                + "where 1=1 ");

        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" And t.moldId = :moldId ");
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("moldId", moldId);

            List list = query.getResultList();
            tblMoldInventoryList.setTblMoldInventoryList(list);
            return tblMoldInventoryList;
        }

        if (companyId != null && !"".equals(companyId)) {
            sql = sql.append(" And t.mstCompanyByCompanyId.id = :companyId ");
        } 

        if (ownerCompanyName != null && !"".equals(ownerCompanyName)) {
            sql = sql.append(" And t.mstCompanyByOwnerCompanyId.companyName = :ownerCompanyName ");
        }

        if (locationId != null && !"".equals(locationId)) {
            sql = sql.append(" And t.mstLocation.id = :locationId ");
        } 

        if (installationSiteId != null && !"".equals(installationSiteId)) {
            sql = sql.append(" And t.mstInstallationSite.id = :instllationSiteId ");
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
            query.setParameter("instllationSiteId", installationSiteId);
        }
        if (department != 0) {
            query.setParameter("department", department);
        }
        List list = query.getResultList();
        tblMoldInventoryList.setTblMoldInventoryList(list);

        return tblMoldInventoryList;
    }

    /**
     * 金型棚卸登録/登録 棚卸結果を登録
     *
     * @param tblMoldInventorys
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateMoldInventories(TblMoldInventoryList tblMoldInventorys, LoginUser loginUser) {
        TblMoldInventorys inputData;
        TblMoldInventory tblMoldInventory = new TblMoldInventory();
        boolean isFlag = true;
        BasicResponse response = new BasicResponse();
        if (tblMoldInventorys.getTblMoldInventorys() != null && tblMoldInventorys.getTblMoldInventorys().size() > 0) {
            // 棚卸という文言を選択マスタから取得する
            MstChoiceList choiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_mold.change_reason");
            MstChoiceList inventoryresultChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_mold_inventory.inventory_result");
            MstChoiceList moldstatusChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_mold.status");
            SimpleDateFormat format = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);

            for (int i = 0; i < tblMoldInventorys.getTblMoldInventorys().size(); i++) {
                inputData = tblMoldInventorys.getTblMoldInventorys().get(i);

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
                MstMold mold = entityManager.find(MstMold.class, inputData.getMoldId());
                oldCompanyId = null == (oldCompanyId = mold.getCompanyId()) ? "" : oldCompanyId;
                oldLocationId = null == (oldLocationId = mold.getLocationId()) ? "" : oldLocationId;
                oldInstllationSiteId = null == (oldInstllationSiteId = mold.getInstllationSiteId()) ? "" : oldInstllationSiteId;
//                oldStatus = mold.getStatus().toString();

                //外部データが取得 add 2017-1-16 10:28:43 jiangxiaosong
                response = FileUtil.checkExternal(entityManager, mstDictionaryService, mold.getMoldId(), loginUser);
                if (response.isError()) {
                    return response;
                }

                if (inputData != null) {

                    TblMoldLocationHistory inData = null;
                    if ("1".equals(inputData.getHisFlag())) {
                        List list = tblMoldLocationHistoryService.getMoldLocationHistoriesByMoldId(inputData.getMoldId());
                        if (list.size() > 0) {
                            String objs = (String) list.get(0);
                            if (objs != null) {
                                inData = new TblMoldLocationHistory();
                                String strId = objs;
                                FileUtil fu = new FileUtil();
                                Date befInstalledDate = fu.getSpecifiedDayBefore(new Date());
                                inData.setEndDate(befInstalledDate);//現在の履歴は終了日に新しい設置日の前日をセットして更新する。
                                inData.setUpdateDate(new Date());
                                inData.setUpdateUserUuid(loginUser.getUserUuid());
                                inData.setId(strId);
                                //update 現在の履歴 MoldLocationHistory
                                tblMoldLocationHistoryService.updateMoldLocationHistoriesByEndDate(inData);
                            }
                        }
                        inData = new TblMoldLocationHistory();
                        inData.setId(IDGenerator.generate());
                        inData.setMoldUuid(inputData.getMoldUuid());
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
                        //creat MoldLocationHistory(new) 新たな金型移動履歴
                        tblMoldLocationHistoryService.creatMoldLocationHistories(inData);
                    }

                    //金型棚卸登録 if 最新棚卸IDが存在とき　update 金型棚卸テーブル
                    boolean needNewInventory = false;
                    if (inputData.getInventoryId() != null && !"".equals(inputData.getInventoryId())) {
                        if (oldStatus.equals(inputData.getStatus())) {
                            updateTblMoldInventor(inputData, loginUser);
                        } else {
                            needNewInventory = true;
                        }
                    } else {
                        needNewInventory = true;
                    }
                    if (needNewInventory == true) {
                        tblMoldInventory = new TblMoldInventory();
                        //creat 金型棚卸テーブル  update金型マスタ.最新棚卸ID
                        String strId = IDGenerator.generate();
                        tblMoldInventory.setId(strId);//ID
                        tblMoldInventory.setMoldUuid(inputData.getMoldUuid());//金型UUID
                        tblMoldInventory.setInventoryDate(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));//棚卸日時
                        tblMoldInventory.setInventoryDateSzt(new Date());//棚卸日時(SZT)

                        Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
                        userQuery.setParameter("uuid", loginUser.getUserUuid());
                        try {
                            MstUser user = (MstUser) userQuery.getSingleResult();
                            tblMoldInventory.setPersonName(user.getUserName());//担当者名称
                            tblMoldInventory.setPersonUuid(loginUser.getUserUuid());//担当者UUID
                        } catch (NoResultException e) {
                            tblMoldInventory.setPersonName(null);
                            tblMoldInventory.setPersonUuid(loginUser.getUserUuid());
                        }

                        if (null != inputData.getInventoryResult() && !"".equals(inputData.getInventoryResult().trim())) {
                            tblMoldInventory.setInventoryResult(Integer.parseInt(inputData.getInventoryResult()));//棚卸結果
                        } else {
                            tblMoldInventory.setInventoryResult(0);//棚卸結果
                        }
                        tblMoldInventory.setSiteConfirmMethod(Integer.parseInt(inputData.getSiteConfirmMethod()));//設置場所確認方法(手動/QR)
                        tblMoldInventory.setMoldConfirmMethod(Integer.parseInt(inputData.getMoldConfirmMethod()));//金型確認方法(手動/QR)

                        if (null != inputData.getCompanyId() && !"".equals(inputData.getCompanyId())) {
                            tblMoldInventory.setCompanyId(inputData.getCompanyId());//会社ID
                            tblMoldInventory.setCompanyName(inputData.getCompanyName());//会社名称
                        }
                        if (null != inputData.getLocationId() && !"".equals(inputData.getLocationId())) {
                            tblMoldInventory.setLocationId(inputData.getLocationId());//所在地ID
                            tblMoldInventory.setLocationName(inputData.getLocationName());//所在地名称
                        }

                        if (null != inputData.getInstllationSiteId() && !"".equals(inputData.getInstllationSiteId())) {
                            tblMoldInventory.setInstllationSiteId(inputData.getInstllationSiteId());//設置場所ID
                            tblMoldInventory.setInstllationSiteName(inputData.getInstllationSiteName());//設置場所名称
                        }
                        tblMoldInventory.setRemarks(inputData.getRemarks());//備考
                        tblMoldInventory.setInputType(Integer.parseInt(inputData.getInputType()));//入力区分(Tablet/Web)

                        //2018.5.02
                        tblMoldInventory.setAssetDamaged(Integer.parseInt(inputData.getAssetDamaged()));
                        tblMoldInventory.setBarcodeReprint(Integer.parseInt(inputData.getBarcodeReprint()));
                        tblMoldInventory.setDepartment(Integer.parseInt(inputData.getDepartment()));
                        // 棚卸時に変更後部署不明にすると部署変更フラグが立たない KM-853
                        tblMoldInventory.setDepartmentChange(Integer.parseInt(inputData.getDepartmentChange()));
//                        if (null != inputData.getDepartment() && !"0".equals(inputData.getDepartment())) {
//                            tblMoldInventory.setDepartmentChange(Integer.parseInt(inputData.getDepartmentChange()));
//                        } else {
//                            tblMoldInventory.setDepartmentChange(0);
//                        }
                        // end
                        tblMoldInventory.setNotInUse(Integer.parseInt(inputData.getNotInUse()));

                        if (inputData.getImgFilePath() != null && !inputData.getImgFilePath().isEmpty()) {
                            tblMoldInventory.setImgFilePath(inputData.getImgFilePath());
                        }

                        if (inputData.getFileType() != null && !inputData.getFileType().isEmpty()) {
                            tblMoldInventory.setFileType(Integer.parseInt(inputData.getFileType()));
                        }
                        try {
                            if (inputData.getTakenDate() != null && !inputData.getTakenDate().isEmpty()) {
                                tblMoldInventory.setTakenDate(format.parse(DateFormat.dateTimeFormat(format.parse(inputData.getTakenDate()), loginUser.getJavaZoneId())));
                                tblMoldInventory.setTakenDateStz(format.parse(inputData.getTakenDate()));
                            }

                        } catch (ParseException ex) {
                            Logger.getLogger(TblMoldInventoryService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        tblMoldInventory.setCreateDate(new java.util.Date());//作成日時
                        tblMoldInventory.setCreateUserUuid(loginUser.getUserUuid());//更新日時
                        tblMoldInventory.setUpdateDate(new java.util.Date());//作成ユーザーUUID
                        tblMoldInventory.setUpdateUserUuid(loginUser.getUserUuid());//更新ユーザーUUID
                        entityManager.persist(tblMoldInventory);
                        inputData.setInventoryId(tblMoldInventory.getId());
                        inputData.setInventoryDate(DateFormat.dateFormat(tblMoldInventory.getInventoryDate(), loginUser.getLangId()));
                    }

                    if (!inputData.getInventoryResult().equals("0")) {
                        //棚卸結果が所在不明、処分済みのとき、所在確認、金型マスタのステータスを合わせて更新する
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

                            if (null != moldstatusChoiceList && !moldstatusChoiceList.getMstChoice().isEmpty()) {
                                for (int si = 0, scount = moldstatusChoiceList.getMstChoice().size(); si < scount; si++) {
                                    if (choiceText.equals(moldstatusChoiceList.getMstChoice().get(si).getChoice())) {
                                        statusValue = moldstatusChoiceList.getMstChoice().get(si).getMstChoicePK().getSeq();
                                        break;
                                    }
                                }
                            }
                        }

                        //update金型マスタ.最新棚卸ID    
                        mold.setLatestInventoryId(inputData.getInventoryId());
                        mold.setCompanyId(null == (strCompanyId = inputData.getCompanyId()) || strCompanyId.equals("") ? null : strCompanyId);
                        mold.setCompanyName(null == (strCompanyName = inputData.getCompanyName()) || strCompanyName.equals("") ? null : strCompanyName);
                        mold.setLocationId(null == (strLocationId = inputData.getLocationId()) || strLocationId.equals("") ? null : strLocationId);
                        mold.setLocationName(null == (strLocationName = inputData.getLocationName()) || strLocationName.equals("") ? null : strLocationName);
                        mold.setInstllationSiteId(null == (strInstllationSiteId = inputData.getInstllationSiteId()) || strInstllationSiteId.equals("") ? null : strInstllationSiteId);
                        mold.setInstllationSiteName(null == (strInstllationSiteName = inputData.getInstllationSiteName()) || strInstllationSiteName.equals("") ? null : strInstllationSiteName);
                        if (statusValue.equals("") == false) {
                            mold.setStatus(Integer.parseInt(statusValue));
                            mold.setStatusChangedDate(new Date());
                        }
                        if (null == mold.getInstalledDate() && ((null != strCompanyId && !"".equals(strCompanyId.trim())) || (null != strLocationId && !"".equals(strLocationId.trim())) || (null != strInstllationSiteId && !"".equals(strInstllationSiteId.trim())))) {
                            FileUtil fu = new FileUtil();
                            mold.setInstalledDate(fu.getSpecifiedDayBefore(new Date()));
                        }
                        mold.setUpdateDate(new Date());
                        mold.setUpdateUserUuid(loginUser.getUserUuid());
                        //変更後部署を小ダイアログで登録。設備マスタの部署を更新
                        mold.setDepartment(Integer.parseInt(inputData.getDepartment()));
                        mold.setInventoryStatus(1);
                        //チェックした時、設備マスタのステータスを遊休にする。
                        if(Integer.parseInt(inputData.getNotInUse())==1){
                         mold.setStatus(7);
                        }
                        entityManager.merge(mold);
//                        strStatus = mold.getStatus().toString();
                    }

                    if (!oldLocationId.equals(tblMoldInventory.getLocationId())) {
                        isFlag = false;
                    }

                    if ((null == inData || null == inData.getId())
                            && (!oldCompanyId.equals(null == strCompanyId ? "" : strCompanyId) || !oldLocationId.equals(null == strLocationId ? "" : strLocationId) || !oldInstllationSiteId.equals(null == strInstllationSiteId ? "" : strInstllationSiteId))) {
                        FileUtil fu = new FileUtil();
                        Query query = entityManager.createQuery("UPDATE TblMoldLocationHistory t SET "
                                + "t.endDate = :endDate,"
                                + "t.updateDate = :updateDate,"
                                + "t.updateUserUuid = :updateUserUuid "
                                + "WHERE t.endDate = :oldEndDate and t.moldUuid = :moldUuid ");
                        query.setParameter("endDate", fu.getSpecifiedDayBefore(new Date()));
                        query.setParameter("updateDate", new Date());
                        query.setParameter("updateUserUuid", loginUser.getUserUuid());
                        query.setParameter("oldEndDate", CommonConstants.SYS_MAX_DATE);
                        query.setParameter("moldUuid", mold.getUuid());
                        query.executeUpdate();
//                        inData.setEndDate(fu.getSpecifiedDayBefore(new Date()));
//                        inData.setUpdateDate(new Date());
//                        inData.setUpdateUserUuid(loginUser.getUserUuid());
//                        entityManager.merge(inData);

                        TblMoldLocationHistory tblMoldLocationHistory = new TblMoldLocationHistory();
                        tblMoldLocationHistory.setId(IDGenerator.generate());
                        tblMoldLocationHistory.setMoldUuid(mold.getUuid());
                        //移動理由を「棚卸」に設定
                        if (null != choiceList.getMstChoice() && choiceList.getMstChoice().size() > 1) {
                            MstChoice mstChoice = choiceList.getMstChoice().get(1);
                            tblMoldLocationHistory.setChangeReason(Integer.parseInt(mstChoice.getMstChoicePK().getSeq())); //1
                            tblMoldLocationHistory.setChangeReasonText(mstChoice.getChoice()); //棚卸
                        }
                        tblMoldLocationHistory.setCompanyName(strCompanyName);
                        tblMoldLocationHistory.setLocationName(strLocationName);
                        tblMoldLocationHistory.setInstllationSiteName(strInstllationSiteName);
                        tblMoldLocationHistory.setCreateDate(new Date());
                        tblMoldLocationHistory.setCreateUserUuid(loginUser.getUserUuid());
                        //TODO StartDate not allowed null but mstMold's inspectedDate、installedDate and createDate allowed null
                        tblMoldLocationHistory.setStartDate(null == mold.getInstalledDate() ? new Date() : mold.getInstalledDate());
                        tblMoldLocationHistory.setEndDate(CommonConstants.SYS_MAX_DATE);//終了日にシステム最大日付/todo
                        entityManager.persist(tblMoldLocationHistory);
                    }
                }
                // 金型IDで棚卸依頼IDテーブルを見る
                List<TblInventoryRequestDetail> list = tblInventoryRequestDetailService.getTblInventoryRequestMoldMachineIdExist(mold.getMoldId(), null);
                if (list != null && list.size() > 0) {
                    //棚卸依頼明細テーブル\棚卸依頼明細IDテーブル現品有無更新
                    tblInventoryRequestDetailService.updateTblInventoryRequestDetailExistence(list, tblMoldInventory.getInventoryResult(), loginUser.getUserUuid(), isFlag, tblMoldInventory.getLocationId());
                } else {
                    //棚卸明細テーブル現品有無更新
                    tblInventoryDetailService.updateTblInventoryDetailExistence(mold.getUuid(), null, tblMoldInventory.getInventoryResult(), isFlag, tblMoldInventory.getLocationId());
                }
            }
        }

        return response;
    }

    /**
     * 金型棚卸登録/登録-棚卸結果を登録 TblMoldInventor更新のため
     *
     * @param getData
     * @param loginUser
     * @return
     */
    @Transactional
    public int updateTblMoldInventor(TblMoldInventorys getData, LoginUser loginUser) {
        String id = getData.getInventoryId();
        Query query = entityManager.createNamedQuery("TblMoldInventory.updateById");

        query.setParameter("inventoryDate", new Date());//棚卸日時
        query.setParameter("inventoryResult", Integer.parseInt(getData.getInventoryResult()));//棚卸結果
        query.setParameter("siteConfirmMethod", Integer.parseInt(getData.getSiteConfirmMethod()));//設置場所確認方法(手動/QR)
        query.setParameter("moldConfirmMethod", Integer.parseInt(getData.getMoldConfirmMethod()));//金型確認方法(手動/QR)
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
     * @param tblMoldInventory
     */
    @Transactional
    public void createTblMoldInventory(TblMoldInventory tblMoldInventory) {
        entityManager.persist(tblMoldInventory);
    }

    /**
     * バッチで金型棚卸テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMoldInventoryList getExtMoldInventorysByBatch(String latestExecutedDate, String moldUuid) {
        TblMoldInventoryList resList = new TblMoldInventoryList();
        List<TblMoldInventorys> res = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMoldInventory t join MstApiUser u on u.companyId = t.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.moldUuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMoldInventory> tmpList = query.getResultList();
        for (TblMoldInventory tblMoldInventory : tmpList) {
            TblMoldInventorys aRes = new TblMoldInventorys();
            if (null != tblMoldInventory.getMstCompany()) {
                aRes.setCompanyId(tblMoldInventory.getMstCompany().getId());
                aRes.setCompanyCode(tblMoldInventory.getMstCompany().getCompanyCode());
                aRes.setCompanyName(tblMoldInventory.getCompanyName());
            }
            aRes.setId(tblMoldInventory.getId());
            aRes.setInputType("" + tblMoldInventory.getInputType());
            if (null != tblMoldInventory.getMstInstallationSite()) {
                aRes.setInstllationSiteCode(tblMoldInventory.getMstInstallationSite().getInstallationSiteCode());
                aRes.setInstllationSiteName(tblMoldInventory.getInstllationSiteName());
            }
            if (null != tblMoldInventory.getInventoryDate()) {
                aRes.setInventoryDate(new FileUtil().getDateTimeFormatForStr(tblMoldInventory.getInventoryDate()));
            }
            if (null != tblMoldInventory.getInventoryDateSzt()) {
                aRes.setInventoryDateSzt(new FileUtil().getDateTimeFormatForStr(tblMoldInventory.getInventoryDateSzt()));
            }
            aRes.setPersonName(tblMoldInventory.getPersonName());
            aRes.setInventoryResult("" + tblMoldInventory.getInventoryResult());
            if (null != tblMoldInventory.getMstLocation()) {
                aRes.setLocationCode(tblMoldInventory.getMstLocation().getLocationCode());
                aRes.setLocationName(tblMoldInventory.getLocationName());
            }
            aRes.setMoldConfirmMethod("" + tblMoldInventory.getMoldConfirmMethod());
            aRes.setMoldCreateDate(new FileUtil().getDateTimeFormatForStr(tblMoldInventory.getCreateDate()));
            aRes.setMoldId(tblMoldInventory.getMstMold().getMoldId());
            aRes.setRemarks(tblMoldInventory.getRemarks());
            aRes.setSiteConfirmMethod("" + tblMoldInventory.getSiteConfirmMethod());
            if (null != tblMoldInventory.getFileType()) {
                aRes.setFileType("" + tblMoldInventory.getFileType());
            } else {
                aRes.setFileType(null);
            }
            if (null != tblMoldInventory.getImgFilePath() && !tblMoldInventory.getImgFilePath().isEmpty()) {
                aRes.setImgFilePath(tblMoldInventory.getImgFilePath());
            } else {
                aRes.setImgFilePath(null);
            }
            if (null != tblMoldInventory.getTakenDate()) {
                aRes.setTakenDate(new FileUtil().getDateTimeFormatForStr(tblMoldInventory.getTakenDate()));
            } else {
                aRes.setTakenDate(null);
            }
            if (null != tblMoldInventory.getTakenDateStz()) {
                aRes.setTakenDateStz(new FileUtil().getDateTimeFormatForStr(tblMoldInventory.getTakenDateStz()));
            } else {
                aRes.setTakenDateStz(null);
            }
            res.add(aRes);
        }
        resList.setTblMoldInventorys(res);
        return resList;
    }

    /**
     * バッチで金型棚卸テーブルデータを更新
     *
     * @param moldInventorys
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldInventorysByBatch(List<TblMoldInventorys> moldInventorys) {
        BasicResponse response = new BasicResponse();
        if (moldInventorys != null && !moldInventorys.isEmpty()) {
            for (TblMoldInventorys aMoldInventorys : moldInventorys) {
                TblMoldInventory newMoldInventory;
                List<TblMoldInventory> oldInventorys = entityManager.createQuery("SELECT t FROM TblMoldInventory t WHERE t.id=:id ")
                        .setParameter("id", aMoldInventorys.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null == oldInventorys || oldInventorys.isEmpty()) {
                    newMoldInventory = new TblMoldInventory();
                    newMoldInventory.setId(aMoldInventorys.getId());
                } else {
                    newMoldInventory = oldInventorys.get(0);
                }
                //自社の金型UUIDに変換                        
                MstMold ownerMold = entityManager.find(MstMold.class, aMoldInventorys.getMoldId());
                if (null != ownerMold) {
                    newMoldInventory.setMoldUuid(ownerMold.getUuid());

                    if (null != aMoldInventorys.getInventoryDate()) {
                        newMoldInventory.setInventoryDate(new FileUtil().getDateTimeParseForDate(aMoldInventorys.getInventoryDate()));
                    }
                    if (null != aMoldInventorys.getInventoryDateSzt()) {
                        newMoldInventory.setInventoryDateSzt(new FileUtil().getDateTimeParseForDate(aMoldInventorys.getInventoryDateSzt()));
                    }
                    //連携しない newMoldInventory.setPersonUuid(aMoldInventory.getPersonUuid());
                    newMoldInventory.setPersonName(aMoldInventorys.getPersonName());
                    if (null != aMoldInventorys.getInventoryResult() && !aMoldInventorys.getInventoryResult().trim().equals("")) {
                        newMoldInventory.setInventoryResult(Integer.parseInt(aMoldInventorys.getInventoryResult()));
                    }
                    if (null != aMoldInventorys.getSiteConfirmMethod()) {
                        newMoldInventory.setSiteConfirmMethod(Integer.parseInt(aMoldInventorys.getSiteConfirmMethod()));
                    }
                    if (null != aMoldInventorys.getMoldConfirmMethod()) {
                        newMoldInventory.setMoldConfirmMethod(Integer.parseInt(aMoldInventorys.getMoldConfirmMethod()));
                    }
                    if (null != aMoldInventorys.getCompanyCode() && !aMoldInventorys.getCompanyCode().trim().equals("")) {
                        MstCompany com = entityManager.find(MstCompany.class, aMoldInventorys.getCompanyId());
                        if (null != com) {
                            newMoldInventory.setCompanyId(com.getId());
                            newMoldInventory.setCompanyName(com.getCompanyName());
                        }
                    } else {
                        newMoldInventory.setCompanyId(null);
                        newMoldInventory.setCompanyName(null);
                    }
                    if (null != aMoldInventorys.getLocationCode() && !aMoldInventorys.getLocationCode().trim().equals("")) {
                        List<MstLocation> locations = entityManager.createNamedQuery("MstLocation.findByLocationCode")
                                .setParameter("locationCode", aMoldInventorys.getLocationCode())
                                .setParameter("externalFlg", CommonConstants.EXTERNALFLG)
                                .getResultList();
                        if (null != locations && !locations.isEmpty()) {
                            newMoldInventory.setLocationId(locations.get(0).getId());
                            newMoldInventory.setLocationName(locations.get(0).getLocationName());
                        }
                    } else {
                        newMoldInventory.setLocationId(null);
                        newMoldInventory.setLocationName(null);
                    }
                    if (null != aMoldInventorys.getInstllationSiteCode() && !aMoldInventorys.getInstllationSiteCode().trim().equals("")) {
                        List<MstInstallationSite> installationSites = entityManager.createNamedQuery("MstInstallationSite.findByInstallationSiteCode")
                                .setParameter("installationSiteCode", aMoldInventorys.getInstllationSiteCode())
                                .setParameter("externalFlg", CommonConstants.EXTERNALFLG)
                                .getResultList();
                        if (null != installationSites && !installationSites.isEmpty()) {
                            newMoldInventory.setInstllationSiteId(installationSites.get(0).getId());
                            newMoldInventory.setInstllationSiteName(installationSites.get(0).getInstallationSiteName());
                        }
                    } else {
                        newMoldInventory.setInstllationSiteId(null);
                        newMoldInventory.setInstllationSiteName(null);
                    }
                    newMoldInventory.setRemarks(aMoldInventorys.getRemarks());
                    if (null != aMoldInventorys.getInputType()) {
                        newMoldInventory.setInputType(Integer.parseInt(aMoldInventorys.getInputType()));
                    }

                    if (null != aMoldInventorys.getFileType() && !aMoldInventorys.getFileType().isEmpty()) {
                        newMoldInventory.setFileType(Integer.parseInt(aMoldInventorys.getFileType()));
                    }

                    newMoldInventory.setImgFilePath(aMoldInventorys.getImgFilePath());
                    if (null != aMoldInventorys.getTakenDate()) {
                        newMoldInventory.setTakenDate(new FileUtil().getDateTimeParseForDate(aMoldInventorys.getTakenDate()));
                    } else {
                        newMoldInventory.setTakenDate(null);
                    }
                    if (null != aMoldInventorys.getTakenDateStz()) {
                        newMoldInventory.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aMoldInventorys.getTakenDateStz()));
                    } else {
                        newMoldInventory.setTakenDateStz(null);
                    }

                    newMoldInventory.setCreateDate(aMoldInventorys.getCreateDate());
                    newMoldInventory.setCreateUserUuid(aMoldInventorys.getCreateUserUuid());
                    newMoldInventory.setUpdateDate(new Date());
                    newMoldInventory.setUpdateUserUuid(aMoldInventorys.getUpdateUserUuid());
                    if (null == oldInventorys || oldInventorys.isEmpty()) {
                        entityManager.persist(newMoldInventory);
                    } else {
                        entityManager.merge(newMoldInventory);
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
     * @param tblMoldInventoryList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateMoldInventory(TblMoldInventoryList tblMoldInventoryList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        Query query = entityManager.createQuery("SELECT t FROM TblMoldInventory t WHERE t.inventoryDate <= :inventoryDate");
        FileUtil fileUtil = new FileUtil();
        query.setParameter("inventoryDate", fileUtil.strDateTimeFormatToDate(tblMoldInventoryList.getInventoryStandardDate() + CommonConstants.SYS_MAX_TIME));
        List<TblMoldInventory> list = query.getResultList();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TblMoldInventory tblMoldInventory = list.get(i);
                if (tblMoldInventory.getMstMold() != null) {
                    MstMold mstMold = entityManager.find(MstMold.class, tblMoldInventory.getMstMold().getMoldId());
                    if (mstMold != null) {
                        mstMold.setInventoryStatus(0);
                        mstMold.setUpdateDate(new Date());
                        mstMold.setUpdateUserUuid(loginUser.getUserUuid());
                        entityManager.merge(mstMold);
                    }
                }
            }
        }
        return response;
    }

    CountResponse getTblMoldInventoryCount(TblMoldInventorys paraMoldInventorys, LoginUser loginUser, String inventoryStatus) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
