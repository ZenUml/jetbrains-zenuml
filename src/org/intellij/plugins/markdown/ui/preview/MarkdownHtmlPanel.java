package org.intellij.plugins.markdown.ui.preview;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Range;
import com.intellij.util.containers.ContainerUtil;
import org.intellij.markdown.html.HtmlGenerator;
import org.intellij.plugins.markdown.settings.MarkdownCssSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;

import javax.swing.*;
import java.util.List;

public interface MarkdownHtmlPanel extends Disposable {

  List<String> SCRIPTS = ContainerUtil.immutableList(
          "about.7b0cdab9.js",
          "app.07962091.js",
          "chunk-vendors.6e699145.js");

  List<String> STYLES = ContainerUtil.immutableList(
          "darcula.css",
          "app.c412605a.css",
          "chunk-vendors.8fad9714.css",
          PreviewStaticServer2.INLINE_CSS_FILENAME
  );

  @NotNull
  JComponent getComponent();

  void setHtml(@NotNull String html);

  void setCSS(@Nullable String inlineCss, @NotNull String... fileUris);

  void render(@NotNull String text);

  void scrollToMarkdownSrcOffset(int offset);

  @Nullable
  static Range<Integer> nodeToSrcRange(@NotNull Node node) {
    if (!node.hasAttributes()) {
      return null;
    }
    final Node attribute = node.getAttributes().getNamedItem(HtmlGenerator.Companion.getSRC_ATTRIBUTE_NAME());
    if (attribute == null) {
      return null;
    }
    final List<String> startEnd = StringUtil.split(attribute.getNodeValue(), "..");
    if (startEnd.size() != 2) {
      return null;
    }
    return new Range<>(Integer.parseInt(startEnd.get(0)), Integer.parseInt(startEnd.get(1)));
  }

  @NotNull
  static String getCssLines(@Nullable String inlineCss, @NotNull String... fileUris) {
    StringBuilder result = new StringBuilder();

    for (String uri : fileUris) {
      uri = migrateUriToHttp(uri);
      result.append("<link rel=\"stylesheet\" href=\"").append(uri).append("\" />\n");
    }
    if (inlineCss != null) {
      result.append("<style>\n").append(inlineCss).append("\n</style>\n");
    }
    return result.toString();
  }

  static String migrateUriToHttp(@NotNull String uri) {
    if (uri.equals(MarkdownCssSettings.DEFAULT.getStylesheetUri())) {
      return PreviewStaticServer2.getStyleUrl("default.css");
    }
    else if (uri.equals(MarkdownCssSettings.DARCULA.getStylesheetUri())) {
      return PreviewStaticServer2.getStyleUrl("darcula.css");
    }
    else {
      return uri;
    }
  }
}
