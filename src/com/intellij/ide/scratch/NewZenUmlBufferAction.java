package com.intellij.ide.scratch;

import com.intellij.lang.Language;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NewZenUmlBufferAction extends DumbAwareAction {

    private static int nextBufferIndex() {
        try {
            Method method = ScratchFileActions.class.getDeclaredMethod("nextBufferIndex");
            method.setAccessible(true);
            return (int) method.invoke(null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such method: ScratchFileActions.nextBufferIndex", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static ScratchFileCreationHelper.Context createContext(@NotNull AnActionEvent event, @NotNull Project project) {
        try {
            Method method = ScratchFileActions.class.getDeclaredMethod("createContext", AnActionEvent.class, Project.class);
            method.setAccessible(true);
            return (ScratchFileCreationHelper.Context) method.invoke(null, event, project);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such method: ScratchFileActions.createContext", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static void doCreateNewScratch(@NotNull Project project, @NotNull ScratchFileCreationHelper.Context context) {
        try {
            Method method = ScratchFileActions.class.getDeclaredMethod("doCreateNewScratch", Project.class, ScratchFileCreationHelper.Context.class);
            method.setAccessible(true);
            method.invoke(null, project, context);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such method: ScratchFileActions.doCreateNewScratch", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
    public void update(@NotNull AnActionEvent e) {
        boolean enabled = e.getProject() != null && Registry.intValue("ide.scratch.buffers") > 0;
        e.getPresentation().setEnabledAndVisible(enabled);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        ScratchFileCreationHelper.Context context = createContext(e, project);
        context.filePrefix = "buffer";
        context.createOption = ScratchFileService.Option.create_if_missing;
        context.fileCounter = NewZenUmlBufferAction::nextBufferIndex;
        context.language = getLanguage();
        doCreateNewScratch(project, context);
    }

    private Language getLanguage() {
        Language zenUML = Language.findLanguageByID("ZenUML");
        return zenUML != null ? zenUML : StdLanguages.TEXT;
    }
}
