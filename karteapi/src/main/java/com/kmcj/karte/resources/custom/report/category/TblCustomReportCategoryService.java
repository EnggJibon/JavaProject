/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.category;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;

import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * レポートクエリユーザーマスタ サービス
 *
 * @author admin
 */
@Dependent
public class TblCustomReportCategoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    //@Transactional
    public TblCustomReportCategoryList getCategories() {
        List<TblCustomReportCategory> categoryList = new ArrayList();
        TblCustomReportCategoryList categoryListAll = new TblCustomReportCategoryList();
        TblCustomReportCategory categories;
        Query query = entityManager.createQuery("SELECT t FROM TblCustomReportCategory t ORDER BY t.name ASC");
        List<TblCustomReportCategory> categoryQueryList = query.getResultList();
        for(TblCustomReportCategory categoriesList : categoryQueryList){
            categories = new TblCustomReportCategory();
            categories.setId(categoriesList.getId());
            categories.setName(categoriesList.getName());
            categoryList.add(categories);
        }
        categoryListAll.setTblCustomReportCategories(categoryList);
        return categoryListAll;
    }

    @Transactional
    public TblCustomReportCategoryList updateCategories(TblCustomReportCategoryList tblCustomReportCategoryList) {
        for(TblCustomReportCategory tblCustomReportCategory : tblCustomReportCategoryList.getTblCustomReportCategories()){
            if (tblCustomReportCategory.isAdded()) {
                String generatedId = IDGenerator.generate();
                tblCustomReportCategory.setId(generatedId);
                entityManager.persist(tblCustomReportCategory);
            }
            else if (tblCustomReportCategory.isModified()) {
                entityManager.merge(tblCustomReportCategory);
            }
        }
        return tblCustomReportCategoryList;
    }
    
    @Transactional
    public int deleteCategory(String id) {
        Query query = entityManager.createNamedQuery("TblCustomReportCategory.deleteById");
        query.setParameter("id", id);
        int deleteCnt = query.executeUpdate();
        return deleteCnt;
    }
}
