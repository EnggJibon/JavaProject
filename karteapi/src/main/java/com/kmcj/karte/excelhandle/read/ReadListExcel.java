package com.kmcj.karte.excelhandle.read;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.kmcj.karte.excelhandle.annotation.Excel;
import com.kmcj.karte.excelhandle.annotation.Select;
import com.kmcj.karte.excelhandle.util.ExcelUtil;
import com.kmcj.karte.excelhandle.util.MyStringUtils;
import java.io.FileInputStream;
import org.apache.commons.lang.StringUtils;

/**
 * 读取列表数据
 *
 * @author wangqh
 *
 * @param <T>
 */
public class ReadListExcel<T> implements ReadExcel<T> {

    private String inFilePath;
    private String sheetName;
    private int beginRow;

    @SuppressWarnings("unchecked")
    @Override
    public List<T> read(Map<String, Object> param, Class clazz) throws IOException, InstantiationException,
            IllegalAccessException {
        initParam(param, clazz);
        Workbook workbook = ExcelUtil.getWorkbookTypeByFile(inFilePath);
        if (workbook == null) {
            return new ArrayList();
        }
        return readList(workbook, clazz);
    }

    private void initParam(Map<String, Object> param, Class clazz) {
        if (param != null) {
            if (param.get("inFilePath") != null) {
                inFilePath = param.get("inFilePath").toString();
            }
            if (param.get("beginRow") != null) {
                beginRow = Integer.parseInt(param.get("beginRow").toString());
            }
            if (param.get("sheetName") != null) {
                sheetName = param.get("sheetName").toString();
            }
        } else {
            Excel excel = (Excel) clazz.getAnnotation(Excel.class);
            if (excel != null) {
                inFilePath = excel.inFilePath();
                beginRow = excel.beginRow();
            }
        }

    }

    @SuppressWarnings("unchecked")
    private List<T> readList(Workbook workbook, Class clazz) throws InstantiationException, IllegalAccessException, FileNotFoundException, IOException {
        List<T> list = new ArrayList();
        Excel excel = (Excel) clazz.getAnnotation(Excel.class);
        if (excel != null) {
            beginRow = excel.beginRow();
        }
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (StringUtils.isNotEmpty(sheetName)) {
                //指定されたシート名を読み込む
                if (sheetName.equals(sheet.getSheetName())) {
                    for (int rowIndex = beginRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                        T it = (T) clazz.newInstance();
                        Row row = sheet.getRow(rowIndex);
                        if (row == null) {
                            continue;
                        }
                        int countNum = 0;
                        for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                            Cell cell = row.getCell(columnIndex);
                            String value = ExcelUtil.getCellStringValue(cell);
                            if (MyStringUtils.isDecimalPointOfManyZero(value)) {
                                value = value.split("\\.")[0];
                            }
                            if (StringUtils.isEmpty(value)) {
                                countNum++;
                            }
                            for (Field field : clazz.getDeclaredFields()) {
                                com.kmcj.karte.excelhandle.annotation.Cell ca = field.getAnnotation(com.kmcj.karte.excelhandle.annotation.Cell.class);
                                if (ca == null) {
                                    continue;
                                }
                                int cn = ExcelUtil.cellTNumByCa(ca);
                                if (columnIndex == (cn - 1)) {
                                    field.setAccessible(true);
                                    Select select = field.getAnnotation(Select.class);
                                    if (select != null) {
                                        continue;
                                    }
                                    String type = field.getGenericType().toString();
                                    if (type.equals("class java.lang.Integer") || type.equals("int")) {
                                        field.set(it, Integer.parseInt(value));
                                    } else if (type.equals("class java.lang.Double") || type.equals("double")) {
                                        field.set(it, Double.parseDouble(value));
                                    } else if (type.equals("class java.lang.Boolean") || type.equals("boolean")) {
                                        field.set(it, Boolean.parseBoolean(value));
                                    } else {
                                        field.set(it, value);
                                    }
                                }
                            }
                        }
                        if (countNum == row.getLastCellNum()) {
                            break;
                        }
                        list.add(it);
                    }
                }
            } else {
                for (int rowIndex = beginRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    T it = (T) clazz.newInstance();
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) {
                        continue;
                    }
                    int countNum = 0;
                    for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                        Cell cell = row.getCell(columnIndex);
                        String value = ExcelUtil.getCellStringValue(cell);
                        if (MyStringUtils.isDecimalPointOfManyZero(value)) {
                            value = value.split("\\.")[0];
                        }
                        if (StringUtils.isEmpty(value)) {
                            countNum++;
                        }
                        for (Field field : clazz.getDeclaredFields()) {
                            com.kmcj.karte.excelhandle.annotation.Cell ca = field.getAnnotation(com.kmcj.karte.excelhandle.annotation.Cell.class);
                            if (ca == null) {
                                continue;
                            }
                            int cn = ExcelUtil.cellTNumByCa(ca);
                            if (columnIndex == (cn - 1)) {
                                field.setAccessible(true);
                                Select select = field.getAnnotation(Select.class);
                                if (select != null) {
                                    continue;
                                }
                                String type = field.getGenericType().toString();
                                if (type.equals("class java.lang.Integer") || type.equals("int")) {
                                    field.set(it, Integer.parseInt(value));
                                } else if (type.equals("class java.lang.Double") || type.equals("double")) {
                                    field.set(it, Double.parseDouble(value));
                                } else if (type.equals("class java.lang.Boolean") || type.equals("boolean")) {
                                    field.set(it, Boolean.parseBoolean(value));
                                } else {
                                    field.set(it, value);
                                }
                            }
                        }
                    }
                    if (countNum == row.getLastCellNum()) {
                        break;
                    }
                    list.add(it);
                }
            }
        }
        FileInputStream fileInputStream = new FileInputStream(inFilePath);
        fileInputStream.close();
        return list;
    }

}
