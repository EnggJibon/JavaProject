/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mgmt.company;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMgmtCompanyList extends BasicResponse {

    private List<MstMgmtCompany> mstMgmtCompanies;

    /**
     * @return the mstMgmtCompanies
     */
    public List<MstMgmtCompany> getMstMgmtCompanies() {
        return mstMgmtCompanies;
    }

    /**
     * @param mstMgmtCompanies the mstMgmtCompanies to set
     */
    public void setMstMgmtCompanies(List<MstMgmtCompany> mstMgmtCompanies) {
        this.mstMgmtCompanies = mstMgmtCompanies;
    }

}
