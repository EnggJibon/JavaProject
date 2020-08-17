package com.kmcj.karte.resources.component.inspection.result;

import com.kmcj.karte.util.XmlDateAdapter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "tbl_component_inspection_result_file")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TblComponentInspectionResultFile.findAll", query = "SELECT m FROM TblComponentInspectionResultFile m"),
        @NamedQuery(name = "TblComponentInspectionResultFile.findById", query = "SELECT m FROM TblComponentInspectionResultFile m WHERE m.id = :id"),
        @NamedQuery(name = "TblComponentInspectionResultFile.findByComponentInspectionResultId",
                query = "SELECT m FROM TblComponentInspectionResultFile m LEFT JOIN TblUploadFile t ON m.fileUuid = t.fileUuid WHERE m.componentInspectionResultId = :componentInspectionResultId ORDER BY t.uploadDate DESC"),
        @NamedQuery(name = "TblComponentInspectionResultFile.deleteByCondition",
                query = "DELETE FROM TblComponentInspectionResultFile m WHERE m.componentInspectionResultId = :componentInspectionResultId AND m.fileUuid = :fileUuid"),
        @NamedQuery(name = "TblComponentInspectionResultFile.deleteBycomponentInspectionResultId",
                query = "DELETE FROM TblComponentInspectionResultFile m WHERE m.componentInspectionResultId = :componentInspectionResultId")
})
public class TblComponentInspectionResultFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_INSPECTION_RESULT_ID")
    private String componentInspectionResultId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "FILE_UUID")
    private String fileUuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentInspectionResultId() {
        return componentInspectionResultId;
    }

    public void setComponentInspectionResultId(String componentInspectionResultId) {
        this.componentInspectionResultId = componentInspectionResultId;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }
}
