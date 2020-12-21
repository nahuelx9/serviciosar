package np.com.proyecto.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import np.com.proyecto.domain.DBFile;
import np.com.proyecto.domain.Servicio;
import np.com.proyecto.domain.Usuario;
import np.com.proyecto.servicio.DBFileStorageService;
import np.com.proyecto.servicio.ServicioService;
import np.com.proyecto.servicio.UsuarioService;
import np.com.proyecto.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
public class ControladorImagen {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DBFileStorageService dbFileStorageService;

    @Autowired
    private ServicioService servicioService;

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
            if (Util.ValidarFormatoImagen(dbFileNew)) {
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
        if (usuarioLogueado.getIdUsuario().equals(servicio.getIdUsuario()) || user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            DBFile dbFileNew = dbFileStorageService.storeFile(file);
                if (Util.ValidarFormatoImagen(dbFileNew)) {
                dbFileStorageService.modificarImagen(dbFileNew.getData(), dbFileNew.getFileName(), dbFileNew.getFileType(), idValue);
                if (user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
                    return "redirect:/admin";
                } else {
                    return "redirect:/imagenGuardada";
                }

            } else {
                return "redirect:/imagenModificadaError";
            }
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
        if (usuarioLogueado.getIdUsuario().equals(servicio.getIdUsuario()) || user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
            if (cantidadDeImagenes > 1) {
                dbFileStorageService.eliminar(dbFile);
                if (user.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
                    return "redirect:/admin";
                } else {
                    return "redirect:/imagenEliminada";
                }
            } else {
                return "redirect:/imagenMinima";
            }
        } else {
            return "error";
        }
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
