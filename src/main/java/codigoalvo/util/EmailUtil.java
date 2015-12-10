package codigoalvo.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPSenderFailedException;
import com.sun.mail.util.MailConnectException;

public class EmailUtil {

	private static final Logger LOG = Logger.getLogger(EmailUtil.class);

	public static boolean sendMail(String destinatario, String assunto, String corpoHtml) {

		boolean emailAutenticado = (!Globals.getProperty("MAIL_SMTP_EMAIL", "").trim().isEmpty() &&
									!Globals.getProperty("MAIL_SMTP_PASSWORD", "").trim().isEmpty());

		Properties mailProps = new Properties();
		Session session;

		if (emailAutenticado) {
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
					return new PasswordAuthentication(Globals.getProperty("MAIL_SMTP_EMAIL"), Globals.getProperty("MAIL_SMTP_PASSWORD"));
				}
			};
			session = Session.getDefaultInstance(mailProps, authenticator);
		} else {
			mailProps.put("mail.transport.protocol", "smtp");
			mailProps.put("mail.smtp.host", getMXRecordsForEmailAddress(destinatario));
			mailProps.put("mail.smtp.port", "25");
			mailProps.put("mail.smtp.localhost", Globals.getProperty("HOST_EMPRESA"));
			mailProps.put("mail.smtp.from", Globals.getProperty("EMAIL_REGISTRO"));
			mailProps.put("mail.smtp.allow8bitmime", "true");
			session = Session.getDefaultInstance(mailProps);
		}

		try {

			final MimeMessage message = new MimeMessage(session);
			String remetente = "\""+Globals.getProperty("NOME_EMPRESA")+"\""+"<"+Globals.getProperty("EMAIL_REGISTRO")+">";
			message.setFrom(new InternetAddress(remetente));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			message.setSubject(assunto);
			message.setContent(corpoHtml, "text/html" );

			LOG.debug("Enviando email para: "+destinatario);
			Transport.send(message);
			LOG.debug("Email enviado com sucesso para: "+destinatario);
			return true;
		} catch (AddressException exc) {
			LOG.error("Bad address format: " + exc.getRef());
		} catch (SMTPSenderFailedException exc) {
			LOG.error(exc.getReturnCode() + ": We can't send emails from this address: " + exc.getAddress());
		} catch (NoSuchProviderException exc) {
			LOG.error(exc.getMessage() + ": No such provider");
		} catch (SMTPSendFailedException exc) {
			LOG.error(exc.getReturnCode() + ": " + exc.getMessage());
		} catch (MailConnectException exc) {
			LOG.error("Can't connect to " + exc.getHost() + ":" + exc.getPort());
		} catch (MessagingException exc) {
			LOG.error("Unknown exception" + exc);
		} catch (Exception exc) {
			LOG.error(exc);
		}
		return false;
	}

	public static String getMXRecordsForEmailAddress(String eMailAddress) {
		String returnValue = new String();
		try {
			String parts[] = eMailAddress.split("@");
			String hostName = parts[1];

			Record[] records = new Lookup(hostName, Type.MX).run();
			if (records == null) {
				throw new RuntimeException("No MX records found for domain " + hostName + ".");
			}

			if (records.length > 0) {
				MXRecord mxr = (MXRecord) records[0];
				for (int i = 0; i < records.length; i++) {
					MXRecord tocompare = (MXRecord) records[i];
					if (mxr.getPriority() > tocompare.getPriority())
						mxr = tocompare;
				}
				returnValue = mxr.getTarget().toString();
			}
		} catch (TextParseException e) {
			return new String("NULL");
		}
		return returnValue;
	}

}