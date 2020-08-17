/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authorization;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MstAuthList extends BasicResponse {
    private List<MstAuth> mstAuths;

    /**
     * @return the mstAuths
     */
    public List<MstAuth> getMstAuths() {
        return mstAuths;
    }

    /**
     * @param mstAuths the mstAuths to set
     */
    public void setMstAuths(List<MstAuth> mstAuths) {
        this.mstAuths = mstAuths;
    }
    
}
