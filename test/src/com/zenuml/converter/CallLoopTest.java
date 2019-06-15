package com.zenuml.converter;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.zenuml.dsl.PsiToDslConverter;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CallLoopTest extends ZenUmlTestCase {

    private PsiToDslConverter psiToDslConverter;

    public void setUp() throws Exception {
        super.setUp();
        psiToDslConverter = new PsiToDslConverter();
    }

    public void test_convert_to_dsl_callLoop() {
        myFixture.copyDirectoryToProject("callLoop","");
        PsiClass aClass = myFixture.findClass("callLoop.CallLoop");
        PsiMethod method = aClass.findMethodsByName("main", true)[0];
        method.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat("Actual:\n" + dsl, dsl, is("CallLoop.main() {\n\tnew Foo();\n\tnew Bar();\n\tFoo.method1() {\n\t\tBar.method2() {\n\t\t}\n\t}\n}\n"));
    }

}
