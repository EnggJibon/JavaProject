/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.dictionary;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MstDictionaryList extends BasicResponse {
    private List<MstDictionary> mstDictionary;

    /**
     * @return the mstDictionary
     */
    public List<MstDictionary> getMstDictionary() {
        return mstDictionary;
    }

    /**
     * @param mstDictionary the mstDictionary to set
     */
    public void setMstDictionary(List<MstDictionary> mstDictionary) {
        this.mstDictionary = mstDictionary;
    }

    
}
