package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;

import java.util.*;

import static java.lang.String.format;

public class PsiToDslConverter extends JavaRecursiveElementVisitor {
    private static final Logger LOG = Logger.getInstance(PsiToDslConverter.class);

    private final MethodStack methodStack = new MethodStack();
    private final ZenDsl zenDsl = new ZenDsl();

    public void visitNewExpression(PsiNewExpression expression) {
        LOG.debug("Enter: visitNewExpression: " + expression);
        zenDsl.append(expression.getText()).append(";\n");
        super.visitNewExpression(expression);
        LOG.debug("Exit: visitNewExpression: " + expression);
    }

    @Override
    public void visitMethod(PsiMethod method) {
        visitMethod(method, null);
    }

    private void visitMethod(PsiMethod method, String insertBefore) {
        LOG.debug("Enter: visitMethod: " + method);

        if (methodStack.contains(method)) {
            LOG.debug("Exit (loop detected): visitMethod: " + method);
            return;
        }

        String methodCall = getMethodCall(method);

        process(method, insertBefore, methodCall);

        LOG.debug("Exit: visitMethod: " + method);
    }

    private void process(PsiMethod method, String insertBefore, String methodCall) {
        if (insertBefore == null) {
            zenDsl.addMethodCall(methodCall);
            processChildren(method);
        } else {
            String remainder = getRemainder(zenDsl, insertBefore);
            zenDsl.addMethodCall(methodCall);
            processChildren(method);

            zenDsl.addRemainder(remainder, this.zenDsl.getLevel());
        }
    }

    private String getRemainder(ZenDsl zenDsl, String insertBefore) {
        int index = zenDsl.getDsl().lastIndexOf(insertBefore);

        String remainder = zenDsl.getDsl().substring(index);
        zenDsl.cut(0, index);
        return remainder;
    }

    private void processChildren(PsiMethod method) {
        methodStack.push(method);
        super.visitMethod(method);
        methodStack.pop();
    }

    private String getMethodCall(PsiMethod method) {
        PsiClass containingClass = method.getContainingClass();
        // prefix is : `ClassName.`
        String methodPrefix = methodStack
                .peekContainingClass()
                .filter(cls -> cls.equals(containingClass))
                .map(cls -> "")
                .orElse(containingClass.getName() + ".");

        return methodPrefix + method.getName();
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
        zenDsl.append("(");
        super.visitParameterList(list);
        zenDsl.append(")");
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
//            dsl.append(type.getText() + " ";
//        }
//        super.visitTypeElement(type);
//        LOG.debug("Exit: visitTypeElement: " + type);
//    }

    public void visitDeclarationStatement(PsiDeclarationStatement statement) {
        LOG.debug("Enter: visitDeclarationStatement: " + statement);
        zenDsl.appendIndent(zenDsl.getLevel());
        super.visitDeclarationStatement(statement);
    }

    public void visitExpressionStatement(PsiExpressionStatement statement) {
        LOG.debug("Enter: visitExpressionStatement: " + statement);
        zenDsl.appendIndent(zenDsl.getLevel());
        super.visitExpressionStatement(statement);
    }

    // variable: String s = clientMethod();
    public void visitLocalVariable(PsiLocalVariable variable) {
        LOG.debug("Enter: visitLocalVariable: " + variable);
        zenDsl.append(variable.getType().getCanonicalText());
        zenDsl.append(" ");
        zenDsl.append(variable.getName());
        zenDsl.append(" = ");
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
        String indent = zenDsl.getIndent(zenDsl.getLevel());
        zenDsl.append(newlineIfNecessary() + indent + "while" + getCondition(statement));

        boolean hasBlock = hasBlock(statement.getChildren());
        if (!hasBlock) {
            zenDsl.append(" {\n");
            zenDsl.levelIncrease();
        }
        super.visitWhileStatement(statement);
        if (!hasBlock) {
            zenDsl.levelDecrease();
            zenDsl.append(newlineIfNecessary() + indent + "}\n");
        }
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        LOG.debug("Enter: visitIfStatement: " + statement);

        String indent = zenDsl.getIndent(zenDsl.getLevel());
        zenDsl.append(newlineIfNecessary() + indent + "if(");
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
                .findFirst().ifPresent(e -> zenDsl.append(e.getText() + ")"));
        boolean hasBlock = hasBlock(statement.getChildren());
        if (!hasBlock) {
            zenDsl.append(" {\n");
            zenDsl.levelIncrease();
        }
        super.visitIfStatement(statement);
        if (!hasBlock) {
            zenDsl.levelDecrease();
            zenDsl.append(newlineIfNecessary() + indent + "}\n");
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
            zenDsl.append(";\n");
            return;
        }
        // getBody return null if the method belongs to a compiled class
        zenDsl.levelIncrease();
        zenDsl.append(" {\n");
        super.visitCodeBlock(block);

        zenDsl.levelDecrease();
        zenDsl.append(newlineIfNecessary() + zenDsl.getIndent(zenDsl.getLevel()) + "}\n");
    }

    public String getDsl() {
        return zenDsl.getDsl().toString();
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
        return zenDsl.getDsl().toString().isEmpty() || zenDsl.getDsl().toString().endsWith("\n") ? "" : "\n";
    }

}
