package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ZenDsl {
    private static final Logger LOG = Logger.getInstance(ZenDsl.class);

    private StringBuffer dsl = new StringBuffer();
    private int level = 0;

    String getDsl() {
        return dsl.toString();
    }

    @NotNull
    ZenDsl append(String s) {
        ensureIndent();
        dsl.append(s);
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
}
