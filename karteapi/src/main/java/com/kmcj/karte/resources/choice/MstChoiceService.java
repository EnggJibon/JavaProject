/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.batch.externalmold.choice.ExtMstChoice;
import com.kmcj.karte.batch.externalmold.choice.ExtMstChoicePK;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

/**
 *
 * @author
 */
@Dependent
public class MstChoiceService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     *
     * @param langId
     * @param categoryKey
     * @return
     */
    public MstChoiceList getChoice(String langId, String categoryKey) {

        Query query = entityManager.createQuery("SELECT m FROM MstChoice m WHERE m.mstChoicePK.langId = :langId AND m.mstChoicePK.category = :category AND m.deleteFlg <> 1 ORDER BY m.displaySeq");
        query.setParameter("langId", langId);
        query.setParameter("category", categoryKey);
        List list = query.getResultList();
        MstChoiceList response = new MstChoiceList();
        response.setMstChoice(list);

        return response;
    }
    
    public HashMap<Integer, String> getSeqValueMap(String langId, String categoryKey) {
        MstChoiceList list = getChoice(langId, categoryKey);
        HashMap<Integer, String> map = new HashMap();
        for (MstChoice choice: list.getMstChoice()) {
            map.put(Integer.valueOf(choice.mstChoicePK.getSeq()), choice.getChoice());
        }
        return map;
    }

    /**
     * categoryを取得
     *
     * @param langId
     * @param category
     * @param parentSeq
     * @param loginUser
     * @return
     */
    public MstChoiceList getCategories(String langId, String category, String parentSeq, LoginUser loginUser) {
        StringBuilder sql = new StringBuilder(" SELECT a.mstChoiceCategory.parentCategory,a.mstChoicePK.category,a.choice,a.mstChoicePK.seq,a.parentSeq FROM "
                + " MstChoice a WHERE 1=1 ");

        sql.append(" AND a.mstChoicePK.langId = :langId ");
            
        if (category != null && !"".equals(category)) {
            sql.append(" AND a.mstChoicePK.category = :category ");
        }

        sql.append(" AND a.deleteFlg <> 1 ");
        
//        if (parentSeq != null && !"".equals(parentSeq)) {
//            // http://www.hachi-log.com/mysql-find-in-set/
//            // [FIND_IN_SET]とは N 個の部分文字列で構成されるリスト strlist に、文字列 str が含まれている場合は、1 から N までのいずれかの値を返す。
//            // 文字列のリストは、それぞれの間を ‘,’ 文字で区切られた各部分文字列で構成される文字列である。
//            // FIND_IN_SET関数はカンマ区切り限定ですが、”-“だったり、”|”で区切られているデータの場合は、REPLACE関数で置換してから使えばいいですね。
//            sql.append(" AND FIND_IN_SET( '").append(parentSeq).append("', a.parentSeq) ");
//        }
        sql.append(" ORDER BY a.mstChoicePK.category,a.displaySeq ");
        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("langId", null == langId || langId.equals("") ? loginUser.getLangId() : langId);

        if (category != null && !"".equals(category)) {
            query.setParameter("category", category);
        }
        List list = query.getResultList();
        MstChoiceList response = new MstChoiceList();
        List<MstChoiceVo> mstChoiceVoList = new ArrayList<>();
        MstChoiceVo mstChoiceVo;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] objs = (Object[]) list.get(i);
                if (objs[4] == null || objs[4].equals("")) {
                    mstChoiceVo = new MstChoiceVo();
                    mstChoiceVo.setParentCategory(objs[0] == null ? "" : objs[0].toString());
                    mstChoiceVo.setCategory(objs[1].toString());
                    mstChoiceVo.setChoice(objs[2].toString());
                    mstChoiceVo.setSeq(objs[3].toString());
                    mstChoiceVo.setParentSeq("0");
                    mstChoiceVoList.add(mstChoiceVo);
                } else {
                    String[] arrParentSeq = objs[4].toString().split(",");
                    if (arrParentSeq.length > 0 && parentSeq != null && !"".equals(parentSeq)) {
                        List<String> listParentSeq = Arrays.asList(arrParentSeq);
                        if (listParentSeq.contains(parentSeq)) {
                            mstChoiceVo = new MstChoiceVo();
                            mstChoiceVo.setParentCategory(objs[0] == null ? "" : objs[0].toString());
                            mstChoiceVo.setCategory(objs[1].toString());
                            mstChoiceVo.setChoice(objs[2].toString());
                            mstChoiceVo.setSeq(objs[3].toString());
                            mstChoiceVo.setParentSeq(objs[4].toString());
                            mstChoiceVoList.add(mstChoiceVo);
                        }
                    }
                }
            }
        }

        response.setMstChoiceVo(mstChoiceVoList);
        return response;
    }

    /**
     *
     * @param choicePK
     * @param choice
     * @return
     */
    public MstChoice checkMstChoiceByPKAndChoice(MstChoicePK choicePK, String choice) {
        StringBuilder sql = new StringBuilder("select c from MstChoice c where 1=1 ");
        if (null != choicePK.getCategory() && !choicePK.getCategory().trim().equals("")) {
            sql.append(" and c.mstChoicePK.category = :category ");
        }
        if (null != choicePK.getLangId() && !choicePK.getLangId().trim().equals("")) {
            sql.append(" and c.mstChoicePK.langId = :langId ");
        }
        if (null != choicePK.getSeq() && !choicePK.getSeq().equals("")) {
            sql.append(" and c.mstChoicePK.seq = :seq ");
        }
        if (null != choice && !choice.trim().equals("")) {
            sql.append(" and c.choice = :choice ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != choicePK.getCategory() && !choicePK.getCategory().trim().equals("")) {
            query.setParameter("category", choicePK.getCategory());
        }
        if (null != choicePK.getLangId() && !choicePK.getLangId().trim().equals("")) {
            query.setParameter("langId", choicePK.getLangId());
        }
        if (null != choicePK.getSeq() && !choicePK.getSeq().equals("")) {
            query.setParameter("seq", choicePK.getSeq());
        }
        if (null != choice && !choice.trim().equals("")) {
            query.setParameter("choice", choice);
        }

        List<MstChoice> list = query.getResultList();

        return list.isEmpty() ? null : list.get(0);

    }

    public MstChoice getByChoiceSeq(String choice, String langId, String category) {
        Query query = entityManager.createNamedQuery("MstChoice.findByChoiceSeq");
        query.setParameter("choice", choice);
        query.setParameter("langId", langId);
        query.setParameter("category", category);
        try {
            List list = query.getResultList();
            int cnt = list.size();
            if (cnt >= 1) {
                MstChoice response = (MstChoice) list.get(0);
                return response;
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    public MstChoice getBySeqChoice(String Seq, String langId, String category) {
        Query query = entityManager.createNamedQuery("MstChoice.findByPK");
        query.setParameter("seq", Seq);
        query.setParameter("langId", langId);
        query.setParameter("category", category);
        try {
            MstChoice response = (MstChoice) query.getSingleResult();
            return response;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * バッチで選択肢マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstChoiceVo getExtChoicesByBatch(String latestExecutedDate) {
        MstChoiceVo resVo = new MstChoiceVo();
        StringBuilder sql;
        sql = new StringBuilder("SELECT t FROM MstChoice t WHERE 1=1 ");

        Query query = entityManager.createQuery(sql.toString());

        List<MstChoice> resList = query.getResultList();
        for (MstChoice mstChoice : resList) {
            mstChoice.setMstChoiceCategory(null);
            mstChoice.setMstLanguage(null);
        }
        resVo.setChoices(resList);
        return resVo;
    }
    
    /**
     * バッチで選択肢マスタデータをdelete by company
     *
     * @param companyId
     * @return
     */
    @Transactional
    public BasicResponse deleteExtChoicesByBatch(String companyId) {
        BasicResponse response = new BasicResponse();
        entityManager.createNamedQuery("ExtMstChoice.deleteByCompanyId").setParameter("companyId", companyId).executeUpdate();
        
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /**
     * バッチで選択肢マスタデータを更新
     *
     * @param choices
     * @param companyId
     * @return
     */
    @Transactional
    public BasicResponse updateExtChoicesByBatch(List<MstChoice> choices, String companyId) {
        BasicResponse response = new BasicResponse();

        if (choices != null && !choices.isEmpty()) {
            ExtMstChoice extMstChoice;
            ExtMstChoicePK choicePK;
            for (MstChoice aChoice : choices) {
                extMstChoice = new ExtMstChoice();
                choicePK = new ExtMstChoicePK();
                choicePK.setId(aChoice.getId());
                choicePK.setCompanyId(companyId);
                extMstChoice.setExtMstChoicePK(choicePK);
                extMstChoice.setCategory(aChoice.getMstChoicePK().getCategory());
                extMstChoice.setLangId(aChoice.getMstChoicePK().getLangId());
                extMstChoice.setSeq(aChoice.getMstChoicePK().getSeq());
                extMstChoice.setChoice(aChoice.getChoice());
                if (null == aChoice.getParentSeq() || aChoice.getParentSeq().isEmpty()) {
                    extMstChoice.setParentSeq(null);
                } else {
                    extMstChoice.setParentSeq(aChoice.getParentSeq());
                }
                extMstChoice.setCreateDate(aChoice.getCreateDate());
                extMstChoice.setCreateUserUuid(aChoice.getCreateUserUuid());
                extMstChoice.setUpdateDate(aChoice.getUpdateDate());
                extMstChoice.setUpdateUserUuid(aChoice.getUpdateUserUuid());
                entityManager.persist(extMstChoice);

            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        //  response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
    
    /**
     * PK存在チェック
     * @param category
     * @param langId
     * @param seq
     * @return 
     */
    public boolean isExsistByPk(String category, String langId, String seq) {
        Query query = entityManager.createNamedQuery("MstChoice.findByPK");
        query.setParameter("langId", langId);
        query.setParameter("category", category);
        query.setParameter("seq", seq);
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
     * PK取得
     * @param category
     * @param langId
     * @param seq
     * @return 
     */
    public MstChoice findByPk(String category, String langId, String seq) {
        Query query = entityManager.createNamedQuery("MstChoice.findByPK");
        query.setParameter("langId", langId);
        query.setParameter("category", category);
        query.setParameter("seq", seq);
        try {
            return (MstChoice)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public String getChoiceVal(String category, String seq, String langId) {
        MstChoice ch = findByPk(category, langId, seq);
        return ch == null ? "" : ch.getChoice();
    }
    
    /**
     * カテゴリとSEQで選択肢マスタのリストを取得
     * @param category
     * @param seq
     * @return 
     */
    public List<MstChoice> findByCategoryAndSeq(String category, String seq) {
        Query query = entityManager.createNamedQuery("MstChoice.findByCategoryAndSeq");
        query.setParameter("category", category);
        query.setParameter("seq", seq);
        return query.getResultList();
    }
    
    /**
     * カテゴリおよび親SEQによる存在チェック
     * @param category
     * @param parentSeq
     * @return 
     */
    public boolean isExsistChildByCategoryAndParentSeq(String category, String parentSeq) {
        Query query = entityManager.createNamedQuery("MstChoice.findByCategoryAndParentSeq");
        query.setParameter("category", category);
        query.setParameter("parentSeq", parentSeq);
        return query.getResultList().size() > 0;
    }
    /**
     * 1件のみ取得
     * @param category
     * @param langId
     * @param parentSeq
     * @param seq
     * @return 
     */
    public MstChoice findByCategoryAndLangIdAndParentseqAndSeq(String category, String langId, String parentSeq, String seq) {
        Query query = entityManager.createNamedQuery("MstChoice.findByCategoryAndLangIdAndParentseqAndSeq");
        query.setParameter("category", category);
        query.setParameter("langId", langId);
        query.setParameter("parentSeq", parentSeq);
        query.setParameter("seq", seq);
        try {
            return (MstChoice)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
    *
    * @param langId
    * @param categoryKeys
    * @return
    */
    public Map<String, String> getChoiceMap(String langId, String[] categoryKeys) {

        Query query = entityManager.createNamedQuery("MstChoice.findByCategoryList");

        query.setParameter("langId", langId);
        List<String> categoryList = Arrays.asList(categoryKeys);
        query.setParameter("categoryList", categoryList);

        List<MstChoice> list = (List<MstChoice>) query.getResultList();

        Map<String, String> result = new HashMap<String, String>();

        for (MstChoice mstChoice : list) {

            result.put(mstChoice.getMstChoicePK().getCategory() + mstChoice.mstChoicePK.getSeq(),
                    mstChoice.getChoice());
        }

        return result;
    }
    
    /**
    *
    * @param langId
    * @param categoryKeys
    * @return
    */
    public Map<String, List<String>> getChoiceListMap(String langId, String[] categoryKeys) {

        Query query = entityManager.createNamedQuery("MstChoice.findByCategoryList");

        query.setParameter("langId", langId);
        List<String> categoryList = Arrays.asList(categoryKeys);
        query.setParameter("categoryList", categoryList);

        List<MstChoice> list = (List<MstChoice>) query.getResultList();

        Map<String, List<String>> result = new HashMap<String, List<String>>();
        
        for(int i=0 ; i<categoryKeys.length; i++){
            
            List<String> caregoryList = new ArrayList<String>();
            
            String tempKey = categoryKeys[i];

            for (MstChoice mstChoice : list) {
                
                if(tempKey.equals(mstChoice.getMstChoicePK().getCategory())){
                    
                    caregoryList.add(mstChoice.getChoice());
                    
                }

            }
            
            result.put(tempKey, caregoryList);
        }

        return result;
    }
    
    /**
     * 選択肢マスタPK(親)登録
     */
    @Transactional
    public void createMstChoicePK(MstChoicePK mstChoicePK, String choice, LoginUser loginUser) {
        MstChoice mstChoice = new MstChoice(mstChoicePK, IDGenerator.generate(), choice);
        mstChoice.setDisplaySeq(new Integer(mstChoicePK.getSeq()));
        mstChoice.setDeleteFlg(0);
        mstChoice.setCreateDate(new java.util.Date());
        mstChoice.setCreateUserUuid(loginUser.getUserUuid());
        mstChoice.setUpdateDate(mstChoice.getCreateDate());
        mstChoice.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(mstChoice);
    }
    @Transactional
    public void createMstChoice(MstChoicePK mstChoicePK, String choice,String parentSeq, LoginUser loginUser, Integer displaySeq, Integer delFlg) {
        MstChoice mstChoice = new MstChoice(mstChoicePK, IDGenerator.generate(), choice);
        mstChoice.setDisplaySeq(displaySeq);
        mstChoice.setDeleteFlg(delFlg);
        mstChoice.setCreateDate(new java.util.Date());
        mstChoice.setCreateUserUuid(loginUser.getUserUuid());
        mstChoice.setUpdateDate(mstChoice.getCreateDate());
        mstChoice.setUpdateUserUuid(loginUser.getUserUuid());
        mstChoice.setParentSeq(parentSeq);
        entityManager.persist(mstChoice);
    }
    /**
     * 選択肢マスタ削除
     * @param mstChoice 
     */
    @Transactional
    public void deleteMstChoice(MstChoice mstChoice) {
        //entityManager.remove(mstChoicePK);
        Query query = entityManager.createNamedQuery("MstChoice.DeleteById");
        query.setParameter("id", mstChoice.getId());
        query.executeUpdate();
    }
    
    /**
     * PK取得
     * @param category
     * @param langId
     * @param seq
     * @return 
     */
    public MstChoice findByCategoryANDLangIdAndChoice(String category, String langId, String choice) {
        Query query = entityManager.createNamedQuery("MstChoice.findByCategoryAndLangIdAndChoice");
        query.setParameter("langId", langId);
        query.setParameter("category", category);
        query.setParameter("choice", choice);

        List list = query.getResultList();

        if (list.size() > 0) {

            return (MstChoice) list.get(0);
        } else {
            return null;
        }

    }

    /**
     * 作業工程マスタのための選択肢マスタ更新
     * @param mstChoice 
     */
    @Transactional
    public void updateForMstWorkPhase(MstChoice mstChoice) {
        entityManager.merge(mstChoice);
    }
    
    public Map<MstChoicePrime.Pk, MstChoicePrime> getAll() {
        List<MstChoicePrime> chList = entityManager.createNamedQuery("MstChoicePrime.findAll", MstChoicePrime.class).getResultList();
        Map<MstChoicePrime.Pk, MstChoicePrime> ret = new HashMap<>();
        for (MstChoicePrime choice : chList) {
            ret.put(choice.getPk(), choice);
        }
        return ret;
    }
}
