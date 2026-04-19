# GitHub Copilot Skills Lab

Este laboratorio es una copia del proyecto Java de `lab-05-agents` y está diseñado para enseñarte cómo usar GitHub Copilot Skills en lugar de Custom Prompt Files.

## Pre-requisitos

- [Visual Studio Code](https://code.visualstudio.com/) o cualquier editor compatible con GitHub Copilot.
- Extensiones de GitHub Copilot instaladas.
- Java 17 o superior.
- Maven.

## Estructura del laboratorio

- El proyecto base es el mismo que `lab-05-agents/java`.
- Las definiciones de prompts originales se encuentran en `.github/prompts/`.
- El enfoque de este laboratorio es crear y usar `skills` en `.github/skills/`.

## Cómo usar este laboratorio

1. Abre el proyecto `lab-07-skills/java` en VS Code.
2. Abre Copilot Chat.
3. Usa los siguientes skills directamente con slash commands:
   - `/explain-java`
   - `/review-java`
   - `/generate-tests-java`
4. Si quieres ver el código del skill, abre los archivos en `.github/skills/`.

## Qué está incluido

- `.github/prompts/` contiene las versiones originales de los Custom Prompt Files del lab 5.
- `.github/skills/` contiene las versiones adaptadas como skills, con las mismas capacidades de explicar código, revisar código y generar pruebas.
- El proyecto Java completo de `lab-05-agents/java` está copiado aquí, para que puedas trabajar con la misma aplicación y los mismos ejemplos.

## Desafíos

### 1. Ejecutar los skills

Ejecuta los siguientes comandos en Copilot Chat:

- `/explain-java`
- `/review-java`
- `/generate-tests-java`

Usa como entrada el código Java de cualquier archivo del proyecto, por ejemplo `EmployeeController.java` o `EmployeeService.java`.

### 2. Comparar prompt vs skill

- Abre `.github/prompts/explain-java-gabriel.prompt.md` y `.github/skills/explain-java.skill.md`.
- Observa cómo la lógica y las preguntas son las mismas, pero el directorio y el nombre del archivo reflejan la idea de `skills`.

### 3. Crear un nuevo skill

Crea un nuevo archivo en `.github/skills/` con el siguiente nombre:

- `fix-java-style.skill.md`

Y usa este contenido base:

```markdown
---
description: "Detecta problemas de estilo y sugiere mejoras en código Java"
name: "Fix Java Style Skill"
---

Revisa el siguiente código Java y sugiere mejoras de estilo, formato y convenciones.

☕ **Code to review**:
${input:code:Paste your Java code here}

Responde con:
- Problemas de estilo detectados
- Cambios recomendados
- Ejemplos de código mejorado
```

Después, en Copilot Chat, ejecuta `/fix-java-style`.

### 4. Usar el mismo flujo del lab 5 con skills

Este laboratorio reproduce el flujo de `lab-05-agents` usando skills en lugar de custom prompts. El objetivo es que puedas usar el mismo proyecto y la misma experiencia, pero con la forma recomendada de trabajar con GitHub Copilot Skills.

## Resultado esperado

- Saber ejecutar `/explain-java`, `/review-java` y `/generate-tests-java`.
- Entender la diferencia entre `.github/prompts/` y `.github/skills/`.
- Saber crear un nuevo skill basado en un prompt existente.
