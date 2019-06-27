package com.zenuml.dsl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;

class MethodStack {
    private static final Logger LOG = Logger.getInstance(MethodStack.class);

    private final Stack<PsiMethod> callStack = new Stack<>();

    @NotNull
    Optional<PsiClass> peekContainingClass() {
        return peekMethod().map(PsiMember::getContainingClass);
    }

    @NotNull
    private Optional<PsiMethod> peekMethod() {
        return callStack.stream().findFirst();
    }

    void push(PsiMethod method) {
        callStack.push(method);
    }

    void pop() {
        callStack.pop();
    }

    boolean contains(PsiMethod method) {
        boolean contains = callStack.contains(method);
        if (contains) {
            String callLoop = Stream.concat(callStack.stream(), Stream.of(method))
                    .map(PsiMethod::getName)
                    .reduce((m1, m2) -> format("%s -> %s", m1, m2))
                    .orElse("");
            LOG.info(format("Call loop detected: %s, stopped", callLoop));
        }
        return contains;
    }
}
