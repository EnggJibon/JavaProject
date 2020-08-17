/*

 */
package com.kmcj.karte.resources.mold.assetno;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author admin
 */
public class MstMoldAssetNos extends BasicResponse {

    private String id;
    private String assetNo;
    private int mainFlg;
    private Date numberedDate;
    private BigDecimal assetAmount;
    private String strAssetAmount;
    private String moldId;
    private String uuid;
    private String moldUuid;
    private String deleteFlag;

    public MstMoldAssetNos() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMainFlg() {
        return mainFlg;
    }

    public void setMainFlg(int mainFlg) {
        this.mainFlg = mainFlg;
    }

    public Date getNumberedDate() {
        return numberedDate;
    }

    public void setNumberedDate(Date numberedDate) {
        this.numberedDate = numberedDate;
    }

    public BigDecimal getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(BigDecimal assetAmount) {
        this.assetAmount = assetAmount;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
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

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @param strAssetAmount the strAssetAmount to set
     */
    public void setStrAssetAmount(String strAssetAmount) {
        this.strAssetAmount = strAssetAmount;
    }

    /**
     * @return the strAssetAmount
     */
    public String getStrAssetAmount() {
        return strAssetAmount;
    }

}
