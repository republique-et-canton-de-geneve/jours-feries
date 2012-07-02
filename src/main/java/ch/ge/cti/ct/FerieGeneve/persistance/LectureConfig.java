package ch.ge.cti.ct.FerieGeneve.persistance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * Cette classe permet la lecture du Distribution.properties afin d'y trouver les dâtes des jours états fermés
 * @author pinaudj
 */
public final class LectureConfig {
	private static Properties prop;
	private static final Logger LOG = Logger.getLogger(LectureConfig.class);

	static {
		prop = init();
	}

	/**
	 * constructeur caché
	 */
	private LectureConfig() {

	}

	/**
	 * Retourne la liste des jours états fermés pour une année données
	 * @param annee
	 * @return
	 */
	public static String[] getFerie(int annee) {
		String[] tab = new String[0];

		// obtention des valeurs pour l'année donnée
		String valeurs = prop.getProperty("JOURS_FERMETURE_ETAT_" + Integer.toString(annee));

		//parsing
		if (valeurs != null && !valeurs.equals("")) {
			tab = valeurs.split(";");
		}

		return tab;
	}

	/**
	 * Initialise les properties
	 * (lecture de Distribution.properties)
	 * @return
	 */
	private static Properties init() {
		String base = System.getProperty("jonas.base");
		File file = null;
		Properties propT = new Properties();

		// si pas de jonas.base dans les propriétés système on cherche distribution.properties
		if (base==null || base.equals("")) {
			base = System.getProperty("distribution.properties");
			if (base!=null) {
				file = new File(base);
			}
			else {
				return propT;
			}
		}
		// sinon on charge le fichier
		else {
			file = new File(base + "/Distribution.properties");
		}
		
		// remplissage du Properties
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			propT.load(is);
		}
		catch (FileNotFoundException e) {
			LOG.error("fichier Distribution.properties non trouvé", e);
		}
		catch (IOException e) {
			LOG.error("erreur lecture fichier Distribution.properties", e);
		}
		finally {
			try {
				is.close();
			}
			catch (IOException e) {
				LOG.error("erreur fermeture fichier Distribution.properties", e);
			}
		}

		return propT;
	}

}
