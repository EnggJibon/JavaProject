/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect.inspection.detail;

/**
 *
 * @author liujiyong
 */
public class DefectInspectionDetailVo {
    
    // 面
    private String side;
    
    // 不良箇所
    private String defectPlace;
    
    // 不良内容
    private String defectContent;
    
    // 不良件数
    private int defectCount;
    
    // 対応箇所
    private String mendPlace;
    
    // 対応内容
    private String mendContent;
    
    // 対応件数
    private int mendCount;

    /**
     * @return the side
     */
    public String getSide() {
        return side;
    }

    /**
     * @param side the side to set
     */
    public void setSide(String side) {
        this.side = side;
    }

    /**
     * @return the defectPlace
     */
    public String getDefectPlace() {
        return defectPlace;
    }

    /**
     * @param defectPlace the defectPlace to set
     */
    public void setDefectPlace(String defectPlace) {
        this.defectPlace = defectPlace;
    }

    /**
     * @return the defectContent
     */
    public String getDefectContent() {
        return defectContent;
    }

    /**
     * @param defectContent the defectContent to set
     */
    public void setDefectContent(String defectContent) {
        this.defectContent = defectContent;
    }

    /**
     * @return the defectCount
     */
    public int getDefectCount() {
        return defectCount;
    }

    /**
     * @param defectCount the defectCount to set
     */
    public void setDefectCount(int defectCount) {
        this.defectCount = defectCount;
    }

    /**
     * @return the mendPlace
     */
    public String getMendPlace() {
        return mendPlace;
    }

    /**
     * @param mendPlace the mendPlace to set
     */
    public void setMendPlace(String mendPlace) {
        this.mendPlace = mendPlace;
    }

    /**
     * @return the mendContent
     */
    public String getMendContent() {
        return mendContent;
    }

    /**
     * @param mendContent the mendContent to set
     */
    public void setMendContent(String mendContent) {
        this.mendContent = mendContent;
    }

    /**
     * @return the mendCount
     */
    public int getMendCount() {
        return mendCount;
    }

    /**
     * @param mendCount the mendCount to set
     */
    public void setMendCount(int mendCount) {
        this.mendCount = mendCount;
    }
}
