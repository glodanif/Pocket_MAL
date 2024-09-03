# Pocket MAL #

Android client for myanimelist.net
> [Play Store](https://play.google.com/store/apps/details?id=com.g.pocketmal)

### Excuses
* This codebase was untouched for more than three years
* Build configurations were sloppily migrated from Groovy to Kotlin
* Proguard is disabled in order to quickly ship a particular hotfix and not solve configuration

### Setting up
To build the project you should create a `pocketmal.properties` file in the root of the project.
This file should contain these signing and API configs:
* `keystore=`
* `keystore.password=`
* `keyAlias=`
* `keyPassword=`
* `clientId=`
* `redirectUrl=`

### Who do I talk to? ###

* glodanif@gmail.com
* http://myanimelist.net/profile/G-Lodan
* https://discord.gg/GWNb4T4x
