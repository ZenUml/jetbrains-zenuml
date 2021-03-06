// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.plugins.markdown.actions;

import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import org.intellij.plugins.markdown.MarkdownTestingUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;

@Ignore
public class MarkdownToggleStrikeThroughTest extends LightPlatformCodeInsightTestCase {

  public void testSimple() {
    doTest();
  }

  public void testSimpleNoSelection() {
    doTest();
  }

  public void testSimpleSelection() {
    doTest();
  }

  public void testSimpleSelectionInWord() {
    doTest();
  }

  public void testSimpleSelectionInList() {
    doTest();
  }

  public void testSimpleSelectionInQuote() {
    doTest();
  }

  private void doTest() {
    configureByFile(getTestName(true) + "_before.zen");
    executeAction("org.intellij.plugins.markdown.ui.actions.styling.ZenUmlToggleStrikethroughAction");
    checkResultByFile(getTestName(true) + "_after.zen");
  }

  @NotNull
  @Override
  protected String getTestDataPath() {
    return MarkdownTestingUtil.TEST_DATA_PATH + "/actions/toggleStrikethrough/";
  }
}