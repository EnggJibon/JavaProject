/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstChoiceVo extends BasicResponse {

    private String id;
    private String parentCategory;
    private String category;
    private String categoriesId;
    private String choice;
    private String seq;
    private Integer displaySeq;
    private String oldSeq;
    private String parentSeq;
    private String langId;
    private String deleteFlag;
    private Integer deleteFlg;
    List<MstChoice> choices;
    List<MstChoiceVo> choiceVo;
    private MstChoice mstChoice; //need

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the parentCategory
     */
    public String getParentCategory() {
        return parentCategory;
    }

    /**
     * @param parentCategory the parentCategory to set
     */
    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

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

    public String getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(String categoriesId) {
        this.categoriesId = categoriesId;
    }

    /**
     * @return the choice
     */
    public String getChoice() {
        return choice;
    }

    /**
     * @param choice the choice to set
     */
    public void setChoice(String choice) {
        this.choice = choice;
    }
    
    /**
     * @return the displaySeq
     */
    public Integer getDisplaySeq() {
        return displaySeq;
    }

    /**
     * @param displaySeq the displaySeq to set
     */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }
    
    /**
     * @return the seq
     */
    public String getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(String seq) {
        this.seq = seq;
    }

    /**
     * @return the parentSeq
     */
    public String getParentSeq() {
        return parentSeq;
    }

    /**
     * @param parentSeq the parentSeq to set
     */
    public void setParentSeq(String parentSeq) {
        this.parentSeq = parentSeq;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Integer deleteFlg) {
        this.deleteFlg = deleteFlg;
    }
    
    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public List<MstChoiceVo> getChoiceVo() {
        return choiceVo;
    }

    public void setChoiceVo(List<MstChoiceVo> choiceVo) {
        this.choiceVo = choiceVo;
    }

    public String getOldSeq() {
        return oldSeq;
    }

    public void setOldSeq(String oldSeq) {
        this.oldSeq = oldSeq;
    }

    public MstChoice getMstChoice() {
        return mstChoice;
    }

    public void setMstChoice(MstChoice mstChoice) {
        this.mstChoice = mstChoice;
    }

    public List<MstChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<MstChoice> choices) {
        this.choices = choices;
    }
}
