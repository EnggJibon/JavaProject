/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice.category;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceVo;
import java.util.Date;
import java.util.List;


public class MstChoiceCategoryVo extends BasicResponse {
    private String parentCategory;
    private String parentCategoryCode;
    private String id;
    private String category;
    private String categoryCode;
    private String canAddDelete;
    private String deleteFlag;
    private String categoryTypeDictValue;
    private String categoryNameDictKey;
    private String seq;
    private String tableId;
    private String fieldId;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private List<MstChoiceCategoryVo> choiceCategoryVos;
    private List<MstChoice> mstChoice;
    private List<MstChoiceVo> mstChoiceVo;

    public MstChoiceCategoryVo() {
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public String getParentCategoryCode() {
        return parentCategoryCode;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getCanAddDelete() {
        return canAddDelete;
    }

    public String getCategoryTypeDictValue() {
        return categoryTypeDictValue;
    }

    public String getCategoryNameDictKey() {
        return categoryNameDictKey;
    }

    public String getSeq() {
        return seq;
    }

    public String getTableId() {
        return tableId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public List<MstChoiceCategoryVo> getChoiceCategoryVos() {
        return choiceCategoryVos;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public void setParentCategoryCode(String parentCategoryCode) {
        this.parentCategoryCode = parentCategoryCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCanAddDelete(String canAddDelete) {
        this.canAddDelete = canAddDelete;
    }

    public void setCategoryTypeDictValue(String categoryTypeDictValue) {
        this.categoryTypeDictValue = categoryTypeDictValue;
    }

    public void setCategoryNameDictKey(String categoryNameDictKey) {
        this.categoryNameDictKey = categoryNameDictKey;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setChoiceCategoryVos(List<MstChoiceCategoryVo> choiceCategoryVos) {
        this.choiceCategoryVos = choiceCategoryVos;
    }

    public List<MstChoice> getMstChoice() {
        return mstChoice;
    }

    public void setMstChoice(List<MstChoice> mstChoice) {
        this.mstChoice = mstChoice;
    }

    public List<MstChoiceVo> getMstChoiceVo() {
        return mstChoiceVo;
    }

    public void setMstChoiceVo(List<MstChoiceVo> mstChoiceVo) {
        this.mstChoiceVo = mstChoiceVo;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
