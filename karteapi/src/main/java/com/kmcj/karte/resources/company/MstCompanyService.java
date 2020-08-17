/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.company;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompanyService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
 * @author hangju
 */
@Dependent
public class MstCompanyService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblCsvImportService tblCsvImportService;
    
    @Inject
    private MstMgmtCompanyService mstMgmtCompanyService;

    // リターン情報を初期化
    long succeededCount = 0;//成功件数
    long addedCount = 0;//追加件数
    long updatedCount = 0;//更新件数
    long failedCount = 0; //失敗件数
    long deletedCount = 0; //削除件数
    /**
     *
     * @param companyCode
     * @param companyName
     * @return
     */
    public MstCompanyList getMstCompanies(String companyCode, String companyName) {

        MstCompanyList response = new MstCompanyList();
        response.setMstCompanies(sql(companyCode, companyName, 1));
        return response;

    }

    /**
     * Sql文を用意
     *
     * @param companyCode
     * @param companyName
     * @param flag
     * @return
     */
    public List sql(String companyCode, String companyName, int flag) {
        StringBuilder sql;
        if (flag == 1) {
            sql = new StringBuilder("SELECT m FROM MstCompany m"
                    + " LEFT JOIN FETCH m.mstMgmtCompany WHERE 1=1 ");
        } else {
            sql = new StringBuilder("SELECT count(m) FROM MstCompany m"
                    + " LEFT JOIN FETCH m.mstMgmtCompany WHERE 1=1 ");
        }

        String sqlCompanyCode = "";
        String sqlCompanyName = "";
        if (companyCode != null && !"".equals(companyCode)) {
            sqlCompanyCode = companyCode.trim();
            sql = sql.append(" and m.companyCode like :companyCode ");
        }
        if (companyName != null && !"".equals(companyName)) {
            sqlCompanyName = companyName.trim();
            sql = sql.append(" and m.companyName like :companyName ");
        }

        //2016-11-30 jiangxiaosong add start
        sql = sql.append(" and m.externalFlg = :externalFlg ");
        //2016-11-30 jiangxiaosong add end

        sql = sql.append(" Order by m.companyCode ");    //companyCodeの昇順
        Query query = entityManager.createQuery(sql.toString());
        if (companyCode != null && !"".equals(companyCode)) {
            query.setParameter("companyCode", "%" + sqlCompanyCode + "%");
        }
        if (companyName != null && !"".equals(companyName)) {
            query.setParameter("companyName", "%" + sqlCompanyName + "%");
        }
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        return query.getResultList();
    }

    /**
     * CSV 出力
     * @param companyCode
     * @param companyName
     * @param loginUser
     * @return
     */
    public FileReponse getOutputCsv(String companyCode, String companyName, LoginUser loginUser) {

        // 出力へーだー準備
        ArrayList<ArrayList> gLineList = getHeaderes(loginUser.getLangId());
        //明細データを取得
        List list = sql(companyCode, companyName, 1);

        /*Detail*/
        ArrayList lineList;
        for (int i = 0; i < list.size(); i++) {
            lineList = new ArrayList();
            MstCompany mstCompany = (MstCompany) list.get(i);
            lineList.add(mstCompany.getCompanyCode());
            lineList.add(mstCompany.getCompanyName());
            lineList.add(mstCompany.getZipCode());
            lineList.add(mstCompany.getAddress());
            lineList.add(mstCompany.getTelNo());
            lineList.add(mstCompany.getMgmtCompanyCode());//20170607 Apeng add
            if(mstCompany.getMstMgmtCompany() != null) {//20170707 Apeng add
                lineList.add(mstCompany.getMstMgmtCompany().getMgmtCompanyName());
            } else {
                lineList.add("");
            }
            lineList.add(String.valueOf(mstCompany.getMyCompany()));
            lineList.add("");
            gLineList.add(lineList);
        }

        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_COMPANY);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_COMPANY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_COMPANY);

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        FileReponse fileReponse = new FileReponse();
        fileReponse.setFileUuid(uuid);
        return fileReponse;
    }

    /**
     *
     * @param companyCode
     * @param companyName
     * @return
     */
    public CountResponse getMstCompanyCount(String companyCode, String companyName) {
        List list = sql(companyCode, companyName, 0);
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    /**
     *
     * @param companyCode
     * @return
     */
    public MstCompany getMstCompanyByCode(String companyCode) {
        Query query = entityManager.createNamedQuery("MstCompany.findByCompanyCode");
        query.setParameter("companyCode", companyCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        MstCompany mstCompany = null;
        try {
            mstCompany = (MstCompany) query.getSingleResult();
            return mstCompany;
        } catch (NoResultException e) {
            return mstCompany;
        }
    }

    /**
     * 会社マスタのFK依存関係チェック
     *
     * @param companyCode
     * @return
     */
    public boolean getMstCompanyFKCheck(String companyCode) {
        //mst_api_user	NO ACTION
        //mst_external_data_get_setting	NO ACTION
        //mst_location	NO ACTION
        //mst_mold	NO ACTION
        //tbl_mold_inventory	NO ACTION
        //tbl_mold_location_history	NO ACTION
        //mst_machine  NO ACTION	NO ACTION
        //tbl_machine_location_history  NO ACTION	NO ACTION
        //tbl_machine_inventory NO ACTION	NO ACTION
        Query query = entityManager.createNamedQuery("MstCompany.findByCompanyCode");
        query.setParameter("companyCode", companyCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        boolean flg = false;

        MstCompany mstCompany = (MstCompany) query.getSingleResult();
        String companyId = mstCompany.getId();
//            mst_api_user NO ACTION ok
        if (!flg) {
            Query queryMstApiUser = entityManager.createNamedQuery("MstApiUser.findByCompanyId");
            queryMstApiUser.setParameter("companyId", companyId);
            flg = queryMstApiUser.getResultList().size() > 0;
        }

        if (!flg) {
//            mst_external_data_get_setting	NO ACTION ok
            Query queryMstEexternalDataGetSetting = entityManager.createNamedQuery("MstExternalDataGetSetting.findByCompanyId");
            queryMstEexternalDataGetSetting.setParameter("companyId", companyId);
            flg = queryMstEexternalDataGetSetting.getResultList().size() > 0;
        }

        if (!flg) {
            //mst_location	NO ACTION  ok
            Query queryMstLocation = entityManager.createNamedQuery("MstLocation.findFkByCompanyId");
            queryMstLocation.setParameter("companyId", companyId);
            flg = queryMstLocation.getResultList().size() > 0;
        }

        if (!flg) {
            //mst_mold	NO ACTION ok
            Query queryMstMold = entityManager.createNamedQuery("MstMold.findFkByCompanyId");
            queryMstMold.setParameter("companyId", companyId);
            flg = queryMstMold.getResultList().size() > 0;
        }

        if (!flg) {
            //mst_mold	NO ACTION ok
            Query queryMstMold = entityManager.createNamedQuery("MstMold.findFkByOwnerCompanyId");
            queryMstMold.setParameter("companyId", companyId);
            flg = queryMstMold.getResultList().size() > 0;
        }

        if (!flg) {
            //tbl_mold_inventory	NO ACTION ok
            Query queryTblMoldInventory = entityManager.createNamedQuery("TblMoldInventory.findFkByCompanyId");
            queryTblMoldInventory.setParameter("companyId", companyId);
            flg = queryTblMoldInventory.getResultList().size() > 0;
        }
        if (!flg) {
            //tbl_mold_location_history	NO ACTION 
            Query queryTblMoldLocationHistory = entityManager.createNamedQuery("TblMoldLocationHistory.findFkByCompanyId");
            queryTblMoldLocationHistory.setParameter("companyId", companyId);
            flg = queryTblMoldLocationHistory.getResultList().size() > 0;
        }
        
        // 設備の関連テーブルの会社ＦＫチェックを実装
        //mst_machine	NO ACTION	OWNER_COMPANY_ID
        if (!flg) {
            Query queryMstMold = entityManager.createNamedQuery("MstMachine.findFkByCompanyId");
            queryMstMold.setParameter("companyId", companyId);
            flg = queryMstMold.getResultList().size() > 0;
        }
        //mst_machine	NO ACTION	COMPANY_ID
        if (!flg) {
            Query queryMstMachine = entityManager.createNamedQuery("MstMachine.findFkByOwnerCompanyId");
            queryMstMachine.setParameter("companyId", companyId);
            flg = queryMstMachine.getResultList().size() > 0;
        }
        //tbl_machine_inventory	NO ACTION	COMPANY_ID
        if (!flg) {
            Query queryTblMachineInventory = entityManager.createNamedQuery("TblMachineInventory.findFkByCompanyId");
            queryTblMachineInventory.setParameter("companyId", companyId);
            flg = queryTblMachineInventory.getResultList().size() > 0;
        }
        //tbl_machine_location_history	NO ACTION	COMPANY_ID
        if (!flg) {
            Query queryTblMachineLocationHistory = entityManager.createNamedQuery("TblMachineLocationHistory.findFkByCompanyId");
            queryTblMachineLocationHistory.setParameter("companyId", companyId);
            flg = queryTblMachineLocationHistory.getResultList().size() > 0;
        }

        return flg;
    }

    /**
     *
     * @param companyCode
     * @return
     */
    public boolean getMstCompanyExistCheck(String companyCode) {
        Query query = entityManager.createNamedQuery("MstCompany.findByCompanyCode");
        query.setParameter("companyCode", companyCode);
        //2016-12-21 14:23:39 jiangxiaosong update
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        return query.getResultList().size() > 0;
    }
    
    /**
    *
    * @param companyCode
    * @return
    */
   public boolean getMstCompanyExistCheckById(String companyUuid) {
       Query query = entityManager.createNamedQuery("MstCompany.findByIdAndExternalFlg");
       query.setParameter("id", companyUuid);
       //2016-12-21 14:23:39 jiangxiaosong update
       query.setParameter("externalFlg", CommonConstants.MINEFLAG);
       return query.getResultList().size() > 0;
   }
    
    /**
     * 会社マスタの自社フラグ1（自社）は1レコードしか登録できない仕様としますので、登録時のチェック
     * @param companyCode
     * @return
     */
    public boolean checkMyCompanyCount(String companyCode) {
        Query query = entityManager.createNamedQuery("MstCompany.countMyCompany");
        query.setParameter("myCompany", 1);
        query.setParameter("companyCode", companyCode);
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        
        List list = query.getResultList();
        
        return ((Long) list.get(0)) >= 1;
    }

    /**
     *
     * @param companyCode
     * @return
     */
    public MstCompanyList getMstCompanyDetail(String companyCode) {
        Query query = entityManager.createNamedQuery("MstCompany.findByCompanyCode");
        query.setParameter("companyCode", companyCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        List list = query.getResultList();
        MstCompanyList response = new MstCompanyList();
        response.setMstCompanies(list);
        return response;
    }

    /**
     *
     * @param companyName
     * @return
     */
    public MstCompanyList getCompanyByName(String companyName) {
        Query query = entityManager.createNamedQuery("MstCompany.findByCompanyName");
        query.setParameter("companyName", companyName);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        List list = query.getResultList();
        MstCompanyList response = new MstCompanyList();
        response.setMstCompanies(list);
        return response;
    }
    
    /**
     *
     * @param companyId
     * @return
     */
    public MstCompanyResp getCompanyById(String companyId) {
        Query query = entityManager.createNamedQuery("MstCompany.findById");
        query.setParameter("id", companyId);
        MstCompany mstCompany =  null;
        MstCompanyResp mstCompanyResp = new MstCompanyResp();
        try {
            mstCompany = (MstCompany) query.getSingleResult();
            mstCompanyResp.setMstCompany(mstCompany);
            return mstCompanyResp;
        } catch (NoResultException e) {
            return mstCompanyResp;
        }
    }

    /**
     *
     * @param companyCode
     * @return
     */
    @Transactional
    public int deleteMstCompany(String companyCode) {

        Query query = entityManager.createNamedQuery("MstCompany.delete");
        query.setParameter("companyCode", companyCode);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        return query.executeUpdate();
    }

    /**
     *
     * @param mstCompany
     */
    @Transactional
    public void createMstCompany(MstCompany mstCompany) {
        entityManager.persist(mstCompany);
    }

    /**
     *
     * @param mstCompany
     * @return
     */
    @Transactional
    public int updateMstCompanyByQuery(MstCompany mstCompany) {
        Query query = entityManager.createNamedQuery("MstCompany.updateByCompanyCode");
        query.setParameter("companyCode", mstCompany.getCompanyCode());
        query.setParameter("companyName", mstCompany.getCompanyName());
        query.setParameter("zipCode", mstCompany.getZipCode());
        query.setParameter("address", mstCompany.getAddress());
        query.setParameter("telNo", mstCompany.getTelNo());
        query.setParameter("myCompany", mstCompany.getMyCompany());
        query.setParameter("updateDate", mstCompany.getUpdateDate());
        query.setParameter("updateUserUuid", mstCompany.getUpdateUserUuid());
        //2016-12-2 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-2 jiangxiaosong add end
        query.setParameter("mgmtCompanyCode", mstCompany.getMgmtCompanyCode());//20170607 Apeng add
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * CSVの中身に対してチェックを行う
     * @param logMap
     * @param lineCsv
     * @param userLangId
     * @param logFile
     * @param index
     * @return 
     */
    private MstCompany checkCsvInfo(ArrayList lineCsv, String logFile, int index, FileUtil fileUtil, Map<String, String> csvHeader, Map<String, String> csvInfoMsg) {

        MstCompany mstCompany = new MstCompany();
        //ログ出力内容を用意する
        int arrayLength = lineCsv.size();
        if (arrayLength != 9) {//20170707 Apeng update
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("companyCode"), "", csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorWrongCsvLayout")));
            mstCompany.setIsError(true);
            return mstCompany;
        }

        String strCompanyCode = (null == lineCsv.get(0) ? "" : String.valueOf(lineCsv.get(0)));
        //分割した文字をObjectに格納する
        if (fileUtil.isNullCheck(strCompanyCode)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("companyCode"), strCompanyCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotNull")));
            failedCount = failedCount + 1;
            mstCompany.setIsError(true);
            return mstCompany;
        } else if (fileUtil.maxLangthCheck(strCompanyCode, 45)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("companyCode"), strCompanyCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstCompany.setIsError(true);
            return mstCompany;
        }

        String strDelFlag = (null == lineCsv.get(8) ? "" : String.valueOf(lineCsv.get(8)));//20170607 Apeng update
        //delFlag 指定されている場合
        if (strDelFlag != null && strDelFlag.trim().equals("1")) {
            // 削除する前に、存在確認
            if (!getMstCompanyExistCheck(strCompanyCode)) {
                failedCount = failedCount + 1;
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("companyCode"), strCompanyCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
                mstCompany.setIsError(true);
                return mstCompany;
            }
            // FK依存関係チェック　True依存関係なし削除できる　False　削除できない
            if (!getMstCompanyFKCheck(strCompanyCode)) {
                deleteMstCompany(strCompanyCode);
                deletedCount = deletedCount + 1;
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("companyCode"), strCompanyCode, csvInfoMsg.get("error"), 0, csvInfoMsg.get("dbProcess"), csvInfoMsg.get("msgRecordDeleted")));
                mstCompany.setIsError(true); // 正しく削除され、無視
                return mstCompany;
            } else {
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("companyCode"), strCompanyCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgCannotDeleteUsedRecord")));
                failedCount = failedCount + 1;
                mstCompany.setIsError(true);
                return mstCompany;
            }
        }

        String strCompanyName = (null == lineCsv.get(1) ? "" : String.valueOf(lineCsv.get(1)));
        if (fileUtil.isNullCheck(strCompanyName)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("companyName"), strCompanyName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotNull")));
            failedCount = failedCount + 1;
            mstCompany.setIsError(true);
            return mstCompany;
        } else if (fileUtil.maxLangthCheck(strCompanyName, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("companyCode"), strCompanyName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstCompany.setIsError(true);
            return mstCompany;
        }

        String strZipCode = (null == lineCsv.get(2) ? "" : String.valueOf(lineCsv.get(2)));
        if (fileUtil.maxLangthCheck(strZipCode, 15)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("zipCode"), strCompanyName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            mstCompany.setIsError(true);
            failedCount = failedCount + 1;
            return mstCompany;
        }

        String strAddress = (null == lineCsv.get(3) ? "" : String.valueOf(lineCsv.get(3)));
        if (fileUtil.maxLangthCheck(strAddress, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("address"), strCompanyName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            mstCompany.setIsError(true);
            failedCount = failedCount + 1;
            return mstCompany;
        }

        String strTelNo = (null == lineCsv.get(4) ? "" : String.valueOf(lineCsv.get(4)));
        if (fileUtil.maxLangthCheck(strTelNo, 25)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("telNo"), strCompanyName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            mstCompany.setIsError(true);
            failedCount = failedCount + 1;
            return mstCompany;
        }
        
         String strMgmtCompanyCode = (null == lineCsv.get(5) ? "" : String.valueOf(lineCsv.get(5)));//Apeng 20170710 update
        if(StringUtils.isNotEmpty(strMgmtCompanyCode)) {
            if (fileUtil.maxLangthCheck(strMgmtCompanyCode, 100)) {
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("mgmtCompanyCode"), strCompanyName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                mstCompany.setIsError(true);
                failedCount = failedCount + 1;
                return mstCompany;
            } else if(!mstMgmtCompanyService.getSingleMstMgmtCompany(strMgmtCompanyCode)){
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("mgmtCompanyCode"), strCompanyName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
                mstCompany.setIsError(true);
                failedCount = failedCount + 1;
                return mstCompany;
            }
        }

        String strMyCompany = (null == lineCsv.get(7) ? "" : String.valueOf(lineCsv.get(7)));//Apeng 20170710 update
        if (!strMyCompany.equals("1") && !strMyCompany.equals("0")) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("myCompany"), strCompanyName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
            mstCompany.setIsError(true);
            failedCount = failedCount + 1;
            return mstCompany;
        }

        // 会社マスタの自社フラグ1（自社）は1レコードしか登録できない仕様としますので、登録時のチェック
        if (strMyCompany.equals("1")) {
            if (checkMyCompanyCount(strCompanyCode)) {
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("myCompany"), strCompanyName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msg_error_my_company_duplicate")));
                mstCompany.setIsError(true);
                failedCount = failedCount + 1;
                return mstCompany;
            }
        }
        
        mstCompany.setCompanyCode(strCompanyCode);
        mstCompany.setCompanyName(strCompanyName);
        mstCompany.setZipCode(strZipCode);
        mstCompany.setAddress(strAddress);
        mstCompany.setTelNo(strTelNo);
        mstCompany.setMyCompany(Integer.parseInt(strMyCompany));
        mstCompany.setMgmtCompanyCode(strMgmtCompanyCode);//20170607 Apeng add
        mstCompany.setIsError(false);

        return mstCompany;

    }

    /**
     * 自社フラグ＝1のものをデフォルトセット。複数見つかった場合は最初に見つかったもの
     *
     * @return
     */
    public MstCompanyList getMyCompanies() {
        MstCompanyList mstCompanyList = new MstCompanyList();
        Query query = entityManager.createNamedQuery("MstCompany.findByMyCompany");
        query.setParameter("myCompany", 1);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        List<MstCompany> list = query.getResultList();
        mstCompanyList.setMstCompanies(list);
        return mstCompanyList;
    }
    
    /**
     * 自社フラグ＝0のものをデフォルトセット。
     *
     * @return
     */
    public MstCompanyList getNotMyCompanies() {
        MstCompanyList mstCompanyList = new MstCompanyList();
        Query query = entityManager.createQuery(
                "SELECT m FROM MstCompany m WHERE m.myCompany = :myCompany And m.externalFlg = :externalFlg ORDER BY m.companyCode");
        
        query.setParameter("myCompany", 0);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        List<MstCompany> list = query.getResultList();
        mstCompanyList.setMstCompanies(list);
        return mstCompanyList;
    }

    /**
     * Get self company
     *
     * @return
     */
    public MstCompany getSelfCompany() {
        MstCompany selfCompany = this.entityManager
                .createNamedQuery("MstCompany.findByMyCompany", MstCompany.class)
                .setParameter("myCompany", 1)
                .setParameter("externalFlg", CommonConstants.MINEFLAG)
                .getResultList().stream().findFirst().orElse(null);

        return selfCompany;
    }
    
    public MstCompany getByCompanyNameId(String companyName) {
        Query query = entityManager.createNamedQuery("MstCompany.findByCompanyName");
        query.setParameter("companyName", companyName);
        //2016-11-30 jiangxiaosong add start
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-11-30 jiangxiaosong add end
        try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1) {
                MstCompany response = (MstCompany) list.get(0);
                return response;
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    public MstCompany getByCompanyIdName(String companyId) {
        Query query = entityManager.createNamedQuery("MstCompany.findById");
        query.setParameter("id", companyId);
        try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1) {
                MstCompany response = (MstCompany) list.get(0);
                return response;
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * バッチで外部会社マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstCompanyList getExtMstCompaniesByBatch(String latestExecutedDate) {
        MstCompanyList resList = new MstCompanyList();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstCompany t WHERE 1=1 and t.externalFlg = 0 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstCompany> tempList = new ArrayList<>();
        tempList = query.getResultList();
        for (MstCompany mstCompany : tempList) {
            mstCompany.setMstApiUserCollection(null);
            mstCompany.setMstExternalDataGetSettingCollection(null);
            mstCompany.setMstLocationCollection(null);
            mstCompany.setMstMoldCollection(null);
            mstCompany.setMstMoldCollection1(null);
            mstCompany.setMstUserCollection(null);
            mstCompany.setTblMoldInventoryCollection(null);
            mstCompany.setTblMoldLocationHistoryCollection(null);
        }
        resList.setMstCompanies(tempList);
        return resList;
    }

    /**
     * バッチで外部会社マスタデータを更新
     *
     * @param companies
     * @return
     */
    @Transactional
    public BasicResponse updateExtMstCompaniesByBatch(List<MstCompany> companies) {
        BasicResponse response = new BasicResponse();
        if (null != companies && !companies.isEmpty()) {
            for (MstCompany mstCompany : companies) {
                MstCompany newCompany;
                MstCompany com = entityManager.find(MstCompany.class, mstCompany.getId());
                if (null != com) {
                    newCompany = com;
                } else {
                    newCompany = new MstCompany();
                }

                newCompany.setExternalFlg(1);
                newCompany.setCompanyCode(mstCompany.getCompanyCode());
                newCompany.setCompanyName(mstCompany.getCompanyName());
                newCompany.setCreateDate(mstCompany.getCreateDate());
                newCompany.setCreateUserUuid(mstCompany.getCreateUserUuid());
                newCompany.setUpdateDate(mstCompany.getUpdateDate());
                newCompany.setUpdateUserUuid(mstCompany.getUpdateUserUuid());
                newCompany.setMyCompany(mstCompany.getMyCompany());
//                newCompany.setMgmtCompanyCode(mstCompany.getMgmtCompanyCode());//20170607 Apeng add
                newCompany.setTelNo(mstCompany.getTelNo());
                newCompany.setZipCode(mstCompany.getZipCode());
                newCompany.setAddress(mstCompany.getAddress());

                if (null != com) {
                    entityManager.merge(newCompany);//更新
                } else {
                    newCompany.setId(mstCompany.getId());
                    entityManager.persist(newCompany);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /**
     * 会社マスタ　CSV取込み
     * @param fileUuid
     * @param loginUser
     * @return 
     */
    @Transactional
    public ImportResultResponse importCsv(String fileUuid, LoginUser loginUser) {
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }

        // SCVデータを取込み
        ArrayList readList = CSVFileUtil.readCsv(csvFile);
        int csvInfoSize = readList.size();
        if (csvInfoSize <= 1) {
            return importResultResponse;
        } else {
            String logFileUuid = IDGenerator.generate();//ログファイルUUID用意
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

            csvProcesStart(loginUser, readList, logFile);
            succeededCount = addedCount + updatedCount + deletedCount;
            csvProcesExit(loginUser, fileUuid, csvInfoSize, logFileUuid);
            // リターン情報
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
     * CSV取込み処理開始
     * @param loginUser
     * @param csvInfoList
     * @param logFile
     */
    private void csvProcesStart(LoginUser loginUser, ArrayList csvInfoList, String logFile) {
        FileUtil fileUtil = new FileUtil();
        Map<String, String> csvHeader = getDictValues(loginUser.getLangId());
        Map<String, String> csvCheckMsg = getCsvInfoCheckMsg(loginUser.getLangId());
        for (int i = 1; i < csvInfoList.size(); i++) {
            
            ArrayList comList = (ArrayList) csvInfoList.get(i);
            // 入力データチェック & 削除処理
            MstCompany mstCompany = checkCsvInfo(comList, logFile, i, fileUtil, csvHeader, csvCheckMsg);
            // チェックエラーなし場合
            if (!mstCompany.isError()) {
                if (getMstCompanyExistCheck(mstCompany.getCompanyCode())) {
                    //存在チェックの場合　更新
                    mstCompany.setUpdateDate(new Date());
                    mstCompany.setUpdateUserUuid(loginUser.getUserUuid());
                    int count = this.updateMstCompanyByQuery(mstCompany);
                    updatedCount = updatedCount + count;
                    //DB操作情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvCheckMsg.get("rowNumber"), i, csvHeader.get("companyCode"), mstCompany.getCompanyCode(), csvCheckMsg.get("error"), 0, csvCheckMsg.get("dbProcess"), csvCheckMsg.get("msgDataModified")));

                } else {
                    //追加
                    mstCompany.setId(IDGenerator.generate());
                    mstCompany.setCreateDate(new Date());
                    mstCompany.setCreateUserUuid(loginUser.getUserUuid());
                    mstCompany.setUpdateDate(new Date());
                    mstCompany.setUpdateUserUuid(loginUser.getUserUuid());
                    mstCompany.setExternalFlg(CommonConstants.MINEFLAG);
                    this.createMstCompany(mstCompany);
                    addedCount = addedCount + 1;
                    //DB操作情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvCheckMsg.get("rowNumber"), i, csvHeader.get("companyCode"), mstCompany.getCompanyCode(), csvCheckMsg.get("error"), 0, csvCheckMsg.get("dbProcess"), csvCheckMsg.get("msgRecordAdded")));
                }
            }
        }
    }
    
    /**
     * CSV取込処理完了処理
     * @param loginUser
     * @param fileUuid
     * @param csvInfoSize
     * @param logFileUuid
     */
    private void csvProcesExit(LoginUser loginUser, String fileUuid, int csvInfoSize, String logFileUuid) {

        //アップロードログをテーブルに書き出し
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_COMPANY);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MST_COMPANY);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(csvInfoSize - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_COMPANY);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

        tblCsvImportService.createCsvImpor(tblCsvImport);
    }
    
    
    /**
     * CSV出力へーだー準備
     *
     * @return
     */
    private ArrayList<ArrayList> getHeaderes(String langId) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        /*Head*/
        Map<String, String> csvHeader = getDictValues(langId);
        headList.add(csvHeader.get("companyCode"));
        headList.add(csvHeader.get("companyName"));
        headList.add(csvHeader.get("zipCode"));
        headList.add(csvHeader.get("address"));
        headList.add(csvHeader.get("telNo"));
        headList.add(csvHeader.get("mgmtCompanyCode"));//20170607 Apeng add
        headList.add(csvHeader.get("mgmtCompanyName"));//20170607 Apeng add
        headList.add(csvHeader.get("myCompany"));
        headList.add(csvHeader.get("deleteRecord"));
        gLineList.add(headList);
        return gLineList;
    }

    /**
     * CSVヘーダー用
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictValues(String langId) {

        Map<String, String> dictMap = new HashMap<>();
        dictMap.put("companyCode", mstDictionaryService.getDictionaryValue(langId, "company_code"));
        dictMap.put("companyName", mstDictionaryService.getDictionaryValue(langId, "company_name"));
        dictMap.put("zipCode", mstDictionaryService.getDictionaryValue(langId, "zip_code"));
        dictMap.put("address", mstDictionaryService.getDictionaryValue(langId, "address"));
        dictMap.put("telNo", mstDictionaryService.getDictionaryValue(langId, "tel_no"));
        dictMap.put("mgmtCompanyCode", mstDictionaryService.getDictionaryValue(langId, "mgmt_company_code"));//20170607 Apeng add
        dictMap.put("mgmtCompanyName", mstDictionaryService.getDictionaryValue(langId, "mgmt_company_name"));//20170607 Apeng add
        dictMap.put("myCompany", mstDictionaryService.getDictionaryValue(langId, "my_company"));
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
        // 会社マスタの自社フラグ1（自社）は1レコードしか登録できない仕様としますので、登録時のチェック
        msgMap.put("msg_error_my_company_duplicate",mstDictionaryService.getDictionaryValue(langId, "msg_error_my_company_duplicate"));
        
        return msgMap;
    }
    
    /**
     *
     * @param company
     * @return
     */
    public MstCompanyList getInspectIncomingMstCompanies(String company) {

        MstCompanyList response = new MstCompanyList();
        
        StringBuilder sql;
            sql = new StringBuilder("SELECT m FROM MstCompany m WHERE 1=1 ");

        String sqlCompany = "";
        if (StringUtils.isNotBlank(company)) {
            sqlCompany = company.trim();
            sql.append(" and (m.companyCode like :company OR m.companyName like :company) ");
        }

        sql.append(" and m.externalFlg = :externalFlg ");
        
        // 検査管理項目作成の場合、納品先会社を取得 20180918 start
        sql.append(" AND m.id NOT IN ( SELECT a.companyId FROM MstApiUser a WHERE a.validFlg = 1)");
        // 検査管理項目作成の場合、納品先会社を取得 20180918 end

        sql.append(" Order by m.companyCode ");    //companyCodeの昇順
        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotBlank(company)) {
            query.setParameter("company", "%" + sqlCompany + "%");
        }
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        List<MstCompany> mstCompanies = query.setMaxResults(100).getResultList();
        for(MstCompany mstCompany :mstCompanies){
            mstCompany.setMstMgmtCompany(null);
        }
        response.setMstCompanies(mstCompanies);
        
        return response;

    }
    
    /**
     *
     * @param company
     * @return
     */
    public MstCompanyMinList getInspectCompanies(String company) {
        MstCompanyMinList response = new MstCompanyMinList();
        
        List<MstCompanyMin> mstCompanies = entityManager.createNamedQuery("MstCompanyMin.findCodeOrName", MstCompanyMin.class)
            .setParameter("codeOrName", "%" + company.trim() + "%")
            .setParameter("externalFlg", CommonConstants.MINEFLAG)
            .setMaxResults(100).getResultList();

        response.setMstCompanies(mstCompanies);
        return response;
    }
    
    /**
     *
     * @param companyName
     * @return
     */
    public MstCompanyList getIncomingCompanyByName(String companyName) {

        MstCompanyList response = new MstCompanyList();

        StringBuilder sql;
        sql = new StringBuilder("SELECT m FROM MstCompany m WHERE 1=1 ");

        if (StringUtils.isNotBlank(companyName)) {
            sql.append(" AND m.companyName = :companyName ");
        }

        sql.append(" AND m.externalFlg = :externalFlg ");

        // 検査管理項目作成の場合、納品先会社を取得 20180918 start
        sql.append(" AND m.id NOT IN ( SELECT a.companyId FROM MstApiUser a WHERE a.validFlg = 1)");
        // 検査管理項目作成の場合、納品先会社を取得 20180918 end

        sql.append(" Order by m.companyCode ");    //companyCodeの昇順
        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotBlank(companyName)) {
            query.setParameter("companyName", companyName);
        }
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);

        response.setMstCompanies(query.getResultList());

        return response;
    }
    
}
