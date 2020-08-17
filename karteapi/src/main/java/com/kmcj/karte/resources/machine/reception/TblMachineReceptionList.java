package com.kmcj.karte.resources.machine.reception;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author Apeng
 * @date 20170621
 */
public class TblMachineReceptionList extends BasicResponse {

    private List<TblMachineReceptionVo> tblMachineReceptionVos;//設備受信テーブル

    public TblMachineReceptionList() {
    }

    /**
     * @return the tblMachineReceptionVos
     */
    public List<TblMachineReceptionVo> getTblMachineReceptionVos() {
        return tblMachineReceptionVos;
    }

    /**
     * @param tblMachineReceptionVos the tblMachineReceptionVos to set
     */
    public void setTblMachineReceptionVos(List<TblMachineReceptionVo> tblMachineReceptionVos) {
        this.tblMachineReceptionVos = tblMachineReceptionVos;
    }

}
