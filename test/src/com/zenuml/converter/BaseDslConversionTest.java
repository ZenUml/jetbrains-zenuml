package com.zenuml.converter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.zenuml.dsl.PsiToDslConverter;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public abstract class BaseDslConversionTest extends ZenUmlTestCase {

    private PsiToDslConverter psiToDslConverter;

    public void setUp() throws Exception {
        super.setUp();
        psiToDslConverter = new PsiToDslConverter();
    }

    protected void testDslConversion(String directory, String className, String methodName, String expectedDsl) {
        myFixture.copyDirectoryToProject(directory, "");
        PsiClass selfMessageClass = myFixture.findClass(String.format("%s.%s", directory, className));
        PsiMethod method = selfMessageClass.findMethodsByName(methodName, true)[0];
        method.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat("Actual:\n" + dsl, dsl, is(expectedDsl));
    }
}
