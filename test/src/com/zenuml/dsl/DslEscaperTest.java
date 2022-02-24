package com.zenuml.dsl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DslEscaperTest {

  @Test
  public void test_remove_backtick_from_dsl() {
    String dsl = "`foo`";
    String expected = "foo";
    String actual = DslEscaper.removeBacktick.apply(dsl);
    assertEquals(expected, actual);
  }


}
