/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.lot;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.material.stock.TblMaterialStockService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
 * 材料ロット サービス
 *
 * @author admin
 */
@Dependent
public class TblMaterialLotService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private TblMaterialStockService tblMaterialStockService;

    @Inject
    private MstChoiceService mstChoiceService;

    private String tbl_material_lot_status = "tbl_material_lot.status";

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();

        orderKey.put("materialCode", " ORDER BY mstMaterial.materialCode ");//材料コード
        orderKey.put("materialName", " ORDER BY mstMaterial.materialName ");//材料名称
        orderKey.put("lotNo", " ORDER BY tblMaterialLot.lotNo ");//ロット番号
        orderKey.put("lotQuantity", " ORDER BY tblMaterialLot.stockQuantity ");//在庫数
        orderKey.put("stockQuantity", " ORDER BY tblMaterialLot.stockQuantity ");//登録日
        orderKey.put("lotIssueDate", " ORDER BY tblMaterialLot.lotIssueDate ");//ロット発行日        
        orderKey.put("statusText", " ORDER BY tblMaterialLot.status ");//状態
        orderKey.put("remarks01", " ORDER BY tblMaterialLot.remarks01 ");//コメント

    }

    /**
     *
     * @param materialCode
     * @param materialName
     * @param status
     * @param remarks
     * @param loginUser
     * @return
     */
    public FileReponse getTblMaterialLotVoCsv(String materialCode, String materialName, int status, String remarks, LoginUser loginUser) {

        /**
         * Header
         */
        ArrayList dictKeyList = new ArrayList();
        dictKeyList.add("material_code");
        dictKeyList.add("material_name");
        dictKeyList.add("lot_number");
        dictKeyList.add("quantity");
        dictKeyList.add("stock_quantity");
        dictKeyList.add("material_lot_status");
        dictKeyList.add("issue_remarks");
        dictKeyList.add("lot_issue_datetime");
        dictKeyList.add("delete_record");
        dictKeyList.add("material_lot_registration");

        Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(loginUser.getLangId(), dictKeyList);

        /*Head*/
        ArrayList headList = new ArrayList();
        headList.add(dictMap.get("material_code"));
        headList.add(dictMap.get("material_name"));
        headList.add(dictMap.get("lot_number"));
        headList.add(dictMap.get("quantity"));
        headList.add(dictMap.get("stock_quantity"));
        headList.add(dictMap.get("material_lot_status"));
        headList.add(dictMap.get("issue_remarks"));
        headList.add(dictMap.get("lot_issue_datetime"));
        headList.add(dictMap.get("delete_record"));

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(headList);

        TblMaterialLotVoList tblMaterialLotVoList = getTblMaterialLotVoList("", materialCode, materialName, status, remarks, loginUser.getLangId(), 1, false, "", "", 0, 0);

        //明細データを取得
        if (null != tblMaterialLotVoList && tblMaterialLotVoList.getTblMaterialLotVo() != null) {
            ArrayList tempOutList;
            for (int i = 0; i < tblMaterialLotVoList.getTblMaterialLotVo().size(); i++) {
                TblMaterialLotVo tblMaterialLotVo = tblMaterialLotVoList.getTblMaterialLotVo().get(i);
                tempOutList = new ArrayList<>();
                tempOutList.add(String.valueOf(tblMaterialLotVo.getMaterialCode()));
                tempOutList.add(String.valueOf(tblMaterialLotVo.getMaterialName()));
                tempOutList.add(String.valueOf(tblMaterialLotVo.getLotNo()));
                tempOutList.add(String.valueOf(tblMaterialLotVo.getLotQuantity()));
                tempOutList.add(String.valueOf(tblMaterialLotVo.getStockQuantity()));
                tempOutList.add(String.valueOf(tblMaterialLotVo.getStatusText()));
                tempOutList.add(String.valueOf(tblMaterialLotVo.getRemarks01()));
                tempOutList.add(String.valueOf(tblMaterialLotVo.getLotIssueDateCsv()));
                tempOutList.add("");
                gLineList.add(tempOutList);
            }
        }

        FileReponse fileReponse = new FileReponse();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable("tbl_material_lot");
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MATERIAL_STOCK_QUANTITY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(dictMap.get("material_lot_registration")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

    /**
     *
     * @param materialId
     * @param materialCode
     * @param materialName
     * @param status
     * @param remarks
     * @param langId
     * @param searchFlg
     * @param isPage
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public TblMaterialLotVoList getTblMaterialLotVoList(String materialId, String materialCode, String materialName, int status, String remarks,
            String langId, int searchFlg, boolean isPage, String sidx, String sord, int pageNumber, int pageSize
    ) {
        TblMaterialLotVoList tblMaterialLotVoList = new TblMaterialLotVoList();

        List list = getTblMaterialLotListSql(materialId, materialCode, materialName, status, remarks, isPage, sidx, sord, pageNumber, pageSize, true);
        if (list.size() > 0) {
            // ページをめぐる
            if (isPage) {
                Pager pager = new Pager();
                tblMaterialLotVoList.setPageNumber(pageNumber);
                long counts = (long) list.get(0);
                tblMaterialLotVoList.setCount(counts);
                tblMaterialLotVoList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
            }

            list = getTblMaterialLotListSql(materialId, materialCode, materialName, status, remarks, isPage, sidx, sord, pageNumber, pageSize, false);
            TblMaterialLotVo tblMaterialLotVo;
            List<TblMaterialLotVo> tblMaterialLotVos = new ArrayList();
            FileUtil fileUtil = new FileUtil();
            Map<String, String> map = mstChoiceService.getChoiceMap(langId, new String[]{tbl_material_lot_status});
            for (int i = 0; i < list.size(); i++) {
                tblMaterialLotVo = new TblMaterialLotVo();
                TblMaterialLot tblMaterialLot = (TblMaterialLot) list.get(i);

                tblMaterialLotVo.setUuid(tblMaterialLot.getUuid());
                tblMaterialLotVo.setLotQuantity(tblMaterialLot.getLotQuantity());
                tblMaterialLotVo.setStockQuantity(tblMaterialLot.getStockQuantity());
                tblMaterialLotVo.setLotNo(tblMaterialLot.getLotNo());
                tblMaterialLotVo.setMaterialId(tblMaterialLot.getMaterialId());
                if (tblMaterialLot.getMstMaterial() != null) {
                    tblMaterialLotVo.setMaterialCode(tblMaterialLot.getMstMaterial().getMaterialCode());
                    tblMaterialLotVo.setMaterialName(tblMaterialLot.getMstMaterial().getMaterialName());
                } else {
                    tblMaterialLotVo.setMaterialCode("");
                    tblMaterialLotVo.setMaterialName("");
                }

                //状態
                tblMaterialLotVo.setStatus(tblMaterialLot.getStatus());
                tblMaterialLotVo.setStatusText(FileUtil.getStringValue(map.get(tbl_material_lot_status + tblMaterialLot.getStatus())));
                //コメント01
                tblMaterialLotVo.setRemarks01(FileUtil.getStringValue(tblMaterialLot.getRemarks01()));
                //コメント02
                tblMaterialLotVo.setRemarks02(FileUtil.getStringValue(tblMaterialLot.getRemarks02()));
                //コメント03
                tblMaterialLotVo.setRemarks03(FileUtil.getStringValue(tblMaterialLot.getRemarks03()));
                //コメント04
                tblMaterialLotVo.setRemarks04(FileUtil.getStringValue(tblMaterialLot.getRemarks04()));
                //コメント05
                tblMaterialLotVo.setRemarks05(FileUtil.getStringValue(tblMaterialLot.getRemarks05()));

                tblMaterialLotVo.setLotIssueDateCsv(fileUtil.getDateTimeFormatForStr(tblMaterialLot.getLotIssueDate()));
                tblMaterialLotVo.setLotIssueDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblMaterialLot.getLotIssueDate()));

                tblMaterialLotVo.setOperationFlag("2");
                tblMaterialLotVos.add(tblMaterialLotVo);
            }
            tblMaterialLotVoList.setTblMaterialLotVo(tblMaterialLotVos);
        }

        return tblMaterialLotVoList;
    }

    /**
     *
     * @param materialId
     * @param moveDateFrom
     * @param moveDateTo
     * @param langId
     * @param searchFlg
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    private List getTblMaterialLotListSql(
            String materialId,
            String materialCode,
            String materialName,
            int status,
            String remarks,
            boolean isPage,
            String sidx, // ソートキー
            String sord, // ソート順
            int pageNumber, // ページNo
            int pageSize, // ページSize
            boolean isCount
    ) {

        StringBuilder sql;
        if (isPage && isCount) {
            sql = new StringBuilder(" SELECT COUNT(1) ");
        } else {
            sql = new StringBuilder(" SELECT tblMaterialLot ");
        }
        sql.append("  FROM TblMaterialLot tblMaterialLot LEFT JOIN FETCH tblMaterialLot.mstMaterial mstMaterial "
                + " WHERE 1=1 ");
        if (StringUtils.isNotEmpty(materialId)) {
            sql.append(" AND tblMaterialLot.materialId = :materialId ");
        }

        if (StringUtils.isNotEmpty(materialCode)) {
            sql.append(" AND mstMaterial.materialCode LIKE :materialCode ");
        }

        if (StringUtils.isNotEmpty(materialName)) {
            sql.append(" AND mstMaterial.materialName LIKE :materialName ");
        }

        if (StringUtils.isNotEmpty(remarks)) {
            sql.append(" AND tblMaterialLot.remarks01 LIKE :remarks ");
        }

        if (status > 0) {
            sql.append(" AND tblMaterialLot.status = :status ");
        }

        if (!isCount) {
            if (isPage && StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                // 表示順
                sql.append(sortStr);

            } else {
                // 表示順はロット番号の降順。
                sql.append(" ORDER BY tblMaterialLot.lotNo  DESC ");
            }
        }

        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotEmpty(materialId)) {
            query.setParameter("materialId", materialId);
        }

        if (StringUtils.isNotEmpty(materialCode)) {
            query.setParameter("materialCode", "%" + materialCode + "%");
        }

        if (StringUtils.isNotEmpty(materialName)) {
            query.setParameter("materialName", "%" + materialName + "%");
        }

        if (StringUtils.isNotEmpty(remarks)) {
            query.setParameter("remarks", "%" + remarks + "%");
        }

        if (status > 0) {
            query.setParameter("status", status);
        }

        // 画面改ページを設定する
        if (isPage && !isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();
        return list;
    }

    /**
     *
     * @param tblMaterialLotVos
     * @param userUuid
     * @return
     */
    @Transactional
    public BasicResponse updateTblMaterialLot(TblMaterialLotVoList tblMaterialLotVos, String userUuid, String langId) {

        if (tblMaterialLotVos != null && tblMaterialLotVos.getTblMaterialLotVo() != null && !tblMaterialLotVos.getTblMaterialLotVo().isEmpty()) {

            for (TblMaterialLotVo tblMaterialLotVo : tblMaterialLotVos.getTblMaterialLotVo()) {

                TblMaterialLot tblMaterialLot = getSingleResultTblMaterialLot(tblMaterialLotVo.getMaterialId(), tblMaterialLotVo.getLotNo());
                if (StringUtils.isNotEmpty(tblMaterialLotVo.getOperationFlag()) && "1".equals(tblMaterialLotVo.getOperationFlag()) && StringUtils.isNotEmpty(tblMaterialLotVo.getUuid()) && tblMaterialLot != null) {//Delete
                    deleteTblMaterialLotByUuid(tblMaterialLotVo.getUuid());
                    tblMaterialStockService.doMaterialStock(tblMaterialLotVo.getMaterialCode(), tblMaterialLotVo.getLotNo(), CommonConstants.DELIVERY_DISCARD, tblMaterialLot.getStockQuantity(), DateFormat.getCurrentDateTime(), null, 0, null, userUuid, langId, true);
                    continue;
                }

                if (StringUtils.isNotEmpty(tblMaterialLotVo.getMaterialId()) && StringUtils.isNotEmpty(tblMaterialLotVo.getLotNo())) {

                    if (tblMaterialLot != null) {

                        if (tblMaterialLotVo.getLotQuantity() != null) {
                            tblMaterialLot.setLotQuantity(tblMaterialLotVo.getLotQuantity());
                        } else {
                            tblMaterialLot.setLotQuantity(new BigDecimal(0.000));
                        }

                        if (tblMaterialLotVo.getStockQuantity() != null) {
                            tblMaterialLot.setStockQuantity(tblMaterialLotVo.getStockQuantity());
                        } else {
                            tblMaterialLot.setStockQuantity(new BigDecimal(0.000));
                        }

                        tblMaterialLot.setStatus(tblMaterialLotVo.getStatus());

                        tblMaterialLot.setRemarks01(tblMaterialLotVo.getRemarks01());

                        tblMaterialLot.setUpdateDate(new Date());
                        tblMaterialLot.setUpdateUserUuid(userUuid);
                        entityManager.merge(tblMaterialLot);

                    }
                }
            }
        }

        BasicResponse basicResponse = new BasicResponse();
        return basicResponse;
    }

    /**
     *
     * @param uuid
     */
    @Transactional
    private void deleteTblMaterialLotByUuid(String uuid) {
        StringBuilder sql;
        sql = new StringBuilder("DELETE FROM  TblMaterialLot tblMaterialLot "
                + " WHERE tblMaterialLot.uuid =:uuid "
        );

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("uuid", uuid);
        query.executeUpdate();
    }

    private TblMaterialLot getSingleResultTblMaterialLot(String materialId, String lotNo) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT tblMaterialLot FROM  TblMaterialLot tblMaterialLot "
                + " WHERE tblMaterialLot.materialId =:materialId "
                + " AND tblMaterialLot.lotNo =:lotNo "
        );

        try {
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("materialId", materialId);
            query.setParameter("lotNo", lotNo);
            TblMaterialLot tblMaterialLot = (TblMaterialLot) query.getSingleResult();

            return tblMaterialLot;
        } catch (NoResultException e) {
            return null;
        }
    }

    private TblMaterialLot getSingleResultTblMaterialLotByCode(String materialCode, String lotNo) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT tblMaterialLot FROM  TblMaterialLot tblMaterialLot LEFT JOIN FETCH tblMaterialLot.mstMaterial mstMaterial "
                + " WHERE mstMaterial.materialCode =:materialCode "
                + " AND tblMaterialLot.lotNo =:lotNo "
        );

        try {
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("materialCode", materialCode);
            query.setParameter("lotNo", lotNo);
            TblMaterialLot tblMaterialLot = (TblMaterialLot) query.getSingleResult();

            return tblMaterialLot;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * csv入出庫処理
     *
     * @param fileUuid
     * @param storeDelivery
     * @param loginUser
     * @return
     */
    @Transactional
    public ImportResultResponse postTblMaterialLotCsvStoreDelivery(String fileUuid, int storeDelivery, LoginUser loginUser) {
        ImportResultResponse importResultResponse = new ImportResultResponse();

        //①CSVファイルを取込み
        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long failedCount = 0;
        long deletedCount = 0;
        try {
            String logFileUuid = IDGenerator.generate();
            importResultResponse.setLog(logFileUuid);
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

                String userLangId = loginUser.getLangId();
                String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

                ArrayList dictKeyList = new ArrayList();

                dictKeyList.add("row_number");
                dictKeyList.add("error_detail");
                dictKeyList.add("error");
                dictKeyList.add("msg_error_wrong_csv_layout");
                dictKeyList.add("msg_data_modified");
                dictKeyList.add("mst_error_record_not_found");
                dictKeyList.add("msg_record_added");
                dictKeyList.add("msg_record_deleted");
                dictKeyList.add("db_process");
                dictKeyList.add("msg_error_value_invalid");
                dictKeyList.add("msg_error_not_null");
                dictKeyList.add("msg_error_over_length");
                dictKeyList.add("material_lot_registration");

                dictKeyList.add("material_code");
                dictKeyList.add("lot_number");
                dictKeyList.add("material_lot_status");
                dictKeyList.add("issue_remarks");
                dictKeyList.add("quantity");
                dictKeyList.add("stock_quantity");
                dictKeyList.add("lot_issue_datetime");
                dictKeyList.add("msg_error_num_not_negative");

                Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(userLangId, dictKeyList);

                Map<String, Integer> inMap = new HashMap<>();
                MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_material_lot.status");

                for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                    inMap.put(mstChoice.getChoice(), Integer.parseInt(mstChoice.getMstChoicePK().getSeq()));
                }

                FileUtil fileUtil = new FileUtil();
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);

                    if (comList.size() != 9) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_code"), "", dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_wrong_csv_layout")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    String materialCode = String.valueOf(comList.get(0)).trim();
                    if (StringUtils.isEmpty(materialCode)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_code"), materialCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    String lotNumber = String.valueOf(comList.get(2)).trim();
                    if (StringUtils.isEmpty(lotNumber)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("lot_number"), lotNumber, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    } else if (fileUtil.maxLangthCheck(lotNumber, 45)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("lot_number"), lotNumber, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    String quantity = String.valueOf(comList.get(3)).trim();
                    if (StringUtils.isNotEmpty(quantity)) {
                        try {

                            float quantityF = Float.parseFloat(quantity);
                            if (quantityF < 0F) {
                                //エラー情報をログファイルに記入
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("quantity"), quantity, dictMap.get("error"), 1, dictMap.get("error_detail"), String.format(dictMap.get("msg_error_num_not_negative"), dictMap.get("quantity"))));
                                failedCount = failedCount + 1;
                                continue;
                            }

                            if (isNumeric(quantity, 6, 3)) {
                                //エラー情報をログファイルに記入
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("quantity"), quantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                                failedCount = failedCount + 1;
                                continue;
                            }

                        } catch (NumberFormatException e) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("quantity"), quantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    } else {
                        quantity = "0.000";
                    }

                    String stockQuantity = String.valueOf(comList.get(4)).trim();
                    if (StringUtils.isNotEmpty(stockQuantity)) {
                        try {

                            float stockQuantityF = Float.parseFloat(stockQuantity);
                            if (stockQuantityF < 0F) {
                                //エラー情報をログファイルに記入
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("stock_quantity"), stockQuantity, dictMap.get("error"), 1, dictMap.get("error_detail"), String.format(dictMap.get("msg_error_num_not_negative"), dictMap.get("stock_quantity"))));
                                failedCount = failedCount + 1;
                                continue;
                            }

                            if (isNumeric(stockQuantity, 6, 3)) {
                                //エラー情報をログファイルに記入
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("stock_quantity"), stockQuantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                                failedCount = failedCount + 1;
                                continue;
                            }

                        } catch (NumberFormatException e) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("stock_quantity"), stockQuantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    } else {
                        stockQuantity = "0.000";
                    }

                    int status = 0;
                    String productionStatus = String.valueOf(comList.get(5)).trim();
                    if (StringUtils.isNotEmpty(productionStatus)) {
                        if (inMap.get(productionStatus) == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_lot_status"), productionStatus, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        } else {
                            status = inMap.get(productionStatus);
                        }
                    }

                    String remarks01 = String.valueOf(comList.get(6)).trim();
                    if (fileUtil.maxLangthCheck(remarks01, 200)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("issue_remarks"), remarks01, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    String lotIssueDate = String.valueOf(comList.get(7)).trim();
                    if (StringUtils.isEmpty(lotIssueDate)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("lot_issue_datetime"), lotIssueDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    if (lotIssueDate.length() < 11) {
                        lotIssueDate += CommonConstants.SYS_MIN_TIME;
                    }
                    String lotIssueDateFormat = DateFormat.formatDateYear(lotIssueDate, DateFormat.DATETIME_FORMAT);
                    if ("-1".equals(lotIssueDateFormat)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("lot_issue_datetime"), lotIssueDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    TblMaterialLotVo tblMaterialLotVo = new TblMaterialLotVo();
                    tblMaterialLotVo.setMaterialCode(materialCode);
                    tblMaterialLotVo.setLotNo(lotNumber);
                    tblMaterialLotVo.setStatus(status);
                    tblMaterialLotVo.setRemarks01(remarks01);
                    tblMaterialLotVo.setLotIssueDate(lotIssueDateFormat);
                    tblMaterialLotVo.setStockQuantity(new BigDecimal(stockQuantity));
                    tblMaterialLotVo.setLotQuantity(new BigDecimal(quantity));

                    String deleteFlg = String.valueOf(comList.get(comList.size() - 1)).trim();
                    if ("1".equals(deleteFlg)) {
                        TblMaterialLot tblMaterialLot = getSingleResultTblMaterialLotByCode(tblMaterialLotVo.getMaterialCode(), tblMaterialLotVo.getLotNo());
                        if (tblMaterialLot != null) {
                            deleteTblMaterialLotByUuid(tblMaterialLot.getUuid());
                            tblMaterialStockService.doMaterialStock(tblMaterialLotVo.getMaterialCode(), tblMaterialLotVo.getLotNo(), CommonConstants.DELIVERY_DISCARD, tblMaterialLot.getStockQuantity(), DateFormat.getCurrentDateTime(), null, status, remarks01, loginUser.getUserUuid(), loginUser.getLangId(), true);
                        }
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_code"), materialCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_deleted")));
                        deletedCount = deletedCount + 1;
                        continue;
                    }

                    postTblMaterialLotStoreDelivery(tblMaterialLotVo, storeDelivery, loginUser);

                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_code"), materialCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_data_modified")));
                    updatedCount = updatedCount + 1;
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
                tblCsvImport.setImportTable("tbl_material_lot");

                TblUploadFile tblUploadFile = new TblUploadFile();
                tblUploadFile.setFileUuid(fileUuid);
                tblCsvImport.setUploadFileUuid(tblUploadFile);
                MstFunction mstFunction = new MstFunction();
                mstFunction.setId(CommonConstants.FUN_ID_MATERIAL_STOCK_QUANTITY);
                tblCsvImport.setFunctionId(mstFunction);
                tblCsvImport.setRecordCount(readList.size() - 1);
                tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
                tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
                tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
                tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
                tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
                tblCsvImport.setLogFileUuid(logFileUuid);
                tblCsvImport.setLogFileName(FileUtil.getLogFileName(dictMap.get("material_lot_registration")));

                tblCsvImportService.createCsvImpor(tblCsvImport);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return importResultResponse;
    }

    /**
     * 画面から入出庫処理
     *
     * @param tblMaterialLotVo
     * @param storeDelivery
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postTblMaterialLotStoreDelivery(TblMaterialLotVo tblMaterialLotVo, int storeDelivery, LoginUser loginUser) {
        String materialCode = tblMaterialLotVo.getMaterialCode();
        String lotNumber = tblMaterialLotVo.getLotNo();
        BigDecimal quantity = tblMaterialLotVo.getLotQuantity();
        String stockChangeDate = tblMaterialLotVo.getLotIssueDate();
        String productionDetailId = null;
        String userUuid = loginUser.getUserUuid();
        String langId = loginUser.getLangId();
        int status = tblMaterialLotVo.getStatus();
        String remarks01 = tblMaterialLotVo.getRemarks01();

        return tblMaterialStockService.doMaterialStock(materialCode, lotNumber, storeDelivery, quantity, stockChangeDate, productionDetailId, status, remarks01, userUuid, langId, false);
    }

    /**
     *
     * @param value
     * @param intLength 整数位
     * @param decimalLength　小数位
     * @return
     */
    private boolean isNumeric(String value, int intLength, int decimalLength) {

        String[] arrValue = value.split("\\.");
        if (arrValue.length > 0) {
            String strInt = arrValue[0];
            if (strInt.length() > intLength) {
                return true;
            }
        }

        if (arrValue.length > 1) {
            String strDecimal = arrValue[1];
            if (strDecimal.length() > decimalLength) {
                return true;
            }
        }

        return false;
    }

    /**
     * ロット番号採番
     *
     * @param materialId 材料ＩＤ
     * @return
     */
    public String makeNewLotNumber(String materialId) {
        /**
         * ロット番号を採番
         *
         */
        // 正規表現検索文字列
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String searchLotNumber = dateFormat.format(new Date());

        // LIKE検索で現在の番号を取得
        String maxLotNumber = getMaxLotNumber(materialId, searchLotNumber);
        if (maxLotNumber != null) {
            try {
                int currentLotNumberBranch = Integer.parseInt(maxLotNumber.replaceAll(searchLotNumber, "").replaceFirst("-", ""));
                currentLotNumberBranch++;
                return searchLotNumber + "-" + String.format("%02d", currentLotNumberBranch);
            } catch (NumberFormatException numberFormatException) {
                return searchLotNumber + "-" + "01";
            }
        } // 存在しない場合は01を付与して設定
        else {
            return searchLotNumber + "-" + "01";
        }
    }

    /**
     * ロット番号 正規表現LIKE検索 最大値取得 指定されたロット番号の - NN であるロット番号の最大値を取得
     * パラメータが「20160101」)なら ⇒ REGEX '20160101-[0-9]*$' で検索
     *
     * @param materialId
     * @param searchLotNumber
     * @return
     */
    public String getMaxLotNumber(String materialId, String searchLotNumber) {
        /* 
         * JPQLで条件定義
         *   結合条件はエンティティクラスに定義済み
         */
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblMaterialLot.lotNo FROM  TblMaterialLot tblMaterialLot WHERE 1=1 "
        );

        sql.append(" AND tblMaterialLot.materialId =:materialId ");
        sql.append(" AND tblMaterialLot.lotNo REGEXP :searchLotNumber ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("materialId", materialId);
        // 正規表現部分を追加
        String regexpSearchLotNumber = searchLotNumber + "-[0-9]*$";
        query.setParameter("searchLotNumber", regexpSearchLotNumber);

        List<String> lotNumbers = query.getResultList();
        if (lotNumbers == null || lotNumbers.size() < 1) {
            return null;
        }
        String maxLotNumber = null;
        int maxLotNumberSuffix = 0;
        for (String lotNumber : lotNumbers) {
            try {
                // 末尾の数値部分を比較
                int tmpMaxLotNumberSuffix = Integer.parseInt(lotNumber.replaceAll(searchLotNumber, "").replaceFirst("-", ""));
                if (tmpMaxLotNumberSuffix >= maxLotNumberSuffix) {
                    maxLotNumber = lotNumber;
                    maxLotNumberSuffix = tmpMaxLotNumberSuffix;
                }
            } catch (NumberFormatException numberFormatException) {
                return null;
            }
        }
        return maxLotNumber;
    }

    public TblMaterialLotVoList getProductionTblMaterialLotList(String materialId) {
        TblMaterialLotVoList response = new TblMaterialLotVoList();
        if (StringUtils.isNotEmpty(materialId)) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ml FROM TblMaterialLot ml ");
            sql.append(" WHERE ml.stockQuantity > 0 ");
            sql.append(" AND ml.materialId = :materialId");
            sql.append(" ORDER BY ml.lotNo ASC ");
            
            Query query = entityManager.createQuery(sql.toString());
            
            query.setParameter("materialId", materialId);
            
            List<TblMaterialLot> list = query.getResultList();
            response.setTblMaterialLots(list);
        }
        return response;
    }
}
