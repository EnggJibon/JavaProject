package com.kmcj.karte.excelhandle.write;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Apeng
 */
public interface WriteExcelList {
    
    //默认样式写入
    public boolean write(Map<String, Object> param, List list) throws IOException, IllegalArgumentException, IllegalAccessException;
}
