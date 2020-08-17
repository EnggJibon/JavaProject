/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;

/**
 *
 * @author f.kitaoji
 */
@ApplicationScoped
//@Singleton
//@Startup
public class KartePropertyService {

    private Properties properties;
    
    @Inject
    private ServletContext servletContext;

    //public void init(@Observes @Initialized(ApplicationScoped.class) ServletContext servletContext) throws IOException {
    @PostConstruct
    public void init() {
        String filePath;
        filePath = servletContext.getRealPath("/WEB-INF");
        File currentDirectory = new File(filePath);
        filePath = currentDirectory.getParentFile().getParentFile().getParent();
        properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(new File(filePath, "applicationconfig/karteapi.properties"));
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("KartePropertyService Initialized:" + this);
    }

    public String getDocumentDirectory() {
        return properties.getProperty("documentDirectory");
    }

    public String getSmtpHost() {
        return properties.getProperty("smtp_host");
    }
    
    public String getSmtpPort() {
        return properties.getProperty("smtp_port");
    }
    
    public String getSmtpUser() {
        return properties.getProperty("smtp_user");
    }
    
    public String getSmtpPassword() {
        return properties.getProperty("smtp_password");
    }
    //-----------------Apeng 20171109 add-------------
    public String getBaseUrl() {
        return properties.getProperty("base_url");
    }

}
