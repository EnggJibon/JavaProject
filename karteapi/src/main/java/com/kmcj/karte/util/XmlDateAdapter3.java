/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Fumisato
 */
//@RequestScoped
public class XmlDateAdapter3 extends XmlAdapter<String, Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd"
    );
    
    @Override
    public Date unmarshal(String v) throws Exception {
        return dateFormat.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }
    
}
