/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author kmc
 */
public class MstOperationList extends BasicResponse {

    private List<MstOperation> mstOperation;
    
    /**
     * @return the mstOperation
     */
    public List<MstOperation> getMstOperationList() {
        return mstOperation;
    }

    /**
     * @param mstOperation the mstOperation to set
     */
    public void setMstOperationList(List<MstOperation> mstOperation) {
        this.mstOperation = mstOperation;
    }
    
}
