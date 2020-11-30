package np.com.proyecto.servicio;

import java.util.List;
import javax.persistence.Query;
import np.com.proyecto.dao.ServicioDao;
import np.com.proyecto.domain.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
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
   public void actualizarServicio(String nombre, String celular,boolean whatsaap,String precio_descripcion, String horario, String descripcion,int id_servicio) {
        servicioDao.actualizarServicio(nombre,celular,whatsaap,precio_descripcion,horario,descripcion,id_servicio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findByPrecio(String precioRango) {
       return servicioDao.findByPrecio(precioRango);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findByHorario(String horario) {
        return servicioDao.findByHorario(horario);
        
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findByProvincia(String provincia) {
        return servicioDao.findByProvincia(provincia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findByDepartamento(String provincia,String departamento) {
        return servicioDao.findByDepartamento(provincia,departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findByAllFilters(String provincia, String departamento, String horario, String precio) {
        return servicioDao.findByAllFilters(provincia, departamento, horario, precio);
    }

    @Override
    public List<Servicio> findByProvinciaDepartamentoHorario(String provincia, String departamento, String horario) {
       return servicioDao.findByProvinciaDepartamentoHorario(provincia, departamento, horario);
    }

    @Override
    public List<Servicio> findByProvinciaDepartamentoPrecio(String provincia, String departamento, String precio) {
        return servicioDao.findByProvinciaDepartamentoPrecio(provincia, departamento, precio);
    }

    @Override
    public List<Servicio> findByProvinciaHorarioPrecio(String provincia, String horario, String precio) {
        return servicioDao.findByProvinciaHorarioPrecio(provincia,horario, precio);
    }

    @Override
    public List<Servicio> findByProvinciaHorario(String provincia, String horario) {
        return servicioDao.findByProvinciaHorario(provincia, horario);
    }

    @Override
    public List<Servicio> findByProvinciaPrecio(String provincia, String precio) {
        return servicioDao.findByProvinciaPrecio(provincia, precio);
    }

    @Override
    public List<Servicio> findByPrecioHorario( String horario,String precioRango) {
        return servicioDao.findByPrecioHorario(horario,precioRango);
    }

    @Override
    public List<Servicio> findByNombre(String nombre) {
        return servicioDao.findByNombre(nombre);
    }

    @Override
    public List<Servicio> findAllAndNombre( String provincia, String departamento, String horario, String precio,String nombre) {
        return servicioDao.findAllAndName( provincia, departamento, horario, precio,nombre);
    }

    @Override
    public List<Servicio> findByNombreProvinciaDepartamentoHora( String provincia, String departamento, String horario,String nombre) {
     return servicioDao.findByNombreProvinciaDepartamentoHora( provincia, departamento, horario,nombre);
    }

    @Override
    public List<Servicio> findByNombreProvinciaDepartamento(String provincia, String departamento,String nombre) {
        return servicioDao.findByNombreProvinciaDepartamento( provincia, departamento,nombre);
    }

    @Override
    public List<Servicio> findByNombreProvincia(String nombre, String provincia) {
      return servicioDao.findByNombreProvincia(nombre, provincia);
    }

    @Override
    public List<Servicio> findByNombreDepartamento(String nombre, String departamento) {
        return servicioDao.findByNombreDepartamento(nombre, departamento);
    }

    @Override
    public List<Servicio> findByNombreHora(String nombre, String horario) {
         return servicioDao.findByNombreHora(nombre, horario);
    }

    @Override
    public List<Servicio> findByNombrePrecio(String nombre, String precio) {
         return servicioDao.findByNombrePrecio(nombre, precio);
    }

    @Override
    public List<Servicio> findByNombreProvinciaHorarioPrecio( String provincia, String horario, String precio,String nombre) {
        return servicioDao.findByNombreProvinciaHorarioPrecio( provincia, horario, precio,nombre);
    }

    @Override
    public List<Servicio> findByNombreHorarioPrecio(String horario, String precio,String nombre) {
           return servicioDao.findByNombreHorarioPrecio( horario, precio,nombre);
    }

    @Override
    public List<Servicio> findByNombreProvinciaPrecio( String provincia, String precio,String nombre) {
      return servicioDao.findByNombreProvinciaPrecio( provincia, precio,nombre);
    }

    
    
    
}
