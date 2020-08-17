/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.procedure;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.production.plan.TblProductionPlan;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 工程マスタ（部品ごとの製造手順）エンティティ
 * @author t.ariki
 */
@Entity
@Table(name = "mst_procedure")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstProcedure.findAll", query = "SELECT m FROM MstProcedure m"),
    @NamedQuery(name = "MstProcedure.findById", query = "SELECT m FROM MstProcedure m WHERE m.id = :id"),
    @NamedQuery(name = "MstProcedure.findByComponentId", query = "SELECT m FROM MstProcedure m WHERE m.componentId = :componentId"),
    @NamedQuery(name = "MstProcedure.findByProcedureCode", query = "SELECT m FROM MstProcedure m WHERE m.procedureCode = :procedureCode"),
    @NamedQuery(name = "MstProcedure.findByProcedureName", query = "SELECT m FROM MstProcedure m WHERE m.procedureName = :procedureName"),
    @NamedQuery(name = "MstProcedure.findBySeq", query = "SELECT m FROM MstProcedure m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstProcedure.findByFinalFlg", query = "SELECT m FROM MstProcedure m WHERE m.finalFlg = :finalFlg"),
    @NamedQuery(name = "MstProcedure.findByExternalFlg", query = "SELECT m FROM MstProcedure m WHERE m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstProcedure.findByCreateDate", query = "SELECT m FROM MstProcedure m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstProcedure.findByUpdateDate", query = "SELECT m FROM MstProcedure m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstProcedure.findByCreateUserUuid", query = "SELECT m FROM MstProcedure m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstProcedure.findByUpdateUserUuid", query = "SELECT m FROM MstProcedure m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstProcedure.deleteById", query = "DELETE FROM MstProcedure m WHERE m.id = :id AND m.externalFlg = :externalFlg ")
})
@Cacheable(value = false)
public class MstProcedure implements Serializable {

    @OneToMany(mappedBy = "mstProcedure")
    private Collection<TblProductionPlan> tblProductionPlanCollection;

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
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PROCEDURE_CODE")
    private String procedureCode;
    @Size(max = 45)
    @Column(name = "PROCEDURE_NAME")
    private String procedureName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FINAL_FLG")
    private int finalFlg;
    @Column(name = "EXTERNAL_FLG")
    private Integer externalFlg;
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
    
    @Column(name = "INSTRATION_SITE_ID")
    private String installationSiteId;

    @JoinColumn(name = "INSTRATION_SITE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstInstallationSite mstInstallationSite;
    
    
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID" ,insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    
    // 生産実績で使用 金型部品関係マスタを部品IDのみで検索した結果を格納
    @Transient
    List<MstMoldComponentRelation> mstMoldComponentRelation;
    public List<MstMoldComponentRelation> getMstMoldComponentRelation() {
        return mstMoldComponentRelation;
    }
    public void setMstMoldComponentRelation(List<MstMoldComponentRelation> mstMoldComponentRelation) {
        this.mstMoldComponentRelation = mstMoldComponentRelation;
    }
    
    public MstProcedure() {
    }

    public MstProcedure(String id) {
        this.id = id;
    }

    public MstProcedure(String id, String componentId, String procedureCode, int seq, int finalFlg) {
        this.id = id;
        this.componentId = componentId;
        this.procedureCode = procedureCode;
        this.seq = seq;
        this.finalFlg = finalFlg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getFinalFlg() {
        return finalFlg;
    }

    public void setFinalFlg(int finalFlg) {
        this.finalFlg = finalFlg;
    }

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstProcedure)) {
            return false;
        }
        MstProcedure other = (MstProcedure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.procedure.MstProcedure[ id=" + id + " ]";
    }

    /**
     * @return the mstComponent
     */
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    @XmlTransient
    public Collection<TblProductionPlan> getTblProductionPlanCollection() {
        return tblProductionPlanCollection;
    }

    public void setTblProductionPlanCollection(Collection<TblProductionPlan> tblProductionPlanCollection) {
        this.tblProductionPlanCollection = tblProductionPlanCollection;
    }

    /**
     * @return the installationSiteId
     */
    public String getInstallationSiteId() {
        return installationSiteId;
    }

    /**
     * @param installationSiteId the installationSiteId to set
     */
    public void setInstallationSiteId(String installationSiteId) {
        this.installationSiteId = installationSiteId;
    }

    /**
     * @return the mstInstallationSite
     */
    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    /**
     * @param mstInstallationSite the mstInstallationSite to set
     */
    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }

    
}
