package com.zenuml.dsl;

import org.jetbrains.annotations.NotNull;
import java.util.stream.IntStream;

public class ZenDsl {
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

    void addRemainder(String remainder) {
        // because body of the while statement was process before.
        level--;
        appendIndent();
        level++;
        append(remainder);
    }

    void keepHead(int pos) {
        dsl.replace(0, dsl.length(), dsl.substring(0, pos));
    }

    @NotNull
    ZenDsl append(String s) {
        dsl.append(s);
        return this;
    }

    ZenDsl appendIndent() {
        String indent = getIndent();
        return append(indent);
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
    }

    private ZenDsl whiteSpace() {
        return append(" ");
    }

    void closeBlock() {
        levelDecrease();
        appendIndent()
        .append("}")
        .changeLine();
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
