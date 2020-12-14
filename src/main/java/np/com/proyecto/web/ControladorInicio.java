package np.com.proyecto.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import np.com.proyecto.domain.DBFile;
import np.com.proyecto.domain.Filtro;
import np.com.proyecto.domain.Servicio;
import np.com.proyecto.domain.Usuario;
import np.com.proyecto.servicio.DBFileStorageService;
import np.com.proyecto.servicio.ServicioService;
import np.com.proyecto.servicio.UsuarioService;
import np.com.proyecto.util.Departamento;
import np.com.proyecto.util.Provincia;
import np.com.proyecto.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private ServicioService servicioService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DBFileStorageService dbFileStorageService;

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

    @GetMapping("/agregarUsuario")
    public String agregarUsuario(Model model, Usuario usuario, Provincia provincia, Departamento departamento) {
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);
        return "registroUsuario";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@Valid Usuario usuario, BindingResult result, Model model, @RequestParam("password2") String password2, Provincia provincia, Departamento departamento) {
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        if (result.hasErrors()) {
            model.addAttribute("provincias", provincias);
            model.addAttribute("departamentos", departamentos);
            return "registroUsuario";
        }
        if (usuarioService.verificarExistenciaEmail(usuario.getUsername()) == false) {
            result.rejectValue("username", "error.user", "Ya existe una cuenta con ese email");
            model.addAttribute("provincias", provincias);
            model.addAttribute("departamentos", departamentos);
            return "registroUsuario";
        }
        if (!usuario.getPassword().equals(password2)) {
            result.rejectValue("password", "error.user", "Las contraseñas no coinciden");
            model.addAttribute("provincias", provincias);
            model.addAttribute("departamentos", departamentos);
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
            model.addAttribute("provincias", provincias);
            model.addAttribute("departamentos", departamentos);
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
    public String modificarProvinciaDepartamentoUsuario(@RequestParam("provincia") String provincia, @RequestParam("departamento") String departamento, @AuthenticationPrincipal User user, Util util) {
        Usuario usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        String nombreProvincia = util.obtenerNombreProvincia(provincia);
        usuarioService.modificarProvinciaDepartamentoUsuario(nombreProvincia, departamento, usuario.getIdUsuario());
        return "redirect:/datosModificados";
    }

    @PostMapping("/modificarEmailUsuario")
    public String modificarEmailUsuario(@RequestParam("email") String email, @AuthenticationPrincipal User user, Util util) {
        Usuario usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        if (usuarioService.verificarExistenciaEmail(email) && !email.isEmpty() && util.ValidarMail(email) == true) {
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

    @GetMapping("/agregarServicio")
    public String agregarServicio(Servicio servicio, @AuthenticationPrincipal User user, Usuario usuario) {
        usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        if (usuario.getServicios().size() <= 3) {
            return "registroServicio";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/guardarServicio", method = RequestMethod.POST)
    public String guardar(@Valid Servicio servicio, BindingResult result, Usuario usuario, @AuthenticationPrincipal User user, @RequestParam("files") MultipartFile[] files, Util util) throws IOException {
        if (result.hasErrors() || !util.validar(files)) {
            return "registroServicio";
        }

        usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        servicio.setIdUsuario(usuario.getIdUsuario());
        servicioService.guardar(servicio);
        for (MultipartFile file : files) {
            DBFile dbFile = dbFileStorageService.storeFile(file);
            dbFile.setIdServicio(servicio.getIdServicio());
            if (dbFile.getFileType().equals("image/jpeg") || dbFile.getFileType().equals("image/jpg") || dbFile.getFileType().equals("image/pneg") || dbFile.getFileType().equals("image/png")) {
                dbFileStorageService.guardar(dbFile);
            }
        }
        return "redirect:/servicioCreado";
    }

    @GetMapping("/serviciosUsuario/{idUsuario}")
    public String serviciosUsuario(Model model, Usuario usuario, @AuthenticationPrincipal User user) throws UnsupportedEncodingException {
        usuario = usuarioService.encontrarUsuario(usuario);
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        if (usuario.equals(usuarioLogueado)) {
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
        } else {
            return "error";
        }
    }

    @GetMapping("/editarServicio/{idServicio}")
    public String editar(Servicio servicio, Model model, DBFile dbFile, @AuthenticationPrincipal User user) {
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        servicio = servicioService.encontrarServicio(servicio);
        if (usuarioLogueado.getIdUsuario().equals(servicio.getIdUsuario())) {
            List<DBFile> files = servicio.getFiless();
            model.addAttribute("servicio", servicio);
            model.addAttribute("files", files);
            return "editarServicio";
        } else {
            return "error";
        }
    }

    @PostMapping("/guardarServicioEditado")
    public String guardarEditado(@Valid Servicio servicio, Errors errores, @AuthenticationPrincipal User user) throws UnsupportedEncodingException {
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        if (errores.hasErrors() || usuarioLogueado.getIdUsuario().equals(servicio.getIdServicio())) {
            log.info("error = " + errores);
            return "error";
        }
        servicioService.actualizarServicio(servicio.getNombre(), servicio.getCelular(), servicio.isWhatsapp(), servicio.getEmail(), servicio.getPrecio(), servicio.getPrecioDescripcion(), servicio.getHorario(), servicio.getDescripcion(), servicio.getIdServicio());
        return "redirect:/servicioModificado";
    }

    /**
     * IMAGENES
     *
     * @param file
     * @param idServicio
     * @param user
     * @return
     */
    @PostMapping("/guardarImagen")
    public String guardarImagen(@RequestParam("file") MultipartFile file, @RequestParam("idServicio") int idServicio, @AuthenticationPrincipal User user) {
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        Servicio servicio = servicioService.encontrarServicioById(idServicio);
        if (usuarioLogueado.getIdUsuario().equals(servicio.getIdUsuario())) {
            DBFile dbFileNew = dbFileStorageService.storeFile(file);
            if (dbFileNew.getFileType().equals("image/jpeg") || dbFileNew.getFileType().equals("image/jpg") || dbFileNew.getFileType().equals("image/pneg") || dbFileNew.getFileType().equals("image/png")) {
                dbFileNew.setIdServicio(idServicio);
                dbFileStorageService.guardar(dbFileNew);
                return "redirect:/imagenGuardada";
            } else {
                return "redirect:/imagenModificadaError";
            }
        } else {
            return "error";
        }
    }

    @GetMapping("/editarImagenes/{idServicio}")
    public String editarImagenes(Servicio servicio, Model model, DBFile dbFile, @AuthenticationPrincipal User user) throws UnsupportedEncodingException {
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        servicio = servicioService.encontrarServicio(servicio);
        if (usuarioLogueado.getIdUsuario().equals(servicio.getIdUsuario())) {
            List<DBFile> files = new ArrayList<>();
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
            model.addAttribute("servicio", servicio);
            return "editarImagenes";
        } else {
            return "error";
        }
    }

    @PostMapping("/modificarImagen")
    public String modificarImagen(@RequestParam("file") MultipartFile file, @RequestParam("idValue") String idValue, @AuthenticationPrincipal User user) {
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        DBFile dbFile = dbFileStorageService.encontrarDBFileById(idValue);
        Servicio servicio = servicioService.encontrarServicioById(dbFile.getIdServicio());
        if (usuarioLogueado.getIdUsuario().equals(servicio.getIdUsuario())) {
            DBFile dbFileNew = dbFileStorageService.storeFile(file);
            if (dbFileNew.getFileType().equals("image/jpeg") || dbFileNew.getFileType().equals("image/jpg") || dbFileNew.getFileType().equals("image/pneg") || dbFileNew.getFileType().equals("image/png")) {
                dbFileStorageService.modificarImagen(dbFileNew.getData(), dbFileNew.getFileName(), dbFileNew.getFileType(), idValue);
                return "redirect:/imagenGuardada";
            } else {
                return "redirect:/imagenModificadaError";
            }
        } else {
            return "error";
        }
    }

    @GetMapping("/buscar")
    public String buscar(Model model, Provincia provincia, Departamento departamento, Usuario usuario, Util util, @RequestParam Map<String, Object> params) throws UnsupportedEncodingException {

        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        for(Departamento dep : departamentos){
            System.out.println("departamentos::" + dep.getNombre() + dep.getId());
        }
        Filtro filtro = new Filtro();

        /*Paginacion*/
 /* obtenemos el parametro y si es diferente de null entonces convertimos el valor a un integer*/
        int page = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;

        PageRequest pageRequest = PageRequest.of(page, 7);
        Page<Servicio> pageServicio = servicioService.getAll(pageRequest);

        int totalPage = pageServicio.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        util.modificarUrlImagen(pageServicio.getContent());

        model.addAttribute("list", pageServicio.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        model.addAttribute("filtro", filtro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);

        return "buscadorServicios";
    }

    @GetMapping("/buscarPorNombre")
    public String buscarPorNombre(Model model, Provincia provincia, Departamento departamento, Usuario usuario, Util util, @RequestParam("nombre") String nombre, @RequestParam Map<String, Object> params, Filtro filtro) throws UnsupportedEncodingException {
        Page<Servicio> pageServicio;
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        filtro.setNombre(nombre);

        /*Paginacion*/
 /* obtenemos el parametro y si es diferente de null entonces convertimos el valor a un integer*/
        int page = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        PageRequest pageRequest = PageRequest.of(page, 7);

        pageServicio = servicioService.findByNombre(nombre, pageRequest);

        util.modificarUrlImagen(pageServicio.getContent());

        int totalPage = pageServicio.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        model.addAttribute("filtro", filtro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);

        model.addAttribute("list", pageServicio.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "buscadorServicios";
    }

    @GetMapping("/buscarPorFiltros")
    public String buscarPorFiltros(Model model, Provincia provincia, Departamento departamento, Usuario usuario, @RequestParam("nombre") String nombreFiltro, @RequestParam("provincia") String provinciaFiltro, @RequestParam("departamento") String departamentoFiltro, @RequestParam("horario") String horarioFiltro, @RequestParam("rangoPrecio") String rangoPrecioFiltro, @RequestParam Map<String, Object> params, Util util, Filtro filtro) throws UnsupportedEncodingException {
        String nombreProvincia = util.obtenerNombreProvincia(provinciaFiltro);
        filtro = util.obtenerFiltros(nombreFiltro, nombreProvincia, departamentoFiltro, horarioFiltro, rangoPrecioFiltro);
        Page<Servicio> pageServicio = null;
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        boolean sinServicios = false;

        /*Paginacion*/
 /* obtenemos el parametro y si es diferente de null entonces convertimos el valor a un integer*/
        int page = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        PageRequest pageRequest = PageRequest.of(page, 7);

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            return "redirect:/buscar";
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByAllFilters(filtro.getProvincia(), filtro.getDepartamento(), filtro.getHorario(), filtro.getPrecio(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findAllAndNombre(filtro.getProvincia(), filtro.getDepartamento(), filtro.getHorario(), filtro.getPrecio(), filtro.getNombre(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByProvincia(filtro.getProvincia(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && "".equals(filtro.getNombre())) {
            pageServicio = servicioService.findByDepartamento(filtro.getProvincia(), filtro.getDepartamento(), pageRequest);

        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByPrecio(filtro.getPrecio(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario())) {
            pageServicio = servicioService.findByProvinciaDepartamentoHorario(filtro.getProvincia(), filtro.getDepartamento(), filtro.getHorario(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"0".equals(filtro.getPrecio())) {
            pageServicio = servicioService.findByProvinciaDepartamentoPrecio(filtro.getProvincia(), filtro.getDepartamento(), filtro.getPrecio(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio())) {
            pageServicio = servicioService.findByProvinciaHorarioPrecio(filtro.getProvincia(), filtro.getHorario(), filtro.getPrecio(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && !"No especificar".equals(filtro.getHorario())) {
            pageServicio = servicioService.findByProvinciaHorario(filtro.getProvincia(), filtro.getHorario(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && !"0".equals(filtro.getPrecio())) {
            pageServicio = servicioService.findByProvinciaPrecio(filtro.getProvincia(), filtro.getPrecio(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByNombreProvincia(filtro.getNombre(), filtro.getProvincia(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByNombreProvinciaHorarioPrecio(filtro.getProvincia(), filtro.getHorario(), filtro.getPrecio(), filtro.getNombre(), pageRequest);
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByNombre(filtro.getNombre(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByNombreProvinciaDepartamentoHora(filtro.getProvincia(), filtro.getDepartamento(), filtro.getHorario(), filtro.getNombre(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && !"".equals(filtro.getDepartamento()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByNombreProvinciaDepartamento(filtro.getProvincia(), filtro.getDepartamento(), filtro.getNombre(), pageRequest);
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            pageServicio = servicioService.getAll(pageRequest);
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByHorario(filtro.getHorario(), pageRequest);
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByNombrePrecio(filtro.getNombre(), filtro.getPrecio(), pageRequest);
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByNombreHora(filtro.getNombre(), filtro.getHorario(), pageRequest);
        }

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && !"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByNombreHorarioPrecio(filtro.getHorario(), filtro.getPrecio(), filtro.getNombre(), pageRequest);
        }

        if (!"No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByPrecioHorario(filtro.getHorario(), filtro.getPrecio(), pageRequest);
        }

        if (!"".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && !"0".equals(filtro.getPrecio()) && (filtro.getNombre() != null || !"".equals(filtro.getNombre()))) {
            pageServicio = servicioService.findByNombreProvinciaPrecio(filtro.getProvincia(), filtro.getPrecio(), filtro.getNombre(), pageRequest);
        }
        if (pageServicio.isEmpty()) {
            pageServicio = servicioService.getAll(pageRequest);
            sinServicios = true;
        }

        util.modificarUrlImagen(pageServicio.getContent());

        int totalPage = pageServicio.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        model.addAttribute("filtro", filtro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("sinServicios", sinServicios);

        model.addAttribute("list", pageServicio.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "buscadorServicios";
    }

    /**
     * Eliminar Usuario
     *
     * @param usuario
     * @return
     */
    @GetMapping("/eliminarUsuario")
    public String eliminar(Usuario usuario) {
        usuarioService.eliminar(usuario);
        return "redirect:/";
    }

    /**
     * Eliminar Servicio
     *
     * @param servicio
     * @param user
     * @return
     */
    @GetMapping("/eliminarServicio")
    public String eliminar(Servicio servicio, @AuthenticationPrincipal User user) {
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        Servicio servicioEncontrado = servicioService.encontrarServicio(servicio);
        if (usuarioLogueado.getIdUsuario().equals(servicioEncontrado.getIdUsuario())) {
            servicioService.eliminar(servicio);
            return "redirect:/servicioEliminado";
        } else {
            return "error";
        }
    }

    /**
     * Eliminar Imagen
     *
     * @param dbFile
     * @param user
     * @return
     */
    @GetMapping("/eliminarImagen")
    public String eliminarImagen(DBFile dbFile, @AuthenticationPrincipal User user) {
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        DBFile db = dbFileStorageService.encontrarDBFile(dbFile);
        Servicio servicio = servicioService.encontrarServicioById(db.getIdServicio());
        int cantidadDeImagenes = servicio.getFiless().size();
        if (usuarioLogueado.getIdUsuario().equals(servicio.getIdUsuario())) {
            if (cantidadDeImagenes > 1) {
                dbFileStorageService.eliminar(dbFile);
                return "redirect:/imagenEliminada";
            } else {
                return "redirect:/imagenMinima";
            }
        } else {
            return "error";
        }
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
     * Sesion finalizada correctamente
     *
     * @param model
     * @return
     */
    @RequestMapping("/login-logout.html")
    public String loginLogout(Model model
    ) {
        model.addAttribute("loginLogout", true);
        return "redirect:/";
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

    @RequestMapping("/servicioCreado")
    public String servicioCreado(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("servicioCreado", true);
        return "index";
    }

    @RequestMapping("/servicioModificado")
    public String servicioModificado(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("servicioModificado", true);
        return "index";
    }

    @RequestMapping("/servicioEliminado")
    public String servicioEliminado(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("servicioEliminado", true);
        return "index";
    }

    @RequestMapping("/imagenGuardada")
    public String imagenGuardada(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("imagenGuardada", true);
        return "index";
    }

    @RequestMapping("/imagenEliminada")
    public String imagenEliminada(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("imagenEliminada", true);
        return "index";
    }
    
      @RequestMapping("/imagenMinima")
    public String imagenMinima(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("imagenMinima", true);
        return "index";
    }

    @RequestMapping("/imagenModificadaError")
    public String imagenModificadaError(Usuario usuario, Model model,
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("imagenModificadaError", true);
        return "index";
    }

}
