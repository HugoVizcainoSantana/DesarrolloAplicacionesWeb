package daw.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import daw.spring.repository.Email;


@Service("email")
public class EmailService implements Email {

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public void sendEmail(SimpleMailMessage email) {
		mailSender.send(email);		
	}

	

	

}
