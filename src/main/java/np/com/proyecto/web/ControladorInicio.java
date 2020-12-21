package np.com.proyecto.web;

import lombok.extern.slf4j.Slf4j;
import np.com.proyecto.domain.Usuario;
import np.com.proyecto.servicio.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String inicio(Usuario usuario, Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        return "index";
    }

    /**
     * Login form with error.
     *
     * @param model
     * @return
     */
    @RequestMapping("/login-error.html")
    public String loginError(Model model
    ) {
        model.addAttribute("loginError", true);
        return "index";
    }

    /**
     * Sesion finalizada correctamente
     *
     * @param model
     * @return
     */
    @RequestMapping("/login-logout.html")
    public String loginLogout(Model model) {
        model.addAttribute("loginLogout", true);
        return "redirect:/";
    }

    @RequestMapping("/mensajeEnviado")
    public String mensajeEnviado(Model model, Usuario usuario, @AuthenticationPrincipal User user) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("mensajeEnviado", true);
        return "index";
    }

    @RequestMapping("/reporteEnviado")
    public String reporteEnviado(Model model, Usuario usuario, @AuthenticationPrincipal User user) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("reporteEnviado", true);
        return "index";
    }

    @RequestMapping("/expired")
    public String sesionExpirada(Usuario usuario, Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("sesionExpirada", true);
        return "index";
    }

}
