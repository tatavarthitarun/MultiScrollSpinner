# Multi-Scroll Spinner

[![JitPack](https://jitpack.io/v/tatavarthitarun/MultiScrollSpinner.svg)](https://jitpack.io/#tatavarthitarun/MultiScrollSpinner)

A custom Android spinner (dropdown list) widget that supports both horizontal scrolling for long item text and vertical scrolling for multiple items. Built with Kotlin and Material Design components.

## Features

- ✅ **Horizontal Scrolling**: Each dropdown item can scroll horizontally if the text is too long to fit on screen
- ✅ **Vertical Scrolling**: The dropdown list scrolls vertically when there are many items
- ✅ **Material Design**: Follows Android Material Design guidelines
- ✅ **Easy Integration**: Simple API for quick implementation
- ✅ **Customizable**: Easy to customize appearance and behavior
- ✅ **Performance Optimized**: Uses RecyclerView for efficient rendering of large lists

## Requirements

- **Min SDK**: 29 (Android 10)
- **Target SDK**: 36 (Android 15)
- **Kotlin**: 2.0.21+
- **Gradle**: 8.13+

## Installation

Add JitPack repository to your project's `settings.gradle.kts` (or `settings.gradle`):

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Then add the dependency in your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.tatavarthitarun:MultiScrollSpinner:v0.0.1")
}
```

**Important:** 
- Make sure you have created and pushed a Git tag (e.g., `v0.0.1`) to GitHub
- JitPack needs to build the library first - visit https://jitpack.io/#tatavarthitarun/MultiScrollSpinner to trigger the build
- Replace `tatavarthitarun` with your GitHub username
- Replace `v0.0.1` with the release tag you want to use

**Troubleshooting "Failed to resolve" error:**

1. **If tag doesn't exist, create and push it:**
   ```bash
   git tag -a v0.0.1 -m "Release v0.0.1"
   git push origin v0.0.1
   ```

   **If tag already exists locally:**
   ```bash
   # Just push the existing tag
   git push origin v0.0.1
   
   # Or if you need to update it:
   git tag -d v0.0.1                    # Delete local tag
   git push origin :refs/tags/v0.0.1   # Delete remote tag
   git tag -a v0.0.1 -m "Release v0.0.1"  # Create new tag
   git push origin v0.0.1              # Push new tag
   ```

2. **Verify tag exists on GitHub:**
   Visit: `https://github.com/tatavarthitarun/MultiScrollSpinner/tags`
   The tag `v0.0.1` should be visible

3. **Trigger JitPack build:**
   - Visit: `https://jitpack.io/#tatavarthitarun/MultiScrollSpinner`
   - Click "Get it" or "Look up" next to `v0.0.1`
   - Wait for build to complete (first build: 5-10 minutes)
   - Look for green checkmark ✓ when build succeeds

4. **Verify JitPack repository in your sample project:**
   Make sure `settings.gradle.kts` has:
   ```kotlin
   maven { url = uri("https://jitpack.io") }
   ```

5. **If build fails on JitPack:**
   - **Check the build log:** Click on the version on JitPack website to see detailed error
   - **Try removing jitpack.yml:** Delete the file, commit, and recreate the tag - JitPack auto-detection often works better
   - **Common fixes:**
     - Ensure Gradle wrapper version in `gradle-wrapper.properties` is compatible (currently 8.13)
     - Verify `:library` module is correctly configured
     - Check that all dependencies in `libs.versions.toml` are valid
     - Make sure repository is public (JitPack only works with public repos)

6. **Alternative: Use commit SHA for testing:**
   ```kotlin
   // Get commit SHA: git rev-parse HEAD
   implementation("com.github.tatavarthitarun:MultiScrollSpinner:COMMIT_SHA")
   ```

6. **Check repository visibility:**
   - Repository must be **public** on GitHub
   - Private repos require JitPack Pro

## Usage

### Basic Setup

1. **Add to your layout XML:**

```xml
<com.tatav.multiscrollspinner.MultiScrollSpinner
    android:id="@+id/multiScrollSpinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

2. **Initialize in your Activity/Fragment:**

```kotlin
val spinner = findViewById<MultiScrollSpinner>(R.id.multiScrollSpinner)

// Set items
val items = listOf(
    "Short item",
    "Medium length item",
    "This is a very long item that will require horizontal scrolling",
    "Another item",
    // ... more items
)

spinner.setItems(items)

// Set selection listener
spinner.setOnItemSelectedListener { position, item ->
    Toast.makeText(this, "Selected: $item", Toast.LENGTH_SHORT).show()
}
```

### Customization via XML

You can customize the spinner using XML attributes:

```xml
<com.tatav.multiscrollspinner.MultiScrollSpinner
    android:id="@+id/multiScrollSpinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:ms_dropdownArrow="@drawable/custom_arrow"
    app:ms_arrowTint="#6200EE"
    app:ms_textSize="18sp"
    app:ms_textColor="#000000"
    app:ms_hintText="Choose an option"
    app:ms_hintTextColor="#9E9E9E"
    app:ms_itemTextSize="16sp"
    app:ms_itemTextColor="#424242"
    app:ms_selectedItemBackground="#E3F2FD"
    app:ms_selectedItemTextColor="#1976D2"
    app:ms_itemHeight="56dp"
    app:ms_maxDropdownHeight="300dp"
    app:ms_popupBackground="#FFFFFF" />
```

### Customization via Code

You can also customize programmatically:

```kotlin
val spinner = findViewById<MultiScrollSpinner>(R.id.multiScrollSpinner)

// Arrow customization
spinner.setDropdownArrow(R.drawable.custom_arrow)
spinner.setArrowTint(Color.BLUE)

// Text customization
spinner.setTextSize(18f) // in sp
spinner.setTextColor(Color.BLACK)
spinner.setHintText("Select an option")
spinner.setHintTextColor(Color.GRAY)

// Item customization
spinner.setItemTextSize(16f) // in sp
spinner.setItemTextColor(Color.DKGRAY)
spinner.setSelectedItemBackground(Color.LIGHT_BLUE)
spinner.setSelectedItemTextColor(Color.BLUE)
spinner.setItemHeight(56) // in dp

// Popup customization
spinner.setMaxDropdownHeight(400) // in dp
spinner.setPopupBackground(Color.WHITE)
```

### Advanced Usage

#### Programmatically Set Selection

```kotlin
// Select item at position 2
spinner.setSelection(2)

// Get selected item
val selectedItem = spinner.getSelectedItem()
val selectedPosition = spinner.getSelectedPosition()
```

#### Handle Empty State

```kotlin
// The spinner will show "Select an item" when no item is selected
spinner.setItems(emptyList()) // Empty list
```

## Customization Attributes

### XML Attributes Reference

| Attribute | Type | Description | Default |
|-----------|------|-------------|---------|
| `ms_dropdownArrow` | reference | Drawable resource for dropdown arrow | `@android:drawable/arrow_down_float` |
| `ms_arrowTint` | color | Color for arrow tinting | Theme default |
| `ms_arrowWidth` | dimension | Width of dropdown arrow | 24dp |
| `ms_arrowHeight` | dimension | Height of dropdown arrow | 24dp |
| `ms_textSize` | dimension | Text size for selected item | 16sp |
| `ms_textColor` | color | Text color for selected item | Theme default |
| `ms_hintText` | string | Hint text when no item selected | "Select an item" |
| `ms_hintTextColor` | color | Color for hint text | Theme default |
| `ms_itemTextSize` | dimension | Text size for dropdown items | 16sp |
| `ms_itemTextColor` | color | Text color for dropdown items | Theme default |
| `ms_selectedItemBackground` | color | Background color for selected item | `@android:color/holo_blue_light` |
| `ms_selectedItemTextColor` | color | Text color for selected item | `@android:color/white` |
| `ms_itemHeight` | dimension | Height of each dropdown item | 48dp |
| `ms_maxDropdownHeight` | dimension | Maximum height of dropdown | 60% screen or 400dp |
| `ms_popupBackground` | reference/color | Background for popup | `@android:color/white` |

### Programmatic Customization Methods

| Method | Description |
|--------|-------------|
| `setDropdownArrow(drawableRes: Int)` | Set dropdown arrow drawable resource |
| `setDropdownArrow(drawable: Drawable?)` | Set dropdown arrow drawable |
| `setArrowTint(color: Int)` | Set arrow tint color |
| `setTextSize(size: Float)` | Set selected text size (in sp) |
| `setTextColor(color: Int)` | Set selected text color |
| `setHintText(hint: String)` | Set hint text |
| `setHintTextColor(color: Int)` | Set hint text color |
| `setItemTextSize(size: Float)` | Set item text size (in sp) |
| `setItemTextColor(color: Int)` | Set item text color |
| `setSelectedItemBackground(color: Int)` | Set selected item background color |
| `setSelectedItemTextColor(color: Int)` | Set selected item text color |
| `setItemHeight(height: Int)` | Set item height (in dp) |
| `setMaxDropdownHeight(height: Int)` | Set max dropdown height (in dp) |
| `setPopupBackground(background: Int)` | Set popup background |

## API Reference

### MultiScrollSpinner

#### Methods

| Method | Description |
|--------|-------------|
| `setItems(items: List<String>)` | Sets the list of items to display |
| `setSelection(position: Int)` | Programmatically select an item at the given position |
| `getSelectedPosition(): Int` | Returns the currently selected position (-1 if none) |
| `getSelectedItem(): String?` | Returns the currently selected item (null if none) |
| `setOnItemSelectedListener(listener: (Int, String) -> Unit)` | Sets a callback for when an item is selected |

### Example: Complete Implementation

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<MultiScrollSpinner>(R.id.multiScrollSpinner)
        
        val items = listOf(
            "Option 1",
            "Option 2 with longer text",
            "This is an extremely long option that demonstrates horizontal scrolling capability",
            "Option 4",
            // ... more items
        )
        
        spinner.setItems(items)
        
        spinner.setOnItemSelectedListener { position, item ->
            Log.d("Spinner", "Selected: $item at position $position")
            // Handle selection
        }
        
        // Optionally set initial selection
        spinner.setSelection(0)
    }
}
```

## Project Structure

```
MultiScrollSpinner/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/tatav/multiscrollspinner/
│           │   ├── MultiScrollSpinner.kt          # Main spinner widget
│           │   ├── MultiScrollSpinnerAdapter.kt   # RecyclerView adapter
│           │   └── MultiScrollSpinnerPopup.kt    # Popup window implementation
│           └── res/
│               └── layout/
│                   ├── layout_multiscroll_spinner.xml  # Main spinner layout
│                   ├── item_multiscroll_spinner.xml   # Dropdown item layout
│                   └── popup_multiscroll_spinner.xml   # Popup container layout
└── README.md
```

## How It Works

1. **Main Spinner View**: Displays the selected item with horizontal scrolling capability
2. **Popup Window**: Shows a dropdown list using `PopupWindow` with a `RecyclerView`
3. **Item Rendering**: Each item uses a `HorizontalScrollView` to enable horizontal scrolling for long text
4. **Vertical Scrolling**: The `RecyclerView` handles vertical scrolling when there are many items
5. **Selection Handling**: Click listeners on items trigger selection callbacks and update the main spinner

## Customization

### Modify Item Height

Edit `item_multiscroll_spinner.xml`:
```xml
<TextView
    android:minHeight="48dp"  <!-- Change this value -->
    ... />
```

### Modify Dropdown Max Height

Edit `MultiScrollSpinnerPopup.kt`:
```kotlin
val maxHeight = (displayMetrics.heightPixels * 0.6).toInt()  // 60% of screen
```

### Change Selected Item Highlight Color

Edit `MultiScrollSpinnerAdapter.kt`:
```kotlin
holder.itemView.setBackgroundColor(
    ContextCompat.getColor(holder.itemView.context, android.R.color.holo_blue_light)
)
```

## Screenshots

*Add screenshots of your spinner in action here*

## Publishing to JitPack

To publish a new version of the library to JitPack:

1. **Update the version** in `library/build.gradle.kts`:
   ```kotlin
   version = "1.0.0"  // Update this
   ```

2. **Create a Git tag** for the version:
   ```bash
   git tag -a v1.0.0 -m "Release version 1.0.0"
   git push origin v1.0.0
   ```

3. **Or create a GitHub Release**:
   - Go to your repository on GitHub
   - Click "Releases" → "Create a new release"
   - Choose a tag (create new tag like `v1.0.0`)
   - Add release notes
   - Click "Publish release"

4. **JitPack will automatically build** your library when:
   - A new tag is pushed
   - A new release is created
   - You visit https://jitpack.io/#YourUsername/MultiScrollSpinner

5. **Users can then use**:
   ```kotlin
   implementation("com.github.YourUsername:MultiScrollSpinner:v1.0.0")
   ```

**Important Notes:**
- Replace `YourUsername` with your actual GitHub username in:
  - `library/build.gradle.kts` (groupId)
  - `jitpack.yml` (if needed)
  - README examples
- The first build on JitPack may take 5-10 minutes. Subsequent builds are usually faster.
- See [PUBLISHING.md](PUBLISHING.md) for detailed publishing instructions.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is open source and available under the [MIT License](LICENSE).

## Author

**Tatavarthi Tarun**

- GitHub: [@yourusername](https://github.com/yourusername)

## Acknowledgments

- Built with Android Material Design components
- Uses RecyclerView for efficient list rendering
- Inspired by the need for a spinner that handles both horizontal and vertical scrolling

---

**Note**: This is a custom implementation. For production use, consider adding more customization options, accessibility features, and comprehensive testing.
