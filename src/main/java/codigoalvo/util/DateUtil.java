package codigoalvo.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static Date zeraSegundosMilisegundosData(Date data) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static int diferencaMinutosEntreDatas(Date data1, Date data2) {
		long diferenca = data1.getTime() - data2.getTime();
		if (diferenca < 0) {
			diferenca *= -1;
		}
		Long diferencaMinutos = ((diferenca / 1000) / 60);
		return diferencaMinutos.intValue();
	}

}
