package np.com.proyecto.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
public class ControlladorServicio {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DBFileStorageService dbFileStorageService;

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
    public String guardar(@Valid Servicio servicio, BindingResult result, Usuario usuario, @AuthenticationPrincipal User user, @RequestParam("files") MultipartFile[] files) throws IOException {
        if (result.hasErrors() || !Util.validar(files)) {
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
        if (usuarioLogueado.getIdUsuario().equals(servicio.getIdUsuario()) || user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
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
        if (errores.hasErrors()) {
            log.info("error = " + errores);
            return "error";
        }
        if (usuarioLogueado.getIdUsuario().equals(servicio.getIdUsuario()) || user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            servicioService.actualizarServicio(servicio);
            if (user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
                return "redirect:/admin";
            } else {
                return "redirect:/servicioModificado";
            }
        } else {
            return "error";
        }
    }

    @GetMapping("/listaServicios")
    public String listaServicios(Model model, Provincia provincia, Departamento departamento, Usuario usuario, Util util, @RequestParam Map<String, Object> params) throws UnsupportedEncodingException {

        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Departamento> localidadesAmba = departamento.listarLocalidadesAmba();
        List<Usuario> usuarios = usuarioService.listarUsuarios();
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

        Util.modificarUrlImagen(pageServicio.getContent());

        model.addAttribute("list", pageServicio.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        model.addAttribute("filtro", filtro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("localidadesAmba", localidadesAmba);

        return "buscadorServicios";
    }

    @GetMapping("/listaPorNombre")
    public String listarrPorNombre(Model model, Provincia provincia, Departamento departamento, Usuario usuario, @RequestParam("nombre") String nombre, @RequestParam Map<String, Object> params, Filtro filtro) throws UnsupportedEncodingException {
        Page<Servicio> pageServicio;
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Departamento> localidadesAmba = departamento.listarLocalidadesAmba();
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        filtro.setNombre(nombre);

        /*Paginacion*/
 /* obtenemos el parametro y si es diferente de null entonces convertimos el valor a un integer*/
        int page = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        PageRequest pageRequest = PageRequest.of(page, 7);

        pageServicio = servicioService.findByNombre(nombre, pageRequest);

        Util.modificarUrlImagen(pageServicio.getContent());

        int totalPage = pageServicio.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        model.addAttribute("filtro", filtro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("localidadesAmba", localidadesAmba);

        model.addAttribute("list", pageServicio.getContent());
        model.addAttribute("current", page + 1);
        model.addAttribute("next", page + 2);
        model.addAttribute("prev", page);
        model.addAttribute("last", totalPage);

        return "buscadorServicios";
    }

    @GetMapping("/listaPorFiltros")
    public String listarPorFiltros(Model model, Provincia provincia, Departamento departamento, Usuario usuario, @RequestParam("nombre") String nombreFiltro, @RequestParam("provincia") String provinciaFiltro, @RequestParam("departamento") String departamentoFiltro, @RequestParam("horario") String horarioFiltro, @RequestParam("rangoPrecio") String rangoPrecioFiltro, @RequestParam Map<String, Object> params, Filtro filtro) throws UnsupportedEncodingException {
        String nombreProvincia = Util.obtenerNombreProvincia(provinciaFiltro);
        filtro = Util.obtenerFiltros(nombreFiltro, nombreProvincia, departamentoFiltro, horarioFiltro, rangoPrecioFiltro);
        Page<Servicio> pageServicio = null;
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Departamento> localidadesAmba = departamento.listarLocalidadesAmba();
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        boolean sinServicios = false;

        /*Paginacion*/
 /* obtenemos el parametro y si es diferente de null entonces convertimos el valor a un integer*/
        int page = params.get("page") != null ? Integer.valueOf(params.get("page").toString()) - 1 : 0;
        PageRequest pageRequest = PageRequest.of(page, 7);

        if ("".equals(filtro.getProvincia()) && "".equals(filtro.getDepartamento()) && "No especificar".equals(filtro.getHorario()) && "0".equals(filtro.getPrecio()) && (filtro.getNombre() == null || "".equals(filtro.getNombre()))) {
            return "redirect:/listaServicios";
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

        Util.modificarUrlImagen(pageServicio.getContent());

        int totalPage = pageServicio.getTotalPages();
        if (totalPage > 0) {
            List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pages", pages);
        }

        model.addAttribute("filtro", filtro);
        model.addAttribute("provinciaFiltro", provinciaFiltro);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("localidadesAmba", localidadesAmba);
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
     * @param user
     * @return
     */
    @GetMapping("/eliminarUsuario")
    public String eliminar(Usuario usuario, @AuthenticationPrincipal User user) {
        Usuario usuarioLogueado = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
        Usuario usuarioEncontrado = usuarioService.encontrarUsuario(usuario);
        if (usuarioLogueado.getIdUsuario().equals(usuarioEncontrado.getIdUsuario()) || user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            List<Servicio> servicios = usuarioEncontrado.getServicios();
            for (Servicio servicio : servicios) {
                List<DBFile> imagenes = servicio.getFiless();
                for (DBFile imagen : imagenes) {
                    dbFileStorageService.eliminar(imagen);
                }
                servicioService.eliminar(servicio);
            }
            usuarioService.eliminar(usuario);
            if (user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
                return "redirect:/admin";
            } else {
                return "redirect:/";
            }
        } else {
            return "error";
        }

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
        if (usuarioLogueado.getIdUsuario().equals(servicioEncontrado.getIdUsuario()) || user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            List<DBFile> imagenes = servicioEncontrado.getFiless();
            for (DBFile imagen : imagenes) {
                dbFileStorageService.eliminar(imagen);
            }
            servicioService.eliminar(servicio);
            if (user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
                return "redirect:/admin";
            } else {
                return "redirect:/servicioEliminado";
            }
        } else {
            return "error";
        }
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
}
