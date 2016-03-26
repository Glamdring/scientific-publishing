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
