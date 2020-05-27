package np.com.proyecto.dao;

import np.com.proyecto.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioDao extends JpaRepository<Servicio, Long> {
    
}
