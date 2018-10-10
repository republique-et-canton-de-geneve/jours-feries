/*
 * Jours fériés
 *
 * Copyright (C) 2012-2018 République et Canton de Genève
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.ge.cti.ct.joursferies;

import static ch.ge.cti.ct.joursferies.service.JourFerie.ASCENSION;
import static ch.ge.cti.ct.joursferies.service.JourFerie.JEUNE_GENEVOIS;
import static ch.ge.cti.ct.joursferies.service.JourFerie.LUNDI_DE_PAQUES;
import static ch.ge.cti.ct.joursferies.service.JourFerie.ASCENSION;
import static ch.ge.cti.ct.joursferies.service.JourFerie.LUNDI_DE_PAQUES;
import static ch.ge.cti.ct.joursferies.service.JourFerie.LUNDI_DE_PENTECOTE;
import static ch.ge.cti.ct.joursferies.service.JourFerie.FETE_NATIONALE;
import static ch.ge.cti.ct.joursferies.service.JourFerie.NOEL;
import static ch.ge.cti.ct.joursferies.service.JourFerie.NOUVEL_AN;
import static ch.ge.cti.ct.joursferies.service.JourFerie.RESTAURATION_DE_LA_REPUBLIQUE;
import static ch.ge.cti.ct.joursferies.service.JourFerie.VENDREDI_SAINT;

import ch.ge.cti.ct.joursferies.exception.JoursFeriesException;
import ch.ge.cti.ct.joursferies.persistance.LecteurParametres;
import ch.ge.cti.ct.joursferies.service.JourFerie;
import ch.ge.cti.ct.joursferies.service.JoursFeriesService;
import ch.ge.cti.ct.joursferies.service.impl.JoursFeriesServiceImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Test;

public class TestJoursFeriesService extends TestCase {

    private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    private JoursFeriesService joursFeriesService;

    @Override
    protected void setUp() {
        System.setProperty("jours-feries.config.file",
                getClass().getResource("/jours-feries.properties").getFile());
        LecteurParametres lecteur = new LecteurParametres();
        
        joursFeriesService = new JoursFeriesServiceImpl();
        ((JoursFeriesServiceImpl) joursFeriesService).setFournisseurParametres(lecteur);
    }

    @Test
    public void testJourEtat() {
        Date[] dates = joursFeriesService.getJoursEtatFermesDuMois(2012, Calendar.MARCH);

        Date dateValide = new Date();

        try {
            dateValide = df.parse("14/03/2012");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = new GregorianCalendar();
        cal.setTime(dateValide);

        assertEquals("erreur sur le nombre de date", 1, dates.length);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(dates[0]);

        assertEquals("14 mars 2012", cal, cal2);

        dates = joursFeriesService.getJoursEtatFermesDuMois(2010, Calendar.DECEMBER);

        assertEquals("erreur sur le nombre de date", 6, dates.length);

        try {
            dateValide = df.parse("24/12/2010");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(dateValide);
        cal2.setTime(dates[0]);
        assertEquals("24/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("24/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("24/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        try {
            dateValide = df.parse("26/12/2010");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(dateValide);
        cal2.setTime(dates[1]);
        assertEquals("26/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("26/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("26/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        try {
            dateValide = df.parse("27/12/2010");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(dateValide);
        cal2.setTime(dates[2]);
        assertEquals("27/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("27/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("27/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        try {
            dateValide = df.parse("28/12/2010");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(dateValide);
        cal2.setTime(dates[3]);
        assertEquals("28/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("28/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("28/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        try {
            dateValide = df.parse("29/12/2010");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(dateValide);
        cal2.setTime(dates[4]);
        assertEquals("29/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("29/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("29/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        try {
            dateValide = df.parse("30/12/2010");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(dateValide);
        cal2.setTime(dates[5]);
        assertEquals("30/12/2010", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("30/12/2010", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("30/12/2010", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        int[] jourFerme = joursFeriesService.getJoursDTDEtatFermesDuMois(2012, Calendar.JANUARY);
        assertEquals(20120102, jourFerme[0]);

        jourFerme = joursFeriesService.getJoursDTDEtatFermesDuMois(2008, Calendar.AUGUST);
        assertEquals(0, jourFerme.length);

        jourFerme = joursFeriesService.getJoursDTDEtatFermesDuMois(2012, Calendar.DECEMBER);
        assertEquals(0, jourFerme.length);

        try {
            joursFeriesService.getJoursDTDEtatFermesDuMois(99999, Calendar.DECEMBER);
            fail("pas d'exception levée");
        } catch (Exception e) {
        }
    }

    public void testSamedi() {
        Date d = null;
        try {
            d = df.parse("30/06/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("test samedi true", joursFeriesService.isSamedi(d));
        try {
            d = df.parse("29/06/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertFalse("test samedi false", joursFeriesService.isSamedi(d));

        assertTrue("test samedi true", joursFeriesService.isSamedi(20120630));
        assertFalse("test samedi false", joursFeriesService.isSamedi(20120629));
    }

    public void testDimanche() {
        Date d = null;
        try {
            d = df.parse("1/07/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("test dimanche true", joursFeriesService.isDimanche(d));
        try {
            d = df.parse("29/06/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertFalse("test dimanche false", joursFeriesService.isDimanche(d));

        assertTrue("test dimanche true", joursFeriesService.isDimanche(20120701));
        assertFalse("test dimanche false", joursFeriesService.isDimanche(20120629));
    }

    public void testJourFerie() {
        Date d = null;
        try {
            d = df.parse("25/12/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("25/12/12 : noel", joursFeriesService.isJourFerie(d));
        assertTrue("25/12/12 : noel", joursFeriesService.isJourFerie(20121225));

        try {
            d = df.parse("31/12/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("31/12/12 : premier de l'an", joursFeriesService.isJourFerie(d));
        assertTrue("31/12/12 : premier de l'an", joursFeriesService.isJourFerie(20121231));

        try {
            d = df.parse("06/09/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("06/09/12 : jeune genevois", joursFeriesService.isJourFerie(d));
        assertTrue("06/09/12 : jeune genevois", joursFeriesService.isJourFerie(20120906));

        try {
            d = df.parse("01/08/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("01/08/12 : fete nationnale", joursFeriesService.isJourFerie(d));
        assertTrue("01/08/12 : fete nationnale", joursFeriesService.isJourFerie(20120801));

        try {
            d = df.parse("01/01/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("01/01/12 : premier de l'an", joursFeriesService.isJourFerie(d));
        assertTrue("01/01/12 : premier de l'an", joursFeriesService.isJourFerie(20120101));

        try {
            d = df.parse("22/04/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("22/04/11 : Vendredi-saint", joursFeriesService.isJourFerie(d));
        assertTrue("22/04/11 : Vendredi-saint", joursFeriesService.isJourFerie(20110422));

        try {
            d = df.parse("25/04/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("25/04/11 : lundi de Pâques", joursFeriesService.isJourFerie(d));

        try {
            d = df.parse("02/06/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("02/06/11 : Ascension", joursFeriesService.isJourFerie(d));

        try {
            d = df.parse("13/06/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("13/06/11 : lundi de Pentecôte", joursFeriesService.isJourFerie(d));

        try {
            d = df.parse("13/07/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertFalse("aucune fete", joursFeriesService.isJourFerie(d));
    }

    public void testJourFerieDuMois() {
        int[] dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.JANUARY);
        assertEquals(20120101, dateFeries[0]);

        dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.FEBRUARY);
        assertEquals(0, dateFeries.length);

        dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.MARCH);
        assertEquals(0, dateFeries.length);

        dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.APRIL);
        assertEquals(20120406, dateFeries[0]);
        assertEquals(20120409, dateFeries[1]);

        dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.MAY);
        assertEquals(20120517, dateFeries[0]);
        assertEquals(20120528, dateFeries[1]);

        dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.JUNE);
        assertEquals(0, dateFeries.length);

        dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.JULY);
        assertEquals(0, dateFeries.length);

        dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.AUGUST);
        assertEquals(20120801, dateFeries[0]);

        dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.SEPTEMBER);
        assertEquals(20120906, dateFeries[0]);

        dateFeries = joursFeriesService.getJoursDTDFeriesDuMois(2012, Calendar.DECEMBER);
        assertEquals(20121225, dateFeries[0]);
        assertEquals(20121231, dateFeries[1]);
    }

    public void testJourFermeEtat() {
        Date d = null;
        try {
            d = df.parse("25/12/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("25/12/12 : noel", joursFeriesService.isJourEtatFerie(d));
        assertTrue("25/12/12 : noel", joursFeriesService.isJourEtatFerie(20121225));

        try {
            d = df.parse("31/12/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("31/12/12 : premier de l'an", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("06/09/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("06/09/12 : jeune genevois", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("01/08/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("01/08/12 : fete nationnale", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("01/01/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("01/01/12 : premier de l'an", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("22/04/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("22/04/11 : Vendredi-saint", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("25/04/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("25/04/11 : lundi de Pâques", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("02/06/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("02/06/11 : Ascension", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("13/06/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("13/06/11 : lundi de Pentecôte", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("13/07/2011");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertFalse("aucune fete", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("24/12/2010");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("24/12/2010 : test fermeture etat", joursFeriesService.isJourEtatFerie(d));
        assertTrue("24/12/2010 : test fermeture etat", joursFeriesService.isJourEtatFerie(20101224));

        try {
            d = df.parse("26/12/2010");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("26/12/2010 : test fermeture etat", joursFeriesService.isJourEtatFerie(d));

        try {
            d = df.parse("14/03/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue("14/03/2012 : test fermeture etat", joursFeriesService.isJourEtatFerie(d));
        assertTrue("14/03/2012 : test fermeture etat", joursFeriesService.isJourEtatFerie(20120314));

    }

    public void testGetJourFerie() {
        Date d = null;
        Calendar cal = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();

        try {
            d = df.parse("17/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(joursFeriesService.getJourFerie(ASCENSION, 2012));
        assertEquals("Ascension 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("Ascension 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("Ascension 2012", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        try {
            d = df.parse("9/04/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(joursFeriesService.getJourFerie(LUNDI_DE_PAQUES, 2012));
        assertEquals("Pâques 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("Pâques 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("Pâques 2012", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        try {
            d = df.parse("06/09/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(joursFeriesService.getJourFerie(JEUNE_GENEVOIS, 2012));
        assertEquals("Pâques 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("Pâques 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("Pâques 2012", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        assertEquals("Ascension 2012", 20120517, joursFeriesService
                .getJourDTDFerie(ASCENSION, 2012));

        try {
            joursFeriesService.getJourFerie(JEUNE_GENEVOIS, -1);
            throw new IllegalStateException("On ne devrait passer ici");
        } catch (JoursFeriesException e) {
            // exception attendue
        }

        try {
            joursFeriesService.getJourFerie(JEUNE_GENEVOIS, 99999);
            throw new IllegalStateException("On ne devrait passer ici");
        } catch (JoursFeriesException e) {
            // exception attendue
        }

        Map<JourFerie, Date> dates = joursFeriesService.getJoursFeries(2012);
        try {
            d = df.parse("01/01/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(NOUVEL_AN));
        assertEquals("1er 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("1er 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("06/04/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(VENDREDI_SAINT));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("09/04/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(LUNDI_DE_PAQUES));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("17/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(ASCENSION));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("28/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(LUNDI_DE_PENTECOTE));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("01/08/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(FETE_NATIONALE));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("06/09/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(JEUNE_GENEVOIS));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("25/12/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(NOEL));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("31/12/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(RESTAURATION_DE_LA_REPUBLIQUE));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        Map<JourFerie, Integer> datesDTD = joursFeriesService.getJoursDTDFeries(2012);
        assertEquals(20120101, datesDTD.get(NOUVEL_AN).intValue());
        assertEquals(20120406, datesDTD.get(VENDREDI_SAINT).intValue());
        assertEquals(20120409, datesDTD.get(LUNDI_DE_PAQUES).intValue());
        assertEquals(20120517, datesDTD.get(ASCENSION).intValue());
        assertEquals(20120528, datesDTD.get(LUNDI_DE_PENTECOTE).intValue());
        assertEquals(20120801, datesDTD.get(FETE_NATIONALE).intValue());
        assertEquals(20120906, datesDTD.get(JEUNE_GENEVOIS).intValue());
        assertEquals(20121225, datesDTD.get(NOEL).intValue());
        assertEquals(20121231, datesDTD.get(RESTAURATION_DE_LA_REPUBLIQUE).intValue());
    }

    public void testGetJourFerie2014() {
        Date d = null;
        Calendar cal = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();

        try {
            d = df.parse("17/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(joursFeriesService.getJourFerie(ASCENSION, 2012));
        assertEquals("Ascension 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("Ascension 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("Ascension 2012", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        try {
            d = df.parse("9/04/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(joursFeriesService.getJourFerie(LUNDI_DE_PAQUES, 2012));
        assertEquals("Pâques 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("Pâques 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("Pâques 2012", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        try {
            d = df.parse("06/09/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(joursFeriesService.getJourFerie(JEUNE_GENEVOIS, 2012));
        assertEquals("Pâques 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("Pâques 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals("Pâques 2012", cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        assertEquals("Ascension 2012", 20120517, joursFeriesService
                .getJourDTDFerie(ASCENSION, 2012));

        Map<JourFerie, Date> dates = joursFeriesService.getJoursFeries(2012);
        try {
            d = df.parse("01/01/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(NOUVEL_AN));
        assertEquals("1er 2012", cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals("1er 2012", cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("06/04/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(VENDREDI_SAINT));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("09/04/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(LUNDI_DE_PAQUES));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("17/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(ASCENSION));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("28/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(LUNDI_DE_PENTECOTE));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("01/08/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(FETE_NATIONALE));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("06/09/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(JEUNE_GENEVOIS));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("25/12/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(NOEL));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        try {
            d = df.parse("31/12/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        cal2.setTime(dates.get(RESTAURATION_DE_LA_REPUBLIQUE));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));

        Map<JourFerie, Integer> datesDTD = joursFeriesService.getJoursDTDFeries(2014);
        assertEquals(20140101, datesDTD.get(NOUVEL_AN).intValue());
        assertEquals(20140418, datesDTD.get(VENDREDI_SAINT).intValue());
        assertEquals(20140421, datesDTD.get(LUNDI_DE_PAQUES).intValue());
        assertEquals(20140529, datesDTD.get(ASCENSION).intValue());
        assertEquals(20140609, datesDTD.get(LUNDI_DE_PENTECOTE).intValue());
        assertEquals(20140801, datesDTD.get(FETE_NATIONALE).intValue());
        assertEquals(20140911, datesDTD.get(JEUNE_GENEVOIS).intValue());
        assertEquals(20141225, datesDTD.get(NOEL).intValue());
        assertEquals(20141231, datesDTD.get(RESTAURATION_DE_LA_REPUBLIQUE).intValue());
    }

    public void testAddJourouvrable() {
        Calendar cal = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        Date d = null;
        try {
            d = df.parse("16/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }

        Date drecu = joursFeriesService.addJoursEtatOuvrables(d, 3);
        cal2.setTime(drecu);
        try {
            d = df.parse("22/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);

        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        int drecuDTD = joursFeriesService.addJoursEtatOuvrables(20120516, 3);
        assertEquals(20120522, drecuDTD);

        drecuDTD = joursFeriesService.addJoursEtatOuvrables(20120516, 10);
        assertEquals(20120601, drecuDTD);

        try {
            d = df.parse("16/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        drecu = joursFeriesService.addJoursOuvrables(d, 10);
        cal2.setTime(drecu);
        try {
            d = df.parse("01/06/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        cal.setTime(d);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), cal2.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), cal2.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.YEAR), cal2.get(Calendar.YEAR));

        drecuDTD = joursFeriesService.addJoursOuvrables(20120516, 10);
        assertEquals(20120601, drecuDTD);
    }

    public void testJourOuvrable() {
        Date d = null;
        try {
            d = df.parse("16/05/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertTrue(joursFeriesService.isJourEtatOuvrable(d));
        assertTrue(joursFeriesService.isJourEtatOuvrable(20120516));
        assertTrue(joursFeriesService.isJourOuvrable(d));
        assertTrue(joursFeriesService.isJourOuvrable(20120516));
        try {
            d = df.parse("30/06/2012");
        } catch (Exception e) {
            fail("erreur parsing...");
        }
        assertFalse(joursFeriesService.isJourEtatOuvrable(d));
        assertFalse(joursFeriesService.isJourEtatOuvrable(20120630));
        assertFalse(joursFeriesService.isJourOuvrable(d));
        assertFalse(joursFeriesService.isJourOuvrable(20120630));

        int jourOuvrable = joursFeriesService.getProchainJourEtatOuvrable(20120630);
        assertEquals(20120702, jourOuvrable);

        jourOuvrable = joursFeriesService.getProchainJourEtatOuvrable(20091224);
        assertEquals(20100104, jourOuvrable);
    }

}
