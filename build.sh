#!/usr/bin/env bash
set -euo pipefail

OUT="out"
JAR="dist/PokePark.jar"
JSON_SIMPLE="lib/json-simple-1.1.1.jar"

if ! command -v javac >/dev/null 2>&1; then
  echo "javac was not found. Install JDK 17 or newer." >&2
  exit 1
fi

if [[ ! -f "$JSON_SIMPLE" ]]; then
  echo "Missing dependency: $JSON_SIMPLE" >&2
  exit 1
fi

rm -rf "$OUT"
mkdir -p "$OUT/Pokemon" "$OUT/i18n" dist

echo "[1/5] Compiling sources..."
javac \
  -encoding UTF-8 \
  -cp "$JSON_SIMPLE" \
  -d "$OUT" \
  PokeParkApp.java \
  PokePark.java \
  Pokemon/*.java \
  Player/*.java \
  Shop/*.java \
  MisteryBox/*.java \
  ui/*.java \
  i18n/*.java \
  save/*.java

echo "[2/5] Copying resources..."
cp Pokemon/PokemonList.json "$OUT/Pokemon/PokemonList.json"
cp i18n/Messages*.properties "$OUT/i18n/"

echo "[3/5] Bundling json-simple..."
(
  cd "$OUT"
  jar xf "../$JSON_SIMPLE"
)

echo "[4/5] Creating manifest..."
printf 'Main-Class: PokeParkApp\n\n' > "$OUT/MANIFEST.MF"

echo "[5/5] Creating executable JAR..."
jar cfm "$JAR" "$OUT/MANIFEST.MF" -C "$OUT" .

echo
echo "Built: $JAR"
