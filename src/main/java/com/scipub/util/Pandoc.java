package com.scipub.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

public class Pandoc {

    public static void convert(Format from, Format to, File in, File out, List<String> additionalArguments)
            throws IOException, InterruptedException {
        List<String> args =
                Lists.newArrayList("-f", from.getArgument(), "-t", to.getArgument(), "-o", out.getAbsolutePath(),
                        in.getAbsolutePath());
        args.addAll(additionalArguments);
        args.add(0, "pandoc");
        Process process = new ProcessBuilder(args).start();
        process.waitFor();
    }

    /*
     private def readOutputAsString(p: Process, encoding: String = "UTF-8")(
    implicit ctx: ExecutionContext): Future[String] = {
    val sb = new StringBuilder

    future {
      val in = new BufferedReader(new InputStreamReader(
        p.getInputStream(), encoding))

      var c = in.read()
      while (c != -1) {
        if (c != '\r') // filter windows style line endings
          sb += c.toChar
        c = in.read()
      }

      sb.result
    }
     */
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
