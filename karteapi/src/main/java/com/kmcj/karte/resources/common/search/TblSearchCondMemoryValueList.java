package com.kmcj.karte.resources.common.search;

import com.kmcj.karte.BasicResponse;

import java.util.List;

public class TblSearchCondMemoryValueList extends BasicResponse {
    
    private List<TblSearchCondMemoryValue> tblSearchCondMemoryValues;

    public List<TblSearchCondMemoryValue> getTblSearchCondMemoryValues() {
        return tblSearchCondMemoryValues;
    }

    public void setTblSearchCondMemoryValues(List<TblSearchCondMemoryValue> tblSearchCondMemoryValues) {
        this.tblSearchCondMemoryValues = tblSearchCondMemoryValues;
    }
}
