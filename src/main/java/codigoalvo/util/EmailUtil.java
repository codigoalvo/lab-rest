package codigoalvo.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class EmailUtil {

	private static final Logger LOG = Logger.getLogger(EmailUtil.class);

	public static boolean sendMail(String destinatario, String assunto, String corpoHtml) {

		Properties mailProps = new Properties();
		// Set properties required to connect to Gmail's SMTP server
		mailProps.put("mail.smtp.host", Globals.getProperty("MAIL_SMTP_HOST"));
		mailProps.put("mail.smtp.port", Globals.getProperty("MAIL_SMTP_PORT"));
		mailProps.put("mail.smtp.auth", Globals.getProperty("MAIL_SMTP_AUTH"));
		mailProps.put("mail.smtp.starttls.enable", Globals.getProperty("MAIL_SMTP_STARTTLS_ENABLE"));

		// Create a username-password authenticator to authenticate SMTP session
		Authenticator authenticator = new Authenticator() {
			// override the getPasswordAuthentication method
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Globals.getProperty("EMAIL_REMETENTE"), Globals.getProperty("EMAIL_SENHA"));
			}
		};

		Session session = Session.getDefaultInstance(mailProps, authenticator);
		try {
			final MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Globals.getProperty("EMAIL_REMETENTE")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			message.setSubject(assunto);
			message.setContent(corpoHtml, "text/html" );
			LOG.debug("Enviando email para: "+destinatario);
			Transport.send(message);
			LOG.debug("Email enviado com sucesso para: "+destinatario);
			return true;
		} catch (Exception exc) {
			LOG.error("Erro ao enviar email para: "+destinatario, exc);
			return false;
		}
	}
}