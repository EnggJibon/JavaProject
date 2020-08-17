/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.match;

import com.kmcj.karte.resources.asset.MstAssetVo;
import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelationVo;
import java.util.List;

/**
 * 照合未確認資産
 *
 * @author xiaozhou.wang
 */
public class TblAssetMatchingResultVo {

    // 資産マスタ表示リスト
    private MstAssetVo mstAssetVo;
    
    // 照合結果
    private String matchingResult;
    
    // 関係マスタ
    private List<TblMoldMachineAssetRelationVo> TblMoldMachineAssetRelationVos;

    /**
     * @return the mstAssetVo
     */
    public MstAssetVo getMstAssetVo() {
        return mstAssetVo;
    }

    /**
     * @param mstAssetVo the mstAssetVo to set
     */
    public void setMstAssetVo(MstAssetVo mstAssetVo) {
        this.mstAssetVo = mstAssetVo;
    }

    /**
     * @return the TblMoldMachineAssetRelationVos
     */
    public List<TblMoldMachineAssetRelationVo> getTblMoldMachineAssetRelationVos() {
        return TblMoldMachineAssetRelationVos;
    }

    /**
     * @param TblMoldMachineAssetRelationVos the TblMoldMachineAssetRelationVos to set
     */
    public void setTblMoldMachineAssetRelationVos(List<TblMoldMachineAssetRelationVo> TblMoldMachineAssetRelationVos) {
        this.TblMoldMachineAssetRelationVos = TblMoldMachineAssetRelationVos;
    }

    /**
     * @return the matchingResult
     */
    public String getMatchingResult() {
        return matchingResult;
    }

    /**
     * @param matchingResult the matchingResult to set
     */
    public void setMatchingResult(String matchingResult) {
        this.matchingResult = matchingResult;
    }

}
