package codigoalvo.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void testZeraSegundosMilisegundosData() {
		Date data1a = new Date();
		Date data2a = new Date(data1a.getTime()+1850);
		System.out.println("[testZeraSegundosMilisegundosData] data1a"+data1a);
		System.out.println("[testZeraSegundosMilisegundosData] data2a"+data2a);
		System.out.println("[testZeraSegundosMilisegundosData] data1a.getTime(): "+data1a.getTime());
		System.out.println("[testZeraSegundosMilisegundosData] data2a.getTime(): "+data2a.getTime());
		Date data1b = DateUtil.zeraSegundosMilisegundosData(data1a);
		Date data2b = DateUtil.zeraSegundosMilisegundosData(data2a);
		System.out.println("[testZeraSegundosMilisegundosData] data1b.getTime(): "+data1b.getTime());
		System.out.println("[testZeraSegundosMilisegundosData] data2b.getTime(): "+data2b.getTime());
		assertTrue("Após segundos e millesegundos zerados as datas deveriam ser iguais", data1b.getTime() == data2b.getTime());
	}

	@Test
	public void testDiferencaMinutosEntreDatas() {
		Date data1 = new Date();
		Date data2 = new Date(data1.getTime() + (5 * 60 * 1000));
		System.out.println("[testDiferencaMinutosEntreDatas] data1: "+data1);
		System.out.println("[testDiferencaMinutosEntreDatas] data2: "+data2);
		int diferencaMinutosEntreDatas = DateUtil.diferencaMinutosEntreDatas(data1, data2);
		System.out.println("[testDiferencaMinutosEntreDatas] minutos: "+diferencaMinutosEntreDatas);
		assertTrue("Diferença deveria ser de 5 minutos", diferencaMinutosEntreDatas == 5);
	}

}
