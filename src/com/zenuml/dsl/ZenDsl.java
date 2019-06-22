package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.stream.IntStream;

public class ZenDsl {
    private static final Logger LOG = Logger.getInstance(ZenDsl.class);

    private StringBuffer dsl = new StringBuffer();
    private int level = 0;

    String getIndent() {
        return IntStream.range(0, level)
                .mapToObj(i -> "\t")
                .reduce((s1, s2) -> s1 + s2)
                .orElse("");
    }

    public String getDsl() {
        return dsl.toString();
    }

    @NotNull
    ZenDsl append(String s) {
        ensureIndent();
        dsl.append(s);
        LOG.debug(dsl.toString());
        return this;
    }

    ZenDsl ensureIndent() {
        if(!dsl.toString().endsWith("\n")) {
            return this;
        }
        String indent = getIndent();
        dsl.append(indent);
        return this;
    }

    private void levelIncrease() {
        level++;
    }

    private void levelDecrease() {
        level--;
    }

    @NotNull
    ZenDsl closeExpressionAndNewLine() {
        return append(";").changeLine();
    }

    void appendAssignment(String type, String name) {
        append(type)
        .whiteSpace()
        .append(name)
        .whiteSpace()
        .append("=")
        .whiteSpace();
    }

    void startBlock() {
        whiteSpace()
        .append("{")
        .changeLine();
        levelIncrease();
        LOG.debug("StartBlock");
    }

    private ZenDsl whiteSpace() {
        return append(" ");
    }

    void closeBlock() {
        levelDecrease();
        ensureIndent()
        .append("}")
        .changeLine();
        LOG.debug("CloseBlock");
    }

    @NotNull
    ZenDsl openParenthesis() {
        return append("(");
    }

    @NotNull
    ZenDsl closeParenthesis() {
        return append(")");
    }

    ZenDsl changeLine() {
        return append("\n");
    }

    public ZenDsl comment(String text) {
        return append("// ").append(text).changeLine();
    }
}
