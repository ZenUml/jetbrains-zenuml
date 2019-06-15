package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.dsl.PsiToDslConverter;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimpleMessageZenUmlTest extends ZenUmlTestCase {

    private PsiToDslConverter psiToDslConverter;

    public void setUp() throws Exception {
        super.setUp();
        psiToDslConverter = new PsiToDslConverter();
    }

    public void test_convert_to_dsl_simpleMessage() {
        PsiMethod clientMethod = getPsiMethod("simpleMessage", "simpleMessage.SimpleMessage", "clientMethod");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("SimpleMessage.clientMethod();\n"));
    }

    public void test_convert_to_dsl_nestedMessage() {
        PsiMethod clientMethod = getPsiMethod("simpleMessage", "simpleMessage.SimpleMessage", "nestedMethod");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("SimpleMessage.nestedMethod() {\n\tSimpleMessage.clientMethod();\n}\n"));
    }

    public void test_convert_to_dsl_nestedMessage_with_assignment() {
        PsiMethod clientMethod = getPsiMethod("simpleMessage", "simpleMessage.SimpleMessage", "nestedMethod_with_assignment");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("SimpleMessage.nestedMethod_with_assignment() {\n\tString s = SimpleMessage.clientMethod();\n}\n"));
    }

}
