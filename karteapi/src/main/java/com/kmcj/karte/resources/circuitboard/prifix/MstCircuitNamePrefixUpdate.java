package com.kmcj.karte.resources.circuitboard.prifix;

import java.io.Serializable;
import java.util.Date;

public class MstCircuitNamePrefixUpdate implements Serializable {
    private String id;
    private String prefix;
    private String prefixId;
    private int displayFlg;
    private Date updateDate;
    private String updateUserUuid;

    public String getId() { return id;}

    public void setId(String id) {
        this.id = id;
    }

    public int getDisplayFlg() {
        return displayFlg;
    }

    /**
     * @param displayFlg the displayFlg to set
     */
    public void setDisplayFlg(int displayFlg) {
        this.displayFlg = displayFlg;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPrefixId(String oldId) {
        this.prefixId = oldId;
    }

    public String getPrefixId() {
        return this.prefixId;
    }

}
