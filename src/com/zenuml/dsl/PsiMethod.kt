package com.zenuml.dsl

import com.intellij.psi.PsiMethod


fun PsiMethod.convertThrows(): ZenDsl = when {
    throwsTypes.isNotEmpty() -> ZenDsl().comment("This method throws " + throwsTypes.map { it.name }.joinToString())
    else -> ZenDsl()
}