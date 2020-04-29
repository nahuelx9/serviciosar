package np.com.proyecto.web;

import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import np.com.proyecto.domain.Servicio;
import np.com.proyecto.servicio.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class ControladorInicio {

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/")
    public String inicio(Model model) {
        return "index";
    }

    @GetMapping("/agregarUsuario")
    public String agregarUsuario() {
        return "registroUsuario";
    }

    /*
    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@Valid Servicio servicio, Errors errores) {
        if (errores.hasErrors()) {
            return "registroUsuario";
        }
        servicioService.guardar(servicio);
        return "redirect:/";
    }

    @GetMapping("/editar/{idUsuario}")
    public String editarUsuario(Servicio servicio, Model model) {
        servicio = servicioService.encontrarServicio(servicio);
        model.addAttribute("servicio", servicio);
        return "modificar";
    }

    @GetMapping("/eliminarUsuario")
    public String eliminarUsuario(Servicio servicio) {
        servicioService.eliminar(servicio);
        return "redirect:/";
    }
     */
    @GetMapping("/agregarServicio")
    public String agregarServicio(Servicio servicio) {
        return "registroServicio";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Servicio servicio, Errors errores) {
        if (errores.hasErrors()) {
            return "modificar";
        }
        servicioService.guardar(servicio);
        return "redirect:/";
    }

    @GetMapping("/buscar")
    public String buscar(Model model) {
        List<Servicio> servicios = servicioService.listarServicios();
        model.addAttribute("servicios", servicios);
        return "buscadorServicios";
    }

    @GetMapping("/serviciosUsuario")
    public String serviciosUsuario(Model model) {
        return "serviciosUsuario";
    }

    @GetMapping("/editar/{idServicio}")
    public String editar(Servicio servicio, Model model) {
        servicio = servicioService.encontrarServicio(servicio);
        model.addAttribute("servicio", servicio);
        return "editarServicio";
    }

    @GetMapping("/eliminar")
    public String eliminar(Servicio servicio) {
        servicioService.eliminar(servicio);
        return "redirect:/serviciosUsuario";
    }

    @GetMapping("/prueba")
    public String prueba(Model model) {
         List<Servicio> servicios = servicioService.listarServicios();
        log.info("ejecutando el controlador spring mvc");
        model.addAttribute("servicios", servicios);
        return "index-prueba";
    }

}
