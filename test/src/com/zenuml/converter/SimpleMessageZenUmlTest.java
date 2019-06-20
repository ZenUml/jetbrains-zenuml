package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class SimpleMessageZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_simpleMessage() {
        testDslConversion("clientMethod", "SimpleMessage.clientMethod();\n");
    }

    public void test_convert_to_dsl_simpleMessage_declare_a_variable() {
        testDslConversion("declareVariable", "SimpleMessage.declareVariable() {\n\t// String s;\n}\n");
    }

    public void test_convert_to_dsl_nestedMessage() {
        testDslConversion("nestedMethod", "SimpleMessage.nestedMethod() {\n\tclientMethod();\n}\n");
    }

    public void test_convert_to_dsl_nestedMessage_method_in_parameter() {
        testDslConversion("nestedMethod_method_in_parameter", "SimpleMessage.nestedMethod_method_in_parameter() {\n\tgetCount() {\n\t\t// return 0;\n\t}\n\tprocessInt(getCount());\n}\n");
    }

    public void test_convert_to_dsl_nestedMessage_unresolvable_method() {
        testDslConversion("nestedMethod_unresolvable_method_call", "SimpleMessage.nestedMethod_unresolvable_method_call() {\n\tprintln()\n}\n");
    }

    public void test_convert_to_dsl_nestedMessage_with_assignment() {
        testDslConversion("nestedMethod_with_assignment", "SimpleMessage.nestedMethod_with_assignment() {\n\tString s = clientMethod();\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "simpleMessage.SimpleMessage";
    }
}
