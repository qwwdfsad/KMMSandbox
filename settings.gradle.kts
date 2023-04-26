pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://api.touchlab.dev/public")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://api.touchlab.dev/public")
    }
}

rootProject.name = "KMMSandbox"
include(":androidApp")
include(":shared")