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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class FormatConverter {

    public static String convert(Format from, Format to, String input) {
        return new String(convert(from, to, input.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
    }
    
    public static byte[] convert(Format from, Format to, byte[] in) {
        return pandocConvert(from, to, in);
    }

    private static byte[] pandocConvert(Format from, Format to, byte[] in) {
        File inFile = null;
        try {
            inFile = File.createTempFile("pandocInFile", "." + from.getExtension());
            Files.write(in, inFile);
            
            List<String> args =
                    Lists.newArrayList("pandoc", "-f", from.getArgument(), "-t", to.getArgument(), inFile.getAbsolutePath());
            Process process = new ProcessBuilder(args).start();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ByteStreams.copy(new BufferedInputStream(process.getInputStream()), baos);
            byte[] result = baos.toByteArray();
            if (result.length == 0) {
                ByteStreams.copy(new BufferedInputStream(process.getErrorStream()), baos);
                throw new IllegalStateException(new String(baos.toByteArray(), "UTF-8"));
            } else {
                return result;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (inFile != null) {
                inFile.delete();
            }
        }
    }
    
    public static enum Format {
        NATIVE_HASKELL("native", "haskell"),
        JSON("json", "json"),
        PLAIN_TEXT("plain", "txt"),
        MARKDOWN("markdown", "md"),
        MARKDOWN_STRICT("markdown_strict", "mds"),
        MARKDOWN_GITHUB("markdown_github", "mdg"),
        TEXTILE("textile", "tx"),
        RST("rst", "rst"),
        HTML("html", "html"),
        HTML5("html5", "html"),
        DOCBOOK("docbook", "docbook"),
        MEDIA_WIKI("madiawiki", "mediawiki"),
        LATEX("latex", "tex"),
        BEAMER("beamer", "bm"),
        CONTEXT("context", "context"),
        MAN("man", "man"),
        PDF("pdf", "pdf"),
        RTF("rtf", "rtf"),
        ASCII_DOC("asciidoc", "adoc"),
        ORG("org", "org"), // emacs org-mode
        TEX_INFO("texinfo", "texinfo"), // GNU Texinfo
        OPEN_DOCUMENT("opendocument", "odt"),
        DOCX("docx", "docx"),
        EPUB("epub", "epub"),
        EPUB_3("epub3", "epub3"),
        FICTION_BOOK2("fb2", "fb2"),
        SLIDY("slidy", "slidy"),
        SLIDEOUS("slideous", "slideous"),
        DZSlides("dzslides", "dzslides"),
        S5("s5", "s5");

        private final String argument;
        private final String extension;

        private Format(String argument, String extension) {
            this.argument = argument;
            this.extension = extension;
        }

        public String getArgument() {
            return argument;
        }

        public String getExtension() {
            return extension;
        }
        
        public static Format forExtension(String extension) {
            for (Format format : Format.values()) {
                if (format.getExtension().equals(extension)) {
                    return format;
                }
            }
            return null;
        }
    }
}
