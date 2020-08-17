/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.stock.detail;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
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
 * 材料在庫履歴 サービス
 *
 * @author admin
 */
@Dependent
public class TblMaterialStockDetailService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("moveDate", " ORDER BY tblMaterialStockDetail.moveDate ");//入出庫日
        orderKey.put("stockType", " ORDER BY tblMaterialStockDetail.stockType ");//在庫分類
        orderKey.put("moveQuantity", " ORDER BY tblMaterialStockDetail.moveQuantity ");//入出庫数
        orderKey.put("stockQuantity", " ORDER BY tblMaterialStockDetail.stockQuantity ");//在庫数
        orderKey.put("materialLotNo", " ORDER BY tblMaterialStockDetail.materialLotNo ");//ロット番号
    }

    /**
     * 選択した材料で材料在庫履歴画面を表示する
     *
     * @param materialId
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
    public TblMaterialStockDetailVoList getTblMaterialStockDetailVoList(
            String materialId,
            String moveDateFrom,
            String moveDateTo,
            String langId,
            int searchFlg,
            boolean isPage,
            String sidx, // ソートキー
            String sord, // ソート順
            int pageNumber, // ページNo
            int pageSize // ページSize
    ) {
        TblMaterialStockDetailVoList tblMaterialStockDetailVoList = new TblMaterialStockDetailVoList();
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
                tblMaterialStockDetailVoList.setMoveDateFrom(fileUtil.getDateFormatForStr(dBefore));
                formatMoveDateFrom = sdf.parse(tblMaterialStockDetailVoList.getMoveDateFrom());
            }
        } catch (ParseException ex) {
        }

        if (StringUtils.isNotEmpty(moveDateTo)) {

            FileUtil fu = new FileUtil();
            formatMoveDateTo = fu.getDateTimeParseForDate(moveDateTo + CommonConstants.SYS_MAX_TIME);
        } else if (searchFlg == 0) {
            FileUtil fileUtil = new FileUtil();
            tblMaterialStockDetailVoList.setMoveDateTo(fileUtil.getDateFormatForStr(new Date()));

            FileUtil fu = new FileUtil();
            formatMoveDateTo = fu.getDateTimeParseForDate(tblMaterialStockDetailVoList.getMoveDateTo() + CommonConstants.SYS_MAX_TIME);

        }

        List list = getTblMaterialStockDetailSql(materialId, formatMoveDateFrom, formatMoveDateTo, isPage, sidx, sord, pageNumber, pageSize, true);
        if (list.size() > 0) {
            // ページをめぐる
            if (isPage) {
                Pager pager = new Pager();
                tblMaterialStockDetailVoList.setPageNumber(pageNumber);
                long counts = (long) list.get(0);
                tblMaterialStockDetailVoList.setCount(counts);
                tblMaterialStockDetailVoList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
            }

            list = getTblMaterialStockDetailSql(materialId, formatMoveDateFrom, formatMoveDateTo, isPage, sidx, sord, pageNumber, pageSize, false);
            TblMaterialStockDetailVo tblMaterialStockDetailVo;
            List<TblMaterialStockDetailVo> tblMaterialStockDetailVos = new ArrayList();
            List dictKeys = new ArrayList();
            dictKeys.add("store");//1
            dictKeys.add("delivery");//2
            dictKeys.add("store_discard");//3
            dictKeys.add("delivery_discard");//4
            Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeys);

            for (int i = 0; i < list.size(); i++) {
                tblMaterialStockDetailVo = new TblMaterialStockDetailVo();
                TblMaterialStockDetail tblMaterialStockDetail = (TblMaterialStockDetail) list.get(i);

                tblMaterialStockDetailVo.setUuid(tblMaterialStockDetail.getUuid());
                tblMaterialStockDetailVo.setMoveDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblMaterialStockDetail.getMoveDate()));

                switch (tblMaterialStockDetail.getStockType()) {
                    case CommonConstants.STORE:
                        tblMaterialStockDetailVo.setStockType(dictMap.get("store"));
                        break;
                    case CommonConstants.DELIVERY:
                        tblMaterialStockDetailVo.setStockType(dictMap.get("delivery"));
                        break;
                    case CommonConstants.STORE_DISCARD:
                        tblMaterialStockDetailVo.setStockType(dictMap.get("store_discard"));
                        break;
                    case CommonConstants.DELIVERY_DISCARD:
                        tblMaterialStockDetailVo.setStockType(dictMap.get("delivery_discard"));
                        break;
                    default:
                        tblMaterialStockDetailVo.setStockType("");
                        break;
                }

                tblMaterialStockDetailVo.setMoveQuantity(tblMaterialStockDetail.getMoveQuantity());// 入出庫数
                tblMaterialStockDetailVo.setStockQuantity(tblMaterialStockDetail.getStockQuantity());// 在庫数
                if (StringUtils.isNotEmpty(tblMaterialStockDetail.getMaterialLotNo())) {
                    tblMaterialStockDetailVo.setMaterialLotNo(tblMaterialStockDetail.getMaterialLotNo());// ロット番号
                } else {
                    tblMaterialStockDetailVo.setMaterialLotNo("");// ロット番号
                }
                tblMaterialStockDetailVos.add(tblMaterialStockDetailVo);
            }
            tblMaterialStockDetailVoList.setTblMaterialStockDetailVoList(tblMaterialStockDetailVos);
        }

        return tblMaterialStockDetailVoList;
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
    private List getTblMaterialStockDetailSql(
            String materialId,
            Date moveDateFrom,
            Date moveDateTo,
            boolean isPage,
            String sidx, // ソートキー
            String sord, // ソート順
            int pageNumber, // ページNo
            int pageSize, // ページSize
            boolean isCount
    ) {

        StringBuilder sql;
        if (isPage && isCount) {
            sql = new StringBuilder(" SELECT COUNT(1) FROM TblMaterialStockDetail tblMaterialStockDetail "
                    + " WHERE tblMaterialStockDetail.materialId = :materialId ");
        } else {
            sql = new StringBuilder(" SELECT tblMaterialStockDetail FROM TblMaterialStockDetail tblMaterialStockDetail "
                    + " WHERE tblMaterialStockDetail.materialId = :materialId ");
        }

        if (moveDateFrom != null) {
            sql.append(" AND  tblMaterialStockDetail.moveDate >= :formatMoveDateFrom ");
        }

        if (moveDateTo != null) {
            sql.append(" AND  tblMaterialStockDetail.moveDate <= :formatMoveDateTo ");
        }

        if (!isCount) {
            if (isPage && StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                // 表示順
                sql.append(sortStr);

            } else {
                // 表示順は入出庫日の降順。
                sql.append(" ORDER BY tblMaterialStockDetail.moveDate  DESC, tblMaterialStockDetail.createDate DESC ");
            }
        }

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("materialId", materialId);

        if (moveDateFrom != null) {
            query.setParameter("formatMoveDateFrom", moveDateFrom);
        }

        if (moveDateTo != null) {
            query.setParameter("formatMoveDateTo", moveDateTo);
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
     * @param materialId
     * @param moveDateFrom
     * @param moveDateTo
     * @param loginUser
     * @return
     */
    public FileReponse getTblMaterialStockDetailVoCsv(String materialId, String moveDateFrom, String moveDateTo, LoginUser loginUser) {

        /**
         * Header
         */
        ArrayList dictKeyList = new ArrayList();
        dictKeyList.add("move_date");
        dictKeyList.add("stock_type");
        dictKeyList.add("move_quantity");
        dictKeyList.add("stock_quantity");
        dictKeyList.add("lot_number");
        dictKeyList.add("material_stock_detail");

        Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(loginUser.getLangId(), dictKeyList);

        /*Head*/
        ArrayList headList = new ArrayList();
        headList.add(dictMap.get("move_date"));
        headList.add(dictMap.get("stock_type"));
        headList.add(dictMap.get("move_quantity"));
        headList.add(dictMap.get("stock_quantity"));
        headList.add(dictMap.get("lot_number"));

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(headList);

        TblMaterialStockDetailVoList tblMaterialStockDetailVoList = getTblMaterialStockDetailVoList(materialId, moveDateFrom, moveDateTo, loginUser.getLangId(), 1, false, "", "", 0, 0);

        //明細データを取得
        if (null != tblMaterialStockDetailVoList && tblMaterialStockDetailVoList.getTblMaterialStockDetailVoList() != null) {
            ArrayList tempOutList;
            for (int i = 0; i < tblMaterialStockDetailVoList.getTblMaterialStockDetailVoList().size(); i++) {
                TblMaterialStockDetailVo tblMaterialStockDetailVo = tblMaterialStockDetailVoList.getTblMaterialStockDetailVoList().get(i);
                tempOutList = new ArrayList<>();

                tempOutList.add(String.valueOf(tblMaterialStockDetailVo.getMoveDate()));
                tempOutList.add(String.valueOf(tblMaterialStockDetailVo.getStockType()));
                tempOutList.add(String.valueOf(tblMaterialStockDetailVo.getMoveQuantity()));
                tempOutList.add(String.valueOf(tblMaterialStockDetailVo.getStockQuantity()));
                tempOutList.add(String.valueOf(tblMaterialStockDetailVo.getMaterialLotNo()));
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
        tblCsvExport.setExportTable("tbl_material_stock_detail");
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MATERIAL_STOCK_QUANTITY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(dictMap.get("material_stock_detail")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

}
