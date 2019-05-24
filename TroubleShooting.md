# Conflict with Markdown plugin
## Error
```DuplicateComponentKeyRegistrationException: org.intellij.plugins.markdown.settings.MarkdownApplicationSettings```

## Workaround
Disable the `markdown` plugin by adding the following line to file `disabled_plugins.txt`:

```org.intellij.plugins.markdown```

### File location
- Windows: `$HOME\.IdeaIC2019.1\config\disabled_plugins.txt`
- Mac: `~/Library/Preferences/.IdeaIC2019.1/disabled_plugins.txt`