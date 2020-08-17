/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part;

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
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Dependent
public class MstMoldPartService {

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
    

    // リターン情報を初期化
    long succeededCount = 0;//成功件数
    long addedCount = 0;//追加件数
    long updatedCount = 0;//更新件数
    long failedCount = 0; //失敗件数
    long deletedCount = 0; //削除件数
   
    
    /**
     *
     * @param moldPartCode
     * @param moldPartName
     * @param manufacturer
     * @param memo
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public MstMoldPartList getMstMoldParts(String moldPartCode, String moldPartName, String manufacturer, String memo, int pageNumber, int pageSize) {
        MstMoldPartList response = new MstMoldPartList();
        response.setMstMoldParts(sql(moldPartCode, moldPartName, manufacturer, memo, pageNumber, pageSize, 1));
        List list = sql(moldPartCode, moldPartName, manufacturer, memo, pageNumber, pageSize, 0);
        response.setTotalCount((Long)list.get(0));
        return response;
    }
    /**
     * Sql文を用意
     *
     * @param moldPartCode
     * @param moldPartName
     * @param manufacturer
     * @param memo
     * @param pageNumber
     * @param pageSize
     * @param flag
     * @return
     */
    public List sql(String moldPartCode, String moldPartName, String manufacturer, String memo, int pageNumber, int pageSize, int flag) {
        StringBuilder sql;
        if (flag == 1) {
            sql = new StringBuilder("SELECT m FROM MstMoldPart m WHERE 1=1 ");
        } else {
            sql = new StringBuilder("SELECT count(m) FROM MstMoldPart m WHERE 1=1 ");
        }

        String sqlMoldPartCode = "";
        String sqlMoldPartName = "";
        String sqlManufacturer = "";
        String sqlMemo = "";
        if (moldPartCode != null && !"".equals(moldPartCode)) {
            sqlMoldPartCode = moldPartCode.trim();
            sql = sql.append(" and m.moldPartCode like :moldPartCode ");
        }
        if (moldPartName != null && !"".equals(moldPartName)) {
            sqlMoldPartName = moldPartName.trim();
            sql = sql.append(" and m.moldPartName like :moldPartName ");
        }
        if (manufacturer != null && !"".equals(manufacturer)) {
            sqlManufacturer = manufacturer.trim();
            sql = sql.append(" and m.manufacturer like :manufacturer ");
        }
        if (memo != null && !"".equals(memo)) {
            sqlMemo = memo.trim();
            sql = sql.append(" and m.memo like :memo ");
        }

        sql = sql.append(" Order by m.moldPartCode ");    //moldPartCodeの昇順
        Query query = entityManager.createQuery(sql.toString());
        if (moldPartCode != null && !"".equals(moldPartCode)) {
            query.setParameter("moldPartCode", "%" + sqlMoldPartCode + "%");
        }
        if (moldPartName != null && !"".equals(moldPartName)) {
            query.setParameter("moldPartName", "%" + sqlMoldPartName + "%");
        }
        if (manufacturer != null && !"".equals(manufacturer)) {
            query.setParameter("manufacturer", "%" + sqlManufacturer + "%");
        }
        if (memo != null && !"".equals(memo)) {
            query.setParameter("memo", "%" + sqlMemo + "%");
        }

        if(flag == 1 && pageSize != -1 && pageNumber > 0){
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        return query.getResultList();
    }

    /**
     * CSV 出力
     * @param moldPartCode
     * @param moldPartName
     * @param manufacturer
     * @param memo
     * @param loginUser
     * @return
     */
    public FileReponse getOutputCsv(String moldPartCode, String moldPartName, String manufacturer, String memo, LoginUser loginUser) {

        // 出力へーだー準備
        ArrayList<ArrayList> gLineList = getHeaderes(loginUser.getLangId());
        //明細データを取得
        List list = sql(moldPartCode, moldPartName, manufacturer, memo, 0, -1, 1);

        /*Detail*/
        ArrayList lineList;
        for (int i = 0; i < list.size(); i++) {
            lineList = new ArrayList();
            MstMoldPart mstMoldPart = (MstMoldPart) list.get(i);
            lineList.add(mstMoldPart.getMoldPartCode());
            lineList.add(mstMoldPart.getMoldPartName());
            lineList.add(mstMoldPart.getManufacturer());
            lineList.add(mstMoldPart.getModelNumber());
            lineList.add(mstMoldPart.getMemo());
            lineList.add(""+mstMoldPart.getUnitPrice().setScale(0, RoundingMode.HALF_UP));
            lineList.add(""+mstMoldPart.getUsedUnitPrice().setScale(0, RoundingMode.HALF_UP));
            lineList.add(mstMoldPart.getIntrExtProduct() == 0 ? mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "internal_product") : 
                    mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "external_product"));
            lineList.add(""+mstMoldPart.getRecyclableFlg());
            lineList.add("");
            gLineList.add(lineList);
        }

        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD_PART_MAINTENANCE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD_PART);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MOLD_PART_MAINTENANCE);

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        FileReponse fileReponse = new FileReponse();
        fileReponse.setFileUuid(uuid);
        return fileReponse;
    }

    /**
     *
     * @param moldPartCode
     * @param moldPartName
     * @param manufacturer
     * @param memo
     * @return
     */
    public CountResponse getMstMoldPartCount(String moldPartCode, String moldPartName, String manufacturer, String memo) {
        List list = sql(moldPartCode, moldPartName, manufacturer, memo, 0, -1, 0);
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    /**
     *
     * @param moldPartCode
     * @return
     */
    public MstMoldPart getMstMoldPartByCode(String moldPartCode) {
        Query query = entityManager.createNamedQuery("MstMoldPart.findByMoldPartCode");
        query.setParameter("moldPartCode", moldPartCode);
        MstMoldPart mstMoldPart = null;
        try {
            mstMoldPart = (MstMoldPart) query.getSingleResult();
            return mstMoldPart;
        } catch (NoResultException e) {
            return mstMoldPart;
        }
    }

    /**
     *
     * @param moldPartCode
     * @return
     */
    public boolean getMstMoldPartExistCheck(String moldPartCode) {
        Query query = entityManager.createNamedQuery("MstMoldPart.findByMoldPartCode");
        query.setParameter("moldPartCode", moldPartCode);
        return query.getResultList().size() > 0;
    }
    
    /**
     *
     * @param moldPartId
     * @return
     */
    public MstMoldPartList getMstMoldPartDetail(String moldPartId) {
        Query query = entityManager.createNamedQuery("MstMoldPart.findById");
        query.setParameter("id", moldPartId);
        List list = query.getResultList();
        MstMoldPartList response = new MstMoldPartList();
        response.setMstMoldParts(list);
        response.setTotalCount(list.size());
        return response;
    }

    /**
     *
     * @param moldPartName
     * @return
     */
    public MstMoldPartList getMoldPartByName(String moldPartName) {
        Query query = entityManager.createNamedQuery("MstMoldPart.findByMoldPartName");
        query.setParameter("moldPartName", moldPartName);
        List list = query.getResultList();
        MstMoldPartList response = new MstMoldPartList();
        response.setMstMoldParts(list);
        return response;
    }

    /**
     *
     * @param moldPartCode
     * @return
     */
    @Transactional
    public int deleteMstMoldPart(String moldPartCode) {

        Query query = entityManager.createNamedQuery("MstMoldPart.deleteByCode");
        query.setParameter("moldPartCode", moldPartCode);
        return query.executeUpdate();
    }
    
    @Transactional
    public int deleteMstMoldPartById(String moldPartId) {
        Query query = entityManager.createNamedQuery("MstMoldPart.deleteById");
        query.setParameter("id", moldPartId);
        return query.executeUpdate();
    }
    
    public boolean isDeletableMoldPart(String moldPartId) {
        Query moldPartQuery = entityManager.createNamedQuery("MstMoldPart.findById");
        moldPartQuery.setParameter("id", moldPartId);
        
        Query moldPartRelQuery = entityManager.createQuery("SELECT t FROM MstMoldPartRel t WHERE t.moldPartId = :moldPartId");
        moldPartRelQuery.setParameter("moldPartId", moldPartId);
        
        List moldPartDetail = moldPartQuery.getResultList();
        List moldPartRel = moldPartRelQuery.getResultList();
        return !moldPartDetail.isEmpty() && moldPartRel.isEmpty();
    }

    /**
     *
     * @param mstMoldPart
     */
    @Transactional
    public void createMstMoldPart(MstMoldPart mstMoldPart) {
        entityManager.persist(mstMoldPart);
    }

    /**
     *
     * @param mstMoldPart
     * @return
     */
    @Transactional
    public int updateMstMoldPartByQuery(MstMoldPart mstMoldPart) {
        Query query = entityManager.createNamedQuery("MstMoldPart.updateByMoldPartCode");
        query.setParameter("moldPartCode", mstMoldPart.getMoldPartCode());
        query.setParameter("moldPartName", mstMoldPart.getMoldPartName());
        query.setParameter("manufacturer", mstMoldPart.getManufacturer());
        query.setParameter("modelNumber", mstMoldPart.getModelNumber());
        query.setParameter("memo", mstMoldPart.getMemo());
        query.setParameter("unitPrice", mstMoldPart.getUnitPrice() == null ? 0 : mstMoldPart.getUnitPrice());
        query.setParameter("usedUnitPrice", mstMoldPart.getUsedUnitPrice() == null ? 0 : mstMoldPart.getUsedUnitPrice());
        query.setParameter("intrExtProduct", mstMoldPart.getIntrExtProduct());
        query.setParameter("recyclableFlg", mstMoldPart.getRecyclableFlg());
        query.setParameter("updateDate", mstMoldPart.getUpdateDate());
        query.setParameter("updateUserUuid", mstMoldPart.getUpdateUserUuid());
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
    private MstMoldPart checkCsvInfo(ArrayList lineCsv, String logFile, int index, FileUtil fileUtil, Map<String, String> csvHeader, Map<String, String> csvInfoMsg, LoginUser loginUser) {

        MstMoldPart mstMoldPart = new MstMoldPart();
        //ログ出力内容を用意する
        int arrayLength = lineCsv.size();
        if (arrayLength != 10) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldPartCode"), "", csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorWrongCsvLayout")));
            failedCount = failedCount + 1;
            mstMoldPart.setIsError(true);
            return mstMoldPart;
        }

        String strMoldPartCode = (null == lineCsv.get(0) ? "" : String.valueOf(lineCsv.get(0)));
        //分割した文字をObjectに格納する
        if (fileUtil.isNullCheck(strMoldPartCode)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldPartCode"), strMoldPartCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotNull")));
            failedCount = failedCount + 1;
            mstMoldPart.setIsError(true);
            return mstMoldPart;
        } else if (fileUtil.maxLangthCheck(strMoldPartCode, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldPartCode"), strMoldPartCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPart.setIsError(true);
            return mstMoldPart;
        }
        String strDelFlag = (null == lineCsv.get(9) ? "" : String.valueOf(lineCsv.get(9)));
        //delFlag 指定されている場合
        if (strDelFlag != null && strDelFlag.trim().equals("1")) {
            // 削除する前に、存在確認
            if (!getMstMoldPartExistCheck(strMoldPartCode)) {
                failedCount = failedCount + 1;
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldPartCode"), strMoldPartCode, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("mstErrorRecordNotFound")));
                mstMoldPart.setIsError(true);
                return mstMoldPart;
            }
                deleteMstMoldPart(strMoldPartCode);
                deletedCount = deletedCount + 1;
                //エラー情報をログファイルに記入
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldPartCode"), strMoldPartCode, csvInfoMsg.get("error"), 0, csvInfoMsg.get("dbProcess"), csvInfoMsg.get("msgRecordDeleted")));
                mstMoldPart.setIsError(true); // 正しく削除され、無視
                return mstMoldPart;
        }
        
        String strMoldPartName = (null == lineCsv.get(1) ? "" : String.valueOf(lineCsv.get(1)));
        if (fileUtil.isNullCheck(strMoldPartName)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldPartName"), strMoldPartName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotNull")));
            failedCount = failedCount + 1;
            mstMoldPart.setIsError(true);
            return mstMoldPart;
        } else if (fileUtil.maxLangthCheck(strMoldPartName, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("moldPartCode"), strMoldPartName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            failedCount = failedCount + 1;
            mstMoldPart.setIsError(true);
            return mstMoldPart;
        }

        String strManufacturer = (null == lineCsv.get(2) ? "" : String.valueOf(lineCsv.get(2)));
        if (fileUtil.maxLangthCheck(strManufacturer, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("manufacturer"), strMoldPartName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            mstMoldPart.setIsError(true);
            failedCount = failedCount + 1;
            return mstMoldPart;
        }

        String strModelNumber = (null == lineCsv.get(3) ? "" : String.valueOf(lineCsv.get(3)));
        if (fileUtil.maxLangthCheck(strModelNumber, 100)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("modelNumber"), strMoldPartName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            mstMoldPart.setIsError(true);
            failedCount = failedCount + 1;
            return mstMoldPart;
        }

        String strMemo = (null == lineCsv.get(4) ? "" : String.valueOf(lineCsv.get(4)));
        if (fileUtil.maxLangthCheck(strMemo, 200)) {
            //エラー情報をログファイルに記入
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("memo"), strMoldPartName, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            mstMoldPart.setIsError(true);
            failedCount = failedCount + 1;
            return mstMoldPart;
        }
        
        String strUnitPrice = (null == lineCsv.get(5) ? "" : String.valueOf(lineCsv.get(5)));
       if (!fileUtil.isNullCheck(strUnitPrice)) {
            if (!fileUtil.isDouble(strUnitPrice)) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("unitPrice"), strUnitPrice, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msg_error_not_isnumber")));
                mstMoldPart.setIsError(true);
                failedCount = failedCount + 1;
                return mstMoldPart;
            }
            if (fileUtil.maxLangthCheck(strUnitPrice, 15)) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("unitPrice"), strUnitPrice, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                mstMoldPart.setIsError(true);
                failedCount = failedCount + 1;
                return mstMoldPart;
            }            
        }
        BigDecimal unitPriceVal = new BigDecimal(strUnitPrice);
       
        String strUsedUnitPrice = (null == lineCsv.get(6) ? "" : String.valueOf(lineCsv.get(6)));
       if (!fileUtil.isNullCheck(strUsedUnitPrice)) {
            if (!fileUtil.isDouble(strUsedUnitPrice)) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("usedUnitPrice"), strUsedUnitPrice, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msg_error_not_isnumber")));
                mstMoldPart.setIsError(true);
                failedCount = failedCount + 1;
                return mstMoldPart;
            }
            if (fileUtil.maxLangthCheck(strUsedUnitPrice, 15)) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("usedUnitPrice"), strUsedUnitPrice, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                mstMoldPart.setIsError(true);
                failedCount = failedCount + 1;
                return mstMoldPart;
            }
        }
        BigDecimal usedUnitPriceVal = new BigDecimal(strUsedUnitPrice);
        
        String strIntrExtProduct = (null == lineCsv.get(7) ? "" : String.valueOf(lineCsv.get(7)));
        if (!fileUtil.isNullCheck(strIntrExtProduct)) {
            if (fileUtil.maxLangthCheck(strIntrExtProduct, 100)) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("intExtProduct"), strIntrExtProduct, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                mstMoldPart.setIsError(true);
                failedCount = failedCount + 1;
                return mstMoldPart;
            }
            
            if(strIntrExtProduct.equals(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "internal_product"))){
                strIntrExtProduct = "0"; 
            } else if (strIntrExtProduct.equals(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "external_product"))) {
                   strIntrExtProduct = "1";
            } else {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("intrExtProduct"), strIntrExtProduct, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("valueInvalid")));
                mstMoldPart.setIsError(true);
                failedCount = failedCount + 1;
                return mstMoldPart;
            }
        } else {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("intrExtProduct"), strIntrExtProduct, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("valueInvalid")));
            mstMoldPart.setIsError(true);
            failedCount = failedCount + 1;
            return mstMoldPart;
        }
        
        String strRecyclableFlg = (null == lineCsv.get(8) ? "" : String.valueOf(lineCsv.get(8)));
        if (!fileUtil.isNullCheck(strRecyclableFlg)) {
            if (fileUtil.maxLangthCheck(strRecyclableFlg, 1)) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("recyclableFlg"), strRecyclableFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                mstMoldPart.setIsError(true);
                failedCount = failedCount + 1;
                return mstMoldPart;
            }
        }
        
        mstMoldPart.setMoldPartCode(strMoldPartCode);
        mstMoldPart.setMoldPartName(strMoldPartName);
        mstMoldPart.setManufacturer(strManufacturer);
        mstMoldPart.setModelNumber(strModelNumber);
        mstMoldPart.setMemo(strMemo);
        mstMoldPart.setUnitPrice(unitPriceVal);
        mstMoldPart.setUsedUnitPrice(usedUnitPriceVal);
        mstMoldPart.setIntrExtProduct(Integer.valueOf(strIntrExtProduct));
        mstMoldPart.setRecyclableFlg(Integer.valueOf(strRecyclableFlg));
        mstMoldPart.setIsError(false);

        return mstMoldPart;

    }
    
    public MstMoldPart getByMoldPartNameId(String moldPartName) {
        Query query = entityManager.createNamedQuery("MstMoldPart.findByMoldPartName");
        query.setParameter("moldPartName", moldPartName);
        try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1) {
                MstMoldPart response = (MstMoldPart) list.get(0);
                return response;
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    public MstMoldPart getByMoldPartIdName(String moldPartId) {
        Query query = entityManager.createNamedQuery("MstMoldPart.findById");
        query.setParameter("id", moldPartId);
        try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1) {
                MstMoldPart response = (MstMoldPart) list.get(0);
                return response;
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Update external moldpart data in batch
     *
     * @param moldParts
     * @return
     */
    @Transactional
    public BasicResponse updateExtMstMoldPartsByBatch(List<MstMoldPart> moldParts) {
        BasicResponse response = new BasicResponse();
        if (null != moldParts && !moldParts.isEmpty()) {
            for (MstMoldPart mstMoldPart : moldParts) {
                MstMoldPart newMoldPart;
                MstMoldPart com = entityManager.find(MstMoldPart.class, mstMoldPart.getId());
                if (null != com) {
                    newMoldPart = com;
                } else {
                    newMoldPart = new MstMoldPart();
                }

                newMoldPart.setMoldPartCode(mstMoldPart.getMoldPartCode());
                newMoldPart.setMoldPartName(mstMoldPart.getMoldPartName());
                newMoldPart.setCreateDate(mstMoldPart.getCreateDate());
                newMoldPart.setCreateUserUuid(mstMoldPart.getCreateUserUuid());
                newMoldPart.setUpdateDate(mstMoldPart.getUpdateDate());
                newMoldPart.setUpdateUserUuid(mstMoldPart.getUpdateUserUuid());
                newMoldPart.setManufacturer(mstMoldPart.getManufacturer());
                newMoldPart.setModelNumber(mstMoldPart.getModelNumber());
                newMoldPart.setMemo(mstMoldPart.getMemo());

                if (null != com) {
                    entityManager.merge(newMoldPart);//更新
                } else {
                    newMoldPart.setId(mstMoldPart.getId());
                    entityManager.persist(newMoldPart);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        return response;
    }

    /**
     * moldpart CSV import
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

            importResultResponse = csvProcesStart(loginUser, readList, logFile);
            if(!importResultResponse.isError()){
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
            }
            return importResultResponse;
        }
    }
    
    public MstMoldPartDetailList searchMoldParts(String moldPartCode, String moldPartName, Boolean isFilter) {
        StringBuilder sql = new StringBuilder("SELECT m FROM MstMoldPart m WHERE 1=1 ");
        String sqlMoldPartCode = "";
        String sqlmoldPartName = "";
        String operatorMethod = isFilter ? "like" : "=";
        if (moldPartCode != null && !"".equals(moldPartCode)) {
            sqlMoldPartCode = moldPartCode.trim();
            sql.append(" and m.moldPartCode ");
            sql.append(operatorMethod);
            sql.append(" :moldPartCode");
        }

        if (moldPartName != null && !"".equals(moldPartName)) {
            sqlmoldPartName = moldPartName.trim();
            sql.append(" and m.moldPartName ");
            sql.append(operatorMethod);
            sql.append(" :moldPartName");
        }
        sql.append(" ORDER BY m.moldPartCode");
        Query query = entityManager.createQuery(sql.toString());
        if (moldPartCode != null && !"".equals(moldPartCode)) {
            
            query.setParameter("moldPartCode", isFilter ? "%" + sqlMoldPartCode + "%" : sqlMoldPartCode);
        }

        if (moldPartName != null && !"".equals(moldPartName)) {
            query.setParameter("moldPartName", isFilter ? "%" + sqlmoldPartName + "%" : sqlmoldPartName);
        }
        query.setMaxResults(100);
        List<MstMoldPart> mstMoldParts = query.getResultList();
        List<MstMoldPartDetail> mstMoldPartDetails = mstMoldParts.stream().map(mp -> new MstMoldPartDetail(mp)).collect(Collectors.toList());
        MstMoldPartDetailList mstMoldPartList = new MstMoldPartDetailList();
        mstMoldPartList.setMstMoldParts(mstMoldPartDetails);
        mstMoldPartList.setTotalCount(mstMoldPartDetails.size());
        return mstMoldPartList;
    }
    
    /**
     * CSV取込み処理開始
     * @param loginUser
     * @param csvInfoList
     * @param logFile
     */
    private ImportResultResponse csvProcesStart(LoginUser loginUser, ArrayList csvInfoList, String logFile) {
        FileUtil fileUtil = new FileUtil();
        ImportResultResponse importResultResponse = new ImportResultResponse();
        Map<String, String> csvHeader = getDictValues(loginUser.getLangId());
        Map<String, String> csvCheckMsg = getCsvInfoCheckMsg(loginUser.getLangId());
        
        // Validate header size
        ArrayList headerList = (ArrayList) csvInfoList.get(0);
        if (headerList.size() != 10) {
            //fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvCheckMsg.get("rowNumber"), 1, csvHeader.get("moldPartCode"), "", csvCheckMsg.get("error"), 1, csvCheckMsg.get("errorDetail"), csvCheckMsg.get("msgErrorWrongCsvLayout")));
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }
        
        for (int i = 1; i < csvInfoList.size(); i++) {
            
            ArrayList comList = (ArrayList) csvInfoList.get(i);
            
            
            // 入力データチェック & 削除処理
            MstMoldPart mstMoldPart = checkCsvInfo(comList, logFile, i, fileUtil, csvHeader, csvCheckMsg, loginUser);
            
            // チェックエラーなし場合
            if (!mstMoldPart.isError()) {
                if (getMstMoldPartExistCheck(mstMoldPart.getMoldPartCode())) {
                    //存在チェックの場合　更新
                    mstMoldPart.setUpdateDate(new Date());
                    mstMoldPart.setUpdateUserUuid(loginUser.getUserUuid());
                    int count = this.updateMstMoldPartByQuery(mstMoldPart);
                    updatedCount = updatedCount + count;
                    //DB操作情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvCheckMsg.get("rowNumber"), i, csvHeader.get("moldPartCode"), mstMoldPart.getMoldPartCode(), csvCheckMsg.get("error"), 0, csvCheckMsg.get("dbProcess"), csvCheckMsg.get("msgDataModified")));

                } else {
                    //追加
                    mstMoldPart.setId(IDGenerator.generate());
                    mstMoldPart.setCreateDate(new Date());
                    mstMoldPart.setCreateUserUuid(loginUser.getUserUuid());
                    mstMoldPart.setUpdateDate(new Date());
                    mstMoldPart.setUpdateUserUuid(loginUser.getUserUuid());
                    this.createMstMoldPart(mstMoldPart);
                    addedCount = addedCount + 1;
                    //DB操作情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvCheckMsg.get("rowNumber"), i, csvHeader.get("moldPartCode"), mstMoldPart.getMoldPartCode(), csvCheckMsg.get("error"), 0, csvCheckMsg.get("dbProcess"), csvCheckMsg.get("msgRecordAdded")));
                }
            }
        }
        return importResultResponse;
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
        tblCsvImport.setImportTable(CommonConstants.TBL_MST_MOLD_PART_MAINTENANCE);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD_PART);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(csvInfoSize - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
        tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
        tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MOLD_PART_MAINTENANCE);
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
        headList.add(csvHeader.get("moldPartCode"));
        headList.add(csvHeader.get("moldPartName"));
        headList.add(csvHeader.get("manufacturer"));
        headList.add(csvHeader.get("modelNumber"));
        headList.add(csvHeader.get("memo"));
        headList.add(csvHeader.get("unitPrice"));
	headList.add(csvHeader.get("usedUnitPrice"));
	headList.add(csvHeader.get("intrExtProduct"));
	headList.add(csvHeader.get("recyclableFlg"));
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
        dictMap.put("moldPartCode", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_mold_part_code"));
        dictMap.put("moldPartName", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_mold_part_name"));
        dictMap.put("manufacturer", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_manufacturer"));
        dictMap.put("modelNumber", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_model_number"));
        dictMap.put("memo", mstDictionaryService.getDictionaryValue(langId, "mst_mold_part_memo"));
        dictMap.put("unitPrice", mstDictionaryService.getDictionaryValue(langId, "unit_price"));
        dictMap.put("usedUnitPrice", mstDictionaryService.getDictionaryValue(langId, "used_unit_price"));
        dictMap.put("intrExtProduct", mstDictionaryService.getDictionaryValue(langId, "intr_ext_product"));
        dictMap.put("recyclableFlg", mstDictionaryService.getDictionaryValue(langId, "recyclable_flg"));
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
        msgMap.put("valueInvalid", mstDictionaryService.getDictionaryValue(langId, "msg_error_value_invalid"));
        // db info
        msgMap.put("dbProcess", mstDictionaryService.getDictionaryValue(langId, "db_process"));
        msgMap.put("msgRecordAdded", mstDictionaryService.getDictionaryValue(langId, "msg_record_added"));
        msgMap.put("msgDataModified", mstDictionaryService.getDictionaryValue(langId, "msg_data_modified"));
        msgMap.put("msgErrorDataDeleted",mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
        msgMap.put("msgRecordDeleted",mstDictionaryService.getDictionaryValue(langId, "msg_record_deleted"));
        
        return msgMap;
    }

}
