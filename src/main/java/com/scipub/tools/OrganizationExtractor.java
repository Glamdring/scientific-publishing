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
package com.scipub.tools;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class OrganizationExtractor {

    public static void main(String[] args) throws Exception {
        URL universitiesCsv = new URL("https://raw.githubusercontent.com/endSly/world-universities-csv/master/world-universities.csv");
        try (CSVParser parser = new CSVParser(new InputStreamReader(universitiesCsv.openStream(), "UTF-8"), CSVFormat.DEFAULT)) { 
            try (Writer out = new OutputStreamWriter(new FileOutputStream("c:/tmp/org.txt"), "UTF-8")) {
                for (CSVRecord record : parser.getRecords()) {
                    String country = record.get(0);
                    String name = record.get(1).replace("\"", "\\\"");
                    String website = record.get(2);
                    
                    out.write(String.format(
                            "INSERT INTO `organizations` (name, country, website) VALUES (\"%s\", \"%s\", \"%s\");" + System.lineSeparator(),
                            name, country, website));
                }
            }
        }
    }
}
