package com.zenuml.converter;

import com.intellij.psi.*;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;
import org.jetbrains.annotations.NotNull;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SelfMessageZenUmlTest extends ZenUmlTestCase {

    public void test_convert_to_dsl_node_selfMessage() {
        PsiMethod clientMethod = getPsiMethod("clientMethod");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat("Actual:\n" + dsl, dsl, is("SelfMessage.clientMethod() {\n\tinternalMethodA() {\n\t\tinternalMethodB() {\n\t\t\tinternalMethodC();\n\t\t}\n\t}\n\tinternalMethodB() {\n\t\tinternalMethodC();\n\t}\n\tinternalMethodC();\n}\n"));
    }

    public void test_convert_to_dsl_node_selfMessage_nest_2_levels() {
        PsiMethod clientMethod = getPsiMethod("clientMethod2");
        clientMethod.accept(psiToDslConverter);
        assertThat(psiToDslConverter.getDsl(), is("SelfMessage.clientMethod2() {\n\tint i = \tinternalMethodA() {\n\t\tinternalMethodB() {\n\t\t\tinternalMethodC();\n\t\t}\n\t}\n}\n"));
    }

    @Override
    @NotNull
    protected String getClassName() {
        return "selfMessage.SelfMessage";
    }

}
