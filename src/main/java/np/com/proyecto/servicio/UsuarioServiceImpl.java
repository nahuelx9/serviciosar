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
            roles.add(new SimpleGrantedAuthority(rol.getNombre()));
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
       if("".equals(emailIngresado) || emailIngresado ==null){
           return true;
       }else{
           return false;
       }
    }
}
