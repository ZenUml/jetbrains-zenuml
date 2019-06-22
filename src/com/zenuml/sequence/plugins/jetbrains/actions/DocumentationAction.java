package com.zenuml.sequence.plugins.jetbrains.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.zenuml.sequence.plugins.jetbrains.license.CheckLicense;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DocumentationAction extends AnAction {

    private static final String TITLE = "Documentation";

    public DocumentationAction() {
        super(TITLE);
    }

    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        BrowserUtil.browse("https://zenuml.atlassian.net/wiki/spaces/ZEN/pages/233373697/Documentations");
    }

    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }
}
