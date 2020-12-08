package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.domain.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServicioService {

    public List<Servicio> listarServicios();

    public void guardar(Servicio servicio);

    public void eliminar(Servicio servicio);

    public Servicio encontrarServicio(Servicio servicio);

    public void actualizarServicio(String nombre, String celular, boolean whatsaap,String email,String precio, String precio_descripcion, String horario, String descripcion, int id_servicio);

   public Page<Servicio>  findByPrecio(String precioRango,Pageable pageable);

   public Page<Servicio> findByHorario(String horario,Pageable pageable);

    public Page<Servicio> findByProvincia(String provincia,Pageable pageable);

   public Page<Servicio> findByDepartamento(String provincia, String departamento,Pageable pageable);

  public Page<Servicio> findByAllFilters(String provincia, String departamento, String horario, String precio,Pageable pageable);

   public Page<Servicio> findByProvinciaDepartamentoHorario(String provincia, String departamento, String horario,Pageable pageable);

    public Page<Servicio> findByProvinciaDepartamentoPrecio(String provincia, String departamento, String precio,Pageable pageable);

  public Page<Servicio> findByProvinciaHorarioPrecio(String provincia, String horario, String precio,Pageable pageable);

  public Page<Servicio> findByProvinciaHorario(String provincia, String horario,Pageable pageable);

   public Page<Servicio> findByProvinciaPrecio(String provincia, String precio,Pageable pageable);

    public Page<Servicio> findByPrecioHorario(String horario,String precioRango,Pageable pageable);

   public Page<Servicio> findByNombre(String nombre, Pageable pageable);

    public Page<Servicio> findAllAndNombre( String provincia, String departamento, String horario, String precio, String nombre,Pageable pageable);

   public Page<Servicio> findByNombreProvinciaDepartamentoHora(String provincia, String departamento, String horario,String nombre,Pageable pageable);

  public Page<Servicio> findByNombreProvinciaDepartamento(String provincia, String departamento,String nombre,Pageable pageable);

   public Page<Servicio> findByNombreProvincia(String nombre, String provincia,Pageable pageable);

   public Page<Servicio> findByNombreHora(String nombre, String horario,Pageable pageable);

    public Page<Servicio> findByNombrePrecio(String nombre, String precio,Pageable pageable);
    
    public Page<Servicio> findByNombreProvinciaHorarioPrecio(String provincia, String horario,String precio,String nombre,Pageable pageable);
    
    public Page<Servicio> findByNombreHorarioPrecio(String horario,String precio,String nombre,Pageable pageable);
     
     public Page<Servicio> findByNombreProvinciaPrecio(String provincia,String precio,String nombre,Pageable pageable);
      
      Page<Servicio> getAll(Pageable pageable);

}
