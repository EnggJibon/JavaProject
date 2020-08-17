/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.installationsite;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCLIAutoComplete;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author admin
 */
@Dependent
//@Transactional
public class MstInstallationSiteService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     *
     * @param installationSiteCode
     * @param installationSiteName
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @param isEquals
     * @return
     */
    public MstInstallationSiteList getMstInstallationSites(String installationSiteCode,
            String installationSiteName,
            String locationCode,
            String locationName,
            String companyCode,
            String companyName,
            boolean isEquals
    ) {
        List list = getSql(installationSiteCode,
                installationSiteName,
                locationCode,
                locationName,
                companyCode,
                companyName,
                "",
                isEquals
        );
        MstInstallationSiteList response = new MstInstallationSiteList();
        response.setMstInstallationSites(list);
        return response;

    }

    /**
     *
     * @param installationSiteName
     * @return
     */
    public MstInstallationSiteList getMstInstallationSitesByName(String installationSiteName) {
        Query query = entityManager.createNamedQuery("MstInstallationSite.findByInstallationSiteName");
        query.setParameter("installationSiteName", installationSiteName);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        List list = query.getResultList();
        MstInstallationSiteList mstInstallationSiteList = new MstInstallationSiteList();
        mstInstallationSiteList.setMstInstallationSites(list);
        return mstInstallationSiteList;
    }
    
    /**
     *
     * @param installationSiteId
     * @return
     */
    public MstInstallationSiteResp getInstallationSiteById(String installationSiteId) {
        Query query = entityManager.createNamedQuery("MstInstallationSite.findById");
        query.setParameter("id", installationSiteId);
        MstInstallationSite mstInstallationSite = null;
        MstInstallationSiteResp mstInstallationSiteResp = new MstInstallationSiteResp();
        try {
            mstInstallationSite = (MstInstallationSite) query.getSingleResult();
            mstInstallationSiteResp.setMstInstallationSite(mstInstallationSite);
            return mstInstallationSiteResp;
        } catch (NoResultException e) {
            return mstInstallationSiteResp;
        }
    }

    /**
     *
     * @param installationSiteCode
     * @param installationSiteName
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @return
     */
    public CountResponse getMstInstallationSiteCount(String installationSiteCode,
            String installationSiteName,
            String locationCode,
            String locationName,
            String companyCode,
            String companyName) {
        List list = getSql(installationSiteCode,
                installationSiteName,
                locationCode,
                locationName, companyCode,
                companyName,
                "count",
                false
        );
        CountResponse count = new CountResponse();
        for (Object objs : list) {
            Object[] obj = (Object[]) objs;
            count.setCount((Long) obj[0]);
        }
        return count;
    }

    private List getSql(String installationSiteCode,
            String installationSiteName,
            String locationCode,
            String locationName,
            String companyCode,
            String companyName,
            String action,
            boolean isEquals
    ) {
        StringBuilder sql;
        String sqlInstallationSiteCode = "";
        String sqlInstallationSiteName = "";
        String sqlLocationCode = "";
        String sqlLocationName = "";
        String sqlCompanyCode = "";
        String sqlCompanyName = "";

        if ("count".equals(action)) {

            sql = new StringBuilder("SELECT count(m.installationSiteCode),"
                    + " m.mstLocation.locationCode,m.mstLocation.mstCompany.companyCode"
                    + " FROM MstInstallationSite m LEFT JOIN FETCH m.mstLocation  "
                    + " LEFT JOIN FETCH m.mstLocation.mstCompany WHERE 1=1");
        } else {
            sql = new StringBuilder("SELECT m FROM MstInstallationSite m "
                    + "LEFT JOIN FETCH m.mstLocation LEFT JOIN FETCH m.mstLocation.mstCompany WHERE 1=1");

        }

        if (installationSiteCode != null && !"".equals(installationSiteCode)) {
            sqlInstallationSiteCode = installationSiteCode.trim();
            sql = sql.append(" and m.installationSiteCode LIKE :installationSiteCode ");
        }
        if (installationSiteName != null && !"".equals(installationSiteName)) {
            sqlInstallationSiteName = installationSiteName.trim();
            sql = sql.append(" and m.installationSiteName like :installationSiteName ");
        }
        if (locationCode != null && !"".equals(locationCode)) {
            sqlLocationCode = locationCode.trim();
            if (isEquals) {
                sql.append(" and m.mstLocation.locationCode = :locationCode ");
            } else {
                sql.append(" and m.mstLocation.locationCode LIKE :locationCode ");
            }
            
        }

        if (locationName != null && !"".equals(locationName)) {
            sqlLocationName = locationName.trim();
            sql.append(" and m.mstLocation.locationName like :locationName ");
        }
        if (companyCode != null && !"".equals(companyCode)) {
            sqlCompanyCode = companyCode.trim();
            if (isEquals) {
                sql = sql.append(" and m.mstLocation.mstCompany.companyCode = :companyCode ");
            } else {
                sql = sql.append(" and m.mstLocation.mstCompany.companyCode LIKE :companyCode ");
            }
        }
        if (companyName != null && !"".equals(companyName)) {
            sqlCompanyName = companyName.trim();
            sql = sql.append(" and m.mstLocation.mstCompany.companyName like :companyName ");
        }
        
        //2016-11-30 jiangxiaosong add start
        sql = sql.append(" And m.externalFlg = :externalFlg ");
        //2016-11-30 jiangxiaosong add end

        sql = sql.append(" Order by m.installationSiteCode ");//設置場所コードの昇順

        Query query = entityManager.createQuery(sql.toString());

        if (installationSiteCode != null && !"".equals(installationSiteCode)) {
            query.setParameter("installationSiteCode", "%" + sqlInstallationSiteCode + "%");
        }
        if (installationSiteName != null && !"".equals(installationSiteName)) {
            query.setParameter("installationSiteName", "%" + sqlInstallationSiteName + "%");
        }
        if (locationCode != null && !"".equals(locationCode)) {
            if (isEquals) {
                query.setParameter("locationCode", sqlLocationCode);
            } else {
                query.setParameter("locationCode", "%" + sqlLocationCode + "%");
            }
        }

        if (locationName != null && !"".equals(locationName)) {
            query.setParameter("locationName", "%" + sqlLocationName + "%");
        }
        if (companyCode != null && !"".equals(companyCode)) {
            if (isEquals) {
                query.setParameter("companyCode", sqlCompanyCode);
            } else {
                query.setParameter("companyCode", "%" + sqlCompanyCode + "%");
            }
        }
        if (companyName != null && !"".equals(companyName)) {
            query.setParameter("companyName", "%" + sqlCompanyName + "%");
        }
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        List list = query.getResultList();
        return list;

    }

    /**
     *
     * @param installationSiteCode
     * @return
     */
    public boolean getMstInstallationSiteExistCheck(String installationSiteCode) {
        Query query = entityManager.createNamedQuery("MstInstallationSite.findByInstallationSiteCode");
        query.setParameter("installationSiteCode", installationSiteCode);
        //2016-12-21 14:50:19 jiangxiaosong update 
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        return query.getResultList().size() > 0;
    }

    /**
     *
     * @param installationSiteCode
     * @return
     */
    public MstInstallationSiteList getMstInstallationSiteDetail(String installationSiteCode) {
        Query query = entityManager.createNamedQuery("MstInstallationSite.findByInstallationSiteCode");
        query.setParameter("installationSiteCode", installationSiteCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        List list = query.getResultList();
        MstInstallationSiteList response = new MstInstallationSiteList();
        response.setMstInstallationSites(list);
        return response;
    }

    /**
     *
     * @param mstInstallationSite
     */
    @Transactional
    public void createMstInstallationSite(MstInstallationSite mstInstallationSite) {
        entityManager.persist(mstInstallationSite);
    }

    /**
     *
     * @param installationSiteCode
     * @return
     */
    @Transactional
    public int deleteMstInstallationSite(String installationSiteCode) {

        Query query = entityManager.createNamedQuery("MstInstallationSite.delete");
        query.setParameter("installationSiteCode", installationSiteCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        return query.executeUpdate();
    }

    /**
     * 設置場所マスターのFK依存関係チェック
     *
     * @param installationSiteCode
     * @return
     */
    public boolean getMstInstallationSiteFKCheck(String installationSiteCode) {

        //mst_mold	NO ACTION
        //tbl_mold_inventory	NO ACTION
        //tbl_mold_location_history	NO ACTION
        Query query = entityManager.createNamedQuery("MstInstallationSite.findByInstallationSiteCode");
        query.setParameter("installationSiteCode", installationSiteCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        boolean flg = false;
        try {
            MstInstallationSite mstInstallationSite = (MstInstallationSite) query.getSingleResult();
            String installationSiteId = mstInstallationSite.getId();
            
            if (!flg) {
                //mst_mold	NO ACTION ok
                Query queryMstMold = entityManager.createNamedQuery("MstMold.findFkByInstallationSiteId");
                queryMstMold.setParameter("instllationSiteId", installationSiteId);
                flg = queryMstMold.getResultList().size() > 0;
            }

            if (!flg) {
                //tbl_mold_inventory	NO ACTION ok
                Query queryTblMoldInventory = entityManager.createNamedQuery("TblMoldInventory.findFkByInstallationSiteId");
                queryTblMoldInventory.setParameter("instllationSiteId", installationSiteId);
                flg = queryTblMoldInventory.getResultList().size() > 0;
            }

            if (!flg) {
                //tbl_mold_location_history	NO ACTION ok
                Query queryTblMoldLocationHistory = entityManager.createNamedQuery("TblMoldLocationHistory.findFkByInstallationSiteId");
                queryTblMoldLocationHistory.setParameter("instllationSiteId", installationSiteId);
                flg = queryTblMoldLocationHistory.getResultList().size() > 0;
            }

            // 設備の関連テーブルの設置場所ＦＫチェックを実装
            //mst_installation_site	NO ACTION	INSTLLATION_SITE_ID	mst_machine
            if (!flg) {
                Query queryMstMachine = entityManager.createNamedQuery("MstMachine.findFkByInstallationSiteId");
                queryMstMachine.setParameter("instllationSiteId", installationSiteId);
                flg = queryMstMachine.getResultList().size() > 0;
            }
            //mst_installation_site	NO ACTION	INSTLLATION_SITE_ID	tbl_machine_inventory
            if (!flg) {
                Query queryTblMachineInventory = entityManager.createNamedQuery("TblMachineInventory.findFkByInstallationSiteId");
                queryTblMachineInventory.setParameter("instllationSiteId", installationSiteId);
                flg = queryTblMachineInventory.getResultList().size() > 0;
            }
            //mst_installation_site	NO ACTION	INSTLLATION_SITE_ID	tbl_machine_location_history
            if (!flg) {
                Query queryTblMachineLocationHistory = entityManager.createNamedQuery("TblMachineLocationHistory.findFkByInstallationSiteId");
                queryTblMachineLocationHistory.setParameter("instllationSiteId", installationSiteId);
                flg = queryTblMachineLocationHistory.getResultList().size() > 0;
            }
        } catch (NoResultException e) {
            // nothing
        }
        return flg;

    }

    /**
     *
     * @param mstInstallationSite
     * @return
     */
    @Transactional
    public int updateMstInstallationSiteByQuery(MstInstallationSite mstInstallationSite) {
        Query query = entityManager.createNamedQuery("MstInstallationSite.updateByInstallationSiteCode");
        query.setParameter("installationSiteCode", mstInstallationSite.getInstallationSiteCode());
        query.setParameter("installationSiteName", mstInstallationSite.getInstallationSiteName());
        query.setParameter("locationId", mstInstallationSite.getLocationId());
        query.setParameter("updateDate", mstInstallationSite.getUpdateDate());
        query.setParameter("updateUserUuid", mstInstallationSite.getUpdateUserUuid());
        //2016-12-02 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-02 jiangxiaosong add end
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     *
     * @param installationSiteCode
     * @param installationSiteName
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @param loginUser
     * @return
     */
    public FileReponse getMstInstallationSitesOutputCsv(String installationSiteCode,
            String installationSiteName,
            String locationCode,
            String locationName,
            String companyCode,
            String companyName,
            LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        String outCompanyCode = mstDictionaryService.getDictionaryValue(langId, "company_code");
        String outCompanyName = mstDictionaryService.getDictionaryValue(langId, "company_name");
        String outLocationCode = mstDictionaryService.getDictionaryValue(langId, "location_code");
        String outLocationName = mstDictionaryService.getDictionaryValue(langId, "location_name");
        String outInstallationSiteCode = mstDictionaryService.getDictionaryValue(langId, "installation_site_code");
        String outInstallationSiteName = mstDictionaryService.getDictionaryValue(langId, "installation_site_name");
        String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");
        /*Head*/

        HeadList.add(outInstallationSiteCode);
        HeadList.add(outInstallationSiteName);
        HeadList.add(outLocationCode);
        HeadList.add(outLocationName);
        HeadList.add(outCompanyCode);
        HeadList.add(outCompanyName);
        HeadList.add(delete);
        gLineList.add(HeadList);

        //明細データを取得
        List list = getSql(installationSiteCode,
                installationSiteName,
                locationCode,
                locationName,
                companyCode,
                companyName,
                "",
                false
        );
        MstInstallationSiteList response = new MstInstallationSiteList();
        response.setMstInstallationSites(list);

        for (int i = 0; i < response.getMstInstallationSites().size(); i++) {
            lineList = new ArrayList();
            MstInstallationSite mstinstallationSite = response.getMstInstallationSites().get(i);
            lineList.add(mstinstallationSite.getInstallationSiteCode());
            lineList.add(mstinstallationSite.getInstallationSiteName());
            lineList.add(mstinstallationSite.getMstLocation().getLocationCode());
            lineList.add(mstinstallationSite.getMstLocation().getLocationName());
            lineList.add(mstinstallationSite.getMstLocation().getMstCompany().getCompanyCode());
            lineList.add(mstinstallationSite.getMstLocation().getMstCompany().getCompanyName());
            lineList.add("");
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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_INSTALLATION_SITE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_INSTALLATION_SITE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_installation_site");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean getFkByLocationId(String id) {

        Query query = entityManager.createNamedQuery("MstLocation.findById");
        query.setParameter("id", id);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * CSVの中身に対してチェックを行う
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
        String lineNo = logParm.get("lineNo");
        String installationSiteCode = logParm.get("installationSiteCode");
        String installationSiteName = logParm.get("installationSiteName");
        String locationId = logParm.get("locationId");

        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String maxLangth = logParm.get("maxLangth");

        FileUtil fu = new FileUtil();

        //分割した文字をObjectに格納する
        String strInstallationSiteCode = lineCsv[0].trim();
        if (fu.isNullCheck(strInstallationSiteCode)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, installationSiteCode, strInstallationSiteCode, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strInstallationSiteCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, installationSiteCode, strInstallationSiteCode, error, 1, errorContents, maxLangth));
            return false;
        }

        String strInstallationSiteName = lineCsv[1].trim();
        if (fu.isNullCheck(strInstallationSiteName)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, installationSiteName, strInstallationSiteName, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strInstallationSiteName, 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, installationSiteName, strInstallationSiteName, error, 1, errorContents, maxLangth));
            return false;
        }

        String strLocationCode = lineCsv[2].trim();
        if (fu.isNullCheck(strLocationCode)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, locationId, strLocationCode, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strLocationCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, locationId, strLocationCode, error, 1, errorContents, maxLangth));
            return false;
        }

        return true;
    }

    /**
     * バッチで設置場所マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstInstallationSiteList getExtInstallationSitesByBatch(String latestExecutedDate) {
        MstInstallationSiteList resList = new MstInstallationSiteList();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstInstallationSite t WHERE 1=1 and t.externalFlg = 0 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstInstallationSite> tmpInstallationSitesList = query.getResultList();
        for (MstInstallationSite mstInstallationSite : tmpInstallationSitesList) {
            mstInstallationSite.setMstLocation(null);
            mstInstallationSite.setMstMoldCollection(null);
            mstInstallationSite.setTblMoldInventoryCollection(null);
            mstInstallationSite.setTblMoldLocationHistoryCollection(null);
        }
        resList.setMstInstallationSites(tmpInstallationSitesList);
        return resList;
    }

    /**
     * バッチで設置場所マスタデータを更新、ＤＢを更新する
     *
     * @param installationSites
     * @return
     */
    @Transactional
    public BasicResponse updateExtInstallationSitesByBatch(List<MstInstallationSite> installationSites) {
        BasicResponse response = new BasicResponse();

        if (null != installationSites && !installationSites.isEmpty()) {
            for (MstInstallationSite aInstallationSite : installationSites) {
                MstInstallationSite newInstallationSite;
                List<MstInstallationSite> oldInstallationSites = entityManager.createQuery("SELECT t FROM MstInstallationSite t WHERE 1=1 and t.id=:id and t.externalFlg = 1 ")
                        .setParameter("id", aInstallationSite.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldInstallationSites && !oldInstallationSites.isEmpty()) {
                    newInstallationSite = oldInstallationSites.get(0);
                } else {
                    newInstallationSite = new MstInstallationSite();
                }
                
                MstLocation location = entityManager.find(MstLocation.class, aInstallationSite.getLocationId());
                if (null != location) {
                    newInstallationSite.setExternalFlg(1);
                    newInstallationSite.setInstallationSiteCode(aInstallationSite.getInstallationSiteCode());
                    newInstallationSite.setInstallationSiteName(aInstallationSite.getInstallationSiteName());
                    newInstallationSite.setLocationId(aInstallationSite.getLocationId());
                    newInstallationSite.setCreateDate(aInstallationSite.getCreateDate());
                    newInstallationSite.setCreateUserUuid(aInstallationSite.getCreateUserUuid());
                    newInstallationSite.setUpdateDate(aInstallationSite.getUpdateDate());
                    newInstallationSite.setUpdateUserUuid(aInstallationSite.getUpdateUserUuid());

                    if (null != oldInstallationSites && !oldInstallationSites.isEmpty()) {
                        entityManager.merge(newInstallationSite);//更新
                    } else {
                        newInstallationSite.setId(aInstallationSite.getId());//追加 
                        entityManager.persist(newInstallationSite);
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
     * 所在３点セット制御 順番
     * @param companyId
     * @param locationId
     * @return 
     */
    public MstCLIAutoComplete getMstCLIAutoComplete(String companyId,String locationId){
        
        MstCLIAutoComplete mstCLIAutoComplete = new MstCLIAutoComplete();
        List<MstLocation> mstCLIAutoCompleteLocationList = new ArrayList<>();
        List<MstInstallationSite> mstCLIAutoCompleteMstInstallationSiteList = new ArrayList<>();
        if(companyId != null && !"".equals(companyId)){
            //所在地マスタ複数取得
            String sql = " SELECT m FROM MstLocation m WHERE m.companyId = :companyId And m.externalFlg = :externalFlg Order By m.locationCode";
            Query query = entityManager.createQuery(sql);
            query.setParameter("companyId", companyId);
            query.setParameter("externalFlg", CommonConstants.MINEFLAG);
            List list = query.getResultList();
            String locationIds = "";
            for(int i=0;i<list.size();i++){
                MstLocation mstLocation = (MstLocation)list.get(i);
                MstLocation newLocation = new MstLocation();
                newLocation.setId(mstLocation.getId() == null ? "" : mstLocation.getId());
                newLocation.setLocationCode(mstLocation.getLocationCode() == null ? "" : mstLocation.getLocationCode());
                newLocation.setLocationName(mstLocation.getLocationName() == null ? "" : mstLocation.getLocationName());
                newLocation.setMgmtCompanyCode(mstLocation.getMgmtCompanyCode() == null ? "" : mstLocation.getMgmtCompanyCode());//20170725 Apeng add
                mstCLIAutoCompleteLocationList.add(newLocation);
                locationIds = locationIds + "'" + mstLocation.getId() + "',";
            }
            mstCLIAutoComplete.setMstCLIAutoCompleteLocation(mstCLIAutoCompleteLocationList);
            if(locationIds != null && !"".equals(locationIds)){
                locationIds = locationIds.substring(0, locationIds.length()-1);
            }
            if (list != null && list.size() > 0) {
                String newsql = "SELECT m FROM MstInstallationSite m WHERE m.locationId in ("+locationIds+") And m.externalFlg = :externalFlg Order By m.installationSiteCode";
                Query newquery = entityManager.createQuery(newsql);
                newquery.setParameter("externalFlg", CommonConstants.MINEFLAG);
                List newlist = newquery.getResultList();
                for (int i = 0; i < newlist.size(); i++) {
                    MstInstallationSite mstInstallationSite = (MstInstallationSite) newlist.get(i);
                    MstInstallationSite newInstallationSite = new MstInstallationSite();
                    newInstallationSite.setId(mstInstallationSite.getId() == null ? "" : mstInstallationSite.getId());
                    newInstallationSite.setInstallationSiteCode(mstInstallationSite.getInstallationSiteCode() == null ? "" : mstInstallationSite.getInstallationSiteCode());
                    newInstallationSite.setInstallationSiteName(mstInstallationSite.getInstallationSiteName() == null ? "" : mstInstallationSite.getInstallationSiteName());
                    mstCLIAutoCompleteMstInstallationSiteList.add(newInstallationSite);
                }
            }
            
            mstCLIAutoComplete.setMstCLIAutoCompleteInstallationSite(mstCLIAutoCompleteMstInstallationSiteList);
        }
        
        if(locationId != null && !"".equals(locationId)){
            //設置場所マスタ複数取得
            String newsql = "SELECT m FROM MstInstallationSite m WHERE m.locationId = :locationId And m.externalFlg = :externalFlg Order By m.installationSiteCode";
            Query newquery = entityManager.createQuery(newsql);
            newquery.setParameter("locationId", locationId);
            newquery.setParameter("externalFlg", CommonConstants.MINEFLAG);
            List newlist = newquery.getResultList();
            for(int i=0;i<newlist.size();i++){
                MstInstallationSite mstInstallationSite = (MstInstallationSite)newlist.get(i);MstInstallationSite newInstallationSite = new MstInstallationSite();
                newInstallationSite.setId(mstInstallationSite.getId() == null ? "" : mstInstallationSite.getId());
                newInstallationSite.setInstallationSiteCode(mstInstallationSite.getInstallationSiteCode() == null ? "" : mstInstallationSite.getInstallationSiteCode());
                newInstallationSite.setInstallationSiteName(mstInstallationSite.getInstallationSiteName() == null ? "" : mstInstallationSite.getInstallationSiteName());
                mstCLIAutoCompleteMstInstallationSiteList.add(newInstallationSite);
            }
            mstCLIAutoComplete.setMstCLIAutoCompleteInstallationSite(mstCLIAutoCompleteMstInstallationSiteList);
        }
        return mstCLIAutoComplete;
    }
    
    /**
     * 所在３点セット制御 逆
     * @param locationId
     * @param installationSiteId
     * @param loginUser
     * @return 
     */
    public MstCLIAutoComplete getMstCLIAutoCompleteDown(String locationId, String installationSiteId, LoginUser loginUser) {

        MstCLIAutoComplete mstCLIAutoComplete = new MstCLIAutoComplete();
        List<MstLocation> mstLocationList = new ArrayList<>();
        List<MstCompany> mstCompanyList = new ArrayList<>();
        List<MstInstallationSite> mstInstallationSiteList = new ArrayList<>();

        if (installationSiteId != null && !"".equals(installationSiteId)) {
            StringBuilder sql = new StringBuilder(" SELECT m FROM MstInstallationSite m WHERE 1=1 ");
            sql.append(" And m.id = :installationSiteId And m.externalFlg = :externalFlg Order By m.installationSiteCode");
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("installationSiteId", installationSiteId);
            query.setParameter("externalFlg", CommonConstants.MINEFLAG);
            try {
                MstInstallationSite mstInstallationSite = (MstInstallationSite) query.getSingleResult();
                
                MstLocation location = mstInstallationSite.getMstLocation();
                MstLocation inLocation = new MstLocation();
                inLocation.setId(location.getId() == null ? "" : location.getId());
                mstLocationList.add(inLocation);
                mstCLIAutoComplete.setMstCLIAutoCompleteLocation(mstLocationList);

                MstCompany company = mstInstallationSite.getMstLocation().getMstCompany();
                MstCompany inCompany = new MstCompany();
                inCompany.setId(company.getId() == null ? "" : company.getId());
                inCompany.setCompanyCode(company.getCompanyCode() == null ? "" : company.getCompanyCode());
                inCompany.setCompanyName(company.getCompanyName() == null ? "" : company.getCompanyName());
                inCompany.setMgmtCompanyCode(company.getMgmtCompanyCode() == null ? "" : company.getMgmtCompanyCode());//20170725 Apeng add
                mstCompanyList.add(company);
                mstCLIAutoComplete.setMstCLIAutoCompleteCompany(mstCompanyList);

                String companyId = mstInstallationSite.getMstLocation().getMstCompany().getId();

                String sqlCompany = " SELECT m FROM MstLocation m WHERE m.companyId = :companyId And m.externalFlg = :externalFlg Order By m.locationCode";
                Query queryCompany = entityManager.createQuery(sqlCompany);
                queryCompany.setParameter("companyId", companyId);
                queryCompany.setParameter("externalFlg", CommonConstants.MINEFLAG);
                List list = queryCompany.getResultList();
                for (int i = 0; i < list.size(); i++) {
                    MstLocation mstLocation = (MstLocation) list.get(i);
                    MstLocation newLocation = new MstLocation();
                    newLocation.setId(mstLocation.getId() == null ? "" : mstLocation.getId());
                    newLocation.setLocationCode(mstLocation.getLocationCode() == null ? "" : mstLocation.getLocationCode());
                    newLocation.setLocationName(mstLocation.getLocationName() == null ? "" : mstLocation.getLocationName());
                    newLocation.setMgmtCompanyCode(mstLocation.getMgmtCompanyCode() == null ? "" : mstLocation.getMgmtCompanyCode());//20170725 Apeng add
                    mstLocationList.add(newLocation);
                }
                mstCLIAutoComplete.setMstCLIAutoCompleteLocation(mstLocationList);
                if (list != null && list.size() > 0) {
                    String newsql = "SELECT m FROM MstInstallationSite m WHERE m.locationId = :locationId And m.externalFlg = :externalFlg Order By m.installationSiteCode";
                    Query newquery = entityManager.createQuery(newsql);
                    newquery.setParameter("locationId", location.getId());
                    newquery.setParameter("externalFlg", CommonConstants.MINEFLAG);
                    List newlist = newquery.getResultList();
                    for (int i = 0; i < newlist.size(); i++) {
                        MstInstallationSite newMstInstallationSite = (MstInstallationSite) newlist.get(i);
                        MstInstallationSite newInstallationSite = new MstInstallationSite();
                        newInstallationSite.setId(newMstInstallationSite.getId() == null ? "" : newMstInstallationSite.getId());
                        newInstallationSite.setInstallationSiteCode(newMstInstallationSite.getInstallationSiteCode() == null ? "" : newMstInstallationSite.getInstallationSiteCode());
                        newInstallationSite.setInstallationSiteName(newMstInstallationSite.getInstallationSiteName() == null ? "" : newMstInstallationSite.getInstallationSiteName());
                        mstInstallationSiteList.add(newInstallationSite);
                    }
                }
                mstCLIAutoComplete.setMstCLIAutoCompleteInstallationSite(mstInstallationSiteList);
            } catch (NoResultException e) {
                mstCLIAutoComplete.setError(true);
                mstCLIAutoComplete.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstCLIAutoComplete.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            }
        }

        if (locationId != null && !"".equals(locationId)) {
            String sqlCompany = " SELECT m FROM MstLocation m WHERE m.id = :locationId And m.externalFlg = :externalFlg Order By m.locationCode";
            Query queryCompany = entityManager.createQuery(sqlCompany);
            queryCompany.setParameter("locationId", locationId);
            queryCompany.setParameter("externalFlg", CommonConstants.MINEFLAG);
            try {
                MstLocation location = (MstLocation) queryCompany.getSingleResult();
                MstCompany mstCompany = location.getMstCompany();
                MstCompany newMstCompany = new MstCompany();
                newMstCompany.setId(mstCompany.getId());
                newMstCompany.setCompanyCode(mstCompany.getCompanyCode());
                newMstCompany.setCompanyName(mstCompany.getCompanyName());
                newMstCompany.setMgmtCompanyCode(mstCompany.getMgmtCompanyCode() == null ? "" : mstCompany.getMgmtCompanyCode());//20170725 Apeng add
                mstCompanyList.add(newMstCompany);
                mstCLIAutoComplete.setMstCLIAutoCompleteCompany(mstCompanyList);

                String sql = " SELECT m FROM MstLocation m WHERE m.companyId = :companyId And m.externalFlg = :externalFlg Order By m.locationCode";
                Query query = entityManager.createQuery(sql);
                query.setParameter("companyId", mstCompany.getId());
                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                List list = query.getResultList();
                for (int i = 0; i < list.size(); i++) {
                    MstLocation mstLocation = (MstLocation) list.get(i);
                    MstLocation newLocation = new MstLocation();
                    newLocation.setId(mstLocation.getId() == null ? "" : mstLocation.getId());
                    newLocation.setLocationCode(mstLocation.getLocationCode() == null ? "" : mstLocation.getLocationCode());
                    newLocation.setLocationName(mstLocation.getLocationName() == null ? "" : mstLocation.getLocationName());
                    newLocation.setMgmtCompanyCode(mstLocation.getMgmtCompanyCode() == null ? "" : mstLocation.getMgmtCompanyCode());//20170725 Apeng add
                    mstLocationList.add(newLocation);
                }
                mstCLIAutoComplete.setMstCLIAutoCompleteLocation(mstLocationList);
                if (list != null && list.size() > 0) {
                    String newsql = "SELECT m FROM MstInstallationSite m WHERE m.locationId = :locationId And m.externalFlg = :externalFlg Order By m.installationSiteCode";
                    Query newquery = entityManager.createQuery(newsql);
                    newquery.setParameter("locationId", locationId);
                    newquery.setParameter("externalFlg", CommonConstants.MINEFLAG);
                    List newlist = newquery.getResultList();
                    for (int i = 0; i < newlist.size(); i++) {
                        MstInstallationSite mstInstallationSite = (MstInstallationSite) newlist.get(i);
                        MstInstallationSite newInstallationSite = new MstInstallationSite();
                        newInstallationSite.setId(mstInstallationSite.getId() == null ? "" : mstInstallationSite.getId());
                        newInstallationSite.setInstallationSiteCode(mstInstallationSite.getInstallationSiteCode() == null ? "" : mstInstallationSite.getInstallationSiteCode());
                        newInstallationSite.setInstallationSiteName(mstInstallationSite.getInstallationSiteName() == null ? "" : mstInstallationSite.getInstallationSiteName());
                        mstInstallationSiteList.add(newInstallationSite);
                    }
                }
                mstCLIAutoComplete.setMstCLIAutoCompleteInstallationSite(mstInstallationSiteList);

            } catch (NoResultException e) {
                mstCLIAutoComplete.setError(true);
                mstCLIAutoComplete.setErrorCode(ErrorMessages.E201_APPLICATION);
                mstCLIAutoComplete.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            }
        }
        return mstCLIAutoComplete;
    }

    /**
     * カスタマイズ,保管場所コンボボックスに設定用。
     *
     * @return
     */
    public MstInstallationSiteList loadComboBoxInstallationSiteList() {
        StringBuilder sql = new StringBuilder("SELECT m.id,m.installationSiteName FROM MstInstallationSite m "
                + "  WHERE 1=1 ");
        sql = sql.append(" And m.externalFlg = 0 ORDER BY m.installationSiteName ");
        Query query = entityManager.createQuery(sql.toString());
        List list = query.getResultList();

        MstInstallationSiteList response = new MstInstallationSiteList();
        List<MstInstallationSite> mstInstallationSites = new ArrayList();
        MstInstallationSite mstInstallationSite;
        for (int i = 0; i < list.size(); i++) {
            Object[] objs = (Object[]) list.get(i);
            mstInstallationSite = new MstInstallationSite();
            mstInstallationSite.setId(String.valueOf(objs[0]));
            mstInstallationSite.setInstallationSiteName(String.valueOf(objs[1]));
            mstInstallationSites.add(mstInstallationSite);
        }

        response.setMstInstallationSites(mstInstallationSites);
        return response;
    }
}
