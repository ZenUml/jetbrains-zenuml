package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class LoopMessageTest extends BaseDslConversionTest {

    public void test_method1() {
        testDslConversion(
                "method1",
                "LoopMessage.method1() {\n\twhile(true) {\n\t\tfoo();\n\t}\n}\n");
    }

    public void test_method2() {
        testDslConversion(
                "method2",
                "LoopMessage.method2() {\n\twhile(1 + 1 > 1) {\n\t\tfoo();\n\t}\n}\n");
    }

    public void test_method3() {
        testDslConversion(
                "method3",
                "LoopMessage.method3() {\n\twhile(1 + 1 > 1 && isFoo) {\n\t\tfoo();\n\t}\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "loopMessage.LoopMessage";
    }
}
