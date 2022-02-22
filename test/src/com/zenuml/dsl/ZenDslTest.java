package com.zenuml.dsl;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ZenDslTest {

    @Test
    public void testEscape() {
        // remove the following characters: \, ", \n, \t, \r, `
        String input = "\\, \", \n, \t, \r, `";
        String output = "";
        assertEquals(output, ZenDsl.escape(input));
    }


    @Test
    public void addComment() {
        assertThat(new ZenDsl().comment("a").getDsl(),              is("// a\n"));
        assertThat(new ZenDsl().comment("a\nb").getDsl(),           is("// a\n// b\n"));
        assertThat(new ZenDsl()
                .startBlock().comment("a\nb")
                .closeBlock().getDsl(),                             is(" {\n\t// a\n\t// b\n}\n"));
    }
}
