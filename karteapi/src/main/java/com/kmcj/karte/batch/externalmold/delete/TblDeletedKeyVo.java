/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmold.delete;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */

public class TblDeletedKeyVo extends BasicResponse {
    private String tblName;
    private String deletedKey;
    private String deleteUserUuid;
    private Date deletedDate;
    private List<TblDeletedKey> tblDeletedKeys;
    

    public TblDeletedKeyVo() {
    }

    public String getTblName() {
        return tblName;
    }

    public String getDeletedKey() {
        return deletedKey;
    }

    public String getDeleteUserUuid() {
        return deleteUserUuid;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    public void setDeletedKey(String deletedKey) {
        this.deletedKey = deletedKey;
    }

    public void setDeleteUserUuid(String deleteUserUuid) {
        this.deleteUserUuid = deleteUserUuid;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public List<TblDeletedKey> getTblDeletedKeys() {
        return tblDeletedKeys;
    }

    public void setTblDeletedKeys(List<TblDeletedKey> tblDeletedKeys) {
        this.tblDeletedKeys = tblDeletedKeys;
    }
    
}
