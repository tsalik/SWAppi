// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.apollo) apply false
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

val detektAll by tasks.registering(io.gitlab.arturbosch.detekt.Detekt::class) {
    description = "Custom Detekt task for all modules"
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    setSource(files(projectDir))
    config.from(files(rootProject.files("config/detekt/detekt.yml")))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    baseline.fileValue(rootProject.file("config/detekt/baseline.xml"))
    reports {
        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        html {
            required.set(true)
            outputLocation.set(file("build/reports/detekt.html"))
        }
    }
}

val detektAllBaseline by tasks.registering(io.gitlab.arturbosch.detekt.DetektCreateBaselineTask::class) {
    description = "Custom Detekt task to build baseline for all modules"
    setSource(files(projectDir))
    baseline.fileValue(rootProject.file("config/detekt/baseline.xml"))
    config.from(files(rootProject.files("config/detekt/detekt.yml")))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**")
}
