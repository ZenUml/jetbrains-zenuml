package org.intellij.plugins.markdown.ui.preview.javafx;

import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.intellij.util.ArrayUtil;
import org.apache.commons.io.FileUtils;
import org.intellij.plugins.markdown.ui.preview.MarkdownHtmlPanel;
import org.intellij.plugins.markdown.ui.preview.PreviewStaticServer2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class MarkdownJavaFxHtmlPanel extends JCEFHtmlPanel implements MarkdownHtmlPanel {

  @NotNull
  private String[] myCssUris = ArrayUtil.EMPTY_STRING_ARRAY;
  @NotNull
  private String myLastRawHtml = "";
  @NotNull
  private String myLastHtmlWithCss = "";

  public MarkdownJavaFxHtmlPanel() {
    super(null);
  }

  public File writeHtmlToTempFile() {
    try {
      File file = File.createTempFile("zenuml", ".html");
      FileUtils.writeStringToFile(file, this.myLastHtmlWithCss, "UTF-8");
      return file;
    } catch (IOException e) {
      throw new RuntimeException("Failed to create temp file");
    }
  }

  @Override
  public void setHtml(@NotNull String html) {
    myLastRawHtml = html;
    super.setHtml(html);
  }

  @NotNull
  @Override
  protected String prepareHtml(@NotNull String html) {
    if(html.length() == 0) return html;
    
    this.myLastHtmlWithCss = html
            .replace("<head>", "<head>"
                    + MarkdownHtmlPanel.getCssLines(null, myCssUris) + "\n");
    return this.myLastHtmlWithCss;
  }

  @Override
  public void setCSS(@Nullable String inlineCss, @NotNull String... fileUris) {
    PreviewStaticServer2.getInstance().setInlineStyle(inlineCss);
    myCssUris = inlineCss == null ? fileUris
                                  : ArrayUtil
                  .mergeArrays(fileUris, PreviewStaticServer2.getStyleUrl(PreviewStaticServer2.INLINE_CSS_FILENAME));
    setHtml(myLastRawHtml);
  }

  @Override
  public void render() {

  }

  @Override
  public void scrollToMarkdownSrcOffset(int offset) {

  }

}
