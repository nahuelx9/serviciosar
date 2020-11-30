package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.domain.Servicio;

public interface ServicioService {

    public List<Servicio> listarServicios();

    public void guardar(Servicio servicio);

    public void eliminar(Servicio servicio);

    public Servicio encontrarServicio(Servicio servicio);

    public void actualizarServicio(String nombre, String celular, boolean whatsaap, String precio_descripcion, String horario, String descripcion, int id_servicio);

    public List<Servicio> findByPrecio(String precioRango);

    public List<Servicio> findByHorario(String horario);

    public List<Servicio> findByProvincia(String provincia);

    public List<Servicio> findByDepartamento(String provincia, String departamento);

    public List<Servicio> findByAllFilters(String provincia, String departamento, String horario, String precio);

    public List<Servicio> findByProvinciaDepartamentoHorario(String provincia, String departamento, String horario);

    public List<Servicio> findByProvinciaDepartamentoPrecio(String provincia, String departamento, String precio);

    public List<Servicio> findByProvinciaHorarioPrecio(String provincia, String horario, String precio);

    public List<Servicio> findByProvinciaHorario(String provincia, String horario);

    public List<Servicio> findByProvinciaPrecio(String provincia, String precio);

    public List<Servicio> findByPrecioHorario(String horario,String precioRango);

    public List<Servicio> findByNombre(String nombre);

    public List<Servicio> findAllAndNombre( String provincia, String departamento, String horario, String precio, String nombre);

    public List<Servicio> findByNombreProvinciaDepartamentoHora(String provincia, String departamento, String horario,String nombre);

    public List<Servicio> findByNombreProvinciaDepartamento(String provincia, String departamento,String nombre);

    public List<Servicio> findByNombreProvincia(String nombre, String provincia);

    public List<Servicio> findByNombreDepartamento(String nombre, String departamento);

    public List<Servicio> findByNombreHora(String nombre, String horario);

    public List<Servicio> findByNombrePrecio(String nombre, String precio);
    
    public List<Servicio> findByNombreProvinciaHorarioPrecio(String provincia, String horario,String precio,String nombre);
    
     public List<Servicio> findByNombreHorarioPrecio(String horario,String precio,String nombre);
     
      public List<Servicio> findByNombreProvinciaPrecio(String provincia,String precio,String nombre);

}
