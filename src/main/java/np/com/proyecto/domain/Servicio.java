package np.com.proyecto.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "servicio")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idServicio;
    @Size( max=50)
    @NotEmpty
    private String nombre;
    
    @NotEmpty
    @Pattern(regexp = "[\\s]*[0-9]*[1-9]+", message="El celular ingresado es incorrecto")
    private String celular;
    
    private boolean whatsapp;
    
    @NotEmpty
    @Email
    private String email;
    
     @NotEmpty
    private String categoria;
    
     @Size(max=5)
    private String precio;
    
    
    @NotEmpty
    private String precioDescripcion;
    @NotEmpty
    private String horario;
    
    @NotEmpty
    @Size(min = 92, max=365, message="La descripcion debe contener entre 92 y 365 caracteres" )
    private String descripcion;
    
    
    @Column(name = "id_usuario")
    private Integer idUsuario = 0;
    
     private Date fecha  = new Date();
    
    @OneToMany
    @JoinColumn(name = "id_servicio")
    private List<DBFile> filess;
    
    
    
}
