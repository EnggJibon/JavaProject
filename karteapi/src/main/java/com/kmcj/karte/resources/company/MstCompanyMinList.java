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
 * @author t.takasaki
 */
public class MstCompanyMinList extends BasicResponse {
    private List<MstCompanyMin> mstCompanies;

    public List<MstCompanyMin> getMstCompanies() {
        return mstCompanies;
    }

    public void setMstCompanies(List<MstCompanyMin> mstCompanies) {
        this.mstCompanies = mstCompanies;
    }
}
