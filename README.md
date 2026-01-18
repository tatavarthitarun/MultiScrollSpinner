# Multi-Scroll Spinner

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

### Option 1: Clone the Repository

```bash
git clone https://github.com/yourusername/MultiScrollSpinner.git
cd MultiScrollSpinner
```

### Option 2: Copy the Source Files

Copy the following files to your project:

- `app/src/main/java/com/tatav/multiscrollspinner/MultiScrollSpinner.kt`
- `app/src/main/java/com/tatav/multiscrollspinner/MultiScrollSpinnerAdapter.kt`
- `app/src/main/java/com/tatav/multiscrollspinner/MultiScrollSpinnerPopup.kt`
- `app/src/main/res/layout/layout_multiscroll_spinner.xml`
- `app/src/main/res/layout/item_multiscroll_spinner.xml`
- `app/src/main/res/layout/popup_multiscroll_spinner.xml`

### Dependencies

Add the following dependencies to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
```

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
