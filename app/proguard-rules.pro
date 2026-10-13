# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Preserve JavascriptInterface methods for WebView bridges in release builds
-keepattributes *Annotation*,JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Preserve data models and Room entities
-keep class com.example.data.model.** { *; }
-keep class com.example.data.local.** { *; }

# Preserve source file and line numbers for Play Console crash reporting
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

