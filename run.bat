@echo off
setlocal
set JDK=C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot\bin
set JAR=dist\PokePark.jar

if not exist "%JAR%" (
  echo JAR introuvable: %JAR%
  echo Lance d'abord: .\build.bat
  exit /b 1
)

if exist "%JDK%\java.exe" (
  "%JDK%\java.exe" -jar "%JAR%"
  exit /b %ERRORLEVEL%
)

where java >nul 2>&1
if errorlevel 1 (
  echo Java introuvable. Installe un JDK 17+.
  exit /b 1
)

for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do (
  set JAVA_VER=%%v
  goto :check
)

:check
echo %JAVA_VER% | findstr /r "\"1[7-9]\. \"2[0-9]\." >nul
if errorlevel 1 (
  echo.
  echo Ton "java" par defaut est trop vieux ^(besoin de Java 17+^).
  echo Version actuelle:
  java -version
  echo.
  echo Solutions:
  echo   1. Lance: .\run.bat  ^(apres avoir installe JDK 17 et adapte le chemin dans run.bat^)
  echo   2. Ou utilise le chemin complet du JDK 17:
  echo      "C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot\bin\java.exe" -jar dist\PokePark.jar
  exit /b 1
)

java -jar "%JAR%"
endlocal
