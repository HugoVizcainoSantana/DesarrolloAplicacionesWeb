package daw.spring.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import daw.spring.model.User;
import daw.spring.service.EmailService;
import daw.spring.service.UserService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordController {
	
    private final Logger log = LoggerFactory.getLogger(getClass());

	
	 private  BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;

	
	@RequestMapping(value = "/forgotPasword")
	public String displayForgotPasswordPage() {
		return "forgotPasword";
    }
	@RequestMapping(value = "/forgotPasword", method = RequestMethod.POST)
	public String processForgotPasswordForm(Model model, @RequestParam("email") String userEmail, HttpServletRequest request) {

		log.info("Entra en el post");
		// Lookup user in database by e-mail
		Optional<User> optional = userService.findUserByEmail(userEmail);

		if (!optional.isPresent()) {
			log.info("No existe el email ha verificado en la base de datos");
			model.addAttribute("errorMessage", "No existe ese email en nuestra base de datos");
		} else {
			
			// Generate random 36-character string token for reset password 
			User user = optional.get();
			user.setResetToken(UUID.randomUUID().toString());

			// Save token to database
			userService.saveUser(user);

			String appUrl = request.getScheme() + "://" + request.getServerName();
			
			// Email message
			SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
			passwordResetEmail.setFrom("oncontrolhome@gmail.com");
			passwordResetEmail.setTo(user.getEmail());
			passwordResetEmail.setSubject("Password Reset Request");
			passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
					+ "/reset?token=" + user.getResetToken());
			
			emailService.sendEmail(passwordResetEmail);

			// Add success message to view
			model.addAttribute("successMessage", "El link para resetear la contraseña ha sido enviado a " + userEmail);
		}

		//modelAndView.setViewName("forgotPassword");
		return "forgotPasword";

	}

	// Display form to reset password
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String displayResetPasswordPage(Model model, @RequestParam("token") String token) {
		
		Optional<User> user = userService.findUserByResetToken(token);

		if (user.isPresent()) { // Token found in DB
			model.addAttribute("resetToken", token);
		} else { // Token not found in DB
			model.addAttribute("errorMessage", "Oops!  Esto es un password incorrecto.");
		}

		//modelAndView.setViewName("resetPassword");
		return "resetPasword";
	}

	// Process reset password form
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String setNewPassword(Model model, @RequestParam Map<String, String> requestParams, RedirectAttributes redir) {

		// Find the user associated with the reset token
		Optional<User> user = userService.findUserByResetToken(requestParams.get("token"));

		// This should always be non-null but we check just in case
		if (user.isPresent()) {
			
			User resetUser = user.get(); 
            
			// Set new password    
            resetUser.setPasswordHash(encoder.encode(requestParams.get("password")));
            
			// Set the reset token to null so it cannot be used again
			resetUser.setResetToken(null);

			// Save user
			userService.saveUser(resetUser);

			// In order to set a model attribute on a redirect, we must use
			// RedirectAttributes
			redir.addFlashAttribute("successMessage", "You have successfully reset your password.  You may now login.");

			//modelAndView.setViewName("redirect:login");
			return "redirect:login";
			
		} else {
			model.addAttribute("errorMessage", "Oops!  Este password es invalido.");
			//modelAndView.setViewName("resetPassword");	
		}
		
		return "resetPasword";
   }
   /*
   	Para poder enviar el emial por gmail tenog que crear una cuenta para poder hacerlo yq
   	que hay riesgo de que puede ser vulnerada https://www.google.com/settings/security/lesssecureapps
   	Por lo demas parece que funciona bien todo
   */
    // Going to reset page without a token redirects to login page
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMissingParams(MissingServletRequestParameterException ex) {
		return "redirect:login";
	}
}
