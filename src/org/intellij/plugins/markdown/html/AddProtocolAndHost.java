package org.intellij.plugins.markdown.html;

public class AddProtocolAndHost {
    public static String addProtocolAndHost(String html, String protocolAndHost) {
        // add protocol and host to script tag in Java code


        // also replace the script tag
        return html.replaceAll("(?i)<script src=\"(.*?)\"", "<script src=\"" + protocolAndHost + "" + "$1\"")
                .replaceAll("(?<=href=\")(?!http)", protocolAndHost);
    }
}
