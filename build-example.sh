#!/usr/bin/env bash
set -euo pipefail

SDK_DIR="/Users/kksoft/Desktop/KKSoftAndroidSDK"
EXAMPLE_DIR="/Users/kksoft/Desktop/UnityAndroidExample"
AAR_SOURCE="$SDK_DIR/kksoftsdk/build/outputs/aar/kksoftsdk-release.aar"
AAR_DEST="$EXAMPLE_DIR/unityLibrary/libs/kksoftsdk-release.aar"

cd "$SDK_DIR"
./gradlew :kksoftsdk:assembleRelease publishToMavenLocal

mkdir -p "$EXAMPLE_DIR/unityLibrary/libs"
cp "$AAR_SOURCE" "$AAR_DEST"

cd "$EXAMPLE_DIR"
if [ ! -f local.properties ] && [ -f local.properties.example ]; then
  cp local.properties.example local.properties
fi
./gradlew :launcher:assembleDebug
