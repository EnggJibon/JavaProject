/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.log;

import com.kmcj.karte.resources.circuitboard.productionline.MstProductionLine;
import com.kmcj.karte.resources.machine.MstMachine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author liujiyong
 */
@Entity
@Table(name = "tbl_automatic_machine_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblAutomaticMachineLog.findAll", query = "SELECT t FROM TblAutomaticMachineLog t")
})
public class TblAutomaticMachineLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private TblAutomaticMachineLogPK tblAutomaticMachineLogPK;
    @Size(max = 45)
    @Column(name = "MACHINE_TYPE")
    @NotNull
    private String machineType;
    //@Size(min = 1, max = 3)
    @Column(name = "LINE_NUMBER")
    @NotNull
    private int lineNumber;
    @Size(max = 45)
    @Column(name = "CIRCUIT_BOARD_CODE")
    @NotNull
    private String circuitBoardCode;
    @Size(max = 45)
    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;
    @Size(max = 2)
    @Column(name = "LOG_TYPE")
    private String logType;
    @Size(max = 800)
    @Column(name = "TXTCOL1")
    private String txtcol1;
    @Size(max = 800)
    @Column(name = "TXTCOL2")
    private String txtcol2;
    @Size(max = 800)
    @Column(name = "TXTCOL3")
    private String txtcol3;
    @Size(max = 800)
    @Column(name = "TXTCOL4")
    private String txtcol4;
    @Size(max = 800)
    @Column(name = "TXTCOL5")
    private String txtcol5;
    @Size(max = 800)
    @Column(name = "TXTCOL6")
    private String txtcol6;
    @Size(max = 800)
    @Column(name = "TXTCOL7")
    private String txtcol7;
    @Size(max = 800)
    @Column(name = "TXTCOL8")
    private String txtcol8;
    @Size(max = 800)
    @Column(name = "TXTCOL9")
    private String txtcol9;
    @Size(max = 800)
    @Column(name = "TXTCOL10")
    private String txtcol10;
    @Size(max = 800)
    @Column(name = "TXTCOL11")
    private String txtcol11;
    @Size(max = 800)
    @Column(name = "TXTCOL12")
    private String txtcol12;
    @Size(max = 800)
    @Column(name = "TXTCOL13")
    private String txtcol13;
    @Size(max = 800)
    @Column(name = "TXTCOL14")
    private String txtcol14;
    @Size(max = 800)
    @Column(name = "TXTCOL15")
    private String txtcol15;
    @Column(name = "NUMCOL1")
    private BigDecimal numcol1;
    @Column(name = "NUMCOL2")
    private BigDecimal numcol2;
    @Column(name = "NUMCOL3")
    private BigDecimal numcol3;
    @Column(name = "NUMCOL4")
    private BigDecimal numcol4;
    @Column(name = "NUMCOL5")
    private BigDecimal numcol5;
    @Column(name = "NUMCOL6")
    private BigDecimal numcol6;
    @Column(name = "NUMCOL7")
    private BigDecimal numcol7;
    @Column(name = "NUMCOL8")
    private BigDecimal numcol8;
    @Column(name = "NUMCOL9")
    private BigDecimal numcol9;
    @Column(name = "NUMCOL10")
    private BigDecimal numcol10;
    @Column(name = "NUMCOL11")
    private BigDecimal numcol11;
    @Column(name = "NUMCOL12")
    private BigDecimal numcol12;
    @Column(name = "NUMCOL13")
    private BigDecimal numcol13;
    @Column(name = "NUMCOL14")
    private BigDecimal numcol14;
    @Column(name = "NUMCOL15")
    private BigDecimal numcol15;
    @Column(name = "DTCOL1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol1;
    @Column(name = "DTCOL2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol2;
    @Column(name = "DTCOL3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol3;
    @Column(name = "DTCOL4")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol4;
    @Column(name = "DTCOL5")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol5;
    @Column(name = "DTCOL6")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol6;
    @Column(name = "DTCOL7")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol7;
    @Column(name = "DTCOL8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol8;
    @Column(name = "DTCOL9")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol9;
    @Column(name = "DTCOL10")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcol10;

    /**
     * 設備マスタ
     */
    @PrimaryKeyJoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;

    /**
     * 生産ラインマスタ
     */
    @JoinColumn(name = "LINE_NUMBER", referencedColumnName = "PRODUCTION_LINE_ID", insertable = false, updatable = false)
    @ManyToOne
    private MstProductionLine mstProductionLine;

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public MstProductionLine getMstProductionLine() {
        return mstProductionLine;
    }

    public void setMstProductionLine(MstProductionLine mstProductionLine) {
        this.mstProductionLine = mstProductionLine;
    }

    public TblAutomaticMachineLog() {
    }

    public TblAutomaticMachineLog(TblAutomaticMachineLogPK tblAutomaticMachineLogPK) {
        this.tblAutomaticMachineLogPK = tblAutomaticMachineLogPK;
    }

    public TblAutomaticMachineLog(String machineUuid, long eventNo, Date createDate) {
        this.tblAutomaticMachineLogPK = new TblAutomaticMachineLogPK(machineUuid, eventNo, createDate);
    }

    public TblAutomaticMachineLogPK getTblAutomaticMachineLogPK() {
        return tblAutomaticMachineLogPK;
    }

    public void setTblAutomaticMachineLogPK(TblAutomaticMachineLogPK tblAutomaticMachineLogPK) {
        this.tblAutomaticMachineLogPK = tblAutomaticMachineLogPK;
    }

    /**
     * @return the machineType
     */
    public String getMachineType() {
        return machineType;
    }

    /**
     * @param machineType the machineType to set
     */
    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    /**
     * @return the lineNumber
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @param lineNumber the lineNumber to set
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @return the circuitBoardCode
     */
    public String getCircuitBoardCode() {
        return circuitBoardCode;
    }

    /**
     * @param circuitBoardCode the circuitBoardCode to set
     */
    public void setCircuitBoardCode(String circuitBoardCode) {
        this.circuitBoardCode = circuitBoardCode;
    }

    /**
     * @return the serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return the logType
     */
    public String getLogType() {
        return logType;
    }

    /**
     * @param logType the logType to set
     */
    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getTxtcol1() {
        return txtcol1;
    }

    public void setTxtcol1(String txtcol1) {
        this.txtcol1 = txtcol1;
    }

    public String getTxtcol2() {
        return txtcol2;
    }

    public void setTxtcol2(String txtcol2) {
        this.txtcol2 = txtcol2;
    }

    public String getTxtcol3() {
        return txtcol3;
    }

    public void setTxtcol3(String txtcol3) {
        this.txtcol3 = txtcol3;
    }

    public String getTxtcol4() {
        return txtcol4;
    }

    public void setTxtcol4(String txtcol4) {
        this.txtcol4 = txtcol4;
    }

    public String getTxtcol5() {
        return txtcol5;
    }

    public void setTxtcol5(String txtcol5) {
        this.txtcol5 = txtcol5;
    }

    public String getTxtcol6() {
        return txtcol6;
    }

    public void setTxtcol6(String txtcol6) {
        this.txtcol6 = txtcol6;
    }

    public String getTxtcol7() {
        return txtcol7;
    }

    public void setTxtcol7(String txtcol7) {
        this.txtcol7 = txtcol7;
    }

    public String getTxtcol8() {
        return txtcol8;
    }

    public void setTxtcol8(String txtcol8) {
        this.txtcol8 = txtcol8;
    }

    public String getTxtcol9() {
        return txtcol9;
    }

    public void setTxtcol9(String txtcol9) {
        this.txtcol9 = txtcol9;
    }
    
    public BigDecimal getNumcol1() {
        return numcol1;
    }

    public void setNumcol1(BigDecimal numcol1) {
        this.numcol1 = numcol1;
    }

    public BigDecimal getNumcol2() {
        return numcol2;
    }

    public void setNumcol2(BigDecimal numcol2) {
        this.numcol2 = numcol2;
    }

    public BigDecimal getNumcol3() {
        return numcol3;
    }

    public void setNumcol3(BigDecimal numcol3) {
        this.numcol3 = numcol3;
    }

    public BigDecimal getNumcol4() {
        return numcol4;
    }

    public void setNumcol4(BigDecimal numcol4) {
        this.numcol4 = numcol4;
    }

    public BigDecimal getNumcol5() {
        return numcol5;
    }

    public void setNumcol5(BigDecimal numcol5) {
        this.numcol5 = numcol5;
    }

    public BigDecimal getNumcol6() {
        return numcol6;
    }

    public void setNumcol6(BigDecimal numcol6) {
        this.numcol6 = numcol6;
    }

    public BigDecimal getNumcol7() {
        return numcol7;
    }

    public void setNumcol7(BigDecimal numcol7) {
        this.numcol7 = numcol7;
    }

    public BigDecimal getNumcol8() {
        return numcol8;
    }

    public void setNumcol8(BigDecimal numcol8) {
        this.numcol8 = numcol8;
    }

    public BigDecimal getNumcol9() {
        return numcol9;
    }

    public void setNumcol9(BigDecimal numcol9) {
        this.numcol9 = numcol9;
    }

    public BigDecimal getNumcol10() {
        return numcol10;
    }

    public void setNumcol10(BigDecimal numcol10) {
        this.numcol10 = numcol10;
    }

    public BigDecimal getNumcol11() {
        return numcol11;
    }

    public void setNumcol11(BigDecimal numcol11) {
        this.numcol11 = numcol11;
    }

    public BigDecimal getNumcol12() {
        return numcol12;
    }

    public void setNumcol12(BigDecimal numcol12) {
        this.numcol12 = numcol12;
    }

    public BigDecimal getNumcol13() {
        return numcol13;
    }

    public void setNumcol13(BigDecimal numcol13) {
        this.numcol13 = numcol13;
    }

    public BigDecimal getNumcol14() {
        return numcol14;
    }

    public void setNumcol14(BigDecimal numcol14) {
        this.numcol14 = numcol14;
    }

    public BigDecimal getNumcol15() {
        return numcol15;
    }

    public void setNumcol15(BigDecimal numcol15) {
        this.numcol15 = numcol15;
    }

    public Date getDtcol1() {
        return dtcol1;
    }

    public void setDtcol1(Date dtcol1) {
        this.dtcol1 = dtcol1;
    }

    public Date getDtcol2() {
        return dtcol2;
    }

    public void setDtcol2(Date dtcol2) {
        this.dtcol2 = dtcol2;
    }

    public Date getDtcol3() {
        return dtcol3;
    }

    public void setDtcol3(Date dtcol3) {
        this.dtcol3 = dtcol3;
    }

    public Date getDtcol4() {
        return dtcol4;
    }

    public void setDtcol4(Date dtcol4) {
        this.dtcol4 = dtcol4;
    }

    public Date getDtcol5() {
        return dtcol5;
    }

    public void setDtcol5(Date dtcol5) {
        this.dtcol5 = dtcol5;
    }

    public Date getDtcol6() {
        return dtcol6;
    }

    public void setDtcol6(Date dtcol6) {
        this.dtcol6 = dtcol6;
    }

    public Date getDtcol7() {
        return dtcol7;
    }

    public void setDtcol7(Date dtcol7) {
        this.dtcol7 = dtcol7;
    }

    public Date getDtcol8() {
        return dtcol8;
    }

    public void setDtcol8(Date dtcol8) {
        this.dtcol8 = dtcol8;
    }

    public Date getDtcol9() {
        return dtcol9;
    }

    public void setDtcol9(Date dtcol9) {
        this.dtcol9 = dtcol9;
    }

    public Date getDtcol10() {
        return dtcol10;
    }

    public void setDtcol10(Date dtcol10) {
        this.dtcol10 = dtcol10;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getTblAutomaticMachineLogPK() != null ? getTblAutomaticMachineLogPK().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblAutomaticMachineLog)) {
            return false;
        }
        TblAutomaticMachineLog other = (TblAutomaticMachineLog) object;
        if ((this.getTblAutomaticMachineLogPK() == null && other.getTblAutomaticMachineLogPK() != null) || (this.getTblAutomaticMachineLogPK() != null && !this.tblAutomaticMachineLogPK.equals(other.tblAutomaticMachineLogPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.log.TblAutomaticMachineLog[ tblAutomaticMachineLogPK=" + getTblAutomaticMachineLogPK() + " ]";
    }

    /**
     * @return the txtcol10
     */
    public String getTxtcol10() {
        return txtcol10;
    }

    /**
     * @param txtcol10 the txtcol10 to set
     */
    public void setTxtcol10(String txtcol10) {
        this.txtcol10 = txtcol10;
    }

    /**
     * @return the txtcol11
     */
    public String getTxtcol11() {
        return txtcol11;
    }

    /**
     * @param txtcol11 the txtcol11 to set
     */
    public void setTxtcol11(String txtcol11) {
        this.txtcol11 = txtcol11;
    }

    /**
     * @return the txtcol12
     */
    public String getTxtcol12() {
        return txtcol12;
    }

    /**
     * @param txtcol12 the txtcol12 to set
     */
    public void setTxtcol12(String txtcol12) {
        this.txtcol12 = txtcol12;
    }

    /**
     * @return the txtcol13
     */
    public String getTxtcol13() {
        return txtcol13;
    }

    /**
     * @param txtcol13 the txtcol13 to set
     */
    public void setTxtcol13(String txtcol13) {
        this.txtcol13 = txtcol13;
    }

    /**
     * @return the txtcol14
     */
    public String getTxtcol14() {
        return txtcol14;
    }

    /**
     * @param txtcol14 the txtcol14 to set
     */
    public void setTxtcol14(String txtcol14) {
        this.txtcol14 = txtcol14;
    }

    /**
     * @return the txtcol15
     */
    public String getTxtcol15() {
        return txtcol15;
    }

    /**
     * @param txtcol15 the txtcol15 to set
     */
    public void setTxtcol15(String txtcol15) {
        this.txtcol15 = txtcol15;
    }
    
}
