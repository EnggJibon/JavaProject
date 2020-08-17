package com.kmcj.karte.resources.component.inspection.result.model;

import java.util.List;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author penggd
 */
public class TblComponentInspectionResultVo extends BasicResponse {

    private String id;
    private List<String> idList;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

}
