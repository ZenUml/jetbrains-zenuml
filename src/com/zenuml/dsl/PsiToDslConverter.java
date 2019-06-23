package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import io.reactivex.Observable;
import org.intellij.sequencer.util.PsiUtil;

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
            zenDsl.comment("Method re-entered");
            return;
        }

        String methodCall = getMethodCall(method);

        zenDsl.append(methodCall)
            .openParenthesis()
            .closeParenthesis();
        processChildren(method);

        LOG.debug("Exit: visitMethod: " + method);
    }

    private void processChildren(PsiMethod method) {
        // TODO: Not covered in test
        if (PsiUtil.isInJarFileSystem(method) || PsiUtil.isInClassFile(method)) {
            zenDsl.closeExpressionAndNewLine();
        }

        if (methodStack.contains(method)) {
            LOG.debug("Exit (loop detected): visitMethod: " + method);
            zenDsl.comment("Method re-entered");
            return;
        }
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

        // An expression can be resolved to a method when IDE can find the method in the provided classpath.
        // In our test, if we use System.out.println(), IDE cannot resolve it, because JDK is not in the
        // classpath. If for any reason, in production, it cannot be resolved, we should append it as text.
        PsiMethod method = expression.resolveMethod();
        if (method != null) {
            LOG.debug("Method resolved from expression:" + method);
            // If we delegate it to visit method, we lose the parameters.
            zenDsl.append(getMethodCall(method))
                    .openParenthesis()
                    .append(getArgs(expression.getArgumentList()))
                    .closeParenthesis();
            processChildren(method);
        } else {
            LOG.debug("Method not resolved from expression, appending the expression directly");
            zenDsl.append(expression.getMethodExpression().getText())
                    .openParenthesis()
                    .append(getArgs(expression.getArgumentList()))
                    .closeParenthesis()
                    .closeExpressionAndNewLine();
        }
    }

    private String getArgs(PsiExpressionList argumentList) {
        return Observable.fromArray(argumentList.getExpressions())
                .map( e -> e instanceof PsiLambdaExpression ? "λ" : e.getText())
                .reduce("", ((s1, s2) -> s1 + (s1.length() > 0 ? ", " : "") + s2)).blockingGet();
    }

    @Override
    public void visitWhileStatement(PsiWhileStatement statement) {
        LOG.debug("Enter: visitWhileStatement: " + statement);
        zenDsl.append("while")
                .openParenthesis()
                .append(statement.getCondition().getText())
                .closeParenthesis();

        processBody(statement);
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        LOG.debug("Enter: visitIfStatement: " + statement);
        zenDsl.ensureIndent()
                .append("if")
                .openParenthesis()
                .append(statement.getCondition().getText())
                .closeParenthesis();

        processBody(statement);
    }

    @Override
    public void visitKeyword(PsiKeyword keyword) {
        if ("else".equals(keyword.getText())) {
            zenDsl.append(keyword.getText()).append(" ");
        }
        super.visitKeyword(keyword);
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
        LOG.debug("Enter: visitCodeBlock: " + block.getText());
        if (block.getParent() instanceof PsiLambdaExpression) {
//            zenDsl.closeExpressionAndNewLine();
            return;
        }
        zenDsl.startBlock();
        super.visitCodeBlock(block);
        zenDsl.closeBlock();
    }

    // TODO: this method trigger a class inspection warning.
    @Override
    public void visitReturnStatement(PsiReturnStatement statement) {
        LOG.debug("Enter: visitCodeBlock: " + statement);
        zenDsl.comment(statement.getText());
    }


    public String getDsl() {
        return zenDsl.getDsl();
    }

    private boolean hasFollowingBraces(PsiElement[] children) {
        return Arrays.stream(children).anyMatch(c -> PsiBlockStatement.class.isAssignableFrom(c.getClass()));
    }

    private boolean isRparenth(PsiElement child) {
        return isParenth(child, "RPARENTH");
    }

    private boolean isParenth(PsiElement child, String parenth) {
        return child instanceof PsiJavaToken && ((PsiJavaToken) child).getTokenType().toString().equals(parenth);
    }

}
