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
package ch.ge.cti.ct.joursferies.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
public interface JoursFeriesService {

    /**
     * Cette méthode teste si la date fournie en paramètre est un samedi.
     * @param pDate La date à tester
     * @return true si la date est un samedi
     */
    boolean isSamedi(Date pDate);

    /**
     * Cette méthode teste si la date fournie en paramètre est un samedi
     * @param pDTDDate Un entier : la date à tester au format DTD (yyyyMMdd)
     * @return true si la date est un samedi
     */
    boolean isSamedi(int pDTDDate);

    /**
     * Cette méthode teste si la date fournie en paramètre est un dimanche
     * @param pDate La date à tester
     * @return true si la date est un dimanche
     */
    boolean isDimanche(Date pDate);

    /**
     * Cette méthode teste si la date fournie en paramètre est un dimanche
     * @param pDTDDate Un entier : la date à tester au format DTD (yyyyMMdd)
     * @return true si la date est un dimanche
     */
    boolean isDimanche(int pDTDDate);

    /**
     * Retourne vrai si la date passée en argument est un jour férié
     * @param pDate La date à tester
     * @return <code>true</code> si le date est un jour férié officiel.
     */
    boolean isJourFerie(Date pDate);

    /**
     * Retourne vrai si la date passée en argument est un jour férié
     * @param pDTDDate Un entier correspondant à la date au format DTD (yyyyMMdd)
     * @return <code>true</code> si le date est un jour férié officiel.
     */
    boolean isJourFerie(int pDTDDate);

    /**
     * Retourne vrai si la date passée en argument est un jour férié à l'État i.e. s'il s'agit d'un jour
     * férié officiel ou d'un jours fermés (1er mai, 26 décembre, ....)
     * @param pDate La date à tester
     * @return <code>true</code> si le date est un jour férié officiel.
     */
    boolean isJourEtatFerie(Date pDate);

    /**
     * Retourne vrai si la date passée en argument est un jour férié à l'État i.e. s'il s'agit d'un jour
     * férié officiel ou d'un jours fermés (1er mai, 26 décembre, ....)
     * @param pDTDDate Un entier correspondant à la date au format DTD (yyyyMMdd)
     * @return <code>true</code> si le date est un jour férié officiel.
     */
    boolean isJourEtatFerie(int pDTDDate);

    /**
     * Cette méthode retourne vrai si le jour est un jour de la semaine (hors samedi et dimanche) qui n'est pas
     * férié.
     * @param pDate La date
     * @return <code>true</code> si la date passée en paramètre est un jour ouvrable.
     */
    boolean isJourOuvrable(Date pDate);

    /**
     * Cette méthode retourne vrai si le jour est un jour de la semaine (hors samedi et dimanche) qui n'est pas
     * férié.
     * @param pDtdDate Un entier correspondant à la date au format DTD (yyyyMMdd)
     * @return <code>true</code> si la date passée en paramètre est un jour ouvrable.
     */
    boolean isJourOuvrable(int pDtdDate);

    /**
     * Cette méthode retourne vrai si le jour est un jour de la semaine (hors samedi et dimanche) qui n'est ni un jour
     * férié ni un jour fermé à l'État.
     * @param pDate La date
     * @return <code>true</code> si la date passée en paramètre est un jour ouvrable au sens de l'État..
     */
    boolean isJourEtatOuvrable(Date pDate);

    /**
     * Cette méthode retourne vrai si le jour est un jour de la semaine (hors samedi et dimanche) qui n'est ni un jour
     * férié ni un jour fermé à l'État.
     * @param pDtdDate Un entier correspondant à la date au format DTD (yyyyMMdd)
     * @return <code>true</code> si la date passée en paramètre est un jour ouvrable au sens de l'État
     */
    boolean isJourEtatOuvrable(int pDtdDate);

    /**
     * Cette méthode permet d'obtenir la date d'un jour férié pour une année donnée. Par exemple, pour obtenir
     * la date du jeudi de l'Ascension de l'année 2003, on appelera <code>getJourFerie(UtiFerie.ASCENSION,2003)
     * </code>.
     * @param pJourFerie Un jour férié
     * @param pAnnee     Une année comprise entre 0 et 10000.
     * @return La date du jour férié
     */
    Date getJourFerie(JourFerie pJourFerie, int pAnnee);

    /**
     * Cette méthode permet d'obtenir la date d'un jour férié pour une année donnée. Par exemple, pour obtenir
     * la date du jeudi de l'Ascension de l'année 2003, on appelera <code>getJourDTDFerie(UtiFerie.ASCENSION,2003)
     * </code>.
     * @param pJourFerie Un jour férié
     * @param pAnnee     Une année comprise entre 0 et 10000.
     * @return Un entier : la date du jour férié au format DTD (yyyyMMdd)
     */
    int getJourDTDFerie(JourFerie pJourFerie, int pAnnee);

    /**
     * Cet algorithme provient de l'ouvrage de Donald Knuth <i>Fundamental Algorithms</i>.
     * @param pAnnee
     * @return la date de Pâques (l'heure est fixàe à midi)
     */
    Date getDatePaques(int pAnnee);

    /**
     * Cette méthode retourne la date du jeûne genevois pour une année donnée. Le jeûne genevois est le jeudi
     * qui suit le premier dimanche de septembre.
     * @param pAnnee
     * @return la date du jeûne genevois
     */
    Date getJeuneGenevois(int pAnnee);

    /**
     * Cette méthode retourne le tableau des jours fériés de l'année passée en paramètre. Le tableau est
     * trià par ordre chronologique croissant.
     * @param pAnnee
     * @return le tableau des jours fériés
     */
    Map<JourFerie, Date> getJoursFeries(int pAnnee);

    /**
     * Cette méthode retourne le tableau des jours fériés de l'année passée en paramètre. Le tableau est
     * trià par ordre chronologique croissant.
     * @param pAnnee
     * @return le tableau des jours fériés au format DTD (yyyyMMdd)
     */
    Map<JourFerie, Integer> getJoursDTDFeries(int pAnnee);

    /**
     * Cette méthode retourne les jours fermés d'un mois donné et d'une année donnée. Les jours fermés ne
     * sont pas des jours fériés officiels.
     * <br>
     * <strong>Attention</strong>, le paramètre pMois est le mois au sens du Calendar java
     * <i>i.e.</i> Calendar.JANUARY = 0, .....
     * @param pAnnee une année comprise entre 0 et 10000.
     * @param pMois  Un mois au sens java <i>i.e.</i> Calendar.JANUARY = 0, ..
     * @return Un tableau (qui peut être vide) contenant les jours fermés du mois.
     */
    Date[] getJoursEtatFermesDuMois(int pAnnee, int pMois);
    
    List<Date> getJoursFermesParConseilEtat(int pAnnee, int pMois);

    /**
     * Cette méthode retourne les jours fermés d'un mois donné et d'une année donnée. Les jours fermés ne
     * sont pas des jours fériés officiels.
     * <br>
     * <strong>Attention</strong>, le paramètre pMois est le mois au sens du Calendar java
     * <i>i.e.</i> Calendar.JANUARY = 0, .....
     * @param pAnnee une année comprise entre 0 et 10000.
     * @param pMois  Un mois au sens java <i>i.e.</i> Calendar.JANUARY = 0, ..
     * @return Un tableau (qui peut être vide) contenant les jours fériés du mois au format DTD (yyyyMMdd).
     */
    int[] getJoursDTDEtatFermesDuMois(int pAnnee, int pMois);

    /**
     * Cette méthode retourne les jours fériés d'un mois donné et d'une année donnée.
     * <br>
     * <strong>Attention</strong>, le paramètre pMois est le mois au sens du Calendar java
     * <i>i.e.</i> Calendar.JANUARY = 0, .....
     * @param pAnnee une année comprise entre 0 et 10000.
     * @param pMois  Un mois au sens java <i>i.e.</i> Calendar.JANUARY = 0, ..
     * @return Un tableau (qui peut être vide) contenant les jours fériés du mois.
     */
    Date[] getJoursFeriesDuMois(int pAnnee, int pMois);

    /**
     * Cette méthode retourne les jours fériés d'un mois donné et d'une année donnée.
     * <br>
     * <strong>Attention</strong>, le paramètre pMois est le mois au sens du Calendar java
     * <i>i.e.</i> Calendar.JANUARY = 0, .....
     * @param pAnnee une année comprise entre 0 et 10000.
     * @param pMois  Un mois au sens java <i>i.e.</i> Calendar.JANUARY = 0, ..
     * @return Un tableau (qui peut être vide) contenant les jours fériés du mois au format DTD (yyyyMMdd).
     */
    int[] getJoursDTDFeriesDuMois(int pAnnee, int pMois);

    /**
     * Cette méthode retourne la date passée en paramètre (pDate) à laquelle on a ajouté pNbreJoursOuvrables
     * jours ouvrables. Les jours ouvrables sont les jours de la semaine excepté le samedi, le dimanche, les jours
     * fériés et les jours fermés à l'État. Par exemple, si pDate représente le 23 décembre 2004, l'appel
     * addJoursOuvrables(pDate,6) doit retourner le 10 janvier 2005.
     * @param pDate               La date à laquelle on veut ajouter des jours ouvrables
     * @param pNbreJoursOuvrables Le nombre de jours ouvrables à ajouter ou soustraire.
     * @return pDate augmentàe de pNbreJoursOuvrables
     */
    Date addJoursEtatOuvrables(final Date pDate, final int pNbreJoursOuvrables);

    /**
     * Cette méthode retourne la date passée en paramètre (pDate) à laquelle on a ajouté pNbreJoursOuvrables
     * jours ouvrables. Les jours ouvrables sont les jours de la semaine excepté le samedi, le dimanche, les jours
     * fériés et les jours fermés à l'État. Par exemple, si pDate représente le 23 décembre 2004, l'appel
     * addJoursOuvrables(pDate,6) doit retourner le 10 janvier 2005.
     * @param pDTDDate            Un entier : la date au format DTD (yyyyMMdd) à laquelle on veut ajouter des jours
     *                            ouvrables
     * @param pNbreJoursOuvrables Le nombre de jours ouvrables à ajouter. Ce nombre doit être &gt;=0.
     * @return Un entier : pDate augmentàe de pNbreJoursOuvrables au format DTD (yyyyMMdd)
     */
    int addJoursEtatOuvrables(final int pDTDDate, final int pNbreJoursOuvrables);

    /**
     * Cette méthode retourne la date passée en paramètre (pDate) à laquelle on a ajouté pNbreJoursOuvrables
     * jours ouvrables. Les jours ouvrables sont les jours de la semaine exceptés le samedi, le dimanche et les jours
     * fériés officiels. Par exemple, si pDate représente le 23 décembre 2004, l'appel
     * addJoursOuvrables(pDate,6) doit retourner le 2 janvier 2005.
     * @param pDate               La date à laquelle on veut ajouter des jours ouvrables
     * @param pNbreJoursOuvrables Le nombre de jours ouvrables à ajouter ou soustraire
     * @return pDate augmentàe de pNbreJoursOuvrables
     */
    Date addJoursOuvrables(final Date pDate, final int pNbreJoursOuvrables);

    /**
     * Cette méthode retourne la date passée en paramètre (pDate) à laquelle on a ajouté pNbreJoursOuvrables
     * jours ouvrables. Les jours ouvrables sont les jours de la semaine exceptés le samedi, le dimanche et les jours
     * fériés officiels. Par exemple, si pDate représente le 23 décembre 2004, l'appel
     * addJoursOuvrables(pDate,6) doit retourner le 2 janvier 2005.
     * @param pDTDDate            Un entier : la date au format DTD (yyyyMMdd) à laquelle on veut ajouter des jours
     *                            ouvrables
     * @param pNbreJoursOuvrables Le nombre de jours ouvrables à ajouter. Ce nombre doit être &gt;=0.
     * @return Un entier : pDate augmentàe de pNbreJoursOuvrables au format DTD (yyyyMMdd)
     */
    int addJoursOuvrables(final int pDTDDate, final int pNbreJoursOuvrables);

    /**
     * Cette méthode retourne la prochaine date suivant la date donnée en paramètre qui est un jour ouvrable. Si
     * la date donnée en paramètre est un jour ouvrable, c'est cette même date qui est retournée.
     * @param pDate La date à partir de laquelle on veut le prochain jour ouvrable
     * @return La date suivant la date donnée en paramètre qui est ouvrable.
     */
    Date getProchainJourOuvrable(final Date pDate);

    /**
     * Cette méthode retourne la prochaine date suivant la date donnée en paramètre qui est un jour ouvrable. Si
     * la date donnée en paramètre est un jour ouvrable, c'est cette même date qui est retournée.
     * @param pDTDDate Un entier : la date au format DTD (yyyyMMdd) à partir de laquelle on veut le prochain jour
     *                 ouvrable
     * @return La date au format DTD (yyyyMMdd) suivant la date donnée en paramètre qui est ouvrable.
     */
    int getProchainJourOuvrable(final int pDTDDate);

    /**
     * Cette méthode retourne la prochaine date suivant la date donnée en paramètre qui est un jour ouvrable à
     * l'État de Genàve. Si la date donnée en paramètre est un jour ouvrable, c'est cette même date qui est
     * retournée.
     * @param pDate La date à partir de laquelle on veut le prochain jour ouvrable (pour l'État)
     * @return La date suivant la date donnée en paramètre qui est ouvrable.
     */
    Date getProchainJourEtatOuvrable(final Date pDate);

    /**
     * Cette méthode retourne la prochaine date suivant la date donnée en paramètre qui est un jour ouvrable à
     * l'État de Genàve. Si la date donnée en paramètre est un jour ouvrable, c'est cette même date qui est
     * retournée.
     * @param pDTDDate Un entier : la date au format DTD (yyyyMMdd) à partir de laquelle on veut le prochain jour
     *                 ouvrable (pour l'État)
     * @return La date au format DTD (yyyyMMdd) suivant la date donnée en paramètre qui est ouvrable.
     */
    int getProchainJourEtatOuvrable(final int pDTDDate);

}
