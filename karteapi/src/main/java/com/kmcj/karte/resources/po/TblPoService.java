/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.po;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.po.shipment.TblShipment;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.stock.TblStockService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
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
public class TblPoService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    
    @Inject
    private TblStockService tblStockService;
    
    @Inject
    private MstProcedureService mstProcedureService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("componentCode", " ORDER BY component.componentCode ");//　部品コード
        orderKey.put("deliveryDestName", " ORDER BY company.companyName ");// 納品先
        orderKey.put("itemNumber", " ORDER BY po.itemNumber ");// アイテムナンバー
        orderKey.put("orderNumber", " ORDER BY po.orderNumber ");// 発注番号
        orderKey.put("quantity", " ORDER BY po.quantity ");// 数量
        orderKey.put("orderDate", " ORDER BY po.orderDate ");// 受注日
        orderKey.put("dueDate", " ORDER BY po.dueDate ");// 納期

    }

    /**
     * 出荷登録 vb
     *
     * @param tblPoVo
     * @param userUuid
     * @param langId
     * @return
     */
    @Transactional
    public BasicResponse saveTblPo(TblPoVo tblPoVo, String userUuid, String langId) {
        BasicResponse basicResponse = new BasicResponse();
        // POテーブル
        FileUtil fileUtil = new FileUtil();
        TblPo tblPo = new TblPo();

        //key
        tblPo.setOrderNumber(tblPoVo.getOrderNumber());
        tblPo.setItemNumber(tblPoVo.getItemNumber());
        if (StringUtils.isNotEmpty(tblPoVo.getComponentId())) {
            tblPo.setComponentId(tblPoVo.getComponentId());
        }

        if (StringUtils.isNotEmpty(tblPoVo.getDeliveryDestId())) {
            tblPo.setDeliveryDestId(tblPoVo.getDeliveryDestId());
        } else {
            tblPo.setDeliveryDestId(null);
        }

        if (StringUtils.isNotEmpty(tblPoVo.getOrderDate())) {
            tblPo.setOrderDate(fileUtil.getDateParseForDate(tblPoVo.getOrderDate()));
        } else {
            tblPo.setOrderDate(null);
        }

        if (StringUtils.isNotEmpty(tblPoVo.getDueDate())) {
            tblPo.setDueDate(fileUtil.getDateParseForDate(tblPoVo.getDueDate()));
        } else {
            tblPo.setDueDate(null);
        }


        tblPo.setUnitPrice(new BigDecimal(0.000));
        
        tblPo.setQuantity(tblPoVo.getQuantity());

        TblPoVo oldTblPoVo = load(tblPoVo.getOrderNumber(), tblPoVo.getDeliveryDestId(), tblPoVo.getItemNumber());
//        //※POテーブルのキーである納品先、発注番号、アイテムナンバーがすでに重複した場合、受注数量、受注日、納期は上書き更新するが、
//        //部品コードに異なるものが指定された場合はエラーとする
//        if (oldTblPoVo != null && oldTblPoVo.getComponentId() != null && !oldTblPoVo.getComponentId().equals(tblPoVo.getComponentId())) {
//            basicResponse.setError(true);
//            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
//            String msg = mstDictionaryService.getDictionaryValue(langId, "msg_error_uniquekey_component");
//            basicResponse.setErrorMessage(msg);
//            return basicResponse;
//        }

        if (StringUtils.isNotEmpty(tblPoVo.getUuid())) {
            tblPoVo.setUuid(tblPoVo.getUuid());
        }

        if (oldTblPoVo != null) {
            tblPoVo.setUuid(oldTblPoVo.getUuid());
            tblPo.setCreateDate(oldTblPoVo.getCreateDate());
            tblPo.setCreateUserUuid(oldTblPoVo.getCreateUserUuid());
        }

        if (StringUtils.isNotEmpty(tblPoVo.getUuid())) {
            tblPo.setUuid(tblPoVo.getUuid());
            tblPo.setUpdateDate(new Date());
            tblPo.setUpdateUserUuid(userUuid);
            tblPo.setBatchUpdateStatus(CommonConstants.PO_BATCH_UPDATE_STATUS_UNDONE);//20171205 Apeng ADD
            entityManager.merge(tblPo);
        } else {
            String poUuid = IDGenerator.generate();
            tblPoVo.setUuid(poUuid);
            tblPo.setUuid(poUuid);
            tblPo.setCreateDate(new Date());
            tblPo.setUpdateDate(new Date());
            tblPo.setCreateUserUuid(userUuid);
            tblPo.setUpdateUserUuid(userUuid);
            tblPo.setBatchUpdateStatus(CommonConstants.PO_BATCH_UPDATE_STATUS_UNDONE);
            entityManager.persist(tblPo);
        }
        return basicResponse;
    }

    /**
     *
     * @param uuid
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteTblPo(String uuid, LoginUser loginUser) {

        // POを削除する際に在庫管理APIを呼び出して在庫数を加算する。
        //	部品コード　：　削除するPOで出荷テーブルを検索して取得した部品IDに対応する部品マスタの部品コード
        //	工程番号　：　部品コードの中で最大の工程番号
        //	分類　：　入庫赤伝票（STORE_DISCARD）
        //	数量　：　画面に入力した出荷数量
        //	在庫変更日　：　システム日付
        //	製造ロット番号　：　削除するPOで出荷テーブルを検索して取得した製造ロット番号
        Query query1 = entityManager.createNamedQuery("TblShipment.findByPoId");
        query1.setParameter("poId", uuid);
        List list = query1.getResultList();
        for (int i = 0; i < list.size(); i++) {
            TblShipment tblShipment = (TblShipment) list.get(i);
            MstComponent mstComponent = tblShipment.getMstComponent();
            MstProcedure mstProcedure = mstProcedureService.getFinalProcedureByComponentId(mstComponent.getId());
            if (mstProcedure != null) {
                tblStockService.doTblStock(tblShipment.getMstComponent().getComponentCode(), mstProcedure, null,
                        CommonConstants.STORE_DISCARD, tblShipment.getQuantity(), DateFormat.getCurrentDateTime(), tblShipment.getProductionLotNumber(), 0, null, CommonConstants.SHIPMENT_YES, null, loginUser.getUserUuid(), loginUser.getLangId());
            }

        }
        // POテーブル削除
        Query query = entityManager.createNamedQuery("TblPo.delete");
        query.setParameter("uuid", uuid);
        query.executeUpdate();

        return new BasicResponse();
    }

    /**
     *
     * @param orderNumber
     * @param deliveryDestName
     * @param componentCode
     * @param formatShipDateFrom
     * @param formatShipDateTo
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public TblPoVoList getPoList(
            String orderNumber,
            String deliveryDestName,
            String componentCode,
            Date formatShipDateFrom,
            Date formatShipDateTo,
            String sidx,
            String sord,
            int pageNumber,
            int pageSize) {

        TblPoVoList tblPoVoList = new TblPoVoList();

        List count = myQuery(orderNumber, deliveryDestName, componentCode, formatShipDateFrom, formatShipDateTo, sidx, sord, pageNumber, pageSize, true);

        // ページをめぐる
        Pager pager = new Pager();
        tblPoVoList.setPageNumber(pageNumber);
        long counts = (long) count.get(0);

        List<TblPoVo> tblPoVos = new ArrayList();
        TblPoVo tblPoVo;
        int index = 0;
        if (counts > 0) {
            List list = myQuery(orderNumber, deliveryDestName, componentCode, formatShipDateFrom, formatShipDateTo, sidx, sord, pageNumber, pageSize, false);
            FileUtil fileUtil = new FileUtil();
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                tblPoVo = new TblPoVo();
                TblPo tblPo = (TblPo) list.get(i);

                tblPoVo.setUuid(tblPo.getUuid());
                if (map.containsKey(tblPo.getUuid())) {
                    index++;
                    continue;
                }
                tblPoVo.setOrderNumber(FileUtil.getStringValue(tblPo.getOrderNumber()));
                tblPoVo.setItemNumber(FileUtil.getStringValue(tblPo.getItemNumber()));
                tblPoVo.setDeliveryDestId(tblPo.getDeliveryDestId());

                MstCompany mstCompany = tblPo.getMstCompany();
                if (mstCompany != null) {
                    tblPoVo.setDeliveryDestName(mstCompany.getCompanyName());
                } else {
                    tblPoVo.setDeliveryDestName("");
                }

                tblPoVo.setQuantity(tblPo.getQuantity());
                tblPoVo.setOrderDate(fileUtil.getDateFormatForStr(tblPo.getOrderDate()));
                tblPoVo.setDueDate(fileUtil.getDateFormatForStr(tblPo.getDueDate()));
                MstComponent mstComponent = tblPo.getMstComponent();
                if (mstComponent != null) {
                    tblPoVo.setComponentCode(mstComponent.getComponentCode());
                } else {
                    tblPoVo.setComponentCode("");
                }
                map.put(tblPo.getUuid(), 0);
                tblPoVos.add(tblPoVo);
            }
            counts = counts - index;
            tblPoVoList.setCount(counts);
            tblPoVoList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        }

        tblPoVoList.setTblPoVos(tblPoVos);
        return tblPoVoList;
    }

    /**
     *
     * @param orderNumber
     * @param deliveryDestId
     * @param componentCode
     * @param formatShipDateFrom
     * @param formatShipDateTo
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    private List myQuery(
            String orderNumber,
            String deliveryDestName,
            String componentCode,
            Date formatShipDateFrom,
            Date formatShipDateTo,
            String sidx,
            String sord,
            int pageNumber,
            int pageSize,
            boolean isCount) {

        StringBuilder sql = new StringBuilder("SELECT ");
        if (isCount) {
            sql.append(" COUNT(po) ");
        } else {
            sql.append(" po ");
        }
        sql.append(" FROM TblPo po LEFT JOIN FETCH po.mstCompany company LEFT JOIN FETCH po.mstComponent component ");

        if (formatShipDateFrom != null || formatShipDateTo != null) {
            sql.append(" LEFT JOIN FETCH po.tblShipment shipment ");
        }

        sql.append(" WHERE 1 = 1 ");

        if (StringUtils.isNotEmpty(orderNumber)) {
            sql.append(" AND  po.orderNumber LIKE :orderNumber ");
        }

        if (StringUtils.isNotEmpty(deliveryDestName)) {
            sql.append(" AND  company.companyName LIKE :deliveryDestName ");
        }

        if (StringUtils.isNotEmpty(componentCode)) {
            sql.append(" AND  component.componentCode LIKE :componentCode ");
        }

        if (formatShipDateFrom != null) {
            sql.append(" AND  shipment.shipDate >= :formatShipDateFrom ");
        }

        if (formatShipDateTo != null) {
            sql.append(" AND  shipment.shipDate <= :formatShipDateTo ");
        }

        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);

            } else {

                sql.append(" ORDER BY po.orderNumber ");

            }

        }

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(orderNumber)) {
            query.setParameter("orderNumber", "%" + orderNumber + "%");
        }

        if (StringUtils.isNotEmpty(deliveryDestName)) {
            query.setParameter("deliveryDestName", "%" + deliveryDestName + "%");
        }

        if (StringUtils.isNotEmpty(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }

        if (formatShipDateFrom != null) {
            query.setParameter("formatShipDateFrom", formatShipDateFrom);
        }

        if (formatShipDateTo != null) {
            query.setParameter("formatShipDateTo", formatShipDateTo);
        }

        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();

        return list;

    }

    /**
     * 納品先、発注番号、アイテムナンバーでPOテーブルを検索し、すでに存在ずれば受注日、受注数量、納期は表示する。
     *
     * @param orderNumber
     * @param deliveryDestId
     * @param itemNumber
     * @return
     */
    public TblPoVo load(String orderNumber, String deliveryDestId, String itemNumber) {
        TblPoVo tblPoVo = null;
        Query query = entityManager.createNamedQuery("TblPo.findByUniqueKey");
        query.setParameter("orderNumber", orderNumber);
        query.setParameter("deliveryDestId", deliveryDestId);
        query.setParameter("itemNumber", itemNumber);
        try {
            TblPo tblPo = (TblPo) query.getSingleResult();
            tblPoVo = new TblPoVo();
            tblPoVo.setUuid(tblPo.getUuid());
            tblPoVo.setItemNumber(tblPo.getItemNumber());
            FileUtil fileUtil = new FileUtil();
            tblPoVo.setOrderDate(fileUtil.getDateFormatForStr(tblPo.getOrderDate()));
            tblPoVo.setOrderNumber(FileUtil.getStringValue(tblPo.getOrderNumber()));
            tblPoVo.setQuantity(tblPo.getQuantity());
            tblPoVo.setComponentId(tblPo.getComponentId());
            tblPoVo.setComponentCode(tblPo.getMstComponent().getComponentCode());
            tblPoVo.setDeliveryDestId(tblPo.getDeliveryDestId());
            tblPoVo.setDueDate(fileUtil.getDateFormatForStr(tblPo.getDueDate()));
            tblPoVo.setCreateDate(tblPo.getCreateDate());
            tblPoVo.setCreateUserUuid(tblPo.getCreateUserUuid());

        } catch (NoResultException e) {
        }

        return tblPoVo;
    }
    
}
