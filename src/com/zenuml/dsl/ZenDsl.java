package com.zenuml.dsl;

public class ZenDsl {
    private StringBuffer dsl;

    public ZenDsl(StringBuffer dsl) {

        this.dsl = dsl;
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
        dsl.append(PsiToDslConverter.getIndent(level - 1) + remainder);
    }

    public void substring(int i, int index) {
        dsl.replace(i, dsl.length(), dsl.substring(i, index));
    }
}
