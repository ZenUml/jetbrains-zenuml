package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Stream;

public class PsiToDslConverter extends JavaRecursiveElementVisitor {
    private static final Logger LOG = Logger.getInstance(PsiToDslConverter.class);


    private String dsl = "";
    private int level = 0;
    private ArrayList<PsiMethod> callStack = new ArrayList<>();

    public void visitNewExpression(PsiNewExpression expression) {
        LOG.debug("Enter: visitNewExpression: " + expression);
        String indent = getIndent(level);
        dsl += indent + expression.getText() + ";\n";
        super.visitNewExpression(expression);
        LOG.debug("Exit: visitNewExpression: " + expression);
    }

    @Override
    public void visitMethod(PsiMethod method) {
        LOG.debug("Enter: visitMethod: " + method);

        if(callStack.contains(method)) {
            String callLoop = Stream.concat(callStack.stream(), Stream.of(method))
                    .map(PsiMethod::getName)
                    .reduce((m1, m2) -> String.format("%s -> %s", m1, m2))
                    .get();
            LOG.info(String.format("Call loop detected: %s, stopped", callLoop));
            return;
        }

        String indent = getIndent(level);
        dsl += newlineIfNecessary() + indent + method.getContainingClass().getName() + "." + method.getName() + "()";
        // getBody return null if the method belongs to a compiled class
        if (method.getBody() != null && !method.getBody().isEmpty()) {
            level++;
            dsl += " {\n";
            callStack.add(method);

            super.visitMethod(method);

            level--;
            dsl += newlineIfNecessary() + indent + "}\n";
            callStack.remove(method);
        } else {
            dsl += ";\n";
        }
        LOG.debug("Exit: visitMethod: " + method);
    }

    public void visitParameter(PsiParameter parameter) {
        LOG.debug("Enter: visitParameter: " + parameter);
        super.visitParameter(parameter);
        LOG.debug("Exit: visitParameter: " + parameter);
    }

//    public void visitReceiverParameter(PsiReceiverParameter parameter) {
//        LOG.debug("Enter: visitReceiverParameter: " + parameter);
//        super.visitReceiverParameter(parameter);
//        LOG.debug("Exit: visitReceiverParameter: " + parameter);
//    }

    public void visitParameterList(PsiParameterList list) {
        LOG.debug("Enter: visitParameterList: " + list);
        super.visitParameterList(list);
        LOG.debug("Exit: visitParameterList: " + list);
    }

    @Override
    public void visitReferenceExpression(PsiReferenceExpression expression) {
        LOG.debug("Enter: visitReferenceExpression: " + expression);
        super.visitReferenceExpression(expression);
        LOG.debug("Exit: visitReferenceExpression: " + expression);
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        LOG.debug("Enter: visitMethodCallExpression: " + expression);
        super.visitMethodCallExpression(expression);

        PsiMethod method = expression.resolveMethod();
        if (method != null) {
            LOG.debug("Method resolved from expression:" + method);
            visitMethod(method);
        }
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        LOG.debug("Enter: visitMethodCallExpression: " + statement);

        String indent = getIndent(level);
        dsl += newlineIfNecessary() + indent + "if(";
        List<Class<? extends PsiExpression>> allowedConditionExpressions = Arrays.asList(PsiLiteralExpression.class,
                PsiBinaryExpression.class,
                PsiReferenceExpression.class);
        System.out.println("Enter: visitIfStatement:" + statement);
        Arrays.stream(statement.getChildren())
                .filter(e -> {
                    System.out.println(e.toString());
                    return true;
                })
                .filter(e -> allowedConditionExpressions.stream().anyMatch(clz -> clz.isInstance(e)))
                .findFirst().ifPresent(e -> dsl += e.getText() + ")");
        boolean hasBlock = Arrays.stream(statement.getChildren()).anyMatch(c -> PsiBlockStatement.class.isAssignableFrom(c.getClass()));
        if(!hasBlock) {
            dsl += " {\n";
            level++;
        }
        super.visitIfStatement(statement);
        if(!hasBlock) {
            level--;
            dsl += newlineIfNecessary() + indent + "}\n";
        }
    }

    public void visitBlockStatement(PsiBlockStatement statement) {
        LOG.debug("Enter: visitBlockStatement: " + statement);

        dsl += " {\n";
        level++;
        super.visitBlockStatement(statement);
        level--;
        dsl += newlineIfNecessary() + getIndent(level) + "}";
    }

    public String getDsl() {
        return dsl;
    }

    private String newlineIfNecessary() {
        return dsl.isEmpty() || dsl.endsWith("\n") ? "" : "\n";
    }

    private static String getIndent(int number) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            builder.append('\t');
        }
        return builder.toString();
    }
}
