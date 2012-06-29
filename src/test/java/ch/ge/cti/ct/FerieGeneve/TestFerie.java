package ch.ge.cti.ct.FerieGeneve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class TestFerie extends TestCase {
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	protected void setUp() throws Exception {
		System.setProperty("distribution.properties", this.getClass().getResource("/Distribution.properties").getFile());
		super.setUp();
	}
	
	public void testJourEtat() { 
		Date[] dates = Ferie.getJoursEtatFermesDuMois(2012, Calendar.MARCH);

		Date dateValide = new Date();
		
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
	
	public void testSamedi() {
		Date d = null;
		try { d = df.parse("30/06/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("test samedi true", Ferie.isSamedi(d));
		try { d = df.parse("29/06/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertFalse("test samedi false", Ferie.isSamedi(d));
		
		assertTrue("test samedi true", Ferie.isSamedi(20120630));
		assertFalse("test samedi false", Ferie.isSamedi(20120629));
	}
	
	public void testDimanche() {
		Date d = null;
		try { d = df.parse("1/07/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("test dimanche true", Ferie.isDimanche(d));
		try { d = df.parse("29/06/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertFalse("test dimanche false", Ferie.isDimanche(d));
	
		assertTrue("test dimanche true", Ferie.isDimanche(20120701));
		assertFalse("test dimanche false", Ferie.isDimanche(20120629));
	}
	
	public void testJourFerie() {
		Date d = null;
		try { d = df.parse("25/12/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("25/12/12 : noel", Ferie.isJourFerie(d));
		assertTrue("25/12/12 : noel", Ferie.isJourFerie(20121225));
		
		try { d = df.parse("31/12/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("31/12/12 : premier de l'an", Ferie.isJourFerie(d));
		assertTrue("31/12/12 : premier de l'an", Ferie.isJourFerie(20121231));
		
		try { d = df.parse("06/09/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("06/09/12 : jeune genevois", Ferie.isJourFerie(d));
		assertTrue("06/09/12 : jeune genevois", Ferie.isJourFerie(20120906));
		
		try { d = df.parse("01/08/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("01/08/12 : fete nationnale", Ferie.isJourFerie(d));
		assertTrue("01/08/12 : fete nationnale", Ferie.isJourFerie(20120801));
		
		try { d = df.parse("01/01/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("01/01/12 : premier de l'an", Ferie.isJourFerie(d));
		assertTrue("01/01/12 : premier de l'an", Ferie.isJourFerie(20120101));
		
		try { d = df.parse("22/04/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("22/04/11 : vendredi saint", Ferie.isJourFerie(d));
		assertTrue("22/04/11 : vendredi saint", Ferie.isJourFerie(20110422));
		
		try { d = df.parse("25/04/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("25/04/11 : lundi de paque", Ferie.isJourFerie(d));
		
		try { d = df.parse("02/06/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("02/06/11 : ascension", Ferie.isJourFerie(d));
		
		try { d = df.parse("13/06/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("13/06/11 : lundi de pentecote", Ferie.isJourFerie(d));
		
		try { d = df.parse("13/07/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertFalse("aucune fete", Ferie.isJourFerie(d));
	}
	
	public void testJourFermeEtat() {
		Date d = null;
		try { d = df.parse("25/12/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("25/12/12 : noel", Ferie.isJourEtatFerie(d));
		assertTrue("25/12/12 : noel", Ferie.isJourEtatFerie(20121225));
		
		try { d = df.parse("31/12/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("31/12/12 : premier de l'an", Ferie.isJourEtatFerie(d));
		
		try { d = df.parse("06/09/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("06/09/12 : jeune genevois", Ferie.isJourEtatFerie(d));
		
		try { d = df.parse("01/08/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("01/08/12 : fete nationnale", Ferie.isJourEtatFerie(d));
		
		try { d = df.parse("01/01/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("01/01/12 : premier de l'an", Ferie.isJourEtatFerie(d));
		
		try { d = df.parse("22/04/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("22/04/11 : vendredi saint", Ferie.isJourEtatFerie(d));
		
		try { d = df.parse("25/04/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("25/04/11 : lundi de paques", Ferie.isJourEtatFerie(d));
		
		try { d = df.parse("02/06/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("02/06/11 : ascension", Ferie.isJourEtatFerie(d));
		
		try { d = df.parse("13/06/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("13/06/11 : lundi de pentecote", Ferie.isJourEtatFerie(d));
		
		try { d = df.parse("13/07/2011"); } catch (Exception e) {fail("erreur parsing...");}
		assertFalse("aucune fete", Ferie.isJourEtatFerie(d));
		

		try { d = df.parse("24/12/2010"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("24/12/2010 : test fermeture etat", Ferie.isJourEtatFerie(d));
		assertTrue("24/12/2010 : test fermeture etat", Ferie.isJourEtatFerie(20101224));

		try { d = df.parse("26/12/2010"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("26/12/2010 : test fermeture etat", Ferie.isJourEtatFerie(d));

		try { d = df.parse("14/03/2012"); } catch (Exception e) {fail("erreur parsing...");}
		assertTrue("14/03/2012 : test fermeture etat", Ferie.isJourEtatFerie(d));
		assertTrue("14/03/2012 : test fermeture etat", Ferie.isJourEtatFerie(20120314));

	}
	
	public void testGetJourFerie() {
		Date d = null;
		Calendar cal = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();
		
		try { d = df.parse("17/05/2012"); } catch (Exception e) {fail("erreur parsing...");}
		cal.setTime(d);
		cal2.setTime(Ferie.getJourFerie(Ferie.ASCENSION, 2012));
		assertEquals("ascension 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals("ascension 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals("ascension 2012", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		
		
		try { d = df.parse("9/04/2012"); } catch (Exception e) {fail("erreur parsing...");}
		cal.setTime(d);
		cal2.setTime(Ferie.getJourFerie(Ferie.LUNDI_PAQUES, 2012));
		assertEquals("paques 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals("paques 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals("paques 2012", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		
		assertEquals("ascension 2012", 20120517, Ferie.getJourDTDFerie(Ferie.ASCENSION, 2012));
		
	}
	
	public void testAddJourouvrable() {
		Calendar cal = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();
		Date d = null;
		try { d = df.parse("16/05/2012"); } catch (Exception e) {fail("erreur parsing...");}
		
		Date drecu = Ferie.addJoursEtatOuvrables(d, 3);
		cal2.setTime(drecu);
		try { d = df.parse("22/05/2012"); } catch (Exception e) {fail("erreur parsing...");}
		cal.setTime(d);

		assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
		
		int drecuDTD = Ferie.addJoursEtatOuvrables(20120516, 3);
		assertEquals(20120522, drecuDTD);
	}
	
}
