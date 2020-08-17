package com.kmcj.karte.resources.machine.filedef;

import com.kmcj.karte.resources.machine.MstMachine;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_machine_file_def")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineFileDef.findAll", query = "SELECT m FROM MstMachineFileDef m"),
    @NamedQuery(name = "MstMachineFileDef.findById", query = "SELECT m FROM MstMachineFileDef m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineFileDef.findByMachineUuid", query = "SELECT m FROM MstMachineFileDef m WHERE m.machineUuid = :machineUuid"),
    @NamedQuery(name = "MstMachineFileDef.findByMachineUuidAndHeaderLabel", query = "SELECT m FROM MstMachineFileDef m WHERE m.machineUuid = :machineUuid AND m.headerLabel = :headerLabel AND m.dispGraphFlg = 1"),
    @NamedQuery(name = "MstMachineFileDef.findByColumnName", query = "SELECT m FROM MstMachineFileDef m WHERE m.columnName = :columnName"),
    @NamedQuery(name = "MstMachineFileDef.findByUseFlg", query = "SELECT m FROM MstMachineFileDef m WHERE m.useFlg = :useFlg"),
    @NamedQuery(name = "MstMachineFileDef.findByHasThreshold", query = "SELECT m FROM MstMachineFileDef m WHERE m.hasThreshold = :hasThreshold"),
    @NamedQuery(name = "MstMachineFileDef.findByHeaderLabel", query = "SELECT m FROM MstMachineFileDef m WHERE m.headerLabel = :headerLabel"),
    @NamedQuery(name = "MstMachineFileDef.findByOnOffJudgeFlg", query = "SELECT m FROM MstMachineFileDef m WHERE m.onOffJudgeFlg = :onOffJudgeFlg"),
    @NamedQuery(name = "MstMachineFileDef.findByStopJudgeFlg", query = "SELECT m FROM MstMachineFileDef m WHERE m.stopJudgeFlg = :stopJudgeFlg"),
    @NamedQuery(name = "MstMachineFileDef.findByShotCountFlg", query = "SELECT m FROM MstMachineFileDef m WHERE m.shotCountFlg = :shotCountFlg"),
    @NamedQuery(name = "MstMachineFileDef.findByMaxVal", query = "SELECT m FROM MstMachineFileDef m WHERE m.maxVal = :maxVal"),
    @NamedQuery(name = "MstMachineFileDef.findByMinVal", query = "SELECT m FROM MstMachineFileDef m WHERE m.minVal = :minVal"),
    @NamedQuery(name = "MstMachineFileDef.findByCreateDate", query = "SELECT m FROM MstMachineFileDef m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineFileDef.findByUpdateDate", query = "SELECT m FROM MstMachineFileDef m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineFileDef.findByCreateUserUuid", query = "SELECT m FROM MstMachineFileDef m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachineFileDef.findByUpdateUserUuid", query = "SELECT m FROM MstMachineFileDef m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMachineFileDef.findByMacIdAndColNm", query = "SELECT m FROM MstMachineFileDef m, MstMachine mac WHERE m.machineUuid = mac.uuid and mac.machineId = :machineId and m.columnName = :columnName")
})
@Cacheable(value = false)
public class MstMachineFileDef implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Size(max = 45)
    @Column(name = "COLUMN_NAME")
    private String columnName;
    @Column(name = "USE_FLG")
    private Integer useFlg;
    @Column(name = "HAS_THRESHOLD")
    private Integer hasThreshold;
    @Size(max = 100)
    @Column(name = "HEADER_LABEL")
    private String headerLabel;
    @Column(name = "ON_OFF_JUDGE_FLG")
    private Integer onOffJudgeFlg;
    @Column(name = "STOP_JUDGE_FLG")
    private Integer stopJudgeFlg;
    @Column(name = "SHOT_COUNT_FLG")
    private Integer shotCountFlg;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MAX_VAL")
    private BigDecimal maxVal;
    @Column(name = "MIN_VAL")
    private BigDecimal minVal;
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
    @Column(name = "DISP_GRAPH_FLG")
    private Integer dispGraphFlg;

    //Ver.2.4 Sigma Gunshi Edge
    @Column(name = "WARNING_MAX")
    private BigDecimal warningMax;
    @Column(name = "WARNING_MIN")
    private BigDecimal warningMin;
    @Column(name = "WARNING_MESR_TERM_MIN")
    private int warningMesrTermMin;
    @Column(name = "WARNING_REACH_COUNT")
    private int warningReachCount;
    @Column(name = "WARN_AVG_LINE_DT")
    private Integer warnAgvLineDt;
    @Column(name = "WARN_AVG_LINE_DY")
    private BigDecimal warnAgvLineDy;
    @Column(name = "AVG_DURATION")
    private Integer avgDuration;
    @Column(name = "DETECT_DURATION")
    private Integer detectDuration;
    @Column(name = "WARN_AVG_LINE_CNT")
    private Integer warnAvgLineCnt;
    @Column(name = "ERR_AVG_LINE_CNT")
    private Integer errAvgLineCnt;

    
    @Transient
    private String operationFlag;
      
    @Transient
    private boolean isError;
    
    // 設備マスタ
    @PrimaryKeyJoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;
    public MstMachine getMstMachine() {
        return this.mstMachine;
    }
    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public MstMachineFileDef() {
    }

    public MstMachineFileDef(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getUseFlg() {
        return useFlg;
    }

    public void setUseFlg(Integer useFlg) {
        this.useFlg = useFlg;
    }

    public Integer getHasThreshold() {
        return hasThreshold;
    }

    public void setHasThreshold(Integer hasThreshold) {
        this.hasThreshold = hasThreshold;
    }

    public String getHeaderLabel() {
        return headerLabel;
    }

    public void setHeaderLabel(String headerLabel) {
        this.headerLabel = headerLabel;
    }

    public Integer getOnOffJudgeFlg() {
        return onOffJudgeFlg;
    }

    public void setOnOffJudgeFlg(Integer onOffJudgeFlg) {
        this.onOffJudgeFlg = onOffJudgeFlg;
    }

    public Integer getStopJudgeFlg() {
        return stopJudgeFlg;
    }

    public void setStopJudgeFlg(Integer stopJudgeFlg) {
        this.stopJudgeFlg = stopJudgeFlg;
    }

    public Integer getShotCountFlg() {
        return shotCountFlg;
    }

    public void setShotCountFlg(Integer shotCountFlg) {
        this.shotCountFlg = shotCountFlg;
    }

    public BigDecimal getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(BigDecimal maxVal) {
        this.maxVal = maxVal;
    }

    public BigDecimal getMinVal() {
        return minVal;
    }

    public void setMinVal(BigDecimal minVal) {
        this.minVal = minVal;
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

    public boolean isIsError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
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
        if (!(object instanceof MstMachineFileDef)) {
            return false;
        }
        MstMachineFileDef other = (MstMachineFileDef) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachineFileDef[ id=" + id + " ]";
    }

    public Integer getDispGraphFlg() {
        return dispGraphFlg;
    }

    public void setDispGraphFlg(Integer dispGraphFlg) {
        this.dispGraphFlg = dispGraphFlg;
    }

    /**
     * @return the operationFlag
     */
    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @param operationFlag the operationFlag to set
     */
    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    /**
     * @return the warningMax
     */
    public BigDecimal getWarningMax() {
        return warningMax;
    }

    /**
     * @param warningMax the warningMax to set
     */
    public void setWarningMax(BigDecimal warningMax) {
        this.warningMax = warningMax;
    }

    /**
     * @return the warningMin
     */
    public BigDecimal getWarningMin() {
        return warningMin;
    }

    /**
     * @param warningMin the warningMin to set
     */
    public void setWarningMin(BigDecimal warningMin) {
        this.warningMin = warningMin;
    }

    /**
     * @return the warningMesrTermMin
     */
    public int getWarningMesrTermMin() {
        return warningMesrTermMin;
    }

    /**
     * @param warningMesrTermMin the warningMesrTermMin to set
     */
    public void setWarningMesrTermMin(int warningMesrTermMin) {
        this.warningMesrTermMin = warningMesrTermMin;
    }

    /**
     * @return the warningReachCount
     */
    public int getWarningReachCount() {
        return warningReachCount;
    }

    /**
     * @param warningReachCount the warningReachCount to set
     */
    public void setWarningReachCount(int warningReachCount) {
        this.warningReachCount = warningReachCount;
    }

    public Integer getWarnAgvLineDt() {
        return warnAgvLineDt;
    }

    public void setWarnAgvLineDt(Integer warnAgvLineDt) {
        this.warnAgvLineDt = warnAgvLineDt;
    }

    public BigDecimal getWarnAgvLineDy() {
        return warnAgvLineDy;
    }

    public void setWarnAgvLineDy(BigDecimal warnAgvLineDy) {
        this.warnAgvLineDy = warnAgvLineDy;
    }

    public Integer getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Integer avgDuration) {
        this.avgDuration = avgDuration;
    }

    public Integer getDetectDuration() {
        return detectDuration;
    }

    public void setDetectDuration(Integer detectDuration) {
        this.detectDuration = detectDuration;
    }

    public Integer getWarnAvgLineCnt() {
        return warnAvgLineCnt;
    }

    public void setWarnAvgLineCnt(Integer warnAvgLineCnt) {
        this.warnAvgLineCnt = warnAvgLineCnt;
    }

    public Integer getErrAvgLineCnt() {
        return errAvgLineCnt;
    }

    public void setErrAvgLineCnt(Integer errAvgLineCnt) {
        this.errAvgLineCnt = errAvgLineCnt;
    }

}
