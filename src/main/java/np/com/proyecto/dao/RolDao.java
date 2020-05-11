package np.com.proyecto.dao;

import np.com.proyecto.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolDao extends JpaRepository<Rol, Long> {
    
}
