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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @NotEmpty
    @Size(max = 50)
    private String nombre;

    @NotEmpty
    @Size(max = 50)
    private String apellido;

    @NotEmpty
    private String provincia;

    @NotEmpty
    private String departamento;

    @NotEmpty
    @Email
    @Size(max = 50)
    private String username;

    @NotEmpty
    @Size(min = 8)
    private String password;

    @Column(name = "id_rol")
    private Integer idRol = 1;

    private Date fecha = new Date();

    @Column(name = "reset_password_token")
    String resetPasswordToken;

    @OneToMany
    @JoinColumn(name = "id_usuario")
    private List<Servicio> servicios;

    public String encriptarPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public boolean compararPassword(String passwordNew, String passwordOld) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(passwordNew, passwordOld);
    }

}
