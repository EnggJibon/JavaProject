package com.kmcj.karte.resources.machine;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author Apeng
 * @date 20170626
 */
public class MstMachineSendList extends BasicResponse {

    private List<MstMachine> mstMachines;//設備list
    private String mailAddress;//メールアドレス
    private String companyId;//会社ID
    private String subject;//件名
    private String letterBody;//本文

    public MstMachineSendList() {
    }

    /**
     * @return the mstMachines
     */
    public List<MstMachine> getMstMachines() {
        return mstMachines;
    }

    /**
     * @param mstMachines the mstMachines to set
     */
    public void setMstMachines(List<MstMachine> mstMachines) {
        this.mstMachines = mstMachines;
    }

    /**
     * @return the mailAddress
     */
    public String getMailAddress() {
        return mailAddress;
    }

    /**
     * @param mailAddress the mailAddress to set
     */
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the letterBody
     */
    public String getLetterBody() {
        return letterBody;
    }

    /**
     * @param letterBody the letterBody to set
     */
    public void setLetterBody(String letterBody) {
        this.letterBody = letterBody;
    }

}
