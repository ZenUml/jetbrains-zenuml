package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DifferentClassesZenUmlTest extends ZenUmlTestCase {

    public void test_convert_to_dsl_node_differentClass() {
        PsiMethod clientMethod = getPsiMethod("differentClass", "differentClass.FirstClass", "clientMethod");
        clientMethod.accept(psiToDslConverter);
        assertThat(psiToDslConverter.getDsl(), is("FirstClass.clientMethod() {\n\tdifferentClass.FirstClass.SecondClass secondClass = new SecondClass();\n\tSecondClass.method1();\n}\n"));
    }

    public void test_convert_to_dsl_node_differentClass_multiple_calls() {
        PsiMethod clientMethod = getPsiMethod("differentClass", "differentClass.FirstClass", "clientMethod_multiple_calls");
        clientMethod.accept(psiToDslConverter);
        assertThat(psiToDslConverter.getDsl(), is("FirstClass.clientMethod_multiple_calls() {\n\tdifferentClass.FirstClass.SecondClass secondClass = new SecondClass();\n\tSecondClass.method1();\n\tSecondClass.method1();\n}\n"));
    }
}
