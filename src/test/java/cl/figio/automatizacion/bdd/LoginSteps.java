package cl.figio.automatizacion.bdd;

import cl.figio.automatizacion.LoginService;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    private LoginService loginService;
    private boolean resultadoLogin;

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