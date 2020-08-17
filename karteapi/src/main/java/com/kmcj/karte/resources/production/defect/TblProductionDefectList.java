/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.defect;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 */
public class TblProductionDefectList extends BasicResponse {
    
    private List<TblProductionDefect> productionDefects;
    
    private List<TblProductionDefectDaily> productionDefectsDaily;

    public List<TblProductionDefect> getProductionDefects() {
        return productionDefects;
    }

    public void setProductionDefects(List<TblProductionDefect> productionDefects) {
        this.productionDefects = productionDefects;
    }

    public List<TblProductionDefectDaily> getProductionDefectsDaily() {
        return productionDefectsDaily;
    }

    public void setProductionDefectsDaily(List<TblProductionDefectDaily> productionDefectsDaily) {
        this.productionDefectsDaily = productionDefectsDaily;
    }
}
