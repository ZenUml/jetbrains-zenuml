package org.intellij.plugins.markdown.ui.actions.editorLayout;

import org.intellij.plugins.markdown.ui.split.SplitFileEditor;

public class ZenUmlEditorAndPreviewLayoutChangeAction extends BaseChangeSplitLayoutAction {
  protected ZenUmlEditorAndPreviewLayoutChangeAction() {
    super(SplitFileEditor.SplitEditorLayout.SPLIT);
  }
}
