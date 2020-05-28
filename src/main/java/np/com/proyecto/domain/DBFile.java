package np.com.proyecto.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "files")
public class DBFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

     private String url = null;
    
    @Column(name = "id_servicio")
    private int idServicio = 0;
    
   

    public DBFile() {
    }

    public DBFile(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

}
