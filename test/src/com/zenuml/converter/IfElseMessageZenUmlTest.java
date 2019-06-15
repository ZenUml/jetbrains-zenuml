package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IfElseMessageZenUmlTest extends ZenUmlTestCase {

    public void test_convert_to_dsl_ifMessage() {
        PsiMethod clientMethod = getPsiMethod("ifMessage", "ifMessage.IfMessage", "nestedMethod");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat("Actual:\n" + dsl, dsl, is("IfMessage.nestedMethod() {\n\tint i = \tIfMessage.clientMethod() {\n\t\tIfMessage.foo();\n\t}\n\t\tIfMessage.foo();\n\t\n\tif(true) {\n\t\t\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tif(true) {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tif(true) {\n\t\t\t\n\t\t}\n\t\tif(true) {\n\t\t\t\n\t\t}\n\t}\n}\n"));
    }

    public void test_convert_to_dsl_ifMessage_reference_expression_as_condition() {
        PsiMethod clientMethod = getPsiMethod("ifMessage", "ifMessage.IfMessage", "nestedMethod1");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("IfMessage.nestedMethod1() {\n\tif(condition) {\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t}\n}\n"));
    }

    public void test_convert_to_dsl_ifMessage__expression_as_condition() {
        PsiMethod clientMethod = getPsiMethod("ifMessage", "ifMessage.IfMessage", "nestedMethod2");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("IfMessage.nestedMethod2() {\n\tif(1 + 1 == 2) {\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t}\n}\n"));
    }

    public void test_convert_to_dsl_ifMessage_binary_expression_as_condition() {
        PsiMethod clientMethod = getPsiMethod("ifMessage", "ifMessage.IfMessage", "nestedMethod3");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("IfMessage.nestedMethod3() {\n\tList<Object> list = new ArrayList<>();\n\tif(list.size() == 2) {\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t}\n}\n"));
    }
}
