/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package company;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import common.Common;
import static common.Common.closeConfirmDialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 会社マスタのテストコード
 * @author t.takasaki
 */
public class CompanyTest {
    @BeforeClass
    public static void beforeClass() {
        Common.beforeClass();
        Common.login("admin", "admin");
    }
    
    /**
     * C:\apps\M-KARTE\documents\testData\company.xlsxのデータを会社マスタで登録する。
     */
    @Test
    public void addCompanies() {
        List<Company> companies = getCompaniesFromExcel();
        for (Company c : companies) {
            open("/php/M0001_MstCompany.php");
            $("#btnnew").click();
            $("#companyCode").val(c.code);
            $("#companyName").val(c.name);
            $("#zipCode").val(c.postNo);
            $("#address").val(c.address);
            $("#telNo").val(c.tellNo);
            $("#mgmtCompanyCode").val(c.mngCmpCode);
            $("#myCompany").setSelected(c.isSelf);
            $("#btnsave").click();
            closeConfirmDialog("OK");
        }
    }
    
    private List<Company> getCompaniesFromExcel() {
        try {
            Workbook book = new XSSFWorkbook("C:\\apps\\M-KARTE\\documents\\testData\\company.xlsx");
            List<Company> ret = new ArrayList<>();
            Sheet sheet = book.getSheetAt(0);
            int lastRow = sheet.getLastRowNum();
            for(int i = 1; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                String code = row.getCell(0).getStringCellValue();
                if(StringUtils.isEmpty(code)) {
                    break;
                }
                Company c = new Company();
                c.code = code;
                c.name = row.getCell(1).getStringCellValue();
                c.postNo = row.getCell(2).getStringCellValue();
                c.address = row.getCell(3).getStringCellValue();
                c.tellNo = row.getCell(4).getStringCellValue();
                c.mngCmpCode = row.getCell(5).getStringCellValue();
                c.isSelf = "1".equals(row.getCell(6).getStringCellValue());
                ret.add(c);
            }
            return ret;
        } catch (IOException ex) {throw new RuntimeException(ex);}
    }
    
    private static class Company {
        private String code;
        private String name;
        private String postNo;
        private String address;
        private String tellNo;
        private String mngCmpCode;
        private boolean isSelf;
    }
}
