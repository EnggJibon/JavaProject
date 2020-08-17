/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.filedef;

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
 * @author liujiyong
 */
@Entity
@Table(name = "mst_automatic_machine_file_def")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstAutomaticMachineFileDef.findAll", query = "SELECT m FROM MstAutomaticMachineFileDef m"),
    @NamedQuery(name = "MstAutomaticMachineFileDef.findByAutomaticMachineFileDefId", query = "SELECT m FROM MstAutomaticMachineFileDef m WHERE m.automaticMachineFileDefId = :automaticMachineFileDefId"),
    @NamedQuery(name = "MstAutomaticMachineFileDef.findByColumnName", query = "SELECT m FROM MstAutomaticMachineFileDef m WHERE m.columnName = :columnName"),
    @NamedQuery(name = "MstAutomaticMachineFileDef.findByHeaderLabel", query = "SELECT m FROM MstAutomaticMachineFileDef m WHERE m.headerLabel = :headerLabel"),
    @NamedQuery(name = "MstAutomaticMachineFileDef.findByDefectiveContentFlg", query = "SELECT m FROM MstAutomaticMachineFileDef m WHERE m.defectiveContentFlg = :defectiveContentFlg"),
    @NamedQuery(name = "MstAutomaticMachineFileDef.findByAggregationKeyFlg", query = "SELECT m FROM MstAutomaticMachineFileDef m WHERE m.aggregationKeyFlg = :aggregationKeyFlg"),
    @NamedQuery(name = "MstAutomaticMachineFileDef.findByAggregationFlg", query = "SELECT m FROM MstAutomaticMachineFileDef m WHERE m.aggregationFlg = :aggregationFlg"),
    @NamedQuery(name = "MstAutomaticMachineFileDef.findByUseFlg", query = "SELECT m FROM MstAutomaticMachineFileDef m WHERE m.useFlg = :useFlg")})
public class MstAutomaticMachineFileDef implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "AUTOMATIC_MACHINE_FILE_DEF_ID")
    private String automaticMachineFileDefId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COLUMN_NAME")
    private String columnName;
    @Size(max = 100)
    @Column(name = "HEADER_LABEL")
    private String headerLabel;
    @Column(name = "DEFECTIVE_CONTENT_FLG")
    private Integer defectiveContentFlg;
    @Column(name = "AGGREGATION_KEY_FLG")
    private Integer aggregationKeyFlg;
    @Column(name = "AGGREGATION_FLG")
    private Integer aggregationFlg;
    @Column(name = "USE_FLG")
    private Integer useFlg;

    public MstAutomaticMachineFileDef() {
    }

    public MstAutomaticMachineFileDef(String automaticMachineFileDefId) {
        this.automaticMachineFileDefId = automaticMachineFileDefId;
    }

    public MstAutomaticMachineFileDef(String automaticMachineFileDefId, String columnName) {
        this.automaticMachineFileDefId = automaticMachineFileDefId;
        this.columnName = columnName;
    }

    public String getAutomaticMachineFileDefId() {
        return automaticMachineFileDefId;
    }

    public void setAutomaticMachineFileDefId(String automaticMachineFileDefId) {
        this.automaticMachineFileDefId = automaticMachineFileDefId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getHeaderLabel() {
        return headerLabel;
    }

    public void setHeaderLabel(String headerLabel) {
        this.headerLabel = headerLabel;
    }

    public Integer getDefectiveContentFlg() {
        return defectiveContentFlg;
    }

    public void setDefectiveContentFlg(Integer defectiveContentFlg) {
        this.defectiveContentFlg = defectiveContentFlg;
    }

    public Integer getAggregationKeyFlg() {
        return aggregationKeyFlg;
    }

    public void setAggregationKeyFlg(Integer aggregationKeyFlg) {
        this.aggregationKeyFlg = aggregationKeyFlg;
    }

    public Integer getAggregationFlg() {
        return aggregationFlg;
    }

    public void setAggregationFlg(Integer aggregationFlg) {
        this.aggregationFlg = aggregationFlg;
    }

    public Integer getUseFlg() {
        return useFlg;
    }

    public void setUseFlg(Integer useFlg) {
        this.useFlg = useFlg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (automaticMachineFileDefId != null ? automaticMachineFileDefId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstAutomaticMachineFileDef)) {
            return false;
        }
        MstAutomaticMachineFileDef other = (MstAutomaticMachineFileDef) object;
        if ((this.automaticMachineFileDefId == null && other.automaticMachineFileDefId != null) || (this.automaticMachineFileDefId != null && !this.automaticMachineFileDefId.equals(other.automaticMachineFileDefId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.MstAutomaticMachineFileDef[ automaticMachineFileDefId=" + automaticMachineFileDefId + " ]";
    }
    
}
