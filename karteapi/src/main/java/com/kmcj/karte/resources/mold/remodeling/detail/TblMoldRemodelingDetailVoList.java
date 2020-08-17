/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kmcj.karte.resources.mold.remodeling.detail;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class TblMoldRemodelingDetailVoList extends  BasicResponse{
    
    public TblMoldRemodelingDetailVoList() {
        tblMoldRemodelingDetailVoList = new ArrayList<>();
    }
private List<TblMoldRemodelingDetailVo> tblMoldRemodelingDetailVoList;
/**
 * 
 * @return 
 */
    public List<TblMoldRemodelingDetailVo> getTblMoldRemodelingDetailVoList() {
        return tblMoldRemodelingDetailVoList;
    }
/**
 * 
 * @param tblMoldRemodelingDetailVoList 
 */
    public void setTblMoldRemodelingDetailVoList(List<TblMoldRemodelingDetailVo> tblMoldRemodelingDetailVoList) {
        this.tblMoldRemodelingDetailVoList = tblMoldRemodelingDetailVoList;
    }

}
