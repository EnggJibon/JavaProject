/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;
//import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Fumisato
 */
//@XmlRootElement
public class MstUserList extends BasicResponse {
    private List<MstUser> mstUsers;

    public MstUserList() {
        mstUsers = new ArrayList<>();
    }
    
    /**
     * @return the mstUsers
     */
    public List<MstUser> getMstUsers() {
        return mstUsers;
    }

    /**
     * @param mstUsers the mstUsers to set
     */
    public void setMstUsers(List<MstUser> mstUsers) {
        this.mstUsers = mstUsers;
    }
    
}
