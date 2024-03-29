import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

fun NamedDomainObjectContainer<KotlinSourceSet>.enableK2Compiler() {
    all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}
