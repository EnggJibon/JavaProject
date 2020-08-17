package masterSetup;

import categoryItemSetting.CategoryItemSettingTest;
import common.Common;
import org.junit.BeforeClass;
import org.junit.Test;
import user.UserTest;
import static com.codeborne.selenide.Selenide.open;
import company.CompanyTest;
import downtimesetting.DowntimeSettingTest;
import inspect.InspectItemsTest;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import parts.PartsTest;
import workphase.WorkPhaseTest;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MasterSetupTest {

    
    @BeforeClass
    public static void beforeClass() {
        Common.beforeClass();
        // Scenario-1: Login M-Karte Web 
        Common.login("admin", "admin");
    }
    //Test cases executed alphabatical order
    @Test
    public void CategoryItemSettingTest(){
       //Scenario-2: Enter Category Item Setting Screen
       open("/php/S0011_MstChoiceCategory.php");
       CategoryItemSettingTest objCategoryItemSetting = new CategoryItemSettingTest();
       objCategoryItemSetting.createCategoryItemTestCases();
    }
    @Test
    public void DowntimeTest(){
       // Scenario-2: Enter WprkPhase Registration Screen
       open("/php/M1017_MachineDowntimeSetting.php"); 
       DowntimeSettingTest objDowntime = new DowntimeSettingTest();
       objDowntime.EnterDowntimeFromExcel();
    } 
    @Test
    public void EUserTest(){
       //Scenario-2: Open User Registration screen
       open("/php/S0005_MstUser.php");
       UserTest objUser = new UserTest();
       objUser.createUserTestCases();
    }          
    @Test
    public void FPartsTest(){
       //Scenario-1: Enter Parts Master Screen   
       open("/php/M0009_MstComponent.php");
       PartsTest objParts = new PartsTest();
       objParts.createPartsTestCases();
    }
       
    @Test
    public void GWorkphaseTest(){
       // Scenario-2: Enter WprkPhase Registration Screen    
       open("/php/M0017_MstWorkPhase.php");
       WorkPhaseTest objWorkPhase = new WorkPhaseTest();
       objWorkPhase.EnterWorkPhaseDataFromExcel();
    }
    
    @Test
    public  void companyTest() {
        CompanyTest ct = new CompanyTest();
        ct.addCompanies();
    }
    
    @Test
    public void inspFileTypeTest() {
        InspectItemsTest items = new InspectItemsTest();
        items.initInspectFileType();
        items.addInspItems("TpAuto01", "C:\\apps\\M-KARTE\\documents\\testData\\inspect\\inspItemTbl\\inspItemTbl.csv", "樹脂成型", true);
        items.addInspItems("TpAuto02", "C:\\apps\\M-KARTE\\documents\\testData\\inspect\\inspItemTbl\\inspItemTblMesOnly.csv", "樹脂成型", false);
        items.addInspItems("TpAuto03", "C:\\apps\\M-KARTE\\documents\\testData\\inspect\\inspItemTbl\\inspItemTblVisOnly.csv", "樹脂成型", false);
    }
}
