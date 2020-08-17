/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

/**
 *
 * @author f.kitaoji
 */
public class ImportResultResponse extends BasicResponse {
    private long totalCount;
    private long succeededCount;
    private long addedCount;
    private long updatedCount;
    private long deletedCount;
    private long failedCount;
    private String log;

    /**
     * @return the totalCount
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return the succeededCount
     */
    public long getSucceededCount() {
        return succeededCount;
    }

    /**
     * @param succeededCount the succeededCount to set
     */
    public void setSucceededCount(long succeededCount) {
        this.succeededCount = succeededCount;
    }

    /**
     * @return the addedCount
     */
    public long getAddedCount() {
        return addedCount;
    }

    /**
     * @param addedCount the addedCount to set
     */
    public void setAddedCount(long addedCount) {
        this.addedCount = addedCount;
    }

    /**
     * @return the updatedCount
     */
    public long getUpdatedCount() {
        return updatedCount;
    }

    /**
     * @param updatedCount the updatedCount to set
     */
    public void setUpdatedCount(long updatedCount) {
        this.updatedCount = updatedCount;
    }

    /**
     * @return the deletedCount
     */
    public long getDeletedCount() {
        return deletedCount;
    }

    /**
     * @param deletedCount the deletedCount to set
     */
    public void setDeletedCount(long deletedCount) {
        this.deletedCount = deletedCount;
    }

    /**
     * @return the failedCount
     */
    public long getFailedCount() {
        return failedCount;
    }

    /**
     * @param failedCount the failedCount to set
     */
    public void setFailedCount(long failedCount) {
        this.failedCount = failedCount;
    }

    /**
     * @return the log
     */
    public String getLog() {
        return log;
    }

    /**
     * @param log the log to set
     */
    public void setLog(String log) {
        this.log = log;
    }
}
