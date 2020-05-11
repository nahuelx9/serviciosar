package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.dao.RolDao;
import np.com.proyecto.domain.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolServiceImpl implements RolService {
    
    @Autowired
    private RolDao rolDao;

    @Override
    @Transactional(readOnly = true)
    public List<Rol> listarRols() {
        return (List<Rol>) rolDao.findAll();
    }

    @Override
    public void guardar(Rol rol) {
        rolDao.save(rol);
    }

    @Override
    public void eliminar(Rol rol) {
        rolDao.delete(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public Rol encontrarRol(Rol rol) {
       return rolDao.findById(rol.getIdRol()).orElse(null);
    }
    
  
}
