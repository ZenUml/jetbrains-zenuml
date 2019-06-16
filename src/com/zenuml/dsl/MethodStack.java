package com.zenuml.dsl;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

class MethodStack {
    private final List<PsiMethod> callStack;

    MethodStack(List<PsiMethod> callStack) {
        this.callStack = callStack;
    }

    @NotNull
    Optional<PsiClass> peekContainingClass() {
        return peekMethod().map(PsiMember::getContainingClass);
    }

    @NotNull
    private Optional<PsiMethod> peekMethod() {
        return callStack.stream().findFirst();
    }

}
