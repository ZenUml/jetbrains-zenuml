package com.zenuml.dsl;

import com.zenuml.license.CheckLicense;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CheckLicense.class)
public class ZenDslTest {

  @Before
  public void setUp() {
    PowerMockito.mockStatic(CheckLicense.class);
  }

  @Test
  public void testEscape() {
    // remove the following characters: \, ", \n, \t, \r, `
    String input = "\\, \", \n, \t, \r, `";
    String output = "";
    assertEquals(output, ZenDsl.escape(input));
  }

  @Test
  public void test_quoted() {
    Mockito.when(CheckLicense.isLicensed()).thenReturn(true);
    assertThat(new ZenDsl().quoted("a").getDsl(), is("\"a\""));
    assertThat(new ZenDsl().quoted("a\nb").getDsl(), is("\"ab\""));
    assertThat(new ZenDsl()
        .startBlock().quoted("a\nb")
        .closeBlock().getDsl(), is(" {\n\t\"ab\"}\n"));

  }

  @Test
  public void addComment() {
    Mockito.when(CheckLicense.isLicensed()).thenReturn(true);
    assertThat(new ZenDsl().comment("a").getDsl(), is("// a\n"));
    assertThat(new ZenDsl().comment("a\nb").getDsl(), is("// a\n// b\n"));
    assertThat(new ZenDsl()
        .startBlock().comment("a\nb")
        .closeBlock().getDsl(), is(" {\n\t// a\n\t// b\n}\n"));
  }

  @Test
  public void testNotLicensed() {
    Mockito.when(CheckLicense.isLicensed()).thenReturn(false);
    assertThat(new ZenDsl().getDsl(), is(ZenDsl.LICENSE_IS_NOT_VALID));
  }

  @Test
  public void testNotLicensedWithNull() {
    Mockito.when(CheckLicense.isLicensed()).thenReturn(null);
    assertThat(new ZenDsl().getDsl(), is(ZenDsl.LICENSE_IS_NOT_VALID));
  }
}
