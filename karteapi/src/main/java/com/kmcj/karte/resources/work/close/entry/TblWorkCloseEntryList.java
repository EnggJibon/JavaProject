/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.close.entry;
import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 作業入力締めテーブルResponse
 * @author t.ariki
 */
public class TblWorkCloseEntryList extends BasicResponse {    
    private List<TblWorkCloseEntry> tblWorkCloseEntries;
    
    public TblWorkCloseEntryList() {
        tblWorkCloseEntries = new ArrayList<>();
    }
    /**
     * @return the tblWorkCloseEntries
     */
    public List<TblWorkCloseEntry> getTblWorkCloseEntries() {
        return tblWorkCloseEntries;
    }
    /**
     * @param tblWorkCloseEntries the tblWorkCloseEntries to set
     */
    public void setTblWorkCloseEntries(List<TblWorkCloseEntry> tblWorkCloseEntries) {
        this.tblWorkCloseEntries = tblWorkCloseEntries;
    }
}
