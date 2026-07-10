# UnityAndroidExample

Example Android project for checking the KKSoft Android SDK from a Unity-style host.

## Integration

1. Add JitPack to `settings.gradle`:

   ```gradle
   dependencyResolutionManagement {
       repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
       repositories {
           google()
           mavenCentral()
           maven { url "https://jitpack.io" }
       }
   }
   ```

2. Add the KKSoft SDK dependency to the Android library/app module:

   ```gradle
   implementation("com.github.kksoftdeveloper:KKSoftAndroidSDK:352227d")
   ```

   Use `api(...)` instead of `implementation(...)` when another module, such as
   `:launcher`, needs to compile directly against SDK classes exposed through a
   library module.

## Environment Configuration

Host apps do not need to add `BuildConfig.ENVIRONMENT`,
`BuildConfig.IS_STAGING`, `BuildConfig.IS_PRODUCTION`, or SDK base URL fields.
These values are generated inside the KKSoft SDK.

By default, the SDK uses production. For SDK development only, set the
environment before building the SDK:

```properties
# local.properties in the SDK project
environment=staging
```

## Build
Archive
```bash
./gradlew :launcher:assembleStagingDebug
./gradlew :launcher:assembleProductionDebug
```

Run
```bash
./gradlew :launcher:installStagingDebug
```

The Android Unity host code is in `unityLibrary/src/main/java/com/unity3d/player/Main2Activity.java`.
The Unity C# wrapper sample is in `UnityAssets/Scripts/KKSoftAndroid.cs`.
