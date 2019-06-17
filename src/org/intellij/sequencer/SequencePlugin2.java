package org.intellij.sequencer;

import com.intellij.ide.scratch.NewZenUmlBufferAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AllClassesSearch;
import com.intellij.psi.search.searches.DefinitionsScopedSearch;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.Query;
import com.zenuml.dsl.PsiToDslConverter;
import icons.SequencePluginIcons;
import org.intellij.sequencer.generator.SequenceParams;
import org.intellij.sequencer.generator.filters.MethodFilter;
import org.intellij.sequencer.ui.ButtonTabComponent;
import org.intellij.sequencer.util.PsiUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SequencePlugin2 implements ProjectComponent {
    private static final String PLAGIN_NAME = "Sequence2";
    private static final Icon DISABLED_ICON = SequencePluginIcons.LOCKED_ICON;
    private static final Icon S_ICON = SequencePluginIcons.SEQUENCE_ICON_13;

    private final Project _project;
    private ToolWindow _toolWindow;
    private JTabbedPane _jTabbedPane;

    public static Icon loadIcon(String name) {
        return IconLoader.findIcon(SequencePlugin2.class.getResource("/icons/" + name),true);
    }

    public SequencePlugin2(Project project) {
        _project = project;
    }

    public void projectOpened() {
        createTabPane();
        _toolWindow = getToolWindowManager().registerToolWindow(
              PLAGIN_NAME, false, ToolWindowAnchor.BOTTOM);
        final Content content = ServiceManager.getService(ContentFactory.class).createContent(_jTabbedPane, "", false);
        _toolWindow.getContentManager().addContent(content);
        _toolWindow.setIcon(SequencePluginIcons.SEQUENCE_ICON_13);
        _toolWindow.setAvailable(false, null);
    }

    private ToolWindowManager getToolWindowManager() {
        return ToolWindowManager.getInstance(_project);
    }

    public static SequencePlugin2 getInstance(Project project) {
        return (SequencePlugin2)project.getComponent(SequencePlugin2.class);
    }

    public void projectClosed() {
        getToolWindowManager().unregisterToolWindow(PLAGIN_NAME);
    }

    public String getComponentName() {
        return PLAGIN_NAME;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public void showSequence(AnActionEvent event, SequenceParams params) {
        PsiMethod enclosingPsiMethod = getCurrentPsiMethod();
        if(enclosingPsiMethod == null)
            return;
        _toolWindow.setAvailable(true, null);

        final SequencePanel sequencePanel = new SequencePanel(this, enclosingPsiMethod, params);
        Runnable postAction = new Runnable() {
            public void run() {
                sequencePanel.generate();
                addSequencePanel(sequencePanel);

            }
        };
        if(_toolWindow.isActive())
            _toolWindow.show(postAction);
        else
            _toolWindow.activate(postAction);
    }

    public void showZenUMLScratch(AnActionEvent event) {
        PsiMethod enclosingPsiMethod = getCurrentPsiMethod();
        if(enclosingPsiMethod == null)
            return;

        String dsl = generateZenUML(enclosingPsiMethod);
        createZenUMLScratch(dsl, event);
    }

    private String generateZenUML(PsiMethod psiMethod) {
        PsiToDslConverter psiToDslConverter = new PsiToDslConverter();
        psiToDslConverter.visitMethod(psiMethod);
        return psiToDslConverter.getDsl();
    }

    private void createZenUMLScratch(String dsl, AnActionEvent event) {
        NewZenUmlBufferAction newBufferAction = new NewZenUmlBufferAction();

        HashMap<String, Object> map = new HashMap<>();
        map.put(CommonDataKeys.PROJECT.getName(), event.getData(CommonDataKeys.PROJECT));
        map.put(LangDataKeys.IDE_VIEW.getName(), event.getData(LangDataKeys.IDE_VIEW));
        map.put(PlatformDataKeys.PREDEFINED_TEXT.getName(), dsl);
        AnActionEvent anActionEvent = AnActionEvent.createFromAnAction(newBufferAction,
                null,
                event.getPlace(),
                SimpleDataContext.getSimpleContext(map, null));
        newBufferAction.actionPerformed(anActionEvent);
    }

    private void addSequencePanel(final SequencePanel sequencePanel) {
        int tabIndex = findUnlockedTab();
        if(tabIndex != -1) {
            _jTabbedPane.setComponentAt(tabIndex, sequencePanel);
            _jTabbedPane.setTitleAt(tabIndex, sequencePanel.getTitleName());
            _jTabbedPane.setSelectedIndex(tabIndex);
            ButtonTabComponent buttonTabComponent = new ButtonTabComponent(_jTabbedPane);
            buttonTabComponent.addTabButtonListener(e -> closeSequence(sequencePanel));
            _jTabbedPane.setTabComponentAt(tabIndex, buttonTabComponent);
        }
        else {
            _jTabbedPane.addTab(sequencePanel.getTitleName(), S_ICON, sequencePanel);
            _jTabbedPane.setSelectedComponent(sequencePanel);
            ButtonTabComponent buttonTabComponent = new ButtonTabComponent(_jTabbedPane);
            buttonTabComponent.addTabButtonListener(e -> closeSequence(sequencePanel));
            _jTabbedPane.setTabComponentAt(_jTabbedPane.getSelectedIndex(), buttonTabComponent);
        }
        _toolWindow.setTitle(sequencePanel.getTitleName());
    }

    private int findUnlockedTab() {
        for(int i = 0; i < _jTabbedPane.getTabCount(); i++) {
            if(!isLockedTab(i))
                return i;
        }
        return -1;
    }

    private boolean isLockedTab(int i) {
        return _jTabbedPane.getIconAt(i) == DISABLED_ICON;
    }

    private void closeSequenceAtIndex(int index) {
        _jTabbedPane.remove(index);
        if(_jTabbedPane.getTabCount() == 0)
            _toolWindow.setAvailable(false, null);
    }

    void closeSequence(SequencePanel sequencePanel) {
        closeSequenceAtIndex(_jTabbedPane.indexOfComponent(sequencePanel));
    }

    boolean isInsideAMethod() {
        return getCurrentPsiMethod() != null;
    }

    private Editor getSelectedEditor() {
        return getFileEditorManager().getSelectedTextEditor();
    }

    private PsiManager getPsiManager() {
        return PsiManager.getInstance(_project);
    }


    private PsiFile getPsiFile(VirtualFile virtualFile) {
        return PsiManager.getInstance(_project).findFile(virtualFile);
    }

    private FileEditorManager getFileEditorManager() {
        return FileEditorManager.getInstance(_project);
    }

    private PsiMethod getCurrentPsiMethod() {
        Editor editor = getSelectedEditor();
        if(editor == null)
            return null;
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        if(virtualFile == null)
            return null;
        PsiFile psiFile = getPsiFile(virtualFile);
        return PsiUtil.getEnclosingMethod(psiFile, editor.getCaretModel().getOffset());
    }

    void openClassInEditor(final String className) {
        Query<PsiClass> search = AllClassesSearch.search(GlobalSearchScope.projectScope(_project), _project, className::endsWith);
        PsiClass psiClass = search.findFirst();
        if(psiClass == null)
            return;
        openInEditor(psiClass, psiClass);
    }

    void openMethodInEditor(String className, String methodName, List argTypes) {
        PsiMethod psiMethod = PsiUtil.findPsiMethod(_project, getPsiManager(), className, methodName, argTypes);
        if(psiMethod == null)
            return;
        openInEditor(psiMethod.getContainingClass(), psiMethod);
    }

    private void openInEditor(PsiClass psiClass, PsiElement psiElement) {
        VirtualFile virtualFile = PsiUtil.findVirtualFile(psiClass);
        if(virtualFile == null)
            return;
        getFileEditorManager().openTextEditor(new OpenFileDescriptor(_project,
                virtualFile, psiElement.getTextOffset()), true);
    }

    void openMethodCallInEditor(MethodFilter filter, String fromClass, String fromMethod, List fromArgTypes,
                                String toClass, String toMethod, List toArgType, int callNo) {
        PsiMethod fromPsiMethod = PsiUtil.findPsiMethod(_project, getPsiManager(), fromClass, fromMethod, fromArgTypes);
        if(fromPsiMethod == null)
            return;
        PsiMethod toPsiMethod = PsiUtil.findPsiMethod(_project, getPsiManager(), toClass, toMethod, toArgType);
        if(toPsiMethod == null)
            return;
        PsiElement psiElement = PsiUtil.findPsiCallExpression(filter, fromPsiMethod, toPsiMethod, callNo);
        if(psiElement == null)
            return;
        openInEditor(fromPsiMethod.getContainingClass(), psiElement);
    }

    private void createTabPane() {
        _jTabbedPane = new JBTabbedPane(JBTabbedPane.TOP);
//        if(UIManager.getLookAndFeel() instanceof MetalLookAndFeel)
//            _jTabbedPane.setUI(new PlasticTabbedPaneUI());
        _jTabbedPane.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                showPopupForTab(e);
            }

            public void mousePressed(MouseEvent e) {
                showPopupForTab(e);
            }

            public void mouseClicked(MouseEvent e) {
                showPopupForTab(e);
            }

            private void showPopupForTab(MouseEvent e) {
                if(!e.isPopupTrigger())
                    return;
                int index = _jTabbedPane.indexAtLocation(e.getX(), e.getY());
                if(index == -1)
                    return;
                DefaultActionGroup popupGroup = new DefaultActionGroup("SequencePlugin2.TabPopup", true);
                if(isLockedTab(index))
                    popupGroup.add(new LockUnlockAction(index, false));
                else
                    popupGroup.add(new LockUnlockAction(index, true));
                popupGroup.addSeparator();
                popupGroup.add(new CloseAction(index));
                ActionPopupMenu popupMenu = ActionManager.getInstance().createActionPopupMenu("Popup", popupGroup);
                popupMenu.getComponent().show(_jTabbedPane, e.getX(), e.getY());
            }
        });
        _jTabbedPane.addChangeListener(e -> {
            int selectedIndex = _jTabbedPane.getSelectedIndex();
            if(selectedIndex == -1)
                return;
            _toolWindow.setTitle(_jTabbedPane.getTitleAt(selectedIndex));
        });
    }

    List<String> findImplementations(String className) {
        PsiClass psiClass = PsiUtil.findPsiClass(_project, getPsiManager(), className);

        if (PsiUtil.isAbstract(psiClass)) {
            PsiElement[] psiElements = DefinitionsScopedSearch.search(psiClass).toArray(PsiElement.EMPTY_ARRAY);
            ArrayList<String> result = new ArrayList<>();

            for (PsiElement element : psiElements) {
                if (element instanceof PsiClass) {
                    PsiClass implClass = (PsiClass) element;
                    result.add(implClass.getQualifiedName());
                }
            }

            return result;
        }
        return new ArrayList<>();

    }

    List<String> findImplementations(String className, String methodName, List argTypes) {
        PsiMethod psiMethod = PsiUtil.findPsiMethod(_project, getPsiManager(), className, methodName, argTypes);
        PsiClass containingClass = psiMethod.getContainingClass();
        if (containingClass == null) {
            containingClass = (PsiClass) psiMethod.getParent().getContext();
        }
        if (PsiUtil.isAbstract(containingClass)) {
            PsiElement[] psiElements = DefinitionsScopedSearch.search(psiMethod).toArray(PsiElement.EMPTY_ARRAY);
            ArrayList<String> result = new ArrayList<String>();

            for (PsiElement element : psiElements) {
                if (element instanceof PsiMethod) {

                    PsiMethod method = (PsiMethod) element;
                    PsiClass implClass = method.getContainingClass();
                    if (implClass == null) {
                        implClass = (PsiClass) method.getParent().getContext();
                    }
                    result.add(implClass.getQualifiedName());
                }
            }

            return result;
        }
        return new ArrayList<String>();
    }

    private class LockUnlockAction extends AnAction {
        private int _index;
        private boolean _isLock;

        LockUnlockAction(int index, boolean isLock) {
            super(isLock? "Lock Tab": "Unlock Tab");
            _index = index;
            _isLock = isLock;
        }

        public void actionPerformed(AnActionEvent anActionEvent) {
            _jTabbedPane.setIconAt(_index, _isLock? DISABLED_ICON: S_ICON);
        }
    }

    private class CloseAction extends AnAction {
        private int _index;

        CloseAction(int index) {
            super("Close Tab");
            _index = index;
        }

        public void actionPerformed(AnActionEvent anActionEvent) {
            closeSequenceAtIndex(_index);
        }
    }

}
