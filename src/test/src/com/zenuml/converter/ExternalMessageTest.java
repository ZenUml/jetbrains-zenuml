package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class ExternalMessageTest extends BaseDslConversionTest {

    public void test_methodWithUnresolvableMethodCall() {
        testDslConversion(
                "methodWithUnresolvableMethodCall",
                "ExternalMessage.methodWithUnresolvableMethodCall() {\n\tUnknownClass.method();\n\tfoo() {\n\t}\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "simpleMessage.ExternalMessage";
    }
}
