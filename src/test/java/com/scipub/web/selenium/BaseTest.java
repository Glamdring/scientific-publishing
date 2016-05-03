/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
