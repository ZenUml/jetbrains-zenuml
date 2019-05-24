# Conflict with Markdown plugin
## Error
```DuplicateComponentKeyRegistrationException: org.intellij.plugins.markdown.settings.MarkdownApplicationSettings```

## Workaround
Disable `markdown` plugin by adding the following line to file `$HOME\.IdeaIC2019.1\config\disabled_plugins.txt`:

```org.intellij.plugins.markdown```