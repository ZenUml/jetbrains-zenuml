package org.intellij.plugins.markdown.html;

import org.junit.Assert;
import org.junit.Test;

public class AddProtocolAndHostTest {
    @Test
    public void test_should_enhance_html_with_protocol_and_host_link() {
        String html = "<a href=\"/foo\">foo</a>";
        String expected = "<a href=\"http://example.com/foo\">foo</a>";
        Assert.assertEquals(expected, AddProtocolAndHost.addProtocolAndHost(html, "http://localhost:63343/api/markdown-preview/"));
    }

    @Test
    public void test_should_enhance_html_with_protocol_and_host_script() {
        String html = "<script src=\"/js/chunk-vendors.559e3696.js\"></script>\n";
        String expected = "<script src=\"http://example.com/js/chunk-vendors.559e3696.js\"></script>\n";
        Assert.assertEquals(expected, AddProtocolAndHost.addProtocolAndHost(html, "http://example.com"));
    }

    @Test
    public void test_should_enhance_html_with_protocol_and_host_real_case() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\n" +
                "    <link rel=\"icon\" href=\"/favicon.ico\">\n" +
                "    <title>jetbrains-viewer</title>\n" +
                "    <link href=\"/js/about.17eb60dc.js\" rel=\"prefetch\">\n" +
                "    <link href=\"/css/app.c412605a.css\" rel=\"preload\" as=\"style\">\n" +
                "    <link href=\"/js/app.241acdf2.js\" rel=\"preload\" as=\"script\">\n" +
                "    <link href=\"/js/chunk-vendors.559e3696.js\" rel=\"preload\" as=\"script\">\n" +
                "    <link href=\"/css/app.c412605a.css\" rel=\"stylesheet\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<noscript><strong>We're sorry but jetbrains-viewer doesn't work properly without JavaScript enabled. Please enable it to\n" +
                "    continue.</strong></noscript>\n" +
                "<div id=\"app\"></div>\n" +
                "<script src=\"/js/chunk-vendors.559e3696.js\"></script>\n" +
                "<script src=\"/js/app.241acdf2.js\"></script>\n" +
                "</body>\n" +
                "</html>";
        String expected = "<!DOCTYPE html>\n" +
                "<html lang=\"\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\n" +
                "    <link rel=\"icon\" href=\"http://example.com/favicon.ico\">\n" +
                "    <title>jetbrains-viewer</title>\n" +
                "    <link href=\"http://example.com/js/about.17eb60dc.js\" rel=\"prefetch\">\n" +
                "    <link href=\"http://example.com/css/app.c412605a.css\" rel=\"preload\" as=\"style\">\n" +
                "    <link href=\"http://example.com/js/app.241acdf2.js\" rel=\"preload\" as=\"script\">\n" +
                "    <link href=\"http://example.com/js/chunk-vendors.559e3696.js\" rel=\"preload\" as=\"script\">\n" +
                "    <link href=\"http://example.com/css/app.c412605a.css\" rel=\"stylesheet\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<noscript><strong>We're sorry but jetbrains-viewer doesn't work properly without JavaScript enabled. Please enable it to\n" +
                "    continue.</strong></noscript>\n" +
                "<div id=\"app\"></div>\n" +
                "<script src=\"http://example.com/js/chunk-vendors.559e3696.js\"></script>\n" +
                "<script src=\"http://example.com/js/app.241acdf2.js\"></script>\n" +
                "</body>\n" +
                "</html>";
        Assert.assertEquals(expected, AddProtocolAndHost.addProtocolAndHost(html, "http://example.com"));
    }
}
