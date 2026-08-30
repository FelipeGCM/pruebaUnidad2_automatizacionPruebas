# Actividad 2

## 1. Sesión Three Amigos

Para definir la funcionalidad de autenticación se simuló una sesión Three Amigos, considerando la participación de los siguientes roles:

### Product Owner / Negocio

Su responsabilidad es definir el comportamiento esperado desde el punto de vista del usuario y del negocio.

Se definió como objetivo que solamente los usuarios registrados con credenciales válidas puedan acceder al sistema.

### QA

Su responsabilidad es revisar que los criterios de aceptación sean claros, verificables y que contemplen tanto casos exitosos como alternativos.

Durante la sesión se propusieron los siguientes ejemplos:
- Credenciales válidas.
- Contraseña incorrecta.
- Usuario inexistente.

### Desarrollador

Su responsabilidad es revisar la viabilidad técnica de los criterios definidos y aclarar cómo será implementada la funcionalidad.

Se acordó implementar una clase simple de autenticación que permita validar un usuario y contraseña previamente registrados.

### Criterios de aceptación

- Un usuario registrado con contraseña correcta puede iniciar sesión.
- Una contraseña incorrecta debe impedir el acceso.
- Un usuario inexistente no debe poder autenticarse.
- La validación debe entregar un resultado claro que pueda ser comprobado mediante pruebas automatizadas.

### Ejemplos revisados

| Usuario | Contraseña | Resultado esperado |
|---|---|---|
| felipe | 1234 | Acceso permitido |
| felipe | incorrecta | Acceso rechazado |
| inexistente | 1234 | Acceso rechazado |

---
## 2. Creación de escenarios con los criterios solicitados

Se crean los escenarios en Gherkin contemplando pruebas con credenciales válidas, contraseña incorrecta y usuario inexistente.

Se utiliza el Scenario Outline y Examples.

```gherkin
# language: es

Característica: Inicio de sesión de usuarios

  Como usuario registrado
  Quiero iniciar sesión con mis credenciales
  Para poder acceder al sistema

  Escenario: Inicio de sesión con credenciales válidas
    Dado que existe un usuario "felipe" con contraseña "1234"
    Cuando intenta iniciar sesión con usuario "felipe" y contraseña "1234"
    Entonces el acceso debe ser permitido

  Esquema del escenario: Inicio de sesión con credenciales inválidas
    Dado que existe un usuario "felipe" con contraseña "1234"
    Cuando intenta iniciar sesión con usuario "<usuario>" y contraseña "<contrasena>"
    Entonces el acceso debe ser rechazado

    Ejemplos:
      | usuario     | contrasena |
      | felipe      | incorrecta |
      | inexistente | 1234       |
```

---
## 3. Implementación de steps en Java + Cucumber

Se implementan los Step Definitions en Java utilizando Cucumber para asociar cada paso definido en Gherkin con la lógica de prueba correspondiente:

```Java
{
    @Dado("que existe un usuario {string} con contraseña {string}")
    public void existeUsuarioRegistrado(String usuario, String contrasena) {
        loginService = new LoginService(usuario, contrasena);
    }
    
    @Cuando("intenta iniciar sesión con usuario {string} y contraseña {string}")
    public void intentaIniciarSesion(String usuario, String contrasena) {
        resultadoLogin = loginService.iniciarSesion(usuario, contrasena);
    }
    
    @Entonces("el acceso debe ser permitido")
    public void validarAccesoPermitido() {
        assertTrue(resultadoLogin);
    }
    
    @Entonces("el acceso debe ser rechazado")
    public void validarAccesoRechazado() {
        assertFalse(resultadoLogin);
    }
}
```
---
## 4. Integración de escenarios BDD en Pipeline CI

Los escenarios BDD fueron integrados al pipeline de integración continua utilizando GitHub Actions.

El pipeline ejecuta las pruebas unitarias y los escenarios BDD mediante Maven:

```yaml
- name: Ejecutar pruebas unitarias y BDD
  run: mvn test
```
Además, el reporte generado por Cucumber se publica como artifact para poder ser descargado y revisado posteriormente:

```yaml
- name: Guardar reporte BDD
  if: always()
  uses: actions/upload-artifact@v4
  with:
    name: reporte-bdd-cucumber
    path: target/cucumber-report.html
    if-no-files-found: warn
```
Durante la ejecución realizada en GitHub Actions se obtuvieron los tres escenarios definidos con resultado exitoso.

---
## 5. Reporte navegable de pruebas BDD

Para facilitar la revisión de los resultados se configuró Cucumber para generar un reporte HTML.

La configuración utilizada en la suite de Cucumber fue:

```Java
@ConfigurationParameter(
key = PLUGIN_PROPERTY_NAME,
value = "pretty, html:target/cucumber-report.html"
)
```
El reporte queda disponible localmente en:

```text
target/cucumber-report.html
```

En la ejecución realizada se obtuvo:

```text
3 escenarios ejecutados
3 escenarios aprobados
100% passed
```

El reporte muestra el detalle de la característica, los escenarios, los pasos Dado/Cuando/Entonces y los valores utilizados en el esquema del escenario.

El mismo reporte es generado automáticamente en GitHub Actions y publicado como artifact con el nombre:
```text
reporte-bdd-cucumber
```
---
## 6. Prueba de performance con JMeter

Para complementar las pruebas funcionales se implementó una prueba básica de performance utilizando Apache JMeter 5.6.3.

Se creó un servidor HTTP simple para exponer la funcionalidad de inicio de sesión mediante:
```text
POST http://localhost:8080/login
```

La prueba fue configurada con los siguientes parámetros:
```text
Usuarios concurrentes: 20
Ramp-up: 5 segundos
Iteraciones por usuario: 10
Total de solicitudes: 200
```

La solicitud utilizada corresponde a un inicio de sesión válido:
```Json
{
  "usuario": "felipe",
  "contrasena": "1234"
}
```

Se agregaron las siguientes validaciones:
- Código de respuesta HTTP igual a 200.
- Tiempo máximo de respuesta de 500 ms.

El plan de pruebas quedó almacenado en:
```text
performance/login-performance-test.jmx
```

La ejecución de la prueba de carga se realizó por línea de comandos:
```text
jmeter -n -t performance/login-performance-test.jmx -l performance/results/resultados.jtl -e -o performance/report
```

En la ejecución local se obtuvieron los siguientes resultados:

| Métrica              |                   Resultado |
| ---------------------- | --------------------------: |
| Solicitudes ejecutadas |                         200 |
| Errores                |                           0 |
| Error %                |                       0,00% |
| Tiempo promedio        |                     0,48 ms |
| Tiempo máximo          |                       19 ms |
| Percentil 90           |                     1,00 ms |
| Percentil 95           |                     1,00 ms |
| Percentil 99           |                     2,99 ms |
| Throughput             | 42,58 transacciones/segundo |
| APDEX                  |                       1,000 |

Los resultados muestran que todas las solicitudes fueron procesadas correctamente y sin superar el umbral de tiempo configurado.

---
## 7. Métricas y dashboard de performance

JMeter genera un dashboard HTML navegable a partir de los resultados de la ejecución.

El dashboard permite revisar indicadores como:

- Cantidad total de solicitudes.
- Porcentaje de errores.
- Tiempo promedio de respuesta.
- Tiempos mínimo y máximo.
- Percentiles 90, 95 y 99.
- Throughput.
- APDEX.
- Resumen de solicitudes exitosas y fallidas.

El reporte se genera en:
```text
performance/report/index.html
```

Para evitar versionar archivos generados automáticamente, las carpetas de resultados fueron agregadas al archivo .gitignore:
```gitignore
### JMeter ###
jmeter.log
performance/results/
performance/report/
```

El plan .jmx sí se mantiene versionado, ya que forma parte de la configuración de la prueba.

---
## 8. Integración de performance y alertas automáticas

La prueba de performance también fue integrada al pipeline de GitHub Actions.

Durante la ejecución del pipeline se realizan las siguientes tareas:

```text
- Instalar JMeter
- Iniciar servidor HTTP
- Ejecutar prueba de performance
- Validar resultados
- Generar dashboard
- Publicar artifact
```

Para detectar errores o degradaciones se configuraron dos criterios dentro de JMeter:

```text
- Response Assertion: código HTTP 200
- Duration Assertion: máximo 500 ms
```

Posteriormente, el pipeline revisa los resultados generados por JMeter:

```yaml
- name: Validar resultados de performance
  shell: bash
  run: |
    if grep -q "false" performance/results/resultados.jtl; then
      echo "::error::Se detectaron errores o degradaciones en la prueba de performance"
      exit 1
    fi

    echo "Prueba de performance completada sin degradaciones"
```

Si alguna solicitud incumple los criterios definidos, JMeter la marca como fallida y el pipeline termina con error.

Esto permite simular una alerta automática dentro del flujo de CI, ya que ante una degradación o falla detectada la ejecución del pipeline queda marcada como fallida.

El reporte de performance se publica como artifact con el nombre:

```text
reporte-performance-jmeter
```

Al finalizar el pipeline se generan tres artifacts:

```text
reporte-pruebas-unitarias
reporte-bdd-cucumber
reporte-performance-jmeter
```

De esta forma se centralizan los resultados de las pruebas unitarias, BDD y performance dentro del mismo flujo de integración continua.

---
