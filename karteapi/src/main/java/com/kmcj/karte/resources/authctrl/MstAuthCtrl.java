/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authctrl;

import com.kmcj.karte.resources.function.MstFunction;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "mst_auth_ctrl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstAuthCtrl.findByPK", query =
            "SELECT m FROM MstAuthCtrl m WHERE m.mstAuthCtrlPK.authId = :authId AND m.mstAuthCtrlPK.functionId = :functionId"),
    @NamedQuery(name = "MstAuthCtrl.findAll", query = "SELECT m FROM MstAuthCtrl m"),
    @NamedQuery(name = "MstAuthCtrl.findByAuthId", query = "SELECT m FROM MstAuthCtrl m WHERE m.mstAuthCtrlPK.authId = :authId"),
    @NamedQuery(name = "MstAuthCtrl.findByAuthIdAvailable", query = "SELECT m FROM MstAuthCtrl m WHERE m.mstAuthCtrlPK.authId = :authId AND m.available = 1"),
    @NamedQuery(name = "MstAuthCtrl.findByFunctionId", query = "SELECT m FROM MstAuthCtrl m WHERE m.mstAuthCtrlPK.functionId = :functionId"),
    @NamedQuery(name = "MstAuthCtrl.findByAvailable", query = "SELECT m FROM MstAuthCtrl m WHERE m.available = :available"),
    @NamedQuery(name = "MstAuthCtrl.findByCreateDate", query = "SELECT m FROM MstAuthCtrl m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstAuthCtrl.findByUpdateDate", query = "SELECT m FROM MstAuthCtrl m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstAuthCtrl.findByCreateUserUuid", query = "SELECT m FROM MstAuthCtrl m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstAuthCtrl.findByUpdateUserUuid", query = "SELECT m FROM MstAuthCtrl m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstAuthCtrl.deleteAll", query = "DELETE FROM MstAuthCtrl")
})

@Cacheable(value = false)
public class MstAuthCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstAuthCtrlPK mstAuthCtrlPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AVAILABLE")
    private int available;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;

    
    //@Transient
    //private final String authId = null;
    //@Transient
    //private final String functionId = null;


    public MstAuthCtrl() {
    }

    public MstAuthCtrl(MstAuthCtrlPK mstAuthCtrlPK) {
        this.mstAuthCtrlPK = mstAuthCtrlPK;
    }

    public MstAuthCtrl(MstAuthCtrlPK mstAuthCtrlPK, int available) {
        this.mstAuthCtrlPK = mstAuthCtrlPK;
        this.available = available;
    }

    public MstAuthCtrl(String authId, String functionId) {
        this.mstAuthCtrlPK = new MstAuthCtrlPK(authId, functionId);
    }

    @XmlTransient
    public MstAuthCtrlPK getMstAuthCtrlPK() {
        return mstAuthCtrlPK;
    }

    public void setMstAuthCtrlPK(MstAuthCtrlPK mstAuthCtrlPK) {
        this.mstAuthCtrlPK = mstAuthCtrlPK;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstAuthCtrlPK != null ? mstAuthCtrlPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstAuthCtrl)) {
            return false;
        }
        MstAuthCtrl other = (MstAuthCtrl) object;
        if ((this.mstAuthCtrlPK == null && other.mstAuthCtrlPK != null) || (this.mstAuthCtrlPK != null && !this.mstAuthCtrlPK.equals(other.mstAuthCtrlPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.authorization.MstAuthCtrl[ mstAuthCtrlPK=" + mstAuthCtrlPK + " ]";
    }

    /**
     * @return the authId
     */
    @XmlElement
    public String getAuthId() {
        return this.mstAuthCtrlPK.getAuthId(); //authId;
    }

    /**
     * @param authId the authId to set
     */
    @XmlElement
    public void setAuthId(String authId) {
        if (mstAuthCtrlPK == null) {
            mstAuthCtrlPK = new MstAuthCtrlPK();
        }
        this.mstAuthCtrlPK.setAuthId(authId);
    }

    /**
     * @return the functionId
     */
    @XmlElement
    public String getFunctionId() {
        return this.mstAuthCtrlPK.getFunctionId(); // functionId;
    }

    /**
     * @param functionId the functionId to set
     */
    @XmlElement
    public void setFunctionId(String functionId) {
        if (mstAuthCtrlPK == null) {
            mstAuthCtrlPK = new MstAuthCtrlPK();
        }
        this.mstAuthCtrlPK.setFunctionId(functionId);
    }
    
}
