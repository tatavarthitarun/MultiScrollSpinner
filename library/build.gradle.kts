plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

android {
    namespace = "com.tatav.multiscrollspinner"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 29
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = false
    }
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                
                groupId = "com.github.tatavarthitarun"
                artifactId = "MultiScrollSpinner"
                version = "0.0.2"
                
                pom {
                    name.set("MultiScrollSpinner")
                    description.set("A custom Android spinner widget that supports both horizontal scrolling for long item text and vertical scrolling for multiple items")
                    url.set("https://github.com/tatavarthitarun/MultiScrollSpinner")
                    
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    
                    developers {
                        developer {
                            id.set("tatavarthitarun")
                            name.set("Tatavarthi Tarun")
                        }
                    }
                    
                    scm {
                        connection.set("scm:git:git://github.com/tatavarthitarun/MultiScrollSpinner.git")
                        developerConnection.set("scm:git:ssh://github.com:tatavarthitarun/MultiScrollSpinner.git")
                        url.set("https://github.com/tatavarthitarun/MultiScrollSpinner")
                    }
                }
            }
        }
    }
}
