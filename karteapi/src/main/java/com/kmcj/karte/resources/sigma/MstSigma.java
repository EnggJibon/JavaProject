package com.kmcj.karte.resources.sigma;

import com.kmcj.karte.resources.machine.MstMachine;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_sigma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstSigma.findAll", query = "SELECT m FROM MstSigma m"),
    @NamedQuery(name = "MstSigma.findById", query = "SELECT m FROM MstSigma m WHERE m.id = :id"),
    @NamedQuery(name = "MstSigma.findBySigmaId", query = "SELECT m FROM MstSigma m WHERE m.id = :sigmaId"),
    @NamedQuery(name = "MstSigma.findBySigmaCode", query = "SELECT m FROM MstSigma m WHERE m.sigmaCode = :sigmaCode"),
    @NamedQuery(name = "MstSigma.findByFilesPath", query = "SELECT m FROM MstSigma m WHERE m.filesPath = :filesPath"),
    @NamedQuery(name = "MstSigma.findByBackupFilesPath", query = "SELECT m FROM MstSigma m WHERE m.backupFilesPath = :backupFilesPath"),
    @NamedQuery(name = "MstSigma.findByMachineName", query = "SELECT m FROM MstSigma m WHERE m.machineName = :machineName"),
    @NamedQuery(name = "MstSigma.findByIpAddress", query = "SELECT m FROM MstSigma m WHERE m.ipAddress = :ipAddress"),
    @NamedQuery(name = "MstSigma.findByValidFlg", query = "SELECT m FROM MstSigma m WHERE m.validFlg = :validFlg"),
    @NamedQuery(name = "MstSigma.findByCreateDate", query = "SELECT m FROM MstSigma m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstSigma.findByUpdateDate", query = "SELECT m FROM MstSigma m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstSigma.findByCreateUserUuid", query = "SELECT m FROM MstSigma m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstSigma.findByUpdateUserUuid", query = "SELECT m FROM MstSigma m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstSigma implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "SIGMA_CODE")
    private String sigmaCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "FILES_PATH")
    private String filesPath;
    @Size(max = 400)
    @Column(name = "BACK_UP_FILES_PATH")
    private String backupFilesPath;
    @Size(max = 100)
    @Column(name = "MACHINE_NAME")
    private String machineName;
    @Size(max = 45)
    @Column(name = "IP_ADDRESS")
    private String ipAddress; 
    @Basic(optional = false)
    @NotNull
    @Column(name = "COUNT_ERROR_NOTICE")
    private int countErrorNotice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ERROR_NOTICE_INTERVAL")
    private int errorNoticeInterval;
    @Column(name = "VALID_FLG")
    private Integer validFlg;
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
    @Size(max = 45)
    @Column(name = "GUNSHI_USER")
    private String gunshiUser;
    @Size(max = 255)
    @Column(name = "GUNSHI_PASSWORD")
    private String gunshiPassword;


    @Transient
    private String operationFlag;

    public MstSigma() {
    }

    public MstSigma(String id) {
        this.id = id;
    }

    public MstSigma(String id, String filesPath) {
        this.id = id;
        this.filesPath = filesPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSigmaCode() {
        return sigmaCode;
    }

    public void setSigmaCode(String sigmaCode) {
        this.sigmaCode = sigmaCode;
    }

    public String getFilesPath() {
        return filesPath;
    }

    public void setFilesPath(String filesPath) {
        this.filesPath = filesPath;
    }
    
    public String getBackupFilesPath() {
        return backupFilesPath;
    }

    public void setBackupFilesPath(String backupFilesPath) {
        this.backupFilesPath = backupFilesPath;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getValidFlg() {
        return validFlg;
    }

    public void setValidFlg(Integer validFlg) {
        this.validFlg = validFlg;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstSigma)) {
            return false;
        }
        MstSigma other = (MstSigma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.sigma.MstSigma[ id=" + id + " ]";
    }

    /**
     * @return the operationFlag
     */
    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @param operationFlag the operationFlag to set
     */
    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    /**
     * @return the gunshiUser
     */
    public String getGunshiUser() {
        return gunshiUser;
    }

    /**
     * @param gunshiUser the gunshiUser to set
     */
    public void setGunshiUser(String gunshiUser) {
        this.gunshiUser = gunshiUser;
    }
    
    /**
     * @return the gunshiPassword
     */
    public String getGunshiPassword() {
        return gunshiPassword;
    }
    
    /**
     * @param gunshiPassword the gunshiPassword to set
     */
    public void setGunshiPassword(String gunshiPassword) {
        this.gunshiPassword = gunshiPassword;
    }
    public int getCountErrorNotice() {
        return countErrorNotice;
    }

    public void setCountErrorNotice(int countErrorNotice) {
        this.countErrorNotice = countErrorNotice;
    }

    public int getErrorNoticeInterval() {
        return errorNoticeInterval;
    }

    public void setErrorNoticeInterval(int errorNoticeInterval) {
        this.errorNoticeInterval = errorNoticeInterval;
    }
}
