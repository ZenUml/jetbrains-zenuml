package org.intellij.plugins.markdown.ui.actions.editorLayout;

import org.intellij.plugins.markdown.ui.split.SplitFileEditor;

public class ZenUmlPreviewOnlyLayoutChangeAction extends BaseChangeSplitLayoutAction {
  protected ZenUmlPreviewOnlyLayoutChangeAction() {
    super(SplitFileEditor.SplitEditorLayout.SECOND);
  }
}
