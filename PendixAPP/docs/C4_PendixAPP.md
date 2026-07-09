# Modelo C4 — PendixAPP

## C4 Nivel 1 — Contexto

**Para quién es:**  
Este nivel está dirigido a personas no técnicas, docentes o compañeros que necesitan entender de forma general qué es PendixAPP y quién lo utiliza.

**Qué pregunta responde:**  
¿Quién usa el sistema y cuál es el propósito principal de PendixAPP?

```mermaid
flowchart LR
    Usuario["Persona: Usuario<br/>Estudiante o persona que necesita organizar pendientes"]
    PendixAPP["Sistema: PendixAPP<br/>Prototipo web local para gestionar pendientes, recordatorios, calendario, planes e inicio de sesión simulado"]
    Navegador["Navegador web<br/>Medio donde se visualiza la aplicación"]

    Usuario -->|"Registra, consulta, modifica y elimina pendientes"| PendixAPP
    PendixAPP -->|"Se muestra mediante"| Navegador
