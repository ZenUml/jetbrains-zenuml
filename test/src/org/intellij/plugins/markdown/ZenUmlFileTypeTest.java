package org.intellij.plugins.markdown;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.HeavyPlatformTestCase;
import com.intellij.testFramework.SkipSlowTestLocally;
import com.intellij.testFramework.fixtures.SkipWithExecutionPolicy;
import org.intellij.plugins.markdown.lang.ZenUmlFileType;
import org.intellij.plugins.markdown.lang.ZenUmlLanguage;
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile;
import org.junit.Ignore;

import java.io.IOException;

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

  private void doTest(String extension) throws IOException {
    VirtualFile virtualFile = getTempDir().createVirtualFile(extension);
    PsiFile psi = getPsiManager().findFile(virtualFile);
    assertEquals("ZenUML", psi.getLanguage().getID());
    assertEquals("ZenUML", virtualFile.getFileType().getName());
  }
}
