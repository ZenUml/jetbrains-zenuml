package com.zenuml.converter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.zenuml.dsl.PsiToDslConverter;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IfElseMessageZenUmlTest extends ZenUmlTestCase {

    private PsiToDslConverter psiToDslConverter;

    public void setUp() throws Exception {
        super.setUp();
        psiToDslConverter = new PsiToDslConverter();
    }

    public void test_convert_to_dsl_ifMessage() {
        myFixture.copyDirectoryToProject("ifMessage","");
        PsiClass selfMessageClass = myFixture.findClass("ifMessage.IfMessage");
        PsiMethod clientMethod = selfMessageClass.findMethodsByName("nestedMethod", true)[0];
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat("Actual:\n" + dsl, dsl, is("IfMessage.nestedMethod() {\n\tIfMessage.clientMethod() {\n\t\tIfMessage.foo();\n\t}\n\tIfMessage.foo();\n\tif(true) {\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tif(true) {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tif(true) {\n\t\t}\n\t\tif(true) {\n\t\t}\n\t}\n}\n"));
    }

    public void test_convert_to_dsl_ifMessage_reference_expression_as_condition() {
        myFixture.copyDirectoryToProject("ifMessage","");
        PsiClass selfMessageClass = myFixture.findClass("ifMessage.IfMessage");
        PsiMethod clientMethod = selfMessageClass.findMethodsByName("nestedMethod1", true)[0];
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("IfMessage.nestedMethod1() {\n\tif(condition) {\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t}\n}\n"));
    }

    public void test_convert_to_dsl_ifMessage__expression_as_condition() {
        myFixture.copyDirectoryToProject("ifMessage","");
        PsiClass selfMessageClass = myFixture.findClass("ifMessage.IfMessage");
        PsiMethod clientMethod = selfMessageClass.findMethodsByName("nestedMethod2", true)[0];
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("IfMessage.nestedMethod2() {\n\tif(1 + 1 == 2) {\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t\tIfMessage.clientMethod() {\n\t\t\tIfMessage.foo();\n\t\t}\n\t}\n}\n"));
    }

    public void test_convert_to_dsl_ifMessage_binary_expression_as_condition() {
        myFixture.copyDirectoryToProject("ifMessage","");
        PsiClass selfMessageClass = myFixture.findClass("ifMessage.IfMessage");
        PsiMethod clientMethod = selfMessageClass.findMethodsByName("nestedMethod3", true)[0];
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("IfMessage.nestedMethod3() {\nif(list.length == 2){\tIfMessage.clientMethod();\n\tIfMessage.clientMethod();\n}}\n"));
    }

}
