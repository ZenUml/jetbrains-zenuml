package com.zenuml.converter;

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.zenuml.dsl.FunctionNode;
import com.zenuml.testFramework.fixture.ZenUmlTestCase;

import java.util.Optional;

public class FunctionNodeBuilderTest extends ZenUmlTestCase {

    public void test_build_from_method(){
        myFixture.copyDirectoryToProject("selfMessage","");
        PsiClass selfMessageClass = myFixture.findClass("selfMessage.SelfMessage");
        PsiMethod clientMethod = selfMessageClass.findMethodsByName("clientMethod", true)[0];
        String methodExpression = clientMethod.getName() + "()";
        PsiClass containingClass = clientMethod.getContainingClass();
        Optional<FunctionNode> functionNode = Optional
                .ofNullable(containingClass)
                .map(NavigationItem::getName)
                .map(className -> new FunctionNode(className, methodExpression, null));
        StringBuffer sb = new StringBuffer();
        functionNode.ifPresent(n -> n.toDsl(sb));
        System.out.println(sb.toString());
    }
}
