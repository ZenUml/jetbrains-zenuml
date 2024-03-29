// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.plugins.markdown.ui.preview;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import org.intellij.plugins.markdown.settings.ZenUmlApplicationSettings;
import org.intellij.plugins.markdown.ui.split.SplitFileEditor;
import org.jetbrains.annotations.NotNull;

public class MarkdownSplitEditor extends SplitFileEditor<TextEditor, MarkdownPreviewFileEditor> implements TextEditor {
  private boolean myAutoScrollPreview = ZenUmlApplicationSettings.getInstance().getMarkdownPreviewSettings().isAutoScrollPreview();
  private boolean myVerticalSplit = ZenUmlApplicationSettings.getInstance().getMarkdownPreviewSettings().isVerticalSplit();

  public MarkdownSplitEditor(@NotNull TextEditor mainEditor, @NotNull MarkdownPreviewFileEditor secondEditor) {
    super(mainEditor, secondEditor);

    ZenUmlApplicationSettings.SettingsChangedListener settingsChangedListener =
      new ZenUmlApplicationSettings.SettingsChangedListener() {
        @Override
        public void beforeSettingsChanged(@NotNull ZenUmlApplicationSettings newSettings) {
          boolean oldAutoScrollPreview = ZenUmlApplicationSettings.getInstance().getMarkdownPreviewSettings().isAutoScrollPreview();
          boolean oldVerticalSplit = ZenUmlApplicationSettings.getInstance().getMarkdownPreviewSettings().isVerticalSplit();

          ApplicationManager.getApplication().invokeLater(() -> {
            if (oldAutoScrollPreview == myAutoScrollPreview) {
              setAutoScrollPreview(newSettings.getMarkdownPreviewSettings().isAutoScrollPreview());
            }

            if (oldVerticalSplit == myVerticalSplit) {
              setVerticalSplit(newSettings.getMarkdownPreviewSettings().isVerticalSplit());
            }
          });
        }
      };

    ApplicationManager.getApplication().getMessageBus().connect(this)
      .subscribe(ZenUmlApplicationSettings.SettingsChangedListener.TOPIC, settingsChangedListener);

    mainEditor.getEditor().getCaretModel().addCaretListener(new MyCaretListener());
  }

  @NotNull
  @Override
  public String getName() {
    return "Markdown split editor";
  }

  @NotNull
  @Override
  public Editor getEditor() {
    return getMainEditor().getEditor();
  }

  @Override
  public boolean canNavigateTo(@NotNull Navigatable navigatable) {
    return getMainEditor().canNavigateTo(navigatable);
  }

  @Override
  public void navigateTo(@NotNull Navigatable navigatable) {
    getMainEditor().navigateTo(navigatable);
  }

  @Override
  @NotNull
  public VirtualFile getFile() {
    return getMainEditor().getFile();
  }

  public boolean isAutoScrollPreview() {
    return myAutoScrollPreview;
  }

  public void setVerticalSplit(boolean verticalSplit) {
    myVerticalSplit = verticalSplit;
  }

  public void setAutoScrollPreview(boolean autoScrollPreview) {
    myAutoScrollPreview = autoScrollPreview;
  }

  private class MyCaretListener implements CaretListener {
    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
      if (!isAutoScrollPreview()) return;

      final Editor editor = e.getEditor();
      if (editor.getCaretModel().getCaretCount() != 1) {
        return;
      }

      final int offset = editor.logicalPositionToOffset(e.getNewPosition());
      getSecondEditor().scrollToSrcOffset(offset);
    }
  }
}
