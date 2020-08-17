/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user.mail.reception;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstUserMailReceptionEventList extends BasicResponse {

    private List<MstUserMailReceptionEvent> mstUserMailReceptionEvents;
    private List<MstUserMailReceptionDepartment> mstUserMailReceptionDepartments;

    /**
     * @return the mstUserMailReceptionEvents
     */
    public List<MstUserMailReceptionEvent> getMstUserMailReceptionEvents() {
        return mstUserMailReceptionEvents;
    }

    /**
     * @param mstUserMailReceptionEvents the mstUserMailReceptionEvents to set
     */
    public void setMstUserMailReceptionEvents(List<MstUserMailReceptionEvent> mstUserMailReceptionEvents) {
        this.mstUserMailReceptionEvents = mstUserMailReceptionEvents;
    }

    /**
     * @return the mstUserMailReceptionDepartments
     */
    public List<MstUserMailReceptionDepartment> getMstUserMailReceptionDepartments() {
        return mstUserMailReceptionDepartments;
    }

    /**
     * @param mstUserMailReceptionDepartments the mstUserMailReceptionDepartments to set
     */
    public void setMstUserMailReceptionDepartments(List<MstUserMailReceptionDepartment> mstUserMailReceptionDepartments) {
        this.mstUserMailReceptionDepartments = mstUserMailReceptionDepartments;
    }

}
