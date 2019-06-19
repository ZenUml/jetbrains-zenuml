package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class DifferentClassesZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_node_differentClass() {
        testDslConversion("clientMethod", "FirstClass.clientMethod() {\n\tSecondClass secondClass = new SecondClass();\n\tSecondClass.method1();\n}\n");
    }

    public void test_convert_to_dsl_node_differentClass_multiple_calls() {
        testDslConversion("clientMethod_multiple_calls", "FirstClass.clientMethod_multiple_calls() {\n\tSecondClass secondClass = new SecondClass();\n\tSecondClass.method1();\n\tSecondClass.method1();\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "differentClass.FirstClass";
    }
}
