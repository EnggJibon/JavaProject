/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.stock;

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
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.material.MstMaterialService;
import com.kmcj.karte.resources.material.lot.TblMaterialLot;
import com.kmcj.karte.resources.material.stock.detail.TblMaterialStockDetail;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
public class TblMaterialStockService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    private final static Map<String, String> orderKey;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstMaterialService mstMaterialService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    static {
        orderKey = new HashMap<>();
        orderKey.put("materialCode", " ORDER BY mstMaterial.materialCode ");// 材料コード
        orderKey.put("materialName", " ORDER BY mstMaterial.materialName ");// 材料名称
        orderKey.put("stockQty", " ORDER BY tblMaterialStock.stockQty ");// 在庫数
        orderKey.put("stockUnitText", " ORDER BY tblMaterialStock.stockUnit ");// 在庫単位
        orderKey.put("updateDateTimeStr", " ORDER BY tblMaterialStock.updateDate ");//更新日時
    }

    /**
     * 材料在庫計上処理
     *
     * @param materialCode
     * @param lotNumber
     * @param storeType
     * @param quantity
     * @param stockChangeDate
     * @param productionDetailId
     * @param status
     * @param remarks01
     * @param userUuid
     * @param langId
     * @param lotDelFlg
     * @return
     */
    @Transactional
    public BasicResponse doMaterialStock(String materialCode, String lotNumber, int storeType, BigDecimal quantity, String stockChangeDate, String productionDetailId, int status, String remarks01, String userUuid, String langId, boolean lotDelFlg) {
        BasicResponse basicResponse = new BasicResponse();
        // 在庫変更日
        // 空欄の場合はシステム日付を設定
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        Date formatStockChangeDate;
        try {
            if (StringUtils.isNotEmpty(stockChangeDate)) {
                if (stockChangeDate.length() < 11) {
                    stockChangeDate += CommonConstants.SYS_MIN_TIME;
                }
                formatStockChangeDate = sdf.parse(stockChangeDate);
            } else {
                formatStockChangeDate = new Date();
            }
        } catch (ParseException e) {

            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_date_format_invalid"));
            return basicResponse;
        }

        // 材料情報取得
        MstMaterial mstMaterial = mstMaterialService.getMstMaterialByCode(materialCode);
        if (mstMaterial == null) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
            return basicResponse;
        }

        // 取得した材料在庫の在庫数を変更して材料在庫を更新する。
        // 入庫、入庫赤伝票：在庫数に数量を加算して更新
        // 出庫、出庫赤伝票：在庫数に数量を減算して更新
        TblMaterialStock tblMaterialStock = updateTblMaterialStock(mstMaterial.getId(), quantity, storeType, userUuid);

        // 材料ロットテーブルの在庫数を更新する。
        // 入庫、入庫赤伝票：在庫数に数量を加算して更新
        // 出庫、出庫赤伝票：在庫数に数量を減算して更新
        TblMaterialLot tblMaterialLot = null;
        if (!lotDelFlg) {
            tblMaterialLot = updateTblMaterialLot(mstMaterial.getId(), lotNumber, storeType, quantity, tblMaterialStock, formatStockChangeDate, productionDetailId, status, remarks01, userUuid);
        }

        // 材料在庫履歴テーブルを登録する。
        updateTblMaterialStockDetail(mstMaterial.getId(), lotNumber, storeType, quantity, tblMaterialStock, formatStockChangeDate, productionDetailId, tblMaterialLot, userUuid);

        return basicResponse;
    }

    private TblMaterialStock updateTblMaterialStock(String materialId, BigDecimal quantity, int storeType, String userUuid) {
        TblMaterialStock tblMaterialStock = getSingleResultTblMaterialStockByMaterialId(materialId);
        if (tblMaterialStock != null) {
            if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                tblMaterialStock.setStockQty(tblMaterialStock.getStockQty().add(quantity));
            } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                tblMaterialStock.setStockQty(tblMaterialStock.getStockQty().subtract(quantity));
            }
            tblMaterialStock.setUpdateUserUuid(userUuid);
            tblMaterialStock.setUpdateDate(new Date());
            entityManager.merge(tblMaterialStock);

            return tblMaterialStock;
        } else {
            tblMaterialStock = new TblMaterialStock();
            tblMaterialStock.setUuid(IDGenerator.generate());
            tblMaterialStock.setMaterialId(materialId);
            if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                tblMaterialStock.setStockQty(quantity);
            } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                tblMaterialStock.setStockQty(quantity.multiply(new BigDecimal(-1)));
            }
            tblMaterialStock.setCreateDate(new Date());
            tblMaterialStock.setCreateUserUuid(userUuid);
            tblMaterialStock.setUpdateDate(tblMaterialStock.getCreateDate());
            tblMaterialStock.setUpdateUserUuid(userUuid);
            entityManager.persist(tblMaterialStock);

            return tblMaterialStock;
        }
    }

    private TblMaterialLot updateTblMaterialLot(String materialId, String lotNumber, int storeType, BigDecimal quantity, TblMaterialStock tblMaterialStock, Date formatStockChangeDate, String productionDetailId, int status, String remarks01, String userUuid) {
        TblMaterialLot materialLot = null;
        // ロット番号を指定している場合は指定したロット番号の在庫数を引き算する
        if (StringUtils.isNotEmpty(lotNumber)) {
            try {
                Query query = entityManager.createNamedQuery("TblMaterialLot.findByMaterialIdAndLotNumber");
                query.setParameter("materialId", materialId);
                query.setParameter("lotNo", lotNumber);
                materialLot = (TblMaterialLot) query.getSingleResult();
                // 更新
                if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                    if (StringUtils.isEmpty(productionDetailId)) {
                        int rst = materialLot.getLotQuantity().add(quantity).compareTo(new BigDecimal(BigInteger.ZERO));
                        materialLot.setLotQuantity(rst > 0 ? materialLot.getLotQuantity().add(quantity) : new BigDecimal(BigInteger.ZERO));
                    }
                    int rst = materialLot.getStockQuantity().add(quantity).compareTo(new BigDecimal(BigInteger.ZERO));
                    materialLot.setStockQuantity(rst > 0 ? materialLot.getStockQuantity().add(quantity) : new BigDecimal(BigInteger.ZERO));
                } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                    // 出庫、出庫赤伝票では在庫数の引き算のみで数量の引き算はしないように変更する。(2018/01/23)
//                    if (StringUtils.isEmpty(productionDetailId)) {
//                        int rst = materialLot.getLotQuantity().subtract(quantity).compareTo(new BigDecimal(BigInteger.ZERO));
//                        materialLot.setLotQuantity(rst > 0 ? materialLot.getLotQuantity().subtract(quantity) : new BigDecimal(BigInteger.ZERO));
//                    }
                    int rst = materialLot.getStockQuantity().subtract(quantity).compareTo(new BigDecimal(BigInteger.ZERO));
                    materialLot.setStockQuantity(rst > 0 ? materialLot.getStockQuantity().subtract(quantity) : new BigDecimal(BigInteger.ZERO));
                }
                materialLot.setUpdateUserUuid(userUuid);
                materialLot.setUpdateDate(new Date());
                if (StringUtils.isEmpty(productionDetailId)) {
                    materialLot.setStatus(status);
                    materialLot.setRemarks01(remarks01);
                }
                entityManager.merge(materialLot);
            } catch (NoResultException e) {
                if (StringUtils.isEmpty(productionDetailId)) {
                    // 追加
                    materialLot = new TblMaterialLot();
                    materialLot.setUuid(IDGenerator.generate());
                    materialLot.setMaterialId(materialId);
                    materialLot.setLotNo(lotNumber);
                    materialLot.setLotQuantity(quantity);
                    materialLot.setStockQuantity(quantity);
                    materialLot.setLotIssueDate(formatStockChangeDate);
                    materialLot.setMaterialStockId(tblMaterialStock.getUuid());
                    materialLot.setStatus(status);
                    materialLot.setRemarks01(remarks01);
                    materialLot.setCreateDate(new Date());
                    materialLot.setCreateUserUuid(userUuid);
                    materialLot.setUpdateDate(materialLot.getCreateDate());
                    materialLot.setUpdateUserUuid(userUuid);

                    entityManager.persist(materialLot);
                } else {
                    // 何もしない、更新のみ
                }
            }
            // ロット番号を指定していない場合は在庫数が0より大きいロット番号の古い順に引き算する
        } else {
            Query query = entityManager.createNamedQuery("TblMaterialLot.findByMaterialId");
            query.setParameter("materialId", materialId);
            List<TblMaterialLot> tblMaterialLotList = query.getResultList();
            if (tblMaterialLotList != null && !tblMaterialLotList.isEmpty()) {
                for (TblMaterialLot tblMaterialLot : tblMaterialLotList) {
                    // 複数のロット番号で引き算した場合は最初に引き算したロット番号材料ロットレコードを返却する
                    if (materialLot == null) {
                        materialLot = tblMaterialLot;
                    }
                    if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                        tblMaterialLot.setUpdateUserUuid(userUuid);
                        tblMaterialLot.setUpdateDate(new Date());
                        int rst = tblMaterialLot.getStockQuantity().add(quantity).compareTo(new BigDecimal(BigInteger.ZERO));
                        if (rst >= 0) {
                            if (tblMaterialLot.getLotQuantity().compareTo(tblMaterialLot.getStockQuantity().add(quantity)) >= 0) {
                                tblMaterialLot.setStockQuantity(tblMaterialLot.getStockQuantity().add(quantity));
                                entityManager.merge(tblMaterialLot);
                                break;
                            } else {
                                quantity = tblMaterialLot.getStockQuantity().add(quantity).subtract(tblMaterialLot.getLotQuantity()).abs();
                                tblMaterialLot.setStockQuantity(tblMaterialLot.getLotQuantity());
                                entityManager.merge(tblMaterialLot);
                            }
                        } else {
                            quantity = tblMaterialLot.getStockQuantity().add(quantity);
                            tblMaterialLot.setStockQuantity(new BigDecimal(BigInteger.ZERO));
                            entityManager.merge(tblMaterialLot);
                        }
                    } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                        if (tblMaterialLot.getStockQuantity().compareTo(new BigDecimal(BigInteger.ZERO)) != 0) {
                            tblMaterialLot.setUpdateUserUuid(userUuid);
                            tblMaterialLot.setUpdateDate(new Date());
                            int rst = tblMaterialLot.getStockQuantity().subtract(quantity).compareTo(new BigDecimal(BigInteger.ZERO));
                            if (rst >= 0) {
                                if (tblMaterialLot.getLotQuantity().compareTo(tblMaterialLot.getStockQuantity().subtract(quantity)) >= 0) {
                                    tblMaterialLot.setStockQuantity(tblMaterialLot.getStockQuantity().subtract(quantity));
                                    entityManager.merge(tblMaterialLot);
                                    break;
                                } else {
                                    quantity = tblMaterialLot.getLotQuantity().subtract(tblMaterialLot.getStockQuantity().subtract(quantity));
                                    tblMaterialLot.setStockQuantity(tblMaterialLot.getLotQuantity());
                                    entityManager.merge(tblMaterialLot);
                                }
                            } else {
                                quantity = tblMaterialLot.getStockQuantity().subtract(quantity).abs();
                                tblMaterialLot.setStockQuantity(new BigDecimal(BigInteger.ZERO));
                                entityManager.merge(tblMaterialLot);
                            }
                        }
                    }
                }
            }
        }

        return materialLot;
    }

//材料在庫履歴
    private void updateTblMaterialStockDetail(String materialId, String lotNumber, int storeType, BigDecimal quantity, TblMaterialStock tblMaterialStock, Date formatStockChangeDate, String productionDetailId, TblMaterialLot tblMaterialLot, String userUuid) {
        TblMaterialStockDetail tblMaterialStockDetail = new TblMaterialStockDetail();
        tblMaterialStockDetail.setUuid(IDGenerator.generate());
        tblMaterialStockDetail.setMaterialId(materialId);
        tblMaterialStockDetail.setMaterialLotNo(lotNumber);
        if (tblMaterialLot != null) {
            tblMaterialStockDetail.setMaterialLotId(tblMaterialLot.getUuid());
        }
        tblMaterialStockDetail.setProductionDetailId(productionDetailId);
        tblMaterialStockDetail.setStockType(storeType);
        tblMaterialStockDetail.setMoveDate(formatStockChangeDate);
        if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD || storeType == CommonConstants.NONE) {
            tblMaterialStockDetail.setMoveQuantity(quantity);
        } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
            tblMaterialStockDetail.setMoveQuantity(quantity.multiply(new BigDecimal(-1)));
        }
        tblMaterialStockDetail.setStockQuantity(tblMaterialStock.getStockQty());
        tblMaterialStockDetail.setMaterialStockId(tblMaterialStock.getUuid());
        tblMaterialStockDetail.setCreateDate(new Date());
        tblMaterialStockDetail.setCreateUserUuid(userUuid);
        tblMaterialStockDetail.setUpdateDate(tblMaterialStockDetail.getCreateDate());
        tblMaterialStockDetail.setUpdateUserUuid(userUuid);

        entityManager.persist(tblMaterialStockDetail);
    }

    /**
     * 檢索
     *
     * @param materialCode
     * @param materialName
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @param orderBy
     * @return TblMaterialStockVoList
     */
    public TblMaterialStockVoList getTblMaterialStockVoList(String langId,
            String materialCode,
            String materialName,
            String sidx,
            String sord,
            int pageNumber,
            int pageSize,
            boolean isPage,
            int orderBy
    ) {
        TblMaterialStockVoList response = new TblMaterialStockVoList();
        if (isPage) {
            List count = getTblMaterialStockVoListSQL(materialCode, materialName, sidx, sord, pageNumber, pageSize, true, orderBy);
            // ページをめぐる
            Pager pager = new Pager();
            response.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            response.setCount(counts);
            response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        }
        List<TblMaterialStockVo> tblMaterialStockVos = new ArrayList();
        TblMaterialStockVo tblMaterialStockVo;
        List list = getTblMaterialStockVoListSQL(materialCode, materialName, sidx, sord, pageNumber, pageSize, false, orderBy);

        if (list != null && list.size() > 0) {
            FileUtil fu = new FileUtil();
            Map<Integer, String> inMap2 = new HashMap<>();
            MstChoiceList mstChoiceList = mstChoiceService.getChoice(langId, "mst_material.stock_unit");
            for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                inMap2.put(Integer.parseInt(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
            for (Object obj : list) {

                Object[] objTblMaterialStock = (Object[]) obj;

                tblMaterialStockVo = new TblMaterialStockVo();

                int coLIndex = 0;
                if (objTblMaterialStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblMaterialStock[coLIndex]))) {
                    tblMaterialStockVo.setUuid(String.valueOf(objTblMaterialStock[coLIndex]));
                } else {
                    tblMaterialStockVo.setUuid("");
                }
                coLIndex++;
                if (objTblMaterialStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblMaterialStock[coLIndex]))) {
                    tblMaterialStockVo.setMaterialId(String.valueOf(objTblMaterialStock[coLIndex]));
                } else {
                    tblMaterialStockVo.setMaterialId("");//
                }
                coLIndex++;
                if (objTblMaterialStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblMaterialStock[coLIndex]))) {
                    tblMaterialStockVo.setMaterialCode(String.valueOf(objTblMaterialStock[coLIndex]));
                } else {
                    tblMaterialStockVo.setMaterialCode("");//
                }
                coLIndex++;
                if (objTblMaterialStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblMaterialStock[coLIndex]))) {
                    tblMaterialStockVo.setMaterialName(String.valueOf(objTblMaterialStock[coLIndex]));
                } else {
                    tblMaterialStockVo.setMaterialName("");//
                }
                coLIndex++;
                if (objTblMaterialStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblMaterialStock[coLIndex]))) {
                    tblMaterialStockVo.setStockQty((new BigDecimal(String.valueOf(objTblMaterialStock[coLIndex]))));
                } else {
                    tblMaterialStockVo.setStockQty(new BigDecimal(0));
                }
                coLIndex++;
                if (objTblMaterialStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblMaterialStock[coLIndex]))) {
                    int stockUnit = Integer.parseInt(String.valueOf(objTblMaterialStock[coLIndex]));
                    tblMaterialStockVo.setStockUnitText("");
                    if (stockUnit != 0) {
                        tblMaterialStockVo.setStockUnit(stockUnit);
                        tblMaterialStockVo.setStockUnitText(inMap2.get(stockUnit));
                    }
                } else {
                    tblMaterialStockVo.setStockUnit(0);//
                    tblMaterialStockVo.setStockUnitText("");
                }
                coLIndex++;
                if (objTblMaterialStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblMaterialStock[coLIndex]))) {
                    tblMaterialStockVo.setUpdateDateTime(fu.getDateTimeFormatForStr((objTblMaterialStock[coLIndex])));
                } else {
                    tblMaterialStockVo.setUpdateDateTime("");//
                }

                tblMaterialStockVos.add(tblMaterialStockVo);
            }
            response.setTblMaterialStockVos(tblMaterialStockVos);
        }
        return response;
    }

    /**
     * マスタ複数取得
     *
     * @param materialCode
     * @param materialName
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @param isCount
     * @param orderBy
     * @return
     */
    private List getTblMaterialStockVoListSQL(
            String materialCode,
            String materialName,
            String sidx,
            String sord,
            int pageNumber,
            int pageSize,
            boolean isCount,
            int orderBy
    ) {
        StringBuilder sql = new StringBuilder();
        if (isCount) {
            sql.append("SELECT count(1) ");

        } else {
            sql = new StringBuilder(
                    " SELECT "
                    + " tblMaterialStock.uuid " //uuid
                    + " ,mstMaterial.id " //materialId
                    + " ,mstMaterial.materialCode " //materialCode
                    + " ,mstMaterial.materialName " //materialName
                    + " ,tblMaterialStock.stockQty " //stockQty
                    + " ,tblMaterialStock.stockUnit  " //stockUnit
                    + " ,tblMaterialStock.updateDate ");//upadteDate
        }
        sql.append(" FROM MstMaterial mstMaterial  "
                + " LEFT JOIN TblMaterialStock tblMaterialStock ON  mstMaterial.id = tblMaterialStock.materialId "
                + " WHERE 1=1 "
        );

        if (StringUtils.isNotEmpty(materialCode)) {
            sql = sql.append(" AND mstMaterial.materialCode LIKE :materialCode ");
        }
        if (StringUtils.isNotEmpty(materialName)) {
            sql = sql.append(" AND mstMaterial.materialName LIKE :materialName ");
        }

        if (orderBy == 1) {
            // 表示順は材料コードの昇順の昇順
            sql = sql.append(" ORDER BY mstMaterial.materialCode");
        } else if (!isCount) {
            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);
            } else {
                // 表示順は材料コードの昇順の昇順
                sql = sql.append(" ORDER BY mstMaterial.materialCode");
            }
        }
        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(materialCode)) {
            query.setParameter("materialCode", "%" + materialCode + "%");
        }
        if (StringUtils.isNotEmpty(materialName)) {
            query.setParameter("materialName", "%" + materialName + "%");
        }

        // 画面改ページを設定する
        if (!isCount && orderBy == 0) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();
        return list;
    }

    /**
     * 在庫テーブルより条件にあてはまるデータを取得し、CSVファイルに出力する。　部品コード昇順
     *
     * @param materialCode
     * @param materialName
     * @param loginUser
     * @return
     */
    public FileReponse getTblMaterialStockVoListCsv(String materialCode, String materialName, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();

        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("material_code", "material_name", "stock_quantity",
                "stock_unit_choice", "update_date", "material_stock_quantity_registration");
        Map<String, String> csvHeader = mstDictionaryService.getDictionaryHashMap(loginUser.getLangId(), dictKeyList);
        ArrayList headList = new ArrayList();
        headList.add(csvHeader.get("material_code"));
        headList.add(csvHeader.get("material_name"));
        headList.add(csvHeader.get("stock_quantity"));
        headList.add(csvHeader.get("stock_unit_choice"));
        headList.add(csvHeader.get("update_date"));
        //出力へーだー準備
        gLineList.add(headList);
        //明細データを取得
        TblMaterialStockVoList tblMaterialStockVoList = getTblMaterialStockVoList(loginUser.getLangId(), materialCode, materialName, "", "", 0, 0, false, 1);
        if (tblMaterialStockVoList.getTblMaterialStockVos() != null && !tblMaterialStockVoList.getTblMaterialStockVos().isEmpty()) {
            /*Detail*/
            ArrayList lineList;
            for (int i = 0; i < tblMaterialStockVoList.getTblMaterialStockVos().size(); i++) {
                lineList = new ArrayList();

                TblMaterialStockVo tblMaterialStockVo = tblMaterialStockVoList.getTblMaterialStockVos().get(i);
                lineList.add(tblMaterialStockVo.getMaterialCode());
                lineList.add(tblMaterialStockVo.getMaterialName());
                lineList.add(String.valueOf(tblMaterialStockVo.getStockQty()));
                lineList.add(tblMaterialStockVo.getStockUnitText());
                lineList.add(tblMaterialStockVo.getUpdateDateTime());
                gLineList.add(lineList);
            }
        }
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable("tbl_material_stock");
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MATERIAL_STOCK_QUANTITY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(csvHeader.get("material_stock_quantity_registration")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        FileReponse fileReponse = new FileReponse();
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

    /**
     *
     * @param componentId
     * @param procedureCode
     * @return
     */
    private TblMaterialStock getSingleResultTblMaterialStockByMaterialId(String materialId) {

        StringBuilder sql;
        sql = new StringBuilder("SELECT tblMaterialStock FROM  TblMaterialStock tblMaterialStock "
                + " WHERE tblMaterialStock.materialId =:materialId "
        );
        // KM-867材料在庫APIバグ
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("materialId", materialId);
        List list = query.getResultList();
        if (list != null && list.size() > 0) {
            return (TblMaterialStock) list.get(0);
        } else {
            return null;
        }
    }

    /**
     * CSVファイルを取込み
     *
     * @param fileUuid
     * @param loginUser
     * @return
     */
    @Transactional
    public ImportResultResponse postTblMaterialStockVoListCsv(String fileUuid, LoginUser loginUser) {
        ImportResultResponse importResultResponse = new ImportResultResponse();
        //①CSVファイルを取込み
        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long failedCount = 0;
        long deletedCount = 0;

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
            dictKeyList.add("material_stock_quantity_registration");
            dictKeyList.add("row_number");
            dictKeyList.add("material_code");
            dictKeyList.add("material_name");
            dictKeyList.add("stock_quantity");
            dictKeyList.add("stock_unit_choice");
            dictKeyList.add("error_detail");
            dictKeyList.add("error");
            dictKeyList.add("msg_error_wrong_csv_layout");
            dictKeyList.add("msg_record_updated");
            dictKeyList.add("db_process");
            dictKeyList.add("mst_error_record_not_found");
            dictKeyList.add("msg_record_added");
            dictKeyList.add("msg_error_value_invalid");
            dictKeyList.add("msg_error_not_null");

            Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(userLangId, dictKeyList);

            Map<String, Integer> colIndexMap = new HashMap<>();

            for (int j = 0; j < readList.get(0).size(); j++) {
                if (readList.get(0).get(j).trim().equals(dictMap.get("material_code"))) {
                    colIndexMap.put("materialCode", j);
                } else if (readList.get(0).get(j).trim().equals(dictMap.get("stock_quantity"))) {
                    colIndexMap.put("stockQuantity", j);
                } else if (readList.get(0).get(j).trim().equals(dictMap.get("stock_unit_choice"))) {
                    colIndexMap.put("stockUnit", j);
                }
            }
            FileUtil fileUtil = new FileUtil();
            Map<String, Integer> inMap2 = new HashMap<>();
            MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_material.stock_unit");
            for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                inMap2.put(mstChoice.getChoice(), Integer.parseInt(mstChoice.getMstChoicePK().getSeq()));
            }
            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);

                String materialCode = null, stockQuantity = null, stockUnit = null;

                if (colIndexMap.get("materialCode") != null) {
                    materialCode = String.valueOf(comList.get(colIndexMap.get("materialCode"))).trim();
                }
                if (colIndexMap.get("stockQuantity") != null) {
                    stockQuantity = String.valueOf(comList.get(colIndexMap.get("stockQuantity"))).trim();
                }
                if (colIndexMap.get("stockUnit") != null) {
                    stockUnit = String.valueOf(comList.get(colIndexMap.get("stockUnit"))).trim();
                }
                BigDecimal stockQty;

                // 在庫数
                if (colIndexMap.get("stockQuantity") != null) {
                    try {
                        stockQty = new BigDecimal(stockQuantity);
                    } catch (NumberFormatException e) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("stock_quantity"), stockQuantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                        continue;
                    }
                } else {
                    stockQty = new BigDecimal(0.000);

                }
                int intStockUnit = 0;
                // 在庫単位
                if (colIndexMap.get("stockUnit") == null) {
                } else if (StringUtils.isEmpty(stockUnit)) {
                } else {
                    Integer numStockUnitChoice = inMap2.get(stockUnit);
                    if (numStockUnitChoice == null) {
                        //エラー情報をログファイルに記入
                        failedCount = failedCount + 1;
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("stock_unit_choice"), stockUnit, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                        continue;
                    } else {
                        intStockUnit = numStockUnitChoice;
                    }
                }

                MstMaterial mstMaterial;
                if (StringUtils.isEmpty(materialCode)) {
                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_code"), materialCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                    failedCount = failedCount + 1;
                    continue;
                } else {

                    mstMaterial = mstMaterialService.getMstMaterialByCode(materialCode);
                    if (mstMaterial == null) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_code"), materialCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                }

                TblMaterialStock tblMaterialStock = getSingleResultTblMaterialStockByMaterialId(mstMaterial.getId());
                if (tblMaterialStock != null) {
                    if (tblMaterialStock.getStockQty().compareTo(stockQty) != 0 || tblMaterialStock.getStockUnit() != intStockUnit) {
                        // 更新
                        BigDecimal stockQuantityBfUpd = tblMaterialStock.getStockQty();
                        tblMaterialStock.setUpdateDate(new Date());
                        tblMaterialStock.setUpdateUserUuid(loginUser.getUserUuid());
                        tblMaterialStock.setStockQty(stockQty);
                        tblMaterialStock.setStockUnit(intStockUnit);
                        entityManager.merge(tblMaterialStock);

                        updateTblMaterialStockDetail(tblMaterialStock.getMaterialId(), null, 0, tblMaterialStock.getStockQty().subtract(stockQuantityBfUpd), tblMaterialStock, new Date(), null, null, loginUser.getUserUuid());
                    }
                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_code"), materialCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_updated")));
                    updatedCount = updatedCount + 1;

                } else {
                    tblMaterialStock = new TblMaterialStock();
                    tblMaterialStock.setUuid(IDGenerator.generate());
                    tblMaterialStock.setMaterialId(mstMaterial.getId());
                    tblMaterialStock.setStockQty(stockQty);
                    tblMaterialStock.setStockUnit(intStockUnit);
                    tblMaterialStock.setCreateDate(new Date());
                    tblMaterialStock.setCreateUserUuid(loginUser.getUserUuid());
                    tblMaterialStock.setUpdateDate(new Date());
                    tblMaterialStock.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(tblMaterialStock);

                    updateTblMaterialStockDetail(mstMaterial.getId(), null, 0, tblMaterialStock.getStockQty(), tblMaterialStock, new Date(), null, null, loginUser.getUserUuid());
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_code"), materialCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_added")));
                    addedCount = addedCount + 1;
                }

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
            tblCsvImport.setImportTable("tbl_material_stock");

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
            tblCsvImport.setLogFileName(FileUtil.getLogFileName(dictMap.get("material_stock_quantity_registration")));

            tblCsvImportService.createCsvImpor(tblCsvImport);
        }
        return importResultResponse;
    }

    /**
     *
     * 登録
     *
     * @param tblMaterialStockVos
     * @param userUuid
     * @return
     */
    @Transactional
    public BasicResponse updateTblMaterialStock(TblMaterialStockVoList tblMaterialStockVos, String userUuid) { //update tblMaterialStock
        if (tblMaterialStockVos != null && tblMaterialStockVos.getTblMaterialStockVos() != null && !tblMaterialStockVos.getTblMaterialStockVos().isEmpty()) {
            for (TblMaterialStockVo tblMaterialStockVo : tblMaterialStockVos.getTblMaterialStockVos()) {
                if (StringUtils.isNotEmpty(tblMaterialStockVo.getMaterialId())) {
                    // KM-867材料在庫APIバグ
                    //TblMaterialStock tblMaterialStock = entityManager.find(TblMaterialStock.class, tblMaterialStockVo.getUuid());
                    TblMaterialStock tblMaterialStock = getSingleResultTblMaterialStockByMaterialId(tblMaterialStockVo.getMaterialId());

                    if (tblMaterialStock != null) {
                        if (tblMaterialStock.getStockQty().compareTo(tblMaterialStockVo.getStockQty()) != 0 || tblMaterialStock.getStockUnit() != tblMaterialStockVo.getStockUnit()) {
                            BigDecimal stockQuantityBfUpd = tblMaterialStock.getStockQty();
                            tblMaterialStock.setStockUnit(tblMaterialStockVo.getStockUnit());
                            tblMaterialStock.setStockQty(tblMaterialStockVo.getStockQty());
                            tblMaterialStock.setUpdateDate(new Date());
                            tblMaterialStock.setUpdateUserUuid(userUuid);

                            entityManager.merge(tblMaterialStock);

                            updateTblMaterialStockDetail(tblMaterialStock.getMaterialId(), null, 0, tblMaterialStock.getStockQty().subtract(stockQuantityBfUpd), tblMaterialStock, new Date(), null, null, userUuid);
                        }
                    } else {
                        tblMaterialStock = new TblMaterialStock();
                        tblMaterialStock.setUuid(IDGenerator.generate());
                        tblMaterialStock.setMaterialId(tblMaterialStockVo.getMaterialId());
                        tblMaterialStock.setStockQty(tblMaterialStockVo.getStockQty());
                        tblMaterialStock.setStockUnit(tblMaterialStockVo.getStockUnit());
                        tblMaterialStock.setCreateDate(new Date());
                        tblMaterialStock.setCreateUserUuid(userUuid);
                        tblMaterialStock.setUpdateDate(new Date());
                        tblMaterialStock.setUpdateUserUuid(userUuid);

                        entityManager.persist(tblMaterialStock);

                        updateTblMaterialStockDetail(tblMaterialStockVo.getMaterialId(), null, 0, tblMaterialStock.getStockQty(), tblMaterialStock, new Date(), null, null, userUuid);
                    }
                }
            }
        }
        BasicResponse basicResponse = new BasicResponse();
        return basicResponse;
    }

}
