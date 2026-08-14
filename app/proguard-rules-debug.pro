# Debug-specific rules to allow safe shrinking without breaking tests
-dontwarn javax.lang.model.**
-dontwarn com.google.errorprone.annotations.**
-ignorewarnings

# Remove explicit broad keeps to allow R8 to actually shrink
# -keep class androidx.glance.** { *; }
# -keep class androidx.compose.** { *; }

-keep class * implements androidx.glance.appwidget.action.ActionCallback {
    public <init>();
}
