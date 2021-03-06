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
import com.kmcj.karte.excelhandle.annotation.ListExcel;
import com.kmcj.karte.excelhandle.annotation.Select;
import com.kmcj.karte.excelhandle.util.ExcelUtil;
import com.kmcj.karte.excelhandle.util.MyStringUtils;
import com.kmcj.karte.excelhandle.write.util.WriteExcelUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @author Apeng
 */
public class WriteModelExcelList implements WriteExcelList {

    private Workbook workbook;
    private Sheet sheet;
    private String inFilePath;
    private String outFilePath;

    @Override
    public boolean write(Map<String, Object> param, List list)
            throws IOException, IllegalArgumentException, IllegalAccessException {
        initParam(param, list.get(0).getClass());
        workbook = ExcelUtil.getWorkbookTypeByFile(outFilePath);
        int beginNum = 0;
        for (Object t : list) {
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
                ListExcel lea = field.getAnnotation(ListExcel.class);
                if (lea != null) {
                    String beginRowName = lea.beginRowName();
                    if (MyStringUtils.isNotBlank(beginRowName)) {
                        Cell cell = getCellByLable(beginRowName);
                        if(cell != null) {
                            beginNum = cell.getRow().getRowNum() + 1;
                        } else {
                            throw new IOException("The name of the input cell does not exist！");
                        }
                    }
                    Object obj = field.get(t);
                    if (obj instanceof List) {
                        param.put("workbook", workbook);
                        param.put("isConvertWorkbook", false);
                        param.put("sheet", sheet);
                        if (beginNum != 0) {
                            param.put("beginRow", beginNum);
                        }
                        List le = (List) obj;
                        WriteListExcel wl = new WriteListExcel();
                        wl.initParam(param, le.get(0).getClass());
                        wl.generateExcel(le,outFilePath);
                    }
                } else {
                    com.kmcj.karte.excelhandle.annotation.Cell cl = field.getAnnotation(com.kmcj.karte.excelhandle.annotation.Cell.class);
                    Select select = field.getAnnotation(Select.class);
                    if (cl == null) {
                        if (select != null) {
                            generateSelect(field.getName(), field.get(t));
                        } else {
                            writeCellOfLable(field.getName(), getFiledValue(field.get(t), cl), excel);
                        }
                    } else if (MyStringUtils.isNotBlank(cl.name())) {
                        if (select != null) {
                            generateSelect(cl.name(), field.get(t));
                        } else {
                            writeCellOfLable(cl.name(), getFiledValue(field.get(t), cl), excel);
                        }
                    } else {
                        if (select != null) {
                            WriteExcelUtil.generateSelect(sheet, field.get(t), cl.rowNum() - 1, ExcelUtil.cellTNumByCa(cl) - 1);
                        } else {
                            Row row = sheet.getRow(cl.rowNum() - 1);
                            if (row == null) {
                                row = sheet.createRow(cl.rowNum() - 1);
                            }
                            Cell cell = row.getCell(ExcelUtil.cellTNumByCa(cl) - 1);
                            if (cell == null) {
                                cell = row.createCell(ExcelUtil.cellTNumByCa(cl) - 1);
                            }
                            setCellValue(getFiledValue(field.get(t), cl), cell);
                        }
                        if (excel != null && excel.autoHeight()) {
                            ExcelUtil.calcAndSetRowHeigt(sheet.getRow(cl.rowNum() - 1), 1.1);
                        }
                    }
                }
            }
        }
        ExcelUtil.workbookToFile(workbook, outFilePath);
        return true;
    }

    private void generateSelect(String name, Object object) {
        Cell cell = getCellByLable(name);
        WriteExcelUtil.generateSelect(sheet, object, cell.getRowIndex(), cell.getColumnIndex());
    }

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
        Cell c = getCellByLable(key);
        setCellValue(value, c);
        if (excel != null && excel.autoHeight()) {
            ExcelUtil.calcAndSetRowHeigt(c.getRow(), 1.2);
        }
    }

    private Cell getCellByLable(String name) {
        int namedCellIdx = workbook.getNameIndex(name);
        if (namedCellIdx == -1) {
            return null;
        }
        Name aNamedCell = workbook.getNameAt(namedCellIdx);
        AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
        CellReference[] crefs = aref.getAllReferencedCells();
        Row r = sheet.getRow(crefs[0].getRow());
        if (r == null) {
            r = sheet.createRow(crefs[0].getRow());
        }
        Cell c = r.getCell(crefs[0].getCol());
        if (c == null) {
            c = r.createCell(crefs[0].getCol());
        }
        return c;
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
