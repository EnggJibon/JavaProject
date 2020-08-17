package com.kmcj.karte.resources.component.inspection.item.model;

import com.kmcj.karte.util.XmlDateAdapter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "mst_component_inspection_items_file")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "MstComponentInspectionItemsFile.findAll", query = "SELECT m FROM MstComponentInspectionItemsFile m"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.findById", query = "SELECT m FROM MstComponentInspectionItemsFile m WHERE m.id = :id"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.findByComponentId", query = "SELECT m FROM MstComponentInspectionItemsFile m WHERE m.componentId = :componentId"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.findByOutgoingCompanyId", query = "SELECT m FROM MstComponentInspectionItemsFile m WHERE m.outgoingCompanyId = :outgoingCompanyId"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.findByIncomingCompanyId", query = "SELECT m FROM MstComponentInspectionItemsFile m WHERE m.incomingCompanyId = :incomingCompanyId"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.findByCondition", query = "SELECT m FROM MstComponentInspectionItemsFile m WHERE m.componentId = :componentId AND m.outgoingCompanyId = :outgoingCompanyId AND m.incomingCompanyId = :incomingCompanyId"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.findByConditionSort",
                query = "SELECT m FROM MstComponentInspectionItemsFile m LEFT JOIN TblUploadFile t ON m.fileUuid = t.fileUuid WHERE m.componentId = :componentId AND m.outgoingCompanyId = :outgoingCompanyId AND m.incomingCompanyId = :incomingCompanyId ORDER BY t.uploadDate DESC"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.deleteByCondition", query = "DELETE FROM MstComponentInspectionItemsFile m WHERE m.componentId = :componentId AND m.outgoingCompanyId = :outgoingCompanyId AND m.incomingCompanyId = :incomingCompanyId AND m.fileUuid = :fileUuid"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.deleteByInspectionItems",
                query = "DELETE FROM MstComponentInspectionItemsFile m WHERE m.componentId = :componentId AND m.outgoingCompanyId = :outgoingCompanyId AND m.incomingCompanyId = :incomingCompanyId"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.deleteById", query = "DELETE FROM MstComponentInspectionItemsFile m WHERE m.id = :id"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.deleteByUuid", query = "DELETE FROM MstComponentInspectionItemsFile m WHERE m.fileUuid = :fileUuid"),
        @NamedQuery(name = "MstComponentInspectionItemsFile.clear", query = "DELETE FROM MstComponentInspectionItemsFile m WHERE m.componentId = :componentId AND m.outgoingCompanyId = :outgoingCompanyId AND m.incomingCompanyId = :incomingCompanyId")
})
public class MstComponentInspectionItemsFile implements Serializable {
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
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "OUTGOING_COMPANY_ID")
    private String outgoingCompanyId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INCOMING_COMPANY_ID")
    private String incomingCompanyId;
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

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getOutgoingCompanyId() {
        return outgoingCompanyId;
    }

    public void setOutgoingCompanyId(String outgoingCompanyId) {
        this.outgoingCompanyId = outgoingCompanyId;
    }

    public String getIncomingCompanyId() {
        return incomingCompanyId;
    }

    public void setIncomingCompanyId(String incomingCompanyId) {
        this.incomingCompanyId = incomingCompanyId;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }
}
