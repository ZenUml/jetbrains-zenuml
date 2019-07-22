package com.zenuml.dsl

import com.intellij.psi.PsiMethod


fun PsiMethod.convert_throws(): ZenDsl {
    val zenDsl = ZenDsl()
    if (throwsTypes.isNotEmpty()) {
        val comment = "This method throws"
        throwsTypes.map { name }
        val fold = throwsTypes.fold(
            "",
            { acc, exceptionType -> acc + " " + exceptionType.name}
        )
        zenDsl.comment(comment + fold)
    }
    return zenDsl
}