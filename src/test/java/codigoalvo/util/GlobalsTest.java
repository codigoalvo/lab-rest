package codigoalvo.util;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class GlobalsTest {

	private static PropertiesUtil P;

	@BeforeClass
	public static void prepare() {
		P = Globals.getPropertiesUtil();
	}

	@Test
	public void testString() {
		String teste = P.getProperty("TESTE_STRING", "");
		System.out.println("TESTE_STRING="+teste);
		assertEquals("[TESTE_STRING] deveria retornar [Testando 123]", "Testando 123", teste);
	}

	@Test
	public void testInt() {
		int minutos = P.getInt("TESTE_INT", 0);
		System.out.println("TESTE_INT="+minutos);
		assertEquals("[TESTE_INT] deveria retornar [15]", 15, minutos);
	}

	@Test
	public void testBoolean() {
		boolean ativo = P.getBoolean("TESTE_BOOLEAN", false);
		System.out.println("TESTE_BOOLEAN="+ativo);
		assertEquals("[TESTE_BOOLEAN] deveria retornar [true]", true, ativo);
	}

}
