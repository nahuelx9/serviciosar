package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.domain.Servicio;

public interface ServicioService {
    
    public List<Servicio> listarServicios();
    
    public void guardar(Servicio servicio);
    
    public void eliminar(Servicio servicio);
    
    public Servicio encontrarServicio(Servicio servicio);
}
