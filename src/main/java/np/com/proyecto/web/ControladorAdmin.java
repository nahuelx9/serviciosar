/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package np.com.proyecto.web;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import np.com.proyecto.domain.Alerta;
import np.com.proyecto.domain.Buzon;
import np.com.proyecto.domain.DBFile;
import np.com.proyecto.domain.Servicio;
import np.com.proyecto.domain.Usuario;
import np.com.proyecto.servicio.AlertaService;
import np.com.proyecto.servicio.BuzonService;
import np.com.proyecto.servicio.DBFileStorageService;
import np.com.proyecto.servicio.ServicioService;
import np.com.proyecto.servicio.UsuarioService;
import np.com.proyecto.util.Departamento;
import np.com.proyecto.util.Provincia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class ControladorAdmin {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DBFileStorageService dbFileStorageService;

    @Autowired
    private BuzonService buzonService;

    @Autowired
    private AlertaService alertaService;

    @GetMapping("/admin")
    public String admin(Model model) throws UnsupportedEncodingException {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<Servicio> servicios = servicioService.listarServicios();
        List<Buzon> buzones = buzonService.listarBuzon();
        List<Alerta> alertas = alertaService.listarAlertas();
        List<DBFile> imagenes = dbFileStorageService.listarDBFiles();
        for (DBFile i : imagenes) {
            byte[] encodeBase64 = Base64.getEncoder().encode(i.getData());
            String img = new String(encodeBase64, "UTF-8");
            i.setUrl(img);
        }
        int cantidadUsuarios = usuarios.size();
        int cantidadServicios = servicios.size();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("servicios", servicios);
        model.addAttribute("buzones", buzones);
        model.addAttribute("alertas", alertas);
        model.addAttribute("imagenes", imagenes);
        model.addAttribute("cantidadUsuarios", cantidadUsuarios);
        model.addAttribute("cantidadServicios", cantidadServicios);
        return "administracion";
    }

    @GetMapping("/admin/editarUsuario/{idUsuario}")
    public String adminEditarUsuario(Model model, Usuario usuario, Provincia provincia, Departamento departamento) {
        List<Provincia> provincias = provincia.listarProvincia();
        List<Departamento> departamentos = departamento.listarDepartamento();
        List<Departamento> localidadesAmba = departamento.listarLocalidadesAmba();
        usuario = usuarioService.encontrarUsuario(usuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("provincias", provincias);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("localidadesAmba", localidadesAmba);
        return "adminEdicionUsuario";
    }

    @PostMapping("/admin/editarUsuario")
    public String adminEditarUsuario(Usuario usuario) {
        usuarioService.modificarUsuario(usuario);
        return "redirect:/admin";
    }

    @GetMapping("/contacto")
    public String contacto(Buzon buzon) {
        return "buzon";
    }

    @PostMapping("/enviarMensaje")
    public String enviarMensaje(@Valid Buzon buzon, BindingResult result) {
        if (result.hasErrors()) {
            return "buzon";
        }
        buzonService.guardar(buzon);
        return "redirect:/mensajeEnviado";
    }

    @GetMapping("/admin/eliminarMensaje")
    public String eliminarMensaje(Buzon buzon) {
        buzonService.eliminar(buzon);
        return "redirect:/admin";
    }

    @PostMapping("/enviarReporte")
    public String enviarReporte(Alerta alerta) {
        alertaService.guardar(alerta);
        return "redirect:/reporteEnviado";
    }

    @GetMapping("/admin/eliminarAlerta")
    public String eliminarMensaje(Alerta alerta) {
        alertaService.eliminar(alerta);
        return "redirect:/admin";
    }
}
