package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;
import org.jetbrains.annotations.NotNull;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IfElseMessageZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_ifMessage() {
        testDslConversion("nestedMethod", "IfMessage.nestedMethod() {\n" +
            "\tint i = clientMethod() {\n" +
            "\t\tfoo() {\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\t\"i\" = \"2\";\n" +
            "\tif(true) {\n" +
            "\t\tclientMethod() {\n" +
            "\t\t\tfoo() {\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t\tif(true) {\n" +
            "\t\t\tfoo() {\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t\tif(true) {\n" +
            "\t\t\t\"i\" = \"5\";\n" +
            "\t\t}\n" +
            "\t\tif(true) {\n" +
            "\t\t\t\"i\" = \"6\";\n" +
            "\t\t}\n" +
            "\t}\n" +
            "}\n");
    }

    public void test_convert_to_dsl_ifMessage_reference_expression_as_condition() {
        testDslConversion("nestedMethod1", "IfMessage.nestedMethod1() {\n\tif(condition) {\n\t\tclientMethod() {\n\t\t\tfoo() {\n\t\t\t}\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo() {\n\t\t\t}\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage__expression_as_condition() {
        testDslConversion("nestedMethod2", "IfMessage.nestedMethod2() {\n\tif(1 + 1 == 2) {\n\t\tclientMethod() {\n\t\t\tfoo() {\n\t\t\t}\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo() {\n\t\t\t}\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage_binary_expression_as_condition() {
        testDslConversion("nestedMethod3", "IfMessage.nestedMethod3() {\n\tList list = new ArrayList();\n\tif(list.size() == 2) {\n\t\tclientMethod() {\n\t\t\tfoo() {\n\t\t\t}\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo() {\n\t\t\t}\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage_with_chained_method_calls() {
        testDslConversion("methodWithChainedMethodCalls", "IfMessage.methodWithChainedMethodCalls() {\n\tif(new Foo().getBar().isBar()) {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_methodWithElse() {
        testDslConversion("methodWithElse", "IfMessage.methodWithElse() {\n\tif(true) {\n\t\tfoo() {\n\t\t}\n\t}\n\telse  {\n\t\tfoo() {\n\t\t}\n\t}\n}\n");
    }

    public void test_methodWithElseIf() {
        testDslConversion("methodWithElseIf", "IfMessage.methodWithElseIf() {\n\tif(true) {\n\t\tfoo() {\n\t\t}\n\t}\n\telse if(2 > 1) {\n\t\tfoo2();\n\t}\n\telse  {\n\t\tfoo3();\n\t}\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "ifMessage.IfMessage";
    }
}
