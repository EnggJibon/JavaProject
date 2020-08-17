package com.kmcj.karte.batch.inventory;

import com.kmcj.karte.resources.asset.inventory.excelvo.R0001InventoryRequest;
import org.apache.poi.ss.usermodel.Cell;
import java.io.IOException;
import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Apeng
 */
public class InventoryInfoCreateTemplate {

    public final static String DATE_FORMAT = "yyyy/MM/dd";

    private final static String ASSET_TYPE = "mst_asset.asset_type";

    private final static String ACQUISITION_TYPE = "mst_asset.acquisition_type";

    /**
     * ファイル種類によりインステンスを取得
     *
     * @param filepath
     * @return
     * @throws IOException
     */
    private Workbook getWorkbookTypeByFile(String filepath) {
        Workbook workbook = null;
        File file = new File(filepath);
        BufferedInputStream in = null;

        try {
            if (file.exists()) {
                in = new BufferedInputStream(new FileInputStream(file));
            }

            if (filepath.endsWith("xls")) {
                workbook = new HSSFWorkbook(in);
            } else if (filepath.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(in);
            }
        } catch (Exception e) {
            Logger.getLogger(InventoryInfoCreateTemplate.class.getName()).log(Level.SEVERE, null, e);
        }
        return workbook;
    }

    /**
     * 管理先ごとにテンプレート書き込み
     *
     * @param outputPath
     * @param moldList
     * @param machineList
     * @param choiceMap
     * @throws IOException
     */
    public void write(String outputPath, List moldList, List machineList, Map<String, String> choiceMap) throws IOException {

        // 管理先ごとにループし依頼票ファイルを作成
        // ワークブックを読み込みます。
        Workbook workbook = getWorkbookTypeByFile(outputPath);
        if (workbook == null) {
            return;
        }

        if (moldList != null && moldList.size() > 0) {
            saveMoldListToExcelBook(workbook, moldList, choiceMap);
        } else {
            workbook.setSheetHidden(0, 2);
            //workbook.setSheetHidden(0, true);
        }

        if (machineList != null && machineList.size() > 0) {
            saveMachineListToExcelBook(workbook, machineList, choiceMap);
        } else {
            workbook.setSheetHidden(1, 2);
            //workbook.setSheetHidden(1, true);
        }

        //ワークブックを読み込みます。
        FileOutputStream outputStream = null;
        try {

            outputStream = new FileOutputStream(outputPath);

            workbook.write(outputStream);

            workbook.close();

            outputStream.close();

        } catch (Exception e) {
            Logger.getLogger(InventoryInfoCreateTemplate.class.getName()).log(Level.SEVERE, null, e);
            workbook.close();
            workbook = null;

            if (outputStream != null) {
                outputStream.close();
                outputStream = null;

            }

        }
    }

    /**
     *
     * @param workbook
     * @param moldList
     * @param choiceMap
     */
    private void saveMoldListToExcelBook(Workbook workbook, List moldList, Map<String, String> choiceMap) {

        // シートを読み込みます。
        Sheet sheet = workbook.getSheetAt(0);
        // 管理先名
        Name name = workbook.getName("MOLD_MGMT_COMPANY_NAME");
        CellReference ref = new CellReference(name.getRefersToFormula());
        Row row = sheet.getRow(ref.getRow());
        //sheet.autoSizeColumn((int) ref.getCol());
        Cell cell = row.getCell(ref.getCol());
        Object[] r0001InventoryRequestVo1 = (Object[]) moldList.get(0);
        cell.setCellValue(getStringValue(r0001InventoryRequestVo1[13]));

        // 管理先Code
        name = workbook.getName("MOLD_MGMT_COMPANY_CODE");
        ref = new CellReference(name.getRefersToFormula());
        row = sheet.getRow(ref.getRow());
        //sheet.autoSizeColumn((int) ref.getCol());
        cell = row.getCell(ref.getCol());
        cell.setCellValue(getStringValue(r0001InventoryRequestVo1[14]));

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String outDate = sdf.format(new Date());
        // 回答期限
        name = workbook.getName("MOLD_DUE_DATE");
        ref = new CellReference(name.getRefersToFormula());
        row = sheet.getRow(ref.getRow());
        //sheet.autoSizeColumn((int) ref.getCol());
        cell = row.getCell(ref.getCol());
        cell.setCellValue(outDate);

        // 発行日
        name = workbook.getName("MOLD_REQUESTED_DATE");
        ref = new CellReference(name.getRefersToFormula());
        row = sheet.getRow(ref.getRow());
        //sheet.autoSizeColumn((int) ref.getCol());
        cell = row.getCell(ref.getCol());
        cell.setCellValue(outDate);

        // 品目コード
        name = workbook.getName("MOLD_ITEM_CODE");
        CellReference refITEM_CODE = new CellReference(name.getRefersToFormula());

        // 資産番号
        name = workbook.getName("MOLD_ASSET_NO");
        CellReference refASSET_NO = new CellReference(name.getRefersToFormula());

        // 補助番号
        name = workbook.getName("MOLD_BRANCH_NO");
        CellReference refBRANCH_NO = new CellReference(name.getRefersToFormula());

        // 名称
        name = workbook.getName("MOLD_ITEM_NAME");
        CellReference refITEM_NAME = new CellReference(name.getRefersToFormula());

        // 種類
        name = workbook.getName("MOLD_ASSET_TYPE");
        CellReference refASSET_TYPE = new CellReference(name.getRefersToFormula());

        // SupplierCode
        name = workbook.getName("MOLD_VENDOR_CODE");
        CellReference refVENDOR_CODE = new CellReference(name.getRefersToFormula());

        // 取得区分
        name = workbook.getName("MOLD_ACQUISITION_TYPE");
        CellReference refACQUISITION_TYPE = new CellReference(name.getRefersToFormula());

        //取得金額
        name = workbook.getName("MOLD_ACQUISITION_AMOUNT");
        CellReference refACQUISITION_AMOUNT = new CellReference(name.getRefersToFormula());

        // 取得年月
        name = workbook.getName("MOLD_ACQUISITION_YYYYMM");
        CellReference refACQUISITION_YYYYMM = new CellReference(name.getRefersToFormula());

        //型数
        name = workbook.getName("MOLD_COUNT");
        CellReference refMOLD_COUNT = new CellReference(name.getRefersToFormula());

        // 金型所在先
        name = workbook.getName("MOLD_MGMT_LOCATION_NAME");
        CellReference refMGMT_LOCATION_NAME = new CellReference(name.getRefersToFormula());

        // 共通情報
        name = workbook.getName("MOLD_COMMON_INFORMATION");
        CellReference refCOMMON_INFORMATION = new CellReference(name.getRefersToFormula());

        // 棚卸結果
        name = workbook.getName("MOLD_INVENTORY_RESULT_YES");// 有
        CellReference refMOLD_YES = new CellReference(name.getRefersToFormula());
        name = workbook.getName("MOLD_INVENTORY_RESULT_NO");// 無
        CellReference refMOLD_NO = new CellReference(name.getRefersToFormula());
        // 金型所在先変更が有る場合
        name = workbook.getName("MOLD_CHANGED_LOCATION_NAME");// 変更された場所名
        CellReference refMOLD_CHANGED_LOCATION_NAME = new CellReference(name.getRefersToFormula());
        name = workbook.getName("MOLD_CHANGED_ADDRESS");//住所
        CellReference refMOLD_CHANGED_ADDRESS = new CellReference(name.getRefersToFormula());

        int startRowNum = refMGMT_LOCATION_NAME.getRow();

        // 一行を挿入する
        boolean rowAdded = false;
        if (moldList.size() > 1) {
            rowAdded = true;
            sheet.shiftRows(startRowNum, sheet.getLastRowNum(), moldList.size() - 1, true, true);
        }

        /**
         * 画線を描画する用
         */
        CellStyle cellStyle = workbook.createCellStyle();
        if (rowAdded) {
            cellStyle.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyle.setBorderBottom(BorderStyle.THIN); //下枠
        }
        cellStyle.setBorderLeft(BorderStyle.THIN);//左枠
        cellStyle.setBorderRight(BorderStyle.THIN);//右枠

        CellStyle lastRowCellStyle = workbook.createCellStyle();
        lastRowCellStyle.setBorderBottom(BorderStyle.THIN); //下枠
        lastRowCellStyle.setBorderLeft(BorderStyle.THIN);//左枠
        lastRowCellStyle.setBorderRight(BorderStyle.THIN);//右枠

        /**
         * 棚卸結果 有	無 の画線を描画する用
         */
        CellStyle cellStyleMoldYesBorderColor = workbook.createCellStyle();
        cellStyleMoldYesBorderColor.setBorderLeft(BorderStyle.MEDIUM);//左枠
        cellStyleMoldYesBorderColor.setBorderRight(BorderStyle.THIN);//右枠
        if (rowAdded) {
            cellStyleMoldYesBorderColor.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyleMoldYesBorderColor.setBorderBottom(BorderStyle.THIN); //下枠
        }
        cellStyleMoldYesBorderColor.setLeftBorderColor(IndexedColors.RED.getIndex());//左边框为红色 

        CellStyle lastRowCellStyleMoldYesBorderColor = workbook.createCellStyle();
        lastRowCellStyleMoldYesBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        lastRowCellStyleMoldYesBorderColor.setBottomBorderColor(IndexedColors.RED.getIndex());
        lastRowCellStyleMoldYesBorderColor.setBorderLeft(BorderStyle.MEDIUM);//左枠
        lastRowCellStyleMoldYesBorderColor.setLeftBorderColor(IndexedColors.RED.getIndex());//左边框为红色 
        lastRowCellStyleMoldYesBorderColor.setBorderRight(BorderStyle.THIN);//右枠

        CellStyle cellStyleMoldNoBorderColor = workbook.createCellStyle();
        cellStyleMoldNoBorderColor.setBorderRight(BorderStyle.MEDIUM);//右枠
        if (rowAdded) {
            cellStyleMoldNoBorderColor.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyleMoldNoBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        }
        cellStyleMoldNoBorderColor.setRightBorderColor(IndexedColors.RED.getIndex());//右边框为红色 

        CellStyle lastRowCellStyleMoldNoBorderColor = workbook.createCellStyle();
        lastRowCellStyleMoldNoBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        lastRowCellStyleMoldNoBorderColor.setBottomBorderColor(IndexedColors.RED.getIndex());
        lastRowCellStyleMoldNoBorderColor.setBorderRight(BorderStyle.MEDIUM);//左枠
        lastRowCellStyleMoldNoBorderColor.setRightBorderColor(IndexedColors.RED.getIndex());//左边框为红色 

        /**
         * 金型所在先変更が有る場合 変更された場所名　住所 の画線を描画する用
         */
        CellStyle cellStyleLocationNameBorderColor = workbook.createCellStyle();
        cellStyleLocationNameBorderColor.setBorderLeft(BorderStyle.MEDIUM);//左枠
        cellStyleLocationNameBorderColor.setBorderRight(BorderStyle.DOTTED);//右枠
        if (rowAdded) {
            cellStyleLocationNameBorderColor.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyleLocationNameBorderColor.setBorderBottom(BorderStyle.THIN); //下枠
        }
        cellStyleLocationNameBorderColor.setLeftBorderColor(IndexedColors.RED.getIndex());//左边框为红色 

        CellStyle lastRowCellStyleLocationNameBorderColor = workbook.createCellStyle();
        lastRowCellStyleLocationNameBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        lastRowCellStyleLocationNameBorderColor.setBottomBorderColor(IndexedColors.RED.getIndex());
        lastRowCellStyleLocationNameBorderColor.setBorderLeft(BorderStyle.MEDIUM);//左枠
        lastRowCellStyleLocationNameBorderColor.setLeftBorderColor(IndexedColors.RED.getIndex());//左边框为红色 
        lastRowCellStyleLocationNameBorderColor.setBorderRight(BorderStyle.DOTTED);//右枠

        CellStyle cellStyleLocationAddressBorderColor = workbook.createCellStyle();
        cellStyleLocationAddressBorderColor.setBorderRight(BorderStyle.MEDIUM);//右枠
        if (rowAdded) {
            cellStyleLocationAddressBorderColor.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyleLocationAddressBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        }
        cellStyleLocationAddressBorderColor.setRightBorderColor(IndexedColors.RED.getIndex());//右边框为红色 

        CellStyle lastRowCellStyleLocationAddressBorderColor = workbook.createCellStyle();
        lastRowCellStyleLocationAddressBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        lastRowCellStyleLocationAddressBorderColor.setBottomBorderColor(IndexedColors.RED.getIndex());
        lastRowCellStyleLocationAddressBorderColor.setBorderRight(BorderStyle.MEDIUM);//左枠
        lastRowCellStyleLocationAddressBorderColor.setRightBorderColor(IndexedColors.RED.getIndex());//左边框为红色 

        int count = moldList.size() - 1;
        for (int rowNum = 0; rowNum < moldList.size(); rowNum++) {

            Object[] r0001InventoryRequestVo = (Object[]) moldList.get(rowNum);

            int index = 0;
            row = sheet.createRow(startRowNum + rowNum);
            //  品目コード 0
            cell = row.createCell((int) refITEM_CODE.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }

            // 資産番号 1
            index++;
            cell = row.createCell((int) refASSET_NO.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 補助番号 2
            index++;
            cell = row.createCell((int) refBRANCH_NO.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            //名称 3
            index++;
            cell = row.createCell((int) refITEM_NAME.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 種類 4
            index++;
            cell = row.createCell((int) refASSET_TYPE.getCol());
            if ("0".equals(getStringValue(r0001InventoryRequestVo[index]))) {
                cell.setCellValue("");
            } else {
                String assetType = choiceMap.get(ASSET_TYPE + getStringValue(r0001InventoryRequestVo[index]));
                cell.setCellValue(assetType);
            }
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // SupplierCode 5
            index++;
            cell = row.createCell((int) refVENDOR_CODE.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 取得区分 6
            index++;
            cell = row.createCell((int) refACQUISITION_TYPE.getCol());
            if ("0".equals(getStringValue(r0001InventoryRequestVo[index]))) {
                cell.setCellValue("");
            } else {
                String type = choiceMap.get(ACQUISITION_TYPE + getStringValue(r0001InventoryRequestVo[index]));
                cell.setCellValue(type);
            }
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 取得金額 7
            index++;
            cell = row.createCell((int) refACQUISITION_AMOUNT.getCol());
            int intItem = Integer.parseInt(getStringValue(r0001InventoryRequestVo[12]));
            String aAmount = String.valueOf(new BigDecimal(getStringValue(r0001InventoryRequestVo[index])).setScale(intItem, BigDecimal.ROUND_DOWN));
            cell.setCellValue(aAmount);
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 取得年月 8
            index++;
            cell = row.createCell((int) refACQUISITION_YYYYMM.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 型数 9
            index++;
            cell = row.createCell((int) refMOLD_COUNT.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }

            cell = row.createCell((int) refMOLD_YES.getCol());
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyleMoldYesBorderColor);
            } else {
                cell.setCellStyle(cellStyleMoldYesBorderColor);
            }

            cell = row.createCell((int) refMOLD_NO.getCol());
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyleMoldNoBorderColor);
            } else {
                cell.setCellStyle(cellStyleMoldNoBorderColor);
            }

            // 金型所在先 10
            index++;
            cell = row.createCell((int) refMGMT_LOCATION_NAME.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }

            cell = row.createCell((int) refMOLD_CHANGED_LOCATION_NAME.getCol());
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyleLocationNameBorderColor);
            } else {
                cell.setCellStyle(cellStyleLocationNameBorderColor);
            }

            cell = row.createCell((int) refMOLD_CHANGED_ADDRESS.getCol());
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyleLocationAddressBorderColor);
            } else {
                cell.setCellStyle(cellStyleLocationAddressBorderColor);
            }

            // 共通情報 11
            index++;
            cell = row.createCell((int) refCOMMON_INFORMATION.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
        }
//
//        // 列の内容により幅を自動調整
        sheet.autoSizeColumn((int) refITEM_CODE.getCol());
//        sheet.autoSizeColumn((int) refASSET_NO.getCol());
//        sheet.autoSizeColumn((int) refBRANCH_NO.getCol());
//        sheet.autoSizeColumn((int) refITEM_NAME.getCol());
//        sheet.autoSizeColumn((int) refASSET_TYPE.getCol());
//        sheet.autoSizeColumn((int) refVENDOR_CODE.getCol());
//        sheet.autoSizeColumn((int) refACQUISITION_TYPE.getCol());
//        sheet.autoSizeColumn((int) refACQUISITION_AMOUNT.getCol());
//        sheet.autoSizeColumn((int) refACQUISITION_YYYYMM.getCol());
//        sheet.autoSizeColumn((int) refMOLD_COUNT.getCol());
//        sheet.autoSizeColumn((int) refMGMT_LOCATION_NAME.getCol());
//        sheet.autoSizeColumn((int) refCOMMON_INFORMATION.getCol());

    }

    /**
     *
     * @param workbook
     * @param machineList
     * @param choiceMap
     */
    private void saveMachineListToExcelBook(Workbook workbook, List machineList, Map<String, String> choiceMap) {
        // シートを読み込みます。
        Sheet sheet = workbook.getSheetAt(1);
        // 管理先名
        Name name = workbook.getName("MACHINE_MGMT_COMPANY_NAME");
        CellReference ref = new CellReference(name.getRefersToFormula());
        Row row = sheet.getRow(ref.getRow());
        //sheet.autoSizeColumn((int) ref.getCol());
        Cell cell = row.getCell(ref.getCol());
        Object[] r0001InventoryRequestVo1 = (Object[]) machineList.get(0);
        cell.setCellValue(getStringValue(r0001InventoryRequestVo1[11]));

        // 管理先Code
        name = workbook.getName("MACHINE_MGMT_COMPANY_CODE");
        ref = new CellReference(name.getRefersToFormula());
        row = sheet.getRow(ref.getRow());
        //sheet.autoSizeColumn((int) ref.getCol());
        cell = row.getCell(ref.getCol());
        cell.setCellValue(getStringValue(r0001InventoryRequestVo1[12]));

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String outDate = sdf.format(new Date());
        // 回答期限
        name = workbook.getName("MACHINE_DUE_DATE");
        ref = new CellReference(name.getRefersToFormula());
        row = sheet.getRow(ref.getRow());
        //sheet.autoSizeColumn((int) ref.getCol());
        cell = row.getCell(ref.getCol());
        cell.setCellValue(outDate);

        // 発行日
        name = workbook.getName("MACHINE_REQUESTED_DATE");
        ref = new CellReference(name.getRefersToFormula());
        row = sheet.getRow(ref.getRow());
        //sheet.autoSizeColumn((int) ref.getCol());
        cell = row.getCell(ref.getCol());
        cell.setCellValue(outDate);

        // 品目コード
        name = workbook.getName("MACHINE_ITEM_CODE");
        CellReference refITEM_CODE = new CellReference(name.getRefersToFormula());

        // 資産番号
        name = workbook.getName("MACHINE_ASSET_NO");
        CellReference refASSET_NO = new CellReference(name.getRefersToFormula());

        // 補助番号
        name = workbook.getName("MACHINE_BRANCH_NO");
        CellReference refBRANCH_NO = new CellReference(name.getRefersToFormula());

        // 名称
        name = workbook.getName("MACHINE_ITEM_NAME");
        CellReference refITEM_NAME = new CellReference(name.getRefersToFormula());

        // 種類
        name = workbook.getName("MACHINE_ASSET_TYPE");
        CellReference refASSET_TYPE = new CellReference(name.getRefersToFormula());

        // 取得区分
        name = workbook.getName("MACHINE_ACQUISITION_TYPE");
        CellReference refACQUISITION_TYPE = new CellReference(name.getRefersToFormula());

        //取得金額
        name = workbook.getName("MACHINE_ACQUISITION_AMOUNT");
        CellReference refACQUISITION_AMOUNT = new CellReference(name.getRefersToFormula());

        // 取得年月
        name = workbook.getName("MACHINE_ACQUISITION_YYYYMM");
        CellReference refACQUISITION_YYYYMM = new CellReference(name.getRefersToFormula());

        // 金型所在先
        name = workbook.getName("MACHINE_MGMT_LOCATION_NAME");
        CellReference refMGMT_LOCATION_NAME = new CellReference(name.getRefersToFormula());

        // 共通情報
        name = workbook.getName("MACHINE_COMMON_INFORMATION");
        CellReference refCOMMON_INFORMATION = new CellReference(name.getRefersToFormula());

        // 棚卸結果　有
        name = workbook.getName("MACHINE_INVENTORY_RESULT_YES");
        CellReference refMACHINE_YES = new CellReference(name.getRefersToFormula());
        // 棚卸結果　無
        name = workbook.getName("MACHINE_INVENTORY_RESULT_NO");
        CellReference refMACHINE_NO = new CellReference(name.getRefersToFormula());
        // 稼働状況       
        name = workbook.getName("MACHINE_WORK_STAUTS");
        CellReference refMACHINE_WORK_STAUTS = new CellReference(name.getRefersToFormula());

        // 変更された場所名
        name = workbook.getName("MACHINE_CHANGED_LOCATION_NAME");
        CellReference refMACHINE_CHANGED_LOCATION_NAME = new CellReference(name.getRefersToFormula());
        // 住所
        name = workbook.getName("MACHINE_CHANGED_ADDRESS");
        CellReference refMACHINE_CHANGED_ADDRESS = new CellReference(name.getRefersToFormula());

        int startRowNum = refMGMT_LOCATION_NAME.getRow();

        // 一行を挿入する
        boolean rowAdded = false;
        if (machineList.size() > 1) {
            rowAdded = true;
            sheet.shiftRows(startRowNum, sheet.getLastRowNum(), machineList.size() - 1, true, true);
        }

        /**
         * 画線を描画する用
         */
        CellStyle cellStyle = workbook.createCellStyle();
        if (rowAdded) {
            cellStyle.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyle.setBorderBottom(BorderStyle.THIN); //下枠
        }
        cellStyle.setBorderLeft(BorderStyle.THIN);//左枠
        cellStyle.setBorderRight(BorderStyle.THIN);//右枠

        CellStyle lastRowCellStyle = workbook.createCellStyle();
        lastRowCellStyle.setBorderBottom(BorderStyle.THIN); //下枠
        lastRowCellStyle.setBorderLeft(BorderStyle.THIN);//左枠
        lastRowCellStyle.setBorderRight(BorderStyle.THIN);//右枠

        /**
         * 棚卸結果 有	無 の画線を描画する用
         */
        CellStyle cellStyleMachineYesBorderColor = workbook.createCellStyle();
        cellStyleMachineYesBorderColor.setBorderLeft(BorderStyle.MEDIUM);//左枠
        cellStyleMachineYesBorderColor.setBorderRight(BorderStyle.THIN);//右枠
        if (rowAdded) {
            cellStyleMachineYesBorderColor.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyleMachineYesBorderColor.setBorderBottom(BorderStyle.THIN); //下枠
        }
        cellStyleMachineYesBorderColor.setLeftBorderColor(IndexedColors.RED.getIndex());//左边框为红色 

        CellStyle lastRowCellStyleMachineYesBorderColor = workbook.createCellStyle();
        lastRowCellStyleMachineYesBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        lastRowCellStyleMachineYesBorderColor.setBottomBorderColor(IndexedColors.RED.getIndex());
        lastRowCellStyleMachineYesBorderColor.setBorderLeft(BorderStyle.MEDIUM);//左枠
        lastRowCellStyleMachineYesBorderColor.setLeftBorderColor(IndexedColors.RED.getIndex());//左边框为红色 
        lastRowCellStyleMachineYesBorderColor.setBorderRight(BorderStyle.THIN);//右枠

        CellStyle cellStyleMachineNoBorderColor = workbook.createCellStyle();
        cellStyleMachineNoBorderColor.setBorderLeft(BorderStyle.THIN);//左枠
        cellStyleMachineNoBorderColor.setBorderRight(BorderStyle.THIN);//右枠
        if (rowAdded) {
            cellStyleMachineNoBorderColor.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyleMachineNoBorderColor.setBorderBottom(BorderStyle.THIN); //下枠
        }

        CellStyle lastRowCellStyleMachineNoBorderColor = workbook.createCellStyle();
        lastRowCellStyleMachineNoBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        lastRowCellStyleMachineNoBorderColor.setBottomBorderColor(IndexedColors.RED.getIndex());
        lastRowCellStyleMachineNoBorderColor.setBorderLeft(BorderStyle.THIN);//左枠
        lastRowCellStyleMachineNoBorderColor.setBorderRight(BorderStyle.THIN);//右枠

        CellStyle cellStyleWorkStatusBorderColor = workbook.createCellStyle();
        cellStyleWorkStatusBorderColor.setBorderRight(BorderStyle.MEDIUM);//右枠
        if (rowAdded) {
            cellStyleWorkStatusBorderColor.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyleWorkStatusBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        }
        cellStyleWorkStatusBorderColor.setRightBorderColor(IndexedColors.RED.getIndex());//右边框为红色 

        CellStyle lastRowCellStyleWorkStatusBorderColor = workbook.createCellStyle();
        lastRowCellStyleWorkStatusBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        lastRowCellStyleWorkStatusBorderColor.setBottomBorderColor(IndexedColors.RED.getIndex());
        lastRowCellStyleWorkStatusBorderColor.setBorderRight(BorderStyle.MEDIUM);//左枠
        lastRowCellStyleWorkStatusBorderColor.setRightBorderColor(IndexedColors.RED.getIndex());//左边框为红色 

        /**
         * 設備所在先変更が有る場合 変更された場所名　住所 の画線を描画する用
         */
        CellStyle cellStyleLocationNameBorderColor = workbook.createCellStyle();
        cellStyleLocationNameBorderColor.setBorderLeft(BorderStyle.MEDIUM);//左枠
        cellStyleLocationNameBorderColor.setBorderRight(BorderStyle.DOTTED);//右枠
        if (rowAdded) {
            cellStyleLocationNameBorderColor.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyleLocationNameBorderColor.setBorderBottom(BorderStyle.THIN); //下枠
        }
        cellStyleLocationNameBorderColor.setLeftBorderColor(IndexedColors.RED.getIndex());//左边框为红色 

        CellStyle lastRowCellStyleLocationNameBorderColor = workbook.createCellStyle();
        lastRowCellStyleLocationNameBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        lastRowCellStyleLocationNameBorderColor.setBottomBorderColor(IndexedColors.RED.getIndex());
        lastRowCellStyleLocationNameBorderColor.setBorderLeft(BorderStyle.MEDIUM);//左枠
        lastRowCellStyleLocationNameBorderColor.setLeftBorderColor(IndexedColors.RED.getIndex());//左边框为红色 
        lastRowCellStyleLocationNameBorderColor.setBorderRight(BorderStyle.DOTTED);//右枠

        CellStyle cellStyleLocationAddressBorderColor = workbook.createCellStyle();
        cellStyleLocationAddressBorderColor.setBorderRight(BorderStyle.MEDIUM);//右枠
        if (rowAdded) {
            cellStyleLocationAddressBorderColor.setBorderBottom(BorderStyle.HAIR); //下枠
        } else {
            cellStyleLocationAddressBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        }
        cellStyleLocationAddressBorderColor.setRightBorderColor(IndexedColors.RED.getIndex());//右边框为红色 

        CellStyle lastRowCellStyleLocationAddressBorderColor = workbook.createCellStyle();
        lastRowCellStyleLocationAddressBorderColor.setBorderBottom(BorderStyle.MEDIUM); //下枠
        lastRowCellStyleLocationAddressBorderColor.setBottomBorderColor(IndexedColors.RED.getIndex());
        lastRowCellStyleLocationAddressBorderColor.setBorderRight(BorderStyle.MEDIUM);//左枠
        lastRowCellStyleLocationAddressBorderColor.setRightBorderColor(IndexedColors.RED.getIndex());//左边框为红色 

        String[] selection = new String[]{choiceMap.get("in_operation"), choiceMap.get("low_operation"), choiceMap.get("non_operation")};

        int count = machineList.size() - 1;
        for (int rowNum = 0; rowNum < machineList.size(); rowNum++) {

            Object[] r0001InventoryRequestVo = (Object[]) machineList.get(rowNum);

            int index = 0;
            row = sheet.createRow(startRowNum + rowNum);
            //  品目コード 0
            cell = row.createCell((int) refITEM_CODE.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 資産番号 1
            index++;
            cell = row.createCell((int) refASSET_NO.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 補助番号 2
            index++;
            cell = row.createCell((int) refBRANCH_NO.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            //名称 3
            index++;
            cell = row.createCell((int) refITEM_NAME.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 種類 4
            index++;
            cell = row.createCell((int) refASSET_TYPE.getCol());
            if ("0".equals(getStringValue(r0001InventoryRequestVo[index]))) {
                cell.setCellValue("");
            } else {
                String assetType = choiceMap.get(ASSET_TYPE + getStringValue(r0001InventoryRequestVo[index]));
                cell.setCellValue(assetType);
            }
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 取得区分 5
            index++;
            cell = row.createCell((int) refACQUISITION_TYPE.getCol());
            if ("0".equals(getStringValue(r0001InventoryRequestVo[index]))) {
                cell.setCellValue("");
            } else {
                String type = choiceMap.get(ACQUISITION_TYPE + getStringValue(r0001InventoryRequestVo[index]));
                cell.setCellValue(type);
            }
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 取得金額 6
            index++;
            cell = row.createCell((int) refACQUISITION_AMOUNT.getCol());
            int intItem = Integer.parseInt(getStringValue(r0001InventoryRequestVo[10]));
            String aAmount = String.valueOf(new BigDecimal(getStringValue(r0001InventoryRequestVo[index])).setScale(intItem, BigDecimal.ROUND_DOWN));
            cell.setCellValue(aAmount);
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
            // 取得年月 7
            index++;
            cell = row.createCell((int) refACQUISITION_YYYYMM.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }

            cell = row.createCell((int) refMACHINE_YES.getCol());
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyleMachineYesBorderColor);
            } else {
                cell.setCellStyle(cellStyleMachineYesBorderColor);
            }

            cell = row.createCell((int) refMACHINE_NO.getCol());
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyleMachineNoBorderColor);
            } else {
                cell.setCellStyle(cellStyleMachineNoBorderColor);
            }

            cell = row.createCell((int) refMACHINE_WORK_STAUTS.getCol());
            generateSelect(sheet, selection, row.getRowNum(), cell.getColumnIndex());
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyleWorkStatusBorderColor);
            } else {
                cell.setCellStyle(cellStyleWorkStatusBorderColor);
            }

            // 金型所在先 8
            index++;
            cell = row.createCell((int) refMGMT_LOCATION_NAME.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }

            cell = row.createCell((int) refMACHINE_CHANGED_LOCATION_NAME.getCol());
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyleLocationNameBorderColor);
            } else {
                cell.setCellStyle(cellStyleLocationNameBorderColor);
            }

            cell = row.createCell((int) refMACHINE_CHANGED_ADDRESS.getCol());
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyleLocationAddressBorderColor);
            } else {
                cell.setCellStyle(cellStyleLocationAddressBorderColor);
            }

            // 共通情報 9
            index++;
            cell = row.createCell((int) refCOMMON_INFORMATION.getCol());
            cell.setCellValue(getStringValue(r0001InventoryRequestVo[index]));
            if (rowNum == count) {
                cell.setCellStyle(lastRowCellStyle);
            } else {
                cell.setCellStyle(cellStyle);
            }
        }

//        // 列の内容により幅を自動調整
        sheet.autoSizeColumn((int) refITEM_CODE.getCol());
//        sheet.autoSizeColumn((int) refASSET_NO.getCol());
//        sheet.autoSizeColumn((int) refBRANCH_NO.getCol());
//        sheet.autoSizeColumn((int) refITEM_NAME.getCol());
//        sheet.autoSizeColumn((int) refASSET_TYPE.getCol());
//        sheet.autoSizeColumn((int) refACQUISITION_TYPE.getCol());
//        sheet.autoSizeColumn((int) refACQUISITION_AMOUNT.getCol());
//        sheet.autoSizeColumn((int) refACQUISITION_YYYYMM.getCol());
//        sheet.autoSizeColumn((int) refMGMT_LOCATION_NAME.getCol());
//        sheet.autoSizeColumn((int) refCOMMON_INFORMATION.getCol());
    }

    /**
     *
     * @param obj
     * @return
     */
    private String getStringValue(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
        }

    }

    /**
     * 管理先ごとにテンプレート書き込み
     *
     * @param mapData
     * @throws IOException
     */
    public void update(Map<String, R0001InventoryRequest> mapData) throws IOException {

        // 管理先ごとにループし依頼票ファイルを更新
        for (String mgmtConpanyCode : mapData.keySet()) {
            R0001InventoryRequest r0001InventoryRequest = mapData.get(mgmtConpanyCode);
            String outputPath = r0001InventoryRequest.getOutputPath();
            // ワークブックを読み込みます。
            Workbook workbook = getWorkbookTypeByFile(outputPath);
            if (workbook == null) {
                return;
            }

            // シートを読み込みます。
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            // 金型
            Sheet sheet = workbook.getSheetAt(0);
            Name name = workbook.getName("MOLD_DUE_DATE");
            CellReference ref = new CellReference(name.getRefersToFormula());
            Row row = sheet.getRow(ref.getRow());
            Cell cell = row.getCell(ref.getCol());
            if (r0001InventoryRequest.getDueDate() != null) {
                String outDate = sdf.format(r0001InventoryRequest.getDueDate());
                cell.setCellValue(outDate);
            } else {
                cell.setCellValue("");
            }

            // 設備
            sheet = workbook.getSheetAt(1);
            name = workbook.getName("MACHINE_DUE_DATE");
            ref = new CellReference(name.getRefersToFormula());
            row = sheet.getRow(ref.getRow());
            cell = row.getCell(ref.getCol());
            if (r0001InventoryRequest.getDueDate() != null) {
                String outDate = sdf.format(r0001InventoryRequest.getDueDate());
                cell.setCellValue(outDate);
            } else {
                cell.setCellValue("");
            }

            //ワークブックを読み込みます。
            FileOutputStream outputStream = null;
            try {

                outputStream = new FileOutputStream(outputPath);

                workbook.write(outputStream);

                workbook.close();

                outputStream.close();

            } catch (Exception e) {
                Logger.getLogger(InventoryInfoCreateTemplate.class.getName()).log(Level.SEVERE, null, e);
                workbook.close();
                workbook = null;

                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;

                }
            }
        }
    }

    /**
     * 管理先ごとにテンプレート書き込み
     *
     * @param mapData
     * @throws IOException
     */
    public void updateRequestDate(Map<String, R0001InventoryRequest> mapData) throws IOException {

        // 管理先ごとにループし依頼票ファイルを更新
        for (String mgmtConpanyCode : mapData.keySet()) {
            R0001InventoryRequest r0001InventoryRequest = mapData.get(mgmtConpanyCode);
            String outputPath = r0001InventoryRequest.getOutputPath();
            // ワークブックを読み込みます。
            Workbook workbook = getWorkbookTypeByFile(outputPath);
            if (workbook == null) {
                return;
            }

            // シートを読み込みます。
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            // 金型
            Sheet sheet = workbook.getSheetAt(0);
            Name name = workbook.getName("MOLD_REQUESTED_DATE");
            CellReference ref = new CellReference(name.getRefersToFormula());
            Row row = sheet.getRow(ref.getRow());
            Cell cell = row.getCell(ref.getCol());
            String outDate = sdf.format(new Date());
            cell.setCellValue(outDate);

            // 設備
            sheet = workbook.getSheetAt(1);
            name = workbook.getName("MACHINE_REQUESTED_DATE");
            ref = new CellReference(name.getRefersToFormula());
            row = sheet.getRow(ref.getRow());
            cell = row.getCell(ref.getCol());
            cell.setCellValue(outDate);

            //ワークブックを読み込みます。
            FileOutputStream outputStream = null;
            try {

                outputStream = new FileOutputStream(outputPath);

                workbook.write(outputStream);

                workbook.close();

                outputStream.close();

            } catch (Exception e) {
                Logger.getLogger(InventoryInfoCreateTemplate.class.getName()).log(Level.SEVERE, null, e);
                workbook.close();
                workbook = null;

                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;

                }
            }
        }
    }

    /**
     * セルリストをセットする用
     *
     * @param sheet
     * @param object
     * @param rowNum
     * @param cn
     */
    private void generateSelect(Sheet sheet, Object object, int rowNum, int cn) {
        if (object != null && object instanceof String[]) {
            String[] textlist = (String[]) object;
            // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列  
            CellRangeAddressList regions = new CellRangeAddressList(rowNum,
                    rowNum, cn, cn);
            if (sheet instanceof HSSFSheet) {
                DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);

                // 数据有效性对象  
                HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
                sheet.addValidationData(data_validation_list);
            } else if (sheet instanceof XSSFSheet) {
                if (textlist.length > 0) {
                    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
                    XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                            .createExplicitListConstraint(textlist);
                    XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(
                            dvConstraint, regions);
                    sheet.addValidationData(validation);
                }
            }
        }

    }

}
