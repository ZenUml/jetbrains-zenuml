package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class ConstructorMessageTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_constructor() {
        testDslConversion(
                "clientMethod",
                "Constructor.clientMethod() {\n\tConstructor c = new Constructor(){methodCallInConstructor()};\n}\n"

        );
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "constructor.Constructor";
    }
}
