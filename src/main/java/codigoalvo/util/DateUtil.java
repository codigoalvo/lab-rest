package codigoalvo.util;

import java.text.SimpleDateFormat;
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

	public static Calendar primeiroDiaDoMes(int mes, int ano) {
		Calendar calPeriodo = GregorianCalendar.getInstance();
		calPeriodo.set(Calendar.MONTH, mes-1);
		calPeriodo.set(Calendar.YEAR, ano);
		calPeriodo.set(Calendar.DAY_OF_MONTH, 1);
		calPeriodo.set(Calendar.HOUR_OF_DAY, 6);
		calPeriodo.set(Calendar.MINUTE, 0);
		calPeriodo.set(Calendar.SECOND, 0);
		calPeriodo.set(Calendar.MILLISECOND, 0);
		return calPeriodo;
	}

	public static String toSqlDateString(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(data);
	}

}
