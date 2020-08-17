/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice.category;

import com.kmcj.karte.excelhandle.annotation.Cell;
import com.kmcj.karte.excelhandle.annotation.Excel;

/**
 * 分類項目エクセルデータ出力用
 *
 * @author admin
 */
@Excel(isNeedSequence = false)
public class CategoryExcelOutPut {

    @Cell(columnNum = "1")
    private String category; //CATEGORY

    @Cell(columnNum = "2")
    private String categoryType;//種別

    @Cell(columnNum = "3")
    private String categoryItem;//分類項目

    @Cell(columnNum = "4")
    private String parentCategoryItem;//上位分類項目

    @Cell(columnNum = "5")
    private String categorySeq;//連番

    @Cell(columnNum = "6")
    private String displaySeq;
    
    @Cell(columnNum = "7")
    private String deleteFlg;
    
    @Cell(columnNum = "8")
    private String categoryValueJa;//区分値_ja

    @Cell(columnNum = "9")
    private String categoryValueEn;//区分値_en

    @Cell(columnNum = "10")
    private String categoryValueZh;//区分値_zh

    @Cell(columnNum = "11")
    private String parentCategoryValue;//上位区分値

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the categoryType
     */
    public String getCategoryType() {
        return categoryType;
    }

    /**
     * @param categoryType the categoryType to set
     */
    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    /**
     * @return the categoryItem
     */
    public String getCategoryItem() {
        return categoryItem;
    }

    /**
     * @param categoryItem the categoryItem to set
     */
    public void setCategoryItem(String categoryItem) {
        this.categoryItem = categoryItem;
    }

    /**
     * @return the parentCategoryItem
     */
    public String getParentCategoryItem() {
        return parentCategoryItem;
    }

    /**
     * @param parentCategoryItem the parentCategoryItem to set
     */
    public void setParentCategoryItem(String parentCategoryItem) {
        this.parentCategoryItem = parentCategoryItem;
    }

    /**
     * @return the categorySeq
     */
    public String getCategorySeq() {
        return categorySeq;
    }

    /**
     * @param categorySeq the categorySeq to set
     */
    public void setCategorySeq(String categorySeq) {
        this.categorySeq = categorySeq;
    }
    
    /**
     * @return the displaySeq
     */
    public String getDisplaySeq() {
        return displaySeq;
    }

    /**
     * @param displaySeq the displaySeq to set
     */
    public void setDisplaySeq(String displaySeq) {
        this.displaySeq = displaySeq;
    }

    /**
     * @return the deleteFlg
     */
    public String getDeleteFlg() {
        return deleteFlg;
    }

    /**
     * @param deleteFlg the deleteFlg to set
     */
    public void setDeleteFlg(String deleteFlg) {
        this.deleteFlg = deleteFlg;
    }
    
    /**
     * @return the categoryValueJa
     */
    public String getCategoryValueJa() {
        return categoryValueJa;
    }

    /**
     * @param categoryValueJa the categoryValueJa to set
     */
    public void setCategoryValueJa(String categoryValueJa) {
        this.categoryValueJa = categoryValueJa;
    }

    /**
     * @return the categoryValueEn
     */
    public String getCategoryValueEn() {
        return categoryValueEn;
    }

    /**
     * @param categoryValueEn the categoryValueEn to set
     */
    public void setCategoryValueEn(String categoryValueEn) {
        this.categoryValueEn = categoryValueEn;
    }

    /**
     * @return the categoryValueZh
     */
    public String getCategoryValueZh() {
        return categoryValueZh;
    }

    /**
     * @param categoryValueZh the categoryValueZh to set
     */
    public void setCategoryValueZh(String categoryValueZh) {
        this.categoryValueZh = categoryValueZh;
    }

    /**
     * @return the parentCategoryValue
     */
    public String getParentCategoryValue() {
        return parentCategoryValue;
    }

    /**
     * @param parentCategoryValue the parentCategoryValue to set
     */
    public void setParentCategoryValue(String parentCategoryValue) {
        this.parentCategoryValue = parentCategoryValue;
    }

}
