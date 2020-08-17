/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.recomend;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelVO;
import com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecommend;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author admin
 */
@Dependent
public class TblMoldMaintenanceRecomendService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     * 金型メンテ開始候補リスト取得
     *
     * @return
     */
    public TblMoldMaintenanceRecomendListVO getMoldMaintenanceRecommendList() {
        TblMoldMaintenanceRecomendListVO tblMoldMaintenanceRecomendList = new TblMoldMaintenanceRecomendListVO();

        // アラート・メンテ区分が2(メンテ)且つメンテ済みフラグが0(未メンテ)且つ金型メンテステータスが0(通常)のデータ抽出
        Query moldRecomendQuery = entityManager.createNamedQuery("TblMoldMaintenanceRecomend.findRecomendList");
        List<TblMoldMaintenanceRecomend> moldMaintRecommendList = moldRecomendQuery.getResultList();
        
        StringBuilder moldPartMaintenanceRecommendQuery = new StringBuilder(
                "SELECT moldPartMaintenanceRecommend FROM TblMoldPartMaintenanceRecommend moldPartMaintenanceRecommend"
                + " LEFT JOIN FETCH moldPartMaintenanceRecommend.mstMold mstMold"
                + " LEFT JOIN FETCH moldPartMaintenanceRecommend.mstMoldPartRel mstMoldPartRel"
                + " LEFT JOIN FETCH mstMoldPartRel.mstMoldPart mstMoldPart"
                + " WHERE moldPartMaintenanceRecommend.maintainedFlag = 0"); 
        Query moldPartRecomendQuery = entityManager.createQuery(moldPartMaintenanceRecommendQuery.toString());
        List<TblMoldPartMaintenanceRecommend> moldPartMaintRecommendlist = moldPartRecomendQuery.getResultList();

        List<TblMoldMaintenanceRecomendVO> moldMaintenanceRecommendVO = new ArrayList<>();
        
        Comparator<TblMoldPartMaintenanceRecommend> compare = (TblMoldPartMaintenanceRecommend recommend1, TblMoldPartMaintenanceRecommend recommend2) -> {
            int result = recommend1.getReplaceOrRepair().compareTo(recommend2.getReplaceOrRepair());
            if (result == 0) {
                result = recommend1.getCreateDate().compareTo(recommend2.getCreateDate());
            }
            if (result == 0) {
                result = recommend1.getMstMoldPartRel().getLocation().compareTo(recommend2.getMstMoldPartRel().getLocation());
            }
            return result;
        };
        
        if (!moldMaintRecommendList.isEmpty()) {
            FileUtil fu = new FileUtil();
            for (TblMoldMaintenanceRecomend tblMoldMaintenanceRecomend : moldMaintRecommendList) {
                TblMoldMaintenanceRecomendVO newRecommend = new TblMoldMaintenanceRecomendVO();
                newRecommend.setId(tblMoldMaintenanceRecomend.getId());
                newRecommend.setAlertMainteType(tblMoldMaintenanceRecomend.getAlertMainteType());
                newRecommend.setMaintainedFlag(tblMoldMaintenanceRecomend.getMaintainedFlag());
                newRecommend.setHitCondition(tblMoldMaintenanceRecomend.getHitCondition());
                
                newRecommend.setMoldUuid(tblMoldMaintenanceRecomend.getMoldUuid());
                newRecommend.setMstMold(tblMoldMaintenanceRecomend.getMstMold());
                newRecommend.setMainteCycleId(tblMoldMaintenanceRecomend.getMainteCycleId());
                newRecommend.setTblMaintenanceCyclePtn(tblMoldMaintenanceRecomend.getTblMaintenanceCyclePtn());
                
                newRecommend.setCreateDate(tblMoldMaintenanceRecomend.getCreateDate());
                newRecommend.setCreateUserUuid(tblMoldMaintenanceRecomend.getCreateUserUuid());
                newRecommend.setUpdateDate(tblMoldMaintenanceRecomend.getUpdateDate());
                newRecommend.setUpdateUserUuid(tblMoldMaintenanceRecomend.getUpdateUserUuid());
                
                if (tblMoldMaintenanceRecomend.getMstMold() != null) {
                    tblMoldMaintenanceRecomend.getMstMold().setLastMainteDateStr(
                            fu.getDateFormatForStr(tblMoldMaintenanceRecomend.getMstMold().getLastMainteDate()));
                
                    // Check existed mold part maintenance recommend include mold uuid
                    List<TblMoldPartMaintenanceRecommend> mpMaintRecommend = 
                            moldPartMaintRecommendlist.stream()
                                    .filter(mpRecommend -> 
                                            mpRecommend.getMoldUuid().equals(tblMoldMaintenanceRecomend.getMoldUuid())
                                    ).collect(Collectors.toList());
                    
                    Collections.sort(mpMaintRecommend, compare);
                    // If the list of mold part maintenance recommend contains the item has the mold uuid
                    // 1. Add the mold part maintenance recommend
                    // 2. Remove other recommends have same mold uuid in the list of mold part maintenance recommend
                    if (null != mpMaintRecommend && mpMaintRecommend.size() > 0) {
                        MstMoldPartRelVO moldPartDetailRelVo = new MstMoldPartRelVO();
                        moldPartDetailRelVo.setLocation(mpMaintRecommend.get(0).getMstMoldPartRel().getLocation());
                        moldPartDetailRelVo.setMstMoldPart(mpMaintRecommend.get(0).getMstMoldPartRel().getMstMoldPart());
                        newRecommend.setMstMoldPartRelVO(moldPartDetailRelVo);
                        moldPartMaintRecommendlist.removeAll(mpMaintRecommend);
                    }
                }
                moldMaintenanceRecommendVO.add(newRecommend);
            }
        }
        // This list will contains molds that don't existed in the current mold maintenance recommend table
        if (!moldPartMaintRecommendlist.isEmpty()) {
            FileUtil fu = new FileUtil();
            // Grouping the mold part maintenance recommend by mold 
            Map<String, List<TblMoldPartMaintenanceRecommend>> groupedMPMaintRecommendByMoldId = 
                    moldPartMaintRecommendlist.stream().collect(Collectors.groupingBy(m -> m.getMoldUuid()));
            
            groupedMPMaintRecommendByMoldId.forEach((key, value) -> {
                if (null != value && !value.isEmpty()) {
                    Collections.sort(value, compare);
                    TblMoldPartMaintenanceRecommend mpMaintRecommend = value.get(0);
                    
                    if (mpMaintRecommend != null) {
                        TblMoldMaintenanceRecomendVO newRecommend = new TblMoldMaintenanceRecomendVO();
                        newRecommend.setId(mpMaintRecommend.getId());
                        newRecommend.setMaintainedFlag(mpMaintRecommend.getMaintainedFlag());
                        newRecommend.setHitCondition(mpMaintRecommend.getHitCondition());

                        mpMaintRecommend.getMstMold().setLastMainteDateStr(fu.getDateFormatForStr(mpMaintRecommend.getMstMold().getLastMainteDate()));
                        newRecommend.setMstMold(mpMaintRecommend.getMstMold());

                        newRecommend.setCreateDate(mpMaintRecommend.getCreateDate());
                        newRecommend.setCreateUserUuid(mpMaintRecommend.getCreateUserUuid());
                        newRecommend.setUpdateDate(mpMaintRecommend.getUpdateDate());
                        newRecommend.setUpdateUserUuid(mpMaintRecommend.getUpdateUserUuid());
                        if (mpMaintRecommend.getMstMoldPartRel() != null 
                                && mpMaintRecommend.getMstMoldPartRel().getMstMoldPart() != null) {
                            newRecommend.setMoldUuid(mpMaintRecommend.getMoldUuid());
                            MstMoldPartRelVO moldPartDetailRelVo = new MstMoldPartRelVO();
                            moldPartDetailRelVo.setLocation(mpMaintRecommend.getMstMoldPartRel().getLocation());
                            moldPartDetailRelVo.setMstMoldPart(mpMaintRecommend.getMstMoldPartRel().getMstMoldPart());
                            newRecommend.setMstMoldPartRelVO(moldPartDetailRelVo);
                        }
                        moldMaintenanceRecommendVO.add(newRecommend);
                    }
                }
            });
        }
        tblMoldMaintenanceRecomendList.setTblMoldMaintenanceRecomendList(moldMaintenanceRecommendVO);
        return tblMoldMaintenanceRecomendList;
    }

    /**
     * 金型UUIDより金型メンテ候補データ取得
     *
     * @param moldUuid
     * @return
     */
    public TblMoldMaintenanceRecomendList getMoldMaintenanceRecommendByUuid(String moldUuid) {
        TblMoldMaintenanceRecomendList tblMoldMaintenanceRecomendList = new TblMoldMaintenanceRecomendList();

        // 金型UUIDによりメンテ済みフラグが0(未メンテ)のデータ抽出
        Query moldRecomendQuery = entityManager.createNamedQuery("TblMoldMaintenanceRecomend.findByUuid");
        moldRecomendQuery.setParameter("moldUuid", moldUuid);
        List<TblMoldMaintenanceRecomend> list = moldRecomendQuery.getResultList();

        tblMoldMaintenanceRecomendList.setTblMoldMaintenanceRecomendList(list);
        return tblMoldMaintenanceRecomendList;
    }
    
    /**
     * 金型メンテナンス候補テーブル登録
     *
     * @param tblMoldMaintenanceRecomend
     * @param loginUser
     */
    @Transactional
    public void createtblMoldMaintenanceRecomend(TblMoldMaintenanceRecomend tblMoldMaintenanceRecomend, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        tblMoldMaintenanceRecomend.setId(IDGenerator.generate());
        tblMoldMaintenanceRecomend.setCreateDate(new java.util.Date());
        tblMoldMaintenanceRecomend.setCreateUserUuid(loginUser.getUserUuid());
        tblMoldMaintenanceRecomend.setUpdateDate(new java.util.Date());
        tblMoldMaintenanceRecomend.setUpdateUserUuid(loginUser.getUserUuid());

        entityManager.persist(tblMoldMaintenanceRecomend);
    }

    /**
     * 金型メンテナンス候補テーブル削除 BY サイクルID
     * サイクルコード修正するとき、且つメンテ候補テーブルのデータがまだ処理されていないデータを削除
     * @param mainteCycleId
     */
    @Transactional
    public void deleteTblMoldMaintenanceRecomendBymainteCycleId(String mainteCycleId) {

        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRecomend.deleteByMainteCycleId");
        query.setParameter("mainteCycleId", mainteCycleId);
        query.executeUpdate();
    }
    
    /**
     * // 最終メンテナンス日 //メンテナンス後生産時間 //メンテナンス後ショット数 //メンテサイクルコード01 //メンテサイクルコード02
     * //メンテサイクルコード03 //　上記項目について、いずれか更新されたら、金型候補テーブルに該当金型が未メンテのデータを削除を行う
     *
     * @param moldUuid
     */
    @Transactional
    public void deleteTblMoldMaintenanceRecomendByMoldUuid(String moldUuid) {

        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRecomend.deleteByMainteMoldUuid");
        query.setParameter("moldUuid", moldUuid);
        query.executeUpdate();
    }
    
    
    /**
     * 設備メンテナンス候補テーブル削除
     *
     * @param tblMoldMaintenanceRecomend
     */
    @Transactional
    public void deleteTblMoldMaintenanceRecomend(TblMoldMaintenanceRecomend tblMoldMaintenanceRecomend) {

        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRecomend.delete");
        query.setParameter("id", tblMoldMaintenanceRecomend.getId());
        query.executeUpdate();
    }

    /**
     * 金型メンテナンス候補テーブル更新
     *
     * @param tblMoldMaintenanceRecomend
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMoldMaintenanceRecomend updateTblMoldMaintenanceRecomend(TblMoldMaintenanceRecomend tblMoldMaintenanceRecomend, LoginUser loginUser) {
        tblMoldMaintenanceRecomend.setUpdateDate(new java.util.Date());
        tblMoldMaintenanceRecomend.setUpdateUserUuid(loginUser.getUserUuid());
        // メンテ済を更新する
        tblMoldMaintenanceRecomend.setMaintainedFlag(1);
        entityManager.merge(tblMoldMaintenanceRecomend);
        return tblMoldMaintenanceRecomend;
    }

    /**
     * 金型メンテ開始候補リスト取得
     *
     * @param moldUuid
     * @param alertMainteType
     * @param maintainedFlag
     *
     * @return
     */
    public int chkExists(String moldUuid, Integer alertMainteType, Integer maintainedFlag) {

        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRecomend.chkExists");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("alertMainteType", alertMainteType);
        query.setParameter("maintainedFlag", maintainedFlag);

        return query.getResultList().size();
    }

    /**
     * 金型メンテナンス候補テーブルに登録
     *
     * @param list
     * @return
     */
    @Transactional
    public int batchInsert(List<TblMoldMaintenanceRecomend> list) {

        int insertCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            entityManager.persist(list.get(i - 1));

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();

                insertCount += 50;
            }

            count = i;

        }

        insertCount += count % 50;

        return insertCount;
    }

    /**
     * @param moldId
     * @param moldName
     * @param department
     * @return 
     */
    public TblMoldMoldPartMaintenanceRecommendList getRecommendMoldParts(String moldId, String moldName, int department){
        TblMoldMoldPartMaintenanceRecommendList tblMoldPartMaintenanceRecommendList = new TblMoldMoldPartMaintenanceRecommendList();
        int index = 1;
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT mm.UUID, mm.MOLD_ID, mm.MOLD_NAME, mm.LAST_MAINTE_DATE, mm.TOTAL_SHOT_COUNT, moldPart.MOLD_PART_CODE, ");
        sql.append("mm.TOTAL_PRODUCING_TIME_HOUR, maintCyclePtn.CYCLE_NAME, moldPartRel.LOCATION, moldPartMaintRec.REPLACE_OR_REPAIR, ");
        sql.append("moldPartRel.LAST_RPL_DATETIME, moldPartRel.LAST_RPR_DATETIME, moldPartRel.RPL_CL_SHOT_CNT, moldPartRel.RPR_CL_SHOT_CNT, ");
        sql.append("moldPartRel.RPL_CL_PROD_TIME_HOUR, moldPartRel.RPR_CL_PROD_TIME_HOUR ");
        sql.append("FROM mst_mold mm ");
        sql.append("LEFT OUTER JOIN tbl_mold_maintenance_recomend moldMaintRec ON moldMaintRec.MOLD_UUID = mm.UUID AND moldMaintRec.MAINTAINED_FLAG = 0 ");
        sql.append("LEFT OUTER JOIN tbl_mold_part_maintenance_recommend moldPartMaintRec ON moldPartMaintRec.MOLD_UUID = mm.UUID AND moldPartMaintRec.MAINTAINED_FLAG = 0 ");
        sql.append("LEFT OUTER JOIN mst_mold_part_rel moldPartRel ON moldPartRel.ID = moldPartMaintRec.MOLD_PART_REL_ID ");
        sql.append("LEFT OUTER JOIN tbl_maintenance_cycle_ptn maintCyclePtn ON maintCyclePtn.ID = moldMaintRec.MAINTE_CYCLE_ID ");
        sql.append("LEFT OUTER JOIN mst_mold_part moldPart ON moldPart.ID = moldPartRel.MOLD_PART_ID ");
        sql.append("WHERE (moldMaintRec.MOLD_UUID IS NOT NULL OR moldPartMaintRec.MOLD_UUID IS NOT NULL) ");
        if(!StringUtils.isEmpty(moldId)){
            sql = sql.append("AND mm.MOLD_ID LIKE ? ");
        }
        if(!StringUtils.isEmpty(moldName)){
            sql = sql.append("AND mm.MOLD_NAME LIKE ? ");
        }
        if(department != 0 ){
            sql = sql.append("AND mm.DEPARTMENT = ? ");
        }
        sql.append("ORDER BY mm.MOLD_ID, moldPartRel.LOCATION ");
        Query query = entityManager.createNativeQuery(sql.toString());
        if(!StringUtils.isEmpty(moldId)){
            query.setParameter(index++, "%" + moldId.trim() + "%");
        }
        if(!StringUtils.isEmpty(moldName)){
            query.setParameter(index++, "%" + moldName.trim() + "%");
        }
        if(department != 0 ){
            query.setParameter(index++, department);
        }
                 
        List<Object[]> recommendMoldPartList = query.getResultList();
        List<TblMoldMoldPartMaintenanceRecommend> moldMoldPartList = new ArrayList<>();
        for(Object[] moldPart : recommendMoldPartList){
            TblMoldMoldPartMaintenanceRecommend moldMoldPart = new TblMoldMoldPartMaintenanceRecommend();
            if(moldPart[0] != null){
                moldMoldPart.setMoldUuid(moldPart[0].toString());
            }else{
                moldMoldPart.setMoldUuid("");
            }
            if(moldPart[1] != null){
                moldMoldPart.setMoldId(moldPart[1].toString());
            }else{
                moldMoldPart.setMoldId("");
            }
            if(moldPart[2] != null){
                moldMoldPart.setMoldName(moldPart[2].toString());
            }else{
                moldMoldPart.setMoldName("");
            }
            if(moldPart[3] != null){
                moldMoldPart.setLastMainteDate((Date)moldPart[3]);
            }
            if(moldPart[4] != null){
                moldMoldPart.setShotCount((int)moldPart[4]);
            }
            if(moldPart[5] != null){
                moldMoldPart.setMoldPartCode(moldPart[5].toString());
            }else{
                moldMoldPart.setMoldPartCode("");
            }
            if(moldPart[6] != null){
                moldMoldPart.setTotalProducingTimeHour((BigDecimal)moldPart[6]);
            }
            if(moldPart[7] != null){
                moldMoldPart.setMainteCycleName(moldPart[7].toString());
            }else{
                moldMoldPart.setMainteCycleName("");
            }
            if(moldPart[8] != null){
                moldMoldPart.setLocation(moldPart[8].toString());
            }else{
                moldMoldPart.setLocation("");
            }
            if(moldPart[9] != null){
                moldMoldPart.setReplaceOrRepair((int)moldPart[9]);
            }
            if(moldPart[10] != null){
                moldMoldPart.setLastReplaceDate((Date)moldPart[10]);
            }
            if(moldPart[11] != null){
                moldMoldPart.setLastRepairDate((Date)moldPart[11]);
            }
            if(moldPart[12] != null){
                moldMoldPart.setReplaceShotCount((int)moldPart[12]);
            }
            if(moldPart[13] != null){
                moldMoldPart.setRepairShotCount((int)moldPart[13]);
            }
            if(moldPart[14] != null){
                moldMoldPart.setReplaceProdTimeHour((int)moldPart[14]);
            }
            if(moldPart[15] != null){
                moldMoldPart.setRepairProdTimeHour((int)moldPart[15]);
            }
            
            moldMoldPartList.add(moldMoldPart);
        }
        
        tblMoldPartMaintenanceRecommendList.setTblMoldMoldPartMaintenanceRecommendList(moldMoldPartList);
        return tblMoldPartMaintenanceRecommendList;
    }
    
}
