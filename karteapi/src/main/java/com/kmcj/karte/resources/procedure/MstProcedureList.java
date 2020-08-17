/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.procedure;
import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 工程マスタ（部品ごとの製造手順）Response
 * @author t.ariki
 */
public class MstProcedureList extends BasicResponse {    
    private List<MstProcedure> mstProcedures;
    private List<MstProcedureVo> mstProcedureVos;
    
    public MstProcedureList() {
        mstProcedures = new ArrayList<>();
    }
    public List<MstProcedure> getMstProcedures() {
        return mstProcedures;
    }
    public void setMstProcedures(List<MstProcedure> mstProcedures) {
        this.mstProcedures = mstProcedures;
    }

    public List<MstProcedureVo> getMstProcedureVos() {
        return mstProcedureVos;
    }

    public void setMstProcedureVos(List<MstProcedureVo> mstProcedureVos) {
        this.mstProcedureVos = mstProcedureVos;
    }
}
