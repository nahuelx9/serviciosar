package np.com.proyecto.dao;

import java.util.List;
import np.com.proyecto.domain.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioDao extends JpaRepository<Servicio, Integer> {
    
    @Modifying
    @Query(value = "UPDATE servicio SET nombre=?,celular=?,whatsapp=?,email=?,precio=?,precio_descripcion=?,horario=?,descripcion=? where id_servicio=?",nativeQuery= true)
   public void actualizarServicio(String nombre, String celular,boolean whatsaap,String email,String precio,String precio_descripcion, String horario, String descripcion,int id_servicio);
   
   /*Buscar por precio*/
   @Query(value = "SELECT * FROM servicio WHERE precio IS NULL OR precio <=?", nativeQuery = true)
  public Page<Servicio> findByPrecio(String precioRango,Pageable pageable);
   
   /*Buscar por horario*/
   @Query(value = "SELECT * FROM servicio WHERE horario=?", nativeQuery = true)
  public Page<Servicio> findByHorario(String horario,Pageable pageable);
   
   /*Buscar por provincia*/
   @Query(value = "SELECT * FROM servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia=? GROUP BY id_servicio", nativeQuery = true)
  public Page<Servicio> findByProvincia(String provincia,Pageable pageable);
   
    /*Buscar por departamento y provincia */
   @Query(value = "SELECT * FROM servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia=? AND usuario.departamento=?  GROUP BY id_servicio", nativeQuery = true)
  public Page<Servicio> findByDepartamento(String provincia, String departamento,Pageable pageable);
   
   
    /*Buscar por todos los filtros*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  usuario.departamento = ? AND  horario = ? AND precio IS NULL OR precio<=? GROUP BY id_servicio", nativeQuery = true)
  public Page<Servicio> findByAllFilters(String provincia, String departamento, String horario, String precio,Pageable pageable);

   
   /*Buscar por provincia,departamento y horario*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  usuario.departamento = ? AND  horario = ?  GROUP BY id_servicio", nativeQuery = true)
   public Page<Servicio> findByProvinciaDepartamentoHorario(String provincia, String departamento, String horario,Pageable pageable);
    
     /*Buscar por provincia departamento y precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  usuario.departamento = ?  AND precio IS NULL OR precio<=? GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findByProvinciaDepartamentoPrecio(String provincia, String departamento, String precio,Pageable pageable);
   
    /*Buscar por provincia horario y precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  horario = ? AND precio IS NULL OR precio<=? GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findByProvinciaHorarioPrecio(String provincia, String horario, String precio,Pageable pageable);
   
     /*Buscar por provincia y horario*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?   AND  horario = ?  GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findByProvinciaHorario(String provincia, String horario,Pageable pageable);
   
   
    /*Buscar por provincia y precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND usuario.provincia =?  AND precio IS NULL OR precio<=? GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findByProvinciaPrecio(String provincia, String precio,Pageable pageable);
   
    /*Buscar por precio y horario*/

    /**
     *
     * @param precio
     * @param horario
     * @return
     */

   @Query(value = "SELECT * FROM servicio WHERE  horario =? AND  precio IS NULL OR precio <=?  ", nativeQuery = true)
 public Page<Servicio> findByPrecioHorario(String horario,String precio,Pageable pageable);
   
     /*Buscar por coincidencia del nombre*/
     @Query(value = "SELECT  * FROM  servicio WHERE nombre LIKE %?%", nativeQuery = true)
  public Page<Servicio> findByNombre(String nombre, Pageable pageable);
   
    /*Buscar por todos los filtros  y   el  nombre*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario  AND  usuario.provincia =?   AND  usuario.departamento = ? AND  horario = ? AND precio IS NULL OR precio<=?  AND servicio.nombre LIKE %?%  GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findAllAndName( String provincia, String departamento, String horario,String precio,String nombre,Pageable pageable);
   
       /*Buscar por   el  nombre, provincia,departamento,hora*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario   AND  usuario.provincia =?   AND  usuario.departamento = ? AND  horario = ? AND servicio.nombre  LIKE %?% GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findByNombreProvinciaDepartamentoHora( String provincia, String departamento, String horario, String nombre,Pageable pageable);
   
      /*Buscar por   el  nombre, provincia,departamento*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario  AND  usuario.provincia =?   AND  usuario.departamento = ? AND   servicio.nombre LIKE %?%  GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findByNombreProvinciaDepartamento( String provincia, String departamento,String nombre,Pageable pageable);
   
   /*Buscar por   el  nombre, provincia*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND servicio.nombre LIKE %?%  AND  usuario.provincia =?  GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findByNombreProvincia(String nombre, String provincia,Pageable pageable);
   
      /*Buscar por   el  nombre,departamento*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario AND servicio.nombre LIKE %?%     AND  usuario.departamento = ?  GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findByNombreDepartamento(String nombre,  String departamento,Pageable pageable);
   
       /*Buscar por   el  nombre,hora*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario  AND servicio.nombre LIKE %?%    AND  horario = ? GROUP BY id_servicio", nativeQuery = true)
 public Page<Servicio> findByNombreHora(String nombre,  String horario,Pageable pageable);
   
     /*Buscar por  el  nombre, precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario   AND servicio.nombre LIKE %?%  AND precio IS NULL OR precio<=? GROUP BY id_servicio ORDER BY precio desc", nativeQuery = true)
 public Page<Servicio> findByNombrePrecio(String nombre, String precio,Pageable pageable);
   
    /*Buscar por nombre,provincia,horario,precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario  AND  usuario.provincia =?    AND  horario = ? AND precio IS NULL OR precio<=? AND servicio.nombre LIKE %?%  GROUP BY id_servicio ", nativeQuery = true)
 public Page<Servicio> findByNombreProvinciaHorarioPrecio( String provincia, String horario,String precio,String nombre,Pageable pageable);
   
 /*Buscar por nombre,horario,precio*/
   @Query(value = "SELECT   * FROM  servicio WHERE   horario = ? AND precio IS NULL OR precio<=?   AND  nombre LIKE %?%  ", nativeQuery = true)
 public Page<Servicio> findByNombreHorarioPrecio(String horario,String precio,String nombre,Pageable pageable);
   
    /*Buscar por nombre,provincia,precio*/
   @Query(value = "SELECT   * FROM  servicio INNER JOIN usuario ON servicio.id_usuario = usuario.id_usuario   AND  usuario.provincia =?   AND precio IS NULL OR precio<=? AND servicio.nombre LIKE %?%  GROUP BY id_servicio ", nativeQuery = true)
 public Page<Servicio> findByNombreProvinciaPrecio( String provincia,String precio,String nombre,Pageable pageable);
   
   
   


}
