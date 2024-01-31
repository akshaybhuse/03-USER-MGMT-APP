package in.akshay.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;



import javax.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmail(String to, String subject, String body) {

		boolean isMailSent = false;

		try {
			MimeMessage createMimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(createMimeMessage);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(createMimeMessage);

			isMailSent = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isMailSent;
	}
}
