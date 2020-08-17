package categoryItemSetting;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CategoryItemSettingTest {

    @Test
    public void createCategoryItemTestCases() {

        //Scenario-3: Enter All CategoryItem Data 
        EnterAllCategorySettingData();
    }

    public void EnterAllCategorySettingData() {
        try {
            //Scenario-4: Select Master Category Item
            SelectDepartmentMasterRecord();
            //Scenario-5: Enter Child Record for selected Master record
            EnterDepartmentFromExcel();

            //Scenario-6: Select Master Category Item
            SelectPartsMasterRecord();
            //Scenario-7: Enter Child Record for selected Master record
            EnterPartsFromExcel();

            //Scenario-8: Select Master Category Item
            SelectMoldMasterRecord();
            //Scenario-9: Enter Child Record for selected Master record
            EnterMoldFromExcel();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void EnterDepartmentFromExcel() throws IOException {
        // Read Data from Excel File.
        String filePath = "C:\\apps\\M-Karte\\documents\\testData";
        String fileName = "departmentCategory.xlsx";
        String sheetName = "department";
        String CellText;
        File objFile = new File(filePath + "\\" + fileName);
        FileInputStream objFileInpStrm = new FileInputStream(objFile);

        XSSFWorkbook  excelWorkbook = new XSSFWorkbook(objFileInpStrm);

        XSSFSheet sheetOfExcellWorkbook = excelWorkbook.getSheet(sheetName);
        int excelRowCount = sheetOfExcellWorkbook.getLastRowNum() - sheetOfExcellWorkbook.getFirstRowNum();
        if (excelRowCount > 0) {
            String listXpath = "//table[@id='list21']/tbody/tr";  //   //*[@id="list11"] #list11     
            ElementsCollection categoryChildList = $$(By.xpath(listXpath));
            int countChildRecordList = categoryChildList.size();//("#//*[@id=\"list11\"]").size();; 
            int indexOfAllRow = countChildRecordList - 1;//To avoid header index decrease index 1

            for (int i = 1; i < excelRowCount + 1; i++) {
                //Scenario-5: Add New record
                $("#btnadd").click();
                int indexOfnewRow = indexOfAllRow + i;
                String beforeXpath = "//*[@id=\"";
                String afterXpath = "_choice\"]";
                CellText = sheetOfExcellWorkbook.getRow(i).getCell(0).getStringCellValue();
                $(By.xpath(beforeXpath + indexOfnewRow + afterXpath)).sendKeys(CellText);
            }
            //Common Scenario: Click Save button.
            $("#btnsave").click();

            //Common Scenario: Click OK button to confirm record Registration.
            ConfirmDialog();
        }
    }

    public void EnterPartsFromExcel() throws IOException {
        // Read Data from Excel File.
        String filePath = "C:\\apps\\M-Karte\\documents\\testData";
        String fileName = "partCategory.xlsx";
        String sheetName = "part";
        String CellText;
        File objFile = new File(filePath + "\\" + fileName);
        FileInputStream objFileInpStrm = new FileInputStream(objFile);

        XSSFWorkbook  excelWorkbook = new XSSFWorkbook(objFileInpStrm);

        XSSFSheet sheetOfExcellWorkbook = excelWorkbook.getSheet(sheetName);
        int excelRowCount = sheetOfExcellWorkbook.getLastRowNum() - sheetOfExcellWorkbook.getFirstRowNum();
        if (excelRowCount > 0) {
            String listXpath = "//table[@id='list21']/tbody/tr";  //   //*[@id="list11"] #list11     
            ElementsCollection categoryChildList = $$(By.xpath(listXpath));
            int countChildRecordList = categoryChildList.size();//("#//*[@id=\"list11\"]").size();; 
            int indexOfAllRow = countChildRecordList - 1;//To avoid header index decrease index 1

            for (int i = 1; i < excelRowCount + 1; i++) {
                //Scenario-5: Add New record
                $("#btnadd").click();
                int indexOfnewRow = indexOfAllRow + i;
                String beforeXpath = "//*[@id=\"";
                String afterXpath = "_choice\"]";
                CellText = sheetOfExcellWorkbook.getRow(i).getCell(0).getStringCellValue();
                $(By.xpath(beforeXpath + indexOfnewRow + afterXpath)).sendKeys(CellText);
            }
            //Common Scenario: Click Save button.
            $("#btnsave").click();

            //Common Scenario: Click OK button to confirm record Registration.
            ConfirmDialog();
        }
    }

    public void EnterMoldFromExcel() throws IOException {
        // Read Data from Excel File.
        String filePath = "C:\\apps\\M-Karte\\documents\\testData";
        String fileName = "moldCategory.xlsx";
        String sheetName = "mold";
        String CellText;
        File objFile = new File(filePath + "\\" + fileName);
        FileInputStream objFileInpStrm = new FileInputStream(objFile);

        XSSFWorkbook  excelWorkbook = new XSSFWorkbook(objFileInpStrm);

        XSSFSheet sheetOfExcellWorkbook = excelWorkbook.getSheet(sheetName);
        int excelRowCount = sheetOfExcellWorkbook.getLastRowNum() - sheetOfExcellWorkbook.getFirstRowNum();
        if (excelRowCount > 0) {
            String listXpath = "//table[@id='list21']/tbody/tr";  //   //*[@id="list11"] #list11     
            ElementsCollection categoryChildList = $$(By.xpath(listXpath));
            int countChildRecordList = categoryChildList.size();//("#//*[@id=\"list11\"]").size();; 
            int indexOfAllRow = countChildRecordList - 1;//To avoid header index decrease index 1

            for (int i = 1; i < excelRowCount + 1; i++) {
                //Scenario-5: Add New record
                $("#btnadd").click();
                int indexOfnewRow = indexOfAllRow + i;
                String beforeXpath = "//*[@id=\"";
                String afterXpath = "_choice\"]";
                CellText = sheetOfExcellWorkbook.getRow(i).getCell(0).getStringCellValue();
                $(By.xpath(beforeXpath + indexOfnewRow + afterXpath)).sendKeys(CellText);
            }
            //Common Scenario: Click Save button.
            $("#btnsave").click();

            //Common Scenario: Click OK button to confirm record Registration.
            ConfirmDialog();
        }
    }

    private void ConfirmDialog() {
        $(By.xpath("//div[@class='ui-dialog-buttonset']")).click();
    }

    private void SelectDepartmentMasterRecord() {
        $(By.xpath("//*[@id=\"1\"]/td[1]")).click();
    }

    private void SelectPartsMasterRecord() {
        $(By.xpath("//*[@id=\"2\"]/td[1]")).click();
    }

    private void SelectMoldMasterRecord() {
        $(By.xpath("//*[@id=\"4\"]/td[1]")).click();
    }

    private void SelectWorkPhaseMasterRecord() {
        $(By.xpath("//*[@id=\"4\"]/td[1]")).click();
    }

    private void SelectWorkPhaseTypeMasterRecord() {
        $(By.xpath("//*[@id=\"39\"]/td[1]")).click();
    }

//    //Scenario-3
//    private void SelectMasterCategoryRecord() throws IOException {
//        String listXpath = "//table[@id='list11']/tbody/tr";  //   //*[@id="list11"] #list11     
//        ElementsCollection categoryMasterList = $$(By.xpath(listXpath));
//        int countMasterRecordList = categoryMasterList.size();//("#//*[@id=\"list11\"]").size();;
//        for(int i=1; i<countMasterRecordList; i++){
//            String beforeXpath = "//*[@id=\"";
//            String afterXpath = "\"]/td[1]";
//            String fullXpath =beforeXpath+afterXpath;
//            $(By.xpath(beforeXpath+i+afterXpath)).click();    
//            //Scenario-4: Enter Child Record for selected Master record
//            EnterDepartmentFromExcel();
//
//        }
//     
//    }
    //*[@id="1"]/td[1]
    //*[@id="3"]/td[1]
}
