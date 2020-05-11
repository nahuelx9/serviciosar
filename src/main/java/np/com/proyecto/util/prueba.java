package np.com.proyecto.util;

import np.com.proyecto.domain.Usuario;

public class prueba {
    public static void main(String[] args) {
        Usuario usuario = new Usuario();
        Long id = usuario.getIdUsuario();
        System.out.println(id);
    }
}
