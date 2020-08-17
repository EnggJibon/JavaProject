/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.files;

import com.kmcj.karte.resources.mold.issue.*;
import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class TblUploadFileList extends BasicResponse {
    
    private List<TblUploadFile> tblUploadFiles;

    public List<TblUploadFile> getTblUploadFiles() {
        return tblUploadFiles;
    }

    public void setTblUploadFiles(List<TblUploadFile> tblUploadFiles) {
        this.tblUploadFiles = tblUploadFiles;
    }
    
}
