/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.filelinkptn;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author admin
 */
public class MstFileLinkPtnes  extends BasicResponse {
    
    private String id;
    private String fileLinkPtnName;
    private String linkString;
    private int purpose;
    private String  deleteFlag;

    /**
     * @return the fileLinkPtnName
     */
    public String getFileLinkPtnName() {
        return fileLinkPtnName;
    }

    /**
     * @param fileLinkPtnName the fileLinkPtnName to set
     */
    public void setFileLinkPtnName(String fileLinkPtnName) {
        this.fileLinkPtnName = fileLinkPtnName;
    }

    /**
     * @return the linkString
     */
    public String getLinkString() {
        return linkString;
    }

    /**
     * @param linkString the linkString to set
     */
    public void setLinkString(String linkString) {
        this.linkString = linkString;
    }

    /**
     * @return the purpose
     */
    public int getPurpose() {
        return purpose;
    }

    /**
     * @param purpose the purpose to set
     */
    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    /**
     * @return the deleteFlag
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * @param deleteFlag the deleteFlag to set
     */
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
