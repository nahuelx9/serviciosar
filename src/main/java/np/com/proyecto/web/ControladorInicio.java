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
import np.com.proyecto.domain.Filtro;
import np.com.proyecto.domain.Servicio;
import np.com.proyecto.domain.Usuario;
import np.com.proyecto.payload.UploadFileResponse;
import np.com.proyecto.servicio.DBFileStorageService;
import np.com.proyecto.servicio.RolService;
import np.com.proyecto.servicio.ServicioService;
import np.com.proyecto.servicio.UsuarioService;
import np.com.proyecto.util.Departamento;
import np.com.proyecto.util.Provincia;
import np.com.proyecto.util.Util;
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
import org.springframework.web.bind.annotation.PutMapping;
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
    public String guardarEditado(@Valid Servicio servicio, Errors errores, Usuario usuario, DBFile dbFile, @AuthenticationPrincipal User user) throws UnsupportedEncodingException {
        if (errores.hasErrors()) {
            log.info("error = " + errores);
            return "registroServicio";
        }

        log.info("Servicio = " + servicio.getIdServicio());
        Long idServicio = servicio.getIdServicio();
        int idServ = idServicio.intValue();
        servicioService.actualizarServicio(servicio.getNombre(), servicio.getCelular(), servicio.isWhatsapp(), servicio.getPrecioDescripcion(), servicio.getHorario(), servicio.getDescripcion(), idServ);
        return "redirect:/";
    }

    @GetMapping("/buscar")
    public String buscar(Model model, Provincia provincia, Departamento departamento, Usuario usuario, Util util) throws UnsupportedEncodingException {
        List<Servicio> servicios = servicioService.listarServicios();
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        util.modificarUrlImagen(servicios);
        Filtro filtro = new Filtro();

        model.addAttribute("filtro", filtro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("servicios", servicios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);

        return "buscadorServicios";
    }

    @GetMapping("/buscarPorNombre")
    public String buscarPorNombre(Model model, Provincia provincia, Departamento departamento, Usuario usuario, Util util, @RequestParam("nombre") String nombre) throws UnsupportedEncodingException {
        List<Servicio> servicios = servicioService.listarServicios();
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        util.modificarUrlImagen(servicios);
        Filtro filtro = new Filtro();
        if (!"".equals(nombre)) {
            filtro.setNombre(nombre);
            servicios = servicioService.findByNombre(nombre);
        }

        model.addAttribute("filtro", filtro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("servicios", servicios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);

        return "buscadorServicios";
    }

    @GetMapping("/buscarPorFiltros")
    public String buscarPorFiltros(Model model, Provincia provincia, Departamento departamento, Usuario usuario, @RequestParam("nombre") String nombreFiltro, @RequestParam("provincia") String provinciaFiltro, @RequestParam("departamento") String departamentoFiltro, @RequestParam("horario") String horarioFiltro, @RequestParam("rangoPrecio") String rangoPrecioFiltro, Util util, Filtro filtro) throws UnsupportedEncodingException {
        String nombreProvincia = util.obtenerNombreProvincia(provinciaFiltro);
        filtro = util.obtenerFiltros(nombreFiltro, nombreProvincia, departamentoFiltro, horarioFiltro, rangoPrecioFiltro);
        List<Servicio> servicios = new ArrayList();
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        System.out.println("FILTROSSSSS =====" + filtro.toString());
        /*Condicionales filtros*/
 /*todos los filtros*/

 /*   if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            servicios = servicioService.findByAllFilters(filtro.getProvincia(), filtro.getDepartamento(), filtro.getHorario(), filtro.getPrecio());
        }*/

 /*todos los filtros y el filtro.getNombre() */
        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findAllAndNombre( filtro.getProvincia(), filtro.getDepartamento(), filtro.getHorario(), filtro.getPrecio(),filtro.getNombre());
        }

        if (!"".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            servicios = servicioService.findByProvincia(filtro.getProvincia());
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && "".equals(filtro.getNombre())) {
            servicios = servicioService.findByDepartamento(filtro.getProvincia(), filtro.getDepartamento());

        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            servicios = servicioService.findByPrecio(filtro.getPrecio());
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario())) {
            servicios = servicioService.findByProvinciaDepartamentoHorario(filtro.getProvincia(), filtro.getDepartamento(), filtro.getHorario());
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"0".equals(filtro.getPrecio())) {
            servicios = servicioService.findByProvinciaDepartamentoPrecio(filtro.getProvincia(), filtro.getDepartamento(), filtro.getPrecio());
        }

        if (!"".equals(filtro.getProvincia()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio())) {
            servicios = servicioService.findByProvinciaHorarioPrecio(filtro.getProvincia(), filtro.getHorario(), filtro.getPrecio());
        }

        if (!"".equals(filtro.getProvincia()) && !"No especificar".equals(filtro.getHorario())) {
            servicios = servicioService.findByProvinciaHorario(filtro.getProvincia(), filtro.getHorario());
        }

        if (!"".equals(filtro.getProvincia()) && !"0".equals(filtro.getPrecio())) {
            servicios = servicioService.findByProvinciaPrecio(filtro.getProvincia(), filtro.getPrecio());
        }

        if (!"".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findByNombreProvincia(filtro.getNombre(), filtro.getProvincia());
        }

        if (!"".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findByNombreProvinciaHorarioPrecio( filtro.getProvincia(), filtro.getHorario(), filtro.getPrecio(),filtro.getNombre());
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findByNombre(filtro.getNombre());
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findByNombreProvinciaDepartamentoHora( filtro.getProvincia(), filtro.getDepartamento(), filtro.getHorario(),filtro.getNombre());
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findByNombreProvinciaDepartamento( filtro.getProvincia(), filtro.getDepartamento(),filtro.getNombre());
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            servicios = servicioService.listarServicios();
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            servicios = servicioService.findByHorario(filtro.getHorario());
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findByNombrePrecio(filtro.getNombre(), filtro.getPrecio());
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findByNombreHora(filtro.getNombre(), filtro.getHorario());
        }

        
        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findByNombreHorarioPrecio( filtro.getHorario(), filtro.getPrecio(),filtro.getNombre());
        }
         
        
        if (  !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            servicios = servicioService.findByPrecioHorario(filtro.getHorario(), filtro.getPrecio());
        }
        
          if (!"".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            servicios = servicioService.findByNombreProvinciaPrecio( filtro.getProvincia(),filtro.getPrecio(),filtro.getNombre());
        }

        
        
        util.modificarUrlImagen(servicios);

        model.addAttribute("filtro", filtro);
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
        List<DBFile> files = servicio.getFiless();
        model.addAttribute("servicio", servicio);
        model.addAttribute("files", files);
        return "editarServicio";
    }

    /**
     * Editar fotos
     */
    @GetMapping("/editarImagenes/{idServicio}")
    public String editarImagenes(Servicio servicio, Model model, DBFile dbFile) throws UnsupportedEncodingException {
        servicio = servicioService.encontrarServicio(servicio);
        List<DBFile> files = new ArrayList<DBFile>();
        for (DBFile f : servicio.getFiless()) {
            byte[] encodeBase64 = Base64.getEncoder().encode(f.getData());
            String img = new String(encodeBase64, "UTF-8");
            f.setUrl(img);
            files.add(f);
            log.info("file" + f);
        }
        int cantFiles = files.size();
        model.addAttribute("cantFiles", cantFiles);
        model.addAttribute("files", files);
        return "editarImagenes";
    }

    @PostMapping("/modificarImagen")
    public String modificarImagen(@Valid DBFile dbFile, Errors errores, @RequestParam("file") MultipartFile file, @RequestParam("idValue") String idValue) throws UnsupportedEncodingException {
        if (errores.hasErrors()) {
            log.info("error = " + errores);
            return "registroServicio";
        }
        String id = idValue;
        log.info("id imagen = " + idValue);
        DBFile dbFileNew = dbFileStorageService.storeFile(file);
        if (dbFileNew.getFileType().equals("image/jpeg") || dbFileNew.getFileType().equals("image/jpg") || dbFileNew.getFileType().equals("image/pneg") || dbFileNew.getFileType().equals("image/png")) {
            dbFileStorageService.modificarImagen(dbFileNew.getData(), dbFileNew.getFileName(), dbFileNew.getFileType(), id);
        }
        return "redirect:/";
    }

    /**
     * Eliminar Servicio
     */
    @GetMapping("/eliminar")
    public String eliminar(Servicio servicio) {
        servicioService.eliminar(servicio);
        return "redirect:/";
    }

    /**
     * Eliminar Imagen
     */
    @GetMapping("/eliminarImagen")
    public String eliminarImagen(DBFile dbFile) {
        dbFileStorageService.eliminar(dbFile);
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

    /* @GetMapping("/prueba")
    public void prueba(Model model, @RequestParam("provincia") String provincia, @RequestParam("departamento") String departamento,@RequestParam("horario") String horario,@RequestParam("rangoPrecio") String rangoPrecio, Util util) {
       
       String nombreProvincia = util.obtenerNombreProvincia(provincia);
        log.info("Horario filtros:" + horario);
       log.info("provinicia filtros:" +  nombreProvincia);
       log.info("departamento filtros:" + departamento);
       log.info("Rango filtros:" + rangoPrecio);
    
    = new Filtro("");
        
        if(!"".equals(nombre)){
            filtro.setNombre(nombre);
        }
        
         for (Servicio s : servicios) {
            List<DBFile> filess = s.getFiless();
            for (DBFile f : filess) {
                byte[] encodeBase64 = Base64.getEncoder().encode(f.getData());
                String img = new String(encodeBase64, "UTF-8");
                f.setUrl(img);
            }
        }
         model.addAttribute("filtro",filtro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("servicios", servicios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);
         
        
         return "buscadorServicios";
     }
       
    }*/
}
