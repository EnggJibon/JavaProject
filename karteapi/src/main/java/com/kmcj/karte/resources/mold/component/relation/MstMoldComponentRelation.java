/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.component.relation;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.procedure.MstProcedure;
import java.io.Serializable;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_mold_component_relation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldComponentRelation.findAll", query = "SELECT m FROM MstMoldComponentRelation m"),
    @NamedQuery(name = "MstMoldComponentRelation.findByMoldUuid", query = "SELECT m FROM MstMoldComponentRelation m WHERE m.mstMoldComponentRelationPK.moldUuid = :moldUuid And m.mstMoldComponentRelationPK.componentId = :componentId"),
    @NamedQuery(name = "MstMoldComponentRelation.findByComponentId", query = "SELECT m FROM MstMoldComponentRelation m JOIN FETCH m.mstMold WHERE m.mstMoldComponentRelationPK.componentId = :componentId order by m.mstMold.moldId "),
    @NamedQuery(name = "MstMoldComponentRelation.findByComponentIdWithoutDispose", query = "SELECT m FROM MstMoldComponentRelation m JOIN FETCH m.mstMold WHERE m.mstMoldComponentRelationPK.componentId = :componentId and m.mstMold.status <> 9 order by m.mstMold.moldId "),
    @NamedQuery(name = "MstMoldComponentRelation.findByCreateDate", query = "SELECT m FROM MstMoldComponentRelation m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldComponentRelation.findByUpdateDate", query = "SELECT m FROM MstMoldComponentRelation m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldComponentRelation.findByCreateUserUuid", query = "SELECT m FROM MstMoldComponentRelation m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldComponentRelation.findByUpdateUserUuid", query = "SELECT m FROM MstMoldComponentRelation m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldComponentRelation.findSql", query = "SELECT m FROM MstMoldComponentRelation m JOIN FETCH m.mstComponent JOIN FETCH m.mstMold WHERE  m.mstMoldComponentRelationPK.moldUuid=:uuid"),
    @NamedQuery(name = "MstMoldComponentRelation.deleteBymoldUuid", query = "DELETE FROM MstMoldComponentRelation m WHERE m.mstMold.uuid = :moldUuid"),
    @NamedQuery(name = "MstMoldComponentRelation.deleteByComponentId", query = "DELETE FROM MstMoldComponentRelation m WHERE m.mstMoldComponentRelationPK.componentId = :componentId"),
    @NamedQuery(name = "MstMoldComponentRelation.deleteByComponentIdAndMoldUuid", query = ""
            + "DELETE FROM MstMoldComponentRelation m "
            + "WHERE m.mstMoldComponentRelationPK.componentId = :componentId "
            + "AND m.mstMoldComponentRelationPK.moldUuid =:moldUuid ")

})
@Cacheable(value = false)
public class MstMoldComponentRelation implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstMoldComponentRelationPK mstMoldComponentRelationPK;
    
    @Size(max = 45)
    @Column(name = "PROCEDURE_ID")
    private String procedureId;
    @Column(name = "COUNT_PER_SHOT")
    private Integer countPerShot;
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

    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstComponent mstComponent;

    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstMold mstMold;
    
    @JoinColumn(name = "PROCEDURE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstProcedure mstProcedure;

    public MstMoldComponentRelation() {
    }

    public MstMoldComponentRelation(MstMoldComponentRelationPK mstMoldComponentRelationPK) {
        this.mstMoldComponentRelationPK = mstMoldComponentRelationPK;
    }

    public MstMoldComponentRelation(String moldUuid, String componentId) {
        this.mstMoldComponentRelationPK = new MstMoldComponentRelationPK(moldUuid, componentId);
    }

    public MstMoldComponentRelationPK getMstMoldComponentRelationPK() {
        return mstMoldComponentRelationPK;
    }

    public void setMstMoldComponentRelationPK(MstMoldComponentRelationPK mstMoldComponentRelationPK) {
        this.mstMoldComponentRelationPK = mstMoldComponentRelationPK;
    }
    
    
    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }
    
    public Integer getCountPerShot() {
        return countPerShot;
    }

    public void setCountPerShot(Integer countPerShot) {
        this.countPerShot = countPerShot;
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

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public MstMold getMstMold() {
        return mstMold;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }
    
    public MstProcedure getMstProcedure() {
        return mstProcedure;
    }

    public void setMstProcedure(MstProcedure mstProcedure) {
        this.mstProcedure = mstProcedure;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstMoldComponentRelationPK != null ? mstMoldComponentRelationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldComponentRelation)) {
            return false;
        }
        MstMoldComponentRelation other = (MstMoldComponentRelation) object;
        if ((this.mstMoldComponentRelationPK == null && other.mstMoldComponentRelationPK != null) || (this.mstMoldComponentRelationPK != null && !this.mstMoldComponentRelationPK.equals(other.mstMoldComponentRelationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation[ mstMoldComponentRelationPK=" + mstMoldComponentRelationPK + " ]";
    }

}
