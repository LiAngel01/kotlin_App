# AOD App

Android Always On Display application built with Kotlin.

## Features
- Shows time and date on the screen when charging or sleeping
- Displays battery level
- Customizable AOD experience

## Build with Codemagic

This project is configured to be built with [Codemagic](https://codemagic.io/).

### Requirements
- Android SDK
- Java JDK 17

### Build locally (Windows)
```
gradlew.bat assembleDebug
```

The APK will be generated in `app/build/outputs/apk/debug/`

### Codemagic Setup
1. Connect your GitHub repository to Codemagic
2. Set the project type to Android
3. Build will trigger automatically on push

## Project Structure
- `app/src/main/java/com/aodapp/` - Kotlin source code
- `app/src/main/res/` - Android resources
- `app/build.gradle` - App-level build configuration
- `build.gradle` - Root build configuration
