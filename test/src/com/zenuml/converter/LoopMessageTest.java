package com.zenuml.converter;

public class LoopMessageTest extends BaseDslConversionTest {

    public void test_method1() {
        testDslConversion("loopMessage",
                "LoopMessage",
                "method1",
                "LoopMessage.method1() {\n\twhile(true) {\n\t\tfoo();\n\t}\n}\n");
    }

    public void test_method2() {
        testDslConversion("loopMessage",
                "LoopMessage",
                "method2",
                "LoopMessage.method2() {\n\twhile(1 + 1 > 1) {\n\t\tfoo();\n\t}\n}\n");
    }

    public void test_method3() {
        testDslConversion("loopMessage",
                "LoopMessage",
                "method3",
                "LoopMessage.method3() {\n\twhile(1 + 1 > 1 && isFoo) {\n\t\tfoo();\n\t}\n}\n");
    }
}
