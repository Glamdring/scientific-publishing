package com.scipub.web;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations={"classpath:/spring/mvc-core-config.xml", "classpath:/spring/services.xml"})
public abstract class BaseIntegrationTest extends AbstractJUnit4SpringContextTests {

}
