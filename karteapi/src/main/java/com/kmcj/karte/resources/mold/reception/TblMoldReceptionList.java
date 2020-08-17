package com.kmcj.karte.resources.mold.reception;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * 
 * @author Apeng
 * @date 20170621
 */
public class TblMoldReceptionList extends BasicResponse {

    private List<TblMoldReceptionVo> tblMoldReceptionVos;//金型受信テーブル

    public TblMoldReceptionList() {
    }

    /**
     * @return the tblMoldReceptionVos
     */
    public List<TblMoldReceptionVo> getTblMoldReceptionVos() {
        return tblMoldReceptionVos;
    }

    /**
     * @param tblMoldReceptionVos the tblMoldReceptionVos to set
     */
    public void setTblMoldReceptionVos(List<TblMoldReceptionVo> tblMoldReceptionVos) {
        this.tblMoldReceptionVos = tblMoldReceptionVos;
    }
    
}
