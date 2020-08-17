/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kmc
 */
@Entity
@Table(name = "mst_operation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstOperation.findAll", query = "SELECT m FROM MstOperation m"),
    @NamedQuery(name = "MstOperation.findById", query = "SELECT m FROM MstOperation m WHERE m.id = :id"),
    @NamedQuery(name = "MstOperation.findByOperationProc", query = "SELECT m FROM MstOperation m WHERE m.operationProc = :operationProc"),
    @NamedQuery(name = "MstOperation.findByOperationPath", query = "SELECT m FROM MstOperation m WHERE m.operationPath = :operationPath"),
    @NamedQuery(name = "MstOperation.findByOperationParm", query = "SELECT m FROM MstOperation m WHERE m.operationParm = :operationParm"),
    @NamedQuery(name = "MstOperation.findByParmValue", query = "SELECT m FROM MstOperation m WHERE m.parmValue = :parmValue"),
    @NamedQuery(name = "MstOperation.findByDictKey", query = "SELECT m FROM MstOperation m WHERE m.dictKey = :dictKey"),
    @NamedQuery(name = "MstOperation.findByOperationProcPath", query = "SELECT m FROM MstOperation m WHERE m.operationProc = :operationProc and  m.operationPath = :operationPath"),
    @NamedQuery(name = "MstOperation.findByOperationProcPathParm", query = "SELECT m FROM MstOperation m WHERE m.operationProc = :operationProc and  m.operationPath = :operationPath and m.operationParm = :operationParm and m.parmValue = :parmValue")
})
public class MstOperation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "OPERATION_PROC")
    private String operationProc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "OPERATION_PATH")
    private String operationPath;
    @Size(max = 255)
    @Column(name = "OPERATION_PARM")
    private String operationParm;
    @Size(max = 255)
    @Column(name = "PARM_VALUE")
    private String parmValue;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DICT_KEY")
    private String dictKey;

    public MstOperation() {
    }

    public MstOperation(String id) {
        this.id = id;
    }

    public MstOperation(String id, String operationProc, String operationPath, String dictKey) {
        this.id = id;
        this.operationProc = operationProc;
        this.operationPath = operationPath;
        this.dictKey = dictKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationProc() {
        return operationProc;
    }

    public void setOperationProc(String operationProc) {
        this.operationProc = operationProc;
    }

    public String getOperationPath() {
        return operationPath;
    }

    public void setOperationPath(String operationPath) {
        this.operationPath = operationPath;
    }

    public String getOperationParm() {
        return operationParm;
    }

    public void setOperationParm(String operationParm) {
        this.operationParm = operationParm;
    }

    public String getParmValue() {
        return parmValue;
    }

    public void setParmValue(String parmValue) {
        this.parmValue = parmValue;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstOperation)) {
            return false;
        }
        MstOperation other = (MstOperation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.operation.MstOperation[ id=" + id + " ]";
    }
    
}
