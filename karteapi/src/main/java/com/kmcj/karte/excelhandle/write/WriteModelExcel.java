package com.kmcj.karte.excelhandle.write;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

import com.kmcj.karte.excelhandle.annotation.Excel;
import com.kmcj.karte.excelhandle.annotation.Ingroe;
import com.kmcj.karte.excelhandle.util.ExcelUtil;
import com.kmcj.karte.excelhandle.util.MyStringUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteModelExcel<T> implements WriteExcel<T> {

    private Workbook workbook;
    private Sheet sheet;
    private String inFilePath;
    private String outFilePath;

    @Override
    public boolean write(Map<String, Object> param, List<T> list)
            throws IOException, IllegalArgumentException, IllegalAccessException {
        initParam(param, list.get(0).getClass());
        workbook = ExcelUtil.getWorkbookTypeByFile(inFilePath);
        for (T t : list) {
            Class<? extends Object> clazz = t.getClass();
            Excel excel = clazz.getAnnotation(Excel.class);
            com.kmcj.karte.excelhandle.annotation.Sheet s = (com.kmcj.karte.excelhandle.annotation.Sheet) clazz.getAnnotation(com.kmcj.karte.excelhandle.annotation.Sheet.class);
            int sheetNum = 0;
            if (s != null) {
                sheetNum = s.sheetNum() - 1;
            }
            sheet = workbook.getSheetAt(sheetNum);
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Ingroe ingroe = field.getAnnotation(Ingroe.class);
                if (ingroe != null) {
                    continue;
                }
                com.kmcj.karte.excelhandle.annotation.Cell cl = field.getAnnotation(com.kmcj.karte.excelhandle.annotation.Cell.class);
//                if (cl == null) {
//                    writeCellOfLable(field.getName(), field.get(t).toString(), excel);
//                } else if (MyStringUtils.isNotBlank(cl.name())) {
//                    writeCellOfLable(cl.name(), field.get(t).toString(), excel);
//                } else {
//                    Cell cell = sheet.getRow(cl.rowNum() - 1).getCell(ExcelUtil.cellTNumByCa(cl) - 1);
//                    if (cell == null) {
//                        cell = sheet.getRow(cl.rowNum() - 1).createCell(ExcelUtil.cellTNumByCa(cl) - 1);
//                    }
//                    setCellValue(field.get(t).toString(), cell);
//                    if (excel != null && excel.autoHeight()) {
//                        ExcelUtil.calcAndSetRowHeigt(sheet.getRow(cl.rowNum() - 1), 1.1);
//                    }
//                }
                if (cl == null) {
                    writeCellOfLable(field.getName(), getFiledValue(field.get(t), cl), excel);
                } else if (MyStringUtils.isNotBlank(cl.name())) {
                    writeCellOfLable(cl.name(), getFiledValue(field.get(t), cl), excel);
                } else {
                    Cell cell = sheet.getRow(cl.rowNum() - 1).getCell(ExcelUtil.cellTNumByCa(cl) - 1);
                    if (cell == null) {
                        cell = sheet.getRow(cl.rowNum() - 1).createCell(ExcelUtil.cellTNumByCa(cl) - 1);
                    }
                    setCellValue(getFiledValue(field.get(t), cl), cell);
                    if (excel != null && excel.autoHeight()) {
                        ExcelUtil.calcAndSetRowHeigt(sheet.getRow(cl.rowNum() - 1), 1.1);
                    }
                }
            }
        }
        ExcelUtil.workbookToFile(workbook, outFilePath);
        return true;
    }
    
    /**
     * date format
     * @param obj
     * @param cl
     * @return 
     */
    private String getFiledValue(Object obj, com.kmcj.karte.excelhandle.annotation.Cell cl) {
        if (obj == null) {
            return "";
        } else if (obj instanceof Date) {
            Date date = (Date) obj;
            DateFormat format = new SimpleDateFormat(cl.dateFormat());
            return format.format(date);
        } else {
            return obj.toString();
        }
    }

    private void initParam(Map<String, Object> param,
            Class<? extends Object> clazz) {
        Excel excel = clazz.getAnnotation(Excel.class);
        if (excel != null) {
            inFilePath = excel.inFilePath();
            outFilePath = excel.outFilePath();
        }
        if (param != null) {
            if (param.get("inFilePath") != null) {
                inFilePath = param.get("inFilePath").toString();
            }
            if (param.get("outFilePath") != null) {
                outFilePath = param.get("outFilePath").toString();
            }
        }
    }

    //处理一般的excel名称
    public void writeCellOfLable(String key, String value, Excel excel) {
        int namedCellIdx = workbook.getNameIndex(key);
        if (namedCellIdx == -1) {
            return;
        }
        Name aNamedCell = workbook.getNameAt(namedCellIdx);
        AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
        CellReference[] crefs = aref.getAllReferencedCells();
        Row r = sheet.getRow(crefs[0].getRow());
        Cell c = r.getCell(crefs[0].getCol());
        if (c == null) {
            c = r.createCell(crefs[0].getCol());
        }
        setCellValue(value, c);
        if (excel != null && excel.autoHeight()) {
            ExcelUtil.calcAndSetRowHeigt(r, 1.2);
        }
    }

    private void setCellValue(String value, Cell c) {
        if (MyStringUtils.isNotBlank(value) && MyStringUtils.isDouble(value)) {
            c.setCellValue(Double.parseDouble(value));
        } else if (MyStringUtils.isNotBlank(value) && MyStringUtils.isNumeric(value)) {
            c.setCellValue(Integer.parseInt(value));
        } else {
            c.setCellValue(value);
        }
    }
}
