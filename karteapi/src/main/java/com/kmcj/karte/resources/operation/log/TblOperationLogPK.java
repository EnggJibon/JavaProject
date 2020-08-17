/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation.log;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kmc
 */
@Embeddable
public class TblOperationLogPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER_ID")
    private String userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "OPERATION_PATH")
    private String operationPath;

    public TblOperationLogPK() {
    }

    public TblOperationLogPK(String userId, Date createDate, String operationPath) {
        this.userId = userId;
        this.createDate = createDate;
        this.operationPath = operationPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOperationPath() {
        return operationPath;
    }

    public void setOperationPath(String operationPath) {
        this.operationPath = operationPath;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        hash += (createDate != null ? createDate.hashCode() : 0);
        hash += (operationPath != null ? operationPath.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblOperationLogPK)) {
            return false;
        }
        TblOperationLogPK other = (TblOperationLogPK) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        if ((this.createDate == null && other.createDate != null) || (this.createDate != null && !this.createDate.equals(other.createDate))) {
            return false;
        }
        if ((this.operationPath == null && other.operationPath != null) || (this.operationPath != null && !this.operationPath.equals(other.operationPath))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.operation.log.TblOperationLogPK[ userId=" + userId + ", createDate=" + createDate + ", operationPath=" + operationPath + " ]";
    }
    
}
