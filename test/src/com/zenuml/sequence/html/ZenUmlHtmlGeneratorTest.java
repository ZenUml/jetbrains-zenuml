package com.zenuml.sequence.html;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;

public class ZenUmlHtmlGeneratorTest {

    @Test
    public void test_should_generate_an_html_with_code() {
        ZenUmlHtmlGenerator generator = new ZenUmlHtmlGenerator();
        String html = generator.from("AB\nCD");
        Assert.assertThat(html, containsString("AB\\nCD"));
    }
}
