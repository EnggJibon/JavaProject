package com.kmcj.karte.excelhandle.write.util;

import com.kmcj.karte.constants.CommonConstants;
import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.*;

import com.kmcj.karte.excelhandle.annotation.Excel;
import com.kmcj.karte.excelhandle.annotation.Select;
import com.kmcj.karte.excelhandle.util.ExcelUtil;
import com.kmcj.karte.excelhandle.util.MyStringUtils;
import com.kmcj.karte.excelhandle.write.style.MyCellStyle;
import java.io.IOException;
import java.util.Map;
import org.apache.poi.ss.util.CellReference;

public class WriteListExcelHelp {

    private Workbook workbook;
    private int sequence = 1;
    private CellStyle headCellStyle;
    private CellStyle commonCellStyle;
    private CellStyle textCellStyle;
    private CellStyle numberCellStyle;
    private CellStyle dateCellStyle;

    public WriteListExcelHelp(MyCellStyle cellStyle) {
        headCellStyle = cellStyle.createCommonHeaderCellStyler();
        commonCellStyle = cellStyle.createCommonCellStyle();
        textCellStyle = cellStyle.createCommonTextCellStyle();
        numberCellStyle = cellStyle.createCommonNumberCellStyle();
        dateCellStyle = cellStyle.createCommonDateCellStyle();
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Row generateHeader(Sheet sheet, int beginNum, Class clazz) {
        Excel excel = (Excel) clazz.getAnnotation(Excel.class);
        Row row = null;
        if (excel != null) {
            String dataHeader = excel.dataHeader();
            if (MyStringUtils.isNotBlank(dataHeader)) {
                if (excel.createRowWay().equals("add")) {
                    row = sheet.createRow(beginNum - 1);
                } else {
                    sheet.shiftRows(beginNum - 1, sheet.getLastRowNum(), 1, true, false);
                    row = sheet.createRow(beginNum - 1);
                }
                if (excel.autoHeight()) {
                    row.setHeight((short) 400);
                }
                boolean isNeedSequence = excel.isNeedSequence();
                if (isNeedSequence) {
                    Cell cell = row.createCell(0);
                    sheet.setColumnWidth(0, 1680);
                    cell.setCellValue("序号");
                    cell.setCellStyle(headCellStyle);
                }
                int cdn = 1;
                for (String cellData : dataHeader.split(",")) {
                    String[] cds = cellData.split(":");
                    if (MyStringUtils.isNotBlank(cds[0])) {
                        Cell cell = null;
                        if (cds.length == 1) {
                            if (!isNeedSequence) {
                                cdn = cdn - 1;
                            }
                            cell = row.createCell(cdn);
                        } else {
                            int cn = Integer.parseInt(cds[1]);
                            if (!isNeedSequence) {
                                cn = cn - 1;
                            }
                            cell = row.createCell(cn);
                            if (cds.length == 3) {
                                sheet.setColumnWidth(Integer.parseInt(cds[1]), Integer.parseInt(cds[2]));
                            }
                        }
                        cell.setCellValue(cds[0]);
                        cell.setCellStyle(headCellStyle);
                    }
                    cdn++;
                }
            }
        }
        if (excel != null && excel.autoHeight()) {
            ExcelUtil.calcAndSetRowHeigt(row, 1.3);
        }
        return row;
    }

    public Row generateBody(Sheet sheet, int rowNum, Object t, String outFilePath, Map<Integer, Integer> map, int initBeginNum) throws IllegalArgumentException, IllegalAccessException, IOException {
        Class<? extends Object> clazz = t.getClass();
        Excel excel = clazz.getAnnotation(Excel.class);
        Row row = null;
        if (excel == null || excel.createRowWay().equals("add")) {
            row = sheet.createRow(rowNum - 1);
        } else {
            sheet.shiftRows(rowNum - 1, sheet.getLastRowNum(), 1, true, false);
            row = sheet.createRow(rowNum - 1);
        }
        if (excel != null && excel.autoHeight()) {
            row.setHeight((short) 400);
        }
        if (excel == null || excel.isNeedSequence()) {
            Cell cell = row.createCell(0);
            cell.setCellValue(sequence);
            cell.setCellStyle(commonCellStyle);
        }
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            com.kmcj.karte.excelhandle.annotation.Cell ca = field.getAnnotation(com.kmcj.karte.excelhandle.annotation.Cell.class);
            if (ca == null) {
                continue;
            }
            int colNumber = 0;
            if (MyStringUtils.isNotBlank(ca.name())) {
                workbook = ExcelUtil.getWorkbookTypeByFile(outFilePath);
                int namedCellIdx = workbook.getNameIndex(ca.name());
                Name aNamedCell = workbook.getNameAt(namedCellIdx);
                CellReference cref = new CellReference(aNamedCell.getRefersToFormula());
                colNumber = cref.getCol() + 1;
            } else {
                colNumber = ExcelUtil.cellTNumByCa(ca);
            }
            if (colNumber == 0) {
                continue;
            }
            if (excel != null && !excel.isNeedSequence()) {
                colNumber = colNumber - 1;
            }
            Select select = field.getAnnotation(Select.class);
            if (select != null) {
                WriteExcelUtil.generateSelect(sheet, field.get(t), row.getRowNum(), colNumber);
            } else {
                Cell cell = row.createCell(colNumber);
                Object obj = field.get(t);
                if (obj != null) {
                    String objValue = String.valueOf(obj);
                    Integer newLength = objValue.getBytes().length;
                    if (row.getRowNum() > initBeginNum) {
                        if (newLength < map.get(colNumber)) {
                            newLength = map.get(colNumber);
                        }
                    }
                    try {
                        sheet.setColumnWidth(colNumber, newLength * 2 * 170);
                    } catch (Exception e) {
                        sheet.setColumnWidth(colNumber, 255 * 256);
                    }
                    map.put(colNumber, newLength);
                    if (obj instanceof String) {
                        String value = obj + "";
                        setCellValueAndCellStyle(cell, value, textCellStyle);
                    } else if (obj instanceof Double || obj instanceof Float) {
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue((double) obj);
                        cell.setCellStyle(numberCellStyle);
                    } else if (obj instanceof Integer) {
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue((int) obj);
                        cell.setCellStyle(commonCellStyle);
                    } else {
                        setCellValueAndCellStyle(cell, obj + "", commonCellStyle);
                    }
                } else {
                    switch (ca.columnType()) {
                        case CommonConstants.EXCEL_EXPORT_CELL_TYPE_NUMBER:
                            setCellValueAndCellStyle(cell, "", numberCellStyle);
                            break;
                        case CommonConstants.EXCEL_EXPORT_CELL_TYPE_DATE:
                            setCellValueAndCellStyle(cell, "", dateCellStyle);
                            break;
                        default:
                            setCellValueAndCellStyle(cell, "", textCellStyle);
                            break;
                    }
                }
            }
        }
        sequence++;
        if (excel != null && excel.autoHeight()) {
            ExcelUtil.calcAndSetRowHeigt(row, 1.3);
        }
        return row;
    }

    private void setCellValueAndCellStyle(Cell cell, String value, CellStyle cellStyle) {
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

}
