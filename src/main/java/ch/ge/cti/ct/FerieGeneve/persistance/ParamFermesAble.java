package ch.ge.cti.ct.FerieGeneve.persistance;

/**
 * <p>
 * Cet(te) interface enum  est utilis�e pour lire les donn�es param�trables
 * </p>
 * <p>
 * <b>� Copyright 2012 CTI - �tat de Gen�ve.</b>
 * </p>
 * <p>
 * <b>Soci�t�</b> : CTI - �tat de Gen�ve
 * {@link <a href="http://www.ge.ch/cti/welcome.html"> CTI - �tat de Gen�ve </a>}
 * </p>
 * <p>
 * <b>Projet</b> : ct_jourferie_ge
 * </p>
 * <p>
 * <b>Historique des modifications</b> :
 * <br>
 * <br>
 * 2 juil. 2012 - cr�ation du fichier.
 * <br>
 * <!-- date - {@link <a href="">lien vers JIRA</a>} -->
 * <br>
 * </p>
 *
 * @author pinaudj
 */
public interface ParamFermesAble {

    /**
     * Retourne la liste des jours �tats ferm�s pour une ann�e donn�es
     *
     * @param annee
     *
     * @return la liste des jours ferm�s au format dd/MM
     */
    public String[] getJoursFermes(int annee);
}
