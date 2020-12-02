package np.com.proyecto.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import np.com.proyecto.domain.DBFile;
import np.com.proyecto.domain.Filtro;
import np.com.proyecto.domain.Servicio;
import np.com.proyecto.domain.Usuario;
import np.com.proyecto.servicio.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Util {

    @Autowired
    private ServicioService servicioService;

    /*public static void main(String[] args) {
        Usuario usuario = new Usuario();
        Long id = usuario.getIdUsuario();
        System.out.println(id);
    }*/
 /*Obtener nombre de las provincias segun su id*/
    public String obtenerNombreProvincia(String idProvincia) {
        String provincia = "";
        switch (idProvincia) {
            case "54":
                provincia = "Misiones";
                break;
            case "74":
                provincia = "San Luis";
                break;
            case "70":
                provincia = "San Juan";
                break;
            case "30":
                provincia = "Entre Ríos";
                break;
            case "78":
                provincia = "Santa Cruz";
                break;
            case "62":
                provincia = "Río Negro";
                break;
            case "26":
                provincia = "Chubut";
                break;
            case "14":
                provincia = "Córdoba";
                break;
            case "50":
                provincia = "Mendoza";
                break;
            case "46":
                provincia = "La Rioja";
                break;
            case "10":
                provincia = "Catamarca";
                break;
            case "42":
                provincia = "La Pampa";
                break;
            case "86":
                provincia = "Santiago del Estero";
                break;
            case "18":
                provincia = "Corrientes";
                break;
            case "82":
                provincia = "Santa Fe";
                break;
            case "90":
                provincia = "Tucumán";
                break;
            case "58":
                provincia = "Neuquén";
                break;
            case "66":
                provincia = "Salta";
                break;
            case "22":
                provincia = "Chaco";
                break;
            case "34":
                provincia = "Formosa";
                break;
            case "38":
                provincia = "Jujuy";
                break;
            case "02":
                provincia = "Ciudad Autónoma de Buenos Aires";
                break;
            case "06":
                provincia = "Buenos Aires";
                break;
            case "94":
                provincia = "Tierra del Fuego, Antártida e Islas del Atlántico Sur";
                break;
        }
        return provincia;
    }

    /*Obtiene los valores por paametro y le asigna el valor a los filtros*/
    public Filtro obtenerFiltros(String nombre, String provincia, String departamento, String horario, String precio) {

        Filtro filtro = new Filtro();

        if (!"".equals(nombre) || nombre != null) {
            filtro.setNombre(nombre);
        }

        if (!"".equals(provincia) || provincia != null || !"0".equals(provincia)) {
            filtro.setProvincia(provincia);
        }

        if (!"".equals(departamento) && departamento != null || !"0".equals(provincia)) {
            filtro.setDepartamento(departamento);
        }

        if (!"".equals(horario) || horario != null) {
            filtro.setHorario(horario);
        }

        if (!"".equals(precio) || precio != null) {
            filtro.setPrecio(precio);
        }
        return filtro;

    }

    /*
    public List<Servicio> serviciosBusqueda(String nombre, String provincia, String departamento, String horario, String rangoPrecio) {
        List<Servicio> servicios = null;

        
        if (!"".equals(provincia) && !"".equals(departamento) && !"No especificar".equals(horario) && !"0".equals(rangoPrecio) && (nombre == null || "".equals(nombre))) {
            servicios = servicioService.findByAllFilters(provincia, departamento, horario, rangoPrecio);
        }

        
        if (!"".equals(provincia) && !"".equals(departamento) && !"No especificar".equals(horario) && !"0".equals(rangoPrecio) && (nombre != null || !"".equals(nombre))) {
            servicios = servicioService.findAllAndNombre(nombre, provincia, departamento, horario, rangoPrecio);
        }

        if (!"".equals(provincia) && "".equals(departamento) && "No especificar".equals(horario) && "0".equals(rangoPrecio) && (nombre == null || "".equals(nombre))) {
            servicios = servicioService.findByProvincia(provincia);
        }

        if (!"".equals(provincia) && !"".equals(departamento) && "No especificar".equals(horario) && "0".equals(rangoPrecio) && (nombre == null || "".equals(nombre))) {
            servicios = servicioService.findByDepartamento(provincia, departamento);

        }

        if ("".equals(provincia) && "".equals(departamento) && !"No especificar".equals(horario) && "0".equals(rangoPrecio) && (nombre == null || "".equals(nombre))) {
            servicios = servicioService.findByHorario(horario);
        }

        if ("".equals(provincia) && "".equals(departamento) && "No especificar".equals(horario) && !"0".equals(rangoPrecio) && (nombre == null || "".equals(nombre))) {
            servicios = servicioService.findByPrecio(rangoPrecio);
        }

        if (!"".equals(provincia) && !"".equals(departamento) && !"No especificar".equals(horario)) {
            servicios = servicioService.findByProvinciaDepartamentoHorario(provincia, departamento, horario);
        }

        if (!"".equals(provincia) && !"".equals(departamento) && !"0".equals(rangoPrecio)) {
            servicios = servicioService.findByProvinciaDepartamentoPrecio(provincia, departamento, rangoPrecio);
        }

        if (!"".equals(provincia) && !"No especificar".equals(horario) && !"0".equals(rangoPrecio)) {
            servicios = servicioService.findByProvinciaHorarioPrecio(provincia, horario, rangoPrecio);
        }

        if (!"".equals(provincia) && !"No especificar".equals(horario)) {
            servicios = servicioService.findByProvinciaHorario(provincia, horario);
        }

        if (!"".equals(provincia) && !"0".equals(rangoPrecio)) {
            servicios = servicioService.findByProvinciaPrecio(provincia, rangoPrecio);
        }

        if (!"No especificar".equals(horario) && !"0".equals(rangoPrecio)) {
            servicios = servicioService.findByPrecioHorario(rangoPrecio, horario);
        }

        if ("".equals(provincia) && "".equals(departamento) && "No especificar".equals(horario) && "0".equals(rangoPrecio) && (nombre == null || "".equals(nombre))) {
            servicios = servicioService.listarServicios();
        }

      
        if (!"".equals(provincia) && "".equals(departamento) && "No especificar".equals(horario) && "0".equals(rangoPrecio) && (nombre != null || !"".equals(nombre))) {
            servicios = servicioService.findByNombreProvincia(nombre, provincia);
        }

        if ("".equals(provincia) && "".equals(departamento) && !"No especificar".equals(horario) && "0".equals(rangoPrecio) && (nombre != null || !"".equals(nombre))) {
            servicios = servicioService.findByNombreHora(nombre, horario);
        }

        if ("".equals(provincia) && "".equals(departamento) && !"No especificar".equals(horario) && "0".equals(rangoPrecio) && (nombre != null || !"".equals(nombre))) {
            servicios = servicioService.findByNombrePrecio(nombre, horario);
        }

        if (!"".equals(provincia) && "".equals(departamento) && !"No especificar".equals(horario) && !"0".equals(rangoPrecio) && (nombre != null || !"".equals(nombre))) {
            servicios = servicioService.findByNombreProvinciaHorarioPrecio(nombre, provincia, horario, rangoPrecio);
        }

        if ("".equals(provincia) && "".equals(departamento) && !"No especificar".equals(horario) && !"0".equals(rangoPrecio) && (nombre != null || !"".equals(nombre))) {
            servicios = servicioService.findByNombreHorarioPrecio(nombre, horario, rangoPrecio);
        }

        if ("".equals(provincia) && "".equals(departamento) && "No especificar".equals(horario) && "0".equals(rangoPrecio) && (nombre != null || !"".equals(nombre))) {
            servicios = servicioService.findByNombre(nombre);
        }

        if (!"".equals(provincia) && !"".equals(departamento) && !"No especificar".equals(horario) && (nombre != null || !"".equals(nombre))) {
            servicios = servicioService.findByNombreProvinciaDepartamentoHora(nombre, provincia, departamento, horario);
        }

        if (!"".equals(provincia) && !"".equals(departamento) && (nombre != null || !"".equals(nombre))) {
            servicios = servicioService.findByNombreProvinciaDepartamento(nombre, provincia, departamento);
        }

        return servicios;

    }
     */

 /* convertir el codigo en imgen */
    public void modificarUrlImagen(List<Servicio> servicios) throws UnsupportedEncodingException {
        for (Servicio s : servicios) {
            List<DBFile> filess = s.getFiless();
            for (DBFile f : filess) {
                byte[] encodeBase64 = Base64.getEncoder().encode(f.getData());
                String img = new String(encodeBase64, "UTF-8");
                f.setUrl(img);
            }
        }
    }

}
