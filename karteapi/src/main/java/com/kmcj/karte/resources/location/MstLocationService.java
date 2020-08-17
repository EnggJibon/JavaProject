/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.location;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompanyService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
//@Transactional
public class MstLocationService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstCompanyService mstCompanyService;
    
    @Inject
    private MstMgmtCompanyService mstMgmtCompanyService;

    /**
     *
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @return
     */
    public CountResponse getMstLocationCount(String locationCode, String locationName, String companyCode, String companyName) {
        List list = getSql(locationCode, locationName, companyCode, companyName, "count", false);
        CountResponse count = new CountResponse();
        for (Object objs : list) {
            Object[] obj = (Object[]) objs;
            count.setCount((Long) obj[0]);
        }
        return count;

    }

    /**
     *
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @param isEquals
     * @return MstLocationList
     */
    public MstLocationList getLocations(String locationCode, String locationName, String companyCode, String companyName, boolean isEquals) {
        List list = getSql(locationCode, locationName, companyCode, companyName, "", isEquals);
        MstLocationList response = new MstLocationList();
        response.setMstLocations(list);
        return response;
    }

    private List getSql(String locationCode, String locationName, String companyCode, String companyName, String action, boolean isEquals) {

        StringBuilder sql;
        String strLocationCode = "";
        String strLocationName = "";
        String strCompanyCode = "";
        String strCompanyName = "";

        if ("count".equals(action)) {
            sql = new StringBuilder("SELECT count(m.locationCode),m.mstCompany.companyCode FROM MstLocation m"
                    + " JOIN FETCH m.mstCompany"
                    + " LEFT JOIN FETCH m.mstMgmtCompany WHERE 1=1 ");
        } else {
            sql = new StringBuilder("SELECT m FROM MstLocation m"
                    + " JOIN FETCH m.mstCompany"
                    + " LEFT JOIN FETCH m.mstMgmtCompany WHERE 1=1 ");

        }

        if (locationCode != null && !"".equals(locationCode)) {
            strLocationCode = locationCode.trim();
            sql.append(" and m.locationCode LIKE :locationCode ");
        }

        if (locationName != null && !"".equals(locationName)) {
            strLocationName = locationName.trim();
            sql.append(" and m.locationName like :locationName ");
        }

        if (companyCode != null && !"".equals(companyCode)) {
            strCompanyCode = companyCode.trim();
            if (isEquals) {
                sql.append(" and m.mstCompany.companyCode = :companyCode ");
            } else {
                sql.append(" and m.mstCompany.companyCode LIKE :companyCode ");
            }
            
        }

        if (companyName != null && !"".equals(companyName)) {
            strCompanyName = companyName.trim();
            sql.append(" and m.mstCompany.companyName like :companyName ");
        }
        //2016-11-30 jiangxiaosong add start
        sql.append(" And m.externalFlg = :externalFlg ");
        //2016-11-30 jiangxiaosong add end
        sql.append(" order by m.locationCode ");//所在地コードの昇順
        Query query = entityManager.createQuery(sql.toString());

        if (locationCode != null && !"".equals(locationCode)) {
            query.setParameter("locationCode", "%" + strLocationCode + "%");
        }

        if (locationName != null && !"".equals(locationName)) {
            query.setParameter("locationName", "%" + strLocationName + "%");
        }

        if (companyCode != null && !"".equals(companyCode)) {
            if (isEquals) {
                query.setParameter("companyCode", strCompanyCode);
            } else {
                query.setParameter("companyCode", "%" + strCompanyCode + "%");
            }
        }

        if (companyName != null && !"".equals(companyName)) {
            query.setParameter("companyName", "%" + strCompanyName + "%");
        }
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end

        List list = query.getResultList();
        return list;
    }

    /**
     *
     * @param locationCode
     * @return
     */
    public MstLocation getMstLocationByCode(String locationCode) {
        Query query = entityManager.createNamedQuery("MstLocation.findByLocationCode");
        query.setParameter("locationCode", locationCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        MstLocation mstLocation = null;
        try {
            mstLocation = (MstLocation) query.getSingleResult();
            return mstLocation;
        } catch (NoResultException e) {
            return mstLocation;
        }
    }

    /**
     *
     * @param locationCode
     * @return
     */
    public boolean getMstLocationExistCheck(String locationCode) {
        Query query = entityManager.createNamedQuery("MstLocation.findByLocationCode");
        query.setParameter("locationCode", locationCode);
        //2016-12-21 14:44:30 jiangxiaosong update 
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        return query.getResultList().size() > 0;
    }

    /**
     *
     * @param locationCode
     * @return
     */
    public MstLocationList getMstLocationDetail(String locationCode) {
        Query query = entityManager.createNamedQuery("MstLocation.findByLocationCode");
        query.setParameter("locationCode", locationCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        List list = query.getResultList();
        MstLocationList response = new MstLocationList();
        response.setMstLocations(list);
        return response;
    }

    /**
     *
     * @param locationName
     * @return
     */
    public MstLocationList getLocationByName(String locationName) {
        Query query = entityManager.createNamedQuery("MstLocation.findByLocationName");
        query.setParameter("locationName", locationName);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        List list = query.getResultList();
        MstLocationList response = new MstLocationList();
        response.setMstLocations(list);
        return response;
    }

    /**
     *
     * @param locationId
     * @return
     */
    public MstLocationResp getLocationById(String locationId) {
        Query query = entityManager.createNamedQuery("MstLocation.findById");
        query.setParameter("id", locationId);
        MstLocation mstLocation = null;
        MstLocationResp mstLocationResp = new MstLocationResp();
        try {
            mstLocation = (MstLocation) query.getSingleResult();
            mstLocationResp.setMstLocation(mstLocation);
            return mstLocationResp;
        } catch (NoResultException e) {
            return mstLocationResp;
        }
    }
    
    /**
     *
     * @param locationCode
     * @return
     */
    @Transactional
    public int deleteMstLocation(String locationCode) {

        Query query = entityManager.createNamedQuery("MstLocation.delete");
        query.setParameter("locationCode", locationCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        return query.executeUpdate();
    }

    /**
     * 所在地マスターのFK依存関係チェック
     *
     * @param locationCode
     * @return
     */
    public boolean getMstLocationFKCheck(String locationCode) {

        //mst_installation_site	NO ACTION
        //mst_mold	NO ACTION
        //tbl_mold_inventory	NO ACTION
        //tbl_mold_location_history	NO ACTION
        Query query = entityManager.createNamedQuery("MstLocation.findByLocationCode");
        query.setParameter("locationCode", locationCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        boolean flg = false;
        try {
            MstLocation mstLocation = (MstLocation) query.getSingleResult();
            String locationId = mstLocation.getId();

            if (!flg) {
                //mst_installation_site	NO ACTION ok
                Query queryMstInstallationSite = entityManager.createNamedQuery("MstInstallationSite.findFkByLocationId");
                queryMstInstallationSite.setParameter("locationId", locationId);
                flg = queryMstInstallationSite.getResultList().size() > 0;
            }

            if (!flg) {
                //mst_mold	NO ACTION ok
                Query queryMstMold = entityManager.createNamedQuery("MstMold.findFkByLocationId");
                queryMstMold.setParameter("locationId", locationId);
                flg = queryMstMold.getResultList().size() > 0;
            }

            if (!flg) {
                //tbl_mold_inventory	NO ACTION ok
                Query queryTblMoldInventory = entityManager.createNamedQuery("TblMoldInventory.findFkByLocationId");
                queryTblMoldInventory.setParameter("locationId", locationId);
                flg = queryTblMoldInventory.getResultList().size() > 0;
            }

            if (!flg) {
                //tbl_mold_location_history	NO ACTION ok
                Query queryTblMoldLocationHistory = entityManager.createNamedQuery("TblMoldLocationHistory.findFkByLocationId");
                queryTblMoldLocationHistory.setParameter("locationId", locationId);
                flg = queryTblMoldLocationHistory.getResultList().size() > 0;
            }

            // 設備の関連テーブルの所在地ＦＫチェックを実装
            //mst_location	NO ACTION	LOCATION_ID	mst_machine
            if (!flg) {
                Query queryMstMachine = entityManager.createNamedQuery("MstMachine.findFkByLocationId");
                queryMstMachine.setParameter("locationId", locationId);
                flg = queryMstMachine.getResultList().size() > 0;
            }
            //mst_location	NO ACTION	LOCATION_ID	tbl_machine_location_history
            if (!flg) {
                Query queryTblMachineLocationHistory = entityManager.createNamedQuery("TblMachineLocationHistory.findFkByLocationId");
                queryTblMachineLocationHistory.setParameter("locationId", locationId);
                flg = queryTblMachineLocationHistory.getResultList().size() > 0;
            }
            //mst_location	NO ACTION	LOCATION_ID	tbl_machine_inventory
            if (!flg) {
                Query queryTblMachineInventory = entityManager.createNamedQuery("TblMachineInventory.findFkByLocationId");
                queryTblMachineInventory.setParameter("locationId", locationId);
                flg = queryTblMachineInventory.getResultList().size() > 0;
            }
        } catch (NoResultException e) {
            // nothing
        }
        return flg;

    }

    /**
     *
     * @param mstLocation
     */
    @Transactional
    public void createMstLocation(MstLocation mstLocation) {
        entityManager.persist(mstLocation);
    }

    /**
     *
     * @param mstLocation
     * @return
     */
    @Transactional
    public int updateMstLocationByQuery(MstLocation mstLocation) {
        Query query = entityManager.createNamedQuery("MstLocation.updateByLocationCode");
        query.setParameter("locationCode", mstLocation.getLocationCode());
        query.setParameter("locationName", mstLocation.getLocationName());
        query.setParameter("zipCode", mstLocation.getZipCode());
        query.setParameter("address", mstLocation.getAddress());
        query.setParameter("telNo", mstLocation.getTelNo());
        query.setParameter("companyId", mstLocation.getCompanyId());
        query.setParameter("updateDate", mstLocation.getUpdateDate());
        query.setParameter("updateUserUuid", mstLocation.getUpdateUserUuid());
        //2016-12-2 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        query.setParameter("mgmtCompanyCode",mstLocation.getMgmtCompanyCode());//20170607 Apeng add
        //2016-12-2 jiangxiaosong add end
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     *
     * @param locationCode
     * @param locationName
     * @param companyCode
     * @param companyName
     * @param loginUser
     * @return
     */
    public FileReponse getMstLocationsOutputCsv(String locationCode,
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

        String outLocationCode = mstDictionaryService.getDictionaryValue(langId, "location_Code");
        String outLocationyName = mstDictionaryService.getDictionaryValue(langId, "location_Name");
        String outZipCode = mstDictionaryService.getDictionaryValue(langId, "zip_code");
        String outAddress = mstDictionaryService.getDictionaryValue(langId, "address");
        String outTelNo = mstDictionaryService.getDictionaryValue(langId, "tel_no");
        String outCompanyCode = mstDictionaryService.getDictionaryValue(langId, "company_code");
        String outCompanyName = mstDictionaryService.getDictionaryValue(langId, "company_name");
        String outMgmtCompanyCode = mstDictionaryService.getDictionaryValue(langId, "mgmt_company_code");//20170607 Apeng add
        String outMgmtCompanyName = mstDictionaryService.getDictionaryValue(langId, "mgmt_company_name");//20170710 Apeng add
        String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");
        /*Head*/
        HeadList.add(outLocationCode);
        HeadList.add(outLocationyName);
        HeadList.add(outZipCode);
        HeadList.add(outAddress);
        HeadList.add(outTelNo);
        HeadList.add(outCompanyCode);
        HeadList.add(outCompanyName);
        HeadList.add(outMgmtCompanyCode);//20170607 Apeng add
        HeadList.add(outMgmtCompanyName);//20170607 Apeng add
        HeadList.add(delete);
        gLineList.add(HeadList);

        //明細データを取得
        List list = getSql(
                locationCode,
                locationName,
                companyCode,
                companyName,
                "",
                false);
        MstLocationList response = new MstLocationList();
        response.setMstLocations(list);

        for (int i = 0; i < response.getMstLocations().size(); i++) {
            lineList = new ArrayList();
            MstLocation mstLocation = response.getMstLocations().get(i);
            lineList.add(mstLocation.getLocationCode());
            lineList.add(mstLocation.getLocationName());
            lineList.add(mstLocation.getZipCode());
            lineList.add(mstLocation.getAddress());
            lineList.add(mstLocation.getTelNo());
            lineList.add(mstLocation.getMstCompany().getCompanyCode());
            lineList.add(mstLocation.getMstCompany().getCompanyName());
            lineList.add(mstLocation.getMgmtCompanyCode());//20170607 Apeng add
            if(mstLocation.getMstMgmtCompany() != null) {//20170710 Apeng add
                lineList.add(mstLocation.getMstMgmtCompany().getMgmtCompanyName());
            } else {
                lineList.add("");
            }
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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_LOCATION);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_LOCATION);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_location");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;

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
    public boolean checkCsvFileData(Map<String, String> logParm,String lineCsv[], String userLangId, String logFile, int index) {

        //ログ出力内容を用意する
        String lineNo = logParm.get("lineNo");
        String locationCode = logParm.get("locationCode");
        String locationName = logParm.get("locationName");
        String zipCode = logParm.get("zipCode");
        String address = logParm.get("address");
        String telNo = logParm.get("telNo");
        String companyCode = logParm.get("companyCode");
        String mgmtCompanyCode = logParm.get("mgmtCompanyCode");

        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String notFound = logParm.get("notFound");
        String maxLangth = logParm.get("maxLangth");
        String layout = logParm.get("layout");

        int arrayLength = lineCsv.length;
        FileUtil fu = new FileUtil();
        if (arrayLength != 10) { //20170607 Apeng update
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, locationCode, "", error, 1, errorContents, layout));
            return false;
        }
        //分割した文字をObjectに格納する
        String strLocationCode = lineCsv[0].trim();
        if (fu.isNullCheck(strLocationCode)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, locationCode, strLocationCode, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strLocationCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, locationCode, strLocationCode, error, 1, errorContents, maxLangth));
            return false;
        }

        String strLocationName = lineCsv[1].trim();
        if (fu.isNullCheck(strLocationName)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, locationName, strLocationName, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strLocationName, 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, locationName, strLocationName, error, 1, errorContents, maxLangth));
            return false;
        }

        String strZipCode = lineCsv[2].trim();
        if (fu.maxLangthCheck(strZipCode, 15)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, zipCode, strZipCode, error, 1, errorContents, maxLangth));
            return false;
        }
        String strAddress = lineCsv[3].trim();
        if (fu.maxLangthCheck(strAddress, 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, address, strAddress, error, 1, errorContents, maxLangth));
            return false;
        }
        String strTelNo = lineCsv[4].trim();
        if (fu.maxLangthCheck(strTelNo, 25)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, telNo, strTelNo, error, 1, errorContents, maxLangth));
            return false;
        }
        //分割した文字をObjectに格納する
        String strCompanyCode = lineCsv[5].trim();
        if (fu.isNullCheck(strCompanyCode)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, companyCode, strCompanyCode, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strCompanyCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, companyCode, strCompanyCode, error, 1, errorContents, maxLangth));
            return false;
        } else {
            MstCompany mstCompany = mstCompanyService.getMstCompanyByCode(strCompanyCode);
            if (mstCompany == null) {
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, companyCode, strCompanyCode, error, 1, errorContents, notFound));
                return false;
            }
        }
        
        //20170607 Apeng add
        String strMgmtCompanyCode = lineCsv[7].trim();
        if(StringUtils.isNotEmpty(strMgmtCompanyCode)) {
            if (fu.maxLangthCheck(strMgmtCompanyCode, 100)) {
                //エラー情報をログファイルに記入
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, mgmtCompanyCode, strMgmtCompanyCode, error, 1, errorContents, maxLangth));
                return false;
             } else if(!mstMgmtCompanyService.getSingleMstMgmtCompany(strMgmtCompanyCode)){
                fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, mgmtCompanyCode, strMgmtCompanyCode, error, 1, errorContents, notFound));
                return false;
             }
        }
        return true;
    }

    /**
     * バッチで外部所在地マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstLocationVo getExtLocationsByBatch(String latestExecutedDate) {
        MstLocationVo resVo = new MstLocationVo();
        List<MstLocationVo> resList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstLocation t WHERE 1=1 and t.externalFlg = 0 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstLocation> tmpList = query.getResultList();
        for (MstLocation mstLocation : tmpList) {
            MstLocationVo aVo = new MstLocationVo();
            if (null != mstLocation.getMstCompany()){
                aVo.setCompanyId(mstLocation.getMstCompany().getId());
                aVo.setCompanyCode(mstLocation.getMstCompany().getCompanyCode());
            }            
            mstLocation.setMstCompany(null);
            mstLocation.setMstInstallationSiteCollection(null);
            mstLocation.setMstMoldCollection(null);
            mstLocation.setTblMoldInventoryCollection(null);
            mstLocation.setTblMoldLocationHistoryCollection(null);
            aVo.setMstLocation(mstLocation);
            resList.add(aVo);
        }
        resVo.setMstLocationVos(resList);
        return resVo;
    }

    /**
     * バッチで所在地マスタデータを更新、ＤＢを更新する
     *
     * @param locationVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtLocationsByBatch(List<MstLocationVo> locationVos) {
        BasicResponse response = new BasicResponse();

        if (null != locationVos && !locationVos.isEmpty()) {
            for (MstLocationVo aLocationVo : locationVos) {
                MstLocation newLocation;
                
                List<MstLocation> oldLocations = entityManager.createQuery("SELECT t FROM MstLocation t WHERE 1=1 and t.id=:id and t.externalFlg = 1 ")
                        .setParameter("id", aLocationVo.getMstLocation().getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldLocations && !oldLocations.isEmpty()) {
                    newLocation = oldLocations.get(0);
                } else {
                    newLocation = new MstLocation();
                }
                
                if (null != aLocationVo.getCompanyCode() && !aLocationVo.getCompanyCode().trim().equals("")) {
//                    List<MstCompany> coms = entityManager.createNamedQuery("MstCompany.findByCompanyCode")
//                            .setParameter("companyCode", aLocationVo.getCompanyCode())
//                            .setParameter("externalFlg", CommonConstants.EXTERNALFLG)
//                            .getResultList();
//                    if (null != coms && !coms.isEmpty()) {
//                        newLocation.setCompanyId(coms.get(0).getId());
                    MstCompany com = entityManager.find(MstCompany.class, aLocationVo.getCompanyId());
                    if (null != com) {
                        newLocation.setCompanyId(com.getId());
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }

                newLocation.setExternalFlg(1);
                newLocation.setLocationCode(aLocationVo.getMstLocation().getLocationCode());
                newLocation.setLocationName(aLocationVo.getMstLocation().getLocationName());
                newLocation.setZipCode(aLocationVo.getMstLocation().getZipCode());
                newLocation.setAddress(aLocationVo.getMstLocation().getAddress());
                newLocation.setTelNo(aLocationVo.getMstLocation().getTelNo());                
                
                newLocation.setCreateDate(aLocationVo.getMstLocation().getCreateDate());
                newLocation.setCreateUserUuid(aLocationVo.getMstLocation().getCreateUserUuid());
                newLocation.setUpdateDate(aLocationVo.getMstLocation().getUpdateDate());
                newLocation.setUpdateUserUuid(aLocationVo.getMstLocation().getUpdateUserUuid());
                
//                newLocation.setMgmtCompanyCode(aLocationVo.getMgmtCompanyCode());//20170607 Apeng add
                if (null != oldLocations && !oldLocations.isEmpty()) {
                    entityManager.merge(newLocation);//更新
                } else {
                    newLocation.setId(aLocationVo.getMstLocation().getId());//追加
                    entityManager.persist(newLocation);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
