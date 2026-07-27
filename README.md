# Valet - Android App

An Android application built with Java and Material Design 3.

## Tech Stack

- **Language**: Java
- **Min SDK**: API 24 (Android 7.0 Nougat)
- **Target/Compile SDK**: API 34 (Android 14)
- **Build System**: Gradle 8.4 with Kotlin DSL (`build.gradle.kts`)
- **Android Gradle Plugin**: 8.2.2
- **UI Toolkit**: Material Design 3, AndroidX AppCompat, ConstraintLayout

## Project Structure

```
android/
├── app/
│   ├── src/main/
│   │   ├── java/com/valet/app/    # Application source code
│   │   │   └── MainActivity.java  # Main entry point
│   │   ├── res/
│   │   │   ├── layout/            # XML layouts
│   │   │   ├── values/            # Strings, colors, themes
│   │   │   └── mipmap-*/          # Launcher icons
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts           # App-level build config
│   └── proguard-rules.pro         # ProGuard/R8 rules
├── gradle/wrapper/                # Gradle wrapper
├── build.gradle.kts               # Root build config
├── settings.gradle.kts            # Project settings
└── gradle.properties              # Build properties
```

## Prerequisites

- **Java 17** or higher
- **Android SDK** with:
  - Platform SDK 34 (`platforms;android-34`)
  - Build Tools 34.0.0 (`build-tools;34.0.0`)

### Installing the Android SDK (macOS)

```bash
brew install --cask android-commandlinetools
sdkmanager "platforms;android-34" "build-tools;34.0.0" "platform-tools"
```

Then create a `local.properties` file in the project root:

```properties
sdk.dir=/opt/homebrew/share/android-commandlinetools
```

Or set the `ANDROID_HOME` environment variable instead.

## Building

### Debug build

```bash
./gradlew assembleDebug
```

The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.

### Release build

```bash
./gradlew assembleRelease
```

> Note: Release builds require signing configuration. See the [Android signing docs](https://developer.android.com/studio/publish/app-signing) for setup instructions.

### Clean build

```bash
./gradlew clean assembleDebug
```

## Running

### On a connected device or emulator

```bash
./gradlew installDebug
```

Or open the project in Android Studio and click **Run**.

## Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| `androidx.appcompat:appcompat` | 1.6.1 | Backward-compatible Activity and UI components |
| `com.google.android.material:material` | 1.11.0 | Material Design 3 components and theming |
| `androidx.constraintlayout:constraintlayout` | 2.1.4 | Flexible layout manager |

## License

All rights reserved.
