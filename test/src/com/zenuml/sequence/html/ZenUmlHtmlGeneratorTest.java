package com.zenuml.sequence.html;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

public class ZenUmlHtmlGeneratorTest {

    /**
     * 1. generate an empty html with head and body
     * 2. show an hard coded sequence diagram
     *   2.1 add main(#demo)->seq-diagram
     *   2.2 add script (vue-sequence-bundle.js)
     * 3. accept DSL as dynamic content
     */

    @Test
    public void test_should_generate_an_empty_html() throws IOException {

        ZenUmlHtmlGenerator generator = new ZenUmlHtmlGenerator();
        String expected = "<html><head></head><body><main id=\"domo\"><seq-diagram></seq-diagram></main></body></html>";
        assertEquals(expected, generator.from(""));
    }

    @Test
    public void givenFileName_whenUsingJarFile_thenFileData() throws IOException {
//        String expectedData = "MIT License";
//
//        Class clazz = Assert.class;
//        InputStream inputStream = clazz.getResourceAsStream("/LICENSE.txt");
//        String data = readFromInputStream(inputStream);
//
//        Assert.assertThat(data, containsString(expectedData));
    }

}
