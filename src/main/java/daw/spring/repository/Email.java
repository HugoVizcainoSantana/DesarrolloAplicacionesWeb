package daw.spring.repository;

import org.springframework.mail.SimpleMailMessage;

public interface Email {

	public void sendEmail(SimpleMailMessage email);
	
}
