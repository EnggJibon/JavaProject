package com.kmcj.karte.resources.mold;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author Apeng
 * @date 20170609
 */
public class MstMoldSendList extends BasicResponse {

    private List<MstMold> mstMolds;//金型list
    private String mailAddress;//メールアドレス
    private String companyId;//会社ID
    private String subject;//件名
    private String letterBody;//本文

    public MstMoldSendList() {
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
     * @return the mstMolds
     */
    public List<MstMold> getMstMolds() {
        return mstMolds;
    }

    /**
     * @param mstMolds the mstMolds to set
     */
    public void setMstMolds(List<MstMold> mstMolds) {
        this.mstMolds = mstMolds;
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
