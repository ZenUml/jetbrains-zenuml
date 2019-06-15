package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;
import org.jetbrains.annotations.NotNull;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SimpleMessageZenUmlTest extends ZenUmlTestCase {

    public void test_convert_to_dsl_simpleMessage() {
        PsiMethod clientMethod = getPsiMethod("clientMethod");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("SimpleMessage.clientMethod();\n"));
    }

    public void test_convert_to_dsl_nestedMessage() {
        PsiMethod clientMethod = getPsiMethod("nestedMethod");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("SimpleMessage.nestedMethod() {\n\tclientMethod();\n}\n"));
    }

    public void test_convert_to_dsl_nestedMessage_with_assignment() {
        PsiMethod clientMethod = getPsiMethod("nestedMethod_with_assignment");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("SimpleMessage.nestedMethod_with_assignment() {\n\tString s = clientMethod();\n}\n"));
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "simpleMessage.SimpleMessage";
    }
}
