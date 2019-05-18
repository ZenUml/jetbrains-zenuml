package org.intellij.plugins.markdown.actions;

import com.intellij.testFramework.LightPlatformCodeInsightTestCase;
import org.intellij.plugins.markdown.MarkdownTestingUtil;
import org.jetbrains.annotations.NotNull;

public class MarkdownToggleBoldTest extends LightPlatformCodeInsightTestCase {

  public void testSimple() {
    doTest();
  }

  private void doTest() {
    configureByFile(getTestName(true) + "_before.zen");
    executeAction("org.intellij.plugins.markdown.ui.actions.styling.ToggleBoldAction");
    checkResultByFile(getTestName(true) + "_after.zen");
  }

  @NotNull
  @Override
  protected String getTestDataPath() {
    return MarkdownTestingUtil.TEST_DATA_PATH + "/actions/toggleBold/";
  }
}
