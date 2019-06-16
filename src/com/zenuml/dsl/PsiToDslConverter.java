package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;

public class PsiToDslConverter extends JavaRecursiveElementVisitor {
    private static final Logger LOG = Logger.getInstance(PsiToDslConverter.class);

    private String dsl = "";
    private int level = 0;
    private ArrayList<PsiMethod> callStack = new ArrayList<>();

    public void visitNewExpression(PsiNewExpression expression) {
        LOG.debug("Enter: visitNewExpression: " + expression);
        dsl += expression.getText() + ";\n";
        super.visitNewExpression(expression);
        LOG.debug("Exit: visitNewExpression: " + expression);
    }

    @Override
    public void visitMethod(PsiMethod method) {
        visitMethod(method, null);
    }

    private void visitMethod(PsiMethod method, String insertBefore) {
        LOG.debug("Enter: visitMethod: " + method);

        if (callStack.contains(method)) {
            String callLoop = Stream.concat(callStack.stream(), Stream.of(method))
                    .map(PsiMethod::getName)
                    .reduce((m1, m2) -> format("%s -> %s", m1, m2))
                    .get();
            LOG.info(format("Call loop detected: %s, stopped", callLoop));
            return;
        }

        PsiClass containingClass = method.getContainingClass();
        // prefix is : `ClassName.`
        String methodPrefix = new MethodStack(callStack)
                .peekContainingClass()
                .filter(cls -> cls.equals(containingClass))
                .map(cls -> "")
                .orElse(containingClass.getName() + ".");

        String methodName = methodPrefix + method.getName();

        String remainder = "";
        if (insertBefore != null) {
            int index = dsl.lastIndexOf(insertBefore);
            remainder = this.dsl.substring(index);
            this.dsl = this.dsl.substring(0, index);
        }

        this.dsl += methodName;

        callStack.add(method);

        super.visitMethod(method);
        callStack.remove(method);

        if (remainder.length() > 0) {
            this.dsl += getIndent(level - 1) + remainder;
        }

        LOG.debug("Exit: visitMethod: " + method);
    }

    //    public void visitParameter(PsiParameter parameter) {
//        LOG.debug("Enter: visitParameter: " + parameter);
//        super.visitParameter(parameter);
//        LOG.debug("Exit: visitParameter: " + parameter);
//    }

//    public void visitReceiverParameter(PsiReceiverParameter parameter) {
//        LOG.debug("Enter: visitReceiverParameter: " + parameter);
//        super.visitReceiverParameter(parameter);
//        LOG.debug("Exit: visitReceiverParameter: " + parameter);
//    }

    public void visitParameterList(PsiParameterList list) {
        LOG.debug("Enter: visitParameterList: " + list);
        dsl += "(";
        super.visitParameterList(list);
        dsl += ")";
        LOG.debug("Exit: visitParameterList: " + list);
    }

//    @Override
//    public void visitReferenceExpression(PsiReferenceExpression expression) {
//        LOG.debug("Enter: visitReferenceExpression: " + expression);
//        super.visitReferenceExpression(expression);
//        LOG.debug("Exit: visitReferenceExpression: " + expression);
//    }

//    @Override
//    public void visitModifierList(PsiModifierList list) {
//        LOG.debug("Enter: visitModifierList: " + list);
//        super.visitModifierList(list);
//    }

//    @Override
//    public void visitTypeElement(PsiTypeElement type) {
//        LOG.debug("Enter: visitTypeElement: " + type);
//        if (!type.getText().equals("void")) {
//            dsl += type.getText() + " ";
//        }
//        super.visitTypeElement(type);
//        LOG.debug("Exit: visitTypeElement: " + type);
//    }

    public void visitDeclarationStatement(PsiDeclarationStatement statement) {
        LOG.debug("Enter: visitDeclarationStatement: " + statement);
        String indent = getIndent(level);
        dsl += indent;
        super.visitDeclarationStatement(statement);
    }

    public void visitExpressionStatement(PsiExpressionStatement statement) {
        LOG.debug("Enter: visitExpressionStatement: " + statement);
        String indent = getIndent(level);
        dsl += indent;
        super.visitExpressionStatement(statement);
    }

    // variable: String s = clientMethod();
    public void visitLocalVariable(PsiLocalVariable variable) {
        LOG.debug("Enter: visitLocalVariable: " + variable);
        dsl += variable.getType().getCanonicalText();
        dsl += " ";
        dsl += variable.getName();
        dsl += " = ";
        super.visitLocalVariable(variable);
        LOG.debug("Exit: visitLocalVariable: " + variable);
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        LOG.debug("Enter: visitMethodCallExpression: " + expression);

        super.visitMethodCallExpression(expression);

        PsiMethod method = expression.resolveMethod();
        if (method != null) {
            LOG.debug("Method resolved from expression:" + method);
            PsiElement whileChild = getDirectWhileStatementChildFromAncestors(expression);
            String insertBefore = whileChild != null && isInsideCondition(whileChild) ? "while" : null;
            visitMethod(method, insertBefore);
        }
    }

    private PsiElement getDirectWhileStatementChildFromAncestors(PsiElement element) {
        PsiElement current = element;
        PsiElement parent = current.getParent();
        while (parent != null) {
            if(parent instanceof PsiWhileStatement) return current;
            current = parent;
            parent = current.getParent();
        }
        return null;
    }

    private boolean isInsideCondition(PsiElement element) {
        PsiElement nextSibling = element.getNextSibling();
        if(nextSibling == null) return false;
        if(isRparenth(nextSibling)) return true;
        return isInsideCondition(nextSibling);
    }

    @Override
    public void visitWhileStatement(PsiWhileStatement statement) {
        String indent = getIndent(level);
        dsl += newlineIfNecessary() + indent + "while" + getCondition(statement);

        boolean hasBlock = hasBlock(statement.getChildren());
        if (!hasBlock) {
            dsl += " {\n";
            level++;
        }
        super.visitWhileStatement(statement);
        if (!hasBlock) {
            level--;
            dsl += newlineIfNecessary() + indent + "}\n";
        }
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        LOG.debug("Enter: visitIfStatement: " + statement);

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
        boolean hasBlock = hasBlock(statement.getChildren());
        if (!hasBlock) {
            dsl += " {\n";
            level++;
        }
        super.visitIfStatement(statement);
        if (!hasBlock) {
            level--;
            dsl += newlineIfNecessary() + indent + "}\n";
        }
    }

    @Override
    public void visitBlockStatement(PsiBlockStatement statement) {
        LOG.debug("Enter: visitBlockStatement: " + statement);
        super.visitBlockStatement(statement);
    }

    @Override
    public void visitCodeBlock(PsiCodeBlock block) {
        LOG.debug("Enter: visitCodeBlock: " + block);
        if (block.getStatements().length == 0) {
            dsl += ";\n";
            return;
        }
        // getBody return null if the method belongs to a compiled class
        level++;
        dsl += " {\n";
        super.visitCodeBlock(block);

        level--;
        dsl += newlineIfNecessary() + getIndent(level) + "}\n";
    }

    public String getDsl() {
        return dsl;
    }

    private boolean hasBlock(PsiElement[] children) {
        return Arrays.stream(children).anyMatch(c -> PsiBlockStatement.class.isAssignableFrom(c.getClass()));
    }

    private String getCondition(PsiWhileStatement statement) {
        boolean started = false;
        ArrayList<PsiElement> elements = new ArrayList<>();
        for (PsiElement child : statement.getChildren()) {
            if (started) {
                if (isRparenth(child)) {
                    break;
                }
                elements.add(child);
            } else if (isLparenth(child)) {
                started = true;
            }
        }
        return format("(%s)", elements.stream().map(PsiElement::getText).reduce((s1, s2) -> s1 + s2).orElse(""));
    }

    private boolean isLparenth(PsiElement child) {
        return isParenth(child, "LPARENTH");
    }

    private boolean isRparenth(PsiElement child) {
        return isParenth(child, "RPARENTH");
    }

    private boolean isParenth(PsiElement child, String parenth) {
        return child instanceof PsiJavaToken && ((PsiJavaToken) child).getTokenType().toString().equals(parenth);
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
