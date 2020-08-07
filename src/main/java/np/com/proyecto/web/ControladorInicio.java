package np.com.proyecto.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import np.com.proyecto.domain.DBFile;
import np.com.proyecto.domain.Servicio;
import np.com.proyecto.domain.Usuario;
import np.com.proyecto.payload.UploadFileResponse;
import np.com.proyecto.servicio.DBFileStorageService;
import np.com.proyecto.servicio.RolService;
import np.com.proyecto.servicio.ServicioService;
import np.com.proyecto.servicio.UsuarioService;
import np.com.proyecto.util.Departamento;
import np.com.proyecto.util.Provincia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.unbescape.html.HtmlEscape;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private ServicioService servicioService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolService rolService;
    @Autowired
    private DBFileStorageService dbFileStorageService;

    @GetMapping("/")
    public String inicio(Usuario usuario, Model model, @AuthenticationPrincipal User user) {
        log.info("Usuario que hizo login:" + user);
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            log.info("id Usuario que hizo login:" + usuario.getIdUsuario());
            Long id = usuario.getIdUsuario();
            model.addAttribute("id", id);
        }
        return "index";
    }

    @GetMapping("/agregarUsuario")
    public String agregarUsuario(Model model, Provincia provincia, Departamento departamento) {
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);
        return "registroUsuario";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@Valid Usuario usuario, Errors errores) {
        if (errores.hasErrors()) {
            return "registroUsuario";
        }
        usuarioService.guardar(usuario);
        return "redirect:/new-user.html";
    }

    @GetMapping("/agregarServicio")
    public String agregarServicio(Servicio servicio) {
        return "registroServicio";
    }

    @PostMapping("/guardarServicio")
    public String guardar(@Valid Servicio servicio, Errors errores, Usuario usuario, @AuthenticationPrincipal User user, @RequestParam("files") MultipartFile[] files) {
        if (errores.hasErrors()) {
            log.info("error = " + errores);
            return "registroServicio";
        }
        usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        Long id = usuario.getIdUsuario();
        int idU = id.intValue();
        servicio.setIdUsuario(idU);
        servicioService.guardar(servicio);
        Long idS = servicio.getIdServicio();
        int idServicio = idS.intValue();
        for (MultipartFile file : files) {
            DBFile dbFile = dbFileStorageService.storeFile(file);
            dbFile.setIdServicio(idServicio);
            if (dbFile.getFileType().equals("image/jpeg") || dbFile.getFileType().equals("image/jpg") || dbFile.getFileType().equals("image/pneg") || dbFile.getFileType().equals("image/png")) {
                dbFileStorageService.guardar(dbFile);
            }
        }
        return "redirect:/";
    }

    @PostMapping("/guardarServicioEditado")
    public String guardarEditado(@Valid Servicio servicio, Errors errores, Usuario usuario, @AuthenticationPrincipal User user) throws UnsupportedEncodingException {
        if (errores.hasErrors()) {
            log.info("error = " + errores);
            return "registroServicio";
        }
        usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        Long id = usuario.getIdUsuario();
        int idU = id.intValue();
        servicio.setIdUsuario(idU);
        Servicio servicioExistente = servicioService.encontrarServicio(servicio);
        servicio.setFiless(servicioExistente.getFiless());
        log.info("Servicio == " + servicio);
        servicioService.guardar(servicio);

        return "redirect:/";
    }

    @GetMapping("/buscar")
    public String buscar(Model model, Provincia provincia, Departamento departamento, Usuario usuario) throws UnsupportedEncodingException {
        List<Servicio> servicios = servicioService.listarServicios();
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        for (Servicio s : servicios) {
            List<DBFile> filess = s.getFiless();
            for (DBFile f : filess) {
                byte[] encodeBase64 = Base64.getEncoder().encode(f.getData());
                String img = new String(encodeBase64, "UTF-8");
                f.setUrl(img);
            }
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("servicios", servicios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);

        return "buscadorServicios";
    }

    @GetMapping("/serviciosUsuario/{idUsuario}")
    public String serviciosUsuario(Model model, Usuario usuario) throws UnsupportedEncodingException {
        usuario = usuarioService.encontrarUsuario(usuario);
        List<Servicio> servicios = usuario.getServicios();
        for (Servicio s : servicios) {
            List<DBFile> filess = s.getFiless();
            for (DBFile f : filess) {
                byte[] encodeBase64 = Base64.getEncoder().encode(f.getData());
                String img = new String(encodeBase64, "UTF-8");
                f.setUrl(img);
            }
        }
        model.addAttribute("servicios", servicios);
        model.addAttribute("usuario", usuario);

        return "serviciosUsuario";
    }

    @GetMapping("/editar/{idServicio}")
    public String editar(Servicio servicio, Model model, DBFile dbFile) {
        servicio = servicioService.encontrarServicio(servicio);
        model.addAttribute("servicio", servicio);

        return "editarServicio";
    }

    @GetMapping("/eliminar")
    public String eliminar(Servicio servicio) {
        servicioService.eliminar(servicio);
        return "redirect:/";
    }

    /**
     * Login form with error.
     */
    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "index";
    }
    
    /**
     * Usuario creado correctamente
     */
    @RequestMapping("/new-user.html")
    public String newUser(Model model) {
        model.addAttribute("newUser", true);
        return "index";
    }
    
  /**
     * Sesion finalizada correctamente
     */
    @RequestMapping("/login-logout.html")
    public String loginLogout(Model model) {
        model.addAttribute("loginLogout", true);
        return "index";
    }

    @GetMapping("/prueba")
    public String prueba(Model model) {
        List<Servicio> servicios = servicioService.listarServicios();
        log.info("ejecutando el controlador spring mvc");
        model.addAttribute("servicios", servicios);
        return "prueba-imagenes";
    }

}
