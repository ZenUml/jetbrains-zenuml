package com.zenuml.sequence.plugins.jetbrains.html;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;

public class ZenUmlHtmlGeneratorTest {

    @Test
    public void test_should_generate_an_html_with_code() {
        ZenUmlHtmlGenerator generator = new ZenUmlHtmlGenerator();
        String html = generator.from("AB\\CD");
        Assert.assertThat(html, containsString("AB\\\\CD"));
    }

    @Test
    @Ignore("We cannot handle ` in the parser now.")
    public void should_escape_back_tick_in_DSL() {
        ZenUmlHtmlGenerator generator = new ZenUmlHtmlGenerator();
        String html = generator.from("AB`CD`");
        Assert.assertThat(html, containsString("AB\\`CD\\`"));
    }
}
