package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.domain.Usuario;

public interface UsuarioService {

    public List<Usuario> listarUsuarios();

    public void guardar(Usuario usuario);

    public void eliminar(Usuario usuario);

    public Usuario encontrarUsuario(Usuario usuario);

    public Usuario encontrarUsuarioPorUsername(String username);

    public boolean verificarExistenciaEmail(String email);

}
