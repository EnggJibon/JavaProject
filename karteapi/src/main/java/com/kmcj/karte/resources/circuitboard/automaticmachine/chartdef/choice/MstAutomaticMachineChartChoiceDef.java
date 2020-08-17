/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.choice;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author liujiyong
 */
@Entity
@Table(name = "mst_automatic_machine_chart_choice_def")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findAll", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m"),
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findByAutomaticMachineType", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m WHERE m.mstAutomaticMachineChartChoiceDefPK.automaticMachineType = :automaticMachineType"),
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findByLogType", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m WHERE m.mstAutomaticMachineChartChoiceDefPK.logType = :logType"),
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findByGraphType", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m WHERE m.graphType = :graphType"),
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findBySeq", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m WHERE m.mstAutomaticMachineChartChoiceDefPK.seq = :seq"),
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findByColumnName", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m WHERE m.columnName = :columnName"),
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findByCreateDate", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findByUpdateDate", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findByCreateUserUuid", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstAutomaticMachineChartChoiceDef.findByUpdateUserUuid", query = "SELECT m FROM MstAutomaticMachineChartChoiceDef m WHERE m.updateUserUuid = :updateUserUuid")})
public class MstAutomaticMachineChartChoiceDef implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstAutomaticMachineChartChoiceDefPK mstAutomaticMachineChartChoiceDefPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GRAPH_TYPE")
    private int graphType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COLUMN_NAME")
    private String columnName;
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

    public MstAutomaticMachineChartChoiceDef() {
    }

    public MstAutomaticMachineChartChoiceDef(MstAutomaticMachineChartChoiceDefPK mstAutomaticMachineChartChoiceDefPK) {
        this.mstAutomaticMachineChartChoiceDefPK = mstAutomaticMachineChartChoiceDefPK;
    }

    public MstAutomaticMachineChartChoiceDef(MstAutomaticMachineChartChoiceDefPK mstAutomaticMachineChartChoiceDefPK, int graphType, String columnName) {
        this.mstAutomaticMachineChartChoiceDefPK = mstAutomaticMachineChartChoiceDefPK;
        this.graphType = graphType;
        this.columnName = columnName;
    }

    public MstAutomaticMachineChartChoiceDef(String automaticMachineType, String logType, int seq) {
        this.mstAutomaticMachineChartChoiceDefPK = new MstAutomaticMachineChartChoiceDefPK(automaticMachineType, logType, seq);
    }

    public MstAutomaticMachineChartChoiceDefPK getMstAutomaticMachineChartChoiceDefPK() {
        return mstAutomaticMachineChartChoiceDefPK;
    }

    public void setMstAutomaticMachineChartChoiceDefPK(MstAutomaticMachineChartChoiceDefPK mstAutomaticMachineChartChoiceDefPK) {
        this.mstAutomaticMachineChartChoiceDefPK = mstAutomaticMachineChartChoiceDefPK;
    }

    public int getGraphType() {
        return graphType;
    }

    public void setGraphType(int graphType) {
        this.graphType = graphType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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
        hash += (mstAutomaticMachineChartChoiceDefPK != null ? mstAutomaticMachineChartChoiceDefPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstAutomaticMachineChartChoiceDef)) {
            return false;
        }
        MstAutomaticMachineChartChoiceDef other = (MstAutomaticMachineChartChoiceDef) object;
        if ((this.mstAutomaticMachineChartChoiceDefPK == null && other.mstAutomaticMachineChartChoiceDefPK != null) || (this.mstAutomaticMachineChartChoiceDefPK != null && !this.mstAutomaticMachineChartChoiceDefPK.equals(other.mstAutomaticMachineChartChoiceDefPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.choice.MstAutomaticMachineChartChoiceDef[ mstAutomaticMachineChartChoiceDefPK=" + mstAutomaticMachineChartChoiceDefPK + " ]";
    }
    
}
