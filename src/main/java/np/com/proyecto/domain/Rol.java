package np.com.proyecto.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name = "rol")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRol;

    @NotEmpty
    private String nombre;
    
    @ManyToMany
    @JoinColumn(name = "id_rol")
    private List<Usuario> usuarios;

}
