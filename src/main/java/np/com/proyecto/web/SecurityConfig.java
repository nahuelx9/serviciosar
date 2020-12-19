package np.com.proyecto.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncode() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncode());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/agregarServicio/**", "/guardarServicio/**", "/guardarServicioEditado","/serviciosUsuario/**", "/editarServicio/**", "/eliminarServicio", "/editarImagenes/**",
                        "/modificarImagen","/eliminarImagen", "/datosUsuario/**","/modificarNombreUsuario","/modificarApellidoUsuario","/modificarProvinciaDepartamentoUsuario",
                        "/modificarEmailUsuario","/modificarContraseñaUsuario","/eliminarUsuario").hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error.html")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login-logout.html")
                .permitAll()
                .and()
                .rememberMe().key("uniqueAndSecret")
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/expired")
                ;

    }

}
