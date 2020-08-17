package com.kmcj.karte.resources.sigma;

import com.kmcj.karte.BasicResponse;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstSigmaList extends BasicResponse {

    private List<MstSigma> mstSigmas;

    private List<MstSigmaVo> mstSigmaVos;
    
    private HashMap<String, String> resourceMap = new HashMap<String, String>();

    public MstSigmaList() {
    }

    public void setMstSigmas(List<MstSigma> mstSigmas) {
        this.mstSigmas = mstSigmas;
    }

    public void setMstSigmaVos(List<MstSigmaVo> mstSigmaVos) {
        this.mstSigmaVos = mstSigmaVos;
    }

    public List<MstSigma> getMstSigmas() {
        return mstSigmas;
    }

    public List<MstSigmaVo> getMstSigmaVos() {
        return mstSigmaVos;
    }

    public void setResourceMap(HashMap<String, String> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public HashMap<String, String> getResourceMap() {
        return resourceMap;
    }

}
