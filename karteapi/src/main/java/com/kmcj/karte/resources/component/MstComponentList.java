/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component;
import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstComponentList extends BasicResponse {

    private List<MstComponent> mstComponents;
    private List<MstComponentDetail> mstComponentDetails;
    private long count;
    private int pageNumber;
    private int pageTotal;

    /**
     * @return the mstComponens
     */
    public List< MstComponent> getMstComponents() {
        return mstComponents;
    }

    /**
     * @param mstComponents the mstComponents to set
     */
    public void setMstComponents(List< MstComponent> mstComponents) {
        this.mstComponents = mstComponents;
    }

    public List<MstComponentDetail> getMstComponentDetails() {
        return mstComponentDetails;
    }

    public void setMstComponentDetails(List<MstComponentDetail> mstComponentDetails) {
        this.mstComponentDetails = mstComponentDetails;
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
