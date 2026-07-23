#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"
chmod +x gradlew
./gradlew clean build
java -jar build/libs/PendixAPP-1.0.0.jar
