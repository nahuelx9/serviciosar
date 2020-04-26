package np.com.proyecto.servicio;

import java.util.List;
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
    
}
