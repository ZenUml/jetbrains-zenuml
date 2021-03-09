package org.intellij.plugins.markdown;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.HeavyPlatformTestCase;
import com.intellij.testFramework.SkipSlowTestLocally;
import com.intellij.testFramework.fixtures.SkipWithExecutionPolicy;
import org.intellij.plugins.markdown.lang.ZenUmlFileType;
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile;
import org.junit.Ignore;

import java.io.IOException;

@Ignore("Failing due to ZenUmlLanguage is initialised multiple times in different class loaders")
public class ZenUmlFileTypeTest extends HeavyPlatformTestCase {
  public void testZExtension() throws IOException {
    doTest(".z");
  }
  public void testZenExtension() throws IOException {
    doTest(".zen");
  }
  public void testZenUmlExtension() throws IOException {
    doTest(".zenuml");
  }

  public void testMdExtension() throws IOException {
    doTest(".zen");
  }

  private void doTest(String extension) throws IOException {
    VirtualFile virtualFile = getTempDir().createVirtualFile(extension);
    PsiFile psi = getPsiManager().findFile(virtualFile);
    assertTrue(psi instanceof MarkdownFile);
    assertEquals(ZenUmlFileType.INSTANCE, virtualFile.getFileType());
  }
}
