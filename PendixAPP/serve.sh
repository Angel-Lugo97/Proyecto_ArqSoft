#!/usr/bin/env bash
set -Eeuo pipefail

ROOT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT_DIR"

PORT="${PENDIX_PORT:-5018}"
HOST="127.0.0.1"
LOCAL_URL="http://${HOST}:${PORT}"
JAR_PATH="build/libs/PendixAPP-1.0.0.jar"
RUNTIME_DIR="$ROOT_DIR/.serve"
APP_LOG="$RUNTIME_DIR/app.log"
TUNNEL_LOG="$RUNTIME_DIR/cloudflared.log"
APP_PID=""
TUNNEL_PID=""
PUBLIC_URL=""

mkdir -p "$RUNTIME_DIR"
: > "$APP_LOG"
: > "$TUNNEL_LOG"

require_command() {
  local command_name="$1"
  local install_hint="$2"
  if ! command -v "$command_name" >/dev/null 2>&1; then
    echo "ERROR: falta el comando '$command_name'."
    echo "Instálalo con: $install_hint"
    exit 1
  fi
}

require_command java "sudo pacman -S --needed jdk21-openjdk"
require_command curl "sudo pacman -S --needed curl"
require_command cloudflared "sudo pacman -S --needed cloudflared"
require_command sha256sum "sudo pacman -S --needed coreutils"

cleanup() {
  local exit_code=$?
  trap - EXIT INT TERM
  echo
  echo "Deteniendo PendixAPP y el túnel..."

  if [[ -n "${APP_PID:-}" ]] && kill -0 "$APP_PID" 2>/dev/null; then
    kill "$APP_PID" 2>/dev/null || true
    wait "$APP_PID" 2>/dev/null || true
  fi

  if [[ -n "${TUNNEL_PID:-}" ]] && kill -0 "$TUNNEL_PID" 2>/dev/null; then
    kill "$TUNNEL_PID" 2>/dev/null || true
    wait "$TUNNEL_PID" 2>/dev/null || true
  fi

  echo "PendixAPP apagada correctamente."
  exit "$exit_code"
}
trap cleanup EXIT INT TERM

source_fingerprint() {
  {
    find src -type f -print0
    printf '%s\0' build.gradle settings.gradle
  } | sort -z | xargs -0 sha256sum | sha256sum | awk '{print $1}'
}

build_app() {
  echo "Compilando la versión de producción..."
  chmod +x gradlew
  ./gradlew --no-daemon clean jar
  [[ -s "$JAR_PATH" ]] || {
    echo "ERROR: no se generó $JAR_PATH"
    return 1
  }
}

wait_for_local_app() {
  for _ in {1..50}; do
    if curl --silent --fail --max-time 2 "$LOCAL_URL/health" >/dev/null; then
      return 0
    fi
    if [[ -n "${APP_PID:-}" ]] && ! kill -0 "$APP_PID" 2>/dev/null; then
      echo "ERROR: PendixAPP terminó durante el arranque."
      tail -n 30 "$APP_LOG" || true
      return 1
    fi
    sleep 0.2
  done
  echo "ERROR: PendixAPP no respondió en $LOCAL_URL"
  tail -n 30 "$APP_LOG" || true
  return 1
}

start_app() {
  : > "$APP_LOG"
  PENDIX_HOST="$HOST" \
  PENDIX_PORT="$PORT" \
  PENDIX_OPEN_BROWSER=false \
    java -jar "$JAR_PATH" >>"$APP_LOG" 2>&1 &
  APP_PID=$!
  wait_for_local_app
  echo "Aplicación local activa en $LOCAL_URL"
}

stop_app() {
  if [[ -n "${APP_PID:-}" ]] && kill -0 "$APP_PID" 2>/dev/null; then
    kill "$APP_PID" 2>/dev/null || true
    wait "$APP_PID" 2>/dev/null || true
  fi
  APP_PID=""
}

start_tunnel() {
  : > "$TUNNEL_LOG"
  cloudflared tunnel --no-autoupdate --url "$LOCAL_URL" >"$TUNNEL_LOG" 2>&1 &
  TUNNEL_PID=$!

  local public_url=""
  for _ in {1..60}; do
    public_url="$(grep -Eo 'https://[a-zA-Z0-9-]+\.trycloudflare\.com' "$TUNNEL_LOG" | head -n 1 || true)"
    if [[ -n "$public_url" ]]; then
      PUBLIC_URL="$public_url"
      return 0
    fi
    if ! kill -0 "$TUNNEL_PID" 2>/dev/null; then
      echo "ERROR: cloudflared terminó antes de crear la URL." >&2
      tail -n 40 "$TUNNEL_LOG" >&2 || true
      return 1
    fi
    sleep 0.5
  done

  echo "ERROR: no se pudo obtener la URL pública." >&2
  tail -n 40 "$TUNNEL_LOG" >&2 || true
  return 1
}

verify_public_url() {
  local public_url="$1"
  local headers_file="$RUNTIME_DIR/headers.tmp"
  local body_file="$RUNTIME_DIR/body.tmp"

  for _ in {1..40}; do
    if curl --silent --show-error --fail --max-time 8 "$public_url/" >/dev/null 2>&1; then
      break
    fi
    sleep 0.5
  done

  curl --silent --show-error --fail --max-time 15 "$public_url/" >/dev/null
  curl --silent --show-error --fail --max-time 15 "$public_url/health" >/dev/null

  curl --silent --show-error --fail --compressed \
    -H "Accept-Encoding: gzip, br" \
    -D "$headers_file" -o "$body_file" "$public_url/styles.css"
  [[ -s "$body_file" ]] || { echo "ERROR: styles.css llegó vacío."; return 1; }
  grep -qi '^content-type: text/css' "$headers_file" || {
    echo "ERROR: styles.css tiene un Content-Type incorrecto."
    return 1
  }

  curl --silent --show-error --fail --compressed \
    -H "Accept-Encoding: gzip, br" \
    -D "$headers_file" -o "$body_file" "$public_url/app.js"
  [[ -s "$body_file" ]] || { echo "ERROR: app.js llegó vacío."; return 1; }
  grep -qi '^content-type: application/javascript' "$headers_file" || {
    echo "ERROR: app.js tiene un Content-Type incorrecto."
    return 1
  }

  rm -f "$headers_file" "$body_file"
  echo "Verificación pública completada: home, health, CSS y JavaScript responden correctamente."
}

if curl --silent --max-time 1 "$LOCAL_URL/health" >/dev/null 2>&1; then
  echo "ERROR: el puerto $PORT ya está siendo usado por otra instancia de PendixAPP."
  echo "Deténla antes de ejecutar este script."
  exit 1
fi

build_app
start_app
start_tunnel

echo
echo "============================================================"
echo "  PENDIXAPP ESTÁ PUBLICADA"
echo "  $PUBLIC_URL"
echo "============================================================"
echo

verify_public_url "$PUBLIC_URL"
echo "Los cambios en src/ reiniciarán la app sin cambiar esta URL."
echo "Los navegadores abiertos se recargarán al detectar la nueva versión."
echo "Presiona Ctrl+C para apagar la app y el túnel."

LAST_FINGERPRINT="$(source_fingerprint)"
while true; do
  sleep 2
  CURRENT_FINGERPRINT="$(source_fingerprint)"
  if [[ "$CURRENT_FINGERPRINT" == "$LAST_FINGERPRINT" ]]; then
    continue
  fi

  echo
  echo "Cambio detectado. Preparando una nueva versión..."
  if build_app; then
    stop_app
    start_app
    LAST_FINGERPRINT="$CURRENT_FINGERPRINT"
    echo "Nueva versión activa; el túnel continúa en $PUBLIC_URL"
  else
    echo "La compilación falló. La versión anterior continúa activa."
    LAST_FINGERPRINT="$CURRENT_FINGERPRINT"
  fi
done
