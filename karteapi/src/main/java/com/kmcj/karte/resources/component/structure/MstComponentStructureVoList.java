/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.structure;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstComponentStructureVoList extends BasicResponse {

    private List<MstComponentStructureVo> mstComponentStructureVos;
    private List<MstComponentStructure> mstComponentStructures;
    private long count;
    private int pageNumber;
    private int pageTotal;

    /**
     * @return the mstComponentStructureVos
     */
    public List<MstComponentStructureVo> getMstComponentStructureVos() {
        return mstComponentStructureVos;
    }

    /**
     * @param mstComponentStructureVos the mstComponentStructureVos to set
     */
    public void setMstComponentStructureVos(List<MstComponentStructureVo> mstComponentStructureVos) {
        this.mstComponentStructureVos = mstComponentStructureVos;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @return the pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return the pageTotal
     */
    public int getPageTotal() {
        return pageTotal;
    }

    /**
     * @param pageTotal the pageTotal to set
     */
    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    /**
     * @return the mstComponentStructures
     */
    public List<MstComponentStructure> getMstComponentStructures() {
        return mstComponentStructures;
    }

    /**
     * @param mstComponentStructures the mstComponentStructures to set
     */
    public void setMstComponentStructures(List<MstComponentStructure> mstComponentStructures) {
        this.mstComponentStructures = mstComponentStructures;
    }
}
