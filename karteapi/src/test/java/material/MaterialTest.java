package material;

import static com.codeborne.selenide.Selenide.$;
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

public class MaterialTest {

    @Test
    public void EnterDowntimeFromExcel() {

        EnterDataFromExcel();
    }

    public void EnterDataFromExcel() {

        try {
            ArrayList<String> materialCode = ExportExcelData(0);
            ArrayList<String> materialName = ExportExcelData(1);
            ArrayList<String> materialType = ExportExcelData(2);
            ArrayList<String> materialGrade = ExportExcelData(3);
            ArrayList<String> assetType = ExportExcelData(4);

            int totalRowCount = materialCode.size();
            if (totalRowCount > 0) {
                String cellText;
                for (int i = 0; i < totalRowCount; i++) {
                    // Scenario-3: Click New button to add record
                    $("#btnaddRecord").click();
                    if (materialCode.size()>0) {
                        cellText = materialCode.get(i);
                        String beforeXpath = "//*[@id=\"";
                        String afterXpath = "_materialCode\"]";
                        int j = i + 1;
                        $(By.xpath(beforeXpath + j + afterXpath)).sendKeys(cellText);
                    }
                    if (materialName.size()>0) {

                        cellText = materialName.get(i);
                        String beforeXpath = "//*[@id=\"";
                        String afterXpath = "_materialName\"]";
                        int j = i + 1;
                        $(By.xpath(beforeXpath + j + afterXpath)).sendKeys(cellText);
                    }
                    if (materialType.size()>0) {
                        cellText = materialType.get(i);
                        String beforeXpath = "//*[@id=\"";
                        String afterXpath = "_materialType\"]";
                        int j = i + 1;
                        $(By.xpath(beforeXpath + j + afterXpath)).sendKeys(cellText);
                    }
                    if (materialGrade.size()>0) {
                        cellText = materialGrade.get(i);
                        String beforeXpath = "//*[@id=\"";
                        String afterXpath = "_materialGrade\"]";
                        int j = i + 1;
                        $(By.xpath(beforeXpath + j + afterXpath)).sendKeys(cellText);
                    }

                    if (assetType.size()>0) {
                        double assetTypevalue = Double.valueOf(assetType.get(i));
                        int assetTypeValues = (int) Math.round(assetTypevalue);
                        String beforeXpath = "//*[@id=\"";
                        String afterXpath = "_assetCtg\"]";
                        int j = i + 1;
                        $(By.xpath(beforeXpath + j + afterXpath)).selectOptionByValue(String.valueOf(assetTypeValues));
                    }
                }
            }

            //Scenario-6: Click Save button to Save Data
            $("#btnsave").click();

            //Scenario-7: Confirm Registration
            ConfirmDialog();
            
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public ArrayList<String> ExportExcelData(int colNo) throws FileNotFoundException, IOException {

        ArrayList<String> list = new ArrayList<>();
        // Read Data from Excel File.
        String filePath = "C:\\apps\\M-Karte Development\\M-Karte Latest(1.8.00.00)\\WebTestCases\\M-KARTE\\documents\\testData";
        String fileName = "material.xlsx";
        String sheetName = "material";
        String CellText;
        File objFile = new File(filePath + "\\" + fileName);
        FileInputStream objFileInpStrm = new FileInputStream(objFile);

        XSSFWorkbook   excelWorkbook = new XSSFWorkbook(objFileInpStrm);
        
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
}
