package com.zenuml.sequence.plugins.jetbrains.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.intellij.plugins.markdown.ui.actions.MarkdownActionUtil;
import org.intellij.plugins.markdown.ui.preview.MarkdownPreviewFileEditor;
import org.intellij.plugins.markdown.ui.split.SplitFileEditor;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class OpenInBrowserAction extends AnAction {

    private static final String TITLE = "Open in browser";

    public OpenInBrowserAction() {
        super(TITLE);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        SplitFileEditor splitEditor = MarkdownActionUtil.findSplitEditor(anActionEvent);
        MarkdownPreviewFileEditor markdownPreviewFileEditor = (MarkdownPreviewFileEditor) splitEditor.getSecondEditor();
        File file = markdownPreviewFileEditor.writeHtmlToTempFile();
        if (file != null) {
            BrowserUtil.browse(file);
        }
    }

    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }
}
