package com.zenuml.sequence.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZenUmlHtmlGenerator {
    public String from(String dsl) {
        Class clazz = this.getClass();
        InputStream inputStream = clazz.getResourceAsStream("/html/zenuml/preview.html");
        try {
            return readFromInputStream(inputStream);
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
