/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.remodeling.inspection;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.batch.externalmachine.choice.ExtMstMachineInspectionChoice;
import com.kmcj.karte.batch.externalmachine.choice.ExtMstMachineInspectionChoiceService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.inspection.choice.MstMachineInspectionChoice;
import com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItem;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetail;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
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
 * @author jiangxs
 */
@Dependent
public class TblMachineRemodelingInspectionResultService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private ExtMstMachineInspectionChoiceService extMstMachineInspectionChoiceService;
    
    /**
     * 設備改造詳細 点検情報を取得
     * @param mainte_DetailId
     * @param machineId
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMachineRemodelingInspectionResultVo getInspectionResultsByMaintenanceId(String mainte_DetailId, String machineId, LoginUser loginUser) {
        TblMachineRemodelingInspectionResultVo resVo = new TblMachineRemodelingInspectionResultVo();
        Query query = entityManager.createNamedQuery("TblMachineRemodelingInspectionResult.findByRemodelingDetailId");
        query.setParameter("remodelingDetailId", mainte_DetailId);
        List<TblMachineRemodelingInspectionResult> mirs = query.getResultList();
        List<TblMachineRemodelingInspectionResultVo> mirVos = new ArrayList<>();
        if (null != mirs && !mirs.isEmpty()){
            boolean isExtDate = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", loginUser).isError();
            for (TblMachineRemodelingInspectionResult mir : mirs) {
                TblMachineRemodelingInspectionResultVo mirVo = new TblMachineRemodelingInspectionResultVo();
                mirVo.setId(mir.getId());
                mirVo.setRemodelingDetailId(mir.getTblMachineRemodelingInspectionResultPK().getRemodelingDetailId());
                MstMachineInspectionItem item = mir.getMstMachineInspectionItem();
                if ( isExtDate && item.getResultType() == 3) {
                    if (null != mir.getMstMachineInspectionItem()) {
                        //TODO
                        List<ExtMstMachineInspectionChoice> choices = extMstMachineInspectionChoiceService.getExtMstMachineInspectionChoiceByItemId(mir.getMstMachineInspectionItem().getId());
                        for (ExtMstMachineInspectionChoice choice : choices) {
                            if (choice != null) {
                                String choiceSeq = String.valueOf(choice.getSeq());
                                if (choiceSeq != null) {
                                    if (choiceSeq.equals(mir.getInspectionResultText())) {
                                        mirVo.setInspectionResultText(choice.getChoice());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    mirVo.setInspectionResult(mir.getInspectionResult());
                    //mirVo.setInspectionResultText(mir.getInspectionResultText());
                } else {
                    mirVo.setInspectionResult(mir.getInspectionResult());
                    mirVo.setInspectionResultText(mir.getInspectionResultText());
                }

                mirVo.setMstMachineInspectionItemId("" + item.getId());
                mirVo.setMstMachineinspectionItemName(item.getInspectionItemName());
                mirVo.setResultType("" + item.getResultType());
                
                mirVo.setSeq(mir.getTblMachineRemodelingInspectionResultPK().getSeq());
                mirVos.add(mirVo);
            }
        }
        
        resVo.setMachineRemodelingInspectionResultVo(mirVos);
        return resVo;
    }
    
    
    /**
     * 設備改造詳細 点検情報を取得
     * 設備改造詳細一覧グリッドの一行目を選択し、該当する点検結果があれば、表示する。
     * @param mainte_DetailId
     * @param loginUser
     * @return 
     */
    public TblMachineRemodelingInspectionResultVo getInspectionResult(String mainte_DetailId, LoginUser loginUser) {
        TblMachineRemodelingInspectionResultVo resVo = new TblMachineRemodelingInspectionResultVo();
        Query query = entityManager.createNamedQuery("TblMachineRemodelingInspectionResult.findByRemodelingDetailId");
        query.setParameter("remodelingDetailId", mainte_DetailId);
        List<TblMachineRemodelingInspectionResult> mirs = query.getResultList();
        List<TblMachineRemodelingInspectionResultVo> mirVos = new ArrayList<>();
        if (null != mirs && !mirs.isEmpty()){
            for (TblMachineRemodelingInspectionResult mir : mirs) {
                TblMachineRemodelingInspectionResultVo mirVo = new TblMachineRemodelingInspectionResultVo();
                mirVo.setId(mir.getId());
                if (null != mir.getInspectionResult() && !"".equals(mir.getInspectionResult())) {
                    if ("3".equals(mir.getInspectionResult())) {
                        TblMachineRemodelingDetail aDetail = (TblMachineRemodelingDetail) entityManager.createNamedQuery("TblMachineRemodelingDetail.findById").setParameter("id", mainte_DetailId).getSingleResult();
                        String machineId = aDetail.getTblMachineMaintenanceRemodeling().getMstMachine().getMachineId();
                        String companyId = aDetail.getTblMachineMaintenanceRemodeling().getMstMachine().getCompanyId();

                        MstMachineInspectionItem mstMachineInspectionItem = mir.getMstMachineInspectionItem();
                        if (mstMachineInspectionItem != null) {
                            mirVo.setInspectionResult(mir.getInspectionResult());
                            if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", companyId, loginUser).isError() == true) {
//                            aVo.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_remodeling.mainte_type", String.valueOf(aMaintenanceRemodeling.getMainteType()), loginUser.getLangId()));
                                List<ExtMstMachineInspectionChoice> choices = extMstMachineInspectionChoiceService.getExtMstMachineInspectionChoiceByItemId(mstMachineInspectionItem.getId());
                                for (ExtMstMachineInspectionChoice choice : choices) {
                                    if (choice != null) {
                                        String choiceSeq = String.valueOf(choice.getSeq());
                                        if (choiceSeq != null) {
                                            if (choiceSeq.equals(mir.getInspectionResultText())) {
                                                mirVo.setInspectionResultText(choice.getChoice());
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else {
                                StringBuilder sql = new StringBuilder("SELECT m FROM MstMachineInspectionChoice m WHERE 1=1 ");
                                sql.append(" And m.inspectionItemId = :inspectionItemId ");
                                Query queryChoice = entityManager.createQuery(sql.toString());
                                queryChoice.setParameter("inspectionItemId", mstMachineInspectionItem.getId());
                                List<MstMachineInspectionChoice> list = queryChoice.getResultList();
                                for (int i=0; i<list.size(); i++) {
                                    MstMachineInspectionChoice mstMachineInspectionChoice = list.get(i);
                                    if (mstMachineInspectionChoice != null) {
                                        String choiceSeq = String.valueOf(mstMachineInspectionChoice.getSeq());
                                        if (choiceSeq != null) {
                                            if (choiceSeq.equals(mir.getInspectionResultText())) {
                                                mirVo.setInspectionResultText(mstMachineInspectionChoice.getChoice());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                        } else {
                            mirVo.setInspectionResult(mir.getInspectionResult());
                            mirVo.setInspectionResultText("");
                        }
                    } else {
                        mirVo.setInspectionResult(mir.getInspectionResult());
                        mirVo.setInspectionResultText(mir.getInspectionResultText());
                    }
                }
                
                
                MstMachineInspectionItem item = mir.getMstMachineInspectionItem();
                mirVo.setMstMachineInspectionItemId("" + item.getId());
                mirVo.setMstMachineinspectionItemName(item.getInspectionItemName());
                mirVo.setResultType("" + item.getResultType());
                
                mirVo.setSeq(mir.getTblMachineRemodelingInspectionResultPK().getSeq());
                mirVos.add(mirVo);
            }
        }
        
        resVo.setMachineRemodelingInspectionResultVo(mirVos);
        return resVo;
    }
    
    
    
    /**
     * 設備メンテナンス詳細 全部点検情報を取得
     * 点検項目マスタから作業大分類、作業中分類、作業小分類に該当する点検項目が定義されているか検索し、
     * 存在すれば、点検結果一覧に点検項目を表示する。
     * @param taskCategory1
     * @param taskCategory2
     * @param loginUser
     * @param taskCategory3
     * @return 
     */
    public TblMachineRemodelingInspectionResultVo getInspectionResultShow(String taskCategory1, String taskCategory2, String taskCategory3, LoginUser loginUser) {
        
        StringBuilder sql = new StringBuilder("SELECT m FROM MstMachineInspectionItem m WHERE 1 = 1 ");
        if (null != taskCategory1 && !taskCategory1.trim().equals("") && !taskCategory1.trim().equals("null")) {
            sql.append(" and m.taskCategory1 = :taskCategory1 ");
            if (null != taskCategory2 && !taskCategory2.trim().equals("") && !taskCategory2.trim().equals("null")) {
                sql.append(" and m.taskCategory2 = :taskCategory2 ");
                if (null != taskCategory3 && !taskCategory3.trim().equals("") && !taskCategory3.trim().equals("null")) {
                    sql.append(" and m.taskCategory3 = :taskCategory3 ");
                }
            }
        }
        sql.append(" and m.externalFlg = 0 ");
        sql.append(" order by m.seq ");
        Query query = entityManager.createQuery(sql.toString());
        if (null != taskCategory1 && !taskCategory1.trim().equals("") && !taskCategory1.trim().equals("null")) {
            query.setParameter("taskCategory1", Integer.parseInt(taskCategory1));
            if (null != taskCategory2 && !taskCategory2.trim().equals("") && !taskCategory2.trim().equals("null")) {
                query.setParameter("taskCategory2", Integer.parseInt(taskCategory2));
                if (null != taskCategory3 && !taskCategory3.trim().equals("") && !taskCategory3.trim().equals("null")) {
                    query.setParameter("taskCategory3", Integer.parseInt(taskCategory3));
                }
            }
        }
        List<MstMachineInspectionItem> mii = query.getResultList();
        
        TblMachineRemodelingInspectionResultVo resVo = new TblMachineRemodelingInspectionResultVo();

        List<TblMachineRemodelingInspectionResultVo> mirVos = new ArrayList<>();
        if (mii != null && !mii.isEmpty()) {
            TblMachineRemodelingInspectionResultVo mirVo;
            for (MstMachineInspectionItem miis : mii) {
                mirVo = new TblMachineRemodelingInspectionResultVo();
                mirVo.setMstMachineInspectionItemId(miis.getId());
                mirVo.setSeq(miis.getSeq());
                mirVo.setMstMachineinspectionItemName(miis.getInspectionItemName());
                if (null != miis.getResultType()) {
                    mirVo.setResultType(String.valueOf(miis.getResultType()));
                } else {
                    mirVo.setResultType("");
                }
                mirVo.setInspectionResultText("");
                mirVos.add(mirVo);
            }
        }
        resVo.setMachineRemodelingInspectionResultVo(mirVos);
        return resVo;
    }
    
    
    /**
     * バッチで設備点検结果データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public TblMachineRemodelingInspectionResultVo getExtMachineRemodelingInspectionResultsByBatch(String latestExecutedDate) {
        TblMachineRemodelingInspectionResultVo resList = new TblMachineRemodelingInspectionResultVo();
        List<TblMachineRemodelingInspectionResultVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblMachineRemodelingInspectionResult t WHERE 1=1 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMachineRemodelingInspectionResult> tmpList = query.getResultList();
        for (TblMachineRemodelingInspectionResult machineInspectionResult : tmpList) {
            TblMachineRemodelingInspectionResultVo aRes = new TblMachineRemodelingInspectionResultVo();
            machineInspectionResult.setMstMachineInspectionItem(null);
            
            aRes.setTblMachineRemodelingInspectionResult(machineInspectionResult);
            resVo.add(aRes);
        }
        resList.setMachineRemodelingInspectionResultVo(resVo);
        return resList;
    }
    
    
    /**
     * バッチで設備点検结果データを更新、ＤＢを更新する
     *
     * @param machineRemodelingInspectionResultVo
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineRemodelingInspectionResultsByBatch(List<TblMachineRemodelingInspectionResultVo> machineRemodelingInspectionResultVo) {
        BasicResponse response = new BasicResponse();

        if (null != machineRemodelingInspectionResultVo && !machineRemodelingInspectionResultVo.isEmpty()) {
            for (TblMachineRemodelingInspectionResultVo aMachineInspectionResult : machineRemodelingInspectionResultVo) {
                List<TblMachineRemodelingDetail> remodelingDetails = entityManager.createQuery("select t.id from TblMachineRemodelingDetail t where t.id = :id ").setParameter("id", aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getTblMachineRemodelingInspectionResultPK().getRemodelingDetailId()).getResultList();
                if (null == remodelingDetails || remodelingDetails.isEmpty()) {
                    continue;
                }
                List<MstMachineInspectionItem> machineInspectionItems = entityManager.createQuery("select t.id from MstMachineInspectionItem t where t.id = :id ").setParameter("id", aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getTblMachineRemodelingInspectionResultPK().getInspectionItemId()).getResultList();
                if (null == machineInspectionItems || machineInspectionItems.isEmpty()) {
                    continue;
                }

                TblMachineRemodelingInspectionResult newMachineInspectionResult;
                List<TblMachineRemodelingInspectionResult> oldMachineInspectionResults = entityManager.createQuery("SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.tblMachineRemodelingInspectionResultPK.inspectionItemId = :inspectionItemId and t.tblMachineRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId and t.tblMachineRemodelingInspectionResultPK.seq = :seq ")
                        .setParameter("inspectionItemId", aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getTblMachineRemodelingInspectionResultPK().getInspectionItemId())
                        .setParameter("remodelingDetailId", aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getTblMachineRemodelingInspectionResultPK().getRemodelingDetailId())
                        .setParameter("seq", aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getTblMachineRemodelingInspectionResultPK().getSeq())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMachineInspectionResults && !oldMachineInspectionResults.isEmpty()) {
                    newMachineInspectionResult = oldMachineInspectionResults.get(0);
                } else {
                    newMachineInspectionResult = new TblMachineRemodelingInspectionResult();
                    TblMachineRemodelingInspectionResultPK pk = new TblMachineRemodelingInspectionResultPK();
                    pk.setInspectionItemId(aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getTblMachineRemodelingInspectionResultPK().getInspectionItemId());
                    pk.setRemodelingDetailId(aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getTblMachineRemodelingInspectionResultPK().getRemodelingDetailId());
                    pk.setSeq(aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getTblMachineRemodelingInspectionResultPK().getSeq());
                    newMachineInspectionResult.setTblMachineRemodelingInspectionResultPK(pk);
                }

                newMachineInspectionResult.setInspectionResult(aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getInspectionResult());
                newMachineInspectionResult.setInspectionResultText(aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getInspectionResultText());

                newMachineInspectionResult.setCreateDate(aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getCreateDate());
                newMachineInspectionResult.setCreateUserUuid(aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getCreateUserUuid());
                newMachineInspectionResult.setUpdateDate(new Date());
                newMachineInspectionResult.setUpdateUserUuid(aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getUpdateUserUuid());

                if (null != oldMachineInspectionResults && !oldMachineInspectionResults.isEmpty()) {
                    entityManager.merge(newMachineInspectionResult);//更新
                } else {
                    newMachineInspectionResult.setId(aMachineInspectionResult.getTblMachineRemodelingInspectionResult().getId());//追加
                    entityManager.persist(newMachineInspectionResult);
                }
            }
        }
        response.setError(false);
        return response;
    }
    
}
