package ch.ge.cti.ct.FerieGeneve.persistance;

/**
 * <p>
 * Cette interface est utilisée pour lire les données paramétrables.
 * </p>
 * @author pinaudj
 */
public interface ParamFermesAble {

    /**
     * Retourne la liste des jours fermés à l´État, pour une année donnée.
     * @return la liste des jours fermés au format dd/MM
     */
    public String[] getJoursFermes(int annee);
}
