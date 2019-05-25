package org.intellij.plugins.markdown.reference

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.intellij.plugins.markdown.MarkdownTestingUtil
import org.intellij.plugins.markdown.lang.references.ZenUmlUnresolvedFileReferenceInspection

class UnresolvedLinkDestinationReferenceTest : LightPlatformCodeInsightFixtureTestCase() {
  override fun getTestDataPath(): String = MarkdownTestingUtil.TEST_DATA_PATH + "/reference/linkDestination/"

  fun testUnresolvedReference() {
    doTest("sample_unresolved.zen")
  }

  fun testUnresolvedFileAnchorReference() {
    doTest("sample_file_anchor_unresolved.zen")
  }

  fun testUnresolvedFileAnchor1Reference() {
    myFixture.configureByFile("sample.zen")
    doTest("sample_file_anchor_unresolved1.zen")
  }

  fun testUnresolvedAnchorReference() {
    doTest("sample_anchor_unresolved.zen")
  }

  private fun doTest(fileName: String) {
    myFixture.enableInspections(ZenUmlUnresolvedFileReferenceInspection::class.java)
    myFixture.testHighlighting(true, false, false, fileName)
  }

  override fun tearDown() {
    try {
      val markdownUnresolvedFileReferenceInspection = ZenUmlUnresolvedFileReferenceInspection()
      myFixture.disableInspections(markdownUnresolvedFileReferenceInspection)
    }
    finally {
      super.tearDown()
    }
  }
}