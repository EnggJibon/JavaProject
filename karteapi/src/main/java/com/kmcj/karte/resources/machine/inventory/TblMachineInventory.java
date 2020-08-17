package com.kmcj.karte.resources.machine.inventory;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.machine.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_machine_inventory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineInventory.findAll", query = "SELECT t FROM TblMachineInventory t"),
    @NamedQuery(name = "TblMachineInventory.findById", query = "SELECT t FROM TblMachineInventory t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineInventory.removeById", query = "DELETE FROM TblMachineInventory t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineInventory.findByInventoryDate", query = "SELECT t FROM TblMachineInventory t WHERE t.inventoryDate = :inventoryDate"),
    @NamedQuery(name = "TblMachineInventory.findByInventoryDateSzt", query = "SELECT t FROM TblMachineInventory t WHERE t.inventoryDateSzt = :inventoryDateSzt"),
    @NamedQuery(name = "TblMachineInventory.findByPersonUuid", query = "SELECT t FROM TblMachineInventory t WHERE t.personUuid = :personUuid"),
    @NamedQuery(name = "TblMachineInventory.findByPersonName", query = "SELECT t FROM TblMachineInventory t WHERE t.personName = :personName"),
    @NamedQuery(name = "TblMachineInventory.findByInventoryResult", query = "SELECT t FROM TblMachineInventory t WHERE t.inventoryResult = :inventoryResult"),
    @NamedQuery(name = "TblMachineInventory.findBySiteConfirmMethod", query = "SELECT t FROM TblMachineInventory t WHERE t.siteConfirmMethod = :siteConfirmMethod"),
    @NamedQuery(name = "TblMachineInventory.findByMachineConfirmMethod", query = "SELECT t FROM TblMachineInventory t WHERE t.machineConfirmMethod = :machineConfirmMethod"),
    @NamedQuery(name = "TblMachineInventory.findByCompanyName", query = "SELECT t FROM TblMachineInventory t WHERE t.companyName = :companyName"),
    @NamedQuery(name = "TblMachineInventory.findByLocationName", query = "SELECT t FROM TblMachineInventory t WHERE t.locationName = :locationName"),
    @NamedQuery(name = "TblMachineInventory.findByInstllationSiteName", query = "SELECT t FROM TblMachineInventory t WHERE t.instllationSiteName = :instllationSiteName"),
    @NamedQuery(name = "TblMachineInventory.findByRemarks", query = "SELECT t FROM TblMachineInventory t WHERE t.remarks = :remarks"),
    @NamedQuery(name = "TblMachineInventory.findByInputType", query = "SELECT t FROM TblMachineInventory t WHERE t.inputType = :inputType"),
    @NamedQuery(name = "TblMachineInventory.findByCreateDate", query = "SELECT t FROM TblMachineInventory t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineInventory.findByUpdateDate", query = "SELECT t FROM TblMachineInventory t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineInventory.findByCreateUserUuid", query = "SELECT t FROM TblMachineInventory t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineInventory.findByUpdateUserUuid", query = "SELECT t FROM TblMachineInventory t WHERE t.updateUserUuid = :updateUserUuid"),
     //  　会社マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "TblMachineInventory.findFkByCompanyId", query = "SELECT t FROM TblMachineInventory t WHERE t.companyId = :companyId"),
    // 　所在地マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "TblMachineInventory.findFkByLocationId", query = "SELECT t FROM TblMachineInventory t WHERE t.locationId = :locationId"),
    // 　設置場所マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "TblMachineInventory.findFkByInstallationSiteId", query = "SELECT t FROM TblMachineInventory t WHERE t.instllationSiteId = :instllationSiteId")
})
@Cacheable(value = false)
public class TblMachineInventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "INVENTORY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inventoryDate;
    @Column(name = "INVENTORY_DATE_SZT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inventoryDateSzt;
    @Size(max = 45)
    @Column(name = "PERSON_UUID")
    private String personUuid;
    @Size(max = 45)
    @Column(name = "PERSON_NAME")
    private String personName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INVENTORY_RESULT")
    private int inventoryResult;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SITE_CONFIRM_METHOD")
    private int siteConfirmMethod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MACHINE_CONFIRM_METHOD")
    private int machineConfirmMethod;
    @Size(max = 100)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Size(max = 100)
    @Column(name = "LOCATION_NAME")
    private String locationName;
    @Size(max = 100)
    @Column(name = "INSTLLATION_SITE_NAME")
    private String instllationSiteName;
    @Size(max = 200)
    @Column(name = "REMARKS")
    private String remarks;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INPUT_TYPE")
    private int inputType;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    @JoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;
    @Column(name = "COMPANY_ID")
    private String companyId;
    @JoinColumn(name = "INSTLLATION_SITE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstInstallationSite mstInstallationSite;
    @Column(name = "INSTLLATION_SITE_ID")
    private String instllationSiteId;
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstLocation mstLocation;
    @Column(name = "LOCATION_ID")
    private String locationId;

    // KM-149 対応　ADD
    @Column(name = "IMG_FILE_PATH")
    private String imgFilePath;
    
    @Column(name = "FILE_TYPE")
    private Integer fileType;
  
    @Column(name = "TAKEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenDate;
    
    @Column(name = "TAKEN_DATE_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenDateStz;
    
    @Column(name = "DEPARTMENT_CHANGE")
    private Integer departmentChange;

    @Column(name = "DEPARTMENT")
    private Integer department;

    @Column(name = "BARCODE_REPRINT")
    private Integer barcodeReprint;

    @Column(name = "ASSET_DAMAGED")
    private Integer assetDamaged;

    @Column(name = "NOT_IN_USE")
    private Integer notInUse;

    @Column(name = "IMAGE_FILE_KEY")
    private String imageFileKey;

    @Column(name = "OUTPUT_FILE_UUID")
    private String outputFileUuid;

    public TblMachineInventory() {
    }

    public TblMachineInventory(String id) {
        this.id = id;
    }

    public TblMachineInventory(String id, int inventoryResult, int siteConfirmMethod, int machineConfirmMethod, int inputType) {
        this.id = id;
        this.inventoryResult = inventoryResult;
        this.siteConfirmMethod = siteConfirmMethod;
        this.machineConfirmMethod = machineConfirmMethod;
        this.inputType = inputType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public Date getInventoryDateSzt() {
        return inventoryDateSzt;
    }

    public void setInventoryDateSzt(Date inventoryDateSzt) {
        this.inventoryDateSzt = inventoryDateSzt;
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getInventoryResult() {
        return inventoryResult;
    }

    public void setInventoryResult(int inventoryResult) {
        this.inventoryResult = inventoryResult;
    }

    public int getSiteConfirmMethod() {
        return siteConfirmMethod;
    }

    public void setSiteConfirmMethod(int siteConfirmMethod) {
        this.siteConfirmMethod = siteConfirmMethod;
    }

    public int getMachineConfirmMethod() {
        return machineConfirmMethod;
    }

    public void setMachineConfirmMethod(int machineConfirmMethod) {
        this.machineConfirmMethod = machineConfirmMethod;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getInstllationSiteName() {
        return instllationSiteName;
    }

    public void setInstllationSiteName(String instllationSiteName) {
        this.instllationSiteName = instllationSiteName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public MstCompany getMstCompany() {
        return mstCompany;
    }

    public String getCompanyId() {
        return companyId;
    }

    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    public String getInstllationSiteId() {
        return instllationSiteId;
    }

    public MstLocation getMstLocation() {
        return mstLocation;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }

    public void setInstllationSiteId(String instllationSiteId) {
        this.instllationSiteId = instllationSiteId;
    }

    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineInventory)) {
            return false;
        }
        TblMachineInventory other = (TblMachineInventory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.TblMachineInventory[ id=" + id + " ]";
    }

    /**
     * @return the fileType
     */
    public Integer getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the takenDate
     */
    public Date getTakenDate() {
        return takenDate;
    }

    /**
     * @param takenDate the takenDate to set
     */
    public void setTakenDate(Date takenDate) {
        this.takenDate = takenDate;
    }

    /**
     * @return the takenDateStz
     */
    public Date getTakenDateStz() {
        return takenDateStz;
    }

    /**
     * @param takenDateStz the takenDateStz to set
     */
    public void setTakenDateStz(Date takenDateStz) {
        this.takenDateStz = takenDateStz;
    }

    /**
     * @return the imgFilePath
     */
    public String getImgFilePath() {
        return imgFilePath;
    }

    /**
     * @param imgFilePath the imgFilePath to set
     */
    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
    }

    /**
     * @return the departmentChange
     */
    public Integer getDepartmentChange() {
        return departmentChange;
    }

    /**
     * @param departmentChange the departmentChange to set
     */
    public void setDepartmentChange(Integer departmentChange) {
        this.departmentChange = departmentChange;
    }

    /**
     * @return the department
     */
    public Integer getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(Integer department) {
        this.department = department;
    }

    /**
     * @return the barcodeReprint
     */
    public Integer getBarcodeReprint() {
        return barcodeReprint;
    }

    /**
     * @param barcodeReprint the barcodeReprint to set
     */
    public void setBarcodeReprint(Integer barcodeReprint) {
        this.barcodeReprint = barcodeReprint;
    }

    /**
     * @return the assetDamaged
     */
    public Integer getAssetDamaged() {
        return assetDamaged;
    }

    /**
     * @param assetDamaged the assetDamaged to set
     */
    public void setAssetDamaged(Integer assetDamaged) {
        this.assetDamaged = assetDamaged;
    }

    /**
     * @return the notInUse
     */
    public Integer getNotInUse() {
        return notInUse;
    }

    /**
     * @param notInUse the notInUse to set
     */
    public void setNotInUse(Integer notInUse) {
        this.notInUse = notInUse;
    }
    
    /**
     * @return the imageFileKey
     */
    public String getImageFileKey() {
        return imageFileKey;
    }

    /**
     * @param imageFileKey the imageFileKey to set
     */
    public void setImageFileKey(String imageFileKey) {
        this.imageFileKey = imageFileKey;
    }

    /**
     * @return the outputFileUuid
     */
    public String getOutputFileUuid() {
        return outputFileUuid;
    }

    /**
     * @param outputFileUuid the outputFileUuid to set
     */
    public void setOutputFileUuid(String outputFileUuid) {
        this.outputFileUuid = outputFileUuid;
    }
}
