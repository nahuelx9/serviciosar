package np.com.proyecto.web;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import np.com.proyecto.domain.Usuario;
import np.com.proyecto.servicio.UsuarioService;
import np.com.proyecto.util.Departamento;
import np.com.proyecto.util.Provincia;
import np.com.proyecto.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class ControladorUsuario {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/crearCuenta")
    public String agregarUsuario(Model model, Usuario usuario, Provincia provincia, Departamento departamento) {
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Departamento> localidadesAmba = departamento.listarLocalidadesAmba();
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("localidadesAmba", localidadesAmba);
        return "registroUsuario";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@Valid Usuario usuario, BindingResult result, Model model, @RequestParam("password2") String password2, Provincia provincia, Departamento departamento) {
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Departamento> localidadesAmba = departamento.listarLocalidadesAmba();
        if (result.hasErrors()) {
            model.addAttribute("provincias", provincias);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("localidadesAmba", localidadesAmba);
            return "registroUsuario";
        }
        if (usuarioService.verificarExistenciaEmail(usuario.getUsername()) == false) {
            result.rejectValue("username", "error.user", "Ya existe una cuenta con ese email");
            model.addAttribute("provincias", provincias);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("localidadesAmba", localidadesAmba);
            return "registroUsuario";
        }
        if (!usuario.getPassword().equals(password2)) {
            result.rejectValue("password", "error.user", "Las contraseñas no coinciden");
            model.addAttribute("provincias", provincias);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("localidadesAmba", localidadesAmba);
            return "registroUsuario";
        }

        usuarioService.guardar(usuario);
        return "redirect:/new-user.html";
    }

    /**
     * ModificarDatosUsuario
     *
     * @param model
     * @param usuario
     * @param provincia
     * @param departamento
     * @param user
     * @return
     */
    @GetMapping("/datosUsuario/{idUsuario}")
    public String editarUsuario(Model model, Usuario usuario, Provincia provincia, Departamento departamento, @AuthenticationPrincipal User user) {
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        usuario = usuarioService.encontrarUsuario(usuario);
        if (usuarioLogueado.equals(usuario)) {
            List<Provincia> provincias = provincia.listarProvincia();
            List<Departamento> departamentos = departamento.listarDepartamento();
            List<Departamento> localidadesAmba = departamento.listarLocalidadesAmba();
            model.addAttribute("provincias", provincias);
            model.addAttribute("departamentos", departamentos);
            model.addAttribute("localidadesAmba", localidadesAmba);
            model.addAttribute("usuario", usuario);
            return "datosUsuario";
        } else {
            return "error";
        }
    }

    @PostMapping("/modificarNombreUsuario")
    public String modificarNombreUsuario(@RequestParam("nombre") String nombre, @AuthenticationPrincipal User user) {
        Usuario usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        usuarioService.modificarNombreUsuario(nombre, usuario.getIdUsuario());
        return "redirect:/datosModificados";
    }

    @PostMapping("/modificarApellidoUsuario")
    public String modificarApellidoUsuario(@RequestParam("apellido") String apellido, @AuthenticationPrincipal User user) {
        Usuario usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        usuarioService.modificarApellidoUsuario(apellido, usuario.getIdUsuario());
        return "redirect:/datosModificados";
    }

    @PostMapping("/modificarProvinciaDepartamentoUsuario")
    public String modificarProvinciaDepartamentoUsuario(@RequestParam("provincia") String provincia, @RequestParam("departamento") String departamento, @AuthenticationPrincipal User user) {
        Usuario usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        String nombreProvincia = Util.obtenerNombreProvincia(provincia);
        usuarioService.modificarProvinciaDepartamentoUsuario(nombreProvincia, departamento, usuario.getIdUsuario());
        return "redirect:/datosModificados";
    }

    @PostMapping("/modificarEmailUsuario")
    public String modificarEmailUsuario(@RequestParam("email") String email, @AuthenticationPrincipal User user) {
        Usuario usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        if (usuarioService.verificarExistenciaEmail(email) && !email.isEmpty() && Util.ValidarMail(email) == true) {
            usuarioService.modificarUsernameUsuario(email, usuario.getIdUsuario());
            SecurityContextHolder.getContext().setAuthentication(null);
            return "redirect:/datosModificados";
        } else {
            return "redirect:/datosModificadosErrorEmail";
        }
    }

    @PostMapping("/modificarContraseñaUsuario")
    public String modificarContraseñaUsuario(@RequestParam("passwordActual") String passwordActual, @RequestParam("password1") String password1, @RequestParam("password2") String password2, @AuthenticationPrincipal User user) {
        Usuario usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        int error = usuarioService.modificarPasswordUsuario(passwordActual, password1, password2, usuario);
        if (error == 1 || error == 3) {
            return "redirect:/passwordCamposError";
        }
        if (error == 2) {
            return "redirect:/passwordInvalida";
        }
        return "redirect:/datosModificados";
    }

    /**
     * Usuario creado correctamente
     *
     * @param model
     * @return
     */
    @RequestMapping("/new-user.html")
    public String newUser(Model model
    ) {
        model.addAttribute("newUser", true);
        return "index";
    }

    /**
     * Datos del usuario modificados
     *
     * @param usuario
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/datosModificados")
    public String datosModificados(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        model.addAttribute("datosModificados", true);
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        return "index";
    }

    @RequestMapping("/datosModificadosErrorEmail")
    public String datosModificadosErrorEmail(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("datosModificadosErrorEmail", true);
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        return "index";
    }

    @RequestMapping("/passwordCamposError")
    public String contraseñaCamposError(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("passwordCamposError", true);
        return "index";
    }

    @RequestMapping("/passwordInvalida")
    public String contraseñaInvalida(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("passwordInvalida", true);
        return "index";
    }

}
