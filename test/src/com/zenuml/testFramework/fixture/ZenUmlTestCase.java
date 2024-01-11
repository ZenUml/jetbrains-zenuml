package com.zenuml.testFramework.fixture;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScopeBuilderImpl;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.zenuml.dsl.PsiToDslConverter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public abstract class ZenUmlTestCase extends LightJavaCodeInsightFixtureTestCase {

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

//        Project project = getProject();
//        myFixture.copyDirectoryToProject(folder, project.getName());
//        GlobalSearchScope globalSearchScope = new ProjectScopeBuilderImpl(project).buildProjectScope();
//        JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
//        PsiShortNamesCache shortNamesCache = PsiShortNamesCache.getInstance(project);
//        String[] parts = className.split("\\.");
//        String simpleClassName = parts.length > 0 ? parts[parts.length - 1] : null;
//        PsiClass[] classesByName = shortNamesCache.getClassesByName(simpleClassName, globalSearchScope);
//        PsiClass selfMessageClass = classesByName[0];
//        PsiMethod[] methodsByName = shortNamesCache.getMethodsByName(methodName, globalSearchScope);
    }

    protected PsiMethod getPsiMethod(String clientMethod) {
        return getPsiMethod(getFolder(), getClassName(), clientMethod);
    }

    @NotNull
    protected abstract String getClassName();

    @NotNull
    private String getFolder() {
        String className = getClassName();
        String packageName = className.substring(0, className.lastIndexOf('.'));
        return packageName.replace('.', '/');
    }
}
