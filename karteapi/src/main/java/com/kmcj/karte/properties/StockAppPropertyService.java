/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.properties;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author admin
 */
@Dependent
public class StockAppPropertyService {

    @Inject
    private KartePropertyService kartePropertyService;

    public String getVersion(String osType) throws Exception {

        StringBuilder filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        filePath.append(FileUtil.SEPARATOR).append(CommonConstants.MODULE).append(FileUtil.SEPARATOR);
        filePath.append("stocktakeAppVersion.properties");

        Properties properties = new Properties();

        InputStream inputStream = new FileInputStream(new File(filePath.toString()));
        properties.load(inputStream);

        return properties.getProperty(osType);
    }

}
