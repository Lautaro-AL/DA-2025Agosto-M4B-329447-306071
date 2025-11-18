package ort.dda.obl.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ort.dda.obl.UsuarioException;
import ort.dda.obl.modelo.Administrador;
import ort.dda.obl.modelo.Fachada;
import ort.dda.obl.modelo.Propietario;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/acceso")
public class ControladorLogin {

    @PostMapping("/loginPropietario")
    public List<Respuesta> loginPropetario(HttpSession sesionHttp, @RequestParam String cedula,
            @RequestParam String password) throws UsuarioException {

        Propietario sesion;
        try {
            sesion = Fachada.getInstancia().loginPropetario(cedula, password);
        } catch (UsuarioException e) {
            return Respuesta.lista(new Respuesta("error", e.getMessage()));
        }

        // Si hay una sesión activa, la cierro
        logoutAdmin(sesionHttp);
        // Guardo la sesión del propietario
        sesionHttp.setAttribute("usuarioPropietario", sesion);
        // Devuelvo la respuesta de login exitoso
        return Respuesta.lista(new Respuesta("loginExitoso", "tablero.html"));
    }

    @PostMapping("/loginAdmin")
    public List<Respuesta> loginAdministrador(HttpSession sesionHttp,
            @RequestParam String cedula, @RequestParam String password) throws UsuarioException {
        // login al modelo
        Administrador admin;
        try {
            admin = Fachada.getInstancia().loginAdministrador(cedula, password);
        } catch (UsuarioException e) {
            return Respuesta.lista(new Respuesta("error", e.getMessage()));
        }

        // guardo el admin en la sesionHttp
        sesionHttp.setAttribute("usuarioAdmin", admin);
        return Respuesta.lista(new Respuesta("loginExitoso", "menu-admin.html")); // completar html admin
    }

    @PostMapping("/logout") // SOlo admin
    public List<Respuesta> logoutAdmin(HttpSession sesionHttp) throws UsuarioException {
        Administrador sesion = (Administrador) sesionHttp.getAttribute("usuarioAdmin");
        if (sesion != null) {
            sesionHttp.removeAttribute("usuarioAdmin");
        }
        return Respuesta.lista(new Respuesta("paginaLogin", "login-admin.html"));

    }
}
