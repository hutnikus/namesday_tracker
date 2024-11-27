# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Prevent ProGuard from removing or obfuscating classes and methods used to access assets
-keep class android.content.res.AssetManager {
    public ** open*(...);
    public ** list*(...);
}

# Keep any classes or methods that access your assets dynamically
# For example, if you use reflection to load assets or their paths
-keepclassmembers class * {
    @android.annotation.SuppressLint *;
    public static ** load*(...);
    public ** get*(...);
}

# Prevent stripping of dynamically loaded classes or resources
-keepclassmembers class * {
    public static android.content.Context getAssets(...);
}
