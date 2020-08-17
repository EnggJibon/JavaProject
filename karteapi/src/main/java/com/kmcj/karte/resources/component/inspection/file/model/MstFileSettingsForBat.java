package com.kmcj.karte.resources.component.inspection.file.model;

import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectClass;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFile;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectLang;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectType;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 検査区分、部品業種、ファイル登録設定をpushするためのデータクラス。
 * @author t.takasaki
 */
@XmlRootElement
public class MstFileSettingsForBat implements Serializable {
    /** 検査区分*/
    private List<MstComponentInspectClass> inspClasses;
    /** 部品業種*/
    private List<MstComponentInspectType> inspTypes;
    /** ファイル登録設定*/
    private List<MstComponentInspectFile> inspFiles;
    /* ファイル名称*/
    private List<FileName> fileNames;
    /* 検査系連携文言*/
    private List<MstComponentInspectLang> inspLangs;
    
    public List<MstComponentInspectClass> getInspClasses() {
        return inspClasses;
    }

    public void setInspClasses(List<MstComponentInspectClass> inspClasses) {
        this.inspClasses = inspClasses;
    }

    public List<MstComponentInspectType> getInspTypes() {
        return inspTypes;
    }

    public void setInspTypes(List<MstComponentInspectType> inspTypes) {
        this.inspTypes = inspTypes;
    }

    public List<MstComponentInspectFile> getInspFiles() {
        return inspFiles;
    }

    public void setInspFiles(List<MstComponentInspectFile> inspFiles) {
        this.inspFiles = inspFiles;
    }

    public List<FileName> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<FileName> fileNames) {
        this.fileNames = fileNames;
    }

    public List<MstComponentInspectLang> getInspLangs() {
        return inspLangs;
    }

    public void setInspLangs(List<MstComponentInspectLang> inspLangs) {
        this.inspLangs = inspLangs;
    }

    public static class FileName implements Serializable {
        private String id;
        private String fileName;
        
        public FileName() {
        }
        
        public FileName(String id, String fileName) {
            this.id = id;
            this.fileName = fileName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
