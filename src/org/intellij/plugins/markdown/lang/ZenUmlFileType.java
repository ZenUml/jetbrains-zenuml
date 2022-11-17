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
package org.intellij.plugins.markdown.lang;

import com.intellij.ide.highlighter.custom.SyntaxTable;
import com.intellij.lang.Commenter;
import com.intellij.openapi.fileTypes.impl.AbstractFileType;
import icons.MarkdownIcons;
import icons.SequencePluginIcons;
import org.intellij.plugins.markdown.MarkdownBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;

public class ZenUmlFileType extends AbstractFileType implements Commenter {
  private static final Collection<String> ZENUML_LANGUAGE_KEYWORDS =
          Arrays.asList("title",
          "if", "else", "nil",
          "while", "for", "foreach", "forEach", "loop", "in",
          "return",
          "new",
          "par", "opt", "try", "catch", "finally",
          "group", "as",
          "true", "false");
  private static final Collection<String> ZENUML_LANGUAGE_ANNOTATORS =
          Arrays.asList("@Starter", "@starter",
                  "@Return", "@Reply", "@reply", "@return");
  private static final Collection<String> ZENUML_LANGUAGE_OPERATORS =
          Arrays.asList(".", "->", ":",  // message structure
                  ">", "<",  "==", "!=", ">=", "<=", // comparing
                  "&&", "||", "!", // logic
                  "<<", ">>", // stereotype
                  "#", // hex
                  "+", "-", "*", "/", "%", "^", // math
                  "@", // annotation
                  ";", "," // separators
                  );

  public static final ZenUmlFileType INSTANCE = new ZenUmlFileType();

  private ZenUmlFileType() {
    super(createSyntaxTable());
    setName("ZenUML");
    setDescription("MyLanguage (syntax highlighting only)");
    setIcon(SequencePluginIcons.SEQUENCE_ICON);
  }

  @NotNull
  private static SyntaxTable createSyntaxTable() {
    SyntaxTable syntaxTable = new SyntaxTable();
    syntaxTable.setIgnoreCase(false);
    syntaxTable.setLineComment("//");
    syntaxTable.setStartComment("/**");
    syntaxTable.setEndComment("*/");
    syntaxTable.setHasBraces(true);
    syntaxTable.setHasBrackets(true);
    syntaxTable.setHasParens(true);
    syntaxTable.setHasStringEscapes(true);

    syntaxTable.getKeywords1().addAll(ZENUML_LANGUAGE_KEYWORDS);
    syntaxTable.getKeywords2().addAll(ZENUML_LANGUAGE_ANNOTATORS);
    syntaxTable.getKeywords3().addAll(ZENUML_LANGUAGE_OPERATORS);
    return syntaxTable;
  }

  @Override
  public Commenter getCommenter() {
    return this;
  }

  // Commenter
  @Override
  public @Nullable
  String getLineCommentPrefix() {
    return getSyntaxTable().getLineComment();
  }

  @Override
  public @Nullable
  String getBlockCommentPrefix() {
    return getSyntaxTable().getStartComment();
  }

  @Override
  public @Nullable
  String getBlockCommentSuffix() {
    return getSyntaxTable().getEndComment();
  }

  @Override
  public @Nullable
  String getCommentedBlockCommentPrefix() {
    return null;
  }

  @Override
  public @Nullable
  String getCommentedBlockCommentSuffix() {
    return null;
  }
  @NotNull
  @Override
  public String getName() {
    return "ZenUML";
  }

  @NotNull
  @Override
  public String getDescription() {
    return MarkdownBundle.message("markdown.file.type.description");
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "zen";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return MarkdownIcons.MarkdownPlugin;
  }
}
