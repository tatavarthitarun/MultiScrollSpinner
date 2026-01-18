# Publishing Guide for MultiScrollSpinner

This guide explains how to publish new versions of the library to JitPack.

## Prerequisites

- GitHub repository set up
- Git tags/releases configured
- Library module properly configured

## Publishing Steps

### Step 1: Update Version

Update the version in `library/build.gradle.kts`:

```kotlin
version = "1.0.0"  // Update this to your new version
```

### Step 2: Commit and Push Changes

```bash
git add .
git commit -m "Release version 1.0.0"
git push origin main
```

### Step 3: Create a Git Tag

**If tag doesn't exist:**
```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

**If tag already exists locally but not on GitHub:**
```bash
git push origin v1.0.0
```

**If tag exists and you want to update it:**
```bash
# Delete local tag
git tag -d v1.0.0
# Delete remote tag
git push origin :refs/tags/v1.0.0
# Create new tag
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

**Important:** Tag names should start with `v` (e.g., `v1.0.0`, `v1.0.1`, `v2.0.0`)

### Step 4: Create GitHub Release (Optional but Recommended)

1. Go to your GitHub repository
2. Click on "Releases" → "Create a new release"
3. Select the tag you just created (e.g., `v1.0.0`)
4. Add a release title (e.g., "Version 1.0.0")
5. Add release notes describing changes
6. Click "Publish release"

### Step 5: JitPack Build

1. Visit https://jitpack.io/#tatavarthitarun/MultiScrollSpinner
2. JitPack will automatically detect your new tag/release
3. Click "Get it" next to your version
4. JitPack will build your library (first build may take 5-10 minutes)
5. Once built, the version will show a green checkmark

### Step 6: Verify Installation

Users can now add the dependency:

```kotlin
dependencies {
    implementation("com.github.tatavarthitarun:MultiScrollSpinner:v1.0.0")
}
```

## Versioning Strategy

Follow [Semantic Versioning](https://semver.org/):

- **MAJOR** version (1.0.0 → 2.0.0): Breaking changes
- **MINOR** version (1.0.0 → 1.1.0): New features, backward compatible
- **PATCH** version (1.0.0 → 1.0.1): Bug fixes, backward compatible

## Troubleshooting

### Build Fails on JitPack

1. **Check the build log on JitPack:**
   - Click on the version on JitPack website
   - Scroll down to see the full build log
   - Look for specific error messages

2. **Common issues and fixes:**
   - **Gradle version mismatch:** Ensure `gradle-wrapper.properties` has compatible Gradle version
   - **Module not found:** Verify `:library` module exists and is included in `settings.gradle.kts`
   - **Dependencies not resolving:** Check that all dependencies in `libs.versions.toml` are valid
   - **Build command error:** Try removing `jitpack.yml` to let JitPack auto-detect

3. **Try removing jitpack.yml (Recommended if build fails):**
   - Delete `jitpack.yml` file completely
   - Commit and push the change: `git add . && git commit -m "Remove jitpack.yml for auto-detection" && git push`
   - Delete and recreate the tag to trigger a new build:
     ```bash
     git tag -d v0.0.1
     git push origin :refs/tags/v0.0.1
     git tag -a v0.0.1 -m "Release v0.0.1"
     git push origin v0.0.1
     ```
   - Let JitPack auto-detect the build configuration
   - JitPack usually works better with auto-detection for Android libraries
   - It will automatically detect the `:library` module and build it

4. **Verify library module structure:**
   - Ensure `library/build.gradle.kts` exists
   - Check that `library/src/main/AndroidManifest.xml` exists
   - Verify all source files are in `library/src/main/java/`

### Version Not Appearing

1. Make sure the tag is pushed to GitHub
2. Wait a few minutes for JitPack to detect it
3. Try refreshing the JitPack page
4. Check that the tag name follows the format `v*.*.*`

### Dependency Resolution Issues

1. **Check if JitPack repository is added:**
   Ensure `settings.gradle.kts` has:
   ```kotlin
   dependencyResolutionManagement {
       repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
       repositories {
           google()
           mavenCentral()
           maven { url = uri("https://jitpack.io") }  // Must be present!
       }
   }
   ```

2. **Verify the tag exists on GitHub:**
   - Visit: `https://github.com/YourUsername/MultiScrollSpinner/tags`
   - Make sure the tag (e.g., `v0.0.1`) is visible

3. **Trigger JitPack build:**
   - Visit: `https://jitpack.io/#YourUsername/MultiScrollSpinner`
   - Click "Get it" next to your version
   - Wait for build to complete (green checkmark)

4. **Use commit SHA for testing:**
   If tag doesn't work, use the commit SHA:
   ```kotlin
   implementation("com.github.YourUsername:MultiScrollSpinner:COMMIT_SHA")
   ```
   Get commit SHA: `git rev-parse HEAD`

5. **Verify repository is public:**
   - JitPack only works with public repositories
   - Check repository visibility on GitHub

6. **Check build logs on JitPack:**
   - Click on the version on JitPack website
   - Check build logs for any errors

## Testing Before Publishing

Before publishing, test locally:

```bash
./gradlew :library:publishToMavenLocal
```

Then test in a sample project:

```kotlin
repositories {
    mavenLocal()
}

dependencies {
    implementation("com.github.tatavarthitarun:MultiScrollSpinner:1.0.0")
}
```

## Additional Resources

- [JitPack Documentation](https://docs.jitpack.io/)
- [JitPack Android Guide](https://docs.jitpack.io/android/)
- [Semantic Versioning](https://semver.org/)
