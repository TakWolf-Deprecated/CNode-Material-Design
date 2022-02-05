plugins {
    id("com.android.application") version "7.1.1" apply false
    id("com.android.library") version "7.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("com.google.devtools.ksp") version "1.6.10-1.0.2" apply false
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}
