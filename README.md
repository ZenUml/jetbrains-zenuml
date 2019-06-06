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

# Jetbrains Market Place Demo
1. Use @Xiaopeng's account to login into https://intellij-support.jetbrains.com/hc/en-us
1. Find `Marketplace EAP` section and related documentations. https://intellij-support.jetbrains.com/hc/en-us/categories/115000066390-Marketplace-EAP
1. Contact Jetbrains to set up credential for accessing `Market Place Demo Instance` https://marketplace.demo.plugins.aws.intellij.net 
1. Use@Xiaopeng's account to login into `Demo Instance`. Then click `My Profile` in right top corner dropdown. Then Zenuml Plugin` will be in the dashboard.
1. Follow documentations in step 2 for usage and testing.


### [Enabling Internal Mode Of IntelliJ IDEA.](http://www.jetbrains.org/intellij/sdk/docs/reference_guide/internal_actions/enabling_internal.html)
Not Sure how to use it for  now, But might need it at a later time.