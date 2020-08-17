/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.filelinkptn;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstFileLinkPtnList extends BasicResponse {
    
    private List<MstFileLinkPtn> mstFileLinkPtnes ;
    
    private List<MstFileLinkPtnes> mstFileLinkPtnesList;
    
    /**
     * @return the mstFileLinkPtnes
     */
    public List<MstFileLinkPtn> getMstFileLinkPtnes() {
        return mstFileLinkPtnes;
    }

    /**
     * @param mstFileLinkPtnes the mstFileLinkPtnes to set
     */
    public void setMstFileLinkPtnes(List<MstFileLinkPtn> mstFileLinkPtnes) {
        this.mstFileLinkPtnes = mstFileLinkPtnes;
    }

    /**
     * @return the mstFileLinkPtnesList
     */
    public List<MstFileLinkPtnes> getMstFileLinkPtnesList() {
        return mstFileLinkPtnesList;
    }

    /**
     * @param mstFileLinkPtnesList the mstFileLinkPtnesList to set
     */
    public void setMstFileLinkPtnesList(List<MstFileLinkPtnes> mstFileLinkPtnesList) {
        this.mstFileLinkPtnesList = mstFileLinkPtnesList;
    }


    
    
}
