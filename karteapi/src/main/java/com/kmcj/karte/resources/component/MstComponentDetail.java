/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.attribute.MstComponentAttribute;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVo;
import com.kmcj.karte.resources.component.spec.MstComponentSpecDetail;

import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstComponentDetail extends BasicResponse{
    
   private MstComponent mstComponent;

   private List<MstMold> mstMoldList;
   
   private List<MstComponentCompanyVo> mstComponentCompanyVos;
   
   private List <MstComponentAttribute> mstComponentAttributes;
   
      
   /** 部品詳細用　Start*/
   private MstComponents mstComponents;
   private List<MstMoldDetail> mstMoldDetail;
   private List<MstComponentSpecDetail> mstComponentSpecDetailList;
   /** 部品詳細用　End*/

    /**
     * @return the mstMoldList
     */
    public List<MstMold> getMstMoldList() {
        return mstMoldList;
    }

    /**
     * @param mstMoldList the mstMoldList to set
     */
    public void setMstMoldList(List<MstMold> mstMoldList) {
        this.mstMoldList = mstMoldList;
    }


    /**
     * @return the mstComponent
     */
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    /**
     * @return the mstComponentSpecDetailList
     */
    public List<MstComponentSpecDetail> getMstComponentSpecDetailList() {
        return mstComponentSpecDetailList;
    }

    /**
     * @param mstComponentSpecDetailList the mstComponentSpecDetailList to set
     */
    public void setMstComponentSpecDetailList(List<MstComponentSpecDetail> mstComponentSpecDetailList) {
        this.mstComponentSpecDetailList = mstComponentSpecDetailList;
    }

    /**
     * @return the mstComponentAttributes
     */
    public List <MstComponentAttribute> getMstComponentAttributes() {
        return mstComponentAttributes;
    }

    /**
     * @param mstComponentAttributes the mstComponentAttributes to set
     */
    public void setMstComponentAttributes(List <MstComponentAttribute> mstComponentAttributes) {
        this.mstComponentAttributes = mstComponentAttributes;
    }

    /**
     * @return the mstComponents
     */
    public MstComponents getMstComponents() {
        return mstComponents;
    }

    /**
     * @param mstComponents the mstComponents to set
     */
    public void setMstComponents(MstComponents mstComponents) {
        this.mstComponents = mstComponents;
    }

    /**
     * @return the mstMoldDetail
     */
    public List<MstMoldDetail> getMstMoldDetail() {
        return mstMoldDetail;
    }

    /**
     * @param mstMoldDetail the mstMoldDetail to set
     */
    public void setMstMoldDetail(List<MstMoldDetail> mstMoldDetail) {
        this.mstMoldDetail = mstMoldDetail;
    }

    /**
     * @return the mstComponentCompanyVos
     */
    public List<MstComponentCompanyVo> getMstComponentCompanyVos() {
        return mstComponentCompanyVos;
    }

    /**
     * @param mstComponentCompanyVos the mstComponentCompanyVos to set
     */
    public void setMstComponentCompanyVos(List<MstComponentCompanyVo> mstComponentCompanyVos) {
        this.mstComponentCompanyVos = mstComponentCompanyVos;
    }

    
}
