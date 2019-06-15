package com.zenuml.converter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.zenuml.dsl.PsiToDslConverter;
import com.zenuml.dsl.SequenceDiagram;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DifferentClassesZenUmlTest extends ZenUmlTestCase {

    private PsiToDslConverter psiToDslConverter;

    public void setUp() throws Exception {
        super.setUp();
        psiToDslConverter = new PsiToDslConverter();

    }

    public void test_convert_to_dsl_node_differentClass() {
        myFixture.copyDirectoryToProject("differentClass","");
        PsiClass firstClass = myFixture.findClass("differentClass.FirstClass");
        PsiMethod clientMethod = firstClass.findMethodsByName("clientMethod", true)[0];

        clientMethod.accept(psiToDslConverter);


        assertThat(psiToDslConverter.getDsl(), is("FirstClass.clientMethod() {\n\tdifferentClass.FirstClass.SecondClass secondClass = new SecondClass();\n\tSecondClass.method1();\n}\n"));
    }

    public void test_convert_to_dsl_node_differentClass_multiple_calls() {
        myFixture.copyDirectoryToProject("differentClass","");
        PsiClass firstClass = myFixture.findClass("differentClass.FirstClass");
        PsiMethod clientMethod = firstClass.findMethodsByName("clientMethod_multiple_calls", true)[0];

        clientMethod.accept(psiToDslConverter);

        assertThat(psiToDslConverter.getDsl(), is("FirstClass.clientMethod_multiple_calls() {\n\tdifferentClass.FirstClass.SecondClass secondClass = new SecondClass();\n\tSecondClass.method1();\n\tSecondClass.method1();\n}\n"));
    }
}
