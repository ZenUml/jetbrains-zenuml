package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import io.reactivex.Observable;

import java.util.Arrays;

public class PsiToDslConverter extends JavaRecursiveElementVisitor {
    private static final Logger LOG = Logger.getInstance(PsiToDslConverter.class);

    private final MethodStack methodStack = new MethodStack();
    private final ZenDsl zenDsl = new ZenDsl();

    // TODO: we are not following the implementation of constructor. The behaviour is NOT defined.
    public void visitNewExpression(PsiNewExpression expression) {
        LOG.debug("Enter: visitNewExpression: " + expression);
        zenDsl.append(expression.getText()).closeExpressionAndNewLine();
        super.visitNewExpression(expression);
        LOG.debug("Exit: visitNewExpression: " + expression);
    }

    @Override
    public void visitMethod(PsiMethod method) {
        LOG.debug("Enter: visitMethod: " + method);

        if (methodStack.contains(method)) {
            LOG.debug("Exit (loop detected): visitMethod: " + method);
            zenDsl.changeLine();
            return;
        }

        String methodCall = getMethodCall(method);

        zenDsl.append(methodCall);
        processChildren(method);

        LOG.debug("Exit: visitMethod: " + method);
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

    public void visitParameterList(PsiParameterList list) {
        LOG.debug("Enter: visitParameterList: " + list);
        zenDsl.openParenthesis();
        super.visitParameterList(list);
        zenDsl.closeParenthesis();
        LOG.debug("Exit: visitParameterList: " + list);
    }

    public void visitDeclarationStatement(PsiDeclarationStatement statement) {
        LOG.debug("Enter: visitDeclarationStatement: " + statement);
        super.visitDeclarationStatement(statement);
    }

    public void visitExpressionStatement(PsiExpressionStatement statement) {
        LOG.debug("Enter: visitExpressionStatement: " + statement);
        super.visitExpressionStatement(statement);
    }

    // case 1: String s;
    // case 2: String s = clientMethod();
    public void visitLocalVariable(PsiLocalVariable variable) {
        LOG.debug("Enter: visitLocalVariable: " + variable);
        if (variable.hasInitializer()) {
            zenDsl.appendAssignment(variable.getTypeElement().getText(), variable.getName());
        } else {
            zenDsl.comment(variable.getText());
        }
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
            visitMethod(method);
        }
    }

    @Override
    public void visitWhileStatement(PsiWhileStatement statement) {
        LOG.debug("Enter: visitWhileStatement: " + statement);
        visitCondition(statement);

        zenDsl.append("while")
                .openParenthesis()
                .append(getCondition(statement))
                .closeParenthesis();

        processBody(statement);
    }

    private void visitCondition(PsiStatement statement) {
        getChildrenWithinParenthesis(statement).subscribe(element -> element.accept(this));
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        LOG.debug("Enter: visitIfStatement: " + statement);
        visitCondition(statement);

        zenDsl.ensureIndent()
                .append("if")
                .openParenthesis()
                .append(getCondition(statement))
                .closeParenthesis();

        processBody(statement);
    }

    private void processBody(PsiStatement statement) {
        LOG.debug("Enter: processBody");
        boolean hasBlock = hasFollowingBraces(statement.getChildren());
        // following braces are not there, we should add them here.
        if (!hasBlock) {
            zenDsl.startBlock();
        }
        Observable.fromArray(statement.getChildren())
                .skipWhile(psiElement -> !isRparenth(psiElement))
                .skip(1)
                .subscribe(psiElement -> {
                    LOG.debug("Process body then:" + psiElement.getText());
                    psiElement.accept(this);
                });
        if (!hasBlock) {
            zenDsl.closeBlock();
        }
        LOG.debug("Exit: processBody");
    }

    // A a = B.method() seems triggering declaration
    // a = B.method() is trigger this.
    // Only simple `i = 1` does.
    @Override
    public void visitAssignmentExpression(PsiAssignmentExpression expression) {
        zenDsl.comment(expression.getText());
    }

    @Override
    public void visitCodeBlock(PsiCodeBlock block) {
        LOG.debug("Enter: visitCodeBlock: " + block);
        if (block.getStatements().length == 0) {
            zenDsl.closeExpressionAndNewLine();
            return;
        }
        zenDsl.startBlock();
        super.visitCodeBlock(block);
        zenDsl.closeBlock();
    }

    public String getDsl() {
        return zenDsl.getDsl();
    }

    private boolean hasFollowingBraces(PsiElement[] children) {
        return Arrays.stream(children).anyMatch(c -> PsiBlockStatement.class.isAssignableFrom(c.getClass()));
    }

    private String getCondition(PsiStatement statement) {
        return getChildrenWithinParenthesis(statement)
                .map(PsiElement::getText)
                .reduce((s1, s2) -> s1 + s2).blockingGet();
    }

    private Observable<PsiElement> getChildrenWithinParenthesis(PsiStatement statement) {
        return Observable.fromArray(statement.getChildren())
                .skipWhile(psiElement -> !isLparenth(psiElement))
                .skip(1) // skip `(`
                .takeWhile(psiElement -> !isRparenth(psiElement));
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

}
