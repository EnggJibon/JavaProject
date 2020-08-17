package com.kmcj.karte.resources.machine.operating.rate;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author apeng
 */
public class TblMachineOperatingRatePeriodVo extends BasicResponse {

    private Date productionDate;//生産日
    private String productionDateStr;

    private long operatingTime;//稼動時間
    private long deductionTime;//控除時間
    private boolean deductionFlg;//控除フラグ
    private String operatingTimeStr;
    private String operatingRate;//稼動率

    private String operatingTimeHeder;//稼動時間を文言から取得
    private String operatingRateHeder;//稼動率を文言から取得
    
    private String machineUuid;

    public Date getProductionDate() {
        return productionDate;
    }

    public String getProductionDateStr() {
        return productionDateStr;
    }

    public long getOperatingTime() {
        return operatingTime;
    }

    public String getOperatingRate() {
        return operatingRate;
    }

    public String getOperatingTimeHeder() {
        return operatingTimeHeder;
    }

    public String getOperatingRateHeder() {
        return operatingRateHeder;
    }

    public long getDeductionTime() {
        return deductionTime;
    }

    public void setDeductionTime(long deductionTime) {
        this.deductionTime = deductionTime;
    }

    public boolean getDeductionFlg() {
        return deductionFlg;
    }

    public void setDeductionFlg(boolean deductionFlg) {
        this.deductionFlg = deductionFlg;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public void setProductionDateStr(String productionDateStr) {
        this.productionDateStr = productionDateStr;
    }

    public void setOperatingTime(long operatingTime) {
        this.operatingTime = operatingTime;
    }

    public void setOperatingRate(String operatingRate) {
        this.operatingRate = operatingRate;
    }

    public void setOperatingTimeHeder(String operatingTimeHeder) {
        this.operatingTimeHeder = operatingTimeHeder;
    }

    public void setOperatingRateHeder(String operatingRateHeder) {
        this.operatingRateHeder = operatingRateHeder;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    /**
     * @return the operatingTimeStr
     */
    public String getOperatingTimeStr() {
        return operatingTimeStr;
    }

    /**
     * @param operatingTimeStr the operatingTimeStr to set
     */
    public void setOperatingTimeStr(String operatingTimeStr) {
        this.operatingTimeStr = operatingTimeStr;
    }
    
        
}
