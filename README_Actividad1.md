# Actividad 1

Proyecto desarrollado para la evaluación de la Unidad 2 del ramo Automatización de Pruebas.

El objetivo principal es implementar un flujo básico de automatización de pruebas en Java, utilizando control de versiones, Maven, pruebas unitarias e integración continua mediante GitHub Actions.

---

## 1. Tecnologías utilizadas

Para el desarrollo del proyecto se utilizaron las siguientes herramientas:

- Java 17
- Apache Maven 3.9.16
- JUnit 5
- Git
- GitHub
- GitHub Actions
- IntelliJ IDEA
- Maven Surefire
- Maven Surefire Report Plugin

---

## 2. Estructura del proyecto

El proyecto utiliza la estructura estándar de Maven:

```text
pruebaUnidad2_automatizacionPruebas/
│
├── .github/
│   └── workflows/
│       └── ci.yml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── cl/
│   │   │       └── figio/
│   │   │           └── automatizacion/
│   │   │               └── Calculator.java
│   │   │
│   │   └── resources/
│   │
│   └── test/
│       └── java/
│           └── cl/
│               └── figio/
│                   └── automatizacion/
│                       └── CalculatorTest.java
│
├── .gitignore
├── pom.xml
└── README.md
└── README_Actividad1.md
```
Se utilizó cl.figio.automatizacion como package base para mantener una estructura organizada y seguir la convención de nombres utilizada habitualmente en proyectos Java.

---
## 3. Control de versiones con Git

El repositorio fue inicializado manualmente utilizando Git.

Comandos utilizados:
```text
git init
git branch -M main
```
Para comprobar el estado inicial del repositorio:
```text
git status
```
Algunos de los commits realizados fueron:
```text
Inicializa proyecto Maven y configuración base
Agrega JUnit para las pruebas unitarias
Agrega operaciones básicas de la calculadora
Agrega pruebas unitarias de suma y resta
Integra pruebas unitarias a la rama principal
Configura pipeline para ejecutar pruebas automáticamente
Agrega reporte HTML con resultados de las pruebas
```
La idea fue realizar cambios pequeños y frecuentes, permitiendo mantener un historial claro de lo desarrollado.

---
## 4. Configuración del proyecto Maven

El proyecto fue configurado utilizando Maven para administrar las dependencias y automatizar la compilación y ejecución de las pruebas.

En el archivo pom.xml se configuró Java 17:
```XML
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <junit.version>5.11.0</junit.version>
</properties>
```

También se agregó JUnit 5 como dependencia de pruebas:
```XML
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>${junit.version}</version>
    <scope>test</scope>
</dependency>
```

Para ejecutar las pruebas mediante Maven se configuró Maven Surefire:
```XML
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.5.0</version>
</plugin>
```
---
## 5. Pruebas unitarias

Para demostrar la ejecución de pruebas unitarias se creó una clase simple llamada Calculator.

Esta clase contiene dos operaciones independientes:
```java
public int sumar(int a, int b) {
        return a + b;
        }

        public int restar(int a, int b) {
        return a - b;
        }
```
Para validar estas operaciones se implementaron dos pruebas unitarias utilizando JUnit 5.

Prueba de suma:
```java
@Test
void debeSumarDosNumerosCorrectamente() {
    Calculator calculator = new Calculator();

    int resultado = calculator.sumar(5, 3);

    assertEquals(8, resultado);
}
```

Prueba de resta:
```java
@Test
void debeRestarDosNumerosCorrectamente() {
    Calculator calculator = new Calculator();

    int resultado = calculator.restar(10, 4);

    assertEquals(6, resultado);
}
```
Cada prueba valida una operación específica y no depende del resultado o estado generado por la otra. Esto permite ejecutarlas individualmente, en cualquier orden o como parte de la suite completa, manteniendo siempre el mismo resultado.

---
## 6. Ejecución local de pruebas
Las pruebas pueden ejecutarse desde Maven utilizando:

```text
mvn clean test
```
Resultado Obtenido:
```text
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```
Esto confirma que ambas pruebas se ejecutan correctamente en el entorno local

---
## 7. Archivo .gitignore

Se configuró un archivo .gitignore para evitar versionar archivos generados automáticamente o configuraciones específicas de los IDE utilizados.

Entre los elementos ignorados se encuentran:
```gitignore
target/
.idea/
*.iml
.vscode/
.DS_Store
```
De esta forma el repositorio mantiene solamente los archivos necesarios para construir y ejecutar el proyecto.

---
## 8. Repositorio Remoto

El proyecto fue publicado en GitHub y la rama main quedó asociada al repositorio remoto.

Para configurar el repositorio se utilizó:
```text
git remote add origin https://github.com/FelipeGCM/pruebaUnidad2_automatizacionPruebas.git
```

Posteriormente se realizó el primer push:
```text
git push -u origin main
```

El desarrollo posterior se realizó utilizando ramas y Pull Requests para mantener los cambios separados antes de integrarlos a main.

---
## 9. Pipeline de Integración Continua

Se configuró GitHub Actions mediante el archivo:
```text
.github/workflows/ci.yml
```

El pipeline se ejecuta automáticamente ante:
- Pull Requests hacia main
- Push realizados sobre main

El flujo implementado realiza las siguientes tareas:
- Descarga el código del repositorio.
- Configura Java 17.
- Compila el proyecto.
- Ejecuta las pruebas unitarias.
- Genera un reporte HTML.
- Publica los resultados como artifact.

Configuración principal:

```yaml
name: Integración Continua

on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main

jobs:
  pruebas:
    runs-on: ubuntu-latest

    steps:
      - name: Descargar código del repositorio
        uses: actions/checkout@v4

      - name: Configurar Java 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven

      - name: Compilar proyecto
        run: mvn clean compile

      - name: Ejecutar pruebas unitarias
        run: mvn test

      - name: Generar reporte HTML
        if: always()
        run: mvn site

      - name: Guardar reporte de pruebas
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: reporte-pruebas-unitarias
          path: |
            target/site/
            target/surefire-reports/
```

---
## 10. Reporte navegable de pruebas

Para generar un reporte HTML de los resultados se agregó Maven Surefire Report Plugin al archivo pom.xml.
```XML
<reporting>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-report-plugin</artifactId>
            <version>3.5.0</version>
        </plugin>
    </plugins>
</reporting>
```

El resultado queda disponible en:
```text
target/site/surefire.html
```

El reporte muestra información como:
- cantidad de pruebas ejecutadas.
- errores.
- fallos.
- pruebas omitidas.
- porcentaje de éxito.
- tiempo de ejecución.
- clases ejecutadas.
- detalle de cada caso de prueba.

En la ejecución realizada se obtuvo:
```text
Tests: 2
Errors: 0
Failures: 0
Skipped: 0
Success Rate: 100%
```

El mismo reporte es generado automáticamente por GitHub Actions y publicado como un artifact llamado:
```text
reporte-pruebas-unitarias
```
