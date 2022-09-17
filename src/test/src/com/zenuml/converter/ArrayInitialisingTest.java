package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class ArrayInitialisingTest extends BaseDslConversionTest {

    public void test_convert_array_initialisation_in_parameter() {
        testDslConversion("method_with_array_initialization", "ArrayInitialising.method_with_array_initialization() {\n\tinternalMethod(new ArrayList());\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "converter.arrayInitialising.ArrayInitialising";
    }
}
