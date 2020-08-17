package com.kmcj.karte.excelhandle.read;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReadExcel<T> {
	
	public List<T> read(Map<String,Object> param, Class clazz) throws IOException, InstantiationException,
		IllegalAccessException;
}
