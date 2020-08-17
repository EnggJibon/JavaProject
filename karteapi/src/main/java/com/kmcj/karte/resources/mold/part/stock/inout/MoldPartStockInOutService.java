/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock.inout;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.mold.part.stock.maint.MoldMaintenance;
import com.kmcj.karte.resources.mold.part.stock.maint.MoldMaintenanceDetail;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStockService;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MoldPartStockInOutService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    MoldPartStockService moldPartStockService;
    @Inject
    MstChoiceService choiceService;
    
    public MoldPartStockInOutList getMoldPartStockInOutListForPageView(
            String moldPartStockId, int pageNumber, int pageSize, LoginUser loginUser) {
        MoldPartStockInOutList list = new MoldPartStockInOutList();
        Query countQuery = makeQuery(true, moldPartStockId, 0, 0);
        int recCount = ((Long)countQuery.getSingleResult()).intValue();
        list.setTotalCount(recCount);
        Query recordQuery = makeQuery(false, moldPartStockId, pageNumber, pageSize);
        List<MoldPartStockInOut> resultList = recordQuery.getResultList();
        setMaintenanceReason(resultList, loginUser);
        list.setMoldPartStockInOutList(resultList);
        return list;
    }
    
    private void setMaintenanceReason(List<MoldPartStockInOut> moldPartStockInOutList, LoginUser loginUser) {
        for (MoldPartStockInOut io : moldPartStockInOutList) {
            if (io.getMoldMaintId() != null && !io.getMoldMaintId().equals("")) {
                MoldMaintenance maintenance = io.getMoldMaintenance();
                if (maintenance == null || maintenance.getMoldMaintenanceDetails() == null || maintenance.getMoldMaintenanceDetails().size() <= 0) continue;
                MoldMaintenanceDetail detail = maintenance.getMoldMaintenanceDetails().get(0);
                if (detail == null) continue;
                String maintenanceReason = null;
                if (detail.getMainteReasonCategory1() == null) continue;
                MstChoice choice = choiceService.getBySeqChoice(detail.getMainteReasonCategory1().toString(), loginUser.getLangId(), "tbl_mold_maintenance_detail.mainte_reason_category1");
                if (choice != null) {
                    maintenanceReason = choice.getChoice();
                }
                maintenance.setMaintenanceReason(maintenanceReason);
            }
        }
    }

    private Query makeQuery(
            boolean countQuery, String moldPartStockId, int pageNumber, int pageSize)
    {
        StringBuilder sbQuery = new StringBuilder();
        if (countQuery) {
            sbQuery.append("SELECT count(io) FROM MoldPartStockInOut io WHERE io.moldPartStockId = :moldPartStockId ");
        }
        else {
            sbQuery.append("SELECT io FROM MoldPartStockInOut io WHERE io.moldPartStockId = :moldPartStockId ORDER BY io.ioDate DESC ");
        }
        Query query = entityManager.createQuery(sbQuery.toString());
        query.setParameter("moldPartStockId", moldPartStockId);
        if (!countQuery && pageNumber > 0 && pageSize > 0) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        return query;
    }
        
    @Transactional
    public BasicResponse adjustMoldPartStockInOut(MoldPartStockInOut moldPartStockIO, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        Date currentDate = new java.util.Date();
        MoldPartStockInOut io = new MoldPartStockInOut();
        io.setMoldPartStockId(moldPartStockIO.getMoldPartStockId());
        io.setIoDate(moldPartStockIO.getIoDate());
        io.setNewStockIo(moldPartStockIO.getNewStockIo());
        io.setUsedStockIo(moldPartStockIO.getUsedStockIo());
        io.setId(IDGenerator.generate());
        io.setIoEvent(MoldPartStockInOut.IoEvent.ADJUST);
        io.setAdjustReason(moldPartStockIO.getAdjustReason());
        io.setCreateDate(currentDate);
        io.setCreateUserUuid(loginUser.getUserUuid());
        io.setUpdateDate(currentDate);
        io.setUpdateUserUuid(loginUser.getUserUuid());
        //ロックをかけて在庫テーブルを取得し、在庫を更新
        MoldPartStock stock = moldPartStockService.getMoldPartStockWithLock(io.getMoldPartStockId());
        io.setStock(stock.getStock() + io.getNewStockIo());
        io.setUsedStock(stock.getUsedStock() + io.getUsedStockIo());
        stock.setStock(io.getStock());
        stock.setUsedStock(io.getUsedStock());
        moldPartStockService.updateMoldPartStockStatus(stock);
        stock.setUpdateDate(currentDate);
        stock.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(io);
        return response;
    }
    
    public List<MoldPartStockInOut> getListFromMaintenanceId(String maintenanceId) {
        Query query = entityManager.createNamedQuery("MoldPartStockInOut.findByMaintId");
        query.setParameter("moldMaintId", maintenanceId);
        return query.getResultList();
    }
    
    /**
     * メンテナンスが削除、開始取消されたときの在庫調整
     * @param maintenanceId 
     * @param loginUser 
     */
    @Transactional
    public void cancelMaintenance(String maintenanceId, LoginUser loginUser) {
        Date currentDate = new java.util.Date();
        //入出庫履歴からそのメンテナンスで作られたレコードを取得
        List<MoldPartStockInOut> ioList = this.getListFromMaintenanceId(maintenanceId);
        for (MoldPartStockInOut inOut: ioList) {
            MoldPartStock stock = moldPartStockService.getMoldPartStockWithLock(inOut.getMoldPartStockId()); //排他制御あり
            //取消日付を設定
            inOut.setCanceledDate(currentDate);
            inOut.setUpdateUserUuid(loginUser.getUserUuid());
            inOut.setUpdateDate(currentDate);
            int updateStock = stock.getStock() - inOut.getNewStockIo(); //新品在庫を戻す
            int updateUsedStock = stock.getUsedStock() - inOut.getUsedStockIo(); //中古在庫を戻す
            //在庫テーブルの在庫を更新
            stock.setStock(updateStock);
            stock.setUsedStock(updateUsedStock);
            stock.setUpdateUserUuid(loginUser.getUserUuid());
            stock.setUpdateDate(currentDate);
            moldPartStockService.updateMoldPartStockStatus(stock); //ステータスの判定
            //符号反転した取消用入出庫履歴を作る
            MoldPartStockInOut correctIo = new MoldPartStockInOut();
            correctIo.setId(IDGenerator.generate());
            correctIo.setMoldPartStockId(stock.getId());
            correctIo.setIoDate(currentDate);
            correctIo.setIoEvent(MoldPartStockInOut.IoEvent.EXCHANGE);
            correctIo.setNewStockIo(inOut.getNewStockIo() * -1); //新品増減符号反転
            correctIo.setUsedStockIo(inOut.getUsedStockIo() * -1); //中古増減符号反転
            correctIo.setStock(updateStock);
            correctIo.setUsedStock(updateUsedStock);
            correctIo.setCanceledSourceId(inOut.getId()); //取消元のIDを保持
            correctIo.setCreateDate(currentDate);
            correctIo.setUpdateDate(currentDate);
            correctIo.setCreateUserUuid(loginUser.getUserUuid());
            correctIo.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(correctIo);
        }
    }
    
}
