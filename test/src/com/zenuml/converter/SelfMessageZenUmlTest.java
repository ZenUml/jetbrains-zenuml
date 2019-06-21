package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class SelfMessageZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_node_selfMessage() {
        testDslConversion("clientMethod", "SelfMessage.clientMethod() {\n\tinternalMethodA(1) {\n\t\tinternalMethodB(100, 1000) {\n\t\t\tinternalMethodC(i1, i);\n\t\t}\n\t}\n\tinternalMethodB(2) {\n\t\tinternalMethodC(i1, i);\n\t}\n\tinternalMethodC(3);\n}\n");
    }

    public void test_convert_to_dsl_node_selfMessage_nest_2_levels() {
        testDslConversion("clientMethod2", "SelfMessage.clientMethod2() {\n\tint i = internalMethodA(i) {\n\t\tinternalMethodB(100, 1000) {\n\t\t\tinternalMethodC(i1, i);\n\t\t}\n\t}\n}\n");
    }

    @Override
    @NotNull
    protected String getClassName() {
        return "selfMessage.SelfMessage";
    }
}
