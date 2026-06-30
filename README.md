# UnityAndroidExample

Example Android project for checking the KKSoft Android SDK from a Unity-style host.

## Build

Run:

```bash
cd /Users/kksoft/Desktop/UnityAndroidExample
./build-example.sh
```

The script will:

1. Build `kksoftsdk-release.aar` from `/Users/kksoft/Desktop/KKSoftAndroidSDK`.
2. Copy it to `unityLibrary/libs/kksoftsdk-release.aar`.
3. Publish the SDK dependency modules to Maven local.
4. Build `:launcher:assembleDebug`.

Manual build after the AAR is copied:

```bash
./gradlew :launcher:assembleDebug
```

The Android bridge is in `unityLibrary/src/main/java/com/kksoft/unityexample/KKSoftUnityBridge.java`.
The Unity C# wrapper sample is in `UnityAssets/Scripts/KKSoftAndroid.cs`.
