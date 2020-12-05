package np.com.proyecto.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class modificarPassword implements Serializable {
        private static final long serialVersionUID = 1L;
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idModificarPassword;

   @NotEmpty(message="Error: campo nulo o incorrecto")
   private  String passwordOld;
   
    @NotEmpty(message="Error: campo nulo o incorrecto")
   private String passwordNew;
   
     @NotEmpty(message="Error: campo nulo o incorrecto")
   private String passwordNew2;
}
