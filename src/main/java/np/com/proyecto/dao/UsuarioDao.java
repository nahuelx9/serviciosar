package np.com.proyecto.dao;

import np.com.proyecto.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    
    @Query(value="SELECT username FROM usuario WHERE username =? ", nativeQuery=true)
    public String verificarExistenciaEmail(String email);
}
