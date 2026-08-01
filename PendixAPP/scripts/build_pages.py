from __future__ import annotations

import json
import os
import shutil
from datetime import datetime, timezone
from pathlib import Path
from urllib.parse import urlparse


SOURCE_DIRECTORY = Path(
    "src/main/resources/public"
)

OUTPUT_DIRECTORY = Path(
    "build/pages"
)

INDEX_FILE = SOURCE_DIRECTORY / "index.html"

API_PLACEHOLDER = "__PENDIX_API_URL__"

DEFAULT_API_URL = (
    "https://api-not-configured.invalid"
)


def fail(message: str) -> None:
    raise SystemExit(
        f"[ERROR] {message}"
    )


def read_api_url() -> str:
    value = os.getenv(
        "PENDIX_API_URL",
        DEFAULT_API_URL
    ).strip()

    if not value:
        value = DEFAULT_API_URL

    return value.rstrip("/")


def validate_api_url(api_url: str) -> None:
    parsed = urlparse(api_url)

    if not parsed.hostname:
        fail(
            "PENDIX_API_URL debe contener un host válido"
        )

    is_https = parsed.scheme == "https"

    is_local_http = (
        parsed.scheme == "http"
        and parsed.hostname
        in {
            "localhost",
            "127.0.0.1",
        }
    )

    if not is_https and not is_local_http:
        fail(
            "PENDIX_API_URL debe usar HTTPS. "
            "HTTP solo se permite para localhost."
        )

    if parsed.query or parsed.fragment:
        fail(
            "PENDIX_API_URL no debe contener "
            "query parameters ni fragmentos"
        )


def escape_javascript_string(
    value: str
) -> str:
    return json.dumps(
        value,
        ensure_ascii=False
    )


def build_site(api_url: str) -> None:
    if not SOURCE_DIRECTORY.is_dir():
        fail(
            f"No existe el directorio "
            f"{SOURCE_DIRECTORY}"
        )

    if not INDEX_FILE.is_file():
        fail(
            f"No existe {INDEX_FILE}"
        )

    source_html = INDEX_FILE.read_text(
        encoding="utf-8"
    )

    placeholder_expression = (
        f"'{API_PLACEHOLDER}'"
    )

    if placeholder_expression not in source_html:
        fail(
            "No se encontró el placeholder "
            f"{API_PLACEHOLDER}"
        )

    if OUTPUT_DIRECTORY.exists():
        shutil.rmtree(
            OUTPUT_DIRECTORY
        )

    shutil.copytree(
        SOURCE_DIRECTORY,
        OUTPUT_DIRECTORY
    )

    output_html = source_html.replace(
        placeholder_expression,
        escape_javascript_string(api_url),
        1
    )

    if API_PLACEHOLDER in output_html:
        fail(
            "El placeholder de la API no fue "
            "reemplazado completamente"
        )

    (
        OUTPUT_DIRECTORY / "index.html"
    ).write_text(
        output_html,
        encoding="utf-8"
    )

    (
        OUTPUT_DIRECTORY / ".nojekyll"
    ).write_text(
        "",
        encoding="utf-8"
    )

    deployment_information = {
        "application": "PendixAPP",
        "commit": os.getenv(
            "GITHUB_SHA",
            "local-build"
        ),
        "apiUrl": api_url,
        "generatedAt": datetime.now(
            timezone.utc
        ).isoformat(),
    }

    (
        OUTPUT_DIRECTORY
        / "deployment-info.json"
    ).write_text(
        json.dumps(
            deployment_information,
            indent=2,
            ensure_ascii=False
        )
        + "\n",
        encoding="utf-8"
    )

    print(
        "[OK] Frontend preparado para GitHub Pages"
    )

    print(
        f"[OK] API configurada: {api_url}"
    )

    print(
        f"[OK] Salida: {OUTPUT_DIRECTORY}"
    )


def main() -> None:
    api_url = read_api_url()

    validate_api_url(
        api_url
    )

    build_site(
        api_url
    )


if __name__ == "__main__":
    main()
