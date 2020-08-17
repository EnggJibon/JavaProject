/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author penggd
 */
public class TblSigmaLogBatchVo extends BasicResponse {
    
    private String macKey;
    
    private List<SigmaLogDetailVo> logs;

    public String getMacKey() {
        return macKey;
    }

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

    public List<SigmaLogDetailVo> getLogs() {
        return logs;
    }

    public void setLogs(List<SigmaLogDetailVo> logs) {
        this.logs = logs;
    }    
    
}
