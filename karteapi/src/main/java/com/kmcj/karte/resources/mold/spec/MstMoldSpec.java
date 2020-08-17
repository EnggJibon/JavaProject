/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec;

import com.kmcj.karte.resources.mold.attribute.MstMoldAttribute;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistory;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author CH
 */
@Entity
@Table(name = "mst_mold_spec")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldSpec.findAll", query = "SELECT m FROM MstMoldSpec m"),
    @NamedQuery(name = "MstMoldSpec.findById", query = "SELECT m FROM MstMoldSpec m WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldSpec.findByMoldSpecHstId", query = "SELECT m FROM MstMoldSpec m WHERE m.mstMoldSpecPK.moldSpecHstId = :moldSpecHstId"),
    @NamedQuery(name = "MstMoldSpec.findByMoldUuid", query = "SELECT ms FROM MstMoldSpec ms join ms.mstMoldSpecHistory msh join msh.mstMold m WHERE m.uuid = :moldUuid"),
    @NamedQuery(name = "MstMoldSpec.findByAttrId", query = "SELECT m FROM MstMoldSpec m WHERE m.mstMoldSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstMoldSpec.findByAttrValue", query = "SELECT m FROM MstMoldSpec m WHERE m.attrValue = :attrValue"),
    @NamedQuery(name = "MstMoldSpec.findByCreateDate", query = "SELECT m FROM MstMoldSpec m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldSpec.findByUpdateDate", query = "SELECT m FROM MstMoldSpec m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldSpec.findByCreateUserUuid", query = "SELECT m FROM MstMoldSpec m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldSpec.findByUpdateUserUuid", query = "SELECT m FROM MstMoldSpec m WHERE m.updateUserUuid = :updateUserUuid"),
    // 金型一覧で仕様のＣＳＶ出力
//    @NamedQuery(name = "MstMoldSpec.findByMoldListSpec", query = "SELECT m1  FROM MstMoldSpec m1  LEFT JOIN MstMoldAttribute m ON m.id = m1.mstMoldSpecPK.attrId  WHERE m1.mstMoldSpecPK.moldSpecHstId = :moldSpecHstId  order by m.seq "),
    @NamedQuery(name = "MstMoldSpec.findByMoldListSpec", query = "SELECT m1 FROM MstMoldSpec m1 LEFT JOIN MstMoldAttribute m ON m.id = m1.mstMoldSpecPK.attrId WHERE m1.mstMoldSpecPK.moldSpecHstId = :moldSpecHstId and m.moldType = :moldType and m.externalFlg = :externalFlg order by m.seq "),
    
    @NamedQuery(name = "MstMoldSpec.findByByMoldSpecPk", query = "SELECT m FROM MstMoldSpec m WHERE m.mstMoldSpecPK.attrId = :attrId and m.mstMoldSpecPK.moldSpecHstId = :moldSpecHstId"),
    @NamedQuery(name = "MstMoldSpec.findAttrNameAndValue", query = "SELECT m FROM MstMoldSpec m JOIN FETCH m.mstMoldAttribute ma JOIN FETCH m.mstMoldSpecHistory mh WHERE m.mstMoldAttribute.id = m.mstMoldSpecPK.attrId and m.mstMoldSpecPK.moldSpecHstId = m.mstMoldSpecHistory.id and m.mstMoldSpecPK.moldSpecHstId = :moldSpecHstId "),
    @NamedQuery(name = "MstMoldSpec.delete", query = "DELETE FROM MstMoldSpec m WHERE m.mstMoldSpecPK.moldSpecHstId = :moldSpecHstId"),
//    @NamedQuery(name = "MstMoldSpec.deleteByMoldUuid", query = "DELETE FROM MstMoldSpec m WHERE m.mstMoldSpecHistory.mstMold.uuid = :moldUuid"),
    @NamedQuery(name = "MstMoldSpec.findByMoldSpecHstIdAndAttrId", query = "SELECT m FROM MstMoldSpec m WHERE m.mstMoldSpecPK.moldSpecHstId = :moldSpecHstId And m.mstMoldSpecPK.attrId = :attrId"),
    //FK check
    @NamedQuery(name = "MstMoldSpec.findFKByAttrId", query = "SELECT m FROM MstMoldSpec m WHERE m.mstMoldSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstMoldSpec.updateByMoldSpecHstIdAndAttrId", query = "UPDATE MstMoldSpec m SET "
            + "m.attrValue = :attrValue,"
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid "
            + "WHERE m.mstMoldSpecPK.moldSpecHstId = :moldSpecHstId "
            + "AND m.mstMoldSpecPK.attrId = :attrId")
})
@Cacheable(value = false)
public class MstMoldSpec implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected MstMoldSpecPK mstMoldSpecPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 256)
    @Column(name = "ATTR_VALUE")
    private String attrValue;
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

//@JoinColumn(name = "ATTR_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @PrimaryKeyJoinColumn(name = "ATTR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private MstMoldAttribute mstMoldAttribute;

//@JoinColumn(name = "MOLD_SPEC_HST_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @PrimaryKeyJoinColumn(name = "MOLD_SPEC_HST_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false,cascade = {CascadeType.REMOVE})
    private MstMoldSpecHistory mstMoldSpecHistory;

    public MstMoldSpec() {
    }

    public MstMoldSpec(MstMoldSpecPK mstMoldSpecPK) {
        this.mstMoldSpecPK = mstMoldSpecPK;
    }

    public MstMoldSpec(MstMoldSpecPK mstMoldSpecPK, String id) {
        this.mstMoldSpecPK = mstMoldSpecPK;
        this.id = id;
    }

    public MstMoldSpec(String moldSpecHstId, String attrId) {
        this.mstMoldSpecPK = new MstMoldSpecPK(moldSpecHstId, attrId);
    }

    public MstMoldSpecPK getMstMoldSpecPK() {
        return mstMoldSpecPK;
    }

    public void setMstMoldSpecPK(MstMoldSpecPK mstMoldSpecPK) {
        this.mstMoldSpecPK = mstMoldSpecPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
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

    public MstMoldAttribute getMstMoldAttribute() {
        return mstMoldAttribute;
    }

    public void setMstMoldAttribute(MstMoldAttribute mstMoldAttribute) {
        this.mstMoldAttribute = mstMoldAttribute;
    }

    public MstMoldSpecHistory getMstMoldSpecHistory() {
        return mstMoldSpecHistory;
    }

    public void setMstMoldSpecHistory(MstMoldSpecHistory mstMoldSpecHistory) {
        this.mstMoldSpecHistory = mstMoldSpecHistory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstMoldSpecPK != null ? mstMoldSpecPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldSpec)) {
            return false;
        }
        MstMoldSpec other = (MstMoldSpec) object;
        if ((this.mstMoldSpecPK == null && other.mstMoldSpecPK != null) || (this.mstMoldSpecPK != null && !this.mstMoldSpecPK.equals(other.mstMoldSpecPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.spec.MstMoldSpec[ mstMoldSpecPK=" + mstMoldSpecPK + " ]";
    }

}
