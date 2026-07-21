@echo off
setlocal EnableExtensions

if "%~1"=="" (
    set "PROJECT=%CD%"
) else (
    set "PROJECT=%~1"
)
set "PATCH=%~dp0"

if not exist "%PROJECT%\src\main\resources\runtime\runtime\init.js" (
    echo [ERROR] Project was not found at:
    echo %PROJECT%
    echo.
    echo Usage:
    echo install_offline_patch.bat "C:\path\to\zonecraftlauncher"
    exit /b 1
)

set "RUNTIME=%PROJECT%\src\main\resources\runtime\runtime"
set "JAVA=%PROJECT%\src\main\java\launcher\client"

for %%F in (
    "%RUNTIME%\config.js"
    "%RUNTIME%\dialog\auth\auth.js"
    "%RUNTIME%\dialog\servers\servers.js"
    "%RUNTIME%\dialog\settings\settings.js"
    "%RUNTIME%\dialog\settings\settings.fxml"
    "%RUNTIME%\dialog\update\update.js"
) do (
    if exist "%%~F" copy /Y "%%~F" "%%~F.offline_backup" >nul
)

if not exist "%JAVA%" mkdir "%JAVA%"

xcopy /E /I /Y "%PATCH%src" "%PROJECT%\src" >nul
if errorlevel 1 (
    echo [ERROR] Failed to copy patch files.
    exit /b 1
)

echo [OK] Offline-mode source files installed.
echo.

pushd "%PROJECT%"
if exist "build_and_embed_runtime_v5.bat" (
    echo Building with build_and_embed_runtime_v5.bat...
    call build_and_embed_runtime_v5.bat
) else (
    echo build_and_embed_runtime_v5.bat was not found.
    echo Running a normal Gradle build instead...
    call gradlew clean build
)
set "RESULT=%ERRORLEVEL%"
popd

if not "%RESULT%"=="0" (
    echo.
    echo [ERROR] Build failed with code %RESULT%.
    exit /b %RESULT%
)

echo.
echo ============================================================
echo OFFLINE SETTINGS PATCH INSTALLED SUCCESSFULLY
echo ============================================================
echo Open launcher Settings, enable Offline mode and enter a nick.
echo Offline launch uses only client files already present in the updates folder.
exit /b 0
