package org.intellij.plugins.markdown;

import org.intellij.plugins.markdown.lang.MarkdownLanguage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MarkdownLanguageTest {
  @Test
  public void should_return_correct_mime_types() {
    assertEquals("text/x-zenuml", MarkdownLanguage.INSTANCE.getMimeTypes()[0]);
  }
}
