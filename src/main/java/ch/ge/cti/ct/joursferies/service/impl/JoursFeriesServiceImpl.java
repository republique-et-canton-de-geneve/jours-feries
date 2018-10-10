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
package ch.ge.cti.ct.joursferies.service.impl;

import static ch.ge.cti.ct.joursferies.service.JourFerie.ASCENSION;
import static ch.ge.cti.ct.joursferies.service.JourFerie.FETE_NATIONALE;
import static ch.ge.cti.ct.joursferies.service.JourFerie.JEUNE_GENEVOIS;
import static ch.ge.cti.ct.joursferies.service.JourFerie.NOEL;
import static ch.ge.cti.ct.joursferies.service.JourFerie.NOUVEL_AN;
import static ch.ge.cti.ct.joursferies.service.JourFerie.LUNDI_DE_PAQUES;
import static ch.ge.cti.ct.joursferies.service.JourFerie.LUNDI_DE_PENTECOTE;
import static ch.ge.cti.ct.joursferies.service.JourFerie.RESTAURATION_DE_LA_REPUBLIQUE;
import static ch.ge.cti.ct.joursferies.service.JourFerie.VENDREDI_SAINT;

import ch.ge.cti.ct.joursferies.persistance.FournisseurParametres;
import ch.ge.cti.ct.joursferies.service.JourFerie;
import ch.ge.cti.ct.joursferies.service.JoursFeriesService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JoursFeriesServiceImpl implements JoursFeriesService {

    private static final int ECART_VENDREDI_SAINT_PAQUES = -2;

    private static final int ECART_LUNDI_PAQUES = 1;

    private static final int ECART_ASCENSION_PAQUES = 39;

    private static final int ECART_LUNDI_PENTECOTE_PAQUES = 50;

    // tout cela pour être conforme sonar...
    private static final int NOEL_JOUR = 25;

    private static final int ANNEE_MAX = 10001;

    private static final int RESTAURATION_JOUR = 31;

    private static final int CONV_ANNEE_DTD = 10000;

    private static final int CONV_MOIS_DTD = 100;

    private static final int CONV_SIECLE = 100;

    private static final int MOD_PAQUE = 19;

    private static final int ANNEE_PARAM = 2011;

    private static final int NBR_MOIS = 12;

    private static final int HEURE_DEFAUT = 12;

    private static final int MAX_JOUR_PAQUE = 31;

    private static final Logger LOG = LoggerFactory.getLogger(JoursFeriesServiceImpl.class);

    /**
     * Permet de lire les données concernant le paramétrage des jours fermés.
     */
    @Resource
    private FournisseurParametres fournisseurParametres;

    private static Map<Integer, String[]> joursFermetureEtat = new HashMap<>();

    public JoursFeriesServiceImpl() {
        this.fournisseurParametres = fournisseurParametres;
    }

    public void setFournisseurParametres(FournisseurParametres fournisseurParametres) {
        this.fournisseurParametres = fournisseurParametres;
    }

    @Override
    public boolean isSamedi(Date pDate) {
        DateValidator.checkValidite(pDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        return Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public boolean isSamedi(int pDTDDate) {
        DateValidator.checkValidite(Integer.toString(pDTDDate), "yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        return isSamedi(convert(cal, pDTDDate));
    }

    @Override
    public boolean isDimanche(Date pDate) {
        DateValidator.checkValidite(pDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        return Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK);
    }

    @Override
    public boolean isDimanche(int pDTDDate) {
        DateValidator.checkValidite(Integer.toString(pDTDDate), "yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        return isDimanche(convert(cal, pDTDDate));
    }

    @Override
    public boolean isJourFerie(Date pDate) {
        DateValidator.checkValidite(pDate);
        boolean result = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        Date[] dates = getJoursFeriesDuMois(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        int jour = cal.get(Calendar.DATE);
        if (null != dates) {
            for (int i = 0; i < dates.length; i++) {
                cal.setTime(dates[i]);
                result = jour == cal.get(Calendar.DATE);
                if (result) {
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public boolean isJourFerie(int pDTDDate) {
        DateValidator.checkValidite(Integer.toString(pDTDDate), "yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        return isJourFerie(convert(cal, pDTDDate));
    }

    @Override
    public boolean isJourEtatFerie(Date pDate) {
        DateValidator.checkValidite(pDate);
        boolean result = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        Date[] dates = getJoursFeriesDuMois(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        int jour = cal.get(Calendar.DATE);
        if (null != dates) {
            for (int i = 0; i < dates.length; i++) {
                cal.setTime(dates[i]);
                result = jour == cal.get(Calendar.DATE);
                if (result) {
                    break;
                }
            }
        }
        if (!result) {
            // on rajoute ici le test sur les jours qui ne sont pas fériés officiels mais qui sont fermés à
            // l'État.
            dates = getJoursEtatFermesDuMois(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
            if (null != dates) {
                for (int i = 0; i < dates.length; i++) {
                    cal.setTime(dates[i]);
                    result = jour == cal.get(Calendar.DATE);
                    if (result) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean isJourEtatFerie(int pDTDDate) {
        DateValidator.checkValidite(Integer.toString(pDTDDate), "yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        return isJourEtatFerie(convert(cal, pDTDDate));
    }

    @Override
    public boolean isJourOuvrable(Date pDate) {
        DateValidator.checkValidite(pDate);
        DateValidator.checkValidite(pDate);
        return !(isSamedi(pDate) || isDimanche(pDate) || isJourFerie(pDate));
    }

    @Override
    public boolean isJourOuvrable(int pDtdDate) {
        DateValidator.checkValidite(Integer.toString(pDtdDate), "yyyyMMdd");
        return !(isSamedi(pDtdDate) || isDimanche(pDtdDate) || isJourFerie(pDtdDate));
    }

    @Override
    public boolean isJourEtatOuvrable(Date pDate) {
        DateValidator.checkValidite(pDate);
        return !(isSamedi(pDate) || isDimanche(pDate) || isJourEtatFerie(pDate));
    }

    @Override
    public boolean isJourEtatOuvrable(int pDtdDate) {
        DateValidator.checkValidite(Integer.toString(pDtdDate), "yyyyMMdd");
        return !(isSamedi(pDtdDate) || isDimanche(pDtdDate) || isJourEtatFerie(pDtdDate));
    }

    @Override
    public Date getJourFerie(JourFerie pJourFerie, int pAnnee) {
        DateValidator.checkAnneeValidite(pAnnee);
        Date date = null;
        Calendar cal = Calendar.getInstance();
        setDefaultTime(cal);
        cal.set(Calendar.YEAR, pAnnee);

        if (pAnnee > -1 && pAnnee < ANNEE_MAX) {
            switch (pJourFerie) {
            case NOUVEL_AN:
                cal.set(Calendar.DATE, 1);
                cal.set(Calendar.MONTH, Calendar.JANUARY);
                date = cal.getTime();
                break;

            case VENDREDI_SAINT:
                cal.setTime(getDatePaques(pAnnee));
                cal.add(Calendar.DATE, ECART_VENDREDI_SAINT_PAQUES);
                date = cal.getTime();
                break;

            case LUNDI_DE_PAQUES:
                cal.setTime(getDatePaques(pAnnee));
                cal.add(Calendar.DATE, ECART_LUNDI_PAQUES);
                date = cal.getTime();
                break;

            case ASCENSION:
                cal.setTime(getDatePaques(pAnnee));
                cal.add(Calendar.DATE, ECART_ASCENSION_PAQUES);
                date = cal.getTime();
                break;

            case LUNDI_DE_PENTECOTE:
                cal.setTime(getDatePaques(pAnnee));
                cal.add(Calendar.DATE, ECART_LUNDI_PENTECOTE_PAQUES);
                date = cal.getTime();
                break;

            case FETE_NATIONALE:
                cal.set(Calendar.DATE, 1);
                cal.set(Calendar.MONTH, Calendar.AUGUST);
                date = cal.getTime();
                break;

            case JEUNE_GENEVOIS:
                date = getJeuneGenevois(pAnnee);
                break;

            case NOEL:
                cal.set(Calendar.DATE, NOEL_JOUR);
                cal.set(Calendar.MONTH, Calendar.DECEMBER);
                date = cal.getTime();
                break;

            case RESTAURATION_DE_LA_REPUBLIQUE:
                cal.set(Calendar.DATE, RESTAURATION_JOUR);
                cal.set(Calendar.MONTH, Calendar.DECEMBER);
                date = cal.getTime();
                break;

            default:
                throw new IllegalArgumentException("pJourFerie demandé n'est pas dans la liste");
            }
        }
        return date;
    }

    @Override
    public int getJourDTDFerie(JourFerie pJourFerie, int pAnnee) {
        DateValidator.checkAnneeValidite(pAnnee);
        Calendar cal = Calendar.getInstance();
        cal.setTime(getJourFerie(pJourFerie, pAnnee));
        return cal.get(Calendar.YEAR) * CONV_ANNEE_DTD + (cal.get(Calendar.MONTH) + 1) * CONV_MOIS_DTD + cal
                .get(Calendar.DATE);
    }

    @Override
    public Date getDatePaques(int pAnnee) {
        DateValidator.checkAnneeValidite(pAnnee);
        Calendar cal = Calendar.getInstance();
        int nombreOr = pAnnee % MOD_PAQUE + 1;
        int siecle = pAnnee / 100 + 1;

        // Nombre d'année divisible par 4 qui ne sont pas bisextile. Comprend le dàcalage
        // du pape Gràgoire
        int pasBisextile = 3 * siecle / 4 - 12;

        int synchroLune = (8 * siecle + 5) / 25 - 5;
        int dimanche = (5 * pAnnee) / 4 - pasBisextile - 10;

        // Epacte (date de la pleine lune)
        int epacte = ((11 * nombreOr) + 20 + synchroLune - pasBisextile) % 30;
        if ((epacte == 25 && nombreOr > 11) || (epacte == 24)) {
            epacte++;
        }

        // Pâques est le premier dimanche qui suit la pleine lune apparaissant
        // le 21 mars ou apràs le 21 mars.
        int paque = 44 - epacte;
        if (paque < 21) {
            paque += 30;
        }

        // on se cale sur le dimanche
        paque = paque + 7 - (dimanche + paque) % 7;

        // si le nombre paques est > 31, on est au mois d'avril
        if (paque > MAX_JOUR_PAQUE) {
            paque -= MAX_JOUR_PAQUE;
            cal.set(Calendar.MONTH, Calendar.APRIL);
        } else {
            cal.set(Calendar.MONTH, Calendar.MARCH);
        }
        cal.set(Calendar.DATE, paque);
        cal.set(Calendar.YEAR, pAnnee);
        setDefaultTime(cal);
        return cal.getTime();
    }

    @Override
    public Date getJeuneGenevois(int pAnnee) {
        DateValidator.checkAnneeValidite(pAnnee);
        Calendar cal = Calendar.getInstance();
        setDefaultTime(cal);
        // On fixe le calendrier au 1er septembre de l'année passée en paramètre
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        cal.set(Calendar.YEAR, pAnnee);
        // On boucle jusqu'à tomber un dimanche
        while (Calendar.SUNDAY != cal.get(Calendar.DAY_OF_WEEK)) {
            cal.add(Calendar.DATE, 1);
        }
        // Le jeûne genevois est le jeudi qui suit ce dimanche soit 4 jours plus tard
        cal.add(Calendar.DATE, 4);
        return cal.getTime();
    }

    @Override
    public Map<JourFerie, Date> getJoursFeries(int pAnnee) {
        DateValidator.checkAnneeValidite(pAnnee);
        Map<JourFerie, Date> dates = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        setDefaultTime(cal);
        // On commence par les dates fixes

        // Jour de l'an
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.YEAR, pAnnee);
        dates.put(NOUVEL_AN, cal.getTime());

        // Fête nationale
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.YEAR, pAnnee);
        dates.put(FETE_NATIONALE, cal.getTime());

        // Noël
        cal.set(Calendar.DATE, NOEL_JOUR);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.YEAR, pAnnee);
        dates.put(NOEL, cal.getTime());

        // Restauration de la République
        cal.set(Calendar.DATE, RESTAURATION_JOUR);
        dates.put(RESTAURATION_DE_LA_REPUBLIQUE, cal.getTime());

        // On continue avec les dates qui dépendent de la date de Pâques

        // Vendredi-saint
        cal.setTime(getDatePaques(pAnnee));
        cal.add(Calendar.DATE, ECART_VENDREDI_SAINT_PAQUES);
        dates.put(VENDREDI_SAINT, cal.getTime());

        // Lundi de Pâques
        cal.add(Calendar.DATE, ECART_LUNDI_PAQUES - ECART_VENDREDI_SAINT_PAQUES);
        dates.put(LUNDI_DE_PAQUES, cal.getTime());

        // Jeudi de l'Ascension
        cal.add(Calendar.DATE, ECART_ASCENSION_PAQUES - ECART_LUNDI_PAQUES);
        dates.put(ASCENSION, cal.getTime());

        // Lundi de Pentecôte
        cal.add(Calendar.DATE, ECART_LUNDI_PENTECOTE_PAQUES - ECART_ASCENSION_PAQUES);
        dates.put(LUNDI_DE_PENTECOTE, cal.getTime());

        // Reste le jeûne genevois
        dates.put(JEUNE_GENEVOIS, getJeuneGenevois(pAnnee));

        return dates;
    }

    @Override
    public Map<JourFerie, Integer> getJoursDTDFeries(int pAnnee) {
        DateValidator.checkAnneeValidite(pAnnee);
        Calendar cal = Calendar.getInstance();
        Map<JourFerie, Date> dates = getJoursFeries(pAnnee);
        return dates.entrySet().stream()
                               .collect(Collectors.toMap(Map.Entry::getKey,
                                                         e -> convert(cal, e.getValue())));
    }

    @Override
    public Date[] getJoursEtatFermesDuMois(int pAnnee, int pMois) {
        DateValidator.checkAnneeValidite(pAnnee);
        Date[] dates = new Date[0];
        // Teste si les paramètres sont bien entre les bornes voulues
        if (pAnnee > -1 && pAnnee < ANNEE_MAX && pMois > -1 && pMois < NBR_MOIS) {
            Calendar cal;

            switch (pMois) {
            case Calendar.JANUARY:
                // Au mois de janvier si le 1er janvier tombe un dimanche, le 2 janvier est fermé
                cal = Calendar.getInstance();
                setDefaultTime(cal);
                cal.set(pAnnee, Calendar.JANUARY, 1);
                Date premierJanvier = cal.getTime();
                if (isDimanche(premierJanvier)) {
                    dates = new Date[1];
                    cal.add(Calendar.DATE, 1);
                    dates[0] = cal.getTime();
                }

                break;

            case Calendar.MAY:
                // Au mois de mai, le 1er mai est fermé
                dates = new Date[1];
                cal = Calendar.getInstance();
                setDefaultTime(cal);
                cal.set(pAnnee, Calendar.MAY, 1);
                dates[0] = cal.getTime();
                break;

            case Calendar.AUGUST:
                // Au mois d'août, si le 1er août tombe un dimanche, il est rattrapé le lundi suivant
                cal = Calendar.getInstance();
                setDefaultTime(cal);
                cal.set(pAnnee, Calendar.AUGUST, 1);
                Date premierAout = cal.getTime();

                if (isDimanche(premierAout)) {
                    dates = new Date[1];
                    cal.add(Calendar.DATE, 1);
                    dates[0] = cal.getTime();
                }
                break;

            case Calendar.DECEMBER:
                // Au mois de décembre, les 24, 26, 27, 28, 29 et 30 décembre étaient fermés avant 2011
                if (pAnnee < ANNEE_PARAM) {
                    cal = Calendar.getInstance();
                    setDefaultTime(cal);
                    dates = new Date[6];
                    cal.set(pAnnee, Calendar.DECEMBER, NOEL_JOUR - 1);
                    dates[0] = cal.getTime();
                    cal.add(Calendar.DATE, 2);
                    dates[1] = cal.getTime();
                    cal.add(Calendar.DATE, 1);
                    dates[2] = cal.getTime();
                    cal.add(Calendar.DATE, 1);
                    dates[3] = cal.getTime();
                    cal.add(Calendar.DATE, 1);
                    dates[4] = cal.getTime();
                    cal.add(Calendar.DATE, 1);
                    dates[5] = cal.getTime();
                }
                break;

            default:
                break;
            }

            // à partir de 2011 les dates de fermeture sont paramétrées
            if (pAnnee >= ANNEE_PARAM) {
                List<Date> joursFermes = getJoursFermesParConseilEtat(pAnnee, pMois);
                joursFermes.addAll(Arrays.asList(dates));
                dates = joursFermes.toArray(new Date[joursFermes.size()]);
            }

            return dates;
        } else {
            throw new IllegalArgumentException(
                    "L'année doit être comprise entre 1 et 10000 et le mois entre Calendar.JANUARY et Calendar"
                            + ".DECEMBER.");
        }
    }

    @Override
    public List<Date> getJoursFermesParConseilEtat(int pAnnee, int pMois) {
        DateValidator.checkAnneeValidite(pAnnee);
        String[] tabJoursFermes = joursFermetureEtat.get(Integer.valueOf(pAnnee));
        if (tabJoursFermes == null) {
            //LECTURE des joursFermes format dd/MM/yyyy
            tabJoursFermes = fournisseurParametres.getJoursFermes(pAnnee);
            joursFermetureEtat.put(Integer.valueOf(pAnnee), tabJoursFermes);
        }

        Calendar cal = Calendar.getInstance();
        List<Date> listeJoursFermes = new ArrayList<>();
        for (int i = 0; i < tabJoursFermes.length; i++) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date jourFerme = df.parse(tabJoursFermes[i] + "/" + pAnnee);
                cal.setTime(jourFerme);
                if (cal.get(Calendar.MONTH) == pMois) {
                    listeJoursFermes.add(jourFerme);
                }
            } catch (ParseException ex) {
                LOG.error("erreur de formatage de la date reçue : " + tabJoursFermes[i]
                        + " attendue : dd/MM/yyyy", ex);
            }
        }

        return listeJoursFermes;
    }

    @Override
    public int[] getJoursDTDEtatFermesDuMois(int pAnnee, int pMois) {
        DateValidator.checkAnneeValidite(pAnnee);
        Calendar cal = Calendar.getInstance();
        // On cherche les dates au format java du mois pMois
        Date[] dates = getJoursEtatFermesDuMois(pAnnee, pMois);
        int[] datesDTD = new int[dates.length];
        // On convertit les dates du format java au format DTD
        for (int i = 0; i < dates.length; i++) {
            datesDTD[i] = convert(cal, dates[i]);
        }
        return datesDTD;
    }

    @Override
    public Date[] getJoursFeriesDuMois(int pAnnee, int pMois) {
        DateValidator.checkAnneeValidite(pAnnee);
        Date[] dates = null;
        // Teste si les paramètres sont bien entre les bornes voulues
        if (pAnnee > -1 && pAnnee < ANNEE_MAX && pMois > -1 && pMois < NBR_MOIS) {
            Calendar cal;
            int index;
            Date vendrediSaint;
            Date lundiPaques;
            Date Ascension;
            Date lundiPentecote;
            switch (pMois) {
            case Calendar.JANUARY:
                // Il n'y a qu'un jour férié au mois de janvier : le Nouvel an
                dates = new Date[1];
                dates[0] = getJourFerie(NOUVEL_AN, pAnnee);
                break;

            case Calendar.MARCH:
                // Au mois de mars, il peut y avoir le Vendredi-saint et le lundi de Pâques
                cal = Calendar.getInstance();
                index = 0;
                vendrediSaint = getJourFerie(VENDREDI_SAINT, pAnnee);
                lundiPaques = null;
                cal.setTime(vendrediSaint);
                if (Calendar.MARCH == cal.get(Calendar.MONTH)) {
                    // Cas où le vendredi saint est en mars
                    index = 1;
                    lundiPaques = getJourFerie(LUNDI_DE_PAQUES, pAnnee);
                    cal.setTime(lundiPaques);
                    if (Calendar.MARCH == cal.get(Calendar.MONTH)) {
                        // Cas où le lundi de Pâques est aussi en mars
                        index = 2;
                    }
                }
                if (index == 0) {
                    dates = new Date[0];
                } else if (index == 1) {
                    dates = new Date[1];
                    dates[0] = vendrediSaint;
                } else if (index == 2) {
                    dates = new Date[2];
                    dates[0] = vendrediSaint;
                    dates[1] = lundiPaques;
                }
                break;

            case Calendar.APRIL:
                cal = Calendar.getInstance();
                index = 0;
                vendrediSaint = getJourFerie(VENDREDI_SAINT, pAnnee);
                lundiPaques = getJourFerie(LUNDI_DE_PAQUES, pAnnee);
                Ascension = null;
                lundiPentecote = null;
                cal.setTime(vendrediSaint);
                if (Calendar.APRIL == cal.get(Calendar.MONTH)) {
                    // Cas où le Vendredi-saint est au mois d'avril. Dans ce cas, l'Ascension et le lundi
                    // de Pentecôte ne peuvent pas être au mois d'avril.
                    // Par contre, forcément, le lundi de Pâques est aussi au mois d'avril.
                    index = 1;
                } else {
                    // Le vendredi saint est au mois de mars
                    cal.setTime(lundiPaques);
                    if (Calendar.APRIL == cal.get(Calendar.MONTH)) {
                        // Cas où le lundi de Pâques est au mois d'avril
                        index = 2;
                    } else {
                        // Cas où le lundi de Pâques est au mois de mars
                        lundiPentecote = getJourFerie(LUNDI_DE_PENTECOTE, pAnnee);
                        cal.setTime(lundiPentecote);
                        if (Calendar.APRIL == cal.get(Calendar.MONTH)) {
                            // Cas où le lundi de Pentecôte est au mois d'avril
                            // Forcément, l'Ascension est aussi au mois d'avril
                            index = 4;
                        } else {
                            // Si le lundi de Pentecôte n'est pas au mois d'avril,
                            // l'Ascension peut tout de même être au mois d'avril
                            Ascension = getJourFerie(ASCENSION, pAnnee);
                            cal.setTime(Ascension);
                            if (Calendar.APRIL == cal.get(Calendar.MONTH)) {
                                index = 3;
                            }
                        }
                    }
                }
                if (index == 0) {
                    dates = new Date[0];
                } else if (index == 1) {
                    dates = new Date[2];
                    dates[0] = vendrediSaint;
                    dates[1] = lundiPaques;
                } else if (index == 2) {
                    dates = new Date[1];
                    dates[0] = lundiPaques;
                } else if (index == 3) {
                    dates = new Date[1];
                    dates[0] = Ascension;
                } else if (index == 4) {
                    dates = new Date[2];
                    dates[0] = Ascension;
                    dates[1] = lundiPentecote;
                }
                break;

            case Calendar.MAY:
                // Au mois de mai, il peut y avoir l'Ascension et le lundi de Pentecôte
                cal = Calendar.getInstance();
                index = 0;
                Ascension = getJourFerie(ASCENSION, pAnnee);
                lundiPentecote = getJourFerie(LUNDI_DE_PENTECOTE, pAnnee);
                cal.setTime(Ascension);
                if (Calendar.MAY == cal.get(Calendar.MONTH)) {
                    // Cas où l'Ascension est au mois de mai
                    index = 1;
                    cal.setTime(lundiPentecote);
                    if (Calendar.MAY == cal.get(Calendar.MONTH)) {
                        // Cas où l'Ascension et le lundi de Pentecôte sont au mois de mai.
                        index = 3;
                    }
                } else {
                    cal.setTime(lundiPentecote);
                    if (Calendar.MAY == cal.get(Calendar.MONTH)) {
                        // Cas où l'Ascension est au mois d'avril et le lundi de Pentecôte au mois de mai.
                        index = 2;
                    }
                }
                if (index == 0) {
                    dates = new Date[0];
                } else if (index == 1) {
                    dates = new Date[1];
                    dates[0] = Ascension;
                } else if (index == 2) {
                    dates = new Date[1];
                    dates[0] = lundiPentecote;
                } else if (index == 3) {
                    dates = new Date[2];
                    dates[0] = Ascension;
                    dates[1] = lundiPentecote;
                }
                break;

            case Calendar.JUNE:
                // Il est possible d'avoir l'Ascension et le lundi de Pentecôte au mois de juin
                cal = Calendar.getInstance();
                index = 0;
                Ascension = getJourFerie(ASCENSION, pAnnee);
                lundiPentecote = getJourFerie(LUNDI_DE_PENTECOTE, pAnnee);
                cal.setTime(Ascension);
                if (Calendar.JUNE == cal.get(Calendar.MONTH)) {
                    // Cas où l'Ascension est au mois de juin, alors forcément le lundi de Pentecôte
                    // est aussi au mois de juin
                    index = 1;
                } else {
                    cal.setTime(lundiPentecote);
                    if (Calendar.JUNE == cal.get(Calendar.MONTH)) {
                        // Cas où l'Ascension est au mois de mai et le lundi de Pentecôte au mois de juin
                        index = 2;
                    }
                }
                if (index == 0) {
                    dates = new Date[0];
                } else if (index == 1) {
                    dates = new Date[2];
                    dates[0] = Ascension;
                    dates[1] = lundiPentecote;
                } else if (index == 2) {
                    dates = new Date[1];
                    dates[0] = lundiPentecote;
                }
                break;

            case Calendar.AUGUST:
                // Un seul jour férié : la fête nationale suisse
                dates = new Date[1];
                dates[0] = getJourFerie(FETE_NATIONALE, pAnnee);
                break;

            case Calendar.SEPTEMBER:
                // Un seul jour férié : le jeûne genevois
                dates = new Date[1];
                dates[0] = getJeuneGenevois(pAnnee);
                break;

            case Calendar.DECEMBER:
                // 2 jours fériés : Noël et le 31 décembre (Restauration de la République)
                dates = new Date[2];
                dates[0] = getJourFerie(NOEL, pAnnee);
                dates[1] = getJourFerie(RESTAURATION_DE_LA_REPUBLIQUE, pAnnee);
                break;

            default:
                dates = new Date[0];
                break;
            }
            return dates;
        } else {
            assert false : pMois;
            throw new IllegalArgumentException(
                    "L'année doit être comprise entre 1 et 10000 et le mois entre Calendar.JANUARY et Calendar"
                            + ".DECEMBER.");
        }
    }

    @Override
    public int[] getJoursDTDFeriesDuMois(int pAnnee, int pMois) {
        DateValidator.checkAnneeValidite(pAnnee);
        Calendar cal = Calendar.getInstance();
        int[] datesDTD = new int[0];
        // On cherche les dates au format jav du mois pMois
        Date[] dates = getJoursFeriesDuMois(pAnnee, pMois);
        if (dates != null) {
            datesDTD = new int[dates.length];
            // On convertit les dates du format java au format DTD
            for (int i = 0; i < dates.length; i++) {
                datesDTD[i] = convert(cal, dates[i]);
            }
        }
        return datesDTD;
    }

    @Override
    public Date addJoursEtatOuvrables(final Date pDate, final int pNbreJoursOuvrables) {
        DateValidator.checkValidite(pDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        int compteurJoursOuvrables = 0;

        boolean add = pNbreJoursOuvrables > 0;
        int increment = add ? 1 : -1;
        int nbreJoursOuvrables = increment * pNbreJoursOuvrables;

        while (compteurJoursOuvrables < nbreJoursOuvrables) {
            cal.add(Calendar.DATE, 1 * increment);

            if (Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK)) {
                if (add) {
                    cal.add(Calendar.DATE, 2);
                } else {
                    cal.add(Calendar.DATE, -1);
                }
            } else if (Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK)) {
                if (add) {
                    cal.add(Calendar.DATE, 1);
                } else {
                    cal.add(Calendar.DATE, -2);
                }
            }

            if (!isJourEtatFerie(cal.getTime())) {
                compteurJoursOuvrables++;
            }
        }
        return cal.getTime();
    }

    @Override
    public int addJoursEtatOuvrables(final int pDTDDate, final int pNbreJoursOuvrables) {
        DateValidator.checkValidite(Integer.toString(pDTDDate), "yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        return convert(cal, addJoursEtatOuvrables(convert(cal, pDTDDate), pNbreJoursOuvrables));
    }

    @Override
    public Date addJoursOuvrables(final Date pDate, final int pNbreJoursOuvrables) {
        DateValidator.checkValidite(pDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        int compteurJoursOuvrables = 0;

        boolean add = pNbreJoursOuvrables > 0;
        int increment = add ? 1 : -1;
        int nbreJoursOuvrables = increment * pNbreJoursOuvrables;

        while (compteurJoursOuvrables < nbreJoursOuvrables) {
            cal.add(Calendar.DATE, 1 * increment);

            if (Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK)) {
                if (add) {
                    cal.add(Calendar.DATE, 2);
                } else {
                    cal.add(Calendar.DATE, -1);
                }
            } else if (Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK)) {
                if (add) {
                    cal.add(Calendar.DATE, 1);
                } else {
                    cal.add(Calendar.DATE, -2);
                }
            }

            if (!isJourFerie(cal.getTime())) {
                compteurJoursOuvrables++;
            }
        }
        return cal.getTime();
    }

    @Override
    public int addJoursOuvrables(final int pDTDDate, final int pNbreJoursOuvrables) {
        DateValidator.checkValidite(Integer.toString(pDTDDate), "yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        return convert(cal, addJoursOuvrables(convert(cal, pDTDDate), pNbreJoursOuvrables));
    }

    @Override
    public Date getProchainJourOuvrable(final Date pDate) {
        DateValidator.checkValidite(pDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        Date date = pDate;
        while (isSamedi(date) || isDimanche(date) || isJourFerie(date)) {
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        }
        return cal.getTime();
    }

    @Override
    public int getProchainJourOuvrable(final int pDTDDate) {
        DateValidator.checkValidite(Integer.toString(pDTDDate), "yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        return convert(cal, getProchainJourOuvrable(convert(cal, pDTDDate)));
    }

    @Override
    public Date getProchainJourEtatOuvrable(final Date pDate) {
        DateValidator.checkValidite(pDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        Date date = pDate;
        while (isSamedi(date) || isDimanche(date) || isJourEtatFerie(date)) {
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        }
        return cal.getTime();
    }

    @Override
    public int getProchainJourEtatOuvrable(final int pDTDDate) {
        DateValidator.checkValidite(Integer.toString(pDTDDate), "yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        return convert(cal, getProchainJourEtatOuvrable(convert(cal, pDTDDate)));
    }

    /**
     * Fixe l'heure du calendrier passà en paramètre à midi.
     *
     * @param pCal Le calendrier dont l'heure doit être fixàe à midi.
     */
    private static void setDefaultTime(Calendar pCal) {
        pCal.set(Calendar.HOUR_OF_DAY, HEURE_DEFAUT);
        pCal.set(Calendar.MINUTE, 0);
        pCal.set(Calendar.SECOND, 0);
        pCal.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Convertit une date au sens java (java.util.Date) en un entier : une date au sens DTD
     *
     * @param pCal  Une instance de Calendar
     * @param pDate La date au sens Java
     *
     * @return un entier : la date au format DTD (yyyyMMdd)
     */
    private static int convert(Calendar pCal, Date pDate) {
        pCal.setTime(pDate);
        return pCal.get(Calendar.YEAR) * CONV_ANNEE_DTD + (pCal.get(Calendar.MONTH) + 1) * CONV_MOIS_DTD + pCal
                .get(Calendar.DATE);
    }

    /**
     * Convertit une date au format DTD (yyyyMMdd) au format java.util.Date
     *
     * @param pCal     Une instance de Calendar
     * @param pDTDDate Un entier : la date au format DTD
     *
     * @return Une date au sens Java. L'heure est fixàe à 12h00.
     */
    private static Date convert(Calendar pCal, int pDTDDate) {
        int jour = pDTDDate % CONV_MOIS_DTD;
        int pDTDDateT = pDTDDate / CONV_MOIS_DTD;
        int mois = pDTDDateT % CONV_MOIS_DTD;
        int annee = pDTDDateT / CONV_SIECLE;
        pCal.set(annee, mois - 1, jour, NBR_MOIS, 0, 0);
        return pCal.getTime();
    }

}
