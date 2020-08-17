/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRel;
import com.kmcj.karte.util.DateFormat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author fumi
 */
@Dependent
public class TotalUpdater {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    List<MstMold> moldList = new ArrayList<>();         //今回更新したい差分のショット数と生産時間を格納
    List<MstMachine> machineList = new ArrayList<>();   //今回更新したい差分のショット数と生産時間を格納
    
    private MstMold findMoldFromDB(String moldUuid) {
        Query query = entityManager.createNamedQuery("MstMold.findByUuid");
        query.setParameter("uuid", moldUuid);
        try {
            return (MstMold)query.getSingleResult();
        }
        catch (NoResultException ne) {
            return null;
        }
    }

    private MstMachine findMachineFromDB(String machineUuid) {
        Query query = entityManager.createNamedQuery("MstMachine.findByUuid");
        query.setParameter("uuid", machineUuid);
        try {
            return (MstMachine)query.getSingleResult();
        }
        catch (NoResultException ne) {
            return null;
        }
    }
    
    
    public void addMoldCounts(String moldUuid, int shotCount, int producingMinutes) {
        MstMold mstMold = findMoldFromList(moldUuid);
        if (mstMold == null) {
            //mstMold = findMoldFromDB(moldUuid);
            //if (mstMold == null) return;
            mstMold = new MstMold();
            mstMold.setUuid(moldUuid);
            moldList.add(mstMold);
        }
        if (mstMold.getTotalShotCount() == null) {
            mstMold.setTotalShotCount(0);
        }
        if (mstMold.getAfterMainteTotalShotCount() == null) {
            mstMold.setAfterMainteTotalShotCount(0);
        }
        mstMold.setTotalShotCount(mstMold.getTotalShotCount() + shotCount);
        mstMold.setAfterMainteTotalShotCount(mstMold.getAfterMainteTotalShotCount() + shotCount); //0から開始するのでtotalShotCountと同じ
        if (mstMold.getTotalProducingTimeHour() == null) {
            mstMold.setTotalProducingTimeHour(BigDecimal.ZERO);
        }
        if (mstMold.getAfterMainteTotalProducingTimeHour() == null) {
            mstMold.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO); 
        }
        BigDecimal bdMinutes = new BigDecimal(String.valueOf(producingMinutes));
        BigDecimal bd60Minutes = new BigDecimal("60");
        BigDecimal bdHours = bdMinutes.divide(bd60Minutes, 1, BigDecimal.ROUND_HALF_UP);
        mstMold.setTotalProducingTimeHour(mstMold.getTotalProducingTimeHour().add(bdHours));
        mstMold.setAfterMainteTotalProducingTimeHour(mstMold.getAfterMainteTotalProducingTimeHour().add(bdHours)); //0から開始するのでtotalProducingTimeHourと同じ
    }

    public void addMachineCounts(String machineUuid, int shotCount, int producingMinutes) {
        MstMachine mstMachine = findMachineFromList(machineUuid);
        if (mstMachine == null) {
            //mstMachine = findMachineFromDB(machineUuid);
            //if (mstMachine == null) return;
            mstMachine = new MstMachine();
            mstMachine.setUuid(machineUuid);
            machineList.add(mstMachine);
        }
        if (mstMachine.getTotalShotCount() == null) {
            mstMachine.setTotalShotCount(0);
        }
        if (mstMachine.getAfterMainteTotalShotCount() == null) {
            mstMachine.setAfterMainteTotalShotCount(0);
        }
        mstMachine.setTotalShotCount(mstMachine.getTotalShotCount() + shotCount);
        mstMachine.setAfterMainteTotalShotCount(mstMachine.getAfterMainteTotalShotCount() + shotCount);

        BigDecimal bdMinutes = new BigDecimal(String.valueOf(producingMinutes));
        BigDecimal bd60Minutes = new BigDecimal("60");
        BigDecimal bdHours = bdMinutes.divide(bd60Minutes, 1, BigDecimal.ROUND_HALF_UP);
        if (mstMachine.getTotalProducingTimeHour() == null) {
            mstMachine.setTotalProducingTimeHour(BigDecimal.ZERO);
        }
        if (mstMachine.getAfterMainteTotalProducingTimeHour() == null) {
            mstMachine.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO);
        }
        mstMachine.setTotalProducingTimeHour(mstMachine.getTotalProducingTimeHour().add(bdHours));
        mstMachine.setAfterMainteTotalProducingTimeHour(mstMachine.getAfterMainteTotalProducingTimeHour().add(bdHours));
    }

    
    @Transactional
    public void update(Date lastProductionDate) {
        updateMstMolds(lastProductionDate);
        updateMstMachines(lastProductionDate);
    }
    
    @Transactional
    public void updateMstMolds(Date lastProductionDate) {
        String sql = 
            " UPDATE MstMold t SET t.totalShotCount = :totalShotCount, t.afterMainteTotalShotCount = :afterMainteTotalShotCount, " +
            " t.totalProducingTimeHour = :totalProducingTimeHour, t.afterMainteTotalProducingTimeHour = :afterMainteTotalProducingTimeHour, " +
            " t.lastProductionDate = :lastProductionDate " +
            " WHERE t.uuid = :uuid ";
        for (MstMold mstMold : moldList) {
            MstMold currentRecord = findMoldFromDB(mstMold.getUuid());
            if (currentRecord == null) continue;
            Query query = entityManager.createQuery(sql);
            query.setParameter("totalShotCount", mstMold.getTotalShotCount() + 
                    (currentRecord.getTotalShotCount() == null ? 0 : currentRecord.getTotalShotCount()));
            query.setParameter("afterMainteTotalShotCount", mstMold.getAfterMainteTotalShotCount() + 
                    (currentRecord.getAfterMainteTotalShotCount() == null ? 0 : currentRecord.getAfterMainteTotalShotCount()));
            query.setParameter("totalProducingTimeHour", mstMold.getTotalProducingTimeHour().add(
                currentRecord.getTotalProducingTimeHour() == null ? BigDecimal.ZERO : currentRecord.getTotalProducingTimeHour()));
            query.setParameter("afterMainteTotalProducingTimeHour", mstMold.getAfterMainteTotalProducingTimeHour().add(
                currentRecord.getAfterMainteTotalProducingTimeHour() == null ? BigDecimal.ZERO : currentRecord.getAfterMainteTotalProducingTimeHour()));
            query.setParameter("uuid", mstMold.getUuid());
            Date pLastProductionDate = lastProductionDate;
            if (pLastProductionDate == null) {
                pLastProductionDate = getMoldLastProductionDate(mstMold.getUuid());
            }
            query.setParameter("lastProductionDate", pLastProductionDate);
            query.executeUpdate();
            updateMoldParts(mstMold);
        }
    }
    
    @Transactional
    private void updateMoldParts(MstMold mold) {
       List<MstMoldPartRel> moldParts = findMoldPartsFromDB(mold.getUuid());
       if (moldParts == null || moldParts.size() <= 0) return;
       String sql = "UPDATE MstMoldPartRel m SET m.aftRplShotCnt = :aftRplShotCnt, m.aftRplProdTimeHour = :aftRplProdTimeHour, " +
            " m.aftRprShotCnt = :aftRprShotCnt, m.aftRprProdTimeHour = :aftRprProdTimeHour WHERE m.id = :id";
       for (MstMoldPartRel moldPart: moldParts) {
           Query query = entityManager.createQuery(sql);
           query.setParameter("aftRplShotCnt", moldPart.getAftRplShotCnt() + mold.getTotalShotCount());
           query.setParameter("aftRplProdTimeHour", moldPart.getAftRplProdTimeHour().add(mold.getTotalProducingTimeHour()));
           query.setParameter("aftRprShotCnt", moldPart.getAftRprShotCnt() + mold.getTotalShotCount());
           query.setParameter("aftRprProdTimeHour", moldPart.getAftRprProdTimeHour().add(mold.getTotalProducingTimeHour()));
           query.setParameter("id", moldPart.getId());
           query.executeUpdate();
       }
    }
    
    private List<MstMoldPartRel> findMoldPartsFromDB(String uuid) {
        String sql = "SELECT m FROM MstMoldPartRel m WHERE m.moldUuid = :uuid";
        Query query = entityManager.createQuery(sql);
        query.setParameter("uuid", uuid);
        return query.getResultList();
    }
    
    private Date getMoldLastProductionDate(String uuid) {
        Date lastDate = null;
        Query query = entityManager.createQuery(
            "SELECT MAX(t.report.reportDate) FROM TblMachineDailyReport2Detail t WHERE t.moldUuid = :uuid AND t.detailType = 2 ");
        query.setParameter("uuid", uuid);
        List<Object> result = query.getResultList();
        if (result.size() > 0) {
            lastDate = (Date)(result.get(0));
        }
        if (lastDate == null) {
            //機械日報2に見つからなければ生産実績から探す
            query = entityManager.createQuery("SELECT MAX(t.endDatetime) FROM Production t WHERE t.moldUuid = :uuid");
            query.setParameter("uuid", uuid);
            result = query.getResultList();
            if (result.size() > 0) {
                lastDate = (Date)(result.get(0));
            }
        }
        //時分秒切り捨て
        if (lastDate != null) {
            lastDate = DateFormat.strToDate(DateFormat.dateToStr(lastDate, DateFormat.DATE_FORMAT));
        }
        return lastDate;
    }

    @Transactional
    public void updateMstMachines(Date lastProductionDate) {
        String sql = 
            " UPDATE MstMachine t SET t.totalShotCount = :totalShotCount, t.afterMainteTotalShotCount = :afterMainteTotalShotCount, " +
            " t.totalProducingTimeHour = :totalProducingTimeHour, t.afterMainteTotalProducingTimeHour = :afterMainteTotalProducingTimeHour, " +
            " t.lastProductionDate = :lastProductionDate " +
            " WHERE t.uuid = :uuid ";
        for (MstMachine mstMachine : machineList) {
            MstMachine currentRecord = findMachineFromDB(mstMachine.getUuid());
            if (currentRecord == null) continue;
            Query query = entityManager.createQuery(sql);
            query.setParameter("totalShotCount", mstMachine.getTotalShotCount() +
                    (currentRecord.getTotalShotCount() == null ? 0 : currentRecord.getTotalShotCount()));
            query.setParameter("afterMainteTotalShotCount", mstMachine.getAfterMainteTotalShotCount() +
                    (currentRecord.getAfterMainteTotalShotCount() == null ? 0 : currentRecord.getAfterMainteTotalShotCount()));
            query.setParameter("totalProducingTimeHour", mstMachine.getTotalProducingTimeHour().add(
                currentRecord.getTotalProducingTimeHour() == null ? BigDecimal.ZERO : currentRecord.getTotalProducingTimeHour()));
            query.setParameter("afterMainteTotalProducingTimeHour", mstMachine.getAfterMainteTotalProducingTimeHour().add(
                currentRecord.getAfterMainteTotalProducingTimeHour() == null ? BigDecimal.ZERO : currentRecord.getAfterMainteTotalProducingTimeHour()));
            query.setParameter("uuid", mstMachine.getUuid());
            Date pLastProductionDate = lastProductionDate;
            if (pLastProductionDate == null) {
                pLastProductionDate = getMachineLastProductionDate(mstMachine.getUuid());
            }
            query.setParameter("lastProductionDate", pLastProductionDate);
            query.executeUpdate();
        }
    }
    
    private Date getMachineLastProductionDate(String uuid) {
        Date lastDate = null;
        Query query = entityManager.createQuery(
            "SELECT MAX(t.reportDate) FROM TblMachineDailyReport2 t WHERE t.machineUuid = :uuid AND t.totalShotCount > 0 ");
        query.setParameter("uuid", uuid);
        List<Object> result = query.getResultList();
        if (result.size() > 0) {
            lastDate = (Date)(result.get(0));
        }
        if (lastDate == null) {
            //機械日報2に見つからなければ生産実績から探す
            query = entityManager.createQuery("SELECT MAX(t.endDatetime) FROM Production t WHERE t.machineUuid = :uuid");
            query.setParameter("uuid", uuid);
            result = query.getResultList();
            if (result.size() > 0) {
                lastDate = (Date)(result.get(0));
            }
        }
        //時分秒切り捨て
        if (lastDate != null) {
            lastDate = DateFormat.strToDate(DateFormat.dateToStr(lastDate, DateFormat.DATE_FORMAT));
        }
        return lastDate;
    }

    private MstMold findMoldFromList(String uuid) {
        for (MstMold mstMold: moldList) {
            if (mstMold.getUuid().equals(uuid)) return mstMold;
        }
        return null;
    }

    private MstMachine findMachineFromList(String uuid) {
        for (MstMachine mstMachine: machineList) {
            if (mstMachine.getUuid().equals(uuid)) return mstMachine;
        }
        return null;
    }
}
