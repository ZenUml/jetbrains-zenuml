package com.zenuml.dsl;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class PsiToDslConverter extends JavaRecursiveElementVisitor {
    private String dsl = "";
    private int level = 0;

    @Override
    public void visitMethod(PsiMethod method) {
        String indent = getIndent(level);
        dsl += indent + method.getContainingClass().getName() + "." + method.getName() + "()";
        // getBody return null if the method belongs to a compiled class
        if (method.getBody() != null && !method.getBody().isEmpty()) {
            level++;
            dsl += " {\n";
            super.visitMethod(method);
            System.out.println(method.getNameIdentifier());
            dsl += indent + "}\n";
        } else {
            dsl += ";\n";
        }
    }

    private static String getIndent(int number) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            builder.append('\t');
        }
        return builder.toString();
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
