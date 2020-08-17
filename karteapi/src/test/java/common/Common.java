package common;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.close;
import com.codeborne.selenide.WebDriverRunner;
import com.google.gson.Gson;
import com.kmcj.karte.resources.authentication.Credential;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

/**
 *
 * @author t.takasaki
 */
public class Common {
    public static final String TEST_FILE_DIR_PATH = "C:/apps/M-KARTE/autotest";
    private static final String BASE_URL = "http://localhost/karte";
    private static final String API_BASE_URL = "http://localhost/ws/karte/api";
    /**
     * ブラウザ回りの初期セットアップを行う。<br>
     * ベースurlはlocalhost/karteとなる。<br>
     */
    public static void beforeClass() {
        beforeClass(BASE_URL);
    }
    
    public static void beforeClass(String baseUrl) {
        Configuration.browser = WebDriverRunner.CHROME;
        Configuration.baseUrl = baseUrl;
        Configuration.holdBrowserOpen = false;
        
        File testFileDir = new File(TEST_FILE_DIR_PATH);
        if(!testFileDir.exists()) {
            testFileDir.mkdirs();
            File sampleTxt = new File(testFileDir, "sample.txt");
            if(!sampleTxt.exists()) {
                try (FileWriter w = new FileWriter(sampleTxt)) {
                    w.write("This file is sample text for auto test.");
                } catch (IOException ex) {throw new RuntimeException(ex);}
            }
        }
        
        File reportDir = new File(testFileDir, "screenShot/" + dateTimeString());
        if(!reportDir.exists()) {
            reportDir.mkdirs();
        }
        Configuration.reportsFolder = reportDir.getAbsolutePath();
    }
    
    public static void afterClass() {
        close();
    }
    
    public static String dateTimeString() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return df.format(new Date());
    }
    
    /**
     * ブラウザでログイン画面を起動し、ログインを実行する。
     * @param loginid
     * @param pwd
     */
    public static void login(String loginid, String pwd) {
        open("/");
        $("#userid").val(loginid);
        $("#password").val(pwd);
        $("input[name='login']").click();
    }
    
    /**
     * APIのログインを実施し、トークンを取得する。
     * @param loginid
     * @param pwd
     * @return API token
     */
    @Deprecated
    public static String apiLogin(String loginid, String pwd) {
        return apiLogin(loginid, pwd, API_BASE_URL);
    }
    
    /**
     * ログイントークンを取得する。
     * @param loginid
     * @param pwd
     * @param baseurl
     * @return
     * @deprecated APIがhttpsに対応している場合、証明書によってはSSLHandshakeExceptionが発生する可能性があります。
     */
    @Deprecated
    public static String apiLogin(String loginid, String pwd, String baseurl) {
        try {
            Credential cred = new Credential();
            cred.setUserid(loginid);
            cred.setPassword(pwd);
            Response res = Request.Post(baseurl + "/authentication/login")
                .bodyString(new Gson().toJson(cred), ContentType.APPLICATION_JSON).execute();
            Credential resCred = new Gson().fromJson(new String(res.returnContent().asBytes(),StandardCharsets.UTF_8), Credential.class);
            if(resCred.isError()) {
                throw new RuntimeException(resCred.getErrorMessage());
            }
            return resCred.getToken();
        } catch (IOException ex) {throw new RuntimeException(ex);}
    }
    
    public static File getSampleTxt() {
        return new File(TEST_FILE_DIR_PATH + "/sample.txt");
    }
    
    /** jquery-ui製の確認ダイアログを閉じる
     * @param btnText。閉じる際にクリックするボタンのテキスト
     */
    public static void closeConfirmDialog(String btnText) {
        $$("button.ui-button").filter(Condition.text(btnText)).first().click();
    }
    
    /** jquery-ui製の確認ダイアログを閉じる。*/
    public static void closeConfirmDialog() {
        $("button.ui-button").click();
    }
}
