package org.intellij.plugins.markdown.ui.preview.javafx;

import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.intellij.util.ArrayUtil;
import com.zenuml.dsl.DslEscaper;
import com.zenuml.sequence.plugins.jetbrains.html.ZenUmlHtmlGenerator;
import org.apache.commons.io.FileUtils;
import org.intellij.plugins.markdown.html.AddProtocolAndHost;
import org.intellij.plugins.markdown.ui.preview.MarkdownHtmlPanel;
import org.intellij.plugins.markdown.ui.preview.PreviewStaticServer2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class MarkdownJavaFxHtmlPanel extends JCEFHtmlPanel implements MarkdownHtmlPanel {

  @NotNull
  private String[] myCssUris = ArrayUtil.EMPTY_STRING_ARRAY;
  @NotNull
  private String myLastRawHtml = "";
  @NotNull
  private String myLastHtmlWithCss = "";

  public MarkdownJavaFxHtmlPanel() {
    super(null);
    String resource = "/html/zenuml/index.html";
    InputStream inputStream = this.getClass().getResourceAsStream(resource);
    if (inputStream == null) {
      throw new IllegalStateException(String.format("Resource %s not found", resource));
    }

    setHtml(readFromInputStream(inputStream));
  }

  private String readFromInputStream(InputStream inputStream) {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    } catch (IOException e) {
      return "";
    }

    return resultStringBuilder.toString();
  }

  public File writeHtmlToTempFile(String dsl) {
    try {
      File file = File.createTempFile("zenuml", ".html");
      dsl = DslEscaper.removeBacktick.apply(dsl);

      String withDsl = new ZenUmlHtmlGenerator().from(dsl);
      prepareHtml(withDsl);
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

    // We have to use ">" + "<head>", because otherwise it will be replaced by this method itself.
    this.myLastHtmlWithCss = html
            .replace(">" + "<head>", ">"+"<head>"
                    + MarkdownHtmlPanel.getCssLines(null, myCssUris) + "\n");
    this.myLastHtmlWithCss = AddProtocolAndHost.addProtocolAndHost(this.myLastHtmlWithCss, PreviewStaticServer2.getBaseUrl());
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
  public void render(@NotNull String text) {
    text = DslEscaper.removeBacktick.apply(text);
    getCefBrowser().executeJavaScript("app.__vue__.$store.dispatch('updateCode', {code: `" + text + "`})", null, 0);
  }

  @Override
  public void scrollToMarkdownSrcOffset(int offset) {

  }

}
