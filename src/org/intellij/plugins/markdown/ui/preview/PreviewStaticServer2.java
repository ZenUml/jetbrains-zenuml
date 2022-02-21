// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.plugins.markdown.ui.preview;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Url;
import com.intellij.util.Urls;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.intellij.plugins.markdown.settings.MarkdownCssSettings;
import org.intellij.plugins.markdown.ui.preview.javafx.MarkdownJavaFxHtmlPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.ide.BuiltInServerManager;
import org.jetbrains.ide.HttpRequestHandler;
import org.jetbrains.io.FileResponses;
import org.jetbrains.io.Responses;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PreviewStaticServer2 extends HttpRequestHandler {
  public static final String INLINE_CSS_FILENAME = "inline.css";
  private static final Logger LOG = Logger.getInstance(PreviewStaticServer2.class);
  private static final String PREFIX = "/api/markdown-preview/";

  @Nullable
  private byte[] myInlineStyleBytes = null;
  private long myInlineStyleTimestamp = 0;

  public static PreviewStaticServer2 getInstance() {
    return HttpRequestHandler.Companion.getEP_NAME().findExtension(PreviewStaticServer2.class);
  }

  @NotNull
  public static String getBaseUrl() {
    return "http://localhost:" + BuiltInServerManager.getInstance().getPort() + PREFIX;
  }

  @NotNull
  public static String getStaticUrl(@NotNull String staticPath) {
    Url url = Urls.parseEncoded("http://localhost:" + BuiltInServerManager.getInstance().getPort() + PREFIX + staticPath);
    return BuiltInServerManager.getInstance().addAuthToken(Objects.requireNonNull(url)).toExternalForm();
  }

  @NotNull
  public static String getScriptUrl(@NotNull String scriptFileName) {
    return getStaticUrl("scripts/" + scriptFileName);
  }

  @NotNull
  public static String getStyleUrl(@NotNull String scriptFileName) {
    return getStaticUrl("styles/" + scriptFileName);
  }

  public void setInlineStyle(@Nullable String inlineStyle) {
    myInlineStyleBytes = inlineStyle == null ? null : inlineStyle.getBytes(StandardCharsets.UTF_8);
    myInlineStyleTimestamp = System.currentTimeMillis();
  }

  @Override
  public boolean process(@NotNull QueryStringDecoder urlDecoder,
                         @NotNull FullHttpRequest request,
                         @NotNull ChannelHandlerContext context) {
    final String path = urlDecoder.path();


    final String payLoad = path;
    final List<String> typeAndName = StringUtil.split(payLoad, "/");

//    final String contentType = typeAndName.get(0);
    final String fileName = typeAndName.get(typeAndName.size() - 1);

      sendResource(request,
                   context.channel(),
                   MarkdownJavaFxHtmlPanel.class,
                   fileName);
//    }
//    else if ("styles".equals(contentType) && MarkdownHtmlPanel.STYLES.contains(fileName)) {
//      if (INLINE_CSS_FILENAME.equals(fileName)) {
//        sendInlineStyle(request, context.channel());
//      }
//      else {
//        sendResource(request,
//                     context.channel(),
//                     MarkdownCssSettings.class,
//                     fileName);
//      }
//    }
//    else {
//      return false;
//    }

    return true;
  }


  private void sendInlineStyle(@NotNull HttpRequest request, @NotNull Channel channel) {
    if (FileResponses.INSTANCE.checkCache(request, channel, myInlineStyleTimestamp)) {
      return;
    }

    if (myInlineStyleBytes == null) {
      Responses.send(HttpResponseStatus.NOT_FOUND, channel, request);
      return;
    }

    FullHttpResponse response =
      new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(myInlineStyleBytes));
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/css");
    response.headers().set(HttpHeaderNames.CACHE_CONTROL, "private, must-revalidate");
    response.headers().set(HttpHeaderNames.LAST_MODIFIED, new Date(myInlineStyleTimestamp));
    Responses.send(response, channel, request);
  }

  private static void sendResource(@NotNull HttpRequest request,
                                   @NotNull Channel channel,
                                   @NotNull Class<?> clazz,
                                   @NotNull String resourceName) {
    long lastModified = ApplicationInfo.getInstance().getBuildDate().getTimeInMillis();
    if (FileResponses.INSTANCE.checkCache(request, channel, lastModified)) {
      return;
    }

    byte[] data = new byte[0];
    // if filename ends with js, load it from resources/main/html/zenuml/js folder
    if (resourceName.endsWith(".js")) {
      try (final InputStream inputStream = PreviewStaticServer2.class.getResourceAsStream("/html/zenuml/js/" + resourceName)) {
        if (inputStream == null) {
          Responses.send(HttpResponseStatus.NOT_FOUND, channel, request);
          return;
        }

        data = FileUtilRt.loadBytes(inputStream);
      }
      catch (IOException e) {
        LOG.warn(e);
        Responses.send(HttpResponseStatus.INTERNAL_SERVER_ERROR, channel, request);
        return;
      }
    } else if (resourceName.endsWith(".css")) {
      try (final InputStream inputStream = PreviewStaticServer2.class.getResourceAsStream("/html/zenuml/css/" + resourceName)) {
        if (inputStream == null) {
          Responses.send(HttpResponseStatus.NOT_FOUND, channel, request);
          return;
        }

        data = FileUtilRt.loadBytes(inputStream);
      }
      catch (IOException e) {
        LOG.warn(e);
        Responses.send(HttpResponseStatus.INTERNAL_SERVER_ERROR, channel, request);
        return;
      }
    }

    FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(data));
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, FileResponses.INSTANCE.getContentType(resourceName));
    response.headers().set(HttpHeaderNames.CACHE_CONTROL, "private, must-revalidate");
    response.headers().set(HttpHeaderNames.LAST_MODIFIED, new Date(lastModified));
    Responses.send(response, channel, request);
  }
}
