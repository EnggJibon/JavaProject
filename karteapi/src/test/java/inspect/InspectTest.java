package inspect;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Condition.text;
import static common.Common.closeConfirmDialog;
import static common.Common.dateTimeString;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.BeforeClass;
import common.Common;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

public class InspectTest {
    
    @BeforeClass
    public static void beforeClass() {
        Common.beforeClass();
        Common.login("admin", "admin");
    }
    
    @AfterClass
    public static void afterClass() {
        Common.afterClass();
    }
    
    /**
     * 初物検査を一通り流す。
     */
    @Test
    public void firstOrdinaly() {
        mainStream("初物", 4, 3, 2, 3);
    }
    
    /**
     * 量産検査を一通り流す。
     */
    @Test
    public void massOrdinaly() {
        mainStream("量産", 2, 3, 3, 3);
    }
    
    /**
     * 工程内検査を一通り流す。
     */
    @Test
    public void inProcOrdinaly() {
        createNewInspData("TpAuto01", "工程内");
        fillMeasureResult(1);
        
        $("#vBulkOK").click();
        refresh();
        Selenide.screenshot("工程内_出荷実施" + dateTimeString());
        $("#btnregister").click();
        
        /** 検査実施画面のURL*/
        String urlDoInspect = WebDriverRunner.getWebDriver().getCurrentUrl();
        
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=3"));//出荷検査確認
        Selenide.screenshot("工程内_出荷確認" + dateTimeString());
        $("#btnconfirm").click();
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=4"));//出荷検査承認
        Selenide.screenshot("工程内_出荷承認" + dateTimeString());
        $("#btnapprove").click();
        closeConfirmDialog("OK");
    }
    
    /**
     * 出荷検査の否認及び再実施。
     */
    @Test
    public void rejectAndRestart() {
        createNewInspData("TpAuto01", "初物");
        fillMeasureResult(1);
        
        $("#vBulkOK").click();
        refresh();
        Selenide.screenshot("初物" + "_出荷実施" + dateTimeString());
        $("#btnregister").click();
        
        /** 検査実施画面のURL*/
        String urlDoInspect = WebDriverRunner.getWebDriver().getCurrentUrl();
        
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=3"));//出荷検査確認
        Selenide.screenshot("初物" + "_出荷確認" + dateTimeString());
        $("#btndisavow").click();
        closeConfirmDialog("OK");
        
        open(urlDoInspect);//出荷検査確認
        $("#btnregister").click();
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=4"));//出荷検査承認
        Selenide.screenshot("初物" + "_出荷承認" + dateTimeString());
        $("#btndisavow").click();
        closeConfirmDialog("OK");
        
        open(urlDoInspect);//出荷検査確認
        $("#btnregister").click();
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=4"));//出荷検査承認
        Selenide.screenshot("初物" + "_出荷承認" + dateTimeString());
        $("#btnapprove").click();
        closeConfirmDialog("OK");
    }
    
    /**
     * 受入から開始。
     */
    @Test
    public void startFromIncoming() {
        open("/php/T3000_ComponentInspectionList.php?functionType=3");
        $("#btnincominginsert").click();
        
        $("#componentCode").val("TpAuto01").pressTab();
        SelenideElement cn = $("#companyName").val("KMC Inc.");
        cn.sendKeys(Keys.chord(Keys.ARROW_DOWN), Keys.chord(Keys.ARROW_DOWN), Keys.chord(Keys.ARROW_DOWN));
        cn.pressEnter();
        $("#sComponentInspectionPo").val("PO_" + dateTimeString());
        $("#itemNumber").val("Item01");
        $("#sManufactureLot").val(dateTimeString());
        $("#sFirstFlag").selectOption("初物");
        $("#sMachineQuantity").val("3").pressTab();
        $("#btncreate").click();
        closeConfirmDialog("OK");
        
        fillMeasureResult(1);
        $("#vBulkOK").click();
        confirmEachDocs();
        String urlDoInspect = WebDriverRunner.getWebDriver().getCurrentUrl();
        $("#btnregister").click();
        closeConfirmDialog("OK");
        
        //受入検査確認(帳票確認)
        open(urlDoInspect.replaceAll("act=2", "act=3"));
        $("#btn_doc_confirm").click();
        closeConfirmDialog("OK");
        //受入検査確認
        open(urlDoInspect.replaceAll("act=2", "act=3"));
        $("#btnconfirm").click();
        
        //受入検査承認
        open(urlDoInspect.replaceAll("act=2", "act=4").replaceAll("functionType=1", "functionType=3"));
        $("#btnapprove").click();
        closeConfirmDialog("OK");
    }
    
    /**
     * 測定結果CSVのインポートを実施する。
     */
    @Test
    public void resultCsvImp() {
        createNewInspData("TpAuto01", "初物");
        
        Selenide.executeJavaScript("$('#mbtnmeasurement1').mouseenter();");
        
        $("#mmeasurement1").sendKeys("C:\\apps\\M-KARTE\\documents\\testData\\inspect\\inspResultCsv\\InspResult.csv");
        refresh();
        getSlickCell("#measurementResultList", "measurement1", 1).shouldHave(text("11.1"));
    }
    
    /**
     * 測定検査のみの検査管理項目で一連の流れを確認する。
     */
    @Test
    public void onlyMeasure() {
        createNewInspData("TpAuto02", "初物");
        fillMeasureResult(1);
        $("#btnregister").click();
        
        /** 検査実施画面のURL*/
        String urlDoInspect = WebDriverRunner.getWebDriver().getCurrentUrl();
        
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=3"));//出荷検査確認
        $("#btnconfirm").click();
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=4"));//出荷検査承認
        $("#btnapprove").click();
        closeConfirmDialog("OK");
        
        //受入検査作成
        open(urlDoInspect.replaceAll("act=2", "act=1").replaceAll("functionType=1", "functionType=3"));
        $("#btncreate").click();
        closeConfirmDialog("OK");
        
        fillMeasureResult(1);
        confirmEachDocs();
        $("#btnregister").click();
        closeConfirmDialog("OK");
        
        //受入検査確認(帳票確認)
        open(urlDoInspect.replaceAll("act=2", "act=3").replaceAll("functionType=1", "functionType=3"));
        $("#btn_doc_confirm").click();
        closeConfirmDialog("OK");
        //受入検査確認
        open(urlDoInspect.replaceAll("act=2", "act=3").replaceAll("functionType=1", "functionType=3"));
        $("#btnconfirm").click();
        
        //受入検査承認
        open(urlDoInspect.replaceAll("act=2", "act=4").replaceAll("functionType=1", "functionType=3"));
        $("#btnapprove").click();
        closeConfirmDialog("OK");
    }
    
    /**
     * 目視検査のみの検査管理項目で一連の流れを確認する。
     */
    @Test
    public void onlyVisual() {
        createNewInspData("TpAuto03", "初物");
        $("#vBulkOK").click();
        $("#btnregister").click();
        
        /** 検査実施画面のURL*/
        String urlDoInspect = WebDriverRunner.getWebDriver().getCurrentUrl();
        
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=3"));//出荷検査確認
        $("#btnconfirm").click();
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=4"));//出荷検査承認
        $("#btnapprove").click();
        closeConfirmDialog("OK");
        
        //受入検査作成
        open(urlDoInspect.replaceAll("act=2", "act=1").replaceAll("functionType=1", "functionType=3"));
        $("#btncreate").click();
        closeConfirmDialog("OK");
        
        $("#vBulkOK").click();
        confirmEachDocs();
        $("#btnregister").click();
        closeConfirmDialog("OK");
        
        //受入検査確認
        open(urlDoInspect.replaceAll("act=2", "act=3").replaceAll("functionType=1", "functionType=3"));
        $("#btn_doc_confirm").click();
        
        //受入検査承認
        open(urlDoInspect.replaceAll("act=2", "act=4").replaceAll("functionType=1", "functionType=3"));
        $("#btnapprove").click();
        closeConfirmDialog("OK");
    }
    
    /**
     * 出荷検査作成、実施、確認、承認、受入検査作成、実施、確認、承認の一連の流れを実行する。<br>
     * その際、検査項目数をチェックする。
     * @param inspClass 検査区分
     * @param outMesCnt 出荷測定検査項目数
     * @param outVisCnt 出荷目視検査項目数
     * @param inMesCnt 受入測定検査項目数
     * @param inVisCnt 受入目視検査項目数
     */
    private void mainStream(String inspClass, int outMesCnt, int outVisCnt, int inMesCnt, int inVisCnt) {
        createNewInspData("TpAuto01", inspClass);
        checkMesListCnt(outMesCnt, 15);
        checkVisListCnt(outVisCnt, 9);
        fillMeasureResult(1);
        
        $("#vBulkOK").click();
        refresh();
        Selenide.screenshot(inspClass + "_出荷実施" + dateTimeString());
        $("#btnregister").click();
        
        /** 検査実施画面のURL*/
        String urlDoInspect = WebDriverRunner.getWebDriver().getCurrentUrl();
        
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=3"));//出荷検査確認
        Selenide.screenshot(inspClass + "_出荷確認" + dateTimeString());
        $("#btnconfirm").click();
        closeConfirmDialog("OK");
        
        open(urlDoInspect.replaceAll("act=2", "act=4"));//出荷検査承認
        Selenide.screenshot(inspClass + "_出荷承認" + dateTimeString());
        $("#btnapprove").click();
        closeConfirmDialog("OK");
        
        //受入検査作成
        open(urlDoInspect.replaceAll("act=2", "act=1").replaceAll("functionType=1", "functionType=3"));
        $("#btncreate").click();
        closeConfirmDialog("OK");
        
        checkMesListCnt(inMesCnt, 20);
        checkVisListCnt(inVisCnt, 13);
        fillMeasureResult(1);
        $("#vBulkOK").click();
        confirmEachDocs();
        refresh();
        Selenide.screenshot(inspClass + "_受入実施" + dateTimeString());
        $("#btnregister").click();
        closeConfirmDialog("OK");
        
        if(!"量産".equals(inspClass)) {
            //受入検査確認(帳票確認)
            open(urlDoInspect.replaceAll("act=2", "act=3").replaceAll("functionType=1", "functionType=3"));
            $("#btn_doc_confirm").click();
            closeConfirmDialog("OK");
        }
        
        //受入検査確認
        open(urlDoInspect.replaceAll("act=2", "act=3").replaceAll("functionType=1", "functionType=3"));
        $("#btnconfirm").click();
        
        //受入検査承認
        open(urlDoInspect.replaceAll("act=2", "act=4").replaceAll("functionType=1", "functionType=3"));
        Selenide.screenshot(inspClass + "_受入承認" + dateTimeString());
        $("#btnapprove").click();
        closeConfirmDialog("OK");
    }
    
    /** 新規の検査データを作成する。<br>
     * このメソッド終了後は検査実施画面が開かれた状態になる。
     */
    private void createNewInspData(String partCode, String inspClass) {
        open("/php/T3000_ComponentInspectionList.php");
        $("#btnadd").click();
        
        $("#componentCode").val(partCode).pressTab();
        SelenideElement cn = $("#companyName").val("KMC Inc.");
        cn.sendKeys(Keys.chord(Keys.ARROW_DOWN), Keys.chord(Keys.ARROW_DOWN), Keys.chord(Keys.ARROW_DOWN));
        cn.pressEnter();
        $("#sManufactureLot").val(dateTimeString());
        $("#sFirstFlag").selectOption(inspClass);
        $("#sMachineQuantity").val("3").pressTab();
        $("#btncreate").click();
        closeConfirmDialog("OK");
    }
    
    /**
     * 帳票毎の確認ステータスを「確認」にする。
     */
    private void confirmEachDocs() {
        $$("#pdfList tr.jqgrow").forEach(row -> {
            row.click();
            SelenideElement sel = row.find("select");
            if(sel.isDisplayed()) {
                sel.selectOption("確認");
            }
        });
    }
    
    /**
     * 規格値を測定値にコピーする。
     */
    private void fillMeasureResult(int sampleNum) {
        for(int rowidx = 0; rowidx < $$("#measurementResultList .slick-viewport-left .slick-row").size(); rowidx++) {
            SelenideElement dimCell = getSlickCell("#measurementResultList", "dimensionValue", rowidx);
            for(int i = 0; i < sampleNum; i++) {
                SelenideElement mesCell = getSlickCell("#measurementResultList", "measurement" + (i + 1), rowidx);
                mesCell.click();
                mesCell.find("input").val(dimCell.text());
                mesCell.find("input").pressEnter();
            }
        }
    }
    
    private SelenideElement getSlickCell(String gridSelector, String columnID, int rowidx) {
        int colIdx = getSlickColmnIdx(gridSelector, columnID);
        return $$(gridSelector + String.format(" .slick-cell.l%d.r%d", colIdx, colIdx)).get(rowidx);
    }
    
    private int getSlickColmnIdx(String gridSelector, String columnID) {
        SelenideElement grid = $(gridSelector);
        ElementsCollection headers = $$(gridSelector + " .slick-header-column");
        SelenideElement elm = headers.find(Condition.id(grid.attr("class").split(" ")[0] + columnID));
        return headers.indexOf(elm);
    }
    
    /**
     * 測定検査のグリッドの行数、列数をチェックする。
     */
    private void checkMesListCnt(int rowNum, int colNum) {
        $$("#measurementResultList .slick-header-column").shouldHaveSize(colNum);
        $$("#measurementResultList .slick-viewport-left .slick-row").shouldHaveSize(rowNum);
    }
    
    private void checkVisListCnt(int rowNum, int colNum) {
        $$("#visualResultList .slick-header-column").shouldHaveSize(colNum);
        $$("#visualResultList .slick-viewport-left .slick-row").shouldHaveSize(rowNum);
    }
}
