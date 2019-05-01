package com.zenuml.sequence.html;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ZenUmlHtmlGeneratorTest {

    /**
     * 1. generate an empty html with head and body
     * 2. show an hard coded sequence diagram
     * 3. accept DSL as dynamic content
     */

    @Test
    public void test_should_generate_an_empty_html() {
        ZenUmlHtmlGenerator generator = new ZenUmlHtmlGenerator();

        assertEquals("<html><head></head><body></body></html>", generator.from(""));
    }
}
