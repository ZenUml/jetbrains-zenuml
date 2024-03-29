package org.intellij.plugins.markdown.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.plugins.markdown.MarkdownBundle;
import org.intellij.plugins.markdown.ui.preview.MarkdownHtmlPanelProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Optional;

public class MarkdownSettingsConfigurable implements SearchableConfigurable {
  static final String PLANT_UML_DIRECTORY = "plantUML";
  static final String PLANTUML_JAR = "plantuml.jar";

  private static final String DOWNLOAD_CACHE_DIRECTORY = "download-cache";
//  @TestOnly
  public static final Ref<VirtualFile> PLANTUML_JAR_TEST = Ref.create();
  @Nullable
  private MarkdownSettingsForm myForm = null;
  @NotNull
  private final ZenUmlApplicationSettings myZenUmlApplicationSettings = ZenUmlApplicationSettings.getInstance();

  @NotNull
  @Override
  public String getId() {
    return "Settings.ZenUml";
  }

  @Nls
  @Override
  public String getDisplayName() {
    return MarkdownBundle.message("markdown.settings.name");
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    MarkdownSettingsForm form = getForm();
    if (form == null) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new JLabel(MarkdownBundle.message("markdown.settings.no.providers")), BorderLayout.NORTH);
      return panel;
    }
    return form.getComponent();
  }

  @Nullable
  public MarkdownSettingsForm getForm() {
    if (!MarkdownHtmlPanelProvider.hasAvailableProviders()) {
      return null;
    }

    if (myForm == null) {
      myForm = new MarkdownSettingsForm();
    }
    return myForm;
  }

  @Override
  public boolean isModified() {
    MarkdownSettingsForm form = getForm();
    if (form == null) {
      return false;
    }
    return !form.getMarkdownCssSettings().equals(myZenUmlApplicationSettings.getMarkdownCssSettings()) ||
           !form.getMarkdownPreviewSettings().equals(myZenUmlApplicationSettings.getMarkdownPreviewSettings()) ||
           form.isDisableInjections() != myZenUmlApplicationSettings.isDisableInjections();
  }

  @Override
  public void apply() throws ConfigurationException {
    final MarkdownSettingsForm form = getForm();
    if (form == null) {
      return;
    }

    form.validate();

    myZenUmlApplicationSettings.setMarkdownCssSettings(form.getMarkdownCssSettings());
    myZenUmlApplicationSettings.setMarkdownPreviewSettings(form.getMarkdownPreviewSettings());
    myZenUmlApplicationSettings.setDisableInjections(form.isDisableInjections());

    ApplicationManager.getApplication().getMessageBus().syncPublisher(ZenUmlApplicationSettings.SettingsChangedListener.TOPIC)
      .settingsChanged(myZenUmlApplicationSettings);
  }

  @Override
  public void reset() {
    MarkdownSettingsForm form = getForm();
    if (form == null) {
      return;
    }
    form.setMarkdownCssSettings(myZenUmlApplicationSettings.getMarkdownCssSettings());
    form.setMarkdownPreviewSettings(myZenUmlApplicationSettings.getMarkdownPreviewSettings());
  }

  @Override
  public void disposeUIResources() {
    if (myForm != null) {
      Disposer.dispose(myForm);
    }
    myForm = null;
  }

  /**
   * Returns true if PlantUML jar has been already downloaded
   */
  public static boolean isPlantUMLAvailable() {
    File jarPath = getDownloadedJarPath();
    return jarPath != null && jarPath.exists();
  }

  /**
   * Gets 'download-cache' directory PlantUML jar to be download to
   */
  @NotNull
  public static File getDirectoryToDownload() {
    return new File(PathManager.getSystemPath(), DOWNLOAD_CACHE_DIRECTORY + "/" + PLANT_UML_DIRECTORY);
  }

  /**
   * Returns {@link File} presentation of downloaded PlantUML jar
   */
  @Nullable
  public static File getDownloadedJarPath() {
    if (ApplicationManager.getApplication().isUnitTestMode()) {
      //noinspection TestOnlyProblems
      return Optional.ofNullable(PLANTUML_JAR_TEST.get()).map(VfsUtilCore::virtualToIoFile).orElse(null);
    }
    else {
      return new File(getDirectoryToDownload(), PLANTUML_JAR);
    }
  }
}
