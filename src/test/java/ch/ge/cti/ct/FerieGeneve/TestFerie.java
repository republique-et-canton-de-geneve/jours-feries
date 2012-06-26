package ch.ge.cti.ct.FerieGeneve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class TestFerie extends TestCase {
	public void testJourEtat() { 
		System.setProperty("distribution.properties", this.getClass().getResource("/Distribution.properties").getFile());
		Date[] dates = Ferie.getJoursEtatFermesDuMois(2012, Calendar.MARCH);

		Date dateValide = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {dateValide = df.parse("14/03/2012");}catch (ParseException e) {e.printStackTrace();}
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(dateValide);
		
		assertEquals("erreur sur le nombre de date", 1, dates.length);
		Calendar cal2 = new GregorianCalendar();
		cal2.setTime(dates[0]);
		
		assertEquals("14 mars 2012", cal, cal2);
		
		dates = Ferie.getJoursEtatFermesDuMois(2010, Calendar.DECEMBER);
		
		assertEquals("erreur sur le nombre de date", 6, dates.length);
		
		try {dateValide = df.parse("24/12/2010");}catch (ParseException e) {e.printStackTrace();}
		cal.setTime(dateValide);
		cal2.setTime(dates[0]);
		assertEquals("24/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals("24/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals("24/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

		try {dateValide = df.parse("26/12/2010");}catch (ParseException e) {e.printStackTrace();}
		cal.setTime(dateValide);
		cal2.setTime(dates[1]);
		assertEquals("26/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals("26/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals("26/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		
		try {dateValide = df.parse("27/12/2010");}catch (ParseException e) {e.printStackTrace();}
		cal.setTime(dateValide);
		cal2.setTime(dates[2]);
		assertEquals("27/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals("27/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals("27/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		
		try {dateValide = df.parse("28/12/2010");}catch (ParseException e) {e.printStackTrace();}
		cal.setTime(dateValide);
		cal2.setTime(dates[3]);
		assertEquals("28/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals("28/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals("28/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		
		try {dateValide = df.parse("29/12/2010");}catch (ParseException e) {e.printStackTrace();}
		cal.setTime(dateValide);
		cal2.setTime(dates[4]);
		assertEquals("29/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals("29/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals("29/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		
		try {dateValide = df.parse("30/12/2010");}catch (ParseException e) {e.printStackTrace();}
		cal.setTime(dateValide);
		cal2.setTime(dates[5]);
		assertEquals("30/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals("30/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals("30/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		
	}
}
