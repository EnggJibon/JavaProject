/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.plan;
import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 生産計画テーブル Response
 * @author t.ariki
 */
public class TblProductionPlanList extends BasicResponse {    
    private List<TblProductionPlan> mblProductionPlan;
    
    private List<TblProductionPlanVo> TblProductionPlanVo;
    
    private long count;
    private int pageNumber;
    private int pageTotal;
    
    public TblProductionPlanList() {
        mblProductionPlan = new ArrayList<>();
    }

    /**
     * @return the mblProductionPlan
     */
    public List<TblProductionPlan> getMblProductionPlan() {
        return mblProductionPlan;
    }

    /**
     * @param mblProductionPlan the mblProductionPlan to set
     */
    public void setMblProductionPlan(List<TblProductionPlan> mblProductionPlan) {
        this.mblProductionPlan = mblProductionPlan;
    }

    /**
     * @return the TblProductionPlanVo
     */
    public List<TblProductionPlanVo> getTblProductionPlanVo() {
        return TblProductionPlanVo;
    }

    /**
     * @param TblProductionPlanVo the TblProductionPlanVo to set
     */
    public void setTblProductionPlanVo(List<TblProductionPlanVo> TblProductionPlanVo) {
        this.TblProductionPlanVo = TblProductionPlanVo;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }   
    
}
