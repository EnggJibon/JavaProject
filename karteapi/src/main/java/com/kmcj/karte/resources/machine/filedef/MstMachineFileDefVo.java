package com.kmcj.karte.resources.machine.filedef;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public class MstMachineFileDefVo extends BasicResponse {

    private String id;

    private String machineUuid;

    private String columnName;

    private String useFlg;

    private String hasThreshold;

    private String headerLabel;

    private String onOffJudgeFlg;

    private String stopJudgeFlg;

    private String shotCountFlg;

    private String maxVal;

    private String minVal;

    private String createDate;

    private String updateDate;

    private String createUserUuid;

    private String updateUserUuid;

    private String dispGraphFlg;
    
    private String keythreshold;
    
    private String listNum;

    private String warningMax;

    private String warningMin;
    
    private String warningMesrTermMin;

    private String warningReachCount;
    
    private Integer warnAgvLineDt;
    private BigDecimal warnAgvLineDy;
    private Integer avgDuration;
    private Integer detectDuration;
    private Integer warnAvgLineCnt;
    private Integer errAvgLineCnt;
    
    public MstMachineFileDefVo() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setUseFlg(String useFlg) {
        this.useFlg = useFlg;
    }

    public void setHasThreshold(String hasThreshold) {
        this.hasThreshold = hasThreshold;
    }

    public void setHeaderLabel(String headerLabel) {
        this.headerLabel = headerLabel;
    }

    public void setOnOffJudgeFlg(String onOffJudgeFlg) {
        this.onOffJudgeFlg = onOffJudgeFlg;
    }

    public void setStopJudgeFlg(String stopJudgeFlg) {
        this.stopJudgeFlg = stopJudgeFlg;
    }

    public void setShotCountFlg(String shotCountFlg) {
        this.shotCountFlg = shotCountFlg;
    }

    public void setMaxVal(String maxVal) {
        this.maxVal = maxVal;
    }

    public void setMinVal(String minVal) {
        this.minVal = minVal;
    }
    
    public void setWarningMax(String warningMax) {
        this.warningMax = warningMax;
    }

    public void setWarningMin(String warningMin) {
        this.warningMin = warningMin;
    }

    public void setWarningMesrTermMin(String warningMesrTermMin) {
        this.warningMesrTermMin = warningMesrTermMin;
    }

    public void setWarningReachCount(String warningReachCount) {
        this.warningReachCount = warningReachCount;
    }
    
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public String getId() {
        return id;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getUseFlg() {
        return useFlg;
    }

    public String getHasThreshold() {
        return hasThreshold;
    }

    public String getHeaderLabel() {
        return headerLabel;
    }

    public String getOnOffJudgeFlg() {
        return onOffJudgeFlg;
    }

    public String getStopJudgeFlg() {
        return stopJudgeFlg;
    }

    public String getShotCountFlg() {
        return shotCountFlg;
    }

    public String getMaxVal() {
        return maxVal;
    }

    public String getMinVal() {
        return minVal;
    }
    
    public String getWarningMax() {
        return warningMax;
    }

    public String getWarningMin() {
        return warningMin;
    }
    
    public String getWarningMesrTermMin() {
        return warningMesrTermMin;
    }

    public String getWarningReachCount() {
        return warningReachCount;
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

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public String getDispGraphFlg() {
        return dispGraphFlg;
    }

    public void setDispGraphFlg(String dispGraphFlg) {
        this.dispGraphFlg = dispGraphFlg;
    }

    /**
     * @return the keythreshold
     */
    public String getKeythreshold() {
        return keythreshold;
    }

    /**
     * @param keythreshold the keythreshold to set
     */
    public void setKeythreshold(String keythreshold) {
        this.keythreshold = keythreshold;
    }

    /**
     * @return the listNum
     */
    public String getListNum() {
        return listNum;
    }

    /**
     * @param listNum the listNum to set
     */
    public void setListNum(String listNum) {
        this.listNum = listNum;
    }

}
