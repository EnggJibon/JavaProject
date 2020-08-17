/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock.excel;

import com.kmcj.karte.excelhandle.util.ExcelUtil;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStockList;
import com.kmcj.karte.util.DateFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
//import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class MoldPartStockExcelService {
    
    @Inject
    MstDictionaryService dictionaryService;
    @Inject
    MstChoiceService choiceService;

    private final static String[] HEADER_DICT_KEYS = {
        "mold_id", "user_department", "mold_part_storage_code", "mst_mold_part_mold_part_code",
        "mold_part_stock_qty", "mold_part_used_stock_qty", "unit_price", "used_unit_price",
        "mold_part_stock_value", "mold_part_used_stock_value", "mold_part_order_point", "mold_part_order_unit",
        "mold_part_stock_status", "mold_part_order_job_no"
    };
    private final static int[] CHAR_LENGTH = {20, 20, 20, 30, 10, 10, 10, 10, 10, 10, 10, 10, 15, 50};
    private final static String[] CELL_STYLES = {
        "text","text","text","text",null,null,null,null,null,null,null,null,"text","text"
    };
    private final static String[] DICT_KEYS = {
        "mold_part_stock_status_normal", "mold_part_stock_status_order_needed", "mold_part_stock_status_waiting_delivery", "mold_part_stock_ref"
    };
    private final static String [] IMP_MSG_DICT_KEYS = {
        "row_number", "msg_error_not_null_with_item", "msg_error_over_length", "msg_error_not_isnumber", "error", "error_detail", "mst_error_record_not_found",
        "db_process", "msg_record_added", "msg_data_modified", "msg_record_deleted"
    };
    private HashMap<String, String> dictMap = null;
    private HashMap<Integer, String> departmentChoiceMap = null;
    private HashMap<String, String> impMsgDictMap = null;
    
    public Workbook createMoldPartStockExcel(MoldPartStockList stockList, LoginUser loginUser) {        
        List<String> dictKeys = new ArrayList();
        dictKeys.addAll(Arrays.asList(HEADER_DICT_KEYS));
        dictKeys.addAll(Arrays.asList(DICT_KEYS));
        dictMap = dictionaryService.getDictionaryHashMap(loginUser.getLangId(), dictKeys);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet();
        workbook.setSheetName(0, dictMap.get("mold_part_stock_ref"));
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
        cells.get(cellNum++).setCellValue(stock.getMoldPart().getUnitPrice().doubleValue());
        cells.get(cellNum++).setCellValue(stock.getMoldPart().getUsedUnitPrice().doubleValue());
        cells.get(cellNum++).setCellValue(stock.getStock() * stock.getMoldPart().getUnitPrice().doubleValue());
        cells.get(cellNum++).setCellValue(stock.getUsedStock() * stock.getMoldPart().getUsedUnitPrice().doubleValue());
        cells.get(cellNum++).setCellValue(stock.getOrderPoint());
        cells.get(cellNum++).setCellValue(stock.getOrderUnit());
        cells.get(cellNum++).setCellValue(getStatusText(stock.getStatus()));
        cells.get(cellNum++).setCellValue(stock.getOrderJobNo());
    }
    
    private String getStatusText(int status) {
        switch (status) {
            case MoldPartStock.Status.NORMAL:
                return dictMap.get("mold_part_stock_status_normal");
            case MoldPartStock.Status.ORDER_REQ:
                return dictMap.get("mold_part_stock_status_order_needed");
            case MoldPartStock.Status.DELI_WT:
                return dictMap.get("mold_part_stock_status_waiting_delivery");
            default:
                return null;
        }        
    }

    public String getFileName(LoginUser loginUser) {
        String baseName = null;
        if (dictMap != null) {
            baseName = dictMap.get("mold_part_stock_ref");
        }
        if (baseName == null) {
            baseName = dictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_part_stock_ref");
        }
        return baseName + "_" + DateFormat.dateToStr(new java.util.Date(), "yyyyMMddHHmmss");
    }
    
    public List<MoldPartStockExcelRecord> getMoldPartStockAdjustFromExcel(String filePath) throws IOException, InvalidFormatException, IllegalStateException {
        List<MoldPartStockExcelRecord> list = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        Sheet sheet = workbook.getSheetAt(0);
        //Row headerRow = sheet.getRow(0);
        //Integer.MAX_VALUE
        List<String> headerDictKeys = Arrays.asList(HEADER_DICT_KEYS);
        int moldIdCol = headerDictKeys.indexOf("mold_id");
        int moldPartCodeCol = headerDictKeys.indexOf("mst_mold_part_mold_part_code");
        int storageCodeCol = headerDictKeys.indexOf("mold_part_storage_code");
        int stockCol = headerDictKeys.indexOf("mold_part_stock_qty");
        int usedStockCol = headerDictKeys.indexOf("mold_part_used_stock_qty");
        int orderPointCol = headerDictKeys.indexOf("mold_part_order_point");
        int orderUnitCol = headerDictKeys.indexOf("mold_part_order_unit");
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            Row row = sheet.getRow(i);
            if (row == null) break;
            MoldPartStockExcelRecord record = new MoldPartStockExcelRecord();
            if (row.getCell(moldIdCol) == null) {
                break;
            }
            //record.setMoldId(row.getCell(moldIdCol).getStringCellValue());
            record.setMoldId(ExcelUtil.getCellStringValue(row.getCell(moldIdCol)));
            if (record.getMoldId() == null || record.getMoldId().equals("")) {
                break;
            }
            if (row.getCell(moldPartCodeCol) != null) {
                //record.setMoldPartCode(row.getCell(moldPartCodeCol).getStringCellValue());
                record.setMoldPartCode(ExcelUtil.getCellStringValue(row.getCell(moldPartCodeCol)));
            }
            if (row.getCell(storageCodeCol) != null) {
                //record.setStorageCode(row.getCell(storageCodeCol).getStringCellValue());
                record.setStorageCode(ExcelUtil.getCellStringValue(row.getCell(storageCodeCol)));
            }
            //数値項目はセル属性がNUMERICでないとき、文字列が数値として正しいか評価する。
            if (row.getCell(stockCol) == null) {
                record.setStrStock(""); //セルそのものがNULLのときはブランクを入れておく。
            }
            else {
                if (row.getCell(stockCol).getCellTypeEnum().equals(CellType.NUMERIC)) {
                    record.setStock((int)row.getCell(stockCol).getNumericCellValue());
                }
                else {
                    String sVal = row.getCell(stockCol).getStringCellValue();
                    try {
                        record.setStock(Integer.parseInt(sVal));
                    }
                    catch (NumberFormatException ne) {
                        record.setStrStock(sVal); //数値として正しくなければログに出すため文字列変数に格納
                    }
                }
            }
            if (row.getCell(usedStockCol) == null) {
                record.setStrUsedStock("");
            }
            else {
                if (row.getCell(usedStockCol).getCellTypeEnum().equals(CellType.NUMERIC)) {
                    record.setUsedStock((int)row.getCell(usedStockCol).getNumericCellValue());
                }
                else {
                    String sVal = row.getCell(usedStockCol).getStringCellValue();
                    try {
                        record.setUsedStock(Integer.parseInt(sVal));
                    }
                    catch (NumberFormatException ne) {
                        record.setStrUsedStock(sVal); //数値として正しくなければログに出すため文字列変数に格納
                    }
                }
            }
            if (row.getCell(orderPointCol) == null) {
                record.setStrOrderPoint("");
            }
            else {
                if (row.getCell(orderPointCol).getCellTypeEnum().equals(CellType.NUMERIC)) {
                    record.setOrderPoint((int)row.getCell(orderPointCol).getNumericCellValue());
                }
                else {
                    String sVal = row.getCell(orderPointCol).getStringCellValue();
                    try {
                        record.setOrderPoint(Integer.parseInt(sVal));
                    }
                    catch (NumberFormatException ne) {
                        record.setStrOrderPoint(sVal); //数値として正しくなければログに出すため文字列変数に格納
                    }
                }
            }
            if (row.getCell(orderUnitCol) == null) {
                record.setStrOrderUnit("");
            }
            else {
                if (row.getCell(orderUnitCol).getCellTypeEnum().equals(CellType.NUMERIC)) {
                    record.setOrderUnit((int)row.getCell(orderUnitCol).getNumericCellValue());
                }
                else {
                    String sVal = row.getCell(orderUnitCol).getStringCellValue();
                    try {
                        record.setOrderUnit(Integer.parseInt(sVal));
                    }
                    catch (NumberFormatException ne) {
                        record.setStrOrderUnit(sVal); //数値として正しくなければログに出すため文字列変数に格納
                    }
                }
            }
            list.add(record);
        }
        return list;
    }
    
    public String getMessage(String dictKey, LoginUser loginUser) {
        if (impMsgDictMap == null) {
            List<String> dictKeys = new ArrayList();
            dictKeys.addAll(Arrays.asList(IMP_MSG_DICT_KEYS));
            dictKeys.addAll(Arrays.asList(HEADER_DICT_KEYS));
            impMsgDictMap = dictionaryService.getDictionaryHashMap(loginUser.getLangId(), dictKeys);
        }
        return impMsgDictMap.get(dictKey);
    }

}
