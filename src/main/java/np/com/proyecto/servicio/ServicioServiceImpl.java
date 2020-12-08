package np.com.proyecto.servicio;

import java.util.List;
import javax.persistence.Query;
import np.com.proyecto.dao.ServicioDao;
import np.com.proyecto.domain.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioServiceImpl implements ServicioService {
    
    
    @Autowired
    private ServicioDao servicioDao;

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> listarServicios() {
       return (List<Servicio>) servicioDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Servicio servicio) {
        servicioDao.save(servicio);
    }

    @Override
    @Transactional
    public void eliminar(Servicio servicio) {
        servicioDao.delete(servicio);
    }

    @Override
    @Transactional(readOnly = true)
    public Servicio encontrarServicio(Servicio servicio) {
        return servicioDao.findById(servicio.getIdServicio()).orElse(null);
    }

    @Override
    @Transactional
   public void actualizarServicio(String nombre, String celular,boolean whatsaap,String email,String precio,String precio_descripcion, String horario, String descripcion,int id_servicio) {
        servicioDao.actualizarServicio(nombre,celular,whatsaap,email,precio,precio_descripcion,horario,descripcion,id_servicio);
    }

    @Override
    @Transactional(readOnly = true)
   public Page<Servicio> findByPrecio(String precioRango,Pageable pageable) {
       return servicioDao.findByPrecio(precioRango,pageable);
    }

    @Override
    @Transactional(readOnly = true)
   public Page<Servicio> findByHorario(String horario,Pageable pageable) {
        return servicioDao.findByHorario(horario,pageable);
        
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Servicio> findByProvincia(String provincia,Pageable pageable) {
        return servicioDao.findByProvincia(provincia,pageable);
    }

    @Override
    @Transactional(readOnly = true)
   public Page<Servicio> findByDepartamento(String provincia,String departamento,Pageable pageable) {
        return servicioDao.findByDepartamento(provincia,departamento,pageable);
    }

    @Override
    @Transactional(readOnly = true)
  public Page<Servicio> findByAllFilters(String provincia, String departamento, String horario, String precio,Pageable pageable) {
        return servicioDao.findByAllFilters(provincia, departamento, horario, precio,pageable);
    }

    @Override
 public Page<Servicio> findByProvinciaDepartamentoHorario(String provincia, String departamento, String horario,Pageable pageable) {
       return servicioDao.findByProvinciaDepartamentoHorario(provincia, departamento, horario,pageable);
    }

    @Override
   public Page<Servicio> findByProvinciaDepartamentoPrecio(String provincia, String departamento, String precio,Pageable pageable) {
        return servicioDao.findByProvinciaDepartamentoPrecio(provincia, departamento, precio,pageable);
    }

    @Override
   public Page<Servicio> findByProvinciaHorarioPrecio(String provincia, String horario, String precio,Pageable pageable) {
        return servicioDao.findByProvinciaHorarioPrecio(provincia,horario, precio,pageable);
    }

    @Override
    public Page<Servicio>findByProvinciaHorario(String provincia, String horario,Pageable pageable) {
        return servicioDao.findByProvinciaHorario(provincia, horario,pageable);
    }

    @Override
  public Page<Servicio> findByProvinciaPrecio(String provincia, String precio,Pageable pageable) {
        return servicioDao.findByProvinciaPrecio(provincia, precio,pageable);
    }

    @Override
  public Page<Servicio> findByPrecioHorario( String horario,String precioRango,Pageable pageable) {
        return servicioDao.findByPrecioHorario(horario,precioRango,pageable);
    }

    @Override
   public Page<Servicio> findByNombre(String nombre, Pageable pageable) {
        return servicioDao.findByNombre(nombre,pageable);
    }

    @Override
   public Page<Servicio> findAllAndNombre( String provincia, String departamento, String horario, String precio,String nombre,Pageable pageable) {
        return servicioDao.findAllAndName( provincia, departamento, horario, precio,nombre,pageable);
    }

    @Override
  public Page<Servicio> findByNombreProvinciaDepartamentoHora( String provincia, String departamento, String horario,String nombre,Pageable pageable) {
     return servicioDao.findByNombreProvinciaDepartamentoHora( provincia, departamento, horario,nombre,pageable);
    }

    @Override
  public Page<Servicio> findByNombreProvinciaDepartamento(String provincia, String departamento,String nombre,Pageable pageable) {
        return servicioDao.findByNombreProvinciaDepartamento( provincia, departamento,nombre,pageable);
    }

    @Override
 public Page<Servicio> findByNombreProvincia(String nombre, String provincia,Pageable pageable) {
      return servicioDao.findByNombreProvincia(nombre, provincia,pageable);
    }

    @Override
 public Page<Servicio> findByNombreHora(String nombre, String horario,Pageable pageable) {
         return servicioDao.findByNombreHora(nombre, horario,pageable);
    }

    @Override
  public Page<Servicio> findByNombrePrecio(String nombre, String precio,Pageable pageable) {
         return servicioDao.findByNombrePrecio(nombre, precio,pageable);
    }

    @Override
   public Page<Servicio> findByNombreProvinciaHorarioPrecio( String provincia, String horario, String precio,String nombre,Pageable pageable) {
        return servicioDao.findByNombreProvinciaHorarioPrecio( provincia, horario, precio,nombre,pageable);
    }

    @Override
  public Page<Servicio> findByNombreHorarioPrecio(String horario, String precio,String nombre,Pageable pageable) {
           return servicioDao.findByNombreHorarioPrecio( horario, precio,nombre,pageable);
    }

    @Override
  public Page<Servicio> findByNombreProvinciaPrecio( String provincia, String precio,String nombre,Pageable pageable) {
      return servicioDao.findByNombreProvinciaPrecio( provincia, precio,nombre,pageable);
    }

    @Override
    public Page<Servicio> getAll(Pageable pageable) {
            return servicioDao.findAll(pageable);
    }


    
    
    
}
