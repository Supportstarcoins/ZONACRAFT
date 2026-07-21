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

## Installation

Run:

```bat
install_offline_patch.bat "C:\Users\User\Desktop\Meine Projeckte\ZONACRAFT\zonecraftlauncher"
```
