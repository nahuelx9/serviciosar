package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.domain.Alerta;

public interface AlertaService {
      public List<Alerta> listarAlertas();
    
    public void guardar(Alerta alerta);
    
    public void eliminar(Alerta alerta);
    
    public Alerta encontrarAlerta(Alerta alerta);
}
