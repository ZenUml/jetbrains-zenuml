package org.intellij.plugins.markdown.structureView;

import com.intellij.openapi.ui.Queryable;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.SkipWithExecutionPolicy;
import com.intellij.util.ui.tree.TreeUtil;
import org.intellij.plugins.markdown.MarkdownTestingUtil;
import org.junit.Ignore;

import javax.swing.*;
import javax.swing.tree.TreePath;

@Ignore("Failing due to ZenUmlLanguage is initialised multiple times in different class loaders")
public class MarkdownStructureViewTest extends LightPlatformCodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return MarkdownTestingUtil.TEST_DATA_PATH + "/structureView/";
  }

  public void doTest() {
    myFixture.configureByFile(getTestName(true) + ".zen");
    myFixture.testStructureView(svc -> {
      JTree tree = svc.getTree();
      TreeUtil.expandAll(tree);
      PlatformTestUtil.waitForPromise(svc.select(svc.getTreeModel().getCurrentEditorElement(), false));
      assertSameLinesWithFile(
        getTestDataPath() + '/' + getTestName(true) + ".txt",
        PlatformTestUtil.print(tree, new TreePath(tree.getModel().getRoot()), new Queryable.PrintInfo(null, null), true));
    });
  }

  public void testOneParagraph() {
    doTest();
  }

  public void testTwoParagraphs() {
    doTest();
  }

  public void testNormalATXDocument() {
    doTest();
  }

  public void testNormalSetextDocument() {
    doTest();
  }

  public void testHeadersLadder() {
    doTest();
  }

  public void testHeadersUnderBlockquotesAndLists() {
    doTest();
  }

  public void testPuppetlabsCoreTypes() {
    doTest();
  }

}
