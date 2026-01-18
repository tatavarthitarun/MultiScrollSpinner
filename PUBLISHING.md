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

Create a tag for the release:

```bash
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

1. Check the build log on JitPack
2. Ensure `jitpack.yml` is correct
3. Verify all dependencies are available
4. Check that the library module name is correct

### Version Not Appearing

1. Make sure the tag is pushed to GitHub
2. Wait a few minutes for JitPack to detect it
3. Try refreshing the JitPack page
4. Check that the tag name follows the format `v*.*.*`

### Dependency Resolution Issues

1. Ensure users have added JitPack repository:
   ```kotlin
   maven { url = uri("https://jitpack.io") }
   ```
2. Verify the groupId matches your GitHub username
3. Check that the artifactId matches the repository name

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
