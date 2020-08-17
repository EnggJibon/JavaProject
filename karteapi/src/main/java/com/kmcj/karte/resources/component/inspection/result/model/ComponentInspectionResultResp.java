/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * Component inspection item response model.
 *
 * @author duanlin
 */
public class ComponentInspectionResultResp extends BasicResponse {

    // for detail page
    private ComponentInspectionResultInfo inspectionResultInfo;

    // for list page
    private List<ComponentInspectionResultInfo> inspectionResultList;

    // for get outgoing result batch
    private List<ComponentInspectionResultInfoForBatch> dataForBatch;

    // for get outgoing poInfo batch
    private ComponentPoInfoForBatch dataForPoInfoBatch;

    /**
     * 検索条件に合致するデータの件数
     */
    private long count;
    /**
     * 現在表示しているページ番号
     */
    private int pageNumber;
    /**
     * 合計ページ数
     */
    private int pageTotal;

    public ComponentInspectionResultInfo getInspectionResultInfo() {
        return inspectionResultInfo;
    }

    public void setInspectionResultInfo(ComponentInspectionResultInfo inspectionResultInfo) {
        this.inspectionResultInfo = inspectionResultInfo;
    }

    public List<ComponentInspectionResultInfo> getInspectionResultList() {
        return inspectionResultList;
    }

    public void setInspectionResultList(List<ComponentInspectionResultInfo> inspectionResultList) {
        this.inspectionResultList = inspectionResultList;
    }

    public List<ComponentInspectionResultInfoForBatch> getDataForBatch() {
        return dataForBatch;
    }

    public void setDataForBatch(List<ComponentInspectionResultInfoForBatch> dataForBatch) {
        this.dataForBatch = dataForBatch;
    }

    public ComponentPoInfoForBatch getDataForPoInfoBatch() {
        return dataForPoInfoBatch;
    }

    public void setDataForPoInfoBatch(ComponentPoInfoForBatch dataForPoInfoBatch) {
        this.dataForPoInfoBatch = dataForPoInfoBatch;
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
