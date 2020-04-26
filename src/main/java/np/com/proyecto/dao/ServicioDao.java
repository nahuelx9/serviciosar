package np.com.proyecto.dao;

import np.com.proyecto.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioDao extends JpaRepository<Servicio, Long> {
    
}
