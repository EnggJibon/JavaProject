/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.item;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoice;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoiceVo;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author jiangxs
 */
@Dependent
public class MstMoldInspectionItemService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     *
     * @param taskCategory1
     * @param taskCategory2
     * @param taskCategory3
     * @param loginUser
     * @return
     */
    public MstMoldInspectionItemVo getInspectionItems(String taskCategory1, String taskCategory2, String taskCategory3, LoginUser loginUser) {
        MstMoldInspectionItemVo resVo = new MstMoldInspectionItemVo();
        List<MstMoldInspectionItemVo> moldInspectionItemVos = new ArrayList<>();
        Query query = null;
        if (null == taskCategory1 || taskCategory1.trim().equals("") || null == taskCategory2 || taskCategory2.trim().equals("") || null == taskCategory3 || taskCategory3.trim().equals("")) {
            query = entityManager.createNamedQuery("MstMoldInspectionItem.findAllGroupbyTaskCategory");
        } else {
            StringBuilder sql = new StringBuilder("SELECT m FROM MstMoldInspectionItem m WHERE 1 = 1 and m.externalFlg = 0 ");
            if (null != taskCategory1 && !taskCategory1.trim().equals("")) {
                sql.append(" and m.taskCategory1 = :taskCategory1 ");
                if (null != taskCategory2 && !taskCategory2.trim().equals("")) {
                    sql.append(" and m.taskCategory2 = :taskCategory2 ");
                    if (null != taskCategory3 && !taskCategory3.trim().equals("")) {
                        sql.append(" and m.taskCategory3 = :taskCategory3 ");
                    }
                }
            }
            sql.append(" order by m.seq ");
            query = entityManager.createQuery(sql.toString());
            if (null != taskCategory1 && !taskCategory1.trim().equals("")) {
                query.setParameter("taskCategory1", Integer.parseInt(taskCategory1));
                if (null != taskCategory2 && !taskCategory2.trim().equals("")) {
                    query.setParameter("taskCategory2", Integer.parseInt(taskCategory2));
                    if (null != taskCategory3 && !taskCategory3.trim().equals("")) {
                        query.setParameter("taskCategory3", Integer.parseInt(taskCategory3));
                    }
                }
            }
        }

        List<MstMoldInspectionItem> moldInspectionItems = query.getResultList();
        if (null != moldInspectionItems && !moldInspectionItems.isEmpty()) {

            for (int i = 0; i < moldInspectionItems.size(); i++) {
                MstMoldInspectionItem aMoldInspectionItem = moldInspectionItems.get(i);
                MstMoldInspectionItemVo aVo = new MstMoldInspectionItemVo();
                aVo.setTaskCategory1("" + aMoldInspectionItem.getTaskCategory1());

                aVo.setTaskCategory2("" + aMoldInspectionItem.getTaskCategory2());

                aVo.setTaskCategory3("" + aMoldInspectionItem.getTaskCategory3());

                if (null != taskCategory1 && null != taskCategory2 && null != taskCategory3) {
                    aVo.setItemSeq("" + aMoldInspectionItem.getSeq());
                    aVo.setInspectionItemName(aMoldInspectionItem.getInspectionItemName());
                    if (null != aMoldInspectionItem.getResultType()) {
                        aVo.setResultType("" + aMoldInspectionItem.getResultType());
                    }
                    aVo.setItemId(aMoldInspectionItem.getId());
                }

                moldInspectionItemVos.add(aVo);
            }
        }

        resVo.setMoldInspectionItemVos(moldInspectionItemVos);
        return resVo;
    }

    /**
     *
     * @param moldInspectionItemVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postInspectionItems(MstMoldInspectionItemVo moldInspectionItemVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        Map<String, MstMoldInspectionItem> miItemVosMap = new HashMap<>();
        if (null != moldInspectionItemVo.getMoldInspectionItemVos() && moldInspectionItemVo.getMoldInspectionItemVos().isEmpty() == false) {

            List<MstMoldInspectionItemVo> moldInspectionItemVos = moldInspectionItemVo.getMoldInspectionItemVos();
            Collections.sort(moldInspectionItemVos);

            for (int i = moldInspectionItemVos.size() - 1; i >= 0; i--) {
                MstMoldInspectionItemVo aInspectionItemVo = moldInspectionItemVos.get(i);
                if (null == aInspectionItemVo) {
                    continue;
                }
                MstMoldInspectionItem moldInspectionItem = null;
                if ("1".equals(aInspectionItemVo.getDeleteFlag())) {
                    if(aInspectionItemVo.getItemId() == null || "".equals(aInspectionItemVo.getItemId())){
                        continue;
                    }
                    //削除の記し
                    //delete MstMoldInspectionItemVo
                    moldInspectionItem = entityManager.find(MstMoldInspectionItem.class, aInspectionItemVo.getItemId());
                    if (null == moldInspectionItem.getTblMoldInspectionResultCollection() || moldInspectionItem.getTblMoldInspectionResultCollection().isEmpty()) {
                        if(getMstMoldInspectionItemFKCheck(aInspectionItemVo.getItemId())){
                            basicResponse.setError(true);
                            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
                            return basicResponse;
                        }
                        Query delQuery = entityManager.createQuery("DELETE FROM MstMoldInspectionItem ii where ii.id = :id ");
                        delQuery.setParameter("id", aInspectionItemVo.getItemId());
                        delQuery.executeUpdate();
                        moldInspectionItemVos.remove(aInspectionItemVo);
//                        Query updateQuery = entityManager.createQuery("UPDATE MstMoldInspectionItem ii set ii.seq = ii.seq - 1 where ii.seq > :seq and ii.taskCategory1 = :taskCategory1 and ii.taskCategory2 = :taskCategory2 and ii.taskCategory3 = :taskCategory3 ");
//                        updateQuery.setParameter("seq", aInspectionItemVo.getSeq());
//                        updateQuery.setParameter("taskCategory1", Integer.parseInt(aInspectionItemVo.getTaskCategory1()));
//                        updateQuery.setParameter("taskCategory2", Integer.parseInt(aInspectionItemVo.getTaskCategory2()));
//                        updateQuery.setParameter("taskCategory3", Integer.parseInt(aInspectionItemVo.getTaskCategory3()));
//                        updateQuery.executeUpdate();
                    } else {
                        basicResponse.setError(true);
                        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                        basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
                        return basicResponse;
                    }
                }
            }

            for (int i = moldInspectionItemVos.size() - 1; i >= 0; i--) {
                MstMoldInspectionItemVo aInspectionItemVo = moldInspectionItemVos.get(i);
                if (null == aInspectionItemVo) {
                    continue;
                }
                MstMoldInspectionItem moldInspectionItem;
                if ("1".equals(aInspectionItemVo.getDeleteFlag()) == false) {

                    if (null == aInspectionItemVo.getItemId() || aInspectionItemVo.getItemId().equals("")) {
                        //new
                        moldInspectionItem = new MstMoldInspectionItem();
                        moldInspectionItem.setId(IDGenerator.generate());
                        moldInspectionItem.setTaskCategory1(Integer.parseInt(aInspectionItemVo.getTaskCategory1()));
                        moldInspectionItem.setTaskCategory2(Integer.parseInt(aInspectionItemVo.getTaskCategory2()));
                        moldInspectionItem.setTaskCategory3(Integer.parseInt(aInspectionItemVo.getTaskCategory3()));
                        moldInspectionItem.setCreateDate(new Date());
                        moldInspectionItem.setCreateDateUuid(loginUser.getUserUuid());
                    } else {
                        //modify
                        moldInspectionItem = entityManager.find(MstMoldInspectionItem.class, aInspectionItemVo.getItemId());
                    }
                    moldInspectionItem.setInspectionItemName(aInspectionItemVo.getInspectionItemName());
                    moldInspectionItem.setResultType(Integer.parseInt(aInspectionItemVo.getResultType()));
                    moldInspectionItem.setUpdateDate(new Date());
                    moldInspectionItem.setUpdateUserUuid(loginUser.getUserUuid());
                    moldInspectionItem.setSeq(aInspectionItemVo.getSeq());
                    moldInspectionItem.setExternalFlg(0);
                    if (null == aInspectionItemVo.getItemId() || aInspectionItemVo.getItemId().equals("")) {
                        entityManager.persist(moldInspectionItem);
                    } else {
                        Query updateQuery = entityManager.createQuery("Update MstMoldInspectionItem i set i.seq = :seq where i.id = :id");
                        updateQuery.setParameter("seq", aInspectionItemVo.getSeq());
                        updateQuery.setParameter("id", aInspectionItemVo.getItemId());
                        updateQuery.executeUpdate();

                        if (null != aInspectionItemVo.getChangeFlag() && aInspectionItemVo.getChangeFlag().equals("1")) {
                            Query delQuery = entityManager.createNamedQuery("MstMoldInspectionChoice.deleteByInspectionItemId");
                            delQuery.setParameter("inspectionItemId", aInspectionItemVo.getItemId());
                            delQuery.executeUpdate();
                        }
                    }

                    miItemVosMap.put("" + moldInspectionItem.getSeq(), moldInspectionItem);
                }
            }
        }

        if (null != moldInspectionItemVo.getMoldInspectionChoiceVo() && moldInspectionItemVo.getMoldInspectionChoiceVo().isEmpty() == false) {
            MstMoldInspectionItem item = (MstMoldInspectionItem) miItemVosMap.get(moldInspectionItemVo.getMoldInspectionChoiceVo().get(0).getInspectionItemSeq());

            if (null != item && null != item.getResultType() && item.getResultType() == 3) { // had MstMoldInspectionChoices
                //remove old
                Query delChoices = entityManager.createNamedQuery("MstMoldInspectionChoice.deleteByInspectionItemId");
                delChoices.setParameter("inspectionItemId", item.getId());
                delChoices.executeUpdate();

                //add new
                for (int i = 0, tmpSeq = 1; i < moldInspectionItemVo.getMoldInspectionChoiceVo().size(); i++, tmpSeq++) {
                    MstMoldInspectionChoiceVo aVo = moldInspectionItemVo.getMoldInspectionChoiceVo().get(i);
                    if (aVo.getDeleteFlag().equals("1") || null == aVo.getInspectionItemSeq() || aVo.getInspectionItemSeq().equals("") || null == aVo.getChoice() || aVo.getChoice().trim().equals("")) {
                        tmpSeq--;
                        continue;
                    }
                    MstMoldInspectionChoice newChoice = new MstMoldInspectionChoice();
                    newChoice.setId(IDGenerator.generate());
                    newChoice.setInspectionItemId(item.getId());
                    newChoice.setChoice(aVo.getChoice());
                    newChoice.setSeq(tmpSeq);
                    newChoice.setCreateDate(new Date());
                    newChoice.setCreateUserUuid(loginUser.getUserUuid());
                    newChoice.setUpdateDate(new Date());
                    newChoice.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(newChoice);
                }
            }
        }

        return basicResponse;
    }

    /**
     * バッチで金型点検項目マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstMoldInspectionItemList getExtMoldInspectionItemsByBatch(String latestExecutedDate) {
        MstMoldInspectionItemList resList = new MstMoldInspectionItemList();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstMoldInspectionItem t WHERE 1=1 and t.externalFlg = 0 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMoldInspectionItem> mstMoldInspectionItemsList = query.getResultList();
        for (MstMoldInspectionItem mstMoldInspectionItem : mstMoldInspectionItemsList) {
            mstMoldInspectionItem.setMstMoldInspectionChoiceCollection(null);
            mstMoldInspectionItem.setTblMoldInspectionResultCollection(null);
        }
        resList.setMstMoldInspectionItemList(mstMoldInspectionItemsList);
        return resList;
    }

    /**
     * バッチで金型点検項目マスタデータを更新
     *
     * @param moldInspectionItems
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldInspectionItemsByBatch(List<MstMoldInspectionItem> moldInspectionItems) {
        BasicResponse response = new BasicResponse();
        if (moldInspectionItems != null && !moldInspectionItems.isEmpty()) {
            for (MstMoldInspectionItem aMoldInspectionItem : moldInspectionItems) {
                MstMoldInspectionItem newMoldInspectionItem;
                List<MstMoldInspectionItem> oldMoldInspectionItems = entityManager.createQuery("SELECT t FROM MstMoldInspectionItem t WHERE 1=1 and t.id=:id and t.externalFlg = 1 ")
                        .setParameter("id", aMoldInspectionItem.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMoldInspectionItems && !oldMoldInspectionItems.isEmpty()) {
                    newMoldInspectionItem = oldMoldInspectionItems.get(0);
                } else {
                    newMoldInspectionItem = new MstMoldInspectionItem();
                }

                newMoldInspectionItem.setExternalFlg(1);
                newMoldInspectionItem.setTaskCategory1(aMoldInspectionItem.getTaskCategory1());
                newMoldInspectionItem.setTaskCategory2(aMoldInspectionItem.getTaskCategory2());
                newMoldInspectionItem.setTaskCategory3(aMoldInspectionItem.getTaskCategory3());
                newMoldInspectionItem.setSeq(aMoldInspectionItem.getSeq());
                newMoldInspectionItem.setInspectionItemName(aMoldInspectionItem.getInspectionItemName());
                newMoldInspectionItem.setResultType(aMoldInspectionItem.getResultType());
                newMoldInspectionItem.setCreateDate(aMoldInspectionItem.getCreateDate());
                newMoldInspectionItem.setCreateDateUuid(aMoldInspectionItem.getCreateDateUuid());
                newMoldInspectionItem.setUpdateDate(aMoldInspectionItem.getUpdateDate());
                newMoldInspectionItem.setUpdateUserUuid(aMoldInspectionItem.getUpdateUserUuid());

                if (null != oldMoldInspectionItems && !oldMoldInspectionItems.isEmpty()) {
                    entityManager.merge(newMoldInspectionItem);//更新
                } else {
                    newMoldInspectionItem.setId(aMoldInspectionItem.getId());//追加
                    entityManager.persist(newMoldInspectionItem);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
    
    
    /**
     *点検項目設定マスタのFK依存関係チェック
     *
     * @param id
     * @return
     */
    public boolean getMstMoldInspectionItemFKCheck(String id) {

        
        boolean flg = false;

        //tbl_machine_inspection_result	FK_TBL_MACHINE_INSPECTION_RESULT_INSPECTION_ITEM_ID	NO ACTION
        //tbl_machine_remodeling_inspection_result  FK_TBL_MACHINE_REMODELING_INSPECTION_RESULT_INSPECTION_ITEM_ID	NO ACTION
        if (!flg) {
            Query queryMstMachineAttribute = entityManager.createNamedQuery("TblMoldInspectionResult.findByInspectionItemId");
            queryMstMachineAttribute.setParameter("inspectionItemId", id);
            flg = queryMstMachineAttribute.getResultList().size() > 0;
        }
        
        if (!flg) {
            Query queryMstMachineAttribute = entityManager.createNamedQuery("TblMoldRemodelingInspectionResult.findByInspectionItemId");
            queryMstMachineAttribute.setParameter("inspectionItemId", id);
            flg = queryMstMachineAttribute.getResultList().size() > 0;
        }

        return flg;
    }
}
