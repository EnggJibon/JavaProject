/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downtimesetting;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import com.codeborne.selenide.SelenideElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.openqa.selenium.By;

/**
 *
 * @author m.jibon
 */
public class DowntimeSettingTest {

    @Test
    public void EnterDowntimeFromExcel() {

        EnterDataFromExcel();

        //Scenario-6: Click Save button to Save Data
        $("#btnsave").click();

        //Scenario-7: Confirm Registration
        ConfirmDialog();
    }

    public void EnterDataFromExcel() {

        try {
            ArrayList<String> downtimeCode = ExportExcelData(0);
            ArrayList<String> downtimeReason = ExportExcelData(1);
            ArrayList<String> downtimeType = ExportExcelData(2);

            int totalRowCount = downtimeCode.size();
            if (totalRowCount > 0) {
                for (int i = 0; i < totalRowCount; i++) {
                    // Scenario-3: Click New button to add record
                    $("#btnadd").click();

                    int indexOfAllRow = $$("#gridDowntime .slick-viewport .slick-row").size();//To avoid header index decrease index 1
                    String cellText;
                    if (downtimeCode.size()>0) {
                        SelenideElement downtimeCodeCell = getSlickCell("#gridDowntime", "downtimeCode", indexOfAllRow - 1);
                        downtimeCodeCell.click();
//                        String beforeXpath = "//*[@id=\"gridDowntime\"]/div[5]/div/div";
//                        String afterXPath = "/div[1]/input";
                        cellText = downtimeCode.get(i);
//                        $(By.xpath(beforeXpath + indexOfAllRow + afterXPath)).click();
//                        $(By.xpath(beforeXpath + afterXPath)).val(cellText).pressTab();
                        downtimeCodeCell.find("input").val(cellText).pressTab();
                    }
                    if (downtimeReason.size()>0) {
//                        String beforeXpath = "//*[@id=\"gridDowntime\"]/div[5]/div/div[";
//                        String afterXPath = "]/div[2]/input";
                        cellText = downtimeCode.get(i);
//                        $(By.xpath(beforeXpath + indexOfAllRow + afterXPath)).val(cellText).pressTab();
                        SelenideElement downtimeReasonCell = getSlickCell("#gridDowntime", "downtimeReason", indexOfAllRow - 1);
                        downtimeReasonCell.find("input").val(cellText).pressTab();
                    }
                    if (downtimeType.size()>0) {
                        
                        double downtimeTypevalue = Double.valueOf(downtimeType.get(i));
                        int downtimeTypeValues = (int) Math.round(downtimeTypevalue);
//                        String beforeXpath = "//*[@id=\"gridDowntime\"]/div[5]/div/div[";
//                        String afterXPath = "]/div[3]/select";
                        SelenideElement plannedFlgCell = getSlickCell("#gridDowntime", "plannedFlg", indexOfAllRow - 1);
                        plannedFlgCell.click();
                        plannedFlgCell.find("select").selectOptionByValue(String.valueOf(downtimeTypeValues));
//                        $(By.xpath(beforeXpath + indexOfAllRow + afterXPath)).click();
//                        $(By.xpath(beforeXpath + indexOfAllRow + afterXPath)).selectOptionByValue(String.valueOf(downtimeTypeValues));
//                        $(By.xpath(beforeXpath + indexOfAllRow + afterXPath)).pressTab();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public ArrayList<String> ExportExcelData(int colNo) throws FileNotFoundException, IOException {

        ArrayList<String> list = new ArrayList<>();
        // Read Data from Excel File.
        String filePath = "C:\\apps\\M-Karte\\documents\\testData";
        String fileName = "machineDowntime.xlsx";
        String sheetName = "downtime";
        String CellText;
        File objFile = new File(filePath + "\\" + fileName);
        FileInputStream objFileInpStrm = new FileInputStream(objFile);
        XSSFWorkbook  excelWorkbook = new XSSFWorkbook(objFileInpStrm);
        XSSFSheet sheetOfExcellWorkbook = excelWorkbook.getSheet(sheetName);
        int excelRowCount = sheetOfExcellWorkbook.getLastRowNum() - sheetOfExcellWorkbook.getFirstRowNum();
        if (excelRowCount > 0) {
            for (int i = 1; i < excelRowCount + 1; i++) {
                Row row = sheetOfExcellWorkbook.getRow(i);
                String cellText;

                XSSFCell cell = sheetOfExcellWorkbook.getRow(i).getCell(colNo);
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        list.add(sheetOfExcellWorkbook.getRow(i).getCell(colNo).getStringCellValue());
                        //list.add(cell.getStringCellValue());
                        //cellText = rowIterator.next().getCell(colNo).getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        cellText = String.valueOf(sheetOfExcellWorkbook.getRow(i).getCell(colNo).getNumericCellValue());
                        list.add(cellText);
                        //list.add(String.valueOf(cell.getNumericCellValue()));
                        //cellText =String.valueOf(cell.getNumericCellValue());
                        //cellText = String.valueOf(rowIterator.next().getCell(colNo).getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        cellText = String.valueOf(sheetOfExcellWorkbook.getRow(i).getCell(colNo).getBooleanCellValue());
                        list.add(cellText);
                        //list.add(String.valueOf(cell.getBooleanCellValue()));
                        //cellText = String.valueOf(cell.getBooleanCellValue());
                        //cellText = String.valueOf(rowIterator.next().getCell(colNo).getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        cellText = "";
                        list.add(cellText);
                        break;
                    default:
                        cellText = String.valueOf(cell);
                        list.add(cellText);
                }
            }
        }
        return list;
    }

    private void ConfirmDialog() {
        $(By.xpath("//div[@class='ui-dialog-buttonset']")).click();
    }

    private SelenideElement getSlickCell(String gridSelector, String columnID, int rowidx) {

        int colIdx = getSlickColmnIdx(gridSelector, columnID);
        return $$(gridSelector + String.format(" .slick-cell.l%d.r%d", colIdx, colIdx)).get(rowidx);
        //SelenideElement dimCell = $$(gridSelector + String.format(" .slick-cell.l%d.r%d", 0, 0)).set(rowidx, element);
    }

    private int getSlickColmnIdx(String gridSelector, String columnID) {
        SelenideElement grid = $(gridSelector);
        ElementsCollection headers = $$(gridSelector + " .slick-header-column");
        SelenideElement elm = headers.find(Condition.id(grid.attr("class").split(" ")[0] + columnID));//headers.find(Condition.id(grid.attr("class").split(" ")[0] + columnID));
        return headers.indexOf(elm);
    }

}
