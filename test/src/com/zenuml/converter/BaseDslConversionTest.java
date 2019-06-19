package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public abstract class BaseDslConversionTest extends ZenUmlTestCase {

    void testDslConversion(String methodName, String expectedDsl) {
        PsiMethod method = getPsiMethod(methodName);
        method.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        String failureMessage = String.format("Actual DSL:\n%s", dsl);
        assertThat(failureMessage, dsl, is(expectedDsl));
    }
}
