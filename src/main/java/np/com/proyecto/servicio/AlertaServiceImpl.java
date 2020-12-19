
package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.dao.AlertaDao;
import np.com.proyecto.domain.Alerta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertaServiceImpl implements AlertaService {
    
       @Autowired
    private AlertaDao alertaDao;

   @Override
    public List<Alerta> listarAlertas() {
           return (List<Alerta>) alertaDao.findAll();
    }

    @Override
    public void guardar(Alerta alerta) {
        alertaDao.save(alerta);
    }

    @Override
    public void eliminar(Alerta alerta) {
      alertaDao.delete(alerta);
    }

    @Override
    @Transactional
    public Alerta encontrarAlerta(Alerta alerta) {
      return alertaDao.findById(alerta.getIdAlerta()).orElse(null);
    }

}
