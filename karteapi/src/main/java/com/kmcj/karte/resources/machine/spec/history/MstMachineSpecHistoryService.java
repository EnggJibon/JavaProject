/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.spec.history;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author admin
 */
@Dependent
//@Transactional
public class MstMachineSpecHistoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     *
     * @param machineUuid
     * @return
     */
    public MstMachineSpecHistoryList getMachineByMachineId(String machineUuid) {
        Query query = entityManager.createNamedQuery("MstMachineSpecHistory.findByMachineUuid");
        query.setParameter("machineUuid", machineUuid);
        List list = query.getResultList();
        MstMachineSpecHistoryList response = new MstMachineSpecHistoryList();
        response.setMstMachineSpecHistorys(list);
        return response;
    }

    /**
     *
     * @param machineUuid
     * @return
     */
    public MstMachineSpecHistoryVo getMachineSpecHistoryNamesByMachineUuid(String machineUuid) {
        Query query = entityManager.createQuery("SELECT DISTINCT h from MstMachineSpecHistory h join h.mstMachine m where m.uuid = :machineUuid");
        query.setParameter("machineUuid", machineUuid);
        List<MstMachineSpecHistory> list = query.getResultList();
        List<MstMachineSpecHistoryVo> resList = new ArrayList<MstMachineSpecHistoryVo>();
        MstMachineSpecHistoryVo response = new MstMachineSpecHistoryVo();
        if (list.isEmpty() == false) {
            response.setMachineUuid(machineUuid);
            response.setId(list.get(0).getId());
            response.setMachineSpecName(list.get(0).getMachineSpecName());

            resList.add(response);
        }
        return response;
    }

    /**
     * 最新の設備仕様履歴を取得する
     *
     * @param machineUuid
     * @return
     */
    public List getMstMachineSpecHistoryByIdFromMachineCsv(String machineUuid) {
        Query query = entityManager.createNamedQuery("MstMachineSpecHistory.findByIdForNewEndDate");
        query.setParameter("machineUuid", machineUuid);
        List list = query.getResultList();
        return list;
    }

    public MstMachineSpecHistory getMstMachineSpecHistoryLatestByMachineUuidFromMachineCsv(String machineUuid) {
        Query query = entityManager.createQuery("SELECT m FROM MstMachineSpecHistory m WHERE m.mstMachine.uuid = :machineUuid and m.endDate = :endDate ");
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("endDate", CommonConstants.SYS_MAX_DATE);
        query.setMaxResults(1);
        List<MstMachineSpecHistory> list = query.getResultList();
        return null == list || list.isEmpty() ? null : list.get(0);
    }

    /**
     *
     * @param machineUuid
     * @return
     */
    @Transactional
    public int deleteMstMachineSpecHistory(String machineUuid) {
        //DELETE FROM MstMoldSpecHistory m WHERE m.mstMold.uuid = :moldUuid
        StringBuilder sql = new StringBuilder("DELETE FROM MstMachineSpecHistory m WHERE m.mstMachine.uuid = :machineUuid");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("machineUuid", machineUuid);
        return query.executeUpdate();
    }

    /**
     *
     * @param mstMachineSpecHistory
     */
    @Transactional
    public void createMstMachineSpecHistoryByCsv(MstMachineSpecHistory mstMachineSpecHistory) {
        entityManager.persist(mstMachineSpecHistory);
    }

    /**
     * T0004_新仕様登録画面_画面描画時
     *
     * @param machineId
     * @return
     */
    public MstMachineSpecHistory getMachineSpecHistoryList(String machineId) {

        StringBuilder sql = new StringBuilder(" SELECT m FROM MstMachineSpecHistory m WHERE 1=1 ");
        if (machineId != null && !"".equals(machineId)) {
            sql.append(" And m.mstMachine.machineId = :machineId ");
        }
        sql.append(" ORDER BY m.endDate DESC ");

        Query query = entityManager.createQuery(sql.toString());

        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", machineId);
        }

        List list = query.getResultList();
        MstMachineSpecHistory response = (MstMachineSpecHistory) list.get(0);
        return response;
    }

    /**
     * バッチで設備仕様履歴マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @param machineUuid
     * @return
     */
    public MstMachineSpecHistoryList getExtMachineSpecHistorysByBatch(String latestExecutedDate, String machineUuid) {
        MstMachineSpecHistoryList resList = new MstMachineSpecHistoryList();
        List<MstMachineSpecHistoryVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMachineSpecHistory t join MstApiUser u on u.companyId = t.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.machineUuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }

        List<MstMachineSpecHistory> tmpList = query.getResultList();
        for (MstMachineSpecHistory mstMachineSpecHistory : tmpList) {
            MstMachineSpecHistoryVo aVo = new MstMachineSpecHistoryVo();
            aVo.setMachineId(mstMachineSpecHistory.getMstMachine().getMachineId());
            aVo.setEndDateStr(new FileUtil().getDateFormatForStr(mstMachineSpecHistory.getEndDate()));//todo 字符串
            aVo.setEndDateHs(mstMachineSpecHistory.getEndDate().getTime() / 1000);//todo 毫秒
            mstMachineSpecHistory.setMstMachine(null);
            aVo.setMstMachineSpecHistory(mstMachineSpecHistory);
            resVo.add(aVo);
        }
        resList.setMstMachineSpecHistoryVos(resVo);
        return resList;
    }

    /**
     * バッチで設備仕様履歴マスタデータを更新
     *
     * @param machineSpecHistorys
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineSpecHistorysByBatch(List<MstMachineSpecHistoryVo> machineSpecHistorys) {
        BasicResponse response = new BasicResponse();

        if (machineSpecHistorys != null && !machineSpecHistorys.isEmpty()) {

            for (MstMachineSpecHistoryVo aMachineSpecHistoryVo : machineSpecHistorys) {
                MstMachine ownerMachine = entityManager.find(MstMachine.class, aMachineSpecHistoryVo.getMachineId());
                List<MstMachineSpecHistory> oldHistorys = entityManager.createQuery("SELECT t FROM MstMachineSpecHistory t WHERE t.id = :id ")
                        .setParameter("id", aMachineSpecHistoryVo.getMstMachineSpecHistory().getId())
                        .setMaxResults(1)
                        .getResultList();

                MstMachineSpecHistory machineSpecHistory;
                if (null != oldHistorys && !oldHistorys.isEmpty()) {
                    machineSpecHistory = oldHistorys.get(0);
                } else {
                    machineSpecHistory = new MstMachineSpecHistory();
                    machineSpecHistory.setId(aMachineSpecHistoryVo.getMstMachineSpecHistory().getId());
                }
                //自社の設備UUIDに変換

                if (null != ownerMachine) {
                    machineSpecHistory.setMachineUuid(ownerMachine.getUuid());
                    machineSpecHistory.setStartDate(aMachineSpecHistoryVo.getMstMachineSpecHistory().getStartDate());
                    machineSpecHistory.setEndDate(aMachineSpecHistoryVo.getMstMachineSpecHistory().getEndDate());
                    machineSpecHistory.setMachineSpecName(aMachineSpecHistoryVo.getMstMachineSpecHistory().getMachineSpecName());

                    machineSpecHistory.setCreateDate(aMachineSpecHistoryVo.getMstMachineSpecHistory().getCreateDate());
                    machineSpecHistory.setCreateUserUuid(aMachineSpecHistoryVo.getMstMachineSpecHistory().getCreateUserUuid());
                    machineSpecHistory.setUpdateDate(new Date());
                    machineSpecHistory.setUpdateUserUuid(aMachineSpecHistoryVo.getMstMachineSpecHistory().getUpdateUserUuid());

                    if (null != oldHistorys && !oldHistorys.isEmpty()) {
                        entityManager.merge(machineSpecHistory);//更新
                    } else {
                        List<MstMachineSpecHistory> lasMstMachineSpecHistorys = entityManager.createQuery("SELECT h from MstMachineSpecHistory h where h.machineUuid = :machineUuid ORDER BY h.endDate DESC ")
                                .setParameter("machineUuid", ownerMachine.getUuid()).setMaxResults(1).getResultList();
                        if (null != lasMstMachineSpecHistorys && !lasMstMachineSpecHistorys.isEmpty()) {
                            lasMstMachineSpecHistorys.get(0).setEndDate(new FileUtil().getSpecifiedDayBefore(machineSpecHistory.getStartDate()));
                            entityManager.merge(lasMstMachineSpecHistorys.get(0));
                        }

                        entityManager.persist(machineSpecHistory);
                    }
                }
            }
        }
        response.setError(false);
        return response;
    }

    /**
     * 外部データ getAllId4Check
     *
     * @return
     */
    public List<String> getAllIdList() {
        return entityManager.createQuery("select m.id from MstMachineSpecHistory m").getResultList();
    }
}
