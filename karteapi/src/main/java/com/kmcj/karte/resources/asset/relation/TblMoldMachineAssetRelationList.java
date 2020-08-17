/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.relation;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMoldMachineAssetRelationList extends BasicResponse{

    private List<TblMoldMachineAssetRelationVo> TblMoldMachineAssetRelationList;

    /**
     * @return the TblMoldMachineAssetRelationList
     */
    public List<TblMoldMachineAssetRelationVo> getTblMoldMachineAssetRelationList() {
        return TblMoldMachineAssetRelationList;
    }

    /**
     * @param TblMoldMachineAssetRelationList the
     * TblMoldMachineAssetRelationList to set
     */
    public void setTblMoldMachineAssetRelationList(List<TblMoldMachineAssetRelationVo> TblMoldMachineAssetRelationList) {
        this.TblMoldMachineAssetRelationList = TblMoldMachineAssetRelationList;
    }

}
