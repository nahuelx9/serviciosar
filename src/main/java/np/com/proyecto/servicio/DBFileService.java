package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.domain.DBFile;

public interface DBFileService {
    
     public List<DBFile> listarDBFiles();
    
    public void guardar(DBFile dbFile);
    
    public void eliminar(DBFile dbFile);
    
    public DBFile encontrarDBFile(DBFile dbFile);
    
    
}
