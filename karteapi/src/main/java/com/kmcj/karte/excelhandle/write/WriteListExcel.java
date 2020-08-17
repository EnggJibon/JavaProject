package com.kmcj.karte.excelhandle.write;

import java.io.IOException;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.kmcj.karte.excelhandle.annotation.Excel;
import com.kmcj.karte.excelhandle.util.ExcelUtil;
import com.kmcj.karte.excelhandle.write.style.CommonCellStyle;
import com.kmcj.karte.excelhandle.write.style.MyCellStyle;
import com.kmcj.karte.excelhandle.write.util.WriteListExcelHelp;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteListExcel implements WriteExcelList {

    private MyCellStyle cellStyle;
    private boolean isNeedSequence;
    private Workbook workbook;
    private Sheet sheet;
    private int beginNum = 1;
    private int sheetSize;
    private String outFilePath;
    private String sheetName = "sheet";

    @Override
    public boolean write(Map<String, Object> param, List list) throws IOException, IllegalArgumentException, IllegalAccessException {
        initParam(param, list.get(0).getClass());
        generateExcel(list, outFilePath);
        ExcelUtil.workbookToFile(workbook, outFilePath);
        return true;
    }

    public void generateExcel(List list, String outFilePath) throws IllegalAccessException, IOException {
        if (workbook instanceof HSSFWorkbook && sheetSize == 0 && list.size() > 65535) {
            sheetSize = 65535;
        }
        if (workbook instanceof SXSSFWorkbook && sheetSize == 0 && list.size() > 1048575) {
            sheetSize = 1048575;
        }
        WriteListExcelHelp help = new WriteListExcelHelp(cellStyle);
        Map<Integer, Integer> map = new HashMap();
        int initBeginNum;
        if (sheetSize != 0 && list.size() > sheetSize) {
            double sn = (double) list.size() / sheetSize;
            sn = Math.ceil(sn);
            initBeginNum = beginNum;
            int k = 0;
            for (int i = 1; i <= sn; i++) {
                sheet = workbook.createSheet(sheetName + "-" + i);
                Row row = help.generateHeader(sheet, beginNum, list.get(0).getClass());
                if (row == null) {
                    beginNum = beginNum - 1;
                }
                help.setSequence(1);
                for (int j = 0; j < sheetSize; j++) {
                    if (k == list.size()) {
                        break;
                    }
                    help.generateBody(sheet, ++beginNum, list.get(k), outFilePath, map,initBeginNum);
                    k++;
                }
            }
        } else {
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }
            Row row = help.generateHeader(sheet, beginNum, list.get(0).getClass());
            if (row == null) {
                beginNum = beginNum - 1;
            }
            initBeginNum = beginNum;
            for (Object t : list) {
                help.generateBody(sheet, ++beginNum, t, outFilePath, map,initBeginNum);
            }
        }
    }

    public void initParam(Map<String, Object> param, Class clazz) {
        Excel excel = (Excel) clazz.getAnnotation(Excel.class);
        com.kmcj.karte.excelhandle.annotation.Sheet s = (com.kmcj.karte.excelhandle.annotation.Sheet) clazz.getAnnotation(com.kmcj.karte.excelhandle.annotation.Sheet.class);
        if (excel != null) {
            isNeedSequence = excel.isNeedSequence();
            outFilePath = excel.outFilePath();
            beginNum = excel.beginRow();
        }
        if (s != null) {
            sheetName = s.sheetName();
            sheetSize = s.sheetSize();
        }
        if (param == null) {
            workbook = new HSSFWorkbook();
            cellStyle = new CommonCellStyle(workbook);
        } else {
            if (param.get("workbook") == null) {
                workbook = new HSSFWorkbook();
            } else {
                workbook = (Workbook) param.get("workbook");
                if (param.get("isConvertWorkbook") == null) {
                    if (workbook instanceof XSSFWorkbook) {
                        workbook = new SXSSFWorkbook();
                    }
                }
            }
            if (param.get("myCellStyle") == null) {
                cellStyle = new CommonCellStyle(workbook);
            } else {
                cellStyle = (MyCellStyle) param.get("myCellStyle");
            }
            if (param.get("outFilePath") != null) {
                outFilePath = param.get("outFilePath").toString();
            }
            if (param.get("beginRow") != null) {
                beginNum = Integer.parseInt(param.get("beginRow").toString());
            }
            if (param.get("sheet") != null) {
                sheet = (Sheet) param.get("sheet");
            }
            if (param.get("sheetName") != null) {
                sheetName = String.valueOf(param.get("sheetName"));
            }
        }
    }

}
