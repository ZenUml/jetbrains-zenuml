# jetbrains-zenuml
The ZenUML plugin running on the JetBrains IDEs.

# Development
1. Java version: OpenJDK 11 or higher.
1. `./gradlew`
1. `./gradlew runIde` to start another instance of IDEA with our plugin installed.
    ## Using Gradle In IntelliJ
    If you get an error like this:
    > Cause: org/openjfx/gradle/JavaFXPlugin has been compiled by a more recent version of the Java Runtime (class file version 55.0), this version of the Java Runtime only recognizes class file versions up to 52.0

    you need to change `Build Tools > Gradle > Gradle JVM` to version 11 or higher.


# Release
1. determine release version number in the format of YYYY.R.N, 2019.1.1 for example. N can be omitted for the first release of a major version.
1. File `resource/META-INF/plugin.xml` line 4, update plugin version in `<idea-plugin>.<version>` with YYYY.R.N
1. Pull request -> Merge to master
1. `git checkout master && git pull --reb`
1. `git tag -a release-YYYY.R.N -m 'release tag msg'`
1. `git push origin release-YYYY.R.N`
1. travis will release zenuml.zip to github in a few minutes

# Publish
1. Log on to Market place.
1. Use github release file to upload.

# Installation
* ### Inside Jetbrains IDE
    * (Skip this for prod version) To install Alpha Versions 
        
      Add `https://plugins.jetbrains.com/plugins/alpha/list` as plugin repository
       
    * Search for `ZenUml`, click install button then follow instructions to restart IDE 
    
* ### Install via plugin file
    1. Preferences > Plugins > setting > Install Plugin from disk.
    1. Choose the file to install.

# Jetbrains Market Place 
* Ask admin for market place access.
* https://plugins.jetbrains.com/marketplace
* Jetbrains organization: [ZenUML](https://plugins.jetbrains.com/organization/ZenUML)
* Plugin Page : [ZenUML support](https://plugins.jetbrains.com/plugin/12437-zenuml-support)

### [Enabling Internal Mode Of IntelliJ IDEA.](http://www.jetbrains.org/intellij/sdk/docs/reference_guide/internal_actions/enabling_internal.html)
Not Sure how to use it for  now, But might need it at a later time.
