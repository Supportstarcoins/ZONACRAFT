# ZoneCraft Launcher — offline mode in Settings

This patch adds offline mode **only to the existing Settings page**. It does not add a new button to the main launcher screen.

## What it changes

- Adds the `Офлайн-режим` checkbox and nickname field to Settings.
- Saves the mode and nickname in `settings.bin`.
- Skips `LauncherRequest` and `AuthRequest` when offline mode is enabled.
- Keeps the existing server/profile selection screen.
- Starts an already installed local Minecraft/Forge 1.6.4 client without the old LaunchServer.
- Validates nicknames using `[A-Za-z0-9_]{3,16}`.
- Leaves online mode available when the checkbox is disabled.

## Important limitation

Offline mode cannot download a missing client. At least one client profile directory with its JAR files must already exist under the launcher's `updates` directory.

The Java helper searches these locations:

```text
updates/<profileName>
updates/clients/<profileName>
updates/client/<profileName>
updates/client
updates
```

For a shaders profile, it also falls back from `<profileName>_shaders` to `<profileName>`.

## Installation

Download this patch directory and run:

```bat
install_offline_patch.bat "C:\Users\User\Desktop\Meine Projeckte\ZONACRAFT\zonecraftlauncher"
```

The installer backs up the replaced JS/FXML files with the suffix:

```text
.offline_backup
```

It then uses `build_and_embed_runtime_v5.bat` when that script is present. Otherwise it runs:

```bat
gradlew clean build
```

## Starting the launcher

Use the same development command as before:

```bat
"C:\Program Files\BellSoft\LibericaJDK-8-Full\bin\java.exe" -Dlauncher.debug=true -Dlauncher.devUnsigned=true -jar "build\libs\zonecraftlauncher.jar"
```

Open Settings, enable `Офлайн-режим`, enter a nickname, apply the settings, then use the existing launcher flow to choose a local profile.

## Files added or changed

```text
src/main/java/launcher/client/LocalOfflineLauncher.java
src/main/resources/runtime/runtime/config.js
src/main/resources/runtime/runtime/dialog/auth/auth.js
src/main/resources/runtime/runtime/dialog/servers/servers.js
src/main/resources/runtime/runtime/dialog/settings/settings.js
src/main/resources/runtime/runtime/dialog/settings/settings.fxml
src/main/resources/runtime/runtime/dialog/update/update.js
```

The `settingsMagic` value is changed because two new fields were added to the binary settings format. The old settings file will be reset once, then saved in the new format.
