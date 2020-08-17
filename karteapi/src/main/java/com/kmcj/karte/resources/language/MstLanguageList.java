/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.language;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MstLanguageList extends BasicResponse {
    private List<MstLanguage> mstLanguages;

    /**
     * @return the mstLanguages
     */
    public List<MstLanguage> getMstLanguages() {
        return mstLanguages;
    }

    /**
     * @param mstLanguages the mstLanguages to set
     */
    public void setMstLanguages(List<MstLanguage> mstLanguages) {
        this.mstLanguages = mstLanguages;
    }
    
}
