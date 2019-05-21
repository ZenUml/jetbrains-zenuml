// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.intellij.plugins.markdown.lang.formatter

import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import org.intellij.plugins.markdown.lang.MarkdownLanguage

class MarkdownLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
  override fun getLanguage(): Language = MarkdownLanguage.INSTANCE

  private val STANDARD_WRAPPING_OPTIONS = arrayOf("RIGHT_MARGIN", "WRAP_ON_TYPING")

  override fun createConfigurable(baseSettings: CodeStyleSettings, modelSettings: CodeStyleSettings):
    CodeStyleConfigurable = MarkdownCodeStyleConfigurable(baseSettings, modelSettings)

  override fun getConfigurableDisplayName() = "ZenUML"

  override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType)  {
    if (settingsType == LanguageCodeStyleSettingsProvider.SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
      consumer.showStandardOptions(*STANDARD_WRAPPING_OPTIONS)
    }
  }

  override fun getCodeSample(settingsType: SettingsType): String =
    """Controller.getBook(id) {
  BookService.get(id) {
    book = BookRepo.findOne(id)
    if (book == null) {
      book = NullBook()
    }
  }
}
    """.trimMargin()
}