package np.com.proyecto.dao;

import np.com.proyecto.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Integer> {

    Usuario findByUsername(String username);

    @Query(value = "SELECT username FROM usuario WHERE username =? ", nativeQuery = true)
    public String verificarExistenciaEmail(String email);
    
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE usuario SET nombre=?,apellido=?,provincia=?, departamento=?, username=?, id_rol=? WHERE id_usuario=?", nativeQuery = true)
    public void modificarUsuario(String nombre,String apellido,String provincia,String depertamento, String Username, int id_rol,  int id_usuario);

    @Transactional
    @Modifying
    @Query(value = "UPDATE usuario SET nombre=? WHERE id_usuario=?", nativeQuery = true)
    public void modificarNombreUsuario(String nombre, int id_usuario);

    @Transactional
    @Modifying
    @Query(value = "UPDATE usuario SET apellido=? WHERE id_usuario=?", nativeQuery = true)
    public void modificarApellidoUsuario(String apellido, int id_usuario);

    @Transactional
    @Modifying
    @Query(value = "UPDATE usuario SET provincia=?, departamento=? WHERE id_usuario=?", nativeQuery = true)
    public void modificarProvinciaDepartamentoUsuario(String provincia, String departamento, int id_usuario);

    @Transactional
    @Modifying
    @Query(value = "UPDATE usuario SET username=? WHERE id_usuario=?", nativeQuery = true)
    public void modificarUsernameUsuario(String username, int id_usuario);

    @Transactional
    @Modifying
    @Query(value = "UPDATE usuario SET password=? WHERE id_usuario=?", nativeQuery = true)
    public void modificarPasswordUsuario(String password, int id_usuario);
    
     @Query("SELECT u FROM Usuario u WHERE u.username = ?1")
    public Usuario findByEmail(String email); 
    
    public Usuario findByResetPasswordToken(String token);

}
