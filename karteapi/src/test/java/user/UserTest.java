package user;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Selenide.$;
import org.junit.Test;
import static com.codeborne.selenide.Selenide.open;
import com.kmcj.karte.constants.CommonConstants;
//import constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.openqa.selenium.WebDriver;
//import javax.persistence.PersistenceContext;
//import javax.persistence.PersistenceContext;

@Dependent
public class UserTest {
//    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
//    EntityManager entityManager;
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Test
    public void createUserTestCases() {

        //Scenario-3: Click New button to add user Information
        $("#btnnew").click();

        //Scenario-4: Enter data in User Screen
        enterUserFromExcel();
    }
    
    public void deleteAlltheUserData(){
        
        //EntityManager entityManager = Persistence.createEntityManagerFactory("pu_karte").createEntityManager();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(CommonConstants.PERSISTENCE_UNIT_NAME);      
        EntityManager entityManager =  emf.createEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM MstUser m WHERE m.userId <>'admin'");
        Query query = entityManager.createQuery(sql.toString());
        //Query query =entityManager.createQuery(sql.toString());
        query.executeUpdate();
    }
    

    public void enterUserFromExcel() {
        try {
            //Delete Data from User Master Table
            //deleteAlltheUserData();
                  
            ArrayList<String> userId = readFile(0);
            ArrayList<String> userName = readFile(1);
            ArrayList<String> mailAddress = readFile(2);
            ArrayList<String> langId = readFile(3);
            ArrayList<String> timezone = readFile(4);
            ArrayList<String> authId = readFile(5);
            ArrayList<String> companyName = readFile(6);
            ArrayList<String> department = readFile(7);
            ArrayList<String> procCd = readFile(8);
            ArrayList<String> validFlg = readFile(9);
            int totalRowCount = userId.size();
            for (int i = 0; i < totalRowCount; i++) {
                WebDriver newTab = Selenide.switchTo().window(1);
                if (userId.size()>0) {
                    $("#userId").val(userId.get(i)).pressTab();
                }
                if (userName.size()>0) {
                    $("#userName").val(userName.get(i)).pressTab();
                }
                if (mailAddress.size()>0) {
                    $("#mailAddress").val(mailAddress.get(i)).pressTab();
                }
                if (langId.size()>0) {
                    $("#langId").selectOptionByValue(langId.get(i));
                    $("#langId").pressTab();
                }
//                if (authId.size()>0) {
//                    double authIdValue = Double.valueOf(authId.get(i));
//                    int numericAuthid = (int) Math.round(authIdValue);
//                    $("#authId").selectOption(numericAuthid);
//                    $("#authId").pressTab();
//                }
//                if (timezone.size()>0) {
//                    $("#timezone").selectOption(timezone.get(i));
//                    $("#timezone").pressTab();
//                }
//                if (companyName.size()>0) {
//                    $("#companyName").val(companyName.get(i));
//                    $("#companyName").shouldBe(Condition.visible);
//                }
//                if (department.size()>0) {
//                    double departmentValue = Double.valueOf(authId.get(i));
//                    int numericDepartmentId = (int) Math.round(departmentValue);
//                    $("#department").selectOption(numericDepartmentId);
//                }
                if (procCd.size()>0) {
                    $("#procCd").val(procCd.get(i)).pressTab();
                    $("#procCd").pressTab();
                }

                //Scenario-5: Click Save button to Save Data
                $("#btnsave").click();

                //Scenario-6: Confirm Registration
                confirmDialog();

                Selenide.switchTo().window(0);

                // Repeat Scenario-3: Click New button to add user Information
                $("#btnnew").click();
            }
            //////EnterUserMaintainance();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //public  void readTestFile() throws IOException{
    public ArrayList<String> readFile(int colNo) throws FileNotFoundException, IOException {

        ArrayList<String> list = new ArrayList<>();
        //FileName
        String filePath = "C:\\apps\\M-Karte\\documents\\testData";
//        String folderPath = kartePropertyService.getDocumentDirectory();
//        StringBuffer txtOutPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
//        txtOutPath = txtOutPath.append(SEPARATOR).append(CommonConstants.EXCEL);
//        String filePath = txtOutPath.toString();
        String fileName = "UserTestScript.xlsx";
        String sheetName = "UserTestScript";
        //Create an object of File Class 
        File objFile = new File(filePath + "\\" + fileName);
        //Create an object of File input Stream to read excel file
        FileInputStream objFileInpStrm = new FileInputStream(objFile);

        XSSFWorkbook dataFromExcelFile = new XSSFWorkbook(objFileInpStrm);      
        //Read Sheet Name inside the workbook    

        XSSFSheet dataFromExcelFileSheet = dataFromExcelFile.getSheet(sheetName);
        int rowCount = dataFromExcelFileSheet.getLastRowNum() - dataFromExcelFileSheet.getFirstRowNum();

        for (int i = 1; i < rowCount + 1; i++) {
            Row row = dataFromExcelFileSheet.getRow(i);
            String cellText;

            XSSFCell cell = dataFromExcelFileSheet.getRow(i).getCell(colNo);
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    list.add(dataFromExcelFileSheet.getRow(i).getCell(colNo).getStringCellValue());
                    //list.add(cell.getStringCellValue());
                    //cellText = rowIterator.next().getCell(colNo).getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    cellText = String.valueOf(dataFromExcelFileSheet.getRow(i).getCell(colNo).getNumericCellValue());
                    list.add(cellText);
                    //list.add(String.valueOf(cell.getNumericCellValue()));
                    //cellText =String.valueOf(cell.getNumericCellValue());
                    //cellText = String.valueOf(rowIterator.next().getCell(colNo).getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    cellText = String.valueOf(dataFromExcelFileSheet.getRow(i).getCell(colNo).getBooleanCellValue());
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
        return list;
    }

    private void createUserTestInformation() {
        //Scenario-1: Enter User Maintainance Screen
        open("/php/S0005_MstUser.php");
        //Scenario-2: Click New button to add user Information
        $("#btnnew").click();

        String companyName = "KMC";
        //Scenario-3: Enter User Information in User Maintainance Screen
        $("#userId").val("TestJB001").pressTab();
        $("#userName").val("TestJB001").pressTab();
        $("#mailAddress").val("m.jibon@kmc-j.com").pressTab();
        $("#langId").selectOptionByValue("en");
        $("#timezone").selectOption("Asia/Tokyo");
        $("#authId").selectOption(1);
        $("#companyName").val(companyName).pressTab();
        $("#companyName").shouldBe(Condition.visible);
        $("#department").selectOption(2);
        $("#procCd").val("TestedBySelenide").pressTab();

        //Scenario-4: Click Save button to Save Data  
        $("#btnsave").click();

        //Scenario-5: Close Saveed or Failed confirm Dialog
        confirmDialog();

    }

    private void confirmDialog() {
        $(By.xpath("//div[@class='ui-dialog-buttonset']")).click();
    }

}
