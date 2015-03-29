package com.scipub.util;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.scipub.util.FormatConverter.Format;

public class FormatConverterTest {

    @Test
    public void htmlTest() {
        String result = new String(FormatConverter.convert(Format.HTML, Format.MARKDOWN, "<strong>foo</strong><h2>AAAA</h2>".getBytes(Charsets.UTF_8)));
        String expected = "**foo**" 
                + System.lineSeparator() 
                + System.lineSeparator() 
                + "AAAA" 
                + System.lineSeparator() 
                + "----";
        
        assertThat(expected, equalTo(result.trim()));
    }
}
