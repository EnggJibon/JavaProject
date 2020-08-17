/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.function;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionary;
import com.kmcj.karte.resources.dictionary.MstDictionaryList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MstFunctionService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    public MstFunctionList getMstFunctionList(MstDictionaryService mstDictionaryService, LoginUser loginUser) {
        Query query = entityManager.createNamedQuery("MstFunction.findAll");
        List list = query.getResultList();
        MstFunctionList response = new MstFunctionList();
        response.setMstFunctionList(list);
        //Dictionaryから機能名称を取得
        List<String> dictKeys = new ArrayList<>();
        for (MstFunction function: response.getMstFunctionList()) {
            dictKeys.add(function.getDictKey());
        }
        MstDictionaryList mstDictionaryList = mstDictionaryService.getDictionaryList(loginUser.getLangId(), dictKeys);
        HashMap<String, String> map = new HashMap<>();
        for (MstDictionary dict: mstDictionaryList.getMstDictionary()) {
            map.put(dict.getDictKey(), dict.getDictValue());
        }
        for (MstFunction function: response.getMstFunctionList()) {
            function.setDictFunctionName(map.get(function.getDictKey()));
        }
        return response;
        
    }
}
