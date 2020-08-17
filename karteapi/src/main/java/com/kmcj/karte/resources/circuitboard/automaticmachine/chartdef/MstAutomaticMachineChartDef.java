/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef;

import com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.detail.MstAutomaticMachineChartDetailDef;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author liujiyong
 */
@Entity
@Table(name = "mst_automatic_machine_chart_def")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstAutomaticMachineChartDef.findAll", query = "SELECT m FROM MstAutomaticMachineChartDef m"),
    @NamedQuery(name = "MstAutomaticMachineChartDef.findByAutomaticMachineChartDefId", query = "SELECT m FROM MstAutomaticMachineChartDef m WHERE m.automaticMachineChartDefId = :automaticMachineChartDefId"),
    @NamedQuery(name = "MstAutomaticMachineChartDef.findByFileNameForDisplay", query = "SELECT m FROM MstAutomaticMachineChartDef m WHERE m.fileNameForDisplay = :fileNameForDisplay"),
    @NamedQuery(name = "MstAutomaticMachineChartDef.findByDisplayAreaId", query = "SELECT m FROM MstAutomaticMachineChartDef m WHERE m.displayAreaId = :displayAreaId"),
    @NamedQuery(name = "MstAutomaticMachineChartDef.findByGraphType", query = "SELECT m FROM MstAutomaticMachineChartDef m WHERE m.graphType = :graphType"),
    @NamedQuery(name = "MstAutomaticMachineChartDef.findByUseFlg", query = "SELECT m FROM MstAutomaticMachineChartDef m WHERE m.useFlg = :useFlg")})
public class MstAutomaticMachineChartDef implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "AUTOMATIC_MACHINE_CHART_DEF_ID")
    private String automaticMachineChartDefId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "FILE_NAME_FOR_DISPLAY")
    private String fileNameForDisplay;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DISPLAY_AREA_ID")
    private String displayAreaId;
    @Column(name = "GRAPH_TYPE")
    private Integer graphType;
    @Column(name = "USE_FLG")
    private Integer useFlg;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "automatedMachineChartDefId")
    private Collection<MstAutomaticMachineChartDetailDef> mstAutomaticMachineChartDetailDefCollection;

    public MstAutomaticMachineChartDef() {
    }

    public MstAutomaticMachineChartDef(String automaticMachineChartDefId) {
        this.automaticMachineChartDefId = automaticMachineChartDefId;
    }

    public MstAutomaticMachineChartDef(String automaticMachineChartDefId, String fileNameForDisplay, String displayAreaId) {
        this.automaticMachineChartDefId = automaticMachineChartDefId;
        this.fileNameForDisplay = fileNameForDisplay;
        this.displayAreaId = displayAreaId;
    }

    public String getAutomaticMachineChartDefId() {
        return automaticMachineChartDefId;
    }

    public void setAutomaticMachineChartDefId(String automaticMachineChartDefId) {
        this.automaticMachineChartDefId = automaticMachineChartDefId;
    }

    public String getFileNameForDisplay() {
        return fileNameForDisplay;
    }

    public void setFileNameForDisplay(String fileNameForDisplay) {
        this.fileNameForDisplay = fileNameForDisplay;
    }

    public String getDisplayAreaId() {
        return displayAreaId;
    }

    public void setDisplayAreaId(String displayAreaId) {
        this.displayAreaId = displayAreaId;
    }

    public Integer getGraphType() {
        return graphType;
    }

    public void setGraphType(Integer graphType) {
        this.graphType = graphType;
    }

    public Integer getUseFlg() {
        return useFlg;
    }

    public void setUseFlg(Integer useFlg) {
        this.useFlg = useFlg;
    }

    @XmlTransient
    public Collection<MstAutomaticMachineChartDetailDef> getMstAutomaticMachineChartDetailDefCollection() {
        return mstAutomaticMachineChartDetailDefCollection;
    }

    public void setMstAutomaticMachineChartDetailDefCollection(Collection<MstAutomaticMachineChartDetailDef> mstAutomaticMachineChartDetailDefCollection) {
        this.mstAutomaticMachineChartDetailDefCollection = mstAutomaticMachineChartDetailDefCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (automaticMachineChartDefId != null ? automaticMachineChartDefId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstAutomaticMachineChartDef)) {
            return false;
        }
        MstAutomaticMachineChartDef other = (MstAutomaticMachineChartDef) object;
        if ((this.automaticMachineChartDefId == null && other.automaticMachineChartDefId != null) || (this.automaticMachineChartDefId != null && !this.automaticMachineChartDefId.equals(other.automaticMachineChartDefId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.MstAutomaticMachineChartDef[ automaticMachineChartDefId=" + automaticMachineChartDefId + " ]";
    }
    
}
