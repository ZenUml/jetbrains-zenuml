package com.zenuml.dsl;

import com.intellij.psi.PsiLocalVariable;
import org.jetbrains.annotations.NotNull;
import java.util.stream.IntStream;

public class ZenDsl {
    private StringBuffer dsl = new StringBuffer();
    private int level = 0;

    String getIndent() {
        return IntStream.range(0, level).mapToObj(i -> "\t").reduce((s1, s2) -> s1 + s2).orElse("");
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

    void addRemainder(String remainder) {
        level--;
        String indent = getIndent();
        level++;
        dsl.append(indent).append(remainder);
    }

    public void cut(int i, int index) {
        dsl.replace(i, dsl.length(), dsl.substring(i, index));
    }

    @NotNull
    ZenDsl append(String s) {
        dsl.append(s);
        return this;
    }

    void appendIndent() {
        String indent = getIndent();
        append(indent);
    }

    public void levelIncrease() {
        level++;
    }

    public void levelDecrease() {
        level--;
    }

    @NotNull
    ZenDsl closeExpressionAndNewLine() {
        dsl.append(";\n");
        return this;
    }

    String getRemainder(String cursorWord) {
        int index = getDsl().lastIndexOf(cursorWord);

        String remainder = getDsl().substring(index);
        cut(0, index);
        return remainder;
    }

    void appendAssignment(PsiLocalVariable variable) {
        append(variable.getType().getCanonicalText());
        append(" ");
        append(variable.getName());
        append(" = ");
    }
}
