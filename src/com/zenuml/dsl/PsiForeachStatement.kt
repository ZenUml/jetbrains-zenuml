package com.zenuml.dsl

import com.intellij.psi.PsiForeachStatement


fun PsiForeachStatement.toDsl(): ZenDsl = ZenDsl().append("forEach")
    .openParenthesis()
    .append(this.iteratedValue!!.text)
    .closeParenthesis()