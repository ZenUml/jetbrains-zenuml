package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConstructorMessageTest extends ZenUmlTestCase {

    public void test_convert_to_dsl_constructor() {
        PsiMethod clientMethod = getPsiMethod("constructor", "constructor.Constructor", "clientMethod");
        clientMethod.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat(dsl, is("Constructor.clientMethod() {\n\tconstructor.Constructor c = new Constructor();\n}\n"));
    }

}
