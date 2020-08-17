/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import com.kmcj.karte.resources.component.inspection.referencefile.TblComponentInspectionReferenceFile;
import com.kmcj.karte.resources.component.inspection.referencefile.TblComponentInspectionReferenceFileNewest;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResult;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultDetail;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultFile;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionSampleName;
import com.kmcj.karte.resources.component.inspection.visualimage.TblComponentInspectionVisualImageFile;
import java.util.List;

/**
 * Component inspection result info model.
 *
 * @author duanlin
 */
public class ComponentInspectionResultInfoForBatch {
    private String componentCode;
    
    private TblComponentInspectionResult inspectionResult;
    private TblComponentInspectionReferenceFile inspectionRefFile;
    private List<TblComponentInspectionResultDetail> inspectionResultDetails;
    private List<TblComponentInspectionSampleName> inspectionSampleNames;
    private List<TblComponentInspectionVisualImageFile> inspectionVisualImageFiles; 
    private TblComponentInspectionReferenceFileNewest inspectionRefFileNewest;

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public TblComponentInspectionResult getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(TblComponentInspectionResult inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    public TblComponentInspectionReferenceFile getInspectionRefFile() {
        return inspectionRefFile;
    }

    public void setInspectionRefFile(TblComponentInspectionReferenceFile inspectionRefFile) {
        this.inspectionRefFile = inspectionRefFile;
    }

    public List<TblComponentInspectionResultDetail> getInspectionResultDetails() {
        return inspectionResultDetails;
    }

    public void setInspectionResultDetails(List<TblComponentInspectionResultDetail> inspectionResultDetails) {
        this.inspectionResultDetails = inspectionResultDetails;
    }

    public List<TblComponentInspectionVisualImageFile> getInspectionVisualImageFiles() {
        return inspectionVisualImageFiles;
    }

    public void setInspectionVisualImageFiles(List<TblComponentInspectionVisualImageFile> inspectionVisualImageFiles) {
        this.inspectionVisualImageFiles = inspectionVisualImageFiles;
    }

    public TblComponentInspectionReferenceFileNewest getInspectionRefFileNewest() {
        return inspectionRefFileNewest;
    }

    public void setInspectionRefFileNewest(TblComponentInspectionReferenceFileNewest inspectionRefFileNewest) {
        this.inspectionRefFileNewest = inspectionRefFileNewest;
    }

    public List<TblComponentInspectionSampleName> getInspectionSampleNames() {
        return inspectionSampleNames;
    }

    public void setInspectionSampleNames(List<TblComponentInspectionSampleName> inspectionSampleNames) {
        this.inspectionSampleNames = inspectionSampleNames;
    }
}
