package com.zenuml.dsl;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class PsiToDslNodeConverter extends JavaRecursiveElementVisitor {

    private SequenceDiagram sequenceDiagram = new SequenceDiagram();
    private int depth = 0;

    @Override
    public void visitMethod(PsiMethod method) {
        depth += 2;
        String indent = StringUtils.repeat(" ", depth);
        System.out.println(indent + "Enter: visitMethod:" + method);
        super.visitMethod(method);
        System.out.println(indent + "Exit: visitMethod");
        depth -= 2;
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        depth += 2;
        String indent = StringUtils.repeat(" ", depth);

        System.out.println(indent + "Enter: visitMethodCallExpression:" + expression);
        super.visitMethodCallExpression(expression);
        System.out.println(indent + "Exit: visitMethodCallExpression");
        depth -= 2;
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        depth += 2;
        String indent = StringUtils.repeat(" ", depth);
        System.out.println(indent + "Enter: visitIfStatement:" + statement);
        Arrays.stream(statement.getChildren())
                .filter(c -> c.getClass() != PsiWhiteSpaceImpl.class)
                .forEach(c -> System.out.println(indent + c.getClass() + ":\n" + indent + c.getText()));
        super.visitIfStatement(statement);
        System.out.println(indent + "Exit: visitIfStatement:" + statement);
        depth -= 2;
    }

    public SequenceDiagram rootNode() {
        return sequenceDiagram;
    }
}
