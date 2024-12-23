package com.zenuml.sequence.plugins.jetbrains.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import org.jetbrains.annotations.NotNull;

public class DocumentationAction extends AnAction {

    private static final String TITLE = "Documentation";

    public DocumentationAction() {
        super(TITLE);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        BrowserUtil.browse("https://zenuml.atlassian.net/wiki/spaces/ZEN/pages/233373697/Documentations");
    }

    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }
}
