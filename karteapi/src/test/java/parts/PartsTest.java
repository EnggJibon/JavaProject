package parts;

import com.codeborne.selenide.Condition;
import static com.codeborne.selenide.Selenide.$;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.apache.poi.ss.usermodel.Cell;

public class PartsTest {

    @Test
    public void createPartsTestCases() {

        //Scenario-2: Cliclk Add New Record  
        $("#btnnew").shouldBe(Condition.visible);
        $("#btnnew").click();

        // Scenario-3: Enter Parts Master record from Excel
        EnterPartsFromExcel();
    }

    public void EnterMoldFromExcel() throws FileNotFoundException, IOException {
        String filePath = "C:\\apps\\M-Karte\\documents\\testData";
        String fileName = "partsMold.xlsx";
        String sheetName = "partsMold";
        //Create an object of File Class 
        File objFile = new File(filePath + "\\" + fileName);
        //Create an object of File input Stream to read excel file
        FileInputStream objFileInpStrm = new FileInputStream(objFile);

        XSSFWorkbook  dataFromExcelFile = new XSSFWorkbook(objFileInpStrm);
        XSSFSheet dataFromExcelFileSheet = dataFromExcelFile.getSheet(sheetName);
        int rowCount = dataFromExcelFileSheet.getLastRowNum() - dataFromExcelFileSheet.getFirstRowNum();
        String cellText;
        if (rowCount > 0) {
            for (int i = 1; i < rowCount + 1; i++) {
                $("#btnmoldadd").click();
                String beforeXpath = "//*[@id=\"";
                String afterXpath = "_moldId\"]";
                cellText = dataFromExcelFileSheet.getRow(i).getCell(0).getStringCellValue();
                $(By.xpath(beforeXpath + i + afterXpath)).sendKeys(cellText);
                $(By.xpath(beforeXpath + i + afterXpath)).shouldBe(Condition.visible);
            }
        }
    }

    public void EnterPartsFromExcel() {
        try {
            ArrayList<String> componentCode = readFile(0);
            ArrayList<String> componentName = readFile(1);
            ArrayList<String> componentType = readFile(2);
            ArrayList<String> unitPrice = readFile(8);
            ArrayList<String> currencyCode = readFile(9);
            ArrayList<String> stockUnit = readFile(10);
            ArrayList<String> isPurchasedPart = readFile(3);
            ArrayList<String> isCircuitBoard = readFile(4);
            ArrayList<String> snLength = readFile(5);
            ArrayList<String> snFixedValue = readFile(6);
            ArrayList<String> snFixedLength = readFile(7);
            int totalRowCount = componentCode.size();
            if (totalRowCount > 0) {
                for (int i = 0; i < totalRowCount; i++) {
                    if (componentCode.size()>0) {
                        $("#componentCode").val(componentCode.get(i)).pressTab();
                    }
                    if (componentName.size()>0) {
                        $("#componentName").val(componentName.get(i)).pressTab();
                    }
                    if (componentType.size()>0) {
                        $("#componentType").selectOption(componentType.get(i));
                        $("#componentType").pressTab();
                    }
                    if (unitPrice.size()>0) {
                        double unitPriceValue = Double.valueOf(unitPrice.get(i));
                        int unitPriceValueId = (int) Math.round(unitPriceValue);
                        $("#unitPrice").val(String.valueOf(unitPriceValueId));
                        $("#unitPrice").pressTab();
                    }
                    if (currencyCode.size()>0) {
                        $("#currencyCode").selectOption(currencyCode.get(i));
                        $("#currencyCode").pressTab();
                    }
                    if (stockUnit.size()>0) {
                        double stockUnitvalue = Double.valueOf(stockUnit.get(i));
                        int stockUnitRoundValue = (int) Math.round(stockUnitvalue);
                        $("#stockUnit").val(String.valueOf(stockUnitRoundValue));
                        $("#stockUnit").pressTab();
                    }
                    if (isPurchasedPart.size()>0) {
                        boolean purchasePart = false;
                        double ispurchasedPartValue = Double.valueOf(isPurchasedPart.get(i));
                        int ispurchasedPartRoundValue = (int) Math.round(ispurchasedPartValue);
                        if (ispurchasedPartRoundValue == 1) {
                            purchasePart = true;
                        }
                        $("#isPurchasedPart").setSelected(purchasePart);
                        $("#isPurchasedPart").pressTab();
                    }
                    if (isCircuitBoard.size()>0) {

                        boolean circuitBoard = false;
                        double iscircuitBoardValue = Double.valueOf(isCircuitBoard.get(i));
                        int iscircuitBoardRoundValue = (int) Math.round(iscircuitBoardValue);
                        if (iscircuitBoardRoundValue == 1) {
                            circuitBoard = true;
                        }
                        $("#isCircuitBoard").setSelected(circuitBoard);
                        $("#isCircuitBoard").pressTab();
                    }
                    if (snLength.size()>0) {
                        $("#snLength").val(snLength.get(i)).pressTab();
                    }
                    if (snFixedValue.size()>0) {
                        $("#snFixedValue").val(snFixedValue.get(i)).pressTab();
                    }
                    if (snFixedLength.size()>0) {
                        $("#snFixedLength").val(snFixedLength.get(i));
                    }

                    // Scenario-4: Enter Parts Master record from Excel  
                    //EnterMoldFromExcel();

                    //Scenario-5: Click Save button to Save Data
                    $("#btnsave").click();

                    //Scenario-6: Confirm Registration
                    ConfirmDialog();
                    
                    //Scenario-7: Back to User Detail Screen  
                    $("#btncancel").click();

                    // Repeat Scenario-3: Click New button to add user Information
                    $("#btnnew").click();

                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<String> readFile(int colNo) throws FileNotFoundException, IOException {
        //FileName
        String filePath = "C:\\apps\\M-Karte\\documents\\testData";
        String fileName = "PartsTestScript.xlsx";
        String sheetName = "parts";
        //Create an object of File Class 
        File objFile = new File(filePath + "\\" + fileName);
        //Create an object of File input Stream to read excel file
        FileInputStream objFileInpStrm = new FileInputStream(objFile);

        XSSFWorkbook  dataFromExcelFile = new XSSFWorkbook(objFileInpStrm);
        XSSFSheet dataFromExcelFileSheet = dataFromExcelFile.getSheet(sheetName);
        int rowCount = dataFromExcelFileSheet.getLastRowNum() - dataFromExcelFileSheet.getFirstRowNum();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < rowCount + 1; i++) {
            Row row = dataFromExcelFileSheet.getRow(i);
            String cellText = "";
            XSSFCell cell = dataFromExcelFileSheet.getRow(i).getCell(colNo);
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    list.add(dataFromExcelFileSheet.getRow(i).getCell(colNo).getStringCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    cellText = String.valueOf(dataFromExcelFileSheet.getRow(i).getCell(colNo).getNumericCellValue());
                    list.add(cellText);
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cellText = String.valueOf(dataFromExcelFileSheet.getRow(i).getCell(colNo).getBooleanCellValue());
                    list.add(cellText);
                    break;
                case Cell.CELL_TYPE_BLANK:
                    list.add(cellText);
                    ;
                    break;
                default:
                    cellText = String.valueOf(cell);
                    list.add(cellText);
            }
        }
        return list;
    }

    private void ConfirmDialog() {
        $(By.xpath("//div[@class='ui-dialog-buttonset']")).click();
    }

}
