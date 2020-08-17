/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inventory;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.MstMold;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class TblMoldInventoryList extends BasicResponse{
    private String inventoryStandardDate;
    private List<MstMold> tblMoldInventoryList;

    private List<TblMoldInventorys> tblMoldInventorys;
    private long count;
    private int pageNumber;
    private int pageTotal;

    /**
     * @return the tblMoldInventoryList
     */
    public List<MstMold> getTblMoldInventoryList() {
        return tblMoldInventoryList;
    }

    /**
     * @param tblMoldInventoryList the tblMoldInventoryList to set
     */
    public void setTblMoldInventoryList(List<MstMold> tblMoldInventoryList) {
        this.tblMoldInventoryList = tblMoldInventoryList;
    }

    /**
     * @return the tblMoldInventorys
     */
    public List<TblMoldInventorys> getTblMoldInventorys() {
        return tblMoldInventorys;
    }

    /**
     * @param tblMoldInventorys the tblMoldInventorys to set
     */
    public void setTblMoldInventorys(List<TblMoldInventorys> tblMoldInventorys) {
        this.tblMoldInventorys = tblMoldInventorys;
    }

    /**
     * @return the inventoryStandardDate
     */
    public String getInventoryStandardDate() {
        return inventoryStandardDate;
    }

    /**
     * @param inventoryStandardDate the inventoryStandardDate to set
     */
    public void setInventoryStandardDate(String inventoryStandardDate) {
        this.inventoryStandardDate = inventoryStandardDate;
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
