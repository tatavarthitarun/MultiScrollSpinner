# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep custom view classes
-keep class com.tatav.multiscrollspinner.** { *; }

# Keep adapter classes
-keep class * extends androidx.recyclerview.widget.RecyclerView$Adapter {
    <init>(...);
}
