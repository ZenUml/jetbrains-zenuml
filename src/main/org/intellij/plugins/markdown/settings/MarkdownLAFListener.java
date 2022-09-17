package org.intellij.plugins.markdown.settings;

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

class MarkdownLAFListener implements LafManagerListener {
  private boolean isLastLAFWasDarcula = UIUtil.isUnderDarcula();

  @Override
  public void lookAndFeelChanged(@NotNull LafManager source) {
    final UIManager.LookAndFeelInfo newLookAndFeel = source.getCurrentLookAndFeel();
    final boolean isNewLookAndFeelDarcula = isDarcula(newLookAndFeel);

    if (isNewLookAndFeelDarcula == isLastLAFWasDarcula) {
      return;
    }

    updateCssSettingsForced(isNewLookAndFeelDarcula);
  }

  public void updateCssSettingsForced(boolean isDarcula) {
    final MarkdownCssSettings currentCssSettings = ZenUmlApplicationSettings.getInstance().getMarkdownCssSettings();
    final String stylesheetUri = StringUtil.isEmpty(currentCssSettings.getStylesheetUri())
                       ? MarkdownCssSettings.getDefaultCssSettings(isDarcula).getStylesheetUri()
                       : currentCssSettings.getStylesheetUri();

    ZenUmlApplicationSettings.getInstance().setMarkdownCssSettings(new MarkdownCssSettings(
      currentCssSettings.isUriEnabled(),
      stylesheetUri,
      currentCssSettings.isTextEnabled(),
      currentCssSettings.getStylesheetText()
    ));
    isLastLAFWasDarcula = isDarcula;
  }

  public static boolean isDarcula(@Nullable UIManager.LookAndFeelInfo laf) {
    if (laf == null) {
      return false;
    }
    return laf.getName().contains("Darcula");
  }
}
