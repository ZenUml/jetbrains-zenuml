# jetbrains-zenuml
The ZenUML plugin running on the JetBrains IDEs.

# Development

1. `./gradlew`
2. `./gradlew runIde` to start another instance of IDEA with our plugin installed.

# Deployment
1. `./gradlew buildPlugin` build plugin into a local package `./build/distributions/zenuml.zip`.

# Installation
### Alpha Versions
#### Inside Jetbrains IDE 
1. Add `https://plugins.jetbrains.com/plugins/alpha/list` as plugin repository
1. Search for `ZenUml`, click install button then follow instructions to restart IDE 

### [Enabling Internal Mode Of IntelliJ IDEA.](http://www.jetbrains.org/intellij/sdk/docs/reference_guide/internal_actions/enabling_internal.html)
Not Sure how to use it for  now, But might need it at a later time.