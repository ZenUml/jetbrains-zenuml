package com.zenuml.converter;

import com.intellij.psi.*;
import com.zenuml.dsl.PsiToDslConverter;
import com.zenuml.dsl.SequenceDiagram;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SelfMessageZenUmlTest extends ZenUmlTestCase {

    private PsiToDslConverter psiToDslConverter;

    public void setUp() throws Exception {
        super.setUp();
        psiToDslConverter = new PsiToDslConverter();
    }

    public void test_convert_to_dsl_node_selfMessage() {
        myFixture.copyDirectoryToProject("selfMessage","");
        PsiClass selfMessageClass = myFixture.findClass("selfMessage.SelfMessage");
        PsiMethod clientMethod = selfMessageClass.findMethodsByName("clientMethod", true)[0];
        clientMethod.accept(psiToDslConverter);

        String dsl = psiToDslConverter.getDsl();
        assertThat("Actual:\n" + dsl, dsl, is("SelfMessage.clientMethod() {\n\tSelfMessage.internalMethodA() {\n\t\tSelfMessage.internalMethodB() {\n\t\t\tSelfMessage.internalMethodC();\n\t\t}\n\t}\n\tSelfMessage.internalMethodB() {\n\t\tSelfMessage.internalMethodC();\n\t}\n\tSelfMessage.internalMethodC();\n}\n"));
    }

    public void test_convert_to_dsl_node_selfMessage_nest_2_levels() {
        myFixture.copyDirectoryToProject("selfMessage","");
        PsiClass selfMessageClass = myFixture.findClass("selfMessage.SelfMessage");
        PsiMethod clientMethod = selfMessageClass.findMethodsByName("clientMethod2", true)[0];
        clientMethod.accept(psiToDslConverter);
        assertThat(psiToDslConverter.getDsl(), is("SelfMessage.clientMethod2() {\n\tSelfMessage.internalMethodA() {\n\t\tSelfMessage.internalMethodB() {\n\t\t\tSelfMessage.internalMethodC();\n\t\t}\n\t}\n}\n"));
    }
}
