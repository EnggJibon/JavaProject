package com.kmcj.karte.util;

import com.kmcj.karte.constants.CommonConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * ZIP解凍API
 * 
 */
public class ZipCompressor {
    
    private static final String SHIFT_JIS = "shift-jis";
    /**
     * 指定されたパスワードを使って、ZIPファイルは指定されたフォルダに解凍する
     * 
     * @param zipFilePath
     * @param toPath
     * @param passwd
     * @return
     * @throws ZipException
     */
    public static File[] unzip(String zipFilePath, String toPath, String passwd) throws ZipException {

        File zipFile = new File(zipFilePath);

        return unzip(zipFile, toPath, passwd);
    }

    /**
     * 指定されたパスワードを使って、圧縮ファイルを解凍する
     * 
     * @param zipFilePath
     * @param passwd
     * @return
     * @throws ZipException
     */
    public static File[] unzip(String zipFilePath, String passwd) throws ZipException {
        File zipFile = new File(zipFilePath);
        File parentDir = zipFile.getParentFile();
        return unzip(zipFile, parentDir.getAbsolutePath(), passwd);
    }

    /**
     * 指定されたパスワードを使って、圧縮ファイルは指定されたフォルダに解凍する
     * 
     * @param zipFile
     * @param toPath
     * @param passwd
     * @return
     * @throws ZipException
     */
    public static File[] unzip(File zipFile, String toPath, String passwd) throws ZipException {

        ZipFile zFile = new ZipFile(zipFile);

        zFile.setFileNameCharset("UTF-8");

        if (!zFile.isValidZipFile()) {
            throw new ZipException("圧縮ファイルが破損された.");
        }

        File destDir = new File(toPath);

        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }

        if (zFile.isEncrypted()) {
            zFile.setPassword(passwd.toCharArray());
        }

        zFile.extractAll(toPath);

        List<FileHeader> headerList = zFile.getFileHeaders();
        List<File> extractedFileList = new ArrayList<File>();
        for (FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {
                extractedFileList.add(new File(destDir, fileHeader.getFileName()));
            }
        }

        File[] extractedFiles = new File[extractedFileList.size()];
        extractedFileList.toArray(extractedFiles);
        return extractedFiles;
    }

    /**
     * ファイル を圧縮する
     * 
     * @param filePath
     * 
     * @return
     */
    public static String zip(String filePath, boolean charsetFlg) {

        return zip(filePath, null, null, 0, charsetFlg);
    }

    /**
     * ファイル を圧縮する
     * 
     * @param filePath
     * @param outFileName
     * 
     * @return
     */
    public static String zip(String filePath, String outFileName, boolean charsetFlg) {

        return zip(filePath, outFileName, null, 0, charsetFlg);
    }

    /**
     * パスワードが指定されなかった、ファイル を圧縮する
     * 
     * @param filePath
     * @param outFileName
     * 
     * @return
     */
    public static String zipWithPassword(String filePath, String outFileName, boolean charsetFlg) {

        return zip(filePath, outFileName, null, 1, charsetFlg);
    }

    /**
     * 指定されたパスワードを使って、ファイル を圧縮する
     * 
     * @param filePath
     * @param outFileName
     * @param passwd
     * 
     * @return
     */
    public static String zipWithPassword(String filePath, String outFileName, String passwd, boolean charsetFlg) {

        return zip(filePath, outFileName, passwd, 1, charsetFlg);
    }

    /**
     * 指定されたパスワードを使って、ファイル或いはフォルダは指定されたパスに圧縮する
     * 
     * @param filePath
     * @param outFileName
     * @param passwd
     * @param passwdFlg(1:パスワードが必要;0:パスワード不要)
     * 
     * @return
     */
    private static String zip(String filePath, String outFileName, String passwd, int passwdFlg, boolean charsetFlg) {

        File srcFile = new File(filePath);

        String toPath = buildDestinationZipFilePath(srcFile, outFileName);

        ZipParameters parameters = new ZipParameters();

        // 圧縮方式を指定
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        // 圧縮レベルを指定
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        
        // パスワードが必要の場合
        if (1 == passwdFlg) {

            // パスワード未指定の場合
            if (StringUtils.isEmpty(passwd)) {

                // パスワード自動生成
                passwd = RandomStringUtils.randomAlphanumeric(10);
            }

            // パスワード認証を設定
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(passwd.toCharArray());
        }

        try {

            ZipFile zipFile = new ZipFile(toPath);
            if (charsetFlg) {
                zipFile.setFileNameCharset(SHIFT_JIS);
            }
            
            if (srcFile.isDirectory()) {
                File [] subFiles = srcFile.listFiles();  
                ArrayList<File> temp = new ArrayList();
                Collections.addAll(temp, subFiles);  
                zipFile.addFiles(temp, parameters);
            } else {
                zipFile.addFile(srcFile, parameters);
            }

        } catch (ZipException e) {
            e.printStackTrace();
        }

        return passwd;
    }

    /**
     * 圧縮ファイルの保存フォルダを構成する、フォルダが存在しなければ、フォルダを作成する
     * 
     * @param srcFile
     * @param outFileName
     * 
     * @return
     */
    private static String buildDestinationZipFilePath(File srcFile, String outFileName) {

        String toPath = "";

        if (srcFile.isDirectory()) {

            toPath = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";

        } else {

            String fileName = "";

            // ファイル名未指定の場合
            if (StringUtils.isEmpty(outFileName)) {

                fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));

            } else {// ファイル指定の場合

                fileName = outFileName;
            }
            toPath = srcFile.getParent() + File.separator + fileName + ".zip";
        }

        return toPath;
    }
    
    
    public static void zipDirectory(String filePath,String zipFileName, boolean charsetFlg) {

        File srcFile = new File(filePath);

        ZipParameters parameters = new ZipParameters();

        // 圧縮方式を指定
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        // 圧縮レベルを指定
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        
        try {

            ZipFile zipFile = new ZipFile(filePath + zipFileName + CommonConstants.EXT_ZIP);
            if (charsetFlg) {
                zipFile.setFileNameCharset(SHIFT_JIS);
            }
            
            if (srcFile.isDirectory()) {
                File [] subFiles = srcFile.listFiles();  
                ArrayList<File> temp = new ArrayList();
                Collections.addAll(temp, subFiles);  
                zipFile.addFiles(temp, parameters);
            } else {
                zipFile.addFile(srcFile, parameters);
            }

        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
    
}