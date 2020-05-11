package np.com.proyecto.dao;

import np.com.proyecto.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioDao extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
}
