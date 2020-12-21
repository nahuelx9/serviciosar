package np.com.proyecto.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import np.com.proyecto.dao.RolDao;
import np.com.proyecto.dao.UsuarioDao;
import np.com.proyecto.domain.Rol;
import np.com.proyecto.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Slf4j
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private RolDao rolDao;

    @Override
    @Transactional
    public List<Usuario> listarUsuarios() {
        return (List<Usuario>) usuarioDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Usuario usuario) {
        String idProvincia = usuario.getProvincia();
        switch (idProvincia) {
            case "54":
                usuario.setProvincia("Misiones");
                break;
            case "74":
                usuario.setProvincia("San Luis");
                break;
            case "70":
                usuario.setProvincia("San Juan");
                break;
            case "30":
                usuario.setProvincia("Entre Ríos");
                break;
            case "78":
                usuario.setProvincia("Santa Cruz");
                break;
            case "62":
                usuario.setProvincia("Río Negro");
                break;
            case "26":
                usuario.setProvincia("Chubut");
                break;
            case "14":
                usuario.setProvincia("Córdoba");
                break;
            case "50":
                usuario.setProvincia("Mendoza");
                break;
            case "46":
                usuario.setProvincia("La Rioja");
                break;
            case "10":
                usuario.setProvincia("Catamarca");
                break;
            case "42":
                usuario.setProvincia("La Pampa");
                break;
            case "86":
                usuario.setProvincia("Santiago del Estero");
                break;
            case "18":
                usuario.setProvincia("Corrientes");
                break;
            case "82":
                usuario.setProvincia("Santa Fe");
                break;
            case "90":
                usuario.setProvincia("Tucumán");
                break;
            case "58":
                usuario.setProvincia("Neuquén");
                break;
            case "66":
                usuario.setProvincia("Salta");
                break;
            case "22":
                usuario.setProvincia("Chaco");
                break;
            case "34":
                usuario.setProvincia("Formosa");
                break;
            case "38":
                usuario.setProvincia("Jujuy");
                break;
            case "02":
                usuario.setProvincia("Ciudad Autónoma de Buenos Aires");
                break;
            case "06":
                usuario.setProvincia("Buenos Aires");
                break;
            case "94":
                usuario.setProvincia("Tierra del Fuego, Antártida e Islas del Atlántico Sur");
                break;
        }
        String pass = usuario.encriptarPassword(usuario.getPassword());
        usuario.setPassword(pass);
        usuarioDao.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Usuario usuario) {
        usuarioDao.delete(usuario);
    }

    @Override
    @Transactional
     public void modificarUsuario(Usuario usuario){
         usuarioDao.modificarUsuario(usuario.getNombre(), usuario.getApellido(), usuario.getProvincia(), usuario.getDepartamento(), usuario.getUsername(), usuario.getIdRol(), usuario.getIdUsuario());
     }

    
    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuario(Usuario usuario) {
        return usuarioDao.findById(usuario.getIdUsuario()).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();

        for (Rol rol : rolDao.findAll()) {
            if (rol.getIdRol().equals(usuario.getIdRol())) {
                roles.add(new SimpleGrantedAuthority(rol.getNombre()));
            }
        }
        return new org.springframework.security.core.userdetails.User(usuario.getUsername(), usuario.getPassword(), roles);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuarioPorUsername(String username) {
        Usuario usuario = usuarioDao.findByUsername(username);
        return usuario;
    }

    @Override
    public boolean verificarExistenciaEmail(String email) {
        String emailIngresado = usuarioDao.verificarExistenciaEmail(email);
        if ("".equals(emailIngresado) || emailIngresado == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void modificarNombreUsuario(String nombre, int id_usuario) {
        if (!nombre.isEmpty()) {
            usuarioDao.modificarNombreUsuario(nombre, id_usuario);
        }
    }

    @Override
    public void modificarApellidoUsuario(String apellido, int id_usuario) {
        if (!apellido.isEmpty()) {
            usuarioDao.modificarApellidoUsuario(apellido, id_usuario);
        }
    }

    @Override
    public void modificarProvinciaDepartamentoUsuario(String provincia, String departamento, int id_usuario) {
        usuarioDao.modificarProvinciaDepartamentoUsuario(provincia, departamento, id_usuario);
    }

    @Override
    public void modificarUsernameUsuario(String username, int id_usuario) {
        usuarioDao.modificarUsernameUsuario(username, id_usuario);
    }

    @Override
    public int modificarPasswordUsuario(String passwordActual, String password1, String password2, Usuario usuario) {
        int error = 0;
        if (!passwordActual.isEmpty() && !password1.isEmpty() && !password2.isEmpty() && password1.length() >= 8 && password2.length() >= 8) {
            if (usuario.compararPassword(passwordActual, usuario.getPassword())) {
                if (password1.equals(password2)) {
                    int idFinal = Math.toIntExact(usuario.getIdUsuario());
                    String encriptada = usuario.encriptarPassword(password1);
                    usuarioDao.modificarPasswordUsuario(encriptada, idFinal);
                } else {
                    error = 3;//las contraseñas son  distintas
                }
            } else {
                error = 2;//Contraseña icorrecta
            }
        } else {
            error = 1;//Campos vacios
        }
        return error;
    }

    @Override
    public void updateResetPasswordToken(String token, String email){
        Usuario usuario = usuarioDao.findByEmail(email);
        if (usuario != null) {
            usuario.setResetPasswordToken(token);
            usuarioDao.save(usuario);
        }
    }

    /**
     *
     * @param token
     * @return
     */
    @Override
    public Usuario getByResetPasswordToken(String token) {
        return usuarioDao.findByResetPasswordToken(token);
    }

    /**
     *
     * @param usuario
     * @param newPassword
     */
    @Override
    public void updatePassword(Usuario usuario, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);

        usuario.setResetPasswordToken(null);
        usuarioDao.save(usuario);
    }
    
   

}
