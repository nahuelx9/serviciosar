package np.com.proyecto.servicio;

import java.util.List;
import java.util.Optional;
import np.com.proyecto.domain.DBFile;

public interface DBFileService {
    
     public List<DBFile> listarDBFiles();
    
    public void guardar(DBFile dbFile);
    
    public void eliminar(DBFile dbFile);
    
    public DBFile encontrarDBFile(DBFile dbFile);
    
    public void modificarImagen(byte[] data,String file_name,String file_type,String id);
    
        public DBFile encontrarDBFileById(String id);
}
