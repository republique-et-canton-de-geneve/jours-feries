package ch.ge.cti.ct.FerieGeneve.persistance;

/**
 * <p>
 * Cet(te) interface enum  est utilisée pour lire les données paramètrables
 * </p>
 * <p>
 * <b>© Copyright 2012 CTI - État de Genève.</b>
 * </p>
 * <p>
 * <b>Société</b> : CTI - État de Genève
 * {@link <a href="http://www.ge.ch/cti/welcome.html"> CTI - État de Genève </a>}
 * </p>
 * <p>
 * <b>Projet</b> : ct_jourferie_ge
 * </p>
 * <p>
 * <b>Historique des modifications</b> :
 * <br>
 * <br>
 * 2 juil. 2012 - création du fichier.
 * <br>
 * <!-- date - {@link <a href="">lien vers JIRA</a>} -->
 * <br>
 * </p>
 *
 * @author pinaudj
 */
public interface ParamFermesAble {

    /**
     * Retourne la liste des jours états fermés pour une année données
     *
     * @param annee
     *
     * @return la liste des jours fermés au format dd/MM
     */
    public String[] getJoursFermes(int annee);
}
