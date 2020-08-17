package com.kmcj.karte.resources.sigma.threshold;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.MstComponents;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDefVo;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstSigmaThresholdList extends BasicResponse {

    private List<MstSigmaThreshold> mstSigmaThresholds;

    private List<MstSigmaThresholdVo> mstSigmaThresholdVos;
    
    private List<MstMachineFileDefVo> mstMachineFileDefVo;
    
    private List<MstComponents> mstComponent;

    public MstSigmaThresholdList() {
    }

    public void setMstSigmaThresholds(List<MstSigmaThreshold> mstSigmaThresholds) {
        this.mstSigmaThresholds = mstSigmaThresholds;
    }

    public void setMstSigmaThresholdVos(List<MstSigmaThresholdVo> mstSigmaThresholdVos) {
        this.mstSigmaThresholdVos = mstSigmaThresholdVos;
    }

    public List<MstSigmaThreshold> getMstSigmaThresholds() {
        return mstSigmaThresholds;
    }

    public List<MstSigmaThresholdVo> getMstSigmaThresholdVos() {
        return mstSigmaThresholdVos;
    }

    /**
     * @return the mstMachineFileDefVo
     */
    public List<MstMachineFileDefVo> getMstMachineFileDefVo() {
        return mstMachineFileDefVo;
    }

    /**
     * @param mstMachineFileDefVo the mstMachineFileDefVo to set
     */
    public void setMstMachineFileDefVo(List<MstMachineFileDefVo> mstMachineFileDefVo) {
        this.mstMachineFileDefVo = mstMachineFileDefVo;
    }

    /**
     * @return the mstComponent
     */
    public List<MstComponents> getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(List<MstComponents> mstComponent) {
        this.mstComponent = mstComponent;
    }
}
