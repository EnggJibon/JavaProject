/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.result;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.batch.externalmold.choice.ExtMstMoldInspectionChoice;
import com.kmcj.karte.batch.externalmold.choice.ExtMstMoldInspectionChoiceService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoice;
import com.kmcj.karte.resources.mold.inspection.item.MstMoldInspectionItem;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetail;
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
public class TblMoldInspectionResultService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private ExtMstMoldInspectionChoiceService extMstMoldInspectionChoiceService;


    /**
     * T0008	金型メンテナンス詳細 点検情報を取得
     * @param mainte_DetailId
     * @param moldId
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMoldInspectionResultVo getInspectionResultsByMaintenanceId(String mainte_DetailId, String moldId, LoginUser loginUser) {
        TblMoldInspectionResultVo resVo = new TblMoldInspectionResultVo();
        Query query = entityManager.createNamedQuery("TblMoldInspectionResult.findByMaintenanceDetailId");
        query.setParameter("maintenanceDetailId", mainte_DetailId);
        List<TblMoldInspectionResult> mirs = query.getResultList();
        List<TblMoldInspectionResultVo> mirVos = new ArrayList<>();
        if (null != mirs && !mirs.isEmpty()){
            boolean isExtDate = FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser).isError();
            for (TblMoldInspectionResult mir : mirs) {
                TblMoldInspectionResultVo mirVo = new TblMoldInspectionResultVo();
                mirVo.setId(mir.getId());
                MstMoldInspectionItem item = mir.getMstMoldInspectionItem();
                if ( isExtDate && item.getResultType() == 3) {
                    if (null != mir.getMstMoldInspectionItem()) {
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
                
                mirVo.setSeq(mir.getTblMoldInspectionResultPK().getSeq());
                mirVos.add(mirVo);
            }
        }
        
        resVo.setMoldInspectionResultVos(mirVos);
        return resVo;
    }

    /**
     * T0008 金型メンテナンス詳細 点検情報を取得
     * 金型メンテナンス詳細一覧グリッドの一行目を選択し、該当する点検結果があれば、表示する。
     * @param mainte_DetailId
     * @param loginUser
     * @return 
     */
    public TblMoldInspectionResultVo getInspectionResult(String mainte_DetailId, LoginUser loginUser) {
        TblMoldInspectionResultVo resVo = new TblMoldInspectionResultVo();
        Query query = entityManager.createNamedQuery("TblMoldInspectionResult.findByMaintenanceDetailId");
        query.setParameter("maintenanceDetailId", mainte_DetailId);
        List<TblMoldInspectionResult> mirs = query.getResultList();
        List<TblMoldInspectionResultVo> mirVos = new ArrayList<>();
        if (null != mirs && !mirs.isEmpty()){
            for (TblMoldInspectionResult mir : mirs) {
                TblMoldInspectionResultVo mirVo = new TblMoldInspectionResultVo();
                mirVo.setId(mir.getId());
                if (null != mir.getInspectionResult() && !"".equals(mir.getInspectionResult())) {
                    if ("3".equals(mir.getInspectionResult())) {
                        TblMoldMaintenanceDetail aDetail = (TblMoldMaintenanceDetail) entityManager.createNamedQuery("TblMoldMaintenanceDetail.findById").setParameter("id", mainte_DetailId).getSingleResult();
                        String moldId = aDetail.getTblMoldMaintenanceRemodeling().getMstMold().getMoldId();

                        MstMoldInspectionItem mstMoldInspectionItem = mir.getMstMoldInspectionItem();
                        if (mstMoldInspectionItem != null) {
                            mirVo.setInspectionResult(mir.getInspectionResult());
                            if (FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser).isError() == true) {
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
                                Iterator<MstMoldInspectionChoice> mstMoldInspectionChoices = mstMoldInspectionItem.getMstMoldInspectionChoiceCollection().iterator();
                                while (mstMoldInspectionChoices.hasNext()) {
                                    MstMoldInspectionChoice mstMoldInspectionChoice = mstMoldInspectionChoices.next();
                                    if (mstMoldInspectionChoice != null) {
                                        String choiceSeq = String.valueOf(mstMoldInspectionChoice.getSeq());
                                        if (choiceSeq != null) {
                                            if (choiceSeq.equals(mir.getInspectionResultText())) {
                                                mirVo.setInspectionResultText(mstMoldInspectionChoice.getChoice());
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
                
                mirVo.setSeq(mir.getTblMoldInspectionResultPK().getSeq());
                mirVos.add(mirVo);
            }
        }
        
        resVo.setMoldInspectionResultVos(mirVos);
        return resVo;
    }
    
    
    /**
     * T0008 金型メンテナンス詳細 全部点検情報を取得
     * 点検項目マスタから作業大分類、作業中分類、作業小分類に該当する点検項目が定義されているか検索し、
     * 存在すれば、点検結果一覧に点検項目を表示する。
     * @param taskCategory1
     * @param taskCategory2
     * @param loginUser
     * @param taskCategory3
     * @return 
     */
    public TblMoldInspectionResultVo getInspectionResultShow(String taskCategory1, String taskCategory2, String taskCategory3, LoginUser loginUser) {
        
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
        
        TblMoldInspectionResultVo resVo = new TblMoldInspectionResultVo();

        List<TblMoldInspectionResultVo> mirVos = new ArrayList<>();
        if (mii != null && !mii.isEmpty()) {
            TblMoldInspectionResultVo mirVo;
            for (MstMoldInspectionItem miis : mii) {
                mirVo = new TblMoldInspectionResultVo();
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
        resVo.setMoldInspectionResultVos(mirVos);
        return resVo;
    }
    
    /**
     * バッチで金型点検结果データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMoldInspectionResultVo getExtMoldInspectionResultsByBatch(String latestExecutedDate) {
        TblMoldInspectionResultVo resList = new TblMoldInspectionResultVo();
        List<TblMoldInspectionResultVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblMoldInspectionResult t WHERE 1=1 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMoldInspectionResult> tmpList = query.getResultList();
        for (TblMoldInspectionResult moldInspectionResult : tmpList) {
            TblMoldInspectionResultVo aRes = new TblMoldInspectionResultVo();
            moldInspectionResult.setMstMoldInspectionItem(null);
            
            aRes.setTblMoldInspectionResult(moldInspectionResult);
            resVo.add(aRes);
        }
        resList.setMoldInspectionResultVos(resVo);
        return resList;
    }
    

    /**
     * バッチで金型点検结果データを更新、ＤＢを更新する
     *
     * @param moldInspectionResultVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldInspectionResultsByBatch(List<TblMoldInspectionResultVo> moldInspectionResultVos) {
        BasicResponse response = new BasicResponse();

        if (null != moldInspectionResultVos && !moldInspectionResultVos.isEmpty()) {
            for (TblMoldInspectionResultVo aMoldInspectionResult : moldInspectionResultVos) {
                List<TblMoldMaintenanceDetail> maintenanceDetails = entityManager.createQuery("select t.id from TblMoldMaintenanceDetail t where t.id = :id ").setParameter("id", aMoldInspectionResult.getTblMoldInspectionResult().getTblMoldInspectionResultPK().getMaintenanceDetailId()).getResultList();
                if (null == maintenanceDetails || maintenanceDetails.isEmpty()) {
                    continue;
                }
                List<MstMoldInspectionItem> moldInspectionItems = entityManager.createQuery("select t.id from MstMoldInspectionItem t where t.id = :id ").setParameter("id", aMoldInspectionResult.getTblMoldInspectionResult().getTblMoldInspectionResultPK().getInspectionItemId()).getResultList();
                if (null == moldInspectionItems || moldInspectionItems.isEmpty()) {
                    continue;
                }

                TblMoldInspectionResult newMoldInspectionResult;
                List<TblMoldInspectionResult> oldMoldInspectionResults = entityManager.createQuery("SELECT t FROM TblMoldInspectionResult t WHERE t.tblMoldInspectionResultPK.inspectionItemId = :inspectionItemId and t.tblMoldInspectionResultPK.maintenanceDetailId = :maintenanceDetailId and t.tblMoldInspectionResultPK.seq = :seq ")
                        .setParameter("inspectionItemId", aMoldInspectionResult.getTblMoldInspectionResult().getTblMoldInspectionResultPK().getInspectionItemId())
                        .setParameter("maintenanceDetailId", aMoldInspectionResult.getTblMoldInspectionResult().getTblMoldInspectionResultPK().getMaintenanceDetailId())
                        .setParameter("seq", aMoldInspectionResult.getTblMoldInspectionResult().getTblMoldInspectionResultPK().getSeq())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMoldInspectionResults && !oldMoldInspectionResults.isEmpty()) {
                    newMoldInspectionResult = oldMoldInspectionResults.get(0);
                } else {
                    newMoldInspectionResult = new TblMoldInspectionResult();
                    TblMoldInspectionResultPK pk = new TblMoldInspectionResultPK();
                    pk.setInspectionItemId(aMoldInspectionResult.getTblMoldInspectionResult().getTblMoldInspectionResultPK().getInspectionItemId());
                    pk.setMaintenanceDetailId(aMoldInspectionResult.getTblMoldInspectionResult().getTblMoldInspectionResultPK().getMaintenanceDetailId());
                    pk.setSeq(aMoldInspectionResult.getTblMoldInspectionResult().getTblMoldInspectionResultPK().getSeq());
                    newMoldInspectionResult.setTblMoldInspectionResultPK(pk);
                }

                newMoldInspectionResult.setInspectionResult(aMoldInspectionResult.getTblMoldInspectionResult().getInspectionResult());
                newMoldInspectionResult.setInspectionResultText(aMoldInspectionResult.getTblMoldInspectionResult().getInspectionResultText());

                newMoldInspectionResult.setCreateDate(aMoldInspectionResult.getTblMoldInspectionResult().getCreateDate());
                newMoldInspectionResult.setCreateUserUuid(aMoldInspectionResult.getTblMoldInspectionResult().getCreateUserUuid());
                newMoldInspectionResult.setUpdateDate(new Date());
                newMoldInspectionResult.setUpdateUserUuid(aMoldInspectionResult.getTblMoldInspectionResult().getUpdateUserUuid());

                if (null != oldMoldInspectionResults && !oldMoldInspectionResults.isEmpty()) {
                    entityManager.merge(newMoldInspectionResult);//更新
                } else {
                    newMoldInspectionResult.setId(aMoldInspectionResult.getTblMoldInspectionResult().getId());//追加
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
