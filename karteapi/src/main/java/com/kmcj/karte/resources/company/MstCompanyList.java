/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.company;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MstCompanyList extends BasicResponse {

    private List<MstCompany> mstCompanies;

    /**
     * @return the mstCompanies
     */
    public List<MstCompany> getMstCompanies() {
        return mstCompanies;
    }

    /**
     * @param mstCompanies the mstCompanies to set
     */
    public void setMstCompanies(List<MstCompany> mstCompanies) {
        this.mstCompanies = mstCompanies;
    }

}
