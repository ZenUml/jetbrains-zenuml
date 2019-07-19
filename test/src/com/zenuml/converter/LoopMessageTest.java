package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class LoopMessageTest extends BaseDslConversionTest {

    public void test_method1() {
        testDslConversion(
                "method1",
                "LoopMessage.method1() {\n\twhile(true) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_method2() {
        testDslConversion(
                "method2",
                "LoopMessage.method2() {\n\twhile(1 + 1 > 1) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_method3() {
        testDslConversion(
                "method3",
                "LoopMessage.method3() {\n\twhile(1 + 1 > 1 && isFoo) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_method4() {
        testDslConversion(
                "method4",
                "LoopMessage.method4() {\n\twhile(1 + 1 > 1 && isFoo || 2 > 1) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_method5() {
        testDslConversion(
                "method5",
                "LoopMessage.method5() {\n\twhile((isBar()) && 2>1) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_method6() {
        testDslConversion(
                "method6",
                "LoopMessage.method6() {\n\twhile(getCount() > 1) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_methodWithChainedMethodCalls() {
        testDslConversion(
                "methodWithChainedMethodCalls",
                "LoopMessage.methodWithChainedMethodCalls() {\n\twhile(new Foo().getBar().isBar()) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_methodWithFor() {
        testDslConversion(
                "methodWithFor",
                "LoopMessage.methodWithFor() {\n\tfor(i < 10) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_methodWithForOnConsignments() {
        testDslConversion(
                "methodWithForOnConsignments",
                "LoopMessage.methodWithForOnConsignments() {\n\tforEach(integers) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "loopMessage.LoopMessage";
    }
}
