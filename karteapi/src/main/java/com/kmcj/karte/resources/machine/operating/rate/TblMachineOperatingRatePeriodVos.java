package com.kmcj.karte.resources.machine.operating.rate;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author apeng
 */
public class TblMachineOperatingRatePeriodVos extends BasicResponse {

    private String machineUuid;
    private String machineId;
    private String machineName;
    private Integer machineType;

    private List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRateProducintTimeVos;

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setMachineType(Integer machineType) {
        this.machineType = machineType;
    }

    public void setTblMachineOperatingRateProducintTimeVos(List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRateProducintTimeVos) {
        this.tblMachineOperatingRateProducintTimeVos = tblMachineOperatingRateProducintTimeVos;
    }
    
    public String getMachineUuid() {
        return machineUuid;
    }

    public String getMachineId() {
        return machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public Integer getMachineType() {
        return machineType;
    }

    public List<TblMachineOperatingRatePeriodVo> getTblMachineOperatingRateProducintTimeVos() {
        return tblMachineOperatingRateProducintTimeVos;
    }
    
}
