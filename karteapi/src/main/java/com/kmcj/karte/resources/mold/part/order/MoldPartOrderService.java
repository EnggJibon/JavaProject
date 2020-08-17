/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.order;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.mold.part.order.excel.MoldPartOrderExcelService;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStockService;
import com.kmcj.karte.util.IDGenerator;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MoldPartOrderService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    private MoldPartStockService moldPartStockService;
    @Inject
    private MoldPartOrderExcelService moldPartOrderExcelService;
    
    public MoldPartOrderList getMoldPartOrders(
        int department,
        String moldId,
        String moldPartCode,
        String storageCode,
        String orderJobNo,
        boolean orderedByMe,
        int receivedFlg,
        LoginUser loginUser
    ) {
        MoldPartOrderList list = new MoldPartOrderList();
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("SELECT m FROM MoldPartOrder m WHERE m.receivedFlg = :receivedFlg ");

        if (department != 0) {
            sbQuery.append(" AND m.moldPartStock.mold.department = :department ");
        }
        if (moldId != null && !moldId.equals("")) {
            sbQuery.append(" AND m.moldPartStock.mold.moldId LIKE :moldId ");
        }
        if (moldPartCode != null && !moldPartCode.equals("")) {
            sbQuery.append(" AND m.moldPartStock.moldPart.moldPartCode LIKE :moldPartCode ");
        }
        if (storageCode != null && !storageCode.equals("")) {
            sbQuery.append(" AND m.moldPartStock.storageCode LIKE :storageCode ");
        }
        if (orderJobNo != null && !orderJobNo.equals("")) {
            sbQuery.append(" AND m.orderJobNo LIKE :orderJobNo ");
        }
        if (orderedByMe && loginUser != null) {
            sbQuery.append(" AND m.orderUserUuid = :orderUserUuid ");
        }
        sbQuery.append(" ORDER BY m.orderJobNo, m.moldPartStock.mold.moldId, m.moldPartStock.moldPart.moldPartCode");
        Query query = entityManager.createQuery(sbQuery.toString());
        query .setParameter("receivedFlg", receivedFlg);
        if (department != 0) {
            query.setParameter("department", department);
        }
        if (moldId != null && !moldId.equals("")) {
            query.setParameter("moldId", "%" + moldId + "%");
        }
        if (moldPartCode != null && !moldPartCode.equals("")) {
            query.setParameter("moldPartCode", "%" + moldPartCode + "%");
        }
        if (storageCode != null && !storageCode.equals("")) {
            query.setParameter("storageCode", "%" + storageCode + "%");
        }
        if (orderJobNo != null && !orderJobNo.equals("")) {
            query.setParameter("orderJobNo", "%" + orderJobNo + "%");
        }
        if (orderedByMe && loginUser != null) {
            query.setParameter("orderUserUuid", loginUser.getUserUuid());
        }

        List<MoldPartOrder> result = query.getResultList();
        list.setMoldPartOrders(result);
        return list;
    }
    
    public MoldPartOrder getMoldPartOrderById(String id) {
        return entityManager.createNamedQuery("MoldPartOrder.findById", MoldPartOrder.class).
                setParameter("id", id).getResultList().stream().findFirst().orElse(null);
    }

    @Transactional
    public BasicResponse upsertMoldPartOrders(MoldPartOrderList moldPartOrderList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        for (MoldPartOrder order : moldPartOrderList.getMoldPartOrders()) {
            if (order.getId() == null || order.getId().equals("")) {
                insertMoldPartOrder(order, loginUser);
            }
            else {
                updateMoldPartOrder(order, loginUser);
            }
        }
        return response;
    }
    
    @Transactional
    private void insertMoldPartOrder(MoldPartOrder moldPartOrder, LoginUser loginUser) {
        java.util.Date currentDate = new java.util.Date();
        MoldPartOrder newOrder = new MoldPartOrder();
        newOrder.setId(IDGenerator.generate());
        MoldPartStock stock = moldPartStockService.getMoldPartStockWithLock(moldPartOrder.getMoldPartStockId());
        if (stock == null) return;
        newOrder.setMoldPartStockId(moldPartOrder.getMoldPartStockId());
        newOrder.setOrderJobNo(moldPartOrder.getOrderJobNo());
        newOrder.setOrderUserUuid(loginUser.getUserUuid());
        newOrder.setStockAtOrder(stock.getStock());
        newOrder.setUsedStockAtOrder(stock.getUsedStock());
        newOrder.setCreateDate(currentDate);
        newOrder.setCreateUserUuid(loginUser.getUserUuid());
        newOrder.setUpdateDate(currentDate);
        newOrder.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(newOrder);
        updateStockStatus(stock, loginUser);
    }
    
    @Transactional
    private void updateStockStatus(MoldPartStock stock, LoginUser loginUser) {
        List<MoldPartOrder> orderList = getOrdersForJobNoUpdate(stock.getId());
        StringBuilder sbJobNo = new StringBuilder();
        for (int i = 0; i < orderList.size(); i++) {
            MoldPartOrder order = orderList.get(i);
            if (i > 0) {
                sbJobNo.append("," + order.getOrderJobNo());
            }
            else {
                sbJobNo.append(order.getOrderJobNo());
            }
        }
        if (orderList.size() > 0) {
            //納品待ちあればステータスを納品待ちにし発注工事番号を更新
            stock.setStatus(MoldPartStock.Status.DELI_WT);
            stock.setOrderJobNo(sbJobNo.toString());
        }
        else {
            //納品待ちがなければ発注工事番号をNULLにしてステータス判定(通常 or 要発注)
            stock.setOrderJobNo(null);
            stock.setStatus(MoldPartStock.Status.NORMAL); //通常にしてから判定メソッドで判定させる
            moldPartStockService.updateMoldPartStockStatus(stock); //ステータス判定メソッド
        }
        stock.setUpdateDate(new java.util.Date());
        stock.setUpdateUserUuid(loginUser.getUserUuid());
    }
    
    private List<MoldPartOrder> getOrdersForJobNoUpdate(String moldPartStockId) {
        String sql = "SELECT m FROM MoldPartOrder m WHERE m.moldPartStockId = :moldPartStockId AND m.receivedFlg = 0 ORDER BY m.orderJobNo ";
        Query query = entityManager.createQuery(sql);
        query.setParameter("moldPartStockId", moldPartStockId);
        return query.getResultList();
    }
    
    @Transactional
    private void updateMoldPartOrder(MoldPartOrder moldPartOrder, LoginUser loginUser) {
        MoldPartOrder updatingOrder = getMoldPartOrderById(moldPartOrder.getId());
        if (updatingOrder == null) return;
        updatingOrder.setOrderJobNo(moldPartOrder.getOrderJobNo());
        updatingOrder.setOrderUserUuid(loginUser.getUserUuid());
        updatingOrder.setUpdateUserUuid(loginUser.getUserUuid());
        updatingOrder.setUpdateDate(new java.util.Date());
        MoldPartStock stock = moldPartStockService.getMoldPartStockWithLock(moldPartOrder.getMoldPartStockId());
        if (stock == null) return;
        updateStockStatus(stock, loginUser);
    }
    
    
    @Transactional
    public BasicResponse deleteMoldPartOrders(MoldPartOrderList moldPartOrderList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        for (MoldPartOrder order : moldPartOrderList.getMoldPartOrders()) {
            if (order.getId() == null || order.getId().equals("")) {
                continue;
            }
            else {
                deleteOrder(order, loginUser);
            }
        }
        return response;
        
    }
    
    @Transactional
    private void deleteOrder(MoldPartOrder order, LoginUser loginUser) {
        MoldPartOrder deletingOrder = getMoldPartOrderById(order.getId());
        if (deletingOrder == null) return;
        String moldPartStockId = deletingOrder.getMoldPartStockId();
        entityManager.remove(deletingOrder);
        MoldPartStock stock = moldPartStockService.getMoldPartStockWithLock(moldPartStockId);
        if (stock == null) return;
        updateStockStatus(stock, loginUser);
    }
    
    public Workbook createMoldPartOrderExcel(
        int department,
        String moldId,
        String moldPartCode,
        String storageCode,
        String orderJobNo,
        boolean orderedByMe,
        int receivedFlg,
        LoginUser loginUser
    ) {
        MoldPartOrderList list = getMoldPartOrders(department, moldId, moldPartCode, storageCode, orderJobNo, orderedByMe, receivedFlg, loginUser);
        return moldPartOrderExcelService.createMoldPartOrderExcel(list, loginUser);
    }
    
    public String getExcelFileName(LoginUser loginUser) {
        return moldPartOrderExcelService.getFileName(loginUser);
    }
    
}
