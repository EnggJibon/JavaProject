/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log.backup;

import com.kmcj.karte.BasicResponse;
import java.util.HashMap;
import java.util.List;
/**
 *
 * @author c.darvin
 */
public class TblSigmaLogBackupList extends BasicResponse {
    private List<TblSigmaLogBackup> tblSigmaLogBackups; 
    
    public TblSigmaLogBackupList(){
    }
    
    public void setTblSigmaLogBackup(List<TblSigmaLogBackup> tblSigmaLogBackups) {
        this.tblSigmaLogBackups = tblSigmaLogBackups;
    }
    
    public List<TblSigmaLogBackup> getTblSigmaLogBackup(){
        return tblSigmaLogBackups;
    }
    
    public void formatJson() {
        for (TblSigmaLogBackup list : tblSigmaLogBackups) {
            list.formatJson();
        }
    }
}
