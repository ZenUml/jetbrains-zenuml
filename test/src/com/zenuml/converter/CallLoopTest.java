package com.zenuml.converter;

import com.intellij.psi.PsiMethod;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;
import org.jetbrains.annotations.NotNull;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CallLoopTest extends ZenUmlTestCase {

    public void test_convert_to_dsl_callLoop() {
        PsiMethod method = getPsiMethod("main");
        method.accept(psiToDslConverter);
        String dsl = psiToDslConverter.getDsl();
        assertThat("Actual:\n" + dsl, dsl, is("CallLoop.main() {\n\tcallLoop.Foo foo = new Foo();\n\tcallLoop.Bar bar = new Bar();\n\tFoo.method1() {\n\t\tBar.method2() {\n\t\t\t\n\t\t}\n\t}\n}\n"));
    }

    @NotNull
    @Override
    protected String getClassName() {
        return "callLoop.CallLoop";
    }
}
