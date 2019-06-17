# jetbrains-zenuml
The ZenUML plugin running on the JetBrains IDEs.

# Development
1. `./gradlew`
2. `./gradlew runIde` to start another instance of IDEA with our plugin installed.

# Github Release
1. add new tag to the commit id on **master** branch, format: `release-******`
1. push tag to remote
1. travis will release zenuml.zip to github

# Publish
1. Log on to Market place.
1. Use github release file to upload.

# Installation
### Alpha Versions
#### Inside Jetbrains IDE 
1. Add `https://plugins.jetbrains.com/plugins/alpha/list` as plugin repository
1. Search for `ZenUml`, click install button then follow instructions to restart IDE 

# Jetbrains Market Place Demo
## Terminology
1. Account: the account you register on plugin.jetbrains.com
1. Credential: An email & password pair. (To access Demo instance) 
## Process
1. Use @Xiaopeng's account to login into https://intellij-support.jetbrains.com/hc/en-us
1. Find `Marketplace EAP` section and related documentations. https://intellij-support.jetbrains.com/hc/en-us/categories/115000066390-Marketplace-EAP
1. Contact Jetbrains to set up credential for accessing `Market Place Demo Instance` https://marketplace.demo.plugins.aws.intellij.net 
1. Use@Xiaopeng's account to login into `Demo Instance`. Then click `My Profile` in right top corner dropdown. Then `Zenuml Plugin` will be in the dashboard.
1. Login Out any Account ( No marketplace accout is needed for buying plugin) on `Demo Instance` https://marketplace.demo.plugins.aws.intellij.net, then go to https://marketplace.demo.plugins.aws.intellij.net/plugin/12437-zenuml-support# to buy zenuml plugin.
1. You may be asked for password by https://entrance.auth.eu-west-1.amazoncognito.com . Enter the credential for `Market Place Demo` in Step 3.
1. Follow documentations NO.6 https://intellij-support.jetbrains.com/hc/en-us/articles/360000464820-6-Test-the-plugin-licensing-on-the-demo-instance in step 2 for usage and testing.

!!! @xiaopeng's account is used for access `Market Place EAP docs` and Publish Plugin in `Demo Instance`.


### [Enabling Internal Mode Of IntelliJ IDEA.](http://www.jetbrains.org/intellij/sdk/docs/reference_guide/internal_actions/enabling_internal.html)
Not Sure how to use it for  now, But might need it at a later time.