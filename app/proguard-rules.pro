# FlexiWidget ProGuard Rules - Optimized for Release

# Suppress warnings for missing optional dependencies in Jetpack/Google libraries
-dontwarn javax.lang.model.element.Modifier
-dontwarn com.google.errorprone.annotations.**
-dontwarn javax.annotation.**
-dontwarn org.checkerframework.**
-dontwarn com.google.j2objc.annotations.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

# Keep the ActionCallback classes as they are instantiated via reflection by Glance
-keep class * implements androidx.glance.appwidget.action.ActionCallback {
    public <init>();
}

# General optimization rules
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
