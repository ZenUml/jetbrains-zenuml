package org.intellij.plugins.markdown.ui.actions.editorLayout;

import org.intellij.plugins.markdown.ui.split.SplitFileEditor;

public class ZenUmlEditorOnlyLayoutChangeAction extends BaseChangeSplitLayoutAction {
  protected ZenUmlEditorOnlyLayoutChangeAction() {
    super(SplitFileEditor.SplitEditorLayout.FIRST);
  }
}
