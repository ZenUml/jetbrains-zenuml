package org.intellij.plugins.markdown;

import org.intellij.plugins.markdown.lang.ZenUmlLanguage;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ZenUmlLanguageTest {
  @Test
  public void should_return_correct_mime_types() {
    assertEquals("text/x-zenuml", ZenUmlLanguage.INSTANCE.getMimeTypes()[0]);
  }
}
