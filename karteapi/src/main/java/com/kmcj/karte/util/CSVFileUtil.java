/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author zds
 */
public class CSVFileUtil {

    /** CSVエンコード utf8 */
    public static final String ENCODE = "UTF-8";
    /** 取り込み*/
    private FileInputStream fis = null;
    private InputStreamReader isw = null;
    private BufferedReader br = null;
    /**　書き出し*/
    private FileOutputStream fos = null;
    private OutputStreamWriter osw = null;
    private BufferedWriter bw = null;
    /** 改行コード */
    public static final String RETURN_CODE = "\r\n";

    /**
     * ファイルをクローズします.
     *
     */
    public final void close() {
       
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                br = null;
            }
        }
        
        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                bw = null;
            }
        }
    }

    /**
     * コンストラクタ
     * @param fileName
     * @param output
     * @throws Exception 
     */
    public CSVFileUtil(String fileName, String output) throws Exception {
        // 書き込み
        fos = new FileOutputStream(fileName);
        fos.write(0xef);
        fos.write(0xbb);
        fos.write(0xbf);
        osw = new OutputStreamWriter(fos, ENCODE);
        bw = new BufferedWriter(osw);
    }

    /**
     * コンストラクタ
     * @param filename
     * @throws Exception 
     */
    public CSVFileUtil(String filename) throws Exception {

//        取り込み
        fis = new FileInputStream(filename);
        isw = new InputStreamReader(fis, ENCODE);
        br = new BufferedReader(isw);

    }
  

    /**
     * CSV Streamから一行を読み込み
     * @return
     * @throws Exception 
     */
    public String readLine() throws Exception {

        StringBuffer readLine = new StringBuffer();
        boolean bReadNext = true;

        while (bReadNext) {
            //  
            if (readLine.length() > 0) {
                readLine.append("\r\n");
            }
            // 一行  
            String strReadLine = br.readLine();

            // readLine is Null  
            if (strReadLine == null) {
                return null;
            }
            readLine.append(strReadLine);

            // ダブルクォーテーションがある場合。改行コードがある場合 
            if (countChar(readLine.toString(), '"', 0) % 2 == 1) {
                bReadNext = true;
            } else {
                bReadNext = false;
            }
        }
        //　ＢＯＭをスキップ
        String line = readLine.toString();
        if (line.startsWith("\uFEFF")) {
            //line = line.substring(1);
            line = line.replace("\uFEFF", "");
        }
        return line;
    }

    /**
     * CSVファイルの一行を文字列Arrayに変換、足りない場合がＮＵＬＬを設定
     * @param source
     * @param size
     * @return 
     */
    public static String[] fromCSVLine(String source, int size) {
        ArrayList tmpArray = fromCSVLinetoArray(source);
        if (size < tmpArray.size()) {
            size = tmpArray.size();
        }
        String[] rtnArray = new String[size];
        tmpArray.toArray(rtnArray);
        return rtnArray;
    }

    /**
     * CSVファイルの一行を文字列Arrayに変換
     * @param source
     * @return 
     */
    public static ArrayList fromCSVLinetoArray(String source) {
        if (source == null || source.length() == 0) {
            return new ArrayList();
        }
        int currentPosition = 0;
        int maxPosition = source.length();
        int nextComma = 0;
        ArrayList rtnArray = new ArrayList();
        while (currentPosition < maxPosition) {
            nextComma = nextComma(source, currentPosition);
            rtnArray.add(nextToken(source, currentPosition, nextComma));
            currentPosition = nextComma + 1;
            if (currentPosition == maxPosition) {
                rtnArray.add("");
            }
        }
        return rtnArray;
    }

    /**
     * Ａｒｒａｙ文字列をＣＳＶ一行に変換（ＣＳＶファイル出力とき用）
     * @param strArray
     * @return 
     */
    public static String toCSVLine(String[] strArray) {
        if (strArray == null) {
            return "";
        }
        StringBuffer cvsLine = new StringBuffer();
        for (int idx = 0; idx < strArray.length; idx++) {
            String item = addQuote(strArray[idx]);
            cvsLine.append(item);
            if (strArray.length - 1 != idx) {
                cvsLine.append(',');
            }
        }
        cvsLine = cvsLine.append(RETURN_CODE);
        return cvsLine.toString();
    }

    /**
     * 文字列ＬｉｓｔをＣＳＶ一行を変換（ＣＳＶ出力とき用）
     * @param strArrList 
     */
    public void toCSVLine(ArrayList strArrList) {
        if (strArrList != null) {

            String[] strArray = new String[strArrList.size()];
            for (int idx = 0; idx < strArrList.size(); idx++) {
                strArray[idx] = (String) strArrList.get(idx);
            }
            try {

                String outputLine = new String(toCSVLine(strArray).getBytes(ENCODE), ENCODE);
                bw.write(outputLine);
                osw.flush();
            } catch (IOException ex) {
                Logger.getLogger(CSVFileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // ==========該当クラス自分用=============================  
    /**
     * Ｃｏｕｎｔ　Ｃｈａｒ　個数
     *
     * @param str 文字列
     * @param c 文字
     * @param start 開始位置
     * @return 個数
     */
    private int countChar(String str, char c, int start) {
        int i = 0;
        int index = str.indexOf(c, start);
        return index == -1 ? i : countChar(str, c, index + 1) + 1;
    }

    /**
     * 次のカマンを見つかる。
     *
     * @param source　文字列
     * @param st 開始位置を見つかる
     * @return 次のカマンの位置
     */
    private static int nextComma(String source, int st) {
        int maxPosition = source.length();
        boolean inquote = false;
        while (st < maxPosition) {
            char ch = source.charAt(st);
            if (!inquote && ch == ',') {
                break;
            } else if ('"' == ch) {
                inquote = !inquote;
            }
            st++;
        }
        return st;
    }

    /**
     * 次の文字列を取得
     * @param source
     * @param st
     * @param nextComma
     * @return 
     */
    private static String nextToken(String source, int st, int nextComma) {
        StringBuffer strb = new StringBuffer();
        int next = st;
        while (next < nextComma) {
            char ch = source.charAt(next++);
            if (ch == '"') {
                if ((st + 1 < next && next < nextComma) && (source.charAt(next) == '"')) {
                    strb.append(ch);
                    next++;
                }
            } else {
                strb.append(ch);
            }
        }
        return strb.toString();
    }

    /**
     * エスケープ処理
     * 文字列全体を"（ダブルクォーテーション）で囲む
     * @param item 文字列
     * @return 処理された文字列
     */
    private static String addQuote(String item) {
        if (item == null || item.length() == 0) {
            return "\"\"";
        }
        StringBuffer sb = new StringBuffer();
        sb.append('"');
        for (int idx = 0; idx < item.length(); idx++) {
            char ch = item.charAt(idx);
            if ('"' == ch) {
                sb.append("\"\"");
            } else {
                sb.append(ch);
            }
        }
        sb.append('"');
        return sb.toString();
    }
    
    /**
     * CSV中身取込み
     *
     * @param csvFilePath　取込みCSVファイルパス
     * @return
     */
    public static ArrayList readCsv(String csvFilePath) {
        CSVFileUtil csvFileUtil = null;
        ArrayList readList = new ArrayList();
        try {
            csvFileUtil = new CSVFileUtil(csvFilePath);
            boolean readEnd = false;
            do {
                String readLine = csvFileUtil.readLine();
                if (StringUtils.isEmpty(readLine)) {
                    readEnd = true;
                } else {
                    readList.add(CSVFileUtil.fromCSVLinetoArray(readLine));
                }
            } while (!readEnd);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }
        return readList;
    }
    
    /**
     * データをCSVに書き出し
     * @param outCsvPath 書き出し先
     * @param csvInfos 　書き出しデータ
     */
    public static void writeCsv(String outCsvPath, ArrayList csvInfos) {

        CSVFileUtil csvFileUtil = null;
        try {
            csvFileUtil = new CSVFileUtil(outCsvPath, "csvOutput");
            Iterator<ArrayList> iter = csvInfos.iterator();
            while (iter.hasNext()) {
                csvFileUtil.toCSVLine(iter.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }
    }
    
}
