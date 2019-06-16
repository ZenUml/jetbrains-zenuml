package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;
import org.jetbrains.annotations.NotNull;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IfElseMessageZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_ifMessage() {
        testDslConversion("nestedMethod", "IfMessage.nestedMethod() {\n\tint i = \tclientMethod() {\n\t\tfoo();\n\t}\n\t// i = 2\n\tfoo();\n\t// i = 3\n\tif(true) {\n\t\t// i = 4\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tif(true) {\n\t\t\tfoo();\n\t\t}\n\t\tif(true) {\n\t\t\t// i=5\n\t\t}\n\t\tif(true) {\n\t\t\t// i = 6\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage_reference_expression_as_condition() {
        testDslConversion("nestedMethod1", "IfMessage.nestedMethod1() {\n\tif(condition) {\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage__expression_as_condition() {
        testDslConversion("nestedMethod2", "IfMessage.nestedMethod2() {\n\tif(1 + 1 == 2) {\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage_binary_expression_as_condition() {
        testDslConversion("nestedMethod3", "IfMessage.nestedMethod3() {\n\tList<Object> list = new ArrayList<>();\n\tif(list.size() == 2) {\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t}\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "ifMessage.IfMessage";
    }
}
