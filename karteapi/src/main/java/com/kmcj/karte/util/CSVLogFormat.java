/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author kmc
 */
public class CSVLogFormat extends Formatter {

    @Override
    public String format(LogRecord rec) {
        return formatMessage(rec);
    }
}
