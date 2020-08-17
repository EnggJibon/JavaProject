/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.remodeling.inspection;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.batch.externalmold.choice.ExtMstMoldInspectionChoice;
import com.kmcj.karte.batch.externalmold.choice.ExtMstMoldInspectionChoiceService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoice;
import com.kmcj.karte.resources.mold.inspection.item.MstMoldInspectionItem;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetail;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
public class TblMoldRemodelingInspectionResultService {
    
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private ExtMstMoldInspectionChoiceService extMstMoldInspectionChoiceService;
    
    /**
     * 金型改造詳細 点検情報を取得
     * @param mainte_DetailId
     * @param moldId
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMoldRemodelingInspectionResultVo getInspectionResultsByMaintenanceId(String mainte_DetailId, String moldId, LoginUser loginUser) {
        TblMoldRemodelingInspectionResultVo resVo = new TblMoldRemodelingInspectionResultVo();
        Query query = entityManager.createNamedQuery("TblMoldRemodelingInspectionResult.findByRemodelingDetailId");
        query.setParameter("remodelingDetailId", mainte_DetailId);
        List<TblMoldRemodelingInspectionResult> mirs = query.getResultList();
        List<TblMoldRemodelingInspectionResultVo> mirVos = new ArrayList<>();
        if (null != mirs && !mirs.isEmpty()){
            boolean isExtDate = FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser).isError();
            for (TblMoldRemodelingInspectionResult mir : mirs) {
                TblMoldRemodelingInspectionResultVo mirVo = new TblMoldRemodelingInspectionResultVo();
                mirVo.setId(mir.getId());
                mirVo.setRemodelingDetailId(mir.getTblMoldRemodelingInspectionResultPK().getRemodelingDetailId());
                MstMoldInspectionItem item = mir.getMstMoldInspectionItem();
                if ( isExtDate && item.getResultType() == 3) {
                    if (null != mir.getMstMoldInspectionItem()) {
                        //TODO
                        List<ExtMstMoldInspectionChoice> choices = extMstMoldInspectionChoiceService.getExtMstMoldInspectionChoiceByItemId(mir.getMstMoldInspectionItem().getId());
                        for (ExtMstMoldInspectionChoice choice : choices) {
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

                mirVo.setInspectionItemId("" + item.getId());
                mirVo.setInspectionItemName(item.getInspectionItemName());
                mirVo.setResultType("" + item.getResultType());
                
                mirVo.setSeq(mir.getTblMoldRemodelingInspectionResultPK().getSeq());
                mirVos.add(mirVo);
            }
        }
        
        resVo.setMoldRemodelingInspectionResultVos(mirVos);
        return resVo;
    }
    
    
    
    /**
     * 金型改造詳細 点検情報を取得
     * 金型改造詳細一覧グリッドの一行目を選択し、該当する点検結果があれば、表示する。
     * @param mainte_DetailId
     * @param loginUser
     * @return 
     */
    public TblMoldRemodelingInspectionResultVo getInspectionResult(String mainte_DetailId, LoginUser loginUser) {
        TblMoldRemodelingInspectionResultVo resVo = new TblMoldRemodelingInspectionResultVo();
        Query query = entityManager.createNamedQuery("TblMoldRemodelingInspectionResult.findByRemodelingDetailId");
        query.setParameter("remodelingDetailId", mainte_DetailId);
        List<TblMoldRemodelingInspectionResult> mirs = query.getResultList();
        List<TblMoldRemodelingInspectionResultVo> mirVos = new ArrayList<>();
        if (null != mirs && !mirs.isEmpty()){
            for (TblMoldRemodelingInspectionResult mir : mirs) {
                TblMoldRemodelingInspectionResultVo mirVo = new TblMoldRemodelingInspectionResultVo();
                mirVo.setId(mir.getId());
                if (null != mir.getInspectionResult() && !"".equals(mir.getInspectionResult())) {
                    if ("3".equals(mir.getInspectionResult())) {
                        TblMoldRemodelingDetail aDetail = (TblMoldRemodelingDetail) entityManager.createNamedQuery("TblMoldRemodelingDetail.findById").setParameter("id", mainte_DetailId).getSingleResult();
                        String machineId = aDetail.getTblMoldMaintenanceRemodeling().getMstMold().getMoldId();
//                        String companyId = aDetail.getTblMoldMaintenanceRemodeling().getMstMold().getCompanyId();

                        MstMoldInspectionItem mstMoldInspectionItem = mir.getMstMoldInspectionItem();
                        if (mstMoldInspectionItem != null) {
                            mirVo.setInspectionResult(mir.getInspectionResult());
                            if (FileUtil.checkExternal(entityManager, mstDictionaryService, machineId, loginUser).isError() == true) {
//                            aVo.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_remodeling.mainte_type", String.valueOf(aMaintenanceRemodeling.getMainteType()), loginUser.getLangId()));
                                List<ExtMstMoldInspectionChoice> choices = extMstMoldInspectionChoiceService.getExtMstMoldInspectionChoiceByItemId(mstMoldInspectionItem.getId());
                                for (ExtMstMoldInspectionChoice choice : choices) {
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
                                Iterator<MstMoldInspectionChoice> mstMachineInspectionChoices = mstMoldInspectionItem.getMstMoldInspectionChoiceCollection().iterator();
                                while (mstMachineInspectionChoices.hasNext()) {
                                    MstMoldInspectionChoice mstMachineInspectionChoice = mstMachineInspectionChoices.next();
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
                
                
                MstMoldInspectionItem item = mir.getMstMoldInspectionItem();
                mirVo.setInspectionItemId("" + item.getId());
                mirVo.setInspectionItemName(item.getInspectionItemName());
                mirVo.setResultType("" + item.getResultType());
                
                mirVo.setSeq(mir.getTblMoldRemodelingInspectionResultPK().getSeq());
                mirVos.add(mirVo);
            }
        }
        
        resVo.setMoldRemodelingInspectionResultVos(mirVos);
        return resVo;
    }
    
    
    /**
     * 金型メンテナンス詳細 全部点検情報を取得
     * 点検項目マスタから作業大分類、作業中分類、作業小分類に該当する点検項目が定義されているか検索し、
     * 存在すれば、点検結果一覧に点検項目を表示する。
     * @param taskCategory1
     * @param taskCategory2
     * @param loginUser
     * @param taskCategory3
     * @return 
     */
    public TblMoldRemodelingInspectionResultVo getInspectionResultShow(String taskCategory1, String taskCategory2, String taskCategory3, LoginUser loginUser) {
        
        StringBuilder sql = new StringBuilder("SELECT m FROM MstMoldInspectionItem m WHERE 1 = 1 ");
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
        List<MstMoldInspectionItem> mii = query.getResultList();
        
        TblMoldRemodelingInspectionResultVo resVo = new TblMoldRemodelingInspectionResultVo();

        List<TblMoldRemodelingInspectionResultVo> mirVos = new ArrayList<>();
        if (mii != null && !mii.isEmpty()) {
            TblMoldRemodelingInspectionResultVo mirVo;
            for (MstMoldInspectionItem miis : mii) {
                mirVo = new TblMoldRemodelingInspectionResultVo();
                mirVo.setInspectionItemId(miis.getId());
                mirVo.setSeq(miis.getSeq());
                mirVo.setInspectionItemName(miis.getInspectionItemName());
                if (null != miis.getResultType()) {
                    mirVo.setResultType(String.valueOf(miis.getResultType()));
                } else {
                    mirVo.setResultType("");
                }
                mirVo.setInspectionResultText("");
                mirVos.add(mirVo);
            }
        }
        resVo.setMoldRemodelingInspectionResultVos(mirVos);
        return resVo;
    }
    
    
    /**
     * バッチで金型改造点検结果データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMoldRemodelingInspectionResultVo getExtMoldRemodelingInspectionResultsByBatch(String latestExecutedDate) {
        TblMoldRemodelingInspectionResultVo resList = new TblMoldRemodelingInspectionResultVo();
        List<TblMoldRemodelingInspectionResultVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblMoldRemodelingInspectionResult t WHERE 1=1 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMoldRemodelingInspectionResult> tmpList = query.getResultList();
        for (TblMoldRemodelingInspectionResult machineInspectionResult : tmpList) {
            TblMoldRemodelingInspectionResultVo aRes = new TblMoldRemodelingInspectionResultVo();
            machineInspectionResult.setMstMoldInspectionItem(null);
            
            aRes.setTblMoldRemodelingInspectionResult(machineInspectionResult);
            resVo.add(aRes);
        }
        resList.setMoldRemodelingInspectionResultVos(resVo);
        return resList;
    }
    
    
    /**
     * バッチで金型改造点検结果データを更新、ＤＢを更新する
     *
     * @param moldRemodelingInspectionResultVo
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldRemodelingInspectionResultsByBatch(List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVo) {
        BasicResponse response = new BasicResponse();

        if (null != moldRemodelingInspectionResultVo && !moldRemodelingInspectionResultVo.isEmpty()) {
            for (TblMoldRemodelingInspectionResultVo aMoldInspectionResult : moldRemodelingInspectionResultVo) {
                List<TblMoldRemodelingDetail> remodelingDetails = entityManager.createQuery("select t.id from TblMoldRemodelingDetail t where t.id = :id ").setParameter("id", aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getTblMoldRemodelingInspectionResultPK().getRemodelingDetailId()).getResultList();
                if (null == remodelingDetails || remodelingDetails.isEmpty()) {
                    continue;
                }
                List<MstMoldInspectionItem> moldInspectionItems = entityManager.createQuery("select t.id from MstMoldInspectionItem t where t.id = :id ").setParameter("id", aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getTblMoldRemodelingInspectionResultPK().getInspectionItemId()).getResultList();
                if (null == moldInspectionItems || moldInspectionItems.isEmpty()) {
                    continue;
                }

                TblMoldRemodelingInspectionResult newMoldInspectionResult;
                List<TblMoldRemodelingInspectionResult> oldMoldInspectionResults = entityManager.createQuery("SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.tblMoldRemodelingInspectionResultPK.inspectionItemId = :inspectionItemId and t.tblMoldRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId and t.tblMoldRemodelingInspectionResultPK.seq = :seq ")
                        .setParameter("inspectionItemId", aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getTblMoldRemodelingInspectionResultPK().getInspectionItemId())
                        .setParameter("remodelingDetailId", aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getTblMoldRemodelingInspectionResultPK().getRemodelingDetailId())
                        .setParameter("seq", aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getTblMoldRemodelingInspectionResultPK().getSeq())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMoldInspectionResults && !oldMoldInspectionResults.isEmpty()) {
                    newMoldInspectionResult = oldMoldInspectionResults.get(0);
                } else {
                    newMoldInspectionResult = new TblMoldRemodelingInspectionResult();
                    TblMoldRemodelingInspectionResultPK pk = new TblMoldRemodelingInspectionResultPK();
                    pk.setInspectionItemId(aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getTblMoldRemodelingInspectionResultPK().getInspectionItemId());
                    pk.setRemodelingDetailId(aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getTblMoldRemodelingInspectionResultPK().getRemodelingDetailId());
                    pk.setSeq(aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getTblMoldRemodelingInspectionResultPK().getSeq());
                    newMoldInspectionResult.setTblMoldRemodelingInspectionResultPK(pk);
                }

                newMoldInspectionResult.setInspectionResult(aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getInspectionResult());
                newMoldInspectionResult.setInspectionResultText(aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getInspectionResultText());

                newMoldInspectionResult.setCreateDate(aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getCreateDate());
                newMoldInspectionResult.setCreateUserUuid(aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getCreateUserUuid());
                newMoldInspectionResult.setUpdateDate(new Date());
                newMoldInspectionResult.setUpdateUserUuid(aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getUpdateUserUuid());

                if (null != oldMoldInspectionResults && !oldMoldInspectionResults.isEmpty()) {
                    entityManager.merge(newMoldInspectionResult);//更新
                } else {
                    newMoldInspectionResult.setId(aMoldInspectionResult.getTblMoldRemodelingInspectionResult().getId());//追加
                    entityManager.persist(newMoldInspectionResult);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
