/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock.excel;

import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStockList;
import com.kmcj.karte.util.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class OrderNeededExcelService {
    
    @Inject
    MstDictionaryService dictionaryService;
    @Inject
    MstChoiceService choiceService;

    private final static String[] HEADER_DICT_KEYS = {
        "mold_id", "user_department", "mold_part_storage_code", "mst_mold_part_mold_part_code",
        "mold_part_stock_qty", "mold_part_used_stock_qty", "mold_part_order_point", "mold_part_order_unit",
        "mold_part_replaced_datetime", "mold_part_replace_person"
    };
    private final static int[] CHAR_LENGTH = {20, 20, 20, 30, 10, 10, 10, 10, 20, 20};
    private final static String[] CELL_STYLES = {
        "text","text","text","text",null,null,null,null,"yyyy/mm/dd hh:mm","text"
    };
    private final static String[] DICT_KEYS = {
        "mold_part_order_needed_list"
    };
    private HashMap<String, String> dictMap = null;
    private HashMap<Integer, String> departmentChoiceMap = null;
    
    public Workbook createMoldPartStockExcel(MoldPartStockList stockList, LoginUser loginUser) {        
        List<String> dictKeys = new ArrayList();
        dictKeys.addAll(Arrays.asList(HEADER_DICT_KEYS));
        dictKeys.addAll(Arrays.asList(DICT_KEYS));
        dictMap = dictionaryService.getDictionaryHashMap(loginUser.getLangId(), dictKeys);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet();
        workbook.setSheetName(0, dictMap.get("mold_part_order_needed_list"));
        createHeaderRow(sheet1);
        departmentChoiceMap = choiceService.getSeqValueMap(loginUser.getLangId(), "mst_user.department");
        int rowNum = 1;
        CellStyle[] cellStyles = new CellStyle[CELL_STYLES.length];
        DataFormat dataFormat = workbook.createDataFormat();
        for (int i = 0; i < CELL_STYLES.length; i++) {
            if (CELL_STYLES[i] == null) {
                cellStyles[i] = null;
            }
            else {
                CellStyle style = workbook.createCellStyle();
                style.setDataFormat(dataFormat.getFormat(CELL_STYLES[i]));
                cellStyles[i] = style;
            }
        }
        for (MoldPartStock stock: stockList.getMoldPartStocks()) {
            Row row = sheet1.createRow(rowNum);
            createDataRow(row, stock, cellStyles);
            rowNum++;
        }
        return workbook;
    }
    
    private void createHeaderRow(Sheet sheet) {
        Row row = sheet.createRow(0);
        for (int i = 0; i < HEADER_DICT_KEYS.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(dictMap.get(HEADER_DICT_KEYS[i]));
            sheet.setColumnWidth(i, CHAR_LENGTH[i] * 256);
        }
    }
    
    private void createDataRow(Row row, MoldPartStock stock, CellStyle[] cellStyles) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < HEADER_DICT_KEYS.length; i++) {
            Cell cell = row.createCell(i);
            if (cellStyles[i] != null) {
                cell.setCellStyle(cellStyles[i]);
            }
            cells.add(cell);
        }
        int cellNum = 0;
        cells.get(cellNum++).setCellValue(stock.getMold().getMoldId());
        cells.get(cellNum++).setCellValue(departmentChoiceMap.get(stock.getMold().getDepartment()));
        cells.get(cellNum++).setCellValue(stock.getStorageCode());
        cells.get(cellNum++).setCellValue(stock.getMoldPart().getMoldPartCode());
        cells.get(cellNum++).setCellValue(stock.getStock());
        cells.get(cellNum++).setCellValue(stock.getUsedStock());
        cells.get(cellNum++).setCellValue(stock.getOrderPoint());
        cells.get(cellNum++).setCellValue(stock.getOrderUnit());
        if (stock.getMoldMaintenance() != null) {
            cells.get(cellNum++).setCellValue(stock.getMoldMaintenance().getUpdateDate());
            if (stock.getMoldMaintenance().getUpdateUser() != null) {
                cells.get(cellNum++).setCellValue(stock.getMoldMaintenance().getUpdateUser().getUserName());
            }
            else {
                cellNum++;
            }
        }
        else {
            cellNum += 2;
        }
    }
    
    public String getFileName(LoginUser loginUser) {
        String baseName = null;
        if (dictMap != null) {
            baseName = dictMap.get("mold_part_order_needed_list");
        }
        if (baseName == null) {
            baseName = dictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_part_order_needed_list");
        }
        return baseName + "_" + DateFormat.dateToStr(new java.util.Date(), "yyyyMMddHHmmss");
    }
    

}
