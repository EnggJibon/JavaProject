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
public class TblUploadFileList extends BasicResponse {
    private List<TblUploadFile2> tblUploadFile;
    
    public TblUploadFileList() {
        tblUploadFile = new ArrayList<>();
    }
    
    /**
     * @return the mstAuths
     */
    public List<TblUploadFile2> getTblUploadFile() {
        return tblUploadFile;
    }

    /**
     * @param tblUploadFile the tblUploadFile to set
     */
    public void setTblUploadFile(List<TblUploadFile2> tblUploadFile) {
        this.tblUploadFile = tblUploadFile;
    }

}
