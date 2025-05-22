/*
 *abiola 2022
 */

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("mshdabiola.android.feature")
}

android {
    namespace = "com.mshdabiola.main"
}
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.components.resources)
                implementation(libs.lazyPaginationCompose)
//                implementation("dev.sargunv.maplibre-compose:maplibre-compose:0.7.0")
//                implementation("dev.sargunv.maplibre-compose:maplibre-compose-material3:0.7.0")


            }
        }


    }
}
