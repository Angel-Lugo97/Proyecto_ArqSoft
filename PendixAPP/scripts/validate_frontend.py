from pathlib import Path
import re
import sys


INDEX_PATH = Path(
    "src/main/resources/public/index.html"
)

OUTPUT_PATH = Path(
    "build/frontend-validation/app.js"
)

REQUIRED_FRAGMENTS = {
    "placeholder de la API":
        "__PENDIX_API_URL__",

    "endpoint de pendientes":
        "/api/pendientes",

    "uso de fetch":
        "fetch(",

    "petición POST":
        "method:'POST'",

    "petición PUT":
        "method:'PUT'",

    "petición DELETE":
        "method:'DELETE'",

    "carga inicial":
        "cargarTareas();",
}

LEGACY_SOURCE = (
    "let tareas=JSON.parse("
    "localStorage.getItem("
    "'pendixapp_tareas_java5018'"
)


def fail(message: str) -> None:
    print(
        f"[ERROR] {message}",
        file=sys.stderr
    )

    raise SystemExit(1)


def main() -> None:
    if not INDEX_PATH.is_file():
        fail(
            f"No se encontró el frontend: "
            f"{INDEX_PATH}"
        )

    html = INDEX_PATH.read_text(
        encoding="utf-8"
    )

    if not html.strip():
        fail(
            "index.html está vacío"
        )

    missing = [
        name
        for name, fragment
        in REQUIRED_FRAGMENTS.items()
        if fragment not in html
    ]

    if missing:
        fail(
            "Faltan elementos requeridos: "
            + ", ".join(missing)
        )

    if LEGACY_SOURCE in html:
        fail(
            "El frontend todavía usa localStorage "
            "como fuente principal de pendientes"
        )

    script_blocks = re.findall(
        r"<script(?:\s[^>]*)?>(.*?)</script>",
        html,
        flags=re.IGNORECASE | re.DOTALL
    )

    if not script_blocks:
        fail(
            "No se encontró ningún bloque JavaScript"
        )

    main_script = script_blocks[-1].strip()

    if not main_script:
        fail(
            "El bloque JavaScript principal está vacío"
        )

    OUTPUT_PATH.parent.mkdir(
        parents=True,
        exist_ok=True
    )

    OUTPUT_PATH.write_text(
        main_script + "\n",
        encoding="utf-8"
    )

    print(
        "[OK] Frontend encontrado"
    )

    print(
        "[OK] Integración con la API detectada"
    )

    print(
        "[OK] Métodos POST, PUT y DELETE detectados"
    )

    print(
        "[OK] localStorage no es la fuente "
        "principal de pendientes"
    )

    print(
        f"[OK] JavaScript extraído en "
        f"{OUTPUT_PATH}"
    )


if __name__ == "__main__":
    main()
