package inspect;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import common.Common;
import static common.Common.closeConfirmDialog;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 検査管理項目関連のテスト
 * @author t.takasaki
 */
public class InspectItemsTest {
    @BeforeClass
    public static void beforeClass() {
        Common.beforeClass();
        Common.login("admin", "admin");
    }
    
    @AfterClass
    public static void afterClass() {
        Common.afterClass();
    }
    
    @Test
    public void initInspectFileType() {
        open("/php/T3018_ComponentInspectionFileTypeSet.php");
        $("#btnadd").click();
        $("#list01").find("input").val("樹脂成型").pressEnter();
        $("#btnregistration").click();
        closeConfirmDialog("OK");
        sleep(1000);
        $("#btnadd").click();
        $("#list01").find("input").val("板金").pressEnter();
        $("#btnregistration").click();
        closeConfirmDialog("OK");
        
        $("#btncheck").click();
        $("#btnadd2").click();
        $("#list03").find("input").val("初物");
        $("#list03").find("select").selectOption(0);
        $("#btnadd2").click();
        $("#list03").find("input").val("量産");
        $("#list03").find("select").selectOption(1);
        $("#btnadd2").click();
        $("#list03").find("input").val("工程内");
        $("#list03").find("select").selectOption(2);
        $("#btnsave").click();
        closeConfirmDialog("OK");
        
        $("#list02").find(".jqgrow").click();
        $("#list02").find("select").selectOption(1);
        $("#list02").find("select", 1).selectOption(1);
        $("#list02").find("select", 2).selectOption(1);
        $("#btnregistration").click();
        closeConfirmDialog("OK");
    }
    
//    @Test
    public void addInspItems(String componentCode, String csvPath, String inspectType, boolean isFirst) {
        if(!isFirst) {
            /* $("#btnapply").click()のajaxのコールバックでページ遷移を行うのが、次のopenをかき消してしまう。
             * openする前にajaxの処理が終わるのを待機する。*/
            sleep(1000);
        }
        open("/php/M3001_MstComponentInspectionItem.php");
        if(isFirst) {
            closeConfirmDialog("OK");
        }
        
        $("#btncreate").click();
        
        $("#componentCode").val(componentCode).pressTab();
        $("#inseptionType").selectOption(inspectType);
        $("#companyName").val("KMC Inc.").pressTab();
        $("#upfile").sendKeys(csvPath);
        
        $(".ui-dialog-content p").click();//ダイアログが表示されるまで待つため、クリックを実施。
        assertTrue($$(".ui-dialog-content p").texts().contains("エラー件数:0"));
        closeConfirmDialog("OK");
        
        $("#btnapply").click();
    }
}
