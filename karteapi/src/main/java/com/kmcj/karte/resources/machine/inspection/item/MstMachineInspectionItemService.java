/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inspection.item;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.inspection.choice.MstMachineInspectionChoice;
import com.kmcj.karte.resources.machine.inspection.choice.MstMachineInspectionChoiceVo;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResult;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
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
public class MstMachineInspectionItemService {

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
    public MstMachineInspectionItemVo getInspectionItems(String taskCategory1, String taskCategory2, String taskCategory3, LoginUser loginUser) {
        MstMachineInspectionItemVo resVo = new MstMachineInspectionItemVo();
        List<MstMachineInspectionItemVo> machineInspectionItemVos = new ArrayList<>();
        Query query = null;
        if (null == taskCategory1 || taskCategory1.trim().equals("") || null == taskCategory2 || taskCategory2.trim().equals("") || null == taskCategory3 || taskCategory3.trim().equals("")) {
            query = entityManager.createNamedQuery("MstMachineInspectionItem.findAllGroupbyTaskCategory");
        } else {
            StringBuilder sql = new StringBuilder("SELECT m FROM MstMachineInspectionItem m WHERE 1 = 1 and m.externalFlg = 0 ");
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

        List<MstMachineInspectionItem> machineInspectionItems = query.getResultList();
        if (null != machineInspectionItems && !machineInspectionItems.isEmpty()) {
            for (int i = 0; i < machineInspectionItems.size(); i++) {
                MstMachineInspectionItem aMachineInspectionItem = machineInspectionItems.get(i);
                MstMachineInspectionItemVo aVo = new MstMachineInspectionItemVo();

                aVo.setTaskCategory1("" + aMachineInspectionItem.getTaskCategory1());

                aVo.setTaskCategory2("" + aMachineInspectionItem.getTaskCategory2());

                aVo.setTaskCategory3("" + aMachineInspectionItem.getTaskCategory3());

                if (null != taskCategory1 && null != taskCategory2 && null != taskCategory3) {
                    aVo.setItemSeq("" + aMachineInspectionItem.getSeq());
                    aVo.setInspectionItemName(aMachineInspectionItem.getInspectionItemName());
                    if (null != aMachineInspectionItem.getResultType()) {
                        aVo.setResultType("" + aMachineInspectionItem.getResultType());
                    }
                    aVo.setItemId(aMachineInspectionItem.getId());
                }

                machineInspectionItemVos.add(aVo);
            }
        }

        resVo.setMstMachineInspectionItemVos(machineInspectionItemVos);
        return resVo;
    }

    /**
     *
     * @param machineInspectionItemVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postInspectionItems(MstMachineInspectionItemVo machineInspectionItemVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        Map<String, MstMachineInspectionItem> miItemVosMap = new HashMap<>();
        if (null != machineInspectionItemVo.getMstMachineInspectionItemVos() && machineInspectionItemVo.getMstMachineInspectionItemVos().isEmpty() == false) {

            List<MstMachineInspectionItemVo> machineInspectionItemVos = machineInspectionItemVo.getMstMachineInspectionItemVos();

            int delCount = 0;
            for (int i = machineInspectionItemVos.size() - 1; i >= 0; i--) {
                MstMachineInspectionItemVo aInspectionItemVo = machineInspectionItemVos.get(i);
                if (null == aInspectionItemVo) {
                    continue;
                }
                
                if ("1".equals(aInspectionItemVo.getOperationFlag())) {
                    if (aInspectionItemVo.getItemId() == null || "".equals(aInspectionItemVo.getItemId())) {
                        continue;
                    }
                    //削除の記し
                    delCount += 1;
                    
                    Query query = entityManager.createNamedQuery("TblMachineInspectionResult.findByInspectionItemId");
                    query.setParameter("inspectionItemId", aInspectionItemVo.getItemId());
                    List<TblMachineInspectionResult> machineInspectionItemResults = query.getResultList();
                    
                    if (null == machineInspectionItemResults || machineInspectionItemResults.isEmpty()) {
                        
                        if (getMstMachineInspectionItemFKCheck(aInspectionItemVo.getItemId())) {                            
                            basicResponse.setError(true);
                            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
                            return basicResponse;                            
                        } else {
                            Query delQuery = entityManager.createQuery("DELETE FROM MstMachineInspectionItem inspectionItem WHERE inspectionItem.id = :id ");
                            delQuery.setParameter("id", aInspectionItemVo.getItemId());
                            delQuery.executeUpdate();

//                        StringBuilder sb = new StringBuilder(" UPDATE MstMachineInspectionItem inspectionItem ");
//                        sb.append(" SET inspectionItem.seq = inspectionItem.seq - 1 ");
//                        sb.append(" , inspectionItem.updateUserUuid = :updateUserUuid ");
//                        sb.append(" , inspectionItem.updateDate = :updateDate ");
//                        sb.append(" WHERE inspectionItem.seq > :seq ");
//                        sb.append(" AND inspectionItem.taskCategory1 = :taskCategory1 ");
//                        sb.append(" AND inspectionItem.taskCategory2 = :taskCategory2 ");
//                        sb.append(" AND inspectionItem.taskCategory3 = :taskCategory3 ");
//                        
//                        Query updateQuery = entityManager.createQuery(sb.toString());
//                        updateQuery.setParameter("seq", Integer.parseInt(aInspectionItemVo.getItemSeq()));
//                        updateQuery.setParameter("taskCategory1", Integer.parseInt(aInspectionItemVo.getTaskCategory1()));
//                        updateQuery.setParameter("taskCategory2", Integer.parseInt(aInspectionItemVo.getTaskCategory2()));
//                        updateQuery.setParameter("taskCategory3", Integer.parseInt(aInspectionItemVo.getTaskCategory3()));
//                        updateQuery.setParameter("updateUserUuid", loginUser.getUserUuid());
//                        updateQuery.setParameter("updateDate", new Date());
//                        updateQuery.executeUpdate();
                            machineInspectionItemVos.remove(aInspectionItemVo);
                        }
                        
                    } else {
                        basicResponse.setError(true);
                        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                        basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
                        return basicResponse;
                    }
                }
            }

            for (int i = machineInspectionItemVos.size() - 1; i >= 0; i--) {
                MstMachineInspectionItemVo aInspectionItemVo = machineInspectionItemVos.get(i);
                if (null == aInspectionItemVo) {

                    continue;
                }
                MstMachineInspectionItem machineInspectionItem;
                if ("1".equals(aInspectionItemVo.getOperationFlag()) == false) {

                    if (null == aInspectionItemVo.getItemId() || aInspectionItemVo.getItemId().equals("")) {
                        //new
                        machineInspectionItem = new MstMachineInspectionItem();
                        machineInspectionItem.setId(IDGenerator.generate());
                        machineInspectionItem.setTaskCategory1(Integer.parseInt(aInspectionItemVo.getTaskCategory1()));
                        machineInspectionItem.setTaskCategory2(Integer.parseInt(aInspectionItemVo.getTaskCategory2()));
                        machineInspectionItem.setTaskCategory3(Integer.parseInt(aInspectionItemVo.getTaskCategory3()));
                        machineInspectionItem.setCreateDate(new Date());
                        machineInspectionItem.setCreateDateUuid(loginUser.getUserUuid());
                    } else {
                        //modify
                        machineInspectionItem = entityManager.find(MstMachineInspectionItem.class, aInspectionItemVo.getItemId());
                    }
                    machineInspectionItem.setInspectionItemName(aInspectionItemVo.getInspectionItemName());
                    machineInspectionItem.setResultType(Integer.parseInt(aInspectionItemVo.getResultType()));
                    machineInspectionItem.setUpdateDate(new Date());
                    machineInspectionItem.setUpdateUserUuid(loginUser.getUserUuid());
                    if (null != aInspectionItemVo.getItemSeq() && !aInspectionItemVo.getItemSeq().trim().equals("")) {
//                        int newSeq = Integer.parseInt(aInspectionItemVo.getItemSeq()) - delCount;
//                        machineInspectionItem.setSeq(0 == newSeq ? 1 : newSeq);
                        machineInspectionItem.setSeq(Integer.parseInt(aInspectionItemVo.getItemSeq()));
                    }

                    machineInspectionItem.setExternalFlg(0);
                    if (null == aInspectionItemVo.getItemId() || aInspectionItemVo.getItemId().equals("")) {
                        entityManager.persist(machineInspectionItem);
                    } else {

                        StringBuilder sb = new StringBuilder("");
                        sb.append(" UPDATE MstMachineInspectionItem inspectionItem SET inspectionItem.seq = :seq ");
                        sb.append(" , inspectionItem.updateUserUuid = :updateUserUuid , inspectionItem.updateDate = :updateDate ");
                        sb.append(" WHERE inspectionItem.id = :id ");
                        
                        Query updateQuery = entityManager.createQuery(sb.toString());
                        updateQuery.setParameter("updateUserUuid", loginUser.getUserUuid());
                        updateQuery.setParameter("updateDate", new Date());
//                        int newSeq = Integer.parseInt(aInspectionItemVo.getItemSeq()) - delCount;
//                        updateQuery.setParameter("seq", 0 == newSeq ? 1 : newSeq);
                        updateQuery.setParameter("seq",Integer.parseInt(aInspectionItemVo.getItemSeq()));
                        updateQuery.setParameter("id", aInspectionItemVo.getItemId());
                        updateQuery.executeUpdate();

                        if (null != aInspectionItemVo.getOperationFlag() && aInspectionItemVo.getOperationFlag().equals("1")) {
                            Query delQuery = entityManager.createNamedQuery("MstMachineInspectionChoice.deleteByInspectionItemId");
                            delQuery.setParameter("inspectionItemId", aInspectionItemVo.getItemId());
                            delQuery.executeUpdate();
                        }
                    }

                    miItemVosMap.put("" + machineInspectionItem.getSeq(), machineInspectionItem);
                }
            }
        }

        if (null != machineInspectionItemVo.getMstMachineInspectionChoiceVo() && machineInspectionItemVo.getMstMachineInspectionChoiceVo().isEmpty() == false) {
            MstMachineInspectionItem item = (MstMachineInspectionItem) miItemVosMap.get(machineInspectionItemVo.getMstMachineInspectionChoiceVo().get(0).getInspectionItemSeq());

            if (null != item && null != item.getResultType() && item.getResultType() == 3) { // had MstMachineInspectionChoices
                //remove old
                Query delChoices = entityManager.createNamedQuery("MstMachineInspectionChoice.deleteByInspectionItemId");
                delChoices.setParameter("inspectionItemId", item.getId());
                delChoices.executeUpdate();

                //add new
                for (int i = 0, tmpSeq = 1; i < machineInspectionItemVo.getMstMachineInspectionChoiceVo().size(); i++, tmpSeq++) {
                    MstMachineInspectionChoiceVo aVo = machineInspectionItemVo.getMstMachineInspectionChoiceVo().get(i);
                    if (aVo.getOperationFlag().equals("1") || null == aVo.getInspectionItemSeq() || aVo.getInspectionItemSeq().equals("") || null == aVo.getChoice() || aVo.getChoice().trim().equals("")) {
                        tmpSeq--;
                        continue;
                    }
                    MstMachineInspectionChoice newChoice = new MstMachineInspectionChoice();
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
     * バッチで設備点検項目マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstMachineInspectionItemList getExtMachineInspectionItemsByBatch(String latestExecutedDate) {
        MstMachineInspectionItemList resList = new MstMachineInspectionItemList();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstMachineInspectionItem t WHERE 1=1 and t.externalFlg = 0 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMachineInspectionItem> mstMachineInspectionItemsList = query.getResultList();

        resList.setMstMachineInspectionItems(mstMachineInspectionItemsList);
        return resList;
    }

    /**
     * バッチで設備点検項目マスタデータを更新
     *
     * @param machineInspectionItems
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineInspectionItemsByBatch(List<MstMachineInspectionItem> machineInspectionItems) {
        BasicResponse response = new BasicResponse();
        if (machineInspectionItems != null && !machineInspectionItems.isEmpty()) {
            for (MstMachineInspectionItem aMachineInspectionItem : machineInspectionItems) {
                MstMachineInspectionItem newMachineInspectionItem;
                List<MstMachineInspectionItem> oldMachineInspectionItems = entityManager.createQuery("SELECT t FROM MstMachineInspectionItem t WHERE 1=1 and t.id=:id and t.externalFlg = 1 ")
                        .setParameter("id", aMachineInspectionItem.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMachineInspectionItems && !oldMachineInspectionItems.isEmpty()) {
                    newMachineInspectionItem = oldMachineInspectionItems.get(0);
                } else {
                    newMachineInspectionItem = new MstMachineInspectionItem();
                }

                newMachineInspectionItem.setExternalFlg(1);
                newMachineInspectionItem.setTaskCategory1(aMachineInspectionItem.getTaskCategory1());
                newMachineInspectionItem.setTaskCategory2(aMachineInspectionItem.getTaskCategory2());
                newMachineInspectionItem.setTaskCategory3(aMachineInspectionItem.getTaskCategory3());
                newMachineInspectionItem.setSeq(aMachineInspectionItem.getSeq());
                newMachineInspectionItem.setInspectionItemName(aMachineInspectionItem.getInspectionItemName());
                newMachineInspectionItem.setResultType(aMachineInspectionItem.getResultType());
                newMachineInspectionItem.setCreateDate(aMachineInspectionItem.getCreateDate());
                newMachineInspectionItem.setCreateDateUuid(aMachineInspectionItem.getCreateDateUuid());
                newMachineInspectionItem.setUpdateDate(aMachineInspectionItem.getUpdateDate());
                newMachineInspectionItem.setUpdateUserUuid(aMachineInspectionItem.getUpdateUserUuid());

                if (null != oldMachineInspectionItems && !oldMachineInspectionItems.isEmpty()) {
                    entityManager.merge(newMachineInspectionItem);//更新
                } else {
                    newMachineInspectionItem.setId(aMachineInspectionItem.getId());//追加
                    entityManager.persist(newMachineInspectionItem);
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
    public boolean getMstMachineInspectionItemFKCheck(String id) {

        
        boolean flg = false;

        //tbl_machine_inspection_result	FK_TBL_MACHINE_INSPECTION_RESULT_INSPECTION_ITEM_ID	NO ACTION
        //tbl_machine_remodeling_inspection_result  FK_TBL_MACHINE_REMODELING_INSPECTION_RESULT_INSPECTION_ITEM_ID	NO ACTION
        if (!flg) {
            Query queryMstMachineAttribute = entityManager.createNamedQuery("TblMachineInspectionResult.findByInspectionItemId");
            queryMstMachineAttribute.setParameter("inspectionItemId", id);
            flg = queryMstMachineAttribute.getResultList().size() > 0;
        }
        
        if (!flg) {
            Query queryMstMachineAttribute = entityManager.createNamedQuery("TblMachineRemodelingInspectionResult.findByInspectionItemId");
            queryMstMachineAttribute.setParameter("inspectionItemId", id);
            flg = queryMstMachineAttribute.getResultList().size() > 0;
        }

        return flg;
    }
    
}
