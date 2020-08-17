package com.kmcj.karte.resources.sigma.log;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDef;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblSigmaLogList extends BasicResponse {

    private List<TblSigmaLog> tblSigmaLogs;

    private List<TblSigmaLogVo> tblSigmaLogVos;
    
    private List<MachineGraphLogVo> machineGraphLogVos;
    
    private List<MstMachineFileDef> machineGraphThresholdVos;
    
    private boolean reachThresholds1;
    private boolean reachWarning1;
    private boolean reachThresholds2;
    private boolean reachWarning2;

    public TblSigmaLogList() {
    }

    public void setTblSigmaLogs(List<TblSigmaLog> tblSigmaLogs) {
        this.tblSigmaLogs = tblSigmaLogs;
    }

    public void setTblSigmaLogVos(List<TblSigmaLogVo> tblSigmaLogVos) {
        this.tblSigmaLogVos = tblSigmaLogVos;
    }

    public List<TblSigmaLog> getTblSigmaLogs() {
        return tblSigmaLogs;
    }

    public List<TblSigmaLogVo> getTblSigmaLogVos() {
        return tblSigmaLogVos;
    }

    public void setMachineGraphLogVos(List<MachineGraphLogVo> machineGraphLogVos) {
        this.machineGraphLogVos = machineGraphLogVos;
    }

    public List<MachineGraphLogVo> getMachineGraphLogVos() {
        return machineGraphLogVos;
    }
    
    public void setMachineGraphThresholdVos(List<MstMachineFileDef> machineGraphThresholdVos) {
        this.machineGraphThresholdVos = machineGraphThresholdVos;
    }

    public List<MstMachineFileDef> getMachineGraphThresholdVos() {
        return machineGraphThresholdVos;
    }

    /**
     * @return the reachThresholds1
     */
    public boolean isReachThresholds1() {
        return reachThresholds1;
    }

    /**
     * @param reachThresholds1 the reachThresholds1 to set
     */
    public void setReachThresholds1(boolean reachThresholds1) {
        this.reachThresholds1 = reachThresholds1;
    }

    /**
     * @return the reachWarning1
     */
    public boolean isReachWarning1() {
        return reachWarning1;
    }

    /**
     * @param reachWarning1 the reachWarning1 to set
     */
    public void setReachWarning1(boolean reachWarning1) {
        this.reachWarning1 = reachWarning1;
    }

    /**
     * @return the reachThresholds2
     */
    public boolean isReachThresholds2() {
        return reachThresholds2;
    }

    /**
     * @param reachThresholds2 the reachThresholds2 to set
     */
    public void setReachThresholds2(boolean reachThresholds2) {
        this.reachThresholds2 = reachThresholds2;
    }

    /**
     * @return the reachWarning2
     */
    public boolean isReachWarning2() {
        return reachWarning2;
    }

    /**
     * @param reachWarning2 the reachWarning2 to set
     */
    public void setReachWarning2(boolean reachWarning2) {
        this.reachWarning2 = reachWarning2;
    }
}
