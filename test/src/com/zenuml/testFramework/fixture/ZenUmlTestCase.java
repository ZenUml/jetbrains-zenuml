package com.zenuml.testFramework.fixture;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.zenuml.dsl.PsiToDslConverter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public abstract class ZenUmlTestCase extends LightCodeInsightFixtureTestCase {

    protected PsiToDslConverter psiToDslConverter;

    public void setUp() throws Exception {
        super.setUp();
        psiToDslConverter = new PsiToDslConverter();
    }

    @Override
    protected String getTestDataPath() {
        return Paths.get("test/data/").toAbsolutePath().toString();
    }

    private PsiMethod getPsiMethod(String folder, String className, String methodName) {
        myFixture.copyDirectoryToProject(folder, "");
        PsiClass selfMessageClass = myFixture.findClass(className);
        return selfMessageClass.findMethodsByName(methodName, true)[0];
    }

    protected PsiMethod getPsiMethod(String clientMethod) {
        return getPsiMethod(getFolder(), getClassName(), clientMethod);
    }

    @NotNull
    protected abstract String getClassName();

    @NotNull
    private String getFolder() {
        String className = getClassName();
        return className.substring(0, className.lastIndexOf('.'));
    }
}
