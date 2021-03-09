package org.intellij.plugins.markdown.completion

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.intellij.testFramework.fixtures.SkipWithExecutionPolicy
import org.intellij.plugins.markdown.MarkdownTestingUtil
import org.junit.Ignore

@Ignore("Failing due to ZenUmlLanguage is initialised multiple times in different class loaders")
class HeaderReferenceCompletionTest : LightPlatformCodeInsightFixtureTestCase() {
  override fun getTestDataPath(): String = MarkdownTestingUtil.TEST_DATA_PATH + "/completion/headerAnchor/"

  fun testHeader1() {
    doTest()
  }

  fun testHeader2() {
    doTest()
  }

  fun testInBullet() {
    doTest()
  }

  fun testMultipleHeaders() {
    myFixture.testCompletionVariants(getBeforeFileName(),
                                     "environment-variables",
                                     "how-do-i-get-set-up",
                                     "mxbezier3scalar",
                                     "plugin-list",
                                     "requirements",
                                     "what-is-this-repository-for")
  }

  private fun getBeforeFileName() = getTestName(true) + ".zen"

  private fun doTest() {
    myFixture.testCompletion(getBeforeFileName(), getTestName(true) + "_after.zen")
  }

  fun testRelativePath() {
    myFixture.testCompletion("relativePath.zen", "relativePath_after.zen")
  }

  fun testAFileHeaders1() {
    myFixture.configureByFile("relativePath.zen")
    myFixture.testCompletionVariants(getBeforeFileName(), "aFileHeaders1.zen", "relativePath.zen")
  }

  fun testAFileHeaders2() {
    myFixture.configureByFile("multipleHeaders.zen")
    myFixture.testCompletionVariants(getBeforeFileName(), "environment-variables",
                                     "how-do-i-get-set-up",
                                     "mxbezier3scalar",
                                     "plugin-list",
                                     "requirements",
                                     "what-is-this-repository-for")
  }

  fun testGfmLowercase() {
    myFixture.testCompletionVariants(getBeforeFileName(), "what-is-this-repository-for", "what-is-this-for")
  }

  fun testGfmPunctuation() {
    myFixture.testCompletionVariants(getBeforeFileName(), "100-april-8-2018", "100-april-82018")
  }

  fun testGfmSpecial() {
    myFixture.testCompletionVariants(getBeforeFileName(), "get-method", "get-call")
  }


}