package com.zenuml;

import com.intellij.lang.Language;

public class ZenUmlLanguage extends Language {
  public static final ZenUmlLanguage INSTANCE = new ZenUmlLanguage();

  protected ZenUmlLanguage() {
    super("ZenUML");
  }
}
