package org.intellij.plugins.markdown;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.HeavyPlatformTestCase;
import org.intellij.plugins.markdown.lang.MarkdownFileType;
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile;

import java.io.File;
import java.io.IOException;

public class MarkdownFileTypeTest extends HeavyPlatformTestCase {
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
    assertEquals(MarkdownFileType.INSTANCE, virtualFile.getFileType());
  }
}
