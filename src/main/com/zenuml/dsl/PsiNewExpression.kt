package com.zenuml.dsl

import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiNewExpression
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.impl.source.PsiImmediateClassType


fun PsiNewExpression.getClassName(): String? = when (this.type) {
    is PsiClassReferenceType, is PsiImmediateClassType -> this.classOrAnonymousClassReference!!.referenceName
    is PsiArrayType -> (this.type as PsiArrayType).componentType.canonicalText + "_array"
    else -> this.text
}