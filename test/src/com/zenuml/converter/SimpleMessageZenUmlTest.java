package com.zenuml.converter;

import org.jetbrains.annotations.NotNull;

public class SimpleMessageZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_simpleMessage() {
        testDslConversion("clientMethod", "SimpleMessage.clientMethod() {\n}\n");
    }

    public void test_convert_to_dsl_simpleMessage_declare_a_variable() {
        testDslConversion("declareVariable", "SimpleMessage.declareVariable() {\n\t// String s;\n}\n");
    }
    public void test_convert_to_dsl_simpleMessage_return_a_constant() {
        testDslConversion("returnConstant", "SimpleMessage.returnConstant() {\n\t// return 0;\n}\n");
    }

    public void test_convert_to_dsl_simpleMessage_return_multiple_line_expression() {
        testDslConversion("return_multiple_line_expression", "SimpleMessage.return_multiple_line_expression() {\n\t// return someInstance.getAll()\n\t//                 .first();\n}\n");
    }

    public void test_convert_to_dsl_nestedMessage() {
        testDslConversion("nestedMethod", "SimpleMessage.nestedMethod() {\n\tclientMethod(lambda) {\n\t}\n}\n");
    }

    public void test_convert_to_dsl_nestedMessage_arguments() {
        testDslConversion("nestedMethod_arguments", "SimpleMessage.nestedMethod_arguments() {\n\tclientMethod(1, 2) {\n\t}\n}\n");
    }
    public void test_convert_to_dsl_nestedMessage_chain() {
        testDslConversion("nestedMethod_chain", "SimpleMessage.nestedMethod_chain() {\n\tclientMethod().isEmpty();\n}\n");
    }

    public void test_convert_to_dsl_nestedMessage_method_in_parameter() {
        testDslConversion("nestedMethod_method_in_parameter", "SimpleMessage.nestedMethod_method_in_parameter() {\n\tprocessInt(getCount()) {\n\t}\n}\n");
    }

    public void test_convert_to_dsl_nestedMessage_unresolvable_method() {
        testDslConversion("nestedMethod_unresolvable_method_call", "SimpleMessage.nestedMethod_unresolvable_method_call() {\n\tprintln();\n}\n");
    }

    public void test_convert_to_dsl_nestedMessage_with_assignment() {
        testDslConversion("nestedMethod_with_assignment", "SimpleMessage.nestedMethod_with_assignment() {\n\tString s = clientMethod() {\n\t}\n}\n");
    }

    public void test_method_with_generic_type() {
        testDslConversion("method_with_generic_type", "SimpleMessage.method_with_generic_type() {\n\tList s = clientMethod() {\n\t}\n}\n");
    }

    public void test_method_with_generic_type_in_new_expression() {
        testDslConversion("method_with_generic_type_in_new_expression", "SimpleMessage.method_with_generic_type_in_new_expression() {\n\tArrayList a = new ArrayList();\n}\n");
    }

    public void test_method_with_generic_type_in_method_param() {
        testDslConversion("method_with_generic_type_in_method_param", "SimpleMessage.method_with_generic_type_in_method_param() {\n\tfoo(new ArrayList());\n}\n");
    }

    public void test_method_with_try_catch() {
        testDslConversion("method_with_try_catch", "SimpleMessage.method_with_try_catch() {\n\ttry {\n\t\tfoo();\n\t}\n\tcatch(Exception e) {\n\t\tbar();\n\t}\n}\n");
    }

    public void test_method_with_try_catch_throw() {
        testDslConversion("method_with_try_catch_throw", "SimpleMessage.method_with_try_catch_throw() {\n\ttry {\n\t\tfoo();\n\t}\n\tcatch(Exception e) {\n\t\tthrow(new MyException());\n\t}\n}\n");
    }

    public void test_method_with_try_catch_finally() {
        testDslConversion("method_with_try_catch_finally", "SimpleMessage.method_with_try_catch_finally() {\n\ttry {\n\t\tfoo();\n\t}\n\tcatch(Exception1 e) {\n\t\thandleException1();\n\t}\n\tcatch(Exception2 , Exception3 e) {\n\t\thandleExceptions();\n\t}\n\tfinally {\n\t\tcleanup();\n\t}\n}\n");
    }

    public void test_method_with_anonymous_object() {
        testDslConversion("method_with_anonymous_object", "SimpleMessage.method_with_anonymous_object() {\n\tnew Runnable();\n}\n");
    }

    public void test_method_with_anonymous_object_and_initialization() {
        testDslConversion("method_with_anonymous_object_and_initialization", "SimpleMessage.method_with_anonymous_object_and_initialization() {\n\tArrayList numbers = new ArrayList();\n}\n");
    }

    public void test_method_with_new_int_array() {
        testDslConversion("method_with_new_int_array", "SimpleMessage.method_with_new_int_array() {\n\tint_array numbers = new int_array();\n\t {\n\t}\n}\n");
    }

    public void test_method_with_array_literal() {
        testDslConversion("method_with_array_literal", "SimpleMessage.method_with_array_literal() {\n\t// int_array numbers = { 1 };\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "simpleMessage.SimpleMessage";
    }
}
