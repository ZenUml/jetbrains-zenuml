package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import io.reactivex.Observable;
import org.intellij.sequencer.util.PsiUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PsiToDslConverter extends JavaRecursiveElementVisitor {
    private static final Logger LOG = Logger.getInstance(PsiToDslConverter.class);

    private final MethodStack methodStack = new MethodStack();
    private final ZenDsl zenDsl = new ZenDsl();
    private static final String TYPE_PARAMETER_PATTERN = "<[^<>]*>";
    private static final String ARRAY_PATTERN = "\\[\\d*\\]";

    @Override
    public void visitMethod(PsiMethod method) {
        LOG.debug("Enter: visitMethod: " + method);
        appendThrows(method);
        appendMethod(method);
        processChildren(method);
        LOG.debug("Exit: visitMethod: " + method);
    }

    private void appendThrows(PsiMethod method) {
        zenDsl.append(PsiMethodKt.convertThrows(method));
    }

    private boolean detectReEntry(PsiMethod method) {
        if (methodStack.contains(method)) {
            LOG.debug("Exit (loop detected): visitMethod: " + method);
            zenDsl.comment("Method re-entered");
            return true;
        }
        return false;
    }

    private void appendMethod(PsiMethod method) {
        appendParticipant(method.getContainingClass());
        zenDsl.append(method.getName());
        appendParameters(method.getParameterList().getParameters());
    }

    private void appendParameters(PsiParameter[] parameters) {
        String parameterNames = Stream.of(parameters)
            .map(PsiNamedElement::getName)
            .collect(Collectors.joining(", "));

        zenDsl.appendParams(parameterNames);
    }

    private void appendParticipant(PsiClass containingClass) {
        Optional<PsiClass> headClass = methodStack.peekContainingClass();
        Optional<PsiClass> optionalParticipant = Optional.ofNullable(containingClass);

        optionalParticipant.filter(cls -> !cls.equals(headClass.orElse(null)))
                .ifPresent(cls -> zenDsl.appendParticipant(cls.getName()));
    }

    private void processChildren(PsiMethod method) {
        // TODO: Not covered in test
        if (PsiUtil.isInJarFileSystem(method) || PsiUtil.isInClassFile(method)) {
            zenDsl.closeExpressionAndNewLine();
        }

        if (detectReEntry(method)) return;
        methodStack.push(method);
        super.visitMethod(method);
        methodStack.pop();
    }

    // case 1: String s;
    // case 2: String s = clientMethod();
    public void visitLocalVariable(PsiLocalVariable variable) {
        LOG.debug("Enter: visitLocalVariable: " + variable);
        if(isWithinForStatement(variable)) return;

        if (variable.hasInitializer() && !(variable.getInitializer() instanceof PsiArrayInitializerExpression)) {
            zenDsl.appendAssignment(replaceArray(withoutTypeParameter(variable.getTypeElement().getText())), variable.getName());
        } else {
            zenDsl.comment(replaceArray(variable.getText()));
        }
        super.visitLocalVariable(variable);
        LOG.debug("Exit: visitLocalVariable: " + variable);
    }

    private String withoutTypeParameter(String text) {
        return text.replaceAll(TYPE_PARAMETER_PATTERN, "");
    }

    private String replaceArray(String text) {
        return text.replaceAll(ARRAY_PATTERN, "_array");
    }

    private boolean isWithinForStatement(PsiElement element) {
        if(element == null) return false;
        return element.getParent() instanceof PsiForStatement || isWithinForStatement(element.getParent());
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {
        LOG.debug("Enter: visitMethodCallExpression: " + expression);
        zenDsl.append(expression.getMethodExpression().getText())
                .openParenthesis()
                .append(getArgs(expression.getArgumentList()))
                .closeParenthesis();
        super.visitMethodCallExpression(expression);
    }

    @Override
    public void visitNewExpression(PsiNewExpression expression) {
        LOG.debug("Enter: visitNewExpression: " + expression);
        zenDsl
            .append("new ")
            .append(PsiNewExpressionKt.getClassName(expression))
            .openParenthesis()
            .append(getArgs(expression.getArgumentList()))
            .closeParenthesis();
        super.visitNewExpression(expression);
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        // An expression can be resolved to a method when IDE can find the method in the provided classpath.
        // In our test, if we use System.out.println(), IDE cannot resolve it, because JDK is not in the
        // classpath. If for any reason, in production, it cannot be resolved, we should append it as text.
        PsiMethod method = callExpression.resolveMethod();
        if (method != null) {
            LOG.debug("Method resolved from expression:" + method);
            // If we delegate it to visit method, we lose the parameters.
            // If the expression is a chain (e.g. A.m1().m2()), only m2 is resolved in the method.
            processChildren(method);
        } else {
            LOG.debug("Method not resolved from expression, appending the expression directly");
            zenDsl.closeExpressionAndNewLine();
        }
    }

    private String getArgs(PsiExpressionList argumentList) {
        if(argumentList == null) return "";

        String[] objects = Arrays.stream(argumentList.getExpressions())
                .map(e -> {
                    if (e instanceof PsiLambdaExpression) {
                        return "lambda";
                    }
                    if (e instanceof PsiNewExpression) {
                        // This implementation will not output the parameters of the constructor.
                        // someMethod(new ArrayList<Long>() {{ add(1) }}) -> someMethod(new ArrayList())
                        // Note: getCanonicalText will return java.utils.ArrayList. Not covered in UT.
                        return "new" + " " + withoutTypeParameter(e.getType().getPresentableText() + "()");
                    }
                    return withoutTypeParameter(e.getText());
                })
                .toArray(String[]::new);
        return String.join(", ", objects );
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
    public void visitForStatement(PsiForStatement statement) {
        zenDsl.append("for")
                .openParenthesis()
                .append(statement.getCondition().getText())
                .closeParenthesis();
        super.visitForStatement(statement);
    }

    @Override
    public void visitForeachStatement(PsiForeachStatement statement) {
        zenDsl.append(PsiForeachStatementKt.toDsl(statement));
        super.visitForeachStatement(statement);
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        LOG.debug("Enter: visitIfStatement: " + statement);
        zenDsl.append("if")
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

    @Override
    public void visitTryStatement(PsiTryStatement statement) {
        zenDsl.append("try");
        statement.getTryBlock().accept(this);
        Arrays.stream(statement.getCatchSections()).forEach(s -> s.accept(this));
        if (statement.getFinallyBlock() != null) {
            zenDsl.append("finally");
            statement.getFinallyBlock().accept(this);
        }
    }

    @Override
    public void visitCatchSection(PsiCatchSection section) {
        String parameter = section.getParameter().getType().getPresentableText();
        zenDsl.append(String.format("catch(%s)", parameter.replace('|', ','))); //Parser doesn't support the pipe(|) character
        super.visitCatchSection(section);
    }

    @Override
    public void visitThrowStatement(PsiThrowStatement statement) {
        zenDsl.append(String.format("throw(%s)", withoutTypeParameter(statement.getException().getText()))).closeExpressionAndNewLine();
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
