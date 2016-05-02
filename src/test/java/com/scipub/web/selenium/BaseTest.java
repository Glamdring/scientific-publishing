package com.scipub.web.selenium;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

public class BaseTest {
    protected WebDriver webDriver;
    
    private static Properties properties;
   
    @Before
    public void setUp() throws MalformedURLException {
        FirefoxProfile profile = new FirefoxProfile();
        ProfilesIni profilesIni = new ProfilesIni();
        profile = profilesIni.getProfile("default");
        // The following 3 settings disable the 'Firefox automatically sends some data to Mozilla...'
        profile.setPreference("datareporting.healthreport.uploadEnabled", false);
        profile.setPreference("datareporting.healthreport.service.enabled", false);
        profile.setPreference("datareporting.healthreport.service.firstRun", false);
        webDriver = new FirefoxDriver(profile);
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() {
        webDriver.quit();
    }
    
    protected String getWebsiteUrl() {
        return getProperty("website.url");
    }
    
    public static String getProperty(String key) {
        String prop = System.getProperty(key);
        if (prop == null) {
            if (properties == null) {
                try {
                    properties = new Properties();
                    properties.load(new FileInputStream("src/test/resources/selenium.properties"));
                } catch (IOException e) {
                    // Leave the properties empty
                }
            }
            prop = properties.getProperty(key);
        }
        return prop;
    }

}
