/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.phase;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoicePK;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.util.IDGenerator;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.language.MstLanguage;
import com.kmcj.karte.resources.language.MstLanguageList;
import com.kmcj.karte.resources.language.MstLanguageService;
import com.kmcj.karte.resources.work.phase.flow.MstWorkPhaseFlow;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * 作業工程マスタサービス
 * @author t.ariki
 */
@Dependent
public class MstWorkPhaseService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    MstDictionaryService mstDictionaryService;
    
    @Inject
    MstChoiceService mstChoiceService;
    
    @Inject
    MstLanguageService mstLanguageService;
    
//    @Inject
//    TblWorkService tblWorkService;
    
    /**
     * 作業工程マスタ取得
     * @return mstWorkPhaseList
     */
    public MstWorkPhaseList getMstWorkPhases() {
        Query query = entityManager.createNamedQuery("MstWorkPhase.findAllOrderByWorkPhaseCode");
        List<MstWorkPhase> list = query.getResultList();
        for (MstWorkPhase aWorkPhase : list) {
            List<String> ds = entityManager.createNamedQuery("MstWorkPhaseDepartment.findIdsByWorkPhaseId")
                    .setParameter("workPhaseId", aWorkPhase.getId())
                    .getResultList();
            aWorkPhase.setDepartmentIds(ds);
        }
        MstWorkPhaseList response = new MstWorkPhaseList();
        response.setMstWorkPhases(list);
        return response;
    }

    /**
     * 次の作業工程マスタ取得
     * @return mstWorkPhaseList
     */
    public MstWorkPhaseList getNextMstWorkPhases(String workPhaseId) {
        Query query = entityManager.createNamedQuery("MstWorkPhase.findNextWorkPhase");
        query.setParameter("workPhaseId", workPhaseId);
        List list = query.getResultList();
        MstWorkPhaseList response = new MstWorkPhaseList();
        response.setMstWorkPhases(list);
        return response;
    }
    
    /**
     * IDで取得
     * @param id
     * @return 
     */
    public MstWorkPhase getMstWorkPhaseById(String id) {
        Query query = entityManager.createNamedQuery("MstWorkPhase.findById");
        query.setParameter("id", id);
        try {
            return (MstWorkPhase)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Codeで取得
     * @param code
     * @return 
     */
    public MstWorkPhase getMstWorkPhaseByCode(String code) {
        Query query = entityManager.createNamedQuery("MstWorkPhase.findByWorkPhaseCode");
        query.setParameter("workPhaseCode", code);
        try {
            return (MstWorkPhase)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 選択肢連番で取得(仕様上1件取得となる)
     * @param choiceSeq
     * @return 
     */
    public MstWorkPhase getMstWorkPhaseByChoiceSeq(Integer choiceSeq) {
        Query query = entityManager.createNamedQuery("MstWorkPhase.findByChoiceSeq");
        query.setParameter("choiceSeq", choiceSeq);
        try {
            return (MstWorkPhase)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 作業工程マスタ反映
     * @param mstWorkPhaseList
     * @param loginUser
     * @return mstWorkPhaseList
     */
    @Transactional
    public MstWorkPhaseList replaceWorkPhases(MstWorkPhaseList mstWorkPhaseList, LoginUser loginUser)  {
        
        // 作業工程コード 重複チェック用
        ArrayList<String> workPhaseCodes = new ArrayList<>();
        // 次作業工程コード 重複チェック用
        ArrayList<String> nextWorkPhaseCodes = new ArrayList<>();
                
        for (MstWorkPhase mstWorkPhase: mstWorkPhaseList.getMstWorkPhases()) {
            /*
             * 必須チェック
             */
            if (mstWorkPhase.getWorkPhaseCode().isEmpty() || mstWorkPhase.getWorkPhaseName().isEmpty()) {
                setApplicationError(mstWorkPhaseList, loginUser, "msg_error_not_null");
                return mstWorkPhaseList;
            }
            
            /*
             * 重複チェック
             */
            if (workPhaseCodes.indexOf(mstWorkPhase.getWorkPhaseCode()) >= 0) {
                setApplicationError(mstWorkPhaseList, loginUser, "msg_error_record_exists");
                return mstWorkPhaseList;
            }
            
            /*
             * 作業工程・次作業工程重複チェック
             * 次作業工程内重複チェック
             */
            if(mstWorkPhase.getNextWorkPhaseIds() != null && mstWorkPhase.getNextWorkPhaseIds().size() > 0) {
                for (String nextId : mstWorkPhase.getNextWorkPhaseIds()) {
                    if (mstWorkPhase.getId().equals(nextId)) {
                        setApplicationError(mstWorkPhaseList, loginUser, "msg_error_next_work_phase_code_exist");
                        return mstWorkPhaseList;
                    } else {
                        if (nextWorkPhaseCodes.indexOf(nextId) >= 0) {
                            setApplicationError(mstWorkPhaseList, loginUser, "msg_error_record_exists");
                            return mstWorkPhaseList;
                        } else {
                            nextWorkPhaseCodes.add(nextId);
                        }
                    }
                }
            }
            // 作業工程コード 重複チェック用 配列追加
            workPhaseCodes.add(mstWorkPhase.getWorkPhaseCode());
            
            /*
             * 登録制御
             */
            if (mstWorkPhase.isAdded()) {
                // 登録制御処理実行
                if (createControl(mstWorkPhaseList, mstWorkPhase, loginUser).isError()) {
                    return mstWorkPhaseList;
                }
            }
            
            /*
             * 削除制御
             */
            else if (mstWorkPhase.isDeleted()) {
                // 削除制御処理実行
                if (deleteControl(mstWorkPhaseList, mstWorkPhase, loginUser).isError()) {
                    return mstWorkPhaseList;
                }
            }

            /*
             * 更新制御
             */
            else if (mstWorkPhase.isModified()) {
                if (updateControl(mstWorkPhaseList, mstWorkPhase, loginUser).isError()) {
                    return mstWorkPhaseList;
                }
            }
            //次作業工程を作業工程フローマスタから削除
            if (mstWorkPhase.isAdded() || mstWorkPhase.isModified()) {
                Query delQuery = entityManager.createNamedQuery("MstWorkPhaseFlow.delete");
                delQuery.setParameter("workPhaseId", mstWorkPhase.getId());
                delQuery.executeUpdate();
            }
            //次作業工程を作業工程フローマスタへ追加
            if ((mstWorkPhase.isAdded() || mstWorkPhase.isModified()) &&
                    mstWorkPhase.getNextWorkPhaseIds() != null && mstWorkPhase.getNextWorkPhaseIds().size() > 0) {
                for (String nextId : mstWorkPhase.getNextWorkPhaseIds()) {
                    MstWorkPhaseFlow flow = new MstWorkPhaseFlow(mstWorkPhase.getId(), nextId);
                    flow.setCreateDate(new java.util.Date());
                    flow.setCreateUserUuid(loginUser.getUserUuid());
                    flow.setUpdateDate(flow.getCreateDate());
                    flow.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(flow);
                }
            }
            //所属を作業工程所属マスタから削除
            if (mstWorkPhase.isAdded() || mstWorkPhase.isModified()) {
                Query delQuery = entityManager.createNamedQuery("MstWorkPhaseDepartment.deleteByWorkPhaseId");
                delQuery.setParameter("workPhaseId", mstWorkPhase.getId());
                delQuery.executeUpdate();
            }
            //所属を作業工程所属マスタへ追加
            if ((mstWorkPhase.isAdded() || mstWorkPhase.isModified())
                    && mstWorkPhase.getDepartmentIds() != null && mstWorkPhase.getDepartmentIds().size() > 0) {
                for (String deptId : mstWorkPhase.getDepartmentIds()) {
                    try {
                        MstWorkPhaseDepartment dept = new MstWorkPhaseDepartment(mstWorkPhase.getId(), Integer.parseInt(deptId));
                        dept.setCreateDate(new java.util.Date());
                        dept.setCreateUserUuid(loginUser.getUserUuid());
                        dept.setUpdateDate(dept.getCreateDate());
                        dept.setUpdateUserUuid(loginUser.getUserUuid());
                        entityManager.persist(dept);
                    } catch (NumberFormatException e) {
                        //nothing
                    }
                }
            }
        }
        return mstWorkPhaseList;
    }
    
    /**
     * 作業工程マスタおよび同期する選択肢マスタ登録制御処理
     * @param mstWorkPhaseList
     * @param mstWorkPhase
     * @param loginUser
     * @return 
     */
    @Transactional
    public MstWorkPhaseList createControl(MstWorkPhaseList mstWorkPhaseList, MstWorkPhase mstWorkPhase, LoginUser loginUser) {
        /*
         * 作業工程マスタ登録制御
         */
        // 作業工程マスタ 作業工程コードによる存在チェック
        if (isExsistByWorkPhaseCode(mstWorkPhase.getWorkPhaseCode())) {
            setApplicationError(mstWorkPhaseList, loginUser, "msg_error_record_exists");
            return mstWorkPhaseList;
        }
        
        // 登録用のCHOICE_SEQを取得
        int registChoiseSeq = getMaxChoiceSeq();
        // 0であれば初期値1を、それ以外は+1を設定
        if (registChoiseSeq == 0) {
            registChoiseSeq = 1;
        } else {
            registChoiseSeq++;
        }
        mstWorkPhase.setChoiceSeq(registChoiseSeq);
        
        // 作業工程マスタ登録
        createMstWorkPhase(mstWorkPhase, loginUser);
        
        /**
         * 選択肢マスタ登録制御
         * 登録した作業工程マスタに対して言語IDの件数分選択肢マスタに登録を行う
         */
        // 言語マスタ取得
        MstLanguageList mstLanguageList = mstLanguageService.getMstLanguages();
        for (MstLanguage mstLanguage : mstLanguageList.getMstLanguages()) {
            // 選択肢マスタに既にPKが存在する場合はエラー
            MstChoicePK mstChoicePk = new MstChoicePK("mst_work_phase.work_phase", mstLanguage.getId(), Integer.toString(mstWorkPhase.getChoiceSeq()));
            if (mstChoiceService.isExsistByPk(mstChoicePk.getCategory(), mstChoicePk.getLangId(), mstChoicePk.getSeq())) {
                setApplicationError(mstWorkPhaseList, loginUser, "msg_error_record_exists");
                return mstWorkPhaseList;
            }
            // 選択肢マスタ登録
            mstChoiceService.createMstChoicePK(mstChoicePk, mstWorkPhase.getWorkPhaseName(), loginUser);
        }
        return mstWorkPhaseList;
    }
    
    /**
     * 作業工程マスタおよび同期する選択肢マスタ削除制御処理
     * @param mstWorkPhaseList
     * @param mstWorkPhase
     * @param loginUser
     * @return 
     */
    @Transactional
    public MstWorkPhaseList deleteControl(MstWorkPhaseList mstWorkPhaseList, MstWorkPhase mstWorkPhase, LoginUser loginUser) {
        // 選択肢マスタ⇒作業工程マスタの順で削除制御
        /*
         * 選択肢マスタ削除制御
         */
        // カテゴリおよびSEQ(作業工程マスタと同期されている値)ですべての言語IDの選択肢マスタを取得
        List<MstChoice> mstChoices = mstChoiceService.findByCategoryAndSeq("mst_work_phase.work_phase", Integer.toString(mstWorkPhase.getChoiceSeq()));
        
        // 選択肢マスタが1件も取得できない場合はエラー
        if (mstChoices.isEmpty()) {
            setApplicationError(mstWorkPhaseList, loginUser, "msg_error_data_deleted");
            return mstWorkPhaseList;
        }
        // 作業工程マスタIDによる存在チェック
        if (!isExsistById(mstWorkPhase.getId())) {
            setApplicationError(mstWorkPhaseList, loginUser, "msg_error_data_deleted");
            return mstWorkPhaseList;
        }
        // 作業工程マスタFKチェック
        if (checkMstWorkPhaseFK(mstWorkPhase.getId())) {
            setApplicationError(mstWorkPhaseList, loginUser, "msg_cannot_delete_used_record");
            return mstWorkPhaseList;
        }
        for (MstChoice mstChoice : mstChoices) {
            /**
             * 選択肢マスタの子存在チェック
             * 作業内容(tbl_work.work_category)が1件でも子に設定されている場合はエラー
             */
            if (mstChoiceService.isExsistChildByCategoryAndParentSeq("tbl_work.work_category", mstChoice.getMstChoicePK().getSeq())) {
                setApplicationError(mstWorkPhaseList, loginUser, "msg_cannot_delete_used_record");
                return mstWorkPhaseList;
            }
            // 選択肢マスタ削除
            mstChoiceService.deleteMstChoice(mstChoice);
        }
        
        /*
         * 作業工程マスタ削除制御
         */
//        // IDによる存在チェック
//        if (!isExsistById(mstWorkPhase.getId())) {
//            setApplicationError(mstWorkPhaseList, loginUser, "msg_error_data_deleted");
//            return mstWorkPhaseList;
//        }
//
//        // FKチェック
//        if (checkMstWorkPhaseFK(mstWorkPhase.getId())) {
//            setApplicationError(mstWorkPhaseList, loginUser, "msg_cannot_delete_used_record");
//            return mstWorkPhaseList;
//        }
        // 作業工程マスタ削除
        deleteMstWorkPhase(mstWorkPhase);
        return mstWorkPhaseList;
    }
    
    /**
     * 作業工程マスタおよび同期する選択肢マスタ更新制御処理
     * @param mstWorkPhaseList
     * @param mstWorkPhase
     * @param loginUser
     * @return 
     */
    @Transactional
    public MstWorkPhaseList updateControl(MstWorkPhaseList mstWorkPhaseList, MstWorkPhase mstWorkPhase, LoginUser loginUser) {
        /*
         * 作業工程マスタ更新制御
         */
        // 作業工程マスタ 作業工程コードによる存在チェック
        if (!isExsistByWorkPhaseCode(mstWorkPhase.getWorkPhaseCode())) {
            setApplicationError(mstWorkPhaseList, loginUser, "msg_error_data_deleted");
            return mstWorkPhaseList;
        }
        // 作業工程マスタ更新
        updateMstWorkPhase(mstWorkPhase, loginUser);
        
        /*
         * 選択肢マスタ更新制御
         */
        // カテゴリおよびSEQ(作業工程マスタと同期されている値)ですべての言語IDの選択肢マスタを取得
        List<MstChoice> mstChoices = mstChoiceService.findByCategoryAndSeq("mst_work_phase.work_phase", Integer.toString(mstWorkPhase.getChoiceSeq()));
        // 選択肢マスタが1件も取得できない場合はエラー
        if (mstChoices.isEmpty()) {
            setApplicationError(mstWorkPhaseList, loginUser, "msg_error_data_deleted");
            return mstWorkPhaseList;
        }
        
        for (MstChoice mstChoice : mstChoices) {
            // 選択肢マスタ更新
            mstChoice.setChoice(mstWorkPhase.getWorkPhaseName());
            mstChoice.setUpdateDate(new java.util.Date());
            mstChoice.setUpdateUserUuid(loginUser.getUserUuid());
            mstChoiceService.updateForMstWorkPhase(mstChoice);
        }
        return mstWorkPhaseList;
    }
    
    /**
     * IDによる存在チェック
     * @param id
     * @return 
     */
    public boolean isExsistById(String id) {
        Query query = entityManager.createNamedQuery("MstWorkPhase.findById");
        query.setParameter("id", id);
        try {
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (NoResultException noResultException) {
            return false;
        }
        return false;
    }
    
    /**
     * 作業工程コードによる存在チェック
     * @param workPhaseCode
     * @return 
     */
    public boolean isExsistByWorkPhaseCode(String workPhaseCode) {
        Query query = entityManager.createNamedQuery("MstWorkPhase.findByWorkPhaseCode");
        query.setParameter("workPhaseCode", workPhaseCode);
        try {
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (NoResultException noResultException) {
            return false;
        }
        return false;
    }
    
    /**
     * 作業工程マスタのFK依存関係チェック
     * @param id
     * @return
     */
    public boolean checkMstWorkPhaseFK(String id) {
        // 作業実績テーブルチェック
        Query query = entityManager.createNamedQuery("TblWork.findByWorkPhaseId");
        query.setParameter("workPhaseId", id);
        return query.getResultList().size() > 0;
// 循環参照となるためMstWrokPhase側のサービスはInjectしない
//        return tblWorkService.isExistsByPK(id);
    }
    
    /**
     * 選択肢連番最大値取得
     * @return 
     */
    public int getMaxChoiceSeq() {
        Query query = entityManager.createNamedQuery("MstWorkPhase.findMaxChoiceSeq");
        Integer maxChoiceSeq = (Integer)query.getSingleResult();
        if (maxChoiceSeq == null) {
            maxChoiceSeq = 0;
        }
        return maxChoiceSeq;
    }
    
    /**
     * 作業工程マスタ登録
     * @param mstWorkPhase
     * @param loginUser 
     */
    @Transactional
    public void createMstWorkPhase(MstWorkPhase mstWorkPhase, LoginUser loginUser) {
        mstWorkPhase.setId(IDGenerator.generate());
        mstWorkPhase.setWorkPhaseCode(mstWorkPhase.getWorkPhaseCode());
        mstWorkPhase.setWorkPhaseName(mstWorkPhase.getWorkPhaseName());
        mstWorkPhase.setWorkPhaseType(mstWorkPhase.getWorkPhaseType());
        mstWorkPhase.setChoiceSeq(mstWorkPhase.getChoiceSeq());
        mstWorkPhase.setCreateDate(new java.util.Date());
        mstWorkPhase.setCreateUserUuid(loginUser.getUserUuid());
        mstWorkPhase.setUpdateDate(mstWorkPhase.getCreateDate());
        mstWorkPhase.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(mstWorkPhase);
    }
    
    /**
     * 作業工程マスタ削除
     * @param mstWorkPhase
     */
    @Transactional
    public void deleteMstWorkPhase(MstWorkPhase mstWorkPhase) {      
        //entityManager.remove(mstWorkPhase);
        Query query = entityManager.createNamedQuery("MstWorkPhase.delete");
        query.setParameter("id", mstWorkPhase.getId());
        query.executeUpdate();
    }
    
    /**
     * 作業工程マスタ更新
     * @param mstWorkPhase
     * @param loginUser
     */
    @Transactional
    public void updateMstWorkPhase(MstWorkPhase mstWorkPhase, LoginUser loginUser) {
        Query query = entityManager.createNamedQuery("MstWorkPhase.update");
        query.setParameter("id", mstWorkPhase.getId());
        query.setParameter("workPhaseName", mstWorkPhase.getWorkPhaseName());
        query.setParameter("workPhaseType", mstWorkPhase.getWorkPhaseType());
        query.setParameter("directFlg", mstWorkPhase.getDirectFlg());
//        query.setParameter("nextWorkPhaseId", mstWorkPhase.getNextWorkPhaseId());
        query.setParameter("updateDate", new java.util.Date());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.executeUpdate();
    }
    
    private void setApplicationError(MstWorkPhaseList mstWorkPhaseList, LoginUser loginUser, String dictKey) {
        setBasicResponseError(
                mstWorkPhaseList
               ,true
               ,ErrorMessages.E201_APPLICATION
               ,mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
        );
    }
    
    private void setBasicResponseError(MstWorkPhaseList mstWorkPhaseList, boolean error, String errorCode, String errorMessage) {
        mstWorkPhaseList.setError(error);
        mstWorkPhaseList.setErrorCode(errorCode);
        mstWorkPhaseList.setErrorMessage(errorMessage);
    }
}
