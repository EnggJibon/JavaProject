package workphase;

import com.codeborne.selenide.ElementsCollection;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class WorkPhaseTest {

    @Test
    public void OpenWorkPhaseScreen() {
        // Scenario-3: Enter workphase from excel
        EnterWorkPhaseDataFromExcel();
    }

    public void EnterWorkPhaseDataFromExcel() {
        try {
            ArrayList<String> workPhaseCode = ExportExcelData(0);
            ArrayList<String> workPhaseName = ExportExcelData(1);
            ArrayList<String> workPhaseType = ExportExcelData(2);
            ArrayList<String> directFlag = ExportExcelData(3);

            String cellText;
            int totalRowCount = workPhaseCode.size();
            if (totalRowCount > 0) {

                for (int i = 0; i < totalRowCount; i++) {
                    String listXpath = "//table[@id='list_work_phase']/tbody/tr";
                    ElementsCollection categoryChildList = $$(By.xpath(listXpath));
                    int countChildRecordList = categoryChildList.size();
                    int indexOfAllRow = countChildRecordList - 1;

                    // Scenario-3: Click New button to add record
                    $("#btnadd").click();
                    int indexOfnewRow = indexOfAllRow + 1;
                    if (workPhaseCode.size()>0) {
                        cellText = workPhaseCode.get(i);
                        String beforeXpath = "//*[@id=\"";
                        String afterXpath = "_workPhaseCode\"]";
                        int j = i + 1;
                        $(By.xpath("//*[@id=\"" + j + "\"]/td[2]")).click();
                        $(By.xpath(beforeXpath + indexOfnewRow + afterXpath)).val(cellText).pressTab();
                    }
                    if (workPhaseName.size()>0) {
                        cellText = workPhaseName.get(i);
                        String beforeXpath = "//*[@id=\"";
                        String afterXpath = "_workPhaseName\"]";
                        //$(By.xpath(beforeXpath+indexOfnewRow+afterXpath)).click();
                        $(By.xpath(beforeXpath + indexOfnewRow + afterXpath)).val(cellText).pressTab();
                    }

                    if (directFlag.size()>0) {
                        double directFlagvalue = Double.valueOf(directFlag.get(i));
                        int directFlagValues = (int) Math.round(directFlagvalue);
                        String beforeXpath = "//*[@id=\"";
                        String afterXpath = "_directFlg\"]";
                        // $(By.xpath(beforeXpath+indexOfnewRow+afterXpath)).click();
                        $(By.xpath(beforeXpath + indexOfnewRow + afterXpath)).selectOptionByValue(String.valueOf(directFlagValues));
                        $(By.xpath(beforeXpath + indexOfnewRow + afterXpath));
                    }
//                    if (workPhaseType.size()>0) {
//                        double workPhaseTypeValue = Double.valueOf(workPhaseType.get(i));
//                        int workPhaseTypeValues = (int) Math.round(workPhaseTypeValue);
//                        String beforeXpath = "//*[@id=\"";
//                        String afterXpath = "_workPhaseType\"]";
//                        $(By.xpath(beforeXpath + indexOfnewRow + afterXpath)).click();
//                        $(By.xpath(beforeXpath + indexOfnewRow + afterXpath)).selectOptionByValue(String.valueOf(workPhaseTypeValues));
////                        $(By.xpath(beforeXpath + indexOfnewRow + afterXpath)).pressTab();
//                    }
                    //Scenario-5: Click Save button to Save Data
                    //selectDepartmentForWorkPhase();
                    
                    //Scenario-6: Click Save button to Save Data
                    $("#btnsave").click();
                    
                    //Scenario-7: Confirm Registration
                    ConfirmDialog();

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
        String fileName = "workPhase.xlsx";
        String sheetName = "workPhase";
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

    public void selectDepartmentForWorkPhase() {
        $(By.xpath("//*[@id=\"1\"]/td[1]/input")).setSelected(true);
        $(By.xpath("//*[@id=\"2\"]/td[1]/input")).setSelected(true);
        $(By.xpath("//*[@id=\"3\"]/td[1]/input")).setSelected(true);
    }

    private void ConfirmDialog() {
        $(By.xpath("//div[@class='ui-dialog-buttonset']")).click();
    }

}
