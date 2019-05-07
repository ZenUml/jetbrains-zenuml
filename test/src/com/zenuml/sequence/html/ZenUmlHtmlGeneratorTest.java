package com.zenuml.sequence.html;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ZenUmlHtmlGeneratorTest {

    /**
     * 1. generate an empty html with head and body
     * 2. show an hard coded sequence diagram
     *   2.1 add main(#demo)->div(#diagram)->seq-diagram
     *   2.2 add script (vue-sequence-bundle.js)
     * 3. accept DSL as dynamic content
     */

    @Test
    public void test_should_generate_an_empty_html() {
        ZenUmlHtmlGenerator generator = new ZenUmlHtmlGenerator();

        String expected = "<html><head></head><body></body></html>";
        assertEquals(expected, generator.from(""));
    }
}
