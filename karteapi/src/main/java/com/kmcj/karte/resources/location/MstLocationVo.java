package com.kmcj.karte.resources.location;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;

/**
 *
 * @author admin
 */

@Cacheable(value = false)
public class MstLocationVo extends BasicResponse {
    private String id;    
    private String locationCode;
    private String locationName;
    private String zipCode;
    private String address;
    private String telNo;
    private String companyId;
    private String companyCode;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private Integer externalFlg;
    private MstLocation mstLocation;
    private List<MstLocationVo> mstLocationVos;
    
    private String mgmtCompanyCode;//20170607 Apeng add

    public MstLocationVo() {
    }

    public String getId() {
        return id;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getAddress() {
        return address;
    }

    public String getTelNo() {
        return telNo;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyCode() {
        return companyCode;
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

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public MstLocation getMstLocation() {
        return mstLocation;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }

    public List<MstLocationVo> getMstLocationVos() {
        return mstLocationVos;
    }

    public void setMstLocationVos(List<MstLocationVo> mstLocationVos) {
        this.mstLocationVos = mstLocationVos;
    }

    /**
     * @return the mgmtCompanyCode
     */
    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    /**
     * @param mgmtCompanyCode the mgmtCompanyCode to set
     */
    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }
}
