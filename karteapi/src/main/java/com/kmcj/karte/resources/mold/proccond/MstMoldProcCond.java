/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond;

import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpec;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_mold_proc_cond")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldProcCond.findAll", query = "SELECT m FROM MstMoldProcCond m"),
    @NamedQuery(name = "MstMoldProcCond.findById", query = "SELECT m FROM MstMoldProcCond m WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldProcCond.findByMoldProcCondName", query = "SELECT m FROM MstMoldProcCond m WHERE m.moldProcCondName = :moldProcCondName"),
    @NamedQuery(name = "MstMoldProcCond.findBySeq", query = "SELECT m FROM MstMoldProcCond m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstMoldProcCond.findByCreateDate", query = "SELECT m FROM MstMoldProcCond m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldProcCond.findByUpdateDate", query = "SELECT m FROM MstMoldProcCond m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldProcCond.findByCreateUserUuid", query = "SELECT m FROM MstMoldProcCond m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldProcCond.findByUpdateUserUuid", query = "SELECT m FROM MstMoldProcCond m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldProcCond.findByMoldUuidAndMoldProcCondName", query = "SELECT m FROM MstMoldProcCond m WHERE  m.moldUuid = :moldUuid And m.moldProcCondName = :moldProcCondName "),
    @NamedQuery(name = "MstMoldProcCond.deleteByMoldUuid", query = "DELETE FROM MstMoldProcCond WHERE moldUuid = :moldUuid "),
    @NamedQuery(name = "MstMoldProcCond.findByMoldUuid", query = "SELECT m FROM MstMoldProcCond m WHERE m.moldUuid = :moldUuid "),
    @NamedQuery(name = "MstMoldProcCond.deleteById", query = "DELETE FROM MstMoldProcCond WHERE id = :id ")
})
@Cacheable(value = false)
public class MstMoldProcCond implements Serializable {

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    @OneToMany(cascade={CascadeType.PERSIST,CascadeType.REMOVE}, mappedBy = "mstMoldProcCond")
    private Collection<MstMoldProcCondSpec> mstMoldProcCondSpecCollection;

    private static long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "MOLD_PROC_COND_NAME")
    private String moldProcCondName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
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
    
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID",insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstMold mstMold;

    public MstMoldProcCond() {
    }

    public MstMoldProcCond(String id) {
        this.id = id;
    }

    public MstMoldProcCond(String id, int seq) {
        this.id = id;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoldProcCondName() {
        return moldProcCondName;
    }

    public void setMoldProcCondName(String moldProcCondName) {
        this.moldProcCondName = moldProcCondName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldProcCond)) {
            return false;
        }
        MstMoldProcCond other = (MstMoldProcCond) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.proccond.MstMoldProcCond[ id=" + getId() + " ]";
    }

    @XmlTransient
    public Collection<MstMoldProcCondSpec> getMstMoldProcCondSpecCollection() {
        return mstMoldProcCondSpecCollection;
    }

    public void setMstMoldProcCondSpecCollection(Collection<MstMoldProcCondSpec> mstMoldProcCondSpecCollection) {
        this.mstMoldProcCondSpecCollection = mstMoldProcCondSpecCollection;
    }

    /**
     * @return the moldUuid
     */
    public String getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldUuid the moldUuid to set
     */
    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    /**
     * @return the mstMold
     */
    public MstMold getMstMold() {
        return mstMold;
    }

    /**
     * @param mstMold the mstMold to set
     */
    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }
    
}
