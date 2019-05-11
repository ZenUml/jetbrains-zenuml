/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.intellij.plugins.markdown.highlighting;

import com.intellij.ide.highlighter.JavaFileHighlighter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.intellij.plugins.markdown.lang.MarkdownElementTypes;
import org.intellij.plugins.markdown.lang.MarkdownTokenTypeSets;
import org.intellij.plugins.markdown.lang.MarkdownTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ZenUmlSyntaxHighlighter extends JavaFileHighlighter {

  // TODO: add ZenUML keywords and highlighting attributes here
}
