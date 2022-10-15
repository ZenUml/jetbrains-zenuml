package com.zenuml.sequence.plugins.jetbrains.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.zenuml.license.CheckLicense;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Eugene Zhuravlev
 * Date: 17-Sep-18
 */
public class DemoAction extends AnAction {

  private static final String TITLE = "JB Marketplace Demo";

  public DemoAction() {
    super(TITLE);
  }

  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    final boolean isLicensed = CheckLicense.isLicensed();
    final String message = "Sample Plugin with License Support.\nLicense successfully obtained: " + (isLicensed? "yes" : "no");
    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, TITLE, JOptionPane.INFORMATION_MESSAGE);
  }

  public void update(@NotNull AnActionEvent e) {
    super.update(e);
  }
}
