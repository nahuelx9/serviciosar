package np.com.proyecto.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "alerta")
public class Alerta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAlerta;
    private String tipo;
    @Size(max = 255)
     @Column(name = "text_reporte")
    private String textReporte;
  @Column(name = "id_servicio")
    private Integer idServicio;
  
    private Date fecha = new Date();

}
