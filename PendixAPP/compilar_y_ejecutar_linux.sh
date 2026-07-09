#!/usr/bin/env bash
set -e
DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"
mkdir -p out
javac --release 17 -encoding UTF-8 -d out src/PendixAppServer.java
jar cfe PendixApp.jar PendixAppServer -C out .
java -jar PendixApp.jar
