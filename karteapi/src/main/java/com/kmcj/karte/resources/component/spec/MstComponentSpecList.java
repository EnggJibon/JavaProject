/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.spec;
import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstComponentSpecList extends BasicResponse {

        
    private List<MstComponentAttrSpecVo> csvMstComponentAttrSpecVo; //画面CSV取込用
    private List< MstComponentSpec> mstComponentSpecs;

    /**
     * @return the mstComponentSpecs
     */
    public List< MstComponentSpec> getMstComponentSpecs() {
        return mstComponentSpecs;
    }

    /**
     * @param mstComponentSpecs the mstComponents to set
     */
    public void setMstComponentSpecs(List< MstComponentSpec> mstComponentSpecs) {
        this.mstComponentSpecs = mstComponentSpecs;
    }

    /**
     * @return the csvMstComponentAttrSpecVo
     */
    public List<MstComponentAttrSpecVo> getCsvMstComponentAttrSpecVo() {
        return csvMstComponentAttrSpecVo;
    }

    /**
     * @param csvMstComponentAttrSpecVo the csvMstComponentAttrSpecVo to set
     */
    public void setCsvMstComponentAttrSpecVo(List<MstComponentAttrSpecVo> csvMstComponentAttrSpecVo) {
        this.csvMstComponentAttrSpecVo = csvMstComponentAttrSpecVo;
    }
}
