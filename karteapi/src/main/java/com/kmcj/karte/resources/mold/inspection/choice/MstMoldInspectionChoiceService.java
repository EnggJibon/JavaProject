/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.choice;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.batch.externalmold.choice.ExtMstMoldInspectionChoice;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.mold.inspection.item.MstMoldInspectionItem;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author
 */
@Dependent
public class MstMoldInspectionChoiceService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     *
     * @param inspectionItemId
     * @param loginUser
     * @return
     */
    public MstMoldInspectionChoiceVo getInspectionChoicesByItemId(String inspectionItemId, LoginUser loginUser) {
        MstMoldInspectionChoiceVo resVo = new MstMoldInspectionChoiceVo();
        List<MstMoldInspectionChoiceVo> moldInspectionChoiceVos = new ArrayList<>();
        Query query = entityManager.createNamedQuery("MstMoldInspectionChoice.findByInspectionItemId");
        query.setParameter("inspectionItemId", inspectionItemId);

        List<MstMoldInspectionChoice> moldInspectionItems = query.getResultList();
        if (null != moldInspectionItems && !moldInspectionItems.isEmpty()) {
            for (int i = 0; i < moldInspectionItems.size(); i++) {
                MstMoldInspectionChoice aMoldInspectionItem = moldInspectionItems.get(i);
                MstMoldInspectionChoiceVo aVo = new MstMoldInspectionChoiceVo();

                aVo.setId(aMoldInspectionItem.getId());
                aVo.setChoice(aMoldInspectionItem.getChoice());
                aVo.setSeq("" + aMoldInspectionItem.getSeq());

                moldInspectionChoiceVos.add(aVo);
            }
        }

        resVo.setMoldInspectionChoiceVos(moldInspectionChoiceVos);
        return resVo;
    }

    /**
     * バッチで金型点検項目選択肢マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstMoldInspectionChoiceVo getExtMoldInspectionChoicesByBatch(String latestExecutedDate) {
        MstMoldInspectionChoiceVo resVo = new MstMoldInspectionChoiceVo();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstMoldInspectionChoice t WHERE 1=1 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMoldInspectionChoice> resList = query.getResultList();
        for (MstMoldInspectionChoice mstMoldInspectionChoice : resList) {
            mstMoldInspectionChoice.setMstMoldInspectionItem(null);
        }
        resVo.setMoldInspectionChoices(resList);
        return resVo;
    }

    /**
     * バッチで金型点検項目選択肢マスタデータを更新
     *
     * @param moldInspectionChoices
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldInspectionChoicesByBatch(List<MstMoldInspectionChoice> moldInspectionChoices) {
        BasicResponse response = new BasicResponse();
        if (moldInspectionChoices != null && !moldInspectionChoices.isEmpty()) {

            for (MstMoldInspectionChoice aMoldInspectionChoice : moldInspectionChoices) {
                ExtMstMoldInspectionChoice newMoldInspectionChoice = null;
                List<ExtMstMoldInspectionChoice> oldMoldInspectionItems = entityManager.createQuery("SELECT t FROM ExtMstMoldInspectionChoice t WHERE t.id=:id ")
                        .setParameter("id", aMoldInspectionChoice.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMoldInspectionItems && !oldMoldInspectionItems.isEmpty()) {
                    newMoldInspectionChoice = oldMoldInspectionItems.get(0);
                } else {
                    newMoldInspectionChoice = new ExtMstMoldInspectionChoice();
                    newMoldInspectionChoice.setId(aMoldInspectionChoice.getId());
                }
                
                MstMoldInspectionItem moldInspectionItem = entityManager.find(MstMoldInspectionItem.class, aMoldInspectionChoice.getInspectionItemId());
                if (null == moldInspectionItem){
                    continue;
                }
                newMoldInspectionChoice.setInspectionItemId(aMoldInspectionChoice.getInspectionItemId());
                newMoldInspectionChoice.setSeq(aMoldInspectionChoice.getSeq());
                newMoldInspectionChoice.setChoice(aMoldInspectionChoice.getChoice());

                newMoldInspectionChoice.setCreateDate(aMoldInspectionChoice.getCreateDate());
                newMoldInspectionChoice.setCreateUserUuid(aMoldInspectionChoice.getCreateUserUuid());
                newMoldInspectionChoice.setUpdateDate(aMoldInspectionChoice.getUpdateDate());
                newMoldInspectionChoice.setUpdateUserUuid(aMoldInspectionChoice.getUpdateUserUuid());

                if (null != oldMoldInspectionItems && !oldMoldInspectionItems.isEmpty()) {
                    entityManager.merge(newMoldInspectionChoice);
                } else {
                    entityManager.persist(newMoldInspectionChoice);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
