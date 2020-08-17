/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.assetno;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMoldAssetNoList extends BasicResponse{
    private List <MstMoldAssetNo> mstMoldAssetNo;
    private List <MstMoldAssetNos> mstMoldAssetNos;
    /**
     * @return the mstMoldAssetNo
     */
    public List <MstMoldAssetNo> getMstMoldAssetNo() {
        return mstMoldAssetNo;
    }

    /**
     * @param mstMoldAssetNo the mstMoldAssetNo to set
     */
    public void setMstMoldAssetNo(List <MstMoldAssetNo> mstMoldAssetNo) {
        this.mstMoldAssetNo = mstMoldAssetNo;
    }

    /**
     * @return the mstMoldAssetNos
     */
    public List <MstMoldAssetNos> getMstMoldAssetNos() {
        return mstMoldAssetNos;
    }

    /**
     * @param mstMoldAssetNos the mstMoldAssetNos to set
     */
    public void setMstMoldAssetNos(List <MstMoldAssetNos> mstMoldAssetNos) {
        this.mstMoldAssetNos = mstMoldAssetNos;
    }
    
}
