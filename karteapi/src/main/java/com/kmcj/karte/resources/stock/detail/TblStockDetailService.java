/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock.detail;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.lot.TblComponentLot;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.po.shipment.TblShipment;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
public class TblStockDetailService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("moveDate", " ORDER BY tblStockDetail.moveDate ");//入出庫日
        orderKey.put("stockType", " ORDER BY tblStockDetail.stockType ");//在庫分類
        orderKey.put("moveQuantity", " ORDER BY tblStockDetail.moveQuantity ");//入出庫数
        orderKey.put("moveCost", " ORDER BY tblStockDetail.moveQuantity ");//入出庫金額
        orderKey.put("stockQuantity", " ORDER BY tblStockDetail.stockQuantity ");//在庫数
        orderKey.put("productionLotNumber", " ORDER BY tblComponentLot.lotNo ");//ロット番号
        orderKey.put("orderNumber", " ORDER BY tblPo.orderNumber ");//発注番号
    }

    /**
     *
     * @param componentId
     * @param procedureCode
     * @param moveDateFrom
     * @param moveDateTo
     * @param langId
     * @param searchFlg
     * @param isPage
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public TblStockDetailVoList getTblStockDetailVoList(
            String componentId,
            String procedureCode,
            String moveDateFrom,
            String moveDateTo,
            String langId, int searchFlg,
            boolean isPage,
            String sidx, // ソートキー
            String sord, // ソート順
            int pageNumber, // ページNo
            int pageSize // ページSize
    ) {
        TblStockDetailVoList tblStockDetailVoList = new TblStockDetailVoList();

        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        Date formatMoveDateFrom = null;
        Date formatMoveDateTo = null;

        try {
            if (StringUtils.isNotEmpty(moveDateFrom)) {
                formatMoveDateFrom = sdf.parse(moveDateFrom);
            } else if (searchFlg == 0) {
                // 初期表示は当日から１ヶ月前と当日を表示
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());//システム日付
                calendar.add(Calendar.MONTH, -1);  //１ヶ月前
                Date dBefore = calendar.getTime();
                FileUtil fileUtil = new FileUtil();
                tblStockDetailVoList.setMoveDateFrom(fileUtil.getDateFormatForStr(dBefore));
                formatMoveDateFrom = sdf.parse(tblStockDetailVoList.getMoveDateFrom());
            }
        } catch (ParseException ex) {
        }

        if (StringUtils.isNotEmpty(moveDateTo)) {
            FileUtil fu = new FileUtil();
            formatMoveDateTo = fu.getDateTimeParseForDate(moveDateTo + CommonConstants.SYS_MAX_TIME);
        } else if (searchFlg == 0) {
            FileUtil fileUtil = new FileUtil();
            tblStockDetailVoList.setMoveDateTo(fileUtil.getDateFormatForStr(new Date()));

            FileUtil fu = new FileUtil();
            formatMoveDateTo = fu.getDateTimeParseForDate(tblStockDetailVoList.getMoveDateTo() + CommonConstants.SYS_MAX_TIME);

        }

        List list = getStockDetailSql(componentId, procedureCode, formatMoveDateFrom, formatMoveDateTo, isPage, sidx, sord, pageNumber, pageSize, true);
        if (list.size() > 0) {
            // ページをめぐる
            if (isPage) {
                Pager pager = new Pager();
                tblStockDetailVoList.setPageNumber(pageNumber);
                long counts = (long) list.get(0);
                tblStockDetailVoList.setCount(counts);
                tblStockDetailVoList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
            }

            list = getStockDetailSql(componentId, procedureCode, formatMoveDateFrom, formatMoveDateTo, isPage, sidx, sord, pageNumber, pageSize, false);

            TblStockDetailVo tblStockDetailVo;
            List<TblStockDetailVo> tblStockDetailVos = new ArrayList();
            List dictKeys = new ArrayList();
            dictKeys.add("store");//1
            dictKeys.add("delivery");//2
            dictKeys.add("store_discard");//3
            dictKeys.add("delivery_discard");//4
            Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeys);
            for (int i = 0; i < list.size(); i++) {

                tblStockDetailVo = new TblStockDetailVo();
                TblStockDetail tblStockDetail = (TblStockDetail) list.get(i);

                tblStockDetailVo.setUuid(tblStockDetail.getUuid());
                tblStockDetailVo.setMoveDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblStockDetail.getMoveDate()));

                switch (tblStockDetail.getStockType()) {
                    case CommonConstants.STORE:
                        tblStockDetailVo.setStockType(dictMap.get("store"));
                        break;
                    case CommonConstants.DELIVERY:
                        tblStockDetailVo.setStockType(dictMap.get("delivery"));
                        break;
                    case CommonConstants.STORE_DISCARD:
                        tblStockDetailVo.setStockType(dictMap.get("store_discard"));
                        break;
                    case CommonConstants.DELIVERY_DISCARD:
                        tblStockDetailVo.setStockType(dictMap.get("delivery_discard"));
                        break;
                    default:
                        tblStockDetailVo.setStockType("");
                        break;
                }

                tblStockDetailVo.setMoveQuantity(tblStockDetail.getMoveQuantity());
                tblStockDetailVo.setMoveCost(tblStockDetail.getMoveCost());
                tblStockDetailVo.setStockQuantity(tblStockDetail.getStockQuantity());
                TblShipment tblShipment = tblStockDetail.getTblShipment();
                //在庫履歴テーブルの部品ロットIDから部品ロットテーブルを参照して表示
                //tblStockDetailVo.setProductionLotNumber(tblShipment != null ? tblShipment.getProductionLotNumber() : "");
                TblComponentLot tblComponentLot = tblStockDetail.getTblComponentLot();
                tblStockDetailVo.setProductionLotNumber(tblComponentLot != null ? tblComponentLot.getLotNo() : "");
                tblStockDetailVo.setOrderNumber((tblShipment != null && tblShipment.getTblPo() != null) ? tblShipment.getTblPo().getOrderNumber() : "");

                tblStockDetailVos.add(tblStockDetailVo);

            }

            tblStockDetailVoList.setTblStockDetailVos(tblStockDetailVos);
        }

        return tblStockDetailVoList;
    }

    /**
     *
     * @param stockId
     * @param moveDateFrom
     * @param moveDateTo
     * @param loginUser
     * @return
     */
    public FileReponse getTblStockDetailVoCsv(String stockId, String procedureCode, String moveDateFrom, String moveDateTo, LoginUser loginUser) {

        /**
         * Header
         */
        ArrayList dictKeyList = new ArrayList();
        dictKeyList.add("move_date");
        dictKeyList.add("stock_type");
        dictKeyList.add("move_quantity");
        dictKeyList.add("move_cost");
        dictKeyList.add("stock_quantity");
        dictKeyList.add("lot_number");
        dictKeyList.add("order_number");
        dictKeyList.add("stock_history");

        Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(loginUser.getLangId(), dictKeyList);

        /*Head*/
        ArrayList headList = new ArrayList();
        headList.add(dictMap.get("move_date"));
        headList.add(dictMap.get("stock_type"));
        headList.add(dictMap.get("move_quantity"));
        headList.add(dictMap.get("move_cost"));
        headList.add(dictMap.get("stock_quantity"));
        headList.add(dictMap.get("lot_number"));
        headList.add(dictMap.get("order_number"));

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(headList);

        TblStockDetailVoList tblStockDetailVoList = getTblStockDetailVoList(stockId, procedureCode, moveDateFrom, moveDateTo, loginUser.getLangId(), 1, false, "", "", 0, 0);

        //明細データを取得
        if (null != tblStockDetailVoList && tblStockDetailVoList.getTblStockDetailVos() != null) {
            ArrayList tempOutList;
            for (int i = 0; i < tblStockDetailVoList.getTblStockDetailVos().size(); i++) {
                TblStockDetailVo tblStockDetailVo = tblStockDetailVoList.getTblStockDetailVos().get(i);
                tempOutList = new ArrayList<>();

                tempOutList.add(String.valueOf(tblStockDetailVo.getMoveDate()));
                tempOutList.add(String.valueOf(tblStockDetailVo.getStockType()));
                tempOutList.add(String.valueOf(tblStockDetailVo.getMoveQuantity()));
                tempOutList.add(String.valueOf(tblStockDetailVo.getMoveCost()));
                tempOutList.add(String.valueOf(tblStockDetailVo.getStockQuantity()));
                tempOutList.add(String.valueOf(tblStockDetailVo.getProductionLotNumber()));
                tempOutList.add(String.valueOf(tblStockDetailVo.getOrderNumber()));
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
        tblCsvExport.setExportTable("tbl_stock_detail");
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_STOCK_HISTORY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(dictMap.get("stock_history")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;

    }

    /**
     *
     * @param stockId
     * @param formatMoveDateFrom
     * @param formatMoveDateTo
     * @param isPage
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    private List getStockDetailSql(String componentId, String procedureCode, Date formatMoveDateFrom, Date formatMoveDateTo, boolean isPage, String sidx, String sord, int pageNumber, int pageSize, boolean isCount) {
        StringBuilder sql;
        if (isPage && isCount) {
            sql = new StringBuilder(" SELECT COUNT(1) ");
        } else {
            sql = new StringBuilder(" SELECT tblStockDetail ");
        }

        sql.append("  FROM TblStockDetail tblStockDetail "
                + " LEFT JOIN FETCH tblStockDetail.tblShipment tblShipment "
                + " LEFT JOIN FETCH tblShipment.tblPo tblPo "
                + " LEFT JOIN FETCH tblStockDetail.tblComponentLot tblComponentLot "
                + " WHERE tblStockDetail.componentId = :componentId "
                + " AND tblStockDetail.procedureCode = :procedureCode ");
        if (formatMoveDateFrom != null) {
            sql.append(" AND  tblStockDetail.moveDate >= :formatMoveDateFrom ");
        }

        if (formatMoveDateTo != null) {
            sql.append(" AND  tblStockDetail.moveDate <= :formatMoveDateTo ");
        }

        if (!isCount) {
            if (isPage && StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                // 表示順
                sql.append(sortStr);

            } else {
                // 表示順は入出庫日の降順。
                sql.append(" ORDER BY tblStockDetail.moveDate DESC, tblStockDetail.createDate DESC ");
            }
        }

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentId", componentId);
        query.setParameter("procedureCode", procedureCode);

        if (formatMoveDateFrom != null) {
            query.setParameter("formatMoveDateFrom", formatMoveDateFrom);
        }

        if (formatMoveDateTo != null) {
            query.setParameter("formatMoveDateTo", formatMoveDateTo);
        }

        // 画面改ページを設定する
        if (isPage && !isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        return query.getResultList();
    }

}
