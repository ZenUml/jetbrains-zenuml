package com.zenuml.dsl;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;

import java.util.Arrays;
import java.util.HashSet;

public class PsiToDslConverter extends JavaRecursiveElementVisitor {
    private String dsl = "";
    private HashSet<PsiMethod> visitedMethods = new HashSet<>();

    @Override
    public void visitMethod(PsiMethod method) {
        if(visitedMethods.contains(method)) return;

        visitedMethods.add(method);

        dsl += method.getContainingClass().getName() + "." + method.getName() + "()";
        // getBody return null if the method belongs to a compiled class
        if (method.getBody() != null && !method.getBody().isEmpty()) {
            dsl += "{";
            super.visitMethod(method);
            dsl += "}";
        } else {
            dsl += ";";
        }
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        PsiMethod method = expression.resolveMethod();
        if (method != null) {
            visitMethod(method);
            super.visitMethodCallExpression(expression);
        }
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        dsl += "if(condition)";
        System.out.println("Enter: visitIfStatement:" + statement);
        Arrays.stream(statement.getChildren())
                .filter(c -> c.getClass() != PsiWhiteSpaceImpl.class)
                .forEach(c -> System.out.println(c.getClass() + ":\n" + c.getText()));
        super.visitIfStatement(statement);
        System.out.println("Exit: visitIfStatement:" + statement);
    }

    public void visitBlockStatement(PsiBlockStatement statement) {
        dsl += "{";
        super.visitBlockStatement(statement);
        dsl += "}";
    }

    public String getDsl() {
        return dsl;
    }
}
