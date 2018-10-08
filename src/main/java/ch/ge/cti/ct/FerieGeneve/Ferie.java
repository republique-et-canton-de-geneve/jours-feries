package ch.ge.cti.ct.FerieGeneve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.ge.cti.ct.FerieGeneve.persistance.LectureConfig;
import ch.ge.cti.ct.FerieGeneve.persistance.ParamFermesAble;

/**
 * Cette classe utilitaire permet de connaître les jours fériés à l'État de Genève.
 * Les jours fériés officiels sont :
 * <ul>
 * <li>nouvel an <i>1<sup>er</sup> janvier</i>,</li>
 * <li>Vendredi-saint <i>2 jours avant Pâques</i>,</li>
 * <li>lundi de Pâques <i>1 jour après Pâques</i>,</li>
 * <li>jeudi de l'Ascension <i>39 jours après Pâques</i>,</li>
 * <li>lundi de Pentecôte <i>50 jours après Pâques</i>,</li>
 * <li>fête nationale <i>1<sup>er</sup> août</i>,</li>
 * <li>jeûne genevois <i>jeudi qui suit le premier dimanche de septembre</i>,</li>
 * <li>Noël <i>25 décembre</i>,</li>
 * <li>Restauration de la République <i>31 décembre</i></li>
 * </ul>
 * En plus, à l'État, il y a des jours fermés : le 1er mai, le lundi suivant le 1er août si celui-ci tombe un
 * dimanche et les 24, 26, 27, 28, 29 et 30 décembre. Les méthodes incluant ces jours fériés supplémentaires
 * contiennent le mot "Etat" dans leur nom (getJoursEtatFermesDuMois(), isJourEtatFerie(), ....).
 * <br>
 * Les jours ouvrables sont les jours qui ne sont pas fériés, qui ne sont pas samedi ni dimanche. De nouveau, on a
 * la notion de jour ouvrable général et de jour ouvrable à l'État de Genève (méthodes isJourOuvrable() et
 * isJourEtatOuvrable()).
 * <br>
 * Les méthodes existent en 2 exemplaires. Le premier avec des dates aux formats java.util.Date et le second avec des
 * dates au format DTD, par exemple yyyyMMdd.
 * <br>
 * Deux méthodes auxiliaires testent si la date est un samedi ou un dimanche.
 *
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 */
public final class Ferie {

    /**
     * Le premier jour de l'année : 1<sup>er</sup> janvier.
     */
    public static final int NOUVEL_AN = 0;

    /**
     * Vendredi-saint : 2 jours avant Pâques.
     */
    public static final int VENDREDI_SAINT = 1;

    /**
     * Le lendemain de Pâques-
     */
    public static final int LUNDI_PAQUES = 2;

    /**
     * Le jeudi de l'Ascension.
     */
    public static final int ASCENSION = 3;

    /**
     * Lendemain de Pentecôte.
     */
    public static final int LUNDI_PENTECOTE = 4;

    /**
     * Fête nationale suisse : commémore un traité d'alliance passé au début du mois d'août 1291 entre "les
     * hommes de la vallée d'Uri, la communauté de Schwytz et celle des hommes de la vallée inférieure
     * d'Unterwald".
     */
    public static final int FETE_NATIONALE = 5;

    /**
     * Jeûne genevois.
     */
    public static final int JEUNE_GENEVOIS = 6;

    /**
     * Noël.
     */
    public static final int NOEL = 7;

    /**
     * Restauration de la République du 31 décembre 1813 (fin de l'annexion de la République de Genève à la
     * France).
     */
    public static final int RESTAURATION_REPUBLIQUE = 8;

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

    private static final Logger LOG = Logger.getLogger(Ferie.class);

    // permet de lire les données concernant le paramètrage des jours fermés
    private static ParamFermesAble lecteurParam;

    private static Map<Integer, String[]> joursFermetureEtat = new HashMap<Integer, String[]>();

    /**
     * Lecteur de paramètres par défaut.
     */
    static {
        lecteurParam = new LectureConfig();
    }

    /**
     * On n'instancie pas cette classe.
     */
    protected Ferie() {
    }

    /**
     * Cette méthode teste si la date fournie en paramètre est un samedi.
     * @param pDate La date à tester
     * @return true si la date est un samedi
     */
    public static boolean isSamedi(Date pDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        return Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Cette méthode teste si la date fournie en paramètre est un samedi
     *
     * @param pDTDDate Un entier : la date à tester au format DTD (yyyyMMdd)
     *
     * @return true si la date est un samedi
     */
    public static boolean isSamedi(int pDTDDate) {
        Calendar cal = Calendar.getInstance();
        return isSamedi(convert(cal, pDTDDate));
    }

    /**
     * Cette méthode teste si la date fournie en paramètre est un dimanche
     *
     * @param pDate La date à tester
     *
     * @return true si la date est un dimanche
     */
    public static boolean isDimanche(Date pDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        return Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Cette méthode teste si la date fournie en paramètre est un dimanche
     *
     * @param pDTDDate Un entier : la date à tester au format DTD (yyyyMMdd)
     *
     * @return true si la date est un dimanche
     */
    public static boolean isDimanche(int pDTDDate) {
        Calendar cal = Calendar.getInstance();
        return isDimanche(convert(cal, pDTDDate));
    }

    /**
     * Retourne vrai si la date passée en argument est un jour férié
     *
     * @param pDate
     *
     * @return <code>true</code> si le date est un jour férié officiel.
     */
    public static boolean isJourFerie(Date pDate) {
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

    /**
     * Retourne vrai si la date passée en argument est un jour férié
     *
     * @param pDTDDate Un entier correspondant à la date au format DTD (yyyyMMdd)
     *
     * @return <code>true</code> si le date est un jour férié officiel.
     */
    public static boolean isJourFerie(int pDTDDate) {
        Calendar cal = Calendar.getInstance();
        return isJourFerie(convert(cal, pDTDDate));
    }

    /**
     * Retourne vrai si la date passée en argument est un jour férié à l'État i.e. s'il s'agit d'un jour
     * férié officiel ou d'un jours fermés (1er mai, 26 décembre, ....)
     *
     * @param pDate
     *
     * @return <code>true</code> si le date est un jour férié officiel.
     */
    public static boolean isJourEtatFerie(Date pDate) {
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

    /**
     * Retourne vrai si la date passée en argument est un jour férié à l'État i.e. s'il s'agit d'un jour
     * férié officiel ou d'un jours fermés (1er mai, 26 décembre, ....)
     *
     * @param pDTDDate Un entier correspondant à la date au format DTD (yyyyMMdd)
     *
     * @return <code>true</code> si le date est un jour férié officiel.
     */
    public static boolean isJourEtatFerie(int pDTDDate) {
        Calendar cal = Calendar.getInstance();
        return isJourEtatFerie(convert(cal, pDTDDate));
    }

    /**
     * Cette méthode retourne vrai si le jour est un jour de la semaine (hors samedi et dimanche) qui n'est pas
     * férié.
     *
     * @param pDate La date
     *
     * @return <code>true</code> si la date passée en paramètre est un jour ouvrable.
     */
    public static boolean isJourOuvrable(Date pDate) {
        return !(isSamedi(pDate) || isDimanche(pDate) || isJourFerie(pDate));
    }

    /**
     * Cette méthode retourne vrai si le jour est un jour de la semaine (hors samedi et dimanche) qui n'est pas
     * férié.
     *
     * @param pDtdDate Un entier correspondant à la date au format DTD (yyyyMMdd)
     *
     * @return <code>true</code> si la date passée en paramètre est un jour ouvrable.
     */
    public static boolean isJourOuvrable(int pDtdDate) {
        return !(isSamedi(pDtdDate) || isDimanche(pDtdDate) || isJourFerie(pDtdDate));
    }

    /**
     * Cette méthode retourne vrai si le jour est un jour de la semaine (hors samedi et dimanche) qui n'est ni un jour
     * férié ni un jour fermé à l'État.
     *
     * @param pDate La date
     *
     * @return <code>true</code> si la date passée en paramètre est un jour ouvrable au sens de l'État..
     */
    public static boolean isJourEtatOuvrable(Date pDate) {
        return !(isSamedi(pDate) || isDimanche(pDate) || isJourEtatFerie(pDate));
    }

    /**
     * Cette méthode retourne vrai si le jour est un jour de la semaine (hors samedi et dimanche) qui n'est ni un jour
     * férié ni un jour fermé à l'État.
     *
     * @param pDtdDate Un entier correspondant à la date au format DTD (yyyyMMdd)
     *
     * @return <code>true</code> si la date passée en paramètre est un jour ouvrable au sens de l'État..
     */
    public static boolean isJourEtatOuvrable(int pDtdDate) {
        return !(isSamedi(pDtdDate) || isDimanche(pDtdDate) || isJourEtatFerie(pDtdDate));
    }

    /**
     * Cette méthode permet d'obtenir la date d'un jour férié pour une année donnée. Par exemple, pour obtenir
     * la date du jeudi de l'ascension de l'année 2003, on appelera <code>getJourFerie(UtiFerie.ASCENSION,2003)
     * </code>.
     *
     * @param pJourFerie Une des constantes <code>NOUVEL_AN, VENDREDI_SAINT, LUNDI_PAQUES, ASCENSION, LUNDI_PENTECOTE,
     *                   FETE_NATIONALE, JEUNE_GENEVOIS, NOEL, RESTAURATION_REPUBLIQUE</code>.
     * @param pAnnee     Une année comprise entre 0 et 10000.
     *
     * @return La date du jour férié
     */
    public static Date getJourFerie(int pJourFerie, int pAnnee) {
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

            case LUNDI_PAQUES:
                cal.setTime(getDatePaques(pAnnee));
                cal.add(Calendar.DATE, ECART_LUNDI_PAQUES);
                date = cal.getTime();
                break;

            case ASCENSION:
                cal.setTime(getDatePaques(pAnnee));
                cal.add(Calendar.DATE, ECART_ASCENSION_PAQUES);
                date = cal.getTime();
                break;

            case LUNDI_PENTECOTE:
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

            case RESTAURATION_REPUBLIQUE:
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

    /**
     * Cette méthode permet d'obtenir la date d'un jour férié pour une année donnée. Par exemple, pour obtenir
     * la date du jeudi de l'ascension de l'année 2003, on appelera <code>getJourDTDFerie(UtiFerie.ASCENSION,2003)
     * </code>.
     *
     * @param pJourFerie Une des constantes <code>NOUVEL_AN, VENDREDI_SAINT, LUNDI_PAQUES, ASCENSION, LUNDI_PENTECOTE,
     *                   FETE_NATIONALE, JEUNE_GENEVOIS, NOEL, RESTAURATION_REPUBLIQUE</code>
     * @param pAnnee     Une année comprise entre 0 et 10000.
     *
     * @return Un entier : la date du jour férié au format DTD (yyyyMMdd)
     */
    public static int getJourDTDFerie(int pJourFerie, int pAnnee) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getJourFerie(pJourFerie, pAnnee));
        return cal.get(Calendar.YEAR) * CONV_ANNEE_DTD + (cal.get(Calendar.MONTH) + 1) * CONV_MOIS_DTD + cal
                .get(Calendar.DATE);
    }

    /**
     * Cet algorithme provient de l'ouvrage de Donald Knuth <i>Fundamental Algorithms</i>.
     *
     * @param pAnnee
     *
     * @return la date de Pâques (l'heure est fixàe à midi)
     */
    private static Date getDatePaques(int pAnnee) {
        Calendar cal = Calendar.getInstance();
        int nombreOr = pAnnee % MOD_PAQUE + 1;
        int siecle = pAnnee / 100 + 1;

        // Nombre d'année divisible par 4 qui ne sont pas bisextile. Comprend le dàcalage
        // du pape Gràgoire
        int pasBisextile = 3 * siecle / 4 - 12;

        int synchroLune = (8 * siecle + 5) / 25 - 5;
        int dimanche = (5 * pAnnee) / 4 - pasBisextile - 10;

        // Epacte (date de la pleine lune)
        int epacte = (((11 * nombreOr) + 20 + synchroLune - pasBisextile)) % 30;
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

    /**
     * Cette méthode retourne la date du jeûne genevois pour une année donnée. Le jeûne genevois est le jeudi
     * qui suit le premier dimanche de septembre.
     *
     * @param pAnnee
     *
     * @return la date du jeûne genevois
     */
    private static Date getJeuneGenevois(int pAnnee) {
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

    /**
     * Cette méthode retourne le tableau des jours fériés de l'année passée en paramètre. Le tableau est
     * trià par ordre chronologique croissant.
     *
     * @param pAnnee
     *
     * @return le tableau des jours fériés
     */
    public static Date[] getJoursFeries(int pAnnee) {
        Date[] dates = new Date[9];
        Calendar cal = Calendar.getInstance();
        setDefaultTime(cal);
        // On commence par les dates fixes

        // Jour de l'an
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.YEAR, pAnnee);
        dates[NOUVEL_AN] = cal.getTime();

        // Fàte nationale
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.YEAR, pAnnee);
        dates[FETE_NATIONALE] = cal.getTime();

        // Noàl
        cal.set(Calendar.DATE, NOEL_JOUR);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.YEAR, pAnnee);
        dates[NOEL] = cal.getTime();

        // Restauration de la Ràpublique
        cal.set(Calendar.DATE, RESTAURATION_JOUR);
        dates[RESTAURATION_REPUBLIQUE] = cal.getTime();

        // On continue avec les dates qui dàpendent de la date de Pâques
        // Vendredi saint
        cal.setTime(getDatePaques(pAnnee));
        cal.add(Calendar.DATE, ECART_VENDREDI_SAINT_PAQUES);
        dates[VENDREDI_SAINT] = cal.getTime();
        // Lundi de Pâques
        cal.add(Calendar.DATE, ECART_LUNDI_PAQUES - ECART_VENDREDI_SAINT_PAQUES);
        dates[LUNDI_PAQUES] = cal.getTime();
        // Jeudi de l'ascension
        cal.add(Calendar.DATE, ECART_ASCENSION_PAQUES - ECART_LUNDI_PAQUES);
        dates[ASCENSION] = cal.getTime();
        // Lundi de Pentecôte
        cal.add(Calendar.DATE, ECART_LUNDI_PENTECOTE_PAQUES - ECART_ASCENSION_PAQUES);
        dates[LUNDI_PENTECOTE] = cal.getTime();

        // Reste le jeûne genevois
        dates[JEUNE_GENEVOIS] = getJeuneGenevois(pAnnee);

        return dates;
    }

    /**
     * Cette méthode retourne le tableau des jours fériés de l'année passée en paramètre. Le tableau est
     * trià par ordre chronologique croissant.
     *
     * @param pAnnee
     *
     * @return le tableau des jours fériés au format DTD (yyyyMMdd)
     */
    public static int[] getJoursDTDFeries(int pAnnee) {
        Calendar cal = Calendar.getInstance();
        Date[] dates = getJoursFeries(pAnnee);
        int[] datesDTD = new int[dates.length];
        for (int i = 0; i < dates.length; i++) {
            datesDTD[i] = convert(cal, dates[i]);
        }
        return datesDTD;
    }

    /**
     * Cette méthode retourne les jours fermés d'un mois donné et d'une année donnée. Les jours fermés ne
     * sont pas des jours fériés officiels.
     * <br>
     * <strong>Attention</strong>, le paramètre pMois est le mois au sens du Calendar java
     * <i>i.e.</i> Calendar.JANUARY = 0, .....
     *
     * @param pAnnee une année comprise entre 0 et 10000.
     * @param pMois  Un mois au sens java <i>i.e.</i> Calendar.JANUARY = 0, ..
     *
     * @return Un tableau (qui peut être vide) contenant les jours fermés du mois.
     */
    public static Date[] getJoursEtatFermesDuMois(int pAnnee, int pMois) {
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
                // Au mois d'aoàt, si le 1er aoàt tombe un dimanche, il est rattrapà le lundi suivant
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
                // Au mois de décembre, les 24, 26, 27, 28, 29 et 30 décembre àtaient fermés avant 2011
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

            // aprés 2011 les dates de fermetures sont paramàtràes
            if (pAnnee >= ANNEE_PARAM) {
                List<Date> joursFermes = getJoursFermesParConseilEtat(pAnnee, pMois);
                if (dates != null) {
                    joursFermes.addAll(Arrays.asList(dates));
                }
                dates = (Date[]) joursFermes.toArray(new Date[joursFermes.size()]);
            }

            return dates;
        } else {
            throw new IllegalArgumentException(
                    "L'année doit être comprise entre 1 et 10000 et le mois entre Calendar.JANUARY et Calendar"
                            + ".DECEMBER.");
        }
    }

    private static List<Date> getJoursFermesParConseilEtat(int pAnnee, int pMois) {
        String[] tabJoursFermes = (String[]) joursFermetureEtat.get(Integer.valueOf(pAnnee));
        if (tabJoursFermes == null) {
            //LECTURE des joursFermes format dd/MM/yyyy
            tabJoursFermes = lecteurParam.getJoursFermes(pAnnee);
            joursFermetureEtat.put(Integer.valueOf(pAnnee), tabJoursFermes);
        }

        Calendar cal = Calendar.getInstance();
        List<Date> listeJoursFermes = new ArrayList<Date>();
        for (int i = 0; i < tabJoursFermes.length; i++) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date jourFerme = df.parse(tabJoursFermes[i] + "/" + pAnnee);
                cal.setTime(jourFerme);
                if (cal.get(Calendar.MONTH) == pMois) {
                    listeJoursFermes.add(jourFerme);
                }
            } catch (ParseException ex) {
                LOG.error("erreur au niveau du formatage de la date reçue : " + tabJoursFermes[i]
                        + " attendue : dd/MM/yyyy", ex);
                // sysout( "erreur au niveau du formatage de la date reçue : " + tabJoursFermes[i] + " attendue :
                // dd/MM/yyyy")
            }
        }

        return listeJoursFermes;
    }

    /**
     * Cette méthode retourne les jours fermés d'un mois donné et d'une année donnée. Les jours fermés ne
     * sont pas des jours fériés officiels.
     * <br>
     * <strong>Attention</strong>, le paramètre pMois est le mois au sens du Calendar java
     * <i>i.e.</i> Calendar.JANUARY = 0, .....
     *
     * @param pAnnee une année comprise entre 0 et 10000.
     * @param pMois  Un mois au sens java <i>i.e.</i> Calendar.JANUARY = 0, ..
     *
     * @return Un tableau (qui peut être vide) contenant les jours fériés du mois au format DTD (yyyyMMdd).
     */
    public static int[] getJoursDTDEtatFermesDuMois(int pAnnee, int pMois) {
        Calendar cal = Calendar.getInstance();
        // On cherche les dates au format jav du mois pMois
        Date[] dates = getJoursEtatFermesDuMois(pAnnee, pMois);
        int[] datesDTD = new int[dates.length];
        // On convertit les dates du format java au format DTD
        for (int i = 0; i < dates.length; i++) {
            datesDTD[i] = convert(cal, dates[i]);
        }
        return datesDTD;
    }

    /**
     * Cette méthode retourne les jours fériés d'un mois donné et d'une année donnée.
     * <br>
     * <strong>Attention</strong>, le paramètre pMois est le mois au sens du Calendar java
     * <i>i.e.</i> Calendar.JANUARY = 0, .....
     *
     * @param pAnnee une année comprise entre 0 et 10000.
     * @param pMois  Un mois au sens java <i>i.e.</i> Calendar.JANUARY = 0, ..
     *
     * @return Un tableau (qui peut être vide) contenant les jours fériés du mois.
     */
    public static Date[] getJoursFeriesDuMois(int pAnnee, int pMois) {
        Date[] dates = null;
        // Teste si les paramètres sont bien entre les bornes voulues
        if (pAnnee > -1 && pAnnee < ANNEE_MAX && pMois > -1 && pMois < NBR_MOIS) {
            Calendar cal;
            int index;
            Date vendrediSaint, lundiPaques, ascension, lundiPentecote;
            switch (pMois) {
            case Calendar.JANUARY:
                // Il n'y a qu'un jour férié au mois de janvier : le nouvel an
                dates = new Date[1];
                dates[0] = getJourFerie(NOUVEL_AN, pAnnee);
                break;

            case Calendar.MARCH:
                // Au mois de mars, il peut y avoir le vendredi saint et le lundi de Pâques
                cal = Calendar.getInstance();
                index = 0;
                vendrediSaint = getJourFerie(VENDREDI_SAINT, pAnnee);
                lundiPaques = null;
                cal.setTime(vendrediSaint);
                if (Calendar.MARCH == cal.get(Calendar.MONTH)) {
                    // Cas oà le vendredi saint est en mars
                    index = 1;
                    lundiPaques = getJourFerie(LUNDI_PAQUES, pAnnee);
                    cal.setTime(lundiPaques);
                    if (Calendar.MARCH == cal.get(Calendar.MONTH)) {
                        // Cas oà le lundi de Pâques est aussi en mars
                        index = 2;
                    }
                }
                switch (index) {
                case 0:
                    dates = new Date[0];
                    break;

                case 1:
                    dates = new Date[1];
                    dates[0] = vendrediSaint;
                    break;

                case 2:
                    dates = new Date[2];
                    dates[0] = vendrediSaint;
                    dates[1] = lundiPaques;
                    break;

                default:
                    break;
                }
                break;

            case Calendar.APRIL:
                cal = Calendar.getInstance();
                index = 0;
                vendrediSaint = getJourFerie(VENDREDI_SAINT, pAnnee);
                lundiPaques = getJourFerie(LUNDI_PAQUES, pAnnee);
                ascension = null;
                lundiPentecote = null;
                cal.setTime(vendrediSaint);
                if (Calendar.APRIL == cal.get(Calendar.MONTH)) {
                    // Cas oà le vendredi saint est au mois d'avril. Dans ce cas, l'Ascension et le lundi
                    // de Pentecôte ne peuvent pas être au mois d'avril.
                    // Par contre, forcément, le lundi de Pâques est aussi au mois d'avril.
                    index = 1;
                } else {
                    // Le vendredi saint est au mois de mars
                    cal.setTime(lundiPaques);
                    if (Calendar.APRIL == cal.get(Calendar.MONTH)) {
                        // Cas oà le lundi de Pâques est au mois d'avril
                        index = 2;
                    } else {
                        // Cas oà le lundi de Pâques est au mois de mars
                        lundiPentecote = getJourFerie(LUNDI_PENTECOTE, pAnnee);
                        cal.setTime(lundiPentecote);
                        if (Calendar.APRIL == cal.get(Calendar.MONTH)) {
                            // Cas oà le lundi de Pentecôte est au mois d'avril
                            // Forcàment, l'ascension est aussi au mois d'avril
                            index = 4;
                        } else {
                            // Si le lundi de Pentecôte n'est pas au mois d'avril,
                            // l'ascension peut tout de màme être au mois d'avril
                            ascension = getJourFerie(ASCENSION, pAnnee);
                            cal.setTime(ascension);
                            if (Calendar.APRIL == cal.get(Calendar.MONTH)) {
                                index = 3;
                            }

                        }
                    }
                }
                switch (index) {
                case 0:
                    dates = new Date[0];
                    break;

                case 1:
                    dates = new Date[2];
                    dates[0] = vendrediSaint;
                    dates[1] = lundiPaques;
                    break;

                case 2:
                    dates = new Date[1];
                    dates[0] = lundiPaques;
                    break;

                case 3:
                    dates = new Date[1];
                    dates[0] = ascension;
                    break;

                case 4:
                    dates = new Date[2];
                    dates[0] = ascension;
                    dates[1] = lundiPentecote;
                    break;

                default:
                    break;
                }
                break;

            case Calendar.MAY:
                // Au mois de mai, il peut y avoir l'ascension et le lundi de Pentecôte
                cal = Calendar.getInstance();
                index = 0;
                ascension = getJourFerie(ASCENSION, pAnnee);
                lundiPentecote = getJourFerie(LUNDI_PENTECOTE, pAnnee);
                cal.setTime(ascension);
                if (Calendar.MAY == cal.get(Calendar.MONTH)) {
                    // Cas oà l'ascension est au mois de mai
                    index = 1;
                    cal.setTime(lundiPentecote);
                    if (Calendar.MAY == cal.get(Calendar.MONTH)) {
                        // Cas oà l'ascension et le lundi de Pentecôte sont au mois de mai.
                        index = 3;
                    }
                } else {
                    cal.setTime(lundiPentecote);
                    if (Calendar.MAY == cal.get(Calendar.MONTH)) {
                        // Cas oà l'ascension est au mois d'avril et le lundi de Pentecôte au mois de mai.
                        index = 2;
                    }
                }
                switch (index) {
                case 0:
                    dates = new Date[0];
                    break;

                case 1:
                    dates = new Date[1];
                    dates[0] = ascension;
                    break;

                case 2:
                    dates = new Date[1];
                    dates[0] = lundiPentecote;
                    break;

                case 3:
                    dates = new Date[2];
                    dates[0] = ascension;
                    dates[1] = lundiPentecote;
                    break;

                default:
                    break;
                }
                break;

            case Calendar.JUNE:
                // Il est possible d'avoir l'ascension et le lundi de Pentecôte au mois de juin
                cal = Calendar.getInstance();
                index = 0;
                ascension = getJourFerie(ASCENSION, pAnnee);
                lundiPentecote = getJourFerie(LUNDI_PENTECOTE, pAnnee);
                cal.setTime(ascension);
                if (Calendar.JUNE == cal.get(Calendar.MONTH)) {
                    // Cas oà l'ascension est au mois de juin, alors forcément le lundi de Pentecôte
                    // est aussi au mois de juin
                    index = 1;
                } else {
                    cal.setTime(lundiPentecote);
                    if (Calendar.JUNE == cal.get(Calendar.MONTH)) {
                        // Cas oà l'ascension est au mois de mai et le lundi de Pentecôte au mois de juin
                        index = 2;
                    }
                }
                switch (index) {
                case 0:
                    dates = new Date[0];
                    break;

                case 1:
                    dates = new Date[2];
                    dates[0] = ascension;
                    dates[1] = lundiPentecote;
                    break;

                case 2:
                    dates = new Date[1];
                    dates[0] = lundiPentecote;
                    break;

                default:
                    break;
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
                // 2 jours fériés : Noàl et le 31 décembre (Restauration de la Ràpublique)
                dates = new Date[2];
                dates[0] = getJourFerie(NOEL, pAnnee);
                dates[1] = getJourFerie(RESTAURATION_REPUBLIQUE, pAnnee);
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

    /**
     * Cette méthode retourne les jours fériés d'un mois donné et d'une année donnée.
     * <br>
     * <strong>Attention</strong>, le paramètre pMois est le mois au sens du Calendar java
     * <i>i.e.</i> Calendar.JANUARY = 0, .....
     *
     * @param pAnnee une année comprise entre 0 et 10000.
     * @param pMois  Un mois au sens java <i>i.e.</i> Calendar.JANUARY = 0, ..
     *
     * @return Un tableau (qui peut être vide) contenant les jours fériés du mois au format DTD (yyyyMMdd).
     */
    public static int[] getJoursDTDFeriesDuMois(int pAnnee, int pMois) {
        Calendar cal = Calendar.getInstance();
        // On cherche les dates au format jav du mois pMois
        Date[] dates = getJoursFeriesDuMois(pAnnee, pMois);
        int[] datesDTD = new int[dates.length];
        // On convertit les dates du format java au format DTD
        for (int i = 0; i < dates.length; i++) {
            datesDTD[i] = convert(cal, dates[i]);
        }
        return datesDTD;
    }

    /**
     * Cette méthode retourne la date passée en paramètre (pDate) à laquelle on a ajouté pNbreJoursOuvrables
     * jours ouvrables. Les jours ouvrables sont les jours de la semaine excepté le samedi, le dimanche, les jours
     * fériés et les jours fermés à l'État. Par exemple, si pDate représente le 23 décembre 2004, l'appel
     * addJoursOuvrables(pDate,6) doit retourner le 10 janvier 2005.
     *
     * @param pDate               La date à laquelle on veut ajouter des jours ouvrables
     * @param pNbreJoursOuvrables Le nombre de jours ouvrables à ajouter ou soustraire.
     *
     * @return pDate augmentàe de pNbreJoursOuvrables
     */
    public static Date addJoursEtatOuvrables(final Date pDate, final int pNbreJoursOuvrables) {
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

    /**
     * Cette méthode retourne la date passée en paramètre (pDate) à laquelle on a ajouté pNbreJoursOuvrables
     * jours ouvrables. Les jours ouvrables sont les jours de la semaine excepté le samedi, le dimanche, les jours
     * fériés et les jours fermés à l'État. Par exemple, si pDate représente le 23 décembre 2004, l'appel
     * addJoursOuvrables(pDate,6) doit retourner le 10 janvier 2005.
     *
     * @param pDTDDate            Un entier : la date au format DTD (yyyyMMdd) à laquelle on veut ajouter des jours
     *                            ouvrables
     * @param pNbreJoursOuvrables Le nombre de jours ouvrables à ajouter. Ce nombre doit être &gt;=0.
     *
     * @return Un entier : pDate augmentàe de pNbreJoursOuvrables au format DTD (yyyyMMdd)
     */
    public static int addJoursEtatOuvrables(final int pDTDDate, final int pNbreJoursOuvrables) {
        Calendar cal = Calendar.getInstance();
        return convert(cal, addJoursEtatOuvrables(convert(cal, pDTDDate), pNbreJoursOuvrables));
    }

    /**
     * Cette méthode retourne la date passée en paramètre (pDate) à laquelle on a ajouté pNbreJoursOuvrables
     * jours ouvrables. Les jours ouvrables sont les jours de la semaine exceptés le samedi, le dimanche et les jours
     * fériés officiels. Par exemple, si pDate représente le 23 décembre 2004, l'appel
     * addJoursOuvrables(pDate,6) doit retourner le 2 janvier 2005.
     *
     * @param pDate               La date à laquelle on veut ajouter des jours ouvrables
     * @param pNbreJoursOuvrables Le nombre de jours ouvrables à ajouter ou soustraire..
     *
     * @return pDate augmentàe de pNbreJoursOuvrables
     */
    public static Date addJoursOuvrables(final Date pDate, final int pNbreJoursOuvrables) {
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

    /**
     * Cette méthode retourne la date passée en paramètre (pDate) à laquelle on a ajouté pNbreJoursOuvrables
     * jours ouvrables. Les jours ouvrables sont les jours de la semaine exceptés le samedi, le dimanche et les jours
     * fériés officiels. Par exemple, si pDate représente le 23 décembre 2004, l'appel
     * addJoursOuvrables(pDate,6) doit retourner le 2 janvier 2005.
     *
     * @param pDTDDate            Un entier : la date au format DTD (yyyyMMdd) à laquelle on veut ajouter des jours
     *                            ouvrables
     * @param pNbreJoursOuvrables Le nombre de jours ouvrables à ajouter. Ce nombre doit être &gt;=0.
     *
     * @return Un entier : pDate augmentàe de pNbreJoursOuvrables au format DTD (yyyyMMdd)
     */
    public static int addJoursOuvrables(final int pDTDDate, final int pNbreJoursOuvrables) {
        Calendar cal = Calendar.getInstance();
        return convert(cal, addJoursOuvrables(convert(cal, pDTDDate), pNbreJoursOuvrables));
    }

    /**
     * Cette méthode retourne la prochaine date suivant la date donnée en paramètre qui est un jour ouvrable. Si
     * la date donnée en paramètre est un jour ouvrable, c'est cette màme date qui est retournàe.
     *
     * @param pDate La date à partir de laquelle on veut le prochain jour ouvrable
     *
     * @return La date suivant la date donnée en paramètre qui est ouvrable.
     */
    public static Date getProchainJourOuvrable(final Date pDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        Date date = pDate;
        while (isSamedi(date) || isDimanche(date) || isJourFerie(date)) {
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        }
        return cal.getTime();
    }

    /**
     * Cette méthode retourne la prochaine date suivant la date donnée en paramètre qui est un jour ouvrable. Si
     * la date donnée en paramètre est un jour ouvrable, c'est cette màme date qui est retournàe.
     *
     * @param pDTDDate Un entier : la date au format DTD (yyyyMMdd) à partir de laquelle on veut le prochain jour
     *                 ouvrable
     *
     * @return La date au format DTD (yyyyMMdd) suivant la date donnée en paramètre qui est ouvrable.
     */
    public static int getProchainJourOuvrable(final int pDTDDate) {
        Calendar cal = Calendar.getInstance();
        return convert(cal, getProchainJourOuvrable(convert(cal, pDTDDate)));
    }

    /**
     * Cette méthode retourne la prochaine date suivant la date donnée en paramètre qui est un jour ouvrable à
     * l'État de Genàve. Si la date donnée en paramètre est un jour ouvrable, c'est cette màme date qui est
     * retournàe.
     *
     * @param pDate La date à partir de laquelle on veut le prochain jour ouvrable (pour l'État)
     *
     * @return La date suivant la date donnée en paramètre qui est ouvrable.
     */
    public static Date getProchainJourEtatOuvrable(final Date pDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        Date date = pDate;
        while (isSamedi(date) || isDimanche(date) || isJourEtatFerie(date)) {
            cal.add(Calendar.DATE, 1);
            date = cal.getTime();
        }
        return cal.getTime();
    }

    /**
     * Cette méthode retourne la prochaine date suivant la date donnée en paramètre qui est un jour ouvrable à
     * l'État de Genàve. Si la date donnée en paramètre est un jour ouvrable, c'est cette màme date qui est
     * retournàe.
     *
     * @param pDTDDate Un entier : la date au format DTD (yyyyMMdd) à partir de laquelle on veut le prochain jour
     *                 ouvrable (pour l'État)
     *
     * @return La date au format DTD (yyyyMMdd) suivant la date donnée en paramètre qui est ouvrable.
     */
    public static int getProchainJourEtatOuvrable(final int pDTDDate) {
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
        int jour, mois, annee;
        jour = pDTDDate % CONV_MOIS_DTD;
        int pDTDDateT = pDTDDate / CONV_MOIS_DTD;
        mois = pDTDDateT % CONV_MOIS_DTD;
        annee = pDTDDateT / CONV_SIECLE;
        pCal.set(annee, mois - 1, jour, NBR_MOIS, 0, 0);
        return pCal.getTime();

    }

    /**
     * Permet de définir le lecteur des paramètres
     *
     * @param lecteur
     */
    public static void init(ParamFermesAble lecteur) {
        lecteurParam = lecteur;
    }
}
