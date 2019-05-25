package org.intellij.plugins.markdown.lang.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.intellij.plugins.markdown.lang.psi.MarkdownElementVisitor;
import org.intellij.plugins.markdown.lang.psi.MarkdownPsiElement;
import org.jetbrains.annotations.NotNull;

public class ZenUmlLinkDestinationImpl extends ASTWrapperPsiElement implements MarkdownPsiElement {
  public ZenUmlLinkDestinationImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MarkdownElementVisitor) {
      ((MarkdownElementVisitor)visitor).visitLinkDestination(this);
      return;
    }

    super.accept(visitor);
  }

  @NotNull
  @Override
  public PsiReference[] getReferences() {
    return ReferenceProvidersRegistry.getReferencesFromProviders(this);
  }

  public static class Manipulator extends AbstractElementManipulator<ZenUmlLinkDestinationImpl> {

    @Override
    public ZenUmlLinkDestinationImpl handleContentChange(@NotNull ZenUmlLinkDestinationImpl element,
                                                         @NotNull TextRange range,
                                                         String newContent) throws IncorrectOperationException {
      final PsiElement child = element.getFirstChild();
      if (child instanceof LeafPsiElement) {
        ((LeafPsiElement)child).replaceWithText(range.replace(child.getText(), newContent));
      }
      else {
        throw new IncorrectOperationException("Bad child");
      }

      return element;
    }

    @NotNull
    @Override
    public TextRange getRangeInElement(@NotNull ZenUmlLinkDestinationImpl element) {
      final String text = element.getText();
      if (text.startsWith("<") && text.endsWith(">")) {
        return TextRange.create(1, text.length() - 1);
      }
      else {
        return TextRange.allOf(text);
      }
    }
  }
}
