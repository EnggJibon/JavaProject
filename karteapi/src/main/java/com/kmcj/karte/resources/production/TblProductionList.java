/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.structure.MstComponentStructureVoList;
import com.kmcj.karte.resources.production2.Production;

import java.util.ArrayList;
import java.util.List;

/**
 * 生産実績テーブルResponse
 * @author t.ariki
 */
public class TblProductionList extends BasicResponse {
    private List<TblProduction> tblProductions;
    private List<TblProductionVo> tblProductionVo;
    private List<MstComponentStructureVoList> mstComponentStructureVoList;
    private List<Production> productions;
    private long count;
    private int pageNumber;
    private int pageTotal;
    
    public TblProductionList() {
        tblProductions = new ArrayList<>();
    }
    public List<TblProduction> getTblProductions() {
        return tblProductions;
    }
    public void setTblProductions(List<TblProduction> tblProductions) {
        this.tblProductions = tblProductions;
    }

    /**
     * @return the tblProductionVo
     */
    public List<TblProductionVo> getTblProductionVo() {
        return tblProductionVo;
    }

    /**
     * @param tblProductionVo the tblProductionVo to set
     */
    public void setTblProductionVo(List<TblProductionVo> tblProductionVo) {
        this.tblProductionVo = tblProductionVo;
    }

    /**
     * @return the mstComponentStructureVoList
     */
    public List<MstComponentStructureVoList> getMstComponentStructureVoList() {
        return mstComponentStructureVoList;
    }

    /**
     * @param mstComponentStructureVoList the mstComponentStructureVoList to set
     */
    public void setMstComponentStructureVoList(List<MstComponentStructureVoList> mstComponentStructureVoList) {
        this.mstComponentStructureVoList = mstComponentStructureVoList;
    }
	public List<Production> getProductions() {
		return productions;
	}
	public void setProductions(List<Production> productions) {
		this.productions = productions;
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
