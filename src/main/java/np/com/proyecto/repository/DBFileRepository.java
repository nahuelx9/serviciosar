package np.com.proyecto.repository;

import np.com.proyecto.domain.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile, String> {

     @Modifying
    @Query(value = "UPDATE files SET data=?,file_name=?,file_type=? where id=?",nativeQuery= true)
    public void modificarImagen(byte[] data,String file_name,String file_type,String id);
}
