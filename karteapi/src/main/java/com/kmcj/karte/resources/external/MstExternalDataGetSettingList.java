/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.external;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstExternalDataGetSettingList extends BasicResponse {
    
    private List<MstExternalDataGetSettingVo> mstExternalDataGetSettingVoList;

    /**
     * @return the mstExternalDataGetSettingVoList
     */
    public List<MstExternalDataGetSettingVo> getMstExternalDataGetSettingVoList() {
        return mstExternalDataGetSettingVoList;
    }

    /**
     * @param mstExternalDataGetSettingVoList the mstExternalDataGetSettingVoList to set
     */
    public void setMstExternalDataGetSettingVoList(List<MstExternalDataGetSettingVo> mstExternalDataGetSettingVoList) {
        this.mstExternalDataGetSettingVoList = mstExternalDataGetSettingVoList;
    }
    
    
}
