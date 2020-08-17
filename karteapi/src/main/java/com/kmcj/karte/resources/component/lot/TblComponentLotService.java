/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot;

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
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.stock.TblStockService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
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
 * poサービス
 *
 * @author admin
 */
@Dependent
public class TblComponentLotService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MstComponentService mstComponentService;

    @Inject
    private TblStockService tblStockService;

    @Inject
    private MstProcedureService mstProcedureService;
    
    @Inject
    private TblComponentLotRelationService tblComponentLotRelationService;

    private String tbl_component_lot_status = "tbl_component_lot.status";

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();

        orderKey.put("componentCode", " ORDER BY tblComponentLot.mstComponent.componentCode ");//部品コード
        orderKey.put("componentName", " ORDER BY tblComponentLot.mstComponent.componentName ");//部品名称
        orderKey.put("lotNo", " ORDER BY tblComponentLot.lotNo ");//ロット番号
        orderKey.put("procedureCode", " ORDER BY tblComponentLot.procedureCode ");//工程番号
        orderKey.put("stockQty", " ORDER BY tblComponentLot.stockQty ");//在庫数
        orderKey.put("lotQty", " ORDER BY tblComponentLot.lotQty ");//数量
        orderKey.put("statusText", " ORDER BY tblComponentLot.status ");//状態
        orderKey.put("lotIssueDate", " ORDER BY tblComponentLot.lotIssueDate ");//ロット発行日
        orderKey.put("remarks01", " ORDER BY tblComponentLot.remarks01 ");//コメント

    }

    /**
     *
     * @param componentCode
     * @param status
     * @param remarks
     * @param langId
     * @param userUuid
     * @return
     */
    public FileReponse getTblComponentLotCsv(String componentCode, int status, String remarks, String langId, String userUuid) {

        /**
         * Header
         */
        ArrayList dictKeyList = new ArrayList();
        dictKeyList.add("component_code");
        dictKeyList.add("component_name");
        dictKeyList.add("procedure_code");
        dictKeyList.add("lot_number");
        dictKeyList.add("quantity");
        dictKeyList.add("stock_quantity");
        dictKeyList.add("component_lot_status");
        dictKeyList.add("lot_issue_datetime");
        dictKeyList.add("issue_remarks");
        dictKeyList.add("delete_record");
        dictKeyList.add("component_lot_registration");

        Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeyList);

        /*Head*/
        ArrayList headList = new ArrayList();
        headList.add(dictMap.get("component_code"));
        headList.add(dictMap.get("component_name"));
        headList.add(dictMap.get("procedure_code"));
        headList.add(dictMap.get("lot_number"));
        headList.add(dictMap.get("quantity"));
        headList.add(dictMap.get("stock_quantity"));
        headList.add(dictMap.get("component_lot_status"));
        headList.add(dictMap.get("lot_issue_datetime"));
        headList.add(dictMap.get("issue_remarks"));
        headList.add(dictMap.get("delete_record"));

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(headList);

        TblComponentLotVoList tblComponentLotVoList = getTblComponentLotList("", componentCode, status, remarks, false, langId, null, null, 0, 0);

        //明細データを取得
        if (null != tblComponentLotVoList && tblComponentLotVoList.getTblComponentLotVos() != null) {
            ArrayList tempOutList;
            for (int i = 0; i < tblComponentLotVoList.getTblComponentLotVos().size(); i++) {
                TblComponentLotVo tblComponentLotVo = tblComponentLotVoList.getTblComponentLotVos().get(i);
                tempOutList = new ArrayList<>();
                tempOutList.add(String.valueOf(tblComponentLotVo.getComponentCode()));
                tempOutList.add(String.valueOf(tblComponentLotVo.getComponentName()));
                tempOutList.add(String.valueOf(tblComponentLotVo.getProcedureCode()));
                tempOutList.add(String.valueOf(tblComponentLotVo.getLotNo()));
                tempOutList.add(String.valueOf(tblComponentLotVo.getLotQty()));
                tempOutList.add(String.valueOf(tblComponentLotVo.getStockQty()));
                tempOutList.add(String.valueOf(tblComponentLotVo.getStatusText()));
                tempOutList.add(String.valueOf(tblComponentLotVo.getLotIssueDateCsv()));
                tempOutList.add(String.valueOf(tblComponentLotVo.getRemarks01()));
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
        tblCsvExport.setExportTable("tbl_component_lot");
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_STOCK_QUANTITY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(userUuid);

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(dictMap.get("component_lot_registration")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

    /**
     * 画面で追加・変更・削除した内容を部品ロットテーブルへ更新する。
     *
     * @param tblComponentLotVoList
     * @param langId
     * @param userUuid
     * @return
     */
    @Transactional
    public BasicResponse postTblComponentLot(TblComponentLotVoList tblComponentLotVoList, String langId, String userUuid) {
        BasicResponse basicResponse = new BasicResponse();
        if (tblComponentLotVoList != null && tblComponentLotVoList.getTblComponentLotVos() != null && !tblComponentLotVoList.getTblComponentLotVos().isEmpty()) {
            Date sysDate = new Date();
            for (TblComponentLotVo tblComponentLotVo : tblComponentLotVoList.getTblComponentLotVos()) {
                TblComponentLot tblComponentLot = entityManager.find(TblComponentLot.class, tblComponentLotVo.getUuid());
                
                if (tblComponentLot != null) {
                    if (StringUtils.isNotEmpty(tblComponentLotVo.getOperationFlag()) && "1".equals(tblComponentLotVo.getOperationFlag()) && StringUtils.isNotEmpty(tblComponentLotVo.getUuid())) {
                        MstProcedure mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(tblComponentLot.getComponentId(), tblComponentLot.getProcedureCode());
                        //在庫管理テーブルからも在庫数を減算する
                        TblComponentLotRelationVoList tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationListByIdAndLotId(tblComponentLot.getComponentId(), tblComponentLot.getUuid());
                        basicResponse = tblStockService.doTblStock(
                            tblComponentLot.getMstComponent().getComponentCode(),
                            mstProcedure,
                            null,
                            CommonConstants.DELIVERY_DISCARD,
                            tblComponentLot.getLotQty(),
                            DateFormat.getCurrentDateTime(),
                            tblComponentLot.getLotNo(),
                            0,
                            null,
                            CommonConstants.SHIPMENT_NO,
                            tblComponentLotRelationVoList,
                            userUuid,
                            langId);

                        if (basicResponse.isError()) {
                            basicResponse.setError(true);
                            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                            basicResponse.setErrorMessage(basicResponse.getErrorMessage());

                            return basicResponse;
                        }
                        //選択した行の部品ロットを削除する。
                        deleteTblComponentLotByUuid(tblComponentLotVo.getUuid());
                        continue;
                    }
                
                    //	数量
                    tblComponentLot.setLotQty(tblComponentLotVo.getLotQty());
                    //	在庫数
                    tblComponentLot.setStockQty(tblComponentLotVo.getStockQty());
                    //	状態
                    tblComponentLot.setStatus(tblComponentLotVo.getStatus());
                    //	コメント
                    tblComponentLot.setRemarks01(tblComponentLotVo.getRemarks01());
                    tblComponentLot.setUpdateDate(sysDate);
                    tblComponentLot.setUpdateUserUuid(userUuid);
                    entityManager.merge(tblComponentLot);
                }
            }
        }
        
        return basicResponse;
    }

    /**
     * 在庫数登録画面で選択した部品の部品ロットを検索して一覧表示する。 部品コードを変更して検索も可能。
     *
     * @param componentId
     * @param componentCode
     * @param status
     * @param remarks
     * @param isPage
     * @param langId
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public TblComponentLotVoList getTblComponentLotList(String componentId, String componentCode, int status, String remarks, boolean isPage, String langId, String sidx, String sord, int pageNumber, int pageSize) {
        TblComponentLotVoList tblComponentLotVoList = new TblComponentLotVoList();

        List list = getTblComponentLotSql(componentId, componentCode, status, remarks, sidx, sord, pageNumber, pageSize, true, isPage);

        if (list.size() > 0) {
            if (isPage) {
                // ページをめぐる
                Pager pager = new Pager();
                tblComponentLotVoList.setPageNumber(pageNumber);
                long counts = (long) list.get(0);
                tblComponentLotVoList.setCount(counts);
                tblComponentLotVoList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
            }

            list = getTblComponentLotSql(componentId, componentCode, status, remarks, sidx, sord, pageNumber, pageSize, false, isPage);

            TblComponentLotVo tblComponentLotVo;
            List<TblComponentLotVo> tblComponentLotVos = new ArrayList();

            Map<String, String> map = mstChoiceService.getChoiceMap(langId, new String[]{tbl_component_lot_status});
            FileUtil fileUtil = new FileUtil();
            for (int i = 0; i < list.size(); i++) {
                tblComponentLotVo = new TblComponentLotVo();
                TblComponentLot tblComponentLot = (TblComponentLot) list.get(i);

                //ID
                tblComponentLotVo.setUuid(tblComponentLot.getUuid());
                //部品ID
                //在庫管理ID

                if (tblComponentLot.getTblStock() != null && tblComponentLot.getMstComponent() != null) {
                    MstComponent mstComponent = tblComponentLot.getMstComponent();
                    //在庫管理テーブルの部品IDから部品名を表示
                    tblComponentLotVo.setComponentId(mstComponent.getId());
                    tblComponentLotVo.setComponentCode(mstComponent.getComponentCode());
                    tblComponentLotVo.setComponentName(mstComponent.getComponentName());
                } else {
                    tblComponentLotVo.setComponentId("");
                    tblComponentLotVo.setComponentCode("");
                    tblComponentLotVo.setComponentName("");
                }
                //工程番号
                tblComponentLotVo.setProcedureCode(FileUtil.getStringValue(tblComponentLot.getProcedureCode()));
                //ロット番号
                tblComponentLotVo.setLotNo(FileUtil.getStringValue(tblComponentLot.getLotNo()));
                //ロット発行日
                tblComponentLotVo.setLotIssueDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblComponentLot.getLotIssueDate()));
                tblComponentLotVo.setLotIssueDateCsv(fileUtil.getDateTimeFormatForStr(tblComponentLot.getLotIssueDate()));
                //数量
                tblComponentLotVo.setLotQty(tblComponentLot.getLotQty());
                //在庫数
                tblComponentLotVo.setStockQty(tblComponentLot.getStockQty());
                //状態
                tblComponentLotVo.setStatus(tblComponentLot.getStatus());
                tblComponentLotVo.setStatusText(FileUtil.getStringValue(map.get(tbl_component_lot_status + tblComponentLot.getStatus())));
                //コメント01
                tblComponentLotVo.setRemarks01(FileUtil.getStringValue(tblComponentLot.getRemarks01()));
                //コメント02
                tblComponentLotVo.setRemarks02(FileUtil.getStringValue(tblComponentLot.getRemarks02()));
                //コメント03
                tblComponentLotVo.setRemarks03(FileUtil.getStringValue(tblComponentLot.getRemarks03()));
                //コメント04
                tblComponentLotVo.setRemarks04(FileUtil.getStringValue(tblComponentLot.getRemarks04()));
                //コメント05
                tblComponentLotVo.setRemarks05(FileUtil.getStringValue(tblComponentLot.getRemarks05()));
                tblComponentLotVo.setOperationFlag("2");
                tblComponentLotVos.add(tblComponentLotVo);
            }
            tblComponentLotVoList.setTblComponentLotVos(tblComponentLotVos);
        }

        return tblComponentLotVoList;

    }

    /**
     *
     * @param componentCode
     * @param status
     * @param remarks
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @param isPage
     * @return
     */
    private List getTblComponentLotSql(String componentId, String componentCode, int status, String remarks, String sidx, String sord, int pageNumber, int pageSize, boolean isCount, boolean isPage) {

        StringBuilder sql;
        if (isPage && isCount) {
            sql = new StringBuilder(" SELECT COUNT(1) ");
        } else {
            sql = new StringBuilder(" SELECT tblComponentLot ");
        }
        sql.append("  FROM TblComponentLot tblComponentLot "
                + " LEFT JOIN FETCH tblComponentLot.mstComponent mstComponent "
                + " WHERE 1=1 ");
        if (status > 0) {
            sql.append(" AND tblComponentLot.status = :status ");
        }

        if (StringUtils.isNotEmpty(componentId)) {
            sql.append(" AND tblComponentLot.componentId = :componentId ");
        }

        if (StringUtils.isNotEmpty(componentCode)) {
            sql.append(" AND mstComponent.componentCode LIKE :componentCode ");
        }

        if (StringUtils.isNotEmpty(remarks)) {
            sql.append(" AND tblComponentLot.remarks01 LIKE :remarks ");
        }

        if (!isCount) {
            if (isPage && StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                // 表示順
                sql.append(sortStr);

            } else {
                // 表示順はロット発行日の降順。
                sql.append(" ORDER BY tblComponentLot.lotIssueDate  DESC ");
            }
        }

        Query query = entityManager.createQuery(sql.toString());
        if (status > 0) {
            query.setParameter("status", status);
        }
        if (StringUtils.isNotEmpty(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }

        if (StringUtils.isNotEmpty(componentId)) {
            query.setParameter("componentId", componentId);
        }

        if (StringUtils.isNotEmpty(remarks)) {
            query.setParameter("remarks", "%" + remarks + "%");
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
     * @param tblComponentLotVo
     * @param storeDelivery 分類　：　入庫（STORE）/出庫（DELIVERY）
     * @param loginUser
     * @return
     */
    public BasicResponse postTblComponentLotStoreDelivery(TblComponentLotVo tblComponentLotVo, int storeDelivery, LoginUser loginUser) {
        //     在庫管理クラスへ渡す引数
        //	部品コード　：　ダイアログで指定した部品コード
        String componentCode = tblComponentLotVo.getComponentCode();
        String componentId = tblComponentLotVo.getComponentId();
        //	部品ロット番号　：　ダイアログで入力したロット番号
        String lotNo = tblComponentLotVo.getLotNo();
        //	分類　：　入庫（STORE）/出庫（DELIVERY）
        //	数量　：　ダイアログで入力した数量
        int quantity = tblComponentLotVo.getLotQty();
        //	在庫変更日　：　ダイアログの在庫入庫日
        String stockChangeDate = tblComponentLotVo.getLotIssueDate();
        //	状態　：　ダイアログで選択した分類（分類項目の連番）
        int status = tblComponentLotVo.getStatus();
        //	コメント　：　ダイアログのコメント
        String remarks01 = tblComponentLotVo.getRemarks01();

        MstProcedure mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(componentId, tblComponentLotVo.getProcedureCode());
        BasicResponse basicResponse = new BasicResponse();
        if (mstProcedure != null) {
            basicResponse = tblStockService.doTblStock(componentCode, mstProcedure, null,
                    storeDelivery, quantity, stockChangeDate, lotNo, status, remarks01, CommonConstants.SHIPMENT_NO, null, loginUser.getUserUuid(), loginUser.getLangId());
        }

        return basicResponse;
    }

    /**
     *
     * @param fileUuid
     * @param storeDelivery
     * @param loginUser
     * @return
     */
    @Transactional
    public ImportResultResponse postTblComponentLotCsvStoreDelivery(String fileUuid, int storeDelivery, LoginUser loginUser) {
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

            ArrayList<List<String>> readList = CSVFileUtil.readCsv(csvFile);
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
                dictKeyList.add("mst_error_record_not_found");
                dictKeyList.add("msg_error_value_invalid");
                dictKeyList.add("msg_error_not_null");
                dictKeyList.add("msg_error_over_length");
                dictKeyList.add("msg_error_num_not_negative");

                dictKeyList.add("db_process");
                dictKeyList.add("msg_data_modified");
                dictKeyList.add("msg_record_added");
                dictKeyList.add("msg_record_deleted");

                dictKeyList.add("component_lot_registration");

                dictKeyList.add("component_code");
                dictKeyList.add("procedure_code");
                dictKeyList.add("lot_number");
                dictKeyList.add("quantity");
                dictKeyList.add("component_lot_status");
                dictKeyList.add("lot_issue_datetime");
                dictKeyList.add("issue_remarks");
                dictKeyList.add("delete_record");

                Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(userLangId, dictKeyList);

                Map<String, Integer> colIndexMap = new HashMap<>();

                for (int j = 0; j < readList.get(0).size(); j++) {
                    if (readList.get(0).get(j).trim().equals(dictMap.get("component_code"))) {
                        colIndexMap.put("component_code", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("procedure_code"))) {
                        colIndexMap.put("procedure_code", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("lot_number"))) {
                        colIndexMap.put("lot_number", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("quantity"))) {
                        colIndexMap.put("quantity", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("lot_issue_datetime"))) {
                        colIndexMap.put("lot_issue_datetime", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("component_lot_status"))) {
                        colIndexMap.put("component_lot_status", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("issue_remarks"))) {
                        colIndexMap.put("issue_remarks", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("delete_record"))) {
                        colIndexMap.put("delete_record", j);
                    }
                }

                MstComponent mstComponent;
                MstProcedure mstProcedure;
                TblComponentLot tblComponentLot;
                TblComponentLotVo tblComponentLotVo;

                FileUtil fileUtil = new FileUtil();
                Map<String, Integer> inMap = new HashMap<>();
                MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), tbl_component_lot_status);

                for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                    inMap.put(mstChoice.getChoice(), Integer.parseInt(mstChoice.getMstChoicePK().getSeq()));
                }

                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);

                    String componentCode = null, procedureCode = null, lotNumber = null, quantity = null, lotIssueDate = null, productionStatus = null, issueRemarks = null, deleteFlg = null;
                    if (colIndexMap.get("component_code") != null) {
                        componentCode = String.valueOf(comList.get(colIndexMap.get("component_code"))).trim();
                    }
                    if (colIndexMap.get("procedure_code") != null) {
                        procedureCode = String.valueOf(comList.get(colIndexMap.get("procedure_code"))).trim();
                    }
                    if (colIndexMap.get("lot_number") != null) {
                        lotNumber = String.valueOf(comList.get(colIndexMap.get("lot_number"))).trim();
                    }
                    if (colIndexMap.get("quantity") != null) {
                        quantity = String.valueOf(comList.get(colIndexMap.get("quantity"))).trim();
                    }
                    if (colIndexMap.get("lot_issue_datetime") != null) {
                        lotIssueDate = String.valueOf(comList.get(colIndexMap.get("lot_issue_datetime"))).trim();
                    }
                    if (colIndexMap.get("component_lot_status") != null) {
                        productionStatus = String.valueOf(comList.get(colIndexMap.get("component_lot_status"))).trim();
                    }
                    if (colIndexMap.get("issue_remarks") != null) {
                        issueRemarks = String.valueOf(comList.get(colIndexMap.get("issue_remarks"))).trim();
                    }
                    if (colIndexMap.get("delete_record") != null) {
                        deleteFlg = String.valueOf(comList.get(colIndexMap.get("delete_record"))).trim();
                    }

                    if (StringUtils.isEmpty(componentCode)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isEmpty(procedureCode)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }

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

                    int status = 0;

                    if (StringUtils.isNotEmpty(productionStatus)) {
                        if (inMap.get(productionStatus) == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_lot_status"), productionStatus, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        } else {
                            status = inMap.get(productionStatus);
                        }
                    }

                    int quantityInt = 0;
                    if (StringUtils.isNotEmpty(quantity)) {
                        try {

                            quantityInt = Integer.parseInt(quantity);

                            if (quantityInt < 0) {
                                //エラー情報をログファイルに記入
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("quantity"), quantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_num_not_negative")));
                                failedCount = failedCount + 1;
                                continue;
                            }

                        } catch (NumberFormatException e) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("quantity"), quantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }

                    if (StringUtils.isEmpty(lotIssueDate)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("lot_issue_datetime"), lotIssueDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }
//                    if (lotIssueDate.length() < 11) {
//                        lotIssueDate += CommonConstants.SYS_MIN_TIME;
//                    }
                    String lotIssueDateFormat = DateFormat.formatDateYear(lotIssueDate, DateFormat.DATETIME_FORMAT);
                    if ("-1".equals(lotIssueDateFormat)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("lot_issue_datetime"), lotIssueDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    if (fileUtil.maxLangthCheck(issueRemarks, 200)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("issue_remarks"), issueRemarks, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    mstComponent = mstComponentService.getMstComponent(componentCode);
                    if (mstComponent == null) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(mstComponent.getId(), procedureCode);
                    if (mstProcedure == null) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    tblComponentLotVo = new TblComponentLotVo();
                    tblComponentLotVo.setComponentId(mstComponent.getId());
                    tblComponentLotVo.setComponentCode(componentCode);
                    tblComponentLotVo.setProcedureCode(procedureCode);
                    tblComponentLotVo.setLotNo(lotNumber);
                    tblComponentLotVo.setLotQty(quantityInt);
                    tblComponentLotVo.setStatus(status);
                    tblComponentLotVo.setRemarks01(issueRemarks);

                    if ("1".equals(deleteFlg)) {
                        tblComponentLot = getSingleResultTblComponentLot(componentCode, procedureCode, lotNumber);
                        if (tblComponentLot != null) {
                            //在庫管理テーブルからも在庫数を減算する
                            TblComponentLotRelationVoList tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationListByIdAndLotId(tblComponentLot.getComponentId(), tblComponentLot.getUuid());
                            BasicResponse basicResponse = tblStockService.doTblStock(
                                tblComponentLot.getMstComponent().getComponentCode(),
                                mstProcedure,
                                null,
                                CommonConstants.DELIVERY_DISCARD,
                                tblComponentLot.getLotQty(),
                                DateFormat.getCurrentDateTime(),
                                tblComponentLot.getLotNo(),
                                0,
                                null,
                                CommonConstants.SHIPMENT_NO,
                                tblComponentLotRelationVoList,
                                loginUser.getUserUuid(),
                                loginUser.getLangId());

                            if (basicResponse.isError()) {
                                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 0, dictMap.get("error_detail"), basicResponse.getErrorMessage()));
                                failedCount = failedCount + 1;
                                continue;
                            }
                            //選択した行の部品ロットを削除する。
                            deleteTblComponentLotByUuid(tblComponentLot.getUuid());
                        }
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_deleted")));
                        deletedCount = deletedCount + 1;
                        continue;
                    }

                    postTblComponentLotStoreDelivery(tblComponentLotVo, storeDelivery, loginUser);

                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_data_modified")));
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
                tblCsvImport.setImportTable("tbl_component_lot");

                TblUploadFile tblUploadFile = new TblUploadFile();
                tblUploadFile.setFileUuid(fileUuid);
                tblCsvImport.setUploadFileUuid(tblUploadFile);
                MstFunction mstFunction = new MstFunction();
                mstFunction.setId(CommonConstants.FUN_ID_STOCK_QUANTITY);
                tblCsvImport.setFunctionId(mstFunction);
                tblCsvImport.setRecordCount(readList.size() - 1);
                tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
                tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
                tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
                tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
                tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
                tblCsvImport.setLogFileUuid(logFileUuid);
                tblCsvImport.setLogFileName(FileUtil.getLogFileName(dictMap.get("component_lot_registration")));

                tblCsvImportService.createCsvImpor(tblCsvImport);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return importResultResponse;
    }

    public TblComponentLot getSingleResultTblComponentLot(String componentCode, String procedureCode, String lotNo) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT tblComponentLot FROM  TblComponentLot tblComponentLot JOIN FETCH tblComponentLot.mstComponent mstComponent "
                + " WHERE mstComponent.componentCode =:componentCode "
                + " AND tblComponentLot.procedureCode =:procedureCode "
                + " AND tblComponentLot.lotNo =:lotNo "
        );

        try {
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("componentCode", componentCode);
            query.setParameter("procedureCode", procedureCode);
            query.setParameter("lotNo", lotNo);
            TblComponentLot tblComponentLot = (TblComponentLot) query.getSingleResult();

            return tblComponentLot;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    private void deleteTblComponentLotByUuid(String uuid) {
        StringBuilder sql;
        sql = new StringBuilder("DELETE FROM  TblComponentLot tblComponentLot "
                + " WHERE tblComponentLot.uuid =:uuid "
        );

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("uuid", uuid);
        query.executeUpdate();
    }

    /**
     * 部品ロットテーブルを検索して部品ロット番号を表示
     *
     * @param componentLotNo
     * @param componentId
     * @param isLike
     * @return
     */
    public TblComponentLotVoList getComponentLotCsv(String componentLotNo, String componentId, boolean isLike) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT tblComponentLot FROM  TblComponentLot tblComponentLot WHERE 1=1 ");

        if (StringUtils.isNotEmpty(componentId)) {
            sql.append(" AND tblComponentLot.componentId =:componentId ");
        }

        if (StringUtils.isNotEmpty(componentLotNo) && StringUtils.isNotEmpty(componentLotNo.trim())) {
            if (isLike) {
                sql.append(" AND tblComponentLot.lotNo LIKE :componentLotNo ");
            } else {
                sql.append(" AND tblComponentLot.lotNo =:componentLotNo ");
            }
        }

        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotEmpty(componentId)) {
            query.setParameter("componentId", componentId);
        }
        if (StringUtils.isNotEmpty(componentLotNo) && StringUtils.isNotEmpty(componentLotNo.trim())) {
            if (isLike) {
                query.setParameter("componentLotNo", "%" + componentLotNo + "%");
            } else {
                query.setParameter("componentLotNo", componentLotNo);
            }
        }

        query.setMaxResults(100);
        List list = query.getResultList();

        TblComponentLotVoList tblComponentLotVoList = new TblComponentLotVoList();
        List<TblComponentLotVo> tblComponentLotVos = new ArrayList();

        TblComponentLot tblComponentLot;
        TblComponentLotVo tblComponentLotVo;

        for (int i = 0; i < list.size(); i++) {
            tblComponentLot = (TblComponentLot) list.get(i);
            tblComponentLotVo = new TblComponentLotVo();
            tblComponentLotVo.setLotNo(tblComponentLot.getLotNo());
            tblComponentLotVo.setUuid(tblComponentLot.getUuid());
            tblComponentLotVo.setComponentId(tblComponentLot.getComponentId());
            tblComponentLotVos.add(tblComponentLotVo);

        }
        tblComponentLotVoList.setTblComponentLotVos(tblComponentLotVos);
        return tblComponentLotVoList;

    }

    /**
     * ロット番号採番
     *
     * @param componentId 部品ID
     * @return
     */
    public String makeNewLotNumber(String componentId) {
        /**
         * ロット番号を採番
         *
         */
        // 正規表現検索文字列
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String searchLotNumber = dateFormat.format(new Date());

        // LIKE検索で現在の番号を取得
        String maxLotNumber = getMaxLotNumber(componentId, searchLotNumber);
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
     * @param componentId
     * @param searchLotNumber
     * @return
     */
    public String getMaxLotNumber(String componentId, String searchLotNumber) {
        /* 
         * JPQLで条件定義
         *   結合条件はエンティティクラスに定義済み
         */
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblComponentLot.lotNo FROM  TblComponentLot tblComponentLot WHERE 1=1 "
        );

        sql.append(" AND tblComponentLot.componentId =:componentId ");
        sql.append(" AND tblComponentLot.lotNo REGEXP :searchLotNumber ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("componentId", componentId);
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

    /**
     * 生産明細IDから部品ロットテーブル取得
     * @param productionDetailId
     * @return 
     */
    public TblComponentLot getComponentLotByProductionDetailId(String productionDetailId) {
        Query query = entityManager.createQuery("SELECT t FROM TblComponentLot t WHERE t.productionDetailId = :productionDetailId");
        query.setParameter("productionDetailId", productionDetailId);
        List<TblComponentLot> resultList = query.getResultList();
        if (resultList != null && resultList.size() > 0) {
            return (TblComponentLot)resultList.get(0);
        }
        else {
            return null;
        }
    }
    
    
}
