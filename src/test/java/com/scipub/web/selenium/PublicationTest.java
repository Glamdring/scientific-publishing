package com.scipub.web.selenium;

import org.junit.Test;
import org.openqa.selenium.By;

public class PublicationTest extends BaseTest {

    @Test
    public void newPublicationTest() {
        webDriver.get(getWebsiteUrl());
        webDriver.findElement(By.id("title")).sendKeys("Title");
        webDriver.findElement(By.id("abstract")).sendKeys("Abstract");
    }
}
