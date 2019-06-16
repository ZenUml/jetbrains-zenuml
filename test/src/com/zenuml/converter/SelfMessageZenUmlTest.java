package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class SelfMessageZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_node_selfMessage() {
        testDslConversion("clientMethod", "SelfMessage.clientMethod() {\n\tinternalMethodA() {\n\t\tinternalMethodB() {\n\t\t\tinternalMethodC();\n\t\t}\n\t}\n\tinternalMethodB() {\n\t\tinternalMethodC();\n\t}\n\tinternalMethodC();\n}\n");
    }

    public void test_convert_to_dsl_node_selfMessage_nest_2_levels() {
        testDslConversion("clientMethod2", "SelfMessage.clientMethod2() {\n\tint i = \tinternalMethodA() {\n\t\tinternalMethodB() {\n\t\t\tinternalMethodC();\n\t\t}\n\t}\n}\n");
    }

    @Override
    @NotNull
    protected String getClassName() {
        return "selfMessage.SelfMessage";
    }
}
