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