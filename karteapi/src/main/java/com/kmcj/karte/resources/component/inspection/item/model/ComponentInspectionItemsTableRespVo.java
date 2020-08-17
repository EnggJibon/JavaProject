package com.kmcj.karte.resources.component.inspection.item.model;

import java.util.List;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author penggd
 */
public class ComponentInspectionItemsTableRespVo extends BasicResponse {

    private List<String> idList;

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }
    
    
    
}
