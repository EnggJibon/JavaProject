/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.fileimportjob;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kmc
 */
public class TblCsvImportList extends BasicResponse {
    private List<TblCsvImport2> tblCsvImport;
    
    public TblCsvImportList() {
        tblCsvImport = new ArrayList<>();
    }
    
    /**
     * @return the tblCsvImport
     */
    public List<TblCsvImport2> getTblCsvImport() {
        return tblCsvImport;
    }

    /**
     * @param tblCsvImport the tblCsvImport to set
     */
    public void setTblCsvImport(List<TblCsvImport2> tblCsvImport) {
        this.tblCsvImport = tblCsvImport;
    }

}
