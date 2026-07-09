#!/usr/bin/env bash
set -e
DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"

if ! command -v java >/dev/null 2>&1; then
  echo "No se encontro Java instalado. Instala Java 17 o superior."
  exit 1
fi

java -jar "$DIR/PendixApp.jar"
