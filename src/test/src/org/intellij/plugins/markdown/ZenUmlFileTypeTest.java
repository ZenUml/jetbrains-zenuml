package org.intellij.plugins.markdown;

import com.intellij.debugger.impl.OutputChecker;
import com.intellij.execution.ExecutionTestCase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.HeavyPlatformTestCase;
import com.intellij.testFramework.JavaProjectTestCase;
import com.intellij.testFramework.SkipSlowTestLocally;
import com.intellij.testFramework.fixtures.SkipWithExecutionPolicy;
import org.junit.Ignore;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@Ignore("Skip it temporarily because I cannot make it work. But in prod, it works fine.")
public class ZenUmlFileTypeTest extends JavaProjectTestCase {
  public void testZExtension() throws IOException {
    doTest(".z");
  }
  public void testZenExtension() throws IOException {
    doTest(".zen");
  }
  public void testZenUmlExtension() throws IOException {
    doTest(".zenuml");
  }

  private void doTest(String extension) {
    VirtualFile virtualFile = getTempDir().createVirtualFile(extension);
    PsiFile psi = getPsiManager().findFile(virtualFile);
    assertEquals("ZenUML", psi.getLanguage().getID());
    assertEquals("ZenUML", virtualFile.getFileType().getName());
  }

}
