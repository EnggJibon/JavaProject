/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inventory;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.mold.MstMold;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "tbl_mold_inventory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldInventory.findAll", query = "SELECT t FROM TblMoldInventory t"),
    @NamedQuery(name = "TblMoldInventory.findById", query = "SELECT t FROM TblMoldInventory t WHERE t.id = :id"),
    @NamedQuery(name = "TblMoldInventory.findByInventoryDate", query = "SELECT t FROM TblMoldInventory t WHERE t.inventoryDate = :inventoryDate"),
    @NamedQuery(name = "TblMoldInventory.findByInventoryDateSzt", query = "SELECT t FROM TblMoldInventory t WHERE t.inventoryDateSzt = :inventoryDateSzt"),
    @NamedQuery(name = "TblMoldInventory.findByPersonUuid", query = "SELECT t FROM TblMoldInventory t WHERE t.personUuid = :personUuid"),
    @NamedQuery(name = "TblMoldInventory.findByInventoryResult", query = "SELECT t FROM TblMoldInventory t WHERE t.inventoryResult = :inventoryResult"),
    @NamedQuery(name = "TblMoldInventory.findBySiteConfirmMethod", query = "SELECT t FROM TblMoldInventory t WHERE t.siteConfirmMethod = :siteConfirmMethod"),
    @NamedQuery(name = "TblMoldInventory.findByMoldConfirmMethod", query = "SELECT t FROM TblMoldInventory t WHERE t.moldConfirmMethod = :moldConfirmMethod"),
    @NamedQuery(name = "TblMoldInventory.findByCompanyName", query = "SELECT t FROM TblMoldInventory t WHERE t.companyName = :companyName"),
    @NamedQuery(name = "TblMoldInventory.findByLocationName", query = "SELECT t FROM TblMoldInventory t WHERE t.locationName = :locationName"),
    @NamedQuery(name = "TblMoldInventory.findByInstllationSiteName", query = "SELECT t FROM TblMoldInventory t WHERE t.instllationSiteName = :instllationSiteName"),
    @NamedQuery(name = "TblMoldInventory.findByRemarks", query = "SELECT t FROM TblMoldInventory t WHERE t.remarks = :remarks"),
    @NamedQuery(name = "TblMoldInventory.findByInputType", query = "SELECT t FROM TblMoldInventory t WHERE t.inputType = :inputType"),
    @NamedQuery(name = "TblMoldInventory.findByCreateDate", query = "SELECT t FROM TblMoldInventory t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMoldInventory.findByUpdateDate", query = "SELECT t FROM TblMoldInventory t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMoldInventory.findByCreateUserUuid", query = "SELECT t FROM TblMoldInventory t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMoldInventory.findByUpdateUserUuid", query = "SELECT t FROM TblMoldInventory t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblMoldInventory.findFkByCompanyId", query = "SELECT t FROM TblMoldInventory t WHERE t.companyId = :companyId"),
    @NamedQuery(name = "TblMoldInventory.findFkByLocationId", query = "SELECT t FROM TblMoldInventory t WHERE t.locationId = :locationId"),
    @NamedQuery(name = "TblMoldInventory.findFkByInstallationSiteId", query = "SELECT t FROM TblMoldInventory t WHERE t.instllationSiteId = :instllationSiteId"),
    @NamedQuery(name = "TblMoldInventory.removeById", query = "DELETE FROM TblMoldInventory t WHERE t.id = :id"),
    @NamedQuery(name = "TblMoldInventory.updateById", query = " UPDATE TblMoldInventory t SET "
            + "t.inventoryDate = :inventoryDate,"
            + "t.inventoryResult = :inventoryResult,"
            + "t.siteConfirmMethod = :siteConfirmMethod,"
            + "t.moldConfirmMethod = :moldConfirmMethod,"
            + "t.inputType = :inputType,"
            + "t.remarks = :remarks,"
            + "t.createDate = :createDate,"
            + "t.updateDate = :updateDate,"
            + "t.createUserUuid = :createUserUuid,"
            + "t.updateUserUuid = :updateUserUuid, "
            + "t.companyId = :companyId,"
            + "t.companyName = :companyName,"
            + "t.locationId = :locationId,"
            + "t.locationName = :locationName,"
            + "t.instllationSiteId = :instllationSiteId,"
            + "t.instllationSiteName = :instllationSiteName"
            + " WHERE t.id = :id ")
})
@Cacheable(value = false)
public class TblMoldInventory implements Serializable {

    @Size(max = 45)
    @Column(name = "PERSON_NAME")
    private String personName;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;

//    @Basic(optional = false)
    @Size(max = 45)
    @NotNull
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    @Size(max = 45)
    @Column(name = "COMPANY_ID")
    private String companyId;
    @Size(max = 45)
    @Column(name = "INSTLLATION_SITE_ID")
    private String instllationSiteId;
    @Size(max = 45)
    @Column(name = "LOCATION_ID")
    private String locationId;

    @Column(name = "INVENTORY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inventoryDate;
    @Column(name = "INVENTORY_DATE_SZT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inventoryDateSzt;
    @Size(max = 45)
    @Column(name = "PERSON_UUID")
    private String personUuid;
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
    @Column(name = "MOLD_CONFIRM_METHOD")
    private int moldConfirmMethod;
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
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstCompany mstCompany;
    @JoinColumn(name = "INSTLLATION_SITE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstInstallationSite mstInstallationSite;
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstLocation mstLocation;

    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne//(optional = false)
    private MstMold mstMold;
    
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

    public TblMoldInventory() {
    }

    public TblMoldInventory(String id) {
        this.id = id;
    }

    public TblMoldInventory(String id, int inventoryResult, int siteConfirmMethod, int moldConfirmMethod, int inputType) {
        this.id = id;
        this.inventoryResult = inventoryResult;
        this.siteConfirmMethod = siteConfirmMethod;
        this.moldConfirmMethod = moldConfirmMethod;
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

    public int getMoldConfirmMethod() {
        return moldConfirmMethod;
    }

    public void setMoldConfirmMethod(int moldConfirmMethod) {
        this.moldConfirmMethod = moldConfirmMethod;
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

    public String getInstllationSiteId() {
        return instllationSiteId;
    }

    public String getLocationId() {
        return locationId;
    }

    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    public MstLocation getMstLocation() {
        return mstLocation;
    }

    public void setInstllationSiteId(String instllationSiteId) {
        this.instllationSiteId = instllationSiteId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }

    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }

    public MstMold getMstMold() {
        return mstMold;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }
/*
    @XmlTransient
    public Collection<MstMold> getMstMoldCollection() {
        return mstMoldCollection;
    }

    public void setMstMoldCollection(Collection<MstMold> mstMoldCollection) {
        this.mstMoldCollection = mstMoldCollection;
    }
*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldInventory)) {
            return false;
        }
        TblMoldInventory other = (TblMoldInventory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.TblMoldInventory[ id=" + id + " ]";
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    void setLocationId(Class<MstLocation> aClass, Object singleResult) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the moldUuid
     */
    public String getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldUuid the moldUuid to set
     */
    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public String getCompanyId() {
        return companyId;
    }

    public MstCompany getMstCompany() {
        return mstCompany;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
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
