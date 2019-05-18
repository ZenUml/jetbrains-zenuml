The new file extensions to be supported are `*.zenuml`, `*.zen` and `*.z`.

To achieve this goal, we need three classes:
1. A `Language` subclass - `ZenUmlLanguage`. `Name` of the language and `mineTypes` are defined here.
1. A `LanguageFileType` subclass - `ZenUmlLanguageFileType`. It passes the `ZenUmlLanguage` to its base class constructor.
   `Name` of the file type, default file extension and icon is defined here.
1. A `FileTypeFactory` subclass - `ZenUmlFileTypeFactory`. It calls `consumer.consume(ZenUmlFileType.INSTANCE, "z;zen;zenuml")`
   in its overridable method `createFileTypes`. Here are all file extensions defined.

Then we need to register the `FileTypeFactory` subclass in the `plugin.xml` file via the `com.intellij.fileTypeFactory`
extension point.

Re implementation, we have two choices:
1. Refactor the `MarkdownXxx` classes into what we need.
1. Build a new set of files (classes).

We will try the first approach first.
