package np.com.proyecto.dao;

import java.util.List;
import np.com.proyecto.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioDao extends JpaRepository<Servicio, Long> {
    
    @Modifying
    @Query(value = "UPDATE servicio SET nombre=?,celular=?,whatsapp=?,precio_descripcion=?,horario=?,descripcion=? where id_servicio=?",nativeQuery= true)
   public void actualizarServicio(String nombre, String celular,boolean whatsaap,String precio_descripcion, String horario, String descripcion,int id_servicio);
   
   /*Buscar por precio*/
   @Query(value = "SELECT * FROM servicio WHERE precio IS NULL OR precio <=?", nativeQuery = true)
   public List<Servicio> findByPrecio(String precioRango);
   
   /*Buscar por horario*/
   @Query(value = "SELECT * FROM servicio WHERE horario=?", nativeQuery = true)
   public List<Servicio> findByHorario(String horario);
   
   /*Buscar por provincia*/
   @Query(value = "SELECT * FROM servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia=? GROUP BY id_servicio", nativeQuery = true)
  public  List<Servicio> findByProvincia(String provincia);
   
    /*Buscar por departamento y provincia */
   @Query(value = "SELECT * FROM servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia=? AND usuario.departamento=?  GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByDepartamento(String provincia, String departamento);
   
   
    /*Buscar por todos los filtros*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  usuario.departamento = ? AND  horario = ? AND precio IS NULL OR precio<=? GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByAllFilters(String provincia, String departamento, String horario, String precio);

   
   /*Buscar por provincia,departamento y horario*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  usuario.departamento = ? AND  horario = ?  GROUP BY id_servicio", nativeQuery = true)
    public List<Servicio> findByProvinciaDepartamentoHorario(String provincia, String departamento, String horario);
    
     /*Buscar por provincia departamento y precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  usuario.departamento = ?  AND precio IS NULL OR precio<=? GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByProvinciaDepartamentoPrecio(String provincia, String departamento, String precio);
   
    /*Buscar por provincia horario y precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  horario = ? AND precio IS NULL OR precio<=? GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByProvinciaHorarioPrecio(String provincia, String horario, String precio);
   
     /*Buscar por provincia y horario*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  horario = ?  GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByProvinciaHorario(String provincia, String horario);
   
   
    /*Buscar por provincia y precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?  AND precio IS NULL OR precio<=? GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByProvinciaPrecio(String provincia, String precio);
   
    /*Buscar por precio y horario*/

    /**
     *
     * @param precio
     * @param horario
     * @return
     */

   @Query(value = "SELECT * FROM servicio WHERE  horario =? AND  precio IS NULL OR precio <=?  ", nativeQuery = true)
   public List<Servicio> findByPrecioHorario(String horario,String precio);
   
     /*Buscar por coincidencia del nombre*/
     @Query(value = "SELECT  * FROM  servicio WHERE nombre LIKE %?%", nativeQuery = true)
   public List<Servicio> findByNombre(String nombre);
   
    /*Buscar por todos los filtros  y   el  nombre*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario  AND  usuario.provincia =?   AND  usuario.departamento = ? AND  horario = ? AND precio IS NULL OR precio<=?  AND servicio.nombre LIKE %?%  GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findAllAndName( String provincia, String departamento, String horario,String precio,String nombre);
   
       /*Buscar por   el  nombre, provincia,departamento,hora*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario   AND  usuario.provincia =?   AND  usuario.departamento = ? AND  horario = ? AND servicio.nombre  LIKE %?% GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByNombreProvinciaDepartamentoHora( String provincia, String departamento, String horario, String nombre);
   
      /*Buscar por   el  nombre, provincia,departamento*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario  AND  usuario.provincia =?   AND  usuario.departamento = ? AND   servicio.nombre LIKE %?%  GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByNombreProvinciaDepartamento( String provincia, String departamento,String nombre);
   
   /*Buscar por   el  nombre, provincia*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND servicio.nombre LIKE %?%  AND  usuario.provincia =?  GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByNombreProvincia(String nombre, String provincia);
   
      /*Buscar por   el  nombre,departamento*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND servicio.nombre LIKE %?%     AND  usuario.departamento = ?  GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByNombreDepartamento(String nombre,  String departamento);
   
       /*Buscar por   el  nombre,hora*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario  AND servicio.nombre LIKE %?%    AND  horario = ? GROUP BY id_servicio", nativeQuery = true)
   public List<Servicio> findByNombreHora(String nombre,  String horario);
   
     /*Buscar por  el  nombre, precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario   AND servicio.nombre LIKE %?%  AND precio IS NULL OR precio<=? GROUP BY id_servicio ORDER BY precio desc", nativeQuery = true)
   public List<Servicio> findByNombrePrecio(String nombre, String precio);
   
    /*Buscar por nombre,provincia,horario,precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario  AND  usuario.provincia =?    AND  horario = ? AND precio IS NULL OR precio<=? AND servicio.nombre LIKE %?%  GROUP BY id_servicio ", nativeQuery = true)
   public List<Servicio> findByNombreProvinciaHorarioPrecio( String provincia, String horario,String precio,String nombre);
   
 /*Buscar por nombre,horario,precio*/
   @Query(value = "SELECT   * FROM  servicio WHERE   horario = ? AND precio IS NULL OR precio<=?   AND  nombre LIKE %?%  ", nativeQuery = true)
   public List<Servicio> findByNombreHorarioPrecio(String horario,String precio,String nombre);
   
    /*Buscar por nombre,provincia,precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario   AND  usuario.provincia =?   AND precio IS NULL OR precio<=? AND servicio.nombre LIKE %?%  GROUP BY id_servicio ", nativeQuery = true)
   public List<Servicio> findByNombreProvinciaPrecio( String provincia,String precio,String nombre);
   
   


}
