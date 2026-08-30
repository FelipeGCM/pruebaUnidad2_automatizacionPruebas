package cl.figio.automatizacion;

public class LoginService {

    private final String usuarioRegistrado;
    private final String contrasenaRegistrada;

    public LoginService(String usuarioRegistrado, String contrasenaRegistrada) {
        this.usuarioRegistrado = usuarioRegistrado;
        this.contrasenaRegistrada = contrasenaRegistrada;
    }

    public boolean iniciarSesion(String usuario, String contrasena) {
        return usuarioRegistrado.equals(usuario)
                && contrasenaRegistrada.equals(contrasena);
    }
}