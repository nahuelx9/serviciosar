package np.com.proyecto.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "servicio")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idServicio;
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String celular;
    @NotEmpty
    private String email;
    private String precio;
    @NotEmpty
    private String precioDescripcion;
    @NotEmpty
    private String horario;
    @NotEmpty
    @Size(max = 365)
    @Size(min = 70)
    private String descripcion;

}
