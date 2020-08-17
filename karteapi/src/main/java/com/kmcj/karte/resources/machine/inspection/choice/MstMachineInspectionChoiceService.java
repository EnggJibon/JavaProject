package com.kmcj.karte.resources.machine.inspection.choice;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.batch.externalmachine.choice.ExtMstMachineInspectionChoice;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItem;
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
 * @author jiangxs
 */
@Dependent
public class MstMachineInspectionChoiceService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

   
    /**
     * 点検項目設定
     * 選択された点検項目の結果種別が、任意選択式のとき、点検項目選択肢マスタより該当の結果値を取得し、右側の点検項目選択肢一覧に連番順に表示する
     * @param inspectionItemId
     * @param loginUser
     * @return
     */
    public MstMachineInspectionChoiceVo getInspectionChoicesByItemId(String inspectionItemId, LoginUser loginUser) {
        MstMachineInspectionChoiceVo resVo = new MstMachineInspectionChoiceVo();
        List<MstMachineInspectionChoiceVo> machineInspectionChoiceVos = new ArrayList<>();
        Query query = entityManager.createNamedQuery("MstMachineInspectionChoice.findByInspectionItemId");
        query.setParameter("inspectionItemId", inspectionItemId);

        List<MstMachineInspectionChoice> machineInspectionItems = query.getResultList();
        if (null != machineInspectionItems && !machineInspectionItems.isEmpty()) {
            for (int i = 0; i < machineInspectionItems.size(); i++) {
                MstMachineInspectionChoice aMachineInspectionItem = machineInspectionItems.get(i);
                MstMachineInspectionChoiceVo aVo = new MstMachineInspectionChoiceVo();

                aVo.setId(aMachineInspectionItem.getId());
                aVo.setChoice(aMachineInspectionItem.getChoice());
                aVo.setSeq("" + aMachineInspectionItem.getSeq());

                machineInspectionChoiceVos.add(aVo);
            }
        }

        resVo.setMachineInspectionChoiceVos(machineInspectionChoiceVos);
        return resVo;
    }
    
    
    /**
     * バッチで設備点検項目選択肢マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstMachineInspectionChoiceVo getExtMachineInspectionChoicesByBatch(String latestExecutedDate) {
        MstMachineInspectionChoiceVo resVo = new MstMachineInspectionChoiceVo();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstMachineInspectionChoice t WHERE 1=1 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMachineInspectionChoice> resList = query.getResultList();
        for (MstMachineInspectionChoice mstMoldInspectionChoice : resList) {
            mstMoldInspectionChoice.setMstMachineInspectionItem(null);
        }
        resVo.setMachineInspectionChoices(resList);
        return resVo;
    }

    /**
     * バッチで設備点検項目選択肢マスタデータを更新
     *
     * @param machineInspectionChoices
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineInspectionChoicesByBatch(List<MstMachineInspectionChoice> machineInspectionChoices) {
        BasicResponse response = new BasicResponse();
        if (machineInspectionChoices != null && !machineInspectionChoices.isEmpty()) {

            for (MstMachineInspectionChoice aMachineInspectionChoice : machineInspectionChoices) {
                ExtMstMachineInspectionChoice newMachineInspectionChoice = null;
                List<ExtMstMachineInspectionChoice> oldMachineInspectionItems = entityManager.createQuery("SELECT t FROM ExtMstMachineInspectionChoice t WHERE t.id=:id ")
                        .setParameter("id", aMachineInspectionChoice.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMachineInspectionItems && !oldMachineInspectionItems.isEmpty()) {
                    newMachineInspectionChoice = oldMachineInspectionItems.get(0);
                } else {
                    newMachineInspectionChoice = new ExtMstMachineInspectionChoice();
                    newMachineInspectionChoice.setId(aMachineInspectionChoice.getId());
                }
                
                MstMachineInspectionItem machineInspectionItem = entityManager.find(MstMachineInspectionItem.class, aMachineInspectionChoice.getInspectionItemId());
                if (null == machineInspectionItem){
                    continue;
                }
                newMachineInspectionChoice.setInspectionItemId(aMachineInspectionChoice.getInspectionItemId());
                newMachineInspectionChoice.setSeq(aMachineInspectionChoice.getSeq());
                newMachineInspectionChoice.setChoice(aMachineInspectionChoice.getChoice());

                newMachineInspectionChoice.setCreateDate(aMachineInspectionChoice.getCreateDate());
                newMachineInspectionChoice.setCreateUserUuid(aMachineInspectionChoice.getCreateUserUuid());
                newMachineInspectionChoice.setUpdateDate(aMachineInspectionChoice.getUpdateDate());
                newMachineInspectionChoice.setUpdateUserUuid(aMachineInspectionChoice.getUpdateUserUuid());

                if (null != oldMachineInspectionItems && !oldMachineInspectionItems.isEmpty()) {
                    entityManager.merge(newMachineInspectionChoice);
                } else {
                    entityManager.persist(newMachineInspectionChoice);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
