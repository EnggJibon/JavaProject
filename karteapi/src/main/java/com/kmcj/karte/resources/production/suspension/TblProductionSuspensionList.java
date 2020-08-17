/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.suspension;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * 生産中断履歴テーブル
 *
 * @author zds
 */
public class TblProductionSuspensionList extends BasicResponse {

    private List<TblProductionSuspension> tblProductionSuspensionList;
    
    private int sumSuspendedTimeMinute; //中断時間合計
    private List<TblProductionSuspensionVo> tblProductionSuspensionVo;

    /**
     * @return the tblProductionSuspensionList
     */
    public List<TblProductionSuspension> getTblProductionSuspensionList() {
        return tblProductionSuspensionList;
    }

    /**
     * @param tblProductionSuspensionList the tblProductionSuspensionList to set
     */
    public void setTblProductionSuspensionList(List<TblProductionSuspension> tblProductionSuspensionList) {
        this.tblProductionSuspensionList = tblProductionSuspensionList;
    }

    /**
     * @return the tblProductionSuspensionVo
     */
    public List<TblProductionSuspensionVo> getTblProductionSuspensionVo() {
        return tblProductionSuspensionVo;
    }

    /**
     * @param tblProductionSuspensionVo the tblProductionSuspensionVo to set
     */
    public void setTblProductionSuspensionVo(List<TblProductionSuspensionVo> tblProductionSuspensionVo) {
        this.tblProductionSuspensionVo = tblProductionSuspensionVo;
    }

    /**
     * @return the sumSuspendedTimeMinute
     */
    public int getSumSuspendedTimeMinute() {
        return sumSuspendedTimeMinute;
    }

    /**
     * @param sumSuspendedTimeMinute the sumSuspendedTimeMinute to set
     */
    public void setSumSuspendedTimeMinute(int sumSuspendedTimeMinute) {
        this.sumSuspendedTimeMinute = sumSuspendedTimeMinute;
    }
}
