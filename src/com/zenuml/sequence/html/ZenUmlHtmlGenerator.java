package com.zenuml.sequence.html;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZenUmlHtmlGenerator {
    public String from(String dsl) {
        Class clazz = this.getClass();
        InputStream inputStream = clazz.getResourceAsStream("/html/zenuml/preview.html");
        try {
            String previewHtml = readFromInputStream(inputStream);
            Escaper escaper = Escapers.builder()
                    .addEscape('\n', "\\n")
                    .addEscape('\r', "\\r")
                    .addEscape('`', "\\`")
                    .build();

            return previewHtml.replace("$DSL_CODE", escaper.escape(dsl));
        } catch (IOException e) {
            return "Error";
        }
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

}
