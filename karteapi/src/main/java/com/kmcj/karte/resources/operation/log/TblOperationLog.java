/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation.log;

import com.kmcj.karte.resources.dictionary.MstDictionary;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kmc
 */
@Entity
@Table(name = "tbl_operation_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblOperationLog.findAll", query = "SELECT t FROM TblOperationLog t"),
    @NamedQuery(name = "TblOperationLog.findByUserId", query = "SELECT t FROM TblOperationLog t WHERE t.tblOperationLogPK.userId = :userId"),
    @NamedQuery(name = "TblOperationLog.findByCreateDate", query = "SELECT t FROM TblOperationLog t WHERE t.tblOperationLogPK.createDate = :createDate"),
    @NamedQuery(name = "TblOperationLog.findByOperationPath", query = "SELECT t FROM TblOperationLog t WHERE t.tblOperationLogPK.operationPath = :operationPath"),
    @NamedQuery(name = "TblOperationLog.findByOperationParm", query = "SELECT t FROM TblOperationLog t WHERE t.operationParm = :operationParm"),
    @NamedQuery(name = "TblOperationLog.findByOperationProc", query = "SELECT t FROM TblOperationLog t WHERE t.operationProc = :operationProc"),
    @NamedQuery(name = "TblOperationLog.findByPK", query = "SELECT t FROM TblOperationLog t WHERE t.tblOperationLogPK.userId = :userId and t.tblOperationLogPK.createDate = :createDate and t.tblOperationLogPK.operationPath = :operationPath")
})
@Cacheable(value = false)
public class TblOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblOperationLogPK tblOperationLogPK;
    @Size(max = 1000)
    @Column(name = "OPERATION_PARM")
    private String operationParm;
    @Size(max = 45)
    @Column(name = "OPERATION_PROC")
    private String operationProc;
    @Size(max = 100)
    @Column(name = "DICT_KEY")
    private String dictKey;

    /*
     * ユーザーマスタ
     */
    @PrimaryKeyJoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUser;
    
    /*
     * 辞書マスタ
     */
    @PrimaryKeyJoinColumn(name = "DICT_KEY", referencedColumnName = "DICT_KEY")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstDictionary mstDictionary;
    
    public TblOperationLog() {
    }

    public TblOperationLog(TblOperationLogPK tblOperationLogPK) {
        this.tblOperationLogPK = tblOperationLogPK;
    }

    public TblOperationLog(String userId, Date createDate, String operationPath) {
        this.tblOperationLogPK = new TblOperationLogPK(userId, createDate, operationPath);
    }

    public TblOperationLogPK getTblOperationLogPK() {
        return tblOperationLogPK;
    }

    public void setTblOperationLogPK(TblOperationLogPK tblOperationLogPK) {
        this.tblOperationLogPK = tblOperationLogPK;
    }

    public String getOperationParm() {
        return operationParm;
    }

    public void setOperationParm(String operationParm) {
        this.operationParm = operationParm;
    }

    public String getOperationProc() {
        return operationProc;
    }

    public void setOperationProc(String operationProc) {
        this.operationProc = operationProc;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public MstUser getMstUser() {
        return mstUser;
    }

    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }

    public MstDictionary getMstDictionary() {
        return mstDictionary;
    }

    public void setMstDictionary(MstDictionary mstDictionary) {
        this.mstDictionary = mstDictionary;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblOperationLogPK != null ? tblOperationLogPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblOperationLog)) {
            return false;
        }
        TblOperationLog other = (TblOperationLog) object;
        if ((this.tblOperationLogPK == null && other.tblOperationLogPK != null) || (this.tblOperationLogPK != null && !this.tblOperationLogPK.equals(other.tblOperationLogPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.operation.log.TblOperationLog[ tblOperationLogPK=" + tblOperationLogPK + " ]";
    }
    
}
