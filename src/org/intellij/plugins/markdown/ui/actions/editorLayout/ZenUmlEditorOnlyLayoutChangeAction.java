package org.intellij.plugins.markdown.ui.actions.editorLayout;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import org.intellij.plugins.markdown.ui.split.SplitFileEditor;
import org.jetbrains.annotations.NotNull;

public class ZenUmlEditorOnlyLayoutChangeAction extends BaseChangeSplitLayoutAction {
  protected ZenUmlEditorOnlyLayoutChangeAction() {
    super(SplitFileEditor.SplitEditorLayout.FIRST);
  }

  @Override
  @NotNull
  public ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.EDT;
  }
}
