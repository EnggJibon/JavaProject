/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock.excel;

/**
 *
 * @author f.kitaoji
 */
public class MoldPartStockExcelRecord {
    private String moldId;
    private String moldPartCode;
    private String storageCode;
    private int stock;
    private int usedStock;
    private int orderPoint;
    private int orderUnit;
    private String strStock;
    private String strUsedStock;
    private String strOrderPoint;
    private String strOrderUnit;

    /**
     * @return the moldId
     */
    public String getMoldId() {
        return moldId;
    }

    /**
     * @param moldId the moldId to set
     */
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    /**
     * @return the moldPartCode
     */
    public String getMoldPartCode() {
        return moldPartCode;
    }

    /**
     * @param moldPartCode the moldPartCode to set
     */
    public void setMoldPartCode(String moldPartCode) {
        this.moldPartCode = moldPartCode;
    }

    /**
     * @return the storageCode
     */
    public String getStorageCode() {
        return storageCode;
    }

    /**
     * @param storageCode the storageCode to set
     */
    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    /**
     * @return the strStock
     */
    public String getStrStock() {
        return strStock;
    }

    /**
     * @param strStock the strStock to set
     */
    public void setStrStock(String strStock) {
        this.strStock = strStock;
    }

    /**
     * @return the strUsedStock
     */
    public String getStrUsedStock() {
        return strUsedStock;
    }

    /**
     * @param strUsedStock the strUsedStock to set
     */
    public void setStrUsedStock(String strUsedStock) {
        this.strUsedStock = strUsedStock;
    }

    /**
     * @return the strOrderPoint
     */
    public String getStrOrderPoint() {
        return strOrderPoint;
    }

    /**
     * @param strOrderPoint the strOrderPoint to set
     */
    public void setStrOrderPoint(String strOrderPoint) {
        this.strOrderPoint = strOrderPoint;
    }

    /**
     * @return the strOrderUnit
     */
    public String getStrOrderUnit() {
        return strOrderUnit;
    }

    /**
     * @param strOrderUnit the strOrderUnit to set
     */
    public void setStrOrderUnit(String strOrderUnit) {
        this.strOrderUnit = strOrderUnit;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the usedStock
     */
    public int getUsedStock() {
        return usedStock;
    }

    /**
     * @param usedStock the usedStock to set
     */
    public void setUsedStock(int usedStock) {
        this.usedStock = usedStock;
    }

    /**
     * @return the orderPoint
     */
    public int getOrderPoint() {
        return orderPoint;
    }

    /**
     * @param orderPoint the orderPoint to set
     */
    public void setOrderPoint(int orderPoint) {
        this.orderPoint = orderPoint;
    }

    /**
     * @return the orderUnit
     */
    public int getOrderUnit() {
        return orderUnit;
    }

    /**
     * @param orderUnit the orderUnit to set
     */
    public void setOrderUnit(int orderUnit) {
        this.orderUnit = orderUnit;
    }
    
}
