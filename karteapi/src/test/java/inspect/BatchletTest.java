/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inspect;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import com.codeborne.selenide.WebDriverRunner;
import static org.junit.Assert.assertTrue;
import static common.Common.closeConfirmDialog;
import common.Common;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 検証環境Domain2を対象として、Batchletによるデータ連携のテストを行います。
 * @author t.takasaki
 */
public class BatchletTest { 
    private static final String REL_TGT_BASE_ULR = "https://karte-dev.kmcweb.net/domain2/karte";
    
    @BeforeClass
    public static void beforeClass() {
        Common.beforeClass();
    }
    
    @AfterClass
    public static void afterClass() {
        Common.afterClass();
    }
    
    /**
     * 検査管理項目のバージョンアップを行い、domain2とデータ連携し、更新されていることを確認します。
     */
    @Test
    public void verUpInspItems() {
        Common.login("takasaki", "takasaki");
        
        open("/php/M3001_MstComponentInspectionItem.php");
        $("#btncreate").click();
        
        $("#componentCode").val("TpAuto04").pressTab();
        $("#inseptionType").selectOption("樹脂成型");
        $("#companyName").selectOption("資産テスト会社001");
        $("#upfile").sendKeys("C:\\apps\\M-KARTE\\autotest\\inspItemTbl\\inspItemTbl.csv");
        
        $(".ui-dialog-content p").click();//ダイアログが表示されるまで待つため、クリックを実施。
        assertTrue($$(".ui-dialog-content p").texts().contains("エラー件数:0"));
        closeConfirmDialog("OK");
        
        $("#btnapply").click();
        
        String url = WebDriverRunner.getWebDriver().getCurrentUrl();
        String itemID = url.replaceAll(".*inspectionItemId=", "");
        assertTrue(checkDataRelation10times(itemID));
    }
    
    /**
     * 該当の検査管理項目がデータ連携しているかを確認する。<br>
     * 20s置きに、最大10回試みる。
     */
    private boolean checkDataRelation10times(String itemid) {
        ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
        for(int i = 0; i < 10; i++) {
            ScheduledFuture<Boolean> f = es.schedule(() -> checkDataRelation(itemid), 20, TimeUnit.SECONDS);
            try {
                boolean result = f.get();
                if(result) {
                    return true;
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
    
    private boolean checkDataRelation(String itemid) {
        String domain2base = "https://karte-dev.kmcweb.net/domain2/karte";
        Configuration.baseUrl = domain2base;
        Common.login("takasaki", "takasaki");
        open("/php/M3001_MstComponentInspectionItem.php");
        $("#componentCode").val("TpAuto04");
        $("#incomingCompanyName").val("株式会社ＫＭＣ");
        $("#btnsearch").click();
        return $("#list").find("[aria-describedby='list_componentInspectionItemsTableId']").is(Condition.attribute("title", itemid));
    }
}
