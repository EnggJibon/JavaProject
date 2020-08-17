/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.dictionary;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.language.MstLanguage;
import com.kmcj.karte.resources.language.MstLanguageService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MstDictionaryService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject 
    MstLanguageService mstLanguageService;

    public MstDictionaryList getAllDictionary() {
        Query query = entityManager.createNamedQuery("MstDictionary.findAll");
        List list = query.getResultList();
        MstDictionaryList response = new MstDictionaryList();
        response.setMstDictionary(list);
        return response;
    }
    
    @Transactional
    public void updateDictionary(MstDictionaryList mstDictionaryList, LoginUser loginUser) {
        Query query = entityManager.createNamedQuery("MstDictionary.update");
        java.util.Date updateDate = new java.util.Date();
        for (MstDictionary mstDictionary: mstDictionaryList.getMstDictionary()) {
            if (mstDictionary.isModified()) {
                query.setParameter("dictValue", mstDictionary.getDictValue());
                query.setParameter("langId", mstDictionary.getLangId());
                query.setParameter("dictKey", mstDictionary.getDictKey());
                query.setParameter("updateDate", updateDate);
                query.setParameter("updateUserUuid", loginUser.getUserUuid());
                query.executeUpdate();
            }
        }
        
    }

    @Transactional
    public void updateDictionarySingle(MstDictionarySingle mstDictionarySingle, LoginUser loginUser) {
        Query query = entityManager.createNamedQuery("MstDictionary.update");
        java.util.Date updateDate = new java.util.Date();
        query.setParameter("dictValue", mstDictionarySingle.getNewDictValue());
        query.setParameter("langId", loginUser.getLangId());
        query.setParameter("dictKey", mstDictionarySingle.getDictKey());
        query.setParameter("updateDate", updateDate);
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.executeUpdate();
    }
    
    public String getDictionaryValue(String langId, String dictKey) {
        MstDictionaryList mstDictionaryList = getDictionary(langId, dictKey);
        if (mstDictionaryList.getMstDictionary().size() > 0) {
            return ((MstDictionary)mstDictionaryList.getMstDictionary().get(0)).getDictValue();
        }
        else {
            return null;
        }
    }
    
    public MstDictionaryList getDictionary(String langId, String dictKey) {
        if (langId == null || langId.equals("null")) {
            MstLanguage mstLanguage = mstLanguageService.getDefaultLanguage();
            langId = mstLanguage.getId();
        }
        Query query = entityManager.createNamedQuery("MstDictionary.findByKey");
        query.setParameter("langId", langId);
        query.setParameter("dictKey", dictKey);
        List list = query.getResultList();
        MstDictionaryList response = new MstDictionaryList();
        if (list.size() > 0) {
            response.setMstDictionary(list);
            getParamDictionary(response);
        }
        else {
            MstDictionary mstDictionary = new MstDictionary();
            mstDictionary.setLangId(langId);
            mstDictionary.setDictKey(dictKey);
            mstDictionary.setDictValue(dictKey);
            response.setMstDictionary(new ArrayList<MstDictionary>());
            response.getMstDictionary().add(mstDictionary);
        }
        return response;
        
    }
    
    /**
     * 指定された文言キーにリストに対してハッシュマップで文言を返す
     * ハッシュマップには文言キーをキー、文言を値として格納
     * @param langId
     * @param dictKeyList
     * @return 
     */
    public HashMap<String, String> getDictionaryHashMap(String langId, List<String> dictKeyList) {
        MstDictionaryList list = getDictionaryList(langId, dictKeyList);
        HashMap<String, String> dictMap = new HashMap<>();
        for (MstDictionary dict : list.getMstDictionary()) {
            dictMap.put(dict.getDictKey(), dict.getDictValue());
        }
        return dictMap;
    }
    
    public MstDictionaryList getDictionaryList(String langId, List<String> dictKeyList) {
        if (langId == null || langId.equals("null")) {
            MstLanguage mstLanguage = mstLanguageService.getDefaultLanguage();
            langId = mstLanguage.getId();
        }
        Query query = entityManager.createNamedQuery("MstDictionary.findByKeyList");
        query.setParameter("langId", langId);
        query.setParameter("dictKeyList", dictKeyList);
        List list = query.getResultList();
        MstDictionaryList response = new MstDictionaryList();
        response.setMstDictionary(list);
        getParamDictionary(response);
        for (String dictKey: dictKeyList) {
            boolean found = false;
            for (MstDictionary mstDictionary: response.getMstDictionary()) {
                if (mstDictionary.getDictKey().equals(dictKey)) {
                    found = true;
                }
            }
            if (!found) {
                MstDictionary mstDictionary = new MstDictionary();
                mstDictionary.setLangId(langId);
                mstDictionary.setDictKey(dictKey);
                mstDictionary.setDictValue(dictKey);
                response.getMstDictionary().add(mstDictionary);
            }
        }
        return response;
    }
    
    private void getParamDictionary(MstDictionaryList mstDictionaryList) {
        List<MstDictionary> list = mstDictionaryList.getMstDictionary();
        String langId;
        if (list.size() <= 0) {
            return;
        }
        else {
            langId = ((MstDictionary)list.get(0)).getLangId();
        }
        List<String> paramKeys = new ArrayList<>();
        for (MstDictionary mstDictionary: list) {
            if (mstDictionary.getParamKey01() != null && !mstDictionary.getParamKey01().equals("")) {
                paramKeys.add(mstDictionary.getParamKey01());
            }
            if (mstDictionary.getParamKey02() != null && !mstDictionary.getParamKey02().equals("")) {
                paramKeys.add(mstDictionary.getParamKey02());
            }
            if (mstDictionary.getParamKey03() != null && !mstDictionary.getParamKey03().equals("")) {
                paramKeys.add(mstDictionary.getParamKey03());
            }
            if (mstDictionary.getParamKey04() != null && !mstDictionary.getParamKey04().equals("")) {
                paramKeys.add(mstDictionary.getParamKey04());
            }
            if (mstDictionary.getParamKey05() != null && !mstDictionary.getParamKey05().equals("")) {
                paramKeys.add(mstDictionary.getParamKey05());
            }
        }
        HashMap<String, MstDictionary> paramValues = new HashMap<>();
        if (paramKeys.size() > 0) {
            MstDictionaryList paramDicts = getDictionaryList(langId, paramKeys);
            List<MstDictionary> paramDictList = paramDicts.getMstDictionary();
            for (MstDictionary paramDict: paramDictList) {
                paramValues.put(paramDict.getDictKey(), paramDict);
            }
            for (MstDictionary mstDictionary: list) {
                if (mstDictionary.getParamKey01() != null && !mstDictionary.getParamKey01().equals("")) {
                    MstDictionary paramVal = (MstDictionary)paramValues.get(mstDictionary.getParamKey01());
                    String val = mstDictionary.getDictValue().replaceAll("%" + mstDictionary.getParamKey01() + "%", paramVal.getDictValue());
                    mstDictionary.setDictValue(val);
                }
                if (mstDictionary.getParamKey02() != null && !mstDictionary.getParamKey02().equals("")) {
                    MstDictionary paramVal = (MstDictionary)paramValues.get(mstDictionary.getParamKey02());
                    String val = mstDictionary.getDictValue().replaceAll("%" + mstDictionary.getParamKey02() + "%", paramVal.getDictValue());
                    mstDictionary.setDictValue(val);
                }
                if (mstDictionary.getParamKey03() != null && !mstDictionary.getParamKey03().equals("")) {
                    MstDictionary paramVal = (MstDictionary)paramValues.get(mstDictionary.getParamKey03());
                    String val = mstDictionary.getDictValue().replaceAll("%" + mstDictionary.getParamKey03() + "%", paramVal.getDictValue());
                    mstDictionary.setDictValue(val);
                }
                if (mstDictionary.getParamKey04() != null && !mstDictionary.getParamKey04().equals("")) {                    
                    MstDictionary paramVal = (MstDictionary)paramValues.get(mstDictionary.getParamKey04());
                    String val = mstDictionary.getDictValue().replaceAll("%" + mstDictionary.getParamKey04() + "%", paramVal.getDictValue());
                    mstDictionary.setDictValue(val);
                }
                if (mstDictionary.getParamKey05() != null && !mstDictionary.getParamKey05().equals("")) {
                    MstDictionary paramVal = (MstDictionary)paramValues.get(mstDictionary.getParamKey05());
                    String val = mstDictionary.getDictValue().replaceAll("%" + mstDictionary.getParamKey05() + "%", paramVal.getDictValue());
                    mstDictionary.setDictValue(val);
                }
            }
        }
    }
    
}
