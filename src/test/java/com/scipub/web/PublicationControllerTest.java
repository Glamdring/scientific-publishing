package com.scipub.web;

import java.io.IOException;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.scipub.model.Language;

public class PublicationControllerTest {

    @Test
    public void languagesTest() {
        PublicationController controller = new PublicationController();
        assertThat(controller.getLanguages(), is(Language.values()));
    }
    
    @Test
    public void importFileTest() {
        PublicationController controller = new PublicationController();
        try {
            //TODO check conversion
            controller.importFile(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
