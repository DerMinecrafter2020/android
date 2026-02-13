# Retrofit
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class retrofit2.adapter.kotlin.coroutines.** { *; }
-keep class com.squareup.okhttp.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.annotations.** { *; }
-dontwarn com.google.gson.**

# Keep all app classes
-keep class com.android.swingmusic.** { *; }
-dontwarn com.android.swingmusic.**

# Timber - strip logging calls from release builds
-assumenosideeffects class timber.log.Timber* {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
    public static *** wtf(...);
    public static *** tag(...);
}
