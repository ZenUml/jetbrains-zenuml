package org.intellij.plugins.markdown.ui.preview.javafx;

import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.ui.jcef.JCEFHtmlPanel;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.ContainerUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import org.apache.commons.io.FileUtils;
import org.intellij.plugins.markdown.MarkdownBundle;
import org.intellij.plugins.markdown.ui.preview.MarkdownHtmlPanel;
import org.intellij.plugins.markdown.ui.preview.PreviewStaticServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class MarkdownJavaFxHtmlPanel extends JCEFHtmlPanel implements MarkdownHtmlPanel {

  private static final NotNullLazyValue<String> MY_SCRIPTING_LINES = new NotNullLazyValue<String>() {
    @NotNull
    @Override
    protected String compute() {
      return SCRIPTS.stream()
        .map(s -> "<script src=\"" + PreviewStaticServer.getScriptUrl(s) + "\"></script>")
        .reduce((s, s2) -> s + "\n" + s2)
        .orElseGet(String::new);
    }
  };

  @NotNull
  private String[] myCssUris = ArrayUtil.EMPTY_STRING_ARRAY;
  @NotNull
  private String myCSP = "";
  @NotNull
  private String myLastRawHtml = "";
  @NotNull
  private String myLastHtmlWithCss = "";
  @NotNull
  private final ScrollPreservingListener myScrollPreservingListener = new ScrollPreservingListener();
  @NotNull
  private final BridgeSettingListener myBridgeSettingListener = new BridgeSettingListener();

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
    html = html.replace("$VUE_SEQUENCE_BUNDLE_JS", PreviewStaticServer.getScriptUrl("vue-sequence-bundle.js"));
    html = html.replace("$VUE_SEQUENCE_EXT_CSS", PreviewStaticServer.getStyleUrl("vue-sequence-ext.css"));
    html = html.replace("$FONT_AWESOME_MIN_CSS", PreviewStaticServer.getScriptUrl("font-awesome.min.css"));
    myLastRawHtml = html;
    super.setHtml(html);
  }

  @NotNull
  @Override
  protected String prepareHtml(@NotNull String html) {
    if(html.length() == 0) return html;
    
    this.myLastHtmlWithCss = html
            .replace("<head>", "<head>"
                    + MarkdownHtmlPanel.getCssLines(null, myCssUris) + "\n" + getScriptingLines());
    return this.myLastHtmlWithCss;
  }

  @Override
  public void setCSS(@Nullable String inlineCss, @NotNull String... fileUris) {
    PreviewStaticServer.getInstance().setInlineStyle(inlineCss);
    myCssUris = inlineCss == null ? fileUris
                                  : ArrayUtil
                  .mergeArrays(fileUris, PreviewStaticServer.getStyleUrl(PreviewStaticServer.INLINE_CSS_FILENAME));
    myCSP = PreviewStaticServer.createCSP(ContainerUtil.map(SCRIPTS, s -> PreviewStaticServer.getScriptUrl(s)),
                                          ContainerUtil.concat(
                                            ContainerUtil.map(STYLES, s -> PreviewStaticServer.getStyleUrl(s)),
                                            ContainerUtil.filter(fileUris, s -> s.startsWith("http://") || s.startsWith("https://"))
                                          ));
    setHtml(myLastRawHtml);
  }

  @Override
  public void render() {

  }

  @Override
  public void scrollToMarkdownSrcOffset(int offset) {

  }

  @NotNull
  private static String getScriptingLines() {
    return MY_SCRIPTING_LINES.getValue();
  }

  @SuppressWarnings("unused")
  public static class JavaPanelBridge {
    static final JavaPanelBridge INSTANCE = new JavaPanelBridge();
    private static final NotificationGroup MARKDOWN_NOTIFICATION_GROUP =
      NotificationGroup.toolWindowGroup(MarkdownBundle.message("markdown.navigate.to.header.group"), ToolWindowId.MESSAGES_WINDOW);

    public void openInExternalBrowser(@NotNull String link) {
      SafeOpener.openLink(link);
    }

    public void log(@Nullable String text) {
      Logger.getInstance(JavaPanelBridge.class).warn(text);
    }
  }

  private class BridgeSettingListener implements ChangeListener<State> {
    @Override
    public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
    }
  }

  private class ScrollPreservingListener implements ChangeListener<State> {
    volatile int myScrollY = 0;

    @Override
    public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
    }
  }
}
