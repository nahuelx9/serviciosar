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

    public void modificarNombreUsuario(String nombre, int id_usuario);

    public void modificarApellidoUsuario(String apellido, int id_usuario);

    public void modificarProvinciaDepartamentoUsuario(String provincia, String departamento, int id_usuario);

    public void modificarUsernameUsuario(String username, int id_usuario);

    public void modificarPasswordUsuario(String passwordActual, String password1,String password2, Usuario usuario);

}
