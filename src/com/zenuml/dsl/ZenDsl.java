package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import com.zenuml.license.CheckLicense;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ZenDsl {
    private static final Logger LOG = Logger.getInstance(ZenDsl.class);
    public static final String LICENSE_IS_NOT_VALID = "License.isNotValid";

    private StringBuffer dsl = new StringBuffer();
    private int level = 0;

    public static String escape(String input) {
        // remove the following characters: \, ", \n, \t, \r, `
        return input.replaceAll("[\\\\,\"\\n\\t\\r` ]", "");
    }

    String getDsl() {
        final boolean isLicensed = CheckLicense.isLicensed();
        if (!isLicensed) {
            return LICENSE_IS_NOT_VALID;
        }
        return dsl.toString();
    }

    @NotNull
    ZenDsl append(String s) {
        ensureIndent();
        dsl.append(s);
        return this;
    }

    @NotNull
    ZenDsl append(ZenDsl zenDsl) {
        ensureIndent();
        dsl.append(zenDsl.getDsl());
        return this;
    }

    @NotNull
    ZenDsl closeExpressionAndNewLine() {
        return append(";").changeLine();
    }

    void appendAssignment(String type, String name) {
        append(type).whiteSpace().append(name).whiteSpace().append("=").whiteSpace();
    }

    ZenDsl startBlock() {
        whiteSpace().append("{").changeLine();
        level++;
        return this;
    }

    ZenDsl closeBlock() {
        level--;
        append("}").changeLine();
        return this;
    }

    @NotNull
    ZenDsl openParenthesis() {
        return append("(");
    }

    @NotNull
    ZenDsl closeParenthesis() {
        return append(")");
    }

    // This method take care of the end change-line.
    ZenDsl comment(String text) {
        Arrays.stream(text.split("\n"))
                .map(line -> "// " + line)
                .forEach(line -> append(line).changeLine());
        return this;
    }

    ZenDsl ret(String text) {
        return append("return ").append("\"").append(escape(text)).append("\"")
                .closeExpressionAndNewLine();
    }
    private String getIndent() {
        return IntStream.range(0, level)
                .mapToObj(i -> "\t")
                .reduce((s1, s2) -> s1 + s2)
                .orElse("");
    }

    private ZenDsl changeLine() {
        return append("\n");
    }

    private ZenDsl whiteSpace() {
        return append(" ");
    }

    private ZenDsl ensureIndent() {
        if(!dsl.toString().endsWith("\n")) {
            return this;
        }
        String indent = getIndent();
        dsl.append(indent);
        return this;
    }

    ZenDsl appendParticipant(String participant) {
        dsl.append(participant).append(".");
        return this;
    }

    ZenDsl appendParams(String params) {
        return openParenthesis()
                .append(params)
                .closeParenthesis();
    }

    public ZenDsl quoted(String s) {
        return append("\"").append(escape(s)).append("\"");
    }
}
