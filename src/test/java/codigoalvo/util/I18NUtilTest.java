package codigoalvo.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class I18NUtilTest {

	@Test
	public void testGetMessageOk() {
		String message = I18NUtil.getMessage("login.sucesso");
		System.out.println("login.sucesso = "+message);
		assertEquals("A mensagem não foi obtida corretamente: "+message, message, "Login efetuado com sucesso!");
	}

	@Test
	public void testGetMessateInvalid() {
		String message = I18NUtil.getMessage("nao.existe");
		System.out.println("nao.existe = "+message);
		assertEquals("A mensagem não foi obtida corretamente: "+message, message, "nao.existe");
	}

	@Test
	public void testGetMessageParam() {
		String key = "login.senhaInvalida";
		key += MsgParamUtil.buildParams("3", "5");
		String message = I18NUtil.getMessage(key);
		System.out.println("login.senhaInvalida = "+message);
		assertEquals("A mensagem não foi obtida corretamente: "+message, message, "Senha inválida! - Tentativa 3 de 5");
	}

}
