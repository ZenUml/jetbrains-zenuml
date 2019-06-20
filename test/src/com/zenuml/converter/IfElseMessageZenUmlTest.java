package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;
import org.jetbrains.annotations.NotNull;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IfElseMessageZenUmlTest extends BaseDslConversionTest {

    public void test_convert_to_dsl_ifMessage() {
        testDslConversion("nestedMethod", "IfMessage.nestedMethod() {\n\tint i = clientMethod() {\n\t\tfoo();\n\t}\n\t// i = 2\n\tfoo();\n\t// i = 3\n\tif(true) {\n\t\t// i = 4\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tif(true) {\n\t\t\tfoo();\n\t\t}\n\t\tif(true) {\n\t\t\t// i=5\n\t\t}\n\t\tif(true) {\n\t\t\t// i = 6\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage_reference_expression_as_condition() {
        testDslConversion("nestedMethod1", "IfMessage.nestedMethod1() {\n\tif(condition) {\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage__expression_as_condition() {
        testDslConversion("nestedMethod2", "IfMessage.nestedMethod2() {\n\tif(1 + 1 == 2) {\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage_binary_expression_as_condition() {
        testDslConversion("nestedMethod3", "IfMessage.nestedMethod3() {\n\tList<Object> list = new ArrayList<>();\n\tlist.size()\n\tif(list.size() == 2) {\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t\tclientMethod() {\n\t\t\tfoo();\n\t\t}\n\t}\n}\n");
    }

    public void test_convert_to_dsl_ifMessage_with_chained_method_calls() {
        testDslConversion("methodWithChainedMethodCalls", "IfMessage.methodWithChainedMethodCalls() {\n\tnew Foo();\n\tFoo.getBar() {\n\t\t// return new Bar();\n\t}\n\tBar.isBar() {\n\t\t// return true;\n\t}\n\tif(new Foo().getBar().isBar()) {\n\t\tfoo();\n\t}\n}\n");
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "ifMessage.IfMessage";
    }
}
