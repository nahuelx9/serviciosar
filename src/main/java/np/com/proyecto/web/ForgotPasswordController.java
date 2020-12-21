package np.com.proyecto.web;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import np.com.proyecto.domain.Usuario;
import np.com.proyecto.servicio.UsuarioService;
import np.com.proyecto.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForgotPasswordController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        usuarioService.updateResetPasswordToken(token, email);
        String resetPasswordLink = Util.getSiteURL(request) + "/reset_password?token=" + token;
        try {
            sendEmail(email, resetPasswordLink);
            model.addAttribute("enlaceEnviado", true);
        } catch (MessagingException ex) {
            model.addAttribute("enlaceEnviado", false);
        }
        return "forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("nperea641@gmail.com", "Servicios.com Support");
        helper.setTo(recipientEmail);

        String subject = "Enlace para restablecer la contraseña";

        String content = "<p>Hola,</p>"
                + "<p>Solicito el restablecimiento de contraseña.</p>"
                + "<p>Haga click en el siguiente link para obtener una nueva contraseña:</p>"
                + "<p><a href=\"" + link + "\">Restablecer contraseña</a></p>"
                + "<br>"
                + "<p>Ignore este mensaje si ya recodordo su contraseña, "
                + "o no ha realizado esta  solicitud.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Usuario usuario = usuarioService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (usuario == null) {
            return "redirect:/tokenInvalido";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Usuario usuario = usuarioService.getByResetPasswordToken(token);

        if (usuario == null) {
          return "redirect:/tokenInvalido";
        } else {
            usuarioService.updatePassword(usuario, password);

            model.addAttribute("passwordRestablecida", true);
        }

        return "redirect:/passwordRestablecida";
    }

    @RequestMapping("/tokenInvalido")
    public String tokenInvalido(Usuario usuario, Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("tokenInvalido", true);
        return "index";
    }

      @RequestMapping("/passwordRestablecida")
    public String passwordRestablecida(Usuario usuario, Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            usuario = usuarioService.encontrarUsuarioPorUsername(user.getUsername());
            model.addAttribute("usuario", usuario);
            model.addAttribute("id", usuario.getIdUsuario());
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("passwordRestablecida", true);
        return "index";
    }
}
