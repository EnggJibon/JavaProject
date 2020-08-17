/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.component.relation;
import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMoldComponentRelationList  extends BasicResponse {
    private List <MstMoldComponentRelation> mstMoldComponentRelation;
    private List <MstMoldComponentRelationVo> mstMoldComponentRelationVos;

    /**
     * @return the mstMoldComponentRelation
     */
    public List <MstMoldComponentRelation> getMstMoldComponentRelation() {
        return mstMoldComponentRelation;
    }

    /**
     * @param mstMoldComponentRelation the mstMoldComponentRelation to set
     */
    public void setMstMoldComponentRelation(List <MstMoldComponentRelation> mstMoldComponentRelation) {
        this.mstMoldComponentRelation = mstMoldComponentRelation;
    }    

    public List<MstMoldComponentRelationVo> getMstMoldComponentRelationVos() {
        return mstMoldComponentRelationVos;
    }

    public void setMstMoldComponentRelationVos(List<MstMoldComponentRelationVo> mstMoldComponentRelationVos) {
        this.mstMoldComponentRelationVos = mstMoldComponentRelationVos;
    }
    
}
