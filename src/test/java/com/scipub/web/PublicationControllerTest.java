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
