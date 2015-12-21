package codigoalvo.templates;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import codigoalvo.entity.ValidadorEmail;
import codigoalvo.entity.ValidadorEmailTipo;
import codigoalvo.util.Globals;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateUtil {

	private static final Logger LOG = Logger.getLogger(TemplateUtil.class);

	public static String getHtmlTemplateEmail(ValidadorEmail email, String urlRegistro) {
		Map<String, Object> valores = new HashMap<String, Object>();
		//String urlAplicacao = Globals.getProperty("URL_APLICACAO");
		valores.put("email", email.getEmail());
		valores.put("mensagem", getMensagem(email.getTipo()));
		valores.put("nomeEmpresa", Globals.getProperty("NOME_EMPRESA"));
		valores.put("nomeAplicacao", Globals.getProperty("NOME_APLICACAO"));
		valores.put("urlConfirma", urlRegistro+email.getId().toString());
		valores.put("urlLogotipo", "http://www.codigoalvo.com.br/images/logo.png");
		return getTemplateString("email-registro.html", valores);
	}

	private static String getMensagem(ValidadorEmailTipo tipo) {
		String mensagem = "Clique no link abaixo para ";
		switch (tipo) {
		case REGISTRO:
			return mensagem+"confirmar o registro";
		case SENHA:
			return mensagem+"alterar sua senha";
		case EMAIL:
			return mensagem+"confirmar a alteração de email";
		default:
			return mensagem+"continuar";
		}
	}

	public static String getTemplateString(String templateFileName, Map<String, Object> valores) {
		TemplateLoader loader = new ClassTemplateLoader(TemplateUtil.class, "");
		return getTemplateString(loader, templateFileName, valores);
	}

	public static String getTemplateString(TemplateLoader loader, String templateFileName, Map<String, Object> valores) {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
		cfg.setTemplateLoader(loader);
		try {
			Template template = cfg.getTemplate(templateFileName);
			Writer out = new OutputStreamWriter(System.out);
			template.process(valores, out);
			out.flush();
			Writer writer = new StringWriter();
			template.process(valores, writer);
			writer.flush();
			writer.close();
			return writer.toString();
		} catch (TemplateException exc) {
			LOG.error(exc);
		} catch (IOException exc) {
			LOG.error(exc);
		}
		return "";
	}

}
