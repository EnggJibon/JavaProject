/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.visualimage.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 検査データ(tbl_component_inspection_result_detail)をINSPECTION_ITEM_SNO毎にまとめるクラス。
 * @author t.takasaki
 */
public class ComponentInspectionItemDetail {
    private String inspectionItemSno;
    private List<ComponentInspectionItemVisualImages> details;
    
    public ComponentInspectionItemDetail() {}
    public ComponentInspectionItemDetail(String inspectionItemSno) {
        this.inspectionItemSno = inspectionItemSno;
        this.details = new ArrayList<>();
    }
    
    public String getInspectionItemSno() {
        return inspectionItemSno;
    }

    public void setInspectionItemSno(String inspectionItemSno) {
        this.inspectionItemSno = inspectionItemSno;
    }

    public List<ComponentInspectionItemVisualImages> getDetails() {
        return details;
    }

    public void setDetails(List<ComponentInspectionItemVisualImages> details) {
        this.details = details;
    }
}
