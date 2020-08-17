/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionary;
import com.kmcj.karte.resources.dictionary.MstDictionaryList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mold.MstMold;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
public class FileUtil {

    public static final String ENCRYPT_KEY = "1234567890123456";

    public static final String ENCRYPT_IV = "abcdefghijklmnop";

    public static final String SEPARATOR = File.separator;

    public static final String RETURN_CODE = "\r\n";

    /**
     * エンコード UTF-8
     */
    public static final String CHAR_CODE_UTF8 = "UTF-8";   
    
    public static final String CHAR_CODE_UTF8Q = "\"=?UTF-8?Q?\"";  
    public static final char[] HEX_CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B','C', 'D', 'E', 'F' }; 
    private static final String ENCODING_ISO8859 = "iso8859-1";
    public final static String DATE_FORMAT = "yyyy/MM/dd";
    public final static String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public final static String DATETIME_FORMAT_YYYYMMDDHHMM = "yyyy/MM/dd HH:mm";
    public final static String DATETIME_FORMAT_YYYYMMDDHHMMSSS = "yyyy/MM/dd HH:mm:ss.SSS";
    public final static String DATETIME_FORMAT_MMDDHHMM = "MM/dd HH:mm";
    public final static String DATE_FORMAT_DF = "yyyy-MM-dd";
    public final static String DATE_FORMAT_MONTH = "yyyy/MM";
    public final static String DATE_FORMAT_TO_MONTH = "yyyyMM";

    /**
     * Apeng update 20170605
     */
    public final static String FORMAT_NUMBER = "[0-9]{1,11}";
    public final static String FORMAT_URL = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";
    public final static String FORMAT_DECIMAL_PLACE = "^[+-]?[0-9]{0,16}([.][0-9]{0,4})?$";
    public final static String CHECK_IS_VALID_EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

    /**
     * OutputStream
     */
    private BufferedOutputStream out = null;

    private int writeBytes = 0;

    public boolean isNumber(String str) {
        // 判定するパターンを生成
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(str);
        // リターン
        return m.find();
    }

    public static boolean isNumberPlace(String str) {
        // 判定するパターンを生成
        Pattern p = Pattern.compile(FORMAT_NUMBER);
        Matcher m = p.matcher(str);
        // リターン
        return m.matches();
    }

    /**
     * Verify the decimal number two
     * 
     * @param str
     * @return
     */
    public static boolean isStrByDecimal(String str) {
        // 判定するパターンを生成
        Pattern p = Pattern.compile(FORMAT_DECIMAL_PLACE);
        Matcher m = p.matcher(str);
        // リターン
        return m.matches();
    }

    public static boolean isCheckUrl(String str) {
        // 判定するパターンを生成
        Pattern p = Pattern.compile(FORMAT_URL);
        Matcher m = p.matcher(str);
        // リターン
        return m.matches();
    }

    /**
     * does email effective
     * 
     * @param str
     * @return
     */
    public static boolean isValidEmail(String str) {
        // 判定するパターンを生成
        Pattern p = Pattern.compile(CHECK_IS_VALID_EMAIL);
        Matcher m = p.matcher(str);
        // リターン
        return m.matches();
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * パスを作成
     *
     * @param sPath
     */
    public void createPath(String sPath) {

        File path = new File(sPath);
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    /**
     *
     * @param kartePropertyService
     * @param importCsvFileUuid
     * @return
     */
    public static String getCsvFilePath(KartePropertyService kartePropertyService, String importCsvFileUuid) {

        StringBuffer csvSavePath = new StringBuffer(kartePropertyService.getDocumentDirectory());
        csvSavePath = csvSavePath.append(SEPARATOR).append(CommonConstants.CSV);
        FileUtil fu = new FileUtil();
        File file = new File(csvSavePath.toString());
        String str = fu.loadFileByFileName(file, importCsvFileUuid);
        return str;
    }

    /**
     *
     * @param kartePropertyService
     * @param importCsvFileUuid
     * @return
     */
    public static String getExcelFilePath(KartePropertyService kartePropertyService, String importCsvFileUuid) {

        StringBuffer csvSavePath = new StringBuffer(kartePropertyService.getDocumentDirectory());
        csvSavePath = csvSavePath.append(SEPARATOR).append(CommonConstants.EXCEL_FILE);
        FileUtil fu = new FileUtil();
        File file = new File(csvSavePath.toString());
        String str = fu.loadFileByFileName(file, importCsvFileUuid);
        return str;
    }

    /**
     *
     * @param lineCsv
     * @return
     */
    public static String[] splitCsvLine(String lineCsv) {

        return lineCsv.split(",", -1);
    }

    /**
     *
     * @param kartePropertyService
     * @param uuid
     * @return
     */
    public static String outCsvFile(KartePropertyService kartePropertyService, String uuid) {

        StringBuffer csvOutPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
        csvOutPath = csvOutPath.append(SEPARATOR).append(CommonConstants.CSV);
        FileUtil fu = new FileUtil();
        fu.createPath(csvOutPath.toString());

        csvOutPath = csvOutPath.append(SEPARATOR).append(uuid).append(CommonConstants.EXT_CSV);
        fu.createFile(csvOutPath.toString());
        return csvOutPath.toString();
    }
    
    /**
     *
     * @param kartePropertyService
     * @param uuid
     * @return
     */
    public static String outJsonFile(KartePropertyService kartePropertyService, String uuid) {

        StringBuffer csvOutPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
        csvOutPath = csvOutPath.append(SEPARATOR).append(CommonConstants.CSV);
        FileUtil fu = new FileUtil();
        fu.createPath(csvOutPath.toString());

        csvOutPath = csvOutPath.append(SEPARATOR).append(uuid).append(CommonConstants.EXT_JSON);
        fu.createFile(csvOutPath.toString());
        return csvOutPath.toString();
    }
    
    /**
     *
     * @param kartePropertyService
     * @param uuid
     * @return
     */
    public static String outTxtFile(KartePropertyService kartePropertyService, String uuid) {

        StringBuffer txtOutPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
        txtOutPath = txtOutPath.append(SEPARATOR).append(CommonConstants.DOC);
        FileUtil fu = new FileUtil();
        fu.createPath(txtOutPath.toString());

        txtOutPath = txtOutPath.append(SEPARATOR).append(uuid).append(CommonConstants.EXT_TXT);
        fu.createFile(txtOutPath.toString());
        return txtOutPath.toString();
    }

    /**
     *
     * @param kartePropertyService
     * @param uuid
     * @return
     * @throws IOException
     */
    public static String outExcelFile(KartePropertyService kartePropertyService, String uuid) throws IOException {

        StringBuffer csvOutPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
        csvOutPath = csvOutPath.append(SEPARATOR).append(CommonConstants.EXCEL_FILE);
        FileUtil fu = new FileUtil();
        fu.createPath(csvOutPath.toString());

        csvOutPath = csvOutPath.append(SEPARATOR).append(uuid).append(CommonConstants.EXT_EXCEL);
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        ExcelUtil.workbookToFile(workbook, csvOutPath.toString());
        return csvOutPath.toString();
    }

    /**
     *
     * @param kartePropertyService
     * @param logFileUuid
     * @return
     */
    public static String getLogFilePath(KartePropertyService kartePropertyService, String logFileUuid) {

        // log出力パスとファイルを作成
        FileUtil fu = new FileUtil();
        StringBuffer logOutputPath = new StringBuffer(kartePropertyService.getDocumentDirectory());
        logOutputPath = logOutputPath.append(SEPARATOR).append(CommonConstants.LOG);
        fu.createPath(logOutputPath.toString());
        logOutputPath = logOutputPath.append(SEPARATOR).append(logFileUuid).append(CommonConstants.EXT_LOG);
        String sbLogTemp = logOutputPath.toString();
        fu.createFile(sbLogTemp);
        return sbLogTemp;
    }

    /**
     * ファイルを作成
     *
     * @param sPath
     * @return
     */
    public boolean createFile(String sPath) {
        boolean flag = true;
        File file = new File(sPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * ファイルリネーム処理
     *
     * @param path
     * @param oldName
     * @param newName
     */
    public static void renameFile(String path, String oldName, String newName) {
        if (!oldName.equals(newName)) {
            if (!path.endsWith(SEPARATOR)) {
                path = path + SEPARATOR;
            }
            File oldFile = new File(path + oldName);
            
//            try {
//                MimeUtility.encodeText(newName, "utf-8", "B").replaceAll("\r","").replaceAll("\n","");
//                MimeUtility.decodeText(newName);
//                //newName = new String(newName.getBytes(CHAR_CODE_UTF8), ENCODING_ISO8859);
//            } catch (UnsupportedEncodingException ex) {
//                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
//                return;
//            }
            File newFile = new File(path + newName);
            if (!oldFile.exists()) {
                return;
            }
            if (!newFile.exists()) {
                oldFile.renameTo(newFile);
            }
        }
    }

    /**
     * ファイルコピー処理
     *
     * @param srcFileName
     * @param destFileName
     * @param overlay
     * @return
     */
    public static boolean fileChannelCopy(String srcFileName, String destFileName, boolean overlay) {
        File srcFile = new File(srcFileName);
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }

        File destFile = new File(destFileName);
        if (destFile.exists()) {
            if (overlay) {
                new File(destFileName).delete();
            }
        } else {
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    return false;
                }
            }
        }

        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(srcFile);
            outStream = new FileOutputStream(destFile);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (in != null) {
                    in.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 文字列をCSVファイルに出力 一行ごとに書き出し
     *
     * @param fileFuulPath
     * @param outputLine
     * @return
     */
    public boolean writeInfoToFile(String fileFuulPath, String outputLine) {
        boolean flag = true;
        PrintWriter pw = null;
        File file = new File(fileFuulPath);
        try {

            // ファイル先頭にBOMを付けます。
            FileOutputStream os = new FileOutputStream(file, true);
            if (file.length() <= 0) {
                os.write(0xef);
                os.write(0xbb);
                os.write(0xbf);
            }
            pw = new PrintWriter(new OutputStreamWriter(os, CHAR_CODE_UTF8));
            //pw.println(outputLine);
            pw.print(outputLine + "\r\n");

        } catch (IOException ex) {
            return flag;
        } finally {

            if (pw != null) {
                // ファイルを閉じる
                pw.close();
            }
        }
        return flag;
    }

    /**
     * ファイルチェック
     *
     * @param file
     * @return
     */
    private static boolean checkBeforeReadfile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.canRead()) {
                return true;
            }
        }

        return false;
    }

    /**
     * ファイルからデータを取込み
     *
     * @param filePath
     * @return
     */
    public List<String> readInfoFormFile(String filePath) {

        List<String> readList = new ArrayList<>();
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(filePath);

            br = new BufferedReader(
                    new InputStreamReader(skipUTF8BOM(new FileInputStream(new File(filePath)), CHAR_CODE_UTF8)));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) {
                    break;
                }
                readList.add(line);
            }
        } catch (FileNotFoundException e) {
            // System.out.println(e.getMessage());
        } catch (IOException e) {
            // System.out.println(e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                // System.out.println(e.getMessage());
            }
        }
        return readList;
    }
    
/**
     * ファイルからデータを取込み
     *
     * @param filePath
     * @return
     */
    public String readInfoFormTxtFile(String filePath) {

        StringBuffer sb = new StringBuffer();
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(filePath);

            br = new BufferedReader(
                    new InputStreamReader(skipUTF8BOM(new FileInputStream(new File(filePath)), CHAR_CODE_UTF8),CHAR_CODE_UTF8));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                // System.out.println(e.getMessage());
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param is
     * @param charSet
     * @return
     */
    public static InputStream skipUTF8BOM(InputStream is, String charSet) {
        if (!charSet.toUpperCase().equals(CHAR_CODE_UTF8)) {
            return is;
        }
        if (!is.markSupported()) {
            // マーク機能が無い場合BufferedInputStreamを被せる
            is = new BufferedInputStream(is);
        }
        is.mark(3); // 先頭にマークを付ける
        try {
            if (is.available() >= 3) {
                byte b[] = { 0, 0, 0 };
                is.read(b, 0, 3);
                if (b[0] != (byte) 0xEF || b[1] != (byte) 0xBB || b[2] != (byte) 0xBF) {
                    is.reset();// BOMでない場合は先頭まで巻き戻す
                }
            }
        } catch (IOException e) {
            // System.out.println(e.getMessage());
        }
        return is;
    }

    /**
     * fileupload ファイルアップロードする
     *
     * @param inputStream
     * @param saveToPath
     * @return
     */
    public boolean writeToFile(InputStream inputStream, String saveToPath) {
        boolean uploadFlag = false;
        int byteCount;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(saveToPath);

            byte[] bytes = new byte[1024];
            while ((byteCount = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, byteCount);
            }
            uploadFlag = true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            // System.out.println(e.getMessage());
            return uploadFlag;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // System.out.println(e.getMessage());
            return uploadFlag;
        } finally {
            try {
                inputStream.close();
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                // System.out.println(e.getMessage());
                return uploadFlag;
            }
        }
        return uploadFlag;

    }

    /**
     *
     * @param loadPath
     * @param fileUuid
     * @return
     */
    public String loadFileByFileName(File loadPath, String fileUuid) {
        String loadFile = "";
        if (!loadPath.exists()) {
            loadPath.mkdirs();
        }
        File[] fs = loadPath.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                loadFileByFileName(f, fileUuid);
            } else if (f.getName().contains(fileUuid)) {
                loadFile = f.getAbsolutePath();
                return loadFile;
            }
        }
        return loadFile;
    }

    /**
     *
     * @param value
     * @return
     */
    public boolean isNullCheck(String value) {
        return value == null || "".equals(value);
    }

    /**
     *
     * @param value
     * @param langth
     * @return
     */
    public boolean maxLangthCheck(String value, int langth) {
        return value.length() > langth;
    }

    /**
     * yyyy-MM-dd
     *
     * @param inDate
     * @return
     */
    public boolean dateCheck(String inDate) {
        if (inDate == null) {
            return false;
        }
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat(DATE_FORMAT);

        if (inDate.trim().length() != dateFormat.toPattern().length()) {
            return false;
        }
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    /**
     * yyyy/MM/dd String
     *
     * @param inDate
     * @return String
     */
    public String getDateParseForStr(String inDate) {
        if (inDate == null || inDate.equals("")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DF);
        Date date;
        try {
            date = sdf.parse(inDate);
        } catch (ParseException ex) {
            return "";
        }
        sdf = new SimpleDateFormat(DATE_FORMAT);
        String formattedTime = sdf.format(date);
        return formattedTime;
    }

    /**
     * yyyy/MM/dd Date
     *
     * @param inDate
     * @return String
     */
    public String getDateFormatForStr(Object inDate) {
        if (inDate == null || inDate.equals("")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String outDate = sdf.format(inDate);
        return outDate;
    }

    /**
     * 有効な日付チェック yyyy/MM/dd
     *
     * @param date
     * @return
     */
    public static boolean isValidDate(String date) {
        String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\/\\s]?((((0?[13578])|(1[02]))[\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\/\\s]?((((0?[13578])|(1[02]))[\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(date);
        return m.matches();
    }

    /**
     * yyyy/MM/dd HH:mm:ss.SSS Date
     *
     * @param inDate
     * @return String
     */
    public String getDateTimeFormatForStr(Object inDate) {
        if (inDate == null || inDate.equals("")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        String outDate = sdf.format(inDate);
        return outDate;
    }
    
    /**
     * yyyy/MM/dd HH:mm:ss Date
     *
     * @param inDate
     * @return String
     */
    public String getDateTimeMillFormatForStr(Object inDate) {
        if (inDate == null || inDate.equals("")) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_YYYYMMDDHHMMSSS);
        String outDate = sdf.format(inDate);
        return outDate;
    }

    /**
     * yyyy/MM/dd HH:mm:ss
     *
     * @param inDate
     * @return Date
     */
    public Date strDateTimeFormatToDate(String inDate) {
        if (inDate == null || inDate.equals("")) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        try {
            return sdf.parse(inDate);

        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * yyyy/MM/dd Date
     *
     * @param inDate
     * @return String
     */
    public Date getDateParseForDate(String inDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date outDate;
        try {
            outDate = sdf.parse(inDate);
        } catch (ParseException ex) {
            return null;
        }
        return outDate;
    }

    /**
     * "yyyy/MM/dd HH:mm:ss Date
     *
     * @param inDate
     * @return String
     */
    public Date getDateTimeParseForDate(String inDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        Date outDate;
        try {
            outDate = sdf.parse(inDate);
        } catch (ParseException ex) {
            return null;
        }
        return outDate;
    }

    /**
     * specifiedDayの前日
     *
     * @param specifiedDay
     * @return
     */
    public Date getSpecifiedDayBefore(Date specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date;
        c.setTime(specifiedDay);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        String dayBefore = new SimpleDateFormat(DATE_FORMAT).format(c.getTime());
        java.text.DateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
        try {
            date = fmt.parse(dayBefore);
        } catch (ParseException ex) {
            return null;
        }
        return date;
    }

    /**
     *
     * @param hLineNo
     * @param dLineNoint
     * @param hKeyName
     * @param dKeyValue
     * @param hError
     * @param error
     * @param result
     * @param msg
     * @return
     */
    public String outValue(String hLineNo, int dLineNoint, String hKeyName, String dKeyValue, String hError, int error,
            String result, String msg) {
        StringBuffer sb = new StringBuffer();
        sb = sb.append(hLineNo).append(":").append(dLineNoint).append(", ");
        sb = sb.append(hKeyName).append(":").append(getStr(dKeyValue)).append(", ");

        if (error == 1) {
            sb = sb.append(hError).append(":").append(error).append(", ");
            sb = sb.append(result).append(":").append(msg);
        } else {
            sb = sb.append(hError).append(":").append(error).append(", ");
            sb = sb.append(result).append(":").append(msg);
        }
        return sb.toString();

    }
    
    /**
     *
     * @param LineNo
     * @param keyName
     * @param result
     * @param error
     * @return
     */
    public String outLogValue(String LineNo, String keyName, String result, String error) {
        StringBuffer sb = new StringBuffer();
        sb = sb.append(LineNo).append(",").append(keyName).append(",").append(result);
        if (error != null && error.length() > 0) {
            sb.append(",").append(error);
        }
        return sb.toString();
    }

    /**
     *
     * @param date
     * @return
     */
    public static String dateFormat(Date date) {
        if(date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     *
     * @param date
     * @return
     */
    public static String dateFormatToMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_MONTH);
        return sdf.format(date);
    }

    /**
     *
     * @param date
     * @return
     */
    public static String dateFormatToMonthStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TO_MONTH);
        return sdf.format(date);
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String getLogFileName(String fileName) {
        java.text.DateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = format2.format(new Date());
        StringBuffer strFileName = new StringBuffer();
        strFileName = strFileName.append(fileName).append("_").append(format).append(".log");
        return strFileName.toString();
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String getCsvFileName(String fileName) {
        java.text.DateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = format2.format(new Date());
        StringBuffer strFileName = new StringBuffer();
        strFileName = strFileName.append(fileName).append("_").append(format).append(".csv");
        return strFileName.toString();
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String getTxtFileName(String fileName) {
        java.text.DateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = format2.format(new Date());
        StringBuffer strFileName = new StringBuffer();
        strFileName = strFileName.append(fileName).append("_").append(format).append(".txt");
        return strFileName.toString();
    }
    
    /**
    *
    * @param fileName
    * @return
    */
   public static String getExcelFileName(String fileName) {
       java.text.DateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
       String format = format2.format(new Date());
       StringBuffer strFileName = new StringBuffer();
       strFileName = strFileName.append(fileName).append("_").append(format).append(".xlsx");
       return strFileName.toString();
   }

    /* Add by chenyihang FROM 20161022 SATRT */
    /**
     * ファイルをオープンし、ヘッダ行を読み取ります
     *
     * @param outputFile
     * @throws IOException
     *             入出力例外
     */
    public final void openCsvFile(File outputFile) throws IOException {
        closeCsvFile();
        out = new BufferedOutputStream(new FileOutputStream(outputFile));
    }

    /**
     * ファイルをクローズします.
     *
     */
    public final void closeCsvFile() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
            } finally {
                out = null;
            }
        }
        writeBytes = 0;
    }

    /**
     * エスケープ処理 文字列全体を"（ダブルクォーテーション）で囲む
     *
     * @param value
     *            カラム値
     * @return 処理後カラム値
     */
    public String escape(final String value) {
        if (value == null) {
            return "\"\"";
        }
        String ret = value.replaceAll("\"", "\"\"");
        return "\"" + ret + "\"";
    }

    /**
     *
     *
     * @param value
     *            カラム値
     * @return 処理後カラム値
     */
    public String blankEscape(final String value) {
        String ret = "";
        if (!isNullCheck(value)) {
            if (value.contains("\"")) {
                ret = value.replaceAll("\"", "");
            } else {
                ret = value;
            }
        }
        return ret;
    }

    /**
     * ファイルの出力
     *
     * @throws IOException
     *             ファイルの出力に失敗した場合
     */
    public void flush() throws IOException {
        out.flush();
    }

    /**
     * 出力バイト数の取得
     *
     * @return
     */
    public int getWriteBytes() {
        return writeBytes;
    }

    /**
     * ファイル出力処理
     *
     * @param line
     *            出力レコード
     * @throws IOException
     *             入出力例外
     */
    public void writeLine(final List line) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean b = false;

        byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
        out.write(bom);

        for (int i = 0; i < line.size(); i++) {
            String s = line.get(i).toString();
            if (b) {
                sb.append(',');
            }
            sb.append(escape(s));
            b = true;
        }
        sb.append(RETURN_CODE);
        String p = sb.toString();

        byte[] bb = p.getBytes(CHAR_CODE_UTF8);

        out.write(bb);
        writeBytes += bb.length;
    }

    /**
     *
     * @param value
     * @return
     */
    public static String getDecode(String value) {
        try {
            value = URLDecoder.decode(value, CHAR_CODE_UTF8);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    /**
     *
     * @param value
     * @return
     */
    public static String getEncod(String value) {
        try {
            value = URLEncoder.encode(value, CHAR_CODE_UTF8);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public static String getFileName(String filename) 
    {  
        try
           {
                byte[] bytes = filename.getBytes("UTF-8");
                StringBuilder buff = new StringBuilder(bytes.length);
                buff.append("=?UTF-8?Q?");
                for (byte b : bytes) 
                {
                    int unsignedByte = b & 0xFF; //we also can use string.format         
                    buff.append('=').append(HEX_CHARS[unsignedByte >> 4]).append(HEX_CHARS[unsignedByte & 0xF]);
                }
                String hexchar = buff.append("?=").toString();
                return hexchar;
                
            }
        catch(UnsupportedEncodingException ex)
            {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        return filename;
    }
    
    public static String getValueByUTF8(String value) {
        String reValue = "";
        try {
            if (null != value) {
                reValue = new String(value.getBytes("iso-8859-1"), CHAR_CODE_UTF8);
            }
        } catch (UnsupportedEncodingException ex) {

            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
            return reValue;
        }
        return reValue;
    }

    /**
     *
     * @param myString
     * @return
     */
    public static String getRegex(String myString) {

        // 4 different combinaisons
        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
        Matcher m = CRLF.matcher(myString);
        if (m.find()) {
            return m.replaceAll("\r\n");
        }
        return myString;
    }

    public static String removeReturnCode(String myString) {
        Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
        Matcher m = CRLF.matcher(myString);
        if (m.find()) {
            return m.replaceAll("");
        }
        return myString;
    }
    
    /**
     * 指定ＵＲＬへPOSTリクエスト
     *
     * @param urlStr
     * @param paramMap
     * @return
     */
    public static Credential sendPost(String urlStr, Credential paramMap) {
        Gson gson = new Gson();
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        BufferedReader reader = null;
        Credential resCredential = new Credential();
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);

            connection.setRequestProperty("Accept-Charset", CHAR_CODE_UTF8);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.connect();

            // POSTリクエスト
            out = new DataOutputStream(connection.getOutputStream());
            String jsonString = gson.toJson(paramMap);

            out.writeBytes(jsonString);
            out.flush();

            // 結果を取得
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHAR_CODE_UTF8));
            String lines;
            StringBuilder sb = new StringBuilder("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(CHAR_CODE_UTF8), CHAR_CODE_UTF8);

                sb.append(lines);
            }

            resCredential = gson.fromJson(sb.toString(), Credential.class);

        } catch (Exception e) {
            resCredential.setError(true);
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (null != reader) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException ex) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (null != connection) {
                // アクセスを切り
                connection.disconnect();
                connection = null;
            }
        }

        return resCredential;
    }

    /**
     * 指定ＵＲＬへＧＥＴでデータを取得
     *
     * @param urlStr
     * @param apiToken
     * @return
     */
    public static String getDataGet(String urlStr, String apiToken) {
        String result = "";
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Accept-Charset", CHAR_CODE_UTF8);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("APITOKEN", apiToken);
            connection.connect();

            // 結果を取得
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHAR_CODE_UTF8));
                String lines;
                StringBuilder sb = new StringBuilder("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(CHAR_CODE_UTF8), CHAR_CODE_UTF8);
                    sb.append(lines);
                }
                result = sb.toString();
            }
        } catch (Exception e) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException ex) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (null != connection) {
                connection.disconnect();
                connection = null;
            }
        }

        // アクセスを切り
        return result;
    }
    
    /**
     * urlStrのGetAPIをコールし、結果をオブジェクトで取得します。
     * @param <T>
     * @param urlStr
     * @param apiToken
     * @param cls
     * @return 
     */
    public static <T> T callGetAPI(String urlStr, String apiToken, Class<T> cls) {
        return callGetAPIByType(urlStr, apiToken, cls);
    }
    
    private static <T> T callGetAPIByType(String urlStr, String apiToken, Type t) {
        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection) new URL(urlStr).openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Accept-Charset", CHAR_CODE_UTF8);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("APITOKEN", apiToken);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                try(JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream(), CHAR_CODE_UTF8))) {
                    return gson.fromJson(reader, t);
                }
            } else {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, "API call failed. URL: {0}", urlStr);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(connection != null) {
                    connection.disconnect();
                }
            } catch(Exception e) {}
        }
        return null;
    }
    
    /**
     * urlStrのGetAPIをコールし、結果をオブジェクトで取得します。<br>
     * オブジェクトがGenericsパラメータを持つ場合、Class指定のcallGetAPIでは正しくデシリアライズされません。<br>
     * その場合にTypeTokenを用いた本メソッドを使用してください。<br>
     * TypeTokenは下記のように作成します。<br>
     * TypeToken&lt;ObjectResponse&lt;List&lt;String&gt;&gt;&gt; typeToken = new TypeToken&lt;ObjectResponse&lt;List&lt;String&gt;&gt;&gt;() {};
     * @param <T>
     * @param urlStr
     * @param apiToken
     * @param typeToken
     * @return 
     */
    public static <T> T callGetAPI(String urlStr, String apiToken, TypeToken<T> typeToken) {
        return callGetAPIByType(urlStr, apiToken, typeToken.getType());
    }

    /**
     * 指定ＵＲＬへＧＥＴでFileを取得
     *
     * @param urlStr
     * @param fileName
     * @param apiToken
     * @param filePath
     */
    public static void getFileGet(String urlStr, String fileName, String apiToken, String filePath) {
        StringBuilder path = new StringBuilder(filePath);
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        FileOutputStream fos = null;
        HttpURLConnection connection = null;
        URL url;
        try {
            url = new URL(urlStr);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Accept-Charset", CHAR_CODE_UTF8);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/octet-stream");
            connection.setRequestProperty("APITOKEN", apiToken);
            connection.connect();
            // 結果を取得
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();

                path.append(File.separator);
                path.append(fileName);

                bufferedInputStream = new BufferedInputStream(is);

                byte[] b = new byte[1];
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path.toString()));
                while (bufferedInputStream.read(b) != -1) {
                    bufferedOutputStream.write(b);
                }
                bufferedOutputStream.flush();

            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (null != bufferedInputStream) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if (null != bufferedOutputStream) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
                }
            }

            // アクセスを切り
            if (null != connection) {
                connection.disconnect();
            }
        }
    }

    /**
     * 指定ＵＲＬへPOSTリクエスト
     *
     * @param urlStr
     * @param apiToken
     * @param paramData
     * @return
     */
    public static String sendPost(String urlStr, String apiToken, Object paramData) {
        HttpURLConnection connection = null;
        BufferedWriter out = null;
        BufferedReader reader = null;
        String resultJson = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);

            connection.setRequestProperty("Accept-Charset", CHAR_CODE_UTF8);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("APITOKEN", apiToken);
            connection.connect();

            // POSTリクエスト
            out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), CHAR_CODE_UTF8));

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            try (JsonWriter writer = new JsonWriter(out)) {
                gson.toJson(paramData, paramData.getClass(), writer);
            }

            // 結果を取得
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHAR_CODE_UTF8));
                String lines;
                StringBuilder sb = new StringBuilder("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(CHAR_CODE_UTF8), CHAR_CODE_UTF8);
                    sb.append(lines);
                }
                resultJson = sb.toString();
            } else {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, "HTTP status code was {0}",
                        connection.getResponseCode());
            }

        } catch (Exception e) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (null != reader) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException ex) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (null != connection) {
                // アクセスを切り
                connection.disconnect();
                connection = null;
            }
        }

        return resultJson;
    }
    
    public static <T> T sendPost(String urlStr, String apiToken, Object paramData, Class<T> cls) throws IOException {
        HttpURLConnection conn = createPostConnection(urlStr, apiToken);
        try {
            conn.connect();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), CHAR_CODE_UTF8));
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            try (JsonWriter writer = new JsonWriter(out)) {
                gson.toJson(paramData, paramData.getClass(), writer);
            }
            
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException(String.format("%s returned http code %d.", urlStr, conn.getResponseCode()));
            }
            try(JsonReader reader = new JsonReader(new InputStreamReader(conn.getInputStream(), CHAR_CODE_UTF8))) {
                return gson.fromJson(reader, cls);
            }
        } finally {
            try {
                conn.disconnect();
            } catch (Exception ex) {}
        }
    }
    
    private static HttpURLConnection createPostConnection(String urlStr, String apiToken) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Accept-Charset", CHAR_CODE_UTF8);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("APITOKEN", apiToken);
        return conn;
    }

    /**
     * 暗号化メソッド
     *
     * @param text
     *            暗号化する文字列
     * @return 暗号化文字列
     */
    public static String encrypt(String text) {
        // 変数初期化
        String strResult = null;

        try {
            // 文字列をバイト配列へ変換
            byte[] byteText = text.getBytes(CHAR_CODE_UTF8);

            // 暗号化キーと初期化ベクトルをバイト配列へ変換
            byte[] byteKey = ENCRYPT_KEY.getBytes(CHAR_CODE_UTF8);
            byte[] byteIv = ENCRYPT_IV.getBytes(CHAR_CODE_UTF8);

            // 暗号化キーと初期化ベクトルのオブジェクト生成
            SecretKeySpec key = new SecretKeySpec(byteKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(byteIv);

            // Cipherオブジェクト生成
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Cipherオブジェクトの初期化
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            // 暗号化の結果格納
            byte[] byteResult = cipher.doFinal(byteText);

            // Base64へエンコード
            strResult = Base64.encode(byteResult);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        // 暗号化文字列を返却
        return strResult;
    }

    /**
     * 復号化メソッド
     *
     * @param text
     *            復号化する文字列
     * @return 復号化文字列
     */
    public static String decrypt(String text) {
        // 変数初期化
        String strResult = null;

        try {
            // Base64をデコード
            byte[] byteText = Base64.decode(text);

            // 暗号化キーと初期化ベクトルをバイト配列へ変換
            byte[] byteKey = ENCRYPT_KEY.getBytes(CHAR_CODE_UTF8);
            byte[] byteIv = ENCRYPT_IV.getBytes(CHAR_CODE_UTF8);

            // 復号化キーと初期化ベクトルのオブジェクト生成
            SecretKeySpec key = new SecretKeySpec(byteKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(byteIv);

            // Cipherオブジェクト生成
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Cipherオブジェクトの初期化
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            // 復号化の結果格納
            byte[] byteResult = cipher.doFinal(byteText);

            // バイト配列を文字列へ変換
            strResult = new String(byteResult, CHAR_CODE_UTF8);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        // 復号化文字列を返却
        return strResult;
    }

    public static void SSL() throws Exception {
        // HTTPS で追加 S
        System.setProperty("jsse.enableSNIExtension", "false");
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // HTTPS で追加 E

    }

    /**
     * FileLink 置換処理
     *
     * @param mstChoiceService
     * @param langId
     * @param tmpLnk
     * @param moldId
     * @param moldName
     * @param moldType
     * @param mainAssetNo
     * @return
     */
    public static String getFileLinkString(MstChoiceService mstChoiceService, String langId, String tmpLnk,
            String moldId, String moldName, String moldType, String mainAssetNo) {
        if (null != tmpLnk && !"".equals(tmpLnk)) {

            if (tmpLnk.contains("%mold_id%")) {
                if (null == moldId) {
                    moldId = "";
                }
                tmpLnk = tmpLnk.replace("%mold_id%", moldId);
            }

            if (tmpLnk.contains("%mold_name%")) {
                if (null == moldName) {
                    moldName = "";
                }
                tmpLnk = tmpLnk.replace("%mold_name%", moldName);
            }

            if (tmpLnk.contains("%mold_type_seq%")) {
                if (null == moldType) {
                    moldType = "";
                }
                tmpLnk = tmpLnk.replace("%mold_type_seq%", moldType);
            }
            if (tmpLnk.contains("%main_asset_no%")) {
                if (null == mainAssetNo) {
                    mainAssetNo = "";
                }
                tmpLnk = tmpLnk.replace("%main_asset_no%", mainAssetNo);
            }

            if (tmpLnk.contains("%mold_type_name%")) {
                List<MstChoice> cs = mstChoiceService.getChoice(langId, "mst_mold.mold_type").getMstChoice();
                tmpLnk = tmpLnk.replace("%mold_type_name%", "");
                if (null != cs && !cs.isEmpty()) {
                    for (int j = 0; j < cs.size(); j++) {
                        if (cs.get(j).getMstChoicePK().getSeq().equals(moldType)) {
                            if (null == cs.get(j).getChoice()) {
                                tmpLnk = tmpLnk.replace("%mold_type_name%", "");
                            } else {
                                tmpLnk = tmpLnk.replace("%mold_type_name%", cs.get(j).getChoice());
                            }
                            break;
                        }
                    }
                }
            }

            if (tmpLnk.contains("%machine_id%")) {
                if (null == moldId) {
                    moldId = "";
                }
                tmpLnk = tmpLnk.replace("%machine_id%", moldId);
            }

            if (tmpLnk.contains("%machine_name%")) {
                if (null == moldName) {
                    moldName = "";
                }
                tmpLnk = tmpLnk.replace("%machine_name%", moldName);
            }

            if (tmpLnk.contains("%machine_type_seq%")) {
                if (null == moldType) {
                    moldType = "";
                }
                tmpLnk = tmpLnk.replace("%machine_type_seq%", moldType);
            }

            if (tmpLnk.contains("%machine_type_name%")) {
                List<MstChoice> cs = mstChoiceService.getChoice(langId, "mst_machine.machine_type").getMstChoice();
                tmpLnk = tmpLnk.replace("%machine_type_name%", "");
                if (null != cs && !cs.isEmpty()) {
                    for (int j = 0; j < cs.size(); j++) {
                        if (cs.get(j).getMstChoicePK().getSeq().equals(moldType)) {
                            if (null == cs.get(j).getChoice()) {
                                tmpLnk = tmpLnk.replace("%machine_type_name%", "");
                            } else {
                                tmpLnk = tmpLnk.replace("%machine_type_name%", cs.get(j).getChoice());
                            }
                            break;
                        }
                    }
                }
            }
        }
        return tmpLnk;
    }

    /**
     * 金型が外部管理しているかを確認
     *
     * @param entityManager
     * @param mstDictionaryService
     * @param moldId
     * @param loginUser
     * @return
     */
    public static BasicResponse checkExternal(EntityManager entityManager, MstDictionaryService mstDictionaryService,
            String moldId, LoginUser loginUser) {
        Query query = entityManager.createNamedQuery("MstMold.findByMoldId");
        query.setParameter("moldId", moldId);
        BasicResponse response = new BasicResponse();
        String companyId;
        try {
            MstMold mstMold = (MstMold) query.getSingleResult();
            if (mstMold.getCompanyId() != null && !"".equals(mstMold.getCompanyId())) {
                companyId = mstMold.getCompanyId();
                String sql = "SELECT m FROM MstExternalDataGetSetting m WHERE m.companyId = :companyId AND m.validFlg = 1 AND m.mstCompany.myCompany = 0 ";
                Query checkquery = entityManager.createQuery(sql);
                checkquery.setParameter("companyId", companyId);
                List lists = checkquery.getResultList();
                if (lists != null && lists.size() > 0) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(
                            mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_ext_edit"));
                    return response;
                }
            }
        } catch (NoResultException e) {
            response.setError(false);
            // response.setErrorCode(ErrorMessages.E201_APPLICATION);
            // response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(),
            // "msg_error_no_processing_record"));
            return response;
        }
        response.setError(false);
        return response;
    }

    /**
     * 設備が外部管理しているかを確認
     *
     * @param entityManager
     * @param mstDictionaryService
     * @param machineId
     * @param companyId
     * @param loginUser
     * @return
     */
    public static BasicResponse checkMachineExternal(EntityManager entityManager,
            MstDictionaryService mstDictionaryService, String machineId, String companyId, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        // 該当設備ＩＤにより、会社ＩＤを取得する
        if (companyId == null || "".equals(companyId)) {
            if (machineId != null && !"".equals(machineId)) {
                Query query = entityManager.createNamedQuery("MstMachine.findByMachineId");
                query.setParameter("machineId", machineId);
                try {
                    MstMachine mstMachine = (MstMachine) query.getSingleResult();
                    companyId = mstMachine.getCompanyId();
                } catch (NoResultException e) {
                    response.setError(false);
                    // response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    // response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(),
                    // "mst_error_record_not_found"));
                    return response;
                }
            }
        }
        // 該当設備の会社が外部管理しているかどうか、チェックする
        if (companyId != null && !"".equals(companyId)) {
            String sql = "SELECT m FROM MstExternalDataGetSetting m LEFT JOIN FETCH m.mstCompany WHERE m.mstCompany.id = :companyId AND m.validFlg = 1 AND m.mstCompany.myCompany = 0 ";
            Query checkquery = entityManager.createQuery(sql);
            checkquery.setParameter("companyId", companyId);
            List lists = checkquery.getResultList();
            if (lists != null && lists.size() > 0) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(
                        mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_ext_edit"));
                return response;
            }
        }
        response.setError(false);
        return response;
    }

    public static BasicResponse checkMachineExternalByCompanyCode(EntityManager entityManager,
            MstDictionaryService mstDictionaryService, String companyCode, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        // 該当設備の会社が外部管理しているかどうか、チェックする
        if (companyCode != null && !"".equals(companyCode)) {
            String sql = "SELECT m FROM MstExternalDataGetSetting m LEFT JOIN FETCH m.mstCompany WHERE m.mstCompany.companyCode = :companyCode AND m.validFlg = 1 AND m.mstCompany.myCompany = 0 ";
            Query checkquery = entityManager.createQuery(sql);
            checkquery.setParameter("companyCode", companyCode);
            List lists = checkquery.getResultList();
            if (lists != null && lists.size() > 0) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(
                        mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_ext_edit"));
                return response;
            }
        }
        response.setError(false);
        return response;
    }

    /**
     * Double Check
     *
     * @param str
     * @return
     */
    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * Deciaml Type Validate
     *
     * @param str
     * @param left
     * @param right
     * @return
     */
    public boolean validateDeciamlLen(String str, int left, int right) {
        boolean isTrue = false;
        if (str.contains(".")) {
            isTrue = ((str.substring(0, str.indexOf(".")).length()
                    + str.substring(str.indexOf(".") + 1).length()) <= left
                    && (str.substring(str.indexOf(".") + 1).length() <= right));
        } else {
            isTrue = (str.length() <= left);
        }
        return isTrue;
    }

    /**
     * 二つの日差
     *
     * @param d1
     * @param d2
     * @return d2-d1の天数
     */
    public static int daysBetween(Date d1, Date d2) {
        int days = (int) ((d2.getTime() - d1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 時間の差を求める
        long diff = endDate.getTime() - nowDate.getTime();
        // 天の差
        // long day = diff / nd;
        // 時刻の差
        long hour = diff % nd / nh;
        // 分の差
        long min = diff % nd % nh / nm;
        // 計算し戻す
        return hour + ":" + min;
    }

    /**
     * 両日付の分の差を取得
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static Integer getDatePoorMinute(Date endDate, Date nowDate) {

        long nm = 1000 * 60;

        // 分の差を求める
        long diff = endDate.getTime() / nm - nowDate.getTime() / nm;

        // 計算し戻す
        return Integer.parseInt(diff + "");
    }

    /**
     * 両日付の差を取得
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static int getDaysOfTwo(Date fromDate, Date toDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fromDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(toDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    /**
     * 時刻を秒に換算
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static long getDatePoorSec(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 二つ時間の差を秒に換算
        long diff = endDate.getTime() - nowDate.getTime();

        long sec = diff % nd % nh % nm / ns;
        return sec;
    }

    /**
     *
     * @param param
     * @return
     */
    public static Date dateTime(String param) {

        Date date = new Date();
        String[] arrDateTime = param.split(" ");
        try {
            if (param.length() > 10) {

                if (arrDateTime.length > 1) {
                    String[] arrTime = arrDateTime[1].split(":");
                    switch (arrTime.length) {
                    case 1:
                        date = getDate(arrDateTime[0], arrTime[0], "00", "00");
                        break;
                    case 2:
                        date = getDate(arrDateTime[0], arrTime[0], arrTime[1], "00");
                        break;
                    case 3:
                        date = getDate(arrDateTime[0], arrTime[0], arrTime[1], arrTime[2]);
                        break;
                    default:
                        date = getDate(arrDateTime[0]);
                        break;
                    }
                } else {
                    date = getDate(arrDateTime[0]);
                }
            }
        } catch (ParseException e) {
            date = getDate(arrDateTime[0]);
        }
        return date;
    }

    public static Date getDate(String date, String hour, String minute, String second) throws ParseException {
        String result = (hour.length() == 1 ? ("0" + hour) : hour) + ":"
                + (minute.length() == 1 ? ("0" + minute) : minute) + ":"
                + (second.length() == 1 ? ("0" + second) : second);
        return parse(date + " " + result);
    }

    public static Date getDate(String date) {
        try {
            return parse(date + " 08:00:00");
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Date parse(String strDate) throws ParseException {
        return (strDate == null || "".equals(strDate)) ? null : parse(strDate, getDatePattern());
    }

    public static Date parse(String strDate, String datePattern) throws ParseException {
        return (strDate == null || "".equals(strDate)) ? null : new SimpleDateFormat(datePattern).parse(strDate);
    }

    public static String getDatePattern() {
        return DATETIME_FORMAT;
    }

    /**
     *
     * @param attType
     * @param strAttrValue
     * @return
     */
    public boolean checkSpecAttrType(int attType, String strAttrValue) {
        switch (attType) {
        case CommonConstants.ATTRIBUTE_TYPE_NUMBER:
            if (!isNullCheck(strAttrValue)) {
                if (maxLangthCheck(strAttrValue, 11)) {
                    return false;
                }
            }
            break;
        case CommonConstants.ATTRIBUTE_TYPE_DATE:
            if (!isNullCheck(strAttrValue)) {
                if (!isValidDate(strAttrValue)) {
                    return false;
                }
            }
            break;
        case CommonConstants.ATTRIBUTE_TYPE_TEXT:
        case CommonConstants.ATTRIBUTE_TYPE_STATICLINK:
            if (!isNullCheck(strAttrValue)) {
                if (maxLangthCheck(strAttrValue, 256)) {
                    return false;
                }
            }
            break;
        default:
            break;
        }

        return true;
    }

    /**
     * 辞書Keyにより複数取得
     *
     * @param mstDictionaryService
     * @param langId
     * @param dictKeyList
     * @return
     */
    public static Map<String, String> getDictionaryList(MstDictionaryService mstDictionaryService, String langId,
            List<String> dictKeyList) {
        Map<String, String> keyMap = new HashMap<>();
        MstDictionaryList dictKeies = mstDictionaryService.getDictionaryList(langId, dictKeyList);
        for (MstDictionary mstDictionary : dictKeies.getMstDictionary()) {
            keyMap.put(mstDictionary.getDictKey(), mstDictionary.getDictValue());
        }

        return keyMap;
    }

    /**
     * 文字列→数字に変換
     *
     * @param para
     * @return
     */
    public static Integer getStrToNum(String para) {

        if (StringUtils.isEmpty(para)) {
            return 0;
        } else {

            return Integer.valueOf(para);
        }

    }

    /**
     * 数字の取得
     *
     * @param para
     * @return
     */
    public static BigDecimal getNum(BigDecimal para) {

        if (null == para) {

            return new BigDecimal(0);
        } else {

            return para;
        }

    }

    /**
     * 数字の取得
     *
     * @param para
     * @return
     */
    public static String getStr(String para) {

        if (StringUtils.isEmpty(para)) {

            return "";
        } else {

            return para;
        }

    }

    /**
     * MM/dd HH:mm Date
     *
     * @param dateStr
     * @return String
     */
    public static String getDateTimeFormatMMDDHHMMStr(Object dateStr) {

        if (null == dateStr) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_MMDDHHMM);
        String outDate = sdf.format(dateStr);
        return outDate;
    }

    /**
     * yyyy/MM/dd HH:mm Date
     *
     * @param dateStr
     * @return String
     */
    public static String getDateTimeFormatYYYYMMDDHHMMStr(Object dateStr) {

        if (null == dateStr) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_YYYYMMDDHHMM);
        String outDate = sdf.format(dateStr);
        return outDate;
    }

    /**
     * Integer Value
     *
     * @param integer
     * @return Integer
     */
    public static Integer getIntegerValue(Integer integer) {

        if (null == integer) {
            return 0;
        }

        return integer;
    }

    /**
     * String Value
     *
     * @param str
     * @return String
     */
    public static String getStringValue(String str) {

        if (null == str) {
            return "";
        }

        return str;
    }

    /**
     * Turn the second into a small time
     * 
     * @param seconds
     * @return
     */
    public static String formatSeconds(long seconds) {
        String timeStr;
        String minuteStr;
        String hourStr;
        if (seconds > 60) {
            long minute;
            minute = (seconds / 60) % 60;
            long hour = (seconds / 60) / 60;
            if (minute < 10) {
                minuteStr = "0" + minute;
            } else {
                minuteStr = String.valueOf(minute);
            }
            if (hour < 10) {
                hourStr = "0" + hour;
            } else {
                hourStr = String.valueOf(hour);
            }
            timeStr = hourStr + ":" + minuteStr;
        } else {

            timeStr = "00:00";
        }
        return timeStr;
    }

    /**
     * 英数字のみ文字列を取得
     * 
     * @param inputStr
     * @return
     */
    public static String getSimpleStr(String inputStr) {
        if (inputStr != null) {
            return inputStr.replaceAll("[^(A-Za-z0-9)]", "");
        } else {
            return "";
        }
    }
    
    /**
     * バッチでFileデータを取得
     *
     * @param uuid
     * @param path
     * @return
     */
    public static File downloadFile(String uuid, String path) {

        File file = new File(path);
        FileUtil fu = new FileUtil();
        String filePath = fu.loadFileByFileName(file, uuid);
        file = new File(filePath);
        return file;
    }
    
    /**
     * 指定ＵＲＬへPOSTリクエスト(ファイルアップロード)
     *
     * @param urlStr
     * @param apiToken
     * @param textParams
     * @param fileparams
     * @return
     */
    public static Credential sendPostUpload(String urlStr, String apiToken, Map<String, String> textParams,
            Map<String, File> fileparams) {
        Gson gson = new Gson();
        HttpURLConnection connection = null;

        DataOutputStream ds;
        BufferedReader reader = null;
        Credential resCredential = new Credential();
        try {
            
            // 終了記号
            String boundary = "--";

            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Accept-Charset", CHAR_CODE_UTF8);
            connection.setRequestProperty("Content-Type", "multipart/form-data; charset=utf-8; boundary=" + boundary);
            connection.setRequestProperty("APITOKEN", apiToken);
            connection.connect();

            // POSTリクエスト
            ds = new DataOutputStream(connection.getOutputStream());
            // ファイルパラメータを設定する
            writeStringParams(textParams, boundary, ds);
            // 文字列パラメータを設定する
            writeFileParams(fileparams, boundary, ds);
            ds.writeBytes("--" + boundary + "--" + "\r\n");
            ds.writeBytes("\r\n");

            // 結果を取得
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHAR_CODE_UTF8));
            String lines;
            StringBuilder sb = new StringBuilder("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(CHAR_CODE_UTF8), CHAR_CODE_UTF8);

                sb.append(lines);
            }

            resCredential = gson.fromJson(sb.toString(), Credential.class);

        } catch (Exception e) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
        } finally {

            if (null != reader) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException ex) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (null != connection) {
                // アクセスを切り
                connection.disconnect();
                connection = null;
            }
        }

        return resCredential;
    }
    
//    /**
//     * bytes2HexString
//     * 
//     * @param src
//     * @return
//     */
//    public static String bytes2HexString(byte[] src) {  
//        StringBuilder stringBuilder = new StringBuilder();  
//        if (src == null || src.length <= 0) {  
//            return null;  
//        }  
//        for (int i = 0; i < src.length; i++) {  
//            int v = src[i] & 0xFF;  
//            String hv = Integer.toHexString(v);  
//            if (hv.length() < 2) {  
//                stringBuilder.append(0);  
//            }  
//            stringBuilder.append(hv);  
//        }  
//        //System.out.println(" bytes2HexString = "+stringBuilder.toString().toUpperCase());  
//        return stringBuilder.toString().toUpperCase();  
//    }  
//    
//    /**
//     * judgeFileType
//     * 
//     * @param is
//     * @param fileType
//     * @return
//     */
//    public static boolean judgeFileType(InputStream is, String fileType){  
//        try {  
//            byte[] b = new byte[4];  
//            is.read(b, 0, b.length);  
//            return bytes2HexString(b).contains(fileType);  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//        return false;  
//    }
    
    /**
     * ファイルアップロードの場合、文字列パラメータを設定する
     *
     * @param textParams
     * @param boundary
     * @param ds
     */
    private static void writeStringParams(Map<String, String> textParams, String boundary, DataOutputStream ds) throws Exception {  
        Set<String> keySet = textParams.keySet();  
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {  
            String name = it.next();  
            String value = textParams.get(name);  
            ds.writeBytes("--" + boundary + "\r\n");  
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name  
                    + "\"\r\n");  
            ds.writeBytes("\r\n");  
            ds.writeBytes(URLEncoder.encode(value, "UTF-8") + "\r\n");  
        }  
    }  
    
    /**
     * ファイルアップロードの場合、ファイルパラメータを設定する
     *
     * @param fileparams
     * @param boundary
     * @param ds
     */
    private static void writeFileParams(Map<String, File> fileparams, String boundary, DataOutputStream ds) throws Exception {  
        Set<String> keySet = fileparams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {  
            String name = it.next();  
            File value = fileparams.get(name);  
            ds.writeBytes("--" + boundary + "\r\n");  
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name  
                    + "\"; filename=\"" + URLEncoder.encode(value.getName(), "UTF-8") + "\"\r\n");  
            ds.writeBytes("Content-Type: " + "application/octet-stream" + "\r\n");  
            ds.writeBytes("\r\n");  
            ds.write(getBytes(value));  
            ds.writeBytes("\r\n");  
        }  
    }
    
    /**
     * ファイルから、文字列に変換する
     *
     * @param f
     * @return
     */
    private static byte[] getBytes(File f) throws Exception {  
        FileInputStream in = new FileInputStream(f);  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        byte[] b = new byte[1024];  
        int n;  
        while ((n = in.read(b)) != -1) {  
            out.write(b, 0, n);  
        }  
        in.close();  
        return out.toByteArray();  
    }

    public static String addLeftZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuilder sb = new StringBuilder();
                sb.append("0").append(str);
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

}
