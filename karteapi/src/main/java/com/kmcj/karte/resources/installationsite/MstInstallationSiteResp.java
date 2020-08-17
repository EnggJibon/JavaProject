/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.installationsite;

import com.kmcj.karte.BasicResponse;
/**
 *
 * @author c.darvin
 */
public class MstInstallationSiteResp extends BasicResponse{
    
    private MstInstallationSite mstInstallationSite;

    /**
     * @return the mstInstallationSite
     */
    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    /**
     * @param mstInstallationSite the mstInstallationSite to set
     */
    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }
    
}
