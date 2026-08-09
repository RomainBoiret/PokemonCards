#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:-1.1.0}"
INPUT="package-input"
OUTPUT="release"
APP_NAME="PokePark"
APP_DIR="$OUTPUT/$APP_NAME"
ARCHIVE="$OUTPUT/PokePark-Linux-x64-v${VERSION}.tar.gz"

if [[ "$(uname -s)" != "Linux" ]]; then
  echo "This script must run on Linux. jpackage cannot cross-package Linux apps." >&2
  exit 1
fi

if ! command -v jpackage >/dev/null 2>&1; then
  echo "jpackage was not found. Install JDK 17 or newer." >&2
  exit 1
fi

echo "[1/4] Building the application JAR..."
./build.sh

echo "[2/4] Preparing package input..."
rm -rf "$INPUT" "$APP_DIR" "$ARCHIVE"
mkdir -p "$INPUT" "$OUTPUT"
cp dist/PokePark.jar "$INPUT/PokePark.jar"

echo "[3/4] Creating self-contained Linux application..."
jpackage \
  --type app-image \
  --name "$APP_NAME" \
  --app-version "$VERSION" \
  --vendor "Romain Boiret" \
  --description "Pokemon park management game" \
  --input "$INPUT" \
  --main-jar "PokePark.jar" \
  --main-class "PokeParkApp" \
  --dest "$OUTPUT"

echo "[4/4] Creating release archive..."
tar -C "$OUTPUT" -czf "$ARCHIVE" "$APP_NAME"
rm -rf "$INPUT"

echo
echo "Built: $ARCHIVE"
echo "Players can extract it and launch: PokePark/bin/PokePark"
echo "No Java installation is required."
