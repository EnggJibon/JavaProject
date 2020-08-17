/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.lot.TblComponentLot;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVo;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.component.structure.MstComponentStructure;
import com.kmcj.karte.resources.currency.MstCurrency;
import com.kmcj.karte.resources.currency.MstCurrencyList;
import com.kmcj.karte.resources.currency.MstCurrencyService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.po.shipment.TblShipment;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.production.detail.TblProductionDetailList;
import com.kmcj.karte.resources.production.detail.TblProductionDetailService;
import com.kmcj.karte.resources.production.stock.TblProductionStock;
import com.kmcj.karte.resources.stock.detail.TblStockDetail;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 *
 * @author admin
 */
@Dependent
public class TblStockService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstComponentService mstComponentService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstCurrencyService mstCurrencyService;

    @Inject
    private TblProductionDetailService tblProductionDetailService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvImportService tblCsvImportService;

    @Inject
    private MstProcedureService mstProcedureService;

    private TblProductionDetailList tblProductionDetailList = null;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("componentCode", " ORDER BY mstComponent.componentCode ");//部品コード
        orderKey.put("componentName", " ORDER BY mstComponent.componentName ");//部品名称
        orderKey.put("isPurchasedPart", " ORDER BY mstComponent.isPurchasedPart ");//在庫数
        orderKey.put("procedureCode", " ORDER BY mstProcedure.procedureCode ");//工程番号
        orderKey.put("stockQuantity", " ORDER BY tblStock.stockQuantity ");//在庫数
        orderKey.put("stockCost", " ORDER BY tblStock.stockQuantity ");//在庫金額
        orderKey.put("currencyUnit", " ORDER BY mstComponent.currencyCode ");//通貨単位
        orderKey.put("stockUnit", " ORDER BY mstComponent.stockUnit ");//在庫単位数
        orderKey.put("procedureName", " ORDER BY mstProcedure.procedureName ");//工程名称
        orderKey.put("moveDate", " ORDER BY tblStock.moveDate ");//入出庫日
        orderKey.put("updateDateTime", " ORDER BY tblStock.updateDate ");//更新日時
    }

    /**
     * <P>
     * 在庫数を変更する処理を作成する。
     * <P>
     * 引数の内容で在庫数の変更を行う
     * <P>
     * 在庫数を変更する際には自部品以外にも下位階層の部品の入庫、出庫も行う。
     * <P>
     * 取得した部品コード、工程番号に対して入庫・出庫処理を行う。
     * <P>
     * 部品構成上、指定した部品コードに対して子部品が存在する場合は入庫する際に構成部品の出庫を行う。
     * <P>
     * 但し、子部品のさらに構成部品にあたる孫部品に対しては構成部品の出庫は不要。
     *
     * @param componentCode 	部品コード(在庫数を変更する部品コード（キー情報）)
     * @param mstCurrentProcedure 	工程番号(在庫数を変更する工程番号（キー情報）)
     * @param mstPrevProcedure     前工程番号
     * @param storeType 	分類(1>入庫：STORE, 2>出庫：DELIVERY, 3>入庫赤伝票：STORE-DISCARD,
     * 4>出庫赤伝票：DELIVERY-DISCARD)
     * @param quantity	数量(在庫数を変更する数量)
     * @param stockChangeDate	在庫変更日(在庫を変更した日,空欄の場合はシステム日付を設定)
     * @param lotNumber	製造ロット番号(在庫数を変更する生産実績テーブルのロット番号)
     * @param status 状態
     * @param remarks01 コメント
     * @param shipmentFlg 出荷有無フラグ0：無し、1：有り
     * @param relationVos 部品ロット関連リスト
     * @param userUuid	ログインユーザUUID
     * @param langId ログインユーザ言語ID
     * @return
     */
    @Transactional
    public ImportResultResponse doTblStock(String componentCode, MstProcedure mstCurrentProcedure, MstProcedure mstPrevProcedure, int storeType, long quantity, String stockChangeDate, String lotNumber, int status, String remarks01, int shipmentFlg,
            TblComponentLotRelationVoList relationVos,
            String userUuid, String langId) {

        ImportResultResponse basicResponse = new ImportResultResponse();
        // 在庫変更日
        // 空欄の場合はシステム日付を設定
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        Date formatStockChangeDate = null;
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

        // 部品情報取得
        MstComponent mstComponent = mstComponentService.getMstComponent(componentCode);
        if (mstComponent == null) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_data_deleted"));
            return basicResponse;
        }

        // 部品コード、工程番号で在庫管理テーブルを検索し、取得した在庫管理テーブルの在庫数を変更して在庫管理テーブルを更新する。
        // 入庫、入庫赤伝票：在庫数に数量を加算して更新
        // 出庫、出庫赤伝票：在庫数に数量を減算して更新
        TblStock tblStock = updateTblStock(mstComponent.getId(), mstCurrentProcedure.getProcedureCode(), quantity, storeType, formatStockChangeDate, userUuid, basicResponse, true);
        // 前工程番号在庫数の減算
        TblStock tblStockPrev = null;
        int changedStoreType = changeStoreType(storeType);
        if (mstPrevProcedure != null) {
            tblStockPrev = updateTblStock(mstComponent.getId(), mstPrevProcedure.getProcedureCode(), quantity, changedStoreType, formatStockChangeDate, userUuid, basicResponse, false);
        }

        // ロット番号により生産実績明細リスト取得
        if (tblProductionDetailList == null) {
            tblProductionDetailList = tblProductionDetailService.getProductionByLotNumber(lotNumber);
        }
        if (tblProductionDetailList.getTblProductionDetails() != null && !tblProductionDetailList.getTblProductionDetails().isEmpty()) {
            // 生産実績在庫テーブルの在庫数を更新する。
            // 部品コード、工程番号、製造ロット番号で生産実績明細テーブルを検索してから生産実績在庫テーブルを取得する。
            // 入庫、入庫赤伝票：在庫数に数量を加算して更新
            // 出庫、出庫赤伝票：在庫数に数量を減算して更新
            boolean isFind = false;
            for (TblProductionDetail tblProductionDetail : tblProductionDetailList.getTblProductionDetails()) {
                if (mstComponent.getId().equals(tblProductionDetail.getComponentId())) {
                    updateTblProductionStock(mstComponent.getId(), mstCurrentProcedure.getProcedureCode(), tblProductionDetail.getId(), quantity, storeType, userUuid);
                    // 部品ロットテーブルの在庫数を更新する。
                    updateTblComponentLot(mstComponent.getId(), mstCurrentProcedure.getProcedureCode(), Arrays.asList(lotNumber), storeType, quantity, tblStock, formatStockChangeDate, tblProductionDetail.getId(), status, remarks01, userUuid, shipmentFlg, mstComponent);
                    // 前工番の部品ロットテーブルの在庫数を更新する。
                    if (mstPrevProcedure != null) {
                        updateTblComponentLot(mstComponent.getId(), mstPrevProcedure.getProcedureCode(), Arrays.asList(lotNumber), changedStoreType, quantity, tblStockPrev, formatStockChangeDate, tblProductionDetail.getId(), status, remarks01, userUuid, shipmentFlg, mstComponent);
                    }
                    isFind = true;
                    break;
                }
            }
            if (!isFind) {
                // 部品ロットテーブルの在庫数を更新する。
                updateTblComponentLot(mstComponent.getId(), mstCurrentProcedure.getProcedureCode(), Arrays.asList(lotNumber), storeType, quantity, tblStock, formatStockChangeDate, null, status, remarks01, userUuid, shipmentFlg, mstComponent);
                // 前工番の部品ロットテーブルの在庫数を更新する。
                if (mstPrevProcedure != null) {
                    updateTblComponentLot(mstComponent.getId(), mstPrevProcedure.getProcedureCode(), Arrays.asList(lotNumber), changedStoreType, quantity, tblStockPrev, formatStockChangeDate, null, status, remarks01, userUuid, shipmentFlg, mstComponent);
                }
            }
        } else {
            // 部品ロットテーブルの在庫数を更新する。
            updateTblComponentLot(mstComponent.getId(), mstCurrentProcedure.getProcedureCode(), Arrays.asList(lotNumber), storeType, quantity, tblStock, formatStockChangeDate, null, status, remarks01, userUuid, shipmentFlg, mstComponent);
            // 前工番の部品ロットテーブルの在庫数を更新する。
            if (mstPrevProcedure != null) {
                updateTblComponentLot(mstComponent.getId(), mstPrevProcedure.getProcedureCode(), Arrays.asList(lotNumber), changedStoreType, quantity, tblStockPrev, formatStockChangeDate, null, status, remarks01, userUuid, shipmentFlg, mstComponent);
            }
        }

//        Query query = null;
//        if (shipmentFlg == 1) {
//            // 部品IDと製造ロット番号から出荷IDを取得する
//            String sql = "SELECT m FROM TblShipment m WHERE m.componentId = :componentId AND m.productionLotNumber = :productionLotNumber ORDER BY m.shipDate DESC ";
//            query = entityManager.createQuery(sql);
//            query.setParameter("componentId", mstComponent.getId());
//            query.setParameter("productionLotNumber", lotNumber);
//            List<TblShipment> tblShipmentList = query.getResultList();
//            if (tblShipmentList != null && !tblShipmentList.isEmpty()) {
//                // 部品コード、工程番号、分類、数量、在庫変更日で在庫履歴テーブルを登録する。（INSERT）
//                insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, quantity, tblStock, tblComponentLot, tblShipmentList.get(0).getUuid(), userUuid);
//            } else {
//                insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, quantity, tblStock, tblComponentLot, null, userUuid);
//            }
//        } else {
//            insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, quantity, tblStock, tblComponentLot, null, userUuid);
//        }

        // 出荷登録、PO別生産実績照会でPOを削除するには下位階層の部品のん出入庫は実施しない
        if (shipmentFlg != 1 && mstCurrentProcedure.getFinalFlg() == 1) {
            // 部品コードから部品IDを取得して親部品IDとして部品構成テーブルを取得する。
            List<MstComponentStructure> mstComponentStructureList = getStructureListByParentComponentId(mstComponent.getId());

            // 取得した部品ID分在庫変更処理を繰り返す。
            if (mstComponentStructureList != null && !mstComponentStructureList.isEmpty()) {
                String parentComponentId = mstComponent.getId();
                for (MstComponentStructure mstComponentStructure : mstComponentStructureList) {
                    mstComponent = mstComponentService.getMstComponentById(mstComponentStructure.getComponentId());
                    MstProcedure mstProcedure = mstProcedureService.getFinalProcedureByComponentId(mstComponentStructure.getComponentId());
                    if (mstComponent == null || mstProcedure == null) {
                        continue;
                    }

                    if (tblProductionDetailList.getTblProductionDetails() != null && !tblProductionDetailList.getTblProductionDetails().isEmpty()) {
                        // 生産実績在庫テーブルの在庫数を更新する
                        // 在庫数：数量×構成個数を逆にする（加算、減算を逆にする）
                        boolean isFind = false;
                        for (TblProductionDetail tblProductionDetail : tblProductionDetailList.getTblProductionDetails()) {
                            if (tblProductionDetail.getComponentId().equals(parentComponentId)) {
                                updateTblProductionStock(mstComponentStructure.getComponentId(), mstProcedure.getProcedureCode(), tblProductionDetail.getId(), quantity * mstComponentStructure.getQuantity(), changedStoreType, userUuid);
                                if (relationVos != null && relationVos.getTblComponentLotRelationVos() != null && !relationVos.getTblComponentLotRelationVos().isEmpty()) {
                                    List<String> relationLotNoList = new ArrayList<>();
                                    for (TblComponentLotRelationVo relationVo : relationVos.getTblComponentLotRelationVos()) {
                                        if (relationVo.getSubComponentId().equals(mstComponentStructure.getComponentId())) {
                                            relationLotNoList.add(relationVo.getSubComponentLotNo());
                                        }
                                    }
                                    if (!relationLotNoList.isEmpty()) {
                                        // 在庫管理テーブルを取得する。
                                        // 在庫数：数量×構成個数を逆にする（加算、減算を逆にする）
                                        tblStock = updateTblStock(mstComponentStructure.getComponentId(), mstProcedure.getProcedureCode(), quantity * mstComponentStructure.getQuantity(), changedStoreType, formatStockChangeDate, userUuid, basicResponse, false);
                                        // 部品ロットテーブルの在庫数を更新する。
                                        updateTblComponentLot(mstComponentStructure.getComponentId(), mstProcedure.getProcedureCode(), relationLotNoList, changedStoreType, quantity * mstComponentStructure.getQuantity(), tblStock, formatStockChangeDate, tblProductionDetail.getId(), status, remarks01, userUuid, shipmentFlg, mstComponent);
                                        // 在庫履歴テーブルを登録する。（INSERT）
                                        // 工程番号：部品IDで最大の番号
                                        // 分類：引数の逆を指定（入庫であれば出庫を指定）
                                        // 入出庫数：数量×構成個数
                                        //insertTblStockDetail(mstComponent, mstProcedure.getProcedureCode(), storeType, formatStockChangeDate, quantity * mstComponentStructure.getQuantity(), tblStock, tblComponentLot, null, userUuid);
                                    }
                                }
                                isFind = true;
                                break;
                            }
                        }
                        if (!isFind) {
                            tblStock = updateTblStock(mstComponentStructure.getComponentId(), mstProcedure.getProcedureCode(), quantity * mstComponentStructure.getQuantity(), changedStoreType, formatStockChangeDate, userUuid, basicResponse, false);
                            // 部品ロットテーブルの在庫数を更新する。
                            updateTblComponentLot(mstComponentStructure.getComponentId(), mstProcedure.getProcedureCode(), Arrays.asList(lotNumber), changedStoreType, quantity * mstComponentStructure.getQuantity(), tblStock, formatStockChangeDate, null, status, remarks01, userUuid, shipmentFlg, mstComponent);
                            //insertTblStockDetail(mstComponent, mstProcedure.getProcedureCode(), storeType, formatStockChangeDate, quantity * mstComponentStructure.getQuantity(), tblStock, tblComponentLot, null, userUuid);
                        }
                    } else {
                        tblStock = updateTblStock(mstComponentStructure.getComponentId(), mstProcedure.getProcedureCode(), quantity * mstComponentStructure.getQuantity(), changedStoreType, formatStockChangeDate, userUuid, basicResponse, false);
                        // 部品ロットテーブルの在庫数を更新する。
                        updateTblComponentLot(mstComponentStructure.getComponentId(), mstProcedure.getProcedureCode(), Arrays.asList(lotNumber), changedStoreType, quantity * mstComponentStructure.getQuantity(), tblStock, formatStockChangeDate, null, status, remarks01, userUuid, shipmentFlg, mstComponent);
                        //insertTblStockDetail(mstComponent, mstProcedure.getProcedureCode(), storeType, formatStockChangeDate, quantity * mstComponentStructure.getQuantity(), tblStock, tblComponentLot, null, userUuid);
                    }

                    // 在庫履歴テーブルを登録する。（INSERT）
                    // 工程番号：部品IDで最大の番号
                    // 分類：引数の逆を指定（入庫であれば出庫を指定）
                    // 入出庫数：数量×構成個数
                    // insertTblStockDetail(mstComponent, mstProcedure.getProcedureCode(), storeType, formatStockChangeDate, quantity * mstComponentStructure.getQuantity(), tblStock, tblComponentLot, null, userUuid);
                }
            }
        }

        return basicResponse;
    }

    private TblStock updateTblStock(String componentId, String procedureCode, long quantity, int storeType, Date formatStockChangeDate, String userUuid, ImportResultResponse basicResponse, boolean countFlg) {
        TblStock tblStock = getSingleResultTblStockByComponentId(componentId, procedureCode);
        if (tblStock != null) {
            if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                tblStock.setStockQuantity(tblStock.getStockQuantity() + quantity);
            } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                tblStock.setStockQuantity(tblStock.getStockQuantity() - quantity);
            }
            tblStock.setProcedureCode(procedureCode);
            tblStock.setMoveDate(formatStockChangeDate);
            tblStock.setUpdateUserUuid(userUuid);
            tblStock.setUpdateDate(new Date());
            entityManager.merge(tblStock);

            if (countFlg) {
                basicResponse.setUpdatedCount(1);
            }

            return tblStock;
        } else {
            tblStock = new TblStock();
            tblStock.setUuid(IDGenerator.generate());
            tblStock.setComponentId(componentId);
            tblStock.setProcedureCode(procedureCode);
            if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                tblStock.setStockQuantity(quantity);
            } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                tblStock.setStockQuantity(-1 * quantity);
            }
            tblStock.setMoveDate(formatStockChangeDate);
            tblStock.setCreateDate(new Date());
            tblStock.setCreateUserUuid(userUuid);
            tblStock.setUpdateDate(tblStock.getCreateDate());
            tblStock.setUpdateUserUuid(userUuid);
            entityManager.persist(tblStock);

            if (countFlg) {
                basicResponse.setAddedCount(1);
            }

            return tblStock;
        }
    }

    private void updateTblComponentLot(String componentId, String procedureCode, List<String> lotNumberList, int storeType, long quantity, TblStock tblStock, Date formatStockChangeDate, String productionDetailId, int status, String remarks01, String userUuid, int shipmentFlg, MstComponent mstComponent) {
        TblComponentLot componentLot = null;
        if (lotNumberList != null && !lotNumberList.isEmpty()) {
            for (int i = 0; i < lotNumberList.size(); i++) {
                if (quantity == 0) {
                    break;
                }
                long stockDetailQty = 0;
                String lotNumber = lotNumberList.get(i);
                // ロット番号を指定している場合は指定したロット番号の在庫数を引き算する
                if (StringUtils.isNotEmpty(lotNumber)) {
                    try {
                        Query query = entityManager.createNamedQuery("TblComponentLot.findByComponentIdAndLotNumber");
                        query.setParameter("componentId", componentId);
                        query.setParameter("procedureCode", procedureCode);
                        query.setParameter("lotNo", lotNumber);
                        componentLot = (TblComponentLot) query.getSingleResult();
                        // 更新
                        if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                            if (storeType == CommonConstants.STORE) {
                                boolean rst = (componentLot.getLotQty() + quantity) > 0;
                                componentLot.setLotQty(rst ? (int) (componentLot.getLotQty() + quantity) : 0);
                            }
                            boolean rst = (componentLot.getStockQty() + quantity) > 0;
                            if (rst) {
                                if (componentLot.getLotQty() >= (componentLot.getStockQty() + quantity)) {
                                    stockDetailQty = quantity;
                                    componentLot.setStockQty((int) (componentLot.getStockQty() + quantity));
                                    entityManager.merge(componentLot);
                                    quantity = 0;
                                } else {
                                    stockDetailQty = componentLot.getLotQty() - componentLot.getStockQty();
                                    quantity = componentLot.getStockQty() + quantity - componentLot.getLotQty();
                                    componentLot.setStockQty(componentLot.getLotQty());
                                    entityManager.merge(componentLot);
                                }
                            } else {
                                stockDetailQty = -1 * componentLot.getStockQty();
                                quantity = componentLot.getStockQty() + quantity;
                                componentLot.setStockQty(0);
                                entityManager.merge(componentLot);
                            }
                        } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                            boolean rst = (componentLot.getStockQty() - quantity) > 0;
                            if (rst) {
                                if (componentLot.getLotQty() >= (componentLot.getStockQty() - quantity)) {
                                    stockDetailQty = quantity;
                                    componentLot.setStockQty((int) (componentLot.getStockQty() - quantity));
                                    entityManager.merge(componentLot);
                                    quantity = 0;
                                } else {
                                    stockDetailQty = componentLot.getLotQty() - componentLot.getStockQty();
                                    quantity = componentLot.getLotQty() - (componentLot.getStockQty() - quantity);
                                    componentLot.setStockQty(componentLot.getLotQty());
                                    entityManager.merge(componentLot);
                                }
                            } else {
                                stockDetailQty = componentLot.getStockQty();
                                quantity = Math.abs(componentLot.getStockQty() - quantity);
                                componentLot.setStockQty(0);
                                entityManager.merge(componentLot);
                            }
                        }
                        componentLot.setProcedureCode(procedureCode);
                        componentLot.setUpdateUserUuid(userUuid);
                        componentLot.setUpdateDate(new Date());
                        if (remarks01 != null) {
                            componentLot.setStatus(status);
                            componentLot.setRemarks01(remarks01);
                        }
                        entityManager.merge(componentLot);
                        
                        if (shipmentFlg == 1) {
                            // 部品IDと製造ロット番号から出荷IDを取得する
                            String sql = "SELECT m FROM TblShipment m WHERE m.componentId = :componentId AND m.productionLotNumber = :productionLotNumber ORDER BY m.shipDate DESC ";
                            query = entityManager.createQuery(sql);
                            query.setParameter("componentId", mstComponent.getId());
                            query.setParameter("productionLotNumber", lotNumber);
                            List<TblShipment> tblShipmentList = query.getResultList();
                            if (tblShipmentList != null && !tblShipmentList.isEmpty()) {
                                // 部品コード、工程番号、分類、数量、在庫変更日で在庫履歴テーブルを登録する。（INSERT）
                                insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, stockDetailQty, tblStock, componentLot, tblShipmentList.get(0).getUuid(), userUuid);
                            } else {
                                insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, stockDetailQty, tblStock, componentLot, null, userUuid);
                            }
                        } else {
                            insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, stockDetailQty, tblStock, componentLot, null, userUuid);
                        }
                        
                        if (quantity != 0 && i == lotNumberList.size() - 1) {
                            updateTblComponentLot(componentId, procedureCode, Arrays.asList(""), storeType, quantity, tblStock, formatStockChangeDate, productionDetailId, status, remarks01, userUuid, shipmentFlg, mstComponent);
                        }
                    } catch (NoResultException e) {
                        // 部品ロット登録画面から空白文字列の場合、新規ロット番号登録、生産、日報、出荷などの場合新規登録ロジック外す
                        if (shipmentFlg != 1) {
                            // 追加
                            componentLot = new TblComponentLot();
                            componentLot.setUuid(IDGenerator.generate());
                            componentLot.setComponentId(componentId);
                            componentLot.setProcedureCode(procedureCode);
                            componentLot.setStockId(tblStock.getUuid());
                            componentLot.setLotNo(lotNumber);
                            if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                                componentLot.setLotQty((int) quantity);
                                componentLot.setStockQty((int) quantity);
                            } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                                componentLot.setLotQty((-1 * quantity) <= 0 ? 0 : (int) (-1 * quantity));
                                componentLot.setStockQty((-1 * quantity) <= 0 ? 0 : (int) (-1 * quantity));
                            }
                            // 2018/05/28 修正 下位階層部品ロット入庫データを作成する必要は無い S
                            if (componentLot.getLotQty() == 0 && componentLot.getStockQty() == 0) {
                                insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, quantity, tblStock, null, null, userUuid);
                                continue;
                            }
                            // 2018/05/28 修正 下位階層部品ロット入庫データを作成する必要は無い E
                            componentLot.setLotIssueDate(formatStockChangeDate);
                            componentLot.setStatus(status);
                            componentLot.setProductionDetailId(productionDetailId);
                            componentLot.setRemarks01(remarks01);
                            componentLot.setCreateDate(new Date());
                            componentLot.setCreateUserUuid(userUuid);
                            componentLot.setUpdateDate(componentLot.getCreateDate());
                            componentLot.setUpdateUserUuid(userUuid);

                            entityManager.persist(componentLot);

                            insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, quantity, tblStock, componentLot, null, userUuid);
                        } else {
                            // 不存在なロット番号指定した場合も在庫数が0より大きいロット発行日古い順に引き算する　在庫3.1フェーズ対応する
                            updateTblComponentLot(componentId, procedureCode, Arrays.asList(""), storeType, quantity, tblStock, formatStockChangeDate, productionDetailId, status, remarks01, userUuid, shipmentFlg, mstComponent);
                        }
                    }
                    // ロット番号を指定していない場合は在庫数が0より大きいロット発行日古い順に引き算する
                } else {
                    // 在庫数が0より大きいロット発行日昇順レコードに引き算する
                    Query query = entityManager.createNamedQuery("TblComponentLot.findByComponentId");
                    query.setParameter("componentId", componentId);
                    query.setParameter("procedureCode", procedureCode);
                    List<TblComponentLot> tblComponentLotList = query.getResultList();
                    if (tblComponentLotList != null && !tblComponentLotList.isEmpty()) {
                        long beforeStockQty = 0;
                        for (TblComponentLot tblComponentLot : tblComponentLotList) {
                            if (quantity == 0) {
                                break;
                            }
                            beforeStockQty = tblComponentLot.getStockQty();
                            stockDetailQty = 0;
//                            // 複数のロット番号で引き算した場合は最初に引き算したロット番号部品ロットレコードを返却する
//                            if (componentLot == null) {
//                                componentLot = tblComponentLot;
//                            }
                            if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                                tblComponentLot.setUpdateUserUuid(userUuid);
                                tblComponentLot.setUpdateDate(new Date());
                                tblComponentLot.setProcedureCode(procedureCode);
                                boolean rst = (tblComponentLot.getStockQty() + quantity) > 0;
                                if (rst) {
                                    if (tblComponentLot.getLotQty() >= (tblComponentLot.getStockQty() + quantity)) {
                                        stockDetailQty = quantity;
                                        tblComponentLot.setStockQty((int) (tblComponentLot.getStockQty() + quantity));
                                        entityManager.merge(tblComponentLot);
                                        insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, stockDetailQty, tblStock, tblComponentLot, null, userUuid);
                                        quantity = 0;
                                        break;
                                    } else {
                                        stockDetailQty = tblComponentLot.getLotQty() - tblComponentLot.getStockQty();
                                        quantity = tblComponentLot.getStockQty() + quantity - tblComponentLot.getLotQty();
                                        tblComponentLot.setStockQty(tblComponentLot.getLotQty());
                                        entityManager.merge(tblComponentLot);
                                    }
                                } else {
                                    stockDetailQty = -1 * tblComponentLot.getStockQty();
                                    quantity = tblComponentLot.getStockQty() + quantity;
                                    tblComponentLot.setStockQty(0);
                                    entityManager.merge(tblComponentLot);
                                }
                            } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                                if (tblComponentLot.getStockQty() != 0) {
                                    tblComponentLot.setUpdateUserUuid(userUuid);
                                    tblComponentLot.setUpdateDate(new Date());
                                    tblComponentLot.setProcedureCode(procedureCode);
                                    boolean rst = (tblComponentLot.getStockQty() - quantity) > 0;
                                    if (rst) {
                                        if (tblComponentLot.getLotQty() >= (tblComponentLot.getStockQty() - quantity)) {
                                            stockDetailQty = quantity;
                                            tblComponentLot.setStockQty((int) (tblComponentLot.getStockQty() - quantity));
                                            entityManager.merge(tblComponentLot);
                                            insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, stockDetailQty, tblStock, tblComponentLot, null, userUuid);
                                            break;
                                        } else {
                                            stockDetailQty = tblComponentLot.getLotQty() - tblComponentLot.getStockQty();
                                            quantity = tblComponentLot.getLotQty() - (tblComponentLot.getStockQty() - quantity);
                                            tblComponentLot.setStockQty(tblComponentLot.getLotQty());
                                            entityManager.merge(tblComponentLot);
                                        }
                                    } else {
                                        stockDetailQty = tblComponentLot.getStockQty();
                                        quantity = Math.abs(tblComponentLot.getStockQty() - quantity);
                                        tblComponentLot.setStockQty(0);
                                        entityManager.merge(tblComponentLot);
                                    }
                                }
                            }
                            
                            if ((storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) && beforeStockQty == 0) {
                                // Do Nothing
                            } else {
                                insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, stockDetailQty, tblStock, tblComponentLot, null, userUuid);
                            }
                        }
                    }
                    if (quantity > 0) {
                        insertTblStockDetail(mstComponent, procedureCode, storeType, formatStockChangeDate, quantity, tblStock, null, null, userUuid);
                    }
                }
            }
        }
    }

    private void insertTblStockDetail(MstComponent mstComponent, String procedureCode, int storeType, Date formatStockChangeDate, long quantity, TblStock tblStock, TblComponentLot tblComponentLot, String shipmentId, String userUuid) {
        TblStockDetail tblStockDetail = new TblStockDetail();
        tblStockDetail.setUuid(IDGenerator.generate());
        tblStockDetail.setComponentId(mstComponent.getId());
        tblStockDetail.setProcedureCode(procedureCode);
        tblStockDetail.setStockId(tblStock.getUuid());
        tblStockDetail.setStockType(storeType);
        tblStockDetail.setMoveDate(formatStockChangeDate);
        if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD || storeType == CommonConstants.NONE) {
            tblStockDetail.setMoveQuantity(quantity);
            tblStockDetail.setMoveCost(new BigDecimal(quantity).multiply(mstComponent.getUnitPrice()));
        } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
            tblStockDetail.setMoveQuantity(-1 * quantity);
            tblStockDetail.setMoveCost(new BigDecimal(-1 * quantity).multiply(mstComponent.getUnitPrice()));
        }
        tblStockDetail.setShipmentId(shipmentId);
        if (tblComponentLot != null) {
            tblStockDetail.setComponentLotId(tblComponentLot.getUuid());
            tblStockDetail.setStockQuantity(tblComponentLot.getStockQty());
        }
        tblStockDetail.setCreateDate(new Date());
        tblStockDetail.setCreateUserUuid(userUuid);
        tblStockDetail.setUpdateDate(tblStockDetail.getCreateDate());
        tblStockDetail.setUpdateUserUuid(userUuid);

        entityManager.persist(tblStockDetail);
    }

    private TblProductionStock updateTblProductionStock(String componentId, String procedureCode, String productionDetailId, long quantity, int storeType, String userUuid) {
        Query query = entityManager.createNamedQuery("TblProductionStock.findByUniqueKey");
        query.setParameter("componentId", componentId);
        query.setParameter("procedureCode", procedureCode);
        query.setParameter("productionDetailId", productionDetailId);
        List<TblProductionStock> tblProductionStockList = query.getResultList();
        if (tblProductionStockList != null && !tblProductionStockList.isEmpty()) {
            if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                tblProductionStockList.get(0).setStockQuantity(tblProductionStockList.get(0).getStockQuantity() + quantity);
            } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                tblProductionStockList.get(0).setStockQuantity(tblProductionStockList.get(0).getStockQuantity() - quantity);
            }
            tblProductionStockList.get(0).setUpdateUserUuid(userUuid);
            tblProductionStockList.get(0).setUpdateDate(new Date());
            entityManager.merge(tblProductionStockList.get(0));

            return tblProductionStockList.get(0);
        } else {
            TblProductionStock tblProductionStock = new TblProductionStock();
            tblProductionStock.setUuid(IDGenerator.generate());
            tblProductionStock.setComponentId(componentId);
            tblProductionStock.setProcedureCode(procedureCode);
            tblProductionStock.setProductionDetailId(productionDetailId);
            tblProductionStock.setStockQuantity(quantity);
            if (storeType == CommonConstants.STORE || storeType == CommonConstants.STORE_DISCARD) {
                tblProductionStock.setStockQuantity(quantity);
            } else if (storeType == CommonConstants.DELIVERY || storeType == CommonConstants.DELIVERY_DISCARD) {
                tblProductionStock.setStockQuantity(-1 * quantity);
            }
            tblProductionStock.setCreateDate(new Date());
            tblProductionStock.setCreateUserUuid(userUuid);
            tblProductionStock.setUpdateDate(tblProductionStock.getCreateDate());
            tblProductionStock.setUpdateUserUuid(userUuid);
            entityManager.persist(tblProductionStock);

            return tblProductionStock;
        }
    }

    public List<MstComponentStructure> getStructureListByParentComponentId(String parentComponentId) {
        String sql = "SELECT m FROM MstComponentStructure m WHERE m.parentComponentId = :parentComponentId GROUP BY m.parentComponentId, m.componentId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("parentComponentId", parentComponentId);

        return query.getResultList();
    }
    
    private int changeStoreType(int storeType) {
        int changedStoreType = 0;
        switch (storeType) {
            case CommonConstants.STORE:
                changedStoreType = CommonConstants.DELIVERY;
                break;
            case CommonConstants.DELIVERY:
                changedStoreType = CommonConstants.STORE;
                break;
            case CommonConstants.STORE_DISCARD:
                changedStoreType = CommonConstants.DELIVERY_DISCARD;
                break;
            case CommonConstants.DELIVERY_DISCARD:
                changedStoreType = CommonConstants.STORE_DISCARD;
                break;
            default:
                break;
        }
        return changedStoreType;
    }

    /**
     * 部品毎の完成品在庫数の参照
     *
     * @param componentCode
     * @param isPurchasedPart
     * @param finalProcedureOnly
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @param langId
     * @return
     */
    public TblStockVoList getTblStockVoList(String componentCode, int isPurchasedPart, int finalProcedureOnly, String sidx, String sord, int pageNumber, int pageSize, boolean isPage, String langId) {

        TblStockVoList tblStockVoList = new TblStockVoList();

        if (isPage) {
            List count = getSqlByPage(componentCode, isPurchasedPart, finalProcedureOnly, sidx, sord, pageNumber, pageSize, true, isPage);
            // ページをめぐる
            Pager pager = new Pager();
            tblStockVoList.setPageNumber(pageNumber);
            long counts = count.size();//(long) count.get(0);
            tblStockVoList.setCount(counts);
            tblStockVoList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        }

        List list = getSqlByPage(componentCode, isPurchasedPart, finalProcedureOnly, sidx, sord, pageNumber, pageSize, false, isPage);
        if (!list.isEmpty()) {

            MstCurrencyList mstCurrencyList = mstCurrencyService.getMstCurrency();
            Map<String, String> currencyUnitMap = new HashMap();
            List<String> dictKeyList = new ArrayList();
            for (int i = 0; i < mstCurrencyList.getMstCurrencies().size(); i++) {
                MstCurrency mstCurrency = mstCurrencyList.getMstCurrencies().get(i);
                dictKeyList.add(mstCurrency.getCurrencyUnitDictKey());
                currencyUnitMap.put(mstCurrency.getCurrencyCode(), mstCurrency.getCurrencyUnitDictKey());
            }
            Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeyList);

            List<TblStockVo> tblStockVolist = new ArrayList<>();
            TblStockVo tblStockVo;

            FileUtil fu = new FileUtil();
            for (Object obj : list) {
                Object[] objTblStock = (Object[]) obj;
                tblStockVo = new TblStockVo();
                int coLIndex = 0;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setUuid(String.valueOf(objTblStock[coLIndex]));
                } else {
                    tblStockVo.setUuid("");
                }
                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setComponentId(String.valueOf(objTblStock[coLIndex]));
                } else {
                    tblStockVo.setComponentId("");
                }
                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setComponentCode(String.valueOf(objTblStock[coLIndex]));
                } else {
                    tblStockVo.setComponentCode("");
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setComponentName(String.valueOf(objTblStock[coLIndex]));
                } else {
                    tblStockVo.setComponentName("");
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setIsPurchasedPart(Integer.parseInt(String.valueOf(objTblStock[coLIndex])));
                } else {
                    tblStockVo.setIsPurchasedPart(0);
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setProcedureName(String.valueOf(objTblStock[coLIndex]));
                } else {
                    tblStockVo.setProcedureName("");
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setProcedureCode(String.valueOf(objTblStock[coLIndex]));
                } else {
                    tblStockVo.setProcedureCode("");
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setCurrencyUnit(dictMap.get(currencyUnitMap.get(String.valueOf(objTblStock[coLIndex]))));//通貨単位
                } else {
                    tblStockVo.setCurrencyUnit("");//通貨単位
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setStockUnit(Integer.parseInt(String.valueOf(objTblStock[coLIndex])));//在庫単位数
                } else {
                    tblStockVo.setStockUnit(0);//在庫単位数
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setStockQuantity(Integer.parseInt(String.valueOf(objTblStock[coLIndex])));//在庫数
                } else {
                    tblStockVo.setStockQuantity(0);//在庫数
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    // 在庫管理テーブルの在庫数×部品マスタの単価を表示
                    BigDecimal unitPrice = new BigDecimal(String.valueOf(objTblStock[coLIndex]));
                    tblStockVo.setUnitPrice(unitPrice); //単価
                    BigDecimal stockQuantity = new BigDecimal(tblStockVo.getStockQuantity());
                    BigDecimal diff = stockQuantity.multiply(unitPrice);
                    tblStockVo.setStockCost(diff.setScale(3, BigDecimal.ROUND_DOWN));//在庫金額

                } else {
                    tblStockVo.setUnitPrice(new BigDecimal(0.000)); // 単価
                    tblStockVo.setStockCost(new BigDecimal(0.000)); // 在庫金額
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setMoveDate(fu.getDateTimeFormatForStr(objTblStock[coLIndex]));//入出庫日
                } else {
                    tblStockVo.setMoveDate("");//入出庫日
                }

                coLIndex++;
                if (objTblStock[coLIndex] != null && StringUtils.isNotEmpty(String.valueOf(objTblStock[coLIndex]))) {
                    tblStockVo.setUpdateDateTime(fu.getDateTimeFormatForStr(objTblStock[coLIndex]));//更新日時
                } else {
                    tblStockVo.setUpdateDateTime("");//更新日時
                }

                tblStockVo.setOperationFlag("2");

                tblStockVolist.add(tblStockVo);
            }
            tblStockVoList.setTblStockVos(tblStockVolist);
        }
        return tblStockVoList;
    }

    /**
     *
     * @param componentCode
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    private List getSqlByPage(String componentCode, int isPurchasedPart, int finalProcedureOnly, String sidx,
            String sord, int pageNumber, int pageSize, boolean isCount, boolean isPage) {

        StringBuilder sql;
        String strComponentCode = "";

        if (isCount) {
            sql = new StringBuilder("SELECT mstProcedure.procedureCode ");

        } else {
            sql = new StringBuilder(
                    " SELECT "
                    + " tblStock.uuid "
                    + " ,mstComponent.id "
                    + " ,mstComponent.componentCode "
                    // 在庫ＳＴＥＰ2により追加 S
                    + " ,mstComponent.componentName "
                    + " ,mstComponent.isPurchasedPart "
                    + " ,mstProcedure.procedureName  "
                    // 在庫ＳＴＥＰ2により追加 E
                    + " ,mstProcedure.procedureCode "
                    + " ,mstComponent.currencyCode "
                    + " ,mstComponent.stockUnit "
                    + " ,tblStock.stockQuantity "
                    + " ,mstComponent.unitPrice "
                    + " ,tblStock.moveDate "
                    + " ,tblStock.updateDate ");
        }
        sql.append(" FROM MstProcedure mstProcedure  "
                + " LEFT JOIN MstComponent mstComponent ON  mstComponent.id = mstProcedure.componentId "
                + " LEFT JOIN TblStock tblStock ON  mstComponent.id = tblStock.componentId and mstProcedure.procedureCode = tblStock.procedureCode "
                + " WHERE 1=1 AND mstProcedure.procedureCode IS NOT NULL AND mstProcedure.externalFlg = 0"
        );

        if (componentCode != null && !"".equals(componentCode)) {
            strComponentCode = componentCode.trim();
            sql.append("  AND mstComponent.componentCode LIKE :componentCode ");
        }

        if (isPurchasedPart == 1) {//部品マスタの購入部品フラグ
            sql.append("  AND mstComponent.isPurchasedPart = 1 ");
        }

        if (finalProcedureOnly == 1) {//最終工程フラグ
            sql.append("  AND mstProcedure.finalFlg = 1 ");
        }

        if (!isCount) {
            if (StringUtils.isNotEmpty(sidx) && isPage) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                // 表示順は設備IDの昇順。
                sql.append(sortStr);

            } else {

                // 表示順は設備IDの昇順。
                sql.append(" order by mstComponent.componentCode, mstProcedure.procedureCode ");// componentCodeの昇順、工程番号昇順

            }

        }

        Query query = entityManager.createQuery(sql.toString());

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + strComponentCode + "%");
        }

        // 画面改ページを設定する
        if (!isCount && isPage) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    /**
     * 在庫数を更新する。
     *
     * @param tblStockVos
     * @param userUuid
     * @return
     */
    @Transactional
    public BasicResponse updateTblStock(TblStockVoList tblStockVos, String userUuid) {

        if (tblStockVos != null && tblStockVos.getTblStockVos() != null && !tblStockVos.getTblStockVos().isEmpty()) {
            for (TblStockVo tblStockVo : tblStockVos.getTblStockVos()) {
                if (StringUtils.isNotEmpty(tblStockVo.getComponentId())) {
                    TblStock tblStock = getSingleResultTblStockByComponentId(tblStockVo.getComponentId(), tblStockVo.getProcedureCode());
                    // tblStock = entityManager.find(TblStock.class, tblStockVo.getUuid());
                    if (tblStock != null && tblStock.getStockQuantity() != tblStockVo.getStockQuantity()) {
                        long stockQuantityBfUpd = tblStock.getStockQuantity();
                        tblStock.setStockQuantity(tblStockVo.getStockQuantity());
                        tblStock.setProcedureCode(tblStockVo.getProcedureCode());
                        tblStock.setUpdateDate(new Date());
                        tblStock.setUpdateUserUuid(userUuid);
                        entityManager.merge(tblStock);

                        insertTblStockDetail(tblStock.getMstComponent(), tblStock.getProcedureCode(), 0, new Date(), tblStock.getStockQuantity() - stockQuantityBfUpd, tblStock, null, null, userUuid);
                    } else if (tblStock == null) {

                        tblStock = new TblStock();
                        tblStock.setUuid(IDGenerator.generate());
                        tblStock.setComponentId(tblStockVo.getComponentId());
                        tblStock.setStockQuantity(tblStockVo.getStockQuantity());
                        tblStock.setProcedureCode(tblStockVo.getProcedureCode());
                        tblStock.setCreateDate(new Date());
                        tblStock.setCreateUserUuid(userUuid);
                        tblStock.setUpdateDate(new Date());
                        tblStock.setUpdateUserUuid(userUuid);

                        entityManager.persist(tblStock);
                        MstComponent mstComponent = entityManager.find(MstComponent.class, tblStockVo.getComponentCode());
                        insertTblStockDetail(mstComponent, tblStock.getProcedureCode(), 0, new Date(), tblStock.getStockQuantity(), tblStock, null, null, userUuid);
                    }
                }
            }
        }
        BasicResponse basicResponse = new BasicResponse();
        return basicResponse;
    }

    /**
     * 在庫テーブルより条件にあてはまるデータを取得し、CSVファイルに出力する。　部品コード昇順
     *
     * @param componentCode
     * @param isPurchasedPart
     * @param loginUser
     * @return
     */
    public FileReponse getTblStockVoListCsv(String componentCode, int isPurchasedPart, int finalProcedureOnly, LoginUser loginUser) {

        ArrayList<ArrayList> gLineList = new ArrayList<>();

        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("component_code", "procedure_code", "procedure_name", "stock_quantity",
                "stock_cost", "currency_unit", "stock_unit", "move_date", "update_date", "stock_quantity_registration", "component_name", "is_purchased_part");

        Map<String, String> csvHeader = mstDictionaryService.getDictionaryHashMap(loginUser.getLangId(), dictKeyList);
        ArrayList headList = new ArrayList();
        headList.add(csvHeader.get("component_code"));
        headList.add(csvHeader.get("component_name"));
        headList.add(csvHeader.get("is_purchased_part"));
        headList.add(csvHeader.get("procedure_name"));
        headList.add(csvHeader.get("procedure_code"));
        headList.add(csvHeader.get("stock_quantity"));
        headList.add(csvHeader.get("stock_cost"));
        headList.add(csvHeader.get("currency_unit"));
        headList.add(csvHeader.get("stock_unit"));
        headList.add(csvHeader.get("move_date"));
        headList.add(csvHeader.get("update_date"));
        //出力へーだー準備
        gLineList.add(headList);

        //明細データを取得
        TblStockVoList tblStockVoList = getTblStockVoList(componentCode, isPurchasedPart, finalProcedureOnly, "", "", 0, 0, false, loginUser.getLangId());
        if (tblStockVoList.getTblStockVos() != null && !tblStockVoList.getTblStockVos().isEmpty()) {
            /*Detail*/
            ArrayList lineList;
            for (int i = 0; i < tblStockVoList.getTblStockVos().size(); i++) {
                lineList = new ArrayList();

                TblStockVo tblStockVo = tblStockVoList.getTblStockVos().get(i);
                lineList.add(tblStockVo.getComponentCode());//部品コード
                lineList.add(tblStockVo.getComponentName());//部品名称
                lineList.add(String.valueOf(tblStockVo.getIsPurchasedPart()));//購買フラグ
                lineList.add(tblStockVo.getProcedureName());//工程名称
                lineList.add(tblStockVo.getProcedureCode());//工程番号
                lineList.add(String.valueOf(tblStockVo.getStockQuantity()));//在庫数
                lineList.add(String.valueOf(tblStockVo.getStockCost()));//在庫金額
                lineList.add(tblStockVo.getCurrencyUnit());//通貨Code
                lineList.add(String.valueOf(tblStockVo.getStockUnit()));//在庫単位数
                lineList.add(tblStockVo.getMoveDate());//入出庫日
                lineList.add(tblStockVo.getUpdateDateTime());//更新日時
                gLineList.add(lineList);
            }
        }
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable("tbl_stock");
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_STOCK_QUANTITY);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(csvHeader.get("stock_quantity_registration")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        FileReponse fileReponse = new FileReponse();
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

    /**
     * CSVファイルを取込み
     *
     * @param fileUuid
     * @param loginUser
     * @return
     */
    @Transactional
    public ImportResultResponse postTblStockVoListCsv(String fileUuid, LoginUser loginUser) {
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
            dictKeyList.add("stock_quantity_registration");
            dictKeyList.add("row_number");
            dictKeyList.add("component_code");
            dictKeyList.add("component_name");
            dictKeyList.add("error_detail");
            dictKeyList.add("error");
            dictKeyList.add("msg_error_wrong_csv_layout");
            dictKeyList.add("msg_record_updated");
            dictKeyList.add("db_process");
            dictKeyList.add("mst_error_record_not_found");
            dictKeyList.add("msg_record_added");
            dictKeyList.add("stock_quantity");
            dictKeyList.add("msg_error_value_invalid");
            dictKeyList.add("is_purchased_part");
            dictKeyList.add("procedure_code");
            dictKeyList.add("procedure_name");
            dictKeyList.add("msg_error_not_null");
            dictKeyList.add("move_date");

            Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(userLangId, dictKeyList);

            Map<String, Integer> colIndexMap = new HashMap<>();

            for (int j = 0; j < readList.get(0).size(); j++) {
                if (readList.get(0).get(j).trim().equals(dictMap.get("component_code"))) {
                    colIndexMap.put("componentCode", j);
                } else if (readList.get(0).get(j).trim().equals(dictMap.get("component_name"))) {
                    colIndexMap.put("componentName", j);
                } else if (readList.get(0).get(j).trim().equals(dictMap.get("is_purchased_part"))) {
                    colIndexMap.put("isPurchasedPart", j);
                } else if (readList.get(0).get(j).trim().equals(dictMap.get("procedure_code"))) {
                    colIndexMap.put("procedureCode", j);
                } else if (readList.get(0).get(j).trim().equals(dictMap.get("procedure_name"))) {
                    colIndexMap.put("procedureName", j);
                } else if (readList.get(0).get(j).trim().equals(dictMap.get("stock_quantity"))) {
                    colIndexMap.put("stockQuantity", j);
                } else if (readList.get(0).get(j).trim().equals(dictMap.get("move_date"))) {
                    colIndexMap.put("moveDate", j);
                }
            }

            FileUtil fileUtil = new FileUtil();
            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);

                String componentCode = null, componentName = null, isPurchasedPart = null, moveDate = null, stockProcedureCode = null, stockProcedureName = null, stockQuantity = null;
                if (colIndexMap.get("componentCode") != null) {
                    componentCode = String.valueOf(comList.get(colIndexMap.get("componentCode"))).trim();
                }
                if (colIndexMap.get("componentName") != null) {
                    componentName = String.valueOf(comList.get(colIndexMap.get("componentName"))).trim();
                }
                if (colIndexMap.get("isPurchasedPart") != null) {
                    isPurchasedPart = String.valueOf(comList.get(colIndexMap.get("isPurchasedPart"))).trim();
                }
                if (colIndexMap.get("procedureCode") != null) {
                    stockProcedureCode = String.valueOf(comList.get(colIndexMap.get("procedureCode"))).trim();
                }
                if (colIndexMap.get("procedureName") != null) {
                    stockProcedureName = String.valueOf(comList.get(colIndexMap.get("procedureName"))).trim();
                }
                if (colIndexMap.get("stockQuantity") != null) {
                    stockQuantity = String.valueOf(comList.get(colIndexMap.get("stockQuantity"))).trim();
                }
//                if (colIndexMap.get("moveDate") != null) {
//                    moveDate = String.valueOf(comList.get(colIndexMap.get("moveDate"))).trim();
//                }

                try {
                    Integer.parseInt(stockQuantity);
                } catch (NumberFormatException e) {
                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("stock_quantity"), stockQuantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                    failedCount = failedCount + 1;
                    continue;
                }

                try {
                    Integer.parseInt(isPurchasedPart);
                } catch (NumberFormatException e) {
                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("is_purchased_part"), isPurchasedPart, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
                    failedCount = failedCount + 1;
                    continue;
                }

//                if ("-1".equals(DateFormat.formatDateYear(moveDate, DateFormat.DATETIME_FORMAT))) {
//                    //エラー情報をログファイルに記入
//                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("move_date"), moveDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_date_format_invalid")));
//                    failedCount = failedCount + 1;
//                    continue;
//                }
                MstComponent mstComponent;
                //一、部品コード存在チェック、存在であれば、引き続き、存在していない場合スキップ
                //  logFile 
                if (StringUtils.isEmpty(componentCode)) {
                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_wrong_csv_layout")));
                    failedCount = failedCount + 1;
                    continue;
                } else {
                    mstComponent = mstComponentService.getMstComponent(componentCode);
                    if (mstComponent == null) {

                        if (StringUtils.isEmpty(stockProcedureCode)) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), stockProcedureCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                            failedCount = failedCount + 1;
                            continue;
                        }

                        if (StringUtils.isEmpty(componentName)) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_name"), componentName, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                            failedCount = failedCount + 1;
                            continue;
                        }

                        // 在庫ＳＴＥＰ２により仕様変更する　部品マスタに存在しない部品の登録処理追加 S
                        mstComponent = new MstComponent();
                        mstComponent.setId(IDGenerator.generate());
                        mstComponent.setComponentCode(componentCode);
                        mstComponent.setComponentName(componentName);
                        mstComponent.setComponentType(0);
                        mstComponent.setCountPerShot(0);
                        mstComponent.setIsCircuitBoard(0);
                        mstComponent.setUnitPrice(BigDecimal.ZERO);
                        mstComponent.setIsPurchasedPart(Integer.parseInt(isPurchasedPart));
                        Date sysDate = new Date();
                        mstComponent.setCreateDate(sysDate);
                        mstComponent.setUpdateDate(sysDate);
                        mstComponent.setCreateUserUuid(loginUser.getUserUuid());
                        mstComponent.setUpdateUserUuid(loginUser.getUserUuid());
                        mstComponentService.createMstComponent(mstComponent);

                        MstProcedure mstProcedure = new MstProcedure();
                        mstProcedure.setId(IDGenerator.generate());
                        mstProcedure.setComponentId(mstComponent.getId());
                        mstProcedure.setProcedureCode(stockProcedureCode);
                        mstProcedure.setProcedureName(stockProcedureName);
                        mstProcedure.setExternalFlg(CommonConstants.MINEFLAG);
                        mstProcedure.setSeq(1);
                        mstProcedure.setCreateDate(sysDate);
                        mstProcedure.setUpdateDate(sysDate);
                        mstProcedure.setCreateUserUuid(loginUser.getUserUuid());
                        mstProcedure.setUpdateUserUuid(loginUser.getUserUuid());

                        entityManager.persist(mstProcedure);

                        // 在庫ＳＴＥＰ２により仕様変更する　部品マスタに存在しない部品の登録処理追加 E
                    } else {
                        MstProcedure mstProcedure = mstProcedureService.getFinalProcedureByComponentId(mstComponent.getId());
                        // 工程番号
                        if (mstProcedure == null || !mstProcedure.getProcedureCode().equals(stockProcedureCode)) {
                            //エラー情報をログファイルに記入
                            failedCount = failedCount + 1;
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), stockProcedureCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            continue;
                        }
                    }
                }

                // 二、
                // 1, データをチェック実施
                // 2, index 0 と 3データ更新
                // 3, 2で在庫管理テーブルを検索し、存在であれば、更新する、存在していない場合スキップ
                TblStock tblStock = getSingleResultTblStockByComponentId(mstComponent.getId(), stockProcedureCode);
                if (tblStock != null) {
                    // 更新
                    long stockQuantityBfUpd = tblStock.getStockQuantity();
                    tblStock.setUpdateDate(new Date());
                    tblStock.setUpdateUserUuid(loginUser.getUserUuid());
                    tblStock.setStockQuantity(Integer.parseInt(stockQuantity));
                    tblStock.setProcedureCode(stockProcedureCode);
                    //tblStock.setMoveDate(DateFormat.strToDatetime(moveDate));
                    entityManager.merge(tblStock);

                    // 在庫履歴登録
                    insertTblStockDetail(tblStock.getMstComponent(), tblStock.getProcedureCode(), 0, new Date(), tblStock.getStockQuantity() - stockQuantityBfUpd, tblStock, null, null, loginUser.getUserUuid());
                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_updated")));
                    updatedCount = updatedCount + 1;
                    // continue;
                } else {

                    tblStock = new TblStock();
                    tblStock.setUuid(IDGenerator.generate());
                    tblStock.setComponentId(mstComponent.getId());
                    tblStock.setStockQuantity(Integer.parseInt(stockQuantity));
                    tblStock.setProcedureCode(stockProcedureCode);
                    //tblStock.setMoveDate(DateFormat.strToDatetime(moveDate));
                    tblStock.setCreateDate(new Date());
                    tblStock.setCreateUserUuid(loginUser.getUserUuid());
                    tblStock.setUpdateDate(new Date());
                    tblStock.setUpdateUserUuid(loginUser.getUserUuid());

                    entityManager.persist(tblStock);

                    insertTblStockDetail(mstComponent, tblStock.getProcedureCode(), 0, new Date(), tblStock.getStockQuantity(), tblStock, null, null, loginUser.getUserUuid());
                    // logFile
                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_added")));
                    addedCount = addedCount + 1;
                    // continue;
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
            tblCsvImport.setImportTable("tbl_stock");

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
            tblCsvImport.setLogFileName(FileUtil.getLogFileName(dictMap.get("stock_quantity_registration")));

            tblCsvImportService.createCsvImpor(tblCsvImport);
        }

        return importResultResponse;
    }

    /**
     *
     * @param componentId
     * @param procedureCode
     * @return
     */
    private TblStock getSingleResultTblStockByComponentId(String componentId, String procedureCode) {

        StringBuilder sql;
        sql = new StringBuilder("SELECT tblStock FROM  TblStock tblStock "
                + " WHERE tblStock.componentId =:componentId "
                        + " AND tblStock.procedureCode =:procedureCode "
        );

        try {
            Query query = entityManager.createQuery(sql.toString());
            query.setParameter("componentId", componentId);
            query.setParameter("procedureCode", procedureCode);
            TblStock tblStock = (TblStock) query.getSingleResult();

            return tblStock;
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * 在庫数を取得
     *
     * @param componentId
     * @return
     */
    public TblStockVo getTblStockQuantity(String componentId) {
        TblStockVo tblStockVo = new TblStockVo();
        tblStockVo.setStockQuantity(0);
        if (StringUtils.isNotEmpty(componentId)) {
            MstProcedure mstProcedure = mstProcedureService.getFinalProcedureByComponentId(componentId);
            if (mstProcedure != null) {
                Query query = entityManager.createNamedQuery("TblStock.findByComponentAndProcedure");
                query.setParameter("componentId", mstProcedure.getComponentId());
                query.setParameter("procedureCode", mstProcedure.getProcedureCode());
                try {
                    TblStock tblStock = (TblStock) query.getSingleResult();
                    tblStockVo.setStockQuantity(tblStock.getStockQuantity());
                } catch (NoResultException e) {
                }
            }
        }
        return tblStockVo;
    }

//    /**
//     * CSVファイルから一括で入出庫
//     *
//     * @param fileUuid
//     * @param storeType
//     * @param loginUser
//     * @return
//     */
//    @Transactional
//    public ImportResultResponse postTblStockStoreCsv(String fileUuid, int storeType, LoginUser loginUser) {
//        ImportResultResponse importResultResponse = new ImportResultResponse();
//
//        //①CSVファイルを取込み
//        long succeededCount = 0;
//        long addedCount = 0;
//        long updatedCount = 0;
//        long failedCount = 0;
//
//        String logFileUuid = IDGenerator.generate();
//        importResultResponse.setLog(logFileUuid);
//        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
//
//        if (!csvFile.endsWith(CommonConstants.CSV)) {
//            importResultResponse.setError(true);
//            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
//            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
//            importResultResponse.setErrorMessage(msg);
//            return importResultResponse;
//        }
//
//        ArrayList<List<String>> readList = CSVFileUtil.readCsv(csvFile);
//        if (readList.size() <= 1) {
//            return importResultResponse;
//        } else {
//            String userLangId = loginUser.getLangId();
//            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
//
//            ArrayList dictKeyList = new ArrayList();
//            dictKeyList.add("stock_quantity_registration");
//            dictKeyList.add("row_number");
//            dictKeyList.add("component_code");
//            dictKeyList.add("component_name");
//            dictKeyList.add("error_detail");
//            dictKeyList.add("error");
//            dictKeyList.add("msg_error_wrong_csv_layout");
//            dictKeyList.add("msg_record_updated");
//            dictKeyList.add("msg_record_added");
//            dictKeyList.add("db_process");
//            dictKeyList.add("mst_error_record_not_found");
//            dictKeyList.add("stock_quantity");
//            dictKeyList.add("msg_error_value_invalid");
//            dictKeyList.add("procedure_code");
//            dictKeyList.add("msg_error_not_null");
//            dictKeyList.add("msg_error_date_format_invalid");
//            dictKeyList.add("move_date");
//
//            Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(userLangId, dictKeyList);
//
//            Map<String, Integer> colIndexMap = new HashMap<>();
//            for (int j = 0; j < readList.get(0).size() - 1; j++) {
//                if (readList.get(0).get(j).trim().equals(dictMap.get("component_code"))) {
//                    colIndexMap.put("componentCode", j);
//                } else if (readList.get(0).get(j).trim().equals(dictMap.get("procedure_code"))) {
//                    colIndexMap.put("procedureCode", j);
//                } else if (readList.get(0).get(j).trim().equals(dictMap.get("stock_quantity"))) {
//                    colIndexMap.put("stockQuantity", j);
//                } else if (readList.get(0).get(j).trim().equals(dictMap.get("move_date"))) {
//                    colIndexMap.put("moveDate", j);
//                }
//            }
//
//            FileUtil fileUtil = new FileUtil();
//            for (int i = 1; i < readList.size(); i++) {
//                ArrayList comList = (ArrayList) readList.get(i);
//
//                String componentCode = null, procedureCode = null, stockQuantity = null, moveDate = null;
//                if (colIndexMap.get("componentCode") != null) {
//                    componentCode = String.valueOf(comList.get(colIndexMap.get("componentCode"))).trim();
//                }
//                if (colIndexMap.get("procedureCode") != null) {
//                    procedureCode = String.valueOf(comList.get(colIndexMap.get("procedureCode"))).trim();
//                }
//                if (colIndexMap.get("stockQuantity") != null) {
//                    stockQuantity = String.valueOf(comList.get(colIndexMap.get("stockQuantity"))).trim();
//                }
//                if (colIndexMap.get("moveDate") != null) {
//                    moveDate = String.valueOf(comList.get(colIndexMap.get("moveDate"))).trim();
//                }
//
//                try {
//                    Long.parseLong(stockQuantity);
//                } catch (NumberFormatException e) {
//                    //エラー情報をログファイルに記入
//                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("stock_quantity"), stockQuantity, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_value_invalid")));
//                    failedCount = failedCount + 1;
//                    continue;
//                }
//                if ("-1".equals(DateFormat.formatDateYear(moveDate, DateFormat.DATETIME_FORMAT))) {
//                    //エラー情報をログファイルに記入
//                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("move_date"), moveDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_date_format_invalid")));
//                    failedCount = failedCount + 1;
//                    continue;
//                }
//
//                //一、部品コード存在チェック、存在であれば、引き続き、存在していない場合スキップ
//                //  logFile 
//                if (StringUtils.isEmpty(componentCode)) {
//                    //エラー情報をログファイルに記入
//                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
//                    failedCount = failedCount + 1;
//                } else {
//                    MstComponent mstComponent = mstComponentService.getMstComponent(componentCode);
//                    if (mstComponent == null) {
//                        //エラー情報をログファイルに記入
//                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
//                        failedCount = failedCount + 1;
//                        continue;
//                    }
//                    MstProcedure mstProcedure = mstProcedureService.getMaxProcedureCode(mstComponent.getId());
//                    // 工程番号
//                    if (mstProcedure == null || !mstProcedure.getProcedureCode().equals(procedureCode)) {
//                        //エラー情報をログファイルに記入
//                        failedCount = failedCount + 1;
//                        //エラー情報をログファイルに記入
//                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), procedureCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
//                        continue;
//                    }
//                    // 在庫管理呼ぶ
//                    ImportResultResponse response = doTblStock(componentCode, mstProcedure.getProcedureCode(), storeType, Long.parseLong(stockQuantity), moveDate, null, CommonConstants.SHIPMENT_NO, loginUser.getUserUuid(), loginUser.getLangId());
//                    if (response.isError()) {
//                        failedCount += 1;
//                    } else {
//                        succeededCount += 1;
//                        if (response.getAddedCount() != 0L) {
//                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_added")));
//                            addedCount += response.getAddedCount();
//                        }
//                        if (response.getUpdatedCount()!= 0L) {
//                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_record_updated")));
//                            updatedCount += response.getUpdatedCount();
//                        }
//                    }
//                }
//            }
//
//            // リターン情報
//            importResultResponse.setTotalCount(readList.size() - 1);
//            importResultResponse.setSucceededCount(succeededCount);
//            importResultResponse.setAddedCount(addedCount);
//            importResultResponse.setUpdatedCount(updatedCount);
//            importResultResponse.setFailedCount(failedCount);
//            importResultResponse.setLog(logFileUuid);
//
//            //アップロードログをテーブルに書き出し
//            TblCsvImport tblCsvImport = new TblCsvImport();
//            tblCsvImport.setImportUuid(IDGenerator.generate());
//            tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
//            tblCsvImport.setImportDate(new Date());
//
//            TblUploadFile tblUploadFile = new TblUploadFile();
//            tblUploadFile.setFileUuid(fileUuid);
//            tblCsvImport.setUploadFileUuid(tblUploadFile);
//            MstFunction mstFunction = new MstFunction();
//            mstFunction.setId(CommonConstants.FUN_ID_STOCK_QUANTITY);
//            tblCsvImport.setFunctionId(mstFunction);
//            tblCsvImport.setRecordCount(readList.size() - 1);
//            tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
//            tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
//            tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
//            tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
//            tblCsvImport.setLogFileUuid(logFileUuid);
//            tblCsvImport.setLogFileName(FileUtil.getLogFileName(dictMap.get("stock_quantity_registration")));
//
//            tblCsvImportService.createCsvImpor(tblCsvImport);
//        }
//
//        return importResultResponse;
//    }
}
