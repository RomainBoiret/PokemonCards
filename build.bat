@echo off
setlocal
set JDK=C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot\bin
set OUT=out
set JAR=dist\PokePark.jar

if not exist "%JDK%\javac.exe" (
  echo JDK introuvable: %JDK%
  echo Installe un JDK 17+ ou adapte la variable JDK dans build.bat
  exit /b 1
)

rmdir /s /q "%OUT%" 2>nul
mkdir "%OUT%"
mkdir dist 2>nul

echo Compilation...
"%JDK%\javac.exe" -encoding UTF-8 -cp "lib\json-simple-1.1.1.jar" -d "%OUT%" ^
  PokeParkApp.java PokePark.java ^
  Pokemon\*.java Player\*.java Shop\*.java MisteryBox\*.java ui\*.java i18n\*.java
if errorlevel 1 exit /b 1

echo Copie des ressources...
mkdir "%OUT%\Pokemon" 2>nul
mkdir "%OUT%\i18n" 2>nul
copy /Y "Pokemon\PokemonList.json" "%OUT%\Pokemon\PokemonList.json" >nul
copy /Y "i18n\Messages*.properties" "%OUT%\i18n\" >nul

echo Integration de json-simple...
pushd "%OUT%"
"%JDK%\jar.exe" xf "..\lib\json-simple-1.1.1.jar"
popd

echo Manifest...
(
  echo Main-Class: PokeParkApp
  echo.
) > "%OUT%\MANIFEST.MF"

echo Creation du JAR...
"%JDK%\jar.exe" cfm "%JAR%" "%OUT%\MANIFEST.MF" -C "%OUT%" .
if errorlevel 1 exit /b 1

echo.
echo OK: %JAR%
echo Lancer: java -jar %JAR%
endlocal
