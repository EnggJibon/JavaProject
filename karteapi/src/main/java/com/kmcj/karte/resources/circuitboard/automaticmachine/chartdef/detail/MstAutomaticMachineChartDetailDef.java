/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.detail;

import com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.MstAutomaticMachineChartDef;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "mst_automatic_machine_chart_detail_def")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findAll", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByAutomaticMachineChartDetailDefId", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.automaticMachineChartDetailDefId = :automaticMachineChartDetailDefId"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByPieChartColumn1", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.pieChartColumn1 = :pieChartColumn1"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByPieChartColumn2", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.pieChartColumn2 = :pieChartColumn2"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByPieChartColumn3", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.pieChartColumn3 = :pieChartColumn3"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByBarChartColumn1", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.barChartColumn1 = :barChartColumn1"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByBarChartColumn2", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.barChartColumn2 = :barChartColumn2"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByBarChartColumn3", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.barChartColumn3 = :barChartColumn3"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartXColumn1", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartXColumn1 = :lineChartXColumn1"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartXColumn2", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartXColumn2 = :lineChartXColumn2"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartXColumn3", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartXColumn3 = :lineChartXColumn3"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartXColumn4", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartXColumn4 = :lineChartXColumn4"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartXColumn5", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartXColumn5 = :lineChartXColumn5"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartYColumn1", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartYColumn1 = :lineChartYColumn1"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartYColumn2", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartYColumn2 = :lineChartYColumn2"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartYColumn3", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartYColumn3 = :lineChartYColumn3"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartYColumn4", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartYColumn4 = :lineChartYColumn4"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartYColumn5", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartYColumn5 = :lineChartYColumn5"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByBarChartMaxVal", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.barChartMaxVal = :barChartMaxVal"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByBarChartMinVal", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.barChartMinVal = :barChartMinVal"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartMaxVal", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartMaxVal = :lineChartMaxVal"),
    @NamedQuery(name = "MstAutomaticMachineChartDetailDef.findByLineChartMinVal", query = "SELECT m FROM MstAutomaticMachineChartDetailDef m WHERE m.lineChartMinVal = :lineChartMinVal")})
public class MstAutomaticMachineChartDetailDef implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "AUTOMATIC_MACHINE_CHART_DETAIL_DEF_ID")
    private String automaticMachineChartDetailDefId;
    @Size(max = 45)
    @Column(name = "PIE_CHART_COLUMN1")
    private String pieChartColumn1;
    @Size(max = 45)
    @Column(name = "PIE_CHART_COLUMN2")
    private String pieChartColumn2;
    @Size(max = 45)
    @Column(name = "PIE_CHART_COLUMN3")
    private String pieChartColumn3;
    @Size(max = 45)
    @Column(name = "BAR_CHART_COLUMN1")
    private String barChartColumn1;
    @Size(max = 45)
    @Column(name = "BAR_CHART_COLUMN2")
    private String barChartColumn2;
    @Size(max = 45)
    @Column(name = "BAR_CHART_COLUMN3")
    private String barChartColumn3;
    @Size(max = 45)
    @Column(name = "LINE_CHART_X_COLUMN1")
    private String lineChartXColumn1;
    @Size(max = 45)
    @Column(name = "LINE_CHART_X_COLUMN2")
    private String lineChartXColumn2;
    @Size(max = 45)
    @Column(name = "LINE_CHART_X_COLUMN3")
    private String lineChartXColumn3;
    @Size(max = 45)
    @Column(name = "LINE_CHART_X_COLUMN4")
    private String lineChartXColumn4;
    @Size(max = 45)
    @Column(name = "LINE_CHART_X_COLUMN5")
    private String lineChartXColumn5;
    @Size(max = 45)
    @Column(name = "LINE_CHART_Y_COLUMN1")
    private String lineChartYColumn1;
    @Size(max = 45)
    @Column(name = "LINE_CHART_Y_COLUMN2")
    private String lineChartYColumn2;
    @Size(max = 45)
    @Column(name = "LINE_CHART_Y_COLUMN3")
    private String lineChartYColumn3;
    @Size(max = 45)
    @Column(name = "LINE_CHART_Y_COLUMN4")
    private String lineChartYColumn4;
    @Size(max = 45)
    @Column(name = "LINE_CHART_Y_COLUMN5")
    private String lineChartYColumn5;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "BAR_CHART_MAX_VAL")
    private BigDecimal barChartMaxVal;
    @Column(name = "BAR_CHART_MIN_VAL")
    private BigDecimal barChartMinVal;
    @Column(name = "LINE_CHART_MAX_VAL")
    private BigDecimal lineChartMaxVal;
    @Column(name = "LINE_CHART_MIN_VAL")
    private BigDecimal lineChartMinVal;
    @JoinColumn(name = "AUTOMATED_MACHINE_CHART_DEF_ID", referencedColumnName = "AUTOMATIC_MACHINE_CHART_DEF_ID")
    @ManyToOne(optional = false)
    private MstAutomaticMachineChartDef automatedMachineChartDefId;

    public MstAutomaticMachineChartDetailDef() {
    }

    public MstAutomaticMachineChartDetailDef(String automaticMachineChartDetailDefId) {
        this.automaticMachineChartDetailDefId = automaticMachineChartDetailDefId;
    }

    public String getAutomaticMachineChartDetailDefId() {
        return automaticMachineChartDetailDefId;
    }

    public void setAutomaticMachineChartDetailDefId(String automaticMachineChartDetailDefId) {
        this.automaticMachineChartDetailDefId = automaticMachineChartDetailDefId;
    }

    public String getPieChartColumn1() {
        return pieChartColumn1;
    }

    public void setPieChartColumn1(String pieChartColumn1) {
        this.pieChartColumn1 = pieChartColumn1;
    }

    public String getPieChartColumn2() {
        return pieChartColumn2;
    }

    public void setPieChartColumn2(String pieChartColumn2) {
        this.pieChartColumn2 = pieChartColumn2;
    }

    public String getPieChartColumn3() {
        return pieChartColumn3;
    }

    public void setPieChartColumn3(String pieChartColumn3) {
        this.pieChartColumn3 = pieChartColumn3;
    }

    public String getBarChartColumn1() {
        return barChartColumn1;
    }

    public void setBarChartColumn1(String barChartColumn1) {
        this.barChartColumn1 = barChartColumn1;
    }

    public String getBarChartColumn2() {
        return barChartColumn2;
    }

    public void setBarChartColumn2(String barChartColumn2) {
        this.barChartColumn2 = barChartColumn2;
    }

    public String getBarChartColumn3() {
        return barChartColumn3;
    }

    public void setBarChartColumn3(String barChartColumn3) {
        this.barChartColumn3 = barChartColumn3;
    }

    public String getLineChartXColumn1() {
        return lineChartXColumn1;
    }

    public void setLineChartXColumn1(String lineChartXColumn1) {
        this.lineChartXColumn1 = lineChartXColumn1;
    }

    public String getLineChartXColumn2() {
        return lineChartXColumn2;
    }

    public void setLineChartXColumn2(String lineChartXColumn2) {
        this.lineChartXColumn2 = lineChartXColumn2;
    }

    public String getLineChartXColumn3() {
        return lineChartXColumn3;
    }

    public void setLineChartXColumn3(String lineChartXColumn3) {
        this.lineChartXColumn3 = lineChartXColumn3;
    }

    public String getLineChartXColumn4() {
        return lineChartXColumn4;
    }

    public void setLineChartXColumn4(String lineChartXColumn4) {
        this.lineChartXColumn4 = lineChartXColumn4;
    }

    public String getLineChartXColumn5() {
        return lineChartXColumn5;
    }

    public void setLineChartXColumn5(String lineChartXColumn5) {
        this.lineChartXColumn5 = lineChartXColumn5;
    }

    public String getLineChartYColumn1() {
        return lineChartYColumn1;
    }

    public void setLineChartYColumn1(String lineChartYColumn1) {
        this.lineChartYColumn1 = lineChartYColumn1;
    }

    public String getLineChartYColumn2() {
        return lineChartYColumn2;
    }

    public void setLineChartYColumn2(String lineChartYColumn2) {
        this.lineChartYColumn2 = lineChartYColumn2;
    }

    public String getLineChartYColumn3() {
        return lineChartYColumn3;
    }

    public void setLineChartYColumn3(String lineChartYColumn3) {
        this.lineChartYColumn3 = lineChartYColumn3;
    }

    public String getLineChartYColumn4() {
        return lineChartYColumn4;
    }

    public void setLineChartYColumn4(String lineChartYColumn4) {
        this.lineChartYColumn4 = lineChartYColumn4;
    }

    public String getLineChartYColumn5() {
        return lineChartYColumn5;
    }

    public void setLineChartYColumn5(String lineChartYColumn5) {
        this.lineChartYColumn5 = lineChartYColumn5;
    }

    public BigDecimal getBarChartMaxVal() {
        return barChartMaxVal;
    }

    public void setBarChartMaxVal(BigDecimal barChartMaxVal) {
        this.barChartMaxVal = barChartMaxVal;
    }

    public BigDecimal getBarChartMinVal() {
        return barChartMinVal;
    }

    public void setBarChartMinVal(BigDecimal barChartMinVal) {
        this.barChartMinVal = barChartMinVal;
    }

    public BigDecimal getLineChartMaxVal() {
        return lineChartMaxVal;
    }

    public void setLineChartMaxVal(BigDecimal lineChartMaxVal) {
        this.lineChartMaxVal = lineChartMaxVal;
    }

    public BigDecimal getLineChartMinVal() {
        return lineChartMinVal;
    }

    public void setLineChartMinVal(BigDecimal lineChartMinVal) {
        this.lineChartMinVal = lineChartMinVal;
    }

    public MstAutomaticMachineChartDef getAutomatedMachineChartDefId() {
        return automatedMachineChartDefId;
    }

    public void setAutomatedMachineChartDefId(MstAutomaticMachineChartDef automatedMachineChartDefId) {
        this.automatedMachineChartDefId = automatedMachineChartDefId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (automaticMachineChartDetailDefId != null ? automaticMachineChartDetailDefId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstAutomaticMachineChartDetailDef)) {
            return false;
        }
        MstAutomaticMachineChartDetailDef other = (MstAutomaticMachineChartDetailDef) object;
        if ((this.automaticMachineChartDetailDefId == null && other.automaticMachineChartDetailDefId != null) || (this.automaticMachineChartDetailDefId != null && !this.automaticMachineChartDetailDefId.equals(other.automaticMachineChartDetailDefId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.MstAutomaticMachineChartDetailDef[ automaticMachineChartDetailDefId=" + automaticMachineChartDetailDefId + " ]";
    }
    
}
