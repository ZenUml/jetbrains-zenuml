package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class DifferentClassesZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_node_differentClass() {
        testDslConversion("clientMethod", "FirstClass.clientMethod() {\n\tSecondClass secondClass = new SecondClass();\n\tsecondClass.method1() {\n\t}\n}\n");
    }

    public void test_convert_to_dsl_node_differentClass_multiple_calls() {
        testDslConversion("clientMethod_multiple_calls", "FirstClass.clientMethod_multiple_calls() {\n\tSecondClass secondClass = new SecondClass();\n\tsecondClass.method1() {\n\t}\n\tsecondClass.method1() {\n\t}\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "differentClass.FirstClass";
    }
}
