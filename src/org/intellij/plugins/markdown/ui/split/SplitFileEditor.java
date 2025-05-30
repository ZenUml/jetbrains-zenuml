package org.intellij.plugins.markdown.ui.split;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.ex.EditorGutterComponentEx;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.JBSplitter;
import org.intellij.plugins.markdown.MarkdownBundle;
import org.intellij.plugins.markdown.settings.ZenUmlApplicationSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class SplitFileEditor<E1 extends FileEditor, E2 extends FileEditor> extends UserDataHolderBase implements FileEditor {
  public static final Key<SplitFileEditor> PARENT_SPLIT_KEY = Key.create("parentSplit");

  private static final String MY_PROPORTION_KEY = "SplitFileEditor.Proportion";

  @NotNull
  protected final E1 myMainEditor;
  @NotNull
  protected final E2 mySecondEditor;
  @NotNull
  private final JComponent myComponent;
  @NotNull
  private SplitEditorLayout mySplitEditorLayout =
    ZenUmlApplicationSettings.getInstance().getMarkdownPreviewSettings().getSplitEditorLayout();

  private boolean myVerticalSplitOption = ZenUmlApplicationSettings.getInstance().getMarkdownPreviewSettings().isVerticalSplit();
  @NotNull
  private final MyListenersMultimap myListenersGenerator = new MyListenersMultimap();

  private SplitEditorToolbar myToolbarWrapper;
  private JBSplitter mySplitter;

  public SplitFileEditor(@NotNull E1 mainEditor, @NotNull E2 secondEditor) {
    myMainEditor = mainEditor;
    mySecondEditor = secondEditor;

    myComponent = createComponent();

    if (myMainEditor instanceof TextEditor) {
      myMainEditor.putUserData(PARENT_SPLIT_KEY, this);
    }
    if (mySecondEditor instanceof TextEditor) {
      mySecondEditor.putUserData(PARENT_SPLIT_KEY, this);
    }

    ZenUmlApplicationSettings.SettingsChangedListener settingsChangedListener =
      new ZenUmlApplicationSettings.SettingsChangedListener() {
        @Override
        public void beforeSettingsChanged(@NotNull ZenUmlApplicationSettings newSettings) {
          SplitEditorLayout oldSplitEditorLayout =
            ZenUmlApplicationSettings.getInstance().getMarkdownPreviewSettings().getSplitEditorLayout();

          boolean oldVerticalSplitOption =
            ZenUmlApplicationSettings.getInstance().getMarkdownPreviewSettings().isVerticalSplit();

          ApplicationManager.getApplication().invokeLater(() -> {
            if (oldSplitEditorLayout == mySplitEditorLayout) {
              triggerLayoutChange(newSettings.getMarkdownPreviewSettings().getSplitEditorLayout(), false);
            }

            if (oldVerticalSplitOption == myVerticalSplitOption) {
              triggerSplitOrientationChange(newSettings.getMarkdownPreviewSettings().isVerticalSplit());
            }
          });
        }
      };

    ApplicationManager.getApplication().getMessageBus().connect(this)
      .subscribe(ZenUmlApplicationSettings.SettingsChangedListener.TOPIC, settingsChangedListener);
  }

  private void triggerSplitOrientationChange(boolean isVerticalSplit) {
    if (myVerticalSplitOption == isVerticalSplit) {
      return;
    }

    myVerticalSplitOption = isVerticalSplit;

    myToolbarWrapper.refresh();
    mySplitter.setOrientation(!myVerticalSplitOption);
    myComponent.repaint();
  }

  @NotNull
  private JComponent createComponent() {
    mySplitter =
      new JBSplitter(!ZenUmlApplicationSettings.getInstance().getMarkdownPreviewSettings().isVerticalSplit(), 0.5f, 0.15f, 0.85f);
    mySplitter.setSplitterProportionKey(MY_PROPORTION_KEY);
    mySplitter.setFirstComponent(myMainEditor.getComponent());
    mySplitter.setSecondComponent(mySecondEditor.getComponent());

    myToolbarWrapper = new SplitEditorToolbar(mySplitter);
    if (myMainEditor instanceof TextEditor) {
      myToolbarWrapper.addGutterToTrack(((EditorGutterComponentEx)((TextEditor)myMainEditor).getEditor().getGutter()));
    }
    if (mySecondEditor instanceof TextEditor) {
      myToolbarWrapper.addGutterToTrack(((EditorGutterComponentEx)((TextEditor)mySecondEditor).getEditor().getGutter()));
    }

    final JPanel result = new JPanel(new BorderLayout());
    result.add(myToolbarWrapper, BorderLayout.NORTH);
    result.add(mySplitter, BorderLayout.CENTER);
    adjustEditorsVisibility();

    return result;
  }

  public void triggerLayoutChange() {
    final int oldValue = mySplitEditorLayout.ordinal();
    final int N = SplitEditorLayout.values().length;
    final int newValue = (oldValue + N - 1) % N;

    triggerLayoutChange(SplitEditorLayout.values()[newValue], true);
  }

  public void triggerLayoutChange(@NotNull SplitEditorLayout newLayout, boolean requestFocus) {
    if (mySplitEditorLayout == newLayout) {
      return;
    }

    mySplitEditorLayout = newLayout;
    invalidateLayout(requestFocus);
  }

  @NotNull
  public SplitEditorLayout getCurrentEditorLayout() {
    return mySplitEditorLayout;
  }

  private void invalidateLayout(boolean requestFocus) {
    adjustEditorsVisibility();
    myToolbarWrapper.refresh();
    myComponent.repaint();

    if (!requestFocus) return;

    final JComponent focusComponent = getPreferredFocusedComponent();
    if (focusComponent != null) {
      IdeFocusManager.findInstanceByComponent(focusComponent).requestFocus(focusComponent, true);
    }
  }

  private void adjustEditorsVisibility() {
    myMainEditor.getComponent().setVisible(mySplitEditorLayout.showFirst);
    mySecondEditor.getComponent().setVisible(mySplitEditorLayout.showSecond);
  }

  @NotNull
  public E1 getMainEditor() {
    return myMainEditor;
  }

  @NotNull
  public E2 getSecondEditor() {
    return mySecondEditor;
  }

  @NotNull
  @Override
  public JComponent getComponent() {
    return myComponent;
  }

  @Nullable
  @Override
  public JComponent getPreferredFocusedComponent() {
    if (mySplitEditorLayout.showFirst) return myMainEditor.getPreferredFocusedComponent();
    if (mySplitEditorLayout.showSecond) return mySecondEditor.getPreferredFocusedComponent();
    return null;
  }

  @NotNull
  @Override
  public FileEditorState getState(@NotNull FileEditorStateLevel level) {
    return new MyFileEditorState(mySplitEditorLayout.name(), myMainEditor.getState(level), mySecondEditor.getState(level));
  }

  @Override
  public void setState(@NotNull FileEditorState state) {
    if (state instanceof MyFileEditorState) {
      final MyFileEditorState compositeState = (MyFileEditorState)state;
      if (compositeState.getFirstState() != null) {
        myMainEditor.setState(compositeState.getFirstState());
      }
      if (compositeState.getSecondState() != null) {
        mySecondEditor.setState(compositeState.getSecondState());
      }
      if (compositeState.getSplitLayout() != null && SplitEditorLayout.isValid(compositeState.getSplitLayout())) {
        mySplitEditorLayout = SplitEditorLayout.valueOf(compositeState.getSplitLayout());
        invalidateLayout(true);
      }
    }
  }

  @Override
  public boolean isModified() {
    return myMainEditor.isModified() || mySecondEditor.isModified();
  }

  @Override
  public boolean isValid() {
    return myMainEditor.isValid() && mySecondEditor.isValid();
  }

  @Override
  public void selectNotify() {
    myMainEditor.selectNotify();
    mySecondEditor.selectNotify();
  }

  @Override
  public void deselectNotify() {
    myMainEditor.deselectNotify();
    mySecondEditor.deselectNotify();
  }

  @Override
  public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    myMainEditor.addPropertyChangeListener(listener);
    mySecondEditor.addPropertyChangeListener(listener);

    final DoublingEventListenerDelegate delegate = myListenersGenerator.addListenerAndGetDelegate(listener);
    myMainEditor.addPropertyChangeListener(delegate);
    mySecondEditor.addPropertyChangeListener(delegate);
  }

  @Override
  public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    myMainEditor.removePropertyChangeListener(listener);
    mySecondEditor.removePropertyChangeListener(listener);

    final DoublingEventListenerDelegate delegate = myListenersGenerator.removeListenerAndGetDelegate(listener);
    if (delegate != null) {
      myMainEditor.removePropertyChangeListener(delegate);
      mySecondEditor.removePropertyChangeListener(delegate);
    }
  }

  @Nullable
  @Override
  public BackgroundEditorHighlighter getBackgroundHighlighter() {
    return myMainEditor.getBackgroundHighlighter();
  }

  @Nullable
  @Override
  public FileEditorLocation getCurrentLocation() {
    return myMainEditor.getCurrentLocation();
  }

  @Nullable
  @Override
  public StructureViewBuilder getStructureViewBuilder() {
    return myMainEditor.getStructureViewBuilder();
  }

  @Override
  public void dispose() {
    Disposer.dispose(myMainEditor);
    Disposer.dispose(mySecondEditor);
  }

  public static class MyFileEditorState implements FileEditorState {
    @Nullable
    private final String mySplitLayout;
    @Nullable
    private final FileEditorState myFirstState;
    @Nullable
    private final FileEditorState mySecondState;

    public MyFileEditorState(@Nullable String splitLayout, @Nullable FileEditorState firstState, @Nullable FileEditorState secondState) {
      mySplitLayout = splitLayout;
      myFirstState = firstState;
      mySecondState = secondState;
    }

    @Nullable
    public String getSplitLayout() {
      return mySplitLayout;
    }

    @Nullable
    public FileEditorState getFirstState() {
      return myFirstState;
    }

    @Nullable
    public FileEditorState getSecondState() {
      return mySecondState;
    }

    @Override
    public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level) {
      return otherState instanceof MyFileEditorState
             && (myFirstState == null || myFirstState.canBeMergedWith(((MyFileEditorState)otherState).myFirstState, level))
             && (mySecondState == null || mySecondState.canBeMergedWith(((MyFileEditorState)otherState).mySecondState, level));
    }
  }

  private class DoublingEventListenerDelegate implements PropertyChangeListener {
    @NotNull
    private final PropertyChangeListener myDelegate;

    private DoublingEventListenerDelegate(@NotNull PropertyChangeListener delegate) {
      myDelegate = delegate;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      myDelegate.propertyChange(new PropertyChangeEvent(SplitFileEditor.this, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()));
    }
  }

  private class MyListenersMultimap {
    private final Map<PropertyChangeListener, Pair<Integer, DoublingEventListenerDelegate>> myMap = new HashMap<>();

    @NotNull
    public DoublingEventListenerDelegate addListenerAndGetDelegate(@NotNull PropertyChangeListener listener) {
      if (!myMap.containsKey(listener)) {
        myMap.put(listener, Pair.create(1, new DoublingEventListenerDelegate(listener)));
      }
      else {
        final Pair<Integer, DoublingEventListenerDelegate> oldPair = myMap.get(listener);
        myMap.put(listener, Pair.create(oldPair.getFirst() + 1, oldPair.getSecond()));
      }

      return myMap.get(listener).getSecond();
    }

    @Nullable
    public DoublingEventListenerDelegate removeListenerAndGetDelegate(@NotNull PropertyChangeListener listener) {
      final Pair<Integer, DoublingEventListenerDelegate> oldPair = myMap.get(listener);
      if (oldPair == null) {
        return null;
      }

      if (oldPair.getFirst() == 1) {
        myMap.remove(listener);
      }
      else {
        myMap.put(listener, Pair.create(oldPair.getFirst() - 1, oldPair.getSecond()));
      }
      return oldPair.getSecond();
    }
  }

  public enum SplitEditorLayout {
    FIRST(true, false, MarkdownBundle.message("markdown.layout.editor.only")),
    SECOND(false, true, MarkdownBundle.message("markdown.layout.preview.only")),
    SPLIT(true, true, MarkdownBundle.message("markdown.layout.editor.and.preview"));

    public final boolean showFirst;
    public final boolean showSecond;
    public final String presentationName;

    SplitEditorLayout(boolean showFirst, boolean showSecond, String presentationName) {
      this.showFirst = showFirst;
      this.showSecond = showSecond;
      this.presentationName = presentationName;
    }

    public String getPresentationText() {
      //noinspection ConstantConditions
      return StringUtil.capitalize(StringUtil.substringAfter(presentationName, "Show "));
    }

    public static boolean isValid(String value) {
      return Arrays.stream(values()).anyMatch(e -> e.name().equals(value));
    }

    @Override
    public String toString() {
      return presentationName;
    }
  }
}
