package org.intellij.sequencer;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;

public class ShowZenUMLScratchAction extends AnAction {

    public ShowZenUMLScratchAction() {
    }

    public void update(AnActionEvent event) {
        super.update(event);

        Presentation presentation = event.getPresentation();
        SequencePlugin2 plugin = getPlugin(event);
        presentation.setEnabled(plugin != null && plugin.isInsideAMethod());
    }

    public void actionPerformed(AnActionEvent event) {
        SequencePlugin2 plugin = getPlugin(event);
        if (plugin != null) {
            plugin.showZenUMLScratch(event);
        }
    }

    private SequencePlugin2 getPlugin(AnActionEvent event) {
        Project project = getProject(event);
        if (project == null)
            return null;
        return getPlugin(project);
    }

    private SequencePlugin2 getPlugin(Project project) {
        return SequencePlugin2.getInstance(project);
    }

    private Project getProject(AnActionEvent event) {
        return event.getData(CommonDataKeys.PROJECT);
    }

}
