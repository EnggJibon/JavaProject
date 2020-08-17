/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.match;

import java.util.List;

/**
 * 照合未確認資産
 *
 * @author xiaozhou.wang
 */
public class TblAssetMatchingResultVoList {

    private String batchEndTime;

    private List<TblAssetMatchingResultVo> tblAssetMatchingResultVo;

    /**
     * @return the tblAssetMatchingResultVo
     */
    public List<TblAssetMatchingResultVo> getTblAssetMatchingResultVo() {
        return tblAssetMatchingResultVo;
    }

    /**
     * @param tblAssetMatchingResultVo the tblAssetMatchingResultVo to set
     */
    public void setTblAssetMatchingResultVo(List<TblAssetMatchingResultVo> tblAssetMatchingResultVo) {
        this.tblAssetMatchingResultVo = tblAssetMatchingResultVo;
    }

    /**
     * @return the batchEndTime
     */
    public String getBatchEndTime() {
        return batchEndTime;
    }

    /**
     * @param batchEndTime the batchEndTime to set
     */
    public void setBatchEndTime(String batchEndTime) {
        this.batchEndTime = batchEndTime;
    }

}
