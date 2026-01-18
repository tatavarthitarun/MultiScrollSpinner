# Multi-Scroll Spinner

[![JitPack](https://jitpack.io/v/tatavarthitarun/MultiScrollSpinner.svg)](https://jitpack.io/#tatavarthitarun/MultiScrollSpinner)

A custom Android spinner (dropdown list) widget that supports both horizontal scrolling for long item text and vertical scrolling for multiple items. Built with Kotlin and Material Design components.

## Features

- ✅ **Horizontal Scrolling**: Each dropdown item can scroll horizontally if the text is too long to fit on screen
- ✅ **Vertical Scrolling**: The dropdown list scrolls vertically when there are many items
- ✅ **Material Design**: Follows Android Material Design guidelines
- ✅ **Easy Integration**: Simple API for quick implementation
- ✅ **Highly Customizable**: Extensive customization options via XML attributes or programmatically
- ✅ **Performance Optimized**: Uses RecyclerView for efficient rendering of large lists
- ✅ **Toast Support**: Built-in option to show/hide toast messages on item selection

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
    implementation("com.github.tatavarthitarun:MultiScrollSpinner:v0.0.3")
}
```

## Quick Start

### 1. Add to Layout XML

```xml
<com.tatav.multiscrollspinner.MultiScrollSpinner
    android:id="@+id/multiScrollSpinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

### 2. Initialize in Code

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

// Optional: Enable automatic toast on selection
spinner.setShowToast(true)

// Set selection listener
spinner.setOnItemSelectedListener { position, item ->
    // Handle selection
    Log.d("Spinner", "Selected: $item at position $position")
}
```

## Toast Feature

The library includes a built-in option to automatically show toast messages when an item is selected.

### Enable Toast via XML

```xml
<com.tatav.multiscrollspinner.MultiScrollSpinner
    android:id="@+id/multiScrollSpinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:ms_showToast="true" />
```

### Enable Toast Programmatically

```kotlin
// Enable toast
spinner.setShowToast(true)

// Disable toast
spinner.setShowToast(false)

// Check if toast is enabled
val isEnabled = spinner.isShowToastEnabled()
```

When enabled, selecting an item will automatically show a toast message: **"Selected: [item] (Position: [position])"**

**Note**: The toast feature is disabled by default. You can still use `setOnItemSelectedListener` for custom logic even when toast is enabled.

## Customization

### XML Attributes

You can customize the spinner using XML attributes:

```xml
<com.tatav.multiscrollspinner.MultiScrollSpinner
    android:id="@+id/multiScrollSpinner"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    
    <!-- Dropdown Arrow -->
    app:ms_dropdownArrow="@drawable/custom_arrow"
    app:ms_arrowTint="#6200EE"
    app:ms_arrowWidth="24dp"
    app:ms_arrowHeight="24dp"
    
    <!-- Selected Text -->
    app:ms_textSize="18sp"
    app:ms_textColor="#000000"
    app:ms_hintText="Choose an option"
    app:ms_hintTextColor="#9E9E9E"
    
    <!-- Dropdown Items -->
    app:ms_itemTextSize="16sp"
    app:ms_itemTextColor="#424242"
    app:ms_selectedItemBackground="#E3F2FD"
    app:ms_selectedItemTextColor="#1976D2"
    app:ms_itemHeight="56dp"
    
    <!-- Popup -->
    app:ms_maxDropdownHeight="300dp"
    app:ms_popupBackground="#FFFFFF"
    
    <!-- Toast -->
    app:ms_showToast="true" />
```

### Programmatic Customization

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

// Toast customization
spinner.setShowToast(true)
```

## Complete Example

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
            "Option 5",
            // ... more items
        )
        
        spinner.setItems(items)
        
        // Enable automatic toast
        spinner.setShowToast(true)
        
        // Optional: Custom listener for additional logic
        spinner.setOnItemSelectedListener { position, item ->
            Log.d("Spinner", "Selected: $item at position $position")
            // Your custom logic here
        }
        
        // Optionally set initial selection
        spinner.setSelection(0)
    }
}
```

## API Reference

### Main Methods

| Method | Description |
|--------|-------------|
| `setItems(items: List<String>)` | Sets the list of items to display |
| `setSelection(position: Int)` | Programmatically select an item at the given position |
| `getSelectedPosition(): Int` | Returns the currently selected position (-1 if none) |
| `getSelectedItem(): String?` | Returns the currently selected item (null if none) |
| `setOnItemSelectedListener(listener: (Int, String) -> Unit)` | Sets a callback for when an item is selected |
| `setShowToast(show: Boolean)` | Enable/disable automatic toast on item selection |
| `isShowToastEnabled(): Boolean` | Check if toast is enabled |

### Customization Methods

| Method | Description |
|--------|-------------|
| `setDropdownArrow(drawableRes: Int)` | Set dropdown arrow drawable resource |
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
| `setPopupBackground(background: Int)` | Set popup background color |

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
| `ms_showToast` | boolean | Enable/disable automatic toast on selection | `false` |

## Advanced Usage

### Programmatically Set Selection

```kotlin
// Select item at position 2
spinner.setSelection(2)

// Get selected item
val selectedItem = spinner.getSelectedItem()
val selectedPosition = spinner.getSelectedPosition()
```

### Dynamic Item Updates

```kotlin
// Update items dynamically
val newItems = listOf("New Item 1", "New Item 2", "New Item 3")
spinner.setItems(newItems)

// The selection will be reset when items are updated
```

### Handle Empty State

```kotlin
// The spinner will show hint text when no item is selected
spinner.setItems(emptyList()) // Empty list
```

## Troubleshooting

### "Failed to resolve" Error

1. **Verify JitPack repository** is added to `settings.gradle.kts`:
   ```kotlin
   maven { url = uri("https://jitpack.io") }
   ```

2. **Check version tag** exists on GitHub:
   - Visit: `https://github.com/tatavarthitarun/MultiScrollSpinner/tags`
   - Ensure the version tag (e.g., `v0.0.3`) is visible

3. **Trigger JitPack build**:
   - Visit: `https://jitpack.io/#tatavarthitarun/MultiScrollSpinner`
   - Click on the version to trigger a build
   - Wait for build to complete (first build: 5-10 minutes)

4. **Sync Gradle** in Android Studio after adding the dependency

## License

This project is open source and available under the [MIT License](LICENSE).

## Author

**Tatavarthi Tarun**

- GitHub: [@tatavarthitarun](https://github.com/tatavarthitarun)

## Acknowledgments

- Built with Android Material Design components
- Uses RecyclerView for efficient list rendering
- Inspired by the need for a spinner that handles both horizontal and vertical scrolling
