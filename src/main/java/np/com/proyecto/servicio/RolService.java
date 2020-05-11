package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.domain.Rol;

public interface RolService {
    
    public List<Rol> listarRols();
    
    public void guardar(Rol rol);
    
    public void eliminar(Rol rol);
    
    public Rol encontrarRol(Rol rol);
}
