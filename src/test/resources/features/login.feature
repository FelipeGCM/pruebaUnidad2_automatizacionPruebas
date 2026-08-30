# language: es

Característica: Inicio de sesión de usuarios

  Como usuario registrado
  Quiero iniciar sesión con mis credenciales
  Para poder acceder al sistema

  Escenario: Inicio de sesión con credenciales válidas
    Dado que existe un usuario "felipe" con contraseña "1234"
    Cuando intenta iniciar sesión con usuario "felipe" y contraseña "1234"
    Entonces el acceso debe ser permitido

  Escenario: Inicio de sesión con contraseña incorrecta
    Dado que existe un usuario "felipe" con contraseña "1234"
    Cuando intenta iniciar sesión con usuario "felipe" y contraseña "incorrecta"
    Entonces el acceso debe ser rechazado

  Esquema del escenario: Inicio de sesión con usuario inexistente
    Dado que existe un usuario "felipe" con contraseña "1234"
    Cuando intenta iniciar sesión con usuario "<usuario>" y contraseña "<contrasena>"
    Entonces el acceso debe ser rechazado

    Ejemplos:
      | usuario     | contrasena |
      | felipe      | 1234       |
      | felipe      | incorrecta |
      | inexistente | 1234       |