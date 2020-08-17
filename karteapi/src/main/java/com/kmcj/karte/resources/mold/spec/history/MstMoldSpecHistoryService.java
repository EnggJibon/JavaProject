/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec.history;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.mold.MstMold;
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
public class MstMoldSpecHistoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     *
     * @param moldUuid
     * @return
     */
    public MstMoldSpecHistoryList getMoldByMoldId(String moldUuid) {
        Query query = entityManager.createNamedQuery("MstMoldSpecHistory.findByMoldUuid");
        query.setParameter("moldUuid", moldUuid);
        List list = query.getResultList();
        MstMoldSpecHistoryList response = new MstMoldSpecHistoryList();
        response.setMstMoldSpecHistory(list);
        return response;
    }

    /**
     *
     * @param moldUuid
     * @return
     */
    public MstMoldSpecHistorys getMoldSpecHistoryNamesByMoldUuid(String moldUuid) {
        Query query = entityManager.createQuery("SELECT DISTINCT h from MstMoldSpecHistory h join h.mstMold m where m.uuid = :moldUuid");
        query.setParameter("moldUuid", moldUuid);
        List<MstMoldSpecHistory> list = query.getResultList();
        List<MstMoldSpecHistorys> resList = new ArrayList<MstMoldSpecHistorys>();
        MstMoldSpecHistorys response = new MstMoldSpecHistorys();
        if (list.isEmpty() == false) {
            response.setMoldUuid(moldUuid);
            response.setId(list.get(0).getId());
            response.setMoldSpecName(list.get(0).getMoldSpecName());
            
            resList.add(response);
        }
        return response;
    }

    /**
     * 最新の金型仕様履歴を取得する
     *
     * @param moldUuid
     * @return
     */
    public List getMstMoldSpecHistoryByIdFromMoldCsv(String moldUuid) {
        Query query = entityManager.createNamedQuery("MstMoldSpecHistory.findByIdForNewEndDate");
        query.setParameter("moldUuid", moldUuid);
        List list = query.getResultList();
        return list;
    }
    
    public MstMoldSpecHistory getMstMoldSpecHistoryLatestByMoldUuidFromMoldCsv(String moldUuid) {
        Query query = entityManager.createQuery("SELECT m FROM MstMoldSpecHistory m WHERE m.mstMold.uuid = :moldUuid and m.endDate = :endDate ");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("endDate", CommonConstants.SYS_MAX_DATE);
        query.setMaxResults(1);
        List<MstMoldSpecHistory> list = query.getResultList();
        return null == list || list.isEmpty() ? null : list.get(0);
    }

    /**
     *
     * @param moldUuid
     * @return
     */
    @Transactional
    public int deleteMstMoldSpecHistory(String moldUuid) {
        Query query = entityManager.createNamedQuery("MstMoldSpecHistory.delete");
        query.setParameter("moldUuid", moldUuid);
        return query.executeUpdate();
    }

    /**
     *
     * @param mstMoldSpecHistory
     */
    @Transactional
    public void createMstMoldSpecHistoryByCsv(MstMoldSpecHistory mstMoldSpecHistory) {
        entityManager.persist(mstMoldSpecHistory);
    }
    
    
    /**
     * T0004_新仕様登録画面_画面描画時
     * @param moldId
     * @return 
     */
    public MstMoldSpecHistory getMoldSpecHistoryList(String moldId){
        
        StringBuilder sql = new StringBuilder(" SELECT m FROM MstMoldSpecHistory m WHERE 1=1 ");
        if (moldId != null && !"".equals(moldId)) {
            sql.append(" And m.mstMold.moldId = :moldId ");
        }
        sql.append(" ORDER BY m.endDate DESC ");

        Query query = entityManager.createQuery(sql.toString());

        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", moldId);
        }

        List list = query.getResultList();
        MstMoldSpecHistory response = (MstMoldSpecHistory) list.get(0);
        return response;
    }

    
    /**
     * バッチで金型仕様履歴マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @param moldUuid
     * @return
     */
    public MstMoldSpecHistoryList getExtMoldSpecHistorysByBatch(String latestExecutedDate, String moldUuid) {
        MstMoldSpecHistoryList resList = new MstMoldSpecHistoryList();
        List<MstMoldSpecHistorys> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMoldSpecHistory t join MstApiUser u on u.companyId = t.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.moldUuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }

        List<MstMoldSpecHistory> tmpList = query.getResultList();
        for (MstMoldSpecHistory mstMoldSpecHistory : tmpList) {
            MstMoldSpecHistorys aVo = new MstMoldSpecHistorys();
            aVo.setMoldId(mstMoldSpecHistory.getMstMold().getMoldId());
            aVo.setEndDateStr(new FileUtil().getDateFormatForStr(mstMoldSpecHistory.getEndDate()));//todo 字符串
            aVo.setEndDateHs(mstMoldSpecHistory.getEndDate().getTime()/1000);//todo 毫秒
            mstMoldSpecHistory.setMstMold(null);
            mstMoldSpecHistory.setMstMoldSpecCollection(null);
            mstMoldSpecHistory.setTblMoldMaintenanceRemodelingCollection(null);
            mstMoldSpecHistory.setMstMoldSpecCollection(null);
            aVo.setMstMoldSpecHistory(mstMoldSpecHistory);
            resVo.add(aVo);
        }
        resList.setMstMoldSpecHistorys(resVo);
        return resList;
    }

    /**
     * バッチで金型仕様履歴マスタデータを更新
     *
     * @param moldSpecHistorys
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldSpecHistorysByBatch(List<MstMoldSpecHistorys> moldSpecHistorys) {
        BasicResponse response = new BasicResponse();

        if (moldSpecHistorys != null && !moldSpecHistorys.isEmpty()) {

            for (MstMoldSpecHistorys aMoldSpecHistorys : moldSpecHistorys) {
                MstMold ownerMold = entityManager.find(MstMold.class, aMoldSpecHistorys.getMoldId());
                List<MstMoldSpecHistory> oldHistorys = entityManager.createQuery("SELECT t FROM MstMoldSpecHistory t WHERE t.id = :id ")
                        .setParameter("id", aMoldSpecHistorys.getMstMoldSpecHistory().getId())
                        .setMaxResults(1)
                        .getResultList();

                MstMoldSpecHistory moldSpecHistory;
                if (null != oldHistorys && !oldHistorys.isEmpty()) {
                    moldSpecHistory = oldHistorys.get(0);
                } else {
                    moldSpecHistory = new MstMoldSpecHistory();
                    moldSpecHistory.setId(aMoldSpecHistorys.getMstMoldSpecHistory().getId());
                } 
                //自社の金型UUIDに変換
                
                if (null != ownerMold) {
                    moldSpecHistory.setMoldUuid(ownerMold.getUuid());
                    moldSpecHistory.setStartDate(aMoldSpecHistorys.getMstMoldSpecHistory().getStartDate());
                    moldSpecHistory.setEndDate(aMoldSpecHistorys.getMstMoldSpecHistory().getEndDate());
                    moldSpecHistory.setMoldSpecName(aMoldSpecHistorys.getMstMoldSpecHistory().getMoldSpecName());

                    moldSpecHistory.setCreateDate(aMoldSpecHistorys.getMstMoldSpecHistory().getCreateDate());
                    moldSpecHistory.setCreateUserUuid(aMoldSpecHistorys.getMstMoldSpecHistory().getCreateUserUuid());
                    moldSpecHistory.setUpdateDate(new Date());
                    moldSpecHistory.setUpdateUserUuid(aMoldSpecHistorys.getMstMoldSpecHistory().getUpdateUserUuid());

                    if (null != oldHistorys && !oldHistorys.isEmpty()) {
                        entityManager.merge(moldSpecHistory);//更新
                    } else {
                        List<MstMoldSpecHistory> lasMstMoldSpecHistorys = entityManager.createQuery("SELECT h from MstMoldSpecHistory h where h.moldUuid = :moldUuid ORDER BY h.endDate DESC ")
                                .setParameter("moldUuid", ownerMold.getUuid()).setMaxResults(1).getResultList();
                        if (null != lasMstMoldSpecHistorys && !lasMstMoldSpecHistorys.isEmpty()){
                            lasMstMoldSpecHistorys.get(0).setEndDate(new FileUtil().getSpecifiedDayBefore(moldSpecHistory.getStartDate()));
                            entityManager.merge(lasMstMoldSpecHistorys.get(0));
                        }
                        
                        entityManager.persist(moldSpecHistory);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
    
    
    /**
     * 外部データ getAllId4Check
     *
     * @return
     */
    public List<String> getAllIdList() {
        return entityManager.createQuery("select m.id from MstMoldSpecHistory m").getResultList();
    }
}
