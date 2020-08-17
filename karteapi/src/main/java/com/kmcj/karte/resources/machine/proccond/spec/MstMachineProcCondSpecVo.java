package com.kmcj.karte.resources.machine.proccond.spec;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.proccond.MstMachineProcCond;
import com.kmcj.karte.resources.machine.proccond.attribute.MstMachineProcCondAttribute;
import java.util.Date;

/**
 *
 * @author admin
 */
public class MstMachineProcCondSpecVo extends BasicResponse {

    protected MstMachineProcCondSpecPK mstMachineProcCondSpecPK;

    private String id;

    private String attrValue;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private MstMachineProcCondAttribute mstMachineProcCondAttribute;

    private MstMachineProcCond mstMachineProcCond;
    private String componentId;

    public MstMachineProcCondSpecVo() {
    }

    public MstMachineProcCondSpecVo(MstMachineProcCondSpecPK mstMachineProcCondSpecPK) {
        this.mstMachineProcCondSpecPK = mstMachineProcCondSpecPK;
    }

    public MstMachineProcCondSpecVo(MstMachineProcCondSpecPK mstMachineProcCondSpecPK, String id) {
        this.mstMachineProcCondSpecPK = mstMachineProcCondSpecPK;
        this.id = id;
    }

    public MstMachineProcCondSpecVo(String machineProcCondId, String attrId) {
        this.mstMachineProcCondSpecPK = new MstMachineProcCondSpecPK(machineProcCondId, attrId);
    }

    public MstMachineProcCondSpecPK getMstMachineProcCondSpecPK() {
        return mstMachineProcCondSpecPK;
    }

    public void setMstMachineProcCondSpecPK(MstMachineProcCondSpecPK mstMachineProcCondSpecPK) {
        this.mstMachineProcCondSpecPK = mstMachineProcCondSpecPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
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

    public MstMachineProcCondAttribute getMstMachineProcCondAttribute() {
        return mstMachineProcCondAttribute;
    }

    public void setMstMachineProcCondAttribute(MstMachineProcCondAttribute mstMachineProcCondAttribute) {
        this.mstMachineProcCondAttribute = mstMachineProcCondAttribute;
    }

    public MstMachineProcCond getMstMachineProcCond() {
        return mstMachineProcCond;
    }

    public void setMstMachineProcCond(MstMachineProcCond mstMachineProcCond) {
        this.mstMachineProcCond = mstMachineProcCond;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentId() {
        return componentId;
    }

}
