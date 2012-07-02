package ch.ge.cti.ct.FerieGeneve.persistance;

public interface ParamFermesAble {
	/**
	 * Retourne la liste des jours états fermés pour une année données
	 * @param annee
	 * @return la liste des jours fermés au format dd/MM
	 */
	public String[] getJoursFermes(int annee);
}
