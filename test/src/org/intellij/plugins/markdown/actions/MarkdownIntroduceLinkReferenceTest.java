// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.plugins.markdown.actions;

import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import org.intellij.plugins.markdown.MarkdownTestingUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;

@Ignore
public class MarkdownIntroduceLinkReferenceTest extends LightPlatformCodeInsightTestCase {
  public void testAutoLink() {
    doTest();
  }

  public void testGfmAutoLink() {
    doTest();
  }

  public void testInlineLink() {
    doTest();
  }

  public void testInlineLinkInList() {
    doTest();
  }

  public void testInlineLinkNoTitle() {
    doTest();
  }

  public void testLarge() {
    doTest();
  }

  public void testComplexLinkText() {
    doTest();
  }

  public void testComplexTitle() {
    doTest();
  }

  public void testDuplicateGfm() {
    doTest();
  }

  public void testDuplicateAutoLink() {
    doTest();
  }

  public void testDuplicateInlineLink() {
    doTest();
  }

  public void testSameTitles() {
    doTest();
  }

  public void testDifferentTitles() {
    doTest();
  }

  private void doTest() {
    configureByFile(getTestName(true) + "_before.zen");
    executeAction("org.intellij.plugins.markdown.ui.actions.styling.ZenUmlIntroduceLinkReferenceAction");
    checkResultByFile(getTestName(true) + "_after.zen");
  }

  @NotNull
  @Override
  protected String getTestDataPath() {
    return MarkdownTestingUtil.TEST_DATA_PATH + "/actions/link/";
  }
}
