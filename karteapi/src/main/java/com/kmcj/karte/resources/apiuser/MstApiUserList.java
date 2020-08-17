/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.apiuser;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ksen
 */
public class MstApiUserList extends BasicResponse{
    private List<MstApiUserVo> mstApiUserVoList;
    
    private List<MstApiUser> mstApiUser;
    
    public MstApiUserList(){
        mstApiUser = new ArrayList<>();
    }

    /**
     * @return the mstApiUserVoList
     */
    public List<MstApiUserVo> getMstApiUserVoList() {
        return mstApiUserVoList;
    }

    /**
     * @param mstApiUserVoList the mstApiUserVoList to set
     */
    public void setMstApiUserVoList(List<MstApiUserVo> mstApiUserVoList) {
        this.mstApiUserVoList = mstApiUserVoList;
    }

    /**
     * @return the mstApiUser
     */
    public List<MstApiUser> getMstApiUser() {
        return mstApiUser;
    }

    /**
     * @param mstApiUser the mstApiUser to set
     */
    public void setMstApiUser(List<MstApiUser> mstApiUser) {
        this.mstApiUser = mstApiUser;
    }
    
    

}
