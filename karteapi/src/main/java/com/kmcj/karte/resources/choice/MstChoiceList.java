/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstChoiceList extends BasicResponse{

    private List<MstChoice> mstChoice;
    
    private List<MstChoiceVo> mstChoiceVo;

    /**
     * @return the mstChoice
     */
    public List<MstChoice> getMstChoice() {
        return mstChoice;
    }

    /**
     * @param mstChoice the mstChoice to set
     */
    public void setMstChoice(List<MstChoice> mstChoice) {
        this.mstChoice = mstChoice;
    }

    /**
     * @return the mstChoiceVo
     */
    public List<MstChoiceVo> getMstChoiceVo() {
        return mstChoiceVo;
    }

    /**
     * @param mstChoiceVo the mstChoiceVo to set
     */
    public void setMstChoiceVo(List<MstChoiceVo> mstChoiceVo) {
        this.mstChoiceVo = mstChoiceVo;
    }
}
