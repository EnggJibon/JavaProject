package com.kmcj.karte.resources.machine.inventory;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineInventoryList extends BasicResponse {
    private String inventoryStandardDate;
    private List<TblMachineInventory> tblMachineInventorys;
    
    private List<TblMachineInventoryVo> tblMachineInventoryVos;
    private long count;
    private int pageNumber;
    private int pageTotal;

    public TblMachineInventoryList() {
    }

    public void setTblMachineInventorys(List<TblMachineInventory> tblMachineInventorys) {
        this.tblMachineInventorys = tblMachineInventorys;
    }

    public void setTblMachineInventoryVos(List<TblMachineInventoryVo> tblMachineInventoryVos) {
        this.tblMachineInventoryVos = tblMachineInventoryVos;
    }

    public List<TblMachineInventory> getTblMachineInventorys() {
        return tblMachineInventorys;
    }

    public List<TblMachineInventoryVo> getTblMachineInventoryVos() {
        return tblMachineInventoryVos;
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
