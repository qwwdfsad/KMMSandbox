plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.0.0").apply(false)
    id("com.android.library").version("8.0.0").apply(false)
    kotlin("android").version("1.8.20").apply(false)
    kotlin("multiplatform").version("1.8.20").apply(false)
    id("com.google.devtools.ksp").version("1.8.20-1.0.10").apply(false)
    id("com.rickclephas.kmp.nativecoroutines").version("1.0.0-ALPHA-7").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
