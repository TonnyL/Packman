import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class SpotlessConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(SpotlessPlugin::class.java)
            extensions.getByType(SpotlessExtension::class.java).apply {
                format("misc") {
                    target(
                        fileTree(
                            mapOf(
                                "dir" to ".",
                                "include" to listOf(
                                    "**/*.md",
                                    "**/.gitignore",
                                    "**/*.yaml",
                                    "**/*.yml",
                                    "**/*.toml",
                                    "**/*.properties",
                                    "**/*.pro"
                                ),
                                "exclude" to listOf(
                                    "**/.gradle/**",
                                    "**/.gradle-cache/**",
                                    "**/tools/**",
                                    "**/build/**"
                                )
                            )
                        )
                    )
                    trimTrailingWhitespace()
                    endWithNewline()
                }

                format("xml") {
                    target("**/res/**/*.xml")
                    targetExclude("**/build/**")
                    trimTrailingWhitespace()
                    endWithNewline()
                }

                val editorConfigPath = rootProject.file(".editorconfig")

                kotlin {
                    target("**/*.kt")
                    ktlint()
                        .setEditorConfigPath(editorConfigPath)
                    trimTrailingWhitespace()
                    endWithNewline()
                }

                kotlinGradle {
                    target("**/*.kts")
                    ktlint()
                        .setEditorConfigPath(editorConfigPath)
                    trimTrailingWhitespace()
                    endWithNewline()
                }
            }
        }
    }

}
