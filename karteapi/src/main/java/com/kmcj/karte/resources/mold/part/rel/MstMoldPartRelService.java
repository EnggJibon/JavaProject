/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.rel;

import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintenancePart;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
import java.util.ArrayList;
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
public class MstMoldPartRelService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private CnfSystemService cnfSystemService;

    long succeededCount = 0;
    long addedCount = 0;
    long updatedCount = 0;
    long failedCount = 0;
    long deletedCount = 0;

    public MstMoldPartRelList getMstMoldPartRels() {
        Query query = entityManager.createNamedQuery("MstMoldPartRel.findAll");
        List list = query.getResultList();
        MstMoldPartRelList response = new MstMoldPartRelList();
        response.setMstMoldPartRels(list);
        return response;
    }
    /**
     * Mold part relation master FK dependency check
     *
     * @param id
     * @return
     */
    public boolean getMstMoldPartRelFKCheck(String moldUuid, String location, String moldPartId) {
        Query query = entityManager.createNamedQuery("MstMoldPartRel.findByItem");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("location", location);
        query.setParameter("moldPartId", moldPartId);
        boolean flg = false;

        MstMoldPartRel mstMoldPartRel = (MstMoldPartRel) query.getSingleResult();
        String moldPartRelId = mstMoldPartRel.getId();
//            mst_mold_maintenance_part NO ACTION ok
        if (!flg) {
            Query queryMstMoldMaintenancePart = entityManager.createNamedQuery("MstMoldMaintenancePart.findByMoldPartRelId");
            queryMstMoldMaintenancePart.setParameter("id", moldPartRelId);
            flg = queryMstMoldMaintenancePart.getResultList().size() > 0;
        }

        if (!flg) {
//            mst_mold_part_maintenance_recommend	NO ACTION ok
            Query queryMstMoldPartMaintenanceRecommend = entityManager.createNamedQuery("MstMoldPartMaintenanceRecommend.findByMoldPartRelId");
            queryMstMoldPartMaintenanceRecommend.setParameter("id", moldPartRelId);
            flg = queryMstMoldPartMaintenanceRecommend.getResultList().size() > 0;
        }

        return flg;
    }

    /**
     * 
     * @param moldUuid
     * @param location
     * @param moldPartId
     * @return
     */
    public boolean getMstMoldPartRelExistCheck(String moldUuid, String location, String moldPartId) {
        Query query = entityManager.createNamedQuery("MstMoldPartRel.findByKey");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("location", location);
        query.setParameter("moldPartId", moldPartId);
        return query.getResultList().size() > 0;
    }
    
    public MstMoldPartRel getMstMoldPartRelByKeys(String moldUuid, String location, String moldPartId) {
        Query query = entityManager.createNamedQuery("MstMoldPartRel.findByKey");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("location", location);
        query.setParameter("moldPartId", moldPartId);
        try {
            return (MstMoldPartRel) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param moldUuid
     * @param location
     * @param moldPartId
     * @return
     */
    @Transactional
    public int deleteMstMoldPartRel(String moldUuid, String location, String moldPartId) {

        Query query = entityManager.createNamedQuery("MstMoldPartRel.delete");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("location", location);
        query.setParameter("moldPartId", moldPartId);
        return query.executeUpdate();
    }

    /**
     *
     * @param mstMoldPartRel
     */
    @Transactional
    public void createMstMoldPartRel(MstMoldPartRel mstMoldPartRel) {
        entityManager.persist(mstMoldPartRel);
    }

    /**
     *
     * @param mstMoldPartRel
     * @return
     */
    @Transactional
    public int updateMstMoldPartRelByQuery(MstMoldPartRel mstMoldPartRel) {
         Query query = entityManager.createNamedQuery("MstMoldPartRel.updateById");
        query.setParameter("moldUuid", mstMoldPartRel.getMoldUuid());
        query.setParameter("location", mstMoldPartRel.getLocation());
        query.setParameter("moldPartId", mstMoldPartRel.getMoldPartId());
        query.setParameter("quantity", mstMoldPartRel.getQuantity());
        query.setParameter("rplClShotCnt", mstMoldPartRel.getRplClShotCnt());
        query.setParameter("rplClProdTimeHour", mstMoldPartRel.getRplClProdTimeHour());
        query.setParameter("rplClLappsedDay", mstMoldPartRel.getRplClLappsedDay());
        query.setParameter("rprClShotCnt", mstMoldPartRel.getRprClShotCnt());
        query.setParameter("rprClProdTimeHour", mstMoldPartRel.getRprClProdTimeHour());
        query.setParameter("rprClLappsedDay", mstMoldPartRel.getRprClLappsedDay());
        query.setParameter("aftRplShotCnt", mstMoldPartRel.getAftRplShotCnt());
        query.setParameter("aftRplProdTimeHour", mstMoldPartRel.getAftRplProdTimeHour());
        query.setParameter("lastRplDatetime", mstMoldPartRel.getLastRplDatetime());
        query.setParameter("aftRprShotCnt", mstMoldPartRel.getAftRprShotCnt());
        query.setParameter("aftRprProdTimeHour", mstMoldPartRel.getAftRprProdTimeHour());
        query.setParameter("lastRprDatetime", mstMoldPartRel.getLastRprDatetime());
        query.setParameter("updateDate", mstMoldPartRel.getUpdateDate());
        query.setParameter("updateUserUuid", mstMoldPartRel.getUpdateUserUuid());
        int cnt = query.executeUpdate();
        return cnt;
    }
    /**
     *
     * @param id
     * @param loginUser
     * @return
     */
    public List<MstMoldPartDetailMaintenance> getMstMoldPartRelDetailByMaintId(String id, LoginUser loginUser){
        String sql = "SELECT r FROM TblMoldMaintenanceRemodeling r WHERE r.id = :id ";       

        Query query = entityManager.createQuery(sql);
        if (null != id && !id.isEmpty()) {
            query.setParameter("id", id);
        }
        TblMoldMaintenanceRemodeling mstMoldMaintenanceRemodeing = (TblMoldMaintenanceRemodeling) query.getSingleResult();
        Map<String, TblMoldMaintenancePart> moldParts = getMoldParts(id);
        List<MstMoldPartDetailMaintenance> ret = this.getMstMoldPartRelsByMoldUuid(mstMoldMaintenanceRemodeing.getMoldUuid(), loginUser);
        for(MstMoldPartDetailMaintenance detail : ret) {
            TblMoldMaintenancePart maintPart = moldParts.getOrDefault(detail.getMoldPartRelId(), null);
            detail.setMaintPart(maintPart);
            if(maintPart != null) {
                detail.setReplaceIsChecked(1 == maintPart.getReplaceOrRepair());
                detail.setRepairIsChecked(2 == maintPart.getReplaceOrRepair());
                detail.setPartialReplaceIsChecked(3 == maintPart.getReplaceOrRepair());
            }
        }
        return ret;
    }
    
    public List<MstMoldPartDetailMaintenance> getMstMoldPartRelsByMoldUuid(String moldUuid, LoginUser loginUser) {
        String sql = "SELECT r FROM MstMoldPartRel r JOIN FETCH r.mstMoldPart WHERE r.moldUuid = :moldUuid ORDER BY r.location";

        List<MstMoldPartRel> list = entityManager.createQuery(sql, MstMoldPartRel.class)
            .setParameter("moldUuid", moldUuid).getResultList();
        
        //金型部品在庫が無効な場合、在庫なし、リサイクル不可として扱う。
        boolean stockIsAvailable = cnfSystemService.findByKey("mold", "mold_part_stock_available").getConfigValue().equals("1");
        List<String> moldPartIds = list.stream().map(rel->rel.getMoldPartId()).collect(Collectors.toList());
        Map<String, List<MoldPartStock>> stocks = stockIsAvailable ? getStocksInMoldParts(moldPartIds) : new HashMap<>();
        
        List<MstMoldPartDetailMaintenance> mstMoldPartDetailList = new ArrayList();
        for (MstMoldPartRel moldPartRel : list) {
            MstMoldPartDetailMaintenance mstMoldPartDetail = new MstMoldPartDetailMaintenance();
            mstMoldPartDetail.setMoldId(moldPartRel.getMstMold().getMoldId());
            mstMoldPartDetail.setMoldPartRelId(moldPartRel.getId());
            mstMoldPartDetail.setLocation(moldPartRel.getLocation());
            mstMoldPartDetail.setQuantity(moldPartRel.getQuantity());
            mstMoldPartDetail.setRecyclableFlg(stockIsAvailable ? moldPartRel.getMstMoldPart().getRecyclableFlg() : 0);
            mstMoldPartDetail.setRplClShotCnt(moldPartRel.getRplClShotCnt());
            mstMoldPartDetail.setRplClProdTimeHour(moldPartRel.getRplClProdTimeHour());
            mstMoldPartDetail.setRplClLappsedDay(moldPartRel.getRplClLappsedDay());
            mstMoldPartDetail.setRprClShotCnt(moldPartRel.getRprClShotCnt());
            mstMoldPartDetail.setRprClProdTimeHour(moldPartRel.getRprClProdTimeHour());
            mstMoldPartDetail.setRprClLappsedDay(moldPartRel.getRprClLappsedDay());
            mstMoldPartDetail.setAftRplShotCnt(moldPartRel.getAftRplShotCnt());
            mstMoldPartDetail.setAftRplProdTimeHour(moldPartRel.getAftRplProdTimeHour());
            mstMoldPartDetail.setAftRprShotCnt(moldPartRel.getAftRprShotCnt());
            mstMoldPartDetail.setLastRplDatetime(moldPartRel.getLastRplDatetime());
            mstMoldPartDetail.setLastRprDatetime(moldPartRel.getLastRprDatetime());
            mstMoldPartDetail.setAftRprProdTimeHour(moldPartRel.getAftRprProdTimeHour());
            mstMoldPartDetail.setReplaceIsChecked(false);
            mstMoldPartDetail.setRepairIsChecked(false);
            if (null == moldPartRel.getMstMoldPart()) {
                mstMoldPartDetail.setMoldPartCode("");
            } else {
                mstMoldPartDetail.setMoldPartCode(moldPartRel.getMstMoldPart().getMoldPartCode());
            }
            mstMoldPartDetail.setStocks(stocks.getOrDefault(moldPartRel.getMoldPartId(), new ArrayList<>()));
            mstMoldPartDetailList.add(mstMoldPartDetail);
        }
        return mstMoldPartDetailList;
    }
    
    private Map<String, List<MoldPartStock>> getStocksInMoldParts(List<String> moldPartIds) {
        Map<String, List<MoldPartStock>> ret = new HashMap<>();
        if(moldPartIds.isEmpty()) {
            return ret;
        }
        List<MoldPartStock> list = entityManager.createNamedQuery("MoldPartStock.findByMoldPartIds", MoldPartStock.class)
            .setParameter("moldPartIds", moldPartIds).getResultList();
        for (MoldPartStock moldPartStock : list) {
            ret.putIfAbsent(moldPartStock.getMoldPartId(), new ArrayList<>());
            ret.get(moldPartStock.getMoldPartId()).add(moldPartStock);
        }
        return ret;
    }
    
    private Map<String, TblMoldMaintenancePart> getMoldParts(String mainteId) {
        Map<String, TblMoldMaintenancePart> ret = new HashMap<>();
        List<TblMoldMaintenancePart> list = entityManager.createNamedQuery("TblMoldMaintenancePart.findByMainte", TblMoldMaintenancePart.class)
            .setParameter("maintenanceId", mainteId).getResultList();
        for(TblMoldMaintenancePart mp : list) {
            ret.put(mp.getTblMoldMaintenancePartPK().getMoldPartRelId(), mp);
        }
        return ret;
    }
}
