@echo off
setlocal

set JDK=C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot\bin
set VERSION=1.1.0
set INPUT=package-input
set OUTPUT=release
set APP_DIR=%OUTPUT%\PokePark
set ARCHIVE=%OUTPUT%\PokePark-Windows-x64-v%VERSION%.zip

if not exist "%JDK%\jpackage.exe" (
  echo jpackage introuvable: %JDK%\jpackage.exe
  echo Installe un JDK 17+ avec jpackage ou adapte la variable JDK.
  exit /b 1
)

echo [1/4] Construction du JAR...
call build.bat
if errorlevel 1 exit /b 1

echo [2/4] Preparation du package...
rmdir /s /q "%INPUT%" 2>nul
rmdir /s /q "%APP_DIR%" 2>nul
del /q "%ARCHIVE%" 2>nul
mkdir "%INPUT%"
mkdir "%OUTPUT%" 2>nul
copy /Y "dist\PokePark.jar" "%INPUT%\PokePark.jar" >nul

echo [3/4] Creation de l'application Windows autonome...
"%JDK%\jpackage.exe" ^
  --type app-image ^
  --name PokePark ^
  --app-version %VERSION% ^
  --vendor "Romain Boiret" ^
  --description "Pokemon park management game" ^
  --input "%INPUT%" ^
  --main-jar "PokePark.jar" ^
  --main-class "PokeParkApp" ^
  --dest "%OUTPUT%"
if errorlevel 1 exit /b 1

echo [4/4] Creation de l'archive de release...
powershell -NoProfile -ExecutionPolicy Bypass -Command ^
  "Compress-Archive -Path '%APP_DIR%' -DestinationPath '%ARCHIVE%' -CompressionLevel Optimal -Force"
if errorlevel 1 exit /b 1

rmdir /s /q "%INPUT%" 2>nul

echo.
echo OK: %ARCHIVE%
echo.
echo Les joueurs Windows doivent:
echo   1. Extraire le ZIP
echo   2. Ouvrir PokePark\PokePark.exe
echo.
echo Aucune installation de Java n'est necessaire.
endlocal
