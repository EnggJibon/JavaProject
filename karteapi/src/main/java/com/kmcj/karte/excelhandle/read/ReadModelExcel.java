package com.kmcj.karte.excelhandle.read;

import com.kmcj.karte.excelhandle.annotation.Excel;
import com.kmcj.karte.excelhandle.annotation.Ingroe;
import com.kmcj.karte.excelhandle.util.ExcelUtil;
import com.kmcj.karte.excelhandle.util.MyStringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Apeng
 * @param <T>
 */
public class ReadModelExcel<T> implements ReadExcel<T> {

    private String inFilePath;
    private Workbook workbook;
    private Sheet sheet;

    @Override
    public List<T> read(Map<String, Object> param, Class clazz) throws IOException, InstantiationException, IllegalAccessException {
        initParam(param, clazz);
        workbook = ExcelUtil.getWorkbookTypeByFile(inFilePath);
        if (workbook == null) {
            throw new FileNotFoundException("File does not exist！");
        }
        return readModel(workbook, clazz);
    }

    private List<T> readModel(Workbook workbook, Class clazz) throws IllegalAccessException, InstantiationException {
        List<T> list = new ArrayList<T>();
        com.kmcj.karte.excelhandle.annotation.Sheet s = (com.kmcj.karte.excelhandle.annotation.Sheet) clazz.getAnnotation(com.kmcj.karte.excelhandle.annotation.Sheet.class);
        int sheetNum = 1;
        if (s != null) {
            sheetNum = s.sheetNum();
        }
        sheet = workbook.getSheetAt(sheetNum - 1);
        T t = (T) clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Ingroe ingroe = field.getAnnotation(Ingroe.class);
            if (ingroe != null) {
                continue;
            }
            com.kmcj.karte.excelhandle.annotation.Cell cl = field.getAnnotation(com.kmcj.karte.excelhandle.annotation.Cell.class);
            String value = "";
            if (cl == null) {
                value = writeCellOfLable(field.getName());
            } else if (MyStringUtils.isNotBlank(cl.name())) {
                value = writeCellOfLable(cl.name());
            } else {
                Cell cell = sheet.getRow(cl.rowNum() - 1).getCell(ExcelUtil.cellTNumByCa(cl) - 1);
                value = ExcelUtil.getCellStringValue(cell);
            }
            if (MyStringUtils.isNotBlank(value)) {
                if (MyStringUtils.isDecimalPointOfManyZero(value)) {
                    value = value.substring(0, value.indexOf("."));
                }
                String type = field.getGenericType().toString();
                if (type.equals("class java.lang.Integer") || type.equals("int")) {
                    field.set(t, Integer.parseInt(value));
                } else if (type.equals("class java.lang.Double") || type.equals("double")) {
                    field.set(t, Double.parseDouble(value));
                } else if (type.equals("class java.lang.Boolean") || type.equals("boolean")) {
                    field.set(t, Boolean.parseBoolean(value));
                } else {
                    field.set(t, value);
                }
            }
        }
        list.add(t);
        return list;
    }

    private String writeCellOfLable(String key) {
        int namedCellIdx = workbook.getNameIndex(key);
        if (namedCellIdx == -1) {
            return "";
        }
        Name aNamedCell = workbook.getNameAt(namedCellIdx);
        AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
        CellReference[] crefs = aref.getAllReferencedCells();
        Row r = sheet.getRow(crefs[0].getRow());
        Cell c = r.getCell(crefs[0].getCol());
        return ExcelUtil.getCellStringValue(c);
    }

    private void initParam(Map<String, Object> param, Class clazz) {
        if (param != null) {
            if (param.get("inFilePath") != null) {
                inFilePath = param.get("inFilePath").toString();
            }
        } else {
            Excel excel = (Excel) clazz.getAnnotation(Excel.class);
            if (excel != null) {
                inFilePath = excel.inFilePath();
            }
        }
    }
}
