package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.domain.Buzon;

public interface BuzonService {
       public List<Buzon> listarBuzon();
    
    public void guardar(Buzon buzon);
    
    public void eliminar(Buzon buzon);
    
    public Buzon encontrarBuzon(Buzon buzon);
}
