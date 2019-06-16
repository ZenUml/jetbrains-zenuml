package com.zenuml.dsl;

import org.jetbrains.annotations.NotNull;

public class ZenDsl {
    private StringBuffer dsl = new StringBuffer();
    private int level = 0;

    static String getIndent(int number) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            builder.append('\t');
        }
        return builder.toString();
    }

    public void setDsl(String dsl) {
        this.dsl = new StringBuffer(dsl);
    }

    public StringBuffer getDsl() {
        return dsl;
    }

    public void addMethodCall(String methodCall) {
        dsl.append(methodCall);
    }

    void addRemainder(String remainder, int level) {
        dsl.append(getIndent(level - 1)).append(remainder);
    }

    public void cut(int i, int index) {
        dsl.replace(i, dsl.length(), dsl.substring(i, index));
    }

    @NotNull
    StringBuffer append(String s) {
        return dsl.append(s);
    }

    void appendIndent(int level) {
        String indent = getIndent(level);
        append(indent);
    }

    public void levelIncrease() {
        level++;
    }

    public void levelDecrease() {
        level--;
    }

    public int getLevel() {
        return level;
    }

}
