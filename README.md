# Valet - Application Android

Une application Android construite avec Java et Material Design 3.

## Stack Technique

- **Langage** : Java
- **SDK Minimum** : API 24 (Android 7.0 Nougat)
- **SDK Cible/Compilation** : API 34 (Android 14)
- **Systeme de build** : Gradle 8.4 avec Kotlin DSL (`build.gradle.kts`)
- **Plugin Android Gradle** : 8.2.2
- **Toolkit UI** : Material Design 3, AndroidX AppCompat, ConstraintLayout

## Structure du Projet

```
android/
├── app/
│   ├── src/main/
│   │   ├── java/com/valet/app/    # Code source de l'application
│   │   │   └── MainActivity.java  # Point d'entree principal
│   │   ├── res/
│   │   │   ├── layout/            # Layouts XML
│   │   │   ├── values/            # Chaines, couleurs, themes
│   │   │   └── mipmap-*/          # Icones de lancement
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts           # Configuration de build de l'app
│   └── proguard-rules.pro         # Regles ProGuard/R8
├── gradle/wrapper/                # Wrapper Gradle
├── build.gradle.kts               # Configuration de build racine
├── settings.gradle.kts            # Parametres du projet
└── gradle.properties              # Proprietes de build
```

## Prerequis

- **Java 17** ou superieur
- **Android SDK** avec :
  - Platform SDK 34 (`platforms;android-34`)
  - Build Tools 34.0.0 (`build-tools;34.0.0`)

### Installation du Android SDK (macOS)

```bash
brew install --cask android-commandlinetools
sdkmanager "platforms;android-34" "build-tools;34.0.0" "platform-tools"
```

Ensuite, creez un fichier `local.properties` a la racine du projet :

```properties
sdk.dir=/opt/homebrew/share/android-commandlinetools
```

Ou definissez la variable d'environnement `ANDROID_HOME` a la place.

## Compilation

### Build debug

```bash
./gradlew assembleDebug
```

L'APK sera genere dans `app/build/outputs/apk/debug/app-debug.apk`.

### Build release

```bash
./gradlew assembleRelease
```

> Note : Les builds release necessitent une configuration de signature. Consultez la [documentation de signature Android](https://developer.android.com/studio/publish/app-signing) pour les instructions.

### Build propre

```bash
./gradlew clean assembleDebug
```

## Execution

### Sur un appareil connecte ou un emulateur

```bash
./gradlew installDebug
```

Ou ouvrez le projet dans Android Studio et cliquez sur **Run**.

## Dependances

| Bibliotheque | Version | Utilisation |
|--------------|---------|-------------|
| `androidx.appcompat:appcompat` | 1.6.1 | Composants Activity et UI retro-compatibles |
| `com.google.android.material:material` | 1.11.0 | Composants et theming Material Design 3 |
| `androidx.constraintlayout:constraintlayout` | 2.1.4 | Gestionnaire de layout flexible |

## Licence

Tous droits reserves.
