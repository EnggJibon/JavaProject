/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.password.policy;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class CnfPasswordPolicyList extends BasicResponse {

    private List<CnfPasswordPolicy> cnfPasswordPolicies;

    /**
     * @return the cnfPasswordPolicies
     */
    public List<CnfPasswordPolicy> getCnfPasswordPolicies() {
        return cnfPasswordPolicies;
    }

    /**
     * @param cnfPasswordPolicies the cnfPasswordPolicies to set
     */
    public void setCnfPasswordPolicies(List<CnfPasswordPolicy> cnfPasswordPolicies) {
        this.cnfPasswordPolicies = cnfPasswordPolicies;
    }

}
